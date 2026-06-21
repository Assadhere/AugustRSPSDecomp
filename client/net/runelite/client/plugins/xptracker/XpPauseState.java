package net.runelite.client.plugins.xptracker;

import java.util.EnumMap;
import java.util.Map;
import net.runelite.api.Skill;

class XpPauseState {
   private final Map<Skill, XpPauseStateSingle> skillPauses = new EnumMap(Skill.class);
   private final XpPauseStateSingle overall = new XpPauseStateSingle();
   private boolean prevIsLoggedIn = false;

   boolean pauseSkill(Skill skill) {
      return this.findPauseState(skill).manualPause();
   }

   boolean pauseOverall() {
      return this.overall.manualPause();
   }

   boolean unpauseSkill(Skill skill) {
      return this.findPauseState(skill).unpause();
   }

   boolean unpauseOverall() {
      return this.overall.unpause();
   }

   boolean isPaused(Skill skill) {
      return this.findPauseState(skill).isPaused();
   }

   boolean isOverallPaused() {
      return this.overall.isPaused();
   }

   void tickXp(Skill skill, long currentXp, int pauseAfterMinutes) {
      XpPauseStateSingle state = this.findPauseState(skill);
      this.tick(state, currentXp, pauseAfterMinutes);
   }

   void tickOverall(long currentXp, int pauseAfterMinutes) {
      this.tick(this.overall, currentXp, pauseAfterMinutes);
   }

   private void tick(XpPauseStateSingle state, long currentXp, int pauseAfterMinutes) {
      if (state.getXp() != currentXp) {
         state.xpChanged(currentXp);
      } else if (pauseAfterMinutes > 0) {
         long now = System.currentTimeMillis();
         int pauseAfterMillis = pauseAfterMinutes * 60 * 1000;
         long lastChangeMillis = state.getLastChangeMillis();
         if (lastChangeMillis != 0L && now - lastChangeMillis >= (long)pauseAfterMillis) {
            state.timeout();
         }
      }

   }

   void tickLogout(boolean pauseOnLogout, boolean loggedIn) {
      Skill[] var3;
      int var4;
      int var5;
      Skill skill;
      if (!this.prevIsLoggedIn && loggedIn) {
         this.prevIsLoggedIn = true;
         var3 = Skill.values();
         var4 = var3.length;

         for(var5 = 0; var5 < var4; ++var5) {
            skill = var3[var5];
            this.findPauseState(skill).login();
         }

         this.overall.login();
      } else if (this.prevIsLoggedIn && !loggedIn) {
         this.prevIsLoggedIn = false;
         if (pauseOnLogout) {
            var3 = Skill.values();
            var4 = var3.length;

            for(var5 = 0; var5 < var4; ++var5) {
               skill = var3[var5];
               this.findPauseState(skill).logout();
            }

            this.overall.logout();
         }
      }

   }

   private XpPauseStateSingle findPauseState(Skill skill) {
      return (XpPauseStateSingle)this.skillPauses.computeIfAbsent(skill, (s) -> {
         return new XpPauseStateSingle();
      });
   }
}
