package net.runelite.client.plugins.playerindicators;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.inject.Inject;
import javax.inject.Singleton;
import net.runelite.api.Client;
import net.runelite.api.FriendsChatRank;
import net.runelite.api.IndexedSprite;
import net.runelite.api.Player;
import net.runelite.api.Point;
import net.runelite.client.game.ChatIconManager;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayUtil;
import net.runelite.client.util.ColorUtil;
import net.runelite.client.util.Text;

@Singleton
public class PlayerIndicatorsOverlay extends Overlay {
   private static final int ACTOR_OVERHEAD_TEXT_MARGIN = 40;
   private static final int ACTOR_HORIZONTAL_TEXT_MARGIN = 10;
   private static final Pattern IMG_TAG_PATTERN = Pattern.compile("<img=(\\d+)>");
   private final Client client;
   private final PlayerIndicatorsService playerIndicatorsService;
   private final PlayerIndicatorsConfig config;
   private final ChatIconManager chatIconManager;

   @Inject
   private PlayerIndicatorsOverlay(Client client, PlayerIndicatorsConfig config, PlayerIndicatorsService playerIndicatorsService, ChatIconManager chatIconManager) {
      this.client = client;
      this.config = config;
      this.playerIndicatorsService = playerIndicatorsService;
      this.chatIconManager = chatIconManager;
      this.setPosition(OverlayPosition.DYNAMIC);
      this.setPriority(0.5F);
   }

   public Dimension render(Graphics2D graphics) {
      this.playerIndicatorsService.forEachPlayer((player, decorations) -> {
         this.renderPlayerOverlay(graphics, player, decorations);
      });
      return null;
   }

   private void renderPlayerOverlay(Graphics2D graphics, Player actor, PlayerIndicatorsService.Decorations decorations) {
      PlayerNameLocation drawPlayerNamesConfig = this.config.playerNamePosition();
      if (drawPlayerNamesConfig != PlayerNameLocation.DISABLED) {
         int zOffset;
         switch (drawPlayerNamesConfig) {
            case MODEL_CENTER:
            case MODEL_RIGHT:
               zOffset = actor.getLogicalHeight() / 2;
               break;
            default:
               zOffset = actor.getLogicalHeight() + 40;
         }

         String rawName = Text.sanitize(actor.getName());
         String name = Text.removeTags(rawName);
         Point textLocation = actor.getCanvasTextLocation(graphics, name, zOffset);
         if (drawPlayerNamesConfig == PlayerNameLocation.MODEL_RIGHT) {
            textLocation = actor.getCanvasTextLocation(graphics, "", zOffset);
            if (textLocation == null) {
               return;
            }

            textLocation = new Point(textLocation.getX() + 10, textLocation.getY());
         }

         if (textLocation != null) {
            BufferedImage rankImage = null;
            if (decorations.getFriendsChatRank() != null && this.config.showFriendsChatRanks()) {
               if (decorations.getFriendsChatRank() != FriendsChatRank.UNRANKED) {
                  rankImage = this.chatIconManager.getRankImage(decorations.getFriendsChatRank());
               }
            } else if (decorations.getClanTitle() != null && this.config.showClanChatRanks()) {
               rankImage = this.chatIconManager.getRankImage(decorations.getClanTitle());
            }

            if (rankImage != null) {
               int imageWidth = rankImage.getWidth();
               int imageTextMargin;
               int imageNegativeMargin;
               if (drawPlayerNamesConfig == PlayerNameLocation.MODEL_RIGHT) {
                  imageTextMargin = imageWidth;
                  imageNegativeMargin = 0;
               } else {
                  imageTextMargin = imageWidth / 2;
                  imageNegativeMargin = imageWidth / 2;
               }

               int textHeight = graphics.getFontMetrics().getHeight() - graphics.getFontMetrics().getMaxDescent();
               Point imageLocation = new Point(textLocation.getX() - imageNegativeMargin - 1, textLocation.getY() - textHeight / 2 - rankImage.getHeight() / 2);
               OverlayUtil.renderImageLocation(graphics, imageLocation, rankImage);
               textLocation = new Point(textLocation.getX() + imageTextMargin, textLocation.getY());
            }

            if (IMG_TAG_PATTERN.matcher(rawName).find()) {
               this.renderNameWithIcons(graphics, textLocation, rawName, decorations.getColor());
            } else {
               OverlayUtil.renderTextLocation(graphics, textLocation, name, decorations.getColor());
            }

         }
      }
   }

   private void renderNameWithIcons(Graphics2D graphics, Point startLocation, String rawName, Color color) {
      IndexedSprite[] modIcons = this.client.getModIcons();
      Matcher matcher = IMG_TAG_PATTERN.matcher(rawName);
      int x = startLocation.getX();
      int y = startLocation.getY();
      int textHeight = graphics.getFontMetrics().getHeight() - graphics.getFontMetrics().getMaxDescent();
      Color textColor = ColorUtil.colorWithAlpha(color, 255);

      int lastEnd;
      String remaining;
      for(lastEnd = 0; matcher.find(); lastEnd = matcher.end()) {
         remaining = Text.removeTags(rawName.substring(lastEnd, matcher.start()));
         if (!remaining.isEmpty()) {
            graphics.setColor(Color.BLACK);
            graphics.drawString(remaining, x + 1, y + 1);
            graphics.setColor(textColor);
            graphics.drawString(remaining, x, y);
            x += graphics.getFontMetrics().stringWidth(remaining);
         }

         int iconIdx = Integer.parseInt(matcher.group(1));
         if (modIcons != null && iconIdx >= 0 && iconIdx < modIcons.length) {
            BufferedImage iconImage = indexedSpriteToImage(modIcons[iconIdx]);
            if (iconImage != null) {
               int iconY = y - textHeight / 2 - iconImage.getHeight() / 2;
               graphics.drawImage(iconImage, x, iconY, (ImageObserver)null);
               x += iconImage.getWidth() + 1;
            }
         }
      }

      remaining = Text.removeTags(rawName.substring(lastEnd));
      if (!remaining.isEmpty()) {
         graphics.setColor(Color.BLACK);
         graphics.drawString(remaining, x + 1, y + 1);
         graphics.setColor(textColor);
         graphics.drawString(remaining, x, y);
      }

   }

   private static BufferedImage indexedSpriteToImage(IndexedSprite sprite) {
      int width = sprite.getWidth();
      int height = sprite.getHeight();
      if (width > 0 && height > 0) {
         BufferedImage image = new BufferedImage(width, height, 2);
         byte[] pixels = sprite.getPixels();
         int[] palette = sprite.getPalette();

         for(int i = 0; i < pixels.length; ++i) {
            int paletteIdx = pixels[i] & 255;
            if (paletteIdx != 0) {
               image.setRGB(i % width, i / width, -16777216 | palette[paletteIdx]);
            }
         }

         return image;
      } else {
         return null;
      }
   }
}
