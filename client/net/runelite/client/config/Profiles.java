package net.runelite.client.config;

import java.util.List;

class Profiles {
   private List<ConfigProfile> profiles;

   public Profiles() {
   }

   public List<ConfigProfile> getProfiles() {
      return this.profiles;
   }

   public void setProfiles(List<ConfigProfile> profiles) {
      this.profiles = profiles;
   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof Profiles)) {
         return false;
      } else {
         Profiles other = (Profiles)o;
         if (!other.canEqual(this)) {
            return false;
         } else {
            Object this$profiles = this.getProfiles();
            Object other$profiles = other.getProfiles();
            if (this$profiles == null) {
               if (other$profiles != null) {
                  return false;
               }
            } else if (!this$profiles.equals(other$profiles)) {
               return false;
            }

            return true;
         }
      }
   }

   protected boolean canEqual(Object other) {
      return other instanceof Profiles;
   }

   public int hashCode() {
      int PRIME = true;
      int result = 1;
      Object $profiles = this.getProfiles();
      result = result * 59 + ($profiles == null ? 43 : $profiles.hashCode());
      return result;
   }

   public String toString() {
      return "Profiles(profiles=" + String.valueOf(this.getProfiles()) + ")";
   }
}
