package osrs;

import net.runelite.api.ChatPlayer;

public class gP extends gH implements ChatPlayer {
   public int e = -1;
   public int f;
   public int g;

   public void a(int var1, int var2) {
      this.e = var1;
      this.f = var2;
   }

   public int f() {
      return this.e;
   }

   public boolean g() {
      return this.e > 0;
   }

   public int getWorld() {
      return this.e;
   }

   public int h() {
      return this.g;
   }
}
