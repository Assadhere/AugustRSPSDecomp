package net.runelite.api;

public final class Point {
   private final int x;
   private final int y;

   public int distanceTo(Point other) {
      return (int)Math.hypot((double)(this.getX() - other.getX()), (double)(this.getY() - other.getY()));
   }

   public Point(int x, int y) {
      this.x = x;
      this.y = y;
   }

   public int getX() {
      return this.x;
   }

   public int getY() {
      return this.y;
   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof Point)) {
         return false;
      } else {
         Point other = (Point)o;
         if (this.getX() != other.getX()) {
            return false;
         } else {
            return this.getY() == other.getY();
         }
      }
   }

   public int hashCode() {
      int PRIME = true;
      int result = 1;
      result = result * 59 + this.getX();
      result = result * 59 + this.getY();
      return result;
   }

   public String toString() {
      int var10000 = this.getX();
      return "Point(x=" + var10000 + ", y=" + this.getY() + ")";
   }
}
