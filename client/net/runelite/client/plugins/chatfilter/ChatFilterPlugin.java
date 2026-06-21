package net.runelite.client.plugins.chatfilter;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.CharMatcher;
import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableSet;
import com.google.inject.Provides;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import java.util.stream.Stream;
import javax.inject.Inject;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.FriendsChatManager;
import net.runelite.api.MessageNode;
import net.runelite.api.Player;
import net.runelite.api.clan.ClanChannel;
import net.runelite.api.events.ChatMessage;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.OverheadTextChanged;
import net.runelite.api.events.ScriptCallbackEvent;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.util.Text;
import org.apache.commons.lang3.StringUtils;

@PluginDescriptor(
   name = "Chat Filter",
   description = "Censor user configurable words or patterns from chat",
   enabledByDefault = false
)
public class ChatFilterPlugin extends Plugin {
   private static final Splitter NEWLINE_SPLITTER = Splitter.on("\n").omitEmptyStrings().trimResults();
   @VisibleForTesting
   static final String CENSOR_MESSAGE = "Hey, everyone, I just tried to say something very silly!";
   private static final Set<ChatMessageType> COLLAPSIBLE_MESSAGETYPES;
   private static final CharMatcher jagexPrintableCharMatcher;
   private List<Pattern> filteredPatterns = Collections.emptyList();
   private List<Pattern> filteredNamePatterns = Collections.emptyList();
   private final LinkedHashMap<String, Duplicate> duplicateChatCache = new LinkedHashMap<String, Duplicate>() {
      private static final int MAX_ENTRIES = 100;

      protected boolean removeEldestEntry(Map.Entry<String, Duplicate> eldest) {
         return this.size() > 100;
      }
   };
   private final Map<ChatMessageType, FilterCacheMap> filterCache = new HashMap();
   @Inject
   private Client client;
   @Inject
   private ChatFilterConfig config;

   @Provides
   ChatFilterConfig provideConfig(ConfigManager configManager) {
      return (ChatFilterConfig)configManager.getConfig(ChatFilterConfig.class);
   }

   protected void startUp() throws Exception {
      this.updateFilteredPatterns();
      this.client.refreshChat();
   }

   protected void shutDown() throws Exception {
      this.filteredPatterns = Collections.emptyList();
      this.filteredNamePatterns = Collections.emptyList();
      this.duplicateChatCache.clear();
      this.filterCache.clear();
      this.client.refreshChat();
   }

   @Subscribe
   public void onGameStateChanged(GameStateChanged gameStateChanged) {
      switch (gameStateChanged.getGameState()) {
         case CONNECTION_LOST:
         case HOPPING:
         case LOGGING_IN:
            this.duplicateChatCache.values().forEach((d) -> {
               d.messageId = -1;
            });
            this.filterCache.clear();
         default:
      }
   }

   @Subscribe
   public void onScriptCallbackEvent(ScriptCallbackEvent event) {
      if ("chatFilterCheck".equals(event.getEventName())) {
         int[] intStack = this.client.getIntStack();
         int intStackSize = this.client.getIntStackSize();
         Object[] objectStack = this.client.getObjectStack();
         int objectStackSize = this.client.getObjectStackSize();
         int messageType = intStack[intStackSize - 2];
         int messageId = intStack[intStackSize - 1];
         String message = (String)objectStack[objectStackSize - 1];
         ChatMessageType chatMessageType = ChatMessageType.of(messageType);
         MessageNode messageNode = (MessageNode)this.client.getMessages().get((long)messageId);
         String name = messageNode.getName();
         int duplicateCount = 0;
         boolean blockMessage = false;
         switch (chatMessageType) {
            case PUBLICCHAT:
            case MODCHAT:
            case AUTOTYPER:
            case PRIVATECHAT:
            case MODPRIVATECHAT:
            case FRIENDSCHAT:
            case CLAN_CHAT:
            case CLAN_GUEST_CHAT:
            case CLAN_GIM_CHAT:
               if (this.canFilterPlayer(Text.sanitize(name))) {
                  message = this.censorMessage(messageNode, name, message);
                  blockMessage = message == null;
               }
               break;
            case GAMEMESSAGE:
            case ENGINE:
            case FRIENDSCHATNOTIFICATION:
            case ITEM_EXAMINE:
            case NPC_EXAMINE:
            case OBJECT_EXAMINE:
            case SPAM:
            case CLAN_MESSAGE:
            case CLAN_GUEST_MESSAGE:
            case CLAN_GIM_MESSAGE:
            case NPC_SAY:
               if (this.config.filterGameChat()) {
                  message = this.censorMessage(messageNode, (String)null, message);
                  blockMessage = message == null;
               }
         }

         boolean shouldCollapse = chatMessageType != ChatMessageType.PUBLICCHAT && chatMessageType != ChatMessageType.MODCHAT ? COLLAPSIBLE_MESSAGETYPES.contains(chatMessageType) && this.config.collapseGameChat() : this.config.collapsePlayerChat();
         if (!blockMessage && shouldCollapse) {
            Duplicate duplicateCacheEntry = (Duplicate)this.duplicateChatCache.get(name + ":" + message);
            if (duplicateCacheEntry != null && duplicateCacheEntry.messageId != -1) {
               blockMessage = duplicateCacheEntry.messageId != messageId || (chatMessageType == ChatMessageType.PUBLICCHAT || chatMessageType == ChatMessageType.MODCHAT) && this.config.maxRepeatedPublicChats() > 0 && duplicateCacheEntry.count > this.config.maxRepeatedPublicChats();
               duplicateCount = duplicateCacheEntry.count;
            }
         }

         if (blockMessage) {
            intStack[intStackSize - 3] = 0;
         } else {
            if (duplicateCount > 1) {
               message = message + " (" + duplicateCount + ")";
            }

            objectStack[objectStackSize - 1] = message;
         }

      }
   }

   @Subscribe
   public void onOverheadTextChanged(OverheadTextChanged event) {
      if (event.getActor() instanceof Player && event.getActor().getName() != null && this.canFilterPlayer(event.getActor().getName())) {
         String message = this.censorMessage(event.getActor().getName(), event.getOverheadText());
         if (message == null) {
            message = " ";
         }

         event.getActor().setOverheadText(message);
      }
   }

   @Subscribe(
      priority = -2.0F
   )
   public void onChatMessage(ChatMessage chatMessage) {
      if (COLLAPSIBLE_MESSAGETYPES.contains(chatMessage.getType())) {
         MessageNode messageNode = chatMessage.getMessageNode();
         String var10000 = messageNode.getName();
         String key = var10000 + ":" + messageNode.getValue();
         Duplicate duplicate = (Duplicate)this.duplicateChatCache.remove(key);
         if (duplicate == null) {
            duplicate = new Duplicate();
         }

         ++duplicate.count;
         duplicate.messageId = messageNode.getId();
         this.duplicateChatCache.put(key, duplicate);
      }

   }

   boolean canFilterPlayer(String playerName) {
      boolean isMessageFromSelf = playerName.equals(this.client.getLocalPlayer().getName());
      return !isMessageFromSelf && (this.config.filterFriends() || !this.client.isFriended(playerName, false)) && (this.config.filterFriendsChat() || !this.isFriendsChatMember(playerName)) && (this.config.filterClanChat() || !this.isClanChatMember(playerName));
   }

   private boolean isFriendsChatMember(String name) {
      FriendsChatManager friendsChatManager = this.client.getFriendsChatManager();
      return friendsChatManager != null && friendsChatManager.findByName(name) != null;
   }

   private boolean isClanChatMember(String name) {
      ClanChannel clanChannel = this.client.getClanChannel();
      if (clanChannel != null && clanChannel.findMember(name) != null) {
         return true;
      } else {
         clanChannel = this.client.getGuestClanChannel();
         return clanChannel != null && clanChannel.findMember(name) != null;
      }
   }

   String censorMessage(String username, String message) {
      String strippedMessage = jagexPrintableCharMatcher.retainFrom(message).replace(' ', ' ').replace("<lt>", "<").replace("<gt>", ">");
      String strippedAccents = this.stripAccents(strippedMessage);

      assert strippedMessage.length() == strippedAccents.length();

      if (username != null && this.isNameFiltered(username)) {
         switch (this.config.filterType()) {
            case CENSOR_WORDS:
               return StringUtils.repeat('*', strippedMessage.length());
            case CENSOR_MESSAGE:
               return "Hey, everyone, I just tried to say something very silly!";
            case REMOVE_MESSAGE:
               return null;
         }
      }

      boolean filtered = false;
      Iterator var6 = this.filteredPatterns.iterator();

      do {
         if (!var6.hasNext()) {
            return filtered ? strippedMessage : message;
         }

         Pattern pattern = (Pattern)var6.next();
         Matcher m = pattern.matcher(strippedAccents);
         StringBuilder sb = new StringBuilder();
         int idx = 0;

         while(m.find()) {
            switch (this.config.filterType()) {
               case CENSOR_WORDS:
                  MatchResult matchResult = m.toMatchResult();
                  sb.append(strippedMessage, idx, matchResult.start()).append(StringUtils.repeat('*', matchResult.group().length()));
                  idx = m.end();
                  filtered = true;
                  break;
               case CENSOR_MESSAGE:
                  return "Hey, everyone, I just tried to say something very silly!";
               case REMOVE_MESSAGE:
                  return null;
            }
         }

         sb.append(strippedMessage.substring(idx));
         strippedMessage = sb.toString();
      } while($assertionsDisabled || strippedMessage.length() == strippedAccents.length());

      throw new AssertionError();
   }

   private String censorMessage(MessageNode messageNode, String username, String message) {
      FilterCacheMap map = (FilterCacheMap)this.filterCache.get(messageNode.getType());
      if (map == null) {
         map = new FilterCacheMap();
         this.filterCache.put(messageNode.getType(), map);
      }

      if (map.containsKey(messageNode.getId())) {
         return (String)map.get(messageNode.getId());
      } else {
         String censoredMessage = this.censorMessage(username, message);
         map.put(messageNode.getId(), censoredMessage);
         return censoredMessage;
      }
   }

   void updateFilteredPatterns() {
      List<Pattern> patterns = new ArrayList();
      List<Pattern> namePatterns = new ArrayList();
      Stream var10000 = Text.fromCSV(this.config.filteredWords()).stream().map(this::stripAccents).map((s) -> {
         return Pattern.compile(Pattern.quote(s), 2);
      });
      Objects.requireNonNull(patterns);
      var10000.forEach(patterns::add);
      var10000 = NEWLINE_SPLITTER.splitToList(this.config.filteredRegex()).stream().map(this::stripAccents).map(ChatFilterPlugin::compilePattern).filter(Objects::nonNull);
      Objects.requireNonNull(patterns);
      var10000.forEach(patterns::add);
      var10000 = NEWLINE_SPLITTER.splitToList(this.config.filteredNames()).stream().map(this::stripAccents).map(ChatFilterPlugin::compilePattern).filter(Objects::nonNull);
      Objects.requireNonNull(namePatterns);
      var10000.forEach(namePatterns::add);
      this.filteredPatterns = patterns;
      this.filteredNamePatterns = namePatterns;
      this.filterCache.clear();
   }

   private String stripAccents(String input) {
      return this.config.stripAccents() ? StringUtils.stripAccents(input) : input;
   }

   private static Pattern compilePattern(String pattern) {
      try {
         return Pattern.compile(pattern, 2);
      } catch (PatternSyntaxException var2) {
         return null;
      }
   }

   @Subscribe
   public void onConfigChanged(ConfigChanged event) {
      if ("chatfilter".equals(event.getGroup())) {
         this.updateFilteredPatterns();
         this.client.refreshChat();
      }
   }

   @VisibleForTesting
   boolean isNameFiltered(String playerName) {
      String sanitizedName = Text.standardize(playerName);
      Iterator var3 = this.filteredNamePatterns.iterator();

      Matcher m;
      do {
         if (!var3.hasNext()) {
            return false;
         }

         Pattern pattern = (Pattern)var3.next();
         m = pattern.matcher(sanitizedName);
      } while(!m.find());

      return true;
   }

   static {
      COLLAPSIBLE_MESSAGETYPES = ImmutableSet.of(ChatMessageType.ENGINE, ChatMessageType.GAMEMESSAGE, ChatMessageType.ITEM_EXAMINE, ChatMessageType.NPC_EXAMINE, ChatMessageType.OBJECT_EXAMINE, ChatMessageType.SPAM, new ChatMessageType[]{ChatMessageType.PUBLICCHAT, ChatMessageType.MODCHAT, ChatMessageType.NPC_SAY});
      jagexPrintableCharMatcher = Text.JAGEX_PRINTABLE_CHAR_MATCHER;
   }

   private static class FilterCacheMap extends LinkedHashMap<Integer, String> {
      private static final int MAX_ENTRIES = 100;

      protected boolean removeEldestEntry(Map.Entry<Integer, String> eldest) {
         return this.size() > 100;
      }
   }

   private static class Duplicate {
      int messageId;
      int count;
   }
}
