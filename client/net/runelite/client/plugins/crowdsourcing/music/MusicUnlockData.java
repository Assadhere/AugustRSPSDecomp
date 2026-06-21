package net.runelite.client.plugins.crowdsourcing.music;

import net.runelite.api.coords.WorldPoint;

public class MusicUnlockData {
   private final WorldPoint location;
   private final boolean isInInstance;
   private final String message;

   public WorldPoint getLocation() {
      return this.location;
   }

   public boolean isInInstance() {
      return this.isInInstance;
   }

   public String getMessage() {
      return this.message;
   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof MusicUnlockData)) {
         return false;
      } else {
         MusicUnlockData other = (MusicUnlockData)o;
         if (!other.canEqual(this)) {
            return false;
         } else if (this.isInInstance() != other.isInInstance()) {
            return false;
         } else {
            Object this$location = this.getLocation();
            Object other$location = other.getLocation();
            if (this$location == null) {
               if (other$location != null) {
                  return false;
               }
            } else if (!this$location.equals(other$location)) {
               return false;
            }

            Object this$message = this.getMessage();
            Object other$message = other.getMessage();
            if (this$message == null) {
               if (other$message != null) {
                  return false;
               }
            } else if (!this$message.equals(other$message)) {
               return false;
            }

            return true;
         }
      }
   }

   protected boolean canEqual(Object other) {
      return other instanceof MusicUnlockData;
   }

   public int hashCode() {
      int PRIME = true;
      int result = 1;
      result = result * 59 + (this.isInInstance() ? 79 : 97);
      Object $location = this.getLocation();
      result = result * 59 + ($location == null ? 43 : $location.hashCode());
      Object $message = this.getMessage();
      result = result * 59 + ($message == null ? 43 : $message.hashCode());
      return result;
   }

   public String toString() {
      String var10000 = String.valueOf(this.getLocation());
      return "MusicUnlockData(location=" + var10000 + ", isInInstance=" + this.isInInstance() + ", message=" + this.getMessage() + ")";
   }

   public MusicUnlockData(WorldPoint location, boolean isInInstance, String message) {
      this.location = location;
      this.isInInstance = isInInstance;
      this.message = message;
   }
}
