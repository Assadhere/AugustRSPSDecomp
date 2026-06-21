package net.runelite.api;

public interface Nameable extends Comparable<Nameable> {
   String getName();

   String getPrevName();
}
