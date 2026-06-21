package net.runelite.client.plugins.mta;

import com.google.inject.Provides;
import javax.inject.Inject;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.EventBus;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.mta.alchemy.AlchemyRoom;
import net.runelite.client.plugins.mta.enchantment.EnchantmentRoom;
import net.runelite.client.plugins.mta.graveyard.GraveyardRoom;
import net.runelite.client.plugins.mta.telekinetic.TelekineticRoom;
import net.runelite.client.ui.overlay.OverlayManager;

@PluginDescriptor(
   name = "Mage Training Arena",
   description = "Show helpful information for the Mage Training Arena minigame",
   tags = {"mta", "magic", "minigame", "overlay"},
   enabledByDefault = false
)
public class MTAPlugin extends Plugin {
   @Inject
   private OverlayManager overlayManager;
   @Inject
   private AlchemyRoom alchemyRoom;
   @Inject
   private GraveyardRoom graveyardRoom;
   @Inject
   private TelekineticRoom telekineticRoom;
   @Inject
   private EnchantmentRoom enchantmentRoom;
   @Inject
   private EventBus eventBus;
   @Inject
   private MTASceneOverlay sceneOverlay;
   @Inject
   private MTAItemOverlay itemOverlay;
   private MTARoom[] rooms;

   @Provides
   public MTAConfig getConfig(ConfigManager manager) {
      return (MTAConfig)manager.getConfig(MTAConfig.class);
   }

   public void startUp() {
      this.overlayManager.add(this.sceneOverlay);
      this.overlayManager.add(this.itemOverlay);
      this.rooms = new MTARoom[]{this.alchemyRoom, this.graveyardRoom, this.telekineticRoom, this.enchantmentRoom};
      MTARoom[] var1 = this.rooms;
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         MTARoom room = var1[var3];
         this.eventBus.register(room);
      }

   }

   public void shutDown() {
      this.overlayManager.remove(this.sceneOverlay);
      this.overlayManager.remove(this.itemOverlay);
      MTARoom[] var1 = this.rooms;
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         MTARoom room = var1[var3];
         this.eventBus.unregister((Object)room);
      }

      this.telekineticRoom.resetRoom();
   }

   protected MTARoom[] getRooms() {
      return this.rooms;
   }
}
