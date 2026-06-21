package osrs;

public class gA extends gn {
   public eX f = null;
   public int g = 0;

   public gA(gn var1, int var2, boolean var3, int var4) {
      super(var1);
      this.c = "FadeOutTask";
      if (var2 >= 0) {
         if (var3 && var2 < eZ.c.size()) {
            this.f = (eX)eZ.c.get(var2);
         } else if (!var3 && var2 < eZ.d.size()) {
            this.f = (eX)eZ.d.get(var2);
         }

         this.g = var4;
      }

   }

   public boolean e() {
      if (this.f != null && this.f.j != null) {
         this.f.f = true;

         try {
            if (this.f.d > 0.0F && this.f.j.f()) {
               float var1 = this.g == 0 ? (float)this.g : (float)this.f.c / (float)this.g;
               eX var2 = this.f;
               var2.d -= var1 == 0.0F ? (float)this.f.c : var1;
               if (this.f.d < 0.0F) {
                  this.f.d = 0.0F;
               }

               this.f.j.a((int)this.f.d);
               return false;
            }
         } catch (Exception var3) {
            this.a(var3.getMessage());
            return true;
         }

         this.f.f = false;
         return true;
      } else {
         return true;
      }
   }
}
