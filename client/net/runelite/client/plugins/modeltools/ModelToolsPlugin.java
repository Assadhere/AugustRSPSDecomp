package net.runelite.client.plugins.modeltools;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import javax.inject.Inject;
import javax.swing.SwingUtilities;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.events.ChatMessage;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.callback.Hooks;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.input.KeyListener;
import net.runelite.client.input.KeyManager;
import net.runelite.client.input.MouseListener;
import net.runelite.client.input.MouseManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.ClientToolbar;
import net.runelite.client.ui.NavigationButton;
import net.runelite.client.ui.PluginPanel;
import net.runelite.client.ui.overlay.OverlayManager;
import net.runelite.client.util.ImageUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@PluginDescriptor(
   name = "Model Tools",
   description = "Developer tools for previewing, exporting, and recolouring game models",
   tags = {"model", "preview", "export", "recolour", "dev"},
   developerPlugin = true,
   enabledByDefault = true
)
public class ModelToolsPlugin extends Plugin {
   private static final Logger log = LoggerFactory.getLogger(ModelToolsPlugin.class);
   private static final String MODEL_TOOLS_PREFIX = "Model tools:";
   private static final String EDITOR_IMPL_CLASS = "net.runelite.client.plugins.modeltools.editor.ModelToolsBridgeImpl";
   @Inject
   private Client client;
   @Inject
   private ClientThread clientThread;
   @Inject
   private ClientToolbar clientToolbar;
   @Inject
   private Hooks hooks;
   @Inject
   private OverlayManager overlayManager;
   @Inject
   private MouseManager mouseManager;
   @Inject
   private KeyManager keyManager;
   @Inject
   private ConfigManager configManager;
   private volatile ModelToolsBridge bridge;
   private NavigationButton navButton;
   private URLClassLoader editorClassLoader;
   private final KeyListener keyListener = new KeyListener() {
      public void keyTyped(KeyEvent e) {
      }

      public void keyPressed(KeyEvent e) {
         try {
            if (ModelToolsPlugin.this.bridge != null && ModelToolsPlugin.this.bridge.handleKeyPressed(e)) {
               e.consume();
            }
         } catch (Exception var3) {
            Exception ex = var3;
            ModelToolsPlugin.log.warn("Key handler error", ex);
         }

      }

      public void keyReleased(KeyEvent e) {
      }
   };
   private final MouseListener mouseListener = new MouseListener() {
      public MouseEvent mousePressed(MouseEvent e) {
         try {
            if (ModelToolsPlugin.this.bridge != null && ModelToolsPlugin.this.bridge.handleMousePressed(e)) {
               ModelToolsPlugin.this.client.getCanvas().requestFocusInWindow();
               e.consume();
               return e;
            }
         } catch (Exception var3) {
            Exception ex = var3;
            ModelToolsPlugin.log.warn("Mouse handler error", ex);
         }

         ModelToolsPlugin.this.client.getCanvas().requestFocusInWindow();
         return e;
      }

      public MouseEvent mouseReleased(MouseEvent e) {
         try {
            if (ModelToolsPlugin.this.bridge != null && ModelToolsPlugin.this.bridge.handleMouseReleased(e)) {
               ModelToolsPlugin.this.client.getCanvas().requestFocusInWindow();
               e.consume();
            }
         } catch (Exception var3) {
            Exception ex = var3;
            ModelToolsPlugin.log.warn("Mouse handler error", ex);
         }

         return e;
      }

      public MouseEvent mouseDragged(MouseEvent e) {
         try {
            if (ModelToolsPlugin.this.bridge != null && ModelToolsPlugin.this.bridge.handleMouseDragged(e)) {
               e.consume();
            }
         } catch (Throwable var3) {
            Throwable t = var3;
            ModelToolsPlugin.log.debug("Mouse drag error", t);
         }

         return e;
      }

      public MouseEvent mouseClicked(MouseEvent e) {
         return e;
      }

      public MouseEvent mouseEntered(MouseEvent e) {
         return e;
      }

      public MouseEvent mouseExited(MouseEvent e) {
         return e;
      }

      public MouseEvent mouseMoved(MouseEvent e) {
         return e;
      }
   };

   protected void startUp() {
      this.bridge = this.createBridge();
      this.bridge.initialize(new ModelToolsBridge.EditorDependencies(this.client, this.clientThread, this.hooks, this.overlayManager, this.mouseManager, this.configManager));
      this.mouseManager.registerMouseListener(this.mouseListener);
      this.keyManager.registerKeyListener(this.keyListener);
      PluginPanel panel = this.bridge.getEditorPanel();
      if (panel != null) {
         this.addNavButton(panel);
      } else {
         log.info("Model tools editor module not loaded — editor panel will not be available.");
      }

   }

   protected void shutDown() {
      this.mouseManager.unregisterMouseListener(this.mouseListener);
      this.keyManager.unregisterKeyListener(this.keyListener);
      if (this.navButton != null) {
         this.clientToolbar.removeNavigation(this.navButton);
         this.navButton = null;
      }

      if (this.bridge != null) {
         this.bridge.cleanup();
         this.bridge = null;
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

   }

   @Subscribe
   public void onChatMessage(ChatMessage event) {
      if (event.getType() == ChatMessageType.ENGINE) {
         String message = event.getMessage();
         if (message.startsWith("Model tools:")) {
            String url = event.getName();
            if (url != null && !url.isEmpty()) {
               if (!(this.bridge instanceof NoOpModelToolsBridge)) {
                  log.info("Model tools editor already loaded, ignoring message");
               } else {
                  log.info("Received model tools editor URL: {}", url);
                  (new Thread(() -> {
                     try {
                        File tempJar = this.downloadJar(url);
                        SwingUtilities.invokeLater(() -> {
                           try {
                              this.initializeFromJar(tempJar);
                           } catch (Throwable var6) {
                              Throwable e = var6;
                              log.error("Failed to initialize model tools editor (version mismatch?)", e);
                              this.bridge = new NoOpModelToolsBridge();
                           } finally {
                              if (!tempJar.delete()) {
                                 log.debug("Could not delete temp JAR immediately, will be deleted on exit");
                              }

                           }

                        });
                     } catch (Exception var3) {
                        Exception e = var3;
                        log.error("Failed to download model tools editor from {}", url, e);
                     }

                  }, "ModelToolsEditorDownload")).start();
               }
            } else {
               log.warn("Model tools URL is empty");
            }
         }
      }
   }

   private ModelToolsBridge createBridge() {
      try {
         Class<?> implClass = Class.forName("net.runelite.client.plugins.modeltools.editor.ModelToolsBridgeImpl");
         log.info("Model tools editor loaded from classpath (development mode)");
         return (ModelToolsBridge)implClass.getDeclaredConstructor().newInstance();
      } catch (ClassNotFoundException var2) {
         log.debug("Model tools editor not in classpath");
      } catch (Exception var3) {
         Exception e = var3;
         log.warn("Failed to load model tools editor from classpath", e);
      }

      return new NoOpModelToolsBridge();
   }

   private File downloadJar(String urlString) throws Exception {
      log.info("Downloading model tools editor from: {}", urlString);
      URL url = new URL(urlString);
      File tempFile = File.createTempFile("model-tools-", ".jar");
      tempFile.deleteOnExit();
      InputStream inputStream = url.openStream();

      try {
         ReadableByteChannel channel = Channels.newChannel(inputStream);

         try {
            FileOutputStream fos = new FileOutputStream(tempFile);

            try {
               fos.getChannel().transferFrom(channel, 0L, Long.MAX_VALUE);
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
            if (channel != null) {
               try {
                  channel.close();
               } catch (Throwable var10) {
                  var13.addSuppressed(var10);
               }
            }

            throw var13;
         }

         if (channel != null) {
            channel.close();
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

      log.info("Successfully downloaded model tools editor JAR");
      return tempFile;
   }

   private void initializeFromJar(File jarFile) throws Exception {
      if (!jarFile.exists()) {
         log.warn("Model tools JAR does not exist: {}", jarFile);
      } else {
         if (this.bridge != null) {
            this.bridge.cleanup();
         }

         if (this.editorClassLoader != null) {
            try {
               this.editorClassLoader.close();
            } catch (Exception var4) {
               Exception e = var4;
               log.warn("Failed to close old classloader", e);
            }
         }

         this.editorClassLoader = new URLClassLoader(new URL[]{jarFile.toURI().toURL()}, this.getClass().getClassLoader());
         Class<?> implClass = this.editorClassLoader.loadClass("net.runelite.client.plugins.modeltools.editor.ModelToolsBridgeImpl");
         this.bridge = (ModelToolsBridge)implClass.getDeclaredConstructor().newInstance();
         this.bridge.initialize(new ModelToolsBridge.EditorDependencies(this.client, this.clientThread, this.hooks, this.overlayManager, this.mouseManager, this.configManager));
         PluginPanel panel = this.bridge.getEditorPanel();
         if (panel != null) {
            this.addNavButton(panel);
            log.info("Model tools editor loaded successfully from JAR");
         } else {
            log.warn("Model tools editor loaded but returned null panel");
         }

      }
   }

   private void addNavButton(PluginPanel panel) {
      if (this.navButton != null) {
         this.clientToolbar.removeNavigation(this.navButton);
      }

      BufferedImage icon = ImageUtil.loadImageResource(this.getClass(), "/model_tool_icon.png");
      this.navButton = NavigationButton.builder().tooltip("Model Tools").icon(icon).priority(11).panel(panel).build();
      this.clientToolbar.addNavigation(this.navButton);
   }
}
