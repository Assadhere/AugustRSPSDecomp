package net.runelite.client.plugins.hunter;

import com.google.inject.Provides;
import java.time.Instant;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.GameObject;
import net.runelite.api.Player;
import net.runelite.api.Tile;
import net.runelite.api.coords.Angle;
import net.runelite.api.coords.Direction;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.GameObjectSpawned;
import net.runelite.api.events.GameTick;
import net.runelite.client.Notifier;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@PluginDescriptor(
   name = "Hunter",
   description = "Show the state of your traps",
   tags = {"overlay", "skilling", "timers"}
)
public class HunterPlugin extends Plugin {
   private static final Logger log = LoggerFactory.getLogger(HunterPlugin.class);
   @Inject
   private Client client;
   @Inject
   private OverlayManager overlayManager;
   @Inject
   private TrapOverlay overlay;
   @Inject
   private Notifier notifier;
   @Inject
   private HunterConfig config;
   private final Map<WorldPoint, HunterTrap> traps = new HashMap();
   private WorldPoint lastTickLocalPlayerLocation;

   @Provides
   HunterConfig provideConfig(ConfigManager configManager) {
      return (HunterConfig)configManager.getConfig(HunterConfig.class);
   }

   protected void startUp() {
      this.overlayManager.add(this.overlay);
      this.overlay.updateConfig();
   }

   protected void shutDown() throws Exception {
      this.overlayManager.remove(this.overlay);
      this.traps.clear();
   }

   @Subscribe
   public void onGameObjectSpawned(GameObjectSpawned event) {
      GameObject gameObject = event.getGameObject();
      WorldPoint trapLocation = gameObject.getWorldLocation();
      HunterTrap myTrap = (HunterTrap)this.traps.get(trapLocation);
      Player localPlayer = this.client.getLocalPlayer();
      switch (gameObject.getId()) {
         case 721:
         case 8734:
         case 8986:
         case 8996:
         case 9004:
         case 9348:
         case 9373:
         case 9375:
         case 9377:
         case 9379:
         case 9382:
         case 9383:
         case 9384:
         case 19226:
         case 20648:
         case 20649:
         case 20650:
         case 20651:
         case 28830:
         case 28831:
         case 50717:
         case 50726:
         case 50727:
            if (myTrap != null) {
               myTrap.setState(HunterTrap.State.FULL);
               myTrap.resetTimer();
               if (myTrap.getObjectId() == 28827) {
                  this.notifier.notify(this.config.maniacalMonkeyNotify(), "You've caught part of a monkey's tail.");
               }
            }
            break;
         case 2025:
         case 2026:
         case 2028:
         case 2029:
         case 8972:
         case 8974:
         case 8985:
         case 8987:
         case 8993:
         case 8997:
         case 9003:
         case 9005:
         case 9346:
         case 9347:
         case 9349:
         case 9374:
         case 9376:
         case 9378:
         case 9381:
         case 9386:
         case 9387:
         case 9388:
         case 9390:
         case 9391:
         case 9392:
         case 9393:
         case 9394:
         case 9396:
         case 9397:
         case 19218:
         case 19225:
         case 19851:
         case 20128:
         case 20129:
         case 20130:
         case 20131:
         case 28828:
         case 28829:
         case 50716:
         case 50718:
         case 50724:
         case 50725:
         case 50728:
         case 50729:
         case 50730:
         case 50731:
            if (myTrap != null) {
               myTrap.setState(HunterTrap.State.TRANSITION);
            }
            break;
         case 8731:
         case 8992:
         case 9002:
         case 9343:
         case 50723:
            if (this.lastTickLocalPlayerLocation != null && trapLocation.distanceTo(this.lastTickLocalPlayerLocation) == 0) {
               Direction trapOrientation = (new Angle(gameObject.getOrientation())).getNearestDirection();
               WorldPoint translatedTrapLocation = trapLocation;
               switch (trapOrientation) {
                  case SOUTH:
                     translatedTrapLocation = trapLocation.dy(-1);
                     break;
                  case WEST:
                     translatedTrapLocation = trapLocation.dx(-1);
               }

               log.debug("Trap placed by \"{}\" on {} facing {}", new Object[]{localPlayer.getName(), translatedTrapLocation, trapOrientation});
               this.traps.put(translatedTrapLocation, new HunterTrap(gameObject));
            }
            break;
         case 9344:
         case 9385:
         case 19215:
         case 19224:
         case 50719:
            if (myTrap != null) {
               myTrap.setState(HunterTrap.State.EMPTY);
               myTrap.resetTimer();
            }
            break;
         case 9345:
         case 9380:
         case 19223:
            if (this.lastTickLocalPlayerLocation != null && trapLocation.distanceTo(this.lastTickLocalPlayerLocation) == 0) {
               log.debug("Trap placed by \"{}\" on {}", localPlayer.getName(), localPlayer.getWorldLocation());
               this.traps.put(trapLocation, new HunterTrap(gameObject));
            }
            break;
         case 19217:
         case 28827:
            if (localPlayer.getWorldLocation().distanceTo(trapLocation) <= 2) {
               log.debug("Trap placed by \"{}\" on {}", localPlayer.getName(), trapLocation);
               this.traps.put(trapLocation, new HunterTrap(gameObject));
            }
      }

   }

   @Subscribe
   public void onGameTick(GameTick event) {
      Iterator<Map.Entry<WorldPoint, HunterTrap>> it = this.traps.entrySet().iterator();
      Tile[][][] tiles = this.client.getScene().getTiles();
      Instant expire = Instant.now().minus(HunterTrap.TRAP_TIME.multipliedBy(2L));

      while(true) {
         while(it.hasNext()) {
            Map.Entry<WorldPoint, HunterTrap> entry = (Map.Entry)it.next();
            HunterTrap trap = (HunterTrap)entry.getValue();
            WorldPoint world = (WorldPoint)entry.getKey();
            LocalPoint local = LocalPoint.fromWorld(this.client, world);
            if (local == null) {
               if (trap.getPlacedOn().isBefore(expire)) {
                  log.debug("Trap removed from personal trap collection due to timeout, {} left", this.traps.size());
                  it.remove();
               }
            } else {
               Tile tile = tiles[world.getPlane()][local.getSceneX()][local.getSceneY()];
               GameObject[] objects = tile.getGameObjects();
               boolean containsBoulder = false;
               boolean containsAnything = false;
               boolean containsYoungTree = false;
               GameObject[] var14 = objects;
               int var15 = objects.length;

               for(int var16 = 0; var16 < var15; ++var16) {
                  GameObject object = var14[var16];
                  if (object != null) {
                     containsAnything = true;
                     if (object.getId() == 19215 || object.getId() == 28824) {
                        containsBoulder = true;
                        break;
                     }

                     if (object.getId() == 8732 || object.getId() == 8990 || object.getId() == 9000 || object.getId() == 9341 || object.getId() == 50721 || object.getId() == 50722) {
                        containsYoungTree = true;
                     }
                  }
               }

               if (containsAnything && !containsYoungTree) {
                  if (containsBoulder) {
                     it.remove();
                     log.debug("Special trap removed from personal trap collection, {} left", this.traps.size());
                     if (trap.getObjectId() == 28827 && !trap.getState().equals(HunterTrap.State.FULL) && !trap.getState().equals(HunterTrap.State.OPEN)) {
                        this.notifier.notify(this.config.maniacalMonkeyNotify(), "The monkey escaped.");
                     }
                  }
               } else {
                  it.remove();
                  log.debug("Trap removed from personal trap collection, {} left", this.traps.size());
               }
            }
         }

         this.lastTickLocalPlayerLocation = this.client.getLocalPlayer().getWorldLocation();
         return;
      }
   }

   @Subscribe
   public void onConfigChanged(ConfigChanged event) {
      if (event.getGroup().equals("hunterplugin")) {
         this.overlay.updateConfig();
      }

   }

   public Map<WorldPoint, HunterTrap> getTraps() {
      return this.traps;
   }
}
