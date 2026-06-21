package net.runelite.client.plugins.chathistory;

import com.google.common.collect.EvictingQueue;
import com.google.inject.Provides;
import java.awt.Toolkit;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;
import java.util.Queue;
import javax.inject.Inject;
import net.runelite.api.ChatLineBuffer;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.MenuAction;
import net.runelite.api.MenuEntry;
import net.runelite.api.MessageNode;
import net.runelite.api.events.ChatMessage;
import net.runelite.api.events.MenuEntryAdded;
import net.runelite.api.events.MenuOptionClicked;
import net.runelite.api.vars.InputType;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetUtil;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.input.KeyListener;
import net.runelite.client.input.KeyManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.util.Text;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@PluginDescriptor(
   name = "Chat History",
   description = "Retain your chat history when logging in/out or world hopping",
   tags = {"chat", "history", "retain", "cycle", "pm"}
)
public class ChatHistoryPlugin extends Plugin implements KeyListener {
   private static final Logger log = LoggerFactory.getLogger(ChatHistoryPlugin.class);
   private static final String WELCOME_MESSAGE = "Welcome to Old School RuneScape";
   private static final String CLEAR_HISTORY = "Clear history";
   private static final String COPY_TO_CLIPBOARD = "Copy to clipboard";
   private static final String REPORT = "Report";
   private static final int CYCLE_HOTKEY = 9;
   private static final int FRIENDS_MAX_SIZE = 5;
   private Queue<MessageNode> messageQueue;
   private Deque<String> friends;
   @Inject
   private Client client;
   @Inject
   private ClientThread clientThread;
   @Inject
   private ChatHistoryConfig config;
   @Inject
   private KeyManager keyManager;

   @Provides
   ChatHistoryConfig getConfig(ConfigManager configManager) {
      return (ChatHistoryConfig)configManager.getConfig(ChatHistoryConfig.class);
   }

   protected void startUp() {
      this.messageQueue = EvictingQueue.create(100);
      this.friends = new ArrayDeque(6);
      this.keyManager.registerKeyListener(this);
   }

   protected void shutDown() {
      this.messageQueue.clear();
      this.messageQueue = null;
      this.friends.clear();
      this.friends = null;
      this.keyManager.unregisterKeyListener(this);
   }

   @Subscribe
   public void onChatMessage(ChatMessage chatMessage) {
      ChatMessageType chatMessageType = chatMessage.getType();
      if (chatMessageType == ChatMessageType.WELCOME && StringUtils.startsWithIgnoreCase(chatMessage.getMessage(), "Welcome to Old School RuneScape")) {
         if (this.config.retainChatHistory()) {
            Iterator var6 = this.messageQueue.iterator();

            while(var6.hasNext()) {
               MessageNode queuedMessage = (MessageNode)var6.next();
               MessageNode node = this.client.addChatMessage(queuedMessage.getType(), queuedMessage.getName(), queuedMessage.getValue(), queuedMessage.getSender(), false);
               node.setRuneLiteFormatMessage(queuedMessage.getRuneLiteFormatMessage());
               node.setTimestamp(queuedMessage.getTimestamp());
            }

         }
      } else {
         switch (chatMessageType) {
            case PRIVATECHATOUT:
            case PRIVATECHAT:
            case MODPRIVATECHAT:
               String name = Text.removeTags(chatMessage.getName());
               if (!this.friends.remove(name) && this.friends.size() >= 5) {
                  this.friends.remove();
               }

               this.friends.add(name);
            case PUBLICCHAT:
            case MODCHAT:
            case FRIENDSCHAT:
            case CLAN_GUEST_CHAT:
            case CLAN_GUEST_MESSAGE:
            case CLAN_CHAT:
            case CLAN_MESSAGE:
            case CLAN_GIM_CHAT:
            case CLAN_GIM_MESSAGE:
            case CONSOLE:
               this.messageQueue.offer(chatMessage.getMessageNode());
            default:
         }
      }
   }

   @Subscribe
   public void onMenuOptionClicked(MenuOptionClicked event) {
      String menuOption = event.getMenuOption();
      if (menuOption.endsWith("Clear history")) {
         this.clearChatboxHistory(ChatboxTab.of(event.getParam1()));
      }

   }

   @Subscribe
   public void onMenuEntryAdded(MenuEntryAdded entry) {
      if (entry.getType() == MenuAction.CC_OP.getId() || entry.getType() == MenuAction.CC_OP_LOW_PRIORITY.getId()) {
         ChatboxTab tab = ChatboxTab.of(entry.getActionParam1());
         if (tab != null && tab.getAfter() != null && this.config.clearHistory() && entry.getOption().endsWith(tab.getAfter())) {
            MenuEntry clearEntry = this.client.createMenuEntry(-2).setType(MenuAction.RUNELITE_HIGH_PRIORITY);
            clearEntry.setParam1(entry.getActionParam1());
            StringBuilder optionBuilder = new StringBuilder();
            if (tab != ChatboxTab.ALL) {
               String option = entry.getOption();
               int idx = option.indexOf(58);
               if (idx != -1) {
                  optionBuilder.append(option, 0, idx).append(":</col> ");
               }
            }

            optionBuilder.append("Clear history");
            clearEntry.setOption(optionBuilder.toString());
         } else {
            if (entry.getOption().equals("Report") && this.config.copyToClipboard()) {
               int groupId = WidgetUtil.componentToInterface(entry.getActionParam1());
               int childId = WidgetUtil.componentToId(entry.getActionParam1());
               if (groupId != 162) {
                  return;
               }

               Widget widget = this.client.getWidget(groupId, childId);
               Widget parent = widget.getParent();
               if (10616890 != parent.getId()) {
                  return;
               }

               int first = WidgetUtil.componentToId(10616891);
               int dynamicChildId = (childId - first) * 4 + 1;
               Widget messageContents = parent.getChild(dynamicChildId);
               if (messageContents == null) {
                  return;
               }

               String currentMessage = messageContents.getText();
               this.client.createMenuEntry(1).setOption("Copy to clipboard").setTarget(entry.getTarget()).setType(MenuAction.RUNELITE).onClick((e) -> {
                  StringSelection stringSelection = new StringSelection(Text.removeTags(currentMessage));
                  Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringSelection, (ClipboardOwner)null);
               });
            }

         }
      }
   }

   private void clearMessageQueue(ChatboxTab tab) {
      if (tab == ChatboxTab.ALL || tab == ChatboxTab.PRIVATE) {
         this.friends.clear();
      }

      this.messageQueue.removeIf((e) -> {
         return ArrayUtils.contains(tab.getMessageTypes(), e.getType());
      });
   }

   private void clearChatboxHistory(ChatboxTab tab) {
      if (tab != null) {
         log.debug("Clearing chatbox history for tab {}", tab);
         this.clearMessageQueue(tab);
         if (tab.getAfter() != null) {
            boolean removed = false;
            ChatMessageType[] var3 = tab.getMessageTypes();
            int var4 = var3.length;

            for(int var5 = 0; var5 < var4; ++var5) {
               ChatMessageType msgType = var3[var5];
               ChatLineBuffer lineBuffer = (ChatLineBuffer)this.client.getChatLineMap().get(msgType.getType());
               if (lineBuffer != null) {
                  MessageNode[] lines = (MessageNode[])lineBuffer.getLines().clone();
                  MessageNode[] var9 = lines;
                  int var10 = lines.length;

                  for(int var11 = 0; var11 < var10; ++var11) {
                     MessageNode line = var9[var11];
                     if (line != null) {
                        lineBuffer.removeMessageNode(line);
                        removed = true;
                     }
                  }
               }
            }

            if (removed) {
               this.clientThread.invoke(() -> {
                  this.client.runScript(new Object[]{83});
               });
            }

         }
      }
   }

   public void keyPressed(KeyEvent e) {
      if (e.getKeyCode() == 9 && this.config.pmTargetCycling()) {
         if (this.client.getVarcIntValue(5) == InputType.PRIVATE_MESSAGE.getType()) {
            this.clientThread.invoke(() -> {
               String target = this.findPreviousFriend();
               if (target != null) {
                  String currentMessage = this.client.getVarcStrValue(359);
                  this.client.runScript(new Object[]{107, target});
                  this.client.setVarcStrValue(359, currentMessage);
                  this.client.runScript(new Object[]{222, ""});
               }
            });
         }
      }
   }

   public void keyTyped(KeyEvent e) {
   }

   public void keyReleased(KeyEvent e) {
   }

   private String findPreviousFriend() {
      String currentTarget = this.client.getVarcStrValue(360);
      if (currentTarget != null && !this.friends.isEmpty()) {
         Iterator<String> it = this.friends.descendingIterator();

         String friend;
         do {
            if (!it.hasNext()) {
               return (String)this.friends.getLast();
            }

            friend = (String)it.next();
         } while(!friend.equals(currentTarget));

         return it.hasNext() ? (String)it.next() : (String)this.friends.getLast();
      } else {
         return null;
      }
   }
}
