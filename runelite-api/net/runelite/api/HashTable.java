package net.runelite.api;

public interface HashTable<T extends Node> extends Iterable<T> {
   T get(long var1);
}
