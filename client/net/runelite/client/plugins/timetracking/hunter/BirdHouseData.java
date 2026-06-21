package net.runelite.client.plugins.timetracking.hunter;

final class BirdHouseData {
   private final BirdHouseSpace space;
   private final int varp;
   private final long timestamp;

   public BirdHouseData(BirdHouseSpace space, int varp, long timestamp) {
      this.space = space;
      this.varp = varp;
      this.timestamp = timestamp;
   }

   public BirdHouseSpace getSpace() {
      return this.space;
   }

   public int getVarp() {
      return this.varp;
   }

   public long getTimestamp() {
      return this.timestamp;
   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof BirdHouseData)) {
         return false;
      } else {
         BirdHouseData other = (BirdHouseData)o;
         if (this.getVarp() != other.getVarp()) {
            return false;
         } else if (this.getTimestamp() != other.getTimestamp()) {
            return false;
         } else {
            Object this$space = this.getSpace();
            Object other$space = other.getSpace();
            if (this$space == null) {
               if (other$space != null) {
                  return false;
               }
            } else if (!this$space.equals(other$space)) {
               return false;
            }

            return true;
         }
      }
   }

   public int hashCode() {
      int PRIME = true;
      int result = 1;
      result = result * 59 + this.getVarp();
      long $timestamp = this.getTimestamp();
      result = result * 59 + (int)($timestamp >>> 32 ^ $timestamp);
      Object $space = this.getSpace();
      result = result * 59 + ($space == null ? 43 : $space.hashCode());
      return result;
   }

   public String toString() {
      String var10000 = String.valueOf(this.getSpace());
      return "BirdHouseData(space=" + var10000 + ", varp=" + this.getVarp() + ", timestamp=" + this.getTimestamp() + ")";
   }
}
