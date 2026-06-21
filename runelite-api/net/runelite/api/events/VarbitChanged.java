package net.runelite.api.events;

public class VarbitChanged {
   private int varpId = -1;
   private int varbitId = -1;
   private int value;

   /** @deprecated */
   @Deprecated
   public int getIndex() {
      return this.varpId;
   }

   public int getVarpId() {
      return this.varpId;
   }

   public int getVarbitId() {
      return this.varbitId;
   }

   public int getValue() {
      return this.value;
   }

   public void setVarpId(int varpId) {
      this.varpId = varpId;
   }

   public void setVarbitId(int varbitId) {
      this.varbitId = varbitId;
   }

   public void setValue(int value) {
      this.value = value;
   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof VarbitChanged)) {
         return false;
      } else {
         VarbitChanged other = (VarbitChanged)o;
         if (!other.canEqual(this)) {
            return false;
         } else if (this.getVarpId() != other.getVarpId()) {
            return false;
         } else if (this.getVarbitId() != other.getVarbitId()) {
            return false;
         } else {
            return this.getValue() == other.getValue();
         }
      }
   }

   protected boolean canEqual(Object other) {
      return other instanceof VarbitChanged;
   }

   public int hashCode() {
      int PRIME = true;
      int result = 1;
      result = result * 59 + this.getVarpId();
      result = result * 59 + this.getVarbitId();
      result = result * 59 + this.getValue();
      return result;
   }

   public String toString() {
      int var10000 = this.getVarpId();
      return "VarbitChanged(varpId=" + var10000 + ", varbitId=" + this.getVarbitId() + ", value=" + this.getValue() + ")";
   }
}
