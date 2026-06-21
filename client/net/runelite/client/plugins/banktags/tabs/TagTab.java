package net.runelite.client.plugins.banktags.tabs;

public class TagTab {
   private String tag;
   private int iconItemId;

   TagTab(int iconItemId, String tag) {
      this.iconItemId = iconItemId;
      this.tag = tag;
   }

   public String getTag() {
      return this.tag;
   }

   public int getIconItemId() {
      return this.iconItemId;
   }

   public void setTag(String tag) {
      this.tag = tag;
   }

   public void setIconItemId(int iconItemId) {
      this.iconItemId = iconItemId;
   }

   public String toString() {
      String var10000 = this.getTag();
      return "TagTab(tag=" + var10000 + ", iconItemId=" + this.getIconItemId() + ")";
   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof TagTab)) {
         return false;
      } else {
         TagTab other = (TagTab)o;
         if (!other.canEqual(this)) {
            return false;
         } else {
            Object this$tag = this.getTag();
            Object other$tag = other.getTag();
            if (this$tag == null) {
               if (other$tag != null) {
                  return false;
               }
            } else if (!this$tag.equals(other$tag)) {
               return false;
            }

            return true;
         }
      }
   }

   protected boolean canEqual(Object other) {
      return other instanceof TagTab;
   }

   public int hashCode() {
      int PRIME = true;
      int result = 1;
      Object $tag = this.getTag();
      result = result * 59 + ($tag == null ? 43 : $tag.hashCode());
      return result;
   }

   public TagTab() {
   }
}
