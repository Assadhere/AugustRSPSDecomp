package osrs;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public final class gJ {
   public final Comparator a;
   public final long b;
   public final int c;
   public final gm d;
   public final Map e;
   public final gj f;
   public final gj g;

   public gJ(int var1, gm var2) {
      this(-1L, var1, var2);
   }

   public gJ(long var1, int var3, gm var4) {
      this.a = new a();
      this.b = var1;
      this.c = var3;
      this.d = var4;
      if (this.c == -1) {
         this.e = new HashMap(64);
         this.f = new gj(64, this.a);
         this.g = null;
      } else {
         if (this.d == null) {
            throw new IllegalArgumentException("");
         }

         this.e = new HashMap(this.c);
         this.f = new gj(this.c, this.a);
         this.g = new gj(this.c);
      }

   }

   public boolean a() {
      return this.c != -1;
   }

   public Object a(Object var1) {
      synchronized(this) {
         if (this.b != -1L) {
            this.b();
         }

         gI var3 = (gI)this.e.get(var1);
         if (var3 == null) {
            return null;
         } else {
            this.a(var3, false);
            return var3.a;
         }
      }
   }

   public Object a(Object var1, Object var2) {
      synchronized(this) {
         if (this.b != -1L) {
            this.b();
         }

         gI var4 = (gI)this.e.get(var1);
         if (var4 != null) {
            Object var8 = var4.a;
            var4.a = var2;
            this.a(var4, false);
            return var8;
         } else {
            gI var5;
            if (this.a() && this.e.size() == this.c) {
               var5 = (gI)this.g.remove();
               this.e.remove(var5.b);
               this.f.remove(var5);
            }

            var5 = new gI(var2, var1);
            this.e.put(var1, var5);
            this.a(var5, true);
            return null;
         }
      }
   }

   public void a(gI var1, boolean var2) {
      if (!var2) {
         this.f.remove(var1);
         if (this.a() && !this.g.remove(var1)) {
            throw new IllegalStateException("");
         }
      }

      var1.d = System.currentTimeMillis();
      if (this.a()) {
         switch (this.d.c) {
            case 0:
               var1.c = var1.d;
               break;
            case 1:
               ++var1.c;
         }

         this.g.add(var1);
      }

      this.f.add(var1);
   }

   public void b() {
      if (this.b == -1L) {
         throw new IllegalStateException("");
      } else {
         long var1 = System.currentTimeMillis() - this.b;

         while(!this.f.isEmpty()) {
            gI var3 = (gI)this.f.peek();
            if (var3.d >= var1) {
               return;
            }

            this.e.remove(var3.b);
            this.f.remove(var3);
            if (this.a()) {
               this.g.remove(var3);
            }
         }

      }
   }

   public void c() {
      synchronized(this) {
         this.e.clear();
         this.f.clear();
         if (this.a()) {
            this.g.clear();
         }

      }
   }

   class a implements Comparator {
      public a() {
      }

      public int a(gI var1, gI var2) {
         if (var1.d > var2.d) {
            return 1;
         } else {
            return var1.d < var2.d ? -1 : 0;
         }
      }

      public int compare(Object var1, Object var2) {
         return this.a((gI)var1, (gI)var2);
      }

      public boolean equals(Object var1) {
         return super.equals(var1);
      }
   }
}
