package net.runelite.client.plugins.config;

import java.util.Arrays;
import java.util.List;
import javax.annotation.Nullable;
import javax.swing.JMenuItem;
import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigDescriptor;
import net.runelite.client.externalplugins.ExternalPluginManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.util.LinkBrowser;

final class PluginConfigurationDescriptor {
   private final String name;
   private final String description;
   private final String[] tags;
   @Nullable
   private final Plugin plugin;
   @Nullable
   private final Config config;
   @Nullable
   private final ConfigDescriptor configDescriptor;
   @Nullable
   private final List<String> conflicts;

   PluginConfigurationDescriptor(String name, String description, String[] tags, Config config, ConfigDescriptor configDescriptor) {
      this(name, description, tags, (Plugin)null, config, configDescriptor, (List)null);
   }

   @Nullable
   JMenuItem createSupportMenuItem() {
      String iname = this.getInternalPluginHubName();
      JMenuItem menuItem;
      if (iname != null) {
         menuItem = new JMenuItem("Support");
         menuItem.addActionListener((e) -> {
            LinkBrowser.browse("https://runelite.net/plugin-hub/show/" + iname);
         });
         return menuItem;
      } else {
         menuItem = new JMenuItem("Wiki");
         menuItem.addActionListener((e) -> {
            LinkBrowser.browse("https://github.com/runelite/runelite/wiki/" + this.name.replace(' ', '-'));
         });
         return menuItem;
      }
   }

   @Nullable
   String getInternalPluginHubName() {
      return this.plugin == null ? null : ExternalPluginManager.getInternalName(this.plugin.getClass());
   }

   public String getName() {
      return this.name;
   }

   public String getDescription() {
      return this.description;
   }

   public String[] getTags() {
      return this.tags;
   }

   @Nullable
   public Plugin getPlugin() {
      return this.plugin;
   }

   @Nullable
   public Config getConfig() {
      return this.config;
   }

   @Nullable
   public ConfigDescriptor getConfigDescriptor() {
      return this.configDescriptor;
   }

   @Nullable
   public List<String> getConflicts() {
      return this.conflicts;
   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof PluginConfigurationDescriptor)) {
         return false;
      } else {
         PluginConfigurationDescriptor other = (PluginConfigurationDescriptor)o;
         Object this$name = this.getName();
         Object other$name = other.getName();
         if (this$name == null) {
            if (other$name != null) {
               return false;
            }
         } else if (!this$name.equals(other$name)) {
            return false;
         }

         label77: {
            Object this$description = this.getDescription();
            Object other$description = other.getDescription();
            if (this$description == null) {
               if (other$description == null) {
                  break label77;
               }
            } else if (this$description.equals(other$description)) {
               break label77;
            }

            return false;
         }

         if (!Arrays.deepEquals(this.getTags(), other.getTags())) {
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

            label62: {
               Object this$config = this.getConfig();
               Object other$config = other.getConfig();
               if (this$config == null) {
                  if (other$config == null) {
                     break label62;
                  }
               } else if (this$config.equals(other$config)) {
                  break label62;
               }

               return false;
            }

            label55: {
               Object this$configDescriptor = this.getConfigDescriptor();
               Object other$configDescriptor = other.getConfigDescriptor();
               if (this$configDescriptor == null) {
                  if (other$configDescriptor == null) {
                     break label55;
                  }
               } else if (this$configDescriptor.equals(other$configDescriptor)) {
                  break label55;
               }

               return false;
            }

            Object this$conflicts = this.getConflicts();
            Object other$conflicts = other.getConflicts();
            if (this$conflicts == null) {
               if (other$conflicts != null) {
                  return false;
               }
            } else if (!this$conflicts.equals(other$conflicts)) {
               return false;
            }

            return true;
         }
      }
   }

   public int hashCode() {
      int PRIME = true;
      int result = 1;
      Object $name = this.getName();
      result = result * 59 + ($name == null ? 43 : $name.hashCode());
      Object $description = this.getDescription();
      result = result * 59 + ($description == null ? 43 : $description.hashCode());
      result = result * 59 + Arrays.deepHashCode(this.getTags());
      Object $plugin = this.getPlugin();
      result = result * 59 + ($plugin == null ? 43 : $plugin.hashCode());
      Object $config = this.getConfig();
      result = result * 59 + ($config == null ? 43 : $config.hashCode());
      Object $configDescriptor = this.getConfigDescriptor();
      result = result * 59 + ($configDescriptor == null ? 43 : $configDescriptor.hashCode());
      Object $conflicts = this.getConflicts();
      result = result * 59 + ($conflicts == null ? 43 : $conflicts.hashCode());
      return result;
   }

   public String toString() {
      String var10000 = this.getName();
      return "PluginConfigurationDescriptor(name=" + var10000 + ", description=" + this.getDescription() + ", tags=" + Arrays.deepToString(this.getTags()) + ", plugin=" + String.valueOf(this.getPlugin()) + ", config=" + String.valueOf(this.getConfig()) + ", configDescriptor=" + String.valueOf(this.getConfigDescriptor()) + ", conflicts=" + String.valueOf(this.getConflicts()) + ")";
   }

   public PluginConfigurationDescriptor(String name, String description, String[] tags, @Nullable Plugin plugin, @Nullable Config config, @Nullable ConfigDescriptor configDescriptor, @Nullable List<String> conflicts) {
      this.name = name;
      this.description = description;
      this.tags = tags;
      this.plugin = plugin;
      this.config = config;
      this.configDescriptor = configDescriptor;
      this.conflicts = conflicts;
   }
}
