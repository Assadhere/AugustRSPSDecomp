package net.runelite.api;

public interface Preferences {
   String getRememberedUsername();

   void setRememberedUsername(String var1);

   int getSoundEffectVolume();

   void setSoundEffectVolume(int var1);

   int getAreaSoundEffectVolume();

   void setAreaSoundEffectVolume(int var1);

   boolean getHideUsername();
}
