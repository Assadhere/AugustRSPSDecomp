package net.runelite.client.plugins.config;

import java.awt.Component;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;
import javax.swing.JSeparator;

final class TruncatedListCellRenderer extends DefaultListCellRenderer {
   private final int maxLength;

   private String truncateValue(String value) {
      if (value.length() > this.maxLength) {
         String var10000 = value.substring(0, this.maxLength);
         return var10000 + "…";
      } else {
         return value;
      }
   }

   public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
      if (value.equals("---")) {
         return new JSeparator(0);
      } else {
         String text = this.truncateValue(value.toString());
         return super.getListCellRendererComponent(list, text, index, isSelected, cellHasFocus);
      }
   }

   public TruncatedListCellRenderer(int maxLength) {
      this.maxLength = maxLength;
   }
}
