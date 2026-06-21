package net.runelite.client.config;

public class RuneScapeProfile {
   public static final int ACCOUNT_HASH_INVALID = -1;
   private final String displayName;
   private final RuneScapeProfileType type;
   private final long accountHash;
   private final String key;

   public RuneScapeProfile(String displayName, RuneScapeProfileType type, long accountHash, String key) {
      this.displayName = displayName;
      this.type = type;
      this.accountHash = accountHash;
      this.key = key;
   }

   public String getDisplayName() {
      return this.displayName;
   }

   public RuneScapeProfileType getType() {
      return this.type;
   }

   public long getAccountHash() {
      return this.accountHash;
   }

   public String getKey() {
      return this.key;
   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof RuneScapeProfile)) {
         return false;
      } else {
         RuneScapeProfile other = (RuneScapeProfile)o;
         if (!other.canEqual(this)) {
            return false;
         } else if (this.getAccountHash() != other.getAccountHash()) {
            return false;
         } else {
            label49: {
               Object this$displayName = this.getDisplayName();
               Object other$displayName = other.getDisplayName();
               if (this$displayName == null) {
                  if (other$displayName == null) {
                     break label49;
                  }
               } else if (this$displayName.equals(other$displayName)) {
                  break label49;
               }

               return false;
            }

            Object this$type = this.getType();
            Object other$type = other.getType();
            if (this$type == null) {
               if (other$type != null) {
                  return false;
               }
            } else if (!this$type.equals(other$type)) {
               return false;
            }

            Object this$key = this.getKey();
            Object other$key = other.getKey();
            if (this$key == null) {
               if (other$key != null) {
                  return false;
               }
            } else if (!this$key.equals(other$key)) {
               return false;
            }

            return true;
         }
      }
   }

   protected boolean canEqual(Object other) {
      return other instanceof RuneScapeProfile;
   }

   public int hashCode() {
      int PRIME = true;
      int result = 1;
      long $accountHash = this.getAccountHash();
      result = result * 59 + (int)($accountHash >>> 32 ^ $accountHash);
      Object $displayName = this.getDisplayName();
      result = result * 59 + ($displayName == null ? 43 : $displayName.hashCode());
      Object $type = this.getType();
      result = result * 59 + ($type == null ? 43 : $type.hashCode());
      Object $key = this.getKey();
      result = result * 59 + ($key == null ? 43 : $key.hashCode());
      return result;
   }

   public String toString() {
      String var10000 = this.getDisplayName();
      return "RuneScapeProfile(displayName=" + var10000 + ", type=" + String.valueOf(this.getType()) + ", accountHash=" + this.getAccountHash() + ", key=" + this.getKey() + ")";
   }
}
