package net.runelite.client.plugins.tithefarm;

enum TitheFarmPlantState {
   UNWATERED,
   WATERED,
   DEAD,
   GROWN;

   public static TitheFarmPlantState getState(int objectId) {
      TitheFarmPlantType plantType = TitheFarmPlantType.getPlantType(objectId);
      if (plantType == null) {
         return null;
      } else {
         int baseId = plantType.getBaseId();
         if (objectId == baseId) {
            return GROWN;
         } else {
            switch ((baseId - objectId) % 3) {
               case 0:
                  return UNWATERED;
               case 2:
                  return WATERED;
               default:
                  return DEAD;
            }
         }
      }
   }
}
