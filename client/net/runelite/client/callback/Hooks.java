package net.runelite.client.callback;

import io.sentry.Sentry;
import io.sentry.protocol.User;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.awt.image.VolatileImage;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import net.runelite.api.Client;
import net.runelite.api.MainBufferProvider;
import net.runelite.api.Player;
import net.runelite.api.Renderable;
import net.runelite.api.Skill;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.BeforeRender;
import net.runelite.api.events.FakeXpDrop;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.PostClientTick;
import net.runelite.api.events.ScriptCallbackEvent;
import net.runelite.api.hooks.Callbacks;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetItem;
import net.runelite.api.worldmap.WorldMap;
import net.runelite.api.worldmap.WorldMapRenderer;
import net.runelite.client.Notifier;
import net.runelite.client.RuneLiteProperties;
import net.runelite.client.RuntimeConfig;
import net.runelite.client.TelemetryClient;
import net.runelite.client.chat.ChatMessageManager;
import net.runelite.client.eventbus.EventBus;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.RuneScapeProfileChanged;
import net.runelite.client.input.KeyManager;
import net.runelite.client.input.MouseManager;
import net.runelite.client.task.Scheduler;
import net.runelite.client.ui.ClientUI;
import net.runelite.client.ui.DrawManager;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayRenderer;
import net.runelite.client.ui.overlay.infobox.InfoBoxManager;
import net.runelite.client.util.DeferredEventBus;
import net.runelite.client.util.LinkBrowser;
import net.runelite.client.util.RSTimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
public class Hooks implements Callbacks {
   private static final Logger log = LoggerFactory.getLogger(Hooks.class);
   private static final long CHECK;
   private static final GameTick GAME_TICK;
   private static final BeforeRender BEFORE_RENDER;
   private final Client client;
   private final OverlayRenderer renderer;
   private final EventBus eventBus;
   private final DeferredEventBus deferredEventBus;
   private final Scheduler scheduler;
   private final InfoBoxManager infoBoxManager;
   private final ChatMessageManager chatMessageManager;
   private final MouseManager mouseManager;
   private final KeyManager keyManager;
   private final ClientThread clientThread;
   private final DrawManager drawManager;
   private final Notifier notifier;
   private final ClientUI clientUi;
   @Nullable
   private final TelemetryClient telemetryClient;
   @Nullable
   private final RuntimeConfig runtimeConfig;
   private final boolean developerMode;
   private final RenderCallbackManager renderCallbackManager;
   private Dimension lastStretchedDimensions;
   private VolatileImage stretchedImage;
   private Graphics2D stretchedGraphics;
   private long lastCheck;
   private boolean shouldProcessGameTick;
   private static MainBufferProvider lastMainBufferProvider;
   private static Graphics2D lastGraphics;
   private long nextError;
   private boolean rateLimitedError;
   private int errorBackoff = 1;

   private static Graphics2D getGraphics(MainBufferProvider mainBufferProvider) {
      if (lastGraphics == null || lastMainBufferProvider != mainBufferProvider) {
         if (lastGraphics != null) {
            log.debug("Graphics reset!");
            lastGraphics.dispose();
         }

         lastMainBufferProvider = mainBufferProvider;
         lastGraphics = (Graphics2D)mainBufferProvider.getImage().getGraphics();
      }

      return lastGraphics;
   }

   @Inject
   private Hooks(Client client, OverlayRenderer renderer, EventBus eventBus, DeferredEventBus deferredEventBus, Scheduler scheduler, InfoBoxManager infoBoxManager, ChatMessageManager chatMessageManager, MouseManager mouseManager, KeyManager keyManager, ClientThread clientThread, DrawManager drawManager, Notifier notifier, ClientUI clientUi, @Nullable TelemetryClient telemetryClient, @Nullable RuntimeConfig runtimeConfig, @Named("developerMode") boolean developerMode, RenderCallbackManager renderCallbackManager) {
      this.client = client;
      this.renderer = renderer;
      this.eventBus = eventBus;
      this.deferredEventBus = deferredEventBus;
      this.scheduler = scheduler;
      this.infoBoxManager = infoBoxManager;
      this.chatMessageManager = chatMessageManager;
      this.mouseManager = mouseManager;
      this.keyManager = keyManager;
      this.clientThread = clientThread;
      this.drawManager = drawManager;
      this.notifier = notifier;
      this.clientUi = clientUi;
      this.telemetryClient = telemetryClient;
      this.runtimeConfig = runtimeConfig;
      this.developerMode = developerMode;
      this.renderCallbackManager = renderCallbackManager;
      eventBus.register(this);
   }

   public void post(Object event) {
      this.eventBus.post(event);
   }

   public void postDeferred(Object event) {
      this.deferredEventBus.post(event);
   }

   public void tick() {
      if (this.shouldProcessGameTick) {
         this.shouldProcessGameTick = false;
         this.deferredEventBus.replay();
         this.eventBus.post(GAME_TICK);
         int tick = this.client.getTickCount();
         this.client.setTickCount(tick + 1);
      }

      this.clientThread.invoke();
      long now = System.nanoTime();
      if (now - this.lastCheck >= CHECK) {
         this.lastCheck = now;

         try {
            this.scheduler.tick();
            this.infoBoxManager.cull();
            this.chatMessageManager.process();
            this.checkWorldMap();
         } catch (Exception var4) {
            Exception ex = var4;
            log.error("error during main loop tasks", ex);
         }

      }
   }

   public void tickEnd() {
      this.clientThread.invokeTickEnd();
      this.eventBus.post(new PostClientTick());
   }

   public void frame() {
      this.eventBus.post(BEFORE_RENDER);
   }

   private void checkWorldMap() {
      Widget widget = this.client.getWidget(38993927);
      if (widget == null) {
         WorldMap worldMap = this.client.getWorldMap();
         if (worldMap != null) {
            WorldMapRenderer manager = worldMap.getWorldMapRenderer();
            if (manager != null && manager.isLoaded()) {
               log.debug("World map was closed, reinitializing");
               worldMap.initializeWorldMap(worldMap.getWorldMapData());
            }

         }
      }
   }

   public MouseEvent mousePressed(MouseEvent mouseEvent) {
      return this.mouseManager.processMousePressed(mouseEvent);
   }

   public MouseEvent mouseReleased(MouseEvent mouseEvent) {
      return this.mouseManager.processMouseReleased(mouseEvent);
   }

   public MouseEvent mouseClicked(MouseEvent mouseEvent) {
      return this.mouseManager.processMouseClicked(mouseEvent);
   }

   public MouseEvent mouseEntered(MouseEvent mouseEvent) {
      return this.mouseManager.processMouseEntered(mouseEvent);
   }

   public MouseEvent mouseExited(MouseEvent mouseEvent) {
      return this.mouseManager.processMouseExited(mouseEvent);
   }

   public MouseEvent mouseDragged(MouseEvent mouseEvent) {
      return this.mouseManager.processMouseDragged(mouseEvent);
   }

   public MouseEvent mouseMoved(MouseEvent mouseEvent) {
      return this.mouseManager.processMouseMoved(mouseEvent);
   }

   public MouseWheelEvent mouseWheelMoved(MouseWheelEvent event) {
      return this.mouseManager.processMouseWheelMoved(event);
   }

   public void keyPressed(KeyEvent keyEvent) {
      this.keyManager.processKeyPressed(keyEvent);
   }

   public void keyReleased(KeyEvent keyEvent) {
      this.keyManager.processKeyReleased(keyEvent);
   }

   public void keyTyped(KeyEvent keyEvent) {
      this.keyManager.processKeyTyped(keyEvent);
   }

   public void draw(MainBufferProvider mainBufferProvider, Graphics graphics, int x, int y) {
      if (graphics != null) {
         Graphics2D graphics2d = getGraphics(mainBufferProvider);

         try {
            this.renderer.renderOverlayLayer(graphics2d, OverlayLayer.ALWAYS_ON_TOP);
         } catch (Exception var11) {
            Exception ex = var11;
            log.error("Error during overlay rendering", ex);
         }

         this.notifier.processFlash(graphics2d);
         this.clientUi.paintOverlays(graphics2d);
         if (!this.client.isGpu()) {
            Image image = mainBufferProvider.getImage();
            Object finalImage;
            if (this.client.isStretchedEnabled()) {
               GraphicsConfiguration gc = this.clientUi.getGraphicsConfiguration();
               Dimension stretchedDimensions = this.client.getStretchedDimensions();
               int status = true;
               int status;
               if (!stretchedDimensions.equals(this.lastStretchedDimensions) || this.stretchedImage == null || (status = this.stretchedImage.validate(gc)) != 0) {
                  log.debug("Volatile image non-OK status: {}", status);
                  if (this.stretchedGraphics != null) {
                     this.stretchedGraphics.dispose();
                  }

                  if (!stretchedDimensions.equals(this.lastStretchedDimensions) || this.stretchedImage == null || status == 2) {
                     if (this.stretchedImage != null) {
                        this.stretchedImage.flush();
                     }

                     this.stretchedImage = gc.createCompatibleVolatileImage(stretchedDimensions.width, stretchedDimensions.height);
                     this.lastStretchedDimensions = stretchedDimensions;
                  }

                  this.stretchedGraphics = (Graphics2D)this.stretchedImage.getGraphics();
               }

               this.stretchedGraphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, this.client.isStretchedFast() ? RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR : RenderingHints.VALUE_INTERPOLATION_BILINEAR);
               this.stretchedGraphics.drawImage(image, 0, 0, stretchedDimensions.width, stretchedDimensions.height, (ImageObserver)null);
               finalImage = this.stretchedImage;
            } else {
               if (this.stretchedImage != null) {
                  log.debug("Releasing stretched volatile image");
                  this.stretchedGraphics.dispose();
                  this.stretchedImage.flush();
                  this.stretchedGraphics = null;
                  this.stretchedImage = null;
                  this.lastStretchedDimensions = null;
               }

               finalImage = image;
            }

            graphics.drawImage((Image)finalImage, 0, 0, this.client.getCanvas());
            this.drawManager.processDrawComplete(() -> {
               return this.screenshot(finalImage);
            });
         }
      }
   }

   private Image screenshot(Image src) {
      AffineTransform transform = this.clientUi.getGraphicsConfiguration().getDefaultTransform();
      int swidth = src.getWidth((ImageObserver)null);
      int sheight = src.getHeight((ImageObserver)null);
      int twidth = (int)((double)swidth * transform.getScaleX() + 0.5);
      int theight = (int)((double)sheight * transform.getScaleY() + 0.5);
      BufferedImage image = new BufferedImage(twidth, theight, 1);
      Graphics2D graphics = (Graphics2D)image.getGraphics();
      graphics.setTransform(transform);
      graphics.drawImage(src, 0, 0, swidth, sheight, (ImageObserver)null);
      graphics.dispose();
      return image;
   }

   public void drawScene() {
      MainBufferProvider bufferProvider = (MainBufferProvider)this.client.getBufferProvider();
      Graphics2D graphics2d = getGraphics(bufferProvider);

      try {
         this.renderer.renderOverlayLayer(graphics2d, OverlayLayer.ABOVE_SCENE);
      } catch (Exception var4) {
         Exception ex = var4;
         log.error("Error during overlay rendering", ex);
      }

   }

   public void drawAboveOverheads() {
      MainBufferProvider bufferProvider = (MainBufferProvider)this.client.getBufferProvider();
      Graphics2D graphics2d = getGraphics(bufferProvider);

      try {
         this.renderer.renderOverlayLayer(graphics2d, OverlayLayer.UNDER_WIDGETS);
      } catch (Exception var4) {
         Exception ex = var4;
         log.error("Error during overlay rendering", ex);
      }

   }

   public void serverTick() {
      this.shouldProcessGameTick = true;
   }

   public void drawInterface(int interfaceId, List<WidgetItem> widgetItems) {
      MainBufferProvider bufferProvider = (MainBufferProvider)this.client.getBufferProvider();
      Graphics2D graphics2d = getGraphics(bufferProvider);

      try {
         this.renderer.renderAfterInterface(graphics2d, interfaceId, widgetItems);
      } catch (Exception var6) {
         Exception ex = var6;
         log.error("Error during overlay rendering", ex);
      }

   }

   public void drawLayer(Widget layer, List<WidgetItem> widgetItems) {
      MainBufferProvider bufferProvider = (MainBufferProvider)this.client.getBufferProvider();
      Graphics2D graphics2d = getGraphics(bufferProvider);

      try {
         this.renderer.renderAfterLayer(graphics2d, layer, widgetItems);
      } catch (Exception var6) {
         Exception ex = var6;
         log.error("Error during overlay rendering", ex);
      }

   }

   @Subscribe
   public void onRuneScapeProfileChanged(RuneScapeProfileChanged event) {
      Player localPlayer = this.client.getLocalPlayer();
      if (localPlayer != null && localPlayer.getName() != null) {
         Sentry.configureScope((scope) -> {
            User user = new User();
            user.setUsername(localPlayer.getName());
            scope.setUser(user);
         });
      }

   }

   @Subscribe
   public void onScriptCallbackEvent(ScriptCallbackEvent scriptCallbackEvent) {
      if (scriptCallbackEvent.getEventName().equals("fakeXpDrop")) {
         int[] intStack = this.client.getIntStack();
         int intStackSize = this.client.getIntStackSize();
         int statId = intStack[intStackSize - 2];
         int xp = intStack[intStackSize - 1];
         Skill skill = Skill.values()[statId];
         FakeXpDrop fakeXpDrop = new FakeXpDrop(skill, xp);
         this.eventBus.post(fakeXpDrop);
      }
   }

   /** @deprecated */
   @Deprecated
   public void registerRenderableDrawListener(RenderableDrawListener listener) {
      this.renderCallbackManager.register(listener);
   }

   /** @deprecated */
   @Deprecated
   public void unregisterRenderableDrawListener(RenderableDrawListener listener) {
      this.renderCallbackManager.unregister(listener);
   }

   public boolean draw(Renderable renderable, boolean drawingUi) {
      try {
         return this.renderCallbackManager.addEntity(renderable, drawingUi);
      } catch (Exception var4) {
         Exception ex = var4;
         log.error("exception from render callback", ex);
         return true;
      }
   }

   public void onDraw(Renderable renderable) {
      try {
         this.renderCallbackManager.onDraw(renderable);
      } catch (Exception var3) {
         Exception ex = var3;
         log.error("exception from render callback", ex);
      }

   }

   public void error(String message, Throwable reason) {
      if (this.telemetryClient != null) {
         long now = System.currentTimeMillis();
         if (now > this.nextError) {
            StringWriter sw = new StringWriter();
            sw.append(message);
            if (reason != null) {
               sw.append(" - ").append(reason.toString()).append('\n');
               PrintWriter pw = new PrintWriter(sw);

               try {
                  reason.printStackTrace(pw);
               } catch (Throwable var11) {
                  try {
                     pw.close();
                  } catch (Throwable var10) {
                     var11.addSuppressed(var10);
                  }

                  throw var11;
               }

               pw.close();
            }

            String coord = "unk";
            Player player = this.client.getLocalPlayer();
            if (player != null) {
               LocalPoint lp = player.getLocalLocation();
               if (lp.getWorldView() == 0 || this.client.getClientThread() == Thread.currentThread()) {
                  WorldPoint p = WorldPoint.fromLocalInstance(this.client, lp);
                  coord = String.format("%d_%d_%d_%d_%d", p.getPlane(), p.getX() / 64, p.getY() / 64, p.getX() & 63, p.getY() & 63);
               }
            }

            this.telemetryClient.submitError("client error", sw.toString(), Collections.singletonMap("coord", coord));
            if (this.rateLimitedError) {
               ++this.errorBackoff;
               this.rateLimitedError = false;
            } else {
               this.errorBackoff = 1;
            }

            this.nextError = now + 10000L * (long)this.errorBackoff;
         } else {
            this.rateLimitedError = true;
         }

      }
   }

   public void openUrl(String url) {
      LinkBrowser.browse(url);
   }

   public boolean isRuneLiteClientOutdated() {
      if (this.runtimeConfig != null && !this.developerMode) {
         Set<String> outdatedClientVersions = this.runtimeConfig.getOutdatedClientVersions();
         return outdatedClientVersions == null ? false : outdatedClientVersions.contains(RuneLiteProperties.getVersion());
      } else {
         return false;
      }
   }

   static {
      CHECK = RSTimeUnit.GAME_TICKS.getDuration().toNanos();
      GAME_TICK = new GameTick();
      BEFORE_RENDER = new BeforeRender();
   }

   /** @deprecated */
   @FunctionalInterface
   @Deprecated
   public interface RenderableDrawListener extends RenderCallback {
      boolean draw(Renderable var1, boolean var2);

      default boolean addEntity(Renderable renderable, boolean ui) {
         return this.draw(renderable, ui);
      }
   }
}
