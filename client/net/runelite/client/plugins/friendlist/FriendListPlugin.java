package net.runelite.client.plugins.friendlist;

import com.google.inject.Provides;
import java.lang.reflect.Type;
import java.time.temporal.ChronoUnit;
import java.util.Iterator;
import javax.inject.Inject;
import net.runelite.api.ChatMessageType;
import net.runelite.api.ChatPlayer;
import net.runelite.api.Client;
import net.runelite.api.Friend;
import net.runelite.api.Ignore;
import net.runelite.api.MenuAction;
import net.runelite.api.MessageNode;
import net.runelite.api.NameableContainer;
import net.runelite.api.PendingLogin;
import net.runelite.api.events.ChatMessage;
import net.runelite.api.events.MenuEntryAdded;
import net.runelite.api.events.ScriptPostFired;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetUtil;
import net.runelite.client.chat.ChatMessageManager;
import net.runelite.client.chat.QueuedMessage;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.task.Schedule;
import net.runelite.client.util.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@PluginDescriptor(
   name = "Friend List",
   description = "Add extra information to the friend and ignore lists"
)
public class FriendListPlugin extends Plugin {
   private static final Logger log = LoggerFactory.getLogger(FriendListPlugin.class);
   private static final int MAX_FRIENDS_P2P = 400;
   private static final int MAX_FRIENDS_F2P = 200;
   private static final int MAX_IGNORES_P2P = 400;
   private static final int MAX_IGNORES_F2P = 100;
   private static final String HIDE_NOTIFICATIONS = "Hide notifications";
   private static final String SHOW_NOTIFICATIONS = "Show notifications";
   @Inject
   private Client client;
   @Inject
   private FriendListConfig config;
   @Inject
   private ConfigManager configManager;
   @Inject
   private ChatMessageManager chatMessageManager;

   @Provides
   FriendListConfig getConfig(ConfigManager configManager) {
      return (FriendListConfig)configManager.getConfig(FriendListConfig.class);
   }

   protected void shutDown() {
      int world = this.client.getWorld();
      this.setFriendsListTitle("Friends List - World " + world);
      this.setIgnoreListTitle("Ignore List - World " + world);
   }

   @Subscribe
   public void onScriptPostFired(ScriptPostFired event) {
      int world;
      boolean isMember;
      int ignoreCount;
      int limit;
      String title;
      if (event.getScriptId() == 631) {
         world = this.client.getWorld();
         isMember = this.client.getVarpValue(1780) > 0;
         NameableContainer<Friend> friendContainer = this.client.getFriendContainer();
         ignoreCount = friendContainer.getCount();
         if (ignoreCount >= 0) {
            limit = isMember ? 400 : 200;
            title = "Friends - W" + world + " (" + ignoreCount + "/" + limit + ")";
            this.setFriendsListTitle(title);
         }
      } else if (event.getScriptId() == 630) {
         world = this.client.getWorld();
         isMember = this.client.getVarpValue(1780) > 0;
         NameableContainer<Ignore> ignoreContainer = this.client.getIgnoreContainer();
         ignoreCount = ignoreContainer.getCount();
         if (ignoreCount >= 0) {
            limit = isMember ? 400 : 100;
            title = "Ignores - W" + world + " (" + ignoreCount + "/" + limit + ")";
            this.setIgnoreListTitle(title);
         }
      }

   }

   @Subscribe
   public void onChatMessage(ChatMessage message) {
      if (message.getType() == ChatMessageType.LOGINLOGOUTNOTIFICATION && this.config.showWorldOnLogin()) {
         MessageNode messageNode = message.getMessageNode();
         String name = messageNode.getValue().substring(0, messageNode.getValue().indexOf(" "));
         ChatPlayer player = this.findFriend(name);
         if (player != null && player.getWorld() > 0) {
            String var10001 = messageNode.getValue();
            messageNode.setValue(var10001 + String.format(" (World %d)", player.getWorld()));
         }
      }

   }

   @Subscribe
   public void onMenuEntryAdded(MenuEntryAdded event) {
      int groupId = WidgetUtil.componentToInterface(event.getActionParam1());
      if (groupId == 429 && event.getOption().equals("Message")) {
         String friend = Text.toJagexName(Text.removeTags(event.getTarget()));
         this.client.createMenuEntry(-1).setOption(this.isHideNotification(friend) ? "Show notifications" : "Hide notifications").setType(MenuAction.RUNELITE).setTarget(event.getTarget()).onClick((e) -> {
            boolean hidden = this.isHideNotification(friend);
            this.setHideNotifications(friend, !hidden);
            this.chatMessageManager.queue(QueuedMessage.builder().type(ChatMessageType.CONSOLE).value("Login notifications for " + friend + " are now " + (hidden ? "shown." : "hidden.")).build());
         });
      }

   }

   @Schedule(
      period = 5L,
      unit = ChronoUnit.SECONDS
   )
   public void setHideNotifications() {
      Iterator<PendingLogin> it = this.client.getFriendContainer().getPendingLogins().iterator();

      while(it.hasNext()) {
         PendingLogin pendingLogin = (PendingLogin)it.next();
         if (this.isHideNotification(Text.toJagexName(pendingLogin.getName()))) {
            log.debug("Removing login notification for {}", pendingLogin.getName());
            it.remove();
         }
      }

   }

   private void setFriendsListTitle(String title) {
      Widget friendListTitleWidget = this.client.getWidget(28114947);
      if (friendListTitleWidget != null) {
         friendListTitleWidget.setText(title);
      }

   }

   private void setIgnoreListTitle(String title) {
      Widget ignoreTitleWidget = this.client.getWidget(28311555);
      if (ignoreTitleWidget != null) {
         ignoreTitleWidget.setText(title);
      }

   }

   private ChatPlayer findFriend(String name) {
      NameableContainer<Friend> friendContainer = this.client.getFriendContainer();
      if (friendContainer != null) {
         String cleanName = Text.removeTags(name);
         return (ChatPlayer)friendContainer.findByName(cleanName);
      } else {
         return null;
      }
   }

   private void setHideNotifications(String friend, boolean hide) {
      if (hide) {
         this.configManager.setConfiguration("friendlist", "hidenotification_" + friend, (Object)true);
      } else {
         this.configManager.unsetConfiguration("friendlist", "hidenotification_" + friend);
      }

   }

   private boolean isHideNotification(String friend) {
      return this.configManager.getConfiguration("friendlist", "hidenotification_" + friend, (Type)Boolean.class) == Boolean.TRUE;
   }
}
