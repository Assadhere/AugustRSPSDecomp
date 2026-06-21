package net.runelite.client.plugins.augustcustom.donator;

import custom.UpdateServerAndPlayerInfoScript;
import javax.inject.Inject;
import net.runelite.api.Client;
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
   name = "August Donator Info",
   description = "See your donator info",
   tags = {"donation", "donator", "tracking"}
)
public class AugustDonatorInfoPlugin extends Plugin {
   private static final Logger log = LoggerFactory.getLogger(AugustDonatorInfoPlugin.class);
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
   private AugustDonatorInfoPanel panel;

   protected void startUp() throws Exception {
      this.panel = new AugustDonatorInfoPanel(this, this.client);
      this.navButton = NavigationButton.builder().tooltip("Donator Info").icon(ImageUtil.loadImageResource(this.getClass(), "/bond_icon.png")).priority(1).panel(this.panel).build();
      this.clientToolbar.addNavigation(this.navButton);
   }

   protected void shutDown() throws Exception {
      this.clientToolbar.removeNavigation(this.navButton);
   }

   @Subscribe
   public void onUpdateServerAndPlayerInfoScript(UpdateServerAndPlayerInfoScript packet) {
      this.panel.setInfo(packet.getDonatorRank(), packet.getDonatorPoints(), packet.getDonatorPerks());
   }
}
