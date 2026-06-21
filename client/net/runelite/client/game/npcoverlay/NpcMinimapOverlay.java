package net.runelite.client.game.npcoverlay;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.util.Iterator;
import java.util.Map;
import java.util.function.Predicate;
import net.runelite.api.NPC;
import net.runelite.api.NPCComposition;
import net.runelite.api.Point;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayUtil;
import net.runelite.client.util.Text;

class NpcMinimapOverlay extends Overlay {
   private final Map<NPC, HighlightedNpc> highlightedNpcs;

   NpcMinimapOverlay(Map<NPC, HighlightedNpc> highlightedNpcs) {
      this.highlightedNpcs = highlightedNpcs;
      this.setPosition(OverlayPosition.DYNAMIC);
      this.setLayer(OverlayLayer.ABOVE_WIDGETS);
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
            Point minimapLocation = actor.getMinimapLocation();
            if (minimapLocation != null) {
               Color color = highlightedNpc.getHighlightColor();
               OverlayUtil.renderMinimapLocation(graphics, minimapLocation, color);
               if (highlightedNpc.isNameOnMinimap() && actor.getName() != null) {
                  String name = Text.removeTags(actor.getName());
                  OverlayUtil.renderTextLocation(graphics, minimapLocation, name, color);
               }
            }

         }
      }
   }
}
