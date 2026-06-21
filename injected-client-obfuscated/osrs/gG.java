package osrs;

import net.runelite.api.ActorSpotAnim;

public class gG extends az implements ActorSpotAnim {
   public int a = -1;
   public final gq b = new gq();
   public int c = 0;
   public int d = 0;

   public gG(int var1, int var2, int var3) {
      this.a = var1;
      this.d = var2;
      this.c = var3;
      if (this.a >= 0) {
         this.b.a(bn.b(this.a).b);
      }

   }

   public void setFrame(int var1) {
      this.b.c = var1;
   }

   public int getFrame() {
      return this.b.c;
   }

   public static void a(String var0, Throwable var1, short var2) {
      Throwable var3 = var1;
      if (var1 instanceof ik && "".equals(var1.getMessage())) {
         var3 = var1.getCause();
      }

      if (var0 == null) {
         Client.dF.error("Client error", var3);
      } else {
         Client.dF.error("Client error: {}", var0, var3);
      }

      Client.s.getCallbacks().error(var0, var3);
   }

   public void setCycle(int var1) {
      this.b.g = var1;
   }

   public int getCycle() {
      return this.b.g;
   }

   public int getStartCycle() {
      return this.c;
   }

   public void setStartCycle(int var1) {
      this.c = var1;
   }

   public int getId() {
      return this.a;
   }

   public void setId(int var1) {
      this.a = var1;
   }

   public void setHeight(int var1) {
      this.d = var1;
   }

   public int getHeight() {
      return this.d;
   }
}
