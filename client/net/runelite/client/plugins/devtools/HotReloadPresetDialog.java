package net.runelite.client.plugins.devtools;

import com.google.common.base.Strings;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import java.awt.Dialog.ModalityType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import net.runelite.api.Client;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.ui.ColorScheme;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class HotReloadPresetDialog extends JDialog {
   private static final Logger log = LoggerFactory.getLogger(HotReloadPresetDialog.class);
   private static final String CONFIG_GROUP = "devtools";
   private static final String CONFIG_PRESETS = "hotReloadPresets";
   private static final String CONFIG_SELECTED_PRESET = "hotReloadSelectedPreset";
   private static final Dimension FIELD_SIZE = new Dimension(460, 28);
   private static final Gson GSON = new Gson();
   private static final Type PRESET_LIST_TYPE = (new TypeToken<List<HotReloadPreset>>() {
   }).getType();
   private final ConfigManager configManager;
   private final ClientThread clientThread;
   private final Client client;
   private final JPanel presetPanel = new JPanel();
   private List<HotReloadPreset> presets;
   private String selectedPresetId;
   private ButtonGroup buttonGroup = new ButtonGroup();

   static void open(Component parent, ConfigManager configManager, ClientThread clientThread, Client client) {
      Window owner = SwingUtilities.getWindowAncestor(parent);
      HotReloadPresetDialog dialog = new HotReloadPresetDialog(owner, configManager, clientThread, client);
      dialog.setLocationRelativeTo(parent);
      dialog.setVisible(true);
   }

   static void applySelectedPreset(ConfigManager configManager, ClientThread clientThread, Client client) {
      HotReloadPreset preset = findPreset(loadPresets(configManager), configManager.getConfiguration("devtools", "hotReloadSelectedPreset"));
      clientThread.invoke(() -> {
         applyPreset(client, preset);
      });
   }

   private HotReloadPresetDialog(Window owner, ConfigManager configManager, ClientThread clientThread, Client client) {
      super(owner, "Hot Reload Directories", ModalityType.APPLICATION_MODAL);
      this.configManager = configManager;
      this.clientThread = clientThread;
      this.client = client;
      this.presets = loadPresets(configManager);
      this.selectedPresetId = configManager.getConfiguration("devtools", "hotReloadSelectedPreset");
      this.setLayout(new BorderLayout(8, 8));
      this.getContentPane().setBackground(ColorScheme.DARK_GRAY_COLOR);
      this.presetPanel.setLayout(new BoxLayout(this.presetPanel, 1));
      this.presetPanel.setBackground(ColorScheme.DARK_GRAY_COLOR);
      JScrollPane scrollPane = new JScrollPane(this.presetPanel);
      scrollPane.setPreferredSize(new Dimension(620, 320));
      scrollPane.setBorder(BorderFactory.createEmptyBorder());
      this.add(scrollPane, "Center");
      this.add(this.createControls(), "South");
      this.renderPresets();
      this.pack();
   }

   private JPanel createControls() {
      JPanel controls = new JPanel(new GridBagLayout());
      controls.setBorder(BorderFactory.createEmptyBorder(0, 8, 8, 8));
      controls.setBackground(ColorScheme.DARK_GRAY_COLOR);
      JButton addButton = new JButton("Add");
      addButton.addActionListener((ev) -> {
         this.addPreset();
      });
      JButton editButton = new JButton("Edit");
      editButton.addActionListener((ev) -> {
         this.editSelectedPreset();
      });
      JButton duplicateButton = new JButton("Duplicate");
      duplicateButton.addActionListener((ev) -> {
         this.duplicateSelectedPreset();
      });
      JButton removeButton = new JButton("Remove");
      removeButton.addActionListener((ev) -> {
         this.removeSelectedPreset();
      });
      JButton closeButton = new JButton("Close");
      closeButton.addActionListener((ev) -> {
         this.dispose();
      });
      GridBagConstraints constraints = new GridBagConstraints();
      constraints.insets = new Insets(0, 3, 0, 3);
      constraints.gridy = 0;
      constraints.gridx = 0;
      controls.add(addButton, constraints);
      constraints.gridx = 1;
      controls.add(editButton, constraints);
      constraints.gridx = 2;
      controls.add(duplicateButton, constraints);
      constraints.gridx = 3;
      controls.add(removeButton, constraints);
      constraints.gridx = 4;
      controls.add(closeButton, constraints);
      return controls;
   }

   private void renderPresets() {
      this.presetPanel.removeAll();
      this.buttonGroup = new ButtonGroup();
      JRadioButton noneButton = new JRadioButton("None", this.getSelectedPreset() == null);
      noneButton.setBackground(ColorScheme.DARK_GRAY_COLOR);
      noneButton.setForeground(ColorScheme.TEXT_COLOR);
      noneButton.addActionListener((ev) -> {
         this.selectPreset((String)null);
      });
      this.buttonGroup.add(noneButton);
      this.presetPanel.add(this.createRadioRow(noneButton, "Disable script and interface hot reload paths.", (String)null));
      Iterator var2 = this.presets.iterator();

      while(var2.hasNext()) {
         HotReloadPreset preset = (HotReloadPreset)var2.next();
         JRadioButton presetButton = new JRadioButton(preset.getName(), Objects.equals(preset.id, this.selectedPresetId));
         presetButton.setBackground(ColorScheme.DARK_GRAY_COLOR);
         presetButton.setForeground(ColorScheme.TEXT_COLOR);
         presetButton.addActionListener((ev) -> {
            this.selectPreset(preset.id);
         });
         this.buttonGroup.add(presetButton);
         this.presetPanel.add(this.createRadioRow(presetButton, "Interfaces: " + displayPath(preset.interfacePath), "Scripts: " + displayPath(preset.scriptPath)));
      }

      this.presetPanel.revalidate();
      this.presetPanel.repaint();
   }

   private JPanel createRadioRow(JRadioButton radioButton, String firstDetail, String secondDetail) {
      JPanel row = new JPanel(new BorderLayout(6, 0));
      row.setBorder(BorderFactory.createEmptyBorder(6, 8, 6, 8));
      row.setBackground(ColorScheme.DARK_GRAY_COLOR);
      JPanel detailPanel = new JPanel();
      detailPanel.setLayout(new BoxLayout(detailPanel, 1));
      detailPanel.setBackground(ColorScheme.DARK_GRAY_COLOR);
      JLabel firstLabel = new JLabel(firstDetail);
      firstLabel.setForeground(ColorScheme.LIGHT_GRAY_COLOR);
      detailPanel.add(firstLabel);
      if (secondDetail != null) {
         JLabel secondLabel = new JLabel(secondDetail);
         secondLabel.setForeground(ColorScheme.LIGHT_GRAY_COLOR);
         detailPanel.add(secondLabel);
      }

      row.add(radioButton, "North");
      row.add(detailPanel, "Center");
      return row;
   }

   private void addPreset() {
      HotReloadPreset preset = this.promptForPreset("Add Hot Reload Preset", (HotReloadPreset)null);
      if (preset != null) {
         this.presets.add(preset);
         this.savePresets();
         this.selectPreset(preset.id);
         this.renderPresets();
      }
   }

   private void editSelectedPreset() {
      HotReloadPreset selectedPreset = this.getSelectedPreset();
      if (selectedPreset == null) {
         JOptionPane.showMessageDialog(this, "Select a preset to edit.");
      } else {
         HotReloadPreset updatedPreset = this.promptForPreset("Edit Hot Reload Preset", selectedPreset);
         if (updatedPreset != null) {
            for(int i = 0; i < this.presets.size(); ++i) {
               if (Objects.equals(((HotReloadPreset)this.presets.get(i)).id, selectedPreset.id)) {
                  this.presets.set(i, updatedPreset);
                  break;
               }
            }

            this.savePresets();
            this.applySelectedPreset();
            this.renderPresets();
         }
      }
   }

   private void duplicateSelectedPreset() {
      HotReloadPreset selectedPreset = this.getSelectedPreset();
      if (selectedPreset == null) {
         JOptionPane.showMessageDialog(this, "Select a preset to duplicate.");
      } else {
         HotReloadPreset presetCopy = new HotReloadPreset(UUID.randomUUID().toString(), selectedPreset.getName() + " Copy", selectedPreset.interfacePath, selectedPreset.scriptPath);
         HotReloadPreset updatedPreset = this.promptForPreset("Duplicate Hot Reload Preset", presetCopy);
         if (updatedPreset != null) {
            this.presets.add(updatedPreset);
            this.savePresets();
            this.selectPreset(updatedPreset.id);
            this.renderPresets();
         }
      }
   }

   private void removeSelectedPreset() {
      HotReloadPreset selectedPreset = this.getSelectedPreset();
      if (selectedPreset == null) {
         JOptionPane.showMessageDialog(this, "Select a preset to remove.");
      } else {
         int result = JOptionPane.showConfirmDialog(this, "Remove preset '" + selectedPreset.getName() + "'?", "Remove Hot Reload Preset", 2, 2);
         if (result == 0) {
            this.presets.removeIf((preset) -> {
               return Objects.equals(preset.id, selectedPreset.id);
            });
            this.savePresets();
            this.selectPreset((String)null);
            this.renderPresets();
         }
      }
   }

   private HotReloadPreset promptForPreset(String title, HotReloadPreset preset) {
      JTextField nameField = new JTextField(preset == null ? "" : preset.name);
      JTextField interfaceField = new JTextField(preset == null ? "" : nullToEmpty(preset.interfacePath));
      JTextField scriptField = new JTextField(preset == null ? "" : nullToEmpty(preset.scriptPath));
      nameField.setPreferredSize(FIELD_SIZE);
      interfaceField.setPreferredSize(FIELD_SIZE);
      scriptField.setPreferredSize(FIELD_SIZE);
      JPanel panel = new JPanel(new GridBagLayout());
      GridBagConstraints constraints = new GridBagConstraints();
      constraints.insets = new Insets(4, 4, 4, 4);
      constraints.anchor = 17;
      addInputRow(panel, constraints, 0, "Name", nameField);
      addInputRow(panel, constraints, 1, "Interface directory", interfaceField);
      addInputRow(panel, constraints, 2, "Script directory", scriptField);

      while(true) {
         while(true) {
            int result = JOptionPane.showConfirmDialog(this, panel, title, 2, -1);
            if (result != 0) {
               return null;
            }

            String name = normalize(nameField.getText());
            String interfacePath = normalize(interfaceField.getText());
            String scriptPath = normalize(scriptField.getText());
            if (name != null) {
               if (interfacePath != null || scriptPath != null) {
                  String id = preset == null ? UUID.randomUUID().toString() : preset.id;
                  return new HotReloadPreset(id, name, interfacePath, scriptPath);
               }

               JOptionPane.showMessageDialog(this, "Enter an interface directory, a script directory, or both.");
            } else {
               JOptionPane.showMessageDialog(this, "Enter a preset name.");
            }
         }
      }
   }

   private static void addInputRow(JPanel panel, GridBagConstraints constraints, int row, String label, JTextField field) {
      constraints.gridx = 0;
      constraints.gridy = row;
      constraints.weightx = 0.0;
      constraints.fill = 0;
      panel.add(new JLabel(label), constraints);
      constraints.gridx = 1;
      constraints.weightx = 1.0;
      constraints.fill = 2;
      panel.add(field, constraints);
   }

   private void selectPreset(String presetId) {
      this.selectedPresetId = nullToEmpty(presetId);
      this.configManager.setConfiguration("devtools", "hotReloadSelectedPreset", this.selectedPresetId);
      this.applySelectedPreset();
   }

   private void applySelectedPreset() {
      HotReloadPreset preset = this.getSelectedPreset();
      this.clientThread.invoke(() -> {
         applyPreset(this.client, preset);
      });
   }

   private HotReloadPreset getSelectedPreset() {
      return findPreset(this.presets, this.selectedPresetId);
   }

   private void savePresets() {
      this.configManager.setConfiguration("devtools", "hotReloadPresets", GSON.toJson(this.presets, PRESET_LIST_TYPE));
   }

   private static List<HotReloadPreset> loadPresets(ConfigManager configManager) {
      String storedPresets = configManager.getConfiguration("devtools", "hotReloadPresets");
      if (Strings.isNullOrEmpty(storedPresets)) {
         return new ArrayList();
      } else {
         try {
            List<HotReloadPreset> loadedPresets = (List)GSON.fromJson(storedPresets, PRESET_LIST_TYPE);
            if (loadedPresets == null) {
               return new ArrayList();
            } else {
               List<HotReloadPreset> sanitizedPresets = new ArrayList();
               Iterator var4 = loadedPresets.iterator();

               while(true) {
                  HotReloadPreset preset;
                  String id;
                  String interfacePath;
                  String scriptPath;
                  do {
                     do {
                        do {
                           if (!var4.hasNext()) {
                              return sanitizedPresets;
                           }

                           preset = (HotReloadPreset)var4.next();
                        } while(preset == null);
                     } while(normalize(preset.name) == null);

                     id = normalize(preset.id);
                     if (id == null) {
                        id = UUID.randomUUID().toString();
                     }

                     interfacePath = normalize(preset.interfacePath);
                     scriptPath = normalize(preset.scriptPath);
                  } while(interfacePath == null && scriptPath == null);

                  sanitizedPresets.add(new HotReloadPreset(id, normalize(preset.name), interfacePath, scriptPath));
               }
            }
         } catch (JsonSyntaxException var9) {
            JsonSyntaxException ex = var9;
            log.warn("Unable to read hot reload presets", ex);
            return new ArrayList();
         }
      }
   }

   private static HotReloadPreset findPreset(List<HotReloadPreset> presets, String presetId) {
      String normalizedPresetId = normalize(presetId);
      if (normalizedPresetId == null) {
         return null;
      } else {
         Iterator var3 = presets.iterator();

         HotReloadPreset preset;
         do {
            if (!var3.hasNext()) {
               return null;
            }

            preset = (HotReloadPreset)var3.next();
         } while(!Objects.equals(preset.id, normalizedPresetId));

         return preset;
      }
   }

   private static void applyPreset(Client client, HotReloadPreset preset) {
      client.setDevelopmentInterfaceLocation(preset == null ? null : preset.interfacePath);
      client.setDevelopmentScriptLocation(preset == null ? null : preset.scriptPath);
      client.clearDevelopmentComponentCache();
      client.clearDevelopmentScriptCache();
   }

   private static String displayPath(String path) {
      return path == null ? "(none)" : path;
   }

   private static String normalize(String value) {
      String trimmed = value == null ? null : value.trim();
      return Strings.isNullOrEmpty(trimmed) ? null : trimmed;
   }

   private static String nullToEmpty(String value) {
      return value == null ? "" : value;
   }

   private static final class HotReloadPreset {
      private String id;
      private String name;
      private String interfacePath;
      private String scriptPath;

      private HotReloadPreset(String id, String name, String interfacePath, String scriptPath) {
         this.id = id;
         this.name = name;
         this.interfacePath = interfacePath;
         this.scriptPath = scriptPath;
      }

      private String getName() {
         String normalizedName = HotReloadPresetDialog.normalize(this.name);
         return normalizedName == null ? "Untitled" : normalizedName;
      }
   }
}
