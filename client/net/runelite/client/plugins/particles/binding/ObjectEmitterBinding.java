package net.runelite.client.plugins.particles.binding;

import java.util.List;

public class ObjectEmitterBinding {
   private int objectId;
   private List<EmissiveTriangle> emitters;

   public int getObjectId() {
      return this.objectId;
   }

   public List<EmissiveTriangle> getEmitters() {
      return this.emitters;
   }

   public void setObjectId(int objectId) {
      this.objectId = objectId;
   }

   public void setEmitters(List<EmissiveTriangle> emitters) {
      this.emitters = emitters;
   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof ObjectEmitterBinding)) {
         return false;
      } else {
         ObjectEmitterBinding other = (ObjectEmitterBinding)o;
         if (!other.canEqual(this)) {
            return false;
         } else if (this.getObjectId() != other.getObjectId()) {
            return false;
         } else {
            Object this$emitters = this.getEmitters();
            Object other$emitters = other.getEmitters();
            if (this$emitters == null) {
               if (other$emitters != null) {
                  return false;
               }
            } else if (!this$emitters.equals(other$emitters)) {
               return false;
            }

            return true;
         }
      }
   }

   protected boolean canEqual(Object other) {
      return other instanceof ObjectEmitterBinding;
   }

   public int hashCode() {
      int PRIME = true;
      int result = 1;
      result = result * 59 + this.getObjectId();
      Object $emitters = this.getEmitters();
      result = result * 59 + ($emitters == null ? 43 : $emitters.hashCode());
      return result;
   }

   public String toString() {
      int var10000 = this.getObjectId();
      return "ObjectEmitterBinding(objectId=" + var10000 + ", emitters=" + String.valueOf(this.getEmitters()) + ")";
   }

   public ObjectEmitterBinding() {
   }

   public ObjectEmitterBinding(int objectId, List<EmissiveTriangle> emitters) {
      this.objectId = objectId;
      this.emitters = emitters;
   }
}
