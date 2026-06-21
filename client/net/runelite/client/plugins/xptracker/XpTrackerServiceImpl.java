package net.runelite.client.plugins.xptracker;

import javax.inject.Inject;
import javax.inject.Singleton;
import net.runelite.api.Skill;

@Singleton
class XpTrackerServiceImpl implements XpTrackerService {
   private final XpTrackerPlugin plugin;

   @Inject
   XpTrackerServiceImpl(XpTrackerPlugin plugin) {
      this.plugin = plugin;
   }

   public int getActions(Skill skill) {
      return this.plugin.getSkillSnapshot(skill).getActionsInSession();
   }

   public int getActionsHr(Skill skill) {
      return this.plugin.getSkillSnapshot(skill).getActionsPerHour();
   }

   public int getActionsLeft(Skill skill) {
      return this.plugin.getSkillSnapshot(skill).getActionsRemainingToGoal();
   }

   public int getXpHr(Skill skill) {
      return this.plugin.getSkillSnapshot(skill).getXpPerHour();
   }

   public int getStartGoalXp(Skill skill) {
      return this.plugin.getSkillSnapshot(skill).getStartGoalXp();
   }

   public int getEndGoalXp(Skill skill) {
      return this.plugin.getSkillSnapshot(skill).getEndGoalXp();
   }

   public String getTimeTilGoal(Skill skill) {
      return this.plugin.getSkillSnapshot(skill).getTimeTillGoalShort();
   }
}
