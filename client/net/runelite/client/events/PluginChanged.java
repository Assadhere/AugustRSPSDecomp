package net.runelite.client.events;

import net.runelite.client.plugins.Plugin;

public class PluginChanged {
   private final Plugin plugin;
   private final boolean loaded;

   public PluginChanged(Plugin plugin, boolean loaded) {
      this.plugin = plugin;
      this.loaded = loaded;
   }

   public Plugin getPlugin() {
      return this.plugin;
   }

   public boolean isLoaded() {
      return this.loaded;
   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof PluginChanged)) {
         return false;
      } else {
         PluginChanged other = (PluginChanged)o;
         if (!other.canEqual(this)) {
            return false;
         } else if (this.isLoaded() != other.isLoaded()) {
            return false;
         } else {
            Object this$plugin = this.getPlugin();
            Object other$plugin = other.getPlugin();
            if (this$plugin == null) {
               if (other$plugin != null) {
                  return false;
               }
            } else if (!this$plugin.equals(other$plugin)) {
               return false;
            }

            return true;
         }
      }
   }

   protected boolean canEqual(Object other) {
      return other instanceof PluginChanged;
   }

   public int hashCode() {
      int PRIME = true;
      int result = 1;
      result = result * 59 + (this.isLoaded() ? 79 : 97);
      Object $plugin = this.getPlugin();
      result = result * 59 + ($plugin == null ? 43 : $plugin.hashCode());
      return result;
   }

   public String toString() {
      String var10000 = String.valueOf(this.getPlugin());
      return "PluginChanged(plugin=" + var10000 + ", loaded=" + this.isLoaded() + ")";
   }
}
