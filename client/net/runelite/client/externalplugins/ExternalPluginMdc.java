package net.runelite.client.externalplugins;

import java.util.LinkedHashMap;
import java.util.Map;
import javax.annotation.Nullable;
import org.slf4j.MDC;

public final class ExternalPluginMdc {
   public static final String PLUGIN_TAG = "pluginhub_plugin";
   public static final String PLUGIN_NAME_TAG = "pluginhub_plugin_name";
   public static final String PLUGIN_VERSION_TAG = "pluginhub_plugin_version";

   public static ScopedMdc open(@Nullable Object source) {
      return source == null ? ExternalPluginMdc.ScopedMdc.NOOP : open(source.getClass().getClassLoader());
   }

   public static ScopedMdc open(@Nullable ClassLoader classLoader) {
      if (!(classLoader instanceof PluginHubClassLoader)) {
         return ExternalPluginMdc.ScopedMdc.NOOP;
      } else {
         PluginHubClassLoader pluginHubClassLoader = (PluginHubClassLoader)classLoader;
         Map<String, String> mdcValues = new LinkedHashMap();
         putIfPresent(mdcValues, "pluginhub_plugin", pluginHubClassLoader.getJarData().getInternalName());
         putIfPresent(mdcValues, "pluginhub_plugin_name", pluginHubClassLoader.getStub().getDisplayName());
         putIfPresent(mdcValues, "pluginhub_plugin_version", pluginHubClassLoader.getStub().getVersion());
         if (mdcValues.isEmpty()) {
            return ExternalPluginMdc.ScopedMdc.NOOP;
         } else {
            Map<String, String> previous = new LinkedHashMap();
            mdcValues.forEach((key, value) -> {
               previous.put(key, MDC.get(key));
               MDC.put(key, value);
            });
            return () -> {
               previous.forEach((key, value) -> {
                  if (value == null) {
                     MDC.remove(key);
                  } else {
                     MDC.put(key, value);
                  }

               });
            };
         }
      }
   }

   private static void putIfPresent(Map<String, String> mdcValues, String key, @Nullable String value) {
      if (value != null && !value.isEmpty()) {
         mdcValues.put(key, value);
      }

   }

   private ExternalPluginMdc() {
   }

   public interface ScopedMdc extends AutoCloseable {
      ScopedMdc NOOP = () -> {
      };

      void close();
   }
}
