package net.runelite.client.plugins.cluescrolls.clues.item;

import net.runelite.api.Client;
import net.runelite.api.Item;

public interface ItemRequirement {
   boolean fulfilledBy(int var1);

   boolean fulfilledBy(Item[] var1);

   String getCollectiveName(Client var1);
}
