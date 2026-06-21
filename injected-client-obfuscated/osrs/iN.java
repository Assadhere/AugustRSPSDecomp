package osrs;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class iN extends ComponentAdapter {
   public final T a;

   public void componentResized(ComponentEvent var1) {
      this.a.fc = this.a.fb.getSize();
   }

   public iN(T var1) {
      this.a = var1;
   }
}
