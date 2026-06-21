package osrs;

import java.io.File;
import java.io.RandomAccessFile;
import java.util.Hashtable;

public class dT {
   public static boolean a = false;
   public static Hashtable b = new Hashtable(16);

   public static void a(File var0) {
      bo.bB = var0;
      if (!bo.bB.exists()) {
         throw new RuntimeException("");
      } else {
         a = true;
      }
   }

   public static File a(String var0) {
      if (!a) {
         throw new RuntimeException("");
      } else {
         File var1 = (File)b.get(var0);
         if (var1 != null) {
            return var1;
         } else {
            File var2 = new File(bo.bB, var0);
            RandomAccessFile var3 = null;

            try {
               File var4 = new File(var2.getParent());
               if (!var4.exists()) {
                  throw new RuntimeException("");
               } else {
                  var3 = new RandomAccessFile(var2, "rw");
                  int var8 = var3.read();
                  var3.seek(0L);
                  var3.write(var8);
                  var3.seek(0L);
                  var3.close();
                  b.put(var0, var2);
                  return var2;
               }
            } catch (Exception var7) {
               try {
                  if (var3 != null) {
                     var3.close();
                     Object var5 = null;
                  }
               } catch (Exception var6) {
               }

               throw new RuntimeException();
            }
         }
      }
   }

   public static void a() {
      b.clear();
   }
}
