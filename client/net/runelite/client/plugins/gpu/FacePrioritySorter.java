package net.runelite.client.plugins.gpu;

import io.sentry.Breadcrumb;
import io.sentry.Sentry;
import io.sentry.SentryLevel;
import java.nio.IntBuffer;
import java.util.Arrays;
import net.runelite.api.Model;
import net.runelite.api.Perspective;
import net.runelite.api.Projection;

class FacePrioritySorter {
   static final int[] distances = new int[6500];
   static final char[] zsortHead = new char[6000];
   static final char[] zsortTail = new char[6000];
   static final char[] zsortNext = new char[16384];
   private static final float[] modelProjectedX = new float[6500];
   private static final float[] modelProjectedY = new float[6500];
   static final float[] modelLocalX = new float[6500];
   static final float[] modelLocalY = new float[6500];
   static final float[] modelLocalZ = new float[6500];
   static final int[] numOfPriority = new int[12];
   private static final int[] eq10 = new int[16384];
   private static final int[] eq11 = new int[16384];
   private static final int[] lt10 = new int[12];
   static final int[][] orderedFaces = new int[12][16384];
   private static final int[] vertexBuffer = new int[294912];
   static final int MAX_VERTEX_COUNT = 6500;
   static final int MAX_FACE_COUNT = 16384;
   static final int MAX_DIAMETER = 6000;
   private static final int MAX_FACES_PER_PRIORITY = 16384;
   private static final int FACE_SIZE = 18;
   private static final long TEMP_MODEL_SORT_LOG_INTERVAL_MILLIS = 60000L;
   private static long tempModelSortBreadcrumbMillis;
   private static int tempModelSortSuppressedLogs;
   private static int tempModelSortSuppressedMaxVertices;
   private static int tempModelSortSuppressedMaxFaces;
   private static int tempModelSortSuppressedMaxDiameter;
   private final SceneUploader sceneUploader;

   FacePrioritySorter(SceneUploader sceneUploader) {
      this.sceneUploader = sceneUploader;
   }

   int uploadSortedModel(Projection proj, Model model, int orientation, int x, int y, int z, IntBuffer opaqueBuffer, IntBuffer alphaBuffer) {
      int vertexCount = model.getVerticesCount();
      int modelFaceCount = model.getFaceCount();
      if (vertexCount <= 6500 && modelFaceCount <= 16384) {
         float[] verticesX = model.getVerticesX();
         float[] verticesY = model.getVerticesY();
         float[] verticesZ = model.getVerticesZ();
         int faceCount = modelFaceCount;
         int[] indices1 = model.getFaceIndices1();
         int[] indices2 = model.getFaceIndices2();
         int[] indices3 = model.getFaceIndices3();
         int[] faceColors1 = model.getFaceColors1();
         int[] faceColors2 = model.getFaceColors2();
         int[] faceColors3 = model.getFaceColors3();
         byte[] faceRenderPriorities = model.getFaceRenderPriorities();
         short[] faceTextures = model.getFaceTextures();
         byte[] transparencies = model.getFaceTransparencies();
         byte[] bias = model.getFaceBias();
         float orientSine = 0.0F;
         float orientCosine = 0.0F;
         if (orientation != 0) {
            orientSine = (float)Perspective.SINE[orientation] / 65536.0F;
            orientCosine = (float)Perspective.COSINE[orientation] / 65536.0F;
         }

         float[] p = proj.project((float)x, (float)y, (float)z);
         int zero = (int)p[2];

         int diameter;
         for(diameter = 0; diameter < vertexCount; ++diameter) {
            float vertexX = verticesX[diameter];
            float vertexY = verticesY[diameter];
            float vertexZ = verticesZ[diameter];
            if (orientation != 0) {
               float x0 = vertexX;
               vertexX = vertexZ * orientSine + x0 * orientCosine;
               vertexZ = vertexZ * orientCosine - x0 * orientSine;
            }

            vertexX += (float)x;
            vertexY += (float)y;
            vertexZ += (float)z;
            modelLocalX[diameter] = vertexX;
            modelLocalY[diameter] = vertexY;
            modelLocalZ[diameter] = vertexZ;
            p = proj.project(vertexX, vertexY, vertexZ);
            if (p[2] < 50.0F) {
               return 0;
            }

            modelProjectedX[diameter] = p[0] / p[2];
            modelProjectedY[diameter] = p[1] / p[2];
            distances[diameter] = (int)p[2] - zero;
         }

         diameter = model.getDiameter();
         int radius = model.getRadius();
         if (diameter >= 6000) {
            return 0;
         } else {
            Arrays.fill(zsortHead, 0, diameter, '\uffff');
            Arrays.fill(zsortTail, 0, diameter, '\uffff');
            int minFz = diameter;
            int maxFz = 0;

            int avg12;
            int avg34;
            int avg68;
            int priNum;
            int faceIdx;
            int face;
            for(char faceIdx = 0; faceIdx < faceCount; ++faceIdx) {
               if (faceColors3[faceIdx] != -2) {
                  avg12 = indices1[faceIdx];
                  avg34 = indices2[faceIdx];
                  avg68 = indices3[faceIdx];
                  float aX = modelProjectedX[avg12];
                  float aY = modelProjectedY[avg12];
                  float bX = modelProjectedX[avg34];
                  float bY = modelProjectedY[avg34];
                  float cX = modelProjectedX[avg68];
                  float cY = modelProjectedY[avg68];
                  if ((aX - bX) * (cY - bY) - (cX - bX) * (aY - bY) > 0.0F) {
                     priNum = radius + (distances[avg12] + distances[avg34] + distances[avg68]) / 3;
                     if (priNum >= 0 && priNum < diameter) {
                        int su0;
                        if (zsortTail[priNum] == '\uffff') {
                           zsortHead[priNum] = zsortTail[priNum] = faceIdx;
                           zsortNext[faceIdx] = '\uffff';
                        } else {
                           su0 = zsortTail[priNum];
                           zsortNext[su0] = faceIdx;
                           zsortNext[faceIdx] = '\uffff';
                           zsortTail[priNum] = faceIdx;
                        }

                        minFz = Math.min(minFz, priNum);
                        maxFz = Math.max(maxFz, priNum);
                        this.sceneUploader.computeFaceUvs(model, faceIdx);
                        su0 = (int)(this.sceneUploader.u0 * 256.0F);
                        faceIdx = (int)(this.sceneUploader.v0 * 256.0F);
                        face = (int)(this.sceneUploader.u1 * 256.0F);
                        int sv1 = (int)(this.sceneUploader.v1 * 256.0F);
                        int su2 = (int)(this.sceneUploader.u2 * 256.0F);
                        int sv2 = (int)(this.sceneUploader.v2 * 256.0F);
                        int color1 = faceColors1[faceIdx];
                        int color2 = faceColors2[faceIdx];
                        int color3 = faceColors3[faceIdx];
                        if (color3 == -1) {
                           color3 = color1;
                           color2 = color1;
                        }

                        if ((faceTextures == null || faceTextures[faceIdx] == -1) && model.getOverrideAmount() > 0) {
                           color1 = SceneUploader.interpolateHSL(color1, model.getOverrideHue(), model.getOverrideSaturation(), model.getOverrideLuminance(), model.getOverrideAmount());
                           color2 = SceneUploader.interpolateHSL(color2, model.getOverrideHue(), model.getOverrideSaturation(), model.getOverrideLuminance(), model.getOverrideAmount());
                           color3 = SceneUploader.interpolateHSL(color3, model.getOverrideHue(), model.getOverrideSaturation(), model.getOverrideLuminance(), model.getOverrideAmount());
                        }

                        int alphaBias = 0;
                        alphaBias |= transparencies != null ? (transparencies[faceIdx] & 255) << 24 : 0;
                        alphaBias |= bias != null ? (bias[faceIdx] & 255) << 16 : 0;
                        int texture = faceTextures != null ? faceTextures[faceIdx] + 1 : 0;
                        int vbOff = faceIdx * 18;
                        vertexBuffer[vbOff++] = Float.floatToIntBits(modelLocalX[avg12]);
                        vertexBuffer[vbOff++] = Float.floatToIntBits(modelLocalY[avg12]);
                        vertexBuffer[vbOff++] = Float.floatToIntBits(modelLocalZ[avg12]);
                        vertexBuffer[vbOff++] = alphaBias | color1;
                        vertexBuffer[vbOff++] = (su0 & '\uffff') << 16 | texture & '\uffff';
                        vertexBuffer[vbOff++] = faceIdx & '\uffff';
                        vertexBuffer[vbOff++] = Float.floatToIntBits(modelLocalX[avg34]);
                        vertexBuffer[vbOff++] = Float.floatToIntBits(modelLocalY[avg34]);
                        vertexBuffer[vbOff++] = Float.floatToIntBits(modelLocalZ[avg34]);
                        vertexBuffer[vbOff++] = alphaBias | color2;
                        vertexBuffer[vbOff++] = (face & '\uffff') << 16 | texture & '\uffff';
                        vertexBuffer[vbOff++] = sv1 & '\uffff';
                        vertexBuffer[vbOff++] = Float.floatToIntBits(modelLocalX[avg68]);
                        vertexBuffer[vbOff++] = Float.floatToIntBits(modelLocalY[avg68]);
                        vertexBuffer[vbOff++] = Float.floatToIntBits(modelLocalZ[avg68]);
                        vertexBuffer[vbOff++] = alphaBias | color3;
                        vertexBuffer[vbOff++] = (su2 & '\uffff') << 16 | texture & '\uffff';
                        vertexBuffer[vbOff++] = sv2 & '\uffff';
                     } else {
                        this.addSortBoundsBreadcrumb(model, faceIdx, priNum, diameter, radius, orientation, x, y, z, avg12, avg34, avg68);
                     }
                  }
               }
            }

            int len = 0;
            char face;
            if (faceRenderPriorities == null) {
               for(avg12 = maxFz; avg12 >= minFz; --avg12) {
                  for(face = zsortHead[avg12]; face != '\uffff'; face = zsortNext[face]) {
                     IntBuffer b = transparencies != null && transparencies[face] != 0 ? alphaBuffer : opaqueBuffer;
                     b.put(vertexBuffer, face * 18, 18);
                  }
               }
            } else {
               Arrays.fill(numOfPriority, 0);
               Arrays.fill(lt10, 0);

               int drawnFaces;
               for(avg12 = maxFz; avg12 >= minFz; --avg12) {
                  for(face = zsortHead[avg12]; face != '\uffff'; face = zsortNext[face]) {
                     avg68 = faceRenderPriorities[face] & 255;
                     if (avg68 < numOfPriority.length) {
                        drawnFaces = numOfPriority[avg68]++;
                        orderedFaces[avg68][drawnFaces] = face;
                        if (avg68 < 10) {
                           int[] var10000 = lt10;
                           var10000[avg68] += avg12;
                        } else if (avg68 == 10) {
                           eq10[drawnFaces] = avg12;
                        } else {
                           eq11[drawnFaces] = avg12;
                        }
                     }
                  }
               }

               avg12 = 0;
               if (numOfPriority[1] > 0 || numOfPriority[2] > 0) {
                  avg12 = (lt10[1] + lt10[2]) / (numOfPriority[1] + numOfPriority[2]);
               }

               avg34 = 0;
               if (numOfPriority[3] > 0 || numOfPriority[4] > 0) {
                  avg34 = (lt10[3] + lt10[4]) / (numOfPriority[3] + numOfPriority[4]);
               }

               avg68 = 0;
               if (numOfPriority[6] > 0 || numOfPriority[8] > 0) {
                  avg68 = (lt10[8] + lt10[6]) / (numOfPriority[8] + numOfPriority[6]);
               }

               drawnFaces = 0;
               int numDynFaces = numOfPriority[10];
               int[] dynFaces = orderedFaces[10];
               int[] dynFaceDistances = eq10;
               if (drawnFaces == numDynFaces) {
                  drawnFaces = 0;
                  numDynFaces = numOfPriority[11];
                  dynFaces = orderedFaces[11];
                  dynFaceDistances = eq11;
               }

               int currFaceDistance;
               if (drawnFaces < numDynFaces) {
                  currFaceDistance = dynFaceDistances[drawnFaces];
               } else {
                  currFaceDistance = -1000;
               }

               int pri;
               for(pri = 0; pri < 10; ++pri) {
                  IntBuffer b;
                  while(pri == 0 && currFaceDistance > avg12) {
                     priNum = dynFaces[drawnFaces++];
                     b = transparencies != null && transparencies[priNum] != 0 ? alphaBuffer : opaqueBuffer;
                     b.put(vertexBuffer, priNum * 18, 18);
                     if (drawnFaces == numDynFaces && dynFaces != orderedFaces[11]) {
                        drawnFaces = 0;
                        numDynFaces = numOfPriority[11];
                        dynFaces = orderedFaces[11];
                        dynFaceDistances = eq11;
                     }

                     if (drawnFaces < numDynFaces) {
                        currFaceDistance = dynFaceDistances[drawnFaces];
                     } else {
                        currFaceDistance = -1000;
                     }
                  }

                  while(pri == 3 && currFaceDistance > avg34) {
                     priNum = dynFaces[drawnFaces++];
                     b = transparencies != null && transparencies[priNum] != 0 ? alphaBuffer : opaqueBuffer;
                     b.put(vertexBuffer, priNum * 18, 18);
                     if (drawnFaces == numDynFaces && dynFaces != orderedFaces[11]) {
                        drawnFaces = 0;
                        numDynFaces = numOfPriority[11];
                        dynFaces = orderedFaces[11];
                        dynFaceDistances = eq11;
                     }

                     if (drawnFaces < numDynFaces) {
                        currFaceDistance = dynFaceDistances[drawnFaces];
                     } else {
                        currFaceDistance = -1000;
                     }
                  }

                  while(pri == 5 && currFaceDistance > avg68) {
                     priNum = dynFaces[drawnFaces++];
                     b = transparencies != null && transparencies[priNum] != 0 ? alphaBuffer : opaqueBuffer;
                     b.put(vertexBuffer, priNum * 18, 18);
                     if (drawnFaces == numDynFaces && dynFaces != orderedFaces[11]) {
                        drawnFaces = 0;
                        numDynFaces = numOfPriority[11];
                        dynFaces = orderedFaces[11];
                        dynFaceDistances = eq11;
                     }

                     if (drawnFaces < numDynFaces) {
                        currFaceDistance = dynFaceDistances[drawnFaces];
                     } else {
                        currFaceDistance = -1000;
                     }
                  }

                  priNum = numOfPriority[pri];
                  int[] priFaces = orderedFaces[pri];

                  for(faceIdx = 0; faceIdx < priNum; ++faceIdx) {
                     face = priFaces[faceIdx];
                     IntBuffer b = transparencies != null && transparencies[face] != 0 ? alphaBuffer : opaqueBuffer;
                     b.put(vertexBuffer, face * 18, 18);
                  }
               }

               while(currFaceDistance != -1000) {
                  pri = dynFaces[drawnFaces++];
                  IntBuffer b = transparencies != null && transparencies[pri] != 0 ? alphaBuffer : opaqueBuffer;
                  b.put(vertexBuffer, pri * 18, 18);
                  if (drawnFaces == numDynFaces && dynFaces != orderedFaces[11]) {
                     drawnFaces = 0;
                     dynFaces = orderedFaces[11];
                     numDynFaces = numOfPriority[11];
                     dynFaceDistances = eq11;
                  }

                  if (drawnFaces < numDynFaces) {
                     currFaceDistance = dynFaceDistances[drawnFaces];
                  } else {
                     currFaceDistance = -1000;
                  }
               }
            }

            return len;
         }
      } else {
         this.addScratchLimitBreadcrumb(model, vertexCount, modelFaceCount);
         return 0;
      }
   }

   private void addScratchLimitBreadcrumb(Model model, int vertexCount, int faceCount) {
      int suppressedLogs = reserveTempModelSortBreadcrumbSlot(vertexCount, faceCount, model.getDiameter());
      if (suppressedLogs >= 0) {
         int suppressedMaxVertices = tempModelSortSuppressedMaxVertices;
         int suppressedMaxFaces = tempModelSortSuppressedMaxFaces;
         int suppressedMaxDiameter = tempModelSortSuppressedMaxDiameter;
         Breadcrumb breadcrumb = new Breadcrumb();
         breadcrumb.setCategory("gpu.temp_model_sort_scratch");
         breadcrumb.setLevel(SentryLevel.WARNING);
         breadcrumb.setMessage("GPU temp model sort scratch limit exceeded");
         breadcrumb.setData("modelIdentity", System.identityHashCode(model));
         breadcrumb.setData("sceneId", model.getSceneId());
         breadcrumb.setData("vertices", vertexCount);
         breadcrumb.setData("vertexScratchLimit", 6500);
         breadcrumb.setData("faces", faceCount);
         breadcrumb.setData("faceScratchLimit", 16384);
         breadcrumb.setData("radius", model.getRadius());
         breadcrumb.setData("diameter", model.getDiameter());
         breadcrumb.setData("suppressedLogsSinceLast", suppressedLogs);
         breadcrumb.setData("suppressedMaxVertices", suppressedMaxVertices);
         breadcrumb.setData("suppressedMaxFaces", suppressedMaxFaces);
         breadcrumb.setData("suppressedMaxDiameter", suppressedMaxDiameter);
         Sentry.addBreadcrumb(breadcrumb);
         Sentry.captureMessage("GPU temp model sort scratch limit exceeded", SentryLevel.WARNING, (scope) -> {
            scope.setTag("gpuTempModelSortFailure", "scratch");
            scope.setExtra("modelIdentity", String.valueOf(System.identityHashCode(model)));
            scope.setExtra("sceneId", String.valueOf(model.getSceneId()));
            scope.setExtra("vertices", String.valueOf(vertexCount));
            scope.setExtra("vertexScratchLimit", String.valueOf(6500));
            scope.setExtra("faces", String.valueOf(faceCount));
            scope.setExtra("faceScratchLimit", String.valueOf(16384));
            scope.setExtra("radius", String.valueOf(model.getRadius()));
            scope.setExtra("diameter", String.valueOf(model.getDiameter()));
            scope.setExtra("suppressedLogsSinceLast", String.valueOf(suppressedLogs));
            scope.setExtra("suppressedMaxVertices", String.valueOf(suppressedMaxVertices));
            scope.setExtra("suppressedMaxFaces", String.valueOf(suppressedMaxFaces));
            scope.setExtra("suppressedMaxDiameter", String.valueOf(suppressedMaxDiameter));
         });
         clearTempModelSortSuppressedPeaks();
      }
   }

   private void addSortBoundsBreadcrumb(Model model, int faceIdx, int distance, int diameter, int radius, int orientation, int x, int y, int z, int v1, int v2, int v3) {
      int suppressedLogs = reserveTempModelSortBreadcrumbSlot(model.getVerticesCount(), model.getFaceCount(), diameter);
      if (suppressedLogs >= 0) {
         int suppressedMaxVertices = tempModelSortSuppressedMaxVertices;
         int suppressedMaxFaces = tempModelSortSuppressedMaxFaces;
         int suppressedMaxDiameter = tempModelSortSuppressedMaxDiameter;
         Breadcrumb breadcrumb = new Breadcrumb();
         breadcrumb.setCategory("gpu.temp_model_sort_bounds");
         breadcrumb.setLevel(SentryLevel.ERROR);
         breadcrumb.setMessage("GPU temp model face sort distance exceeded model diameter");
         breadcrumb.setData("modelIdentity", System.identityHashCode(model));
         breadcrumb.setData("sceneId", model.getSceneId());
         breadcrumb.setData("vertices", model.getVerticesCount());
         breadcrumb.setData("faces", model.getFaceCount());
         breadcrumb.setData("radius", radius);
         breadcrumb.setData("diameter", diameter);
         breadcrumb.setData("faceIdx", faceIdx);
         breadcrumb.setData("distance", distance);
         breadcrumb.setData("orientation", orientation);
         breadcrumb.setData("x", x);
         breadcrumb.setData("y", y);
         breadcrumb.setData("z", z);
         breadcrumb.setData("faceV1", v1);
         breadcrumb.setData("faceV2", v2);
         breadcrumb.setData("faceV3", v3);
         breadcrumb.setData("depthV1", distances[v1]);
         breadcrumb.setData("depthV2", distances[v2]);
         breadcrumb.setData("depthV3", distances[v3]);
         breadcrumb.setData("suppressedLogsSinceLast", suppressedLogs);
         breadcrumb.setData("suppressedMaxVertices", suppressedMaxVertices);
         breadcrumb.setData("suppressedMaxFaces", suppressedMaxFaces);
         breadcrumb.setData("suppressedMaxDiameter", suppressedMaxDiameter);
         Sentry.addBreadcrumb(breadcrumb);
         Sentry.captureMessage("GPU temp model face sort distance exceeded model diameter", SentryLevel.ERROR, (scope) -> {
            scope.setTag("gpuTempModelSortFailure", "bounds");
            scope.setExtra("modelIdentity", String.valueOf(System.identityHashCode(model)));
            scope.setExtra("sceneId", String.valueOf(model.getSceneId()));
            scope.setExtra("vertices", String.valueOf(model.getVerticesCount()));
            scope.setExtra("faces", String.valueOf(model.getFaceCount()));
            scope.setExtra("radius", String.valueOf(radius));
            scope.setExtra("diameter", String.valueOf(diameter));
            scope.setExtra("faceIdx", String.valueOf(faceIdx));
            scope.setExtra("distance", String.valueOf(distance));
            scope.setExtra("orientation", String.valueOf(orientation));
            scope.setExtra("x", String.valueOf(x));
            scope.setExtra("y", String.valueOf(y));
            scope.setExtra("z", String.valueOf(z));
            scope.setExtra("faceV1", String.valueOf(v1));
            scope.setExtra("faceV2", String.valueOf(v2));
            scope.setExtra("faceV3", String.valueOf(v3));
            scope.setExtra("depthV1", String.valueOf(distances[v1]));
            scope.setExtra("depthV2", String.valueOf(distances[v2]));
            scope.setExtra("depthV3", String.valueOf(distances[v3]));
            scope.setExtra("suppressedLogsSinceLast", String.valueOf(suppressedLogs));
            scope.setExtra("suppressedMaxVertices", String.valueOf(suppressedMaxVertices));
            scope.setExtra("suppressedMaxFaces", String.valueOf(suppressedMaxFaces));
            scope.setExtra("suppressedMaxDiameter", String.valueOf(suppressedMaxDiameter));
         });
         clearTempModelSortSuppressedPeaks();
      }
   }

   private static int reserveTempModelSortBreadcrumbSlot(int vertices, int faces, int diameter) {
      long now = System.currentTimeMillis();
      if (tempModelSortBreadcrumbMillis != 0L && now - tempModelSortBreadcrumbMillis < 60000L) {
         ++tempModelSortSuppressedLogs;
         tempModelSortSuppressedMaxVertices = Math.max(tempModelSortSuppressedMaxVertices, vertices);
         tempModelSortSuppressedMaxFaces = Math.max(tempModelSortSuppressedMaxFaces, faces);
         tempModelSortSuppressedMaxDiameter = Math.max(tempModelSortSuppressedMaxDiameter, diameter);
         return -1;
      } else {
         int suppressedLogs = tempModelSortSuppressedLogs;
         tempModelSortSuppressedLogs = 0;
         tempModelSortBreadcrumbMillis = now;
         return suppressedLogs;
      }
   }

   private static void clearTempModelSortSuppressedPeaks() {
      tempModelSortSuppressedMaxVertices = 0;
      tempModelSortSuppressedMaxFaces = 0;
      tempModelSortSuppressedMaxDiameter = 0;
   }
}
