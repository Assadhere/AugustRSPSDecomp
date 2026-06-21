package net.runelite.api;

public interface IndexDataBase {
   boolean isOverlayOutdated();

   int[] getFileIds(int var1);

   byte[] loadData(int var1, int var2);
}
