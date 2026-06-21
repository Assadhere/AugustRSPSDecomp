package net.runelite.client.plugins.timersandbuffs;

import java.awt.Color;
import java.awt.image.BufferedImage;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.ui.overlay.infobox.InfoBox;
import net.runelite.client.ui.overlay.infobox.InfoBoxPriority;

class CustomServerIndicator extends InfoBox {
   private final int indicatorId;
   private final String description;
   private final GameTimerImageType imageType;

   CustomServerIndicator(int indicatorId, GameTimerImageType imageType, String description, Plugin plugin) {
      super((BufferedImage)null, plugin);
      this.indicatorId = indicatorId;
      this.description = description;
      this.imageType = imageType;
      this.setPriority(InfoBoxPriority.MED);
   }

   public String getText() {
      return "";
   }

   public Color getTextColor() {
      return Color.BLACK;
   }

   public String getName() {
      return "custom_indicator_" + this.indicatorId;
   }

   public int getIndicatorId() {
      return this.indicatorId;
   }

   public String getDescription() {
      return this.description;
   }

   public GameTimerImageType getImageType() {
      return this.imageType;
   }
}
