package net.runelite.client;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import javax.annotation.Nullable;

public class RuneLiteProperties {
   private static final String RUNELITE_VERSION = "runelite.version";
   private static final String RUNELITE_COMMIT = "runelite.commit";
   private static final String RUNELITE_DIRTY = "runelite.dirty";
   private static final String DISCORD_INVITE = "runelite.discord.invite";
   private static final String LAUNCHER_VERSION_PROPERTY = "runelite.launcher.version";
   private static final String INSECURE_SKIP_TLS_VERIFICATION_PROPERTY = "runelite.insecure-skip-tls-verification";
   private static final String TROUBLESHOOTING_LINK = "runelite.wiki.troubleshooting.link";
   private static final String BUILDING_LINK = "runelite.wiki.building.link";
   private static final String DNS_CHANGE_LINK = "runelite.dnschange.link";
   private static final String JAV_CONFIG = "runelite.jav_config";
   private static final String JAV_CONFIG_BACKUP = "runelite.jav_config_backup";
   private static final String PLUGINHUB_VERSION = "runelite.pluginhub.version";
   private static final String API_BASE = "runelite.api.base";
   private static final String RUNELITE_CONFIG = "runelite.config";
   private static final String OSRS_TWITTER_LINK = "runelite.osrstwitter.link";
   private static final String JAGEX_DOMAINBLOCK = "runelite.jagex.domainblock";
   private static final String CHANNEL_PROPERTY = "runelite.channel";
   private static final Properties properties = new Properties();

   public static String getVersion() {
      return properties.getProperty("runelite.version");
   }

   public static String getCommit() {
      return properties.getProperty("runelite.commit");
   }

   public static boolean isDirty() {
      return Boolean.parseBoolean(properties.getProperty("runelite.dirty"));
   }

   public static String getDiscordInvite() {
      return properties.getProperty("runelite.discord.invite");
   }

   @Nullable
   public static String getLauncherVersion() {
      return System.getProperty("runelite.launcher.version");
   }

   public static boolean isInsecureSkipTlsVerification() {
      return Boolean.getBoolean("runelite.insecure-skip-tls-verification");
   }

   public static String getTroubleshootingLink() {
      return properties.getProperty("runelite.wiki.troubleshooting.link");
   }

   public static String getBuildingLink() {
      return properties.getProperty("runelite.wiki.building.link");
   }

   public static String getDNSChangeLink() {
      return properties.getProperty("runelite.dnschange.link");
   }

   public static String getJavConfig() {
      return properties.getProperty("runelite.jav_config");
   }

   public static String getJavConfigBackup() {
      return properties.getProperty("runelite.jav_config_backup");
   }

   public static String getPluginHubVersion() {
      return System.getProperty("runelite.pluginhub.version", properties.getProperty("runelite.pluginhub.version"));
   }

   public static String getApiBase() {
      return properties.getProperty("runelite.api.base");
   }

   public static String getRuneLiteConfig() {
      return properties.getProperty("runelite.config");
   }

   public static String getOSRSTwitterLink() {
      return properties.getProperty("runelite.osrstwitter.link");
   }

   public static String[] getJagexBlockedDomains() {
      return properties.getProperty("runelite.jagex.domainblock").split(",");
   }

   @Nullable
   public static String getChannel() {
      return System.getProperty("runelite.channel");
   }

   public static boolean isBeta() {
      return "beta".equalsIgnoreCase(getChannel());
   }

   static Properties getProperties() {
      return properties;
   }

   static {
      try {
         InputStream in = RuneLiteProperties.class.getResourceAsStream("runelite.properties");

         try {
            properties.load(in);
         } catch (Throwable var4) {
            if (in != null) {
               try {
                  in.close();
               } catch (Throwable var3) {
                  var4.addSuppressed(var3);
               }
            }

            throw var4;
         }

         if (in != null) {
            in.close();
         }

      } catch (IOException var5) {
         IOException ex = var5;
         throw new RuntimeException(ex);
      }
   }
}
