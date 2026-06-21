package net.runelite.client.ui;

import java.util.HashMap;
import java.util.Map;
import javax.swing.JFormattedTextField;

public final class UnitFormatterFactory extends JFormattedTextField.AbstractFormatterFactory {
   private final JFormattedTextField.AbstractFormatterFactory delegateFactory;
   private final String units;
   private final Map<JFormattedTextField, JFormattedTextField.AbstractFormatter> formatters = new HashMap(1);

   public JFormattedTextField.AbstractFormatter getFormatter(JFormattedTextField tf) {
      return (JFormattedTextField.AbstractFormatter)this.formatters.computeIfAbsent(tf, (key) -> {
         return new UnitFormatter(this.delegateFactory.getFormatter(key), this.units);
      });
   }

   public UnitFormatterFactory(JFormattedTextField.AbstractFormatterFactory delegateFactory, String units) {
      this.delegateFactory = delegateFactory;
      this.units = units;
   }
}
