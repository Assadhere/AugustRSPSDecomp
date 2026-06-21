package custom;

public class UpdateDiscordPresenceScript {
   private final String activityName;
   private final int worldId;
   private final boolean leagueWorld;
   private final String partyId;
   private final int partySize;
   private final int partyMax;

   public UpdateDiscordPresenceScript(String var1, int var2, boolean var3, String var4, int var5, int var6) {
      this.activityName = var1;
      this.worldId = var2;
      this.leagueWorld = var3;
      this.partyId = var4;
      this.partySize = var5;
      this.partyMax = var6;
   }

   public String getActivityName() {
      return this.activityName;
   }

   public int getWorldId() {
      return this.worldId;
   }

   public boolean isLeagueWorld() {
      return this.leagueWorld;
   }

   public String getPartyId() {
      return this.partyId;
   }

   public int getPartySize() {
      return this.partySize;
   }

   public int getPartyMax() {
      return this.partyMax;
   }
}
