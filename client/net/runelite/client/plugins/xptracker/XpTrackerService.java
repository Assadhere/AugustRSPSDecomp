package net.runelite.client.plugins.xptracker;

import net.runelite.api.Skill;

public interface XpTrackerService {
   int getActions(Skill var1);

   int getActionsHr(Skill var1);

   int getActionsLeft(Skill var1);

   int getXpHr(Skill var1);

   int getStartGoalXp(Skill var1);

   int getEndGoalXp(Skill var1);

   String getTimeTilGoal(Skill var1);
}
