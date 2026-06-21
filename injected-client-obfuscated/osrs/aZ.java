package osrs;

import java.util.Arrays;
import net.runelite.api.ColorTextureOverride;
import net.runelite.api.ModelData;
import net.runelite.api.PlayerComposition;
import net.runelite.api.kit.KitType;

public class aZ implements PlayerComposition {
   public long a;
   public long b;
   public cF[] c;
   public boolean d = false;
   public static eI e = new eI(260);
   public static gJ f;
   public static int g;
   public int h = -1;
   public int i = 0;
   public int[] j;
   public int[] k;
   public int[] l;
   public int m;
   private ModelUnlit[] n;

   public aZ() {
   }

   public aZ(aZ var1) {
      if (var1 != null) {
         int[] var2 = Arrays.copyOf(var1.j, var1.j.length);
         int[] var3 = Arrays.copyOf(var1.k, var1.k.length);
         cF[] var4 = var1.c != null ? (cF[])Arrays.copyOf(var1.c, var1.c.length) : null;
         int[] var5 = Arrays.copyOf(var1.l, var1.l.length);
         this.a(var3, var2, var4, false, var5, var1.i, var1.m, var1.h);
      }

   }

   public void a(int[] var1, int[] var2, cF[] var3, boolean var4, int[] var5, int var6, int var7, int var8) {
      this.c = var3;
      this.d = var4;
      this.h = var8;
      this.a(var1, var2, var5, var6, var7);
   }

   public void a(int[] var1, int[] var2, int[] var3, int var4, int var5) {
      if (var1 == null) {
         var1 = this.a(var4);
      }

      if (var2 == null) {
         var2 = this.a(var4);
      }

      this.k = var1;
      this.j = var2;
      this.l = var3;
      this.i = var4;
      this.m = var5;
      this.a();
   }

   public int[] a(int var1) {
      int[] var2 = new int[12];

      for(int var3 = 0; var3 < 7; ++var3) {
         for(int var4 = 0; var4 < af.a; ++var4) {
            af var5 = af.a(var4);
            if (var5 != null && !var5.k && var5.a(var3, var1)) {
               var2[da.a(var3)] = var4 + 256;
               break;
            }
         }
      }

      return var2;
   }

   public void a(int var1, boolean var2) {
      int var3 = this.j[da.a(var1)];
      if (var3 != 0) {
         var3 -= 256;

         af var4;
         do {
            do {
               do {
                  if (!var2) {
                     --var3;
                     if (var3 < 0) {
                        var3 = af.a - 1;
                     }
                  } else {
                     ++var3;
                     if (var3 >= af.a) {
                        var3 = 0;
                     }
                  }

                  var4 = af.a(var3);
               } while(var4 == null);
            } while(var4.k);
         } while(!var4.a(var1, this.i));

         this.j[da.a(var1)] = var3 + 256;
         this.a();
      }

   }

   public void b(int var1, boolean var2) {
      int var3 = this.l[var1];
      if (!var2) {
         do {
            --var3;
            if (var3 < 0) {
               var3 = bo.bI[var1].length - 1;
            }
         } while(!fj.a(var1, var3));
      } else {
         do {
            ++var3;
            if (var3 >= bo.bI[var1].length) {
               var3 = 0;
            }
         } while(!fj.a(var1, var3));
      }

      this.l[var1] = var3;
      this.a();
   }

   public void b(int var1) {
      if (this.i != var1) {
         this.a((int[])null, (int[])null, this.l, var1, -1);
      }

   }

   public void a(aR var1) {
      var1.a(this.i);

      int var2;
      for(var2 = 0; var2 < 7; ++var2) {
         int var3 = this.j[da.a(var2)];
         if (var3 == 0) {
            var1.b(-1);
         } else {
            var1.b(var3 - 256);
         }
      }

      for(var2 = 0; var2 < 5; ++var2) {
         var1.a(this.l[var2]);
      }

   }

   public void a() {
      long var1 = this.a;
      long[] var3 = aR.a;
      this.a = -1L;

      int var4;
      for(var4 = 0; var4 < 12; ++var4) {
         this.a = this.a >>> 8 ^ var3[(int)((this.a ^ (long)(this.j[var4] >> 24)) & 255L)];
         this.a = this.a >>> 8 ^ var3[(int)((this.a ^ (long)(this.j[var4] >> 16)) & 255L)];
         this.a = this.a >>> 8 ^ var3[(int)((this.a ^ (long)(this.j[var4] >> 8)) & 255L)];
         this.a = this.a >>> 8 ^ var3[(int)((this.a ^ (long)this.j[var4]) & 255L)];
      }

      int var5;
      if (this.c != null) {
         for(var4 = 0; var4 < this.c.length; ++var4) {
            if (this.c[var4] != null) {
               if (this.c[var4].e != null) {
                  for(var5 = 0; var5 < this.c[var4].e.length; ++var5) {
                     this.a = this.a >>> 8 ^ var3[(int)((this.a ^ (long)(this.c[var4].e[var5] >> 8)) & 255L)];
                     this.a = this.a >>> 8 ^ var3[(int)((this.a ^ (long)this.c[var4].e[var5]) & 255L)];
                  }
               }

               if (this.c[var4].f != null) {
                  for(var5 = 0; var5 < this.c[var4].f.length; ++var5) {
                     this.a = this.a >>> 8 ^ var3[(int)((this.a ^ (long)(this.c[var4].f[var5] >> 8)) & 255L)];
                     this.a = this.a >>> 8 ^ var3[(int)((this.a ^ (long)this.c[var4].f[var5]) & 255L)];
                  }
               }
            }
         }
      }

      for(var4 = 0; var4 < 5; ++var4) {
         this.a = this.a >>> 8 ^ var3[(int)((this.a ^ (long)this.l[var4]) & 255L)];
      }

      this.a = this.a >>> 8 ^ var3[(int)((this.a ^ (long)this.i) & 255L)];
      if (this.n != null) {
         for(var4 = 0; var4 < this.n.length; ++var4) {
            if (this.n[var4] != null) {
               var5 = System.identityHashCode(this.n[var4]);
               this.a = this.a >>> 8 ^ var3[(int)((this.a ^ (long)(var5 >> 24)) & 255L)];
               this.a = this.a >>> 8 ^ var3[(int)((this.a ^ (long)(var5 >> 16)) & 255L)];
               this.a = this.a >>> 8 ^ var3[(int)((this.a ^ (long)(var5 >> 8)) & 255L)];
               this.a = this.a >>> 8 ^ var3[(int)((this.a ^ (long)var5) & 255L)];
            }
         }
      }

      if (var1 != 0L && this.a != var1 || this.d) {
         e.b(var1);
      }

   }

   public aH a(bk var1, int var2, bk var3, int var4) {
      if (this.m != -1) {
         return aN.a(this.m).a(var1, var2, var3, var4, (aL)null);
      } else {
         long var5 = this.a;
         int[] var7 = this.j;
         if (var1 != null && (var1.w >= 0 || var1.x >= 0)) {
            var7 = new int[12];
            System.arraycopy(this.j, 0, var7, 0, var7.length);
            if (var1.w >= 0) {
               var5 ^= (long)(var1.w - this.j[ds.g.a]) << 40;
               var7[ds.g.a] = this.h(var1.w);
            }

            if (var1.x >= 0) {
               var5 ^= (long)(var1.x - this.j[ds.e.a]) << 48;
               var7[ds.e.a] = this.h(var1.x);
            }
         }

         aH var8 = (aH)e.a(var5);
         if (var8 == null) {
            boolean var9 = false;

            int var11;
            for(int var10 = 0; var10 < 12; ++var10) {
               if (this.n == null || this.n[var10] == null) {
                  var11 = var7[var10];
                  if (this.d(var11)) {
                     af var12 = this.e(var11);
                     if (var12 != null && !var12.b()) {
                        var9 = true;
                     }
                  }

                  if (this.f(var11)) {
                     cF var19 = this.c == null ? null : this.c[var10];
                     if (!this.g(var11).a(this.i, var19)) {
                        var9 = true;
                     }
                  }
               }
            }

            if (var9) {
               if (this.b != -1L) {
                  var8 = (aH)e.a(this.b);
               }

               if (var8 == null) {
                  return null;
               }
            }

            if (var8 == null) {
               ModelUnlit[] var18 = new ModelUnlit[12];
               var11 = 0;

               int var13;
               for(int var20 = 0; var20 < 12; ++var20) {
                  if (this.n != null && this.n[var20] != null) {
                     var18[var11++] = this.n[var20];
                  } else {
                     var13 = var7[var20];
                     if (this.d(var13)) {
                        af var14 = this.e(var13);
                        ModelUnlit var15 = null;
                        if (var14 != null) {
                           var15 = var14.c();
                        }

                        if (var15 != null) {
                           var18[var11++] = var15;
                        }
                     }

                     if (this.f(var13)) {
                        aO var22 = this.g(var13);
                        cF var23 = this.c == null ? null : this.c[var20];
                        ModelUnlit var16 = var22.b(this.i, var23);
                        if (var16 != null) {
                           var18[var11++] = var16;
                        }
                     }
                  }
               }

               ModelUnlit var21 = new ModelUnlit(var18, var11);

               for(var13 = 0; var13 < 5; ++var13) {
                  if (this.l[var13] < bo.bI[var13].length) {
                     var21.a(bo.bH[var13], bo.bI[var13][this.l[var13]]);
                  }

                  if (this.l[var13] < bo.dt[var13].length) {
                     var21.a(bo.bJ[var13], bo.dt[var13][this.l[var13]]);
                  }
               }

               var8 = var21.a(64, 850, -30, -50, -30);
               e.a(var8, var5);
               this.b = var5;
            }
         }

         aH var17;
         if (var1 == null && var3 == null) {
            var17 = var8.a(true);
         } else if (var1 != null && var3 != null) {
            var17 = var1.b(var8, var2, var3, var4);
         } else if (var1 != null) {
            var17 = var1.a(var8, var2);
         } else {
            var17 = var3.a(var8, var4);
         }

         return var17;
      }
   }

   public ModelUnlit b() {
      if (this.m != -1) {
         return aN.a(this.m).a((aL)null);
      } else {
         boolean var1 = false;

         int var3;
         for(int var2 = 0; var2 < 12; ++var2) {
            if (this.n == null || this.n[var2] == null) {
               var3 = this.j[var2];
               if (this.d(var3)) {
                  af var4 = this.e(var3);
                  if (var4 != null && !var4.d()) {
                     var1 = true;
                  }
               }

               if (this.f(var3)) {
                  cF var9 = this.c == null ? null : this.c[var2];
                  if (!this.g(var3).c(this.i, var9)) {
                     var1 = true;
                  }
               }
            }
         }

         if (var1) {
            return null;
         } else {
            ModelUnlit[] var8 = new ModelUnlit[12];
            var3 = 0;

            int var5;
            for(int var10 = 0; var10 < 12; ++var10) {
               if (this.n != null && this.n[var10] != null) {
                  var8[var3++] = this.n[var10];
               } else {
                  var5 = this.j[var10];
                  ModelUnlit var7;
                  if (this.d(var5)) {
                     af var6 = this.e(var5);
                     var7 = null;
                     if (var6 != null) {
                        var7 = var6.e();
                     }

                     if (var7 != null) {
                        var8[var3++] = var7;
                     }
                  }

                  if (this.f(var5)) {
                     cF var12 = this.c == null ? null : this.c[var10];
                     var7 = this.g(var5).d(this.i, var12);
                     if (var7 != null) {
                        var8[var3++] = var7;
                     }
                  }
               }
            }

            ModelUnlit var11 = new ModelUnlit(var8, var3);

            for(var5 = 0; var5 < 5; ++var5) {
               if (this.l[var5] < bo.bI[var5].length) {
                  var11.a(bo.bH[var5], bo.bI[var5][this.l[var5]]);
               }

               if (this.l[var5] < bo.dt[var5].length) {
                  var11.a(bo.bJ[var5], bo.dt[var5][this.l[var5]]);
               }
            }

            return var11;
         }
      }
   }

   public int c() {
      long var1 = this.a;
      if (this.m != -1) {
         var1 = -65536L | (long)this.m;
      }

      Integer var3 = (Integer)f.a(var1);
      if (var3 == null) {
         var3 = ++g - 1;
         f.a(var1, var3);
         g %= 65535;
      }

      return var3;
   }

   public void d() {
      this.a(this.k, this.j, this.l, this.i, this.m);
   }

   public void a(int var1, int var2) {
      boolean var3 = this.i != var2;
      this.i = var2;
      if (var3) {
         int var4;
         int var5;
         if (this.i == var1) {
            for(var4 = 0; var4 < 7; ++var4) {
               var5 = da.a(var4);
               if (this.j[var5] > 0 && this.j[var5] < 2048) {
                  this.j[var5] = this.k[var5];
               }
            }
         } else {
            if (this.j[0] < 2048 || this.e()) {
               this.j[ds.m.a] = 1;
            }

            for(var4 = 0; var4 < 7; ++var4) {
               var5 = da.a(var4);
               if (this.j[var5] > 0 && this.j[var5] < 2048) {
                  int[] var6 = this.j;

                  for(int var7 = 0; var7 < af.a; ++var7) {
                     af var8 = af.a(var7);
                     if (var8 != null && !var8.k && var8.a(var4, var2)) {
                        var6[da.a(var4)] = var7 + 256;
                        break;
                     }
                  }
               }
            }
         }
      }

      this.d();
   }

   public boolean e() {
      if (!this.f(this.j[0])) {
         return false;
      } else {
         aO var1 = this.g(this.j[0]);
         return ds.m.a != var1.z && ds.m.a != var1.A;
      }
   }

   public void b(int var1, int var2) {
      this.l[var1] = var2;
      this.d();
   }

   public void c(int var1) {
      aO var2 = aO.a(var1);
      this.j[var2.y] = var1 + 2048;
      if (var2.z != -1) {
         this.j[var2.z] = 0;
      }

      if (var2.A != -1) {
         this.j[var2.A] = 0;
      }

      this.d();
   }

   public boolean d(int var1) {
      return var1 >= 256 && var1 < 2048;
   }

   public af e(int var1) {
      return af.a(var1 - 256);
   }

   public boolean f(int var1) {
      return var1 >= 2048;
   }

   public aO g(int var1) {
      return aO.a(var1 - 2048);
   }

   public int h(int var1) {
      return var1 - 512 + 2048;
   }

   public ColorTextureOverride createColorTextureOverride(KitType var1, int var2) {
      cF var3 = new cF(var2);
      if (this.c == null) {
         this.c = new cF[12];
      }

      this.c[var1.getIndex()] = var3;
      return var3;
   }

   public int getKitId(KitType var1) {
      int var2 = this.getEquipmentIds()[var1.getIndex()];
      return var2 >= 256 && var2 < 2048 ? var2 - 256 : -1;
   }

   public void removeColorTextureOverride(KitType var1) {
      if (this.c != null) {
         this.c[var1.getIndex()] = null;
      }

   }

   public int getEquipmentId(KitType var1) {
      int var2 = this.getEquipmentIds()[var1.getIndex()];
      return var2 < 2048 ? -1 : var2 - 2048;
   }

   public boolean isFemale() {
      return this.getGender() == 1;
   }

   public ColorTextureOverride getColorTextureOverride(KitType var1) {
      return this.c != null ? this.c[var1.getIndex()] : null;
   }

   public ColorTextureOverride[] getColorTextureOverrides() {
      return this.c;
   }

   public int getGender() {
      return this.i;
   }

   public int[] getColors() {
      return this.l;
   }

   public void setHash() {
      this.a();
   }

   public int[] getEquipmentIds() {
      return this.j;
   }

   public void setTransformedNpcId(int var1) {
      this.m = var1;
   }

   public int getTransformedNpcId() {
      return this.m;
   }

   public void setSlotModelOverride(KitType var1, ModelData var2) {
      if (this.n == null) {
         this.n = new ModelUnlit[12];
      }

      this.n[var1.getIndex()] = (ModelUnlit)var2;
      this.a();
   }

   public void clearSlotModelOverride(KitType var1) {
      if (this.n != null) {
         this.n[var1.getIndex()] = null;
      }

      this.a();
   }

   public void clearAllSlotModelOverrides() {
      this.n = null;
      this.a();
   }

   public boolean hasAnySlotModelOverride() {
      if (this.n == null) {
         return false;
      } else {
         ModelUnlit[] var1 = this.n;
         int var2 = var1.length;

         for(int var3 = 0; var3 < var2; ++var3) {
            ModelUnlit var4 = var1[var3];
            if (var4 != null) {
               return true;
            }
         }

         return false;
      }
   }

   String f() {
      if (this.n == null) {
         return "none";
      } else {
         StringBuilder var1 = new StringBuilder();

         for(int var2 = 0; var2 < this.n.length; ++var2) {
            ModelUnlit var3 = this.n[var2];
            if (var3 != null) {
               if (var1.length() > 0) {
                  var1.append("; ");
               }

               var1.append(var2).append('=').append(System.identityHashCode(var3)).append('(').append(var3.getVerticesCount()).append("v/").append(var3.getFaceCount()).append("f)");
            }
         }

         return var1.length() == 0 ? "none" : var1.toString();
      }
   }

   static {
      f = new gJ(16, gm.a);
      g = 0;
   }
}
