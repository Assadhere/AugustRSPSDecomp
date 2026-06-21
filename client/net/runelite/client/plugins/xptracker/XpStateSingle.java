package net.runelite.client.plugins.xptracker;

import java.util.Arrays;
import net.runelite.api.Experience;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class XpStateSingle {
   private static final Logger log = LoggerFactory.getLogger(XpStateSingle.class);
   private int actions = 0;
   private int actionsSinceReset = 0;
   private boolean actionsHistoryInitialized = false;
   private final int[] actionExps = new int[10];
   private int actionExpIndex = 0;
   private long startXp;
   private int xpGainedSinceReset = 0;
   private int xpGainedBeforeReset = 0;
   private long skillTime = 0L;
   private long lastChangeMillis;
   private int startLevelExp = 0;
   private int endLevelExp = 0;
   private boolean compactView;

   XpStateSingle(long startXp) {
      this.startXp = startXp;
   }

   long getCurrentXp() {
      return this.startXp + (long)this.getTotalXpGained();
   }

   int getTotalXpGained() {
      return this.xpGainedBeforeReset + this.xpGainedSinceReset;
   }

   private int getActionsHr() {
      return this.toHourly(this.actionsSinceReset);
   }

   private int toHourly(int value) {
      return (int)(1.0 / ((double)this.getTimeElapsedInSeconds() / 3600.0) * (double)value);
   }

   private long getTimeElapsedInSeconds() {
      return Math.max(60L, this.skillTime / 1000L);
   }

   private int getXpRemaining() {
      return this.endLevelExp - (int)this.getCurrentXp();
   }

   private int getActionsRemaining() {
      if (this.actionsHistoryInitialized) {
         long xpRemaining = (long)(this.getXpRemaining() * this.actionExps.length);
         long totalActionXp = 0L;
         int[] var5 = this.actionExps;
         int var6 = var5.length;

         for(int var7 = 0; var7 < var6; ++var7) {
            int actionXp = var5[var7];
            totalActionXp += (long)actionXp;
         }

         if (totalActionXp > 0L) {
            long remainder = xpRemaining % totalActionXp;
            long quotient = xpRemaining / totalActionXp;
            return Math.toIntExact(quotient + (long)(remainder > 0L ? 1 : 0));
         }
      }

      return Integer.MAX_VALUE;
   }

   private double getSkillProgress() {
      double xpGained = (double)(this.getCurrentXp() - (long)this.startLevelExp);
      double xpGoal = (double)(this.endLevelExp - this.startLevelExp);
      return xpGained / xpGoal * 100.0;
   }

   private long getSecondsTillLevel() {
      long seconds = this.getTimeElapsedInSeconds();
      return seconds > 0L && this.xpGainedSinceReset > 0 ? (long)this.getXpRemaining() * seconds / (long)this.xpGainedSinceReset : -1L;
   }

   private String getTimeTillLevel(XpGoalTimeType goalTimeType) {
      long remainingSeconds = this.getSecondsTillLevel();
      if (remainingSeconds < 0L) {
         return "∞";
      } else {
         long durationDays = remainingSeconds / 86400L;
         long durationHours = remainingSeconds % 86400L / 3600L;
         long durationHoursTotal = remainingSeconds / 3600L;
         long durationMinutes = remainingSeconds % 3600L / 60L;
         long durationSeconds = remainingSeconds % 60L;
         switch (goalTimeType) {
            case DAYS:
               if (durationDays > 1L) {
                  return String.format("%d days %02d:%02d:%02d", durationDays, durationHours, durationMinutes, durationSeconds);
               } else if (durationDays == 1L) {
                  return String.format("1 day %02d:%02d:%02d", durationHours, durationMinutes, durationSeconds);
               }
            case HOURS:
               if (durationHoursTotal > 1L) {
                  return String.format("%d hours %02d:%02d", durationHoursTotal, durationMinutes, durationSeconds);
               } else if (durationHoursTotal == 1L) {
                  return String.format("1 hour %02d:%02d", durationMinutes, durationSeconds);
               }
            case SHORT:
            default:
               return durationHoursTotal > 0L ? String.format("%d:%02d:%02d", durationHoursTotal, durationMinutes, durationSeconds) : String.format("%02d:%02d", durationMinutes, durationSeconds);
         }
      }
   }

   int getXpHr() {
      return this.toHourly(this.xpGainedSinceReset);
   }

   void resetPerHour() {
      this.actionsSinceReset = 0;
      this.xpGainedBeforeReset += this.xpGainedSinceReset;
      this.xpGainedSinceReset = 0;
      this.lastChangeMillis = System.currentTimeMillis();
      this.setSkillTime(0L);
   }

   boolean update(long currentXp) {
      if (this.startXp == -1L) {
         log.warn("Attempted to update skill state {} but was not initialized with current xp", this);
         return false;
      } else {
         long originalXp = (long)this.getTotalXpGained() + this.startXp;
         int actionExp = (int)(currentXp - originalXp);
         if (actionExp == 0) {
            return false;
         } else {
            if (this.actionsHistoryInitialized) {
               this.actionExps[this.actionExpIndex] = actionExp;
            } else {
               Arrays.fill(this.actionExps, actionExp);
               this.actionsHistoryInitialized = true;
            }

            this.actionExpIndex = (this.actionExpIndex + 1) % this.actionExps.length;
            ++this.actions;
            ++this.actionsSinceReset;
            this.xpGainedSinceReset = (int)(currentXp - (this.startXp + (long)this.xpGainedBeforeReset));
            this.lastChangeMillis = System.currentTimeMillis();
            return true;
         }
      }
   }

   void updateGoals(long currentXp, int goalStartXp, int goalEndXp) {
      if (goalStartXp >= 0 && currentXp <= (long)goalEndXp) {
         this.startLevelExp = goalStartXp;
      } else {
         this.startLevelExp = Experience.getXpForLevel(Experience.getLevelForXp((int)currentXp));
      }

      if (goalEndXp > 0 && currentXp <= (long)goalEndXp) {
         this.endLevelExp = goalEndXp;
      } else {
         int currentLevel = Experience.getLevelForXp((int)currentXp);
         this.endLevelExp = currentLevel + 1 <= 126 ? Experience.getXpForLevel(currentLevel + 1) : 200000000;
      }

   }

   public void tick(long delta) {
      if (this.xpGainedSinceReset > 0) {
         this.skillTime += delta;
      }
   }

   XpSnapshotSingle snapshot() {
      return XpSnapshotSingle.builder().startLevel(Experience.getLevelForXp(this.startLevelExp)).endLevel(Experience.getLevelForXp(this.endLevelExp)).xpGainedInSession(this.getTotalXpGained()).xpRemainingToGoal(this.getXpRemaining()).xpPerHour(this.getXpHr()).skillProgressToGoal(this.getSkillProgress()).actionsInSession(this.actions).actionsRemainingToGoal(this.getActionsRemaining()).actionsPerHour(this.getActionsHr()).timeTillGoal(this.getTimeTillLevel(XpGoalTimeType.DAYS)).timeTillGoalHours(this.getTimeTillLevel(XpGoalTimeType.HOURS)).timeTillGoalShort(this.getTimeTillLevel(XpGoalTimeType.SHORT)).startGoalXp(this.startLevelExp).endGoalXp(this.endLevelExp).compactView(this.compactView).build();
   }

   XpSaveSingle save() {
      XpSaveSingle save = new XpSaveSingle();
      save.startXp = this.startXp;
      save.xpGainedBeforeReset = this.xpGainedBeforeReset;
      save.xpGainedSinceReset = this.xpGainedSinceReset;
      save.time = this.skillTime;
      return save;
   }

   void restore(XpSaveSingle save) {
      this.startXp = save.startXp;
      this.xpGainedBeforeReset = save.xpGainedBeforeReset;
      this.xpGainedSinceReset = save.xpGainedSinceReset;
      this.skillTime = save.time;
   }

   public long getStartXp() {
      return this.startXp;
   }

   public void setStartXp(long startXp) {
      this.startXp = startXp;
   }

   public int getXpGainedSinceReset() {
      return this.xpGainedSinceReset;
   }

   public void setSkillTime(long skillTime) {
      this.skillTime = skillTime;
   }

   public long getLastChangeMillis() {
      return this.lastChangeMillis;
   }

   public boolean isCompactView() {
      return this.compactView;
   }

   public void setCompactView(boolean compactView) {
      this.compactView = compactView;
   }
}
