package net.runelite.client.plugins.inventorytags;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import javax.inject.Inject;
import net.runelite.api.widgets.WidgetItem;
import net.runelite.client.game.ItemManager;
import net.runelite.client.ui.overlay.WidgetItemOverlay;
import net.runelite.client.util.ColorUtil;
import net.runelite.client.util.ImageUtil;

class InventoryTagsOverlay extends WidgetItemOverlay {
   private final ItemManager itemManager;
   private final InventoryTagsPlugin plugin;
   private final InventoryTagsConfig config;
   private final Cache<Long, Image> fillCache;
   private final Cache<Integer, Tag> tagCache;
   private final Tag NONE = new Tag();

   @Inject
   private InventoryTagsOverlay(ItemManager itemManager, InventoryTagsPlugin plugin, InventoryTagsConfig config) {
      this.itemManager = itemManager;
      this.plugin = plugin;
      this.config = config;
      this.showOnEquipment();
      this.showOnInventory();
      this.showOnInterfaces(new int[]{551, 271, 550, 672});
      this.fillCache = CacheBuilder.newBuilder().concurrencyLevel(1).maximumSize(32L).build();
      this.tagCache = CacheBuilder.newBuilder().concurrencyLevel(1).maximumSize(39L).build();
   }

   public void renderItemOverlay(Graphics2D graphics, int itemId, WidgetItem widgetItem) {
      Tag tag = this.getTag(itemId);
      if (tag != null && tag.color != null) {
         Color color = tag.color;
         Rectangle bounds = widgetItem.getCanvasBounds();
         if (this.config.showTagOutline()) {
            BufferedImage outline = this.itemManager.getItemOutline(itemId, widgetItem.getQuantity(), color);
            graphics.drawImage(outline, (int)bounds.getX(), (int)bounds.getY(), (ImageObserver)null);
         }

         if (this.config.showTagFill()) {
            Image image = this.getFillImage(color, widgetItem.getId(), widgetItem.getQuantity());
            graphics.drawImage(image, (int)bounds.getX(), (int)bounds.getY(), (ImageObserver)null);
         }

         if (this.config.showTagUnderline()) {
            int heightOffSet = (int)bounds.getY() + (int)bounds.getHeight() + 2;
            graphics.setColor(color);
            graphics.drawLine((int)bounds.getX(), heightOffSet, (int)bounds.getX() + (int)bounds.getWidth(), heightOffSet);
         }

      }
   }

   private Tag getTag(int itemId) {
      Tag tag = (Tag)this.tagCache.getIfPresent(itemId);
      if (tag == null) {
         tag = this.plugin.getTag(itemId);
         if (tag == null) {
            this.tagCache.put(itemId, this.NONE);
            return null;
         }

         if (tag == this.NONE) {
            return null;
         }

         this.tagCache.put(itemId, tag);
      }

      return tag;
   }

   private Image getFillImage(Color color, int itemId, int qty) {
      long key = (long)itemId << 32 | (long)qty;
      Image image = (Image)this.fillCache.getIfPresent(key);
      if (image == null) {
         Color fillColor = ColorUtil.colorWithAlpha(color, this.config.fillOpacity());
         image = ImageUtil.fillImage(this.itemManager.getImage(itemId, qty, false), fillColor);
         this.fillCache.put(key, image);
      }

      return (Image)image;
   }

   void invalidateCache() {
      this.fillCache.invalidateAll();
      this.tagCache.invalidateAll();
   }
}
