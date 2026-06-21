package net.runelite.client.plugins.fairyring;

import com.google.common.base.Strings;
import com.google.inject.Provides;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.MenuAction;
import net.runelite.api.MenuEntry;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.MenuEntryAdded;
import net.runelite.api.events.ScriptCallbackEvent;
import net.runelite.api.events.VarbitChanged;
import net.runelite.api.events.WidgetLoaded;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetUtil;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.game.chatbox.ChatboxPanelManager;
import net.runelite.client.game.chatbox.ChatboxTextInput;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.util.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@PluginDescriptor(
   name = "Fairy Rings",
   description = "Show the location of the fairy ring teleport",
   tags = {"teleportation"}
)
public class FairyRingPlugin extends Plugin {
   private static final Logger log = LoggerFactory.getLogger(FairyRingPlugin.class);
   private static final String[] leftDial = new String[]{"A", "D", "C", "B"};
   private static final String[] middleDial = new String[]{"I", "L", "K", "J"};
   private static final String[] rightDial = new String[]{"P", "S", "R", "Q"};
   private static final String EDIT_TAGS_MENU_OPTION = "Edit Tags";
   @Inject
   private Client client;
   @Inject
   private FairyRingConfig config;
   @Inject
   private ChatboxPanelManager chatboxPanelManager;
   @Inject
   private ClientThread clientThread;
   @Inject
   private ConfigManager configManager;
   private ChatboxTextInput tagInput;

   @Provides
   FairyRingConfig getConfig(ConfigManager configManager) {
      return (FairyRingConfig)configManager.getConfig(FairyRingConfig.class);
   }

   public void resetConfiguration() {
      List<String> extraKeys = this.configManager.getConfigurationKeys("fairyrings.fairyringtags");
      Iterator var2 = extraKeys.iterator();

      while(var2.hasNext()) {
         String prefix = (String)var2.next();
         List<String> keys = this.configManager.getConfigurationKeys(prefix);
         Iterator var5 = keys.iterator();

         while(var5.hasNext()) {
            String key = (String)var5.next();
            String[] str = key.split("\\.", 2);
            if (str.length == 2) {
               this.configManager.unsetConfiguration(str[0], str[1]);
            }
         }
      }

   }

   @Subscribe
   public void onVarbitChanged(VarbitChanged event) {
      this.setWidgetTextToDestination();
   }

   @Subscribe
   public void onWidgetLoaded(WidgetLoaded widgetLoaded) {
      if (widgetLoaded.getGroupId() == 381) {
         this.setWidgetTextToDestination();
      }

   }

   private void setWidgetTextToDestination() {
      Widget fairyRingTeleportButton = this.client.getWidget(26083354);
      if (fairyRingTeleportButton != null && !fairyRingTeleportButton.isHidden()) {
         String destination;
         try {
            FairyRing fairyRingDestination = this.getFairyRingDestination(this.client.getVarbitValue(3985), this.client.getVarbitValue(3986), this.client.getVarbitValue(3987));
            destination = fairyRingDestination.getDestination();
         } catch (IllegalArgumentException var4) {
            destination = "Invalid location";
         }

         fairyRingTeleportButton.setText(destination);
      }

   }

   private void openSearch() {
      Widget widget = this.client.getWidget(24969223);
      if (widget != null) {
         this.client.setVarcStrValue(359, "");
         this.client.createScriptEventBuilder(widget.getOnOpListener()).setOp(1).build().run();
      }

   }

   @Subscribe
   private void onScriptCallbackEvent(ScriptCallbackEvent ev) {
      String code = null;
      if ("fairyringFilterFavorite".equals(ev.getEventName())) {
         Widget widget = this.client.getWidget(this.client.getIntStack()[this.client.getIntStackSize() - 2]);
         if (widget != null) {
            code = widget.getText();
         }
      } else if ("fairyringFilterDbrow".equals(ev.getEventName())) {
         code = (String)this.client.getDBTableField(this.client.getIntStack()[this.client.getIntStackSize() - 2], 3, 0)[0];
      }

      if (code != null && !code.isEmpty()) {
         code = code.replace(" ", "");
         String tags = null;
         FairyRing ring = FairyRing.forCode(code);
         if (ring != null) {
            tags = ring.getTags();
         }

         String filter = this.client.getVarcStrValue(1344).toLowerCase();
         if (code.toLowerCase().contains(filter) || tags != null && tags.contains(filter) || this.getConfigTags(code).stream().anyMatch((s) -> {
            return s.contains(filter);
         })) {
            this.client.getIntStack()[this.client.getIntStackSize() - 1] = 1;
         }
      }

   }

   @Subscribe
   public void onGameTick(GameTick t) {
      Widget fairyRingTeleportButton = this.client.getWidget(26083354);
      boolean fairyRingWidgetOpen = fairyRingTeleportButton != null && !fairyRingTeleportButton.isHidden();
      boolean tagInputBoxOpen = this.tagInput != null && this.chatboxPanelManager.getCurrentInput() == this.tagInput;
      if (!fairyRingWidgetOpen && tagInputBoxOpen) {
         this.chatboxPanelManager.close();
      }

   }

   private FairyRing getFairyRingDestination(int varbitValueDialLeft, int varbitValueDialMiddle, int varbitValueDialRight) {
      String var10000 = leftDial[varbitValueDialLeft];
      return FairyRing.valueOf(var10000 + middleDial[varbitValueDialMiddle] + rightDial[varbitValueDialRight]);
   }

   @Subscribe
   public void onMenuEntryAdded(MenuEntryAdded event) {
      if (WidgetUtil.componentToInterface(event.getActionParam1()) == 381 && event.getOption().equals("Use code") && !event.getTarget().isEmpty()) {
         this.client.getMenu().createMenuEntry(-1).setParam0(event.getActionParam0()).setParam1(event.getActionParam1()).setTarget(event.getTarget()).setOption("Edit Tags").setType(MenuAction.RUNELITE).setIdentifier(event.getIdentifier()).onClick(this::setTagMenuOpen);
      }

   }

   private List<String> getConfigTags(String fairyRingCode) {
      String config = ((String)Optional.ofNullable(this.configManager.getConfiguration("fairyrings.fairyringtags", fairyRingCode)).orElse("")).toLowerCase();
      return Text.fromCSV(config);
   }

   private void setConfigTags(String fairyRingCode, String tags) {
      if (Strings.isNullOrEmpty(tags)) {
         this.configManager.unsetConfiguration("fairyrings.fairyringtags", fairyRingCode);
      } else {
         this.configManager.setConfiguration("fairyrings.fairyringtags", fairyRingCode, tags);
      }

   }

   private void setTagMenuOpen(MenuEntry menuEntry) {
      String code = Text.removeTags(menuEntry.getTarget()).replaceAll(" ", "");
      String initialValue = Text.toCSV(this.getConfigTags(code));
      this.client.playSoundEffect(2266);
      this.tagInput = this.chatboxPanelManager.openTextInput("Code " + code + " tags:").value(initialValue).onDone((s) -> {
         this.setConfigTags(code, s);
         this.clientThread.invokeLater(() -> {
            this.clientThread.invokeLater(this::openSearch);
         });
      }).build();
   }
}
