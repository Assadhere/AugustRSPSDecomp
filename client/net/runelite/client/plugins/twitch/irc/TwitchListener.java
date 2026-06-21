package net.runelite.client.plugins.twitch.irc;

import java.util.Map;

public interface TwitchListener {
   void privmsg(String var1, Map<String, String> var2, String var3);

   void roomstate(Map<String, String> var1);

   void usernotice(Map<String, String> var1, String var2);
}
