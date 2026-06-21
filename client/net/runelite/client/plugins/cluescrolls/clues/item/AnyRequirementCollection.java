package net.runelite.client.plugins.cluescrolls.clues.item;

import net.runelite.api.Client;
import net.runelite.api.Item;

public class AnyRequirementCollection implements ItemRequirement {
   private final String name;
   private final ItemRequirement[] requirements;

   public AnyRequirementCollection(String name, ItemRequirement... requirements) {
      this.name = name;
      this.requirements = requirements;
   }

   public boolean fulfilledBy(int itemId) {
      ItemRequirement[] var2 = this.requirements;
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         ItemRequirement requirement = var2[var4];
         if (requirement.fulfilledBy(itemId)) {
            return true;
         }
      }

      return false;
   }

   public boolean fulfilledBy(Item[] items) {
      ItemRequirement[] var2 = this.requirements;
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         ItemRequirement requirement = var2[var4];
         if (requirement.fulfilledBy(items)) {
            return true;
         }
      }

      return false;
   }

   public String getCollectiveName(Client client) {
      return this.name;
   }
}
