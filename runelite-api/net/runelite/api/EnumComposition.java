package net.runelite.api;

public interface EnumComposition {
   int size();

   int[] getKeys();

   int[] getIntVals();

   long[] getLongVals();

   String[] getStringVals();

   int getIntValue(int var1);

   String getStringValue(int var1);

   long getLongValue(int var1);
}
