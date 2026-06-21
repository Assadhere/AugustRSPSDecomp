package net.runelite.client.plugins.devtools;

import com.google.common.base.Strings;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import java.awt.Dialog.ModalityType;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import net.runelite.api.Client;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.ui.ColorScheme;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class DevServerDialog extends JDialog {
   private static final Logger log = LoggerFactory.getLogger(DevServerDialog.class);
   private static final Dimension FIELD_SIZE = new Dimension(360, 28);
   private static final int MIN_PORT = 1;
   private static final int MAX_PORT = 65535;
   private static boolean overrideEnabled;
   private static String sessionHost;
   private static Integer sessionPort;
   private final ClientThread clientThread;
   private final Client client;
   private final JCheckBox overrideEnabledCheckbox;
   private final JTextField hostField;
   private final JTextField portField;

   static void open(Component parent, ClientThread clientThread, Client client) {
      Window owner = SwingUtilities.getWindowAncestor(parent);
      DevServerDialog dialog = new DevServerDialog(owner, clientThread, client);
      dialog.setLocationRelativeTo(parent);
      dialog.setVisible(true);
   }

   static void applySessionSettings(ClientThread clientThread, Client client) {
      String host = overrideEnabled ? sessionHost : null;
      int port = overrideEnabled && sessionPort != null ? sessionPort : 0;
      clientThread.invoke(() -> {
         client.setDevGameServerHost(host);
         client.setDevGameServerPort(port);
      });
   }

   private DevServerDialog(Window owner, ClientThread clientThread, Client client) {
      super(owner, "Dev Server", ModalityType.APPLICATION_MODAL);
      this.clientThread = clientThread;
      this.client = client;
      this.overrideEnabledCheckbox = new JCheckBox("Enable dev-server override");
      this.hostField = new JTextField(sessionHost == null ? "" : sessionHost);
      this.portField = new JTextField(sessionPort == null ? "" : String.valueOf(sessionPort));
      this.overrideEnabledCheckbox.setSelected(overrideEnabled);
      this.overrideEnabledCheckbox.setBackground(ColorScheme.DARK_GRAY_COLOR);
      this.overrideEnabledCheckbox.setForeground(ColorScheme.LIGHT_GRAY_COLOR);
      this.hostField.setPreferredSize(FIELD_SIZE);
      this.portField.setPreferredSize(FIELD_SIZE);
      this.setLayout(new BorderLayout(8, 8));
      this.getContentPane().setBackground(ColorScheme.DARK_GRAY_COLOR);
      this.add(this.createFormPanel(), "Center");
      this.add(this.createControls(), "South");
      this.pack();
   }

   private JPanel createFormPanel() {
      JPanel panel = new JPanel(new GridBagLayout());
      panel.setBackground(ColorScheme.DARK_GRAY_COLOR);
      panel.setBorder(BorderFactory.createEmptyBorder(12, 12, 4, 12));
      GridBagConstraints constraints = new GridBagConstraints();
      constraints.insets = new Insets(4, 4, 4, 4);
      constraints.anchor = 17;
      addInputRow(panel, constraints, 0, "Host", this.hostField);
      addInputRow(panel, constraints, 1, "Port", this.portField);
      constraints.gridx = 0;
      constraints.gridy = 2;
      constraints.gridwidth = 2;
      constraints.weightx = 1.0;
      constraints.fill = 2;
      panel.add(this.overrideEnabledCheckbox, constraints);
      JLabel hint = new JLabel("Enable, host, and port are session-only.");
      hint.setForeground(ColorScheme.LIGHT_GRAY_COLOR);
      constraints.gridx = 0;
      constraints.gridy = 3;
      constraints.gridwidth = 2;
      constraints.fill = 2;
      panel.add(hint, constraints);
      return panel;
   }

   private JPanel createControls() {
      JPanel controls = new JPanel(new GridBagLayout());
      controls.setBorder(BorderFactory.createEmptyBorder(0, 8, 8, 8));
      controls.setBackground(ColorScheme.DARK_GRAY_COLOR);
      JButton saveButton = new JButton("Save");
      saveButton.addActionListener((ev) -> {
         this.save();
      });
      JButton closeButton = new JButton("Close");
      closeButton.addActionListener((ev) -> {
         this.dispose();
      });
      GridBagConstraints constraints = new GridBagConstraints();
      constraints.insets = new Insets(0, 3, 0, 3);
      constraints.gridy = 0;
      constraints.gridx = 0;
      controls.add(saveButton, constraints);
      constraints.gridx = 1;
      controls.add(closeButton, constraints);
      return controls;
   }

   private void save() {
      String host = normalize(this.hostField.getText());
      Integer port = parsePort(this.portField.getText());
      if (port == null && !Strings.isNullOrEmpty(this.portField.getText().trim())) {
         JOptionPane.showMessageDialog(this, "Enter a port between 1 and 65535, or leave it blank.");
      } else {
         overrideEnabled = this.overrideEnabledCheckbox.isSelected();
         sessionHost = overrideEnabled ? host : null;
         sessionPort = overrideEnabled ? port : null;
         String hostToApply = overrideEnabled ? host : null;
         int portToApply = overrideEnabled && port != null ? port : 0;
         this.clientThread.invoke(() -> {
            this.client.setDevGameServerHost(hostToApply);
            this.client.setDevGameServerPort(portToApply);
         });
         this.dispose();
      }
   }

   private static void addInputRow(JPanel panel, GridBagConstraints constraints, int row, String label, JTextField field) {
      constraints.gridx = 0;
      constraints.gridy = row;
      constraints.gridwidth = 1;
      constraints.weightx = 0.0;
      constraints.fill = 0;
      panel.add(new JLabel(label), constraints);
      constraints.gridx = 1;
      constraints.weightx = 1.0;
      constraints.fill = 2;
      panel.add(field, constraints);
   }

   private static Integer parsePort(String value) {
      String trimmed = value == null ? null : value.trim();
      if (Strings.isNullOrEmpty(trimmed)) {
         return null;
      } else {
         try {
            int port = Integer.parseInt(trimmed);
            return port >= 1 && port <= 65535 ? port : null;
         } catch (NumberFormatException var3) {
            return null;
         }
      }
   }

   private static String normalize(String value) {
      String trimmed = value == null ? null : value.trim();
      return Strings.isNullOrEmpty(trimmed) ? null : trimmed;
   }
}
