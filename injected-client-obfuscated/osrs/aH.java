package osrs;

import io.sentry.Breadcrumb;
import io.sentry.Sentry;
import io.sentry.SentryLevel;
import java.awt.Shape;
import java.util.Arrays;
import net.runelite.api.AABB;
import net.runelite.api.Model;
import net.runelite.api.Perspective;
import net.runelite.api.model.Jarvis;

public class aH extends dG implements Model {
   public static int[] a = new int[6500];
   public static char[] b = new char[6000];
   public static char[][] c = new char[6000][512];
   public static int[] d = new int[12];
   public static int[][] e = new int[12][2500];
   public static int[] f = new int[2500];
   public static int[] g = new int[2500];
   public static int[] h = new int[12];
   public static boolean i = true;
   public static int[] j;
   public static int[] k;
   public static int[] l;
   public static int[] m;
   public static float[] n;
   public static aH o;
   public static byte[] p;
   public static aH q;
   public static byte[] r;
   public static fX s;
   public static fX t;
   public static fX u;
   public static int[] v;
   public static int[] w;
   public static int[] x;
   public static float[] y;
   public int z = 0;
   public int A = 0;
   public int[] B;
   public int[] C;
   public int[] D;
   public int[] E;
   public int[] F;
   public int[] G;
   public byte[] H;
   public byte[] I;
   public byte[] J;
   public short[] K;
   public byte[] L;
   public byte M = 0;
   public int N = 0;
   public int[] O;
   public int[] P;
   public int[] Q;
   public int[][] R;
   public int[][] S;
   public boolean T = false;
   public int U;
   public int V;
   public int W;
   public int X;
   public int Y;
   public int[][] Z;
   public int[][] aa;
   public dx ab;
   public dH ac = null;
   public dg ad;
   public short ae;
   public float[] af;
   public int[] ag;
   public int ah;
   public int ai;
   public short[] aj;
   public float[] ak;
   public float[] al;
   public static boolean[] am = new boolean[6500];
   public static boolean[] an = new boolean[6500];
   public static boolean[] ao = new boolean[6500];
   public static float[] ap = new float[6500];
   public static float[] aq = new float[6500];
   public static float[] ar = new float[6500];
   public static int[] as = new int[6500];
   public static int[] at = new int[6500];
   public static int[] au = new int[6500];
   private static long aA;
   private static int aB;
   private static int aC;
   private static int aD;
   private static int aE;
   private static long aF;
   private long aG;
   private int aH = -1;
   public static final boolean av;
   public int aw;
   public aH ax;
   public int[] ay;
   public int[] az;

   public aH() {
      this.ad = dg.a;
   }

   private void a(long var1) {
      if (var1 == 0L) {
         this.aG = 0L;
         this.aH = -1;
      } else {
         this.aG = var1;
         this.aH = Client.x;
      }

   }

   private long b(long var1) {
      if (var1 != 0L) {
         return var1;
      } else {
         return this.aH == Client.x ? this.aG : 0L;
      }
   }

   private boolean t() {
      return this.z > 6500 || this.A > 6500 || this.X >= 6000;
   }

   private static int a(int var0, int var1) {
      if (var1 > 65535) {
         return 65535;
      } else {
         int var2 = Math.max(1, var0);

         while(true) {
            while(var2 < var1) {
               int var3 = var2 + Math.max(1, var2 >> 1);
               if (var3 > var2 && var3 <= 65535) {
                  var2 = var3;
               } else {
                  var2 = 65535;
               }
            }

            return var2;
         }
      }
   }

   private static void m(int var0) {
      if (var0 > as.length) {
         int var1 = a(as.length, var0);
         a = Arrays.copyOf(a, var1);
         ap = Arrays.copyOf(ap, var1);
         aq = Arrays.copyOf(aq, var1);
         ar = Arrays.copyOf(ar, var1);
         as = Arrays.copyOf(as, var1);
         at = Arrays.copyOf(at, var1);
         au = Arrays.copyOf(au, var1);
      }
   }

   static void a(int var0) {
      m(var0);
   }

   private static void n(int var0) {
      int var1;
      if (var0 > ao.length) {
         var1 = a(ao.length, var0);
         am = Arrays.copyOf(am, var1);
         an = Arrays.copyOf(an, var1);
         ao = Arrays.copyOf(ao, var1);
      }

      if (var0 > f.length) {
         var1 = a(f.length, var0);

         for(int var2 = 0; var2 < e.length; ++var2) {
            e[var2] = Arrays.copyOf(e[var2], var1);
         }

         f = Arrays.copyOf(f, var1);
         g = Arrays.copyOf(g, var1);
      }

   }

   private static void o(int var0) {
      if (var0 > b.length) {
         int var1 = b.length;
         int var2 = a(var1, var0);
         b = Arrays.copyOf(b, var2);
         c = (char[][])Arrays.copyOf(c, var2);

         for(int var3 = var1; var3 < var2; ++var3) {
            c[var3] = new char[512];
         }

      }
   }

   private static void i(int var0, int var1, int var2) {
      m(var0);
      n(var1);
      o(var2);
   }

   private static void a(int var0, char var1) {
      if (var0 >= 0 && var0 < c.length) {
         char var2 = b[var0];
         if (var2 < '\uffff') {
            char[] var3 = c[var0];
            if (var2 >= var3.length) {
               var3 = Arrays.copyOf(var3, a(var3.length, var2 + 1));
               c[var0] = var3;
            }

            var3[var2] = var1;
            b[var0] = (char)(var2 + 1);
         }
      }
   }

   private static int p(int var0) {
      int var1 = d[var0]++;
      if (var1 >= e[var0].length) {
         int var2 = a(e[var0].length, var1 + 1);
         e[var0] = Arrays.copyOf(e[var0], var2);
         if (var0 == 10) {
            f = Arrays.copyOf(f, var2);
         } else if (var0 == 11) {
            g = Arrays.copyOf(g, var2);
         }
      }

      return var1;
   }

   private static void a(int var0, int var1, int var2, long var3) {
      ++aB;
      aC = Math.max(aC, var0);
      aD = Math.max(aD, var1);
      aE = Math.max(aE, var2);
      if (var3 != 0L) {
         aF = var3;
      }

   }

   private static int c(long var0) {
      return (int)(var0 >>> 16 & 7L);
   }

   private static String q(int var0) {
      switch (var0) {
         case 0:
            return "player";
         case 1:
            return "npc";
         case 2:
            return "loc";
         case 3:
            return "ground_item";
         case 4:
            return "world_entity";
         case 5:
            return "camera";
         default:
            return "unknown";
      }
   }

   private String a(Breadcrumb var1, bG var2, int var3) {
      t var4 = var2 != null ? var2.a(var3) : null;
      if (var4 == null) {
         var1.setData("entityResolved", false);
         var1.setData("entityType", "player");
         var1.setData("playerIndex", var3);
         return "player[" + var3 + "]";
      } else {
         var1.setData("entityResolved", true);
         var1.setData("entityType", "player");
         var1.setData("playerIndex", var4.getId());
         var1.setData("playerName", var4.getName());
         var1.setData("playerCombatLevel", var4.getCombatLevel());
         var1.setData("playerTeam", var4.getTeam());
         var1.setData("playerSkullIcon", var4.getSkullIcon());
         var1.setData("playerX", var4.k());
         var1.setData("playerY", var4.l());
         var1.setData("playerPlane", var4.m());
         var1.setData("playerOrientation", var4.getCurrentOrientation());
         var1.setData("playerFootprintSize", var4.getFootprintSize());
         var1.setData("playerHidden", var4.V());
         StringBuilder var5 = (new StringBuilder("player[")).append(var4.getId()).append("] ").append(var4.getName());
         if (var4.aq != null) {
            var1.setData("playerHasAttachedModel", true);
            var1.setData("playerAttachedModelVertices", var4.aq.z);
            var1.setData("playerAttachedModelFaces", var4.aq.A);
            var5.append(" attachedModel=").append(var4.aq.z).append("v/").append(var4.aq.A).append("f");
         } else {
            var1.setData("playerHasAttachedModel", false);
         }

         aZ var6 = var4.U();
         if (var6 != null) {
            String var7 = this.a(var6);
            String var8 = var6.f();
            var1.setData("playerCompositionHash", var6.a);
            var1.setData("playerGender", var6.getGender());
            var1.setData("playerColors", Arrays.toString(var6.getColors()));
            var1.setData("playerEquipmentIds", Arrays.toString(var6.getEquipmentIds()));
            var1.setData("playerEquipmentSummary", var7);
            var1.setData("playerTransformedNpcId", var6.getTransformedNpcId());
            var1.setData("playerOverrideSummary", this.b(var6));
            var1.setData("playerHasSlotModelOverrides", var6.hasAnySlotModelOverride());
            var1.setData("playerSlotModelOverrideSummary", var8);
            if (!var7.isEmpty()) {
               var5.append(" equipment={").append(var7).append('}');
            }

            if (var6.getTransformedNpcId() != -1) {
               var5.append(" transformedNpc=").append(var6.getTransformedNpcId());
            }

            if (var6.hasAnySlotModelOverride()) {
               var5.append(" slotOverrides={").append(var8).append('}');
            }
         }

         return var5.toString();
      }
   }

   private String b(Breadcrumb var1, bG var2, int var3) {
      s var4 = var2 != null ? (s)var2.s.a((long)var3) : null;
      if (var4 == null) {
         var1.setData("entityResolved", false);
         var1.setData("entityType", "npc");
         var1.setData("npcIndex", var3);
         return "npc[" + var3 + "]";
      } else {
         var1.setData("entityResolved", true);
         var1.setData("entityType", "npc");
         var1.setData("npcIndex", var4.getIndex());
         var1.setData("npcId", var4.getId());
         var1.setData("npcName", var4.getName());
         var1.setData("npcCombatLevel", var4.getCombatLevel());
         var1.setData("npcX", var4.k());
         var1.setData("npcY", var4.l());
         var1.setData("npcPlane", var4.m());
         var1.setData("npcOrientation", var4.getCurrentOrientation());
         var1.setData("npcFootprintSize", var4.getFootprintSize());
         var1.setData("npcUsesLocalPlayerModel", var4.ay != null && var4.ay.a);
         aN var5 = var4.R();
         if (var5 != null) {
            var1.setData("npcDefinitionId", var5.getId());
            var1.setData("npcDefinitionName", var5.getName());
            var1.setData("npcModels", Arrays.toString(var5.getModels()));
            if (var5.getColorToReplace() != null) {
               var1.setData("npcRecolourCount", var5.getColorToReplace().length);
            }

            if (var5.getColorToReplaceWith() != null) {
               var1.setData("npcRecolourTargetCount", var5.getColorToReplaceWith().length);
            }
         }

         int var10000 = var4.getIndex();
         return "npc[" + var10000 + "] " + var4.getName();
      }
   }

   private String a(Breadcrumb var1, int var2) {
      aC var3 = osrs.aC.c(var2);
      var1.setData("entityResolved", var3 != null);
      var1.setData("entityType", "loc");
      var1.setData("objectId", var2);
      if (var3 == null) {
         return "loc[" + var2 + "]";
      } else {
         var1.setData("objectName", var3.getName());
         var1.setData("objectSizeX", var3.getSizeX());
         var1.setData("objectSizeY", var3.getSizeY());
         var1.setData("objectMapIconId", var3.getMapIconId());
         var1.setData("objectMapSceneId", var3.getMapSceneId());
         var1.setData("objectModels", Arrays.toString(var3.getObjectModels()));
         aC var4 = var3.e();
         if (var4 != null) {
            var1.setData("objectImpostorId", var4.getId());
            var1.setData("objectImpostorName", var4.getName());
         }

         return "loc[" + var2 + "] " + var3.getName();
      }
   }

   private String c(Breadcrumb var1, bG var2, int var3) {
      gZ var4 = var2 != null ? (gZ)var2.t.a((long)var3) : null;
      if (var4 == null) {
         var1.setData("entityResolved", false);
         var1.setData("entityType", "world_entity");
         var1.setData("worldEntityId", var3);
         return "world_entity[" + var3 + "]";
      } else {
         var1.setData("entityResolved", true);
         var1.setData("entityType", "world_entity");
         var1.setData("worldEntityId", var3);
         var1.setData("worldEntityPlane", var4.m());
         var1.setData("worldEntityX", var4.k());
         var1.setData("worldEntityY", var4.l());
         var1.setData("worldEntityOrientation", var4.d());
         var1.setData("worldEntityCurrentWorldViewId", var4.e);
         bH var5 = var4.n();
         if (var5 != null) {
            var1.setData("worldEntityConfigId", var5.getId());
            var1.setData("worldEntityConfigName", var5.c);
            var1.setData("worldEntityCategory", var5.getCategory());
         }

         return "world_entity[" + var3 + "]";
      }
   }

   private String a(Breadcrumb var1, long var2) {
      if (var2 == 0L) {
         return "none";
      } else {
         int var4 = c(var2);
         int var5 = dB.c(var2);
         int var6 = (int)(var2 >>> 52 & 4095L);
         if (var6 == 4095) {
            var6 = -1;
         }

         var1.setData("tagType", var4);
         var1.setData("tagTypeName", q(var4));
         var1.setData("tagEntityId", var5);
         var1.setData("tagX", (int)(var2 & 127L));
         var1.setData("tagY", dB.b(var2));
         var1.setData("tagPlane", (int)(var2 >>> 14 & 3L));
         var1.setData("tagWorldViewId", var6);
         var1.setData("tagFlag", (int)(var2 >>> 19 & 1L));
         bG var7 = bG.b(var6);
         if (var7 != null) {
            var1.setData("tagWorldBaseX", var7.getBaseX());
            var1.setData("tagWorldBaseY", var7.getBaseY());
            var1.setData("tagWorldPlane", var7.getPlane());
         }

         switch (var4) {
            case 0:
               return this.a(var1, var7, var5);
            case 1:
               return this.b(var1, var7, var5);
            case 2:
               return this.a(var1, var5);
            case 3:
            default:
               var1.setData("entityType", q(var4));
               var1.setData("entityResolved", false);
               String var10000 = q(var4);
               return var10000 + "[" + var5 + "]";
            case 4:
               return this.c(var1, var7, var5);
         }
      }
   }

   private String a(aZ var1) {
      int[] var2 = var1.getEquipmentIds();
      StringBuilder var3 = new StringBuilder();

      for(int var4 = 0; var4 < var2.length; ++var4) {
         int var5 = var2[var4];
         if (var5 != 0) {
            if (var3.length() > 0) {
               var3.append("; ");
            }

            var3.append(var4).append('=');
            if (var5 >= 2048) {
               int var6 = var5 - 2048;
               aO var7 = aO.a(var6);
               var3.append("item:").append(var6);
               if (var7 != null) {
                  var3.append('(').append(var7.getName()).append(')');
               }
            } else if (var5 >= 256) {
               var3.append("kit:").append(var5 - 256);
            } else {
               var3.append(var5);
            }
         }
      }

      return var3.toString();
   }

   private String b(aZ var1) {
      if (var1.c == null) {
         return "";
      } else {
         int[] var2 = var1.getEquipmentIds();
         StringBuilder var3 = new StringBuilder();

         for(int var4 = 0; var4 < var1.c.length; ++var4) {
            cF var5 = var1.c[var4];
            if (var5 != null) {
               if (var3.length() > 0) {
                  var3.append("; ");
               }

               var3.append(var4).append('[');
               if (var4 < var2.length && var2[var4] >= 2048) {
                  var3.append("item=").append(var2[var4] - 2048).append(',');
               }

               var3.append("recol=").append(var5.e != null ? var5.e.length : 0);
               var3.append(",retex=").append(var5.f != null ? var5.f.length : 0);
               var3.append(",maleModel=").append(var5.a);
               var3.append(",maleHead=").append(var5.b);
               var3.append(",femaleModel=").append(var5.c);
               var3.append(",femaleHead=").append(var5.d);
               var3.append(']');
            }
         }

         return var3.toString();
      }
   }

   private void a(String var1, int var2, long var3) {
      int var5 = this.z;
      int var6 = this.A;
      int var7 = this.X;
      long var8 = this.b(var3);
      long var10 = System.currentTimeMillis();
      if (aA != 0L && var10 - aA < 60000L) {
         a(var5, var6, var7, var8);
      } else {
         int var12 = aB;
         int var13 = aC;
         int var14 = aD;
         int var15 = aE;
         long var16 = aF;
         aB = 0;
         aC = 0;
         aD = 0;
         aE = 0;
         aF = 0L;
         aA = var10;

         try {
            Breadcrumb var18 = new Breadcrumb();
            var18.setType("debug");
            var18.setCategory("model.render");
            var18.setLevel(SentryLevel.WARNING);
            var18.setMessage("ModelLit oversized model");
            var18.setData("renderPath", var1);
            var18.setData("vertices", var5);
            var18.setData("faces", var6);
            var18.setData("diameter", var7);
            var18.setData("nearDepth", var2);
            var18.setData("rawTag", var3);
            var18.setData("tag", var8);
            var18.setData("tagSource", var3 != 0L ? "direct" : (var8 != 0L ? "carried" : "none"));
            var18.setData("suppressedLogsSinceLast", var12);
            var18.setData("suppressedMaxVertices", var13);
            var18.setData("suppressedMaxFaces", var14);
            var18.setData("suppressedMaxDiameter", var15);
            var18.setData("suppressedLastTag", var16);
            String var19 = this.a(var18, var8);
            Sentry.addBreadcrumb(var18);
            Sentry.captureMessage("ModelLit oversized model", SentryLevel.WARNING, (var16x) -> {
               var16x.setTag("renderPath", var1);
               var16x.setTag("tagType", var8 != 0L ? q(c(var8)) : "none");
               var16x.setExtra("vertices", String.valueOf(var5));
               var16x.setExtra("faces", String.valueOf(var6));
               var16x.setExtra("diameter", String.valueOf(var7));
               var16x.setExtra("nearDepth", String.valueOf(var2));
               var16x.setExtra("rawTag", String.valueOf(var3));
               var16x.setExtra("tag", String.valueOf(var8));
               var16x.setExtra("tagSource", var3 != 0L ? "direct" : (var8 != 0L ? "carried" : "none"));
               var16x.setExtra("suppressedLogsSinceLast", String.valueOf(var12));
               var16x.setExtra("suppressedMaxVertices", String.valueOf(var13));
               var16x.setExtra("suppressedMaxFaces", String.valueOf(var14));
               var16x.setExtra("suppressedMaxDiameter", String.valueOf(var15));
               var16x.setExtra("suppressedLastTag", String.valueOf(var16));
               var16x.setExtra("tagSummary", var19);
            });
            Client.dF.info("ModelLit oversized model path={} vertices={} faces={} diameter={} tag={} tagType={} suppressed={} suppressedMax={}/{}/{} tagSummary={}", new Object[]{var1, var5, var6, var7, var8, var8 != 0L ? q(c(var8)) : "none", var12, var13, var14, var15, var19});
         } catch (Throwable var20) {
         }

      }
   }

   public aH(aH var1) {
      this.ad = dg.a;
      this.z = var1.z;
      this.A = var1.A;
      this.N = var1.N;
      this.ak = var1.ak;
      this.al = var1.al;
      this.af = var1.af;
      this.B = var1.B;
      this.C = var1.C;
      this.D = var1.D;
      this.E = var1.E;
      this.F = var1.F;
      this.G = var1.G;
      this.J = var1.J;
      this.I = var1.I;
      this.L = var1.L;
      this.K = var1.K;
      this.H = var1.H;
      this.M = var1.M;
      this.O = var1.O;
      this.P = var1.P;
      this.Q = var1.Q;
      this.Z = var1.Z;
      this.aa = var1.aa;
      this.T = var1.T;
      this.c(var1);
   }

   public aH(aH[] var1, int var2) {
      this.ad = dg.a;
      this.z = 0;
      this.A = 0;
      this.N = 0;
      this.M = -1;

      int var3;
      for(var3 = 0; var3 < var2; ++var3) {
         aH var4 = var1[var3];
         if (var4 != null) {
            this.z += var4.z;
            this.A += var4.A;
            this.N += var4.N;
            if (this.M == -1) {
               this.M = var4.M;
            }
         }
      }

      this.a(this.z, this.A, this.N);
      this.z = 0;
      this.A = 0;
      this.N = 0;

      for(var3 = 0; var3 < var2; ++var3) {
         this.b(var1[var3]);
      }

      this.a(var1, var2);
   }

   public aH(int var1, int var2, int var3, byte var4) {
      this.ad = dg.a;
      this.a(var1, var2, var3);
      this.M = var4;
      this.z = 0;
      this.A = 0;
      this.N = 0;
   }

   public void a(int var1, int var2, int var3) {
      this.b(var1, var2, var3);
      this.ak = new float[var1];
      this.al = new float[var1];
      this.af = new float[var1];
      this.B = new int[var2];
      this.C = new int[var2];
      this.D = new int[var2];
      this.E = new int[var2];
      this.F = new int[var2];
      this.G = new int[var2];
      if (var3 > 0) {
         this.O = new int[var3];
         this.P = new int[var3];
         this.Q = new int[var3];
      }

   }

   public void a(aH var1) {
      int var2 = this.B.length;
      if (this.J == null && (var1.J != null || this.M != var1.M)) {
         this.J = new byte[var2];
         Arrays.fill(this.J, this.M);
      }

      if (this.I == null && var1.I != null) {
         this.I = new byte[var2];
         Arrays.fill(this.I, (byte)0);
      }

      if (this.K == null && var1.K != null) {
         this.K = new short[var2];
         Arrays.fill(this.K, (short)-1);
      }

      if (this.L == null && var1.L != null) {
         this.L = new byte[var2];
         Arrays.fill(this.L, (byte)-1);
      }

      if (this.H == null && var1.H != null) {
         this.H = new byte[var2];
         Arrays.fill(this.H, (byte)0);
      }

   }

   public void b(aH var1) {
      this.d(var1);
      if (var1 != null) {
         this.a(var1);

         int var2;
         for(var2 = 0; var2 < var1.A; ++var2) {
            this.B[this.A] = var1.B[var2] + this.z;
            this.C[this.A] = var1.C[var2] + this.z;
            this.D[this.A] = var1.D[var2] + this.z;
            this.E[this.A] = var1.E[var2];
            this.F[this.A] = var1.F[var2];
            this.G[this.A] = var1.G[var2];
            if (this.J != null) {
               this.J[this.A] = var1.J != null ? var1.J[var2] : var1.M;
            }

            if (this.I != null && var1.I != null) {
               this.I[this.A] = var1.I[var2];
            }

            if (this.K != null) {
               this.K[this.A] = var1.K != null ? var1.K[var2] : -1;
            }

            if (this.L != null) {
               if (var1.L != null && var1.L[var2] != -1) {
                  this.L[this.A] = (byte)(var1.L[var2] + this.N);
               } else {
                  this.L[this.A] = -1;
               }
            }

            if (this.H != null && var1.H != null) {
               this.H[this.A] = var1.H[var2];
            }

            ++this.A;
         }

         for(var2 = 0; var2 < var1.N; ++var2) {
            this.O[this.N] = var1.O[var2] + this.z;
            this.P[this.N] = var1.P[var2] + this.z;
            this.Q[this.N] = var1.Q[var2] + this.z;
            ++this.N;
         }

         for(var2 = 0; var2 < var1.z; ++var2) {
            this.ak[this.z] = var1.ak[var2];
            this.al[this.z] = var1.al[var2];
            this.af[this.z] = var1.af[var2];
            ++this.z;
         }
      }

   }

   public aH a(boolean var1) {
      if (!var1 && p.length < this.A) {
         p = new byte[this.A + 100];
      }

      return this.a(var1, o, p);
   }

   public aH b(boolean var1) {
      if (!var1 && r.length < this.A) {
         r = new byte[this.A + 100];
      }

      return this.a(var1, q, r);
   }

   public aH a(boolean var1, aH var2, byte[] var3) {
      this.b(var1, var2, var3);
      var2.z = this.z;
      var2.A = this.A;
      var2.N = this.N;
      if (var2.ak == null || var2.ak.length < this.z) {
         var2.ak = new float[this.z + 100];
         var2.al = new float[this.z + 100];
         var2.af = new float[this.z + 100];
      }

      int var4;
      for(var4 = 0; var4 < this.z; ++var4) {
         var2.ak[var4] = this.ak[var4];
         var2.al[var4] = this.al[var4];
         var2.af[var4] = this.af[var4];
      }

      if (var1) {
         var2.I = this.I;
      } else {
         var2.I = var3;
         if (this.I == null) {
            for(var4 = 0; var4 < this.A; ++var4) {
               var2.I[var4] = 0;
            }
         } else {
            for(var4 = 0; var4 < this.A; ++var4) {
               var2.I[var4] = this.I[var4];
            }
         }
      }

      var2.B = this.B;
      var2.C = this.C;
      var2.D = this.D;
      var2.E = this.E;
      var2.F = this.F;
      var2.G = this.G;
      var2.J = this.J;
      var2.L = this.L;
      var2.K = this.K;
      var2.H = this.H;
      var2.M = this.M;
      var2.O = this.O;
      var2.P = this.P;
      var2.Q = this.Q;
      var2.Z = this.Z;
      var2.aa = this.aa;
      var2.R = this.R;
      var2.S = this.S;
      var2.T = this.T;
      var2.ac = this.ac;
      var2.ae = this.ae;
      var2.a();
      return var2;
   }

   public void b(int var1) {
      dx var2 = this.c(var1);
      if (var2 == null) {
         int var3 = 0;
         int var4 = 0;
         int var5 = 0;
         int var6 = 0;
         int var7 = 0;
         int var8 = 0;
         int var9 = k[var1];
         int var10 = j[var1];

         for(int var11 = 0; var11 < this.z; ++var11) {
            int var12 = jl.a((int)this.ak[var11], (int)this.af[var11], var9, var10);
            int var13 = (int)this.al[var11];
            int var14 = jl.b((int)this.ak[var11], (int)this.af[var11], var9, var10);
            if (var12 < var3) {
               var3 = var12;
            }

            if (var12 > var6) {
               var6 = var12;
            }

            if (var13 < var4) {
               var4 = var13;
            }

            if (var13 > var7) {
               var7 = var13;
            }

            if (var14 < var5) {
               var5 = var14;
            }

            if (var14 > var8) {
               var8 = var14;
            }
         }

         dx var15 = new dx(var1, (var3 + var6) / 2, (var4 + var7) / 2, (var5 + var8) / 2, (var6 - var3 + 1) / 2, (var7 - var4 + 1) / 2, (var8 - var5 + 1) / 2);
         boolean var16 = true;
         if (var15.e < 32) {
            var15.e = 32;
         }

         if (var15.g < 32) {
            var15.g = 32;
         }

         if (this.T) {
            boolean var17 = true;
            var15.e += 8;
            var15.g += 8;
         }

         var15.h = this.ab;
         this.ab = var15;
      }

   }

   public dx c(int var1) {
      for(dx var2 = this.ab; var2 != null; var2 = var2.h) {
         if (var2.a == var1) {
            return var2;
         }
      }

      return null;
   }

   public void a() {
      this.U = 0;
      this.ab = null;
   }

   public void a(dD var1, int var2) {
      if (this.Z != null && var2 != -1) {
         dq var3 = var1.a[var2];
         db var4 = var3.e;
         bo.az = 0;
         bo.aA = 0;
         bo.aB = 0;
         j(-1);

         for(int var5 = 0; var5 < var3.f; ++var5) {
            int var6 = var3.h[var5];
            this.a(var4.b[var6], var4.c[var6], var3.i[var5], var3.j[var5], var3.k[var5]);
         }

         this.a();
      }

   }

   public void a(cz var1, int var2) {
      db var3 = var1.i;
      de var4 = var3.a();
      if (var4 != null) {
         var4.a(var1, var2);
         this.a(var4, var1.a());
      }

      if (var1.b()) {
         this.b(var1, var2);
      }

      this.a();
   }

   public final void a(int var1, int var2, int var3, int var4, int var5, int var6, int var7) {
      try {
         int var8 = var7;
         int var9 = var6;
         int var10 = var5;
         int var11 = var3;
         int var12 = var2;
         int var13 = var1;
         aH var14 = this;
         if (this.U != 2) {
            this.g();
         }

         int var15 = aW.b();
         int var16 = aW.c();
         int var17 = j[var1];
         int var18 = k[var1];
         int var19 = j[var2];
         int var20 = k[var2];
         int var21 = j[var3];
         int var22 = k[var3];
         int var23 = j[var4];
         int var24 = k[var4];
         int var25 = var7 * var24 + var6 * var23 >> 16;
         this.a(0L);
         if (this.t()) {
            this.a("render", var25, 0L);
         }

         for(int var26 = 0; var26 < var14.z; ++var26) {
            int var27 = (int)var14.ak[var26];
            int var28 = (int)var14.al[var26];
            int var29 = (int)var14.af[var26];
            int var30;
            if (var11 != 0) {
               var30 = var21 * var28 + var22 * var27 >> 16;
               var28 = var22 * var28 - var21 * var27 >> 16;
               var27 = var30;
            }

            if (var13 != 0) {
               var30 = var18 * var28 - var17 * var29 >> 16;
               var29 = var17 * var28 + var18 * var29 >> 16;
               var28 = var30;
            }

            if (var12 != 0) {
               var30 = var19 * var29 + var20 * var27 >> 16;
               var29 = var20 * var29 - var19 * var27 >> 16;
               var27 = var30;
            }

            var30 = var10 + var27;
            int var31 = var9 + var28;
            int var32 = var8 + var29;
            int var33 = var24 * var31 - var23 * var32 >> 16;
            int var34 = var23 * var31 + var24 * var32 >> 16;
            as[var26] = var34 - var25;
            ap[var26] = (float)(var15 + var30 * aW.h() / var34);
            aq[var26] = (float)(var16 + var33 * aW.h() / var34);
            ar[var26] = (float)var34;
            if (var14.K != null) {
               at[var26] = var30;
               au[var26] = var33;
               a[var26] = var34;
            }
         }

         try {
            var14.a(false, false, false, 0L);
         } catch (Exception var35) {
         }
      } catch (Exception var36) {
         Client.dF.debug("failed drawing model", var36);
      }

   }

   public void a(de var1, int var2) {
      this.b(var1, var2);
   }

   public void a(dD var1, int var2, dD var3, int var4, int[] var5) {
      if (var2 != -1) {
         if (var5 != null && var4 != -1) {
            dq var6 = var1.a[var2];
            dq var7 = var3.a[var4];
            db var8 = var6.e;
            bo.az = 0;
            bo.aA = 0;
            bo.aB = 0;
            j(-1);
            int var9 = 0;
            int var10 = var5[var9++];

            int var11;
            int var12;
            for(var11 = 0; var11 < var6.f; ++var11) {
               for(var12 = var6.h[var11]; var12 > var10; var10 = var5[var9++]) {
               }

               if (var10 != var12 || var8.b[var12] == 0) {
                  this.a(var8.b[var12], var8.c[var12], var6.i[var11], var6.j[var11], var6.k[var11]);
               }
            }

            bo.az = 0;
            bo.aA = 0;
            bo.aB = 0;
            j(-1);
            byte var15 = 0;
            var11 = var15 + 1;
            var12 = var5[var15];

            for(int var13 = 0; var13 < var7.f; ++var13) {
               int var14;
               for(var14 = var7.h[var13]; var14 > var12; var12 = var5[var11++]) {
               }

               if (var12 == var14 || var8.b[var14] == 0) {
                  this.a(var8.b[var14], var8.c[var14], var7.i[var13], var7.j[var13], var7.k[var13]);
               }
            }

            this.a();
         } else {
            this.a(var1, var2);
         }
      }

   }

   public void a(db var1, cz var2, int var3, boolean[] var4, boolean var5, boolean var6) {
      de var7 = var1.a();
      if (var7 != null) {
         var7.a(var2, var3, var4, var5, 1192265556);
         if (var6) {
            this.a(var7, var2.a());
         }
      }

      if (!var5 && var2.b()) {
         this.b(var2, var3);
      }

   }

   public void a(dD var1, int var2, int[] var3, boolean var4) {
      if (var3 == null) {
         this.a(var1, var2);
      } else {
         dq var5 = var1.a[var2];
         db var6 = var5.e;
         int var7 = 0;
         int var8 = var3[var7++];
         bo.az = 0;
         bo.aA = 0;
         bo.aB = 0;
         j(-1);

         for(int var9 = 0; var9 < var5.f; ++var9) {
            int var10;
            for(var10 = var5.h[var9]; var10 > var8; var8 = var3[var7++]) {
            }

            if (var4) {
               if (var8 == var10 || var6.b[var10] == 0) {
                  this.a(var6.b[var10], var6.c[var10], var5.i[var9], var5.j[var9], var5.k[var9]);
               }
            } else if (var8 != var10 || var6.b[var10] == 0) {
               this.a(var6.b[var10], var6.c[var10], var5.i[var9], var5.j[var9], var5.k[var9]);
            }
         }
      }

   }

   public void d(int var1) {
      int var2 = j[var1];
      int var3 = k[var1];

      for(int var4 = 0; var4 < this.z; ++var4) {
         int var5 = (int)this.al[var4] * var3 - (int)this.af[var4] * var2 >> 16;
         this.af[var4] = (float)((int)this.af[var4] * var3 + (int)this.al[var4] * var2 >> 16);
         this.al[var4] = (float)var5;
      }

      this.a();
   }

   public void c() {
      for(int var1 = 0; var1 < this.z; ++var1) {
         float var2 = this.ak[var1];
         this.ak[var1] = this.af[var1];
         this.af[var1] = -var2;
      }

      this.q();
   }

   public final void a(int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8) {
      if (this.U != 2) {
         this.g();
      }

      int var9 = aW.b();
      int var10 = aW.c();
      int var11 = j[var1];
      int var12 = k[var1];
      int var13 = j[var2];
      int var14 = k[var2];
      int var15 = j[var3];
      int var16 = k[var3];
      int var17 = j[var4];
      int var18 = k[var4];
      int var19 = var6 * var17 + var7 * var18 >> 16;
      this.a(0L);
      if (this.t()) {
         this.a("renderOrthog", var19, 0L);
      }

      for(int var20 = 0; var20 < this.z; ++var20) {
         int var21 = (int)this.ak[var20];
         int var22 = (int)this.al[var20];
         int var23 = (int)this.af[var20];
         int var24;
         if (var3 != 0) {
            var24 = var15 * var22 + var16 * var21 >> 16;
            var22 = var16 * var22 - var15 * var21 >> 16;
            var21 = var24;
         }

         if (var1 != 0) {
            var24 = var12 * var22 - var11 * var23 >> 16;
            var23 = var11 * var22 + var12 * var23 >> 16;
            var22 = var24;
         }

         if (var2 != 0) {
            var24 = var13 * var23 + var14 * var21 >> 16;
            var23 = var14 * var23 - var13 * var21 >> 16;
            var21 = var24;
         }

         var24 = var5 + var21;
         int var25 = var6 + var22;
         int var26 = var7 + var23;
         int var27 = var18 * var25 - var17 * var26 >> 16;
         int var28 = var17 * var25 + var18 * var26 >> 16;
         as[var20] = var28 - var19;
         ap[var20] = (float)(var9 + var24 * aW.h() / var8);
         aq[var20] = (float)(var10 + var27 * aW.h() / var8);
         ar[var20] = (float)var8;
         if (this.K != null) {
            at[var20] = var24;
            au[var20] = var27;
            a[var20] = var28;
         }
      }

      try {
         this.a(false, false, false, 0L);
      } catch (Exception var29) {
      }

   }

   public void a(int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9, long var10, int var12, int var13, boolean var14) {
      this.a(var1, var2, var3, var4 - var7, var5 - var8, var6 - var9, var10);
   }

   public void a(int var1, int var2, int var3, int var4, int var5, int var6, long var7) {
      if (aW.h.p != 1 || this.I != null) {
         int var9 = aW.d[var2];
         int var10 = aW.e[var2];
         int var11 = aW.d[var3];
         int var12 = aW.e[var3];
         float var13 = aW.f[var2];
         float var14 = aW.g[var2];
         float var15 = aW.f[var3];
         float var16 = aW.g[var3];
         if (this.U != 1) {
            this.h();
         }

         this.b(var1);
         int var17 = var6 * var12 - var4 * var11 >> 16;
         int var18 = var5 * var9 + var10 * var17 >> 16;
         int var19 = this.W * var10 >> 16;
         int var20 = var18 + var19;
         if (var20 > 50 && var18 < df.e()) {
            int var21 = var4 * var12 + var6 * var11 >> 16;
            int var22 = (var21 - this.W) * aW.h();
            if (var22 / var20 < aW.e()) {
               int var23 = (this.W + var21) * aW.h();
               if (var23 / var20 > aW.d()) {
                  int var24 = var5 * var10 - var9 * var17 >> 16;
                  int var25 = this.W * var9 >> 16;
                  int var26 = (this.V * var10 >> 16) + var25;
                  int var27 = (var24 + var26) * aW.h();
                  if (var27 / var20 > aW.g()) {
                     int var28 = (this.cd * var10 >> 16) + var25;
                     int var29 = (var24 - var28) * aW.h();
                     if (var29 / var20 < aW.f()) {
                        int var30 = (this.cd * var9 >> 16) + var19;
                        boolean var31 = false;
                        boolean var32 = false;
                        if (var18 - var30 <= 50) {
                           var32 = true;
                        }

                        boolean var33 = var32 || this.K != null;
                        int var34 = dB.c;
                        int var35 = dB.d;
                        boolean var36 = bo.h();
                        boolean var37 = dB.a(var7);
                        boolean var38 = false;
                        int var40;
                        int var43;
                        if (var37 && var36) {
                           boolean var39 = false;
                           if (i) {
                              var39 = dB.a(this, var1, var4, var5, var6, var13, var14, var15, var16, aW.b(), aW.c(), aW.h());
                           } else {
                              var40 = var18 - var19;
                              if (var40 <= 50) {
                                 var40 = 50;
                              }

                              int var41;
                              int var42;
                              if (var21 > 0) {
                                 var41 = var22 / var20;
                                 var42 = var23 / var40;
                              } else {
                                 var42 = var23 / var20;
                                 var41 = var22 / var40;
                              }

                              int var44;
                              if (var24 > 0) {
                                 var43 = var29 / var20;
                                 var44 = var27 / var40;
                              } else {
                                 var44 = var27 / var20;
                                 var43 = var29 / var40;
                              }

                              int var45 = var34 - aW.b();
                              int var46 = var35 - aW.c();
                              if (var45 > var41 && var45 < var42 && var46 > var43 && var46 < var44) {
                                 var39 = true;
                              }
                           }

                           if (var39) {
                              if (this.T) {
                                 var40 = dB.b(this, var1, var4, var5, var6, var13, var14, var15, var16, aW.b(), aW.c(), aW.h());
                                 var40 += 32;
                                 dB.a(var7, var40);
                              } else {
                                 var38 = true;
                              }
                           }
                        }

                        int var55 = aW.b();
                        var40 = aW.c();
                        float var56 = 0.0F;
                        float var57 = 0.0F;
                        this.a(var7);
                        if (var1 != 0) {
                           var56 = aW.f[var1];
                           var57 = aW.g[var1];
                        }

                        if (this.t()) {
                           this.a("method4610", var18, var7);
                        }

                        for(var43 = 0; var43 < this.z; ++var43) {
                           float var58 = (float)((int)this.ak[var43]);
                           float var59 = (float)((int)this.al[var43]);
                           float var60 = (float)((int)this.af[var43]);
                           float var47;
                           if (var1 != 0) {
                              var47 = var56 * var60 + var57 * var58;
                              var60 = var57 * var60 - var56 * var58;
                              var58 = var47;
                           }

                           var47 = (float)var4 + var58;
                           float var48 = (float)var5 + var59;
                           float var49 = (float)var6 + var60;
                           float var50 = var15 * var49 + var16 * var47;
                           float var51 = var16 * var49 - var15 * var47;
                           float var52 = var14 * var48 - var13 * var51;
                           float var53 = var13 * var48 + var14 * var51;
                           var31 |= this.a(var43, var50, var52, var53, var18, var55, var40, var33);
                        }

                        try {
                           this.a(var31, var38, this.T, var7);
                        } catch (Exception var54) {
                        }
                     }
                  }
               }
            }
         }
      }

   }

   public void a(int var1, fX var2, int var3, int var4, int var5, long var6) {
      if (aW.h.p != 1 || this.I != null) {
         if (this.U != 1) {
            this.h();
         }

         this.b(var1);
         ge var8 = ge.a();
         var8.c.a((float)var1 * 0.0030679614F, 0.0F, 0.0F);
         var8.e.b(1.0F, 1.0F, 1.0F);
         var8.d.b((float)var3, (float)var4, (float)var5);
         fX var9 = fX.a();
         var9.a((ge)var8, (byte)108);
         var9.a(var2);
         int var10 = (int)var9.e(0.0F, 0.0F, 0.0F);
         boolean var11 = false;
         int var12 = var10 - this.Y;
         boolean var13 = var12 <= 50 || this.K != null;
         boolean var14 = dB.a(var6);
         boolean var15 = false;
         int var17;
         if (var14 && bo.h()) {
            boolean var16 = dB.a(this, var1, var3, var4, var5, var2, aW.b(), aW.c(), aW.h());
            if (var16) {
               if (this.T) {
                  var17 = dB.b(this, var1, var3, var4, var5, var2, aW.b(), aW.c(), aW.h());
                  var17 += 32;
                  dB.a(var6, var17);
               } else {
                  var15 = true;
               }
            }
         }

         int var26 = aW.b();
         var17 = aW.c();
         this.a(var6);
         if (this.t()) {
            this.a("method3887", var10, var6);
         }

         for(int var18 = 0; var18 < this.z; ++var18) {
            float var19 = (float)((int)this.ak[var18]);
            float var20 = (float)((int)this.al[var18]);
            float var21 = (float)((int)this.af[var18]);
            var9.a(var19, var20, var21, n);
            float var22 = n[0];
            float var23 = n[1];
            float var24 = n[2];
            var11 |= this.a(var18, var22, var23, var24, var10, var26, var17, var13);
         }

         var8.b();
         var9.b();

         try {
            this.a(var11, var15, this.T, var6);
         } catch (Exception var25) {
         }
      }

   }

   public boolean a(int var1, float var2, float var3, float var4, int var5, int var6, int var7, boolean var8) {
      if (var1 >= as.length) {
         this.a("method4759", var5, 0L);
         m(var1 + 1);
         if (var1 >= as.length) {
            return true;
         }
      }

      as[var1] = (int)var4 - var5;
      if (var8) {
         at[var1] = (int)var2;
         au[var1] = (int)var3;
         a[var1] = (int)var4;
      }

      if (var4 >= 50.0F) {
         ap[var1] = (float)var6 + var2 * (float)aW.h() / var4;
         aq[var1] = (float)var7 + var3 * (float)aW.h() / var4;
         ar[var1] = var4;
         return false;
      } else {
         ap[var1] = -5000.0F;
         return true;
      }
   }

   public final void a(boolean var1, boolean var2, boolean var3, long var4) {
      if (this.t()) {
         this.a("method4613", 0, var4);
      }

      if (this.A <= 65535) {
         i(this.z, this.A, this.X + 1);
         if (this.X < b.length) {
            int var6;
            for(var6 = 0; var6 < this.X; ++var6) {
               b[var6] = 0;
            }

            var6 = var3 ? 20 : 5;
            int var7 = aW.i();

            for(int var8 = 0; var8 < this.A; ++var8) {
               if (this.G[var8] == -2) {
                  ao[var8] = true;
               } else {
                  if (dg.c != this.ad) {
                     if (aW.h.p == 1 && (this.I == null || this.I[var8] == 0)) {
                        ao[var8] = true;
                        continue;
                     }

                     if (aW.h.p == 0 && this.I != null && this.I[var8] != 0) {
                        ao[var8] = true;
                        continue;
                     }
                  }

                  int var9 = this.B[var8];
                  int var10 = this.C[var8];
                  int var11 = this.D[var8];
                  float var12 = ap[var9];
                  float var13 = ap[var10];
                  float var14 = ap[var11];
                  an[var8] = var1 && (var12 == -5000.0F || var13 == -5000.0F || var14 == -5000.0F);
                  int var15;
                  int var16;
                  int var17;
                  int var18;
                  int var19;
                  int var20;
                  int var21;
                  int var23;
                  int var24;
                  int var25;
                  if (an[var8]) {
                     var15 = at[var9];
                     var16 = at[var10];
                     var17 = at[var11];
                     var18 = au[var9];
                     var19 = au[var10];
                     var20 = au[var11];
                     var21 = a[var9];
                     int var39 = a[var10];
                     var23 = a[var11];
                     var24 = var15 - var16;
                     var25 = var17 - var16;
                     int var26 = var18 - var19;
                     int var27 = var20 - var19;
                     int var28 = var21 - var39;
                     int var29 = var23 - var39;
                     int var30 = var26 * var29 - var27 * var28;
                     int var31 = var25 * var28 - var24 * var29;
                     int var32 = var24 * var27 - var25 * var26;
                     ao[var8] = (float)(var39 * var32 + var16 * var30 + var19 * var31) <= 0.0F;
                  } else {
                     ao[var8] = (aq[var11] - aq[var10]) * (var12 - var13) - (aq[var9] - aq[var10]) * (var14 - var13) <= 0.0F;
                     am[var8] = var12 < 0.0F || var13 < 0.0F || var14 < 0.0F || var12 > (float)var7 || var13 > (float)var7 || var14 > (float)var7;
                     if (var2) {
                        var15 = (int)aq[var9];
                        var16 = (int)aq[var10];
                        var17 = (int)aq[var11];
                        var18 = (int)var12;
                        var19 = (int)var13;
                        var20 = (int)var14;
                        var21 = dB.d + var6;
                        boolean var22;
                        if (var21 < var15 && var21 < var16 && var21 < var17) {
                           var22 = false;
                        } else {
                           var23 = dB.d - var6;
                           if (var23 > var15 && var23 > var16 && var23 > var17) {
                              var22 = false;
                           } else {
                              var24 = dB.c + var6;
                              if (var24 < var18 && var24 < var19 && var24 < var20) {
                                 var22 = false;
                              } else {
                                 var25 = dB.c - var6;
                                 if (var25 > var18 && var25 > var19 && var25 > var20) {
                                    var22 = false;
                                 } else {
                                    var22 = true;
                                 }
                              }
                           }
                        }

                        if (var22) {
                           var23 = (int)(ar[var9] + ar[var10] + ar[var11]) / 3;
                           dB.a(var4, var23);
                        }
                     }
                  }
               }
            }

            short var36 = this.ae;
            boolean var37 = false;
            if (aW.h.o.b()) {
               this.ae = (short)this.A;
            } else if (this.ac != null && this.ac.b()) {
               aW.h.o.a(this.ac);
               var37 = true;
            }

            try {
               if (!aW.j()) {
                  this.c(true);
               } else {
                  dg var38 = this.ad;
                  if (dg.a == var38) {
                     if (aW.h.p != 0 && this.I != null) {
                        var38 = dg.b;
                     } else {
                        var38 = dg.d;
                     }
                  }

                  switch (var38.e) {
                     case 1:
                        aW.h.m = -1;
                        aW.h.n = 1;
                        this.c(false);
                        break;
                     case 2:
                        aW.h.m = -1;
                        aW.h.n = 0;
                        this.c(true);
                        aW.h.m = 0;
                        aW.h.n = 1;
                        this.c(true);
                        aW.h.m = -1;
                        aW.h.n = 1;
                        break;
                     case 3:
                        aW.h.m = -1;
                        aW.h.n = 1;
                        this.d();
                  }
               }
            } finally {
               this.ae = var36;
               if (var37) {
                  aW.h.o.a();
               }

            }
         }

      }
   }

   public void d() {
      for(int var1 = 0; var1 < this.A; ++var1) {
         if (!ao[var1]) {
            this.e(var1);
         }
      }

   }

   public void c(boolean var1) {
      int var2;
      for(var2 = 0; var2 < this.A; ++var2) {
         if (!ao[var2]) {
            int var3 = (as[this.C[var2]] + as[this.B[var2]] + as[this.D[var2]]) / 3 + this.Y;
            a(var3, (char)var2);
         }
      }

      if (this.J != null && var1) {
         this.e();
      } else {
         for(var2 = this.X - 1; var2 >= 0; --var2) {
            char var6 = b[var2];
            if (var6 > 0) {
               char[] var4 = c[var2];

               for(int var5 = 0; var5 < var6; ++var5) {
                  this.e(var4[var5]);
               }
            }
         }
      }

   }

   public void e() {
      int var1;
      for(var1 = 0; var1 < 12; ++var1) {
         d[var1] = 0;
         h[var1] = 0;
      }

      int var2;
      int var4;
      int var5;
      for(var1 = this.X - 1; var1 >= 0; --var1) {
         var2 = b[var1];
         if (var2 > 0) {
            char[] var3 = c[var1];

            for(var4 = 0; var4 < var2; ++var4) {
               var5 = var3[var4];
               int var6 = this.J[var5] & 255;
               if (var6 < d.length) {
                  int var7 = p(var6);
                  e[var6][var7] = var5;
                  if (var6 < 10) {
                     int[] var8 = h;
                     var8[var6] += var1;
                  } else if (var6 == 10) {
                     f[var7] = var1;
                  } else {
                     g[var7] = var1;
                  }
               }
            }
         }
      }

      var1 = 0;
      if (d[1] > 0 || d[2] > 0) {
         var1 = (h[1] + h[2]) / (d[1] + d[2]);
      }

      var2 = 0;
      if (d[3] > 0 || d[4] > 0) {
         var2 = (h[3] + h[4]) / (d[3] + d[4]);
      }

      int var13 = 0;
      if (d[6] > 0 || d[8] > 0) {
         var13 = (h[6] + h[8]) / (d[6] + d[8]);
      }

      var4 = 0;
      var5 = d[10];
      int[] var14 = e[10];
      int[] var15 = f;
      if (var4 == var5) {
         var4 = 0;
         var5 = d[11];
         var14 = e[11];
         var15 = g;
      }

      int var16;
      if (var4 < var5) {
         var16 = var15[var4];
      } else {
         var16 = -1000;
      }

      for(int var9 = 0; var9 < 10; ++var9) {
         while(var9 == 0 && var16 > var1) {
            this.e(var14[var4++]);
            if (var4 == var5 && e[11] != var14) {
               var4 = 0;
               var5 = d[11];
               var14 = e[11];
               var15 = g;
            }

            if (var4 < var5) {
               var16 = var15[var4];
            } else {
               var16 = -1000;
            }
         }

         while(var9 == 3 && var16 > var2) {
            this.e(var14[var4++]);
            if (var4 == var5 && e[11] != var14) {
               var4 = 0;
               var5 = d[11];
               var14 = e[11];
               var15 = g;
            }

            if (var4 < var5) {
               var16 = var15[var4];
            } else {
               var16 = -1000;
            }
         }

         while(var9 == 5 && var16 > var13) {
            this.e(var14[var4++]);
            if (var4 == var5 && e[11] != var14) {
               var4 = 0;
               var5 = d[11];
               var14 = e[11];
               var15 = g;
            }

            if (var4 < var5) {
               var16 = var15[var4];
            } else {
               var16 = -1000;
            }
         }

         int var10 = d[var9];
         int[] var11 = e[var9];

         for(int var12 = 0; var12 < var10; ++var12) {
            this.e(var11[var12]);
         }
      }

      while(var16 != -1000) {
         this.e(var14[var4++]);
         if (var4 == var5 && e[11] != var14) {
            var4 = 0;
            var14 = e[11];
            var5 = d[11];
            var15 = g;
         }

         if (var4 < var5) {
            var16 = var15[var4];
         } else {
            var16 = -1000;
         }
      }

   }

   public void a(iX var1, int var2, int[] var3, float var4, float var5, float var6) {
      int var8;
      int var9;
      int[] var10;
      int var11;
      int var12;
      if (var2 == 0) {
         int var7 = 0;
         var1.m = var1.d = var1.b = 0.0F;

         for(var8 = 0; var8 < var3.length; ++var8) {
            var9 = var3[var8];
            if (var9 < this.Z.length) {
               var10 = this.Z[var9];

               for(var11 = 0; var11 < var10.length; ++var11) {
                  var12 = var10[var11];
                  var1.m += this.ak[var12];
                  var1.d += this.al[var12];
                  var1.b += this.af[var12];
                  ++var7;
               }
            }
         }

         if (var7 > 0) {
            var1.m = var1.m / (float)var7 + var4;
            var1.d = var1.d / (float)var7 + var5;
            var1.b = var1.b / (float)var7 + var6;
         } else {
            var1.m = var4;
            var1.d = var5;
            var1.b = var6;
         }
      } else {
         float[] var23;
         if (var2 == 1) {
            for(var8 = 0; var8 < var3.length; ++var8) {
               var9 = var3[var8];
               if (var9 < this.Z.length) {
                  var10 = this.Z[var9];

                  for(var11 = 0; var11 < var10.length; ++var11) {
                     var12 = var10[var11];
                     var23 = this.ak;
                     var23[var12] += var4;
                     var23 = this.al;
                     var23[var12] += var5;
                     var23 = this.af;
                     var23[var12] += var6;
                  }
               }
            }
         } else if (var2 == 2) {
            float var24 = var4 * 8.0F * 0.0030679617F;
            float var25 = var5 * 8.0F * 0.0030679617F;
            float var26 = var6 * 8.0F * 0.0030679617F;
            float var27 = 0.0F;
            float var28 = 0.0F;
            float var13 = 0.0F;
            float var14 = 0.0F;
            float var15 = 0.0F;
            float var16 = 0.0F;
            if (var24 != 0.0F) {
               var27 = (float)Math.sin((double)var24);
               var28 = (float)Math.cos((double)var24);
            }

            if (var25 != 0.0F) {
               var13 = (float)Math.sin((double)var25);
               var14 = (float)Math.cos((double)var25);
            }

            if (var26 != 0.0F) {
               var15 = (float)Math.sin((double)var26);
               var16 = (float)Math.cos((double)var26);
            }

            for(int var17 = 0; var17 < var3.length; ++var17) {
               int var18 = var3[var17];
               if (var18 < this.Z.length) {
                  int[] var19 = this.Z[var18];

                  for(int var20 = 0; var20 < var19.length; ++var20) {
                     int var21 = var19[var20];
                     var23 = this.ak;
                     var23[var21] -= var1.m;
                     var23 = this.al;
                     var23[var21] -= var1.d;
                     var23 = this.af;
                     var23[var21] -= var1.b;
                     float var22;
                     if (var26 != 0.0F) {
                        var22 = this.ak[var21] * var16 + this.al[var21] * var15;
                        this.al[var21] = this.al[var21] * var16 - this.ak[var21] * var15;
                        this.ak[var21] = var22;
                     }

                     if (var24 != 0.0F) {
                        var22 = this.al[var21] * var28 - this.af[var21] * var27;
                        this.af[var21] = this.af[var21] * var28 + this.al[var21] * var27;
                        this.al[var21] = var22;
                     }

                     if (var25 != 0.0F) {
                        var22 = this.af[var21] * var13 + this.ak[var21] * var14;
                        this.af[var21] = this.af[var21] * var14 - this.ak[var21] * var13;
                        this.ak[var21] = var22;
                     }

                     var23 = this.ak;
                     var23[var21] += var1.m;
                     var23 = this.al;
                     var23[var21] += var1.d;
                     var23 = this.af;
                     var23[var21] += var1.b;
                  }
               }
            }
         } else if (var2 == 3) {
            for(var8 = 0; var8 < var3.length; ++var8) {
               var9 = var3[var8];
               if (var9 < this.Z.length) {
                  var10 = this.Z[var9];

                  for(var11 = 0; var11 < var10.length; ++var11) {
                     var12 = var10[var11];
                     var23 = this.ak;
                     var23[var12] -= var1.m;
                     var23 = this.al;
                     var23[var12] -= var1.d;
                     var23 = this.af;
                     var23[var12] -= var1.b;
                     this.ak[var12] = this.ak[var12] * var4 / 128.0F;
                     this.al[var12] = this.al[var12] * var5 / 128.0F;
                     this.af[var12] = this.af[var12] * var6 / 128.0F;
                     var23 = this.ak;
                     var23[var12] += var1.m;
                     var23 = this.al;
                     var23[var12] += var1.d;
                     var23 = this.af;
                     var23[var12] += var1.b;
                  }
               }
            }
         } else if (var2 == 5 && this.aa != null && this.I != null) {
            for(var8 = 0; var8 < var3.length; ++var8) {
               var9 = var3[var8];
               if (var9 < this.aa.length) {
                  var10 = this.aa[var9];

                  for(var11 = 0; var11 < var10.length; ++var11) {
                     var12 = var10[var11];
                     int var29 = (this.I[var12] & 255) + (int)var4 * 8;
                     if (var29 < 0) {
                        var29 = 0;
                     } else if (var29 > 255) {
                        var29 = 255;
                     }

                     this.I[var12] = (byte)var29;
                  }
               }
            }
         }
      }

   }

   public final void e(int var1) {
      if (this.I == null) {
         aW.h.a = 0;
      } else {
         aW.h.a = (this.I[var1] == -1 ? 253 : this.I[var1]) & 255;
      }

      if ((aW.h.p != 1 || aW.h.a != 0) && (aW.h.p != 0 || aW.h.a == 0)) {
         if (an[var1]) {
            this.g(var1);
         } else {
            int var2 = this.B[var1];
            int var3 = this.C[var1];
            int var4 = this.D[var1];
            aW.h.q = am[var1];
            int var5 = this.H == null ? 0 : this.H[var1] * 2;
            float var6 = jl.a(ar[var2] - (float)var5);
            float var7 = jl.a(ar[var3] - (float)var5);
            float var8 = jl.a(ar[var4] - (float)var5);
            this.a(var1, aq[var2], aq[var3], aq[var4], ap[var2], ap[var3], ap[var4], var6, var7, var8, this.E[var1], this.F[var1], this.G[var1]);
         }
      }

   }

   public boolean f(int var1) {
      return var1 < this.ae;
   }

   public void a(int var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8, float var9, float var10, int var11, int var12, int var13) {
      if (this.K != null && this.K[var1] != -1) {
         int var15;
         int var16;
         int var18;
         if (this.L != null && this.L[var1] != -1) {
            int var17 = this.L[var1] & 255;
            var18 = this.O[var17];
            var15 = this.P[var17];
            var16 = this.Q[var17];
         } else {
            var18 = this.B[var1];
            var15 = this.C[var1];
            var16 = this.D[var1];
         }

         if (this.G[var1] == -1) {
            aW.a(var2, var3, var4, var5, var6, var7, var8, var9, var10, var11, var11, var11, at[var18], at[var15], at[var16], au[var18], au[var15], au[var16], a[var18], a[var15], a[var16], this.K[var1]);
         } else {
            aW.a(var2, var3, var4, var5, var6, var7, var8, var9, var10, var11, var12, var13, at[var18], at[var15], at[var16], au[var18], au[var15], au[var16], a[var18], a[var15], a[var16], this.K[var1]);
         }
      } else {
         boolean var14 = this.f(var1);
         if (this.G[var1] == -1 && var14) {
            aW.b(var2, var3, var4, var5, var6, var7, var8, var9, var10, l[this.E[var1]]);
         } else if (this.G[var1] == -1) {
            aW.a(var2, var3, var4, var5, var6, var7, var8, var9, var10, l[this.E[var1]]);
         } else if (var14) {
            aW.b(var2, var3, var4, var5, var6, var7, var8, var9, var10, var11, var12, var13);
         } else {
            aW.a(var2, var3, var4, var5, var6, var7, var8, var9, var10, var11, var12, var13);
         }
      }

   }

   public final void g(int var1) {
      int var2 = aW.b();
      int var3 = aW.c();
      int var4 = 0;
      int var5 = this.B[var1];
      int var6 = this.C[var1];
      int var7 = this.D[var1];
      int var8 = a[var5];
      int var9 = a[var6];
      int var10 = a[var7];
      if (this.I == null) {
         aW.h.a = 0;
      } else {
         aW.h.a = this.I[var1] & 255;
      }

      int var11;
      int var12;
      int var13;
      int var14;
      if (var8 >= 50) {
         v[var4] = (int)ap[var5];
         w[var4] = (int)aq[var5];
         y[var4] = ar[var5];
         x[var4++] = this.E[var1];
      } else {
         var11 = at[var5];
         var12 = au[var5];
         var13 = this.E[var1];
         if (var10 >= 50) {
            var14 = (50 - var8) * m[var10 - var8];
            v[var4] = var2 + (((at[var7] - var11) * var14 >> 16) + var11) * aW.h() / 50;
            w[var4] = var3 + (((au[var7] - var12) * var14 >> 16) + var12) * aW.h() / 50;
            y[var4] = 50.0F;
            x[var4++] = ((this.G[var1] - var13) * var14 >> 16) + var13;
         }

         if (var9 >= 50) {
            var14 = (50 - var8) * m[var9 - var8];
            v[var4] = var2 + (((at[var6] - var11) * var14 >> 16) + var11) * aW.h() / 50;
            w[var4] = var3 + (((au[var6] - var12) * var14 >> 16) + var12) * aW.h() / 50;
            y[var4] = 50.0F;
            x[var4++] = ((this.F[var1] - var13) * var14 >> 16) + var13;
         }
      }

      if (var9 >= 50) {
         v[var4] = (int)ap[var6];
         w[var4] = (int)aq[var6];
         y[var4] = ar[var6];
         x[var4++] = this.F[var1];
      } else {
         var11 = at[var6];
         var12 = au[var6];
         var13 = this.F[var1];
         if (var8 >= 50) {
            var14 = (50 - var9) * m[var8 - var9];
            v[var4] = var2 + (((at[var5] - var11) * var14 >> 16) + var11) * aW.h() / 50;
            w[var4] = var3 + (((au[var5] - var12) * var14 >> 16) + var12) * aW.h() / 50;
            y[var4] = 50.0F;
            x[var4++] = ((this.E[var1] - var13) * var14 >> 16) + var13;
         }

         if (var10 >= 50) {
            var14 = (50 - var9) * m[var10 - var9];
            v[var4] = var2 + (((at[var7] - var11) * var14 >> 16) + var11) * aW.h() / 50;
            w[var4] = var3 + (((au[var7] - var12) * var14 >> 16) + var12) * aW.h() / 50;
            y[var4] = 50.0F;
            x[var4++] = ((this.G[var1] - var13) * var14 >> 16) + var13;
         }
      }

      if (var10 >= 50) {
         v[var4] = (int)ap[var7];
         w[var4] = (int)aq[var7];
         y[var4] = ar[var7];
         x[var4++] = this.G[var1];
      } else {
         var11 = at[var7];
         var12 = au[var7];
         var13 = this.G[var1];
         if (var9 >= 50) {
            var14 = (50 - var10) * m[var9 - var10];
            v[var4] = var2 + (((at[var6] - var11) * var14 >> 16) + var11) * aW.h() / 50;
            w[var4] = var3 + (((au[var6] - var12) * var14 >> 16) + var12) * aW.h() / 50;
            y[var4] = 50.0F;
            x[var4++] = ((this.F[var1] - var13) * var14 >> 16) + var13;
         }

         if (var8 >= 50) {
            var14 = (50 - var10) * m[var8 - var10];
            v[var4] = var2 + (((at[var5] - var11) * var14 >> 16) + var11) * aW.h() / 50;
            w[var4] = var3 + (((au[var5] - var12) * var14 >> 16) + var12) * aW.h() / 50;
            y[var4] = 50.0F;
            x[var4++] = ((this.E[var1] - var13) * var14 >> 16) + var13;
         }
      }

      var11 = v[0];
      var12 = v[1];
      var13 = v[2];
      var14 = w[0];
      int var15 = w[1];
      int var16 = w[2];
      int var17 = this.H == null ? 0 : this.H[var1] * 2;
      float var18 = jl.a(y[0] - (float)var17);
      float var19 = jl.a(y[1] - (float)var17);
      float var20 = jl.a(y[2] - (float)var17);
      aW.h.q = false;
      int var21 = aW.i();
      if (var4 == 3) {
         if (var11 < 0 || var12 < 0 || var13 < 0 || var11 > var21 || var12 > var21 || var13 > var21) {
            aW.h.q = true;
         }

         this.a(var1, (float)var14, (float)var15, (float)var16, (float)var11, (float)var12, (float)var13, var18, var19, var20, x[0], x[1], x[2]);
      }

      if (var4 == 4) {
         if (var11 < 0 || var12 < 0 || var13 < 0 || var11 > var21 || var12 > var21 || var13 > var21 || v[3] < 0 || v[3] > var21) {
            aW.h.q = true;
         }

         int var23;
         if (this.K != null && this.K[var1] != -1) {
            int var24;
            int var26;
            if (this.L != null && this.L[var1] != -1) {
               int var25 = this.L[var1] & 255;
               var26 = this.O[var25];
               var23 = this.P[var25];
               var24 = this.Q[var25];
            } else {
               var26 = var5;
               var23 = var6;
               var24 = var7;
            }

            short var27 = this.K[var1];
            if (this.G[var1] == -1) {
               aW.a((float)var14, (float)var15, (float)var16, (float)var11, (float)var12, (float)var13, var18, var19, var20, this.E[var1], this.E[var1], this.E[var1], at[var26], at[var23], at[var24], au[var26], au[var23], au[var24], a[var26], a[var23], a[var24], var27);
               aW.a((float)var14, (float)var16, (float)w[3], (float)var11, (float)var13, (float)v[3], var18, var20, y[3], this.E[var1], this.E[var1], this.E[var1], at[var26], at[var23], at[var24], au[var26], au[var23], au[var24], a[var26], a[var23], a[var24], var27);
            } else {
               aW.a((float)var14, (float)var15, (float)var16, (float)var11, (float)var12, (float)var13, var18, var19, var20, x[0], x[1], x[2], at[var26], at[var23], at[var24], au[var26], au[var23], au[var24], a[var26], a[var23], a[var24], var27);
               aW.a((float)var14, (float)var16, (float)w[3], (float)var11, (float)var13, (float)v[3], var18, var20, y[3], x[0], x[2], x[3], at[var26], at[var23], at[var24], au[var26], au[var23], au[var24], a[var26], a[var23], a[var24], var27);
            }
         } else {
            boolean var22 = this.f(var1);
            if (this.G[var1] == -1 && var22) {
               var23 = l[this.E[var1]];
               aW.b((float)var14, (float)var15, (float)var16, (float)var11, (float)var12, (float)var13, var18, var19, var20, var23);
               aW.b((float)var14, (float)var16, (float)w[3], (float)var11, (float)var13, (float)v[3], var18, var20, y[3], var23);
            } else if (this.G[var1] == -1) {
               var23 = l[this.E[var1]];
               aW.a((float)var14, (float)var15, (float)var16, (float)var11, (float)var12, (float)var13, var18, var19, var20, var23);
               aW.a((float)var14, (float)var16, (float)w[3], (float)var11, (float)var13, (float)v[3], var18, var20, y[3], var23);
            } else if (var22) {
               aW.b((float)var14, (float)var15, (float)var16, (float)var11, (float)var12, (float)var13, var18, var19, var20, x[0], x[1], x[2]);
               aW.b((float)var14, (float)var16, (float)w[3], (float)var11, (float)var13, (float)v[3], var18, var20, y[3], x[0], x[2], x[3]);
            } else {
               aW.a((float)var14, (float)var15, (float)var16, (float)var11, (float)var12, (float)var13, var18, var19, var20, x[0], x[1], x[2]);
               aW.a((float)var14, (float)var16, (float)w[3], (float)var11, (float)var13, (float)v[3], var18, var20, y[3], x[0], x[2], x[3]);
            }
         }
      }

   }

   public void a(dH var1, short var2) {
      if (this.ac == null) {
         this.ac = new dH();
      }

      this.ac.a(var1);
      this.ae = var2;
   }

   public void f() {
      this.ac = null;
   }

   public void a(dg var1) {
      this.ad = var1;
   }

   public void a(iX var1, db var2, dq var3, dq var4, float var5, int var6) {
      int var7;
      int var8;
      if (var4 != null && var5 != 0.0F) {
         var7 = 0;
         var8 = 0;

         for(int var9 = 0; var9 < var2.a; ++var9) {
            boolean var10 = false;
            if (var7 < var3.f && var3.h[var7] == var9) {
               var10 = true;
            }

            boolean var11 = false;
            if (var8 < var4.f && var4.h[var8] == var9) {
               var11 = true;
            }

            if (var10 || var11) {
               short var12 = 0;
               int var13 = var2.b[var9];
               if (var13 == 3) {
                  var12 = 128;
               }

               int var14 = var12;
               int var15 = var12;
               int var16 = var12;
               if (var10) {
                  var14 = var3.i[var7];
                  var15 = var3.j[var7];
                  var16 = var3.k[var7];
                  ++var7;
               }

               int var17 = var12;
               int var18 = var12;
               int var19 = var12;
               if (var11) {
                  var17 = var4.i[var8];
                  var18 = var4.j[var8];
                  var19 = var4.k[var8];
                  ++var8;
               }

               float var20;
               float var21;
               float var22;
               if (var13 == 2) {
                  int var23 = var17 - var14 & 255;
                  int var24 = var18 - var15 & 255;
                  int var25 = var19 - var16 & 255;
                  if (var23 >= 128) {
                     var23 -= 256;
                  }

                  if (var24 >= 128) {
                     var24 -= 256;
                  }

                  if (var25 >= 128) {
                     var25 -= 256;
                  }

                  var20 = (float)var23 * var5 / (float)var6 + (float)var14;
                  var21 = (float)var24 * var5 / (float)var6 + (float)var15;
                  var22 = (float)var25 * var5 / (float)var6 + (float)var16;
               } else if (var13 == 5) {
                  var20 = (float)var14;
                  var21 = 0.0F;
                  var22 = 0.0F;
               } else {
                  var20 = (float)(var17 - var14) * var5 / (float)var6 + (float)var14;
                  var21 = (float)(var18 - var15) * var5 / (float)var6 + (float)var15;
                  var22 = (float)(var19 - var16) * var5 / (float)var6 + (float)var16;
               }

               this.a(var1, var13, var2.c[var9], var20, var21, var22);
            }
         }
      } else {
         for(var7 = 0; var7 < var3.f; ++var7) {
            var8 = var3.h[var7];
            this.a(var1, var2.b[var8], var2.c[var8], (float)var3.i[var7], (float)var3.j[var7], (float)var3.k[var7]);
         }
      }

   }

   public void a(iX var1, int var2, int[] var3, int var4, int var5, int var6) {
      int var8;
      int var9;
      int[] var10;
      int var11;
      int var12;
      if (var2 == 0) {
         int var7 = 0;
         var1.h = 0;
         var1.g = 0;
         var1.i = 0;

         for(var8 = 0; var8 < var3.length; ++var8) {
            var9 = var3[var8];
            if (var9 < this.Z.length) {
               var10 = this.Z[var9];

               for(var11 = 0; var11 < var10.length; ++var11) {
                  var12 = var10[var11];
                  var1.h = (int)((float)var1.h + this.ak[var12]);
                  var1.g = (int)((float)var1.g + this.al[var12]);
                  var1.i = (int)((float)var1.i + this.af[var12]);
                  ++var7;
               }
            }
         }

         if (var7 > 0) {
            var1.h = var1.h / var7 + var4;
            var1.g = var1.g / var7 + var5;
            var1.i = var1.i / var7 + var6;
         } else {
            var1.h = var4;
            var1.g = var5;
            var1.i = var6;
         }
      } else {
         float[] var19;
         if (var2 == 1) {
            for(var8 = 0; var8 < var3.length; ++var8) {
               var9 = var3[var8];
               if (var9 < this.Z.length) {
                  var10 = this.Z[var9];

                  for(var11 = 0; var11 < var10.length; ++var11) {
                     var12 = var10[var11];
                     var19 = this.ak;
                     var19[var12] += (float)var4;
                     var19 = this.al;
                     var19[var12] += (float)var5;
                     var19 = this.af;
                     var19[var12] += (float)var6;
                  }
               }
            }
         } else {
            int var13;
            if (var2 == 2) {
               for(var8 = 0; var8 < var3.length; ++var8) {
                  var9 = var3[var8];
                  if (var9 < this.Z.length) {
                     var10 = this.Z[var9];

                     for(var11 = 0; var11 < var10.length; ++var11) {
                        var12 = var10[var11];
                        var19 = this.ak;
                        var19[var12] -= (float)var1.h;
                        var19 = this.al;
                        var19[var12] -= (float)var1.g;
                        var19 = this.af;
                        var19[var12] -= (float)var1.i;
                        var13 = (var4 & 255) * 8;
                        int var14 = (var5 & 255) * 8;
                        int var15 = (var6 & 255) * 8;
                        int var16;
                        int var17;
                        int var18;
                        if (var15 != 0) {
                           var16 = aW.d[var15];
                           var17 = aW.e[var15];
                           var18 = (int)this.ak[var12] * var17 + (int)this.al[var12] * var16 >> 16;
                           this.al[var12] = (float)((int)this.al[var12] * var17 - (int)this.ak[var12] * var16 >> 16);
                           this.ak[var12] = (float)var18;
                        }

                        if (var13 != 0) {
                           var16 = aW.d[var13];
                           var17 = aW.e[var13];
                           var18 = (int)this.al[var12] * var17 - (int)this.af[var12] * var16 >> 16;
                           this.af[var12] = (float)((int)this.af[var12] * var17 + (int)this.al[var12] * var16 >> 16);
                           this.al[var12] = (float)var18;
                        }

                        if (var14 != 0) {
                           var16 = aW.d[var14];
                           var17 = aW.e[var14];
                           var18 = (int)this.af[var12] * var16 + (int)this.ak[var12] * var17 >> 16;
                           this.af[var12] = (float)((int)this.af[var12] * var17 - (int)this.ak[var12] * var16 >> 16);
                           this.ak[var12] = (float)var18;
                        }

                        var19 = this.ak;
                        var19[var12] += (float)var1.h;
                        var19 = this.al;
                        var19[var12] += (float)var1.g;
                        var19 = this.af;
                        var19[var12] += (float)var1.i;
                     }
                  }
               }
            } else if (var2 == 3) {
               for(var8 = 0; var8 < var3.length; ++var8) {
                  var9 = var3[var8];
                  if (var9 < this.Z.length) {
                     var10 = this.Z[var9];

                     for(var11 = 0; var11 < var10.length; ++var11) {
                        var12 = var10[var11];
                        var19 = this.ak;
                        var19[var12] -= (float)var1.h;
                        var19 = this.al;
                        var19[var12] -= (float)var1.g;
                        var19 = this.af;
                        var19[var12] -= (float)var1.i;
                        this.ak[var12] = (float)((int)this.ak[var12] * var4 / 128);
                        this.al[var12] = (float)((int)this.al[var12] * var5 / 128);
                        this.af[var12] = (float)((int)this.af[var12] * var6 / 128);
                        var19 = this.ak;
                        var19[var12] += (float)var1.h;
                        var19 = this.al;
                        var19[var12] += (float)var1.g;
                        var19 = this.af;
                        var19[var12] += (float)var1.i;
                     }
                  }
               }
            } else if (var2 == 5 && this.aa != null && this.I != null) {
               for(var8 = 0; var8 < var3.length; ++var8) {
                  var9 = var3[var8];
                  if (var9 < this.aa.length) {
                     var10 = this.aa[var9];

                     for(var11 = 0; var11 < var10.length; ++var11) {
                        var12 = var10[var11];
                        var13 = (this.I[var12] & 255) + var4 * 8;
                        if (var13 < 0) {
                           var13 = 0;
                        } else if (var13 > 255) {
                           var13 = 255;
                        }

                        this.I[var12] = (byte)var13;
                     }
                  }
               }
            }
         }
      }

   }

   public dx h(int var1) {
      this.calculateExtreme(var1);
      dx var2 = this.l(var1);
      if (!av && var2 == null) {
         throw new AssertionError();
      } else {
         return var2;
      }
   }

   public void b(iX var1, int var2, int[] var3, int var4, int var5, int var6) {
      if ((bo.fe & 8) != 0) {
         this.a(var1, var2, var3, (float)var4, (float)var5, (float)var6);
      } else {
         this.a(var1, var2, var3, var4, var5, var6);
      }

   }

   public void g() {
      if (this.U != 2) {
         this.U = 2;
         float var1 = 0.0F;

         for(int var2 = 0; var2 < this.z; ++var2) {
            float var3 = this.ak[var2];
            float var4 = this.al[var2];
            float var5 = this.af[var2];
            float var6 = var4 * var4 + var3 * var3 + var5 * var5;
            if (var6 > var1) {
               var1 = var6;
            }
         }

         this.W = (int)Math.ceil(Math.sqrt((double)var1));
         this.Y = this.W;
         this.X = this.W + this.W;
      }

   }

   public void b(int var1, int var2, int var3) {
      if ((bo.fe & 64) != 0) {
         this.aj = new short[var2];
      }

   }

   public void a(iX var1, cz var2, int var3) {
      db var4 = var2.i;
      de var5 = var4.d;
      if (var5 != null) {
         var5.a(var1, var2, var3, (boolean[])null, false);
         this.a(var1, var5, var2.o);
      }

      if (var2.p) {
         this.c(var2, var3);
      }

      this.q();
   }

   public void h() {
      if (this.U != 1) {
         this.U = 1;
         float var1 = 0.0F;
         float var2 = 0.0F;
         float var3 = 0.0F;

         for(int var4 = 0; var4 < this.z; ++var4) {
            float var5 = this.ak[var4];
            float var6 = this.al[var4];
            float var7 = this.af[var4];
            if (-var6 > var1) {
               var1 = -var6;
            }

            if (var6 > var2) {
               var2 = var6;
            }

            float var8 = var5 * var5 + var7 * var7;
            if (var8 > var3) {
               var3 = var8;
            }
         }

         this.V = (int)Math.ceil((double)var2);
         super.cd = (int)Math.ceil((double)var1);
         this.W = (int)Math.ceil(Math.sqrt((double)var3));
         this.Y = (int)Math.ceil(Math.sqrt((double)(super.cd * super.cd + this.W * this.W)));
         this.X = this.Y + (int)Math.ceil(Math.sqrt((double)(this.V * this.V + this.W * this.W)));
      }

   }

   public void c(aH var1) {
      this.ay = var1.ay;
      this.az = var1.az;
      this.ag = var1.ag;
      this.aj = var1.aj;
   }

   public void d(aH var1) {
      if (var1 != null && this.aj != null && var1.aj != null) {
         for(int var2 = 0; var2 < var1.A; ++var2) {
            this.aj[this.A + var2] = var1.aj[var2];
         }
      }

   }

   public void i() {
      for(int var1 = 0; var1 < this.z; ++var1) {
         this.ak[var1] = -this.ak[var1];
         this.af[var1] = -this.af[var1];
      }

      this.q();
   }

   public void b(de var1, int var2) {
      this.a(iX.c, var1, var2);
   }

   public void c(int var1, int var2, int var3) {
      for(int var4 = 0; var4 < this.z; ++var4) {
         float[] var5 = this.ak;
         var5[var4] += (float)var1;
         var5 = this.al;
         var5[var4] += (float)var2;
         var5 = this.af;
         var5[var4] += (float)var3;
      }

      this.q();
   }

   public void b(cz var1, int var2) {
      db var3 = var1.i;
      byte[] var4 = this.getFaceTransparencies();

      for(int var5 = 0; var5 < var3.a; ++var5) {
         int var6 = var3.b[var5];
         if (var6 == 5 && var1.f != null && var1.f[var5 + 0] != 0 && this.aa != null && var4 != null) {
            int var7 = var1.f[var5 + 0];
            int[] var8 = var3.c[var5];
            int var9 = var8.length;

            for(int var10 = 0; var10 < var9; ++var10) {
               int var11 = var8[var10];
               if (var11 < this.aa.length) {
                  int[] var12 = this.aa[var11];

                  for(int var13 = 0; var13 < var12.length; ++var13) {
                     int var14 = var12[var13];
                     int var15 = (int)((float)(var4[var14] & 255) + var1.a(var7, (float)var2) * 255.0F);
                     if (var15 < 0) {
                        var15 = 0;
                     } else if (var15 > 255) {
                        var15 = 255;
                     }

                     var4[var14] = (byte)var15;
                  }
               }
            }
         }
      }

   }

   public void d(int var1, int var2, int var3) {
      for(int var4 = 0; var4 < this.z; ++var4) {
         this.ak[var4] = this.ak[var4] * (float)var1 / 128.0F;
         this.al[var4] = this.al[var4] * (float)var2 / 128.0F;
         this.af[var4] = this.af[var4] * (float)var3 / 128.0F;
      }

      this.q();
   }

   public aH a(int[][] var1, int var2, int var3, int var4, boolean var5, int var6) {
      int var7 = var6;
      int var8 = var4;
      int var9 = var3;
      int var10 = var2;
      int[][] var11 = var1;
      aH var12 = this;
      this.h();
      int var13 = var2 - this.W;
      int var14 = this.W + var2;
      int var15 = var4 - this.W;
      int var16 = this.W + var4;
      aH var17;
      if (var13 >= 0 && var14 + 128 >> 7 < var1.length && var15 >= 0 && var16 + 128 >> 7 < var1[0].length) {
         int var18 = var13 >> 7;
         int var19 = var14 + 127 >> 7;
         int var20 = var15 >> 7;
         int var21 = var16 + 127 >> 7;
         if (var1[var18][var20] == var3 && var1[var19][var20] == var3 && var1[var18][var21] == var3 && var1[var19][var21] == var3) {
            var17 = this;
         } else {
            aH var22;
            if (var5) {
               var22 = new aH(this);
               var22.al = new float[var22.z];
            } else {
               var22 = this;
            }

            int var23;
            int var24;
            int var25;
            int var26;
            int var27;
            int var28;
            int var29;
            int var30;
            int var31;
            int var32;
            if (var6 == 0) {
               for(var23 = 0; var23 < var22.z; ++var23) {
                  var24 = (int)var12.ak[var23] + var10;
                  var25 = (int)var12.af[var23] + var8;
                  var26 = var24 & 127;
                  var27 = var25 & 127;
                  var28 = var24 >> 7;
                  var29 = var25 >> 7;
                  var30 = (128 - var26) * var11[var28][var29] + var11[var28 + 1][var29] * var26 >> 7;
                  var31 = (128 - var26) * var11[var28][var29 + 1] + var11[var28 + 1][var29 + 1] * var26 >> 7;
                  var32 = (128 - var27) * var30 + var27 * var31 >> 7;
                  var22.al[var23] = (float)((int)var12.al[var23] + var32 - var9);
               }
            } else {
               for(var23 = 0; var23 < var22.z; ++var23) {
                  var24 = (-((int)var12.al[var23]) << 16) / var12.cd;
                  if (var24 < var7) {
                     var25 = (int)var12.ak[var23] + var10;
                     var26 = (int)var12.af[var23] + var8;
                     var27 = var25 & 127;
                     var28 = var26 & 127;
                     var29 = var25 >> 7;
                     var30 = var26 >> 7;
                     var31 = (128 - var27) * var11[var29][var30] + var11[var29 + 1][var30] * var27 >> 7;
                     var32 = (128 - var27) * var11[var29][var30 + 1] + var11[var29 + 1][var30 + 1] * var27 >> 7;
                     int var33 = (128 - var28) * var31 + var28 * var32 >> 7;
                     var22.al[var23] = (float)((var7 - var24) * (var33 - var9) / var7 + (int)var12.al[var23]);
                  } else {
                     var22.al[var23] = var12.al[var23];
                  }
               }
            }

            var22.a();
            var17 = var22;
         }
      } else {
         var17 = this;
      }

      if (this != var17 && (bo.fe & 2) == 2 && var6 == 0) {
         var17.ax = this;
      }

      return var17;
   }

   public void j() {
      for(int var1 = 0; var1 < this.z; ++var1) {
         float var2 = this.af[var1];
         this.af[var1] = this.ak[var1];
         this.ak[var1] = -var2;
      }

      this.q();
   }

   public void a(iX var1, de var2, int var3) {
      if (this.R != null) {
         for(int var4 = 0; var4 < this.z; ++var4) {
            int[] var5 = this.R[var4];
            if (var5 != null && var5.length != 0) {
               int[] var6 = this.S[var4];
               var1.a.k();

               for(int var7 = 0; var7 < var5.length; ++var7) {
                  int var8 = var5[var7];
                  cq var9 = var2.b(var8);
                  if (var9 != null) {
                     float var10 = (float)var6[var7] / 255.0F;
                     var1.f.i(var10, var10, var10);
                     var1.j.e(var9.a(var1, var3));
                     var1.j.c(var1.f);
                     var1.a.d(var1.j);
                  }
               }

               this.b(var4, var1.a);
            }
         }
      }

   }

   public void k() {
      if (this.ay == null && (bo.fe & 4) == 4) {
         int var1 = this.getVerticesCount();
         this.ay = new int[var1];
         this.az = new int[var1];
         this.ag = new int[var1];
         int[] var2 = this.getFaceIndices1();
         int[] var3 = this.getFaceIndices2();
         int[] var4 = this.getFaceIndices3();
         float[] var5 = this.getVerticesX();
         float[] var6 = this.getVerticesY();
         float[] var7 = this.getVerticesZ();

         for(int var8 = 0; var8 < this.getFaceCount(); ++var8) {
            int var9 = var2[var8];
            int var10 = var3[var8];
            int var11 = var4[var8];
            int var12 = (int)(var5[var10] - var5[var9]);
            int var13 = (int)(var6[var10] - var6[var9]);
            int var14 = (int)(var7[var10] - var7[var9]);
            int var15 = (int)(var5[var11] - var5[var9]);
            int var16 = (int)(var6[var11] - var6[var9]);
            int var17 = (int)(var7[var11] - var7[var9]);
            int var18 = var13 * var17 - var14 * var16;
            int var19 = var14 * var15 - var12 * var17;

            int var20;
            for(var20 = var12 * var16 - var13 * var15; var18 > 8192 || var19 > 8192 || var20 > 8192 || var18 < -8192 || var19 < -8192 || var20 < -8192; var20 >>= 1) {
               var18 >>= 1;
               var19 >>= 1;
            }

            int var21 = (int)Math.sqrt((double)(var20 * var20 + var18 * var18 + var19 * var19));
            if (var21 <= 0) {
               var21 = 1;
            }

            int var22 = var18 * 256 / var21;
            int var23 = var19 * 256 / var21;
            int var24 = var20 * 256 / var21;
            int[] var25 = this.ay;
            var25[var9] += var22;
            var25 = this.az;
            var25[var9] += var23;
            var25 = this.ag;
            var25[var9] += var24;
            var25 = this.ay;
            var25[var10] += var22;
            var25 = this.az;
            var25[var10] += var23;
            var25 = this.ag;
            var25[var10] += var24;
            var25 = this.ay;
            var25[var11] += var22;
            var25 = this.az;
            var25[var11] += var23;
            var25 = this.ag;
            var25[var11] += var24;
         }
      }

   }

   public static void j(int var0) {
      if (!av && !Client.s.isClientThread()) {
         throw new AssertionError();
      } else {
         iX var1 = iX.c;
         var1.h = var1.g = var1.i = 0;
         var1.m = var1.d = var1.b = 0.0F;
      }
   }

   public Model e(int var1, int var2, int var3) {
      return this.h(var1, var2, var3);
   }

   public aH l() {
      return this.ax;
   }

   public byte getOverrideAmount() {
      return this.ac != null ? this.ac.d : 0;
   }

   public float[] getVerticesZ() {
      return this.af;
   }

   public Model getUnskewedModel() {
      return this.l();
   }

   public float[] getVerticesY() {
      return this.al;
   }

   public void a(fY var1) {
      bE var2 = bE.d(0.0F, 0.0F, 0.0F);

      for(int var3 = 0; var3 < this.z; ++var3) {
         var2.e(this.ak[var3], this.al[var3], this.af[var3]);
         var2.b(var1);
         this.ak[var3] = var2.h;
         this.al[var3] = var2.i;
         this.af[var3] = var2.j;
      }

      var2.f();
      this.q();
   }

   public void a(aH[] var1, int var2) {
      this.k();
   }

   public int getBufferOffset() {
      return this.ah;
   }

   public void a(iX var1, dD var2, int var3) {
      if (this.Z != null && var3 != -1) {
         dq var4 = var2.a[var3];
         db var5 = var4.e;
         var1.h = 0;
         var1.g = 0;
         var1.i = 0;

         for(int var6 = 0; var6 < var4.f; ++var6) {
            int var7 = var4.h[var6];
            this.b(var1, var5.b[var7], var5.c[var7], var4.i[var6], var4.j[var6], var4.k[var6]);
         }

         this.q();
      }

   }

   public Shape a(bG var1, int var2, int var3, int var4, int var5) {
      int[] var6 = new int[this.z];
      int[] var7 = new int[this.z];
      Perspective.modelToCanvas(Client.s, var1, this.z, var2, var3, var5, var4, this.ak, this.af, this.al, var6, var7);
      return Jarvis.convexHull(var6, var7);
   }

   public void setUvBufferOffset(int var1) {
      this.ai = var1;
   }

   public byte getOverrideHue() {
      return this.ac != null ? this.ac.a : 0;
   }

   public void k(int var1) {
      for(int var2 = 0; var2 < this.z; ++var2) {
         float[] var3 = this.al;
         var3[var2] += (float)var1;
      }

   }

   public Model m() {
      return this.p();
   }

   public void setBufferOffset(int var1) {
      this.ah = var1;
   }

   public float[] getVerticesX() {
      return this.ak;
   }

   public aH a(iX var1, boolean var2) {
      if (!var2 && var1.l.length < this.A) {
         var1.l = new byte[this.A + 100];
      }

      return this.c(var2, var1.k, var1.l);
   }

   public void a(int var1, fX var2) {
      float var3 = this.ak[var1];
      float var4 = -this.al[var1];
      float var5 = -this.af[var1];
      float var6 = 1.0F;
      this.ak[var1] = var2.k * var6 + var2.n * var5 + var2.c * var3 + var2.l * var4;
      this.al[var1] = -(var2.p * var6 + var2.b * var5 + var2.f * var4 + var2.m * var3);
      this.af[var1] = -(var2.o * var6 + var2.g * var5 + var2.t * var4 + var2.r * var3);
   }

   public void a(iX var1, dD var2, int var3, dD var4, int var5, int var6, int var7) {
      if (this.Z != null && var3 != -1) {
         dq var8 = var2.a[var3];
         db var9 = var8.e;
         dq var10 = null;
         if (var4 != null) {
            var10 = var4.a[var5];
            if (var10.e != var9) {
               var10 = null;
            }
         }

         var1.m = var1.d = var1.b = 0.0F;
         this.a(var1, var9, var8, var10, (float)bo.ce + (float)var6, var7);
         this.q();
      }

   }

   public byte getOverrideSaturation() {
      return this.ac != null ? this.ac.b : 0;
   }

   public AABB getAABB(int var1) {
      return this.h(var1);
   }

   public void a(int var1, int[] var2, int var3, int var4, int var5) {
      if (!av && !Client.s.isClientThread()) {
         throw new AssertionError();
      } else {
         this.b(iX.c, var1, var2, var3, var4, var5);
      }
   }

   public void a(float var1) {
      for(int var2 = 0; var2 < this.z; ++var2) {
         float[] var3 = this.al;
         var3[var2] *= var1;
      }

   }

   public Model n() {
      return this.s();
   }

   public int[] getVertexNormalsZ() {
      return this.ag;
   }

   public void b(boolean var1, aH var2, byte[] var3) {
      var2.ay = this.ay;
      var2.az = this.az;
      var2.ag = this.ag;
      var2.aj = this.aj;
   }

   public int[] getVertexNormalsX() {
      return this.ay;
   }

   public int getSceneId() {
      return this.aw;
   }

   public int getUvBufferOffset() {
      return this.ai;
   }

   public int[] getVertexNormalsY() {
      return this.az;
   }

   public void setSceneId(int var1) {
      this.aw = var1;
   }

   public short[] getUnlitFaceColors() {
      return this.aj;
   }

   public Model f(int var1, int var2, int var3) {
      return this.g(var1, var2, var3);
   }

   public byte getOverrideLuminance() {
      return this.ac != null ? this.ac.c : 0;
   }

   public Model o() {
      return this.r();
   }

   public int getRadius() {
      return this.Y;
   }

   public aH p() {
      this.i();
      return this;
   }

   public aH g(int var1, int var2, int var3) {
      this.d(var1, var2, var3);
      return this;
   }

   public int[] getFaceColors2() {
      return this.F;
   }

   public void q() {
      this.a();
   }

   public int[] getTexIndices1() {
      return this.O;
   }

   public aH r() {
      this.c();
      return this;
   }

   public short[] getFaceTextures() {
      return this.K;
   }

   public void c(cz var1, int var2) {
      this.b(var1, var2);
   }

   public void calculateBoundsCylinder() {
      this.h();
   }

   public aH d(boolean var1) {
      return this.a(var1);
   }

   public boolean useBoundingBox() {
      return this.T;
   }

   public int[] getFaceIndices2() {
      return this.C;
   }

   public byte[] getTextureFaces() {
      return this.L;
   }

   public aH s() {
      this.j();
      return this;
   }

   public dx l(int var1) {
      return this.c(var1);
   }

   public byte[] getFaceRenderPriorities() {
      return this.J;
   }

   public aH e(boolean var1) {
      return this.b(var1);
   }

   public int getFaceCount() {
      return this.A;
   }

   public int getDiameter() {
      return this.X;
   }

   public aH h(int var1, int var2, int var3) {
      this.c(var1, var2, var3);
      return this;
   }

   public int[] getFaceColors1() {
      return this.E;
   }

   public int[] getFaceIndices1() {
      return this.B;
   }

   public int[] getFaceIndices3() {
      return this.D;
   }

   public int getVerticesCount() {
      return this.z;
   }

   public void drawFrustum(int var1, int var2, int var3, int var4, int var5, int var6, int var7) {
      this.a(var1, var2, var3, var4, var5, var6, var7);
   }

   public int[] getFaceColors3() {
      return this.G;
   }

   public void b(int var1, fX var2) {
      this.a(var1, var2);
   }

   public aH b(int[][] var1, int var2, int var3, int var4, boolean var5, int var6) {
      return this.a(var1, var2, var3, var4, var5, var6);
   }

   public aH c(boolean var1, aH var2, byte[] var3) {
      return this.a(var1, var2, var3);
   }

   public void drawOrtho(int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8) {
      this.a(var1, var2, var3, var4, var5, var6, var7, var8);
   }

   public int getBottomY() {
      return this.V;
   }

   public void calculateExtreme(int var1) {
      this.b(var1);
   }

   public int[] getTexIndices2() {
      return this.P;
   }

   public byte[] getFaceTransparencies() {
      return this.I;
   }

   public int[] getTexIndices3() {
      return this.Q;
   }

   public int getXYZMag() {
      return this.W;
   }

   public byte[] getFaceBias() {
      return this.H;
   }

   static {
      j = aW.d;
      k = aW.e;
      l = aW.a;
      m = aW.c;
      n = new float[3];
      o = new aH();
      p = new byte[1];
      q = new aH();
      r = new byte[1];
      s = new fX();
      t = new fX();
      u = new fX();
      v = new int[10];
      w = new int[10];
      x = new int[10];
      y = new float[10];
      av = !aH.class.desiredAssertionStatus();
   }
}
