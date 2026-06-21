package osrs;

import net.runelite.api.Nameable;
import net.runelite.api.events.NameableNameChanged;

public class gH implements Comparable<Nameable>, Nameable {
   public I a;
   public I b;

   public I a() {
      return this.b;
   }

   public String b() {
      return this.b == null ? "" : this.b.a();
   }

   public String c() {
      return this.a == null ? "" : this.a.a();
   }

   public void a(I var1, I var2) {
      if (var1 == null) {
         throw new NullPointerException();
      } else {
         this.b = var1;
         this.a = var2;
         this.a(-1);
      }
   }

   public int a(gH var1) {
      return this.b.a(var1.b);
   }

   public int a(Nameable var1) {
      return this.a((gH)var1);
   }

   public void a(int var1) {
      NameableNameChanged var2 = new NameableNameChanged(this);
      Client.s.getCallbacks().post(var2);
   }

   public String getName() {
      return this.e().d();
   }

   public String getPrevName() {
      I var1 = this.d();
      return var1 == null ? null : var1.d();
   }

   public I d() {
      return this.a;
   }

   public I e() {
      return this.b;
   }
}
