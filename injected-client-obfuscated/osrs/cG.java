package osrs;

import java.util.concurrent.Future;

public class cG {
   public Future a;
   public String b;

   public cG(Future var1) {
      this.a = var1;
   }

   public cG(String var1) {
      this.a(var1);
   }

   public final String a() {
      return this.b;
   }

   public void a(String var1) {
      if (var1 == null) {
         var1 = "";
      }

      this.b = var1;
      if (this.a != null) {
         this.a.cancel(true);
         this.a = null;
      }

   }

   public boolean b() {
      return this.b != null || this.a == null;
   }

   public final boolean c() {
      return this.b() ? true : this.a.isDone();
   }

   public final fw d() {
      if (this.b()) {
         return new fw(this.b);
      } else if (!this.c()) {
         return null;
      } else {
         try {
            return (fw)this.a.get();
         } catch (Exception var3) {
            String var2 = "Error retrieving REST request reply";
            System.err.println(var2 + "\r\n" + String.valueOf(var3));
            this.a(var2);
            return new fw(var2);
         }
      }
   }
}
