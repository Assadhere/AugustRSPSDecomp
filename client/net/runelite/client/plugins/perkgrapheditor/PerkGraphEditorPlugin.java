package net.runelite.client.plugins.perkgrapheditor;

import com.google.inject.Provides;
import java.awt.image.BufferedImage;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.ClientToolbar;
import net.runelite.client.ui.NavigationButton;
import net.runelite.client.ui.PluginPanel;
import net.runelite.client.util.ImageUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@PluginDescriptor(
   name = "Perk Graph Editor",
   description = "Developer tool for authoring perk graph node layouts (draw_x/draw_y) visually. Requires the optional perk-graph-editor module JAR — only shipped in developer builds.",
   tags = {"perk", "graph", "editor", "dev", "layout"},
   developerPlugin = true,
   enabledByDefault = false
)
public class PerkGraphEditorPlugin extends Plugin {
   private static final Logger log = LoggerFactory.getLogger(PerkGraphEditorPlugin.class);
   private static final String EDITOR_IMPL_CLASS = "net.runelite.client.plugins.perkgrapheditor.editor.PerkGraphEditorBridgeImpl";
   @Inject
   private Client client;
   @Inject
   private ClientThread clientThread;
   @Inject
   private ClientToolbar clientToolbar;
   @Inject
   private ConfigManager configManager;
   @Inject
   private PerkGraphEditorConfig config;
   private PerkGraphEditorBridge bridge;
   private NavigationButton navButton;

   protected void startUp() {
      this.bridge = this.createBridge();
      this.bridge.initialize(new PerkGraphEditorBridge.EditorDependencies(this.client, this.clientThread, this.configManager, this.config));
      PluginPanel panel = this.bridge.getEditorPanel();
      if (panel == null) {
         log.warn("Perk Graph Editor module not on classpath — this plugin does nothing in non-developer builds. Rebuild without -PnoPerkGraphEditor=true if you want it available.");
      } else {
         BufferedImage icon = ImageUtil.loadImageResource(PerkGraphEditorPlugin.class, "/perk_graph_editor_icon.png");
         this.navButton = NavigationButton.builder().tooltip("Perk Graph Editor").icon(icon).priority(12).panel(panel).build();
         this.clientToolbar.addNavigation(this.navButton);
      }
   }

   protected void shutDown() {
      if (this.navButton != null) {
         this.clientToolbar.removeNavigation(this.navButton);
         this.navButton = null;
      }

      if (this.bridge != null) {
         this.bridge.cleanup();
         this.bridge = null;
      }

   }

   @Provides
   PerkGraphEditorConfig provideConfig(ConfigManager configManager) {
      return (PerkGraphEditorConfig)configManager.getConfig(PerkGraphEditorConfig.class);
   }

   private PerkGraphEditorBridge createBridge() {
      try {
         Class<?> implClass = Class.forName("net.runelite.client.plugins.perkgrapheditor.editor.PerkGraphEditorBridgeImpl");
         return (PerkGraphEditorBridge)implClass.getDeclaredConstructor().newInstance();
      } catch (ClassNotFoundException var2) {
         log.debug("Perk graph editor module not in classpath");
      } catch (Exception var3) {
         Exception e = var3;
         log.warn("Failed to load perk graph editor module", e);
      }

      return new NoOpPerkGraphEditorBridge();
   }
}
