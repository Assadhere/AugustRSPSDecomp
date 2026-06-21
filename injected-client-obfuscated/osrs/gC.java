package osrs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class gC {
   public static byte[][] a = new byte[100][];
   public static byte[][] b = new byte[50][];
   public static ArrayList c = new ArrayList();
   public static final Object d = new Object();
   public static int e = 0;
   public static int f = 0;
   public static int g = 0;
   public static int h = 0;
   public static int i = 1000;
   public static int j = 250;
   public static int k = 100;
   public static int l = 50;
   public static byte[][] m = new byte[1000][];
   public static byte[][] n = new byte[250][];

   public static void a(int[] var0, int[] var1) {
      if (var0 != null && var1 != null) {
         bo.dL = var0;
         bo.bX = new int[var0.length];
         bo.es = new byte[var0.length][][];

         for(int var2 = 0; var2 < bo.dL.length; ++var2) {
            bo.es[var2] = new byte[var1[var2]][];
            c.add(var0[var2]);
         }

         Collections.sort(c);
      } else {
         bo.dL = null;
         bo.bX = null;
         bo.es = (byte[][][])null;
         c.clear();
         c.add(100);
         c.add(5000);
         c.add(10000);
         c.add(30000);
      }

   }

   public static void a(byte[] var0) {
      synchronized(d) {
         if (var0.length == 100 && e < i) {
            m[++e - 1] = var0;
         } else if (var0.length == 5000 && f < j) {
            n[++f - 1] = var0;
         } else if (var0.length == 10000 && g < k) {
            a[++g - 1] = var0;
         } else if (var0.length == 30000 && h < l) {
            b[++h - 1] = var0;
         } else if (bo.es != null) {
            for(int var2 = 0; var2 < bo.dL.length; ++var2) {
               if (bo.dL[var2] == var0.length && bo.bX[var2] < bo.es[var2].length) {
                  bo.es[var2][bo.bX[var2]++] = var0;
                  return;
               }
            }
         }

      }
   }

   static {
      new HashMap();
      c.clear();
      c.add(100);
      c.add(5000);
      c.add(10000);
      c.add(30000);
   }
}
