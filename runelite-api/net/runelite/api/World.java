package net.runelite.api;

import java.util.EnumSet;

public interface World {
   EnumSet<WorldType> getTypes();

   void setTypes(EnumSet<WorldType> var1);

   int getPlayerCount();

   void setPlayerCount(int var1);

   int getLocation();

   void setLocation(int var1);

   int getIndex();

   void setIndex(int var1);

   int getId();

   void setId(int var1);

   String getActivity();

   void setActivity(String var1);

   String getAddress();

   void setAddress(String var1);
}
