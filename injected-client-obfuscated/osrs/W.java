package osrs;

import java.util.Iterator;
import net.runelite.api.IndexedObjectSet;
import net.runelite.api.IterableHashTable;
import net.runelite.api.Node;

public final class W implements Iterable, IndexedObjectSet, IterableHashTable {
   public int a = 0;
   public int b;
   public az[] c;
   public az d;
   public az e;

   public W(int var1) {
      this.b = var1;
      this.c = new az[var1];

      for(int var2 = 0; var2 < var1; ++var2) {
         az var3 = this.c[var2] = new az();
         var3.cn = var3;
         var3.co = var3;
      }

   }

   public void a(az var1, long var2) {
      if (var1.co != null) {
         var1.X();
      }

      az var4 = this.c[(int)(var2 & (long)(this.b - 1))];
      var1.co = var4.co;
      var1.cn = var4;
      var1.co.cn = var1;
      var1.cn.co = var1;
      var1.cm = var2;
   }

   public void a() {
      for(int var1 = 0; var1 < this.b; ++var1) {
         az var2 = this.c[var1];

         while(true) {
            az var3 = var2.cn;
            if (var2 == var3) {
               break;
            }

            var3.X();
         }
      }

      this.d = null;
      this.e = null;
   }

   public az b() {
      this.a = 0;
      return this.c();
   }

   public az c() {
      az var1;
      if (this.a > 0 && this.c[this.a - 1] != this.e) {
         var1 = this.e;
         this.e = var1.cn;
         return var1;
      } else {
         while(this.a < this.b) {
            var1 = this.c[this.a++].cn;
            if (this.c[this.a - 1] != var1) {
               this.e = var1.cn;
               return var1;
            }
         }

         return null;
      }
   }

   public Iterator iterator() {
      return new fN(this);
   }

   public Object byIndex(int var1) {
      return this.a(var1);
   }

   public az a(long var1) {
      az var3 = this.c[(int)(var1 & (long)(this.b - 1))];

      for(az var4 = var3.cn; var3 != var4; var4 = var4.cn) {
         if (var4.cm == var1) {
            this.d = var4;
            return var4;
         }
      }

      this.d = null;
      return null;
   }

   public void b(az var1, long var2) {
      az var4 = this.b(var2);
      if (var4 != null) {
         var4.ad();
      }

      this.c(var1, var2);
   }

   public Node get(long var1) {
      return this.b(var1);
   }

   public void put(Node var1, long var2) {
      this.b((az)var1, var2);
   }

   public az a(int var1) {
      return this.b((long)var1);
   }

   public void c(az var1, long var2) {
      this.a(var1, var2);
   }

   public az b(long var1) {
      return this.a(var1);
   }

   public void d() {
      this.a();
   }
}
