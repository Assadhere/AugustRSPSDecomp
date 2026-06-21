package net.runelite.client.plugins.objectindicators;

import com.google.gson.annotations.SerializedName;
import java.awt.Color;
import javax.annotation.Nullable;

class ObjectPoint {
   private int id = -1;
   private String name;
   private int regionId;
   private int regionX;
   private int regionY;
   private int z;
   @Nullable
   @SerializedName("color")
   private Color borderColor;
   @Nullable
   private Color fillColor;
   private Boolean hull;
   private Boolean outline;
   private Boolean clickbox;
   private Boolean tile;

   public int getId() {
      return this.id;
   }

   public String getName() {
      return this.name;
   }

   public int getRegionId() {
      return this.regionId;
   }

   public int getRegionX() {
      return this.regionX;
   }

   public int getRegionY() {
      return this.regionY;
   }

   public int getZ() {
      return this.z;
   }

   @Nullable
   public Color getBorderColor() {
      return this.borderColor;
   }

   @Nullable
   public Color getFillColor() {
      return this.fillColor;
   }

   public Boolean getHull() {
      return this.hull;
   }

   public Boolean getOutline() {
      return this.outline;
   }

   public Boolean getClickbox() {
      return this.clickbox;
   }

   public Boolean getTile() {
      return this.tile;
   }

   public void setId(int id) {
      this.id = id;
   }

   public void setName(String name) {
      this.name = name;
   }

   public void setRegionId(int regionId) {
      this.regionId = regionId;
   }

   public void setRegionX(int regionX) {
      this.regionX = regionX;
   }

   public void setRegionY(int regionY) {
      this.regionY = regionY;
   }

   public void setZ(int z) {
      this.z = z;
   }

   public void setBorderColor(@Nullable Color borderColor) {
      this.borderColor = borderColor;
   }

   public void setFillColor(@Nullable Color fillColor) {
      this.fillColor = fillColor;
   }

   public void setHull(Boolean hull) {
      this.hull = hull;
   }

   public void setOutline(Boolean outline) {
      this.outline = outline;
   }

   public void setClickbox(Boolean clickbox) {
      this.clickbox = clickbox;
   }

   public void setTile(Boolean tile) {
      this.tile = tile;
   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof ObjectPoint)) {
         return false;
      } else {
         ObjectPoint other = (ObjectPoint)o;
         if (!other.canEqual(this)) {
            return false;
         } else if (this.getId() != other.getId()) {
            return false;
         } else if (this.getRegionId() != other.getRegionId()) {
            return false;
         } else if (this.getRegionX() != other.getRegionX()) {
            return false;
         } else if (this.getRegionY() != other.getRegionY()) {
            return false;
         } else if (this.getZ() != other.getZ()) {
            return false;
         } else {
            Object this$hull = this.getHull();
            Object other$hull = other.getHull();
            if (this$hull == null) {
               if (other$hull != null) {
                  return false;
               }
            } else if (!this$hull.equals(other$hull)) {
               return false;
            }

            Object this$outline = this.getOutline();
            Object other$outline = other.getOutline();
            if (this$outline == null) {
               if (other$outline != null) {
                  return false;
               }
            } else if (!this$outline.equals(other$outline)) {
               return false;
            }

            label93: {
               Object this$clickbox = this.getClickbox();
               Object other$clickbox = other.getClickbox();
               if (this$clickbox == null) {
                  if (other$clickbox == null) {
                     break label93;
                  }
               } else if (this$clickbox.equals(other$clickbox)) {
                  break label93;
               }

               return false;
            }

            label86: {
               Object this$tile = this.getTile();
               Object other$tile = other.getTile();
               if (this$tile == null) {
                  if (other$tile == null) {
                     break label86;
                  }
               } else if (this$tile.equals(other$tile)) {
                  break label86;
               }

               return false;
            }

            label79: {
               Object this$name = this.getName();
               Object other$name = other.getName();
               if (this$name == null) {
                  if (other$name == null) {
                     break label79;
                  }
               } else if (this$name.equals(other$name)) {
                  break label79;
               }

               return false;
            }

            label72: {
               Object this$borderColor = this.getBorderColor();
               Object other$borderColor = other.getBorderColor();
               if (this$borderColor == null) {
                  if (other$borderColor == null) {
                     break label72;
                  }
               } else if (this$borderColor.equals(other$borderColor)) {
                  break label72;
               }

               return false;
            }

            Object this$fillColor = this.getFillColor();
            Object other$fillColor = other.getFillColor();
            if (this$fillColor == null) {
               if (other$fillColor != null) {
                  return false;
               }
            } else if (!this$fillColor.equals(other$fillColor)) {
               return false;
            }

            return true;
         }
      }
   }

   protected boolean canEqual(Object other) {
      return other instanceof ObjectPoint;
   }

   public int hashCode() {
      int PRIME = true;
      int result = 1;
      result = result * 59 + this.getId();
      result = result * 59 + this.getRegionId();
      result = result * 59 + this.getRegionX();
      result = result * 59 + this.getRegionY();
      result = result * 59 + this.getZ();
      Object $hull = this.getHull();
      result = result * 59 + ($hull == null ? 43 : $hull.hashCode());
      Object $outline = this.getOutline();
      result = result * 59 + ($outline == null ? 43 : $outline.hashCode());
      Object $clickbox = this.getClickbox();
      result = result * 59 + ($clickbox == null ? 43 : $clickbox.hashCode());
      Object $tile = this.getTile();
      result = result * 59 + ($tile == null ? 43 : $tile.hashCode());
      Object $name = this.getName();
      result = result * 59 + ($name == null ? 43 : $name.hashCode());
      Object $borderColor = this.getBorderColor();
      result = result * 59 + ($borderColor == null ? 43 : $borderColor.hashCode());
      Object $fillColor = this.getFillColor();
      result = result * 59 + ($fillColor == null ? 43 : $fillColor.hashCode());
      return result;
   }

   public String toString() {
      int var10000 = this.getId();
      return "ObjectPoint(id=" + var10000 + ", name=" + this.getName() + ", regionId=" + this.getRegionId() + ", regionX=" + this.getRegionX() + ", regionY=" + this.getRegionY() + ", z=" + this.getZ() + ", borderColor=" + String.valueOf(this.getBorderColor()) + ", fillColor=" + String.valueOf(this.getFillColor()) + ", hull=" + this.getHull() + ", outline=" + this.getOutline() + ", clickbox=" + this.getClickbox() + ", tile=" + this.getTile() + ")";
   }

   public ObjectPoint() {
   }

   public ObjectPoint(int id, String name, int regionId, int regionX, int regionY, int z, @Nullable Color borderColor, @Nullable Color fillColor, Boolean hull, Boolean outline, Boolean clickbox, Boolean tile) {
      this.id = id;
      this.name = name;
      this.regionId = regionId;
      this.regionX = regionX;
      this.regionY = regionY;
      this.z = z;
      this.borderColor = borderColor;
      this.fillColor = fillColor;
      this.hull = hull;
      this.outline = outline;
      this.clickbox = clickbox;
      this.tile = tile;
   }
}
