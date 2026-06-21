package custom;

public class CobaltClickScript {
   private final int x;
   private final int y;
   private final int button;
   private final int targetWidth;
   private final int targetHeight;
   private final int deltaMs;

   public CobaltClickScript(int var1, int var2, int var3, int var4, int var5, int var6) {
      this.x = var1;
      this.y = var2;
      this.button = var3;
      this.targetWidth = var4;
      this.targetHeight = var5;
      this.deltaMs = var6;
   }

   public int getX() {
      return this.x;
   }

   public int getY() {
      return this.y;
   }

   public int getButton() {
      return this.button;
   }

   public int getTargetWidth() {
      return this.targetWidth;
   }

   public int getTargetHeight() {
      return this.targetHeight;
   }

   public int getDeltaMs() {
      return this.deltaMs;
   }

   public String toString() {
      int var10000 = this.getX();
      return "CobaltClickScript(x=" + var10000 + ", y=" + this.getY() + ", button=" + this.getButton() + ", targetWidth=" + this.getTargetWidth() + ", targetHeight=" + this.getTargetHeight() + ", deltaMs=" + this.getDeltaMs() + ")";
   }
}
