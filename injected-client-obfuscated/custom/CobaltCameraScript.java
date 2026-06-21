package custom;

public class CobaltCameraScript {
   private final int yaw;
   private final int pitch;
   private final int zoom;

   public CobaltCameraScript(int var1, int var2, int var3) {
      this.yaw = var1;
      this.pitch = var2;
      this.zoom = var3;
   }

   public int getYaw() {
      return this.yaw;
   }

   public int getPitch() {
      return this.pitch;
   }

   public int getZoom() {
      return this.zoom;
   }

   public String toString() {
      int var10000 = this.getYaw();
      return "CobaltCameraScript(yaw=" + var10000 + ", pitch=" + this.getPitch() + ", zoom=" + this.getZoom() + ")";
   }
}
