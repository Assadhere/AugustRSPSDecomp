package net.runelite.api;

public interface Renderable extends Node {
   int RENDERMODE_DEFAULT = 0;
   int RENDERMODE_SORTED = 1;
   int RENDERMODE_SORTED_NO_DEPTH = 2;
   int RENDERMODE_UNSORTED = 3;

   Model getModel();

   int getModelHeight();

   void setModelHeight(int var1);

   int getAnimationHeightOffset();

   int getRenderMode();
}
