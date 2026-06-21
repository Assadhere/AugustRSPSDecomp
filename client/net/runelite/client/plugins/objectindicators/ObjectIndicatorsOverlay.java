package net.runelite.client.plugins.objectindicators;

import com.google.common.base.MoreObjects;
import com.google.common.base.Strings;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Shape;
import java.awt.Stroke;
import java.util.Iterator;
import java.util.List;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.DecorativeObject;
import net.runelite.api.GameObject;
import net.runelite.api.GroundObject;
import net.runelite.api.ObjectComposition;
import net.runelite.api.TileObject;
import net.runelite.api.WallObject;
import net.runelite.api.WorldEntity;
import net.runelite.api.WorldView;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayUtil;
import net.runelite.client.ui.overlay.outline.ModelOutlineRenderer;
import net.runelite.client.util.ColorUtil;

class ObjectIndicatorsOverlay extends Overlay {
   private final Client client;
   private final ObjectIndicatorsConfig config;
   private final ObjectIndicatorsPlugin plugin;
   private final ModelOutlineRenderer modelOutlineRenderer;

   @Inject
   private ObjectIndicatorsOverlay(Client client, ObjectIndicatorsConfig config, ObjectIndicatorsPlugin plugin, ModelOutlineRenderer modelOutlineRenderer) {
      this.client = client;
      this.config = config;
      this.plugin = plugin;
      this.modelOutlineRenderer = modelOutlineRenderer;
      this.setPosition(OverlayPosition.DYNAMIC);
      this.setPriority(0.0F);
      this.setLayer(OverlayLayer.ABOVE_SCENE);
   }

   public Dimension render(Graphics2D graphics) {
      List<ColorTileObject> objects = this.plugin.getObjects();
      if (objects.isEmpty()) {
         return null;
      } else {
         WorldView toplevel = this.client.getTopLevelWorldView();
         Stroke stroke = new BasicStroke((float)this.config.borderWidth());
         int defaultFlags = (this.config.highlightHull() ? 1 : 0) | (this.config.highlightOutline() ? 2 : 0) | (this.config.highlightClickbox() ? 4 : 0) | (this.config.highlightTile() ? 8 : 0);
         Iterator var6 = objects.iterator();

         while(true) {
            ColorTileObject obj;
            TileObject object;
            ObjectComposition composition;
            do {
               WorldEntity we;
               do {
                  WorldView wv;
                  do {
                     do {
                        if (!var6.hasNext()) {
                           return null;
                        }

                        obj = (ColorTileObject)var6.next();
                        object = obj.getTileObject();
                        wv = object.getWorldView();
                     } while(wv == null);
                  } while(object.getPlane() != wv.getPlane());

                  we = (WorldEntity)toplevel.worldEntities().byIndex(wv.getId());
               } while(we != null && we.isHiddenForOverlap());

               composition = obj.getComposition();
               if (composition.getImpostorIds() == null) {
                  break;
               }

               composition = composition.getImpostor();
            } while(composition == null || Strings.isNullOrEmpty(composition.getName()) || "null".equals(composition.getName()) || !composition.getName().equals(obj.getName()));

            Color borderColor = obj.getBorderColor();
            if (borderColor == null) {
               borderColor = this.config.markerColor();
            }

            int flags = obj.getHighlightFlags() != 0 ? obj.getHighlightFlags() : defaultFlags;
            if ((flags & 1) != 0) {
               Color fillColor = (Color)MoreObjects.firstNonNull(obj.getFillColor(), new Color(0, 0, 0, 50));
               this.renderConvexHull(graphics, object, borderColor, fillColor, stroke);
            }

            if ((flags & 2) != 0) {
               this.modelOutlineRenderer.drawOutline(object, (int)this.config.borderWidth(), borderColor, this.config.outlineFeather());
            }

            Color fillColor;
            if ((flags & 4) != 0) {
               Shape clickbox = object.getClickbox();
               if (clickbox != null) {
                  fillColor = (Color)MoreObjects.firstNonNull(obj.getFillColor(), ColorUtil.colorWithAlpha(borderColor, borderColor.getAlpha() / 12));
                  OverlayUtil.renderPolygon(graphics, clickbox, borderColor, fillColor, stroke);
               }
            }

            if ((flags & 8) != 0) {
               Polygon tilePoly = object.getCanvasTilePoly();
               if (tilePoly != null) {
                  fillColor = (Color)MoreObjects.firstNonNull(obj.getFillColor(), ColorUtil.colorWithAlpha(borderColor, borderColor.getAlpha() / 12));
                  OverlayUtil.renderPolygon(graphics, tilePoly, borderColor, fillColor, stroke);
               }
            }
         }
      }
   }

   private void renderConvexHull(Graphics2D graphics, TileObject object, Color color, Color fillColor, Stroke stroke) {
      Shape polygon2 = null;
      Object polygon;
      if (object instanceof GameObject) {
         polygon = ((GameObject)object).getConvexHull();
      } else if (object instanceof WallObject) {
         polygon = ((WallObject)object).getConvexHull();
         polygon2 = ((WallObject)object).getConvexHull2();
      } else if (object instanceof DecorativeObject) {
         polygon = ((DecorativeObject)object).getConvexHull();
         polygon2 = ((DecorativeObject)object).getConvexHull2();
      } else if (object instanceof GroundObject) {
         polygon = ((GroundObject)object).getConvexHull();
      } else {
         polygon = object.getCanvasTilePoly();
      }

      if (polygon != null) {
         OverlayUtil.renderPolygon(graphics, (Shape)polygon, color, fillColor, stroke);
      }

      if (polygon2 != null) {
         OverlayUtil.renderPolygon(graphics, polygon2, color, fillColor, stroke);
      }

   }
}
