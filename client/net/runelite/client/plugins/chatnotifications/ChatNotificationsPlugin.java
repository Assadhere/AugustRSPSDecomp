package net.runelite.client.plugins.chatnotifications;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.MoreObjects;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.inject.Provides;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.inject.Inject;
import javax.inject.Named;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.MessageNode;
import net.runelite.api.events.ChatMessage;
import net.runelite.api.events.GameStateChanged;
import net.runelite.client.Notifier;
import net.runelite.client.chat.ChatColorType;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.config.Notification;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.util.Text;

@PluginDescriptor(
   name = "Chat Notifications",
   description = "Highlight and notify you of chat messages",
   tags = {"duel", "messages", "notifications", "trade", "username"},
   enabledByDefault = false
)
public class ChatNotificationsPlugin extends Plugin {
   @Inject
   private Client client;
   @Inject
   private ChatNotificationsConfig config;
   @Inject
   private Notifier notifier;
   @Inject
   @Named("runelite.title")
   private String runeliteTitle;
   private Pattern usernameMatcher = null;
   private final List<Pattern> highlightPatterns = new ArrayList();

   @Provides
   ChatNotificationsConfig provideConfig(ConfigManager configManager) {
      return (ChatNotificationsConfig)configManager.getConfig(ChatNotificationsConfig.class);
   }

   public void startUp() {
      this.updateHighlights();
   }

   protected void shutDown() {
      this.usernameMatcher = null;
      this.highlightPatterns.clear();
   }

   @Subscribe
   public void onGameStateChanged(GameStateChanged event) {
      switch (event.getGameState()) {
         case LOGIN_SCREEN:
         case HOPPING:
            this.usernameMatcher = null;
         default:
      }
   }

   @Subscribe
   public void onConfigChanged(ConfigChanged event) {
      if (event.getGroup().equals("chatnotification")) {
         this.updateHighlights();
      }

   }

   private void updateHighlights() {
      this.highlightPatterns.clear();
      if (!this.config.highlightWordsString().trim().equals("")) {
         List<String> items = Text.fromCSV(this.config.highlightWordsString());
         String joined = (String)items.stream().map(Text::escapeJagex).map(this::quoteAndIgnoreColor).collect(Collectors.joining("|"));
         this.highlightPatterns.add(Pattern.compile("(?:\\b|(?<=\\s)|\\A)(?:" + joined + ")(?:\\b|(?=\\s)|\\z)", 2));
      }

      Stream var10000 = Splitter.on("\n").omitEmptyStrings().trimResults().splitToList(this.config.highlightRegexString()).stream().map(ChatNotificationsPlugin::compilePattern).filter(Objects::nonNull);
      List var10001 = this.highlightPatterns;
      Objects.requireNonNull(var10001);
      var10000.forEach(var10001::add);
   }

   private static Pattern compilePattern(String pattern) {
      try {
         return Pattern.compile(pattern, 2);
      } catch (PatternSyntaxException var2) {
         return null;
      }
   }

   @Subscribe
   public void onChatMessage(ChatMessage chatMessage) {
      MessageNode messageNode = chatMessage.getMessageNode();
      boolean update = false;
      String message;
      switch (chatMessage.getType()) {
         case TRADEREQ:
            if (chatMessage.getMessage().contains("wishes to trade with you.")) {
               this.notifier.notify(this.config.notifyOnTrade(), chatMessage.getMessage());
            }
            break;
         case CHALREQ_TRADE:
            if (chatMessage.getMessage().contains("wishes to duel with you.")) {
               this.notifier.notify(this.config.notifyOnDuel(), chatMessage.getMessage());
            }
            break;
         case BROADCAST:
            message = chatMessage.getMessage();
            int urlTokenIndex = message.lastIndexOf(124);
            if (urlTokenIndex != -1) {
               message = message.substring(0, urlTokenIndex);
            }

            this.notifier.notify(this.config.notifyOnBroadcast(), Text.removeFormattingTags(message));
            break;
         case PRIVATECHAT:
         case MODPRIVATECHAT:
            Notifier var10000 = this.notifier;
            Notification var10001 = this.config.notifyOnPM();
            String var10002 = Text.removeTags(chatMessage.getName());
            var10000.notify(var10001, var10002 + ": " + chatMessage.getMessage());
            break;
         case PRIVATECHATOUT:
         case DIALOG:
         case MESBOX:
            return;
         case MODCHAT:
         case PUBLICCHAT:
         case FRIENDSCHAT:
         case CLAN_CHAT:
         case CLAN_GUEST_CHAT:
         case CLAN_GIM_CHAT:
         case AUTOTYPER:
         case MODAUTOTYPER:
            if (this.client.getLocalPlayer() != null && Text.toJagexName(Text.removeTags(chatMessage.getName())).equals(this.client.getLocalPlayer().getName())) {
               return;
            }
            break;
         case CONSOLE:
            if (chatMessage.getName().equals(this.runeliteTitle)) {
               return;
            }
      }

      String nodeValue;
      if (this.usernameMatcher == null && this.client.getLocalPlayer() != null && this.client.getLocalPlayer().getName() != null) {
         message = this.client.getLocalPlayer().getName();
         nodeValue = (String)Arrays.stream(message.split(" ")).map((s) -> {
            return s.isEmpty() ? "" : Pattern.quote(s);
         }).collect(Collectors.joining("[  ]"));
         this.usernameMatcher = Pattern.compile("\\b" + nodeValue + "\\b", 2);
      }

      if (this.config.highlightOwnName() && this.usernameMatcher != null) {
         message = messageNode.getValue();
         Matcher matcher = this.usernameMatcher.matcher(message);
         if (matcher.find()) {
            String username = this.client.getLocalPlayer().getName();
            StringBuffer stringBuffer = new StringBuffer();

            do {
               int start = matcher.start();
               String closeColor = (String)MoreObjects.firstNonNull(getLastColor(message.substring(0, start)), "<col" + String.valueOf(ChatColorType.NORMAL) + ">");
               String replacement = "<col" + ChatColorType.HIGHLIGHT.name() + "><u>" + username + "</u>" + closeColor;
               matcher.appendReplacement(stringBuffer, replacement);
            } while(matcher.find());

            matcher.appendTail(stringBuffer);
            messageNode.setValue(stringBuffer.toString());
            update = true;
            if (chatMessage.getType() == ChatMessageType.PUBLICCHAT || chatMessage.getType() == ChatMessageType.PRIVATECHAT || chatMessage.getType() == ChatMessageType.FRIENDSCHAT || chatMessage.getType() == ChatMessageType.MODCHAT || chatMessage.getType() == ChatMessageType.MODPRIVATECHAT || chatMessage.getType() == ChatMessageType.CLAN_CHAT || chatMessage.getType() == ChatMessageType.CLAN_GUEST_CHAT) {
               this.sendNotification(this.config.notifyOnOwnName(), chatMessage);
            }
         }
      }

      boolean matchesHighlight = false;
      nodeValue = messageNode.getValue();
      Iterator var16 = this.highlightPatterns.iterator();

      while(true) {
         Matcher matcher;
         do {
            if (!var16.hasNext()) {
               if (matchesHighlight) {
                  messageNode.setValue(nodeValue);
                  this.sendNotification(this.config.notifyOnHighlight(), chatMessage);
               }

               if (update) {
                  messageNode.setRuneLiteFormatMessage(messageNode.getValue());
               }

               return;
            }

            Pattern pattern = (Pattern)var16.next();
            matcher = pattern.matcher(nodeValue);
         } while(!matcher.find());

         StringBuffer stringBuffer = new StringBuffer();

         do {
            int end = matcher.end();
            String closeColor = (String)MoreObjects.firstNonNull(getLastColor(nodeValue.substring(0, end)), "<col" + String.valueOf(ChatColorType.NORMAL) + ">");
            String value = stripColor(matcher.group());
            matcher.appendReplacement(stringBuffer, "<col" + String.valueOf(ChatColorType.HIGHLIGHT) + ">" + value + closeColor);
            update = true;
            matchesHighlight = true;
         } while(matcher.find());

         matcher.appendTail(stringBuffer);
         nodeValue = stringBuffer.toString();
      }
   }

   private void sendNotification(Notification notification, ChatMessage message) {
      String name = Text.removeTags(message.getName());
      String sender = message.getSender();
      StringBuilder stringBuilder = new StringBuilder();
      if (!Strings.isNullOrEmpty(sender)) {
         stringBuilder.append('[').append(sender).append("] ");
      }

      if (!Strings.isNullOrEmpty(name)) {
         stringBuilder.append(name).append(": ");
      }

      stringBuilder.append(Text.removeTags(message.getMessage()));
      String m = stringBuilder.toString();
      this.notifier.notify(notification, m);
   }

   private String quoteAndIgnoreColor(String str) {
      StringBuilder stringBuilder = new StringBuilder();

      for(int i = 0; i < str.length(); ++i) {
         char c = str.charAt(i);
         stringBuilder.append(Pattern.quote(String.valueOf(c)));
         stringBuilder.append("(?:<col=[^>]*?>)?");
      }

      return stringBuilder.toString();
   }

   private static String getLastColor(String str) {
      int colIdx = str.lastIndexOf("<col=");
      int colEndIdx = str.lastIndexOf("</col>");
      if (colEndIdx > colIdx) {
         return "<col" + String.valueOf(ChatColorType.NORMAL) + ">";
      } else if (colIdx == -1) {
         return null;
      } else {
         int closeIdx = str.indexOf(62, colIdx);
         return closeIdx == -1 ? null : str.substring(colIdx, closeIdx + 1);
      }
   }

   @VisibleForTesting
   static String stripColor(String str) {
      return str.replaceAll("(<col=[0-9a-f]+>|</col>)", "");
   }
}
