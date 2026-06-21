package net.runelite.launcher;

import com.google.common.base.MoreObjects;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.security.GeneralSecurityException;
import java.security.cert.CertificateException;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.imageio.ImageIO;
import javax.net.ssl.SSLHandshakeException;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FatalErrorDialog extends JDialog {
   private static final Logger log = LoggerFactory.getLogger(FatalErrorDialog.class);
   private static final AtomicBoolean alreadyOpen = new AtomicBoolean(false);
   private static final Color DARKER_GRAY_COLOR = new Color(30, 30, 30);
   private static final Color DARK_GRAY_COLOR = new Color(40, 40, 40);
   private static final Color DARK_GRAY_HOVER_COLOR = new Color(35, 35, 35);
   private final JPanel rightColumn = new JPanel();
   private final Font font = new Font("Dialog", 0, 12);

   public FatalErrorDialog(String message) {
      if (alreadyOpen.getAndSet(true)) {
         throw new IllegalStateException("Fatal error during fatal error: " + message);
      } else {
         try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
         } catch (Exception var7) {
         }

         UIManager.put("Button.select", DARKER_GRAY_COLOR);

         try {
            BufferedImage logo = ImageIO.read(SplashScreen.class.getResourceAsStream("runelite_transparent.png"));
            this.setIconImage(logo);
            JLabel runelite = new JLabel();
            runelite.setIcon(new ImageIcon(logo));
            runelite.setAlignmentX(0.5F);
            runelite.setBackground(DARK_GRAY_COLOR);
            runelite.setOpaque(true);
            this.rightColumn.add(runelite);
         } catch (IOException var6) {
         }

         this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
               System.exit(-1);
            }
         });
         this.setTitle("Fatal error starting AugustRSPS");
         this.setLayout(new BorderLayout());
         Container pane = this.getContentPane();
         pane.setBackground(DARKER_GRAY_COLOR);
         JPanel leftPane = new JPanel();
         leftPane.setBackground(DARKER_GRAY_COLOR);
         leftPane.setLayout(new BorderLayout());
         JLabel title = new JLabel("There was a fatal error starting AugustRSPS");
         title.setForeground(Color.WHITE);
         title.setFont(this.font.deriveFont(16.0F));
         title.setBorder(new EmptyBorder(10, 10, 10, 10));
         leftPane.add(title, "North");
         leftPane.setPreferredSize(new Dimension(400, 200));
         JTextArea textArea = new JTextArea(message);
         textArea.setFont(this.font);
         textArea.setBackground(DARKER_GRAY_COLOR);
         textArea.setForeground(Color.LIGHT_GRAY);
         textArea.setLineWrap(true);
         textArea.setWrapStyleWord(true);
         textArea.setBorder(new EmptyBorder(10, 10, 10, 10));
         textArea.setEditable(false);
         leftPane.add(textArea, "Center");
         pane.add(leftPane, "Center");
         this.rightColumn.setLayout(new BoxLayout(this.rightColumn, 1));
         this.rightColumn.setBackground(DARK_GRAY_COLOR);
         this.rightColumn.setMaximumSize(new Dimension(200, Integer.MAX_VALUE));
         this.addButton("Open logs folder", () -> {
            LinkBrowser.open(Launcher.LOGS_DIR.toString());
         });
         this.addButton("Get help on Discord", () -> {
            LinkBrowser.browse(LauncherProperties.getDiscordInvite());
         });
         pane.add(this.rightColumn, "East");
      }
   }

   public void open() {
      this.addButton("Exit", () -> {
         System.exit(-1);
      });
      this.pack();
      SplashScreen.stop();
      this.setLocationRelativeTo((Component)null);
      this.setVisible(true);
   }

   public FatalErrorDialog addButton(String message, Runnable action) {
      JButton button = new JButton(message);
      button.addActionListener((e) -> {
         action.run();
      });
      button.setFont(this.font);
      button.setBackground(DARK_GRAY_COLOR);
      button.setForeground(Color.LIGHT_GRAY);
      button.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, DARK_GRAY_COLOR.brighter()), new EmptyBorder(4, 4, 4, 4)));
      button.setAlignmentX(0.5F);
      button.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
      button.setFocusPainted(false);
      button.addChangeListener((ev) -> {
         if (button.getModel().isPressed()) {
            button.setBackground(DARKER_GRAY_COLOR);
         } else if (button.getModel().isRollover()) {
            button.setBackground(DARK_GRAY_HOVER_COLOR);
         } else {
            button.setBackground(DARK_GRAY_COLOR);
         }

      });
      this.rightColumn.add(button);
      this.rightColumn.revalidate();
      return this;
   }

   public static void showNetErrorWindow(String action, Throwable err) {
      if (!(err instanceof VerificationException) && !(err instanceof GeneralSecurityException)) {
         if (err instanceof SocketException) {
            (new FatalErrorDialog(formatExceptionMessage("AugustRSPS is unable to connect to a required server while " + action + ". Please check your internet connection.", err))).open();
         } else if (err instanceof UnknownHostException) {
            (new FatalErrorDialog(formatExceptionMessage("AugustRSPS is unable to resolve the address of a required server while " + action + ". Your DNS resolver may be misconfigured, pointing to an inaccurate resolver, or your internet connection may be down.", err))).addButton("Change your DNS resolver", () -> {
               LinkBrowser.browse(LauncherProperties.getDNSChangeLink());
            }).open();
         } else if (err instanceof SSLHandshakeException) {
            if (err.getCause() instanceof CertificateException) {
               (new FatalErrorDialog(formatExceptionMessage("AugustRSPS was unable to verify the certificate of a required server while " + action + ". This can be caused by a firewall, antivirus, malware, misbehaving internet service provider, or a proxy.", err))).open();
            } else {
               (new FatalErrorDialog(formatExceptionMessage("AugustRSPS was unable to establish a SSL/TLS connection with a required server while " + action + ". This can be caused by a firewall, antivirus, malware, misbehaving internet service provider, or a proxy.", err))).open();
            }

         } else {
            (new FatalErrorDialog(formatExceptionMessage("AugustRSPS encountered a fatal error while " + action + ".", err))).open();
         }
      } else {
         (new FatalErrorDialog(formatExceptionMessage("AugustRSPS was unable to verify the security of its connection to the internet while " + action + ". You may have a misbehaving antivirus, internet service provider, a proxy, or an incomplete java installation.", err))).open();
      }
   }

   private static String formatExceptionMessage(String message, Throwable err) {
      String nl = System.getProperty("line.separator");
      return message + nl + nl + "Exception: " + err.getClass().getSimpleName() + nl + "Message: " + (String)MoreObjects.firstNonNull(err.getMessage(), "n/a");
   }
}
