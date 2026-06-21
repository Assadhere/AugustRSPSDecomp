package net.runelite.client.plugins.raids;

public enum RoomType {
   START("Start", '#'),
   END("End", '¤'),
   SCAVENGERS("Scavengers", 'S'),
   FARMING("Farming", 'F'),
   EMPTY("Empty", ' '),
   COMBAT("Combat", 'C'),
   PUZZLE("Puzzle", 'P');

   private final String name;
   private final char code;

   RaidRoom getUnsolvedRoom() {
      switch (this) {
         case START:
            return RaidRoom.START;
         case END:
            return RaidRoom.END;
         case SCAVENGERS:
            return RaidRoom.SCAVENGERS;
         case FARMING:
            return RaidRoom.FARMING;
         case COMBAT:
            return RaidRoom.UNKNOWN_COMBAT;
         case PUZZLE:
            return RaidRoom.UNKNOWN_PUZZLE;
         case EMPTY:
         default:
            return RaidRoom.EMPTY;
      }
   }

   static RoomType fromCode(char code) {
      RoomType[] var1 = values();
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         RoomType type = var1[var3];
         if (type.getCode() == code) {
            return type;
         }
      }

      return EMPTY;
   }

   private RoomType(String name, char code) {
      this.name = name;
      this.code = code;
   }

   public String getName() {
      return this.name;
   }

   public char getCode() {
      return this.code;
   }
}
