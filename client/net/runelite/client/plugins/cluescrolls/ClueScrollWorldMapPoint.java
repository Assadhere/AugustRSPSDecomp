package net.runelite.client.plugins.cluescrolls;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import net.runelite.api.Point;
import net.runelite.api.coords.WorldPoint;
import net.runelite.client.ui.overlay.worldmap.WorldMapPoint;

class ClueScrollWorldMapPoint extends WorldMapPoint {
   private final ClueScrollPlugin plugin;
   private final BufferedImage clueScrollWorldImage;
   private final Point clueScrollWorldImagePoint;

   ClueScrollWorldMapPoint(WorldPoint worldPoint, ClueScrollPlugin plugin) {
      super(worldPoint, (BufferedImage)null);
      this.clueScrollWorldImage = new BufferedImage(plugin.getMapArrow().getWidth(), plugin.getMapArrow().getHeight(), 2);
      Graphics graphics = this.clueScrollWorldImage.getGraphics();
      graphics.drawImage(plugin.getMapArrow(), 0, 0, (ImageObserver)null);
      graphics.drawImage(plugin.getClueScrollImage(), 0, 0, (ImageObserver)null);
      this.clueScrollWorldImagePoint = new Point(this.clueScrollWorldImage.getWidth() / 2, this.clueScrollWorldImage.getHeight());
      this.plugin = plugin;
      this.setSnapToEdge(true);
      this.setJumpOnClick(true);
      this.setName("Clue Scroll");
      this.setImage(this.clueScrollWorldImage);
      this.setImagePoint(this.clueScrollWorldImagePoint);
   }

   public void onEdgeSnap() {
      this.setImage(this.plugin.getClueScrollImage());
      this.setImagePoint((Point)null);
   }

   public void onEdgeUnsnap() {
      this.setImage(this.clueScrollWorldImage);
      this.setImagePoint(this.clueScrollWorldImagePoint);
   }
}
