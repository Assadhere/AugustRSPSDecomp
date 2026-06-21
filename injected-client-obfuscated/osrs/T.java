package osrs;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.Panel;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.ImageObserver;
import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.net.URL;
import java.util.Iterator;
import java.util.concurrent.Semaphore;
import net.runelite.api.ClientConfiguration;
import net.runelite.api.Constants;
import net.runelite.api.GameEngine;
import net.runelite.api.events.FocusChanged;

public abstract class T extends Panel implements FocusListener, WindowListener, Runnable, GameEngine, iK {
   public static final boolean eo = !T.class.desiredAssertionStatus();
   public Semaphore ep;
   public final ComponentListener eq = new iN(this);
   public boolean er = false;
   public boolean es = true;
   public boolean et = false;
   public int eu = 0;
   public int ev = 0;
   public volatile boolean ew = true;
   public boolean ex = false;
   public volatile boolean ey = false;
   public volatile long ez = 0L;
   public final EventQueue eA;
   public static int eB = 20;
   public static int eC = 0;
   public static T eD = null;
   public static int eE = 0;
   public static long eF = 0L;
   public static boolean eG = false;
   public static int eH = 1;
   public static long[] eI = new long[32];
   public static long[] eJ = new long[32];
   public static int eK = 500;
   public static volatile boolean eL = true;
   public static iD eM = new iD();
   public static long eN = -1L;
   public static long eO = -1L;
   public static int eP;
   public static int eQ;
   public static int eR;
   public static int eS;
   public int eT;
   public int eU;
   public int eV;
   public int eW;
   public int eX;
   public int eY;
   public bP eZ;
   public static bb fa;
   public Canvas fb;
   public Dimension fc;
   public Frame fd;
   public Clipboard fe;
   public Thread ff;
   public ClientConfiguration fg;
   public static boolean fh;

   public T() {
      EventQueue var1 = null;

      try {
         var1 = Toolkit.getDefaultToolkit().getSystemEventQueue();
      } catch (Throwable var3) {
      }

      this.eA = var1;
      cO var2 = new cO();
      bo.eB = var2;
   }

   public static final int bQ() {
      return eM.a();
   }

   public static final void bR() {
      bo.ai.a();

      int var0;
      for(var0 = 0; var0 < 32; ++var0) {
         eI[var0] = 0L;
      }

      for(var0 = 0; var0 < 32; ++var0) {
         eJ[var0] = 0L;
      }

      eS = 0;
   }

   public static int bS() {
      int var0 = 0;
      if (bo.df == null || !bo.df.isValid()) {
         try {
            Iterator var1 = ManagementFactory.getGarbageCollectorMXBeans().iterator();

            while(var1.hasNext()) {
               GarbageCollectorMXBean var2 = (GarbageCollectorMXBean)var1.next();
               if (var2.isValid()) {
                  bo.df = var2;
                  eO = -1L;
                  eN = -1L;
               }
            }
         } catch (Throwable var9) {
         }
      }

      if (bo.df != null) {
         long var10 = bd.a();
         long var3 = bo.df.getCollectionTime();
         if (eN != -1L) {
            long var5 = var3 - eN;
            long var7 = var10 - eO;
            if (var7 != 0L) {
               var0 = (int)(var5 * 100L / var7);
            }
         }

         eN = var3;
         eO = var10;
      }

      return var0;
   }

   public eg bT() {
      if (this.eZ == null) {
         this.eZ = new bP();
         this.eZ.a(this.fb);
      }

      return this.eZ;
   }

   public void bU() {
      this.fe = this.getToolkit().getSystemClipboard();
   }

   public void j(String var1) {
      this.fe.setContents(new StringSelection(var1), (ClipboardOwner)null);
   }

   public Clipboard bV() {
      return this.fe;
   }

   public final void bW() {
      osrs.eY.a();
      eM.a((Component)this.fb);
   }

   public final void bX() {
      eM.c();
   }

   public void a(ap var1, int var2) {
      eM.a(var1, var2);
   }

   public final void bY() {
      aI.a((Component)this.fb);
   }

   public final void bZ() {
      this.co();
      Container var1 = this.cm();
      if (var1 == null) {
         this.cp();
      } else {
         ba var2 = this.cn();
         this.eV = Math.max(var2.d, this.eX);
         this.eW = Math.max(var2.f, this.eU);
         if (this.eV <= 0) {
            this.eV = 1;
         }

         if (this.eW <= 0) {
            this.eW = 1;
         }

         eP = Math.min(this.eV, this.eY);
         Client.ag(-1);
         eQ = Math.min(this.eW, this.eT);
         Client.am(-1);
         this.eu = (this.eV - eP) / 2;
         this.ev = 0;
         this.fb.setSize(eP, eQ);
         fa = new fx(eP, eQ, this.fb, this.es);
         if (this.fd == var1) {
            Insets var3 = this.fd.getInsets();
            this.fb.setLocation(this.eu + var3.left, this.ev + var3.top);
         } else {
            this.fb.setLocation(this.eu, this.ev);
         }

         this.ew = true;
         this.aG();
         this.cp();
      }

   }

   public void ca() {
      int var1 = this.eu;
      int var2 = this.ev;
      int var3 = this.eV - eP - var1;
      int var4 = this.eW - eQ - var2;
      if (var1 > 0 || var3 > 0 || var2 > 0 || var4 > 0) {
         try {
            Container var5 = this.cm();
            int var6 = 0;
            int var7 = 0;
            if (this.fd == var5) {
               Insets var8 = this.fd.getInsets();
               var6 = var8.left;
               var7 = var8.top;
            }

            Graphics var10 = var5.getGraphics();
            var10.setColor(Color.black);
            if (var1 > 0) {
               var10.fillRect(var6, var7, var1, this.eW);
            }

            if (var2 > 0) {
               var10.fillRect(var6, var7, this.eV, var2);
            }

            if (var3 > 0) {
               var10.fillRect(this.eV + var6 - var3, var7, var3, this.eW);
            }

            if (var4 > 0) {
               var10.fillRect(var6, this.eW + var7 - var4, this.eV, var4);
            }
         } catch (Exception var9) {
         }
      }

   }

   public void b(int var1, int var2, int var3, int var4) {
      try {
         if (eD != null) {
            ++eE;
            if (eE >= 3) {
               this.k("alreadyloaded");
               return;
            }

            this.cr().a(this.cq(), "_self");
            return;
         }

         eD = this;
         eP = var1;
         Client.ag(-1);
         eQ = var2;
         Client.am(-1);
         bo.cg = var3;
         bo.cz = var4;
         bo.eH = this.cs();
         if (bo.aT == null) {
            bo.aT = new dX();
         }

         bo.aT.a((Runnable)this, 1);
      } catch (Exception var6) {
         gG.a((String)null, var6, (short)24205);
         this.k("crash");
      }

   }

   public final synchronized void cb() {
      this.cw();
      Container var1 = this.cm();
      if (this.fb != null) {
         this.fb.removeFocusListener(this);
         var1.remove(this.fb);
      }

      eP = Math.max(var1.getWidth(), this.eX);
      Client.ag(-1);
      eQ = Math.max(var1.getHeight(), this.eU);
      Client.am(-1);
      Insets var2;
      if (this.fd != null) {
         var2 = this.fd.getInsets();
         eP -= var2.right + var2.left;
         Client.ag(-1);
         eQ -= var2.top + var2.bottom;
         Client.am(-1);
      }

      this.fb = new cg(this);
      var1.setBackground(Color.BLACK);
      var1.setLayout((LayoutManager)null);
      var1.add(this.fb);
      this.fb.setSize(eP, eQ);
      this.fb.setVisible(true);
      this.fb.setBackground(Color.BLACK);
      if (this.fd == var1) {
         var2 = this.fd.getInsets();
         this.fb.setLocation(this.eu + var2.left, this.ev + var2.top);
      } else {
         this.fb.setLocation(this.eu, this.ev);
      }

      this.fb.addFocusListener(this);
      this.fb.requestFocus();
      this.ew = true;
      if (fa != null && eP == fa.a && eQ == fa.b) {
         ((fx)fa).a(this.fb);
         fa.a(0, 0, (byte)60);
      } else {
         fa = new fx(eP, eQ, this.fb, this.es);
      }

      this.ey = false;
      this.ez = bd.a();
      this.cv();
   }

   public final boolean cc() {
      String var1 = this.cq().getHost().toLowerCase();
      if (!var1.equals("jagex.com") && !var1.endsWith(".jagex.com")) {
         if (!var1.equals("runescape.com") && !var1.endsWith(".runescape.com")) {
            if (!var1.endsWith("127.0.0.1") && !var1.endsWith(".august-rsps.com") && !var1.endsWith(".august.games")) {
               while(!var1.isEmpty() && var1.charAt(var1.length() - 1) >= '0' && var1.charAt(var1.length() - 1) <= '9') {
                  var1 = var1.substring(0, var1.length() - 1);
               }

               if (var1.endsWith("192.168.1.")) {
                  return true;
               } else {
                  this.k("invalidhost");
                  return false;
               }
            } else {
               return true;
            }
         } else {
            return true;
         }
      } else {
         return true;
      }
   }

   public void cd() {
      long var1 = bd.a();
      long var3 = eJ[eR];
      eJ[eR] = var1;
      eR = eR + 1 & 31;
      if (var3 != 0L && var1 > var3) {
      }

      synchronized(this) {
         fh = eL;
      }

      this.ax();
   }

   public void ce() {
      this.ew = true;
   }

   public void cf() {
      this.ct();
      Container var1 = this.cm();
      long var2 = bd.a();
      long var4 = eI[bo.dP];
      eI[bo.dP] = var2;
      bo.dP = bo.dP + 1 & 31;
      if (var4 != 0L && var2 > var4) {
         int var6 = (int)(var2 - var4);
         eC = ((var6 >> 1) + 32000) / var6;
      }

      if (++eK - 1 > 50) {
         eK -= 50;
         this.ew = true;
         this.fb.setSize(eP, eQ);
         this.fb.setVisible(true);
         if (this.fd == var1) {
            Insets var7 = this.fd.getInsets();
            this.fb.setLocation(this.eu + var7.left, this.ev + var7.top);
         } else {
            this.fb.setLocation(this.eu, this.ev);
         }
      }

      if (this.ey) {
         this.av(125804774);
      }

      this.cg();
      this.c(this.ew);
      if (this.ew) {
         this.ca();
      }

      this.ew = false;
   }

   public final void a(Object var1, int var2) {
      if (!Client.s.isGpu() && this.eA != null) {
         for(int var3 = 0; var3 < 50 && this.eA.peekEvent() != null; ++var3) {
            try {
               Thread.sleep(1L);
            } catch (InterruptedException var5) {
            }
         }

         if (var1 != null) {
            this.eA.postEvent(new ActionEvent(var1, 1001, "dummy"));
         }
      }

      if (Client.ef != null) {
         Client.ef.draw(bo.aD);
      }

   }

   public final void cg() {
      ba var1 = this.cn();
      if (this.eV != var1.d || this.eW != var1.f || this.ex) {
         this.bZ();
         this.ex = false;
      }

   }

   public void ch() {
      this.ex = true;
   }

   public final synchronized void ci() {
      if (!eG) {
         eG = true;

         try {
            this.fb.removeFocusListener(this);
         } catch (Exception var5) {
         }

         try {
            this.ay();
         } catch (Exception var4) {
         }

         if (this.fd != null) {
            try {
               System.exit(0);
            } catch (Throwable var3) {
            }
         }

         if (bo.aT != null) {
            try {
               bo.aT.a();
            } catch (Exception var2) {
            }
         }

         this.aq();
      }

   }

   public final void cj() {
      this.cu();
      if (eD == this && !eG) {
         eF = 0L;
      }

   }

   public final void ck() {
      if (eD == this && !eG) {
         eF = bd.a();
         jw.a(5000L);
         this.ci();
      }

   }

   public final void update(Graphics var1) {
      this.paint(var1);
   }

   public final synchronized void paint(Graphics var1) {
      this.a(var1);
      if (eD == this && !eG) {
         this.ew = true;
         if (bd.a() - this.ez > 1000L) {
            Rectangle var2 = var1.getClipBounds();
            if (var2 == null || var2.width >= eP && var2.height >= eQ) {
               this.ey = true;
            }
         }
      }

   }

   public final void focusGained(FocusEvent var1) {
      this.a(var1);
      eL = true;
      this.ew = true;
   }

   public final void focusLost(FocusEvent var1) {
      eL = false;
   }

   public final void windowActivated(WindowEvent var1) {
   }

   public final void windowClosed(WindowEvent var1) {
   }

   public final void windowClosing(WindowEvent var1) {
      this.ck();
   }

   public final void windowDeactivated(WindowEvent var1) {
   }

   public final void windowDeiconified(WindowEvent var1) {
   }

   public final void windowIconified(WindowEvent var1) {
   }

   public final void windowOpened(WindowEvent var1) {
   }

   public final void a(int var1, String var2, boolean var3, boolean var4) {
      try {
         Graphics var5 = this.fb.getGraphics();
         if (bo.aW == null) {
            bo.aW = new Font("Helvetica", 1, 13);
            bo.dT = this.fb.getFontMetrics(bo.aW);
         }

         if (var3 && !var4) {
            var5.setColor(Color.black);
            var5.fillRect(0, 0, eP, eQ);
         }

         Color var6 = new Color(140, 17, 17);

         try {
            if (bo.dN == null) {
               bo.dN = this.fb.createImage(304, 34);
            }

            Graphics var7 = bo.dN.getGraphics();
            var7.setColor(var6);
            var7.drawRect(0, 0, 303, 33);
            var7.fillRect(2, 2, var1 * 3, 30);
            var7.setColor(Color.black);
            var7.drawRect(1, 1, 301, 31);
            var7.fillRect(var1 * 3 + 2, 2, 300 - var1 * 3, 30);
            var7.setFont(bo.aW);
            var7.setColor(Color.white);
            var7.drawString(var2, (304 - bo.dT.stringWidth(var2)) / 2, 22);
            var5.drawImage(bo.dN, eP / 2 - 152, (var4 ? 50 : -18) + eQ / 2, (ImageObserver)null);
         } catch (Exception var10) {
            int var8 = eP / 2 - 152;
            int var9 = eQ / 2 - 18;
            var5.setColor(var6);
            var5.drawRect(var8, var9, 303, 33);
            var5.fillRect(var8 + 2, var9 + 2, var1 * 3, 30);
            var5.setColor(Color.black);
            var5.drawRect(var8 + 1, var9 + 1, 301, 31);
            var5.fillRect(var1 * 3 + var8 + 2, var9 + 2, 300 - var1 * 3, 30);
            var5.setFont(bo.aW);
            var5.setColor(Color.white);
            var5.drawString(var2, var8 + (304 - bo.dT.stringWidth(var2)) / 2, var9 + 22);
         }
      } catch (Exception var11) {
         this.fb.repaint();
      }

   }

   public final void cl() {
      bo.dN = null;
      bo.aW = null;
      bo.dT = null;
   }

   public void k(String var1) {
      if (!this.et) {
         this.et = true;
         System.out.println("error_game_" + var1);

         try {
            this.cr().a(new URL(this.cs(), "error_game_" + var1 + ".ws"), "_self");
         } catch (Exception var3) {
         }
      }

   }

   public Container cm() {
      return (Container)(this.fd != null ? this.fd : this);
   }

   public ba cn() {
      Container var1 = this.cm();
      int var2 = Math.max(var1.getWidth(), this.eX);
      int var3 = Math.max(var1.getHeight(), this.eU);
      if (this.fd != null) {
         Insets var4 = this.fd.getInsets();
         var2 -= var4.right + var4.left;
         var3 -= var4.top + var4.bottom;
      }

      return new ba(var2, var3);
   }

   public final void p(int var1, int var2) {
      if (Client.s.isStretchedEnabled() && Client.s.isResized()) {
         Dimension var3 = Client.s.getRealDimensions();
         var1 = var3.width;
         var2 = var3.height;
      }

      if (this.eY != var1 || this.eT != var2) {
         this.ch();
      }

      this.eY = var1;
      this.eT = var2;
   }

   public void run() {
      this.ff = Thread.currentThread();
      this.ff.setName("Client");
      this.ff.setUncaughtExceptionHandler((var0, var1x) -> {
         b((String)null, var1x);
      });

      try {
         this.setFocusCycleRoot(true);
         this.cA();
         this.cx();
         bo.ai = new dw();
         if (Boolean.getBoolean("runelite.delaystart")) {
            this.ep = new Semaphore(0);
            this.ep.acquire();
         }

         while(eF == 0L) {
            eS = bo.ai.c(20, 1);

            for(int var1 = 0; var1 < eS; ++var1) {
               this.cz();
            }

            if (bo.cf) {
               this.cy();
               this.a((Object)this.fb);
            }
         }
      } catch (Exception var2) {
         b((String)null, var2);
         this.m("crash");
      }

      this.cB();
   }

   public void co() {
      if (Client.s.isStretchedEnabled()) {
         Client.s.invalidateStretching(false);
         if (Client.s.isResized()) {
            Dimension var1 = Client.s.getRealDimensions();
            this.ax(var1.width);
            this.aw(var1.height);
         }
      }

   }

   public void initialize() {
      this.setSize(Constants.GAME_FIXED_SIZE);
      this.ar();
      this.cj();
   }

   public void a(URL var1) {
      String var2 = var1.getPath();
      if (var2.startsWith("/error_game_")) {
         this.fg.onError(var2.replace("/", "").replace(".ws", ""));
      }

   }

   public void cp() {
      if (Client.s.isStretchedEnabled()) {
         Canvas var1 = this.getCanvas();
         Dimension var2 = Client.s.getStretchedDimensions();
         var1.setSize(var2);
         var1.setLocation((var1.getParent().getWidth() - var2.width) / 2, 0);
         var1.validate();
      }

   }

   public URL cq() {
      return this.cs();
   }

   public Thread getClientThread() {
      return this.ff;
   }

   public void unblockStartup() {
      if (this.ep != null) {
         this.ep.release();
      }

   }

   public void a(boolean var1, byte var2) {
      if (this.es != var1) {
         this.es = var1;
         boolean var3 = var1 & !Client.s.isGpu();
         fa.b(var3);
         fa.b();
      }

   }

   public iK cr() {
      return this;
   }

   public URL cs() {
      return this.fg.getCodeBase();
   }

   public void ct() {
      eK = 0;
   }

   public final void a(Graphics var1) {
      if (!this.er) {
         this.er = true;
         var1.clearRect(0, 0, this.getWidth(), this.getHeight());
      }

   }

   public void cu() {
      this.addHierarchyListener((var0) -> {
         if ((var0.getChangeFlags() & 2L) != 0L && Client.s.isDisplayable()) {
            Client.H();
         }

      });
   }

   public void cv() {
      this.fb.addComponentListener(this.eq);
      this.fc = this.fb.getSize();
   }

   public String l(String var1) {
      return this.fg.getParameter(var1);
   }

   public boolean isClientThread() {
      return this.ff == Thread.currentThread();
   }

   public void a(URL var1, String var2) {
      this.a(var1);
   }

   public final void av(int var1) {
      this.g(false);
   }

   public void setConfiguration(ClientConfiguration var1) {
      if (this.fg != null) {
         throw new IllegalStateException();
      } else {
         this.fg = var1;
      }
   }

   public void a(FocusEvent var1) {
      FocusChanged var2 = new FocusChanged();
      var2.setFocused(true);
      Client.s.getCallbacks().post(var2);
   }

   public void cw() {
      if (this.fb != null) {
         this.fb.removeComponentListener(this.eq);
      }

   }

   public abstract void aG();

   public abstract void c(boolean var1);

   public abstract void aw();

   public abstract void ax();

   public abstract void ay();

   public void g(boolean var1) {
      this.ey = var1;
   }

   public void aw(int var1) {
      this.eT = var1;
   }

   public void cx() {
      this.aw();
   }

   public Canvas getCanvas() {
      return this.fb;
   }

   public void cy() {
      this.cf();
   }

   public void ax(int var1) {
      this.eY = var1;
   }

   public void m(String var1) {
      this.k(var1);
   }

   public void cz() {
      this.cd();
   }

   public abstract void ar();

   public void h(boolean var1) {
      this.ex = var1;
   }

   public static void b(String var0, Throwable var1) {
      gG.a(var0, var1, (short)9230);
   }

   public void cA() {
      this.cb();
   }

   public void cB() {
      this.ci();
   }

   public abstract void aq();

   public void a(Object var1) {
      this.a(var1, 1712758177);
   }

   public void resizeCanvas() {
      this.bZ();
   }
}
