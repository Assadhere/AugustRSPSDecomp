package net.runelite.api.events;

import net.runelite.api.Projectile;
import net.runelite.api.coords.LocalPoint;

public class ProjectileMoved {
   private Projectile projectile;
   private LocalPoint position;
   private int z;

   public Projectile getProjectile() {
      return this.projectile;
   }

   public LocalPoint getPosition() {
      return this.position;
   }

   public int getZ() {
      return this.z;
   }

   public void setProjectile(Projectile projectile) {
      this.projectile = projectile;
   }

   public void setPosition(LocalPoint position) {
      this.position = position;
   }

   public void setZ(int z) {
      this.z = z;
   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof ProjectileMoved)) {
         return false;
      } else {
         ProjectileMoved other = (ProjectileMoved)o;
         if (!other.canEqual(this)) {
            return false;
         } else if (this.getZ() != other.getZ()) {
            return false;
         } else {
            Object this$projectile = this.getProjectile();
            Object other$projectile = other.getProjectile();
            if (this$projectile == null) {
               if (other$projectile != null) {
                  return false;
               }
            } else if (!this$projectile.equals(other$projectile)) {
               return false;
            }

            Object this$position = this.getPosition();
            Object other$position = other.getPosition();
            if (this$position == null) {
               if (other$position != null) {
                  return false;
               }
            } else if (!this$position.equals(other$position)) {
               return false;
            }

            return true;
         }
      }
   }

   protected boolean canEqual(Object other) {
      return other instanceof ProjectileMoved;
   }

   public int hashCode() {
      int PRIME = true;
      int result = 1;
      result = result * 59 + this.getZ();
      Object $projectile = this.getProjectile();
      result = result * 59 + ($projectile == null ? 43 : $projectile.hashCode());
      Object $position = this.getPosition();
      result = result * 59 + ($position == null ? 43 : $position.hashCode());
      return result;
   }

   public String toString() {
      String var10000 = String.valueOf(this.getProjectile());
      return "ProjectileMoved(projectile=" + var10000 + ", position=" + String.valueOf(this.getPosition()) + ", z=" + this.getZ() + ")";
   }
}
