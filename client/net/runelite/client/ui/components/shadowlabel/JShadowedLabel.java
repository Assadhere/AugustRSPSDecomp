package net.runelite.client.ui.components.shadowlabel;

import java.awt.Color;
import java.awt.Point;
import javax.swing.JLabel;

public class JShadowedLabel extends JLabel {
   private Color shadow;
   private Point shadowSize;

   public JShadowedLabel() {
      this.shadow = Color.BLACK;
      this.shadowSize = new Point(1, 1);
      this.setUI(new JShadowedLabelUI());
   }

   public JShadowedLabel(String str) {
      super(str);
      this.shadow = Color.BLACK;
      this.shadowSize = new Point(1, 1);
      this.setUI(new JShadowedLabelUI());
   }

   public void setShadow(Color shadow) {
      this.shadow = shadow;
      this.repaint();
   }

   public void setShadowSize(Point newSize) {
      this.shadowSize = newSize;
      this.revalidate();
      this.repaint();
   }

   public Color getShadow() {
      return this.shadow;
   }

   public Point getShadowSize() {
      return this.shadowSize;
   }
}
