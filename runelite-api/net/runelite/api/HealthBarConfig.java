package net.runelite.api;

public interface HealthBarConfig {
   SpritePixels getHealthBarFrontSprite();

   SpritePixels getHealthBarBackSprite();

   int getHealthBarFrontSpriteId();

   void setPadding(int var1);
}
