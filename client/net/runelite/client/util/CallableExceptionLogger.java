package net.runelite.client.util;

import java.util.concurrent.Callable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CallableExceptionLogger<V> implements Callable<V> {
   private static final Logger log = LoggerFactory.getLogger(CallableExceptionLogger.class);
   private final Callable<V> callable;

   public V call() throws Exception {
      try {
         return this.callable.call();
      } catch (Throwable var2) {
         Throwable ex = var2;
         log.error("Uncaught exception in callable {}", this.callable, ex);
         throw ex;
      }
   }

   public static <V> CallableExceptionLogger<V> wrap(Callable<V> callable) {
      return new CallableExceptionLogger(callable);
   }

   public CallableExceptionLogger(Callable<V> callable) {
      this.callable = callable;
   }
}
