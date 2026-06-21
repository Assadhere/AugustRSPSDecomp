package net.runelite.client.plugins.worldhopper.ping;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.ptr.IntByReference;

interface Ws2_32 extends Library {
   Ws2_32 INSTANCE = (Ws2_32)Native.loadLibrary("Ws2_32", Ws2_32.class);
   int SIO_TCP_INFO = -671088601;

   int WSAIoctl(WinNT.HANDLE var1, int var2, Pointer var3, int var4, Pointer var5, int var6, IntByReference var7, Pointer var8, Pointer var9);
}
