package net.runelite.client.plugins.timetracking;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import javax.annotation.Nullable;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.game.ItemManager;
import net.runelite.client.plugins.timetracking.clocks.ClockManager;
import net.runelite.client.plugins.timetracking.farming.FarmingContractManager;
import net.runelite.client.plugins.timetracking.farming.FarmingNextTickPanel;
import net.runelite.client.plugins.timetracking.farming.FarmingTracker;
import net.runelite.client.plugins.timetracking.hunter.BirdHouseTracker;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.PluginPanel;
import net.runelite.client.ui.components.materialtabs.MaterialTab;
import net.runelite.client.ui.components.materialtabs.MaterialTabGroup;
import net.runelite.client.util.AsyncBufferedImage;

class TimeTrackingPanel extends PluginPanel {
   private final ItemManager itemManager;
   private final TimeTrackingConfig config;
   private final JPanel display = new JPanel();
   private final Map<Tab, MaterialTab> uiTabs = new HashMap();
   private final MaterialTabGroup tabGroup;
   private boolean active;
   @Nullable
   private TabContentPanel activeTabPanel;

   @Inject
   TimeTrackingPanel(ItemManager itemManager, TimeTrackingConfig config, FarmingTracker farmingTracker, BirdHouseTracker birdHouseTracker, ClockManager clockManager, FarmingContractManager farmingContractManager, ConfigManager configManager, @Named("developerMode") boolean developerMode) {
      super(false);
      this.tabGroup = new MaterialTabGroup(this.display);
      this.activeTabPanel = null;
      this.itemManager = itemManager;
      this.config = config;
      this.setLayout(new BorderLayout());
      this.setBackground(ColorScheme.DARK_GRAY_COLOR);
      this.display.setBorder(new EmptyBorder(10, 10, 8, 10));
      this.tabGroup.setLayout(new GridLayout(0, 6, 7, 7));
      this.tabGroup.setBorder(new EmptyBorder(10, 10, 0, 10));
      this.add(this.tabGroup, "North");
      this.add(this.display, "Center");
      this.addTab(Tab.OVERVIEW, new OverviewTabPanel(itemManager, config, this, farmingTracker, birdHouseTracker, clockManager, farmingContractManager));
      this.addTab(Tab.CLOCK, clockManager.getClockTabPanel());
      this.addTab(Tab.BIRD_HOUSE, birdHouseTracker.createBirdHouseTabPanel());
      Tab[] var9 = Tab.FARMING_TABS;
      int var10 = var9.length;

      for(int var11 = 0; var11 < var10; ++var11) {
         Tab tab = var9[var11];
         this.addTab(tab, farmingTracker.createTabPanel(tab, farmingContractManager));
      }

      if (developerMode) {
         this.addTab(Tab.TIME_OFFSET, new FarmingNextTickPanel(farmingTracker, config, configManager));
      }

   }

   private void addTab(Tab tab, TabContentPanel tabContentPanel) {
      JPanel wrapped = new JPanel(new BorderLayout());
      wrapped.add(tabContentPanel, "North");
      wrapped.setBackground(ColorScheme.DARK_GRAY_COLOR);
      JScrollPane scroller = new JScrollPane(wrapped);
      scroller.setHorizontalScrollBarPolicy(31);
      scroller.getVerticalScrollBar().setPreferredSize(new Dimension(16, 0));
      scroller.getVerticalScrollBar().setBorder(new EmptyBorder(0, 9, 0, 0));
      scroller.setBackground(ColorScheme.DARK_GRAY_COLOR);
      MaterialTab materialTab = new MaterialTab(new ImageIcon(), this.tabGroup, scroller);
      materialTab.setPreferredSize(new Dimension(30, 27));
      materialTab.setName(tab.getName());
      materialTab.setToolTipText(tab.getName());
      AsyncBufferedImage icon = this.itemManager.getImage(tab.getItemID());
      icon.onLoaded(() -> {
         BufferedImage subIcon = icon.getSubimage(0, 0, 32, 32);
         materialTab.setIcon(new ImageIcon(subIcon.getScaledInstance(24, 24, 4)));
      });
      materialTab.setOnSelectEvent(() -> {
         this.config.setActiveTab(tab);
         this.activeTabPanel = tabContentPanel;
         tabContentPanel.update();
         return true;
      });
      this.uiTabs.put(tab, materialTab);
      this.tabGroup.addTab(materialTab);
      if (this.config.activeTab() == tab) {
         this.tabGroup.select(materialTab);
      }

   }

   void switchTab(Tab tab) {
      this.tabGroup.select((MaterialTab)this.uiTabs.get(tab));
   }

   int getUpdateInterval() {
      return this.activeTabPanel == null ? Integer.MAX_VALUE : this.activeTabPanel.getUpdateInterval();
   }

   void update() {
      if (this.active && this.activeTabPanel != null) {
         TabContentPanel var10000 = this.activeTabPanel;
         Objects.requireNonNull(var10000);
         SwingUtilities.invokeLater(var10000::update);
      }
   }

   public void onActivate() {
      this.active = true;
      this.update();
   }

   public void onDeactivate() {
      this.active = false;
   }
}
