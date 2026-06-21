package osrs;

import java.math.BigInteger;

public class jW {
   public static final BigInteger a = new BigInteger("10001", 16);
   public static final BigInteger b = new BigInteger("add8ec653ce553406f0b34e3ba74449f933f67f418317e198b1d9f66c6b056eb2731c54e546cc0b2540ab7a199bdd13824a85c645aa2fcc9d68f0b8e0ef6d1b8afa9de387bbe37bc1444771d5a41e88ec355590e027e06509936faa43ac64a1b93ef7e5cff80093e590820d12c4495ff0c80930aee2e668027855287d64d99ad78bca216da297249b825cf1beca8e0fe25571ff724079a422ebf42e85d2965860fc72af9fdbd002540deb6b6c74ea9f9bb3afacf0b987fd1f103d34686ce899021e3d684adf60d526f5b6edcbabeaf48925b1ccfa8bb3bc6f696691a702b52a32af01ffeeebdc7996c5bcca51d4972a8fdb083325a93a6cfae3ed5e1fef07ebb", 16);

   public static final void a(ko var0, int var1, int var2, boolean var3, byte var4) {
      if (var3 || bo.aZ != var1 || bo.eq != var2) {
         if (!Client.a && bo.eX != null) {
            throw new AssertionError();
         }

         if (bo.eX != null) {
            Client.dF.error("Loading map {},{},{} while another map load is in progress!", new Object[]{var1, var2, var3});
            bo.eX.g();
            Client.a("concurrent map load", (Throwable)null);
         }

         jc var5 = new jc(Client.ef, bo.fh, bo.aQ, Client.dr);
         var5.H = (var1 - 6) * 8;
         var5.l = (var2 - 6) * 8;
         var5.n = var1;
         var5.J = var2;
         var5.I = bo.U.y;
         if (!Client.a && bo.fZ) {
            throw new AssertionError();
         }

         if (Client.w != 30) {
            Client.b(var5);
            if (!Client.a && Client.w != 25) {
               throw new AssertionError();
            }

            bo.fu = null;
         } else {
            Client.dr = bo.fw;
            bo.fZ = true;
            bo.U.y = bo.U.A;
            if (!Client.a && bo.fu != null) {
               throw new AssertionError();
            }
         }

         bo.eX = var5;
         var5.e();
      }

   }
}
