package net.runelite.client.plugins.gpu.template;

import com.google.common.io.CharStreams;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Template {
   private static final Logger log = LoggerFactory.getLogger(Template.class);
   private final List<Function<String, String>> resourceLoaders = new ArrayList();

   public String process(String str) {
      StringBuilder sb = new StringBuilder();
      String[] var3 = str.split("\r?\n");
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         String line = var3[var5];
         if (line.startsWith("#include ")) {
            String resource = line.substring(9);
            if (resource.startsWith("\"") && resource.endsWith("\"")) {
               resource = resource.substring(1, resource.length() - 1);
            }

            String resourceStr = this.load(resource);
            sb.append(resourceStr);
         } else {
            sb.append(line).append('\n');
         }
      }

      return sb.toString();
   }

   public String load(String filename) {
      Iterator var2 = this.resourceLoaders.iterator();

      String value;
      do {
         if (!var2.hasNext()) {
            return "";
         }

         Function<String, String> loader = (Function)var2.next();
         value = (String)loader.apply(filename);
      } while(value == null);

      return this.process(value);
   }

   public Template add(Function<String, String> fn) {
      this.resourceLoaders.add(fn);
      return this;
   }

   public Template addInclude(Class<?> clazz) {
      return this.add((f) -> {
         try {
            InputStream is = clazz.getResourceAsStream(f);

            String var3;
            label50: {
               try {
                  if (is != null) {
                     var3 = inputStreamToString(is);
                     break label50;
                  }
               } catch (Throwable var6) {
                  if (is != null) {
                     try {
                        is.close();
                     } catch (Throwable var5) {
                        var6.addSuppressed(var5);
                     }
                  }

                  throw var6;
               }

               if (is != null) {
                  is.close();
               }

               return null;
            }

            if (is != null) {
               is.close();
            }

            return var3;
         } catch (IOException var7) {
            IOException ex = var7;
            log.warn((String)null, ex);
            return null;
         }
      });
   }

   private static String inputStreamToString(InputStream in) {
      try {
         return CharStreams.toString(new InputStreamReader(in, StandardCharsets.UTF_8));
      } catch (IOException var2) {
         IOException e = var2;
         throw new RuntimeException(e);
      }
   }
}
