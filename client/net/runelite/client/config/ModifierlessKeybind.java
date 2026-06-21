package net.runelite.client.config;

import java.awt.event.KeyEvent;

public class ModifierlessKeybind extends Keybind {
   public ModifierlessKeybind(int keyCode, int modifiers) {
      super(keyCode, modifiers, true);
   }

   public ModifierlessKeybind(KeyEvent e) {
      this(e.getExtendedKeyCode(), e.getModifiersEx());

      assert this.matches(e);

   }

   public boolean matches(KeyEvent e) {
      return this.matches(e, true);
   }
}
