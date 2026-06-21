package net.runelite.client.config;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Objects;
import net.runelite.client.util.ReflectUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class ConfigInvocationHandler implements InvocationHandler {
   private static final Logger log = LoggerFactory.getLogger(ConfigInvocationHandler.class);
   private static final Object NULL = new Object();
   private final ConfigManager manager;
   private final Cache<Method, Object> cache = CacheBuilder.newBuilder().maximumSize(256L).build();

   ConfigInvocationHandler(ConfigManager manager) {
      this.manager = manager;
   }

   public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
      if (args == null) {
         Object cachedValue = this.cache.getIfPresent(method);
         if (cachedValue != null) {
            return cachedValue == NULL ? null : cachedValue;
         }
      }

      Class<?> iface = proxy.getClass().getInterfaces()[0];
      if ("toString".equals(method.getName()) && args == null) {
         return iface.getSimpleName();
      } else if ("hashCode".equals(method.getName()) && args == null) {
         return System.identityHashCode(proxy);
      } else if ("equals".equals(method.getName()) && args != null && args.length == 1) {
         return proxy == args[0];
      } else {
         ConfigGroup group = (ConfigGroup)iface.getAnnotation(ConfigGroup.class);
         ConfigItem item = (ConfigItem)method.getAnnotation(ConfigItem.class);
         if (group == null) {
            log.warn("Configuration proxy class {} has no @ConfigGroup!", proxy.getClass());
            return null;
         } else if (item == null) {
            log.warn("Configuration method {} has no @ConfigItem!", method);
            return null;
         } else if (args != null) {
            if (args.length != 1) {
               throw new RuntimeException("Invalid number of arguments to configuration method");
            } else {
               Object newValue = args[0];
               Class<?> type = method.getParameterTypes()[0];
               Object oldValue = this.manager.getConfiguration(group.value(), item.keyName(), (Type)type);
               if (Objects.equals(oldValue, newValue)) {
                  return null;
               } else {
                  if (method.isDefault()) {
                     Object defaultValue = callDefaultMethod(proxy, method, args);
                     if (Objects.equals(newValue, defaultValue)) {
                        this.manager.unsetConfiguration(group.value(), item.keyName());
                        return null;
                     }
                  }

                  if (newValue == null) {
                     this.manager.unsetConfiguration(group.value(), item.keyName());
                  } else {
                     String newValueStr = this.manager.objectToString(newValue);
                     this.manager.setConfiguration(group.value(), item.keyName(), newValueStr);
                  }

                  return null;
               }
            }
         } else {
            log.trace("cache miss (size: {}, group: {}, key: {})", new Object[]{this.cache.size(), group.value(), item.keyName()});
            String value = this.manager.getConfiguration(group.value(), item.keyName());
            Object defaultValue;
            if (value == null) {
               if (method.isDefault()) {
                  defaultValue = callDefaultMethod(proxy, method, (Object[])null);
                  this.cache.put(method, defaultValue == null ? NULL : defaultValue);
                  return defaultValue;
               } else {
                  this.cache.put(method, NULL);
                  return null;
               }
            } else {
               try {
                  defaultValue = this.manager.stringToObject(value, method.getGenericReturnType());
                  this.cache.put(method, defaultValue == null ? NULL : defaultValue);
                  return defaultValue;
               } catch (Exception var11) {
                  Exception e = var11;
                  log.warn("Unable to unmarshal {}.{} ", new Object[]{group.value(), item.keyName(), e});
                  return method.isDefault() ? callDefaultMethod(proxy, method, (Object[])null) : null;
               }
            }
         }
      }
   }

   static Object callDefaultMethod(Object proxy, Method method, Object[] args) throws Throwable {
      Class<?> declaringClass = method.getDeclaringClass();
      return ReflectUtil.privateLookupIn(declaringClass).unreflectSpecial(method, declaringClass).bindTo(proxy).invokeWithArguments(args);
   }

   void invalidate() {
      log.trace("cache invalidate");
      this.cache.invalidateAll();
   }
}
