package net.runelite.client.plugins.cluescrolls.clues.hotcold;

public enum HotColdTemperatureChange {
   WARMER("and warmer than"),
   SAME("and the same temperature as"),
   COLDER("but colder than");

   private final String text;

   public static HotColdTemperatureChange of(String message) {
      if (!message.endsWith(" last time.")) {
         return null;
      } else {
         HotColdTemperatureChange[] var1 = values();
         int var2 = var1.length;

         for(int var3 = 0; var3 < var2; ++var3) {
            HotColdTemperatureChange change = var1[var3];
            if (message.contains(change.text)) {
               return change;
            }
         }

         return null;
      }
   }

   private HotColdTemperatureChange(String text) {
      this.text = text;
   }
}
