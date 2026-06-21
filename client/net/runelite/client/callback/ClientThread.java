package net.runelite.client.callback;

import com.google.inject.Inject;
import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.BooleanSupplier;
import javax.inject.Singleton;
import net.runelite.api.Client;
import net.runelite.client.externalplugins.ExternalPluginMdc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
public class ClientThread {
   private static final Logger log = LoggerFactory.getLogger(ClientThread.class);
   private final ConcurrentLinkedQueue<BooleanSupplier> invokes = new ConcurrentLinkedQueue();
   private final ConcurrentLinkedQueue<BooleanSupplier> invokesAtTickEnd = new ConcurrentLinkedQueue();
   @Inject
   private Client client;

   public void invoke(Runnable r) {
      this.invoke(() -> {
         r.run();
         return true;
      });
   }

   public void invoke(BooleanSupplier r) {
      if (this.client.isClientThread()) {
         if (!r.getAsBoolean()) {
            this.invokes.add(r);
         }

      } else {
         this.invokeLater(r);
      }
   }

   public void invokeLater(Runnable r) {
      this.invokeLater(() -> {
         r.run();
         return true;
      });
   }

   public void invokeLater(BooleanSupplier r) {
      this.invokes.add(r);
   }

   public void invokeAtTickEnd(Runnable r) {
      this.invokesAtTickEnd.add(() -> {
         r.run();
         return true;
      });
   }

   void invoke() {
      this.invokeList(this.invokes);
   }

   void invokeTickEnd() {
      this.invokeList(this.invokesAtTickEnd);
   }

   private void invokeList(ConcurrentLinkedQueue<BooleanSupplier> invokes) {
      assert this.client.isClientThread();

      Iterator<BooleanSupplier> ir = invokes.iterator();

      while(ir.hasNext()) {
         BooleanSupplier r = (BooleanSupplier)ir.next();
         boolean remove = true;
         ExternalPluginMdc.ScopedMdc pluginMdc = ExternalPluginMdc.open((Object)r);

         try {
            remove = r.getAsBoolean();
         } catch (ThreadDeath var11) {
            ThreadDeath d = var11;
            throw d;
         } catch (Throwable var12) {
            Throwable e = var12;
            log.error("Exception in invoke", e);
         } finally {
            pluginMdc.close();
         }

         if (remove) {
            ir.remove();
         } else {
            log.trace("Deferring task {}", r);
         }
      }

   }
}
