package net.runelite.client.eventbus;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Iterables;
import com.google.common.collect.UnmodifiableIterator;
import java.lang.invoke.CallSite;
import java.lang.invoke.LambdaMetafactory;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Comparator;
import java.util.function.Consumer;
import javax.annotation.Nonnull;
import javax.annotation.concurrent.ThreadSafe;
import net.runelite.client.externalplugins.ExternalPluginMdc;
import net.runelite.client.util.ReflectUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

@ThreadSafe
public class EventBus {
   private static final Logger log = LoggerFactory.getLogger(EventBus.class);
   private static final Marker DEDUPLICATE = MarkerFactory.getMarker("DEDUPLICATE");
   private final Consumer<Throwable> exceptionHandler;
   @Nonnull
   private ImmutableMultimap<Class<?>, Subscriber> subscribers;

   public EventBus() {
      this((e) -> {
         log.warn(DEDUPLICATE, "Uncaught exception in event subscriber", e);
      });
   }

   public synchronized void register(@Nonnull Object object) {
      ImmutableMultimap.Builder<Class<?>, Subscriber> builder = ImmutableMultimap.builder();
      builder.putAll(this.subscribers);
      builder.orderValuesBy(Comparator.comparingDouble(Subscriber::getPriority).reversed().thenComparing((s) -> {
         return s.object.getClass().getName();
      }));

      for(Class<?> clazz = object.getClass(); clazz != null; clazz = clazz.getSuperclass()) {
         Method[] var4 = clazz.getDeclaredMethods();
         int var5 = var4.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            Method method = var4[var6];
            Subscribe sub = (Subscribe)method.getAnnotation(Subscribe.class);
            if (sub != null) {
               boolean var10000 = method.getReturnType() == Void.TYPE;
               String var10001 = String.valueOf(method);
               Preconditions.checkArgument(var10000, "@Subscribed method \"" + var10001 + "\" cannot return a value");
               var10000 = method.getParameterCount() == 1;
               var10001 = String.valueOf(method);
               Preconditions.checkArgument(var10000, "@Subscribed method \"" + var10001 + "\" must take exactly 1 argument");
               var10000 = !Modifier.isStatic(method.getModifiers());
               var10001 = String.valueOf(method);
               Preconditions.checkArgument(var10000, "@Subscribed method \"" + var10001 + "\" cannot be static");
               Class<?> parameterClazz = method.getParameterTypes()[0];
               var10000 = !parameterClazz.isPrimitive();
               var10001 = String.valueOf(method);
               Preconditions.checkArgument(var10000, "@Subscribed method \"" + var10001 + "\" cannot subscribe to primitives");
               Preconditions.checkArgument((parameterClazz.getModifiers() & 1536) == 0, "@Subscribed method \"" + String.valueOf(method) + "\" cannot subscribe to polymorphic classes");

               for(Class<?> psc = parameterClazz.getSuperclass(); psc != null; psc = psc.getSuperclass()) {
                  if (this.subscribers.containsKey(psc)) {
                     String var10002 = String.valueOf(method);
                     throw new IllegalArgumentException("@Subscribed method \"" + var10002 + "\" cannot subscribe to class which inherits from subscribed class \"" + String.valueOf(psc) + "\"");
                  }
               }

               String preferredName = "on" + parameterClazz.getSimpleName();
               var10000 = method.getName().equals(preferredName);
               var10001 = String.valueOf(method);
               Preconditions.checkArgument(var10000, "Subscribed method " + var10001 + " should be named " + preferredName);
               method.setAccessible(true);
               Consumer<Object> lambda = null;

               try {
                  MethodHandles.Lookup caller = ReflectUtil.privateLookupIn(clazz);
                  MethodType subscription = MethodType.methodType(Void.TYPE, parameterClazz);
                  MethodHandle target = caller.findVirtual(clazz, method.getName(), subscription);
                  CallSite site = LambdaMetafactory.metafactory(caller, "accept", MethodType.methodType(Consumer.class, clazz), subscription.changeParameterType(0, Object.class), target, subscription);
                  MethodHandle factory = site.getTarget();
                  lambda = factory.bindTo(object).invokeExact();
               } catch (Throwable var17) {
                  Throwable e = var17;
                  log.warn("Unable to create lambda for method {}", method, e);
               }

               Subscriber subscriber = new Subscriber(object, method, sub.priority(), lambda);
               builder.put(parameterClazz, subscriber);
               log.debug("Registering {} - {}", parameterClazz, subscriber);
            }
         }
      }

      this.subscribers = builder.build();
   }

   public synchronized <T> Subscriber register(Class<T> clazz, Consumer<T> subFn, float priority) {
      ImmutableMultimap.Builder<Class<?>, Subscriber> builder = ImmutableMultimap.builder();
      builder.putAll(this.subscribers);
      builder.orderValuesBy(Comparator.comparingDouble(Subscriber::getPriority).reversed().thenComparing((s) -> {
         return s.object.getClass().getName();
      }));
      Subscriber sub = new Subscriber(subFn, (Method)null, priority, subFn);
      builder.put(clazz, sub);
      this.subscribers = builder.build();
      return sub;
   }

   public synchronized void unregister(@Nonnull Object object) {
      this.subscribers = ImmutableMultimap.copyOf(Iterables.filter(this.subscribers.entries(), (e) -> {
         return ((Subscriber)e.getValue()).getObject() != object;
      }));
   }

   public synchronized void unregister(Subscriber sub) {
      if (sub != null) {
         this.subscribers = ImmutableMultimap.copyOf(Iterables.filter(this.subscribers.entries(), (e) -> {
            return sub != e.getValue();
         }));
      }
   }

   public void post(@Nonnull Object event) {
      UnmodifiableIterator var2 = this.subscribers.get(event.getClass()).iterator();

      while(var2.hasNext()) {
         Subscriber subscriber = (Subscriber)var2.next();
         ExternalPluginMdc.ScopedMdc pluginMdc = ExternalPluginMdc.open(subscriber.getObject());

         try {
            subscriber.invoke(event);
         } catch (Exception var9) {
            Exception e = var9;
            this.exceptionHandler.accept(e);
         } finally {
            pluginMdc.close();
         }
      }

   }

   public EventBus(Consumer<Throwable> exceptionHandler) {
      this.subscribers = ImmutableMultimap.of();
      this.exceptionHandler = exceptionHandler;
   }

   public static final class Subscriber {
      private final Object object;
      private final Method method;
      private final float priority;
      private final Consumer<Object> lambda;

      void invoke(Object arg) throws Exception {
         if (this.lambda != null) {
            this.lambda.accept(arg);
         } else {
            this.method.invoke(this.object, arg);
         }

      }

      public Subscriber(Object object, Method method, float priority, Consumer<Object> lambda) {
         this.object = object;
         this.method = method;
         this.priority = priority;
         this.lambda = lambda;
      }

      public Object getObject() {
         return this.object;
      }

      public Method getMethod() {
         return this.method;
      }

      public float getPriority() {
         return this.priority;
      }

      public Consumer<Object> getLambda() {
         return this.lambda;
      }

      public boolean equals(Object o) {
         if (o == this) {
            return true;
         } else if (!(o instanceof Subscriber)) {
            return false;
         } else {
            Subscriber other = (Subscriber)o;
            if (Float.compare(this.getPriority(), other.getPriority()) != 0) {
               return false;
            } else {
               Object this$object = this.getObject();
               Object other$object = other.getObject();
               if (this$object == null) {
                  if (other$object != null) {
                     return false;
                  }
               } else if (!this$object.equals(other$object)) {
                  return false;
               }

               Object this$method = this.getMethod();
               Object other$method = other.getMethod();
               if (this$method == null) {
                  if (other$method != null) {
                     return false;
                  }
               } else if (!this$method.equals(other$method)) {
                  return false;
               }

               return true;
            }
         }
      }

      public int hashCode() {
         int PRIME = true;
         int result = 1;
         result = result * 59 + Float.floatToIntBits(this.getPriority());
         Object $object = this.getObject();
         result = result * 59 + ($object == null ? 43 : $object.hashCode());
         Object $method = this.getMethod();
         result = result * 59 + ($method == null ? 43 : $method.hashCode());
         return result;
      }

      public String toString() {
         String var10000 = String.valueOf(this.getObject());
         return "EventBus.Subscriber(object=" + var10000 + ", method=" + String.valueOf(this.getMethod()) + ", priority=" + this.getPriority() + ", lambda=" + String.valueOf(this.getLambda()) + ")";
      }
   }
}
