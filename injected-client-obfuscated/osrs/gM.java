package osrs;

import net.runelite.api.Friend;

public class gM extends gP implements Friend {
   public boolean c;
   public boolean d;

   public int a(gM var1) {
      if (Client.u == this.e && Client.u != var1.e) {
         return -1;
      } else if (Client.u == var1.e && Client.u != this.e) {
         return 1;
      } else if (this.e != 0 && var1.e == 0) {
         return -1;
      } else if (var1.e != 0 && this.e == 0) {
         return 1;
      } else if (this.c && !var1.c) {
         return -1;
      } else if (!this.c && var1.c) {
         return 1;
      } else if (this.d && !var1.d) {
         return -1;
      } else if (!this.d && var1.d) {
         return 1;
      } else {
         return this.e != 0 ? this.f - var1.f : var1.f - this.f;
      }
   }

   public int a(gH var1) {
      return this.a((gM)var1);
   }
}
