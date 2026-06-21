package net.runelite.client.plugins.party;

import javax.inject.Inject;
import javax.inject.Singleton;
import net.runelite.client.plugins.party.data.PartyData;

@Singleton
public class PartyPluginServiceImpl implements PartyPluginService {
   private final PartyPlugin plugin;

   @Inject
   private PartyPluginServiceImpl(PartyPlugin plugin) {
      this.plugin = plugin;
   }

   public PartyData getPartyData(long memberId) {
      return this.plugin.getPartyData(memberId);
   }
}
