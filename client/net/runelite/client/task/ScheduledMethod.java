package net.runelite.client.task;

import java.lang.reflect.Method;
import java.time.Instant;

public class ScheduledMethod {
   private final Schedule schedule;
   private final Method method;
   private final Object object;
   private final Runnable lambda;
   private Instant last = Instant.now();

   public ScheduledMethod(Schedule schedule, Method method, Object object, Runnable lambda) {
      this.schedule = schedule;
      this.method = method;
      this.object = object;
      this.lambda = lambda;
   }

   public String toString() {
      String var10000 = String.valueOf(this.getSchedule());
      return "ScheduledMethod(schedule=" + var10000 + ", method=" + String.valueOf(this.getMethod()) + ", object=" + String.valueOf(this.getObject()) + ", lambda=" + String.valueOf(this.getLambda()) + ", last=" + String.valueOf(this.getLast()) + ")";
   }

   public Schedule getSchedule() {
      return this.schedule;
   }

   public Method getMethod() {
      return this.method;
   }

   public Object getObject() {
      return this.object;
   }

   public Runnable getLambda() {
      return this.lambda;
   }

   public Instant getLast() {
      return this.last;
   }

   public void setLast(Instant last) {
      this.last = last;
   }
}
