package net.runelite.client.ui.overlay.components;

import com.google.common.base.Strings;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.text.DecimalFormat;

public class ProgressBarComponent implements LayoutableRenderableEntity {
   private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("0.0");
   private static final DecimalFormat DECIMAL_FORMAT_ABS = new DecimalFormat("#0");
   private static final int SIDE_LABEL_OFFSET = 4;
   private long minimum;
   private long maximum = 100L;
   private double value;
   private LabelDisplayMode labelDisplayMode;
   private String centerLabel;
   private String leftLabel;
   private String rightLabel;
   private Color foregroundColor;
   private Color backgroundColor;
   private Color fontColor;
   private Point preferredLocation;
   private Dimension preferredSize;
   private final Rectangle bounds;

   public ProgressBarComponent() {
      this.labelDisplayMode = ProgressBarComponent.LabelDisplayMode.PERCENTAGE;
      this.foregroundColor = new Color(82, 161, 82);
      this.backgroundColor = new Color(255, 255, 255, 127);
      this.fontColor = Color.WHITE;
      this.preferredLocation = new Point();
      this.preferredSize = new Dimension(129, 16);
      this.bounds = new Rectangle();
   }

   public Dimension render(Graphics2D graphics) {
      FontMetrics metrics = graphics.getFontMetrics();
      int barX = this.preferredLocation.x;
      int barY = this.preferredLocation.y;
      long span = this.maximum - this.minimum;
      double currentValue = this.value - (double)this.minimum;
      double pc = currentValue / (double)span;
      String textToWrite;
      switch (this.labelDisplayMode) {
         case TEXT_ONLY:
            textToWrite = "";
            break;
         case PERCENTAGE:
            textToWrite = formatPercentageProgress(pc);
            break;
         case BOTH:
            String var10000 = formatFullProgress(currentValue, this.maximum);
            textToWrite = var10000 + " (" + formatPercentageProgress(pc) + ")";
            break;
         case FULL:
         default:
            textToWrite = formatFullProgress(currentValue, this.maximum);
      }

      if (!Strings.isNullOrEmpty(this.centerLabel)) {
         if (!textToWrite.isEmpty()) {
            textToWrite = textToWrite + " ";
         }

         textToWrite = textToWrite + this.centerLabel;
      }

      int width = this.preferredSize.width;
      int height = Math.max(this.preferredSize.height, 16);
      int progressTextX = barX + (width - metrics.stringWidth(textToWrite)) / 2;
      int progressTextY = barY + (height - metrics.getHeight()) / 2 + metrics.getHeight();
      int progressFill = (int)((double)width * Math.min(1.0, pc));
      graphics.setColor(this.backgroundColor);
      graphics.fillRect(barX + progressFill, barY, width - progressFill, height);
      graphics.setColor(this.foregroundColor);
      graphics.fillRect(barX, barY, progressFill, height);
      TextComponent textComponent = new TextComponent();
      textComponent.setPosition(progressTextX, progressTextY);
      textComponent.setColor(this.fontColor);
      textComponent.setText(textToWrite);
      textComponent.render(graphics);
      TextComponent leftTextComponent;
      if (this.leftLabel != null) {
         leftTextComponent = new TextComponent();
         leftTextComponent.setPosition(barX + 4, progressTextY);
         leftTextComponent.setColor(this.fontColor);
         leftTextComponent.setText(this.leftLabel);
         leftTextComponent.render(graphics);
      }

      if (this.rightLabel != null) {
         leftTextComponent = new TextComponent();
         leftTextComponent.setPosition(barX + width - metrics.stringWidth(this.rightLabel) - 4, progressTextY);
         leftTextComponent.setColor(this.fontColor);
         leftTextComponent.setText(this.rightLabel);
         leftTextComponent.render(graphics);
      }

      Dimension dimension = new Dimension(width, height);
      this.bounds.setLocation(this.preferredLocation);
      this.bounds.setSize(dimension);
      return dimension;
   }

   private static String formatFullProgress(double current, long maximum) {
      String var10000 = DECIMAL_FORMAT_ABS.format(Math.floor(current));
      return var10000 + "/" + maximum;
   }

   private static String formatPercentageProgress(double ratio) {
      return DECIMAL_FORMAT.format(ratio * 100.0) + "%";
   }

   public void setMinimum(long minimum) {
      this.minimum = minimum;
   }

   public void setMaximum(long maximum) {
      this.maximum = maximum;
   }

   public void setValue(double value) {
      this.value = value;
   }

   public void setLabelDisplayMode(LabelDisplayMode labelDisplayMode) {
      this.labelDisplayMode = labelDisplayMode;
   }

   public void setCenterLabel(String centerLabel) {
      this.centerLabel = centerLabel;
   }

   public void setLeftLabel(String leftLabel) {
      this.leftLabel = leftLabel;
   }

   public void setRightLabel(String rightLabel) {
      this.rightLabel = rightLabel;
   }

   public void setForegroundColor(Color foregroundColor) {
      this.foregroundColor = foregroundColor;
   }

   public void setBackgroundColor(Color backgroundColor) {
      this.backgroundColor = backgroundColor;
   }

   public void setFontColor(Color fontColor) {
      this.fontColor = fontColor;
   }

   public void setPreferredLocation(Point preferredLocation) {
      this.preferredLocation = preferredLocation;
   }

   public void setPreferredSize(Dimension preferredSize) {
      this.preferredSize = preferredSize;
   }

   public Rectangle getBounds() {
      return this.bounds;
   }

   public static enum LabelDisplayMode {
      PERCENTAGE,
      FULL,
      TEXT_ONLY,
      BOTH;
   }
}
