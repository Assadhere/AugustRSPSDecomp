package net.runelite.client.plugins.barrows;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.Perspective;
import net.runelite.api.Point;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.widgets.Widget;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;

class BarrowsOverlay extends Overlay {
   private final Client client;
   private final BarrowsPlugin plugin;
   private final BarrowsConfig config;

   @Inject
   private BarrowsOverlay(Client client, BarrowsPlugin plugin, BarrowsConfig config) {
      this.setPosition(OverlayPosition.DYNAMIC);
      this.setLayer(OverlayLayer.ABOVE_WIDGETS);
      this.client = client;
      this.plugin = plugin;
      this.config = config;
   }

   public Dimension render(Graphics2D graphics) {
      if (this.plugin.isBarrowsLoaded() && this.config.showBrotherLoc()) {
         this.renderBarrowsBrothers(graphics);
      }

      Widget puzzleAnswer = this.plugin.getPuzzleAnswer();
      if (puzzleAnswer != null && this.config.showPuzzleAnswer() && !puzzleAnswer.isHidden()) {
         Rectangle answerRect = puzzleAnswer.getBounds();
         graphics.setColor(Color.GREEN);
         graphics.draw(answerRect);
      }

      return null;
   }

   private void renderBarrowsBrothers(Graphics2D graphics) {
      BarrowsBrothers[] var2 = BarrowsBrothers.values();
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         BarrowsBrothers brother = var2[var4];
         LocalPoint localLocation = LocalPoint.fromWorld(this.client, brother.getLocation());
         if (localLocation != null) {
            String brotherLetter = Character.toString(brother.getName().charAt(0));
            Point miniMapLocation = Perspective.getCanvasTextMiniMapLocation(this.client, graphics, localLocation, brotherLetter);
            if (miniMapLocation != null) {
               graphics.setColor(Color.black);
               graphics.drawString(brotherLetter, miniMapLocation.getX() + 1, miniMapLocation.getY() + 1);
               if (this.client.getVarbitValue(brother.getKilledVarbit()) > 0) {
                  graphics.setColor(this.config.deadBrotherLocColor());
               } else {
                  graphics.setColor(this.config.brotherLocColor());
               }

               graphics.drawString(brotherLetter, miniMapLocation.getX(), miniMapLocation.getY());
            }
         }
      }

   }
}
