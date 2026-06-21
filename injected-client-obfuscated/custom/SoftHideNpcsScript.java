package custom;

import java.util.Arrays;

public class SoftHideNpcsScript {
   private final int[] ids;

   public SoftHideNpcsScript(int[] var1) {
      this.ids = var1;
   }

   public int[] getIds() {
      return this.ids;
   }

   public String toString() {
      return "SoftHideNpcsScript(ids=" + Arrays.toString(this.getIds()) + ")";
   }
}
