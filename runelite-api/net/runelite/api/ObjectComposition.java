package net.runelite.api;

public interface ObjectComposition extends ParamHolder {
   int getId();

   String getName();

   EntityOps getOps();

   String[] getActions();

   int getMapSceneId();

   void setMapSceneId(int var1);

   int getMapIconId();

   void setMapIconId(int var1);

   int[] getImpostorIds();

   ObjectComposition getImpostor();

   int getVarbitId();

   int getVarPlayerId();

   int[] getObjectModels();

   int getSizeX();

   int getSizeY();
}
