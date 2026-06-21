package net.runelite.api.clan;

import java.time.LocalDate;

public interface ClanMember {
   String getName();

   ClanRank getRank();

   LocalDate getJoinDate();
}
