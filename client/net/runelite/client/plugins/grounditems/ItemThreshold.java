package net.runelite.client.plugins.grounditems;

import com.google.common.base.Strings;

final class ItemThreshold {
   private final String name;
   private final int quantity;
   private final Inequality inequality;
   private final boolean wildcard;

   static ItemThreshold fromName(String entry) {
      if (Strings.isNullOrEmpty(entry)) {
         return null;
      } else {
         Inequality operator = ItemThreshold.Inequality.MORE_THAN;
         int qty = 0;
         boolean wildcard = entry.contains("*");

         for(int i = entry.length() - 1; i >= 0; --i) {
            char c = entry.charAt(i);
            if ((c < '0' || c > '9') && !Character.isWhitespace(c)) {
               switch (c) {
                  case '<':
                     operator = ItemThreshold.Inequality.LESS_THAN;
                  case '>':
                     if (i + 1 < entry.length()) {
                        try {
                           qty = Integer.parseInt(entry.substring(i + 1).trim());
                        } catch (NumberFormatException var7) {
                           qty = 0;
                           operator = ItemThreshold.Inequality.MORE_THAN;
                        }

                        entry = entry.substring(0, i);
                     }

                     return new ItemThreshold(entry.trim(), qty, operator, wildcard);
                  default:
                     return new ItemThreshold(entry.trim(), qty, operator, wildcard);
               }
            }
         }

         return new ItemThreshold(entry.trim(), qty, operator, wildcard);
      }
   }

   boolean quantityHolds(int itemCount) {
      if (this.inequality == ItemThreshold.Inequality.LESS_THAN) {
         return itemCount < this.quantity;
      } else {
         return itemCount > this.quantity;
      }
   }

   public ItemThreshold(String name, int quantity, Inequality inequality, boolean wildcard) {
      this.name = name;
      this.quantity = quantity;
      this.inequality = inequality;
      this.wildcard = wildcard;
   }

   public String getName() {
      return this.name;
   }

   public int getQuantity() {
      return this.quantity;
   }

   public Inequality getInequality() {
      return this.inequality;
   }

   public boolean isWildcard() {
      return this.wildcard;
   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof ItemThreshold)) {
         return false;
      } else {
         ItemThreshold other = (ItemThreshold)o;
         if (this.getQuantity() != other.getQuantity()) {
            return false;
         } else if (this.isWildcard() != other.isWildcard()) {
            return false;
         } else {
            Object this$name = this.getName();
            Object other$name = other.getName();
            if (this$name == null) {
               if (other$name != null) {
                  return false;
               }
            } else if (!this$name.equals(other$name)) {
               return false;
            }

            Object this$inequality = this.getInequality();
            Object other$inequality = other.getInequality();
            if (this$inequality == null) {
               if (other$inequality != null) {
                  return false;
               }
            } else if (!this$inequality.equals(other$inequality)) {
               return false;
            }

            return true;
         }
      }
   }

   public int hashCode() {
      int PRIME = true;
      int result = 1;
      result = result * 59 + this.getQuantity();
      result = result * 59 + (this.isWildcard() ? 79 : 97);
      Object $name = this.getName();
      result = result * 59 + ($name == null ? 43 : $name.hashCode());
      Object $inequality = this.getInequality();
      result = result * 59 + ($inequality == null ? 43 : $inequality.hashCode());
      return result;
   }

   public String toString() {
      String var10000 = this.getName();
      return "ItemThreshold(name=" + var10000 + ", quantity=" + this.getQuantity() + ", inequality=" + String.valueOf(this.getInequality()) + ", wildcard=" + this.isWildcard() + ")";
   }

   static enum Inequality {
      LESS_THAN,
      MORE_THAN;
   }
}
