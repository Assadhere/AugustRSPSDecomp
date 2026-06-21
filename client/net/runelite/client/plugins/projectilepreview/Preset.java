package net.runelite.client.plugins.projectilepreview;

enum Preset {
   TD_ELEMENTAL_TOWER_SPELL(80, 36, 41, 5, 11),
   PRIME_MAGIC_ATTACK(80, 31, 15, 16, 64),
   BOLT(38, 36, 41, 5, 11),
   ARROW(40, 36, 41, 15, 11),
   JAVELIN(38, 36, 42, 1, 120),
   THROWN(40, 36, 32, 15, 11),
   EXECUTIONERS_AXE(25, 15, 30, 10, 30),
   LUMINARY_PROC(25, 25, 30, 1, 120),
   CHINCHOMPA(40, 36, 21, 15, 11),
   REPRISAL(40, 36, 21, 15, 11),
   MAGIC(100, 31, 41, 16, 64),
   TUMEKENS_SHADOW(62, 31, 56, 32, 40),
   EYE_OF_AYAK(62, 24, 51, 16, 64),
   TEPHRA(43, 31, 35, 16, 64),
   MAIDEN_TORNADO(0, 0, 0, 0, 0),
   BLOAT_FLIES(50, 30, 0, 0, 0),
   XARP_POISON(0, 0, 15, 45, 0),
   VERZIK_MAGIC(100, 24, 15, 0, 0),
   BREACH_BOSS_NIGHTMARE_RANGED(400, 30, 15, 15, 127),
   BREACH_BOSS_NIGHTMARE_MAGE(400, 30, 0, 15, 127),
   BREACH_BOSS_KBD(150, 30, 15, 0, 0),
   BREACH_BOSS_REV_MALEDICTUS(200, 30, 15, 15, 127),
   MIMIC(120, 175, 0, 16, 127),
   IMP_MINION_RANGED(30, 30, 30, 25, 0),
   IMP_MINION_MAGIC(30, 30, 30, 25, 0),
   THRALL_RANGED(30, 30, 30, 25, 0),
   THRALL_MAGIC(30, 30, 30, 25, 0);

   final int startHeight;
   final int endHeight;
   final int delay;
   final int angle;
   final int progress;

   private Preset(int startHeight, int endHeight, int delay, int angle, int progress) {
      this.startHeight = startHeight;
      this.endHeight = endHeight;
      this.delay = delay;
      this.angle = angle;
      this.progress = progress;
   }
}
