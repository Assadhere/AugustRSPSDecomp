package net.runelite.client.plugins.cluescrolls;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import javax.inject.Inject;
import net.runelite.client.plugins.cluescrolls.clues.ClueScroll;
import net.runelite.client.plugins.cluescrolls.clues.EmoteClue;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;

public class ClueScrollWorldOverlay extends Overlay {
   public static final int IMAGE_Z_OFFSET = 30;
   public static final Color CLICKBOX_BORDER_COLOR;
   public static final Color CLICKBOX_HOVER_BORDER_COLOR;
   public static final Color CLICKBOX_FILL_COLOR;
   private final ClueScrollPlugin plugin;

   @Inject
   private ClueScrollWorldOverlay(ClueScrollPlugin plugin) {
      this.setPosition(OverlayPosition.DYNAMIC);
      this.setLayer(OverlayLayer.ABOVE_SCENE);
      this.plugin = plugin;
   }

   public Dimension render(Graphics2D graphics) {
      ClueScroll clue = this.plugin.getClue();
      if (clue != null) {
         clue.makeWorldOverlayHint(graphics, this.plugin);
      }

      EmoteClue activeSTASHClue = this.plugin.getActiveSTASHClue();
      if (activeSTASHClue != null && activeSTASHClue != clue) {
         activeSTASHClue.makeSTASHOverlay(graphics, this.plugin);
      }

      return null;
   }

   static {
      CLICKBOX_BORDER_COLOR = Color.ORANGE;
      CLICKBOX_HOVER_BORDER_COLOR = CLICKBOX_BORDER_COLOR.darker();
      CLICKBOX_FILL_COLOR = new Color(0, 255, 0, 20);
   }
}
