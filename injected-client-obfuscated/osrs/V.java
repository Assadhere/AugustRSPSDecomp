package osrs;

public class V {
   public int a = -1;
   public int b = -1;
   public int c = -1;
   public int d = -1;
   public int e = -1;
   public int f = -1;
   public final int g = 1;
   public final int h = 2;
   public final int i = 1;
   public final int j = 4;
   public final int k = 1;
   public final int l = 1;
   public final int m = 1;
   public final int[][] n = new int[3][5];
   public int o = -1;
   public int p = -1;
   public int q = -1;
   public int r = -1;
   public int s = -1;
   public int t = -1;
   public int u = -1;

   public void a(au var1) {
      byte[] var2 = var1.d(G.b.c);
      aR var3 = new aR(var2);

      while(true) {
         while(true) {
            int var4 = var3.b();
            if (var4 == 0) {
               return;
            }

            switch (var4) {
               case 1:
                  var3.f();
                  break;
               case 2:
                  this.o = var3.u();
                  this.p = var3.u();
                  this.q = var3.u();
                  this.r = var3.u();
                  this.s = var3.u();
                  this.t = var3.u();
                  this.u = var3.u();
                  this.a = var3.u();
                  this.b = var3.u();
                  this.c = var3.u();
                  this.d = var3.u();
                  break;
               case 3:
                  for(int var5 = 0; var5 < this.n.length; ++var5) {
                     for(int var6 = 0; var6 < this.n[var5].length; ++var6) {
                        this.n[var5][var6] = var3.f();
                     }
                  }
                  break;
               case 4:
                  this.e = var3.u();
                  this.f = var3.u();
                  break;
               case 5:
                  this.e = var3.a(kn.a.b);
                  this.f = var3.a(kn.a.b);
            }
         }
      }
   }
}
