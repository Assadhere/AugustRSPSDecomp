package net.runelite.client.plugins.cluescrolls.clues.item;

import net.runelite.api.Client;
import net.runelite.api.Item;

public class AllRequirementsCollection implements ItemRequirement {
   private String name;
   private ItemRequirement[] requirements;

   public AllRequirementsCollection(String name, ItemRequirement... requirements) {
      this.name = name;
      this.requirements = requirements;
   }

   public AllRequirementsCollection(ItemRequirement... requirements) {
      this("N/A", requirements);
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
         if (!requirement.fulfilledBy(items)) {
            return false;
         }
      }

      return true;
   }

   public String getCollectiveName(Client client) {
      return this.name;
   }
}
