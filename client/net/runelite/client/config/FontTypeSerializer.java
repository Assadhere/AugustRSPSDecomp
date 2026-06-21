package net.runelite.client.config;

import com.google.gson.Gson;
import com.google.inject.Inject;

class FontTypeSerializer implements Serializer<FontType> {
   private final Gson gson;

   @Inject
   private FontTypeSerializer(Gson gson) {
      this.gson = gson;
   }

   public String serialize(FontType value) {
      return this.gson.toJson(value);
   }

   public FontType deserialize(String s) {
      if ("REGULAR".equals(s)) {
         return FontType.REGULAR;
      } else if ("BOLD".equals(s)) {
         return FontType.BOLD;
      } else {
         return "SMALL".equals(s) ? FontType.SMALL : (FontType)this.gson.fromJson(s, FontType.class);
      }
   }
}
