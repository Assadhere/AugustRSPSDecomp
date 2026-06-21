package net.runelite.client.plugins.config;

import java.awt.Dimension;
import javax.swing.JPanel;

class FixedWidthPanel extends JPanel {
   public Dimension getPreferredSize() {
      return new Dimension(225, super.getPreferredSize().height);
   }
}
