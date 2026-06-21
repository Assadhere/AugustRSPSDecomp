package net.runelite.client.plugins.prayer;

import java.awt.Color;
import java.awt.image.BufferedImage;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.ui.overlay.infobox.InfoBox;

class PrayerCounter extends InfoBox {
   private final PrayerType prayerType;

   PrayerCounter(Plugin plugin, PrayerType prayerType) {
      super((BufferedImage)null, plugin);
      this.prayerType = prayerType;
   }

   public String getText() {
      return null;
   }

   public Color getTextColor() {
      return null;
   }

   public String getTooltip() {
      return this.prayerType.getDescription();
   }

   public PrayerType getPrayerType() {
      return this.prayerType;
   }
}
