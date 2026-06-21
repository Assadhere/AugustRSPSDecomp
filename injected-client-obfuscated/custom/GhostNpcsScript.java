package custom;

import java.util.Arrays;

public class GhostNpcsScript {
   private final int[] ids;

   public GhostNpcsScript(int[] var1) {
      this.ids = var1;
   }

   public int[] getIds() {
      return this.ids;
   }

   public String toString() {
      return "GhostNpcsScript(ids=" + Arrays.toString(this.getIds()) + ")";
   }
}
