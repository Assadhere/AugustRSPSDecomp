package net.runelite.client.config;

import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import javax.annotation.Nullable;

public class Keybind {
   private static final BiMap<Integer, Integer> MODIFIER_TO_KEY_CODE = (new ImmutableBiMap.Builder()).put(128, 17).put(512, 18).put(64, 16).put(256, 157).build();
   private static final int KEYBOARD_MODIFIER_MASK;
   public static final Keybind NOT_SET;
   public static final Keybind CTRL;
   public static final Keybind ALT;
   public static final Keybind SHIFT;
   private final int keyCode;
   private final int modifiers;

   protected Keybind(int keyCode, int modifiers, boolean ignoreModifiers) {
      modifiers &= KEYBOARD_MODIFIER_MASK;
      Integer mf = getModifierForKeyCode(keyCode);
      if (mf != null) {
         assert (modifiers & mf) != 0;

         keyCode = 0;
      }

      if (ignoreModifiers && keyCode != 0) {
         modifiers = 0;
      }

      this.keyCode = keyCode;
      this.modifiers = modifiers;
   }

   public Keybind(int keyCode, int modifiers) {
      this(keyCode, modifiers, false);
   }

   public Keybind(KeyEvent e) {
      this(e.getExtendedKeyCode(), e.getModifiersEx());

      assert this.matches(e);

   }

   public boolean matches(KeyEvent e) {
      return this.matches(e, false);
   }

   protected boolean matches(KeyEvent e, boolean ignoreModifiers) {
      if (NOT_SET.equals(this)) {
         return false;
      } else {
         int keyCode = e.getExtendedKeyCode();
         int modifiers = e.getModifiersEx() & KEYBOARD_MODIFIER_MASK;
         Integer mf = getModifierForKeyCode(keyCode);
         if (mf != null) {
            modifiers |= mf;
            keyCode = 0;
         }

         if (e.getID() == 402) {
            if (keyCode != 0) {
               return this.keyCode == keyCode;
            }

            if (mf != null) {
               return this.keyCode == keyCode && (this.modifiers & modifiers) == this.modifiers && (mf & this.modifiers) == mf;
            }
         }

         if (ignoreModifiers && keyCode != 0) {
            return this.keyCode == keyCode;
         } else {
            return this.keyCode == keyCode && this.modifiers == modifiers;
         }
      }
   }

   public String toString() {
      if (this.keyCode == 0 && this.modifiers == 0) {
         return "Not set";
      } else {
         String key;
         if (this.keyCode == 0) {
            key = "";
         } else {
            key = KeyEvent.getKeyText(this.keyCode);
         }

         String mod = "";
         if (this.modifiers != 0) {
            mod = InputEvent.getModifiersExText(this.modifiers);
         }

         if (mod.isEmpty() && key.isEmpty()) {
            return "Not set";
         } else if (!mod.isEmpty() && !key.isEmpty()) {
            return mod + "+" + key;
         } else {
            return mod.isEmpty() ? key : mod;
         }
      }
   }

   @Nullable
   public static Integer getModifierForKeyCode(int keyCode) {
      return (Integer)MODIFIER_TO_KEY_CODE.inverse().get(keyCode);
   }

   public int getKeyCode() {
      return this.keyCode;
   }

   public int getModifiers() {
      return this.modifiers;
   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof Keybind)) {
         return false;
      } else {
         Keybind other = (Keybind)o;
         if (!other.canEqual(this)) {
            return false;
         } else if (this.getKeyCode() != other.getKeyCode()) {
            return false;
         } else {
            return this.getModifiers() == other.getModifiers();
         }
      }
   }

   protected boolean canEqual(Object other) {
      return other instanceof Keybind;
   }

   public int hashCode() {
      int PRIME = true;
      int result = 1;
      result = result * 59 + this.getKeyCode();
      result = result * 59 + this.getModifiers();
      return result;
   }

   static {
      KEYBOARD_MODIFIER_MASK = (Integer)MODIFIER_TO_KEY_CODE.keySet().stream().reduce((a, b) -> {
         return a | b;
      }).get();
      NOT_SET = new Keybind(0, 0);
      CTRL = new Keybind(0, 128);
      ALT = new Keybind(0, 512);
      SHIFT = new Keybind(0, 64);
   }
}
