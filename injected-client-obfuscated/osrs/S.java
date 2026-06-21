package osrs;

import net.runelite.api.Friend;
import net.runelite.api.Ignore;
import net.runelite.api.events.RemovedFriend;

public class S {
   public int a = 0;
   public final aM b;
   public final R c;
   public final ah d;

   public S(aM var1) {
      this.b = var1;
      this.c = new R(var1);
      this.d = new ah(var1);
   }

   public static final void a(String var0) {
      iw.a(30, "", var0);
   }

   public static final void a() {
      a(bv.cD);
   }

   public static final void b(String var0) {
      a(bv.cJ + var0 + bv.cK);
   }

   public static final void b() {
      a(bv.cH);
   }

   public static final void a(String var0, int var1) {
      r var2 = r.a(u.U, Client.b.p);
      var2.f.a(aR.a(var0) + 1);
      var2.f.c(var0);
      var2.f.a(var1);
      Client.b.a(var2);
   }

   public boolean c() {
      return this.a == 2;
   }

   public final void d() {
      this.a = 1;
   }

   public void a(aR var1, int var2) {
      this.c.a(var1, var2);
      this.a = 2;
      Client.ae();
   }

   public final void e() {
      for(Q var1 = (Q)this.c.a.a(); var1 != null; var1 = (Q)this.c.a.b()) {
         if ((long)var1.a < bd.a() / 1000L - 5L) {
            String var10002;
            if (var1.c > 0) {
               var10002 = String.valueOf(var1.b);
               iw.a(5, "", var10002 + bv.cj);
            }

            if (var1.c == 0) {
               var10002 = String.valueOf(var1.b);
               iw.a(5, "", var10002 + bv.ck);
            }

            ((ht)var1).b();
         }
      }

   }

   public final void f() {
      this.a = 0;
      this.c.f();
      this.d.f();
   }

   public boolean a(I var1, boolean var2) {
      if (var1 == null) {
         return false;
      } else {
         return var1.equals(Client.s.aN()) ? true : this.c.a(var1, var2);
      }
   }

   public final boolean a(I var1) {
      return var1 == null ? false : this.d.a(var1);
   }

   public void c(String var1) {
      if (var1 != null) {
         I var2 = new I(var1, this.b);
         if (var2.c()) {
            if (this.g()) {
               a();
            } else {
               I var3 = Client.s.aN();
               if (var3 != null && var3.equals(var2)) {
                  b();
               } else if (this.a(var2, false)) {
                  a(var1 + bv.cE);
               } else if (this.a(var2)) {
                  b(var1);
               } else {
                  r var4 = r.a(u.D, Client.b.p);
                  var4.f.a(aR.a(var1));
                  var4.f.c(var1);
                  Client.b.a(var4);
               }
            }
         }
      }

   }

   public final boolean g() {
      return this.c.h() || this.c.g() >= 200 && Client.R != 1;
   }

   public final void d(String var1) {
      if (var1 != null) {
         I var2 = new I(var1, this.b);
         if (var2.c()) {
            if (this.h()) {
               a(bv.cF);
            } else {
               I var3 = Client.s.aN();
               if (var3 != null && var3.equals(var2)) {
                  a(bv.cI);
               } else if (this.a(var2)) {
                  a(var1 + bv.cG);
               } else if (this.a(var2, false)) {
                  a(bv.cL + var1 + bv.cM);
               } else {
                  r var4 = r.a(u.l, Client.b.p);
                  var4.f.a(aR.a(var1));
                  var4.f.c(var1);
                  Client.b.a(var4);
               }
            }
         }
      }

   }

   public final boolean h() {
      return this.d.h() || this.d.g() >= 100 && Client.R != 1;
   }

   public final void e(String var1) {
      this.f(var1);
      if (var1 != null) {
         I var2 = new I(var1, this.b);
         if (var2.c()) {
            if (this.c.e(var2)) {
               Client.p.b();
               r var3 = r.a(u.Z, Client.b.p);
               var3.f.a(aR.a(var1));
               var3.f.c(var1);
               Client.b.a(var3);
            }

            Client.ae();
         }
      }

   }

   public void a(String var1, boolean var2) {
      this.b(var1, var2);
      if (var1 != null) {
         I var3 = new I(var1, this.b);
         if (var3.c()) {
            if (this.d.e(var3)) {
               Client.p.b();
               if (var2) {
                  r var4 = r.a(u.al, Client.b.p);
                  var4.f.a(aR.a(var1));
                  var4.f.c(var1);
                  Client.b.a(var4);
               }
            }

            Client.af();
         }
      }

   }

   public final boolean b(I var1) {
      gM var2 = (gM)this.c.b(var1);
      return var2 != null && var2.g();
   }

   public void b(String var1, boolean var2) {
      Ignore var3 = (Ignore)this.j().findByName(var1);
      if (var3 != null) {
         Client.s.getCallbacks().post(new RemovedFriend(var3));
      }

   }

   public void f(String var1) {
      Friend var2 = (Friend)this.i().findByName(var1);
      if (var2 != null) {
         Client.s.getCallbacks().post(new RemovedFriend(var2));
      }

   }

   public R i() {
      return this.c;
   }

   public boolean b(I var1, boolean var2) {
      return this.a(var1, var2);
   }

   public ah j() {
      return this.d;
   }
}
