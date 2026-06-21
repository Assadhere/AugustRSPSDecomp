package net.runelite.client.game;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.ImmutableMap;
import com.google.inject.Inject;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Named;
import javax.inject.Singleton;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.ItemComposition;
import net.runelite.api.SpritePixels;
import net.runelite.api.events.GameStateChanged;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.RuneLiteConfig;
import net.runelite.client.eventbus.EventBus;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.util.AsyncBufferedImage;
import net.runelite.http.api.item.ItemPrice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
public class ItemManager {
   private static final Logger log = LoggerFactory.getLogger(ItemManager.class);
   private final Client client;
   private final ClientThread clientThread;
   private final ItemClient itemClient;
   private final RuneLiteConfig runeLiteConfig;
   private final ScheduledExecutorService executor;
   @Inject(
      optional = true
   )
   @Named("activePriceThreshold")
   private double activePriceThreshold = 5.0;
   @Inject(
      optional = true
   )
   @Named("lowPriceThreshold")
   private int lowPriceThreshold = 1000;
   private Map<Integer, ItemPrice> itemPrices = Collections.emptyMap();
   private Instant itemPricesTime;
   private Map<Integer, ItemStats> itemStats = Collections.emptyMap();
   private final LoadingCache<ImageKey, AsyncBufferedImage> itemImages;
   private final LoadingCache<OutlineKey, BufferedImage> itemOutlines;
   private static final ImmutableMap<Integer, Integer> WORN_ITEMS = ImmutableMap.builder().put(89, 88).put(10554, 10553).put(11851, 11850).put(11853, 11852).put(11855, 11854).put(11857, 11856).put(11859, 11858).put(11861, 11860).put(13580, 13579).put(13582, 13581).put(13584, 13583).put(13586, 13585).put(13588, 13587).put(13590, 13589).put(13592, 13591).put(13594, 13593).put(13596, 13595).put(13598, 13597).put(13600, 13599).put(13602, 13601).put(13604, 13603).put(13606, 13605).put(13608, 13607).put(13610, 13609).put(13612, 13611).put(13614, 13613).put(13616, 13615).put(13618, 13617).put(13620, 13619).put(13622, 13621).put(13624, 13623).put(13626, 13625).put(13628, 13627).put(13630, 13629).put(13632, 13631).put(13634, 13633).put(13636, 13635).put(13638, 13637).put(13668, 13667).put(13670, 13669).put(13672, 13671).put(13674, 13673).put(13676, 13675).put(13678, 13677).put(21063, 21061).put(21066, 21064).put(21069, 21067).put(21072, 21070).put(21075, 21073).put(21078, 21076).put(24745, 24743).put(24748, 24746).put(24751, 24749).put(24754, 24752).put(24757, 24755).put(24760, 24758).put(25071, 25069).put(25074, 25072).put(25077, 25075).put(25080, 25078).put(25083, 25081).put(25086, 25084).put(27446, 27444).put(27449, 27447).put(27452, 27450).put(27455, 27453).put(27458, 27456).put(27461, 27459).put(30047, 30045).put(30050, 30048).put(30053, 30051).put(30056, 30054).put(30059, 30057).put(30062, 30060).put(13342, 13280).put(10073, 10069).put(10074, 10071).put(13341, 9772).put(13340, 9771).put(28839, 10053).put(28842, 10055).put(28845, 10057).put(28848, 10059).put(28851, 10061).put(28854, 10063).put(28857, 10065).put(28860, 10067).build();

   @Inject
   private ItemManager(Client client, ScheduledExecutorService scheduledExecutorService, ClientThread clientThread, EventBus eventBus, ItemClient itemClient, RuneLiteConfig runeLiteConfig) {
      this.client = client;
      this.clientThread = clientThread;
      this.itemClient = itemClient;
      this.runeLiteConfig = runeLiteConfig;
      this.executor = scheduledExecutorService;
      eventBus.register(this);
      scheduledExecutorService.scheduleWithFixedDelay(this::refreshPrices, 0L, 30L, TimeUnit.MINUTES);
      scheduledExecutorService.submit(this::loadStats);
      this.itemImages = CacheBuilder.newBuilder().maximumSize(128L).expireAfterAccess(1L, TimeUnit.HOURS).build(new CacheLoader<ImageKey, AsyncBufferedImage>() {
         public AsyncBufferedImage load(ImageKey key) throws Exception {
            return ItemManager.this.loadImage(key.itemId, key.itemQuantity, key.stackable);
         }
      });
      this.itemOutlines = CacheBuilder.newBuilder().maximumSize(128L).expireAfterAccess(1L, TimeUnit.HOURS).build(new CacheLoader<OutlineKey, BufferedImage>() {
         public BufferedImage load(OutlineKey key) throws Exception {
            return ItemManager.this.loadItemOutline(key.itemId, key.itemQuantity, key.outlineColor);
         }
      });
   }

   @Subscribe
   private void onGameStateChanged(GameStateChanged event) {
      if (event.getGameState().getState() >= GameState.LOGGING_IN.getState() && (this.itemPricesTime == null || this.itemPricesTime.isBefore(Instant.now().minus(30L, ChronoUnit.MINUTES)))) {
         log.debug("Recaching prices due to login");
         this.itemPricesTime = Instant.now();
         this.executor.execute(this::loadPrices);
      }

   }

   private void refreshPrices() {
      if (this.itemPricesTime == null || this.client.getGameState().getState() >= GameState.LOGGING_IN.getState()) {
         this.itemPricesTime = Instant.now();
         this.loadPrices();
      }

   }

   private void loadPrices() {
      try {
         ItemPrice[] prices = this.itemClient.getPrices();
         if (prices != null) {
            ImmutableMap.Builder<Integer, ItemPrice> map = ImmutableMap.builderWithExpectedSize(prices.length);
            ItemPrice[] var3 = prices;
            int var4 = prices.length;
            int var5 = 0;

            while(true) {
               if (var5 >= var4) {
                  this.itemPrices = map.build();
                  break;
               }

               ItemPrice price = var3[var5];
               map.put(price.getId(), price);
               ++var5;
            }
         }

         log.debug("Loaded {} prices", this.itemPrices.size());
      } catch (IOException var7) {
         IOException e = var7;
         log.warn("error loading prices!", e);
      }

   }

   private void loadStats() {
      try {
         Map<Integer, ItemStats> stats = this.itemClient.getStats();
         if (stats != null) {
            this.itemStats = ImmutableMap.copyOf(stats);
         }

         log.debug("Loaded {} stats", this.itemStats.size());
      } catch (IOException var2) {
         IOException e = var2;
         log.warn("error loading stats!", e);
      }

   }

   public int getItemPrice(int itemID) {
      return this.getItemPriceWithSource(itemID, this.runeLiteConfig.useWikiItemPrices());
   }

   public int getItemPriceWithSource(int itemID, boolean useWikiPrice) {
      if (itemID == 995) {
         return 1;
      } else if (itemID == 13204) {
         return 1000;
      } else {
         ItemComposition itemComposition = this.getItemComposition(itemID);
         if (itemComposition.getNote() != -1) {
            itemID = itemComposition.getLinkedNoteId();
         }

         itemID = (Integer)WORN_ITEMS.getOrDefault(itemID, itemID);
         int price = 0;
         Collection<ItemMapping> mappedItems = ItemMapping.map(itemID);
         ItemMapping mappedItem;
         if (mappedItems == null) {
            ItemPrice ip = (ItemPrice)this.itemPrices.get(itemID);
            if (ip != null) {
               price = useWikiPrice ? this.getWikiPrice(ip) : ip.getPrice();
            }
         } else {
            for(Iterator var8 = mappedItems.iterator(); var8.hasNext(); price = (int)((long)price + (long)this.getItemPriceWithSource(mappedItem.getTradeableItem(), useWikiPrice) * mappedItem.getQuantity())) {
               mappedItem = (ItemMapping)var8.next();
            }
         }

         return price;
      }
   }

   public int getWikiPrice(ItemPrice itemPrice) {
      int wikiPrice = itemPrice.getWikiPrice();
      int jagPrice = itemPrice.getPrice();
      if (wikiPrice <= 0) {
         return jagPrice;
      } else if (wikiPrice <= this.lowPriceThreshold) {
         return wikiPrice;
      } else {
         return (double)wikiPrice < (double)jagPrice * this.activePriceThreshold ? wikiPrice : jagPrice;
      }
   }

   @Nullable
   public ItemStats getItemStats(int itemId) {
      ItemComposition itemComposition = this.getItemComposition(itemId);
      return itemComposition.getName() != null && itemComposition.getNote() == -1 ? (ItemStats)this.itemStats.get(this.canonicalize(itemId)) : null;
   }

   public void addCustomItemStats(Map<Integer, ItemStats> customStats) {
      Map<Integer, ItemStats> merged = new HashMap(this.itemStats);
      merged.putAll(customStats);
      this.itemStats = merged;
   }

   /** @deprecated */
   @Nullable
   @Deprecated
   public net.runelite.http.api.item.ItemStats getItemStats(int itemId, boolean allowNote) {
      ItemComposition itemComposition = this.getItemComposition(itemId);
      if (itemComposition.getName() != null && (allowNote || itemComposition.getNote() == -1)) {
         ItemStats stats = (ItemStats)this.itemStats.get(this.canonicalize(itemId));
         return stats != null ? stats.toHttpApiFormat() : null;
      } else {
         return null;
      }
   }

   public List<ItemPrice> search(String itemName) {
      itemName = itemName.toLowerCase();
      List<ItemPrice> result = new ArrayList();
      Iterator var3 = this.itemPrices.values().iterator();

      while(var3.hasNext()) {
         ItemPrice itemPrice = (ItemPrice)var3.next();
         String name = itemPrice.getName();
         if (name.toLowerCase().contains(itemName)) {
            result.add(itemPrice);
         }
      }

      return result;
   }

   @Nonnull
   public ItemComposition getItemComposition(int itemId) {
      return this.client.getItemDefinition(itemId);
   }

   public int canonicalize(int itemID) {
      ItemComposition itemComposition = this.getItemComposition(itemID);
      if (itemComposition.getNote() != -1) {
         return itemComposition.getLinkedNoteId();
      } else {
         return itemComposition.getPlaceholderTemplateId() != -1 ? itemComposition.getPlaceholderId() : (Integer)WORN_ITEMS.getOrDefault(itemID, itemID);
      }
   }

   private AsyncBufferedImage loadImage(int itemId, int quantity, boolean stackable) {
      AsyncBufferedImage img = new AsyncBufferedImage(this.clientThread, 36, 32, 2);
      this.clientThread.invoke(() -> {
         if (this.client.getGameState().ordinal() < GameState.LOGIN_SCREEN.ordinal()) {
            return false;
         } else {
            SpritePixels sprite = this.client.createItemSprite(itemId, quantity, 1, 3153952, stackable ? 1 : 0, false, 512);
            if (sprite == null) {
               return false;
            } else {
               sprite.toBufferedImage(img);
               img.loaded();
               return true;
            }
         }
      });
      return img;
   }

   public AsyncBufferedImage getImage(int itemId) {
      return this.getImage(itemId, 1, false);
   }

   public AsyncBufferedImage getImage(int itemId, int quantity, boolean stackable) {
      try {
         return (AsyncBufferedImage)this.itemImages.get(new ImageKey(itemId, quantity, stackable));
      } catch (ExecutionException var5) {
         return null;
      }
   }

   private BufferedImage loadItemOutline(int itemId, int itemQuantity, Color outlineColor) {
      SpritePixels itemSprite = this.client.createItemSprite(itemId, itemQuantity, 1, 0, 0, false, 512);
      return itemSprite.toBufferedOutline(outlineColor);
   }

   public BufferedImage getItemOutline(int itemId, int itemQuantity, Color outlineColor) {
      try {
         return (BufferedImage)this.itemOutlines.get(new OutlineKey(itemId, itemQuantity, outlineColor));
      } catch (ExecutionException var5) {
         return null;
      }
   }

   private static final class OutlineKey {
      private final int itemId;
      private final int itemQuantity;
      private final Color outlineColor;

      public OutlineKey(int itemId, int itemQuantity, Color outlineColor) {
         this.itemId = itemId;
         this.itemQuantity = itemQuantity;
         this.outlineColor = outlineColor;
      }

      public int getItemId() {
         return this.itemId;
      }

      public int getItemQuantity() {
         return this.itemQuantity;
      }

      public Color getOutlineColor() {
         return this.outlineColor;
      }

      public boolean equals(Object o) {
         if (o == this) {
            return true;
         } else if (!(o instanceof OutlineKey)) {
            return false;
         } else {
            OutlineKey other = (OutlineKey)o;
            if (this.getItemId() != other.getItemId()) {
               return false;
            } else if (this.getItemQuantity() != other.getItemQuantity()) {
               return false;
            } else {
               Object this$outlineColor = this.getOutlineColor();
               Object other$outlineColor = other.getOutlineColor();
               if (this$outlineColor == null) {
                  if (other$outlineColor != null) {
                     return false;
                  }
               } else if (!this$outlineColor.equals(other$outlineColor)) {
                  return false;
               }

               return true;
            }
         }
      }

      public int hashCode() {
         int PRIME = true;
         int result = 1;
         result = result * 59 + this.getItemId();
         result = result * 59 + this.getItemQuantity();
         Object $outlineColor = this.getOutlineColor();
         result = result * 59 + ($outlineColor == null ? 43 : $outlineColor.hashCode());
         return result;
      }

      public String toString() {
         int var10000 = this.getItemId();
         return "ItemManager.OutlineKey(itemId=" + var10000 + ", itemQuantity=" + this.getItemQuantity() + ", outlineColor=" + String.valueOf(this.getOutlineColor()) + ")";
      }
   }

   private static final class ImageKey {
      private final int itemId;
      private final int itemQuantity;
      private final boolean stackable;

      public ImageKey(int itemId, int itemQuantity, boolean stackable) {
         this.itemId = itemId;
         this.itemQuantity = itemQuantity;
         this.stackable = stackable;
      }

      public int getItemId() {
         return this.itemId;
      }

      public int getItemQuantity() {
         return this.itemQuantity;
      }

      public boolean isStackable() {
         return this.stackable;
      }

      public boolean equals(Object o) {
         if (o == this) {
            return true;
         } else if (!(o instanceof ImageKey)) {
            return false;
         } else {
            ImageKey other = (ImageKey)o;
            if (this.getItemId() != other.getItemId()) {
               return false;
            } else if (this.getItemQuantity() != other.getItemQuantity()) {
               return false;
            } else {
               return this.isStackable() == other.isStackable();
            }
         }
      }

      public int hashCode() {
         int PRIME = true;
         int result = 1;
         result = result * 59 + this.getItemId();
         result = result * 59 + this.getItemQuantity();
         result = result * 59 + (this.isStackable() ? 79 : 97);
         return result;
      }

      public String toString() {
         int var10000 = this.getItemId();
         return "ItemManager.ImageKey(itemId=" + var10000 + ", itemQuantity=" + this.getItemQuantity() + ", stackable=" + this.isStackable() + ")";
      }
   }
}
