package net.runelite.client.plugins.friendnotes;

import com.google.common.base.Strings;
import com.google.inject.Provides;
import java.awt.Color;
import java.awt.image.BufferedImage;
import javax.annotation.Nullable;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.Friend;
import net.runelite.api.GameState;
import net.runelite.api.Ignore;
import net.runelite.api.MenuAction;
import net.runelite.api.Nameable;
import net.runelite.api.events.MenuEntryAdded;
import net.runelite.api.events.NameableNameChanged;
import net.runelite.api.events.RemovedFriend;
import net.runelite.api.events.ScriptCallbackEvent;
import net.runelite.api.widgets.WidgetUtil;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.game.ChatIconManager;
import net.runelite.client.game.chatbox.ChatboxPanelManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;
import net.runelite.client.util.ColorUtil;
import net.runelite.client.util.ImageUtil;
import net.runelite.client.util.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@PluginDescriptor(
   name = "Friend Notes",
   description = "Store notes about your friends"
)
public class FriendNotesPlugin extends Plugin {
   private static final Logger log = LoggerFactory.getLogger(FriendNotesPlugin.class);
   static final String CONFIG_GROUP = "friendNotes";
   private static final int CHARACTER_LIMIT = 128;
   private static final String KEY_PREFIX = "note_";
   private static final String ADD_NOTE = "Add Note";
   private static final String EDIT_NOTE = "Edit Note";
   private static final String NOTE_PROMPT_FORMAT;
   private static final int ICON_WIDTH = 14;
   private static final int ICON_HEIGHT = 12;
   @Inject
   private Client client;
   @Inject
   private ConfigManager configManager;
   @Inject
   private OverlayManager overlayManager;
   @Inject
   private FriendNoteOverlay overlay;
   @Inject
   private ChatboxPanelManager chatboxPanelManager;
   @Inject
   private ClientThread clientThread;
   @Inject
   private FriendNotesConfig config;
   @Inject
   private ChatIconManager chatIconManager;
   private HoveredFriend hoveredFriend = null;
   private int iconId = -1;
   private String currentlyLayouting;

   @Provides
   private FriendNotesConfig getConfig(ConfigManager configManager) {
      return (FriendNotesConfig)configManager.getConfig(FriendNotesConfig.class);
   }

   protected void startUp() throws Exception {
      this.overlayManager.add(this.overlay);
      this.loadIcon();
      if (this.client.getGameState() == GameState.LOGGED_IN) {
         this.rebuildFriendsList();
         this.rebuildIgnoreList();
      }

   }

   protected void shutDown() throws Exception {
      this.overlayManager.remove(this.overlay);
      if (this.client.getGameState() == GameState.LOGGED_IN) {
         this.rebuildFriendsList();
         this.rebuildIgnoreList();
      }

   }

   @Subscribe
   public void onConfigChanged(ConfigChanged event) {
      if (event.getGroup().equals("friendNotes")) {
         switch (event.getKey()) {
            case "showIcons":
               if (this.client.getGameState() == GameState.LOGGED_IN) {
                  this.rebuildFriendsList();
                  this.rebuildIgnoreList();
               }
            default:
         }
      }
   }

   private void setFriendNote(String displayName, String note) {
      if (Strings.isNullOrEmpty(note)) {
         this.configManager.unsetConfiguration("friendNotes", "note_" + displayName);
      } else {
         this.configManager.setConfiguration("friendNotes", "note_" + displayName, note);
      }

      if (this.client.getGameState() == GameState.LOGGED_IN) {
         this.rebuildFriendsList();
         this.rebuildIgnoreList();
      }

   }

   @Nullable
   private String getFriendNote(String displayName) {
      return this.configManager.getConfiguration("friendNotes", "note_" + displayName);
   }

   private void migrateFriendNote(String currentDisplayName, String prevDisplayName) {
      String currentNote = this.getFriendNote(currentDisplayName);
      if (currentNote == null) {
         String prevNote = this.getFriendNote(prevDisplayName);
         if (prevNote != null) {
            log.debug("Update friend's username: '{}' -> '{}'", prevDisplayName, currentDisplayName);
            this.setFriendNote(prevDisplayName, (String)null);
            this.setFriendNote(currentDisplayName, prevNote);
         }
      }

   }

   private void setHoveredFriend(String displayName) {
      this.hoveredFriend = null;
      if (!Strings.isNullOrEmpty(displayName)) {
         String note = this.getFriendNote(displayName);
         if (note != null) {
            this.hoveredFriend = new HoveredFriend(displayName, note);
         }
      }

   }

   @Subscribe
   public void onMenuEntryAdded(MenuEntryAdded event) {
      int groupId = WidgetUtil.componentToInterface(event.getActionParam1());
      if (groupId == 429 && event.getOption().equals("Message") || groupId == 432 && event.getOption().equals("Delete")) {
         this.setHoveredFriend(Text.toJagexName(Text.removeTags(event.getTarget())));
         this.client.createMenuEntry(-1).setOption(this.hoveredFriend != null && this.hoveredFriend.getNote() != null ? "Edit Note" : "Add Note").setType(MenuAction.RUNELITE).setTarget(event.getTarget()).onClick((e) -> {
            String sanitizedTarget = Text.toJagexName(Text.removeTags(e.getTarget()));
            String note = this.getFriendNote(sanitizedTarget);
            this.chatboxPanelManager.openTextInput(String.format(NOTE_PROMPT_FORMAT, sanitizedTarget, 128)).value(Strings.nullToEmpty(note)).onDone((content) -> {
               if (content != null) {
                  content = Text.removeTags(content).trim();
                  log.debug("Set note for '{}': '{}'", sanitizedTarget, content);
                  this.setFriendNote(sanitizedTarget, content);
               }
            }).build();
         });
      } else if (this.hoveredFriend != null) {
         this.hoveredFriend = null;
      }

   }

   @Subscribe
   public void onNameableNameChanged(NameableNameChanged event) {
      Nameable nameable = event.getNameable();
      if (nameable instanceof Friend || nameable instanceof Ignore) {
         String name = nameable.getName();
         String prevName = nameable.getPrevName();
         if (prevName != null) {
            this.migrateFriendNote(Text.toJagexName(name), Text.toJagexName(prevName));
         }
      }

   }

   @Subscribe
   public void onRemovedFriend(RemovedFriend event) {
      String displayName = Text.toJagexName(event.getNameable().getName());
      log.debug("Remove friend: '{}'", displayName);
      this.setFriendNote(displayName, (String)null);
   }

   @Subscribe
   public void onScriptCallbackEvent(ScriptCallbackEvent event) {
      if (this.config.showIcons() && this.iconId != -1) {
         switch (event.getEventName()) {
            case "friendsChatSetText":
               Object[] objectStack = this.client.getObjectStack();
               int objectStackSize = this.client.getObjectStackSize();
               String rsn = (String)objectStack[objectStackSize - 1];
               String sanitized = Text.toJagexName(Text.removeTags(rsn));
               this.currentlyLayouting = sanitized;
               if (this.getFriendNote(sanitized) != null) {
                  objectStack[objectStackSize - 1] = rsn + " <img=" + this.chatIconManager.chatIconIndex(this.iconId) + ">";
               }
               break;
            case "friendsChatSetPosition":
               if (this.currentlyLayouting == null || this.getFriendNote(this.currentlyLayouting) == null) {
                  return;
               }

               int[] intStack = this.client.getIntStack();
               int intStackSize = this.client.getIntStackSize();
               int xpos = intStack[intStackSize - 4];
               xpos += 15;
               intStack[intStackSize - 4] = xpos;
         }

      }
   }

   private void rebuildFriendsList() {
      this.clientThread.invokeLater(() -> {
         log.debug("Rebuilding friends list");
         this.client.runScript(new Object[]{631, 28114949, 28114951, 28114952, 28114953, 28114954, 28114955, 28114956, 28114957, 28114962});
      });
   }

   private void rebuildIgnoreList() {
      this.clientThread.invokeLater(() -> {
         log.debug("Rebuilding ignore list");
         this.client.runScript(new Object[]{630, 28311557, 28311559, 28311560, 28311561, 28311562, 28311563, 28311568});
      });
   }

   private void loadIcon() {
      if (this.iconId == -1) {
         BufferedImage iconImg = ImageUtil.loadImageResource(this.getClass(), "note_icon.png");
         if (iconImg == null) {
            throw new RuntimeException("unable to load icon");
         } else {
            BufferedImage resized = ImageUtil.resizeImage(iconImg, 14, 12);
            this.iconId = this.chatIconManager.registerChatIcon(resized);
         }
      }
   }

   public HoveredFriend getHoveredFriend() {
      return this.hoveredFriend;
   }

   static {
      Color var10001 = new Color(0, 0, 170);
      NOTE_PROMPT_FORMAT = "%s's Notes<br>" + ColorUtil.prependColorTag("(Limit %s Characters)", var10001);
   }
}
