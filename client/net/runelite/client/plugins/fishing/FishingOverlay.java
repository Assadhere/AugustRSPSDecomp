package net.runelite.client.plugins.fishing;

import com.google.common.collect.ImmutableSet;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.util.Set;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.MenuAction;
import net.runelite.api.Skill;
import net.runelite.client.plugins.xptracker.XpTrackerService;
import net.runelite.client.ui.overlay.OverlayPanel;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.components.LineComponent;
import net.runelite.client.ui.overlay.components.TitleComponent;

class FishingOverlay extends OverlayPanel {
   private static final String FISHING_SPOT = "Fishing spot";
   private static final String FISHING_RESET = "Reset";
   private static final Set<Integer> FISHING_ANIMATIONS = ImmutableSet.of(5108, 6709, 6705, 6706, 6707, 6708, new Integer[]{6710, 6711, 6703, 6704, 620, 619, 8336, 7401, 88, 618, 7402, 8783, 11867, 8784, 11868, 1193, 621, 622, 623, 8188, 8189, 8190, 8191, 8192, 8193, 6932, 9350});
   private final Client client;
   private final FishingPlugin plugin;
   private final FishingConfig config;
   private final XpTrackerService xpTrackerService;

   @Inject
   public FishingOverlay(Client client, FishingPlugin plugin, FishingConfig config, XpTrackerService xpTrackerService) {
      super(plugin);
      this.setPosition(OverlayPosition.TOP_LEFT);
      this.client = client;
      this.plugin = plugin;
      this.config = config;
      this.xpTrackerService = xpTrackerService;
      this.addMenuEntry(MenuAction.RUNELITE_OVERLAY_CONFIG, "Configure", "Fishing overlay");
      this.addMenuEntry(MenuAction.RUNELITE_OVERLAY, "Reset", "Fishing overlay", (e) -> {
         plugin.reset();
      });
   }

   public Dimension render(Graphics2D graphics) {
      if (this.config.showFishingStats() && this.plugin.getSession().getLastFishCaught() != null) {
         if (this.client.getLocalPlayer().getInteracting() != null && this.client.getLocalPlayer().getInteracting().getName().contains("Fishing spot") && this.client.getLocalPlayer().getInteracting().getGraphic() != 1387 && FISHING_ANIMATIONS.contains(this.client.getLocalPlayer().getAnimation())) {
            this.panelComponent.getChildren().add(TitleComponent.builder().text("Fishing").color(Color.GREEN).build());
         } else {
            this.panelComponent.getChildren().add(TitleComponent.builder().text("NOT fishing").color(Color.RED).build());
         }

         int actions = this.xpTrackerService.getActions(Skill.FISHING);
         if (actions > 0) {
            this.panelComponent.getChildren().add(LineComponent.builder().left("Caught fish:").right(Integer.toString(actions)).build());
            if (actions > 2) {
               this.panelComponent.getChildren().add(LineComponent.builder().left("Fish/hr:").right(Integer.toString(this.xpTrackerService.getActionsHr(Skill.FISHING))).build());
            }
         }

         return super.render(graphics);
      } else {
         return null;
      }
   }
}
