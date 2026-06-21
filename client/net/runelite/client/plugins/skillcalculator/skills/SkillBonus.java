package net.runelite.client.plugins.skillcalculator.skills;

import java.util.Collections;
import java.util.Set;

public interface SkillBonus {
   String getName();

   float getValue();

   default Set<? extends SkillBonus> getCanBeStackedWith() {
      return Collections.emptySet();
   }
}
