package net.runelite.client.plugins.grandexchange;

import java.awt.BorderLayout;
import javax.inject.Inject;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.PluginPanel;
import net.runelite.client.ui.components.materialtabs.MaterialTab;
import net.runelite.client.ui.components.materialtabs.MaterialTabGroup;

class GrandExchangePanel extends PluginPanel {
   private final JPanel display = new JPanel();
   private final MaterialTabGroup tabGroup;
   private final MaterialTab historyTab;
   private final MaterialTab itemTab;
   private final GrandExchangeOffersPanel offersPanel;
   private final GrandExchangeHistoryPanel historyPanel;
   private final GrandExchangeMarketItemPanel itemPanel;

   @Inject
   private GrandExchangePanel(GrandExchangeOffersPanel offersPanel, GrandExchangeHistoryPanel historyPanel, GrandExchangeMarketItemPanel itemPanel) {
      super(false);
      this.tabGroup = new MaterialTabGroup(this.display);
      this.offersPanel = offersPanel;
      this.historyPanel = historyPanel;
      this.itemPanel = itemPanel;
      this.setLayout(new BorderLayout());
      this.setBackground(ColorScheme.DARK_GRAY_COLOR);
      MaterialTab offersTab = new MaterialTab("Offers", this.tabGroup, offersPanel);
      this.historyTab = new MaterialTab("History", this.tabGroup, historyPanel);
      this.itemTab = new MaterialTab("Item", this.tabGroup, itemPanel);
      this.historyTab.setOnSelectEvent(() -> {
         historyPanel.open();
         return true;
      });
      this.itemTab.setOnSelectEvent(() -> {
         itemPanel.open();
         return true;
      });
      this.tabGroup.setBorder(new EmptyBorder(5, 0, 0, 0));
      this.tabGroup.addTab(offersTab);
      this.tabGroup.addTab(this.historyTab);
      this.tabGroup.addTab(this.itemTab);
      this.tabGroup.select(offersTab);
      this.add(this.tabGroup, "North");
      this.add(this.display, "Center");
   }

   void showHistory() {
      if (!this.historyPanel.isShowing()) {
         this.tabGroup.select(this.historyTab);
         this.revalidate();
      }
   }

   void showItem() {
      if (!this.itemPanel.isShowing()) {
         this.tabGroup.select(this.itemTab);
         this.revalidate();
      }
   }

   public GrandExchangeOffersPanel getOffersPanel() {
      return this.offersPanel;
   }

   public GrandExchangeHistoryPanel getHistoryPanel() {
      return this.historyPanel;
   }

   public GrandExchangeMarketItemPanel getItemPanel() {
      return this.itemPanel;
   }
}
