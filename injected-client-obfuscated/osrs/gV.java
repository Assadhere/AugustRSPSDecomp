package osrs;

import net.runelite.api.FriendsChatMember;
import net.runelite.api.FriendsChatRank;

public class gV extends gP implements FriendsChatMember {
   public gQ c;
   public gQ d;

   public gV() {
      this.c = gQ.a;
      this.d = gQ.a;
   }

   public void i() {
      this.c = gQ.a;
   }

   public final boolean j() {
      if (gQ.a == this.c) {
         this.k();
      }

      return gQ.b == this.c;
   }

   public void k() {
      this.c = bn.l.c.a(this.b) ? gQ.b : gQ.c;
   }

   public void l() {
      this.d = gQ.a;
   }

   public final boolean m() {
      if (gQ.a == this.d) {
         this.n();
      }

      return gQ.b == this.d;
   }

   public void n() {
      this.d = bn.l.d.a(this.b) ? gQ.b : gQ.c;
   }

   public FriendsChatRank getRank() {
      return FriendsChatRank.valueOf(this.h());
   }
}
