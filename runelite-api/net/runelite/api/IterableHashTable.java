package net.runelite.api;

public interface IterableHashTable<T extends Node> extends Iterable<T> {
   T get(long var1);

   void put(T var1, long var2);
}
