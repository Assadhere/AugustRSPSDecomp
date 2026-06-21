package net.runelite.client.plugins.gpu;

import java.nio.IntBuffer;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import net.runelite.api.Model;
import net.runelite.api.Perspective;
import net.runelite.api.Scene;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL33C;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class Zone {
   private static final Logger log = LoggerFactory.getLogger(Zone.class);
   static final int VERT_SIZE = 20;
   int glVao;
   int bufLen;
   int glVaoA;
   int bufLenA;
   int sizeO;
   int sizeA;
   VBO vboO;
   VBO vboA;
   boolean initialized;
   boolean cull;
   boolean dirty;
   boolean invalidate;
   int[] levelOffsets = new int[4];
   int[][] rids;
   int[][] roofStart;
   int[][] roofEnd;
   final List<AlphaModel> alphaModels = new ArrayList(0);
   private static final int NUM_DRAW_RANGES = 512;
   private static final IntBuffer drawOff = BufferUtils.createIntBuffer(512);
   private static final IntBuffer drawEnd = BufferUtils.createIntBuffer(512);
   static final Queue<AlphaModel> modelCache = new ArrayDeque();
   private static final IntBuffer alphaElements = BufferUtils.createIntBuffer(19500);
   private static final int STATIC = 1;
   private static final int TEMP = 2;
   private static final int STATIC_UNSORTED = 3;
   private static int lastDrawMode;
   private static int lastVao;
   private static int lastzx;
   private static int lastzz;
   private static int elementBufferId;
   private static final int[] numOfPriority;
   private static final int[][] orderedFaces;
   private static final AlphaModelComparator alphaModelComparator;

   void init(VBO o, VBO a) {
      assert this.glVao == 0;

      assert this.glVaoA == 0;

      if (o != null) {
         this.vboO = o;
         this.glVao = GL33C.glGenVertexArrays();
         this.setupVao(this.glVao, o.bufId);
      }

      if (a != null) {
         this.vboA = a;
         this.glVaoA = GL33C.glGenVertexArrays();
         this.setupVao(this.glVaoA, a.bufId);
      }

   }

   void free() {
      if (this.vboO != null) {
         this.vboO.destroy();
         this.vboO = null;
      }

      if (this.vboA != null) {
         this.vboA.destroy();
         this.vboA = null;
      }

      if (this.glVao != 0) {
         GL33C.glDeleteVertexArrays(this.glVao);
         this.glVao = 0;
      }

      if (this.glVaoA != 0) {
         GL33C.glDeleteVertexArrays(this.glVaoA);
         this.glVaoA = 0;
      }

      this.alphaModels.clear();
   }

   void unmap() {
      if (this.vboO != null) {
         this.vboO.unmap();
      }

      if (this.vboA != null) {
         this.vboA.unmap();
      }

      if (this.vboO != null) {
         this.bufLen = this.vboO.len / 5;
      }

      if (this.vboA != null) {
         this.bufLenA = this.vboA.len / 5;
      }

   }

   private void setupVao(int vao, int buffer) {
      GL33C.glBindVertexArray(vao);
      GL33C.glBindBuffer(34962, buffer);
      GL33C.glEnableVertexAttribArray(0);
      GL33C.glVertexAttribPointer(0, 3, 5122, false, 20, 0L);
      GL33C.glEnableVertexAttribArray(1);
      GL33C.glVertexAttribIPointer(1, 1, 5124, 20, 8L);
      GL33C.glEnableVertexAttribArray(2);
      GL33C.glVertexAttribIPointer(2, 4, 5122, 20, 12L);
      GL33C.glBindVertexArray(0);
      GL33C.glBindBuffer(34962, 0);
   }

   void updateRoofs(Map<Integer, Integer> updates) {
      for(int level = 0; level < 4; ++level) {
         for(int i = 0; i < this.rids[level].length; ++i) {
            this.rids[level][i] = (Integer)updates.getOrDefault(this.rids[level][i], this.rids[level][i]);
         }
      }

      AlphaModel m;
      for(Iterator var4 = this.alphaModels.iterator(); var4.hasNext(); m.rid = (short)(Integer)updates.getOrDefault(Integer.valueOf(m.rid), Integer.valueOf(m.rid))) {
         m = (AlphaModel)var4.next();
      }

   }

   private void convertForDraw(int vertSize) {
      assert drawOff.position() == drawEnd.position();

      drawOff.flip();
      drawEnd.flip();

      for(int i = 0; i < drawOff.limit(); ++i) {
         int off = drawOff.get(i);
         int end = drawEnd.get(i);

         assert end >= off;

         off /= vertSize >> 2;
         end /= vertSize >> 2;
         end -= off;
         drawOff.put(i, off);
         drawEnd.put(i, end);
      }

   }

   void renderOpaque(int zx, int zz, int minLevel, int currentLevel, int maxLevel, Set<Integer> hiddenRoofIds) {
      drawOff.clear();
      drawEnd.clear();

      for(int level = minLevel; level <= maxLevel; ++level) {
         int[] rids = this.rids[level];
         int[] roofStart = this.roofStart[level];
         int[] roofEnd = this.roofEnd[level];
         int roofIdx;
         int roofIdx;
         if (rids.length != 0 && !hiddenRoofIds.isEmpty() && level > currentLevel) {
            for(roofIdx = 0; roofIdx < rids.length; ++roofIdx) {
               roofIdx = rids[roofIdx];
               if (roofIdx > 0 && !hiddenRoofIds.contains(roofIdx)) {
                  assert roofEnd[roofIdx] >= roofStart[roofIdx];

                  if (roofEnd[roofIdx] > roofStart[roofIdx]) {
                     pushRange(roofStart[roofIdx], roofEnd[roofIdx]);
                  }
               }
            }

            roofIdx = level == 0 ? 0 : this.levelOffsets[level - 1];

            for(roofIdx = rids.length - 1; roofIdx >= 0; --roofIdx) {
               int rid = rids[roofIdx];
               if (rid > 0) {
                  roofIdx = roofEnd[roofIdx];
                  break;
               }
            }

            pushRange(roofIdx, this.levelOffsets[level]);
         } else {
            roofIdx = level == 0 ? 0 : this.levelOffsets[level - 1];
            roofIdx = this.levelOffsets[level];
            pushRange(roofIdx, roofIdx);
         }
      }

      this.convertForDraw(20);
      if (drawOff.limit() > 0) {
         GL33C.glUniform3i(GpuPlugin.uniBase, zx << 10, 0, zz << 10);
         GL33C.glBindVertexArray(this.glVao);
         GL33C.glMultiDrawArrays(4, drawOff, drawEnd);
      }

   }

   private static void pushRange(int start, int end) {
      assert end >= start;

      if (start != end) {
         int idx = drawEnd.position();
         if (idx > 0 && drawEnd.get(idx - 1) == start) {
            drawEnd.put(idx - 1, end);
         } else if (!drawEnd.hasRemaining()) {
            log.debug("draw ranges exhausted");
         } else {
            drawOff.put(start);
            drawEnd.put(end);
         }

      }
   }

   void addAlphaModel(int vao, Model model, int startpos, int endpos, int x, int y, int z, int lx, int lz, int ux, int uz, int rid, int level, int id) {
      AlphaModel m = new AlphaModel();
      m.id = id;
      m.startpos = startpos;
      m.endpos = endpos;
      m.x = (short)x;
      m.y = (short)y;
      m.z = (short)z;
      m.vao = vao;
      m.rid = (short)rid;
      m.level = (byte)level;
      if (lx > -1) {
         m.lx = (byte)lx;
         m.lz = (byte)lz;
         m.ux = (byte)ux;
         m.uz = (byte)uz;
      } else {
         m.lx = m.lz = m.ux = m.uz = -1;
      }

      int faceCount = model.getFaceCount();
      int[] color3 = model.getFaceColors3();
      byte[] transparencies = model.getFaceTransparencies();
      float[] vertexX = model.getVerticesX();
      float[] vertexY = model.getVerticesY();
      float[] vertexZ = model.getVerticesZ();
      int[] indices1 = model.getFaceIndices1();
      int[] indices2 = model.getFaceIndices2();
      int[] indices3 = model.getFaceIndices3();
      int minX = Integer.MAX_VALUE;
      int minY = minX;
      int minZ = minY;
      int maxX = Integer.MIN_VALUE;
      int maxY = maxX;
      int maxZ = maxY;

      int cx;
      int cy;
      int cz;
      int fz;
      for(cx = 0; cx < faceCount; ++cx) {
         if (color3[cx] != -2 && transparencies[cx] != 0) {
            cy = (int)(vertexX[indices1[cx]] + vertexX[indices2[cx]] + vertexX[indices3[cx]]);
            cz = (int)(vertexY[indices1[cx]] + vertexY[indices2[cx]] + vertexY[indices3[cx]]);
            fz = (int)(vertexZ[indices1[cx]] + vertexZ[indices2[cx]] + vertexZ[indices3[cx]]);
            minX = Math.min(minX, cy);
            maxX = Math.max(maxX, cy);
            minY = Math.min(minY, cz);
            maxY = Math.max(maxY, cz);
            minZ = Math.min(minZ, fz);
            maxZ = Math.max(maxZ, fz);
         }
      }

      cx = (minX + maxX) / 6;
      cy = (minY + maxY) / 6;
      cz = (minZ + maxZ) / 6;
      fz = Math.max(Math.max(Math.max(maxX / 3 - cx, minX / -3 - cx), Math.max(maxY / 3 - cy, minY / -3 - cy) * 2), Math.max(maxZ / 3 - cz, minZ / -3 - cz));
      int shift = 0;

      for(int v = fz >> 10; v > 0; v >>= 1) {
         ++shift;
      }

      int[] packedFaces = m.packedFaces = new int[(endpos - startpos) / 15];
      int radius = 0;
      char bufferIdx = 0;

      for(int f = 0; f < faceCount; ++f) {
         if (color3[f] != -2 && transparencies[f] != 0) {
            int fx = (int)(vertexX[indices1[f]] + vertexX[indices2[f]] + vertexX[indices3[f]]) / 3 - cx >> shift;
            int fy = (int)(vertexY[indices1[f]] + vertexY[indices2[f]] + vertexY[indices3[f]]) / 3 - cy >> shift;
            int fz = (int)(vertexZ[indices1[f]] + vertexZ[indices2[f]] + vertexZ[indices3[f]]) / 3 - cz >> shift;
            radius = Math.max(radius, fx * fx + fy * fy + fz * fz);
            packedFaces[bufferIdx] = (fx & 2047) << 21 | (fy & 1023) << 11 | fz & 2047;
            ++bufferIdx;
         }
      }

      assert radius >= 0;

      m.radius = 2 + (int)Math.sqrt((double)radius);

      assert packedFaces.length > 0;

      assert bufferIdx == packedFaces.length;

      this.alphaModels.add(m);
   }

   void addTempAlphaModel(int vao, int startpos, int endpos, int level, int x, int y, int z) {
      AlphaModel m = (AlphaModel)modelCache.poll();
      if (m == null) {
         m = new AlphaModel();
      }

      m.id = -1;
      m.startpos = startpos;
      m.endpos = endpos;
      m.x = (short)x;
      m.y = (short)y;
      m.z = (short)z;
      m.vao = vao;
      m.rid = -1;
      m.level = (byte)level;
      m.lx = m.lz = m.ux = m.uz = -1;
      m.flags = 0;
      m.zofx = m.zofz = 0;
      this.alphaModels.add(m);
   }

   void removeTemp() {
      for(int i = this.alphaModels.size() - 1; i >= 0; --i) {
         AlphaModel m = (AlphaModel)this.alphaModels.get(i);
         if (m.isTemp() || (m.flags & 2) != 0) {
            this.alphaModels.remove(i);
            m.packedFaces = null;
            modelCache.add(m);
         }

         m.flags &= -2;
      }

   }

   static void initBuffer() {
      elementBufferId = GL33C.glGenBuffers();
   }

   static void freeBuffer() {
      GL33C.glDeleteBuffers(elementBufferId);
      elementBufferId = 0;
   }

   void alphaSort(int zx, int zz, int cx, int cy, int cz) {
      alphaModelComparator.zx = zx;
      alphaModelComparator.zz = zz;
      alphaModelComparator.cx = cx;
      alphaModelComparator.cy = cy;
      alphaModelComparator.cz = cz;
      this.alphaModels.sort(alphaModelComparator);
   }

   void renderAlpha(int zx, int zz, int cyaw, int cpitch, int minLevel, int currentLevel, int maxLevel, int level, Set<Integer> hiddenRoofIds, boolean useStaticUnsorted) {
      drawOff.clear();
      drawEnd.clear();
      alphaElements.clear();
      lastVao = 0;
      lastDrawMode = 0;
      lastzx = zx;
      lastzz = zz;
      int yawsin = Perspective.SINE[cyaw];
      int yawcos = Perspective.COSINE[cyaw];
      int pitchsin = Perspective.SINE[cpitch];
      int pitchcos = Perspective.COSINE[cpitch];

      for(int j = 0; j < this.alphaModels.size(); ++j) {
         AlphaModel m = (AlphaModel)this.alphaModels.get(j);
         if ((m.flags & 1) == 0 && m.level == level) {
            boolean ok = false;
            if (level >= minLevel && level <= maxLevel && (level <= currentLevel || !hiddenRoofIds.contains(Integer.valueOf(m.rid)))) {
               ok = true;
            }

            if (ok) {
               if (lastVao != m.vao || lastzx != zx - m.zofx || lastzz != zz - m.zofz) {
                  this.flush();
               }

               lastVao = m.vao;
               lastzx = zx - m.zofx;
               lastzz = zz - m.zofz;
               if (m.isTemp()) {
                  lastDrawMode = 2;
                  pushRange(m.startpos, m.endpos);
               } else if (useStaticUnsorted) {
                  lastDrawMode = 3;
                  pushRange(m.startpos, m.endpos);
               } else {
                  lastDrawMode = 1;
                  int radius = m.radius;
                  int diameter = 1 + radius * 2;
                  int[] packedFaces = m.packedFaces;
                  if (diameter < 6000) {
                     Arrays.fill(FacePrioritySorter.zsortHead, 0, diameter, '\uffff');
                     Arrays.fill(FacePrioritySorter.zsortTail, 0, diameter, '\uffff');

                     int start;
                     int i;
                     int faceIdx;
                     for(start = 0; start < packedFaces.length; start = (char)(start + 1)) {
                        i = packedFaces[start];
                        int x = i >> 21;
                        faceIdx = i << 11 >> 22;
                        int z = i << 21 >> 21;
                        int t = z * yawcos - x * yawsin >> 16;
                        int fz = faceIdx * pitchsin + t * pitchcos >> 16;
                        fz += radius;

                        assert fz >= 0 && fz < diameter : fz;

                        if (FacePrioritySorter.zsortTail[fz] == '\uffff') {
                           FacePrioritySorter.zsortHead[fz] = FacePrioritySorter.zsortTail[fz] = (char)start;
                           FacePrioritySorter.zsortNext[start] = '\uffff';
                        } else {
                           char lastFace = FacePrioritySorter.zsortTail[fz];
                           FacePrioritySorter.zsortNext[lastFace] = (char)start;
                           FacePrioritySorter.zsortNext[start] = '\uffff';
                           FacePrioritySorter.zsortTail[fz] = (char)start;
                        }
                     }

                     if (packedFaces.length * 3 > alphaElements.remaining()) {
                        if (packedFaces.length * 3 > alphaElements.capacity()) {
                           log.debug("Alpha model too large: {}", packedFaces.length);
                           continue;
                        }

                        this.flush();
                     }

                     start = m.startpos / 5;

                     for(i = diameter - 1; i >= 0; --i) {
                        for(char face = FacePrioritySorter.zsortHead[i]; face != '\uffff'; face = FacePrioritySorter.zsortNext[face]) {
                           faceIdx = face * 3;
                           faceIdx += start;
                           alphaElements.put(faceIdx++);
                           alphaElements.put(faceIdx++);
                           alphaElements.put(faceIdx++);
                        }
                     }
                  }
               }
            }
         }
      }

      this.flush();
   }

   private void flush() {
      if (lastDrawMode == 2) {
         this.convertForDraw(24);

         assert drawOff.limit() > 0;

         GL33C.glUniform3i(GpuPlugin.uniBase, 0, 0, 0);
         GL33C.glBindVertexArray(lastVao);
         GL33C.glDepthMask(false);
         GL33C.glMultiDrawArrays(4, drawOff, drawEnd);
         GL33C.glDepthMask(true);
         drawOff.clear();
         drawEnd.clear();
      } else if (lastDrawMode == 1) {
         alphaElements.flip();
         GL33C.glUniform3i(GpuPlugin.uniBase, lastzx << 10, 0, lastzz << 10);
         GL33C.glBindVertexArray(lastVao);
         GL33C.glBindBuffer(34963, elementBufferId);
         GL33C.glBufferData(34963, alphaElements, 35040);
         GL33C.glDepthMask(false);
         GL33C.glDrawElements(4, alphaElements.limit(), 5125, 0L);
         GL33C.glDepthMask(true);
         GL33C.glBindBuffer(34963, 0);
         alphaElements.clear();
      } else if (lastDrawMode == 3) {
         this.convertForDraw(20);

         assert drawOff.limit() > 0;

         GL33C.glUniform3i(GpuPlugin.uniBase, lastzx << 10, 0, lastzz << 10);
         GL33C.glBindVertexArray(lastVao);
         GL33C.glDepthMask(false);
         GL33C.glMultiDrawArrays(4, drawOff, drawEnd);
         GL33C.glDepthMask(true);
         drawOff.clear();
         drawEnd.clear();
      }

   }

   void multizoneLocs(Scene scene, int zx, int zz, int cx, int cz, Zone[][] zones) {
      int offset = scene.getWorldViewId() == 0 ? 5 : 0;

      for(int i = 0; i < this.alphaModels.size(); ++i) {
         AlphaModel m = (AlphaModel)this.alphaModels.get(i);
         if (m.lx != -1) {
            int max = Integer.MAX_VALUE;
            int closestZoneX = -50;
            int closestZoneZ = -50;

            for(int x = m.lx >> 3; x <= m.ux >> 3; ++x) {
               for(int z = m.lz >> 3; z <= m.uz >> 3; ++z) {
                  int centerX = (zx - m.zofx + x) * 8 + 4 << 7;
                  int centerZ = (zz - m.zofz + z) * 8 + 4 << 7;
                  int distance = (centerX - cx) * (centerX - cx) + (centerZ - cz) * (centerZ - cz);
                  if (distance < max) {
                     max = distance;
                     closestZoneX = centerX >> 10;
                     closestZoneZ = centerZ >> 10;
                  }
               }
            }

            assert closestZoneX != -50;

            if (closestZoneX != zx || closestZoneZ != zz) {
               assert (m.flags & 2) == 0;

               assert closestZoneX + offset >= 0 : closestZoneX;

               assert closestZoneX + offset < zones.length : closestZoneX;

               assert closestZoneZ + offset >= 0 : closestZoneZ;

               assert closestZoneZ + offset < zones[0].length : closestZoneZ;

               Zone z = zones[closestZoneX + offset][closestZoneZ + offset];

               assert z != null;

               assert z != this;

               AlphaModel m2 = (AlphaModel)modelCache.poll();
               if (m2 == null) {
                  m2 = new AlphaModel();
               }

               m2.id = m.id;
               m2.startpos = m.startpos;
               m2.endpos = m.endpos;
               m2.x = m.x;
               m2.y = m.y;
               m2.z = m.z;
               m2.vao = m.vao;
               m2.rid = m.rid;
               m2.level = m.level;
               m2.lx = m.lx;
               m2.lz = m.lz;
               m2.ux = m.ux;
               m2.uz = m.uz;
               m2.zofx = (byte)(closestZoneX - zx);
               m2.zofz = (byte)(closestZoneZ - zz);
               m2.packedFaces = m.packedFaces;
               m2.radius = m.radius;
               m2.flags = 2;
               m.flags = (byte)(m.flags | 1);
               z.alphaModels.add(m2);
            }
         }
      }

   }

   public Zone() {
   }

   static {
      numOfPriority = FacePrioritySorter.numOfPriority;
      orderedFaces = FacePrioritySorter.orderedFaces;
      alphaModelComparator = new AlphaModelComparator();
   }

   static class AlphaModelComparator implements Comparator<AlphaModel> {
      int zx;
      int zz;
      int cx;
      int cy;
      int cz;

      public int compare(AlphaModel o1, AlphaModel o2) {
         return Integer.compare(this.z(o2), this.z(o1));
      }

      private int z(AlphaModel m) {
         int mx = m.x + (this.zx - m.zofx << 10);
         int mz = m.z + (this.zz - m.zofz << 10);
         return (mx - this.cx) * (mx - this.cx) + (m.y - this.cy) * (m.y - this.cy) + (mz - this.cz) * (mz - this.cz);
      }
   }

   static class AlphaModel {
      int id;
      int startpos;
      int endpos;
      short x;
      short y;
      short z;
      short rid;
      int vao;
      byte level;
      byte lx;
      byte lz;
      byte ux;
      byte uz;
      byte zofx;
      byte zofz;
      byte flags;
      int radius;
      int[] packedFaces;
      static final int SKIP = 1;
      static final int TEMP = 2;

      boolean isTemp() {
         return this.packedFaces == null;
      }
   }
}
