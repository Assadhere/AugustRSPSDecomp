package net.runelite.client.plugins.poison;

import com.google.inject.Provides;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.text.MessageFormat;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Hashtable;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.events.VarbitChanged;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.game.AlternateSprites;
import net.runelite.client.game.SpriteManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.FontManager;
import net.runelite.client.ui.overlay.OverlayManager;
import net.runelite.client.ui.overlay.infobox.InfoBoxManager;
import net.runelite.client.util.ColorUtil;
import net.runelite.client.util.ImageUtil;

@PluginDescriptor(
   name = "Poison",
   description = "Tracks current damage values for Poison and Venom",
   tags = {"combat", "poison", "venom", "heart", "hp"}
)
public class PoisonPlugin extends Plugin {
   static final int POISON_TICK_MILLIS = 18200;
   private static final int VENOM_THRESHOLD = 1000000;
   private static final int VENOM_MAXIUMUM_DAMAGE = 20;
   private static final BufferedImage HEART_DISEASE = ImageUtil.resizeCanvas(ImageUtil.loadImageResource(AlternateSprites.class, "1067-DISEASE.png"), 26, 26);
   private static final BufferedImage HEART_POISON = ImageUtil.resizeCanvas(ImageUtil.loadImageResource(AlternateSprites.class, "1067-POISON.png"), 26, 26);
   private static final BufferedImage HEART_VENOM = ImageUtil.resizeCanvas(ImageUtil.loadImageResource(AlternateSprites.class, "1067-VENOM.png"), 26, 26);
   @Inject
   private Client client;
   @Inject
   private ClientThread clientThread;
   @Inject
   private PoisonOverlay poisonOverlay;
   @Inject
   private OverlayManager overlayManager;
   @Inject
   private InfoBoxManager infoBoxManager;
   @Inject
   private SpriteManager spriteManager;
   @Inject
   private PoisonConfig config;
   private int lastDamage;
   private boolean envenomed;
   private PoisonInfobox infobox;
   private Instant poisonNaturalCure;
   private Instant nextPoisonTick;
   private BufferedImage heart;

   @Provides
   PoisonConfig getConfig(ConfigManager configManager) {
      return (PoisonConfig)configManager.getConfig(PoisonConfig.class);
   }

   protected void startUp() throws Exception {
      this.overlayManager.add(this.poisonOverlay);
      if (this.client.getGameState() == GameState.LOGGED_IN) {
         this.clientThread.invoke(this::checkHealthIcon);
      }

   }

   protected void shutDown() throws Exception {
      this.overlayManager.remove(this.poisonOverlay);
      if (this.infobox != null) {
         this.infoBoxManager.removeInfoBox(this.infobox);
         this.infobox = null;
      }

      this.envenomed = false;
      this.lastDamage = 0;
      this.poisonNaturalCure = null;
      this.nextPoisonTick = null;
      this.clientThread.invoke(this::resetHealthIcon);
   }

   @Subscribe
   public void onVarbitChanged(VarbitChanged event) {
      if (event.getVarpId() == 102) {
         int poisonValue = event.getValue();
         this.nextPoisonTick = Instant.now().plus(Duration.of(18200L, ChronoUnit.MILLIS));
         int damage = nextDamage(poisonValue);
         this.lastDamage = damage;
         this.envenomed = poisonValue >= 1000000;
         if (poisonValue < 1000000) {
            this.poisonNaturalCure = Instant.now().plus(Duration.of((long)(18200 * poisonValue), ChronoUnit.MILLIS));
         } else {
            this.poisonNaturalCure = null;
         }

         if (this.config.showInfoboxes()) {
            if (this.infobox != null) {
               this.infoBoxManager.removeInfoBox(this.infobox);
               this.infobox = null;
            }

            if (damage > 0) {
               BufferedImage image = this.getSplat(this.envenomed ? 1632 : 1360, damage);
               if (image != null) {
                  this.infobox = new PoisonInfobox(image, this);
                  this.infoBoxManager.addInfoBox(this.infobox);
               }
            }
         }

         this.checkHealthIcon();
      } else if (event.getVarpId() == 456) {
         this.checkHealthIcon();
      }

   }

   @Subscribe
   public void onConfigChanged(ConfigChanged event) {
      if (event.getGroup().equals("poison")) {
         if (!this.config.showInfoboxes() && this.infobox != null) {
            this.infoBoxManager.removeInfoBox(this.infobox);
            this.infobox = null;
         }

         if (this.config.changeHealthIcon()) {
            this.clientThread.invoke(this::checkHealthIcon);
         } else {
            this.clientThread.invoke(this::resetHealthIcon);
         }

      }
   }

   private static int nextDamage(int poisonValue) {
      int damage;
      if (poisonValue >= 1000000) {
         poisonValue -= 999997;
         damage = poisonValue * 2;
         if (damage > 20) {
            damage = 20;
         }
      } else {
         damage = (int)Math.ceil((double)((float)poisonValue / 5.0F));
      }

      return damage;
   }

   private BufferedImage getSplat(int id, int damage) {
      BufferedImage rawSplat = this.spriteManager.getSprite(id, 0);
      if (rawSplat == null) {
         return null;
      } else {
         BufferedImage splat = new BufferedImage(rawSplat.getColorModel(), rawSplat.copyData((WritableRaster)null), rawSplat.getColorModel().isAlphaPremultiplied(), (Hashtable)null);
         Graphics g = splat.getGraphics();
         g.setFont(FontManager.getRunescapeSmallFont());
         FontMetrics metrics = g.getFontMetrics();
         String text = String.valueOf(damage);
         int x = (splat.getWidth() - metrics.stringWidth(text)) / 2;
         int y = (splat.getHeight() - metrics.getHeight()) / 2 + metrics.getAscent();
         g.setColor(Color.BLACK);
         g.drawString(String.valueOf(damage), x + 1, y + 1);
         g.setColor(Color.WHITE);
         g.drawString(String.valueOf(damage), x, y);
         return splat;
      }
   }

   private static String getFormattedTime(Instant endTime) {
      Duration timeLeft = Duration.between(Instant.now(), endTime);
      int seconds = (int)(timeLeft.toMillis() / 1000L);
      int minutes = seconds / 60;
      int secs = seconds % 60;
      return String.format("%d:%02d", minutes, secs);
   }

   String createTooltip() {
      String line1 = MessageFormat.format("Next {0} damage: {1}</br>Time until damage: {2}", this.envenomed ? "venom" : "poison", ColorUtil.wrapWithColorTag(String.valueOf(this.lastDamage), Color.RED), getFormattedTime(this.nextPoisonTick));
      String line2 = this.envenomed ? "" : MessageFormat.format("</br>Time until cure: {0}", getFormattedTime(this.poisonNaturalCure));
      return line1 + line2;
   }

   private void checkHealthIcon() {
      if (this.config.changeHealthIcon()) {
         int poison = this.client.getVarpValue(102);
         BufferedImage newHeart;
         if (poison >= 1000000) {
            newHeart = HEART_VENOM;
         } else if (poison > 0) {
            newHeart = HEART_POISON;
         } else {
            if (this.client.getVarpValue(456) <= 0) {
               this.resetHealthIcon();
               return;
            }

            newHeart = HEART_DISEASE;
         }

         if (newHeart != this.heart) {
            this.heart = newHeart;
            this.client.getWidgetSpriteCache().reset();
            this.client.getSpriteOverrides().put(1067, ImageUtil.getImageSpritePixels(this.heart, this.client));
         }

      }
   }

   private void resetHealthIcon() {
      if (this.heart != null) {
         this.client.getWidgetSpriteCache().reset();
         this.client.getSpriteOverrides().remove(1067);
         this.heart = null;
      }
   }

   public int getLastDamage() {
      return this.lastDamage;
   }
}
