package net.runelite.client.ui.overlay.components;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.Nullable;
import net.runelite.client.ui.overlay.RenderableEntity;

public class TextComponent implements RenderableEntity {
   private static final Pattern COL_TAG_PATTERN = Pattern.compile("<col=([0-9a-fA-F]{2,6})>");
   private String text;
   private int positionX;
   private int positionY;
   private Color color;
   private boolean outline;
   @Nullable
   private Font font;

   public TextComponent() {
      this.color = Color.WHITE;
   }

   public void setPosition(Point position) {
      this.setPosition(position.x, position.y);
   }

   public void setPosition(int x, int y) {
      this.positionX = x;
      this.positionY = y;
   }

   public Dimension render(Graphics2D graphics) {
      Font originalFont = null;
      if (this.font != null) {
         originalFont = graphics.getFont();
         graphics.setFont(this.font);
      }

      FontMetrics fontMetrics = graphics.getFontMetrics();
      Matcher matcher = COL_TAG_PATTERN.matcher(this.text);
      Color textColor = this.color;
      int idx = 0;

      int width;
      String color;
      for(width = 0; matcher.find(); textColor = Color.decode("#" + color)) {
         color = matcher.group(1);
         String s = this.text.substring(idx, matcher.start());
         idx = matcher.end();
         this.renderText(graphics, textColor, this.positionX + width, this.positionY, s);
         width += fontMetrics.stringWidth(s);
      }

      color = this.text.substring(idx);
      this.renderText(graphics, textColor, this.positionX + width, this.positionY, color);
      width += fontMetrics.stringWidth(color);
      int height = fontMetrics.getHeight();
      if (originalFont != null) {
         graphics.setFont(originalFont);
      }

      return new Dimension(width, height);
   }

   private void renderText(Graphics2D graphics, Color color, int x, int y, String text) {
      if (!text.isEmpty()) {
         graphics.setColor(Color.BLACK);
         if (this.outline) {
            graphics.drawString(text, x, y + 1);
            graphics.drawString(text, x, y - 1);
            graphics.drawString(text, x + 1, y);
            graphics.drawString(text, x - 1, y);
         } else {
            graphics.drawString(text, x + 1, y + 1);
         }

         graphics.setColor(color);
         graphics.drawString(text, x, y);
      }
   }

   public void setText(String text) {
      this.text = text;
   }

   public void setColor(Color color) {
      this.color = color;
   }

   public void setOutline(boolean outline) {
      this.outline = outline;
   }

   public void setFont(@Nullable Font font) {
      this.font = font;
   }
}
