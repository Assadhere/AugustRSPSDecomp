package net.runelite.client.plugins.objectindicators;

import java.awt.Color;
import javax.annotation.Nullable;
import net.runelite.api.ObjectComposition;
import net.runelite.api.TileObject;

final class ColorTileObject {
   static final int HF_HULL = 1;
   static final int HF_OUTLINE = 2;
   static final int HF_CLICKBOX = 4;
   static final int HF_TILE = 8;
   private final TileObject tileObject;
   private final ObjectComposition composition;
   private final String name;
   @Nullable
   private final Color borderColor;
   @Nullable
   private final Color fillColor;
   private final byte highlightFlags;

   public TileObject getTileObject() {
      return this.tileObject;
   }

   public ObjectComposition getComposition() {
      return this.composition;
   }

   public String getName() {
      return this.name;
   }

   @Nullable
   public Color getBorderColor() {
      return this.borderColor;
   }

   @Nullable
   public Color getFillColor() {
      return this.fillColor;
   }

   public byte getHighlightFlags() {
      return this.highlightFlags;
   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof ColorTileObject)) {
         return false;
      } else {
         ColorTileObject other = (ColorTileObject)o;
         if (this.getHighlightFlags() != other.getHighlightFlags()) {
            return false;
         } else {
            label71: {
               Object this$tileObject = this.getTileObject();
               Object other$tileObject = other.getTileObject();
               if (this$tileObject == null) {
                  if (other$tileObject == null) {
                     break label71;
                  }
               } else if (this$tileObject.equals(other$tileObject)) {
                  break label71;
               }

               return false;
            }

            Object this$composition = this.getComposition();
            Object other$composition = other.getComposition();
            if (this$composition == null) {
               if (other$composition != null) {
                  return false;
               }
            } else if (!this$composition.equals(other$composition)) {
               return false;
            }

            label57: {
               Object this$name = this.getName();
               Object other$name = other.getName();
               if (this$name == null) {
                  if (other$name == null) {
                     break label57;
                  }
               } else if (this$name.equals(other$name)) {
                  break label57;
               }

               return false;
            }

            Object this$borderColor = this.getBorderColor();
            Object other$borderColor = other.getBorderColor();
            if (this$borderColor == null) {
               if (other$borderColor != null) {
                  return false;
               }
            } else if (!this$borderColor.equals(other$borderColor)) {
               return false;
            }

            Object this$fillColor = this.getFillColor();
            Object other$fillColor = other.getFillColor();
            if (this$fillColor == null) {
               if (other$fillColor == null) {
                  return true;
               }
            } else if (this$fillColor.equals(other$fillColor)) {
               return true;
            }

            return false;
         }
      }
   }

   public int hashCode() {
      int PRIME = true;
      int result = 1;
      result = result * 59 + this.getHighlightFlags();
      Object $tileObject = this.getTileObject();
      result = result * 59 + ($tileObject == null ? 43 : $tileObject.hashCode());
      Object $composition = this.getComposition();
      result = result * 59 + ($composition == null ? 43 : $composition.hashCode());
      Object $name = this.getName();
      result = result * 59 + ($name == null ? 43 : $name.hashCode());
      Object $borderColor = this.getBorderColor();
      result = result * 59 + ($borderColor == null ? 43 : $borderColor.hashCode());
      Object $fillColor = this.getFillColor();
      result = result * 59 + ($fillColor == null ? 43 : $fillColor.hashCode());
      return result;
   }

   public String toString() {
      String var10000 = String.valueOf(this.getTileObject());
      return "ColorTileObject(tileObject=" + var10000 + ", composition=" + String.valueOf(this.getComposition()) + ", name=" + this.getName() + ", borderColor=" + String.valueOf(this.getBorderColor()) + ", fillColor=" + String.valueOf(this.getFillColor()) + ", highlightFlags=" + this.getHighlightFlags() + ")";
   }

   public ColorTileObject(TileObject tileObject, ObjectComposition composition, String name, @Nullable Color borderColor, @Nullable Color fillColor, byte highlightFlags) {
      this.tileObject = tileObject;
      this.composition = composition;
      this.name = name;
      this.borderColor = borderColor;
      this.fillColor = fillColor;
      this.highlightFlags = highlightFlags;
   }
}
