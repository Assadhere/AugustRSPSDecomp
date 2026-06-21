package net.runelite.client.events;

import java.time.Duration;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class ClientShutdown {
   private static final Logger log = LoggerFactory.getLogger(ClientShutdown.class);
   private final Queue<Future<?>> tasks = new ConcurrentLinkedQueue();

   public void waitFor(Future<?> future) {
      this.tasks.add(future);
   }

   public void waitForAllConsumers(Duration totalTimeout) {
      long deadline = System.nanoTime() + totalTimeout.toNanos();

      Future task;
      while((task = (Future)this.tasks.poll()) != null) {
         long timeout = deadline - System.nanoTime();
         if (timeout < 0L) {
            log.warn("Timed out waiting for task completion");
            return;
         }

         try {
            task.get(timeout, TimeUnit.NANOSECONDS);
         } catch (ThreadDeath var8) {
            ThreadDeath d = var8;
            throw d;
         } catch (Throwable var9) {
            Throwable t = var9;
            log.warn("Error during shutdown: ", t);
         }
      }

   }

   public Queue<Future<?>> getTasks() {
      return this.tasks;
   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof ClientShutdown)) {
         return false;
      } else {
         ClientShutdown other = (ClientShutdown)o;
         Object this$tasks = this.getTasks();
         Object other$tasks = other.getTasks();
         if (this$tasks == null) {
            if (other$tasks != null) {
               return false;
            }
         } else if (!this$tasks.equals(other$tasks)) {
            return false;
         }

         return true;
      }
   }

   public int hashCode() {
      int PRIME = true;
      int result = 1;
      Object $tasks = this.getTasks();
      result = result * 59 + ($tasks == null ? 43 : $tasks.hashCode());
      return result;
   }

   public String toString() {
      return "ClientShutdown(tasks=" + String.valueOf(this.getTasks()) + ")";
   }
}
