package net.runelite.api;

import javax.annotation.Nullable;
import net.runelite.api.kit.KitType;

public interface PlayerComposition {
   int KIT_OFFSET = 256;
   int ITEM_OFFSET = 2048;

   /** @deprecated */
   @Deprecated
   boolean isFemale();

   int getGender();

   int[] getColors();

   int[] getEquipmentIds();

   int getEquipmentId(KitType var1);

   int getKitId(KitType var1);

   void setHash();

   int getTransformedNpcId();

   void setTransformedNpcId(int var1);

   @Nullable
   ColorTextureOverride[] getColorTextureOverrides();

   @Nullable
   ColorTextureOverride getColorTextureOverride(KitType var1);

   ColorTextureOverride createColorTextureOverride(KitType var1, int var2);

   void removeColorTextureOverride(KitType var1);

   void setSlotModelOverride(KitType var1, ModelData var2);

   void clearSlotModelOverride(KitType var1);

   void clearAllSlotModelOverrides();

   boolean hasAnySlotModelOverride();
}
