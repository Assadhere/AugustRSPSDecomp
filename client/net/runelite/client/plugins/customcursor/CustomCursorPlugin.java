package net.runelite.client.plugins.customcursor;

import com.google.inject.Provides;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.EquipmentInventorySlot;
import net.runelite.api.Item;
import net.runelite.api.ItemContainer;
import net.runelite.api.events.ItemContainerChanged;
import net.runelite.client.RuneLite;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.game.ItemManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.ClientUI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@PluginDescriptor(
   name = "Custom Cursor",
   description = "Replaces your mouse cursor image",
   enabledByDefault = false
)
public class CustomCursorPlugin extends Plugin {
   private static final Logger log = LoggerFactory.getLogger(CustomCursorPlugin.class);
   private static final File CUSTOM_IMAGE_FILE;
   @Inject
   private Client client;
   @Inject
   private ClientThread clientThread;
   @Inject
   private ClientUI clientUI;
   @Inject
   private CustomCursorConfig config;
   @Inject
   private ItemManager itemManager;

   @Provides
   CustomCursorConfig provideConfig(ConfigManager configManager) {
      return (CustomCursorConfig)configManager.getConfig(CustomCursorConfig.class);
   }

   protected void startUp() {
      this.updateCursor();
   }

   protected void shutDown() {
      this.clientUI.resetCursor();
   }

   @Subscribe
   public void onConfigChanged(ConfigChanged event) {
      if (event.getGroup().equals("customcursor") && event.getKey().equals("cursorStyle")) {
         this.updateCursor();
      }

   }

   @Subscribe
   public void onItemContainerChanged(ItemContainerChanged event) {
      if (this.config.selectedCursor() == CustomCursor.EQUIPPED_WEAPON && event.getContainerId() == 94) {
         this.updateCursor();
      }

   }

   private void updateCursor() {
      CustomCursor selectedCursor = this.config.selectedCursor();
      if (selectedCursor == CustomCursor.CUSTOM_IMAGE) {
         if (CUSTOM_IMAGE_FILE.exists()) {
            try {
               Class var3 = ImageIO.class;
               BufferedImage image;
               synchronized(ImageIO.class) {
                  image = ImageIO.read(CUSTOM_IMAGE_FILE);
               }

               this.clientUI.setCursor(image, selectedCursor.getName());
            } catch (Exception var6) {
               Exception e = var6;
               log.error("error setting custom cursor", e);
               this.clientUI.resetCursor();
            }
         } else {
            this.clientUI.resetCursor();
         }
      } else if (selectedCursor == CustomCursor.EQUIPPED_WEAPON) {
         this.clientThread.invokeLater(() -> {
            ItemContainer equipment = this.client.getItemContainer(94);
            if (equipment == null) {
               this.clientUI.resetCursor();
            } else {
               Item weapon = equipment.getItem(EquipmentInventorySlot.WEAPON.getSlotIdx());
               if (weapon == null) {
                  this.clientUI.resetCursor();
               } else {
                  BufferedImage image = this.itemManager.getImage(weapon.getId());
                  if (weapon.getQuantity() > 0) {
                     this.clientUI.setCursor(image, selectedCursor.getName());
                  } else {
                     this.clientUI.resetCursor();
                  }

               }
            }
         });
      } else {
         assert selectedCursor.getCursorImage() != null;

         this.clientUI.setCursor(selectedCursor.getCursorImage(), selectedCursor.getName());
      }

   }

   static {
      CUSTOM_IMAGE_FILE = new File(RuneLite.RUNELITE_DIR, "cursor.png");
   }
}
