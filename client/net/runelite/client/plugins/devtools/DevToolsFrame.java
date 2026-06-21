package net.runelite.client.plugins.devtools;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Arrays;
import javax.swing.JFrame;
import net.runelite.client.ui.ClientUI;

public class DevToolsFrame extends JFrame {
   protected DevToolsButton devToolsButton;

   public DevToolsFrame() {
      this.setIconImages(Arrays.asList(ClientUI.ICON_128, ClientUI.ICON_16));
      this.setDefaultCloseOperation(0);
      this.addWindowListener(new WindowAdapter() {
         public void windowClosing(WindowEvent e) {
            DevToolsFrame.this.close();
            DevToolsFrame.this.devToolsButton.setActive(false);
         }
      });
   }

   public void open() {
      this.setVisible(true);
      this.toFront();
      this.repaint();
   }

   public void close() {
      this.setVisible(false);
   }

   void setDevToolsButton(DevToolsButton devToolsButton) {
      this.devToolsButton = devToolsButton;
   }
}
