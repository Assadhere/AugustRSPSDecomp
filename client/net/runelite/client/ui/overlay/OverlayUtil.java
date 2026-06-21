package net.runelite.client.ui.overlay;

import com.google.common.base.Strings;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import net.runelite.api.Actor;
import net.runelite.api.Client;
import net.runelite.api.Perspective;
import net.runelite.api.Point;
import net.runelite.api.TileObject;
import net.runelite.api.coords.LocalPoint;
import net.runelite.client.util.ColorUtil;

public class OverlayUtil {
   private static final int MINIMAP_DOT_RADIUS = 4;

   public static void renderPolygon(Graphics2D graphics, Shape poly, Color color) {
      renderPolygon(graphics, poly, color, new BasicStroke(2.0F));
   }

   public static void renderPolygon(Graphics2D graphics, Shape poly, Color color, Stroke borderStroke) {
      renderPolygon(graphics, poly, color, new Color(0, 0, 0, 50), borderStroke);
   }

   public static void renderPolygon(Graphics2D graphics, Shape poly, Color color, Color fillColor, Stroke borderStroke) {
      graphics.setColor(color);
      Stroke originalStroke = graphics.getStroke();
      graphics.setStroke(borderStroke);
      graphics.draw(poly);
      graphics.setColor(fillColor);
      graphics.fill(poly);
      graphics.setStroke(originalStroke);
   }

   public static void renderMinimapLocation(Graphics2D graphics, Point mini, Color color) {
      graphics.setColor(Color.BLACK);
      graphics.fillOval(mini.getX() - 2, mini.getY() - 2 + 1, 4, 4);
      graphics.setColor(ColorUtil.colorWithAlpha(color, 255));
      graphics.fillOval(mini.getX() - 2, mini.getY() - 2, 4, 4);
   }

   /** @deprecated */
   @Deprecated
   public static void renderMinimapRect(Client client, Graphics2D graphics, Point center, int width, int height, Color color) {
      double angle = (double)client.getCameraYawTarget() * 0.0030679615;
      graphics.setColor(color);
      graphics.rotate(angle, (double)center.getX(), (double)center.getY());
      graphics.drawRect(center.getX() - width / 2, center.getY() - height / 2, width - 1, height - 1);
      graphics.rotate(-angle, (double)center.getX(), (double)center.getY());
   }

   public static void renderTextLocation(Graphics2D graphics, Point txtLoc, String text, Color color) {
      if (!Strings.isNullOrEmpty(text)) {
         int x = txtLoc.getX();
         int y = txtLoc.getY();
         graphics.setColor(Color.BLACK);
         graphics.drawString(text, x + 1, y + 1);
         graphics.setColor(ColorUtil.colorWithAlpha(color, 255));
         graphics.drawString(text, x, y);
      }
   }

   public static void renderImageLocation(Client client, Graphics2D graphics, LocalPoint localPoint, BufferedImage image, int zOffset) {
      Point imageLocation = Perspective.getCanvasImageLocation(client, localPoint, image, zOffset);
      if (imageLocation != null) {
         renderImageLocation(graphics, imageLocation, image);
      }

   }

   public static void renderImageLocation(Graphics2D graphics, Point imgLoc, BufferedImage image) {
      int x = imgLoc.getX();
      int y = imgLoc.getY();
      graphics.drawImage(image, x, y, (ImageObserver)null);
   }

   public static void renderActorOverlay(Graphics2D graphics, Actor actor, String text, Color color) {
      Polygon poly = actor.getCanvasTilePoly();
      if (poly != null) {
         renderPolygon(graphics, poly, color);
      }

      Point textLocation = actor.getCanvasTextLocation(graphics, text, actor.getLogicalHeight() + 40);
      if (textLocation != null) {
         renderTextLocation(graphics, textLocation, text, color);
      }

   }

   public static void renderActorOverlayImage(Graphics2D graphics, Actor actor, BufferedImage image, Color color, int zOffset) {
      Polygon poly = actor.getCanvasTilePoly();
      if (poly != null) {
         renderPolygon(graphics, poly, color);
      }

      Point imageLocation = actor.getCanvasImageLocation(image, zOffset);
      if (imageLocation != null) {
         renderImageLocation(graphics, imageLocation, image);
      }

   }

   public static void renderTileOverlay(Graphics2D graphics, TileObject tileObject, String text, Color color) {
      Polygon poly = tileObject.getCanvasTilePoly();
      if (poly != null) {
         renderPolygon(graphics, poly, color);
      }

      Point minimapLocation = tileObject.getMinimapLocation();
      if (minimapLocation != null) {
         renderMinimapLocation(graphics, minimapLocation, color);
      }

      Point textLocation = tileObject.getCanvasTextLocation(graphics, text, 0);
      if (textLocation != null) {
         renderTextLocation(graphics, textLocation, text, color);
      }

   }

   public static void renderTileOverlay(Client client, Graphics2D graphics, LocalPoint localLocation, BufferedImage image, Color color) {
      Polygon poly = Perspective.getCanvasTilePoly(client, localLocation);
      if (poly != null) {
         renderPolygon(graphics, poly, color);
      }

      renderImageLocation(client, graphics, localLocation, image, 0);
   }

   public static void renderHoverableArea(Graphics2D graphics, Shape area, Point mousePosition, Color fillColor, Color borderColor, Color borderHoverColor) {
      if (area != null) {
         if (area.contains((double)mousePosition.getX(), (double)mousePosition.getY())) {
            graphics.setColor(borderHoverColor);
         } else {
            graphics.setColor(borderColor);
         }

         graphics.draw(area);
         graphics.setColor(fillColor);
         graphics.fill(area);
      }

   }

   public static void setGraphicProperties(Graphics2D graphics) {
      graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
   }

   static void shiftSnapCorner(OverlayPosition overlayPosition, Rectangle snapCorner, Rectangle bounds, int padding) {
      int sX = snapCorner.x;
      int sY = snapCorner.y;
      switch (overlayPosition) {
         case BOTTOM_LEFT:
            sX = Math.max(sX, bounds.x + bounds.width + padding);
            break;
         case BOTTOM_RIGHT:
            sX = Math.min(sX, bounds.x - padding);
            break;
         case TOP_LEFT:
         case TOP_CENTER:
         case CANVAS_TOP_RIGHT:
         case TOP_RIGHT:
            sY = Math.max(sY, bounds.y + bounds.height + padding);
            break;
         case ABOVE_CHATBOX_RIGHT:
            sY = Math.min(sY, bounds.y - padding);
            break;
         default:
            throw new IllegalArgumentException();
      }

      snapCorner.x = sX;
      snapCorner.y = sY;
   }

   public static java.awt.Point transformPosition(OverlayPosition position, Dimension dimension) {
      java.awt.Point result = new java.awt.Point();
      switch (position) {
         case BOTTOM_LEFT:
            result.y = -dimension.height;
            break;
         case BOTTOM_RIGHT:
         case ABOVE_CHATBOX_RIGHT:
            result.y = -dimension.height;
         case CANVAS_TOP_RIGHT:
         case TOP_RIGHT:
            result.x = -dimension.width;
         case TOP_LEFT:
            break;
         case TOP_CENTER:
            result.x = -dimension.width / 2;
            break;
         default:
            throw new IllegalArgumentException();
      }

      return result;
   }
}
