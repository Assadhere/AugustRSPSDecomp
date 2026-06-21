package osrs;

public class jD {
   public static int a(CharSequence var0, CharSequence var1, ax var2) {
      int var3 = var0.length();
      int var4 = var1.length();
      int var5 = 0;
      int var6 = 0;
      char var7 = 0;
      char var8 = 0;

      int var9;
      int var12;
      char var13;
      char var14;
      int var17;
      int var18;
      while(var5 - var7 < var3 || var6 - var8 < var4) {
         if (var5 - var7 >= var3) {
            return -1;
         }

         if (var6 - var8 >= var4) {
            return 1;
         }

         if (var7 != 0) {
            var9 = var7;
            boolean var10 = false;
         } else {
            var9 = var0.charAt(var5++);
         }

         if (var8 != 0) {
            var17 = var8;
            boolean var11 = false;
         } else {
            var17 = var1.charAt(var6++);
         }

         var7 = a((char)var9);
         var8 = a((char)var17);
         var18 = a((char)var9, var2);
         var12 = a((char)var17, var2);
         if (var18 != var12 && Character.toUpperCase((char)var18) != Character.toUpperCase((char)var12)) {
            var13 = Character.toLowerCase((char)var18);
            var14 = Character.toLowerCase((char)var12);
            if (var13 != var14) {
               return b(var13, var2) - b(var14, var2);
            }
         }
      }

      var9 = Math.min(var3, var4);

      for(var17 = 0; var17 < var9; ++var17) {
         if (ax.c == var2) {
            var18 = var3 - 1 - var17;
            var12 = var4 - 1 - var17;
         } else {
            var12 = var17;
            var18 = var17;
         }

         var13 = var0.charAt(var18);
         var14 = var1.charAt(var12);
         if (var13 != var14 && Character.toUpperCase(var13) != Character.toUpperCase(var14)) {
            char var15 = Character.toLowerCase(var13);
            char var16 = Character.toLowerCase(var14);
            if (var15 != var16) {
               return b(var15, var2) - b(var16, var2);
            }
         }
      }

      var17 = var3 - var4;
      if (var17 != 0) {
         return var17;
      } else {
         for(var18 = 0; var18 < var9; ++var18) {
            char var19 = var0.charAt(var18);
            var13 = var1.charAt(var18);
            if (var19 != var13) {
               return b(var19, var2) - b(var13, var2);
            }
         }

         return 0;
      }
   }

   public static char a(char var0, ax var1) {
      if (var0 >= 192 && var0 <= 255) {
         if (var0 >= 192 && var0 <= 198) {
            return 'A';
         }

         if (var0 == 199) {
            return 'C';
         }

         if (var0 >= 200 && var0 <= 203) {
            return 'E';
         }

         if (var0 >= 204 && var0 <= 207) {
            return 'I';
         }

         if (var0 == 209 && ax.f != var1) {
            return 'N';
         }

         if (var0 >= 210 && var0 <= 214) {
            return 'O';
         }

         if (var0 >= 217 && var0 <= 220) {
            return 'U';
         }

         if (var0 == 221) {
            return 'Y';
         }

         if (var0 == 223) {
            return 's';
         }

         if (var0 >= 224 && var0 <= 230) {
            return 'a';
         }

         if (var0 == 231) {
            return 'c';
         }

         if (var0 >= 232 && var0 <= 235) {
            return 'e';
         }

         if (var0 >= 236 && var0 <= 239) {
            return 'i';
         }

         if (var0 == 241 && ax.f != var1) {
            return 'n';
         }

         if (var0 >= 242 && var0 <= 246) {
            return 'o';
         }

         if (var0 >= 249 && var0 <= 252) {
            return 'u';
         }

         if (var0 == 253 || var0 == 255) {
            return 'y';
         }
      }

      if (var0 == 338) {
         return 'O';
      } else if (var0 == 339) {
         return 'o';
      } else {
         return var0 == 376 ? 'Y' : var0;
      }
   }

   public static char a(char var0) {
      if (var0 == 198) {
         return 'E';
      } else if (var0 == 230) {
         return 'e';
      } else if (var0 == 223) {
         return 's';
      } else if (var0 == 338) {
         return 'E';
      } else {
         return (char)(var0 == 339 ? 'e' : '\u0000');
      }
   }

   public static int b(char var0, ax var1) {
      int var2 = var0 << 4;
      if (Character.isUpperCase(var0) || Character.isTitleCase(var0)) {
         var0 = Character.toLowerCase(var0);
         var2 = (var0 << 4) + 1;
      }

      if (var0 == 241 && ax.f == var1) {
         var2 = 1762;
      }

      return var2;
   }
}
