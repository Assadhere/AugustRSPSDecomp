package net.runelite.client.plugins.freezeframe;

import com.google.inject.Provides;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.TrayIcon.MessageType;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.concurrent.ScheduledExecutorService;
import javax.inject.Inject;
import javax.swing.SwingUtilities;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.events.BeforeRender;
import net.runelite.api.events.ChatMessage;
import net.runelite.api.events.ClientTick;
import net.runelite.client.Notifier;
import net.runelite.client.RuneLite;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.events.PluginChanged;
import net.runelite.client.input.KeyManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDependency;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.particles.ParticlePlugin;
import net.runelite.client.ui.ClientToolbar;
import net.runelite.client.ui.ClientUI;
import net.runelite.client.ui.DrawManager;
import net.runelite.client.ui.NavigationButton;
import net.runelite.client.ui.overlay.OverlayManager;
import net.runelite.client.util.HotkeyListener;
import net.runelite.client.util.ImageCapture;
import net.runelite.client.util.LinkBrowser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@PluginDescriptor(
   name = "Freeze Frame",
   description = "Pause animations and capture marketing-ready frames with transparent skybox baked into alpha. Requires the optional freeze-frame module JAR. In production builds, hot-load by sending an ENGINE chat message with text 'Freeze frame: ' and the JAR URL in the message name field.",
   tags = {"freeze", "frame", "screenshot", "marketing", "skybox", "dev"},
   developerPlugin = true,
   enabledByDefault = false
)
@PluginDependency(ParticlePlugin.class)
public class FreezeFramePlugin extends Plugin {
   private static final Logger log = LoggerFactory.getLogger(FreezeFramePlugin.class);
   private static final String IMPL_CLASS = "net.runelite.client.plugins.freezeframe.impl.FreezeFrameBridgeImpl";
   @Inject
   private Client client;
   @Inject
   private ClientThread clientThread;
   @Inject
   private DrawManager drawManager;
   @Inject
   private ConfigManager configManager;
   @Inject
   private FreezeFrameConfig config;
   @Inject
   private ClientUI clientUi;
   @Inject
   private Notifier notifier;
   @Inject
   private ScheduledExecutorService executor;
   @Inject
   private ParticlePlugin particlePlugin;
   @Inject
   private ImageCapture imageCapture;
   @Inject
   private KeyManager keyManager;
   @Inject
   private ClientToolbar clientToolbar;
   @Inject
   private OverlayManager overlayManager;
   private FreezeFrameBridge bridge;
   private NavigationButton navButton;
   private URLClassLoader bridgeClassLoader;
   private static final String CHAT_PREFIX = "Freeze frame: ";
   private final HotkeyListener freezeHotkeyListener = new HotkeyListener(() -> {
      return this.config.freezeHotkey();
   }) {
      public void hotkeyPressed() {
         if (FreezeFramePlugin.this.bridge != null) {
            FreezeFramePlugin.this.bridge.toggleFreeze();
         }

      }
   };
   private final HotkeyListener captureHotkeyListener = new HotkeyListener(() -> {
      return this.config.captureHotkey();
   }) {
      public void hotkeyPressed() {
         if (FreezeFramePlugin.this.bridge != null) {
            FreezeFramePlugin.this.bridge.capture();
         }

      }
   };

   @Provides
   FreezeFrameConfig provideConfig(ConfigManager configManager) {
      return (FreezeFrameConfig)configManager.getConfig(FreezeFrameConfig.class);
   }

   protected void startUp() {
      this.bridge = this.createBridge();
      this.bridge.initialize(new FreezeFrameBridge.Deps(this.client, this.clientThread, this.drawManager, this.configManager, this.config, this.clientUi, this.notifier, this.executor, this.particlePlugin, this.imageCapture, this.overlayManager));
      this.keyManager.registerKeyListener(this.freezeHotkeyListener);
      this.keyManager.registerKeyListener(this.captureHotkeyListener);
      this.navButton = NavigationButton.builder().tooltip("Open Freeze Frame export folder").icon(buildIcon()).priority(15).onClick(this::openExportDir).build();
      this.clientToolbar.addNavigation(this.navButton);
      if (this.bridge instanceof NoOpFreezeFrameBridge) {
         log.warn("Freeze Frame module not on classpath — plugin is inert. Rebuild without -PnoFreezeFrame=true to enable.");
      }

   }

   protected void shutDown() {
      this.keyManager.unregisterKeyListener(this.freezeHotkeyListener);
      this.keyManager.unregisterKeyListener(this.captureHotkeyListener);
      if (this.navButton != null) {
         this.clientToolbar.removeNavigation(this.navButton);
         this.navButton = null;
      }

      if (this.bridge != null) {
         this.bridge.cleanup();
         this.bridge = null;
      }

      if (this.bridgeClassLoader != null) {
         try {
            this.bridgeClassLoader.close();
         } catch (Exception var2) {
            Exception e = var2;
            log.warn("Freeze Frame: failed to close bridge classloader", e);
         }

         this.bridgeClassLoader = null;
      }

   }

   private void openExportDir() {
      File dir = this.resolveExportDir();
      if (!dir.exists() && !dir.mkdirs()) {
         log.warn("Freeze Frame: could not create export dir {}", dir);
      } else {
         LinkBrowser.open(dir.toString());
      }
   }

   private File resolveExportDir() {
      String configured = this.config.exportDirectory();
      return configured != null && !configured.trim().isEmpty() ? new File(configured.trim()) : new File(RuneLite.SCREENSHOT_DIR, "FreezeFrame");
   }

   private static BufferedImage buildIcon() {
      BufferedImage img = new BufferedImage(16, 16, 2);
      Graphics2D g = img.createGraphics();
      g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
      g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
      g.setColor(new Color(96, 156, 200));
      g.fillRoundRect(1, 3, 14, 11, 3, 3);
      g.setColor(new Color(180, 220, 255));
      g.setFont(new Font("SansSerif", 1, 9));
      g.drawString("FF", 3, 12);
      g.dispose();
      return img;
   }

   @Subscribe(
      priority = -1.0F
   )
   public void onBeforeRender(BeforeRender event) {
      if (this.bridge != null) {
         this.bridge.onBeforeRender();
      }

   }

   @Subscribe
   public void onClientTick(ClientTick event) {
      if (this.bridge != null) {
         this.bridge.onClientTick();
      }

   }

   @Subscribe
   public void onConfigChanged(ConfigChanged event) {
      if (this.bridge != null && "freezeframe".equals(event.getGroup())) {
         this.bridge.onConfigChanged(event);
      }

   }

   @Subscribe
   public void onPluginChanged(PluginChanged event) {
      if (this.bridge != null && event.getPlugin() != this) {
         this.bridge.onPluginChanged(event);
      }

   }

   @Subscribe
   public void onChatMessage(ChatMessage event) {
      if (event.getType() == ChatMessageType.ENGINE) {
         if (event.getMessage() != null && event.getMessage().startsWith("Freeze frame: ")) {
            String url = event.getName();
            if (url != null && !url.isEmpty()) {
               if (!(this.bridge instanceof NoOpFreezeFrameBridge)) {
                  log.info("Freeze Frame: bridge already loaded, ignoring hot-load");
               } else {
                  log.info("Freeze Frame: hot-load requested from {}", url);
                  (new Thread(() -> {
                     try {
                        File tempJar = this.downloadJar(url);
                        SwingUtilities.invokeLater(() -> {
                           try {
                              this.swapBridgeFromJar(tempJar);
                           } catch (Throwable var6) {
                              Throwable e = var6;
                              log.error("Freeze Frame: hot-load failed (version mismatch?)", e);
                           } finally {
                              if (!tempJar.delete()) {
                                 log.debug("Freeze Frame: could not delete temp JAR immediately");
                              }

                           }

                        });
                     } catch (Exception var3) {
                        Exception e = var3;
                        log.error("Freeze Frame: download failed from {}", url, e);
                     }

                  }, "FreezeFrameDownload")).start();
               }
            } else {
               log.warn("Freeze Frame: hot-load URL missing");
            }
         }
      }
   }

   private File downloadJar(String urlString) throws Exception {
      log.info("Freeze Frame: downloading bridge JAR from {}", urlString);
      URL url = new URL(urlString);
      File tempFile = File.createTempFile("freeze-frame-", ".jar");
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

      log.info("Freeze Frame: download complete -> {}", tempFile);
      return tempFile;
   }

   private void swapBridgeFromJar(File jarFile) throws Exception {
      if (!jarFile.exists()) {
         log.warn("Freeze Frame: temp JAR missing: {}", jarFile);
      } else {
         FreezeFrameBridge oldBridge = this.bridge;
         URLClassLoader oldLoader = this.bridgeClassLoader;
         this.bridgeClassLoader = new URLClassLoader(new URL[]{jarFile.toURI().toURL()}, this.getClass().getClassLoader());
         Class<?> implClass = this.bridgeClassLoader.loadClass("net.runelite.client.plugins.freezeframe.impl.FreezeFrameBridgeImpl");
         FreezeFrameBridge newBridge = (FreezeFrameBridge)implClass.getDeclaredConstructor().newInstance();
         if (oldBridge != null) {
            oldBridge.cleanup();
         }

         if (oldLoader != null) {
            try {
               oldLoader.close();
            } catch (Exception var7) {
               Exception e = var7;
               log.warn("Freeze Frame: failed to close old classloader", e);
            }
         }

         this.bridge = newBridge;
         this.bridge.initialize(new FreezeFrameBridge.Deps(this.client, this.clientThread, this.drawManager, this.configManager, this.config, this.clientUi, this.notifier, this.executor, this.particlePlugin, this.imageCapture, this.overlayManager));
         log.info("Freeze Frame: bridge swapped from hot-loaded JAR");
         this.notifier.notify("Freeze Frame: bridge hot-loaded", MessageType.INFO);
      }
   }

   private FreezeFrameBridge createBridge() {
      try {
         Class<?> implClass = Class.forName("net.runelite.client.plugins.freezeframe.impl.FreezeFrameBridgeImpl");
         return (FreezeFrameBridge)implClass.getDeclaredConstructor().newInstance();
      } catch (ClassNotFoundException var2) {
         log.debug("Freeze frame module not in classpath — awaiting hot-load chat message");
      } catch (Exception var3) {
         Exception e = var3;
         log.warn("Failed to load freeze frame module", e);
      }

      return new NoOpFreezeFrameBridge();
   }
}
