package net.runelite.client.ui.components.materialtabs;

import com.google.common.base.Strings;
import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.function.BooleanSupplier;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import net.runelite.client.ui.ColorScheme;

public class MaterialTab extends JLabel {
   private static final Border SELECTED_BORDER;
   private static final Border UNSELECTED_BORDER;
   private final JComponent content;
   private BooleanSupplier onSelectEvent;
   private boolean selected;

   public MaterialTab(String string, final MaterialTabGroup group, JComponent content) {
      super(string);
      this.content = content;
      if (this.selected) {
         this.select();
      } else {
         this.unselect();
      }

      this.addMouseListener(new MouseAdapter() {
         public void mousePressed(MouseEvent mouseEvent) {
            group.select(MaterialTab.this);
         }
      });
      if (!Strings.isNullOrEmpty(string)) {
         this.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
               MaterialTab tab = (MaterialTab)e.getSource();
               tab.setForeground(Color.WHITE);
            }

            public void mouseExited(MouseEvent e) {
               MaterialTab tab = (MaterialTab)e.getSource();
               if (!tab.isSelected()) {
                  tab.setForeground(Color.GRAY);
               }

            }
         });
      }

   }

   public MaterialTab(ImageIcon icon, MaterialTabGroup group, JComponent content) {
      this("", group, content);
      this.setIcon(icon);
      this.setOpaque(true);
      this.setVerticalAlignment(0);
      this.setHorizontalAlignment(0);
      this.setBackground(ColorScheme.DARKER_GRAY_COLOR);
      this.addMouseListener(new MouseAdapter() {
         public void mouseEntered(MouseEvent e) {
            MaterialTab tab = (MaterialTab)e.getSource();
            tab.setBackground(ColorScheme.DARKER_GRAY_HOVER_COLOR);
         }

         public void mouseExited(MouseEvent e) {
            MaterialTab tab = (MaterialTab)e.getSource();
            tab.setBackground(ColorScheme.DARKER_GRAY_COLOR);
         }
      });
   }

   public boolean select() {
      if (this.onSelectEvent != null && !this.onSelectEvent.getAsBoolean()) {
         return false;
      } else {
         this.setBorder(SELECTED_BORDER);
         this.setForeground(Color.WHITE);
         return this.selected = true;
      }
   }

   public void unselect() {
      this.setBorder(UNSELECTED_BORDER);
      this.setForeground(Color.GRAY);
      this.selected = false;
   }

   public JComponent getContent() {
      return this.content;
   }

   public void setOnSelectEvent(BooleanSupplier onSelectEvent) {
      this.onSelectEvent = onSelectEvent;
   }

   public boolean isSelected() {
      return this.selected;
   }

   static {
      SELECTED_BORDER = new CompoundBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, ColorScheme.BRAND_ORANGE), BorderFactory.createEmptyBorder(5, 10, 4, 10));
      UNSELECTED_BORDER = BorderFactory.createEmptyBorder(5, 10, 5, 10);
   }
}
