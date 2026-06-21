package custom;

import java.util.List;

public class CobaltListChunkScript {
   private final List<CobaltPlayerRow> rows;

   public CobaltListChunkScript(List<CobaltPlayerRow> var1) {
      this.rows = var1;
   }

   public List<CobaltPlayerRow> getRows() {
      return this.rows;
   }

   public String toString() {
      return "CobaltListChunkScript(rows=" + String.valueOf(this.getRows()) + ")";
   }
}
