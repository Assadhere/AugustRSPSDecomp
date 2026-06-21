package net.runelite.api;

import javax.annotation.Nullable;

public interface ItemComposition extends ParamHolder {
   String getName();

   String getMembersName();

   void setName(String var1);

   int getId();

   int getNote();

   int getLinkedNoteId();

   int getPlaceholderId();

   int getPlaceholderTemplateId();

   int getPrice();

   int getHaPrice();

   boolean isMembers();

   boolean isStackable();

   boolean isTradeable();

   String[] getInventoryActions();

   String[][] getSubops();

   int getShiftClickActionIndex();

   void setShiftClickActionIndex(int var1);

   int getInventoryModel();

   void setInventoryModel(int var1);

   @Nullable
   short[] getColorToReplace();

   void setColorToReplace(short[] var1);

   @Nullable
   short[] getColorToReplaceWith();

   void setColorToReplaceWith(short[] var1);

   @Nullable
   short[] getTextureToReplace();

   void setTextureToReplace(short[] var1);

   @Nullable
   short[] getTextureToReplaceWith();

   void setTextureToReplaceWith(short[] var1);

   int getXan2d();

   int getYan2d();

   int getZan2d();

   void setXan2d(int var1);

   void setYan2d(int var1);

   void setZan2d(int var1);

   int getEquipmentSlot();

   int getEquipmentSlot2();

   int getMaleModel();

   int getMaleModel1();

   int getFemaleModel();

   int getFemaleModel1();

   int getZoom2d();

   void setZoom2d(int var1);

   int getXOffset2d();

   void setXOffset2d(int var1);

   int getYOffset2d();

   void setYOffset2d(int var1);

   int getAmbient();

   void setAmbient(int var1);

   int getContrast();

   void setContrast(int var1);
}
