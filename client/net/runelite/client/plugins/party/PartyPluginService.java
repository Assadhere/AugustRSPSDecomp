package net.runelite.client.plugins.party;

import javax.annotation.Nullable;
import net.runelite.client.plugins.party.data.PartyData;

public interface PartyPluginService {
   @Nullable
   PartyData getPartyData(long var1);
}
