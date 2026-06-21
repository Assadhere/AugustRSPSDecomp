package net.runelite.client.plugins.devtools;

import ch.qos.logback.classic.Level;
import com.formdev.flatlaf.extras.FlatInspector;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.primitives.Ints;
import com.google.inject.Provides;
import java.awt.KeyboardFocusManager;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.AWTEventListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import javax.swing.JOptionPane;
import javax.swing.JRootPane;
import javax.swing.RootPaneContainer;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.Experience;
import net.runelite.api.IndexedSprite;
import net.runelite.api.Menu;
import net.runelite.api.MenuAction;
import net.runelite.api.MenuEntry;
import net.runelite.api.NPC;
import net.runelite.api.Player;
import net.runelite.api.Skill;
import net.runelite.api.VarbitComposition;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.ClientTick;
import net.runelite.api.events.CommandExecuted;
import net.runelite.api.events.MenuEntryAdded;
import net.runelite.api.events.ScriptCallbackEvent;
import net.runelite.api.events.StatChanged;
import net.runelite.api.events.VarbitChanged;
import net.runelite.api.kit.KitType;
import net.runelite.client.chat.ChatMessageBuilder;
import net.runelite.client.chat.ChatMessageManager;
import net.runelite.client.chat.QueuedMessage;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.EventBus;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.ClientToolbar;
import net.runelite.client.ui.JagexColors;
import net.runelite.client.ui.NavigationButton;
import net.runelite.client.ui.overlay.OverlayManager;
import net.runelite.client.util.ColorUtil;
import net.runelite.client.util.HotkeyListener;
import net.runelite.client.util.ImageUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@PluginDescriptor(
   name = "Developer Tools",
   tags = {"panel"},
   developerPlugin = true
)
public class DevToolsPlugin extends Plugin {
   private static final Logger log = LoggerFactory.getLogger(DevToolsPlugin.class);
   private static final List<MenuAction> EXAMINE_MENU_ACTIONS;
   @Inject
   private Client client;
   @Inject
   private ClientToolbar clientToolbar;
   @Inject
   private OverlayManager overlayManager;
   @Inject
   private DevToolsOverlay overlay;
   @Inject
   private LocationOverlay locationOverlay;
   @Inject
   private SceneOverlay sceneOverlay;
   @Inject
   private CameraOverlay cameraOverlay;
   @Inject
   private WorldMapLocationOverlay worldMapLocationOverlay;
   @Inject
   private WorldMapRegionOverlay mapRegionOverlay;
   @Inject
   private SoundEffectOverlay soundEffectOverlay;
   @Inject
   private EventBus eventBus;
   @Inject
   private ConfigManager configManager;
   @Inject
   private ChatMessageManager chatMessageManager;
   @Inject
   private DevToolsConfig config;
   private DevToolsButton players;
   private DevToolsButton npcs;
   private DevToolsButton groundItems;
   private DevToolsButton groundObjects;
   private DevToolsButton gameObjects;
   private DevToolsButton graphicsObjects;
   private DevToolsButton walls;
   private DevToolsButton decorations;
   private DevToolsButton projectiles;
   private DevToolsButton location;
   private DevToolsButton zoneBorders;
   private DevToolsButton mapSquares;
   private DevToolsButton loadingLines;
   private DevToolsButton validMovement;
   private DevToolsButton movementFlags;
   private DevToolsButton lineOfSight;
   private DevToolsButton cameraPosition;
   private DevToolsButton worldMapLocation;
   private DevToolsButton tileLocation;
   private DevToolsButton interacting;
   private DevToolsButton examine;
   private DevToolsButton detachedCamera;
   private DevToolsButton widgetInspector;
   private DevToolsButton varInspector;
   private DevToolsButton soundEffects;
   private DevToolsButton scriptInspector;
   private DevToolsButton inventoryInspector;
   private DevToolsButton tileFlags;
   private DevToolsButton shell;
   private DevToolsButton menus;
   private DevToolsButton uiDefaultsInspector;
   private DevToolsButton worldEntities;
   private NavigationButton navButton;
   private final HotkeyListener swingInspectorHotkeyListener = new HotkeyListener(() -> {
      return this.config.swingInspectorHotkey();
   }) {
      Object inspector;

      public void hotkeyPressed() {
         Window window = KeyboardFocusManager.getCurrentKeyboardFocusManager().getActiveWindow();

         try {
            if (this.inspector == null) {
               JRootPane rootPane = ((RootPaneContainer)window).getRootPane();
               FlatInspector fi = new FlatInspector(rootPane);
               fi.setEnabled(true);
               this.inspector = fi;
               fi.addPropertyChangeListener((ev) -> {
                  if ("enabled".equals(ev.getPropertyName()) && !fi.isEnabled() && this.inspector == ev.getSource()) {
                     this.inspector = null;
                  }

               });
            } else {
               ((FlatInspector)this.inspector).setEnabled(false);
            }
         } catch (Exception | LinkageError var4) {
            Throwable e = var4;
            DevToolsPlugin.log.warn("unable to open swing inspector", e);
            JOptionPane.showMessageDialog(window, "The swing inspector is not available.");
         }

      }
   };
   private final AWTEventListener swingInspectorKeyListener = (rawEv) -> {
      if (rawEv instanceof KeyEvent) {
         KeyEvent kev = (KeyEvent)rawEv;
         if (kev.getID() == 401) {
            this.swingInspectorHotkeyListener.keyPressed(kev);
         } else if (kev.getID() == 402) {
            this.swingInspectorHotkeyListener.keyReleased(kev);
         }
      }

   };

   @Provides
   DevToolsConfig provideConfig(ConfigManager configManager) {
      return (DevToolsConfig)configManager.getConfig(DevToolsConfig.class);
   }

   protected void startUp() throws Exception {
      this.players = new DevToolsButton("Players");
      this.npcs = new DevToolsButton("NPCs");
      this.groundItems = new DevToolsButton("Ground Items");
      this.groundObjects = new DevToolsButton("Ground Objects");
      this.gameObjects = new DevToolsButton("Game Objects");
      this.graphicsObjects = new DevToolsButton("Graphics Objects");
      this.walls = new DevToolsButton("Walls");
      this.decorations = new DevToolsButton("Decorations");
      this.projectiles = new DevToolsButton("Projectiles");
      this.location = new DevToolsButton("Location");
      this.worldMapLocation = new DevToolsButton("World Map Location");
      this.tileLocation = new DevToolsButton("Tile Location");
      this.cameraPosition = new DevToolsButton("Camera Position");
      this.zoneBorders = new DevToolsButton("Zone Borders");
      this.mapSquares = new DevToolsButton("Map Squares");
      this.loadingLines = new DevToolsButton("Loading Lines");
      this.lineOfSight = new DevToolsButton("Line Of Sight");
      this.validMovement = new DevToolsButton("Valid Movement");
      this.movementFlags = new DevToolsButton("Movement Flags");
      this.interacting = new DevToolsButton("Interacting");
      this.examine = new DevToolsButton("Examine");
      this.detachedCamera = new DevToolsButton("Detached Camera");
      this.widgetInspector = new DevToolsButton("Widget Inspector");
      this.varInspector = new DevToolsButton("Var Inspector");
      this.soundEffects = new DevToolsButton("Sound Effects");
      this.scriptInspector = new DevToolsButton("Script Inspector");
      this.inventoryInspector = new DevToolsButton("Inventory Inspector");
      this.tileFlags = new DevToolsButton("Tile flags");
      this.shell = new DevToolsButton("Shell");
      this.menus = new DevToolsButton("Menus");
      this.uiDefaultsInspector = new DevToolsButton("Swing Defaults");
      this.worldEntities = new DevToolsButton("World Entities");
      this.overlayManager.add(this.overlay);
      this.overlayManager.add(this.locationOverlay);
      this.overlayManager.add(this.sceneOverlay);
      this.overlayManager.add(this.cameraOverlay);
      this.overlayManager.add(this.worldMapLocationOverlay);
      this.overlayManager.add(this.mapRegionOverlay);
      this.overlayManager.add(this.soundEffectOverlay);
      DevToolsPanel panel = (DevToolsPanel)this.injector.getInstance(DevToolsPanel.class);
      BufferedImage icon = ImageUtil.loadImageResource(this.getClass(), "devtools_icon.png");
      this.navButton = NavigationButton.builder().tooltip("Developer Tools").icon(icon).priority(1).panel(panel).build();
      this.clientToolbar.addNavigation(this.navButton);
      this.eventBus.register(this.soundEffectOverlay);
      Toolkit.getDefaultToolkit().addAWTEventListener(this.swingInspectorKeyListener, 8L);
   }

   protected void shutDown() throws Exception {
      this.eventBus.unregister((Object)this.soundEffectOverlay);
      this.overlayManager.remove(this.overlay);
      this.overlayManager.remove(this.locationOverlay);
      this.overlayManager.remove(this.sceneOverlay);
      this.overlayManager.remove(this.cameraOverlay);
      this.overlayManager.remove(this.worldMapLocationOverlay);
      this.overlayManager.remove(this.mapRegionOverlay);
      this.overlayManager.remove(this.soundEffectOverlay);
      this.clientToolbar.removeNavigation(this.navButton);
      Toolkit.getDefaultToolkit().removeAWTEventListener(this.swingInspectorKeyListener);
   }

   @Subscribe
   public void onCommandExecuted(CommandExecuted commandExecuted) {
      String[] args = commandExecuted.getArguments();
      int i;
      String current;
      String group;
      int id;
      Player player;
      String key;
      int id;
      Player player;
      String value;
      Skill skill;
      int i;
      VarbitChanged varbitChanged;
      switch (commandExecuted.getCommand().toLowerCase()) {
         case "logger":
            ch.qos.logback.classic.Logger logger = (ch.qos.logback.classic.Logger)LoggerFactory.getLogger("ROOT");
            Level currentLoggerLevel = logger.getLevel();
            if (args.length < 1) {
               key = "Logger level is currently set to " + String.valueOf(currentLoggerLevel);
            } else {
               Level newLoggerLevel = Level.toLevel(args[0], currentLoggerLevel);
               logger.setLevel(newLoggerLevel);
               key = "Logger level has been set to " + String.valueOf(newLoggerLevel);
            }

            this.client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", key, (String)null);
            break;
         case "getvarp":
            id = Integer.parseInt(args[0]);
            int[] varps = this.client.getVarps();
            i = varps[id];
            this.client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "VarPlayer " + id + ": " + i, (String)null);
            break;
         case "setvarp":
            id = Integer.parseInt(args[0]);
            id = Integer.parseInt(args[1]);
            int[] varps = this.client.getVarps();
            varps[id] = id;
            this.client.queueChangedVarp(id);
            this.client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "Set VarPlayer " + id + " to " + id, (String)null);
            varbitChanged = new VarbitChanged();
            varbitChanged.setVarpId(id);
            varbitChanged.setValue(id);
            this.eventBus.post(varbitChanged);
            break;
         case "getvarb":
            id = Integer.parseInt(args[0]);
            id = this.client.getVarbitValue(this.client.getVarps(), id);
            this.client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "Varbit " + id + ": " + id, (String)null);
            break;
         case "setvarb":
            id = Integer.parseInt(args[0]);
            id = Integer.parseInt(args[1]);
            this.client.setVarbitValue(this.client.getVarps(), id, id);
            VarbitComposition varbitComposition = this.client.getVarbit(id);
            this.client.queueChangedVarp(varbitComposition.getIndex());
            this.client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "Set varbit " + id + " to " + id, (String)null);
            varbitChanged = new VarbitChanged();
            varbitChanged.setVarbitId(id);
            varbitChanged.setValue(id);
            this.eventBus.post(varbitChanged);
            break;
         case "addxp":
            skill = Skill.valueOf(args[0].toUpperCase());
            id = Integer.parseInt(args[1]);
            i = this.client.getSkillExperience(skill) + id;
            i = Math.min(Experience.getLevelForXp(i), 99);
            this.client.getBoostedSkillLevels()[skill.ordinal()] = i;
            this.client.getRealSkillLevels()[skill.ordinal()] = i;
            this.client.getSkillExperiences()[skill.ordinal()] = i;
            this.client.queueChangedSkill(skill);
            StatChanged statChanged = new StatChanged(skill, i, i, i);
            this.eventBus.post(statChanged);
            break;
         case "setstat":
            skill = Skill.valueOf(args[0].toUpperCase());
            id = Integer.parseInt(args[1]);
            id = Ints.constrainToRange(id, 1, 99);
            i = Experience.getXpForLevel(id);
            this.client.getBoostedSkillLevels()[skill.ordinal()] = id;
            this.client.getRealSkillLevels()[skill.ordinal()] = id;
            this.client.getSkillExperiences()[skill.ordinal()] = i;
            this.client.queueChangedSkill(skill);
            StatChanged statChanged = new StatChanged(skill, i, id, id);
            this.eventBus.post(statChanged);
            break;
         case "anim":
            id = Integer.parseInt(args[0]);
            player = this.client.getLocalPlayer();
            player.setAnimation(id);
            player.setAnimationFrame(0);
            break;
         case "gfx":
            id = Integer.parseInt(args[0]);
            player = this.client.getLocalPlayer();
            player.setGraphic(id);
            player.setSpotAnimFrame(0);
            break;
         case "transform":
            id = Integer.parseInt(args[0]);
            player = this.client.getLocalPlayer();
            player.getPlayerComposition().setTransformedNpcId(id);
            player.setIdlePoseAnimation(-1);
            player.setPoseAnimation(-1);
            break;
         case "wear":
            id = Integer.parseInt(args[0]);
            id = Integer.parseInt(args[1]);
            Player player = this.client.getLocalPlayer();
            player.getPlayerComposition().getEquipmentIds()[id] = id + 2048;
            player.getPlayerComposition().setHash();
            break;
         case "tex":
            player = this.client.getLocalPlayer();
            player.getPlayerComposition().getEquipmentIds()[KitType.CAPE.getIndex()] = 8618;
            player.getPlayerComposition().getEquipmentIds()[KitType.SHIELD.getIndex()] = 6204;
            player.getPlayerComposition().setHash();
            break;
         case "alpha":
            player = this.client.getLocalPlayer();
            player.getPlayerComposition().getEquipmentIds()[KitType.HEAD.getIndex()] = 8157;
            player.getPlayerComposition().getEquipmentIds()[KitType.AMULET.getIndex()] = 22414;
            player.getPlayerComposition().getEquipmentIds()[KitType.CAPE.getIndex()] = 8159;
            player.getPlayerComposition().getEquipmentIds()[KitType.TORSO.getIndex()] = 8155;
            player.getPlayerComposition().getEquipmentIds()[KitType.SHIELD.getIndex()] = 14865;
            player.getPlayerComposition().getEquipmentIds()[KitType.ARMS.getIndex()] = -1;
            player.getPlayerComposition().getEquipmentIds()[KitType.LEGS.getIndex()] = 8156;
            player.getPlayerComposition().getEquipmentIds()[KitType.HAIR.getIndex()] = -1;
            player.getPlayerComposition().getEquipmentIds()[KitType.HANDS.getIndex()] = 8158;
            player.getPlayerComposition().getEquipmentIds()[KitType.BOOTS.getIndex()] = 8154;
            player.getPlayerComposition().setHash();
            break;
         case "sound":
            id = Integer.parseInt(args[0]);
            this.client.playSoundEffect(id);
            break;
         case "msg":
            this.client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", String.join(" ", args), "");
            break;
         case "setconf":
            group = args[0];
            key = args[1];
            value = "";

            for(i = 2; i < args.length; ++i) {
               if (args[i].equals("=")) {
                  value = String.join(" ", (CharSequence[])Arrays.copyOfRange(args, i + 1, args.length));
                  break;
               }

               key = key + " " + args[i];
            }

            current = this.configManager.getConfiguration(group, key);
            String message;
            if (value.isEmpty()) {
               this.configManager.unsetConfiguration(group, key);
               message = String.format("Unset configuration %s.%s (was: %s)", group, key, current);
            } else {
               this.configManager.setConfiguration(group, key, value);
               message = String.format("Set configuration %s.%s to %s (was: %s)", group, key, value, current);
            }

            this.chatMessageManager.queue(QueuedMessage.builder().type(ChatMessageType.GAMEMESSAGE).runeLiteFormattedMessage((new ChatMessageBuilder()).append(message).build()).build());
            break;
         case "getconf":
            group = args[0];
            key = String.join(" ", (CharSequence[])Arrays.copyOfRange(args, 1, args.length));
            value = this.configManager.getConfiguration(group, key);
            current = String.format("%s.%s = %s", group, key, value);
            this.chatMessageManager.queue(QueuedMessage.builder().type(ChatMessageType.GAMEMESSAGE).runeLiteFormattedMessage((new ChatMessageBuilder()).append(current).build()).build());
            break;
         case "modicons":
            ChatMessageBuilder builder = new ChatMessageBuilder();
            IndexedSprite[] modIcons = this.client.getModIcons();

            for(i = 0; i < modIcons.length; ++i) {
               builder.append("" + i + "=").img(i);
               if (i != modIcons.length - 1) {
                  builder.append(", ");
               }
            }

            this.chatMessageManager.queue(QueuedMessage.builder().type(ChatMessageType.GAMEMESSAGE).runeLiteFormattedMessage(builder.build()).build());
      }

   }

   @Subscribe
   public void onMenuEntryAdded(MenuEntryAdded event) {
      if (this.examine.isActive()) {
         MenuAction action = MenuAction.of(event.getType());
         if (EXAMINE_MENU_ACTIONS.contains(action)) {
            MenuEntry entry = event.getMenuEntry();
            int identifier = event.getIdentifier();
            String info = "ID: ";
            if (action == MenuAction.EXAMINE_NPC) {
               NPC npc = entry.getNpc();

               assert npc != null;

               info = info + npc.getId();
            } else {
               info = info + identifier;
               if (action == MenuAction.EXAMINE_OBJECT) {
                  WorldPoint point = WorldPoint.fromScene(this.client, entry.getParam0(), entry.getParam1(), this.client.getPlane());
                  info = info + " X: " + point.getX() + " Y: " + point.getY();
               }
            }

            String var10001 = entry.getTarget();
            entry.setTarget(var10001 + " " + ColorUtil.prependColorTag("(" + info + ")", JagexColors.MENU_TARGET));
         }

      }
   }

   @Subscribe
   public void onScriptCallbackEvent(ScriptCallbackEvent ev) {
      if ("devtoolsEnabled".equals(ev.getEventName())) {
         this.client.getIntStack()[this.client.getIntStackSize() - 1] = 1;
      }

   }

   @Subscribe
   public void onClientTick(ClientTick clientTick) {
      if (this.menus.isActive() && !this.client.isMenuOpen()) {
         for(int i = 0; i < 100; ++i) {
            int i_ = i;
            if (i % 30 == 0) {
               MenuEntry parent = this.client.createMenuEntry(1).setOption("pmenu" + i).setTarget(i % 60 == 0 ? "devtools devtools devtools devtools" : "devtools").setType(MenuAction.RUNELITE);
               Menu submenu = parent.createSubMenu();

               for(int j = 0; j < 4; ++j) {
                  int j_ = j;
                  submenu.createMenuEntry(0).setOption("submenu" + j).setTarget("devtools").setType(MenuAction.RUNELITE).onClick((c) -> {
                     this.client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "menu " + i_ + " sub " + j_, (String)null);
                  });
               }
            } else {
               this.client.createMenuEntry(1).setOption("menu" + i).setTarget("devtools").setType(MenuAction.RUNELITE).onClick((c) -> {
                  this.client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "menu " + i_, (String)null);
               });
            }
         }
      }

   }

   static Map<Integer, String> loadFieldNames(Class<?> clazz) {
      ImmutableMap.Builder<Integer, String> map = ImmutableMap.builder();

      try {
         Field[] var7 = clazz.getDeclaredFields();
         int var3 = var7.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            Field f = var7[var4];
            map.put(f.getInt((Object)null), f.getName());
         }
      } catch (ReflectiveOperationException var6) {
         ReflectiveOperationException e = var6;
         log.debug("Failed to load fields", e);
      }

      return map.build();
   }

   public Client getClient() {
      return this.client;
   }

   public ClientToolbar getClientToolbar() {
      return this.clientToolbar;
   }

   public OverlayManager getOverlayManager() {
      return this.overlayManager;
   }

   public DevToolsOverlay getOverlay() {
      return this.overlay;
   }

   public LocationOverlay getLocationOverlay() {
      return this.locationOverlay;
   }

   public SceneOverlay getSceneOverlay() {
      return this.sceneOverlay;
   }

   public CameraOverlay getCameraOverlay() {
      return this.cameraOverlay;
   }

   public WorldMapLocationOverlay getWorldMapLocationOverlay() {
      return this.worldMapLocationOverlay;
   }

   public WorldMapRegionOverlay getMapRegionOverlay() {
      return this.mapRegionOverlay;
   }

   public SoundEffectOverlay getSoundEffectOverlay() {
      return this.soundEffectOverlay;
   }

   public EventBus getEventBus() {
      return this.eventBus;
   }

   public ConfigManager getConfigManager() {
      return this.configManager;
   }

   public ChatMessageManager getChatMessageManager() {
      return this.chatMessageManager;
   }

   public DevToolsConfig getConfig() {
      return this.config;
   }

   public DevToolsButton getPlayers() {
      return this.players;
   }

   public DevToolsButton getNpcs() {
      return this.npcs;
   }

   public DevToolsButton getGroundItems() {
      return this.groundItems;
   }

   public DevToolsButton getGroundObjects() {
      return this.groundObjects;
   }

   public DevToolsButton getGameObjects() {
      return this.gameObjects;
   }

   public DevToolsButton getGraphicsObjects() {
      return this.graphicsObjects;
   }

   public DevToolsButton getWalls() {
      return this.walls;
   }

   public DevToolsButton getDecorations() {
      return this.decorations;
   }

   public DevToolsButton getProjectiles() {
      return this.projectiles;
   }

   public DevToolsButton getLocation() {
      return this.location;
   }

   public DevToolsButton getZoneBorders() {
      return this.zoneBorders;
   }

   public DevToolsButton getMapSquares() {
      return this.mapSquares;
   }

   public DevToolsButton getLoadingLines() {
      return this.loadingLines;
   }

   public DevToolsButton getValidMovement() {
      return this.validMovement;
   }

   public DevToolsButton getMovementFlags() {
      return this.movementFlags;
   }

   public DevToolsButton getLineOfSight() {
      return this.lineOfSight;
   }

   public DevToolsButton getCameraPosition() {
      return this.cameraPosition;
   }

   public DevToolsButton getWorldMapLocation() {
      return this.worldMapLocation;
   }

   public DevToolsButton getTileLocation() {
      return this.tileLocation;
   }

   public DevToolsButton getInteracting() {
      return this.interacting;
   }

   public DevToolsButton getExamine() {
      return this.examine;
   }

   public DevToolsButton getDetachedCamera() {
      return this.detachedCamera;
   }

   public DevToolsButton getWidgetInspector() {
      return this.widgetInspector;
   }

   public DevToolsButton getVarInspector() {
      return this.varInspector;
   }

   public DevToolsButton getSoundEffects() {
      return this.soundEffects;
   }

   public DevToolsButton getScriptInspector() {
      return this.scriptInspector;
   }

   public DevToolsButton getInventoryInspector() {
      return this.inventoryInspector;
   }

   public DevToolsButton getTileFlags() {
      return this.tileFlags;
   }

   public DevToolsButton getShell() {
      return this.shell;
   }

   public DevToolsButton getMenus() {
      return this.menus;
   }

   public DevToolsButton getUiDefaultsInspector() {
      return this.uiDefaultsInspector;
   }

   public DevToolsButton getWorldEntities() {
      return this.worldEntities;
   }

   public NavigationButton getNavButton() {
      return this.navButton;
   }

   public HotkeyListener getSwingInspectorHotkeyListener() {
      return this.swingInspectorHotkeyListener;
   }

   public AWTEventListener getSwingInspectorKeyListener() {
      return this.swingInspectorKeyListener;
   }

   static {
      EXAMINE_MENU_ACTIONS = ImmutableList.of(MenuAction.EXAMINE_ITEM, MenuAction.EXAMINE_ITEM_GROUND, MenuAction.EXAMINE_NPC, MenuAction.EXAMINE_OBJECT);
   }
}
