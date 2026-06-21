package net.runelite.client.plugins.achievementdiary;

import net.runelite.api.Client;

public class QuestPointRequirement implements Requirement {
   private final int qp;

   public String toString() {
      return this.qp + " Quest points";
   }

   public boolean satisfiesRequirement(Client client) {
      return client.getVarpValue(101) >= this.qp;
   }

   public QuestPointRequirement(int qp) {
      this.qp = qp;
   }

   public int getQp() {
      return this.qp;
   }
}
