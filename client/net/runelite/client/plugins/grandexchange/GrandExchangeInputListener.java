package net.runelite.client.plugins.grandexchange;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.MenuEntry;
import net.runelite.client.input.KeyListener;
import net.runelite.client.input.MouseAdapter;
import net.runelite.client.util.Text;

public class GrandExchangeInputListener extends MouseAdapter implements KeyListener {
   private final Client client;
   private final GrandExchangePlugin plugin;

   @Inject
   private GrandExchangeInputListener(Client client, GrandExchangePlugin plugin) {
      this.client = client;
      this.plugin = plugin;
   }

   public MouseEvent mouseClicked(MouseEvent e) {
      if (e.getButton() == 1 && e.isAltDown()) {
         MenuEntry[] menuEntries = this.client.getMenuEntries();
         MenuEntry[] var3 = menuEntries;
         int var4 = menuEntries.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            MenuEntry menuEntry = var3[var5];
            if (menuEntry.getOption().equals("Search Grand Exchange")) {
               this.plugin.search(Text.removeTags(menuEntry.getTarget()));
               e.consume();
               break;
            }
         }
      }

      return super.mouseClicked(e);
   }

   public void keyTyped(KeyEvent e) {
   }

   public void keyPressed(KeyEvent e) {
      if (e.isAltDown()) {
         this.plugin.setHotKeyPressed(true);
      }

   }

   public void keyReleased(KeyEvent e) {
      if (!e.isAltDown()) {
         this.plugin.setHotKeyPressed(false);
      }

   }
}
