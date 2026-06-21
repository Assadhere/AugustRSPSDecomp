package custom;

import java.util.Arrays;

public class HighlightNpcsScript {
   private final int[] ids;

   public HighlightNpcsScript(int[] var1) {
      this.ids = var1;
   }

   public int[] getIds() {
      return this.ids;
   }

   public String toString() {
      return "HighlightNpcsScript(ids=" + Arrays.toString(this.getIds()) + ")";
   }
}
