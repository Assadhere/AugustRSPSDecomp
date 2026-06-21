package net.runelite.client.plugins.dpscounter;

import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.text.DecimalFormat;
import java.time.Duration;
import java.util.Iterator;
import java.util.Map;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.MenuAction;
import net.runelite.api.Player;
import net.runelite.client.party.PartyService;
import net.runelite.client.ui.overlay.OverlayPanel;
import net.runelite.client.ui.overlay.components.LineComponent;
import net.runelite.client.ui.overlay.components.TitleComponent;
import net.runelite.client.ui.overlay.tooltip.Tooltip;
import net.runelite.client.ui.overlay.tooltip.TooltipManager;
import net.runelite.client.util.QuantityFormatter;

class DpsOverlay extends OverlayPanel {
   private static final DecimalFormat DPS_FORMAT = new DecimalFormat("#0.0");
   private static final int PANEL_WIDTH_OFFSET = 10;
   private final DpsCounterPlugin dpsCounterPlugin;
   private final DpsConfig dpsConfig;
   private final PartyService partyService;
   private final Client client;
   private final TooltipManager tooltipManager;

   @Inject
   DpsOverlay(DpsCounterPlugin dpsCounterPlugin, DpsConfig dpsConfig, PartyService partyService, Client client, TooltipManager tooltipManager) {
      super(dpsCounterPlugin);
      this.dpsCounterPlugin = dpsCounterPlugin;
      this.dpsConfig = dpsConfig;
      this.partyService = partyService;
      this.client = client;
      this.tooltipManager = tooltipManager;
      this.addMenuEntry(MenuAction.RUNELITE_OVERLAY, "Reset", "DPS counter", (e) -> {
         dpsCounterPlugin.reset();
      });
      this.setPaused(false);
   }

   public void onMouseOver() {
      DpsMember total = this.dpsCounterPlugin.getTotal();
      Duration elapsed = total.elapsed();
      long s = elapsed.getSeconds();
      String format;
      if (s >= 3600L) {
         format = String.format("%d:%02d:%02d", s / 3600L, s % 3600L / 60L, s % 60L);
      } else {
         format = String.format("%d:%02d", s / 60L, s % 60L);
      }

      this.tooltipManager.add(new Tooltip("Elapsed time: " + format));
   }

   public Dimension render(Graphics2D graphics) {
      Map<String, DpsMember> dpsMembers = this.dpsCounterPlugin.getMembers();
      if (dpsMembers.isEmpty()) {
         return null;
      } else {
         boolean inParty = this.partyService.isInParty();
         boolean showDamage = this.dpsConfig.showDamage();
         DpsMember total = this.dpsCounterPlugin.getTotal();
         boolean paused = total.isPaused();
         String title = (inParty ? "Party " : "") + (showDamage ? "Damage" : "DPS") + (paused ? " (paused)" : "");
         this.panelComponent.getChildren().add(TitleComponent.builder().text(title).build());
         int maxWidth = 129;
         FontMetrics fontMetrics = graphics.getFontMetrics();
         Iterator var10 = dpsMembers.values().iterator();

         DpsMember self;
         while(var10.hasNext()) {
            self = (DpsMember)var10.next();
            String left = self.getName();
            String right = showDamage ? QuantityFormatter.formatNumber((long)self.getDamage()) : DPS_FORMAT.format((double)self.getDps());
            maxWidth = Math.max(maxWidth, fontMetrics.stringWidth(left) + fontMetrics.stringWidth(right));
            this.panelComponent.getChildren().add(LineComponent.builder().left(left).right(right).build());
         }

         this.panelComponent.setPreferredSize(new Dimension(maxWidth + 10, 0));
         if (!inParty) {
            Player player = this.client.getLocalPlayer();
            if (player.getName() != null) {
               self = (DpsMember)dpsMembers.get(player.getName());
               if (self != null && total.getDamage() > self.getDamage()) {
                  this.panelComponent.getChildren().add(LineComponent.builder().left(total.getName()).right(showDamage ? Integer.toString(total.getDamage()) : DPS_FORMAT.format((double)total.getDps())).build());
               }
            }
         }

         return super.render(graphics);
      }
   }

   void setPaused(boolean paused) {
      this.removeMenuEntry(MenuAction.RUNELITE_OVERLAY, "Pause", "DPS counter");
      this.removeMenuEntry(MenuAction.RUNELITE_OVERLAY, "Unpause", "DPS counter");
      if (paused) {
         this.addMenuEntry(MenuAction.RUNELITE_OVERLAY, "Unpause", "DPS counter", (e) -> {
            this.dpsCounterPlugin.unpause();
         });
      } else {
         this.addMenuEntry(MenuAction.RUNELITE_OVERLAY, "Pause", "DPS counter", (e) -> {
            this.dpsCounterPlugin.pause();
         });
      }

   }
}
