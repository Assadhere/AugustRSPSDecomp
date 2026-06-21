package osrs;

import java.io.IOException;
import java.io.PrintStream;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class cX {
   public final String a;
   public final int b;
   public final ThreadFactory c;
   public final ThreadPoolExecutor d;

   public cX(String var1, int var2, int var3) {
      this.a = var1;
      this.b = var2;
      this.c = new a();
      this.d = this.a(var3, -1762975523);
   }

   public cG a(bO var1) {
      if (this.d.getQueue().remainingCapacity() <= 0) {
         PrintStream var10000 = System.err;
         int var10001 = this.d.getCorePoolSize();
         var10000.println("REST thread pool queue is empty\r\nThread pool size " + var10001 + " Queue capacity " + this.b);
         return new cG("Queue full");
      } else {
         cG var2 = new cG(this.d.submit(new b(var1)));
         return var2;
      }
   }

   public void a() {
      try {
         this.d.shutdown();
      } catch (Exception var2) {
         System.err.println("Error shutting down RestRequestService\r\n" + String.valueOf(var2));
      }

   }

   public final ThreadPoolExecutor a(int var1, int var2) {
      return new ThreadPoolExecutor(0, var1, 2L, TimeUnit.MINUTES, new ArrayBlockingQueue(this.b), this.c);
   }

   public class a implements ThreadFactory {
      public final AtomicInteger a = new AtomicInteger(1);
      public final ThreadGroup b;

      public a() {
         SecurityManager var2 = System.getSecurityManager();
         this.b = var2 != null ? var2.getThreadGroup() : Thread.currentThread().getThreadGroup();
      }

      public Thread newThread(Runnable var1) {
         Thread var2 = new Thread(this.b, var1, cX.this.a + "-rest-request-" + this.a.getAndIncrement(), 0L);
         var2.setDaemon(true);
         var2.setPriority(5);
         return var2;
      }
   }

   public class b implements Callable {
      public final bO a;

      public b(bO var2) {
         this.a = var2;
      }

      public Object call() throws Exception {
         try {
            while(this.a.c()) {
               jw.a(10L);
            }
         } catch (IOException var2) {
            return new fw("Error servicing REST query: " + var2.getMessage());
         }

         return this.a.d();
      }
   }
}
