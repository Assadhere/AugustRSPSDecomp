package net.runelite.client.plugins.playerindicators;

import java.awt.Color;
import java.util.Iterator;
import java.util.function.BiConsumer;
import java.util.function.Predicate;
import javax.inject.Inject;
import javax.inject.Singleton;
import net.runelite.api.Client;
import net.runelite.api.FriendsChatManager;
import net.runelite.api.FriendsChatMember;
import net.runelite.api.FriendsChatRank;
import net.runelite.api.Player;
import net.runelite.api.WorldView;
import net.runelite.api.clan.ClanChannel;
import net.runelite.api.clan.ClanChannelMember;
import net.runelite.api.clan.ClanRank;
import net.runelite.api.clan.ClanSettings;
import net.runelite.api.clan.ClanTitle;
import net.runelite.api.coords.LocalPoint;
import net.runelite.client.party.PartyService;
import net.runelite.client.util.Text;

@Singleton
class PlayerIndicatorsService {
   private final Client client;
   private final PlayerIndicatorsConfig config;
   private final PartyService partyService;

   @Inject
   private PlayerIndicatorsService(Client client, PlayerIndicatorsConfig config, PartyService partyService) {
      this.config = config;
      this.client = client;
      this.partyService = partyService;
   }

   void forEachPlayer(BiConsumer<Player, Decorations> consumer) {
      WorldView wv = this.client.getTopLevelWorldView();
      this.forEachPlayer(consumer, wv);
      Iterator var3 = wv.worldViews().iterator();

      while(var3.hasNext()) {
         WorldView sub = (WorldView)var3.next();
         this.forEachPlayer(consumer, sub);
      }

   }

   private void forEachPlayer(BiConsumer<Player, Decorations> consumer, WorldView wv) {
      Iterator var3 = wv.players().iterator();

      while(var3.hasNext()) {
         Player player = (Player)var3.next();
         LocalPoint lp = player.getLocalLocation();
         if (wv.contains(lp)) {
            Decorations decorations = this.getDecorations(player);
            if (decorations != null && decorations.getColor() != null) {
               consumer.accept(player, decorations);
            }
         }
      }

   }

   Decorations getDecorations(Player player) {
      if (player.getName() == null) {
         return null;
      } else {
         Predicate<PlayerIndicatorsConfig.HighlightSetting> isEnabled = (hs) -> {
            return hs == PlayerIndicatorsConfig.HighlightSetting.ENABLED || hs == PlayerIndicatorsConfig.HighlightSetting.PVP && (this.client.getVarbitValue(5963) == 1 || this.client.getVarbitValue(8121) == 1);
         };
         Color color = null;
         if (player == this.client.getLocalPlayer()) {
            if (isEnabled.test(this.config.highlightOwnPlayer())) {
               color = this.config.getOwnPlayerColor();
            }
         } else if (this.partyService.isInParty() && isEnabled.test(this.config.highlightPartyMembers()) && this.partyService.getMemberByDisplayName(player.getName()) != null) {
            color = this.config.getPartyMemberColor();
         } else if (player.isFriend() && isEnabled.test(this.config.highlightFriends())) {
            color = this.config.getFriendColor();
         } else if (player.isFriendsChatMember() && isEnabled.test(this.config.highlightFriendsChat())) {
            color = this.config.getFriendsChatMemberColor();
         } else if (player.getTeam() > 0 && this.client.getLocalPlayer().getTeam() == player.getTeam() && isEnabled.test(this.config.highlightTeamMembers())) {
            color = this.config.getTeamMemberColor();
         } else if (player.isClanMember() && isEnabled.test(this.config.highlightClanMembers())) {
            color = this.config.getClanMemberColor();
         } else if (!player.isFriendsChatMember() && !player.isClanMember() && isEnabled.test(this.config.highlightOthers())) {
            color = this.config.getOthersColor();
         }

         FriendsChatRank rank = null;
         ClanTitle clanTitle = null;
         if (player.isFriendsChatMember() && this.config.showFriendsChatRanks()) {
            rank = this.getFriendsChatRank(player);
         }

         if (player.isClanMember() && this.config.showClanChatRanks()) {
            clanTitle = this.getClanTitle(player);
         }

         return color == null && rank == null && clanTitle == null ? null : new Decorations(rank, clanTitle, color);
      }
   }

   private ClanTitle getClanTitle(Player player) {
      ClanChannel clanChannel = this.client.getClanChannel();
      ClanSettings clanSettings = this.client.getClanSettings();
      if (clanChannel != null && clanSettings != null) {
         ClanChannelMember member = clanChannel.findMember(player.getName());
         if (member == null) {
            return null;
         } else {
            ClanRank rank = member.getRank();
            return clanSettings.titleForRank(rank);
         }
      } else {
         return null;
      }
   }

   private FriendsChatRank getFriendsChatRank(Player player) {
      FriendsChatManager friendsChatManager = this.client.getFriendsChatManager();
      if (friendsChatManager == null) {
         return FriendsChatRank.UNRANKED;
      } else {
         FriendsChatMember friendsChatMember = (FriendsChatMember)friendsChatManager.findByName(Text.removeTags(player.getName()));
         return friendsChatMember != null ? friendsChatMember.getRank() : FriendsChatRank.UNRANKED;
      }
   }

   static final class Decorations {
      private final FriendsChatRank friendsChatRank;
      private final ClanTitle clanTitle;
      private final Color color;

      public Decorations(FriendsChatRank friendsChatRank, ClanTitle clanTitle, Color color) {
         this.friendsChatRank = friendsChatRank;
         this.clanTitle = clanTitle;
         this.color = color;
      }

      public FriendsChatRank getFriendsChatRank() {
         return this.friendsChatRank;
      }

      public ClanTitle getClanTitle() {
         return this.clanTitle;
      }

      public Color getColor() {
         return this.color;
      }

      public boolean equals(Object o) {
         if (o == this) {
            return true;
         } else if (!(o instanceof Decorations)) {
            return false;
         } else {
            Decorations other;
            label44: {
               other = (Decorations)o;
               Object this$friendsChatRank = this.getFriendsChatRank();
               Object other$friendsChatRank = other.getFriendsChatRank();
               if (this$friendsChatRank == null) {
                  if (other$friendsChatRank == null) {
                     break label44;
                  }
               } else if (this$friendsChatRank.equals(other$friendsChatRank)) {
                  break label44;
               }

               return false;
            }

            Object this$clanTitle = this.getClanTitle();
            Object other$clanTitle = other.getClanTitle();
            if (this$clanTitle == null) {
               if (other$clanTitle != null) {
                  return false;
               }
            } else if (!this$clanTitle.equals(other$clanTitle)) {
               return false;
            }

            Object this$color = this.getColor();
            Object other$color = other.getColor();
            if (this$color == null) {
               if (other$color != null) {
                  return false;
               }
            } else if (!this$color.equals(other$color)) {
               return false;
            }

            return true;
         }
      }

      public int hashCode() {
         int PRIME = true;
         int result = 1;
         Object $friendsChatRank = this.getFriendsChatRank();
         result = result * 59 + ($friendsChatRank == null ? 43 : $friendsChatRank.hashCode());
         Object $clanTitle = this.getClanTitle();
         result = result * 59 + ($clanTitle == null ? 43 : $clanTitle.hashCode());
         Object $color = this.getColor();
         result = result * 59 + ($color == null ? 43 : $color.hashCode());
         return result;
      }

      public String toString() {
         String var10000 = String.valueOf(this.getFriendsChatRank());
         return "PlayerIndicatorsService.Decorations(friendsChatRank=" + var10000 + ", clanTitle=" + String.valueOf(this.getClanTitle()) + ", color=" + String.valueOf(this.getColor()) + ")";
      }
   }
}
