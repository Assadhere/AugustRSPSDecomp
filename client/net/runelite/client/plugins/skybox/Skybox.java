package net.runelite.client.plugins.skybox;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class Skybox {
   private static final double SQRT2 = Math.sqrt(2.0);
   private static final double BLEND_DISTRIBUTION = 3.075;
   private static final int BLEND_RADIUS = 5;
   private static final int MAX_BLEND = 13;
   private static final int PLANE_ALL = 15;
   private static final Pattern PATTERN = Pattern.compile("^[ \\t]*(?<expr>//.*$|m[ \\t]*(?<mrx>[0-9]+)[ \\t]+(?<mry>[0-9]+)|r[ \\t]*(?<rx>[0-9]+)[ \\t]+(?<ry>[0-9]+)|R[ \\t]*(?<rx1>[0-9]+)[ \\t]+(?<ry1>[0-9]+)[ \\t]+(?<rx2>[0-9]+)[ \\t]+(?<ry2>[0-9]+)|c[ \\t]*(?<cx>[0-9-]+)[ \\t]+(?<cy>[0-9-]+)|C[ \\t]*(?<cx1>[0-9-]+)[ \\t]+(?<cy1>[0-9-]+)[ \\t]+(?<cx2>[0-9-]+)[ \\t]+(?<cy2>[0-9-]+)|#[ \\t]*(?<color>[0-9a-fA-F]{6}|[0-9a-fA-F]{3})|p[ \\t]*(?<plane>all|0?[ \\t]*1?[ \\t]*2?[ \\t]*3?)|b[ \\t]*(?<blend>[0-9]+)|bounds[ \\t]+(?<bx1>[0-9]+)[ \\t]+(?<by1>[0-9]+)[ \\t]+(?<bx2>[0-9]+)[ \\t]+(?<by2>[0-9]+))[ \\t]*");
   private final int[] chunks;
   private final int[] planeOverrides;
   private final int x1;
   private final int y1;
   private final int x2;
   private final int y2;
   private final int stride;

   public Skybox(InputStream is, String filename) throws IOException {
      this((Reader)(new InputStreamReader(is, StandardCharsets.UTF_8)), filename);
   }

   public Skybox(Reader reader, String filename) throws IOException {
      int[] chunks = null;
      int[] planeOverrides = new int[64];
      int planeOverrideEnd = 0;
      int x1 = 0;
      int y1 = 0;
      int x2 = 0;
      int y2 = 0;
      int stride = 0;
      BufferedReader br = new BufferedReader(reader);
      int lineNo = 1;
      int color = 0;
      int plane = 15;
      int rx1 = 0;
      int ry1 = 0;
      int rx2 = 0;
      int ry2 = 0;

      String line;
      try {
         for(Matcher m = PATTERN.matcher(""); (line = br.readLine()) != null; ++lineNo) {
            m.reset(line);
            int end = 0;

            label162:
            while(end < line.length()) {
               m.region(end, line.length());
               if (!m.find()) {
                  throw new IllegalArgumentException("Unexpected: \"" + line.substring(end) + "\" (" + filename + ":" + lineNo + ")");
               }

               end = m.end();
               String expr = m.group("expr");
               if (expr != null && expr.length() > 0 && !expr.startsWith("//")) {
                  if (chunks == null) {
                     if (!expr.startsWith("bounds")) {
                        throw new IllegalArgumentException("Expected bounds (" + filename + ":" + lineNo + ")");
                     }

                     x1 = Integer.parseInt(m.group("bx1")) * 8;
                     y1 = Integer.parseInt(m.group("by1")) * 8;
                     x2 = (Integer.parseInt(m.group("bx2")) + 1) * 8;
                     y2 = (Integer.parseInt(m.group("by2")) + 1) * 8;
                     stride = x2 - x1;
                     chunks = new int[stride * (y2 - y1)];
                     Arrays.fill(chunks, -1);
                  } else {
                     char cha = expr.charAt(0);
                     int cx1;
                     switch (cha) {
                        case '#':
                           String sColor = m.group("color");
                           int scolor = Integer.parseInt(sColor, 16);
                           int cr;
                           int cg;
                           int cb;
                           if (sColor.length() == 3) {
                              cr = scolor >> 8 & 15;
                              cr |= cr << 4;
                              cg = scolor >> 4 & 15;
                              cg |= cg << 4;
                              cb = scolor & 15;
                              cb |= cb << 4;
                           } else {
                              cr = scolor >> 16 & 255;
                              cg = scolor >> 8 & 255;
                              cb = scolor & 255;
                           }

                           byte cco = (byte)(cb - cr);
                           byte tmp = (byte)(cr + (cco >> 1));
                           byte ccg = (byte)(tmp - cg);
                           byte cy = (byte)(cg + (ccg >> 1));
                           color = color & -16777216 | (cy & 255) << 16 | (cco & 255) << 8 | ccg & 255;
                           break;
                        case 'R':
                        case 'r':
                           if (cha == 'r') {
                              rx2 = rx1 = Integer.parseInt(m.group("rx"));
                              ry2 = ry1 = Integer.parseInt(m.group("ry"));
                           } else {
                              rx1 = Integer.parseInt(m.group("rx1"));
                              ry1 = Integer.parseInt(m.group("ry1"));
                              rx2 = Integer.parseInt(m.group("rx2"));
                              ry2 = Integer.parseInt(m.group("ry2"));
                           }
                        case 'C':
                        case 'c':
                           cx1 = rx1 * 8;
                           int cy1 = ry1 * 8;
                           int cx2 = rx2 * 8 + 7;
                           int cy2 = ry2 * 8 + 7;
                           if (cha == 'c') {
                              cx2 = cx1 += Integer.parseInt(m.group("cx"));
                              cy2 = cy1 += Integer.parseInt(m.group("cy"));
                           } else if (cha == 'C') {
                              cx2 = cx1 + Integer.parseInt(m.group("cx2"));
                              cy2 = cy1 + Integer.parseInt(m.group("cy2"));
                              cx1 += Integer.parseInt(m.group("cx1"));
                              cy1 += Integer.parseInt(m.group("cy1"));
                           }

                           if (cx1 >= x1 && cy1 >= y1 && cx2 < x2 && cy2 < y2) {
                              if (cx1 <= cx2 && cy1 <= cy2) {
                                 int y = cy1;

                                 while(true) {
                                    if (y > cy2) {
                                       continue label162;
                                    }

                                    int yoffset = stride * (y - y1);

                                    for(int x = cx1; x <= cx2; ++x) {
                                       int offset = x - x1 + yoffset;
                                       if (plane == 15) {
                                          chunks[offset] = color;
                                       } else {
                                          int ocv = chunks[offset];
                                          int poptr;
                                          int i;
                                          if ((ocv & Integer.MIN_VALUE) != 0 && ocv != -1) {
                                             poptr = ocv & Integer.MAX_VALUE;
                                          } else {
                                             poptr = planeOverrideEnd;
                                             planeOverrideEnd += 4;
                                             if (planeOverrideEnd > planeOverrides.length) {
                                                planeOverrides = Arrays.copyOf(planeOverrides, planeOverrideEnd + 64);
                                             }

                                             chunks[offset] = poptr | Integer.MIN_VALUE;

                                             for(i = 0; i < 4; ++i) {
                                                planeOverrides[poptr + i] = ocv;
                                             }
                                          }

                                          for(i = 0; i < 4; ++i) {
                                             if ((plane & 1 << i) != 0) {
                                                planeOverrides[poptr + i] = color;
                                             }
                                          }
                                       }
                                    }

                                    ++y;
                                 }
                              }

                              throw new IllegalArgumentException("First coord must be before second (" + filename + ":" + lineNo + ")");
                           }

                           throw new IllegalArgumentException("Coordinate out of bounds (" + filename + ":" + lineNo + ")");
                        case 'b':
                           int iblend = Integer.parseInt(m.group("blend"));
                           if (iblend < 0) {
                              throw new IllegalArgumentException("Blend must be >=0 (" + filename + ":" + lineNo + ")");
                           }

                           if (iblend > 13) {
                              throw new IllegalArgumentException("Blend must be <= 13 (" + filename + ":" + lineNo + ")");
                           }

                           color = color & 16777215 | iblend << 24;
                           break;
                        case 'm':
                           rx2 = rx1 = Integer.parseInt(m.group("mrx"));
                           ry2 = ry1 = Integer.parseInt(m.group("mry"));
                           break;
                        case 'p':
                           String planes = m.group("plane");
                           if (!"all".equals(planes)) {
                              plane = 0;

                              for(cx1 = 0; cx1 < planes.length(); ++cx1) {
                                 plane |= 1 << planes.charAt(cx1) - 48;
                              }
                           } else {
                              plane = 15;
                           }
                     }
                  }
               }
            }
         }
      } catch (NumberFormatException var46) {
         NumberFormatException ex = var46;
         throw new IllegalArgumentException("Expected number (" + filename + ":" + lineNo + ")", ex);
      }

      if (chunks == null) {
         throw new IllegalArgumentException(filename + ": no data");
      } else {
         this.chunks = chunks;
         this.planeOverrides = planeOverrides;
         this.stride = stride;
         this.x1 = x1;
         this.y1 = y1;
         this.x2 = x2;
         this.y2 = y2;
      }
   }

   private int chunkData(int cx, int cy, int plane, ChunkMapper chunkMapper) {
      int cv;
      if (chunkMapper != null) {
         cv = chunkMapper.getTemplateChunk(cx, cy, plane);
         if (cv == -1) {
            return -1;
         }

         cy = cv >> 3 & 2047;
         cx = cv >> 14 & 1023;
         plane = cv >> 24 & 3;
      }

      if (cx < this.x1) {
         cx = this.x1;
      }

      if (cx >= this.x2) {
         cx = this.x2 - 1;
      }

      if (cy < this.y1) {
         cy = this.y1;
      }

      if (cy >= this.y2) {
         cy = this.y2 - 1;
      }

      cv = this.chunks[this.stride * (cy - this.y1) + (cx - this.x1)];
      if (cv == -1) {
         return -1;
      } else {
         if ((cv & Integer.MIN_VALUE) != 0) {
            cv = this.planeOverrides[cv & Integer.MAX_VALUE | plane];
         }

         return cv;
      }
   }

   public int getColorForPoint(double x, double y, int px, int py, int plane, double brightness, ChunkMapper chunkMapper) {
      x /= 8.0;
      y /= 8.0;
      int centerChunkData = this.chunkData(px / 8, py / 8, plane, chunkMapper);
      if (centerChunkData == -1) {
         return 0;
      } else {
         double t = 0.0;
         double ty = 0.0;
         double tco = 0.0;
         double tcg = 0.0;
         int xmin = (int)(x - 5.0);
         int xmax = (int)Math.ceil(x + 5.0);
         int ymin = (int)(y - 5.0);
         int ymax = (int)Math.ceil(y + 5.0);

         for(int ucx = xmin; ucx < xmax; ++ucx) {
            for(int ucy = ymin; ucy <= ymax; ++ucy) {
               int val = this.chunkData(ucx, ucy, plane, chunkMapper);
               if (val != -1) {
                  double sigma = ((double)(val >>> 24) + 0.125) / 8.0;
                  double minDist = 1.0 + sigma * 3.075;
                  double dxl = (double)ucx - x;
                  double dxh = dxl + 1.0;
                  if (!(dxl < -minDist) && !(dxl > minDist)) {
                     double dyl = (double)ucy - y;
                     double dyh = dyl + 1.0;
                     if (!(dyl < -minDist) && !(dyh > minDist)) {
                        double erfdivc = sigma * SQRT2;
                        double m = (this.erf(dxl / erfdivc) - this.erf(dxh / erfdivc)) * (this.erf(dyl / erfdivc) - this.erf(dyh / erfdivc));
                        double vy = (double)(val >>> 16 & 255) / 255.0;
                        double vco = (double)((byte)(val >>> 8)) / 128.0;
                        double vcg = (double)((byte)val) / 128.0;
                        ty += vy * m;
                        tco += vco * m;
                        tcg += vcg * m;
                        t += m;
                     }
                  }
               }
            }
         }

         byte ay = (byte)((int)Math.min(Math.max(Math.round(ty / t * 255.0), 0L), 255L));
         byte aco = (byte)((int)Math.min(Math.max(Math.round(tco * 128.0 / t), -128L), 127L));
         byte acg = (byte)((int)Math.min(Math.max(Math.round(tcg * 128.0 / t), -128L), 127L));
         int g = ay - (acg >> 1) & 255;
         int tmp = g + acg & 255;
         int r = tmp - (aco >> 1) & 255;
         int b = r + aco & 255;
         float[] hsb = Color.RGBtoHSB(r, g, b, (float[])null);
         hsb[2] = (float)Math.pow((double)hsb[2], brightness);
         return 16777215 & Color.HSBtoRGB(hsb[0], hsb[1], hsb[2]);
      }
   }

   private double erf(double x) {
      double ax = Math.abs(x);
      double t = 1.0 / (1.0 + ax * 0.3275911);
      double y = 1.0 - ((((1.061405429 * t - 1.453152027) * t + 1.421413741) * t - 0.284496736) * t + 0.254829592) * t * Math.exp(-ax * ax);
      return Math.copySign(y, x);
   }

   BufferedImage render(double resolution, int line, int plane, ChunkMapper chunkMapper) {
      int w = (int)((double)((this.x2 - this.x1) * 8) * resolution);
      int h = (int)((double)((this.y2 - this.y1) * 8) * resolution);
      BufferedImage img = new BufferedImage(w, h, 2);
      int lineEvery = line <= 0 ? Integer.MAX_VALUE : (int)((double)line * resolution);

      for(int y = 0; y < h; ++y) {
         for(int x = 0; x < w; ++x) {
            int color;
            if (x % lineEvery != 0 && y % lineEvery != 0) {
               double fx = (double)(this.x1 * 8) + (double)x / resolution;
               double fy = (double)(this.y1 * 8) + (double)y / resolution;
               color = this.getColorForPoint(fx, fy, (int)fx, (int)fy, plane, 0.8, chunkMapper);
            } else {
               color = 16777215;
            }

            img.setRGB(x, h - 1 - y, color | -16777216);
         }
      }

      return img;
   }

   @FunctionalInterface
   public interface ChunkMapper {
      int getTemplateChunk(int var1, int var2, int var3);
   }
}
