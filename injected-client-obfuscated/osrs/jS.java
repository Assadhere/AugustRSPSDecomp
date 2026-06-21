package osrs;

public class jS {
   public int a = -1;
   public v b;
   public int[] c;
   public long[] d;
   public Object[] e;

   public static final void a(ag var0, int var1, int var2, byte var3) {
      ar var4 = var0.b(bo.cR, false);
      if (var4 != null) {
         if (eM.g != 2 && eM.g != 5) {
            int var5 = Client.br & 2047;

            try {
               if (bo.eV == null) {
                  bo.eV = Client.p();
               }

               boolean var6 = !bo.i && Client.B == Client.k;
               if (var6) {
                  df.g(var1, var2, var1 + var4.a(), var2 + var4.b());
                  int var7 = Client.cy / 32 + 48;
                  int var8 = 464 - Client.cA / 32;
                  bo.K.b(var1, var2, var4.a(), var4.b(), var7, var8, var5, 256, var1, var2, var4.d(), var4.c());
                  Client.a(var0, var1, var2, 0.03125F);
               } else {
                  double var19 = Client.dE;
                  double var9 = var19 / 128.0;
                  int var11 = Math.max(var4.a(), var4.b()) + 16;
                  if (bo.fK == null || bo.fK.getWidth() != var11) {
                     bo.fK = new aV(var11, var11);
                  }

                  int var12 = (int)((double)(var11 / 2) / var9);
                  int var13 = Client.cy - var12;
                  int var14 = Client.cA - var12;
                  int var15 = Math.max(-bo.aQ.x.s, var13 >> 7);
                  int var16 = Math.max(-bo.aQ.x.s, var14 >> 7);
                  Client.a(bo.aQ, bo.fK, var19, bo.aQ.y, var15, var16, (int)((double)(-(var13 & 127)) * var9), (int)((double)(-(var14 & 127)) * var9));
                  bo.fK.b(var1, var2, var4.a(), var4.b(), var11 / 2, var11 / 2, var5, 256, var1, var2, var4.d(), var4.c());
                  float var17 = (float)Client.dE / 128.0F;
                  Client.a(var0, var1, var2, var17);
               }
            } catch (Exception var18) {
               Client.dF.debug("minimap", var18);
            }
         } else {
            df.g(var1, var2, var1 + var4.a(), var2 + var4.b());
            df.b(var1, var2, 0, var4.d(), var4.c());
         }
      }

   }
}
