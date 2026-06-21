package custom;

import custom.model.SkillPrestigeInfo;
import java.util.List;

public class UpdateServerAndPlayerInfoScript {
   private final String serverInfoText;
   private final List<SkillPrestigeInfo> skillPrestigeInfo;
   private final String donatorRank;
   private final int donatorPoints;
   private final List<String> donatorPerks;

   public UpdateServerAndPlayerInfoScript(String var1, List<SkillPrestigeInfo> var2, String var3, int var4, List<String> var5) {
      this.serverInfoText = var1;
      this.skillPrestigeInfo = var2;
      this.donatorRank = var3;
      this.donatorPoints = var4;
      this.donatorPerks = var5;
   }

   public String getServerInfoText() {
      return this.serverInfoText;
   }

   public List<SkillPrestigeInfo> getSkillPrestigeInfo() {
      return this.skillPrestigeInfo;
   }

   public String getDonatorRank() {
      return this.donatorRank;
   }

   public int getDonatorPoints() {
      return this.donatorPoints;
   }

   public List<String> getDonatorPerks() {
      return this.donatorPerks;
   }

   public String toString() {
      String var10000 = this.getServerInfoText();
      return "UpdateServerAndPlayerInfoScript(serverInfoText=" + var10000 + ", skillPrestigeInfo=" + String.valueOf(this.getSkillPrestigeInfo()) + ", donatorRank=" + this.getDonatorRank() + ", donatorPoints=" + this.getDonatorPoints() + ", donatorPerks=" + String.valueOf(this.getDonatorPerks()) + ")";
   }
}
