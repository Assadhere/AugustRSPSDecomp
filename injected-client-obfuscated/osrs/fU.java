package osrs;

import java.util.Collection;
import java.util.Iterator;

public class fU implements Iterable, Collection {
   public az a = new az();
   public az b;

   public fU() {
      this.a.cn = this.a;
      this.a.co = this.a;
   }

   public static void a(az var0, az var1) {
      if (var0.co != null) {
         var0.X();
      }

      var0.co = var1;
      var0.cn = var1.cn;
      var0.co.cn = var0;
      var0.cn.co = var0;
   }

   public void a() {
      while(this.a.cn != this.a) {
         this.a.cn.X();
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
      return this.c((az)null);
   }

   public az c(az var1) {
      az var2;
      if (var1 == null) {
         var2 = this.a.cn;
      } else {
         var2 = var1;
      }

      if (this.a == var2) {
         this.b = null;
         return null;
      } else {
         this.b = var2.cn;
         return var2;
      }
   }

   public az d() {
      az var1 = this.b;
      if (this.a == var1) {
         this.b = null;
         return null;
      } else {
         this.b = var1.cn;
         return var1;
      }
   }

   public int e() {
      int var1 = 0;

      for(az var2 = this.a.cn; this.a != var2; var2 = var2.cn) {
         ++var1;
      }

      return var1;
   }

   public boolean f() {
      return this.a.cn == this.a;
   }

   public az[] g() {
      az[] var1 = new az[this.e()];
      int var2 = 0;

      for(az var3 = this.a.cn; this.a != var3; var3 = var3.cn) {
         var1[var2++] = var3;
      }

      return var1;
   }

   public Iterator iterator() {
      return new fR(this);
   }

   public int size() {
      return this.e();
   }

   public boolean isEmpty() {
      return this.f();
   }

   public boolean contains(Object var1) {
      throw new RuntimeException();
   }

   public Object[] toArray() {
      return this.g();
   }

   public Object[] toArray(Object[] var1) {
      int var2 = 0;

      for(az var3 = this.a.cn; this.a != var3; var3 = var3.cn) {
         var1[var2++] = var3;
      }

      return var1;
   }

   public boolean d(az var1) {
      this.a(var1);
      return true;
   }

   public boolean remove(Object var1) {
      throw new RuntimeException();
   }

   public boolean containsAll(Collection var1) {
      throw new RuntimeException();
   }

   public boolean addAll(Collection var1) {
      throw new RuntimeException();
   }

   public boolean removeAll(Collection var1) {
      throw new RuntimeException();
   }

   public boolean retainAll(Collection var1) {
      throw new RuntimeException();
   }

   public void clear() {
      this.a();
   }

   public boolean add(Object var1) {
      return this.d((az)var1);
   }

   public boolean equals(Object var1) {
      return super.equals(var1);
   }

   public int hashCode() {
      return super.hashCode();
   }

   public az h() {
      return this.c();
   }

   public az i() {
      return this.d();
   }
}
