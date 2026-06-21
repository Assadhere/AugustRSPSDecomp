package net.runelite.client.events;

import java.awt.image.BufferedImage;
import java.io.File;

public final class ScreenshotTaken {
   private final File path;
   private final BufferedImage screenshot;

   public ScreenshotTaken(File path, BufferedImage screenshot) {
      this.path = path;
      this.screenshot = screenshot;
   }

   public File getPath() {
      return this.path;
   }

   public BufferedImage getScreenshot() {
      return this.screenshot;
   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof ScreenshotTaken)) {
         return false;
      } else {
         ScreenshotTaken other = (ScreenshotTaken)o;
         Object this$path = this.getPath();
         Object other$path = other.getPath();
         if (this$path == null) {
            if (other$path != null) {
               return false;
            }
         } else if (!this$path.equals(other$path)) {
            return false;
         }

         Object this$screenshot = this.getScreenshot();
         Object other$screenshot = other.getScreenshot();
         if (this$screenshot == null) {
            if (other$screenshot != null) {
               return false;
            }
         } else if (!this$screenshot.equals(other$screenshot)) {
            return false;
         }

         return true;
      }
   }

   public int hashCode() {
      int PRIME = true;
      int result = 1;
      Object $path = this.getPath();
      result = result * 59 + ($path == null ? 43 : $path.hashCode());
      Object $screenshot = this.getScreenshot();
      result = result * 59 + ($screenshot == null ? 43 : $screenshot.hashCode());
      return result;
   }

   public String toString() {
      String var10000 = String.valueOf(this.getPath());
      return "ScreenshotTaken(path=" + var10000 + ", screenshot=" + String.valueOf(this.getScreenshot()) + ")";
   }
}
