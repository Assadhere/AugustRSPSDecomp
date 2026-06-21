package net.runelite.client.ui.components.materialtabs;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.swing.JPanel;

public class MaterialTabGroup extends JPanel {
   private final JPanel display;
   private final List<MaterialTab> tabs;

   public MaterialTabGroup(JPanel display) {
      this.tabs = new ArrayList();
      this.display = display;
      if (display != null) {
         this.display.setLayout(new BorderLayout());
      }

      this.setLayout(new FlowLayout(1, 8, 0));
      this.setOpaque(false);
   }

   public MaterialTabGroup() {
      this((JPanel)null);
   }

   public MaterialTab getTab(int index) {
      return this.tabs != null && !this.tabs.isEmpty() ? (MaterialTab)this.tabs.get(index) : null;
   }

   public void addTab(MaterialTab tab) {
      this.tabs.add(tab);
      this.add(tab, "North");
   }

   public boolean select(MaterialTab selectedTab) {
      if (this.tabs.contains(selectedTab) && !selectedTab.isSelected()) {
         if (!selectedTab.select()) {
            return false;
         } else {
            if (this.display != null) {
               this.display.removeAll();
               this.display.add(selectedTab.getContent());
               this.display.revalidate();
               this.display.repaint();
            }

            Iterator var2 = this.tabs.iterator();

            while(var2.hasNext()) {
               MaterialTab tab = (MaterialTab)var2.next();
               if (!tab.equals(selectedTab)) {
                  tab.unselect();
               }
            }

            return true;
         }
      } else {
         return false;
      }
   }
}
