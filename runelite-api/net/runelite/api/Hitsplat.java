package net.runelite.api;

public interface Hitsplat {
   int getHitsplatType();

   int getAmount();

   int getDisappearsOnGameCycle();

   default boolean isMine() {
      switch (this.getHitsplatType()) {
         case 12:
         case 16:
         case 18:
         case 20:
         case 22:
         case 24:
         case 43:
         case 44:
         case 45:
         case 46:
         case 47:
         case 53:
         case 55:
            return true;
         case 13:
         case 14:
         case 15:
         case 17:
         case 19:
         case 21:
         case 23:
         case 25:
         case 26:
         case 27:
         case 28:
         case 29:
         case 30:
         case 31:
         case 32:
         case 33:
         case 34:
         case 35:
         case 36:
         case 37:
         case 38:
         case 39:
         case 40:
         case 41:
         case 42:
         case 48:
         case 49:
         case 50:
         case 51:
         case 52:
         case 54:
         default:
            return false;
      }
   }

   default boolean isOthers() {
      switch (this.getHitsplatType()) {
         case 13:
         case 17:
         case 19:
         case 21:
         case 23:
         case 25:
         case 54:
            return true;
         default:
            return false;
      }
   }
}
