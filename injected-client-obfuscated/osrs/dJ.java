package osrs;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import org.json.JSONObject;

public class dJ {
   public ce a;
   public int b = 0;
   public int c = -1;
   public dZ d;
   public eb e;
   public String f;
   public String g;
   public String h;

   public dJ() {
   }

   public dJ(dJ var1) {
      if (var1 != null) {
         this.d = var1.d;
         this.e = var1.e;
         this.c = var1.c;
         this.f = var1.f;
         this.g = var1.g;
         this.h = var1.h;
         this.a = null;
         this.b = var1.b;
      }

   }

   public dZ a() {
      return this.d;
   }

   public boolean a(String var1, String var2, cf var3) {
      if (var1 != null && !var1.isEmpty()) {
         if (var3 == null) {
            return false;
         } else {
            this.j();

            try {
               this.f = var1;
               this.a = var3.a(new URL(this.f));
               this.c = 0;
            } catch (MalformedURLException var5) {
               this.j();
               this.c = 4;
               return false;
            }

            if (!var2.isEmpty()) {
               this.h = var2;
            }

            return true;
         }
      } else {
         return false;
      }
   }

   public void a(String var1, String var2, String var3) {
      this.d = dZ.a();
      this.g = var1;
      this.d.a(this.g, var2, var3);
   }

   public void a(cf var1) {
      switch (this.c) {
         case 0:
            this.b(var1);
            break;
         case 1:
            this.k();
            break;
         default:
            return;
      }

   }

   public int b() {
      return this.c;
   }

   public boolean c() {
      return this.e != null;
   }

   public int a(String var1) {
      return this.e.f.containsKey(var1) ? (Integer)this.e.f.get(var1) : -1;
   }

   public String b(String var1) {
      return (String)(this.e.g.containsKey(var1) ? this.e.g.get(var1) : null);
   }

   public ArrayList d() {
      return this.e.b;
   }

   public ArrayList e() {
      return this.e.c;
   }

   public ArrayList f() {
      return this.e.a;
   }

   public String g() {
      return this.e.d;
   }

   public float[] h() {
      return this.e.e;
   }

   public String i() {
      return this.e.a();
   }

   public void j() {
      this.a = null;
   }

   public void b(cf var1) {
      if (this.a != null && this.a.a()) {
         byte[] var2 = this.a.b();
         if (var2 == null) {
            this.j();
            this.c = 4;
         } else {
            try {
               hd var3 = new hd(var2);
               JSONObject var4 = var3.c();
               if (var4 == null) {
                  return;
               }

               try {
                  this.b = var4.getInt("version");
               } catch (Exception var6) {
                  this.j();
                  this.c = 6;
                  return;
               }

               if (this.b < 2) {
                  if (!this.e.a(var4, this.b, var1)) {
                     this.c = 6;
                  }
               } else if (this.b == 2) {
                  dW var5 = dW.a;
                  var5.a(var4, this.b, var1);
                  this.e = var5.a(this.h);
                  if (this.e != null) {
                     this.k();
                     this.c = 1;
                  } else {
                     this.c = 7;
                  }
               }
            } catch (UnsupportedEncodingException var7) {
               this.j();
               this.c = 6;
               return;
            }

            if (this.e != null) {
               this.c = !this.e.b.isEmpty() ? 1 : 2;
            }

            this.a = null;
         }
      }

   }

   public void k() {
      Iterator var1 = this.e.b.iterator();

      eb.a var2;
      do {
         if (!var1.hasNext()) {
            Iterator var3 = this.e.b.iterator();

            while(var3.hasNext()) {
               eb.a var4 = (eb.a)var3.next();
               if (var4.b != null) {
                  byte[] var5 = var4.b.b();
                  if (var5 != null && var5.length > 0) {
                     this.c = 2;
                     return;
                  }
               }
            }

            this.j();
            this.c = 5;
            return;
         }

         var2 = (eb.a)var1.next();
      } while(var2.b == null || var2.b.a());

   }

   public boolean a(String var1, cf var2) {
      try {
         JSONObject var3 = (new hd(var1.getBytes())).c();

         try {
            this.b = var3.getInt("version");
         } catch (Exception var5) {
            this.j();
            this.c = 6;
            return false;
         }

         if (!this.e.a(var3, this.b, var2)) {
            this.c = 6;
         }

         this.c = !this.e.b.isEmpty() ? 1 : 2;
      } catch (UnsupportedEncodingException var6) {
         this.c = 6;
      }

      return this.c < 3;
   }
}
