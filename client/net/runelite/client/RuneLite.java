package net.runelite.client;

import ch.qos.logback.classic.Level;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.MoreObjects;
import com.google.gson.Gson;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.sun.management.HotSpotDiagnosticMXBean;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.nio.charset.StandardCharsets;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.GeneralSecurityException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;
import javax.annotation.Nullable;
import javax.inject.Provider;
import javax.inject.Singleton;
import javax.management.ObjectName;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;
import javax.swing.SwingUtilities;
import joptsimple.ArgumentAcceptingOptionSpec;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import joptsimple.OptionSpec;
import joptsimple.ValueConversionException;
import joptsimple.ValueConverter;
import net.runelite.api.Client;
import net.runelite.client.account.SessionManager;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.discord.DiscordService;
import net.runelite.client.eventbus.EventBus;
import net.runelite.client.externalplugins.ExternalPluginManager;
import net.runelite.client.plugins.PluginManager;
import net.runelite.client.rs.ClientLoader;
import net.runelite.client.ui.ClientUI;
import net.runelite.client.ui.FatalErrorDialog;
import net.runelite.client.ui.SplashScreen;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayManager;
import net.runelite.client.ui.overlay.WidgetOverlay;
import net.runelite.client.ui.overlay.tooltip.TooltipOverlay;
import net.runelite.client.ui.overlay.worldmap.WorldMapOverlay;
import net.runelite.client.util.OSType;
import net.runelite.client.util.ReflectUtil;
import net.runelite.http.api.RuneLiteAPI;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
public class RuneLite {
   private static final Logger log = LoggerFactory.getLogger(RuneLite.class);
   public static final File RUNELITE_DIR = new File(System.getProperty("user.home"), ".augustrsps");
   public static final File CACHE_DIR;
   public static final File PLUGINS_DIR;
   public static final File SCREENSHOT_DIR;
   public static final File LOGS_DIR;
   public static final File DEFAULT_SESSION_FILE;
   public static final File NOTIFICATIONS_DIR;
   public static final File FONTS_DIR;
   private static final int MAX_OKHTTP_CACHE_SIZE = 20971520;
   public static String USER_AGENT;
   private static Injector injector;
   @Inject
   private PluginManager pluginManager;
   @Inject
   private ExternalPluginManager externalPluginManager;
   @Inject
   private EventBus eventBus;
   @Inject
   private ConfigManager configManager;
   @Inject
   private SessionManager sessionManager;
   @Inject
   private DiscordService discordService;
   @Inject
   private ClientSessionManager clientSessionManager;
   @Inject
   private ClientUI clientUI;
   @Inject
   private OverlayManager overlayManager;
   @Inject
   private Provider<TooltipOverlay> tooltipOverlay;
   @Inject
   private Provider<WorldMapOverlay> worldMapOverlay;
   @Inject
   private Gson gson;
   @Inject
   private Client client;
   @Inject
   @Nullable
   private RuntimeConfig runtimeConfig;
   @Inject
   @Nullable
   private TelemetryClient telemetryClient;
   @Inject
   private ScheduledExecutorService scheduledExecutorService;

   private static void enableHeapDumpOnOom() {
      try {
         RUNELITE_DIR.mkdirs();
         File dump = new File(RUNELITE_DIR, "heapdump.hprof");
         if (dump.exists() && !dump.delete()) {
            log.warn("Could not delete stale heap dump {}", dump);
         }

         HotSpotDiagnosticMXBean bean = (HotSpotDiagnosticMXBean)ManagementFactory.getPlatformMXBean(HotSpotDiagnosticMXBean.class);
         bean.setVMOption("HeapDumpPath", dump.getAbsolutePath());
         bean.setVMOption("HeapDumpOnOutOfMemoryError", "true");
         log.debug("Heap-dump-on-OOM armed at {}", dump);
      } catch (Throwable var2) {
         Throwable t = var2;
         log.warn("Unable to enable heap-dump-on-OOM", t);
      }

   }

   public static void main(String[] args) throws Exception {
      Locale.setDefault(Locale.ENGLISH);
      enableHeapDumpOnOom();
      OptionParser parser = new OptionParser(false);
      parser.accepts("developer-mode", "Enable developer tools");
      parser.accepts("debug", "Show extra debugging output");
      parser.accepts("safe-mode", "Disables external plugins and the GPU plugin");
      parser.accepts("insecure-skip-tls-verification", "Disables TLS verification");
      parser.accepts("jav_config", "jav_config url").withRequiredArg().defaultsTo(RuneLiteProperties.getJavConfig(), new String[0]);
      parser.accepts("disable-telemetry", "Disable telemetry");
      parser.accepts("profile", "Configuration profile to use").withRequiredArg();
      parser.accepts("noupdate", "Skips the launcher update");
      ArgumentAcceptingOptionSpec<File> sessionfile = parser.accepts("sessionfile", "Use a specified session file").withRequiredArg().withValuesConvertedBy(new ConfigFileConverter()).defaultsTo(DEFAULT_SESSION_FILE, new File[0]);
      OptionSpec<Void> insecureWriteCredentials = parser.accepts("insecure-write-credentials", "Dump authentication tokens from the Jagex Launcher to a text file to be used for development");
      parser.accepts("help", "Show this text").forHelp();
      OptionSet options = parser.parse(args);
      if (options.has("help")) {
         parser.printHelpOn(System.out);
         System.exit(0);
      }

      if (options.has("debug")) {
         ch.qos.logback.classic.Logger logger = (ch.qos.logback.classic.Logger)LoggerFactory.getLogger("ROOT");
         logger.setLevel(Level.DEBUG);
      }

      Thread.setDefaultUncaughtExceptionHandler((thread, throwable) -> {
         log.error("Uncaught exception:", throwable);
         if (throwable instanceof AbstractMethodError) {
            log.error("Classes are out of date; Build with Gradle again.");
         }

      });
      OkHttpClient okHttpClient = buildHttpClient(options.has("insecure-skip-tls-verification"));
      RuneLiteAPI.CLIENT = okHttpClient;
      SplashScreen.init();
      SplashScreen.stage(0.0, "Preparing RuneScape", "");

      try {
         RuntimeConfigLoader runtimeConfigLoader = new RuntimeConfigLoader(okHttpClient);
         ClientLoader clientLoader = new ClientLoader(okHttpClient, runtimeConfigLoader, (String)options.valueOf("jav_config"));
         (new Thread(() -> {
            clientLoader.get();
            ClassPreloader.preload();
         }, "Preloader")).start();
         boolean developerMode = options.has("developer-mode") && RuneLiteProperties.getLauncherVersion() == null;
         if (developerMode) {
            boolean assertions = false;
            if (!$assertionsDisabled) {
               assertions = true;
               if (false) {
                  throw new AssertionError();
               }
            }

            if (!assertions) {
               SwingUtilities.invokeLater(() -> {
                  (new FatalErrorDialog("Developers should enable assertions; Add `-ea` to your JVM arguments`")).addHelpButtons().addBuildingGuide().open();
               });
               return;
            }
         }

         log.info("RuneLite {} (launcher version {}) starting up, args: {}", new Object[]{RuneLiteProperties.getVersion(), MoreObjects.firstNonNull(RuneLiteProperties.getLauncherVersion(), "unknown"), args.length == 0 ? "none" : String.join(" ", args)});
         RuntimeMXBean runtime = ManagementFactory.getRuntimeMXBean();
         log.info("Java VM arguments: {}", String.join(" ", runtime.getInputArguments()));
         long start = System.currentTimeMillis();
         injector = Guice.createInjector(new Module[]{new RuneLiteModule(okHttpClient, clientLoader, runtimeConfigLoader, developerMode, options.has("safe-mode"), options.has("disable-telemetry"), (File)options.valueOf(sessionfile), (String)options.valueOf("profile"), options.has(insecureWriteCredentials), options.has("noupdate"))});
         ((RuneLite)injector.getInstance(RuneLite.class)).start();
         long end = System.currentTimeMillis();
         long uptime = runtime.getUptime();
         log.info("Client initialization took {}ms. Uptime: {}ms", end - start, uptime);
      } catch (Exception var19) {
         Exception e = var19;
         log.error("Failure during startup", e);
         SwingUtilities.invokeLater(() -> {
            (new FatalErrorDialog("RuneLite has encountered an unexpected error during startup.")).addHelpButtons().open();
         });
      } finally {
         SplashScreen.stop();
      }

   }

   public void start() throws Exception {
      injector.injectMembers(this.client);
      this.setupSystemProps();
      this.setupCompilerControl();
      copyJagexCache();
      System.setProperty("jagex.disableBouncyCastle", "true");
      System.setProperty("jagex.userhome", RUNELITE_DIR.getAbsolutePath());
      this.client.initialize();
      SplashScreen.stage(0.57, (String)null, "Loading configuration");
      this.sessionManager.loadSession();
      this.configManager.load();
      Updater updater = (Updater)injector.getInstance(Updater.class);
      updater.update();
      this.pluginManager.loadCorePlugins();
      this.externalPluginManager.loadExternalPlugins();
      SplashScreen.stage(0.7, (String)null, "Finalizing configuration");
      this.pluginManager.loadDefaultPluginConfiguration((Collection)null);
      this.clientSessionManager.start();
      this.eventBus.register(this.clientSessionManager);
      SplashScreen.stage(0.75, (String)null, "Starting core interface");
      this.clientUI.init();
      this.discordService.init();
      this.eventBus.register(this.clientUI);
      this.eventBus.register(this.pluginManager);
      this.eventBus.register(this.externalPluginManager);
      this.eventBus.register(this.overlayManager);
      this.eventBus.register(this.configManager);
      this.eventBus.register(this.discordService);
      Collection var10000 = WidgetOverlay.createOverlays(this.overlayManager, this.client);
      OverlayManager var10001 = this.overlayManager;
      Objects.requireNonNull(var10001);
      var10000.forEach(var10001::add);
      this.overlayManager.add((Overlay)this.worldMapOverlay.get());
      this.overlayManager.add((Overlay)this.tooltipOverlay.get());
      injector.getInstance(DeveloperModeKeyListener.class);
      this.pluginManager.startPlugins();
      SplashScreen.stop();
      this.clientUI.show();
      this.client.unblockStartup();
      if (this.telemetryClient != null) {
         this.scheduledExecutorService.execute(() -> {
            this.telemetryClient.submitTelemetry();
            this.telemetryClient.submitVmErrors(LOGS_DIR);
         });
      }

      ReflectUtil.queueInjectorAnnotationCacheInvalidation(injector);
      ReflectUtil.invalidateAnnotationCaches();
   }

   @VisibleForTesting
   public static void setInjector(Injector injector) {
      RuneLite.injector = injector;
   }

   @VisibleForTesting
   static OkHttpClient buildHttpClient(boolean insecureSkipTlsVerification) {
      OkHttpClient.Builder builder = (new OkHttpClient.Builder()).pingInterval(30L, TimeUnit.SECONDS).addInterceptor((chain) -> {
         Request request = chain.request();
         if (request.header("User-Agent") != null) {
            return chain.proceed(request);
         } else {
            Request userAgentRequest = request.newBuilder().header("User-Agent", USER_AGENT).build();
            return chain.proceed(userAgentRequest);
         }
      }).cache(new Cache(new File(CACHE_DIR, "okhttp"), 20971520L)).addNetworkInterceptor((chain) -> {
         Response res = chain.proceed(chain.request());
         if (res.code() >= 400 && "GET".equals(res.request().method())) {
            res = res.newBuilder().header("Cache-Control", "no-store").build();
         }

         return res;
      });

      try {
         if (!insecureSkipTlsVerification && !RuneLiteProperties.isInsecureSkipTlsVerification()) {
            setupTrustManager(builder);
         } else {
            setupInsecureTrustManager(builder);
         }
      } catch (KeyManagementException | NoSuchAlgorithmException | KeyStoreException var3) {
         GeneralSecurityException e = var3;
         log.warn("error setting up trust manager", e);
      }

      return builder.build();
   }

   private static void copyJagexCache() {
      Path from = Paths.get(System.getProperty("user.home"), "jagexcache");
      Path to = Paths.get(System.getProperty("user.home"), ".augustrsps", "jagexcache");
      if (!Files.exists(to, new LinkOption[0]) && Files.exists(from, new LinkOption[0])) {
         log.info("Copying jagexcache from {} to {}", from, to);

         try {
            Stream<Path> stream = Files.walk(from);

            try {
               stream.forEach((source) -> {
                  try {
                     Files.copy(source, to.resolve(from.relativize(source)), StandardCopyOption.COPY_ATTRIBUTES);
                  } catch (IOException var4) {
                     IOException e = var4;
                     throw new RuntimeException(e);
                  }
               });
            } catch (Throwable var6) {
               if (stream != null) {
                  try {
                     stream.close();
                  } catch (Throwable var5) {
                     var6.addSuppressed(var5);
                  }
               }

               throw var6;
            }

            if (stream != null) {
               stream.close();
            }
         } catch (Exception var7) {
            Exception e = var7;
            log.warn("unable to copy jagexcache", e);
         }

      }
   }

   private void setupSystemProps() {
      if (this.runtimeConfig != null && this.runtimeConfig.getSysProps() != null) {
         Iterator var1 = this.runtimeConfig.getSysProps().entrySet().iterator();

         while(var1.hasNext()) {
            Map.Entry<String, String> entry = (Map.Entry)var1.next();
            String key = (String)entry.getKey();
            String value = (String)entry.getValue();
            log.debug("Setting property {}={}", key, value);
            System.setProperty(key, value);
         }

      }
   }

   private void setupCompilerControl() {
      try {
         Path file = Files.createTempFile("rl_compilercontrol", "");

         try {
            if (this.runtimeConfig != null && this.runtimeConfig.getCompilerControl() != null) {
               String json = this.gson.toJson(this.runtimeConfig.getCompilerControl());
               Files.writeString(file, json, StandardCharsets.UTF_8);
            } else {
               InputStream in = RuneLite.class.getResourceAsStream("/compilercontrol.json");

               try {
                  Files.copy(in, file, new CopyOption[]{StandardCopyOption.REPLACE_EXISTING});
               } catch (Throwable var11) {
                  if (in != null) {
                     try {
                        in.close();
                     } catch (Throwable var10) {
                        var11.addSuppressed(var10);
                     }
                  }

                  throw var11;
               }

               if (in != null) {
                  in.close();
               }
            }

            ManagementFactory.getPlatformMBeanServer().invoke(new ObjectName("com.sun.management:type=DiagnosticCommand"), "compilerDirectivesAdd", new Object[]{new String[]{file.toFile().getAbsolutePath()}}, new String[]{String[].class.getName()});
         } finally {
            Files.delete(file);
         }
      } catch (Exception var13) {
         Exception e = var13;
         log.info("Failed to set compiler control", e);
      }

   }

   private static TrustManager[] loadTrustManagers(String trustStoreType) throws KeyStoreException, NoSuchAlgorithmException {
      String old;
      if (trustStoreType != null) {
         old = System.setProperty("javax.net.ssl.trustStoreType", trustStoreType);
      } else {
         old = System.clearProperty("javax.net.ssl.trustStoreType");
      }

      TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
      trustManagerFactory.init((KeyStore)null);
      TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
      if (old == null) {
         System.clearProperty("javax.net.ssl.trustStoreType");
      } else {
         System.setProperty("javax.net.ssl.trustStoreType", old);
      }

      return trustManagers;
   }

   private static void setupTrustManager(OkHttpClient.Builder okHttpClientBuilder) throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
      if (OSType.getOSType() == OSType.Windows) {
         TrustManager[] jreTms = loadTrustManagers((String)null);
         TrustManager[] windowsTms = loadTrustManagers("Windows-ROOT");
         final TrustManager[] trustManagers = new TrustManager[jreTms.length + windowsTms.length];
         System.arraycopy(jreTms, 0, trustManagers, 0, jreTms.length);
         System.arraycopy(windowsTms, 0, trustManagers, jreTms.length, windowsTms.length);
         X509TrustManager combiningTrustManager = new X509TrustManager() {
            public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
               CertificateException exception = null;
               TrustManager[] var4 = trustManagers;
               int var5 = var4.length;

               for(int var6 = 0; var6 < var5; ++var6) {
                  TrustManager trustManager = var4[var6];
                  if (trustManager instanceof X509TrustManager) {
                     try {
                        ((X509TrustManager)trustManager).checkClientTrusted(chain, authType);
                        return;
                     } catch (CertificateException var9) {
                        CertificateException ex = var9;
                        exception = ex;
                     }
                  }
               }

               if (exception != null) {
                  throw exception;
               } else {
                  throw new CertificateException("no X509TrustManagers present");
               }
            }

            public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
               CertificateException exception = null;
               TrustManager[] var4 = trustManagers;
               int var5 = var4.length;

               for(int var6 = 0; var6 < var5; ++var6) {
                  TrustManager trustManager = var4[var6];
                  if (trustManager instanceof X509TrustManager) {
                     try {
                        ((X509TrustManager)trustManager).checkServerTrusted(chain, authType);
                        return;
                     } catch (CertificateException var9) {
                        CertificateException ex = var9;
                        exception = ex;
                     }
                  }
               }

               if (exception != null) {
                  throw exception;
               } else {
                  throw new CertificateException("no X509TrustManagers present");
               }
            }

            public X509Certificate[] getAcceptedIssuers() {
               List<X509Certificate> certificates = new ArrayList();
               TrustManager[] var2 = trustManagers;
               int var3 = var2.length;

               for(int var4 = 0; var4 < var3; ++var4) {
                  TrustManager trustManager = var2[var4];
                  if (trustManager instanceof X509TrustManager) {
                     certificates.addAll(Arrays.asList(((X509TrustManager)trustManager).getAcceptedIssuers()));
                  }
               }

               return (X509Certificate[])certificates.toArray(new X509Certificate[0]);
            }
         };
         SSLContext sc = SSLContext.getInstance("TLS");
         sc.init((KeyManager[])null, new TrustManager[]{combiningTrustManager}, new SecureRandom());
         okHttpClientBuilder.sslSocketFactory(sc.getSocketFactory(), combiningTrustManager);
      }
   }

   private static void setupInsecureTrustManager(OkHttpClient.Builder okHttpClientBuilder) throws NoSuchAlgorithmException, KeyManagementException {
      X509TrustManager trustManager = new X509TrustManager() {
         public void checkClientTrusted(X509Certificate[] chain, String authType) {
         }

         public void checkServerTrusted(X509Certificate[] chain, String authType) {
         }

         public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
         }
      };
      SSLContext sc = SSLContext.getInstance("TLS");
      sc.init((KeyManager[])null, new TrustManager[]{trustManager}, new SecureRandom());
      okHttpClientBuilder.sslSocketFactory(sc.getSocketFactory(), trustManager);
   }

   public static Injector getInjector() {
      return injector;
   }

   static {
      CACHE_DIR = new File(RUNELITE_DIR, "cache");
      PLUGINS_DIR = new File(RUNELITE_DIR, "plugins");
      SCREENSHOT_DIR = new File(RUNELITE_DIR, "screenshots");
      LOGS_DIR = new File(RUNELITE_DIR, "logs");
      DEFAULT_SESSION_FILE = new File(RUNELITE_DIR, "session");
      NOTIFICATIONS_DIR = new File(RUNELITE_DIR, "notifications");
      FONTS_DIR = new File(RUNELITE_DIR, "fonts");
      String var10000 = RuneLiteProperties.getVersion();
      USER_AGENT = "RuneLite/" + var10000 + "-" + RuneLiteProperties.getCommit() + (RuneLiteProperties.isDirty() ? "+" : "");
   }

   private static class ConfigFileConverter implements ValueConverter<File> {
      public File convert(String fileName) {
         File file;
         if (!Paths.get(fileName).isAbsolute() && !fileName.startsWith("./") && !fileName.startsWith(".\\")) {
            file = new File(RuneLite.RUNELITE_DIR, fileName);
         } else {
            file = new File(fileName);
         }

         if (!file.exists() || file.isFile() && file.canWrite()) {
            return file;
         } else {
            throw new ValueConversionException(String.format("File %s is not accessible", file.getAbsolutePath()));
         }
      }

      public Class<? extends File> valueType() {
         return File.class;
      }

      public String valuePattern() {
         return null;
      }
   }
}
