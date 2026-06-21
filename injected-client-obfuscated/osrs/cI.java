package osrs;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import net.runelite.api.clan.ClanChannel;
import net.runelite.api.clan.ClanChannelMember;
import net.runelite.api.events.ClanMemberJoined;
import net.runelite.api.events.ClanMemberLeft;

public class cI extends az implements ClanChannel {
   public String a = null;
   public boolean b = true;
   public byte c;
   public byte d;
   public long e;
   public List f;
   public boolean g;
   public int[] h;

   public cI(aR var1) {
      this.a(var1);
   }

   public int[] a() {
      if (this.h == null) {
         String[] var1 = new String[this.f.size()];
         this.h = new int[this.f.size()];

         for(int var2 = 0; var2 < this.f.size(); this.h[var2] = var2++) {
            var1[var2] = ((cr)this.f.get(var2)).c.b();
         }

         kl.a(var1, this.h);
      }

      return this.h;
   }

   public void a(cr var1) {
      this.f.add(var1);
      this.h = null;
      this.b(var1);
   }

   public void a(int var1) {
      this.b(var1);
      this.f.remove(var1);
      this.h = null;
   }

   public int b() {
      return this.f.size();
   }

   public int a(String var1) {
      if (!this.b) {
         throw new RuntimeException("Displaynames not available");
      } else {
         for(int var2 = 0; var2 < this.f.size(); ++var2) {
            if (((cr)this.f.get(var2)).c.a().equalsIgnoreCase(var1)) {
               return var2;
            }
         }

         return -1;
      }
   }

   public void a(aR var1) {
      int var2 = var1.b();
      if ((var2 & 1) != 0) {
         this.g = true;
      }

      if ((var2 & 2) != 0) {
         this.b = true;
      }

      int var3 = 2;
      if ((var2 & 4) != 0) {
         var3 = var1.b();
      }

      this.cm = var1.i();
      this.e = var1.i();
      this.a = var1.m();
      var1.k();
      this.d = var1.c();
      this.c = var1.c();
      int var4 = var1.d();
      if (var4 > 0) {
         this.f = new ArrayList(var4);

         for(int var5 = 0; var5 < var4; ++var5) {
            cr var6 = new cr();
            if (this.g) {
               var1.i();
            }

            if (this.b) {
               var6.c = new I(var1.m());
            }

            var6.a = var1.c();
            var6.b = var1.d();
            if (var3 >= 3) {
               var1.k();
            }

            this.f.add(var5, var6);
         }
      }

   }

   public ClanChannelMember findMember(String var1) {
      I var2 = new I(var1, bo.cp);
      String var3 = var2.e();
      if (var3 == null) {
         return null;
      } else {
         List var4 = this.getMembers();
         int[] var5 = this.c();
         int var6 = 0;
         int var7 = var5.length - 1;

         while(var6 <= var7) {
            int var8 = var6 + var7 >>> 1;
            int var9 = var5[var8];
            cr var10 = (cr)var4.get(var9);
            int var11 = var10.b().e().compareTo(var3);
            if (var11 < 0) {
               var6 = var8 + 1;
            } else {
               if (var11 <= 0) {
                  return var10;
               }

               var7 = var8 - 1;
            }
         }

         return null;
      }
   }

   public void b(int var1) {
      List var2 = this.getMembers();
      cr var3 = (cr)var2.get(var1);
      Client.s.getCallbacks().post(new ClanMemberLeft(this, var3));
   }

   public void b(cr var1) {
      Client.s.getCallbacks().post(new ClanMemberJoined(this, var1));
   }

   public String getName() {
      return this.a;
   }

   public int[] c() {
      return this.a();
   }

   public List getMembers() {
      return this.f;
   }

   static {
      new BitSet(65536);
   }
}
