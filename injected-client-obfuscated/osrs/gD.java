package osrs;

public class gD extends gn {
   public gD(gn var1) {
      super(var1);
      this.c = "SwapSongTask";
   }

   public boolean e() {
      if (eZ.d.size() > 1 && eZ.d.get(0) != null && ((eX)eZ.d.get(0)).j.f() && eZ.d.get(1) != null && ((eX)eZ.d.get(1)).j.f()) {
         eX var1 = (eX)eZ.d.get(0);
         eZ.d.set(0, eZ.d.get(1));
         eZ.d.set(1, var1);
      }

      return true;
   }
}
