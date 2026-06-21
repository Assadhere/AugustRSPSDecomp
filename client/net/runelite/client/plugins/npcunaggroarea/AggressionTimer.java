package net.runelite.client.plugins.npcunaggroarea;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import net.runelite.client.ui.overlay.infobox.Timer;

class AggressionTimer extends Timer {
   private final NpcAggroAreaPlugin plugin;

   AggressionTimer(Duration duration, BufferedImage image, NpcAggroAreaPlugin plugin) {
      super(duration.toMillis(), ChronoUnit.MILLIS, image, plugin);
      this.setTooltip("Time until NPCs become unaggressive");
      this.plugin = plugin;
   }

   public Color getTextColor() {
      Duration timeLeft = Duration.between(Instant.now(), this.getEndTime());
      return timeLeft.getSeconds() < 60L ? Color.RED.brighter() : Color.WHITE;
   }

   public boolean render() {
      return this.plugin.shouldDisplayTimer() && super.render();
   }
}
