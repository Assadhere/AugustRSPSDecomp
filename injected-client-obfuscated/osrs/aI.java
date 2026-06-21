package osrs;

import java.awt.Component;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class aI implements FocusListener, MouseListener, MouseMotionListener {
   public static volatile int a = -1;
   public static volatile long b = -1L;
   public static volatile int c = 0;
   public static volatile int d = 0;
   public static volatile int e = 0;
   public static volatile long f = 0L;
   public static int g = 0;
   public static int h = 0;
   public static int i = 0;
   public static long j = 0L;
   public static int k = 0;
   public static int l = 0;
   public static int m = 0;
   public static long n = 0L;
   public static aI o = new aI();
   public static volatile int p = 0;
   public static volatile int q = 0;
   public static volatile int r = -1;
   public int s;

   public static void a(Component var0) {
      var0.addMouseListener(o);
      var0.addMouseMotionListener(o);
      var0.addFocusListener(o);
   }

   public final int a(MouseEvent var1) {
      int var2 = var1.getButton();
      if (!var1.isAltDown() && var2 != 2) {
         return !var1.isMetaDown() && var2 != 3 ? 1 : 2;
      } else {
         return 4;
      }
   }

   public final void focusGained(FocusEvent var1) {
   }

   public final synchronized void focusLost(FocusEvent var1) {
      if (o != null) {
         q = 0;
      }

   }

   public final synchronized void mouseReleased(MouseEvent var1) {
      if (this.s == 0) {
         var1 = Client.s.getCallbacks().mouseReleased(var1);
      }

      if (!var1.isConsumed()) {
         ++this.s;

         try {
            if (o != null) {
               p = 0;
               Client.A(-1);
               q = 0;
            }

            if (var1.isPopupTrigger()) {
               var1.consume();
            }
         } finally {
            --this.s;
         }
      }

   }

   public final synchronized void mouseEntered(MouseEvent var1) {
      if (this.s == 0) {
         var1 = Client.s.getCallbacks().mouseEntered(var1);
      }

      if (!var1.isConsumed()) {
         ++this.s;

         try {
            this.mouseMoved(var1);
         } finally {
            --this.s;
         }
      }

   }

   public final synchronized void mouseMoved(MouseEvent var1) {
      if (this.s == 0) {
         var1 = Client.s.getCallbacks().mouseMoved(var1);
      }

      if (!var1.isConsumed()) {
         ++this.s;

         try {
            if (o != null) {
               p = 0;
               Client.A(-1);
               r = var1.getX();
               a = var1.getY();
               b = var1.getWhen();
            }
         } finally {
            --this.s;
         }
      }

   }

   public final synchronized void mouseDragged(MouseEvent var1) {
      if (this.s == 0) {
         var1 = Client.s.getCallbacks().mouseDragged(var1);
      }

      if (!var1.isConsumed()) {
         ++this.s;

         try {
            this.mouseMoved(var1);
         } finally {
            --this.s;
         }
      }

   }

   public final synchronized void mouseExited(MouseEvent var1) {
      if (this.s == 0) {
         var1 = Client.s.getCallbacks().mouseExited(var1);
      }

      if (!var1.isConsumed()) {
         ++this.s;

         try {
            if (o != null) {
               p = 0;
               Client.A(-1);
               r = -1;
               a = -1;
               b = var1.getWhen();
            }
         } finally {
            --this.s;
         }
      }

   }

   public final synchronized void mousePressed(MouseEvent var1) {
      if (this.s == 0) {
         var1 = Client.s.getCallbacks().mousePressed(var1);
      }

      if (!var1.isConsumed()) {
         ++this.s;

         try {
            if (o != null) {
               p = 0;
               Client.A(-1);
               d = var1.getX();
               e = var1.getY();
               f = bd.a();
               c = this.a(var1);
               if (c != 0) {
                  q = c;
               }
            }

            if (var1.isPopupTrigger()) {
               var1.consume();
            }
         } finally {
            --this.s;
         }
      }

   }

   public final void mouseClicked(MouseEvent var1) {
      MouseEvent var2 = Client.s.getCallbacks().mouseClicked(var1);
      if (!var2.isConsumed() && var2.isPopupTrigger()) {
         var2.consume();
      }

   }
}
