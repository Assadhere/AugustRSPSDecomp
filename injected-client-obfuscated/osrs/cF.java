package osrs;

import net.runelite.api.ColorTextureOverride;

public class cF implements ColorTextureOverride {
   public int a = -1;
   public int b = -1;
   public int c = -1;
   public int d = -1;
   public short[] e;
   public short[] f;
   public int g;
   public aH h;

   public cF(int var1) {
      aO var2 = aO.a(var1);
      if (var2.d()) {
         this.e = new short[var2.n.length];
         System.arraycopy(var2.n, 0, this.e, 0, this.e.length);
      }

      if (var2.e()) {
         this.f = new short[var2.p.length];
         System.arraycopy(var2.p, 0, this.f, 0, this.f.length);
      }

      this.g = var2.j;
   }

   public boolean a() {
      return this.e != null;
   }

   public boolean b() {
      return this.f != null;
   }

   public boolean a(int var1) {
      return var1 == 0 && this.a != -1 || var1 == 1 && this.b != -1;
   }

   public boolean b(int var1) {
      return var1 == 0 && this.c != -1 || var1 == 1 && this.d != -1;
   }

   public int c(int var1) {
      return var1 == 0 ? this.a : this.b;
   }

   public int d(int var1) {
      return var1 == 0 ? this.c : this.d;
   }

   public short[] getTextureToReplaceWith() {
      return this.f;
   }

   public short[] getColorToReplaceWith() {
      return this.e;
   }
}
