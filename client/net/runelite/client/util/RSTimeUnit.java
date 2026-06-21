package net.runelite.client.util;

import java.time.Duration;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalUnit;

public enum RSTimeUnit implements TemporalUnit {
   CLIENT_TICKS("Client tick", Duration.ofMillis(20L)),
   GAME_TICKS("Game tick", Duration.ofMillis(600L));

   private final String name;
   private final Duration duration;

   private RSTimeUnit(String name, Duration estimatedDuration) {
      this.name = name;
      this.duration = estimatedDuration;
   }

   public boolean isDurationEstimated() {
      return false;
   }

   public boolean isDateBased() {
      return false;
   }

   public boolean isTimeBased() {
      return true;
   }

   public boolean isSupportedBy(Temporal temporal) {
      return temporal.isSupported(this);
   }

   public <R extends Temporal> R addTo(R temporal, long amount) {
      return temporal.plus(amount, this);
   }

   public long between(Temporal temporal1Inclusive, Temporal temporal2Exclusive) {
      return temporal1Inclusive.until(temporal2Exclusive, this);
   }

   public String toString() {
      String var10000 = this.name;
      return var10000 + " (" + this.duration.toMillis() + "ms)";
   }

   public String getName() {
      return this.name;
   }

   public Duration getDuration() {
      return this.duration;
   }
}
