package net.runelite.client.plugins.loginscreen;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("loginscreen")
public interface LoginScreenConfig extends Config {
   @ConfigItem(
      keyName = "syncusername",
      name = "Sync username",
      description = "Syncs the username that is currently remembered between computers."
   )
   default boolean syncUsername() {
      return true;
   }

   @ConfigItem(
      keyName = "pasteenabled",
      name = "Ctrl-V paste",
      description = "Enables Ctrl-V pasting on the login screen."
   )
   default boolean pasteEnabled() {
      return false;
   }

   @ConfigItem(
      keyName = "username",
      name = "",
      description = "",
      hidden = true
   )
   default String username() {
      return "";
   }

   @ConfigItem(
      keyName = "username",
      name = "",
      description = ""
   )
   void username(String var1);

   @ConfigItem(
      keyName = "loginScreen",
      name = "Background",
      description = "Change the login screen background to use an image from the past, or a custom one."
   )
   default LoginScreenOverride loginScreen() {
      return LoginScreenOverride.OFF;
   }

   @ConfigItem(
      keyName = "showLoginFire",
      name = "Display fire",
      description = "Whether the flames above the braziers on the login sceen should be drawn."
   )
   default boolean showLoginFire() {
      return true;
   }
}
