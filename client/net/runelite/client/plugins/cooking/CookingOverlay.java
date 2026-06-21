package net.runelite.client.plugins.cooking;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.text.DecimalFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.MenuAction;
import net.runelite.api.Skill;
import net.runelite.client.plugins.xptracker.XpTrackerService;
import net.runelite.client.ui.overlay.OverlayPanel;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.components.LineComponent;
import net.runelite.client.ui.overlay.components.TitleComponent;

class CookingOverlay extends OverlayPanel {
   private static final int COOK_TIMEOUT = 3;
   private static final DecimalFormat FORMAT = new DecimalFormat("#.#");
   private static final String COOKING_RESET = "Reset";
   private final Client client;
   private final CookingPlugin plugin;
   private final XpTrackerService xpTrackerService;

   @Inject
   private CookingOverlay(Client client, CookingPlugin plugin, XpTrackerService xpTrackerService) {
      super(plugin);
      this.setPosition(OverlayPosition.TOP_LEFT);
      this.client = client;
      this.plugin = plugin;
      this.xpTrackerService = xpTrackerService;
      this.addMenuEntry(MenuAction.RUNELITE_OVERLAY_CONFIG, "Configure", "Cooking overlay");
      this.addMenuEntry(MenuAction.RUNELITE_OVERLAY, "Reset", "Cooking overlay", (e) -> {
         plugin.setSession((CookingSession)null);
      });
   }

   public Dimension render(Graphics2D graphics) {
      CookingSession session = this.plugin.getSession();
      if (session == null) {
         return null;
      } else {
         if (!this.isCooking() && Duration.between(session.getLastCookingAction(), Instant.now()).getSeconds() >= 3L) {
            this.panelComponent.getChildren().add(TitleComponent.builder().text("NOT cooking").color(Color.RED).build());
         } else {
            this.panelComponent.getChildren().add(TitleComponent.builder().text("Cooking").color(Color.GREEN).build());
         }

         List var10000 = this.panelComponent.getChildren();
         LineComponent.LineComponentBuilder var10001 = LineComponent.builder().left("Cooked:");
         int var10002 = session.getCookAmount();
         var10000.add(var10001.right("" + var10002 + (session.getCookAmount() >= 1 ? " (" + this.xpTrackerService.getActionsHr(Skill.COOKING) + "/hr)" : "")).build());
         var10000 = this.panelComponent.getChildren();
         var10001 = LineComponent.builder().left("Burnt:");
         var10002 = session.getBurnAmount();
         String var3;
         if (session.getBurnAmount() >= 1) {
            DecimalFormat var10003 = FORMAT;
            var3 = " (" + var10003.format(session.getBurntPercentage()) + "%)";
         } else {
            var3 = "";
         }

         var10000.add(var10001.right("" + var10002 + var3).build());
         return super.render(graphics);
      }
   }

   private boolean isCooking() {
      switch (this.client.getLocalPlayer().getAnimation()) {
         case 896:
         case 897:
            return true;
         default:
            return false;
      }
   }
}
