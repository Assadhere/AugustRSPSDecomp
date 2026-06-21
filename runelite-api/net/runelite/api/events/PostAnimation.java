package net.runelite.api.events;

import net.runelite.api.Animation;

public final class PostAnimation {
   private final Animation animation;

   public PostAnimation(Animation animation) {
      this.animation = animation;
   }

   public Animation getAnimation() {
      return this.animation;
   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof PostAnimation)) {
         return false;
      } else {
         PostAnimation other = (PostAnimation)o;
         Object this$animation = this.getAnimation();
         Object other$animation = other.getAnimation();
         if (this$animation == null) {
            if (other$animation != null) {
               return false;
            }
         } else if (!this$animation.equals(other$animation)) {
            return false;
         }

         return true;
      }
   }

   public int hashCode() {
      int PRIME = true;
      int result = 1;
      Object $animation = this.getAnimation();
      result = result * 59 + ($animation == null ? 43 : $animation.hashCode());
      return result;
   }

   public String toString() {
      return "PostAnimation(animation=" + String.valueOf(this.getAnimation()) + ")";
   }
}
