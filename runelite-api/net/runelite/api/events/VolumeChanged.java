package net.runelite.api.events;

public final class VolumeChanged {
   private final Type type;

   public VolumeChanged(Type type) {
      this.type = type;
   }

   public Type getType() {
      return this.type;
   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof VolumeChanged)) {
         return false;
      } else {
         VolumeChanged other = (VolumeChanged)o;
         Object this$type = this.getType();
         Object other$type = other.getType();
         if (this$type == null) {
            if (other$type != null) {
               return false;
            }
         } else if (!this$type.equals(other$type)) {
            return false;
         }

         return true;
      }
   }

   public int hashCode() {
      int PRIME = true;
      int result = 1;
      Object $type = this.getType();
      result = result * 59 + ($type == null ? 43 : $type.hashCode());
      return result;
   }

   public String toString() {
      return "VolumeChanged(type=" + String.valueOf(this.getType()) + ")";
   }

   public static enum Type {
      MUSIC,
      EFFECTS,
      AREA;
   }
}
