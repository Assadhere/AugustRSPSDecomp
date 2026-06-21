package net.runelite.api.events;

import net.runelite.api.GraphicsObject;

public final class GraphicsObjectCreated {
   private final GraphicsObject graphicsObject;

   public GraphicsObjectCreated(GraphicsObject graphicsObject) {
      this.graphicsObject = graphicsObject;
   }

   public GraphicsObject getGraphicsObject() {
      return this.graphicsObject;
   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof GraphicsObjectCreated)) {
         return false;
      } else {
         GraphicsObjectCreated other = (GraphicsObjectCreated)o;
         Object this$graphicsObject = this.getGraphicsObject();
         Object other$graphicsObject = other.getGraphicsObject();
         if (this$graphicsObject == null) {
            if (other$graphicsObject != null) {
               return false;
            }
         } else if (!this$graphicsObject.equals(other$graphicsObject)) {
            return false;
         }

         return true;
      }
   }

   public int hashCode() {
      int PRIME = true;
      int result = 1;
      Object $graphicsObject = this.getGraphicsObject();
      result = result * 59 + ($graphicsObject == null ? 43 : $graphicsObject.hashCode());
      return result;
   }

   public String toString() {
      return "GraphicsObjectCreated(graphicsObject=" + String.valueOf(this.getGraphicsObject()) + ")";
   }
}
