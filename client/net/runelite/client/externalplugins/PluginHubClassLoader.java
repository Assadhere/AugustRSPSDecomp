package net.runelite.client.externalplugins;

import com.google.gson.Gson;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.invoke.MethodHandles;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.StandardCharsets;
import net.runelite.client.util.ReflectUtil;

class PluginHubClassLoader extends URLClassLoader implements ReflectUtil.PrivateLookupableClassLoader {
   private final PluginHubManifest.JarData jarData;
   private final PluginHubManifest.Stub stub;
   private MethodHandles.Lookup lookup;

   PluginHubClassLoader(PluginHubManifest.JarData jarData, URL[] urls, Gson gson) throws IOException {
      super(urls, PluginHubClassLoader.class.getClassLoader());
      this.jarData = jarData;
      InputStream is = this.getResourceAsStream("runelite_plugin.json");

      try {
         this.stub = (PluginHubManifest.Stub)gson.fromJson(new InputStreamReader(is, StandardCharsets.UTF_8), PluginHubManifest.Stub.class);
      } catch (Throwable var8) {
         if (is != null) {
            try {
               is.close();
            } catch (Throwable var7) {
               var8.addSuppressed(var7);
            }
         }

         throw var8;
      }

      if (is != null) {
         is.close();
      }

      ReflectUtil.installLookupHelper(this);
   }

   public Class<?> defineClass0(String name, byte[] b, int off, int len) throws ClassFormatError {
      return super.defineClass(name, b, off, len);
   }

   public PluginHubManifest.JarData getJarData() {
      return this.jarData;
   }

   public PluginHubManifest.Stub getStub() {
      return this.stub;
   }

   public MethodHandles.Lookup getLookup() {
      return this.lookup;
   }

   public void setLookup(MethodHandles.Lookup lookup) {
      this.lookup = lookup;
   }
}
