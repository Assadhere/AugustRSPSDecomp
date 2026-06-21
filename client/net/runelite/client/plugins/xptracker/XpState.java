package net.runelite.client.plugins.xptracker;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import lombok.NonNull;
import net.runelite.api.Skill;

class XpState {
   private final Map<Skill, XpStateSingle> xpSkills = new EnumMap(Skill.class);
   private final List<Skill> order = new ArrayList(Skill.values().length);
   private XpStateSingle overall = new XpStateSingle(-1L);
   @Inject
   private XpTrackerConfig xpTrackerConfig;

   void reset() {
      this.xpSkills.clear();
      this.order.clear();
      this.overall = new XpStateSingle(-1L);
   }

   void resetSkillPerHour(Skill skill) {
      ((XpStateSingle)this.xpSkills.get(skill)).resetPerHour();
   }

   void resetOverallPerHour() {
      this.overall.resetPerHour();
   }

   XpUpdateResult updateSkill(Skill skill, long currentXp, int goalStartXp, int goalEndXp) {
      XpStateSingle state = (XpStateSingle)this.xpSkills.get(skill);
      if (state != null && state.getStartXp() != -1L) {
         long startXp = state.getStartXp();
         int gainedXp = state.getTotalXpGained();
         if (startXp + (long)gainedXp > currentXp) {
            this.initializeSkill(skill, currentXp);
            return XpUpdateResult.INITIALIZED;
         } else if (!state.update(currentXp)) {
            return XpUpdateResult.NO_CHANGE;
         } else {
            state.updateGoals(currentXp, goalStartXp, goalEndXp);
            this.updateOrder(skill);
            return XpUpdateResult.UPDATED;
         }
      } else {
         assert currentXp >= 0L;

         this.initializeSkill(skill, currentXp);
         return XpUpdateResult.INITIALIZED;
      }
   }

   void updateOverall(long currentXp) {
      if (this.overall != null && this.overall.getStartXp() + (long)this.overall.getTotalXpGained() <= currentXp) {
         this.overall.update(currentXp);
      } else {
         this.overall = new XpStateSingle(currentXp);
      }

   }

   void tick(Skill skill, long delta) {
      XpStateSingle state = this.getSkill(skill);
      this.tick(state, delta);
   }

   void tickOverall(long delta) {
      this.tick(this.overall, delta);
   }

   private void tick(XpStateSingle state, long delta) {
      state.tick(delta);
      int resetAfterMinutes = this.xpTrackerConfig.resetSkillRateAfter();
      if (resetAfterMinutes > 0) {
         long now = System.currentTimeMillis();
         int resetAfterMillis = resetAfterMinutes * 60 * 1000;
         long lastChangeMillis = state.getLastChangeMillis();
         if (lastChangeMillis != 0L && now - lastChangeMillis >= (long)resetAfterMillis) {
            state.resetPerHour();
         }
      }

   }

   void initializeSkill(Skill skill, long currentXp) {
      this.xpSkills.put(skill, new XpStateSingle(currentXp));
   }

   void initializeOverall(long currentXp) {
      this.overall = new XpStateSingle(currentXp);
   }

   boolean isInitialized(Skill skill) {
      XpStateSingle xpStateSingle = (XpStateSingle)this.xpSkills.get(skill);
      return xpStateSingle != null && xpStateSingle.getStartXp() != -1L;
   }

   boolean isOverallInitialized() {
      return this.overall.getStartXp() != -1L;
   }

   @NonNull XpStateSingle getSkill(Skill skill) {
      return (XpStateSingle)this.xpSkills.computeIfAbsent(skill, (s) -> {
         return new XpStateSingle(-1L);
      });
   }

   @NonNull XpSnapshotSingle getSkillSnapshot(Skill skill) {
      return this.getSkill(skill).snapshot();
   }

   @NonNull XpSnapshotSingle getTotalSnapshot() {
      return this.overall.snapshot();
   }

   void setCompactView(Skill skill, boolean compactView) {
      ((XpStateSingle)this.xpSkills.get(skill)).setCompactView(compactView);
   }

   void setOrder(Skill skill, int newPosition) {
      int oldPosition = this.order.indexOf(skill);
      if (oldPosition != newPosition) {
         this.order.remove(oldPosition);
         this.order.add(newPosition, skill);
      }

   }

   private void updateOrder(Skill skill) {
      if (this.xpTrackerConfig.prioritizeRecentXpSkills()) {
         int idx = this.order.indexOf(skill);
         if (idx != 0) {
            this.order.remove(skill);
            this.order.add(0, skill);
         }
      } else if (!this.order.contains(skill)) {
         this.order.add(skill);
      }

   }

   XpSave save() {
      if (this.overall.getStartXp() == -1L) {
         return null;
      } else {
         XpSave save = new XpSave();
         Iterator var2 = this.order.iterator();

         while(var2.hasNext()) {
            Skill skill = (Skill)var2.next();
            XpStateSingle state = (XpStateSingle)this.xpSkills.get(skill);
            if (state.getTotalXpGained() > 0) {
               save.skills.put(skill, state.save());
            }

            if (state.isCompactView()) {
               save.compactViewSkills.add(skill);
            }
         }

         save.overall = this.overall.save();
         return save;
      }
   }

   void restore(XpSave save) {
      this.reset();
      Iterator var2 = save.skills.entrySet().iterator();

      while(var2.hasNext()) {
         Map.Entry<Skill, XpSaveSingle> entry = (Map.Entry)var2.next();
         Skill skill = (Skill)entry.getKey();
         XpSaveSingle s = (XpSaveSingle)entry.getValue();
         XpStateSingle state = new XpStateSingle(s.startXp);
         state.restore(s);
         this.xpSkills.put(skill, state);
         this.order.add(skill);
      }

      var2 = save.compactViewSkills.iterator();

      while(var2.hasNext()) {
         Skill skill = (Skill)var2.next();
         XpStateSingle state = (XpStateSingle)this.xpSkills.get(skill);
         if (state != null) {
            state.setCompactView(true);
         }
      }

      this.overall.restore(save.overall);
   }
}
