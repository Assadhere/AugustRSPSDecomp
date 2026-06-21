package net.runelite.client.task;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.Duration;
import java.time.Instant;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ScheduledExecutorService;
import javax.inject.Inject;
import javax.inject.Singleton;
import net.runelite.client.externalplugins.ExternalPluginMdc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
public class Scheduler {
   private static final Logger log = LoggerFactory.getLogger(Scheduler.class);
   private final List<ScheduledMethod> scheduledMethods = new CopyOnWriteArrayList();
   @Inject
   ScheduledExecutorService executor;

   public void addScheduledMethod(ScheduledMethod method) {
      this.scheduledMethods.add(method);
   }

   public void removeScheduledMethod(ScheduledMethod method) {
      this.scheduledMethods.remove(method);
   }

   public List<ScheduledMethod> getScheduledMethods() {
      return Collections.unmodifiableList(this.scheduledMethods);
   }

   public void tick() {
      Instant now = Instant.now();
      Iterator var2 = this.scheduledMethods.iterator();

      while(var2.hasNext()) {
         ScheduledMethod scheduledMethod = (ScheduledMethod)var2.next();
         Instant last = scheduledMethod.getLast();
         Duration difference = Duration.between(last, now);
         Schedule schedule = scheduledMethod.getSchedule();
         Duration timeSinceRun = Duration.of(schedule.period(), schedule.unit());
         if (difference.compareTo(timeSinceRun) > 0) {
            log.trace("Scheduled task triggered: {}", scheduledMethod);
            scheduledMethod.setLast(now);
            if (schedule.asynchronous()) {
               this.executor.submit(() -> {
                  this.run(scheduledMethod);
               });
            } else {
               this.run(scheduledMethod);
            }
         }
      }

   }

   private void run(ScheduledMethod scheduledMethod) {
      ExternalPluginMdc.ScopedMdc pluginMdc = ExternalPluginMdc.open(scheduledMethod.getObject());

      try {
         Runnable lambda = scheduledMethod.getLambda();
         if (lambda != null) {
            lambda.run();
         } else {
            Method method = scheduledMethod.getMethod();
            method.invoke(scheduledMethod.getObject());
         }
      } catch (IllegalArgumentException | InvocationTargetException | IllegalAccessException var9) {
         Exception ex = var9;
         log.warn("error invoking scheduled task", ex);
      } catch (Exception var10) {
         Exception ex = var10;
         log.warn("error during scheduled task", ex);
      } finally {
         pluginMdc.close();
      }

   }
}
