package net.runelite.api.events;

import net.runelite.api.Skill;

public final class FakeXpDrop {
   private final Skill skill;
   private final int xp;

   public FakeXpDrop(Skill skill, int xp) {
      this.skill = skill;
      this.xp = xp;
   }

   public Skill getSkill() {
      return this.skill;
   }

   public int getXp() {
      return this.xp;
   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof FakeXpDrop)) {
         return false;
      } else {
         FakeXpDrop other = (FakeXpDrop)o;
         if (this.getXp() != other.getXp()) {
            return false;
         } else {
            Object this$skill = this.getSkill();
            Object other$skill = other.getSkill();
            if (this$skill == null) {
               if (other$skill != null) {
                  return false;
               }
            } else if (!this$skill.equals(other$skill)) {
               return false;
            }

            return true;
         }
      }
   }

   public int hashCode() {
      int PRIME = true;
      int result = 1;
      result = result * 59 + this.getXp();
      Object $skill = this.getSkill();
      result = result * 59 + ($skill == null ? 43 : $skill.hashCode());
      return result;
   }

   public String toString() {
      String var10000 = String.valueOf(this.getSkill());
      return "FakeXpDrop(skill=" + var10000 + ", xp=" + this.getXp() + ")";
   }
}
