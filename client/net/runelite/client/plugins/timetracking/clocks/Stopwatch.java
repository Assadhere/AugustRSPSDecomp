package net.runelite.client.plugins.timetracking.clocks;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

class Stopwatch extends Clock {
   private long elapsed = 0L;
   private List<Long> laps = new ArrayList();

   Stopwatch(String name) {
      super(name);
   }

   long getDisplayTime() {
      return !this.active ? this.elapsed : Math.max(0L, this.elapsed + (Instant.now().getEpochSecond() - this.lastUpdate));
   }

   void setDuration(long duration) {
      this.elapsed = duration;
   }

   boolean start() {
      if (!this.active) {
         this.lastUpdate = Instant.now().getEpochSecond();
         this.active = true;
         return true;
      } else {
         return false;
      }
   }

   boolean pause() {
      if (this.active) {
         this.active = false;
         this.elapsed = Math.max(0L, this.elapsed + (Instant.now().getEpochSecond() - this.lastUpdate));
         this.lastUpdate = Instant.now().getEpochSecond();
         return true;
      } else {
         return false;
      }
   }

   void lap() {
      this.laps.add(this.getDisplayTime());
   }

   void reset() {
      this.active = false;
      this.elapsed = 0L;
      this.laps.clear();
      this.lastUpdate = Instant.now().getEpochSecond();
   }

   public long getElapsed() {
      return this.elapsed;
   }

   public List<Long> getLaps() {
      return this.laps;
   }

   public void setElapsed(long elapsed) {
      this.elapsed = elapsed;
   }

   public void setLaps(List<Long> laps) {
      this.laps = laps;
   }

   public Stopwatch(long elapsed, List<Long> laps) {
      this.elapsed = elapsed;
      this.laps = laps;
   }
}
