package osrs;

public final class km {
   public static int a(CharSequence var0) {
      int var1 = var0.length();
      int var2 = 0;

      for(int var3 = 0; var3 < var1; ++var3) {
         char var4 = var0.charAt(var3);
         if (var4 <= 127) {
            ++var2;
         } else if (var4 <= 2047) {
            var2 += 2;
         } else {
            var2 += 3;
         }
      }

      return var2;
   }

   public static String a(byte[] var0, int var1, int var2) {
      char[] var3 = new char[var2];
      int var4 = 0;
      int var5 = var1;

      int var6;
      for(int var7 = var1 + var2; var5 < var7; var3[var4++] = (char)var6) {
         int var8 = var0[var5++] & 255;
         if (var8 < 128) {
            if (var8 == 0) {
               var6 = 65533;
            } else {
               var6 = var8;
            }
         } else if (var8 < 192) {
            var6 = 65533;
         } else if (var8 < 224) {
            if (var5 < var7 && (var0[var5] & 192) == 128) {
               var6 = (var8 & 31) << 6 | var0[var5++] & 63;
               if (var6 < 128) {
                  var6 = 65533;
               }
            } else {
               var6 = 65533;
            }
         } else if (var8 < 240) {
            if (var5 + 1 < var7 && (var0[var5] & 192) == 128 && (var0[var5 + 1] & 192) == 128) {
               var6 = (var8 & 15) << 12 | (var0[var5++] & 63) << 6 | var0[var5++] & 63;
               if (var6 < 2048) {
                  var6 = 65533;
               }
            } else {
               var6 = 65533;
            }
         } else if (var8 < 248) {
            if (var5 + 2 < var7 && (var0[var5] & 192) == 128 && (var0[var5 + 1] & 192) == 128 && (var0[var5 + 2] & 192) == 128) {
               int var9 = (var8 & 7) << 18 | (var0[var5++] & 63) << 12 | (var0[var5++] & 63) << 6 | var0[var5++] & 63;
               if (var9 >= 65536 && var9 <= 1114111) {
                  var6 = 65533;
               } else {
                  var6 = 65533;
               }
            } else {
               var6 = 65533;
            }
         } else {
            var6 = 65533;
         }
      }

      return new String(var3, 0, var4);
   }
}
