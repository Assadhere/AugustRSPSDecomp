package net.runelite.client.ui.overlay.outline;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import net.runelite.api.Actor;
import net.runelite.api.Client;
import net.runelite.api.DecorativeObject;
import net.runelite.api.GameObject;
import net.runelite.api.GraphicsObject;
import net.runelite.api.GroundObject;
import net.runelite.api.ItemLayer;
import net.runelite.api.MainBufferProvider;
import net.runelite.api.Model;
import net.runelite.api.Perspective;
import net.runelite.api.Renderable;
import net.runelite.api.RuneLiteObject;
import net.runelite.api.TileItem;
import net.runelite.api.TileObject;
import net.runelite.api.WallObject;
import net.runelite.api.WorldView;
import net.runelite.api.coords.LocalPoint;

@Singleton
public class ModelOutlineRenderer {
   private static final int MAX_OUTLINE_WIDTH = 50;
   private static final int MAX_FEATHER = 4;
   private static final int DIRECT_WRITE_OUTLINE_WIDTH_THRESHOLD = 10;
   private final Client client;
   private final int[] projectedVerticesX = new int[6500];
   private final int[] projectedVerticesY = new int[6500];
   private int clipX1;
   private int clipY1;
   private int clipX2;
   private int clipY2;
   private int croppedX1;
   private int croppedY1;
   private int croppedX2;
   private int croppedY2;
   private int croppedWidth;
   private int croppedHeight;
   private int[] visited = new int[0];
   private final IntBlockBuffer outlinePixelsBlockBuffer = new IntBlockBuffer();
   private int[][] outlinePixelsBlockIndices = new int[0][];
   private int[] outlinePixelsBlockIndicesLengths = new int[0];
   private int[] outlinePixelsLastBlockLength;
   private int outlineArrayWidth;
   private PixelDistanceGroupIndex[][][] precomputedGroupIndices = new PixelDistanceGroupIndex[0][][];
   private PixelDistanceDelta[][][] precomputedDistanceDeltas = new PixelDistanceDelta[0][][];

   @Inject
   private ModelOutlineRenderer(Client client) {
      this.client = client;
   }

   private static int nextPowerOfTwo(int value) {
      --value;
      value |= value >> 1;
      value |= value >> 2;
      value |= value >> 4;
      value |= value >> 8;
      value |= value >> 16;
      ++value;
      return value;
   }

   private static boolean cullFace(int x1, int y1, int x2, int y2, int x3, int y3) {
      return (y2 - y1) * (x3 - x2) - (x2 - x1) * (y3 - y2) <= 0;
   }

   private PixelDistanceGroupIndex[] getPriorityList(int outlineWidth, int feather) {
      if (this.precomputedGroupIndices.length <= outlineWidth) {
         this.precomputedGroupIndices = (PixelDistanceGroupIndex[][][])Arrays.copyOf(this.precomputedGroupIndices, outlineWidth + 1);
      }

      if (this.precomputedGroupIndices[outlineWidth] == null) {
         this.precomputedGroupIndices[outlineWidth] = new PixelDistanceGroupIndex[feather + 1][];
      } else if (this.precomputedGroupIndices[outlineWidth].length <= feather) {
         this.precomputedGroupIndices[outlineWidth] = (PixelDistanceGroupIndex[][])Arrays.copyOf(this.precomputedGroupIndices[outlineWidth], feather + 1);
      }

      if (this.precomputedGroupIndices[outlineWidth][feather] == null) {
         double fadedDistance = (double)feather / 4.0 * ((double)outlineWidth - 0.5);
         List<PixelDistanceGroupIndex> ps = new ArrayList();

         for(int x = 0; x <= outlineWidth; ++x) {
            for(int y = 0; y <= outlineWidth; ++y) {
               if (x != 0 || y != 0) {
                  double dist = Math.hypot((double)x, (double)y);
                  if (!(dist > (double)outlineWidth)) {
                     double outerDist = (double)outlineWidth - dist + 0.5;
                     double multipliedAlpha = outerDist < fadedDistance ? outerDist / fadedDistance : 1.0;
                     ps.add(new PixelDistanceGroupIndex(dist, x + y * this.outlineArrayWidth, multipliedAlpha));
                  }
               }
            }
         }

         ps.sort(Comparator.comparingDouble((rec$) -> {
            return ((PixelDistanceGroupIndex)rec$).getDistance();
         }));
         this.precomputedGroupIndices[outlineWidth][feather] = (PixelDistanceGroupIndex[])ps.toArray(new PixelDistanceGroupIndex[0]);
      }

      return this.precomputedGroupIndices[outlineWidth][feather];
   }

   private void ensureDistanceDeltasCreated(int outlineWidth) {
      if (this.precomputedDistanceDeltas.length <= outlineWidth) {
         this.precomputedDistanceDeltas = (PixelDistanceDelta[][][])Arrays.copyOf(this.precomputedDistanceDeltas, outlineWidth + 1);
      }

      if (this.precomputedDistanceDeltas[outlineWidth] == null) {
         this.precomputedDistanceDeltas[outlineWidth] = new PixelDistanceDelta[4][];
      }

      if (this.precomputedDistanceDeltas[outlineWidth][0] == null) {
         List<PixelDistanceDelta> distances = new ArrayList();

         int dy;
         int dx;
         for(dy = -outlineWidth; dy <= outlineWidth; ++dy) {
            for(dx = 1; dx <= outlineWidth; ++dx) {
               if (Math.abs(dy) <= dx) {
                  double dist = Math.hypot((double)dx, (double)dy);
                  if (!(dist > (double)outlineWidth)) {
                     distances.add(new PixelDistanceDelta(dx, dy));
                  }
               }
            }
         }

         for(dy = 0; dy < 4; ++dy) {
            this.precomputedDistanceDeltas[outlineWidth][dy] = (PixelDistanceDelta[])distances.toArray(new PixelDistanceDelta[0]);

            for(dx = 0; dx < distances.size(); ++dx) {
               PixelDistanceDelta pdd = (PixelDistanceDelta)distances.get(dx);
               distances.set(dx, new PixelDistanceDelta(pdd.dy, -pdd.dx));
            }
         }

      }
   }

   private void enqueueOutlinePixel(int distanceGroupIndex, int x, int y) {
      if (this.outlinePixelsLastBlockLength[distanceGroupIndex] == 1024) {
         int minimumBlockIndicesSize = this.outlinePixelsBlockIndicesLengths[distanceGroupIndex] + 1;
         if (minimumBlockIndicesSize > this.outlinePixelsBlockIndices[distanceGroupIndex].length) {
            this.outlinePixelsBlockIndices[distanceGroupIndex] = Arrays.copyOf(this.outlinePixelsBlockIndices[distanceGroupIndex], nextPowerOfTwo(minimumBlockIndicesSize));
         }

         this.outlinePixelsBlockIndices[distanceGroupIndex][this.outlinePixelsBlockIndicesLengths[distanceGroupIndex]] = this.outlinePixelsBlockBuffer.useNewBlock();
         int var10002 = this.outlinePixelsBlockIndicesLengths[distanceGroupIndex]++;
         this.outlinePixelsLastBlockLength[distanceGroupIndex] = 0;
      }

      int[] memory = this.outlinePixelsBlockBuffer.getMemory();
      int block = this.outlinePixelsBlockIndices[distanceGroupIndex][this.outlinePixelsBlockIndicesLengths[distanceGroupIndex] - 1];
      int blockPos = this.outlinePixelsLastBlockLength[distanceGroupIndex]++;
      memory[(block << 10) + blockPos] = y << 16 | x;
   }

   private void resetVisited(int pixelAmount) {
      int size = pixelAmount >>> 5;
      if (this.visited.length < size) {
         this.visited = new int[nextPowerOfTwo(size)];
      }

      Arrays.fill(this.visited, 0, size, 0);
   }

   private void initializeOutlineBuffers() {
      int arraySizes = this.outlineArrayWidth * this.outlineArrayWidth;
      int i;
      if (this.outlinePixelsBlockIndicesLengths.length < arraySizes) {
         this.outlinePixelsBlockIndices = new int[arraySizes][];
         this.outlinePixelsBlockIndicesLengths = new int[arraySizes];
         this.outlinePixelsLastBlockLength = new int[arraySizes];

         for(i = 0; i < arraySizes; ++i) {
            this.outlinePixelsBlockIndices[i] = new int[0];
         }
      }

      for(i = 0; i < arraySizes; ++i) {
         this.outlinePixelsLastBlockLength[i] = 1024;
      }

   }

   private void freeAllBlockMemory() {
      for(int i = 0; i < this.outlineArrayWidth * this.outlineArrayWidth; ++i) {
         while(this.outlinePixelsBlockIndicesLengths[i] > 0) {
            int var10002 = this.outlinePixelsBlockIndicesLengths[i]--;
            this.outlinePixelsBlockBuffer.freeBlock(this.outlinePixelsBlockIndices[i][this.outlinePixelsBlockIndicesLengths[i]]);
         }

         this.outlinePixelsLastBlockLength[i] = 1024;
      }

   }

   private void simulateHorizontalLineRasterizationForOutline(int pixelY, int x1, int x2) {
      if (x2 > this.clipX2) {
         x2 = this.clipX2;
      }

      if (x1 < this.clipX1) {
         x1 = this.clipX1;
      }

      if (x1 < x2) {
         int pixelPos1 = (pixelY - this.croppedY1) * this.croppedWidth + (x1 - this.croppedX1);
         int pixelPos2 = pixelPos1 + x2 - x1;
         int pixelPosIndex1 = pixelPos1 >> 5;
         int pixelPosIndex2 = pixelPos2 >> 5;
         int[] var10000;
         if (pixelPosIndex1 == pixelPosIndex2) {
            var10000 = this.visited;
            var10000[pixelPosIndex1] |= (1 << (pixelPos2 & 31)) - 1 ^ (1 << (pixelPos1 & 31)) - 1;
         } else {
            var10000 = this.visited;
            var10000[pixelPosIndex1] |= -(1 << (pixelPos1 & 31));
            var10000 = this.visited;
            var10000[pixelPosIndex2] |= (1 << (pixelPos2 & 31)) - 1;

            for(int i = pixelPosIndex1 + 1; i < pixelPosIndex2; ++i) {
               this.visited[i] = -1;
            }
         }

      }
   }

   private void simulateTriangleRasterizationForOutline(int x1, int y1, int x2, int y2, int x3, int y3) {
      int slope1;
      int slope2;
      if (y1 > y2) {
         slope1 = y1;
         slope2 = x1;
         y1 = y2;
         y2 = slope1;
         x1 = x2;
         x2 = slope2;
      }

      if (y2 > y3) {
         slope1 = y2;
         slope2 = x2;
         y2 = y3;
         y3 = slope1;
         x2 = x3;
         x3 = slope2;
      }

      if (y1 > y2) {
         slope1 = y1;
         slope2 = x1;
         y1 = y2;
         y2 = slope1;
         x1 = x2;
         x2 = slope2;
      }

      if (y1 <= this.clipY2) {
         slope1 = 0;
         if (y1 != y2) {
            slope1 = (x2 - x1 << 14) / (y2 - y1);
         }

         slope2 = 0;
         if (y3 != y2) {
            slope2 = (x3 - x2 << 14) / (y3 - y2);
         }

         int slope3 = 0;
         if (y1 != y3) {
            slope3 = (x1 - x3 << 14) / (y1 - y3);
         }

         if (y2 > this.clipY2) {
            y2 = this.clipY2;
         }

         if (y3 > this.clipY2) {
            y3 = this.clipY2;
         }

         if (y1 != y3 && y3 >= this.clipY1) {
            x1 <<= 14;
            x2 <<= 14;
            x3 = x1;
            if (y1 < this.clipY1) {
               x3 -= (y1 - this.clipY1) * slope3;
               x1 -= (y1 - this.clipY1) * slope1;
               y1 = this.clipY1;
            }

            if (y2 < this.clipY1) {
               x2 -= (y2 - this.clipY1) * slope2;
               y2 = this.clipY1;
            }

            int pixelY = y1;
            int height1 = y2 - y1;
            int height2 = y3 - y2;
            if (y1 != y2 && slope3 < slope1 || y1 == y2 && slope3 > slope2) {
               while(height1-- > 0) {
                  this.simulateHorizontalLineRasterizationForOutline(pixelY, x3 >> 14, x1 >> 14);
                  x3 += slope3;
                  x1 += slope1;
                  ++pixelY;
               }

               while(height2-- > 0) {
                  this.simulateHorizontalLineRasterizationForOutline(pixelY, x3 >> 14, x2 >> 14);
                  x3 += slope3;
                  x2 += slope2;
                  ++pixelY;
               }
            } else {
               while(height1-- > 0) {
                  this.simulateHorizontalLineRasterizationForOutline(pixelY, x1 >> 14, x3 >> 14);
                  x1 += slope1;
                  x3 += slope3;
                  ++pixelY;
               }

               while(height2-- > 0) {
                  this.simulateHorizontalLineRasterizationForOutline(pixelY, x2 >> 14, x3 >> 14);
                  x3 += slope3;
                  x2 += slope2;
                  ++pixelY;
               }
            }

         }
      }
   }

   private boolean projectVertices(WorldView wv, Model model, int localX, int localY, int localZ, int vertexOrientation) {
      int vertexCount = model.getVerticesCount();
      Perspective.modelToCanvas(this.client, wv, vertexCount, localX, localY, localZ, vertexOrientation, model.getVerticesX(), model.getVerticesZ(), model.getVerticesY(), this.projectedVerticesX, this.projectedVerticesY);
      boolean anyVisible = false;

      for(int i = 0; i < vertexCount; ++i) {
         int x = this.projectedVerticesX[i];
         int y = this.projectedVerticesY[i];
         if (y == Integer.MIN_VALUE) {
            this.projectedVerticesY[i] = Integer.MIN_VALUE;
         } else {
            boolean visibleX = x >= this.clipX1 && x < this.clipX2;
            boolean visibleY = y >= this.clipY1 && y < this.clipY2;
            anyVisible |= visibleX && visibleY;
            this.croppedX1 = Math.min(this.croppedX1, x);
            this.croppedX2 = Math.max(this.croppedX2, x + 1);
            this.croppedY1 = Math.min(this.croppedY1, y);
            this.croppedY2 = Math.max(this.croppedY2, y + 1);
         }
      }

      return anyVisible;
   }

   private void simulateModelRasterizationForOutline(Model model) {
      int triangleCount = model.getFaceCount();
      int[] indices1 = model.getFaceIndices1();
      int[] indices2 = model.getFaceIndices2();
      int[] indices3 = model.getFaceIndices3();
      byte[] triangleTransparencies = model.getFaceTransparencies();

      for(int i = 0; i < triangleCount; ++i) {
         if (this.projectedVerticesY[indices1[i]] != Integer.MIN_VALUE && this.projectedVerticesY[indices2[i]] != Integer.MIN_VALUE && this.projectedVerticesY[indices3[i]] != Integer.MIN_VALUE && (triangleTransparencies == null || (triangleTransparencies[i] & 255) < 254)) {
            int index1 = indices1[i];
            int index2 = indices2[i];
            int index3 = indices3[i];
            int v1x = this.projectedVerticesX[index1];
            int v1y = this.projectedVerticesY[index1];
            int v2x = this.projectedVerticesX[index2];
            int v2y = this.projectedVerticesY[index2];
            int v3x = this.projectedVerticesX[index3];
            int v3y = this.projectedVerticesY[index3];
            if (!cullFace(v1x, v1y, v2x, v2y, v3x, v3y)) {
               this.simulateTriangleRasterizationForOutline(v1x, v1y, v2x, v2y, v3x, v3y);
            }
         }
      }

   }

   private void rasterDistanceDeltas(int[] imageData, int imageWidth, int x, int y, PixelDistanceDelta[] distanceDeltas, int color) {
      PixelDistanceDelta[] var7 = distanceDeltas;
      int var8 = distanceDeltas.length;

      for(int var9 = 0; var9 < var8; ++var9) {
         PixelDistanceDelta delta = var7[var9];
         int cx = x + delta.dx;
         int cy = y + delta.dy;
         int visitedPixelPos = (cy - this.croppedY1) * this.croppedWidth + (cx - this.croppedX1);
         if (cx >= this.clipX1 && cx < this.clipX2 && cy >= this.clipY1 && cy < this.clipY2 && (this.visited[visitedPixelPos >> 5] & 1 << (visitedPixelPos & 31)) == 0) {
            imageData[cy * imageWidth + cx] = color;
         }
      }

   }

   private void processInitialOutlinePixels(boolean directWrite, Color color, int outlineWidth) {
      MainBufferProvider bufferProvider = (MainBufferProvider)this.client.getBufferProvider();
      BufferedImage image = (BufferedImage)bufferProvider.getImage();
      int imageWidth = image.getWidth();
      int[] imageData = ((DataBufferInt)image.getRaster().getDataBuffer()).getData();
      int colorRGB = color.getRGB();

      int y;
      int v1;
      int y;
      int x;
      PixelDistanceDelta[] distancesUp;
      int lastBv;
      int bit;
      int bv;
      int v;
      int end;
      for(y = 0; y < this.croppedWidth; y += 32) {
         v1 = this.visited[y >> 5];

         for(y = 1; y < this.croppedHeight; ++y) {
            x = this.visited[y * this.croppedWidth + y >> 5];
            if (v1 != x) {
               if (!directWrite) {
                  for(v = 0; v < 32; ++v) {
                     end = v1 >>> v & 1;
                     lastBv = x >>> v & 1;
                     if (end != lastBv) {
                        this.enqueueOutlinePixel(this.outlineArrayWidth, this.croppedX1 + y + v, this.croppedY1 + y - lastBv);
                     }
                  }
               } else if (outlineWidth == 1) {
                  for(v = 0; v < 32; ++v) {
                     end = v1 >>> v & 1;
                     lastBv = x >>> v & 1;
                     if (end != lastBv) {
                        imageData[(this.croppedY1 + y - lastBv) * imageWidth + this.croppedX1 + y + v] = colorRGB;
                     }
                  }
               } else {
                  PixelDistanceDelta[] distancesDown = this.precomputedDistanceDeltas[outlineWidth][3];
                  distancesUp = this.precomputedDistanceDeltas[outlineWidth][1];

                  for(lastBv = 0; lastBv < 32; ++lastBv) {
                     bit = v1 >>> lastBv & 1;
                     bv = x >>> lastBv & 1;
                     if (bit == 1 && bv == 0) {
                        this.rasterDistanceDeltas(imageData, imageWidth, this.croppedX1 + y + lastBv, this.croppedY1 + y - 1, distancesDown, colorRGB);
                     } else if (bit == 0 && bv == 1) {
                        this.rasterDistanceDeltas(imageData, imageWidth, this.croppedX1 + y + lastBv, this.croppedY1 + y, distancesUp, colorRGB);
                     }
                  }
               }
            }

            v1 = x;
         }
      }

      for(y = 0; y < this.croppedHeight; ++y) {
         v1 = y * this.croppedWidth;
         y = 0;

         for(x = 0; x < this.croppedWidth; x += 32) {
            v = this.visited[v1 + x >> 5];
            if (v != 0 && v != -1) {
               end = Math.min(32, this.clipX2 - this.croppedX1 - x);
               lastBv = v & 1;
               if (!directWrite) {
                  for(bit = 1; bit < end; ++bit) {
                     bv = v >>> bit & 1;
                     if (bv != lastBv) {
                        this.enqueueOutlinePixel(1, this.croppedX1 + x + bit - bv, this.croppedY1 + y);
                     }

                     lastBv = bv;
                  }
               } else if (outlineWidth == 1) {
                  for(bit = 1; bit < end; ++bit) {
                     bv = v >>> bit & 1;
                     if (bv != lastBv) {
                        imageData[(this.croppedY1 + y) * imageWidth + (this.croppedX1 + x + bit - bv)] = colorRGB;
                     }

                     lastBv = bv;
                  }
               } else {
                  PixelDistanceDelta[] distancesRight = this.precomputedDistanceDeltas[outlineWidth][0];
                  PixelDistanceDelta[] distancesLeft = this.precomputedDistanceDeltas[outlineWidth][2];

                  for(int bit = 1; bit < end; ++bit) {
                     int bv = v >>> bit & 1;
                     if (bv == 1 && lastBv == 0) {
                        this.rasterDistanceDeltas(imageData, imageWidth, this.croppedX1 + x + bit, this.croppedY1 + y, distancesLeft, colorRGB);
                     } else if (bv == 0 && lastBv == 1) {
                        this.rasterDistanceDeltas(imageData, imageWidth, this.croppedX1 + x + bit - 1, this.croppedY1 + y, distancesRight, colorRGB);
                     }

                     lastBv = bv;
                  }
               }
            }

            if (y >>> 31 != (v & 1) && x > 0) {
               if (directWrite) {
                  if (outlineWidth == 1) {
                     imageData[(this.croppedY1 + y) * imageWidth + (this.croppedX1 + x - (v & 1))] = colorRGB;
                  } else if ((v & 1) == 1) {
                     distancesUp = this.precomputedDistanceDeltas[outlineWidth][2];
                     this.rasterDistanceDeltas(imageData, imageWidth, this.croppedX1 + x, this.croppedY1 + y, distancesUp, colorRGB);
                  } else {
                     distancesUp = this.precomputedDistanceDeltas[outlineWidth][0];
                     this.rasterDistanceDeltas(imageData, imageWidth, this.croppedX1 + x - 1, this.croppedY1 + y, distancesUp, colorRGB);
                  }
               } else {
                  this.enqueueOutlinePixel(1, this.croppedX1 + x - (v & 1), this.croppedY1 + y);
               }
            }

            y = v;
         }
      }

   }

   private void processOutlinePixelQueue(int outlineWidth, Color color, int feather) {
      MainBufferProvider bufferProvider = (MainBufferProvider)this.client.getBufferProvider();
      BufferedImage image = (BufferedImage)bufferProvider.getImage();
      int imageWidth = image.getWidth();
      int[] imageData = ((DataBufferInt)image.getRaster().getDataBuffer()).getData();
      PixelDistanceGroupIndex[] ps = this.getPriorityList(outlineWidth, feather);
      PixelDistanceGroupIndex[] var9 = ps;
      int var10 = ps.length;

      for(int var11 = 0; var11 < var10; ++var11) {
         PixelDistanceGroupIndex p = var9[var11];
         int[] blockMemory = this.outlinePixelsBlockBuffer.getMemory();
         int groupIndex = (int)Math.round((double)color.getAlpha() * p.alphaMultiply);
         int inverseAlpha = 256 - groupIndex;
         int colorARGB = groupIndex << 24 | color.getRed() * groupIndex / 255 << 16 | color.getGreen() * groupIndex / 255 << 8 | color.getBlue() * groupIndex / 255;
         groupIndex = p.distanceGroupIndex;
         int nextGroupIndexY = groupIndex + this.outlineArrayWidth;

         for(int nextGroupIndexX = groupIndex + 1; this.outlinePixelsBlockIndicesLengths[groupIndex] > 0; this.outlinePixelsLastBlockLength[groupIndex] = 1024) {
            int block = this.outlinePixelsBlockIndices[groupIndex][this.outlinePixelsBlockIndicesLengths[groupIndex] - 1];
            int blockStart = block << 10;
            int blockEnd = blockStart + this.outlinePixelsLastBlockLength[groupIndex];

            for(int i = blockStart; i < blockEnd; ++i) {
               int x = blockMemory[i] & '\uffff';
               int y = blockMemory[i] >>> 16;
               int visitedPixelPos = (y - this.croppedY1) * this.croppedWidth + (x - this.croppedX1);
               if ((this.visited[visitedPixelPos >> 5] & 1 << (visitedPixelPos & 31)) == 0) {
                  int[] var10000 = this.visited;
                  var10000[visitedPixelPos >> 5] |= 1 << (visitedPixelPos & 31);
                  int pixelPos = y * imageWidth + x;
                  int dst = imageData[pixelPos];
                  imageData[pixelPos] = (colorARGB & -16711936) + ((dst & -16711936) * inverseAlpha >>> 8) & -16711936 | (colorARGB & 16711935) + ((dst & 16711935) * inverseAlpha >>> 8) & 16711935;
                  if (x - 1 >= this.clipX1) {
                     this.enqueueOutlinePixel(nextGroupIndexX, x - 1, y);
                  }

                  if (x + 1 < this.clipX2) {
                     this.enqueueOutlinePixel(nextGroupIndexX, x + 1, y);
                  }

                  if (y - 1 >= this.clipY1) {
                     this.enqueueOutlinePixel(nextGroupIndexY, x, y - 1);
                  }

                  if (y + 1 < this.clipY2) {
                     this.enqueueOutlinePixel(nextGroupIndexY, x, y + 1);
                  }
               }
            }

            this.outlinePixelsBlockBuffer.freeBlock(block);
            int var10002 = this.outlinePixelsBlockIndicesLengths[groupIndex]--;
         }
      }

   }

   private void drawModelOutline(WorldView wv, Model model, int localX, int localY, int localZ, int orientation, int outlineWidth, Color color, int feather) {
      if (outlineWidth > 0 && color.getAlpha() != 0 && model != null) {
         if (outlineWidth > 50) {
            outlineWidth = 50;
         }

         if (feather < 0) {
            feather = 0;
         } else if (feather > 4) {
            feather = 4;
         }

         this.croppedX1 = Integer.MAX_VALUE;
         this.croppedX2 = Integer.MIN_VALUE;
         this.croppedY1 = Integer.MAX_VALUE;
         this.croppedY2 = Integer.MIN_VALUE;
         this.clipX1 = this.client.getViewportXOffset();
         this.clipY1 = this.client.getViewportYOffset();
         this.clipX2 = this.client.getViewportWidth() + this.clipX1;
         this.clipY2 = this.client.getViewportHeight() + this.clipY1;
         if (this.projectVertices(wv, model, localX, localY, localZ, orientation)) {
            this.croppedX1 = Math.max(this.croppedX1 - outlineWidth, this.clipX1);
            this.croppedX2 = Math.min(this.croppedX2 + outlineWidth, this.clipX2);
            this.croppedX2 += ~(this.croppedX2 - this.croppedX1 - 1) & 31;
            this.croppedY1 = Math.max(this.croppedY1 - outlineWidth, this.clipY1);
            this.croppedY2 = Math.min(this.croppedY2 + outlineWidth, this.clipY2);
            this.croppedWidth = this.croppedX2 - this.croppedX1;
            this.croppedHeight = this.croppedY2 - this.croppedY1;
            this.resetVisited(this.croppedWidth * this.croppedHeight);
            this.simulateModelRasterizationForOutline(model);
            boolean directWrite = color.getAlpha() == 255 && outlineWidth <= 10 && (feather == 0 || outlineWidth == 1);
            if (directWrite) {
               this.ensureDistanceDeltasCreated(outlineWidth);
            } else {
               this.outlineArrayWidth = outlineWidth + 2;
               this.initializeOutlineBuffers();
            }

            try {
               this.processInitialOutlinePixels(directWrite, color, outlineWidth);
               if (!directWrite) {
                  this.processOutlinePixelQueue(outlineWidth, color, feather);
               }
            } finally {
               this.freeAllBlockMemory();
            }

         }
      }
   }

   public void drawOutline(Actor actor, int outlineWidth, Color color, int feather) {
      LocalPoint lp = actor.getLocalLocation();
      if (lp != null) {
         WorldView wv = actor.getWorldView();
         this.drawModelOutline(wv, actor.getModel(), lp.getX(), lp.getY(), Perspective.getFootprintTileHeight(this.client, lp, wv.getPlane(), actor.getFootprintSize()) - actor.getAnimationHeightOffset(), actor.getCurrentOrientation(), outlineWidth, color, feather);
      }

   }

   private void drawOutline(GameObject gameObject, int outlineWidth, Color color, int feather) {
      Renderable renderable = gameObject.getRenderable();
      if (renderable != null) {
         Model model = renderable instanceof Model ? (Model)renderable : renderable.getModel();
         if (model != null) {
            this.drawModelOutline(gameObject.getWorldView(), model, gameObject.getX(), gameObject.getY(), gameObject.getZ() - renderable.getAnimationHeightOffset(), gameObject.getModelOrientation(), outlineWidth, color, feather);
         }
      }

   }

   private void drawOutline(GroundObject groundObject, int outlineWidth, Color color, int feather) {
      Renderable renderable = groundObject.getRenderable();
      if (renderable != null) {
         Model model = renderable instanceof Model ? (Model)renderable : renderable.getModel();
         if (model != null) {
            this.drawModelOutline(groundObject.getWorldView(), model, groundObject.getX(), groundObject.getY(), groundObject.getZ() - renderable.getAnimationHeightOffset(), 0, outlineWidth, color, feather);
         }
      }

   }

   private void drawOutline(ItemLayer itemLayer, int outlineWidth, Color color, int feather) {
      Renderable bottomRenderable = itemLayer.getBottom();
      if (bottomRenderable != null) {
         Model model = bottomRenderable instanceof Model ? (Model)bottomRenderable : bottomRenderable.getModel();
         if (model != null) {
            this.drawModelOutline(itemLayer.getWorldView(), model, itemLayer.getX(), itemLayer.getY(), itemLayer.getZ() - itemLayer.getHeight(), 0, outlineWidth, color, feather);
         }
      }

      Renderable middleRenderable = itemLayer.getMiddle();
      if (middleRenderable != null) {
         Model model = middleRenderable instanceof Model ? (Model)middleRenderable : middleRenderable.getModel();
         if (model != null) {
            this.drawModelOutline(itemLayer.getWorldView(), model, itemLayer.getX(), itemLayer.getY(), itemLayer.getZ() - itemLayer.getHeight(), 0, outlineWidth, color, feather);
         }
      }

      Renderable topRenderable = itemLayer.getTop();
      if (topRenderable != null) {
         Model model = topRenderable instanceof Model ? (Model)topRenderable : topRenderable.getModel();
         if (model != null) {
            this.drawModelOutline(itemLayer.getWorldView(), model, itemLayer.getX(), itemLayer.getY(), itemLayer.getZ() - itemLayer.getHeight(), 0, outlineWidth, color, feather);
         }
      }

   }

   private void drawOutline(DecorativeObject decorativeObject, int outlineWidth, Color color, int feather) {
      Renderable renderable1 = decorativeObject.getRenderable();
      if (renderable1 != null) {
         Model model = renderable1 instanceof Model ? (Model)renderable1 : renderable1.getModel();
         if (model != null) {
            this.drawModelOutline(decorativeObject.getWorldView(), model, decorativeObject.getX() + decorativeObject.getXOffset(), decorativeObject.getY() + decorativeObject.getYOffset(), decorativeObject.getZ() - renderable1.getAnimationHeightOffset(), 0, outlineWidth, color, feather);
         }
      }

      Renderable renderable2 = decorativeObject.getRenderable2();
      if (renderable2 != null) {
         Model model = renderable2 instanceof Model ? (Model)renderable2 : renderable2.getModel();
         if (model != null) {
            this.drawModelOutline(decorativeObject.getWorldView(), model, decorativeObject.getX() + decorativeObject.getXOffset2(), decorativeObject.getY() + decorativeObject.getYOffset2(), decorativeObject.getZ() - renderable2.getAnimationHeightOffset(), 0, outlineWidth, color, feather);
         }
      }

   }

   private void drawOutline(WallObject wallObject, int outlineWidth, Color color, int feather) {
      Renderable renderable1 = wallObject.getRenderable1();
      if (renderable1 != null) {
         Model model = renderable1 instanceof Model ? (Model)renderable1 : renderable1.getModel();
         if (model != null) {
            this.drawModelOutline(wallObject.getWorldView(), model, wallObject.getX(), wallObject.getY(), wallObject.getZ() - renderable1.getAnimationHeightOffset(), 0, outlineWidth, color, feather);
         }
      }

      Renderable renderable2 = wallObject.getRenderable2();
      if (renderable2 != null) {
         Model model = renderable2 instanceof Model ? (Model)renderable2 : renderable2.getModel();
         if (model != null) {
            this.drawModelOutline(wallObject.getWorldView(), model, wallObject.getX(), wallObject.getY(), wallObject.getZ() - renderable2.getAnimationHeightOffset(), 0, outlineWidth, color, feather);
         }
      }

   }

   public void drawOutline(TileObject tileObject, int outlineWidth, Color color, int feather) {
      if (tileObject instanceof GameObject) {
         this.drawOutline((GameObject)tileObject, outlineWidth, color, feather);
      } else if (tileObject instanceof GroundObject) {
         this.drawOutline((GroundObject)tileObject, outlineWidth, color, feather);
      } else if (tileObject instanceof ItemLayer) {
         this.drawOutline((ItemLayer)tileObject, outlineWidth, color, feather);
      } else if (tileObject instanceof DecorativeObject) {
         this.drawOutline((DecorativeObject)tileObject, outlineWidth, color, feather);
      } else if (tileObject instanceof WallObject) {
         this.drawOutline((WallObject)tileObject, outlineWidth, color, feather);
      }

   }

   public void drawOutline(ItemLayer layer, TileItem item, int outlineWidth, Color color, int feather) {
      Model model = item.getModel();
      if (model != null) {
         this.drawModelOutline(layer.getWorldView(), model, layer.getX(), layer.getY(), layer.getZ() - layer.getHeight(), 0, outlineWidth, color, feather);
      }

   }

   public void drawOutline(GraphicsObject graphicsObject, int outlineWidth, Color color, int feather) {
      LocalPoint lp = graphicsObject.getLocation();
      if (lp != null) {
         Model model = graphicsObject.getModel();
         if (model != null) {
            this.drawModelOutline(graphicsObject.getWorldView(), model, lp.getX(), lp.getY(), graphicsObject.getZ() - graphicsObject.getAnimationHeightOffset(), 0, outlineWidth, color, feather);
         }
      }

   }

   public void drawOutline(RuneLiteObject runeLiteObject, int outlineWidth, Color color, int feather) {
      LocalPoint lp = runeLiteObject.getLocation();
      if (lp != null) {
         Model model = runeLiteObject.getModel();
         if (model != null) {
            int worldView = runeLiteObject.getWorldView();
            WorldView wv = this.client.getWorldView(worldView);
            if (wv != null) {
               this.drawModelOutline(wv, model, lp.getX(), lp.getY(), runeLiteObject.getZ(), runeLiteObject.getOrientation(), outlineWidth, color, feather);
            }
         }
      }

   }

   private static class PixelDistanceGroupIndex {
      private final double distance;
      private final int distanceGroupIndex;
      private final double alphaMultiply;

      public PixelDistanceGroupIndex(double distance, int distanceGroupIndex, double alphaMultiply) {
         this.distance = distance;
         this.distanceGroupIndex = distanceGroupIndex;
         this.alphaMultiply = alphaMultiply;
      }

      private double getDistance() {
         return this.distance;
      }
   }

   private static class PixelDistanceDelta {
      private final int dx;
      private final int dy;

      public PixelDistanceDelta(int dx, int dy) {
         this.dx = dx;
         this.dy = dy;
      }
   }
}
