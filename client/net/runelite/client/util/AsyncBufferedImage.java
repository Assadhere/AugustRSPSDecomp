package net.runelite.client.util;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import net.runelite.client.callback.ClientThread;

public class AsyncBufferedImage extends BufferedImage {
   private final ClientThread clientThread;
   private final List<Runnable> listeners = new ArrayList();
   private boolean loaded;

   public AsyncBufferedImage(ClientThread clientThread, int width, int height, int imageType) {
      super(width, height, imageType);
      this.clientThread = clientThread;
   }

   public synchronized void loaded() {
      this.loaded = true;
      Iterator var1 = this.listeners.iterator();

      while(var1.hasNext()) {
         Runnable r = (Runnable)var1.next();
         r.run();
      }

      this.listeners.clear();
   }

   public synchronized void onLoaded(Runnable r) {
      if (this.loaded) {
         this.clientThread.invokeLater(r);
      } else {
         this.listeners.add(r);
      }
   }

   public void addTo(JButton c) {
      c.setIcon(this.makeIcon(c));
   }

   public void addTo(JLabel c) {
      c.setIcon(this.makeIcon(c));
   }

   private ImageIcon makeIcon(JComponent c) {
      synchronized(this) {
         if (!this.loaded) {
            List var10000 = this.listeners;
            Objects.requireNonNull(c);
            var10000.add(c::repaint);
         }
      }

      return new ImageIcon(this);
   }
}
