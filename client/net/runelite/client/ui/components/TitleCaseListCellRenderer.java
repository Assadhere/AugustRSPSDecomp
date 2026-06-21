package net.runelite.client.ui.components;

import java.awt.Component;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;
import net.runelite.client.util.Text;

public final class TitleCaseListCellRenderer extends DefaultListCellRenderer {
   public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
      String text;
      if (value instanceof Enum) {
         text = Text.titleCase((Enum)value);
      } else {
         text = value.toString();
      }

      return super.getListCellRendererComponent(list, text, index, isSelected, cellHasFocus);
   }
}
