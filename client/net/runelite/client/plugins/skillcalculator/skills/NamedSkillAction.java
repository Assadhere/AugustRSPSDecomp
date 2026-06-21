package net.runelite.client.plugins.skillcalculator.skills;

import net.runelite.client.game.ItemManager;

public interface NamedSkillAction extends SkillAction {
   String getName();

   default String getName(ItemManager itemManager) {
      return this.getName();
   }
}
