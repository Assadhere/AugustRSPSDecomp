package net.runelite.client.plugins.skillcalculator.skills;

import net.runelite.client.game.ItemManager;

public interface ItemSkillAction extends SkillAction {
   int getItemId();

   default int getIcon() {
      return this.getItemId();
   }

   default String getName(ItemManager itemManager) {
      return itemManager.getItemComposition(this.getItemId()).getMembersName();
   }

   default boolean isMembers(ItemManager itemManager) {
      return itemManager.getItemComposition(this.getItemId()).isMembers();
   }
}
