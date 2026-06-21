package net.runelite.client.plugins.playerindicators;

import java.awt.Dimension;
import java.awt.Graphics2D;
import javax.inject.Inject;
import javax.inject.Singleton;
import net.runelite.api.Player;
import net.runelite.api.Point;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayUtil;
import net.runelite.client.util.Text;

@Singleton
public class PlayerIndicatorsMinimapOverlay extends Overlay {
   private final PlayerIndicatorsService playerIndicatorsService;
   private final PlayerIndicatorsConfig config;

   @Inject
   private PlayerIndicatorsMinimapOverlay(PlayerIndicatorsConfig config, PlayerIndicatorsService playerIndicatorsService) {
      this.config = config;
      this.playerIndicatorsService = playerIndicatorsService;
      this.setLayer(OverlayLayer.ABOVE_WIDGETS);
      this.setPosition(OverlayPosition.DYNAMIC);
      this.setPriority(0.75F);
   }

   public Dimension render(Graphics2D graphics) {
      this.playerIndicatorsService.forEachPlayer((player, decorations) -> {
         this.renderPlayerOverlay(graphics, player, decorations);
      });
      return null;
   }

   private void renderPlayerOverlay(Graphics2D graphics, Player actor, PlayerIndicatorsService.Decorations decorations) {
      String name = Text.removeTags(actor.getName().replace(' ', ' '));
      if (this.config.drawMinimapNames()) {
         Point minimapLocation = actor.getMinimapLocation();
         if (minimapLocation != null) {
            OverlayUtil.renderTextLocation(graphics, minimapLocation, name, decorations.getColor());
         }
      }

   }
}
