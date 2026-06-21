package net.runelite.api;

public interface FriendsChatManager extends NameableContainer<FriendsChatMember> {
   String getOwner();

   String getName();

   FriendsChatRank getMyRank();

   FriendsChatRank getKickRank();
}
