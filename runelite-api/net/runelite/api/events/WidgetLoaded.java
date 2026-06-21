package net.runelite.api.events;

public class WidgetLoaded {
   private int groupId;

   public void setGroupId(int groupId) {
      this.groupId = groupId;
   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof WidgetLoaded)) {
         return false;
      } else {
         WidgetLoaded other = (WidgetLoaded)o;
         if (!other.canEqual(this)) {
            return false;
         } else {
            return this.getGroupId() == other.getGroupId();
         }
      }
   }

   protected boolean canEqual(Object other) {
      return other instanceof WidgetLoaded;
   }

   public int hashCode() {
      int PRIME = true;
      int result = 1;
      result = result * 59 + this.getGroupId();
      return result;
   }

   public String toString() {
      return "WidgetLoaded(groupId=" + this.getGroupId() + ")";
   }

   public int getGroupId() {
      return this.groupId;
   }
}
