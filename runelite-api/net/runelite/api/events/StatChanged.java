package net.runelite.api.events;

import net.runelite.api.Skill;

public final class StatChanged {
   private final Skill skill;
   private final int xp;
   private final int level;
   private final int boostedLevel;

   public StatChanged(Skill skill, int xp, int level, int boostedLevel) {
      this.skill = skill;
      this.xp = xp;
      this.level = level;
      this.boostedLevel = boostedLevel;
   }

   public Skill getSkill() {
      return this.skill;
   }

   public int getXp() {
      return this.xp;
   }

   public int getLevel() {
      return this.level;
   }

   public int getBoostedLevel() {
      return this.boostedLevel;
   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof StatChanged)) {
         return false;
      } else {
         StatChanged other = (StatChanged)o;
         if (this.getXp() != other.getXp()) {
            return false;
         } else if (this.getLevel() != other.getLevel()) {
            return false;
         } else if (this.getBoostedLevel() != other.getBoostedLevel()) {
            return false;
         } else {
            Object this$skill = this.getSkill();
            Object other$skill = other.getSkill();
            if (this$skill == null) {
               if (other$skill == null) {
                  return true;
               }
            } else if (this$skill.equals(other$skill)) {
               return true;
            }

            return false;
         }
      }
   }

   public int hashCode() {
      int PRIME = true;
      int result = 1;
      result = result * 59 + this.getXp();
      result = result * 59 + this.getLevel();
      result = result * 59 + this.getBoostedLevel();
      Object $skill = this.getSkill();
      result = result * 59 + ($skill == null ? 43 : $skill.hashCode());
      return result;
   }

   public String toString() {
      String var10000 = String.valueOf(this.getSkill());
      return "StatChanged(skill=" + var10000 + ", xp=" + this.getXp() + ", level=" + this.getLevel() + ", boostedLevel=" + this.getBoostedLevel() + ")";
   }
}
