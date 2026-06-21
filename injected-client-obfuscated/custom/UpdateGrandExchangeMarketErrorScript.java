package custom;

public class UpdateGrandExchangeMarketErrorScript {
   private final int requestId;
   private final int triggerId;
   private final String code;
   private final String message;

   public UpdateGrandExchangeMarketErrorScript(int var1, int var2, String var3, String var4) {
      this.requestId = var1;
      this.triggerId = var2;
      this.code = var3;
      this.message = var4;
   }

   public int getRequestId() {
      return this.requestId;
   }

   public int getTriggerId() {
      return this.triggerId;
   }

   public String getCode() {
      return this.code;
   }

   public String getMessage() {
      return this.message;
   }
}
