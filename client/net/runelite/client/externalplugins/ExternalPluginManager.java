package net.runelite.client.externalplugins;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.google.common.hash.Hashing;
import com.google.common.hash.HashingInputStream;
import com.google.common.io.FileWriteMode;
import com.google.common.io.Files;
import com.google.gson.Gson;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Stream;
import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.swing.SwingUtilities;
import net.runelite.client.RuneLite;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.EventBus;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ExternalPluginsChanged;
import net.runelite.client.events.ProfileChanged;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginInstantiationException;
import net.runelite.client.plugins.PluginManager;
import net.runelite.client.ui.SplashScreen;
import net.runelite.client.util.CountingInputStream;
import net.runelite.client.util.Text;
import net.runelite.client.util.VerificationException;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
public class ExternalPluginManager {
   private static final Logger log = LoggerFactory.getLogger(ExternalPluginManager.class);
   private static final String PLUGIN_LIST_KEY = "externalPlugins";
   private static final ImmutableSet<String> BLACKLISTED_EXTERNAL_PLUGINS = ImmutableSet.of("rune-pouch");
   private static Class<? extends Plugin>[] builtinExternals = null;
   @Inject
   @Named("safeMode")
   private boolean safeMode;
   private final ConfigManager configManager;
   private final ExternalPluginClient externalPluginClient;
   private final ScheduledExecutorService executor;
   private final PluginManager pluginManager;
   private final EventBus eventBus;
   private final OkHttpClient okHttpClient;
   private final Gson gson;

   @Inject
   private ExternalPluginManager(ConfigManager configManager, ExternalPluginClient externalPluginClient, ScheduledExecutorService executor, PluginManager pluginManager, EventBus eventBus, OkHttpClient okHttpClient, Gson gson) {
      this.configManager = configManager;
      this.externalPluginClient = externalPluginClient;
      this.executor = executor;
      this.pluginManager = pluginManager;
      this.eventBus = eventBus;
      this.okHttpClient = okHttpClient;
      this.gson = gson;
      executor.scheduleWithFixedDelay(() -> {
         externalPluginClient.submitPlugins(this.getInstalledExternalPlugins());
      }, (long)(new Random()).nextInt(60), 180L, TimeUnit.MINUTES);
   }

   public void loadExternalPlugins() throws PluginInstantiationException {
      this.refreshPlugins();
      if (builtinExternals != null) {
         this.pluginManager.loadPlugins(Lists.newArrayList(builtinExternals), true, (BiConsumer)null);
      }

   }

   @Subscribe
   public void onProfileChanged(ProfileChanged profileChanged) {
      this.executor.submit(this::refreshPlugins);
   }

   private void refreshPlugins() {
      if (this.safeMode) {
         log.debug("External plugins are disabled in safe mode!");
      } else {
         Set<String> builtinExternalClasses = new HashSet();
         if (builtinExternals != null) {
            Class[] var2 = builtinExternals;
            int var3 = var2.length;

            for(int var4 = 0; var4 < var3; ++var4) {
               Class<? extends Plugin> pluginClass = var2[var4];
               builtinExternalClasses.add(pluginClass.getName());
            }
         }

         Multimap<PluginHubManifest.JarData, Plugin> loadedExternalPlugins = HashMultimap.create();
         Iterator var45 = this.pluginManager.getPlugins().iterator();

         while(var45.hasNext()) {
            Plugin p = (Plugin)var45.next();
            PluginHubManifest.JarData m = getJarData(p.getClass());
            if (m != null) {
               loadedExternalPlugins.put(m, p);
            }
         }

         List<String> installedIDs = this.getInstalledExternalPlugins();
         if (!installedIDs.isEmpty() || !loadedExternalPlugins.isEmpty()) {
            boolean startup = SplashScreen.isOpen();

            try {
               double splashStart = startup ? 0.6 : 0.0;
               double splashLength = startup ? 0.1 : 1.0;
               if (!startup) {
                  SplashScreen.init();
               }

               Instant now = Instant.now();
               Instant keepAfter = now.minus(3L, ChronoUnit.DAYS);
               SplashScreen.stage(splashStart, (String)null, "Downloading external plugins");
               Set<PluginHubManifest.JarData> externalPlugins = new HashSet();
               RuneLite.PLUGINS_DIR.mkdirs();

               Iterator newPlugins;
               try {
                  PluginHubManifest.ManifestLite manifest = this.externalPluginClient.downloadManifestLite();
                  Map<String, PluginHubManifest.JarData> manifests = (Map)manifest.getJars().stream().collect(ImmutableMap.toImmutableMap(PluginHubManifest.JarData::getInternalName, Function.identity()));
                  Set<PluginHubManifest.JarData> needsDownload = new HashSet();
                  Set<File> keep = new HashSet();
                  newPlugins = installedIDs.iterator();

                  while(newPlugins.hasNext()) {
                     String name = (String)newPlugins.next();
                     if (BLACKLISTED_EXTERNAL_PLUGINS.contains(name)) {
                        log.info("Skipping blacklisted external plugin \"{}\"", name);
                     } else {
                        PluginHubManifest.JarData jarData = (PluginHubManifest.JarData)manifests.get(name);
                        if (jarData != null) {
                           externalPlugins.add(jarData);
                           jarData.getJarFile().setLastModified(now.toEpochMilli());
                           if (!jarData.isValid()) {
                              needsDownload.add(jarData);
                           } else {
                              keep.add(jarData.getJarFile());
                           }
                        }
                     }
                  }

                  File[] files = RuneLite.PLUGINS_DIR.listFiles();
                  int downloaded;
                  if (files != null) {
                     File[] var61 = files;
                     downloaded = files.length;

                     for(int var19 = 0; var19 < downloaded; ++var19) {
                        File fi = var61[var19];
                        if (!keep.contains(fi) && fi.lastModified() < keepAfter.toEpochMilli()) {
                           fi.delete();
                        }
                     }
                  }

                  int toDownload = needsDownload.stream().mapToInt(PluginHubManifest.JarData::getJarSize).sum();
                  downloaded = 0;
                  Iterator var69 = needsDownload.iterator();

                  while(var69.hasNext()) {
                     PluginHubManifest.JarData jarData = (PluginHubManifest.JarData)var69.next();
                     HttpUrl url = this.externalPluginClient.getJarURL(jarData);

                     try {
                        Response res = this.okHttpClient.newCall((new Request.Builder()).url(url).build()).execute();

                        try {
                           int fdownloaded = downloaded;
                           downloaded += jarData.getJarSize();
                           HashingInputStream his = new HashingInputStream(Hashing.sha256(), new CountingInputStream(res.body().byteStream(), (i) -> {
                              SplashScreen.stage(splashStart + splashLength * 0.2, splashStart + splashLength * 0.8, (String)null, "Downloading " + jarData.getDisplayName(), i + fdownloaded, toDownload, true);
                           }));
                           Files.asByteSink(jarData.getJarFile(), new FileWriteMode[0]).writeFrom(his);
                           if (!PluginHubManifest.HASH_ENCODER.encodeToString(his.hash().asBytes()).equals(jarData.getJarHash())) {
                              throw new VerificationException("Plugin " + jarData.getInternalName() + " didn't match its hash");
                           }
                        } catch (Throwable var40) {
                           if (res != null) {
                              try {
                                 res.close();
                              } catch (Throwable var37) {
                                 var40.addSuppressed(var37);
                              }
                           }

                           throw var40;
                        }

                        if (res != null) {
                           res.close();
                        }
                     } catch (VerificationException | IOException var41) {
                        Exception e = var41;
                        externalPlugins.remove(jarData);
                        log.error("Unable to download external plugin \"{}\"", jarData.getInternalName(), e);
                     }
                  }
               } catch (VerificationException | IOException var42) {
                  Exception e = var42;
                  log.error("Unable to download external plugins", e);
                  return;
               }

               SplashScreen.stage(splashStart + splashLength * 0.8, (String)null, "Starting external plugins");
               Set<PluginHubManifest.JarData> add = new HashSet();
               Iterator var53 = externalPlugins.iterator();

               while(var53.hasNext()) {
                  PluginHubManifest.JarData jarData = (PluginHubManifest.JarData)var53.next();
                  if (loadedExternalPlugins.removeAll(jarData).size() <= 0) {
                     add.add(jarData);
                  }
               }

               Collection<Plugin> remove = loadedExternalPlugins.values();

               Iterator var56;
               Plugin p;
               for(var56 = remove.iterator(); var56.hasNext(); this.pluginManager.remove(p)) {
                  p = (Plugin)var56.next();
                  log.info("Stopping external plugin \"{}\"", p.getClass());

                  try {
                     SwingUtilities.invokeAndWait(() -> {
                        try {
                           this.pluginManager.stopPlugin(p);
                        } catch (Exception var3) {
                           Exception e = var3;
                           throw new RuntimeException(e);
                        }
                     });
                  } catch (InvocationTargetException | InterruptedException var36) {
                     log.warn("Unable to stop external plugin \"{}\"", p.getClass().getName(), var36);
                  }
               }

               var56 = add.iterator();

               while(var56.hasNext()) {
                  PluginHubManifest.JarData jarData = (PluginHubManifest.JarData)var56.next();
                  if (!jarData.isValid()) {
                     log.warn("Invalid plugin for validated manifest: {}", jarData);
                  } else {
                     log.info("Loading external plugin \"{}\" jar \"{}\"", jarData.getInternalName(), jarData.getJarHash());
                     newPlugins = null;

                     try {
                        PluginHubClassLoader cl = new PluginHubClassLoader(jarData, new URL[]{jarData.getJarFile().toURI().toURL()}, this.gson);
                        Stream var10000 = Arrays.stream(cl.getStub().getPlugins());
                        Objects.requireNonNull(builtinExternalClasses);
                        if (var10000.anyMatch(builtinExternalClasses::contains)) {
                           log.debug("Skipping loading \"{}\" from hub as a conflicting builtin external is present", jarData.getInternalName());
                        } else {
                           List<Class<?>> clazzes = new ArrayList();
                           String[] var73 = cl.getStub().getPlugins();
                           int var76 = var73.length;

                           for(int var77 = 0; var77 < var76; ++var77) {
                              String className = var73[var77];
                              clazzes.add(cl.loadClass(className));
                           }

                           List newPlugins;
                           List<Plugin> newPlugins2 = newPlugins = this.pluginManager.loadPlugins(clazzes, true, (BiConsumer)null);
                           if (!startup) {
                              this.pluginManager.loadDefaultPluginConfiguration(newPlugins);
                              SwingUtilities.invokeAndWait(() -> {
                                 try {
                                    Iterator var5 = newPlugins2.iterator();

                                    while(var5.hasNext()) {
                                       Plugin p = (Plugin)var5.next();
                                       this.pluginManager.startPlugin(p);
                                    }

                                 } catch (PluginInstantiationException var4) {
                                    PluginInstantiationException e = var4;
                                    throw new RuntimeException(e);
                                 }
                              });
                           }
                        }
                     } catch (ThreadDeath var38) {
                        throw var38;
                     } catch (Throwable var39) {
                        log.warn("Unable to start or load external plugin \"{}\"", jarData.getInternalName(), var39);
                        Plugin p;
                        if (newPlugins != null) {
                           for(Iterator var68 = newPlugins.iterator(); var68.hasNext(); this.pluginManager.remove(p)) {
                              p = (Plugin)var68.next();

                              try {
                                 SwingUtilities.invokeAndWait(() -> {
                                    try {
                                       this.pluginManager.stopPlugin(p);
                                    } catch (Exception var3) {
                                       Exception e2 = var3;
                                       throw new RuntimeException(e2);
                                    }
                                 });
                              } catch (InvocationTargetException | InterruptedException var35) {
                                 log.info("Unable to fully stop plugin \"{}\"", jarData.getInternalName(), var35);
                              }
                           }
                        }
                     }
                  }
               }

               if (!startup) {
                  this.eventBus.post(new ExternalPluginsChanged());
               }

            } finally {
               if (!startup) {
                  SplashScreen.stop();
               }

            }
         }
      }
   }

   public List<String> getInstalledExternalPlugins() {
      String externalPluginsStr = this.configManager.getConfiguration("runelite", "externalPlugins");
      return Text.fromCSV(externalPluginsStr == null ? "" : externalPluginsStr);
   }

   public void install(String key) {
      Set<String> plugins = new HashSet(this.getInstalledExternalPlugins());
      if (plugins.add(key)) {
         this.configManager.setConfiguration("runelite", "externalPlugins", Text.toCSV(plugins));
         this.executor.submit(this::refreshPlugins);
      }

   }

   public void remove(String key) {
      Set<String> plugins = new HashSet(this.getInstalledExternalPlugins());
      if (plugins.remove(key)) {
         this.configManager.setConfiguration("runelite", "externalPlugins", Text.toCSV(plugins));
         this.executor.submit(this::refreshPlugins);
      }

   }

   public void update() {
      this.executor.submit(this::refreshPlugins);
   }

   @Nullable
   public static PluginHubManifest.JarData getJarData(Class<? extends Plugin> plugin) {
      ClassLoader cl = plugin.getClassLoader();
      if (cl instanceof PluginHubClassLoader) {
         PluginHubClassLoader ecl = (PluginHubClassLoader)cl;
         return ecl.getJarData();
      } else {
         return null;
      }
   }

   @Nullable
   public static PluginHubManifest.DisplayData getDisplayData(Class<? extends Plugin> plugin) {
      ClassLoader cl = plugin.getClassLoader();
      if (cl instanceof PluginHubClassLoader) {
         PluginHubClassLoader ecl = (PluginHubClassLoader)cl;
         return ecl.getStub();
      } else {
         return null;
      }
   }

   @Nullable
   public static String getInternalName(Class<? extends Plugin> plugin) {
      PluginHubManifest.JarData jd = getJarData(plugin);
      return jd == null ? null : jd.getInternalName();
   }

   public static void loadBuiltin(Class<? extends Plugin>... plugins) {
      boolean assertsEnabled = false;
      if (!$assertionsDisabled) {
         assertsEnabled = true;
         if (false) {
            throw new AssertionError();
         }
      }

      if (!assertsEnabled) {
         throw new RuntimeException("Assertions are not enabled, add '-ea' to your VM options. Enabling assertions during development catches undefined behavior and incorrect API usage.");
      } else {
         builtinExternals = plugins;
      }
   }
}
