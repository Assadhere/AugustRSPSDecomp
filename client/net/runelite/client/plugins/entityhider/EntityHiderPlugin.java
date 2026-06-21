package net.runelite.client.plugins.entityhider;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.ImmutableSet;
import com.google.inject.Provides;
import custom.SoftHideNpcsScript;
import custom.SoftHidePriority;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.GraphicsObject;
import net.runelite.api.MenuEntry;
import net.runelite.api.NPC;
import net.runelite.api.Player;
import net.runelite.api.Projectile;
import net.runelite.api.Renderable;
import net.runelite.api.Scene;
import net.runelite.api.WorldEntity;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.MenuEntryAdded;
import net.runelite.client.callback.Hooks;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.game.NpcUtil;
import net.runelite.client.party.PartyService;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;

@PluginDescriptor(
   name = "Entity Hider",
   description = "Hide players, NPCs, and/or projectiles",
   tags = {"npcs", "players", "projectiles"},
   enabledByDefault = true
)
public class EntityHiderPlugin extends Plugin {
   private static final Set<Integer> THRALL_IDS = ImmutableSet.of(10878, 10881, 10884, 10879, 10882, 10885, new Integer[]{10880, 10883, 10886});
   private static final Set<Integer> RANDOM_EVENT_NPC_IDS = ImmutableSet.of(6747, 5426, 307, 314, 322, 6749, new Integer[]{390, 6754, 6744, 6748, 5429, 5430, 5431, 5432, 312, 13445, 13446, 13443, 13444, 326, 327, 5438, 5441, 6746, 5437, 5440, 6750, 6751, 6752, 6753, 5436, 5439, 380, 6738, 6755, 375, 376, 5510, 6743, 12551, 12552});
   @Inject
   private Client client;
   @Inject
   private EntityHiderConfig config;
   @Inject
   private Hooks hooks;
   @Inject
   private NpcUtil npcUtil;
   @Inject
   private PartyService partyService;
   private boolean hideOthers;
   private boolean hideOthers2D;
   private boolean hidePartyMembers;
   private boolean hideFriends;
   private boolean hideFriendsChatMembers;
   private boolean hideClanMembers;
   private boolean hideIgnoredPlayers;
   private boolean hideLocalPlayer;
   private boolean hideLocalPlayer2D;
   private boolean hideNPCs;
   private boolean hideNPCs2D;
   private boolean hideBoats;
   private boolean hideDeadNpcs;
   private boolean hidePets;
   private boolean hideThralls;
   private boolean hideRandomEvents;
   private boolean hideAttackers;
   private boolean hideProjectiles;
   private boolean softHideServerNpcs;
   private byte softHideAlphaByte;
   private final Set<Integer> softHiddenNpcIds = new HashSet();
   private final Hooks.RenderableDrawListener drawListener = this::shouldDraw;

   @Provides
   EntityHiderConfig provideConfig(ConfigManager configManager) {
      return (EntityHiderConfig)configManager.getConfig(EntityHiderConfig.class);
   }

   protected void startUp() {
      this.updateConfig();
      this.hooks.registerRenderableDrawListener(this.drawListener);
   }

   protected void shutDown() {
      this.hooks.unregisterRenderableDrawListener(this.drawListener);
      SoftHidePriority.setNpcIds(Collections.emptySet());
      SoftHidePriority.setAlphaByte((byte)0);
   }

   @Subscribe
   public void onConfigChanged(ConfigChanged e) {
      if (e.getGroup().equals("entityhider")) {
         this.updateConfig();
      }

   }

   private void updateConfig() {
      this.hideOthers = this.config.hideOthers();
      this.hideOthers2D = this.config.hideOthers2D();
      this.hidePartyMembers = this.config.hidePartyMembers();
      this.hideFriends = this.config.hideFriends();
      this.hideFriendsChatMembers = this.config.hideFriendsChatMembers();
      this.hideClanMembers = this.config.hideClanChatMembers();
      this.hideIgnoredPlayers = this.config.hideIgnores();
      this.hideLocalPlayer = this.config.hideLocalPlayer();
      this.hideLocalPlayer2D = this.config.hideLocalPlayer2D();
      this.hideNPCs = this.config.hideNPCs();
      this.hideNPCs2D = this.config.hideNPCs2D();
      this.hideDeadNpcs = this.config.hideDeadNpcs();
      this.hideBoats = this.config.hideWorldEntities();
      this.hidePets = this.config.hidePets();
      this.hideThralls = this.config.hideThralls();
      this.hideRandomEvents = this.config.hideRandomEvents();
      this.hideAttackers = this.config.hideAttackers();
      this.hideProjectiles = this.config.hideProjectiles();
      this.softHideServerNpcs = this.config.softHideServerNpcs();
      int opacityPercent = Math.max(1, Math.min(100, this.config.softHideOpacityPercent()));
      int alphaByte = Math.round(255.0F * (1.0F - (float)opacityPercent / 100.0F));
      this.softHideAlphaByte = (byte)Math.max(0, Math.min(255, alphaByte));
      this.pushSoftHide();
   }

   @Subscribe
   public void onSoftHideNpcsScript(SoftHideNpcsScript packet) {
      this.softHiddenNpcIds.clear();
      int[] var2 = packet.getIds();
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         int id = var2[var4];
         this.softHiddenNpcIds.add(id);
      }

      this.pushSoftHide();
   }

   private void pushSoftHide() {
      SoftHidePriority.setNpcIds(this.softHideServerNpcs ? this.softHiddenNpcIds : Collections.emptySet());
      SoftHidePriority.setAlphaByte(this.softHideServerNpcs ? this.softHideAlphaByte : 0);
   }

   @Subscribe
   public void onGameStateChanged(GameStateChanged event) {
      switch (event.getGameState()) {
         case LOGIN_SCREEN:
         case HOPPING:
         case CONNECTION_LOST:
            this.softHiddenNpcIds.clear();
            this.pushSoftHide();
         default:
      }
   }

   @Subscribe
   public void onMenuEntryAdded(MenuEntryAdded event) {
      if (this.softHideServerNpcs && !this.softHiddenNpcIds.isEmpty()) {
         MenuEntry entry = event.getMenuEntry();
         NPC npc = entry.getNpc();
         if (npc != null && this.softHiddenNpcIds.contains(npc.getId())) {
            this.client.getMenu().removeMenuEntry(entry);
         }

      }
   }

   @VisibleForTesting
   boolean shouldDraw(Renderable renderable, boolean drawingUI) {
      boolean var10000;
      if (renderable instanceof Player) {
         Player player = (Player)renderable;
         Player local = this.client.getLocalPlayer();
         if (player.getName() == null) {
            return true;
         } else if (player == local) {
            label263: {
               if (drawingUI) {
                  if (!this.hideLocalPlayer2D) {
                     break label263;
                  }
               } else if (!this.hideLocalPlayer) {
                  break label263;
               }

               var10000 = false;
               return var10000;
            }

            var10000 = true;
            return var10000;
         } else if (this.hideAttackers && player.getInteracting() == local) {
            return false;
         } else if (this.partyService.isInParty() && this.partyService.getMemberByDisplayName(player.getName()) != null) {
            return !this.hidePartyMembers;
         } else if (player.isFriend()) {
            return !this.hideFriends;
         } else if (player.isFriendsChatMember()) {
            return !this.hideFriendsChatMembers;
         } else if (player.isClanMember()) {
            return !this.hideClanMembers;
         } else if (this.client.getIgnoreContainer().findByName(player.getName()) != null) {
            return !this.hideIgnoredPlayers;
         } else {
            label265: {
               if (drawingUI) {
                  if (!this.hideOthers2D) {
                     break label265;
                  }
               } else if (!this.hideOthers) {
                  break label265;
               }

               var10000 = false;
               return var10000;
            }

            var10000 = true;
            return var10000;
         }
      } else if (!(renderable instanceof NPC)) {
         if (renderable instanceof Projectile) {
            return !this.hideProjectiles;
         } else if (renderable instanceof GraphicsObject) {
            if (!this.hideDeadNpcs) {
               return true;
            } else {
               switch (((GraphicsObject)renderable).getId()) {
                  case 1562:
                  case 1563:
                  case 1564:
                  case 1565:
                  case 1566:
                  case 1567:
                     return false;
                  default:
                     return true;
               }
            }
         } else if (renderable instanceof Scene) {
            if (!this.hideBoats) {
               return true;
            } else {
               Scene scene = (Scene)renderable;
               WorldEntity we = (WorldEntity)this.client.getTopLevelWorldView().worldEntities().byIndex(scene.getWorldViewId());
               return we.getOwnerType() != 1;
            }
         } else {
            return true;
         }
      } else {
         NPC npc = (NPC)renderable;
         if (npc.getComposition().isFollower() && npc != this.client.getFollower()) {
            return !this.hidePets;
         } else if (this.npcUtil.isDying(npc) && this.hideDeadNpcs) {
            return false;
         } else if (npc.getInteracting() == this.client.getLocalPlayer()) {
            boolean b = this.hideAttackers;
            if (this.hideNPCs2D || this.hideNPCs) {
               b &= drawingUI ? this.hideNPCs2D : this.hideNPCs;
            }

            return !b;
         } else if (THRALL_IDS.contains(npc.getId())) {
            return !this.hideThralls;
         } else if (RANDOM_EVENT_NPC_IDS.contains(npc.getId())) {
            return !this.hideRandomEvents;
         } else {
            label267: {
               if (drawingUI) {
                  if (!this.hideNPCs2D) {
                     break label267;
                  }
               } else if (!this.hideNPCs) {
                  break label267;
               }

               var10000 = false;
               return var10000;
            }

            var10000 = true;
            return var10000;
         }
      }
   }
}
