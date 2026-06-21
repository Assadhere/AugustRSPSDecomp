package net.runelite.client.plugins.config;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JButton;
import net.runelite.client.config.Keybind;
import net.runelite.client.config.ModifierlessKeybind;
import net.runelite.client.ui.FontManager;

class HotkeyButton extends JButton {
   private Keybind value;

   public HotkeyButton(Keybind value, final boolean modifierless) {
      this.setFocusTraversalKeysEnabled(false);
      this.setFont(FontManager.getDefaultFont().deriveFont(12.0F));
      this.setValue(value);
      this.addMouseListener(new MouseAdapter() {
         public void mouseReleased(MouseEvent e) {
            if (e.getButton() == 1) {
               HotkeyButton.this.setValue(Keybind.NOT_SET);
            }

         }
      });
      this.addKeyListener(new KeyAdapter() {
         public void keyPressed(KeyEvent e) {
            if (modifierless) {
               HotkeyButton.this.setValue(new ModifierlessKeybind(e));
            } else {
               HotkeyButton.this.setValue(new Keybind(e));
            }

         }
      });
   }

   public void setValue(Keybind value) {
      if (value == null) {
         value = Keybind.NOT_SET;
      }

      this.value = value;
      this.setText(value.toString());
   }

   public Keybind getValue() {
      return this.value;
   }
}
