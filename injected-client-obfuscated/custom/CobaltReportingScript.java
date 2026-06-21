package custom;

public class CobaltReportingScript {
   private final boolean enabled;

   public CobaltReportingScript(boolean var1) {
      this.enabled = var1;
   }

   public boolean isEnabled() {
      return this.enabled;
   }

   public String toString() {
      return "CobaltReportingScript(enabled=" + this.isEnabled() + ")";
   }
}
