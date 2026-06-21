package custom;

public class CobaltScrollScript {
   private final int widgetId;
   private final int scrollX;
   private final int scrollY;

   public CobaltScrollScript(int var1, int var2, int var3) {
      this.widgetId = var1;
      this.scrollX = var2;
      this.scrollY = var3;
   }

   public int getWidgetId() {
      return this.widgetId;
   }

   public int getScrollX() {
      return this.scrollX;
   }

   public int getScrollY() {
      return this.scrollY;
   }

   public String toString() {
      int var10000 = this.getWidgetId();
      return "CobaltScrollScript(widgetId=" + var10000 + ", scrollX=" + this.getScrollX() + ", scrollY=" + this.getScrollY() + ")";
   }
}
