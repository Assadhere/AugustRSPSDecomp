package net.runelite.client.ui;

import com.google.common.collect.ImmutableList;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;
import javax.swing.text.StyleContext;
import net.runelite.client.RuneLite;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FontManager {
   private static final Logger log = LoggerFactory.getLogger(FontManager.class);
   private static final List<String> customFontFamilies = new ArrayList();
   private static final Font runescapeFont;
   private static final Font runescapeSmallFont;
   private static final Font runescapeBoldFont;
   private static final Font defaultFont;
   private static final Font defaultBoldFont;

   private static void loadCustomFonts(GraphicsEnvironment ge) {
      Path customFontsPath = RuneLite.FONTS_DIR.toPath();
      if (Files.isDirectory(customFontsPath, new LinkOption[0])) {
         try {
            Stream<Path> paths = Files.list(customFontsPath);

            try {
               paths.filter((x$0) -> {
                  return Files.isRegularFile(x$0, new LinkOption[0]);
               }).filter((path) -> {
                  String name = path.getFileName().toString().toLowerCase();
                  return name.endsWith(".ttf") || name.endsWith(".otf");
               }).map((path) -> {
                  try {
                     InputStream inFont = Files.newInputStream(path);

                     Font var2;
                     try {
                        var2 = Font.createFont(0, inFont);
                     } catch (Throwable var5) {
                        if (inFont != null) {
                           try {
                              inFont.close();
                           } catch (Throwable var4) {
                              var5.addSuppressed(var4);
                           }
                        }

                        throw var5;
                     }

                     if (inFont != null) {
                        inFont.close();
                     }

                     return var2;
                  } catch (FontFormatException | IOException var6) {
                     Exception ex = var6;
                     log.error("Error loading custom font: {}", path, ex);
                     return null;
                  }
               }).filter(Objects::nonNull).filter((font) -> {
                  return !customFontFamilies.contains(font.getFamily());
               }).peek((font) -> {
                  log.info("Loaded custom font: {}", font.getFamily());
               }).forEach((font) -> {
                  ge.registerFont(font);
                  customFontFamilies.add(font.getFamily());
               });
            } catch (Throwable var6) {
               if (paths != null) {
                  try {
                     paths.close();
                  } catch (Throwable var5) {
                     var6.addSuppressed(var5);
                  }
               }

               throw var6;
            }

            if (paths != null) {
               paths.close();
            }
         } catch (IOException var7) {
            IOException ex = var7;
            log.error("Error loading fonts from: {}", customFontsPath, ex);
         }
      }

   }

   public static List<String> getBuiltInFonts() {
      return ImmutableList.of(runescapeFont.getFamily(), runescapeSmallFont.getFamily());
   }

   public static List<String> getSystemFonts() {
      GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
      List<String> families = new ArrayList(Arrays.asList(ge.getAvailableFontFamilyNames()));
      families.remove(runescapeFont.getFamily());
      families.remove(runescapeSmallFont.getFamily());
      families.removeAll(customFontFamilies);
      return families;
   }

   public static List<String> getCustomFonts() {
      GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
      loadCustomFonts(ge);
      return Collections.unmodifiableList(customFontFamilies);
   }

   public static Font getFallbackFont(String family, int style, int size) {
      return StyleContext.getDefaultStyleContext().getFont(family, style, size);
   }

   public static Font getRunescapeFont() {
      return runescapeFont;
   }

   public static Font getRunescapeSmallFont() {
      return runescapeSmallFont;
   }

   public static Font getRunescapeBoldFont() {
      return runescapeBoldFont;
   }

   public static Font getDefaultFont() {
      return defaultFont;
   }

   public static Font getDefaultBoldFont() {
      return defaultBoldFont;
   }

   static {
      GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();

      try {
         InputStream inRunescape = FontManager.class.getResourceAsStream("runescape.ttf");

         try {
            InputStream inRunescapeBold = FontManager.class.getResourceAsStream("runescape_bold.ttf");

            try {
               InputStream inRunescapeSmall = FontManager.class.getResourceAsStream("runescape_small.ttf");

               try {
                  Font font = Font.createFont(0, inRunescape);
                  Font boldFont = Font.createFont(0, inRunescapeBold);
                  Font smallFont = Font.createFont(0, inRunescapeSmall);
                  ge.registerFont(font);
                  ge.registerFont(boldFont);
                  ge.registerFont(smallFont);
                  runescapeFont = getFallbackFont(font.getFamily(), 0, 16);
                  runescapeBoldFont = getFallbackFont(boldFont.getFamily(), 1, 16);
                  runescapeSmallFont = getFallbackFont(smallFont.getFamily(), 0, 16);
               } catch (Throwable var10) {
                  if (inRunescapeSmall != null) {
                     try {
                        inRunescapeSmall.close();
                     } catch (Throwable var9) {
                        var10.addSuppressed(var9);
                     }
                  }

                  throw var10;
               }

               if (inRunescapeSmall != null) {
                  inRunescapeSmall.close();
               }
            } catch (Throwable var11) {
               if (inRunescapeBold != null) {
                  try {
                     inRunescapeBold.close();
                  } catch (Throwable var8) {
                     var11.addSuppressed(var8);
                  }
               }

               throw var11;
            }

            if (inRunescapeBold != null) {
               inRunescapeBold.close();
            }
         } catch (Throwable var12) {
            if (inRunescape != null) {
               try {
                  inRunescape.close();
               } catch (Throwable var7) {
                  var12.addSuppressed(var7);
               }
            }

            throw var12;
         }

         if (inRunescape != null) {
            inRunescape.close();
         }
      } catch (FontFormatException var13) {
         FontFormatException ex = var13;
         throw new RuntimeException("Font loaded, but format incorrect.", ex);
      } catch (IOException var14) {
         IOException ex = var14;
         throw new RuntimeException("Font file not found.", ex);
      }

      loadCustomFonts(ge);
      defaultFont = new Font("Dialog", 0, 16);
      defaultBoldFont = new Font("Dialog", 1, 16);
      RuneLite.FONTS_DIR.mkdirs();
   }
}
