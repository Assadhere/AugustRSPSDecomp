package osrs;

import java.io.IOException;
import java.net.URL;
import org.json.JSONObject;

public class dZ {
   public int a = 0;
   public String b;
   public boolean c;
   public static final dZ d = new dZ();
   public final int e = 1;
   public final int f = 2;
   public String g = "";
   public String h = "";
   public String i = "";
   public String j = "";
   public long k = -1L;
   public int l = 1;
   public cG m;
   public cX n;

   public static dZ a() {
      return d;
   }

   public void a(int var1, String var2) {
      this.a = var1;
      this.b = var2;
   }

   public void a(boolean var1) {
      this.c = var1;
      this.n = new cX("crmsession", 1, 1);
   }

   public void a(String var1, String var2, String var3) {
      if (this.l != 2) {
         this.g = var1;
         this.h = var2;
         this.i = var3;
         if (!this.g.endsWith("/")) {
            this.g = this.g + "/";
         }

         if (!this.h.equals("")) {
            String var4 = this.g;
            String var5 = var4 + "session/open/" + this.h;
            if (this.i.equals("")) {
            }

            String var6 = var5 + "?userHash=" + this.i;

            try {
               this.m = this.b(var6);
               this.k = bd.a();
            } catch (IOException var8) {
               this.m = null;
               this.h = "";
               this.i = "";
            }
         }
      }

   }

   public void b() {
      if (this.m == null && !this.j.isEmpty() && this.l == 1) {
         long var1 = bd.a();
         long var3 = var1 - this.k;
         String var5 = this.g;
         String var6 = var5 + "session/close/" + this.h + "/" + this.j;
         String var7;
         if (this.i.isEmpty()) {
            var7 = var6 + "?sessionDuration=" + var3;
         } else {
            var7 = var6 + "?userHash=" + this.i + "&sessionDuration=" + var3;
         }

         try {
            this.m = this.b(var7);
         } catch (IOException var9) {
            this.l = 1;
         }
      }

   }

   public void c() {
      if (this.n != null) {
         this.n.a();
      }

   }

   public void d() {
      if (!this.j.isEmpty()) {
         String var1 = "";
         switch (this.a) {
            case 1:
               var1 = "events/click";
               break;
            case 2:
               var1 = "events/dismissed";
               break;
            case 3:
               var1 = "events/impression";
         }

         if (!this.b.isEmpty()) {
            String var2 = this.g;
            String var3 = var2 + var1 + "/" + this.h + "/" + this.j + "/" + this.b + "?userHash=" + this.i;

            try {
               this.m = this.b(var3);
            } catch (IOException var5) {
               this.l = 1;
            }

            this.l = 1;
            this.a = 0;
         }
      }

   }

   public void e() {
      if (this.m != null && this.m.c()) {
         if (this.m.c() && this.m.d().a() == 200 && this.j.isEmpty()) {
            String var1 = this.m.d().d();
            if (var1.isEmpty()) {
               return;
            }

            this.j = var1;
         }

         if (this.a != 0) {
            this.d();
         }
      }

   }

   public boolean a(String var1) {
      this.a(2, var1);
      return true;
   }

   public cG b(String var1) throws IOException {
      URL var2 = new URL(var1);
      bO var3 = new bO(var2, dk.a, this.c);

      try {
         JSONObject var4 = new JSONObject();
         var3.a(new hd(var4));
      } catch (Exception var5) {
      }

      return this.n.a(var3);
   }
}
