package osrs;

import java.util.Comparator;

public class gB implements Comparator {
   public boolean a;

   public int a(f var1, f var2) {
      if (var1.c == var2.c) {
         return 0;
      } else {
         if (this.a) {
            if (Client.u == var1.c) {
               return -1;
            }

            if (Client.u == var2.c) {
               return 1;
            }
         }

         return var1.c < var2.c ? -1 : 1;
      }
   }

   public int compare(Object var1, Object var2) {
      return this.a((f)var1, (f)var2);
   }

   public boolean equals(Object var1) {
      return super.equals(var1);
   }
}
