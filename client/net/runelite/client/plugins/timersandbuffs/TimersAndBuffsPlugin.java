package net.runelite.client.plugins.timersandbuffs;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.ImmutableSet;
import com.google.inject.Provides;
import custom.UpdateTimerScript;
import java.time.Duration;
import java.time.Instant;
import java.util.EnumMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.IntPredicate;
import java.util.function.IntUnaryOperator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.inject.Inject;
import net.runelite.api.Actor;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.EquipmentInventorySlot;
import net.runelite.api.GameState;
import net.runelite.api.Item;
import net.runelite.api.ItemContainer;
import net.runelite.api.NPC;
import net.runelite.api.Player;
import net.runelite.api.Skill;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.ActorDeath;
import net.runelite.api.events.ChatMessage;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.GraphicChanged;
import net.runelite.api.events.ItemContainerChanged;
import net.runelite.api.events.NpcChanged;
import net.runelite.api.events.NpcDespawned;
import net.runelite.api.events.NpcSpawned;
import net.runelite.api.events.VarbitChanged;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.game.ItemManager;
import net.runelite.client.game.ItemVariationMapping;
import net.runelite.client.game.SpriteManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.infobox.InfoBox;
import net.runelite.client.ui.overlay.infobox.InfoBoxManager;
import net.runelite.client.util.RSTimeUnit;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@PluginDescriptor(
   name = "Timers & Buffs",
   configName = "TimersPlugin",
   description = "Show various timers and buffs in an infobox",
   tags = {"combat", "items", "magic", "potions", "prayer", "overlay", "abyssal", "sire", "inferno", "fight", "caves", "cape", "timer", "tzhaar", "thieving", "pickpocket", "hunter", "impling", "puro", "buff"}
)
public class TimersAndBuffsPlugin extends Plugin {
   private static final Logger log = LoggerFactory.getLogger(TimersAndBuffsPlugin.class);
   private static final String ABYSSAL_SIRE_STUN_MESSAGE = "The Sire has been disorientated temporarily.";
   private static final String CANNON_BASE_MESSAGE = "You place the cannon base on the ground.";
   private static final String CANNON_STAND_MESSAGE = "You add the stand.";
   private static final String CANNON_BARRELS_MESSAGE = "You add the barrels.";
   private static final String CANNON_FURNACE_MESSAGE = "You add the furnace.";
   private static final String CANNON_PICKUP_MESSAGE = "You pick up the cannon. It's really heavy.";
   private static final String CANNON_REPAIR_MESSAGE = "You repair your cannon, restoring it to working order.";
   private static final String CANNON_DESTROYED_MESSAGE = "Your cannon has been destroyed!";
   private static final String CANNON_BROKEN_MESSAGE = "<col=ef1020>Your cannon has broken!";
   private static final String FROZEN_MESSAGE = "<col=ef1020>You have been frozen!</col>";
   private static final String STAFF_OF_THE_DEAD_SPEC_EXPIRED_MESSAGE = "Your protection fades away";
   private static final String STAFF_OF_THE_DEAD_SPEC_MESSAGE = "Spirits of deceased evildoers offer you their protection";
   private static final String PRAYER_ENHANCE_EXPIRED = "<col=ff0000>Your prayer enhance effect has worn off.</col>";
   private static final String SHADOW_VEIL_MESSAGE = ">Your thieving abilities have been enhanced.</col>";
   private static final String RESURRECT_THRALL_MESSAGE_START = ">You resurrect a ";
   private static final String RESURRECT_THRALL_MESSAGE_END = " thrall.</col>";
   private static final String WARD_OF_ARCEUUS_MESSAGE = ">Your defence against Arceuus magic has been strengthened.</col>";
   private static final String MARK_OF_DARKNESS_MESSAGE = "You have placed a Mark of Darkness upon yourself.</col>";
   private static final String PICKPOCKET_FAILURE_MESSAGE = "You fail to pick ";
   private static final String DODGY_NECKLACE_PROTECTION_MESSAGE = "Your dodgy necklace protects you.";
   private static final String SHADOW_VEIL_PROTECTION_MESSAGE = "Your attempt to steal goes unnoticed.";
   private static final String SILK_DRESSING_MESSAGE = "You quickly apply the dressing to your wounds.";
   private static final String BLESSED_CRYSTAL_SCARAB_MESSAGE = "You crack the crystal in your hand.";
   private static final String LIQUID_ADRENALINE_MESSAGE = "You drink some of the potion, reducing the energy cost of your special attacks.</col>";
   private static final Set<Integer> STAVES_OF_THE_DEAD = (new ImmutableSet.Builder()).addAll(ItemVariationMapping.getVariations(11791)).addAll(ItemVariationMapping.getVariations(12902)).add(22296).add(24144).build();
   private static final int VENOM_VALUE_CUTOFF = -38;
   private static final int POISON_TICK_LENGTH = 30;
   private static final int OVERLOAD_TICK_LENGTH = 25;
   private static final int ANTIFIRE_TICK_LENGTH = 30;
   private static final int SUPERANTIFIRE_TICK_LENGTH = 20;
   static final int FIGHT_CAVES_REGION_ID = 9551;
   static final int INFERNO_REGION_ID = 9043;
   private static final Pattern TZHAAR_WAVE_MESSAGE = Pattern.compile("Wave: (\\d+)");
   private static final Pattern TZHAAR_PAUSED_MESSAGE = Pattern.compile("The (?:Inferno|Fight Cave) has been paused. You may now log out.");
   private TimerTimer freezeTimer;
   private int freezeTime = -1;
   private final Map<GameTimer, TimerTimer> varTimers = new EnumMap(GameTimer.class);
   private int nextPoisonTick;
   private int nextOverloadRefreshTick;
   private int nextAntifireTick;
   private int nextSuperAntifireTick;
   private WorldPoint lastPoint;
   private ElapsedTimer tzhaarTimer;
   private int lastDeathChargeVarb;
   private final Map<GameCounter, BuffCounter> varCounters = new EnumMap(GameCounter.class);
   private static final int ECLIPSE_MOON_REGION_ID = 6038;
   @Inject
   private ItemManager itemManager;
   @Inject
   private SpriteManager spriteManager;
   @Inject
   private Client client;
   @Inject
   private TimersAndBuffsConfig config;
   @Inject
   private InfoBoxManager infoBoxManager;

   @Provides
   TimersAndBuffsConfig getConfig(ConfigManager configManager) {
      return (TimersAndBuffsConfig)configManager.getConfig(TimersAndBuffsConfig.class);
   }

   @Subscribe
   public void onUpdateTimerScript(UpdateTimerScript packet) {
      Duration duration = Duration.of((long)packet.getTicksRemaining(), RSTimeUnit.GAME_TICKS);
      int ticksRemaining = packet.getTicksRemaining();
      if (ticksRemaining == 0) {
         this.infoBoxManager.getInfoBoxes().forEach((infoBox) -> {
            if (infoBox instanceof CustomServerIndicator) {
               CustomServerIndicator indicator = (CustomServerIndicator)infoBox;
               if (indicator.getIndicatorId() == packet.getTimerId()) {
                  this.infoBoxManager.removeInfoBox(indicator);
               }
            } else if (infoBox instanceof CustomServerTimer) {
               CustomServerTimer timer = (CustomServerTimer)infoBox;
               if (timer.getTimerId() == packet.getTimerId()) {
                  this.infoBoxManager.removeInfoBox(timer);
               }
            }

         });
      } else if (ticksRemaining < 0) {
         this.createCustomServerIndicator(packet);
      } else {
         AtomicBoolean found = new AtomicBoolean(false);
         this.infoBoxManager.getInfoBoxes().forEach((infoBox) -> {
            if (infoBox instanceof CustomServerTimer) {
               CustomServerTimer timer = (CustomServerTimer)infoBox;
               if (timer.getTimerId() == packet.getTimerId() && timer.getDescription().equals(packet.getDescription())) {
                  found.set(true);
                  timer.updateDuration(duration);
               } else if (timer.getTimerId() == packet.getTimerId()) {
                  this.infoBoxManager.removeInfoBox(timer);
               }
            }

         });
         if (!found.get()) {
            this.createCustomServerTimer(packet, duration);
         }
      }

   }

   private void createCustomServerTimer(UpdateTimerScript packet, Duration duration) {
      GameTimerImageType imageType = packet.isSpriteId() ? GameTimerImageType.SPRITE : GameTimerImageType.ITEM;
      CustomServerTimer t = new CustomServerTimer(packet.getTimerId(), imageType, packet.getDescription(), duration, this);
      switch (imageType) {
         case SPRITE:
            this.spriteManager.getSpriteAsync(packet.getTimerId(), 0, (InfoBox)t);
            break;
         case ITEM:
            t.setImage(this.itemManager.getImage(packet.getTimerId()));
      }

      t.setTooltip(packet.getDescription());
      this.infoBoxManager.addInfoBox(t);
   }

   private void createCustomServerIndicator(UpdateTimerScript packet) {
      this.infoBoxManager.getInfoBoxes().forEach((infoBox) -> {
         if (infoBox instanceof CustomServerIndicator) {
            CustomServerIndicator indicator = (CustomServerIndicator)infoBox;
            if (indicator.getIndicatorId() == packet.getTimerId()) {
               this.infoBoxManager.removeInfoBox(indicator);
            }
         }

      });
      GameTimerImageType imageType = packet.isSpriteId() ? GameTimerImageType.SPRITE : GameTimerImageType.ITEM;
      CustomServerIndicator indicator = new CustomServerIndicator(packet.getTimerId(), imageType, packet.getDescription(), this);
      switch (imageType) {
         case SPRITE:
            this.spriteManager.getSpriteAsync(packet.getTimerId(), 0, (InfoBox)indicator);
            break;
         case ITEM:
            indicator.setImage(this.itemManager.getImage(packet.getTimerId()));
      }

      indicator.setTooltip(packet.getDescription());
      this.infoBoxManager.addInfoBox(indicator);
   }

   public void startUp() {
      if (this.config.showHomeMinigameTeleports() && this.client.getGameState() == GameState.LOGGED_IN) {
         this.checkTeleport(892);
         this.checkTeleport(888);
      }

   }

   protected void shutDown() throws Exception {
      this.infoBoxManager.removeIf((t) -> {
         return t instanceof TimerTimer;
      });
      this.lastPoint = null;
      this.nextPoisonTick = 0;
      this.nextOverloadRefreshTick = 0;
      this.nextAntifireTick = 0;
      this.nextSuperAntifireTick = 0;
      this.lastDeathChargeVarb = 0;
      this.removeTzhaarTimer();
      this.varTimers.clear();
      this.infoBoxManager.removeIf((buffCounter) -> {
         return buffCounter instanceof BuffCounter;
      });
      this.varCounters.clear();
   }

   @Subscribe
   public void onVarbitChanged(VarbitChanged event) {
      if (event.getVarbitId() == 5432) {
         this.removeVarTimer(GameTimer.OVERLOAD_RAID);
         this.removeGameTimer(GameTimer.PRAYER_ENHANCE);
      }

      if (event.getVarbitId() == 2451 && this.config.showVengeance()) {
         if (event.getValue() == 1) {
            this.createGameTimer(GameTimer.VENGEANCE);
         } else {
            this.removeGameTimer(GameTimer.VENGEANCE);
         }
      }

      if (event.getVarbitId() == 3617 && this.config.showSpellbookSwap()) {
         if (event.getValue() == 1) {
            this.createGameTimer(GameTimer.SPELLBOOK_SWAP);
         } else {
            this.removeGameTimer(GameTimer.SPELLBOOK_SWAP);
         }
      }

      if (event.getVarbitId() == 925 && this.config.showHealGroup()) {
         if (event.getValue() == 1) {
            this.createGameTimer(GameTimer.HEAL_GROUP);
         } else {
            this.removeGameTimer(GameTimer.HEAL_GROUP);
         }
      }

      if (event.getVarbitId() == 12138 && this.config.showArceuusCooldown()) {
         if (event.getValue() == 1) {
            this.createGameTimer(GameTimer.DEATH_CHARGE_COOLDOWN);
         } else {
            this.removeGameTimer(GameTimer.DEATH_CHARGE_COOLDOWN);
         }
      }

      if (event.getVarbitId() == 12288 && this.config.showArceuusCooldown()) {
         if (event.getValue() == 1) {
            this.createGameTimer(GameTimer.CORRUPTION_COOLDOWN);
         } else {
            this.removeGameTimer(GameTimer.CORRUPTION_COOLDOWN);
         }
      }

      if (event.getVarbitId() == 12290 && this.config.showArceuusCooldown()) {
         if (event.getValue() == 1) {
            this.createGameTimer(GameTimer.RESURRECT_THRALL_COOLDOWN);
         } else {
            this.removeGameTimer(GameTimer.RESURRECT_THRALL_COOLDOWN);
         }
      }

      if (event.getVarbitId() == 12291 && this.config.showArceuusCooldown()) {
         if (event.getValue() == 1) {
            this.createGameTimer(GameTimer.SHADOW_VEIL_COOLDOWN);
         } else {
            this.removeGameTimer(GameTimer.SHADOW_VEIL_COOLDOWN);
         }
      }

      if (event.getVarbitId() == 12293 && this.config.showArceuusCooldown()) {
         if (event.getValue() == 1) {
            this.createGameTimer(GameTimer.WARD_OF_ARCEUUS_COOLDOWN);
         } else {
            this.removeGameTimer(GameTimer.WARD_OF_ARCEUUS_COOLDOWN);
         }
      }

      if (event.getVarbitId() == 2450 && this.config.showVengeanceActive()) {
         this.updateVarCounter(GameCounter.VENGEANCE_ACTIVE, event.getValue());
      }

      int moonlightValue;
      if (event.getVarbitId() == 12411 && this.config.showArceuus()) {
         moonlightValue = event.getValue();
         switch (moonlightValue) {
            case 0:
               this.removeGameTimer(GameTimer.DEATH_CHARGE);
               break;
            case 1:
               if (this.lastDeathChargeVarb == 0) {
                  this.createGameTimer(GameTimer.DEATH_CHARGE);
               }
               break;
            case 2:
               this.createGameTimer(GameTimer.DEATH_CHARGE);
         }

         this.lastDeathChargeVarb = moonlightValue;
      }

      if (event.getVarbitId() == 12413 && event.getValue() == 0 && this.config.showArceuus()) {
         this.removeGameTimer(GameTimer.RESURRECT_THRALL);
      }

      if (event.getVarbitId() == 12414 && event.getValue() == 0 && this.config.showArceuus()) {
         this.removeGameTimer(GameTimer.SHADOW_VEIL);
      }

      int tickCount;
      if (event.getVarpId() == 102 && this.config.showAntiPoison()) {
         moonlightValue = event.getValue();
         tickCount = this.client.getTickCount();
         if (moonlightValue == 0) {
            this.nextPoisonTick = -1;
         } else if (this.nextPoisonTick - tickCount <= 0) {
            this.nextPoisonTick = tickCount + 30;
         }

         this.updateVarTimer(GameTimer.ANTIPOISON, event.getValue(), (i) -> {
            return i >= 0 || i < -38;
         }, (i) -> {
            return this.nextPoisonTick - tickCount + Math.abs((i + 1) * 30);
         });
         this.updateVarTimer(GameTimer.ANTIVENOM, event.getValue(), (i) -> {
            return i >= -38;
         }, (i) -> {
            return this.nextPoisonTick - tickCount + Math.abs((i + 1 - -38) * 30);
         });
      }

      if ((event.getVarbitId() == 3955 || event.getVarbitId() == 5418 || event.getVarbitId() == 10954) && this.config.showOverload()) {
         moonlightValue = event.getValue();
         tickCount = this.client.getTickCount();
         if (moonlightValue <= 0) {
            this.nextOverloadRefreshTick = -1;
         } else if (this.nextOverloadRefreshTick - tickCount <= 0) {
            this.nextOverloadRefreshTick = tickCount + 25;
         }

         GameTimer overloadTimer;
         if (event.getVarbitId() == 10954) {
            overloadTimer = GameTimer.BLIGHTED_OVERLOAD;
         } else {
            overloadTimer = this.client.getVarbitValue(5432) == 1 ? GameTimer.OVERLOAD_RAID : GameTimer.OVERLOAD;
         }

         this.updateVarTimer(overloadTimer, moonlightValue, (i) -> {
            return this.nextOverloadRefreshTick - tickCount + (i - 1) * 25;
         });
      }

      if (event.getVarbitId() == 4163 && this.config.showTeleblock()) {
         this.updateVarTimer(GameTimer.TELEBLOCK, event.getValue() - 100, (i) -> {
            return i <= 0;
         }, IntUnaryOperator.identity());
      }

      if (event.getVarpId() == 272 && this.config.showCharge()) {
         this.updateVarTimer(GameTimer.CHARGE, event.getValue(), (i) -> {
            return i * 2;
         });
      }

      if (event.getVarbitId() == 5361 && this.config.showImbuedHeart()) {
         this.updateVarTimer(GameTimer.IMBUEDHEART, event.getValue(), (i) -> {
            return i * 10;
         });
      }

      if (event.getVarbitId() == 6539 && this.config.showDFSSpecial()) {
         this.updateVarTimer(GameTimer.DRAGON_FIRE_SHIELD, event.getValue(), (i) -> {
            return i * 8;
         });
      }

      if (event.getVarpId() == 892 && this.config.showHomeMinigameTeleports()) {
         this.checkTeleport(892);
      }

      if (event.getVarpId() == 888 && this.config.showHomeMinigameTeleports()) {
         this.checkTeleport(888);
      }

      if (event.getVarbitId() == 25 || event.getVarbitId() == 24) {
         moonlightValue = this.client.getVarbitValue(25);
         tickCount = this.client.getVarbitValue(24);
         if (moonlightValue == 1 && this.config.showStamina()) {
            this.updateVarTimer(GameTimer.STAMINA, tickCount, (i) -> {
               return i * 10;
            });
         }
      }

      if (event.getVarbitId() == 3981 && this.config.showAntiFire()) {
         moonlightValue = event.getValue();
         tickCount = this.client.getTickCount();
         if (moonlightValue == 0) {
            this.nextAntifireTick = -1;
         } else if (this.nextAntifireTick - tickCount <= 0) {
            this.nextAntifireTick = tickCount + 30;
         }

         this.updateVarTimer(GameTimer.ANTIFIRE, moonlightValue, (i) -> {
            return this.nextAntifireTick - tickCount + (i - 1) * 30;
         });
      }

      if (event.getVarbitId() == 6101 && this.config.showAntiFire()) {
         moonlightValue = event.getValue();
         tickCount = this.client.getTickCount();
         if (moonlightValue == 0) {
            this.nextSuperAntifireTick = -1;
         } else if (this.nextSuperAntifireTick - tickCount <= 0) {
            this.nextSuperAntifireTick = tickCount + 20;
         }

         this.updateVarTimer(GameTimer.SUPERANTIFIRE, event.getValue(), (i) -> {
            return this.nextSuperAntifireTick - tickCount + (i - 1) * 20;
         });
      }

      if (event.getVarbitId() == 5438 && this.config.showMagicImbue()) {
         this.updateVarTimer(GameTimer.MAGICIMBUE, event.getValue(), (i) -> {
            return i * 10;
         });
      }

      if (event.getVarbitId() == 8429 && this.config.showDivine()) {
         if (this.client.getVarbitValue(13663) > event.getValue()) {
            return;
         }

         this.updateVarTimer(GameTimer.DIVINE_SUPER_ATTACK, event.getValue(), IntUnaryOperator.identity());
      }

      if (event.getVarbitId() == 8430 && this.config.showDivine()) {
         if (this.client.getVarbitValue(13663) > event.getValue()) {
            return;
         }

         this.updateVarTimer(GameTimer.DIVINE_SUPER_STRENGTH, event.getValue(), IntUnaryOperator.identity());
      }

      if (event.getVarbitId() == 8431 && this.config.showDivine()) {
         if (this.client.getVarbitValue(13663) > event.getValue() || this.client.getVarbitValue(13664) > event.getValue() || this.client.getVarbitValue(13665) > event.getValue() || this.client.getVarbitValue(10029) >= event.getValue()) {
            return;
         }

         if (this.client.getVarbitValue(10029) < event.getValue()) {
            this.removeVarTimer(GameTimer.MOONLIGHT_POTION);
         }

         this.updateVarTimer(GameTimer.DIVINE_SUPER_DEFENCE, event.getValue(), IntUnaryOperator.identity());
      }

      if (event.getVarbitId() == 8432 && this.config.showDivine()) {
         if (this.client.getVarbitValue(13664) > event.getValue()) {
            return;
         }

         this.updateVarTimer(GameTimer.DIVINE_RANGING, event.getValue(), IntUnaryOperator.identity());
      }

      if (event.getVarbitId() == 8433 && this.config.showDivine()) {
         if (this.client.getVarbitValue(13665) > event.getValue()) {
            return;
         }

         this.updateVarTimer(GameTimer.DIVINE_MAGIC, event.getValue(), IntUnaryOperator.identity());
      }

      if (event.getVarbitId() == 13663 && this.config.showDivine()) {
         if (this.client.getVarbitValue(8429) == event.getValue()) {
            this.removeVarTimer(GameTimer.DIVINE_SUPER_ATTACK);
         }

         if (this.client.getVarbitValue(8430) == event.getValue()) {
            this.removeVarTimer(GameTimer.DIVINE_SUPER_STRENGTH);
         }

         if (this.client.getVarbitValue(8431) == event.getValue()) {
            this.removeVarTimer(GameTimer.DIVINE_SUPER_DEFENCE);
         }

         this.updateVarTimer(GameTimer.DIVINE_SUPER_COMBAT, event.getValue(), IntUnaryOperator.identity());
      }

      if (event.getVarbitId() == 13664 && this.config.showDivine()) {
         if (this.client.getVarbitValue(8432) == event.getValue()) {
            this.removeVarTimer(GameTimer.DIVINE_RANGING);
         }

         if (this.client.getVarbitValue(8431) == event.getValue()) {
            this.removeVarTimer(GameTimer.DIVINE_SUPER_DEFENCE);
         }

         this.updateVarTimer(GameTimer.DIVINE_BASTION, event.getValue(), IntUnaryOperator.identity());
      }

      if (event.getVarbitId() == 13665 && this.config.showDivine()) {
         if (this.client.getVarbitValue(8433) == event.getValue()) {
            this.removeVarTimer(GameTimer.DIVINE_MAGIC);
         }

         if (this.client.getVarbitValue(8431) == event.getValue()) {
            this.removeVarTimer(GameTimer.DIVINE_SUPER_DEFENCE);
         }

         this.updateVarTimer(GameTimer.DIVINE_BATTLEMAGE, event.getValue(), IntUnaryOperator.identity());
      }

      if (event.getVarbitId() == 14344 && this.config.showOverload()) {
         this.updateVarTimer(GameTimer.SMELLING_SALTS, event.getValue(), (i) -> {
            return i * 25;
         });
      }

      if (event.getVarbitId() == 14448 && this.config.showMenaphiteRemedy()) {
         this.updateVarTimer(GameTimer.MENAPHITE_REMEDY, event.getValue(), (i) -> {
            return i * 25;
         });
      }

      if (event.getVarbitId() == 14361 && event.getValue() == 0 && this.config.showLiquidAdrenaline()) {
         this.removeGameTimer(GameTimer.LIQUID_ADRENALINE);
      }

      if (event.getVarbitId() == 11765 && this.config.showFarmersAffinity()) {
         this.updateVarTimer(GameTimer.FARMERS_AFFINITY, event.getValue(), (i) -> {
            return i * 20;
         });
      }

      if (event.getVarbitId() == 4099 && this.config.showGodWarsAltar()) {
         this.updateVarTimer(GameTimer.GOD_WARS_ALTAR, event.getValue(), (i) -> {
            return i * 100;
         });
      }

      if (event.getVarbitId() == 9853 && this.config.showCurseOfTheMoons()) {
         moonlightValue = WorldPoint.fromLocal(this.client, this.client.getLocalPlayer().getLocalLocation()).getRegionID();
         if (moonlightValue == 6038) {
            this.updateVarCounter(GameCounter.CURSE_OF_THE_MOONS_ECLIPSE, event.getValue());
         } else {
            this.updateVarCounter(GameCounter.CURSE_OF_THE_MOONS_BLUE, event.getValue());
         }
      }

      if (event.getVarbitId() == 9801 && this.config.showColosseumDoom()) {
         this.updateVarCounter(GameCounter.COLOSSEUM_DOOM, event.getValue());
      }

      if (event.getVarbitId() == 10029 && this.config.showMoonlightPotion()) {
         moonlightValue = event.getValue();
         if (this.client.getVarbitValue(8431) == moonlightValue + 1) {
            ++moonlightValue;
         }

         this.updateVarTimer(GameTimer.MOONLIGHT_POTION, moonlightValue, IntUnaryOperator.identity());
      }

      if (event.getVarbitId() == 10934 && this.config.showTormentedDemonBuffs()) {
         this.updateVarCounter(GameCounter.STONE_OF_JAS_EMPOWERMENT, event.getValue());
      }

      if (event.getVarbitId() == 10964 && this.config.showBurnDamageAccumulated()) {
         this.updateVarCounter(GameCounter.BURN_DAMAGE_ACCUMULATED, event.getValue());
      }

      if (event.getVarbitId() == 10965 && this.config.showBurnDamageNextHit()) {
         this.updateVarCounter(GameCounter.BURN_DAMAGE_NEXT_HIT, event.getValue());
      }

      if (event.getVarbitId() == 10944 && this.config.showTormentedDemonBuffs()) {
         this.updateVarTimer(GameTimer.SMOULDERING_HEART, event.getValue(), (i) -> {
            return i * 25;
         });
      }

      if (event.getVarbitId() == 10945 && this.config.showTormentedDemonBuffs()) {
         this.updateVarTimer(GameTimer.SMOULDERING_GLAND, event.getValue(), (i) -> {
            return i * 4;
         });
      }

      if (event.getVarbitId() == 11294 && this.config.showGoading()) {
         this.updateVarTimer(GameTimer.GOADING, event.getValue(), (i) -> {
            return i * 6;
         });
      }

      if (event.getVarbitId() == 11361 && this.config.showPrayerRegneration()) {
         this.updateVarTimer(GameTimer.PRAYER_REGENERATION, event.getValue(), (i) -> {
            return i * 12;
         });
      }

      if (event.getVarbitId() == 9581 && this.config.showScurriusFoodPile()) {
         this.updateVarTimer(GameTimer.SCURRIUS_FOOD_PILE, event.getValue(), (i) -> {
            return i * 100;
         });
      }

      if (event.getVarbitId() == 16270 && this.config.showSurge()) {
         this.updateVarTimer(GameTimer.SURGE_POTION, event.getValue(), (i) -> {
            return i * 10;
         });
      }

   }

   @Subscribe
   public void onConfigChanged(ConfigChanged event) {
      if (event.getGroup().equals("timers")) {
         if (!this.config.showHomeMinigameTeleports()) {
            this.removeGameTimer(GameTimer.HOME_TELEPORT);
            this.removeGameTimer(GameTimer.MINIGAME_TELEPORT);
         } else if (this.client.getGameState() == GameState.LOGGED_IN) {
            this.checkTeleport(892);
            this.checkTeleport(888);
         }

         if (!this.config.showAntiFire()) {
            this.removeVarTimer(GameTimer.ANTIFIRE);
            this.removeVarTimer(GameTimer.SUPERANTIFIRE);
         }

         if (!this.config.showStamina()) {
            this.removeVarTimer(GameTimer.STAMINA);
         }

         if (!this.config.showOverload()) {
            this.removeGameTimer(GameTimer.OVERLOAD);
            this.removeGameTimer(GameTimer.OVERLOAD_RAID);
            this.removeGameTimer(GameTimer.BLIGHTED_OVERLOAD);
            this.removeGameTimer(GameTimer.SMELLING_SALTS);
         }

         if (!this.config.showTormentedDemonBuffs()) {
            this.removeVarCounter(GameCounter.STONE_OF_JAS_EMPOWERMENT);
            this.removeGameTimer(GameTimer.SMOULDERING_HEART);
            this.removeGameTimer(GameTimer.SMOULDERING_GLAND);
         }

         if (!this.config.showPrayerEnhance()) {
            this.removeGameTimer(GameTimer.PRAYER_ENHANCE);
         }

         if (!this.config.showDivine()) {
            this.removeVarTimer(GameTimer.DIVINE_SUPER_ATTACK);
            this.removeVarTimer(GameTimer.DIVINE_SUPER_STRENGTH);
            this.removeVarTimer(GameTimer.DIVINE_SUPER_DEFENCE);
            this.removeVarTimer(GameTimer.DIVINE_RANGING);
            this.removeVarTimer(GameTimer.DIVINE_MAGIC);
            this.removeVarTimer(GameTimer.DIVINE_SUPER_COMBAT);
            this.removeVarTimer(GameTimer.DIVINE_BASTION);
            this.removeVarTimer(GameTimer.DIVINE_BATTLEMAGE);
         }

         if (!this.config.showCannon()) {
            this.removeGameTimer(GameTimer.CANNON);
            this.removeGameTimer(GameTimer.CANNON_REPAIR);
         }

         if (!this.config.showMagicImbue()) {
            this.removeVarTimer(GameTimer.MAGICIMBUE);
         }

         if (!this.config.showCharge()) {
            this.removeGameTimer(GameTimer.CHARGE);
         }

         if (!this.config.showImbuedHeart()) {
            this.removeVarTimer(GameTimer.IMBUEDHEART);
         }

         if (!this.config.showDFSSpecial()) {
            this.removeVarTimer(GameTimer.DRAGON_FIRE_SHIELD);
         }

         if (!this.config.showStaffOfTheDead()) {
            this.removeGameTimer(GameTimer.STAFF_OF_THE_DEAD);
         }

         if (!this.config.showVengeance()) {
            this.removeGameTimer(GameTimer.VENGEANCE);
         }

         if (!this.config.showHealGroup()) {
            this.removeGameTimer(GameTimer.HEAL_GROUP);
         }

         if (!this.config.showVengeanceActive()) {
            this.removeVarCounter(GameCounter.VENGEANCE_ACTIVE);
         }

         if (!this.config.showTeleblock()) {
            this.removeGameTimer(GameTimer.TELEBLOCK);
         }

         if (!this.config.showFreezes()) {
            this.removeGameTimer(GameTimer.BIND);
            this.removeGameTimer(GameTimer.SNARE);
            this.removeGameTimer(GameTimer.ENTANGLE);
            this.removeGameTimer(GameTimer.ICERUSH);
            this.removeGameTimer(GameTimer.ICEBURST);
            this.removeGameTimer(GameTimer.ICEBLITZ);
            this.removeGameTimer(GameTimer.ICEBARRAGE);
         }

         if (!this.config.showArceuus()) {
            this.removeGameTimer(GameTimer.DEATH_CHARGE);
            this.removeGameTimer(GameTimer.RESURRECT_THRALL);
            this.removeGameTimer(GameTimer.SHADOW_VEIL);
            this.removeGameTimer(GameTimer.WARD_OF_ARCEUUS);
            this.removeGameTimer(GameTimer.MARK_OF_DARKNESS);
         }

         if (!this.config.showArceuusCooldown()) {
            this.removeGameTimer(GameTimer.DEATH_CHARGE_COOLDOWN);
            this.removeGameTimer(GameTimer.RESURRECT_THRALL_COOLDOWN);
            this.removeGameTimer(GameTimer.SHADOW_VEIL_COOLDOWN);
            this.removeGameTimer(GameTimer.WARD_OF_ARCEUUS_COOLDOWN);
            this.removeGameTimer(GameTimer.CORRUPTION_COOLDOWN);
            this.removeGameTimer(GameTimer.MARK_OF_DARKNESS_COOLDOWN);
         }

         if (!this.config.showAntiPoison()) {
            this.removeGameTimer(GameTimer.ANTIPOISON);
            this.removeGameTimer(GameTimer.ANTIVENOM);
         }

         if (!this.config.showTzhaarTimers()) {
            this.removeTzhaarTimer();
         } else {
            this.createTzhaarTimer();
         }

         if (!this.config.showFarmersAffinity()) {
            this.removeVarTimer(GameTimer.FARMERS_AFFINITY);
         }

         if (!this.config.showGodWarsAltar()) {
            this.removeVarTimer(GameTimer.GOD_WARS_ALTAR);
         }

         if (!this.config.showLiquidAdrenaline()) {
            this.removeGameTimer(GameTimer.LIQUID_ADRENALINE);
         }

         if (!this.config.showMenaphiteRemedy()) {
            this.removeVarTimer(GameTimer.MENAPHITE_REMEDY);
         }

         if (!this.config.showSilkDressing()) {
            this.removeGameTimer(GameTimer.SILK_DRESSING);
         }

         if (!this.config.showBlessedCrystalScarab()) {
            this.removeGameTimer(GameTimer.BLESSED_CRYSTAL_SCARAB);
         }

         if (!this.config.showAbyssalSireStun()) {
            this.removeGameTimer(GameTimer.ABYSSAL_SIRE_STUN);
         }

         if (!this.config.showPickpocketStun()) {
            this.removeGameTimer(GameTimer.PICKPOCKET_STUN);
         }

         if (!this.config.showSpellbookSwap()) {
            this.removeGameTimer(GameTimer.SPELLBOOK_SWAP);
         }

         if (!this.config.showCurseOfTheMoons()) {
            this.removeVarCounter(GameCounter.CURSE_OF_THE_MOONS_BLUE);
            this.removeVarCounter(GameCounter.CURSE_OF_THE_MOONS_ECLIPSE);
         }

         if (!this.config.showColosseumDoom()) {
            this.removeVarCounter(GameCounter.COLOSSEUM_DOOM);
         }

         if (!this.config.showMoonlightPotion()) {
            this.removeVarTimer(GameTimer.MOONLIGHT_POTION);
         }

         if (!this.config.showBurnDamageAccumulated()) {
            this.removeVarCounter(GameCounter.BURN_DAMAGE_ACCUMULATED);
         }

         if (!this.config.showBurnDamageNextHit()) {
            this.removeVarCounter(GameCounter.BURN_DAMAGE_NEXT_HIT);
         }

         if (!this.config.showGoading()) {
            this.removeVarTimer(GameTimer.GOADING);
         }

         if (!this.config.showPrayerRegneration()) {
            this.removeVarTimer(GameTimer.PRAYER_REGENERATION);
         }

         if (!this.config.showScurriusFoodPile()) {
            this.removeVarTimer(GameTimer.SCURRIUS_FOOD_PILE);
         }

         if (!this.config.showSurge()) {
            this.removeVarTimer(GameTimer.SURGE_POTION);
         }

      }
   }

   @Subscribe
   public void onChatMessage(ChatMessage event) {
      String message = event.getMessage();
      if (event.getType() == ChatMessageType.SPAM || event.getType() == ChatMessageType.GAMEMESSAGE) {
         if (message.contains("Your dodgy necklace protects you.") || message.contains("Your attempt to steal goes unnoticed.")) {
            this.removeGameTimer(GameTimer.PICKPOCKET_STUN);
         }

         if (message.contains("You fail to pick ") && this.config.showPickpocketStun() && message.contains("pocket")) {
            if (!message.contains("hero") && !message.contains("elf")) {
               this.createGameTimer(GameTimer.PICKPOCKET_STUN, Duration.ofSeconds(5L));
            } else {
               this.createGameTimer(GameTimer.PICKPOCKET_STUN, Duration.ofSeconds(6L));
            }
         }

         if (message.equals("The Sire has been disorientated temporarily.") && this.config.showAbyssalSireStun()) {
            this.createGameTimer(GameTimer.ABYSSAL_SIRE_STUN);
         }

         String var10001;
         TimerTimer cannonTimer;
         if (!message.equals("You place the cannon base on the ground.") && !message.equals("You add the stand.") && !message.equals("You add the barrels.") && !message.equals("You add the furnace.") && !message.contains("You repair your cannon, restoring it to working order.")) {
            if (message.equals("<col=ef1020>Your cannon has broken!")) {
               this.removeGameTimer(GameTimer.CANNON);
               if (this.config.showCannon()) {
                  cannonTimer = this.createGameTimer(GameTimer.CANNON_REPAIR);
                  var10001 = cannonTimer.getTooltip();
                  cannonTimer.setTooltip(var10001 + " - World " + this.client.getWorld());
               }
            } else if (message.equals("You pick up the cannon. It's really heavy.") || message.equals("Your cannon has been destroyed!")) {
               this.removeGameTimer(GameTimer.CANNON);
               this.removeGameTimer(GameTimer.CANNON_REPAIR);
            }
         } else {
            this.removeGameTimer(GameTimer.CANNON_REPAIR);
            if (this.config.showCannon()) {
               cannonTimer = this.createGameTimer(GameTimer.CANNON);
               var10001 = cannonTimer.getTooltip();
               cannonTimer.setTooltip(var10001 + " - World " + this.client.getWorld());
            }
         }

         if (message.startsWith("You drink some of your") && message.contains("prayer enhance") && this.config.showPrayerEnhance()) {
            this.createGameTimer(GameTimer.PRAYER_ENHANCE);
         }

         if (message.equals("<col=ff0000>Your prayer enhance effect has worn off.</col>") && this.config.showPrayerEnhance()) {
            this.removeGameTimer(GameTimer.PRAYER_ENHANCE);
         }

         if (message.contains("Spirits of deceased evildoers offer you their protection") && this.config.showStaffOfTheDead()) {
            this.createGameTimer(GameTimer.STAFF_OF_THE_DEAD);
         }

         if (message.contains("Your protection fades away") && this.config.showStaffOfTheDead()) {
            this.removeGameTimer(GameTimer.STAFF_OF_THE_DEAD);
         }

         if (message.equals("<col=ef1020>You have been frozen!</col>") && this.config.showFreezes()) {
            this.freezeTimer = this.createGameTimer(GameTimer.ICEBARRAGE);
            this.freezeTime = this.client.getTickCount();
         }

         int t;
         if (this.config.showArceuus()) {
            int magicLevel = this.client.getRealSkillLevel(Skill.MAGIC);
            if (message.endsWith(">Your thieving abilities have been enhanced.</col>")) {
               this.createGameTimer(GameTimer.SHADOW_VEIL, Duration.of((long)magicLevel, RSTimeUnit.GAME_TICKS));
            } else if (message.endsWith(">Your defence against Arceuus magic has been strengthened.</col>")) {
               this.createGameTimer(GameTimer.WARD_OF_ARCEUUS, Duration.of((long)magicLevel, RSTimeUnit.GAME_TICKS));
            } else if (message.endsWith("You have placed a Mark of Darkness upon yourself.</col>")) {
               this.createGameTimer(GameTimer.MARK_OF_DARKNESS, this.getMarkOfDarknessDuration(magicLevel));
            } else if (message.contains(">You resurrect a ") && message.endsWith(" thrall.</col>")) {
               t = this.client.getBoostedSkillLevel(Skill.MAGIC);
               if (this.client.getVarbitValue(12867) == 2) {
                  t += t;
               }

               this.createGameTimer(GameTimer.RESURRECT_THRALL, Duration.of((long)t, RSTimeUnit.GAME_TICKS));
            }
         }

         if (message.endsWith("You have placed a Mark of Darkness upon yourself.</col>") && this.config.showArceuusCooldown()) {
            this.createGameTimer(GameTimer.MARK_OF_DARKNESS_COOLDOWN);
         }

         if (TZHAAR_PAUSED_MESSAGE.matcher(message).find()) {
            log.debug("Pausing tzhaar timer");
            this.config.tzhaarLastTime(Instant.now());
            if (this.config.showTzhaarTimers()) {
               this.createTzhaarTimer();
            }

         } else {
            Matcher matcher = TZHAAR_WAVE_MESSAGE.matcher(message);
            if (matcher.find()) {
               t = Integer.parseInt(matcher.group(1));
               Instant now;
               if (t == 1) {
                  log.debug("Starting tzhaar timer");
                  now = Instant.now();
                  if (this.isInInferno()) {
                     this.config.tzhaarStartTime(now.minus(Duration.ofSeconds(6L)));
                  } else {
                     this.config.tzhaarStartTime(now);
                  }

                  this.config.tzhaarLastTime((Instant)null);
                  if (this.config.showTzhaarTimers()) {
                     this.createTzhaarTimer();
                  }
               } else if (this.config.tzhaarStartTime() != null && this.config.tzhaarLastTime() != null) {
                  log.debug("Unpausing tzhaar timer");
                  now = this.config.tzhaarStartTime();
                  now = now.plus(Duration.between(this.config.tzhaarLastTime(), Instant.now()));
                  this.config.tzhaarStartTime(now);
                  this.config.tzhaarLastTime((Instant)null);
                  if (this.config.showTzhaarTimers()) {
                     this.createTzhaarTimer();
                  }
               }
            }

            if (message.equals("You quickly apply the dressing to your wounds.") && this.config.showSilkDressing()) {
               this.createGameTimer(GameTimer.SILK_DRESSING);
            }

            if (message.equals("You crack the crystal in your hand.") && this.config.showBlessedCrystalScarab()) {
               this.createGameTimer(GameTimer.BLESSED_CRYSTAL_SCARAB);
            }

            if (message.equals("You drink some of the potion, reducing the energy cost of your special attacks.</col>") && this.config.showLiquidAdrenaline()) {
               this.createGameTimer(GameTimer.LIQUID_ADRENALINE);
            }

         }
      }
   }

   private Duration getMarkOfDarknessDuration(int magicLevel) {
      Duration markOfDarknessDuration = Duration.of((long)magicLevel * 3L, RSTimeUnit.GAME_TICKS);
      ItemContainer container = this.client.getItemContainer(94);
      if (container != null) {
         Item weapon = container.getItem(EquipmentInventorySlot.WEAPON.getSlotIdx());
         if (weapon != null && weapon.getId() == 29594) {
            return markOfDarknessDuration.multipliedBy(5L);
         }
      }

      return markOfDarknessDuration;
   }

   private boolean isInFightCaves() {
      return this.client.getMapRegions() != null && ArrayUtils.contains(this.client.getMapRegions(), 9551);
   }

   private boolean isInInferno() {
      return this.client.getMapRegions() != null && ArrayUtils.contains(this.client.getMapRegions(), 9043);
   }

   private void createTzhaarTimer() {
      this.removeTzhaarTimer();
      int imageItem = this.isInFightCaves() ? 6570 : (this.isInInferno() ? 21295 : -1);
      if (imageItem != -1) {
         this.tzhaarTimer = new ElapsedTimer(this.itemManager.getImage(imageItem), this, this.config.tzhaarStartTime(), this.config.tzhaarLastTime());
         this.infoBoxManager.addInfoBox(this.tzhaarTimer);
      }
   }

   private void removeTzhaarTimer() {
      if (this.tzhaarTimer != null) {
         this.infoBoxManager.removeInfoBox(this.tzhaarTimer);
         this.tzhaarTimer = null;
      }

   }

   private void checkTeleport(int varPlayer) {
      GameTimer teleport;
      switch (varPlayer) {
         case 888:
            teleport = GameTimer.MINIGAME_TELEPORT;
            break;
         case 892:
            teleport = GameTimer.HOME_TELEPORT;
            break;
         default:
            return;
      }

      int lastTeleport = this.client.getVarpValue(varPlayer);
      long lastTeleportSeconds = (long)lastTeleport * 60L;
      Instant teleportExpireInstant = Instant.ofEpochSecond(lastTeleportSeconds).plus(teleport.getDuration());
      Duration remainingTime = Duration.between(Instant.now(), teleportExpireInstant);
      if (remainingTime.getSeconds() > 0L) {
         this.createGameTimer(teleport, remainingTime);
      } else {
         this.removeGameTimer(teleport);
      }

   }

   @Subscribe
   public void onGameTick(GameTick event) {
      Player player = this.client.getLocalPlayer();
      WorldPoint currentWorldPoint = player.getWorldLocation();
      if (this.freezeTimer != null && this.freezeTime != this.client.getTickCount() && !currentWorldPoint.equals(this.lastPoint)) {
         this.removeGameTimer(this.freezeTimer.getTimer());
         this.freezeTimer = null;
      }

      this.lastPoint = currentWorldPoint;
   }

   @Subscribe
   public void onGameStateChanged(GameStateChanged gameStateChanged) {
      switch (gameStateChanged.getGameState()) {
         case LOADING:
            if (this.tzhaarTimer != null && !this.isInFightCaves() && !this.isInInferno()) {
               this.removeTzhaarTimer();
               this.config.tzhaarStartTime((Instant)null);
               this.config.tzhaarLastTime((Instant)null);
            }

            this.removeVarCounter(GameCounter.COLOSSEUM_DOOM);
            break;
         case LOGIN_SCREEN:
         case HOPPING:
            if (this.config.tzhaarStartTime() != null && this.config.tzhaarLastTime() == null) {
               this.config.tzhaarLastTime(Instant.now());
               log.debug("Pausing tzhaar timer");
            }

            this.removeTzhaarTimer();
            this.removeGameTimer(GameTimer.TELEBLOCK);
      }

   }

   @Subscribe
   public void onGraphicChanged(GraphicChanged event) {
      Actor actor = event.getActor();
      if (actor == this.client.getLocalPlayer()) {
         if (actor.getGraphic() == GameTimer.BIND.getGraphicId() && this.config.showFreezes()) {
            this.freezeTimer = this.createGameTimer(GameTimer.BIND);
            this.freezeTime = this.client.getTickCount();
         }

         if (actor.getGraphic() == GameTimer.SNARE.getGraphicId() && this.config.showFreezes()) {
            this.freezeTimer = this.createGameTimer(GameTimer.SNARE);
            this.freezeTime = this.client.getTickCount();
         }

         if (actor.getGraphic() == GameTimer.ENTANGLE.getGraphicId() && this.config.showFreezes()) {
            this.freezeTimer = this.createGameTimer(GameTimer.ENTANGLE);
            this.freezeTime = this.client.getTickCount();
         }

         if (this.freezeTime == this.client.getTickCount() && this.config.showFreezes()) {
            if (actor.getGraphic() == GameTimer.ICERUSH.getGraphicId()) {
               this.removeGameTimer(GameTimer.ICEBARRAGE);
               this.freezeTimer = this.createGameTimer(GameTimer.ICERUSH);
            }

            if (actor.getGraphic() == GameTimer.ICEBURST.getGraphicId()) {
               this.removeGameTimer(GameTimer.ICEBARRAGE);
               this.freezeTimer = this.createGameTimer(GameTimer.ICEBURST);
            }

            if (actor.getGraphic() == GameTimer.ICEBLITZ.getGraphicId()) {
               this.removeGameTimer(GameTimer.ICEBARRAGE);
               this.freezeTimer = this.createGameTimer(GameTimer.ICEBLITZ);
            }
         }

      }
   }

   @Subscribe
   public void onItemContainerChanged(ItemContainerChanged itemContainerChanged) {
      if (itemContainerChanged.getContainerId() == 94) {
         ItemContainer container = itemContainerChanged.getItemContainer();
         Item weapon = container.getItem(EquipmentInventorySlot.WEAPON.getSlotIdx());
         if (weapon == null || !STAVES_OF_THE_DEAD.contains(weapon.getId())) {
            this.removeGameTimer(GameTimer.STAFF_OF_THE_DEAD);
         }

      }
   }

   @Subscribe
   public void onNpcDespawned(NpcDespawned npcDespawned) {
      NPC npc = npcDespawned.getNpc();
      if (npc.isDead()) {
         int npcId = npc.getId();
         if (npcId == 8062 || npcId == 8063) {
            this.removeGameTimer(GameTimer.ICEBARRAGE);
         }

      }
   }

   @Subscribe
   void onNpcSpawned(NpcSpawned event) {
      NPC npc = event.getNpc();
      if (npc.getId() == 5889) {
         this.removeGameTimer(GameTimer.ABYSSAL_SIRE_STUN);
      }

   }

   @Subscribe
   void onNpcChanged(NpcChanged event) {
      NPC npc = event.getNpc();
      if (npc.getId() == 5889) {
         this.removeGameTimer(GameTimer.ABYSSAL_SIRE_STUN);
      }

   }

   @Subscribe
   public void onActorDeath(ActorDeath actorDeath) {
      if (actorDeath.getActor() == this.client.getLocalPlayer()) {
         this.infoBoxManager.removeIf((t) -> {
            return t instanceof TimerTimer && ((TimerTimer)t).getTimer().isRemovedOnDeath();
         });
      }

   }

   private TimerTimer createGameTimer(GameTimer timer) {
      if (timer.getDuration() == null) {
         throw new IllegalArgumentException("Timer with no duration");
      } else {
         return this.createGameTimer(timer, timer.getDuration());
      }
   }

   private TimerTimer createGameTimer(GameTimer timer, Duration duration) {
      this.removeGameTimer(timer);
      TimerTimer t = new TimerTimer(timer, duration, this);
      switch (timer.getImageType()) {
         case SPRITE:
            this.spriteManager.getSpriteAsync(timer.getImageId(), 0, (InfoBox)t);
            break;
         case ITEM:
            t.setImage(this.itemManager.getImage(timer.getImageId()));
      }

      t.setTooltip(timer.getDescription());
      this.infoBoxManager.addInfoBox(t);
      return t;
   }

   @VisibleForTesting
   void removeGameTimer(GameTimer timer) {
      this.infoBoxManager.removeIf((t) -> {
         return t instanceof TimerTimer && ((TimerTimer)t).getTimer() == timer;
      });
   }

   private void updateVarTimer(GameTimer gameTimer, int varValue, IntUnaryOperator tickDuration) {
      this.updateVarTimer(gameTimer, varValue, (i) -> {
         return i == 0;
      }, tickDuration);
   }

   private void updateVarTimer(GameTimer gameTimer, int varValue, IntPredicate removeTimerCheck, IntUnaryOperator tickDuration) {
      TimerTimer timer = (TimerTimer)this.varTimers.get(gameTimer);
      int ticks = tickDuration.applyAsInt(varValue);
      Duration duration = Duration.of((long)ticks, RSTimeUnit.GAME_TICKS);
      if (removeTimerCheck.test(varValue)) {
         this.removeVarTimer(gameTimer);
      } else if (timer != null && ticks <= timer.ticks) {
         timer.ticks = ticks;
         timer.updateDuration(duration);
      } else {
         timer = this.createGameTimer(gameTimer, duration);
         timer.ticks = ticks;
         this.varTimers.put(gameTimer, timer);
      }

   }

   private void removeVarTimer(GameTimer gameTimer) {
      this.removeGameTimer(gameTimer);
      this.varTimers.remove(gameTimer);
   }

   private void updateVarCounter(GameCounter gameCounter, int varValue) {
      BuffCounter buffCounter = (BuffCounter)this.varCounters.get(gameCounter);
      if (varValue == 0) {
         this.removeVarCounter(gameCounter);
      } else if (buffCounter == null) {
         buffCounter = this.createBuffCounter(gameCounter, varValue);
         this.varCounters.put(gameCounter, buffCounter);
      } else {
         buffCounter.setCount(varValue);
      }

   }

   private BuffCounter createBuffCounter(GameCounter gameCounter, int count) {
      this.removeBuffCounter(gameCounter);
      BuffCounter buffCounter = new BuffCounter(this, gameCounter, count);
      switch (gameCounter.getImageType()) {
         case SPRITE:
            this.spriteManager.getSpriteAsync(gameCounter.getImageId(), 0, (InfoBox)buffCounter);
            break;
         case ITEM:
            buffCounter.setImage(this.itemManager.getImage(gameCounter.getImageId()));
      }

      buffCounter.setTooltip(gameCounter.getDescription());
      this.infoBoxManager.addInfoBox(buffCounter);
      return buffCounter;
   }

   private void removeVarCounter(GameCounter gameCounter) {
      this.removeBuffCounter(gameCounter);
      this.varCounters.remove(gameCounter);
   }

   private void removeBuffCounter(GameCounter gameCounter) {
      this.infoBoxManager.removeIf((b) -> {
         return b instanceof BuffCounter && ((BuffCounter)b).getGameCounter() == gameCounter;
      });
   }
}
