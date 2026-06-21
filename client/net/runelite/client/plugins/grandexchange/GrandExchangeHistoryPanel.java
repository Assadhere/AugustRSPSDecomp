package net.runelite.client.plugins.grandexchange;

import custom.UpdateGrandExchangeHistoryScript;
import custom.UpdateGrandExchangeMarketErrorScript;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import javax.inject.Inject;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
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
import net.runelite.api.ItemComposition;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.game.ItemManager;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.FontManager;
import net.runelite.client.ui.components.IconTextField;
import net.runelite.client.util.AsyncBufferedImage;
import net.runelite.client.util.QuantityFormatter;

class GrandExchangeHistoryPanel extends JPanel {
   private static final int HISTORY_LIMIT = 100;
   private static final int HISTORY_TRIGGER = 1;
   private static final int REQUEST_TIMEOUT_MS = 30000;
   private static final Dimension ICON_SIZE = new Dimension(36, 32);
   private static final DateTimeFormatter TIMESTAMP_FORMATTER = DateTimeFormatter.ofPattern("d MMM h:mm a").withZone(ZoneId.systemDefault());
   private final GrandExchangePlugin grandExchangePlugin;
   private final ItemManager itemManager;
   private final ClientThread clientThread;
   private final JComboBox<TimeRange> rangeSelect = new JComboBox(GrandExchangeHistoryPanel.TimeRange.values());
   private final JComboBox<SortMode> sortSelect = new JComboBox(GrandExchangeHistoryPanel.SortMode.values());
   private final IconTextField searchBar = new IconTextField();
   private final JButton refreshButton = createButton("Refresh");
   private final JButton previousButton = createButton("Previous");
   private final JButton nextButton = createButton("Next");
   private final JLabel statusLabel = createMutedLabel("Select History or press Refresh to load market data.");
   private final JPanel transactionsPanel = new JPanel(new GridBagLayout());
   private final Map<Integer, String> itemNameCache = new HashMap();
   private final Timer historyRequestTimeout;
   private List<UpdateGrandExchangeHistoryScript.Transaction> transactions = new ArrayList();
   private int historyPage;
   private boolean hasMoreHistory;
   private int pendingHistoryRequestId = -1;

   @Inject
   private GrandExchangeHistoryPanel(GrandExchangePlugin grandExchangePlugin, ItemManager itemManager, ClientThread clientThread) {
      this.grandExchangePlugin = grandExchangePlugin;
      this.itemManager = itemManager;
      this.clientThread = clientThread;
      this.historyRequestTimeout = new Timer(30000, (e) -> {
         this.handleHistoryTimeout();
      });
      this.historyRequestTimeout.setRepeats(false);
      this.setLayout(new BorderLayout());
      this.setBackground(ColorScheme.DARK_GRAY_COLOR);
      JPanel container = new JPanel(new BorderLayout(5, 5));
      container.setBorder(new EmptyBorder(10, 10, 10, 10));
      container.setBackground(ColorScheme.DARK_GRAY_COLOR);
      container.add(this.createControls(), "North");
      container.add(this.createContent(), "Center");
      container.add(this.createFooter(), "South");
      this.add(container, "Center");
      this.renderTransactions();
   }

   void open() {
      if (this.transactions.isEmpty() && this.pendingHistoryRequestId == -1) {
         this.requestHistory(true);
      }

   }

   void filterByItemName(String itemName) {
      this.searchBar.setText(itemName == null ? "" : itemName);
      this.open();
      this.renderTransactions();
   }

   void displayHistory(UpdateGrandExchangeHistoryScript packet) {
      if (packet.getRequestId() == this.pendingHistoryRequestId) {
         this.historyRequestTimeout.stop();
         this.pendingHistoryRequestId = -1;
         this.historyPage = packet.getPage();
         this.hasMoreHistory = packet.isHasMore();
         this.transactions = new ArrayList(packet.getTransactions());
         this.primeItemNames(this.getTransactionItemIds(this.transactions), () -> {
            this.setHistoryLoading(false);
            this.renderTransactions();
         });
      }
   }

   void displayMarketError(UpdateGrandExchangeMarketErrorScript packet) {
      if (packet.getTriggerId() == 1 && packet.getRequestId() == this.pendingHistoryRequestId) {
         this.historyRequestTimeout.stop();
         this.pendingHistoryRequestId = -1;
         this.setHistoryLoading(false);
         this.searchBar.setIcon(IconTextField.Icon.ERROR);
         this.statusLabel.setText(packet.getMessage());
         this.previousButton.setEnabled(this.historyPage > 0);
         this.nextButton.setEnabled(this.hasMoreHistory);
      }
   }

   private JPanel createControls() {
      JPanel controls = new JPanel(new BorderLayout(5, 5));
      controls.setBackground(ColorScheme.DARK_GRAY_COLOR);
      this.searchBar.setIcon(IconTextField.Icon.SEARCH);
      this.searchBar.setBackground(ColorScheme.DARKER_GRAY_COLOR);
      this.searchBar.setHoverBackgroundColor(ColorScheme.DARK_GRAY_HOVER_COLOR);
      this.searchBar.addActionListener((e) -> {
         this.renderTransactions();
      });
      this.searchBar.addClearListener(this::renderTransactions);
      this.searchBar.getDocument().addDocumentListener(new DocumentListener() {
         public void insertUpdate(DocumentEvent e) {
            GrandExchangeHistoryPanel.this.renderTransactions();
         }

         public void removeUpdate(DocumentEvent e) {
            GrandExchangeHistoryPanel.this.renderTransactions();
         }

         public void changedUpdate(DocumentEvent e) {
            GrandExchangeHistoryPanel.this.renderTransactions();
         }
      });
      JPanel topRow = new JPanel(new GridLayout(1, 2, 5, 0));
      topRow.setBackground(ColorScheme.DARK_GRAY_COLOR);
      styleCombo(this.rangeSelect);
      styleCombo(this.sortSelect);
      this.rangeSelect.addActionListener((e) -> {
         this.requestHistory(true);
      });
      this.sortSelect.addActionListener((e) -> {
         this.renderTransactions();
      });
      topRow.add(this.rangeSelect);
      topRow.add(this.sortSelect);
      this.refreshButton.addActionListener((e) -> {
         this.requestHistory(false);
      });
      JPanel refreshRow = new JPanel(new BorderLayout(5, 0));
      refreshRow.setBackground(ColorScheme.DARK_GRAY_COLOR);
      refreshRow.add(this.searchBar, "Center");
      refreshRow.add(this.refreshButton, "East");
      controls.add(topRow, "North");
      controls.add(refreshRow, "Center");
      return controls;
   }

   private JPanel createContent() {
      JPanel content = new JPanel(new BorderLayout(0, 5));
      content.setBackground(ColorScheme.DARK_GRAY_COLOR);
      this.transactionsPanel.setBackground(ColorScheme.DARK_GRAY_COLOR);
      JPanel wrapper = new JPanel(new BorderLayout());
      wrapper.setBackground(ColorScheme.DARK_GRAY_COLOR);
      wrapper.add(this.transactionsPanel, "North");
      JScrollPane scrollPane = new JScrollPane(wrapper);
      scrollPane.setBackground(ColorScheme.DARK_GRAY_COLOR);
      scrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(12, 0));
      scrollPane.getVerticalScrollBar().setBorder(new EmptyBorder(0, 5, 0, 0));
      content.add(scrollPane, "Center");
      return content;
   }

   private JPanel createFooter() {
      JPanel footer = new JPanel(new BorderLayout(5, 5));
      footer.setBackground(ColorScheme.DARK_GRAY_COLOR);
      JPanel pageButtons = new JPanel(new GridLayout(1, 2, 5, 0));
      pageButtons.setBackground(ColorScheme.DARK_GRAY_COLOR);
      this.previousButton.addActionListener((e) -> {
         if (this.historyPage > 0) {
            --this.historyPage;
            this.requestHistory(false);
         }

      });
      this.nextButton.addActionListener((e) -> {
         if (this.hasMoreHistory) {
            ++this.historyPage;
            this.requestHistory(false);
         }

      });
      pageButtons.add(this.previousButton);
      pageButtons.add(this.nextButton);
      footer.add(this.statusLabel, "North");
      footer.add(pageButtons, "South");
      return footer;
   }

   private void requestHistory(boolean resetPage) {
      if (this.pendingHistoryRequestId == -1) {
         if (resetPage) {
            this.historyPage = 0;
         }

         TimeRange range = (TimeRange)this.rangeSelect.getSelectedItem();
         if (range == null) {
            range = GrandExchangeHistoryPanel.TimeRange.DAY;
         }

         this.pendingHistoryRequestId = this.grandExchangePlugin.requestGrandExchangeHistory(range.hours, 100, this.historyPage);
         this.historyRequestTimeout.restart();
         this.setHistoryLoading(true);
      }
   }

   private void setHistoryLoading(boolean loading) {
      this.searchBar.setEditable(!loading);
      this.searchBar.setIcon(loading ? IconTextField.Icon.LOADING : IconTextField.Icon.SEARCH);
      this.refreshButton.setEnabled(!loading);
      this.rangeSelect.setEnabled(!loading);
      this.previousButton.setEnabled(!loading && this.historyPage > 0);
      this.nextButton.setEnabled(!loading && this.hasMoreHistory);
      if (loading) {
         this.statusLabel.setText("Loading Grand Exchange history...");
      }

   }

   private void handleHistoryTimeout() {
      if (this.pendingHistoryRequestId != -1) {
         this.displayMarketError(new UpdateGrandExchangeMarketErrorScript(this.pendingHistoryRequestId, 1, "TIMEOUT", "Grand Exchange history request timed out. Try again in a moment."));
      }
   }

   private void renderTransactions() {
      this.transactionsPanel.removeAll();
      List<UpdateGrandExchangeHistoryScript.Transaction> visibleTransactions = this.filteredTransactions();
      GridBagConstraints constraints = new GridBagConstraints();
      constraints.fill = 2;
      constraints.weightx = 1.0;
      constraints.gridx = 0;
      if (visibleTransactions.isEmpty()) {
         this.transactionsPanel.add(createDetailMessage(this.transactions.isEmpty() ? "No transactions loaded." : "No transactions match the current filter."), constraints);
      } else {
         int row = 0;
         Iterator var4 = visibleTransactions.iterator();

         while(var4.hasNext()) {
            UpdateGrandExchangeHistoryScript.Transaction transaction = (UpdateGrandExchangeHistoryScript.Transaction)var4.next();
            constraints.gridy = row++;
            this.transactionsPanel.add(this.createTransactionPanel(transaction), constraints);
         }
      }

      this.previousButton.setEnabled(this.pendingHistoryRequestId == -1 && this.historyPage > 0);
      this.nextButton.setEnabled(this.pendingHistoryRequestId == -1 && this.hasMoreHistory);
      this.statusLabel.setText(this.buildStatus(visibleTransactions));
      this.transactionsPanel.revalidate();
      this.transactionsPanel.repaint();
   }

   private List<UpdateGrandExchangeHistoryScript.Transaction> filteredTransactions() {
      String query = this.searchBar.getText() == null ? "" : this.searchBar.getText().trim().toLowerCase(Locale.ENGLISH);
      SortMode sortMode = (SortMode)this.sortSelect.getSelectedItem();
      if (sortMode == null) {
         sortMode = GrandExchangeHistoryPanel.SortMode.NEWEST;
      }

      Comparator<UpdateGrandExchangeHistoryScript.Transaction> comparator = this.transactionComparator(sortMode);
      return (List)this.transactions.stream().filter((transaction) -> {
         return query.isEmpty() || this.getItemName(transaction.getItemId()).toLowerCase(Locale.ENGLISH).contains(query) || Integer.toString(transaction.getItemId()).contains(query);
      }).sorted(comparator).collect(Collectors.toList());
   }

   private Comparator<UpdateGrandExchangeHistoryScript.Transaction> transactionComparator(SortMode sortMode) {
      switch (sortMode) {
         case OLDEST:
            return Comparator.comparingLong(UpdateGrandExchangeHistoryScript.Transaction::getTimestampEpochMs);
         case NAME:
            return Comparator.comparing((transaction) -> {
               return this.getItemName(transaction.getItemId()).toLowerCase(Locale.ENGLISH);
            });
         case QUANTITY:
            return Comparator.comparingInt(UpdateGrandExchangeHistoryScript.Transaction::getQuantity).reversed();
         case PRICE:
            return Comparator.comparingInt(UpdateGrandExchangeHistoryScript.Transaction::getPrice).reversed();
         case TOTAL:
            return Comparator.comparingLong(this::getTransactionTotal).reversed();
         case NEWEST:
         default:
            return Comparator.comparingLong(UpdateGrandExchangeHistoryScript.Transaction::getTimestampEpochMs).reversed();
      }
   }

   private JPanel createTransactionPanel(final UpdateGrandExchangeHistoryScript.Transaction transaction) {
      final JPanel panel = new JPanel(new BorderLayout(6, 0));
      panel.setBackground(ColorScheme.DARKER_GRAY_COLOR);
      panel.setBorder(new EmptyBorder(6, 6, 6, 6));
      panel.setToolTipText("Open item market detail");
      panel.setCursor(new Cursor(12));
      JLabel icon = new JLabel();
      icon.setPreferredSize(ICON_SIZE);
      AsyncBufferedImage itemImage = this.itemManager.getImage(transaction.getItemId(), transaction.getQuantity(), transaction.getQuantity() > 1);
      itemImage.addTo(icon);
      panel.add(icon, "West");
      JPanel info = new JPanel();
      info.setLayout(new BoxLayout(info, 1));
      info.setBackground(ColorScheme.DARKER_GRAY_COLOR);
      String var10000 = QuantityFormatter.formatNumber((long)transaction.getQuantity());
      JLabel title = createWhiteLabel(var10000 + "x " + this.getItemName(transaction.getItemId()));
      JLabel priceEach = createMutedLabel(QuantityFormatter.formatNumber((long)transaction.getPrice()) + " gp each");
      long var11 = this.getTransactionTotal(transaction);
      JLabel priceTotal = createMutedLabel("(" + QuantityFormatter.formatNumber(var11) + " gp total)");
      JLabel timestamp = createMutedLabel(TIMESTAMP_FORMATTER.format(Instant.ofEpochMilli(transaction.getTimestampEpochMs())));
      title.setAlignmentX(0.0F);
      priceEach.setAlignmentX(0.0F);
      priceTotal.setAlignmentX(0.0F);
      timestamp.setAlignmentX(0.0F);
      info.add(title);
      info.add(priceEach);
      info.add(priceTotal);
      info.add(timestamp);
      panel.add(info, "Center");
      panel.addMouseListener(new MouseAdapter() {
         public void mouseEntered(MouseEvent e) {
            GrandExchangeHistoryPanel.setPanelBackground(panel, ColorScheme.DARK_GRAY_HOVER_COLOR);
         }

         public void mouseExited(MouseEvent e) {
            GrandExchangeHistoryPanel.setPanelBackground(panel, ColorScheme.DARKER_GRAY_COLOR);
         }

         public void mouseReleased(MouseEvent e) {
            GrandExchangeHistoryPanel.this.grandExchangePlugin.openGrandExchangeItem(transaction.getItemId(), GrandExchangeHistoryPanel.this.getItemName(transaction.getItemId()));
         }
      });
      JPanel wrapper = new JPanel(new BorderLayout());
      wrapper.setBackground(ColorScheme.DARK_GRAY_COLOR);
      wrapper.setBorder(new EmptyBorder(0, 0, 5, 0));
      wrapper.add(panel, "Center");
      return wrapper;
   }

   private String buildStatus(List<UpdateGrandExchangeHistoryScript.Transaction> visibleTransactions) {
      if (this.pendingHistoryRequestId != -1) {
         return "Loading Grand Exchange history...";
      } else if (this.transactions.isEmpty()) {
         return "No Grand Exchange history loaded.";
      } else {
         long volume = visibleTransactions.stream().mapToLong(UpdateGrandExchangeHistoryScript.Transaction::getQuantity).sum();
         long totalGp = visibleTransactions.stream().mapToLong(this::getTransactionTotal).sum();
         long average = volume == 0L ? 0L : totalGp / volume;
         return String.format("Page %d: %d of %d tx, %s items, avg %s gp", this.historyPage + 1, visibleTransactions.size(), this.transactions.size(), QuantityFormatter.formatNumber(volume), QuantityFormatter.formatNumber(average));
      }
   }

   private long getTransactionTotal(UpdateGrandExchangeHistoryScript.Transaction transaction) {
      return (long)transaction.getQuantity() * (long)transaction.getPrice();
   }

   private String getItemName(int itemId) {
      return (String)this.itemNameCache.getOrDefault(itemId, "Item " + itemId);
   }

   private Set<Integer> getTransactionItemIds(List<UpdateGrandExchangeHistoryScript.Transaction> transactionList) {
      return (Set)transactionList.stream().map(UpdateGrandExchangeHistoryScript.Transaction::getItemId).collect(Collectors.toCollection(LinkedHashSet::new));
   }

   private void primeItemNames(Set<Integer> itemIds, Runnable afterPrime) {
      Set<Integer> missingItemIds = (Set)itemIds.stream().filter((itemId) -> {
         return !this.itemNameCache.containsKey(itemId);
      }).collect(Collectors.toCollection(LinkedHashSet::new));
      if (missingItemIds.isEmpty()) {
         afterPrime.run();
      } else {
         this.clientThread.invokeLater(() -> {
            Map<Integer, String> names = new HashMap();
            Iterator var4 = missingItemIds.iterator();

            while(var4.hasNext()) {
               int itemId = (Integer)var4.next();
               names.put(itemId, this.getItemNameOnClientThread(itemId));
            }

            SwingUtilities.invokeLater(() -> {
               this.itemNameCache.putAll(names);
               afterPrime.run();
            });
         });
      }
   }

   private String getItemNameOnClientThread(int itemId) {
      ItemComposition composition = this.itemManager.getItemComposition(itemId);
      String name = composition == null ? null : composition.getName();
      return name != null && !name.isEmpty() ? name : "Item " + itemId;
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

   private static void setPanelBackground(JPanel panel, Color color) {
      panel.setBackground(color);
      Component[] var2 = panel.getComponents();
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         Component component = var2[var4];
         component.setBackground(color);
         if (component instanceof JPanel) {
            setPanelBackground((JPanel)component, color);
         }
      }

   }

   private static enum SortMode {
      NEWEST("Newest First"),
      OLDEST("Oldest First"),
      NAME("Item Name"),
      QUANTITY("Quantity"),
      PRICE("Price"),
      TOTAL("Total Value");

      private final String label;

      private SortMode(String label) {
         this.label = label;
      }

      public String toString() {
         return this.label;
      }
   }

   private static enum TimeRange {
      SIX_HOURS("Last 6 Hours", 6),
      TWELVE_HOURS("Last 12 Hours", 12),
      DAY("Last 24 Hours", 24),
      THREE_DAYS("Last 3 Days", 72);

      private final String label;
      private final int hours;

      private TimeRange(String label, int hours) {
         this.label = label;
         this.hours = hours;
      }

      public String toString() {
         return this.label;
      }
   }
}
