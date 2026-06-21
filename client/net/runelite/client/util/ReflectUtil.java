package net.runelite.client.util;

import com.google.common.io.ByteStreams;
import com.google.inject.Injector;
import com.google.inject.Key;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Constructor;
import java.lang.reflect.Executable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReflectUtil {
   private static final Logger log = LoggerFactory.getLogger(ReflectUtil.class);
   private static Set<Class<?>> annotationClasses = Collections.newSetFromMap(new WeakHashMap());

   public static MethodHandles.Lookup privateLookupIn(Class<?> clazz) {
      try {
         MethodHandles.Lookup caller;
         if (clazz.getClassLoader() instanceof PrivateLookupableClassLoader) {
            caller = ((PrivateLookupableClassLoader)clazz.getClassLoader()).getLookup();
         } else {
            caller = MethodHandles.lookup();
         }

         return MethodHandles.privateLookupIn(clazz, caller);
      } catch (IllegalAccessException var2) {
         IllegalAccessException e = var2;
         throw new RuntimeException(e);
      }
   }

   public static void installLookupHelper(PrivateLookupableClassLoader cl) {
      String name = PrivateLookupHelper.class.getName();

      try {
         InputStream in = ReflectUtil.class.getResourceAsStream("/" + name.replace('.', '/') + ".class");

         try {
            byte[] classData = ByteStreams.toByteArray(in);
            Class<?> clazz = cl.defineClass0(name, classData, 0, classData.length);
            clazz.getConstructor().newInstance();
         } catch (Throwable var6) {
            if (in != null) {
               try {
                  in.close();
               } catch (Throwable var5) {
                  var6.addSuppressed(var5);
               }
            }

            throw var6;
         }

         if (in != null) {
            in.close();
         }

      } catch (ReflectiveOperationException | IOException var7) {
         Exception e = var7;
         throw new RuntimeException("unable to install lookup helper", e);
      }
   }

   public static synchronized void queueInjectorAnnotationCacheInvalidation(Injector injector) {
      if (annotationClasses != null) {
         Iterator var1 = injector.getAllBindings().keySet().iterator();

         while(var1.hasNext()) {
            Key<?> key = (Key)var1.next();

            for(Class<?> clazz = key.getTypeLiteral().getRawType(); clazz != null; clazz = clazz.getSuperclass()) {
               annotationClasses.add(clazz);
            }
         }

      }
   }

   public static synchronized void invalidateAnnotationCaches() {
      try {
         Iterator var11 = annotationClasses.iterator();

         while(var11.hasNext()) {
            Class<?> clazz = (Class)var11.next();
            Method[] var2 = clazz.getDeclaredMethods();
            int var3 = var2.length;

            int var4;
            for(var4 = 0; var4 < var3; ++var4) {
               Method method = var2[var4];
               uncacheAnnotations(method, Executable.class);
            }

            Field[] var12 = clazz.getDeclaredFields();
            var3 = var12.length;

            for(var4 = 0; var4 < var3; ++var4) {
               Field field = var12[var4];
               uncacheAnnotations(field, Field.class);
            }

            Constructor[] var13 = clazz.getDeclaredConstructors();
            var3 = var13.length;

            for(var4 = 0; var4 < var3; ++var4) {
               Constructor<?> constructor = var13[var4];
               uncacheAnnotations(constructor, Executable.class);
            }
         }
      } catch (Exception var9) {
         Exception ex = var9;
         log.debug((String)null, ex);
      } finally {
         annotationClasses.clear();
         annotationClasses = null;
      }

   }

   private static void uncacheAnnotations(Object object, Class<?> declaredAnnotationsClazz) throws Exception {
      if (object != null) {
         Field declaredAnnotations = declaredAnnotationsClazz.getDeclaredField("declaredAnnotations");
         declaredAnnotations.setAccessible(true);
         synchronized(object) {
            Map<Class<? extends Annotation>, Annotation> m = (Map)declaredAnnotations.get(object);
            if (m != null && m != Collections.emptyMap()) {
               declaredAnnotations.set(object, (Object)null);
            }
         }

         Field rootField = object.getClass().getDeclaredField("root");
         rootField.setAccessible(true);
         Object root = rootField.get(object);
         uncacheAnnotations(root, declaredAnnotationsClazz);
      }
   }

   private ReflectUtil() {
   }

   public static class PrivateLookupHelper {
      static {
         PrivateLookupableClassLoader pcl = (PrivateLookupableClassLoader)PrivateLookupHelper.class.getClassLoader();
         pcl.setLookup(MethodHandles.lookup());
      }
   }

   public interface PrivateLookupableClassLoader {
      Class<?> defineClass0(String var1, byte[] var2, int var3, int var4) throws ClassFormatError;

      MethodHandles.Lookup getLookup();

      void setLookup(MethodHandles.Lookup var1);
   }
}
