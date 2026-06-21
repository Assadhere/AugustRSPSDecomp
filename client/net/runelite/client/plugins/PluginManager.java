package net.runelite.client.plugins;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import com.google.common.graph.Graph;
import com.google.common.graph.GraphBuilder;
import com.google.common.graph.Graphs;
import com.google.common.graph.MutableGraph;
import com.google.common.reflect.ClassPath;
import com.google.inject.CreationException;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.Module;
import java.io.IOException;
import java.lang.invoke.CallSite;
import java.lang.invoke.LambdaMetafactory;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;
import javax.inject.Singleton;
import javax.swing.SwingUtilities;
import net.runelite.client.RuneLite;
import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.EventBus;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.PluginChanged;
import net.runelite.client.events.ProfileChanged;
import net.runelite.client.externalplugins.ExternalPluginMdc;
import net.runelite.client.task.Schedule;
import net.runelite.client.task.ScheduledMethod;
import net.runelite.client.task.Scheduler;
import net.runelite.client.ui.SplashScreen;
import net.runelite.client.util.GameEventManager;
import net.runelite.client.util.ReflectUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
public class PluginManager {
   private static final Logger log = LoggerFactory.getLogger(PluginManager.class);
   private static final String PLUGIN_PACKAGE = "net.runelite.client.plugins";
   private volatile boolean developerMode;
   private final boolean safeMode;
   private final EventBus eventBus;
   private final Scheduler scheduler;
   private final ConfigManager configManager;
   private final Provider<GameEventManager> sceneTileManager;
   private final List<Plugin> plugins = new CopyOnWriteArrayList();
   private final List<Plugin> externalPlugins = new CopyOnWriteArrayList();
   private final List<Plugin> activePlugins = new CopyOnWriteArrayList();
   private static final ImmutableList<String> WHITELISTED_PLUGINS = ImmutableList.of("Particle Effects", "Account", "Ammo", "Animation Smoothing", "Anti Drag", "Attack Styles", "August Donator Info", "August Events", "August Prestige Info", "Cobalt", "Bank", "Bank Tags", new String[]{"Beginner Tooltips", "Boosts Information", "Boss Timers", "Camera", "Chat Filter", "Chat History", "Chat Notifications", "Chat Timestamps", "Combat Level", "Custom Cursor", "Discord", "DPS Counter", "Emojis", "Entity Hider", "Examine", "Fishing", "FPS Control", "Friend List", "Friend Notes", "Ground Items", "Ground Markers", "GPU", "Idle Notifier", "Info Panel", "Instance Map", "Interact Highlight", "Interface Styles", "Inventory Grid", "Inventory Tags", "Inventory Viewer", "Item Identification", "Item Prices", "Item Stats", "Key Remapping", "Login Screen", "Loot Tracker", "Logout Timer", "Low Detail", "Menu Entry Swapper", "Metronome", "Minimap", "Mining", "Model Tools", "Mouse Tooltips", "Notes", "NPC Aggression Timer", "NPC Indicators", "Object Markers", "Opponent Information", "Player Indicators", "Prayer", "Regeneration Meter", "Roof Removal", "Run Energy", "Rune Pouch", "Screenshot", "Screen Markers", "Skill Calculator", "Skybox", "Slayer", "Spellbook", "Stretched Mode", "Status Bars", "Timers & Buffs", "Tile Indicators", "Woodcutting", "XP Drop", "XP Globes", "XP Tracker", "XP Updater", "Zalcano"});

   @Inject
   @VisibleForTesting
   PluginManager(@Named("developerMode") boolean developerMode, @Named("safeMode") boolean safeMode, EventBus eventBus, Scheduler scheduler, ConfigManager configManager, Provider<GameEventManager> sceneTileManager) {
      this.developerMode = developerMode;
      this.safeMode = safeMode;
      this.eventBus = eventBus;
      this.scheduler = scheduler;
      this.configManager = configManager;
      this.sceneTileManager = sceneTileManager;
   }

   public boolean isDeveloperMode() {
      return this.developerMode;
   }

   public void enableDeveloperMode() throws IOException, PluginInstantiationException {
      if (!this.developerMode) {
         this.developerMode = true;
         log.info("Developer mode enabled at runtime");
         ClassPath classPath = ClassPath.from(this.getClass().getClassLoader());
         List<Class<?>> pluginClasses = (List)classPath.getTopLevelClassesRecursive("net.runelite.client.plugins").stream().map(ClassPath.ClassInfo::load).collect(Collectors.toList());
         Set<Class<?>> loadedClasses = (Set)this.plugins.stream().map(Object::getClass).collect(Collectors.toSet());
         List<Class<?>> devPluginClasses = (List)pluginClasses.stream().filter((clazz) -> {
            PluginDescriptor desc = (PluginDescriptor)clazz.getAnnotation(PluginDescriptor.class);
            return desc != null && desc.developerPlugin() && !loadedClasses.contains(clazz);
         }).collect(Collectors.toList());
         if (!devPluginClasses.isEmpty()) {
            List<Plugin> newPlugins = this.loadPlugins(devPluginClasses, false, (BiConsumer)null);
            this.loadDefaultPluginConfiguration(newPlugins);
            SwingUtilities.invokeLater(() -> {
               Iterator var2 = newPlugins.iterator();

               Plugin plugin;
               while(var2.hasNext()) {
                  plugin = (Plugin)var2.next();

                  try {
                     this.startPlugin(plugin);
                  } catch (PluginInstantiationException var5) {
                     PluginInstantiationException e = var5;
                     log.error("Unable to start developer plugin {}", plugin.getClass().getSimpleName(), e);
                  }
               }

               var2 = newPlugins.iterator();

               while(var2.hasNext()) {
                  plugin = (Plugin)var2.next();
                  ReflectUtil.queueInjectorAnnotationCacheInvalidation(plugin.injector);
               }

            });
         }
      }
   }

   @Subscribe
   public void onProfileChanged(ProfileChanged profileChanged) {
      this.refreshPlugins();
   }

   private void refreshPlugins() {
      this.loadDefaultPluginConfiguration((Collection)null);
      SwingUtilities.invokeLater(() -> {
         Iterator var1 = this.getPlugins().iterator();

         while(var1.hasNext()) {
            Plugin plugin = (Plugin)var1.next();

            try {
               if (this.isPluginEnabled(plugin) != this.activePlugins.contains(plugin)) {
                  if (this.activePlugins.contains(plugin)) {
                     this.stopPlugin(plugin);
                  } else {
                     this.startPlugin(plugin);
                  }
               }
            } catch (PluginInstantiationException var4) {
               PluginInstantiationException e = var4;
               log.error("Error during starting/stopping plugin {}", plugin.getClass().getSimpleName(), e);
            }
         }

      });
   }

   public Config getPluginConfigProxy(Plugin plugin) {
      try {
         Injector injector = plugin.getInjector();
         Iterator var3 = injector.getBindings().keySet().iterator();

         while(var3.hasNext()) {
            Key<?> key = (Key)var3.next();
            Class<?> type = key.getTypeLiteral().getRawType();
            if (Config.class.isAssignableFrom(type)) {
               return (Config)injector.getInstance(key);
            }
         }
      } catch (ThreadDeath var6) {
         ThreadDeath e = var6;
         throw e;
      } catch (Throwable var7) {
         Throwable e = var7;
         log.error("Unable to get plugin config", e);
      }

      return null;
   }

   public List<Config> getPluginConfigProxies(Collection<Plugin> plugins) {
      List<Injector> injectors = new ArrayList();
      if (plugins == null) {
         injectors.add(RuneLite.getInjector());
         plugins = this.getPlugins();
      }

      plugins.forEach((pl) -> {
         injectors.add(pl.getInjector());
      });
      List<Config> list = new ArrayList();
      Iterator var4 = injectors.iterator();

      while(var4.hasNext()) {
         Injector injector = (Injector)var4.next();
         Iterator var6 = injector.getBindings().keySet().iterator();

         while(var6.hasNext()) {
            Key<?> key = (Key)var6.next();
            Class<?> type = key.getTypeLiteral().getRawType();
            if (Config.class.isAssignableFrom(type)) {
               Config config = (Config)injector.getInstance(key);
               list.add(config);
            }
         }
      }

      return list;
   }

   public void loadDefaultPluginConfiguration(Collection<Plugin> plugins) {
      try {
         Iterator var7 = this.getPluginConfigProxies(plugins).iterator();

         while(var7.hasNext()) {
            Config config = (Config)var7.next();
            this.configManager.setDefaultConfiguration(config, false);
         }
      } catch (ThreadDeath var4) {
         ThreadDeath e = var4;
         throw e;
      } catch (Throwable var5) {
         Throwable ex = var5;
         log.error("Unable to reset plugin configuration", ex);
      }

   }

   public void startPlugins() {
      List<Plugin> scannedPlugins = new ArrayList(this.plugins);
      int loaded = 0;
      Iterator var3 = scannedPlugins.iterator();

      Plugin plugin;
      while(var3.hasNext()) {
         plugin = (Plugin)var3.next();

         try {
            SwingUtilities.invokeAndWait(() -> {
               try {
                  this.startPlugin(plugin);
               } catch (PluginInstantiationException var3) {
                  PluginInstantiationException ex = var3;
                  log.error("Unable to start plugin {}", plugin.getClass().getSimpleName(), ex);
               }

            });
         } catch (InvocationTargetException | InterruptedException var6) {
            Exception e = var6;
            throw new RuntimeException(e);
         }

         ++loaded;
         SplashScreen.stage(0.8, 1.0, (String)null, "Starting plugins", loaded, scannedPlugins.size(), false);
      }

      var3 = this.plugins.iterator();

      while(var3.hasNext()) {
         plugin = (Plugin)var3.next();
         ReflectUtil.queueInjectorAnnotationCacheInvalidation(plugin.injector);
      }

   }

   public void loadCorePlugins() throws IOException, PluginInstantiationException {
      SplashScreen.stage(0.59, (String)null, "Loading plugins");
      ClassPath classPath = ClassPath.from(this.getClass().getClassLoader());
      List<Class<?>> plugins = (List)classPath.getTopLevelClassesRecursive("net.runelite.client.plugins").stream().map(ClassPath.ClassInfo::load).collect(Collectors.toList());
      this.loadPlugins(plugins, false, (loaded, total) -> {
         SplashScreen.stage(0.6, 0.7, (String)null, "Loading plugins", loaded, total, false);
      });
   }

   public List<Plugin> loadPlugins(List<Class<?>> plugins, boolean isExternalPlugins, BiConsumer<Integer, Integer> onPluginLoaded) throws PluginInstantiationException {
      MutableGraph<Class<? extends Plugin>> graph = GraphBuilder.directed().build();
      Iterator var5 = plugins.iterator();

      while(true) {
         Class clazz;
         while(var5.hasNext()) {
            clazz = (Class)var5.next();
            PluginDescriptor pluginDescriptor = (PluginDescriptor)clazz.getAnnotation(PluginDescriptor.class);
            if (pluginDescriptor == null) {
               if (clazz.getSuperclass() == Plugin.class) {
                  log.error("Class {} is a plugin, but has no plugin descriptor", clazz);
               }
            } else if (clazz.getSuperclass() != Plugin.class) {
               log.error("Class {} has plugin descriptor, but is not a plugin", clazz);
            } else if (!pluginDescriptor.developerPlugin() || this.developerMode) {
               if (this.safeMode && !pluginDescriptor.loadInSafeMode()) {
                  log.debug("Disabling {} due to safe mode", clazz);
                  this.configManager.setConfiguration("runelite", (Strings.isNullOrEmpty(pluginDescriptor.configName()) ? clazz.getSimpleName() : pluginDescriptor.configName()).toLowerCase(), (Object)false);
               } else {
                  graph.addNode(clazz);
               }
            }
         }

         var5 = graph.nodes().iterator();

         while(var5.hasNext()) {
            clazz = (Class)var5.next();
            PluginDependency[] pluginDependencies = (PluginDependency[])clazz.getAnnotationsByType(PluginDependency.class);
            PluginDependency[] var8 = pluginDependencies;
            int var9 = pluginDependencies.length;

            for(int var10 = 0; var10 < var9; ++var10) {
               PluginDependency pluginDependency = var8[var10];
               if (graph.nodes().contains(pluginDependency.value())) {
                  graph.putEdge(pluginDependency.value(), clazz);
               }
            }
         }

         if (Graphs.hasCycle(graph)) {
            throw new PluginInstantiationException("Plugin dependency graph contains a cycle!");
         }

         List<Class<? extends Plugin>> sortedPlugins = topologicalSort(graph);
         int loaded = 0;
         List<Plugin> newPlugins = new ArrayList();
         Iterator var17 = sortedPlugins.iterator();

         while(var17.hasNext()) {
            Class<? extends Plugin> pluginClazz = (Class)var17.next();

            try {
               Plugin plugin = this.instantiate(this.plugins, pluginClazz);
               newPlugins.add(plugin);
               this.plugins.add(plugin);
               if (isExternalPlugins) {
                  this.externalPlugins.add(plugin);
               }
            } catch (PluginInstantiationException var12) {
               log.error("Error instantiating plugin!", var12);
            }

            ++loaded;
            if (onPluginLoaded != null) {
               onPluginLoaded.accept(loaded, sortedPlugins.size());
            }
         }

         return newPlugins;
      }
   }

   public boolean startPlugin(Plugin plugin) throws PluginInstantiationException {
      assert SwingUtilities.isEventDispatchThread();

      if (!this.activePlugins.contains(plugin) && this.isPluginEnabled(plugin)) {
         List<Plugin> conflicts = this.conflictsForPlugin(plugin);
         Iterator var3 = conflicts.iterator();

         while(var3.hasNext()) {
            Plugin conflict = (Plugin)var3.next();
            if (this.isPluginEnabled(conflict)) {
               this.setPluginEnabled(conflict, false);
            }

            if (this.activePlugins.contains(conflict)) {
               this.stopPlugin(conflict);
            }
         }

         this.activePlugins.add(plugin);
         ExternalPluginMdc.ScopedMdc pluginMdc = ExternalPluginMdc.open((Object)plugin);

         try {
            plugin.startUp();
            log.debug("Plugin {} is now running", plugin.getClass().getSimpleName());
            if (this.sceneTileManager != null) {
               GameEventManager gameEventManager = (GameEventManager)this.sceneTileManager.get();
               if (gameEventManager != null) {
                  gameEventManager.simulateGameEvents((Object)plugin);
               }
            }

            this.eventBus.register(plugin);
            this.schedule(plugin);
            this.eventBus.post(new PluginChanged(plugin, true));
         } catch (ThreadDeath var12) {
            ThreadDeath e = var12;
            throw e;
         } catch (Throwable var13) {
            Throwable ex = var13;

            try {
               this.stopPlugin(plugin);
            } catch (Throwable var11) {
               Throwable ex2 = var11;
               log.error("unable to stop plugin", ex2);
            }

            throw new PluginInstantiationException(ex);
         } finally {
            pluginMdc.close();
         }

         return true;
      } else {
         return false;
      }
   }

   public boolean stopPlugin(Plugin plugin) throws PluginInstantiationException {
      assert SwingUtilities.isEventDispatchThread();

      if (!this.activePlugins.remove(plugin)) {
         return false;
      } else {
         this.unschedule(plugin);
         this.eventBus.unregister((Object)plugin);
         ExternalPluginMdc.ScopedMdc pluginMdc = ExternalPluginMdc.open((Object)plugin);

         try {
            plugin.shutDown();
            log.debug("Plugin {} is now stopped", plugin.getClass().getSimpleName());
            this.eventBus.post(new PluginChanged(plugin, false));
         } catch (Exception var7) {
            Exception ex = var7;
            throw new PluginInstantiationException(ex);
         } finally {
            pluginMdc.close();
         }

         return true;
      }
   }

   public void setPluginEnabled(Plugin plugin, boolean enabled) {
      PluginDescriptor pluginDescriptor = (PluginDescriptor)plugin.getClass().getAnnotation(PluginDescriptor.class);
      String keyName = Strings.isNullOrEmpty(pluginDescriptor.configName()) ? plugin.getClass().getSimpleName() : pluginDescriptor.configName();
      this.configManager.setConfiguration("runelite", keyName.toLowerCase(), String.valueOf(enabled));
      if (enabled) {
         List<Plugin> conflicts = this.conflictsForPlugin(plugin);
         Iterator var6 = conflicts.iterator();

         while(var6.hasNext()) {
            Plugin conflict = (Plugin)var6.next();
            if (this.isPluginEnabled(conflict)) {
               this.setPluginEnabled(conflict, false);
            }
         }
      }

   }

   public boolean isPluginEnabled(Plugin plugin) {
      PluginDescriptor pluginDescriptor = (PluginDescriptor)plugin.getClass().getAnnotation(PluginDescriptor.class);
      String keyName = Strings.isNullOrEmpty(pluginDescriptor.configName()) ? plugin.getClass().getSimpleName() : pluginDescriptor.configName();
      String value = this.configManager.getConfiguration("runelite", keyName.toLowerCase());
      return value != null ? Boolean.parseBoolean(value) : pluginDescriptor.enabledByDefault();
   }

   public boolean isPluginActive(Plugin plugin) {
      return this.activePlugins.contains(plugin);
   }

   private Plugin instantiate(List<Plugin> scannedPlugins, Class<Plugin> clazz) throws PluginInstantiationException {
      PluginDependency[] pluginDependencies = (PluginDependency[])clazz.getAnnotationsByType(PluginDependency.class);
      List<Plugin> deps = new ArrayList();
      PluginDependency[] var5 = pluginDependencies;
      int var6 = pluginDependencies.length;

      for(int var7 = 0; var7 < var6; ++var7) {
         PluginDependency pluginDependency = var5[var7];
         Optional<Plugin> dependency = scannedPlugins.stream().filter((px) -> {
            return px.getClass() == pluginDependency.value();
         }).findFirst();
         if (!dependency.isPresent()) {
            String var10002 = clazz.getSimpleName();
            throw new PluginInstantiationException("Unmet dependency for " + var10002 + ": " + pluginDependency.value().getSimpleName());
         }

         deps.add((Plugin)dependency.get());
      }

      Plugin plugin;
      try {
         plugin = (Plugin)clazz.getDeclaredConstructor().newInstance();
      } catch (ThreadDeath var11) {
         throw var11;
      } catch (Throwable var12) {
         throw new PluginInstantiationException(var12);
      }

      try {
         Injector parent = RuneLite.getInjector();
         if (deps.size() <= 1) {
            if (!deps.isEmpty()) {
               parent = ((Plugin)deps.get(0)).injector;
            }
         } else {
            List<Module> modules = new ArrayList(deps.size());
            Iterator var21 = deps.iterator();

            while(var21.hasNext()) {
               Plugin p = (Plugin)var21.next();
               Module module = (binder) -> {
                  binder.bind(p.getClass()).toInstance(p);
                  binder.install(p);
               };
               modules.add(module);
            }

            parent = parent.createChildInjector(modules);
         }

         Module pluginModule = (binder) -> {
            binder.bind(clazz).toInstance(plugin);
            binder.install(plugin);
         };
         Injector pluginInjector = parent.createChildInjector(new Module[]{pluginModule});
         plugin.injector = pluginInjector;
      } catch (CreationException var13) {
         throw new PluginInstantiationException(var13);
      }

      log.debug("Loaded plugin {}", clazz.getSimpleName());
      return plugin;
   }

   public void add(Plugin plugin) {
      this.plugins.add(plugin);
   }

   public void remove(Plugin plugin) {
      this.plugins.remove(plugin);
   }

   public Collection<Plugin> getPlugins() {
      return (Collection)this.plugins.stream().filter((plugin) -> {
         if (!this.externalPlugins.contains(plugin) && !WHITELISTED_PLUGINS.contains(plugin.getName())) {
            if (!this.developerMode) {
               return false;
            } else {
               PluginDescriptor desc = (PluginDescriptor)plugin.getClass().getAnnotation(PluginDescriptor.class);
               return desc != null && desc.developerPlugin();
            }
         } else {
            return true;
         }
      }).collect(Collectors.toList());
   }

   private void schedule(Plugin plugin) {
      Method[] var2 = plugin.getClass().getMethods();
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         Method method = var2[var4];
         Schedule schedule = (Schedule)method.getAnnotation(Schedule.class);
         if (schedule != null) {
            Runnable runnable = null;

            try {
               Class<?> clazz = method.getDeclaringClass();
               MethodHandles.Lookup caller = ReflectUtil.privateLookupIn(clazz);
               MethodType subscription = MethodType.methodType(method.getReturnType(), method.getParameterTypes());
               MethodHandle target = caller.findVirtual(clazz, method.getName(), subscription);
               CallSite site = LambdaMetafactory.metafactory(caller, "run", MethodType.methodType(Runnable.class, clazz), subscription, target, subscription);
               MethodHandle factory = site.getTarget();
               runnable = factory.bindTo(plugin).invokeExact();
            } catch (Throwable var14) {
               Throwable e = var14;
               log.warn("Unable to create lambda for method {}", method, e);
            }

            ScheduledMethod scheduledMethod = new ScheduledMethod(schedule, method, plugin, runnable);
            log.debug("Scheduled task {}", scheduledMethod);
            this.scheduler.addScheduledMethod(scheduledMethod);
         }
      }

   }

   private void unschedule(Plugin plugin) {
      List<ScheduledMethod> methods = new ArrayList(this.scheduler.getScheduledMethods());
      Iterator var3 = methods.iterator();

      while(var3.hasNext()) {
         ScheduledMethod method = (ScheduledMethod)var3.next();
         if (method.getObject() == plugin) {
            log.debug("Removing scheduled task {}", method);
            this.scheduler.removeScheduledMethod(method);
         }
      }

   }

   @VisibleForTesting
   static <T> List<T> topologicalSort(Graph<T> graph) {
      MutableGraph<T> graphCopy = Graphs.copyOf(graph);
      List<T> l = new ArrayList();
      Set<T> s = (Set)graphCopy.nodes().stream().filter((node) -> {
         return graphCopy.inDegree(node) == 0;
      }).collect(Collectors.toSet());

      while(!s.isEmpty()) {
         Iterator<T> it = s.iterator();
         T n = it.next();
         it.remove();
         l.add(n);
         Iterator var6 = (new HashSet(graphCopy.successors(n))).iterator();

         while(var6.hasNext()) {
            T m = var6.next();
            graphCopy.removeEdge(n, m);
            if (graphCopy.inDegree(m) == 0) {
               s.add(m);
            }
         }
      }

      if (!graphCopy.edges().isEmpty()) {
         throw new RuntimeException("Graph has at least one cycle");
      } else {
         return l;
      }
   }

   public List<Plugin> conflictsForPlugin(Plugin plugin) {
      PluginDescriptor desc = (PluginDescriptor)plugin.getClass().getAnnotation(PluginDescriptor.class);
      Set<String> conflicts = new HashSet(Arrays.asList(desc.conflicts()));
      conflicts.add(desc.name());
      return (List)this.plugins.stream().filter((p) -> {
         if (p == plugin) {
            return false;
         } else {
            PluginDescriptor desc = (PluginDescriptor)p.getClass().getAnnotation(PluginDescriptor.class);
            if (conflicts.contains(desc.name())) {
               return true;
            } else {
               String[] var4 = desc.conflicts();
               int var5 = var4.length;

               for(int var6 = 0; var6 < var5; ++var6) {
                  String conflict = var4[var6];
                  if (conflicts.contains(conflict)) {
                     return true;
                  }
               }

               return false;
            }
         }
      }).collect(Collectors.toList());
   }
}
