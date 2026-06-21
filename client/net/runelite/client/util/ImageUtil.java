package net.runelite.client.util;

import com.google.common.primitives.Ints;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.DirectColorModel;
import java.awt.image.ImageObserver;
import java.awt.image.PixelGrabber;
import java.awt.image.RescaleOp;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.GrayFilter;
import net.runelite.api.Client;
import net.runelite.api.IndexedSprite;
import net.runelite.api.SpritePixels;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ImageUtil {
   private static final Logger log = LoggerFactory.getLogger(ImageUtil.class);

   public static BufferedImage bufferedImageFromImage(Image image) {
      return image instanceof BufferedImage ? (BufferedImage)image : toARGB(image);
   }

   public static BufferedImage toARGB(Image image) {
      if (image instanceof BufferedImage && ((BufferedImage)image).getType() == 2) {
         return (BufferedImage)image;
      } else {
         BufferedImage out = new BufferedImage(image.getWidth((ImageObserver)null), image.getHeight((ImageObserver)null), 2);
         Graphics2D g2d = out.createGraphics();
         g2d.drawImage(image, 0, 0, (ImageObserver)null);
         g2d.dispose();
         return out;
      }
   }

   public static BufferedImage recolorImage(Image image, Color rgb) {
      BufferedImage out = new BufferedImage(image.getWidth((ImageObserver)null), image.getHeight((ImageObserver)null), 2);
      Graphics2D g2d = out.createGraphics();
      g2d.drawImage(image, 0, 0, (ImageObserver)null);
      g2d.setComposite(AlphaComposite.SrcAtop);
      g2d.setColor(rgb);
      g2d.fillRect(0, 0, out.getWidth(), out.getHeight());
      g2d.dispose();
      return out;
   }

   public static BufferedImage luminanceOffset(Image rawImg, int offset) {
      BufferedImage image = toARGB(rawImg);
      int numComponents = image.getColorModel().getNumComponents();
      float[] scales = new float[numComponents];
      float[] offsets = new float[numComponents];
      Arrays.fill(scales, 1.0F);

      for(int i = 0; i < numComponents; ++i) {
         offsets[i] = (float)offset;
      }

      offsets[numComponents - 1] = 0.0F;
      return offset(image, scales, offsets);
   }

   public static BufferedImage luminanceScale(Image rawImg, float percentage) {
      BufferedImage image = toARGB(rawImg);
      int numComponents = image.getColorModel().getNumComponents();
      float[] scales = new float[numComponents];
      float[] offsets = new float[numComponents];
      Arrays.fill(offsets, 0.0F);

      for(int i = 0; i < numComponents; ++i) {
         scales[i] = percentage;
      }

      scales[numComponents - 1] = 1.0F;
      return offset(image, scales, offsets);
   }

   public static BufferedImage alphaOffset(Image rawImg, int offset) {
      BufferedImage image = toARGB(rawImg);
      int numComponents = image.getColorModel().getNumComponents();
      float[] scales = new float[numComponents];
      float[] offsets = new float[numComponents];
      Arrays.fill(scales, 1.0F);
      Arrays.fill(offsets, 0.0F);
      offsets[numComponents - 1] = (float)offset;
      return offset(image, scales, offsets);
   }

   public static BufferedImage alphaOffset(Image rawImg, float percentage) {
      BufferedImage image = toARGB(rawImg);
      int numComponents = image.getColorModel().getNumComponents();
      float[] scales = new float[numComponents];
      float[] offsets = new float[numComponents];
      Arrays.fill(scales, 1.0F);
      Arrays.fill(offsets, 0.0F);
      scales[numComponents - 1] = percentage;
      return offset(image, scales, offsets);
   }

   public static BufferedImage grayscaleImage(BufferedImage image) {
      Image grayImage = GrayFilter.createDisabledImage(image);
      return bufferedImageFromImage(grayImage);
   }

   public static BufferedImage resizeImage(BufferedImage image, int newWidth, int newHeight) {
      return resizeImage(image, newWidth, newHeight, false);
   }

   public static BufferedImage resizeImage(BufferedImage image, int newWidth, int newHeight, boolean preserveAspectRatio) {
      Image resized;
      if (preserveAspectRatio) {
         if (image.getWidth() > image.getHeight()) {
            resized = image.getScaledInstance(newWidth, -1, 4);
         } else {
            resized = image.getScaledInstance(-1, newHeight, 4);
         }
      } else {
         resized = image.getScaledInstance(newWidth, newHeight, 4);
      }

      return bufferedImageFromImage(resized);
   }

   public static BufferedImage resizeCanvas(BufferedImage image, int newWidth, int newHeight) {
      BufferedImage dimg = new BufferedImage(newWidth, newHeight, 2);
      int centeredX = newWidth / 2 - image.getWidth() / 2;
      int centeredY = newHeight / 2 - image.getHeight() / 2;
      Graphics2D g2d = dimg.createGraphics();
      g2d.drawImage(image, centeredX, centeredY, (ImageObserver)null);
      g2d.dispose();
      return dimg;
   }

   public static BufferedImage rotateImage(BufferedImage image, double theta) {
      AffineTransform transform = new AffineTransform();
      transform.rotate(theta, (double)image.getWidth() / 2.0, (double)image.getHeight() / 2.0);
      AffineTransformOp transformOp = new AffineTransformOp(transform, 2);
      return transformOp.filter(image, (BufferedImage)null);
   }

   public static BufferedImage flipImage(BufferedImage image, boolean horizontal, boolean vertical) {
      int x = 0;
      int y = 0;
      int w = image.getWidth();
      int h = image.getHeight();
      BufferedImage out = new BufferedImage(w, h, 2);
      Graphics2D g2d = out.createGraphics();
      if (horizontal) {
         x = w;
         w *= -1;
      }

      if (vertical) {
         y = h;
         h *= -1;
      }

      g2d.drawImage(image, x, y, w, h, (ImageObserver)null);
      g2d.dispose();
      return out;
   }

   public static BufferedImage outlineImage(BufferedImage image, Color color) {
      return outlineImage(image, color, false);
   }

   public static BufferedImage outlineImage(BufferedImage image, Color color, Boolean outlineCorners) {
      BufferedImage filledImage = fillImage(image, color);
      BufferedImage outlinedImage = new BufferedImage(image.getWidth(), image.getHeight(), 2);
      Graphics2D g2d = outlinedImage.createGraphics();

      for(int x = -1; x <= 1; ++x) {
         for(int y = -1; y <= 1; ++y) {
            if ((x != 0 || y != 0) && (outlineCorners || Math.abs(x) + Math.abs(y) == 1)) {
               g2d.drawImage(filledImage, x, y, (ImageObserver)null);
            }
         }
      }

      g2d.drawImage(image, 0, 0, (ImageObserver)null);
      g2d.dispose();
      return outlinedImage;
   }

   /** @deprecated */
   @Deprecated
   public static BufferedImage getResourceStreamFromClass(Class<?> c, String path) {
      return loadImageResource(c, path);
   }

   public static BufferedImage loadImageResource(Class<?> c, String path) {
      try {
         InputStream in = c.getResourceAsStream(path);

         BufferedImage var4;
         try {
            Class var13 = ImageIO.class;
            synchronized(ImageIO.class) {
               var4 = ImageIO.read(in);
            }
         } catch (Throwable var8) {
            if (in != null) {
               try {
                  in.close();
               } catch (Throwable var6) {
                  var8.addSuppressed(var6);
               }
            }

            throw var8;
         }

         if (in != null) {
            in.close();
         }

         return var4;
      } catch (IllegalArgumentException var9) {
         IllegalArgumentException e = var9;
         String filePath;
         if (path.startsWith("/")) {
            filePath = path;
         } else {
            String var10000 = c.getPackage().getName().replace('.', '/');
            filePath = var10000 + "/" + path;
         }

         log.warn("Failed to load image from class: {}, path: {}", c.getName(), filePath);
         throw new IllegalArgumentException(path, e);
      } catch (IOException var10) {
         IOException e = var10;
         throw new RuntimeException(path, e);
      }
   }

   public static BufferedImage fillImage(BufferedImage image, Color color) {
      BufferedImage filledImage = new BufferedImage(image.getWidth(), image.getHeight(), 2);

      for(int x = 0; x < filledImage.getWidth(); ++x) {
         for(int y = 0; y < filledImage.getHeight(); ++y) {
            int pixel = image.getRGB(x, y);
            int a = pixel >>> 24;
            if (a != 0) {
               filledImage.setRGB(x, y, color.getRGB());
            }
         }
      }

      return filledImage;
   }

   private static BufferedImage offset(BufferedImage image, float[] scales, float[] offsets) {
      return (new RescaleOp(scales, offsets, (RenderingHints)null)).filter(image, (BufferedImage)null);
   }

   public static SpritePixels getImageSpritePixels(BufferedImage image, Client client) {
      int[] pixels = new int[image.getWidth() * image.getHeight()];

      try {
         PixelGrabber g = new PixelGrabber(image, 0, 0, image.getWidth(), image.getHeight(), pixels, 0, image.getWidth());
         g.setColorModel(new DirectColorModel(32, 16711680, 65280, 255, -16777216));
         g.grabPixels();

         for(int i = 0; i < pixels.length; ++i) {
            if ((pixels[i] & -16777216) == 0) {
               pixels[i] = 0;
            }
         }
      } catch (InterruptedException var5) {
         InterruptedException ex = var5;
         log.debug("PixelGrabber was interrupted: ", ex);
      }

      return client.createSpritePixels(pixels, image.getWidth(), image.getHeight());
   }

   public static IndexedSprite getImageIndexedSprite(BufferedImage image, Client client) {
      byte[] pixels = new byte[image.getWidth() * image.getHeight()];
      List<Integer> palette = new ArrayList();
      palette.add(0);
      int[] sourcePixels = image.getRGB(0, 0, image.getWidth(), image.getHeight(), (int[])null, 0, image.getWidth());

      for(int j = 0; j < sourcePixels.length; ++j) {
         int argb = sourcePixels[j];
         int a = argb >> 24 & 255;
         int rgb = argb & 16777215;
         int paletteIdx = 0;
         if (a == 255) {
            paletteIdx = palette.indexOf(rgb);
            if (paletteIdx == -1) {
               paletteIdx = palette.size();
               palette.add(rgb);
            }
         }

         pixels[j] = (byte)paletteIdx;
      }

      if (palette.size() > 256) {
         int var10002 = palette.size();
         throw new RuntimeException("Passed in image had " + (var10002 - 1) + " different colors, exceeding the max of 255.");
      } else {
         IndexedSprite sprite = client.createIndexedSprite();
         sprite.setPixels(pixels);
         sprite.setPalette(Ints.toArray(palette));
         sprite.setWidth(image.getWidth());
         sprite.setHeight(image.getHeight());
         sprite.setOriginalWidth(image.getWidth());
         sprite.setOriginalHeight(image.getHeight());
         sprite.setOffsetX(0);
         sprite.setOffsetY(0);
         return sprite;
      }
   }

   static {
      ImageIO.setUseCache(false);
   }
}
