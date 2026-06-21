package osrs;

import net.runelite.api.NpcOverrides;

public class aL implements NpcOverrides {
   public boolean a = false;
   public long b;
   public int[] c;
   public short[] d;
   public short[] e;

   public aL(long var1, int[] var3, short[] var4, short[] var5, boolean var6) {
      this.b = var1;
      this.c = var3;
      this.d = var4;
      this.e = var5;
      this.a = var6;
   }

   public short[] getColorToReplaceWith() {
      return this.d;
   }

   public short[] getTextureToReplaceWith() {
      return this.e;
   }

   public int[] getModelIds() {
      return this.c;
   }

   public boolean useLocalPlayer() {
      return this.a;
   }
}
