package net.runelite.client.ui.overlay.tooltip;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.util.Iterator;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;
import net.runelite.api.Client;
import net.runelite.api.Point;
import net.runelite.client.config.RuneLiteConfig;
import net.runelite.client.config.TooltipPositionType;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.components.LayoutableRenderableEntity;
import net.runelite.client.ui.overlay.components.PanelComponent;
import net.runelite.client.ui.overlay.components.TooltipComponent;

@Singleton
public class TooltipOverlay extends Overlay {
   private static final int UNDER_OFFSET = 24;
   private static final int PADDING = 2;
   private final TooltipManager tooltipManager;
   private final Client client;
   private final RuneLiteConfig runeLiteConfig;
   private int prevWidth;
   private int prevHeight;

   @Inject
   private TooltipOverlay(Client client, TooltipManager tooltipManager, RuneLiteConfig runeLiteConfig) {
      this.client = client;
      this.tooltipManager = tooltipManager;
      this.runeLiteConfig = runeLiteConfig;
      this.setPosition(OverlayPosition.TOOLTIP);
      this.setPriority(1.0F);
      this.setLayer(OverlayLayer.ABOVE_WIDGETS);
      this.drawAfterInterface(165);
   }

   public Dimension render(Graphics2D graphics) {
      List<Tooltip> tooltips = this.tooltipManager.getTooltips();
      if (tooltips.isEmpty()) {
         return null;
      } else {
         Dimension var3;
         try {
            var3 = this.renderTooltips(graphics, tooltips);
         } finally {
            this.tooltipManager.clear();
         }

         return var3;
      }
   }

   private Dimension renderTooltips(Graphics2D graphics, List<Tooltip> tooltips) {
      int canvasWidth = this.client.getCanvasWidth();
      int canvasHeight = this.client.getCanvasHeight();
      Point mouseCanvasPosition = this.client.getMouseCanvasPosition();
      int tooltipX = Math.min(canvasWidth - this.prevWidth, mouseCanvasPosition.getX());
      int tooltipY = this.runeLiteConfig.tooltipPosition() == TooltipPositionType.ABOVE_CURSOR ? Math.max(0, mouseCanvasPosition.getY() - this.prevHeight) : Math.min(canvasHeight - this.prevHeight, mouseCanvasPosition.getY() + 24);
      int width = 0;
      int height = 0;

      Dimension dimension;
      for(Iterator var10 = tooltips.iterator(); var10.hasNext(); width = Math.max(width, dimension.width)) {
         Tooltip tooltip = (Tooltip)var10.next();
         Object entity;
         if (tooltip.getComponent() != null) {
            entity = tooltip.getComponent();
            if (entity instanceof PanelComponent) {
               ((PanelComponent)entity).setBackgroundColor(this.runeLiteConfig.overlayBackgroundColor());
            }
         } else {
            TooltipComponent tooltipComponent = new TooltipComponent();
            tooltipComponent.setModIcons(this.client.getModIcons());
            tooltipComponent.setText(tooltip.getText());
            tooltipComponent.setBackgroundColor(this.runeLiteConfig.overlayBackgroundColor());
            entity = tooltipComponent;
         }

         ((LayoutableRenderableEntity)entity).setPreferredLocation(new java.awt.Point(tooltipX, tooltipY + height));
         dimension = ((LayoutableRenderableEntity)entity).render(graphics);
         height += dimension.height + 2;
      }

      this.prevWidth = width;
      this.prevHeight = height;
      return null;
   }
}
