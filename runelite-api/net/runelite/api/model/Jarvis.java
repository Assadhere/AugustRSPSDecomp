package net.runelite.api.model;

import java.util.List;
import net.runelite.api.Point;
import net.runelite.api.geometry.SimplePolygon;

public class Jarvis {
   /** @deprecated */
   @Deprecated
   public static List<Point> convexHull(List<Point> points) {
      int[] xs = new int[points.size()];
      int[] ys = new int[xs.length];

      for(int i = 0; i < xs.length; ++i) {
         Point p = (Point)points.get(i);
         xs[i] = p.getX();
         ys[i] = p.getY();
      }

      SimplePolygon poly = convexHull(xs, ys);
      return poly == null ? null : poly.toRuneLitePointList();
   }

   public static SimplePolygon convexHull(int[] xs, int[] ys) {
      int length = xs.length;
      int left = 0;

      int current;
      for(current = 0; left < length; ++left) {
         if (xs[left] == Integer.MIN_VALUE) {
            ++current;
            ++left;
            break;
         }
      }

      for(; left < length; ++left) {
         if (xs[left] == Integer.MIN_VALUE) {
            ++current;
         } else {
            xs[left - current] = xs[left];
            ys[left - current] = ys[left];
         }
      }

      length -= current;
      if (length < 3) {
         return null;
      } else {
         left = findLeftMost(xs, ys, length);
         current = left;
         SimplePolygon out = new SimplePolygon(new int[16], new int[16], 0);

         do {
            int cx = xs[current];
            int cy = ys[current];
            out.pushRight(cx, cy);
            if (out.size() > length) {
               return null;
            }

            int next = 0;
            int nx = xs[next];
            int ny = ys[next];

            for(int i = 1; i < length; ++i) {
               long cp = crossProduct(cx, cy, xs[i], ys[i], nx, ny);
               if (cp > 0L || cp == 0L && square(cx - xs[i]) + square(cy - ys[i]) > square(cx - nx) + square(cy - ny)) {
                  next = i;
                  nx = xs[next];
                  ny = ys[next];
               }
            }

            current = next;
         } while(current != left);

         return out;
      }
   }

   private static int square(int x) {
      return x * x;
   }

   private static int findLeftMost(int[] xs, int[] ys, int length) {
      int idx = 0;
      int x = xs[idx];
      int y = ys[idx];

      for(int i = 1; i < length; ++i) {
         int ix = xs[i];
         if (ix < x || ix == x && ys[i] < y) {
            idx = i;
            x = xs[idx];
            y = ys[idx];
         }
      }

      return idx;
   }

   private static long crossProduct(int px, int py, int qx, int qy, int rx, int ry) {
      long val = (long)(qy - py) * (long)(rx - qx) - (long)(qx - px) * (long)(ry - qy);
      return val;
   }
}
