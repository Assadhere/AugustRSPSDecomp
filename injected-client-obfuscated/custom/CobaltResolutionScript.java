package custom;

public class CobaltResolutionScript {
   private final int width;
   private final int height;

   public CobaltResolutionScript(int var1, int var2) {
      this.width = var1;
      this.height = var2;
   }

   public int getWidth() {
      return this.width;
   }

   public int getHeight() {
      return this.height;
   }

   public String toString() {
      int var10000 = this.getWidth();
      return "CobaltResolutionScript(width=" + var10000 + ", height=" + this.getHeight() + ")";
   }
}
