package net.runelite.client.plugins.hunter;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.util.Iterator;
import java.util.Map;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.Perspective;
import net.runelite.api.Point;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldPoint;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.components.ProgressPieComponent;
import net.runelite.client.util.ColorUtil;

public class TrapOverlay extends Overlay {
   private static final double TIMER_LOW = 0.25;
   private final Client client;
   private final HunterPlugin plugin;
   private final HunterConfig config;
   private Color colorOpen;
   private Color colorOpenBorder;
   private Color colorEmpty;
   private Color colorEmptyBorder;
   private Color colorFull;
   private Color colorFullBorder;
   private Color colorTrans;
   private Color colorTransBorder;

   @Inject
   TrapOverlay(Client client, HunterPlugin plugin, HunterConfig config) {
      this.setPosition(OverlayPosition.DYNAMIC);
      this.setLayer(OverlayLayer.ABOVE_SCENE);
      this.plugin = plugin;
      this.config = config;
      this.client = client;
   }

   public Dimension render(Graphics2D graphics) {
      this.drawTraps(graphics);
      return null;
   }

   public void updateConfig() {
      this.colorEmptyBorder = this.config.getEmptyTrapColor();
      this.colorEmpty = ColorUtil.colorWithAlpha(this.colorEmptyBorder, (int)((double)this.colorEmptyBorder.getAlpha() / 2.5));
      this.colorFullBorder = this.config.getFullTrapColor();
      this.colorFull = ColorUtil.colorWithAlpha(this.colorFullBorder, (int)((double)this.colorFullBorder.getAlpha() / 2.5));
      this.colorOpenBorder = this.config.getOpenTrapColor();
      this.colorOpen = ColorUtil.colorWithAlpha(this.colorOpenBorder, (int)((double)this.colorOpenBorder.getAlpha() / 2.5));
      this.colorTransBorder = this.config.getTransTrapColor();
      this.colorTrans = ColorUtil.colorWithAlpha(this.colorTransBorder, (int)((double)this.colorTransBorder.getAlpha() / 2.5));
   }

   private void drawTraps(Graphics2D graphics) {
      Iterator var2 = this.plugin.getTraps().entrySet().iterator();

      while(var2.hasNext()) {
         Map.Entry<WorldPoint, HunterTrap> entry = (Map.Entry)var2.next();
         HunterTrap trap = (HunterTrap)entry.getValue();
         switch (trap.getState()) {
            case OPEN:
               this.drawTimerOnTrap(graphics, trap, this.colorOpen, this.colorOpenBorder, this.colorEmpty, this.colorOpenBorder);
               break;
            case EMPTY:
               this.drawTimerOnTrap(graphics, trap, this.colorEmpty, this.colorEmptyBorder, this.colorEmpty, this.colorEmptyBorder);
               break;
            case FULL:
               this.drawTimerOnTrap(graphics, trap, this.colorFull, this.colorFullBorder, this.colorFull, this.colorFullBorder);
               break;
            case TRANSITION:
               this.drawCircleOnTrap(graphics, trap, this.colorTrans, this.colorTransBorder);
         }
      }

   }

   private void drawTimerOnTrap(Graphics2D graphics, HunterTrap trap, Color fill, Color border, Color fillTimeLow, Color borderTimeLow) {
      if (trap.getWorldLocation().getPlane() == this.client.getPlane()) {
         LocalPoint localLoc = LocalPoint.fromWorld(this.client, trap.getWorldLocation());
         if (localLoc != null) {
            Point loc = Perspective.localToCanvas(this.client, localLoc, this.client.getPlane());
            if (loc != null) {
               double timeLeft = 1.0 - trap.getTrapTimeRelative();
               ProgressPieComponent pie = new ProgressPieComponent();
               pie.setFill(timeLeft > 0.25 ? fill : fillTimeLow);
               pie.setBorderColor(timeLeft > 0.25 ? border : borderTimeLow);
               pie.setPosition(loc);
               pie.setProgress(timeLeft);
               pie.render(graphics);
            }
         }
      }
   }

   private void drawCircleOnTrap(Graphics2D graphics, HunterTrap trap, Color fill, Color border) {
      if (trap.getWorldLocation().getPlane() == this.client.getPlane()) {
         LocalPoint localLoc = LocalPoint.fromWorld(this.client, trap.getWorldLocation());
         if (localLoc != null) {
            Point loc = Perspective.localToCanvas(this.client, localLoc, this.client.getPlane());
            ProgressPieComponent pie = new ProgressPieComponent();
            pie.setFill(fill);
            pie.setBorderColor(border);
            pie.setPosition(loc);
            pie.setProgress(1.0);
            pie.render(graphics);
         }
      }
   }
}
