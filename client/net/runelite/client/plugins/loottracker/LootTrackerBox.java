package net.runelite.client.plugins.loottracker;

import com.google.common.base.Strings;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.ToLongFunction;
import java.util.stream.Collectors;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.border.EmptyBorder;
import net.runelite.client.game.ItemManager;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.FontManager;
import net.runelite.client.util.AsyncBufferedImage;
import net.runelite.client.util.ImageUtil;
import net.runelite.client.util.QuantityFormatter;
import net.runelite.client.util.Text;
import net.runelite.http.api.loottracker.LootRecordType;

class LootTrackerBox extends JPanel {
   private static final int ITEMS_PER_ROW = 5;
   private static final int TITLE_PADDING = 5;
   private final JPanel itemContainer = new JPanel();
   private final JLabel priceLabel = new JLabel();
   private final JLabel subTitleLabel = new JLabel();
   private final JPanel logTitle = new JPanel();
   private final ItemManager itemManager;
   private final LootTrackerPriceType priceType;
   private final boolean showPriceType;
   private final LootTrackerRecord record;
   private long totalPrice;
   private final boolean hideIgnoredItems;
   private final BiConsumer<String, Boolean> onItemToggle;

   LootTrackerBox(ItemManager itemManager, LootTrackerRecord record, boolean hideIgnoredItems, LootTrackerPriceType priceType, boolean showPriceType, BiConsumer<String, Boolean> onItemToggle, BiConsumer<String, Boolean> onEventToggle, boolean eventIgnored) {
      this.record = record;
      this.itemManager = itemManager;
      this.onItemToggle = onItemToggle;
      this.hideIgnoredItems = hideIgnoredItems;
      this.priceType = priceType;
      this.showPriceType = showPriceType;
      this.setLayout(new BorderLayout(0, 1));
      this.setBorder(new EmptyBorder(5, 0, 0, 0));
      this.logTitle.setLayout(new BoxLayout(this.logTitle, 0));
      this.logTitle.setBorder(new EmptyBorder(7, 7, 7, 7));
      this.logTitle.setBackground(eventIgnored ? ColorScheme.DARKER_GRAY_HOVER_COLOR : ColorScheme.DARKER_GRAY_COLOR.darker());
      JLabel titleLabel = new JLabel();
      titleLabel.setText(Text.removeTags(record.getTitle()));
      titleLabel.setFont(FontManager.getRunescapeSmallFont());
      titleLabel.setForeground(Color.WHITE);
      titleLabel.setMinimumSize(new Dimension(1, titleLabel.getPreferredSize().height));
      this.logTitle.add(titleLabel);
      this.subTitleLabel.setFont(FontManager.getRunescapeSmallFont());
      this.subTitleLabel.setForeground(ColorScheme.LIGHT_GRAY_COLOR);
      String subtitle = record.getSubTitle();
      if (!Strings.isNullOrEmpty(subtitle)) {
         this.subTitleLabel.setText(subtitle);
      }

      this.logTitle.add(Box.createRigidArea(new Dimension(5, 0)));
      this.logTitle.add(this.subTitleLabel);
      this.logTitle.add(Box.createHorizontalGlue());
      this.logTitle.add(Box.createRigidArea(new Dimension(5, 0)));
      this.priceLabel.setFont(FontManager.getRunescapeSmallFont());
      this.priceLabel.setForeground(ColorScheme.LIGHT_GRAY_COLOR);
      this.logTitle.add(this.priceLabel);
      this.add(this.logTitle, "North");
      this.add(this.itemContainer, "Center");
      JPopupMenu popupMenu = new JPopupMenu();
      popupMenu.setBorder(new EmptyBorder(5, 5, 5, 5));
      this.setComponentPopupMenu(popupMenu);
      JMenuItem toggle = new JMenuItem(eventIgnored ? "Include loot" : "Hide loot");
      toggle.addActionListener((e) -> {
         onEventToggle.accept(record.getTitle(), !eventIgnored);
      });
      popupMenu.add(toggle);
   }

   boolean matches(LootTrackerRecord r) {
      return r.getType() == this.record.getType() && r.getTitle().equals(this.record.getTitle());
   }

   boolean matches(String id, LootRecordType type) {
      if (id == null) {
         return true;
      } else {
         return this.record.getTitle().equals(id) && this.record.getType() == type;
      }
   }

   void rebuild() {
      this.buildItems();
      String priceTypeString = " ";
      if (this.showPriceType) {
         priceTypeString = this.priceType == LootTrackerPriceType.HIGH_ALCHEMY ? "HA: " : "GE: ";
      }

      this.priceLabel.setText(priceTypeString + QuantityFormatter.quantityToStackSize(this.totalPrice) + " gp");
      this.priceLabel.setToolTipText(QuantityFormatter.formatNumber(this.totalPrice) + " gp");
      long kills = (long)this.record.getKills();
      if (kills > 1L) {
         this.subTitleLabel.setText("x " + kills);
         long var10001 = this.totalPrice / kills;
         this.subTitleLabel.setToolTipText(QuantityFormatter.formatNumber(var10001) + " gp (average)");
      }

      this.revalidate();
   }

   void collapse() {
      if (!this.isCollapsed()) {
         this.itemContainer.setVisible(false);
         this.applyDimmer(false, this.logTitle);
      }

   }

   void expand() {
      if (this.isCollapsed()) {
         this.itemContainer.setVisible(true);
         this.applyDimmer(true, this.logTitle);
      }

   }

   boolean isCollapsed() {
      return !this.itemContainer.isVisible();
   }

   private void applyDimmer(boolean brighten, JPanel panel) {
      Component[] var3 = panel.getComponents();
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         Component component = var3[var5];
         Color color = component.getForeground();
         component.setForeground(brighten ? color.brighter() : color.darker());
      }

   }

   private void buildItems() {
      this.totalPrice = 0L;
      ToLongFunction<LootTrackerItem> getPrice = this.priceType == LootTrackerPriceType.HIGH_ALCHEMY ? LootTrackerItem::getTotalHaPrice : LootTrackerItem::getTotalGePrice;
      Map<Integer, LootTrackerItem> items = (Map)Arrays.stream(this.record.getItems()).filter((itemx) -> {
         return !this.hideIgnoredItems || !itemx.isIgnored();
      }).sorted(Comparator.comparingLong(getPrice).reversed()).collect(Collectors.toMap((k) -> {
         return LootTrackerMapping.map(k.getId(), k.getName());
      }, (v) -> {
         return v;
      }, (oldItem, newItem) -> {
         int qty = oldItem.getQuantity() + newItem.getQuantity();
         if (qty < 0) {
            qty = Integer.MAX_VALUE;
         }

         return new LootTrackerItem(oldItem.getId(), oldItem.getName(), qty, oldItem.getGePrice(), oldItem.getHaPrice(), oldItem.isIgnored());
      }, LinkedHashMap::new));
      boolean isHidden = items.isEmpty();
      this.setVisible(!isHidden);
      if (!isHidden) {
         this.totalPrice = items.values().stream().mapToLong(getPrice).sum();
         int rowSize = (items.size() % 5 == 0 ? 0 : 1) + items.size() / 5;
         this.itemContainer.removeAll();
         this.itemContainer.setLayout(new GridLayout(rowSize, 5, 1, 1));
         EmptyBorder emptyBorder = new EmptyBorder(5, 5, 5, 5);
         Iterator<LootTrackerItem> it = items.values().iterator();

         for(int i = 0; i < rowSize * 5; ++i) {
            JPanel slotContainer = new JPanel();
            slotContainer.setBackground(ColorScheme.DARKER_GRAY_COLOR);
            if (it.hasNext()) {
               LootTrackerItem item = (LootTrackerItem)it.next();
               JLabel imageLabel = new JLabel();
               imageLabel.setToolTipText(buildToolTip(item));
               imageLabel.setVerticalAlignment(0);
               imageLabel.setHorizontalAlignment(0);
               AsyncBufferedImage itemImage = this.itemManager.getImage(item.getId(), item.getQuantity(), item.getQuantity() > 1);
               if (item.isIgnored()) {
                  itemImage.onLoaded(() -> {
                     BufferedImage transparentImage = ImageUtil.alphaOffset(itemImage, 0.3F);
                     imageLabel.setIcon(new ImageIcon(transparentImage));
                  });
               } else {
                  itemImage.addTo(imageLabel);
               }

               slotContainer.add(imageLabel);
               JPopupMenu popupMenu = new JPopupMenu();
               popupMenu.setBorder(emptyBorder);
               slotContainer.setComponentPopupMenu(popupMenu);
               JMenuItem toggle = new JMenuItem("Toggle item");
               toggle.addActionListener((e) -> {
                  item.setIgnored(!item.isIgnored());
                  this.onItemToggle.accept(item.getName(), item.isIgnored());
               });
               popupMenu.add(toggle);
            }

            this.itemContainer.add(slotContainer);
         }

         this.itemContainer.revalidate();
      }
   }

   private static String buildToolTip(LootTrackerItem item) {
      String name = item.getName();
      int quantity = item.getQuantity();
      long gePrice = item.getTotalGePrice();
      long haPrice = item.getTotalHaPrice();
      String ignoredLabel = item.isIgnored() ? " - Ignored" : "";
      StringBuilder sb = new StringBuilder("<html>");
      sb.append(name).append(" x ").append(QuantityFormatter.formatNumber((long)quantity)).append(ignoredLabel);
      if (item.getId() == 995) {
         sb.append("</html>");
         return sb.toString();
      } else {
         sb.append("<br>GE: ").append(QuantityFormatter.quantityToStackSize(gePrice));
         if (quantity > 1) {
            sb.append(" (").append(QuantityFormatter.quantityToStackSize((long)item.getGePrice())).append(" ea)");
         }

         if (item.getId() == 13204) {
            sb.append("</html>");
            return sb.toString();
         } else {
            sb.append("<br>HA: ").append(QuantityFormatter.quantityToStackSize(haPrice));
            if (quantity > 1) {
               sb.append(" (").append(QuantityFormatter.quantityToStackSize((long)item.getHaPrice())).append(" ea)");
            }

            sb.append("</html>");
            return sb.toString();
         }
      }
   }
}
