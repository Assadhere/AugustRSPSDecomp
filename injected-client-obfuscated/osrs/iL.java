package osrs;

import java.time.LocalDate;
import net.runelite.api.clan.ClanMember;
import net.runelite.api.clan.ClanRank;

public class iL implements ClanMember {
   public final int a;
   public final cK b;

   public boolean equals(Object var1) {
      if (this == var1) {
         return true;
      } else if (!(var1 instanceof iL)) {
         return false;
      } else {
         iL var2 = (iL)var1;
         if (!var2.a(this)) {
            return false;
         } else {
            String var3 = this.getName();
            String var4 = var2.getName();
            if (var3 == null) {
               if (var4 != null) {
                  return false;
               }
            } else if (!var3.equals(var4)) {
               return false;
            }

            ClanRank var5 = this.getRank();
            ClanRank var6 = var2.getRank();
            if (var5 == null) {
               if (var6 != null) {
                  return false;
               }
            } else if (!var5.equals(var6)) {
               return false;
            }

            return true;
         }
      }
   }

   public String toString() {
      String var1 = this.getName();
      return "ClanMemberImpl(getName=" + var1 + ", getRank=" + String.valueOf(this.getRank()) + ")";
   }

   public ClanRank getRank() {
      return Client.V(this.b.e()[this.a]);
   }

   public String getName() {
      return this.b.d()[this.a];
   }

   public iL(cK var1, int var2) {
      this.b = var1;
      this.a = var2;
   }

   public LocalDate getJoinDate() {
      int var1 = this.b.e[this.a];
      return LocalDate.ofEpochDay((long)(var1 + 11745));
   }

   public boolean a(Object var1) {
      return var1 instanceof iL;
   }

   public int hashCode() {
      byte var1 = 1;
      String var2 = this.getName();
      int var3 = var1 * 59 + (var2 == null ? 43 : var2.hashCode());
      ClanRank var4 = this.getRank();
      int var5 = var3 * 59 + (var4 == null ? 43 : var4.hashCode());
      return var5;
   }
}
