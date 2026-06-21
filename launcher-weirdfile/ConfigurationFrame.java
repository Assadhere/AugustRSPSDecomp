package net.runelite.launcher;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import javax.annotation.Nullable;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConfigurationFrame extends JFrame {
   private static final Logger log = LoggerFactory.getLogger(ConfigurationFrame.class);
   private static final Color DARKER_GRAY_COLOR = new Color(30, 30, 30);
   private final JCheckBox chkboxDebug;
   private final JCheckBox chkboxNoDiffs;
   private final JCheckBox chkboxSkipTlsVerification;
   private final JCheckBox chkboxNoUpdates;
   private final JCheckBox chkboxSafemode;
   private final JTextField txtScale;
   private final JTextArea txtClientArguments;
   private final JTextArea txtJvmArguments;
   private final JComboBox<HardwareAccelerationMode> comboHardwareAccelMode;
   private final JComboBox<LaunchMode> comboLaunchMode;

   private ConfigurationFrame(LauncherSettings settings) {
      this.setTitle("AugustRSPS Launcher Configuration");

      BufferedImage logo;
      try {
         InputStream in = SplashScreen.class.getResourceAsStream("runelite_transparent.png");

         try {
            logo = ImageIO.read(in);
         } catch (Throwable var12) {
            if (in != null) {
               try {
                  in.close();
               } catch (Throwable var11) {
                  var12.addSuppressed(var11);
               }
            }

            throw var12;
         }

         if (in != null) {
            in.close();
         }
      } catch (IOException var13) {
         IOException ex = var13;
         throw new RuntimeException(ex);
      }

      this.setDefaultCloseOperation(3);
      this.setIconImage(logo);
      Container pane = this.getContentPane();
      pane.setLayout(new BoxLayout(pane, 1));
      pane.setBackground(DARKER_GRAY_COLOR);
      JPanel topPanel = new JPanel();
      topPanel.setBackground(DARKER_GRAY_COLOR);
      topPanel.setLayout(new GridLayout(3, 2, 0, 0));
      topPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 8, 0));
      topPanel.add(this.chkboxDebug = checkbox("Debug", "Runs the launcher and client in debug mode. Debug mode writes debug level logging to the log files.", Boolean.TRUE.equals(settings.debug)));
      topPanel.add(this.chkboxNoDiffs = checkbox("Disable diffs", "Downloads full artifacts for updates instead of diffs.", Boolean.TRUE.equals(settings.nodiffs)));
      topPanel.add(this.chkboxSkipTlsVerification = checkbox("Disable TLS verification", "Disables TLS verification.", Boolean.TRUE.equals(settings.skipTlsVerification)));
      topPanel.add(this.chkboxNoUpdates = checkbox("Disable updates", "Disables the launcher self updating", Boolean.TRUE.equals(settings.noupdates)));
      topPanel.add(this.chkboxSafemode = checkbox("Safe mode", "Launches the client in safe mode", Boolean.TRUE.equals(settings.safemode)));
      pane.add(topPanel);
      JPanel midPanel = new JPanel();
      midPanel.setBackground(DARKER_GRAY_COLOR);
      midPanel.setLayout(new GridLayout(2, 2, 0, 0));
      midPanel.add(label("Client arguments", "Arguments passed to the client. One per line."));
      JScrollPane sp = new JScrollPane(this.txtClientArguments = area(Joiner.on('\n').join(settings.clientArguments)), 22, 30);
      midPanel.add(sp);
      midPanel.add(label("JVM arguments", "Arguments passed to the JVM. One per line."));
      sp = new JScrollPane(this.txtJvmArguments = area(Joiner.on('\n').join(settings.jvmArguments)), 22, 30);
      midPanel.add(sp);
      pane.add(midPanel);
      JPanel bottomPanel = new JPanel();
      bottomPanel.setBackground(DARKER_GRAY_COLOR);
      bottomPanel.setLayout(new GridLayout(3, 2, 0, 0));
      bottomPanel.add(label("Scale", "Scaling factor for Java 2D"));
      bottomPanel.add(this.txtScale = field(settings.scale != null ? Double.toString(settings.scale) : null));
      bottomPanel.add(label("Hardware acceleration", "Hardware acceleration mode for Java 2D."));
      bottomPanel.add(this.comboHardwareAccelMode = combobox(HardwareAccelerationMode.values(), settings.hardwareAccelerationMode));
      bottomPanel.add(label("Launch mode", (String)null));
      bottomPanel.add(this.comboLaunchMode = combobox(LaunchMode.values(), settings.launchMode));
      pane.add(bottomPanel);
      JPanel buttonPanel = new JPanel();
      buttonPanel.setBackground(DARKER_GRAY_COLOR);
      JButton save = new JButton("Save");
      save.addActionListener(this::save);
      buttonPanel.add(save);
      JButton cancel = new JButton("Cancel");
      cancel.addActionListener((l) -> {
         this.dispose();
      });
      buttonPanel.add(cancel);
      pane.add(buttonPanel);
      this.pack();
      this.setLocationRelativeTo((Component)null);
      this.setMinimumSize(this.getSize());
   }

   private void save(ActionEvent l) {
      LauncherSettings settings = LauncherSettings.loadSettings();
      settings.debug = this.chkboxDebug.isSelected();
      settings.nodiffs = this.chkboxNoDiffs.isSelected();
      settings.skipTlsVerification = this.chkboxSkipTlsVerification.isSelected();
      settings.noupdates = this.chkboxNoUpdates.isSelected();
      settings.safemode = this.chkboxSafemode.isSelected();
      String t = this.txtScale.getText();
      settings.scale = null;
      if (!t.isEmpty()) {
         try {
            settings.scale = Double.parseDouble(t);
         } catch (NumberFormatException var5) {
         }
      }

      settings.clientArguments = Splitter.on('\n').omitEmptyStrings().trimResults().splitToList(this.txtClientArguments.getText());
      settings.jvmArguments = Splitter.on('\n').omitEmptyStrings().trimResults().splitToList(this.txtJvmArguments.getText());
      settings.hardwareAccelerationMode = (HardwareAccelerationMode)this.comboHardwareAccelMode.getSelectedItem();
      settings.launchMode = (LaunchMode)this.comboLaunchMode.getSelectedItem();
      LauncherSettings.saveSettings(settings);
      log.info("Updated launcher configuration:" + System.lineSeparator() + "{}", settings.configurationStr());
      this.dispose();
   }

   private static JLabel label(String name, String tooltip) {
      JLabel label = new JLabel(name);
      label.setToolTipText(tooltip);
      label.setForeground(Color.WHITE);
      return label;
   }

   private static JTextField field(@Nullable String value) {
      return new JTextField(value);
   }

   private static JTextArea area(@Nullable String value) {
      return new JTextArea(value, 2, 20);
   }

   private static JCheckBox checkbox(String name, String tooltip, boolean checked) {
      JCheckBox checkbox = new JCheckBox(name);
      checkbox.setSelected(checked);
      checkbox.setToolTipText(tooltip);
      checkbox.setForeground(Color.WHITE);
      checkbox.setBackground(DARKER_GRAY_COLOR);
      return checkbox;
   }

   private static <E> JComboBox<E> combobox(E[] values, E default_) {
      JComboBox<E> combobox = new JComboBox(values);
      combobox.setSelectedItem(default_);
      return combobox;
   }

   static void open() {
      (new ConfigurationFrame(LauncherSettings.loadSettings())).setVisible(true);
   }

   public static void main(String[] args) {
      open();
   }
}
