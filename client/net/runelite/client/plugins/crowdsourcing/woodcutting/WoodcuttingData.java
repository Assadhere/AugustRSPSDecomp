package net.runelite.client.plugins.crowdsourcing.woodcutting;

import java.util.List;
import net.runelite.api.coords.WorldPoint;
import net.runelite.client.plugins.crowdsourcing.skilling.SkillingEndReason;

public class WoodcuttingData {
   private final int level;
   private final int startTick;
   private final int endTick;
   private final List<Integer> chopTicks;
   private final List<Integer> nestTicks;
   private final int axe;
   private final int treeId;
   private final WorldPoint treeLocation;
   private final SkillingEndReason reason;

   public int getLevel() {
      return this.level;
   }

   public int getStartTick() {
      return this.startTick;
   }

   public int getEndTick() {
      return this.endTick;
   }

   public List<Integer> getChopTicks() {
      return this.chopTicks;
   }

   public List<Integer> getNestTicks() {
      return this.nestTicks;
   }

   public int getAxe() {
      return this.axe;
   }

   public int getTreeId() {
      return this.treeId;
   }

   public WorldPoint getTreeLocation() {
      return this.treeLocation;
   }

   public SkillingEndReason getReason() {
      return this.reason;
   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof WoodcuttingData)) {
         return false;
      } else {
         WoodcuttingData other = (WoodcuttingData)o;
         if (!other.canEqual(this)) {
            return false;
         } else if (this.getLevel() != other.getLevel()) {
            return false;
         } else if (this.getStartTick() != other.getStartTick()) {
            return false;
         } else if (this.getEndTick() != other.getEndTick()) {
            return false;
         } else if (this.getAxe() != other.getAxe()) {
            return false;
         } else if (this.getTreeId() != other.getTreeId()) {
            return false;
         } else {
            label71: {
               Object this$chopTicks = this.getChopTicks();
               Object other$chopTicks = other.getChopTicks();
               if (this$chopTicks == null) {
                  if (other$chopTicks == null) {
                     break label71;
                  }
               } else if (this$chopTicks.equals(other$chopTicks)) {
                  break label71;
               }

               return false;
            }

            label64: {
               Object this$nestTicks = this.getNestTicks();
               Object other$nestTicks = other.getNestTicks();
               if (this$nestTicks == null) {
                  if (other$nestTicks == null) {
                     break label64;
                  }
               } else if (this$nestTicks.equals(other$nestTicks)) {
                  break label64;
               }

               return false;
            }

            label57: {
               Object this$treeLocation = this.getTreeLocation();
               Object other$treeLocation = other.getTreeLocation();
               if (this$treeLocation == null) {
                  if (other$treeLocation == null) {
                     break label57;
                  }
               } else if (this$treeLocation.equals(other$treeLocation)) {
                  break label57;
               }

               return false;
            }

            Object this$reason = this.getReason();
            Object other$reason = other.getReason();
            if (this$reason == null) {
               if (other$reason != null) {
                  return false;
               }
            } else if (!this$reason.equals(other$reason)) {
               return false;
            }

            return true;
         }
      }
   }

   protected boolean canEqual(Object other) {
      return other instanceof WoodcuttingData;
   }

   public int hashCode() {
      int PRIME = true;
      int result = 1;
      result = result * 59 + this.getLevel();
      result = result * 59 + this.getStartTick();
      result = result * 59 + this.getEndTick();
      result = result * 59 + this.getAxe();
      result = result * 59 + this.getTreeId();
      Object $chopTicks = this.getChopTicks();
      result = result * 59 + ($chopTicks == null ? 43 : $chopTicks.hashCode());
      Object $nestTicks = this.getNestTicks();
      result = result * 59 + ($nestTicks == null ? 43 : $nestTicks.hashCode());
      Object $treeLocation = this.getTreeLocation();
      result = result * 59 + ($treeLocation == null ? 43 : $treeLocation.hashCode());
      Object $reason = this.getReason();
      result = result * 59 + ($reason == null ? 43 : $reason.hashCode());
      return result;
   }

   public String toString() {
      int var10000 = this.getLevel();
      return "WoodcuttingData(level=" + var10000 + ", startTick=" + this.getStartTick() + ", endTick=" + this.getEndTick() + ", chopTicks=" + String.valueOf(this.getChopTicks()) + ", nestTicks=" + String.valueOf(this.getNestTicks()) + ", axe=" + this.getAxe() + ", treeId=" + this.getTreeId() + ", treeLocation=" + String.valueOf(this.getTreeLocation()) + ", reason=" + String.valueOf(this.getReason()) + ")";
   }

   public WoodcuttingData(int level, int startTick, int endTick, List<Integer> chopTicks, List<Integer> nestTicks, int axe, int treeId, WorldPoint treeLocation, SkillingEndReason reason) {
      this.level = level;
      this.startTick = startTick;
      this.endTick = endTick;
      this.chopTicks = chopTicks;
      this.nestTicks = nestTicks;
      this.axe = axe;
      this.treeId = treeId;
      this.treeLocation = treeLocation;
      this.reason = reason;
   }
}
