package net.runelite.client.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.util.TreeMap;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import net.runelite.client.util.ImageUtil;
import net.runelite.client.util.SwingUtil;

class ClientToolbarPanel extends JPanel {
   private final TreeMap<NavigationButton, Component> entries;

   ClientToolbarPanel(boolean isInSidebar) {
      this.entries = new TreeMap(NavigationButton.COMPARATOR);
      this.setBackground(ColorScheme.DARKER_GRAY_COLOR);
      if (isInSidebar) {
         this.setLayout(new DynamicGridLayout(0, 1, 0, 4));
      } else {
         this.setLayout(new DynamicGridLayout(1, 0, 4, 0));
      }

      this.revalidate();
   }

   JButton add(NavigationButton nb, boolean resize) {
      Icon icon = new ImageIcon(resize ? ImageUtil.resizeImage(nb.getIcon(), 16, 16) : nb.getIcon());
      JButton jb = new JButton(icon);
      SwingUtil.removeButtonDecorations(jb);
      jb.setToolTipText(nb.getTooltip());
      jb.setFocusable(false);
      jb.setPreferredSize(new Dimension(23, 23));
      jb.setAlignmentX(0.5F);
      jb.setAlignmentY(0.5F);
      jb.addActionListener((l) -> {
         if (nb.getOnClick() != null) {
            nb.getOnClick().run();
         }

      });
      if (nb.getPopup() != null) {
         JPopupMenu menu = new JPopupMenu();
         nb.getPopup().forEach((name, cb) -> {
            JMenuItem menuItem = new JMenuItem(name);
            menuItem.addActionListener((e) -> {
               cb.run();
            });
            menu.add(menuItem);
         });
         jb.setComponentPopupMenu(menu);
      }

      if (this.entries.putIfAbsent(nb, jb) != null) {
         return null;
      } else {
         this.add(jb, this.entries.headMap(nb).size());
         this.revalidate();
         this.revalidateMaxSize();
         return jb;
      }
   }

   void remove(NavigationButton btn) {
      Component c = (Component)this.entries.remove(btn);
      if (c != null) {
         this.remove(c);
         this.revalidate();
         this.revalidateMaxSize();
      }

   }

   private void revalidateMaxSize() {
      this.setMaximumSize(this.getPreferredSize());
   }

   public JPanel createSidebarPanel() {
      JPanel wrap = new JPanel();
      wrap.setBackground(ColorScheme.DARKER_GRAY_COLOR);
      wrap.setLayout(new BorderLayout());
      wrap.add(this, "South");
      return wrap;
   }
}
