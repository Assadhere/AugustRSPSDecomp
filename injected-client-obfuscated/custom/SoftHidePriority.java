package custom;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import osrs.aH;
import osrs.s;

public final class SoftHidePriority {
   public static final int SORT_BACK_BOOST = 100000;
   private static volatile Set<Integer> npcIds = Collections.emptySet();
   private static volatile byte alphaByte = 0;
   private static volatile byte[] opaqueBuf = new byte[0];
   private static volatile byte opaqueBufFillAlpha = 0;

   private SoftHidePriority() {
   }

   public static Set<Integer> getNpcIds() {
      return npcIds;
   }

   public static void setNpcIds(Set<Integer> var0) {
      npcIds = (Set)(var0 != null && !var0.isEmpty() ? new HashSet(var0) : Collections.emptySet());
   }

   public static void setAlphaByte(byte var0) {
      if (var0 != alphaByte) {
         alphaByte = var0;
      }
   }

   public static void applyAlpha(s var0, aH var1) {
      if (var1 != null && var0 != null) {
         Set var2 = npcIds;
         if (!var2.isEmpty() && var2.contains(var0.getId())) {
            int var3 = var1.getFaceCount();
            if (var3 > 0) {
               byte var4 = alphaByte;
               if (var4 == 0) {
                  var1.I = null;
               } else {
                  byte[] var5 = var1.getFaceTransparencies();
                  if (var5 != null && var5.length >= var3) {
                     var1.I = transparentBufferFor(var5, var3, var4);
                  } else {
                     var1.I = opaqueBufferFor(var3, var4);
                  }

               }
            }
         }
      }
   }

   private static byte[] opaqueBufferFor(int var0, byte var1) {
      byte[] var2 = opaqueBuf;
      if (var2.length >= var0 && opaqueBufFillAlpha == var1) {
         return var2;
      } else {
         int var3 = Math.max(var0, var2.length * 2);
         if (var3 < var0) {
            var3 = var0;
         }

         var2 = new byte[var3];
         Arrays.fill(var2, var1);
         opaqueBuf = var2;
         opaqueBufFillAlpha = var1;
         return var2;
      }
   }

   private static byte[] transparentBufferFor(byte[] var0, int var1, byte var2) {
      byte[] var3 = new byte[var1];
      int var4 = var2 & 255;

      for(int var5 = 0; var5 < var1; ++var5) {
         int var6 = var0[var5] & 255;
         var3[var5] = (byte)(var6 < var4 ? var4 : var6);
      }

      return var3;
   }
}
