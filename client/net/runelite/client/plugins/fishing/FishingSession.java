package net.runelite.client.plugins.fishing;

import java.time.Instant;

class FishingSession {
   private Instant lastFishCaught;

   public Instant getLastFishCaught() {
      return this.lastFishCaught;
   }

   public void setLastFishCaught(Instant lastFishCaught) {
      this.lastFishCaught = lastFishCaught;
   }
}
