package net.runelite.client.plugins.crowdsourcing.thieving;

import net.runelite.api.coords.WorldPoint;

public class PickpocketData {
   private final int level;
   private final int target;
   private final String message;
   private final WorldPoint location;
   private final boolean silence;
   private final boolean thievingCape;
   private final int ardougneDiary;

   public int getLevel() {
      return this.level;
   }

   public int getTarget() {
      return this.target;
   }

   public String getMessage() {
      return this.message;
   }

   public WorldPoint getLocation() {
      return this.location;
   }

   public boolean isSilence() {
      return this.silence;
   }

   public boolean isThievingCape() {
      return this.thievingCape;
   }

   public int getArdougneDiary() {
      return this.ardougneDiary;
   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof PickpocketData)) {
         return false;
      } else {
         PickpocketData other = (PickpocketData)o;
         if (!other.canEqual(this)) {
            return false;
         } else if (this.getLevel() != other.getLevel()) {
            return false;
         } else if (this.getTarget() != other.getTarget()) {
            return false;
         } else if (this.isSilence() != other.isSilence()) {
            return false;
         } else if (this.isThievingCape() != other.isThievingCape()) {
            return false;
         } else if (this.getArdougneDiary() != other.getArdougneDiary()) {
            return false;
         } else {
            Object this$message = this.getMessage();
            Object other$message = other.getMessage();
            if (this$message == null) {
               if (other$message != null) {
                  return false;
               }
            } else if (!this$message.equals(other$message)) {
               return false;
            }

            Object this$location = this.getLocation();
            Object other$location = other.getLocation();
            if (this$location == null) {
               if (other$location != null) {
                  return false;
               }
            } else if (!this$location.equals(other$location)) {
               return false;
            }

            return true;
         }
      }
   }

   protected boolean canEqual(Object other) {
      return other instanceof PickpocketData;
   }

   public int hashCode() {
      int PRIME = true;
      int result = 1;
      result = result * 59 + this.getLevel();
      result = result * 59 + this.getTarget();
      result = result * 59 + (this.isSilence() ? 79 : 97);
      result = result * 59 + (this.isThievingCape() ? 79 : 97);
      result = result * 59 + this.getArdougneDiary();
      Object $message = this.getMessage();
      result = result * 59 + ($message == null ? 43 : $message.hashCode());
      Object $location = this.getLocation();
      result = result * 59 + ($location == null ? 43 : $location.hashCode());
      return result;
   }

   public String toString() {
      int var10000 = this.getLevel();
      return "PickpocketData(level=" + var10000 + ", target=" + this.getTarget() + ", message=" + this.getMessage() + ", location=" + String.valueOf(this.getLocation()) + ", silence=" + this.isSilence() + ", thievingCape=" + this.isThievingCape() + ", ardougneDiary=" + this.getArdougneDiary() + ")";
   }

   public PickpocketData(int level, int target, String message, WorldPoint location, boolean silence, boolean thievingCape, int ardougneDiary) {
      this.level = level;
      this.target = target;
      this.message = message;
      this.location = location;
      this.silence = silence;
      this.thievingCape = thievingCape;
      this.ardougneDiary = ardougneDiary;
   }
}
