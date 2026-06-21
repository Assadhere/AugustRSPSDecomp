package net.runelite.client.ui;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Insets;
import java.util.function.Function;

public class DynamicGridLayout extends GridLayout {
   public DynamicGridLayout() {
      this(1, 0, 0, 0);
   }

   public DynamicGridLayout(int rows, int cols) {
      this(rows, cols, 0, 0);
   }

   public DynamicGridLayout(int rows, int cols, int hgap, int vgap) {
      super(rows, cols, hgap, vgap);
   }

   public Dimension preferredLayoutSize(Container parent) {
      synchronized(parent.getTreeLock()) {
         return this.calculateSize(parent, Component::getPreferredSize);
      }
   }

   public Dimension minimumLayoutSize(Container parent) {
      synchronized(parent.getTreeLock()) {
         return this.calculateSize(parent, Component::getMinimumSize);
      }
   }

   public void layoutContainer(Container parent) {
      synchronized(parent.getTreeLock()) {
         Insets insets = parent.getInsets();
         int ncomponents = parent.getComponentCount();
         int nrows = this.getRows();
         int ncols = this.getColumns();
         if (ncomponents != 0) {
            if (nrows > 0) {
               ncols = (ncomponents + nrows - 1) / nrows;
            } else {
               nrows = (ncomponents + ncols - 1) / ncols;
            }

            int hgap = this.getHgap();
            int vgap = this.getVgap();
            Dimension pd = this.preferredLayoutSize(parent);
            Insets parentInsets = parent.getInsets();
            int wborder = parentInsets.left + parentInsets.right;
            int hborder = parentInsets.top + parentInsets.bottom;
            double sw = (1.0 * (double)parent.getWidth() - (double)wborder) / (double)(pd.width - wborder);
            double sh = (1.0 * (double)parent.getHeight() - (double)hborder) / (double)(pd.height - hborder);
            int[] w = new int[ncols];
            int[] h = new int[nrows];

            int c;
            int x;
            int r;
            for(c = 0; c < ncomponents; ++c) {
               x = c / ncols;
               r = c % ncols;
               Component comp = parent.getComponent(c);
               Dimension d = comp.getPreferredSize();
               d.width = (int)(sw * (double)d.width);
               d.height = (int)(sh * (double)d.height);
               if (w[r] < d.width) {
                  w[r] = d.width;
               }

               if (h[x] < d.height) {
                  h[x] = d.height;
               }
            }

            c = 0;

            for(x = insets.left; c < ncols; ++c) {
               r = 0;

               for(int y = insets.top; r < nrows; ++r) {
                  int i = r * ncols + c;
                  if (i < ncomponents) {
                     parent.getComponent(i).setBounds(x, y, w[c], h[r]);
                  }

                  y += h[r] + vgap;
               }

               x += w[c] + hgap;
            }

         }
      }
   }

   private Dimension calculateSize(Container parent, Function<Component, Dimension> sizer) {
      int ncomponents = parent.getComponentCount();
      int nrows = this.getRows();
      int ncols = this.getColumns();
      if (nrows > 0) {
         ncols = (ncomponents + nrows - 1) / nrows;
      } else {
         nrows = (ncomponents + ncols - 1) / ncols;
      }

      int[] w = new int[ncols];
      int[] h = new int[nrows];

      int nw;
      int nh;
      int i;
      for(nw = 0; nw < ncomponents; ++nw) {
         nh = nw / ncols;
         i = nw % ncols;
         Component comp = parent.getComponent(nw);
         Dimension d = (Dimension)sizer.apply(comp);
         if (w[i] < d.width) {
            w[i] = d.width;
         }

         if (h[nh] < d.height) {
            h[nh] = d.height;
         }
      }

      nw = 0;

      for(nh = 0; nh < ncols; ++nh) {
         nw += w[nh];
      }

      nh = 0;

      for(i = 0; i < nrows; ++i) {
         nh += h[i];
      }

      Insets insets = parent.getInsets();
      return new Dimension(insets.left + insets.right + nw + (ncols - 1) * this.getHgap(), insets.top + insets.bottom + nh + (nrows - 1) * this.getVgap());
   }
}
