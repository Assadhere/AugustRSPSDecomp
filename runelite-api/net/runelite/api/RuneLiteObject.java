package net.runelite.api;

import javax.annotation.Nullable;
import net.runelite.api.coords.LocalPoint;

public class RuneLiteObject extends RuneLiteObjectController {
   private final Client client;
   private Model baseModel;
   @Nullable
   private AnimationController animationController;
   @Nullable
   private AnimationController poseAnimationController;
   private int startCycle;
   private Boolean shouldLoop;

   /** @deprecated */
   @Deprecated
   public void setShouldLoop(boolean shouldLoop) {
      this.shouldLoop = shouldLoop;
      this.updateAnimationControllerLooping();
   }

   public void setModel(Model baseModel) {
      this.baseModel = baseModel;
   }

   public void setLocation(LocalPoint point, int level) {
      boolean needReregister = this.isActive() && point.getWorldView() != this.getWorldView();
      if (needReregister) {
         this.setActive(false);
      }

      super.setLocation(point, level);
      this.setZ(Perspective.getTileHeight(this.client, point, level));
      if (needReregister) {
         this.setActive(true);
      }

   }

   public void setAnimation(Animation animation) {
      this.setAnimationController(new AnimationController(this.client, animation));
   }

   public void setAnimationController(@Nullable AnimationController animationController) {
      this.animationController = animationController;
      this.updateAnimationControllerLooping();
   }

   public void setActive(boolean active) {
      if (active) {
         this.client.registerRuneLiteObject(this);
      } else {
         this.client.removeRuneLiteObject(this);
      }

   }

   public boolean isActive() {
      return this.client.isRuneLiteObjectRegistered(this);
   }

   public void tick(int ticksSinceLastFrame) {
      if (this.animationController != null) {
         this.animationController.tick(ticksSinceLastFrame);
      }

   }

   public Model getModel() {
      if (this.animationController != null) {
         return this.animationController.animate(this.baseModel, this.poseAnimationController);
      } else {
         return this.poseAnimationController != null ? this.poseAnimationController.animate(this.baseModel) : this.baseModel;
      }
   }

   /** @deprecated */
   @Deprecated
   public boolean finished() {
      return !this.isActive();
   }

   /** @deprecated */
   @Deprecated
   public void setFinished(boolean finished) {
      if (finished) {
         this.setActive(false);
      }

   }

   /** @deprecated */
   public Animation getAnimation() {
      if (this.animationController != null) {
         return this.animationController.getAnimation();
      } else {
         return this.poseAnimationController != null ? this.poseAnimationController.getAnimation() : null;
      }
   }

   /** @deprecated */
   @Deprecated
   public int getAnimationFrame() {
      if (this.animationController != null) {
         return this.animationController.getFrame();
      } else {
         return this.poseAnimationController != null ? this.poseAnimationController.getFrame() : -1;
      }
   }

   private void updateAnimationControllerLooping() {
      if (this.shouldLoop != null && this.animationController != null) {
         if (this.shouldLoop) {
            this.animationController.setOnFinished(AnimationController::loop);
         } else {
            this.animationController.setOnFinished((_ac) -> {
               this.setActive(false);
            });
         }
      }

   }

   public RuneLiteObject(Client client) {
      this.client = client;
   }

   public Model getBaseModel() {
      return this.baseModel;
   }

   @Nullable
   public AnimationController getAnimationController() {
      return this.animationController;
   }

   public void setPoseAnimationController(@Nullable AnimationController poseAnimationController) {
      this.poseAnimationController = poseAnimationController;
   }

   @Nullable
   public AnimationController getPoseAnimationController() {
      return this.poseAnimationController;
   }

   public int getStartCycle() {
      return this.startCycle;
   }

   public void setStartCycle(int startCycle) {
      this.startCycle = startCycle;
   }
}
