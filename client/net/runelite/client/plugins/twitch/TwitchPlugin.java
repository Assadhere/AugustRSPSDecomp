package net.runelite.client.plugins.twitch;

import com.google.common.base.Strings;
import com.google.inject.Provides;
import java.io.IOException;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import javax.inject.Inject;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.events.ScriptCallbackEvent;
import net.runelite.client.chat.ChatColorType;
import net.runelite.client.chat.ChatMessageBuilder;
import net.runelite.client.chat.ChatMessageManager;
import net.runelite.client.chat.QueuedMessage;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.twitch.irc.TwitchIRCClient;
import net.runelite.client.plugins.twitch.irc.TwitchListener;
import net.runelite.client.task.Schedule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@PluginDescriptor(
   name = "Twitch",
   description = "Integrates Twitch chat",
   enabledByDefault = false
)
public class TwitchPlugin extends Plugin implements TwitchListener {
   private static final Logger log = LoggerFactory.getLogger(TwitchPlugin.class);
   @Inject
   private TwitchConfig twitchConfig;
   @Inject
   private Client client;
   @Inject
   private ChatMessageManager chatMessageManager;
   private TwitchIRCClient twitchIRCClient;

   protected void startUp() {
      this.connect();
   }

   protected void shutDown() {
      if (this.twitchIRCClient != null) {
         this.twitchIRCClient.close();
         this.twitchIRCClient = null;
      }

   }

   @Provides
   TwitchConfig provideConfig(ConfigManager configManager) {
      return (TwitchConfig)configManager.getConfig(TwitchConfig.class);
   }

   private synchronized void connect() {
      if (this.twitchIRCClient != null) {
         log.debug("Terminating Twitch client {}", this.twitchIRCClient);
         this.twitchIRCClient.close();
         this.twitchIRCClient = null;
      }

      if (!Strings.isNullOrEmpty(this.twitchConfig.username()) && !Strings.isNullOrEmpty(this.twitchConfig.oauthToken()) && !Strings.isNullOrEmpty(this.twitchConfig.channel())) {
         String channel = this.twitchConfig.channel().toLowerCase();
         if (channel.startsWith("https://www.twitch.tv/")) {
            channel = channel.substring("https://www.twitch.tv/".length());
         }

         if (!channel.startsWith("#")) {
            channel = "#" + channel;
         }

         String token = this.twitchConfig.oauthToken().trim();
         if (!token.startsWith("oauth:")) {
            token = "oauth:" + token;
         }

         log.debug("Connecting to Twitch as {}", this.twitchConfig.username());
         this.twitchIRCClient = new TwitchIRCClient(this, this.twitchConfig.username(), token, channel);
         this.twitchIRCClient.start();
      }

   }

   @Schedule(
      period = 30L,
      unit = ChronoUnit.SECONDS,
      asynchronous = true
   )
   public void checkClient() {
      if (this.twitchIRCClient != null) {
         if (this.twitchIRCClient.isConnected()) {
            this.twitchIRCClient.pingCheck();
         }

         if (!this.twitchIRCClient.isConnected()) {
            log.debug("Reconnecting...");
            this.connect();
         }
      }

   }

   @Subscribe
   public void onConfigChanged(ConfigChanged configChanged) {
      if (configChanged.getGroup().equals("twitch")) {
         this.connect();
      }
   }

   private void addChatMessage(String sender, String message) {
      String chatMessage = (new ChatMessageBuilder()).append(ChatColorType.NORMAL).append(message).build();
      this.chatMessageManager.queue(QueuedMessage.builder().type(ChatMessageType.FRIENDSCHAT).sender("Twitch").name(sender).runeLiteFormattedMessage(chatMessage).timestamp((int)(System.currentTimeMillis() / 1000L)).build());
   }

   public void privmsg(String source, Map<String, String> tags, String message) {
      if (this.client.getGameState() == GameState.LOGGED_IN) {
         String displayName = (String)tags.get("display-name");
         String name = source.equalsIgnoreCase(displayName) ? displayName : source;
         this.addChatMessage(name, message);
      }
   }

   public void roomstate(Map<String, String> tags) {
      log.debug("Room state: {}", tags);
   }

   public void usernotice(Map<String, String> tags, String message) {
      log.debug("Usernotice tags: {} message: {}", tags, message);
      if (this.client.getGameState() == GameState.LOGGED_IN) {
         String sysmsg = (String)tags.get("system-msg");
         this.addChatMessage("[System]", sysmsg);
      }
   }

   @Subscribe
   public void onScriptCallbackEvent(ScriptCallbackEvent scriptCallbackEvent) {
      if ("chatDefaultReturn".equals(scriptCallbackEvent.getEventName())) {
         int[] intStack = this.client.getIntStack();
         int intStackCount = this.client.getIntStackSize();
         String message = this.client.getVarcStrValue(335);
         if (message.startsWith("/t ")) {
            message = message.substring("/t ".length());
            intStack[intStackCount - 3] = 1;
            if (message.isEmpty() || this.twitchIRCClient == null) {
               return;
            }

            try {
               this.twitchIRCClient.privmsg(message);
               this.addChatMessage(this.twitchConfig.username(), message);
            } catch (IOException var6) {
               IOException e = var6;
               log.warn("failed to send message", e);
            }
         }

      }
   }
}
