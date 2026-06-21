package net.runelite.client;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import net.runelite.client.ui.FontManager;

class ClassPreloader {
   static void preload() {
      FontManager.getRunescapeSmallFont();
      ZoneId.of("Europe/London");
      Object unused = DateTimeFormatter.BASIC_ISO_DATE;
   }
}
