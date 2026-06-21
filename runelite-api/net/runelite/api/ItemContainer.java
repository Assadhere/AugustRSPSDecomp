package net.runelite.api;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface ItemContainer extends Node {
   int getId();

   @Nonnull
   Item[] getItems();

   @Nullable
   Item getItem(int var1);

   boolean contains(int var1);

   int count(int var1);

   int size();

   int count();

   int find(int var1);
}
