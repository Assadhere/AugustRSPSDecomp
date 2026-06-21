package net.runelite.client.plugins.skillcalculator.skills;

import java.util.Collections;
import java.util.Set;
import net.runelite.client.game.ItemManager;

public interface SkillAction {
   String getName(ItemManager var1);

   int getLevel();

   float getXp();

   default int getIcon() {
      return -1;
   }

   default int getSprite() {
      return -1;
   }

   default boolean isBonusApplicable(SkillBonus bonus) {
      return !this.getExcludedSkillBonuses().contains(bonus);
   }

   boolean isMembers(ItemManager var1);

   default Set<? extends SkillBonus> getExcludedSkillBonuses() {
      return Collections.emptySet();
   }
}
