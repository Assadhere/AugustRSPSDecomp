package net.runelite.client.util;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.turbo.TurboFilter;
import ch.qos.logback.core.spi.FilterReply;
import java.util.Arrays;
import java.util.Deque;
import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedDeque;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

public class DeduplicationFilter extends TurboFilter {
   private static final Marker deduplicateMarker = MarkerFactory.getMarker("DEDUPLICATE");
   private static final int CACHE_SIZE = 8;
   private static final int DUPLICATE_LOG_COUNT = 1000;
   private final Deque<LogException> cache = new ConcurrentLinkedDeque();

   public void stop() {
      this.cache.clear();
      super.stop();
   }

   public FilterReply decide(Marker marker, Logger logger, Level level, String s, Object[] objects, Throwable throwable) {
      if (marker == deduplicateMarker && !logger.isDebugEnabled() && throwable != null) {
         LogException logException = new LogException(s, throwable.getStackTrace());
         Iterator var8 = this.cache.iterator();

         while(var8.hasNext()) {
            LogException e = (LogException)var8.next();
            if (logException.equals(e)) {
               if (++e.count % 1000 == 0) {
                  logger.warn("following log message logged 1000 times!");
                  return FilterReply.NEUTRAL;
               }

               return FilterReply.DENY;
            }
         }

         synchronized(this.cache) {
            if (this.cache.size() >= 8) {
               this.cache.pop();
            }

            this.cache.push(logException);
         }

         return FilterReply.NEUTRAL;
      } else {
         return FilterReply.NEUTRAL;
      }
   }

   private static class LogException {
      private final String message;
      private final StackTraceElement[] stackTraceElements;
      private volatile int count;

      public LogException(String message, StackTraceElement[] stackTraceElements) {
         this.message = message;
         this.stackTraceElements = stackTraceElements;
      }

      public boolean equals(Object o) {
         if (o == this) {
            return true;
         } else if (!(o instanceof LogException)) {
            return false;
         } else {
            LogException other = (LogException)o;
            if (!other.canEqual(this)) {
               return false;
            } else {
               Object this$message = this.message;
               Object other$message = other.message;
               if (this$message == null) {
                  if (other$message == null) {
                     return Arrays.deepEquals(this.stackTraceElements, other.stackTraceElements);
                  }
               } else if (this$message.equals(other$message)) {
                  return Arrays.deepEquals(this.stackTraceElements, other.stackTraceElements);
               }

               return false;
            }
         }
      }

      protected boolean canEqual(Object other) {
         return other instanceof LogException;
      }

      public int hashCode() {
         int PRIME = true;
         int result = 1;
         Object $message = this.message;
         result = result * 59 + ($message == null ? 43 : $message.hashCode());
         result = result * 59 + Arrays.deepHashCode(this.stackTraceElements);
         return result;
      }
   }
}
