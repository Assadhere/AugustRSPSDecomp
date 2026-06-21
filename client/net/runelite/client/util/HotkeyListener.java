package net.runelite.client.util;

import java.awt.event.KeyEvent;
import java.util.function.Supplier;
import net.runelite.client.config.Keybind;
import net.runelite.client.input.KeyListener;

public abstract class HotkeyListener implements KeyListener {
   private final Supplier<Keybind> keybind;
   private boolean isPressed = false;
   private boolean isConsumingTyped = false;
   private boolean enabledOnLoginScreen;

   public boolean isEnabledOnLoginScreen() {
      return this.enabledOnLoginScreen;
   }

   public void focusLost() {
      if (this.isPressed) {
         this.isPressed = false;
         this.isConsumingTyped = false;
         this.hotkeyReleased();
      }
   }

   public void keyTyped(KeyEvent e) {
      if (this.isConsumingTyped) {
         e.consume();
      }

   }

   public void keyPressed(KeyEvent e) {
      if (((Keybind)this.keybind.get()).matches(e)) {
         boolean wasPressed = this.isPressed;
         this.isPressed = true;
         if (!wasPressed) {
            this.hotkeyPressed();
         }

         if (Keybind.getModifierForKeyCode(e.getKeyCode()) == null) {
            this.isConsumingTyped = true;
            e.consume();
         }
      }

   }

   public void keyReleased(KeyEvent e) {
      if (((Keybind)this.keybind.get()).matches(e) && this.isPressed) {
         this.isPressed = false;
         this.isConsumingTyped = false;
         this.hotkeyReleased();
      }

   }

   public void hotkeyPressed() {
   }

   public void hotkeyReleased() {
   }

   public HotkeyListener(Supplier<Keybind> keybind) {
      this.keybind = keybind;
   }

   public void setEnabledOnLoginScreen(boolean enabledOnLoginScreen) {
      this.enabledOnLoginScreen = enabledOnLoginScreen;
   }
}
