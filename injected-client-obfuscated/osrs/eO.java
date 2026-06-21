package osrs;

import net.runelite.api.widgets.WidgetConfigNode;

public class eO extends az implements WidgetConfigNode {
   public final int a;
   public final int b;
   public final int c;
   public final int d;
   public eO e;

   public eO(int var1, int var2, int var3, int var4) {
      this.a = var1;
      this.b = var2;
      this.c = var3;
      this.d = var4;
   }

   public int a() {
      return this.c;
   }

   public int b() {
      return this.d;
   }

   public static eO a(eO var0, int var1, int var2, int var3, int var4) {
      eO var5 = var0;

      eO var6;
      eO var7;
      for(var6 = null; var5 != null; var5 = var5.e) {
         if (var5.b >= var1 && var5.a <= var2) {
            if (var6 != null) {
               var6.e = var5.e;
            } else {
               var0 = var5.e;
            }

            if (var5.a < var1) {
               var7 = new eO(var5.a, var1 - 1, var5.c, var5.d);
               if (var6 != null) {
                  var7.e = var6.e;
               } else {
                  var7.e = var0;
               }

               if (var6 != null) {
                  var6.e = var7;
               } else {
                  var0 = var7;
               }

               var6 = var7;
            }

            if (var5.b > var2) {
               var7 = new eO(var2 + 1, var5.b, var5.c, var5.d);
               if (var6 != null) {
                  var7.e = var6.e;
               } else {
                  var7.e = var0;
               }

               if (var6 != null) {
                  var6.e = var7;
               } else {
                  var0 = var7;
               }
            }
         }

         var6 = var5;
      }

      var6 = null;

      for(var7 = var0; var7 != null && var7.a <= var1; var7 = var7.e) {
         var6 = var7;
      }

      var7 = new eO(var1, var2, var3, var4);
      if (var6 != null) {
         var7.e = var6.e;
      } else {
         var7.e = var0;
      }

      if (var6 != null) {
         var6.e = var7;
      } else {
         var0 = var7;
      }

      return var0;
   }

   public int getClickMask() {
      return this.c;
   }

   public int getOpMask() {
      return this.d;
   }
}
