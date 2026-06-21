package osrs;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class eZ {
   public static final List a = new ArrayList();
   public static ArrayList b = null;
   public static LinkedList c = new LinkedList();
   public static ArrayList d = new ArrayList(3);
   public static ArrayList e = new ArrayList(3);
   public static ArrayList f = new ArrayList();
   public static int g = 0;
   public static int h = 0;
   public static int i = 0;
   public static int j = 0;

   public static boolean a(au var0, au var1, au var2, ArrayList var3) {
      bo.cr = var0;
      bo.dJ = var1;
      bo.bQ = var2;
      b = var3;
      return true;
   }

   public static void a(eU var0) {
      if (!a.contains(var0)) {
         a.add(var0);
      }

   }

   public static void b(eU var0) {
      a.remove(var0);
   }

   public static void a(ArrayList var0, int var1, int var2, int var3, int var4, boolean var5) {
      if (!var0.isEmpty()) {
         c.clear();
         f.clear();
         if (var5) {
            c();
         } else {
            for(int var6 = 0; var6 < d.size(); ++var6) {
               eX var7 = (eX)d.get(var6);
               if (var7 == null) {
                  d.remove(var6);
                  --var6;
               } else if (var7.g) {
                  if (var7.j.x > 0) {
                     --var7.j.x;
                  }

                  var7.j.e();
                  var7.j.d();
                  var7.j.a(0);
                  d.remove(var6);
                  --var6;
               } else {
                  var7.g = true;
               }
            }
         }

         a(var0, var5);
         if (!c.isEmpty()) {
            g = var1;
            h = var2;
            i = var3;
            j = var4;
            f.add(new gv((gn)null));
            f.add(new gp((gn)null, bo.cr, bo.dJ, bo.bQ));
            ArrayList var12 = new ArrayList();
            var12.add(new gz(new gy((gn)null, 0, true, j)));
            if (!d.isEmpty()) {
               ArrayList var11 = new ArrayList();
               var11.add(new gt(new gs((gn)null, var12), i));
               ArrayList var8 = new ArrayList();
               Iterator var9 = d.iterator();

               while(var9.hasNext()) {
                  eX var10 = (eX)var9.next();
                  var8.add(var10);
               }

               var11.add(new gt(new gA(new gx((gn)null, var8), 0, false, h), g));
               f.add(new gs((gn)null, var11));
            } else {
               f.add(new gt((gn)null, i));
               f.add(new gs((gn)null, var12));
            }
         }
      }

   }

   public static void a(int var0, int var1) {
      g = var0;
      h = var1;
      i = 0;
      j = 0;
      e.clear();
      f.clear();
      if (d.isEmpty() || var0 == 0 && var1 == 0) {
         c();
      } else {
         f.add(new gt((gn)null, g));
         f.add(new gA((gn)null, 0, false, h));
         ArrayList var2 = new ArrayList();
         Iterator var3 = d.iterator();

         while(var3.hasNext()) {
            eX var4 = (eX)var3.next();
            var2.add(var4);
         }

         f.add(new gx((gn)null, var2));
      }

   }

   public static boolean a() {
      return !e.isEmpty();
   }

   public static void b() {
      e.clear();
   }

   public static void a(au var0, int var1) {
      if (!e.isEmpty()) {
         ArrayList var2 = new ArrayList();
         Iterator var3 = e.iterator();

         while(var3.hasNext()) {
            eX var4 = (eX)var3.next();
            var4.n = false;
            var4.l = false;
            var4.f = false;
            var4.g = false;
            var4.k = var0;
            var4.c = var1;
            var4.d = 0.0F;
            var2.add(var4);
         }

         a(var2, g, h, i, j, false);
      }

   }

   public static void a(ArrayList var0, boolean var1) {
      if (!var1) {
         e.clear();
      }

      Iterator var2 = var0.iterator();

      while(var2.hasNext()) {
         eX var3 = (eX)var2.next();
         if (var3.a != -1 && var3.b != -1) {
            if (!var1) {
               e.add(var3);
            }

            c.add(var3);
         }
      }

   }

   public static void c() {
      Iterator var0 = d.iterator();

      while(true) {
         eX var1;
         do {
            if (!var0.hasNext()) {
               d.clear();
               return;
            }

            var1 = (eX)var0.next();
         } while(var1 == null);

         var1.j.e();
         var1.j.d();
         var1.j.a(0);
         var1.j.x = 0;
         int var2 = var1.a;
         int var3 = var1.b;
         Iterator var4 = a.iterator();

         while(var4.hasNext()) {
            eU var5 = (eU)var4.next();
            var5.l(var2, var3);
         }
      }
   }
}
