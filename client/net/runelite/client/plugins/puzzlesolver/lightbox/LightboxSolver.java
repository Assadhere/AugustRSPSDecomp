package net.runelite.client.plugins.puzzlesolver.lightbox;

public class LightboxSolver {
   private LightboxState initial;
   private final LightboxState[] switches = new LightboxState[8];

   static boolean isBitSet(int num, int bit) {
      return (num >>> bit & 1) != 0;
   }

   private static boolean isSolved(LightboxState s) {
      for(int i = 0; i < 5; ++i) {
         for(int j = 0; j < 5; ++j) {
            if (!s.getState(i, j)) {
               return false;
            }
         }
      }

      return true;
   }

   public LightboxSolution solve() {
      LightboxSolution solution = null;

      label36:
      for(int i = 0; (double)i < Math.pow(2.0, 8.0); ++i) {
         LightboxState s = this.initial;

         for(int bit = 0; bit < 8; ++bit) {
            if (isBitSet(i, bit)) {
               if (this.switches[bit] == null) {
                  continue label36;
               }

               s = s.diff(this.switches[bit]);
            }
         }

         if (isSolved(s)) {
            LightboxSolution sol = new LightboxSolution(i);
            if (solution == null || sol.numMoves() < solution.numMoves()) {
               solution = sol;
            }
         }
      }

      return solution;
   }

   public void setInitial(LightboxState initial) {
      this.initial = initial;
   }

   public void setSwitchChange(Combination combination, LightboxState newState) {
      this.switches[combination.ordinal()] = newState;
   }
}
