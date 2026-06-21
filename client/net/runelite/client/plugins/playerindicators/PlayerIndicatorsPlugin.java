package net.runelite.client.plugins.playerindicators;

import com.google.common.annotations.VisibleForTesting;
import com.google.inject.Provides;
import java.lang.reflect.Type;
import java.util.Iterator;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.FriendsChatRank;
import net.runelite.api.MenuAction;
import net.runelite.api.MenuEntry;
import net.runelite.api.Player;
import net.runelite.api.events.ClientTick;
import net.runelite.api.events.ScriptPostFired;
import net.runelite.api.widgets.Widget;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ProfileChanged;
import net.runelite.client.game.ChatIconManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;
import net.runelite.client.util.ColorUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@PluginDescriptor(
   name = "Player Indicators",
   description = "Highlight players on-screen and/or on the minimap",
   tags = {"highlight", "minimap", "overlay", "players"}
)
public class PlayerIndicatorsPlugin extends Plugin {
   private static final Logger log = LoggerFactory.getLogger(PlayerIndicatorsPlugin.class);
   private static final String TRADING_WITH_TEXT = "Trading with: ";
   @Inject
   private OverlayManager overlayManager;
   @Inject
   private PlayerIndicatorsConfig config;
   @Inject
   private PlayerIndicatorsOverlay playerIndicatorsOverlay;
   @Inject
   private PlayerIndicatorsTileOverlay playerIndicatorsTileOverlay;
   @Inject
   private PlayerIndicatorsMinimapOverlay playerIndicatorsMinimapOverlay;
   @Inject
   private PlayerIndicatorsService playerIndicatorsService;
   @Inject
   private Client client;
   @Inject
   private ChatIconManager chatIconManager;
   @Inject
   private ClientThread clientThread;
   @Inject
   private ConfigManager configManager;

   @Provides
   PlayerIndicatorsConfig provideConfig(ConfigManager configManager) {
      return (PlayerIndicatorsConfig)configManager.getConfig(PlayerIndicatorsConfig.class);
   }

   protected void startUp() throws Exception {
      this.overlayManager.add(this.playerIndicatorsOverlay);
      this.overlayManager.add(this.playerIndicatorsTileOverlay);
      this.overlayManager.add(this.playerIndicatorsMinimapOverlay);
      this.migrate();
   }

   protected void shutDown() throws Exception {
      this.overlayManager.remove(this.playerIndicatorsOverlay);
      this.overlayManager.remove(this.playerIndicatorsTileOverlay);
      this.overlayManager.remove(this.playerIndicatorsMinimapOverlay);
   }

   @Subscribe
   public void onProfileChanged(ProfileChanged profileChanged) {
      this.migrate();
   }

   private void migrate() {
      String[] keys = new String[]{"drawOwnName", "highlightSelf", "drawPartyMembers", "highlightPartyMembers", "drawFriendNames", "highlightFriends", "drawClanMemberNames", "highlightFriendsChat", "drawTeamMemberNames", "highlightTeamMembers", "drawClanChatMemberNames", "highlightClanMembers", "drawNonClanMemberNames", "highlightOthers"};
      Boolean disableOutsidePvP = (Boolean)this.configManager.getConfiguration("playerindicators", "disableOutsidePvP", (Type)Boolean.class);
      if (disableOutsidePvP != null) {
         this.configManager.unsetConfiguration("playerindicators", "disableOutsidePvP");

         for(int i = 0; i < keys.length; i += 2) {
            String old = keys[i];
            String new_ = keys[i + 1];
            Boolean value = (Boolean)this.configManager.getConfiguration("playerindicators", old, (Type)Boolean.class);
            if (value != null) {
               PlayerIndicatorsConfig.HighlightSetting newSetting;
               if (disableOutsidePvP && value) {
                  newSetting = PlayerIndicatorsConfig.HighlightSetting.PVP;
               } else if (value) {
                  newSetting = PlayerIndicatorsConfig.HighlightSetting.ENABLED;
               } else {
                  newSetting = PlayerIndicatorsConfig.HighlightSetting.DISABLED;
               }

               this.configManager.setConfiguration("playerindicators", new_, (Object)newSetting);
               this.configManager.unsetConfiguration("playerindicators", old);
            }
         }

         log.debug("Migrated player indicators config");
      }

   }

   @Subscribe
   public void onClientTick(ClientTick clientTick) {
      if (!this.client.isMenuOpen()) {
         MenuEntry[] menuEntries = this.client.getMenuEntries();
         MenuEntry[] var3 = menuEntries;
         int var4 = menuEntries.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            MenuEntry entry = var3[var5];
            MenuAction type = entry.getType();
            if (type == MenuAction.WALK || type == MenuAction.WIDGET_TARGET_ON_PLAYER || type == MenuAction.ITEM_USE_ON_PLAYER || type == MenuAction.PLAYER_FIRST_OPTION || type == MenuAction.PLAYER_SECOND_OPTION || type == MenuAction.PLAYER_THIRD_OPTION || type == MenuAction.PLAYER_FOURTH_OPTION || type == MenuAction.PLAYER_FIFTH_OPTION || type == MenuAction.PLAYER_SIXTH_OPTION || type == MenuAction.PLAYER_SEVENTH_OPTION || type == MenuAction.PLAYER_EIGHTH_OPTION || type == MenuAction.RUNELITE_PLAYER) {
               Player player = entry.getPlayer();
               if (player != null) {
                  PlayerIndicatorsService.Decorations decorations = this.playerIndicatorsService.getDecorations(player);
                  if (decorations != null) {
                     String oldTarget = entry.getTarget();
                     String newTarget = this.decorateTarget(oldTarget, decorations);
                     entry.setTarget(newTarget);
                  }
               }
            }
         }

      }
   }

   @VisibleForTesting
   String decorateTarget(String oldTarget, PlayerIndicatorsService.Decorations decorations) {
      String newTarget = oldTarget;
      int image;
      if (decorations.getColor() != null && this.config.colorPlayerMenu()) {
         String prefix = "";
         image = oldTarget.indexOf("->");
         if (image != -1) {
            prefix = oldTarget.substring(0, image + 3);
            oldTarget = oldTarget.substring(image + 3);
         }

         image = oldTarget.indexOf(62);
         oldTarget = oldTarget.substring(image + 1);
         newTarget = prefix + ColorUtil.prependColorTag(oldTarget, decorations.getColor());
      }

      FriendsChatRank rank = decorations.getFriendsChatRank();
      image = -1;
      if (rank != null && rank != FriendsChatRank.UNRANKED && this.config.showFriendsChatRanks()) {
         image = this.chatIconManager.getIconNumber(rank);
      } else if (decorations.getClanTitle() != null && this.config.showClanChatRanks()) {
         image = this.chatIconManager.getIconNumber(decorations.getClanTitle());
      }

      if (image != -1) {
         newTarget = "<img=" + image + ">" + newTarget;
      }

      return newTarget;
   }

   @Subscribe
   public void onScriptPostFired(ScriptPostFired event) {
      if (event.getScriptId() == 755) {
         this.clientThread.invokeLater(() -> {
            Widget tradeTitle = this.client.getWidget(21954591);
            String header = tradeTitle.getText();
            String playerName = header.substring("Trading with: ".length());
            Player targetPlayer = this.findPlayer(playerName);
            if (targetPlayer != null) {
               PlayerIndicatorsService.Decorations decorations = this.playerIndicatorsService.getDecorations(targetPlayer);
               if (decorations != null && decorations.getColor() != null) {
                  String var10001 = ColorUtil.wrapWithColorTag(playerName, decorations.getColor());
                  tradeTitle.setText("Trading with: " + var10001);
               }

            }
         });
      }

   }

   private Player findPlayer(String name) {
      Iterator var2 = this.client.getPlayers().iterator();

      Player player;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         player = (Player)var2.next();
      } while(!player.getName().equals(name));

      return player;
   }
}
