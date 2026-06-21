package net.runelite.client.ui;

import java.awt.Image;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;
import java.util.function.Supplier;
import javax.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
public class DrawManager {
   private static final Logger log = LoggerFactory.getLogger(DrawManager.class);
   private final List<Runnable> everyFrame = new CopyOnWriteArrayList();
   private final Queue<Consumer<Image>> nextFrame = new ConcurrentLinkedQueue();

   public void registerEveryFrameListener(Runnable everyFrameListener) {
      if (!this.everyFrame.contains(everyFrameListener)) {
         this.everyFrame.add(everyFrameListener);
      }

   }

   public void unregisterEveryFrameListener(Runnable everyFrameListener) {
      this.everyFrame.remove(everyFrameListener);
   }

   public void requestNextFrameListener(Consumer<Image> nextFrameListener) {
      this.nextFrame.add(nextFrameListener);
   }

   public void processDrawComplete(Supplier<Image> imageSupplier) {
      Iterator var2 = this.everyFrame.iterator();

      Exception e;
      while(var2.hasNext()) {
         Runnable everyFrameListener = (Runnable)var2.next();

         try {
            everyFrameListener.run();
         } catch (Exception var7) {
            e = var7;
            log.error("Error in draw consumer", e);
         }
      }

      Consumer<Image> nextFrameListener = (Consumer)this.nextFrame.poll();

      for(Image image = null; nextFrameListener != null; nextFrameListener = (Consumer)this.nextFrame.poll()) {
         if (image == null) {
            try {
               image = (Image)imageSupplier.get();
            } catch (Exception var6) {
               e = var6;
               log.warn("error getting screenshot", e);
            }
         }

         if (image == null) {
            this.nextFrame.clear();
            break;
         }

         try {
            nextFrameListener.accept(image);
         } catch (Exception var5) {
            e = var5;
            log.error("Error in draw consumer", e);
         }
      }

   }
}
