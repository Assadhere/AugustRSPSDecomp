package net.runelite.client.plugins.specialcounter;

import com.google.common.collect.ImmutableSet;
import com.google.inject.Provides;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.inject.Inject;
import javax.inject.Named;
import net.runelite.api.Actor;
import net.runelite.api.Client;
import net.runelite.api.EquipmentInventorySlot;
import net.runelite.api.GameState;
import net.runelite.api.Hitsplat;
import net.runelite.api.Item;
import net.runelite.api.ItemContainer;
import net.runelite.api.NPC;
import net.runelite.api.Player;
import net.runelite.api.Skill;
import net.runelite.api.WorldView;
import net.runelite.api.coords.WorldArea;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.CommandExecuted;
import net.runelite.api.events.FakeXpDrop;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.HitsplatApplied;
import net.runelite.api.events.NpcChanged;
import net.runelite.api.events.NpcDespawned;
import net.runelite.api.events.ScriptPostFired;
import net.runelite.api.events.StatChanged;
import net.runelite.api.events.VarbitChanged;
import net.runelite.client.Notifier;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.game.ItemManager;
import net.runelite.client.game.SpriteManager;
import net.runelite.client.party.PartyService;
import net.runelite.client.party.WSClient;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;
import net.runelite.client.ui.overlay.infobox.InfoBoxManager;
import net.runelite.client.util.ImageUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@PluginDescriptor(
   name = "Special Attack Counter",
   description = "Track special attacks used on NPCs",
   tags = {"combat", "npcs", "overlay"},
   enabledByDefault = false
)
public class SpecialCounterPlugin extends Plugin {
   private static final Logger log = LoggerFactory.getLogger(SpecialCounterPlugin.class);
   private static final Set<Integer> IGNORED_NPCS = ImmutableSet.of(320, 8062, 8063, 2668, 7413, 6613, new Integer[]{6614, 5916, 5918});
   private int currentWorld;
   private int specialPercentage;
   private int lastHitPointsExperience;
   private int lastHpChangeCycle;
   private SpecialWeapon specialWeapon;
   private int hitsplatTick;
   private final List<Hitsplat> hitsplats = new ArrayList();
   private NPC lastSpecTarget;
   private boolean lastSpecHpChange;
   private final Set<Integer> interactedNpcIndexes = new HashSet();
   private final SpecialCounter[] specialCounter = new SpecialCounter[SpecialWeapon.values().length];
   private PercentageInfobox percentageInfobox;
   private final List<PlayerInfoDrop> playerInfoDrops = new ArrayList();
   @Inject
   private Client client;
   @Inject
   private ClientThread clientThread;
   @Inject
   private WSClient wsClient;
   @Inject
   private PartyService party;
   @Inject
   private InfoBoxManager infoBoxManager;
   @Inject
   private ItemManager itemManager;
   @Inject
   private SpriteManager spriteManager;
   @Inject
   private Notifier notifier;
   @Inject
   private SpecialCounterConfig config;
   @Inject
   private OverlayManager overlayManager;
   @Inject
   private PlayerInfoDropOverlay playerInfoDropOverlay;
   @Inject
   @Named("developerMode")
   boolean developerMode;

   @Provides
   SpecialCounterConfig getConfig(ConfigManager configManager) {
      return (SpecialCounterConfig)configManager.getConfig(SpecialCounterConfig.class);
   }

   protected void startUp() {
      this.overlayManager.add(this.playerInfoDropOverlay);
      this.wsClient.registerMessage(SpecialCounterUpdate.class);
      this.currentWorld = -1;
      this.specialPercentage = -1;
      this.lastHitPointsExperience = -1;
      this.interactedNpcIndexes.clear();
   }

   protected void shutDown() {
      this.specialWeapon = null;
      this.lastSpecTarget = null;
      this.hitsplats.clear();
      this.removeCounters();
      this.overlayManager.remove(this.playerInfoDropOverlay);
      this.wsClient.unregisterMessage(SpecialCounterUpdate.class);
   }

   @Subscribe
   public void onScriptPostFired(ScriptPostFired event) {
      if (event.getScriptId() == 2308) {
         log.debug("Resetting spec counter as sotetseg maze script was ran");
         this.removeCounters();
      }

   }

   @Subscribe
   public void onFakeXpDrop(FakeXpDrop event) {
      if (event.getSkill() == Skill.HITPOINTS) {
         this.lastHpChangeCycle = this.client.getGameCycle();
      }

   }

   @Subscribe
   public void onStatChanged(StatChanged event) {
      if (event.getSkill() == Skill.HITPOINTS) {
         if (event.getXp() > this.lastHitPointsExperience) {
            this.lastHpChangeCycle = this.client.getGameCycle();
         }

         this.lastHitPointsExperience = event.getXp();
      }

   }

   @Subscribe
   public void onGameTick(GameTick event) {
      if (this.specialWeapon != null && this.lastSpecTarget != null) {
         int tickCount = this.client.getTickCount();
         if (this.specialWeapon == SpecialWeapon.ELDER_MAUL) {
            this.specialAttackHit(this.specialWeapon, this.lastSpecHpChange ? 1 : 0, this.lastSpecTarget);
            this.specialWeapon = null;
            this.hitsplats.clear();
            this.lastSpecTarget = null;
         } else if (this.hitsplatTick == tickCount) {
            Hitsplat last;
            if (this.specialWeapon == SpecialWeapon.TONALZTICS_OF_RALOS) {
               if (this.hitsplats.size() < 2) {
                  log.debug("<2 hitsplats for tonalztics?");
                  return;
               }

               last = (Hitsplat)this.hitsplats.get(this.hitsplats.size() - 1);
               Hitsplat secondToLast = (Hitsplat)this.hitsplats.get(this.hitsplats.size() - 2);
               int hit = Math.min(last.getAmount(), 1) + Math.min(secondToLast.getAmount(), 1);
               this.specialAttackHit(this.specialWeapon, hit, this.lastSpecTarget);
            } else {
               if (this.hitsplats.isEmpty()) {
                  return;
               }

               last = (Hitsplat)this.hitsplats.get(this.hitsplats.size() - 1);
               this.specialAttackHit(this.specialWeapon, last.getAmount(), this.lastSpecTarget);
            }

            this.specialWeapon = null;
            this.hitsplats.clear();
            this.lastSpecTarget = null;
         } else if (tickCount > this.hitsplatTick) {
            log.debug("Timeout waiting for hitsplat for {}", this.specialWeapon);
            this.specialWeapon = null;
            this.hitsplats.clear();
            this.lastSpecTarget = null;
         }

      }
   }

   @Subscribe
   public void onGameStateChanged(GameStateChanged event) {
      if (event.getGameState() == GameState.LOGGED_IN) {
         if (this.currentWorld == -1) {
            this.currentWorld = this.client.getWorld();
         } else if (this.currentWorld != this.client.getWorld()) {
            this.currentWorld = this.client.getWorld();
            this.removeCounters();
         }
      }

   }

   @Subscribe
   public void onVarbitChanged(VarbitChanged event) {
      if (event.getVarpId() == 300) {
         int specialPercentage = event.getValue();
         if (this.specialPercentage != -1 && specialPercentage < this.specialPercentage) {
            this.specialPercentage = specialPercentage;
            int serverTicks = this.client.getTickCount();
            this.clientThread.invokeLater(() -> {
               this.specialWeapon = this.usedSpecialWeapon();
               if (this.specialWeapon != null) {
                  Actor target = this.client.getLocalPlayer().getInteracting();
                  this.lastSpecTarget = target instanceof NPC ? (NPC)target : null;
                  this.lastSpecHpChange = this.lastHpChangeCycle == this.client.getGameCycle();
                  this.hitsplatTick = serverTicks + this.getHitDelay(this.specialWeapon, target);
                  log.debug("Special attack used - cycle: {} percent: {} weapon: {} server cycle {} hitsplat cycle {} hp change: {}", new Object[]{this.client.getGameCycle(), specialPercentage, this.specialWeapon, serverTicks, this.hitsplatTick, this.lastSpecHpChange});
                  if (this.lastSpecTarget != null) {
                     int interactingId = this.lastSpecTarget.getId();
                     int npcIndex = this.lastSpecTarget.getIndex();
                     if (IGNORED_NPCS.contains(interactingId)) {
                        return;
                     }

                     if (!this.interactedNpcIndexes.contains(npcIndex)) {
                        log.debug("Removing counters due to interact change");
                        this.removeCounters();
                        this.interactedNpcIndexes.add(npcIndex);
                     }
                  }

               }
            });
         } else {
            this.specialPercentage = specialPercentage;
         }
      }
   }

   @Subscribe
   public void onHitsplatApplied(HitsplatApplied hitsplatApplied) {
      Actor target = hitsplatApplied.getActor();
      Hitsplat hitsplat = hitsplatApplied.getHitsplat();
      if (hitsplat.isMine() && target != this.client.getLocalPlayer()) {
         if (this.lastSpecTarget != null && target == this.lastSpecTarget) {
            if (this.hitsplatTick == this.client.getTickCount()) {
               this.hitsplats.add(hitsplat);
            }

         }
      }
   }

   private void specialAttackHit(SpecialWeapon specialWeapon, int hit, NPC target) {
      int localPlayerId = this.client.getLocalPlayer().getId();
      int counterHit = specialWeapon.isDamage() ? specialWeapon.computeHit(hit, target) : Math.min(hit, 1);
      float defenceDrain = specialWeapon.computeDrainPercent(hit, target);
      log.debug("Special attack hit {} hitsplat {}", specialWeapon, hit);
      if (this.config.infobox()) {
         this.updateCounter(specialWeapon, (String)null, counterHit, defenceDrain);
      }

      if (this.party.isInParty()) {
         int npcIndex = target.getIndex();
         SpecialCounterUpdate specialCounterUpdate = new SpecialCounterUpdate(npcIndex, specialWeapon, hit, this.client.getWorld(), localPlayerId);
         this.party.send(specialCounterUpdate);
      }

      if (counterHit > 0 || defenceDrain > 0.0F || this.config.specDropMisses()) {
         this.playerInfoDrops.add(this.createSpecInfoDrop(specialWeapon, counterHit, localPlayerId));
      }

   }

   @Subscribe
   public void onNpcDespawned(NpcDespawned npcDespawned) {
      NPC actor = npcDespawned.getNpc();
      if (this.lastSpecTarget == actor) {
         this.lastSpecTarget = null;
      }

      if (actor.isDead() && this.interactedNpcIndexes.contains(actor.getIndex())) {
         this.removeCounters();
      }

   }

   @Subscribe
   public void onNpcChanged(NpcChanged npcChanged) {
      NPC npc = npcChanged.getNpc();
      if (npc.getId() == 12192 || npc.getId() == 12196) {
         log.debug("Duke died");
         this.removeCounters();
      }

   }

   @Subscribe
   public void onSpecialCounterUpdate(SpecialCounterUpdate event) {
      if (this.party.getLocalMember().getMemberId() != event.getMemberId() && event.getWorld() == this.client.getWorld()) {
         String name = this.party.getMemberById(event.getMemberId()).getDisplayName();
         if (name != null) {
            this.clientThread.invoke(() -> {
               WorldView wv = this.client.getTopLevelWorldView();
               NPC target = (NPC)wv.npcs().byIndex(event.getNpcIndex());
               SpecialWeapon specialWeapon = event.getWeapon();
               int counterHit = specialWeapon.isDamage() ? specialWeapon.computeHit(event.getHit(), target) : Math.min(event.getHit(), 1);
               float defenceDrain = event.getWeapon().computeDrainPercent(event.getHit(), target);
               if (this.interactedNpcIndexes.isEmpty()) {
                  this.interactedNpcIndexes.add(event.getNpcIndex());
               }

               if (this.interactedNpcIndexes.contains(event.getNpcIndex()) && this.config.infobox()) {
                  this.updateCounter(event.getWeapon(), name, counterHit, defenceDrain);
               }

               if (counterHit > 0 || defenceDrain > 0.0F || this.config.specDropMisses()) {
                  this.playerInfoDrops.add(this.createSpecInfoDrop(event.getWeapon(), counterHit, event.getPlayerId()));
               }

            });
         }
      }
   }

   @Subscribe
   public void onCommandExecuted(CommandExecuted commandExecuted) {
      if (this.developerMode && commandExecuted.getCommand().equalsIgnoreCase("spec")) {
         int hit = (int)(Math.random() * 2.0);
         this.playerInfoDrops.add(this.createSpecInfoDrop(SpecialWeapon.BANDOS_GODSWORD, hit, this.client.getLocalPlayer().getId()));
      }

   }

   private SpecialWeapon usedSpecialWeapon() {
      ItemContainer equipment = this.client.getItemContainer(94);
      if (equipment == null) {
         return null;
      } else {
         Item weapon = equipment.getItem(EquipmentInventorySlot.WEAPON.getSlotIdx());
         if (weapon == null) {
            return null;
         } else {
            SpecialWeapon[] var3 = SpecialWeapon.values();
            int var4 = var3.length;

            for(int var5 = 0; var5 < var4; ++var5) {
               SpecialWeapon specialWeapon = var3[var5];
               if (Arrays.stream(specialWeapon.getItemID()).anyMatch((id) -> {
                  return id == weapon.getId();
               })) {
                  return specialWeapon;
               }
            }

            return null;
         }
      }
   }

   private void updateCounter(SpecialWeapon specialWeapon, String name, int hit, float defenceDrain) {
      if (defenceDrain > 0.0F && this.config.defenceDrainInfobox()) {
         if (this.percentageInfobox == null) {
            this.percentageInfobox = new PercentageInfobox(this.itemManager.getImage(specialWeapon.getItemID()[0]), this);
            this.infoBoxManager.addInfoBox(this.percentageInfobox);
         }

         this.percentageInfobox.mul(defenceDrain);
      }

      if (hit != 0) {
         SpecialCounter counter = this.specialCounter[specialWeapon.ordinal()];
         if (counter == null) {
            counter = new SpecialCounter(this.itemManager.getImage(specialWeapon.getItemID()[0]), this, this.config, hit, specialWeapon);
            this.infoBoxManager.addInfoBox(counter);
            this.specialCounter[specialWeapon.ordinal()] = counter;
         } else {
            counter.addHits(hit);
         }

         this.sendNotification(specialWeapon, counter);
         Map<String, Integer> partySpecs = counter.getPartySpecs();
         if (this.party.isInParty()) {
            if (partySpecs.containsKey(name)) {
               partySpecs.put(name, hit + (Integer)partySpecs.get(name));
            } else {
               partySpecs.put(name, hit);
            }
         }

      }
   }

   private void sendNotification(SpecialWeapon weapon, SpecialCounter counter) {
      int threshold = (Integer)weapon.getThreshold().apply(this.config);
      if (threshold > 0 && counter.getCount() >= threshold) {
         this.notifier.notify(this.config.thresholdNotification(), weapon.getName() + " special attack threshold reached!");
      }

   }

   private void removeCounters() {
      this.interactedNpcIndexes.clear();

      for(int i = 0; i < this.specialCounter.length; ++i) {
         SpecialCounter counter = this.specialCounter[i];
         if (counter != null) {
            this.infoBoxManager.removeInfoBox(counter);
            this.specialCounter[i] = null;
         }
      }

      this.infoBoxManager.removeInfoBox(this.percentageInfobox);
      this.percentageInfobox = null;
   }

   private PlayerInfoDrop createSpecInfoDrop(SpecialWeapon weapon, int hit, int playerId) {
      int cycle = this.client.getGameCycle();
      BufferedImage image = ImageUtil.resizeImage(this.itemManager.getImage(weapon.getItemID()[0]), 24, 24);
      BufferedImage background = hit == 0 ? this.spriteManager.getSprite(1358, 0) : null;
      return PlayerInfoDrop.builder().startCycle(cycle).endCycle(cycle + 100).playerIdx(playerId).text(Integer.toString(hit)).textBackground(background).color(this.config.specDropColor()).startHeightOffset(100).endHeightOffset(400).image(image).build();
   }

   private int getHitDelay(SpecialWeapon specialWeapon, Actor target) {
      if (target == null) {
         return 1;
      } else {
         Player player = this.client.getLocalPlayer();
         if (player == null) {
            return 1;
         } else {
            WorldPoint playerWp = player.getWorldLocation();
            if (playerWp == null) {
               return 1;
            } else {
               WorldArea targetArea = target.getWorldArea();
               if (targetArea == null) {
                  return 1;
               } else {
                  int distance = targetArea.distanceTo(playerWp);
                  int serverCycles = specialWeapon.getHitDelay(distance);
                  if (serverCycles != 1) {
                     log.debug("Projectile distance {} server cycles {}", distance, serverCycles);
                  }

                  return serverCycles;
               }
            }
         }
      }
   }

   List<PlayerInfoDrop> getPlayerInfoDrops() {
      return this.playerInfoDrops;
   }
}
