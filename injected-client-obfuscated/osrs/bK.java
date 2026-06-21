package osrs;

import java.util.Iterator;
import java.util.LinkedList;
import net.runelite.api.WorldMapData;

public class bK implements WorldMapData {
   public int d = -1;
   public int e = -1;
   public int f = -16777216;
   public int g = -1;
   public B h = null;
   public int i = Integer.MAX_VALUE;
   public int j = 0;
   public int k = Integer.MAX_VALUE;
   public int l = 0;
   public boolean m = false;
   public String n;
   public String o;
   public LinkedList p;

   public void a(aR var1, int var2) {
      this.d = var2;
      this.o = var1.m();
      this.n = var1.m();
      this.h = new B(var1.h());
      this.e = var1.h();
      this.f = var1.h();
      var1.b();
      this.m = var1.b() == 1;
      this.g = var1.b();
      int var3 = var1.b();
      this.p = new LinkedList();

      for(int var4 = 0; var4 < var3; ++var4) {
         this.p.add(this.a(var1));
      }

      this.a();
   }

   public ez a(aR var1) {
      int var2 = var1.b();
      ei var3 = (ei)kk.a(ei.a(), var2);
      Object var4 = null;
      Object var5;
      switch (var3.e) {
         case 0:
            var5 = new eu();
            break;
         case 1:
            var5 = new es();
            break;
         case 2:
            var5 = new eh();
            break;
         case 3:
            var5 = new ep();
            break;
         default:
            throw new IllegalStateException("");
      }

      ((ez)var5).a(var1);
      return (ez)var5;
   }

   public boolean a(int var1, int var2, int var3) {
      Iterator var4 = this.p.iterator();

      while(var4.hasNext()) {
         ez var5 = (ez)var4.next();
         if (var5.a(var1, var2, var3)) {
            return true;
         }
      }

      return false;
   }

   public boolean a(int var1, int var2) {
      int var3 = var1 / 64;
      int var4 = var2 / 64;
      if (var3 >= this.i && var3 <= this.j) {
         if (var4 >= this.k && var4 <= this.l) {
            Iterator var5 = this.p.iterator();

            while(var5.hasNext()) {
               ez var6 = (ez)var5.next();
               if (var6.a(var1, var2)) {
                  return true;
               }
            }

            return false;
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   public int[] b(int var1, int var2, int var3) {
      Iterator var4 = this.p.iterator();

      while(var4.hasNext()) {
         ez var5 = (ez)var4.next();
         if (var5.a(var1, var2, var3)) {
            return var5.b(var1, var2, var3);
         }
      }

      return null;
   }

   public B b(int var1, int var2) {
      Iterator var3 = this.p.iterator();

      while(var3.hasNext()) {
         ez var4 = (ez)var3.next();
         if (var4.a(var1, var2)) {
            return var4.b(var1, var2);
         }
      }

      return null;
   }

   public void a() {
      Iterator var1 = this.p.iterator();

      while(var1.hasNext()) {
         ez var2 = (ez)var1.next();
         var2.a(this);
      }

   }

   public int b() {
      return this.d;
   }

   public boolean c() {
      return this.m;
   }

   public String d() {
      return this.o;
   }

   public String e() {
      return this.n;
   }

   public int f() {
      return this.e;
   }

   public int g() {
      return this.f;
   }

   public int h() {
      return this.g;
   }

   public int i() {
      return this.i;
   }

   public int j() {
      return this.j;
   }

   public int k() {
      return this.k;
   }

   public int l() {
      return this.l;
   }

   public int m() {
      return this.h.b;
   }

   public int n() {
      return this.h.a;
   }

   public int o() {
      return this.h.c;
   }

   public B p() {
      return new B(this.h);
   }

   public boolean surfaceContainsPosition(int var1, int var2) {
      return this.a(var1, var2);
   }
}
