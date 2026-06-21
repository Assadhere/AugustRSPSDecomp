package net.runelite.client.plugins.playerindicators;

import java.awt.Color;
import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.ConfigSection;

@ConfigGroup("playerindicators")
public interface PlayerIndicatorsConfig extends Config {
   String GROUP = "playerindicators";
   @ConfigSection(
      name = "Highlight options",
      description = "Toggle highlighted players by type (self, friends, etc.) and choose their highlight colors.",
      position = 99
   )
   String highlightSection = "section";

   @ConfigItem(
      position = 0,
      keyName = "highlightSelf",
      name = "Highlight own player",
      description = "Configures whether your own player should be highlighted.",
      section = "section"
   )
   default HighlightSetting highlightOwnPlayer() {
      return PlayerIndicatorsConfig.HighlightSetting.DISABLED;
   }

   @ConfigItem(
      position = 1,
      keyName = "ownNameColor",
      name = "Own player",
      description = "Color of your own player.",
      section = "section"
   )
   default Color getOwnPlayerColor() {
      return new Color(0, 184, 212);
   }

   @ConfigItem(
      position = 2,
      keyName = "highlightPartyMembers",
      name = "Highlight party members",
      description = "Configures whether party members should be highlighted.",
      section = "section"
   )
   default HighlightSetting highlightPartyMembers() {
      return PlayerIndicatorsConfig.HighlightSetting.ENABLED;
   }

   @ConfigItem(
      position = 3,
      keyName = "partyMemberNameColor",
      name = "Party member",
      description = "Color of party member names.",
      section = "section"
   )
   default Color getPartyMemberColor() {
      return new Color(234, 123, 91);
   }

   @ConfigItem(
      position = 4,
      keyName = "highlightFriends",
      name = "Highlight friends",
      description = "Configures whether friends should be highlighted.",
      section = "section"
   )
   default HighlightSetting highlightFriends() {
      return PlayerIndicatorsConfig.HighlightSetting.ENABLED;
   }

   @ConfigItem(
      position = 5,
      keyName = "friendNameColor",
      name = "Friend",
      description = "Color of friend names.",
      section = "section"
   )
   default Color getFriendColor() {
      return new Color(0, 200, 83);
   }

   @ConfigItem(
      position = 6,
      keyName = "highlightFriendsChat",
      name = "Highlight friends chat members",
      description = "Configures whether friends chat members should be highlighted.",
      section = "section"
   )
   default HighlightSetting highlightFriendsChat() {
      return PlayerIndicatorsConfig.HighlightSetting.ENABLED;
   }

   @ConfigItem(
      position = 7,
      keyName = "clanMemberColor",
      name = "Friends chat",
      description = "Color of friends chat members.",
      section = "section"
   )
   default Color getFriendsChatMemberColor() {
      return new Color(170, 0, 255);
   }

   @ConfigItem(
      position = 8,
      keyName = "highlightTeamMembers",
      name = "Highlight team members",
      description = "Configures whether team members should be highlighted.",
      section = "section"
   )
   default HighlightSetting highlightTeamMembers() {
      return PlayerIndicatorsConfig.HighlightSetting.ENABLED;
   }

   @ConfigItem(
      position = 9,
      keyName = "teamMemberColor",
      name = "Team member",
      description = "Color of team members.",
      section = "section"
   )
   default Color getTeamMemberColor() {
      return new Color(19, 110, 247);
   }

   @ConfigItem(
      position = 10,
      keyName = "highlightClanMembers",
      name = "Highlight clan members",
      description = "Configures whether clan members should be highlighted.",
      section = "section"
   )
   default HighlightSetting highlightClanMembers() {
      return PlayerIndicatorsConfig.HighlightSetting.ENABLED;
   }

   @ConfigItem(
      position = 11,
      keyName = "clanChatMemberColor",
      name = "Clan member",
      description = "Color of clan members.",
      section = "section"
   )
   default Color getClanMemberColor() {
      return new Color(36, 15, 171);
   }

   @ConfigItem(
      position = 12,
      keyName = "highlightOthers",
      name = "Highlight others",
      description = "Configures whether other players should be highlighted.",
      section = "section"
   )
   default HighlightSetting highlightOthers() {
      return PlayerIndicatorsConfig.HighlightSetting.DISABLED;
   }

   @ConfigItem(
      position = 13,
      keyName = "nonClanMemberColor",
      name = "Others",
      description = "Color of other players names.",
      section = "section"
   )
   default Color getOthersColor() {
      return Color.RED;
   }

   @ConfigItem(
      position = 10,
      keyName = "drawPlayerTiles",
      name = "Draw tiles under players",
      description = "Configures whether or not tiles under highlighted players should be drawn."
   )
   default boolean drawTiles() {
      return false;
   }

   @ConfigItem(
      position = 11,
      keyName = "playerNamePosition",
      name = "Name position",
      description = "Configures the position of drawn player names, or if they should be disabled."
   )
   default PlayerNameLocation playerNamePosition() {
      return PlayerNameLocation.ABOVE_HEAD;
   }

   @ConfigItem(
      position = 12,
      keyName = "drawMinimapNames",
      name = "Draw names on minimap",
      description = "Configures whether or not minimap names for players with rendered names should be drawn."
   )
   default boolean drawMinimapNames() {
      return false;
   }

   @ConfigItem(
      position = 13,
      keyName = "colorPlayerMenu",
      name = "Colorize player menu",
      description = "Color right-click menu for players."
   )
   default boolean colorPlayerMenu() {
      return true;
   }

   @ConfigItem(
      position = 14,
      keyName = "clanMenuIcons",
      name = "Show friends chat ranks",
      description = "Add friends chat rank to right-click menu and next to player names."
   )
   default boolean showFriendsChatRanks() {
      return true;
   }

   @ConfigItem(
      position = 15,
      keyName = "clanchatMenuIcons",
      name = "Show clan chat ranks",
      description = "Add clan chat rank to right-click menu and next to player names."
   )
   default boolean showClanChatRanks() {
      return true;
   }

   public static enum HighlightSetting {
      DISABLED,
      ENABLED,
      PVP;
   }
}
