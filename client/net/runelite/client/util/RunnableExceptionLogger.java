package net.runelite.client.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RunnableExceptionLogger implements Runnable {
   private static final Logger log = LoggerFactory.getLogger(RunnableExceptionLogger.class);
   private final Runnable runnable;

   public void run() {
      try {
         this.runnable.run();
      } catch (Throwable var2) {
         Throwable ex = var2;
         log.error("Uncaught exception in runnable {}", this.runnable, ex);
         throw ex;
      }
   }

   public static RunnableExceptionLogger wrap(Runnable runnable) {
      return new RunnableExceptionLogger(runnable);
   }

   public RunnableExceptionLogger(Runnable runnable) {
      this.runnable = runnable;
   }
}
