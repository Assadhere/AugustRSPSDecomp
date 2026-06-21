package net.runelite.api;

import java.util.HashMap;
import java.util.Map;

public enum FriendsChatRank {
   UNRANKED(-1),
   FRIEND(0),
   RECRUIT(1),
   CORPORAL(2),
   SERGEANT(3),
   LIEUTENANT(4),
   CAPTAIN(5),
   GENERAL(6),
   OWNER(7),
   JMOD(127);

   private static final Map<Integer, FriendsChatRank> RANKS = new HashMap();
   private final int value;

   public static FriendsChatRank valueOf(int rank) {
      return (FriendsChatRank)RANKS.get(rank);
   }

   private FriendsChatRank(int value) {
      this.value = value;
   }

   public int getValue() {
      return this.value;
   }

   static {
      FriendsChatRank[] var0 = values();
      int var1 = var0.length;

      for(int var2 = 0; var2 < var1; ++var2) {
         FriendsChatRank friendsChatRank = var0[var2];
         RANKS.put(friendsChatRank.value, friendsChatRank);
      }

   }
}
