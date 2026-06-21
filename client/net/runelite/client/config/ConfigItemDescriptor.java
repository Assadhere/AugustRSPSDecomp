package net.runelite.client.config;

import java.lang.reflect.Type;

public final class ConfigItemDescriptor implements ConfigObject {
   private final ConfigItem item;
   private final Type type;
   private final Range range;
   private final Alpha alpha;
   private final Units units;

   public String key() {
      return this.item.keyName();
   }

   public String name() {
      return this.item.name();
   }

   public int position() {
      return this.item.position();
   }

   public ConfigItemDescriptor(ConfigItem item, Type type, Range range, Alpha alpha, Units units) {
      this.item = item;
      this.type = type;
      this.range = range;
      this.alpha = alpha;
      this.units = units;
   }

   public ConfigItem getItem() {
      return this.item;
   }

   public Type getType() {
      return this.type;
   }

   public Range getRange() {
      return this.range;
   }

   public Alpha getAlpha() {
      return this.alpha;
   }

   public Units getUnits() {
      return this.units;
   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof ConfigItemDescriptor)) {
         return false;
      } else {
         ConfigItemDescriptor other = (ConfigItemDescriptor)o;
         Object this$item = this.getItem();
         Object other$item = other.getItem();
         if (this$item == null) {
            if (other$item != null) {
               return false;
            }
         } else if (!this$item.equals(other$item)) {
            return false;
         }

         label61: {
            Object this$type = this.getType();
            Object other$type = other.getType();
            if (this$type == null) {
               if (other$type == null) {
                  break label61;
               }
            } else if (this$type.equals(other$type)) {
               break label61;
            }

            return false;
         }

         label54: {
            Object this$range = this.getRange();
            Object other$range = other.getRange();
            if (this$range == null) {
               if (other$range == null) {
                  break label54;
               }
            } else if (this$range.equals(other$range)) {
               break label54;
            }

            return false;
         }

         Object this$alpha = this.getAlpha();
         Object other$alpha = other.getAlpha();
         if (this$alpha == null) {
            if (other$alpha != null) {
               return false;
            }
         } else if (!this$alpha.equals(other$alpha)) {
            return false;
         }

         Object this$units = this.getUnits();
         Object other$units = other.getUnits();
         if (this$units == null) {
            if (other$units != null) {
               return false;
            }
         } else if (!this$units.equals(other$units)) {
            return false;
         }

         return true;
      }
   }

   public int hashCode() {
      int PRIME = true;
      int result = 1;
      Object $item = this.getItem();
      result = result * 59 + ($item == null ? 43 : $item.hashCode());
      Object $type = this.getType();
      result = result * 59 + ($type == null ? 43 : $type.hashCode());
      Object $range = this.getRange();
      result = result * 59 + ($range == null ? 43 : $range.hashCode());
      Object $alpha = this.getAlpha();
      result = result * 59 + ($alpha == null ? 43 : $alpha.hashCode());
      Object $units = this.getUnits();
      result = result * 59 + ($units == null ? 43 : $units.hashCode());
      return result;
   }

   public String toString() {
      String var10000 = String.valueOf(this.getItem());
      return "ConfigItemDescriptor(item=" + var10000 + ", type=" + String.valueOf(this.getType()) + ", range=" + String.valueOf(this.getRange()) + ", alpha=" + String.valueOf(this.getAlpha()) + ", units=" + String.valueOf(this.getUnits()) + ")";
   }
}
