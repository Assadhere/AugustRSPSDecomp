package net.runelite.client.plugins.particles.binding;

import java.util.List;

public class ObjectEffectorBinding {
   private int objectId;
   private List<EffectorTriangle> faceEffectors;

   public int getObjectId() {
      return this.objectId;
   }

   public List<EffectorTriangle> getFaceEffectors() {
      return this.faceEffectors;
   }

   public void setObjectId(int objectId) {
      this.objectId = objectId;
   }

   public void setFaceEffectors(List<EffectorTriangle> faceEffectors) {
      this.faceEffectors = faceEffectors;
   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof ObjectEffectorBinding)) {
         return false;
      } else {
         ObjectEffectorBinding other = (ObjectEffectorBinding)o;
         if (!other.canEqual(this)) {
            return false;
         } else if (this.getObjectId() != other.getObjectId()) {
            return false;
         } else {
            Object this$faceEffectors = this.getFaceEffectors();
            Object other$faceEffectors = other.getFaceEffectors();
            if (this$faceEffectors == null) {
               if (other$faceEffectors != null) {
                  return false;
               }
            } else if (!this$faceEffectors.equals(other$faceEffectors)) {
               return false;
            }

            return true;
         }
      }
   }

   protected boolean canEqual(Object other) {
      return other instanceof ObjectEffectorBinding;
   }

   public int hashCode() {
      int PRIME = true;
      int result = 1;
      result = result * 59 + this.getObjectId();
      Object $faceEffectors = this.getFaceEffectors();
      result = result * 59 + ($faceEffectors == null ? 43 : $faceEffectors.hashCode());
      return result;
   }

   public String toString() {
      int var10000 = this.getObjectId();
      return "ObjectEffectorBinding(objectId=" + var10000 + ", faceEffectors=" + String.valueOf(this.getFaceEffectors()) + ")";
   }

   public ObjectEffectorBinding() {
   }

   public ObjectEffectorBinding(int objectId, List<EffectorTriangle> faceEffectors) {
      this.objectId = objectId;
      this.faceEffectors = faceEffectors;
   }
}
