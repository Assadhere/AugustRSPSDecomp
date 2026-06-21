package net.runelite.client.ui.overlay.components;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;

public class ImageComponent implements LayoutableRenderableEntity {
   private final BufferedImage image;
   private final Rectangle bounds = new Rectangle();
   private Point preferredLocation = new Point();

   public Dimension render(Graphics2D graphics) {
      if (this.image == null) {
         return null;
      } else {
         graphics.drawImage(this.image, this.preferredLocation.x, this.preferredLocation.y, (ImageObserver)null);
         Dimension dimension = new Dimension(this.image.getWidth(), this.image.getHeight());
         this.bounds.setLocation(this.preferredLocation);
         this.bounds.setSize(dimension);
         return dimension;
      }
   }

   public void setPreferredSize(Dimension dimension) {
   }

   public ImageComponent(BufferedImage image) {
      this.image = image;
   }

   public void setPreferredLocation(Point preferredLocation) {
      this.preferredLocation = preferredLocation;
   }

   public Rectangle getBounds() {
      return this.bounds;
   }
}
