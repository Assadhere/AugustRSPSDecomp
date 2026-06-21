package net.runelite.client.plugins.statusbars;

import com.google.inject.Provides;
import javax.inject.Inject;
import net.runelite.api.Actor;
import net.runelite.api.Client;
import net.runelite.api.NPC;
import net.runelite.api.Player;
import net.runelite.api.events.GameTick;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDependency;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.itemstats.ItemStatPlugin;
import net.runelite.client.ui.overlay.OverlayManager;
import org.apache.commons.lang3.ArrayUtils;

@PluginDescriptor(
   name = "Status Bars",
   description = "Draws status bars next to players inventory showing current HP & Prayer and healing amounts",
   enabledByDefault = false
)
@PluginDependency(ItemStatPlugin.class)
public class StatusBarsPlugin extends Plugin {
   @Inject
   private StatusBarsOverlay overlay;
   @Inject
   private OverlayManager overlayManager;
   @Inject
   private Client client;
   @Inject
   private StatusBarsConfig config;
   @Inject
   private ClientThread clientThread;
   private boolean barsDisplayed;
   private int lastCombatActionTickCount;

   protected void startUp() throws Exception {
      this.clientThread.invokeLater(this::checkStatusBars);
      this.overlayManager.add(this.overlay);
   }

   protected void shutDown() throws Exception {
      this.overlayManager.remove(this.overlay);
      this.barsDisplayed = false;
   }

   @Provides
   StatusBarsConfig provideConfig(ConfigManager configManager) {
      return (StatusBarsConfig)configManager.getConfig(StatusBarsConfig.class);
   }

   @Subscribe
   public void onGameTick(GameTick gameTick) {
      this.checkStatusBars();
   }

   @Subscribe
   public void onConfigChanged(ConfigChanged event) {
      if ("statusbars".equals(event.getGroup()) && event.getKey().equals("hideAfterCombatDelay")) {
         this.clientThread.invokeLater(this::checkStatusBars);
      }

   }

   private void checkStatusBars() {
      Player localPlayer = this.client.getLocalPlayer();
      if (localPlayer != null) {
         Actor interacting = localPlayer.getInteracting();
         if (this.config.hideAfterCombatDelay() == 0) {
            this.barsDisplayed = true;
         } else if (interacting instanceof NPC && ArrayUtils.contains(((NPC)interacting).getComposition().getActions(), "Attack") || interacting instanceof Player && this.client.getVarbitValue(8121) == 1) {
            this.lastCombatActionTickCount = this.client.getTickCount();
            this.barsDisplayed = true;
         } else if (this.client.getTickCount() - this.lastCombatActionTickCount >= this.config.hideAfterCombatDelay()) {
            this.barsDisplayed = false;
         }

      }
   }

   boolean isBarsDisplayed() {
      return this.barsDisplayed;
   }
}
