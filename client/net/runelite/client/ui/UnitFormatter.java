package net.runelite.client.ui;

import java.text.ParseException;
import javax.swing.JFormattedTextField;

final class UnitFormatter extends JFormattedTextField.AbstractFormatter {
   private final JFormattedTextField.AbstractFormatter delegate;
   private final String units;

   public Object stringToValue(String text) throws ParseException {
      String trimmedText;
      if (text.endsWith(this.units)) {
         trimmedText = text.substring(0, text.length() - this.units.length());
      } else {
         trimmedText = text;
      }

      return this.delegate.stringToValue(trimmedText);
   }

   public String valueToString(Object value) {
      String var10000 = String.valueOf(value);
      return var10000 + this.units;
   }

   public UnitFormatter(JFormattedTextField.AbstractFormatter delegate, String units) {
      this.delegate = delegate;
      this.units = units;
   }
}
