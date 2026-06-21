package osrs;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

public class U {
   public static String a;
   public static hP b = null;
   public static hP c = null;
   public static hP d = null;

   public static void a(String var0, String var1, String var2, int var3, int var4) throws IOException {
      if (var1 != null && !var1.isEmpty()) {
         var0 = var0 + "-" + var1;
      }

      bo.dq = var4;
      bo.cE = var3;

      try {
         bo.dl = System.getProperty("os.name");
      } catch (Exception var32) {
         bo.dl = "Unknown";
      }

      bo.ef = bo.dl.toLowerCase();
      a = null;

      try {
         a = System.getProperty("jagex.userhome");
      } catch (Exception var31) {
      }

      if (a == null) {
         try {
            a = System.getProperty("user.home");
         } catch (Exception var30) {
         }
      }

      if (a != null) {
         a = a + "/";
      }

      try {
         if (bo.ef.startsWith("win")) {
            if (a == null) {
               a = Client.a("USERPROFILE");
            }
         } else if (a == null) {
            a = Client.a("HOME");
         }

         if (a != null) {
            a = a + "/";
         }
      } catch (Exception var29) {
      }

      if (a == null) {
         a = "~/";
      }

      bo.ev = new String[]{"c:/rscache/", "/rscache/", "c:/windows/", "c:/winnt/", "c:/", a, "/tmp/", ""};
      bo.bT = new String[]{".jagex_cache_" + bo.cE, ".file_store_" + bo.cE};
      int var5 = 0;

      label483:
      while(var5 < 4) {
         String var6 = var5 == 0 ? "" : "" + var5;
         bo.bx = new File(a, "jagex_cl_" + var0 + "_" + var2 + var6 + ".dat");
         String var7 = null;
         String var8 = null;
         boolean var9 = false;
         int var13;
         int var14;
         File var38;
         if (bo.bx.exists()) {
            hQ var10 = null;

            try {
               var10 = new hQ(bo.bx, "rw", 10000L);

               aR var11;
               int var12;
               for(var11 = new aR((int)var10.c()); var11.d < var11.c.length; var11.d += var12) {
                  var12 = var10.b(var11.c, var11.d, var11.c.length - var11.d);
                  if (var12 == -1) {
                     throw new IOException();
                  }
               }

               var11.d = 0;
               var13 = var11.b();
               if (var13 < 1 || var13 > 3) {
                  throw new IOException("" + var13);
               }

               var14 = 0;
               if (var13 > 1) {
                  var14 = var11.b();
               }

               if (var13 <= 2) {
                  var7 = var11.n();
                  if (var14 == 1) {
                     var8 = var11.n();
                  }
               } else {
                  var7 = var11.o();
                  if (var14 == 1) {
                     var8 = var11.o();
                  }
               }
            } catch (Exception var34) {
               var34.printStackTrace();
            } finally {
               try {
                  if (var10 != null) {
                     var10.b();
                  }
               } catch (IOException var28) {
               }

            }

            if (var7 != null) {
               var38 = new File(var7);
               if (!var38.exists()) {
                  var7 = null;
               }
            }

            if (var7 != null) {
               var38 = new File(var7, "test.dat");
               if (!a(var38, true)) {
                  var7 = null;
               }
            }
         }

         if (var7 == null && var5 == 0) {
            label456:
            for(int var37 = 0; var37 < bo.bT.length; ++var37) {
               for(int var40 = 0; var40 < bo.ev.length; ++var40) {
                  String var10002 = bo.ev[var40];
                  File var41 = new File(var10002 + bo.bT[var37] + File.separatorChar + var0 + File.separatorChar);
                  if (var41.exists() && a(new File(var41, "test.dat"), true)) {
                     var7 = var41.toString();
                     var9 = true;
                     break label456;
                  }
               }
            }
         }

         if (var7 == null) {
            var7 = a + File.separatorChar + "jagexcache" + var6 + File.separatorChar + var0 + File.separatorChar + var2 + File.separatorChar;
            var9 = true;
         }

         File var39;
         File[] var42;
         if (var8 != null) {
            var39 = new File(var8);
            var38 = new File(var7);

            try {
               var42 = var39.listFiles();
               File[] var44 = var42;

               for(var14 = 0; var14 < var44.length; ++var14) {
                  File var15 = var44[var14];
                  File var16 = new File(var38, var15.getName());
                  boolean var17 = var15.renameTo(var16);
                  if (!var17) {
                     throw new IOException();
                  }
               }
            } catch (Exception var33) {
               var33.printStackTrace();
            }

            var9 = true;
         }

         if (var9) {
            a(new File(var7), (File)null);
         }

         var39 = new File(var7);
         bo.bS = var39;
         if (!bo.bS.exists()) {
            bo.bS.mkdirs();
         }

         File[] var43 = bo.bS.listFiles();
         if (var43 != null) {
            var42 = var43;

            for(var13 = 0; var13 < var42.length; ++var13) {
               File var45 = var42[var13];
               if (!a(var45, false)) {
                  ++var5;
                  continue label483;
               }
            }
         }
         break;
      }

      dT.a(bo.bS);
      a();
      c = new hP(new hQ(dT.a("main_file_cache.dat2"), "rw", 1048576000L), 5200, 0);
      d = new hP(new hQ(dT.a("main_file_cache.idx255"), "rw", 1048576L), 6000, 0);
      bo.ca = new hP[bo.dq];

      for(int var36 = 0; var36 < bo.dq; ++var36) {
         bo.ca[var36] = new hP(new hQ(dT.a("main_file_cache.idx" + var36), "rw", 1048576L), 6000, 0);
      }

   }

   public static void a(File var0, File var1) {
      try {
         hQ var2 = new hQ(bo.bx, "rw", 10000L);
         aR var3 = new aR(500);
         var3.a(3);
         var3.a(var1 != null ? 1 : 0);
         var3.a((CharSequence)var0.getPath());
         if (var1 != null) {
            var3.a((CharSequence)var1.getPath());
         }

         var2.a(var3.c, 0, var3.d);
         var2.b();
      } catch (IOException var4) {
         var4.printStackTrace();
      }

   }

   public static boolean a(File var0, boolean var1) {
      try {
         RandomAccessFile var2 = new RandomAccessFile(var0, "rw");
         int var3 = var2.read();
         var2.seek(0L);
         var2.write(var3);
         var2.seek(0L);
         var2.close();
         if (var1) {
            var0.delete();
         }

         return true;
      } catch (Exception var4) {
         return false;
      }
   }

   public static hQ a(String var0, String var1, boolean var2) {
      File var3 = new File(bo.bS, "preferences" + var0 + ".dat");
      if (var3.exists()) {
         try {
            hQ var10 = new hQ(var3, "rw", 10000L);
            return var10;
         } catch (IOException var9) {
         }
      }

      String var4 = "";
      if (bo.cE == 33) {
         var4 = "_rc";
      } else if (bo.cE == 34) {
         var4 = "_wip";
      }

      File var5 = new File(a, "jagex_" + var1 + "_preferences" + var0 + var4 + ".dat");
      hQ var6;
      if (!var2 && var5.exists()) {
         try {
            var6 = new hQ(var5, "rw", 10000L);
            return var6;
         } catch (IOException var8) {
         }
      }

      try {
         var6 = new hQ(var3, "rw", 10000L);
         return var6;
      } catch (IOException var7) {
         throw new RuntimeException();
      }
   }

   public static void a() {
      try {
         File var0 = new File(a, "random.dat");
         int var2;
         if (var0.exists()) {
            b = new hP(new hQ(var0, "rw", 25L), 24, 0);
         } else {
            label34:
            for(int var1 = 0; var1 < bo.bT.length; ++var1) {
               for(var2 = 0; var2 < bo.ev.length; ++var2) {
                  String var10002 = bo.ev[var2];
                  File var3 = new File(var10002 + bo.bT[var1] + File.separatorChar + "random.dat");
                  if (var3.exists()) {
                     b = new hP(new hQ(var3, "rw", 25L), 24, 0);
                     break label34;
                  }
               }
            }
         }

         if (b == null) {
            RandomAccessFile var5 = new RandomAccessFile(var0, "rw");
            var2 = var5.read();
            var5.seek(0L);
            var5.write(var2);
            var5.seek(0L);
            var5.close();
            b = new hP(new hQ(var0, "rw", 25L), 24, 0);
         }
      } catch (IOException var4) {
      }

   }

   public static void a(aR var0, int var1) {
      if (b != null) {
         try {
            b.a(0L);
            b.b(var0.c, var1, 24);
         } catch (Exception var3) {
         }
      }

   }

   public static void b() {
      try {
         c.a();

         for(int var0 = 0; var0 < bo.dq; ++var0) {
            bo.ca[var0].a();
         }

         d.a();
         b.a();
      } catch (Exception var1) {
      }

   }
}
