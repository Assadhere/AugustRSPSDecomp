package net.runelite.client.plugins.itemstats;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.inject.Binder;
import com.google.inject.Inject;
import com.google.inject.Provides;
import java.awt.FontMetrics;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.runelite.api.Client;
import net.runelite.api.ItemContainer;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.ScriptPostFired;
import net.runelite.api.events.VarbitChanged;
import net.runelite.api.widgets.Widget;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.game.ItemEquipmentStats;
import net.runelite.client.game.ItemManager;
import net.runelite.client.game.ItemStats;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.FontManager;
import net.runelite.client.ui.JagexColors;
import net.runelite.client.ui.overlay.OverlayManager;
import net.runelite.client.util.QuantityFormatter;

@PluginDescriptor(
   name = "Item Stats",
   description = "Show information about food and potion effects",
   tags = {"food", "inventory", "overlay", "potion"}
)
public class ItemStatPlugin extends Plugin {
   private static final int ORANGE_TEXT;
   private static final int YELLOW_TEXT;
   private static final int TEXT_HEIGHT = 11;
   @Inject
   private OverlayManager overlayManager;
   @Inject
   private ItemStatOverlay overlay;
   @Inject
   private Client client;
   @Inject
   private ItemManager itemManager;
   @Inject
   private ItemStatConfig config;
   @Inject
   private ClientThread clientThread;
   private Widget itemInformationTitle;

   @Provides
   ItemStatConfig getConfig(ConfigManager configManager) {
      return (ItemStatConfig)configManager.getConfig(ItemStatConfig.class);
   }

   public void configure(Binder binder) {
      binder.bind(ItemStatChangesService.class).to(ItemStatChangesServiceImpl.class);
   }

   protected void startUp() throws Exception {
      this.overlayManager.add(this.overlay);
   }

   protected void shutDown() throws Exception {
      this.overlayManager.remove(this.overlay);
      this.clientThread.invokeLater(this::resetGEInventory);
   }

   @Subscribe
   public void onConfigChanged(ConfigChanged event) {
      if (event.getKey().equals("geStats")) {
         this.clientThread.invokeLater(this::resetGEInventory);
      }

   }

   @Subscribe
   public void onGameTick(GameTick event) {
      if (this.itemInformationTitle != null && this.config.geStats() && (this.client.getWidget(30474240) == null || this.client.getWidget(30474240).isHidden())) {
         this.resetGEInventory();
      }

   }

   @Subscribe
   public void onVarbitChanged(VarbitChanged event) {
      if (event.getVarpId() == 1151 && this.config.geStats()) {
         this.resetGEInventory();
      }

   }

   @Subscribe
   public void onScriptPostFired(ScriptPostFired event) {
      if (event.getScriptId() == 779 && this.config.geStats()) {
         int currentGeItem = this.client.getVarpValue(1151);
         if (currentGeItem != -1 && this.client.getVarbitValue(4397) == 0) {
            this.createItemInformation(currentGeItem);
         }
      }

   }

   private void createItemInformation(int id) {
      ItemStats itemStats = this.itemManager.getItemStats(id);
      if (itemStats != null && itemStats.isEquipable()) {
         ItemEquipmentStats equipmentStats = itemStats.getEquipment();
         if (equipmentStats != null) {
            Widget geInv = this.client.getWidget(30605312);
            if (geInv != null) {
               Widget invContainer = this.getInventoryContainer();
               if (invContainer != null) {
                  invContainer.deleteAllChildren();
                  geInv.setHidden(true);
                  int yPos = 0;
                  FontMetrics smallFM = this.client.getCanvas().getFontMetrics(FontManager.getRunescapeSmallFont());
                  this.itemInformationTitle = createText(invContainer, "Item Information", 496, ORANGE_TEXT, 8, 8, invContainer.getWidth(), 16);
                  this.itemInformationTitle.setYTextAlignment(1);
                  Widget closeButton = invContainer.createChild(-1, 5);
                  closeButton.setOriginalY(8);
                  closeButton.setOriginalX(invContainer.getWidth() - 24);
                  closeButton.setOriginalHeight(16);
                  closeButton.setOriginalWidth(16);
                  closeButton.setSpriteId(831);
                  closeButton.setAction(0, "Close");
                  closeButton.setOnMouseOverListener(new Object[]{(ev) -> {
                     closeButton.setSpriteId(832);
                  }});
                  closeButton.setOnMouseLeaveListener(new Object[]{(ev) -> {
                     closeButton.setSpriteId(831);
                  }});
                  closeButton.setOnOpListener(new Object[]{(ev) -> {
                     this.resetGEInventory();
                  }});
                  closeButton.setHasListener(true);
                  closeButton.revalidate();
                  yPos += 15;
                  createSeparator(invContainer, yPos);
                  yPos += 25;
                  Widget icon = invContainer.createChild(-1, 5);
                  icon.setOriginalX(8);
                  icon.setOriginalY(yPos);
                  icon.setOriginalWidth(36);
                  icon.setOriginalHeight(32);
                  icon.setItemId(id);
                  icon.setItemQuantityMode(0);
                  icon.setBorderType(1);
                  icon.revalidate();
                  Widget itemName = createText(invContainer, this.itemManager.getItemComposition(id).getName(), 495, ORANGE_TEXT, 50, yPos, invContainer.getWidth() - 40, 30);
                  itemName.setYTextAlignment(1);
                  yPos += 20;
                  createSeparator(invContainer, yPos);
                  yPos += 25;
                  createText(invContainer, "Attack", 494, ORANGE_TEXT, 5, yPos, 50, -1);
                  int defenceXPos = invContainer.getWidth() - (smallFM.stringWidth("Defence") + 5);
                  createText(invContainer, "Defence", 494, ORANGE_TEXT, defenceXPos, yPos, 50, -1);
                  Set<String> stats = ImmutableSet.of("Stab", "Slash", "Crush", "Magic", "Ranged");
                  List<Integer> attackStats = ImmutableList.of(equipmentStats.getAstab(), equipmentStats.getAslash(), equipmentStats.getAcrush(), equipmentStats.getAmagic(), equipmentStats.getArange());
                  List<Integer> defenceStats = ImmutableList.of(equipmentStats.getDstab(), equipmentStats.getDslash(), equipmentStats.getDcrush(), equipmentStats.getDmagic(), equipmentStats.getDrange());
                  int index = 0;

                  String coinText;
                  Widget coinWidget;
                  for(Iterator var16 = stats.iterator(); var16.hasNext(); ++index) {
                     coinText = (String)var16.next();
                     yPos += 13;
                     coinWidget = createText(invContainer, coinText, 494, ORANGE_TEXT, 0, yPos, invContainer.getWidth(), -1);
                     coinWidget.setXTextAlignment(1);
                     createText(invContainer, ((Integer)attackStats.get(index)).toString(), 494, YELLOW_TEXT, 5, yPos, 50, -1);
                     int defenceX = invContainer.getWidth() - (smallFM.stringWidth(((Integer)defenceStats.get(index)).toString()) + 5);
                     createText(invContainer, ((Integer)defenceStats.get(index)).toString(), 494, YELLOW_TEXT, defenceX, yPos, 50, -1);
                  }

                  yPos += 19;
                  Map<String, Object> miscStats = ImmutableMap.of("Strength", equipmentStats.getStr(), "Ranged Strength", equipmentStats.getRstr(), "Magic Damage", equipmentStats.getMdmg(), "Prayer Bonus", equipmentStats.getPrayer());

                  for(Iterator var23 = miscStats.entrySet().iterator(); var23.hasNext(); yPos += 13) {
                     Map.Entry<String, Object> miscStat = (Map.Entry)var23.next();
                     String name = (String)miscStat.getKey();
                     String value = miscStat.getValue().toString();
                     createText(invContainer, name, 494, ORANGE_TEXT, 5, yPos, 50, -1);
                     int valueXPos = invContainer.getWidth() - (smallFM.stringWidth(value) + 5);
                     createText(invContainer, value, 494, YELLOW_TEXT, valueXPos, yPos, 50, -1);
                  }

                  createSeparator(invContainer, invContainer.getHeight() - 40);
                  String var10000 = QuantityFormatter.quantityToStackSize((long)this.getCurrentGP());
                  coinText = "You have " + var10000 + (this.getCurrentGP() == 1 ? " coin." : " coins.");
                  coinWidget = createText(invContainer, coinText, 495, ORANGE_TEXT, 0, invContainer.getHeight() - 18, invContainer.getWidth(), -1);
                  coinWidget.setXTextAlignment(1);
               }
            }
         }
      }
   }

   private static Widget createText(Widget parent, String text, int fontId, int textColor, int x, int y, int width, int height) {
      Widget widget = parent.createChild(-1, 4);
      widget.setText(text);
      widget.setFontId(fontId);
      widget.setTextColor(textColor);
      widget.setTextShadowed(true);
      widget.setOriginalHeight(height == -1 ? 11 : height);
      widget.setOriginalWidth(width);
      widget.setOriginalY(y);
      widget.setOriginalX(x);
      widget.revalidate();
      return widget;
   }

   private static void createSeparator(Widget parent, int y) {
      Widget separator = parent.createChild(-1, 5);
      separator.setOriginalWidth(parent.getWidth());
      separator.setOriginalY(y);
      separator.setOriginalHeight(32);
      separator.setSpriteId(995);
      separator.revalidate();
   }

   private void resetGEInventory() {
      Widget invContainer = this.getInventoryContainer();
      if (invContainer != null) {
         if (this.itemInformationTitle != null && invContainer.getChild(0) == this.itemInformationTitle) {
            invContainer.deleteAllChildren();
            this.itemInformationTitle = null;
         }

         Widget geInv = this.client.getWidget(30605312);
         if (geInv != null) {
            geInv.setHidden(false);
         }

      }
   }

   private int getCurrentGP() {
      ItemContainer inventory = this.client.getItemContainer(93);
      return inventory == null ? 0 : inventory.count(995);
   }

   private Widget getInventoryContainer() {
      if (this.client.isResized()) {
         return this.client.getVarbitValue(4607) == 1 ? this.client.getWidget(10747980) : this.client.getWidget(10551375);
      } else {
         return this.client.getWidget(35913812);
      }
   }

   static {
      ORANGE_TEXT = JagexColors.DARK_ORANGE_INTERFACE_TEXT.getRGB();
      YELLOW_TEXT = JagexColors.YELLOW_INTERFACE_TEXT.getRGB();
   }
}
