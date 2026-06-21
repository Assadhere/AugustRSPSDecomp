package osrs;

import java.util.Iterator;
import net.runelite.api.Deque;

public class fJ implements Deque {
   public az a = new az();
   public az b;

   public fJ() {
      this.a.cn = this.a;
      this.a.co = this.a;
   }

   public static void a(az var0, az var1) {
      if (var0.co != null) {
         var0.X();
      }

      var0.co = var1.co;
      var0.cn = var1;
      var0.co.cn = var0;
      var0.cn.co = var0;
   }

   public void a() {
      while(true) {
         az var1 = this.a.cn;
         if (this.a == var1) {
            this.b = null;
            return;
         }

         var1.X();
      }
   }

   public void a(az var1) {
      if (var1.co != null) {
         var1.X();
      }

      var1.co = this.a.co;
      var1.cn = this.a;
      var1.co.cn = var1;
      var1.cn.co = var1;
   }

   public void b(az var1) {
      if (var1.co != null) {
         var1.X();
      }

      var1.co = this.a;
      var1.cn = this.a.cn;
      var1.co.cn = var1;
      var1.cn.co = var1;
   }

   public az b() {
      az var1 = this.a.cn;
      if (this.a == var1) {
         return null;
      } else {
         var1.X();
         return var1;
      }
   }

   public az c() {
      az var1 = this.a.co;
      if (this.a == var1) {
         return null;
      } else {
         var1.X();
         return var1;
      }
   }

   public az d() {
      az var1 = this.a.cn;
      if (this.a == var1) {
         this.b = null;
         return null;
      } else {
         this.b = var1.cn;
         return var1;
      }
   }

   public az e() {
      az var1 = this.a.co;
      if (this.a == var1) {
         this.b = null;
         return null;
      } else {
         this.b = var1.co;
         return var1;
      }
   }

   public az f() {
      az var1 = this.b;
      if (this.a == var1) {
         this.b = null;
         return null;
      } else {
         this.b = var1.cn;
         return var1;
      }
   }

   public az g() {
      az var1 = this.b;
      if (this.a == var1) {
         this.b = null;
         return null;
      } else {
         this.b = var1.co;
         return var1;
      }
   }

   public void addLast(Object var1) {
      this.c((az)var1);
   }

   public void a(fJ var1) {
      az var2 = this.a;
      az var3 = this.b;
      this.a = var1.a;
      this.b = var1.b;
      var1.a = var2;
      var1.b = var3;
   }

   public Iterator iterator() {
      return new iM(this);
   }

   public void c(az var1) {
      this.a(var1);
   }

   public az h() {
      return this.f();
   }

   public az i() {
      return this.e();
   }

   public az j() {
      return this.d();
   }

   public void clear() {
      this.a();
   }

   public az k() {
      return this.b();
   }
}
