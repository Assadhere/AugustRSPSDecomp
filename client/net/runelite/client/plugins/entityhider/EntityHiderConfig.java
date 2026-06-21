package net.runelite.client.plugins.entityhider;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.Range;

@ConfigGroup("entityhider")
public interface EntityHiderConfig extends Config {
   String GROUP = "entityhider";

   @ConfigItem(
      position = 1,
      keyName = "hidePlayers",
      name = "Hide others",
      description = "Configures whether or not other players are hidden."
   )
   default boolean hideOthers() {
      return false;
   }

   @ConfigItem(
      position = 2,
      keyName = "hidePlayers2D",
      name = "Hide others' 2D",
      description = "Configures whether or not other players' 2D elements are hidden."
   )
   default boolean hideOthers2D() {
      return false;
   }

   @ConfigItem(
      position = 3,
      keyName = "hidePartyMembers",
      name = "Hide party members",
      description = "Configures whether or not party members are hidden."
   )
   default boolean hidePartyMembers() {
      return false;
   }

   @ConfigItem(
      position = 4,
      keyName = "hideFriends",
      name = "Hide friends",
      description = "Configures whether or not friends are hidden."
   )
   default boolean hideFriends() {
      return false;
   }

   @ConfigItem(
      position = 5,
      keyName = "hideClanMates",
      name = "Hide friends chat members",
      description = "Configures whether or not friends chat members are hidden."
   )
   default boolean hideFriendsChatMembers() {
      return false;
   }

   @ConfigItem(
      position = 6,
      keyName = "hideClanChatMembers",
      name = "Hide clan chat members",
      description = "Configures whether or not clan chat members are hidden."
   )
   default boolean hideClanChatMembers() {
      return false;
   }

   @ConfigItem(
      position = 7,
      keyName = "hideIgnores",
      name = "Hide ignores",
      description = "Configures whether or not ignored players are hidden."
   )
   default boolean hideIgnores() {
      return false;
   }

   @ConfigItem(
      position = 8,
      keyName = "hideLocalPlayer",
      name = "Hide local player",
      description = "Configures whether or not the local player is hidden."
   )
   default boolean hideLocalPlayer() {
      return false;
   }

   @ConfigItem(
      position = 9,
      keyName = "hideLocalPlayer2D",
      name = "Hide local player 2D",
      description = "Configures whether or not the local player's 2D elements are hidden."
   )
   default boolean hideLocalPlayer2D() {
      return false;
   }

   @ConfigItem(
      position = 10,
      keyName = "hideNPCs",
      name = "Hide NPCs",
      description = "Configures whether or not NPCs are hidden."
   )
   default boolean hideNPCs() {
      return false;
   }

   @ConfigItem(
      position = 11,
      keyName = "hideNPCs2D",
      name = "Hide NPCs 2D",
      description = "Configures whether or not NPCs 2D elements are hidden."
   )
   default boolean hideNPCs2D() {
      return false;
   }

   @ConfigItem(
      position = 12,
      keyName = "hideWorldEntities",
      name = "Hide boats",
      description = "Configures whether boats are hidden."
   )
   default boolean hideWorldEntities() {
      return false;
   }

   @ConfigItem(
      position = 20,
      keyName = "hidePets",
      name = "Hide others' pets",
      description = "Configures whether or not other players' pets are hidden."
   )
   default boolean hidePets() {
      return false;
   }

   @ConfigItem(
      position = 21,
      keyName = "hideAttackers",
      name = "Hide attackers",
      description = "Configures whether or not NPCs/players attacking you are hidden."
   )
   default boolean hideAttackers() {
      return false;
   }

   @ConfigItem(
      position = 22,
      keyName = "hideProjectiles",
      name = "Hide projectiles",
      description = "Configures whether or not projectiles are hidden."
   )
   default boolean hideProjectiles() {
      return false;
   }

   @ConfigItem(
      position = 23,
      keyName = "hideDeadNpcs",
      name = "Hide dead NPCs",
      description = "Hides NPCs when their health reaches 0."
   )
   default boolean hideDeadNpcs() {
      return false;
   }

   @ConfigItem(
      position = 24,
      keyName = "hideThralls",
      name = "Hide thralls",
      description = "Configures whether or not thralls are hidden."
   )
   default boolean hideThralls() {
      return false;
   }

   @ConfigItem(
      position = 25,
      keyName = "hideRandomEvents",
      name = "Hide others' random events",
      description = "Configures whether or not other players' random events are hidden."
   )
   default boolean hideRandomEvents() {
      return false;
   }

   @ConfigItem(
      position = 30,
      keyName = "softHideServerNpcs",
      name = "Soft-hide server-listed NPCs",
      description = "Fade NPCs the August server flags as soft-hidden (e.g. all pets) and strip their right-click options. You can still see them, but they don't get in the way."
   )
   default boolean softHideServerNpcs() {
      return true;
   }

   @Range(
      min = 1,
      max = 100
   )
   @ConfigItem(
      position = 31,
      keyName = "softHideOpacityPercent",
      name = "Soft-hide opacity %",
      description = "Opacity (in percent) used when rendering soft-hidden NPCs. Lower = more transparent."
   )
   default int softHideOpacityPercent() {
      return 25;
   }
}
