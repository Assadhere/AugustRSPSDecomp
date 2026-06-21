package net.runelite.api;

public enum EquipmentInventorySlot {
   HEAD(0),
   CAPE(1),
   AMULET(2),
   WEAPON(3),
   BODY(4),
   SHIELD(5),
   ARMS(6),
   LEGS(7),
   HAIR(8),
   GLOVES(9),
   BOOTS(10),
   JAW(11),
   RING(12),
   AMMO(13);

   private final int slotIdx;

   private EquipmentInventorySlot(int slotIdx) {
      this.slotIdx = slotIdx;
   }

   public int getSlotIdx() {
      return this.slotIdx;
   }
}
