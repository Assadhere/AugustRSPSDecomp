package net.runelite.client.plugins.chathistory;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("chathistory")
public interface ChatHistoryConfig extends Config {
   @ConfigItem(
      keyName = "retainChatHistory",
      name = "Retain chat history",
      description = "Retains chat history when logging in/out or world hopping.",
      position = 0
   )
   default boolean retainChatHistory() {
      return true;
   }

   @ConfigItem(
      keyName = "pmTargetCycling",
      name = "PM target cycling",
      description = "Pressing tab while sending a PM will cycle the target username based on PM history.",
      position = 1
   )
   default boolean pmTargetCycling() {
      return true;
   }

   @ConfigItem(
      keyName = "copyToClipboard",
      name = "Copy to clipboard",
      description = "Add option on chat messages to copy them to clipboard.",
      position = 2
   )
   default boolean copyToClipboard() {
      return true;
   }

   @ConfigItem(
      keyName = "clearHistory",
      name = "Clear history option for all tabs",
      description = "Add 'Clear history' option chatbox tab buttons.",
      position = 3
   )
   default boolean clearHistory() {
      return true;
   }
}
