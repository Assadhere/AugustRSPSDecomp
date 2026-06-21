package osrs;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.UnmodifiableIterator;
import com.google.common.primitives.Doubles;
import com.google.common.primitives.Floats;
import com.google.common.primitives.Ints;
import com.google.common.reflect.ClassPath;
import com.jagex.oldscape.pub.OAuthApi;
import custom.CustomPacketHandler;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.datatransfer.Clipboard;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.IntPredicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Named;
import net.runelite.api.Actor;
import net.runelite.api.Animation;
import net.runelite.api.BufferProvider;
import net.runelite.api.CameraFocusableEntity;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Constants;
import net.runelite.api.Deque;
import net.runelite.api.EnumComposition;
import net.runelite.api.FriendContainer;
import net.runelite.api.FriendsChatManager;
import net.runelite.api.GameState;
import net.runelite.api.GrandExchangeOffer;
import net.runelite.api.HashTable;
import net.runelite.api.IndexDataBase;
import net.runelite.api.IndexedSprite;
import net.runelite.api.InventoryID;
import net.runelite.api.ItemComposition;
import net.runelite.api.ItemContainer;
import net.runelite.api.IterableHashTable;
import net.runelite.api.Menu;
import net.runelite.api.MenuAction;
import net.runelite.api.MenuEntry;
import net.runelite.api.MessageNode;
import net.runelite.api.Model;
import net.runelite.api.ModelData;
import net.runelite.api.NPC;
import net.runelite.api.NPCComposition;
import net.runelite.api.NameableContainer;
import net.runelite.api.NodeCache;
import net.runelite.api.ObjectComposition;
import net.runelite.api.Perspective;
import net.runelite.api.Player;
import net.runelite.api.Point;
import net.runelite.api.Prayer;
import net.runelite.api.Preferences;
import net.runelite.api.Projectile;
import net.runelite.api.Projection;
import net.runelite.api.Rasterizer;
import net.runelite.api.RenderOverview;
import net.runelite.api.RuneLiteObject;
import net.runelite.api.RuneLiteObjectController;
import net.runelite.api.SceneTilePaint;
import net.runelite.api.ScriptEventBuilder;
import net.runelite.api.Skill;
import net.runelite.api.SpritePixels;
import net.runelite.api.StructComposition;
import net.runelite.api.TextureProvider;
import net.runelite.api.TileFunction;
import net.runelite.api.VarbitComposition;
import net.runelite.api.WidgetNode;
import net.runelite.api.World;
import net.runelite.api.WorldType;
import net.runelite.api.WorldView;
import net.runelite.api.clan.ClanChannel;
import net.runelite.api.clan.ClanRank;
import net.runelite.api.clan.ClanSettings;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.dbtable.DBRowConfig;
import net.runelite.api.events.AccountHashChanged;
import net.runelite.api.events.CanvasSizeChanged;
import net.runelite.api.events.ChatMessage;
import net.runelite.api.events.ClanChannelChanged;
import net.runelite.api.events.FriendsChatChanged;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.GrandExchangeOfferChanged;
import net.runelite.api.events.ItemDespawned;
import net.runelite.api.events.ItemSpawned;
import net.runelite.api.events.MenuOpened;
import net.runelite.api.events.MenuShouldLeftClick;
import net.runelite.api.events.NpcChanged;
import net.runelite.api.events.NpcSpawned;
import net.runelite.api.events.PlayerMenuOptionsChanged;
import net.runelite.api.events.PostMenuSort;
import net.runelite.api.events.ResizeableChanged;
import net.runelite.api.events.ScriptCallbackEvent;
import net.runelite.api.events.ScriptPostFired;
import net.runelite.api.events.ScriptPreFired;
import net.runelite.api.events.UsernameChanged;
import net.runelite.api.events.VarbitChanged;
import net.runelite.api.events.WorldChanged;
import net.runelite.api.events.WorldEntitySpawned;
import net.runelite.api.events.WorldViewLoaded;
import net.runelite.api.events.WorldViewUnloaded;
import net.runelite.api.hooks.Callbacks;
import net.runelite.api.hooks.DrawCallbacks;
import net.runelite.api.vars.AccountType;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetConfigNode;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.api.widgets.WidgetUtil;
import net.runelite.api.worldmap.MapElementConfig;
import net.runelite.api.worldmap.WorldMap;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.misc.Unsafe;

public final class Client extends T implements OAuthApi, net.runelite.api.Client, eU, gS {
   public static final boolean a;
   public static final iz b = new iz();
   @Inject
   public ScheduledExecutorService c;
   @com.google.inject.Inject(
      optional = true
   )
   @Named("scriptVmMaxOps")
   public int d = 600000;
   public File e;
   public static final ea f;
   public static final String g;
   public static final String h;
   public static int[] i;
   public static final int[] j;
   public static jf k;
   public static final int[] l;
   public static boolean m;
   public static final aZ n;
   public static bg o;
   public static jJ p;
   public static final gu q;
   public static final Map r;
   public static Client s;
   public static boolean t;
   public static int u;
   public static boolean v;
   public static int w;
   public static int x;
   public static ji y;
   public static hY z;
   public static boolean A;
   public static final jf B;
   public static HashMap C;
   public static final io D;
   public long E;
   public static fJ F;
   public static iH G;
   public static int H;
   public static boolean I;
   public static int J;
   public static int K;
   public static int L;
   public static boolean M;
   public static int N;
   public static boolean O;
   public static int P;
   public static int Q;
   public static int R;
   public static String S;
   public static int T;
   public static int U;
   public static int V;
   public static boolean W;
   public static int X;
   public static int[] Y;
   public static int[] Z;
   public static int[] aa;
   public static int[] ab;
   public static boolean ac;
   public static boolean ad;
   public static int ae;
   public static y af;
   public static aP ag;
   public static aP ah;
   public static long ai;
   public static boolean aj;
   public static int ak;
   public static int al;
   public static final q am;
   public static final am an;
   public static volatile long ao;
   public static cK[] ap;
   public static cI[] aq;
   public static short ar;
   public static short as;
   public static short at;
   public static short au;
   public static short av;
   public static short aw;
   public static short ax;
   public static short ay;
   public static int az;
   public static int aA;
   public static int aB;
   public static int aC;
   public static int aD;
   public static bp[] aE;
   public static g aF;
   public static gB aG;
   public static int aH;
   public static int aI;
   public static int aJ;
   public static int aK;
   public static int aL;
   public static boolean aM;
   public static String aN;
   public static int aO;
   public static int aP;
   public static int aQ;
   public static boolean aR;
   public static boolean aS;
   public static int aT;
   public static long aU;
   public static int aV;
   public static int aW;
   public static long aX;
   public static boolean aY;
   public static int aZ;
   public static String ba;
   public static boolean bb;
   public static aB bc;
   public static int bd;
   public static int be;
   public static int bf;
   public static String bg;
   public static int bh;
   public static int bi;
   public static int bj;
   public static int bk;
   public static int bl;
   public static int bm;
   public static int bn;
   public static int bo;
   public static int bp;
   public static int bq;
   public static int br;
   public static int bs;
   public static int bt;
   public static int bu;
   public static int bv;
   public static int bw;
   public static int bx;
   public static int by;
   public static int bz;
   public static boolean bA;
   public static int bB;
   public static int bC;
   public static int bD;
   public static String bE;
   public static jn bF;
   public static long bG;
   public static boolean bH;
   public static float bI;
   public static boolean bJ;
   public static boolean bK;
   public static H bL;
   public static eI bM;
   public static eI bN;
   public static boolean bO;
   public static boolean bP;
   public static boolean bQ;
   public static final fC bR;
   public static iA bS;
   public static iA bT;
   public static ii bU;
   public static int[] bV;
   public static byte[] bW;
   public static int bX;
   public static int[] bY;
   public static boolean bZ;
   public static ft ca;
   public static int cb;
   public static int cc;
   public static int cd;
   public static int ce;
   public static int cf;
   public static int cg;
   public static int ch;
   public static boolean ci;
   public static int cj;
   public static boolean ck;
   public static int cl;
   public static int cm;
   public static int cn;
   public static int co;
   public eI cp = new eI(16);
   public boolean cq;
   public int cr;
   public long cs;
   public hK ct;
   public static int cu;
   public static int cv;
   public static long cw;
   public static long cx;
   public static int cy;
   public static int cz;
   public static int cA;
   public static int cB;
   public static int[] cC;
   public static String[] cD;
   public static boolean[] cE;
   public static int[] cF;
   public static int cG;
   public static boolean cH;
   public static boolean cI;
   public static ig cJ;
   public static boolean cK;
   public static int cL;
   public static int cM;
   public static boolean cN;
   public static int cO;
   public static int cP;
   public static String cQ;
   public static String cR;
   public static int cS;
   public static long[] cT;
   public static int cU;
   public static int cV;
   public static int cW;
   public static int cX;
   public static int cY;
   public static boolean cZ;
   public static boolean da;
   public static boolean db;
   public static l dc;
   public static m dd;
   public static m de;
   public static boolean[] df;
   public static int[] dg;
   public static int[] dh;
   public static int[] di;
   public static int[] dj;
   public static hA dk;
   public static List dl;
   public static ArrayList dm;
   public static int dn;
   public static int do;
   public static int[] dp;
   public static int[] dq;
   public static ko dr;
   public static ko ds;
   public static boolean dt;
   public static boolean du;
   public static boolean dv;
   public static double dw;
   public static boolean[] dx;
   public static boolean dy;
   public static int dz;
   public static int[] dA;
   public static int[] dB;
   public static int dC;
   public static int dD;
   public static double dE;
   public static Logger dF;
   public static final av[] dG;
   public static boolean dH;
   public static List dI;
   public static List dJ;
   public static List dK;
   public static int dL;
   public static List dM;
   public static final ClassLoader dN;
   public static final Map dO;
   @com.google.inject.Inject(
      optional = true
   )
   @Named("scriptVmWarningOps")
   public int dP;
   public cG dQ;
   public cG dR;
   public cX dS;
   public static ae dT;
   public static bI dU;
   public aR dV;
   public bV dW;
   public a dX;
   public c dY;
   @Inject
   @Named("runeLiteDir")
   public File dZ;
   public String ea;
   public Properties eb;
   public Future ec;
   public Future ed;
   @Inject
   public Callbacks ee;
   public static DrawCallbacks ef;
   public boolean eg;
   @Inject
   @Named("insecureWriteCredentials")
   public boolean eh;
   private static long fi;
   private static ByteBuffer fj;
   static String ei;
   static String ej;
   public static Map<Integer, v> ek;
   public static Map<Integer, ag> el;
   static String em;
   static int en;

   public NodeCache getObjectCompositionCache() {
      return this.bC();
   }

   public void a(long var1) {
      double var3 = a(osrs.bo.fP, var1);
      double var5 = a(V, var1);
      osrs.bo.eR += var3 / 2.0;
      dw += var5 / 2.0;
      dw = Doubles.constrainToRange(dw, dt ? 0.0 : 0.39269909262657166, dt ? 1.5707963705062866 : 1.1750292778015137);
      br = b(osrs.bo.eR);
      bq = b(dw);
   }

   public static void a(dE var0, dE var1, aH var2, int var3, int var4, int var5, int var6, long var7) {
      if (var0 instanceof dc) {
         var7 = ((dc)var0).b(var7);
      }

      boolean var9 = var7 != 0L && (var7 >> 19 & 1L) != 1L;
      if (var9 && osrs.dB.b) {
         dx var10 = var2.h(var3);
         boolean var11 = a(var0, var4, var5, var6, var10.getCenterX(), var10.getCenterY(), var10.getCenterZ(), var10.getExtremeX(), var10.getExtremeY(), var10.getExtremeZ());
         if (var11) {
            t var12 = osrs.bo.dZ;
            int var13;
            if (var1 == null && var12 != null && var12.u().isTopLevel()) {
               var13 = (int)(var7 >> 16 & 7L);
               boolean var14 = var13 == 2 || var13 == 3;
               int var15 = (int)(var7 & 127L);
               int var16 = (int)(var7 >> 7 & 127L);
               int var17 = var12.af[0];
               int var18 = var12.ag[0];
               if (var14 && Math.max(Math.abs(var15 - var17), Math.abs(var16 - var18)) > 50) {
                  return;
               }
            }

            if (var2.useBoundingBox()) {
               float var43 = a((float)var4 - osrs.bo.fL + (float)var10.getCenterX(), (float)var5 - osrs.bo.fi + (float)var10.getCenterY(), (float)var6 - osrs.bo.fN + (float)var10.getCenterZ(), (float)var10.getExtremeX(), (float)var10.getExtremeY(), (float)var10.getExtremeZ(), (float)osrs.bo.fE, (float)osrs.bo.fF, (float)osrs.bo.fW, (float)osrs.bo.a, (float)osrs.bo.fj, (float)osrs.bo.fQ);
               if (var0 instanceof dc) {
                  ((dc)var0).a();
               }

               a(var7, (int)var43);
            } else {
               var13 = var2.getVerticesCount();
               int var44 = var2.getFaceCount();
               osrs.aH.a(var13);
               float[] var45 = var2.getVerticesX();
               float[] var46 = var2.getVerticesY();
               float[] var47 = var2.getVerticesZ();
               int[] var48 = var2.getFaceIndices1();
               int[] var19 = var2.getFaceIndices2();
               int[] var20 = var2.getFaceIndices3();
               int[] var21 = var2.getFaceColors3();
               int var22 = s.get3dZoom();
               int var23 = s.getCenterX();
               int var24 = s.getCenterY();
               int var25 = Perspective.SINE[var3];
               int var26 = Perspective.COSINE[var3];

               int var29;
               int var30;
               int var31;
               int var32;
               int var33;
               int var35;
               int var36;
               int var37;
               for(int var27 = 0; var27 < var13; ++var27) {
                  int var28 = (int)var45[var27];
                  var29 = (int)var46[var27];
                  var30 = (int)var47[var27];
                  if (var3 != 0) {
                     var31 = var25 * var30 + var26 * var28 >> 16;
                     var30 = var26 * var30 - var25 * var28 >> 16;
                     var28 = var31;
                  }

                  var31 = var4 + var28;
                  var32 = var5 + var29;
                  var33 = var6 + var30;
                  float[] var34 = var0.project((float)var31, (float)var32, (float)var33);
                  var35 = (int)var34[0];
                  var36 = (int)var34[1];
                  var37 = (int)var34[2];
                  if (var37 >= 50) {
                     osrs.aH.ap[var27] = (float)(var22 * var35 / var37 + var23);
                     osrs.aH.aq[var27] = (float)(var22 * var36 / var37 + var24);
                     osrs.aH.ar[var27] = var34[2] * var34[2] + var34[0] * var34[0] + var34[1] * var34[1];
                  } else {
                     osrs.aH.ap[var27] = -5000.0F;
                  }
               }

               boolean var49 = true;
               float var50 = Float.POSITIVE_INFINITY;

               for(var29 = 0; var29 < var44; ++var29) {
                  if (var21[var29] != -2) {
                     var30 = var48[var29];
                     var31 = var19[var29];
                     var32 = var20[var29];
                     var33 = (int)osrs.aH.ap[var30];
                     int var52 = (int)osrs.aH.ap[var31];
                     var35 = (int)osrs.aH.ap[var32];
                     var36 = (int)osrs.aH.aq[var30];
                     var37 = (int)osrs.aH.aq[var31];
                     int var38 = (int)osrs.aH.aq[var32];
                     if (var33 != -5000 && var52 != -5000 && var35 != -5000) {
                        int var39 = osrs.dB.d + 5;
                        if (var39 >= var36 || var39 >= var37 || var39 >= var38) {
                           int var40 = osrs.dB.d - 5;
                           if (var40 <= var36 || var40 <= var37 || var40 <= var38) {
                              int var41 = osrs.dB.c + 5;
                              if (var41 >= var33 || var41 >= var52 || var41 >= var35) {
                                 int var42 = osrs.dB.c - 5;
                                 if (var42 <= var33 || var42 <= var52 || var42 <= var35) {
                                    var50 = Math.min(var50, osrs.aH.ar[var30] + osrs.aH.ar[var31] + osrs.aH.ar[var32]);
                                 }
                              }
                           }
                        }
                     }
                  }
               }

               if (var50 != Float.POSITIVE_INFINITY) {
                  if (var0 instanceof dc) {
                     ((dc)var0).a();
                  }

                  float var51 = var50 / 3.0F;
                  a(var7, (int)var51);
               }
            }
         }
      }

   }

   public static dG a(dh var0, dh var1, aC var2, int[][] var3, int var4, int var5, int var6, int var7, int var8, int var9, int var10, int var11, int var12, int var13) {
      long var14;
      if (var2.n == null) {
         var14 = (long)((var2.l << 10) + var7);
      } else {
         var14 = (long)((var2.l << 10) + (var6 << 3) + var7);
      }

      Object var16 = (dG)osrs.aC.i.c(var14);
      if (var16 == null) {
         aH var17 = a(var0, var1, var2, var4, var5, var8, var9, var10);
         int var10003;
         if (var17 != null) {
            if (!a && var2.z) {
               throw new AssertionError();
            }

            aH var19;
            if (var2.y < 0) {
               ++osrs.bo.c;
               var10003 = dA[var4]++;
               var19 = new aH(var17);
               osrs.aC.i.b(var19, var14);
               return var19;
            }

            var19 = var17.ax;
            if (var19 != null) {
               aH var20 = new aH(var19);
               aH var21 = new aH(var17);
               var21.ax = var20;
               osrs.aC.i.b(var20, var14);
               ++osrs.bo.eI;
               var10003 = dA[var4]++;
               return var21;
            }

            var10003 = i[var4]++;
         } else if (!var2.z) {
            var10003 = dB[var4]++;
         }

         ModelUnlit var22 = var2.c(var6, var7);
         if (var22 == null) {
            return null;
         }

         if (!var2.z) {
            var16 = var22.b(var2.D + 64, var2.E + 768, -50, -10, -50);
         } else {
            var22.q = (short)(var2.D + 64);
            var22.r = (short)(var2.E + 768);
            var22.B();
            var16 = var22;
         }

         osrs.aC.i.b((aA)var16, var14);
      }

      if (var2.z) {
         var16 = ((ModelUnlit)var16).x();
      }

      if (var2.y >= 0) {
         if (var16 instanceof aH) {
            var16 = ((aH)var16).b(var3, var11, var12, var13, true, var2.y);
         } else if (var16 instanceof ModelUnlit) {
            var16 = ((ModelUnlit)var16).b(var3, var11, var12, var13, true, var2.y);
         }
      }

      return (dG)var16;
   }

   public void changeMemoryMode(boolean var1) {
      aM = var1;
      osrs.dh.c = var1;
      osrs.bo.dv = true;
      if (this.getGameState() == GameState.LOGGED_IN) {
         this.setGameState(GameState.LOADING);
      }

   }

   public static String a(String var0) {
      return s.i(var0);
   }

   public final boolean a(iz var1, int var2) {
      if (osrs.bo.fZ) {
         return false;
      } else {
         iz var3 = var1;
         Client var4 = this;
         gO var5 = var1.e();
         bu var6 = var1.e;
         boolean var7;
         boolean var8;
         if (var5 == null) {
            var8 = false;
            var7 = var8;
         } else {
            label3263: {
               String var11;
               int var12;
               int var10000;
               boolean var36;
               try {
                  label3183: {
                     if (var3.k > 0 && !var5.a(var3.k)) {
                        var7 = false;
                        break label3263;
                     }

                     int var31;
                     if (var3.f == null) {
                        if (var3.h) {
                           if (!var5.a(1)) {
                              var7 = false;
                              return var7;
                           }

                           var3.l += var5.a((byte[])var3.e.c, 0, 1);
                           var3.i = 0;
                           var3.h = false;
                        }

                        var6.d = 0;
                        if (var6.ak()) {
                           if (!var5.a(1)) {
                              var7 = false;
                              return var7;
                           }

                           var3.l += var5.a((byte[])var3.e.c, 1, 1);
                           var3.i = 0;
                        }

                        var3.h = true;
                        bm[] var9 = osrs.bm.a();
                        var31 = var6.al();
                        if (var31 < 0 || var31 >= var9.length) {
                           throw new IOException("" + var31 + " " + var6.d);
                        }

                        var3.f = var9[var31];
                        var3.g = var3.f.bu;
                     }

                     if (var3.g == -1) {
                        if (!var5.a(1)) {
                           var7 = false;
                           return var7;
                        }

                        var3.l += var5.a((byte[])var6.c, 0, 1);
                        var3.g = var6.c[0] & 255;
                     }

                     if (var3.g == -2) {
                        if (!var5.a(2)) {
                           var7 = false;
                           return var7;
                        }

                        var3.l += var5.a((byte[])var6.c, 0, 2);
                        var6.d = 0;
                        var3.g = var6.d();
                     }

                     if (!var5.a(var3.g)) {
                        var7 = false;
                        return var7;
                     }

                     var6.d = 0;
                     var3.l += var5.a((byte[])var6.c, 0, var3.g);
                     var3.i = 0;
                     ca.a();
                     var3.o = var3.n;
                     var3.n = var3.m;
                     var3.m = var3.f;
                     int var29;
                     ag var33;
                     String var110;
                     if (osrs.bm.bC == var3.f) {
                        var29 = var6.h();
                        var110 = ((aR)var6).m();
                        var33 = osrs.bo.cR.a(var29);
                        if (!var110.equals(var33.ar)) {
                           var33.ar = var110;
                        }

                        var3.f = null;
                        var7 = true;
                        return var7;
                     }

                     if (osrs.bm.aA == var3.f) {
                        ++aT;
                        var7 = true;
                        aX();
                        var3.f = null;
                        var8 = true;
                        return var8;
                     }

                     if (osrs.bm.ba == var3.f) {
                        var29 = var6.Q();
                        var31 = var6.F();
                        var33 = osrs.bo.cR.a(var29);
                        if (var33 != null && var33.B == 0) {
                           if (var31 > var33.w - var33.m) {
                              var31 = var33.w - var33.m;
                           }

                           if (var31 < 0) {
                              var31 = 0;
                           }

                           if (var33.ap != var31) {
                              var33.ap = var31;
                           }
                        }

                        var3.f = null;
                        var7 = true;
                        return var7;
                     }

                     if (osrs.bm.z == var3.f) {
                        var29 = ((aR)var6).b();
                        D(var29);
                        var3.f = null;
                        var7 = true;
                        return var7;
                     }

                     if (osrs.bm.aj == var3.f) {
                        bR.a(var6);
                        var3.f = null;
                        var7 = true;
                        return var7;
                     }

                     int var13;
                     int var14;
                     int var15;
                     int var18;
                     int var37;
                     int var49;
                     int var62;
                     if (osrs.bm.ay == var3.f) {
                        var29 = var6.F();
                        var31 = var6.z();
                        var37 = var31 >> 2;
                        var12 = var31 & 3;
                        var13 = j[var37];
                        var14 = ((aR)var6).f();
                        var15 = var14 >> 16;
                        var49 = var14 >> 8 & 255;
                        var62 = (var14 >> 4 & 7) + var15;
                        var18 = (var14 & 7) + var49;
                        if (0 <= var62 && var62 < osrs.bo.U.p - 1 && 0 <= var18 && var18 < osrs.bo.U.q - 1) {
                           c(var62, var18, var37, var12, var13, var29);
                        }

                        var3.f = null;
                        var7 = true;
                        return var7;
                     }

                     if (osrs.bm.bI == var3.f) {
                        a(osrs.eL.h);
                        var3.f = null;
                        var7 = true;
                        return var7;
                     }

                     if (osrs.bm.aX == var3.f) {
                        S = ((aR)var6).m();
                        var3.f = null;
                        var7 = true;
                        return var7;
                     }

                     if (osrs.bm.al == var3.f) {
                        var29 = ((aR)var6).b();
                        if (((aR)var6).b() == 0) {
                           aE[var29] = new bp();
                           y(var29);
                           var6.d += 18;
                        } else {
                           --var6.d;
                           aE[var29] = new bp(var6, false);
                           y(var29);
                        }

                        p.e();
                        var3.f = null;
                        var7 = true;
                        return var7;
                     }

                     String var32;
                     if (osrs.bm.b == var3.f) {
                        var32 = ((aR)var6).m();
                        var110 = osrs.aX.a(osrs.bo.b(jy.a(var6)));
                        iw.a(6, var32, var110);
                        var3.f = null;
                        var7 = true;
                        return var7;
                     }

                     if (osrs.bm.u == var3.f) {
                        byte[] var117 = new byte[var3.g];
                        var6.e(var117, 0, var117.length);
                        aR var115 = new aR(var117);
                        var11 = var115.m();
                        osrs.dG.a(var11, 465024219);
                        var3.f = null;
                        var7 = true;
                        return var7;
                     }

                     if (osrs.bm.h == var3.f) {
                        for(var29 = 0; var29 < osrs.x.c.length; ++var29) {
                           if (osrs.x.c[var29] != osrs.x.b[var29]) {
                              osrs.x.c[var29] = osrs.x.b[var29];
                              B(var29);
                              osrs.bo.j(var29);
                              p.a(var29);
                           }
                        }

                        var3.f = null;
                        var7 = true;
                        return var7;
                     }

                     if (osrs.bm.J == var3.f) {
                        osrs.bo.aR = ih.a(((aR)var6).b());
                        var3.f = null;
                        var7 = true;
                        return var7;
                     }

                     ag var45;
                     if (osrs.bm.w == var3.f) {
                        var29 = var6.O();
                        var31 = var6.H();
                        if (var31 == 65535) {
                           var31 = -1;
                        }

                        var37 = var6.P();
                        var45 = osrs.bo.cR.a(var37);
                        aO var98;
                        if (!var45.a) {
                           if (var31 == -1) {
                              var45.p = 0;
                              var3.f = null;
                              var7 = true;
                              return var7;
                           }

                           var98 = osrs.aO.a(var31).d(var29);
                           var45.p = 4;
                           var45.aM = var31;
                           var45.C = var98.r;
                           var45.k = var98.s;
                           var45.Y = var98.q * 100 / var29;
                        } else {
                           var45.g = var31;
                           var45.aL = var29;
                           var98 = osrs.aO.a(var31).d(var29);
                           var45.C = var98.r;
                           var45.k = var98.s;
                           var45.M = var98.t;
                           var45.D = var98.u;
                           var45.t = var98.v;
                           var45.Y = var98.q;
                           if (var98.w == 1) {
                              var45.s = 1;
                           } else {
                              var45.s = 2;
                           }

                           if (var45.ag > 0) {
                              var45.Y = var45.Y * 32 / var45.ag;
                           } else if (var45.bR > 0) {
                              var45.Y = var45.Y * 32 / var45.bR;
                           }
                        }

                        var3.f = null;
                        var7 = true;
                        return var7;
                     }

                     if (osrs.bm.ae == var3.f) {
                        osrs.bo.d(var3.e);
                        var3.f = null;
                        var7 = true;
                        return var7;
                     }

                     if (osrs.bm.G == var3.f) {
                        var29 = var6.h();
                        var31 = var6.d();
                        var33 = osrs.bo.cR.a(var29);
                        if (var33.p != 6 || var33.aM != var31) {
                           var33.p = 6;
                           var33.aM = var31;
                        }

                        var3.f = null;
                        var7 = true;
                        return var7;
                     }

                     t var53;
                     if (osrs.bm.d == var3.f) {
                        Iterator var116 = osrs.bo.U.r.iterator();

                        while(var116.hasNext()) {
                           var53 = (t)var116.next();
                           var53.d();
                        }

                        Iterator var113 = osrs.bo.U.s.iterator();

                        while(var113.hasNext()) {
                           s var112 = (s)var113.next();
                           var112.d();
                        }

                        var3.f = null;
                        var7 = true;
                        return var7;
                     }

                     if (osrs.bm.aE == var3.f) {
                        var29 = var6.d();
                        var31 = var6.P();
                        var37 = var29 >> 10 & 31;
                        var12 = var29 >> 5 & 31;
                        var13 = var29 & 31;
                        var14 = (var13 << 3) + (var37 << 19) + (var12 << 11);
                        ag var94 = osrs.bo.cR.a(var31);
                        if (var94.ci != var14) {
                           var94.ci = var14;
                        }

                        var3.f = null;
                        var7 = true;
                        return var7;
                     }

                     if (osrs.bm.bn == var3.f) {
                        var29 = var6.h();
                        if (var29 == -1) {
                           cV = 0;
                           cW = 0;
                           var3.f = null;
                           var7 = true;
                           return var7;
                        }

                        var31 = osrs.bo.c(var29);
                        var37 = osrs.bo.d(var29);
                        bG var103 = D.a(var31, var37);
                        B var95 = var103.a(0, var31, var37);
                        cV = var95.b;
                        cW = var95.c;
                        var3.f = null;
                        var7 = true;
                        return var7;
                     }

                     if (osrs.bm.an == var3.f) {
                        var29 = var6.P();
                        var31 = ((aR)var6).b();
                        var37 = var6.B();
                        var45 = osrs.bo.cR.a(var29);
                        var45.al.b(var37, var31);
                        var3.f = null;
                        var7 = true;
                        return var7;
                     }

                     boolean var108;
                     if (osrs.bm.k == var3.f) {
                        var29 = var6.O();
                        var108 = var6.B() == 1;
                        var33 = osrs.bo.cR.a(var29);
                        if (var33.x != var108) {
                           var33.x = var108;
                        }

                        var3.f = null;
                        var7 = true;
                        return var7;
                     }

                     if (osrs.bm.B == var3.f) {
                        bK = false;
                        bJ = false;
                        cZ = false;
                        da = false;
                        osrs.bo.bu = 0;
                        osrs.bo.eb = 0;
                        osrs.bo.u = 0;
                        db = false;
                        osrs.bo.v = 0;
                        osrs.bo.X = 0;
                        osrs.bo.aH = 0;
                        osrs.bo.bG = 0;
                        osrs.bo.dB = 0;
                        osrs.bo.fl = 0;
                        osrs.bo.n = 0;
                        dc = null;
                        de = null;
                        dd = null;

                        for(var29 = 0; var29 < 5; ++var29) {
                           df[var29] = false;
                           z(var29);
                        }

                        var3.f = null;
                        var7 = true;
                        return var7;
                     }

                     if (osrs.bm.bh == var3.f) {
                        var29 = var6.F();
                        s var111 = (s)osrs.bo.U.s.a((long)var29);
                        var37 = var6.z();
                        var12 = var6.H();
                        var13 = var6.O();
                        if (var111 != null) {
                           var111.a(var37, var12, var13 >> 16, var13 & '\uffff');
                        }

                        var3.f = null;
                        var7 = true;
                        return var7;
                     }

                     if (osrs.bm.x == var3.f) {
                        osrs.bo.V = var6.k();
                        var3.f = null;
                        var7 = true;
                        return var7;
                     }

                     if (osrs.bm.S == var3.f) {
                        osrs.bo.O = var6.B();
                        osrs.bo.ci = ((aR)var6).b();
                        cc = var6.B();

                        for(var29 = osrs.bo.ci; var29 < osrs.bo.ci + 8; ++var29) {
                           for(var31 = osrs.bo.O; var31 < osrs.bo.O + 8; ++var31) {
                              if (osrs.bo.U.a[cc][var29][var31] != null) {
                                 osrs.bo.U.a[cc][var29][var31] = null;
                                 c(cc, var29, var31);
                              }
                           }
                        }

                        for(ic var114 = (ic)osrs.bo.U.b.d(); var114 != null; var114 = (ic)osrs.bo.U.b.f()) {
                           if (var114.h >= osrs.bo.ci && var114.h < osrs.bo.ci + 8 && var114.i >= osrs.bo.O && var114.i < osrs.bo.O + 8 && cc == var114.e) {
                              var114.c = 0;
                           }
                        }

                        var3.f = null;
                        var7 = true;
                        return var7;
                     }

                     if (osrs.bm.br == var3.f) {
                        osrs.bo.a(osrs.bo.U, var6);
                        var3.f = null;
                        var7 = true;
                        return var7;
                     }

                     if (osrs.bm.bB == var3.f) {
                        bx = var6.e();
                        p.g();
                        var3.f = null;
                        var7 = true;
                        return var7;
                     }

                     if (osrs.bm.V == var3.f) {
                        a(osrs.eL.a);
                        var3.f = null;
                        var7 = true;
                        return var7;
                     }

                     if (osrs.bm.bE == var3.f) {
                        var36 = var6.k();
                        if (var36) {
                           if (osrs.bo.dx == null) {
                              osrs.bo.dx = new fL();
                           }
                        } else {
                           osrs.bo.dx = null;
                        }

                        var3.f = null;
                        var7 = true;
                        return var7;
                     }

                     if (osrs.bm.av == var3.f) {
                        var29 = var6.H();
                        var31 = var6.G();
                        var37 = var6.G();
                        if (var37 == 65535) {
                           var37 = -1;
                        }

                        var12 = var6.F();
                        var13 = var6.F();
                        if (var13 == 65535) {
                           var13 = -1;
                        }

                        var14 = var6.G();
                        ArrayList var93 = new ArrayList();
                        var93.add(var13);
                        var93.add(var37);
                        a(var93, var14, var29, var12, var31);
                        var3.f = null;
                        var7 = true;
                        return var7;
                     }

                     if (osrs.bm.ap == var3.f) {
                        e(((aR)var6).m());
                        var3.f = null;
                        var7 = true;
                        return var7;
                     }

                     if (osrs.bm.aQ == var3.f) {
                        osrs.eM.g = ((aR)var6).b();
                        var3.f = null;
                        var7 = true;
                        return var7;
                     }

                     if (osrs.bm.ai == var3.f) {
                        var29 = var6.H();
                        var31 = var6.G();
                        var37 = var6.F();
                        var12 = var6.H();
                        osrs.bo.a(var37, var29, var12, var31);
                        var3.f = null;
                        var7 = true;
                        return var7;
                     }

                     if (osrs.bm.bd != var3.f) {
                        if (osrs.bm.bA == var3.f) {
                           osrs.bn.l.d();
                           p.b();
                           var3.f = null;
                           var7 = true;
                           return var7;
                        }

                        if (osrs.bm.A == var3.f) {
                           var3.k = var6.e();
                           var3.f = null;
                           var7 = true;
                           return var7;
                        }

                        if (osrs.bm.bl == var3.f) {
                           bJ = true;
                           bK = false;
                           cZ = true;
                           osrs.bo.dB = var6.d() - osrs.bo.aQ.B;
                           var29 = var6.H();
                           osrs.bo.fl = var6.d() - osrs.bo.aQ.z;
                           var31 = var6.G();
                           var37 = ((aR)var6).b();
                           db = (var6.B() & 1) == 1;
                           var12 = osrs.bo.f(osrs.bo.dB);
                           var13 = osrs.bo.f(osrs.bo.fl);
                           boolean var87 = false;
                           boolean var90 = false;
                           if (db) {
                              var49 = osrs.bo.bD;
                              var62 = b(osrs.bo.aQ, var12, var13, osrs.bo.aQ.y) - var31;
                           } else {
                              var49 = b(osrs.bo.aQ, osrs.bo.bM, osrs.bo.dO, osrs.bo.aQ.y) - osrs.bo.bD;
                              var62 = var31;
                           }

                           dc = new ay(osrs.bo.bM, osrs.bo.dO, var49, var12, var13, var62, var29, var37);
                           var3.f = null;
                           var7 = true;
                           return var7;
                        }

                        int var21;
                        int var22;
                        byte var51;
                        dQ var71;
                        if (osrs.bm.n == var3.f) {
                           var51 = var6.c();
                           var110 = ((aR)var6).m();
                           long var107 = (long)var6.d();
                           long var88 = (long)((aR)var6).f();
                           j var89 = (j)kk.a(osrs.j.a(), ((aR)var6).b());
                           long var80 = (var107 << 32) + var88;
                           boolean var91 = false;
                           var71 = null;
                           cI var81 = var51 >= 0 ? aq[var51] : osrs.bo.z;
                           if (var81 == null) {
                              var91 = true;
                           } else {
                              var21 = 0;

                              while(true) {
                                 if (var21 >= 100) {
                                    if (var89.e && osrs.bn.l.a(new I(var110, osrs.bo.cp))) {
                                       var91 = true;
                                    }
                                    break;
                                 }

                                 if (cT[var21] == var80) {
                                    var91 = true;
                                    break;
                                 }

                                 ++var21;
                              }
                           }

                           if (!var91) {
                              cT[cU] = var80;
                              cU = (cU + 1) % 100;
                              String var83 = osrs.aX.a(jy.a(var6));
                              var22 = var51 >= 0 ? 41 : 44;
                              if (var89.c != -1) {
                                 osrs.bo.a(var22, osrs.bq.a(var89.c) + var110, var83, var81.a);
                              } else {
                                 osrs.bo.a(var22, var110, var83, var81.a);
                              }
                           }

                           var3.f = null;
                           var7 = true;
                           return var7;
                        }

                        if (osrs.bm.N == var3.f) {
                           var29 = ((aR)var6).b();
                           f(var29);
                           var3.f = null;
                           var7 = false;
                           return var7;
                        }

                        if (osrs.bm.aL == var3.f) {
                           bC = ((aR)var6).A();
                           bD = var6.z();
                           var3.f = null;
                           var7 = true;
                           return var7;
                        }

                        if (osrs.bm.bK == var3.f) {
                           p.d();
                           var51 = var6.c();
                           cA var109 = new cA(var6);
                           cI var106;
                           if (var51 >= 0) {
                              var106 = aq[var51];
                           } else {
                              var106 = osrs.bo.z;
                           }

                           if (var106 == null) {
                              var4.K(var51);
                              var3.f = null;
                              var7 = true;
                              return var7;
                           }

                           if (var109.a > var106.e) {
                              var4.K(var51);
                              var3.f = null;
                              var7 = true;
                              return var7;
                           }

                           if (var109.a < var106.e) {
                              var3.f = null;
                              var7 = true;
                              return var7;
                           }

                           var109.a(var106);
                           var3.f = null;
                           var7 = true;
                           return var7;
                        }

                        if (osrs.bm.bc == var3.f) {
                           var29 = var6.Q();
                           var31 = var6.Q();
                           var37 = var6.F();
                           var12 = var6.O();
                           var13 = osrs.bo.b(var31);
                           var14 = osrs.bo.c(var31);
                           var15 = osrs.bo.d(var31);
                           bG var76 = D.a(var14, var15);
                           var62 = var14 - var76.B;
                           var18 = var15 - var76.z;
                           if (var76.b(var13, var62, var18)) {
                              osrs.bo.a(var76, var13, var62, var18, var37, var29, var12);
                           }

                           var3.f = null;
                           var7 = true;
                           return var7;
                        }

                        if (osrs.bm.bs == var3.f) {
                           bJ = true;
                           bK = false;
                           cZ = false;
                           osrs.bo.aH = var6.z();
                           osrs.bo.fl = var6.d() - osrs.bo.aQ.z;
                           osrs.bo.dB = var6.d() - osrs.bo.aQ.B;
                           osrs.bo.bG = var6.z();
                           osrs.bo.n = var6.F();
                           if (osrs.bo.aH >= 100) {
                              osrs.bo.bM = osrs.bo.f(osrs.bo.dB);
                              osrs.bo.dO = osrs.bo.f(osrs.bo.fl);
                              osrs.bo.bD = b(osrs.bo.aQ, osrs.bo.bM, osrs.bo.dO, osrs.bo.aQ.y) - osrs.bo.n;
                           }

                           var3.f = null;
                           var7 = true;
                           return var7;
                        }

                        if (osrs.bm.m == var3.f) {
                           var29 = ((aR)var6).A();
                           var31 = var6.h();
                           var37 = var6.B();
                           var12 = var6.z();
                           aa[var29] = var31;
                           Y[var29] = var37;
                           Z[var29] = 1;
                           ab[var29] = var12;

                           for(var13 = 0; var13 < fn.b.length - 1; ++var13) {
                              if (var31 >= fn.b[var13]) {
                                 Z[var29] = var13 + 2;
                              }
                           }

                           p.b(var29);
                           var3.f = null;
                           var7 = true;
                           return var7;
                        }

                        if (osrs.bm.v == var3.f) {
                           var29 = var6.q();
                           var108 = ((aR)var6).b() == 1;
                           var11 = "";
                           boolean var96 = false;
                           if (var108) {
                              var11 = ((aR)var6).m();
                              if (osrs.bn.l.a(new I(var11, osrs.bo.cp))) {
                                 var96 = true;
                              }
                           }

                           String var79 = ((aR)var6).m();
                           if (!var96) {
                              iw.a(var29, var11, var79);
                           }

                           var3.f = null;
                           var7 = true;
                           return var7;
                        }

                        if (osrs.bm.f == var3.f) {
                           bJ = true;
                           bK = false;
                           da = true;
                           var29 = var6.e();
                           var31 = var6.e();
                           var37 = i(osrs.bo.bV + var31 & 2027);
                           var12 = osrs.bo.cV + var29;
                           var13 = var6.d();
                           var14 = ((aR)var6).b();
                           de = new m(osrs.bo.bV, var37, var13, var14);
                           dd = new m(osrs.bo.cV, var12, var13, var14);
                           var3.f = null;
                           var7 = true;
                           return var7;
                        }

                        if (osrs.bm.T == var3.f) {
                           var36 = ((aR)var6).b() == 1;
                           if (var36) {
                              osrs.bo.eJ = osrs.bd.a() - var6.i();
                              aF = new g(var6, true);
                           } else {
                              aF = null;
                           }

                           p.f();
                           var3.f = null;
                           var7 = true;
                           return var7;
                        }

                        if (osrs.bm.am == var3.f) {
                           osrs.bo.a(var3.e);
                           var3.f = null;
                           var7 = true;
                           return var7;
                        }

                        if (osrs.bm.c == var3.f) {
                           a(osrs.eL.i);
                           var3.f = null;
                           var7 = true;
                           return var7;
                        }

                        if (osrs.bm.aq == var3.f) {
                           osrs.bo.E = (fh)kk.a(osrs.fh.a(), ((aR)var6).b());
                           T = var6.e();
                           U = var6.d();
                           if (kt.a < 237) {
                              ++T;
                              if (osrs.bo.E == osrs.fh.c) {
                                 ++U;
                              }
                           }

                           var3.f = null;
                           var7 = true;
                           return var7;
                        }

                        if (osrs.bm.g == var3.f) {
                           var29 = var6.h();
                           if (ch != var29) {
                              ch = var29;
                              ak();
                           }

                           var3.f = null;
                           var7 = true;
                           return var7;
                        }

                        if (osrs.bm.ad == var3.f) {
                           var29 = var6.h();
                           var31 = var6.P();
                           osrs.bo.cv.a(var31, var29, osrs.bo.cR, o);
                           var3.f = null;
                           var7 = true;
                           return var7;
                        }

                        if (osrs.bm.K == var3.f) {
                           var29 = var6.I();
                           var31 = var6.I();
                           var37 = var6.h();
                           var45 = osrs.bo.cR.a(var37);
                           if (var45.L != var31 || var45.n != var29 || var45.v != 0 || var45.aj != 0) {
                              var45.L = var31;
                              var45.n = var29;
                              var45.v = 0;
                              var45.aj = 0;
                              jF.a(var45, osrs.T.eP, osrs.T.eQ, osrs.bo.cR, o);
                              if (var45.B == 0) {
                                 jF.a(osrs.bo.cR.m[var37 >> 16], var45, false, osrs.bo.cR, o);
                              }
                           }

                           var3.f = null;
                           var7 = true;
                           return var7;
                        }

                        if (osrs.bm.aP == var3.f) {
                           var29 = var6.h();
                           var31 = var6.B();
                           short var104 = (short)var6.K();
                           var12 = var6.F();
                           s var75 = (s)osrs.bo.U.s.a((long)var12);
                           if (var75 != null) {
                              var75.a(var31, var29, var104);
                           }

                           var3.f = null;
                           var7 = true;
                           return var7;
                        }

                        int var19;
                        bG var70;
                        if (osrs.bm.bp == var3.f) {
                           var29 = var6.K();
                           var31 = var6.Q();
                           var37 = var6.e();
                           var12 = var6.F();
                           short var74 = (short)var6.K();
                           short var82 = (short)var6.e();
                           var15 = var6.h();
                           var49 = var6.F();
                           var62 = osrs.bo.b(var15);
                           var18 = osrs.bo.c(var15);
                           var19 = osrs.bo.d(var15);
                           var70 = D.a(var18, var19);
                           var21 = var18 - var70.B;
                           var22 = var19 - var70.z;
                           if (var70.b(var62, var21, var22)) {
                              a(var70, var62, var21, var22, var49, var31, var74, var29, var82, var37, var12);
                           }

                           var3.f = null;
                           var7 = true;
                           return var7;
                        }

                        if (osrs.bm.E == var3.f) {
                           var29 = var6.O();
                           ag var105 = osrs.bo.cR.a(var29);

                           for(var37 = 0; var37 < var105.bd.length; ++var37) {
                              var105.bd[var37] = -1;
                              var105.bd[var37] = 0;
                           }

                           var3.f = null;
                           var7 = true;
                           return var7;
                        }

                        if (osrs.bm.au == var3.f) {
                           osrs.bo.a(osrs.bo.aQ, var6, 2);
                           var3.f = null;
                           var7 = true;
                           return var7;
                        }

                        if (osrs.bm.aV == var3.f) {
                           osrs.bo.eW = var6.k();
                           var3.f = null;
                           var7 = true;
                           return var7;
                        }

                        bG var59;
                        if (osrs.bm.aT == var3.f) {
                           var29 = var6.P();
                           var31 = var6.P();
                           var37 = var6.H();
                           var12 = osrs.bo.b(var31);
                           var13 = osrs.bo.c(var31);
                           var14 = osrs.bo.d(var31);
                           var59 = D.a(var13, var14);
                           var49 = var13 - var59.B;
                           var62 = var14 - var59.z;
                           if (var59.b(var12, var49, var62)) {
                              c(var59, var12, var49, var62, var37, var29);
                           }

                           var3.f = null;
                           var7 = true;
                           return var7;
                        }

                        if (osrs.bm.aC == var3.f) {
                           ag();
                           var51 = var6.c();
                           if (var3.g == 1) {
                              if (var51 >= 0) {
                                 ap[var51] = null;
                              } else {
                                 osrs.bo.bA = null;
                              }

                              var3.f = null;
                              var7 = true;
                              return var7;
                           }

                           if (var51 >= 0) {
                              ap[var51] = new cK(var6);
                           } else {
                              osrs.bo.bA = new cK(var6);
                           }

                           var3.f = null;
                           var7 = true;
                           return var7;
                        }

                        if (osrs.bm.as == var3.f) {
                           var29 = var6.h();
                           var31 = ((aR)var6).J();
                           var33 = osrs.bo.cR.a(var29);
                           if (var33.K != var31 || var31 == -1) {
                              var33.K = var31;
                              var33.ai = 0;
                              var33.o = 0;
                           }

                           var3.f = null;
                           var7 = true;
                           return var7;
                        }

                        if (osrs.bm.F == var3.f) {
                           osrs.bo.cj = var6.k();
                           var3.f = null;
                           var7 = true;
                           return var7;
                        }

                        t var43;
                        if (osrs.bm.q == var3.f) {
                           var29 = var6.B();
                           var31 = var6.H();
                           var43 = (t)osrs.bo.U.r.a((long)var31);
                           var12 = var6.G();
                           var13 = var6.O();
                           if (var43 != null) {
                              var43.a(var29, var12, var13 >> 16, var13 & '\uffff');
                           }

                           var3.f = null;
                           var7 = true;
                           return var7;
                        }

                        if (osrs.bm.bH == var3.f) {
                           var29 = var6.e();
                           var31 = ((aR)var6).b();
                           if (kt.a < 237) {
                              ++var29;
                           }

                           bG var100;
                           if (var29 == 0) {
                              var100 = osrs.bo.aQ;
                              osrs.bo.aQ.c(var31);
                              var100.y = var31;
                              osrs.bo.U = osrs.bo.aQ;
                           } else {
                              gZ var86 = (gZ)osrs.bo.aQ.t.a((long)var29);
                              if (var86 == null) {
                                 throw new RuntimeException("No valid ClientWorldEntity with ID " + var29);
                              }

                              var100 = var86.n;
                              var86.n.c(var31);
                              var100.y = var31;
                              osrs.bo.U = var86.n;
                           }

                           var3.f = null;
                           var7 = true;
                           return var7;
                        }

                        if (osrs.bm.I == var3.f) {
                           var29 = ((aR)var6).b();
                           var31 = ((aR)var6).b();
                           var37 = ((aR)var6).b();
                           var12 = ((aR)var6).b();
                           df[var29] = true;
                           z(var29);
                           dg[var29] = var31;
                           dh[var29] = var37;
                           di[var29] = var12;
                           dj[var29] = 0;
                           var3.f = null;
                           var7 = true;
                           return var7;
                        }

                        if (osrs.bm.aO == var3.f) {
                           var6.d += 28;
                           if (var6.y()) {
                              a((aR)var6, var6.d - 28);
                           }

                           var3.f = null;
                           var7 = true;
                           return var7;
                        }

                        if (osrs.bm.aF != var3.f) {
                           if (osrs.bm.bz == var3.f) {
                              var29 = var6.h();
                              var31 = var6.h();
                              var37 = osrs.T.bS();
                              r var84 = osrs.r.a(osrs.u.q, b.p);
                              var84.f.v(var29);
                              var84.f.t(var31);
                              var84.f.o(eC);
                              var84.f.o(var37);
                              b.a(var84);
                              var3.f = null;
                              var7 = true;
                              return var7;
                           }

                           if (osrs.bm.ag == var3.f) {
                              var29 = var6.e();
                              hf var102 = (hf)kk.a(hf.a(), ((aR)var6).b());
                              hp var97 = (hp)kk.a(hp.a(), ((aR)var6).b());
                              if (var29 == -2) {
                                 D.a(var102, var97);
                              } else {
                                 D.a(var29, var102, var97);
                              }

                              var3.f = null;
                              var7 = true;
                              return var7;
                           }

                           if (osrs.bm.aU == var3.f) {
                              osrs.bo.bs = new hc(ah);
                              var3.f = null;
                              var7 = true;
                              return var7;
                           }

                           if (osrs.bm.L == var3.f) {
                              var29 = var6.h();
                              bt var101 = (bt)osrs.bo.cR.x.a((long)var29);
                              if (var101 != null) {
                                 osrs.bo.cv.a(var101, true, osrs.bo.cR);
                              }

                              osrs.bo.cR.h();
                              var3.f = null;
                              var7 = true;
                              return var7;
                           }

                           if (osrs.bm.ak == var3.f) {
                              a(osrs.eL.c);
                              var3.f = null;
                              var7 = true;
                              return var7;
                           }

                           if (osrs.bm.j != var3.f) {
                              if (osrs.bm.aD == var3.f) {
                                 var29 = ((aR)var6).A();
                                 var31 = ((aR)var6).b();
                                 var11 = ((aR)var6).m();
                                 if (var31 >= 1 && var31 <= 8) {
                                    if (var11.equalsIgnoreCase(osrs.bv.k)) {
                                       var11 = null;
                                    }

                                    cD[var31 - 1] = var11;
                                    ai(var31 - 1);
                                    cE[var31 - 1] = var29 == 0;
                                 }

                                 var3.f = null;
                                 var7 = true;
                                 return var7;
                              }

                              if (osrs.bm.ar == var3.f) {
                                 p.d();
                                 var51 = var6.c();
                                 if (var3.g == 1) {
                                    if (var51 >= 0) {
                                       aq[var51] = null;
                                       S(var51);
                                    } else {
                                       osrs.bo.z = null;
                                       aa(-1);
                                    }

                                    var3.f = null;
                                    var7 = true;
                                    return var7;
                                 }

                                 if (var51 >= 0) {
                                    aq[var51] = new cI(var6);
                                    S(var51);
                                 } else {
                                    osrs.bo.z = new cI(var6);
                                    aa(-1);
                                 }

                                 var3.f = null;
                                 var7 = true;
                                 return var7;
                              }

                              if (osrs.bm.bb == var3.f) {
                                 var36 = var6.z() == 1;
                                 var31 = var6.O();
                                 var43 = J();
                                 if (var43 != null) {
                                    var45 = osrs.bo.cR.a(var31);
                                    var45.a(var43.aO, var36);
                                 }

                                 var3.f = null;
                                 var7 = true;
                                 return var7;
                              }

                              if (osrs.bm.aw == var3.f) {
                                 osrs.bn.l.d.a(var6, var3.g);
                                 af();
                                 p.b();
                                 var3.f = null;
                                 var7 = true;
                                 return var7;
                              }

                              if (osrs.bm.bJ == var3.f) {
                                 var7 = var4.b(var3);
                                 return var7;
                              }

                              bt var68;
                              if (osrs.bm.aa == var3.f) {
                                 var29 = var3.g + var6.d;
                                 var31 = var6.d();
                                 if (var31 == 65535) {
                                    var31 = -1;
                                 }

                                 var37 = var6.d();
                                 if (osrs.bo.cR.o != var31) {
                                    osrs.bo.cv.a(var31, osrs.T.eP, osrs.T.eQ, osrs.bo.cR, o);
                                 }

                                 for(; var37-- > 0; var68.a = true) {
                                    var13 = var6.h();
                                    var14 = var6.d();
                                    var15 = ((aR)var6).b();
                                    var68 = (bt)osrs.bo.cR.x.a((long)var13);
                                    if (var68 != null && var68.c != var14) {
                                       osrs.bo.cv.a(var68, true, osrs.bo.cR);
                                       var68 = null;
                                    }

                                    if (var68 == null) {
                                       var68 = osrs.bo.cv.b(var13, var14, var15, osrs.bo.cR, o);
                                    }
                                 }

                                 for(bt var64 = (bt)osrs.bo.cR.x.b(); var64 != null; var64 = (bt)osrs.bo.cR.x.c()) {
                                    if (var64.a) {
                                       var64.a = false;
                                    } else {
                                       osrs.bo.cv.a(var64, true, osrs.bo.cR);
                                    }
                                 }

                                 osrs.bo.cR.p = new fT(512);

                                 while(var6.d < var29) {
                                    var13 = var6.h();
                                    var14 = var6.d();
                                    if (var14 == 65535) {
                                       var14 = -1;
                                    }

                                    var15 = var6.d();
                                    if (var15 == 65535) {
                                       var15 = -1;
                                    }

                                    var49 = var6.h();
                                    var62 = var6.h();
                                    eO var85 = osrs.eO.a((eO)null, var14, var15, var49, var62);
                                    osrs.bo.cR.p.a(var85, (long)var13);
                                 }

                                 var3.f = null;
                                 var7 = true;
                                 return var7;
                              }

                              if (osrs.bm.af == var3.f) {
                                 var29 = var6.F();
                                 byte var99 = var6.E();
                                 osrs.x.b[var29] = var99;
                                 if (osrs.x.c[var29] != var99) {
                                    osrs.x.c[var29] = var99;
                                    B(var29);
                                 }

                                 osrs.bo.j(var29);
                                 p.a(var29);
                                 var3.f = null;
                                 var7 = true;
                                 return var7;
                              }

                              if (osrs.bm.bt == var3.f) {
                                 bJ = true;
                                 bK = false;
                                 da = false;
                                 osrs.bo.bu = var6.G() - osrs.bo.aQ.B;
                                 osrs.bo.v = var6.B();
                                 osrs.bo.u = var6.H();
                                 osrs.bo.X = ((aR)var6).b();
                                 osrs.bo.eb = var6.d() - osrs.bo.aQ.z;
                                 if (osrs.bo.X >= 100) {
                                    var29 = osrs.bo.f(osrs.bo.bu);
                                    var31 = osrs.bo.f(osrs.bo.eb);
                                    var37 = b(osrs.bo.aQ, var29, var31, osrs.bo.aQ.y) - osrs.bo.u;
                                    var12 = var29 - osrs.bo.bM;
                                    var13 = var37 - osrs.bo.bD;
                                    var14 = var31 - osrs.bo.dO;
                                    var15 = (int)Math.sqrt((double)(var12 * var12 + var14 * var14));
                                    osrs.bo.bV = (int)(Math.atan2((double)var13, (double)var15) * 325.9490051269531) & 2047;
                                    osrs.bo.cV = (int)(Math.atan2((double)var12, (double)var14) * -325.9490051269531) & 2047;
                                    if (osrs.bo.bV < 128) {
                                       osrs.bo.bV = 128;
                                    }

                                    if (osrs.bo.bV > 383) {
                                       osrs.bo.bV = 383;
                                    }
                                 }

                                 var3.f = null;
                                 var7 = true;
                                 return var7;
                              }

                              if (osrs.bm.O == var3.f) {
                                 var29 = var6.Q();
                                 var31 = ((aR)var6).A();
                                 var37 = var6.d();
                                 var68 = (bt)osrs.bo.cR.x.a((long)var29);
                                 if (var68 != null) {
                                    osrs.bo.cv.a(var68, var68.c != var37, osrs.bo.cR);
                                 }

                                 osrs.bo.cv.b(var29, var37, var31, osrs.bo.cR, o);
                                 var3.f = null;
                                 var7 = true;
                                 return var7;
                              }

                              if (osrs.bm.bk == var3.f) {
                                 var29 = var6.Q();
                                 var31 = var6.H();
                                 var37 = var6.O();
                                 var12 = osrs.bo.b(var29);
                                 var13 = osrs.bo.c(var29);
                                 var14 = osrs.bo.d(var29);
                                 var59 = D.a(var13, var14);
                                 var49 = var13 - var59.B;
                                 var62 = var14 - var59.z;
                                 if (var59.b(var12, var49, var62)) {
                                    b(var59, var12, var49, var62, var31, var37);
                                 }

                                 var3.f = null;
                                 var7 = true;
                                 return var7;
                              }

                              boolean var54;
                              boolean var60;
                              if (osrs.bm.bq == var3.f) {
                                 bJ = true;
                                 bK = false;
                                 cZ = true;
                                 var29 = osrs.bo.f(var6.d() - osrs.bo.aQ.z);
                                 var31 = ((aR)var6).A();
                                 var37 = var6.d();
                                 var12 = var6.H();
                                 var13 = osrs.bo.f(var6.d() - osrs.bo.aQ.B);
                                 osrs.bo.fl = var6.F() - osrs.bo.aQ.z;
                                 db = (((aR)var6).A() & 1) == 1;
                                 osrs.bo.dB = var6.H() - osrs.bo.aQ.B;
                                 var14 = osrs.bo.f(osrs.bo.dB);
                                 var15 = osrs.bo.f(osrs.bo.fl);
                                 var60 = false;
                                 var54 = false;
                                 if (db) {
                                    var18 = osrs.bo.bD;
                                    var19 = b(osrs.bo.aQ, var14, var15, osrs.bo.aQ.y) - var37;
                                 } else {
                                    var18 = b(osrs.bo.aQ, osrs.bo.bM, osrs.bo.dO, osrs.bo.aQ.y) - osrs.bo.bD;
                                    var19 = var37;
                                 }

                                 dc = new h(osrs.bo.bM, osrs.bo.dO, var18, var14, var15, var19, var13, var29, var12, var31);
                                 var3.f = null;
                                 var7 = true;
                                 return var7;
                              }

                              long var42;
                              long var46;
                              long var63;
                              String var65;
                              if (osrs.bm.ao == var3.f) {
                                 var51 = var6.c();
                                 var46 = (long)var6.d();
                                 var42 = (long)((aR)var6).f();
                                 var63 = (var46 << 32) + var42;
                                 var60 = false;
                                 cI var73 = var51 >= 0 ? aq[var51] : osrs.bo.z;
                                 if (var73 == null) {
                                    var60 = true;
                                 } else {
                                    for(var18 = 0; var18 < 100; ++var18) {
                                       if (cT[var18] == var63) {
                                          var60 = true;
                                          break;
                                       }
                                    }
                                 }

                                 if (!var60) {
                                    cT[cU] = var63;
                                    cU = (cU + 1) % 100;
                                    var65 = jy.a(var6);
                                    var19 = var51 >= 0 ? 43 : 46;
                                    osrs.bo.a(var19, "", var65, var73.a);
                                 }

                                 var3.f = null;
                                 var7 = true;
                                 return var7;
                              }

                              if (osrs.bm.bf == var3.f) {
                                 var29 = var6.H();
                                 var31 = var6.O();
                                 var33 = osrs.bo.cR.a(var31);
                                 if (var33.p != 2 || var33.aM != var29) {
                                    var33.p = 2;
                                    var33.aM = var29;
                                 }

                                 var3.f = null;
                                 var7 = true;
                                 return var7;
                              }

                              int var20;
                              if (osrs.bm.o == var3.f) {
                                 var32 = ((aR)var6).m();
                                 var46 = var6.i();
                                 var42 = (long)var6.d();
                                 var63 = (long)((aR)var6).f();
                                 j var58 = (j)kk.a(osrs.j.a(), ((aR)var6).b());
                                 long var72 = (var42 << 32) + var63;
                                 boolean var77 = false;

                                 for(var20 = 0; var20 < 100; ++var20) {
                                    if (cT[var20] == var72) {
                                       var77 = true;
                                       break;
                                    }
                                 }

                                 if (var58.e && osrs.bn.l.a(new I(var32, osrs.bo.cp))) {
                                    var77 = true;
                                 }

                                 if (!var77 && N == 0) {
                                    cT[cU] = var72;
                                    cU = (cU + 1) % 100;
                                    String var78 = osrs.aX.a(osrs.bo.b(jy.a(var6)));
                                    if (var58.c != -1) {
                                       osrs.bo.a(9, osrs.bq.a(var58.c) + var32, var78, gh.a(var46));
                                    } else {
                                       osrs.bo.a(9, var32, var78, gh.a(var46));
                                    }
                                 }

                                 var3.f = null;
                                 var7 = true;
                                 return var7;
                              }

                              if (osrs.bm.D == var3.f) {
                                 for(var29 = 0; var29 < osrs.bD.c; ++var29) {
                                    bD var92 = osrs.bD.a(var29);
                                    if (var92 != null) {
                                       osrs.x.b[var29] = 0;
                                       osrs.x.c[var29] = 0;
                                       B(var29);
                                    }
                                 }

                                 p.i();
                                 var3.f = null;
                                 var7 = true;
                                 return var7;
                              }

                              if (osrs.bm.aW == var3.f) {
                                 bw = var6.d();
                                 p.g();
                                 var3.f = null;
                                 var7 = true;
                                 return var7;
                              }

                              if (osrs.bm.s == var3.f) {
                                 var29 = var6.d();
                                 var31 = var6.H();
                                 var37 = var6.G();
                                 var12 = var6.F();
                                 var13 = var6.d();
                                 if (var13 == 65535) {
                                    var13 = -1;
                                 }

                                 ArrayList var61 = new ArrayList();
                                 var61.add(var13);
                                 a(var61, var31, var37, var12, var29);
                                 var3.f = null;
                                 var7 = true;
                                 return var7;
                              }

                              if (osrs.bm.M == var3.f) {
                                 osrs.bo.a(osrs.bo.U, true, var6);
                                 var3.f = null;
                                 var7 = true;
                                 return var7;
                              }

                              if (osrs.bm.bG == var3.f) {
                                 osrs.bo.O = ((aR)var6).A();
                                 cc = var6.B();
                                 osrs.bo.ci = var6.B();
                                 var3.f = null;
                                 var7 = true;
                                 return var7;
                              }

                              if (osrs.bm.bo == var3.f) {
                                 bJ = true;
                                 bK = false;
                                 da = true;
                                 osrs.bo.u = var6.F();
                                 osrs.bo.eb = var6.F() - osrs.bo.aQ.z;
                                 var29 = ((aR)var6).b();
                                 osrs.bo.bu = var6.d() - osrs.bo.aQ.B;
                                 var31 = var6.d();
                                 var37 = osrs.bo.f(osrs.bo.bu);
                                 var12 = osrs.bo.f(osrs.bo.eb);
                                 var13 = b(osrs.bo.aQ, var37, var12, osrs.bo.aQ.y) - osrs.bo.u;
                                 var14 = var37 - osrs.bo.bM;
                                 var15 = var13 - osrs.bo.bD;
                                 var49 = var12 - osrs.bo.dO;
                                 double var69 = Math.sqrt((double)(var14 * var14 + var49 * var49));
                                 var19 = i((int)(Math.atan2((double)var15, var69) * 325.9490051269531) & 2047);
                                 var20 = j((int)(Math.atan2((double)var14, (double)var49) * -325.9490051269531) & 2047);
                                 de = new m(osrs.bo.bV, var19, var31, var29);
                                 dd = new m(osrs.bo.cV, var20, var31, var29);
                                 var3.f = null;
                                 var7 = true;
                                 return var7;
                              }

                              if (osrs.bm.bj == var3.f) {
                                 var29 = var6.O();
                                 var31 = var6.Q();
                                 var33 = osrs.bo.cR.a(var31);
                                 if (var33.p != 1 || var33.aM != var29) {
                                    var33.p = 1;
                                    var33.aM = var29;
                                 }

                                 var3.f = null;
                                 var7 = true;
                                 return var7;
                              }

                              if (osrs.bm.bw == var3.f) {
                                 var32 = ((aR)var6).m();
                                 aZ = var6.H() * 30;
                                 if ("\u0018".equals(var32)) {
                                    ba = "";
                                 } else if (var32 != null && !var32.isEmpty()) {
                                    ba = var32;
                                 }

                                 p.g();
                                 var3.f = null;
                                 var7 = true;
                                 return var7;
                              }

                              if (osrs.bm.bF == var3.f) {
                                 var29 = var6.F();
                                 var31 = var6.B();
                                 var37 = var6.H();
                                 if (var29 == 65535) {
                                    var29 = -1;
                                 }

                                 s var55 = (s)osrs.bo.U.s.a((long)var37);
                                 if (var55 != null) {
                                    bk var48 = var55.P.c();
                                    if (var29 == ((o)var55).c() && var29 != -1) {
                                       var14 = var48.B;
                                       if (var14 == 1) {
                                          var55.P.h();
                                          var55.V = var31;
                                       } else if (var14 == 2) {
                                          var55.P.i();
                                       }
                                    } else if (var29 == -1 || var48 == null || osrs.bk.b(var29).v >= var48.v) {
                                       var55.P.a(var29);
                                       var55.P.h();
                                       var55.V = var31;
                                       var55.l = var55.T;
                                    }
                                 }

                                 var3.f = null;
                                 var7 = true;
                                 return var7;
                              }

                              if (osrs.bm.ab != var3.f) {
                                 if (osrs.bm.X == var3.f) {
                                    G.a(var6, var3.g);
                                    al();
                                    var3.f = null;
                                    var7 = true;
                                    return var7;
                                 }

                                 if (osrs.bm.Z == var3.f) {
                                    var7 = var4.a(var3);
                                    return var7;
                                 }

                                 if (osrs.bm.aH == var3.f) {
                                    osrs.bo.b(var3.e);
                                    var3.f = null;
                                    var7 = true;
                                    return var7;
                                 }

                                 if (osrs.bm.aG == var3.f) {
                                    var29 = var6.Q();
                                    var31 = var6.d();
                                    var37 = var6.F();
                                    var45 = osrs.bo.cR.a(var29);
                                    var45.z = (var31 << 16) + var37;
                                    var3.f = null;
                                    var7 = true;
                                    return var7;
                                 }

                                 if (osrs.bm.y == var3.f) {
                                    osrs.bo.c(var3.e);
                                    var3.f = null;
                                    var7 = true;
                                    return var7;
                                 }

                                 if (osrs.bm.bx == var3.f) {
                                    var29 = var6.F();
                                    im.b(var29);
                                    p.c(var29);
                                    var3.f = null;
                                    var7 = true;
                                    return var7;
                                 }

                                 if (osrs.bm.aI == var3.f) {
                                    var29 = var6.H();
                                    var31 = var6.P();
                                    var33 = osrs.bo.cR.a(var31);
                                    if (var33.p != 1 || var33.aM != var29) {
                                       var33.p = 1;
                                       var33.aM = var29;
                                    }

                                    var3.f = null;
                                    var7 = true;
                                    return var7;
                                 }

                                 if (osrs.bm.aB == var3.f) {
                                    osrs.bn.l.a((aR)var6, var3.g);
                                    p.b();
                                    var3.f = null;
                                    var7 = true;
                                    return var7;
                                 }

                                 if (osrs.bm.aZ == var3.f) {
                                    var29 = var6.e();
                                    D.d(var29);
                                    var3.f = null;
                                    var7 = true;
                                    return var7;
                                 }

                                 if (osrs.bm.bi == var3.f) {
                                    osrs.fd.a(var6, var3.g);
                                    var3.f = null;
                                    var7 = true;
                                    return var7;
                                 }

                                 if (osrs.bm.ah == var3.f) {
                                    var29 = var6.P();
                                    var31 = ((aR)var6).A();
                                    boolean var66 = ((aR)var6).b() == 1;
                                    var12 = var6.B();
                                    var13 = var6.h();
                                    var14 = var6.G();
                                    var15 = var6.d();
                                    var49 = var6.F();
                                    var62 = osrs.bo.b(var13);
                                    var18 = osrs.bo.c(var13);
                                    var19 = osrs.bo.d(var13);
                                    var70 = D.a(var18, var19);
                                    var21 = var18 - var70.B;
                                    var22 = var19 - var70.z;
                                    if (var70.b(var62, var21, var22)) {
                                       osrs.bo.a(var70, var62, var21, var22, var14, var29, var12, var49, var15, var31, var66);
                                    }

                                    var3.f = null;
                                    var7 = true;
                                    return var7;
                                 }

                                 if (osrs.bm.aS == var3.f) {
                                    osrs.bo.bs = null;
                                    var3.f = null;
                                    var7 = true;
                                    return var7;
                                 }

                                 if (osrs.bm.l == var3.f) {
                                    var6.i();
                                    var3.f = null;
                                    var7 = true;
                                    return var7;
                                 }

                                 if (osrs.bm.a == var3.f) {
                                    var29 = var6.Q();
                                    var31 = var6.O();
                                    var33 = osrs.bo.cR.a(var31);
                                    var33.al.c(var29);
                                    var3.f = null;
                                    var7 = true;
                                    return var7;
                                 }

                                 if (osrs.bm.bD == var3.f) {
                                    osrs.bo.aC = ((aR)var6).b();
                                    osrs.bo.R = ((aR)var6).b();
                                    var3.f = null;
                                    var7 = true;
                                    return var7;
                                 }

                                 if (osrs.bm.az == var3.f) {
                                    var29 = var6.P();
                                    var31 = osrs.bo.b(var29);
                                    var37 = osrs.bo.c(var29);
                                    var12 = osrs.bo.d(var29);
                                    var13 = var6.P();
                                    var14 = osrs.bo.b(var13);
                                    var15 = osrs.bo.c(var13);
                                    var49 = osrs.bo.d(var13);
                                    var62 = var6.H();
                                    var18 = var6.G();
                                    var19 = var6.d();
                                    var20 = var6.G();
                                    var21 = var6.L();
                                    var22 = var6.g();
                                    int var23 = var6.d();
                                    int var24 = var6.d();
                                    int var25 = var6.z();
                                    if (var23 != 65535) {
                                       in var26 = new in(var14, var15, var49, var18, var21, var31, var37, var12, var62, var22, var23, x + var19, x + var20, var25, var24);
                                       F.a((az)var26);
                                    }

                                    var3.f = null;
                                    var7 = true;
                                    return var7;
                                 }

                                 if (osrs.bm.H == var3.f) {
                                    var29 = var6.G();
                                    var31 = var6.G();
                                    var37 = var6.h();
                                    var12 = var6.G();
                                    ag var44 = osrs.bo.cR.a(var37);
                                    if (var44.C != var12 || var44.k != var29 || var44.Y != var31) {
                                       var44.C = var12;
                                       var44.k = var29;
                                       var44.Y = var31;
                                    }

                                    var3.f = null;
                                    var7 = true;
                                    return var7;
                                 }

                                 if (osrs.bm.p == var3.f) {
                                    osrs.bo.cv.a(jG.a, osrs.bo.cR);
                                    var3.f = null;
                                    var7 = true;
                                    return var7;
                                 }

                                 if (osrs.bm.Y == var3.f) {
                                    osrs.bo.o();
                                    var3.f = null;
                                    var7 = false;
                                    return var7;
                                 }

                                 if (osrs.bm.U == var3.f) {
                                    var29 = var6.F();
                                    var31 = var6.G();
                                    var37 = ((aR)var6).b();
                                    var12 = ((aR)var6).f();
                                    var13 = var12 >> 16;
                                    var14 = var12 >> 8 & 255;
                                    var15 = (var12 >> 4 & 7) + var13;
                                    var49 = (var12 & 7) + var14;
                                    if (osrs.bo.U.a(var15, var49)) {
                                       var62 = osrs.bo.f(var15);
                                       var18 = osrs.bo.f(var49);
                                       var71 = new dQ(osrs.bo.U, var31, osrs.bo.U.y, var62, var18, b(osrs.bo.U, var62, var18, osrs.bo.U.y) - var37, var29, x);
                                       osrs.bo.U.c.a((az)var71);
                                    }

                                    var3.f = null;
                                    var7 = true;
                                    return var7;
                                 }

                                 if (osrs.bm.r == var3.f) {
                                    var29 = var6.F();
                                    if (var29 == 65535) {
                                       var29 = -1;
                                    }

                                    osrs.bo.cv.a(var29, osrs.T.eP, osrs.T.eQ, osrs.bo.cR, o);
                                    var3.f = null;
                                    var7 = true;
                                    return var7;
                                 }

                                 if (osrs.bm.at == var3.f) {
                                    ag();
                                    var51 = var6.c();
                                    cD var67 = new cD(var6);
                                    cK var52;
                                    if (var51 >= 0) {
                                       var52 = ap[var51];
                                    } else {
                                       var52 = osrs.bo.bA;
                                    }

                                    if (var52 == null) {
                                       var4.L(var51);
                                       var3.f = null;
                                       var7 = true;
                                       return var7;
                                    }

                                    if (var67.b > var52.q) {
                                       var4.L(var51);
                                       var3.f = null;
                                       var7 = true;
                                       return var7;
                                    }

                                    if (var67.b < var52.q) {
                                       var3.f = null;
                                       var7 = true;
                                       return var7;
                                    }

                                    var67.a(var52);
                                    var3.f = null;
                                    var7 = true;
                                    return var7;
                                 }

                                 if (osrs.bm.ac == var3.f) {
                                    var29 = var6.d();
                                    var31 = var6.H();
                                    osrs.eZ.a(var31, var29);
                                    var3.f = null;
                                    var7 = true;
                                    return var7;
                                 }

                                 if (osrs.bm.be != var3.f) {
                                    if (osrs.bm.C == var3.f) {
                                       a(osrs.eL.b);
                                       var3.f = null;
                                       var7 = true;
                                       return var7;
                                    }

                                    if (osrs.bm.aJ == var3.f) {
                                       var29 = var6.h();
                                       var31 = var6.B();
                                       var37 = var6.H();
                                       var12 = osrs.bo.b(var29);
                                       var13 = osrs.bo.c(var29);
                                       var14 = osrs.bo.d(var29);
                                       var59 = D.a(var13, var14);
                                       var49 = var13 - var59.B;
                                       var62 = var14 - var59.z;
                                       if (var59.b(var12, var49, var62)) {
                                          a(var59, var12, var49, var62, var37, var31);
                                       }

                                       var3.f = null;
                                       var7 = true;
                                       return var7;
                                    }

                                    if (osrs.bm.e == var3.f) {
                                       osrs.bo.a(osrs.bo.U, false, var6);
                                       var3.f = null;
                                       var7 = true;
                                       return var7;
                                    }

                                    if (osrs.bm.aM == var3.f) {
                                       var29 = var6.F();
                                       var31 = var6.Q();
                                       osrs.x.b[var29] = var31;
                                       if (osrs.x.c[var29] != var31) {
                                          osrs.x.c[var29] = var31;
                                          B(var29);
                                       }

                                       osrs.bo.j(var29);
                                       p.a(var29);
                                       var3.f = null;
                                       var7 = true;
                                       return var7;
                                    }

                                    if (osrs.bm.bg == var3.f) {
                                       var29 = ((aR)var6).f();
                                       var31 = var6.F();
                                       if (var31 == 65535) {
                                          var31 = -1;
                                       }

                                       b(var31, var29);
                                       var3.f = null;
                                       var7 = true;
                                       return var7;
                                    }

                                    if (osrs.bm.P == var3.f) {
                                       osrs.bo.a(osrs.bo.aQ, var6, 1);
                                       var3.f = null;
                                       var7 = true;
                                       return var7;
                                    }

                                    if (osrs.bm.R == var3.f) {
                                       var29 = var6.O();
                                       var53 = J();
                                       if (var53 != null) {
                                          var33 = osrs.bo.cR.a(var29);
                                          var33.p = 3;
                                          var33.aM = var53.aO.c();
                                       }

                                       var3.f = null;
                                       var7 = true;
                                       return var7;
                                    }

                                    if (osrs.bm.aK == var3.f && bJ) {
                                       bK = true;
                                       da = false;
                                       cZ = false;

                                       for(var29 = 0; var29 < 5; ++var29) {
                                          df[var29] = false;
                                          z(var29);
                                       }

                                       var3.f = null;
                                       var7 = true;
                                       return var7;
                                    }

                                    if (osrs.bm.aN == var3.f) {
                                       var29 = ((aR)var6).A();
                                       var31 = var6.h();
                                       var43 = J();
                                       if (var43 != null) {
                                          var45 = osrs.bo.cR.a(var31);
                                          var45.al.a(var43.aO.i, var29);
                                       }

                                       var3.f = null;
                                       var7 = true;
                                       return var7;
                                    }

                                    if (osrs.bm.aY == var3.f) {
                                       bJ = true;
                                       bK = false;
                                       da = true;
                                       var29 = j(var6.e() & 2027);
                                       var31 = i(var6.e() & 2027);
                                       var37 = var6.d();
                                       var12 = ((aR)var6).b();
                                       de = new m(osrs.bo.bV, var31, var37, var12);
                                       dd = new m(osrs.bo.cV, var29, var37, var12);
                                       var3.f = null;
                                       var7 = true;
                                       return var7;
                                    }

                                    if (osrs.bm.i != var3.f) {
                                       if (osrs.bm.ax == var3.f) {
                                          ey var39 = new ey();
                                          var39.l = ((aR)var6).m();
                                          var39.h = var6.d();
                                          var31 = var6.h();
                                          var39.i = var31;
                                          if (var39.d()) {
                                             var39.c = "beta";
                                             osrs.bo.q = true;
                                          } else {
                                             osrs.bo.q = false;
                                          }

                                          a((int)45);
                                          var5.c();
                                          var11 = null;
                                          osrs.bo.a(var39);
                                          var3.f = null;
                                          var7 = false;
                                          return var7;
                                       }

                                       if (osrs.bm.Q != var3.f) {
                                          if (osrs.bm.W == var3.f) {
                                             var29 = var6.d();
                                             var31 = ((aR)var6).b();
                                             var37 = var6.d();
                                             osrs.bo.bw.a(var29, var31, var37);
                                             var3.f = null;
                                             var7 = true;
                                             return var7;
                                          }

                                          if (osrs.bm.by != var3.f) {
                                             if (osrs.bm.bm == var3.f) {
                                                var4.a(((aR)var6).b() != 0, (byte)0);
                                                var3.f = null;
                                                var7 = true;
                                                return var7;
                                             }

                                             var10000 = var3.f != null ? var3.f.bv : -1;
                                             gG.a("" + var10000 + osrs.bq.b + (var3.n != null ? var3.n.bv : -1) + osrs.bq.b + (var3.o != null ? var3.o.bv : -1) + osrs.bq.b + var3.g, (Throwable)null, (short)20688);
                                             osrs.bo.o();
                                             break label3183;
                                          }

                                          var32 = ((aR)var6).m();
                                          var46 = (long)var6.d();
                                          var42 = (long)((aR)var6).f();
                                          j var50 = (j)kk.a(osrs.j.a(), ((aR)var6).b());
                                          long var57 = (var46 << 32) + var42;
                                          var54 = false;

                                          for(var18 = 0; var18 < 100; ++var18) {
                                             if (cT[var18] == var57) {
                                                var54 = true;
                                                break;
                                             }
                                          }

                                          if (osrs.bn.l.a(new I(var32, osrs.bo.cp))) {
                                             var54 = true;
                                          }

                                          if (!var54 && N == 0) {
                                             cT[cU] = var57;
                                             cU = (cU + 1) % 100;
                                             var65 = osrs.aX.a(osrs.bo.b(jy.a(var6)));
                                             if (var50.d) {
                                                var19 = 7;
                                             } else {
                                                var19 = 3;
                                             }

                                             if (var50.c != -1) {
                                                iw.a(var19, osrs.bq.a(var50.c) + var32, var65);
                                             } else {
                                                iw.a(var19, var32, var65);
                                             }
                                          }

                                          var3.f = null;
                                          var7 = true;
                                          return var7;
                                       }

                                       var29 = ((aR)var6).A();
                                       var31 = var6.G();
                                       var43 = J();
                                       if (var43 != null) {
                                          if (var31 == 65535) {
                                             var31 = -1;
                                          }

                                          a(var43, var31, var29);
                                       }

                                       var3.f = null;
                                       var7 = true;
                                       return var7;
                                    }

                                    var29 = var6.F();
                                    if (var29 == 65535) {
                                       var29 = -1;
                                    }

                                    var31 = var6.F();
                                    if (var31 == 65535) {
                                       var31 = -1;
                                    }

                                    var37 = var6.Q();
                                    var12 = var6.h();
                                    var13 = var6.P();
                                    eO var47 = (eO)osrs.bo.cR.p.a((long)var12);
                                    if (var47 != null) {
                                       var47.X();
                                    }

                                    eO var56 = osrs.eO.a(var47, var29, var31, var37, var13);
                                    osrs.bo.cR.p.a(var56, (long)var12);
                                    var3.f = null;
                                    var7 = true;
                                    return var7;
                                 }

                                 var29 = var6.h();
                                 var31 = var6.d();
                                 if (var29 < -70000) {
                                    var31 += 32768;
                                 }

                                 if (var29 >= 0) {
                                    var33 = osrs.bo.cR.a(var29);
                                 } else {
                                    var33 = null;
                                 }

                                 var4.ct.a();

                                 for(; var6.d < var3.g; im.a(var31, var12, var13, var14)) {
                                    var12 = var6.q();
                                    var13 = var6.d() - 1;
                                    var14 = 0;
                                    if (var13 != -1) {
                                       var14 = ((aR)var6).b();
                                       if (var14 == 255) {
                                          var14 = var6.h();
                                       }
                                    }

                                    var15 = im.a(var31, var12);
                                    var49 = var15 == -1 ? 0 : im.b(var31, var12);
                                    var4.ct.a(var12, var15, var49, var13, var14);
                                    if (var33 != null && var12 >= 0 && var12 < var33.bd.length) {
                                       var33.bd[var12] = var13 + 1;
                                       var33.bC[var12] = var14;
                                    }
                                 }

                                 p.c(var31);
                                 var3.f = null;
                                 var4.ct.b();
                                 var4.ct.b(var31);
                                 var7 = true;
                                 return var7;
                              }

                              var32 = ((aR)var6).m();
                              Object[] var35 = new Object[var32.length() + 1];

                              for(var37 = var32.length() - 1; var37 >= 0; --var37) {
                                 switch (var32.charAt(var37)) {
                                    case 'W':
                                       var12 = var6.w();
                                       be var40 = new be(osrs.i.a, 0, var12, var12);
                                       int[] var41 = var40.a();

                                       for(var15 = 0; var15 < var12; ++var15) {
                                          var41[var15] = var6.x();
                                       }

                                       var35[var37 + 1] = var40;
                                       break;
                                    case 'X':
                                       var15 = var6.w();
                                       be var16 = new be(osrs.i.c, (Object)null, var15, var15);
                                       Object[] var17 = var16.c();

                                       for(var18 = 0; var18 < var15; ++var18) {
                                          var17[var18] = ((aR)var6).m();
                                       }

                                       var35[var37 + 1] = var16;
                                       break;
                                    case 's':
                                       var35[var37 + 1] = ((aR)var6).m();
                                       break;
                                    case 'Ï':
                                       var35[var37 + 1] = var6.i();
                                       break;
                                    default:
                                       var35[var37 + 1] = new Integer(var6.h());
                                 }
                              }

                              var35[0] = new Integer(var6.h());
                              if (CustomPacketHandler.handleCustomClientScript(var35)) {
                                 var3.f = null;
                                 var7 = true;
                              } else {
                                 ac var38 = osrs.ac.a(76).a(var35).a();
                                 osrs.bh.a(var38);
                                 var3.f = null;
                                 var7 = true;
                              }

                              return var7;
                           }

                           if (osrs.bo.bs == null) {
                              osrs.bo.bs = new hc(ah);
                           }

                           ie var30 = ah.a(var6);
                           osrs.bo.bs.a.a(var30.a, var30.b);
                           p.d(var30.a);
                           var3.f = null;
                           var7 = true;
                           return var7;
                        }

                        osrs.bo.ci = var6.B();
                        osrs.bo.O = var6.z();
                        cc = ((aR)var6).A();

                        while(var6.d < var3.g) {
                           var29 = ((aR)var6).b();
                           eL var34 = osrs.eL.a()[var29];
                           a(var34);
                        }

                        var3.f = null;
                        var7 = true;
                        return var7;
                     }

                     var29 = var6.h();
                     var31 = var6.d();
                     if (var29 < -70000) {
                        var31 += 32768;
                     }

                     if (var29 >= 0) {
                        var33 = osrs.bo.cR.a(var29);
                     } else {
                        var33 = null;
                     }

                     if (var33 != null) {
                        for(var12 = 0; var12 < var33.bd.length; ++var12) {
                           var33.bd[var12] = 0;
                           var33.bC[var12] = 0;
                        }
                     }

                     im.a(var31);
                     var12 = var6.d();

                     for(var13 = 0; var13 < var12; ++var13) {
                        var14 = var6.H();
                        var15 = var6.B();
                        if (var15 == 255) {
                           var15 = var6.h();
                        }

                        if (var33 != null && var13 < var33.bd.length) {
                           var33.bd[var13] = var14;
                           var33.bC[var13] = var15;
                        }

                        im.a(var31, var13, var14 - 1, var15);
                     }

                     p.d(var31);
                     var3.f = null;
                     var7 = true;
                     return var7;
                  }
               } catch (IOException var27) {
                  r();
               } catch (Exception var28) {
                  jR var10 = K();
                  var10000 = var1.f != null ? var1.f.bv : -1;
                  var11 = "" + var10000 + osrs.bq.b + (var1.n != null ? var1.n.bv : -1) + osrs.bq.b + (var1.o != null ? var1.o.bv : -1) + osrs.bq.b + var1.g + osrs.bq.b + var10.e() + osrs.bq.b + var10.f() + osrs.bq.b;

                  for(var12 = 0; var12 < var3.g && var12 < 50; ++var12) {
                     var11 = var11 + var6.c[var12] + osrs.bq.b;
                  }

                  gG.a(var11, var28, (short)21427);
                  osrs.bo.o();
               }

               var36 = true;
               return var36;
            }

            var7 = var7;
         }

         return var7;
      }
   }

   public Client() {
      bh();
      this.cq = false;
      this.cr = 0;
      this.cs = -1L;
      this.U(-1);
      this.ct = new hK();
      osrs.bo.cv = new jH(new jA(), new kg(), new ky());
      osrs.bo.bt = new jk(new jA(), new kg(), new jL(), new jP(), new kx());
   }

   public static ae a() {
      return dT;
   }

   public static bI b() {
      return dU;
   }

   public static q c() {
      return am;
   }

   public void d() {
      if (!a && osrs.bo.fu != null) {
         throw new AssertionError();
      } else {
         jc var1;
         if (osrs.bo.eX != null && osrs.bo.eX.f()) {
            var1 = osrs.bo.fu = osrs.bo.eX;
            osrs.bo.eX = null;
            osrs.bo.fZ = false;
            osrs.bo.aQ.y = var1.I;
            if (!a && w != 30) {
               throw new AssertionError();
            }

            b(var1);
            if (!a && w != 25) {
               throw new AssertionError();
            }
         }

         if (osrs.bo.fH != null && osrs.bo.fH.f()) {
            var1 = osrs.bo.fH;
            osrs.bo.fH = null;
            if (!a && !osrs.bo.fZ) {
               throw new AssertionError();
            }

            osrs.bo.fZ = false;
            c(var1);
            if (!a && !bP) {
               throw new AssertionError();
            }

            bP = false;
            osrs.bo.ee = null;
         }

      }
   }

   public static void e() {
      osrs.bo.bO = null;
      osrs.bo.bR = null;
      osrs.bo.s = null;
      osrs.bo.em = null;
      osrs.bo.cN = null;
      osrs.bo.bq = null;
      osrs.bo.T = null;
      osrs.bo.bn = null;
      osrs.bo.dK = null;
      osrs.bo.dp = null;
      osrs.bo.cc = null;
   }

   public static void a(int var0) {
      if (w != var0) {
         if (var0 == 25 || var0 == 30) {
            cD();
         }

         if (w == 30) {
            an.b();
         }

         if (w == 0) {
            s.cl();
         }

         if (var0 == 20 || var0 == 40 || var0 == 45 || var0 == 50) {
            a(osrs.aD.k);
            bh = 0;
            bi = 0;
            ca.a(var0);
            if (var0 != 20) {
               osrs.bo.b(false);
            }
         }

         if (var0 != 20 && var0 != 40 && osrs.bo.cU != null) {
            osrs.bo.cU.c();
            osrs.bo.cU = null;
         }

         if (w == 25) {
            bp = 0;
            bl = 0;
            bm = 1;
            bn = 0;
            bo = 1;
         }

         if (var0 != 5 && var0 != 10) {
            if (var0 == 20) {
               int var3 = w == 11 ? 4 : 0;
               osrs.bA.a(osrs.bo.cF, osrs.bo.dM, iJ.a, false, var3);
            } else if (var0 == 11) {
               osrs.bA.a(osrs.bo.cF, osrs.bo.dM, iJ.a, false, 4);
            } else if (var0 == 50) {
               osrs.bA.a("", "Updating date of birth...", "");
               osrs.bA.a(osrs.bo.cF, osrs.bo.dM, iJ.a, false, 7);
            } else if (var0 != 0 && osrs.bo.dw) {
               osrs.bA.o = null;
               osrs.bA.p = null;
               osrs.bo.aO = null;
               osrs.bo.cI = null;
               osrs.bo.dX = null;
               osrs.bo.dk = null;
               osrs.bA.s = null;
               osrs.bA.t = null;
               osrs.bA.u = null;
               osrs.bo.cm = null;
               osrs.bo.dd = null;
               osrs.bo.bK = null;
               osrs.bo.r = null;
               osrs.bA.J = null;
               osrs.bo.er.b();
               osrs.eZ.a(0, 100);
               ap().a(true);
               osrs.bo.dw = false;
               ad(-1);
            }
         } else {
            boolean var1 = bL.n() >= aO;
            int var2 = var1 ? 0 : 12;
            osrs.bA.a(osrs.bo.cF, osrs.bo.dM, iJ.a, true, var2);
         }

         w = var0;
         T(-1);
      }

   }

   public static boolean a(int var0, int var1, int var2, int var3, int var4, int var5) {
      int var6 = osrs.bo.ct - var0;
      int var7 = osrs.bo.Z - var1;
      int var8 = osrs.bo.j - var2;
      if (Math.abs(var6) > osrs.bo.aV + var3) {
         return false;
      } else if (Math.abs(var7) > osrs.bo.aM + var4) {
         return false;
      } else if (Math.abs(var8) > osrs.bo.bf + var5) {
         return false;
      } else if (Math.abs(osrs.bo.dz * var8 - osrs.bo.dh * var7) > osrs.bo.bf * var4 + osrs.bo.aM * var5) {
         return false;
      } else if (Math.abs(osrs.bo.dh * var6 - osrs.bo.cA * var8) > osrs.bo.bf * var3 + osrs.bo.aV * var5) {
         return false;
      } else {
         return Math.abs(osrs.bo.cA * var7 - osrs.bo.dz * var6) <= osrs.bo.aV * var4 + osrs.bo.aM * var3;
      }
   }

   public static void a(au var0, au var1, V var2, boolean var3, int var4) {
      if (osrs.bo.eT) {
         if (osrs.bo.fM != null) {
            if (osrs.bo.fM.getWidth() > 765) {
               osrs.bz.o = true;
               osrs.bo.fa = osrs.bo.fM;
               osrs.bo.fU = null;
               int var5 = (osrs.T.eP - osrs.bo.fM.getWidth()) / 2;
               osrs.bo.cI = new aV(new int[]{0}, 1, 1);
               osrs.bo.dX = osrs.bo.cI;
               osrs.bo.fa.c(var5, 0);
            } else if (osrs.bo.fM.getWidth() > 383) {
               osrs.bz.o = false;
               osrs.bo.cI = osrs.bo.fM;
               osrs.bo.dX = new aV(new int[]{0}, 1, 1);
               osrs.bo.cI.c(osrs.bA.a, 0);
            } else {
               osrs.bz.o = false;
               osrs.bo.cI = osrs.bo.fM;
               osrs.bo.dX = osrs.bo.cI.n();
               osrs.bo.cI.c(osrs.bA.a, 0);
               osrs.bo.dX.c(osrs.bA.a + 382, 0);
            }
         } else {
            byte[] var8 = var0.d("titlewide.png", "");
            aV var6 = c(var8);
            osrs.bz.o = true;
            osrs.bo.fa = var6;
            osrs.bo.fU = var6.n();
            int var7 = (osrs.T.eP - (var6.getWidth() * 2 - 1)) / 2;
            osrs.bo.cI = new aV(new int[]{0}, 1, 1);
            osrs.bo.dX = osrs.bo.cI;
            osrs.bo.fa.c(var7, 0);
            osrs.bo.fU.c(var7 + var6.getWidth() - 1, 0);
         }
      }

   }

   public void setAllWidgetsAreOpTargetable(boolean var1) {
      osrs.bo.e = var1;
   }

   public static void a(av var0, String var1) {
      iq var2 = new iq(var0, var1);
      dm.add(var2);
      do += var2.c;
   }

   public hW f() {
      bG var1 = D.j(T);
      if (var1 == null) {
         var1 = osrs.bo.aQ;
      }

      Object var2 = null;
      switch (osrs.bo.E.d) {
         case 0:
            var2 = (hW)var1.r.b((long)U);
            break;
         case 1:
            var2 = (hW)var1.s.b((long)U);
            break;
         case 2:
            var2 = (hW)var1.t.b((long)U);
      }

      if (var2 == null) {
         var2 = bA();
      }

      return (hW)var2;
   }

   public static int g() {
      if (dm != null && dn < dm.size()) {
         int var0 = 0;

         for(int var1 = 0; var1 <= dn; ++var1) {
            var0 += ((iq)dm.get(var1)).a;
         }

         return var0 * 10000 / do;
      } else {
         return 10000;
      }
   }

   public static int b(int var0) {
      return var0 * 3 + 600;
   }

   public static void h() {
      if (bc == osrs.aB.e) {
         osrs.bo.bg = a(fA.r.l, false, true, true, false);
         osrs.bo.cL = a(fA.t.l, true, true, true, false);
         osrs.bo.dM = a(fA.v.l, false, true, true, false);
         osrs.bo.cF = a(fA.x.l, false, true, true, false);
         osrs.bo.bl = a(fA.A.l, true, false, true, false);
         osrs.bo.dc = a(fA.a.l, false, true, true, false);
         osrs.bo.bv = a(fA.b.l, false, true, true, false);
         osrs.bo.ei = a(fA.c.l, true, true, true, false);
         osrs.bA.a(20, osrs.bv.r);
         a(osrs.aB.g);
      } else {
         int var0;
         int var1;
         int var2;
         int var3;
         int var4;
         int var5;
         int var6;
         int var7;
         int var8;
         if (bc == osrs.aB.g) {
            var0 = 0;
            var1 = var0 + osrs.bo.bg.d() * 53 / 100;
            var2 = var1 + osrs.bo.cL.d() * 5 / 100;
            var3 = var2 + osrs.bo.dM.d() * 36 / 100;
            var4 = var3 + osrs.bo.cF.d() / 100;
            var5 = var4 + osrs.bo.bl.d() / 100;
            var6 = var5 + osrs.bo.dc.d() * 2 / 100;
            var7 = var6 + osrs.bo.bv.d() / 100;
            var8 = var7 + (osrs.bo.ei.c() && osrs.bo.ei.a() ? 1 : 0);
            if (var8 != 100) {
               if (var8 != 0) {
                  osrs.bA.a(30, osrs.bv.s + var8 + "%");
               }
            } else {
               a(osrs.bo.bg, "Sound FX");
               a(osrs.bo.cL, "Music Tracks");
               a(osrs.bo.dM, "Sprites");
               a(osrs.bo.dc, "Music Samples");
               a(osrs.bo.bv, "Music Patches");
               iJ.a = new V();
               iJ.a.a(osrs.bo.ei);
               osrs.bA.a(40, osrs.bv.t);
               a(osrs.aB.i);
            }
         } else if (bc == osrs.aB.i) {
            boolean var20 = !aM;
            osrs.bo.fG = 22050;
            osrs.bo.dv = var20;
            osrs.bo.aU = 2;
            ArrayList var21 = new ArrayList(3);
            dX var25 = osrs.bo.aT;
            short var27 = 2048;
            if (osrs.bo.fG == 0) {
               throw new IllegalStateException();
            }

            if (var27 < 512) {
               var27 = 512;
            }

            aQ var30;
            try {
               aQ var33 = osrs.bo.eB.a();
               var33.b = new int[512 * (osrs.bo.dv ? 2 : 1)];
               var33.f = var27;
               var33.e();
               var33.e = (var27 & -2048) + 2048;
               if (var33.e > 32768) {
                  var33.e = 32768;
               }

               var33.b(var33.e);
               if (osrs.bo.aU > 0 && osrs.bo.dC == null) {
                  osrs.bo.dC = new iF();
                  osrs.bo.fI = Executors.newScheduledThreadPool(1);
                  osrs.bo.fI.scheduleAtFixedRate(osrs.bo.dC, 0L, 10L, TimeUnit.MILLISECONDS);
               }

               if (osrs.bo.dC != null) {
                  if (osrs.bo.dC.a[0] != null) {
                     throw new IllegalArgumentException();
                  }

                  osrs.bo.dC.a[0] = var33;
               }

               var30 = var33;
            } catch (Throwable var19) {
               a(var19);
               var30 = new aQ();
            }

            osrs.bo.bd = var30;
            osrs.bo.ek = new iG();
            iG var34 = new iG();
            var34.a((cl)osrs.bo.ek);

            for(var6 = 0; var6 < 3; ++var6) {
               aG var37 = new aG(osrs.bo.bd);
               var37.a(9, 128);
               var34.a((cl)var37);
               var21.add(var37);
            }

            osrs.bo.bd.b(var34);
            osrs.eZ.a(osrs.bo.bv, osrs.bo.dc, osrs.bo.bg, var21);
            osrs.bA.a(60, osrs.bv.u);
            a(osrs.aB.j);
         } else {
            String var10001;
            if (bc == osrs.aB.j) {
               if (osrs.bo.da == null) {
                  osrs.bo.da = new hH(osrs.bo.dM, osrs.bo.bl);
               }

               var0 = hv.a().length;
               C = osrs.bo.da.a(hv.a());
               if (C.size() < var0) {
                  var10001 = osrs.bv.v;
                  osrs.bA.a(80, var10001 + C.size() * 100 / var0 + "%");
               } else {
                  osrs.bo.ad = (gg)C.get(hv.a);
                  osrs.bo.L = (gg)C.get(hv.b);
                  osrs.bo.ck = (gg)C.get(hv.c);
                  dT = dk.a();
                  osrs.ae.b(-1);
                  osrs.bA.a(80, osrs.bv.w);
                  a(osrs.aB.k);
               }
            } else if (bc == osrs.aB.k) {
               var0 = osrs.bA.a((au)osrs.bo.cF, (au)osrs.bo.dM);
               var1 = osrs.bA.a((au)osrs.bo.dM);
               if (var0 < var1) {
                  osrs.bA.a(90, osrs.bv.x + var0 * 100 / var1 + "%");
               } else {
                  osrs.bA.a(100, osrs.bv.y);
                  if (osrs.bo.q) {
                     a((int)5);
                     a(osrs.aB.b);
                  } else {
                     a(osrs.aB.f);
                  }
               }
            } else if (bc == osrs.aB.b) {
               a((int)10);
            } else {
               av var26;
               if (bc == osrs.aB.f) {
                  osrs.bo.P = a(fA.n.l, false, true, true, false);
                  osrs.bo.ej = a(fA.h.l, false, true, true, true);
                  osrs.bo.Q = a(fA.o.l, false, true, true, false);
                  osrs.bo.ff = a(fA.p.l, true, false, true, false);
                  osrs.bo.bF = a(fA.q.l, false, true, true, false);
                  osrs.bo.cn = a(fA.s.l, true, true, true, false);
                  osrs.bo.cw = a(fA.u.l, false, true, true, false);
                  osrs.bo.di = a(fA.w.l, false, true, true, false);
                  osrs.bo.bj = a(fA.y.l, false, true, true, false);
                  osrs.bo.bz = a(fA.z.l, false, true, true, false);
                  osrs.bo.de = a(fA.d.l, false, true, true, false);
                  osrs.bo.cq = a(fA.e.l, false, true, true, false);
                  osrs.bo.eD = a(fA.f.l, false, true, true, false);
                  osrs.bo.B = a(fA.g.l, false, true, true, false);
                  var0 = fA.i.l;
                  gR var23 = null;
                  if (osrs.U.c != null) {
                     var23 = new gR(var0, osrs.U.c, osrs.bo.ca[var0], 1000000);
                  }

                  var26 = new av(var23, osrs.bo.cl, osrs.bo.eg, var0, false, true, true, true, true);
                  osrs.bo.aJ = var26;
                  osrs.bA.a(20, osrs.bv.r);
                  a(osrs.aB.h);
               } else {
                  int var12;
                  if (bc == osrs.aB.h) {
                     byte var22 = 0;
                     var1 = var22 + osrs.bo.P.d() * 4 / 100;
                     var2 = var1 + osrs.bo.ej.d() / 100;
                     var3 = var2 + osrs.bo.Q.d() * 2 / 100;
                     var4 = var3 + osrs.bo.ff.d() / 100;
                     var5 = var4 + osrs.bo.bF.d() / 100;
                     var6 = var5 + osrs.bo.cn.d() * 10 / 100;
                     var7 = var6 + osrs.bo.cw.d() * 65 / 100;
                     var8 = var7 + osrs.bo.di.d() / 100;
                     int var9 = var8 + osrs.bo.bj.d() / 100;
                     int var10 = var9 + osrs.bo.bz.d() * 6 / 100;
                     int var11 = var10 + osrs.bo.cq.d() / 100;
                     var12 = var11 + osrs.bo.de.d() * 2 / 100;
                     int var13 = var12 + osrs.bo.eD.d() * 2 / 100;
                     int var14 = var13 + osrs.bo.B.d() / 100;
                     int var15 = var14 + osrs.bo.aJ.d() * 2 / 100;
                     if (var15 != 100) {
                        if (var15 != 0) {
                           osrs.bA.a(30, osrs.bv.s + var15 + "%");
                        }
                     } else {
                        a(osrs.bo.P, "Animations");
                        a(osrs.bo.ej, "Animation Keyframes");
                        a(osrs.bo.Q, "Skeletons");
                        a(osrs.bo.cn, "Maps");
                        a(osrs.bo.cw, "Models");
                        a(osrs.bo.bj, "Music Jingles");
                        a(osrs.bo.cq, "World Map");
                        a(osrs.bo.de, "World Map Geography");
                        a(osrs.bo.eD, "World Map Ground");
                        osrs.bA.a(30, osrs.bv.t);
                        a(osrs.aB.d);
                     }
                  } else if (bc == osrs.aB.d) {
                     osrs.bo.aQ = D.a(104, 104, bL.p());
                     osrs.bo.U = osrs.bo.aQ;
                     osrs.bo.K = new aV(512, 512);
                     osrs.bA.a(30, osrs.bv.q);
                     a(osrs.aB.l);
                  } else if (bc == osrs.aB.l) {
                     if (!osrs.bo.ff.a()) {
                        var10001 = osrs.bv.z;
                        osrs.bA.a(40, var10001 + osrs.bo.ff.g() + "%");
                     } else if (!osrs.bo.B.a()) {
                        var10001 = osrs.bv.z;
                        osrs.bA.a(40, var10001 + (80 + osrs.bo.bz.g() / 6) + "%");
                     } else {
                        osrs.N.a(osrs.bo.ff);
                        av var24 = osrs.bo.ff;
                        osrs.O.g = var24;
                        av var28 = osrs.bo.ff;
                        var26 = osrs.bo.cw;
                        osrs.af.b = var28;
                        osrs.af.c = var26;
                        osrs.af.a = osrs.af.b.b(3, (byte)4);
                        osrs.aC.a(osrs.bo.ff, osrs.bo.cw, aM);
                        osrs.aN.a((au)osrs.bo.ff, (au)osrs.bo.cw);
                        av var32 = osrs.bo.ff;
                        osrs.bo.be = var32;
                        av var35 = osrs.bo.ff;
                        av var36 = osrs.bo.cw;
                        boolean var38 = v;
                        gg var39 = osrs.bo.ad;
                        osrs.aO.b = var35;
                        osrs.aO.c = var36;
                        osrs.aO.d = var38;
                        osrs.aO.a = osrs.aO.b.b(10, (byte)35);
                        osrs.aO.h = var39;
                        av var40 = osrs.bo.ff;
                        av var41 = osrs.bo.P;
                        av var42 = osrs.bo.ej;
                        av var43 = osrs.bo.Q;
                        osrs.bk.a = var40;
                        osrs.bk.b = var41;
                        osrs.bk.c = var42;
                        osrs.bk.d = var43;
                        osrs.bn.a(osrs.bo.ff, osrs.bo.cw);
                        osrs.bB.a((au)osrs.bo.ff);
                        osrs.bD.a((au)osrs.bo.ff);
                        var12 = osrs.bD.c;
                        osrs.x.b = new int[var12];
                        osrs.x.c = new int[var12];
                        B(-1);
                        osrs.bo.cR = new kd(osrs.bo.bF, osrs.bo.cw, osrs.bo.dM, osrs.bo.bl, osrs.bo.aJ);
                        av var46 = osrs.bo.ff;
                        osrs.at.c = var46;
                        osrs.M.a((au)osrs.bo.ff);
                        osrs.bC.a((au)osrs.bo.ff);
                        osrs.cN.a(osrs.bo.ff);
                        osrs.aT.a((au)osrs.bo.ff);
                        osrs.bH.a(osrs.bo.ff, osrs.bo.dM);
                        osrs.E.a((au)osrs.bo.ff);
                        av var47 = osrs.bo.ff;
                        osrs.C.c = var47;
                        ag = new aP(osrs.bo.cZ, 54, osrs.bo.el, osrs.bo.ff);
                        ah = new aP(osrs.bo.cZ, 47, osrs.bo.el, osrs.bo.ff);
                        af = new y();
                        osrs.ab.a(osrs.bo.ff, osrs.bo.dM, osrs.bo.bl);
                        osrs.Z.a(osrs.bo.ff, osrs.bo.dM);
                        av var48 = osrs.bo.ff;
                        av var16 = osrs.bo.dM;
                        osrs.aE.u = var16;
                        osrs.aE.s = ((au)var48).b(35, (byte)-64);
                        osrs.aE.x = new aE[osrs.aE.s];

                        for(int var17 = 0; var17 < osrs.aE.s; ++var17) {
                           byte[] var18 = ((au)var48).b(35, (int)var17);
                           osrs.aE.x[var17] = new aE(var17);
                           if (var18 != null) {
                              osrs.aE.x[var17].a(new aR(var18));
                              osrs.aE.x[var17].a();
                           }
                        }

                        osrs.bA.a(50, osrs.bv.A);
                        a(osrs.aB.m);
                     }
                  } else if (bc == osrs.aB.m) {
                     var0 = 0;
                     if (osrs.bo.bO == null) {
                        osrs.bo.bO = osrs.bo.a(osrs.bo.dM, iJ.a.o, 0);
                     } else {
                        ++var0;
                     }

                     if (osrs.bo.cN == null) {
                        osrs.bo.cN = osrs.bo.a(osrs.bo.dM, iJ.a.p, 0);
                     } else {
                        ++var0;
                     }

                     if (osrs.bo.bq == null) {
                        osrs.bo.bq = osrs.aY.a(osrs.bo.dM, iJ.a.q, 0);
                     } else {
                        ++var0;
                     }

                     if (!jZ.b()) {
                        jZ.a(osrs.aY.b(osrs.bo.dM, iJ.a.r, 0));
                     } else {
                        ++var0;
                     }

                     aV[] var29;
                     if (!jZ.d()) {
                        var29 = osrs.aY.b(osrs.bo.dM, iJ.a.s, 0);
                        osrs.bo.dp = var29;
                     } else {
                        ++var0;
                     }

                     if (!jZ.c()) {
                        var29 = osrs.aY.b(osrs.bo.dM, iJ.a.t, 0);
                        osrs.bo.dK = var29;
                     } else {
                        ++var0;
                     }

                     if (osrs.bo.T == null) {
                        osrs.bo.T = osrs.aY.b(osrs.bo.dM, iJ.a.u, 0);
                     } else {
                        ++var0;
                     }

                     if (osrs.bo.bR == null) {
                        osrs.bo.bR = osrs.aY.b(osrs.bo.dM, iJ.a.a, 0);
                     } else {
                        ++var0;
                     }

                     if (osrs.bo.bn == null) {
                        osrs.bo.bn = osrs.aY.b(osrs.bo.dM, iJ.a.b, 0);
                     } else {
                        ++var0;
                     }

                     if (osrs.bo.s == null) {
                        osrs.bo.s = osrs.aY.a(osrs.bo.dM, iJ.a.c, 0);
                     } else {
                        ++var0;
                     }

                     if (osrs.bo.em == null) {
                        osrs.bo.em = osrs.aY.a(osrs.bo.dM, iJ.a.d, 0);
                     } else {
                        ++var0;
                     }

                     ModelUnlit var31;
                     if (osrs.bo.bN == null && iJ.a.e != -1) {
                        var31 = ModelUnlit.a(osrs.bo.cw, iJ.a.e, 0);
                        if (var31 != null) {
                           osrs.bo.bN = var31.h();
                        }
                     } else {
                        ++var0;
                     }

                     if (osrs.bo.cC == null && iJ.a.f != -1) {
                        var31 = ModelUnlit.a(osrs.bo.cw, iJ.a.f, 0);
                        if (var31 != null) {
                           osrs.bo.cC = var31.h();
                        }
                     } else {
                        ++var0;
                     }

                     if (var0 < 13) {
                        osrs.bA.a(70, osrs.bv.B + var0 * 100 / 14 + "%");
                     } else {
                        osrs.aX.E = osrs.bo.em;
                        osrs.bo.cN.i();
                        var1 = (int)(Math.random() * 21.0) - 10;
                        var2 = (int)(Math.random() * 21.0) - 10;
                        var3 = (int)(Math.random() * 21.0) - 10;
                        var4 = (int)(Math.random() * 41.0) - 20;
                        osrs.bo.bq[0].c(var1 + var4, var2 + var4, var3 + var4);
                        osrs.bA.a(60, osrs.bv.C);
                        a(osrs.aB.n);
                     }
                  } else if (bc == osrs.aB.n) {
                     if (!osrs.bo.di.a()) {
                        osrs.bA.a(70, osrs.bv.D + "0%");
                     } else {
                        osrs.bo.bb = new dO(osrs.bo.di, osrs.bo.dM, 20, bL.i(), 128);
                        osrs.aW.a(osrs.bo.bb);
                        osrs.aW.a(bL.i());
                        a(osrs.aB.o);
                     }
                  } else if (bc == osrs.aB.o) {
                     var0 = osrs.bo.bb.a();
                     if (var0 < 100) {
                        osrs.bA.a(80, osrs.bv.D + var0 + "%");
                     } else {
                        osrs.bA.a(90, osrs.bv.E);
                        a(osrs.aB.p);
                     }
                  } else if (bc == osrs.aB.p) {
                     osrs.bo.bi = new iu();
                     osrs.bo.aT.a((Runnable)osrs.bo.bi, 10);
                     osrs.bA.a(92, osrs.bv.F);
                     a(osrs.aB.q);
                  } else if (bc == osrs.aB.q) {
                     if (!osrs.bo.cF.c("huffman", "")) {
                        osrs.bA.a(94, osrs.bv.G + "0%");
                     } else {
                        bF var44 = new bF(osrs.bo.cF.b("huffman", ""));
                        osrs.bo.bL = var44;
                        osrs.bA.a(94, osrs.bv.H);
                        a(osrs.aB.r);
                     }
                  } else if (bc == osrs.aB.r) {
                     if (!osrs.bo.bF.a()) {
                        var10001 = osrs.bv.I;
                        osrs.bA.a(96, var10001 + osrs.bo.bF.g() * 4 / 5 + "%");
                     } else if (!osrs.bo.aJ.a()) {
                        var10001 = osrs.bv.I;
                        osrs.bA.a(96, var10001 + osrs.bo.aJ.g() * 4 / 5 + "%");
                     } else if (!osrs.bo.bz.a()) {
                        var10001 = osrs.bv.I;
                        osrs.bA.a(96, var10001 + (80 + osrs.bo.bz.g() / 6) + "%");
                     } else if (!osrs.bo.bl.a()) {
                        var10001 = osrs.bv.I;
                        osrs.bA.a(96, var10001 + (96 + osrs.bo.bl.g() / 50) + "%");
                     } else {
                        osrs.bA.a(98, osrs.bv.J);
                        if (osrs.bo.bz.a("version.dat", "")) {
                           aR var45 = new aR(osrs.bo.bz.b("version.dat", ""));
                           var45.d();
                        }

                        a(osrs.aB.a);
                     }
                  } else if (bc == osrs.aB.a) {
                     if (osrs.bo.cq.b() > 0 && !osrs.bo.cq.b(osrs.bL.a.f)) {
                        var10001 = osrs.bv.K;
                        osrs.bA.a(100, var10001 + osrs.bo.cq.d(osrs.bL.a.f) / 10 + "%");
                     } else {
                        if (dU == null) {
                           dU = new bI();
                           dU.a(osrs.bo.cq, osrs.bo.de, osrs.bo.eD, osrs.bo.ck, C, osrs.bo.bq);
                        }

                        osrs.bA.a(100, osrs.bv.L);
                        if (osrs.bo.q) {
                           a(osrs.aB.c);
                        } else {
                           a(osrs.aB.b);
                        }
                     }
                  } else if (bc == osrs.aB.c) {
                     osrs.bo.cu = false;
                     a((int)20);
                     a(osrs.aD.j);
                  }
               }
            }
         }
      }

   }

   public s i() {
      return this.getHintArrowType() == 1 ? (s)osrs.bo.aQ.s.b((long)bR.h) : null;
   }

   public static void j() {
      double var0 = dw;
      double var2 = ao(cl / 256);
      if (var2 > var0) {
         var0 = var2;
      }

      double var4;
      if (dx[4]) {
         var4 = ao(dh[4] + 128);
         if (var4 > var0) {
            var0 = var4;
         }
      }

      var4 = osrs.bo.eR;
      double var6 = osrs.bo.fq;
      double var8 = osrs.bo.eO;
      double var10 = osrs.bo.eS;
      int var12 = b(var0) * 3 + 600;
      int var13 = aC - 334;
      if (var13 < 0) {
         var13 = 0;
      } else if (var13 > 100) {
         var13 = 100;
      }

      int var14 = (au - at) * var13 / 100 + at;
      int var15 = var12 * var14 / 256;
      double var16 = 6.283185307179586 - var0;
      double var18 = 6.283185307179586 - var4;
      double var20 = 0.0;
      double var22 = 0.0;
      double var24 = (double)var15;
      double var26 = Math.sin(var16);
      double var28 = Math.cos(var16);
      double var30 = var22 * var28 - var24 * var26;
      double var32 = var22 * var26 + var24 * var28;
      double var34 = Math.sin(var18);
      double var36 = Math.cos(var18);
      double var38 = var20 * var36 + var32 * var34;
      double var40 = var32 * var36 - var20 * var34;
      if (!bJ) {
         osrs.bo.fx = var6 - var38;
         osrs.bo.fX = var10 - var30;
         osrs.bo.h = var8 - var40;
         osrs.bo.fm = var0;
         osrs.bo.eL = var4;
      } else {
         osrs.bo.fx = (double)osrs.bo.bM;
         osrs.bo.h = (double)osrs.bo.dO;
         osrs.bo.fX = (double)osrs.bo.bD;
         osrs.bo.fm = ao(osrs.bo.bV);
         osrs.bo.eL = ao(osrs.bo.cV);
      }

      for(int var42 = 0; var42 < 5; ++var42) {
         if (dx[var42] && !dy) {
            int var43 = (int)(Math.random() * (double)(dg[var42] * 2 + 1) - (double)dg[var42] + Math.sin((double)di[var42] / 100.0 * (double)dj[var42]) * (double)dh[var42]);
            if (var42 == 0) {
               osrs.bo.bM += var43;
               osrs.bo.fx += (double)var43;
            }

            if (var42 == 1) {
               osrs.bo.bD += var43;
               osrs.bo.fX += (double)var43;
            }

            if (var42 == 2) {
               osrs.bo.dO += var43;
               osrs.bo.h += (double)var43;
            }

            if (var42 == 3) {
               osrs.bo.cV = osrs.bo.cV + var43 & 2047;
               osrs.bo.eL = ao(osrs.bo.cV);
            }

            if (var42 == 4) {
               osrs.bo.bV += var43;
               osrs.bo.bV = Ints.constrainToRange(osrs.bo.bV, dt ? 0 : 128, dt ? 512 : 383);
               osrs.bo.fm = ao(osrs.bo.bV);
            }
         }
      }

   }

   public Dimension getRealDimensions() {
      if (!this.isStretchedEnabled()) {
         return this.fc;
      } else {
         if (osrs.bo.fv == null) {
            if (this.isResized()) {
               Container var1 = this.getCanvas().getParent();
               int var2 = var1.getWidth();
               int var3 = var1.getHeight();
               int var4 = (int)((double)var2 / osrs.bo.eN);
               int var5 = (int)((double)var3 / osrs.bo.eN);
               if (var4 < 765 || var5 < 503) {
                  double var6 = (double)var2 / 765.0;
                  double var8 = (double)var3 / 503.0;
                  double var10 = Math.min(var6, var8);
                  var4 = (int)((double)var2 / var10);
                  var5 = (int)((double)var3 / var10);
               }

               osrs.bo.fv = new Dimension(var4, var5);
            } else {
               osrs.bo.fv = Constants.GAME_FIXED_SIZE;
            }
         }

         return osrs.bo.fv;
      }
   }

   public final void c(int var1) {
      if (osrs.bo.m == osrs.aD.k && !dH) {
         if (this.ee.isRuneLiteClientOutdated()) {
            a("RuneLite has been updated!", "Please restart your client.", "");
            osrs.bA.x = 6;
            this.setGameState(GameState.LOGIN_SCREEN);
         } else {
            dH = true;
         }
      } else {
         Object var2 = b.e();
         bu var3 = b.e;

         try {
            if (osrs.bo.m == osrs.aD.k) {
               if (osrs.bo.ds == null && (bU.b() || bh > 250)) {
                  osrs.bo.ds = bU.c();
                  bU.a();
                  bU = null;
               }

               if (osrs.bo.ds != null) {
                  if (var2 != null) {
                     ((gO)var2).c();
                     var2 = null;
                  }

                  osrs.bo.do = null;
                  bZ = false;
                  bh = 0;
                  if (z.a()) {
                     if (this.at()) {
                        this.g(osrs.bo.aP);
                        a(osrs.aD.h);
                     } else {
                        if (!this.au()) {
                           e(65);
                           return;
                        }

                        this.a(osrs.bo.t, osrs.bo.cM);
                        a(osrs.aD.g);
                     }
                  } else {
                     a(osrs.aD.l);
                  }
               }
            }

            fw var18;
            if (osrs.bo.m == osrs.aD.h) {
               if (this.ec != null) {
                  if (!this.ec.isDone()) {
                     return;
                  }

                  if (this.ec.isCancelled()) {
                     e(65);
                     this.ec = null;
                     return;
                  }

                  try {
                     d var4 = (d)this.ec.get();
                     if (!var4.a()) {
                        e(65);
                        this.ec = null;
                        return;
                     }

                     osrs.bo.en = var4.b();
                     osrs.bo.aP = var4.c();
                     al(-1);
                     this.ec = null;
                  } catch (Exception var15) {
                     gG.a((String)null, var15, (short)0);
                     e(65);
                     this.ec = null;
                     return;
                  }
               } else {
                  if (this.dQ == null) {
                     e(65);
                     return;
                  }

                  if (!this.dQ.c()) {
                     return;
                  }

                  if (this.dQ.b()) {
                     gG.a(this.dQ.a(), (Throwable)null, (short)6681);
                     e(65);
                     this.dQ = null;
                     return;
                  }

                  var18 = this.dQ.d();
                  if (var18.a() != 200) {
                     e(65);
                     this.dQ = null;
                     return;
                  }

                  bh = 0;
                  hd var5 = new hd(var18.d());
                  osrs.bo.en = var5.c().getString("access_token");
                  osrs.bo.aP = var5.c().getString("refresh_token");
                  al(-1);
               }

               this.h(osrs.bo.en);
               a(osrs.aD.g);
            }

            if (osrs.bo.m == osrs.aD.g) {
               if (this.ed != null) {
                  if (!this.ed.isDone()) {
                     return;
                  }

                  if (this.ed.isCancelled()) {
                     e(65);
                     this.ed = null;
                     return;
                  }

                  try {
                     b var19 = (b)this.ed.get();
                     if (!var19.a()) {
                        e(65);
                        this.ed = null;
                        return;
                     }

                     this.ea = var19.b();
                     this.ed = null;
                  } catch (Exception var14) {
                     gG.a((String)null, var14, (short)0);
                     e(65);
                     this.ed = null;
                     return;
                  }
               } else {
                  if (this.dR == null) {
                     e(65);
                     return;
                  }

                  if (!this.dR.c()) {
                     return;
                  }

                  if (this.dR.b()) {
                     gG.a(this.dR.a(), (Throwable)null, (short)26448);
                     e(65);
                     this.dR = null;
                     return;
                  }

                  var18 = this.dR.d();
                  if (var18.a() != 200) {
                     gG.a("Login authentication error. Response code: " + var18.a() + " " + var18.b() + " Response body: " + var18.d(), (Throwable)null, (short)11165);
                     e(65);
                     this.dR = null;
                     return;
                  }

                  List var21 = (List)var18.c().get("Content-Type");
                  if (var21 != null && var21.contains(hl.a.a())) {
                     JSONObject var6 = new JSONObject(var18.d());
                     this.ea = var6.getString("token");
                  } else {
                     this.ea = var18.d();
                  }

                  this.dR = null;
               }

               bh = 0;
               a(osrs.aD.l);
            }

            String var20;
            int var22;
            if (osrs.bo.m == osrs.aD.l) {
               if (osrs.bo.do == null) {
                  var20 = em != null ? em : osrs.bo.cQ;
                  var22 = en > 0 ? en : osrs.bo.ch;
                  osrs.bo.do = osrs.bo.aT.a(var20, var22);
               }

               if (osrs.bo.do.a == 2) {
                  throw new IOException();
               }

               if (osrs.bo.do.a == 1) {
                  Socket var23 = (Socket)osrs.bo.do.e;
                  gF var25 = new gF(var23, 40000, 5000);
                  var2 = var25;
                  b.a((gO)var25);
                  osrs.bo.do = null;
                  a(osrs.aD.m);
               }
            }

            r var24;
            if (osrs.bo.m == osrs.aD.m) {
               b.a();
               var24 = osrs.r.a();
               var24.e = null;
               var24.c = 0;
               var24.f = new bu(5000);
               var24.f.a(osrs.eK.a.j);
               b.a(var24);
               b.b();
               var3.d = 0;
               a(osrs.aD.n);
            }

            int var26;
            if (osrs.bo.m == osrs.aD.n) {
               if (osrs.bo.bd != null) {
                  osrs.bo.bd.b();
               }

               if (((gO)var2).a(1)) {
                  var26 = ((gO)var2).b();
                  if (osrs.bo.bd != null) {
                     osrs.bo.bd.b();
                  }

                  if (var26 != 0) {
                     e(var26);
                     return;
                  }

                  var3.d = 0;
                  a(osrs.aD.o);
               }
            }

            if (osrs.bo.m == osrs.aD.o) {
               if (var3.d < 8) {
                  var26 = ((gO)var2).a();
                  if (var26 > 8 - var3.d) {
                     var26 = 8 - var3.d;
                  }

                  if (var26 > 0) {
                     ((gO)var2).a(var3.c, var3.d, var26);
                     var3.d += var26;
                  }
               }

               if (var3.d == 8) {
                  var3.d = 0;
                  osrs.bo.cb = var3.i();
                  a(osrs.aD.p);
               }
            }

            int var7;
            int var8;
            int var9;
            if (osrs.bo.m == osrs.aD.p) {
               b.e.d = 0;
               b.a();
               bu var33 = new bu(500);
               int[] var27 = new int[]{osrs.bo.ds.nextInt(), osrs.bo.ds.nextInt(), osrs.bo.ds.nextInt(), osrs.bo.ds.nextInt()};
               var33.d = 0;
               ((aR)var33).a(1);
               var33.d(var27[0]);
               var33.d(var27[1]);
               var33.d(var27[2]);
               var33.d(var27[3]);
               var33.b(osrs.bo.cb);
               if (w == 40) {
                  var33.d(bV[0]);
                  var33.d(bV[1]);
                  var33.d(bV[2]);
                  var33.d(bV[3]);
               } else {
                  if (w == 50) {
                     ((aR)var33).a(ji.e.b());
                     var33.d(osrs.bA.k);
                  } else {
                     ((aR)var33).a(y.b());
                     switch (y.f) {
                        case 1:
                        case 2:
                           var33.c(osrs.bo.G);
                           ++var33.d;
                           break;
                        case 3:
                           var33.d += 4;
                           break;
                        case 4:
                           var33.d(bL.c(osrs.bA.i));
                     }
                  }

                  if (z.a()) {
                     ((aR)var33).a(hY.b.b());
                     var33.c(this.ea);
                  } else {
                     ((aR)var33).a(hY.a.b());
                     var33.c(cC());
                  }
               }

               var33.a(jW.a, jW.b);
               bV = var27;
               r var28 = osrs.r.a();
               var28.e = null;
               var28.c = 0;
               var28.f = new bu(5000);
               var28.f.d = 0;
               if (w == 40) {
                  var28.f.a(osrs.eK.d.j);
               } else {
                  var28.f.a(osrs.eK.c.j);
               }

               var28.f.b(0);
               var7 = var28.f.d;
               var28.f.d(237);
               var28.f.d(1);
               var28.f.d(kt.a);
               var28.f.a(aP);
               var28.f.a(aQ);
               var8 = 0;
               var28.f.a(var8);
               var28.f.b(var33.c, 0, var33.d);
               var9 = var28.f.d;
               var28.f.c(osrs.bA.i);
               var28.f.a((aj ? 1 : 0) << 1 | (aM ? 1 : 0));
               var28.f.b(osrs.T.eP);
               var28.f.b(osrs.T.eQ);
               bu var10 = var28.f;
               int var13;
               if (bW != null) {
                  ((aR)var10).b((byte[])bW, 0, bW.length);
               } else {
                  byte[] var11 = new byte[24];

                  try {
                     osrs.U.b.a(0L);
                     osrs.U.b.a(var11);

                     int var12;
                     for(var12 = 0; var12 < 24 && var11[var12] == 0; ++var12) {
                     }

                     if (var12 >= 24) {
                        throw new IOException();
                     }
                  } catch (Exception var16) {
                     for(var13 = 0; var13 < 24; ++var13) {
                        var11[var13] = -1;
                     }
                  }

                  ((aR)var10).b((byte[])var11, 0, var11.length);
               }

               var28.f.c(aN);
               var28.f.d(osrs.bo.aN);
               var28.f.a(0);
               aR var39 = new aR(dT.a());
               dT.a(var39);
               var28.f.b(var39.c, 0, var39.c.length);
               var28.f.a(0);
               var28.f.d(0);
               if (osrs.bo.q) {
                  var28.f.v(osrs.bo.bl.c);
                  var28.f.u(osrs.bo.dM.c);
                  var28.f.u(osrs.bo.bg.c);
                  var28.f.u(osrs.bo.bv.c);
                  var28.f.u(osrs.bo.dc.c);
                  var28.f.d(osrs.bo.cF.c);
                  var28.f.d(osrs.bo.cL.c);
               } else {
                  a(var28);
               }

               var28.f.a(var27, var9, var28.f.d);
               var28.f.f(var28.f.d - var7);
               b.a(var28);
               b.b();
               b.p = new hZ(var27);
               int[] var40 = new int[4];

               for(var13 = 0; var13 < 4; ++var13) {
                  var40[var13] = var27[var13] + 50;
               }

               var3.c(var40);
               a(osrs.aD.q);
            }

            if (osrs.bo.m == osrs.aD.q && ((gO)var2).a() > 0) {
               var26 = ((gO)var2).b();
               if (var26 == 61) {
                  var22 = ((gO)var2).a();
                  osrs.bo.bo = var22 == 1 && ((gO)var2).b() == 1;
                  a(osrs.aD.p);
               }

               if (var26 == 21 && w == 20) {
                  a(osrs.aD.w);
               } else if (var26 == 2) {
                  if (osrs.bo.q) {
                     a(osrs.aD.i);
                  } else {
                     a(osrs.aD.y);
                  }
               } else if (var26 == 15 && w == 40) {
                  b.g = -1;
                  a(osrs.aD.f);
               } else if (var26 == 64) {
                  a(osrs.aD.u);
               } else if (var26 == 23 && bi < 1) {
                  ++bi;
                  a(osrs.aD.k);
               } else if (var26 == 29) {
                  a(osrs.aD.d);
               } else {
                  if (var26 != 69) {
                     e(var26);
                     return;
                  }

                  a(osrs.aD.r);
               }
            }

            if (osrs.bo.m == osrs.aD.i) {
               osrs.bo.cu = true;
               a(osrs.aB.f);
               a((int)0);
            }

            if (osrs.bo.m == osrs.aD.r && ((gO)var2).a() >= 2) {
               ((gO)var2).a((byte[])var3.c, 0, 2);
               var3.d = 0;
               osrs.bo.cO = var3.d();
               a(osrs.aD.s);
            }

            if (osrs.bo.m == osrs.aD.s && ((gO)var2).a() >= osrs.bo.cO) {
               var3.d = 0;
               ((gO)var2).a(var3.c, var3.d, osrs.bo.cO);
               jm var35 = jm.a()[((aR)var3).b()];
               gK var30 = jx.a(var35);
               this.dW = new bV(var3, var30);
               a(osrs.aD.t);
            }

            if (osrs.bo.m == osrs.aD.t && this.dW.a()) {
               this.dV = this.dW.c();
               this.dW.b();
               this.dW = null;
               if (this.dV == null) {
                  e(22);
                  return;
               }

               b.a();
               var24 = osrs.r.a();
               var24.e = null;
               var24.c = 0;
               var24.f = new bu(5000);
               var24.f.a(osrs.eK.e.j);
               var24.f.b(this.dV.d);
               var24.f.a(this.dV);
               b.a(var24);
               b.b();
               this.dV = null;
               a(osrs.aD.q);
            }

            if (osrs.bo.m == osrs.aD.u && ((gO)var2).a() > 0) {
               osrs.bo.bP = ((gO)var2).b();
               a(osrs.aD.v);
            }

            if (osrs.bo.m == osrs.aD.v && ((gO)var2).a() >= osrs.bo.bP) {
               ((gO)var2).a((byte[])var3.c, 0, osrs.bo.bP);
               var3.d = 0;
               a(osrs.aD.q);
            }

            if (osrs.bo.m == osrs.aD.w && ((gO)var2).a() > 0) {
               bj = (((gO)var2).b() + 3) * 60;
               a(osrs.aD.x);
            }

            if (osrs.bo.m == osrs.aD.x) {
               bh = 0;
               int var10002 = bj / 60;
               osrs.bA.a(osrs.bv.S, osrs.bv.T, "" + var10002 + osrs.bv.U);
               if (--bj <= 0) {
                  a(osrs.aD.k);
               }
            } else if (osrs.bo.m == osrs.aD.j) {
               var24 = osrs.r.a();
               var24.e = null;
               var24.c = 0;
               var24.f = new bu(5000);
               var24.f.a(osrs.eK.h.j);
               var24.f.b(osrs.eK.h.k);
               var24.f.v(osrs.bo.bz.c);
               var24.f.t(osrs.bo.B.c);
               var24.f.d(osrs.bo.Q.c);
               var24.f.d(osrs.bo.P.c);
               var24.f.d(osrs.bo.ei.c);
               var24.f.t(0);
               var24.f.d(osrs.bo.de.c);
               var24.f.u(osrs.bo.bj.c);
               var24.f.u(osrs.bo.cq.c);
               var24.f.d(osrs.bo.ff.c);
               var24.f.v(osrs.bo.cn.c);
               var24.f.v(osrs.bo.bF.c);
               var24.f.d(osrs.bo.ej.c);
               var24.f.t(osrs.bo.di.c);
               var24.f.d(osrs.bo.eD.c);
               var24.f.t(osrs.bo.cw.c);
               b.a(var24);
               b.b();
               a(osrs.aD.y);
            } else {
               if (osrs.bo.m == osrs.aD.y && ((gO)var2).a() >= 1) {
                  osrs.bo.bk = ((gO)var2).b();
                  if (osrs.bo.bk != osrs.eK.f.k) {
                     e(osrs.bo.bk);
                     return;
                  }

                  a(osrs.aD.a);
               }

               if (osrs.bo.m == osrs.aD.a && ((gO)var2).a() >= osrs.bo.bk) {
                  boolean var37 = ((gO)var2).b() == 1;
                  ((gO)var2).a((byte[])var3.c, 0, 4);
                  var3.d = 0;
                  boolean var32 = false;
                  if (var37) {
                     int var29 = var3.aj() << 24;
                     var7 = var29 | var3.aj() << 16;
                     var8 = var7 | var3.aj() << 8;
                     var9 = var8 | var3.aj();
                     bL.a(osrs.bA.i, var9);
                  }

                  if (A) {
                     bL.a(osrs.bA.i);
                  } else {
                     bL.a((String)null);
                  }

                  iu.a(714819761);
                  by = ((gO)var2).b();
                  bA = ((gO)var2).b() == 1;
                  P = ((gO)var2).b();
                  P <<= 8;
                  P += ((gO)var2).b();
                  R = ((gO)var2).b();
                  ((gO)var2).a((byte[])var3.c, 0, 8);
                  var3.d = 0;
                  this.cs = var3.i();
                  this.U(-1);
                  ((gO)var2).a((byte[])var3.c, 0, 8);
                  var3.d = 0;
                  cw = var3.i();
                  ((gO)var2).a((byte[])var3.c, 0, 8);
                  var3.d = 0;
                  cx = var3.i();
                  osrs.dZ.a().a(this.cq);
                  a(osrs.aD.b);
               }

               if (osrs.bo.m == osrs.aD.b && ((gO)var2).a() >= b.g) {
                  ((gO)var2).a((byte[])var3.c, 0, 1);
                  var3.d = 0;
                  if (var3.ak()) {
                     ((gO)var2).a((byte[])var3.c, 1, 1);
                     var3.d = 0;
                  }

                  bm[] var38 = osrs.bm.a();
                  var22 = var3.al();
                  if (var22 < 0 || var22 >= var38.length) {
                     throw new IOException("Invalid ServerProt: " + var22 + " at " + var3.d);
                  }

                  b.f = var38[var22];
                  b.g = b.f.bu;
                  ((gO)var2).a((byte[])var3.c, 0, 2);
                  var3.d = 0;
                  b.g = var3.d();
                  a(osrs.aD.c);
               }

               if (osrs.bo.m == osrs.aD.c) {
                  if (((gO)var2).a() >= b.g) {
                     var3.d = 0;
                     ((gO)var2).a((byte[])var3.c, 0, b.g);
                     ca.c();
                     osrs.bo.n();
                     G.a(var3);
                     v();
                     osrs.bo.aZ = -1;
                     if (kt.a < 237) {
                        osrs.bo.a(var3);
                     } else {
                        osrs.bo.b(var3);
                     }

                     b.f = null;
                     bQ = false;
                  }
               } else {
                  if (osrs.bo.m == osrs.aD.d && ((gO)var2).a() >= 2) {
                     var3.d = 0;
                     ((gO)var2).a((byte[])var3.c, 0, 2);
                     var3.d = 0;
                     osrs.bo.dQ = var3.d();
                     a(osrs.aD.e);
                  }

                  if (osrs.bo.m == osrs.aD.e && ((gO)var2).a() >= osrs.bo.dQ) {
                     var3.d = 0;
                     ((gO)var2).a((byte[])var3.c, 0, osrs.bo.dQ);
                     var3.d = 0;
                     var20 = ((aR)var3).m();
                     String var34 = ((aR)var3).m();
                     String var31 = ((aR)var3).m();
                     osrs.bA.a(var20, var34, var31);
                     a((int)10);
                     if (z.a()) {
                        osrs.bA.b((int)9);
                     }
                  }

                  if (osrs.bo.m == osrs.aD.f) {
                     if (b.g == -1) {
                        if (((gO)var2).a() < 2) {
                           return;
                        }

                        ((gO)var2).a((byte[])var3.c, 0, 2);
                        var3.d = 0;
                        b.g = var3.d();
                     }

                     if (((gO)var2).a() >= b.g) {
                        ((gO)var2).a((byte[])var3.c, 0, b.g);
                        var3.d = 0;
                        var26 = b.g;
                        ca.e();
                        b.a();
                        b.k = 0;
                        b.e.d = 0;
                        b.f = null;
                        b.m = null;
                        b.n = null;
                        b.o = null;
                        b.g = 0;
                        b.i = 0;
                        aZ = 0;
                        ba = "";
                        osrs.bo.q();
                        osrs.eM.g = 0;
                        cV = 0;
                        osrs.bo.aQ.c();
                        im.a();
                        a((int)30);
                        if (b != null && b.p != null) {
                           r var36 = osrs.r.a(osrs.u.bk, b.p);
                           var36.f.a(osrs.bo.p());
                           var36.f.b(osrs.T.eP);
                           var36.f.b(osrs.T.eQ);
                           b.a(var36);
                        }

                        G.a(var3);
                        if (var3.d != var26) {
                           throw new RuntimeException();
                        }
                     }
                  } else {
                     ++bh;
                     if (bh > 2000) {
                        if (bi < 1) {
                           if (osrs.bo.ch == osrs.bo.aG) {
                              osrs.bo.ch = osrs.bo.dn;
                           } else {
                              osrs.bo.ch = osrs.bo.aG;
                           }

                           ++bi;
                           a(osrs.aD.k);
                        } else {
                           e(-3);
                        }
                     }
                  }
               }
            }
         } catch (IOException var17) {
            if (bi < 1) {
               if (osrs.bo.ch == osrs.bo.aG) {
                  osrs.bo.ch = osrs.bo.dn;
               } else {
                  osrs.bo.ch = osrs.bo.aG;
               }

               ++bi;
               a(osrs.aD.k);
            } else {
               e(-2);
            }
         }
      }

   }

   public static void a(jc var0) {
      bG var1 = var0.o;
      s.getCallbacks().post(new WorldViewUnloaded(var1));
      var0.g.b(var1.x.ay, var1.x.az, var1.x.aA, var1.x.aB, var1.x.aC);
      var0.g.q = var1.x.q;
      var0.g.w = var1.x.w;
      var0.g.x = var1.x.x;
      var0.g.y = var1.x.y;
      var0.g.bD = var1.x.bD;
      var0.g.bE = var1.x.bE;
      var0.g.Y = var1.x.Y;
      var0.g.Z = var1.x.Z;
      var0.g.aa = var1.x.aa;
      var0.g.ab = var1.x.ab;
      dh var2 = var1.x;
      var1.x = var0.g;
      var1.x.aZ = var1;
      var2.a();
      var1.u = var0.i;
      var1.v = var0.h;
      var1.n = var0.M;

      for(ic var3 = (ic)var1.b.j(); var3 != null; var3 = (ic)var1.b.h()) {
         if (var3.a() == -1) {
            var3.e(0);
            b(var1, var3);
         } else {
            var3.ad();
         }
      }

      bq();
      var1.l.a(var0.r);

      for(hu var5 = (hu)var1.l.j(); var5 != null; var5 = (hu)var1.l.h()) {
         aC var4 = var5.w;
         if (var4 != null) {
            var5.s();
            if (var5.v != null) {
               var5.a = var5.t + (int)(Math.random() * (double)(var5.u - var5.t));
            }
         }
      }

      for(int var6 = 0; var6 < 104; ++var6) {
         for(int var7 = 0; var7 < 104; ++var7) {
            e(var1, var1.y, var6, var7);
         }
      }

      s.getCallbacks().post(new WorldViewLoaded(var1));
      var0.g.bV = true;
      var0.g.m();
      bO = false;
      DrawCallbacks var8 = var0.s;
      if (var8 != null && ef == var8) {
         var8.swapScene(var1.x);
      }

      bk();
      var1.x.h();
   }

   public static boolean d(int var0) {
      int var1;
      int var4;
      if (var0 == 6599) {
         if (!a && osrs.bo.fd.getInstructions()[osrs.bn.p] != 6599) {
            throw new AssertionError();
         } else {
            var1 = s.getObjectStackSize();
            Object[] var11 = s.getObjectStack();
            --var1;
            String var12 = (String)var11[var1];
            s.setObjectStackSize(var1);
            if ("debug".equals(var12)) {
               var4 = s.getIntStackSize();
               var11 = s.getObjectStack();
               --var1;
               String var14 = (String)var11[var1];
               StringBuffer var15 = new StringBuffer();
               Matcher var16 = Pattern.compile("%(.)").matcher(var14);

               while(var16.find()) {
                  var16.appendReplacement(var15, "");
                  switch (var16.group(1).charAt(0)) {
                     case 'd':
                     case 'i':
                        int[] var8 = s.getIntStack();
                        --var4;
                        var15.append(var8[var4]);
                        break;
                     case 's':
                        Object[] var9 = s.getObjectStack();
                        --var1;
                        var15.append((String)var9[var1]);
                        break;
                     default:
                        var15.append(var16.group(0)).append("=unknown");
                  }
               }

               var16.appendTail(var15);
               dF.debug(var15.toString());
               s.setObjectStackSize(var1);
               s.setIntStackSize(var4);
               return true;
            } else if ("mes".equals(var12)) {
               var4 = s.getIntStackSize();
               int[] var5 = s.getIntStack();
               --var4;
               int var6 = var5[var4];
               var11 = s.getObjectStack();
               --var1;
               String var7 = (String)var11[var1];
               s.setObjectStackSize(var1);
               s.setIntStackSize(var4);
               s.addChatMessage(ChatMessageType.of(var6), "", var7, (String)null, true);
               return true;
            } else {
               ScriptCallbackEvent var13 = new ScriptCallbackEvent();
               var13.setScript(osrs.bo.fd);
               var13.setEventName(var12);
               s.getCallbacks().post(var13);
               return true;
            }
         }
      } else {
         if (var0 == 40) {
            var1 = osrs.bo.fd.getIntOperands()[osrs.bn.p];
            s.getCallbacks().post(new ScriptPreFired(var1));
         } else if (var0 == 21) {
            s.getCallbacks().post(new ScriptPostFired((int)osrs.bo.fd.getHash()));
         } else if (var0 == 5504) {
            int[] var10 = s.getIntStack();
            int var2 = s.getIntStackSize();
            int var3 = var10[var2 - 2];
            var4 = var10[var2 - 1];
            if (!bJ) {
               n(var4, var3);
            }
         }

         return false;
      }
   }

   public aH a(int var1, short[] var2, short[] var3) {
      ModelUnlit var4 = this.W(var1);
      if (var4 == null) {
         return null;
      } else {
         if (var2 != null && var3 != null) {
            var4.r();

            for(int var5 = 0; var5 < var2.length; ++var5) {
               var4.c(var2[var5], var3[var5]);
            }
         }

         return var4.m();
      }
   }

   public aH a(Model[] var1, int var2) {
      return new aH((aH[])Arrays.copyOf(var1, var2, aH[].class), var2);
   }

   public static void a(bG var0, int var1, int var2, int var3) {
      jh var4 = var0.x.q()[var1][var2][var3];
      fJ var5 = var0.m[var1][var2][var3];
      fJ var6 = var0.a[var1][var2][var3];
      if (s.getGameState() != GameState.LOGGED_IN) {
         var0.m[var1][var2][var3] = var6;
         osrs.bo.y = null;
      } else {
         iC var7;
         ItemDespawned var8;
         if (var5 != var6) {
            if (var5 != null) {
               for(var7 = (iC)var5.j(); var7 != null; var7 = (iC)var5.h()) {
                  var8 = new ItemDespawned(var4, var7);
                  s.getCallbacks().post(var8);
               }
            }

            var0.m[var1][var2][var3] = var6;
         }

         var7 = osrs.bo.y;
         if (var7 != null) {
            osrs.bo.y = null;
         }

         if (var6 == null) {
            if (var7 != null) {
               var8 = new ItemDespawned(var4, var7);
               s.getCallbacks().post(var8);
            }
         } else {
            az var16 = var6.a;
            iC var9 = null;
            az var10 = var16.ac();
            if (var16 != var10) {
               iC var11 = (iC)var10;
               if (var2 != var11.e() || var3 != var11.d()) {
                  var9 = var11;
               }
            }

            az var13 = var16.ab();
            if (var9 == null && var16 != var13) {
               iC var12 = (iC)var13;
               if (var2 != var12.e() || var3 != var12.d()) {
                  var9 = var12;
               }
            }

            if (var7 != null && var7 != var10 && var7 != var13) {
               ItemDespawned var14 = new ItemDespawned(var4, var7);
               s.getCallbacks().post(var14);
            }

            if (var9 != null) {
               var9.c(var2);
               var9.e(var3);
               var9.c = var0.getId();
               ItemSpawned var15 = new ItemSpawned(var4, var9);
               s.getCallbacks().post(var15);
            }
         }
      }

   }

   public static long a(int var0, int var1, int var2, int var3, boolean var4, int var5, int var6) {
      long var7 = ((long)var1 & 127L) << 0 | ((long)var2 & 127L) << 7 | ((long)var0 & 3L) << 14 | ((long)var3 & 7L) << 16 | ((long)var5 & 4294967295L) << 20 | ((long)var6 & 4095L) << 52;
      if (var4) {
         var7 |= 524288L;
      }

      return var7;
   }

   public static int a(int var0, int var1) {
      if (var0 >= 0 && var0 < osrs.bo.fS.length) {
         int var2 = osrs.bo.fS[var0];
         if (var2 == 0) {
            throw new IndexOutOfBoundsException("Varbit " + var0 + " does not exist");
         } else {
            int var3 = var2 & 255;
            int var4 = var2 >> 8 & 255;
            int var5 = osrs.x.a[var4 - var3];
            return var1 >> var3 & var5;
         }
      } else {
         throw new IndexOutOfBoundsException("Varbit " + var0 + " does not exist");
      }
   }

   public static void k() {
      if (osrs.bo.aQ != null) {
         osrs.bo.aQ.x.a(bL.p());
      }

   }

   public static void a(aB var0) {
      if (bc != var0) {
         bc = var0;
      }

   }

   public static av a(int var0, boolean var1, boolean var2, boolean var3, boolean var4) {
      gR var5 = null;
      if (osrs.U.c != null) {
         var5 = new gR(var0, osrs.U.c, osrs.bo.ca[var0], 1000000);
      }

      return new av(var5, osrs.bo.cl, osrs.bo.eg, var0, var1, var2, var3, var4, false);
   }

   public void l() {
      synchronized(osrs.bo.eg) {
         if (w != 1000) {
            boolean var2 = osrs.bo.eg.a();
            if (!var2) {
               this.az();
            }
         }

      }
   }

   public static void a(r var0) {
      var0.f.d(osrs.bo.ej.c);
      var0.f.v(osrs.bo.ei.c);
      var0.f.d(osrs.bo.bj.c);
      var0.f.d(osrs.bo.di.c);
      var0.f.d(osrs.bo.B.c);
      var0.f.v(osrs.bo.dM.c);
      var0.f.d(osrs.bo.bl.c);
      var0.f.v(osrs.bo.cq.c);
      var0.f.u(osrs.bo.bv.c);
      var0.f.t(osrs.bo.bg.c);
      var0.f.d(osrs.bo.bF.c);
      var0.f.d(0);
      var0.f.t(osrs.bo.cF.c);
      var0.f.u(osrs.bo.de.c);
      var0.f.d(osrs.bo.P.c);
      var0.f.u(osrs.bo.cL.c);
      var0.f.v(osrs.bo.ff.c);
      var0.f.d(osrs.bo.eD.c);
      var0.f.v(osrs.bo.Q.c);
      var0.f.d(osrs.bo.cn.c);
      var0.f.d(osrs.bo.cw.c);
      var0.f.u(osrs.bo.dc.c);
      var0.f.d(osrs.bo.bz.c);
   }

   public SpritePixels createSpritePixels(int[] var1, int var2, int var3) {
      return this.a(var1, var2, var3);
   }

   public SpritePixels createItemSprite(int var1, int var2, int var3, int var4, int var5, boolean var6, int var7) {
      if (!s.isClientThread()) {
         if (!osrs.T.eo) {
            throw new AssertionError("must be called on client thread");
         } else {
            throw new IllegalStateException("must be called on client thread");
         }
      } else {
         dN var8 = osrs.aW.h;
         int var9 = var8.d();
         var8.a(var7);

         aV var10;
         try {
            var10 = this.a(var1, var2, var3, var4, var5, var6, 36, 32);
         } finally {
            var8.a(var9);
         }

         return var10;
      }
   }

   public static void e(int var0) {
      if (var0 != 56 && var0 != 57) {
         a((int)10);
         osrs.bA.b((int)9);
      } else {
         a((int)11);
      }

      if (var0 == -3) {
         osrs.bA.a(osrs.bv.V, osrs.bv.W, osrs.bv.X);
      } else if (var0 == -2) {
         osrs.bA.a(osrs.bv.Y, osrs.bv.Z, osrs.bv.aa);
      } else if (var0 == -1) {
         osrs.bA.a(osrs.bv.ab, osrs.bv.ac, osrs.bv.ad);
      } else if (var0 == 3) {
         osrs.bA.b((int)3);
         osrs.bA.e = 1;
      } else if (var0 == 4) {
         osrs.bA.a((int)0);
      } else if (var0 == 5) {
         osrs.bA.e = 2;
         osrs.bA.a(osrs.bv.ah, osrs.bv.ai, osrs.bv.aj);
      } else if (var0 == 68) {
         if (!bQ) {
            bQ = true;
            osrs.bo.k();
            return;
         }

         osrs.bA.a(osrs.bv.ak, osrs.bv.al, osrs.bv.am);
      } else if (!aR && var0 == 6) {
         osrs.bA.a(osrs.bv.ak, osrs.bv.al, osrs.bv.am);
      } else if (var0 == 7) {
         osrs.bA.a(osrs.bv.an, osrs.bv.ao, osrs.bv.ap);
      } else if (var0 == 8) {
         osrs.bA.a(osrs.bv.aq, osrs.bv.ar, osrs.bv.as);
      } else if (var0 == 9) {
         osrs.bA.a(osrs.bv.at, osrs.bv.au, osrs.bv.av);
      } else if (var0 == 10) {
         osrs.bA.a(osrs.bv.aw, osrs.bv.ax, osrs.bv.ay);
      } else if (var0 == 11) {
         osrs.bA.a(osrs.bv.az, osrs.bv.aA, osrs.bv.aB);
      } else if (var0 == 12) {
         osrs.bA.a(osrs.bv.aC, osrs.bv.aD, osrs.bv.aE);
         osrs.bA.b((int)34);
      } else if (var0 == 13) {
         osrs.bA.a(osrs.bv.aF, osrs.bv.aG, osrs.bv.aH);
      } else if (var0 == 14) {
         osrs.bA.a(osrs.bv.aI, osrs.bv.aJ, osrs.bv.aK);
      } else if (var0 == 16) {
         osrs.bA.a(osrs.bv.aL, osrs.bv.aM, osrs.bv.aN);
         osrs.bA.b((int)33);
      } else if (var0 == 17) {
         osrs.bA.a(osrs.bv.aO, osrs.bv.aP, osrs.bv.aQ);
      } else if (var0 == 18) {
         osrs.bA.a((int)1);
      } else if (var0 == 19) {
         osrs.bA.a(osrs.bv.aU, osrs.bv.aV, osrs.bv.aW);
      } else if (var0 == 20) {
         osrs.bA.a(osrs.bv.aX, osrs.bv.aY, osrs.bv.aZ);
      } else if (var0 == 22) {
         osrs.bA.a(osrs.bv.ba, osrs.bv.bb, osrs.bv.bc);
      } else if (var0 == 23) {
         osrs.bA.a(osrs.bv.bd, osrs.bv.be, osrs.bv.bf);
      } else if (var0 == 24) {
         osrs.bA.a(osrs.bv.bg, osrs.bv.bh, osrs.bv.bi);
      } else if (var0 == 25) {
         osrs.bA.a(osrs.bv.bj, osrs.bv.bk, osrs.bv.bl);
      } else if (var0 == 26) {
         osrs.bA.a(osrs.bv.bm, osrs.bv.bn, osrs.bv.bo);
      } else if (var0 == 27) {
         osrs.bA.a(osrs.bv.bp, osrs.bv.bq, osrs.bv.br);
      } else if (var0 == 31) {
         osrs.bA.a(osrs.bv.by, osrs.bv.bz, osrs.bv.bA);
      } else if (var0 == 32) {
         osrs.bA.a((int)2);
      } else if (var0 == 37) {
         osrs.bA.a(osrs.bv.bE, osrs.bv.bF, osrs.bv.bG);
      } else if (var0 == 38) {
         osrs.bA.a(osrs.bv.bH, osrs.bv.bI, osrs.bv.bJ);
      } else if (var0 == 74) {
         osrs.bA.a(osrs.bv.bK, osrs.bv.bL, osrs.bv.bM);
      } else if (var0 == 55) {
         osrs.bA.b((int)8);
      } else if (var0 == 56) {
         osrs.bA.a(osrs.bv.bR, osrs.bv.bS, osrs.bv.bT);
      } else if (var0 == 57) {
         osrs.bA.a(osrs.bv.bU, osrs.bv.bV, osrs.bv.bW);
      } else if (var0 == 61) {
         osrs.bA.a("", "Please enter your date of birth (DD/MM/YYYY)", "");
         osrs.bA.b((int)7);
      } else if (var0 == 62) {
         osrs.bA.a(osrs.bv.bX, osrs.bv.bY, osrs.bv.bZ);
      } else if (var0 == 63) {
         osrs.bA.a(osrs.bv.ca, osrs.bv.cb, osrs.bv.cc);
      } else if (var0 != 65 && var0 != 67) {
         if (var0 == 71) {
            osrs.bA.b((int)7);
            osrs.bA.a("There was a problem updating your DOB.", "Please try again later. If the problem ", "persists, please contact Jagex Support.");
         } else if (var0 == 73) {
            osrs.bA.b((int)6);
            osrs.bA.a(osrs.bv.eF, osrs.bv.eG, osrs.bv.eH);
         } else if (var0 == 72) {
            osrs.bA.b((int)32);
         } else {
            osrs.bA.a(osrs.bv.cg, osrs.bv.ch, osrs.bv.ci);
         }
      } else {
         osrs.bA.a(osrs.bv.cd, osrs.bv.ce, osrs.bv.cf);
      }

   }

   public AccountType getAccountType() {
      int var1 = this.getVarbitValue(1777);
      switch (var1) {
         case 1:
            return AccountType.IRONMAN;
         case 2:
            return AccountType.ULTIMATE_IRONMAN;
         case 3:
            return AccountType.HARDCORE_IRONMAN;
         case 4:
            return AccountType.GROUP_IRONMAN;
         case 5:
            return AccountType.HARDCORE_GROUP_IRONMAN;
         default:
            return AccountType.NORMAL;
      }
   }

   public static void a(gg var0, gg var1) {
      if (osrs.bz.o) {
         osrs.df.k(0, 0, osrs.T.eP, osrs.T.eQ, 0);
      }

   }

   public static aH a(dh var0, dh var1, aC var2, int var3, int var4, int var5, int var6, int var7) {
      if (var2.z) {
         return null;
      } else {
         int var8 = var1.bQ - var0.bQ;
         int var9 = var1.bR - var0.bR;
         int var10 = var6 + var8;
         int var11 = var7 + var9;
         if (var10 >= var0.o && var10 < var0.z && var11 >= var0.bS && var11 < var0.aK) {
            dG var13;
            if (var3 == 22) {
               dr var16 = var0.C(var5, var10, var11);
               if (var16 == null) {
                  return null;
               }

               if (!a(var16.f, var16.e, var2.getId(), var3, var4)) {
                  return null;
               }

               var13 = var16.d();
               if (!(var13 instanceof aH)) {
                  return null;
               }

               return (aH)var13;
            }

            if (var3 >= 9) {
               dA var15 = var0.y(var5, var10, var11);
               if (var15 == null) {
                  return null;
               }

               if (!a(var15.a, var15.b, var2.getId(), var3, var4)) {
                  return null;
               }

               var13 = var15.d();
               if (!(var13 instanceof aH)) {
                  return null;
               }

               return (aH)var13;
            }

            if (var3 == 0 || var3 == 1 || var3 == 3) {
               dy var14 = var0.A(var5, var10, var11);
               if (var14 == null) {
                  return null;
               }

               if (!a(var14.a, var14.b, var2.getId(), var3, var4)) {
                  return null;
               }

               var13 = var14.e();
               if (!(var13 instanceof aH)) {
                  return null;
               }

               return (aH)var13;
            }

            if (var3 == 4 || var3 == 5 || var3 == 6 || var3 == 7) {
               dP var12 = var0.B(var5, var10, var11);
               if (var12 == null) {
                  return null;
               } else if (!a(var12.a, var12.b, var2.getId(), var3, var4)) {
                  return null;
               } else {
                  var13 = var12.f();
                  return !(var13 instanceof aH) ? null : (aH)var13;
               }
            }
         }

         return null;
      }
   }

   public static final void f(int var0) {
      osrs.bo.o();
      switch (var0) {
         case 1:
            osrs.bA.b((int)24);
            osrs.bA.a(osrs.bv.dE, osrs.bv.dF, osrs.bv.dG);
            break;
         case 2:
            osrs.bA.j();
      }

   }

   public static long m() {
      return cw;
   }

   public static long n() {
      return cx;
   }

   public static final void o() {
      osrs.cT.a();
      osrs.bo.b();
      osrs.cU.a();
      osrs.C.a.a();
      osrs.E.a.a();
      osrs.M.a();
      osrs.N.d.a();
      osrs.O.a();
      osrs.Z.a();
      osrs.bo.g();
      osrs.cC.a.a();
      osrs.af.d.a();
      osrs.at.a();
      osrs.aC.b();
      osrs.aE.k.a();
      osrs.cV.a.a();
      osrs.aN.a.a();
      osrs.aN.b.a();
      osrs.aO.e.a();
      osrs.aO.f.a();
      osrs.aO.g.a();
      osrs.aT.a();
      osrs.bk.a();
      osrs.bn.m.a();
      osrs.bn.n.a();
      osrs.bs.a();
      osrs.bH.u.a();
      osrs.bB.b.a();
      if (ag != null) {
         ag.a();
      }

      if (ah != null) {
         ah.a();
      }

      osrs.bo.c();
      osrs.cL.a();
      osrs.bC.a.a();
      osrs.cS.a();
      osrs.cE.a.a();
      osrs.cM.a();
      osrs.dj.a();
      osrs.bD.a.a();
      osrs.cN.a.a();
      bM.a();
      bN.a();
      osrs.bo.f();
      if (osrs.bo.cR != null) {
         osrs.bo.cR.a();
      }

      if (osrs.aW.h.c != null) {
         ((dO)osrs.aW.h.c).b();
      }

      osrs.v.a.a();
      jZ.a();
      if (osrs.bo.P != null) {
         osrs.bo.P.a((byte)81);
      }

      if (osrs.bo.ej != null) {
         osrs.bo.ej.a((byte)28);
      }

      if (osrs.bo.Q != null) {
         osrs.bo.Q.a((byte)62);
      }

      if (osrs.bo.cF != null) {
         osrs.bo.cF.a((byte)114);
      }

      if (osrs.bo.bz != null) {
         osrs.bo.bz.a((byte)52);
      }

      if (osrs.bo.ff != null) {
         osrs.bo.ff.a((byte)17);
      }

      if (osrs.bo.B != null) {
         osrs.bo.B.a((byte)73);
      }

      if (osrs.bo.ei != null) {
         osrs.bo.ei.a((byte)116);
      }

      if (osrs.bo.bl != null) {
         osrs.bo.bl.a((byte)91);
      }

      if (osrs.bo.bF != null) {
         osrs.bo.bF.a((byte)93);
      }

      if (osrs.bo.aJ != null) {
         osrs.bo.aJ.a((byte)104);
      }

      if (osrs.bo.bg != null) {
         osrs.bo.bg.a((byte)96);
      }

      if (osrs.bo.bj != null) {
         osrs.bo.bj.a((byte)66);
      }

      if (osrs.bo.cn != null) {
         osrs.bo.cn.a((byte)48);
      }

      if (osrs.bo.cL != null) {
         osrs.bo.cL.a((byte)55);
      }

      if (osrs.bo.cw != null) {
         osrs.bo.cw.a((byte)76);
      }

      if (osrs.bo.bv != null) {
         osrs.bo.bv.a((byte)85);
      }

      if (osrs.bo.dM != null) {
         osrs.bo.dM.a((byte)66);
      }

      if (osrs.bo.di != null) {
         osrs.bo.di.a((byte)51);
      }

      if (osrs.bo.dc != null) {
         osrs.bo.dc.a((byte)57);
      }

      if (osrs.bo.eD != null) {
         osrs.bo.eD.a((byte)122);
      }

      if (osrs.bo.de != null) {
         osrs.bo.de.a((byte)14);
      }

      if (osrs.bo.cq != null) {
         osrs.bo.cq.a((byte)10);
      }

   }

   public MenuEntry[] getMenuEntries() {
      return cJ.getMenuEntries();
   }

   public static void a(dh var0) {
      long[] var1 = var0.bw;

      for(int var2 = 0; var2 < 4; ++var2) {
         for(int var3 = 0; var3 < var0.F; ++var3) {
            for(int var4 = 0; var4 < var0.G; ++var4) {
               int var5 = var0.m(var2, var3, var4);
               long var6 = 1080863910568919040L;
               dv var8 = var0.bd[var5];
               if (var8 != null) {
                  var6 |= (long)var8.getRBG() & 16777215L;
               }

               du var9 = var0.bo[var5];
               if (var9 != null) {
                  if (!a && var8 != null) {
                     throw new AssertionError();
                  }

                  var6 |= ((long)var9.e & 15L) << 50 | ((long)var9.f & 3L) << 48 | ((long)var9.getModelOverlay() & 16777215L) << 24 | (long)var9.getModelUnderlay() & 16777215L;
               }

               if (var2 == 0 && (var6 & 16777215L) == 0L && (var0.bY[1][var3][var4] & 2) != 0) {
                  var6 |= 1152921504606846976L;
               }

               if (var0.n(var5) && (var0.bY[var2][var3][var4] & 24) == 0) {
                  var6 |= 18014398509481984L;
               }

               if (var2 < 3 && var0.n(var0.aY + var5) && (var0.bY[var2 + 1][var3][var4] & 8) != 0) {
                  var6 |= 36028797018963968L;
               }

               var1[var5] = var6;
            }
         }
      }

   }

   public static long[] p() {
      long[] var0 = new long[16];
      aV var1 = new aV(8, 8);
      var1.l();
      osrs.df.i();
      osrs.aW.h.q = true;
      osrs.aW.h.a = 0;

      try {
         for(int var2 = 0; var2 < 13; ++var2) {
            du var3 = new du(var2, 0, -1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 0, 0);
            int[] var4 = var3.getVertexX();
            int[] var5 = var3.getVertexZ();
            int[] var6 = var3.getFaceX();
            int[] var7 = var3.getFaceY();
            int[] var8 = var3.getFaceZ();

            int var9;
            for(var9 = 0; var9 < var4.length; ++var9) {
               osrs.du.k[var9] = (float)(var4[var9] * 8 >> 7);
               osrs.du.l[var9] = (float)((128 - var5[var9]) * 8 >> 7);
            }

            for(var9 = 0; var9 < var6.length; ++var9) {
               int var10 = var6[var9];
               int var11 = var7[var9];
               int var12 = var8[var9];
               boolean var13 = var3.getTriangleColorA()[var9] == 0;
               int var14 = var13 ? 0 : 1;
               osrs.df.c(osrs.du.l[var10], osrs.du.l[var11], osrs.du.l[var12], osrs.du.k[var10], osrs.du.k[var11], osrs.du.k[var12], 0.0F, 0.0F, 0.0F, var14);
            }

            for(var9 = 0; var9 < 64; ++var9) {
               var0[var2] |= (long)(var1.getPixels()[var9] & 1) << var9;
            }
         }

         if (!a && var0[0] != 0L) {
            throw new AssertionError();
         }

         if (!a && var0[1] != -1L) {
            throw new AssertionError();
         }
      } finally {
         osrs.df.f();
         osrs.df.i();
      }

      return var0;
   }

   public static final void q() {
      osrs.af.a();
   }

   public static final void r() {
      if (bk > 0) {
         osrs.bo.o();
      } else {
         ca.b();
         a((int)40);
         osrs.bo.cU = b.e();
         b.d();
      }

   }

   public static void a(ArrayList var0, int var1, int var2, int var3, int var4) {
      if (!var0.isEmpty()) {
         int var5 = (Integer)var0.get(0);
         if (var5 == -1 && !bH) {
            osrs.eZ.a(0, 0);
         } else if (var5 != -1) {
            boolean var6;
            if (osrs.eZ.e.isEmpty()) {
               var6 = false;
            } else {
               eX var7 = (eX)osrs.eZ.e.get(0);
               var6 = var7 != null && var7.a == var5;
            }

            if (!var6 && aa() != 0) {
               ArrayList var9 = new ArrayList();

               for(int var8 = 0; var8 < var0.size(); ++var8) {
                  var9.add(new eX(osrs.bo.cL, (Integer)var0.get(var8), 0, aa(), false));
               }

               if (bH) {
                  osrs.eZ.e.clear();
                  osrs.eZ.e.addAll(var9);
                  osrs.eZ.g = var1;
                  osrs.eZ.h = var2;
                  osrs.eZ.i = var3;
                  osrs.eZ.j = var4;
               } else {
                  osrs.eZ.a(var9, var1, var2, var3, var4, false);
               }
            }
         }
      }

   }

   public NodeCache getItemCompositionCache() {
      return this.bv();
   }

   public static void b(int var0, int var1) {
      if (aa() != 0 && var0 != -1) {
         ArrayList var2 = new ArrayList();
         var2.add(new eX(osrs.bo.bj, var0, 0, aa(), false));
         osrs.eZ.a(var2, 0, 0, 0, 0, true);
         bH = true;
      }

   }

   public static final void s() {
      if (osrs.bo.aY) {
         if (osrs.bo.co != null) {
            osrs.bo.co.i();
         }

         Iterator var0 = D.iterator();

         while(var0.hasNext()) {
            bG var1 = (bG)var0.next();

            for(int var2 = 0; var2 < G.j; ++var2) {
               t var3 = (t)osrs.bo.aQ.r.a((long)G.a[var2]);
               if (var3 != null) {
                  var3.M();
               }
            }
         }

         osrs.bo.aY = false;
      }

   }

   public static final void t() {
      if (osrs.bo.cy) {
         Iterator var0 = D.iterator();

         while(var0.hasNext()) {
            bG var1 = (bG)var0.next();

            for(int var2 = 0; var2 < G.j; ++var2) {
               t var3 = (t)osrs.bo.aQ.r.a((long)G.a[var2]);
               if (var3 != null) {
                  var3.Q();
               }
            }
         }

         osrs.bo.cy = false;
      }

   }

   public static void g(int var0) {
      if (!dt) {
         osrs.bo.fg = cl;
      } else {
         int var1;
         if (H != 0) {
            var1 = 0;
         } else {
            int var2 = osrs.bo.dm >> 7;
            int var3 = osrs.bo.o >> 7;
            bG var4 = osrs.bo.aQ;
            int var5 = var4.getPlane();
            int var6 = var4.getTileHeight(osrs.bo.dm, osrs.bo.o, var5);
            int var7 = 0;
            if (var2 > 3 && var3 > 3 && var2 < 100 && var3 < 100) {
               for(int var8 = var2 - 4; var8 <= var2 + 4; ++var8) {
                  for(int var9 = var3 - 4; var9 <= var3 + 4; ++var9) {
                     int var10 = var5;
                     if (var5 < 3 && (var4.v[1][var8][var9] & 2) == 2) {
                        var10 = var5 + 1;
                     }

                     int var11 = var6 - var4.u[var10][var8][var9];
                     if (var11 > var7) {
                        var7 = var11;
                     }
                  }
               }
            }

            var1 = var7 * 128;
            if (var1 > 98048) {
               var1 = 98048;
            }

            if (var1 < 0) {
               var1 = 0;
            }
         }

         if (var1 > osrs.bo.fg) {
            osrs.bo.fg += (var1 - osrs.bo.fg) / 24;
         } else if (var1 < osrs.bo.fg) {
            osrs.bo.fg += (var1 - osrs.bo.fg) / 80;
         }

         cl = osrs.bo.fg;
      }

   }

   public static void h(int var0) {
      r var1 = osrs.r.a(osrs.u.Y, b.p);
      var1.f.o(var0);
      b.a(var1);
   }

   public bt a(int var1, int var2, int var3) {
      if (!s.isClientThread()) {
         if (!osrs.T.eo) {
            throw new AssertionError("must be called on client thread");
         } else {
            throw new IllegalStateException("must be called on client thread");
         }
      } else {
         ag var4 = this.Q(var1);
         if (var4 == null) {
            throw new IllegalStateException("component does not exist");
         } else if (var4.getType() != 0) {
            throw new IllegalStateException("component is not a layer");
         } else {
            fT var5 = osrs.bo.cR.x;
            bt var6 = (bt)var5.b((long)var1);
            if (var6 != null) {
               this.closeInterface(var6, var6.c != var2);
            }

            Iterator var7 = var5.iterator();

            while(var7.hasNext()) {
               bt var8 = (bt)var7.next();
               if (var8.c == var2) {
                  throw new IllegalStateException("interface " + var2 + " is already open");
               }
            }

            bt var9 = new bt();
            var9.c = var2;
            var9.b = var3;
            var5.b(var9, (long)var1);
            kd var10 = osrs.bo.cR;
            ((fa)var10).d(var2);
            a(var10.m[var1 >> 16], var4, false, osrs.bo.cR, o);
            osrs.bo.cv.d(var2, osrs.bo.cR);
            int var11 = this.getTopLevelInterfaceId();
            if (var11 != -1 && ((fa)var10).d(var11)) {
               osrs.bo.cv.b(var10.m[var11], jG.b, osrs.bo.cR);
            }

            return var9;
         }
      }
   }

   public static final void b(String var0) {
      if (var0.equalsIgnoreCase("toggleroof")) {
         bL.b(!bL.c());
         if (bL.c()) {
            iw.a(99, "", "Roofs are now all hidden");
         } else {
            iw.a(99, "", "Roofs will only be removed selectively");
         }
      }

      if (var0.startsWith("zbuf")) {
         boolean var1 = osrs.br.c(var0.substring(5).trim()) == 1;
         s.a(var1, (byte)0);
         osrs.aW.a(var1);
      }

      if (var0.equalsIgnoreCase("z")) {
         bb = !bb;
      }

      if (var0.equalsIgnoreCase("displayfps")) {
         bL.f();
      }

      if (var0.equalsIgnoreCase("renderself")) {
         W = !W;
      }

      if (var0.equalsIgnoreCase("mouseovertext")) {
         ad = !ad;
      }

      int var2;
      String[] var7;
      if (var0.startsWith("setdrawdistance")) {
         var7 = var0.split(" ");

         try {
            var2 = Integer.parseInt(var7[1]);
            bL.g(var2);
         } catch (NumberFormatException var6) {
            iw.a(99, "", String.format("Error setting draw distance. setdrawdistance should be in the format \"::setdrawdistance X\" where X is a valid number. Value provided: %s", var7[1]));
         }
      }

      if (var0.startsWith("settilerendermode")) {
         var7 = var0.split(" ");

         try {
            var2 = Integer.parseInt(var7[1]);
            osrs.bo.aQ.x.a(osrs.dn.a()[var2]);
         } catch (NumberFormatException var5) {
            iw.a(99, "", String.format("Error setting tile render mode. settilerendermode should be in the format \"::settilerendermode X\" where X is a valid number from 0-1. 0=camera 1=target Value provided: %s", var7[1]));
         }
      }

      if (var0.equalsIgnoreCase("getdrawdistance")) {
         iw.a(99, "", String.format("%d", bL.p()));
      }

      if (by >= 2) {
         if (var0.equalsIgnoreCase("errortest")) {
            throw new RuntimeException();
         }

         if (var0.equalsIgnoreCase("showcoord")) {
            dU.ad = !dU.ad;
         }

         if (var0.equalsIgnoreCase("fpson")) {
            bL.e(true);
         }

         if (var0.equalsIgnoreCase("fpsoff")) {
            bL.e(false);
         }

         if (var0.equalsIgnoreCase("gc")) {
            System.gc();
         }

         if (var0.equalsIgnoreCase("clientdrop")) {
            r();
         }

         if (var0.equalsIgnoreCase("clientreload")) {
            osrs.bo.k();
         } else if (var0.equalsIgnoreCase("sfx8bit")) {
            bL.f(true);
         } else if (var0.equalsIgnoreCase("sfx16bit")) {
            bL.f(false);
         } else if (var0.startsWith("playsong ")) {
            String var8 = var0.substring("playsong ".length());

            try {
               if (aa() == 0) {
                  osrs.eZ.a(0, 0);
               } else {
                  var2 = aa();
                  osrs.eZ.a(0, 0);
                  ArrayList var3 = new ArrayList();
                  var3.add(new eX(osrs.bo.cL, var8, "", var2, false));
                  osrs.eZ.a(var3, 0, 0, 0, 100, false);
               }
            } catch (Exception var4) {
               iw.a(99, "", "Can't play the midi named " + var8);
            }
         } else if (var0.equalsIgnoreCase("stopsong")) {
            osrs.eZ.a(0, 0);
         }

         if (var0.equalsIgnoreCase("freecam")) {
            D(1);
         }

         if (var0.startsWith("unlockcam")) {
            var7 = var0.split(" ");
            I = Boolean.parseBoolean(var7[1]);
         }

         if (var0.equalsIgnoreCase("show_test_ops")) {
            m = true;
         }

         if (var0.equalsIgnoreCase("hide_test_ops")) {
            m = false;
         }
      }

      r var9 = osrs.r.a(osrs.u.ab, b.p);
      var9.f.a(var0.length() + 1);
      var9.f.c(var0);
      b.a(var9);
   }

   public static final int i(int var0) {
      return Math.min(Math.max(var0, 128), 383);
   }

   public MenuEntry createMenuEntry(int var1) {
      return this.R(var1);
   }

   public static final int j(int var0) {
      return Math.abs(var0 - osrs.bo.cV) > 1024 ? 2048 * (var0 < osrs.bo.cV ? 1 : -1) + var0 : var0;
   }

   public static final void b(int var0, int var1, int var2) {
      if (osrs.bo.bM < var0) {
         osrs.bo.bM += osrs.bo.aH * (var0 - osrs.bo.bM) / 1000 + osrs.bo.bG;
         if (osrs.bo.bM > var0) {
            osrs.bo.bM = var0;
         }
      }

      if (osrs.bo.bM > var0) {
         osrs.bo.bM -= osrs.bo.aH * (osrs.bo.bM - var0) / 1000 + osrs.bo.bG;
         if (osrs.bo.bM < var0) {
            osrs.bo.bM = var0;
         }
      }

      if (osrs.bo.bD < var1) {
         osrs.bo.bD += osrs.bo.aH * (var1 - osrs.bo.bD) / 1000 + osrs.bo.bG;
         if (osrs.bo.bD > var1) {
            osrs.bo.bD = var1;
         }
      }

      if (osrs.bo.bD > var1) {
         osrs.bo.bD -= osrs.bo.aH * (osrs.bo.bD - var1) / 1000 + osrs.bo.bG;
         if (osrs.bo.bD < var1) {
            osrs.bo.bD = var1;
         }
      }

      if (osrs.bo.dO < var2) {
         osrs.bo.dO += osrs.bo.aH * (var2 - osrs.bo.dO) / 1000 + osrs.bo.bG;
         if (osrs.bo.dO > var2) {
            osrs.bo.dO = var2;
         }
      }

      if (osrs.bo.dO > var2) {
         osrs.bo.dO -= osrs.bo.aH * (osrs.bo.dO - var2) / 1000 + osrs.bo.bG;
         if (osrs.bo.dO < var2) {
            osrs.bo.dO = var2;
         }
      }

   }

   public static final void c(int var0, int var1) {
      if (var0 < 128) {
         var0 = 128;
      } else if (var0 > 383) {
         var0 = 383;
      }

      if (osrs.bo.bV < var0) {
         osrs.bo.bV += osrs.bo.X * (var0 - osrs.bo.bV) / 1000 + osrs.bo.v;
         if (osrs.bo.bV > var0) {
            osrs.bo.bV = var0;
         }
      } else if (osrs.bo.bV > var0) {
         osrs.bo.bV -= osrs.bo.X * (osrs.bo.bV - var0) / 1000 + osrs.bo.v;
         if (osrs.bo.bV < var0) {
            osrs.bo.bV = var0;
         }
      }

      int var2 = var1 & 2047;
      int var3 = var2 - osrs.bo.cV;
      if (var3 > 1024) {
         var3 -= 2048;
      } else if (var3 < -1024) {
         var3 += 2048;
      }

      if (var3 > 0) {
         osrs.bo.cV += osrs.bo.X * var3 / 1000 + osrs.bo.v;
         osrs.bo.cV &= 2047;
      } else if (var3 < 0) {
         osrs.bo.cV -= osrs.bo.X * -var3 / 1000 + osrs.bo.v;
         osrs.bo.cV &= 2047;
      }

      int var4 = var2 - osrs.bo.cV;
      if (var4 > 1024) {
         var4 -= 2048;
      } else if (var4 < -1024) {
         var4 += 2048;
      }

      if (var4 < 0 && var3 > 0 || var4 > 0 && var3 < 0) {
         osrs.bo.cV = var2;
      }

   }

   public eI u() {
      if (!s.isClientThread()) {
         if (!osrs.T.eo) {
            throw new AssertionError("must be called on client thread");
         } else {
            throw new IllegalStateException("must be called on client thread");
         }
      } else {
         return osrs.bs.a;
      }
   }

   public M k(int var1) {
      if (!s.isClientThread()) {
         if (!osrs.T.eo) {
            throw new AssertionError("must be called on client thread");
         } else {
            throw new IllegalStateException("must be called on client thread");
         }
      } else {
         return osrs.M.a(var1);
      }
   }

   public aO l(int var1) {
      if (!s.isClientThread()) {
         if (!osrs.T.eo) {
            throw new AssertionError("must be called on client thread");
         } else {
            throw new IllegalStateException("must be called on client thread");
         }
      } else {
         return osrs.aO.a(var1);
      }
   }

   public bs m(int var1) {
      if (!s.isClientThread()) {
         if (!osrs.T.eo) {
            throw new AssertionError("must be called on client thread");
         } else {
            throw new IllegalStateException("must be called on client thread");
         }
      } else {
         return osrs.bs.a(var1);
      }
   }

   public C n(int var1) {
      if (!s.isClientThread()) {
         if (!osrs.T.eo) {
            throw new AssertionError("must be called on client thread");
         } else {
            throw new IllegalStateException("must be called on client thread");
         }
      } else {
         return osrs.C.a(var1);
      }
   }

   public aN o(int var1) {
      if (!s.isClientThread()) {
         if (!osrs.T.eo) {
            throw new AssertionError("must be called on client thread");
         } else {
            throw new IllegalStateException("must be called on client thread");
         }
      } else {
         return osrs.aN.a(var1);
      }
   }

   public void p(int var1) {
      if (!s.isClientThread()) {
         if (!osrs.T.eo) {
            throw new AssertionError("must be called on client thread");
         } else {
            throw new IllegalStateException("must be called on client thread");
         }
      } else {
         a(var1);
      }
   }

   public aT q(int var1) {
      if (!s.isClientThread()) {
         if (!osrs.T.eo) {
            throw new AssertionError("must be called on client thread");
         } else {
            throw new IllegalStateException("must be called on client thread");
         }
      } else {
         return osrs.aT.a(var1);
      }
   }

   public aV a(int var1, int var2, int var3, int var4, int var5, boolean var6, int var7, int var8) {
      if (!s.isClientThread()) {
         if (!osrs.T.eo) {
            throw new AssertionError("must be called on client thread");
         } else {
            throw new IllegalStateException("must be called on client thread");
         }
      } else {
         return osrs.aO.a(var1, var2, var3, var4, var5, var6, var7, var8);
      }
   }

   public static void a(bG var0, float var1) {
      Iterator var2 = var0.players().iterator();

      while(var2.hasNext()) {
         t var3 = (t)var2.next();
         if (var3 != null) {
            osrs.o.a(var3, var1);
         }
      }

      Iterator var9 = var0.npcs().iterator();

      while(var9.hasNext()) {
         s var4 = (s)var9.next();
         if (var4 != null) {
            osrs.o.a(var4, var1);
         }
      }

      Iterator var10 = var0.worldEntities().iterator();

      while(var10.hasNext()) {
         gZ var5 = (gZ)var10.next();
         if (var5 != null) {
            dh var6 = var5.n.x;
            var6.A = -1;
            dc var7 = (dc)var6.bz;
            var7.c.j();
            dc var8 = var6.aL;
            var8.c.j();
            var5.d(x);
            a(var5.n, var1);
         }
      }

   }

   public static void v() {
      U = P;
      osrs.bo.E = osrs.fh.a;
      T = 0;
   }

   public static bE a(bG var0, int var1, int var2) {
      if (var0 != null && osrs.bo.aQ != var0) {
         gZ var3 = (gZ)osrs.bo.aQ.t.a((long)var0.o);
         return var3 == null ? osrs.bE.a((float)var1, 0.0F, (float)var2) : var3.b(var1, var2);
      } else {
         return osrs.bE.a((float)var1, 0.0F, (float)var2);
      }
   }

   public static boolean w() {
      if (!cI) {
         return false;
      } else {
         if (osrs.bo.fC > 0 && cJ.a > -1) {
            ig var0 = cJ.q[cJ.a];
            if (var0 != null) {
               boolean var1 = osrs.aI.h >= var0.w - 10 && osrs.aI.h <= var0.b + var0.w + 10 && osrs.aI.i >= var0.c - 10 && osrs.aI.i <= var0.v + var0.c + 10;
               if (var1) {
                  return true;
               }
            }
         }

         boolean var2 = osrs.aI.h >= cJ.w - 10 && osrs.aI.h <= cJ.b + cJ.w + 10 && osrs.aI.i >= cJ.c - 10 && osrs.aI.i <= cJ.v + cJ.c + 10;
         return var2 && osrs.bo.fc > 0;
      }
   }

   public List getActiveMidiRequests() {
      return Collections.unmodifiableList(osrs.eZ.d);
   }

   public static void a(ig var0, int var1, int var2) {
      // $FF: Couldn't be decompiled
   }

   public bk r(int var1) {
      if (!s.isClientThread()) {
         if (!osrs.T.eo) {
            throw new AssertionError("must be called on client thread");
         } else {
            throw new IllegalStateException("must be called on client thread");
         }
      } else {
         bk var2 = s.ar(var1);
         return var2.m == null && !var2.isMayaAnim() ? null : var2;
      }
   }

   public static final void x() {
      int var0 = G.j;
      int[] var1 = G.a;
      Iterator var2 = D.iterator();

      while(var2.hasNext()) {
         bG var3 = (bG)var2.next();

         for(int var4 = 0; var4 < var0; ++var4) {
            t var5 = (t)var3.r.a((long)var1[var4]);
            if (var5 != null) {
               a(var3, (o)var5, 1);
            }
         }
      }

   }

   public static void s(int var0) {
      if (osrs.bo.ep != null) {
         osrs.bo.ep.i = cJ.e[cJ.h - 1].c;
      }

   }

   public static final void y() {
      Iterator var0 = D.iterator();

      while(var0.hasNext()) {
         bG var1 = (bG)var0.next();
         Iterator var2 = var1.s.iterator();

         while(var2.hasNext()) {
            s var3 = (s)var2.next();
            if (var3 != null) {
               a(var1, (o)var3, var3.aw.j);
            }
         }
      }

   }

   public static boolean a(aC var0, int var1, int var2) {
      int var3 = var0.getMapSceneId();
      if (var3 == -1) {
         return false;
      } else {
         hX var4 = osrs.bo.bq[var3];
         if (var4 != null && var4.getWidth() > 0 && var4.getHeight() > 0) {
            int var5 = var0.k() * 4;
            int var6 = var0.m() * 4;
            int var7 = var1 * 4 + (var5 - var4.getWidth()) / 2;
            int var8 = var2 * 4 + (var6 - var4.getHeight()) / 2;
            int var9 = var7 + var4.getWidth();
            int var10 = var8 + var4.getHeight();
            int var11 = ab(var7);
            int var12 = J(var8);
            int var13 = ab(var9);
            int var14 = J(var10);
            int var15 = (var13 - var11) * var4.getOriginalWidth() / var4.getWidth();
            int var16 = (var12 - var14) * var4.getOriginalHeight() / var4.getHeight();
            if (var15 > 0 && var16 > 0) {
               var4.g(var11, var14, var15, var16);
            }

            return true;
         } else {
            return true;
         }
      }
   }

   public static void a(ig var0, jd var1, int var2, int var3) {
      int var4 = var0.w;
      int var5 = var0.c;
      int var6 = var0.b;
      int var7 = var0.v;
      osrs.df.i(var4 + 2, var5, var6 - 4, 7170651, var2);
      osrs.df.i(var4 + 2, var5 + var7 - 1, var6 - 4, 7170651, var2);
      osrs.df.n(var4, var5 + 2, var7 - 4, 7170651, var2);
      osrs.df.n(var4 + var6 - 1, var5 + 2, var7 - 4, 7170651, var2);
      osrs.df.n(var4 + 1, var5 + 5, var7 - 6, 2827810, var2);
      osrs.df.n(var4 + var6 - 2, var5 + 5, var7 - 6, 2827810, var2);
      osrs.df.i(var4 + 1, var5 + 17, var6 - 2, 2827810, var2);
      osrs.df.i(var4 + 1, var5 + var7 - 2, var6 - 2, 2827810, var2);
      osrs.df.j(var4 + 2, var5 + var7 - 3, 0, 2827810, var2);
      osrs.df.j(var4 + var6 - 3, var5 + var7 - 3, 0, 2827810, var2);
      osrs.df.b(var4 + 2, var5 + 1, var6 - 4, 16, 3288610, 592388, var2, var2);
      osrs.df.e(var4 + 1, var5 + 1, 2, 4, 2827810, var2);
      osrs.df.e(var4 + var6 - 3, var5 + 1, 2, 4, 2827810, var2);
      osrs.df.i(var4 + 2, var5 + 18, var6 - 4, 5392957, var2);
      osrs.df.i(var4 + 3, var5 + var7 - 3, var6 - 6, 5392957, var2);
      osrs.df.n(var4 + 2, var5 + 18, var7 - 21, 5392957, var2);
      osrs.df.n(var4 + var6 - 3, var5 + 18, var7 - 21, 5392957, var2);
      osrs.df.e(var4 + 3, var5 + 19, var6 - 6, var7 - 22, 2828060, var2);
      osrs.df.j(var4 + 1, var5 + 1, 0, 7170651, var2);
      osrs.df.j(var4 + var6 - 2, var5 + 1, 0, 7170651, var2);
      osrs.df.j(var4 + 1, var5 + var7 - 2, 0, 7170651, var2);
      osrs.df.j(var4 + var6 - 2, var5 + var7 - 2, 0, 7170651, var2);
      String var8 = var1 == null ? "Choose Option" : var1.getTarget();
      osrs.bo.ck.d(var8, var4 + 3, var5 + 14, 13023381, -1);
      int var9 = osrs.aI.h;
      int var10 = osrs.aI.i;

      for(int var11 = 0; var11 < var0.h; ++var11) {
         if (var0.h - 1 - var11 >= var3) {
            int var12 = (var0.h - 1 - var11 - var3) * 15 + var5 + 31;
            String var13 = var0.o[var11];
            if (!var0.p[var11].isEmpty()) {
               if (!var13.isEmpty()) {
                  var13 = var13 + " ";
               }

               var13 = var13 + var0.p[var11];
            }

            if (var0.q[var11] != null) {
               var13 = var13 + " <col=ffffff><gt>";
            }

            osrs.bo.ck.d(var13, var4 + 3, var12, 13023381, -1);
            if (var9 > var4 && var9 < var4 + var6 && var10 > var12 - 13 && var10 < var12 + 3) {
               osrs.df.e(var4 + 3, var12 - 12, var6 - 6, 15, 16777215, 80);
            }

            if (var0.a == var11 && var0.q[var11] != null && var0.q[var11].h > 0) {
               a(var0.q[var11], var0.e[var11], var2, osrs.bo.fs);
            }
         }
      }

   }

   public CameraFocusableEntity getCameraFocusEntity() {
      return this.f();
   }

   public static final void a(bG var0, o var1, int var2) {
      osrs.o.b(var0, var1, var2);
      if (var1.Z != -1) {
         Object var3 = null;
         Object var4 = null;
         int var5 = 65536;
         bG var6;
         o var7;
         if (var1.Z < var5) {
            var6 = jQ.a(var1.Z, D);
            var7 = (o)var6.s.a((long)var1.Z);
         } else {
            int var8 = var1.Z - var5;
            io var9 = D;
            Iterator var10 = var9.iterator();

            bG var11;
            while(true) {
               if (var10.hasNext()) {
                  bG var12 = (bG)var10.next();
                  t var13 = (t)var12.r.a((long)var8);
                  if (var13 == null || var12.a()) {
                     continue;
                  }

                  var11 = var12;
                  break;
               }

               var11 = var9.a();
               break;
            }

            var6 = var11;
            var7 = (o)var11.r.a((long)var8);
         }

         if (var7 != null) {
            bE var19 = osrs.bE.a((float)var1.t, 0.0F, (float)var1.ai);
            bE var20;
            if (var0.o != var6.o) {
               gZ var21;
               if (var0.o == 0) {
                  var21 = (gZ)osrs.bo.aQ.t.a((long)var6.o);
                  var20 = var21.b(var7.t, var7.ai);
               } else if (var6.o == 0) {
                  var21 = (gZ)osrs.bo.aQ.t.a((long)var0.o);
                  var20 = var21.c(var7.t, var7.ai);
               } else {
                  var21 = (gZ)osrs.bo.aQ.t.a((long)var6.o);
                  gZ var23 = (gZ)osrs.bo.aQ.t.a((long)var0.o);
                  bE var25 = var21.b(var7.t, var7.ai);
                  var20 = var23.c((int)var25.h, (int)var25.j);
                  var25.a();
               }
            } else {
               var20 = osrs.bE.a((float)var7.t, 0.0F, (float)var7.ai);
            }

            int var22 = (int)(var19.h - var20.h);
            int var24 = (int)(var19.j - var20.j);
            if (var22 != 0 || var24 != 0) {
               var1.x = osrs.A.a(var22, var24);
            }

            var19.a();
            var20.a();
         } else if (var1.f) {
            var1.Z = -1;
            var1.f(-1);
            var1.f = false;
         }
      }

      int var14;
      if (var1.T == 0 || var1.m > 0) {
         var14 = -1;
         if (var1.v != -1) {
            var14 = var1.v;
         }

         if (var14 != -1) {
            var1.x = var14;
            if (var1.h) {
               var1.ac = var1.x;
            }
         }

         var1.j();
      }

      var14 = var1.x - var1.ac & 2047;
      if (var14 != 0) {
         boolean var15 = true;
         boolean var16 = true;
         ++var1.D;
         int var17 = var14 > 1024 ? -1 : 1;
         var1.ac += var1.B * var17;
         boolean var18 = true;
         if (var14 < var1.B || var14 > 2048 - var1.B) {
            var1.ac = var1.x;
            var18 = false;
         }

         if (var1.B > 0 && var1.a() == var1.W && (var1.D > 25 || var18)) {
            if (var17 == -1 && var1.g != -1) {
               var1.a(var1.g);
            } else if (var17 == 1 && var1.I != -1) {
               var1.a(var1.I);
            } else {
               var1.a(var1.z);
            }
         }

         var1.ac &= 2047;
      } else {
         if (var1.f) {
            var1.Z = -1;
            var1.f(-1);
            var1.f = false;
         }

         var1.D = 0;
      }

      a(var0, var1);
      osrs.o.a(var0, var1, var2);
   }

   public static final void a(bG var0, o var1) {
      osrs.bo.cJ.a(var0, var1.t, var1.ai, P == var1.j);
      int var2 = jM.a(var1.O, 1, osrs.bo.cJ);
      if ((var2 & 2) != 0) {
         var1.O.h();
      }

      var1.N.a(var1.W);
      if (var1.O.j() > 0 && var1.N.j() < 30) {
         if (var1.O.d() == var1.N.d()) {
            var1.N.a(var1.O);
         } else {
            int var3 = jM.a(var1.N, 1, (gw)null);
            if ((var3 & 2) != 0) {
               var1.N.h();
            }
         }
      }

      fN var7 = new fN(var1.g());

      for(gG var4 = (gG)var7.b(); var4 != null; var4 = (gG)var7.next()) {
         if (var4.a != -1 && x >= var4.c) {
            gq var5 = var4.b;
            if (!var5.b()) {
               var4.X();
               --var1.C;
            } else {
               int var6 = jM.a(var5, 1, osrs.bo.cJ);
               if ((var6 & 1) != 0) {
                  var4.X();
                  --var1.C;
               }
            }
         }
      }

      if (var1.P.b() && var1.V <= 1) {
         bk var8 = var1.P.c();
         if (var8.z == 1 && var1.l > 0 && var1.ak <= x && var1.n < x) {
            var1.V = 1;
            return;
         }
      }

      if (var1.V > 0) {
         --var1.V;
      } else {
         int var9 = jM.a(var1.P, 1, osrs.bo.cJ);
         if ((var9 & 2) != 0) {
            var1.P.a();
            if (var1.O.d() == var1.W && var1.s()) {
               var1.O.h();
            }
         }
      }

      var1.a = var1.P.b() && var1.P.c().D;
      osrs.bo.cJ.a();
   }

   public static void z() {
      if (dU != null) {
         dU.a(osrs.bo.aQ.y, (cy >> 7) + osrs.bo.aQ.B, (cA >> 7) + osrs.bo.aQ.z, false);
         dU.j();
      }

   }

   public static void a(t var0, int var1, int var2) {
      if (var0 != null) {
         bk var3 = var0.P.c();
         if (var1 == var0.P.d() && var1 != -1) {
            int var4 = var3.B;
            if (var4 == 1) {
               var0.P.h();
               var0.V = var2;
            }

            if (var4 == 2) {
               var0.P.i();
            }
         } else if (var1 == -1 || !var0.P.b() || osrs.bk.b(var1).v >= var3.v) {
            var0.P.a(var1);
            var0.P.h();
            var0.V = var2;
            var0.l = var0.T;
         }
      }

   }

   public static void t(int var0) {
      ai = 0L;
      if (var0 >= 2) {
         aj = true;
         M(-1);
      } else {
         aj = false;
         M(-1);
      }

      if (osrs.bo.p() == 1) {
         s.p(765, 503);
      } else {
         s.p(7680, 2160);
      }

      if (w >= 25 && b != null && b.p != null) {
         r var1 = osrs.r.a(osrs.u.bk, b.p);
         var1.f.a(osrs.bo.p());
         var1.f.b(osrs.T.eP);
         var1.f.b(osrs.T.eQ);
         b.a(var1);
      }

   }

   public static void A() {
      if (b != null && b.p != null) {
         r var0 = osrs.r.a(osrs.u.bk, b.p);
         var0.f.a(osrs.bo.p());
         var0.f.b(osrs.T.eP);
         var0.f.b(osrs.T.eQ);
         b.a(var0);
      }

   }

   public static final void a(String var0, boolean var1) {
      if (O) {
         byte var2 = 4;
         int var3 = var2 + 6;
         int var4 = var2 + 6;
         int var5 = osrs.bo.L.a(var0, 250);
         int var6 = osrs.bo.L.b(var0, 250) * 13;
         osrs.aU.b(var3 - var2, var4 - var2, var2 + var5 + var2, var2 + var6 + var2, 0);
         osrs.aU.c(var3 - var2, var4 - var2, var2 + var5 + var2, var2 + var6 + var2, 16777215);
         osrs.bo.L.a(var0, var3, var4, var5, var6, 16777215, -1, 1, 1, 0);
         if (var1) {
            osrs.T.fa.a(0, 0, (byte)17);
         }
      }

   }

   public static final void B() {
      osrs.bo.aQ.x.l();

      for(int var0 = 0; var0 < osrs.bo.aQ.e.a(); ++var0) {
         gZ var1 = (gZ)osrs.bo.aQ.t.a((long)osrs.bo.aQ.e.b(var0));
         if (var1 != null) {
            var1.n.x.l();
         }
      }

   }

   public static void a(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7) {
      int var8 = osrs.aU.f;
      int[] var9 = osrs.aU.h;
      int var10 = Math.max(var4, 0);
      int var11 = Math.min(var4 + var6, var8);
      int var12 = Math.max(var5, 0);
      int var13 = Math.min(var5 + var7, osrs.aU.e);
      int var14 = var3 == 0 ? 1 : 0;
      int var15 = var2 - var3;
      if (var0 <= 1) {
         if (var14 != 1 || var0 != 0) {
            int var16 = var0 * var15 + var3;

            for(int var17 = var12; var17 < var13; ++var17) {
               int var18 = var8 * var17;

               for(int var19 = var10; var19 < var11; ++var19) {
                  var9[var18 + var19] = var16;
               }
            }
         }
      } else {
         long var38 = osrs.bo.eV[var0];
         long var39 = 1158011251845644416L;
         int var20 = (int)(1158011251845644416L >> (var6 - 1) * 8) & 255;
         int var21 = (int)(1158011251845644416L >> (var7 - 1) * 8) & 255;
         int var22 = var4 * 8 - 4;
         int var23 = var5 * 8 - 4;
         int var24 = (int)(608392186895073544L >> (var1 << 4));

         for(int var25 = var12; var25 < var13; ++var25) {
            int var26 = var24 >> 4 & 15;
            int var27 = var24 & 15;
            int var28 = ((var25 * 8 - var23) * var21 >> 7 ^ var26) * var27;
            int var29 = var8 * var25;

            for(int var30 = var10; var30 < var11; ++var30) {
               int var31 = var24 >> 12 & 15;
               int var32 = var24 >> 8 & 15;
               int var33 = ((var30 * 8 - var22) * var20 >> 7 ^ var31) * var32;
               int var34 = (int)(var38 >> var28 + var33) & 1;
               int var35 = var15 * var34 + var3;
               int var36 = ~var34 & var14;
               int var37 = var35 | var9[var29 + var30] * var36;
               var9[var29 + var30] = var37;
            }
         }
      }

   }

   public static void C() {
      t var0 = J();
      if (var0 == null || var0.t >> 7 == cV && var0.ai >> 7 == cW) {
         cV = 0;
         cW = 0;
      }

   }

   public static void a(bG var0, int var1, aH var2) {
      float var3 = (float)aC / 334.0F;
      float var4 = (float)aD * 4.0F / var3;
      int var5 = Math.max(512, (int)(1400.0F - var4));
      int var6 = cy;
      double var7 = (double)hg.b(var1) / 65536.0;
      int var9 = var6 - (int)((double)var5 * var7);
      int var10 = cA - (int)(hg.d(var1) * (double)var5);
      long var11 = osrs.dB.a(var0.y, osrs.bo.e(var9), osrs.bo.e(var10), 5, false, -1, var0.o);
      var0.x.a(var0.y, var9, var10, cz, 60, var2, var1, var11, false);
   }

   public static void b(jc var0) {
      bG var1 = var0.o;
      if (!a && !var1.isTopLevel()) {
         throw new AssertionError();
      } else {
         dr = var0.e;
         var1.i = var0.e.b;
         var1.j = var0.e.a;
         var1.C = var0.e.c;
         var1.D = var0.e.d;
         osrs.bo.aZ = var0.n;
         osrs.bo.eq = var0.J;
         s.p(25);
         bO = true;
         int var2 = var0.H - var1.B;
         int var3 = var0.l - var1.z;
         var1.B = var0.H;
         var1.z = var0.l;
         a(cJ, var2, var3);
         a(var1.x, var2, var3);
         var1.k.clear();
         Iterator var4 = var1.s.iterator();

         while(true) {
            o var5;
            int[] var6;
            do {
               if (!var4.hasNext()) {
                  Iterator var26 = var1.r.iterator();

                  while(true) {
                     o var8;
                     do {
                        if (!var26.hasNext()) {
                           Iterator var27 = var1.t.iterator();

                           while(var27.hasNext()) {
                              gZ var10 = (gZ)var27.next();
                              if (var10 != null) {
                                 var10.e(-var2 << 7, -var3 << 7);
                              }
                           }

                           byte var28 = 0;
                           byte var11 = 104;
                           byte var12 = 1;
                           if (var2 < 0) {
                              var28 = 103;
                              var11 = -1;
                              var12 = -1;
                           }

                           byte var13 = 0;
                           byte var14 = 104;
                           byte var15 = 1;
                           if (var3 < 0) {
                              var13 = 103;
                              var14 = -1;
                              var15 = -1;
                           }

                           fJ[][][] var16 = var1.a;

                           int var18;
                           int var19;
                           int var20;
                           int var21;
                           for(int var17 = var28; var11 != var17; var17 += var12) {
                              for(var18 = var13; var14 != var18; var18 += var15) {
                                 var19 = var2 + var17;
                                 var20 = var3 + var18;

                                 for(var21 = 0; var21 < 4; ++var21) {
                                    if (var19 >= 0 && var20 >= 0 && var19 < 104 && var20 < 104) {
                                       var16[var21][var17][var18] = var16[var21][var19][var20];
                                    } else {
                                       var16[var21][var17][var18] = null;
                                    }
                                 }
                              }
                           }

                           for(ic var29 = (ic)var1.b.j(); var29 != null; var29 = (ic)var1.b.h()) {
                              var29.h -= var2;
                              var29.i -= var3;
                              if (var29.h < 0 || var29.i < 0 || var29.h >= 104 || var29.i >= 104) {
                                 var29.ad();
                              }
                           }

                           for(dQ var30 = (dQ)var1.c.j(); var30 != null; var30 = (dQ)var1.c.h()) {
                              var18 = var30.f();
                              var19 = var30.g();
                              var20 = var18 - (var2 << 7);
                              var21 = var19 - (var3 << 7);
                              var30.c(var20);
                              var30.b(var21);
                              if (var20 < 0 || var21 < 0 || var20 >= 13312 || var21 >= 13312) {
                                 var30.ad();
                              }
                           }

                           if (cV != 0) {
                              cV -= var2;
                              cW -= var3;
                           }

                           for(in var31 = (in)F.j(); var31 != null; var31 = (in)F.h()) {
                              var31.b(-var2, -var3);
                           }

                           hm var32 = osrs.bo.bw;

                           int var22;
                           int var23;
                           int var24;
                           for(var18 = 0; var18 < var32.b; ++var18) {
                              hq var33 = var32.c[var18];
                              var20 = var33.e;
                              if (var20 != 0) {
                                 var21 = var20 >> 16 & 255;
                                 var22 = var20 >> 8 & 255;
                                 var23 = var20 & 255;
                                 var24 = var21 - var2;
                                 int var25 = var22 - var3;
                                 if (var24 >= 0 && var25 >= 0 && var24 < 104 && var25 < 104) {
                                    var33.e = var23 | var25 << 8 | var24 << 16;
                                 } else {
                                    var32.c(var18);
                                    --var18;
                                 }
                              }
                           }

                           bJ = false;
                           osrs.bo.bM -= var2 << 7;
                           osrs.bo.dO -= var3 << 7;
                           osrs.bo.fx -= (double)(var2 << 7);
                           osrs.bo.h -= (double)(var3 << 7);
                           osrs.bo.dm -= var2 << 7;
                           osrs.bo.o -= var3 << 7;
                           osrs.bo.fq -= (double)(var2 << 7);
                           osrs.bo.eO -= (double)(var3 << 7);
                           cy -= var2 << 7;
                           cA -= var3 << 7;
                           osrs.eM.h = 1;

                           for(var18 = 0; var18 < 4; ++var18) {
                              z var34 = var1.n[var18];
                              int[][] var35 = var34.e;

                              for(var21 = var28; var11 != var21; var21 += var12) {
                                 for(var22 = var13; var14 != var22; var22 += var15) {
                                    var23 = var2 + var21;
                                    var24 = var3 + var22;
                                    if (var21 != 0 && var22 != 0 && var21 < var34.a - 5 && var22 < var34.b - 5) {
                                       if (var23 >= 0 && var24 >= 0 && var23 < var34.a && var24 < var34.b) {
                                          var35[var21][var22] = var35[var23][var24];
                                       } else {
                                          var35[var21][var22] = 1073741824;
                                       }
                                    } else {
                                       var35[var21][var22] = 16777215;
                                    }
                                 }
                              }
                           }

                           return;
                        }

                        var8 = (o)var26.next();
                     } while(var8 == null);

                     for(int var9 = 0; var9 < 10; ++var9) {
                        var6 = var8.af;
                        var6[var9] -= var2;
                        var6 = var8.ag;
                        var6[var9] -= var3;
                     }

                     var8.t -= var2 << 7;
                     var8.ai -= var3 << 7;
                     var8.an -= (float)(var2 << 7);
                     var8.am -= (float)(var3 << 7);
                     var8.aj -= var2;
                     var8.c -= var3;
                     var8.ad -= var2;
                     var8.w -= var3;
                  }
               }

               var5 = (o)var4.next();
            } while(var5 == null);

            for(int var7 = 0; var7 < 10; ++var7) {
               var6 = var5.af;
               var6[var7] -= var2;
               var6 = var5.ag;
               var6[var7] -= var3;
            }

            var5.t -= var2 << 7;
            var5.ai -= var3 << 7;
            var5.an -= (float)(var2 << 7);
            var5.am -= (float)(var3 << 7);
            var5.aj -= var2;
            var5.c -= var3;
            var5.ad -= var2;
            var5.w -= var3;
         }
      }
   }

   public static void a(bG var0) {
      if (W) {
         a(var0, P, false);
      }

   }

   public static void b(bG var0) {
      if (cG >= 0 && var0.r.a((long)cG) != null) {
         a(var0, cG, false);
      }

   }

   public static void a(bG var0, int var1, boolean var2) {
      t var3 = (t)var0.r.a((long)var1);
      if (var3 != null && var3.f() && !var3.ax) {
         int var4 = var3.au;
         var3.av = false;
         if ((aM && G.j > 50 || G.j > 200) && var2 && var3.a() == var3.W) {
            var3.av = true;
         }

         int var5 = var3.t >> 7;
         int var6 = var3.ai >> 7;
         if (var0.a(var5, var6)) {
            long var7 = osrs.dB.a(0, 0, 0, 0, false, var3.j, var0.o);
            if (var3.aq != null && x >= var3.aI && x < var3.aJ) {
               var3.av = false;
               var3.aL = a(var0, var3.t, var3.ai, var4, var3.p());
               var3.ae = x;
               var0.x.a(var4, var3.t, var3.ai, var3.aL, 60, var3, var3.ac, var7, var3.as, var3.ay, var3.aN, var3.aC);
            } else {
               if ((var3.t & 127) == 64 && (var3.ai & 127) == 64) {
                  if (cm == var0.w[var5][var6]) {
                     return;
                  }

                  var0.w[var5][var6] = cm;
               }

               var3.aL = a(var0, var3.t, var3.ai, var4, var3.p());
               var3.ae = x;
               var0.x.a(var4, var3.t, var3.ai, var3.aL, 60, var3, var3.ac, var7, var3.a);
            }
         }
      }

   }

   public static void a(bG var0, gZ var1, boolean var2) {
      boolean var3 = var1.o.e && !var2;
      long var4 = 0L;
      boolean var6 = Q == var1.n.o;
      fz var7 = var1.b(var6);
      boolean var8;
      if (fz.d == var7) {
         var4 = osrs.dB.a(0, 0, 0, 5, false, 0, 0);
      } else if (var3 && fz.b != var7) {
         var8 = fz.a == var7;
         var4 = osrs.dB.a(var1.m(), osrs.bo.e(var1.k()), osrs.bo.e(var1.l()), 4, !var8, var1.e, var0.o);
      }

      var1.d.b(b(var0, var1.k(), var1.l(), var0.y));
      var1.n.x.b(x);
      var1.a(var2);
      var8 = var0.x.a(var0.y, var1.k(), var1.l(), var1.c(), 60, var1.n.x, var1.d(), var4, false);
      if (var8 && !var2) {
         a(var1.n);
         b(var1.n);
         osrs.bo.a(var1.n, osrs.cY.a);
         bG var9 = var1.n;
         int var10 = G.j;
         int[] var11 = G.a;

         for(int var12 = 0; var12 < var10; ++var12) {
            if (cG != var11[var12] && P != var11[var12]) {
               a(var9, var11[var12], true);
            }
         }

         osrs.bo.a(var1.n, osrs.cY.b);
         osrs.bo.a(var1.n, osrs.cY.c);
         d(var1.n);
      }

   }

   public static void c(bG var0) {
      gZ var1 = (gZ)var0.t.a((long)Q);
      if (var1 != null) {
         a(var0, var1, false);
      }

   }

   public static boolean a(gZ var0, o var1) {
      if (var0 != null && var1 != null) {
         int var2 = var1.U * 64;
         int var3 = var1.t - var2;
         int var4 = var1.ai - var2;
         int var5 = var1.t + var2 - 1;
         int var6 = var1.ai + var2 - 1;
         hB var7 = hB.a();
         var7.a(var0.o.h(), var0.k(), var0.l(), var0.d());
         boolean var8 = hy.b(var7, var3, var4, var5, var6);
         var7.b();
         return var8;
      } else {
         return false;
      }
   }

   public static boolean a(s var0) {
      if (!var0.aw.i()) {
         return false;
      } else {
         for(int var1 = 0; var1 < osrs.bo.aQ.e.a(); ++var1) {
            gZ var2 = (gZ)osrs.bo.aQ.t.a((long)osrs.bo.aQ.e.b(var1));
            if (var2 != null && x == var2.n.x.A && !var2.b() && a((gZ)var2, (o)var0)) {
               return true;
            }
         }

         return false;
      }
   }

   public static final void D() {
      for(in var0 = (in)F.d(); var0 != null; var0 = (in)F.f()) {
         if (x > var0.t) {
            var0.X();
         } else if (x >= var0.s) {
            var0.a(D, x, cd);
            osrs.bo.aQ.x.a(var0.i, (int)var0.w, (int)var0.a, (int)var0.b, 60, var0, var0.c, -1L, false);
         }
      }

   }

   public static final void d(bG var0) {
      for(dQ var1 = (dQ)var0.c.d(); var1 != null; var1 = (dQ)var0.c.f()) {
         if (var0.y == var1.d && !var1.a()) {
            if (x >= var1.h) {
               var1.a(cd, (byte)-103);
               if (var1.a()) {
                  var1.X();
               } else {
                  var0.x.a(var1.d, var1.e, var1.f, var1.g, 60, var1, 0, -1L, false);
               }
            }
         } else {
            var1.X();
         }
      }

   }

   public static final int E() {
      if (bL.c()) {
         return osrs.bo.aQ.y;
      } else {
         int var0 = 3;
         int var1 = cy >> 7;
         int var2 = cA >> 7;
         if (osrs.bo.bV < 310) {
            int var3;
            int var4;
            if (H == 1) {
               var3 = osrs.bo.dm >> 7;
               var4 = osrs.bo.o >> 7;
            } else {
               var3 = var1;
               var4 = var2;
            }

            int var5 = osrs.bo.bM >> 7;
            int var6 = osrs.bo.dO >> 7;
            if (!osrs.bo.aQ.a(var5, var6)) {
               return osrs.bo.aQ.y;
            }

            if (var3 < 0 || osrs.bo.aQ.p <= var3 || var4 < 0 || osrs.bo.aQ.q <= var4) {
               return osrs.bo.aQ.y;
            }

            if ((osrs.bo.aQ.v[osrs.bo.aQ.y][var5][var6] & 4) != 0) {
               var0 = osrs.bo.aQ.y;
            }

            int var7;
            if (var3 > var5) {
               var7 = var3 - var5;
            } else {
               var7 = var5 - var3;
            }

            int var8;
            if (var4 > var6) {
               var8 = var4 - var6;
            } else {
               var8 = var6 - var4;
            }

            int var9;
            int var10;
            if (var7 > var8) {
               var9 = var8 * 65536 / var7;
               var10 = 32768;

               while(var3 != var5) {
                  if (var5 < var3) {
                     ++var5;
                  } else if (var5 > var3) {
                     --var5;
                  }

                  if ((osrs.bo.aQ.v[osrs.bo.aQ.y][var5][var6] & 4) != 0) {
                     var0 = osrs.bo.aQ.y;
                  }

                  var10 += var9;
                  if (var10 >= 65536) {
                     var10 -= 65536;
                     if (var6 < var4) {
                        ++var6;
                     } else if (var6 > var4) {
                        --var6;
                     }

                     if ((osrs.bo.aQ.v[osrs.bo.aQ.y][var5][var6] & 4) != 0) {
                        var0 = osrs.bo.aQ.y;
                     }
                  }
               }
            } else if (var8 > 0) {
               var9 = var7 * 65536 / var8;
               var10 = 32768;

               while(var4 != var6) {
                  if (var6 < var4) {
                     ++var6;
                  } else if (var6 > var4) {
                     --var6;
                  }

                  if ((osrs.bo.aQ.v[osrs.bo.aQ.y][var5][var6] & 4) != 0) {
                     var0 = osrs.bo.aQ.y;
                  }

                  var10 += var9;
                  if (var10 >= 65536) {
                     var10 -= 65536;
                     if (var5 < var3) {
                        ++var5;
                     } else if (var5 > var3) {
                        --var5;
                     }

                     if ((osrs.bo.aQ.v[osrs.bo.aQ.y][var5][var6] & 4) != 0) {
                        var0 = osrs.bo.aQ.y;
                     }
                  }
               }
            }
         }

         if (!osrs.bo.aQ.a(var1, var2)) {
            return osrs.bo.aQ.y;
         } else {
            if ((osrs.bo.aQ.v[osrs.bo.aQ.y][var1][var2] & 4) != 0) {
               var0 = osrs.bo.aQ.y;
            }

            return var0;
         }
      }
   }

   public HashTable getComponentTable() {
      return this.aY();
   }

   public static boolean F() {
      return (X & 1) != 0;
   }

   public static final int b(bG var0, int var1, int var2, int var3) {
      return var0.a(var1, var2, var3, 1170451889);
   }

   public static int c(bG var0, int var1, int var2, int var3) {
      int var4 = b(var0, var1, var2, var3);
      gZ var5 = (gZ)osrs.bo.aQ.t.a((long)var0.o);
      if (var5 != null) {
         var4 += b(osrs.bo.aQ, var5.k(), var5.l(), var5.m());
      }

      return var4;
   }

   public static boolean d(bG var0, int var1, int var2, int var3) {
      return var1 < 3 && (var0.v[1][var2][var3] & 2) == 2;
   }

   public static int a(bG var0, int var1, int var2, int var3, int var4) {
      if (var4 == 0) {
         return b(var0, var1, var2, var3);
      } else {
         int var5 = var4 / 2;
         int var6 = var1 - var5;
         int var7 = var2 - var5;
         int var8 = var1 + var5;
         int var9 = var2 + var5;
         int var10 = osrs.bo.e(var6) + 1;
         int var11 = osrs.bo.e(var7) + 1;
         int var12 = osrs.bo.e(var8);
         int var13 = osrs.bo.e(var9);
         int var14 = Integer.MAX_VALUE;

         int var15;
         int var16;
         for(var15 = var10; var15 <= var12; ++var15) {
            for(var16 = var11; var16 <= var13; ++var16) {
               var14 = Math.min(var14, b(var0, osrs.B.b(var15), osrs.B.b(var16), var3));
            }
         }

         var15 = Math.min(var14, b(var0, var1, var2, var3));
         var16 = Math.min(var15, b(var0, var1 - var5, var2 - var5, var3));
         int var17 = Math.min(var16, b(var0, var1 - var5, var2 + var5, var3));
         int var18 = Math.min(var17, b(var0, var1 + var5, var2 - var5, var3));
         int var19 = Math.min(var18, b(var0, var1 + var5, var2 + var5, var3));
         return var19;
      }
   }

   public void setMinimapTileDrawer(TileFunction var1) {
      if (var1 == null) {
         k = B;
      } else {
         k = (var1x, var2, var3, var4, var5, var6, var7, var8) -> {
            var1.drawTile(var1x.bh[var2 >> var1x.bb & 3][var1x.s + var3][var1x.s + var4], var3, var4, var5, var6, var7, var8);
         };
      }

   }

   public static void u(int var0) {
      int var1;
      label62: {
         var1 = s.getMouseCurrentButton();
         if (osrs.bo.eM != 0) {
            if ((osrs.bo.eM >> var1 & 1) != 0) {
               break label62;
            }
         } else if (var1 == 4 && osrs.bo.cP) {
            break label62;
         }

         if (am.h(96)) {
            osrs.bo.fP += ((int)(bI * -24.0F) - osrs.bo.fP) / 2;
         } else if (am.h(97)) {
            osrs.bo.fP += ((int)(bI * 24.0F) - osrs.bo.fP) / 2;
         } else {
            osrs.bo.fP /= 2;
         }

         if (am.h(98)) {
            V += ((int)(bI * 12.0F) - V) / 2;
         } else if (am.h(99)) {
            V += ((int)(bI * -12.0F) - V) / 2;
         } else {
            V /= 2;
         }

         osrs.bo.eP = osrs.aI.i;
         osrs.bo.fT = osrs.aI.h;
         return;
      }

      var1 = osrs.aI.i - osrs.bo.eP;
      int var2 = osrs.bo.fT - osrs.aI.h;
      V = (int)((float)(var1 * 2) * bI);
      osrs.bo.fP = (int)((float)(var2 * 2) * bI);
      if (dv) {
         V = -V;
      }

      if (du) {
         osrs.bo.fP = -osrs.bo.fP;
      }

      osrs.bo.eP = var1 != -1 && var1 != 1 ? (osrs.aI.i + osrs.bo.eP) / 2 : osrs.aI.i;
      osrs.bo.fT = var2 != -1 && var2 != 1 ? (osrs.aI.h + osrs.bo.fT) / 2 : osrs.aI.h;
   }

   public static void a(bG var0, int var1, int var2, int var3, int var4, int var5, jf var6) {
      int var7 = osrs.bo.fA.getWidth();
      int var8 = osrs.bo.fA.getHeight();
      dh var9 = var0.x;
      long[] var10 = var9.bw;
      int var11 = var3;

      for(double var12 = (double)(var8 - var5); var11 < var9.aK; ++var11) {
         double var14 = var12 - osrs.bo.d;
         if ((int)var14 < 0) {
            break;
         }

         int var16 = var2;

         for(double var17 = (double)var4; var16 < var9.z; ++var16) {
            double var19 = osrs.bo.d + var17;
            if ((int)var19 > var7) {
               break;
            }

            int var21 = var9.m(var1, var9.s + var16, var9.s + var11);
            long var22 = var10[var21];
            if ((var22 & 1152921504606846976L) != 0L) {
               var6.method10157(var9, var21 | var9.aY * 3, var16, var11, (int)var17, (int)var14, (int)var19, (int)var12);
            }

            if ((var22 & 18014398509481984L) != 0L) {
               var6.method10157(var9, var21, var16, var11, (int)var17, (int)var14, (int)var19, (int)var12);
            }

            if ((var22 & 36028797018963968L) != 0L) {
               var6.method10157(var9, var9.aY + var21, var16, var11, (int)var17, (int)var14, (int)var19, (int)var12);
            }

            var17 = var19;
         }

         var12 = var14;
      }

   }

   public static void a(bG var0, int var1, int var2, int var3, float var4, ar var5) {
      iH var6 = G;
      t var7 = bA();

      for(int var8 = 0; var8 < var6.j; ++var8) {
         t var9 = (t)var0.r.b((long)var6.a[var8]);
         if (var9 != null && var9.U() != null && !var9.V()) {
            bE var10 = b(var0, var9.t, var9.ai);
            int var11 = (int)var10.h - cy;
            int var12 = (int)var10.j - cA;
            var10.f();
            int var13 = (int)((float)var11 * var4);
            int var14 = (int)((float)var12 * var4);
            if (var7 == var9) {
               int var19 = osrs.aW.d[var3];
               int var16 = osrs.aW.e[var3];
               int var17 = var13 * var16 + var14 * var19 >> 16;
               int var18 = var14 * var16 - var13 * var19 >> 16;
               osrs.eM.e = var17;
               osrs.eM.f = var18;
            } else {
               aV var15;
               if (var9.isFriend()) {
                  var15 = osrs.bo.bn[3];
               } else if (var7 != null && var7.getTeam() != 0 && var9.getTeam() != 0 && var7.getTeam() == var9.getTeam()) {
                  var15 = osrs.bo.bn[4];
               } else if (var9.isFriendsChatMember()) {
                  var15 = osrs.bo.bn[5];
               } else if (var9.isClanMember()) {
                  var15 = osrs.bo.bn[6];
               } else {
                  var15 = osrs.bo.bn[2];
               }

               a(var1, var2, var13, var14, var3, var15, var5);
            }
         }
      }

   }

   public void setMenuScroll(int var1) {
      osrs.bo.fR = Ints.constrainToRange(var1, 0, osrs.bo.fc);
   }

   public static void a(jc var0, int var1, int var2, int var3, int var4, aC var5, int var6) {
      hu var7 = new hu(var1, var2, var3, var4, var6, var5);
      if (!a && var0 == null) {
         throw new AssertionError();
      } else {
         var0.r.c(var7);
      }
   }

   public double getCameraFocalPointZ() {
      return osrs.bo.eO;
   }

   public static final void a(eL var0) {
      bu var1 = b.e;
      int var2;
      int var3;
      int var4;
      int var5;
      int var6;
      int var7;
      if (osrs.eL.k == var0) {
         var2 = ((aR)var1).b();
         var3 = (var2 >> 4 & 7) + osrs.bo.ci;
         var4 = (var2 & 7) + osrs.bo.O;
         var5 = var1.P();
         var6 = var1.d();
         if (osrs.bo.U.a(var3, var4)) {
            var7 = cc == -1 ? osrs.bo.U.y : cc;
            b(var7, var3, var4, var6, var5);
         }
      } else {
         int var8;
         int var9;
         int var10;
         int var11;
         int var12;
         int var13;
         int var14;
         int var15;
         int var16;
         int var18;
         if (osrs.eL.b == var0) {
            byte var20 = var1.C();
            var3 = var1.G();
            var4 = ((aR)var1).A();
            var5 = (var4 >> 4 & 7) + osrs.bo.ci;
            var6 = (var4 & 7) + osrs.bo.O;
            var7 = var1.H();
            var8 = var1.G();
            var9 = var1.d();
            var10 = var1.D();
            var11 = ((aR)var1).b();
            var12 = var11 >> 2;
            var13 = var11 & 3;
            var14 = j[var12];
            var15 = var1.D();
            var16 = var1.C();
            t var17 = (t)osrs.bo.U.r.a((long)var9);
            if (var17 != null) {
               var18 = cc == -1 ? osrs.bo.U.y : cc;
               a(var18, var5, var6, var12, var13, var14, var8, var3, var7, var20, var15, var16, var10, var17);
            }
         }

         if (osrs.eL.e == var0) {
            var2 = var1.H();
            var3 = ((aR)var1).A();
            var4 = var1.z();
            var5 = (var4 >> 4 & 7) + osrs.bo.ci;
            var6 = (var4 & 7) + osrs.bo.O;
            if (osrs.bo.U.a(var5, var6)) {
               var7 = cc == -1 ? osrs.bo.U.y : cc;
               a(var7, var5, var6, var2, var3);
            }
         } else if (osrs.eL.j == var0) {
            var2 = ((aR)var1).J();
            var3 = ((aR)var1).A();
            var4 = (var3 >> 4 & 7) + osrs.bo.ci;
            var5 = (var3 & 7) + osrs.bo.O;
            var6 = var1.H();
            var7 = var1.H();
            var8 = var1.Q();
            short var22 = (short)var1.e();
            var10 = var1.I();
            short var23 = (short)var1.e();
            if (osrs.bo.U.a(var4, var5)) {
               var12 = cc == -1 ? osrs.bo.U.y : cc;
               a(var12, var4, var5, var7, var8, var22, var2, var23, var10, var6);
            }
         } else if (osrs.eL.i != var0) {
            if (osrs.eL.h == var0) {
               var2 = var1.H();
               var3 = var1.z();
               var4 = var3 >> 2;
               var5 = var3 & 3;
               var6 = j[var4];
               var7 = ((aR)var1).A();
               var8 = (var7 >> 4 & 7) + osrs.bo.ci;
               var9 = (var7 & 7) + osrs.bo.O;
               if (0 <= var8 && var8 < osrs.bo.U.p - 1 && 0 <= var9 && var9 < osrs.bo.U.q - 1) {
                  var10 = cc == -1 ? osrs.bo.U.y : cc;
                  ic var24 = b(osrs.bo.U, var10, var8, var9, var6);
                  if (var24 != null) {
                     aC var26 = osrs.aC.a(var24.o);
                     if (var26.ak) {
                        osrs.bo.a(var10, var8, var9, var4, var5, var6, var2);
                        var24.m = var2;
                        return;
                     }
                  }

                  boolean var27 = osrs.bo.a(var10, var8, var9, var4, var5, var6, var2);
                  if (var27) {
                     return;
                  }

                  if (var24 != null) {
                     var24.m = var2;
                  }
               }
            } else if (osrs.eL.c == var0) {
               var2 = var1.z();
               var3 = (var2 >> 4 & 7) + osrs.bo.ci;
               var4 = (var2 & 7) + osrs.bo.O;
               var5 = var1.B();
               var6 = var5 >> 2;
               var7 = var5 & 3;
               var8 = j[var6];
               if (osrs.bo.U.a(var3, var4)) {
                  var9 = cc == -1 ? osrs.bo.U.y : cc;
                  a(osrs.bo.U, var9, var3, var4, var8, -1, var6, var7, 31, (String[])null, 0, -1);
               }
            } else if (osrs.eL.d == var0) {
               var2 = var1.P();
               var3 = var1.F();
               var4 = var1.z();
               var5 = (var4 >> 4 & 7) + osrs.bo.ci;
               var6 = (var4 & 7) + osrs.bo.O;
               var7 = osrs.bo.U.y;
               if (osrs.bo.U.a(var5, var6)) {
                  var8 = cc == -1 ? var7 : cc;
                  c(var8, var5, var6, var3, var2);
               }
            } else if (osrs.eL.f == var0) {
               var2 = var1.O();
               var3 = ((aR)var1).b();
               var4 = (var3 >> 4 & 7) + osrs.bo.ci;
               var5 = (var3 & 7) + osrs.bo.O;
               var6 = var1.Q();
               var7 = var1.H();
               if (osrs.bo.U.a(var4, var5)) {
                  var8 = cc == -1 ? osrs.bo.U.y : cc;
                  b(var8, var4, var5, var7, var6, var2);
               }
            } else if (osrs.eL.g == var0) {
               var2 = var1.N();
               var3 = ((aR)var1).b();
               var4 = ((aR)var1).b();
               var5 = osrs.bo.U.y;
               var6 = osrs.bo.U.B + (var4 >> 4 & 7) + osrs.bo.ci;
               var7 = osrs.bo.U.z + (var4 & 7) + osrs.bo.O;
               var8 = var1.Q();
               var9 = osrs.bo.b(var8);
               var10 = osrs.bo.c(var8);
               var11 = osrs.bo.d(var8);
               var12 = var1.F();
               var13 = var1.H();
               var14 = var1.M();
               var15 = var1.H();
               var16 = var1.G();
               int var30 = var1.d();
               var18 = var1.d();
               if (var12 != 65535) {
                  in var19 = new in(var5, var6, var7, var30, var14, var9, var10, var11, var18, var2, var12, x + var16, x + var13, var3, var15);
                  F.a((az)var19);
               }
            } else if (osrs.eL.l == var0) {
               var2 = var1.G();
               var3 = var1.B();
               var4 = ((aR)var1).A();
               var5 = (var4 >> 4 & 7) + osrs.bo.ci;
               var6 = (var4 & 7) + osrs.bo.O;
               var7 = var1.z() & 31;
               var8 = ((aR)var1).b();
               var9 = var1.z();
               if (osrs.bo.U.a(var5, var6)) {
                  osrs.bo.bw.a(osrs.bo.U.o, var2, var5, var6, var7, var9, var8, var3);
               }
            } else if (osrs.eL.a == var0) {
               var2 = var1.F();
               var3 = ((aR)var1).b();
               var4 = (var3 >> 4 & 7) + osrs.bo.ci;
               var5 = (var3 & 7) + osrs.bo.O;
               var6 = var1.d();
               var7 = ((aR)var1).A();
               if (osrs.bo.U.a(var4, var5)) {
                  var8 = osrs.bo.f(var4);
                  var9 = osrs.bo.f(var5);
                  var10 = cc == -1 ? osrs.bo.U.y : cc;
                  dQ var25 = new dQ(osrs.bo.U, var2, var10, var8, var9, b(osrs.bo.U, var8, var9, var10) - var7, var6, x);
                  osrs.bo.U.c.a((az)var25);
               }
            } else if (osrs.eL.m == var0) {
               var2 = var1.z();
               var3 = var1.G();
               boolean var21 = ((aR)var1).b() == 1;
               var5 = var1.H();
               var6 = var1.z();
               var7 = var1.B();
               var8 = (var7 >> 4 & 7) + osrs.bo.ci;
               var9 = (var7 & 7) + osrs.bo.O;
               var10 = var1.Q();
               var11 = var1.G();
               if (osrs.bo.U.a(var8, var9)) {
                  var12 = cc == -1 ? osrs.bo.U.y : cc;
                  a(var12, var8, var9, var3, var10, var2, var11, var5, var6, var21);
               }
            }
         } else {
            var2 = ((aR)var1).b();
            var3 = (var2 >> 4 & 7) + osrs.bo.ci;
            var4 = (var2 & 7) + osrs.bo.O;
            var5 = ((aR)var1).A();
            var6 = ((aR)var1).b();
            var7 = var6 >> 2;
            var8 = var6 & 3;
            var9 = j[var7];
            var10 = var1.d();
            var11 = ((aR)var1).b();
            String[] var28 = null;
            if (var11 > 0) {
               var28 = new String[5];

               for(var13 = 0; var13 < var11; ++var13) {
                  var14 = ((aR)var1).b();
                  String var29 = ((aR)var1).m();
                  if (var14 >= 0 && var14 < 5) {
                     var28[var14] = var29;
                  }
               }
            }

            if (osrs.bo.U.a(var3, var4)) {
               var13 = cc == -1 ? osrs.bo.U.y : cc;
               a(osrs.bo.U, var13, var3, var4, var9, var10, var7, var8, var5, var28, 0, -1);
            }
         }
      }

   }

   public static void a(int var0, int var1, int var2, int var3, int var4, int var5, aV var6, ar var7, float var8) {
      if (var6 != null) {
         int var9 = var2 * var2 + var3 * var3;
         if (var9 <= 6400) {
            int var10 = (int)((double)var8 * 8192.0);
            int var11 = osrs.aW.d[var4];
            int var12 = osrs.aW.e[var4];
            int var13 = var2 * var12 + var3 * var11 >> 16;
            int var14 = var3 * var12 - var2 * var11 >> 16;
            int var15 = var4 - var5 & 2047;
            int var16 = var7.a / 2 + var0 + var13;
            int var17 = var7.b / 2 + var1 - var14;
            int var18 = var6.getMaxWidth() * var10 >> 8;
            int var19 = var6.getMaxHeight() * var10 >> 8;
            int var20 = var16 - var18 / 2;
            int var21 = var17 - var19 / 2;
            int var22 = var6.getWidth() / 2;
            int var23 = var6.getHeight() / 2;
            var6.b(var20, var21, var18, var19, var22, var23, var15, var10, var0, var1, var7.d(), var7.c());
         }
      }

   }

   public static void a(long var0, int var2) {
      if (osrs.dB.f + 1 < osrs.dB.g.length) {
         ++osrs.dB.f;
         osrs.dB.g[osrs.dB.f] = var0;
         osrs.dB.h[osrs.dB.f] = var2;
      }

   }

   public Widget getWidget(int var1) {
      return this.Q(var1);
   }

   public static void a(int var0, int var1, int var2, int var3, int var4) {
      a(osrs.bo.U, var0, var1, var2, var3, var4);
   }

   public static void a(bG var0, int var1, int var2, int var3, int var4, int var5) {
      fJ var6 = var0.a[var1][var2][var3];
      if (var6 != null) {
         for(iC var7 = (iC)var6.d(); var7 != null; var7 = (iC)var6.f()) {
            if (var7.f == var4) {
               var7.a(var5);
               break;
            }
         }
      }

   }

   public static void b(int var0, int var1, int var2, int var3, int var4, int var5) {
      osrs.bo.a(osrs.bo.U, var0, var1, var2, var3, var4, var5);
   }

   public static void a(int var0, int var1, int var2, int var3, int var4, short var5, int var6, short var7, int var8, int var9) {
      a(osrs.bo.U, var0, var1, var2, var3, var4, var5, var6, var7, var8, var9);
   }

   public static void a(bG var0, int var1, int var2, int var3, int var4, int var5, short var6, int var7, short var8, int var9, int var10) {
      fJ var11 = var0.a[var1][var2][var3];
      if (var11 != null) {
         for(iC var12 = (iC)var11.d(); var12 != null; var12 = (iC)var11.f()) {
            if (var12.f == var4 && var12.h == var5) {
               cF var13 = var12.a();
               if (var13 == null) {
                  var13 = new cF(var4);
               }

               if (var13.a() && var7 > -1 && var7 < var13.e.length) {
                  var13.e[var7] = var6;
               }

               if (var13.b() && var9 > -1 && var9 < var13.f.length) {
                  var13.f[var9] = var8;
               }

               if (var10 > -1) {
                  var13.g = var10;
                  var13.h = null;
               }

               var12.a(var13);
               break;
            }
         }

         c(var1, var2, var3);
      }

   }

   public static void b(int var0, int var1, int var2, int var3, int var4) {
      b(osrs.bo.U, var0, var1, var2, var3, var4);
   }

   public static void b(bG var0, int var1, int var2, int var3, int var4, int var5) {
      fJ var6 = var0.a[var1][var2][var3];
      if (var6 != null) {
         for(iC var7 = (iC)var6.d(); var7 != null; var7 = (iC)var6.f()) {
            if (var7.f == var4 && var7.h == var5) {
               var7.c();
               break;
            }
         }

         c(var1, var2, var3);
      }

   }

   public static void a(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9, int var10, int var11, int var12, t var13) {
      aC var14 = osrs.aC.a(var6);
      int var15;
      int var16;
      if (var4 != 1 && var4 != 3) {
         var15 = var14.t;
         var16 = var14.u;
      } else {
         var15 = var14.u;
         var16 = var14.t;
      }

      int var17 = (var15 >> 1) + var1;
      int var18 = (var15 + 1 >> 1) + var1;
      int var19 = (var16 >> 1) + var2;
      int var20 = (var16 + 1 >> 1) + var2;
      int[][] var21 = osrs.bo.U.u[var0];
      int var22 = var21[var17][var19] + var21[var18][var19] + var21[var17][var20] + var21[var18][var20] >> 2;
      int var23 = (var1 << 7) + (var15 << 6);
      int var24 = (var2 << 7) + (var16 << 6);
      aH var25 = var14.b(var3, var4, var21, var23, var22, var24);
      if (var25 != null) {
         a(osrs.bo.U, var0, var1, var2, var5, -1, 0, 0, 31, (String[])null, var7 + 1, var8 + 1);
         var13.aI = x + var7;
         var13.aJ = x + var8;
         var13.aq = var25;
         var13.at = var1 * 128 + var15 * 64;
         var13.aM = var2 * 128 + var16 * 64;
         var13.aw = var22;
         int var26;
         if (var9 > var11) {
            var26 = var9;
            var9 = var11;
            var11 = var26;
         }

         if (var10 > var12) {
            var26 = var10;
            var10 = var12;
            var12 = var26;
         }

         var13.as = var1 + var9;
         var13.aN = var1 + var11;
         var13.ay = var2 + var10;
         var13.aC = var2 + var12;
      }

   }

   public static void c(int var0, int var1, int var2, int var3, int var4) {
      c(osrs.bo.U, var0, var1, var2, var3, var4);
   }

   public static void c(bG var0, int var1, int var2, int var3, int var4, int var5) {
      fJ var6 = var0.a[var1][var2][var3];
      if (var6 != null) {
         for(iC var7 = (iC)var6.d(); var7 != null; var7 = (iC)var6.f()) {
            if (var7.f == var4 && var7.h == var5) {
               var7.X();
               break;
            }
         }

         if (var6.d() == null) {
            var0.a[var1][var2][var3] = null;
         }

         osrs.bo.a(var0, var1, var2, var3);
      }

   }

   public static void a(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, boolean var9) {
      osrs.bo.a(osrs.bo.U, var0, var1, var2, var3, var4, var5, var6, var7, var8, var9);
   }

   public List getDBRowsByValue(int var1, int var2, int var3, Object var4) {
      if (!s.isClientThread()) {
         if (!osrs.T.eo) {
            throw new AssertionError("must be called on client thread");
         } else {
            throw new IllegalStateException("must be called on client thread");
         }
      } else {
         D var5 = osrs.D.a(var1 << 12 | var2 << 4);
         Map var6 = (Map)var5.a.get(var3);
         List var7 = (List)var6.get(var4);
         return var7 == null ? Collections.emptyList() : Collections.unmodifiableList(var7);
      }
   }

   public static boolean c(int var0, int var1, int var2, int var3, int var4, int var5) {
      return osrs.bo.a(osrs.bo.U.y, var0, var1, var2, var3, var4, var5);
   }

   public static void a(bG var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, String[] var9, int var10, int var11) {
      fJ var12 = var0.b;
      ic var13 = null;

      for(ic var14 = (ic)var12.d(); var14 != null; var14 = (ic)var12.f()) {
         if (var14.e == var1 && var14.h == var2 && var14.i == var3 && var14.g == var4) {
            var13 = var14;
            break;
         }
      }

      if (var13 == null) {
         var13 = new ic();
         var13.e = var1;
         var13.g = var4;
         var13.h = var2;
         var13.i = var3;
         var13.m = -1;
         a(var0, var13);
         var12.a((az)var13);
      }

      var13.o = var5;
      var13.f = var6;
      var13.l = var7;
      var13.b = var10;
      var13.c = var11;
      var13.a(var8);
      var13.a(var9);
   }

   public static final ic b(bG var0, int var1, int var2, int var3, int var4) {
      for(ic var5 = (ic)var0.b.d(); var5 != null; var5 = (ic)var0.b.f()) {
         if (var5.e == var1 && var5.h == var2 && var5.i == var3 && var5.g == var4) {
            return var5;
         }
      }

      return null;
   }

   public aC v(int var1) {
      if (!s.isClientThread() && Thread.currentThread() != osrs.bo.dI) {
         if (!a) {
            throw new AssertionError("must be called on client or maploader thread");
         } else {
            throw new IllegalStateException("must be called on client or maploader thread");
         }
      } else {
         return osrs.aC.c(var1);
      }
   }

   public static final void a(bG var0, ic var1) {
      dh var2 = var0.x;
      long var3 = 0L;
      int var5 = -1;
      int var6 = 0;
      int var7 = 0;
      if (var1.g == 0) {
         var3 = var2.r(var1.e, var1.h, var1.i);
      }

      if (var1.g == 1) {
         var3 = var2.t(var1.e, var1.h, var1.i);
      }

      if (var1.g == 2) {
         var3 = var2.c(var1.e, var1.h, var1.i);
      }

      if (var1.g == 3) {
         var3 = var2.i(var1.e, var1.h, var1.i);
      }

      if (var3 != 0L) {
         int var8 = var2.a(var1.e, var1.h, var1.i, var3);
         var5 = osrs.dB.c(var3);
         var6 = var8 & 31;
         var7 = var8 >> 6 & 3;
      }

      var1.n = var5;
      var1.k = var6;
      var1.j = var7;
   }

   public static final void G() {
      Iterator var0 = D.iterator();

      while(var0.hasNext()) {
         bG var1 = (bG)var0.next();
         e(var1);
      }

   }

   public static final void e(bG var0) {
      for(ic var1 = (ic)var0.b.d(); var1 != null; var1 = (ic)var0.b.f()) {
         if (var1.c > 0) {
            --var1.c;
         }

         int var2;
         int var3;
         aC var4;
         boolean var5;
         if (var1.c == 0) {
            if (var1.n >= 0) {
               var2 = var1.n;
               var3 = var1.k;
               var4 = osrs.aC.a(var2);
               if (var3 == 11) {
                  var3 = 10;
               }

               if (var3 >= 5 && var3 <= 8) {
                  var3 = 4;
               }

               var5 = var4.b(var3);
               if (!var5) {
                  continue;
               }
            }

            osrs.bo.a(var0, var1.e, var1.g, var1.h, var1.i, var1.n, var1.j, var1.k, var1.m);
            var1.X();
         } else {
            if (var1.b > 0) {
               --var1.b;
            }

            if (var1.b == 0 && var1.h >= 1 && var1.i >= 1 && var1.h <= 102 && var1.i <= 102) {
               if (var1.o >= 0) {
                  var2 = var1.o;
                  var3 = var1.f;
                  var4 = osrs.aC.a(var2);
                  if (var3 == 11) {
                     var3 = 10;
                  }

                  if (var3 >= 5 && var3 <= 8) {
                     var3 = 4;
                  }

                  var5 = var4.b(var3);
                  if (!var5) {
                     continue;
                  }
               }

               osrs.bo.a(var0, var1.e, var1.g, var1.h, var1.i, var1.o, var1.l, var1.f, var1.m);
               var1.b = -1;
               if (var1.o == var1.n && var1.n == -1) {
                  var1.X();
               } else if (var1.o == var1.n && var1.l == var1.j && var1.k == var1.f) {
                  var1.X();
               }
            }
         }
      }

   }

   public static void H() {
      if (!osrs.bo.fk) {
         osrs.bo.fk = true;
         String var0 = System.getProperty("os.name", "");
         String var1 = System.getProperty("os.arch");
         if (var0.startsWith("Windows")) {
            String var2 = "rlicn_" + var1 + ".dll";

            try {
               InputStream var3 = Client.class.getResourceAsStream(var2);

               try {
                  Path var4 = Files.createTempFile("", var2);
                  var4.toFile().deleteOnExit();
                  Files.copy(var3, var4, new CopyOption[]{StandardCopyOption.REPLACE_EXISTING});
                  System.load(var4.toAbsolutePath().toString());
                  a((Component)s);
               } catch (Throwable var7) {
                  if (var3 != null) {
                     try {
                        var3.close();
                     } catch (Throwable var6) {
                        var7.addSuppressed(var6);
                     }
                  }

                  throw var7;
               }

               if (var3 != null) {
                  var3.close();
               }
            } catch (Exception | Error var8) {
            }
         }
      }

   }

   public static void b(bG var0, int var1, int var2, int var3, float var4, ar var5) {
      for(int var6 = 0; var6 < var0.d.c(); ++var6) {
         s var7 = (s)var0.s.b((long)var0.d.c(var6));
         if (var7 != null && var7.T() != null) {
            aN var8 = var7.T();
            if (var8 != null && var8.getConfigs() != null) {
               var8 = var8.k();
            }

            if (var8 != null && var8.isMinimapVisible() && var8.isInteractible()) {
               bE var9 = b(var0, var7.t, var7.ai);
               int var10 = (int)var9.h - cy;
               int var11 = (int)var9.j - cA;
               var9.f();
               int var12 = (int)((float)var10 * var4);
               int var13 = (int)((float)var11 * var4);
               a(var1, var2, var12, var13, var3, osrs.bo.bn[1], var5);
            }
         }
      }

   }

   public static void I() {
      if (osrs.bo.fz == null) {
         av var0 = dG[2];
         int var1 = var0.j(14);
         int[] var2 = var0.getFileIds(14);
         osrs.bo.fz = new int[var2.length];
         osrs.bo.fS = new int[var1];
         int var3 = 0;
         int[] var4 = var2;
         int var5 = var2.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            int var7 = var4[var6];
            VarbitComposition var8 = s.getVarbit(var7);
            osrs.bo.fz[var3++] = var8.getIndex() << 16 | var7;
            osrs.bo.fS[var7] = var8.getIndex() << 16 | var8.getMostSignificantBit() << 8 | var8.getLeastSignificantBit();
         }

         Arrays.sort(osrs.bo.fz);
      }

   }

   public void b(long var1) {
      if (H == 0) {
         bG var3 = D.j(T);
         if (var3 == null) {
            var3 = osrs.bo.aQ;
         }

         hW var4 = this.f();
         if (var4 != null) {
            float var5 = var4.w();
            float var6 = var4.v();
            int var7 = var4.D();
            if (osrs.bo.aQ != var3) {
               gZ var8 = (gZ)osrs.bo.aQ.t.b((long)var3.o);
               bE var9;
               if (var8 != null) {
                  var9 = var8.f((int)var5, (int)var6);
               } else {
                  var9 = osrs.bE.d(var5, 0.0F, var6);
               }

               var5 = var9.h;
               var6 = var9.j;
               var9.f();
            }

            if (!(osrs.bo.fq - (double)var5 < -500.0) && !(osrs.bo.fq - (double)var5 > 500.0) && !(osrs.bo.eO - (double)var6 < -500.0) && !(osrs.bo.eO - (double)var6 > 500.0)) {
               double var10 = (double)var1 / 3.2E8;
               osrs.bo.fq += ((double)var5 - osrs.bo.fq) * var10;
               osrs.bo.eO += ((double)var6 - osrs.bo.eO) * var10;
            } else {
               osrs.bo.fq = (double)var5;
               osrs.bo.eO = (double)var6;
            }

            float var11 = a(var3, var4.w(), var4.v(), var7);
            if (osrs.bo.aQ != var3) {
               gZ var12 = (gZ)osrs.bo.aQ.t.b((long)var3.o);
               if (var12 != null) {
                  var11 += a(osrs.bo.aQ, var12.w(), var12.v(), var12.D());
               }
            }

            osrs.bo.eS = (double)(var11 - (float)J);
            osrs.bo.dm = (int)osrs.bo.fq;
            osrs.bo.o = (int)osrs.bo.eO;
            osrs.bo.dA = (int)osrs.bo.eS;
         }
      }

   }

   public static boolean a(long var0, int var2, int var3, int var4, int var5) {
      int var6 = (int)(var0 >>> 20 & 4294967295L);
      int var7 = (int)(var0 >> 16 & 7L);
      if (var3 == var6 && var7 == 2) {
         int var8 = var4 | var5 << 6;
         int var9 = var2 & 255;
         return var8 == var9;
      } else {
         return false;
      }
   }

   public static void a(ac var0) {
      if (!s.isClientThread()) {
         if (!osrs.T.eo) {
            throw new AssertionError("must be called on client thread");
         } else {
            throw new IllegalStateException("must be called on client thread");
         }
      } else if (!a && osrs.bo.fd != null) {
         throw new AssertionError("scripts are not reentrant");
      } else {
         s.a((ac)var0, 5000000, 0);
         boolean var1 = false;
         if (!a) {
            var1 = true;
         }

         Object[] var2 = var0.getArguments();
         if (var1 && var2[0] instanceof Integer) {
            int var3 = (Integer)var2[0];
            v var4 = (v)osrs.v.a.c((long)var3);
            if (var4 != null) {
               int var5 = 0;
               int var6 = 0;
               int var7 = 0;
               int var8 = 1;

               while(true) {
                  if (var8 >= var2.length) {
                     if (var4.g != var5 || var4.e != var6 || var4.d != var7) {
                        throw new AssertionError("Script " + var3 + " was called with the incorrect number of arguments; takes " + var4.g + "+" + var4.e + "," + var4.d + ", got " + var5 + "+" + var6 + "+" + var7);
                     }
                     break;
                  }

                  if (var2[var8] instanceof Integer) {
                     ++var5;
                  } else if (var2[var8] instanceof Long) {
                     ++var6;
                  } else {
                     ++var7;
                  }

                  ++var8;
               }
            }
         }

      }
   }

   public static void w(int var0) {
      if (osrs.bo.eh != null && !"cache io".equals(osrs.bo.eh.getName())) {
         osrs.bo.eh.setName("cache io");
      }

   }

   public static final void c(int var0, int var1, int var2) {
      osrs.bo.a(osrs.bo.U, var0, var1, var2);
   }

   public static int x(int var0) {
      if (var0 >= 0 && var0 < osrs.bo.fS.length) {
         int var1 = osrs.bo.fS[var0];
         if (var1 == 0) {
            throw new IndexOutOfBoundsException("Varbit " + var0 + " does not exist");
         } else {
            int var2 = var1 >> 16;
            return var2;
         }
      } else {
         throw new IndexOutOfBoundsException("Varbit " + var0 + " does not exist");
      }
   }

   public static bG d(int var0, int var1, int var2) {
      return D.a(var0, var1, var2, bL.p(), osrs.dn.b);
   }

   public static t J() {
      bG var0 = D.b(Q);
      return var0 != null ? var0.a(P) : null;
   }

   public static jR K() {
      return G.a(P);
   }

   public static int L() {
      t var0 = J();
      return var0 != null ? var0.ar : 0;
   }

   public static bE M() {
      bG var0 = D.b(Q);
      t var1 = var0.a(P);
      if (var1 != null) {
         bE var2 = a(var0, var1.k(), var1.l());
         var2.i = (float)var1.m();
         return var2;
      } else {
         synchronized(osrs.bE.g) {
            bE var3;
            if (osrs.bE.e == 0) {
               var3 = new bE();
            } else {
               osrs.bE.g[--osrs.bE.e].b();
               var3 = osrs.bE.g[osrs.bE.e];
            }

            return var3;
         }
      }
   }

   public static B N() {
      bE var0 = M();
      B var1;
      if (var0.c()) {
         var1 = new B();
      } else {
         int var2 = osrs.bo.e((int)var0.h);
         int var3 = osrs.bo.e((int)var0.j);
         var1 = new B((int)var0.i, var2, var3);
      }

      if (var1.c()) {
         var1.b -= osrs.bo.aQ.B;
         var1.c -= osrs.bo.aQ.z;
      }

      return var1;
   }

   public static void b(ig var0, jd var1, int var2, int var3) {
      int var4 = var0.w;
      int var5 = var0.c;
      int var6 = var0.b;
      int var7 = var0.v;
      if (var2 != 255) {
         osrs.df.e(var4, var5, var6, var7, 6116423, var2);
         osrs.df.e(var4 + 1, var5 + 1, var6 - 2, 16, 0, var2);
         osrs.df.f(var4 + 1, var5 + 18, var6 - 2, var7 - 19, 0, var2);
      } else {
         osrs.df.k(var4, var5, var6, var7, 6116423);
         osrs.df.k(var4 + 1, var5 + 1, var6 - 2, 16, 0);
         osrs.df.m(var4 + 1, var5 + 18, var6 - 2, var7 - 19, 0);
      }

      String var8 = var1 == null ? "Choose Option" : var1.getTarget();
      osrs.bo.ck.d(var8, var4 + 3, var5 + 14, 6116423, -1);
      int var9 = osrs.aI.h;
      int var10 = osrs.aI.i;

      for(int var11 = 0; var11 < var0.h; ++var11) {
         if (var0.h - 1 - var11 >= var3) {
            int var12 = (var0.h - 1 - var11 - var3) * 15 + var5 + 31;
            String var13 = var0.o[var11];
            if (!var0.p[var11].isEmpty()) {
               if (!var13.isEmpty()) {
                  var13 = var13 + " ";
               }

               var13 = var13 + var0.p[var11];
            }

            if (var0.q[var11] != null) {
               var13 = var13 + " </col><gt>";
            }

            boolean var14 = var9 > var4 && var9 < var4 + var6 && var10 > var12 - 13 && var10 < var12 + 3;
            int var15 = var14 ? 16776960 : 16777215;
            osrs.bo.ck.d(var13, var4 + 3, var12, var15, 0);
            if (var0.a == var11 && var0.q[var11] != null && var0.q[var11].h > 0) {
               b(var0.q[var11], var0.e[var11], var2, osrs.bo.fs);
            }
         }
      }

   }

   public static float a(float var0, float var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8, float var9, float var10, float var11) {
      float var12 = var0 - var3;
      float var13 = var0 + var3;
      float var14 = var1 - var4;
      float var15 = var1 + var4;
      float var16 = var2 - var5;
      float var17 = var2 + var5;
      float var18 = var9 - var6;
      float var19 = var10 - var7;
      float var20 = var11 - var8;
      float var21 = 0.0F;
      float var22 = 1.0F;
      float var23;
      float var24;
      float var25;
      float var26;
      if ((double)Math.abs(var18) < 1.0E-9) {
         if (var6 < var12 || var6 > var13) {
            return Float.NaN;
         }
      } else {
         var23 = 1.0F / var18;
         var24 = (var12 - var6) * var23;
         var25 = (var13 - var6) * var23;
         if (var24 > var25) {
            var26 = var24;
            var24 = var25;
            var25 = var26;
         }

         var21 = Math.max(var21, var24);
         var22 = Math.min(var22, var25);
         if (var21 > var22) {
            return Float.NaN;
         }
      }

      if ((double)Math.abs(var19) < 1.0E-9) {
         if (var7 < var14 || var7 > var15) {
            return Float.NaN;
         }
      } else {
         var23 = 1.0F / var19;
         var24 = (var14 - var7) * var23;
         var25 = (var15 - var7) * var23;
         if (var24 > var25) {
            var26 = var24;
            var24 = var25;
            var25 = var26;
         }

         var21 = Math.max(var21, var24);
         var22 = Math.min(var22, var25);
         if (var21 > var22) {
            return Float.NaN;
         }
      }

      if ((double)Math.abs(var20) < 1.0E-9) {
         if (var8 < var16 || var8 > var17) {
            return Float.NaN;
         }
      } else {
         var23 = 1.0F / var20;
         var24 = (var16 - var8) * var23;
         var25 = (var17 - var8) * var23;
         if (var24 > var25) {
            var26 = var24;
            var24 = var25;
            var25 = var26;
         }

         var21 = Math.max(var21, var24);
         var26 = Math.min(var22, var25);
         if (var21 > var26) {
            return Float.NaN;
         }
      }

      var23 = var18 * var21 + var6;
      var24 = var19 * var21 + var7;
      var25 = var20 * var21 + var8;
      return var25 * var25 + var23 * var23 + var24 * var24;
   }

   public boolean isRuneLiteObjectRegistered(RuneLiteObjectController var1) {
      return ja.b(var1);
   }

   public static void a(bu var0) {
      osrs.bo.fw = null;
   }

   public void changeWorld(World var1) {
      ey var2 = (ey)var1;
      boolean var3 = this.getWorldType().contains(WorldType.BETA_WORLD) != var2.getTypes().contains(WorldType.BETA_WORLD);
      osrs.bo.q = var2.getTypes().contains(WorldType.BETA_WORLD);
      if (var2.getTypes().contains(WorldType.BETA_WORLD)) {
         var2.c = "beta";
      }

      this.a(var2);
      if (var3 && this.getGameState().getState() < GameState.LOADING.getState()) {
         bP();
      }

   }

   public long getOverallExperience() {
      int[] var1 = this.getSkillExperiences();
      long var2 = 0L;
      int[] var4 = var1;
      int var5 = var1.length;

      for(int var6 = 0; var6 < var5; ++var6) {
         int var7 = var4[var6];
         var2 += (long)var7;
      }

      return var2;
   }

   public int getMenuHeight() {
      return cJ.v;
   }

   public ClanChannel getClanChannel(int var1) {
      cI[] var2 = this.bM();
      return var1 >= 0 && var1 < var2.length ? var2[var1] : null;
   }

   public static void y(int var0) {
      if (var0 != -1) {
         bp var1 = s.bB()[var0];
         if (var1 != null) {
            GrandExchangeOfferChanged var2 = new GrandExchangeOfferChanged();
            var2.setOffer(var1);
            var2.setSlot(var0);
            s.getCallbacks().post(var2);
         }
      }

   }

   public in a(int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9, int var10, Actor var11, int var12, int var13) {
      if (!s.isClientThread()) {
         if (!osrs.T.eo) {
            throw new AssertionError("must be called on client thread");
         } else {
            throw new IllegalStateException("must be called on client thread");
         }
      } else {
         int var14 = 0;
         if (var11 instanceof s) {
            var14 = ((s)var11).getIndex() + 1;
         } else if (var11 instanceof t) {
            var14 = -(((t)var11).getId() + 1);
         }

         WorldPoint var15 = WorldPoint.fromLocal(s, var3, var4, var2);
         WorldPoint var16 = WorldPoint.fromLocal(s, var12, var13, var2);
         in var17 = new in(var2, var15.getX(), var15.getY(), var5, 0, var2, var16.getX(), var16.getY(), 0, var14, var1, var6, var7, var8, 0);
         F.c(var17);
         return var17;
      }
   }

   public static void z(int var0) {
      if (var0 != -1) {
         dx[var0] = df[var0];
         df[var0] = false;
      }

   }

   public static void c(jc var0) {
      bG var1 = var0.o;
      dh var2 = var1.x;
      var1.x = var0.g;
      var1.x.aZ = var1;
      var1.x.bB = var2.bB;
      var1.x.u = var2.u;
      var1.x.bC = var2.bC;
      var1.x.v = var2.v;
      var1.x.r.e(var2.r);
      var1.x.e.b(var2.e);
      var2.a();
      var1.u = var0.Q;
      var1.v = var0.O;
      var1.n = var0.M;
      s.getCallbacks().post(new WorldViewLoaded(var1));
      var1.x.bV = true;
      var1.x.m();
      DrawCallbacks var3 = var0.s;
      if (var3 != null && ef == var3 && (osrs.bo.fe & 16) != 0) {
         var3.swapScene(var1.x);
      }

      var1.x.h();
   }

   public static void b(s var0) {
      var0.U = var0.aw.j;
      var0.B = var0.aw.k;
      var0.z = var0.aw.v;
      var0.S = var0.aw.e;
      var0.k = var0.aw.i;
      var0.y = var0.aw.p;
      var0.W = var0.aw.u;
      var0.g = var0.aw.t;
      var0.I = var0.aw.q;
      var0.o = var0.aw.D;
      var0.p = var0.aw.m;
      var0.u = var0.aw.h;
      var0.q = var0.aw.n;
      var0.K = var0.aw.Q;
      var0.Q = var0.aw.c;
      var0.J = var0.aw.I;
      var0.Y = var0.aw.s;
   }

   public static dR d(int var0, int var1) {
      q.d = var0;
      q.c = var1;
      q.a = 1;
      q.b = 1;
      return q;
   }

   public static final boolean O() {
      return cI;
   }

   public void removeRuneLiteObject(RuneLiteObjectController var1) {
      ja.c(var1);
   }

   public hX P() {
      return new hX();
   }

   public static void b(bu var0) {
      osrs.bo.fw = dr;
      dr = new ko();
   }

   public static void A(int var0) {
      if (osrs.aI.p > 0) {
         long var1 = System.nanoTime() - ao;
         osrs.bo.eU = (int)(var1 / 20000000L);
      } else {
         osrs.bo.eU = 0;
         ao = System.nanoTime();
      }

      osrs.aI.p = osrs.bo.eU * 15000 / dz;
   }

   public static void a(aR var0) {
      try {
         hQ var1 = s.a("", "oldscape", true);

         try {
            var1.c(var0.T(), 0, var0.ag());
         } finally {
            var1.b(true);
         }
      } catch (Exception var6) {
         dF.error("error saving preferences", var6);
      }

   }

   public static void a(fO var0, int var1, int var2, int var3, int var4, int var5, int var6) {
      int var7;
      s var8;
      r var9;
      if (var3 == 9) {
         var7 = var4 & '\uffff';
         var8 = (s)var0.a((long)var7);
         if (var8 != null) {
            cn = var5;
            co = var6;
            cv = 2;
            cu = 0;
            cV = var1;
            cW = var2;
            var9 = osrs.r.a(osrs.u.aR, b.p);
            var9.f.r(var7);
            var9.f.m(am.c(82) ? 1 : 0);
            b.a(var9);
         }
      }

      if (var3 == 10) {
         var7 = var4 & '\uffff';
         var8 = (s)var0.a((long)var7);
         if (var8 != null) {
            cn = var5;
            co = var6;
            cv = 2;
            cu = 0;
            cV = var1;
            cW = var2;
            var9 = osrs.r.a(osrs.u.bn, b.p);
            var9.f.b(var7);
            var9.f.a(am.c(82) ? 1 : 0);
            b.a(var9);
         }
      }

      if (var3 == 11) {
         var7 = var4 & '\uffff';
         var8 = (s)var0.a((long)var7);
         if (var8 != null) {
            cn = var5;
            co = var6;
            cv = 2;
            cu = 0;
            cV = var1;
            cW = var2;
            var9 = osrs.r.a(osrs.u.j, b.p);
            var9.f.a(am.c(82) ? 1 : 0);
            var9.f.p(var7);
            b.a(var9);
         }
      }

      if (var3 == 12) {
         var7 = var4 & '\uffff';
         var8 = (s)var0.a((long)var7);
         if (var8 != null) {
            cn = var5;
            co = var6;
            cv = 2;
            cu = 0;
            cV = var1;
            cW = var2;
            var9 = osrs.r.a(osrs.u.av, b.p);
            var9.f.n(am.c(82) ? 1 : 0);
            var9.f.b(var7);
            b.a(var9);
         }
      }

      if (var3 == 13) {
         var7 = var4 & '\uffff';
         var8 = (s)var0.a((long)var7);
         if (var8 != null) {
            cn = var5;
            co = var6;
            cv = 2;
            cu = 0;
            cV = var1;
            cW = var2;
            var9 = osrs.r.a(osrs.u.az, b.p);
            var9.f.q(var7);
            var9.f.o(am.c(82) ? 1 : 0);
            b.a(var9);
         }
      }

   }

   public static void d(int var0, int var1, int var2, int var3, int var4) {
      if (var0 < osrs.aU.c) {
         var2 -= osrs.aU.c - var0;
         var0 = osrs.aU.c;
      }

      if (var1 < osrs.aU.a) {
         var3 -= osrs.aU.a - var1;
         var1 = osrs.aU.a;
      }

      if (var0 + var2 > osrs.aU.d) {
         var2 = osrs.aU.d - var0;
      }

      if (var1 + var3 > osrs.aU.b) {
         var3 = osrs.aU.b - var1;
      }

      int var5 = osrs.aU.f - var2;
      int var6 = osrs.aU.f * var1 + var0;

      for(int var7 = -var3; var7 < 0; ++var7) {
         for(int var8 = -var2; var8 < 0; ++var8) {
            osrs.aU.h[var6++] = 0;
         }

         var6 += var5;
      }

   }

   public FriendContainer getFriendContainer() {
      return this.bf();
   }

   public static void b(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7) {
      int var8;
      r var9;
      if (var2 == 18) {
         cn = var4;
         co = var5;
         cv = 2;
         cu = 0;
         cV = var0;
         cW = var1;
         var8 = var3 & '\uffff';
         var9 = osrs.r.a(osrs.u.aU, b.p);
         var9.f.r(var0 + var6);
         var9.f.q(var1 + var7);
         var9.f.q(var8);
         var9.f.n(am.c(82) ? 1 : 0);
         b.a(var9);
      }

      if (var2 == 19) {
         cn = var4;
         co = var5;
         cv = 2;
         cu = 0;
         cV = var0;
         cW = var1;
         var8 = var3 & '\uffff';
         var9 = osrs.r.a(osrs.u.aW, b.p);
         var9.f.p(var8);
         var9.f.n(am.c(82) ? 1 : 0);
         var9.f.r(var1 + var7);
         var9.f.p(var0 + var6);
         b.a(var9);
      }

      if (var2 == 20) {
         cn = var4;
         co = var5;
         cv = 2;
         cu = 0;
         cV = var0;
         cW = var1;
         var8 = var3 & '\uffff';
         var9 = osrs.r.a(osrs.u.aO, b.p);
         var9.f.a(am.c(82) ? 1 : 0);
         var9.f.q(var1 + var7);
         var9.f.q(var8);
         var9.f.r(var0 + var6);
         b.a(var9);
      }

      if (var2 == 21) {
         cn = var4;
         co = var5;
         cv = 2;
         cu = 0;
         cV = var0;
         cW = var1;
         var8 = var3 & '\uffff';
         var9 = osrs.r.a(osrs.u.bi, b.p);
         var9.f.m(am.c(82) ? 1 : 0);
         var9.f.p(var1 + var7);
         var9.f.b(var0 + var6);
         var9.f.b(var8);
         b.a(var9);
      }

      if (var2 == 22) {
         cn = var4;
         co = var5;
         cv = 2;
         cu = 0;
         cV = var0;
         cW = var1;
         var8 = var3 & '\uffff';
         var9 = osrs.r.a(osrs.u.O, b.p);
         var9.f.p(var8);
         var9.f.p(var0 + var6);
         var9.f.o(am.c(82) ? 1 : 0);
         var9.f.b(var1 + var7);
         b.a(var9);
      }

   }

   public static void c(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7) {
      int var8;
      int var9;
      r var10;
      if (var2 == 18) {
         cn = var4;
         co = var5;
         cv = 2;
         cu = 0;
         cV = var0;
         cW = var1;
         var8 = var3 & '\uffff';
         var9 = var3 >> 16;
         var10 = osrs.r.a(osrs.u.v, b.p);
         var10.f.m(var9);
         var10.f.m(am.c(82) ? 1 : 0);
         var10.f.p(var1 + var7);
         var10.f.p(var8);
         var10.f.p(var0 + var6);
         b.a(var10);
      }

      if (var2 == 19) {
         cn = var4;
         co = var5;
         cv = 2;
         cu = 0;
         cV = var0;
         cW = var1;
         var8 = var3 & '\uffff';
         var9 = var3 >> 16;
         var10 = osrs.r.a(osrs.u.T, b.p);
         var10.f.a(am.c(82) ? 1 : 0);
         var10.f.b(var8);
         var10.f.p(var0 + var6);
         var10.f.o(var9);
         var10.f.b(var1 + var7);
         b.a(var10);
      }

      if (var2 == 20) {
         cn = var4;
         co = var5;
         cv = 2;
         cu = 0;
         cV = var0;
         cW = var1;
         var8 = var3 & '\uffff';
         var9 = var3 >> 16;
         var10 = osrs.r.a(osrs.u.aN, b.p);
         var10.f.n(var9);
         var10.f.a(am.c(82) ? 1 : 0);
         var10.f.r(var0 + var6);
         var10.f.r(var1 + var7);
         var10.f.p(var8);
         b.a(var10);
      }

      if (var2 == 21) {
         cn = var4;
         co = var5;
         cv = 2;
         cu = 0;
         cV = var0;
         cW = var1;
         var8 = var3 & '\uffff';
         var9 = var3 >> 16;
         var10 = osrs.r.a(osrs.u.aI, b.p);
         var10.f.o(var9);
         var10.f.p(var0 + var6);
         var10.f.b(var1 + var7);
         var10.f.n(am.c(82) ? 1 : 0);
         var10.f.b(var8);
         b.a(var10);
      }

      if (var2 == 22) {
         cn = var4;
         co = var5;
         cv = 2;
         cu = 0;
         cV = var0;
         cW = var1;
         var8 = var3 & '\uffff';
         var9 = var3 >> 16;
         var10 = osrs.r.a(osrs.u.C, b.p);
         var10.f.n(am.c(82) ? 1 : 0);
         var10.f.p(var1 + var7);
         var10.f.p(var0 + var6);
         var10.f.b(var8);
         var10.f.m(var9);
         b.a(var10);
      }

   }

   public ClanSettings getClanSettings(int var1) {
      cK[] var2 = this.bH();
      return var1 >= 0 && var1 < var2.length ? var2[var1] : null;
   }

   public void draw2010Menu(int var1) {
      a(cJ, (jd)null, var1, osrs.bo.fR);
   }

   public static void d(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7) {
      int var8;
      r var9;
      if (var2 == 3) {
         cn = var4;
         co = var5;
         cv = 2;
         cu = 0;
         cV = var0;
         cW = var1;
         var8 = var3 & '\uffff';
         var9 = osrs.r.a(osrs.u.aK, b.p);
         var9.f.r(var1 + var7);
         var9.f.m(am.c(82) ? 1 : 0);
         var9.f.b(var0 + var6);
         var9.f.r(var8);
         b.a(var9);
      }

      if (var2 == 4) {
         cn = var4;
         co = var5;
         cv = 2;
         cu = 0;
         cV = var0;
         cW = var1;
         var8 = var3 & '\uffff';
         var9 = osrs.r.a(osrs.u.bd, b.p);
         var9.f.n(am.c(82) ? 1 : 0);
         var9.f.q(var0 + var6);
         var9.f.r(var1 + var7);
         var9.f.r(var8);
         b.a(var9);
      }

      if (var2 == 5) {
         cn = var4;
         co = var5;
         cv = 2;
         cu = 0;
         cV = var0;
         cW = var1;
         var8 = var3 & '\uffff';
         var9 = osrs.r.a(osrs.u.at, b.p);
         var9.f.b(var1 + var7);
         var9.f.p(var0 + var6);
         var9.f.b(var8);
         var9.f.o(am.c(82) ? 1 : 0);
         b.a(var9);
      }

      if (var2 == 6) {
         cn = var4;
         co = var5;
         cv = 2;
         cu = 0;
         cV = var0;
         cW = var1;
         var8 = var3 & '\uffff';
         var9 = osrs.r.a(osrs.u.aw, b.p);
         var9.f.r(var8);
         var9.f.p(var1 + var7);
         var9.f.a(am.c(82) ? 1 : 0);
         var9.f.b(var0 + var6);
         b.a(var9);
      }

      if (var2 == 1001) {
         cn = var4;
         co = var5;
         cv = 2;
         cu = 0;
         cV = var0;
         cW = var1;
         var8 = var3 & '\uffff';
         var9 = osrs.r.a(osrs.u.r, b.p);
         var9.f.r(var8);
         var9.f.o(am.c(82) ? 1 : 0);
         var9.f.q(var1 + var7);
         var9.f.r(var0 + var6);
         b.a(var9);
      }

   }

   public static void a(bG var0, bu var1, int var2) {
      if (!a && !var0.isTopLevel()) {
         throw new AssertionError();
      } else {
         osrs.bo.eK = false;
      }
   }

   public static void b(fO var0, int var1, int var2, int var3, int var4, int var5, int var6) {
      int var7;
      s var8;
      int var9;
      r var10;
      if (var3 == 9) {
         var7 = var4 & '\uffff';
         var8 = (s)var0.a((long)var7);
         if (var8 != null) {
            cn = var5;
            co = var6;
            cv = 2;
            cu = 0;
            cV = var1;
            cW = var2;
            var9 = var4 >> 16;
            var10 = osrs.r.a(osrs.u.ar, b.p);
            var10.f.p(var7);
            var10.f.n(am.c(82) ? 1 : 0);
            var10.f.a(var9);
            b.a(var10);
         }
      }

      if (var3 == 10) {
         var7 = var4 & '\uffff';
         var8 = (s)var0.a((long)var7);
         if (var8 != null) {
            cn = var5;
            co = var6;
            cv = 2;
            cu = 0;
            cV = var1;
            cW = var2;
            var9 = var4 >> 16;
            var10 = osrs.r.a(osrs.u.bj, b.p);
            var10.f.m(am.c(82) ? 1 : 0);
            var10.f.m(var9);
            var10.f.r(var7);
            b.a(var10);
         }
      }

      if (var3 == 11) {
         var7 = var4 & '\uffff';
         var8 = (s)var0.a((long)var7);
         if (var8 != null) {
            cn = var5;
            co = var6;
            cv = 2;
            cu = 0;
            cV = var1;
            cW = var2;
            var9 = var4 >> 16;
            var10 = osrs.r.a(osrs.u.aQ, b.p);
            var10.f.n(am.c(82) ? 1 : 0);
            var10.f.m(var9);
            var10.f.r(var7);
            b.a(var10);
         }
      }

      if (var3 == 12) {
         var7 = var4 & '\uffff';
         var8 = (s)var0.a((long)var7);
         if (var8 != null) {
            cn = var5;
            co = var6;
            cv = 2;
            cu = 0;
            cV = var1;
            cW = var2;
            var9 = var4 >> 16;
            var10 = osrs.r.a(osrs.u.ay, b.p);
            var10.f.n(var9);
            var10.f.p(var7);
            var10.f.n(am.c(82) ? 1 : 0);
            b.a(var10);
         }
      }

      if (var3 == 13) {
         var7 = var4 & '\uffff';
         var8 = (s)var0.a((long)var7);
         if (var8 != null) {
            cn = var5;
            co = var6;
            cv = 2;
            cu = 0;
            cV = var1;
            cW = var2;
            var9 = var4 >> 16;
            var10 = osrs.r.a(osrs.u.aS, b.p);
            var10.f.n(var9);
            var10.f.q(var7);
            var10.f.o(am.c(82) ? 1 : 0);
            b.a(var10);
         }
      }

   }

   public static void a(int var0, String var1) {
      int var2 = G.j;
      int[] var3 = G.a;
      boolean var4 = false;
      I var5 = new I(var1, osrs.bo.cp);
      bG var6 = D.b(Q);
      bG var7 = var6;
      if (var6 == null) {
         iw.a(4, "", osrs.bv.cl + var1 + ", current world null.");
      } else {
         for(int var8 = 0; var8 < var2; ++var8) {
            t var9 = (t)var7.r.a((long)var3[var8]);
            if (var9 != null && P != var9.j && var9.aP != null && var9.aP.equals(var5)) {
               r var10;
               if (var0 == 1) {
                  var10 = osrs.r.a(osrs.u.as, b.p);
                  var10.f.m(0);
                  var10.f.p(var3[var8]);
                  b.a(var10);
               } else if (var0 == 4) {
                  var10 = osrs.r.a(osrs.u.A, b.p);
                  var10.f.q(var3[var8]);
                  var10.f.a(0);
                  b.a(var10);
               } else if (var0 == 6) {
                  var10 = osrs.r.a(osrs.u.ah, b.p);
                  var10.f.p(var3[var8]);
                  var10.f.m(0);
                  b.a(var10);
               } else if (var0 == 7) {
                  var10 = osrs.r.a(osrs.u.V, b.p);
                  var10.f.a(0);
                  var10.f.q(var3[var8]);
                  b.a(var10);
               }

               var4 = true;
               break;
            }
         }

         if (!var4) {
            iw.a(4, "", osrs.bv.cl + var1);
         }
      }

   }

   public static void a(int var0, int var1, int var2, int var3, String var4, String var5) {
      ag var6 = osrs.bo.cR.a(var0, var1);
      if (var6 != null && var6.bg != null) {
         ac var7 = osrs.ac.a(var6).a(var6.bg).a();
         osrs.bh.a(var7);
      }

      cP = var3;
      cN = true;
      osrs.bo.cX = var0;
      cO = var1;
      osrs.bo.l = var2;
      cQ = var4;
      cR = var5;
   }

   public static void Q() {
      if (cN) {
         ag var0 = osrs.bo.cR.a(osrs.bo.cX, cO);
         if (var0 != null && var0.bi != null) {
            ac var1 = osrs.ac.a(var0).a(var0.bi).a();
            osrs.bh.a(var1);
         }

         cP = -1;
         cN = false;
      }

   }

   public static void e(int var0, int var1) {
      r var2 = osrs.r.a(osrs.u.aV, b.p);
      var2.f.b(var1);
      var2.f.u(var0);
      b.a(var2);
   }

   public static void a(int var0, int var1, int var2, int var3, String var4) {
      int var5 = var0 >>> 16;
      int var6 = var0 & '\uffff';
      ag var7 = osrs.bo.cR.a(var1, var2);
      if (var7 != null) {
         if (var7.br != null) {
            ac var8 = osrs.ac.a(var7).a(var7.br).d(var6).e(var5).a(var4).a();
            osrs.bh.a(var8);
         }

         boolean var10 = true;
         if (var7.l > 0) {
            var10 = b(var7);
         }

         if (var10 && osrs.bl.a(osrs.bo.cR.d(var7), var6 - 1)) {
            r var9;
            if (var5 == 0) {
               var9 = osrs.r.a(osrs.u.aL, b.p);
               var9.f.d(var1);
               var9.f.b(var2);
               var9.f.b(var3);
               var9.f.a(var6);
               b.a(var9);
            } else {
               var9 = osrs.r.a(osrs.u.aM, b.p);
               var9.f.d(var1);
               var9.f.b(var2);
               var9.f.b(var3);
               var9.f.a(var6);
               var9.f.a(var5 - 1);
               b.a(var9);
            }
         }
      }

   }

   public static void a(int var0, int var1, int var2, int var3, Object[] var4) {
      boolean var5 = false;
      ag var6 = osrs.bo.cR.a(var0, var1);
      if (var5 || var6 != null) {
         if (!var5) {
            int var7 = osrs.bo.cR.i(var6);
            boolean var8 = (var7 >> 23 & 1) != 0;
            if (!var8) {
               return;
            }
         }

         b(var0, var1, var2, var3, var4);
      }

   }

   public void sendIfScriptTrigger(int var1, int var2, int var3, Object... var4) {
      int var5 = -1;

      try {
         ag var6 = osrs.bo.cR.a(var1, var2);
         if (var6 != null) {
            var5 = var6.g;
         }
      } catch (RuntimeException var7) {
      }

      b(var1, var2, var5, var3, b(var4));
   }

   private static Object[] b(Object[] var0) {
      if (var0 == null) {
         return new Object[0];
      } else {
         Object[] var1 = new Object[var0.length];

         for(int var2 = 0; var2 < var0.length; ++var2) {
            Object var3 = var0[var2];
            if (var3 != null && !(var3 instanceof Integer) && !(var3 instanceof String) && !(var3 instanceof be)) {
               if (var3 instanceof Boolean) {
                  var1[var2] = (Boolean)var3 ? 1 : 0;
               } else if (var3 instanceof int[]) {
                  var1[var2] = osrs.be.a((int[])var3);
               } else {
                  if (!(var3 instanceof String[])) {
                     throw new IllegalArgumentException("Unsupported IF script trigger argument type: " + var3.getClass().getName());
                  }

                  var1[var2] = osrs.be.a((String[])var3);
               }
            } else {
               var1[var2] = var3;
            }
         }

         return var1;
      }
   }

   private static void b(int var0, int var1, int var2, int var3, Object[] var4) {
      r var5 = osrs.r.a(osrs.u.B, b.p);
      var5.f.b(0);
      int var6 = var5.f.d;
      var5.f.r(var1);
      var5.f.u(var0);
      var5.f.t(var3);
      var5.f.r(var2);
      Object[] var7 = var4 == null ? new Object[0] : var4;

      for(int var8 = 0; var8 < var7.length; ++var8) {
         Object var9 = var7[var8];
         if (var9 instanceof Integer) {
            var5.f.k((Integer)var9);
         } else if (var9 instanceof String) {
            var5.f.c((String)var9);
         } else if (var9 instanceof be) {
            be var10 = (be)var9;
            int var11 = var10.d();
            var5.f.h(var11);
            int var13;
            if (osrs.i.a == var10.a) {
               int[] var14 = var10.a();

               for(var13 = 0; var13 < var11; ++var13) {
                  var5.f.k(var14[var13]);
               }
            } else {
               if (osrs.i.c != var10.a) {
                  throw new IllegalArgumentException("Unsupported IF script trigger array type");
               }

               Object[] var12 = var10.c();

               for(var13 = 0; var13 < var11; ++var13) {
                  var5.f.c((String)var12[var13]);
               }
            }
         } else {
            if (var9 != null) {
               throw new IllegalArgumentException("Unsupported IF script trigger argument type: " + var9.getClass().getName());
            }

            var5.f.a(0);
         }
      }

      var5.f.f(var5.f.d - var6);
      b.a(var5);
   }

   public static void a(ag var0, ag var1) {
      if (var1 != null && osrs.bo.cR.e(var0) != null) {
         r var2 = osrs.r.a(osrs.u.aq, b.p);
         var2.f.q(var1.as);
         var2.f.q(var0.as);
         var2.f.u(var0.T);
         var2.f.u(var1.T);
         var2.f.p(var0.g);
         var2.f.q(var1.g);
         b.a(var2);
      }

   }

   public static void a(ag var0) {
      if (var0 == null) {
         an.b();
      } else {
         aj var1 = var0.l();
         if (var1 != null && var1.o()) {
            an.a(var0);
            var1.a(true);
         }
      }

   }

   public static final int a(String var0, String var1, int var2, int var3, int var4, int var5) {
      return a(var0, var1, var2, var3, var4, var5, -1, false, 0);
   }

   public static final int a(String var0, String var1, int var2, int var3, int var4, int var5, int var6) {
      return a(var0, var1, var2, var3, var4, var5, var6, false, 0);
   }

   public static final int a(String var0, String var1, int var2, int var3, int var4, int var5, int var6, boolean var7, int var8) {
      if (cI) {
         return -1;
      } else {
         return !f(var8, var2) ? -1 : cJ.a(var0, var1, var2, var3, var4, var5, var6, var7, var8);
      }
   }

   public static final void a(int var0, String var1, String var2, int var3, int var4, int var5, int var6, int var7, int var8) {
      if (!cI && f(var8, var3) && var0 >= 0 && var0 < cJ.q.length) {
         if (cJ.q[var0] == null) {
            cJ.q[var0] = new ig(false);
         }

         cJ.q[var0].a(var1, var2, var3, var4, var5, var6, var7, false, var8);
      }

   }

   public static boolean f(int var0, int var1) {
      if (var1 >= 2000) {
         var1 -= 2000;
      }

      hp var2 = D.f(var0);
      gZ var3 = (gZ)D.a().t.a((long)var0);
      boolean var4 = var0 == 0;
      boolean var5 = Q == var0;
      boolean var6 = false;
      if (var3 != null) {
         var6 = var3.f() != fy.a;
         if (!var5) {
            fk var7 = var3.o.j();
            if (kz.a(var1) && !var7.g) {
               return false;
            }

            boolean var8 = kz.c(var1) || kz.d(var1);
            boolean var9;
            if (!var8) {
               var9 = var1 == 16 || var1 == 17 || var1 >= 18 && var1 <= 22;
               var8 = var9;
            }

            var9 = var8 || kz.f(var1);
            if (var9 && !var7.h) {
               return false;
            }
         }
      }

      if (kz.c(var1) && !var2.a(var6, var4, var5)) {
         return false;
      } else if (kz.e(var1) && !var2.b(var6, var4, var5)) {
         return false;
      } else if (kz.d(var1) && !var2.c(var6, var4, var5)) {
         return false;
      } else if (kz.b(var1) && !var2.d(var6, var4, var5)) {
         return false;
      } else if (var1 == 1002 && !var2.e(var6, var4, var5)) {
         return false;
      } else if (var1 == 1004 && !var2.f(var6, var4, var5)) {
         return false;
      } else {
         return var1 != 1003 || var2.g(var6, var4, var5);
      }
   }

   public static void B(int var0) {
      if (var0 == -1) {
         osrs.bo.eQ = new int[osrs.x.c.length];
      } else {
         int var1 = osrs.bo.eQ[var0];
         int var2 = osrs.x.c[var0];
         int var3 = var1 ^ var2;
         int var4 = Arrays.binarySearch(osrs.bo.fz, var0 << 16);
         if (var4 < 0) {
            var4 = -var4 - 1;
         }

         for(; var4 < osrs.bo.fz.length; ++var4) {
            int var5 = osrs.bo.fz[var4] >> 16;
            if (var0 != var5) {
               break;
            }

            int var6 = osrs.bo.fz[var4] & '\uffff';
            int var7 = osrs.bo.fS[var6] >> 8 & 255;
            int var8 = osrs.bo.fS[var6] & 255;
            int var9 = osrs.x.a[var7 - var8] << var8;
            if (((var1 ^ var2) & var9) != 0) {
               var3 &= ~var9;
               int var10 = (var2 & var9) >>> var8;
               VarbitChanged var11 = new VarbitChanged();
               var11.setVarpId(var0);
               var11.setVarbitId(var6);
               var11.setValue(var10);
               s.getCallbacks().post(var11);
            }
         }

         if (var3 != 0) {
            VarbitChanged var12 = new VarbitChanged();
            var12.setVarpId(var0);
            var12.setValue(var2);
            s.getCallbacks().post(var12);
         }

         osrs.bo.eQ[var0] = var2;
      }

   }

   public static void R() {
      int var0 = cJ.h - 1;
      if (var0 != -1) {
         osrs.bo.i(var0);
      }

   }

   public static void b(bG var0, bu var1, int var2) {
      if (!a && !var0.isTopLevel()) {
         throw new AssertionError();
      } else {
         ds = new ko();
         osrs.bo.eK = true;
      }
   }

   public Dimension getStretchedDimensions() {
      if (osrs.bo.fB == null) {
         Container var1 = this.getCanvas().getParent();
         int var2 = var1.getWidth();
         int var3 = var1.getHeight();
         Dimension var4 = this.getRealDimensions();
         if (osrs.bo.fy) {
            double var5 = var4.getWidth() / var4.getHeight();
            int var7 = (int)((double)var3 * var5);
            if (var7 > var2) {
               var3 = (int)((double)var2 / var5);
            } else {
               var2 = var7;
            }
         }

         if (osrs.bo.fr) {
            if (var2 > var4.width) {
               var2 -= var2 % var4.width;
            }

            if (var3 > var4.height) {
               var3 -= var3 % var4.height;
            }
         }

         osrs.bo.fB = new Dimension(var2, var3);
      }

      return osrs.bo.fB;
   }

   public static int g(int var0, int var1) {
      return var0 << 16 | var1;
   }

   public static void a(int var0, L var1, int var2, String var3, int var4, int var5, int var6, int var7, int var8) {
      if (var0 != -1) {
         int var9 = var1.b(var2);

         int var10;
         int var11;
         String var13;
         for(var10 = var9 - 1; var10 >= 0; --var10) {
            var11 = var1.a(var2, var10);
            int var12 = g(var11, var5);
            var13 = var1.c(var2, var10, osrs.x.c);
            if (var13 != null) {
               a(var0, var13, var3, var4, var12, var6, var7, -1, var8);
            }
         }

         if (m) {
            for(var10 = 1; var10 < 26; var10 += 5) {
               var11 = g(var10, var5);
               Object var14 = null;
               if (var14 == null) {
                  var13 = var10 + 1 + ": TEST";
               } else {
                  var13 = var10 + 1 + ": " + String.valueOf(var14);
               }

               a(var0, var13, var3, var4, var11, var6, var7, -1, var8);
            }
         }
      }

   }

   public int get3dZoom() {
      return osrs.aW.h.d();
   }

   public static int a(int var0, gZ var1, bG var2) {
      int var3 = -1;
      if (Q == var0) {
         var3 = var2.y;
      } else if (Q == 0) {
         var3 = var1.o.d();
      } else {
         gZ var4 = (gZ)osrs.bo.aQ.t.a((long)Q);
         if (var4 != null) {
            bG var5 = var4.n;
            if (var5.y == var4.o.d()) {
               if (var0 == 0) {
                  var3 = var4.m();
               } else {
                  var3 = var1.o.d();
               }
            }
         }
      }

      return var3;
   }

   public static void a(int var0, aN var1, int var2, boolean var3, s var4, String var5, int var6, int var7, int var8, int var9) {
      if (var1.a().b()) {
         for(int var10 = 4; var10 >= 0; --var10) {
            if (m || !osrs.bo.cj && var4.h(var10)) {
               String var11 = var1.a().b(var10, osrs.x.c);
               if ((!var3 || var11 != null && var11.equalsIgnoreCase(osrs.bv.co)) && (var3 || var11 == null || !var11.equalsIgnoreCase(osrs.bv.co))) {
                  if (m) {
                     String var12;
                     if (var11 == null) {
                        var12 = var10 + 1 + ": TEST";
                     } else {
                        var12 = var10 + 1 + ": " + var11;
                     }

                     var11 = var12;
                  }

                  if (var11 != null && (!var3 || bT != iA.d)) {
                     if (var3 && bT == iA.b || bT == iA.a && var1.r > var2) {
                        var0 = 2000;
                     }

                     int var14 = 0;
                     if (var10 == 0) {
                        var14 = var0 + 9;
                     }

                     if (var10 == 1) {
                        var14 = var0 + 10;
                     }

                     if (var10 == 2) {
                        var14 = var0 + 11;
                     }

                     if (var10 == 3) {
                        var14 = var0 + 12;
                     }

                     if (var10 == 4) {
                        var14 = var0 + 13;
                     }

                     int var13 = a(var11, var5, var14, var6, var7, var8, -1, false, var9);
                     a(var13, var1.a(), var10, var5, var14, var6, var7, var8, var9);
                  }
               }
            }
         }
      }

   }

   public static final String h(int var0, int var1) {
      int var2 = var1 - var0;
      if (var2 < -9) {
         return osrs.bq.b(16711680);
      } else if (var2 < -6) {
         return osrs.bq.b(16723968);
      } else if (var2 < -3) {
         return osrs.bq.b(16740352);
      } else if (var2 < 0) {
         return osrs.bq.b(16756736);
      } else if (var2 > 9) {
         return osrs.bq.b(65280);
      } else if (var2 > 6) {
         return osrs.bq.b(4259584);
      } else if (var2 > 3) {
         return osrs.bq.b(8453888);
      } else {
         return var2 > 0 ? osrs.bq.b(12648192) : osrs.bq.b(16776960);
      }
   }

   public static int a(ag var0, int var1) {
      if (var0.bB != null && var1 < var0.bB.length) {
         try {
            int[] var2 = var0.bB[var1];
            int var3 = 0;
            int var4 = 0;
            byte var5 = 0;

            while(true) {
               int var6 = var2[var4++];
               int var7 = 0;
               byte var8 = 0;
               if (var6 == 0) {
                  return var3;
               }

               if (var6 == 1) {
                  var7 = Y[var2[var4++]];
               }

               if (var6 == 2) {
                  var7 = Z[var2[var4++]];
               }

               if (var6 == 3) {
                  var7 = aa[var2[var4++]];
               }

               int var9;
               int var10;
               ag var11;
               int var12;
               int var13;
               if (var6 == 4) {
                  var9 = var2[var4++] << 16;
                  var10 = var9 + var2[var4++];
                  var11 = osrs.bo.cR.a(var10);
                  var12 = var2[var4++];
                  if (var12 != -1 && (!osrs.aO.a(var12).B || v)) {
                     for(var13 = 0; var13 < var11.bd.length; ++var13) {
                        if (var12 + 1 == var11.bd[var13]) {
                           var7 += var11.bC[var13];
                        }
                     }
                  }
               }

               if (var6 == 5) {
                  var7 = osrs.x.c[var2[var4++]];
               }

               if (var6 == 6) {
                  var7 = fn.b[Z[var2[var4++]] - 1];
               }

               if (var6 == 7) {
                  var7 = osrs.x.c[var2[var4++]] * 100 / '뜛';
               }

               t var15;
               if (var6 == 8) {
                  var15 = J();
                  var7 = var15 == null ? 0 : var15.aG;
               }

               if (var6 == 9) {
                  for(var9 = 0; var9 < 25; ++var9) {
                     if (fn.a[var9]) {
                        var7 += Z[var9];
                     }
                  }
               }

               if (var6 == 10) {
                  var9 = var2[var4++] << 16;
                  var10 = var9 + var2[var4++];
                  var11 = osrs.bo.cR.a(var10);
                  var12 = var2[var4++];
                  if (var12 != -1 && (!osrs.aO.a(var12).B || v)) {
                     for(var13 = 0; var13 < var11.bd.length; ++var13) {
                        if (var12 + 1 == var11.bd[var13]) {
                           var7 = 999999999;
                           break;
                        }
                     }
                  }
               }

               if (var6 == 11) {
                  var7 = bw;
               }

               if (var6 == 12) {
                  var7 = bx;
               }

               if (var6 == 13) {
                  var9 = osrs.x.c[var2[var4++]];
                  var10 = var2[var4++];
                  var7 = (var9 & 1 << var10) != 0 ? 1 : 0;
               }

               if (var6 == 14) {
                  var9 = var2[var4++];
                  var7 = osrs.x.a(var9);
               }

               if (var6 == 15) {
                  var8 = 1;
               }

               if (var6 == 16) {
                  var8 = 2;
               }

               if (var6 == 17) {
                  var8 = 3;
               }

               if (var6 == 18) {
                  var15 = J();
                  var7 = (var15.t >> 7) + osrs.bo.aQ.B;
               }

               if (var6 == 19) {
                  var15 = J();
                  var7 = (var15.ai >> 7) + osrs.bo.aQ.z;
               }

               if (var6 == 20) {
                  var7 = var2[var4++];
               }

               if (var8 == 0) {
                  if (var5 == 0) {
                     var3 += var7;
                  }

                  if (var5 == 1) {
                     var3 -= var7;
                  }

                  if (var5 == 2 && var7 != 0) {
                     var3 /= var7;
                  }

                  if (var5 == 3) {
                     var3 *= var7;
                  }

                  var5 = 0;
               } else {
                  var5 = var8;
               }
            }
         } catch (Exception var14) {
            return -1;
         }
      } else {
         return -2;
      }
   }

   public static void a(boolean var0) {
      cK = var0;
   }

   public static boolean S() {
      return cK;
   }

   public static final int T() {
      float var0 = 200.0F * ((float)bL.i() - 0.5F);
      return 100 - Math.round(var0);
   }

   public aV[] a(IndexDataBase var1, int var2, int var3) {
      if (!s.isClientThread()) {
         if (!osrs.T.eo) {
            throw new AssertionError("must be called on client thread");
         } else {
            throw new IllegalStateException("must be called on client thread");
         }
      } else {
         au var4 = (au)var1;
         byte[] var5 = var4.loadData(var2, var3);
         if (var5 == null) {
            return null;
         } else {
            this.b(var5);
            int var6 = osrs.bo.ex;
            int var7 = osrs.bo.cx;
            int[] var8 = osrs.bo.eA;
            int[] var9 = osrs.bo.p;
            int[] var10 = osrs.bo.ea;
            int[] var11 = osrs.bo.ez;
            byte[][] var12 = osrs.bo.bh;
            aV[] var13 = new aV[osrs.bo.ew];

            for(int var14 = 0; var14 < osrs.bo.ew; ++var14) {
               int var15 = var10[var14];
               int var16 = var11[var14];
               byte[] var17 = var12[var14];
               int[] var18 = new int[var15 * var16];
               aV var19 = new aV(var18, var15, var16);
               var19.setMaxHeight(var7);
               var19.setMaxWidth(var6);
               var19.setOffsetX(var8[var14]);
               var19.setOffsetY(var9[var14]);

               for(int var20 = 0; var20 < var15 * var16; ++var20) {
                  var18[var20] = osrs.bo.dW[var17[var20] & 255];
               }

               var13[var14] = var19;
            }

            osrs.bo.p = null;
            osrs.bo.eA = null;
            osrs.bo.ez = null;
            osrs.bo.ea = null;
            osrs.bo.dW = null;
            osrs.bo.bh = null;
            return var13;
         }
      }
   }

   public static final void C(int var0) {
      int var1 = Math.max(Math.min(var0, 100), 0);
      int var2 = 100 - var1;
      float var3 = (float)var2 / 200.0F + 0.5F;
      a((double)var3);
   }

   public static final void a(double var0) {
      osrs.aW.a(var0);
      ((dO)osrs.aW.h.c).a(var0);
      if (dU != null) {
         dU.h();
      }

      osrs.aO.b();
      bL.a(var0);
   }

   public static void U() {
      int var0;
      for(var0 = cJ.h - 1; var0 >= 0 && cJ.k[var0] != MenuAction.WALK.getId(); --var0) {
      }

      if (var0 >= 0) {
         for(int var1 = cJ.h - 1; var1 >= 0; --var1) {
            int var2 = cJ.k[var1];
            if (var2 >= 2000) {
               var2 -= 2000;
            }

            if (var2 == MenuAction.PLAYER_FIRST_OPTION.getId() || var2 == MenuAction.PLAYER_SECOND_OPTION.getId() || var2 == MenuAction.PLAYER_THIRD_OPTION.getId() || var2 == MenuAction.PLAYER_FOURTH_OPTION.getId() || var2 == MenuAction.PLAYER_FIFTH_OPTION.getId() || var2 == MenuAction.PLAYER_SIXTH_OPTION.getId() || var2 == MenuAction.PLAYER_SEVENTH_OPTION.getId() || var2 == MenuAction.PLAYER_EIGHTH_OPTION.getId()) {
               int var3 = cJ.l[var1];
               cJ.l[var0] = var3 + 1;
               break;
            }
         }
      }

   }

   public static final void V() {
      X();
      Y();
      Z();
   }

   public void W() {
      jc var1 = osrs.bo.fu;
      if (var1 != null) {
         if (!a && osrs.bo.eX != null) {
            throw new AssertionError();
         }

         osrs.bo.fu = null;
         if (!a && this.bL() != 25) {
            throw new AssertionError();
         }

         a(var1);
         this.setGameState(GameState.LOGGED_IN);
      }

   }

   public static final void X() {
      osrs.bo.ag.a(bL.j(), bL.q());
      int var0 = osrs.bo.ag.c();
      if (var0 == 0) {
         osrs.eZ.a(0, 0);
         bH = false;
      } else if (var0 == 0 && osrs.eZ.a()) {
         osrs.eZ.a(osrs.bo.cL, var0);
         bH = false;
      } else if (!osrs.eZ.d.isEmpty()) {
         Iterator var1 = osrs.eZ.d.iterator();

         eX var2;
         while(var1.hasNext()) {
            var2 = (eX)var1.next();
            if (var2 != null) {
               var2.c = var0;
            }
         }

         var2 = (eX)osrs.eZ.d.get(0);
         if (var2 != null && var2.j != null && var2.j.f() && !var2.f) {
            var2.j.a(var0);
            var2.d = (float)var0;
         }
      }

   }

   public static final void Y() {
      osrs.bo.ag.b(bL.l(), bL.q());
   }

   public static final void Z() {
      osrs.bo.ag.c(bL.k(), bL.q());
   }

   public static final int aa() {
      return osrs.bo.ag.c();
   }

   public static final int ab() {
      return osrs.bo.ag.a();
   }

   public static final int ac() {
      return osrs.bo.ag.b();
   }

   public im a(InventoryID var1) {
      if (!s.isClientThread()) {
         if (!osrs.T.eo) {
            throw new AssertionError("must be called on client thread");
         } else {
            throw new IllegalStateException("must be called on client thread");
         }
      } else {
         fT var2 = this.bE();
         return (im)var2.b((long)var1.getId());
      }
   }

   public NodeCache getAnimationCache() {
      return this.bG();
   }

   public void setShouldRenderLoginScreenFire(boolean var1) {
      osrs.bz.n = var1;
   }

   public static void a(bG var0, aV var1, double var2, int var4, int var5, int var6, int var7, int var8) {
      int[] var9 = var1.getPixels();
      Arrays.fill(var9, 1);
      var1.l();
      osrs.df.i();
      osrs.aW.h.q = true;
      osrs.aW.h.a = 0;
      osrs.bo.fA = var1;
      osrs.bo.d = var2;
      osrs.bo.b = var5;
      osrs.bo.fO = var6;
      osrs.bo.eY = var7;
      osrs.bo.fJ = var8;

      try {
         a(var0, var4, var5, var6, var7, var8, k);
         a(var0, var4, var5, var6, var7, var8, Client::a);
      } finally {
         osrs.df.f();
         osrs.df.i();
      }

   }

   public VarbitComposition getVarbit(int var1) {
      if (!s.isClientThread()) {
         if (!osrs.T.eo) {
            throw new AssertionError("must be called on client thread");
         } else {
            throw new IllegalStateException("must be called on client thread");
         }
      } else {
         return this.Y(var1);
      }
   }

   public static final void ad() {
      r var0 = osrs.r.a(osrs.u.p, b.p);
      b.a(var0);
      osrs.bh.j = true;

      for(bt var1 = (bt)osrs.bo.cR.x.b(); var1 != null; var1 = (bt)osrs.bo.cR.x.c()) {
         if (var1.b == 0 || var1.b == 3) {
            osrs.bo.cv.a(var1, true, osrs.bo.cR);
         }
      }

      osrs.bo.cR.h();
      osrs.bh.j = false;
   }

   public static final boolean b(ag var0) {
      int var1 = var0.l;
      if (var1 == 205) {
         bk = 250;
         return true;
      } else {
         int var2;
         int var3;
         if (var1 >= 300 && var1 <= 313) {
            var2 = (var1 - 300) / 2;
            var3 = var1 & 1;
            n.a(var2, var3 == 1);
         }

         if (var1 >= 314 && var1 <= 323) {
            var2 = (var1 - 314) / 2;
            var3 = var1 & 1;
            n.b(var2, var3 == 1);
         }

         if (var1 == 324) {
            n.b(0);
         }

         if (var1 == 325) {
            n.b(1);
         }

         if (var1 == 326) {
            r var4 = osrs.r.a(osrs.u.bc, b.p);
            n.a(var4.f);
            b.a(var4);
            return true;
         } else {
            return false;
         }
      }
   }

   public final boolean a(int var1, boolean var2) {
      boolean var3 = cH && cJ.h > 2;
      if (!var3) {
         int var4 = var1;
         if (var1 >= 2000) {
            var4 = var1 - 2000;
         }

         boolean var5 = var4 == 1007;
         var3 = var5;
      }

      boolean var6 = var3 && !var2;
      if (var6) {
         return true;
      } else {
         MenuShouldLeftClick var7 = new MenuShouldLeftClick();
         s.getCallbacks().post(var7);
         if (var7.isForceRightClick()) {
            return true;
         } else {
            return var1 == MenuAction.RUNELITE_OVERLAY.getId() || var1 == MenuAction.RUNELITE_OVERLAY_CONFIG.getId() || var1 == MenuAction.RUNELITE_INFOBOX.getId() || var1 == MenuAction.RUNELITE_LOW_PRIORITY.getId();
         }
      }
   }

   public static final void ae() {
      Iterator var0 = D.iterator();

      while(var0.hasNext()) {
         bG var1 = (bG)var0.next();

         for(int var2 = 0; var2 < G.j; ++var2) {
            t var3 = (t)var1.r.a((long)G.a[var2]);
            if (var3 != null) {
               var3.J();
            }
         }
      }

      iw.b();
      if (osrs.bo.co != null) {
         osrs.bo.co.b();
      }

   }

   public static final void af() {
      iw.c();
      if (osrs.bo.co != null) {
         osrs.bo.co.c();
      }

   }

   public static final void ag() {
      p.c();
      osrs.bo.cy = true;
   }

   public static final void c(String var0) {
      if (osrs.bo.co != null) {
         r var1 = osrs.r.a(osrs.u.bl, b.p);
         var1.f.a(osrs.aR.a(var0));
         var1.f.c(var0);
         b.a(var1);
      }

   }

   public static final void d(String var0) {
      if (!var0.equals("")) {
         r var1 = osrs.r.a(osrs.u.t, b.p);
         var1.f.a(osrs.aR.a(var0));
         var1.f.c(var0);
         b.a(var1);
      }

   }

   public static final void ah() {
      r var0 = osrs.r.a(osrs.u.t, b.p);
      var0.f.a(0);
      b.a(var0);
   }

   public static final void i(int var0, int var1) {
      cI var2 = var0 >= 0 ? aq[var0] : osrs.bo.z;
      if (var2 != null && var1 >= 0 && var1 < var2.b()) {
         cr var3 = (cr)var2.f.get(var1);
         if (var3.a == -1) {
            String var4 = var3.c.a();
            r var5 = osrs.r.a(osrs.u.J, b.p);
            var5.f.a(3 + osrs.aR.a(var4));
            var5.f.a(var0);
            var5.f.b(var1);
            var5.f.c(var4);
            b.a(var5);
         }
      }

   }

   public static final void j(int var0, int var1) {
      if (aq[var0] != null && var1 >= 0 && var1 < aq[var0].b()) {
         cr var2 = (cr)aq[var0].f.get(var1);
         if (var2.a == -1) {
            r var3 = osrs.r.a(osrs.u.ag, b.p);
            var3.f.a(3 + osrs.aR.a(var2.c.a()));
            var3.f.a(var0);
            var3.f.b(var1);
            var3.f.c(var2.c.a());
            b.a(var3);
         }
      }

   }

   public static final void a(int var0, int var1, boolean var2) {
      if (aq[var0] != null && var1 >= 0 && var1 < aq[var0].b()) {
         cr var3 = (cr)aq[var0].f.get(var1);
         r var4 = osrs.r.a(osrs.u.W, b.p);
         var4.f.a(4 + osrs.aR.a(var3.c.a()));
         var4.f.a(var0);
         var4.f.b(var1);
         var4.f.a(var2);
         var4.f.c(var3.c.a());
         b.a(var4);
      }

   }

   public static String b(String var0, boolean var1) {
      String var2 = var1 ? "https://" : "http://";
      if (aL == 1) {
         var0 = var0 + "-wtrc";
      } else if (aL == 2) {
         var0 = var0 + "-wtqa";
      } else if (aL == 3) {
         var0 = var0 + "-wtwip";
      } else if (aL == 5) {
         var0 = var0 + "-wti";
      } else if (aL == 4) {
         var0 = "local";
      }

      String var3 = "";
      if (aN != null) {
         var3 = "/p=" + aN;
      }

      String var4 = "runescape.com";
      return var2 + var0 + "." + var4 + "/l=" + String.valueOf(osrs.bo.el) + "/a=" + osrs.bo.aN + var3 + "/";
   }

   public static void e(String var0) {
      aN = var0;

      try {
         String var1 = s.l(Integer.toString(18));
         String var2 = s.l(Integer.toString(13));
         String var3 = var1 + "settings=" + var0 + "; version=1; path=/; domain=" + var2;
         String var4;
         if (var0.isEmpty()) {
            var4 = var3 + "; Expires=Thu, 01-Jan-1970 00:00:00 GMT; Max-Age=0";
         } else {
            String var5 = var3 + "; Expires=";
            long var6 = osrs.bd.a() + 94608000000L;
            fm.c.setTime(new Date(var6));
            int var8 = fm.c.get(7);
            int var9 = fm.c.get(5);
            int var10 = fm.c.get(2);
            int var11 = fm.c.get(1);
            int var12 = fm.c.get(11);
            int var13 = fm.c.get(12);
            int var14 = fm.c.get(13);
            String var15 = fm.b[var8 - 1] + ", " + var9 / 10 + var9 % 10 + "-" + fm.a[0][var10] + "-" + var11 + " " + var12 / 10 + var12 % 10 + ":" + var13 / 10 + var13 % 10 + ":" + var14 / 10 + var14 % 10 + " GMT";
            var4 = var5 + var15 + "; Max-Age=94608000";
         }

         Client var17 = s;
         String var18 = "document.cookie=\"" + var4 + "\"";
      } catch (Throwable var16) {
      }

   }

   public boolean isStretchedEnabled() {
      return osrs.bo.fn;
   }

   public ag k(int var1, int var2) {
      kd var3 = osrs.bo.cR;
      if (var3 == null) {
         return null;
      } else {
         ag[][] var4 = var3.m;
         if (var4 != null && var4.length > var1) {
            ag[] var5 = var4[var1];
            return var5 != null && var5.length > var2 ? var5[var2] : null;
         } else {
            return null;
         }
      }
   }

   public static void a(aR var0, int var1) {
      byte[] var2 = var0.c;
      if (bW == null) {
         bW = new byte[24];
      }

      kr.a(var2, var1, bW, 0, 24);
      osrs.U.a(var0, var1);
   }

   public static void a(int var0, int var1, int var2, boolean var3) {
      r var4 = osrs.r.a(osrs.u.ao, b.p);
      var4.f.b(var1);
      var4.f.p(var0);
      var4.f.t(var3 ? ch : 0);
      var4.f.a(var2);
      b.a(var4);
   }

   public static boolean ai() {
      return by >= 2;
   }

   public static void D(int var0) {
      H = var0;
   }

   public static void aj() {
      b.a(osrs.r.a(osrs.u.ae, b.p));
      H = 0;
   }

   public static void ak() {
      if (H == 1) {
         ci = true;
      }

   }

   public static String f(String var0) {
      while(true) {
         if (var0.startsWith("<img=")) {
            int var1 = var0.indexOf(62);
            if (var1 != -1) {
               var0 = var0.substring(var1 + 1);
               continue;
            }
         }

         return var0;
      }
   }

   public static void al() {
      if (osrs.bo.dx != null) {
         aI = x;
         osrs.bo.dx.a();
         Iterator var0 = osrs.bo.aQ.r.iterator();

         while(var0.hasNext()) {
            t var1 = (t)var0.next();
            osrs.bo.dx.a((var1.t >> 7) + osrs.bo.aQ.B, (var1.ai >> 7) + osrs.bo.aQ.z);
         }
      }

   }

   public static void am() {
      bL.e(aO);
   }

   public static void a(aD var0) {
      if (osrs.bo.m != var0) {
         osrs.bo.m = var0;
      }

   }

   public static boolean an() {
      return osrs.bo.cR.f();
   }

   public static void b(boolean var0) {
      cH = var0;
   }

   public static D E(int var0) {
      D var1 = (D)bM.a((long)var0);
      if (var1 == null) {
         var1 = new D(osrs.bo.B, osrs.F.a(var0), osrs.F.b(var0));
         bM.a(var1, (long)var0);
      }

      return var1;
   }

   public static Clipboard ao() {
      return s.bV();
   }

   public static void F(int var0) {
      bk var1 = osrs.bk.b(var0);
      if (var1.c()) {
         int var2 = var1.i;
         cz var3 = (cz)osrs.bk.g.a((long)var2);
         cz var4;
         if (var3 != null) {
            var4 = var3;
         } else {
            cz var5 = osrs.cz.a(osrs.bk.c, osrs.bk.d, var2);
            if (var5 != null) {
               osrs.bk.g.a(var5, (long)var2);
            }

            var4 = var5;
         }

         int var6;
         if (var4 == null) {
            var6 = 2;
         } else {
            var6 = var4.c(485811346) ? 0 : 1;
         }

         if (var6 == 2) {
            dl.add(var1.i);
         }
      }

   }

   public static fK ap() {
      return osrs.bo.eg;
   }

   public final void aq() {
   }

   public final void ar() {
      if (this.cc()) {
         for(int var1 = 0; var1 <= 28; ++var1) {
            String var2 = this.l(Integer.toString(var1));
            if (var2 != null) {
               switch (var1) {
                  case 3:
                     if (var2.equalsIgnoreCase(osrs.bq.a)) {
                        v = true;
                     } else {
                        v = false;
                     }
                     break;
                  case 4:
                     if (aP == -1) {
                        aP = Integer.parseInt(var2);
                     }
                     break;
                  case 5:
                     aK = Integer.parseInt(var2);
                     break;
                  case 6:
                     int var3 = Integer.parseInt(var2);
                     ax var4;
                     if (var3 >= 0 && var3 < osrs.ax.h.length) {
                        var4 = osrs.ax.h[var3];
                     } else {
                        var4 = null;
                     }

                     osrs.bo.el = var4;
                     break;
                  case 7:
                     osrs.bo.ey = osrs.fe.a(Integer.parseInt(var2));
                     break;
                  case 8:
                     if (var2.equalsIgnoreCase(osrs.bq.a)) {
                     }
                     break;
                  case 9:
                     aN = var2;
                     break;
                  case 10:
                     osrs.bo.cZ = (fi)kk.a(osrs.fi.a(), Integer.parseInt(var2));
                     if (osrs.bo.cZ == osrs.fi.f) {
                        osrs.bo.cp = osrs.aM.a;
                     } else {
                        osrs.bo.cp = osrs.aM.i;
                     }
                     break;
                  case 11:
                     osrs.bo.N = var2;
                     break;
                  case 12:
                     u = Integer.parseInt(var2);
                  case 13:
                  case 16:
                  case 18:
                  case 19:
                  case 20:
                  case 23:
                  case 24:
                  default:
                     break;
                  case 14:
                     osrs.bo.aN = Integer.parseInt(var2);
                     break;
                  case 15:
                     aL = Integer.parseInt(var2);
                     break;
                  case 17:
                     osrs.bo.cs = var2;
                     break;
                  case 21:
                     aO = Integer.parseInt(var2);
                     break;
                  case 22:
                     osrs.bo.cS = var2;
                     break;
                  case 25:
                     int var5 = var2.indexOf(".");
                     if (var5 == -1) {
                        kt.a = Integer.parseInt(var2);
                     } else {
                        kt.a = Integer.parseInt(var2.substring(0, var5));
                        Integer.parseInt(var2.substring(var5 + 1));
                     }
               }
            }
         }

         osrs.dh.c = false;
         aM = false;
         osrs.bo.cQ = this.cs().getHost();
         osrs.bo.eg = new fK();
         String var7 = osrs.bo.ey.e;
         byte var8 = 0;
         if ((aK & hO.b.b()) != 0) {
            osrs.bo.dY = "beta";
         }

         try {
            osrs.U.a("oldschool", osrs.bo.dY, var7, var8, 25);
         } catch (IOException var6) {
            gG.a((String)null, var6, (short)0);
         }

         s = this;
         osrs.bo.aK = aP;
         osrs.bo.j();
         if (Boolean.parseBoolean(System.getProperty("jagex.disableBouncyCastle"))) {
            this.cq = true;
         }

         if (aQ == -1) {
            if (!this.at() && !this.au()) {
               aQ = 0;
            } else {
               aQ = 5;
            }
         }

         ((T)this).b(765, 503, 237, 1);
      }

   }

   public DBRowConfig getDBRowConfig(int var1) {
      if (!s.isClientThread()) {
         if (!osrs.T.eo) {
            throw new AssertionError("must be called on client thread");
         } else {
            throw new IllegalStateException("must be called on client thread");
         }
      } else {
         return this.n(var1);
      }
   }

   public HashTable getWidgetFlags() {
      return this.bd();
   }

   public static void a(bG var0, bu var1) {
      Iterator var2 = dM.iterator();

      while(var2.hasNext()) {
         gZ var3 = (gZ)var2.next();
         dF.debug("WorldEntity spawn: {}", var3.n.getId());
         s.getCallbacks().post(new WorldEntitySpawned(var3));
      }

      dM.clear();
   }

   public static void a(ig var0) {
      b(var0, (jd)null, 255, osrs.bo.fR);
   }

   public void finalize() throws Throwable {
      osrs.eZ.b(this);
      super.finalize();
   }

   public boolean as() {
      return this.cr == 1;
   }

   public long getAccountHash() {
      return this.cs;
   }

   public boolean at() {
      return osrs.bo.en != null && !osrs.bo.en.trim().isEmpty() && osrs.bo.aP != null && !osrs.bo.aP.trim().isEmpty();
   }

   public boolean au() {
      return osrs.bo.t != null && !osrs.bo.t.trim().isEmpty() && osrs.bo.cM != null && !osrs.bo.cM.trim().isEmpty();
   }

   public boolean av() {
      return this.dX != null;
   }

   public void g(String var1) throws IOException {
      HashMap var2 = new HashMap();
      var2.put("grant_type", "refresh_token");
      var2.put("scope", "gamesso.token.create");
      var2.put("refresh_token", var1);
      String var10002 = osrs.bo.N;
      URL var3 = new URL(var10002 + "shield/oauth/token" + (new hb(var2)).c());
      gE var4 = new gE();
      if (this.as()) {
         var4.b(h);
      } else {
         var4.b(g);
      }

      var4.a("Host", (new URL(osrs.bo.N)).getHost());
      var4.b(hl.a);
      dk var5 = osrs.dk.a;
      c var6 = this.dY;
      if (var6 != null) {
         this.ec = var6.a(var5.c(), var3, var4.a(), "");
      } else {
         bO var7 = new bO(var3, var5, var4, this.cq);
         this.dQ = this.dS.a(var7);
      }

   }

   public void h(String var1) throws IOException {
      URL var2 = new URL(osrs.bo.N + "public/v1/games/YCfdbvr2pM1zUYMxJRexZY/play");
      gE var3 = new gE();
      var3.c(var1);
      dk var4 = osrs.dk.b;
      a var5 = this.dX;
      if (var5 != null) {
         this.ed = var5.a(var4.c(), var2, var3.a(), "");
      } else {
         bO var6 = new bO(var2, var4, var3, this.cq);
         this.dR = this.dS.a(var6);
      }

   }

   public void a(String var1, String var2) throws IOException, JSONException {
      URL var3 = new URL(osrs.bo.cS + "game-session/v1/tokens");
      bO var4 = new bO(var3, osrs.dk.a, this.cq);
      var4.a().c(var1);
      var4.a().b(hl.a);
      JSONObject var5 = new JSONObject();
      var5.put("accountId", var2);
      var4.a(new hd(var5));
      this.dR = this.dS.a(var4);
   }

   public final void aw() {
      gC.a(new int[]{20, 260, 10000}, new int[]{1000, 100, 500});
      osrs.bo.q = kh.a(aK, hO.b);
      osrs.bo.aG = aL == 0 ? 'ꩊ' : u + '鱀';
      osrs.bo.dn = aL == 0 ? 443 : u + '썐';
      osrs.bo.ch = osrs.bo.aG;
      osrs.bo.bH = osrs.fj.a;
      osrs.bo.bI = osrs.fj.b;
      osrs.bo.bJ = osrs.fj.c;
      osrs.bo.dt = osrs.fj.d;
      osrs.bo.as = new bQ(this.cq, 237);
      this.bW();
      this.bY();
      osrs.bo.ba = this.bT();
      ((T)this).a((ap)an, 0);
      ((T)this).a((ap)am, 1);
      this.bU();
      osrs.bo.cl = new gR(255, osrs.U.c, osrs.U.d, 750000);
      bL = osrs.H.a();
      t(bL.o());
      osrs.bn.l = new S(osrs.bo.cp);
      this.dS = new cX("tokenRequest", 1, 1);
      osrs.eZ.a(this);
      osrs.bo.ag = new kc();
      osrs.bo.bw = new hm(osrs.bo.ag);
      osrs.bo.cJ = new hG(osrs.bo.bw);
      f.a();
   }

   public final void ax() {
      this.aW();
      ++x;
      this.l();

      while(true) {
         fv var1;
         synchronized(fr.a) {
            var1 = (fv)fr.c.b();
         }

         if (var1 == null) {
            boolean var2 = false;
            boolean var3 = false;
            if (!osrs.eZ.f.isEmpty()) {
               gn var4 = (gn)osrs.eZ.f.get(0);
               if (var4 == null) {
                  osrs.eZ.f.remove(0);
               } else if (var4.e()) {
                  if (var4.a()) {
                     System.out.println("Error in midimanager.service: " + var4.c());
                     var2 = true;
                  } else {
                     if (var4.d() != null) {
                        osrs.eZ.f.add(1, var4.d());
                     }

                     var3 = var4.b();
                  }

                  osrs.eZ.f.remove(0);
               } else {
                  var3 = var4.b();
               }
            }

            if (var2) {
               osrs.eZ.f.clear();
               osrs.eZ.c();
            }

            if (var3 && bH && osrs.bo.bd != null) {
               osrs.bo.bd.c();
            }

            osrs.ej.a(-455495588);
            am.a();
            this.bX();
            synchronized(osrs.aI.o) {
               osrs.aI.g = osrs.aI.q;
               osrs.aI.h = osrs.aI.r;
               osrs.aI.i = osrs.aI.a;
               osrs.aI.j = osrs.aI.b;
               osrs.aI.k = osrs.aI.c;
               osrs.aI.l = osrs.aI.d;
               osrs.aI.m = osrs.aI.e;
               osrs.aI.n = osrs.aI.f;
               osrs.aI.c = 0;
            }

            if (osrs.bo.ba != null) {
               int var8 = osrs.bo.ba.a();
               bB = var8;
            }

            if (w == 0) {
               h();
               osrs.T.bR();
            } else if (w == 5) {
               osrs.bA.a((T)this, (gg)osrs.bo.ad, (gg)osrs.bo.L);
               h();
               osrs.T.bR();
            } else if (w != 10 && w != 11) {
               if (w == 20) {
                  osrs.bA.a((T)this, (gg)osrs.bo.ad, (gg)osrs.bo.L);
                  this.c(-1981778363);
               } else if (w == 50) {
                  osrs.bA.a((T)this, (gg)osrs.bo.ad, (gg)osrs.bo.L);
                  this.c(-323854552);
               } else if (w == 25) {
                  if (bO) {
                     jB.a(1528530233);
                  }

                  if (bP) {
                     osrs.ax.a(osrs.bo.ee, (byte)39);
                  }

                  if (!bP && !bO) {
                     a((int)30);
                  }
               }
            } else {
               osrs.bA.a((T)this, (gg)osrs.bo.ad, (gg)osrs.bo.L);
            }

            if (w == 30) {
               this.aA();
            } else if (w == 40 || w == 45) {
               this.c(1151382516);
            }

            this.aT();
            return;
         }

         var1.c.a(var1.b, (int)var1.cm, var1.d, false, (byte)4);
      }
   }

   public void setCameraShakeDisabled(boolean var1) {
      dy = var1;
   }

   public static float a(bG var0, float var1, float var2, int var3) {
      int var4 = (int)(var1 / 128.0F);
      int var5 = (int)(var2 / 128.0F);
      if (var4 >= 0 && var5 >= 0 && var4 < var0.p && var5 < var0.q) {
         int var6 = var3;
         if (var3 < 3 && (var0.v[1][var4][var5] & 2) == 2) {
            var6 = var3 + 1;
         }

         float var7 = var1 % 128.0F;
         float var8 = var2 % 128.0F;
         float var9 = ((128.0F - var7) * (float)var0.u[var6][var4][var5] + (float)var0.u[var6][var4 + 1][var5] * var7) / 128.0F;
         float var10 = ((128.0F - var7) * (float)var0.u[var6][var4][var5 + 1] + (float)var0.u[var6][var4 + 1][var5 + 1] * var7) / 128.0F;
         return ((128.0F - var8) * var9 + var8 * var10) / 128.0F;
      } else {
         return 0.0F;
      }
   }

   public final void c(boolean var1) {
      this.f(var1);
      this.d(var1);
      hu.a();
      if ((w == 10 || w == 20 || w == 30) && ai != 0L && osrs.bd.a() > ai) {
         t(osrs.bo.p());
      }

      if (w == 0) {
         this.a(osrs.bA.b, osrs.bA.c, var1, osrs.bo.cu);
      } else if (w == 5) {
         osrs.bA.a(osrs.bo.ck, osrs.bo.ad, osrs.bo.L);
      } else if (w != 10 && w != 11) {
         if (w == 20) {
            osrs.bA.a(osrs.bo.ck, osrs.bo.ad, osrs.bo.L);
         } else if (w == 50) {
            osrs.bA.a(osrs.bo.ck, osrs.bo.ad, osrs.bo.L);
         } else if (w == 25) {
            if (bO) {
               int var2;
               if (bp == 1) {
                  if (bl > bm) {
                     bm = bl;
                  }

                  var2 = (bm * 50 - bl * 50) / bm;
                  a(osrs.bv.n + osrs.bq.g + osrs.bq.d + var2 + "%" + osrs.bq.e, false);
               } else if (bp == 2) {
                  if (bn > bo) {
                     bo = bn;
                  }

                  var2 = (bo * 50 - bn * 50) / bo + 50;
                  a(osrs.bv.n + osrs.bq.g + osrs.bq.d + var2 + "%" + osrs.bq.e, false);
               } else {
                  a(osrs.bv.n, false);
               }
            }
         } else if (w == 30) {
            this.aI();
         } else if (w == 40) {
            a(osrs.bv.o + osrs.bq.g + osrs.bv.p, false);
         } else if (w == 45) {
            a(osrs.bv.cv, false);
         }
      } else {
         osrs.bA.a(osrs.bo.ck, osrs.bo.ad, osrs.bo.L);
      }

      if (w > 0) {
         osrs.T.fa.a(0, 0, (byte)-59);
      }

      this.e(var1);
   }

   public final void ay() {
      if (af != null && af.e()) {
         af.b();
      }

      if (osrs.bo.bi != null) {
         osrs.bo.bi.a = false;
      }

      osrs.bo.bi = null;
      b.c();
      if (osrs.aI.o != null) {
         synchronized(osrs.aI.o) {
            osrs.aI.o = null;
         }
      }

      osrs.bo.ba = null;
      if (osrs.bo.bd != null) {
         osrs.bo.bd.d();
      }

      osrs.bo.eg.b();
      fr.a();
      if (osrs.bo.as != null) {
         osrs.bo.as.a();
         osrs.bo.as = null;
      }

      osrs.U.b();
      this.dS.a();
   }

   public void az() {
      if (osrs.bo.eg.q >= 4) {
         this.k("js5crc");
         a((int)1000);
      } else {
         if (osrs.bo.eg.r >= 4) {
            if (w <= 5) {
               this.k("js5io");
               a((int)1000);
               return;
            }

            be = 8;
            osrs.bo.eg.r = 3;
         }

         int var1 = be * 375 - 1;
         be = var1 * -1214044089;
         if (var1 + 1 <= 0) {
            try {
               if (bd == 0) {
                  osrs.bo.eo = osrs.bo.aT.a(osrs.bo.cQ, osrs.bo.ch);
                  ++bd;
               }

               if (bd == 1) {
                  if (osrs.bo.eo.a == 2) {
                     this.G(-1);
                     return;
                  }

                  if (osrs.bo.eo.a == 1) {
                     ++bd;
                  }
               }

               if (bd == 2) {
                  osrs.bo.bU = gO.a((Socket)((Socket)osrs.bo.eo.e), 40000, 5000);
                  aR var2 = new aR(osrs.eK.b.k + 1);
                  var2.a(osrs.eK.b.j);
                  var2.d(237);
                  var2.d(bV[0]);
                  var2.d(bV[1]);
                  var2.d(bV[2]);
                  var2.d(bV[3]);
                  osrs.bo.bU.b(var2.c, 0, osrs.eK.b.k + 1);
                  ++bd;
                  osrs.bo.by = osrs.bd.a();
               }

               if (bd == 3) {
                  if (osrs.bo.bU.a() > 0) {
                     int var4 = osrs.bo.bU.b();
                     if (var4 != 0) {
                        this.G(var4);
                        return;
                     }

                     ++bd;
                  } else if (osrs.bd.a() - osrs.bo.by > 30000L) {
                     this.G(-2);
                     return;
                  }
               }

               if (bd == 4) {
                  osrs.bo.eg.a(osrs.bo.bU, w > 20);
                  osrs.bo.eo = null;
                  osrs.bo.bU = null;
                  bd = 0;
                  bf = 0;
               }
            } catch (IOException var3) {
               this.G(-3);
            }
         }
      }

   }

   public Player getHintArrowPlayer() {
      return this.aZ();
   }

   public void G(int var1) {
      osrs.bo.eo = null;
      osrs.bo.bU = null;
      bd = 0;
      if (osrs.bo.ch == osrs.bo.aG) {
         osrs.bo.ch = osrs.bo.dn;
      } else {
         osrs.bo.ch = osrs.bo.aG;
      }

      ++bf;
      if (bf < 2 || var1 != 7 && var1 != 9) {
         if (bf >= 2 && var1 == 6) {
            this.k("js5connect_outofdate");
            a((int)1000);
         } else if (bf >= 4) {
            if (w <= 5) {
               this.k("js5connect");
               a((int)1000);
            } else {
               be = 8;
            }
         }
      } else if (w <= 5) {
         this.k("js5connect_full");
         a((int)1000);
      } else {
         be = 8;
      }

   }

   public int getCenterX() {
      return osrs.aW.h.e();
   }

   public ag[] H(int var1) {
      kd var2 = osrs.bo.cR;
      if (var2 == null) {
         return null;
      } else {
         ag[][] var3 = var2.m;
         return var3 != null && var1 >= 0 && var1 < var3.length && var3[var1] != null ? var3[var1] : null;
      }
   }

   public static void a(ag var0, int var1, int var2, float var3) {
      ar var4 = var0.b(osrs.bo.cR, false);
      int var5 = cy;
      int var6 = cA;
      bG var7 = osrs.bo.aQ;
      int var8 = br & 2047;
      osrs.df.g(var1, var2, var1 + var4.a(), var2 + var4.b());
      b(var1, var2, var8, var3, var4);
      c(var7, var1, var2, var8, var3, var4);
      b(var7, var1, var2, var8, var3, var4);
      d(var7, var1, var2, var8, var3, var4);
      a(var7, var1, var2, var8, var3, var4);
      if (bR.g != 0 && s.getGameCycle() % 20 < 10) {
         a(var1, var2, var8, var3, var4);
      }

      int var9 = cV;
      int var10 = cW;
      int var11;
      int var12;
      int var13;
      int var14;
      if (var9 != 0) {
         var11 = (var9 << 7) + 64 - var5;
         var12 = (var10 << 7) + 64 - var6;
         var13 = (int)((float)var11 * var3);
         var14 = (int)((float)var12 * var3);
         a(var1, var2, var13, var14, var8, osrs.bo.T[0], var4);
      }

      if (D.i(-1) == hf.c && osrs.bo.T.length >= 4) {
         var11 = var4.a() / 2 + var1;
         var12 = var4.b() / 2 + var2;
         if (var4.b(osrs.aI.h - var1, osrs.aI.i - var2)) {
            var13 = (int)Math.round(Math.atan2((double)(osrs.aI.i - var12), (double)(osrs.aI.h - var11)) / 0.0030679615) - 512;
            var14 = var13 - br & 2047;
            int var15 = (var14 + 64) / 128 * 128;
            a(var11, var12, osrs.bo.T[2], 30, var15);
         }

         if (cY > 0) {
            a(var11, var12, osrs.bo.T[3], 30, cX * 128);
         }
      }

      t var16 = bA();
      if (var16 != null && !var16.V()) {
         osrs.df.k(var4.a() / 2 + osrs.eM.e + var1 - 1, var4.b() / 2 + var2 - osrs.eM.f - 1, 3, 3, 16777215);
      }

   }

   public void setSkyboxColor(int var1) {
      osrs.bo.ft = var1;
   }

   public MessageNode addChatMessage(ChatMessageType var1, String var2, String var3, String var4) {
      return this.addChatMessage(var1, var2, var3, var4, true);
   }

   public Widget[] getWidgetRoots() {
      int var1 = this.getTopLevelInterfaceId();
      if (var1 == -1) {
         return new Widget[0];
      } else {
         ArrayList var2 = new ArrayList();
         kd var3 = osrs.bo.cR;
         ag[] var4 = var3.m[var1];
         int var5 = var4.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            ag var7 = var4[var6];
            if (var7 != null && var7.bL == -1) {
               var2.add(var7);
            }
         }

         return (Widget[])var2.toArray(new Widget[var2.size()]);
      }
   }

   public static void a(dh var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7) {
      long var8 = var0.bw[var1];
      int var12;
      int var13;
      int var14;
      int var18;
      if ((var8 & 72057594037927936L) != 0L) {
         dy var10 = var0.bp[var1];
         boolean var11 = false;
         if (var10 != null) {
            var12 = var10.getConfig();
            var13 = var12 & 31;
            var14 = var12 >> 6 & 3;
            int var15 = (var10.getHash() >>> 19 & 1L) == 0L ? dD : dC;
            if ((var8 & 144115188075855872L) != 0L) {
               aC var16 = s.v(var10.getId());
               if (a(var16, var2, var3)) {
                  var11 = true;
               } else {
                  var8 &= -144115188075855873L;
               }
            }

            if ((var8 & 144115188075855872L) == 0L) {
               if (var13 == 0 || var13 == 2) {
                  var11 = true;
                  if ((var14 & 1) == 0) {
                     osrs.df.f(var14 == 2 ? var6 - 1 : var4, var5, var7 - var5, var15);
                  } else {
                     osrs.df.h(var4, var14 == 3 ? var7 - 1 : var5, var6 - var4, var15);
                  }
               }

               if (var13 == 3) {
                  var11 = true;
                  boolean var26 = var14 == 0 || var14 == 3;
                  boolean var17 = var14 < 2;
                  var18 = Math.max(1, (var6 - var4) / 4);
                  int var19 = Math.max(1, (var7 - var5) / 4);
                  osrs.df.k(var26 ? var4 : var6 - var18, var17 ? var5 : var7 - var19, var18, var19, var15);
               }

               if (var13 == 2) {
                  if ((var14 & 1) == 1) {
                     osrs.df.f(var14 == 1 ? var6 - 1 : var4, var5, var7 - var5, var15);
                  } else {
                     osrs.df.h(var4, var14 == 2 ? var7 - 1 : var5, var6 - var4, var15);
                  }
               }
            }
         }

         if (!var11) {
            var8 &= -72057594037927937L;
         }
      }

      boolean var20;
      if ((var8 & 288230376151711744L) != 0L) {
         var20 = false;
         dA var21 = null;

         for(var12 = 0; var12 < 5; ++var12) {
            dA var22 = var0.bt[var1 * 5 + var12];
            if (var22 != null && (var22.a * 5543273290054098517L * -1025946513300246787L >>> 16 & 7L) == 2L && var22.l * -1636210807 * -1133953351 == var2 && var22.j * -880225481 * 752308871 == var3) {
               var21 = var22;
               break;
            }
         }

         if (var21 != null) {
            var12 = var21.getConfig();
            var13 = var12 & 31;
            var14 = var12 >> 6 & 3;
            aC var25 = s.v(var21.getId());
            if (a(var25, var2, var3)) {
               var20 = true;
            } else if (var13 == 9) {
               var20 = true;
               int var27 = (var21.getHash() >>> 19 & 1L) == 0L ? 15597568 : 15658734;
               int var28;
               if (var14 != 0 && var14 != 2) {
                  var28 = var5;
                  var18 = var7 - 1;
               } else {
                  var28 = var7 - 1;
                  var18 = var5;
               }

               osrs.df.l(var4, var28, var6 - 1, var18, var27);
            }
         }

         if (!var20) {
            var8 &= -288230376151711745L;
         }
      }

      if ((var8 & 576460752303423488L) != 0L) {
         var20 = false;
         dr var24 = var0.br[var1];
         if (var24 != null) {
            aC var23 = s.v(var24.getId());
            if (a(var23, var2, var3)) {
               var20 = true;
            }
         }

         if (!var20) {
            var8 &= -576460752303423489L;
         }
      }

      var0.bw[var1] = var8;
   }

   public void setHintArrow(WorldPoint var1) {
      bR.g = 2;
      bR.a = var1.getX();
      bR.b = var1.getY();
      bR.f = var1.getPlane();
      bR.c = 64;
      bR.d = 64;
      bR.e = 0;
   }

   public final void aA() {
      if (aZ > 1) {
         --aZ;
      }

      if (bk > 0) {
         --bk;
      }

      if (bZ) {
         bZ = false;
         r();
      } else {
         if (!cI) {
            osrs.bn.c();
         }

         this.aK();
         if (w == 30) {
            while(true) {
               ef var1 = (ef)osrs.fd.a.c();
               boolean var2;
               if (var1 == null) {
                  var2 = false;
               } else {
                  var2 = true;
               }

               r var3;
               int var4;
               if (!var2) {
                  if (ca.a) {
                     var3 = osrs.r.a(osrs.u.d, b.p);
                     var3.f.a(0);
                     var4 = var3.f.d;
                     ca.a(var3.f);
                     var3.f.g(var3.f.d - var4);
                     b.a(var3);
                     ca.d();
                  }

                  int var5;
                  int var6;
                  int var7;
                  int var8;
                  int var9;
                  int var10;
                  int var11;
                  int var13;
                  int var14;
                  r var20;
                  int var35;
                  synchronized(osrs.bo.bi.b) {
                     if (!t) {
                        osrs.bo.bi.c = 0;
                     } else if (osrs.aI.k != 0 || osrs.bo.bi.c >= 40) {
                        var20 = null;
                        var5 = 0;
                        var6 = 0;
                        var7 = 0;
                        var8 = 0;

                        for(var9 = 0; var9 < osrs.bo.bi.c && (var20 == null || var20.f.d - var5 < 246); ++var9) {
                           var6 = var9;
                           var10 = osrs.bo.bi.e[var9];
                           if (var10 < -1) {
                              var10 = -1;
                           } else if (var10 > 65534) {
                              var10 = 65534;
                           }

                           var11 = osrs.bo.bi.d[var9];
                           if (var11 < -1) {
                              var11 = -1;
                           } else if (var11 > 65534) {
                              var11 = 65534;
                           }

                           if (aV != var11 || aW != var10) {
                              if (var20 == null) {
                                 var20 = osrs.r.a(osrs.u.M, b.p);
                                 var20.f.a(0);
                                 var5 = var20.f.d;
                                 bu var12 = var20.f;
                                 var12.d += 2;
                                 var7 = 0;
                                 var8 = 0;
                              }

                              if (aX != -1L) {
                                 var35 = var11 - aV;
                                 var13 = var10 - aW;
                                 var14 = (int)((osrs.bo.bi.f[var9] - aX) / 20L);
                                 var7 = (int)((osrs.bo.bi.f[var9] - aX) % 20L + (long)var7);
                              } else {
                                 var35 = var11;
                                 var13 = var10;
                                 var14 = Integer.MAX_VALUE;
                              }

                              aV = var11;
                              aW = var10;
                              if (var14 < 8 && var35 >= -32 && var35 <= 31 && var13 >= -32 && var13 <= 31) {
                                 var35 += 32;
                                 var13 += 32;
                                 var20.f.b((var35 << 6) + (var14 << 12) + var13);
                              } else if (var14 < 32 && var35 >= -128 && var35 <= 127 && var13 >= -128 && var13 <= 127) {
                                 var35 += 128;
                                 var13 += 128;
                                 var20.f.a(var14 + 128);
                                 var20.f.b((var35 << 8) + var13);
                              } else if (var14 < 32) {
                                 var20.f.a(var14 + 192);
                                 if (var11 != -1 && var10 != -1) {
                                    var20.f.d(var11 | var10 << 16);
                                 } else {
                                    var20.f.d(Integer.MIN_VALUE);
                                 }
                              } else {
                                 var20.f.b((var14 & 8191) + '\ue000');
                                 if (var11 != -1 && var10 != -1) {
                                    var20.f.d(var11 | var10 << 16);
                                 } else {
                                    var20.f.d(Integer.MIN_VALUE);
                                 }
                              }

                              ++var8;
                              aX = osrs.bo.bi.f[var9];
                           }
                        }

                        if (var20 != null) {
                           var20.f.g(var20.f.d - var5);
                           var9 = var20.f.d;
                           var20.f.d = var5;
                           var20.f.a(var7 / var8);
                           var20.f.a(var7 % var8);
                           var20.f.d = var9;
                           b.a(var20);
                        }

                        if (var6 >= osrs.bo.bi.c) {
                           osrs.bo.bi.c = 0;
                        } else {
                           iu var30 = osrs.bo.bi;
                           var30.c -= var6;
                           System.arraycopy(osrs.bo.bi.d, var6, osrs.bo.bi.d, 0, osrs.bo.bi.c);
                           System.arraycopy(osrs.bo.bi.e, var6, osrs.bo.bi.e, 0, osrs.bo.bi.c);
                           System.arraycopy(osrs.bo.bi.f, var6, osrs.bo.bi.f, 0, osrs.bo.bi.c);
                        }
                     }
                  }

                  this.aD();
                  if (bB != 0) {
                     var3 = osrs.r.a(osrs.u.K, b.p);
                     var3.f.b(bB);
                     b.a(var3);
                  }

                  boolean var19 = eM.b();
                  if (var19) {
                     var20 = osrs.r.a(osrs.u.ax, b.p);
                     var20.f.b(0);
                     var5 = var20.f.d;
                     long var23 = osrs.bd.a();

                     for(var8 = 0; var8 < am.c * -815660073; ++var8) {
                        long var32 = var23 - bG;
                        if (var32 > 16777215L) {
                           var32 = 16777215L;
                        }

                        bG = var23;
                        var20.f.s((int)var32);
                        var20.f.n(am.b[var8]);
                     }

                     var20.f.f(var20.f.d - var5);
                     b.a(var20);
                  }

                  if (cj > 0) {
                     --cj;
                  }

                  if (am.c(96) || am.c(97) || am.c(98) || am.c(99)) {
                     ck = true;
                  }

                  if (ck && cj <= 0) {
                     cj = 20;
                     ck = false;
                     var20 = osrs.r.a(osrs.u.e, b.p);
                     var20.f.p(bq);
                     var20.f.p(br);
                     b.a(var20);
                  }

                  if (osrs.T.fh && !aY) {
                     aY = true;
                     var20 = osrs.r.a(osrs.u.aF, b.p);
                     var20.f.a(1);
                     b.a(var20);
                  }

                  if (!osrs.T.fh && aY) {
                     aY = false;
                     var20 = osrs.r.a(osrs.u.aF, b.p);
                     var20.f.a(0);
                     b.a(var20);
                  }

                  if (dU != null) {
                     dU.a();
                  }

                  s();
                  t();
                  if (osrs.eM.h * -1 != osrs.bo.aQ.y) {
                     osrs.eM.h = osrs.bo.aQ.y * -1;
                     osrs.eM.a(osrs.bo.aQ.y);
                  }

                  if (w != 30) {
                     return;
                  }

                  G();
                  this.aE();
                  ++b.i;
                  if (b.i > 750) {
                     r();
                     return;
                  }

                  x();
                  y();
                  Iterator var25 = D.iterator();

                  while(var25.hasNext()) {
                     bG var21 = (bG)var25.next();

                     for(var6 = 0; var6 < var21.e.a(); ++var6) {
                        var7 = var21.e.b(var6);
                        gZ var31 = (gZ)var21.t.a((long)var7);
                        if (var31 != null) {
                           var31.d(x, 2107134395);
                        }

                        osrs.bo.cJ.a(var21, var31.k(), var31.l(), false);
                        if (var31.a.c() != null && var31.a.c().g() != -1) {
                           if (var31.b > 0) {
                              --var31.b;
                           } else {
                              var9 = jM.a(var31.a, 1, osrs.bo.cJ);
                              if ((var9 & 2) != 0) {
                                 var31.a.a();
                                 var31.m.h();
                              }
                           }
                        }

                        if (var31.m.c() != null && var31.m.c().g() != -1) {
                           var9 = jM.a(var31.m, 1, osrs.bo.cJ);
                           if ((var9 & 2) != 0) {
                              var31.m.h();
                           }
                        }

                        osrs.bo.cJ.a();
                     }
                  }

                  int[] var22 = G.a;
                  Iterator var24 = D.iterator();

                  while(var24.hasNext()) {
                     bG var26 = (bG)var24.next();

                     for(var8 = 0; var8 < G.j; ++var8) {
                        t var34 = (t)var26.r.a((long)var22[var8]);
                        if (var34 != null && var34.aa > 0) {
                           --var34.aa;
                           if (var34.aa == 0) {
                              var34.F = null;
                              ((o)var34).e(-1);
                           }
                        }
                     }

                     Iterator var33 = var26.s.iterator();

                     while(var33.hasNext()) {
                        s var36 = (s)var33.next();
                        if (var36 != null && var36.aa > 0) {
                           --var36.aa;
                           if (var36.aa == 0) {
                              var36.F = null;
                              ((o)var36).e(-1);
                           }
                        }
                     }
                  }

                  ++cd;
                  if (cv != 0) {
                     cu += 20;
                     if (cu >= 400) {
                        cv = 0;
                     }
                  }

                  if (cY > 0) {
                     --cY;
                  }

                  bF.a();

                  while(am.b() && !bF.c()) {
                     if (by >= 2 && am.c(82) && am.p == 66) {
                        String var27 = iw.d();
                        s.j(var27);
                     } else if (H != 1 || am.n <= 0) {
                        bF.a(am.p, am.n);
                     }
                  }

                  if (ai() && am.c(82) && am.c(81) && bB != 0) {
                     jR var28 = K();
                     if (var28.c()) {
                        var8 = var28.g() - bB;
                        if (var8 < 0) {
                           var8 = 0;
                        } else if (var8 > 3) {
                           var8 = 3;
                        }

                        if (var8 != var28.g()) {
                           a(var28.e(), var28.f(), var8, false);
                        }
                     }

                     bB = 0;
                  }

                  osrs.bo.cv.a(osrs.bo.cR, osrs.T.eP, osrs.T.eQ, x, bB, o, 279654595);
                  p.a();
                  osrs.bo.cv.a(o, osrs.bo.cR);
                  this.a((short)-29343);
                  z();
                  osrs.bo.cv.a(osrs.bo.cR);
                  Iterator var29 = D.iterator();

                  while(var29.hasNext()) {
                     bG var38 = (bG)var29.next();
                     dh var39 = var38.x;
                     if (var39.d()) {
                        var10 = var39.bD;
                        var11 = var39.bE;
                        r var42 = osrs.r.a(osrs.u.bg, b.p);
                        var42.f.a(5);
                        var42.f.a(am.c(82) ? (am.c(81) ? 2 : 1) : 0);
                        var42.f.r(var38.B + var10);
                        var42.f.p(var38.z + var11);
                        b.a(var42);
                        cn = osrs.aI.l;
                        co = osrs.aI.m;
                        cv = 1;
                        cu = 0;
                        cV = var10;
                        cW = var11;
                        var39.f();
                     }
                  }

                  osrs.bn.b();
                  if (bK) {
                     b(osrs.bo.cG, osrs.bo.I, osrs.bo.eu);
                     c(osrs.bo.db, osrs.bo.bZ);
                     if (osrs.bo.cG == osrs.bo.bM && osrs.bo.bD == osrs.bo.I && osrs.bo.eu == osrs.bo.dO && osrs.bo.db == osrs.bo.bV && osrs.bo.cV == osrs.bo.bZ) {
                        bK = false;
                        bJ = false;
                        cZ = false;
                        da = false;
                        osrs.bo.bu = 0;
                        osrs.bo.eb = 0;
                        osrs.bo.u = 0;
                        osrs.bo.v = 0;
                        osrs.bo.X = 0;
                        osrs.bo.aH = 0;
                        osrs.bo.bG = 0;
                        osrs.bo.dB = 0;
                        osrs.bo.fl = 0;
                        osrs.bo.n = 0;
                        dc = null;
                        de = null;
                        dd = null;
                     }
                  } else if (bJ) {
                     if (!cZ) {
                        var8 = osrs.bo.f(osrs.bo.dB);
                        var9 = osrs.bo.f(osrs.bo.fl);
                        var10 = b(osrs.bo.aQ, var8, var9, osrs.bo.aQ.y) - osrs.bo.n;
                        b(var8, var10, var9);
                     } else if (dc != null) {
                        osrs.bo.bM = dc.a();
                        osrs.bo.dO = dc.b();
                        if (db) {
                           osrs.bo.bD = dc.c();
                        } else {
                           osrs.bo.bD = b(osrs.bo.aQ, osrs.bo.bM, osrs.bo.dO, osrs.bo.aQ.y) - dc.c();
                        }

                        dc.d();
                     }

                     if (!da) {
                        var8 = osrs.bo.f(osrs.bo.bu);
                        var9 = osrs.bo.f(osrs.bo.eb);
                        var10 = b(osrs.bo.aQ, var8, var9, osrs.bo.aQ.y) - osrs.bo.u;
                        var11 = var8 - osrs.bo.bM;
                        var35 = var10 - osrs.bo.bD;
                        var13 = var9 - osrs.bo.dO;
                        var14 = (int)Math.sqrt((double)(var11 * var11 + var13 * var13));
                        int var15 = (int)(Math.atan2((double)var35, (double)var14) * 325.9490051269531) & 2047;
                        int var16 = (int)(Math.atan2((double)var11, (double)var13) * -325.9490051269531) & 2047;
                        c(var15, var16);
                     } else {
                        if (de != null) {
                           osrs.bo.bV = de.a();
                           osrs.bo.bV = Math.min(Math.max(osrs.bo.bV, 128), 383);
                           de.d();
                        }

                        if (dd != null) {
                           osrs.bo.cV = dd.a() & 2047;
                           dd.d();
                        }
                     }
                  }

                  for(var8 = 0; var8 < 5; ++var8) {
                     int var10003 = dj[var8]++;
                  }

                  af.d();
                  var8 = osrs.bo.e();
                  var9 = osrs.T.bQ();
                  if (var8 > 15000 && var9 > 15000) {
                     bk = 250;
                     osrs.aI.p = 14500;
                     A(-1);
                     r var37 = osrs.r.a(osrs.u.y, b.p);
                     b.a(var37);
                  }

                  osrs.bn.l.e();

                  for(var10 = 0; var10 < dl.size(); ++var10) {
                     var11 = (Integer)dl.get(var10);
                     cz var43 = (cz)osrs.bk.g.a((long)var11);
                     cz var45;
                     if (var43 != null) {
                        var45 = var43;
                     } else {
                        cz var44 = osrs.cz.a(osrs.bk.c, osrs.bk.d, var11);
                        if (var44 != null) {
                           osrs.bk.g.a(var44, (long)var11);
                        }

                        var45 = var44;
                     }

                     if (var45 == null) {
                        var14 = 2;
                     } else {
                        var14 = var45.c(485811346) ? 0 : 1;
                     }

                     if (var14 != 2) {
                        dl.remove(var10);
                        --var10;
                     }
                  }

                  iz var41 = b;
                  var41.j = (var41.j * -1545448701 + 1) * -1131991125;
                  if (b.j * -1545448701 > 50) {
                     r var40 = osrs.r.a(osrs.u.au, b.p);
                     b.a(var40);
                  }

                  try {
                     b.b();
                  } catch (IOException var17) {
                     r();
                  }

                  osrs.dZ.a().e();
                  return;
               }

               var3 = osrs.r.a(osrs.u.bm, b.p);
               var3.f.a(0);
               var4 = var3.f.d;
               osrs.fd.a(var3.f);
               var3.f.g(var3.f.d - var4);
               b.a(var3);
            }
         }
      }

   }

   public void d(boolean var1) {
      long var2 = System.nanoTime();
      long var4 = var2 - this.E;
      this.E = var2;
      if (this.bL() == GameState.LOGGED_IN.getState()) {
         float var6 = (float)var4 / 2.0E7F;
         a(osrs.bo.aQ, var6);
         this.a(var4);
         this.b(var4);
      }

   }

   public void aB() {
      int var1 = osrs.aI.k;
      boolean var2 = var1 == 1 || !osrs.bo.cP && var1 == 4;
      if (!var2) {
         if (!cJ.f(osrs.aI.h, osrs.aI.i)) {
            cI = false;
            cJ.g();
         }

         int var3 = bB;
         if (cI && var3 != 0) {
            if (osrs.bo.fC > 0 && cJ.a > -1) {
               ig var4 = cJ.q[cJ.a];
               if (var4 != null) {
                  boolean var5 = osrs.aI.h >= var4.w - 10 && osrs.aI.h <= var4.b + var4.w + 10 && osrs.aI.i >= var4.c - 10 && osrs.aI.i <= var4.v + var4.c + 10;
                  if (var5) {
                     osrs.bo.fs += var3;
                     if (osrs.bo.fs < 0) {
                        osrs.bo.fs = 0;
                     } else if (osrs.bo.fs > osrs.bo.fC) {
                        osrs.bo.fs = osrs.bo.fC;
                     }

                     return;
                  }
               }
            }

            boolean var6 = osrs.aI.h >= cJ.w - 10 && osrs.aI.h <= cJ.b + cJ.w + 10 && osrs.aI.i >= cJ.c - 10 && osrs.aI.i <= cJ.v + cJ.c + 10;
            if (var6 && osrs.bo.fc > 0) {
               osrs.bo.fR += var3;
               if (osrs.bo.fR < 0) {
                  osrs.bo.fR = 0;
               } else if (osrs.bo.fR > osrs.bo.fc) {
                  osrs.bo.fR = osrs.bo.fc;
               }
            }
         }
      } else {
         cJ.g(osrs.aI.l, osrs.aI.m);
         cI = false;
         cJ.g();
      }

   }

   public Model applyTransformations(Model var1, Animation var2, int var3, Animation var4, int var5) {
      aH var6 = (aH)var1;
      bk var7 = (bk)var2;
      bk var8 = (bk)var4;
      if (var6 == null) {
         return null;
      } else if (var7 != null && var8 != null) {
         return var7.c(var6, var3, var8, var5);
      } else if (var7 != null) {
         return var7.d(var6, var3);
      } else {
         return (Model)(var8 != null ? var8.d(var6, var5) : var6.d(true));
      }
   }

   public s aC() {
      t var1 = osrs.bo.dZ != null ? osrs.bo.dZ : bA();
      if (var1 == null) {
         return null;
      } else {
         bG var2 = var1.u();
         return var2 == null ? null : (s)var2.s.b((long)bz);
      }
   }

   public IndexDataBase getIndexConfig() {
      return this.bc();
   }

   public void aD() {
      if (osrs.aI.k == 1 || !osrs.bo.cP && osrs.aI.k == 4 || osrs.aI.k == 2) {
         long var1 = osrs.aI.n - aU * -1L;
         if (var1 > 32767L) {
            var1 = 32767L;
         }

         aU = osrs.aI.n * -1L;
         int var3 = osrs.aI.m;
         if (var3 < 0) {
            var3 = 0;
         } else if (var3 > osrs.T.eQ) {
            var3 = osrs.T.eQ;
         }

         int var4 = osrs.aI.l;
         if (var4 < 0) {
            var4 = 0;
         } else if (var4 > osrs.T.eP) {
            var4 = osrs.T.eP;
         }

         int var5 = (int)var1;
         r var6 = osrs.r.a(osrs.u.bo, b.p);
         var6.f.p(var3);
         bu var7 = var6.f;
         int var8 = osrs.bo.ga;
         osrs.bo.ga = 0;
         var7.n((byte)var8);
         var6.f.p((var5 << 1) + (osrs.aI.k == 2 ? 1 : 0));
         var6.f.b(var4);
         b.a(var6);
      }

   }

   public static void I(int var0) {
      if (H == 0) {
         osrs.bo.dA = (int)osrs.bo.eS;
      } else {
         osrs.bo.eS = (double)osrs.bo.dA;
      }

   }

   public void aE() {
      hm var1 = osrs.bo.bw;
      io var2 = D;
      iG var3 = osrs.bo.ek;
      av var4 = osrs.bo.bg;
      kc var5 = osrs.bo.ag;
      boolean var6 = bL.r();
      int var7 = var5.b();
      int var8 = var5.a();

      for(int var9 = 0; var9 < var1.b(); ++var9) {
         hq var10 = var1.b(var9);
         --var10.d;
         if (var10.d < -10) {
            var1.a(var9);
            --var9;
         } else {
            iv var11 = var10.g;
            int var12;
            if (var11 == null) {
               var12 = ((au)var4).b(var10.f, (byte)-52) - 1;
               iv var13 = (iv)null;
               var11 = iv.a(var4, var10.f, var12);
               if (var11 == null) {
                  continue;
               }

               var10.d += var11.b();
               var10.g = var11;
            }

            if (var10.d < 0) {
               if (var10.e != 0) {
                  int var32 = osrs.B.b(var10.e & 255);
                  int var14 = var10.e >> 16 & 255;
                  int var15 = var10.e >> 8 & 255;
                  int var16 = var10.c;
                  bG var17 = var2.b(var16);
                  bE var18 = a(var17, osrs.B.b(var14), osrs.B.b(var15));
                  int var19 = osrs.bo.e((int)var18.h);
                  int var20 = osrs.bo.e((int)var18.j);
                  var18.a();
                  bE var21 = M();
                  int var22 = Math.abs(osrs.bo.f(var19) - (int)var21.h);
                  int var23 = Math.abs(osrs.bo.f(var20) - (int)var21.j);
                  var21.a();
                  int var24 = Math.max(var22 + var23 - 128, 0);
                  int var25 = Math.max(osrs.B.b((var10.b * 2074698075 & 31) - 1), 0);
                  gZ var26 = (gZ)osrs.bo.aQ.t.a((long)Q);
                  gZ var27 = var2.c(var16);
                  boolean var28 = var10.h;
                  boolean var29 = jY.a(var26, var27, var28);
                  if (var24 >= var32 || !var29) {
                     var10.d = -100;
                     continue;
                  }

                  float var30 = var25 < var32 ? Math.min(Math.max((float)(var32 - var24) / (float)(var32 - var25), 0.0F), 1.0F) : 1.0F;
                  var12 = (int)Math.ceil((double)((float)var8 * var30));
               } else {
                  var12 = var7;
               }

               if (var12 > 0) {
                  ix var33 = var11.b(var6);
                  bT var34 = osrs.bT.a((ix)var33, 100, var12);
                  if (var34 != null) {
                     var34.a(var10.a * -1618229329 - 1);
                     var3.a((cl)var34);
                  }
               }

               var10.d = -100;
            }
         }
      }

      if (bH) {
         boolean var31;
         if (!osrs.eZ.f.isEmpty()) {
            var31 = true;
         } else if (!osrs.eZ.d.isEmpty() && osrs.eZ.d.get(0) != null && ((eX)osrs.eZ.d.get(0)).j != null) {
            var31 = ((eX)osrs.eZ.d.get(0)).j.f();
         } else {
            var31 = false;
         }

         if (!var31) {
            if (aa() != 0 && osrs.eZ.a()) {
               osrs.eZ.a(osrs.bo.cL, aa());
            }

            bH = false;
         }
      }

   }

   public ig aF() {
      return cJ;
   }

   public void l(int var1, int var2) {
      if (b != null && b.p != null && var1 > -1 && aa() > 0 && !bH) {
         r var3 = osrs.r.a(osrs.u.g, b.p);
         var3.f.d(var1);
         b.a(var3);
      }

   }

   public final void aG() {
      ai = osrs.bd.a() + 500L;
      this.aH();
      if (osrs.bo.cR != null) {
         jF.a(osrs.bo.cR.o, osrs.T.eP, osrs.T.eQ, true, osrs.bo.cR, o);
      }

   }

   public void aH() {
      int var1 = osrs.T.eP;
      int var2 = osrs.T.eQ;
      int var3;
      if (this.eV < var1) {
         var3 = this.eV;
      }

      if (this.eW < var2) {
         var3 = this.eW;
      }

      if (bL != null) {
         try {
            Client var6 = s;
            Object[] var10000 = new Object[]{osrs.bo.p()};
         } catch (Throwable var5) {
         }
      }

   }

   public final void aI() {
      cL = -1;
      cM = -1;
      osrs.bo.bt.a(osrs.T.eP, osrs.T.eQ, osrs.bo.cR, x, cd);
      osrs.aU.a();
      if (M) {
         if (cv == 1) {
            osrs.bo.bR[cu / 100].b(cn - 8, co - 8);
         }

         if (cv == 2) {
            osrs.bo.bR[cu / 100 + 4].b(cn - 8, co - 8);
         }
      }

      if (!cI) {
         if (cL != -1) {
            int var1 = cL;
            int var2 = cM;
            if ((cJ.h >= 2 || ae != 0 || cN) && ad) {
               int var3 = cJ.h - 1;
               String var4;
               if (ae == 1 && cJ.h < 2) {
                  var4 = osrs.bv.cm + osrs.bv.cx + bg + " " + osrs.bq.f;
               } else if (cN && cJ.h < 2) {
                  var4 = cQ + osrs.bv.cx + cR + " " + osrs.bq.f;
               } else {
                  var4 = cJ.b(var3);
               }

               if (cJ.h > 2) {
                  var4 = var4 + osrs.bq.b(16777215) + " / " + (cJ.h - 2) + osrs.bv.cq;
               }

               osrs.bo.ck.b(var4, var1 + 4, var2 + 15, 16777215, 0, x / 1000);
            }
         }
      } else {
         cJ.e(2025377557);
      }

      this.aJ();
   }

   public Object[] getDBTableField(int var1, int var2, int var3) {
      C var4 = this.n(var1);
      E var5 = this.au(var4.getTableID());
      Object[] var6 = var4.c(var2);
      int[] var7 = var5.c()[var2];
      if (var6 == null) {
         var6 = var5.b()[var2];
      }

      if (var3 >= var7.length) {
         throw new IllegalArgumentException("tuple index too large");
      } else if (var6 == null) {
         return new Object[0];
      } else {
         int var8 = var6.length / var7.length;
         Object[] var9 = new Object[var8];

         for(int var10 = 0; var10 < var8; ++var10) {
            var9[var10] = var6[var7.length * var10 + var3];
         }

         return var9;
      }
   }

   public void aJ() {
      bE var1 = M();
      gZ var2 = (gZ)osrs.bo.aQ.t.a((long)Q);
      hu.a(var2, (int)var1.i, (int)var1.h, (int)var1.j, cd);
      var1.a();
      cd = 0;
   }

   public boolean a(iz var1) {
      if (var1.g == 0) {
         osrs.bo.co = null;
         af(-1);
      } else {
         if (osrs.bo.co == null) {
            osrs.bo.co = new P(osrs.bo.cp, s);
            af(-1);
         }

         osrs.bo.co.a((aR)var1.e);
      }

      p.b();
      osrs.bo.aY = true;
      var1.f = null;
      return true;
   }

   public boolean b(iz var1) {
      if (osrs.bo.co != null) {
         osrs.bo.co.b((aR)var1.e);
      }

      p.b();
      osrs.bo.aY = true;
      var1.f = null;
      return true;
   }

   public MessageNode addChatMessage(ChatMessageType var1, String var2, String var3, String var4, boolean var5) {
      if (!s.isClientThread()) {
         if (!osrs.T.eo) {
            throw new AssertionError("must be called on client thread");
         } else {
            throw new IllegalStateException("must be called on client thread");
         }
      } else if (var2 == null) {
         throw new NullPointerException();
      } else {
         ChatMessageType var6 = var1;
         String var7 = var3;
         if (ChatMessageType.CLAN_GIM_CHAT == var1) {
            var6 = ChatMessageType.CLAN_CHAT;
            var7 = "|" + var3;
         } else if (ChatMessageType.CLAN_GIM_MESSAGE == var1) {
            var6 = ChatMessageType.CLAN_MESSAGE;
            var7 = "|" + var3;
         }

         int var8 = var6.getType();
         ip var9 = (ip)iw.a.get(var8);
         if (var9 == null) {
            var9 = new ip();
            iw.a.put(var8, var9);
         }

         eE var10 = var9.a(var8, var2, var7, var4);
         iw.b.a(var10, (long)var10.f);
         iw.c.a(var10);
         p.h();
         if (dF.isDebugEnabled()) {
            dF.debug("Chat message type {}: {}", var1.name(), var3);
         }

         Map var11 = s.getChatLineMap();
         ip var12 = (ip)var11.get(var6.getType());
         MessageNode var13 = var12.getLines()[0];
         if (var5) {
            ChatMessage var14 = new ChatMessage(var13, var1, var2, var3, var4, var13.getTimestamp());
            s.getCallbacks().post(var14);
         }

         return var13;
      }
   }

   public void aK() {
      this.d();
      int var1 = 0;
      boolean var2 = b.k > 0;

      for(int var3 = b.l; var1 < 100 || var2; var2 = b.k > 0) {
         boolean var4 = this.a(b, 1086968845);
         if (var2) {
            int var5 = b.l - var3;
            iz var6 = b;
            var6.k -= var5;
         }

         if (!var4) {
            break;
         }

         ++var1;
         var3 = b.l;
      }

      this.W();
   }

   public int getSkillExperience(Skill var1) {
      int[] var2 = this.getSkillExperiences();
      int var3 = var1.ordinal();
      return var3 >= var2.length ? -1 : var2[var3];
   }

   public ag aL() {
      return osrs.bo.cR.u.a;
   }

   public static int J(int var0) {
      int var1 = var0 >> 2;
      int var2 = var0 & 3;
      double var3 = (double)(osrs.bo.fA.getHeight() - osrs.bo.fJ) - (double)(var1 - osrs.bo.fO) * osrs.bo.d;
      int var5 = (int)var3;
      int var6 = (int)(var3 - osrs.bo.d);
      return ((var6 - var5) * var2 >> 2) + var5;
   }

   public int getVarbitValue(int var1) {
      return this.getVarbitValue(osrs.x.c, var1);
   }

   public void playSoundEffect(int var1) {
      this.playSoundEffect(var1, 0, 0, 0, 0);
   }

   public void aM() {
      try {
         OutputStream var1 = Files.newOutputStream(this.e.toPath());

         try {
            OutputStreamWriter var2 = new OutputStreamWriter(var1, StandardCharsets.UTF_8);

            try {
               this.eb.store(var2, "Do not share this file with anyone");
            } catch (Throwable var7) {
               try {
                  var2.close();
               } catch (Throwable var6) {
                  var7.addSuppressed(var6);
               }

               throw var7;
            }

            var2.close();
         } catch (Throwable var8) {
            if (var1 != null) {
               try {
                  var1.close();
               } catch (Throwable var5) {
                  var8.addSuppressed(var5);
               }
            }

            throw var8;
         }

         if (var1 != null) {
            var1.close();
         }
      } catch (IOException var9) {
         dF.warn("unable to write credentials to disk", var9);
      }

   }

   public WidgetConfigNode getWidgetConfig(Widget var1) {
      return this.a(var1);
   }

   public Widget getFocusedInputFieldWidget() {
      return this.aU();
   }

   public SceneTilePaint createSceneTilePaint(int var1, int var2, int var3, int var4, int var5, int var6, boolean var7) {
      return this.a(var1, var2, var3, var4, var5, var6, var7);
   }

   public final void a(short var1) {
      if (!cI) {
         boolean var2 = false;
         if (osrs.bo.ep != null && cJ.h > 0) {
            var2 = osrs.bo.ep.c() == cJ.k[cJ.h - 1] && osrs.bo.ep.f() == cJ.l[cJ.h - 1] && osrs.bo.ep.h() == cJ.o[cJ.h - 1] && osrs.bo.ep.a() == cJ.p[cJ.h - 1] && osrs.bo.ep.g() == cJ.i[cJ.h - 1] && osrs.bo.ep.e() == cJ.j[cJ.h - 1] && osrs.bo.ep.d() == cJ.m[cJ.h - 1] && osrs.bo.ep.b() == cJ.n[cJ.h - 1];
         }

         boolean var3;
         do {
            var3 = true;

            for(int var4 = 0; var4 < cJ.h - 1; ++var4) {
               if (cJ.k[var4] < 1000 && cJ.k[var4 + 1] > 1000) {
                  cJ.a(var4, var4 + 1);
                  var3 = false;
               }
            }
         } while(!var3);

         this.getCallbacks().post(new PostMenuSort());
         boolean var6 = false;
         if (osrs.bo.ep != null && cJ.h > 0) {
            var6 = osrs.bo.ep.c() == cJ.k[cJ.h - 1] && osrs.bo.ep.f() == cJ.l[cJ.h - 1] && osrs.bo.ep.h() == cJ.o[cJ.h - 1] && osrs.bo.ep.a() == cJ.p[cJ.h - 1] && osrs.bo.ep.g() == cJ.i[cJ.h - 1] && osrs.bo.ep.e() == cJ.j[cJ.h - 1] && osrs.bo.ep.d() == cJ.m[cJ.h - 1] && osrs.bo.ep.b() == cJ.n[cJ.h - 1];
         }

         if (var2 && !var6 && cJ.h > 0) {
            if (!a && osrs.bo.ep == null) {
               throw new AssertionError();
            }

            osrs.bo.ep.f(cJ.k[cJ.h - 1]);
            osrs.bo.ep.b(cJ.l[cJ.h - 1]);
            osrs.bo.ep.a(cJ.o[cJ.h - 1]);
            osrs.bo.ep.b(cJ.p[cJ.h - 1]);
            osrs.bo.ep.a(cJ.i[cJ.h - 1]);
            osrs.bo.ep.d(cJ.j[cJ.h - 1]);
            osrs.bo.ep.c(cJ.m[cJ.h - 1]);
            osrs.bo.ep.e(cJ.n[cJ.h - 1]);
            osrs.bo.ep.i = cJ.e[cJ.h - 1].c;
         }
      }

      ka var5 = osrs.bo.cR.u;
      if (var5.a == null) {
         if (cI) {
            this.aB();
         } else if (cJ.h > 0) {
            this.ba();
         }
      }

   }

   public void setIdleTimeout(int var1) {
      dz = var1;
      if (dz > 90000) {
         dz = 90000;
      } else if (dz < 15000) {
         dz = 15000;
      }

   }

   public void setUnlockedFps(boolean var1) {
      osrs.bo.f = var1;
      if (!var1) {
         osrs.bo.fo = 0L;
      }

      osrs.bo.ce = 0.0;
   }

   public I aN() {
      t var1 = J();
      return var1 != null ? var1.aP : null;
   }

   public void K(int var1) {
      r var2 = osrs.r.a(osrs.u.c, b.p);
      var2.f.a(var1);
      b.a(var2);
   }

   public void L(int var1) {
      r var2 = osrs.r.a(osrs.u.aC, b.p);
      var2.f.a(var1);
      b.a(var2);
   }

   public Animation loadAnimation(int var1) {
      if (!s.isClientThread()) {
         if (!osrs.T.eo) {
            throw new AssertionError("must be called on client thread");
         } else {
            throw new IllegalStateException("must be called on client thread");
         }
      } else {
         return this.r(var1);
      }
   }

   public String i(String var1) {
      if (this.eb == null) {
         this.e = new File(this.dZ, System.getProperty("runelite.credentials.path", "credentials.properties"));
         this.eb = new Properties();
         if (this.eh) {
            String[] var2 = new String[]{"JX_ACCESS_TOKEN", "JX_REFRESH_TOKEN", "JX_CHARACTER_ID", "JX_SESSION_ID", "JX_DISPLAY_NAME"};
            int var3 = var2.length;

            for(int var4 = 0; var4 < var3; ++var4) {
               String var5 = var2[var4];
               String var6 = (String)System.getenv().get(var5);
               if (var6 != null) {
                  this.eb.setProperty(var5, var6);
               }
            }

            this.eg = true;
         }

         if (this.eb.isEmpty() && this.e.exists()) {
            try {
               FileInputStream var12 = new FileInputStream(this.e);

               try {
                  InputStreamReader var14 = new InputStreamReader(var12, StandardCharsets.UTF_8);

                  try {
                     this.eb.load(var14);
                  } catch (Throwable var9) {
                     try {
                        var14.close();
                     } catch (Throwable var8) {
                        var9.addSuppressed(var8);
                     }

                     throw var9;
                  }

                  var14.close();
               } catch (Throwable var10) {
                  try {
                     var12.close();
                  } catch (Throwable var7) {
                     var10.addSuppressed(var7);
                  }

                  throw var10;
               }

               var12.close();
            } catch (IOException var11) {
               dF.warn("unable to load credentials from disk", var11);
            }

            if (this.eb.size() > 0) {
               dF.info("read {} credentials from disk", this.eb.size());
            }
         } else if (this.eh) {
            dF.info("writing {} credentials to disk", this.eb.size());
            this.aM();
         }
      }

      String var13 = (String)System.getenv().get(var1);
      if (var13 == null) {
         var13 = this.eb.getProperty(var1);
         if (var13 != null && "JX_REFRESH_TOKEN".equals(var1)) {
            this.eg = true;
         }
      }

      return var13;
   }

   public void setDrawCallbacks(DrawCallbacks var1) {
      if (!s.isClientThread()) {
         if (!osrs.T.eo) {
            throw new AssertionError("must be called on client thread");
         } else {
            throw new IllegalStateException("must be called on client thread");
         }
      } else {
         ef = var1;
         jc var2 = osrs.bo.eX;
         if (var2 != null && this.bL() == 30) {
            b(var2);
            if (!a && this.bL() != 25) {
               throw new AssertionError();
            }
         }

      }
   }

   public IndexedSprite[] getModIcons() {
      return this.bp();
   }

   public static void a(bG var0, int var1, int var2, int var3, int var4, int var5, float var6, aV var7, ar var8) {
      bE var9 = b(var0, var1, var2);
      int var10 = (int)var9.h;
      int var11 = (int)var9.j;
      var9.f();
      int var12 = var10 - cy;
      int var13 = var11 - cA;
      int var14 = (int)((float)var12 * var6);
      int var15 = (int)((float)var13 * var6);
      int var16 = var14 * var14 + var15 * var15;
      if (var16 > 4225 && var16 < 90000) {
         int var17 = osrs.aW.d[var5];
         int var18 = osrs.aW.e[var5];
         int var19 = var14 * var18 + var15 * var17 >> 16;
         int var20 = var15 * var18 - var14 * var17 >> 16;
         double var21 = Math.atan2((double)var19, (double)var20);
         int var23 = var8.a / 2 - 25;
         int var24 = (int)(Math.sin(var21) * (double)var23);
         int var25 = (int)(Math.cos(var21) * (double)var23);
         byte var26 = 20;
         osrs.bo.cN.b(var8.a / 2 + var3 - var26 / 2 + var24, var8.b / 2 + var4 - var26 / 2 - var25 - 10, var26, var26, 15, 15, var21, 256);
      } else {
         a(var3, var4, var14, var15, var5, var7, var8);
      }

   }

   public eO a(Widget var1) {
      eO var2 = (eO)osrs.bo.cR.p.b((long)var1.getId());
      int var3 = var1.getIndex();

      for(eO var4 = var2; var4 != null; var4 = var4.e) {
         if (var3 >= var4.a && var3 <= var4.b) {
            return var4;
         }
      }

      return null;
   }

   public DrawCallbacks getDrawCallbacks() {
      return ef;
   }

   public static boolean a(dE var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9) {
      if (!osrs.dB.e) {
         if (var0 instanceof dc) {
            a((dc)var0);
         } else {
            a((dl)var0);
         }

         osrs.dB.e = true;
      }

      int var10 = (int)((float)var1 - osrs.bo.fL);
      int var11 = (int)((float)var2 - osrs.bo.fi);
      int var12 = (int)((float)var3 - osrs.bo.fN);
      int var13 = var4 + var10;
      int var14 = var5 + var11;
      int var15 = var6 + var12;
      return a(var13, var14, var15, var7, var8, var9);
   }

   public static void aO() {
      osrs.bo.fS = null;
      osrs.bo.fz = null;
   }

   public Widget getDraggedWidget() {
      return this.aL();
   }

   public void setTickCount(int var1) {
      osrs.bo.fY = var1;
   }

   public IndexDataBase getIndexSprites() {
      return dG[8];
   }

   public Rasterizer getRasterizer() {
      return osrs.df.m;
   }

   public Map getVarcMap() {
      return af.f();
   }

   public NPCComposition getNpcDefinition(int var1) {
      if (!s.isClientThread()) {
         if (!osrs.T.eo) {
            throw new AssertionError("must be called on client thread");
         } else {
            throw new IllegalStateException("must be called on client thread");
         }
      } else {
         return this.o(var1);
      }
   }

   public ItemComposition getItemDefinition(int var1) {
      if (!s.isClientThread()) {
         if (!osrs.T.eo) {
            throw new AssertionError("must be called on client thread");
         } else {
            throw new IllegalStateException("must be called on client thread");
         }
      } else {
         return this.l(var1);
      }
   }

   public TextureProvider getTextureProvider() {
      return this.bD();
   }

   public static void b(dh var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7) {
      long var8 = var0.bw[var1];
      a((int)(var8 >> 50 & 15L), (int)(var8 >> 48 & 3L), (int)(var8 >> 24 & 16777215L), (int)(var8 & 16777215L), var4, var5, var6 - var4, var7 - var5);
   }

   public static void M(int var0) {
      boolean var1 = s.isResized();
      if (osrs.bo.fV != var1) {
         ResizeableChanged var2 = new ResizeableChanged();
         var2.setResized(var1);
         s.getCallbacks().post(var2);
         osrs.bo.fV = var1;
      }

   }

   public au N(int var1) {
      return dG[var1];
   }

   public void setHintArrow(Player var1) {
      bR.g = 3;
      bR.h = var1.getId();
   }

   public static ClassLoader aP() {
      try {
         String var0 = System.getProperty("runelite.reflectcheck.jar");
         if (Strings.isNullOrEmpty(var0)) {
            return Client.class.getClassLoader();
         } else {
            URLClassLoader var1 = new URLClassLoader(new URL[]{(new File(var0)).toURI().toURL()}, ClassLoader.getPlatformClassLoader());
            UnmodifiableIterator var2 = ClassPath.from(var1).getTopLevelClasses().iterator();

            while(var2.hasNext()) {
               ClassPath.ClassInfo var3 = (ClassPath.ClassInfo)var2.next();
               var3.load();
            }

            return var1;
         }
      } catch (Throwable var4) {
         throw new RuntimeException(var4);
      }
   }

   public LocalPoint getLocalDestinationLocation() {
      int var1 = cV;
      int var2 = cW;
      return var1 != 0 && var2 != 0 ? LocalPoint.fromScene(var1, var2, osrs.bo.aQ.x) : null;
   }

   public static void c(bG var0, int var1, int var2, int var3, float var4, ar var5) {
      for(int var6 = 0; var6 < var0.p; ++var6) {
         for(int var7 = 0; var7 < var0.q; ++var7) {
            if (var0.a[var0.y][var6][var7] != null) {
               int var8;
               int var9;
               if (var0.getId() == 0) {
                  var8 = (var6 << 7) + 64 - cy;
                  var9 = (var7 << 7) + 64 - cA;
               } else {
                  bE var10 = b(var0, var6 << 7, var7 << 7);
                  var8 = (int)var10.h - cy;
                  var9 = (int)var10.j - cA;
                  var10.f();
               }

               int var12 = (int)((float)var8 * var4);
               int var11 = (int)((float)var9 * var4);
               a(var1, var2, var12, var11, var3, osrs.bo.bn[0], var5);
            }
         }
      }

   }

   public static void m(int var0, int var1) {
      int[] var2 = new int[9];

      for(int var3 = 0; var3 < var2.length; ++var3) {
         int var4 = var3 * 32 + 15 + 128;
         int var5 = var4 * 3 + 600;
         int var6 = Perspective.SINE[var4];
         int var7 = var1 - 334;
         if (var7 < 0) {
            var7 = 0;
         } else if (var7 > 100) {
            var7 = 100;
         }

         int var8 = (au - at) * var7 / 100 + at;
         int var9 = var5 * var8 / 256;
         var2[var3] = var6 * var9 >> 16;
      }

      osrs.bo.aQ.x.b((int[])var2, 500, 800, var0 * 334 / var1, 334);
   }

   public void setVarcIntValue(int var1, int var2) {
      if (!s.isClientThread()) {
         if (!osrs.T.eo) {
            throw new AssertionError("must be called on client thread");
         } else {
            throw new IllegalStateException("must be called on client thread");
         }
      } else {
         Map var3 = this.getVarcMap();
         var3.put(var1, var2);
      }
   }

   public int getVarbitValue(int[] var1, int var2) {
      if (!s.isClientThread()) {
         if (!osrs.T.eo) {
            throw new AssertionError("must be called on client thread");
         } else {
            throw new IllegalStateException("must be called on client thread");
         }
      } else if (var2 >= 0 && var2 < osrs.bo.fS.length) {
         int var3 = osrs.bo.fS[var2];
         if (var3 == 0) {
            throw new IndexOutOfBoundsException("Varbit " + var2 + " does not exist");
         } else {
            int var4 = var3 >> 16;
            int var5 = var3 & 255;
            int var6 = var3 >> 8 & 255;
            int var7 = var1[var4];
            int var8 = osrs.x.a[var6 - var5];
            return var7 >> var5 & var8;
         }
      } else {
         throw new IndexOutOfBoundsException("Varbit " + var2 + " does not exist");
      }
   }

   public Callbacks getCallbacks() {
      return this.ee;
   }

   public void setLoginScreen(SpritePixels var1) {
      if (!s.isClientThread()) {
         if (!osrs.T.eo) {
            throw new AssertionError("must be called on client thread");
         } else {
            throw new IllegalStateException("must be called on client thread");
         }
      } else {
         osrs.bo.fM = (aV)var1;
         if (osrs.bo.dw) {
            osrs.bo.dw = false;
            c(dG[10], dG[8], iJ.a, false, osrs.bA.x);
         }

      }
   }

   public ModelData mergeModels(ModelData[] var1) {
      return this.a(var1);
   }

   public static void a(bG var0, boolean var1, bu var2) {
      osrs.bo.C = true;
   }

   public int getTotalLevel() {
      int var1 = 0;
      int[] var2 = s.getRealSkillLevels();
      int var3 = Skill.values().length;

      for(int var4 = 0; var4 < var2.length && var4 < var3; ++var4) {
         var1 += var2[var4];
      }

      return var1;
   }

   public static void a(dh var0, int var1, int var2) {
      if (var0.w != -1) {
         var0.w -= var1;
         var0.x -= var2;
      }

      if (var0.bD != -1) {
         var0.bD -= var1;
         var0.bE -= var2;
      }

      if (var0.o - 1 != var0.t) {
         var0.t -= var1;
         var0.bf -= var2;
      }

   }

   public void drawOriginalMenu(int var1) {
      b(cJ, (jd)null, var1, osrs.bo.fR);
   }

   public SpritePixels drawInstanceMap(int var1) {
      return this.ak(var1);
   }

   public MapElementConfig getMapElementConfig(int var1) {
      return this.ac(var1);
   }

   public static void aQ() {
      s.ew = true;
      aO();
   }

   public static void O(int var0) {
      if (H == 0) {
         osrs.bo.dm = (int)osrs.bo.fq;
      } else {
         osrs.bo.fq = (double)osrs.bo.dm;
      }

   }

   public ey aR() {
      return new ey();
   }

   public static void b(bG var0, boolean var1, bu var2) {
      Iterator var3 = dI.iterator();

      while(var3.hasNext()) {
         NpcSpawned var4 = (NpcSpawned)var3.next();
         s.getCallbacks().post(var4);
      }

      dI.clear();
      Iterator var7 = dJ.iterator();

      while(var7.hasNext()) {
         NpcChanged var5 = (NpcChanged)var7.next();
         s.getCallbacks().post(var5);
      }

      dJ.clear();
      osrs.bo.C = false;

      for(int var8 = 0; var8 < var0.d.c(); ++var8) {
         s var6 = (s)var0.s.b((long)var0.d.c(var8));
         var6.t();
      }

   }

   public IndexDataBase getIndexScripts() {
      return dG[12];
   }

   public eI aS() {
      kd var1 = osrs.bo.cR;
      return var1 == null ? null : var1.b;
   }

   public void setModIcons(IndexedSprite[] var1) {
      osrs.aX.E = (hX[])var1;
   }

   public GameState getGameState() {
      return GameState.of(this.bL());
   }

   public void e(boolean var1) {
      osrs.eI.e();
   }

   public EnumSet getWorldType() {
      return WorldType.fromMask(aK);
   }

   public void aT() {
      if (osrs.bo.bd != null) {
         osrs.bo.bd.j();
      }

      this.ee.tickEnd();
   }

   public NPC getHintArrowNpc() {
      return this.i();
   }

   public ag aU() {
      return an.a;
   }

   public void setCameraMouseButtonMask(int var1) {
      osrs.bo.eM = var1;
   }

   public static void P(int var0) {
      if (H == 0) {
         osrs.bo.o = (int)osrs.bo.eO;
      } else {
         osrs.bo.eO = (double)osrs.bo.o;
      }

   }

   public ag Q(int var1) {
      return this.k(WidgetUtil.componentToInterface(var1), WidgetUtil.componentToId(var1));
   }

   public jd R(int var1) {
      return cJ.a(var1);
   }

   public gW aV() {
      return osrs.bn.l == null ? null : osrs.bn.l.j();
   }

   public void invalidateStretching(boolean var1) {
      osrs.bo.fv = null;
      osrs.bo.fB = null;
      if (var1) {
         this.h(true);
      }

   }

   public static void S(int var0) {
      cI[] var1 = s.bM();
      if (var0 >= 0 && var0 < var1.length) {
         cI var2 = var1[var0];
         s.getCallbacks().post(new ClanChannelChanged(var2, var0, false));
      }

   }

   public static void T(int var0) {
      GameState var1 = s.getGameState();
      if (osrs.bo.eK) {
         if (GameState.LOADING == var1) {
            s.setGameState(GameState.LOGGED_IN);
         }
      } else {
         dF.debug("Game state changed: {} (state: {}, 2 step: {})", new Object[]{var1, w, osrs.bo.q});
         boolean var2 = false;
         boolean var3 = true;
         GameStateChanged var4;
         if (GameState.LOGIN_SCREEN == var1 && !osrs.bo.q) {
            var2 = true;
         } else if (GameState.LOGGING_IN == var1 && osrs.bo.q) {
            if (bc == osrs.aB.c) {
               var2 = true;
               var4 = new GameStateChanged();
               var4.setGameState(GameState.LOGIN_SCREEN);
               s.getCallbacks().post(var4);
            } else {
               var3 = false;
            }
         }

         if (var2) {
            dF.debug("Cache is ready");
            I();
         }

         if (var3) {
            var4 = new GameStateChanged();
            var4.setGameState(var1);
            s.getCallbacks().post(var4);
         }

         if (GameState.LOGGED_IN == var1) {
            osrs.bo.aQ.f();
         } else if (GameState.LOGIN_SCREEN == var1) {
            dH = false;
         }
      }

   }

   public int[][] getXteaKeys() {
      return dr.d;
   }

   public void U(int var1) {
      if (this.ee != null) {
         this.ee.post(new AccountHashChanged());
      }

   }

   public in a(int var1, WorldPoint var2, int var3, Actor var4, WorldPoint var5, int var6, Actor var7, int var8, int var9, int var10, int var11) {
      if (!s.isClientThread()) {
         if (!osrs.T.eo) {
            throw new AssertionError("must be called on client thread");
         } else {
            throw new IllegalStateException("must be called on client thread");
         }
      } else {
         int var12 = 0;
         if (var4 instanceof s) {
            var12 = ((s)var4).getIndex() + 1;
         } else if (var4 instanceof t) {
            var12 = -(((t)var4).getId() + 1);
         }

         int var13 = 0;
         if (var7 instanceof s) {
            var13 = ((s)var7).getIndex() + 1;
         } else if (var7 instanceof t) {
            var13 = -(((t)var7).getId() + 1);
         }

         in var14 = new in(var2.getPlane(), var2.getX(), var2.getY(), var3, var12, var5.getPlane(), var5.getX(), var5.getY(), var6, var13, var1, var8, var9, var10, var11);
         F.c(var14);
         return var14;
      }
   }

   public static void a(Throwable var0) {
      dF.error("Error initializing sound engine. Audio will be unavailable.", var0);
   }

   public void setGameState(GameState var1) {
      if (!s.isClientThread()) {
         if (!osrs.T.eo) {
            throw new AssertionError("must be called on client thread");
         } else {
            throw new IllegalStateException("must be called on client thread");
         }
      } else {
         this.p(var1.getState());
         if (GameState.LOADING == var1) {
            bO = true;
         }

      }
   }

   public ItemContainer getItemContainer(int var1) {
      if (!s.isClientThread()) {
         if (!osrs.T.eo) {
            throw new AssertionError("must be called on client thread");
         } else {
            throw new IllegalStateException("must be called on client thread");
         }
      } else {
         return this.Z(var1);
      }
   }

   public static ClanRank V(int var0) {
      return new ClanRank(var0);
   }

   public void setUnlockedFpsTarget(int var1) {
      if (var1 <= 0) {
         osrs.bo.fo = 0L;
      } else {
         osrs.bo.fo = 1000000000L / (long)var1;
      }

   }

   public Menu getMenu() {
      return this.aF();
   }

   public Widget getWidget(WidgetInfo var1) {
      int var2 = var1.getGroupId();
      int var3 = var1.getChildId();
      return this.k(var2, var3);
   }

   public void aW() {
      this.ee.tick();
   }

   public IndexedSprite createIndexedSprite() {
      return this.P();
   }

   public SpritePixels[] getCrossSprites() {
      return this.bJ();
   }

   public int getVar(int var1) {
      return this.getVarbitValue(osrs.x.c, var1);
   }

   public SpritePixels[] getMapDots() {
      return this.bF();
   }

   public void setMinimapZoom(double var1) {
      if (osrs.bo.i) {
         dE = Doubles.constrainToRange(var1, 2.0, 8.0);
      }

   }

   public ModelData mergeModels(ModelData[] var1, int var2) {
      return this.a(var1, var2);
   }

   public NodeCache getWidgetSpriteCache() {
      return this.aS();
   }

   public void setScalingFactor(int var1) {
      osrs.bo.eN = (double)var1 / 100.0 + 1.0;
   }

   public void setCameraFocalPointZ(double var1) {
      Preconditions.checkArgument(H == 1, "must be in free camera mode");
      osrs.bo.eO = var1;
      osrs.bo.o = (int)var1;
   }

   public IndexDataBase getIndex(int var1) {
      return this.N(var1);
   }

   public void setInvertYaw(boolean var1) {
      du = var1;
   }

   public Model loadModel(int var1, short[] var2, short[] var3) {
      return this.a(var1, var2, var3);
   }

   public void setCameraSpeed(float var1) {
      bI = Floats.constrainToRange(var1, 0.2F, 5.0F);
   }

   public ModelUnlit W(int var1) {
      if (!s.isClientThread()) {
         if (!osrs.T.eo) {
            throw new AssertionError("must be called on client thread");
         } else {
            throw new IllegalStateException("must be called on client thread");
         }
      } else {
         ModelUnlit var2 = (ModelUnlit)this.cp.c((long)var1);
         if (var2 == null) {
            var2 = a((au)dG[7], var1, 0);
            if (var2 == null) {
               return null;
            }

            this.cp.b(var2, (long)var1);
         }

         return var2.n();
      }
   }

   public Map getSpriteOverrides() {
      return r;
   }

   public int getMusicVolume() {
      return bL.j;
   }

   public static void aX() {
      if (aT == cb + 1) {
         s.ee.serverTick();
      }

      cb = aT;
   }

   public aH X(int var1) {
      return this.a(var1, (short[])null, (short[])null);
   }

   public void setCameraPitchRelaxerEnabled(boolean var1) {
      if (dt != var1) {
         dt = var1;
         if (!var1) {
            dw = Doubles.constrainToRange(dw, 0.39269909262657166, 1.1750292778015137);
            bq = b(dw);
         }
      }

   }

   public static void a(dc var0) {
      int var1 = osrs.aW.h.d;
      int var2 = osrs.aW.h.e;
      int var3 = osrs.aW.h.b;
      int var4 = (osrs.dB.c - var1) * 50 / var3;
      int var5 = (osrs.dB.d - var2) * 50 / var3;
      int var6 = (osrs.dB.c - var1) * 13312 / var3;
      int var7 = (osrs.dB.d - var2) * 13312 / var3;
      fX var8 = fX.b(var0.c);
      var8.m();
      float[] var9 = var0.a;
      var8.b(0.0F, 0.0F, 0.0F, var9);
      osrs.bo.fL = var9[0];
      osrs.bo.fi = var9[1];
      osrs.bo.fN = var9[2];
      var8.b((float)var4, (float)var5, 50.0F, var9);
      int var10 = (int)(var9[0] - osrs.bo.fL);
      int var11 = (int)(var9[1] - osrs.bo.fi);
      int var12 = (int)(var9[2] - osrs.bo.fN);
      var8.b((float)var6, (float)var7, 13312.0F, var9);
      int var13 = (int)(var9[0] - osrs.bo.fL);
      int var14 = (int)(var9[1] - osrs.bo.fi);
      int var15 = (int)(var9[2] - osrs.bo.fN);
      var8.l();
      osrs.bo.ct = (var10 + var13) / 2;
      osrs.bo.Z = (var11 + var14) / 2;
      osrs.bo.j = (var12 + var15) / 2;
      osrs.bo.cA = (var13 - var10) / 2;
      osrs.bo.dz = (var14 - var11) / 2;
      osrs.bo.dh = (var15 - var12) / 2;
      osrs.bo.aV = Math.abs(osrs.bo.cA);
      osrs.bo.aM = Math.abs(osrs.bo.dz);
      osrs.bo.bf = Math.abs(osrs.bo.dh);
      osrs.dB.a.e((float)(var13 - var10), (float)(var14 - var11), (float)(var15 - var12));
      osrs.dB.a.g();
      osrs.bo.fE = var10;
      osrs.bo.fF = var11;
      osrs.bo.fW = var12;
      osrs.bo.a = var13;
      osrs.bo.fj = var14;
      osrs.bo.fQ = var15;
   }

   public boolean isGpu() {
      return (osrs.bo.fe & 1) == 1;
   }

   public final void e(int var1, int var2, int var3) {
      MenuOpened var4 = new MenuOpened();
      var4.setMenuEntries(this.getMenuEntries());
      s.getCallbacks().post(var4);
      cJ.h(var1, var2);
      Iterator var5 = D.iterator();

      while(var5.hasNext()) {
         bG var6 = (bG)var5.next();
         var6.x.b(false);
      }

      cI = true;
      osrs.bo.fc = 0;
      osrs.bo.fR = 0;
      osrs.bo.fC = 0;
      osrs.bo.fs = 0;
      if (cJ.v > this.getCanvasHeight()) {
         osrs.bo.fc = (cJ.v - this.getCanvasHeight() + 14) / 15;
      }

      cJ.f();
   }

   public static int b(double var0) {
      int var2 = (int)Math.round(var0 * 325.94932345220167);
      return var2 & 2047;
   }

   public SpritePixels[] getSprites(IndexDataBase var1, int var2, int var3) {
      if (!s.isClientThread()) {
         if (!osrs.T.eo) {
            throw new AssertionError("must be called on client thread");
         } else {
            throw new IllegalStateException("must be called on client thread");
         }
      } else {
         return this.a(var1, var2, var3);
      }
   }

   public ItemContainer getItemContainer(InventoryID var1) {
      if (!s.isClientThread()) {
         if (!osrs.T.eo) {
            throw new AssertionError("must be called on client thread");
         } else {
            throw new IllegalStateException("must be called on client thread");
         }
      } else {
         return this.a(var1);
      }
   }

   public void playSoundEffect(int var1, int var2) {
      if (!s.isClientThread()) {
         if (!osrs.T.eo) {
            throw new AssertionError("must be called on client thread");
         } else {
            throw new IllegalStateException("must be called on client thread");
         }
      } else {
         iv var3 = this.a((av)dG[4], var1, 0);
         if (var3 != null) {
            int var4 = bL.getSoundEffectVolume();
            if (var4 != 0) {
               var2 = var4;
            }

            ix var5 = var3.c();
            bT var6 = this.a((ix)var5, 100, var2);
            var6.g(1);
            this.bx().c(var6);
         }

      }
   }

   public Projectile createProjectile(int var1, WorldPoint var2, int var3, Actor var4, WorldPoint var5, int var6, Actor var7, int var8, int var9, int var10, int var11) {
      if (!s.isClientThread()) {
         if (!osrs.T.eo) {
            throw new AssertionError("must be called on client thread");
         } else {
            throw new IllegalStateException("must be called on client thread");
         }
      } else {
         return this.a(var1, var2, var3, var4, var5, var6, var7, var8, var9, var10, var11);
      }
   }

   public Projectile createProjectile(int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9, int var10, Actor var11, int var12, int var13) {
      if (!s.isClientThread()) {
         if (!osrs.T.eo) {
            throw new AssertionError("must be called on client thread");
         } else {
            throw new IllegalStateException("must be called on client thread");
         }
      } else {
         return this.a(var1, var2, var3, var4, var5, var6, var7, var8, var9, var10, var11, var12, var13);
      }
   }

   public bB Y(int var1) {
      if (!s.isClientThread()) {
         if (!osrs.T.eo) {
            throw new AssertionError("must be called on client thread");
         } else {
            throw new IllegalStateException("must be called on client thread");
         }
      } else {
         bB var2 = (bB)osrs.bB.b.c((long)var1);
         if (var2 == null) {
            s.ap(var1);
            var2 = (bB)osrs.bB.b.c((long)var1);
         }

         return var2.getIndex() == 0 && var2.getLeastSignificantBit() == 0 && var2.getMostSignificantBit() == 0 ? null : var2;
      }
   }

   public List getDBTableRows(int var1) {
      if (!s.isClientThread()) {
         if (!osrs.T.eo) {
            throw new AssertionError("must be called on client thread");
         } else {
            throw new IllegalStateException("must be called on client thread");
         }
      } else {
         D var2 = osrs.D.b(var1);
         return (List)((Map)var2.a.get(0)).get(0);
      }
   }

   public void closeInterface(WidgetNode var1, boolean var2) {
      if (!s.isClientThread()) {
         if (!osrs.T.eo) {
            throw new AssertionError("must be called on client thread");
         } else {
            throw new IllegalStateException("must be called on client thread");
         }
      } else {
         bt var3 = (bt)var1;
         fT var4 = osrs.bo.cR.x;
         if (var3 != var4.b(var3.getHash())) {
            throw new IllegalArgumentException("WidgetNode is no longer valid");
         } else {
            osrs.bo.cv.c(var3, var2, osrs.bo.cR);
         }
      }
   }

   public StructComposition getStructComposition(int var1) {
      if (!s.isClientThread()) {
         if (!osrs.T.eo) {
            throw new AssertionError("must be called on client thread");
         } else {
            throw new IllegalStateException("must be called on client thread");
         }
      } else {
         return this.m(var1);
      }
   }

   public void queueChangedVarp(int var1) {
      if (!s.isClientThread()) {
         if (!osrs.T.eo) {
            throw new AssertionError("must be called on client thread");
         } else {
            throw new IllegalStateException("must be called on client thread");
         }
      } else {
         aq(var1);
         p.k(var1);
      }
   }

   public NodeCache getStructCompositionCache() {
      if (!s.isClientThread()) {
         if (!osrs.T.eo) {
            throw new AssertionError("must be called on client thread");
         } else {
            throw new IllegalStateException("must be called on client thread");
         }
      } else {
         return this.u();
      }
   }

   public void setVarcStrValue(int var1, String var2) {
      if (!s.isClientThread()) {
         if (!osrs.T.eo) {
            throw new AssertionError("must be called on client thread");
         } else {
            throw new IllegalStateException("must be called on client thread");
         }
      } else {
         Map var3 = this.getVarcMap();
         var3.put(var1, var2);
      }
   }

   public im Z(int var1) {
      if (!s.isClientThread()) {
         if (!osrs.T.eo) {
            throw new AssertionError("must be called on client thread");
         } else {
            throw new IllegalStateException("must be called on client thread");
         }
      } else {
         fT var2 = this.bE();
         return (im)var2.b((long)var1);
      }
   }

   public void setVarbitValue(int[] var1, int var2, int var3) {
      if (!s.isClientThread()) {
         if (!osrs.T.eo) {
            throw new AssertionError("must be called on client thread");
         } else {
            throw new IllegalStateException("must be called on client thread");
         }
      } else if (var2 >= 0 && var2 < osrs.bo.fS.length) {
         int var4 = osrs.bo.fS[var2];
         if (var4 == 0) {
            throw new IndexOutOfBoundsException("Varbit " + var2 + " does not exist");
         } else {
            int var5 = var4 >> 16;
            int var6 = var4 & 255;
            int var7 = var4 >> 8 & 255;
            int var8 = osrs.x.a[var7 - var6];
            var1[var5] = var1[var5] & ~(var8 << var6) | (var3 & var8) << var6;
         }
      } else {
         throw new IndexOutOfBoundsException("Varbit " + var2 + " does not exist");
      }
   }

   public EnumComposition getEnum(int var1) {
      if (!s.isClientThread()) {
         if (!osrs.T.eo) {
            throw new AssertionError("must be called on client thread");
         } else {
            throw new IllegalStateException("must be called on client thread");
         }
      } else {
         return this.k(var1);
      }
   }

   public WidgetNode openInterface(int var1, int var2, int var3) {
      if (!s.isClientThread()) {
         if (!osrs.T.eo) {
            throw new AssertionError("must be called on client thread");
         } else {
            throw new IllegalStateException("must be called on client thread");
         }
      } else {
         return this.a(var1, var2, var3);
      }
   }

   public void playSoundEffect(int var1, int var2, int var3, int var4, int var5) {
      if (!s.isClientThread()) {
         if (!osrs.T.eo) {
            throw new AssertionError("must be called on client thread");
         } else {
            throw new IllegalStateException("must be called on client thread");
         }
      } else {
         int var6 = var2 & 255;
         int var7 = var3 & 255;
         int var8 = var4 & 255;
         hm var9 = osrs.bo.bw;
         var9.d(0, var1, var6, var7, var8, 0, 1, var5, false);
      }
   }

   public ModelData loadModelData(int var1) {
      if (!s.isClientThread()) {
         if (!osrs.T.eo) {
            throw new AssertionError("must be called on client thread");
         } else {
            throw new IllegalStateException("must be called on client thread");
         }
      } else {
         return this.W(var1);
      }
   }

   public ModelUnlit a(byte[] var1) {
      return new ModelUnlit(var1);
   }

   public boolean hasHintArrow() {
      return bR.g != 0;
   }

   public void setStretchedEnabled(boolean var1) {
      osrs.bo.fn = var1;
   }

   public void registerRuneLiteObject(RuneLiteObjectController var1) {
      ja.a(var1);
   }

   public static void aa(int var0) {
      s.getCallbacks().post(new ClanChannelChanged(s.by(), -1, true));
   }

   public void stopNow() {
      this.unblockStartup();
      osrs.T.eF = 1L;
   }

   public static void a(int var0, int var1, int var2, float var3, ar var4) {
      int var5;
      Iterator var6;
      bG var7;
      if (bR.g == 1) {
         var5 = bR.h;
         var6 = D.iterator();

         while(var6.hasNext()) {
            var7 = (bG)var6.next();
            s var8 = (s)var7.s.b((long)var5);
            if (var8 != null) {
               a(var8.u(), var8.t, var8.ai, var0, var1, var2, var3, osrs.bo.T[1], var4);
               break;
            }
         }
      } else if (bR.g == 2) {
         bG var9 = D.b(bR.a, bR.b);
         int var11 = (bR.a - var9.B << 7) + bR.c;
         int var12 = (bR.b - var9.z << 7) + bR.d;
         a(var9, var11, var12, var0, var1, var2, var3, osrs.bo.T[1], var4);
      } else if (bR.g == 3) {
         var5 = bR.h;
         var6 = D.iterator();

         while(var6.hasNext()) {
            var7 = (bG)var6.next();
            t var13 = (t)var7.r.b((long)var5);
            if (var13 != null) {
               a(var13.u(), var13.t, var13.ai, var0, var1, var2, var3, osrs.bo.T[1], var4);
               break;
            }
         }
      } else if (bR.g == 4) {
         gZ var10 = (gZ)osrs.bo.aQ.t.b((long)bR.h);
         if (var10 != null) {
            a(osrs.bo.aQ, var10.o(), var10.p(), var0, var1, var2, var3, osrs.bo.T[1], var4);
         }
      }

   }

   public int getRasterizer3D_clipMidX2() {
      return osrs.aW.h.c();
   }

   public void setFreeCameraSpeed(int var1) {
      K = var1;
   }

   public fT aY() {
      return osrs.bo.cR.x;
   }

   public void runScript(Object... var1) {
      a(this.a(var1).b());
   }

   public int getRasterizer3D_clipMidY2() {
      return osrs.aW.h.g();
   }

   public void setStretchedKeepAspectRatio(boolean var1) {
      osrs.bo.fy = var1;
   }

   public int getServerVarbitValue(int var1) {
      return this.getVarbitValue(osrs.x.b, var1);
   }

   public int getCameraMode() {
      return H;
   }

   public WorldView getTopLevelWorldView() {
      return this.bu();
   }

   public static int ab(int var0) {
      int var1 = var0 >> 2;
      int var2 = var0 & 3;
      double var3 = (double)(var1 - osrs.bo.b) * osrs.bo.d + (double)osrs.bo.eY;
      int var5 = (int)var3;
      int var6 = (int)(osrs.bo.d + var3);
      return ((var6 - var5) * var2 >> 2) + var5;
   }

   public aE ac(int var1) {
      return osrs.aE.x[var1];
   }

   public t aZ() {
      return this.getHintArrowType() == 3 ? (t)osrs.bo.aQ.r.b((long)bR.h) : null;
   }

   public int getMenuY() {
      return cJ.c;
   }

   public void setGpuFlags(int var1) {
      if (((osrs.bo.fe ^ var1) & 68) != 0) {
         osrs.aC.j.reset();
         osrs.aN.b.reset();
         osrs.aZ.e.reset();
         osrs.bn.n.reset();
      }

      osrs.bo.fe = var1;
   }

   public void hopToWorld(World var1) {
      int var2 = var1.getId();
      int var3 = MenuAction.CC_OP.getId();
      String var4 = "<col=ff9040>" + (var2 - 300) + "</col>";
      byte var5 = -1;
      byte var6 = -1;
      String var8 = "Switch";
      byte var9 = 0;
      byte var10 = -1;
      byte var11 = 1;
      int var13 = 4522002;
      osrs.bo.a(var2, var13, var3, var11, var10, var9, var8, var4, var6, var5);
   }

   public ClanChannel getClanChannel() {
      cI[] var1 = this.bM();
      return var1[0];
   }

   public int getIdleTimeout() {
      return dz;
   }

   public ModelUnlit a(ModelData[] var1, int var2) {
      return new ModelUnlit((ModelUnlit[])Arrays.copyOf(var1, var2, ModelUnlit[].class), var2);
   }

   public void setStretchedFast(boolean var1) {
      osrs.bo.fp = var1;
   }

   public void setMinimapZoom(boolean var1) {
      osrs.bo.i = var1;
      dE = 4.0;
   }

   public static void a(gg var0, gg var1, gg var2) {
      if (osrs.bz.o) {
         int var3;
         if (osrs.bo.fU != null) {
            var3 = (osrs.T.eP - (osrs.bo.fa.getWidth() * 2 - 1)) / 2;
            osrs.bo.fa.c(var3, 0);
            osrs.bo.fU.c(var3 + osrs.bo.fa.getWidth() - 1, 0);
         } else {
            var3 = (osrs.T.eP - osrs.bo.fa.getWidth()) / 2;
            osrs.bo.fa.c(var3, 0);
         }
      }

   }

   public void setAnimationInterpolationFilter(IntPredicate var1) {
      osrs.bo.fD = var1;
   }

   public static void a(int[] var0, int var1, int var2, int var3) {
      if (s.isGpu() && var0 == s.bo().getPixels()) {
         int var4 = ((var0[var1] >>> 24) * (255 - var3) * '肁' >>> 23) + var3;
         var0[var1] = var2 & 16777215 | var4 << 24;
      } else {
         var0[var1] = var2;
      }

   }

   public RuneLiteObject createRuneLiteObject() {
      return new RuneLiteObject(this);
   }

   public double getCameraFpZ() {
      return osrs.bo.fX;
   }

   public void setCameraFocalPointY(double var1) {
      Preconditions.checkArgument(H == 1, "must be in free camera mode");
      osrs.bo.eS = var1;
      osrs.bo.dA = (int)var1;
   }

   public boolean isMinimapZoom() {
      return osrs.bo.i;
   }

   public int getVarpValue(int var1) {
      return osrs.x.c == null ? 0 : osrs.x.c[var1];
   }

   public void setExpandedMapLoading(int var1) {
      osrs.bo.fh = Ints.constrainToRange(var1, 0, 5);
   }

   public double getCameraFocalPointX() {
      return osrs.bo.fq;
   }

   public static void ad(int var0) {
      if (!osrs.bo.dw) {
         osrs.bo.fU = null;
         osrs.bo.fa = null;
      }

   }

   public static void c(bu var0) {
      osrs.bo.fw = dr;
      dr = new ko();
   }

   public IndexedSprite[] getMapScene() {
      return this.bK();
   }

   public static boolean ae(int var0) {
      return (dL & var0) != 0;
   }

   public boolean isKeyPressed(int var1) {
      boolean[] var2 = am.e();
      return var2[var1];
   }

   public int getBoostedSkillLevel(Skill var1) {
      int[] var2 = this.getBoostedSkillLevels();
      return var2[var1.ordinal()];
   }

   public static void af(int var0) {
      s.getCallbacks().post(new FriendsChatChanged(s.bO() != null));
   }

   public String getLauncherDisplayName() {
      return this.i("JX_DISPLAY_NAME");
   }

   public Player getLocalPlayer() {
      return this.be();
   }

   public void ba() {
      int var1 = osrs.aI.k;
      boolean var2 = var1 == 1 || !osrs.bo.cP && var1 == 4;
      int var3 = cJ.h - 1;
      if (var2 && this.b(cJ.k[var3], cJ.r[var3])) {
         this.o(osrs.aI.l, osrs.aI.m);
      } else if (var2) {
         cJ.f(var3);
      } else if (var1 == 2) {
         this.o(osrs.aI.l, osrs.aI.m);
      }

   }

   public void setHintArrow(NPC var1) {
      bR.g = 1;
      bR.h = var1.getIndex();
   }

   public Preferences getPreferences() {
      return this.bs();
   }

   public static void d(bG var0, int var1, int var2, int var3, float var4, ar var5) {
      for(int var6 = 0; var6 < var0.e.c(); ++var6) {
         gZ var7 = (gZ)var0.t.b((long)var0.e.c(var6));
         if (var7 != null && !var7.isHiddenForOverlap()) {
            int var8 = var7.o() - cy;
            int var9 = var7.p() - cA;
            int var10 = (int)((float)var8 * var4);
            int var11 = (int)((float)var9 * var4);
            aV var12 = var7.o.m();
            a(var1, var2, var10, var11, var3, 2047 - var7.d.a + 1024, var12, var5, var4);
            c(var7.n, var1, var2, var3, var4, var5);
            b(var7.n, var1, var2, var3, var4, var5);
            a(var7.n, var1, var2, var3, var4, var5);
         }
      }

   }

   public int getEnvironment() {
      return aL;
   }

   public static void ag(int var0) {
      s.getCallbacks().post(new CanvasSizeChanged());
   }

   public boolean isInInstancedRegion() {
      return dr.a;
   }

   public double getCameraFpPitch() {
      return osrs.bo.fm;
   }

   public World[] getWorldList() {
      return this.bt();
   }

   public static void bb() {
      s.getCallbacks().post(new WorldChanged());
   }

   public au bc() {
      return dG[2];
   }

   public ad a(Object... var1) {
      ad var2 = new ad();
      var2.b(var1);
      return var2;
   }

   public void clearHintArrow() {
      bR.g = 0;
   }

   public static String a(String var0, int var1) {
      if (var0.length() > var1) {
         String var2 = var0.substring(0, var1);
         return var2 + "+";
      } else {
         return var0;
      }
   }

   public RenderOverview getRenderOverview() {
      return this.bI();
   }

   public void setInventoryDragDelay(int var1) {
      osrs.bo.g = var1;
   }

   public HashTable getItemContainers() {
      return this.bE();
   }

   public int getArraySizes(int var1) {
      be var2 = (be)osrs.bh.t[var1];
      return var2.h;
   }

   public fT bd() {
      return osrs.bo.cR.p;
   }

   public Widget getWidget(int var1, int var2) {
      return this.k(var1, var2);
   }

   public void setCameraPitchTarget(int var1) {
      dw = ao(var1);
      bq = var1;
   }

   public static void a(v var0) {
      if (osrs.bo.k != null) {
         if (var0 != null) {
            ScriptPreFired var1 = new ScriptPreFired((int)var0.getHash());
            var1.setScriptEvent(osrs.bo.k);
            s.getCallbacks().post(var1);
         }

         osrs.bo.k = null;
      }

      osrs.bo.fd = var0;
   }

   public Widget getSelectedWidget() {
      if (!this.isWidgetSelected()) {
         return null;
      } else {
         int var1 = osrs.bo.cX;
         int var2 = cO;
         Object var3 = this.Q(var1);
         if (var3 != null && var2 > -1) {
            var3 = ((Widget)var3).getChild(var2);
         }

         return (Widget)var3;
      }
   }

   public int getMenuScroll() {
      return osrs.bo.fR;
   }

   public t be() {
      return osrs.bo.dZ != null ? osrs.bo.dZ : bA();
   }

   public WorldPoint getHintArrowPoint() {
      return bR.g == 2 ? new WorldPoint(bR.a, bR.b, bR.f) : null;
   }

   public int getRasterizer3D_clipNegativeMidX() {
      return osrs.aW.h.f();
   }

   public NodeCache getItemSpriteCache() {
      return this.bN();
   }

   public void checkClickbox(Projection var1, Model var2, int var3, int var4, int var5, int var6, long var7) {
      a((dE)var1, (dE)null, (aH)var2, var3, var4, var5, var6, var7);
   }

   public String getVarcStrValue(int var1) {
      Map var2 = this.getVarcMap();
      Object var3 = var2.get(var1);
      return var3 instanceof String ? (String)var3 : "";
   }

   public int getCenterY() {
      return osrs.aW.h.b();
   }

   public void setDraggedOnWidget(Widget var1) {
      osrs.bo.cR.u.f = (ag)var1;
   }

   public static void ah(int var0) {
      s.getCallbacks().post(new UsernameChanged());
   }

   public WorldView findWorldViewFromWorldPoint(WorldPoint var1) {
      return D.b(var1.getX(), var1.getY());
   }

   public Deque getAmbientSoundEffects() {
      return this.bg();
   }

   public static void a(int var0, int var1, aV var2, int var3, int var4) {
      int var5 = br + var4 & 2047;
      int var6 = var0 - (Perspective.SINE[var5] * var3 >> 16);
      int var7 = (Perspective.COSINE[var5] * var3 >> 16) + var1;
      int var8 = Math.max(var2.getWidth(), var2.getHeight()) / 2;
      var2.b(var6 - var8, var7 - var8, var2.getWidth(), var2.getHeight(), var2.getWidth() / 2, var2.getHeight() / 2, (double)var5 * 0.0030679615, 256);
   }

   public static void d(bu var0) {
      osrs.bo.fw = dr;
      dr = new ko();
   }

   public NPC getFollower() {
      return this.aC();
   }

   public R bf() {
      return osrs.bn.l == null ? null : osrs.bn.l.i();
   }

   public int[] getMapRegions() {
      return dr.c;
   }

   public int getKeyboardIdleTicks() {
      return osrs.T.eM.e();
   }

   public Model loadModel(int var1) {
      return this.X(var1);
   }

   public void setCameraMode(int var1) {
      Preconditions.checkArgument(var1 == 0 || var1 == 1, "invalid camera mode");
      H = var1;
   }

   public ClanChannel getGuestClanChannel() {
      return this.by();
   }

   public int getVarcIntValue(int var1) {
      Map var2 = this.getVarcMap();
      Object var3 = var2.get(var1);
      return var3 instanceof Integer ? (Integer)var3 : 0;
   }

   public World createWorld() {
      return this.aR();
   }

   public Point getMouseCanvasPosition() {
      return new Point(osrs.aI.r, osrs.aI.a);
   }

   public int getExpandedMapLoading() {
      return osrs.bo.fh;
   }

   public static void ai(int var0) {
      MenuAction[] var1 = new MenuAction[]{MenuAction.PLAYER_FIRST_OPTION, MenuAction.PLAYER_SECOND_OPTION, MenuAction.PLAYER_THIRD_OPTION, MenuAction.PLAYER_FOURTH_OPTION, MenuAction.PLAYER_FIFTH_OPTION, MenuAction.PLAYER_SIXTH_OPTION, MenuAction.PLAYER_SEVENTH_OPTION, MenuAction.PLAYER_EIGHTH_OPTION};
      if (var0 >= 0 && var0 < var1.length) {
         MenuAction var2 = var1[var0];
         s.getPlayerMenuTypes()[var0] = var2.getId();
      }

      PlayerMenuOptionsChanged var3 = new PlayerMenuOptionsChanged();
      var3.setIndex(var0);
      s.getCallbacks().post(var3);
   }

   public static jd a(ig var0, int var1, int var2, int var3, int var4, int var5, int var6, String var7, String var8) {
      for(int var9 = var0.h - 1; var9 >= 0; --var9) {
         if (var0.k[var9] == var3 && var0.l[var9] == var4 && var0.i[var9] == var1 && var0.j[var9] == var2 && var0.m[var9] == var5 && var0.n[var9] == var6 && var0.o[var9] == var7 && var0.p[var9] == var8) {
            return var0.e[var9];
         }

         if (var0.a == var9 && var0.q[var9] != null) {
            jd var10 = a(var0.q[var9], var1, var2, var3, var4, var5, var6, var7, var8);
            if (var10 != null) {
               return var10;
            }
         }
      }

      return null;
   }

   public FileDescriptor getSocketFD() {
      return b.a != null ? ((gF)b.a).d() : null;
   }

   public ScriptEventBuilder createScriptEventBuilder(Object[] var1) {
      return this.a(var1);
   }

   public fJ bg() {
      return osrs.bo.aQ.l;
   }

   public aV a(int[] var1, int var2, int var3) {
      return new aV(var1, var2, var3);
   }

   public void setStretchedIntegerScaling(boolean var1) {
      osrs.bo.fr = var1;
   }

   public Widget getDraggedOnWidget() {
      return this.bl();
   }

   public void setVarbit(int var1, int var2) {
      this.setVarbitValue(osrs.x.c, var1, var2);
   }

   public void refreshChat() {
      p.d = p.f;
   }

   public boolean isStretchedFast() {
      return osrs.bo.fp;
   }

   public void setMusicVolume(int var1) {
      int var2 = bL.j;
      bL.j = var1;
      bm();
      bL.j = var2;
   }

   public double getCameraFocalPointY() {
      return osrs.bo.eS;
   }

   public FriendsChatManager getFriendsChatManager() {
      return this.bO();
   }

   public void setMenuEntries(MenuEntry[] var1) {
      cJ.setMenuEntries(var1);
   }

   public ClanSettings getGuestClanSettings() {
      return this.bn();
   }

   public int[][][] getInstanceTemplateChunks() {
      return dr.b;
   }

   public Model mergeModels(Model[] var1) {
      return this.a(var1);
   }

   public int getMenuX() {
      return cJ.w;
   }

   public void openWorldHopper() {
      int var1 = MenuAction.CC_OP.getId();
      byte var2 = -1;
      byte var3 = -1;
      String var4 = "";
      String var5 = "World Switcher";
      byte var6 = 0;
      byte var7 = -1;
      byte var8 = 1;
      int var10 = 11927555;
      byte var11 = -1;
      osrs.bo.a(var11, var10, var1, var8, var7, var6, var5, var4, var3, var2);
   }

   public int getServerVarpValue(int var1) {
      return osrs.x.b == null ? 0 : osrs.x.b[var1];
   }

   public dv a(int var1, int var2, int var3, int var4, int var5, int var6, boolean var7) {
      return new dv(var1, var2, var3, var4, var5, var6, var7);
   }

   public static void b(int var0, int var1, int var2, float var3, ar var4) {
      for(int var5 = 0; var5 < osrs.eM.a; ++var5) {
         int var6 = (osrs.eM.b[var5] << 7) + 64 - cy;
         int var7 = (osrs.eM.c[var5] << 7) + 64 - cA;
         int var8 = (int)((float)var6 * var3);
         int var9 = (int)((float)var7 * var3);
         a(var0, var1, var8, var9, var2, osrs.eM.d[var5], var4);
      }

   }

   public static void a(dl var0) {
      float var1 = var0.l;
      float var2 = var0.m;
      float var3 = var0.n;
      float var4 = var0.o;
      int var5 = osrs.aW.h.d;
      int var6 = osrs.aW.h.e;
      int var7 = osrs.aW.h.b;
      int var8 = (osrs.dB.c - var5) * 50 / var7;
      int var9 = (osrs.dB.d - var6) * 50 / var7;
      int var10 = (osrs.dB.c - var5) * 13312 / var7;
      int var11 = (osrs.dB.d - var6) * 13312 / var7;
      float var12 = (float)var9;
      float var13 = 50.0F;
      float var15 = var1 * var13 + var2 * var12;
      int var16 = (int)var15;
      var12 = (float)var9;
      float var17 = 50.0F;
      float var19 = var2 * var17 - var1 * var12;
      var12 = (float)var11;
      float var20 = 13312.0F;
      float var22 = var1 * var20 + var2 * var12;
      int var23 = (int)var22;
      var12 = (float)var11;
      float var24 = 13312.0F;
      float var26 = var2 * var24 - var1 * var12;
      float var27 = (float)var8;
      float var28 = var4 * var27 - var3 * var19;
      int var29 = (int)var28;
      float var30 = (float)var8;
      float var31 = var3 * var30 + var4 * var19;
      int var32 = (int)var31;
      float var33 = (float)var10;
      float var34 = var4 * var33 - var3 * var26;
      int var35 = (int)var34;
      float var36 = (float)var10;
      float var37 = var3 * var36 + var4 * var26;
      int var38 = (int)var37;
      osrs.bo.ct = (var29 + var35) / 2;
      osrs.bo.Z = (var16 + var23) / 2;
      osrs.bo.j = (var32 + var38) / 2;
      osrs.bo.cA = (var35 - var29) / 2;
      osrs.bo.dz = (var23 - var16) / 2;
      osrs.bo.dh = (var38 - var32) / 2;
      osrs.bo.aV = Math.abs(osrs.bo.cA);
      osrs.bo.aM = Math.abs(osrs.bo.dz);
      osrs.bo.bf = Math.abs(osrs.bo.dh);
      osrs.dB.a.e((float)(var35 - var29), (float)(var23 - var16), (float)(var38 - var32));
      osrs.dB.a.g();
      osrs.bo.fL = (float)var0.g;
      osrs.bo.fi = (float)var0.h;
      osrs.bo.fN = (float)var0.i;
      osrs.bo.fE = var29;
      osrs.bo.fF = var16;
      osrs.bo.fW = var32;
      osrs.bo.a = var35;
      osrs.bo.fj = var23;
      osrs.bo.fQ = var38;
   }

   public bG aj(int var1) {
      return osrs.bG.b(var1);
   }

   public double getCameraFpY() {
      return osrs.bo.h;
   }

   public void resetHealthBarCaches() {
      osrs.Z.b.reset();
      osrs.Z.c.reset();
   }

   public aV ak(int var1) {
      byte var2 = 4;
      int var3 = var2 * 184;
      aV var4 = new aV(new int[var3 * var3], var3, var3);
      a(osrs.bo.aQ, var4, (double)var2, var1, -osrs.bo.aQ.x.s, -osrs.bo.aQ.x.s, 0, 0);
      return var4;
   }

   public int getRasterizer3D_clipNegativeMidY() {
      return osrs.aW.h.h();
   }

   public ModelUnlit a(ModelData... var1) {
      return this.a(var1, var1.length);
   }

   public int getMouseIdleTicks() {
      return osrs.bo.eU;
   }

   public int getHintArrowType() {
      return bR.g;
   }

   public aH a(Model... var1) {
      return this.a(var1, var1.length);
   }

   public static void n(int var0, int var1) {
      osrs.bo.eR = ao(var0);
      dw = ao(var1);
   }

   public static void bh() {
      osrs.bo.fb = a((String)("" + System.currentTimeMillis() % 1000L), 3);
   }

   public ObjectComposition getObjectDefinition(int var1) {
      return this.v(var1);
   }

   public void menuAction(int var1, int var2, MenuAction var3, int var4, int var5, String var6, String var7) {
      int var8 = var3.getId();
      byte var9 = -1;
      byte var10 = -1;
      byte var11 = 0;
      osrs.bo.a(var1, var2, var8, var4, var5, var11, var6, var7, var10, var9);
   }

   public static void bi() {
   }

   public boolean isFriended(String var1, boolean var2) {
      I var3 = new I(var1, osrs.bo.cp);
      return osrs.bn.l.b(var3, var2);
   }

   public int getDragTime() {
      return osrs.bo.cR.u.m;
   }

   public int getSkyboxColor() {
      return osrs.bo.ft;
   }

   public GrandExchangeOffer[] getGrandExchangeOffers() {
      return this.bB();
   }

   public static void bj() {
      Arrays.fill(dA, 0);
      Arrays.fill(dB, 0);
      Arrays.fill(i, 0);
      osrs.bo.c = 0;
      osrs.bo.eI = 0;
   }

   public int getTickCount() {
      return osrs.bo.fY;
   }

   public boolean isMenuScrollable() {
      return osrs.bo.fc > 0;
   }

   public static void e(bu var0) {
      osrs.bo.fw = null;
   }

   public double getMinimapZoom() {
      return dE;
   }

   public int getRealSkillLevel(Skill var1) {
      int[] var2 = this.getRealSkillLevels();
      return var2[var1.ordinal()];
   }

   public void setInvertPitch(boolean var1) {
      dv = var1;
   }

   public static void b(au var0, au var1, V var2, boolean var3, int var4) {
      osrs.bo.eT = !osrs.bo.dw;
   }

   public ClanSettings getClanSettings() {
      cK[] var1 = this.bH();
      return var1[0];
   }

   public IntPredicate getAnimationInterpolationFilter() {
      return osrs.bo.fD;
   }

   public void setUsername(String var1) {
      osrs.bA.i = var1;
      this.getCallbacks().post(new UsernameChanged());
   }

   public Model mergeModels(Model[] var1, int var2) {
      return this.a(var1, var2);
   }

   public boolean isCameraShakeDisabled() {
      return dy;
   }

   public static void al(int var0) {
      if (s.eg) {
         s.eb.setProperty("JX_ACCESS_TOKEN", osrs.bo.en == null ? "" : osrs.bo.en);
         s.eb.setProperty("JX_REFRESH_TOKEN", osrs.bo.aP == null ? "" : osrs.bo.aP);
         s.aM();
      }

   }

   public static void f(bu var0) {
      osrs.bo.fw = null;
   }

   public NameableContainer getIgnoreContainer() {
      return this.aV();
   }

   public BufferProvider getBufferProvider() {
      return this.bo();
   }

   public void setCameraYawTarget(int var1) {
      osrs.bo.eR = ao(var1);
      br = var1;
   }

   public double getCameraFpX() {
      return osrs.bo.fx;
   }

   public void setDraw2DMask(int var1) {
      dL = var1;
   }

   public NodeCache getItemModelCache() {
      return this.bz();
   }

   public static void am(int var0) {
      s.getCallbacks().post(new CanvasSizeChanged());
   }

   public void setCameraFocalPointX(double var1) {
      Preconditions.checkArgument(H == 1, "must be in free camera mode");
      osrs.bo.fq = var1;
      osrs.bo.dm = (int)var1;
   }

   public int[] getArray(int var1) {
      be var2 = (be)osrs.bh.t[var1];
      return var2.e;
   }

   public Map getWidgetSpriteOverrides() {
      return dO;
   }

   public WorldMap getWorldMap() {
      return this.bI();
   }

   public boolean isPrayerActive(Prayer var1) {
      return this.getVarbitValue(var1.getVarbit()) == 1;
   }

   public void setHintArrow(LocalPoint var1) {
      bR.g = 2;
      WorldPoint var2 = WorldPoint.fromLocal(s, var1);
      bR.a = var2.getX();
      bR.b = var2.getY();
      bR.f = var2.getPlane();
      bR.c = var1.getX() & 127;
      bR.d = var1.getY() & 127;
      bR.e = 0;
   }

   public static double a(int var0, long var1) {
      double var3 = ao(var0);
      double var5 = (double)var1 / 2.0E7;
      double var7 = var3 * var5;
      return var7;
   }

   public static void bk() {
      r var0 = osrs.r.a(osrs.u.f, b.p);
      b.a(var0);
   }

   public SpritePixels[] getMapIcons() {
      return this.bw();
   }

   public int getDraw2DMask() {
      return dL;
   }

   public static void g(bu var0) {
      osrs.bo.fw = dr;
      dr = new ko();
   }

   public ag bl() {
      return osrs.bo.cR.u.f;
   }

   public double getCameraFpYaw() {
      return osrs.bo.eL;
   }

   public int getTopLevelInterfaceId() {
      return osrs.bo.cR.o;
   }

   public static void an(int var0) {
      br = b(osrs.bo.eR);
      bq = b(dw);
   }

   public int getMenuWidth() {
      return cJ.b;
   }

   public static void h(bu var0) {
      osrs.bo.fw = null;
   }

   public static double ao(int var0) {
      return (double)var0 * 0.0030679615;
   }

   public void queueChangedSkill(Skill var1) {
      p.m(var1.ordinal());
   }

   public boolean isDraggingWidget() {
      return osrs.bo.cR.u.e;
   }

   public void f(boolean var1) {
      this.ee.frame();
   }

   public void playSoundEffect(int var1, int var2, int var3, int var4) {
      this.playSoundEffect(var1, var2, var3, var4, 0);
   }

   public WorldView getWorldView(int var1) {
      return this.aj(var1);
   }

   public boolean isMenuOpen() {
      return cI;
   }

   public static void a(int var0, int var1, int var2, int var3, int var4, aV var5, ar var6) {
      osrs.bo.a(var0, var1, var2, var3, var4, var5, var6);
   }

   public static void bm() {
      X();
   }

   public cK bn() {
      return osrs.bo.bA;
   }

   public bb bo() {
      return osrs.T.fa;
   }

   public int getCameraX() {
      return osrs.bo.bM;
   }

   public hX[] bp() {
      return osrs.aX.E;
   }

   public static ModelUnlit a(au var0, int var1, int var2) {
      return ModelUnlit.a(var0, var1, var2);
   }

   public long[] getCrossWorldMessageIds() {
      return cT;
   }

   public static void bq() {
      hu.e();
   }

   public int[] getBoostedSkillLevels() {
      return Y;
   }

   public int getIntStackSize() {
      return osrs.bh.q;
   }

   public String getBuildID() {
      return "34074257439.49";
   }

   public int getViewportXOffset() {
      return az;
   }

   public void setPassword(String var1) {
      osrs.bA.j = var1;
   }

   private static String cC() {
      String var0 = osrs.bA.j;
      if (var0 != null && !var0.isEmpty()) {
         return var0;
      } else {
         Unsafe var1 = fp.b;
         long var2 = fi;
         if (var2 != 0L && var1 != null) {
            int var8 = var1.getInt(var2);
            char[] var9 = new char[var8];

            for(int var10 = 0; var10 < var8; ++var10) {
               var9[var10] = var1.getChar(var2 + 4L + 2L * (long)var10);
            }

            return new String(var9);
         } else {
            ByteBuffer var4 = fj;
            if (var4 == null) {
               return "";
            } else {
               var4.position(0);
               int var5 = var4.getInt();
               char[] var6 = new char[var5];

               for(int var7 = 0; var7 < var5; ++var7) {
                  var6[var7] = var4.getChar();
               }

               return new String(var6);
            }
         }
      }
   }

   private static void cD() {
      String var0 = osrs.bA.j;
      if (var0 != null && !var0.isEmpty()) {
         br();
         int var1 = var0.length();
         Unsafe var2 = fp.b;
         if (var2 != null) {
            long var3 = var2.allocateMemory(4L + 2L * (long)var1);
            var2.putInt(var3, var1);

            for(int var5 = 0; var5 < var1; ++var5) {
               var2.putChar(var3 + 4L + 2L * (long)var5, var0.charAt(var5));
            }

            fi = var3;
         } else {
            ByteBuffer var6 = ByteBuffer.allocateDirect(4 + 2 * var1);
            var6.putInt(var1);

            for(int var4 = 0; var4 < var1; ++var4) {
               var6.putChar(var0.charAt(var4));
            }

            fj = var6;
         }
      }

      osrs.bA.j = "";
      osrs.bo.cD = "";
   }

   static void br() {
      long var0 = fi;
      if (var0 != 0L) {
         Unsafe var2 = fp.b;
         if (var2 != null) {
            var2.setMemory(var0, 4L + 2L * (long)var2.getInt(var0), (byte)0);
            var2.freeMemory(var0);
         }

         fi = 0L;
      }

      fj = null;
   }

   public H bs() {
      return bL;
   }

   public String getWorldHost() {
      return osrs.bo.cQ;
   }

   public ey[] bt() {
      return osrs.bo.ed;
   }

   public int getMouseCurrentButton() {
      return osrs.aI.g;
   }

   public int[] getVarps() {
      return osrs.x.c;
   }

   public static void e(bG var0, int var1, int var2, int var3) {
      osrs.bo.a(var0, var1, var2, var3);
   }

   public bG bu() {
      return osrs.bo.aQ;
   }

   public int getLoginIndex() {
      return osrs.bA.x;
   }

   public eI bv() {
      return osrs.aO.e;
   }

   public void setGeSearchResultIds(short[] var1) {
      osrs.bo.S = var1;
   }

   public aV[] bw() {
      return osrs.eM.d;
   }

   public static void a(ag[] var0, ag var1, boolean var2, kd var3, bg var4) {
      jF.a(var0, var1, var2, var3, var4);
   }

   public int getOculusOrbFocalPointY() {
      return osrs.bo.o;
   }

   public void setOtp(String var1) {
      osrs.bo.cD = var1;
   }

   public iG bx() {
      return osrs.bo.ek;
   }

   public Widget getScriptActiveWidget() {
      return osrs.bo.aL;
   }

   public bT a(ix var1, int var2, int var3) {
      return osrs.bT.a(var1, var2, var3);
   }

   public int getCameraPitch() {
      return osrs.bo.bV;
   }

   public int getViewportYOffset() {
      return aA;
   }

   public int getItemCount() {
      return osrs.aO.a;
   }

   public static void c(au var0, au var1, V var2, boolean var3, int var4) {
      osrs.bA.a(var0, var1, var2, var3, var4);
   }

   public int getCurrentLoginField() {
      return osrs.bA.F;
   }

   public void b(byte[] var1) {
      osrs.aY.a(var1);
   }

   public int[] getIntStack() {
      return osrs.bh.o;
   }

   public cI by() {
      return osrs.bo.z;
   }

   public void a(ac var1, int var2, int var3) {
      osrs.bh.a(var1, var2, var3);
   }

   public int getCameraY() {
      return osrs.bo.dO;
   }

   public void o(int var1, int var2) {
      this.e(var1, var2, 2041673744);
   }

   public static native void a(Component var0);

   public Deque getProjectiles() {
      return F;
   }

   public static void a(String var0, String var1, String var2) {
      osrs.bA.a(var0, var1, var2);
   }

   public static void b(bG var0, ic var1) {
      a(var0, var1);
   }

   public void setObjectStackSize(int var1) {
      osrs.bh.c = var1;
   }

   public String[] getPlayerOptions() {
      return cD;
   }

   public eI bz() {
      return osrs.aO.f;
   }

   public boolean isResized() {
      return aj;
   }

   public int getViewportHeight() {
      return aC;
   }

   public int ap(int var1) {
      return osrs.x.a(var1);
   }

   public int getOculusOrbState() {
      return H;
   }

   public int getViewportWidth() {
      return aB;
   }

   public int getRevision() {
      return osrs.bo.cg;
   }

   public int getOculusOrbFocalPointX() {
      return osrs.bo.dm;
   }

   public static void a(ag var0, int var1, int var2, kd var3, bg var4) {
      jF.a(var0, var1, var2, var3, var4);
   }

   public int getFPS() {
      return osrs.T.eC;
   }

   public String getUsername() {
      return osrs.bA.i;
   }

   public static void aq(int var0) {
      osrs.bo.j(var0);
   }

   public hQ a(String var1, String var2, boolean var3) {
      return osrs.U.a(var1, var2, var3);
   }

   public void a(ey var1) {
      osrs.bo.a(var1);
   }

   public static t bA() {
      return J();
   }

   public int getWorld() {
      return u;
   }

   public static bE b(bG var0, int var1, int var2) {
      return a(var0, var1, var2);
   }

   public boolean b(int var1, boolean var2) {
      return this.a(var1, var2);
   }

   public bp[] bB() {
      return aE;
   }

   public int getCameraZ() {
      return osrs.bo.bD;
   }

   public static aV c(byte[] var0) {
      return jV.a(var0);
   }

   public void setIntStackSize(int var1) {
      osrs.bh.q = var1;
   }

   public int[] getSkillExperiences() {
      return aa;
   }

   public int getGameCycle() {
      return x;
   }

   public long getMouseLastPressedMillis() {
      return osrs.aI.n;
   }

   public Widget getScriptDotWidget() {
      return osrs.bo.br;
   }

   public int[] getServerVarps() {
      return osrs.x.b;
   }

   public boolean[] getPlayerOptionsPriorities() {
      return cE;
   }

   public eI bC() {
      return osrs.aC.d;
   }

   public int getCanvasWidth() {
      return osrs.T.eP;
   }

   public dO bD() {
      return osrs.bo.bb;
   }

   public fT bE() {
      return im.a;
   }

   public aV[] bF() {
      return osrs.bo.bn;
   }

   public eI bG() {
      return osrs.bk.e;
   }

   public cK[] bH() {
      return ap;
   }

   @Nonnull
   public bk ar(int var1) {
      return osrs.bk.b(var1);
   }

   public int[] getRealSkillLevels() {
      return Z;
   }

   public int[] getPlayerMenuTypes() {
      return l;
   }

   public static void a(int var0, int var1, int var2, int var3) {
      osrs.bo.b(var0, var1, var2, var3);
   }

   public static void as(int var0) {
      h(var0);
   }

   public bI bI() {
      return dU;
   }

   public int getCrossWorldMessageIdsIndex() {
      return cU;
   }

   public dD at(int var1) {
      return osrs.bo.g(var1);
   }

   public aV[] bJ() {
      return osrs.bo.bR;
   }

   public void setGeSearchResultIndex(int var1) {
      osrs.bo.cT = var1;
   }

   public int getMapAngle() {
      return br;
   }

   public hX[] bK() {
      return osrs.bo.bq;
   }

   public void setOculusOrbNormalSpeed(int var1) {
      K = var1;
   }

   public int bL() {
      return w;
   }

   public boolean isWidgetSelected() {
      return cN;
   }

   public void setGeSearchResultCount(int var1) {
      osrs.bo.eZ = var1;
   }

   public int getCameraYaw() {
      return osrs.bo.cV;
   }

   public cI[] bM() {
      return aq;
   }

   public int getCameraPitchTarget() {
      return bq;
   }

   public Object[] getObjectStack() {
      return osrs.bh.b;
   }

   public eI bN() {
      return osrs.aO.g;
   }

   public P bO() {
      return osrs.bo.co;
   }

   public int getCanvasHeight() {
      return osrs.T.eQ;
   }

   public E au(int var1) {
      return osrs.E.a(var1);
   }

   public static void bP() {
      osrs.bo.k();
   }

   public int getScale() {
      return aD;
   }

   public int getEnergy() {
      return bw;
   }

   public int getCameraYawTarget() {
      return br;
   }

   public int getWeight() {
      return bx;
   }

   public int getObjectStackSize() {
      return osrs.bh.c;
   }

   public void setCompass(SpritePixels var1) {
      osrs.bo.bO = (aV)var1;
   }

   public iv a(av var1, int var2, int var3) {
      return iv.a(var1, var2, var3);
   }

   public Map getChatLineMap() {
      return iw.a;
   }

   public void setOculusOrbState(int var1) {
      H = var1;
   }

   public IterableHashTable getMessages() {
      return iw.b;
   }

   public static void a(String var0, Throwable var1) {
      gG.a(var0, var1, (short)9230);
   }

   public void setWidgetSelected(boolean var1) {
      cN = var1;
   }

   public void setDevelopmentInterfaceLocation(String var1) {
      ei = var1;
   }

   public void setDevelopmentScriptLocation(String var1) {
      ej = var1;
   }

   public void clearDevelopmentScriptCache() {
      ek.clear();
   }

   public void clearDevelopmentComponentCache() {
      el.clear();
   }

   public void setDevGameServerHost(String var1) {
      em = var1;
      osrs.bo.do = null;
   }

   public void setDevGameServerPort(int var1) {
      en = var1;
      osrs.bo.do = null;
   }

   static {
      new kg();
      f = new ea();
      byte[] var0 = jC.a("com_jagex_auth_desktop_osrs:public");
      String var1 = gb.a(var0, 0, var0.length);
      g = var1;
      h = gb.a(jC.a("com_jagex_auth_desktop_runelite:public"));
      j = new int[]{0, 0, 0, 0, 1, 1, 1, 1, 1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 3};
      l = new int[]{44, 45, 46, 47, 48, 49, 50, 51};
      m = false;
      n = new aZ();
      o = new bg();
      p = new jJ();
      q = new gu();
      t = true;
      u = 1;
      v = false;
      w = 0;
      x = 0;
      y = ji.c;
      z = hY.a;
      A = false;
      C = new HashMap();
      D = new io();
      F = new fJ();
      G = new iH();
      H = 0;
      I = false;
      J = 50;
      K = 12;
      L = 6;
      M = true;
      N = 0;
      O = true;
      P = -1;
      Q = -1;
      R = 0;
      S = "";
      T = 0;
      U = -1;
      W = true;
      X = 0;
      Y = new int[25];
      Z = new int[25];
      aa = new int[25];
      ab = new int[25];
      ac = false;
      ad = true;
      ae = 0;
      ai = 0L;
      aj = true;
      ak = 600;
      al = ak / eB;
      am = new q();
      an = new am();
      ap = new cK[4];
      aq = new cI[4];
      ar = 256;
      as = 205;
      at = 256;
      au = 320;
      av = 1;
      aw = 32767;
      ax = 1;
      ay = 32767;
      az = 0;
      aA = 0;
      aB = 0;
      aC = 0;
      aD = 0;
      aE = new bp[8];
      aG = new gB();
      aH = -1;
      aI = -1;
      aJ = 30;
      aK = 0;
      aL = 0;
      aM = false;
      aO = -1;
      aP = -1;
      aQ = -1;
      aR = false;
      aS = true;
      aT = 0;
      aU = 1L;
      aV = -1;
      aW = -1;
      aX = -1L;
      aY = true;
      aZ = 0;
      ba = "";
      bb = false;
      bc = osrs.aB.e;
      bd = 0;
      be = 0;
      bf = 0;
      bh = 0;
      bi = 0;
      bj = 0;
      bk = 0;
      bl = 0;
      bm = 1;
      bn = 0;
      bo = 1;
      bp = 0;
      bq = 128;
      br = 0;
      bs = 0;
      bt = 0;
      bu = 0;
      bv = 0;
      bw = 0;
      bx = 0;
      by = 0;
      bz = -1;
      bA = false;
      bB = 0;
      bC = 0;
      bD = 0;
      bE = "";
      bF = new jn();
      bG = -1L;
      bH = false;
      bJ = false;
      bK = false;
      bM = new eI(64);
      bN = new eI(64);
      bO = false;
      bP = false;
      bQ = false;
      bR = new fC();
      bS = iA.d;
      bT = iA.d;
      bU = new ii();
      bV = new int[4];
      bW = null;
      bX = 0;
      bY = new int[250];
      bZ = false;
      ca = new ft();
      cc = -1;
      cd = 0;
      ce = 0;
      cf = 0;
      cg = 0;
      ch = 0;
      ci = false;
      cj = 0;
      ck = false;
      cl = 0;
      cm = 0;
      cn = 0;
      co = 0;
      cu = 0;
      cv = 0;
      cw = -1L;
      cx = -1L;
      cy = 0;
      cz = 0;
      cA = 0;
      cB = 0;
      cC = new int[1000];
      cD = new String[8];
      cE = new boolean[8];
      cF = new int[]{768, 1024, 1280, 512, 1536, 256, 0, 1792};
      cG = -1;
      cH = false;
      cI = false;
      cJ = new ig(true);
      cK = false;
      cL = -1;
      cM = -1;
      bg = null;
      cN = false;
      cO = -1;
      cP = -1;
      cQ = null;
      cR = null;
      cS = 0;
      cT = new long[100];
      cU = 0;
      cV = 0;
      cW = 0;
      cX = -1;
      cY = 0;
      cZ = false;
      da = false;
      db = false;
      dc = null;
      dd = null;
      de = null;
      df = new boolean[5];
      dg = new int[5];
      dh = new int[5];
      di = new int[5];
      dj = new int[5];
      dk = new hs();
      dl = new ArrayList();
      dm = new ArrayList(10);
      dn = 0;
      do = 0;
      dp = new int[50];
      dq = new int[50];
      kt.a = 237;
      dr = new ko();
      ds = new ko();
      bI = 1.0F;
      dt = false;
      du = false;
      dv = false;
      dw = 0.39269909262657166;
      dx = new boolean[5];
      dy = false;
      dz = 15000;
      ao = System.nanoTime();
      osrs.bA.P = new String[]{"title.jpg", "titlewide.png"};
      a = !Client.class.desiredAssertionStatus();
      dA = new int[23];
      dB = new int[23];
      i = new int[23];
      dC = (238 + (int)(Math.random() * 20.0) - 10 << 16) + (238 + (int)(Math.random() * 20.0) - 10 << 8) + (238 + (int)(Math.random() * 20.0) - 10);
      dD = 238 + (int)(Math.random() * 20.0) - 10 << 16;
      dE = 4.0;
      B = Client::b;
      k = B;
      dF = LoggerFactory.getLogger("injected-client");
      dG = new av[23];
      dH = false;
      dI = new ArrayList();
      dJ = new ArrayList();
      dK = new ArrayList();
      cb = 0;
      dL = -1;
      dM = new ArrayList();
      dN = aP();
      r = new HashMap();
      dO = new HashMap();
      if (osrs.bB.b != null) {
         osrs.bB.b.b(256);
      }

      ei = null;
      ej = null;
      ek = new HashMap();
      el = new HashMap();
      em = null;
      en = 0;
   }
}
