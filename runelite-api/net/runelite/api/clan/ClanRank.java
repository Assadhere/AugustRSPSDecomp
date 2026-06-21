package net.runelite.api.clan;

public final class ClanRank {
   public static final ClanRank JMOD = new ClanRank(127);
   public static final ClanRank OWNER = new ClanRank(126);
   public static final ClanRank DEPUTY_OWNER = new ClanRank(125);
   public static final ClanRank ADMINISTRATOR = new ClanRank(100);
   public static final ClanRank GUEST = new ClanRank(-1);
   private final int rank;

   public ClanRank(int rank) {
      this.rank = rank;
   }

   public int getRank() {
      return this.rank;
   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof ClanRank)) {
         return false;
      } else {
         ClanRank other = (ClanRank)o;
         return this.getRank() == other.getRank();
      }
   }

   public int hashCode() {
      int PRIME = true;
      int result = 1;
      result = result * 59 + this.getRank();
      return result;
   }

   public String toString() {
      return "ClanRank(rank=" + this.getRank() + ")";
   }
}
