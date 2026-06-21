package net.runelite.api.events;

import net.runelite.api.AmbientSoundEffect;

public final class AmbientSoundEffectCreated {
   private final AmbientSoundEffect ambientSoundEffect;

   public AmbientSoundEffectCreated(AmbientSoundEffect ambientSoundEffect) {
      this.ambientSoundEffect = ambientSoundEffect;
   }

   public AmbientSoundEffect getAmbientSoundEffect() {
      return this.ambientSoundEffect;
   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof AmbientSoundEffectCreated)) {
         return false;
      } else {
         AmbientSoundEffectCreated other = (AmbientSoundEffectCreated)o;
         Object this$ambientSoundEffect = this.getAmbientSoundEffect();
         Object other$ambientSoundEffect = other.getAmbientSoundEffect();
         if (this$ambientSoundEffect == null) {
            if (other$ambientSoundEffect != null) {
               return false;
            }
         } else if (!this$ambientSoundEffect.equals(other$ambientSoundEffect)) {
            return false;
         }

         return true;
      }
   }

   public int hashCode() {
      int PRIME = true;
      int result = 1;
      Object $ambientSoundEffect = this.getAmbientSoundEffect();
      result = result * 59 + ($ambientSoundEffect == null ? 43 : $ambientSoundEffect.hashCode());
      return result;
   }

   public String toString() {
      return "AmbientSoundEffectCreated(ambientSoundEffect=" + String.valueOf(this.getAmbientSoundEffect()) + ")";
   }
}
