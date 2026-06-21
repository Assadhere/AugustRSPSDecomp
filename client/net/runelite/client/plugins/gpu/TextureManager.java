package net.runelite.client.plugins.gpu;

import java.nio.ByteBuffer;
import javax.inject.Singleton;
import net.runelite.api.Texture;
import net.runelite.api.TextureProvider;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL33C;
import org.lwjgl.opengl.GL42C;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
class TextureManager {
   private static final Logger log = LoggerFactory.getLogger(TextureManager.class);
   static final int TEXTURE_COUNT = 256;
   private static final int TEXTURE_SIZE = 128;

   int initTextureArray(TextureProvider textureProvider) {
      if (!this.allTexturesLoaded(textureProvider)) {
         return -1;
      } else {
         Texture[] textures = textureProvider.getTextures();
         int textureArrayId = GL33C.glGenTextures();
         GL33C.glBindTexture(35866, textureArrayId);
         if (GL.getCapabilities().glTexStorage3D != 0L) {
            GL42C.glTexStorage3D(35866, 8, 32856, 128, 128, textures.length);
         } else {
            int size = 128;

            for(int i = 0; i < 8; ++i) {
               GL33C.glTexImage3D(35866, i, 32856, size, size, textures.length, 0, 6408, 5121, 0L);
               size /= 2;
            }
         }

         GL33C.glTexParameteri(35866, 10241, 9728);
         GL33C.glTexParameteri(35866, 10240, 9728);
         GL33C.glTexParameteri(35866, 10242, 33071);
         double save = textureProvider.getBrightness();
         textureProvider.setBrightness(1.0);
         this.updateTextures(textureProvider, textureArrayId);
         textureProvider.setBrightness(save);
         GL33C.glActiveTexture(33985);
         GL33C.glBindTexture(35866, textureArrayId);
         GL33C.glGenerateMipmap(35866);
         GL33C.glActiveTexture(33984);
         return textureArrayId;
      }
   }

   void setAnisotropicFilteringLevel(int textureArrayId, int level) {
      GL33C.glBindTexture(35866, textureArrayId);
      if (level == 0) {
         GL33C.glTexParameteri(35866, 10241, 9728);
      } else {
         GL33C.glTexParameteri(35866, 10241, 9986);
      }

      if (GL.getCapabilities().GL_EXT_texture_filter_anisotropic) {
         float maxSamples = GL33C.glGetFloat(34047);
         float anisoLevel = Math.max(1.0F, Math.min(maxSamples, (float)level));
         GL33C.glTexParameterf(35866, 34046, anisoLevel);
      }

   }

   void freeTextureArray(int textureArrayId) {
      GL33C.glDeleteTextures(textureArrayId);
   }

   private boolean allTexturesLoaded(TextureProvider textureProvider) {
      Texture[] textures = textureProvider.getTextures();
      if (textures != null && textures.length != 0) {
         for(int textureId = 0; textureId < textures.length; ++textureId) {
            Texture texture = textures[textureId];
            if (texture != null) {
               int[] pixels = textureProvider.load(textureId);
               if (pixels == null) {
                  return false;
               }
            }
         }

         return true;
      } else {
         return false;
      }
   }

   private void updateTextures(TextureProvider textureProvider, int textureArrayId) {
      Texture[] textures = textureProvider.getTextures();
      GL33C.glBindTexture(35866, textureArrayId);
      int cnt = 0;

      for(int textureId = 0; textureId < textures.length; ++textureId) {
         Texture texture = textures[textureId];
         if (texture != null) {
            int[] srcPixels = textureProvider.load(textureId);
            if (srcPixels == null) {
               log.warn("No pixels for texture {}!", textureId);
            } else {
               ++cnt;
               if (srcPixels.length != 16384) {
                  log.warn("Texture size for {} is {}!", textureId, srcPixels.length);
               } else {
                  byte[] pixels = convertPixels(srcPixels, 128, 128, 128, 128);
                  ByteBuffer pixelBuffer = ByteBuffer.allocateDirect(pixels.length);
                  pixelBuffer.put(pixels);
                  pixelBuffer.flip();
                  GL33C.glTexSubImage3D(35866, 0, 0, 0, textureId, 128, 128, 1, 6408, 5121, pixelBuffer);
               }
            }
         }
      }

      log.debug("Uploaded textures {}", cnt);
   }

   private static byte[] convertPixels(int[] srcPixels, int width, int height, int textureWidth, int textureHeight) {
      byte[] pixels = new byte[textureWidth * textureHeight * 4];
      int pixelIdx = 0;
      int srcPixelIdx = 0;
      int offset = (textureWidth - width) * 4;

      for(int y = 0; y < height; ++y) {
         for(int x = 0; x < width; ++x) {
            int rgb = srcPixels[srcPixelIdx++];
            if (rgb != 0) {
               pixels[pixelIdx++] = (byte)(rgb >> 16);
               pixels[pixelIdx++] = (byte)(rgb >> 8);
               pixels[pixelIdx++] = (byte)rgb;
               pixels[pixelIdx++] = -1;
            } else {
               pixelIdx += 4;
            }
         }

         pixelIdx += offset;
      }

      return pixels;
   }

   float[] computeTextureAnimations(TextureProvider textureProvider) {
      Texture[] textures = textureProvider.getTextures();
      if (textures.length > 256) {
         log.warn("texture limit exceeded: {} > {}", textures.length, 256);
      }

      float[] anims = new float[512];

      for(int i = 0; i < Math.min(256, textures.length); ++i) {
         Texture texture = textures[i];
         if (texture != null) {
            float u = 0.0F;
            float v = 0.0F;
            switch (texture.getAnimationDirection()) {
               case 1:
                  v = -1.0F;
                  break;
               case 2:
                  u = -1.0F;
                  break;
               case 3:
                  v = 1.0F;
                  break;
               case 4:
                  u = 1.0F;
            }

            int speed = texture.getAnimationSpeed();
            u *= (float)speed;
            v *= (float)speed;
            anims[i * 2] = u;
            anims[i * 2 + 1] = v;
         }
      }

      return anims;
   }
}
