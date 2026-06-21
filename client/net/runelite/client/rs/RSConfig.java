package net.runelite.client.rs;

import java.util.HashMap;
import java.util.Map;
import net.runelite.client.RuneLiteProperties;

class RSConfig {
   private final Map<String, String> appletProperties = new HashMap();
   private final Map<String, String> classLoaderProperties = new HashMap();

   public Map<String, String> getAppletProperties() {
      String debug = System.getenv("DEBUG");
      if (!RuneLiteProperties.isBeta() && (debug == null || !debug.equalsIgnoreCase("true"))) {
         this.appletProperties.put("17", "https://github.com/August-Games/august-static/raw/main/worldlist.ws");
      } else {
         this.appletProperties.put("17", "https://github.com/August-Games/august-static/raw/main/worldlist-beta.ws");
         this.appletProperties.put("7", "2");
      }

      return this.appletProperties;
   }

   String getCodeBase() {
      return RuneLiteProperties.isBeta() ? "http://w1.august.games/" : "http://dedi.august-rsps.com/";
   }

   void setCodebase(String codebase) {
      this.classLoaderProperties.put("codebase", codebase);
   }

   String getInitialJar() {
      return (String)this.classLoaderProperties.get("initial_jar");
   }

   String getInitialClass() {
      return ((String)this.classLoaderProperties.get("initial_class")).replace(".class", "");
   }

   String getRuneLiteWorldParam() {
      return (String)this.classLoaderProperties.get("runelite.worldparam");
   }

   public Map<String, String> getClassLoaderProperties() {
      return this.classLoaderProperties;
   }
}
