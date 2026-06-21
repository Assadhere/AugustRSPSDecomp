package net.runelite.client.game.npcoverlay;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Shape;
import java.util.Iterator;
import java.util.Map;
import java.util.function.Predicate;
import net.runelite.api.Actor;
import net.runelite.api.Client;
import net.runelite.api.NPC;
import net.runelite.api.NPCComposition;
import net.runelite.api.Perspective;
import net.runelite.api.Point;
import net.runelite.api.coords.LocalPoint;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayUtil;
import net.runelite.client.ui.overlay.outline.ModelOutlineRenderer;
import net.runelite.client.util.Text;

class NpcOverlay extends Overlay {
   private final Client client;
   private final ModelOutlineRenderer modelOutlineRenderer;
   private final Map<NPC, HighlightedNpc> highlightedNpcs;

   NpcOverlay(Client client, ModelOutlineRenderer modelOutlineRenderer, Map<NPC, HighlightedNpc> highlightedNpcs) {
      this.client = client;
      this.modelOutlineRenderer = modelOutlineRenderer;
      this.highlightedNpcs = highlightedNpcs;
      this.setPosition(OverlayPosition.DYNAMIC);
      this.setLayer(OverlayLayer.ABOVE_SCENE);
   }

   public Dimension render(Graphics2D graphics) {
      Iterator var2 = this.highlightedNpcs.values().iterator();

      while(var2.hasNext()) {
         HighlightedNpc highlightedNpc = (HighlightedNpc)var2.next();
         this.renderNpcOverlay(graphics, highlightedNpc);
      }

      return null;
   }

   private void renderNpcOverlay(Graphics2D graphics, HighlightedNpc highlightedNpc) {
      NPC actor = highlightedNpc.getNpc();
      NPCComposition npcComposition = actor.getTransformedComposition();
      if (npcComposition != null && npcComposition.isInteractible()) {
         Predicate<NPC> render = highlightedNpc.getRender();
         if (render == null || render.test(actor)) {
            Color borderColor = highlightedNpc.getHighlightColor();
            float borderWidth = highlightedNpc.getBorderWidth();
            Color fillColor = highlightedNpc.getFillColor();
            if (highlightedNpc.isHull()) {
               Shape objectClickbox = actor.getConvexHull();
               this.renderPoly(graphics, borderColor, borderWidth, fillColor, objectClickbox);
            }

            if (highlightedNpc.isTile()) {
               Polygon tilePoly = actor.getCanvasTilePoly();
               this.renderPoly(graphics, borderColor, borderWidth, fillColor, tilePoly);
            }

            LocalPoint lp;
            if (highlightedNpc.isTrueTile()) {
               lp = LocalPoint.fromWorld(this.client, actor.getWorldLocation());
               if (lp != null) {
                  int size = npcComposition.getSize();
                  LocalPoint centerLp = lp.plus(128 * (size - 1) / 2, 128 * (size - 1) / 2);
                  Polygon tilePoly = Perspective.getCanvasTileAreaPoly(this.client, centerLp, size);
                  this.renderPoly(graphics, borderColor, borderWidth, fillColor, tilePoly);
               }
            }

            if (highlightedNpc.isSwTile()) {
               int size = npcComposition.getSize();
               LocalPoint lp = actor.getLocalLocation().plus(-((size - 1) * 128 / 2), -((size - 1) * 128 / 2));
               Polygon southWestTilePoly = Perspective.getCanvasTilePoly(this.client, lp);
               this.renderPoly(graphics, borderColor, borderWidth, fillColor, southWestTilePoly);
            }

            if (highlightedNpc.isSwTrueTile()) {
               lp = LocalPoint.fromWorld(this.client, actor.getWorldLocation());
               if (lp != null) {
                  Polygon tilePoly = Perspective.getCanvasTilePoly(this.client, lp);
                  this.renderPoly(graphics, borderColor, borderWidth, fillColor, tilePoly);
               }
            }

            if (highlightedNpc.isOutline()) {
               this.modelOutlineRenderer.drawOutline((Actor)actor, (int)highlightedNpc.getBorderWidth(), borderColor, highlightedNpc.getOutlineFeather());
            }

            if (highlightedNpc.isName() && actor.getName() != null) {
               String npcName = Text.removeTags(actor.getName());
               Point textLocation = actor.getCanvasTextLocation(graphics, npcName, actor.getLogicalHeight() + 40);
               if (textLocation != null) {
                  OverlayUtil.renderTextLocation(graphics, textLocation, npcName, borderColor);
               }
            }

         }
      }
   }

   private void renderPoly(Graphics2D graphics, Color borderColor, float borderWidth, Color fillColor, Shape polygon) {
      if (polygon != null) {
         graphics.setColor(borderColor);
         graphics.setStroke(new BasicStroke(borderWidth));
         graphics.draw(polygon);
         graphics.setColor(fillColor);
         graphics.fill(polygon);
      }

   }
}
