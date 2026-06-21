package net.runelite.client.plugins.augustcustom.events;

import custom.UpdateEventsInfoScript;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.time.Duration;
import java.time.Instant;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import net.runelite.client.game.SpriteManager;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.FontManager;
import net.runelite.client.util.RSTimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class EventPanel extends JPanel {
   private static final Logger log = LoggerFactory.getLogger(EventPanel.class);
   private static final int CLOCK_SPRITE_ARCHIVE = 4525;
   private static final int CLOCK_SPRITE_FILE = 0;
   private static final Border NORMAL_BORDER = new EmptyBorder(5, 5, 5, 5);
   private static final Border CLICKABLE_BORDER_NORMAL;
   private static final Border CLICKABLE_BORDER_HOVER;
   private static final Color HOVER_BACKGROUND_COLOR;
   private static final Color NORMAL_BACKGROUND_COLOR;
   private final JLabel titleLabel;
   private JLabel timerTextLabel;
   private JLabel spriteLabel;
   private JLabel clockIconLabel;
   private Instant endTime;
   private Timer timer;
   private final boolean isClickable;
   private final ActionListener clickListener;

   EventPanel(UpdateEventsInfoScript.EventPanel panel, SpriteManager spriteManager, ActionListener buttonClickListener) {
      this(panel.getName(), panel.getDescription(), Duration.of((long)panel.getTicksRemaining(), RSTimeUnit.GAME_TICKS), spriteManager, panel.getSpriteArchive(), panel.getSpriteFile(), panel.getButton() != null ? buttonClickListener : null);
   }

   EventPanel(String title, String description, Duration duration, SpriteManager spriteManager, int spriteArchiveId, int spriteFileId, ActionListener buttonClickListener) {
      this.clickListener = buttonClickListener;
      this.isClickable = this.clickListener != null;
      Image loadedSprite = null;
      BufferedImage clockSprite = null;
      Exception e;
      if (spriteManager != null && spriteArchiveId > 0 && spriteFileId >= 0) {
         try {
            loadedSprite = spriteManager.getSprite(spriteArchiveId, spriteFileId);
            if (loadedSprite != null) {
               loadedSprite = ((Image)loadedSprite).getScaledInstance(32, 32, 4);
            }
         } catch (Exception var23) {
            e = var23;
            log.warn("Failed to load event sprite [archive={}, file={}]: {}", new Object[]{spriteArchiveId, spriteFileId, e.getMessage()});
         }
      }

      if (spriteManager != null) {
         try {
            clockSprite = spriteManager.getSprite(4525, 0);
         } catch (Exception var22) {
            e = var22;
            log.warn("Failed to load clock sprite [archive={}, file={}]: {}", new Object[]{4525, 0, e.getMessage()});
         }
      }

      int parentPadding = 10;
      int hGap = 5;
      int availableWidth = 208 - parentPadding;
      int cellWidth = (availableWidth - hGap) / 2;
      int cellBorderPadding = 10;
      int fudgeFactor = 12;
      int contentWidth = cellWidth - cellBorderPadding - fudgeFactor;
      contentWidth = Math.max(10, contentWidth);
      this.setLayout(new GridBagLayout());
      this.setBackground(NORMAL_BACKGROUND_COLOR);
      this.setOpaque(true);
      this.setBorder(this.isClickable ? CLICKABLE_BORDER_NORMAL : NORMAL_BORDER);
      this.setToolTipText(description);
      GridBagConstraints gbc = new GridBagConstraints();
      gbc.gridx = 0;
      gbc.gridy = -1;
      gbc.weightx = 1.0;
      gbc.fill = 2;
      gbc.anchor = 10;
      gbc.insets = new Insets(2, 0, 2, 0);
      Font titleFont = FontManager.getRunescapeSmallFont();
      Font defaultFont = FontManager.getRunescapeSmallFont();
      this.titleLabel = new JLabel();
      String formattedTitle = "<html><body style='text-align: center; width: " + contentWidth + "px'>" + title + "</body></html>";
      this.titleLabel.setText(formattedTitle);
      this.titleLabel.setForeground(Color.WHITE);
      this.titleLabel.setFont(titleFont);
      this.titleLabel.setOpaque(false);
      this.titleLabel.setHorizontalAlignment(0);
      gbc.anchor = 11;
      this.add(this.titleLabel, gbc);
      gbc.anchor = 10;
      gbc.fill = 0;
      if (loadedSprite != null) {
         this.spriteLabel = new JLabel(new ImageIcon((Image)loadedSprite));
         this.spriteLabel.setOpaque(false);
         this.add(this.spriteLabel, gbc);
      } else {
         this.add(Box.createVerticalStrut(5), gbc);
      }

      gbc.fill = 2;
      if (!duration.isZero() && !duration.isNegative()) {
         JPanel timerWrapper = new JPanel(new FlowLayout(1, 2, 0));
         timerWrapper.setOpaque(false);
         if (clockSprite != null) {
            this.clockIconLabel = new JLabel(new ImageIcon(clockSprite));
            this.clockIconLabel.setOpaque(false);
            timerWrapper.add(this.clockIconLabel);
         }

         this.timerTextLabel = new JLabel("Calculating...");
         this.timerTextLabel.setForeground(ColorScheme.LIGHT_GRAY_COLOR);
         this.timerTextLabel.setFont(defaultFont);
         this.timerTextLabel.setOpaque(false);
         timerWrapper.add(this.timerTextLabel);
         gbc.anchor = 15;
         this.add(timerWrapper, gbc);
         this.endTime = Instant.now().plus(duration);
         this.startTimer();
      }

      if (this.isClickable) {
         this.setCursor(Cursor.getPredefinedCursor(12));
         this.addMouseListener(new MouseAdapter() {
            private boolean mousePressed = false;

            public void mousePressed(MouseEvent e) {
               this.mousePressed = true;
               EventPanel.this.setBackground(ColorScheme.MEDIUM_GRAY_COLOR);
               EventPanel.this.setBorder(EventPanel.CLICKABLE_BORDER_HOVER);
            }

            public void mouseReleased(MouseEvent e) {
               this.mousePressed = false;
               if (EventPanel.this.contains(e.getPoint()) && e.getButton() == 1) {
                  EventPanel.this.setBackground(EventPanel.HOVER_BACKGROUND_COLOR);
                  EventPanel.this.setBorder(EventPanel.CLICKABLE_BORDER_HOVER);
                  EventPanel.this.clickListener.actionPerformed((ActionEvent)null);
               } else {
                  EventPanel.this.setBackground(EventPanel.NORMAL_BACKGROUND_COLOR);
                  EventPanel.this.setBorder(EventPanel.CLICKABLE_BORDER_NORMAL);
               }

            }

            public void mouseEntered(MouseEvent e) {
               if (!this.mousePressed) {
                  EventPanel.this.setBackground(EventPanel.HOVER_BACKGROUND_COLOR);
                  EventPanel.this.setBorder(EventPanel.CLICKABLE_BORDER_HOVER);
               }

            }

            public void mouseExited(MouseEvent e) {
               this.mousePressed = false;
               EventPanel.this.setBackground(EventPanel.NORMAL_BACKGROUND_COLOR);
               EventPanel.this.setBorder(EventPanel.CLICKABLE_BORDER_NORMAL);
            }
         });
      }

   }

   private void startTimer() {
      if (this.timer == null) {
         this.timer = new Timer(true);
         this.timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
               if (EventPanel.this.endTime != null && EventPanel.this.timerTextLabel != null) {
                  Duration remaining = Duration.between(Instant.now(), EventPanel.this.endTime);
                  String formattedTime;
                  if (!remaining.isNegative() && !remaining.isZero()) {
                     long totalSeconds = remaining.getSeconds();
                     long hours = TimeUnit.SECONDS.toHours(totalSeconds);
                     long minutes = TimeUnit.SECONDS.toMinutes(totalSeconds) % 60L;
                     long seconds = totalSeconds % 60L;
                     StringBuilder sb = new StringBuilder();
                     if (hours > 0L) {
                        sb.append(hours).append("h ");
                     }

                     if (minutes > 0L || hours > 0L && seconds > 0L) {
                        sb.append(minutes).append("m ");
                     }

                     if (seconds >= 0L) {
                        if (hours <= 0L && minutes <= 0L && seconds <= 0L) {
                           if (sb.length() == 0) {
                              sb.append("0s");
                           }
                        } else {
                           sb.append(seconds).append("s");
                        }
                     }

                     formattedTime = sb.toString().trim();
                     if (formattedTime.isEmpty()) {
                        formattedTime = "0s";
                     }
                  } else {
                     formattedTime = "Ended";
                     EventPanel.this.cancelTimerInternal();
                  }

                  String finalFormattedTime = formattedTime;
                  SwingUtilities.invokeLater(() -> {
                     EventPanel.this.timerTextLabel.setText(finalFormattedTime);
                  });
               } else {
                  EventPanel.this.cancelTimerInternal();
               }
            }
         }, 0L, 1000L);
      }

   }

   public void cancelTimer() {
      this.cancelTimerInternal();
   }

   private void cancelTimerInternal() {
      if (this.timer != null) {
         this.timer.cancel();
         this.timer = null;
      }

   }

   public void removeNotify() {
      super.removeNotify();
      this.cancelTimer();
   }

   private static Border createRoundedRectBorder(Color color, int thickness, int radius) {
      return new RoundedLineBorder(color, thickness, radius);
   }

   static {
      CLICKABLE_BORDER_NORMAL = new CompoundBorder(createRoundedRectBorder(ColorScheme.LIGHT_GRAY_COLOR.brighter(), 1, 5), new EmptyBorder(4, 4, 4, 4));
      CLICKABLE_BORDER_HOVER = new CompoundBorder(createRoundedRectBorder(ColorScheme.BRAND_ORANGE, 1, 5), new EmptyBorder(4, 4, 4, 4));
      HOVER_BACKGROUND_COLOR = ColorScheme.DARKER_GRAY_HOVER_COLOR;
      NORMAL_BACKGROUND_COLOR = ColorScheme.DARKER_GRAY_COLOR;
   }

   static class RoundedLineBorder extends LineBorder {
      private int radius;

      RoundedLineBorder(Color color, int thickness, int radius) {
         super(color, thickness);
         this.radius = radius;
      }

      public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
         Graphics2D g2 = (Graphics2D)g.create();

         try {
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(this.getLineColor());
            g2.setStroke(new BasicStroke((float)this.getThickness()));
            int adj = this.getThickness() / 2;
            g2.drawRoundRect(x + adj, y + adj, width - this.getThickness(), height - this.getThickness(), this.radius, this.radius);
         } finally {
            g2.dispose();
         }

      }

      public Insets getBorderInsets(Component c, Insets insets) {
         int offs = this.getThickness() + 1;
         insets.set(offs, offs, offs, offs);
         return insets;
      }

      public Insets getBorderInsets(Component c) {
         int offs = this.getThickness() + 1;
         return new Insets(offs, offs, offs, offs);
      }
   }
}
