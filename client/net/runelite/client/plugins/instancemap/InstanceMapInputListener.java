package net.runelite.client.plugins.instancemap;

import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import javax.inject.Inject;
import javax.swing.SwingUtilities;
import net.runelite.client.input.KeyListener;
import net.runelite.client.input.MouseAdapter;
import net.runelite.client.input.MouseWheelListener;

public class InstanceMapInputListener extends MouseAdapter implements KeyListener, MouseWheelListener {
   @Inject
   private InstanceMapPlugin plugin;
   @Inject
   private InstanceMapOverlay overlay;

   public void keyTyped(KeyEvent event) {
   }

   public void keyPressed(KeyEvent event) {
      if (this.overlay.isMapShown()) {
         if (event.getKeyCode() == 27) {
            this.plugin.closeMap();
            event.consume();
         }

      }
   }

   public void keyReleased(KeyEvent event) {
   }

   public MouseWheelEvent mouseWheelMoved(MouseWheelEvent event) {
      if (this.overlay.isMapShown() && !this.isNotWithinOverlay(event.getPoint())) {
         int direction = event.getWheelRotation();
         if (direction > 0) {
            this.plugin.ascendMap();
         } else {
            this.plugin.descendMap();
         }

         event.consume();
         return event;
      } else {
         return event;
      }
   }

   public MouseEvent mouseClicked(MouseEvent event) {
      if (this.overlay.isMapShown() && !this.isNotWithinOverlay(event.getPoint())) {
         event.consume();
         return event;
      } else {
         return event;
      }
   }

   public MouseEvent mousePressed(MouseEvent event) {
      if (this.overlay.isMapShown() && !this.isNotWithinOverlay(event.getPoint())) {
         if (SwingUtilities.isLeftMouseButton(event) && this.isWithinCloseButton(event.getPoint())) {
            this.plugin.closeMap();
         }

         event.consume();
         return event;
      } else {
         return event;
      }
   }

   public MouseEvent mouseMoved(MouseEvent event) {
      if (this.overlay.isMapShown()) {
         this.overlay.setCloseButtonHovered(this.isWithinCloseButton(event.getPoint()));
      }

      return event;
   }

   private boolean isNotWithinOverlay(Point point) {
      return !this.overlay.getBounds().contains(point);
   }

   private boolean isWithinCloseButton(Point point) {
      Point overlayPoint = new Point(point.x - (int)this.overlay.getBounds().getX(), point.y - (int)this.overlay.getBounds().getY());
      return this.overlay.getCloseButtonBounds() != null && this.overlay.getCloseButtonBounds().contains(overlayPoint);
   }
}
