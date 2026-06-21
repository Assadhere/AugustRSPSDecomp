package osrs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class dW {
   public static final dW a = new dW();
   public Map b = new HashMap();
   public int c = 0;

   public void a(JSONObject var1, int var2, cf var3) {
      this.b.clear();

      JSONObject var4;
      try {
         var4 = var1;
         this.c = var2;
         var1.getLong("lastfullsyncat");
         var1.getLong("lastcardupdatedat");
      } catch (Exception var7) {
         return;
      }

      try {
         this.a(var4.getJSONArray("crmcomponents"), this.c, var3);
      } catch (Exception var6) {
      }

   }

   public void a(JSONArray var1, int var2, cf var3) throws JSONException {
      if (var1 != null) {
         for(int var4 = 0; var4 < var1.length(); ++var4) {
            JSONObject var5 = var1.getJSONObject(var4);
            if (var5.getString("game").equals("osrs") && !var5.getBoolean("removed")) {
               String var6 = var5.getString("platform");
               if (!var6.equals("mobile") && !var6.equals("android") && !var6.equals("ios")) {
                  eb var7 = new eb();

                  try {
                     var7.a(var5, var2, var3);
                  } catch (Exception var9) {
                  }

                  if (!var7.d.isEmpty()) {
                     String var8 = var5.getString("location");
                     this.b.put(var8, var7);
                  }
               }
            }
         }

         this.b = this.a();
         bd.a();
      }

   }

   public Map a() {
      HashMap var1 = new HashMap();
      ArrayList var2 = new ArrayList();
      var2.addAll(this.b.entrySet());
      boolean var3 = false;

      Map.Entry var5;
      while(!var3) {
         var3 = true;

         for(int var4 = 0; var4 < var2.size() - 1; ++var4) {
            if (((eb)((Map.Entry)var2.get(var4)).getValue()).b() > ((eb)((Map.Entry)var2.get(var4 + 1)).getValue()).b()) {
               var5 = (Map.Entry)var2.get(var4);
               var2.set(var4, var2.get(var4 + 1));
               var2.set(var4 + 1, var5);
               var3 = false;
            }
         }
      }

      Iterator var6 = var2.iterator();

      while(var6.hasNext()) {
         var5 = (Map.Entry)var6.next();
         var1.put(var5.getKey(), var5.getValue());
      }

      return var1;
   }

   public eb a(String var1) {
      return (eb)this.b.get(var1);
   }
}
