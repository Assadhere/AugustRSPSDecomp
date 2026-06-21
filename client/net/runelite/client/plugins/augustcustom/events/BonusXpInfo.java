package net.runelite.client.plugins.augustcustom.events;

import java.awt.image.BufferedImage;
import java.time.Instant;
import javax.swing.JLabel;

class BonusXpInfo {
   private final int worldId;
   private final BufferedImage skillIcon;
   private final Instant endTime;
   private JLabel timeLabel;

   BonusXpInfo(int worldId, BufferedImage skillIcon, long endTimeMillis) {
      this.worldId = worldId;
      this.skillIcon = skillIcon;
      this.endTime = Instant.ofEpochMilli(endTimeMillis);
   }

   public int getWorldId() {
      return this.worldId;
   }

   public BufferedImage getSkillIcon() {
      return this.skillIcon;
   }

   public Instant getEndTime() {
      return this.endTime;
   }

   public JLabel getTimeLabel() {
      return this.timeLabel;
   }

   public void setTimeLabel(JLabel timeLabel) {
      this.timeLabel = timeLabel;
   }
}
