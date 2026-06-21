package custom;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Set;
import osrs.ModelUnlit;
import osrs.aH;
import osrs.aL;
import osrs.aN;
import osrs.dH;
import osrs.s;

public final class GhostRender {
   public static final int DEFAULT_ALPHA = 150;
   public static final int DEFAULT_TINT_HUE = 0;
   public static final int DEFAULT_TINT_SAT = 0;
   public static final int DEFAULT_TINT_LUM = 0;
   public static final int DEFAULT_TINT_AMOUNT = 40;
   private static volatile int ghostAlpha = 150;
   private static volatile dH ghostTint = new dH((byte)0, (byte)0, (byte)0, (byte)40);
   private static volatile int[] npcIds = new int[0];
   private static volatile byte[] opaqueBuf = new byte[0];
   private static volatile int opaqueBufFillAlpha = -1;
   private static final int SENTINEL_TEXTURE_BASE = 8192;

   private GhostRender() {
   }

   public static void setAlpha(int var0) {
      ghostAlpha = Math.max(0, Math.min(255, var0));
   }

   public static void setTint(int var0, int var1, int var2, int var3) {
      ghostTint = new dH((byte)Math.max(0, Math.min(63, var0)), (byte)Math.max(0, Math.min(7, var1)), (byte)Math.max(0, Math.min(127, var2)), (byte)Math.max(0, Math.min(127, var3)));
   }

   public static void resetToDefaults() {
      setAlpha(150);
      setTint(0, 0, 0, 40);
   }

   public static boolean isGhost(int var0) {
      int[] var1 = npcIds;
      int[] var2 = var1;
      int var3 = var1.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         int var5 = var2[var4];
         if (var5 == var0) {
            return true;
         }
      }

      return false;
   }

   public static void setNpcIds(Set<Integer> var0) {
      if (var0 != null && !var0.isEmpty()) {
         int[] var1 = new int[var0.size()];
         int var2 = 0;

         Integer var4;
         for(Iterator var3 = var0.iterator(); var3.hasNext(); var1[var2++] = var4) {
            var4 = (Integer)var3.next();
         }

         npcIds = var1;
      } else {
         npcIds = new int[0];
      }
   }

   public static void setNpcIds(int[] var0) {
      npcIds = var0 != null && var0.length != 0 ? (int[])(([I)var0).clone() : new int[0];
   }

   public static void apply(s var0, aH var1) {
      if (var1 != null && var0 != null) {
         if (isGhost(var0.getId())) {
            int var2 = var1.getFaceCount();
            if (var2 > 0) {
               int var3 = ghostAlpha;
               byte[] var4 = var1.getFaceTransparencies();
               if (var4 != null && var4.length >= var2) {
                  byte[] var5 = new byte[var2];
                  int var6 = var3 & 255;

                  for(int var7 = 0; var7 < var2; ++var7) {
                     int var8 = var4[var7] & 255;
                     var5[var7] = (byte)(var8 < var6 ? var6 : var8);
                  }

                  var1.I = var5;
               } else {
                  var1.I = opaqueBufferFor(var2, var3);
               }

               dH var9 = ghostTint;
               if (var9.b()) {
                  var1.a(var9, (short)var1.A);
               }

            }
         }
      }
   }

   private static byte[] opaqueBufferFor(int var0, int var1) {
      byte[] var2 = opaqueBuf;
      if (var2.length >= var0 && opaqueBufFillAlpha == var1) {
         return var2;
      } else {
         int var3 = Math.max(var0, var2.length * 2);
         if (var3 < var0) {
            var3 = var0;
         }

         var2 = new byte[var3];
         Arrays.fill(var2, (byte)var1);
         opaqueBuf = var2;
         opaqueBufFillAlpha = var1;
         return var2;
      }
   }

   public static void applyOverridePairs(aN var0, aL var1, ModelUnlit[] var2) {
      if (var0 != null && var1 != null && var2 != null) {
         short[] var3 = var0.z;
         if (var3 != null && var3.length != 0 && (var3[0] & '\uffff') >= 8192) {
            short[] var4 = var1.getTextureToReplaceWith();
            if (var4 != null && var4.length != 0) {
               int var5 = 0;
               int var6 = var4[var5++] & '\uffff';

               for(int var7 = 0; var7 < var6; ++var7) {
                  if (var5 + 3 > var4.length) {
                     return;
                  }

                  int var8 = var4[var5++] & '\uffff';
                  int var9 = var4[var5++] & '\uffff';
                  int var10 = var4[var5++] & '\uffff';
                  if (var5 + var10 * 2 + 1 > var4.length) {
                     return;
                  }

                  int var11;
                  int var12;
                  short var13;
                  int var15;
                  for(var11 = 0; var11 < var10; ++var11) {
                     var12 = var4[var5++];
                     var13 = var4[var5++];
                     int var14 = Math.min(var8 + var9, var2.length);

                     for(var15 = Math.min(var8, var2.length); var15 < var14; ++var15) {
                        if (var2[var15] != null) {
                           var2[var15].a((short)var12, (short)var13);
                        }
                     }
                  }

                  var11 = var4[var5++] & '\uffff';
                  if (var5 + var11 * 2 > var4.length) {
                     return;
                  }

                  for(var12 = 0; var12 < var11; ++var12) {
                     var13 = var4[var5++];
                     short var17 = var4[var5++];
                     var15 = Math.min(var8 + var9, var2.length);

                     for(int var16 = Math.min(var8, var2.length); var16 < var15; ++var16) {
                        if (var2[var16] != null) {
                           var2[var16].b(var13, var17);
                        }
                     }
                  }
               }

            }
         }
      }
   }
}
