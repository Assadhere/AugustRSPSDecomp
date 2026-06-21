package net.runelite.client.plugins.achievementdiary;

import net.runelite.api.Client;
import net.runelite.api.Skill;

public class SkillRequirement implements Requirement {
   private final Skill skill;
   private final int level;

   public String toString() {
      int var10000 = this.level;
      return "" + var10000 + " " + this.skill.getName();
   }

   public boolean satisfiesRequirement(Client client) {
      return client.getRealSkillLevel(this.skill) >= this.level;
   }

   public SkillRequirement(Skill skill, int level) {
      this.skill = skill;
      this.level = level;
   }

   public Skill getSkill() {
      return this.skill;
   }

   public int getLevel() {
      return this.level;
   }
}
