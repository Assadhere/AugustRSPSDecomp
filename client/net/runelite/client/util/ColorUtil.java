package net.runelite.client.util;

import com.google.common.primitives.Ints;
import java.awt.Color;
import java.util.regex.Pattern;
import javax.annotation.Nonnull;

public class ColorUtil {
   public static final int MAX_RGB_VALUE = 255;
   public static final int MIN_RGB_VALUE = 0;
   private static final String OPENING_COLOR_TAG_START = "<col=";
   private static final String OPENING_COLOR_TAG_END = ">";
   public static final String CLOSING_COLOR_TAG = "</col>";
   private static final Pattern ALPHA_HEX_PATTERN = Pattern.compile("^(#|0x)?[0-9a-fA-F]{7,8}");
   private static final Pattern HEX_PATTERN = Pattern.compile("^(#|0x)?[0-9a-fA-F]{1,8}");

   public static String colorTag(Color color) {
      return "<col=" + colorToHexCode(color) + ">";
   }

   public static String prependColorTag(String str, Color color) {
      String var10000 = colorTag(color);
      return var10000 + str;
   }

   public static String wrapWithColorTag(String str, Color color) {
      String var10000 = prependColorTag(str, color);
      return var10000 + "</col>";
   }

   public static String toHexColor(Color color) {
      return "#" + colorToHexCode(color);
   }

   public static Color colorLerp(Color a, Color b, double t) {
      double r1 = (double)a.getRed();
      double r2 = (double)b.getRed();
      double g1 = (double)a.getGreen();
      double g2 = (double)b.getGreen();
      double b1 = (double)a.getBlue();
      double b2 = (double)b.getBlue();
      double a1 = (double)a.getAlpha();
      double a2 = (double)b.getAlpha();
      return new Color((int)Math.round(r1 + t * (r2 - r1)), (int)Math.round(g1 + t * (g2 - g1)), (int)Math.round(b1 + t * (b2 - b1)), (int)Math.round(a1 + t * (a2 - a1)));
   }

   public static String colorToHexCode(Color color) {
      return String.format("%06x", color.getRGB() & 16777215);
   }

   public static String colorToAlphaHexCode(Color color) {
      return String.format("%08x", color.getRGB());
   }

   public static Color colorWithAlpha(Color color, int alpha) {
      if (color.getAlpha() == alpha) {
         return color;
      } else {
         alpha = constrainValue(alpha);
         return new Color(color.getRGB() & 16777215 | alpha << 24, true);
      }
   }

   public static boolean isAlphaHex(String hex) {
      return ALPHA_HEX_PATTERN.matcher(hex).matches();
   }

   public static boolean isHex(String hex) {
      return HEX_PATTERN.matcher(hex).matches();
   }

   public static int constrainValue(int value) {
      return Ints.constrainToRange(value, 0, 255);
   }

   public static Color fromString(String string) {
      try {
         int i = Integer.decode(string);
         return new Color(i, true);
      } catch (NumberFormatException var2) {
         return null;
      }
   }

   public static Color fromHex(String hex) {
      if (!hex.startsWith("#") && !hex.startsWith("0x")) {
         hex = "#" + hex;
      }

      if ((hex.length() > 7 || !hex.startsWith("#")) && (hex.length() > 8 || !hex.startsWith("0x"))) {
         try {
            return new Color(Long.decode(hex).intValue(), true);
         } catch (NumberFormatException var3) {
            return null;
         }
      } else {
         try {
            return Color.decode(hex);
         } catch (NumberFormatException var2) {
            return null;
         }
      }
   }

   public static Color fromObject(@Nonnull Object object) {
      int i = object.hashCode();
      float h = (float)(i % 360) / 360.0F;
      return Color.getHSBColor(h, 1.0F, 1.0F);
   }
}
