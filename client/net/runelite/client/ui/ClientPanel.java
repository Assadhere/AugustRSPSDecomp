package net.runelite.client.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import javax.annotation.Nullable;
import javax.swing.JPanel;
import net.runelite.api.Constants;

final class ClientPanel extends JPanel {
   public ClientPanel(@Nullable Component client) {
      this.setSize(Constants.GAME_FIXED_SIZE);
      this.setMinimumSize(Constants.GAME_FIXED_SIZE);
      this.setPreferredSize(Constants.GAME_FIXED_SIZE);
      this.setLayout(new BorderLayout());
      this.setBackground(Color.black);
      if (client != null) {
         this.add(client, "Center");
      }
   }
}
