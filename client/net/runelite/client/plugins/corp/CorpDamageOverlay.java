package net.runelite.client.plugins.corp;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.MenuAction;
import net.runelite.api.NPC;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.widgets.Widget;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPanel;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.components.LineComponent;

class CorpDamageOverlay extends OverlayPanel {
   private final Client client;
   private final CorpPlugin corpPlugin;
   private final CorpConfig config;

   @Inject
   private CorpDamageOverlay(Client client, CorpPlugin corpPlugin, CorpConfig config) {
      super(corpPlugin);
      this.setPosition(OverlayPosition.TOP_LEFT);
      this.setLayer(OverlayLayer.UNDER_WIDGETS);
      this.setPriority(0.0F);
      this.client = client;
      this.corpPlugin = corpPlugin;
      this.config = config;
      this.addMenuEntry(MenuAction.RUNELITE_OVERLAY_CONFIG, "Configure", "Corp overlay");
   }

   public Dimension render(Graphics2D graphics) {
      Widget damageWidget = this.client.getWidget(13, 0);
      if (damageWidget != null) {
         damageWidget.setHidden(true);
      }

      NPC corp = this.corpPlugin.getCorp();
      if (corp == null) {
         return null;
      } else {
         int myDamage = this.client.getVarbitValue(999);
         int totalDamage = this.corpPlugin.getTotalDamage();
         int players = this.corpPlugin.getPlayers().size();
         int damageForKill = players != 0 ? totalDamage / players : 0;
         NPC core = this.corpPlugin.getCore();
         if (core != null) {
            WorldPoint corePoint = core.getWorldLocation();
            WorldPoint myPoint = this.client.getLocalPlayer().getWorldLocation();
            String text = null;
            if (core.getInteracting() == this.client.getLocalPlayer()) {
               text = "The core is targeting you!";
            } else if (corePoint.distanceTo(myPoint) <= 1) {
               text = "Stay away from the core!";
            }

            if (text != null) {
               FontMetrics fontMetrics = graphics.getFontMetrics();
               int textWidth = Math.max(129, fontMetrics.stringWidth(text));
               this.panelComponent.setPreferredSize(new Dimension(textWidth, 0));
               this.panelComponent.getChildren().add(LineComponent.builder().left(text).leftColor(Color.RED).build());
            }
         }

         if (this.config.showDamage()) {
            this.panelComponent.getChildren().add(LineComponent.builder().left("Your damage").right(Integer.toString(myDamage)).rightColor(damageForKill > 0 && myDamage >= damageForKill ? Color.GREEN : Color.RED).build());
            this.panelComponent.getChildren().add(LineComponent.builder().left("Total damage").right(Integer.toString(totalDamage)).build());
         }

         return super.render(graphics);
      }
   }
}
