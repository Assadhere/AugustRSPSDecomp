package net.runelite.client.plugins.raids;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Joiner;
import com.google.inject.Binder;
import com.google.inject.Provides;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.text.DecimalFormat;
import java.time.Instant;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ScheduledExecutorService;
import java.util.stream.Collectors;
import javax.inject.Inject;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.InstanceTemplates;
import net.runelite.api.MessageNode;
import net.runelite.api.Point;
import net.runelite.api.Tile;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.ChatMessage;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.VarbitChanged;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.chat.ChatClient;
import net.runelite.client.chat.ChatColorType;
import net.runelite.client.chat.ChatCommandManager;
import net.runelite.client.chat.ChatMessageBuilder;
import net.runelite.client.chat.ChatMessageManager;
import net.runelite.client.chat.QueuedMessage;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.config.RuneLiteConfig;
import net.runelite.client.eventbus.EventBus;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ChatInput;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.game.SpriteManager;
import net.runelite.client.input.KeyManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.raids.events.RaidReset;
import net.runelite.client.plugins.raids.events.RaidScouted;
import net.runelite.client.plugins.raids.solver.Layout;
import net.runelite.client.plugins.raids.solver.LayoutSolver;
import net.runelite.client.ui.overlay.OverlayManager;
import net.runelite.client.ui.overlay.infobox.InfoBox;
import net.runelite.client.ui.overlay.infobox.InfoBoxManager;
import net.runelite.client.util.HotkeyListener;
import net.runelite.client.util.ImageCapture;
import net.runelite.client.util.Text;
import net.runelite.http.api.chat.LayoutRoom;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@PluginDescriptor(
   name = "Chambers Of Xeric",
   description = "Show helpful information for the Chambers of Xeric raid",
   tags = {"combat", "raid", "overlay", "pve", "pvm", "bosses", "cox"}
)
public class RaidsPlugin extends Plugin {
   private static final Logger log = LoggerFactory.getLogger(RaidsPlugin.class);
   private static final int LOBBY_PLANE = 3;
   private static final int SECOND_FLOOR_PLANE = 2;
   private static final int ROOMS_PER_PLANE = 8;
   private static final int AMOUNT_OF_ROOMS_PER_X_AXIS_PER_PLANE = 4;
   private static final String RAID_START_MESSAGE = "The raid has begun!";
   private static final String LEVEL_COMPLETE_MESSAGE = "level complete!";
   private static final String RAID_COMPLETE_MESSAGE = "Congratulations - your raid is complete!";
   private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("###.##");
   private static final DecimalFormat POINTS_FORMAT = new DecimalFormat("#,###");
   private static final String LAYOUT_COMMAND = "!layout";
   private static final int MAX_LAYOUT_LEN = 300;
   @Inject
   private RuneLiteConfig runeLiteConfig;
   @Inject
   private ChatMessageManager chatMessageManager;
   @Inject
   private InfoBoxManager infoBoxManager;
   @Inject
   private Client client;
   @Inject
   private RaidsConfig config;
   @Inject
   private OverlayManager overlayManager;
   @Inject
   private RaidsOverlay overlay;
   @Inject
   private LayoutSolver layoutSolver;
   @Inject
   private SpriteManager spriteManager;
   @Inject
   private ClientThread clientThread;
   @Inject
   private ChatCommandManager chatCommandManager;
   @Inject
   private ChatClient chatClient;
   @Inject
   private ScheduledExecutorService scheduledExecutorService;
   @Inject
   private KeyManager keyManager;
   @Inject
   private ImageCapture imageCapture;
   @Inject
   private EventBus eventBus;
   private final Set<String> roomWhitelist = new HashSet();
   private final Set<String> roomBlacklist = new HashSet();
   private final Set<String> rotationWhitelist = new HashSet();
   private final Set<String> layoutWhitelist = new HashSet();
   private Raid raid;
   private boolean inRaidChambers;
   private int raidPartyID;
   private RaidsTimer timer;
   private final HotkeyListener screenshotHotkeyListener = new HotkeyListener(() -> {
      return this.config.screenshotHotkey();
   }) {
      public void hotkeyPressed() {
         RaidsPlugin.this.clientThread.invoke(RaidsPlugin.this::screenshotScoutOverlay);
      }
   };

   @Provides
   RaidsConfig provideConfig(ConfigManager configManager) {
      return (RaidsConfig)configManager.getConfig(RaidsConfig.class);
   }

   public void configure(Binder binder) {
      binder.bind(RaidsOverlay.class);
   }

   protected void startUp() throws Exception {
      this.overlayManager.add(this.overlay);
      this.updateLists();
      this.clientThread.invokeLater(this::scoutRaid);
      this.chatCommandManager.registerCommandAsync("!layout", this::lookupRaid, this::submitRaid);
      this.keyManager.registerKeyListener(this.screenshotHotkeyListener);
   }

   protected void shutDown() throws Exception {
      this.chatCommandManager.unregisterCommand("!layout");
      this.overlayManager.remove(this.overlay);
      this.infoBoxManager.removeInfoBox(this.timer);
      this.timer = null;
      this.inRaidChambers = false;
      this.reset();
      this.keyManager.unregisterKeyListener(this.screenshotHotkeyListener);
   }

   @Subscribe
   public void onConfigChanged(ConfigChanged event) {
      if (event.getGroup().equals("raids")) {
         if (event.getKey().equals("raidsTimer")) {
            this.updateInfoBoxState();
         } else {
            this.updateLists();
         }
      }
   }

   @Subscribe
   public void onVarbitChanged(VarbitChanged event) {
      boolean inRaid;
      if (event.getVarpId() == 1427) {
         inRaid = this.inRaidChambers;
         int prevRaidID = this.raidPartyID;
         this.raidPartyID = event.getValue();
         if (this.client.getGameState() == GameState.LOGGED_IN && (!inRaid || prevRaidID != -1 && this.raidPartyID != -1 && prevRaidID != this.raidPartyID)) {
            log.debug("Raid party has been dissolved");
            this.reset();
         }
      }

      if (event.getVarbitId() == 5432) {
         inRaid = event.getValue() == 1;
         this.inRaidChambers = inRaid;
         if (this.client.getGameState() == GameState.LOGGED_IN) {
            if (inRaid) {
               this.scoutRaid();
            } else if (this.raidPartyID == -1) {
               log.debug("Raid has ended");
               this.reset();
            }
         }
      }

   }

   @Subscribe
   public void onChatMessage(ChatMessage event) {
      if (this.inRaidChambers && event.getType() == ChatMessageType.FRIENDSCHATNOTIFICATION) {
         String message = Text.removeTags(event.getMessage());
         if (this.config.raidsTimer() && message.startsWith("The raid has begun!")) {
            this.timer = new RaidsTimer(this, Instant.now(), this.config);
            this.spriteManager.getSpriteAsync(1582, 0, (InfoBox)this.timer);
            this.infoBoxManager.addInfoBox(this.timer);
         }

         if (this.timer != null && message.contains("level complete!")) {
            this.timer.timeFloor();
         }

         if (message.startsWith("Congratulations - your raid is complete!")) {
            if (this.timer != null) {
               this.timer.timeOlm();
               this.timer.setStopped(true);
            }

            if (this.config.pointsMessage()) {
               int totalPoints = this.client.getVarbitValue(5431);
               int personalPoints = this.client.getVarpValue(4609);
               double percentage = (double)personalPoints / ((double)totalPoints / 100.0);
               String chatMessage = (new ChatMessageBuilder()).append(ChatColorType.NORMAL).append("Total points: ").append(ChatColorType.HIGHLIGHT).append(POINTS_FORMAT.format((long)totalPoints)).append(ChatColorType.NORMAL).append(", Personal points: ").append(ChatColorType.HIGHLIGHT).append(POINTS_FORMAT.format((long)personalPoints)).append(ChatColorType.NORMAL).append(" (").append(ChatColorType.HIGHLIGHT).append(DECIMAL_FORMAT.format(percentage)).append(ChatColorType.NORMAL).append("%)").build();
               this.chatMessageManager.queue(QueuedMessage.builder().type(ChatMessageType.FRIENDSCHATNOTIFICATION).runeLiteFormattedMessage(chatMessage).build());
            }
         }
      }

   }

   @Subscribe
   public void onGameStateChanged(GameStateChanged event) {
      if (this.client.getGameState() == GameState.LOGGED_IN) {
         this.scoutRaid();
      }

   }

   private void scoutRaid() {
      if (this.client.getGameState() == GameState.LOGGED_IN) {
         this.inRaidChambers = this.client.getVarbitValue(5432) == 1;
         if (this.inRaidChambers) {
            boolean firstSolve = this.raid == null;
            this.raid = this.buildRaid(this.raid);
            if (this.raid == null) {
               log.debug("Failed to build raid");
            } else {
               if (this.raid.getLayout() == null) {
                  Layout layout = this.layoutSolver.findLayout(this.raid.toCode());
                  if (layout == null) {
                     log.debug("Could not find layout match");
                     this.raid = null;
                     return;
                  }

                  this.raid.updateLayout(layout);
               }

               RaidRoom[] rooms = this.raid.getCombatRooms();
               RotationSolver.solve(rooms);
               this.raid.setCombatRooms(rooms);
               if (this.config.layoutMessage() && firstSolve) {
                  this.sendRaidLayoutMessage();
               }

               this.eventBus.post(new RaidScouted(this.raid, firstSolve));
            }
         }
      }
   }

   private void sendRaidLayoutMessage() {
      String layout = this.getRaid().getLayout().toCodeString();
      String rooms = this.getRaid().toRoomString();
      String raidData = "[" + layout + "]: " + rooms;
      String layoutMessage = (new ChatMessageBuilder()).append(ChatColorType.HIGHLIGHT).append("Layout: ").append(ChatColorType.NORMAL).append(raidData).build();
      this.chatMessageManager.queue(QueuedMessage.builder().type(ChatMessageType.FRIENDSCHATNOTIFICATION).runeLiteFormattedMessage(layoutMessage).build());
   }

   private void updateInfoBoxState() {
      if (this.timer != null && !this.inRaidChambers) {
         this.infoBoxManager.removeInfoBox(this.timer);
         this.timer = null;
      }

   }

   @VisibleForTesting
   void updateLists() {
      this.updateList(this.roomWhitelist, this.config.whitelistedRooms());
      this.updateList(this.roomBlacklist, this.config.blacklistedRooms());
      this.updateList(this.layoutWhitelist, this.config.whitelistedLayouts());
      this.rotationWhitelist.clear();
      String[] var1 = this.config.whitelistedRotations().split("\\n");
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         String line = var1[var3];
         this.rotationWhitelist.add(line.toLowerCase().replace(" ", ""));
      }

   }

   private void updateList(Collection<String> list, String input) {
      list.clear();
      Iterator var3 = Text.fromCSV(input.toLowerCase()).iterator();

      while(var3.hasNext()) {
         String s = (String)var3.next();
         if (s.equals("unknown")) {
            list.add("unknown (combat)");
            list.add("unknown (puzzle)");
         } else {
            list.add(s);
         }
      }

   }

   boolean getRotationMatches() {
      RaidRoom[] combatRooms = this.raid.getCombatRooms();
      String rotation = (String)Arrays.stream(combatRooms).map(RaidRoom::getName).map(String::toLowerCase).collect(Collectors.joining(","));
      return this.rotationWhitelist.contains(rotation);
   }

   private Point findLobbyBase() {
      Tile[][] tiles = this.client.getScene().getTiles()[3];

      for(int x = 0; x < 104; ++x) {
         for(int y = 0; y < 104; ++y) {
            if (tiles[x][y] != null && tiles[x][y].getWallObject() != null && tiles[x][y].getWallObject().getId() == 12231) {
               return tiles[x][y].getSceneLocation();
            }
         }
      }

      return null;
   }

   private Raid buildRaid(Raid from) {
      Raid raid = from;
      if (raid == null) {
         Point gridBase = this.findLobbyBase();
         if (gridBase == null) {
            return null;
         }

         Integer lobbyIndex = this.findLobbyIndex(gridBase);
         if (lobbyIndex == null) {
            return null;
         }

         raid = new Raid(new WorldPoint(this.client.getBaseX() + gridBase.getX(), this.client.getBaseY() + gridBase.getY(), 3), lobbyIndex);
      }

      int baseX = raid.getLobbyIndex() % 4;
      int baseY = raid.getLobbyIndex() % 8 > 3 ? 1 : 0;

      for(int i = 0; i < raid.getRooms().length; ++i) {
         int x = i % 4;
         int y = i % 8 > 3 ? 1 : 0;
         int plane = i > 7 ? 2 : 3;
         x -= baseX;
         y -= baseY;
         x = raid.getGridBase().getX() + x * 32;
         y = raid.getGridBase().getY() - y * 32;
         x -= this.client.getBaseX();
         y -= this.client.getBaseY();
         if (x >= -31 && x < 104) {
            if (x < 1) {
               x = 1;
            }

            if (y < 1) {
               y = 1;
            }

            Tile tile = this.client.getScene().getTiles()[plane][x][y];
            if (tile != null) {
               RaidRoom room = this.determineRoom(tile);
               raid.setRoom(room, i);
            }
         }
      }

      return raid;
   }

   private RaidRoom determineRoom(Tile base) {
      int chunkData = this.client.getInstanceTemplateChunks()[base.getPlane()][base.getSceneLocation().getX() / 8][base.getSceneLocation().getY() / 8];
      InstanceTemplates template = InstanceTemplates.findMatch(chunkData);
      if (template == null) {
         return RaidRoom.EMPTY;
      } else {
         switch (template) {
            case RAIDS_LOBBY:
            case RAIDS_START:
               return RaidRoom.START;
            case RAIDS_END:
               return RaidRoom.END;
            case RAIDS_SCAVENGERS:
            case RAIDS_SCAVENGERS2:
               return RaidRoom.SCAVENGERS;
            case RAIDS_SHAMANS:
               return RaidRoom.SHAMANS;
            case RAIDS_VASA:
               return RaidRoom.VASA;
            case RAIDS_VANGUARDS:
               return RaidRoom.VANGUARDS;
            case RAIDS_ICE_DEMON:
               return RaidRoom.ICE_DEMON;
            case RAIDS_THIEVING:
               return RaidRoom.THIEVING;
            case RAIDS_FARMING:
            case RAIDS_FARMING2:
               return RaidRoom.FARMING;
            case RAIDS_MUTTADILES:
               return RaidRoom.MUTTADILES;
            case RAIDS_MYSTICS:
               return RaidRoom.MYSTICS;
            case RAIDS_TEKTON:
               return RaidRoom.TEKTON;
            case RAIDS_TIGHTROPE:
               return RaidRoom.TIGHTROPE;
            case RAIDS_GUARDIANS:
               return RaidRoom.GUARDIANS;
            case RAIDS_CRABS:
               return RaidRoom.CRABS;
            case RAIDS_VESPULA:
               return RaidRoom.VESPULA;
            default:
               return RaidRoom.EMPTY;
         }
      }
   }

   private void lookupRaid(ChatMessage chatMessage, String s) {
      ChatMessageType type = chatMessage.getType();
      String player;
      if (type.equals(ChatMessageType.PRIVATECHATOUT)) {
         player = this.client.getLocalPlayer().getName();
      } else {
         player = Text.sanitize(chatMessage.getName());
      }

      LayoutRoom[] layout;
      try {
         layout = this.chatClient.getLayout(player);
      } catch (IOException var9) {
         IOException ex = var9;
         log.debug("unable to lookup layout", ex);
         return;
      }

      if (layout != null && layout.length != 0) {
         String layoutMessage = Joiner.on(", ").join(Arrays.stream(layout).map((l) -> {
            return RaidRoom.valueOf(l.name());
         }).filter((room) -> {
            return room.getType() == RoomType.COMBAT || room.getType() == RoomType.PUZZLE;
         }).map(RaidRoom::getName).toArray());
         if (layoutMessage.length() > 300) {
            log.debug("layout message too long! {}", layoutMessage.length());
         } else {
            String response = (new ChatMessageBuilder()).append(ChatColorType.HIGHLIGHT).append("Layout: ").append(ChatColorType.NORMAL).append(layoutMessage).build();
            log.debug("Setting response {}", response);
            MessageNode messageNode = chatMessage.getMessageNode();
            messageNode.setRuneLiteFormatMessage(response);
            this.client.refreshChat();
         }
      }
   }

   private boolean submitRaid(ChatInput chatInput, String s) {
      if (this.raid == null) {
         return false;
      } else {
         String playerName = this.client.getLocalPlayer().getName();
         List<RaidRoom> orderedRooms = this.raid.getOrderedRooms();
         LayoutRoom[] layoutRooms = (LayoutRoom[])orderedRooms.stream().map((room) -> {
            return LayoutRoom.valueOf(room.name());
         }).toArray((x$0) -> {
            return new LayoutRoom[x$0];
         });
         this.scheduledExecutorService.execute(() -> {
            try {
               this.chatClient.submitLayout(playerName, layoutRooms);
            } catch (Exception var8) {
               Exception ex = var8;
               log.warn("unable to submit layout", ex);
            } finally {
               chatInput.resume();
            }

         });
         return true;
      }
   }

   void screenshotScoutOverlay() {
      if (this.overlay.isScoutOverlayShown()) {
         Rectangle overlayDimensions = this.overlay.getBounds();
         BufferedImage overlayImage = new BufferedImage(overlayDimensions.width, overlayDimensions.height, 2);
         Graphics2D graphic = overlayImage.createGraphics();
         graphic.setFont(this.runeLiteConfig.interfaceFont().getFont());
         graphic.setColor(Color.BLACK);
         graphic.fillRect(0, 0, overlayDimensions.width, overlayDimensions.height);
         this.overlay.render(graphic);
         this.imageCapture.saveScreenshot(overlayImage, "CoX_scout", (String)null, false, this.config.copyToClipboard());
         graphic.dispose();
      }
   }

   private Integer findLobbyIndex(Point gridBase) {
      if (104 > gridBase.getX() + 32 && 104 > gridBase.getY() + 32) {
         Tile[][] tiles = this.client.getScene().getTiles()[3];
         byte y;
         if (tiles[gridBase.getX()][gridBase.getY() + 32] == null) {
            y = 0;
         } else {
            y = 1;
         }

         int x;
         if (tiles[gridBase.getX() + 32][gridBase.getY()] == null) {
            x = 3;
         } else {
            for(x = 0; x < 3; ++x) {
               int sceneX = gridBase.getX() - 1 - 32 * x;
               if (sceneX < 0 || tiles[sceneX][gridBase.getY()] == null) {
                  break;
               }
            }
         }

         return x + y * 4;
      } else {
         return null;
      }
   }

   private void reset() {
      this.raid = null;
      this.updateInfoBoxState();
      this.eventBus.post(new RaidReset());
   }

   public Set<String> getRoomWhitelist() {
      return this.roomWhitelist;
   }

   public Set<String> getRoomBlacklist() {
      return this.roomBlacklist;
   }

   public Set<String> getRotationWhitelist() {
      return this.rotationWhitelist;
   }

   public Set<String> getLayoutWhitelist() {
      return this.layoutWhitelist;
   }

   void setRaid(Raid raid) {
      this.raid = raid;
   }

   public Raid getRaid() {
      return this.raid;
   }

   public boolean isInRaidChambers() {
      return this.inRaidChambers;
   }

   public int getRaidPartyID() {
      return this.raidPartyID;
   }
}
