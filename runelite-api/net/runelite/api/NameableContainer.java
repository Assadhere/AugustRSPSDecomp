package net.runelite.api;

public interface NameableContainer<T extends Nameable> {
   int getCount();

   int getSize();

   T[] getMembers();

   T findByName(String var1);
}
