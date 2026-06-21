package net.runelite.api.clan;

import java.util.List;
import javax.annotation.Nullable;

public interface ClanSettings {
   String getName();

   List<ClanMember> getMembers();

   @Nullable
   ClanMember findMember(String var1);

   @Nullable
   ClanTitle titleForRank(ClanRank var1);
}
