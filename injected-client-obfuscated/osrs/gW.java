package osrs;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import net.runelite.api.Nameable;
import net.runelite.api.NameableContainer;

public abstract class gW<T extends Nameable> implements NameableContainer<T> {
   public int h = 0;
   public Comparator i = null;
   public final int j;
   public gH[] k;
   public HashMap l;
   public HashMap m;

   public gW(int var1) {
      this.j = var1;
      this.k = this.a(var1);
      this.l = new HashMap(var1 / 8);
      this.m = new HashMap(var1 / 8);
   }

   public void f() {
      this.h = 0;
      Arrays.fill(this.k, (Object)null);
      this.l.clear();
      this.m.clear();
   }

   public int g() {
      return this.h;
   }

   public boolean h() {
      return this.j == this.h;
   }

   public boolean a(I var1) {
      if (!var1.c()) {
         return false;
      } else {
         return this.l.containsKey(var1) ? true : this.m.containsKey(var1);
      }
   }

   public gH b(I var1) {
      gH var2 = this.c(var1);
      return var2 != null ? var2 : this.d(var1);
   }

   public gH c(I var1) {
      return !var1.c() ? null : (gH)this.l.get(var1);
   }

   public gH d(I var1) {
      return !var1.c() ? null : (gH)this.m.get(var1);
   }

   public boolean e(I var1) {
      gH var2 = this.c(var1);
      if (var2 == null) {
         return false;
      } else {
         this.b(var2);
         return true;
      }
   }

   public final void b(gH var1) {
      this.g(var1);
      int var2 = this.c(var1);
      if (var2 != -1) {
         this.c(var2);
         this.d(var1);
      }

   }

   public gH f(I var1) {
      return this.b(var1, (I)null);
   }

   public gH b(I var1, I var2) {
      if (this.c(var1) != null) {
         throw new IllegalStateException();
      } else {
         gH var3 = this.a();
         var3.a(var1, var2);
         this.e(var3);
         this.f(var3);
         this.c(var1, var2);
         return var3;
      }
   }

   public final gH b(int var1) {
      if (var1 >= 0 && var1 < this.h) {
         return this.k[var1];
      } else {
         throw new ArrayIndexOutOfBoundsException(var1);
      }
   }

   public final void i() {
      if (this.i == null) {
         Arrays.sort(this.k, 0, this.h);
      } else {
         Arrays.sort(this.k, 0, this.h, this.i);
      }

   }

   public final void a(gH var1, I var2, I var3) {
      this.d(var1);
      var1.a(var2, var3);
      this.f(var1);
   }

   public final int c(gH var1) {
      for(int var2 = 0; var2 < this.h; ++var2) {
         if (this.k[var2] == var1) {
            return var2;
         }
      }

      return -1;
   }

   public void d(gH var1) {
      if (this.l.remove(var1.b) == null) {
         throw new IllegalStateException();
      } else {
         if (var1.a != null) {
            this.m.remove(var1.a);
         }

      }
   }

   public final void e(gH var1) {
      this.k[++this.h - 1] = var1;
   }

   public final void f(gH var1) {
      this.l.put(var1.b, var1);
      if (var1.a != null) {
         gH var2 = (gH)this.m.put(var1.a, var1);
         if (var2 != null && var1 != var2) {
            var2.a = null;
            var2.a(-1);
         }
      }

   }

   public void c(int var1) {
      --this.h;
      if (var1 < this.h) {
         System.arraycopy(this.k, var1 + 1, this.k, var1, this.h - var1);
      }

   }

   public final void j() {
      this.i = null;
   }

   public void a(Comparator var1) {
      if (this.i == null) {
         this.i = var1;
      } else if (this.i instanceof gN) {
         ((gN)this.i).a(var1);
      }

   }

   public void a(I var1, I var2) {
   }

   public void g(gH var1) {
      this.a(var1);
   }

   public void c(I var1, I var2) {
      this.a(var1, var2);
   }

   public T findByName(String var1) {
      return this.g(new I(var1, bo.cp));
   }

   public T[] getMembers() {
      gH[] var1 = this.k();
      int var2 = this.getCount();
      return (Nameable[])Arrays.copyOf(var1, var2);
   }

   public void a(gH var1) {
   }

   public gH[] k() {
      return this.k;
   }

   public gH g(I var1) {
      return this.c(var1);
   }

   public abstract gH[] a(int var1);

   public abstract gH a();

   public int getSize() {
      return this.j;
   }

   public int getCount() {
      return this.h;
   }
}
