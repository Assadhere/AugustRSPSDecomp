package net.runelite.api.clan;

public final class ClanTitle {
   private final int id;
   private final String name;

   public ClanTitle(int id, String name) {
      this.id = id;
      this.name = name;
   }

   public int getId() {
      return this.id;
   }

   public String getName() {
      return this.name;
   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof ClanTitle)) {
         return false;
      } else {
         ClanTitle other = (ClanTitle)o;
         if (this.getId() != other.getId()) {
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

            return true;
         }
      }
   }

   public int hashCode() {
      int PRIME = true;
      int result = 1;
      result = result * 59 + this.getId();
      Object $name = this.getName();
      result = result * 59 + ($name == null ? 43 : $name.hashCode());
      return result;
   }

   public String toString() {
      int var10000 = this.getId();
      return "ClanTitle(id=" + var10000 + ", name=" + this.getName() + ")";
   }
}
