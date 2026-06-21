package osrs;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class bV {
   public ExecutorService a = Executors.newSingleThreadExecutor();
   public final aR b;
   public final gK c;
   public Future d;

   public bV(aR var1, gK var2) {
      this.b = var1;
      this.c = var2;
      this.d();
   }

   public boolean a() {
      return this.d.isDone();
   }

   public void b() {
      this.a.shutdown();
      this.a = null;
   }

   public aR c() {
      try {
         return (aR)this.d.get();
      } catch (Exception var2) {
         return null;
      }
   }

   public void d() {
      this.d = this.a.submit(new a(this.b, this.c));
   }

   public class a implements Callable {
      public final aR a;
      public final gK b;

      public a(aR var2, gK var3) {
         this.a = var2;
         this.b = var3;
      }

      public Object call() {
         return this.b.a(this.a);
      }
   }
}
