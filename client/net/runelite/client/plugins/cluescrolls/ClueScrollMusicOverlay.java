package net.runelite.client.plugins.cluescrolls;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.widgets.Widget;
import net.runelite.client.plugins.cluescrolls.clues.ClueScroll;
import net.runelite.client.plugins.cluescrolls.clues.MusicClue;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;

class ClueScrollMusicOverlay extends Overlay {
   private static final Rectangle PADDING = new Rectangle(2, 1, 0, 1);
   private final ClueScrollPlugin plugin;
   private final Client client;
   private boolean hasScrolled;

   @Inject
   private ClueScrollMusicOverlay(ClueScrollPlugin plugin, Client client) {
      this.setPosition(OverlayPosition.DYNAMIC);
      this.setLayer(OverlayLayer.ABOVE_WIDGETS);
      this.plugin = plugin;
      this.client = client;
   }

   public Dimension render(Graphics2D graphics) {
      ClueScroll clue = this.plugin.getClue();
      if (!(clue instanceof MusicClue)) {
         this.hasScrolled = false;
         return null;
      } else {
         MusicClue musicClue = (MusicClue)clue;
         Widget musicContainer = this.client.getWidget(15663104);
         if (musicContainer != null && !musicContainer.isHidden()) {
            Widget trackList = this.client.getWidget(15663115);
            String trackToFind = musicClue.getSong();
            Widget found = null;
            if (trackList == null) {
               return null;
            } else {
               Widget[] var8 = trackList.getDynamicChildren();
               int var9 = var8.length;

               for(int var10 = 0; var10 < var9; ++var10) {
                  Widget track = var8[var10];
                  if (track.getText().equals(trackToFind)) {
                     found = track;
                     break;
                  }
               }

               if (found == null) {
                  return null;
               } else {
                  if (!this.hasScrolled) {
                     this.hasScrolled = true;
                     this.plugin.scrollToWidget(15663113, 15663116, found);
                  }

                  this.plugin.highlightWidget(graphics, found, trackList, PADDING, (String)null);
                  return null;
               }
            }
         } else {
            return null;
         }
      }
   }
}
