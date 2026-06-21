package net.runelite.client.game.npcoverlay;

import java.awt.Color;
import java.util.function.Predicate;
import lombok.NonNull;
import net.runelite.api.NPC;

public final class HighlightedNpc {
   private final @NonNull NPC npc;
   private final @NonNull Color highlightColor;
   private final Color fillColor;
   private final boolean hull;
   private final boolean tile;
   private final boolean trueTile;
   private final boolean swTile;
   private final boolean swTrueTile;
   private final boolean outline;
   private final boolean name;
   private final boolean nameOnMinimap;
   private final float borderWidth;
   private final int outlineFeather;
   private final Predicate<NPC> render;

   private static Color $default$fillColor() {
      return new Color(0, 0, 0, 50);
   }

   private static float $default$borderWidth() {
      return 2.0F;
   }

   HighlightedNpc(@NonNull NPC npc, @NonNull Color highlightColor, Color fillColor, boolean hull, boolean tile, boolean trueTile, boolean swTile, boolean swTrueTile, boolean outline, boolean name, boolean nameOnMinimap, float borderWidth, int outlineFeather, Predicate<NPC> render) {
      if (npc == null) {
         throw new NullPointerException("npc is marked non-null but is null");
      } else if (highlightColor == null) {
         throw new NullPointerException("highlightColor is marked non-null but is null");
      } else {
         this.npc = npc;
         this.highlightColor = highlightColor;
         this.fillColor = fillColor;
         this.hull = hull;
         this.tile = tile;
         this.trueTile = trueTile;
         this.swTile = swTile;
         this.swTrueTile = swTrueTile;
         this.outline = outline;
         this.name = name;
         this.nameOnMinimap = nameOnMinimap;
         this.borderWidth = borderWidth;
         this.outlineFeather = outlineFeather;
         this.render = render;
      }
   }

   public static HighlightedNpcBuilder builder() {
      return new HighlightedNpcBuilder();
   }

   public @NonNull NPC getNpc() {
      return this.npc;
   }

   public @NonNull Color getHighlightColor() {
      return this.highlightColor;
   }

   public Color getFillColor() {
      return this.fillColor;
   }

   public boolean isHull() {
      return this.hull;
   }

   public boolean isTile() {
      return this.tile;
   }

   public boolean isTrueTile() {
      return this.trueTile;
   }

   public boolean isSwTile() {
      return this.swTile;
   }

   public boolean isSwTrueTile() {
      return this.swTrueTile;
   }

   public boolean isOutline() {
      return this.outline;
   }

   public boolean isName() {
      return this.name;
   }

   public boolean isNameOnMinimap() {
      return this.nameOnMinimap;
   }

   public float getBorderWidth() {
      return this.borderWidth;
   }

   public int getOutlineFeather() {
      return this.outlineFeather;
   }

   public Predicate<NPC> getRender() {
      return this.render;
   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof HighlightedNpc)) {
         return false;
      } else {
         HighlightedNpc other = (HighlightedNpc)o;
         if (this.isHull() != other.isHull()) {
            return false;
         } else if (this.isTile() != other.isTile()) {
            return false;
         } else if (this.isTrueTile() != other.isTrueTile()) {
            return false;
         } else if (this.isSwTile() != other.isSwTile()) {
            return false;
         } else if (this.isSwTrueTile() != other.isSwTrueTile()) {
            return false;
         } else if (this.isOutline() != other.isOutline()) {
            return false;
         } else if (this.isName() != other.isName()) {
            return false;
         } else if (this.isNameOnMinimap() != other.isNameOnMinimap()) {
            return false;
         } else if (Float.compare(this.getBorderWidth(), other.getBorderWidth()) != 0) {
            return false;
         } else if (this.getOutlineFeather() != other.getOutlineFeather()) {
            return false;
         } else {
            label81: {
               Object this$npc = this.getNpc();
               Object other$npc = other.getNpc();
               if (this$npc == null) {
                  if (other$npc == null) {
                     break label81;
                  }
               } else if (this$npc.equals(other$npc)) {
                  break label81;
               }

               return false;
            }

            Object this$highlightColor = this.getHighlightColor();
            Object other$highlightColor = other.getHighlightColor();
            if (this$highlightColor == null) {
               if (other$highlightColor != null) {
                  return false;
               }
            } else if (!this$highlightColor.equals(other$highlightColor)) {
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

            Object this$render = this.getRender();
            Object other$render = other.getRender();
            if (this$render == null) {
               if (other$render != null) {
                  return false;
               }
            } else if (!this$render.equals(other$render)) {
               return false;
            }

            return true;
         }
      }
   }

   public int hashCode() {
      int PRIME = true;
      int result = 1;
      result = result * 59 + (this.isHull() ? 79 : 97);
      result = result * 59 + (this.isTile() ? 79 : 97);
      result = result * 59 + (this.isTrueTile() ? 79 : 97);
      result = result * 59 + (this.isSwTile() ? 79 : 97);
      result = result * 59 + (this.isSwTrueTile() ? 79 : 97);
      result = result * 59 + (this.isOutline() ? 79 : 97);
      result = result * 59 + (this.isName() ? 79 : 97);
      result = result * 59 + (this.isNameOnMinimap() ? 79 : 97);
      result = result * 59 + Float.floatToIntBits(this.getBorderWidth());
      result = result * 59 + this.getOutlineFeather();
      Object $npc = this.getNpc();
      result = result * 59 + ($npc == null ? 43 : $npc.hashCode());
      Object $highlightColor = this.getHighlightColor();
      result = result * 59 + ($highlightColor == null ? 43 : $highlightColor.hashCode());
      Object $fillColor = this.getFillColor();
      result = result * 59 + ($fillColor == null ? 43 : $fillColor.hashCode());
      Object $render = this.getRender();
      result = result * 59 + ($render == null ? 43 : $render.hashCode());
      return result;
   }

   public String toString() {
      String var10000 = String.valueOf(this.getNpc());
      return "HighlightedNpc(npc=" + var10000 + ", highlightColor=" + String.valueOf(this.getHighlightColor()) + ", fillColor=" + String.valueOf(this.getFillColor()) + ", hull=" + this.isHull() + ", tile=" + this.isTile() + ", trueTile=" + this.isTrueTile() + ", swTile=" + this.isSwTile() + ", swTrueTile=" + this.isSwTrueTile() + ", outline=" + this.isOutline() + ", name=" + this.isName() + ", nameOnMinimap=" + this.isNameOnMinimap() + ", borderWidth=" + this.getBorderWidth() + ", outlineFeather=" + this.getOutlineFeather() + ", render=" + String.valueOf(this.getRender()) + ")";
   }

   public static class HighlightedNpcBuilder {
      private NPC npc;
      private Color highlightColor;
      private boolean fillColor$set;
      private Color fillColor$value;
      private boolean hull;
      private boolean tile;
      private boolean trueTile;
      private boolean swTile;
      private boolean swTrueTile;
      private boolean outline;
      private boolean name;
      private boolean nameOnMinimap;
      private boolean borderWidth$set;
      private float borderWidth$value;
      private int outlineFeather;
      private Predicate<NPC> render;

      HighlightedNpcBuilder() {
      }

      public HighlightedNpcBuilder npc(@NonNull NPC npc) {
         if (npc == null) {
            throw new NullPointerException("npc is marked non-null but is null");
         } else {
            this.npc = npc;
            return this;
         }
      }

      public HighlightedNpcBuilder highlightColor(@NonNull Color highlightColor) {
         if (highlightColor == null) {
            throw new NullPointerException("highlightColor is marked non-null but is null");
         } else {
            this.highlightColor = highlightColor;
            return this;
         }
      }

      public HighlightedNpcBuilder fillColor(Color fillColor) {
         this.fillColor$value = fillColor;
         this.fillColor$set = true;
         return this;
      }

      public HighlightedNpcBuilder hull(boolean hull) {
         this.hull = hull;
         return this;
      }

      public HighlightedNpcBuilder tile(boolean tile) {
         this.tile = tile;
         return this;
      }

      public HighlightedNpcBuilder trueTile(boolean trueTile) {
         this.trueTile = trueTile;
         return this;
      }

      public HighlightedNpcBuilder swTile(boolean swTile) {
         this.swTile = swTile;
         return this;
      }

      public HighlightedNpcBuilder swTrueTile(boolean swTrueTile) {
         this.swTrueTile = swTrueTile;
         return this;
      }

      public HighlightedNpcBuilder outline(boolean outline) {
         this.outline = outline;
         return this;
      }

      public HighlightedNpcBuilder name(boolean name) {
         this.name = name;
         return this;
      }

      public HighlightedNpcBuilder nameOnMinimap(boolean nameOnMinimap) {
         this.nameOnMinimap = nameOnMinimap;
         return this;
      }

      public HighlightedNpcBuilder borderWidth(float borderWidth) {
         this.borderWidth$value = borderWidth;
         this.borderWidth$set = true;
         return this;
      }

      public HighlightedNpcBuilder outlineFeather(int outlineFeather) {
         this.outlineFeather = outlineFeather;
         return this;
      }

      public HighlightedNpcBuilder render(Predicate<NPC> render) {
         this.render = render;
         return this;
      }

      public HighlightedNpc build() {
         Color fillColor$value = this.fillColor$value;
         if (!this.fillColor$set) {
            fillColor$value = HighlightedNpc.$default$fillColor();
         }

         float borderWidth$value = this.borderWidth$value;
         if (!this.borderWidth$set) {
            borderWidth$value = HighlightedNpc.$default$borderWidth();
         }

         return new HighlightedNpc(this.npc, this.highlightColor, fillColor$value, this.hull, this.tile, this.trueTile, this.swTile, this.swTrueTile, this.outline, this.name, this.nameOnMinimap, borderWidth$value, this.outlineFeather, this.render);
      }

      public String toString() {
         String var10000 = String.valueOf(this.npc);
         return "HighlightedNpc.HighlightedNpcBuilder(npc=" + var10000 + ", highlightColor=" + String.valueOf(this.highlightColor) + ", fillColor$value=" + String.valueOf(this.fillColor$value) + ", hull=" + this.hull + ", tile=" + this.tile + ", trueTile=" + this.trueTile + ", swTile=" + this.swTile + ", swTrueTile=" + this.swTrueTile + ", outline=" + this.outline + ", name=" + this.name + ", nameOnMinimap=" + this.nameOnMinimap + ", borderWidth$value=" + this.borderWidth$value + ", outlineFeather=" + this.outlineFeather + ", render=" + String.valueOf(this.render) + ")";
      }
   }
}
