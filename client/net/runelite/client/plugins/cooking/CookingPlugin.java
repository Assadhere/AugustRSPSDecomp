package net.runelite.client.plugins.cooking;

import com.google.inject.Provides;
import java.time.Duration;
import java.time.Instant;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;
import javax.inject.Inject;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.Player;
import net.runelite.api.events.ChatMessage;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.GraphicChanged;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.game.ItemManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDependency;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.xptracker.XpTrackerPlugin;
import net.runelite.client.ui.overlay.OverlayManager;
import net.runelite.client.ui.overlay.infobox.InfoBoxManager;

@PluginDescriptor(
   name = "Cooking",
   description = "Show cooking statistics",
   tags = {"overlay", "skilling", "cook"}
)
@PluginDependency(XpTrackerPlugin.class)
public class CookingPlugin extends Plugin {
   @Inject
   private Client client;
   @Inject
   private CookingConfig config;
   @Inject
   private CookingOverlay overlay;
   @Inject
   private OverlayManager overlayManager;
   @Inject
   private InfoBoxManager infoBoxManager;
   @Inject
   private ItemManager itemManager;
   private CookingSession session;

   @Provides
   CookingConfig getConfig(ConfigManager configManager) {
      return (CookingConfig)configManager.getConfig(CookingConfig.class);
   }

   protected void startUp() throws Exception {
      this.session = null;
      this.overlayManager.add(this.overlay);
   }

   protected void shutDown() throws Exception {
      InfoBoxManager var10000 = this.infoBoxManager;
      Objects.requireNonNull(FermentTimer.class);
      var10000.removeIf(FermentTimer.class::isInstance);
      this.overlayManager.remove(this.overlay);
      this.session = null;
   }

   @Subscribe
   public void onGameTick(GameTick gameTick) {
      if (this.session != null && this.config.statTimeout() != 0) {
         Duration statTimeout = Duration.ofMinutes((long)this.config.statTimeout());
         Duration sinceCooked = Duration.between(this.session.getLastCookingAction(), Instant.now());
         if (sinceCooked.compareTo(statTimeout) >= 0) {
            this.session = null;
         }

      }
   }

   @Subscribe
   public void onGraphicChanged(GraphicChanged graphicChanged) {
      Player player = this.client.getLocalPlayer();
      if (graphicChanged.getActor() == player) {
         if (player.getGraphic() == 47 && this.config.fermentTimer()) {
            Stream var10000 = this.infoBoxManager.getInfoBoxes().stream();
            Objects.requireNonNull(FermentTimer.class);
            var10000 = var10000.filter(FermentTimer.class::isInstance);
            Objects.requireNonNull(FermentTimer.class);
            Optional<FermentTimer> fermentTimerOpt = var10000.map(FermentTimer.class::cast).findAny();
            FermentTimer fermentTimer;
            if (fermentTimerOpt.isPresent()) {
               fermentTimer = (FermentTimer)fermentTimerOpt.get();
               fermentTimer.reset();
            } else {
               fermentTimer = new FermentTimer(this.itemManager.getImage(1993), this);
               this.infoBoxManager.addInfoBox(fermentTimer);
            }
         }

      }
   }

   @Subscribe
   public void onChatMessage(ChatMessage event) {
      if (event.getType() == ChatMessageType.SPAM) {
         String message = event.getMessage();
         if (!message.startsWith("You successfully cook") && !message.startsWith("You successfully bake") && !message.startsWith("You successfully fry") && !message.startsWith("You manage to cook") && !message.startsWith("You roast a") && !message.startsWith("You spit-roast") && !message.startsWith("You cook") && !message.startsWith("Eventually the Jubbly") && !message.startsWith("You half-cook") && !message.startsWith("The undead meat is now cooked") && !message.startsWith("The undead chicken is now cooked") && !message.startsWith("You successfully scramble") && !message.startsWith("You dry a piece of meat")) {
            if (message.startsWith("You accidentally burn") || message.equals("You burn the mushroom in the fire.") || message.startsWith("Unfortunately the Jubbly") || message.startsWith("You accidentally spoil")) {
               if (this.session == null) {
                  this.session = new CookingSession();
               }

               this.session.updateLastCookingAction();
               this.session.increaseBurnAmount();
            }
         } else {
            if (this.session == null) {
               this.session = new CookingSession();
            }

            this.session.updateLastCookingAction();
            this.session.increaseCookAmount();
         }

      }
   }

   CookingSession getSession() {
      return this.session;
   }

   void setSession(CookingSession session) {
      this.session = session;
   }
}
