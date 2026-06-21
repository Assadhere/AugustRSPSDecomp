package net.runelite.client.plugins.regenmeter;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.geom.Arc2D;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.widgets.Widget;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;

class RegenMeterOverlay extends Overlay {
   private static final Color HITPOINTS_COLOR = brighter(10159875);
   private static final Color SPECIAL_COLOR = brighter(2004400);
   private static final double DIAMETER = 26.0;
   private static final int OFFSET = 27;
   private final Client client;
   private final RegenMeterPlugin plugin;
   private final RegenMeterConfig config;

   private static Color brighter(int color) {
      float[] hsv = new float[3];
      Color.RGBtoHSB(color >>> 16, color >> 8 & 255, color & 255, hsv);
      return Color.getHSBColor(hsv[0], 1.0F, 1.0F);
   }

   @Inject
   public RegenMeterOverlay(Client client, RegenMeterPlugin plugin, RegenMeterConfig config) {
      this.setPosition(OverlayPosition.DYNAMIC);
      this.setLayer(OverlayLayer.ABOVE_WIDGETS);
      this.client = client;
      this.plugin = plugin;
      this.config = config;
   }

   public Dimension render(Graphics2D g) {
      g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
      if (this.config.showHitpoints()) {
         this.renderRegen(g, 10485767, 58654725, this.plugin.getHitpointsPercentage(), HITPOINTS_COLOR);
      }

      if (this.config.showSpecial()) {
         this.renderRegen(g, 10485794, 58654752, this.plugin.getSpecialPercentage(), SPECIAL_COLOR);
      }

      return null;
   }

   private void renderRegen(Graphics2D g, int componentId, int noOrbComponentId, double percent, Color color) {
      Widget widget = this.client.getWidget(componentId);
      if (widget == null || widget.isHidden()) {
         widget = this.client.getWidget(noOrbComponentId);
      }

      if (widget != null && !widget.isHidden()) {
         Rectangle bounds = widget.getBounds();
         Arc2D.Double arc = new Arc2D.Double((double)(bounds.x + 27), (double)bounds.y + ((double)(bounds.height / 2) - 13.0), 26.0, 26.0, 90.0, -360.0 * percent, 0);
         Stroke STROKE = new BasicStroke(2.0F, 0, 0);
         g.setStroke(STROKE);
         g.setColor(color);
         g.draw(arc);
      }
   }
}
