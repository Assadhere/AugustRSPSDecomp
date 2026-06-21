package net.runelite.client.plugins.gpu;

import java.nio.IntBuffer;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import net.runelite.api.DecorativeObject;
import net.runelite.api.DynamicObject;
import net.runelite.api.GameObject;
import net.runelite.api.GroundObject;
import net.runelite.api.Model;
import net.runelite.api.Perspective;
import net.runelite.api.Point;
import net.runelite.api.Renderable;
import net.runelite.api.Scene;
import net.runelite.api.SceneTileModel;
import net.runelite.api.SceneTilePaint;
import net.runelite.api.Tile;
import net.runelite.api.WallObject;
import net.runelite.client.callback.RenderCallbackManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class SceneUploader {
   private static final Logger log = LoggerFactory.getLogger(SceneUploader.class);
   private static final float[] modelLocalX;
   private static final float[] modelLocalY;
   private static final float[] modelLocalZ;
   private final int[] modelLocalXI;
   private final int[] modelLocalYI;
   private final int[] modelLocalZI;
   private final RenderCallbackManager renderCallbackManager;
   private int basex;
   private int basez;
   private int rid;
   private int level;
   float u0;
   float u1;
   float u2;
   float v0;
   float v1;
   float v2;

   SceneUploader(RenderCallbackManager renderCallbackManager) {
      this.renderCallbackManager = renderCallbackManager;
      this.modelLocalXI = new int[6500];
      this.modelLocalYI = new int[6500];
      this.modelLocalZI = new int[6500];
   }

   void zoneSize(Scene scene, Zone zone, int mzx, int mzz) {
      Tile[][][] tiles = scene.getExtendedTiles();

      for(int z = 3; z >= 0; --z) {
         for(int xoff = 0; xoff < 8; ++xoff) {
            for(int zoff = 0; zoff < 8; ++zoff) {
               Tile t = tiles[z][(mzx << 3) + xoff][(mzz << 3) + zoff];
               if (t != null) {
                  this.zoneSize(zone, t);
               }
            }
         }
      }

   }

   void uploadZone(Scene scene, Zone zone, int mzx, int mzz) {
      int[][][] roofs = scene.getRoofs();
      Set<Integer> roofIds = new HashSet();
      GpuIntBuffer vb = zone.vboO != null ? new GpuIntBuffer(zone.vboO.vb) : null;
      GpuIntBuffer ab = zone.vboA != null ? new GpuIntBuffer(zone.vboA.vb) : null;

      int level;
      int pos;
      for(level = 0; level <= 3; ++level) {
         for(pos = 0; pos < 8; ++pos) {
            for(int zoff = 0; zoff < 8; ++zoff) {
               int rid = roofs[level][(mzx << 3) + pos][(mzz << 3) + zoff];
               if (rid > 0) {
                  roofIds.add(rid);
               }
            }
         }
      }

      zone.rids = new int[4][roofIds.size()];
      zone.roofStart = new int[4][roofIds.size()];
      zone.roofEnd = new int[4][roofIds.size()];

      for(level = 0; level <= 3; ++level) {
         this.level = level;
         if (level == 0) {
            this.uploadZoneLevel(scene, zone, mzx, mzz, level, false, roofIds, vb, ab);
            this.uploadZoneLevel(scene, zone, mzx, mzz, level, true, roofIds, vb, ab);
            this.uploadZoneLevel(scene, zone, mzx, mzz, 1, true, roofIds, vb, ab);
            this.uploadZoneLevel(scene, zone, mzx, mzz, 2, true, roofIds, vb, ab);
            this.uploadZoneLevel(scene, zone, mzx, mzz, 3, true, roofIds, vb, ab);
         } else {
            this.uploadZoneLevel(scene, zone, mzx, mzz, level, false, roofIds, vb, ab);
         }

         if (zone.vboO != null) {
            pos = zone.vboO.vb.position();
            zone.levelOffsets[level] = pos;
         }
      }

   }

   private void uploadZoneLevel(Scene scene, Zone zone, int mzx, int mzz, int level, boolean visbelow, Set<Integer> roofIds, GpuIntBuffer vb, GpuIntBuffer ab) {
      int ridx = 0;
      Iterator var11 = roofIds.iterator();

      while(var11.hasNext()) {
         int id = (Integer)var11.next();
         int pos = zone.vboO != null ? zone.vboO.vb.position() : 0;
         this.uploadZoneLevelRoof(scene, zone, mzx, mzz, level, id, visbelow, vb, ab);
         int endpos = zone.vboO != null ? zone.vboO.vb.position() : 0;
         if (endpos > pos) {
            zone.rids[level][ridx] = id;
            zone.roofStart[level][ridx] = pos;
            zone.roofEnd[level][ridx] = endpos;
            ++ridx;
         }
      }

      this.uploadZoneLevelRoof(scene, zone, mzx, mzz, level, 0, visbelow, vb, ab);
   }

   private void uploadZoneLevelRoof(Scene scene, Zone zone, int mzx, int mzz, int level, int roofId, boolean visbelow, GpuIntBuffer vb, GpuIntBuffer ab) {
      byte[][][] settings = scene.getExtendedTileSettings();
      int[][][] roofs = scene.getRoofs();
      Tile[][][] tiles = scene.getExtendedTiles();
      int offset = scene.getWorldViewId() == 0 ? 5 : 0;
      this.basex = mzx - offset << 10;
      this.basez = mzz - offset << 10;

      for(int xoff = 0; xoff < 8; ++xoff) {
         for(int zoff = 0; zoff < 8; ++zoff) {
            int msx = (mzx << 3) + xoff;
            int msz = (mzz << 3) + zoff;
            boolean isbridge = (settings[1][msx][msz] & 2) != 0;
            int maplevel = level;
            if (isbridge) {
               ++maplevel;
            }

            boolean isvisbelow = maplevel <= 3 && (settings[maplevel][msx][msz] & 8) != 0;
            if (isvisbelow == visbelow) {
               int rid;
               if (!isvisbelow && maplevel != 0) {
                  rid = roofs[maplevel - 1][msx][msz];
               } else {
                  rid = 0;
               }

               if (rid == roofId) {
                  Tile t = tiles[level][msx][msz];
                  if (t != null) {
                     this.rid = rid;
                     this.uploadZoneTile(scene, zone, t, vb, ab);
                  }
               }
            }
         }
      }

   }

   private void zoneSize(Zone z, Tile t) {
      SceneTilePaint paint = t.getSceneTilePaint();
      if (paint != null) {
         z.sizeO += 2;
      }

      SceneTileModel model = t.getSceneTileModel();
      if (model != null) {
         z.sizeO += model.getFaceX().length;
      }

      WallObject wallObject = t.getWallObject();
      if (wallObject != null) {
         this.zoneRenderableSize(z, wallObject.getRenderable1());
         this.zoneRenderableSize(z, wallObject.getRenderable2());
      }

      DecorativeObject decorativeObject = t.getDecorativeObject();
      if (decorativeObject != null) {
         this.zoneRenderableSize(z, decorativeObject.getRenderable());
         this.zoneRenderableSize(z, decorativeObject.getRenderable2());
      }

      GroundObject groundObject = t.getGroundObject();
      if (groundObject != null) {
         this.zoneRenderableSize(z, groundObject.getRenderable());
      }

      GameObject[] gameObjects = t.getGameObjects();
      GameObject[] var9 = gameObjects;
      int var10 = gameObjects.length;

      for(int var11 = 0; var11 < var10; ++var11) {
         GameObject gameObject = var9[var11];
         if (gameObject != null && gameObject.getSceneMinLocation().equals(t.getSceneLocation())) {
            Renderable renderable = gameObject.getRenderable();
            this.zoneRenderableSize(z, renderable);
         }
      }

      Tile bridge = t.getBridge();
      if (bridge != null) {
         this.zoneSize(z, bridge);
      }

   }

   private int uploadZoneTile(Scene scene, Zone zone, Tile t, GpuIntBuffer vertexBuffer, GpuIntBuffer ab) {
      int len = 0;
      boolean drawTile = this.renderCallbackManager.drawTile(scene, t);
      SceneTilePaint paint = t.getSceneTilePaint();
      if (paint != null && drawTile) {
         Point tilePoint = t.getSceneLocation();
         len = this.upload(scene, paint, t.getRenderLevel(), tilePoint.getX(), tilePoint.getY(), vertexBuffer, tilePoint.getX() * 128 - this.basex, tilePoint.getY() * 128 - this.basez);
      }

      SceneTileModel model = t.getSceneTileModel();
      if (model != null && drawTile) {
         Point tilePoint = t.getSceneLocation();
         len += this.upload(model, tilePoint.getX() << 7, tilePoint.getY() << 7, vertexBuffer);
      }

      WallObject wallObject = t.getWallObject();
      Renderable renderable;
      if (wallObject != null && this.renderCallbackManager.drawObject(scene, wallObject)) {
         Renderable renderable1 = wallObject.getRenderable1();
         this.uploadZoneRenderable(renderable1, zone, 0, wallObject.getX(), wallObject.getZ(), wallObject.getY(), -1, -1, -1, -1, wallObject.getId(), vertexBuffer, ab);
         renderable = wallObject.getRenderable2();
         this.uploadZoneRenderable(renderable, zone, 0, wallObject.getX(), wallObject.getZ(), wallObject.getY(), -1, -1, -1, -1, wallObject.getId(), vertexBuffer, ab);
      }

      DecorativeObject decorativeObject = t.getDecorativeObject();
      Renderable renderable;
      if (decorativeObject != null && this.renderCallbackManager.drawObject(scene, decorativeObject)) {
         renderable = decorativeObject.getRenderable();
         this.uploadZoneRenderable(renderable, zone, 0, decorativeObject.getX() + decorativeObject.getXOffset(), decorativeObject.getZ(), decorativeObject.getY() + decorativeObject.getYOffset(), -1, -1, -1, -1, decorativeObject.getId(), vertexBuffer, ab);
         renderable = decorativeObject.getRenderable2();
         this.uploadZoneRenderable(renderable, zone, 0, decorativeObject.getX() + decorativeObject.getXOffset2(), decorativeObject.getZ(), decorativeObject.getY() + decorativeObject.getYOffset2(), -1, -1, -1, -1, decorativeObject.getId(), vertexBuffer, ab);
      }

      GroundObject groundObject = t.getGroundObject();
      if (groundObject != null && this.renderCallbackManager.drawObject(scene, groundObject)) {
         renderable = groundObject.getRenderable();
         this.uploadZoneRenderable(renderable, zone, 0, groundObject.getX(), groundObject.getZ(), groundObject.getY(), -1, -1, -1, -1, groundObject.getId(), vertexBuffer, ab);
      }

      GameObject[] gameObjects = t.getGameObjects();
      GameObject[] var14 = gameObjects;
      int var15 = gameObjects.length;

      for(int var16 = 0; var16 < var15; ++var16) {
         GameObject gameObject = var14[var16];
         if (gameObject != null) {
            Point min = gameObject.getSceneMinLocation();
            Point max = gameObject.getSceneMaxLocation();
            if (min.equals(t.getSceneLocation()) && this.renderCallbackManager.drawObject(scene, gameObject)) {
               Renderable renderable = gameObject.getRenderable();
               this.uploadZoneRenderable(renderable, zone, gameObject.getModelOrientation(), gameObject.getX(), gameObject.getZ(), gameObject.getY(), min.getX(), min.getY(), max.getX(), max.getY(), gameObject.getId(), vertexBuffer, ab);
            }
         }
      }

      Tile bridge = t.getBridge();
      if (bridge != null) {
         len += this.uploadZoneTile(scene, zone, bridge, vertexBuffer, ab);
      }

      return len;
   }

   private void zoneRenderableSize(Zone z, Renderable r) {
      Model m = null;
      if (r instanceof Model) {
         m = (Model)r;
      } else if (r instanceof DynamicObject) {
         m = ((DynamicObject)r).getModelZbuf();
      }

      if (m != null) {
         byte[] transparencies = m.getFaceTransparencies();
         int faceCount = m.getFaceCount();
         if (transparencies != null) {
            for(int face = 0; face < faceCount; ++face) {
               boolean alpha = transparencies[face] != 0;
               if (alpha) {
                  ++z.sizeA;
               } else {
                  ++z.sizeO;
               }
            }

         } else {
            z.sizeO += faceCount;
         }
      }
   }

   private void uploadZoneRenderable(Renderable r, Zone zone, int orient, int x, int y, int z, int lx, int lz, int ux, int uz, int id, GpuIntBuffer vb, GpuIntBuffer ab) {
      int pos = zone.vboA != null ? zone.vboA.vb.position() : 0;
      Model model = null;
      if (r instanceof Model) {
         model = (Model)r;
         this.uploadStaticModel(model, orient, x - this.basex, y, z - this.basez, vb, ab);
      } else if (r instanceof DynamicObject) {
         model = ((DynamicObject)r).getModelZbuf();
         if (model != null) {
            this.uploadStaticModel(model, orient, x - this.basex, y, z - this.basez, vb, ab);
         }
      }

      int endpos = zone.vboA != null ? zone.vboA.vb.position() : 0;
      if (endpos > pos) {
         assert model != null;

         if (lx > -1) {
            lx -= this.basex >> 7;
            lz -= this.basez >> 7;
            ux -= this.basex >> 7;
            uz -= this.basez >> 7;

            assert lx >= 0 : lx;

            assert lz >= 0 : lz;

            assert ux < 25 : ux;

            assert uz < 25 : uz;
         }

         zone.addAlphaModel(zone.glVaoA, model, pos, endpos, x - this.basex, y, z - this.basez, lx, lz, ux, uz, this.rid, this.level, id);
      }

   }

   private int upload(Scene scene, SceneTilePaint tile, int tileZ, int tileX, int tileY, GpuIntBuffer vertexBuffer, int lx, int lz) {
      tileX += scene.getWorldViewId() == 0 ? 40 : 0;
      tileY += scene.getWorldViewId() == 0 ? 40 : 0;
      int[][][] tileHeights = scene.getTileHeights();
      int swHeight = tileHeights[tileZ][tileX][tileY];
      int seHeight = tileHeights[tileZ][tileX + 1][tileY];
      int neHeight = tileHeights[tileZ][tileX + 1][tileY + 1];
      int nwHeight = tileHeights[tileZ][tileX][tileY + 1];
      int swColor = tile.getSwColor();
      int seColor = tile.getSeColor();
      int neColor = tile.getNeColor();
      int nwColor = tile.getNwColor();
      if (neColor == 12345678) {
         return 0;
      } else {
         int lx0 = lx;
         int ly0 = swHeight;
         int lz0 = lz;
         int hsl0 = swColor;
         int lx1 = lx + 128;
         int ly1 = seHeight;
         int lz1 = lz;
         int hsl1 = seColor;
         int lx2 = lx + 128;
         int ly2 = neHeight;
         int lz2 = lz + 128;
         int hsl2 = neColor;
         int lx3 = lx;
         int ly3 = nwHeight;
         int lz3 = lz + 128;
         int hsl3 = nwColor;
         int tex = tile.getTexture() + 1;
         vertexBuffer.put22224(lx2, ly2, lz2, hsl2);
         vertexBuffer.put2222(tex, 256, 256, 0);
         vertexBuffer.put22224(lx3, ly3, lz3, hsl3);
         vertexBuffer.put2222(tex, 0, 256, 0);
         vertexBuffer.put22224(lx1, ly1, lz1, hsl1);
         vertexBuffer.put2222(tex, 256, 0, 0);
         vertexBuffer.put22224(lx0, ly0, lz0, hsl0);
         vertexBuffer.put2222(tex, 0, 0, 0);
         vertexBuffer.put22224(lx1, ly1, lz1, hsl1);
         vertexBuffer.put2222(tex, 256, 0, 0);
         vertexBuffer.put22224(lx3, ly3, lz3, hsl3);
         vertexBuffer.put2222(tex, 0, 256, 0);
         return 6;
      }
   }

   private int upload(SceneTileModel sceneTileModel, int lx, int lz, GpuIntBuffer vertexBuffer) {
      int[] faceX = sceneTileModel.getFaceX();
      int[] faceY = sceneTileModel.getFaceY();
      int[] faceZ = sceneTileModel.getFaceZ();
      int[] vertexX = sceneTileModel.getVertexX();
      int[] vertexY = sceneTileModel.getVertexY();
      int[] vertexZ = sceneTileModel.getVertexZ();
      int[] triangleColorA = sceneTileModel.getTriangleColorA();
      int[] triangleColorB = sceneTileModel.getTriangleColorB();
      int[] triangleColorC = sceneTileModel.getTriangleColorC();
      int[] triangleTextures = sceneTileModel.getTriangleTextureId();
      int faceCount = faceX.length;
      int cnt = 0;

      for(int i = 0; i < faceCount; ++i) {
         int vertex0 = faceX[i];
         int vertex1 = faceY[i];
         int vertex2 = faceZ[i];
         int hsl0 = triangleColorA[i];
         int hsl1 = triangleColorB[i];
         int hsl2 = triangleColorC[i];
         if (hsl0 != 12345678) {
            cnt += 3;
            int lx0 = vertexX[vertex0] - this.basex;
            int ly0 = vertexY[vertex0];
            int lz0 = vertexZ[vertex0] - this.basez;
            int lx1 = vertexX[vertex1] - this.basex;
            int ly1 = vertexY[vertex1];
            int lz1 = vertexZ[vertex1] - this.basez;
            int lx2 = vertexX[vertex2] - this.basex;
            int ly2 = vertexY[vertex2];
            int lz2 = vertexZ[vertex2] - this.basez;
            int tex = triangleTextures != null ? triangleTextures[i] + 1 : 0;
            vertexBuffer.put22224(lx0, ly0, lz0, hsl0);
            vertexBuffer.put2222(tex, (int)((float)(vertexX[vertex0] - lx) * 2.0F), (int)((float)(vertexZ[vertex0] - lz) * 2.0F), 0);
            vertexBuffer.put22224(lx1, ly1, lz1, hsl1);
            vertexBuffer.put2222(tex, (int)((float)(vertexX[vertex1] - lx) * 2.0F), (int)((float)(vertexZ[vertex1] - lz) * 2.0F), 0);
            vertexBuffer.put22224(lx2, ly2, lz2, hsl2);
            vertexBuffer.put2222(tex, (int)((float)(vertexX[vertex2] - lx) * 2.0F), (int)((float)(vertexZ[vertex2] - lz) * 2.0F), 0);
         }
      }

      return cnt;
   }

   private int uploadStaticModel(Model model, int orient, int x, int y, int z, GpuIntBuffer vb, GpuIntBuffer ab) {
      int vertexCount = model.getVerticesCount();
      int triangleCount = model.getFaceCount();
      float[] vertexX = model.getVerticesX();
      float[] vertexY = model.getVerticesY();
      float[] vertexZ = model.getVerticesZ();
      int[] indices1 = model.getFaceIndices1();
      int[] indices2 = model.getFaceIndices2();
      int[] indices3 = model.getFaceIndices3();
      int[] color1s = model.getFaceColors1();
      int[] color2s = model.getFaceColors2();
      int[] color3s = model.getFaceColors3();
      short[] faceTextures = model.getFaceTextures();
      byte[] transparencies = model.getFaceTransparencies();
      byte[] bias = model.getFaceBias();
      int orientSin = 0;
      int orientCos = 0;
      if (orient != 0) {
         orientSin = Perspective.SINE[orient];
         orientCos = Perspective.COSINE[orient];
      }

      int len;
      int face;
      int color1;
      int color2;
      int color3;
      for(len = 0; len < vertexCount; ++len) {
         face = (int)vertexX[len];
         color1 = (int)vertexY[len];
         color2 = (int)vertexZ[len];
         if (orient != 0) {
            color3 = face;
            face = color2 * orientSin + color3 * orientCos >> 16;
            color2 = color2 * orientCos - color3 * orientSin >> 16;
         }

         face += x;
         color1 += y;
         color2 += z;
         this.modelLocalXI[len] = face;
         this.modelLocalYI[len] = color1;
         this.modelLocalZI[len] = color2;
      }

      len = 0;

      for(face = 0; face < triangleCount; ++face) {
         color1 = color1s[face];
         color2 = color2s[face];
         color3 = color3s[face];
         boolean alpha = transparencies != null && transparencies[face] != 0;
         if (color3 == -1) {
            color3 = color1;
            color2 = color1;
         } else if (color3 == -2) {
            continue;
         }

         int triangleA = indices1[face];
         int triangleB = indices2[face];
         int triangleC = indices3[face];
         int vx1 = this.modelLocalXI[triangleA];
         int vy1 = this.modelLocalYI[triangleA];
         int vz1 = this.modelLocalZI[triangleA];
         int vx2 = this.modelLocalXI[triangleB];
         int vy2 = this.modelLocalYI[triangleB];
         int vz2 = this.modelLocalZI[triangleB];
         int vx3 = this.modelLocalXI[triangleC];
         int vy3 = this.modelLocalYI[triangleC];
         int vz3 = this.modelLocalZI[triangleC];
         this.computeFaceUvs(model, face);
         int su0 = (int)(this.u0 * 256.0F);
         int sv0 = (int)(this.v0 * 256.0F);
         int su1 = (int)(this.u1 * 256.0F);
         int sv1 = (int)(this.v1 * 256.0F);
         int su2 = (int)(this.u2 * 256.0F);
         int sv2 = (int)(this.v2 * 256.0F);
         int alphaBias = 0;
         alphaBias |= transparencies != null ? (transparencies[face] & 255) << 24 : 0;
         alphaBias |= bias != null ? (bias[face] & 255) << 16 : 0;
         int texture = faceTextures != null ? faceTextures[face] + 1 : 0;
         GpuIntBuffer buf = alpha ? ab : vb;
         buf.put22224(vx1, vy1, vz1, alphaBias | color1);
         buf.put2222(texture, su0, sv0, 0);
         buf.put22224(vx2, vy2, vz2, alphaBias | color2);
         buf.put2222(texture, su1, sv1, 0);
         buf.put22224(vx3, vy3, vz3, alphaBias | color3);
         buf.put2222(texture, su2, sv2, 0);
         len += 3;
      }

      return len;
   }

   int uploadTempModel(Model model, int orientation, int x, int y, int z, IntBuffer opaqueBuffer) {
      int triangleCount = model.getFaceCount();
      int vertexCount = model.getVerticesCount();
      float[] verticesX = model.getVerticesX();
      float[] verticesY = model.getVerticesY();
      float[] verticesZ = model.getVerticesZ();
      int[] indices1 = model.getFaceIndices1();
      int[] indices2 = model.getFaceIndices2();
      int[] indices3 = model.getFaceIndices3();
      int[] color1s = model.getFaceColors1();
      int[] color2s = model.getFaceColors2();
      int[] color3s = model.getFaceColors3();
      short[] faceTextures = model.getFaceTextures();
      byte[] bias = model.getFaceBias();
      byte overrideAmount = model.getOverrideAmount();
      byte overrideHue = model.getOverrideHue();
      byte overrideSat = model.getOverrideSaturation();
      byte overrideLum = model.getOverrideLuminance();
      float orientSine = 0.0F;
      float orientCosine = 0.0F;
      if (orientation != 0) {
         orientSine = (float)Perspective.SINE[orientation] / 65536.0F;
         orientCosine = (float)Perspective.COSINE[orientation] / 65536.0F;
      }

      int len;
      for(len = 0; len < vertexCount; ++len) {
         float vertexX = verticesX[len];
         float vertexY = verticesY[len];
         float vertexZ = verticesZ[len];
         if (orientation != 0) {
            float x0 = vertexX;
            vertexX = vertexZ * orientSine + x0 * orientCosine;
            vertexZ = vertexZ * orientCosine - x0 * orientSine;
         }

         vertexX += (float)x;
         vertexY += (float)y;
         vertexZ += (float)z;
         modelLocalX[len] = vertexX;
         modelLocalY[len] = vertexY;
         modelLocalZ[len] = vertexZ;
      }

      len = 0;

      for(int face = 0; face < triangleCount; ++face) {
         int color1 = color1s[face];
         int color2 = color2s[face];
         int color3 = color3s[face];
         if (color3 == -1) {
            color3 = color1;
            color2 = color1;
         } else if (color3 == -2) {
            continue;
         }

         if ((faceTextures == null || faceTextures[face] == -1) && overrideAmount > 0) {
            color1 = interpolateHSL(color1, overrideHue, overrideSat, overrideLum, overrideAmount);
            color2 = interpolateHSL(color2, overrideHue, overrideSat, overrideLum, overrideAmount);
            color3 = interpolateHSL(color3, overrideHue, overrideSat, overrideLum, overrideAmount);
         }

         int triangleA = indices1[face];
         int triangleB = indices2[face];
         int triangleC = indices3[face];
         float vx1 = modelLocalX[triangleA];
         float vy1 = modelLocalY[triangleA];
         float vz1 = modelLocalZ[triangleA];
         float vx2 = modelLocalX[triangleB];
         float vy2 = modelLocalY[triangleB];
         float vz2 = modelLocalZ[triangleB];
         float vx3 = modelLocalX[triangleC];
         float vy3 = modelLocalY[triangleC];
         float vz3 = modelLocalZ[triangleC];
         this.computeFaceUvs(model, face);
         int su0 = (int)(this.u0 * 256.0F);
         int sv0 = (int)(this.v0 * 256.0F);
         int su1 = (int)(this.u1 * 256.0F);
         int sv1 = (int)(this.v1 * 256.0F);
         int su2 = (int)(this.u2 * 256.0F);
         int sv2 = (int)(this.v2 * 256.0F);
         int alphaBias = 0;
         alphaBias |= bias != null ? (bias[face] & 255) << 16 : 0;
         int texture = faceTextures != null ? faceTextures[face] + 1 : 0;
         putfff4(opaqueBuffer, vx1, vy1, vz1, alphaBias | color1);
         put2222(opaqueBuffer, texture, su0, sv0, 0);
         putfff4(opaqueBuffer, vx2, vy2, vz2, alphaBias | color2);
         put2222(opaqueBuffer, texture, su1, sv1, 0);
         putfff4(opaqueBuffer, vx3, vy3, vz3, alphaBias | color3);
         put2222(opaqueBuffer, texture, su2, sv2, 0);
         len += 3;
      }

      return len;
   }

   static void put2222(IntBuffer vb, int x, int y, int z, int w) {
      vb.put((y & '\uffff') << 16 | x & '\uffff');
      vb.put((w & '\uffff') << 16 | z & '\uffff');
   }

   static void putfff4(IntBuffer vb, float x, float y, float z, int w) {
      vb.put(Float.floatToIntBits(x));
      vb.put(Float.floatToIntBits(y));
      vb.put(Float.floatToIntBits(z));
      vb.put(w);
   }

   static int interpolateHSL(int hsl, byte hue2, byte sat2, byte lum2, byte lerp) {
      int hue = hsl >> 10 & 63;
      int sat = hsl >> 7 & 7;
      int lum = hsl & 127;
      int var9 = lerp & 255;
      if (hue2 != -1) {
         hue += var9 * (hue2 - hue) >> 7;
      }

      if (sat2 != -1) {
         sat += var9 * (sat2 - sat) >> 7;
      }

      if (lum2 != -1) {
         lum += var9 * (lum2 - lum) >> 7;
      }

      return (hue << 10 | sat << 7 | lum) & '\uffff';
   }

   void computeFaceUvs(Model model, int face) {
      float[] vertexX = model.getVerticesX();
      float[] vertexY = model.getVerticesY();
      float[] vertexZ = model.getVerticesZ();
      int[] indices1 = model.getFaceIndices1();
      int[] indices2 = model.getFaceIndices2();
      int[] indices3 = model.getFaceIndices3();
      byte[] textureFaces = model.getTextureFaces();
      int[] texIndices1 = model.getTexIndices1();
      int[] texIndices2 = model.getTexIndices2();
      int[] texIndices3 = model.getTexIndices3();
      if (textureFaces != null && textureFaces[face] != -1) {
         int triangleA = indices1[face];
         int triangleB = indices2[face];
         int triangleC = indices3[face];
         int tfaceIdx = textureFaces[face] & 255;
         int texA = texIndices1[tfaceIdx];
         int texB = texIndices2[tfaceIdx];
         int texC = texIndices3[tfaceIdx];
         float v1x = vertexX[texA];
         float v1y = vertexY[texA];
         float v1z = vertexZ[texA];
         float v2x = vertexX[texB] - v1x;
         float v2y = vertexY[texB] - v1y;
         float v2z = vertexZ[texB] - v1z;
         float v3x = vertexX[texC] - v1x;
         float v3y = vertexY[texC] - v1y;
         float v3z = vertexZ[texC] - v1z;
         float v4x = vertexX[triangleA] - v1x;
         float v4y = vertexY[triangleA] - v1y;
         float v4z = vertexZ[triangleA] - v1z;
         float v5x = vertexX[triangleB] - v1x;
         float v5y = vertexY[triangleB] - v1y;
         float v5z = vertexZ[triangleB] - v1z;
         float v6x = vertexX[triangleC] - v1x;
         float v6y = vertexY[triangleC] - v1y;
         float v6z = vertexZ[triangleC] - v1z;
         float v7x = v2y * v3z - v2z * v3y;
         float v7y = v2z * v3x - v2x * v3z;
         float v7z = v2x * v3y - v2y * v3x;
         float v8x = v3y * v7z - v3z * v7y;
         float v8y = v3z * v7x - v3x * v7z;
         float v8z = v3x * v7y - v3y * v7x;
         float f = 1.0F / (v8x * v2x + v8y * v2y + v8z * v2z);
         this.u0 = (v8x * v4x + v8y * v4y + v8z * v4z) * f;
         this.u1 = (v8x * v5x + v8y * v5y + v8z * v5z) * f;
         this.u2 = (v8x * v6x + v8y * v6y + v8z * v6z) * f;
         v8x = v2y * v7z - v2z * v7y;
         v8y = v2z * v7x - v2x * v7z;
         v8z = v2x * v7y - v2y * v7x;
         f = 1.0F / (v8x * v3x + v8y * v3y + v8z * v3z);
         this.v0 = (v8x * v4x + v8y * v4y + v8z * v4z) * f;
         this.v1 = (v8x * v5x + v8y * v5y + v8z * v5z) * f;
         this.v2 = (v8x * v6x + v8y * v6y + v8z * v6z) * f;
      } else {
         this.u0 = 0.0F;
         this.v0 = 0.0F;
         this.u1 = 1.0F;
         this.v1 = 0.0F;
         this.u2 = 0.0F;
         this.v2 = 1.0F;
      }

   }

   static {
      modelLocalX = FacePrioritySorter.modelLocalX;
      modelLocalY = FacePrioritySorter.modelLocalY;
      modelLocalZ = FacePrioritySorter.modelLocalZ;
   }
}
