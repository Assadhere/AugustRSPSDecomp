package net.runelite.api;

import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.geometry.RectangleUnion;
import net.runelite.api.geometry.Shapes;
import net.runelite.api.geometry.SimplePolygon;
import net.runelite.api.model.Jarvis;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import org.jetbrains.annotations.ApiStatus.Internal;

public class Perspective {
   public static final double UNIT = 0.0030679615;
   public static final int LOCAL_COORD_BITS = 7;
   public static final int LOCAL_TILE_SIZE = 128;
   public static final int LOCAL_HALF_TILE_SIZE = 64;
   public static final int SCENE_SIZE = 104;
   public static final int[] SINE = new int[2048];
   public static final int[] COSINE = new int[2048];
   private static final float[] SINF = new float[2048];
   private static final float[] COSF = new float[2048];
   private static final int ESCENE_OFFSET = 40;

   @Nullable
   public static Point localToCanvas(@Nonnull Client client, @Nonnull LocalPoint point, int plane) {
      return localToCanvas(client, point, plane, 0);
   }

   @Nullable
   public static Point localToCanvas(@Nonnull Client client, @Nonnull LocalPoint point, int plane, int heightOffset) {
      if (point.getWorldView() != 0) {
         WorldView wv = client.getTopLevelWorldView();
         WorldEntity we = (WorldEntity)wv.worldEntities().byIndex(point.getWorldView());
         if (we == null) {
            return null;
         } else {
            LocalPoint entityLocation = we.getLocalLocation();
            int height = we.getWorldView().getTileHeight(point.getX(), point.getY(), plane);
            height += wv.getTileHeight(entityLocation.getX(), entityLocation.getY(), wv.getPlane());
            height -= heightOffset;
            WorldView subWv = we.getWorldView();
            Projection projection = subWv.getCanvasProjection();
            if (projection == null) {
               return null;
            } else {
               float[] p = projection.project((float)point.getX(), (float)height, (float)point.getY());
               float x0 = p[0];
               float y0 = p[1];
               float z0 = p[2];
               int scale = client.getScale();
               float pointX = (float)client.getViewportWidth() / 2.0F + x0 * (float)scale / z0;
               float pointY = (float)client.getViewportHeight() / 2.0F + y0 * (float)scale / z0;
               return new Point((int)pointX + client.getViewportXOffset(), (int)pointY + client.getViewportYOffset());
            }
         }
      } else {
         int tileHeight = getTileHeight(client, point, plane);
         return localToCanvas(client, point.getX(), point.getY(), tileHeight - heightOffset);
      }
   }

   public static Point localToCanvas(@Nonnull Client client, int x, int y, int z) {
      return client.isGpu() ? localToCanvasGpu(client, x, y, z) : localToCanvasCpu(client, x, y, z);
   }

   public static Point localToCanvas(@Nonnull Client client, int worldId, int x, int y, int z) {
      if (worldId != 0) {
         WorldView wv = client.getTopLevelWorldView();
         WorldEntity we = (WorldEntity)wv.worldEntities().byIndex(worldId);
         if (we == null) {
            return null;
         } else {
            WorldView subWv = we.getWorldView();
            Projection projection = subWv.getCanvasProjection();
            if (projection == null) {
               return null;
            } else {
               float[] p = projection.project((float)x, (float)z, (float)y);
               float x0 = p[0];
               float y0 = p[1];
               float z0 = p[2];
               int scale = client.getScale();
               float pointX = (float)client.getViewportWidth() / 2.0F + x0 * (float)scale / z0;
               float pointY = (float)client.getViewportHeight() / 2.0F + y0 * (float)scale / z0;
               return new Point((int)pointX + client.getViewportXOffset(), (int)pointY + client.getViewportYOffset());
            }
         }
      } else {
         return client.isGpu() ? localToCanvasGpu(client, x, y, z) : localToCanvasCpu(client, x, y, z);
      }
   }

   private static Point localToCanvasCpu(Client client, int x, int y, int z) {
      if (x >= -5120 && y >= -5120 && x <= 18432 && y <= 18432) {
         x -= client.getCameraX();
         y -= client.getCameraY();
         z -= client.getCameraZ();
         int cameraPitch = client.getCameraPitch();
         int cameraYaw = client.getCameraYaw();
         int pitchSin = SINE[cameraPitch];
         int pitchCos = COSINE[cameraPitch];
         int yawSin = SINE[cameraYaw];
         int yawCos = COSINE[cameraYaw];
         int x1 = x * yawCos + y * yawSin >> 16;
         int y1 = y * yawCos - x * yawSin >> 16;
         int y2 = z * pitchCos - y1 * pitchSin >> 16;
         int z1 = y1 * pitchCos + z * pitchSin >> 16;
         if (z1 >= 50) {
            int scale = client.getScale();
            int pointX = client.getViewportWidth() / 2 + x1 * scale / z1;
            int pointY = client.getViewportHeight() / 2 + y2 * scale / z1;
            return new Point(pointX + client.getViewportXOffset(), pointY + client.getViewportYOffset());
         }
      }

      return null;
   }

   private static Point localToCanvasGpu(Client client, int x, int y, int z) {
      if (x >= -5120 && y >= -5120 && x <= 18432 && y <= 18432) {
         double cameraPitch = client.getCameraFpPitch();
         double cameraYaw = client.getCameraFpYaw();
         float fx = (float)x - (float)client.getCameraFpX();
         float fy = (float)y - (float)client.getCameraFpY();
         float fz = (float)z - (float)client.getCameraFpZ();
         float pitchSin = (float)Math.sin(cameraPitch);
         float pitchCos = (float)Math.cos(cameraPitch);
         float yawSin = (float)Math.sin(cameraYaw);
         float yawCos = (float)Math.cos(cameraYaw);
         float x1 = fx * yawCos + fy * yawSin;
         float y1 = fy * yawCos - fx * yawSin;
         float y2 = fz * pitchCos - y1 * pitchSin;
         float z1 = y1 * pitchCos + fz * pitchSin;
         if (z1 >= 50.0F) {
            int scale = client.getScale();
            int pointX = Math.round((float)client.getViewportWidth() / 2.0F + x1 * (float)scale / z1);
            int pointY = Math.round((float)client.getViewportHeight() / 2.0F + y2 * (float)scale / z1);
            return new Point(pointX + client.getViewportXOffset(), pointY + client.getViewportYOffset());
         }
      }

      return null;
   }

   /** @deprecated */
   @Deprecated
   public static void modelToCanvas(Client client, int end, int x3dCenter, int y3dCenter, int z3dCenter, int rotate, float[] x3d, float[] y3d, float[] z3d, int[] x2d, int[] y2d) {
      modelToCanvas(client, client.getTopLevelWorldView(), end, x3dCenter, y3dCenter, z3dCenter, rotate, x3d, y3d, z3d, x2d, y2d);
   }

   public static void modelToCanvas(Client client, WorldView wv, int end, int x3dCenter, int y3dCenter, int z3dCenter, int rotate, float[] x3d, float[] y3d, float[] z3d, int[] x2d, int[] y2d) {
      if (!wv.isTopLevel()) {
         modelToCanvasProjection(client, wv, end, x3dCenter, y3dCenter, z3dCenter, rotate, x3d, y3d, z3d, x2d, y2d);
      } else if (client.isGpu()) {
         modelToCanvasGpu(client, end, x3dCenter, y3dCenter, z3dCenter, rotate, x3d, y3d, z3d, x2d, y2d);
      } else {
         modelToCanvasCpu(client, end, x3dCenter, y3dCenter, z3dCenter, rotate, x3d, y3d, z3d, x2d, y2d);
      }

   }

   private static void modelToCanvasProjection(Client client, WorldView wv, int end, int x3dCenter, int y3dCenter, int z3dCenter, int rotate, float[] x3d, float[] y3d, float[] z3d, int[] x2d, int[] y2d) {
      float rotateSin = (float)SINE[rotate] / 65536.0F;
      float rotateCos = (float)COSINE[rotate] / 65536.0F;
      float viewportXMiddle = (float)client.getViewportWidth() / 2.0F;
      float viewportYMiddle = (float)client.getViewportHeight() / 2.0F;
      float viewportXOffset = (float)client.getViewportXOffset();
      float viewportYOffset = (float)client.getViewportYOffset();
      float zoom3d = (float)client.getScale();
      Projection proj = wv.getCanvasProjection();
      if (proj == null) {
         Arrays.fill(x2d, Integer.MIN_VALUE);
         Arrays.fill(y2d, Integer.MIN_VALUE);
      } else {
         for(int i = 0; i < end; ++i) {
            float x = x3d[i];
            float y = y3d[i];
            float z = z3d[i];
            if (rotate != 0) {
               float x0 = x;
               x = x0 * rotateCos + y * rotateSin;
               y = y * rotateCos - x0 * rotateSin;
            }

            x += (float)x3dCenter;
            y += (float)y3dCenter;
            z += (float)z3dCenter;
            float[] p = proj.project(x, z, y);
            float x1 = p[0];
            float y1 = p[1];
            float z1 = p[2];
            int viewX;
            int viewY;
            if (z1 < 50.0F) {
               viewX = Integer.MIN_VALUE;
               viewY = Integer.MIN_VALUE;
            } else {
               viewX = Math.round(viewportXMiddle + x1 * zoom3d / z1 + viewportXOffset);
               viewY = Math.round(viewportYMiddle + y1 * zoom3d / z1 + viewportYOffset);
            }

            x2d[i] = viewX;
            y2d[i] = viewY;
         }

      }
   }

   private static void modelToCanvasGpu(Client client, int end, int x3dCenter, int y3dCenter, int z3dCenter, int rotate, float[] x3d, float[] y3d, float[] z3d, int[] x2d, int[] y2d) {
      double cameraPitch = client.getCameraFpPitch();
      double cameraYaw = client.getCameraFpYaw();
      float pitchSin = (float)Math.sin(cameraPitch);
      float pitchCos = (float)Math.cos(cameraPitch);
      float yawSin = (float)Math.sin(cameraYaw);
      float yawCos = (float)Math.cos(cameraYaw);
      float rotateSin = (float)SINE[rotate] / 65536.0F;
      float rotateCos = (float)COSINE[rotate] / 65536.0F;
      float cx = (float)x3dCenter - (float)client.getCameraFpX();
      float cy = (float)y3dCenter - (float)client.getCameraFpY();
      float cz = (float)z3dCenter - (float)client.getCameraFpZ();
      float viewportXMiddle = (float)client.getViewportWidth() / 2.0F;
      float viewportYMiddle = (float)client.getViewportHeight() / 2.0F;
      float viewportXOffset = (float)client.getViewportXOffset();
      float viewportYOffset = (float)client.getViewportYOffset();
      float zoom3d = (float)client.getScale();

      for(int i = 0; i < end; ++i) {
         float x = x3d[i];
         float y = y3d[i];
         float z = z3d[i];
         float x1;
         if (rotate != 0) {
            x1 = x;
            x = x1 * rotateCos + y * rotateSin;
            y = y * rotateCos - x1 * rotateSin;
         }

         x += cx;
         y += cy;
         z += cz;
         x1 = x * yawCos + y * yawSin;
         float y1 = y * yawCos - x * yawSin;
         float y2 = z * pitchCos - y1 * pitchSin;
         float z1 = y1 * pitchCos + z * pitchSin;
         int viewX;
         int viewY;
         if (z1 < 50.0F) {
            viewX = Integer.MIN_VALUE;
            viewY = Integer.MIN_VALUE;
         } else {
            viewX = Math.round(viewportXMiddle + x1 * zoom3d / z1 + viewportXOffset);
            viewY = Math.round(viewportYMiddle + y2 * zoom3d / z1 + viewportYOffset);
         }

         x2d[i] = viewX;
         y2d[i] = viewY;
      }

   }

   private static void modelToCanvasCpu(Client client, int end, int x3dCenter, int y3dCenter, int z3dCenter, int rotate, float[] x3d, float[] y3d, float[] z3d, int[] x2d, int[] y2d) {
      int cameraPitch = client.getCameraPitch();
      int cameraYaw = client.getCameraYaw();
      float pitchSin = SINF[cameraPitch];
      float pitchCos = COSF[cameraPitch];
      float yawSin = SINF[cameraYaw];
      float yawCos = COSF[cameraYaw];
      float rotateSin = SINF[rotate];
      float rotateCos = COSF[rotate];
      int cx = x3dCenter - client.getCameraX();
      int cy = y3dCenter - client.getCameraY();
      int cz = z3dCenter - client.getCameraZ();
      int viewportXMiddle = client.getViewportWidth() / 2;
      int viewportYMiddle = client.getViewportHeight() / 2;
      int viewportXOffset = client.getViewportXOffset();
      int viewportYOffset = client.getViewportYOffset();
      int zoom3d = client.getScale();

      for(int i = 0; i < end; ++i) {
         float x = x3d[i];
         float y = y3d[i];
         float z = z3d[i];
         float x1;
         if (rotate != 0) {
            x1 = x;
            x = x1 * rotateCos + y * rotateSin;
            y = y * rotateCos - x1 * rotateSin;
         }

         x += (float)cx;
         y += (float)cy;
         z += (float)cz;
         x1 = x * yawCos + y * yawSin;
         float y1 = y * yawCos - x * yawSin;
         float y2 = z * pitchCos - y1 * pitchSin;
         float z1 = y1 * pitchCos + z * pitchSin;
         int viewX;
         int viewY;
         if (z1 < 50.0F) {
            viewX = Integer.MIN_VALUE;
            viewY = Integer.MIN_VALUE;
         } else {
            viewX = (int)((float)viewportXMiddle + x1 * (float)zoom3d / z1) + viewportXOffset;
            viewY = (int)((float)viewportYMiddle + y2 * (float)zoom3d / z1) + viewportYOffset;
         }

         x2d[i] = viewX;
         y2d[i] = viewY;
      }

   }

   @Nullable
   public static Point localToMinimap(@Nonnull Client client, @Nonnull LocalPoint point) {
      int r = true;
      double s = 4.0 / client.getMinimapZoom();
      return localToMinimap(client, point, (int)(2560.0 * s));
   }

   @Nullable
   public static Point localToMinimap(@Nonnull Client client, @Nonnull LocalPoint point, int distance) {
      if (point.getWorldView() != 0) {
         WorldView toplevel = client.getTopLevelWorldView();
         WorldEntity we = (WorldEntity)toplevel.worldEntities().byIndex(point.getWorldView());
         if (we == null) {
            return null;
         }

         point = we.transformToMainWorld(point);
      }

      CameraFocusableEntity cameraFocus = client.getCameraFocusEntity();
      LocalPoint cameraFocusPoint = cameraFocus.getCameraFocus();
      if (cameraFocusPoint.getWorldView() != 0) {
         WorldView toplevel = client.getTopLevelWorldView();
         WorldView wv = cameraFocus.getWorldView();
         WorldEntity we = (WorldEntity)toplevel.worldEntities().byIndex(wv.getId());
         if (we != null) {
            cameraFocusPoint = we.transformToMainWorld(cameraFocusPoint);
         }
      }

      int dx = point.getX() - cameraFocusPoint.getX();
      int dy = point.getY() - cameraFocusPoint.getY();
      if (dx * dx + dy * dy >= distance * distance) {
         return null;
      } else {
         Widget minimapDrawWidget;
         if (client.isResized()) {
            if (client.getVarbitValue(4607) == 1) {
               minimapDrawWidget = client.getWidget(WidgetInfo.RESIZABLE_MINIMAP_DRAW_AREA);
            } else {
               minimapDrawWidget = client.getWidget(WidgetInfo.RESIZABLE_MINIMAP_STONES_DRAW_AREA);
            }
         } else {
            minimapDrawWidget = client.getWidget(WidgetInfo.FIXED_VIEWPORT_MINIMAP_DRAW_AREA);
         }

         if (minimapDrawWidget != null && !minimapDrawWidget.isHidden()) {
            double zoom = client.getMinimapZoom() / 128.0;
            int x = (int)((double)dx * zoom);
            int y = (int)((double)dy * zoom);
            int angle = client.getCameraYawTarget() & 2047;
            int sin = SINE[angle];
            int cos = COSINE[angle];
            int rx = cos * x + sin * y >> 16;
            int ry = sin * x - cos * y >> 16;
            Point loc = minimapDrawWidget.getCanvasLocation();
            int miniMapX = loc.getX() + minimapDrawWidget.getWidth() / 2 + rx;
            int miniMapY = loc.getY() + minimapDrawWidget.getHeight() / 2 + ry;
            return new Point(miniMapX, miniMapY);
         } else {
            return null;
         }
      }
   }

   public static int getTileHeight(@Nonnull Client client, @Nonnull LocalPoint point, int plane) {
      int sceneX = point.getSceneX();
      int sceneY = point.getSceneY();
      WorldView wv = client.getWorldView(point.getWorldView());
      if (wv != null && sceneX >= 0 && sceneY >= 0 && sceneX < wv.getSizeX() && sceneY < wv.getSizeY()) {
         byte[][][] tileSettings = wv.getTileSettings();
         int[][][] tileHeights = wv.getTileHeights();
         int z1 = plane;
         if (plane < 3 && (tileSettings[1][sceneX][sceneY] & 2) == 2) {
            z1 = plane + 1;
         }

         int x = point.getX() & 127;
         int y = point.getY() & 127;
         int var8 = x * tileHeights[z1][sceneX + 1][sceneY] + (128 - x) * tileHeights[z1][sceneX][sceneY] >> 7;
         int var9 = tileHeights[z1][sceneX][sceneY + 1] * (128 - x) + x * tileHeights[z1][sceneX + 1][sceneY + 1] >> 7;
         return (128 - y) * var8 + y * var9 >> 7;
      } else {
         return 0;
      }
   }

   public static int getFootprintTileHeight(@Nonnull Client client, @Nonnull LocalPoint p, int level, int footprintSize) {
      int x = p.getX();
      int z = p.getY();
      int halfFootprint = footprintSize / 2;
      int lx = x - halfFootprint;
      int lz = z - halfFootprint;
      int ux = x + halfFootprint;
      int uz = z + halfFootprint;
      int lsx = (lx >> 7) + 1;
      int lsz = (lz >> 7) + 1;
      int usx = ux >> 7;
      int usz = uz >> 7;
      int h = Integer.MAX_VALUE;

      for(int tx = lsx; tx <= usx; ++tx) {
         for(int tz = lsz; tz <= usz; ++tz) {
            h = Math.min(h, getTileHeight(client, new LocalPoint(tx << 7, tz << 7, p.getWorldView()), level));
         }
      }

      h = Math.min(h, getTileHeight(client, new LocalPoint(x, z, p.getWorldView()), level));
      h = Math.min(h, getTileHeight(client, new LocalPoint(x - halfFootprint, z - halfFootprint, p.getWorldView()), level));
      h = Math.min(h, getTileHeight(client, new LocalPoint(x - halfFootprint, z + halfFootprint, p.getWorldView()), level));
      h = Math.min(h, getTileHeight(client, new LocalPoint(x + halfFootprint, z - halfFootprint, p.getWorldView()), level));
      h = Math.min(h, getTileHeight(client, new LocalPoint(x + halfFootprint, z + halfFootprint, p.getWorldView()), level));
      return h;
   }

   public static Polygon getCanvasTilePoly(@Nonnull Client client, @Nonnull LocalPoint localLocation) {
      return getCanvasTileAreaPoly(client, localLocation, 1);
   }

   public static Polygon getCanvasTilePoly(@Nonnull Client client, @Nonnull LocalPoint localLocation, int zOffset) {
      return getCanvasTileAreaPoly(client, localLocation, 1, 1, -1, zOffset);
   }

   public static Polygon getCanvasTileAreaPoly(@Nonnull Client client, @Nonnull LocalPoint localLocation, int size) {
      return getCanvasTileAreaPoly(client, localLocation, size, size, -1, 0);
   }

   public static Polygon getCanvasTileAreaPoly(@Nonnull Client client, @Nonnull LocalPoint localLocation, int sizeX, int sizeY, int level, int heightOffset) {
      WorldView wv = client.getWorldView(localLocation.getWorldView());
      if (wv == null) {
         return null;
      } else {
         int offset = wv.isTopLevel() ? 40 : 0;
         int escene = offset << 1;
         int msx = localLocation.getSceneX() + offset;
         int msy = localLocation.getSceneY() + offset;
         if (msx >= 0 && msy >= 0 && msx < wv.getSizeX() + escene && msy < wv.getSizeY() + escene) {
            if (level == -1) {
               level = wv.getPlane();
            }

            Scene scene = wv.getScene();
            byte[][][] tileSettings = scene.getExtendedTileSettings();
            int mapLevel = level;
            if (level < 3 && (tileSettings[1][msx][msy] & 2) == 2) {
               mapLevel = level + 1;
            }

            int swX = localLocation.getX() - sizeX * 128 / 2;
            int swY = localLocation.getY() - sizeY * 128 / 2;
            int neX = localLocation.getX() + sizeX * 128 / 2;
            int neY = localLocation.getY() + sizeY * 128 / 2;
            int seX = swX;
            int seY = neY;
            int nwX = neX;
            int nwY = swY;
            int swHeight = wv.getTileHeight(swX, swY, mapLevel) - heightOffset;
            int nwHeight = wv.getTileHeight(nwX, nwY, mapLevel) - heightOffset;
            int neHeight = wv.getTileHeight(neX, neY, mapLevel) - heightOffset;
            int seHeight = wv.getTileHeight(seX, seY, mapLevel) - heightOffset;
            Point p1 = localToCanvas(client, wv.getId(), swX, swY, swHeight);
            Point p2 = localToCanvas(client, wv.getId(), nwX, nwY, nwHeight);
            Point p3 = localToCanvas(client, wv.getId(), neX, neY, neHeight);
            Point p4 = localToCanvas(client, wv.getId(), seX, seY, seHeight);
            if (p1 != null && p2 != null && p3 != null && p4 != null) {
               Polygon poly = new Polygon();
               poly.addPoint(p1.getX(), p1.getY());
               poly.addPoint(p2.getX(), p2.getY());
               poly.addPoint(p3.getX(), p3.getY());
               poly.addPoint(p4.getX(), p4.getY());
               return poly;
            } else {
               return null;
            }
         } else {
            return null;
         }
      }
   }

   public static Point getCanvasTextLocation(@Nonnull Client client, @Nonnull Graphics2D graphics, @Nonnull LocalPoint localLocation, @Nullable String text, int zOffset) {
      if (text == null) {
         return null;
      } else {
         WorldView wv = client.getWorldView(localLocation.getWorldView());
         if (wv == null) {
            return null;
         } else {
            int plane = wv.getPlane();
            Point p = localToCanvas(client, localLocation, plane, zOffset);
            if (p == null) {
               return null;
            } else {
               FontMetrics fm = graphics.getFontMetrics();
               Rectangle2D bounds = fm.getStringBounds(text, graphics);
               int xOffset = p.getX() - (int)(bounds.getWidth() / 2.0);
               return new Point(xOffset, p.getY());
            }
         }
      }
   }

   public static Point getCanvasImageLocation(@Nonnull Client client, @Nonnull LocalPoint localLocation, @Nonnull BufferedImage image, int zOffset) {
      WorldView wv = client.getWorldView(localLocation.getWorldView());
      if (wv == null) {
         return null;
      } else {
         int plane = wv.getPlane();
         Point p = localToCanvas(client, localLocation, plane, zOffset);
         if (p == null) {
            return null;
         } else {
            int xOffset = p.getX() - image.getWidth() / 2;
            int yOffset = p.getY() - image.getHeight() / 2;
            return new Point(xOffset, yOffset);
         }
      }
   }

   public static Point getMiniMapImageLocation(@Nonnull Client client, @Nonnull LocalPoint localLocation, @Nonnull BufferedImage image) {
      Point p = localToMinimap(client, localLocation);
      if (p == null) {
         return null;
      } else {
         int xOffset = p.getX() - image.getWidth() / 2;
         int yOffset = p.getY() - image.getHeight() / 2;
         return new Point(xOffset, yOffset);
      }
   }

   public static Point getCanvasSpriteLocation(@Nonnull Client client, @Nonnull LocalPoint localLocation, @Nonnull SpritePixels sprite, int zOffset) {
      WorldView wv = client.getWorldView(localLocation.getWorldView());
      if (wv == null) {
         return null;
      } else {
         int plane = wv.getPlane();
         Point p = localToCanvas(client, localLocation, plane, zOffset);
         if (p == null) {
            return null;
         } else {
            int xOffset = p.getX() - sprite.getWidth() / 2;
            int yOffset = p.getY() - sprite.getHeight() / 2;
            return new Point(xOffset, yOffset);
         }
      }
   }

   @Nullable
   @Internal
   public static Shape getClickbox(@Nonnull Client client, WorldView wv, Model model, int orientation, int x, int y, int z) {
      if (model == null) {
         return null;
      } else {
         SimplePolygon bounds = calculateAABB(client, wv, model, orientation, x, y, z);
         if (bounds == null) {
            return null;
         } else if (model.useBoundingBox()) {
            return bounds;
         } else {
            Shapes<SimplePolygon> bounds2d = calculate2DBounds(client, wv, model, orientation, x, y, z);
            if (bounds2d == null) {
               return null;
            } else {
               Iterator var9 = bounds2d.getShapes().iterator();

               while(var9.hasNext()) {
                  SimplePolygon poly = (SimplePolygon)var9.next();
                  poly.intersectWithConvex(bounds);
               }

               return bounds2d;
            }
         }
      }
   }

   private static SimplePolygon calculateAABB(Client client, WorldView wv, Model m, int jauOrient, int x, int y, int z) {
      AABB aabb = m.getAABB(jauOrient);
      int x1 = aabb.getCenterX();
      int y1 = aabb.getCenterZ();
      int z1 = aabb.getCenterY();
      int ex = aabb.getExtremeX();
      int ey = aabb.getExtremeZ();
      int ez = aabb.getExtremeY();
      int x2 = x1 + ex;
      int y2 = y1 + ey;
      int z2 = z1 + ez;
      x1 -= ex;
      y1 -= ey;
      z1 -= ez;
      float[] xa = new float[]{(float)x1, (float)x2, (float)x1, (float)x2, (float)x1, (float)x2, (float)x1, (float)x2};
      float[] ya = new float[]{(float)y1, (float)y1, (float)y2, (float)y2, (float)y1, (float)y1, (float)y2, (float)y2};
      float[] za = new float[]{(float)z1, (float)z1, (float)z1, (float)z1, (float)z2, (float)z2, (float)z2, (float)z2};
      int[] x2d = new int[8];
      int[] y2d = new int[8];
      modelToCanvas(client, wv, 8, x, y, z, 0, xa, ya, za, x2d, y2d);
      return Jarvis.convexHull(x2d, y2d);
   }

   private static Shapes<SimplePolygon> calculate2DBounds(Client client, WorldView wv, Model m, int jauOrient, int x, int y, int z) {
      int[] x2d = new int[m.getVerticesCount()];
      int[] y2d = new int[m.getVerticesCount()];
      int[] faceColors3 = m.getFaceColors3();
      modelToCanvas(client, wv, m.getVerticesCount(), x, y, z, jauOrient, m.getVerticesX(), m.getVerticesZ(), m.getVerticesY(), x2d, y2d);
      int radius = true;
      int[][] tris = new int[][]{m.getFaceIndices1(), m.getFaceIndices2(), m.getFaceIndices3()};
      int vpX1 = client.getViewportXOffset();
      int vpY1 = client.getViewportXOffset();
      int vpX2 = vpX1 + client.getViewportWidth();
      int vpY2 = vpY1 + client.getViewportHeight();
      List<RectangleUnion.Rectangle> rects = new ArrayList(m.getFaceCount());

      label56:
      for(int tri = 0; tri < m.getFaceCount(); ++tri) {
         if (faceColors3[tri] != -2) {
            int minX = Integer.MAX_VALUE;
            int minY = Integer.MAX_VALUE;
            int maxX = Integer.MIN_VALUE;
            int maxY = Integer.MIN_VALUE;
            int[][] var22 = tris;
            int var23 = tris.length;

            for(int var24 = 0; var24 < var23; ++var24) {
               int[] vertex = var22[var24];
               int idx = vertex[tri];
               int xs = x2d[idx];
               int ys = y2d[idx];
               if (xs == Integer.MIN_VALUE || ys == Integer.MIN_VALUE) {
                  continue label56;
               }

               if (xs < minX) {
                  minX = xs;
               }

               if (xs > maxX) {
                  maxX = xs;
               }

               if (ys < minY) {
                  minY = ys;
               }

               if (ys > maxY) {
                  maxY = ys;
               }
            }

            minX -= 5;
            minY -= 5;
            maxX += 5;
            maxY += 5;
            if (vpX1 <= maxX && vpX2 >= minX && vpY1 <= maxY && vpY2 >= minY) {
               RectangleUnion.Rectangle r = new RectangleUnion.Rectangle(minX, minY, maxX, maxY);
               rects.add(r);
            }
         }
      }

      return RectangleUnion.union(rects);
   }

   public static Point getCanvasTextMiniMapLocation(@Nonnull Client client, @Nonnull Graphics2D graphics, @Nonnull LocalPoint localLocation, @Nonnull String text) {
      Point p = localToMinimap(client, localLocation);
      if (p == null) {
         return null;
      } else {
         FontMetrics fm = graphics.getFontMetrics();
         Rectangle2D bounds = fm.getStringBounds(text, graphics);
         int xOffset = p.getX() - (int)(bounds.getWidth() / 2.0);
         int yOffset = p.getY() - (int)(bounds.getHeight() / 2.0) + fm.getAscent();
         return new Point(xOffset, yOffset);
      }
   }

   static {
      for(int i = 0; i < 2048; ++i) {
         double s = Math.sin((double)i * 0.0030679615);
         double c = Math.cos((double)i * 0.0030679615);
         SINF[i] = (float)s;
         COSF[i] = (float)c;
         SINE[i] = (int)(65536.0 * s);
         COSINE[i] = (int)(65536.0 * c);
      }

   }
}
