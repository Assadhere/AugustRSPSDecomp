package net.runelite.client.plugins.mta;

import java.awt.Dimension;
import java.awt.Graphics2D;
import javax.inject.Inject;
import net.runelite.client.ui.FontManager;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;

public class MTASceneOverlay extends Overlay {
   private final MTAPlugin plugin;

   @Inject
   public MTASceneOverlay(MTAPlugin plugin) {
      this.plugin = plugin;
      this.setPosition(OverlayPosition.DYNAMIC);
      this.setLayer(OverlayLayer.ABOVE_SCENE);
   }

   public Dimension render(Graphics2D graphics) {
      MTARoom[] var2 = this.plugin.getRooms();
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         MTARoom room = var2[var4];
         if (room.inside()) {
            graphics.setFont(FontManager.getRunescapeFont());
            room.under(graphics);
         }
      }

      return null;
   }
}
