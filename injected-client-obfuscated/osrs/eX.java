package osrs;

import net.runelite.api.MidiRequest;

public class eX implements MidiRequest {
   public int a = -1;
   public int b = -1;
   public int c = 0;
   public float d = 0.0F;
   public boolean e = false;
   public boolean f = false;
   public boolean g = false;
   public iJ h;
   public eT i;
   public aG j;
   public au k;
   public boolean l;
   public boolean m;
   public boolean n;

   public eX(au var1, String var2, String var3, int var4, boolean var5) {
      this.a = var1.a(var2);
      this.b = var1.a(this.a, var3);
      this.a(var1, this.a, this.b, var4, var5);
   }

   public eX(au var1, int var2, int var3, int var4, boolean var5) {
      this.a(var1, var2, var3, var4, var5);
   }

   public void a(au var1, int var2, int var3, int var4, boolean var5) {
      this.b(var1, var2, var3, var4, var5);
      this.k = var1;
      this.a = var2;
      this.b = var3;
      this.c = var4;
      this.e = var5;
   }

   public void b(au var1, int var2, int var3, int var4, boolean var5) {
      av var6 = (av)var1;
      this.m = var6.h() == 11;
   }

   public boolean isJingle() {
      return this.m;
   }

   public int getArchiveId() {
      return this.a;
   }
}
