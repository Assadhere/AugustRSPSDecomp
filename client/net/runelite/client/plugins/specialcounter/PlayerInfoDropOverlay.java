package net.runelite.client.plugins.specialcounter;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Iterator;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;
import net.runelite.api.Client;
import net.runelite.api.Player;
import net.runelite.api.Point;
import net.runelite.api.WorldView;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayUtil;
import net.runelite.client.util.ColorUtil;
import net.runelite.client.util.ImageUtil;

@Singleton
class PlayerInfoDropOverlay extends Overlay {
   private final SpecialCounterPlugin plugin;
   private final SpecialCounterConfig config;
   private final Client client;

   @Inject
   private PlayerInfoDropOverlay(SpecialCounterPlugin plugin, SpecialCounterConfig config, Client client) {
      this.plugin = plugin;
      this.config = config;
      this.client = client;
      this.setPosition(OverlayPosition.DYNAMIC);
      this.setPriority(0.5F);
   }

   public Dimension render(Graphics2D graphics) {
      List<PlayerInfoDrop> infoDrops = this.plugin.getPlayerInfoDrops();
      if (infoDrops.isEmpty()) {
         return null;
      } else {
         WorldView wv = this.client.getTopLevelWorldView();
         int cycle = this.client.getGameCycle();
         Iterator<PlayerInfoDrop> iterator = infoDrops.iterator();

         while(iterator.hasNext()) {
            PlayerInfoDrop infoDrop = (PlayerInfoDrop)iterator.next();
            if (cycle >= infoDrop.getStartCycle()) {
               if (cycle > infoDrop.getEndCycle()) {
                  iterator.remove();
               } else if (this.config.specDrops()) {
                  Player player = (Player)wv.players().byIndex(infoDrop.getPlayerIdx());
                  if (player != null) {
                     int elapsed = cycle - infoDrop.getStartCycle();
                     int percent = elapsed * 100 / (infoDrop.getEndCycle() - infoDrop.getStartCycle());
                     int currentHeight = infoDrop.getEndHeightOffset() * percent / 100;
                     String text = infoDrop.getText();
                     graphics.setFont(infoDrop.getFont());
                     Point playerLocation = player.getCanvasTextLocation(graphics, text, player.getLogicalHeight() + infoDrop.getStartHeightOffset() + currentHeight);
                     if (playerLocation != null) {
                        FontMetrics fontMetrics = graphics.getFontMetrics();
                        int alpha = 255 - 255 * percent / 100;
                        BufferedImage sprite = infoDrop.getImage();
                        int textHeight = fontMetrics.getHeight() - fontMetrics.getMaxDescent();
                        int textWidth = fontMetrics.stringWidth(text);
                        int textMargin = sprite.getWidth() / 2;
                        Point imageLocation = new Point(playerLocation.getX() - textMargin - 1, playerLocation.getY() - textHeight / 2 - sprite.getHeight() / 2);
                        Point textLocation = new Point(playerLocation.getX() + textMargin, playerLocation.getY());
                        BufferedImage backgroundSprite = infoDrop.getTextBackground();
                        if (backgroundSprite != null) {
                           int x = textLocation.getX();
                           int y = textLocation.getY();
                           y -= textHeight / 2;
                           y -= backgroundSprite.getHeight() / 2;
                           x += (textWidth + 1) / 2;
                           x -= backgroundSprite.getWidth() / 2;
                           OverlayUtil.renderImageLocation(graphics, new Point(x, y), ImageUtil.alphaOffset(backgroundSprite, alpha - 255));
                        }

                        OverlayUtil.renderImageLocation(graphics, imageLocation, ImageUtil.alphaOffset(sprite, alpha - 255));
                        drawText(graphics, textLocation, text, infoDrop.getColor(), alpha);
                     }
                  }
               }
            }
         }

         return null;
      }
   }

   private static void drawText(Graphics2D g, Point point, String text, Color color, int colorAlpha) {
      g.setColor(ColorUtil.colorWithAlpha(Color.BLACK, colorAlpha));
      g.drawString(text, point.getX() + 1, point.getY() + 1);
      g.setColor(ColorUtil.colorWithAlpha(color, colorAlpha));
      g.drawString(text, point.getX(), point.getY());
   }
}
