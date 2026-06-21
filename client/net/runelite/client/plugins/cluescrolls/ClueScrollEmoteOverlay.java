package net.runelite.client.plugins.cluescrolls;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.widgets.Widget;
import net.runelite.client.plugins.cluescrolls.clues.ClueScroll;
import net.runelite.client.plugins.cluescrolls.clues.EmoteClue;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;

class ClueScrollEmoteOverlay extends Overlay {
   private final ClueScrollPlugin plugin;
   private final Client client;
   private boolean hasScrolled;

   @Inject
   private ClueScrollEmoteOverlay(ClueScrollPlugin plugin, Client client) {
      this.setPosition(OverlayPosition.DYNAMIC);
      this.setLayer(OverlayLayer.ABOVE_WIDGETS);
      this.plugin = plugin;
      this.client = client;
   }

   public Dimension render(Graphics2D graphics) {
      ClueScroll clue = this.plugin.getClue();
      if (!(clue instanceof EmoteClue)) {
         this.hasScrolled = false;
         return null;
      } else {
         EmoteClue emoteClue = (EmoteClue)clue;
         if (!emoteClue.getFirstEmote().hasSprite()) {
            return null;
         } else {
            Widget emoteContainer = this.client.getWidget(14155778);
            if (emoteContainer != null && !emoteContainer.isHidden()) {
               Widget emoteWindow = this.client.getWidget(14155776);
               if (emoteWindow == null) {
                  return null;
               } else {
                  Widget firstEmoteWidget = null;
                  Widget secondEmoteWidget = null;
                  Widget[] var8 = emoteContainer.getDynamicChildren();
                  int var9 = var8.length;

                  for(int var10 = 0; var10 < var9; ++var10) {
                     Widget emoteWidget = var8[var10];
                     if (emoteWidget.getSpriteId() == emoteClue.getFirstEmote().getSpriteId()) {
                        firstEmoteWidget = emoteWidget;
                        this.plugin.highlightWidget(graphics, emoteWidget, emoteWindow, (Rectangle)null, emoteClue.getSecondEmote() != null ? "1st" : null);
                     } else if (emoteClue.getSecondEmote() != null && emoteWidget.getSpriteId() == emoteClue.getSecondEmote().getSpriteId()) {
                        secondEmoteWidget = emoteWidget;
                        this.plugin.highlightWidget(graphics, emoteWidget, emoteWindow, (Rectangle)null, "2nd");
                     }
                  }

                  if (!this.hasScrolled) {
                     this.hasScrolled = true;
                     this.plugin.scrollToWidget(14155777, 14155780, firstEmoteWidget, secondEmoteWidget);
                  }

                  return null;
               }
            } else {
               return null;
            }
         }
      }
   }
}
