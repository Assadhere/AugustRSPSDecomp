package net.runelite.api;

public interface DynamicObject extends Renderable {
   Animation getAnimation();

   int getAnimFrame();

   int getAnimCycle();

   Model getModelZbuf();

   ObjectComposition getRecordedObjectComposition();
}
