package net.runelite.client.plugins.driftnet;

import java.util.Set;
import net.runelite.api.GameObject;
import net.runelite.api.coords.WorldPoint;

class DriftNet {
   private final int objectId;
   private final int statusVarbit;
   private final int countVarbit;
   private final Set<WorldPoint> adjacentTiles;
   private GameObject net;
   private DriftNetStatus status;
   private int count;
   private DriftNetStatus prevTickStatus;

   boolean isNotAcceptingFish() {
      return this.status != DriftNetStatus.CATCH && this.status != DriftNetStatus.SET || this.prevTickStatus != DriftNetStatus.CATCH && this.prevTickStatus != DriftNetStatus.SET;
   }

   String getFormattedCountText() {
      return this.status != DriftNetStatus.UNSET ? this.count + "/10" : "";
   }

   public int getObjectId() {
      return this.objectId;
   }

   public Set<WorldPoint> getAdjacentTiles() {
      return this.adjacentTiles;
   }

   public GameObject getNet() {
      return this.net;
   }

   public DriftNetStatus getStatus() {
      return this.status;
   }

   public int getCount() {
      return this.count;
   }

   public DriftNetStatus getPrevTickStatus() {
      return this.prevTickStatus;
   }

   public void setNet(GameObject net) {
      this.net = net;
   }

   public void setStatus(DriftNetStatus status) {
      this.status = status;
   }

   public void setCount(int count) {
      this.count = count;
   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof DriftNet)) {
         return false;
      } else {
         DriftNet other = (DriftNet)o;
         if (!other.canEqual(this)) {
            return false;
         } else if (this.getObjectId() != other.getObjectId()) {
            return false;
         } else if (this.getStatusVarbit() != other.getStatusVarbit()) {
            return false;
         } else if (this.getCountVarbit() != other.getCountVarbit()) {
            return false;
         } else if (this.getCount() != other.getCount()) {
            return false;
         } else {
            Object this$adjacentTiles = this.getAdjacentTiles();
            Object other$adjacentTiles = other.getAdjacentTiles();
            if (this$adjacentTiles == null) {
               if (other$adjacentTiles != null) {
                  return false;
               }
            } else if (!this$adjacentTiles.equals(other$adjacentTiles)) {
               return false;
            }

            Object this$net = this.getNet();
            Object other$net = other.getNet();
            if (this$net == null) {
               if (other$net != null) {
                  return false;
               }
            } else if (!this$net.equals(other$net)) {
               return false;
            }

            label55: {
               Object this$status = this.getStatus();
               Object other$status = other.getStatus();
               if (this$status == null) {
                  if (other$status == null) {
                     break label55;
                  }
               } else if (this$status.equals(other$status)) {
                  break label55;
               }

               return false;
            }

            Object this$prevTickStatus = this.getPrevTickStatus();
            Object other$prevTickStatus = other.getPrevTickStatus();
            if (this$prevTickStatus == null) {
               if (other$prevTickStatus != null) {
                  return false;
               }
            } else if (!this$prevTickStatus.equals(other$prevTickStatus)) {
               return false;
            }

            return true;
         }
      }
   }

   protected boolean canEqual(Object other) {
      return other instanceof DriftNet;
   }

   public int hashCode() {
      int PRIME = true;
      int result = 1;
      result = result * 59 + this.getObjectId();
      result = result * 59 + this.getStatusVarbit();
      result = result * 59 + this.getCountVarbit();
      result = result * 59 + this.getCount();
      Object $adjacentTiles = this.getAdjacentTiles();
      result = result * 59 + ($adjacentTiles == null ? 43 : $adjacentTiles.hashCode());
      Object $net = this.getNet();
      result = result * 59 + ($net == null ? 43 : $net.hashCode());
      Object $status = this.getStatus();
      result = result * 59 + ($status == null ? 43 : $status.hashCode());
      Object $prevTickStatus = this.getPrevTickStatus();
      result = result * 59 + ($prevTickStatus == null ? 43 : $prevTickStatus.hashCode());
      return result;
   }

   public String toString() {
      int var10000 = this.getObjectId();
      return "DriftNet(objectId=" + var10000 + ", statusVarbit=" + this.getStatusVarbit() + ", countVarbit=" + this.getCountVarbit() + ", adjacentTiles=" + String.valueOf(this.getAdjacentTiles()) + ", net=" + String.valueOf(this.getNet()) + ", status=" + String.valueOf(this.getStatus()) + ", count=" + this.getCount() + ", prevTickStatus=" + String.valueOf(this.getPrevTickStatus()) + ")";
   }

   public DriftNet(int objectId, int statusVarbit, int countVarbit, Set<WorldPoint> adjacentTiles) {
      this.objectId = objectId;
      this.statusVarbit = statusVarbit;
      this.countVarbit = countVarbit;
      this.adjacentTiles = adjacentTiles;
   }

   public int getStatusVarbit() {
      return this.statusVarbit;
   }

   public int getCountVarbit() {
      return this.countVarbit;
   }

   public void setPrevTickStatus(DriftNetStatus prevTickStatus) {
      this.prevTickStatus = prevTickStatus;
   }
}
