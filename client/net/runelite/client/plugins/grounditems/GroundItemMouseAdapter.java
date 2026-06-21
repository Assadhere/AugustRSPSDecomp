package net.runelite.client.plugins.grounditems;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import javax.inject.Inject;
import javax.swing.SwingUtilities;
import net.runelite.client.input.MouseAdapter;

class GroundItemMouseAdapter extends MouseAdapter {
   @Inject
   private GroundItemsPlugin plugin;

   public MouseEvent mousePressed(MouseEvent e) {
      Point mousePos = e.getPoint();
      if (this.plugin.isHotKeyPressed()) {
         if (SwingUtilities.isLeftMouseButton(e)) {
            if (this.plugin.getHiddenBoxBounds() != null && ((Rectangle)this.plugin.getHiddenBoxBounds().getKey()).contains(mousePos)) {
               this.plugin.updateList(((GroundItem)this.plugin.getHiddenBoxBounds().getValue()).getName(), true);
               e.consume();
               return e;
            }

            if (this.plugin.getHighlightBoxBounds() != null && ((Rectangle)this.plugin.getHighlightBoxBounds().getKey()).contains(mousePos)) {
               this.plugin.updateList(((GroundItem)this.plugin.getHighlightBoxBounds().getValue()).getName(), false);
               e.consume();
               return e;
            }

            if (this.plugin.getTextBoxBounds() != null && ((Rectangle)this.plugin.getTextBoxBounds().getKey()).contains(mousePos)) {
               this.plugin.updateList(((GroundItem)this.plugin.getTextBoxBounds().getValue()).getName(), false);
               e.consume();
               return e;
            }
         } else if (SwingUtilities.isRightMouseButton(e) && this.plugin.getTextBoxBounds() != null && ((Rectangle)this.plugin.getTextBoxBounds().getKey()).contains(mousePos)) {
            this.plugin.updateList(((GroundItem)this.plugin.getTextBoxBounds().getValue()).getName(), true);
            e.consume();
            return e;
         }
      }

      return e;
   }
}
