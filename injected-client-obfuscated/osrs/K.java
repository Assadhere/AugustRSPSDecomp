package osrs;

public class K implements fF {
   public static final K a = new K(15, 15);
   public static final K b = new K(16, 16);
   public static final K c = new K(17, 17);
   public static final K d = new K(18, 18);
   public static final K e = new K(19, 19);
   public static final K f = new K(20, 20);
   public static final K g = new K(21, 21);
   public static final K h = new K(22, 22);
   public static final K i = new K(23, 23);
   public static final K j = new K(24, 24);
   public static final K k = new K(25, 25);
   public static final K l = new K(26, 26);
   public static final K m = new K(27, 27);
   public final int n;
   public static final K[] o;
   public final int p;
   public static final K q = new K(0, 0);
   public static final K r = new K(1, 1);
   public static final K s = new K(2, 2);
   public static final K t = new K(3, 3);
   public static final K u = new K(4, 4);
   public static final K v = new K(5, 5);
   public static final K w = new K(6, 6);
   public static final K x = new K(7, 7);
   public static final K y = new K(8, 8);
   public static final K z = new K(9, 9);
   public static final K A = new K(10, 10);
   public static final K B = new K(11, 11);
   public static final K C = new K(12, 12);
   public static final K D = new K(13, 13);
   public static final K E = new K(14, 14);

   public static K[] a() {
      return new K[]{q, r, s, t, u, v, w, x, y, z, A, B, C, D, E, a, b, c, d, e, f, g, h, i, j, k, l, m};
   }

   public K(int var1, int var2) {
      this.n = var1;
      this.p = var2;
   }

   public static double a(long var0, long var2, K var4) {
      float var5;
      if (var2 > 0L) {
         float var6 = (float)var0 / (float)var2;
         float var7 = Math.max(0.0F, Math.min(var6, 1.0F));
         var5 = var7;
      } else {
         var5 = 1.0F;
      }

      double var28 = (double)var5;
      if (!(var28 <= 0.0) && !(var28 >= 1.0)) {
         switch (var4.n) {
            case 0:
            default:
               return var28;
            case 1:
               return 1.0 - Math.cos(var28 * Math.PI / 2.0);
            case 2:
               return Math.sin(var28 * Math.PI / 2.0);
            case 3:
               return -(Math.cos(var28 * Math.PI) - 1.0) / 2.0;
            case 4:
               return var28 * var28;
            case 5:
               return 1.0 - (1.0 - var28) * (1.0 - var28);
            case 6:
               return var28 < 0.5 ? var28 * 2.0 * var28 : 1.0 - Math.pow(var28 * -2.0 + 2.0, 2.0) / 2.0;
            case 7:
               return var28 * var28 * var28;
            case 8:
               return 1.0 - Math.pow(1.0 - var28, 3.0);
            case 9:
               return var28 < 0.5 ? var28 * 4.0 * var28 * var28 : 1.0 - Math.pow(var28 * -2.0 + 2.0, 3.0) / 2.0;
            case 10:
               return var28 * var28 * var28 * var28;
            case 11:
               return 1.0 - Math.pow(1.0 - var28, 4.0);
            case 12:
               return var28 < 0.5 ? var28 * 8.0 * var28 * var28 * var28 : 1.0 - Math.pow(var28 * -2.0 + 2.0, 4.0) / 2.0;
            case 13:
               return var28 * var28 * var28 * var28 * var28;
            case 14:
               return 1.0 - Math.pow(1.0 - var28, 5.0);
            case 15:
               return var28 < 0.5 ? var28 * 8.0 * var28 * var28 * var28 * var28 : 1.0 - Math.pow(var28 * -2.0 + 2.0, 5.0) / 2.0;
            case 16:
               return Math.pow(2.0, var28 * 10.0 - 10.0);
            case 17:
               return 1.0 - Math.pow(2.0, var28 * -10.0);
            case 18:
               return var28 < 0.5 ? Math.pow(2.0, var28 * 20.0 + 10.0) / 2.0 : (2.0 - Math.pow(2.0, var28 * -20.0 + 10.0)) / 2.0;
            case 19:
               return 1.0 - Math.sqrt(1.0 - Math.pow(var28, 2.0));
            case 20:
               return Math.sqrt(1.0 - Math.pow(var28 - 1.0, 2.0));
            case 21:
               return var28 < 0.5 ? (1.0 - Math.sqrt(1.0 - Math.pow(var28 * 2.0, 2.0))) / 2.0 : (Math.sqrt(1.0 - Math.pow(var28 * -2.0 + 2.0, 2.0)) + 1.0) / 2.0;
            case 22:
               double var8 = 1.70158;
               double var10 = 2.70158;
               return var28 * 2.70158 * var28 * var28 - var28 * 1.70158 * var28;
            case 23:
               double var12 = 1.70158;
               double var14 = 2.70158;
               return 1.0 + 2.70158 * Math.pow(var28 - 1.0, 3.0) + 1.70158 * Math.pow(var28 - 1.0, 2.0);
            case 24:
               double var16 = 1.70158;
               double var18 = 2.5949095;
               return var28 < 0.5 ? Math.pow(var28 * 2.0, 2.0) * (var28 * 7.189819 - 2.5949095) / 2.0 : (Math.pow(var28 * 2.0 - 2.0, 2.0) * ((var28 * 2.0 - 2.0) * 3.5949095 + 2.5949095) + 2.0) / 2.0;
            case 25:
               double var20 = 2.0943951023931953;
               return -Math.pow(2.0, var28 * 10.0 - 10.0) * Math.sin((var28 * 10.0 - 10.75) * 2.0943951023931953);
            case 26:
               double var22 = 2.0943951023931953;
               return Math.pow(2.0, var28 * -10.0) * Math.sin((var28 * 10.0 - 0.75) * 2.0943951023931953) + 1.0;
            case 27:
               double var24 = 1.3962634015954636;
               double var26 = Math.sin((var28 * 20.0 - 11.125) * 1.3962634015954636);
               return var28 < 0.5 ? -(Math.pow(2.0, var28 * 20.0 - 10.0) * var26) / 2.0 : Math.pow(2.0, var28 * -20.0 + 10.0) * var26 / 2.0 + 1.0;
         }
      } else {
         return var28 <= 0.0 ? 0.0 : 1.0;
      }
   }

   public int b() {
      return this.p;
   }

   public static K a(int var0) {
      return o[var0];
   }

   static {
      K[] var0 = a();
      o = new K[var0.length];
      K[] var1 = var0;

      for(int var2 = 0; var2 < var1.length; ++var2) {
         K var3 = var1[var2];
         o[var3.p] = var3;
      }

   }
}
