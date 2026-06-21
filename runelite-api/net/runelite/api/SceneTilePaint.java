package net.runelite.api;

public interface SceneTilePaint {
   int getRBG();

   int getSwColor();

   void setSwColor(int var1);

   int getSeColor();

   void setSeColor(int var1);

   int getNwColor();

   void setNwColor(int var1);

   int getNeColor();

   void setNeColor(int var1);

   int getTexture();

   void setTexture(int var1);

   boolean isFlat();

   int getBufferOffset();

   void setBufferOffset(int var1);

   int getUvBufferOffset();

   void setUvBufferOffset(int var1);

   int getBufferLen();

   void setBufferLen(int var1);
}
