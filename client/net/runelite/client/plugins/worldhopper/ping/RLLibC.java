package net.runelite.client.plugins.worldhopper.ping;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.platform.unix.LibC;
import net.runelite.client.util.OSType;

interface RLLibC extends LibC {
   RLLibC INSTANCE = (RLLibC)Native.loadLibrary("c", RLLibC.class);
   int AF_INET = 2;
   int SOCK_DGRAM = 2;
   int SOL_SOCKET = OSType.getOSType() == OSType.MacOS ? '\uffff' : 1;
   int IPPROTO_ICMP = 1;
   int IPPROTO_TCP = 6;
   int SO_SNDTIMEO = OSType.getOSType() == OSType.MacOS ? 4101 : 21;
   int SO_RCVTIMEO = OSType.getOSType() == OSType.MacOS ? 4102 : 20;
   int TCP_INFO = OSType.getOSType() == OSType.MacOS ? 262 : 11;

   int socket(int var1, int var2, int var3);

   int sendto(int var1, byte[] var2, int var3, int var4, byte[] var5, int var6);

   int recvfrom(int var1, Pointer var2, int var3, int var4, Pointer var5, Pointer var6);

   int setsockopt(int var1, int var2, int var3, Pointer var4, int var5);

   int getsockopt(int var1, int var2, int var3, Pointer var4, Pointer var5);
}
