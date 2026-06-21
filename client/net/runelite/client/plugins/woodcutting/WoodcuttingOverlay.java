package net.runelite.client.plugins.woodcutting;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.MenuAction;
import net.runelite.client.ui.overlay.OverlayPanel;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.components.LineComponent;
import net.runelite.client.ui.overlay.components.TitleComponent;

class WoodcuttingOverlay extends OverlayPanel {
   private static final String WOODCUTTING_RESET = "Reset";
   private static final int BUFF_BAR_DISPLAYED = 96;
   private final Client client;
   private final WoodcuttingPlugin plugin;
   private final WoodcuttingConfig config;

   @Inject
   private WoodcuttingOverlay(Client client, WoodcuttingPlugin plugin, WoodcuttingConfig config) {
      super(plugin);
      this.setPosition(OverlayPosition.TOP_LEFT);
      this.client = client;
      this.plugin = plugin;
      this.config = config;
      this.addMenuEntry(MenuAction.RUNELITE_OVERLAY_CONFIG, "Configure", "Woodcutting overlay");
      this.addMenuEntry(MenuAction.RUNELITE_OVERLAY, "Reset", "Woodcutting overlay", (e) -> {
         plugin.resetSession();
      });
   }

   public Dimension render(Graphics2D graphics) {
      WoodcuttingSession session = this.plugin.getSession();
      if (session != null && session.isActive() && this.config.showWoodcuttingStats()) {
         if (!WoodcuttingPlugin.WOODCUTTING_ANIMS.contains(this.client.getLocalPlayer().getAnimation()) && this.client.getVarpValue(4007) != 96) {
            this.panelComponent.getChildren().add(TitleComponent.builder().text("NOT woodcutting").color(Color.RED).build());
         } else {
            this.panelComponent.getChildren().add(TitleComponent.builder().text("Woodcutting").color(Color.GREEN).build());
         }

         int logsCut = session.getLogsCut();
         int bark;
         if (logsCut > 0) {
            this.panelComponent.getChildren().add(LineComponent.builder().left("Logs cut:").right(Integer.toString(logsCut)).build());
            bark = session.getLogsPerHr();
            if (bark > 0) {
               this.panelComponent.getChildren().add(LineComponent.builder().left("Logs/hr:").right(Integer.toString(bark)).build());
            }
         }

         bark = session.getBark();
         if (bark > 0) {
            this.panelComponent.getChildren().add(LineComponent.builder().left("Bark:").right(Integer.toString(bark)).build());
            int barkPerHr = session.getBarkPerHr();
            if (barkPerHr > 0) {
               this.panelComponent.getChildren().add(LineComponent.builder().left("Bark/hr:").right(Integer.toString(barkPerHr)).build());
            }
         }

         return super.render(graphics);
      } else {
         return null;
      }
   }
}
