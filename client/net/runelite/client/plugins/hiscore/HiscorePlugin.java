package net.runelite.client.plugins.hiscore;

import com.google.inject.Provides;
import custom.UpdateCountGraphicsScript;
import custom.UpdateHiscoreScript;
import custom.UpdateHiscoreTopListScript;
import java.awt.image.BufferedImage;
import javax.inject.Inject;
import javax.swing.SwingUtilities;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.ClientToolbar;
import net.runelite.client.ui.NavigationButton;
import net.runelite.client.util.ImageUtil;

@PluginDescriptor(
   name = "HiScore",
   description = "Enable the HiScore panel",
   tags = {"panel", "players"}
)
public class HiscorePlugin extends Plugin {
   private static final int AUGUST_RUNELITE_UI_INTERFACE = 3009;
   private static final int HISCORE_SEARCH_COMPONENT = 11;
   private static final int HISCORE_SEARCH_TRIGGER = 1;
   private static final int HISCORE_TOPLIST_COMPONENT = 13;
   private static final int HISCORE_TOPLIST_TRIGGER = 1;
   private int nextTopListRequestId = 1;
   @Inject
   private Client client;
   @Inject
   private ClientThread clientThread;
   @Inject
   private ClientToolbar clientToolbar;
   private NavigationButton navButton;
   private HiscorePanel hiscorePanel;

   @Provides
   HiscoreConfig provideConfig(ConfigManager configManager) {
      return (HiscoreConfig)configManager.getConfig(HiscoreConfig.class);
   }

   protected void startUp() throws Exception {
      this.hiscorePanel = (HiscorePanel)this.injector.getInstance(HiscorePanel.class);
      this.hiscorePanel.setSearchHandler(this::requestHiscoreLookup);
      this.hiscorePanel.setTopListHandler(this::requestHiscoreTopList);
      BufferedImage icon = ImageUtil.loadImageResource(this.getClass(), "normal.png");
      this.navButton = NavigationButton.builder().tooltip("Hiscore").icon(icon).priority(5).panel(this.hiscorePanel).build();
      this.clientToolbar.addNavigation(this.navButton);
   }

   protected void shutDown() throws Exception {
      this.hiscorePanel.shutdown();
      this.clientToolbar.removeNavigation(this.navButton);
   }

   private void requestHiscoreLookup(String username) {
      this.clientThread.invoke(() -> {
         if (this.client.getGameState() == GameState.LOGGED_IN) {
            this.client.sendIfScriptTrigger(3009, 11, 1, new Object[]{username});
         }
      });
   }

   private int requestHiscoreTopList(UpdateHiscoreTopListScript.TopListType type, String targetId, int page, int pageSize, int ironmanModeFilter, int gameModeFilter) {
      if (this.client.getGameState() != GameState.LOGGED_IN) {
         return -1;
      } else {
         int requestId = this.nextTopListRequestId++;
         this.clientThread.invoke(() -> {
            this.client.sendIfScriptTrigger(3009, 13, 1, new Object[]{requestId, type.getWireId(), targetId, page, pageSize, ironmanModeFilter, gameModeFilter});
         });
         return requestId;
      }
   }

   @Subscribe
   public void onUpdateCountGraphicsScript(UpdateCountGraphicsScript packet) {
      SwingUtilities.invokeLater(() -> {
         this.hiscorePanel.setCountGraphicMapping(packet.getGraphics());
      });
   }

   @Subscribe
   public void onUpdateHiscoreScript(UpdateHiscoreScript packet) {
      SwingUtilities.invokeLater(() -> {
         this.clientToolbar.openPanel(this.navButton);
         this.hiscorePanel.displayHiscoreData(packet);
      });
   }

   @Subscribe
   public void onUpdateHiscoreTopListScript(UpdateHiscoreTopListScript packet) {
      SwingUtilities.invokeLater(() -> {
         this.clientToolbar.openPanel(this.navButton);
         this.hiscorePanel.displayTopListData(packet);
      });
   }
}
