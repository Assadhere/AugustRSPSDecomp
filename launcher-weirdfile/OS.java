package net.runelite.launcher;

import javax.annotation.Nonnull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OS {
   private static final Logger log = LoggerFactory.getLogger(OS.class);
   private static final OSType DETECTED_OS;

   static OSType parseOs(@Nonnull String os) {
      os = os.toLowerCase();
      if (!os.contains("mac") && !os.contains("darwin")) {
         if (os.contains("win")) {
            return OS.OSType.Windows;
         } else {
            return os.contains("linux") ? OS.OSType.Linux : OS.OSType.Other;
         }
      } else {
         return OS.OSType.MacOS;
      }
   }

   public static OSType getOs() {
      return DETECTED_OS;
   }

   static {
      String os = System.getProperty("os.name", "generic").toLowerCase();
      DETECTED_OS = parseOs(os);
      log.debug("Detect OS: {}", DETECTED_OS);
   }

   public static enum OSType {
      Windows,
      MacOS,
      Linux,
      Other;
   }
}
