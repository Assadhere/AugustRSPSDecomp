package net.runelite.api.events;

import net.runelite.api.HealthBarConfig;

public class PostHealthBarConfig {
   private HealthBarConfig healthBarConfig;

   public HealthBarConfig getHealthBarConfig() {
      return this.healthBarConfig;
   }

   public void setHealthBarConfig(HealthBarConfig healthBarConfig) {
      this.healthBarConfig = healthBarConfig;
   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof PostHealthBarConfig)) {
         return false;
      } else {
         PostHealthBarConfig other = (PostHealthBarConfig)o;
         if (!other.canEqual(this)) {
            return false;
         } else {
            Object this$healthBarConfig = this.getHealthBarConfig();
            Object other$healthBarConfig = other.getHealthBarConfig();
            if (this$healthBarConfig == null) {
               if (other$healthBarConfig != null) {
                  return false;
               }
            } else if (!this$healthBarConfig.equals(other$healthBarConfig)) {
               return false;
            }

            return true;
         }
      }
   }

   protected boolean canEqual(Object other) {
      return other instanceof PostHealthBarConfig;
   }

   public int hashCode() {
      int PRIME = true;
      int result = 1;
      Object $healthBarConfig = this.getHealthBarConfig();
      result = result * 59 + ($healthBarConfig == null ? 43 : $healthBarConfig.hashCode());
      return result;
   }

   public String toString() {
      return "PostHealthBarConfig(healthBarConfig=" + String.valueOf(this.getHealthBarConfig()) + ")";
   }
}
