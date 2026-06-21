package net.runelite.client.plugins.particles.binding;

public class EmissiveTriangle {
   private int emitter;
   private int face;
   private int priority;

   public int getEmitter() {
      return this.emitter;
   }

   public int getFace() {
      return this.face;
   }

   public int getPriority() {
      return this.priority;
   }

   public void setEmitter(int emitter) {
      this.emitter = emitter;
   }

   public void setFace(int face) {
      this.face = face;
   }

   public void setPriority(int priority) {
      this.priority = priority;
   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof EmissiveTriangle)) {
         return false;
      } else {
         EmissiveTriangle other = (EmissiveTriangle)o;
         if (!other.canEqual(this)) {
            return false;
         } else if (this.getEmitter() != other.getEmitter()) {
            return false;
         } else if (this.getFace() != other.getFace()) {
            return false;
         } else {
            return this.getPriority() == other.getPriority();
         }
      }
   }

   protected boolean canEqual(Object other) {
      return other instanceof EmissiveTriangle;
   }

   public int hashCode() {
      int PRIME = true;
      int result = 1;
      result = result * 59 + this.getEmitter();
      result = result * 59 + this.getFace();
      result = result * 59 + this.getPriority();
      return result;
   }

   public String toString() {
      int var10000 = this.getEmitter();
      return "EmissiveTriangle(emitter=" + var10000 + ", face=" + this.getFace() + ", priority=" + this.getPriority() + ")";
   }

   public EmissiveTriangle() {
   }

   public EmissiveTriangle(int emitter, int face, int priority) {
      this.emitter = emitter;
      this.face = face;
      this.priority = priority;
   }
}
