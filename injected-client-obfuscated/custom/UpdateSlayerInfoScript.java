package custom;

public class UpdateSlayerInfoScript {
   private final int taskAmountRemaining;
   private final int taskAmountInitial;
   private final int taskStreak;
   private final int currentSlayerPoints;
   private final String taskName;
   private final String taskLocation;

   public UpdateSlayerInfoScript(int var1, int var2, int var3, int var4, String var5, String var6) {
      this.taskAmountRemaining = var1;
      this.taskAmountInitial = var2;
      this.taskStreak = var3;
      this.currentSlayerPoints = var4;
      this.taskName = var5;
      this.taskLocation = var6;
   }

   public int getTaskAmountRemaining() {
      return this.taskAmountRemaining;
   }

   public int getTaskAmountInitial() {
      return this.taskAmountInitial;
   }

   public int getTaskStreak() {
      return this.taskStreak;
   }

   public int getCurrentSlayerPoints() {
      return this.currentSlayerPoints;
   }

   public String getTaskName() {
      return this.taskName;
   }

   public String getTaskLocation() {
      return this.taskLocation;
   }
}
