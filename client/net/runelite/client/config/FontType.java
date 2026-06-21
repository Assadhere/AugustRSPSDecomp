package net.runelite.client.config;

import java.awt.Font;
import net.runelite.client.ui.FontManager;

@ConfigSerializer(FontTypeSerializer.class)
public class FontType {
   public static final FontType REGULAR = (new FontType()).withFamily(FontManager.getRunescapeFont().getFamily()).withSize(16);
   public static final FontType BOLD = (new FontType()).withFamily(FontManager.getRunescapeBoldFont().getFamily()).withSize(16).withBold(true);
   public static final FontType SMALL = (new FontType()).withFamily(FontManager.getRunescapeSmallFont().getFamily()).withSize(16);
   String family;
   int size;
   boolean bold = false;
   boolean italic = false;
   private transient Font font;

   private int getStyle() {
      return (this.bold ? 1 : 0) | (this.italic ? 2 : 0);
   }

   public Font getFont() {
      return this.font != null ? this.font : (this.font = FontManager.getFallbackFont(this.family, this.getStyle(), this.size));
   }

   public FontType(String family, int size, boolean bold, boolean italic, Font font) {
      this.family = family;
      this.size = size;
      this.bold = bold;
      this.italic = italic;
      this.font = font;
   }

   public FontType() {
   }

   public String getFamily() {
      return this.family;
   }

   public int getSize() {
      return this.size;
   }

   public boolean isBold() {
      return this.bold;
   }

   public boolean isItalic() {
      return this.italic;
   }

   public FontType withFamily(String family) {
      return this.family == family ? this : new FontType(family, this.size, this.bold, this.italic, this.font);
   }

   public FontType withSize(int size) {
      return this.size == size ? this : new FontType(this.family, size, this.bold, this.italic, this.font);
   }

   public FontType withBold(boolean bold) {
      return this.bold == bold ? this : new FontType(this.family, this.size, bold, this.italic, this.font);
   }

   public FontType withItalic(boolean italic) {
      return this.italic == italic ? this : new FontType(this.family, this.size, this.bold, italic, this.font);
   }

   public FontType withFont(Font font) {
      return this.font == font ? this : new FontType(this.family, this.size, this.bold, this.italic, font);
   }
}
