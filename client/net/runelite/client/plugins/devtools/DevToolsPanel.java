package net.runelite.client.plugins.devtools;

import com.formdev.flatlaf.extras.FlatUIDefaultsInspector;
import com.google.inject.ProvisionException;
import java.awt.GridLayout;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;
import javax.swing.JButton;
import javax.swing.JPanel;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.MenuAction;
import net.runelite.client.Notifier;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.PluginPanel;
import net.runelite.client.ui.overlay.OverlayMenuEntry;
import net.runelite.client.ui.overlay.infobox.Counter;
import net.runelite.client.ui.overlay.infobox.InfoBoxManager;
import net.runelite.client.util.ImageUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class DevToolsPanel extends PluginPanel {
   private static final Logger log = LoggerFactory.getLogger(DevToolsPanel.class);
   private final Client client;
   private final ClientThread clientThread;
   private final Notifier notifier;
   private final DevToolsPlugin plugin;
   private final DevToolsConfig config;
   private final ConfigManager configManager;
   private final WidgetInspector widgetInspector;
   private final VarInspector varInspector;
   private final ScriptInspector scriptInspector;
   private final InventoryInspector inventoryInspector;
   private final InfoBoxManager infoBoxManager;
   private final ScheduledExecutorService scheduledExecutorService;

   @Inject
   private DevToolsPanel(Client client, ClientThread clientThread, DevToolsPlugin plugin, DevToolsConfig config, ConfigManager configManager, WidgetInspector widgetInspector, VarInspector varInspector, ScriptInspector scriptInspector, InventoryInspector inventoryInspector, Notifier notifier, InfoBoxManager infoBoxManager, ScheduledExecutorService scheduledExecutorService) {
      this.client = client;
      this.clientThread = clientThread;
      this.plugin = plugin;
      this.config = config;
      this.widgetInspector = widgetInspector;
      this.varInspector = varInspector;
      this.inventoryInspector = inventoryInspector;
      this.scriptInspector = scriptInspector;
      this.notifier = notifier;
      this.infoBoxManager = infoBoxManager;
      this.scheduledExecutorService = scheduledExecutorService;
      this.configManager = configManager;
      this.setBackground(ColorScheme.DARK_GRAY_COLOR);
      HotReloadPresetDialog.applySelectedPreset(configManager, clientThread, client);
      DevServerDialog.applySessionSettings(clientThread, client);
      this.add(this.createOptionsPanel());
   }

   private JPanel createOptionsPanel() {
      JPanel container = new JPanel();
      container.setBackground(ColorScheme.DARK_GRAY_COLOR);
      container.setLayout(new GridLayout(0, 2, 3, 3));
      container.add(this.plugin.getPlayers());
      container.add(this.plugin.getNpcs());
      container.add(this.plugin.getGroundItems());
      container.add(this.plugin.getGroundObjects());
      container.add(this.plugin.getGameObjects());
      container.add(this.plugin.getGraphicsObjects());
      container.add(this.plugin.getWalls());
      container.add(this.plugin.getDecorations());
      container.add(this.plugin.getProjectiles());
      container.add(this.plugin.getLocation());
      container.add(this.plugin.getWorldMapLocation());
      container.add(this.plugin.getTileLocation());
      container.add(this.plugin.getCameraPosition());
      container.add(this.plugin.getZoneBorders());
      container.add(this.plugin.getMapSquares());
      container.add(this.plugin.getLoadingLines());
      container.add(this.plugin.getLineOfSight());
      container.add(this.plugin.getValidMovement());
      container.add(this.plugin.getMovementFlags());
      container.add(this.plugin.getInteracting());
      container.add(this.plugin.getExamine());
      container.add(this.plugin.getDetachedCamera());
      this.plugin.getDetachedCamera().addActionListener((ev) -> {
         this.client.setOculusOrbState(!this.plugin.getDetachedCamera().isActive() ? 1 : 0);
         this.client.setOculusOrbNormalSpeed(!this.plugin.getDetachedCamera().isActive() ? 36 : 12);
      });
      container.add(this.plugin.getWidgetInspector());
      this.plugin.getWidgetInspector().addFrame(this.widgetInspector);
      container.add(this.plugin.getVarInspector());
      this.plugin.getVarInspector().addFrame(this.varInspector);
      container.add(this.plugin.getSoundEffects());
      JButton notificationBtn = new JButton("Notification");
      notificationBtn.addActionListener((ex) -> {
         this.scheduledExecutorService.schedule(() -> {
            this.notifier.notify(this.config.notification(), "Wow!");
         }, 3L, TimeUnit.SECONDS);
      });
      container.add(notificationBtn);
      container.add(this.plugin.getScriptInspector());
      this.plugin.getScriptInspector().addFrame(this.scriptInspector);
      JButton newInfoboxBtn = new JButton("Infobox");
      newInfoboxBtn.addActionListener((ex) -> {
         Counter counter = new Counter(ImageUtil.loadImageResource(this.getClass(), "devtools_icon.png"), this.plugin, 42) {
            public String getName() {
               return "devtools-" + this.hashCode();
            }
         };
         counter.getMenuEntries().add(new OverlayMenuEntry(MenuAction.RUNELITE_INFOBOX, "Test", "DevTools"));
         this.infoBoxManager.addInfoBox(counter);
      });
      container.add(newInfoboxBtn);
      JButton clearInfoboxBtn = new JButton("Clear Infobox");
      clearInfoboxBtn.addActionListener((ex) -> {
         this.infoBoxManager.removeIf((i) -> {
            return true;
         });
      });
      container.add(clearInfoboxBtn);
      container.add(this.plugin.getInventoryInspector());
      this.plugin.getInventoryInspector().addFrame(this.inventoryInspector);
      JButton disconnectBtn = new JButton("Disconnect");
      disconnectBtn.addActionListener((ex) -> {
         this.clientThread.invoke(() -> {
            this.client.setGameState(GameState.CONNECTION_LOST);
         });
      });
      container.add(disconnectBtn);
      container.add(this.plugin.getTileFlags());

      try {
         ShellFrame sf = (ShellFrame)this.plugin.getInjector().getInstance(ShellFrame.class);
         container.add(this.plugin.getShell());
         this.plugin.getShell().addFrame(sf);
      } catch (ProvisionException | LinkageError var10) {
         Throwable e = var10;
         log.debug("Shell is not supported", e);
      } catch (Exception var11) {
         Exception e = var11;
         log.info("Shell couldn't be loaded", e);
      }

      container.add(this.plugin.getMenus());

      try {
         FlatUIDefaultsInspector.class.getName();
         DevToolsButton uiDefaultsBtn = this.plugin.getUiDefaultsInspector();
         uiDefaultsBtn.addFrame(new DevToolsFrame() {
            {
               this.getContentPane().add(FlatUIDefaultsInspector.createInspectorPanel(), "Center");
               this.pack();
            }
         });
         container.add(uiDefaultsBtn);
      } catch (LinkageError var9) {
      }

      container.add(this.plugin.getWorldEntities());
      JButton hotReloadDirsBtn = new JButton("Hot Reload Dirs");
      hotReloadDirsBtn.addActionListener((ev) -> {
         HotReloadPresetDialog.open(this, this.configManager, this.clientThread, this.client);
      });
      container.add(hotReloadDirsBtn);
      JButton devServerBtn = new JButton("Dev Server");
      devServerBtn.addActionListener((ev) -> {
         DevServerDialog.open(this, this.clientThread, this.client);
      });
      container.add(devServerBtn);
      JButton clearCacheBtn = new JButton("Clear Interface/Script Cache");
      clearCacheBtn.addActionListener((ev) -> {
         this.client.clearDevelopmentScriptCache();
         this.client.clearDevelopmentComponentCache();
         System.out.println("Cleared development script+interface caches");
      });
      container.add(clearCacheBtn);
      return container;
   }
}
