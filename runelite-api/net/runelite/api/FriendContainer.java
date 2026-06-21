package net.runelite.api;

public interface FriendContainer extends NameableContainer<Friend> {
   Deque<PendingLogin> getPendingLogins();
}
