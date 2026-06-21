package net.runelite.api;

import javax.annotation.Nullable;

public interface EntityOps {
   int MAX_OPS = 5;

   @Nullable
   String getOp(int var1);

   int getNumSubOps(int var1);

   int getSubID(int var1, int var2);

   @Nullable
   String getSubOp(int var1, int var2);
}
