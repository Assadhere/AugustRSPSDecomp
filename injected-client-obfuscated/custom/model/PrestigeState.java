package custom.model;

public enum PrestigeState {
   UnderLevel(0),
   CanRankUp(1),
   Maxed(2);

   final int value;

   private PrestigeState(int var3) {
      this.value = var3;
   }
}
