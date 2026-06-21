package net.runelite.client.plugins.hiscore;

import custom.UpdateHiscoreScript;
import custom.UpdateHiscoreTopListScript;
import java.awt.BorderLayout;
import java.util.Map;
import java.util.function.Consumer;
import javax.inject.Inject;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.PluginPanel;
import net.runelite.client.ui.components.materialtabs.MaterialTab;
import net.runelite.client.ui.components.materialtabs.MaterialTabGroup;

class HiscorePanel extends PluginPanel {
   private final JPanel display = new JPanel();
   private final MaterialTabGroup tabGroup;
   private final MaterialTab lookupTab;
   private final MaterialTab topListTab;
   private final HiscoreLookupPanel lookupPanel;
   private final HiscoreTopListPanel topListPanel;

   @Inject
   HiscorePanel(HiscoreLookupPanel lookupPanel, HiscoreTopListPanel topListPanel) {
      super(false);
      this.tabGroup = new MaterialTabGroup(this.display);
      this.lookupPanel = lookupPanel;
      this.topListPanel = topListPanel;
      this.setLayout(new BorderLayout());
      this.setBackground(ColorScheme.DARK_GRAY_COLOR);
      JScrollPane lookupScrollPane = new JScrollPane(lookupPanel);
      lookupScrollPane.setHorizontalScrollBarPolicy(31);
      lookupScrollPane.setBorder((Border)null);
      lookupScrollPane.getViewport().setBackground(ColorScheme.DARK_GRAY_COLOR);
      this.lookupTab = new MaterialTab("Lookup", this.tabGroup, lookupScrollPane);
      this.topListTab = new MaterialTab("Top List", this.tabGroup, topListPanel);
      this.tabGroup.setBorder(new EmptyBorder(5, 0, 0, 0));
      this.tabGroup.addTab(this.lookupTab);
      this.tabGroup.addTab(this.topListTab);
      this.tabGroup.select(this.lookupTab);
      lookupPanel.setTopListSelectionHandler((type, targetId) -> {
         this.tabGroup.select(this.topListTab);
         topListPanel.showTarget(type, targetId);
      });
      this.add(this.tabGroup, "North");
      this.add(this.display, "Center");
   }

   void setSearchHandler(Consumer<String> searchHandler) {
      this.lookupPanel.setSearchHandler(searchHandler);
   }

   void setTopListHandler(HiscoreTopListPanel.TopListRequestHandler requestHandler) {
      this.topListPanel.setRequestHandler(requestHandler);
   }

   void setCountGraphicMapping(Map<String, Integer> mapping) {
      this.lookupPanel.setCountGraphicMapping(mapping);
      this.topListPanel.setCountGraphicMapping(mapping);
   }

   void shutdown() {
      this.lookupPanel.shutdown();
      this.topListPanel.shutdown();
   }

   public void displayHiscoreData(UpdateHiscoreScript packet) {
      this.tabGroup.select(this.lookupTab);
      this.lookupPanel.displayHiscoreData(packet);
   }

   public void displayTopListData(UpdateHiscoreTopListScript packet) {
      this.tabGroup.select(this.topListTab);
      this.topListPanel.displayTopListData(packet);
   }
}
