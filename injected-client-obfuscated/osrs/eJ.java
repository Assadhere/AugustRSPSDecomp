package osrs;

import java.lang.ref.SoftReference;

public class eJ extends eG {
   public SoftReference a;

   public eJ(Object var1, int var2) {
      super(var2);
      this.a = new SoftReference(var1);
   }

   public Object a() {
      return this.a.get();
   }

   public boolean b() {
      return true;
   }
}
