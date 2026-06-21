package net.runelite.client.events;

import javax.annotation.Nullable;

public class ConfigChanged {
   private String group;
   @Nullable
   private String profile;
   private String key;
   private String oldValue;
   @Nullable
   private String newValue;

   public String getGroup() {
      return this.group;
   }

   @Nullable
   public String getProfile() {
      return this.profile;
   }

   public String getKey() {
      return this.key;
   }

   public String getOldValue() {
      return this.oldValue;
   }

   @Nullable
   public String getNewValue() {
      return this.newValue;
   }

   public void setGroup(String group) {
      this.group = group;
   }

   public void setProfile(@Nullable String profile) {
      this.profile = profile;
   }

   public void setKey(String key) {
      this.key = key;
   }

   public void setOldValue(String oldValue) {
      this.oldValue = oldValue;
   }

   public void setNewValue(@Nullable String newValue) {
      this.newValue = newValue;
   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof ConfigChanged)) {
         return false;
      } else {
         ConfigChanged other = (ConfigChanged)o;
         if (!other.canEqual(this)) {
            return false;
         } else {
            label71: {
               Object this$group = this.getGroup();
               Object other$group = other.getGroup();
               if (this$group == null) {
                  if (other$group == null) {
                     break label71;
                  }
               } else if (this$group.equals(other$group)) {
                  break label71;
               }

               return false;
            }

            Object this$profile = this.getProfile();
            Object other$profile = other.getProfile();
            if (this$profile == null) {
               if (other$profile != null) {
                  return false;
               }
            } else if (!this$profile.equals(other$profile)) {
               return false;
            }

            label57: {
               Object this$key = this.getKey();
               Object other$key = other.getKey();
               if (this$key == null) {
                  if (other$key == null) {
                     break label57;
                  }
               } else if (this$key.equals(other$key)) {
                  break label57;
               }

               return false;
            }

            Object this$oldValue = this.getOldValue();
            Object other$oldValue = other.getOldValue();
            if (this$oldValue == null) {
               if (other$oldValue != null) {
                  return false;
               }
            } else if (!this$oldValue.equals(other$oldValue)) {
               return false;
            }

            Object this$newValue = this.getNewValue();
            Object other$newValue = other.getNewValue();
            if (this$newValue == null) {
               if (other$newValue == null) {
                  return true;
               }
            } else if (this$newValue.equals(other$newValue)) {
               return true;
            }

            return false;
         }
      }
   }

   protected boolean canEqual(Object other) {
      return other instanceof ConfigChanged;
   }

   public int hashCode() {
      int PRIME = true;
      int result = 1;
      Object $group = this.getGroup();
      result = result * 59 + ($group == null ? 43 : $group.hashCode());
      Object $profile = this.getProfile();
      result = result * 59 + ($profile == null ? 43 : $profile.hashCode());
      Object $key = this.getKey();
      result = result * 59 + ($key == null ? 43 : $key.hashCode());
      Object $oldValue = this.getOldValue();
      result = result * 59 + ($oldValue == null ? 43 : $oldValue.hashCode());
      Object $newValue = this.getNewValue();
      result = result * 59 + ($newValue == null ? 43 : $newValue.hashCode());
      return result;
   }

   public String toString() {
      String var10000 = this.getGroup();
      return "ConfigChanged(group=" + var10000 + ", profile=" + this.getProfile() + ", key=" + this.getKey() + ", oldValue=" + this.getOldValue() + ", newValue=" + this.getNewValue() + ")";
   }
}
