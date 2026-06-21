package osrs;

import java.awt.image.BufferedImage;
import java.awt.image.PixelGrabber;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import javax.imageio.ImageIO;

public class jV {
   public static final aV a(byte[] var0) {
      Object var1 = null;

      try {
         Class var2 = ImageIO.class;
         Class var4 = ImageIO.class;
         BufferedImage var3;
         synchronized(ImageIO.class) {
            var3 = ImageIO.read(new ByteArrayInputStream(var0));
         }

         if (var3 != null) {
            int var11 = var3.getWidth();
            int var5 = var3.getHeight();
            int[] var6 = new int[var11 * var5];
            PixelGrabber var7 = new PixelGrabber(var3, 0, 0, var11, var5, var6, 0, var11);
            var7.grabPixels();
            return new aV(var6, var11, var5);
         }
      } catch (IOException var9) {
      } catch (InterruptedException var10) {
      }

      return null;
   }

   static {
      ImageIO.setUseCache(false);
   }
}
