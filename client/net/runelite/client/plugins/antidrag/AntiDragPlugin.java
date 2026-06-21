package net.runelite.client.plugins.antidrag;

import com.google.inject.Provides;
import java.awt.event.KeyEvent;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.events.FocusChanged;
import net.runelite.api.events.ScriptPostFired;
import net.runelite.api.events.WidgetLoaded;
import net.runelite.api.widgets.Widget;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.input.KeyListener;
import net.runelite.client.input.KeyManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@PluginDescriptor(
   name = "Anti Drag",
   description = "Prevent dragging an item for a specified delay",
   tags = {"antidrag", "delay", "inventory", "items"},
   enabledByDefault = false
)
public class AntiDragPlugin extends Plugin implements KeyListener {
   private static final Logger log = LoggerFactory.getLogger(AntiDragPlugin.class);
   static final String CONFIG_GROUP = "antiDrag";
   private static final int DEFAULT_DELAY = 5;
   @Inject
   private Client client;
   @Inject
   private ClientThread clientThread;
   @Inject
   private AntiDragConfig config;
   @Inject
   private KeyManager keyManager;
   private boolean shiftHeld;
   private boolean ctrlHeld;

   @Provides
   AntiDragConfig getConfig(ConfigManager configManager) {
      return (AntiDragConfig)configManager.getConfig(AntiDragConfig.class);
   }

   protected void startUp() throws Exception {
      if (this.client.getGameState() == GameState.LOGGED_IN) {
         this.clientThread.invokeLater(() -> {
            if (!this.config.onShiftOnly()) {
               this.setDragDelay();
            }

         });
      }

      this.keyManager.registerKeyListener(this);
   }

   protected void shutDown() throws Exception {
      this.clientThread.invoke(this::resetDragDelay);
      this.keyManager.unregisterKeyListener(this);
   }

   public void keyTyped(KeyEvent e) {
   }

   public void keyPressed(KeyEvent e) {
      if (e.getKeyCode() == 17 && this.config.disableOnCtrl() && !this.config.onShiftOnly()) {
         this.resetDragDelay();
         this.ctrlHeld = true;
      } else if (e.getKeyCode() == 16 && this.config.onShiftOnly()) {
         this.setDragDelay();
         this.shiftHeld = true;
      }

   }

   public void keyReleased(KeyEvent e) {
      if (e.getKeyCode() == 17 && this.config.disableOnCtrl() && !this.config.onShiftOnly()) {
         this.setDragDelay();
         this.ctrlHeld = false;
      } else if (e.getKeyCode() == 16 && this.config.onShiftOnly()) {
         this.resetDragDelay();
         this.shiftHeld = false;
      }

   }

   private boolean isOverriding() {
      return (!this.config.onShiftOnly() || this.shiftHeld) && !this.ctrlHeld;
   }

   @Subscribe
   public void onConfigChanged(ConfigChanged event) {
      if (event.getGroup().equals("antiDrag")) {
         if (!this.config.disableOnCtrl()) {
            this.ctrlHeld = false;
         }

         if (this.config.onShiftOnly()) {
            this.shiftHeld = false;
            this.clientThread.invoke(this::resetDragDelay);
         } else {
            this.clientThread.invoke(this::setDragDelay);
         }
      }

   }

   @Subscribe
   public void onFocusChanged(FocusChanged focusChanged) {
      if (!focusChanged.isFocused()) {
         this.shiftHeld = false;
         this.ctrlHeld = false;
         this.clientThread.invoke(this::resetDragDelay);
      } else if (!this.config.onShiftOnly()) {
         this.clientThread.invoke(this::setDragDelay);
      }

   }

   @Subscribe
   public void onWidgetLoaded(WidgetLoaded widgetLoaded) {
      if (this.isOverriding()) {
         if (widgetLoaded.getGroupId() != 12 && widgetLoaded.getGroupId() != 15 && widgetLoaded.getGroupId() != 724 && widgetLoaded.getGroupId() != 725) {
            if (widgetLoaded.getGroupId() == 149) {
               this.setInvDragDelay(this.config.dragDelay());
            }
         } else {
            this.setBankDragDelay(this.config.dragDelay());
         }

      }
   }

   @Subscribe
   private void onScriptPostFired(ScriptPostFired ev) {
      Widget child;
      if (ev.getScriptId() == 6011) {
         child = this.client.getScriptActiveWidget();
         if (child.getParentId() == 786444 || child.getParentId() == 9764864) {
            int delay = this.config.dragDelay();
            boolean overriding = this.isOverriding();
            child.setOnMouseRepeatListener((Object[])null);
            if (overriding) {
               child.setDragDeadTime(delay);
            }
         }
      } else if (ev.getScriptId() == 154) {
         child = this.client.getScriptActiveWidget();
         if (child.getParentId() == 5570560 && this.isOverriding()) {
            child.setDragDeadTime(this.config.dragDelay());
         }
      } else if (ev.getScriptId() == 1607) {
         if (this.isOverriding()) {
            this.setCoxDragDelay(this.config.dragDelay());
         }
      } else if (ev.getScriptId() == 144) {
         if (this.isOverriding()) {
            this.setBankDragDelay(this.config.dragDelay());
         }
      } else if (ev.getScriptId() == 2819 && this.isOverriding()) {
         this.setSeedVaultDragDelay(this.config.dragDelay());
      }

   }

   private static void applyDragDelay(Widget widget, int delay) {
      if (widget != null) {
         Widget[] var2 = widget.getDynamicChildren();
         int var3 = var2.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            Widget item = var2[var4];
            item.setDragDeadTime(delay);
         }
      }

   }

   private void setBankDragDelay(int delay) {
      Widget bankItemContainer = this.client.getWidget(786444);
      Widget bankInventoryItemsContainer = this.client.getWidget(983043);
      Widget groupStorageItems = this.client.getWidget(47448074);
      Widget groupStorageInventoryItems = this.client.getWidget(47513600);
      Widget bankInventoryEquipmentItemsContainer = this.client.getWidget(983044);
      Widget bankDepositContainer = this.client.getWidget(12582936);
      Widget coxPrivateChest = this.client.getWidget(17760262);
      applyDragDelay(bankItemContainer, delay);
      applyDragDelay(bankInventoryItemsContainer, delay);
      applyDragDelay(groupStorageItems, delay);
      applyDragDelay(groupStorageInventoryItems, delay);
      applyDragDelay(bankInventoryEquipmentItemsContainer, delay);
      applyDragDelay(bankDepositContainer, delay);
      applyDragDelay(coxPrivateChest, delay);
   }

   private void setInvDragDelay(int delay) {
      Widget inventory = this.client.getWidget(9764864);
      Widget equipmentInventory = this.client.getWidget(5570560);
      applyDragDelay(inventory, delay);
      applyDragDelay(equipmentInventory, delay);
   }

   private void setCoxDragDelay(int delay) {
      Widget coxChest = this.client.getWidget(17760262);
      applyDragDelay(coxChest, delay);
   }

   private void setSeedVaultDragDelay(int delay) {
      Widget seedVaultItems = this.client.getWidget(41353231);
      Widget seedVaultText = this.client.getWidget(41353232);
      applyDragDelay(seedVaultItems, delay);
      applyDragDelay(seedVaultText, delay);
   }

   private void setDragDelay() {
      int delay = this.config.dragDelay();
      log.debug("Set delay to {}", delay);
      this.client.setInventoryDragDelay(delay);
      this.setInvDragDelay(delay);
      this.setBankDragDelay(delay);
      this.setCoxDragDelay(delay);
      this.setSeedVaultDragDelay(delay);
   }

   private void resetDragDelay() {
      log.debug("Reset delay to {}", 5);
      this.client.setInventoryDragDelay(5);
      this.setInvDragDelay(5);
      this.setBankDragDelay(5);
      this.setCoxDragDelay(5);
      this.setSeedVaultDragDelay(5);
   }
}
