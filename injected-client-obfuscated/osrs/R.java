package osrs;

import net.runelite.api.Deque;
import net.runelite.api.Friend;
import net.runelite.api.FriendContainer;

public class R extends gW<Friend> implements FriendContainer {
   public gd a = new gd();
   public int b = 1;
   public final aM c;

   public R(aM var1) {
      super(400);
      this.c = var1;
   }

   public gH a() {
      return new gM();
   }

   public gH[] a(int var1) {
      return new gM[var1];
   }

   public boolean a(I var1, boolean var2) {
      gM var3 = (gM)this.b(var1);
      if (var3 == null) {
         return false;
      } else {
         return !var2 || var3.e != 0;
      }
   }

   public void a(aR var1, int var2) {
      while(true) {
         if (var1.d < var2) {
            boolean var3 = var1.b() == 1;
            I var4 = new I(var1.m(), this.c);
            I var5 = new I(var1.m(), this.c);
            int var6 = var1.d();
            int var7 = var1.b();
            int var8 = var1.b();
            boolean var9 = (var8 & 2) != 0;
            boolean var10 = (var8 & 1) != 0;
            if (var6 > 0) {
               var1.m();
               var1.b();
               var1.h();
            }

            var1.m();
            if (var4 != null && var4.c()) {
               gM var11 = (gM)this.c(var4);
               if (var3) {
                  gM var12 = (gM)this.c(var5);
                  if (var12 != null && var11 != var12) {
                     if (var11 != null) {
                        this.b(var12);
                     } else {
                        var11 = var12;
                     }
                  }
               }

               if (var11 != null) {
                  this.a(var11, var4, var5);
                  if (var11.e != var6) {
                     boolean var14 = true;

                     for(Q var13 = (Q)this.a.a(); var13 != null; var13 = (Q)this.a.b()) {
                        if (var13.b.equals(var4)) {
                           if (var6 != 0 && var13.c == 0) {
                              ((ht)var13).b();
                              var14 = false;
                           } else if (var6 == 0 && var13.c != 0) {
                              ((ht)var13).b();
                              var14 = false;
                           }
                        }
                     }

                     if (var14) {
                        this.a.a((ht)(new Q(var4, var6)));
                     }
                  }
               } else {
                  if (this.g() >= 400) {
                     continue;
                  }

                  var11 = (gM)((gW)this).b(var4, var5);
               }

               if (var11.e != var6) {
                  var11.f = ++this.b - 1;
                  if (var11.e == -1 && var6 == 0) {
                     var11.f = -(var11.f * -1531624211) * -290735899;
                  }

                  var11.e = var6;
               }

               var11.g = var7;
               var11.c = var9;
               var11.d = var10;
               continue;
            }

            throw new IllegalStateException();
         }

         this.i();
         return;
      }
   }

   public Deque getPendingLogins() {
      return this.a;
   }
}
