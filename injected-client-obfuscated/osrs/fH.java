package osrs;

public final class fH {
   public aA a = new aA();

   public fH() {
      this.a.aR = this.a;
      this.a.aS = this.a;
   }

   public void a(aA var1) {
      if (var1.aS != null) {
         var1.ae();
      }

      var1.aS = this.a.aS;
      var1.aR = this.a;
      var1.aS.aR = var1;
      var1.aR.aS = var1;
   }

   public void b(aA var1) {
      if (var1.aS != null) {
         var1.ae();
      }

      var1.aS = this.a;
      var1.aR = this.a.aR;
      var1.aS.aR = var1;
      var1.aR.aS = var1;
   }

   public aA a() {
      aA var1 = this.a.aR;
      return this.a == var1 ? null : var1;
   }
}
