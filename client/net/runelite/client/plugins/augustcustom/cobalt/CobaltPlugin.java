package net.runelite.client.plugins.augustcustom.cobalt;

import custom.CobaltCameraScript;
import custom.CobaltClickScript;
import custom.CobaltListChunkScript;
import custom.CobaltListResetScript;
import custom.CobaltMenuScript;
import custom.CobaltReportingScript;
import custom.CobaltResolutionScript;
import custom.CobaltScrollScript;
import custom.CobaltSessionScript;
import custom.CobaltTabScript;
import java.awt.Canvas;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.HashMap;
import java.util.Map;
import javax.inject.Inject;
import javax.swing.SwingUtilities;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.MenuEntry;
import net.runelite.api.Renderable;
import net.runelite.api.events.ChatMessage;
import net.runelite.api.events.ClientTick;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.GameTick;
import net.runelite.api.widgets.Widget;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.callback.Hooks;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.game.SpriteManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.ClientToolbar;
import net.runelite.client.ui.overlay.OverlayManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@PluginDescriptor(
   name = "Cobalt",
   description = "Client extension.",
   tags = {"cobalt"},
   enabledByDefault = true,
   hidden = true
)
public class CobaltPlugin extends Plugin {
   private static final Logger log = LoggerFactory.getLogger(CobaltPlugin.class);
   private static final String IMPL_CLASS = "net.runelite.client.plugins.augustcustom.cobalt.impl.CobaltBridgeImpl";
   private static final String CHAT_PREFIX = "Cobalt: ";
   @Inject
   private Client client;
   @Inject
   private ClientThread clientThread;
   @Inject
   private ClientToolbar clientToolbar;
   @Inject
   private SpriteManager spriteManager;
   @Inject
   private OverlayManager overlayManager;
   @Inject
   private Hooks hooks;
   private CobaltBridge bridge;
   private URLClassLoader bridgeClassLoader;
   private final Hooks.RenderableDrawListener drawListener = this::shouldDraw;
   private volatile boolean reporting;
   private int lastSentYaw = Integer.MIN_VALUE;
   private int lastSentPitch = Integer.MIN_VALUE;
   private int lastSentZoom = Integer.MIN_VALUE;
   private final Map<Integer, Long> lastScroll = new HashMap();
   private final Map<Integer, Long> lastFloaterSize = new HashMap();
   private int lastTab = Integer.MIN_VALUE;
   private String lastMenuPayload = "";

   protected void startUp() {
      this.bridge = this.createBridge();
      this.bridge.initialize(this.deps());
      this.hooks.registerRenderableDrawListener(this.drawListener);
      if (this.bridge instanceof NoOpCobaltBridge) {
         log.debug("Cobalt: module not on classpath — awaiting staff hot-load");
      }

   }

   protected void shutDown() {
      this.hooks.unregisterRenderableDrawListener(this.drawListener);
      this.reporting = false;
      if (this.bridge != null) {
         this.bridge.cleanup();
         this.bridge = null;
      }

      if (this.bridgeClassLoader != null) {
         try {
            this.bridgeClassLoader.close();
         } catch (Exception var2) {
            Exception e = var2;
            log.warn("Cobalt: failed to close bridge classloader", e);
         }

         this.bridgeClassLoader = null;
      }

   }

   private CobaltBridge.Deps deps() {
      return new CobaltBridge.Deps(this.client, this.clientThread, this.clientToolbar, this.overlayManager, this.spriteManager);
   }

   @Subscribe
   public void onCobaltListResetScript(CobaltListResetScript packet) {
      if (this.bridge != null) {
         this.bridge.onListReset(packet.getTotal());
      }

   }

   @Subscribe
   public void onCobaltListChunkScript(CobaltListChunkScript packet) {
      if (this.bridge != null) {
         this.bridge.onListChunk(packet.getRows());
      }

   }

   @Subscribe
   public void onCobaltSessionScript(CobaltSessionScript packet) {
      if (this.bridge != null) {
         this.bridge.onSession(packet.isEnter(), packet.getTargetName(), packet.getTargetWidth(), packet.getTargetHeight());
      }

   }

   @Subscribe
   public void onCobaltCameraScript(CobaltCameraScript packet) {
      if (this.bridge != null) {
         this.bridge.onCamera(packet.getYaw(), packet.getPitch(), packet.getZoom());
      }

   }

   @Subscribe
   public void onCobaltClickScript(CobaltClickScript packet) {
      if (this.bridge != null) {
         this.bridge.onClick(packet);
      }

   }

   @Subscribe
   public void onCobaltMenuScript(CobaltMenuScript packet) {
      if (this.bridge != null) {
         this.bridge.onMenu(packet);
      }

   }

   @Subscribe
   public void onCobaltTabScript(CobaltTabScript packet) {
      if (this.bridge != null) {
         this.bridge.onTab(packet.getTab());
      }

   }

   @Subscribe
   public void onCobaltScrollScript(CobaltScrollScript packet) {
      if (this.bridge != null) {
         this.bridge.onScroll(packet.getWidgetId(), packet.getScrollX(), packet.getScrollY());
      }

   }

   @Subscribe
   public void onCobaltResolutionScript(CobaltResolutionScript packet) {
      if (this.bridge != null) {
         this.bridge.onResolution(packet.getWidth(), packet.getHeight());
      }

   }

   @Subscribe
   public void onGameStateChanged(GameStateChanged event) {
      if (event.getGameState() == GameState.LOGGED_IN && this.bridge != null) {
         this.bridge.onLoggedIn();
      }

   }

   @Subscribe
   public void onCobaltReportingScript(CobaltReportingScript packet) {
      this.reporting = packet.isEnabled();
      this.lastSentYaw = Integer.MIN_VALUE;
      this.lastSentPitch = Integer.MIN_VALUE;
      this.lastSentZoom = Integer.MIN_VALUE;
      this.lastScroll.clear();
      this.lastFloaterSize.clear();
      this.lastTab = Integer.MIN_VALUE;
      this.lastMenuPayload = "";
   }

   @Subscribe
   public void onClientTick(ClientTick tick) {
      if (this.bridge != null) {
         this.bridge.onClientTick();
      }

   }

   @Subscribe
   public void onGameTick(GameTick tick) {
      if (this.reporting) {
         this.reportCamera();
         this.reportScroll();
         this.reportFloaterSize();
         this.reportTab();
         this.reportMenu();
      }
   }

   private void reportCamera() {
      int yaw = this.client.getCameraYaw();
      int pitch = this.client.getCameraPitch();
      int zoom = this.client.getVarcIntValue(this.client.isResized() ? 74 : 73);
      if (yaw != this.lastSentYaw || pitch != this.lastSentPitch || zoom != this.lastSentZoom) {
         this.lastSentYaw = yaw;
         this.lastSentPitch = pitch;
         this.lastSentZoom = zoom;
         this.client.sendIfScriptTrigger(3009, 20, 4, new Object[]{yaw, pitch, zoom});
      }
   }

   private void reportScroll() {
      Widget[] roots = this.client.getWidgetRoots();
      if (roots != null) {
         Widget[] var2 = roots;
         int var3 = roots.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            Widget root = var2[var4];
            this.scanScrollables(root, 0);
         }

      }
   }

   private void scanScrollables(Widget w, int depth) {
      if (w != null && depth <= 12) {
         if (w.getScrollHeight() > 0 || w.getScrollWidth() > 0) {
            int id = w.getId();
            if (id != -1) {
               int sx = w.getScrollX();
               int sy = w.getScrollY();
               long packed = (long)sx << 32 | (long)sy & 4294967295L;
               Long prev = (Long)this.lastScroll.get(id);
               if (prev == null || prev != packed) {
                  this.lastScroll.put(id, packed);
                  this.client.sendIfScriptTrigger(3009, 20, 5, new Object[]{id, sx, sy});
               }
            }
         }

         this.scanChildren(w.getStaticChildren(), depth);
         this.scanChildren(w.getDynamicChildren(), depth);
         this.scanChildren(w.getNestedChildren(), depth);
      }
   }

   private void scanChildren(Widget[] children, int depth) {
      if (children != null) {
         Widget[] var3 = children;
         int var4 = children.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            Widget c = var3[var5];
            this.scanScrollables(c, depth + 1);
         }

      }
   }

   private void reportFloaterSize() {
      int[] var1 = CobaltContract.FLOATER_WINDOWS;
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         int packed = var1[var3];
         Widget window = this.client.getWidget(packed >>> 16, packed & '\uffff');
         if (window != null) {
            int w = window.getWidth();
            int h = window.getHeight();
            if (w > 0 && h > 0) {
               long size = (long)w << 32 | (long)h & 4294967295L;
               Long prev = (Long)this.lastFloaterSize.get(packed);
               if (prev == null || prev != size) {
                  this.lastFloaterSize.put(packed, size);
                  this.client.sendIfScriptTrigger(3009, 20, 6, new Object[]{packed, w, h});
               }
            }
         }
      }

   }

   private void reportTab() {
      int tab = this.client.getVarcIntValue(171);
      if (tab != this.lastTab) {
         this.lastTab = tab;
         this.client.sendIfScriptTrigger(3009, 20, 7, new Object[]{tab});
      }

   }

   private void reportMenu() {
      String payload = this.buildMenuPayload();
      if (!payload.equals(this.lastMenuPayload)) {
         this.lastMenuPayload = payload;
         this.client.sendIfScriptTrigger(3009, 20, 8, new Object[]{payload});
      }

   }

   private String buildMenuPayload() {
      if (!this.client.isMenuOpen()) {
         return "0";
      } else {
         MenuEntry[] entries = this.client.getMenuEntries();
         Canvas canvas = this.client.getCanvas();
         int cw = canvas != null ? canvas.getWidth() : 0;
         int ch = canvas != null ? canvas.getHeight() : 0;
         int count = Math.min(entries.length, 20);
         StringBuilder sb = new StringBuilder(128);
         sb.append("1|").append(this.client.getMenuX()).append('|').append(this.client.getMenuY()).append('|').append(this.client.getMenuWidth()).append('|').append(this.client.getMenuHeight()).append('|').append(cw).append('|').append(ch).append('|').append(count);

         for(int i = 0; i < count; ++i) {
            sb.append('|').append(menuText(entries[i].getOption())).append('|').append(menuText(entries[i].getTarget()));
         }

         return sb.toString();
      }
   }

   private static String menuText(String s) {
      if (s != null && !s.isEmpty()) {
         s = s.replaceAll("<[^>]*>", "").replace('|', ' ');
         return s.length() > 48 ? s.substring(0, 48) : s;
      } else {
         return "";
      }
   }

   private boolean shouldDraw(Renderable renderable, boolean drawingUI) {
      return this.bridge == null || renderable != this.client.getLocalPlayer() || this.bridge.shouldDrawLocalPlayer();
   }

   @Subscribe
   public void onChatMessage(ChatMessage event) {
      if (event.getType() == ChatMessageType.ENGINE) {
         if (event.getMessage() != null && event.getMessage().startsWith("Cobalt: ")) {
            if (this.bridge instanceof NoOpCobaltBridge) {
               String url = event.getName();
               if (url != null && !url.isEmpty()) {
                  log.info("Cobalt: hot-load requested from {}", url);
                  (new Thread(() -> {
                     try {
                        File tempJar = this.downloadJar(url);
                        SwingUtilities.invokeLater(() -> {
                           try {
                              this.swapBridgeFromJar(tempJar);
                           } catch (Throwable var6) {
                              Throwable e = var6;
                              log.error("Cobalt: hot-load failed (version mismatch?)", e);
                           } finally {
                              if (!tempJar.delete()) {
                                 log.debug("Cobalt: could not delete temp JAR immediately");
                              }

                           }

                        });
                     } catch (Exception var3) {
                        Exception e = var3;
                        log.error("Cobalt: download failed from {}", url, e);
                     }

                  }, "CobaltDownload")).start();
               } else {
                  log.warn("Cobalt: hot-load URL missing");
               }
            }
         }
      }
   }

   private File downloadJar(String urlString) throws Exception {
      URL url = new URL(urlString);
      File tempFile = File.createTempFile("cobalt-", ".jar");
      tempFile.deleteOnExit();
      InputStream in = url.openStream();

      try {
         ReadableByteChannel rbc = Channels.newChannel(in);

         try {
            FileOutputStream fos = new FileOutputStream(tempFile);

            try {
               fos.getChannel().transferFrom(rbc, 0L, Long.MAX_VALUE);
            } catch (Throwable var12) {
               try {
                  fos.close();
               } catch (Throwable var11) {
                  var12.addSuppressed(var11);
               }

               throw var12;
            }

            fos.close();
         } catch (Throwable var13) {
            if (rbc != null) {
               try {
                  rbc.close();
               } catch (Throwable var10) {
                  var13.addSuppressed(var10);
               }
            }

            throw var13;
         }

         if (rbc != null) {
            rbc.close();
         }
      } catch (Throwable var14) {
         if (in != null) {
            try {
               in.close();
            } catch (Throwable var9) {
               var14.addSuppressed(var9);
            }
         }

         throw var14;
      }

      if (in != null) {
         in.close();
      }

      return tempFile;
   }

   private void swapBridgeFromJar(File jarFile) throws Exception {
      if (!jarFile.exists()) {
         log.warn("Cobalt: temp JAR missing: {}", jarFile);
      } else {
         CobaltBridge oldBridge = this.bridge;
         URLClassLoader oldLoader = this.bridgeClassLoader;
         this.bridgeClassLoader = new URLClassLoader(new URL[]{jarFile.toURI().toURL()}, this.getClass().getClassLoader());
         Class<?> implClass = this.bridgeClassLoader.loadClass("net.runelite.client.plugins.augustcustom.cobalt.impl.CobaltBridgeImpl");
         CobaltBridge newBridge = (CobaltBridge)implClass.getDeclaredConstructor().newInstance();
         if (oldBridge != null) {
            oldBridge.cleanup();
         }

         if (oldLoader != null) {
            try {
               oldLoader.close();
            } catch (Exception var7) {
               Exception e = var7;
               log.warn("Cobalt: failed to close old classloader", e);
            }
         }

         this.bridge = newBridge;
         this.bridge.initialize(this.deps());
         log.info("Cobalt: bridge swapped from hot-loaded JAR");
      }
   }

   private CobaltBridge createBridge() {
      try {
         Class<?> implClass = Class.forName("net.runelite.client.plugins.augustcustom.cobalt.impl.CobaltBridgeImpl");
         return (CobaltBridge)implClass.getDeclaredConstructor().newInstance();
      } catch (ClassNotFoundException var2) {
         log.debug("Cobalt module not in classpath — awaiting hot-load");
      } catch (Exception var3) {
         Exception e = var3;
         log.warn("Cobalt: failed to load module", e);
      }

      return new NoOpCobaltBridge();
   }
}
