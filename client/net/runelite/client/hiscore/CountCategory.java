package net.runelite.client.hiscore;

public enum CountCategory {
   RAID("Raids"),
   BOSS("Bosses"),
   MINIGAME("Minigames"),
   SLAYER("Slayer"),
   MISC("Miscellaneous"),
   OTHER("Other");

   private final String displayName;

   private CountCategory(String displayName) {
      this.displayName = displayName;
   }

   public String getDisplayName() {
      return this.displayName;
   }
}
