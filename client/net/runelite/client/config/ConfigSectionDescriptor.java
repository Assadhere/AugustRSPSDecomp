package net.runelite.client.config;

public final class ConfigSectionDescriptor implements ConfigObject {
   private final String key;
   private final ConfigSection section;

   public String key() {
      return this.key;
   }

   public String name() {
      return this.section.name();
   }

   public int position() {
      return this.section.position();
   }

   public ConfigSectionDescriptor(String key, ConfigSection section) {
      this.key = key;
      this.section = section;
   }

   public String getKey() {
      return this.key;
   }

   public ConfigSection getSection() {
      return this.section;
   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof ConfigSectionDescriptor)) {
         return false;
      } else {
         ConfigSectionDescriptor other = (ConfigSectionDescriptor)o;
         Object this$key = this.getKey();
         Object other$key = other.getKey();
         if (this$key == null) {
            if (other$key != null) {
               return false;
            }
         } else if (!this$key.equals(other$key)) {
            return false;
         }

         Object this$section = this.getSection();
         Object other$section = other.getSection();
         if (this$section == null) {
            if (other$section != null) {
               return false;
            }
         } else if (!this$section.equals(other$section)) {
            return false;
         }

         return true;
      }
   }

   public int hashCode() {
      int PRIME = true;
      int result = 1;
      Object $key = this.getKey();
      result = result * 59 + ($key == null ? 43 : $key.hashCode());
      Object $section = this.getSection();
      result = result * 59 + ($section == null ? 43 : $section.hashCode());
      return result;
   }

   public String toString() {
      String var10000 = this.getKey();
      return "ConfigSectionDescriptor(key=" + var10000 + ", section=" + String.valueOf(this.getSection()) + ")";
   }
}
