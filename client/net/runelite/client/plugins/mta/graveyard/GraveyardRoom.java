package net.runelite.client.plugins.mta.graveyard;

import java.awt.image.BufferedImage;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.Item;
import net.runelite.api.ItemContainer;
import net.runelite.api.Player;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.ItemContainerChanged;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.game.ItemManager;
import net.runelite.client.plugins.mta.MTAConfig;
import net.runelite.client.plugins.mta.MTAPlugin;
import net.runelite.client.plugins.mta.MTARoom;
import net.runelite.client.ui.overlay.infobox.InfoBoxManager;

public class GraveyardRoom extends MTARoom {
   private static final int MTA_GRAVEYARD_REGION = 13462;
   static final int MIN_SCORE = 16;
   private final Client client;
   private final MTAPlugin plugin;
   private final ItemManager itemManager;
   private final InfoBoxManager infoBoxManager;
   private int score;
   private GraveyardCounter counter;

   @Inject
   private GraveyardRoom(MTAConfig config, Client client, MTAPlugin plugin, ItemManager itemManager, InfoBoxManager infoBoxManager) {
      super(config);
      this.client = client;
      this.plugin = plugin;
      this.itemManager = itemManager;
      this.infoBoxManager = infoBoxManager;
   }

   public boolean inside() {
      Player player = this.client.getLocalPlayer();
      return player != null && player.getWorldLocation().getRegionID() == 13462 && player.getWorldLocation().getPlane() == 1;
   }

   @Subscribe
   public void onGameTick(GameTick tick) {
      if ((!this.inside() || !this.config.graveyard()) && this.counter != null) {
         this.infoBoxManager.removeIf((e) -> {
            return e instanceof GraveyardCounter;
         });
         this.counter = null;
      }

   }

   @Subscribe
   public void onItemContainerChanged(ItemContainerChanged event) {
      if (this.inside()) {
         ItemContainer container = event.getItemContainer();
         if (container == this.client.getItemContainer(93)) {
            this.score = this.score(container.getItems());
            if (this.counter == null) {
               BufferedImage image = this.itemManager.getImage(6904);
               this.counter = new GraveyardCounter(image, this.plugin);
               this.infoBoxManager.addInfoBox(this.counter);
            }

            this.counter.setCount(this.score);
         }

      }
   }

   private int score(Item[] items) {
      int score = 0;
      if (items == null) {
         return score;
      } else {
         Item[] var3 = items;
         int var4 = items.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            Item item = var3[var5];
            score += this.getPoints(item.getId());
         }

         return score;
      }
   }

   private int getPoints(int id) {
      switch (id) {
         case 6904:
            return 1;
         case 6905:
            return 2;
         case 6906:
            return 3;
         case 6907:
            return 4;
         default:
            return 0;
      }
   }
}
