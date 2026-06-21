package net.runelite.api.events;

import javax.annotation.Nullable;
import net.runelite.api.Actor;

public class SoundEffectPlayed {
   @Nullable
   private final Actor source;
   private int soundId;
   private int delay;
   private boolean consumed;

   public void consume() {
      this.consumed = true;
   }

   public SoundEffectPlayed(@Nullable Actor source) {
      this.source = source;
   }

   @Nullable
   public Actor getSource() {
      return this.source;
   }

   public int getSoundId() {
      return this.soundId;
   }

   public int getDelay() {
      return this.delay;
   }

   public boolean isConsumed() {
      return this.consumed;
   }

   public void setSoundId(int soundId) {
      this.soundId = soundId;
   }

   public void setDelay(int delay) {
      this.delay = delay;
   }

   public void setConsumed(boolean consumed) {
      this.consumed = consumed;
   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof SoundEffectPlayed)) {
         return false;
      } else {
         SoundEffectPlayed other = (SoundEffectPlayed)o;
         if (!other.canEqual(this)) {
            return false;
         } else if (this.getSoundId() != other.getSoundId()) {
            return false;
         } else if (this.getDelay() != other.getDelay()) {
            return false;
         } else if (this.isConsumed() != other.isConsumed()) {
            return false;
         } else {
            Object this$source = this.getSource();
            Object other$source = other.getSource();
            if (this$source == null) {
               if (other$source != null) {
                  return false;
               }
            } else if (!this$source.equals(other$source)) {
               return false;
            }

            return true;
         }
      }
   }

   protected boolean canEqual(Object other) {
      return other instanceof SoundEffectPlayed;
   }

   public int hashCode() {
      int PRIME = true;
      int result = 1;
      result = result * 59 + this.getSoundId();
      result = result * 59 + this.getDelay();
      result = result * 59 + (this.isConsumed() ? 79 : 97);
      Object $source = this.getSource();
      result = result * 59 + ($source == null ? 43 : $source.hashCode());
      return result;
   }

   public String toString() {
      String var10000 = String.valueOf(this.getSource());
      return "SoundEffectPlayed(source=" + var10000 + ", soundId=" + this.getSoundId() + ", delay=" + this.getDelay() + ", consumed=" + this.isConsumed() + ")";
   }
}
