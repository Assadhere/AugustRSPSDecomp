package net.runelite.client.plugins.attackstyles;

import net.runelite.api.Skill;

enum AttackStyle {
   ACCURATE("Accurate", new Skill[]{Skill.ATTACK}),
   AGGRESSIVE("Aggressive", new Skill[]{Skill.STRENGTH}),
   DEFENSIVE("Defensive", new Skill[]{Skill.DEFENCE}),
   CONTROLLED("Controlled", new Skill[]{Skill.ATTACK, Skill.STRENGTH, Skill.DEFENCE}),
   RANGING("Ranging", new Skill[]{Skill.RANGED}),
   LONGRANGE("Longrange", new Skill[]{Skill.RANGED, Skill.DEFENCE}),
   CASTING("Casting", new Skill[]{Skill.MAGIC}),
   DEFENSIVE_CASTING("Defensive Casting", new Skill[]{Skill.MAGIC, Skill.DEFENCE}),
   OTHER("Other", new Skill[0]);

   private final String name;
   private final Skill[] skills;

   private AttackStyle(String name, Skill... skills) {
      this.name = name;
      this.skills = skills;
   }

   public String getName() {
      return this.name;
   }

   public Skill[] getSkills() {
      return this.skills;
   }
}
