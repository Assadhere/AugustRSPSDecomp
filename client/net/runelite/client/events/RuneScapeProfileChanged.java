package net.runelite.client.events;

import javax.annotation.Nullable;

public final class RuneScapeProfileChanged {
   @Nullable
   private final String previousProfile;
   @Nullable
   private final String newProfile;

   public RuneScapeProfileChanged(@Nullable String previousProfile, @Nullable String newProfile) {
      this.previousProfile = previousProfile;
      this.newProfile = newProfile;
   }

   @Nullable
   public String getPreviousProfile() {
      return this.previousProfile;
   }

   @Nullable
   public String getNewProfile() {
      return this.newProfile;
   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof RuneScapeProfileChanged)) {
         return false;
      } else {
         RuneScapeProfileChanged other = (RuneScapeProfileChanged)o;
         Object this$previousProfile = this.getPreviousProfile();
         Object other$previousProfile = other.getPreviousProfile();
         if (this$previousProfile == null) {
            if (other$previousProfile != null) {
               return false;
            }
         } else if (!this$previousProfile.equals(other$previousProfile)) {
            return false;
         }

         Object this$newProfile = this.getNewProfile();
         Object other$newProfile = other.getNewProfile();
         if (this$newProfile == null) {
            if (other$newProfile != null) {
               return false;
            }
         } else if (!this$newProfile.equals(other$newProfile)) {
            return false;
         }

         return true;
      }
   }

   public int hashCode() {
      int PRIME = true;
      int result = 1;
      Object $previousProfile = this.getPreviousProfile();
      result = result * 59 + ($previousProfile == null ? 43 : $previousProfile.hashCode());
      Object $newProfile = this.getNewProfile();
      result = result * 59 + ($newProfile == null ? 43 : $newProfile.hashCode());
      return result;
   }

   public String toString() {
      String var10000 = this.getPreviousProfile();
      return "RuneScapeProfileChanged(previousProfile=" + var10000 + ", newProfile=" + this.getNewProfile() + ")";
   }
}
