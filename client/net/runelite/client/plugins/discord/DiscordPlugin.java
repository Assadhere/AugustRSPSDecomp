package net.runelite.client.plugins.discord;

import com.google.common.base.Strings;
import com.google.inject.Inject;
import com.google.inject.Provides;
import custom.UpdateDiscordPresenceScript;
import java.awt.image.BufferedImage;
import java.time.Instant;
import javax.inject.Named;
import net.runelite.api.Client;
import net.runelite.api.Player;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.GameTick;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.discord.DiscordPresence;
import net.runelite.client.discord.DiscordService;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.ClientToolbar;
import net.runelite.client.ui.NavigationButton;
import net.runelite.client.util.ImageUtil;
import net.runelite.client.util.LinkBrowser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@PluginDescriptor(
   name = "Discord",
   description = "Show your world and current activity in Discord Rich Presence",
   tags = {"action", "activity", "external", "integration", "presence", "status", "world"}
)
public class DiscordPlugin extends Plugin {
   private static final Logger log = LoggerFactory.getLogger(DiscordPlugin.class);
   private static final String DEFAULT_ACTIVITY = "Exploring Gielinor";
   private static final String DEFAULT_IN_GAME_DETAILS = "In game";
   private static final String LOGIN_SCREEN_DETAILS = "At the login screen";
   private static final String SEP = "\u0001";
   @Inject
   private Client client;
   @Inject
   private DiscordConfig config;
   @Inject
   private ClientToolbar clientToolbar;
   @Inject
   private DiscordService discordService;
   @Inject
   @Named("runelite.discord.invite")
   private String discordInvite;
   private NavigationButton discordButton;
   private volatile boolean loginFlag;
   private volatile Instant loginTime;
   private volatile Instant menuTime;
   private volatile UpdateDiscordPresenceScript serverContext;
   private volatile String activityKey;
   private volatile Instant activityStart;
   private volatile String lastSignature;

   @Provides
   private DiscordConfig provideConfig(ConfigManager configManager) {
      return (DiscordConfig)configManager.getConfig(DiscordConfig.class);
   }

   protected void startUp() throws Exception {
      BufferedImage icon = ImageUtil.loadImageResource(this.getClass(), "discord.png");
      this.discordButton = NavigationButton.builder().tooltip("Join Discord").icon(icon).onClick(() -> {
         LinkBrowser.browse(this.discordInvite);
      }).build();
      this.clientToolbar.addNavigation(this.discordButton);
      this.loginFlag = false;
      this.lastSignature = null;
      this.menuTime = Instant.now();
      if (this.isInGame()) {
         this.loginTime = Instant.now();
      }

      this.updatePresence();
   }

   protected void shutDown() throws Exception {
      this.clientToolbar.removeNavigation(this.discordButton);
      this.loginFlag = false;
      this.serverContext = null;
      this.loginTime = null;
      this.activityKey = null;
      this.activityStart = null;
      this.lastSignature = null;
      this.discordService.clearPresence();
   }

   @Subscribe
   public void onGameStateChanged(GameStateChanged event) {
      switch (event.getGameState()) {
         case LOGIN_SCREEN:
            this.serverContext = null;
            this.loginTime = null;
            this.activityKey = null;
            this.activityStart = null;
            this.menuTime = Instant.now();
            this.lastSignature = null;
            this.updatePresence();
            return;
         case LOGGING_IN:
            this.loginFlag = true;
            break;
         case LOGGED_IN:
            boolean freshLogin = this.loginFlag;
            this.loginFlag = false;
            if (freshLogin || this.loginTime == null) {
               this.loginTime = Instant.now();
               this.activityKey = null;
               this.activityStart = null;
            }

            this.updatePresence();
      }

   }

   @Subscribe
   public void onGameTick(GameTick event) {
      this.updatePresence();
   }

   @Subscribe
   public void onUpdateDiscordPresenceScript(UpdateDiscordPresenceScript event) {
      this.serverContext = event;
      this.updatePresence();
   }

   @Subscribe
   public void onConfigChanged(ConfigChanged event) {
      if (event.getGroup().equalsIgnoreCase("discord")) {
         this.updatePresence();
      }

   }

   private boolean isInGame() {
      switch (this.client.getGameState()) {
         case LOGGED_IN:
         case LOADING:
         case HOPPING:
         case CONNECTION_LOST:
            return true;
         default:
            return false;
      }
   }

   private void updatePresence() {
      Instant login = this.loginTime;
      Instant menu = this.menuTime;
      UpdateDiscordPresenceScript ctx = this.serverContext;
      if (login != null && this.isInGame()) {
         String serverActivity = ctx != null ? ctx.getActivityName() : null;
         int wilderness = this.wildernessLevel();
         String details = composeDetails(serverActivity, wilderness);
         String key = composeActivityKey(serverActivity, wilderness);
         if (!key.equals(this.activityKey)) {
            this.activityKey = key;
            this.activityStart = Instant.now();
         }

         Instant activity = this.activityStart;
         boolean showActivity = this.config.showActivity();
         int worldId = ctx != null ? ctx.getWorldId() : this.client.getWorld();
         boolean league = ctx != null && ctx.isLeagueWorld();
         String state = composeState(worldId, league);
         DiscordConfig.ElapsedTimeType mode = this.config.elapsedTimeType();
         boolean showParty = this.config.showParty() && ctx != null && ctx.getPartySize() > 0;
         String var10000 = showActivity ? details + "\u0001" + state : "In game";
         String signature = var10000 + "\u0001" + key + "\u0001" + String.valueOf(mode) + "\u0001" + (showParty ? ctx.getPartyId() + "\u0001" + ctx.getPartySize() + "\u0001" + ctx.getPartyMax() : "");
         if (!signature.equals(this.lastSignature)) {
            this.lastSignature = signature;
            DiscordPresence.DiscordPresenceBuilder builder = DiscordPresence.builder().startTimestamp(this.startTimestamp(login, activity)).largeImageText("August");
            if (showActivity) {
               builder.details(details).state(state);
            } else {
               builder.details("In game");
            }

            if (showParty) {
               builder.partyId(Strings.isNullOrEmpty(ctx.getPartyId()) ? "august-party" : ctx.getPartyId()).partySize(ctx.getPartySize()).partyMax(Math.max(ctx.getPartyMax(), ctx.getPartySize()));
            }

            this.discordService.updatePresence(builder.build());
         }
      } else {
         this.pushLoginScreen(menu);
      }
   }

   private void pushLoginScreen(Instant menu) {
      if (!this.config.showMainMenu()) {
         if (!"<cleared>".equals(this.lastSignature)) {
            this.lastSignature = "<cleared>";
            this.discordService.clearPresence();
         }

      } else {
         String signature = "<login>\u0001" + String.valueOf(this.config.elapsedTimeType());
         if (!signature.equals(this.lastSignature)) {
            this.lastSignature = signature;
            this.discordService.updatePresence(DiscordPresence.builder().details("At the login screen").startTimestamp(this.startTimestamp(menu, menu)).build());
         }
      }
   }

   private int wildernessLevel() {
      Player local = this.client.getLocalPlayer();
      if (local == null) {
         return 0;
      } else {
         WorldPoint wp = local.getWorldLocation();
         if (wp == null) {
            return 0;
         } else {
            int x = wp.getX();
            int y = wp.getY() > 6400 ? wp.getY() - 6400 : wp.getY();
            return x >= 2944 && x <= 3392 && y >= 3520 && y <= 3967 ? (y - 3520) / 8 + 1 : 0;
         }
      }
   }

   static String composeDetails(String serverActivity, int wildernessLevel) {
      if (!Strings.isNullOrEmpty(serverActivity)) {
         return serverActivity;
      } else {
         return wildernessLevel > 0 ? "In the Wilderness (Level " + wildernessLevel + ")" : "Exploring Gielinor";
      }
   }

   static String composeActivityKey(String serverActivity, int wildernessLevel) {
      if (!Strings.isNullOrEmpty(serverActivity)) {
         return "instance:" + serverActivity;
      } else {
         return wildernessLevel > 0 ? "wilderness" : "overworld";
      }
   }

   static String composeState(int worldId, boolean leagueWorld) {
      return (leagueWorld ? "League World " : "World ") + worldId;
   }

   private Instant startTimestamp(Instant sessionStart, Instant activityStart) {
      switch (this.config.elapsedTimeType()) {
         case HIDDEN:
            return null;
         case ACTIVITY:
            return activityStart != null ? activityStart : sessionStart;
         case TOTAL:
         default:
            return sessionStart;
      }
   }
}
