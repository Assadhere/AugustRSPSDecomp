package net.runelite.client.plugins.config;

import com.google.common.primitives.Ints;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.EmptyBorder;
import net.runelite.client.config.ConfigDescriptor;
import net.runelite.client.config.ConfigItemDescriptor;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.config.FontType;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.DynamicGridLayout;
import net.runelite.client.ui.FontManager;
import net.runelite.client.ui.PluginPanel;
import net.runelite.client.ui.UnitFormatterFactory;
import net.runelite.client.util.SwingUtil;

class FontPanel extends PluginPanel {
   private final ConfigManager configManager;
   private final JLabel title;
   private final FixedWidthPanel mainPanel;
   private ConfigDescriptor configDescriptor;
   private ConfigItemDescriptor configItemDescriptor;

   @Inject
   private FontPanel(ConfigManager configManager, PluginListPanel pluginList) {
      super(false);
      this.configManager = configManager;
      this.setLayout(new BorderLayout());
      this.setBackground(ColorScheme.DARK_GRAY_COLOR);
      JPanel topPanel = new JPanel();
      topPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
      topPanel.setLayout(new BorderLayout(0, 6));
      this.add(topPanel, "North");
      this.mainPanel = new FixedWidthPanel();
      this.mainPanel.setBorder(new EmptyBorder(8, 10, 10, 10));
      this.mainPanel.setLayout(new DynamicGridLayout(0, 1, 0, 5));
      this.mainPanel.setAlignmentX(0.0F);
      JPanel contentsPanel = new FixedWidthPanel();
      ((JPanel)contentsPanel).setLayout(new BorderLayout());
      ((JPanel)contentsPanel).add(this.mainPanel, "North");
      JScrollPane scrollPane = new JScrollPane(contentsPanel);
      scrollPane.setHorizontalScrollBarPolicy(31);
      this.add(scrollPane, "Center");
      JButton topPanelBackButton = new JButton(ConfigPanel.BACK_ICON);
      SwingUtil.removeButtonDecorations(topPanelBackButton);
      topPanelBackButton.setPreferredSize(new Dimension(22, 0));
      topPanelBackButton.setBorder(new EmptyBorder(0, 0, 0, 5));
      topPanelBackButton.addActionListener((e) -> {
         pluginList.getMuxer().popState();
      });
      topPanelBackButton.setToolTipText("Back");
      topPanel.add(topPanelBackButton, "West");
      this.title = new JLabel();
      this.title.setForeground(Color.WHITE);
      topPanel.add(this.title, "Center");
   }

   private void item(String name, String description, Component component) {
      JPanel item = new JPanel();
      item.setLayout(new BorderLayout());
      item.setMinimumSize(new Dimension(225, 0));
      JLabel configEntryName = new JLabel(name);
      configEntryName.setForeground(Color.WHITE);
      if (!"".equals(description)) {
         configEntryName.setToolTipText("<html>" + name + ":<br>" + description + "</html>");
      }

      item.add(configEntryName, "Center");
      item.add(component, "East");
      this.mainPanel.add(item);
   }

   private JCheckBox checkbox(boolean selected) {
      JCheckBox checkbox = new JCheckBox();
      checkbox.setSelected(selected);
      return checkbox;
   }

   private <T> JComboBox<T> combobox(T[] options, T value) {
      JComboBox<T> box = new JComboBox(options);
      box.setRenderer(new TruncatedListCellRenderer(23));
      box.setPreferredSize(new Dimension(box.getPreferredSize().width, 22));
      box.setSelectedItem(value);
      box.setToolTipText(value.toString());
      box.addItemListener((e) -> {
         if (e.getStateChange() == 1) {
            box.setToolTipText(box.getSelectedItem().toString());
         }

      });
      return box;
   }

   private JSpinner createIntSpinner(int min, int max, int value, String unit) {
      value = Ints.constrainToRange(value, min, max);
      SpinnerModel model = new SpinnerNumberModel(value, min, max, 1);
      JSpinner spinner = new JSpinner(model);
      Component editor = spinner.getEditor();
      JFormattedTextField spinnerTextField = ((JSpinner.DefaultEditor)editor).getTextField();
      spinnerTextField.setColumns(6);
      spinnerTextField.setFormatterFactory(new UnitFormatterFactory(spinnerTextField.getFormatterFactory(), unit));
      return spinner;
   }

   void init(ConfigDescriptor cd, ConfigItemDescriptor cid) {
      this.title.setText(cid.name());
      this.configDescriptor = cd;
      this.configItemDescriptor = cid;
      FontType fontType = this.loadRuneLiteFont();
      this.rebuild(fontType);
   }

   private void rebuild(FontType fontType) {
      this.mainPanel.removeAll();
      List<String> fontOptions = new ArrayList();
      fontOptions.addAll(FontManager.getBuiltInFonts());
      fontOptions.add("---");
      fontOptions.addAll(FontManager.getCustomFonts());
      fontOptions.add("---");
      fontOptions.addAll(FontManager.getSystemFonts());
      JComboBox<Object> comboboxFont = this.combobox(fontOptions.toArray(), fontType.getFamily());
      comboboxFont.addItemListener((e) -> {
         if (e.getStateChange() == 1) {
            FontType f = this.loadRuneLiteFont();
            this.saveRuneLiteFont(f.withFamily((String)comboboxFont.getSelectedItem()));
         }

      });
      this.item("Font", "Configures the font.", comboboxFont);
      JSpinner spinnerSize = this.createIntSpinner(1, Integer.MAX_VALUE, fontType.getSize(), "pt");
      spinnerSize.addChangeListener((ce) -> {
         FontType f = this.loadRuneLiteFont();
         this.saveRuneLiteFont(f.withSize((Integer)spinnerSize.getValue()));
      });
      this.item("Size", "Configures the font size.", spinnerSize);
      JCheckBox checkboxBold = this.checkbox(fontType.isBold());
      checkboxBold.addActionListener((ae) -> {
         FontType f = this.loadRuneLiteFont();
         this.saveRuneLiteFont(f.withBold(checkboxBold.isSelected()));
      });
      this.item("Bold", "Toggle bold styling for the font.", checkboxBold);
      JCheckBox checkboxItalic = this.checkbox(fontType.isItalic());
      checkboxItalic.addActionListener((ae) -> {
         FontType f = this.loadRuneLiteFont();
         this.saveRuneLiteFont(f.withItalic(checkboxItalic.isSelected()));
      });
      this.item("Italic", "Toggle italic styling for the font.", checkboxItalic);
   }

   private FontType loadRuneLiteFont() {
      return (FontType)this.configManager.getConfiguration(this.configDescriptor.getGroup().value(), this.configItemDescriptor.getItem().keyName(), (Type)FontType.class);
   }

   private void saveRuneLiteFont(FontType fontType) {
      this.configManager.setConfiguration(this.configDescriptor.getGroup().value(), this.configItemDescriptor.getItem().keyName(), (Object)fontType);
   }
}
