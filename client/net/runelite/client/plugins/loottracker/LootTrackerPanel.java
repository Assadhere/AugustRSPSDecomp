package net.runelite.client.plugins.loottracker;

import com.google.common.collect.Iterables;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Predicate;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButton;
import javax.swing.JToggleButton;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicButtonUI;
import javax.swing.plaf.basic.BasicToggleButtonUI;
import net.runelite.client.game.ItemManager;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.FontManager;
import net.runelite.client.ui.PluginPanel;
import net.runelite.client.ui.components.PluginErrorPanel;
import net.runelite.client.util.ColorUtil;
import net.runelite.client.util.ImageUtil;
import net.runelite.client.util.QuantityFormatter;
import net.runelite.client.util.SwingUtil;
import net.runelite.http.api.loottracker.LootRecordType;

class LootTrackerPanel extends PluginPanel {
   private static final int MAX_LOOT_BOXES = 500;
   private static final int MAX_SESSION_RECORDS = 1024;
   private static final ImageIcon SINGLE_LOOT_VIEW;
   private static final ImageIcon SINGLE_LOOT_VIEW_FADED;
   private static final ImageIcon SINGLE_LOOT_VIEW_HOVER;
   private static final ImageIcon GROUPED_LOOT_VIEW;
   private static final ImageIcon GROUPED_LOOT_VIEW_FADED;
   private static final ImageIcon GROUPED_LOOT_VIEW_HOVER;
   private static final ImageIcon BACK_ARROW_ICON;
   private static final ImageIcon BACK_ARROW_ICON_HOVER;
   private static final ImageIcon VISIBLE_ICON;
   private static final ImageIcon VISIBLE_ICON_HOVER;
   private static final ImageIcon INVISIBLE_ICON;
   private static final ImageIcon INVISIBLE_ICON_HOVER;
   private static final ImageIcon COLLAPSE_ICON;
   private static final ImageIcon EXPAND_ICON;
   private static final String HTML_LABEL_TEMPLATE = "<html><body style='color:%s'>%s<span style='color:white'>%s</span></body></html>";
   private static final String RESET_ALL_WARNING_TEXT = "<html>This will permanently delete <b>all</b> loot.</html>";
   private static final String RESET_CURRENT_WARNING_TEXT = "This will permanently delete \"%s\" loot.";
   private static final String RESET_ONE_WARNING_TEXT = "This will delete one kill.";
   private final PluginErrorPanel errorPanel = new PluginErrorPanel();
   private final JPanel logsContainer = new JPanel();
   private final JPanel overallPanel;
   private final JLabel overallKillsLabel = new JLabel();
   private final JLabel overallGpLabel = new JLabel();
   private final JLabel overallIcon = new JLabel();
   private final JPanel actionsPanel;
   private final JLabel detailsTitle = new JLabel();
   private final JButton backBtn = new JButton();
   private final JToggleButton viewHiddenBtn = new JToggleButton();
   private final JRadioButton singleLootBtn = new JRadioButton();
   private final JRadioButton groupedLootBtn = new JRadioButton();
   private final JButton collapseBtn = new JButton();
   private final LinkedHashMap<LootTrackerRecord, LootTrackerRecord> aggregateRecords = new LinkedHashMap(16, 0.75F, true);
   private final Deque<LootTrackerRecord> sessionRecords = new ArrayDeque();
   private final List<LootTrackerBox> boxes = new ArrayList();
   private final ItemManager itemManager;
   private final LootTrackerPlugin plugin;
   private final LootTrackerConfig config;
   private boolean groupLoot;
   private boolean hideIgnoredItems;
   private String currentView;
   private LootRecordType currentType;

   LootTrackerPanel(LootTrackerPlugin plugin, ItemManager itemManager, LootTrackerConfig config) {
      this.itemManager = itemManager;
      this.plugin = plugin;
      this.config = config;
      this.hideIgnoredItems = true;
      this.setBorder(new EmptyBorder(6, 6, 6, 6));
      this.setBackground(ColorScheme.DARK_GRAY_COLOR);
      this.setLayout(new BorderLayout());
      JPanel layoutPanel = new JPanel();
      layoutPanel.setLayout(new BoxLayout(layoutPanel, 1));
      this.add(layoutPanel, "North");
      this.actionsPanel = this.buildActionsPanel();
      this.overallPanel = this.buildOverallPanel();
      this.logsContainer.setLayout(new BoxLayout(this.logsContainer, 1));
      layoutPanel.add(this.actionsPanel);
      layoutPanel.add(this.overallPanel);
      layoutPanel.add(this.logsContainer);
      this.errorPanel.setContent("Loot tracker", "You have not received any loot yet.");
      this.add(this.errorPanel);
   }

   private JPanel buildActionsPanel() {
      JPanel actionsContainer = new JPanel();
      actionsContainer.setLayout(new BorderLayout());
      actionsContainer.setBackground(ColorScheme.DARKER_GRAY_COLOR);
      actionsContainer.setPreferredSize(new Dimension(0, 30));
      actionsContainer.setBorder(new EmptyBorder(5, 5, 5, 10));
      actionsContainer.setVisible(false);
      JPanel viewControls = new JPanel(new GridLayout(1, 3, 10, 0));
      viewControls.setBackground(ColorScheme.DARKER_GRAY_COLOR);
      SwingUtil.removeButtonDecorations(this.collapseBtn);
      this.collapseBtn.setIcon(EXPAND_ICON);
      this.collapseBtn.setSelectedIcon(COLLAPSE_ICON);
      SwingUtil.addModalTooltip(this.collapseBtn, "Expand All", "Collapse All");
      this.collapseBtn.setBackground(ColorScheme.DARKER_GRAY_COLOR);
      this.collapseBtn.setUI(new BasicButtonUI());
      this.collapseBtn.addActionListener((ev) -> {
         this.changeCollapse();
      });
      viewControls.add(this.collapseBtn);
      SwingUtil.removeButtonDecorations(this.singleLootBtn);
      this.singleLootBtn.setIcon(SINGLE_LOOT_VIEW_FADED);
      this.singleLootBtn.setRolloverIcon(SINGLE_LOOT_VIEW_HOVER);
      this.singleLootBtn.setSelectedIcon(SINGLE_LOOT_VIEW);
      this.singleLootBtn.setToolTipText("Show each kill separately");
      this.singleLootBtn.addActionListener((e) -> {
         this.changeGrouping(false);
      });
      SwingUtil.removeButtonDecorations(this.groupedLootBtn);
      this.groupedLootBtn.setIcon(GROUPED_LOOT_VIEW_FADED);
      this.groupedLootBtn.setRolloverIcon(GROUPED_LOOT_VIEW_HOVER);
      this.groupedLootBtn.setSelectedIcon(GROUPED_LOOT_VIEW);
      this.groupedLootBtn.setToolTipText("Group loot by source");
      this.groupedLootBtn.addActionListener((e) -> {
         this.changeGrouping(true);
      });
      ButtonGroup groupSingleGroup = new ButtonGroup();
      groupSingleGroup.add(this.singleLootBtn);
      groupSingleGroup.add(this.groupedLootBtn);
      viewControls.add(this.groupedLootBtn);
      viewControls.add(this.singleLootBtn);
      this.changeGrouping(true);
      SwingUtil.removeButtonDecorations(this.viewHiddenBtn);
      this.viewHiddenBtn.setIconTextGap(0);
      this.viewHiddenBtn.setIcon(VISIBLE_ICON);
      this.viewHiddenBtn.setRolloverIcon(INVISIBLE_ICON_HOVER);
      this.viewHiddenBtn.setSelectedIcon(INVISIBLE_ICON);
      this.viewHiddenBtn.setRolloverSelectedIcon(VISIBLE_ICON_HOVER);
      this.viewHiddenBtn.setBackground(ColorScheme.DARKER_GRAY_COLOR);
      this.viewHiddenBtn.setUI(new BasicToggleButtonUI());
      this.viewHiddenBtn.addActionListener((e) -> {
         this.changeItemHiding(this.viewHiddenBtn.isSelected());
      });
      SwingUtil.addModalTooltip(this.viewHiddenBtn, "Show ignored items", "Hide ignored items");
      this.changeItemHiding(true);
      viewControls.add(this.viewHiddenBtn);
      JPanel leftTitleContainer = new JPanel(new BorderLayout(5, 0));
      leftTitleContainer.setBackground(ColorScheme.DARKER_GRAY_COLOR);
      this.detailsTitle.setForeground(Color.WHITE);
      SwingUtil.removeButtonDecorations(this.backBtn);
      this.backBtn.setIcon(BACK_ARROW_ICON);
      this.backBtn.setRolloverIcon(BACK_ARROW_ICON_HOVER);
      this.backBtn.setVisible(false);
      this.backBtn.addActionListener((ev) -> {
         this.currentView = null;
         this.currentType = null;
         this.backBtn.setVisible(false);
         this.detailsTitle.setText("");
         this.rebuild();
      });
      leftTitleContainer.add(this.backBtn, "West");
      leftTitleContainer.add(this.detailsTitle, "Center");
      actionsContainer.add(viewControls, "East");
      actionsContainer.add(leftTitleContainer, "West");
      return actionsContainer;
   }

   private JPanel buildOverallPanel() {
      JPanel overallPanel = new JPanel();
      overallPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(5, 0, 0, 0, ColorScheme.DARK_GRAY_COLOR), BorderFactory.createEmptyBorder(8, 10, 8, 10)));
      overallPanel.setBackground(ColorScheme.DARKER_GRAY_COLOR);
      overallPanel.setLayout(new BorderLayout());
      overallPanel.setVisible(false);
      JPanel overallInfo = new JPanel();
      overallInfo.setBackground(ColorScheme.DARKER_GRAY_COLOR);
      overallInfo.setLayout(new GridLayout(2, 1));
      overallInfo.setBorder(new EmptyBorder(2, 10, 2, 0));
      this.overallKillsLabel.setFont(FontManager.getRunescapeSmallFont());
      this.overallGpLabel.setFont(FontManager.getRunescapeSmallFont());
      overallInfo.add(this.overallKillsLabel);
      overallInfo.add(this.overallGpLabel);
      overallPanel.add(this.overallIcon, "West");
      overallPanel.add(overallInfo, "Center");
      JMenuItem reset = new JMenuItem("Reset All");
      reset.addActionListener((e) -> {
         int result = JOptionPane.showOptionDialog(overallPanel, this.currentView == null ? "<html>This will permanently delete <b>all</b> loot.</html>" : String.format("This will permanently delete \"%s\" loot.", this.currentView), "Are you sure?", 0, 2, (Icon)null, new String[]{"Yes", "No"}, "No");
         if (result == 0) {
            this.sessionRecords.removeIf((r) -> {
               return r.matches(this.currentView, this.currentType);
            });
            this.aggregateRecords.values().removeIf((r) -> {
               return r.matches(this.currentView, this.currentType);
            });
            this.boxes.removeIf((b) -> {
               return b.matches(this.currentView, this.currentType);
            });
            this.updateOverall();
            this.logsContainer.removeAll();
            this.logsContainer.revalidate();
            if (this.currentView != null) {
               assert this.currentType != null;

               this.plugin.removeLootConfig(this.currentType, this.currentView);
            } else {
               this.plugin.removeAllLoot();
            }

         }
      });
      JPopupMenu popupMenu = new JPopupMenu();
      popupMenu.setBorder(new EmptyBorder(5, 5, 5, 5));
      popupMenu.add(reset);
      overallPanel.setComponentPopupMenu(popupMenu);
      return overallPanel;
   }

   void updateCollapseText() {
      this.collapseBtn.setSelected(this.isAllCollapsed());
   }

   private boolean isAllCollapsed() {
      return this.boxes.stream().filter(LootTrackerBox::isCollapsed).count() == (long)this.boxes.size();
   }

   void loadHeaderIcon(BufferedImage img) {
      this.overallIcon.setIcon(new ImageIcon(img));
   }

   void add(String eventName, LootRecordType type, int actorLevel, LootTrackerItem[] items, int kills) {
      String subTitle;
      if (type == LootRecordType.PICKPOCKET) {
         subTitle = "(pickpocket)";
      } else {
         subTitle = actorLevel > 0 ? "(lvl-" + actorLevel + ")" : "";
      }

      LootTrackerRecord sessRecord = new LootTrackerRecord(eventName, subTitle, type, items, kills);
      this.sessionRecords.add(sessRecord);
      if (this.sessionRecords.size() > 1024) {
         this.sessionRecords.removeFirst();
      }

      LootTrackerRecord aggRecord = (LootTrackerRecord)this.aggregateRecords.get(sessRecord);
      if (aggRecord != null) {
         aggRecord.merge(sessRecord);
      } else {
         aggRecord = new LootTrackerRecord(eventName, subTitle, type, items, kills);
         this.aggregateRecords.put(aggRecord, aggRecord);
      }

      if (!this.hideIgnoredItems || !this.plugin.isEventIgnored(eventName)) {
         LootTrackerBox box = this.buildBox(this.groupLoot ? aggRecord : sessRecord);
         if (box != null) {
            box.rebuild();
            this.updateOverall();
         }

      }
   }

   boolean hasRecord(LootRecordType type, String name) {
      LootTrackerRecord r = new LootTrackerRecord(name, (String)null, type, (LootTrackerItem[])null, 0);
      return this.aggregateRecords.containsKey(r);
   }

   void clearRecords() {
      this.aggregateRecords.clear();
      this.sessionRecords.clear();
   }

   void addRecords(Collection<LootTrackerRecord> recs) {
      recs.forEach((r) -> {
         this.aggregateRecords.put(r, r);
      });
      this.rebuild();
   }

   private void changeGrouping(boolean group) {
      this.groupLoot = group;
      (group ? this.groupedLootBtn : this.singleLootBtn).setSelected(true);
      this.rebuild();
   }

   private void changeItemHiding(boolean hide) {
      this.hideIgnoredItems = hide;
      this.viewHiddenBtn.setSelected(hide);
      this.rebuild();
   }

   private void changeCollapse() {
      boolean isAllCollapsed = this.isAllCollapsed();
      Iterator var2 = this.boxes.iterator();

      while(var2.hasNext()) {
         LootTrackerBox box = (LootTrackerBox)var2.next();
         if (isAllCollapsed) {
            box.expand();
         } else if (!box.isCollapsed()) {
            box.collapse();
         }
      }

      this.updateCollapseText();
   }

   void updateIgnoredRecords() {
      Iterator var1 = Iterables.concat(this.aggregateRecords.values(), this.sessionRecords).iterator();

      while(var1.hasNext()) {
         LootTrackerRecord record = (LootTrackerRecord)var1.next();
         LootTrackerItem[] var3 = record.getItems();
         int var4 = var3.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            LootTrackerItem item = var3[var5];
            item.setIgnored(this.plugin.isIgnored(item.getName()));
         }
      }

      this.rebuild();
   }

   void rebuild() {
      SwingUtil.fastRemoveAll(this.logsContainer);
      this.boxes.clear();
      if (this.groupLoot) {
         this.aggregateRecords.values().forEach(this::buildBox);
      } else {
         Iterator var1 = this.sessionRecords.iterator();

         label22:
         while(true) {
            LootTrackerRecord r;
            do {
               if (!var1.hasNext()) {
                  break label22;
               }

               r = (LootTrackerRecord)var1.next();
            } while(this.hideIgnoredItems && this.plugin.isEventIgnored(r.getTitle()));

            this.buildBox(r);
         }
      }

      this.boxes.forEach(LootTrackerBox::rebuild);
      this.updateOverall();
      this.logsContainer.revalidate();
   }

   private LootTrackerBox buildBox(LootTrackerRecord record) {
      if (!record.matches(this.currentView, this.currentType)) {
         return null;
      } else {
         boolean isIgnored = this.plugin.isEventIgnored(record.getTitle());
         if (this.hideIgnoredItems && isIgnored) {
            return null;
         } else {
            if (this.groupLoot) {
               Iterator var3 = this.boxes.iterator();

               while(var3.hasNext()) {
                  LootTrackerBox box = (LootTrackerBox)var3.next();
                  if (box.matches(record)) {
                     this.logsContainer.setComponentZOrder(box, 0);
                     return box;
                  }
               }
            }

            this.remove(this.errorPanel);
            this.actionsPanel.setVisible(true);
            this.overallPanel.setVisible(true);
            ItemManager var10002 = this.itemManager;
            boolean var10004 = this.hideIgnoredItems;
            LootTrackerPriceType var10005 = this.config.priceType();
            boolean var10006 = this.config.showPriceType();
            LootTrackerPlugin var10007 = this.plugin;
            Objects.requireNonNull(var10007);
            BiConsumer var7 = var10007::toggleItem;
            LootTrackerPlugin var10008 = this.plugin;
            Objects.requireNonNull(var10008);
            final LootTrackerBox box = new LootTrackerBox(var10002, record, var10004, var10005, var10006, var7, var10008::toggleEvent, isIgnored);
            JPopupMenu popupMenu = box.getComponentPopupMenu();
            if (popupMenu == null) {
               popupMenu = new JPopupMenu();
               popupMenu.setBorder(new EmptyBorder(5, 5, 5, 5));
               box.setComponentPopupMenu(popupMenu);
            }

            box.addMouseListener(new MouseAdapter() {
               public void mouseClicked(MouseEvent e) {
                  if (e.getButton() == 1) {
                     if (box.isCollapsed()) {
                        box.expand();
                     } else {
                        box.collapse();
                     }

                     LootTrackerPanel.this.updateCollapseText();
                  }

               }
            });
            JMenuItem reset = new JMenuItem("Reset");
            reset.addActionListener((e) -> {
               int result = JOptionPane.showOptionDialog(box, this.groupLoot ? String.format("This will permanently delete \"%s\" loot.", record.getTitle()) : "This will delete one kill.", "Are you sure?", 0, 2, (Icon)null, new String[]{"Yes", "No"}, "No");
               if (result == 0) {
                  Predicate<LootTrackerRecord> match = this.groupLoot ? (r) -> {
                     return r.matches(record.getTitle(), record.getType());
                  } : (r) -> {
                     return r.equals(record);
                  };
                  this.sessionRecords.removeIf(match);
                  this.aggregateRecords.values().removeIf(match);
                  this.boxes.remove(box);
                  this.updateOverall();
                  this.logsContainer.remove(box);
                  this.logsContainer.revalidate();
                  if (this.groupLoot) {
                     this.plugin.removeLootConfig(record.getType(), record.getTitle());
                  }

               }
            });
            popupMenu.add(reset);
            JMenuItem details = new JMenuItem("View details");
            details.addActionListener((e) -> {
               this.currentView = record.getTitle();
               this.currentType = record.getType();
               this.detailsTitle.setText(this.currentView);
               this.backBtn.setVisible(true);
               this.rebuild();
            });
            popupMenu.add(details);
            this.boxes.add(box);
            this.logsContainer.add(box, 0);
            if (!this.groupLoot && this.boxes.size() > 500) {
               this.logsContainer.remove((Component)this.boxes.remove(0));
            }

            return box;
         }
      }
   }

   private void updateOverall() {
      long overallKills = 0L;
      long overallGe = 0L;
      long overallHa = 0L;
      Iterable<LootTrackerRecord> records = this.groupLoot ? this.aggregateRecords.values() : this.sessionRecords;
      Iterator var8 = ((Iterable)records).iterator();

      while(true) {
         LootTrackerRecord record;
         do {
            do {
               if (!var8.hasNext()) {
                  String priceType = "";
                  if (this.config.showPriceType()) {
                     priceType = this.config.priceType() == LootTrackerPriceType.HIGH_ALCHEMY ? "HA " : "GE ";
                  }

                  this.overallKillsLabel.setText(htmlLabel("Total count: ", overallKills));
                  this.overallGpLabel.setText(htmlLabel("Total " + priceType + "value: ", this.config.priceType() == LootTrackerPriceType.HIGH_ALCHEMY ? overallHa : overallGe));
                  JLabel var10000 = this.overallGpLabel;
                  String var10001 = QuantityFormatter.formatNumber(overallGe);
                  var10000.setToolTipText("<html>Total GE price: " + var10001 + "<br>Total HA price: " + QuantityFormatter.formatNumber(overallHa) + "</html>");
                  this.updateCollapseText();
                  return;
               }

               record = (LootTrackerRecord)var8.next();
            } while(!record.matches(this.currentView, this.currentType));
         } while(this.hideIgnoredItems && this.plugin.isEventIgnored(record.getTitle()));

         int present = record.getItems().length;
         LootTrackerItem[] var11 = record.getItems();
         int var12 = var11.length;

         for(int var13 = 0; var13 < var12; ++var13) {
            LootTrackerItem item = var11[var13];
            if (this.hideIgnoredItems && item.isIgnored()) {
               --present;
            } else {
               overallGe += item.getTotalGePrice();
               overallHa += item.getTotalHaPrice();
            }
         }

         if (present > 0) {
            overallKills += (long)record.getKills();
         }
      }
   }

   private static String htmlLabel(String key, long value) {
      String valueStr = QuantityFormatter.quantityToStackSize(value);
      return String.format("<html><body style='color:%s'>%s<span style='color:white'>%s</span></body></html>", ColorUtil.toHexColor(ColorScheme.LIGHT_GRAY_COLOR), key, valueStr);
   }

   static {
      BufferedImage singleLootImg = ImageUtil.loadImageResource(LootTrackerPlugin.class, "single_loot_icon.png");
      BufferedImage groupedLootImg = ImageUtil.loadImageResource(LootTrackerPlugin.class, "grouped_loot_icon.png");
      BufferedImage backArrowImg = ImageUtil.loadImageResource(LootTrackerPlugin.class, "back_icon.png");
      BufferedImage visibleImg = ImageUtil.loadImageResource(LootTrackerPlugin.class, "visible_icon.png");
      BufferedImage invisibleImg = ImageUtil.loadImageResource(LootTrackerPlugin.class, "invisible_icon.png");
      BufferedImage collapseImg = ImageUtil.loadImageResource(LootTrackerPlugin.class, "collapsed.png");
      BufferedImage expandedImg = ImageUtil.loadImageResource(LootTrackerPlugin.class, "expanded.png");
      SINGLE_LOOT_VIEW = new ImageIcon(singleLootImg);
      SINGLE_LOOT_VIEW_FADED = new ImageIcon(ImageUtil.alphaOffset(singleLootImg, -180));
      SINGLE_LOOT_VIEW_HOVER = new ImageIcon(ImageUtil.alphaOffset(singleLootImg, -220));
      GROUPED_LOOT_VIEW = new ImageIcon(groupedLootImg);
      GROUPED_LOOT_VIEW_FADED = new ImageIcon(ImageUtil.alphaOffset(groupedLootImg, -180));
      GROUPED_LOOT_VIEW_HOVER = new ImageIcon(ImageUtil.alphaOffset(groupedLootImg, -220));
      BACK_ARROW_ICON = new ImageIcon(backArrowImg);
      BACK_ARROW_ICON_HOVER = new ImageIcon(ImageUtil.alphaOffset(backArrowImg, -180));
      VISIBLE_ICON = new ImageIcon(visibleImg);
      VISIBLE_ICON_HOVER = new ImageIcon(ImageUtil.alphaOffset(visibleImg, -220));
      INVISIBLE_ICON = new ImageIcon(invisibleImg);
      INVISIBLE_ICON_HOVER = new ImageIcon(ImageUtil.alphaOffset(invisibleImg, -220));
      COLLAPSE_ICON = new ImageIcon(collapseImg);
      EXPAND_ICON = new ImageIcon(expandedImg);
   }
}
