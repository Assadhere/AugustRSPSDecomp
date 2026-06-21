package net.runelite.client.plugins.puzzlesolver.lightbox;

public class LightboxSolution {
   private int solution;

   public void flip(Combination c) {
      this.solution ^= 1 << c.ordinal();
   }

   public int numMoves() {
      int count = 0;
      int cur = this.solution;

      for(int i = 0; i < Combination.values().length; ++i) {
         count += cur & 1;
         cur >>= 1;
      }

      return count;
   }

   public String toString() {
      StringBuilder stringBuilder = new StringBuilder();
      Combination[] var2 = Combination.values();
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         Combination combination = var2[var4];
         if ((this.solution >>> combination.ordinal() & 1) != 0) {
            stringBuilder.append(combination.name());
         }
      }

      return stringBuilder.toString();
   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof LightboxSolution)) {
         return false;
      } else {
         LightboxSolution other = (LightboxSolution)o;
         if (!other.canEqual(this)) {
            return false;
         } else {
            return this.solution == other.solution;
         }
      }
   }

   protected boolean canEqual(Object other) {
      return other instanceof LightboxSolution;
   }

   public int hashCode() {
      int PRIME = true;
      int result = 1;
      result = result * 59 + this.solution;
      return result;
   }

   public LightboxSolution() {
   }

   public LightboxSolution(int solution) {
      this.solution = solution;
   }
}
