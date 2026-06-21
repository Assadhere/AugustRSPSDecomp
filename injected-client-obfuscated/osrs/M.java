package osrs;

import net.runelite.api.EnumComposition;

public class M extends aA implements EnumComposition {
   public int a;
   public int[] b;
   public int[] c;
   public long[] d;
   public String[] e;
   public be f;
   public be g;
   public static eI h = new eI(64);
   public String i;
   public char j;
   public char k;
   public int l;
   public long m;
   public static au n;

   public M() {
      this.i = bv.k;
      this.a = 0;
   }

   public static void a(au var0) {
      n = var0;
   }

   public static M a(int var0) {
      M var1 = (M)h.a((long)var0);
      if (var1 != null) {
         return var1;
      } else {
         byte[] var2 = n.b(8, (int)var0);
         M var3 = new M();
         if (var2 != null) {
            var3.a(new aR(var2));
         }

         h.a(var3, (long)var0);
         return var3;
      }
   }

   public static void a() {
      h.a();
   }

   public void a(aR var1) {
      while(true) {
         int var2 = var1.b();
         if (var2 == 0) {
            return;
         }

         this.a(var1, var2);
      }
   }

   public void a(aR var1, int var2) {
      if (var2 == 1) {
         this.j = (char)var1.b();
      } else if (var2 == 2) {
         this.k = (char)var1.b();
      } else if (var2 == 3) {
         this.i = var1.m();
      } else if (var2 == 4) {
         this.l = var1.h();
      } else if (var2 == 8) {
         this.m = var1.i();
      } else {
         int var3;
         if (var2 == 5) {
            this.a = var1.d();
            this.b = new int[this.a];
            this.e = new String[this.a];

            for(var3 = 0; var3 < this.a; ++var3) {
               this.b[var3] = var1.h();
               this.e[var3] = var1.m();
            }
         } else if (var2 == 7) {
            this.a = var1.d();
            this.b = new int[this.a];
            this.d = new long[this.a];

            for(var3 = 0; var3 < this.a; ++var3) {
               this.b[var3] = var1.h();
               this.d[var3] = var1.i();
            }
         } else if (var2 == 6) {
            this.a = var1.d();
            this.b = new int[this.a];
            this.c = new int[this.a];

            for(var3 = 0; var3 < this.a; ++var3) {
               this.b[var3] = var1.h();
               this.c[var3] = var1.h();
            }
         }
      }

   }

   public int b() {
      return this.a;
   }

   public be c() {
      if (this.f == null) {
         int[] var1 = this.b;
         be var2 = new be(osrs.i.a, false);
         var2.e = var1;
         var2.h = var1.length;
         var2.c = var1.length;
         this.f = var2;
      }

      return this.f;
   }

   public be d() {
      if (this.g == null) {
         if (this.k == 's') {
            this.g = be.a(this.e);
         } else if (this.k == 207) {
            this.g = be.a(this.d);
         } else {
            int[] var1 = this.c;
            be var2 = new be(osrs.i.a, false);
            var2.e = var1;
            var2.h = var1.length;
            var2.c = var1.length;
            this.g = var2;
         }
      }

      return this.g;
   }

   public String b(int var1) {
      for(int var2 = 0; var2 < this.a; ++var2) {
         if (this.b[var2] == var1) {
            return this.e[var2];
         }
      }

      return this.i;
   }

   public int c(int var1) {
      for(int var2 = 0; var2 < this.a; ++var2) {
         if (this.b[var2] == var1) {
            return this.c[var2];
         }
      }

      return this.l;
   }

   public long d(int var1) {
      for(int var2 = 0; var2 < this.a; ++var2) {
         if (this.b[var2] == var1) {
            return this.d[var2];
         }
      }

      return this.m;
   }

   public boolean a(char var1) {
      return this.j == var1;
   }

   public boolean b(char var1) {
      return this.k == var1;
   }

   public int e(int var1) {
      int[] var2 = this.getKeys();
      if (var2 == null) {
         return -1;
      } else {
         for(int var3 = 0; var3 < var2.length; ++var3) {
            if (var2[var3] == var1) {
               return var3;
            }
         }

         return -1;
      }
   }

   public static ik a(Throwable var0, String var1) {
      ik var2;
      if (var0 instanceof ik) {
         var2 = (ik)var0;
      } else {
         var2 = new ik(var0, "");
      }

      if (!var1.endsWith("()")) {
         if (var2.a.length() != 0) {
            var2.a = var2.a + " ";
         }

         var2.a = var2.a + var1;
      }

      return var2;
   }

   public String getStringValue(int var1) {
      int var2 = this.e(var1);
      return var2 == -1 ? this.i : this.getStringVals()[var2];
   }

   public int getIntValue(int var1) {
      int var2 = this.e(var1);
      return var2 == -1 ? this.l : this.getIntVals()[var2];
   }

   public long getLongValue(int var1) {
      int var2 = this.e(var1);
      return var2 == -1 ? this.m : this.getLongVals()[var2];
   }

   public int size() {
      return this.a;
   }

   public int[] getKeys() {
      return this.b;
   }

   public long[] getLongVals() {
      return this.d;
   }

   public String[] getStringVals() {
      return this.e;
   }

   public int[] getIntVals() {
      return this.c;
   }
}
