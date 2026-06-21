package custom;

public class UpdateTimerScript {
   private final int timerId;
   private final boolean isSpriteId;
   private final int ticksRemaining;
   private final String name;
   private final String description;

   public UpdateTimerScript(int var1, boolean var2, int var3, String var4, String var5) {
      this.timerId = var1;
      this.isSpriteId = var2;
      this.ticksRemaining = var3;
      this.name = var4;
      this.description = var5;
   }

   public int getTimerId() {
      return this.timerId;
   }

   public boolean isSpriteId() {
      return this.isSpriteId;
   }

   public int getTicksRemaining() {
      return this.ticksRemaining;
   }

   public String getName() {
      return this.name;
   }

   public String getDescription() {
      return this.description;
   }

   public String toString() {
      int var10000 = this.getTimerId();
      return "UpdateTimerScript(timerId=" + var10000 + ", isSpriteId=" + this.isSpriteId() + ", ticksRemaining=" + this.getTicksRemaining() + ", name=" + this.getName() + ", description=" + this.getDescription() + ")";
   }
}
