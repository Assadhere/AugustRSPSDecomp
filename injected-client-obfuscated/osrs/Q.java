package osrs;

import net.runelite.api.PendingLogin;

public class Q extends ht implements PendingLogin {
   public int a = (int)(bd.a() / 1000L);
   public I b;
   public short c;

   public Q(I var1, int var2) {
      this.b = var1;
      this.c = (short)var2;
      this.a(var1, var2);
   }

   public String getName() {
      return this.a().d();
   }

   public void a(I var1, int var2) {
      this.a += 5;
   }

   public short getWorld() {
      return this.c;
   }

   public I a() {
      return this.b;
   }
}
