package net.runelite.client.chat;

import java.awt.Color;

class ChatColor {
   private ChatColorType type;
   private Color color;
   private boolean transparent;
   private boolean isDefault;
   private int setting;

   public ChatColor(ChatColorType type, Color color, boolean transparent) {
      this(type, color, transparent, false, -1);
   }

   public ChatColorType getType() {
      return this.type;
   }

   public Color getColor() {
      return this.color;
   }

   public boolean isTransparent() {
      return this.transparent;
   }

   public boolean isDefault() {
      return this.isDefault;
   }

   public int getSetting() {
      return this.setting;
   }

   public void setType(ChatColorType type) {
      this.type = type;
   }

   public void setColor(Color color) {
      this.color = color;
   }

   public void setTransparent(boolean transparent) {
      this.transparent = transparent;
   }

   public void setDefault(boolean isDefault) {
      this.isDefault = isDefault;
   }

   public void setSetting(int setting) {
      this.setting = setting;
   }

   public String toString() {
      String var10000 = String.valueOf(this.getType());
      return "ChatColor(type=" + var10000 + ", color=" + String.valueOf(this.getColor()) + ", transparent=" + this.isTransparent() + ", isDefault=" + this.isDefault() + ", setting=" + this.getSetting() + ")";
   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof ChatColor)) {
         return false;
      } else {
         ChatColor other = (ChatColor)o;
         if (!other.canEqual(this)) {
            return false;
         } else if (this.isTransparent() != other.isTransparent()) {
            return false;
         } else {
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
   }

   protected boolean canEqual(Object other) {
      return other instanceof ChatColor;
   }

   public int hashCode() {
      int PRIME = true;
      int result = 1;
      result = result * 59 + (this.isTransparent() ? 79 : 97);
      Object $type = this.getType();
      result = result * 59 + ($type == null ? 43 : $type.hashCode());
      return result;
   }

   public ChatColor(ChatColorType type, Color color, boolean transparent, boolean isDefault, int setting) {
      this.type = type;
      this.color = color;
      this.transparent = transparent;
      this.isDefault = isDefault;
      this.setting = setting;
   }
}
