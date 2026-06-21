package net.runelite.launcher;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LauncherProperties {
   private static final Logger log = LoggerFactory.getLogger(LauncherProperties.class);
   private static final String LAUNCHER_VERSION = "runelite.launcher.version";
   private static final String DISCORD_INVITE = "runelite.discord.invite";
   private static final String DNS_CHANGE_LINK = "runelite.dnschange.link";
   private static final String DOWNLOAD_LINK = "runelite.download.link";
   private static final String BOOTSTRAP = "runelite.bootstrap";
   private static final String MAIN = "runelite.main";
   private static final Properties properties = new Properties();

   public static String getVersionKey() {
      return "runelite.launcher.version";
   }

   public static String getVersion() {
      return properties.getProperty("runelite.launcher.version");
   }

   public static String getDiscordInvite() {
      return properties.getProperty("runelite.discord.invite");
   }

   public static String getDNSChangeLink() {
      return properties.getProperty("runelite.dnschange.link");
   }

   public static String getDownloadLink() {
      return properties.getProperty("runelite.download.link");
   }

   public static String getBootstrap() {
      return properties.getProperty("runelite.bootstrap");
   }

   public static String getMain() {
      return properties.getProperty("runelite.main");
   }

   static {
      InputStream in = LauncherProperties.class.getResourceAsStream("launcher.properties");

      try {
         properties.load(in);
      } catch (IOException var2) {
         IOException ex = var2;
         log.warn("Unable to load properties", ex);
      }

   }
}
