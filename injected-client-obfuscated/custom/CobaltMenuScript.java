package custom;

import java.util.Arrays;

public class CobaltMenuScript {
   private final boolean open;
   private final int x;
   private final int y;
   private final int width;
   private final int height;
   private final int canvasWidth;
   private final int canvasHeight;
   private final String[] options;
   private final String[] targets;

   public CobaltMenuScript(boolean var1, int var2, int var3, int var4, int var5, int var6, int var7, String[] var8, String[] var9) {
      this.open = var1;
      this.x = var2;
      this.y = var3;
      this.width = var4;
      this.height = var5;
      this.canvasWidth = var6;
      this.canvasHeight = var7;
      this.options = var8;
      this.targets = var9;
   }

   public boolean isOpen() {
      return this.open;
   }

   public int getX() {
      return this.x;
   }

   public int getY() {
      return this.y;
   }

   public int getWidth() {
      return this.width;
   }

   public int getHeight() {
      return this.height;
   }

   public int getCanvasWidth() {
      return this.canvasWidth;
   }

   public int getCanvasHeight() {
      return this.canvasHeight;
   }

   public String[] getOptions() {
      return this.options;
   }

   public String[] getTargets() {
      return this.targets;
   }

   public String toString() {
      boolean var10000 = this.isOpen();
      return "CobaltMenuScript(open=" + var10000 + ", x=" + this.getX() + ", y=" + this.getY() + ", width=" + this.getWidth() + ", height=" + this.getHeight() + ", canvasWidth=" + this.getCanvasWidth() + ", canvasHeight=" + this.getCanvasHeight() + ", options=" + Arrays.deepToString(this.getOptions()) + ", targets=" + Arrays.deepToString(this.getTargets()) + ")";
   }
}
