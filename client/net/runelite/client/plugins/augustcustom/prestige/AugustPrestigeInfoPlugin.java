package net.runelite.client.plugins.augustcustom.prestige;

import custom.UpdateServerAndPlayerInfoScript;
import java.util.Collections;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.events.GameStateChanged;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.game.ItemManager;
import net.runelite.client.game.NPCManager;
import net.runelite.client.game.SkillIconManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.xptracker.XpClient;
import net.runelite.client.ui.ClientToolbar;
import net.runelite.client.ui.NavigationButton;
import net.runelite.client.ui.overlay.OverlayManager;
import net.runelite.client.util.ImageUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@PluginDescriptor(
   name = "August Prestige Info",
   description = "See your prestige info",
   tags = {"prestige", "tracking"}
)
public class AugustPrestigeInfoPlugin extends Plugin {
   private static final Logger log = LoggerFactory.getLogger(AugustPrestigeInfoPlugin.class);
   @Inject
   private ClientToolbar clientToolbar;
   @Inject
   private Client client;
   @Inject
   private ClientThread clientThread;
   @Inject
   private SkillIconManager skillIconManager;
   @Inject
   private NPCManager npcManager;
   @Inject
   private ItemManager itemManager;
   @Inject
   private OverlayManager overlayManager;
   @Inject
   private XpClient xpClient;
   private NavigationButton navButton;
   private AugustPrestigeInfoPanel panel;

   protected void startUp() throws Exception {
      this.panel = new AugustPrestigeInfoPanel(this.client, this.skillIconManager);
      this.navButton = NavigationButton.builder().tooltip("Prestige Info").icon(ImageUtil.loadImageResource(this.getClass(), "/prestige_icon.png")).priority(1).panel(this.panel).build();
      this.clientToolbar.addNavigation(this.navButton);
   }

   protected void shutDown() throws Exception {
      this.clientToolbar.removeNavigation(this.navButton);
   }

   @Subscribe
   public void onUpdateServerAndPlayerInfoScript(UpdateServerAndPlayerInfoScript packet) {
      this.panel.setPrestigeInfo(packet.getSkillPrestigeInfo());
   }

   @Subscribe
   public void onGameStateChanged(GameStateChanged event) {
      if (event.getGameState() == GameState.LOGIN_SCREEN) {
         this.panel.setPrestigeInfo(Collections.emptyList());
      }

   }
}
