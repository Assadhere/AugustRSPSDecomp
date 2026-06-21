package osrs;

import java.awt.Component;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

public final class bP implements MouseWheelListener, eg {
   public int a = 0;

   public void a(Component var1) {
      var1.addMouseWheelListener(this);
   }

   public synchronized int a() {
      int var1 = this.a;
      this.a = 0;
      return var1;
   }

   public synchronized void mouseWheelMoved(MouseWheelEvent var1) {
      MouseWheelEvent var2 = Client.s.getCallbacks().mouseWheelMoved(var1);
      if (!var2.isConsumed()) {
         this.a += var2.getWheelRotation();
      }

   }
}
