package net.runelite.client.plugins.grandexchange;

import custom.UpdateGrandExchangeItemDetailScript;
import custom.UpdateGrandExchangeMarketErrorScript;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.inject.Inject;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import net.runelite.api.Client;
import net.runelite.api.ItemComposition;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.game.ItemManager;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.FontManager;
import net.runelite.client.ui.components.IconTextField;
import net.runelite.client.util.QuantityFormatter;

class GrandExchangeMarketItemPanel extends JPanel {
   private static final int ITEM_DETAIL_LIMIT = 50;
   private static final int ITEM_DETAIL_TRIGGER = 2;
   private static final int REQUEST_TIMEOUT_MS = 30000;
   private static final int MAX_SUGGESTIONS = 8;
   private static final Dimension ICON_SIZE = new Dimension(36, 32);
   private static final DateTimeFormatter TIMESTAMP_FORMATTER = DateTimeFormatter.ofPattern("d MMM h:mm a").withZone(ZoneId.systemDefault());
   private final GrandExchangePlugin grandExchangePlugin;
   private final ItemManager itemManager;
   private final ClientThread clientThread;
   private final Client client;
   private final JComboBox<DetailRange> rangeSelect = new JComboBox(GrandExchangeMarketItemPanel.DetailRange.values());
   private final IconTextField searchBar = new IconTextField();
   private final JButton refreshButton = createButton("Refresh");
   private final JLabel statusLabel = createMutedLabel("Search for an item to view completed trades and market value.");
   private final JPanel suggestionsPanel = new JPanel(new GridLayout(0, 1, 0, 4));
   private final JPanel detailPanel = new JPanel(new BorderLayout());
   private final Map<Integer, String> itemNameCache = new HashMap();
   private final Timer itemRequestTimeout;
   private List<ItemSearchResult> currentSuggestions = Collections.emptyList();
   private String currentSuggestionQuery = "";
   private int selectedItemId = -1;
   private String selectedItemName;
   private int pendingItemRequestId = -1;
   private int itemSearchGeneration;
   private boolean internalSearchUpdate;

   @Inject
   private GrandExchangeMarketItemPanel(GrandExchangePlugin grandExchangePlugin, ItemManager itemManager, ClientThread clientThread, Client client) {
      this.grandExchangePlugin = grandExchangePlugin;
      this.itemManager = itemManager;
      this.clientThread = clientThread;
      this.client = client;
      this.itemRequestTimeout = new Timer(30000, (e) -> {
         this.handleTimeout();
      });
      this.itemRequestTimeout.setRepeats(false);
      this.setLayout(new BorderLayout());
      this.setBackground(ColorScheme.DARK_GRAY_COLOR);
      JPanel container = new JPanel(new BorderLayout(5, 5));
      container.setBorder(new EmptyBorder(10, 10, 10, 10));
      container.setBackground(ColorScheme.DARK_GRAY_COLOR);
      container.add(this.createControls(), "North");
      container.add(this.createContent(), "Center");
      container.add(this.statusLabel, "South");
      this.add(container, "Center");
      this.renderEmptyDetail();
   }

   void open() {
      if (this.selectedItemId == -1) {
         this.requestItemSuggestions(this.searchBar.getText(), false, false);
      }

   }

   void selectItem(int itemId, String itemName) {
      if (this.pendingItemRequestId != -1) {
         this.itemRequestTimeout.stop();
         this.pendingItemRequestId = -1;
      }

      this.setSelectedItem(itemId, itemName);
      this.requestItemDetail();
   }

   void searchByName(String itemName) {
      this.internalSearchUpdate = true;
      this.searchBar.setText(itemName == null ? "" : itemName);
      this.internalSearchUpdate = false;
      this.requestItemSuggestions(this.searchBar.getText(), true, false);
   }

   void displayItemDetail(UpdateGrandExchangeItemDetailScript packet) {
      if (packet.getRequestId() == this.pendingItemRequestId) {
         this.itemRequestTimeout.stop();
         this.pendingItemRequestId = -1;
         this.primeItemName(packet.getItemId(), () -> {
            this.renderItemDetail(packet);
         });
      }
   }

   void displayMarketError(UpdateGrandExchangeMarketErrorScript packet) {
      if (packet.getTriggerId() == 2 && packet.getRequestId() == this.pendingItemRequestId) {
         this.itemRequestTimeout.stop();
         this.pendingItemRequestId = -1;
         this.setLoading(false);
         this.detailPanel.removeAll();
         this.detailPanel.add(createDetailMessage(packet.getMessage()), "North");
         this.detailPanel.revalidate();
         this.detailPanel.repaint();
      }
   }

   private JPanel createControls() {
      JPanel controls = new JPanel(new BorderLayout(5, 5));
      controls.setBackground(ColorScheme.DARK_GRAY_COLOR);
      styleCombo(this.rangeSelect);
      this.rangeSelect.addActionListener((e) -> {
         if (this.selectedItemId != -1) {
            this.requestItemDetail();
         }

      });
      this.searchBar.setIcon(IconTextField.Icon.SEARCH);
      this.searchBar.setBackground(ColorScheme.DARKER_GRAY_COLOR);
      this.searchBar.setHoverBackgroundColor(ColorScheme.DARK_GRAY_HOVER_COLOR);
      this.searchBar.addActionListener((e) -> {
         this.selectFirstSuggestion();
      });
      this.searchBar.addClearListener(() -> {
         this.selectedItemId = -1;
         this.selectedItemName = null;
         this.requestItemSuggestions("", false, false);
         this.renderEmptyDetail();
      });
      this.searchBar.getDocument().addDocumentListener(new DocumentListener() {
         public void insertUpdate(DocumentEvent e) {
            GrandExchangeMarketItemPanel.this.onSearchChanged();
         }

         public void removeUpdate(DocumentEvent e) {
            GrandExchangeMarketItemPanel.this.onSearchChanged();
         }

         public void changedUpdate(DocumentEvent e) {
            GrandExchangeMarketItemPanel.this.onSearchChanged();
         }
      });
      this.refreshButton.addActionListener((e) -> {
         this.requestItemDetail();
      });
      JPanel searchRow = new JPanel(new BorderLayout(5, 0));
      searchRow.setBackground(ColorScheme.DARK_GRAY_COLOR);
      searchRow.add(this.searchBar, "Center");
      searchRow.add(this.refreshButton, "East");
      controls.add(this.rangeSelect, "North");
      controls.add(searchRow, "Center");
      return controls;
   }

   private JPanel createContent() {
      JPanel content = new JPanel(new BorderLayout(0, 5));
      content.setBackground(ColorScheme.DARK_GRAY_COLOR);
      this.suggestionsPanel.setBackground(ColorScheme.DARK_GRAY_COLOR);
      this.suggestionsPanel.setVisible(false);
      this.detailPanel.setBackground(ColorScheme.DARK_GRAY_COLOR);
      JPanel wrapper = new JPanel(new BorderLayout());
      wrapper.setBackground(ColorScheme.DARK_GRAY_COLOR);
      wrapper.add(this.detailPanel, "North");
      JScrollPane scrollPane = new JScrollPane(wrapper);
      scrollPane.setBackground(ColorScheme.DARK_GRAY_COLOR);
      scrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(12, 0));
      scrollPane.getVerticalScrollBar().setBorder(new EmptyBorder(0, 5, 0, 0));
      content.add(this.suggestionsPanel, "North");
      content.add(scrollPane, "Center");
      return content;
   }

   private void onSearchChanged() {
      if (!this.internalSearchUpdate) {
         this.renderSuggestions();
      }
   }

   private void renderSuggestions() {
      this.requestItemSuggestions(this.searchBar.getText(), false, false);
   }

   private void renderSuggestionResults(String query, List<ItemSearchResult> suggestions) {
      this.currentSuggestionQuery = query;
      this.currentSuggestions = suggestions;
      this.suggestionsPanel.removeAll();
      if (suggestions.isEmpty()) {
         this.suggestionsPanel.setVisible(false);
      } else {
         Iterator var3 = suggestions.iterator();

         while(var3.hasNext()) {
            ItemSearchResult item = (ItemSearchResult)var3.next();
            this.suggestionsPanel.add(this.createSuggestionPanel(item));
         }

         this.suggestionsPanel.setVisible(true);
      }

      this.suggestionsPanel.revalidate();
      this.suggestionsPanel.repaint();
   }

   private JPanel createSuggestionPanel(final ItemSearchResult item) {
      final JPanel panel = new JPanel(new BorderLayout(6, 0));
      panel.setBackground(ColorScheme.DARKER_GRAY_COLOR);
      panel.setBorder(new EmptyBorder(5, 6, 5, 6));
      panel.setCursor(new Cursor(12));
      panel.setToolTipText("View " + item.getName());
      JLabel icon = new JLabel();
      icon.setPreferredSize(ICON_SIZE);
      this.itemManager.getImage(item.getId()).addTo(icon);
      panel.add(icon, "West");
      final JPanel text = new JPanel(new GridLayout(2, 1));
      text.setBackground(ColorScheme.DARKER_GRAY_COLOR);
      text.add(createWhiteLabel(item.getName()));
      text.add(createMutedLabel("Item " + item.getId()));
      panel.add(text, "Center");
      panel.addMouseListener(new MouseAdapter() {
         public void mouseEntered(MouseEvent e) {
            panel.setBackground(ColorScheme.DARK_GRAY_HOVER_COLOR);
            text.setBackground(ColorScheme.DARK_GRAY_HOVER_COLOR);
         }

         public void mouseExited(MouseEvent e) {
            panel.setBackground(ColorScheme.DARKER_GRAY_COLOR);
            text.setBackground(ColorScheme.DARKER_GRAY_COLOR);
         }

         public void mouseReleased(MouseEvent e) {
            GrandExchangeMarketItemPanel.this.selectItem(item.getId(), item.getName());
         }
      });
      return panel;
   }

   private void selectFirstSuggestion() {
      String query = normalizeSearchQuery(this.searchBar.getText());
      if (!query.isEmpty() && query.equals(this.currentSuggestionQuery) && !this.currentSuggestions.isEmpty()) {
         ItemSearchResult item = (ItemSearchResult)this.currentSuggestions.get(0);
         this.selectItem(item.getId(), item.getName());
      } else {
         this.requestItemSuggestions(this.searchBar.getText(), false, true);
      }
   }

   private void requestItemSuggestions(String rawQuery, boolean selectExactMatch, boolean selectFirst) {
      String query = normalizeSearchQuery(rawQuery);
      int searchGeneration = ++this.itemSearchGeneration;
      if (query.isEmpty()) {
         this.renderSuggestionResults(query, Collections.emptyList());
      } else {
         this.clientThread.invokeLater(() -> {
            if (searchGeneration == this.itemSearchGeneration) {
               List<ItemSearchResult> suggestions = this.searchCacheItemsOnClientThread(query);
               SwingUtilities.invokeLater(() -> {
                  if (searchGeneration == this.itemSearchGeneration && query.equals(normalizeSearchQuery(this.searchBar.getText()))) {
                     ItemSearchResult item;
                     if (selectExactMatch) {
                        item = (ItemSearchResult)suggestions.stream().filter((itemx) -> {
                           return itemx.getNormalizedName().equals(query);
                        }).findFirst().orElse((Object)null);
                        if (item != null) {
                           this.selectItem(item.getId(), item.getName());
                           return;
                        }
                     }

                     if (selectFirst) {
                        if (suggestions.isEmpty()) {
                           this.statusLabel.setText("No item matches that search.");
                           this.searchBar.setIcon(IconTextField.Icon.ERROR);
                           this.renderSuggestionResults(query, Collections.emptyList());
                        } else {
                           item = (ItemSearchResult)suggestions.get(0);
                           this.selectItem(item.getId(), item.getName());
                        }
                     } else {
                        this.renderSuggestionResults(query, suggestions);
                     }
                  }
               });
            }
         });
      }
   }

   private List<ItemSearchResult> searchCacheItemsOnClientThread(String query) {
      List<ItemSearchResult> results = new ArrayList();
      Map<Integer, ItemSearchResult> seenItems = new HashMap();
      int itemCount = this.client.getItemCount();

      for(int itemId = 0; itemId < itemCount; ++itemId) {
         int canonicalItemId = this.itemManager.canonicalize(itemId);
         if (canonicalItemId >= 0 && canonicalItemId < itemCount && !seenItems.containsKey(canonicalItemId)) {
            ItemComposition itemComposition = this.itemManager.getItemComposition(canonicalItemId);
            String name = itemComposition.getName();
            if (isSearchableItemName(name)) {
               ItemSearchResult result = new ItemSearchResult(canonicalItemId, name);
               if (result.getNormalizedName().contains(query)) {
                  seenItems.put(canonicalItemId, result);
                  results.add(result);
               }
            }
         }
      }

      results.sort(Comparator.comparing((item) -> {
         return !item.getNormalizedName().equals(query);
      }).thenComparing((item) -> {
         return !item.getNormalizedName().startsWith(query);
      }).thenComparing((rec$) -> {
         return ((ItemSearchResult)rec$).getName();
      }, String.CASE_INSENSITIVE_ORDER).thenComparingInt((rec$) -> {
         return ((ItemSearchResult)rec$).getId();
      }));
      return results.size() <= 8 ? results : new ArrayList(results.subList(0, 8));
   }

   private void setSelectedItem(int itemId, String itemName) {
      this.selectedItemId = itemId;
      this.selectedItemName = itemName != null && !itemName.isEmpty() ? itemName : "Item " + itemId;
      this.itemNameCache.put(itemId, this.selectedItemName);
      this.internalSearchUpdate = true;
      this.searchBar.setText(this.selectedItemName);
      this.internalSearchUpdate = false;
      this.searchBar.setIcon(IconTextField.Icon.SEARCH);
      this.suggestionsPanel.setVisible(false);
      this.currentSuggestions = Collections.emptyList();
      this.currentSuggestionQuery = "";
   }

   private void requestItemDetail() {
      if (this.pendingItemRequestId == -1) {
         if (this.selectedItemId <= 0) {
            this.selectFirstSuggestion();
         } else {
            DetailRange range = (DetailRange)this.rangeSelect.getSelectedItem();
            if (range == null) {
               range = GrandExchangeMarketItemPanel.DetailRange.DAY;
            }

            this.pendingItemRequestId = this.grandExchangePlugin.requestGrandExchangeItemDetail(this.selectedItemId, range.hours, 50, 0);
            this.itemRequestTimeout.restart();
            this.setLoading(true);
            this.suggestionsPanel.setVisible(false);
            this.detailPanel.removeAll();
            this.detailPanel.add(createDetailMessage("Loading " + this.getItemName(this.selectedItemId) + " market detail..."), "North");
            this.detailPanel.revalidate();
            this.detailPanel.repaint();
         }
      }
   }

   private void setLoading(boolean loading) {
      this.searchBar.setEditable(!loading);
      this.searchBar.setIcon(loading ? IconTextField.Icon.LOADING : IconTextField.Icon.SEARCH);
      this.refreshButton.setEnabled(!loading);
      this.rangeSelect.setEnabled(!loading);
      this.statusLabel.setText(loading ? "Loading item market detail..." : "Ready.");
   }

   private void handleTimeout() {
      if (this.pendingItemRequestId != -1) {
         this.displayMarketError(new UpdateGrandExchangeMarketErrorScript(this.pendingItemRequestId, 2, "TIMEOUT", "Grand Exchange item detail request timed out. Try again in a moment."));
      }
   }

   private void renderEmptyDetail() {
      this.detailPanel.removeAll();
      this.detailPanel.add(createDetailMessage("No item selected."), "North");
      this.detailPanel.revalidate();
      this.detailPanel.repaint();
      this.statusLabel.setText("Search for an item to view completed trades and market value.");
   }

   private void renderItemDetail(UpdateGrandExchangeItemDetailScript detail) {
      this.setLoading(false);
      this.selectedItemId = detail.getItemId();
      this.selectedItemName = this.getItemName(detail.getItemId());
      this.detailPanel.removeAll();
      JPanel header = new JPanel(new BorderLayout(6, 0));
      header.setBackground(ColorScheme.DARKER_GRAY_COLOR);
      header.setBorder(new EmptyBorder(6, 6, 6, 6));
      JLabel icon = new JLabel();
      icon.setPreferredSize(ICON_SIZE);
      this.itemManager.getImage(detail.getItemId()).addTo(icon);
      header.add(icon, "West");
      JPanel titlePanel = new JPanel(new GridLayout(2, 1));
      titlePanel.setBackground(ColorScheme.DARKER_GRAY_COLOR);
      titlePanel.add(createWhiteLabel(this.getItemName(detail.getItemId())));
      titlePanel.add(createMutedLabel("Last " + formatRange(detail.getRangeHours()) + " market value"));
      header.add(titlePanel, "Center");
      JPanel stats = new JPanel(new GridLayout(0, 2, 6, 3));
      stats.setBackground(ColorScheme.DARKER_GRAY_COLOR);
      stats.setBorder(new EmptyBorder(8, 6, 8, 6));
      this.addStat(stats, "Last", formatGp(detail.getLastPrice()), "Most recent completed trade price for this item in the selected time range.");
      this.addStat(stats, "Average", formatGp(detail.getAveragePrice()), "Average price per item across completed trades in the selected time range.");
      this.addStat(stats, "Low", formatGp(detail.getMinPrice()), "Lowest completed trade price for this item in the selected time range.");
      this.addStat(stats, "High", formatGp(detail.getMaxPrice()), "Highest completed trade price for this item in the selected time range.");
      this.addStat(stats, "Volume", QuantityFormatter.formatNumber(detail.getTransactionVolume()), "Total number of this item traded in completed transactions.");
      this.addStat(stats, "Total", QuantityFormatter.formatNumber(detail.getTransactionTotalGp()) + " gp", "Total GP value of completed trades for this item.");
      this.addStat(stats, "Trades", QuantityFormatter.formatNumber(detail.getTransactionCount()), "Number of completed transactions included in these stats.");
      JPanel ordersSection = new JPanel(new BorderLayout(0, 5));
      ordersSection.setBackground(ColorScheme.DARK_GRAY_COLOR);
      ordersSection.add(createMutedLabel("Recent completed trades"), "North");
      JPanel orders = new JPanel(new GridLayout(0, 1, 0, 5));
      orders.setBackground(ColorScheme.DARK_GRAY_COLOR);
      orders.setBorder(new EmptyBorder(0, 0, 0, 0));
      if (detail.getOrders().isEmpty()) {
         orders.add(createDetailMessage("No completed trades returned."));
      } else {
         Iterator var8 = detail.getOrders().iterator();

         while(var8.hasNext()) {
            UpdateGrandExchangeItemDetailScript.Order order = (UpdateGrandExchangeItemDetailScript.Order)var8.next();
            orders.add(this.createCompletedTradePanel(order));
         }
      }

      ordersSection.add(orders, "Center");
      JPanel body = new JPanel(new BorderLayout());
      body.setBackground(ColorScheme.DARKER_GRAY_COLOR);
      body.add(header, "North");
      body.add(stats, "Center");
      JPanel wrapper = new JPanel(new BorderLayout());
      wrapper.setBackground(ColorScheme.DARK_GRAY_COLOR);
      wrapper.add(body, "North");
      wrapper.add(ordersSection, "Center");
      this.detailPanel.add(wrapper, "North");
      this.detailPanel.revalidate();
      this.detailPanel.repaint();
      JLabel var10000 = this.statusLabel;
      String var10001 = this.getItemName(detail.getItemId());
      var10000.setText("Showing completed trades for " + var10001 + " over " + formatRange(detail.getRangeHours()) + ".");
   }

   private JPanel createCompletedTradePanel(UpdateGrandExchangeItemDetailScript.Order order) {
      JPanel panel = new JPanel(new BorderLayout(0, 4));
      panel.setBackground(ColorScheme.DARKER_GRAY_COLOR);
      panel.setBorder(new EmptyBorder(5, 6, 5, 6));
      JLabel title = createWhiteLabel(formatTradeType(order.getType()) + " trade");
      panel.add(title, "North");
      long completedTradeQuantity = getCompletedTradeQuantity(order);
      JPanel fields = new JPanel(new GridLayout(0, 1, 0, 4));
      fields.setBackground(ColorScheme.DARKER_GRAY_COLOR);
      fields.add(this.createCompletedTradeField("Quantity", QuantityFormatter.formatNumber(completedTradeQuantity) + " " + pluralize("item", completedTradeQuantity), "Quantity included in this completed trade sample."));
      fields.add(this.createCompletedTradeField("Trade price", QuantityFormatter.formatNumber((long)order.getPrice()) + " gp each", "Price per item in this completed trade sample."));
      fields.add(this.createCompletedTradeField("Time", formatTimestamp(order.getCreatedEpochMs()), "Timestamp provided for this completed trade sample."));
      panel.add(fields, "Center");
      return panel;
   }

   private JPanel createCompletedTradeField(String labelText, String valueText, String tooltip) {
      JPanel field = new JPanel(new GridLayout(0, 1, 0, 0));
      field.setBackground(ColorScheme.DARKER_GRAY_COLOR);
      JLabel label = createMutedLabel(labelText);
      label.setToolTipText(tooltip);
      field.add(label);
      JLabel value = createWhiteLabel(valueText);
      value.setToolTipText(tooltip);
      field.add(value);
      return field;
   }

   private void addStat(JPanel stats, String label, String value, String tooltip) {
      JLabel name = createMutedLabel(label);
      JLabel data = createWhiteLabel(value);
      data.setHorizontalAlignment(4);
      name.setToolTipText(tooltip);
      data.setToolTipText(tooltip);
      stats.add(name);
      stats.add(data);
   }

   private void primeItemName(int itemId, Runnable afterPrime) {
      if (this.itemNameCache.containsKey(itemId)) {
         afterPrime.run();
      } else {
         this.clientThread.invokeLater(() -> {
            String name = this.getItemNameOnClientThread(itemId);
            SwingUtilities.invokeLater(() -> {
               this.itemNameCache.put(itemId, name);
               afterPrime.run();
            });
         });
      }
   }

   private String getItemName(int itemId) {
      return (String)this.itemNameCache.getOrDefault(itemId, "Item " + itemId);
   }

   private static String normalizeSearchQuery(String query) {
      return query == null ? "" : query.trim().toLowerCase(Locale.ENGLISH);
   }

   private static boolean isSearchableItemName(String name) {
      return name != null && !name.isEmpty() && !"null".equalsIgnoreCase(name);
   }

   private String getItemNameOnClientThread(int itemId) {
      ItemComposition composition = this.itemManager.getItemComposition(itemId);
      String name = composition == null ? null : composition.getName();
      return name != null && !name.isEmpty() ? name : "Item " + itemId;
   }

   private static String formatGp(long price) {
      return price <= 0L ? "N/A" : QuantityFormatter.formatNumber(price) + " gp";
   }

   private static String formatTradeType(String type) {
      if (type == null) {
         return "Unknown";
      } else {
         switch (type.trim().toLowerCase(Locale.ENGLISH)) {
            case "0":
            case "buy":
               return "Buy";
            case "1":
            case "sell":
               return "Sell";
            default:
               return formatStableValue(type);
         }
      }
   }

   private static long getCompletedTradeQuantity(UpdateGrandExchangeItemDetailScript.Order order) {
      long fulfilledQuantity = order.getFulfilledQuantity();
      return fulfilledQuantity > 0L ? fulfilledQuantity : (long)order.getItemQuantity();
   }

   private static String formatStableValue(String value) {
      String cleaned = value.trim().replace('_', ' ').toLowerCase(Locale.ENGLISH);
      if (cleaned.isEmpty()) {
         return "Unknown";
      } else {
         char var10000 = Character.toUpperCase(cleaned.charAt(0));
         return "" + var10000 + cleaned.substring(1);
      }
   }

   private static String formatTimestamp(long timestampEpochMs) {
      return timestampEpochMs <= 0L ? "Unknown" : TIMESTAMP_FORMATTER.format(Instant.ofEpochMilli(timestampEpochMs));
   }

   private static String formatRange(int rangeHours) {
      return rangeHours % 24 == 0 && rangeHours >= 72 ? rangeHours / 24 + " days" : "" + rangeHours + " hours";
   }

   private static String pluralize(String singular, long quantity) {
      return quantity == 1L ? singular : singular + "s";
   }

   private static JLabel createWhiteLabel(String text) {
      JLabel label = new JLabel(text);
      label.setForeground(Color.WHITE);
      label.setFont(FontManager.getRunescapeSmallFont());
      return label;
   }

   private static JLabel createMutedLabel(String text) {
      JLabel label = new JLabel(text);
      label.setForeground(ColorScheme.LIGHT_GRAY_COLOR);
      label.setFont(FontManager.getRunescapeSmallFont());
      return label;
   }

   private static JPanel createDetailMessage(String message) {
      JPanel panel = new JPanel(new BorderLayout());
      panel.setBackground(ColorScheme.DARKER_GRAY_COLOR);
      panel.setBorder(new EmptyBorder(8, 8, 8, 8));
      panel.add(createMutedLabel(message), "Center");
      return panel;
   }

   private static JButton createButton(String text) {
      JButton button = new JButton(text);
      button.setFocusable(false);
      button.setBackground(ColorScheme.DARKER_GRAY_COLOR);
      button.setForeground(Color.WHITE);
      button.setBorder(BorderFactory.createLineBorder(ColorScheme.DARK_GRAY_HOVER_COLOR));
      return button;
   }

   private static void styleCombo(JComboBox<?> comboBox) {
      comboBox.setFocusable(false);
      comboBox.setBackground(ColorScheme.DARKER_GRAY_COLOR);
      comboBox.setForeground(Color.WHITE);
   }

   private static class ItemSearchResult {
      private final int id;
      private final String name;
      private final String normalizedName;

      private ItemSearchResult(int id, String name) {
         this.id = id;
         this.name = name;
         this.normalizedName = GrandExchangeMarketItemPanel.normalizeSearchQuery(name);
      }

      private int getId() {
         return this.id;
      }

      private String getName() {
         return this.name;
      }

      private String getNormalizedName() {
         return this.normalizedName;
      }
   }

   private static enum DetailRange {
      SIX_HOURS("Last 6 Hours", 6),
      TWELVE_HOURS("Last 12 Hours", 12),
      DAY("Last 24 Hours", 24),
      THREE_DAYS("Last 3 Days", 72),
      SEVEN_DAYS("Last 7 Days", 168),
      THIRTY_DAYS("Last 30 Days", 720);

      private final String label;
      private final int hours;

      private DetailRange(String label, int hours) {
         this.label = label;
         this.hours = hours;
      }

      public String toString() {
         return this.label;
      }
   }
}
