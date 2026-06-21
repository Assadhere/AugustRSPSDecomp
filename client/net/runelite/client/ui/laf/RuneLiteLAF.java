package net.runelite.client.ui.laf;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLaf;
import java.awt.Color;
import java.awt.RenderingHints;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import javax.swing.UIDefaults;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.FontManager;

public class RuneLiteLAF extends FlatDarkLaf {
   public static boolean setup() {
      System.setProperty("flatlaf.uiScale.enabled", "false");
      return setup(new RuneLiteLAF());
   }

   public RuneLiteLAF() {
      try {
         Map<String, String> extras = new HashMap();
         Properties p = new Properties();
         Class[] var3 = new Class[]{FlatLaf.class, FlatDarkLaf.class, RuneLiteLAF.class};
         int var4 = var3.length;

         int var5;
         for(var5 = 0; var5 < var4; ++var5) {
            Class<?> clazz = var3[var5];
            String var10001 = clazz.getName();
            InputStream is = clazz.getResourceAsStream("/" + var10001.replace('.', '/') + ".properties");

            try {
               if (is != null) {
                  p.load(new InputStreamReader(is, StandardCharsets.UTF_8));
               }
            } catch (Throwable var11) {
               if (is != null) {
                  try {
                     is.close();
                  } catch (Throwable var10) {
                     var11.addSuppressed(var10);
                  }
               }

               throw var11;
            }

            if (is != null) {
               is.close();
            }
         }

         Iterator var14 = p.entrySet().iterator();

         while(true) {
            String k;
            String v;
            do {
               if (!var14.hasNext()) {
                  Field[] var15 = ColorScheme.class.getDeclaredFields();
                  var4 = var15.length;

                  for(var5 = 0; var5 < var4; ++var5) {
                     Field f = var15[var5];
                     if (Modifier.isStatic(f.getModifiers()) && Color.class == f.getType()) {
                        String name = f.getName();
                        if (name.endsWith("_COLOR")) {
                           name = name.substring(0, name.length() - 6);
                        }

                        Color color = (Color)f.get((Object)null);
                        extras.put("@" + name, String.format("#%06x%02x", color.getRGB() & 16777215, color.getRGB() >>> 24));
                     }
                  }

                  this.setExtraDefaults(extras);
                  return;
               }

               Map.Entry<Object, Object> ent = (Map.Entry)var14.next();
               k = (String)ent.getKey();
               v = (String)ent.getValue();
            } while(k.charAt(0) == '[' && !k.startsWith("[style]"));

            extras.put(k, v);
         }
      } catch (Throwable var12) {
         Throwable $ex = var12;
         throw $ex;
      }
   }

   protected List<Class<?>> getLafClassesForDefaultsLoading() {
      return Collections.emptyList();
   }

   public String getName() {
      return "RuneLite";
   }

   public String getDescription() {
      return "RuneLite";
   }

   public UIDefaults getDefaults() {
      UIDefaults d = super.getDefaults();
      d.put("defaultFont", FontManager.getRunescapeFont());
      d.put(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_GASP);
      return d;
   }
}
