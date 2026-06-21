package osrs;

import java.util.ArrayList;
import java.util.Iterator;

public class hG implements gw {
   public final hm a;
   public int b;
   public int c;
   public bG d;
   public boolean e;

   public hG(hm var1) {
      this.a = var1;
   }

   public void a(bG var1, int var2, int var3, boolean var4) {
      this.d = var1;
      this.b = var2;
      this.c = var3;
      this.e = var4;
   }

   public void a() {
      this.d = null;
   }

   public void a(bk var1, int var2) {
      if (this.d != null && !this.a.c() && var1.j != null && var1.j.containsKey(var2)) {
         ArrayList var3 = (ArrayList)var1.j.get(var2);
         if (!var3.isEmpty()) {
            int var4 = 0;
            int var6;
            if (var3.size() > 1) {
               int var5 = 1 + (int)(Math.random() * 100.0);
               var6 = 0;

               for(Iterator var7 = var3.iterator(); var7.hasNext(); ++var4) {
                  di var8 = (di)var7.next();
                  int var9 = var6;
                  var6 += var8.b;
                  if (var9 <= var5 && var5 < var6) {
                     break;
                  }
               }

               if (var4 >= var3.size()) {
                  return;
               }
            }

            di var10 = (di)var3.get(var4);
            if (var10 != null) {
               if (var10.d == 0) {
                  if (!this.e) {
                     return;
                  }

                  this.a.a(this.d.o, var10.a, 0, 0, 0, var10.e, var10.c, 0, var1.u);
               } else {
                  var6 = bo.e(this.b - 64);
                  int var11 = bo.e(this.c - 64);
                  this.a.a(this.d.o, var10.a, var6, var11, var10.d, var10.e, var10.c, 0, var1.u);
               }
            }
         }
      }

   }

   public void b(bG var1, int var2, int var3, boolean var4) {
      this.a(var1, var2, var3, var4);
   }

   public void b() {
      this.a();
   }
}
