package net.runelite.api.clan;

import java.util.List;
import javax.annotation.Nullable;

public interface ClanChannel {
   String getName();

   List<ClanChannelMember> getMembers();

   @Nullable
   ClanChannelMember findMember(String var1);
}
