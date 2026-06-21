package net.runelite.client.rs;

import com.google.common.base.Strings;
import java.io.IOException;
import java.util.Map;
import java.util.function.Supplier;
import javax.annotation.Nonnull;
import javax.swing.SwingUtilities;
import net.runelite.api.Client;
import net.runelite.client.RuneLiteProperties;
import net.runelite.client.RuntimeConfig;
import net.runelite.client.RuntimeConfigLoader;
import net.runelite.client.ui.FatalErrorDialog;
import net.runelite.client.ui.SplashScreen;
import net.runelite.http.api.worlds.World;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClientLoader implements Supplier<Client> {
   private static final Logger log = LoggerFactory.getLogger(ClientLoader.class);
   private static final int NUM_ATTEMPTS = 6;
   private final ClientConfigLoader clientConfigLoader;
   private final WorldSupplier worldSupplier;
   private final RuntimeConfigLoader runtimeConfigLoader;
   private final String javConfigUrl;
   private Object client;

   public ClientLoader(OkHttpClient okHttpClient, RuntimeConfigLoader runtimeConfigLoader, String javConfigUrl) {
      this.clientConfigLoader = new ClientConfigLoader(okHttpClient);
      this.worldSupplier = new WorldSupplier();
      this.runtimeConfigLoader = runtimeConfigLoader;
      this.javConfigUrl = javConfigUrl;
   }

   public synchronized Client get() {
      if (this.client == null) {
         this.client = this.doLoad();
      }

      if (this.client instanceof Throwable) {
         throw new RuntimeException((Throwable)this.client);
      } else {
         return (Client)this.client;
      }
   }

   private Object doLoad() {
      try {
         SplashScreen.stage(0.0, (String)null, "Fetching client config");
         RSConfig config = this.downloadConfig();
         SplashScreen.stage(0.3, "Starting", "Starting Old School RuneScape");
         Client rs = this.loadClient(config);
         SplashScreen.stage(0.4, (String)null, "Starting core classes");
         return rs;
      } catch (OutageException var3) {
         OutageException e = var3;
         return e;
      } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | SecurityException | IOException var4) {
         Exception e = var4;
         log.error("Error loading RS!", e);
         if (!this.checkOutages()) {
            SwingUtilities.invokeLater(() -> {
               FatalErrorDialog.showNetErrorWindow("loading the client", e);
            });
         }

         return e;
      }
   }

   private RSConfig downloadConfig() throws IOException {
      HttpUrl url = HttpUrl.get(this.javConfigUrl);
      IOException err = null;
      int attempt = 0;

      while(attempt < 6) {
         try {
            RSConfig config = this.clientConfigLoader.fetch(url);
            if (!Strings.isNullOrEmpty(config.getCodeBase()) && !Strings.isNullOrEmpty(config.getInitialJar()) && !Strings.isNullOrEmpty(config.getInitialClass())) {
               return config;
            }

            throw new IOException("Invalid or missing jav_config");
         } catch (IOException var7) {
            IOException e = var7;
            log.info("Failed to get jav_config from host \"{}\" ({})", url.host(), e.getMessage());
            if (this.checkOutages()) {
               throw new OutageException(e);
            }

            if (!this.javConfigUrl.equals(RuneLiteProperties.getJavConfig())) {
               throw e;
            }

            String host = this.worldSupplier.get().getAddress();
            url = url.newBuilder().host(host).build();
            err = e;
            ++attempt;
         }
      }

      log.info("Falling back to backup client config");

      try {
         return this.downloadFallbackConfig();
      } catch (IOException var6) {
         log.debug("error downloading backup config", var6);
         throw err;
      }
   }

   @Nonnull
   private RSConfig downloadFallbackConfig() throws IOException {
      RSConfig backupConfig = this.clientConfigLoader.fetch(HttpUrl.get(RuneLiteProperties.getJavConfigBackup()));
      if (!Strings.isNullOrEmpty(backupConfig.getCodeBase()) && !Strings.isNullOrEmpty(backupConfig.getInitialJar()) && !Strings.isNullOrEmpty(backupConfig.getInitialClass())) {
         if (Strings.isNullOrEmpty(backupConfig.getRuneLiteWorldParam())) {
            throw new IOException("Backup config does not have RuneLite gamepack url");
         } else {
            World world = this.worldSupplier.get();
            backupConfig.setCodebase("http://" + world.getAddress() + "/");
            Map<String, String> appletProperties = backupConfig.getAppletProperties();
            appletProperties.put(backupConfig.getRuneLiteWorldParam(), Integer.toString(world.getId()));
            return backupConfig;
         }
      } else {
         throw new IOException("Invalid or missing jav_config");
      }
   }

   private Client loadClient(RSConfig config) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
      Class<?> clientClass = osrs.Client.class;
      Client rs = (Client)clientClass.newInstance();
      rs.setConfiguration(new RSAppletStub(config, this.runtimeConfigLoader));
      log.info("injected-client {}", rs.getBuildID());
      return rs;
   }

   private boolean checkOutages() {
      RuntimeConfig rtc = this.runtimeConfigLoader.tryGet();
      return rtc != null ? rtc.showOutageMessage() : false;
   }

   private static class OutageException extends RuntimeException {
      private OutageException(Throwable cause) {
         super(cause);
      }
   }
}
