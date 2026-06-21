package net.runelite.api;

public interface ParamHolder {
   IterableHashTable<Node> getParams();

   int getIntValue(int var1);

   void setValue(int var1, int var2);

   String getStringValue(int var1);

   void setValue(int var1, String var2);

   long getLongValue(int var1);

   void setValue(int var1, long var2);
}
