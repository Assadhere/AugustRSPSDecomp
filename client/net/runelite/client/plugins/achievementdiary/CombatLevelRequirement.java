package net.runelite.client.plugins.achievementdiary;

import net.runelite.api.Client;

public class CombatLevelRequirement implements Requirement {
   private final int level;

   public String toString() {
      return this.level + " Combat";
   }

   public boolean satisfiesRequirement(Client client) {
      return client.getLocalPlayer() != null && client.getLocalPlayer().getCombatLevel() >= this.level;
   }

   public CombatLevelRequirement(int level) {
      this.level = level;
   }

   public int getLevel() {
      return this.level;
   }
}
