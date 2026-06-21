package net.runelite.client.plugins.freezeframe.impl;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.TrayIcon.MessageType;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;
import javax.imageio.ImageIO;
import net.runelite.api.Actor;
import net.runelite.api.ActorSpotAnim;
import net.runelite.api.Client;
import net.runelite.api.DynamicObject;
import net.runelite.api.GameObject;
import net.runelite.api.GameState;
import net.runelite.api.GraphicsObject;
import net.runelite.api.IntProjection;
import net.runelite.api.Model;
import net.runelite.api.ModelData;
import net.runelite.api.NPC;
import net.runelite.api.Player;
import net.runelite.api.Projectile;
import net.runelite.api.Projection;
import net.runelite.api.Renderable;
import net.runelite.api.RuneLiteObject;
import net.runelite.api.Scene;
import net.runelite.api.SceneTileModel;
import net.runelite.api.SceneTilePaint;
import net.runelite.api.Texture;
import net.runelite.api.TileItem;
import net.runelite.api.TileObject;
import net.runelite.api.WorldEntity;
import net.runelite.api.WorldView;
import net.runelite.api.hooks.DrawCallbacks;
import net.runelite.client.RuneLite;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.events.PluginChanged;
import net.runelite.client.plugins.freezeframe.FreezeFrameBridge;
import net.runelite.client.plugins.freezeframe.FreezeFrameConfig;
import net.runelite.client.plugins.freezeframe.FreezeFrameFlags;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayPriority;
import net.runelite.client.util.ImageUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FreezeFrameBridgeImpl implements FreezeFrameBridge, DrawCallbacks {
   private static final Logger log = LoggerFactory.getLogger(FreezeFrameBridgeImpl.class);
   private static final int SKY_BLACK = 0;
   private static final Color TRANSPARENT = new Color(0, 0, 0, 0);
   private static final DateFormat TIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
   private FreezeFrameBridge.Deps deps;
   private FreezeFrameConfig config;
   private volatile boolean cleanedUp;
   private volatile boolean frozen;
   private final Map<Actor, AnimSnapshot> snapshots = Collections.synchronizedMap(new WeakHashMap());
   private DrawCallbacks delegate;
   private UiHideOverlay uiOverlay;

   public void initialize(FreezeFrameBridge.Deps deps) {
      this.deps = deps;
      this.config = deps.config;
      this.uiOverlay = new UiHideOverlay();
      deps.overlayManager.add(this.uiOverlay);
      deps.clientThread.invokeLater(this::interceptDrawCalls);
   }

   public void cleanup() {
      if (!this.cleanedUp) {
         this.cleanedUp = true;
         FreezeFrameBridge.Deps d = this.deps;
         UiHideOverlay ov = this.uiOverlay;
         if (ov != null) {
            d.overlayManager.remove(ov);
            this.uiOverlay = null;
         }

         d.clientThread.invokeLater(() -> {
            if (d.client.getDrawCallbacks() == this) {
               d.client.setDrawCallbacks(this.delegate);
            }

            this.delegate = null;
            if (this.frozen) {
               this.restoreAnimations();
               this.frozen = false;
               this.snapshots.clear();
               d.particlePlugin.setFrozen(false);
            }

            d.client.setSkyboxColor(0);
         });
      }
   }

   public void onBeforeRender() {
      if (!this.cleanedUp) {
         Client client = this.deps.client;
         if (FreezeFrameFlags.TRANSPARENT_SKYBOX_CAPTURE) {
            client.setSkyboxColor(0);
         } else {
            Color sky = this.config.skyboxColor();
            if (sky != null) {
               client.setSkyboxColor(sky.getRGB());
            }
         }

         if (this.frozen && this.config.freezeAnimations()) {
            this.pinAnimations();
         }

      }
   }

   public void onClientTick() {
      if (!this.cleanedUp) {
         if (this.delegate != null && this.deps.client.getDrawCallbacks() != this) {
            this.interceptDrawCalls();
         }

         if (this.frozen && this.config.freezeAnimations()) {
            this.snapshotMissing();
         }

      }
   }

   public void onConfigChanged(ConfigChanged event) {
   }

   public void onPluginChanged(PluginChanged event) {
      if (!this.cleanedUp) {
         this.deps.clientThread.invokeLater(() -> {
            if (!this.cleanedUp) {
               if (event.isLoaded()) {
                  this.interceptDrawCalls();
               } else if (this.deps.client.getDrawCallbacks() == null) {
                  this.delegate = null;
               }

            }
         });
      }
   }

   public void toggleFreeze() {
      if (!this.cleanedUp) {
         this.deps.clientThread.invokeLater(() -> {
            if (!this.cleanedUp) {
               this.frozen = !this.frozen;
               this.deps.particlePlugin.setFrozen(this.frozen);
               if (this.frozen) {
                  this.captureAllSnapshots();
                  log.info("Freeze Frame: ON ({} actors snapshotted)", this.snapshots.size());
               } else {
                  this.restoreAnimations();
                  this.snapshots.clear();
                  log.info("Freeze Frame: OFF");
               }

            }
         });
      }
   }

   public void capture() {
      if (!this.cleanedUp) {
         this.deps.clientThread.invokeLater(() -> {
            if (!this.cleanedUp) {
               if (this.deps.client.getGameState() != GameState.LOGGED_IN) {
                  log.debug("Capture skipped — not logged in");
               } else if (FreezeFrameFlags.TRANSPARENT_SKYBOX_CAPTURE) {
                  log.debug("Capture already in progress");
               } else {
                  String timestamp;
                  synchronized(TIME_FORMAT) {
                     timestamp = TIME_FORMAT.format(new Date());
                  }

                  FreezeFrameFlags.TRANSPARENT_SKYBOX_CAPTURE = true;
                  this.deps.drawManager.requestNextFrameListener((img) -> {
                     try {
                        this.saveCapture(img, timestamp, "transparent");
                     } finally {
                        FreezeFrameFlags.TRANSPARENT_SKYBOX_CAPTURE = false;
                     }

                     this.deps.notifier.notify("Freeze Frame: captured " + timestamp, MessageType.INFO);
                  });
               }
            }
         });
      }
   }

   private boolean interceptDrawCalls() {
      assert this.deps.client.isClientThread();

      if (this.deps.client.getGameState() == GameState.LOADING) {
         return false;
      } else if (!this.deps.client.isGpu()) {
         return false;
      } else {
         DrawCallbacks current = this.deps.client.getDrawCallbacks();
         if (current != null && current != this) {
            this.delegate = current;
            this.deps.client.setDrawCallbacks(this);
            return true;
         } else {
            return false;
         }
      }
   }

   public void draw(Projection projection, Scene scene, Renderable renderable, int orientation, int x, int y, int z, long hash) {
      if (this.delegate != null) {
         if (this.shouldRender(renderable) && !this.culledByRadius(renderable, x, z)) {
            this.delegate.draw(projection, scene, renderable, orientation, x, y, z, hash);
         } else {
            Model model = renderable instanceof Model ? (Model)renderable : renderable.getModel();
            if (model == null) {
               return;
            }

            if (model != renderable) {
               renderable.setModelHeight(model.getModelHeight());
            }

            model.calculateBoundsCylinder();
            if (projection instanceof IntProjection) {
               IntProjection p = (IntProjection)projection;
               if (!this.isVisible(model, p.getPitchSin(), p.getPitchCos(), p.getYawSin(), p.getYawCos(), x - p.getCameraX(), y - p.getCameraY(), z - p.getCameraZ())) {
                  return;
               }
            }

            this.deps.client.checkClickbox(projection, model, orientation, x, y, z, hash);
         }

      }
   }

   private boolean culledByRadius(Renderable r, int x, int z) {
      int radius = this.config.cullRadius();
      if (radius <= 0) {
         return false;
      } else if (r instanceof Player && (Player)r == this.deps.client.getLocalPlayer()) {
         return false;
      } else {
         int dx = x - this.deps.client.getCameraX();
         int dz = z - this.deps.client.getCameraZ();
         int radiusLocal = radius << 7;
         return (long)dx * (long)dx + (long)dz * (long)dz < (long)radiusLocal * (long)radiusLocal;
      }
   }

   private boolean shouldRender(Renderable r) {
      Client client = this.deps.client;
      if (r instanceof Player) {
         Player p = (Player)r;
         Player local = client.getLocalPlayer();
         if (p == local) {
            return !this.config.hideLocalPlayer();
         } else {
            return !this.config.hideOthers();
         }
      } else if (r instanceof NPC) {
         return !this.config.hideNPCs();
      } else if (r instanceof Projectile) {
         return !this.config.hideProjectiles();
      } else if (r instanceof RuneLiteObject) {
         return !this.config.hideRuneLiteObjects();
      } else if (r instanceof GraphicsObject) {
         return !this.config.hideGraphicsObjects();
      } else if (r instanceof TileItem) {
         return !this.config.hideGroundItems();
      } else if (!(r instanceof Scene)) {
         if (!(r instanceof Model) && !(r instanceof ModelData) && !(r instanceof DynamicObject)) {
            return true;
         } else {
            return !this.config.hideScenery();
         }
      } else if (!this.config.hideWorldEntities()) {
         return true;
      } else {
         Scene s = (Scene)r;
         WorldView wv = client.getTopLevelWorldView();
         if (wv == null) {
            return true;
         } else {
            WorldEntity we = (WorldEntity)wv.worldEntities().byIndex(s.getWorldViewId());
            return we == null || we.getOwnerType() != 1;
         }
      }
   }

   public void drawScenePaint(Scene scene, SceneTilePaint paint, int plane, int tileX, int tileZ) {
      if (this.delegate != null && !this.config.hideTerrain()) {
         this.delegate.drawScenePaint(scene, paint, plane, tileX, tileZ);
      }

   }

   public void drawSceneTileModel(Scene scene, SceneTileModel model, int tileX, int tileZ) {
      if (this.delegate != null && !this.config.hideTerrain()) {
         this.delegate.drawSceneTileModel(scene, model, tileX, tileZ);
      }

   }

   public void draw(int overlayColor) {
      if (this.delegate != null) {
         this.delegate.draw(overlayColor);
      }

   }

   public void drawScene(double cameraX, double cameraY, double cameraZ, double cameraPitch, double cameraYaw, int plane) {
      if (this.delegate != null) {
         this.delegate.drawScene(cameraX, cameraY, cameraZ, cameraPitch, cameraYaw, plane);
      }

   }

   public void postDrawScene() {
      if (this.delegate != null) {
         this.delegate.postDrawScene();
      }

   }

   public void animate(Texture texture, int diff) {
      if (this.delegate != null) {
         this.delegate.animate(texture, diff);
      }

   }

   public void loadScene(Scene scene) {
      if (this.delegate != null) {
         this.delegate.loadScene(scene);
      }

   }

   public void swapScene(Scene scene) {
      if (this.delegate != null) {
         this.delegate.swapScene(scene);
      }

   }

   public void loadScene(WorldView worldView, Scene scene) {
      if (this.delegate != null) {
         this.delegate.loadScene(worldView, scene);
      }

   }

   public void despawnWorldView(WorldView worldView) {
      if (this.delegate != null) {
         this.delegate.despawnWorldView(worldView);
      }

   }

   public boolean tileInFrustum(Scene scene, float pitchSin, float pitchCos, float yawSin, float yawCos, int cameraX, int cameraY, int cameraZ, int plane, int msx, int msy) {
      return this.delegate != null ? this.delegate.tileInFrustum(scene, pitchSin, pitchCos, yawSin, yawCos, cameraX, cameraY, cameraZ, plane, msx, msy) : true;
   }

   public boolean zoneInFrustum(int zoneX, int zoneZ, int maxY, int minY) {
      return this.delegate != null ? this.delegate.zoneInFrustum(zoneX, zoneZ, maxY, minY) : false;
   }

   public void preSceneDraw(Scene scene, float cameraX, float cameraY, float cameraZ, float cameraPitch, float cameraYaw, int minLevel, int level, int maxLevel, Set<Integer> hideRoofIds) {
      if (this.delegate != null) {
         this.delegate.preSceneDraw(scene, cameraX, cameraY, cameraZ, cameraPitch, cameraYaw, minLevel, level, maxLevel, hideRoofIds);
      }

   }

   public void postSceneDraw(Scene scene) {
      if (this.delegate != null) {
         this.delegate.postSceneDraw(scene);
      }

   }

   public void drawPass(Projection entityProjection, Scene scene, int pass) {
      if (this.delegate != null) {
         this.delegate.drawPass(entityProjection, scene, pass);
      }

   }

   public void drawZoneOpaque(Projection entityProjection, Scene scene, int zx, int zz) {
      if (this.delegate != null && !this.config.hideTerrain()) {
         this.delegate.drawZoneOpaque(entityProjection, scene, zx, zz);
      }

   }

   public void drawZoneAlpha(Projection entityProjection, Scene scene, int level, int zx, int zz) {
      if (this.delegate != null && !this.config.hideTerrain()) {
         this.delegate.drawZoneAlpha(entityProjection, scene, level, zx, zz);
      }

   }

   public void drawDynamic(Projection worldProjection, Scene scene, TileObject tileObject, Renderable r, Model m, int orient, int x, int y, int z) {
      if (this.delegate != null) {
         if (this.shouldRender(r)) {
            if (!this.culledByRadius(r, x, z)) {
               this.delegate.drawDynamic(worldProjection, scene, tileObject, r, m, orient, x, y, z);
            }
         }
      }
   }

   public void drawDynamic(int renderThreadId, Projection worldProjection, Scene scene, TileObject tileObject, Renderable r, Model m, int orient, int x, int y, int z) {
      if (this.delegate != null) {
         if (this.shouldRender(r)) {
            if (!this.culledByRadius(r, x, z)) {
               this.delegate.drawDynamic(renderThreadId, worldProjection, scene, tileObject, r, m, orient, x, y, z);
            }
         }
      }
   }

   public void drawTemp(Projection worldProjection, Scene scene, GameObject gameObject, Model m, int orient, int x, int y, int z) {
      if (this.delegate != null) {
         Renderable r = gameObject.getRenderable();
         if (r == null || this.shouldRender(r)) {
            if (r == null || !this.culledByRadius(r, x, z)) {
               this.delegate.drawTemp(worldProjection, scene, gameObject, m, orient, x, y, z);
            }
         }
      }
   }

   public void invalidateZone(Scene scene, int zx, int zz) {
      if (this.delegate != null) {
         this.delegate.invalidateZone(scene, zx, zz);
      }

   }

   private boolean isVisible(Model model, float pitchSin, float pitchCos, float yawSin, float yawCos, int x, int y, int z) {
      int xzMag = model.getXYZMag();
      int bottomY = model.getBottomY();
      int zoom = this.deps.client.get3dZoom();
      int modelHeight = model.getModelHeight();
      int clipMidX2 = this.deps.client.getRasterizer3D_clipMidX2();
      int clipNegMidX = this.deps.client.getRasterizer3D_clipNegativeMidX();
      int clipNegMidY = this.deps.client.getRasterizer3D_clipNegativeMidY();
      int clipMidY2 = this.deps.client.getRasterizer3D_clipMidY2();
      float var11 = yawCos * (float)z - yawSin * (float)x;
      float depth = pitchSin * (float)y + pitchCos * var11 + pitchCos * (float)xzMag;
      if (depth > 50.0F) {
         float rx = (float)z * yawSin + yawCos * (float)x;
         if ((rx - (float)xzMag) * (float)zoom / depth < (float)clipMidX2 && (rx + (float)xzMag) * (float)zoom / depth > (float)clipNegMidX) {
            float ry = pitchCos * (float)y - var11 * pitchSin;
            float yheight = pitchSin * (float)xzMag;
            float ybottom = pitchCos * (float)bottomY + yheight;
            if ((ry + ybottom) * (float)zoom / depth > (float)clipNegMidY) {
               float ytop = pitchCos * (float)modelHeight + yheight;
               return (ry - ytop) * (float)zoom / depth < (float)clipMidY2;
            }
         }
      }

      return false;
   }

   private void captureAllSnapshots() {
      this.snapshots.clear();
      Iterator var1 = this.deps.client.getPlayers().iterator();

      while(var1.hasNext()) {
         Player p = (Player)var1.next();
         this.snapshotActor(p);
      }

      var1 = this.deps.client.getNpcs().iterator();

      while(var1.hasNext()) {
         NPC n = (NPC)var1.next();
         this.snapshotActor(n);
      }

   }

   private void snapshotMissing() {
      Iterator var1 = this.deps.client.getPlayers().iterator();

      while(var1.hasNext()) {
         Player p = (Player)var1.next();
         if (!this.snapshots.containsKey(p)) {
            this.snapshotActor(p);
         }
      }

      var1 = this.deps.client.getNpcs().iterator();

      while(var1.hasNext()) {
         NPC n = (NPC)var1.next();
         if (!this.snapshots.containsKey(n)) {
            this.snapshotActor(n);
         }
      }

   }

   private void snapshotActor(Actor a) {
      if (a != null) {
         AnimSnapshot s = new AnimSnapshot();
         s.animation = a.getAnimation();
         s.animationFrame = a.getAnimationFrame();
         s.poseAnimation = a.getPoseAnimation();
         s.poseAnimationFrame = a.getPoseAnimationFrame();
         s.origIdlePose = a.getIdlePoseAnimation();
         s.origWalk = a.getWalkAnimation();
         s.origRun = a.getRunAnimation();
         s.origIdleRotateLeft = a.getIdleRotateLeft();
         s.origIdleRotateRight = a.getIdleRotateRight();
         s.origWalkRotateLeft = a.getWalkRotateLeft();
         s.origWalkRotateRight = a.getWalkRotateRight();
         s.origWalkRotate180 = a.getWalkRotate180();

         try {
            Iterator var3 = a.getSpotAnims().iterator();

            while(var3.hasNext()) {
               ActorSpotAnim sa = (ActorSpotAnim)var3.next();
               s.spotAnimFrames.put(sa, sa.getFrame());
            }
         } catch (Exception var5) {
         }

         this.snapshots.put(a, s);
      }
   }

   private void pinAnimations() {
      synchronized(this.snapshots) {
         Iterator var2 = this.snapshots.entrySet().iterator();

         while(true) {
            Actor a;
            AnimSnapshot s;
            do {
               if (!var2.hasNext()) {
                  return;
               }

               Map.Entry<Actor, AnimSnapshot> entry = (Map.Entry)var2.next();
               a = (Actor)entry.getKey();
               s = (AnimSnapshot)entry.getValue();
            } while(a == null);

            a.setAnimation(s.animation);
            a.setAnimationFrame(s.animationFrame);
            a.setPoseAnimation(s.poseAnimation);
            a.setPoseAnimationFrame(s.poseAnimationFrame);
            a.setIdlePoseAnimation(s.poseAnimation);
            a.setWalkAnimation(s.poseAnimation);
            a.setRunAnimation(s.poseAnimation);
            a.setIdleRotateLeft(s.poseAnimation);
            a.setIdleRotateRight(s.poseAnimation);
            a.setWalkRotateLeft(s.poseAnimation);
            a.setWalkRotateRight(s.poseAnimation);
            a.setWalkRotate180(s.poseAnimation);
            Iterator var6 = s.spotAnimFrames.entrySet().iterator();

            while(var6.hasNext()) {
               Map.Entry<ActorSpotAnim, Integer> sa = (Map.Entry)var6.next();
               ((ActorSpotAnim)sa.getKey()).setFrame((Integer)sa.getValue());
            }
         }
      }
   }

   private void restoreAnimations() {
      synchronized(this.snapshots) {
         Iterator var2 = this.snapshots.entrySet().iterator();

         while(var2.hasNext()) {
            Map.Entry<Actor, AnimSnapshot> entry = (Map.Entry)var2.next();
            Actor a = (Actor)entry.getKey();
            AnimSnapshot s = (AnimSnapshot)entry.getValue();
            if (a != null) {
               a.setIdlePoseAnimation(s.origIdlePose);
               a.setWalkAnimation(s.origWalk);
               a.setRunAnimation(s.origRun);
               a.setIdleRotateLeft(s.origIdleRotateLeft);
               a.setIdleRotateRight(s.origIdleRotateRight);
               a.setWalkRotateLeft(s.origWalkRotateLeft);
               a.setWalkRotateRight(s.origWalkRotateRight);
               a.setWalkRotate180(s.origWalkRotate180);
            }
         }

      }
   }

   private void saveCapture(Image img, String timestamp, String variant) {
      BufferedImage frame = this.config.captureIncludeFrame() ? this.deps.imageCapture.addClientFrame(img) : ImageUtil.bufferedImageFromImage(img);
      this.deps.executor.submit(() -> {
         File dir = this.resolveExportDir();
         if (!dir.exists() && !dir.mkdirs()) {
            log.error("Freeze Frame: could not create export dir {}", dir);
         } else {
            File out = new File(dir, "freeze_" + timestamp + "_" + variant + ".png");

            try {
               ImageIO.write(frame, "PNG", out);
               log.info("Freeze Frame: wrote {}", out);
            } catch (IOException var7) {
               IOException ex = var7;
               log.error("Freeze Frame: failed to write {}", out, ex);
            }

         }
      });
   }

   private File resolveExportDir() {
      String configured = this.config.exportDirectory();
      return configured != null && !configured.trim().isEmpty() ? new File(configured.trim()) : new File(RuneLite.SCREENSHOT_DIR, "FreezeFrame");
   }

   private static final class AnimSnapshot {
      int animation;
      int animationFrame;
      int poseAnimation;
      int poseAnimationFrame;
      int origIdlePose;
      int origWalk;
      int origRun;
      int origIdleRotateLeft;
      int origIdleRotateRight;
      int origWalkRotateLeft;
      int origWalkRotateRight;
      int origWalkRotate180;
      final HashMap<ActorSpotAnim, Integer> spotAnimFrames = new HashMap();
   }

   private final class UiHideOverlay extends Overlay {
      UiHideOverlay() {
         this.setPosition(OverlayPosition.DYNAMIC);
         this.setLayer(OverlayLayer.ALWAYS_ON_TOP);
         this.setPriority(OverlayPriority.HIGHEST);
      }

      public Dimension render(Graphics2D g) {
         if (FreezeFrameBridgeImpl.this.deps != null && FreezeFrameBridgeImpl.this.config.hideUI() && FreezeFrameBridgeImpl.this.deps.client.isGpu()) {
            Rectangle bounds = g.getClipBounds();
            if (bounds != null) {
               g.setBackground(FreezeFrameBridgeImpl.TRANSPARENT);
               g.clearRect(0, 0, bounds.width, bounds.height);
               g.setColor(FreezeFrameBridgeImpl.TRANSPARENT);
               g.fillRect(0, 0, bounds.width, bounds.height);
            }
         }

         return null;
      }
   }
}
