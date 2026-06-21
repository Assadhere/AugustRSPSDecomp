package net.runelite.client;

import com.google.common.base.Strings;
import com.google.common.math.DoubleMath;
import com.google.gson.Gson;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.binder.ConstantBindingBuilder;
import com.google.inject.name.Names;
import com.google.inject.util.Providers;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.function.Supplier;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.swing.SwingUtilities;
import net.runelite.api.Client;
import net.runelite.api.hooks.Callbacks;
import net.runelite.client.account.SessionManager;
import net.runelite.client.callback.Hooks;
import net.runelite.client.chat.ChatMessageManager;
import net.runelite.client.config.ChatColorConfig;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.config.RuneLiteConfig;
import net.runelite.client.eventbus.EventBus;
import net.runelite.client.game.ItemManager;
import net.runelite.client.menus.MenuManager;
import net.runelite.client.plugins.PluginManager;
import net.runelite.client.task.Scheduler;
import net.runelite.client.util.DeferredEventBus;
import net.runelite.client.util.ExecutorServiceExceptionLogger;
import net.runelite.http.api.RuneLiteAPI;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;

public class RuneLiteModule extends AbstractModule {
   private final OkHttpClient bootupHttpClient;
   private final Supplier<Client> clientLoader;
   private final RuntimeConfigLoader configLoader;
   private final boolean developerMode;
   private final boolean safeMode;
   private final boolean disableTelemetry;
   private final File sessionfile;
   private final String profile;
   private final boolean insecureWriteCredentials;
   private final boolean noupdate;

   protected void configure() {
      Properties properties = RuneLiteProperties.getProperties();
      Map<Object, Object> props = new HashMap(properties);
      RuntimeConfig runtimeConfig = this.configLoader.get();
      if (runtimeConfig != null && runtimeConfig.getProps() != null) {
         props.putAll(runtimeConfig.getProps());
      }

      Iterator var4 = props.entrySet().iterator();

      while(var4.hasNext()) {
         Map.Entry<?, ?> entry = (Map.Entry)var4.next();
         String key = (String)entry.getKey();
         ConstantBindingBuilder binder;
         if (entry.getValue() instanceof String) {
            binder = this.bindConstant().annotatedWith(Names.named(key));
            binder.to((String)entry.getValue());
         } else if (entry.getValue() instanceof Double) {
            binder = this.bindConstant().annotatedWith(Names.named(key));
            if (DoubleMath.isMathematicalInteger((Double)entry.getValue())) {
               binder.to((int)(Double)entry.getValue());
            } else {
               binder.to((Double)entry.getValue());
            }
         } else if (entry.getValue() instanceof Boolean) {
            binder = this.bindConstant().annotatedWith(Names.named(key));
            binder.to((Boolean)entry.getValue());
         }
      }

      this.bindConstant().annotatedWith(Names.named("developerMode")).to(this.developerMode);
      this.bindConstant().annotatedWith(Names.named("safeMode")).to(this.safeMode);
      this.bindConstant().annotatedWith(Names.named("disableTelemetry")).to(this.disableTelemetry);
      this.bind(File.class).annotatedWith(Names.named("sessionfile")).toInstance(this.sessionfile);
      this.bind(String.class).annotatedWith(Names.named("profile")).toProvider(Providers.of(this.profile));
      this.bindConstant().annotatedWith(Names.named("insecureWriteCredentials")).to(this.insecureWriteCredentials);
      this.bindConstant().annotatedWith(Names.named("noupdate")).to(this.noupdate);
      this.bind(File.class).annotatedWith(Names.named("runeLiteDir")).toInstance(RuneLite.RUNELITE_DIR);
      this.bind(ScheduledExecutorService.class).toInstance(new ExecutorServiceExceptionLogger(Executors.newSingleThreadScheduledExecutor()));
      this.bind(RuntimeConfigLoader.class).toInstance(this.configLoader);
      this.bind(RuntimeConfigRefresher.class).asEagerSingleton();
      this.bind(MenuManager.class);
      this.bind(ChatMessageManager.class);
      this.bind(ItemManager.class);
      this.bind(Scheduler.class);
      this.bind(PluginManager.class);
      this.bind(SessionManager.class);
      this.bind(Gson.class).toInstance(RuneLiteAPI.GSON);
      this.bind(Callbacks.class).to(Hooks.class);
      this.bind(EventBus.class).toInstance(new EventBus());
      this.bind(EventBus.class).annotatedWith(Names.named("Deferred EventBus")).to(DeferredEventBus.class);
   }

   @Provides
   @Singleton
   Client provideClient() {
      return (Client)this.clientLoader.get();
   }

   @Provides
   @Singleton
   RuntimeConfig provideRuntimeConfig() {
      return this.configLoader.get();
   }

   @Provides
   @Singleton
   RuneLiteConfig provideConfig(ConfigManager configManager) {
      return (RuneLiteConfig)configManager.getConfig(RuneLiteConfig.class);
   }

   @Provides
   @Singleton
   ChatColorConfig provideChatColorConfig(ConfigManager configManager) {
      return (ChatColorConfig)configManager.getConfig(ChatColorConfig.class);
   }

   @Provides
   @Singleton
   OkHttpClient provideHttpClient(Client client) {
      return this.bootupHttpClient.newBuilder().addInterceptor((chain) -> {
         if (client.isClientThread()) {
            throw new IOException("Blocking network calls are not allowed on the client thread");
         } else if (SwingUtilities.isEventDispatchThread()) {
            throw new IOException("Blocking network calls are not allowed on the event dispatch thread");
         } else {
            if (client.getEnvironment() != 0) {
               HttpUrl url = chain.request().url();
               String[] var3 = RuneLiteProperties.getJagexBlockedDomains();
               int var4 = var3.length;

               for(int var5 = 0; var5 < var4; ++var5) {
                  String domain = var3[var5];
                  if (url.host().endsWith(domain)) {
                     throw new IOException("Network call to " + String.valueOf(url) + " blocked outside of LIVE environment");
                  }
               }
            }

            return chain.proceed(chain.request());
         }
      }).build();
   }

   @Provides
   @Named("runelite.api.base")
   HttpUrl provideApiBase(@Named("runelite.api.base") String s) {
      String prop = System.getProperty("runelite.http-service.url");
      return HttpUrl.get(Strings.isNullOrEmpty(prop) ? s : prop);
   }

   @Provides
   @Named("runelite.session")
   HttpUrl provideSession(@Named("runelite.session") String s) {
      String prop = System.getProperty("runelite.session.url");
      return HttpUrl.get(Strings.isNullOrEmpty(prop) ? s : prop);
   }

   @Provides
   @Named("runelite.static.base")
   HttpUrl provideStaticBase(@Named("runelite.static.base") String s) {
      String prop = System.getProperty("runelite.static.url");
      return HttpUrl.get(Strings.isNullOrEmpty(prop) ? s : prop);
   }

   @Provides
   @Named("runelite.ws")
   HttpUrl provideWs(@Named("runelite.ws") String s) {
      String prop = System.getProperty("runelite.ws.url");
      return HttpUrl.get(Strings.isNullOrEmpty(prop) ? s : prop);
   }

   @Provides
   @Named("runelite.pluginhub.url")
   HttpUrl providePluginHubBase(@Named("runelite.pluginhub.url") String s) {
      return HttpUrl.get(System.getProperty("runelite.pluginhub.url", s));
   }

   @Provides
   @Singleton
   TelemetryClient provideTelemetry(OkHttpClient okHttpClient, Gson gson, @Named("runelite.api.base") HttpUrl apiBase) {
      return this.disableTelemetry ? null : new TelemetryClient(okHttpClient, gson, apiBase);
   }

   public RuneLiteModule(OkHttpClient bootupHttpClient, Supplier<Client> clientLoader, RuntimeConfigLoader configLoader, boolean developerMode, boolean safeMode, boolean disableTelemetry, File sessionfile, String profile, boolean insecureWriteCredentials, boolean noupdate) {
      this.bootupHttpClient = bootupHttpClient;
      this.clientLoader = clientLoader;
      this.configLoader = configLoader;
      this.developerMode = developerMode;
      this.safeMode = safeMode;
      this.disableTelemetry = disableTelemetry;
      this.sessionfile = sessionfile;
      this.profile = profile;
      this.insecureWriteCredentials = insecureWriteCredentials;
      this.noupdate = noupdate;
   }
}
