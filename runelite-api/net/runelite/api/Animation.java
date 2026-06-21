package net.runelite.api;

public interface Animation {
   int getId();

   boolean isMayaAnim();

   int getNumFrames();

   int getRestartMode();

   void setRestartMode(int var1);

   int getDuration();

   int getFrameStep();

   int[] getFrameLengths();
}
