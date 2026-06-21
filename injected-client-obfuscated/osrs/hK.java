package osrs;

import java.util.ArrayList;

public final class hK {
   public static int a = 0;
   public static int b = 1;
   public static int c = 2;
   public static int d = 3;
   public static int e = 5;
   public ArrayList f = new ArrayList(10);
   public int g = 0;

   public hK() {
      this.a(10);
   }

   public void a(int var1) {
      for(int var2 = 0; var2 < var1; ++var2) {
         this.f.add(new is());
      }

   }

   public void a() {
      this.g = 0;
   }

   public void a(int var1, int var2, int var3, int var4, int var5, int var6) {
      if (this.g >= this.f.size()) {
         this.f.add(new is());
      }

      ((is)this.f.get(this.g)).a(var1, var2, var3, var4, var5, var6);
      ++this.g;
   }

   public void a(int var1, int var2, int var3, int var4, int var5) {
      if (var2 == var4 && var2 != -1 && var3 != var5) {
         this.a(e, var1, -1, var2, -1, var5 - var3);
      } else {
         boolean var6;
         int var7;
         is var8;
         if (var2 == -1 && var4 != -1 && var5 > 0) {
            var6 = false;

            for(var7 = 0; var7 < this.g; ++var7) {
               var8 = (is)this.f.get(var7);
               if (c == var8.d && var8.c == var4 && var8.f == var5) {
                  var8.d = d;
                  var8.b = var1;
                  var8.e = -1;
                  var8.f = -1;
                  var6 = true;
                  break;
               }
            }

            if (!var6) {
               this.a(b, var1, -1, var4, -1, var5);
            }
         } else if (var2 != -1 && var4 == -1 && var3 > 0) {
            var6 = false;

            for(var7 = 0; var7 < this.g; ++var7) {
               var8 = (is)this.f.get(var7);
               if (b == var8.d && var8.c == var2 && var8.f == var3) {
                  var8.d = d;
                  var8.b = var1;
                  var8.e = var8.c;
                  var8.c = -1;
                  var8.f = -1;
                  var6 = true;
                  break;
               }
            }

            if (!var6) {
               this.a(c, var1, -1, var2, -1, var3);
            }
         } else if (var2 != -1 && var4 != -1 && var2 != var4) {
            this.a(c, var1, -1, var2, -1, var3);
            this.a(b, var1, -1, var4, -1, var5);
         }
      }

   }

   public void b() {
      boolean var1;
      do {
         int var2 = -1;
         int var3 = -1;
         int var4 = -1;
         int var5 = -1;
         var1 = false;

         for(int var6 = 1; var6 < this.g; ++var6) {
            is var7 = (is)this.f.get(var6);
            is var8 = (is)this.f.get(var6 - 1);
            if (var2 == -1 && c == var8.d && b == var7.d && var7.a == var8.a) {
               var2 = var7.a;
               var3 = var6;
               var4 = var8.c;
               var5 = var7.c;
            } else if (var2 != -1 && c == var8.d && b == var7.d && var7.a == var8.a && var8.c == var5 && var7.c == var4) {
               is var9 = (is)this.f.get(var3);
               is var10 = (is)this.f.get(var3 - 1);
               var10.d = a;
               var9.d = a;
               var7.d = a;
               var8.d = d;
               var8.b = var10.a;
               var8.e = var10.c;
               var8.f = -1;
               var1 = true;
               break;
            }
         }
      } while(var1);

   }

   public void b(int var1) {
      for(int var2 = 0; var2 < this.g; ++var2) {
         is var3 = (is)this.f.get(var2);
         if (var3.d != 0) {
            this.a(var1, var3);
         }
      }

   }

   public void a(int var1, is var2) {
      Object[] var3 = new Object[]{var1, var2.d, var2.a, var2.b, var2.c, var2.e, var2.f};
      ac var4 = ac.a(18).a(var3).a();
      bh.a(var4);
   }
}
