package osrs;

public class jB {
   public static final void a(int var0) {
      if (!Client.a && !Client.bO) {
         throw new AssertionError();
      } else {
         jc var1;
         if (bo.eX == null) {
            var1 = new jc(Client.ef, bo.fh, bo.aQ, Client.dr);
            var1.H = var1.o.B;
            var1.l = var1.o.z;
            var1.n = bo.aZ;
            var1.J = bo.eq;
            var1.I = bo.U.y;
            if (!Client.a && bo.fu != null) {
               throw new AssertionError();
            }

            bo.eX = var1;
            var1.e();
         } else if (bo.eX.f()) {
            var1 = bo.eX;
            if (!Client.a && bo.fu != null) {
               throw new AssertionError();
            }

            bo.eX = null;
            bo.fZ = false;
            bo.aQ.y = var1.I;
            Client.b(var1);
            Client.a(var1);
            if (!Client.a && Client.s.bL() != 25) {
               throw new AssertionError();
            }

            if (!Client.a && Client.bO) {
               throw new AssertionError();
            }

            if (Client.ef != var1.s && Client.ef != null) {
               Client.dF.info("Reloading due to draw callbacks change while loading!");
               Client.bO = true;
            }
         }

      }
   }
}
