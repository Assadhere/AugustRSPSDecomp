package net.runelite.client.plugins.specialcounter;

import java.awt.Color;
import java.awt.Font;
import java.awt.image.BufferedImage;
import javax.annotation.Nullable;
import lombok.NonNull;
import net.runelite.client.ui.FontManager;

final class PlayerInfoDrop {
   private final int startCycle;
   private final int endCycle;
   private final int playerIdx;
   private final String text;
   @Nullable
   private final BufferedImage textBackground;
   private final int startHeightOffset;
   private final int endHeightOffset;
   private final Font font;
   private final Color color;
   private final @NonNull BufferedImage image;

   private static int $default$endHeightOffset() {
      return 200;
   }

   private static Font $default$font() {
      return FontManager.getRunescapeBoldFont();
   }

   private static Color $default$color() {
      return Color.WHITE;
   }

   PlayerInfoDrop(int startCycle, int endCycle, int playerIdx, String text, @Nullable BufferedImage textBackground, int startHeightOffset, int endHeightOffset, Font font, Color color, @NonNull BufferedImage image) {
      if (image == null) {
         throw new NullPointerException("image is marked non-null but is null");
      } else {
         this.startCycle = startCycle;
         this.endCycle = endCycle;
         this.playerIdx = playerIdx;
         this.text = text;
         this.textBackground = textBackground;
         this.startHeightOffset = startHeightOffset;
         this.endHeightOffset = endHeightOffset;
         this.font = font;
         this.color = color;
         this.image = image;
      }
   }

   public static PlayerInfoDropBuilder builder() {
      return new PlayerInfoDropBuilder();
   }

   public int getStartCycle() {
      return this.startCycle;
   }

   public int getEndCycle() {
      return this.endCycle;
   }

   public int getPlayerIdx() {
      return this.playerIdx;
   }

   public String getText() {
      return this.text;
   }

   @Nullable
   public BufferedImage getTextBackground() {
      return this.textBackground;
   }

   public int getStartHeightOffset() {
      return this.startHeightOffset;
   }

   public int getEndHeightOffset() {
      return this.endHeightOffset;
   }

   public Font getFont() {
      return this.font;
   }

   public Color getColor() {
      return this.color;
   }

   public @NonNull BufferedImage getImage() {
      return this.image;
   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof PlayerInfoDrop)) {
         return false;
      } else {
         PlayerInfoDrop other = (PlayerInfoDrop)o;
         if (this.getStartCycle() != other.getStartCycle()) {
            return false;
         } else if (this.getEndCycle() != other.getEndCycle()) {
            return false;
         } else if (this.getPlayerIdx() != other.getPlayerIdx()) {
            return false;
         } else if (this.getStartHeightOffset() != other.getStartHeightOffset()) {
            return false;
         } else if (this.getEndHeightOffset() != other.getEndHeightOffset()) {
            return false;
         } else {
            Object this$text = this.getText();
            Object other$text = other.getText();
            if (this$text == null) {
               if (other$text != null) {
                  return false;
               }
            } else if (!this$text.equals(other$text)) {
               return false;
            }

            label74: {
               Object this$textBackground = this.getTextBackground();
               Object other$textBackground = other.getTextBackground();
               if (this$textBackground == null) {
                  if (other$textBackground == null) {
                     break label74;
                  }
               } else if (this$textBackground.equals(other$textBackground)) {
                  break label74;
               }

               return false;
            }

            Object this$font = this.getFont();
            Object other$font = other.getFont();
            if (this$font == null) {
               if (other$font != null) {
                  return false;
               }
            } else if (!this$font.equals(other$font)) {
               return false;
            }

            Object this$color = this.getColor();
            Object other$color = other.getColor();
            if (this$color == null) {
               if (other$color != null) {
                  return false;
               }
            } else if (!this$color.equals(other$color)) {
               return false;
            }

            Object this$image = this.getImage();
            Object other$image = other.getImage();
            if (this$image == null) {
               if (other$image != null) {
                  return false;
               }
            } else if (!this$image.equals(other$image)) {
               return false;
            }

            return true;
         }
      }
   }

   public int hashCode() {
      int PRIME = true;
      int result = 1;
      result = result * 59 + this.getStartCycle();
      result = result * 59 + this.getEndCycle();
      result = result * 59 + this.getPlayerIdx();
      result = result * 59 + this.getStartHeightOffset();
      result = result * 59 + this.getEndHeightOffset();
      Object $text = this.getText();
      result = result * 59 + ($text == null ? 43 : $text.hashCode());
      Object $textBackground = this.getTextBackground();
      result = result * 59 + ($textBackground == null ? 43 : $textBackground.hashCode());
      Object $font = this.getFont();
      result = result * 59 + ($font == null ? 43 : $font.hashCode());
      Object $color = this.getColor();
      result = result * 59 + ($color == null ? 43 : $color.hashCode());
      Object $image = this.getImage();
      result = result * 59 + ($image == null ? 43 : $image.hashCode());
      return result;
   }

   public String toString() {
      int var10000 = this.getStartCycle();
      return "PlayerInfoDrop(startCycle=" + var10000 + ", endCycle=" + this.getEndCycle() + ", playerIdx=" + this.getPlayerIdx() + ", text=" + this.getText() + ", textBackground=" + String.valueOf(this.getTextBackground()) + ", startHeightOffset=" + this.getStartHeightOffset() + ", endHeightOffset=" + this.getEndHeightOffset() + ", font=" + String.valueOf(this.getFont()) + ", color=" + String.valueOf(this.getColor()) + ", image=" + String.valueOf(this.getImage()) + ")";
   }

   public static class PlayerInfoDropBuilder {
      private int startCycle;
      private int endCycle;
      private int playerIdx;
      private String text;
      private BufferedImage textBackground;
      private int startHeightOffset;
      private boolean endHeightOffset$set;
      private int endHeightOffset$value;
      private boolean font$set;
      private Font font$value;
      private boolean color$set;
      private Color color$value;
      private BufferedImage image;

      PlayerInfoDropBuilder() {
      }

      public PlayerInfoDropBuilder startCycle(int startCycle) {
         this.startCycle = startCycle;
         return this;
      }

      public PlayerInfoDropBuilder endCycle(int endCycle) {
         this.endCycle = endCycle;
         return this;
      }

      public PlayerInfoDropBuilder playerIdx(int playerIdx) {
         this.playerIdx = playerIdx;
         return this;
      }

      public PlayerInfoDropBuilder text(String text) {
         this.text = text;
         return this;
      }

      public PlayerInfoDropBuilder textBackground(@Nullable BufferedImage textBackground) {
         this.textBackground = textBackground;
         return this;
      }

      public PlayerInfoDropBuilder startHeightOffset(int startHeightOffset) {
         this.startHeightOffset = startHeightOffset;
         return this;
      }

      public PlayerInfoDropBuilder endHeightOffset(int endHeightOffset) {
         this.endHeightOffset$value = endHeightOffset;
         this.endHeightOffset$set = true;
         return this;
      }

      public PlayerInfoDropBuilder font(Font font) {
         this.font$value = font;
         this.font$set = true;
         return this;
      }

      public PlayerInfoDropBuilder color(Color color) {
         this.color$value = color;
         this.color$set = true;
         return this;
      }

      public PlayerInfoDropBuilder image(@NonNull BufferedImage image) {
         if (image == null) {
            throw new NullPointerException("image is marked non-null but is null");
         } else {
            this.image = image;
            return this;
         }
      }

      public PlayerInfoDrop build() {
         int endHeightOffset$value = this.endHeightOffset$value;
         if (!this.endHeightOffset$set) {
            endHeightOffset$value = PlayerInfoDrop.$default$endHeightOffset();
         }

         Font font$value = this.font$value;
         if (!this.font$set) {
            font$value = PlayerInfoDrop.$default$font();
         }

         Color color$value = this.color$value;
         if (!this.color$set) {
            color$value = PlayerInfoDrop.$default$color();
         }

         return new PlayerInfoDrop(this.startCycle, this.endCycle, this.playerIdx, this.text, this.textBackground, this.startHeightOffset, endHeightOffset$value, font$value, color$value, this.image);
      }

      public String toString() {
         int var10000 = this.startCycle;
         return "PlayerInfoDrop.PlayerInfoDropBuilder(startCycle=" + var10000 + ", endCycle=" + this.endCycle + ", playerIdx=" + this.playerIdx + ", text=" + this.text + ", textBackground=" + String.valueOf(this.textBackground) + ", startHeightOffset=" + this.startHeightOffset + ", endHeightOffset$value=" + this.endHeightOffset$value + ", font$value=" + String.valueOf(this.font$value) + ", color$value=" + String.valueOf(this.color$value) + ", image=" + String.valueOf(this.image) + ")";
      }
   }
}
