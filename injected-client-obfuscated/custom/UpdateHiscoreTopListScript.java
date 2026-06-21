package custom;

import java.util.List;

public class UpdateHiscoreTopListScript {
   private final int requestId;
   private final TopListType type;
   private final String targetId;
   private final String targetName;
   private final int page;
   private final int pageCount;
   private final int pageSize;
   private final List<Entry> items;

   public UpdateHiscoreTopListScript(int var1, TopListType var2, String var3, String var4, int var5, int var6, int var7, List<Entry> var8) {
      this.requestId = var1;
      this.type = var2;
      this.targetId = var3;
      this.targetName = var4;
      this.page = var5;
      this.pageCount = var6;
      this.pageSize = var7;
      this.items = var8;
   }

   public int getRequestId() {
      return this.requestId;
   }

   public TopListType getType() {
      return this.type;
   }

   public String getTargetId() {
      return this.targetId;
   }

   public String getTargetName() {
      return this.targetName;
   }

   public int getPage() {
      return this.page;
   }

   public int getPageCount() {
      return this.pageCount;
   }

   public int getPageSize() {
      return this.pageSize;
   }

   public List<Entry> getItems() {
      return this.items;
   }

   public static class Entry {
      private final String username;
      private final String displayName;
      private final int ironmanMode;
      private final int gameMode;
      private final int privilege;
      private final int donatorRank;
      private final int rank;
      private final long value;
      private final int level;
      private final int prestige;
      private final long prestigeXp;

      public Entry(String var1, String var2, int var3, int var4, int var5, int var6, int var7, long var8, int var10, int var11, long var12) {
         this.username = var1;
         this.displayName = var2;
         this.ironmanMode = var3;
         this.gameMode = var4;
         this.privilege = var5;
         this.donatorRank = var6;
         this.rank = var7;
         this.value = var8;
         this.level = var10;
         this.prestige = var11;
         this.prestigeXp = var12;
      }

      public String getUsername() {
         return this.username;
      }

      public String getDisplayName() {
         return this.displayName;
      }

      public int getIronmanMode() {
         return this.ironmanMode;
      }

      public int getGameMode() {
         return this.gameMode;
      }

      public int getPrivilege() {
         return this.privilege;
      }

      public int getDonatorRank() {
         return this.donatorRank;
      }

      public int getRank() {
         return this.rank;
      }

      public long getValue() {
         return this.value;
      }

      public int getLevel() {
         return this.level;
      }

      public int getPrestige() {
         return this.prestige;
      }

      public long getPrestigeXp() {
         return this.prestigeXp;
      }
   }

   public static enum TopListType {
      SKILL(0, "Skills"),
      COUNT(1, "Counts");

      private final int wireId;
      private final String displayName;

      private TopListType(int var3, String var4) {
         this.wireId = var3;
         this.displayName = var4;
      }

      public static TopListType fromWireId(int var0) {
         TopListType[] var1 = values();
         int var2 = var1.length;

         for(int var3 = 0; var3 < var2; ++var3) {
            TopListType var4 = var1[var3];
            if (var4.wireId == var0) {
               return var4;
            }
         }

         throw new IllegalArgumentException("Unknown hiscore top list type: " + var0);
      }

      public int getWireId() {
         return this.wireId;
      }

      public String getDisplayName() {
         return this.displayName;
      }
   }
}
