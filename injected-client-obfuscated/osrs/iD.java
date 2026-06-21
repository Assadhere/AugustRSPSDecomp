package osrs;

import java.awt.Component;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import net.runelite.api.events.FocusChanged;

public class iD implements FocusListener, KeyListener {
   public boolean[] a = new boolean[112];
   public ap[] b = new ap[3];
   public volatile int c = 0;
   public Collection d = new ArrayList(100);
   public Collection e = new ArrayList(100);

   public void a(ap var1, int var2) {
      this.b[var2] = var1;
   }

   public int a() {
      return this.c;
   }

   public boolean b() {
      return this.c <= 1;
   }

   public void a(Component var1) {
      var1.setFocusTraversalKeysEnabled(false);
      var1.addKeyListener(this);
      var1.addFocusListener(this);
   }

   public void c() {
      ++this.c;
      this.d();
      Iterator var1 = this.e.iterator();

      while(var1.hasNext()) {
         ai var2 = (ai)var1.next();

         for(int var3 = 0; var3 < this.b.length && !var2.a(this.b[var3]); ++var3) {
         }
      }

      this.e.clear();
   }

   public synchronized void d() {
      Collection var1 = this.e;
      this.e = this.d;
      this.d = var1;
   }

   public final synchronized void focusGained(FocusEvent var1) {
      this.d.add(new ai(4, 1));
   }

   public final synchronized void focusLost(FocusEvent var1) {
      this.a(var1);

      for(int var2 = 0; var2 < 112; ++var2) {
         if (this.a[var2]) {
            this.a[var2] = false;
            this.d.add(new ai(2, var2));
         }
      }

      this.d.add(new ai(4, 0));
   }

   public final synchronized void keyPressed(KeyEvent var1) {
      if (!a(var1)) {
         Client.s.getCallbacks().keyPressed(var1);
      }

      bo.dU = var1.isConsumed();
      int var2 = var1.getKeyCode();
      int var3;
      if (var2 >= 0 && var2 < eY.b()) {
         var3 = eY.a(var2);
         if (eY.b(var3)) {
            var3 = -1;
         }
      } else {
         var3 = -1;
      }

      if (var3 >= 0) {
         if (!this.a[var3]) {
            this.c = 0;
         }

         this.a[var3] = true;
         this.d.add(new ai(1, var3));
      }

      var1.consume();
      bo.dU = false;
   }

   public final synchronized void keyTyped(KeyEvent var1) {
      if (!a(var1)) {
         Client.s.getCallbacks().keyTyped(var1);
      }

      bo.dU = var1.isConsumed();
      char var2 = var1.getKeyChar();
      if (var2 != 0 && var2 != '\uffff' && gc.b(var2)) {
         this.d.add(new ai(3, var2));
      }

      var1.consume();
      bo.dU = false;
   }

   public static boolean a(KeyEvent var0) {
      return (Client.s.bL() == 10 || Client.s.bL() == 11) && Client.s.getCurrentLoginField() == 1 && !var0.isMetaDown() && !var0.isControlDown();
   }

   public void a(FocusEvent var1) {
      FocusChanged var2 = new FocusChanged();
      var2.setFocused(false);
      Client.s.getCallbacks().post(var2);
   }

   public final synchronized void keyReleased(KeyEvent var1) {
      if (!a(var1)) {
         Client.s.getCallbacks().keyReleased(var1);
      }

      bo.dU = var1.isConsumed();
      int var2 = var1.getKeyCode();
      int var3;
      if (var2 >= 0 && var2 < eY.b()) {
         var3 = eY.a(var2) & -129;
      } else {
         var3 = -1;
      }

      if (var3 >= 0) {
         this.a[var3] = false;
         this.d.add(new ai(2, var3));
      }

      var1.consume();
      bo.dU = false;
   }

   public int e() {
      return this.c;
   }
}
