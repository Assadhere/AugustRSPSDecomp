package osrs;

public class iF implements Runnable {
   public volatile aQ[] a = new aQ[2];

   public void run() {
      this.a();

      try {
         for(int var1 = 0; var1 < 2; ++var1) {
            aQ var2 = this.a[var1];
            if (var2 != null) {
               var2.a();
            }
         }
      } catch (Exception var3) {
         gG.a((String)null, var3, (short)9230);
      }

   }

   public void a() {
      Thread.currentThread().setName("Sound Engine");
   }
}
