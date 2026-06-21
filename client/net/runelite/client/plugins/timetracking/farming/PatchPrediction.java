package net.runelite.client.plugins.timetracking.farming;

final class PatchPrediction {
   private final Produce produce;
   private final CropState cropState;
   private final long doneEstimate;
   private final int stage;
   private final int stages;

   public PatchPrediction(Produce produce, CropState cropState, long doneEstimate, int stage, int stages) {
      this.produce = produce;
      this.cropState = cropState;
      this.doneEstimate = doneEstimate;
      this.stage = stage;
      this.stages = stages;
   }

   public Produce getProduce() {
      return this.produce;
   }

   public CropState getCropState() {
      return this.cropState;
   }

   public long getDoneEstimate() {
      return this.doneEstimate;
   }

   public int getStage() {
      return this.stage;
   }

   public int getStages() {
      return this.stages;
   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof PatchPrediction)) {
         return false;
      } else {
         PatchPrediction other = (PatchPrediction)o;
         if (this.getDoneEstimate() != other.getDoneEstimate()) {
            return false;
         } else if (this.getStage() != other.getStage()) {
            return false;
         } else if (this.getStages() != other.getStages()) {
            return false;
         } else {
            label40: {
               Object this$produce = this.getProduce();
               Object other$produce = other.getProduce();
               if (this$produce == null) {
                  if (other$produce == null) {
                     break label40;
                  }
               } else if (this$produce.equals(other$produce)) {
                  break label40;
               }

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
      long $doneEstimate = this.getDoneEstimate();
      result = result * 59 + (int)($doneEstimate >>> 32 ^ $doneEstimate);
      result = result * 59 + this.getStage();
      result = result * 59 + this.getStages();
      Object $produce = this.getProduce();
      result = result * 59 + ($produce == null ? 43 : $produce.hashCode());
      Object $cropState = this.getCropState();
      result = result * 59 + ($cropState == null ? 43 : $cropState.hashCode());
      return result;
   }

   public String toString() {
      String var10000 = String.valueOf(this.getProduce());
      return "PatchPrediction(produce=" + var10000 + ", cropState=" + String.valueOf(this.getCropState()) + ", doneEstimate=" + this.getDoneEstimate() + ", stage=" + this.getStage() + ", stages=" + this.getStages() + ")";
   }
}
