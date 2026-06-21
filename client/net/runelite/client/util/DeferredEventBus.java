package net.runelite.client.util;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import javax.inject.Inject;
import javax.inject.Singleton;
import net.runelite.client.eventbus.EventBus;

@Singleton
public class DeferredEventBus extends EventBus {
   private final EventBus eventBus;
   private final Queue<Object> pendingEvents = new ConcurrentLinkedQueue();

   @Inject
   private DeferredEventBus(EventBus eventBus) {
      this.eventBus = eventBus;
   }

   public void register(Object object) {
      this.eventBus.register(object);
   }

   public void unregister(Object object) {
      this.eventBus.unregister(object);
   }

   public void post(Object object) {
      this.pendingEvents.add(object);
   }

   public void replay() {
      int size = this.pendingEvents.size();

      while(size-- > 0) {
         Object object = this.pendingEvents.poll();
         this.eventBus.post(object);
      }

   }
}
