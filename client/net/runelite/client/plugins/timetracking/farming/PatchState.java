package net.runelite.client.plugins.timetracking.farming;

final class PatchState {
   private final Produce produce;
   private final CropState cropState;
   private final int stage;

   int getStages() {
      return this.cropState != CropState.HARVESTABLE && this.cropState != CropState.FILLING ? this.produce.getStages() : this.produce.getHarvestStages();
   }

   int getTickRate() {
      switch (this.cropState) {
         case HARVESTABLE:
            return this.produce.getRegrowTickrate();
         case GROWING:
            return this.produce.getTickrate();
         default:
            return 0;
      }
   }

   public PatchState(Produce produce, CropState cropState, int stage) {
      this.produce = produce;
      this.cropState = cropState;
      this.stage = stage;
   }

   public Produce getProduce() {
      return this.produce;
   }

   public CropState getCropState() {
      return this.cropState;
   }

   public int getStage() {
      return this.stage;
   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof PatchState)) {
         return false;
      } else {
         PatchState other = (PatchState)o;
         if (this.getStage() != other.getStage()) {
            return false;
         } else {
            Object this$produce = this.getProduce();
            Object other$produce = other.getProduce();
            if (this$produce == null) {
               if (other$produce != null) {
                  return false;
               }
            } else if (!this$produce.equals(other$produce)) {
               return false;
            }

            Object this$cropState = this.getCropState();
            Object other$cropState = other.getCropState();
            if (this$cropState == null) {
               if (other$cropState != null) {
                  return false;
               }
            } else if (!this$cropState.equals(other$cropState)) {
               return false;
            }

            return true;
         }
      }
   }

   public int hashCode() {
      int PRIME = true;
      int result = 1;
      result = result * 59 + this.getStage();
      Object $produce = this.getProduce();
      result = result * 59 + ($produce == null ? 43 : $produce.hashCode());
      Object $cropState = this.getCropState();
      result = result * 59 + ($cropState == null ? 43 : $cropState.hashCode());
      return result;
   }

   public String toString() {
      String var10000 = String.valueOf(this.getProduce());
      return "PatchState(produce=" + var10000 + ", cropState=" + String.valueOf(this.getCropState()) + ", stage=" + this.getStage() + ")";
   }
}
