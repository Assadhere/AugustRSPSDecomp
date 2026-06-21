package net.runelite.api.widgets;

import java.awt.Rectangle;
import javax.annotation.Nullable;
import net.runelite.api.Point;

public class WidgetItem {
   private final int id;
   private final int quantity;
   private final Rectangle canvasBounds;
   private final Widget widget;
   @Nullable
   private final Rectangle draggingCanvasBounds;

   public Rectangle getCanvasBounds() {
      return this.draggingCanvasBounds == null ? this.canvasBounds : this.draggingCanvasBounds;
   }

   public Rectangle getCanvasBounds(boolean dragging) {
      return dragging ? this.draggingCanvasBounds : this.canvasBounds;
   }

   public Point getCanvasLocation() {
      Rectangle bounds = this.getCanvasBounds();
      return new Point((int)bounds.getX(), (int)bounds.getY());
   }

   public WidgetItem(int id, int quantity, Rectangle canvasBounds, Widget widget, @Nullable Rectangle draggingCanvasBounds) {
      this.id = id;
      this.quantity = quantity;
      this.canvasBounds = canvasBounds;
      this.widget = widget;
      this.draggingCanvasBounds = draggingCanvasBounds;
   }

   public String toString() {
      int var10000 = this.getId();
      return "WidgetItem(id=" + var10000 + ", quantity=" + this.getQuantity() + ", canvasBounds=" + String.valueOf(this.getCanvasBounds()) + ", widget=" + String.valueOf(this.getWidget()) + ", draggingCanvasBounds=" + String.valueOf(this.getDraggingCanvasBounds()) + ")";
   }

   public int getId() {
      return this.id;
   }

   public int getQuantity() {
      return this.quantity;
   }

   public Widget getWidget() {
      return this.widget;
   }

   @Nullable
   public Rectangle getDraggingCanvasBounds() {
      return this.draggingCanvasBounds;
   }
}
