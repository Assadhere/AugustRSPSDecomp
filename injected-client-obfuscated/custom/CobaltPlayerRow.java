package custom;

import java.util.List;

public class CobaltPlayerRow {
   private final String name;
   private final int world;
   private final int combatLevel;
   private final int gameModeId;
   private final int ironmanModeId;
   private final String instanceLabel;
   private final int instanceTypeId;
   private final List<String> memberNames;

   public CobaltPlayerRow(String var1, int var2, int var3, int var4, int var5, String var6, int var7, List<String> var8) {
      this.name = var1;
      this.world = var2;
      this.combatLevel = var3;
      this.gameModeId = var4;
      this.ironmanModeId = var5;
      this.instanceLabel = var6;
      this.instanceTypeId = var7;
      this.memberNames = var8;
   }

   public String getName() {
      return this.name;
   }

   public int getWorld() {
      return this.world;
   }

   public int getCombatLevel() {
      return this.combatLevel;
   }

   public int getGameModeId() {
      return this.gameModeId;
   }

   public int getIronmanModeId() {
      return this.ironmanModeId;
   }

   public String getInstanceLabel() {
      return this.instanceLabel;
   }

   public int getInstanceTypeId() {
      return this.instanceTypeId;
   }

   public List<String> getMemberNames() {
      return this.memberNames;
   }

   public String toString() {
      String var10000 = this.getName();
      return "CobaltPlayerRow(name=" + var10000 + ", world=" + this.getWorld() + ", combatLevel=" + this.getCombatLevel() + ", gameModeId=" + this.getGameModeId() + ", ironmanModeId=" + this.getIronmanModeId() + ", instanceLabel=" + this.getInstanceLabel() + ", instanceTypeId=" + this.getInstanceTypeId() + ", memberNames=" + String.valueOf(this.getMemberNames()) + ")";
   }
}
