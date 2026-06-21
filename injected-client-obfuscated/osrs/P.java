package osrs;

import net.runelite.api.FriendsChatManager;
import net.runelite.api.FriendsChatMember;
import net.runelite.api.FriendsChatRank;
import net.runelite.api.events.FriendsChatMemberJoined;
import net.runelite.api.events.FriendsChatMemberLeft;

public class P extends gW<FriendsChatMember> implements FriendsChatManager {
   public String a = null;
   public String b = null;
   public int c = 1;
   public final aM d;
   public final gS e;
   public byte f;
   public int g;

   public P(aM var1, gS var2) {
      super(500);
      this.d = var1;
      this.e = var2;
   }

   public gH a() {
      return new gV();
   }

   public gH[] a(int var1) {
      return new gV[var1];
   }

   public final void a(String var1) {
      this.a = gh.b(var1);
   }

   public final void b(String var1) {
      this.b = gh.b(var1);
   }

   public final void a(aR var1) {
      this.b(var1.m());
      long var2 = var1.i();
      long var4 = var2;
      String var6;
      int var7;
      if (var2 > 0L && var2 < 6582952005840035281L) {
         if (var2 % 37L == 0L) {
            var6 = null;
         } else {
            var7 = 0;

            for(long var8 = var2; var8 != 0L; var8 /= 37L) {
               ++var7;
            }

            StringBuilder var11 = new StringBuilder(var7);

            while(var4 != 0L) {
               long var9 = var4;
               var4 /= 37L;
               var11.append(gh.a[(int)(var9 - var4 * 37L)]);
            }

            var6 = var11.reverse().toString();
         }
      } else {
         var6 = null;
      }

      this.a(var6);
      this.f = var1.c();
      var7 = var1.r();
      if (var7 != -1) {
         this.f();

         for(int var12 = 0; var12 < var7; ++var12) {
            gV var13 = (gV)this.f(new I(var1.m(), this.d));
            int var10 = var1.d();
            var13.a(var10, ++this.c - 1);
            var13.g = var1.c();
            var1.m();
            this.a(var13);
         }
      }

   }

   public final void b(aR var1) {
      I var2 = new I(var1.m(), this.d);
      int var3 = var1.d();
      byte var4 = var1.c();
      boolean var5 = false;
      if (var4 == -128) {
         var5 = true;
      }

      gV var6;
      if (var5) {
         if (this.g() == 0) {
            return;
         }

         var6 = (gV)this.c(var2);
         if (var6 != null && var6.f() == var3) {
            this.b((gH)var6);
         }
      } else {
         var1.m();
         var6 = (gV)this.c(var2);
         if (var6 == null) {
            if (this.g() > this.j) {
               return;
            }

            var6 = (gV)this.f(var2);
         }

         var6.a(var3, ++this.c - 1);
         var6.g = var4;
         this.a(var6);
      }

   }

   public final void b() {
      for(int var1 = 0; var1 < this.g(); ++var1) {
         ((gV)this.b(var1)).i();
      }

   }

   public final void c() {
      for(int var1 = 0; var1 < this.g(); ++var1) {
         ((gV)this.b(var1)).l();
      }

   }

   public final void a(gV var1) {
      if (var1.a().equals(this.e.aN())) {
         this.g = var1.g;
      }

   }

   public void a(I var1, I var2) {
      FriendsChatMember var3 = (FriendsChatMember)this.g(var1);
      if (var3 != null) {
         FriendsChatMemberJoined var4 = new FriendsChatMemberJoined(var3);
         Client.s.getCallbacks().postDeferred(var4);
      }

   }

   public void a(gH var1) {
      FriendsChatMember var2 = (FriendsChatMember)this.g(var1.e());
      if (var2 != null) {
         FriendsChatMemberLeft var3 = new FriendsChatMemberLeft(var2);
         Client.s.getCallbacks().postDeferred(var3);
      }

   }

   public FriendsChatRank getMyRank() {
      return FriendsChatRank.valueOf(this.e());
   }

   public FriendsChatRank getKickRank() {
      return FriendsChatRank.valueOf(this.d());
   }

   public byte d() {
      return this.f;
   }

   public String getOwner() {
      return this.b;
   }

   public int e() {
      return this.g;
   }

   public String getName() {
      return this.a;
   }
}
