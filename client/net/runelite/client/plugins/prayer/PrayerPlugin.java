package net.runelite.client.plugins.prayer;

import com.google.inject.Provides;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.function.Consumer;
import javax.annotation.Nullable;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.Item;
import net.runelite.api.ItemContainer;
import net.runelite.api.Prayer;
import net.runelite.api.Skill;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.ItemContainerChanged;
import net.runelite.api.widgets.Widget;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.EventBus;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.game.ItemManager;
import net.runelite.client.game.ItemStats;
import net.runelite.client.game.SpriteManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;
import net.runelite.client.ui.overlay.infobox.InfoBoxManager;

@PluginDescriptor(
   name = "Prayer",
   description = "Show various information related to prayer",
   tags = {"combat", "flicking", "overlay"}
)
public class PrayerPlugin extends Plugin {
   private final PrayerCounter[] prayerCounter = new PrayerCounter[PrayerType.values().length];
   private Instant startOfLastTick = Instant.now();
   private boolean prayersActive = false;
   private int prayerBonus;
   @Inject
   private Client client;
   @Inject
   private InfoBoxManager infoBoxManager;
   @Inject
   private SpriteManager spriteManager;
   @Inject
   private OverlayManager overlayManager;
   @Inject
   private PrayerFlickOverlay flickOverlay;
   @Inject
   private PrayerDoseOverlay doseOverlay;
   @Inject
   private PrayerBarOverlay barOverlay;
   @Inject
   private PrayerConfig config;
   @Inject
   private ItemManager itemManager;
   @Inject
   private PrayerReorder prayerReorder;
   @Inject
   private EventBus eventBus;

   @Provides
   PrayerConfig provideConfig(ConfigManager configManager) {
      return (PrayerConfig)configManager.getConfig(PrayerConfig.class);
   }

   protected void startUp() {
      this.overlayManager.add(this.flickOverlay);
      this.overlayManager.add(this.doseOverlay);
      this.overlayManager.add(this.barOverlay);
      this.prayerReorder.startUp();
      this.eventBus.register(this.prayerReorder);
   }

   protected void shutDown() {
      this.overlayManager.remove(this.flickOverlay);
      this.overlayManager.remove(this.doseOverlay);
      this.overlayManager.remove(this.barOverlay);
      this.removeIndicators();
      this.prayerReorder.shutDown();
      this.eventBus.unregister((Object)this.prayerReorder);
   }

   public void resetConfiguration() {
      this.prayerReorder.reset();
   }

   @Subscribe
   private void onConfigChanged(ConfigChanged event) {
      if (event.getGroup().equals("prayer")) {
         if (!this.config.prayerIndicator()) {
            this.removeIndicators();
         } else if (!this.config.prayerIndicatorOverheads()) {
            this.removeOverheadsIndicators();
         }
      }

   }

   @Subscribe
   public void onItemContainerChanged(ItemContainerChanged event) {
      int id = event.getContainerId();
      if (id == 93) {
         this.updatePotionBonus(event.getItemContainer(), this.client.getItemContainer(94));
      } else if (id == 94) {
         this.prayerBonus = this.totalPrayerBonus(event.getItemContainer().getItems());
      }

   }

   @Subscribe
   public void onGameTick(GameTick tick) {
      this.prayersActive = this.isAnyPrayerActive();
      if (!this.config.prayerFlickLocation().equals(PrayerFlickLocation.NONE)) {
         this.startOfLastTick = Instant.now();
      }

      if (this.config.showPrayerDoseIndicator()) {
         this.doseOverlay.onTick();
      }

      if (this.config.showPrayerBar()) {
         this.barOverlay.onTick();
      }

      if (this.config.replaceOrbText() && this.isAnyPrayerActive()) {
         this.setPrayerOrbText(this.getEstimatedTimeRemaining(true));
      }

      if (this.config.prayerIndicator()) {
         PrayerType[] var2 = PrayerType.values();
         int var3 = var2.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            PrayerType prayerType = var2[var4];
            int ord = prayerType.ordinal();
            if (prayerType.isActive(this.client)) {
               if ((!prayerType.isOverhead() || this.config.prayerIndicatorOverheads()) && this.prayerCounter[ord] == null) {
                  PrayerCounter counter = this.prayerCounter[ord] = new PrayerCounter(this, prayerType);
                  SpriteManager var10000 = this.spriteManager;
                  int var10001 = prayerType.getSpriteID();
                  Objects.requireNonNull(counter);
                  var10000.getSpriteAsync(var10001, 0, (Consumer)(counter::setImage));
                  this.infoBoxManager.addInfoBox(counter);
               }
            } else if (this.prayerCounter[ord] != null) {
               this.infoBoxManager.removeInfoBox(this.prayerCounter[ord]);
               this.prayerCounter[ord] = null;
            }
         }

      }
   }

   private int totalPrayerBonus(Item[] items) {
      int total = 0;
      Item[] var3 = items;
      int var4 = items.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         Item item = var3[var5];
         ItemStats is = this.itemManager.getItemStats(item.getId());
         if (is != null && is.getEquipment() != null) {
            total += is.getEquipment().getPrayer();
         }
      }

      return total;
   }

   private void updatePotionBonus(ItemContainer inventory, @Nullable ItemContainer equip) {
      boolean hasPrayerPotion = false;
      boolean hasSuperRestore = false;
      boolean hasSanfew = false;
      boolean hasWrench = false;
      Item[] var7 = inventory.getItems();
      int restored = var7.length;

      int var9;
      Item item;
      PrayerRestoreType type;
      for(var9 = 0; var9 < restored; ++var9) {
         item = var7[var9];
         type = PrayerRestoreType.getType(item.getId());
         if (type != null) {
            switch (type) {
               case PRAYERPOT:
                  hasPrayerPotion = true;
                  break;
               case RESTOREPOT:
                  hasSuperRestore = true;
                  break;
               case SANFEWPOT:
                  hasSanfew = true;
                  break;
               case HOLYWRENCH:
                  hasWrench = true;
            }
         }
      }

      if (!hasWrench && equip != null) {
         var7 = equip.getItems();
         restored = var7.length;

         for(var9 = 0; var9 < restored; ++var9) {
            item = var7[var9];
            type = PrayerRestoreType.getType(item.getId());
            if (type == PrayerRestoreType.HOLYWRENCH) {
               hasWrench = true;
               break;
            }
         }
      }

      int prayerLevel = this.client.getRealSkillLevel(Skill.PRAYER);
      restored = 0;
      if (hasSanfew) {
         restored = Math.max(restored, 4 + (int)Math.floor((double)prayerLevel * (hasWrench ? 0.32 : 0.3)));
      }

      if (hasSuperRestore) {
         restored = Math.max(restored, 8 + (int)Math.floor((double)prayerLevel * (hasWrench ? 0.27 : 0.25)));
      }

      if (hasPrayerPotion) {
         restored = Math.max(restored, 7 + (int)Math.floor((double)prayerLevel * (hasWrench ? 0.27 : 0.25)));
      }

      this.doseOverlay.setRestoreAmount(restored);
   }

   double getTickProgress() {
      long timeSinceLastTick = Duration.between(this.startOfLastTick, Instant.now()).toMillis();
      float tickProgress = (float)(timeSinceLastTick % 600L) / 600.0F;
      return (double)tickProgress * Math.PI;
   }

   private boolean isAnyPrayerActive() {
      Prayer[] var1 = Prayer.values();
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         Prayer pray = var1[var3];
         if (this.client.isPrayerActive(pray)) {
            return true;
         }
      }

      return false;
   }

   private void removeIndicators() {
      this.infoBoxManager.removeIf((entry) -> {
         return entry instanceof PrayerCounter;
      });
   }

   private void removeOverheadsIndicators() {
      this.infoBoxManager.removeIf((entry) -> {
         return entry instanceof PrayerCounter && ((PrayerCounter)entry).getPrayerType().isOverhead();
      });
   }

   private void setPrayerOrbText(String text) {
      Widget prayerOrbText = this.client.getWidget(10485781);
      if (prayerOrbText != null) {
         prayerOrbText.setText(text);
      }

      prayerOrbText = this.client.getWidget(58654739);
      if (prayerOrbText != null) {
         prayerOrbText.setText(text);
      }

   }

   private static int getDrainEffect(Client client) {
      int drainEffect = 0;
      PrayerType[] var2 = PrayerType.values();
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         PrayerType prayerType = var2[var4];
         if (prayerType.isActive(client)) {
            drainEffect += prayerType.getDrainEffect();
         }
      }

      return drainEffect;
   }

   String getEstimatedTimeRemaining(boolean formatForOrb) {
      int drainEffect = getDrainEffect(this.client);
      if (drainEffect == 0) {
         return "N/A";
      } else {
         int drainResistance = 2 * this.prayerBonus + 60;
         double secondsPerPoint = 0.6 * ((double)drainResistance / (double)drainEffect);
         int currentPrayer = this.client.getBoostedSkillLevel(Skill.PRAYER);
         double secondsLeft = (double)currentPrayer * secondsPerPoint;
         LocalTime timeLeft = LocalTime.ofSecondOfDay((long)secondsLeft);
         if (!formatForOrb || timeLeft.getHour() <= 0 && timeLeft.getMinute() <= 9) {
            return timeLeft.getHour() > 0 ? timeLeft.format(DateTimeFormatter.ofPattern("H:mm:ss")) : timeLeft.format(DateTimeFormatter.ofPattern("m:ss"));
         } else {
            long minutes = Duration.ofSeconds((long)secondsLeft).toMinutes();
            return String.format("%dm", minutes);
         }
      }
   }

   boolean isPrayersActive() {
      return this.prayersActive;
   }

   int getPrayerBonus() {
      return this.prayerBonus;
   }

   void setPrayerBonus(int prayerBonus) {
      this.prayerBonus = prayerBonus;
   }
}
