package net.runelite.client.plugins.particles;

import com.google.inject.Provides;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.IntBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.inject.Inject;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import net.runelite.api.Actor;
import net.runelite.api.ActorSpotAnim;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.DecorativeObject;
import net.runelite.api.DynamicObject;
import net.runelite.api.GameObject;
import net.runelite.api.GameState;
import net.runelite.api.GraphicsObject;
import net.runelite.api.GroundObject;
import net.runelite.api.MenuAction;
import net.runelite.api.Model;
import net.runelite.api.NPC;
import net.runelite.api.Perspective;
import net.runelite.api.Player;
import net.runelite.api.PlayerComposition;
import net.runelite.api.Point;
import net.runelite.api.Projectile;
import net.runelite.api.Renderable;
import net.runelite.api.Scene;
import net.runelite.api.Tile;
import net.runelite.api.TileObject;
import net.runelite.api.WallObject;
import net.runelite.api.WorldEntity;
import net.runelite.api.WorldView;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.ChatMessage;
import net.runelite.api.events.ClientTick;
import net.runelite.api.events.DecorativeObjectDespawned;
import net.runelite.api.events.DecorativeObjectSpawned;
import net.runelite.api.events.GameObjectDespawned;
import net.runelite.api.events.GameObjectSpawned;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.GraphicChanged;
import net.runelite.api.events.GraphicsObjectCreated;
import net.runelite.api.events.GroundObjectDespawned;
import net.runelite.api.events.GroundObjectSpawned;
import net.runelite.api.events.MenuOpened;
import net.runelite.api.events.NpcChanged;
import net.runelite.api.events.NpcDespawned;
import net.runelite.api.events.NpcSpawned;
import net.runelite.api.events.PlayerChanged;
import net.runelite.api.events.PlayerDespawned;
import net.runelite.api.events.PlayerSpawned;
import net.runelite.api.events.ProjectileMoved;
import net.runelite.api.events.WallObjectDespawned;
import net.runelite.api.events.WallObjectSpawned;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.callback.Hooks;
import net.runelite.client.callback.RenderCallback;
import net.runelite.client.callback.RenderCallbackManager;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.input.KeyListener;
import net.runelite.client.input.KeyManager;
import net.runelite.client.input.MouseListener;
import net.runelite.client.input.MouseManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.particles.binding.EntityParticleState;
import net.runelite.client.plugins.particles.binding.GameObjectModelScanResult;
import net.runelite.client.plugins.particles.binding.GameObjectParticleState;
import net.runelite.client.plugins.particles.config.ParticleEffectorConfig;
import net.runelite.client.plugins.particles.config.ParticleEmitterConfig;
import net.runelite.client.plugins.particles.config.ParticlePluginConfig;
import net.runelite.client.plugins.particles.core.Particle;
import net.runelite.client.plugins.particles.core.ParticleEffector;
import net.runelite.client.plugins.particles.core.ParticleEmitter;
import net.runelite.client.plugins.particles.core.ParticleSystem;
import net.runelite.client.plugins.particles.data.ParticleConfigLoader;
import net.runelite.client.plugins.particles.data.ParticleDBLoader;
import net.runelite.client.plugins.particles.data.ParticleWhitelist;
import net.runelite.client.plugins.particles.render.Particle3DRenderer;
import net.runelite.client.plugins.particles.render.ParticleDebugOverlay;
import net.runelite.client.plugins.particles.render.ParticleTextureManager;
import net.runelite.client.plugins.particles.render.ParticleTransparencyRenderer;
import net.runelite.client.plugins.particles.util.ScanScratchPad;
import net.runelite.client.ui.ClientToolbar;
import net.runelite.client.ui.NavigationButton;
import net.runelite.client.ui.PluginPanel;
import net.runelite.client.ui.components.colorpicker.ColorPickerManager;
import net.runelite.client.ui.overlay.OverlayManager;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@PluginDescriptor(
   name = "Particle Effects",
   description = "Adds custom particle effects to the game",
   tags = {"particles", "effects", "graphics"}
)
public class ParticlePlugin extends Plugin {
   private static final Logger log = LoggerFactory.getLogger(ParticlePlugin.class);
   @Inject
   private Client client;
   @Inject
   private ParticlePluginConfig config;
   @Inject
   private OverlayManager overlayManager;
   @Inject
   private ParticleDebugOverlay particleDebugOverlay;
   @Inject
   private ClientToolbar clientToolbar;
   @Inject
   private MouseManager mouseManager;
   @Inject
   private KeyManager keyManager;
   @Inject
   private ColorPickerManager colorPickerManager;
   @Inject
   private ConfigManager configManager;
   @Inject
   private RenderCallbackManager renderCallbackManager;
   @Inject
   private ClientThread clientThread;
   @Inject
   private Hooks hooks;
   private ParticleEditorBridge editorBridge;
   private NavigationButton navButton;
   private ParticleSystem particleSystem;
   private volatile boolean frozen;
   private Particle3DRenderer particle3DRenderer;
   private ParticleTransparencyRenderer transparencyRenderer;
   private ParticleTextureManager particleTextureManager;
   private volatile boolean pendingTextureReinit = false;
   private final Set<Projectile> processedProjectiles = new HashSet();
   private final Map<Long, GameObjectParticleState> objectStates = new HashMap();
   private final Map<Long, GameObjectModelScanResult> gameObjectModelScanCache = new HashMap();
   private final Set<TileObject> pendingObjects = new HashSet();
   private final Map<Player, EntityParticleState> playerStates = new HashMap();
   private final Map<NPC, EntityParticleState> npcStates = new HashMap();
   private final Map<Projectile, EntityParticleState> projectileStates = new HashMap();
   private final Map<GraphicsObject, EntityParticleState> graphicsObjectStates = new HashMap();
   private final ScanScratchPad scanScratch = new ScanScratchPad();
   private final IdentityHashMap<Renderable, Boolean> renderedEntitiesThisFrame = new IdentityHashMap(10000);
   private final RenderCallback renderCallback = new RenderCallback() {
      public void onDraw(Renderable renderable) {
         ParticlePlugin.this.renderedEntitiesThisFrame.put(renderable, Boolean.TRUE);
      }
   };
   private int lastLocalPlayerWorldViewId = -1;
   private ParticleWhitelist whitelist = new ParticleWhitelist();
   private boolean pendingCacheLoad = false;
   private boolean hasCacheSnapshot = false;
   private int lastDbTablesHash;
   private int lastTexturesHash;
   private final int[] reusableVertex3 = new int[3];
   private final int[] reusableVertices9 = new int[9];
   private final int[] reusableTempVertex = new int[3];
   private static final Pattern COLOUR_TAG_PATTERN = Pattern.compile("<p(\\d+)c=([0-9a-fA-F]+)\\|([0-9a-fA-F]+)\\|([0-9a-fA-F]+)>");
   private final MouseListener mouseListener = new MouseListener() {
      public MouseEvent mouseClicked(MouseEvent mouseEvent) {
         return mouseEvent;
      }

      public MouseEvent mousePressed(MouseEvent mouseEvent) {
         try {
            if (ParticlePlugin.this.editorBridge.isModelEditorActive()) {
               if (ParticlePlugin.this.editorBridge.handleModelEditorMousePressed(mouseEvent)) {
                  ParticlePlugin.this.client.getCanvas().requestFocusInWindow();
                  mouseEvent.consume();
                  return mouseEvent;
               }

               if (ParticlePlugin.this.editorBridge.handleModelEditorMouseClick(mouseEvent)) {
                  ParticlePlugin.this.client.getCanvas().requestFocusInWindow();
                  mouseEvent.consume();
                  return mouseEvent;
               }
            }

            if (ParticlePlugin.this.editorBridge.handlePlacementClick(mouseEvent)) {
               mouseEvent.consume();
               return mouseEvent;
            }
         } catch (Throwable var3) {
            Throwable e = var3;
            ParticlePlugin.this.handleEditorBridgeError(e, "mousePressed");
         }

         return mouseEvent;
      }

      public MouseEvent mouseReleased(MouseEvent mouseEvent) {
         try {
            if (ParticlePlugin.this.editorBridge.isModelEditorActive() && ParticlePlugin.this.editorBridge.handleModelEditorMouseReleased(mouseEvent)) {
               ParticlePlugin.this.client.getCanvas().requestFocusInWindow();
               mouseEvent.consume();
               return mouseEvent;
            }

            if (ParticlePlugin.this.editorBridge.isPlacementMode() && mouseEvent.getButton() != 2) {
               mouseEvent.consume();
            }
         } catch (Throwable var3) {
            Throwable e = var3;
            ParticlePlugin.this.handleEditorBridgeError(e, "mouseReleased");
         }

         return mouseEvent;
      }

      public MouseEvent mouseEntered(MouseEvent mouseEvent) {
         return mouseEvent;
      }

      public MouseEvent mouseExited(MouseEvent mouseEvent) {
         return mouseEvent;
      }

      public MouseEvent mouseDragged(MouseEvent mouseEvent) {
         try {
            if (ParticlePlugin.this.editorBridge.isModelEditorActive() && ParticlePlugin.this.editorBridge.handleModelEditorMouseDragged(mouseEvent)) {
               ParticlePlugin.this.client.getCanvas().requestFocusInWindow();
               mouseEvent.consume();
               return mouseEvent;
            }
         } catch (Throwable var3) {
            Throwable e = var3;
            ParticlePlugin.this.handleEditorBridgeError(e, "mouseDragged");
         }

         return mouseEvent;
      }

      public MouseEvent mouseMoved(MouseEvent mouseEvent) {
         try {
            if (ParticlePlugin.this.editorBridge.isModelEditorActive()) {
               ParticlePlugin.this.editorBridge.handleModelEditorMouseMoved(mouseEvent);
            }
         } catch (Throwable var3) {
            Throwable e = var3;
            ParticlePlugin.this.handleEditorBridgeError(e, "mouseMoved");
         }

         return mouseEvent;
      }
   };
   private final KeyListener keyListener = new KeyListener() {
      public void keyTyped(KeyEvent e) {
      }

      public void keyPressed(KeyEvent e) {
         try {
            if (ParticlePlugin.this.editorBridge.isModelEditorActive() && ParticlePlugin.this.editorBridge.handleModelEditorKeyPressed(e)) {
               e.consume();
               if (e.getKeyCode() == 9) {
                  ParticlePlugin.this.editorBridge.syncModelEditorModeButtons();
               }
            }
         } catch (Throwable var3) {
            Throwable ex = var3;
            ParticlePlugin.this.handleEditorBridgeError(ex, "keyPressed");
         }

      }

      public void keyReleased(KeyEvent e) {
      }
   };
   private URLClassLoader editorClassLoader;
   private static final String PARTICLE_EDITOR_PREFIX = "Particle editor: ";
   private boolean loading = false;
   private int lastBaseX;
   private int lastBaseY;

   public void setFrozen(boolean frozen) {
      this.frozen = frozen;
      if (this.particleSystem != null) {
         this.particleSystem.setEnabled(!frozen);
      }

   }

   public boolean isFrozen() {
      return this.frozen;
   }

   private void handleEditorBridgeError(Throwable e, String context) {
      log.warn("Editor bridge error in {} (version mismatch?), disabling editor", context, e);
      this.editorBridge = new NoOpEditorBridge();
   }

   protected void startUp() throws Exception {
      log.info("Particle Effects plugin started");
      this.particleSystem = new ParticleSystem();
      this.particleSystem.setMaxTotalParticles(this.config.maxParticles());
      this.particle3DRenderer = new Particle3DRenderer();
      this.transparencyRenderer = new ParticleTransparencyRenderer();
      this.particleTextureManager = new ParticleTextureManager();
      this.loadDefaultConfigs();
      this.overlayManager.add(this.particleDebugOverlay);
      this.editorBridge = this.createEditorBridge();
      this.editorBridge.initialize(new ParticleEditorBridge.EditorDependencies(this.client, this.clientThread, this.hooks, this.particleSystem, this.overlayManager, this));
      PluginPanel editorPanel = this.editorBridge.getEditorPanel();
      if (editorPanel != null) {
         BufferedImage icon = this.createSnowflakeIcon();
         this.navButton = NavigationButton.builder().tooltip("Particle Editor").icon(icon).priority(10).panel(editorPanel).build();
         this.clientToolbar.addNavigation(this.navButton);
      }

      this.mouseManager.registerMouseListener(this.mouseListener);
      this.keyManager.registerKeyListener(this.keyListener);
      this.renderCallbackManager.register(this.renderCallback);
      if (this.client.getGameState() == GameState.LOGGED_IN) {
         this.pendingTextureReinit = true;
         this.clientThread.invokeLater(() -> {
            this.particleSystem.syncClientCycle(this.client.getGameCycle());
            if (this.pendingCacheLoad) {
               this.pendingCacheLoad = false;
               this.loadConfigsFromCache();
            }

            this.performFullGameScan();
         });
      }

   }

   private void performFullGameScan() {
      if (this.particleSystem != null) {
         if (this.client.getGameState() == GameState.LOGGED_IN) {
            log.info("Performing full game scan (plugin re-enabled while logged in)");
            WorldView wv = this.client.getTopLevelWorldView();
            if (wv != null) {
               int npcCount;
               Iterator var3;
               if (this.config.playerParticles()) {
                  npcCount = 0;
                  var3 = wv.players().iterator();

                  while(var3.hasNext()) {
                     Player player = (Player)var3.next();
                     if (player != null && this.hasWhitelistedAppearance(player)) {
                        this.scanPlayerModelForParticles(player);
                        ++npcCount;
                     }
                  }

                  log.info("Scanned {} whitelisted players", npcCount);
               }

               if (this.config.npcParticles()) {
                  npcCount = 0;
                  var3 = wv.npcs().iterator();

                  while(var3.hasNext()) {
                     NPC npc = (NPC)var3.next();
                     if (npc != null && this.isNpcWhitelisted(npc.getId())) {
                        this.scanNpcModelForParticles(npc);
                        ++npcCount;
                     }
                  }

                  log.info("Scanned {} whitelisted NPCs", npcCount);
               }

               if (this.config.objectEmitters()) {
                  Scene scene = wv.getScene();
                  if (scene != null) {
                     Tile[][][] tiles = scene.getTiles();
                     int objectCount = 0;
                     int z = 0;

                     while(true) {
                        if (z >= tiles.length) {
                           log.info("Scanned {} whitelisted objects", objectCount);
                           break;
                        }

                        for(int x = 0; x < tiles[z].length; ++x) {
                           for(int y = 0; y < tiles[z][x].length; ++y) {
                              Tile tile = tiles[z][x][y];
                              if (tile != null) {
                                 GameObject[] var9 = tile.getGameObjects();
                                 int var10 = var9.length;

                                 for(int var11 = 0; var11 < var10; ++var11) {
                                    GameObject gameObject = var9[var11];
                                    if (gameObject != null && this.isLocWhitelisted(gameObject.getId())) {
                                       this.handleTileObjectSpawned(gameObject);
                                       ++objectCount;
                                    }
                                 }

                                 GroundObject groundObject = tile.getGroundObject();
                                 if (groundObject != null && this.isLocWhitelisted(groundObject.getId())) {
                                    this.handleTileObjectSpawned(groundObject);
                                    ++objectCount;
                                 }

                                 WallObject wallObject = tile.getWallObject();
                                 if (wallObject != null && this.isLocWhitelisted(wallObject.getId())) {
                                    this.handleTileObjectSpawned(wallObject);
                                    ++objectCount;
                                 }

                                 DecorativeObject decorativeObject = tile.getDecorativeObject();
                                 if (decorativeObject != null && this.isLocWhitelisted(decorativeObject.getId())) {
                                    this.handleTileObjectSpawned(decorativeObject);
                                    ++objectCount;
                                 }
                              }
                           }
                        }

                        ++z;
                     }
                  }

                  this.scanExtendedObjectEmitters();
               }

               log.info("Full game scan complete");
            }
         }
      }
   }

   private void scanExtendedObjectEmitters() {
      WorldView wv = this.client.getTopLevelWorldView();
      if (wv != null) {
         Scene scene = wv.getScene();
         if (scene != null) {
            Tile[][][] extendedTiles = scene.getExtendedTiles();
            if (extendedTiles != null) {
               int sceneOffset = 40;
               int extendedObjectCount = 0;

               for(int z = 0; z < extendedTiles.length; ++z) {
                  for(int x = 0; x < extendedTiles[z].length; ++x) {
                     for(int y = 0; y < extendedTiles[z][x].length; ++y) {
                        if (x < sceneOffset || x >= sceneOffset + 104 || y < sceneOffset || y >= sceneOffset + 104) {
                           Tile tile = extendedTiles[z][x][y];
                           if (tile != null) {
                              GameObject[] var10 = tile.getGameObjects();
                              int var11 = var10.length;

                              for(int var12 = 0; var12 < var11; ++var12) {
                                 GameObject gameObject = var10[var12];
                                 if (gameObject != null && this.isLocWhitelisted(gameObject.getId())) {
                                    this.handleTileObjectSpawned(gameObject);
                                    ++extendedObjectCount;
                                 }
                              }

                              GroundObject groundObject = tile.getGroundObject();
                              if (groundObject != null && this.isLocWhitelisted(groundObject.getId())) {
                                 this.handleTileObjectSpawned(groundObject);
                                 ++extendedObjectCount;
                              }

                              WallObject wallObject = tile.getWallObject();
                              if (wallObject != null && this.isLocWhitelisted(wallObject.getId())) {
                                 this.handleTileObjectSpawned(wallObject);
                                 ++extendedObjectCount;
                              }

                              DecorativeObject decorativeObject = tile.getDecorativeObject();
                              if (decorativeObject != null && this.isLocWhitelisted(decorativeObject.getId())) {
                                 this.handleTileObjectSpawned(decorativeObject);
                                 ++extendedObjectCount;
                              }
                           }
                        }
                     }
                  }
               }

            }
         }
      }
   }

   protected void shutDown() throws Exception {
      log.info("Particle Effects plugin stopped");
      if (this.navButton != null) {
         this.clientToolbar.removeNavigation(this.navButton);
         this.navButton = null;
      }

      if (this.editorBridge != null) {
         this.editorBridge.cleanup();
         this.editorBridge = null;
      }

      if (this.editorClassLoader != null) {
         try {
            this.editorClassLoader.close();
         } catch (Exception var2) {
            Exception e = var2;
            log.warn("Failed to close editor classloader", e);
         }

         this.editorClassLoader = null;
      }

      this.mouseManager.unregisterMouseListener(this.mouseListener);
      this.keyManager.unregisterKeyListener(this.keyListener);
      this.renderCallbackManager.unregister(this.renderCallback);
      this.overlayManager.remove(this.particleDebugOverlay);
      this.processedProjectiles.clear();
      this.objectStates.clear();
      this.playerStates.clear();
      this.npcStates.clear();
      this.projectileStates.clear();
      this.graphicsObjectStates.clear();
      this.pendingObjects.clear();
      this.renderedEntitiesThisFrame.clear();
      if (this.particleSystem != null) {
         this.particleSystem.shutdown();
         this.particleSystem.clear();
         this.particleSystem = null;
      }

      if (this.particleTextureManager != null) {
         this.particleTextureManager.reset();
         this.particleTextureManager = null;
      }

      this.particle3DRenderer = null;
   }

   @Provides
   ParticlePluginConfig provideConfig(ConfigManager configManager) {
      return (ParticlePluginConfig)configManager.getConfig(ParticlePluginConfig.class);
   }

   private ParticleEditorBridge createEditorBridge() {
      try {
         Class<?> implClass = Class.forName("net.runelite.client.plugins.particles.editor.EditorBridgeImpl");
         log.info("Particle editor loaded from classpath (development mode)");
         return (ParticleEditorBridge)implClass.getDeclaredConstructor().newInstance();
      } catch (ClassNotFoundException var2) {
         log.debug("Editor not in classpath");
      } catch (Exception var3) {
         Exception e = var3;
         log.warn("Failed to load editor from classpath", e);
      }

      log.info("Particle editor not available (send 'Particle editor: <url>' as ENGINE message to load)");
      return new NoOpEditorBridge();
   }

   private File downloadEditorJar(String urlString) throws Exception {
      log.info("Downloading particle editor from: {}", urlString);
      URL url = new URL(urlString);
      File tempFile = File.createTempFile("particle-editor-", ".jar");
      tempFile.deleteOnExit();
      InputStream inputStream = url.openStream();

      try {
         ReadableByteChannel readableByteChannel = Channels.newChannel(inputStream);

         try {
            FileOutputStream fileOutputStream = new FileOutputStream(tempFile);

            try {
               fileOutputStream.getChannel().transferFrom(readableByteChannel, 0L, Long.MAX_VALUE);
            } catch (Throwable var12) {
               try {
                  fileOutputStream.close();
               } catch (Throwable var11) {
                  var12.addSuppressed(var11);
               }

               throw var12;
            }

            fileOutputStream.close();
         } catch (Throwable var13) {
            if (readableByteChannel != null) {
               try {
                  readableByteChannel.close();
               } catch (Throwable var10) {
                  var13.addSuppressed(var10);
               }
            }

            throw var13;
         }

         if (readableByteChannel != null) {
            readableByteChannel.close();
         }
      } catch (Throwable var14) {
         if (inputStream != null) {
            try {
               inputStream.close();
            } catch (Throwable var9) {
               var14.addSuppressed(var9);
            }
         }

         throw var14;
      }

      if (inputStream != null) {
         inputStream.close();
      }

      log.info("Successfully downloaded particle editor JAR to temp file");
      return tempFile;
   }

   private ParticleEditorBridge loadEditorFromJar(File jarFile) throws Exception {
      this.editorClassLoader = new URLClassLoader(new URL[]{jarFile.toURI().toURL()}, this.getClass().getClassLoader());
      Class<?> implClass = this.editorClassLoader.loadClass("net.runelite.client.plugins.particles.editor.EditorBridgeImpl");
      return (ParticleEditorBridge)implClass.getDeclaredConstructor().newInstance();
   }

   public Object getModelEditorOverlay() {
      try {
         if (this.editorBridge != null && this.editorBridge.getClass().getName().endsWith("EditorBridgeImpl")) {
            Method method = this.editorBridge.getClass().getMethod("getModelEditorOverlay");
            return method.invoke(this.editorBridge);
         }
      } catch (Throwable var2) {
         Throwable e = var2;
         log.debug("Could not get model editor overlay", e);
      }

      return null;
   }

   public JPanel getEditorPanel() {
      try {
         return this.editorBridge != null ? this.editorBridge.getEditorPanel() : null;
      } catch (Throwable var2) {
         Throwable e = var2;
         this.handleEditorBridgeError(e, "getEditorPanel");
         return null;
      }
   }

   private void loadDefaultConfigs() {
      this.pendingCacheLoad = true;
   }

   private void loadConfigsFromCache() {
      try {
         List<ParticleEmitterConfig> emitters = ParticleDBLoader.loadEmitterConfigs(this.client);
         Iterator var2 = emitters.iterator();

         while(var2.hasNext()) {
            ParticleEmitterConfig config = (ParticleEmitterConfig)var2.next();
            this.particleSystem.registerEmitterConfig(config);
         }

         log.info("Loaded {} emitter configs from cache", emitters.size());
         List<ParticleEffectorConfig> effectors = ParticleDBLoader.loadEffectorConfigs(this.client);
         Iterator var8 = effectors.iterator();

         while(var8.hasNext()) {
            ParticleEffectorConfig config = (ParticleEffectorConfig)var8.next();
            this.particleSystem.registerEffectorConfig(config);
         }

         log.info("Loaded {} effector configs from cache", effectors.size());
         ParticleDBLoader.WhitelistData whitelistData = ParticleDBLoader.loadWhitelist(this.client);
         if (whitelistData != null) {
            this.whitelist = new ParticleWhitelist(whitelistData.spotanims, whitelistData.objs, whitelistData.npcs, whitelistData.locs, whitelistData.kits);
            log.info("Loaded particle whitelist from cache: {} graphics, {} items, {} npcs, {} locs, {} kits", new Object[]{this.whitelist.getGraphicIds().size(), this.whitelist.getWornItemIds().size(), this.whitelist.getNpcIds().size(), this.whitelist.getLocIds().size(), this.whitelist.getKitIds().size()});
         }

         this.lastDbTablesHash = this.computeDbTablesHash();
         this.lastTexturesHash = ParticleTextureManager.computeDataHash(this.client);
         this.hasCacheSnapshot = true;
         if (this.particleTextureManager != null && this.particleTextureManager.isInitialized()) {
            this.resolveEmitterTexturesById();
         }

         if (this.editorBridge != null) {
            SwingUtilities.invokeLater(() -> {
               try {
                  this.editorBridge.onConfigsReloaded();
               } catch (Throwable var2) {
                  Throwable e = var2;
                  this.handleEditorBridgeError(e, "onConfigsReloaded");
               }

            });
         }
      } catch (Exception var5) {
         Exception e = var5;
         log.warn("Could not load particle configs from cache", e);
      }

   }

   private void checkAndReloadIfCacheChanged() {
      if (this.hasCacheSnapshot) {
         int newDbHash = this.computeDbTablesHash();
         int newTexHash = ParticleTextureManager.computeDataHash(this.client);
         boolean dbChanged = newDbHash != this.lastDbTablesHash;
         boolean texChanged = newTexHash != this.lastTexturesHash;
         if (dbChanged || texChanged) {
            log.info("Particle cache changed (db tables={}, textures={}), reloading configs", dbChanged, texChanged);
            this.loadConfigsFromCache();
            if (texChanged) {
               this.pendingTextureReinit = true;
            }

         }
      }
   }

   private int computeDbTablesHash() {
      int hash = 17;
      hash = hash * 31 + this.computeTableHash(65426, 32);
      hash = hash * 31 + this.computeTableHash(65425, 8);
      hash = hash * 31 + this.computeTableHash(65424, 3);
      return hash;
   }

   private int computeTableHash(int tableId, int maxColumn) {
      List<Integer> rows = this.client.getDBTableRows(tableId);
      if (rows != null && !rows.isEmpty()) {
         int hash = rows.size();
         Iterator var5 = rows.iterator();

         while(var5.hasNext()) {
            int rowId = (Integer)var5.next();

            for(int col = 0; col <= maxColumn; ++col) {
               for(int tuple = 0; tuple < 4; ++tuple) {
                  try {
                     Object[] field = this.client.getDBTableField(rowId, col, tuple);
                     if (field != null) {
                        hash = hash * 31 + Arrays.deepHashCode(field);
                     }
                  } catch (NullPointerException | IllegalArgumentException var10) {
                     break;
                  }
               }
            }
         }

         return hash;
      } else {
         return tableId;
      }
   }

   private void loadConfigsFromJson() {
      try {
         InputStream emitterStream = this.getClass().getResourceAsStream("emitters.json");
         InputStream effectorStream = this.getClass().getResourceAsStream("effectors.json");
         if (emitterStream == null && effectorStream == null) {
            log.warn("No particle config resources found");
         } else {
            ParticleConfigLoader.loadIntoSystem(this.particleSystem, emitterStream, effectorStream);
            log.debug("Loaded particle configs from resources");
         }
      } catch (Exception var3) {
         Exception e = var3;
         log.warn("Could not load particle configs from resources", e);
      }

   }

   private boolean hasWhitelistedAppearance(Player player) {
      if (player == null) {
         return false;
      } else {
         PlayerComposition composition = player.getPlayerComposition();
         if (composition == null) {
            return false;
         } else {
            int npcId = composition.getTransformedNpcId();
            if (npcId != -1) {
               return this.whitelist.isNpcWhitelisted(npcId);
            } else if (this.whitelist.getWornItemIds().isEmpty() && this.whitelist.getKitIds().isEmpty()) {
               return false;
            } else {
               int[] equipmentIds = composition.getEquipmentIds();
               if (equipmentIds == null) {
                  return false;
               } else {
                  int[] var5 = equipmentIds;
                  int var6 = equipmentIds.length;

                  for(int var7 = 0; var7 < var6; ++var7) {
                     int equipmentId = var5[var7];
                     int kitId;
                     if (equipmentId >= 2048) {
                        kitId = equipmentId - 2048;
                        if (this.whitelist.isWornItemWhitelisted(kitId)) {
                           return true;
                        }
                     } else if (equipmentId >= 256) {
                        kitId = equipmentId - 256;
                        if (this.whitelist.isKitWhitelisted(kitId)) {
                           return true;
                        }
                     }
                  }

                  return false;
               }
            }
         }
      }
   }

   private boolean isGraphicWhitelisted(int graphicId) {
      return this.whitelist.isGraphicWhitelisted(graphicId);
   }

   private boolean isNpcWhitelisted(int npcId) {
      return this.whitelist.isNpcWhitelisted(npcId);
   }

   private boolean isProjectileWhitelisted(int projectileId) {
      return this.whitelist.isGraphicWhitelisted(projectileId);
   }

   private boolean isLocWhitelisted(int locId) {
      return this.whitelist.isLocWhitelisted(locId);
   }

   @Subscribe
   public void onChatMessage(ChatMessage event) {
      if (event.getType() == ChatMessageType.ENGINE) {
         String message = event.getMessage();
         if (message.startsWith("Particle editor: ")) {
            String url = event.getName();
            if (url != null) {
               if (url.isEmpty()) {
                  log.warn("Particle editor URL is empty");
               } else if (!(this.editorBridge instanceof NoOpEditorBridge)) {
                  log.info("Particle editor already loaded, ignoring message");
               } else {
                  log.info("Received particle editor URL: {}", url);
                  (new Thread(() -> {
                     try {
                        File tempJar = this.downloadEditorJar(url);
                        SwingUtilities.invokeLater(() -> {
                           try {
                              this.initializeEditorFromJar(tempJar);
                           } catch (Throwable var6) {
                              Throwable e = var6;
                              log.error("Failed to initialize particle editor (version mismatch?)", e);
                              this.editorBridge = new NoOpEditorBridge();
                           } finally {
                              if (!tempJar.delete()) {
                                 log.debug("Could not delete temp JAR immediately, will be deleted on exit");
                              }

                           }

                        });
                     } catch (Exception var3) {
                        Exception e = var3;
                        log.error("Failed to download particle editor from {}", url, e);
                     }

                  }, "ParticleEditorDownload")).start();
               }
            }
         }
      }
   }

   private void initializeEditorFromJar(File jarFile) throws Exception {
      if (!jarFile.exists()) {
         log.warn("Particle editor JAR does not exist: {}", jarFile);
      } else {
         if (this.editorBridge != null) {
            this.editorBridge.cleanup();
         }

         if (this.editorClassLoader != null) {
            try {
               this.editorClassLoader.close();
            } catch (Exception var5) {
               Exception e = var5;
               log.warn("Failed to close old editor classloader", e);
            }
         }

         this.editorBridge = this.loadEditorFromJar(jarFile);
         if (this.editorBridge == null) {
            log.error("Failed to load editor from JAR");
            this.editorBridge = new NoOpEditorBridge();
         } else {
            try {
               this.editorBridge.initialize(new ParticleEditorBridge.EditorDependencies(this.client, this.clientThread, this.hooks, this.particleSystem, this.overlayManager, this));
               PluginPanel editorPanel = this.editorBridge.getEditorPanel();
               if (editorPanel != null) {
                  if (this.navButton != null) {
                     this.clientToolbar.removeNavigation(this.navButton);
                  }

                  BufferedImage icon = this.createSnowflakeIcon();
                  this.navButton = NavigationButton.builder().tooltip("Particle Editor").icon(icon).priority(10).panel(editorPanel).build();
                  this.clientToolbar.addNavigation(this.navButton);
                  SwingUtilities.invokeLater(() -> {
                     this.clientToolbar.openPanel(this.navButton);
                  });
               }

               log.info("Particle editor loaded and initialized successfully");
            } catch (Throwable var6) {
               Throwable e = var6;
               log.error("Editor initialization failed (version mismatch between JAR and client?)", e);
               this.editorBridge = new NoOpEditorBridge();
               if (this.editorClassLoader != null) {
                  try {
                     this.editorClassLoader.close();
                  } catch (Exception var4) {
                     Exception ex = var4;
                     log.warn("Failed to close editor classloader after init failure", ex);
                  }

                  this.editorClassLoader = null;
               }
            }

         }
      }
   }

   @Subscribe
   public void onClientTick(ClientTick event) {
      if (this.pendingCacheLoad && this.client.getGameState().getState() >= GameState.LOGIN_SCREEN.getState()) {
         this.pendingCacheLoad = false;
         this.loadConfigsFromCache();
         if (this.client.getGameState() == GameState.LOGGED_IN) {
            this.particleSystem.syncClientCycle(this.client.getGameCycle());
            this.performFullGameScan();
         }
      }

      if (this.config.enabled() && this.particleSystem != null) {
         if (this.client.getGameState() == GameState.LOGGED_IN) {
            if (this.frozen) {
               this.particleSystem.update(this.client);
               this.renderedEntitiesThisFrame.clear();
            } else {
               Player localPlayer = this.client.getLocalPlayer();
               if (localPlayer != null) {
                  WorldView wv = localPlayer.getWorldView();
                  int currentWorldViewId = wv != null ? wv.getId() : -1;
                  if (currentWorldViewId != this.lastLocalPlayerWorldViewId) {
                     log.debug("Local player WorldView changed: {} -> {} (isTopLevel={})", new Object[]{this.lastLocalPlayerWorldViewId, currentWorldViewId, wv != null && wv.isTopLevel()});
                     this.lastLocalPlayerWorldViewId = currentWorldViewId;
                  }
               }

               this.processedProjectiles.clear();
               int currentCycle;
               if (this.config.playerParticles()) {
                  currentCycle = this.client.getGameCycle();
                  List<Player> colourRescanPlayers = null;
                  Iterator var5 = this.playerStates.entrySet().iterator();

                  while(true) {
                     while(var5.hasNext()) {
                        Map.Entry<Player, EntityParticleState> entry = (Map.Entry)var5.next();
                        Player player = (Player)entry.getKey();
                        EntityParticleState state = (EntityParticleState)entry.getValue();
                        if (state.hasPendingSpotanimScan() && currentCycle >= state.pendingSpotanimScanCycle) {
                           this.scanPlayerModelForParticles(player);
                           state.clearPendingSpotanimScan();
                        } else {
                           String[] minimenu = player.minimenuStrings();
                           String colourStr = minimenu != null && minimenu.length >= 3 ? minimenu[2] : null;
                           if (!Objects.equals(colourStr, state.lastColourString)) {
                              if (colourRescanPlayers == null) {
                                 colourRescanPlayers = new ArrayList();
                              }

                              colourRescanPlayers.add(player);
                           }
                        }
                     }

                     if (colourRescanPlayers != null) {
                        var5 = colourRescanPlayers.iterator();

                        while(var5.hasNext()) {
                           Player p = (Player)var5.next();
                           this.scanPlayerModelForParticles(p);
                        }
                     }
                     break;
                  }
               }

               if (this.config.npcParticles()) {
                  currentCycle = this.client.getGameCycle();
                  Iterator var15 = this.npcStates.entrySet().iterator();

                  while(var15.hasNext()) {
                     Map.Entry<NPC, EntityParticleState> entry = (Map.Entry)var15.next();
                     EntityParticleState state = (EntityParticleState)entry.getValue();
                     if (state.hasPendingSpotanimScan() && currentCycle >= state.pendingSpotanimScanCycle) {
                        this.scanNpcModelForParticles((NPC)entry.getKey());
                        state.clearPendingSpotanimScan();
                     }
                  }
               }

               if (this.config.objectEmitters() && !this.pendingObjects.isEmpty()) {
                  this.processPendingObjects();
               }

               if (this.config.playerParticles()) {
                  this.checkPlayerModelFaceCountChanges();
                  this.updatePlayerBoundParticles();
               }

               if (this.config.npcParticles()) {
                  this.updateNpcBoundParticles();
               }

               if (this.config.projectileTrails()) {
                  this.updateProjectileBoundParticles();
               }

               if (this.config.graphicsObjectParticles()) {
                  this.updateGraphicsObjectBoundParticles();
               }

               this.particleSystem.setMaxTotalParticles(this.config.maxParticles());
               this.particleSystem.setMaxEmitterDistanceTiles(this.config.maxRenderDistance());
               if (this.particle3DRenderer != null) {
                  float halfSizeBase = 4.0F * (float)this.config.particleSize() / 3.0F;
                  this.particleSystem.setRenderCullState(this.particle3DRenderer.getCamForwardX(), this.particle3DRenderer.getCamForwardY(), this.particle3DRenderer.getCamForwardZ(), this.particle3DRenderer.getCamRightX(), this.particle3DRenderer.getCamRightZ(), this.particle3DRenderer.getCamUpX(), this.particle3DRenderer.getCamUpY(), this.particle3DRenderer.getCamUpZ(), this.particle3DRenderer.getFrustumScaleW(), this.particle3DRenderer.getFrustumScaleH(), halfSizeBase, (float)this.particle3DRenderer.getCameraX(), (float)this.particle3DRenderer.getCameraY(), (float)this.particle3DRenderer.getCameraZ());
               }

               boolean resumedFromGap = this.particleSystem.update(this.client);
               if (resumedFromGap) {
                  log.info("Particle system resumed from sustained tick gap, performing full rescan");
                  this.performFullGameScan();
               }

               this.renderedEntitiesThisFrame.clear();
            }
         }
      }
   }

   private void processPendingObjects() {
      Iterator<TileObject> it = this.pendingObjects.iterator();

      while(it.hasNext()) {
         TileObject tileObject = (TileObject)it.next();
         Renderable renderable = getTileObjectRenderable(tileObject);
         if (renderable == null) {
            it.remove();
         } else if (this.tryCreateObjectParticlesFromModel(tileObject)) {
            it.remove();
         }
      }

   }

   private static Renderable getTileObjectRenderable(TileObject tileObject) {
      if (tileObject instanceof GameObject) {
         return ((GameObject)tileObject).getRenderable();
      } else if (tileObject instanceof GroundObject) {
         return ((GroundObject)tileObject).getRenderable();
      } else if (tileObject instanceof WallObject) {
         return ((WallObject)tileObject).getRenderable1();
      } else {
         return tileObject instanceof DecorativeObject ? ((DecorativeObject)tileObject).getRenderable() : null;
      }
   }

   @Subscribe
   public void onMenuOpened(MenuOpened event) {
      if (this.config.showEffectorDebug() && this.editorBridge != null) {
         Point mousePos = this.client.getMouseCanvasPosition();
         if (mousePos != null) {
            try {
               ParticleEffector effector = this.editorBridge.findEffectorAtScreen(mousePos.getX(), mousePos.getY());
               if (effector == null || effector.getConfig() == null) {
                  return;
               }

               ParticleEffectorConfig effectorConfig = effector.getConfig();
               String target = buildEffectorTargetString(effector, effectorConfig);
               this.client.createMenuEntry(-1).setOption("Inspect").setTarget(target).setType(MenuAction.RUNELITE).onClick((ex) -> {
                  if (this.navButton != null) {
                     this.clientToolbar.openPanel(this.navButton);
                  }

                  try {
                     this.editorBridge.inspectEffectorConfig(effectorConfig);
                  } catch (Throwable var4) {
                     Throwable exx = var4;
                     log.warn("Failed to inspect effector config", exx);
                  }

               });
            } catch (Throwable var6) {
               Throwable e = var6;
               log.warn("Editor bridge error in onMenuOpened (version mismatch?), disabling editor", e);
               this.editorBridge = new NoOpEditorBridge();
            }

         }
      }
   }

   private static @NotNull String buildEffectorTargetString(ParticleEffector effector, ParticleEffectorConfig effectorConfig) {
      String posInfo = String.format("(%d, %d, %d)", effector.getWorldX(), effector.getWorldY(), effector.getWorldZ());
      String attachInfo = "";
      if (effector.isAttachedToFace()) {
         attachInfo = ", face " + effector.getAttachedFaceIndex();
         if (effector.isInheritDirection()) {
            attachInfo = attachInfo + " (inherit)";
         }
      }

      String target = String.format("<col=aa00ff>Effector #%d %s%s</col>", effectorConfig.getId(), posInfo, attachInfo);
      return target;
   }

   @Subscribe
   public void onProjectileMoved(ProjectileMoved event) {
      if (this.config.enabled() && this.config.projectileTrails() && this.particleSystem != null) {
         Projectile projectile = event.getProjectile();
         if (this.isProjectileWhitelisted(projectile.getId())) {
            if (!this.processedProjectiles.contains(projectile)) {
               this.processedProjectiles.add(projectile);
               if (projectile.getRemainingCycles() <= 0) {
                  this.clearProjectileParticles(projectile);
               } else {
                  if (!this.projectileStates.containsKey(projectile)) {
                     this.scanProjectileModelForParticles(projectile);
                  }

               }
            }
         }
      }
   }

   @Subscribe
   public void onGraphicsObjectCreated(GraphicsObjectCreated event) {
      if (this.config.enabled() && this.config.graphicsObjectParticles() && this.particleSystem != null) {
         GraphicsObject graphicsObject = event.getGraphicsObject();
         this.scanGraphicsObjectForParticles(graphicsObject);
      }
   }

   private void invalidateEmitterSceneCaches() {
      Iterator var1;
      if (this.particleSystem != null) {
         var1 = this.particleSystem.getActiveEmitters().iterator();

         while(var1.hasNext()) {
            ParticleEmitter emitter = (ParticleEmitter)var1.next();
            emitter.invalidateSceneCache();
         }
      }

      var1 = this.objectStates.values().iterator();

      Iterator var3;
      ParticleEmitter emitter;
      while(var1.hasNext()) {
         GameObjectParticleState state = (GameObjectParticleState)var1.next();
         var3 = state.emitters.iterator();

         while(var3.hasNext()) {
            emitter = (ParticleEmitter)var3.next();
            emitter.invalidateSceneCache();
         }
      }

      var1 = this.playerStates.values().iterator();

      EntityParticleState state;
      while(var1.hasNext()) {
         state = (EntityParticleState)var1.next();
         var3 = state.emitters.iterator();

         while(var3.hasNext()) {
            emitter = (ParticleEmitter)var3.next();
            emitter.invalidateSceneCache();
         }
      }

      var1 = this.npcStates.values().iterator();

      while(var1.hasNext()) {
         state = (EntityParticleState)var1.next();
         var3 = state.emitters.iterator();

         while(var3.hasNext()) {
            emitter = (ParticleEmitter)var3.next();
            emitter.invalidateSceneCache();
         }
      }

      var1 = this.projectileStates.values().iterator();

      while(var1.hasNext()) {
         state = (EntityParticleState)var1.next();
         var3 = state.emitters.iterator();

         while(var3.hasNext()) {
            emitter = (ParticleEmitter)var3.next();
            emitter.invalidateSceneCache();
         }
      }

      var1 = this.graphicsObjectStates.values().iterator();

      while(var1.hasNext()) {
         state = (EntityParticleState)var1.next();
         var3 = state.emitters.iterator();

         while(var3.hasNext()) {
            emitter = (ParticleEmitter)var3.next();
            emitter.invalidateSceneCache();
         }
      }

   }

   @Subscribe
   public void onGameStateChanged(GameStateChanged event) {
      if (event.getGameState() == GameState.LOGIN_SCREEN) {
         if (this.pendingCacheLoad) {
            this.pendingCacheLoad = false;
            this.loadConfigsFromCache();
         } else {
            this.checkAndReloadIfCacheChanged();
         }

         this.processedProjectiles.clear();
         this.objectStates.clear();
         this.playerStates.clear();
         this.npcStates.clear();
         this.projectileStates.clear();
         this.graphicsObjectStates.clear();
         this.pendingObjects.clear();
         this.gameObjectModelScanCache.clear();
         if (this.particleSystem != null) {
            this.particleSystem.clear();
         }

         if (this.editorBridge != null) {
            try {
               this.editorBridge.resetAll();
               SwingUtilities.invokeLater(() -> {
                  try {
                     this.editorBridge.updatePlacementButtonState();
                     this.editorBridge.clearModelViewer();
                  } catch (Throwable var2) {
                     Throwable e = var2;
                     this.handleEditorBridgeError(e, "onGameStateChanged/invokeLater");
                  }

               });
            } catch (Throwable var17) {
               Throwable e = var17;
               this.handleEditorBridgeError(e, "onGameStateChanged/resetAll");
            }
         }
      } else if (event.getGameState() == GameState.LOADING) {
         this.loading = true;
         Particle.clearExtendedHeightCache();
         this.invalidateEmitterSceneCaches();
      } else if (event.getGameState() == GameState.LOGGED_IN && this.loading) {
         int baseX = this.client.getTopLevelWorldView().getBaseX();
         int baseY = this.client.getTopLevelWorldView().getBaseY();
         if (baseX != this.lastBaseX || baseY != this.lastBaseY) {
            int shiftX = baseX - this.lastBaseX;
            int shiftY = baseY - this.lastBaseY;
            this.lastBaseX = baseX;
            this.lastBaseY = baseY;
            int extOffset = 40;
            int extMinX = baseX - extOffset;
            int extMinY = baseY - extOffset;
            int extMaxX = extMinX + 184 - 1;
            int extMaxY = extMinY + 184 - 1;
            int shiftLocalX = -shiftX * 128;
            int shiftLocalZ = -shiftY * 128;
            this.objectStates.entrySet().removeIf((entry) -> {
               long key = (Long)entry.getKey();
               int x = (int)(key & 32767L);
               int y = (int)(key >> 15 & 32767L);
               GameObjectParticleState state;
               Iterator var13;
               ParticleEffector effector;
               if (x >= extMinX && x <= extMaxX && y >= extMinY && y <= extMaxY) {
                  state = (GameObjectParticleState)entry.getValue();
                  var13 = state.effectors.iterator();

                  while(var13.hasNext()) {
                     effector = (ParticleEffector)var13.next();
                     effector.shiftPosition(shiftLocalX, shiftLocalZ);
                  }

                  return false;
               } else {
                  state = (GameObjectParticleState)entry.getValue();
                  var13 = state.emitters.iterator();

                  while(var13.hasNext()) {
                     ParticleEmitter emitter = (ParticleEmitter)var13.next();
                     emitter.reset();
                     if (this.particleSystem != null) {
                        this.particleSystem.removeEmitter(emitter);
                     }
                  }

                  var13 = state.effectors.iterator();

                  while(var13.hasNext()) {
                     effector = (ParticleEffector)var13.next();
                     effector.deactivate();
                     if (this.particleSystem != null) {
                        this.particleSystem.removeEffector(effector);
                     }
                  }

                  return true;
               }
            });
            if (this.particleSystem != null) {
               Iterator var13 = this.particleSystem.getActiveEmitters().iterator();

               while(var13.hasNext()) {
                  ParticleEmitter emitter = (ParticleEmitter)var13.next();
                  emitter.checkAliveParticles();
               }
            }

            if (this.editorBridge != null) {
               try {
                  this.editorBridge.repositionEditorObjects(shiftX, shiftY);
               } catch (Throwable var16) {
                  Throwable e = var16;
                  this.handleEditorBridgeError(e, "repositionEditorObjects");
               }
            }
         }

         if (this.editorBridge != null) {
            try {
               this.editorBridge.revalidateEditorObjects();
            } catch (Throwable var15) {
               Throwable e = var15;
               this.handleEditorBridgeError(e, "revalidateEditorObjects");
            }
         }

         this.loading = false;
         Particle.populateExtendedHeightCache(this.client.getTopLevelWorldView());
         if (this.config.objectEmitters()) {
            this.scanExtendedObjectEmitters();
         }
      }

   }

   @Subscribe
   public void onGameObjectSpawned(GameObjectSpawned event) {
      this.handleTileObjectSpawned(event.getGameObject());
   }

   private void handleTileObjectSpawned(TileObject tileObject) {
      if (this.config.enabled() && this.config.objectEmitters() && this.particleSystem != null) {
         int objectId = tileObject.getId();
         if (this.isLocWhitelisted(objectId)) {
            if (!this.tryCreateObjectParticlesFromModel(tileObject)) {
               this.pendingObjects.add(tileObject);
               log.debug("Object {} model not ready, queued for later", objectId);
            }

         }
      }
   }

   private static long stableTileObjectKey(TileObject obj) {
      WorldPoint wp = obj.getWorldLocation();
      int id = obj.getId();
      int plane = wp.getPlane();
      long key = 0L;
      key |= (long)(wp.getX() & 32767);
      key |= (long)(wp.getY() & 32767) << 15;
      key |= (long)(plane & 3) << 30;
      key |= ((long)id & 4294967295L) << 32;
      return key;
   }

   private GameObjectModelScanResult getOrScanGameObjectModel(int objectId, Model model) {
      int faceCount = model.getFaceCount();
      long modelKey = buildModelKey(objectId, model, faceCount);
      GameObjectModelScanResult cached = (GameObjectModelScanResult)this.gameObjectModelScanCache.get(modelKey);
      if (cached != null) {
         log.debug("Using cached scan result for model key {} ({} emitters, {} effectors)", new Object[]{modelKey, cached.emitterFaces.size(), cached.effectorFaces.size()});
         return cached;
      } else {
         List<GameObjectModelScanResult.MarkerFace> emitterFaces = new ArrayList();
         List<GameObjectModelScanResult.MarkerFace> effectorFaces = new ArrayList();
         byte[] transparencies = model.getFaceTransparencies();
         byte[] priorities = model.getFaceRenderPriorities();
         short[] textures = model.getFaceTextures();
         if (transparencies != null && priorities != null && textures != null) {
            for(int faceIndex = 0; faceIndex < faceCount; ++faceIndex) {
               int transparency = transparencies[faceIndex] & 255;
               int priority = priorities[faceIndex] & 255;
               if (transparency == 255 && priority >= 252 && priority <= 254) {
                  int textureId = textures[faceIndex] & '\uffff';
                  if (priority == 254) {
                     emitterFaces.add(new GameObjectModelScanResult.MarkerFace(faceIndex, textureId, false));
                  } else {
                     boolean inheritDirection = priority == 252;
                     effectorFaces.add(new GameObjectModelScanResult.MarkerFace(faceIndex, textureId, inheritDirection));
                  }
               }
            }
         } else {
            log.debug("Object {} model missing arrays for marker detection: transparencies={}, priorities={}, textures={}", new Object[]{modelKey, transparencies != null, priorities != null, textures != null});
         }

         GameObjectModelScanResult result = new GameObjectModelScanResult(emitterFaces, effectorFaces);
         this.gameObjectModelScanCache.put(modelKey, result);
         return result;
      }
   }

   private static long buildModelKey(int objectId, Model model, int faceCount) {
      int vertexCount = model.getVerticesCount();
      long modelKey = (long)objectId;
      modelKey = modelKey * 31L + (long)faceCount;
      modelKey = modelKey * 31L + (long)vertexCount;
      float[] verticesX = model.getVerticesX();
      float[] verticesY = model.getVerticesY();
      float[] verticesZ = model.getVerticesZ();
      if (verticesX != null && verticesX.length > 0) {
         modelKey = modelKey * 31L + (long)Float.floatToIntBits(verticesX[0]);
         modelKey = modelKey * 31L + (long)Float.floatToIntBits(verticesY[0]);
         modelKey = modelKey * 31L + (long)Float.floatToIntBits(verticesZ[0]);
         int last = verticesX.length - 1;
         modelKey = modelKey * 31L + (long)Float.floatToIntBits(verticesX[last]);
         modelKey = modelKey * 31L + (long)Float.floatToIntBits(verticesY[last]);
         modelKey = modelKey * 31L + (long)Float.floatToIntBits(verticesZ[last]);
      }

      return modelKey;
   }

   private boolean tryCreateObjectParticlesFromModel(TileObject tileObject) {
      if (this.loading) {
         return false;
      } else {
         Renderable renderable = getTileObjectRenderable(tileObject);
         if (renderable == null) {
            return true;
         } else {
            Model model;
            if (renderable instanceof Model) {
               model = (Model)renderable;
            } else {
               if (!(renderable instanceof DynamicObject)) {
                  return true;
               }

               model = renderable.getModel();
            }

            if (model == null) {
               return false;
            } else {
               byte[] transparencies = model.getFaceTransparencies();
               byte[] priorities = model.getFaceRenderPriorities();
               short[] textures = model.getFaceTextures();
               if (transparencies != null && priorities != null && textures != null) {
                  long objectHash = stableTileObjectKey(tileObject);
                  if (this.objectStates.containsKey(objectHash)) {
                     return true;
                  } else {
                     GameObjectModelScanResult scanResult = this.getOrScanGameObjectModel(tileObject.getId(), model);
                     if (!scanResult.hasMarkers) {
                        return true;
                     } else {
                        WorldPoint point = tileObject.getWorldLocation();
                        WorldView worldView = tileObject.getWorldView();
                        int baseX = worldView.getBaseX();
                        int baseY = worldView.getBaseY();
                        WorldEntity worldEntity = null;
                        if (!worldView.isTopLevel()) {
                           worldEntity = (WorldEntity)this.client.getTopLevelWorldView().worldEntities().byIndex(worldView.getId());
                        }

                        GameObjectParticleState state = new GameObjectParticleState();
                        state.scanResult = scanResult;
                        Iterator var16 = scanResult.emitterFaces.iterator();

                        GameObjectModelScanResult.MarkerFace markerFace;
                        int[] vertices;
                        int centerX;
                        int centerY;
                        int centerZ;
                        while(var16.hasNext()) {
                           markerFace = (GameObjectModelScanResult.MarkerFace)var16.next();
                           vertices = this.calculateTileObjectFaceVertices(model, markerFace.faceIndex, tileObject);
                           if (vertices != null) {
                              centerX = (vertices[0] + vertices[3] + vertices[6]) / 3;
                              centerY = (vertices[1] + vertices[4] + vertices[7]) / 3;
                              centerZ = (vertices[2] + vertices[5] + vertices[8]) / 3;
                              ParticleEmitter emitter = this.particleSystem.createEmitter(markerFace.markerId, centerX, centerY, centerZ, point, baseX, baseY, worldView, worldEntity);
                              if (emitter != null) {
                                 emitter.setPlane(tileObject.getPlane());
                                 emitter.setAttachedFaceIndex(markerFace.faceIndex);
                                 emitter.setAttachedMarkerId(markerFace.markerId);
                                 emitter.setSpawnArea(vertices[0], vertices[1], vertices[2], vertices[3], vertices[4], vertices[5], vertices[6], vertices[7], vertices[8], this.particleSystem.getClientCycle());
                                 state.emitters.add(emitter);
                                 log.debug("Created emitter {} for tile object {} at face {}", new Object[]{markerFace.markerId, tileObject.getId(), markerFace.faceIndex});
                              }
                           }
                        }

                        var16 = scanResult.effectorFaces.iterator();

                        while(var16.hasNext()) {
                           markerFace = (GameObjectModelScanResult.MarkerFace)var16.next();
                           vertices = this.calculateTileObjectFaceVertices(model, markerFace.faceIndex, tileObject);
                           if (vertices != null) {
                              centerX = (vertices[0] + vertices[3] + vertices[6]) / 3;
                              centerY = (vertices[1] + vertices[4] + vertices[7]) / 3;
                              centerZ = (vertices[2] + vertices[5] + vertices[8]) / 3;
                              ParticleEffector effector = this.particleSystem.createGlobalEffector(markerFace.markerId, centerX, centerY, centerZ);
                              if (effector != null) {
                                 effector.setAttachedFaceIndex(markerFace.faceIndex);
                                 effector.setAttachedMarkerId(markerFace.markerId);
                                 effector.setInheritDirection(markerFace.inheritDirection);
                                 if (markerFace.inheritDirection) {
                                    effector.setFaceNormal(vertices[0], vertices[1], vertices[2], vertices[3], vertices[4], vertices[5], vertices[6], vertices[7], vertices[8]);
                                 }

                                 state.effectors.add(effector);
                                 log.debug("Created effector {} for tile object {} at face {} (inheritDirection={})", new Object[]{markerFace.markerId, tileObject.getId(), markerFace.faceIndex, markerFace.inheritDirection});
                              }
                           }
                        }

                        if (!state.isEmpty()) {
                           this.objectStates.put(objectHash, state);
                        }

                        return true;
                     }
                  }
               } else {
                  log.debug("Object {} model missing arrays for marker detection: transparencies={}, priorities={}, textures={}", new Object[]{tileObject.getId(), transparencies != null, priorities != null, textures != null});
                  return true;
               }
            }
         }
      }
   }

   private int[] calculateTileObjectFaceVertices(Model model, int faceIndex, TileObject tileObject) {
      int faceCount = model.getFaceCount();
      if (faceIndex >= faceCount) {
         return null;
      } else {
         int[] fv1 = model.getFaceIndices1();
         int[] fv2 = model.getFaceIndices2();
         int[] fv3 = model.getFaceIndices3();
         if (fv1 != null && fv2 != null && fv3 != null) {
            int v1Idx = fv1[faceIndex];
            int v2Idx = fv2[faceIndex];
            int v3Idx = fv3[faceIndex];
            float[] verticesX = model.getVerticesX();
            float[] verticesY = model.getVerticesY();
            float[] verticesZ = model.getVerticesZ();
            if (verticesX != null && verticesY != null && verticesZ != null) {
               WorldView wv = tileObject.getWorldView();
               if (wv == null) {
                  return null;
               } else {
                  LocalPoint lp = tileObject.getLocalLocation();
                  int localX = lp.getX();
                  int localY = lp.getY();
                  int plane = tileObject.getPlane();
                  int tileHeight = wv.getTileHeight(localX, localY, plane);
                  int baseX = localX;
                  int baseZ = localY;
                  int[] result = new int[]{baseX + (int)verticesX[v1Idx], tileHeight + (int)verticesY[v1Idx], baseZ + (int)verticesZ[v1Idx], baseX + (int)verticesX[v2Idx], tileHeight + (int)verticesY[v2Idx], baseZ + (int)verticesZ[v2Idx], baseX + (int)verticesX[v3Idx], tileHeight + (int)verticesY[v3Idx], baseZ + (int)verticesZ[v3Idx]};
                  return result;
               }
            } else {
               return null;
            }
         } else {
            return null;
         }
      }
   }

   @Subscribe
   public void onGameObjectDespawned(GameObjectDespawned event) {
      this.handleTileObjectDespawned(event.getGameObject());
   }

   @Subscribe
   public void onGroundObjectSpawned(GroundObjectSpawned event) {
      this.handleTileObjectSpawned(event.getGroundObject());
   }

   @Subscribe
   public void onGroundObjectDespawned(GroundObjectDespawned event) {
      this.handleTileObjectDespawned(event.getGroundObject());
   }

   @Subscribe
   public void onWallObjectSpawned(WallObjectSpawned event) {
      this.handleTileObjectSpawned(event.getWallObject());
   }

   @Subscribe
   public void onWallObjectDespawned(WallObjectDespawned event) {
      this.handleTileObjectDespawned(event.getWallObject());
   }

   @Subscribe
   public void onDecorativeObjectSpawned(DecorativeObjectSpawned event) {
      this.handleTileObjectSpawned(event.getDecorativeObject());
   }

   @Subscribe
   public void onDecorativeObjectDespawned(DecorativeObjectDespawned event) {
      this.handleTileObjectDespawned(event.getDecorativeObject());
   }

   private void handleTileObjectDespawned(TileObject tileObject) {
      long objectHash = stableTileObjectKey(tileObject);
      this.pendingObjects.remove(tileObject);
      GameObjectParticleState state = (GameObjectParticleState)this.objectStates.remove(objectHash);
      if (state != null) {
         Iterator var5 = state.emitters.iterator();

         while(var5.hasNext()) {
            ParticleEmitter emitter = (ParticleEmitter)var5.next();
            emitter.stop();
            if (this.particleSystem != null) {
               this.particleSystem.removeEmitter(emitter);
            }
         }

         var5 = state.effectors.iterator();

         while(var5.hasNext()) {
            ParticleEffector effector = (ParticleEffector)var5.next();
            if (this.particleSystem != null) {
               this.particleSystem.removeEffector(effector);
            }
         }
      }

   }

   @Subscribe
   public void onPlayerSpawned(PlayerSpawned event) {
      if (this.config.enabled() && this.config.playerParticles() && this.particleSystem != null) {
         Player player = event.getPlayer();
         if (this.hasWhitelistedAppearance(player)) {
            this.scanPlayerModelForParticles(player);
         }
      }
   }

   @Subscribe
   public void onPlayerChanged(PlayerChanged event) {
      if (this.config.enabled() && this.config.playerParticles() && this.particleSystem != null) {
         Player player = event.getPlayer();
         if (!this.hasWhitelistedAppearance(player)) {
            this.clearPlayerParticles(player);
         } else {
            this.scanPlayerModelForParticles(player);
         }
      }
   }

   @Subscribe
   public void onGraphicChanged(GraphicChanged event) {
      if (this.config.enabled() && this.particleSystem != null) {
         Actor actor = event.getActor();
         ActorSpotAnim lastSpotAnim;
         Iterator var5;
         ActorSpotAnim spotAnim;
         if (actor instanceof Player && this.config.playerParticles()) {
            Player player = (Player)actor;
            lastSpotAnim = null;

            for(var5 = player.getSpotAnims().iterator(); var5.hasNext(); lastSpotAnim = spotAnim) {
               spotAnim = (ActorSpotAnim)var5.next();
            }

            if (lastSpotAnim != null && this.isGraphicWhitelisted(lastSpotAnim.getId())) {
               this.getOrCreatePlayerState(player).pendingSpotanimScanCycle = lastSpotAnim.getStartCycle() + 1;
            }
         } else if (actor instanceof NPC && this.config.npcParticles()) {
            NPC npc = (NPC)actor;
            lastSpotAnim = null;

            for(var5 = npc.getSpotAnims().iterator(); var5.hasNext(); lastSpotAnim = spotAnim) {
               spotAnim = (ActorSpotAnim)var5.next();
            }

            if (lastSpotAnim != null && this.isGraphicWhitelisted(lastSpotAnim.getId())) {
               this.getOrCreateNpcState(npc).pendingSpotanimScanCycle = lastSpotAnim.getStartCycle() + 1;
            }
         }

      }
   }

   @Subscribe
   public void onPlayerDespawned(PlayerDespawned event) {
      Player player = event.getPlayer();
      this.clearPlayerParticles(player);
   }

   private void clearPlayerParticles(Player player) {
      EntityParticleState state = (EntityParticleState)this.playerStates.remove(player);
      if (state != null) {
         Iterator var3 = state.emitters.iterator();

         while(var3.hasNext()) {
            ParticleEmitter emitter = (ParticleEmitter)var3.next();
            emitter.stop();
            if (this.particleSystem != null) {
               this.particleSystem.removeEmitter(emitter);
            }
         }

         var3 = state.effectors.iterator();

         while(var3.hasNext()) {
            ParticleEffector effector = (ParticleEffector)var3.next();
            if (this.particleSystem != null) {
               this.particleSystem.removeEffector(effector);
            }
         }
      }

   }

   private EntityParticleState getOrCreatePlayerState(Player player) {
      return (EntityParticleState)this.playerStates.computeIfAbsent(player, (k) -> {
         return new EntityParticleState();
      });
   }

   private EntityParticleState getOrCreateNpcState(NPC npc) {
      return (EntityParticleState)this.npcStates.computeIfAbsent(npc, (k) -> {
         return new EntityParticleState();
      });
   }

   private EntityParticleState getOrCreateProjectileState(Projectile projectile) {
      return (EntityParticleState)this.projectileStates.computeIfAbsent(projectile, (k) -> {
         return new EntityParticleState();
      });
   }

   private EntityParticleState getOrCreateGraphicsObjectState(GraphicsObject graphicsObject) {
      return (EntityParticleState)this.graphicsObjectStates.computeIfAbsent(graphicsObject, (k) -> {
         return new EntityParticleState();
      });
   }

   @Subscribe
   public void onNpcSpawned(NpcSpawned event) {
      if (this.config.enabled() && this.config.npcParticles() && this.particleSystem != null) {
         NPC npc = event.getNpc();
         if (this.isNpcWhitelisted(npc.getId())) {
            this.scanNpcModelForParticles(npc);
         }
      }
   }

   @Subscribe
   public void onNpcChanged(NpcChanged event) {
      if (this.config.enabled() && this.config.npcParticles() && this.particleSystem != null) {
         NPC npc = event.getNpc();
         if (!this.isNpcWhitelisted(npc.getId())) {
            this.clearNpcParticles(npc);
         } else {
            this.scanNpcModelForParticles(npc);
         }
      }
   }

   @Subscribe
   public void onNpcDespawned(NpcDespawned event) {
      NPC npc = event.getNpc();
      this.clearNpcParticles(npc);
   }

   private void clearNpcParticles(NPC npc) {
      EntityParticleState state = (EntityParticleState)this.npcStates.remove(npc);
      if (state != null) {
         Iterator var3 = state.emitters.iterator();

         while(var3.hasNext()) {
            ParticleEmitter emitter = (ParticleEmitter)var3.next();
            emitter.stop();
            if (this.particleSystem != null) {
               this.particleSystem.removeEmitter(emitter);
            }
         }

         var3 = state.effectors.iterator();

         while(var3.hasNext()) {
            ParticleEffector effector = (ParticleEffector)var3.next();
            if (this.particleSystem != null) {
               this.particleSystem.removeEffector(effector);
            }
         }
      }

   }

   private void clearProjectileParticles(Projectile projectile) {
      EntityParticleState state = (EntityParticleState)this.projectileStates.remove(projectile);
      if (state != null) {
         Iterator var3 = state.emitters.iterator();

         while(var3.hasNext()) {
            ParticleEmitter emitter = (ParticleEmitter)var3.next();
            emitter.stop();
         }

         var3 = state.effectors.iterator();

         while(var3.hasNext()) {
            ParticleEffector effector = (ParticleEffector)var3.next();
            if (this.particleSystem != null) {
               this.particleSystem.removeEffector(effector);
            }
         }
      }

   }

   private void scanProjectileModelForParticles(Projectile projectile) {
      if (projectile != null) {
         Model model = projectile.getModel();
         if (model != null) {
            if (model.getFaceCount() > 0) {
               byte[] transparencies = model.getFaceTransparencies();
               byte[] priorities = model.getFaceRenderPriorities();
               short[] textures = model.getFaceTextures();
               if (transparencies != null && priorities != null && textures != null) {
                  int faceCount = model.getFaceCount();
                  this.scanScratch.clear();
                  int projX = (int)projectile.getX();
                  int projZ = (int)projectile.getY();
                  WorldView worldView = null;
                  Actor sourceActor = projectile.getSourceActor();
                  if (sourceActor != null) {
                     worldView = sourceActor.getWorldView();
                  }

                  if (worldView == null) {
                     worldView = this.client.getTopLevelWorldView();
                  }

                  int baseX = worldView.getBaseX();
                  int baseY = worldView.getBaseY();
                  WorldPoint worldPoint = WorldPoint.fromLocal(this.client, projX, projZ, projectile.getSourceLevel());
                  WorldEntity worldEntity = null;
                  if (!worldView.isTopLevel()) {
                     worldEntity = (WorldEntity)this.client.getTopLevelWorldView().worldEntities().byIndex(worldView.getId());
                  }

                  for(int faceIndex = 0; faceIndex < faceCount; ++faceIndex) {
                     int transparency = transparencies[faceIndex] & 255;
                     int priority = priorities[faceIndex] & 255;
                     if (transparency == 255 && priority >= 252 && priority <= 254) {
                        int textureId = textures[faceIndex] & '\uffff';
                        int[] faceCenter = this.calculateProjectileFaceCenter(projectile, model, faceIndex);
                        if (faceCenter != null) {
                           int worldX = faceCenter[0];
                           int worldY = faceCenter[1];
                           int worldZ = faceCenter[2];
                           if (priority == 254) {
                              ParticleEmitter emitter = this.particleSystem.createEmitter(textureId, worldX, worldY, worldZ, worldPoint, baseX, baseY, worldView, worldEntity);
                              if (emitter != null) {
                                 emitter.setPlane(worldPoint.getPlane());
                                 emitter.setAttachedFaceIndex(faceIndex);
                                 emitter.setAttachedMarkerId(textureId);
                                 int[] vertices = this.calculateProjectileFaceVertices(projectile, model, faceIndex);
                                 if (vertices != null) {
                                    emitter.setSpawnArea(vertices[0], vertices[1], vertices[2], vertices[3], vertices[4], vertices[5], vertices[6], vertices[7], vertices[8], this.particleSystem.getClientCycle());
                                 }

                                 this.scanScratch.newEmitters.add(emitter);
                                 log.debug("Created projectile emitter {} at face {} for projectile {}", new Object[]{textureId, faceIndex, projectile.getId()});
                              }
                           } else {
                              boolean inheritDirection = priority == 252;
                              ParticleEffector effector = this.particleSystem.createGlobalEffector(textureId, worldX, worldY, worldZ);
                              if (effector != null) {
                                 effector.setAttachedFaceIndex(faceIndex);
                                 effector.setAttachedMarkerId(textureId);
                                 effector.setInheritDirection(inheritDirection);
                                 if (inheritDirection) {
                                    int[] vertices = this.calculateProjectileFaceVertices(projectile, model, faceIndex);
                                    if (vertices != null) {
                                       effector.setFaceNormal(vertices[0], vertices[1], vertices[2], vertices[3], vertices[4], vertices[5], vertices[6], vertices[7], vertices[8]);
                                    }
                                 }

                                 this.scanScratch.newEffectors.add(effector);
                                 log.debug("Created projectile effector {} at face {} for projectile {} (inheritDirection={})", new Object[]{textureId, faceIndex, projectile.getId(), inheritDirection});
                              }
                           }
                        }
                     }
                  }

                  if (!this.scanScratch.newEmitters.isEmpty() || !this.scanScratch.newEffectors.isEmpty()) {
                     EntityParticleState state = this.getOrCreateProjectileState(projectile);
                     state.emitters.addAll(this.scanScratch.newEmitters);
                     state.effectors.addAll(this.scanScratch.newEffectors);
                  }

               }
            }
         }
      }
   }

   private int[] calculateProjectileFaceCenter(Projectile projectile, Model model, int faceIndex) {
      int posX = (int)projectile.getX();
      int posY = (int)projectile.getZ();
      int posZ = (int)projectile.getY();
      int yawOrientation = projectile.getOrientation();
      double pitch = Math.atan((double)projectile.getSlope());
      return this.calculateModelFaceCenter(model, faceIndex, posX, posY, posZ, yawOrientation, pitch, this.reusableVertex3) ? this.reusableVertex3 : null;
   }

   private int[] calculateProjectileFaceVertices(Projectile projectile, Model model, int faceIndex) {
      int posX = (int)projectile.getX();
      int posY = (int)projectile.getZ();
      int posZ = (int)projectile.getY();
      int yawOrientation = projectile.getOrientation();
      double pitch = Math.atan((double)projectile.getSlope());
      return this.calculateModelFaceVertices(model, faceIndex, posX, posY, posZ, yawOrientation, pitch, this.reusableVertices9, this.reusableTempVertex) ? this.reusableVertices9 : null;
   }

   private void scanGraphicsObjectForParticles(GraphicsObject graphicsObject) {
      if (graphicsObject != null) {
         Model model = graphicsObject.getModel();
         if (model != null) {
            if (model.getFaceCount() > 0) {
               byte[] transparencies = model.getFaceTransparencies();
               byte[] priorities = model.getFaceRenderPriorities();
               short[] textures = model.getFaceTextures();
               if (transparencies != null && priorities != null && textures != null) {
                  int faceCount = model.getFaceCount();
                  this.scanScratch.clear();
                  LocalPoint location = graphicsObject.getLocation();
                  WorldView worldView = graphicsObject.getWorldView();
                  if (worldView == null) {
                     worldView = this.client.getTopLevelWorldView();
                  }

                  int baseX = worldView.getBaseX();
                  int baseY = worldView.getBaseY();
                  WorldPoint worldPoint = WorldPoint.fromLocal(this.client, location);
                  WorldEntity worldEntity = null;
                  if (!worldView.isTopLevel()) {
                     worldEntity = (WorldEntity)this.client.getTopLevelWorldView().worldEntities().byIndex(worldView.getId());
                  }

                  for(int faceIndex = 0; faceIndex < faceCount; ++faceIndex) {
                     int transparency = transparencies[faceIndex] & 255;
                     int priority = priorities[faceIndex] & 255;
                     if (transparency == 255 && priority >= 252 && priority <= 254) {
                        int textureId = textures[faceIndex] & '\uffff';
                        int[] faceCenter = this.calculateGraphicsObjectFaceCenter(graphicsObject, model, faceIndex);
                        if (faceCenter != null) {
                           int worldX = faceCenter[0];
                           int worldY = faceCenter[1];
                           int worldZ = faceCenter[2];
                           if (priority == 254) {
                              ParticleEmitter emitter = this.particleSystem.createEmitter(textureId, worldX, worldY, worldZ, worldPoint, baseX, baseY, worldView, worldEntity);
                              if (emitter != null) {
                                 emitter.setPlane(graphicsObject.getLevel());
                                 emitter.setAttachedFaceIndex(faceIndex);
                                 emitter.setAttachedMarkerId(textureId);
                                 int[] vertices = this.calculateGraphicsObjectFaceVertices(graphicsObject, model, faceIndex);
                                 if (vertices != null) {
                                    emitter.setSpawnArea(vertices[0], vertices[1], vertices[2], vertices[3], vertices[4], vertices[5], vertices[6], vertices[7], vertices[8], this.particleSystem.getClientCycle());
                                 }

                                 this.scanScratch.newEmitters.add(emitter);
                                 log.debug("Created graphics object emitter {} at face {} for spotanim {}", new Object[]{textureId, faceIndex, graphicsObject.getId()});
                              }
                           } else {
                              boolean inheritDirection = priority == 252;
                              ParticleEffector effector = this.particleSystem.createGlobalEffector(textureId, worldX, worldY, worldZ);
                              if (effector != null) {
                                 effector.setAttachedFaceIndex(faceIndex);
                                 effector.setAttachedMarkerId(textureId);
                                 effector.setInheritDirection(inheritDirection);
                                 if (inheritDirection) {
                                    int[] vertices = this.calculateGraphicsObjectFaceVertices(graphicsObject, model, faceIndex);
                                    if (vertices != null) {
                                       effector.setFaceNormal(vertices[0], vertices[1], vertices[2], vertices[3], vertices[4], vertices[5], vertices[6], vertices[7], vertices[8]);
                                    }
                                 }

                                 this.scanScratch.newEffectors.add(effector);
                                 log.debug("Created graphics object effector {} at face {} for spotanim {} (inheritDirection={})", new Object[]{textureId, faceIndex, graphicsObject.getId(), inheritDirection});
                              }
                           }
                        }
                     }
                  }

                  if (!this.scanScratch.newEmitters.isEmpty() || !this.scanScratch.newEffectors.isEmpty()) {
                     EntityParticleState state = this.getOrCreateGraphicsObjectState(graphicsObject);
                     state.emitters.addAll(this.scanScratch.newEmitters);
                     state.effectors.addAll(this.scanScratch.newEffectors);
                  }

               }
            }
         }
      }
   }

   private int[] calculateGraphicsObjectFaceCenter(GraphicsObject graphicsObject, Model model, int faceIndex) {
      LocalPoint location = graphicsObject.getLocation();
      int posX = location.getX();
      int posZ = location.getY();
      int posY = graphicsObject.getZ();
      return this.calculateModelFaceCenter(model, faceIndex, posX, posY, posZ, 0, 0.0, this.reusableVertex3) ? this.reusableVertex3 : null;
   }

   private int[] calculateGraphicsObjectFaceVertices(GraphicsObject graphicsObject, Model model, int faceIndex) {
      LocalPoint location = graphicsObject.getLocation();
      int posX = location.getX();
      int posZ = location.getY();
      int posY = graphicsObject.getZ();
      return this.calculateModelFaceVertices(model, faceIndex, posX, posY, posZ, 0, 0.0, this.reusableVertices9, this.reusableTempVertex) ? this.reusableVertices9 : null;
   }

   private void updateGraphicsObjectBoundParticles() {
      Iterator<Map.Entry<GraphicsObject, EntityParticleState>> it = this.graphicsObjectStates.entrySet().iterator();

      while(true) {
         while(it.hasNext()) {
            Map.Entry<GraphicsObject, EntityParticleState> entry = (Map.Entry)it.next();
            GraphicsObject graphicsObject = (GraphicsObject)entry.getKey();
            EntityParticleState state = (EntityParticleState)entry.getValue();
            if (graphicsObject.finished()) {
               Iterator var18 = state.emitters.iterator();

               while(var18.hasNext()) {
                  ParticleEmitter emitter = (ParticleEmitter)var18.next();
                  emitter.stop();
               }

               var18 = state.effectors.iterator();

               while(var18.hasNext()) {
                  ParticleEffector effector = (ParticleEffector)var18.next();
                  this.particleSystem.removeEffector(effector);
               }

               it.remove();
            } else {
               WorldView gfxWorldView = graphicsObject.getWorldView();
               boolean gfxRendered = gfxWorldView != null && !gfxWorldView.isTopLevel() || this.renderedEntitiesThisFrame.containsKey(graphicsObject);
               if (!gfxRendered) {
                  Iterator var21 = state.emitters.iterator();

                  while(var21.hasNext()) {
                     ParticleEmitter emitter = (ParticleEmitter)var21.next();
                     emitter.setActive(false);
                  }

                  var21 = state.effectors.iterator();

                  while(var21.hasNext()) {
                     ParticleEffector effector = (ParticleEffector)var21.next();
                     effector.setActive(false);
                  }
               } else {
                  Model model = graphicsObject.getModel();
                  if (model != null) {
                     WorldView worldView = graphicsObject.getWorldView();
                     if (worldView == null) {
                        worldView = this.client.getTopLevelWorldView();
                     }

                     int baseX = worldView.getBaseX();
                     int baseY = worldView.getBaseY();
                     int weYOffset = this.computeWorldEntityYOffset(worldView);
                     LocalPoint location = graphicsObject.getLocation();
                     WorldPoint worldPoint = WorldPoint.fromLocal(this.client, location);
                     Iterator var14 = state.emitters.iterator();

                     int faceIndex;
                     int[] pos;
                     while(var14.hasNext()) {
                        ParticleEmitter emitter = (ParticleEmitter)var14.next();
                        emitter.setWorldEntityYOffset(weYOffset);
                        if (emitter.isAttachedToFace()) {
                           faceIndex = emitter.getAttachedFaceIndex();
                           if (faceIndex >= model.getFaceCount()) {
                              emitter.setActive(false);
                           } else {
                              emitter.setActive(true);
                              pos = this.calculateGraphicsObjectFaceVertices(graphicsObject, model, faceIndex);
                              if (pos != null) {
                                 emitter.setSpawnArea(pos[0], pos[1], pos[2], pos[3], pos[4], pos[5], pos[6], pos[7], pos[8], this.particleSystem.getClientCycle());
                                 emitter.updatePosition((pos[0] + pos[3] + pos[6]) / 3, (pos[1] + pos[4] + pos[7]) / 3, (pos[2] + pos[5] + pos[8]) / 3, worldPoint, baseX, baseY);
                              }
                           }
                        }
                     }

                     var14 = state.effectors.iterator();

                     while(var14.hasNext()) {
                        ParticleEffector effector = (ParticleEffector)var14.next();
                        if (effector.isAttachedToFace()) {
                           faceIndex = effector.getAttachedFaceIndex();
                           if (faceIndex >= model.getFaceCount()) {
                              effector.setActive(false);
                           } else {
                              effector.setActive(true);
                              pos = this.calculateGraphicsObjectFaceCenter(graphicsObject, model, faceIndex);
                              if (pos != null) {
                                 effector.setPosition(pos[0], pos[1], pos[2]);
                              }
                           }
                        }
                     }
                  }
               }
            }
         }

         return;
      }
   }

   private void updateProjectileBoundParticles() {
      Iterator<Map.Entry<Projectile, EntityParticleState>> it = this.projectileStates.entrySet().iterator();

      while(true) {
         while(it.hasNext()) {
            Map.Entry<Projectile, EntityParticleState> entry = (Map.Entry)it.next();
            Projectile projectile = (Projectile)entry.getKey();
            EntityParticleState state = (EntityParticleState)entry.getValue();
            if (projectile.getRemainingCycles() <= 0) {
               Iterator var18 = state.emitters.iterator();

               while(var18.hasNext()) {
                  ParticleEmitter emitter = (ParticleEmitter)var18.next();
                  emitter.stop();
               }

               var18 = state.effectors.iterator();

               while(var18.hasNext()) {
                  ParticleEffector effector = (ParticleEffector)var18.next();
                  this.particleSystem.removeEffector(effector);
               }

               it.remove();
            } else {
               boolean projectileRendered = this.renderedEntitiesThisFrame.containsKey(projectile);
               if (!projectileRendered && !state.isOnWorldEntity()) {
                  Iterator var19 = state.emitters.iterator();

                  while(var19.hasNext()) {
                     ParticleEmitter emitter = (ParticleEmitter)var19.next();
                     emitter.setActive(false);
                  }

                  var19 = state.effectors.iterator();

                  while(var19.hasNext()) {
                     ParticleEffector effector = (ParticleEffector)var19.next();
                     effector.setActive(false);
                  }
               } else {
                  Model model = projectile.getModel();
                  if (model != null) {
                     WorldView worldView = this.client.getTopLevelWorldView();
                     int baseX = worldView.getBaseX();
                     int baseY = worldView.getBaseY();
                     int projX = (int)projectile.getX();
                     int projZ = (int)projectile.getY();
                     WorldPoint worldPoint = WorldPoint.fromLocal(this.client, projX, projZ, worldView.getPlane());
                     Iterator var13 = state.emitters.iterator();

                     int faceIndex;
                     while(var13.hasNext()) {
                        ParticleEmitter emitter = (ParticleEmitter)var13.next();
                        faceIndex = this.computeWorldEntityYOffset(emitter.getWorldView());
                        emitter.setWorldEntityYOffset(faceIndex);
                        if (emitter.isAttachedToFace()) {
                           int faceIndex = emitter.getAttachedFaceIndex();
                           if (faceIndex >= model.getFaceCount()) {
                              emitter.setActive(false);
                           } else {
                              emitter.setActive(true);
                              int[] vertices = this.calculateProjectileFaceVertices(projectile, model, faceIndex);
                              if (vertices != null) {
                                 emitter.setSpawnArea(vertices[0], vertices[1], vertices[2], vertices[3], vertices[4], vertices[5], vertices[6], vertices[7], vertices[8], this.particleSystem.getClientCycle());
                                 emitter.updatePosition((vertices[0] + vertices[3] + vertices[6]) / 3, (vertices[1] + vertices[4] + vertices[7]) / 3, (vertices[2] + vertices[5] + vertices[8]) / 3, worldPoint, baseX, baseY);
                              }
                           }
                        }
                     }

                     var13 = state.effectors.iterator();

                     while(var13.hasNext()) {
                        ParticleEffector effector = (ParticleEffector)var13.next();
                        if (effector.isAttachedToFace()) {
                           faceIndex = effector.getAttachedFaceIndex();
                           if (faceIndex >= model.getFaceCount()) {
                              effector.setActive(false);
                           } else {
                              effector.setActive(true);
                              int[] pos = this.calculateProjectileFaceCenter(projectile, model, faceIndex);
                              if (pos != null) {
                                 effector.setPosition(pos[0], pos[1], pos[2]);
                              }
                           }
                        }
                     }
                  }
               }
            }
         }

         return;
      }
   }

   private void scanPlayerModelForParticles(Player player) {
      if (player != null) {
         WorldView playerWorldView = player.getWorldView();
         EntityParticleState state = (EntityParticleState)this.playerStates.get(player);
         if (state != null && !state.emitters.isEmpty()) {
            WorldView existingWorldView = ((ParticleEmitter)state.emitters.get(0)).getWorldView();
            if (existingWorldView != null && existingWorldView.getId() != (playerWorldView != null ? playerWorldView.getId() : -1)) {
               Iterator var5 = state.emitters.iterator();

               while(var5.hasNext()) {
                  ParticleEmitter emitter = (ParticleEmitter)var5.next();
                  emitter.stop();
               }

               var5 = state.effectors.iterator();

               while(var5.hasNext()) {
                  ParticleEffector effector = (ParticleEffector)var5.next();
                  this.particleSystem.removeEffector(effector);
               }

               state.emitters.clear();
               state.effectors.clear();
            }
         }

         Model model = player.getModel();
         if (model != null) {
            if (model.getFaceCount() > 0) {
               byte[] transparencies = model.getFaceTransparencies();
               byte[] priorities = model.getFaceRenderPriorities();
               short[] textures = model.getFaceTextures();
               if (transparencies != null && priorities != null && textures != null) {
                  int faceCount = model.getFaceCount();
                  if (state == null) {
                     state = this.getOrCreatePlayerState(player);
                  }

                  state.lastFaceCount = faceCount;
                  String[] minimenu = player.minimenuStrings();
                  String colourStr = minimenu != null && minimenu.length >= 3 ? minimenu[2] : null;
                  int transparency;
                  int baseX;
                  int baseY;
                  Iterator var38;
                  ParticleEmitter emitter;
                  if (!Objects.equals(colourStr, state.lastColourString)) {
                     state.lastColourString = colourStr;
                     state.colourOverrides.clear();
                     if (colourStr != null) {
                        Matcher m = COLOUR_TAG_PATTERN.matcher(colourStr);

                        while(m.find()) {
                           transparency = Integer.parseInt(m.group(1));
                           baseX = parseHexColour(m.group(2));
                           baseY = parseHexColour(m.group(3));
                           int targetArgb = parseHexColour(m.group(4));
                           state.colourOverrides.put(transparency, new int[]{baseX, baseY, targetArgb});
                        }
                     }

                     var38 = state.emitters.iterator();

                     while(var38.hasNext()) {
                        emitter = (ParticleEmitter)var38.next();
                        baseX = emitter.getAttachedMarkerId();
                        if (baseX >= 0) {
                           ParticleEmitterConfig base = this.particleSystem.getEmitterConfig(baseX);
                           if (base != null) {
                              int[] override = (int[])state.colourOverrides.get(baseX);
                              emitter.setConfig(override != null ? base.copyWithColourOverride(override[0], override[1], override[2]) : base);
                           }
                        }
                     }
                  }

                  this.scanScratch.clear();
                  this.scanScratch.prepareFacePriorities(faceCount);

                  for(int faceIndex = 0; faceIndex < faceCount; ++faceIndex) {
                     transparency = transparencies[faceIndex] & 255;
                     baseX = priorities[faceIndex] & 255;
                     if (transparency >= 254 && baseX >= 252 && baseX <= 254) {
                        baseY = textures[faceIndex] & '\uffff';
                        this.scanScratch.setFacePriority(faceIndex, baseX);
                        if (baseX == 254) {
                           this.scanScratch.getOrCreateEmitterFaceList(baseY).add(faceIndex);
                        } else {
                           this.scanScratch.getOrCreateEffectorFaceList(baseY).add(faceIndex);
                        }
                     }
                  }

                  var38 = state.emitters.iterator();

                  while(var38.hasNext()) {
                     emitter = (ParticleEmitter)var38.next();
                     baseX = emitter.getAttachedMarkerId();
                     if (baseX >= 0) {
                        this.scanScratch.getOrCreateEmitterList(baseX).add(emitter);
                     }
                  }

                  var38 = state.effectors.iterator();

                  while(var38.hasNext()) {
                     ParticleEffector effector = (ParticleEffector)var38.next();
                     baseX = effector.getAttachedMarkerId();
                     if (baseX >= 0) {
                        this.scanScratch.getOrCreateEffectorList(baseX).add(effector);
                     }
                  }

                  WorldPoint playerWorldPoint = player.getWorldLocation();
                  WorldView worldView = player.getWorldView();
                  baseX = worldView.getBaseX();
                  baseY = worldView.getBaseY();
                  WorldEntity worldEntity = null;
                  if (!worldView.isTopLevel()) {
                     worldEntity = (WorldEntity)this.client.getTopLevelWorldView().worldEntities().byIndex(worldView.getId());
                  }

                  this.scanScratch.allEmitterMarkerIds.addAll(this.scanScratch.modelEmitterFaces.keySet());
                  this.scanScratch.allEmitterMarkerIds.addAll(this.scanScratch.existingEmittersByMarkerId.keySet());
                  Iterator var16 = this.scanScratch.allEmitterMarkerIds.iterator();

                  while(true) {
                     int markerId;
                     List modelFaces;
                     List existingForMarker;
                     int modelCount;
                     int existingCount;
                     int i;
                     int i;
                     int[] faceCenter;
                     int worldX;
                     int worldY;
                     int worldZ;
                     Iterator var47;
                     Iterator var49;
                     int faceIndex;
                     while(var16.hasNext()) {
                        markerId = (Integer)var16.next();
                        modelFaces = (List)this.scanScratch.modelEmitterFaces.getOrDefault(markerId, Collections.emptyList());
                        existingForMarker = (List)this.scanScratch.existingEmittersByMarkerId.getOrDefault(markerId, Collections.emptyList());
                        modelCount = modelFaces.size();
                        existingCount = existingForMarker.size();
                        if (modelCount > existingCount) {
                           this.scanScratch.claimedFaces.clear();
                           var47 = existingForMarker.iterator();

                           while(var47.hasNext()) {
                              ParticleEmitter emitter = (ParticleEmitter)var47.next();
                              this.scanScratch.claimedFaces.add(emitter.getAttachedFaceIndex());
                           }

                           i = modelCount - existingCount;
                           var49 = modelFaces.iterator();

                           while(var49.hasNext()) {
                              faceIndex = (Integer)var49.next();
                              if (i <= 0) {
                                 break;
                              }

                              if (!this.scanScratch.claimedFaces.contains(faceIndex)) {
                                 faceCenter = this.calculatePlayerFaceCenter(player, model, faceIndex);
                                 if (faceCenter != null) {
                                    worldX = faceCenter[0];
                                    worldY = faceCenter[1];
                                    worldZ = faceCenter[2];
                                    int[] colourOverride = (int[])state.colourOverrides.get(markerId);
                                    ParticleEmitter emitter;
                                    if (colourOverride != null) {
                                       emitter = this.particleSystem.createEmitterWithColourOverride(markerId, worldX, worldY, worldZ, playerWorldPoint, baseX, baseY, worldView, worldEntity, colourOverride[0], colourOverride[1], colourOverride[2]);
                                    } else {
                                       emitter = this.particleSystem.createEmitter(markerId, worldX, worldY, worldZ, playerWorldPoint, baseX, baseY, worldView, worldEntity);
                                    }

                                    if (emitter != null) {
                                       emitter.setPlane(player.getWorldLocation().getPlane());
                                       emitter.setAttachedFaceIndex(faceIndex);
                                       emitter.setAttachedMarkerId(markerId);
                                       int[] vertices = this.calculatePlayerFaceVertices(player, model, faceIndex);
                                       if (vertices != null) {
                                          emitter.setSpawnArea(vertices[0], vertices[1], vertices[2], vertices[3], vertices[4], vertices[5], vertices[6], vertices[7], vertices[8], this.particleSystem.getClientCycle());
                                       }

                                       this.scanScratch.newEmitters.add(emitter);
                                       this.scanScratch.claimedFaces.add(faceIndex);
                                       --i;
                                       log.debug("Created new player emitter {} at face {}", markerId, faceIndex);
                                    }
                                 }
                              }
                           }
                        } else if (modelCount < existingCount) {
                           i = existingCount - modelCount;

                           for(i = existingCount - 1; i >= 0 && i > 0; --i) {
                              ParticleEmitter emitter = (ParticleEmitter)existingForMarker.get(i);
                              emitter.stop();
                              emitter.setAttachedFaceIndex(-1);
                              emitter.setAttachedMarkerId(-1);
                              this.scanScratch.emittersToRemove.add(emitter);
                              --i;
                              log.debug("Detached player emitter {} (model has {} faces, had {} emitters)", new Object[]{markerId, modelCount, existingCount});
                           }
                        } else if (existingCount > 0) {
                           existingForMarker.sort(Comparator.comparingInt(ParticleEmitter::getAttachedFaceIndex));

                           for(i = 0; i < modelCount; ++i) {
                              ((ParticleEmitter)existingForMarker.get(i)).setAttachedFaceIndex((Integer)modelFaces.get(i));
                           }
                        }
                     }

                     this.scanScratch.allEffectorMarkerIds.addAll(this.scanScratch.modelEffectorFaces.keySet());
                     this.scanScratch.allEffectorMarkerIds.addAll(this.scanScratch.existingEffectorsByMarkerId.keySet());
                     var16 = this.scanScratch.allEffectorMarkerIds.iterator();

                     while(true) {
                        while(var16.hasNext()) {
                           markerId = (Integer)var16.next();
                           modelFaces = (List)this.scanScratch.modelEffectorFaces.getOrDefault(markerId, Collections.emptyList());
                           existingForMarker = (List)this.scanScratch.existingEffectorsByMarkerId.getOrDefault(markerId, Collections.emptyList());
                           modelCount = modelFaces.size();
                           existingCount = existingForMarker.size();
                           if (modelCount > existingCount) {
                              this.scanScratch.claimedFaces.clear();
                              var47 = existingForMarker.iterator();

                              while(var47.hasNext()) {
                                 ParticleEffector effector = (ParticleEffector)var47.next();
                                 this.scanScratch.claimedFaces.add(effector.getAttachedFaceIndex());
                              }

                              i = modelCount - existingCount;
                              var49 = modelFaces.iterator();

                              while(var49.hasNext()) {
                                 faceIndex = (Integer)var49.next();
                                 if (i <= 0) {
                                    break;
                                 }

                                 if (!this.scanScratch.claimedFaces.contains(faceIndex)) {
                                    faceCenter = this.calculatePlayerFaceCenter(player, model, faceIndex);
                                    if (faceCenter != null) {
                                       worldX = faceCenter[0];
                                       worldY = faceCenter[1];
                                       worldZ = faceCenter[2];
                                       int priority = this.scanScratch.getFacePriority(faceIndex, 253);
                                       boolean inheritDirection = priority == 252;
                                       ParticleEffector effector = this.particleSystem.createGlobalEffector(markerId, worldX, worldY, worldZ);
                                       if (effector != null) {
                                          effector.setAttachedFaceIndex(faceIndex);
                                          effector.setAttachedMarkerId(markerId);
                                          effector.setInheritDirection(inheritDirection);
                                          if (inheritDirection) {
                                             int[] vertices = this.calculatePlayerFaceVertices(player, model, faceIndex);
                                             if (vertices != null) {
                                                effector.setFaceNormal(vertices[0], vertices[1], vertices[2], vertices[3], vertices[4], vertices[5], vertices[6], vertices[7], vertices[8]);
                                             }
                                          }

                                          this.scanScratch.newEffectors.add(effector);
                                          this.scanScratch.claimedFaces.add(faceIndex);
                                          --i;
                                          log.debug("Created new player effector {} at face {} (inheritDirection={})", new Object[]{markerId, faceIndex, inheritDirection});
                                       }
                                    }
                                 }
                              }
                           } else if (modelCount < existingCount) {
                              i = existingCount - modelCount;

                              for(i = existingCount - 1; i >= 0 && i > 0; --i) {
                                 ParticleEffector effector = (ParticleEffector)existingForMarker.get(i);
                                 this.particleSystem.removeEffector(effector);
                                 this.scanScratch.effectorsToRemove.add(effector);
                                 --i;
                                 log.debug("Removed excess player effector {} (model has {} faces, had {} effectors)", new Object[]{markerId, modelCount, existingCount});
                              }
                           } else if (existingCount > 0) {
                              existingForMarker.sort(Comparator.comparingInt(ParticleEffector::getAttachedFaceIndex));

                              for(i = 0; i < modelCount; ++i) {
                                 ((ParticleEffector)existingForMarker.get(i)).setAttachedFaceIndex((Integer)modelFaces.get(i));
                              }
                           }
                        }

                        state.emitters.removeAll(this.scanScratch.emittersToRemove);
                        state.emitters.addAll(this.scanScratch.newEmitters);
                        state.effectors.removeAll(this.scanScratch.effectorsToRemove);
                        state.effectors.addAll(this.scanScratch.newEffectors);
                        if (state.isEmpty()) {
                           this.playerStates.remove(player);
                        }

                        return;
                     }
                  }
               }
            }
         }
      }
   }

   private void scanNpcModelForParticles(NPC npc) {
      if (npc != null) {
         WorldView npcWorldView = npc.getWorldView();
         EntityParticleState state = (EntityParticleState)this.npcStates.get(npc);
         if (state != null && !state.emitters.isEmpty()) {
            WorldView existingWorldView = ((ParticleEmitter)state.emitters.get(0)).getWorldView();
            if (existingWorldView != null && existingWorldView.getId() != (npcWorldView != null ? npcWorldView.getId() : -1)) {
               Iterator var5 = state.emitters.iterator();

               while(var5.hasNext()) {
                  ParticleEmitter emitter = (ParticleEmitter)var5.next();
                  emitter.stop();
               }

               var5 = state.effectors.iterator();

               while(var5.hasNext()) {
                  ParticleEffector effector = (ParticleEffector)var5.next();
                  this.particleSystem.removeEffector(effector);
               }

               state.emitters.clear();
               state.effectors.clear();
            }
         }

         Model model = npc.getModel();
         if (model != null) {
            if (model.getFaceCount() > 0) {
               byte[] transparencies = model.getFaceTransparencies();
               byte[] priorities = model.getFaceRenderPriorities();
               short[] textures = model.getFaceTextures();
               if (transparencies != null && priorities != null && textures != null) {
                  int faceCount = model.getFaceCount();
                  if (state == null) {
                     state = this.getOrCreateNpcState(npc);
                  }

                  this.scanScratch.clear();
                  this.scanScratch.prepareFacePriorities(faceCount);

                  int baseX;
                  int baseY;
                  for(int faceIndex = 0; faceIndex < faceCount; ++faceIndex) {
                     int transparency = transparencies[faceIndex] & 255;
                     baseX = priorities[faceIndex] & 255;
                     if (transparency >= 254 && baseX >= 252 && baseX <= 254) {
                        baseY = textures[faceIndex] & '\uffff';
                        this.scanScratch.setFacePriority(faceIndex, baseX);
                        if (baseX == 254) {
                           this.scanScratch.getOrCreateEmitterFaceList(baseY).add(faceIndex);
                        } else {
                           this.scanScratch.getOrCreateEffectorFaceList(baseY).add(faceIndex);
                        }
                     }
                  }

                  Iterator var36 = state.emitters.iterator();

                  while(var36.hasNext()) {
                     ParticleEmitter emitter = (ParticleEmitter)var36.next();
                     baseX = emitter.getAttachedMarkerId();
                     if (baseX >= 0) {
                        this.scanScratch.getOrCreateEmitterList(baseX).add(emitter);
                     }
                  }

                  var36 = state.effectors.iterator();

                  while(var36.hasNext()) {
                     ParticleEffector effector = (ParticleEffector)var36.next();
                     baseX = effector.getAttachedMarkerId();
                     if (baseX >= 0) {
                        this.scanScratch.getOrCreateEffectorList(baseX).add(effector);
                     }
                  }

                  WorldPoint npcWorldPoint = npc.getWorldLocation();
                  WorldView worldView = npc.getWorldView();
                  baseX = worldView.getBaseX();
                  baseY = worldView.getBaseY();
                  WorldEntity worldEntity = null;
                  if (!worldView.isTopLevel()) {
                     worldEntity = (WorldEntity)this.client.getTopLevelWorldView().worldEntities().byIndex(worldView.getId());
                  }

                  this.scanScratch.allEmitterMarkerIds.addAll(this.scanScratch.modelEmitterFaces.keySet());
                  this.scanScratch.allEmitterMarkerIds.addAll(this.scanScratch.existingEmittersByMarkerId.keySet());
                  Iterator var14 = this.scanScratch.allEmitterMarkerIds.iterator();

                  while(true) {
                     int markerId;
                     List modelFaces;
                     List existingForMarker;
                     int modelCount;
                     int existingCount;
                     int toCreate;
                     int i;
                     int[] faceCenter;
                     int worldX;
                     int worldY;
                     int worldZ;
                     Iterator var41;
                     Iterator var43;
                     int faceIndex;
                     while(var14.hasNext()) {
                        markerId = (Integer)var14.next();
                        modelFaces = (List)this.scanScratch.modelEmitterFaces.getOrDefault(markerId, Collections.emptyList());
                        existingForMarker = (List)this.scanScratch.existingEmittersByMarkerId.getOrDefault(markerId, Collections.emptyList());
                        modelCount = modelFaces.size();
                        existingCount = existingForMarker.size();
                        if (modelCount > existingCount) {
                           this.scanScratch.claimedFaces.clear();
                           var41 = existingForMarker.iterator();

                           while(var41.hasNext()) {
                              ParticleEmitter emitter = (ParticleEmitter)var41.next();
                              this.scanScratch.claimedFaces.add(emitter.getAttachedFaceIndex());
                           }

                           toCreate = modelCount - existingCount;
                           var43 = modelFaces.iterator();

                           while(var43.hasNext()) {
                              faceIndex = (Integer)var43.next();
                              if (toCreate <= 0) {
                                 break;
                              }

                              if (!this.scanScratch.claimedFaces.contains(faceIndex)) {
                                 faceCenter = this.calculateNpcFaceCenter(npc, model, faceIndex);
                                 if (faceCenter != null) {
                                    worldX = faceCenter[0];
                                    worldY = faceCenter[1];
                                    worldZ = faceCenter[2];
                                    ParticleEmitter emitter = this.particleSystem.createEmitter(markerId, worldX, worldY, worldZ, npcWorldPoint, baseX, baseY, worldView, worldEntity);
                                    if (emitter != null) {
                                       emitter.setPlane(npc.getWorldLocation().getPlane());
                                       emitter.setAttachedFaceIndex(faceIndex);
                                       emitter.setAttachedMarkerId(markerId);
                                       int[] vertices = this.calculateNpcFaceVertices(npc, model, faceIndex);
                                       if (vertices != null) {
                                          emitter.setSpawnArea(vertices[0], vertices[1], vertices[2], vertices[3], vertices[4], vertices[5], vertices[6], vertices[7], vertices[8], this.particleSystem.getClientCycle());
                                       }

                                       this.scanScratch.newEmitters.add(emitter);
                                       this.scanScratch.claimedFaces.add(faceIndex);
                                       --toCreate;
                                       log.debug("Created new NPC emitter {} at face {}", markerId, faceIndex);
                                    }
                                 }
                              }
                           }
                        } else if (modelCount < existingCount) {
                           toCreate = existingCount - modelCount;

                           for(i = existingCount - 1; i >= 0 && toCreate > 0; --i) {
                              ParticleEmitter emitter = (ParticleEmitter)existingForMarker.get(i);
                              emitter.stop();
                              emitter.setAttachedFaceIndex(-1);
                              emitter.setAttachedMarkerId(-1);
                              this.scanScratch.emittersToRemove.add(emitter);
                              --toCreate;
                              log.debug("Detached NPC emitter {} (model has {} faces, had {} emitters)", new Object[]{markerId, modelCount, existingCount});
                           }
                        }
                     }

                     this.scanScratch.allEffectorMarkerIds.addAll(this.scanScratch.modelEffectorFaces.keySet());
                     this.scanScratch.allEffectorMarkerIds.addAll(this.scanScratch.existingEffectorsByMarkerId.keySet());
                     var14 = this.scanScratch.allEffectorMarkerIds.iterator();

                     while(true) {
                        while(var14.hasNext()) {
                           markerId = (Integer)var14.next();
                           modelFaces = (List)this.scanScratch.modelEffectorFaces.getOrDefault(markerId, Collections.emptyList());
                           existingForMarker = (List)this.scanScratch.existingEffectorsByMarkerId.getOrDefault(markerId, Collections.emptyList());
                           modelCount = modelFaces.size();
                           existingCount = existingForMarker.size();
                           if (modelCount > existingCount) {
                              this.scanScratch.claimedFaces.clear();
                              var41 = existingForMarker.iterator();

                              while(var41.hasNext()) {
                                 ParticleEffector effector = (ParticleEffector)var41.next();
                                 this.scanScratch.claimedFaces.add(effector.getAttachedFaceIndex());
                              }

                              toCreate = modelCount - existingCount;
                              var43 = modelFaces.iterator();

                              while(var43.hasNext()) {
                                 faceIndex = (Integer)var43.next();
                                 if (toCreate <= 0) {
                                    break;
                                 }

                                 if (!this.scanScratch.claimedFaces.contains(faceIndex)) {
                                    faceCenter = this.calculateNpcFaceCenter(npc, model, faceIndex);
                                    if (faceCenter != null) {
                                       worldX = faceCenter[0];
                                       worldY = faceCenter[1];
                                       worldZ = faceCenter[2];
                                       int priority = this.scanScratch.getFacePriority(faceIndex, 253);
                                       boolean inheritDirection = priority == 252;
                                       ParticleEffector effector = this.particleSystem.createGlobalEffector(markerId, worldX, worldY, worldZ);
                                       if (effector != null) {
                                          effector.setAttachedFaceIndex(faceIndex);
                                          effector.setAttachedMarkerId(markerId);
                                          effector.setInheritDirection(inheritDirection);
                                          if (inheritDirection) {
                                             int[] vertices = this.calculateNpcFaceVertices(npc, model, faceIndex);
                                             if (vertices != null) {
                                                effector.setFaceNormal(vertices[0], vertices[1], vertices[2], vertices[3], vertices[4], vertices[5], vertices[6], vertices[7], vertices[8]);
                                             }
                                          }

                                          this.scanScratch.newEffectors.add(effector);
                                          this.scanScratch.claimedFaces.add(faceIndex);
                                          --toCreate;
                                          log.debug("Created new NPC effector {} at face {} (inheritDirection={})", new Object[]{markerId, faceIndex, inheritDirection});
                                       }
                                    }
                                 }
                              }
                           } else if (modelCount < existingCount) {
                              toCreate = existingCount - modelCount;

                              for(i = existingCount - 1; i >= 0 && toCreate > 0; --i) {
                                 ParticleEffector effector = (ParticleEffector)existingForMarker.get(i);
                                 this.particleSystem.removeEffector(effector);
                                 this.scanScratch.effectorsToRemove.add(effector);
                                 --toCreate;
                                 log.debug("Removed excess NPC effector {} (model has {} faces, had {} effectors)", new Object[]{markerId, modelCount, existingCount});
                              }
                           }
                        }

                        state.emitters.removeAll(this.scanScratch.emittersToRemove);
                        state.emitters.addAll(this.scanScratch.newEmitters);
                        state.effectors.removeAll(this.scanScratch.effectorsToRemove);
                        state.effectors.addAll(this.scanScratch.newEffectors);
                        if (state.isEmpty()) {
                           this.npcStates.remove(npc);
                        }

                        return;
                     }
                  }
               }
            }
         }
      }
   }

   private int[] calculatePlayerFaceVertices(Player player, Model model, int faceIndex) {
      LocalPoint localLocation = player.getLocalLocation();
      int posX = localLocation.getX();
      int posZ = localLocation.getY();
      int plane = player.getWorldLocation().getPlane();
      int posY = Perspective.getFootprintTileHeight(this.client, localLocation, plane, player.getFootprintSize()) - player.getAnimationHeightOffset();
      int yawOrientation = player.getCurrentOrientation();
      return this.calculateModelFaceVertices(model, faceIndex, posX, posY, posZ, yawOrientation, 0.0, this.reusableVertices9, this.reusableTempVertex) ? this.reusableVertices9 : null;
   }

   private int[] calculatePlayerFaceCenter(Player player, Model model, int faceIndex) {
      LocalPoint localLocation = player.getLocalLocation();
      int posX = localLocation.getX();
      int posZ = localLocation.getY();
      int plane = player.getWorldLocation().getPlane();
      int posY = Perspective.getFootprintTileHeight(this.client, localLocation, plane, player.getFootprintSize()) - player.getAnimationHeightOffset();
      int yawOrientation = player.getCurrentOrientation();
      return this.calculateModelFaceCenter(model, faceIndex, posX, posY, posZ, yawOrientation, 0.0, this.reusableVertex3) ? this.reusableVertex3 : null;
   }

   private int[] calculateNpcFaceVertices(NPC npc, Model model, int faceIndex) {
      LocalPoint localLocation = npc.getLocalLocation();
      int posX = localLocation.getX();
      int posZ = localLocation.getY();
      int plane = npc.getWorldLocation().getPlane();
      int posY = Perspective.getFootprintTileHeight(this.client, localLocation, plane, npc.getComposition().getFootprintSize()) - npc.getAnimationHeightOffset();
      int yawOrientation = npc.getCurrentOrientation();
      return this.calculateModelFaceVertices(model, faceIndex, posX, posY, posZ, yawOrientation, 0.0, this.reusableVertices9, this.reusableTempVertex) ? this.reusableVertices9 : null;
   }

   private int[] calculateNpcFaceCenter(NPC npc, Model model, int faceIndex) {
      LocalPoint localLocation = npc.getLocalLocation();
      int posX = localLocation.getX();
      int posZ = localLocation.getY();
      int plane = npc.getWorldLocation().getPlane();
      int posY = Perspective.getFootprintTileHeight(this.client, localLocation, plane, npc.getComposition().getFootprintSize()) - npc.getAnimationHeightOffset();
      int yawOrientation = npc.getCurrentOrientation();
      return this.calculateModelFaceCenter(model, faceIndex, posX, posY, posZ, yawOrientation, 0.0, this.reusableVertex3) ? this.reusableVertex3 : null;
   }

   private void checkPlayerModelFaceCountChanges() {
      Player localPlayer = this.client.getLocalPlayer();
      if (localPlayer != null) {
         this.checkPlayerFaceCount(localPlayer);
         Iterator var2 = this.playerStates.keySet().iterator();

         while(var2.hasNext()) {
            Player player = (Player)var2.next();
            if (player != localPlayer) {
               this.checkPlayerFaceCount(player);
            }
         }

      }
   }

   private void checkPlayerFaceCount(Player player) {
      if (!this.hasWhitelistedAppearance(player)) {
         EntityParticleState state = (EntityParticleState)this.playerStates.get(player);
         if (state != null) {
            state.lastFaceCount = -1;
         }

      } else {
         Model model = player.getModel();
         if (model != null) {
            int currentFaceCount = model.getFaceCount();
            EntityParticleState state = (EntityParticleState)this.playerStates.get(player);
            if (state == null) {
               state = this.getOrCreatePlayerState(player);
               state.lastFaceCount = currentFaceCount;
            } else if (state.lastFaceCount < 0) {
               state.lastFaceCount = currentFaceCount;
            } else if (currentFaceCount != state.lastFaceCount) {
               state.lastFaceCount = currentFaceCount;
               this.scanPlayerModelForParticles(player);
            }

         }
      }
   }

   private int computeWorldEntityYOffset(WorldView worldView) {
      if (worldView != null && !worldView.isTopLevel()) {
         WorldEntity we = (WorldEntity)this.client.getTopLevelWorldView().worldEntities().byIndex(worldView.getId());
         if (we == null) {
            return 0;
         } else {
            LocalPoint entityLoc = we.getLocalLocation();
            return entityLoc == null ? 0 : Perspective.getTileHeight(this.client, entityLoc, this.client.getTopLevelWorldView().getPlane());
         }
      } else {
         return 0;
      }
   }

   private void updatePlayerBoundParticles() {
      Iterator<Map.Entry<Player, EntityParticleState>> it = this.playerStates.entrySet().iterator();

      while(true) {
         label167:
         while(it.hasNext()) {
            Map.Entry<Player, EntityParticleState> entry = (Map.Entry)it.next();
            Player player = (Player)entry.getKey();
            EntityParticleState state = (EntityParticleState)entry.getValue();
            if (player.getName() == null) {
               Iterator var20 = state.emitters.iterator();

               while(var20.hasNext()) {
                  ParticleEmitter emitter = (ParticleEmitter)var20.next();
                  emitter.stop();
                  this.particleSystem.removeEmitter(emitter);
               }

               var20 = state.effectors.iterator();

               while(var20.hasNext()) {
                  ParticleEffector effector = (ParticleEffector)var20.next();
                  this.particleSystem.removeEffector(effector);
               }

               it.remove();
            } else {
               WorldView worldView = player.getWorldView();
               boolean playerRendered = worldView != null && !worldView.isTopLevel() || this.renderedEntitiesThisFrame.containsKey(player);
               if (!playerRendered) {
                  Iterator var23 = state.emitters.iterator();

                  while(var23.hasNext()) {
                     ParticleEmitter emitter = (ParticleEmitter)var23.next();
                     emitter.setActive(false);
                  }

                  var23 = state.effectors.iterator();

                  while(var23.hasNext()) {
                     ParticleEffector effector = (ParticleEffector)var23.next();
                     effector.setActive(false);
                  }
               } else {
                  int baseX = worldView.getBaseX();
                  int baseY = worldView.getBaseY();
                  int weYOffset = this.computeWorldEntityYOffset(worldView);
                  Model model = player.getModel();
                  WorldPoint playerWorldPoint = player.getWorldLocation();
                  Iterator var12 = state.emitters.iterator();

                  while(true) {
                     ParticleEmitter emitter;
                     int faceIndex;
                     boolean faceValid;
                     int markerId;
                     int ordinal;
                     Iterator var18;
                     int[] vertices;
                     int newFaceIndex;
                     do {
                        do {
                           if (!var12.hasNext()) {
                              var12 = state.effectors.iterator();

                              while(true) {
                                 ParticleEffector effector;
                                 do {
                                    do {
                                       if (!var12.hasNext()) {
                                          continue label167;
                                       }

                                       effector = (ParticleEffector)var12.next();
                                    } while(!effector.isAttachedToFace());
                                 } while(model == null);

                                 faceIndex = effector.getAttachedFaceIndex();
                                 faceValid = false;
                                 if (faceIndex < model.getFaceCount() && this.isFaceStillValidMarker(model, faceIndex)) {
                                    faceValid = true;
                                 } else {
                                    markerId = effector.getAttachedMarkerId();
                                    if (markerId >= 0) {
                                       ordinal = 0;
                                       var18 = state.effectors.iterator();

                                       while(var18.hasNext()) {
                                          ParticleEffector sibling = (ParticleEffector)var18.next();
                                          if (sibling != effector && sibling.getAttachedMarkerId() == markerId && sibling.getAttachedFaceIndex() < effector.getAttachedFaceIndex()) {
                                             ++ordinal;
                                          }
                                       }

                                       newFaceIndex = this.findMarkerFaceByTextureId(model, markerId, ordinal);
                                       if (newFaceIndex >= 0) {
                                          effector.setAttachedFaceIndex(newFaceIndex);
                                          faceIndex = newFaceIndex;
                                          faceValid = true;
                                       }
                                    }
                                 }

                                 if (!faceValid) {
                                    effector.setActive(false);
                                 } else {
                                    effector.setActive(true);
                                    vertices = this.calculatePlayerFaceCenter(player, model, faceIndex);
                                    if (vertices != null) {
                                       effector.setPosition(vertices[0], vertices[1], vertices[2]);
                                    }
                                 }
                              }
                           }

                           emitter = (ParticleEmitter)var12.next();
                           emitter.setWorldEntityYOffset(weYOffset);
                        } while(!emitter.isAttachedToFace());
                     } while(model == null);

                     faceIndex = emitter.getAttachedFaceIndex();
                     faceValid = false;
                     if (faceIndex < model.getFaceCount() && this.isFaceStillValidMarker(model, faceIndex)) {
                        faceValid = true;
                     } else {
                        markerId = emitter.getAttachedMarkerId();
                        if (markerId >= 0) {
                           ordinal = 0;
                           var18 = state.emitters.iterator();

                           while(var18.hasNext()) {
                              ParticleEmitter sibling = (ParticleEmitter)var18.next();
                              if (sibling != emitter && sibling.getAttachedMarkerId() == markerId && sibling.getAttachedFaceIndex() < emitter.getAttachedFaceIndex()) {
                                 ++ordinal;
                              }
                           }

                           newFaceIndex = this.findMarkerFaceByTextureId(model, markerId, ordinal);
                           if (newFaceIndex >= 0) {
                              emitter.setAttachedFaceIndex(newFaceIndex);
                              faceIndex = newFaceIndex;
                              faceValid = true;
                           }
                        }
                     }

                     if (!faceValid) {
                        emitter.setActive(false);
                     } else {
                        if (!emitter.isActive()) {
                           emitter.resetStaleTimer(this.particleSystem.getClientCycle());
                        }

                        emitter.setActive(true);
                        emitter.setPlane(playerWorldPoint.getPlane());
                        vertices = this.calculatePlayerFaceVertices(player, model, faceIndex);
                        if (vertices != null) {
                           if (isFaceCollapsed(vertices)) {
                              emitter.setActive(false);
                           } else {
                              emitter.setSpawnArea(vertices[0], vertices[1], vertices[2], vertices[3], vertices[4], vertices[5], vertices[6], vertices[7], vertices[8], this.particleSystem.getClientCycle());
                              emitter.updatePosition((vertices[0] + vertices[3] + vertices[6]) / 3, (vertices[1] + vertices[4] + vertices[7]) / 3, (vertices[2] + vertices[5] + vertices[8]) / 3, playerWorldPoint, baseX, baseY);
                           }
                        }
                     }
                  }
               }
            }
         }

         return;
      }
   }

   private void updateNpcBoundParticles() {
      Iterator<Map.Entry<NPC, EntityParticleState>> it = this.npcStates.entrySet().iterator();

      while(true) {
         label167:
         while(it.hasNext()) {
            Map.Entry<NPC, EntityParticleState> entry = (Map.Entry)it.next();
            NPC npc = (NPC)entry.getKey();
            EntityParticleState state = (EntityParticleState)entry.getValue();
            if (npc.getName() == null) {
               Iterator var20 = state.emitters.iterator();

               while(var20.hasNext()) {
                  ParticleEmitter emitter = (ParticleEmitter)var20.next();
                  emitter.stop();
                  this.particleSystem.removeEmitter(emitter);
               }

               var20 = state.effectors.iterator();

               while(var20.hasNext()) {
                  ParticleEffector effector = (ParticleEffector)var20.next();
                  this.particleSystem.removeEffector(effector);
               }

               state.clear();
               it.remove();
            } else {
               WorldView worldView = npc.getWorldView();
               boolean npcRendered = worldView != null && !worldView.isTopLevel() || this.renderedEntitiesThisFrame.containsKey(npc);
               if (!npcRendered) {
                  Iterator var23 = state.emitters.iterator();

                  while(var23.hasNext()) {
                     ParticleEmitter emitter = (ParticleEmitter)var23.next();
                     emitter.setActive(false);
                  }

                  var23 = state.effectors.iterator();

                  while(var23.hasNext()) {
                     ParticleEffector effector = (ParticleEffector)var23.next();
                     effector.setActive(false);
                  }
               } else {
                  int baseX = worldView.getBaseX();
                  int baseY = worldView.getBaseY();
                  int weYOffset = this.computeWorldEntityYOffset(worldView);
                  Model model = npc.getModel();
                  WorldPoint npcWorldPoint = npc.getWorldLocation();
                  Iterator var12 = state.emitters.iterator();

                  while(true) {
                     ParticleEmitter emitter;
                     int faceIndex;
                     boolean faceValid;
                     int markerId;
                     int ordinal;
                     Iterator var18;
                     int[] vertices;
                     int newFaceIndex;
                     do {
                        do {
                           if (!var12.hasNext()) {
                              var12 = state.effectors.iterator();

                              while(true) {
                                 ParticleEffector effector;
                                 do {
                                    do {
                                       if (!var12.hasNext()) {
                                          continue label167;
                                       }

                                       effector = (ParticleEffector)var12.next();
                                    } while(!effector.isAttachedToFace());
                                 } while(model == null);

                                 faceIndex = effector.getAttachedFaceIndex();
                                 faceValid = false;
                                 if (faceIndex < model.getFaceCount() && this.isFaceStillValidMarker(model, faceIndex)) {
                                    faceValid = true;
                                 } else {
                                    markerId = effector.getAttachedMarkerId();
                                    if (markerId >= 0) {
                                       ordinal = 0;
                                       var18 = state.effectors.iterator();

                                       while(var18.hasNext()) {
                                          ParticleEffector sibling = (ParticleEffector)var18.next();
                                          if (sibling != effector && sibling.getAttachedMarkerId() == markerId && sibling.getAttachedFaceIndex() < effector.getAttachedFaceIndex()) {
                                             ++ordinal;
                                          }
                                       }

                                       newFaceIndex = this.findMarkerFaceByTextureId(model, markerId, ordinal);
                                       if (newFaceIndex >= 0) {
                                          effector.setAttachedFaceIndex(newFaceIndex);
                                          faceIndex = newFaceIndex;
                                          faceValid = true;
                                       }
                                    }
                                 }

                                 if (!faceValid) {
                                    effector.setActive(false);
                                 } else {
                                    effector.setActive(true);
                                    vertices = this.calculateNpcFaceCenter(npc, model, faceIndex);
                                    if (vertices != null) {
                                       effector.setPosition(vertices[0], vertices[1], vertices[2]);
                                    }
                                 }
                              }
                           }

                           emitter = (ParticleEmitter)var12.next();
                           emitter.setWorldEntityYOffset(weYOffset);
                        } while(!emitter.isAttachedToFace());
                     } while(model == null);

                     faceIndex = emitter.getAttachedFaceIndex();
                     faceValid = false;
                     if (faceIndex < model.getFaceCount() && this.isFaceStillValidMarker(model, faceIndex)) {
                        faceValid = true;
                     } else {
                        markerId = emitter.getAttachedMarkerId();
                        if (markerId >= 0) {
                           ordinal = 0;
                           var18 = state.emitters.iterator();

                           while(var18.hasNext()) {
                              ParticleEmitter sibling = (ParticleEmitter)var18.next();
                              if (sibling != emitter && sibling.getAttachedMarkerId() == markerId && sibling.getAttachedFaceIndex() < emitter.getAttachedFaceIndex()) {
                                 ++ordinal;
                              }
                           }

                           newFaceIndex = this.findMarkerFaceByTextureId(model, markerId, ordinal);
                           if (newFaceIndex >= 0) {
                              emitter.setAttachedFaceIndex(newFaceIndex);
                              faceIndex = newFaceIndex;
                              faceValid = true;
                           }
                        }
                     }

                     if (!faceValid) {
                        emitter.setActive(false);
                     } else {
                        if (!emitter.isActive()) {
                           emitter.resetStaleTimer(this.particleSystem.getClientCycle());
                        }

                        emitter.setActive(true);
                        emitter.setPlane(npcWorldPoint.getPlane());
                        vertices = this.calculateNpcFaceVertices(npc, model, faceIndex);
                        if (vertices != null) {
                           if (isFaceCollapsed(vertices)) {
                              emitter.setActive(false);
                           } else {
                              emitter.setSpawnArea(vertices[0], vertices[1], vertices[2], vertices[3], vertices[4], vertices[5], vertices[6], vertices[7], vertices[8], this.particleSystem.getClientCycle());
                              emitter.updatePosition((vertices[0] + vertices[3] + vertices[6]) / 3, (vertices[1] + vertices[4] + vertices[7]) / 3, (vertices[2] + vertices[5] + vertices[8]) / 3, npcWorldPoint, baseX, baseY);
                           }
                        }
                     }
                  }
               }
            }
         }

         return;
      }
   }

   private boolean isFaceStillValidMarker(Model model, int faceIndex) {
      byte[] transparencies = model.getFaceTransparencies();
      byte[] priorities = model.getFaceRenderPriorities();
      if (transparencies != null && priorities != null) {
         if (faceIndex < transparencies.length && faceIndex < priorities.length) {
            int transparency = transparencies[faceIndex] & 255;
            int priority = priorities[faceIndex] & 255;
            return transparency >= 254 && priority >= 252 && priority <= 254;
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   private static boolean isFaceCollapsed(int[] vertices) {
      int e1x = vertices[3] - vertices[0];
      int e1y = vertices[4] - vertices[1];
      int e1z = vertices[5] - vertices[2];
      if (e1x != 0 || e1y != 0 || e1z != 0) {
         int e2x = vertices[6] - vertices[0];
         int e2y = vertices[7] - vertices[1];
         int e2z = vertices[8] - vertices[2];
         if (e2x != 0 || e2y != 0 || e2z != 0) {
            int cx = e1y * e2z - e1z * e2y;
            if (cx != 0) {
               return false;
            } else {
               int cy = e1z * e2x - e1x * e2z;
               if (cy != 0) {
                  return false;
               } else {
                  return e1x * e2y - e1y * e2x == 0;
               }
            }
         }
      }

      return true;
   }

   private int findMarkerFaceByTextureId(Model model, int markerId) {
      return this.findMarkerFaceByTextureId(model, markerId, 0);
   }

   private int findMarkerFaceByTextureId(Model model, int markerId, int skip) {
      byte[] transparencies = model.getFaceTransparencies();
      byte[] priorities = model.getFaceRenderPriorities();
      short[] textures = model.getFaceTextures();
      if (transparencies != null && priorities != null && textures != null) {
         int found = 0;
         int faceCount = model.getFaceCount();

         for(int i = 0; i < faceCount; ++i) {
            int transparency = transparencies[i] & 255;
            int priority = priorities[i] & 255;
            if (transparency >= 254 && priority >= 252 && priority <= 254) {
               int textureId = textures[i] & '\uffff';
               if (textureId == markerId) {
                  if (found == skip) {
                     return i;
                  }

                  ++found;
               }
            }
         }

         return -1;
      } else {
         return -1;
      }
   }

   private boolean calculateModelFaceCenter(Model model, int faceIndex, int posX, int posY, int posZ, int yawOrientation, double pitch, int[] out) {
      float[] verticesX = model.getVerticesX();
      float[] verticesY = model.getVerticesY();
      float[] verticesZ = model.getVerticesZ();
      int[] faceIndices1 = model.getFaceIndices1();
      int[] faceIndices2 = model.getFaceIndices2();
      int[] faceIndices3 = model.getFaceIndices3();
      if (faceIndex >= model.getFaceCount()) {
         return false;
      } else {
         int vi1 = faceIndices1[faceIndex];
         int vi2 = faceIndices2[faceIndex];
         int vi3 = faceIndices3[faceIndex];
         int vertexCount = model.getVerticesCount();
         if (vi1 < vertexCount && vi2 < vertexCount && vi3 < vertexCount) {
            float modelX = (verticesX[vi1] + verticesX[vi2] + verticesX[vi3]) / 3.0F;
            float modelY = (verticesY[vi1] + verticesY[vi2] + verticesY[vi3]) / 3.0F;
            float modelZ = (verticesZ[vi1] + verticesZ[vi2] + verticesZ[vi3]) / 3.0F;
            int yawIndex = yawOrientation << 3 & 16383;
            int cosYaw = ParticleEmitter.COSINE_TABLE[yawIndex];
            int sinYaw = -ParticleEmitter.SINE_TABLE[yawIndex];
            this.applyRotationToVertex(modelX, modelY, modelZ, posX, posY, posZ, cosYaw, sinYaw, pitch, out);
            return true;
         } else {
            return false;
         }
      }
   }

   private boolean calculateModelFaceVertices(Model model, int faceIndex, int posX, int posY, int posZ, int yawOrientation, double pitch, int[] out, int[] tempVertex) {
      float[] verticesX = model.getVerticesX();
      float[] verticesY = model.getVerticesY();
      float[] verticesZ = model.getVerticesZ();
      int[] faceIndices1 = model.getFaceIndices1();
      int[] faceIndices2 = model.getFaceIndices2();
      int[] faceIndices3 = model.getFaceIndices3();
      if (faceIndex >= model.getFaceCount()) {
         return false;
      } else {
         int vi1 = faceIndices1[faceIndex];
         int vi2 = faceIndices2[faceIndex];
         int vi3 = faceIndices3[faceIndex];
         int vertexCount = model.getVerticesCount();
         if (vi1 < vertexCount && vi2 < vertexCount && vi3 < vertexCount) {
            int yawIndex = yawOrientation << 3 & 16383;
            int cosYaw = ParticleEmitter.COSINE_TABLE[yawIndex];
            int sinYaw = -ParticleEmitter.SINE_TABLE[yawIndex];
            this.applyRotationToVertex(verticesX[vi1], verticesY[vi1], verticesZ[vi1], posX, posY, posZ, cosYaw, sinYaw, pitch, tempVertex);
            out[0] = tempVertex[0];
            out[1] = tempVertex[1];
            out[2] = tempVertex[2];
            this.applyRotationToVertex(verticesX[vi2], verticesY[vi2], verticesZ[vi2], posX, posY, posZ, cosYaw, sinYaw, pitch, tempVertex);
            out[3] = tempVertex[0];
            out[4] = tempVertex[1];
            out[5] = tempVertex[2];
            this.applyRotationToVertex(verticesX[vi3], verticesY[vi3], verticesZ[vi3], posX, posY, posZ, cosYaw, sinYaw, pitch, tempVertex);
            out[6] = tempVertex[0];
            out[7] = tempVertex[1];
            out[8] = tempVertex[2];
            return true;
         } else {
            return false;
         }
      }
   }

   private void applyRotationToVertex(float modelX, float modelY, float modelZ, int posX, int posY, int posZ, int cosYaw, int sinYaw, double pitch, int[] out) {
      int rotatedX = (int)((long)(modelX * (float)cosYaw) - (long)(modelZ * (float)sinYaw) >> 14);
      int rotatedZ = (int)((long)(modelX * (float)sinYaw) + (long)(modelZ * (float)cosYaw) >> 14);
      int rotatedY = (int)modelY;
      if (pitch != 0.0) {
         double cosPitch = Math.cos(pitch);
         double sinPitch = Math.sin(pitch);
         int finalY = (int)((double)rotatedY * cosPitch - (double)rotatedZ * sinPitch);
         int finalZ = (int)((double)rotatedY * sinPitch + (double)rotatedZ * cosPitch);
         rotatedY = finalY;
         rotatedZ = finalZ;
      }

      out[0] = posX + rotatedX;
      out[1] = posY + rotatedY;
      out[2] = posZ + rotatedZ;
   }

   public ParticlePluginConfig getPluginConfig() {
      return this.config;
   }

   private BufferedImage createSnowflakeIcon() {
      int size = 16;
      BufferedImage icon = new BufferedImage(size, size, 2);
      Graphics2D g = icon.createGraphics();
      g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
      int cx = size / 2;
      int cy = size / 2;
      g.setColor(new Color(135, 206, 250));
      g.setStroke(new BasicStroke(1.5F));

      for(int i = 0; i < 6; ++i) {
         double angle = Math.toRadians((double)(i * 60));
         int x2 = cx + (int)(6.0 * Math.cos(angle));
         int y2 = cy + (int)(6.0 * Math.sin(angle));
         g.drawLine(cx, cy, x2, y2);
         int mx = cx + (int)(4.0 * Math.cos(angle));
         int my = cy + (int)(4.0 * Math.sin(angle));
         double branchAngle1 = angle + Math.toRadians(45.0);
         double branchAngle2 = angle - Math.toRadians(45.0);
         int bx1 = mx + (int)(2.0 * Math.cos(branchAngle1));
         int by1 = my + (int)(2.0 * Math.sin(branchAngle1));
         int bx2 = mx + (int)(2.0 * Math.cos(branchAngle2));
         int by2 = my + (int)(2.0 * Math.sin(branchAngle2));
         g.drawLine(mx, my, bx1, by1);
         g.drawLine(mx, my, bx2, by2);
      }

      g.fillOval(cx - 1, cy - 1, 3, 3);
      g.dispose();
      return icon;
   }

   private void applyRenderTimeLocalSpaceShifts() {
      if (this.client.getGameState() == GameState.LOGGED_IN) {
         this.particleSystem.awaitPhysics();
         Iterator var1;
         Map.Entry entry;
         Model model;
         EntityParticleState state;
         Iterator var6;
         ParticleEmitter emitter;
         int faceIndex;
         int[] center;
         if (this.config.playerParticles()) {
            var1 = this.playerStates.entrySet().iterator();

            label94:
            while(true) {
               Player player;
               do {
                  do {
                     if (!var1.hasNext()) {
                        break label94;
                     }

                     entry = (Map.Entry)var1.next();
                     player = (Player)entry.getKey();
                  } while(player.getName() == null);

                  model = player.getModel();
               } while(model == null);

               state = (EntityParticleState)entry.getValue();
               var6 = state.emitters.iterator();

               while(var6.hasNext()) {
                  emitter = (ParticleEmitter)var6.next();
                  if (emitter.isAttachedToFace() && emitter.getConfig().isLocalSpace()) {
                     faceIndex = emitter.getAttachedFaceIndex();
                     if (faceIndex >= 0 && faceIndex < model.getFaceCount()) {
                        center = this.calculatePlayerFaceCenter(player, model, faceIndex);
                        if (center != null) {
                           emitter.renderTimeShift(center[0], center[1], center[2]);
                        }
                     }
                  }
               }
            }
         }

         if (this.config.npcParticles()) {
            var1 = this.npcStates.entrySet().iterator();

            while(true) {
               NPC npc;
               do {
                  do {
                     if (!var1.hasNext()) {
                        return;
                     }

                     entry = (Map.Entry)var1.next();
                     npc = (NPC)entry.getKey();
                  } while(npc.getName() == null);

                  model = npc.getModel();
               } while(model == null);

               state = (EntityParticleState)entry.getValue();
               var6 = state.emitters.iterator();

               while(var6.hasNext()) {
                  emitter = (ParticleEmitter)var6.next();
                  if (emitter.isAttachedToFace() && emitter.getConfig().isLocalSpace()) {
                     faceIndex = emitter.getAttachedFaceIndex();
                     if (faceIndex >= 0 && faceIndex < model.getFaceCount()) {
                        center = this.calculateNpcFaceCenter(npc, model, faceIndex);
                        if (center != null) {
                           emitter.renderTimeShift(center[0], center[1], center[2]);
                        }
                     }
                  }
               }
            }
         }
      }
   }

   public int getRequiredBufferSize() {
      return this.config.enabled() && this.particleSystem != null && this.particle3DRenderer != null ? this.particle3DRenderer.getRequiredBufferSize(this.particleSystem.getTotalParticleCount()) : 0;
   }

   public int renderParticlesToBuffer(IntBuffer buffer, float cameraYaw, float cameraPitch, int cameraX, int cameraY, int cameraZ, float baseSize, int drawDistance) {
      if (this.config.enabled() && this.particleSystem != null && this.particle3DRenderer != null) {
         this.applyRenderTimeLocalSpaceShifts();
         this.particleSystem.setGpuDrawDistanceTiles(drawDistance);
         if (this.transparencyRenderer != null && this.transparencyRenderer.isDepthBinnedModeActive()) {
            return this.renderParticlesInFrontOfAlpha(buffer, cameraYaw, cameraPitch, cameraX, cameraY, cameraZ, baseSize);
         } else {
            if (this.particleTextureManager != null && this.particleTextureManager.hasPendingUploads()) {
               this.particleTextureManager.uploadPendingTextures();
            }

            this.particle3DRenderer.updateCamera(cameraYaw, cameraPitch, cameraX, cameraY, cameraZ, this.client.getScale(), this.client.getViewportWidth() / 2, this.client.getViewportHeight() / 2);
            return this.particle3DRenderer.render(buffer, this.particleSystem, baseSize, 0, 0);
         }
      } else {
         return 0;
      }
   }

   public float getParticleSize() {
      return (float)this.config.particleSize();
   }

   public int renderParticlesBehindAlpha(IntBuffer buffer, float cameraYaw, float cameraPitch, int cameraX, int cameraY, int cameraZ, float baseSize) {
      if (this.config.enabled() && this.particleSystem != null && this.particle3DRenderer != null && this.transparencyRenderer != null) {
         if (this.particleTextureManager != null && this.particleTextureManager.hasPendingUploads()) {
            this.particleTextureManager.uploadPendingTextures();
         }

         this.particle3DRenderer.updateCamera(cameraYaw, cameraPitch, cameraX, cameraY, cameraZ, this.client.getScale(), this.client.getViewportWidth() / 2, this.client.getViewportHeight() / 2);
         this.transparencyRenderer.beginFrame(cameraX, cameraY, cameraZ, cameraYaw, cameraPitch);
         this.transparencyRenderer.prepareDepthBins(this.particleSystem, 0, 0, 0, (float[])null);
         return this.transparencyRenderer.renderBehindParticles(buffer, this.particle3DRenderer, baseSize, 0, 0);
      } else {
         return 0;
      }
   }

   public int renderParticlesInFrontOfAlpha(IntBuffer buffer, float cameraYaw, float cameraPitch, int cameraX, int cameraY, int cameraZ, float baseSize) {
      if (this.config.enabled() && this.particleSystem != null && this.particle3DRenderer != null && this.transparencyRenderer != null) {
         if (!this.transparencyRenderer.isDepthBinnedModeActive()) {
            this.particle3DRenderer.updateCamera(cameraYaw, cameraPitch, cameraX, cameraY, cameraZ, this.client.getScale(), this.client.getViewportWidth() / 2, this.client.getViewportHeight() / 2);
            return this.particle3DRenderer.render(buffer, this.particleSystem, baseSize, 0, 0);
         } else {
            int rendered = this.transparencyRenderer.renderInFrontParticles(buffer, this.particle3DRenderer, baseSize, 0, 0);
            this.transparencyRenderer.endFrame();
            return rendered;
         }
      } else {
         return 0;
      }
   }

   public void bindParticleTextures(int baseUnit) {
      if (this.pendingTextureReinit) {
         this.initializeParticleTexturesForContext();
         this.pendingTextureReinit = false;
      }

      if (this.particleTextureManager != null && this.particleTextureManager.isInitialized()) {
         this.particleTextureManager.bindAll(baseUnit);
      }

   }

   public void initializeParticleTexturesForContext() {
      Map<String, ParticleTextureManager.CustomTextureData> savedCustomTextures = null;
      if (this.particleTextureManager != null) {
         savedCustomTextures = this.particleTextureManager.snapshotCustomTextures();
         this.particleTextureManager.reset();
      }

      this.particleTextureManager = new ParticleTextureManager();
      int[] tierCounts = this.particleTextureManager.scanCacheForTierCounts(this.client);
      int var10002;
      Iterator var3;
      int tier;
      if (savedCustomTextures != null && !savedCustomTextures.isEmpty()) {
         for(var3 = savedCustomTextures.values().iterator(); var3.hasNext(); var10002 = tierCounts[tier]++) {
            ParticleTextureManager.CustomTextureData data = (ParticleTextureManager.CustomTextureData)var3.next();
            int maxDim = Math.max(data.getImage().getWidth(), data.getImage().getHeight());
            tier = maxDim <= 64 ? 0 : (maxDim <= 128 ? 1 : (maxDim <= 256 ? 2 : 3));
         }
      }

      this.particleTextureManager.init(tierCounts);
      if (savedCustomTextures != null && !savedCustomTextures.isEmpty()) {
         var3 = savedCustomTextures.entrySet().iterator();

         while(var3.hasNext()) {
            Map.Entry<String, ParticleTextureManager.CustomTextureData> entry = (Map.Entry)var3.next();
            ParticleTextureManager.CustomTextureData data = (ParticleTextureManager.CustomTextureData)entry.getValue();
            this.particleTextureManager.loadTexture((String)entry.getKey(), data.getImage(), data.getColumns(), data.getRows(), data.getFrameCount());
         }

         log.debug("Restored {} custom textures after GL context reset", savedCustomTextures.size());
      }

      this.resolveEmitterTexturesById();
   }

   private void resolveEmitterTexturesById() {
      if (this.particleSystem == null) {
         log.debug("resolveEmitterTexturesById: particleSystem is null, skipping");
      } else if (this.particleTextureManager == null) {
         log.debug("resolveEmitterTexturesById: particleTextureManager is null, skipping");
      } else {
         int resolved = 0;
         int total = this.particleSystem.getEmitterConfigs().size();
         List<ParticleEmitterConfig> nameBasedConfigs = new ArrayList();
         Iterator var4 = this.particleSystem.getEmitterConfigs().values().iterator();

         ParticleEmitterConfig config;
         while(var4.hasNext()) {
            config = (ParticleEmitterConfig)var4.next();
            String texture = config.getTexture();
            if (texture != null && !texture.isEmpty()) {
               try {
                  int textureId = Integer.parseInt(texture);
                  if (textureId > 0) {
                     ParticleTextureManager.TextureInfo info = this.particleTextureManager.loadTextureById(textureId, this.client);
                     if (info != null) {
                        config.setTextureIndex(info.getEncodedId());
                        config.setSpriteColumns(info.getColumns());
                        config.setSpriteRows(info.getRows());
                        config.setSpriteFrameCount(info.getFrameCount());
                        ++resolved;
                     } else {
                        config.setTextureIndex(-1);
                        log.warn("Failed to load texture '{}' for emitter {}", texture, config.getId());
                     }
                  }
               } catch (NumberFormatException var9) {
                  nameBasedConfigs.add(config);
               }
            }
         }

         var4 = nameBasedConfigs.iterator();

         while(var4.hasNext()) {
            config = (ParticleEmitterConfig)var4.next();
            this.resolveEmitterTextures(config);
            if (config.getTextureIndex() >= 0) {
               ++resolved;
            }
         }

         this.syncColourOverrideCopyTextures();
         log.debug("Resolved {}/{} emitter textures by ID", resolved, total);
      }
   }

   private void syncColourOverrideCopyTextures() {
      if (this.particleSystem != null) {
         Iterator var1 = this.particleSystem.getActiveEmitters().iterator();

         while(var1.hasNext()) {
            ParticleEmitter emitter = (ParticleEmitter)var1.next();
            ParticleEmitterConfig emitterConfig = emitter.getConfig();
            ParticleEmitterConfig base = this.particleSystem.getEmitterConfig(emitterConfig.getId());
            if (base != null && base != emitterConfig && base.getTextureIndex() != emitterConfig.getTextureIndex()) {
               emitterConfig.setTextureIndex(base.getTextureIndex());
               emitterConfig.setSpriteColumns(base.getSpriteColumns());
               emitterConfig.setSpriteRows(base.getSpriteRows());
               emitterConfig.setSpriteFrameCount(base.getSpriteFrameCount());
            }
         }

      }
   }

   public void initParticleTextures() {
      if (this.particleTextureManager != null && !this.particleTextureManager.isInitialized()) {
         this.particleTextureManager.init();
      }

   }

   public int loadParticleTexture(String name, String resourcePath) {
      if (this.particleTextureManager == null) {
         return -1;
      } else {
         if (!this.particleTextureManager.isInitialized()) {
            this.particleTextureManager.init();
         }

         return this.particleTextureManager.loadTexture(name, resourcePath);
      }
   }

   public List<String> getAvailableTextureNames() {
      return this.particleTextureManager == null ? Collections.emptyList() : this.particleTextureManager.getTextureNames();
   }

   public List<Integer> preloadTexturePreviewsFromCache() {
      if (this.particleTextureManager == null) {
         this.particleTextureManager = new ParticleTextureManager();
      }

      return this.particleTextureManager.preloadPreviewsFromCache(this.client);
   }

   public List<Integer> getAvailableTextureIds() {
      return this.particleTextureManager == null ? Collections.emptyList() : this.particleTextureManager.getAvailableTextureIds(this.client);
   }

   public String getTextureNameById(int fileId) {
      return this.particleTextureManager == null ? null : this.particleTextureManager.getTextureNameById(fileId);
   }

   public BufferedImage getTexturePreview(String name) {
      return this.particleTextureManager == null ? null : this.particleTextureManager.getTexturePreview(name);
   }

   public int loadCustomTexture(String name, BufferedImage image) {
      if (this.particleTextureManager == null) {
         return -1;
      } else {
         if (!this.particleTextureManager.isInitialized()) {
            this.particleTextureManager.init();
         }

         return this.particleTextureManager.loadTexture(name, image);
      }
   }

   public void resolveEmitterTexture(ParticleEmitterConfig config) {
      if (config != null && this.particleTextureManager != null) {
         String texture = config.getTexture();
         if (texture != null && !texture.isEmpty()) {
            int index = this.particleTextureManager.getTextureIndex(texture);
            config.setTextureIndex(index);
         } else {
            config.setTextureIndex(-1);
         }

      }
   }

   public void resolveEmitterTextures(ParticleEmitterConfig config) {
      if (config != null) {
         if (this.particleTextureManager == null) {
            config.setTextureIndex(-1);
            config.setSpriteColumns(1);
            config.setSpriteRows(1);
            config.setSpriteFrameCount(1);
            log.debug("Cannot resolve texture - texture manager is null");
         } else {
            String texture = config.getTexture();
            if (texture != null && !texture.isEmpty()) {
               ParticleTextureManager.TextureInfo info;
               try {
                  int textureId = Integer.parseInt(texture);
                  info = this.particleTextureManager.loadTextureById(textureId, this.client);
               } catch (NumberFormatException var6) {
                  info = this.particleTextureManager.getTextureInfoByName(texture);
                  if (info == null) {
                     Integer fileId = this.particleTextureManager.getFileIdByName(texture);
                     if (fileId != null) {
                        info = this.particleTextureManager.loadTextureById(fileId, this.client);
                     }
                  }
               }

               if (info != null) {
                  config.setTextureIndex(info.getEncodedId());
                  config.setSpriteColumns(info.getColumns());
                  config.setSpriteRows(info.getRows());
                  config.setSpriteFrameCount(info.getFrameCount());
                  log.info("Resolved texture '{}' -> tier={}, index={}, encodedId={}, cols={}, rows={}, frames={}", new Object[]{texture, info.getTier(), info.getIndexInTier(), info.getEncodedId(), info.getColumns(), info.getRows(), info.getFrameCount()});
               } else {
                  config.setTextureIndex(-1);
                  config.setSpriteColumns(1);
                  config.setSpriteRows(1);
                  config.setSpriteFrameCount(1);
                  log.warn("Failed to resolve texture '{}' - not found", texture);
               }
            } else {
               config.setTextureIndex(-1);
               config.setSpriteColumns(1);
               config.setSpriteRows(1);
               config.setSpriteFrameCount(1);
            }

         }
      }
   }

   public int loadCustomTexture(String name, BufferedImage image, int columns, int rows, int frameCount) {
      if (this.particleTextureManager == null) {
         return -1;
      } else {
         if (!this.particleTextureManager.isInitialized()) {
            this.particleTextureManager.init();
         }

         return this.particleTextureManager.loadTexture(name, image, columns, rows, frameCount);
      }
   }

   private static int parseHexColour(String hex) {
      long value = Long.parseLong(hex, 16);
      if (hex.length() <= 6) {
         value |= 4278190080L;
      }

      return (int)value;
   }

   public Client getClient() {
      return this.client;
   }

   public ColorPickerManager getColorPickerManager() {
      return this.colorPickerManager;
   }

   public ConfigManager getConfigManager() {
      return this.configManager;
   }

   public ClientThread getClientThread() {
      return this.clientThread;
   }

   public ParticleSystem getParticleSystem() {
      return this.particleSystem;
   }

   public Particle3DRenderer getParticle3DRenderer() {
      return this.particle3DRenderer;
   }

   public ParticleTransparencyRenderer getTransparencyRenderer() {
      return this.transparencyRenderer;
   }

   public ParticleTextureManager getParticleTextureManager() {
      return this.particleTextureManager;
   }
}
