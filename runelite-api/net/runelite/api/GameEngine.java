package net.runelite.api;

import java.awt.Canvas;

public interface GameEngine {
   void setConfiguration(ClientConfiguration var1);

   void initialize();

   Canvas getCanvas();

   Thread getClientThread();

   boolean isClientThread();

   void resizeCanvas();

   void unblockStartup();
}
