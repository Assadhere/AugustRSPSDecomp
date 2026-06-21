package net.runelite.client.util;

public enum OSType {
   Windows,
   MacOS,
   Linux,
   Other;

   private static final OSType OS_TYPE;

   public static OSType getOSType() {
      return OS_TYPE;
   }

   static {
      String OS = System.getProperty("os.name", "generic").toLowerCase();
      if (!OS.contains("mac") && !OS.contains("darwin")) {
         if (OS.contains("win")) {
            OS_TYPE = Windows;
         } else if (OS.contains("nux")) {
            OS_TYPE = Linux;
         } else {
            OS_TYPE = Other;
         }
      } else {
         OS_TYPE = MacOS;
      }

   }
}
