package net.runelite.launcher;

import com.google.common.base.Strings;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.Toolkit;
import java.awt.Desktop.Action;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.StringSelection;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LinkBrowser {
   private static final Logger log = LoggerFactory.getLogger(LinkBrowser.class);
   private static boolean shouldAttemptXdg;

   public static void browse(String url) {
      (new Thread(() -> {
         if (Strings.isNullOrEmpty(url)) {
            log.warn("LinkBrowser.browse() called with invalid input");
         } else if (shouldAttemptXdg && attemptXdgOpen(url)) {
            log.debug("Opened url through xdg-open to {}", url);
         } else if (attemptDesktopBrowse(url)) {
            log.debug("Opened url through Desktop#browse to {}", url);
         } else {
            log.warn("LinkBrowser.browse() could not open {}", url);
            showMessageBox("Unable to open link. Press 'OK' and the link will be copied to your clipboard.", url);
         }
      })).start();
   }

   public static void open(String directory) {
      (new Thread(() -> {
         if (Strings.isNullOrEmpty(directory)) {
            log.warn("LinkBrowser.open() called with invalid input");
         } else if (shouldAttemptXdg && attemptXdgOpen(directory)) {
            log.debug("Opened directory through xdg-open to {}", directory);
         } else if (attemptDesktopOpen(directory)) {
            log.debug("Opened directory through Desktop#open to {}", directory);
         } else {
            log.warn("LinkBrowser.open() could not open {}", directory);
            showMessageBox("Unable to open folder. Press 'OK' and the folder directory will be copied to your clipboard.", directory);
         }
      })).start();
   }

   private static boolean attemptXdgOpen(String resource) {
      try {
         Process exec = Runtime.getRuntime().exec(new String[]{"xdg-open", resource});
         exec.waitFor();
         int ret = exec.exitValue();
         if (ret == 0) {
            return true;
         } else {
            log.warn("xdg-open {} returned with error code {}", resource, ret);
            return false;
         }
      } catch (IOException var3) {
         shouldAttemptXdg = false;
         return false;
      } catch (InterruptedException var4) {
         log.warn("Interrupted while waiting for xdg-open {} to execute", resource);
         return false;
      }
   }

   private static boolean attemptDesktopBrowse(String url) {
      if (!Desktop.isDesktopSupported()) {
         return false;
      } else {
         Desktop desktop = Desktop.getDesktop();
         if (!desktop.isSupported(Action.BROWSE)) {
            return false;
         } else {
            try {
               desktop.browse(new URI(url));
               return true;
            } catch (URISyntaxException | IOException var3) {
               Exception ex = var3;
               log.warn("Failed to open Desktop#browse {}", url, ex);
               return false;
            }
         }
      }
   }

   private static boolean attemptDesktopOpen(String directory) {
      if (!Desktop.isDesktopSupported()) {
         return false;
      } else {
         Desktop desktop = Desktop.getDesktop();
         if (!desktop.isSupported(Action.OPEN)) {
            return false;
         } else {
            try {
               desktop.open(new File(directory));
               return true;
            } catch (IOException var3) {
               IOException ex = var3;
               log.warn("Failed to open Desktop#open {}", directory, ex);
               return false;
            }
         }
      }
   }

   private static void showMessageBox(String message, String data) {
      SwingUtilities.invokeLater(() -> {
         int result = JOptionPane.showConfirmDialog((Component)null, message, "Message", 2);
         if (result == 0) {
            StringSelection stringSelection = new StringSelection(data);
            Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringSelection, (ClipboardOwner)null);
         }

      });
   }

   static {
      shouldAttemptXdg = OS.getOs() == OS.OSType.Linux;
   }
}
