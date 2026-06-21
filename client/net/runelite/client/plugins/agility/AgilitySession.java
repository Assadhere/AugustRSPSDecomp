package net.runelite.client.plugins.agility;

import com.google.common.collect.EvictingQueue;
import java.time.Duration;
import java.time.Instant;
import java.util.Iterator;
import net.runelite.api.Client;
import net.runelite.api.Skill;
import net.runelite.client.plugins.xptracker.XpTrackerService;

class AgilitySession {
   private final Courses course;
   private Instant lastLapCompleted;
   private int totalLaps;
   private int lapsTillGoal;
   private final EvictingQueue<Duration> lastLapTimes = EvictingQueue.create(30);
   private int lapsPerHour;

   AgilitySession(Courses course) {
      this.course = course;
   }

   void incrementLapCount(Client client, XpTrackerService xpTrackerService) {
      this.calculateLapsPerHour();
      ++this.totalLaps;
      this.recalculateLapsTillGoal(client, xpTrackerService);
   }

   void recalculateLapsTillGoal(Client client, XpTrackerService xpTrackerService) {
      int currentExp = client.getSkillExperience(Skill.AGILITY);
      int goalXp = xpTrackerService.getEndGoalXp(Skill.AGILITY);
      int goalRemainingXp = goalXp - currentExp;
      double courseTotalExp = (Double)this.course.getTotalXpProvider().apply(client);
      if (this.course == Courses.PYRAMID) {
         courseTotalExp += (double)Math.min(300 + 8 * client.getRealSkillLevel(Skill.AGILITY), 1000);
      }

      this.lapsTillGoal = goalRemainingXp > 0 ? (int)Math.ceil((double)goalRemainingXp / courseTotalExp) : 0;
   }

   void calculateLapsPerHour() {
      Instant now = Instant.now();
      if (this.lastLapCompleted != null) {
         Duration timeSinceLastLap = Duration.between(this.lastLapCompleted, now);
         if (!timeSinceLastLap.isNegative()) {
            this.lastLapTimes.add(timeSinceLastLap);
            Duration sum = Duration.ZERO;

            Duration lapTime;
            for(Iterator var4 = this.lastLapTimes.iterator(); var4.hasNext(); sum = sum.plus(lapTime)) {
               lapTime = (Duration)var4.next();
            }

            Duration averageLapTime = sum.dividedBy((long)this.lastLapTimes.size());
            this.lapsPerHour = (int)(Duration.ofHours(1L).toMillis() / averageLapTime.toMillis());
         }
      }

      this.lastLapCompleted = now;
   }

   public Courses getCourse() {
      return this.course;
   }

   public Instant getLastLapCompleted() {
      return this.lastLapCompleted;
   }

   public int getTotalLaps() {
      return this.totalLaps;
   }

   public int getLapsTillGoal() {
      return this.lapsTillGoal;
   }

   public EvictingQueue<Duration> getLastLapTimes() {
      return this.lastLapTimes;
   }

   public int getLapsPerHour() {
      return this.lapsPerHour;
   }

   public void setLastLapCompleted(Instant lastLapCompleted) {
      this.lastLapCompleted = lastLapCompleted;
   }

   public void setTotalLaps(int totalLaps) {
      this.totalLaps = totalLaps;
   }

   public void setLapsTillGoal(int lapsTillGoal) {
      this.lapsTillGoal = lapsTillGoal;
   }

   public void setLapsPerHour(int lapsPerHour) {
      this.lapsPerHour = lapsPerHour;
   }
}
