package net.runelite.client.plugins.hiscore;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import custom.UpdateHiscoreTopListScript;
import custom.UpdateHiscoreTopListScript.TopListType;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.IntConsumer;
import javax.inject.Inject;
import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollPane;
import javax.swing.Scrollable;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import net.runelite.client.game.SpriteManager;
import net.runelite.client.hiscore.CountCategory;
import net.runelite.client.hiscore.CountId;
import net.runelite.client.hiscore.HiscoreSkill;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.FontManager;
import net.runelite.client.ui.components.materialtabs.MaterialTab;
import net.runelite.client.ui.components.materialtabs.MaterialTabGroup;
import net.runelite.client.util.ImageUtil;
import net.runelite.client.util.QuantityFormatter;

class HiscoreTopListPanel extends JPanel {
   private static final int PAGE_SIZE = 25;
   private static final int REQUEST_TIMEOUT_MILLIS = 30000;
   private static final Color BUTTON_SELECTED;
   private static final Color BUTTON_UNSELECTED;
   private static final Color FIRST_PLACE_BORDER;
   private static final Color SECOND_PLACE_BORDER;
   private static final Color THIRD_PLACE_BORDER;
   private static final int CHAT_ICON_ARCHIVE = 423;
   private static final FilterOption ANY_IRONMAN_FILTER;
   private static final FilterOption NO_IRONMAN_FILTER;
   private static final List<FilterOption> IRONMAN_FILTERS;
   private static final FilterOption ANY_GAME_MODE_FILTER;
   private static final FilterOption NO_GAME_MODE_FILTER;
   private static final List<FilterOption> GAME_MODE_FILTERS;
   private static final List<HiscoreSkill> TOPLIST_SKILLS;
   private static final Map<HiscoreSkill, String> TOPLIST_SKILL_IDS;
   private final SpriteManager spriteManager;
   private final JLabel titleLabel = new JLabel("Top Lists");
   private final JLabel statusLabel = new JLabel("Select a skill or count to load rankings.");
   private final JButton browseButton = createActionButton("Browse");
   private final JButton previousButton = createActionButton("Prev");
   private final JButton nextButton = createActionButton("Next");
   private final JButton ironmanFilterButton;
   private final JButton gameModeFilterButton;
   private final JPanel entriesPanel;
   private final JPanel paginationPanel;
   private final JPanel selectorDisplay;
   private final MaterialTabGroup selectorTabs;
   private final MaterialTab skillsTab;
   private final MaterialTab countsTab;
   private final JPanel skillSelectorPanel;
   private final JPanel countSelectorPanel;
   private final JScrollPane selectorScrollPane;
   private final JPanel selectorContainer;
   private final Map<HiscoreSkill, JButton> skillButtons;
   private final Map<String, JButton> countButtons;
   private final Set<String> extraCountTargets;
   private Map<String, Integer> countGraphicMapping;
   private TopListRequestHandler requestHandler;
   private UpdateHiscoreTopListScript.TopListType currentType;
   private String currentTargetId;
   private String currentTargetName;
   private int currentPage;
   private int currentPageCount;
   private int currentIronmanModeFilter;
   private int currentGameModeFilter;
   private int expectedRequestId;
   private boolean pending;
   private Timer timeoutTimer;

   @Inject
   HiscoreTopListPanel(SpriteManager spriteManager) {
      this.ironmanFilterButton = createActionButton(ANY_IRONMAN_FILTER.getLabel());
      this.gameModeFilterButton = createActionButton(ANY_GAME_MODE_FILTER.getLabel());
      this.entriesPanel = new ViewportWidthPanel();
      this.paginationPanel = new JPanel(new FlowLayout(0, 4, 0));
      this.selectorDisplay = new JPanel();
      this.selectorTabs = new MaterialTabGroup(this.selectorDisplay);
      this.skillSelectorPanel = new JPanel();
      this.countSelectorPanel = new JPanel();
      this.selectorContainer = new JPanel();
      this.skillButtons = new LinkedHashMap();
      this.countButtons = new LinkedHashMap();
      this.extraCountTargets = new LinkedHashSet();
      this.countGraphicMapping = Collections.emptyMap();
      this.requestHandler = (ignoredType, ignoredTarget, ignoredPage, ignoredPageSize, ignoredIronFilter, ignoredGameModeFilter) -> {
         return -1;
      };
      this.currentIronmanModeFilter = ANY_IRONMAN_FILTER.getValue();
      this.currentGameModeFilter = ANY_GAME_MODE_FILTER.getValue();
      this.expectedRequestId = -1;
      this.spriteManager = spriteManager;
      this.setLayout(new BorderLayout(0, 8));
      this.setBackground(ColorScheme.DARK_GRAY_COLOR);
      this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
      this.titleLabel.setFont(FontManager.getRunescapeBoldFont());
      this.titleLabel.setForeground(ColorScheme.BRAND_ORANGE);
      this.titleLabel.setAlignmentX(0.0F);
      this.titleLabel.setIconTextGap(6);
      this.titleLabel.setHorizontalTextPosition(4);
      this.statusLabel.setFont(FontManager.getRunescapeSmallFont());
      this.statusLabel.setForeground(ColorScheme.LIGHT_GRAY_COLOR);
      this.statusLabel.setAlignmentX(0.0F);
      JPanel summaryPanel = new JPanel();
      summaryPanel.setLayout(new BoxLayout(summaryPanel, 1));
      summaryPanel.setOpaque(false);
      summaryPanel.add(this.titleLabel);
      summaryPanel.add(Box.createVerticalStrut(2));
      summaryPanel.add(this.statusLabel);
      JPanel controlsPanel = new JPanel(new FlowLayout(0, 6, 0));
      controlsPanel.setOpaque(false);
      this.browseButton.addActionListener((e) -> {
         this.toggleSelectorPanel();
      });
      this.previousButton.addActionListener((e) -> {
         this.requestPage(this.currentPage - 1);
      });
      this.nextButton.addActionListener((e) -> {
         this.requestPage(this.currentPage + 1);
      });
      controlsPanel.add(this.browseButton);
      controlsPanel.add(this.previousButton);
      controlsPanel.add(this.nextButton);
      JPanel filtersPanel = new JPanel(new FlowLayout(0, 6, 0));
      filtersPanel.setOpaque(false);
      this.ironmanFilterButton.addActionListener((e) -> {
         this.showFilterMenu(this.ironmanFilterButton, IRONMAN_FILTERS, this.currentIronmanModeFilter, this::setIronmanModeFilter);
      });
      this.gameModeFilterButton.addActionListener((e) -> {
         this.showFilterMenu(this.gameModeFilterButton, GAME_MODE_FILTERS, this.currentGameModeFilter, this::setGameModeFilter);
      });
      filtersPanel.add(this.ironmanFilterButton);
      filtersPanel.add(this.gameModeFilterButton);
      this.refreshFilterButtonLabels();
      JPanel actionsPanel = new JPanel();
      actionsPanel.setLayout(new BoxLayout(actionsPanel, 1));
      actionsPanel.setOpaque(false);
      actionsPanel.add(controlsPanel);
      actionsPanel.add(Box.createVerticalStrut(6));
      actionsPanel.add(filtersPanel);
      JPanel headerPanel = new JPanel(new BorderLayout(0, 8));
      headerPanel.setOpaque(false);
      headerPanel.add(summaryPanel, "North");
      headerPanel.add(actionsPanel, "Center");
      this.buildSkillSelectorPanel();
      this.rebuildCountSelectorPanel();
      this.skillsTab = new MaterialTab("Skills", this.selectorTabs, this.skillSelectorPanel);
      this.countsTab = new MaterialTab("Counts", this.selectorTabs, this.countSelectorPanel);
      this.selectorTabs.setLayout(new FlowLayout(0, 8, 0));
      this.selectorTabs.setBorder(new EmptyBorder(0, 0, 6, 0));
      this.selectorTabs.addTab(this.skillsTab);
      this.selectorTabs.addTab(this.countsTab);
      this.selectorTabs.select(this.skillsTab);
      this.selectorContainer.setLayout(new BorderLayout(0, 6));
      this.selectorContainer.setBackground(ColorScheme.DARKER_GRAY_COLOR);
      this.selectorContainer.setBorder(new EmptyBorder(8, 8, 8, 8));
      this.selectorContainer.add(this.selectorTabs, "North");
      this.selectorScrollPane = new JScrollPane(this.selectorDisplay);
      this.selectorScrollPane.setHorizontalScrollBarPolicy(31);
      this.selectorScrollPane.setBorder(BorderFactory.createEmptyBorder());
      this.selectorScrollPane.getViewport().setBackground(ColorScheme.DARKER_GRAY_COLOR);
      this.selectorScrollPane.setPreferredSize(new Dimension(205, 220));
      this.selectorContainer.add(this.selectorScrollPane, "Center");
      this.selectorContainer.setVisible(true);
      JPanel northPanel = new JPanel(new GridBagLayout());
      northPanel.setOpaque(false);
      GridBagConstraints northConstraints = new GridBagConstraints();
      northConstraints.gridx = 0;
      northConstraints.gridy = 0;
      northConstraints.weightx = 1.0;
      northConstraints.fill = 2;
      northConstraints.anchor = 17;
      northPanel.add(headerPanel, northConstraints);
      ++northConstraints.gridy;
      northConstraints.insets = new Insets(8, 0, 0, 0);
      northPanel.add(this.selectorContainer, northConstraints);
      this.add(northPanel, "North");
      this.entriesPanel.setLayout(new BoxLayout(this.entriesPanel, 1));
      this.entriesPanel.setBackground(ColorScheme.DARK_GRAY_COLOR);
      JScrollPane entriesScrollPane = new JScrollPane(this.entriesPanel);
      entriesScrollPane.setHorizontalScrollBarPolicy(31);
      entriesScrollPane.setBorder(BorderFactory.createEmptyBorder());
      entriesScrollPane.getViewport().setBackground(ColorScheme.DARK_GRAY_COLOR);
      this.add(entriesScrollPane, "Center");
      this.paginationPanel.setOpaque(false);
      this.paginationPanel.setBorder(new EmptyBorder(4, 0, 0, 0));
      this.add(this.paginationPanel, "South");
      this.updateNavigationState();
      this.browseButton.setText("Hide");
      this.showPlaceholder("Select a skill or count from Browse.");
   }

   void setRequestHandler(TopListRequestHandler requestHandler) {
      this.requestHandler = requestHandler == null ? (ignoredType, ignoredTarget, ignoredPage, ignoredPageSize, ignoredIronFilter, ignoredGameModeFilter) -> {
         return -1;
      } : requestHandler;
   }

   void shutdown() {
      if (this.timeoutTimer != null) {
         this.timeoutTimer.stop();
         this.timeoutTimer = null;
      }

      this.requestHandler = (ignoredType, ignoredTarget, ignoredPage, ignoredPageSize, ignoredIronFilter, ignoredGameModeFilter) -> {
         return -1;
      };
      this.refreshFilterButtonLabels();
      this.pending = false;
      this.expectedRequestId = -1;
      this.updateNavigationState();
   }

   void setCountGraphicMapping(Map<String, Integer> mapping) {
      assert SwingUtilities.isEventDispatchThread();

      this.countGraphicMapping = mapping;
      this.rebuildCountSelectorPanel();
      this.refreshButtonSelectionState();
   }

   void showTarget(UpdateHiscoreTopListScript.TopListType type, String targetId) {
      assert SwingUtilities.isEventDispatchThread();

      if (type == TopListType.COUNT) {
         this.ensureCountTarget(targetId);
         this.selectorTabs.select(this.countsTab);
      } else {
         this.selectorTabs.select(this.skillsTab);
      }

      this.currentType = type;
      this.currentTargetId = targetId;
      this.currentTargetName = this.resolveTargetName(type, targetId);
      this.currentPage = 0;
      this.currentPageCount = 0;
      this.selectorContainer.setVisible(false);
      this.browseButton.setText("Browse");
      this.refreshButtonSelectionState();
      this.requestPage(0);
   }

   void displayTopListData(UpdateHiscoreTopListScript packet) {
      assert SwingUtilities.isEventDispatchThread();

      if (packet.getRequestId() == this.expectedRequestId) {
         this.finishPendingRequest();
         this.currentType = packet.getType();
         this.currentTargetId = packet.getTargetId();
         this.currentTargetName = packet.getTargetName();
         this.currentPage = packet.getPage();
         this.currentPageCount = packet.getPageCount();
         if (this.currentType == TopListType.COUNT) {
            this.ensureCountTarget(this.currentTargetId);
            this.selectorTabs.select(this.countsTab);
         } else {
            this.selectorTabs.select(this.skillsTab);
         }

         this.refreshButtonSelectionState();
         this.updateHeaderState();
         this.renderEntries(packet);
         this.updateNavigationState();
      }
   }

   private void buildSkillSelectorPanel() {
      this.skillSelectorPanel.setLayout(new GridLayout(0, 2, 6, 6));
      this.skillSelectorPanel.setBackground(ColorScheme.DARKER_GRAY_COLOR);
      Iterator var1 = TOPLIST_SKILLS.iterator();

      while(var1.hasNext()) {
         HiscoreSkill skill = (HiscoreSkill)var1.next();
         JButton button = createSelectionButton(skill.getName());
         this.loadSprite(button, skill == HiscoreSkill.OVERALL ? 222 : skill.getSpriteId());
         button.addActionListener((e) -> {
            this.showTarget(TopListType.SKILL, (String)TOPLIST_SKILL_IDS.get(skill));
         });
         this.skillButtons.put(skill, button);
         this.skillSelectorPanel.add(button);
      }

   }

   private void rebuildCountSelectorPanel() {
      this.countButtons.clear();
      this.countSelectorPanel.removeAll();
      this.countSelectorPanel.setLayout(new BoxLayout(this.countSelectorPanel, 1));
      this.countSelectorPanel.setBackground(ColorScheme.DARKER_GRAY_COLOR);
      Map<CountCategory, List<String>> idsByCategory = new EnumMap(CountCategory.class);
      Iterator var2 = this.getSelectableCountIds().iterator();

      CountCategory category;
      while(var2.hasNext()) {
         String countId = (String)var2.next();
         CountId knownCount = CountId.fromId(countId);
         category = knownCount == null ? CountCategory.OTHER : knownCount.getCategory();
         ((List)idsByCategory.computeIfAbsent(category, (ignored) -> {
            return new ArrayList();
         })).add(countId);
      }

      CountCategory[] var13 = CountCategory.values();
      int var14 = var13.length;

      for(int var15 = 0; var15 < var14; ++var15) {
         category = var13[var15];
         List<String> ids = (List)idsByCategory.get(category);
         if (ids != null && !ids.isEmpty()) {
            JLabel header = new JLabel(category.getDisplayName());
            header.setFont(FontManager.getRunescapeBoldFont());
            header.setForeground(ColorScheme.BRAND_ORANGE);
            header.setAlignmentX(0.0F);
            this.countSelectorPanel.add(header);
            this.countSelectorPanel.add(Box.createVerticalStrut(4));
            JPanel categoryPanel = new JPanel(new GridLayout(0, 1, 0, 4));
            categoryPanel.setAlignmentX(0.0F);
            categoryPanel.setBackground(ColorScheme.DARKER_GRAY_COLOR);
            Iterator var9 = ids.iterator();

            while(var9.hasNext()) {
               String countId = (String)var9.next();
               JButton button = createSelectionButton(this.resolveTargetName(TopListType.COUNT, countId));
               Integer graphic = (Integer)this.countGraphicMapping.get(countId);
               if (graphic != null && graphic >= 0) {
                  this.loadSprite(button, graphic);
               }

               button.addActionListener((e) -> {
                  this.showTarget(TopListType.COUNT, countId);
               });
               this.countButtons.put(countId, button);
               categoryPanel.add(button);
            }

            this.countSelectorPanel.add(categoryPanel);
            this.countSelectorPanel.add(Box.createVerticalStrut(8));
         }
      }

      this.countSelectorPanel.revalidate();
      this.countSelectorPanel.repaint();
   }

   private Set<String> getSelectableCountIds() {
      Set<String> ids = new LinkedHashSet();
      CountId[] var2 = CountId.values();
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         CountId countId = var2[var4];
         ids.add(countId.getId());
      }

      ids.addAll(this.countGraphicMapping.keySet());
      ids.addAll(this.extraCountTargets);
      return ids;
   }

   private void ensureCountTarget(String targetId) {
      if (!this.countButtons.containsKey(targetId)) {
         if (this.extraCountTargets.add(targetId)) {
            this.rebuildCountSelectorPanel();
         }

      }
   }

   private void requestPage(int page) {
      if (this.currentType != null && this.currentTargetId != null && !this.pending) {
         if (page >= 0) {
            if (this.currentPageCount <= 0 || page < this.currentPageCount) {
               this.currentPage = page;
               this.updateHeaderState();
               this.showPlaceholder("Loading rankings...");
               this.pending = true;
               this.expectedRequestId = this.requestHandler.request(this.currentType, this.currentTargetId, this.currentPage, 25, this.currentIronmanModeFilter, this.currentGameModeFilter);
               if (this.expectedRequestId < 0) {
                  this.pending = false;
                  this.expectedRequestId = -1;
                  this.statusLabel.setText("Top lists are unavailable right now.");
                  this.showPlaceholder("Unable to send request.");
                  this.updateNavigationState();
               } else {
                  this.timeoutTimer = new Timer(30000, (e) -> {
                     this.pending = false;
                     this.expectedRequestId = -1;
                     this.statusLabel.setText("Timed out waiting for rankings.");
                     this.showPlaceholder("Request timed out.");
                     this.updateNavigationState();
                  });
                  this.timeoutTimer.setRepeats(false);
                  this.timeoutTimer.start();
                  this.updateNavigationState();
               }
            }
         }
      }
   }

   private void finishPendingRequest() {
      this.pending = false;
      this.expectedRequestId = -1;
      if (this.timeoutTimer != null) {
         this.timeoutTimer.stop();
         this.timeoutTimer = null;
      }

   }

   private void toggleSelectorPanel() {
      boolean show = !this.selectorContainer.isVisible();
      this.selectorContainer.setVisible(show);
      this.browseButton.setText(show ? "Hide" : "Browse");
      this.revalidate();
      this.repaint();
   }

   private void setIronmanModeFilter(int filterValue) {
      if (this.currentIronmanModeFilter != filterValue) {
         this.currentIronmanModeFilter = filterValue;
         this.refreshFilterButtonLabels();
         this.reloadCurrentTargetForFilters();
      }
   }

   private void setGameModeFilter(int filterValue) {
      if (this.currentGameModeFilter != filterValue) {
         this.currentGameModeFilter = filterValue;
         this.refreshFilterButtonLabels();
         this.reloadCurrentTargetForFilters();
      }
   }

   private void reloadCurrentTargetForFilters() {
      this.finishPendingRequest();
      if (this.currentType != null && this.currentTargetId != null) {
         this.currentPage = 0;
         this.currentPageCount = 0;
         this.updateHeaderState();
         this.requestPage(0);
      } else {
         this.updateHeaderState();
         this.updateNavigationState();
      }
   }

   private void refreshFilterButtonLabels() {
      FilterOption ironmanFilter = resolveFilterOption(IRONMAN_FILTERS, this.currentIronmanModeFilter, ANY_IRONMAN_FILTER);
      FilterOption gameModeFilter = resolveFilterOption(GAME_MODE_FILTERS, this.currentGameModeFilter, ANY_GAME_MODE_FILTER);
      this.applyFilterButtonState(this.ironmanFilterButton, "Iron: ", ironmanFilter);
      this.applyFilterButtonState(this.gameModeFilterButton, "Mode: ", gameModeFilter);
   }

   private void applyFilterButtonState(JButton button, String prefix, FilterOption filter) {
      button.setText(prefix + filter.getLabel());
      button.setToolTipText(filter.getTooltip());
      button.setIcon((Icon)null);
      this.applyChatIcon(button, filter.getIconFile(), 14);
   }

   private void showFilterMenu(JButton anchor, List<FilterOption> options, int currentValue, IntConsumer onSelect) {
      JPopupMenu menu = new JPopupMenu();
      Iterator var6 = options.iterator();

      while(var6.hasNext()) {
         FilterOption option = (FilterOption)var6.next();
         JRadioButtonMenuItem item = new JRadioButtonMenuItem(option.getLabel(), option.getValue() == currentValue);
         item.setToolTipText(option.getTooltip());
         this.applyChatIcon(item, option.getIconFile(), 14);
         item.addActionListener((e) -> {
            onSelect.accept(option.getValue());
         });
         menu.add(item);
      }

      menu.show(anchor, 0, anchor.getHeight());
   }

   private void applyChatIcon(AbstractButton button, int iconFile, int size) {
      if (iconFile >= 0) {
         this.spriteManager.getSpriteAsync(423, iconFile, (Consumer)((sprite) -> {
            SwingUtilities.invokeLater(() -> {
               BufferedImage scaledSprite = ImageUtil.resizeImage(sprite, size, size);
               button.setIcon(new ImageIcon(scaledSprite));
            });
         }));
      }
   }

   private static FilterOption resolveFilterOption(List<FilterOption> options, int value, FilterOption fallback) {
      Iterator var3 = options.iterator();

      FilterOption option;
      do {
         if (!var3.hasNext()) {
            return fallback;
         }

         option = (FilterOption)var3.next();
      } while(option.getValue() != value);

      return option;
   }

   private void updateHeaderState() {
      if (this.currentType != null && this.currentTargetId != null) {
         String targetName = this.currentTargetName == null ? this.resolveTargetName(this.currentType, this.currentTargetId) : this.currentTargetName;
         this.titleLabel.setText(targetName + " Top List");
         this.updateTitleIcon(this.currentType, this.currentTargetId);
         if (this.pending) {
            this.statusLabel.setText("Loading page " + (this.currentPage + 1) + "...");
         } else if (this.currentPageCount > 0) {
            int var10001 = this.currentPage + 1;
            this.statusLabel.setText("Page " + var10001 + " of " + this.currentPageCount);
         } else {
            this.statusLabel.setText("No rankings loaded yet.");
         }

      } else {
         this.titleLabel.setText("Top Lists");
         this.titleLabel.setIcon((Icon)null);
         this.statusLabel.setText("Select a skill or count to load rankings.");
      }
   }

   private void updateNavigationState() {
      boolean hasSelection = this.currentType != null && this.currentTargetId != null;
      this.previousButton.setEnabled(hasSelection && !this.pending && this.currentPage > 0);
      this.nextButton.setEnabled(hasSelection && !this.pending && this.currentPageCount > 0 && this.currentPage + 1 < this.currentPageCount);
      this.rebuildPaginationControls();
   }

   private void rebuildPaginationControls() {
      this.paginationPanel.removeAll();
      boolean hasPages = this.currentType != null && this.currentTargetId != null && this.currentPageCount > 0;
      this.paginationPanel.setVisible(hasPages);
      if (!hasPages) {
         this.paginationPanel.revalidate();
         this.paginationPanel.repaint();
      } else {
         int lastPage = this.currentPageCount - 1;
         this.paginationPanel.add(this.createPaginationButton("<<", 0, !this.pending && this.currentPage > 0, false));
         this.paginationPanel.add(this.createPaginationButton("<", this.currentPage - 1, !this.pending && this.currentPage > 0, false));
         int previousVisiblePage = -1;

         int pageNumber;
         for(Iterator var4 = this.buildVisiblePageNumbers().iterator(); var4.hasNext(); previousVisiblePage = pageNumber) {
            pageNumber = (Integer)var4.next();
            if (previousVisiblePage >= 0 && pageNumber - previousVisiblePage > 1) {
               JLabel ellipsis = new JLabel("...");
               ellipsis.setFont(FontManager.getRunescapeSmallFont());
               ellipsis.setForeground(ColorScheme.LIGHT_GRAY_COLOR);
               this.paginationPanel.add(ellipsis);
            }

            this.paginationPanel.add(this.createPaginationButton(Integer.toString(pageNumber + 1), pageNumber, !this.pending && pageNumber != this.currentPage, pageNumber == this.currentPage));
         }

         this.paginationPanel.add(this.createPaginationButton(">", this.currentPage + 1, !this.pending && this.currentPage < lastPage, false));
         this.paginationPanel.add(this.createPaginationButton(">>", lastPage, !this.pending && this.currentPage < lastPage, false));
         this.paginationPanel.revalidate();
         this.paginationPanel.repaint();
      }
   }

   private List<Integer> buildVisiblePageNumbers() {
      int lastPage = this.currentPageCount - 1;
      List<Integer> pages = new ArrayList();
      if (lastPage < 0) {
         return pages;
      } else {
         int windowStart;
         if (this.currentPageCount <= 7) {
            for(windowStart = 0; windowStart <= lastPage; ++windowStart) {
               pages.add(windowStart);
            }

            return pages;
         } else {
            pages.add(0);
            int windowEnd;
            if (this.currentPage <= 2) {
               windowStart = 1;
               windowEnd = 3;
            } else if (this.currentPage >= lastPage - 2) {
               windowStart = Math.max(1, lastPage - 3);
               windowEnd = lastPage - 1;
            } else {
               windowStart = this.currentPage - 1;
               windowEnd = this.currentPage + 1;
            }

            for(int page = windowStart; page <= windowEnd; ++page) {
               pages.add(page);
            }

            pages.add(lastPage);
            return pages;
         }
      }
   }

   private void renderEntries(UpdateHiscoreTopListScript packet) {
      this.entriesPanel.removeAll();
      if (packet.getItems().isEmpty()) {
         this.showPlaceholder("No ranked players found.");
      } else {
         Iterator var2 = packet.getItems().iterator();

         while(var2.hasNext()) {
            UpdateHiscoreTopListScript.Entry entry = (UpdateHiscoreTopListScript.Entry)var2.next();
            this.entriesPanel.add(this.createEntryPanel(packet.getType(), entry));
            this.entriesPanel.add(Box.createVerticalStrut(6));
         }

         this.entriesPanel.revalidate();
         this.entriesPanel.repaint();
      }
   }

   private JPanel createEntryPanel(UpdateHiscoreTopListScript.TopListType type, UpdateHiscoreTopListScript.Entry entry) {
      JPanel row = new JPanel(new BorderLayout(4, 0));
      row.setBackground(ColorScheme.DARKER_GRAY_COLOR);
      row.setBorder(this.createEntryBorder(entry.getRank()));
      row.setAlignmentX(0.0F);
      row.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
      JLabel rankLabel = new JLabel("#" + entry.getRank());
      rankLabel.setFont(FontManager.getRunescapeBoldFont());
      rankLabel.setForeground(ColorScheme.BRAND_ORANGE);
      rankLabel.setVerticalAlignment(1);
      row.add(rankLabel, "West");
      JPanel centerPanel = new JPanel();
      centerPanel.setLayout(new BoxLayout(centerPanel, 1));
      centerPanel.setBackground(ColorScheme.DARKER_GRAY_COLOR);
      centerPanel.setMinimumSize(new Dimension(0, 0));
      JPanel nameLine = new JPanel();
      nameLine.setLayout(new BoxLayout(nameLine, 0));
      nameLine.setBackground(ColorScheme.DARKER_GRAY_COLOR);
      nameLine.setAlignmentX(0.0F);
      this.addModeIcon(nameLine, this.resolveIronmanIconFile(entry.getIronmanMode()), this.buildIronmanTooltip(entry.getIronmanMode()));
      this.addModeIcon(nameLine, this.resolveGameModeIconFile(entry.getGameMode()), this.buildGameModeTooltip(entry.getGameMode()));
      JLabel name = new JLabel(entry.getDisplayName());
      name.setFont(FontManager.getRunescapeBoldFont());
      name.setForeground(Color.WHITE);
      name.setToolTipText(entry.getDisplayName());
      nameLine.add(name);
      centerPanel.add(nameLine);
      Iterator var8 = this.buildEntryDetailLines(type, entry).iterator();

      while(var8.hasNext()) {
         String detailText = (String)var8.next();
         centerPanel.add(Box.createVerticalStrut(2));
         JLabel detail = new JLabel(detailText);
         detail.setFont(FontManager.getRunescapeSmallFont());
         detail.setForeground(ColorScheme.LIGHT_GRAY_COLOR);
         detail.setToolTipText(detailText);
         centerPanel.add(detail);
      }

      row.add(centerPanel, "Center");
      JLabel value = new JLabel(this.buildEntryValue(type, entry), 4);
      value.setFont(FontManager.getRunescapeBoldFont());
      value.setForeground(Color.WHITE);
      value.setVerticalAlignment(1);
      value.setToolTipText(this.buildEntryValueTooltip(type, entry));
      row.add(value, "East");
      return row;
   }

   private void addModeIcon(JPanel parent, int iconFile, String tooltip) {
      if (iconFile >= 0 && tooltip != null && !tooltip.isEmpty()) {
         JLabel iconLabel = new JLabel();
         iconLabel.setToolTipText(tooltip);
         iconLabel.setBorder(new EmptyBorder(0, 0, 0, 4));
         parent.add(iconLabel);
         this.spriteManager.getSpriteAsync(423, iconFile, (Consumer)((sprite) -> {
            SwingUtilities.invokeLater(() -> {
               BufferedImage scaledSprite = ImageUtil.resizeImage(sprite, 14, 14);
               iconLabel.setIcon(new ImageIcon(scaledSprite));
            });
         }));
      }
   }

   private Border createEntryBorder(int rank) {
      EmptyBorder padding = new EmptyBorder(8, 6, 8, 6);
      Color borderColor;
      switch (rank) {
         case 1:
            borderColor = FIRST_PLACE_BORDER;
            break;
         case 2:
            borderColor = SECOND_PLACE_BORDER;
            break;
         case 3:
            borderColor = THIRD_PLACE_BORDER;
            break;
         default:
            return padding;
      }

      return BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(borderColor, 1), padding);
   }

   private List<String> buildEntryDetailLines(UpdateHiscoreTopListScript.TopListType type, UpdateHiscoreTopListScript.Entry entry) {
      if (type == TopListType.COUNT) {
         return Collections.emptyList();
      } else {
         List<String> detailLines = new ArrayList(3);
         detailLines.add("Lvl " + entry.getLevel());
         if (entry.getPrestige() > 0) {
            detailLines.add("P" + entry.getPrestige());
         }

         detailLines.add("XP " + QuantityFormatter.quantityToStackSize(entry.getValue()));
         return detailLines;
      }
   }

   private String buildEntryValue(UpdateHiscoreTopListScript.TopListType type, UpdateHiscoreTopListScript.Entry entry) {
      if (type == TopListType.COUNT) {
         return QuantityFormatter.quantityToStackSize(entry.getValue());
      } else {
         long displayValue = entry.getPrestigeXp() > 0L ? entry.getPrestigeXp() : entry.getValue();
         return QuantityFormatter.quantityToStackSize(displayValue);
      }
   }

   private String buildEntryValueTooltip(UpdateHiscoreTopListScript.TopListType type, UpdateHiscoreTopListScript.Entry entry) {
      if (type == TopListType.COUNT) {
         return "Count: " + QuantityFormatter.quantityToStackSize(entry.getValue());
      } else {
         return entry.getPrestigeXp() > 0L ? "Total prestiged XP: " + QuantityFormatter.quantityToStackSize(entry.getPrestigeXp()) : "Total XP: " + QuantityFormatter.quantityToStackSize(entry.getValue());
      }
   }

   private int resolveIronmanIconFile(int ironmanMode) {
      switch (ironmanMode) {
         case 1:
            return 15;
         case 2:
            return 17;
         case 3:
            return 16;
         case 4:
            return 41;
         case 5:
            return 42;
         case 6:
            return 30;
         case 100:
            return 43;
         default:
            return -1;
      }
   }

   private String buildIronmanTooltip(int ironmanMode) {
      switch (ironmanMode) {
         case 1:
            return "Ironman: Ironman";
         case 2:
            return "Ironman: Hardcore Ironman";
         case 3:
            return "Ironman: Ultimate Ironman";
         case 4:
            return "Ironman: Group Ironman";
         case 5:
            return "Ironman: Hardcore Group Ironman";
         case 6:
            return "Ironman: Perma-Death Hardcore Ironman";
         case 100:
            return "Ironman: Swapper Group Ironman";
         default:
            return null;
      }
   }

   private int resolveGameModeIconFile(int gameMode) {
      switch (gameMode) {
         case 1:
            return 5;
         case 2:
            return 3;
         case 3:
            return 6;
         case 4:
            return 53;
         case 5:
            return 7;
         case 6:
            return 56;
         case 7:
            return 7;
         case 8:
            return 56;
         case 9:
            return 57;
         default:
            return -1;
      }
   }

   private String buildGameModeTooltip(int gameMode) {
      switch (gameMode) {
         case 1:
            return "Game mode: PvMer";
         case 2:
            return "Game mode: Skiller";
         case 3:
            return "Game mode: Masochist";
         case 4:
            return "Game mode: Slayer Locked";
         case 5:
            return "Game mode: League Mode";
         case 6:
            return "Game mode: League Mode (Randomised)";
         case 7:
            return "Game mode: Pantheon League Mode";
         case 8:
            return "Game mode: Pantheon League Mode (Randomised - Softcore)";
         case 9:
            return "Game mode: Pantheon League Mode (Randomised - Hardcore)";
         default:
            return null;
      }
   }

   private void showPlaceholder(String message) {
      this.entriesPanel.removeAll();
      JLabel placeholder = new JLabel(message);
      placeholder.setFont(FontManager.getRunescapeSmallFont());
      placeholder.setForeground(ColorScheme.LIGHT_GRAY_COLOR);
      placeholder.setAlignmentX(0.0F);
      placeholder.setBorder(new EmptyBorder(8, 4, 8, 4));
      this.entriesPanel.add(placeholder);
      this.entriesPanel.revalidate();
      this.entriesPanel.repaint();
   }

   private void refreshButtonSelectionState() {
      Iterator var1 = this.skillButtons.entrySet().iterator();

      Map.Entry entry;
      boolean selected;
      while(var1.hasNext()) {
         entry = (Map.Entry)var1.next();
         selected = this.currentType == TopListType.SKILL && ((String)TOPLIST_SKILL_IDS.get(entry.getKey())).equals(this.currentTargetId);
         this.applySelectionState((JButton)entry.getValue(), selected);
      }

      var1 = this.countButtons.entrySet().iterator();

      while(var1.hasNext()) {
         entry = (Map.Entry)var1.next();
         selected = this.currentType == TopListType.COUNT && ((String)entry.getKey()).equals(this.currentTargetId);
         this.applySelectionState((JButton)entry.getValue(), selected);
      }

   }

   private void applySelectionState(JButton button, boolean selected) {
      button.setBackground(selected ? BUTTON_SELECTED : BUTTON_UNSELECTED);
      button.setBorder((Border)(selected ? BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(ColorScheme.BRAND_ORANGE), new EmptyBorder(5, 6, 5, 6)) : BorderFactory.createEmptyBorder(6, 7, 6, 7)));
   }

   private String resolveTargetName(UpdateHiscoreTopListScript.TopListType type, String targetId) {
      if (type == TopListType.SKILL) {
         Iterator var5 = TOPLIST_SKILL_IDS.entrySet().iterator();

         Map.Entry entry;
         do {
            if (!var5.hasNext()) {
               return targetId;
            }

            entry = (Map.Entry)var5.next();
         } while(!((String)entry.getValue()).equals(targetId));

         return ((HiscoreSkill)entry.getKey()).getName();
      } else {
         CountId countId = CountId.fromId(targetId);
         return countId == null ? HiscoreLookupPanel.prettifyCountId(targetId) : countId.getDisplayName();
      }
   }

   private void updateTitleIcon(UpdateHiscoreTopListScript.TopListType type, String targetId) {
      int spriteId = this.resolveTargetSpriteId(type, targetId);
      if (spriteId < 0) {
         this.titleLabel.setIcon((Icon)null);
      } else {
         this.spriteManager.getSpriteAsync(spriteId, 0, (Consumer)((sprite) -> {
            SwingUtilities.invokeLater(() -> {
               BufferedImage scaledSprite = ImageUtil.resizeImage(ImageUtil.resizeCanvas(sprite, 25, 25), 18, 18);
               this.titleLabel.setIcon(new ImageIcon(scaledSprite));
            });
         }));
      }
   }

   private int resolveTargetSpriteId(UpdateHiscoreTopListScript.TopListType type, String targetId) {
      if (type == TopListType.SKILL) {
         Iterator var6 = TOPLIST_SKILL_IDS.entrySet().iterator();

         Map.Entry entry;
         do {
            if (!var6.hasNext()) {
               return -1;
            }

            entry = (Map.Entry)var6.next();
         } while(!((String)entry.getValue()).equals(targetId));

         HiscoreSkill skill = (HiscoreSkill)entry.getKey();
         return skill == HiscoreSkill.OVERALL ? 222 : skill.getSpriteId();
      } else {
         Integer spriteId = (Integer)this.countGraphicMapping.get(targetId);
         return spriteId == null ? -1 : spriteId;
      }
   }

   private void loadSprite(JButton button, int spriteId) {
      if (spriteId >= 0) {
         this.spriteManager.getSpriteAsync(spriteId, 0, (Consumer)((sprite) -> {
            SwingUtilities.invokeLater(() -> {
               BufferedImage scaledSprite = ImageUtil.resizeImage(ImageUtil.resizeCanvas(sprite, 25, 25), 18, 18);
               button.setIcon(new ImageIcon(scaledSprite));
            });
         }));
      }
   }

   private static JButton createActionButton(String text) {
      JButton button = new JButton(text);
      button.setFont(FontManager.getRunescapeSmallFont());
      button.setBackground(ColorScheme.DARKER_GRAY_COLOR);
      button.setForeground(Color.WHITE);
      button.setFocusPainted(false);
      button.setMargin(new Insets(4, 8, 4, 8));
      return button;
   }

   private JButton createPaginationButton(String text, int targetPage, boolean enabled, boolean selected) {
      JButton button = new JButton(text);
      button.setFont(FontManager.getRunescapeSmallFont());
      button.setForeground(Color.WHITE);
      button.setBackground(selected ? BUTTON_SELECTED : ColorScheme.DARKER_GRAY_COLOR);
      button.setOpaque(true);
      button.setFocusPainted(false);
      button.setMargin(new Insets(3, 6, 3, 6));
      button.setEnabled(enabled);
      button.setBorder((Border)(selected ? BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(ColorScheme.BRAND_ORANGE), new EmptyBorder(2, 5, 2, 5)) : BorderFactory.createEmptyBorder(3, 6, 3, 6)));
      if (enabled) {
         button.addActionListener((e) -> {
            this.requestPage(targetPage);
         });
      }

      return button;
   }

   private static JButton createSelectionButton(String text) {
      JButton button = new JButton(text);
      button.setHorizontalAlignment(2);
      button.setFont(FontManager.getRunescapeSmallFont().deriveFont(0));
      button.setForeground(Color.WHITE);
      button.setBackground(BUTTON_UNSELECTED);
      button.setOpaque(true);
      button.setFocusPainted(false);
      button.setBorder(BorderFactory.createEmptyBorder(6, 7, 6, 7));
      button.setIconTextGap(6);
      return button;
   }

   static {
      BUTTON_SELECTED = ColorScheme.DARK_GRAY_HOVER_COLOR;
      BUTTON_UNSELECTED = ColorScheme.DARKER_GRAY_COLOR;
      FIRST_PLACE_BORDER = new Color(13214247);
      SECOND_PLACE_BORDER = new Color(10989495);
      THIRD_PLACE_BORDER = new Color(11104578);
      ANY_IRONMAN_FILTER = new FilterOption("Any Iron", -1, -1, "Any ironman mode");
      NO_IRONMAN_FILTER = new FilterOption("No Iron", 0, -1, "No ironman mode");
      IRONMAN_FILTERS = ImmutableList.of(ANY_IRONMAN_FILTER, NO_IRONMAN_FILTER, new FilterOption("Ironman", 1, 15, "Ironman"), new FilterOption("HCIM", 2, 17, "Hardcore Ironman"), new FilterOption("UIM", 3, 16, "Ultimate Ironman"), new FilterOption("GIM", 4, 41, "Group Ironman"), new FilterOption("HCGIM", 5, 42, "Hardcore Group Ironman"), new FilterOption("PDHCIM", 6, 30, "Perma-Death Hardcore Ironman"), new FilterOption("Swapper GIM", 100, 43, "Swapper Group Ironman"));
      ANY_GAME_MODE_FILTER = new FilterOption("Any Mode", -1, -1, "Any game mode");
      NO_GAME_MODE_FILTER = new FilterOption("No Mode", 0, -1, "No game mode");
      GAME_MODE_FILTERS = ImmutableList.of(ANY_GAME_MODE_FILTER, NO_GAME_MODE_FILTER, new FilterOption("PvMer", 1, 5, "PvMer"), new FilterOption("Skiller", 2, 3, "Skiller"), new FilterOption("Masochist", 3, 6, "Masochist"), new FilterOption("Slayer", 4, 53, "Slayer Locked"), new FilterOption("Pantheon", 7, 7, "Pantheon League Mode"), new FilterOption("Pantheon RNG SC", 8, 56, "Pantheon League Mode (Randomised - Softcore)"), new FilterOption("Pantheon RNG HC", 9, 57, "Pantheon League Mode (Randomised - Hardcore)"));
      TOPLIST_SKILLS = ImmutableList.of(HiscoreSkill.OVERALL, HiscoreSkill.ATTACK, HiscoreSkill.HITPOINTS, HiscoreSkill.MINING, HiscoreSkill.STRENGTH, HiscoreSkill.AGILITY, HiscoreSkill.SMITHING, HiscoreSkill.DEFENCE, HiscoreSkill.HERBLORE, HiscoreSkill.FISHING, HiscoreSkill.RANGED, HiscoreSkill.THIEVING, new HiscoreSkill[]{HiscoreSkill.COOKING, HiscoreSkill.PRAYER, HiscoreSkill.CRAFTING, HiscoreSkill.FIREMAKING, HiscoreSkill.MAGIC, HiscoreSkill.FLETCHING, HiscoreSkill.WOODCUTTING, HiscoreSkill.RUNECRAFT, HiscoreSkill.SLAYER, HiscoreSkill.FARMING, HiscoreSkill.CONSTRUCTION, HiscoreSkill.HUNTER});
      TOPLIST_SKILL_IDS = ImmutableMap.builder().put(HiscoreSkill.OVERALL, "total").put(HiscoreSkill.ATTACK, "attack").put(HiscoreSkill.DEFENCE, "defence").put(HiscoreSkill.STRENGTH, "strength").put(HiscoreSkill.HITPOINTS, "hitpoints").put(HiscoreSkill.RANGED, "ranged").put(HiscoreSkill.PRAYER, "prayer").put(HiscoreSkill.MAGIC, "magic").put(HiscoreSkill.COOKING, "cooking").put(HiscoreSkill.WOODCUTTING, "woodcutting").put(HiscoreSkill.FLETCHING, "fletching").put(HiscoreSkill.FISHING, "fishing").put(HiscoreSkill.FIREMAKING, "firemaking").put(HiscoreSkill.CRAFTING, "crafting").put(HiscoreSkill.SMITHING, "smithing").put(HiscoreSkill.MINING, "mining").put(HiscoreSkill.HERBLORE, "herblore").put(HiscoreSkill.AGILITY, "agility").put(HiscoreSkill.THIEVING, "thieving").put(HiscoreSkill.SLAYER, "slayer").put(HiscoreSkill.FARMING, "farming").put(HiscoreSkill.RUNECRAFT, "runecrafting").put(HiscoreSkill.HUNTER, "hunter").put(HiscoreSkill.CONSTRUCTION, "construction").build();
   }

   private static final class FilterOption {
      private final String label;
      private final int value;
      private final int iconFile;
      private final String tooltip;

      private FilterOption(String label, int value, int iconFile, String tooltip) {
         this.label = label;
         this.value = value;
         this.iconFile = iconFile;
         this.tooltip = tooltip;
      }

      private String getLabel() {
         return this.label;
      }

      private int getValue() {
         return this.value;
      }

      private int getIconFile() {
         return this.iconFile;
      }

      private String getTooltip() {
         return this.tooltip;
      }
   }

   private static final class ViewportWidthPanel extends JPanel implements Scrollable {
      public Dimension getPreferredScrollableViewportSize() {
         return this.getPreferredSize();
      }

      public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {
         return 16;
      }

      public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) {
         return orientation == 1 ? visibleRect.height : visibleRect.width;
      }

      public boolean getScrollableTracksViewportWidth() {
         return true;
      }

      public boolean getScrollableTracksViewportHeight() {
         return false;
      }
   }

   @FunctionalInterface
   interface TopListRequestHandler {
      int request(UpdateHiscoreTopListScript.TopListType var1, String var2, int var3, int var4, int var5, int var6);
   }
}
