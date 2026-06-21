package osrs;

public class aK {
   public int[] a = new int[8];
   public short[] b = new short[8];

   public aK(aN var1) {
      int var2 = 0;
      if (var1.e()) {
         var2 = var1.f().length;
         System.arraycopy(var1.f(), 0, this.a, 0, var2);
         System.arraycopy(var1.g(), 0, this.b, 0, var2);
      }

      for(int var3 = var2; var3 < 8; ++var3) {
         this.a[var3] = -1;
         this.b[var3] = -1;
      }

   }

   public int[] a() {
      return this.a;
   }

   public short[] b() {
      return this.b;
   }

   public void a(int var1, int var2, short var3) {
      this.a[var1] = var2;
      this.b[var1] = var3;
   }

   public void a(int[] var1, short[] var2) {
      this.a = var1;
      this.b = var2;
   }
}
