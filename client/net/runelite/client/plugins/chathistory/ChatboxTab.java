package net.runelite.client.plugins.chathistory;

import com.google.common.collect.ImmutableMap;
import java.util.Map;
import javax.annotation.Nullable;
import net.runelite.api.ChatMessageType;

enum ChatboxTab {
   ALL("Switch tab", 10616836, ChatMessageType.values()),
   PRIVATE((String)null, 10616847, new ChatMessageType[]{ChatMessageType.PRIVATECHAT, ChatMessageType.PRIVATECHATOUT, ChatMessageType.MODPRIVATECHAT, ChatMessageType.LOGINLOGOUTNOTIFICATION}),
   PUBLIC((String)null, 10616843, new ChatMessageType[]{ChatMessageType.PUBLICCHAT, ChatMessageType.AUTOTYPER, ChatMessageType.MODCHAT, ChatMessageType.MODAUTOTYPER}),
   GAME("Filter", 10616839, new ChatMessageType[]{ChatMessageType.GAMEMESSAGE, ChatMessageType.ENGINE, ChatMessageType.BROADCAST, ChatMessageType.SNAPSHOTFEEDBACK, ChatMessageType.ITEM_EXAMINE, ChatMessageType.NPC_EXAMINE, ChatMessageType.OBJECT_EXAMINE, ChatMessageType.FRIENDNOTIFICATION, ChatMessageType.IGNORENOTIFICATION, ChatMessageType.CONSOLE, ChatMessageType.SPAM, ChatMessageType.PLAYERRELATED, ChatMessageType.TENSECTIMEOUT, ChatMessageType.WELCOME, ChatMessageType.UNKNOWN}),
   CHANNEL((String)null, 10616851, new ChatMessageType[]{ChatMessageType.FRIENDSCHATNOTIFICATION, ChatMessageType.FRIENDSCHAT, ChatMessageType.CHALREQ_FRIENDSCHAT}),
   CLAN((String)null, 10616855, new ChatMessageType[]{ChatMessageType.CLAN_CHAT, ChatMessageType.CLAN_MESSAGE, ChatMessageType.CLAN_GUEST_CHAT, ChatMessageType.CLAN_GUEST_MESSAGE}),
   TRADE_GROUP("Trade:</col> Show none", 10616859, new ChatMessageType[]{ChatMessageType.TRADE_SENT, ChatMessageType.TRADEREQ, ChatMessageType.TRADE, ChatMessageType.CHALREQ_TRADE, ChatMessageType.CLAN_GIM_CHAT, ChatMessageType.CLAN_GIM_MESSAGE});

   private static final Map<Integer, ChatboxTab> TAB_MESSAGE_TYPES;
   @Nullable
   private final String after;
   private final int widgetId;
   private final ChatMessageType[] messageTypes;

   private ChatboxTab(String after, int widgetId, ChatMessageType... messageTypes) {
      this.after = after;
      this.widgetId = widgetId;
      this.messageTypes = messageTypes;
   }

   static ChatboxTab of(int widgetId) {
      return (ChatboxTab)TAB_MESSAGE_TYPES.get(widgetId);
   }

   @Nullable
   public String getAfter() {
      return this.after;
   }

   public int getWidgetId() {
      return this.widgetId;
   }

   public ChatMessageType[] getMessageTypes() {
      return this.messageTypes;
   }

   static {
      ImmutableMap.Builder<Integer, ChatboxTab> builder = ImmutableMap.builder();
      ChatboxTab[] var1 = values();
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         ChatboxTab t = var1[var3];
         builder.put(t.widgetId, t);
      }

      TAB_MESSAGE_TYPES = builder.build();
   }
}
