package net.runelite.api;

public interface Player extends Actor {
   int getId();

   int getCombatLevel();

   String[] minimenuStrings();

   PlayerComposition getPlayerComposition();

   int getTeam();

   boolean isFriendsChatMember();

   boolean isFriend();

   boolean isClanMember();

   HeadIcon getOverheadIcon();

   int getSkullIcon();

   void setSkullIcon(int var1);
}
