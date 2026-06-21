package net.runelite.api.clan;

import net.runelite.api.ChatPlayer;

public interface ClanChannelMember extends ChatPlayer {
   String getName();

   ClanRank getRank();

   int getWorld();
}
