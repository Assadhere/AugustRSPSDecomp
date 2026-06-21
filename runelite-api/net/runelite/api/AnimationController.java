package net.runelite.api;

import java.util.function.Consumer;
import java.util.function.IntPredicate;
import javax.annotation.Nullable;
import lombok.NonNull;

public class AnimationController {
   private final Client client;
   @Nullable
   private Animation animation;
   private @NonNull Consumer<AnimationController> onFinished;
   private int frame;
   private int elapsedTicks;

   public AnimationController(Client client, int animationID) {
      this(client, client.loadAnimation(animationID));
   }

   public AnimationController(Client client, Animation animation) {
      this.onFinished = AnimationController::loop;
      this.client = client;
      this.setAnimation(animation);
   }

   public void setAnimation(@Nullable Animation animation) {
      this.animation = animation;
      this.reset();
   }

   public void reset() {
      this.frame = 0;
      this.elapsedTicks = 0;
   }

   public void loop() {
      if (this.animation != null) {
         this.frame -= this.animation.getFrameStep();
         if (this.frame < 0 || this.frame >= this.animation.getDuration()) {
            this.frame = 0;
         }

      }
   }

   public void tick(int ticks) {
      if (this.animation != null) {
         if (this.animation.isMayaAnim()) {
            this.frame += ticks;
            if (this.frame >= this.animation.getDuration()) {
               this.onFinished.accept(this);
               if (this.frame < 0 || this.animation == null || this.frame >= this.animation.getDuration()) {
                  this.animation = null;
               }
            }
         } else {
            this.elapsedTicks += ticks;
            int[] frameLengths = this.animation.getFrameLengths();

            do {
               do {
                  if (this.elapsedTicks <= frameLengths[this.frame]) {
                     return;
                  }

                  this.elapsedTicks -= frameLengths[this.frame];
                  ++this.frame;
               } while(this.frame < frameLengths.length);

               this.onFinished.accept(this);
            } while(this.frame >= 0 && this.animation != null && this.frame < frameLengths.length);

            this.animation = null;
            return;
         }

      }
   }

   public Model animate(Model model) {
      return this.animate(model, (AnimationController)null);
   }

   public Model animate(Model model, @Nullable AnimationController other) {
      return other != null ? this.client.applyTransformations(model, this.animation, this.getPackedFrame(), other.animation, other.getPackedFrame()) : this.client.applyTransformations(model, this.animation, this.getPackedFrame(), (Animation)null, 0);
   }

   private int getPackedFrame() {
      if (this.animation == null) {
         return 0;
      } else {
         IntPredicate interpFilter = this.client.getAnimationInterpolationFilter();
         return interpFilter != null && interpFilter.test(this.animation.getId()) ? Integer.MIN_VALUE | this.elapsedTicks << 16 | this.frame : this.frame;
      }
   }

   @Nullable
   public Animation getAnimation() {
      return this.animation;
   }

   public AnimationController setOnFinished(@NonNull Consumer<AnimationController> onFinished) {
      if (onFinished == null) {
         throw new NullPointerException("onFinished is marked non-null but is null");
      } else {
         this.onFinished = onFinished;
         return this;
      }
   }

   public int getFrame() {
      return this.frame;
   }

   public AnimationController setFrame(int frame) {
      this.frame = frame;
      return this;
   }
}
