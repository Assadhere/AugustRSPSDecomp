package net.runelite.client.util;

import com.google.common.base.CharMatcher;

class JagexPrintableCharMatcher extends CharMatcher {
   public boolean matches(char c) {
      return c >= ' ' && c <= '~' || c == 128 || c >= 160 && c <= 255;
   }
}
