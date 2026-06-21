package net.runelite.client.util;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinUser;
import com.sun.jna.win32.StdCallLibrary;
import com.sun.jna.win32.W32APIOptions;
import java.awt.Frame;
import java.awt.Window;

public class WinUtil {
   private static boolean isWindowArrangedSupported;

   public static void requestForeground(Frame frame) {
      frame.setState(0);
      User32 user32 = User32.INSTANCE;
      WinUser.INPUT input = new WinUser.INPUT();
      input.type = new WinDef.DWORD(1L);
      input.input.ki.wVk = new WinDef.WORD(133L);
      user32.SendInput(new WinDef.DWORD(1L), (WinUser.INPUT[])input.toArray(1), input.size());
      WinDef.HWND hwnd = new WinDef.HWND(Native.getComponentPointer(frame));
      user32.SetForegroundWindow(hwnd);
   }

   public static boolean isWindowArranged(Window window) {
      if (isWindowArrangedSupported && window.isDisplayable()) {
         try {
            Pointer hwnd = Native.getComponentPointer(window);
            return WinUtil.RLUser32.INSTANCE.IsWindowArranged(hwnd);
         } catch (LinkageError var2) {
            isWindowArrangedSupported = false;
            return false;
         }
      } else {
         return false;
      }
   }

   static {
      isWindowArrangedSupported = OSType.getOSType() == OSType.Windows;
   }

   interface RLUser32 extends StdCallLibrary {
      RLUser32 INSTANCE = (RLUser32)Native.load("user32", RLUser32.class, W32APIOptions.DEFAULT_OPTIONS);

      boolean IsWindowArranged(Pointer var1);
   }
}
