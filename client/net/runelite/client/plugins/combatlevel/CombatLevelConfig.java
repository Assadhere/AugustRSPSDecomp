package net.runelite.client.plugins.combatlevel;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("combatlevel")
public interface CombatLevelConfig extends Config {
   @ConfigItem(
      keyName = "showLevelsUntil",
      name = "Calculate next level",
      description = "Mouse over the combat level to calculate what skill levels will increase combat."
   )
   default boolean showLevelsUntil() {
      return true;
   }

   @ConfigItem(
      keyName = "showPreciseCombatLevel",
      name = "Show precise combat level",
      description = "Displays your combat level with accurate decimals."
   )
   default boolean showPreciseCombatLevel() {
      return true;
   }

   @ConfigItem(
      keyName = "wildernessAttackLevelRange",
      name = "Show level range in wilderness",
      description = "Displays a PvP-world-like attack level range in the wilderness."
   )
   default boolean wildernessAttackLevelRange() {
      return true;
   }
}
