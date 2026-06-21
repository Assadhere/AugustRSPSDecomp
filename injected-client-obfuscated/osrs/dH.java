package osrs;

public class dH {
   public byte a = -1;
   public byte b = -1;
   public byte c = -1;
   public byte d = 0;

   public dH() {
   }

   public dH(byte var1, byte var2, byte var3, byte var4) {
      this.a = var1;
      this.b = var2;
      this.c = var3;
      this.d = var4;
   }

   public void a(dH var1) {
      this.a = var1.a;
      this.b = var1.b;
      this.c = var1.c;
      this.d = var1.d;
   }

   public void a(byte var1, byte var2, byte var3, byte var4) {
      this.a = var1;
      this.b = var2;
      this.c = var3;
      this.d = var4;
   }

   public void a() {
      this.a = -1;
      this.b = -1;
      this.c = -1;
      this.d = 0;
   }

   public boolean b() {
      return this.d > 0;
   }

   public void b(dH var1) {
      this.a(var1);
   }
}
