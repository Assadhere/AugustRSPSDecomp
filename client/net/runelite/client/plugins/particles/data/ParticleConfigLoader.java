package net.runelite.client.plugins.particles.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import net.runelite.client.plugins.particles.binding.ObjectEffectorBinding;
import net.runelite.client.plugins.particles.binding.ObjectEmitterBinding;
import net.runelite.client.plugins.particles.config.ParticleEffectorConfig;
import net.runelite.client.plugins.particles.config.ParticleEmitterConfig;
import net.runelite.client.plugins.particles.core.ParticleSystem;

public class ParticleConfigLoader {
   private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().create();

   public static List<ParticleEmitterConfig> loadEmitterConfigs(InputStream inputStream) throws IOException {
      Reader reader = new InputStreamReader(inputStream);

      List var8;
      try {
         Type listType = (new TypeToken<List<ParticleEmitterConfig>>() {
         }).getType();
         List<ParticleEmitterConfig> configs = (List)GSON.fromJson(reader, listType);
         if (configs != null) {
            Iterator var4 = configs.iterator();

            while(var4.hasNext()) {
               ParticleEmitterConfig config = (ParticleEmitterConfig)var4.next();
               config.normalize();
               config.postDecode();
            }
         }

         var8 = configs;
      } catch (Throwable var7) {
         try {
            ((Reader)reader).close();
         } catch (Throwable var6) {
            var7.addSuppressed(var6);
         }

         throw var7;
      }

      ((Reader)reader).close();
      return var8;
   }

   public static List<ParticleEffectorConfig> loadEffectorConfigs(InputStream inputStream) throws IOException {
      Reader reader = new InputStreamReader(inputStream);

      List var8;
      try {
         Type listType = (new TypeToken<List<ParticleEffectorConfig>>() {
         }).getType();
         List<ParticleEffectorConfig> configs = (List)GSON.fromJson(reader, listType);
         if (configs != null) {
            Iterator var4 = configs.iterator();

            while(var4.hasNext()) {
               ParticleEffectorConfig config = (ParticleEffectorConfig)var4.next();
               config.postDecode();
            }
         }

         var8 = configs;
      } catch (Throwable var7) {
         try {
            ((Reader)reader).close();
         } catch (Throwable var6) {
            var7.addSuppressed(var6);
         }

         throw var7;
      }

      ((Reader)reader).close();
      return var8;
   }

   public static Map<Integer, ObjectEmitterBinding> loadObjectEmitterBindings(InputStream inputStream) throws IOException {
      Reader reader = new InputStreamReader(inputStream);

      HashMap var9;
      try {
         Type listType = (new TypeToken<List<ObjectEmitterBinding>>() {
         }).getType();
         List<ObjectEmitterBinding> bindings = (List)GSON.fromJson(reader, listType);
         Map<Integer, ObjectEmitterBinding> bindingMap = new HashMap();
         if (bindings != null) {
            Iterator var5 = bindings.iterator();

            while(var5.hasNext()) {
               ObjectEmitterBinding binding = (ObjectEmitterBinding)var5.next();
               bindingMap.put(binding.getObjectId(), binding);
            }
         }

         var9 = bindingMap;
      } catch (Throwable var8) {
         try {
            ((Reader)reader).close();
         } catch (Throwable var7) {
            var8.addSuppressed(var7);
         }

         throw var8;
      }

      ((Reader)reader).close();
      return var9;
   }

   public static Map<Integer, ObjectEffectorBinding> loadObjectEffectorBindings(InputStream inputStream) throws IOException {
      Reader reader = new InputStreamReader(inputStream);

      HashMap var9;
      try {
         Type listType = (new TypeToken<List<ObjectEffectorBinding>>() {
         }).getType();
         List<ObjectEffectorBinding> bindings = (List)GSON.fromJson(reader, listType);
         Map<Integer, ObjectEffectorBinding> bindingMap = new HashMap();
         if (bindings != null) {
            Iterator var5 = bindings.iterator();

            while(var5.hasNext()) {
               ObjectEffectorBinding binding = (ObjectEffectorBinding)var5.next();
               bindingMap.put(binding.getObjectId(), binding);
            }
         }

         var9 = bindingMap;
      } catch (Throwable var8) {
         try {
            ((Reader)reader).close();
         } catch (Throwable var7) {
            var8.addSuppressed(var7);
         }

         throw var8;
      }

      ((Reader)reader).close();
      return var9;
   }

   public static ParticleWhitelist loadWhitelist(InputStream inputStream) throws IOException {
      Reader reader = new InputStreamReader(inputStream);

      ParticleWhitelist var3;
      try {
         ParticleWhitelist whitelist = (ParticleWhitelist)GSON.fromJson(reader, ParticleWhitelist.class);
         if (whitelist == null) {
            whitelist = new ParticleWhitelist();
         }

         whitelist.postDecode();
         var3 = whitelist;
      } catch (Throwable var5) {
         try {
            ((Reader)reader).close();
         } catch (Throwable var4) {
            var5.addSuppressed(var4);
         }

         throw var5;
      }

      ((Reader)reader).close();
      return var3;
   }

   public static void saveEmitterConfigs(List<ParticleEmitterConfig> configs, OutputStream outputStream) throws IOException {
      Writer writer = new OutputStreamWriter(outputStream);

      try {
         GSON.toJson(configs, writer);
      } catch (Throwable var6) {
         try {
            ((Writer)writer).close();
         } catch (Throwable var5) {
            var6.addSuppressed(var5);
         }

         throw var6;
      }

      ((Writer)writer).close();
   }

   public static void saveEffectorConfigs(List<ParticleEffectorConfig> configs, OutputStream outputStream) throws IOException {
      Writer writer = new OutputStreamWriter(outputStream);

      try {
         GSON.toJson(configs, writer);
      } catch (Throwable var6) {
         try {
            ((Writer)writer).close();
         } catch (Throwable var5) {
            var6.addSuppressed(var5);
         }

         throw var6;
      }

      ((Writer)writer).close();
   }

   public static void loadIntoSystem(ParticleSystem system, InputStream emitterStream, InputStream effectorStream) throws IOException {
      List effectors;
      Iterator var4;
      if (emitterStream != null) {
         effectors = loadEmitterConfigs(emitterStream);
         if (effectors != null) {
            var4 = effectors.iterator();

            while(var4.hasNext()) {
               ParticleEmitterConfig config = (ParticleEmitterConfig)var4.next();
               system.registerEmitterConfig(config);
            }
         }
      }

      if (effectorStream != null) {
         effectors = loadEffectorConfigs(effectorStream);
         if (effectors != null) {
            var4 = effectors.iterator();

            while(var4.hasNext()) {
               ParticleEffectorConfig config = (ParticleEffectorConfig)var4.next();
               system.registerEffectorConfig(config);
            }
         }
      }

   }
}
