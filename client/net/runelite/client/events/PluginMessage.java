package net.runelite.client.events;

import java.util.Collections;
import java.util.Map;
import lombok.NonNull;

public final class PluginMessage {
   private final String namespace;
   private final String name;
   private final Map<String, Object> data;

   public PluginMessage(@NonNull String namespace, @NonNull String name) {
      this(namespace, name, Collections.emptyMap());
      if (namespace == null) {
         throw new NullPointerException("namespace is marked non-null but is null");
      } else if (name == null) {
         throw new NullPointerException("name is marked non-null but is null");
      }
   }

   public PluginMessage(@NonNull String namespace, @NonNull String name, @NonNull Map<String, Object> data) {
      if (namespace == null) {
         throw new NullPointerException("namespace is marked non-null but is null");
      } else if (name == null) {
         throw new NullPointerException("name is marked non-null but is null");
      } else if (data == null) {
         throw new NullPointerException("data is marked non-null but is null");
      } else {
         this.namespace = namespace;
         this.name = name;
         this.data = data;
      }
   }

   public String getNamespace() {
      return this.namespace;
   }

   public String getName() {
      return this.name;
   }

   public Map<String, Object> getData() {
      return this.data;
   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof PluginMessage)) {
         return false;
      } else {
         PluginMessage other;
         label44: {
            other = (PluginMessage)o;
            Object this$namespace = this.getNamespace();
            Object other$namespace = other.getNamespace();
            if (this$namespace == null) {
               if (other$namespace == null) {
                  break label44;
               }
            } else if (this$namespace.equals(other$namespace)) {
               break label44;
            }

            return false;
         }

         Object this$name = this.getName();
         Object other$name = other.getName();
         if (this$name == null) {
            if (other$name != null) {
               return false;
            }
         } else if (!this$name.equals(other$name)) {
            return false;
         }

         Object this$data = this.getData();
         Object other$data = other.getData();
         if (this$data == null) {
            if (other$data != null) {
               return false;
            }
         } else if (!this$data.equals(other$data)) {
            return false;
         }

         return true;
      }
   }

   public int hashCode() {
      int PRIME = true;
      int result = 1;
      Object $namespace = this.getNamespace();
      result = result * 59 + ($namespace == null ? 43 : $namespace.hashCode());
      Object $name = this.getName();
      result = result * 59 + ($name == null ? 43 : $name.hashCode());
      Object $data = this.getData();
      result = result * 59 + ($data == null ? 43 : $data.hashCode());
      return result;
   }

   public String toString() {
      String var10000 = this.getNamespace();
      return "PluginMessage(namespace=" + var10000 + ", name=" + this.getName() + ", data=" + String.valueOf(this.getData()) + ")";
   }
}
