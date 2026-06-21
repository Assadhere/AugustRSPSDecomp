package osrs;

public class aA extends az {
   public long aQ;
   public aA aR;
   public aA aS;

   public void ae() {
      if (this.aS != null) {
         this.aS.aR = this.aR;
         this.aR.aS = this.aS;
         this.aR = null;
         this.aS = null;
      }

   }

   public aA af() {
      return this.aR;
   }

   public void ag() {
      this.ae();
   }
}
