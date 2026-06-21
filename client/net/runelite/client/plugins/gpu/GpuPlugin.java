package net.runelite.client.plugins.gpu;

import com.google.common.base.Stopwatch;
import com.google.common.primitives.Ints;
import com.google.inject.Provides;
import io.sentry.Breadcrumb;
import io.sentry.Sentry;
import io.sentry.SentryLevel;
import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.GraphicsConfiguration;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import javax.inject.Inject;
import javax.swing.SwingUtilities;
import net.runelite.api.BufferProvider;
import net.runelite.api.Client;
import net.runelite.api.FloatProjection;
import net.runelite.api.GameObject;
import net.runelite.api.GameState;
import net.runelite.api.Model;
import net.runelite.api.ObjectComposition;
import net.runelite.api.Projection;
import net.runelite.api.Renderable;
import net.runelite.api.Scene;
import net.runelite.api.TextureProvider;
import net.runelite.api.TileObject;
import net.runelite.api.WorldEntity;
import net.runelite.api.WorldView;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.PostClientTick;
import net.runelite.api.hooks.DrawCallbacks;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.callback.RenderCallbackManager;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.PluginInstantiationException;
import net.runelite.client.plugins.PluginManager;
import net.runelite.client.plugins.freezeframe.FreezeFrameFlags;
import net.runelite.client.plugins.gpu.config.AntiAliasingMode;
import net.runelite.client.plugins.gpu.config.UIScalingMode;
import net.runelite.client.plugins.gpu.template.Template;
import net.runelite.client.ui.ClientUI;
import net.runelite.client.ui.DrawManager;
import net.runelite.rlawt.AWTContext;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL33C;
import org.lwjgl.opengl.GL43C;
import org.lwjgl.opengl.GL45C;
import org.lwjgl.opengl.GLCapabilities;
import org.lwjgl.opengl.GLUtil;
import org.lwjgl.system.Callback;
import org.lwjgl.system.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@PluginDescriptor(
   name = "GPU",
   description = "Offloads rendering to GPU",
   tags = {"fog", "draw distance"},
   loadInSafeMode = false,
   enabledByDefault = true
)
public class GpuPlugin extends Plugin implements DrawCallbacks {
   private static final Logger log = LoggerFactory.getLogger(GpuPlugin.class);
   static final int MAX_DISTANCE = 184;
   static final int MAX_FOG_DEPTH = 100;
   static final int SCENE_OFFSET = 40;
   private static final int UNIFORM_BUFFER_SIZE = 20;
   private static final int NUM_ZONES = 23;
   private static final int MAX_WORLDVIEWS = 4096;
   private static final long TEMP_MODEL_SORT_CONTEXT_LOG_INTERVAL_MILLIS = 60000L;
   private static long tempModelSortContextBreadcrumbMillis;
   private static int tempModelSortContextSuppressedLogs;
   private static long tempModelExceptionLogMillis;
   private static int tempModelExceptionSuppressedLogs;
   @Inject
   private Client client;
   @Inject
   private ClientUI clientUI;
   @Inject
   private ClientThread clientThread;
   @Inject
   private GpuPluginConfig config;
   @Inject
   private TextureManager textureManager;
   @Inject
   private RegionManager regionManager;
   @Inject
   private DrawManager drawManager;
   @Inject
   private PluginManager pluginManager;
   @Inject
   private RenderCallbackManager renderCallbackManager;
   private Canvas canvas;
   private AWTContext awtContext;
   private Callback debugCallback;
   private boolean lwjglInitted = false;
   private GLCapabilities glCapabilities;
   static final Shader PROGRAM = (new Shader()).add(35633, "vert.glsl").add(35632, "frag.glsl");
   static final Shader UI_PROGRAM = (new Shader()).add(35633, "vertui.glsl").add(35632, "fragui.glsl");
   static int glProgram;
   private int glUiProgram;
   private int interfaceTexture;
   private int interfacePbo;
   private int vaoUiHandle;
   private int vboUiHandle;
   private int fboScene;
   private int fboCapture = -1;
   private int rboCapture = 0;
   private int captureFboWidth;
   private int captureFboHeight;
   private boolean sceneFboValid;
   private int rboColorBuffer;
   private int rboDepthBuffer;
   private int textureArrayId;
   private final GLBuffer glUniformBuffer = new GLBuffer("uniform buffer");
   private int lastCanvasWidth;
   private int lastCanvasHeight;
   private int lastStretchedCanvasWidth;
   private int lastStretchedCanvasHeight;
   private AntiAliasingMode lastAntiAliasingMode;
   private int lastAnisotropicFilteringLevel = -1;
   private GpuFloatBuffer uniformBuffer;
   private int cameraYaw;
   private int cameraPitch;
   private float cameraYawRad;
   private float cameraPitchRad;
   private VAOList vaoO;
   private VAOList vaoA;
   private VAOList vaoPO;
   private SceneUploader clientUploader;
   private SceneUploader mapUploader;
   private FacePrioritySorter facePrioritySorter;
   private SceneContext root;
   private SceneContext[] subs;
   private Zone[][] nextZones;
   private Map<Integer, Integer> nextRoofChanges;
   private int uniUseFog;
   private int uniFogColor;
   private int uniFogDepth;
   private int uniDrawDistance;
   private int uniExpandedMapLoadingChunks;
   private int uniSmoothBanding;
   private int uniWorldProj;
   private static int uniEntityProj;
   static int uniEntityTint;
   private int uniBrightness;
   private int uniTex;
   private int uniTexSourceDimensions;
   private int uniTexTargetDimensions;
   private int uniUiAlphaOverlay;
   private int uniTextures;
   private int uniTextureAnimations;
   private int uniBlockMain;
   private int uniTextureLightMode;
   private int uniTick;
   private int uniColorblindIntensity;
   private int uniUiColorblindIntensity;
   static int uniBase;
   private static Projection lastProjection;
   static final Shader PARTICLE_PROGRAM = (new Shader()).add(35633, "particle_vert.glsl").add(35632, "particle_frag.glsl");
   private int glParticleProgram;
   private VAOList vaoParticles;
   private Plugin cachedParticlePlugin;
   private Method getRequiredBufferSizeMethod;
   private Method getParticleSizeMethod;
   private Method renderParticlesToBufferMethod;
   private boolean particleTexturesInitialized;
   private int uniParticleWorldProj;
   private int uniParticleTex64;
   private int uniParticleTex128;
   private int uniParticleTex256;
   private int uniParticleTex1024;
   private int uniParticleBrightness;
   private int uniParticleCamPos;
   private Method bindParticleTexturesMethod;
   private static final int ALPHA_ZSORT_CLOSE = 2048;

   SceneContext context(Scene scene) {
      int wvid = scene.getWorldViewId();
      return wvid == 0 ? this.root : this.subs[wvid];
   }

   SceneContext context(WorldView wv) {
      int wvid = wv.getId();
      return wvid == 0 ? this.root : this.subs[wvid];
   }

   protected void startUp() {
      this.root = new SceneContext(23, 23);
      this.subs = new SceneContext[4096];
      this.clientUploader = new SceneUploader(this.renderCallbackManager);
      this.mapUploader = new SceneUploader(this.renderCallbackManager);
      this.facePrioritySorter = new FacePrioritySorter(this.clientUploader);
      this.clientThread.invoke(() -> {
         try {
            this.fboScene = -1;
            this.lastAnisotropicFilteringLevel = -1;
            AWTContext.loadNatives();
            this.canvas = this.client.getCanvas();
            synchronized(this.canvas.getTreeLock()) {
               if (!this.canvas.isValid()) {
                  return false;
               }

               this.awtContext = new AWTContext(this.canvas);
               this.awtContext.configurePixelFormat(0, 0, 0);
            }

            this.awtContext.createGLContext();
            this.canvas.setIgnoreRepaint(true);
            Configuration.SHARED_LIBRARY_EXTRACT_DIRECTORY.set("lwjgl-rl");
            this.glCapabilities = GL.createCapabilities();
            log.info("Using device: {}", GL33C.glGetString(7937));
            log.info("Using driver: {}", GL33C.glGetString(7938));
            if (!this.glCapabilities.OpenGL33) {
               throw new RuntimeException("OpenGL 3.3 is required but not available");
            }

            this.lwjglInitted = true;
            this.checkGLErrors();
            if (log.isDebugEnabled() && this.glCapabilities.glDebugMessageControl != 0L) {
               this.debugCallback = GLUtil.setupDebugMessageCallback();
               if (this.debugCallback != null) {
                  GL43C.glDebugMessageControl(33350, 33361, 4352, 131185, false);
                  GL43C.glDebugMessageControl(33350, 33360, 4352, 131154, false);
               }
            }

            this.setupSyncMode();
            this.initBuffers();
            this.initVao();
            this.initProgram();
            this.initInterfaceTexture();
            if (this.glCapabilities.OpenGL45) {
               GL45C.glClipControl(36001, 37727);
            }

            this.client.setDrawCallbacks(this);
            this.client.setGpuFlags(1 | (this.config.removeVertexSnapping() ? 8 : 0) | 16);
            this.client.setExpandedMapLoading(this.config.expandedMapLoadingZones());
            this.client.resizeCanvas();
            this.lastCanvasWidth = this.lastCanvasHeight = -1;
            this.lastStretchedCanvasWidth = this.lastStretchedCanvasHeight = -1;
            this.lastAntiAliasingMode = null;
            this.textureArrayId = -1;
            if (this.client.getGameState() == GameState.LOGGED_IN) {
               this.startupWorldLoad();
            }

            this.checkGLErrors();
         } catch (Throwable var4) {
            Throwable e = var4;
            log.error("Error starting GPU plugin", e);
            SwingUtilities.invokeLater(() -> {
               try {
                  this.pluginManager.setPluginEnabled(this, false);
                  this.pluginManager.stopPlugin(this);
               } catch (PluginInstantiationException var2) {
                  PluginInstantiationException ex = var2;
                  log.error("error stopping plugin", ex);
               }

            });
            this.shutDown();
         }

         return true;
      });
   }

   private void startupWorldLoad() {
      WorldView root = this.client.getTopLevelWorldView();
      Scene scene = root.getScene();
      this.loadScene(root, scene);
      this.swapScene(scene);
      Iterator var3 = root.worldEntities().iterator();

      while(var3.hasNext()) {
         WorldEntity subEntity = (WorldEntity)var3.next();
         WorldView sub = subEntity.getWorldView();
         log.debug("WorldView loading: {}", sub.getId());
         this.loadSubScene(sub, sub.getScene());
         this.swapSub(sub.getScene());
      }

   }

   protected void shutDown() {
      this.clientThread.invoke(() -> {
         this.clearParticlePluginCache();
         this.client.setGpuFlags(0);
         this.client.setDrawCallbacks((DrawCallbacks)null);
         this.client.setUnlockedFps(false);
         this.client.setExpandedMapLoading(0);
         if (this.lwjglInitted) {
            if (this.textureArrayId != -1) {
               this.textureManager.freeTextureArray(this.textureArrayId);
               this.textureArrayId = -1;
            }

            this.root.free();
            this.shutdownInterfaceTexture();
            this.shutdownProgram();
            this.shutdownVao();
            this.shutdownBuffers();
            this.shutdownFbo();
         }

         if (this.awtContext != null) {
            this.awtContext.destroy();
            this.awtContext = null;
         }

         if (this.debugCallback != null) {
            this.debugCallback.free();
            this.debugCallback = null;
         }

         this.glCapabilities = null;
         this.client.resizeCanvas();
      });
   }

   @Provides
   GpuPluginConfig provideConfig(ConfigManager configManager) {
      return (GpuPluginConfig)configManager.getConfig(GpuPluginConfig.class);
   }

   @Subscribe
   public void onConfigChanged(ConfigChanged configChanged) {
      if (configChanged.getGroup().equals("gpu")) {
         if (!configChanged.getKey().equals("unlockFps") && !configChanged.getKey().equals("vsyncMode") && !configChanged.getKey().equals("fpsTarget")) {
            if (configChanged.getKey().equals("expandedMapLoadingChunks")) {
               this.clientThread.invokeLater(() -> {
                  this.client.setExpandedMapLoading(this.config.expandedMapLoadingZones());
                  if (this.client.getGameState() == GameState.LOGGED_IN) {
                     this.client.setGameState(GameState.LOADING);
                  }

               });
            } else if (configChanged.getKey().equals("removeVertexSnapping")) {
               log.debug("Toggle {}", configChanged.getKey());
               this.client.setGpuFlags(1 | (this.config.removeVertexSnapping() ? 8 : 0) | 16);
            } else if (configChanged.getKey().equals("uiScalingMode") || configChanged.getKey().equals("colorBlindMode")) {
               this.clientThread.invokeLater(() -> {
                  log.debug("Recompiling shaders");
                  this.shutdownProgram();
                  this.initProgram();
               });
            }
         } else {
            log.debug("Rebuilding sync mode");
            this.clientThread.invokeLater(this::setupSyncMode);
         }
      }

   }

   private void setupSyncMode() {
      boolean unlockFps = this.config.unlockFps();
      this.client.setUnlockedFps(unlockFps);
      GpuPluginConfig.SyncMode syncMode = unlockFps ? this.config.syncMode() : GpuPluginConfig.SyncMode.OFF;
      int swapInterval = 0;
      switch (syncMode) {
         case ON:
            swapInterval = 1;
            break;
         case OFF:
            swapInterval = 0;
            break;
         case ADAPTIVE:
            swapInterval = -1;
      }

      int actualSwapInterval = this.awtContext.setSwapInterval(swapInterval);
      if (actualSwapInterval != swapInterval) {
         log.info("unsupported swap interval {}, got {}", Integer.valueOf(swapInterval), actualSwapInterval);
      }

      this.client.setUnlockedFpsTarget(actualSwapInterval == 0 ? this.config.fpsTarget() : 0);
      this.checkGLErrors();
   }

   private Template createTemplate() {
      Template template = new Template();
      template.add((key) -> {
         switch (key) {
            case "texture_config":
               return "#define TEXTURE_COUNT 256\n";
            case "sampling_mode":
               return "#define SAMPLING_MODE " + this.config.uiScalingMode().ordinal() + "\n";
            case "colorblind_mode":
               return "#define COLORBLIND_MODE " + this.config.colorBlindMode().ordinal() + "\n";
            default:
               return null;
         }
      });
      template.addInclude(GpuPlugin.class);
      return template;
   }

   private void initProgram() throws ShaderException {
      GL33C.glBindVertexArray(this.vaoUiHandle);
      Template template = this.createTemplate();
      glProgram = PROGRAM.compile(template);
      this.glUiProgram = UI_PROGRAM.compile(template);
      this.compileParticleProgram(template);
      GL33C.glBindVertexArray(0);
      this.initUniforms();
   }

   private void initUniforms() {
      this.uniWorldProj = GL33C.glGetUniformLocation(glProgram, "worldProj");
      uniEntityProj = GL33C.glGetUniformLocation(glProgram, "entityProj");
      uniEntityTint = GL33C.glGetUniformLocation(glProgram, "entityTint");
      this.uniSmoothBanding = GL33C.glGetUniformLocation(glProgram, "smoothBanding");
      this.uniBrightness = GL33C.glGetUniformLocation(glProgram, "brightness");
      this.uniUseFog = GL33C.glGetUniformLocation(glProgram, "useFog");
      this.uniFogColor = GL33C.glGetUniformLocation(glProgram, "fogColor");
      this.uniFogDepth = GL33C.glGetUniformLocation(glProgram, "fogDepth");
      this.uniDrawDistance = GL33C.glGetUniformLocation(glProgram, "drawDistance");
      this.uniExpandedMapLoadingChunks = GL33C.glGetUniformLocation(glProgram, "expandedMapLoadingChunks");
      this.uniTextureLightMode = GL33C.glGetUniformLocation(glProgram, "textureLightMode");
      this.uniTick = GL33C.glGetUniformLocation(glProgram, "tick");
      this.uniBlockMain = GL33C.glGetUniformBlockIndex(glProgram, "uniforms");
      this.uniTextures = GL33C.glGetUniformLocation(glProgram, "textures");
      this.uniTextureAnimations = GL33C.glGetUniformLocation(glProgram, "textureAnimations");
      uniBase = GL33C.glGetUniformLocation(glProgram, "base");
      this.uniColorblindIntensity = GL33C.glGetUniformLocation(glProgram, "colorblindIntensity");
      this.uniTex = GL33C.glGetUniformLocation(this.glUiProgram, "tex");
      this.uniTexTargetDimensions = GL33C.glGetUniformLocation(this.glUiProgram, "targetDimensions");
      this.uniTexSourceDimensions = GL33C.glGetUniformLocation(this.glUiProgram, "sourceDimensions");
      this.uniUiAlphaOverlay = GL33C.glGetUniformLocation(this.glUiProgram, "alphaOverlay");
      this.uniUiColorblindIntensity = GL33C.glGetUniformLocation(this.glUiProgram, "colorblindIntensity");
      this.initializeParticleUniforms();
   }

   private void shutdownProgram() {
      GL33C.glDeleteProgram(glProgram);
      glProgram = 0;
      GL33C.glDeleteProgram(this.glUiProgram);
      this.glUiProgram = 0;
      GL33C.glDeleteProgram(this.glParticleProgram);
      this.glParticleProgram = 0;
   }

   private void initVao() {
      this.vaoUiHandle = GL33C.glGenVertexArrays();
      this.vboUiHandle = GL33C.glGenBuffers();
      GL33C.glBindVertexArray(this.vaoUiHandle);
      FloatBuffer vboUiBuf = GpuFloatBuffer.allocateDirect(20);
      vboUiBuf.put(new float[]{1.0F, 1.0F, 0.0F, 1.0F, 0.0F, 1.0F, -1.0F, 0.0F, 1.0F, 1.0F, -1.0F, -1.0F, 0.0F, 0.0F, 1.0F, -1.0F, 1.0F, 0.0F, 0.0F, 0.0F});
      vboUiBuf.rewind();
      GL33C.glBindBuffer(34962, this.vboUiHandle);
      GL33C.glBufferData(34962, vboUiBuf, 35044);
      GL33C.glVertexAttribPointer(0, 3, 5126, false, 20, 0L);
      GL33C.glEnableVertexAttribArray(0);
      GL33C.glVertexAttribPointer(1, 2, 5126, false, 20, 12L);
      GL33C.glEnableVertexAttribArray(1);
      GL33C.glBindVertexArray(0);
      GL33C.glBindBuffer(34962, 0);
   }

   private void shutdownVao() {
      GL33C.glDeleteBuffers(this.vboUiHandle);
      this.vboUiHandle = 0;
      GL33C.glDeleteVertexArrays(this.vaoUiHandle);
      this.vaoUiHandle = 0;
   }

   private void initBuffers() {
      this.uniformBuffer = new GpuFloatBuffer(20);
      this.initGlBuffer(this.glUniformBuffer);
      Zone.initBuffer();
      this.vaoO = new VAOList();
      this.vaoA = new VAOList();
      this.vaoPO = new VAOList();
      this.vaoParticles = new VAOList();
   }

   private void initGlBuffer(GLBuffer glBuffer) {
      glBuffer.glBufferId = GL33C.glGenBuffers();
   }

   private void shutdownBuffers() {
      this.destroyGlBuffer(this.glUniformBuffer);
      this.uniformBuffer = null;
      Zone.freeBuffer();
      if (this.vaoO != null) {
         this.vaoO.free();
      }

      if (this.vaoA != null) {
         this.vaoA.free();
      }

      if (this.vaoPO != null) {
         this.vaoPO.free();
      }

      if (this.vaoParticles != null) {
         this.vaoParticles.free();
      }

      this.vaoO = this.vaoA = this.vaoPO = this.vaoParticles = null;
   }

   private void destroyGlBuffer(GLBuffer glBuffer) {
      if (glBuffer.glBufferId != -1) {
         GL33C.glDeleteBuffers(glBuffer.glBufferId);
         glBuffer.glBufferId = -1;
      }

      glBuffer.size = -1;
   }

   private void initInterfaceTexture() {
      this.interfacePbo = GL33C.glGenBuffers();
      this.interfaceTexture = GL33C.glGenTextures();
      GL33C.glBindTexture(3553, this.interfaceTexture);
      GL33C.glTexParameteri(3553, 10242, 33071);
      GL33C.glTexParameteri(3553, 10243, 33071);
      GL33C.glTexParameteri(3553, 10241, 9729);
      GL33C.glTexParameteri(3553, 10240, 9729);
      GL33C.glBindTexture(3553, 0);
   }

   private void shutdownInterfaceTexture() {
      GL33C.glDeleteBuffers(this.interfacePbo);
      GL33C.glDeleteTextures(this.interfaceTexture);
      this.interfaceTexture = -1;
   }

   private void initFbo(int width, int height, int aaSamples) {
      GraphicsConfiguration graphicsConfiguration = this.clientUI.getGraphicsConfiguration();
      AffineTransform transform = graphicsConfiguration.getDefaultTransform();
      width = this.getScaledValue(transform.getScaleX(), width);
      height = this.getScaledValue(transform.getScaleY(), height);
      if (aaSamples > 0) {
         GL33C.glEnable(32925);
      } else {
         GL33C.glDisable(32925);
      }

      this.fboScene = GL33C.glGenFramebuffers();
      GL33C.glBindFramebuffer(36160, this.fboScene);
      this.rboColorBuffer = GL33C.glGenRenderbuffers();
      GL33C.glBindRenderbuffer(36161, this.rboColorBuffer);
      GL33C.glRenderbufferStorageMultisample(36161, aaSamples, 32856, width, height);
      GL33C.glFramebufferRenderbuffer(36160, 36064, 36161, this.rboColorBuffer);
      this.rboDepthBuffer = GL33C.glGenRenderbuffers();
      GL33C.glBindRenderbuffer(36161, this.rboDepthBuffer);
      GL33C.glRenderbufferStorageMultisample(36161, aaSamples, 36012, width, height);
      GL33C.glFramebufferRenderbuffer(36160, 36096, 36161, this.rboDepthBuffer);
      int status = GL33C.glCheckFramebufferStatus(36160);
      if (status != 36053) {
         throw new RuntimeException("FBO is incomplete. status: " + status);
      } else {
         GL33C.glBindFramebuffer(36160, this.awtContext.getFramebuffer(false));
         GL33C.glBindRenderbuffer(36161, 0);
      }
   }

   private void shutdownFbo() {
      if (this.fboScene != -1) {
         GL33C.glDeleteFramebuffers(this.fboScene);
         this.fboScene = -1;
      }

      if (this.rboColorBuffer != 0) {
         GL33C.glDeleteRenderbuffers(this.rboColorBuffer);
         this.rboColorBuffer = 0;
      }

      if (this.rboDepthBuffer != 0) {
         GL33C.glDeleteRenderbuffers(this.rboDepthBuffer);
         this.rboDepthBuffer = 0;
      }

      if (this.fboCapture != -1) {
         GL33C.glDeleteFramebuffers(this.fboCapture);
         this.fboCapture = -1;
      }

      if (this.rboCapture != 0) {
         GL33C.glDeleteRenderbuffers(this.rboCapture);
         this.rboCapture = 0;
      }

   }

   private void ensureCaptureFbo(int width, int height) {
      if (this.fboCapture == -1 || this.captureFboWidth != width || this.captureFboHeight != height) {
         if (this.fboCapture != -1) {
            GL33C.glDeleteFramebuffers(this.fboCapture);
            GL33C.glDeleteRenderbuffers(this.rboCapture);
         }

         this.fboCapture = GL33C.glGenFramebuffers();
         GL33C.glBindFramebuffer(36160, this.fboCapture);
         this.rboCapture = GL33C.glGenRenderbuffers();
         GL33C.glBindRenderbuffer(36161, this.rboCapture);
         GL33C.glRenderbufferStorage(36161, 32856, width, height);
         GL33C.glFramebufferRenderbuffer(36160, 36064, 36161, this.rboCapture);
         this.captureFboWidth = width;
         this.captureFboHeight = height;
         GL33C.glBindFramebuffer(36160, this.awtContext.getFramebuffer(false));
         GL33C.glBindRenderbuffer(36161, 0);
      }
   }

   static void updateEntityProjection(Projection projection) {
      if (lastProjection != projection) {
         float[] p = projection instanceof FloatProjection ? ((FloatProjection)projection).getProjection() : Mat4.identity();
         GL33C.glUniformMatrix4fv(uniEntityProj, false, p);
         lastProjection = projection;
      }

   }

   public void preSceneDraw(Scene scene, float cameraX, float cameraY, float cameraZ, float cameraPitch, float cameraYaw, int minLevel, int level, int maxLevel, Set<Integer> hideRoofIds) {
      SceneContext ctx = this.context(scene);
      if (ctx != null) {
         ctx.cameraX = (int)cameraX;
         ctx.cameraY = (int)cameraY;
         ctx.cameraZ = (int)cameraZ;
         ctx.minLevel = minLevel;
         ctx.level = level;
         ctx.maxLevel = maxLevel;
         ctx.hideRoofIds = hideRoofIds;
      }

      if (scene.getWorldViewId() == 0) {
         this.cameraYawRad = cameraYaw;
         this.cameraPitchRad = cameraPitch;
         this.cameraYaw = this.client.getCameraYaw();
         this.cameraPitch = this.client.getCameraPitch();
         this.preSceneDrawToplevel(scene, cameraX, cameraY, cameraZ, cameraPitch, cameraYaw);
      } else {
         Scene toplevel = this.client.getScene();
         this.vaoO.addRange((Projection)null, toplevel);
         this.vaoPO.addRange((Projection)null, toplevel);
         GL33C.glUniform4i(uniEntityTint, scene.getOverrideHue(), scene.getOverrideSaturation(), scene.getOverrideLuminance(), scene.getOverrideAmount());
      }

   }

   private void preSceneDrawToplevel(Scene scene, float cameraX, float cameraY, float cameraZ, float cameraPitch, float cameraYaw) {
      scene.setDrawDistance(this.getDrawDistance());
      this.uniformBuffer.clear();
      this.uniformBuffer.put(cameraYaw).put(cameraPitch).put(cameraX).put(cameraY).put(cameraZ);
      this.uniformBuffer.flip();
      GL33C.glBindBuffer(35345, this.glUniformBuffer.glBufferId);
      GL33C.glBufferData(35345, this.uniformBuffer.getBuffer(), 35048);
      GL33C.glBindBuffer(35345, 0);
      this.uniformBuffer.clear();
      GL33C.glBindBufferBase(35345, 0, this.glUniformBuffer.glBufferId);
      this.checkGLErrors();
      int canvasHeight = this.client.getCanvasHeight();
      int canvasWidth = this.client.getCanvasWidth();
      int viewportHeight = this.client.getViewportHeight();
      int viewportWidth = this.client.getViewportWidth();
      AntiAliasingMode antiAliasingMode = this.config.antiAliasingMode();
      Dimension stretchedDimensions = this.client.getStretchedDimensions();
      int anisotropicFilteringLevel = this.client.isStretchedEnabled() ? stretchedDimensions.width : canvasWidth;
      int renderWidthOff = this.client.isStretchedEnabled() ? stretchedDimensions.height : canvasHeight;
      int renderHeightOff;
      int renderCanvasHeight;
      int renderViewportHeight;
      if (this.lastStretchedCanvasWidth != anisotropicFilteringLevel || this.lastStretchedCanvasHeight != renderWidthOff || this.lastAntiAliasingMode != antiAliasingMode) {
         this.shutdownFbo();
         GL33C.glBindFramebuffer(36160, this.awtContext.getFramebuffer(false));
         renderHeightOff = GL33C.glGetInteger(32937);
         renderCanvasHeight = GL33C.glGetInteger(36183);
         renderViewportHeight = renderHeightOff != 0 ? renderHeightOff : Math.min(antiAliasingMode.getSamples(), renderCanvasHeight);
         log.debug("AA samples: {}, max samples: {}, forced samples: {}", new Object[]{renderViewportHeight, renderCanvasHeight, renderHeightOff});
         this.initFbo(anisotropicFilteringLevel, renderWidthOff, renderViewportHeight);
         this.lastStretchedCanvasWidth = anisotropicFilteringLevel;
         this.lastStretchedCanvasHeight = renderWidthOff;
         this.lastAntiAliasingMode = antiAliasingMode;
      }

      GL33C.glBindFramebuffer(36009, this.fboScene);
      int sky = this.client.getSkyboxColor();
      float skyAlpha = FreezeFrameFlags.TRANSPARENT_SKYBOX_CAPTURE ? 0.0F : 1.0F;
      GL33C.glClearColor((float)(sky >> 16 & 255) / 255.0F, (float)(sky >> 8 & 255) / 255.0F, (float)(sky & 255) / 255.0F, skyAlpha);
      GL33C.glClearDepth(0.0);
      GL33C.glClear(16640);
      anisotropicFilteringLevel = this.config.anisotropicFilteringLevel();
      if (this.textureArrayId != -1 && this.lastAnisotropicFilteringLevel != anisotropicFilteringLevel) {
         this.textureManager.setAnisotropicFilteringLevel(this.textureArrayId, anisotropicFilteringLevel);
         this.lastAnisotropicFilteringLevel = anisotropicFilteringLevel;
      }

      renderWidthOff = this.client.getViewportXOffset();
      renderHeightOff = this.client.getViewportYOffset();
      renderCanvasHeight = canvasHeight;
      renderViewportHeight = viewportHeight;
      int renderViewportWidth = viewportWidth;
      if (this.client.isStretchedEnabled()) {
         Dimension dim = this.client.getStretchedDimensions();
         renderCanvasHeight = dim.height;
         double scaleFactorY = dim.getHeight() / (double)canvasHeight;
         double scaleFactorX = dim.getWidth() / (double)canvasWidth;
         int padding = true;
         renderViewportHeight = (int)Math.ceil(scaleFactorY * (double)renderViewportHeight) + 2;
         renderViewportWidth = (int)Math.ceil(scaleFactorX * (double)renderViewportWidth) + 2;
         renderHeightOff = (int)Math.floor(scaleFactorY * (double)renderHeightOff) - 1;
         renderWidthOff = (int)Math.floor(scaleFactorX * (double)renderWidthOff) - 1;
      }

      this.glDpiAwareViewport(renderWidthOff, renderCanvasHeight - renderViewportHeight - renderHeightOff, renderViewportWidth, renderViewportHeight);
      GL33C.glUseProgram(glProgram);
      int drawDistance = this.getDrawDistance();
      int fogDepth = this.config.fogDepth();
      GL33C.glUniform1i(this.uniUseFog, fogDepth > 0 ? 1 : 0);
      GL33C.glUniform4f(this.uniFogColor, (float)(sky >> 16 & 255) / 255.0F, (float)(sky >> 8 & 255) / 255.0F, (float)(sky & 255) / 255.0F, skyAlpha);
      GL33C.glUniform1i(this.uniFogDepth, fogDepth);
      GL33C.glUniform1i(this.uniDrawDistance, drawDistance * 128);
      GL33C.glUniform1i(this.uniExpandedMapLoadingChunks, this.client.getExpandedMapLoading());
      GL33C.glUniform1f(this.uniColorblindIntensity, (float)this.config.colorBlindIntensity());
      TextureProvider textureProvider = this.client.getTextureProvider();
      GL33C.glUniform1f(this.uniBrightness, (float)textureProvider.getBrightness());
      GL33C.glUniform1f(this.uniSmoothBanding, this.config.smoothBanding() ? 0.0F : 1.0F);
      GL33C.glUniform1f(this.uniTextureLightMode, this.config.brightTextures() ? 1.0F : 0.0F);
      if (this.client.getGameState() == GameState.LOGGED_IN) {
         GL33C.glUniform1i(this.uniTick, this.client.getGameCycle() & 127);
      }

      float[] projectionMatrix = Mat4.scale((float)this.client.getScale(), (float)this.client.getScale(), 1.0F);
      Mat4.mul(projectionMatrix, Mat4.projection((float)viewportWidth, (float)viewportHeight, 50.0F));
      Mat4.mul(projectionMatrix, Mat4.rotateX(cameraPitch));
      Mat4.mul(projectionMatrix, Mat4.rotateY(cameraYaw));
      Mat4.mul(projectionMatrix, Mat4.translate(-cameraX, -cameraY, -cameraZ));
      GL33C.glUniformMatrix4fv(this.uniWorldProj, false, projectionMatrix);
      projectionMatrix = Mat4.identity();
      GL33C.glUniformMatrix4fv(uniEntityProj, false, projectionMatrix);
      GL33C.glUniform4i(uniEntityTint, 0, 0, 0, 0);
      GL33C.glUniformBlockBinding(glProgram, this.uniBlockMain, 0);
      GL33C.glUniform1i(this.uniTextures, 1);
      GL33C.glEnable(2884);
      GL33C.glEnable(3042);
      GL33C.glBlendFuncSeparate(770, 771, 1, 1);
      GL33C.glDepthFunc(516);
      GL33C.glEnable(2929);
      this.checkGLErrors();
   }

   public void postSceneDraw(Scene scene) {
      if (scene.getWorldViewId() == 0) {
         this.renderParticles(scene);
         this.postDrawToplevel();
      } else {
         GL33C.glUniform4i(uniEntityTint, 0, 0, 0, 0);
      }

   }

   private void compileParticleProgram(Template template) {
      try {
         this.glParticleProgram = PARTICLE_PROGRAM.compile(template);
         log.debug("Particle shader compiled successfully, program ID: {}", this.glParticleProgram);
      } catch (ShaderException var3) {
         ShaderException e = var3;
         log.warn("Failed to compile particle shader, particles will use main shader fallback", e);
         this.glParticleProgram = 0;
      }

   }

   private void initializeParticleUniforms() {
      if (this.glParticleProgram != 0) {
         this.uniParticleWorldProj = GL33C.glGetUniformLocation(this.glParticleProgram, "worldProj");
         this.uniParticleTex64 = GL33C.glGetUniformLocation(this.glParticleProgram, "particleTex64");
         this.uniParticleTex128 = GL33C.glGetUniformLocation(this.glParticleProgram, "particleTex128");
         this.uniParticleTex256 = GL33C.glGetUniformLocation(this.glParticleProgram, "particleTex256");
         this.uniParticleTex1024 = GL33C.glGetUniformLocation(this.glParticleProgram, "particleTex1024");
         this.uniParticleBrightness = GL33C.glGetUniformLocation(this.glParticleProgram, "brightness");
         this.uniParticleCamPos = GL33C.glGetUniformLocation(this.glParticleProgram, "camPos");
         log.info("Particle shader uniforms: worldProj={}, tex64={}, tex128={}, tex256={}, tex1024={}, brightness={}, camPos={}", new Object[]{this.uniParticleWorldProj, this.uniParticleTex64, this.uniParticleTex128, this.uniParticleTex256, this.uniParticleTex1024, this.uniParticleBrightness, this.uniParticleCamPos});
      } else {
         this.uniParticleWorldProj = -1;
         this.uniParticleTex64 = -1;
         this.uniParticleTex128 = -1;
         this.uniParticleTex256 = -1;
         this.uniParticleTex1024 = -1;
         this.uniParticleBrightness = -1;
         this.uniParticleCamPos = -1;
      }

   }

   private void renderParticles(Scene scene) {
      Plugin particlePlugin = this.findParticlePlugin();
      if (particlePlugin != null && this.getRequiredBufferSizeMethod != null) {
         try {
            int requiredSize = (Integer)this.getRequiredBufferSizeMethod.invoke(particlePlugin);
            if (requiredSize == 0) {
               return;
            }

            VAO vao = this.vaoParticles.get(requiredSize);
            vao.vbo.vb.clear();
            SceneContext ctx = this.context(scene);
            float particleSize = (Float)this.getParticleSizeMethod.invoke(particlePlugin);
            float baseSize = particleSize / 3.0F;
            int rendered = (Integer)this.renderParticlesToBufferMethod.invoke(particlePlugin, vao.vbo.vb, this.cameraYawRad, this.cameraPitchRad, ctx.cameraX, ctx.cameraY, ctx.cameraZ, baseSize, this.getDrawDistance());
            this.vaoParticles.unmap();
            if (rendered > 0) {
               float[] projectionMatrix = Mat4.scale((float)this.client.getScale(), (float)this.client.getScale(), 1.0F);
               Mat4.mul(projectionMatrix, Mat4.projection((float)this.client.getViewportWidth(), (float)this.client.getViewportHeight(), 50.0F));
               Mat4.mul(projectionMatrix, Mat4.rotateX(this.cameraPitchRad));
               Mat4.mul(projectionMatrix, Mat4.rotateY(this.cameraYawRad));
               Mat4.mul(projectionMatrix, Mat4.translate((float)(-ctx.cameraX), (float)(-ctx.cameraY), (float)(-ctx.cameraZ)));
               if (this.glParticleProgram != 0) {
                  GL33C.glUseProgram(this.glParticleProgram);
                  GL33C.glUniformMatrix4fv(this.uniParticleWorldProj, false, projectionMatrix);
                  GL33C.glUniform3f(this.uniParticleCamPos, (float)ctx.cameraX, (float)ctx.cameraY, (float)ctx.cameraZ);
                  if (this.bindParticleTexturesMethod != null) {
                     try {
                        this.bindParticleTexturesMethod.invoke(particlePlugin, 33985);
                     } catch (ReflectiveOperationException var11) {
                     }
                  }

                  GL33C.glActiveTexture(33984);
                  GL33C.glUniform1i(this.uniParticleTex64, 1);
                  GL33C.glUniform1i(this.uniParticleTex128, 2);
                  GL33C.glUniform1i(this.uniParticleTex256, 3);
                  GL33C.glUniform1i(this.uniParticleTex1024, 4);
                  TextureProvider textureProvider = this.client.getTextureProvider();
                  GL33C.glUniform1f(this.uniParticleBrightness, (float)textureProvider.getBrightness());
               } else {
                  GL33C.glUniform3i(uniBase, 0, 0, 0);
               }

               GL33C.glDisable(2884);
               GL33C.glDepthMask(false);
               GL33C.glBlendFunc(770, 771);
               GL33C.glBindVertexArray(vao.vao);
               GL33C.glVertexAttribDivisor(0, 1);
               GL33C.glVertexAttribDivisor(1, 1);
               GL33C.glVertexAttribDivisor(2, 1);
               GL33C.glDrawArraysInstanced(4, 0, 6, rendered);
               GL33C.glVertexAttribDivisor(0, 0);
               GL33C.glVertexAttribDivisor(1, 0);
               GL33C.glVertexAttribDivisor(2, 0);
               GL33C.glBlendFuncSeparate(770, 771, 1, 1);
               GL33C.glDepthMask(true);
               GL33C.glEnable(2884);
               if (this.glParticleProgram != 0) {
                  GL33C.glUseProgram(glProgram);
                  GL33C.glActiveTexture(33985);
                  GL33C.glBindTexture(35866, this.textureArrayId);
                  GL33C.glActiveTexture(33984);
               }
            }

            this.checkGLErrors();
         } catch (ReflectiveOperationException var12) {
            ReflectiveOperationException e = var12;
            log.warn("Failed to invoke particle plugin methods via reflection", e);
            this.clearParticlePluginCache();
         }

      }
   }

   private Plugin findParticlePlugin() {
      if (this.cachedParticlePlugin != null) {
         if (this.pluginManager.isPluginEnabled(this.cachedParticlePlugin)) {
            if (!this.particleTexturesInitialized) {
               this.initializeParticleTextures(this.cachedParticlePlugin);
            }

            return this.cachedParticlePlugin;
         }

         this.clearParticlePluginCache();
      }

      Iterator var1 = this.pluginManager.getPlugins().iterator();

      Plugin plugin;
      do {
         if (!var1.hasNext()) {
            return null;
         }

         plugin = (Plugin)var1.next();
      } while(!plugin.getClass().getSimpleName().equals("ParticlePlugin") || !this.pluginManager.isPluginEnabled(plugin));

      this.cacheParticlePlugin(plugin);
      return this.cachedParticlePlugin;
   }

   private void initializeParticleTextures(Plugin plugin) {
      if (!this.particleTexturesInitialized) {
         try {
            Method initMethod = plugin.getClass().getMethod("initializeParticleTexturesForContext");
            initMethod.invoke(plugin);
            this.particleTexturesInitialized = true;
            log.debug("Particle textures initialized for GPU plugin context");
         } catch (ReflectiveOperationException var3) {
            ReflectiveOperationException e = var3;
            log.warn("Failed to initialize particle textures", e);
         }

      }
   }

   private void cacheParticlePlugin(Plugin plugin) {
      try {
         Class<?> clazz = plugin.getClass();
         this.getRequiredBufferSizeMethod = clazz.getMethod("getRequiredBufferSize");
         this.getParticleSizeMethod = clazz.getMethod("getParticleSize");
         this.renderParticlesToBufferMethod = clazz.getMethod("renderParticlesToBuffer", IntBuffer.class, Float.TYPE, Float.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE, Float.TYPE, Integer.TYPE);

         try {
            this.bindParticleTexturesMethod = clazz.getMethod("bindParticleTextures", Integer.TYPE);
         } catch (NoSuchMethodException var4) {
            this.bindParticleTexturesMethod = null;
         }

         this.cachedParticlePlugin = plugin;
         this.initializeParticleTextures(plugin);
      } catch (NoSuchMethodException var5) {
         NoSuchMethodException e = var5;
         log.warn("Failed to find particle plugin methods via reflection", e);
         this.clearParticlePluginCache();
      }

   }

   private void clearParticlePluginCache() {
      this.cachedParticlePlugin = null;
      this.getRequiredBufferSizeMethod = null;
      this.getParticleSizeMethod = null;
      this.renderParticlesToBufferMethod = null;
      this.bindParticleTexturesMethod = null;
      this.particleTexturesInitialized = false;
   }

   private void postDrawToplevel() {
      GL33C.glDisable(3042);
      GL33C.glDisable(2884);
      GL33C.glDisable(2929);
      GL33C.glBindFramebuffer(36009, this.awtContext.getFramebuffer(false));
      this.sceneFboValid = true;
   }

   private void blitSceneFbo() {
      int width = this.lastStretchedCanvasWidth;
      int height = this.lastStretchedCanvasHeight;
      GraphicsConfiguration graphicsConfiguration = this.clientUI.getGraphicsConfiguration();
      AffineTransform transform = graphicsConfiguration.getDefaultTransform();
      width = this.getScaledValue(transform.getScaleX(), width);
      height = this.getScaledValue(transform.getScaleY(), height);
      int defaultFbo = this.awtContext.getFramebuffer(false);
      GL33C.glBindFramebuffer(36008, this.fboScene);
      GL33C.glBindFramebuffer(36009, defaultFbo);
      GL33C.glBlitFramebuffer(0, 0, width, height, 0, 0, width, height, 16384, 9728);
      GL33C.glBindFramebuffer(36008, defaultFbo);
      this.checkGLErrors();
   }

   public void drawZoneOpaque(Projection entityProjection, Scene scene, int zx, int zz) {
      updateEntityProjection(entityProjection);
      SceneContext ctx = this.context(scene);
      if (ctx != null) {
         Zone z = ctx.zones[zx][zz];
         if (z.initialized) {
            int offset = scene.getWorldViewId() == 0 ? 5 : 0;
            z.renderOpaque(zx - offset, zz - offset, ctx.minLevel, ctx.level, ctx.maxLevel, ctx.hideRoofIds);
            this.checkGLErrors();
         }
      }
   }

   public void drawZoneAlpha(Projection entityProjection, Scene scene, int level, int zx, int zz) {
      SceneContext ctx = this.context(scene);
      if (ctx != null) {
         this.vaoA.unmap();
         Zone z = ctx.zones[zx][zz];
         if (z.initialized) {
            updateEntityProjection(entityProjection);
            GL33C.glUniform4i(uniEntityTint, scene.getOverrideHue(), scene.getOverrideSaturation(), scene.getOverrideLuminance(), scene.getOverrideAmount());
            int offset = scene.getWorldViewId() == 0 ? 5 : 0;
            int dx = ctx.cameraX - (zx - offset << 10);
            int dz = ctx.cameraZ - (zz - offset << 10);
            boolean close = dx * dx + dz * dz < 4194304;
            if (level == 0) {
               z.alphaSort(zx - offset, zz - offset, ctx.cameraX, ctx.cameraY, ctx.cameraZ);
               z.multizoneLocs(scene, zx - offset, zz - offset, ctx.cameraX, ctx.cameraZ, ctx.zones);
            }

            z.renderAlpha(zx - offset, zz - offset, this.cameraYaw, this.cameraPitch, ctx.minLevel, ctx.level, ctx.maxLevel, level, ctx.hideRoofIds, !close || scene.getOverrideAmount() > 0);
            this.checkGLErrors();
         }
      }
   }

   public void drawPass(Projection projection, Scene scene, int pass) {
      SceneContext ctx = this.context(scene);
      if (ctx != null) {
         updateEntityProjection(projection);
         int sz;
         int i;
         if (pass == 0) {
            this.vaoO.addRange(projection, scene);
            this.vaoPO.addRange(projection, scene);
            if (scene.getWorldViewId() == 0) {
               GL33C.glUniform3i(uniBase, 0, 0, 0);
               sz = this.vaoO.unmap();

               VAO vao;
               for(i = 0; i < sz; ++i) {
                  vao = (VAO)this.vaoO.vaos.get(i);
                  vao.draw();
                  vao.reset();
               }

               sz = this.vaoPO.unmap();
               if (sz > 0) {
                  GL33C.glDepthMask(false);

                  for(i = 0; i < sz; ++i) {
                     vao = (VAO)this.vaoPO.vaos.get(i);
                     vao.draw();
                  }

                  GL33C.glDepthMask(true);
                  GL33C.glColorMask(false, false, false, false);

                  for(i = 0; i < sz; ++i) {
                     vao = (VAO)this.vaoPO.vaos.get(i);
                     vao.draw();
                     vao.reset();
                  }

                  GL33C.glColorMask(true, true, true, true);
               }
            }
         } else if (pass == 1) {
            for(sz = 0; sz < ctx.sizeX; ++sz) {
               for(i = 0; i < ctx.sizeZ; ++i) {
                  Zone zone = ctx.zones[sz][i];
                  zone.removeTemp();
               }
            }
         }

         this.checkGLErrors();
      }
   }

   public void drawDynamic(Projection worldProjection, Scene scene, TileObject tileObject, Renderable r, Model m, int orient, int x, int y, int z) {
      SceneContext ctx = this.context(scene);
      if (ctx != null) {
         if (this.renderCallbackManager.drawObject(scene, tileObject)) {
            int size = m.getFaceCount() * 3 * 24;
            VAO o;
            if (m.getFaceTransparencies() == null) {
               o = this.vaoO.get(size);
               this.clientUploader.uploadTempModel(m, orient, x, y, z, o.vbo.vb);
            } else {
               m.calculateBoundsCylinder();
               o = this.vaoO.get(size);
               VAO a = this.vaoA.get(size);
               int start = a.vbo.vb.position();

               try {
                  this.facePrioritySorter.uploadSortedModel(worldProjection, m, orient, x, y, z, o.vbo.vb, a.vbo.vb);
               } catch (AssertionError var21) {
                  this.addTempModelSortContextBreadcrumb("drawDynamic", scene, tileObject, r, m, orient, x, y, z);
               } catch (Exception var22) {
                  Exception ex = var22;
                  logTempModelException("error drawing dynamic entity", ex);
               }

               int end = a.vbo.vb.position();
               if (end > start) {
                  int offset = scene.getWorldViewId() == 0 ? 40 : 0;
                  int zx = (x >> 10) + (offset >> 3);
                  int zz = (z >> 10) + (offset >> 3);
                  Zone zone = ctx.zones[zx][zz];
                  int plane = Math.min(ctx.maxLevel, tileObject.getPlane());
                  zone.addTempAlphaModel(a.vao, start, end, plane, x & 1023, y, z & 1023);
               }
            }

         }
      }
   }

   public void drawTemp(Projection worldProjection, Scene scene, GameObject gameObject, Model m, int orient, int x, int y, int z) {
      SceneContext ctx = this.context(scene);
      if (ctx != null) {
         if (this.renderCallbackManager.drawObject(scene, gameObject)) {
            Renderable renderable = gameObject.getRenderable();
            int size = m.getFaceCount() * 3 * 24;
            int renderMode = renderable.getRenderMode();
            VAO o;
            if (renderMode != 2 && m.getFaceTransparencies() == null) {
               o = this.vaoO.get(size);
               this.clientUploader.uploadTempModel(m, orient, x, y, z, o.vbo.vb);
            } else {
               o = renderMode == 2 ? this.vaoPO.get(size) : this.vaoO.get(size);
               VAO a = this.vaoA.get(size);
               int start = a.vbo.vb.position();
               m.calculateBoundsCylinder();

               try {
                  this.facePrioritySorter.uploadSortedModel(worldProjection, m, orient, x, y, z, o.vbo.vb, a.vbo.vb);
               } catch (AssertionError var22) {
                  this.addTempModelSortContextBreadcrumb("drawTemp", scene, gameObject, renderable, m, orient, x, y, z);
               } catch (Exception var23) {
                  Exception ex = var23;
                  logTempModelException("error drawing temp entity", ex);
               }

               int end = a.vbo.vb.position();
               if (end > start) {
                  int offset = scene.getWorldViewId() == 0 ? 5 : 0;
                  int zx = (gameObject.getX() >> 10) + offset;
                  int zz = (gameObject.getY() >> 10) + offset;
                  Zone zone = ctx.zones[zx][zz];
                  int plane = Math.min(ctx.maxLevel, gameObject.getPlane());
                  zone.addTempAlphaModel(a.vao, start, end, plane, x & 1023, y - renderable.getModelHeight(), z & 1023);
               }
            }

         }
      }
   }

   public void invalidateZone(Scene scene, int zx, int zz) {
      SceneContext ctx = this.context(scene);
      if (ctx != null) {
         Zone z = ctx.zones[zx][zz];
         if (!z.invalidate) {
            z.invalidate = true;
            log.debug("Zone invalidated: wx={} x={} z={}", new Object[]{scene.getWorldViewId(), zx, zz});
         }

      }
   }

   private void addTempModelSortContextBreadcrumb(String drawPath, Scene scene, TileObject tileObject, Renderable renderable, Model model, int orientation, int x, int y, int z) {
      long now = System.currentTimeMillis();
      if (tempModelSortContextBreadcrumbMillis != 0L && now - tempModelSortContextBreadcrumbMillis < 60000L) {
         ++tempModelSortContextSuppressedLogs;
      } else {
         int suppressedLogs = tempModelSortContextSuppressedLogs;
         tempModelSortContextSuppressedLogs = 0;
         tempModelSortContextBreadcrumbMillis = now;
         int modelIdentity = System.identityHashCode(model);
         int objectId = tileObject.getId();
         Breadcrumb breadcrumb = new Breadcrumb();
         breadcrumb.setCategory("gpu.temp_model_sort_context");
         breadcrumb.setLevel(SentryLevel.ERROR);
         breadcrumb.setMessage("GPU temp model sort failed for tile object");
         breadcrumb.setData("drawPath", drawPath);
         breadcrumb.setData("worldViewId", scene.getWorldViewId());
         breadcrumb.setData("objectId", objectId);
         breadcrumb.setData("objectHash", Long.toUnsignedString(tileObject.getHash()));
         breadcrumb.setData("plane", tileObject.getPlane());
         breadcrumb.setData("tileLocalX", tileObject.getX());
         breadcrumb.setData("tileLocalY", tileObject.getY());
         breadcrumb.setData("tileZ", tileObject.getZ());
         breadcrumb.setData("orientation", orientation);
         breadcrumb.setData("renderX", x);
         breadcrumb.setData("renderY", y);
         breadcrumb.setData("renderZ", z);
         breadcrumb.setData("renderableClass", renderable.getClass().getName());
         breadcrumb.setData("renderMode", renderable.getRenderMode());
         breadcrumb.setData("modelHeight", renderable.getModelHeight());
         breadcrumb.setData("modelIdentity", modelIdentity);
         breadcrumb.setData("modelSceneId", model.getSceneId());
         breadcrumb.setData("vertices", model.getVerticesCount());
         breadcrumb.setData("faces", model.getFaceCount());
         breadcrumb.setData("radius", model.getRadius());
         breadcrumb.setData("diameter", model.getDiameter());
         breadcrumb.setData("suppressedLogsSinceLast", suppressedLogs);
         ObjectComposition objectComposition = this.client.getObjectDefinition(objectId);
         String objectName = null;
         String objectModelIds = null;
         if (objectComposition != null) {
            objectName = objectComposition.getName();
            breadcrumb.setData("objectName", objectName);
            int[] objectModels = objectComposition.getObjectModels();
            if (objectModels != null) {
               objectModelIds = Arrays.toString(objectModels);
               breadcrumb.setData("objectModelIds", objectModelIds);
            }
         }

         Sentry.addBreadcrumb(breadcrumb);
         String finalObjectName = objectName;
         String finalObjectModelIds = objectModelIds;
         Sentry.captureMessage("GPU temp model sort failed for tile object", SentryLevel.ERROR, (scope) -> {
            scope.setTag("drawPath", drawPath);
            scope.setExtra("worldViewId", String.valueOf(scene.getWorldViewId()));
            scope.setExtra("objectId", String.valueOf(objectId));
            scope.setExtra("objectHash", Long.toUnsignedString(tileObject.getHash()));
            scope.setExtra("plane", String.valueOf(tileObject.getPlane()));
            scope.setExtra("tileLocalX", String.valueOf(tileObject.getX()));
            scope.setExtra("tileLocalY", String.valueOf(tileObject.getY()));
            scope.setExtra("tileZ", String.valueOf(tileObject.getZ()));
            scope.setExtra("orientation", String.valueOf(orientation));
            scope.setExtra("renderX", String.valueOf(x));
            scope.setExtra("renderY", String.valueOf(y));
            scope.setExtra("renderZ", String.valueOf(z));
            scope.setExtra("renderableClass", renderable.getClass().getName());
            scope.setExtra("renderMode", String.valueOf(renderable.getRenderMode()));
            scope.setExtra("modelHeight", String.valueOf(renderable.getModelHeight()));
            scope.setExtra("modelIdentity", String.valueOf(modelIdentity));
            scope.setExtra("modelSceneId", String.valueOf(model.getSceneId()));
            scope.setExtra("vertices", String.valueOf(model.getVerticesCount()));
            scope.setExtra("faces", String.valueOf(model.getFaceCount()));
            scope.setExtra("radius", String.valueOf(model.getRadius()));
            scope.setExtra("diameter", String.valueOf(model.getDiameter()));
            scope.setExtra("suppressedLogsSinceLast", String.valueOf(suppressedLogs));
            if (finalObjectName != null) {
               scope.setExtra("objectName", finalObjectName);
            }

            if (finalObjectModelIds != null) {
               scope.setExtra("objectModelIds", finalObjectModelIds);
            }

         });
      }
   }

   private static void logTempModelException(String message, Exception exception) {
      long now = System.currentTimeMillis();
      if (tempModelExceptionLogMillis != 0L && now - tempModelExceptionLogMillis < 60000L) {
         ++tempModelExceptionSuppressedLogs;
      } else {
         int suppressedLogs = tempModelExceptionSuppressedLogs;
         tempModelExceptionSuppressedLogs = 0;
         tempModelExceptionLogMillis = now;
         log.debug("{} suppressed={}", new Object[]{message, suppressedLogs, exception});
         Sentry.captureException(exception, (scope) -> {
            scope.setTag("gpuTempModelException", "true");
            scope.setExtra("message", message);
            scope.setExtra("suppressedLogsSinceLast", String.valueOf(suppressedLogs));
         });
      }
   }

   @Subscribe
   public void onPostClientTick(PostClientTick event) {
      WorldView wv = this.client.getTopLevelWorldView();
      if (wv != null) {
         this.rebuild(wv);
         Iterator var3 = wv.worldEntities().iterator();

         while(var3.hasNext()) {
            WorldEntity we = (WorldEntity)var3.next();
            wv = we.getWorldView();
            this.rebuild(wv);
         }

      }
   }

   private void rebuild(WorldView wv) {
      SceneContext ctx = this.context(wv);
      if (ctx != null) {
         for(int x = 0; x < ctx.sizeX; ++x) {
            for(int z = 0; z < ctx.sizeZ; ++z) {
               Zone zone = ctx.zones[x][z];
               if (zone.invalidate) {
                  assert zone.initialized;

                  zone.free();
                  zone = ctx.zones[x][z] = new Zone();
                  Scene scene = wv.getScene();
                  this.clientUploader.zoneSize(scene, zone, x, z);
                  VBO o = null;
                  VBO a = null;
                  int sz = zone.sizeO * 20 * 3;
                  if (sz > 0) {
                     o = new VBO(sz);
                     o.init(35044);
                     o.map();
                  }

                  sz = zone.sizeA * 20 * 3;
                  if (sz > 0) {
                     a = new VBO(sz);
                     a.init(35044);
                     a.map();
                  }

                  zone.init(o, a);
                  this.clientUploader.uploadZone(scene, zone, x, z);
                  zone.unmap();
                  zone.initialized = true;
                  zone.dirty = true;
                  log.debug("Rebuilt zone wv={} x={} z={}", new Object[]{wv.getId(), x, z});
               }
            }
         }

      }
   }

   private void prepareInterfaceTexture(int canvasWidth, int canvasHeight) {
      if (canvasWidth != this.lastCanvasWidth || canvasHeight != this.lastCanvasHeight) {
         this.lastCanvasWidth = canvasWidth;
         this.lastCanvasHeight = canvasHeight;
         GL33C.glBindBuffer(35052, this.interfacePbo);
         GL33C.glBufferData(35052, (long)(canvasWidth * canvasHeight) * 4L, 35040);
         GL33C.glBindBuffer(35052, 0);
         GL33C.glBindTexture(3553, this.interfaceTexture);
         GL33C.glTexImage2D(3553, 0, 6408, canvasWidth, canvasHeight, 0, 32993, 5121, 0L);
         GL33C.glBindTexture(3553, 0);
      }

      BufferProvider bufferProvider = this.client.getBufferProvider();
      int[] pixels = bufferProvider.getPixels();
      int width = bufferProvider.getWidth();
      int height = bufferProvider.getHeight();
      GL33C.glBindBuffer(35052, this.interfacePbo);
      ByteBuffer interfaceBuf = GL33C.glMapBuffer(35052, 35001);
      if (interfaceBuf != null) {
         interfaceBuf.asIntBuffer().put(pixels, 0, width * height);
         GL33C.glUnmapBuffer(35052);
      }

      GL33C.glBindTexture(3553, this.interfaceTexture);
      GL33C.glTexSubImage2D(3553, 0, 0, 0, width, height, 32993, 33639, 0L);
      GL33C.glBindBuffer(35052, 0);
      GL33C.glBindTexture(3553, 0);
   }

   public void draw(int overlayColor) {
      GameState gameState = this.client.getGameState();
      if (gameState != GameState.STARTING) {
         TextureProvider textureProvider = this.client.getTextureProvider();
         if (this.textureArrayId == -1 && textureProvider != null) {
            this.textureArrayId = this.textureManager.initTextureArray(textureProvider);
            if (this.textureArrayId > -1) {
               float[] texAnims = this.textureManager.computeTextureAnimations(textureProvider);
               GL33C.glUseProgram(glProgram);
               GL33C.glUniform2fv(this.uniTextureAnimations, texAnims);
               GL33C.glUseProgram(0);
            }
         }

         int canvasHeight = this.client.getCanvasHeight();
         int canvasWidth = this.client.getCanvasWidth();
         this.prepareInterfaceTexture(canvasWidth, canvasHeight);
         GL33C.glClearColor(0.0F, 0.0F, 0.0F, 1.0F);
         GL33C.glClear(16384);
         if (this.sceneFboValid) {
            this.blitSceneFbo();
         }

         this.drawUi(overlayColor, canvasHeight, canvasWidth);

         try {
            this.awtContext.swapBuffers();
         } catch (RuntimeException var7) {
            RuntimeException ex = var7;
            if (!this.canvas.isValid()) {
               return;
            }

            log.error("error swapping buffers", ex);
            SwingUtilities.invokeLater(() -> {
               try {
                  this.pluginManager.stopPlugin(this);
               } catch (PluginInstantiationException var2) {
                  PluginInstantiationException ex2 = var2;
                  log.error("error stopping plugin", ex2);
               }

            });
            return;
         }

         this.drawManager.processDrawComplete(this::screenshot);
         GL33C.glBindFramebuffer(36160, this.awtContext.getFramebuffer(false));
         this.checkGLErrors();
      }
   }

   private void drawUi(int overlayColor, int canvasHeight, int canvasWidth) {
      GL33C.glEnable(3042);
      GL33C.glBlendFunc(1, 771);
      GL33C.glBindTexture(3553, this.interfaceTexture);
      UIScalingMode uiScalingMode = this.config.uiScalingMode();
      GL33C.glUseProgram(this.glUiProgram);
      GL33C.glUniform1i(this.uniTex, 0);
      GL33C.glUniform2i(this.uniTexSourceDimensions, canvasWidth, canvasHeight);
      GL33C.glUniform4f(this.uniUiAlphaOverlay, (float)(overlayColor >> 16 & 255) / 255.0F, (float)(overlayColor >> 8 & 255) / 255.0F, (float)(overlayColor & 255) / 255.0F, (float)(overlayColor >>> 24) / 255.0F);
      GL33C.glUniform1f(this.uniUiColorblindIntensity, (float)this.config.colorBlindIntensity());
      if (this.client.isStretchedEnabled()) {
         Dimension dim = this.client.getStretchedDimensions();
         this.glDpiAwareViewport(0, 0, dim.width, dim.height);
         GL33C.glUniform2i(this.uniTexTargetDimensions, dim.width, dim.height);
      } else {
         this.glDpiAwareViewport(0, 0, canvasWidth, canvasHeight);
         GraphicsConfiguration graphicsConfiguration = this.clientUI.getGraphicsConfiguration();
         AffineTransform t = graphicsConfiguration.getDefaultTransform();
         GL33C.glUniform2i(this.uniTexTargetDimensions, this.getScaledValue(t.getScaleX(), canvasWidth), this.getScaledValue(t.getScaleY(), canvasHeight));
      }

      int function = uiScalingMode != UIScalingMode.LINEAR && uiScalingMode != UIScalingMode.HYBRID ? 9728 : 9729;
      GL33C.glTexParameteri(3553, 10241, function);
      GL33C.glTexParameteri(3553, 10240, function);
      GL33C.glBindVertexArray(this.vaoUiHandle);
      GL33C.glDrawArrays(6, 0, 4);
      GL33C.glBindTexture(3553, 0);
      GL33C.glBindVertexArray(0);
      GL33C.glUseProgram(0);
      GL33C.glBlendFunc(770, 771);
      GL33C.glDisable(3042);
   }

   private Image screenshot() {
      boolean keepAlpha = FreezeFrameFlags.TRANSPARENT_SKYBOX_CAPTURE;
      int width;
      int height;
      if (keepAlpha) {
         width = this.lastStretchedCanvasWidth;
         height = this.lastStretchedCanvasHeight;
      } else {
         width = this.client.getCanvasWidth();
         height = this.client.getCanvasHeight();
         if (this.client.isStretchedEnabled()) {
            Dimension dim = this.client.getStretchedDimensions();
            width = dim.width;
            height = dim.height;
         }

         GraphicsConfiguration graphicsConfiguration = this.clientUI.getGraphicsConfiguration();
         AffineTransform t = graphicsConfiguration.getDefaultTransform();
         width = this.getScaledValue(t.getScaleX(), width);
         height = this.getScaledValue(t.getScaleY(), height);
      }

      ByteBuffer buffer = ByteBuffer.allocateDirect(width * height * 4).order(ByteOrder.nativeOrder());
      if (keepAlpha) {
         this.ensureCaptureFbo(width, height);
         GL33C.glBindFramebuffer(36008, this.fboScene);
         GL33C.glBindFramebuffer(36009, this.fboCapture);
         GL33C.glBlitFramebuffer(0, 0, width, height, 0, 0, width, height, 16384, 9728);
         GL33C.glBindFramebuffer(36008, this.fboCapture);
         GL33C.glReadBuffer(36064);
      } else {
         GL33C.glReadBuffer(this.awtContext.getBufferMode());
      }

      GL33C.glReadPixels(0, 0, width, height, 6408, 5121, buffer);
      BufferedImage image = new BufferedImage(width, height, keepAlpha ? 2 : 1);
      int[] pixels = ((DataBufferInt)image.getRaster().getDataBuffer()).getData();

      for(int y = 0; y < height; ++y) {
         for(int x = 0; x < width; ++x) {
            int r = buffer.get() & 255;
            int g = buffer.get() & 255;
            int b = buffer.get() & 255;
            int a = buffer.get() & 255;
            pixels[(height - y - 1) * width + x] = keepAlpha ? a << 24 | r << 16 | g << 8 | b : r << 16 | g << 8 | b;
         }
      }

      return image;
   }

   @Subscribe
   public void onGameStateChanged(GameStateChanged gameStateChanged) {
      GameState state = gameStateChanged.getGameState();
      if (state.getState() < GameState.LOADING.getState()) {
         this.sceneFboValid = false;
      }

      if (state == GameState.STARTING) {
         if (this.textureArrayId != -1) {
            this.textureManager.freeTextureArray(this.textureArrayId);
         }

         this.textureArrayId = -1;
         this.lastAnisotropicFilteringLevel = -1;
      }

   }

   public void loadScene(WorldView worldView, Scene scene) {
      if (scene.getWorldViewId() != 0) {
         this.loadSubScene(worldView, scene);
      } else {
         if (this.nextZones != null) {
            log.debug("Double zone load!");
            CountDownLatch latch = new CountDownLatch(1);
            this.clientThread.invoke(() -> {
               for(int x = 0; x < 23; ++x) {
                  for(int z = 0; z < 23; ++z) {
                     Zone zone = this.nextZones[x][z];

                     assert !zone.cull;

                     if (!zone.initialized) {
                        zone.unmap();
                        zone.initialized = true;
                        zone.free();
                     }
                  }
               }

               latch.countDown();
            });

            try {
               latch.await();
            } catch (InterruptedException var27) {
               InterruptedException e = var27;
               throw new RuntimeException(e);
            }

            this.nextZones = null;
            this.nextRoofChanges = null;
         }

         SceneContext ctx = this.root;
         Scene prev = this.client.getTopLevelWorldView().getScene();
         this.regionManager.prepare(scene);
         int dx = scene.getBaseX() - prev.getBaseX() >> 3;
         int dy = scene.getBaseY() - prev.getBaseY() >> 3;
         int SCENE_ZONES = true;

         for(int x = 0; x < 23; ++x) {
            for(int z = 0; z < 23; ++z) {
               ctx.zones[x][z].cull = true;
            }
         }

         Map<Integer, Integer> roofChanges = new HashMap();
         Zone[][] newZones = new Zone[23][23];
         GameState gameState = this.client.getGameState();
         int x;
         int z;
         int ox;
         int oz;
         Zone old;
         if (prev.isInstance() == scene.isInstance() && gameState == GameState.LOGGED_IN) {
            int[][][] prevTemplates = prev.getInstanceTemplateChunks();
            int[][][] curTemplates = scene.getInstanceTemplateChunks();
            int[][][] prids = prev.getRoofs();
            int[][][] nrids = scene.getRoofs();

            for(x = 0; x < 23; ++x) {
               label225:
               for(z = 0; z < 23; ++z) {
                  ox = x + dx;
                  oz = z + dy;
                  if (canReuse(ctx.zones, ox, oz)) {
                     int level;
                     int tx;
                     int tz;
                     int prid;
                     int nrid;
                     if (scene.isInstance()) {
                        int jx = x - 5;
                        level = z - 5;
                        tx = ox - 5;
                        tz = oz - 5;
                        if (jx >= 0 && jx < 13 && level >= 0 && level < 13 && tx >= 0 && tx < 13 && tz >= 0 && tz < 13) {
                           for(prid = 0; prid < 4; ++prid) {
                              nrid = prevTemplates[prid][tx][tz];
                              int curTemplate = curTemplates[prid][jx][level];
                              if (nrid != curTemplate) {
                                 log.error("Instance template reuse mismatch! prev={} cur={}", nrid, curTemplate);
                                 continue label225;
                              }
                           }
                        }
                     }

                     old = ctx.zones[ox][oz];

                     assert old.initialized;

                     if (!old.dirty) {
                        assert old.sizeO > 0 || old.sizeA > 0;

                        for(level = 0; level < 4; ++level) {
                           for(tx = 0; tx < 8; ++tx) {
                              for(tz = 0; tz < 8; ++tz) {
                                 prid = prids[level][(ox << 3) + tx][(oz << 3) + tz];
                                 nrid = nrids[level][(x << 3) + tx][(z << 3) + tz];
                                 if (prid != nrid && (prid == 0 || nrid == 0)) {
                                    log.trace("Roof mismatch: {} -> {}", prid, nrid);
                                    continue label225;
                                 }

                                 Integer orid = (Integer)roofChanges.putIfAbsent(prid, nrid);
                                 if (orid == null) {
                                    log.trace("Roof change: {} -> {}", prid, nrid);
                                 } else if (orid != nrid) {
                                    log.trace("Roof mismatch: {} -> {} vs {}", new Object[]{prid, nrid, orid});
                                    continue label225;
                                 }
                              }
                           }
                        }

                        assert old.cull;

                        old.cull = false;
                        newZones[x][z] = old;
                     }
                  }
               }
            }
         }

         int len;
         for(int x = 0; x < 23; ++x) {
            for(len = 0; len < 23; ++len) {
               if (newZones[x][len] == null) {
                  newZones[x][len] = new Zone();
               }
            }
         }

         Stopwatch sw = Stopwatch.createStarted();
         len = 0;
         int lena = 0;
         int reused = 0;
         x = 0;

         for(z = 0; z < 23; ++z) {
            for(ox = 0; ox < 23; ++ox) {
               Zone zone = newZones[z][ox];
               if (!zone.initialized) {
                  assert zone.glVao == 0;

                  assert zone.glVaoA == 0;

                  this.mapUploader.zoneSize(scene, zone, z, ox);
                  len += zone.sizeO;
                  lena += zone.sizeA;
                  ++x;
               } else {
                  ++reused;
               }
            }
         }

         log.debug("Scene size time {} reused {} new {} len opaque {} size opaque {}kb len alpha {} size alpha {}kb", new Object[]{sw, reused, x, len, len * 20 * 3 / 1024, lena, lena * 20 * 3 / 1024});
         CountDownLatch latch = new CountDownLatch(1);
         this.clientThread.invoke(() -> {
            for(int x = 0; x < 23; ++x) {
               for(int z = 0; z < 23; ++z) {
                  Zone zone = newZones[x][z];
                  if (!zone.initialized) {
                     VBO o = null;
                     VBO a = null;
                     int sz = zone.sizeO * 20 * 3;
                     if (sz > 0) {
                        o = new VBO(sz);
                        o.init(35044);
                        o.map();
                     }

                     sz = zone.sizeA * 20 * 3;
                     if (sz > 0) {
                        a = new VBO(sz);
                        a.init(35044);
                        a.map();
                     }

                     zone.init(o, a);
                  }
               }
            }

            latch.countDown();
         });

         try {
            latch.await();
         } catch (InterruptedException var26) {
            throw new RuntimeException(var26);
         }

         sw = Stopwatch.createStarted();

         for(ox = 0; ox < 23; ++ox) {
            for(oz = 0; oz < 23; ++oz) {
               old = newZones[ox][oz];
               if (!old.initialized) {
                  this.mapUploader.uploadZone(scene, old, ox, oz);
               }
            }
         }

         log.debug("Scene upload time {}", sw);
         this.nextZones = newZones;
         this.nextRoofChanges = roofChanges;
      }
   }

   private static boolean canReuse(Zone[][] zones, int zx, int zz) {
      for(int x = zx - 1; x <= zx + 1; ++x) {
         if (x < 0 || x >= 23) {
            return false;
         }

         for(int z = zz - 1; z <= zz + 1; ++z) {
            if (z < 0 || z >= 23) {
               return false;
            }

            Zone zone = zones[x][z];
            if (!zone.initialized) {
               return false;
            }

            if (zone.sizeO == 0 && zone.sizeA == 0) {
               return false;
            }
         }
      }

      return true;
   }

   private void loadSubScene(WorldView worldView, Scene scene) {
      int worldViewId = scene.getWorldViewId();

      assert worldViewId != -1;

      log.debug("Loading world view {}", worldViewId);
      SceneContext ctx0 = this.subs[worldViewId];
      if (ctx0 != null) {
         log.info("Reload of an already loaded worldview?");
      } else {
         SceneContext ctx = new SceneContext(worldView.getSizeX() >> 3, worldView.getSizeY() >> 3);
         this.subs[worldViewId] = ctx;

         int x;
         for(int x = 0; x < ctx.sizeX; ++x) {
            for(x = 0; x < ctx.sizeZ; ++x) {
               Zone zone = ctx.zones[x][x];
               this.mapUploader.zoneSize(scene, zone, x, x);
            }
         }

         CountDownLatch latch = new CountDownLatch(1);
         this.clientThread.invoke(() -> {
            for(int x = 0; x < ctx.sizeX; ++x) {
               for(int z = 0; z < ctx.sizeZ; ++z) {
                  Zone zone = ctx.zones[x][z];
                  VBO o = null;
                  VBO a = null;
                  int sz = zone.sizeO * 20 * 3;
                  if (sz > 0) {
                     o = new VBO(sz);
                     o.init(35044);
                     o.map();
                  }

                  sz = zone.sizeA * 20 * 3;
                  if (sz > 0) {
                     a = new VBO(sz);
                     a.init(35044);
                     a.map();
                  }

                  zone.init(o, a);
               }
            }

            latch.countDown();
         });

         try {
            latch.await();
         } catch (InterruptedException var10) {
            throw new RuntimeException(var10);
         }

         for(x = 0; x < ctx.sizeX; ++x) {
            for(int z = 0; z < ctx.sizeZ; ++z) {
               Zone zone = ctx.zones[x][z];
               this.mapUploader.uploadZone(scene, zone, x, z);
            }
         }

      }
   }

   public void despawnWorldView(WorldView worldView) {
      int worldViewId = worldView.getId();
      if (worldViewId != 0) {
         log.debug("WorldView despawn: {}", worldViewId);
         SceneContext sub = this.subs[worldViewId];
         if (sub == null) {
            return;
         }

         sub.free();
         this.subs[worldViewId] = null;
      }

   }

   public void swapScene(Scene scene) {
      if (scene.getWorldViewId() != 0) {
         this.swapSub(scene);
      } else {
         SceneContext ctx = this.root;

         int x;
         int z;
         Zone zone;
         for(x = 0; x < ctx.sizeX; ++x) {
            for(z = 0; z < ctx.sizeZ; ++z) {
               zone = ctx.zones[x][z];
               if (zone.cull) {
                  zone.free();
               } else {
                  zone.updateRoofs(this.nextRoofChanges);
               }
            }
         }

         this.nextRoofChanges = null;
         ctx.zones = this.nextZones;
         this.nextZones = null;

         for(x = 0; x < ctx.zones.length; ++x) {
            for(z = 0; z < ctx.zones[0].length; ++z) {
               zone = ctx.zones[x][z];
               if (!zone.initialized) {
                  zone.unmap();
                  zone.initialized = true;
               }
            }
         }

         this.checkGLErrors();
      }
   }

   private void swapSub(Scene scene) {
      SceneContext ctx = this.context(scene);
      if (ctx != null) {
         for(int x = 0; x < ctx.sizeX; ++x) {
            for(int z = 0; z < ctx.sizeZ; ++z) {
               Zone zone = ctx.zones[x][z];
               if (!zone.initialized) {
                  zone.unmap();
                  zone.initialized = true;
               }
            }
         }

         log.debug("WorldView ready: {}", scene.getWorldViewId());
      }
   }

   private int getScaledValue(double scale, int value) {
      return (int)((double)value * scale);
   }

   private void glDpiAwareViewport(int x, int y, int width, int height) {
      GraphicsConfiguration graphicsConfiguration = this.clientUI.getGraphicsConfiguration();
      AffineTransform t = graphicsConfiguration.getDefaultTransform();
      GL33C.glViewport(this.getScaledValue(t.getScaleX(), x), this.getScaledValue(t.getScaleY(), y), this.getScaledValue(t.getScaleX(), width), this.getScaledValue(t.getScaleY(), height));
   }

   private int getDrawDistance() {
      return Ints.constrainToRange(this.config.drawDistance(), 0, 184);
   }

   private void checkGLErrors() {
      if (log.isDebugEnabled()) {
         while(true) {
            int err = GL33C.glGetError();
            if (err == 0) {
               return;
            }

            String errStr;
            switch (err) {
               case 1280:
                  errStr = "INVALID_ENUM";
                  break;
               case 1281:
                  errStr = "INVALID_VALUE";
                  break;
               case 1282:
                  errStr = "INVALID_OPERATION";
                  break;
               case 1283:
               case 1284:
               case 1285:
               default:
                  errStr = "" + err;
                  break;
               case 1286:
                  errStr = "INVALID_FRAMEBUFFER_OPERATION";
            }

            log.debug("glGetError:", new Exception(errStr));
         }
      }
   }

   static class SceneContext {
      final int sizeX;
      final int sizeZ;
      Zone[][] zones;
      private int cameraX;
      private int cameraY;
      private int cameraZ;
      private int minLevel;
      private int level;
      private int maxLevel;
      private Set<Integer> hideRoofIds;

      SceneContext(int sizeX, int sizeZ) {
         this.sizeX = sizeX;
         this.sizeZ = sizeZ;
         this.zones = new Zone[sizeX][sizeZ];

         for(int x = 0; x < sizeX; ++x) {
            for(int z = 0; z < sizeZ; ++z) {
               this.zones[x][z] = new Zone();
            }
         }

      }

      void free() {
         for(int x = 0; x < this.sizeX; ++x) {
            for(int z = 0; z < this.sizeZ; ++z) {
               this.zones[x][z].free();
            }
         }

      }
   }
}
