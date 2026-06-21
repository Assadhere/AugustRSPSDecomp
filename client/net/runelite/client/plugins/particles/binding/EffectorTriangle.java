package net.runelite.client.plugins.particles.binding;

public class EffectorTriangle {
   private int effector;
   private int face;
   private boolean inheritDirection = true;

   public int getEffector() {
      return this.effector;
   }

   public int getFace() {
      return this.face;
   }

   public boolean isInheritDirection() {
      return this.inheritDirection;
   }

   public void setEffector(int effector) {
      this.effector = effector;
   }

   public void setFace(int face) {
      this.face = face;
   }

   public void setInheritDirection(boolean inheritDirection) {
      this.inheritDirection = inheritDirection;
   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof EffectorTriangle)) {
         return false;
      } else {
         EffectorTriangle other = (EffectorTriangle)o;
         if (!other.canEqual(this)) {
            return false;
         } else if (this.getEffector() != other.getEffector()) {
            return false;
         } else if (this.getFace() != other.getFace()) {
            return false;
         } else {
            return this.isInheritDirection() == other.isInheritDirection();
         }
      }
   }

   protected boolean canEqual(Object other) {
      return other instanceof EffectorTriangle;
   }

   public int hashCode() {
      int PRIME = true;
      int result = 1;
      result = result * 59 + this.getEffector();
      result = result * 59 + this.getFace();
      result = result * 59 + (this.isInheritDirection() ? 79 : 97);
      return result;
   }

   public String toString() {
      int var10000 = this.getEffector();
      return "EffectorTriangle(effector=" + var10000 + ", face=" + this.getFace() + ", inheritDirection=" + this.isInheritDirection() + ")";
   }

   public EffectorTriangle() {
   }

   public EffectorTriangle(int effector, int face, boolean inheritDirection) {
      this.effector = effector;
      this.face = face;
      this.inheritDirection = inheritDirection;
   }
}
