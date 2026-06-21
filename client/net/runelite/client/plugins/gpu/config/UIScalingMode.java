package net.runelite.client.plugins.gpu.config;

public enum UIScalingMode {
   NEAREST("Nearest Neighbor"),
   LINEAR("Bilinear"),
   MITCHELL("Bicubic (Mitchell)"),
   CATMULL_ROM("Bicubic (Catmull-Rom)"),
   XBR("xBR"),
   HYBRID("Hybrid");

   private final String name;

   public String toString() {
      return this.name;
   }

   public String getName() {
      return this.name;
   }

   private UIScalingMode(String name) {
      this.name = name;
   }
}
