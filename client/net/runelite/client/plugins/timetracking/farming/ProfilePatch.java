package net.runelite.client.plugins.timetracking.farming;

final class ProfilePatch {
   private final FarmingPatch patch;
   private final String rsProfileKey;

   public ProfilePatch(FarmingPatch patch, String rsProfileKey) {
      this.patch = patch;
      this.rsProfileKey = rsProfileKey;
   }

   public FarmingPatch getPatch() {
      return this.patch;
   }

   public String getRsProfileKey() {
      return this.rsProfileKey;
   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof ProfilePatch)) {
         return false;
      } else {
         ProfilePatch other = (ProfilePatch)o;
         Object this$patch = this.getPatch();
         Object other$patch = other.getPatch();
         if (this$patch == null) {
            if (other$patch != null) {
               return false;
            }
         } else if (!this$patch.equals(other$patch)) {
            return false;
         }

         Object this$rsProfileKey = this.getRsProfileKey();
         Object other$rsProfileKey = other.getRsProfileKey();
         if (this$rsProfileKey == null) {
            if (other$rsProfileKey != null) {
               return false;
            }
         } else if (!this$rsProfileKey.equals(other$rsProfileKey)) {
            return false;
         }

         return true;
      }
   }

   public int hashCode() {
      int PRIME = true;
      int result = 1;
      Object $patch = this.getPatch();
      result = result * 59 + ($patch == null ? 43 : $patch.hashCode());
      Object $rsProfileKey = this.getRsProfileKey();
      result = result * 59 + ($rsProfileKey == null ? 43 : $rsProfileKey.hashCode());
      return result;
   }

   public String toString() {
      String var10000 = String.valueOf(this.getPatch());
      return "ProfilePatch(patch=" + var10000 + ", rsProfileKey=" + this.getRsProfileKey() + ")";
   }
}
