package osrs;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.net.ssl.HttpsURLConnection;

public class gE {
   public final Map a = new HashMap();
   public final Map b = new HashMap();
   public final DecimalFormat c = new DecimalFormat();

   public gE() {
      this.c.setMaximumFractionDigits(2);
   }

   public void a(HttpsURLConnection var1) {
      Iterator var2 = this.a.entrySet().iterator();

      while(var2.hasNext()) {
         Map.Entry var3 = (Map.Entry)var2.next();
         var1.setRequestProperty((String)var3.getKey(), (String)var3.getValue());
      }

   }

   public Map a() {
      return this.a;
   }

   public void a(String var1, String var2) {
      if (var1 != null && !var1.isEmpty()) {
         this.a.put(var1, var2 != null ? var2 : "");
      }

   }

   public void a(String var1) {
      if (var1 != null && !var1.isEmpty()) {
         this.a.remove(var1);
      }

   }

   public void a(gr var1, String var2) {
      String var3 = String.format("%s %s", var1.a(), var2);
      this.a("Authorization", var3);
   }

   public void b(String var1) {
      this.a(gr.a, var1);
   }

   public void c(String var1) {
      this.a(gr.b, var1);
   }

   public void a(hl var1) {
      this.a.put("Content-Type", var1.a());
   }

   public void b() {
      this.a.remove("Content-Type");
   }

   public void b(hl var1) {
      this.a(var1, 1.0F);
   }

   public void a(hl var1, float var2) {
      this.b.put(var1, Math.max(0.0F, Math.min(1.0F, var2)));
      this.c();
   }

   public void c() {
      this.a.remove("Accept");
      if (!this.b.isEmpty()) {
         this.a.put("Accept", this.d());
      }

   }

   public String d() {
      ArrayList var1 = new ArrayList(this.b.entrySet());
      Collections.sort(var1, new a());
      StringBuilder var2 = new StringBuilder();
      Iterator var3 = var1.iterator();

      while(var3.hasNext()) {
         Map.Entry var4 = (Map.Entry)var3.next();
         if (var2.length() > 0) {
            var2.append(",");
         }

         var2.append(((hl)var4.getKey()).a());
         float var5 = (Float)var4.getValue();
         if (var5 < 1.0F) {
            String var6 = this.c.format((double)var5);
            var2.append(";q=").append(var6);
         }
      }

      return var2.toString();
   }

   class a implements Comparator {
      public a() {
      }

      public int a(Map.Entry var1, Map.Entry var2) {
         return ((Float)var2.getValue()).compareTo((Float)var1.getValue());
      }

      public int compare(Object var1, Object var2) {
         return this.a((Map.Entry)var1, (Map.Entry)var2);
      }

      public boolean equals(Object var1) {
         return super.equals(var1);
      }
   }
}
