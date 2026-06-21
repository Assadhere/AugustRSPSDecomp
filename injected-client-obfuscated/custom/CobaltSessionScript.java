package custom;

public class CobaltSessionScript {
   private final boolean enter;
   private final String targetName;
   private final int targetWidth;
   private final int targetHeight;

   public CobaltSessionScript(boolean var1, String var2, int var3, int var4) {
      this.enter = var1;
      this.targetName = var2;
      this.targetWidth = var3;
      this.targetHeight = var4;
   }

   public boolean isEnter() {
      return this.enter;
   }

   public String getTargetName() {
      return this.targetName;
   }

   public int getTargetWidth() {
      return this.targetWidth;
   }

   public int getTargetHeight() {
      return this.targetHeight;
   }

   public String toString() {
      boolean var10000 = this.isEnter();
      return "CobaltSessionScript(enter=" + var10000 + ", targetName=" + this.getTargetName() + ", targetWidth=" + this.getTargetWidth() + ", targetHeight=" + this.getTargetHeight() + ")";
   }
}
