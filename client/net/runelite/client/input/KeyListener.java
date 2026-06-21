package net.runelite.client.input;

public interface KeyListener extends java.awt.event.KeyListener {
   default boolean isEnabledOnLoginScreen() {
      return false;
   }

   default void focusLost() {
   }
}
