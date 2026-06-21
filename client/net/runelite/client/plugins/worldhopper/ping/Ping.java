package net.runelite.client.plugins.worldhopper.ping;

import com.google.common.base.Charsets;
import com.google.common.primitives.Bytes;
import com.sun.jna.Memory;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.ptr.IntByReference;
import java.io.FileDescriptor;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import javax.annotation.Nullable;
import net.runelite.client.util.OSType;
import net.runelite.http.api.worlds.World;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Ping {
   private static final Logger log = LoggerFactory.getLogger(Ping.class);
   private static final byte[] RUNELITE_PING;
   private static final int TIMEOUT = 2000;
   private static final int PORT = 43594;
   private static final int MAX_IPV4_HEADER_SIZE = 60;
   private static short seq;

   /** @deprecated */
   @Deprecated
   public static int ping(World world) {
      return ping(world, false);
   }

   public static int ping(World world, boolean useTcpPing) {
      InetAddress inetAddress;
      try {
         inetAddress = InetAddress.getByName(world.getAddress());
      } catch (UnknownHostException var7) {
         UnknownHostException ex = var7;
         log.debug("error resolving host for world ping", ex);
         return -1;
      }

      if (!(inetAddress instanceof Inet4Address)) {
         log.debug("Only ipv4 ping is supported");
         return -1;
      } else {
         try {
            int p;
            switch (OSType.getOSType()) {
               case Windows:
                  p = windowsPing(inetAddress);
                  if (p == -1 && useTcpPing) {
                     p = tcpPing(inetAddress);
                  }

                  return p;
               case MacOS:
               case Linux:
                  p = -1;

                  try {
                     p = icmpPing(inetAddress, OSType.getOSType() == OSType.MacOS);
                  } catch (IOException var5) {
                     IOException ex = var5;
                     log.debug("error during icmp ping", ex);
                  }

                  if (p == -1 && useTcpPing) {
                     return tcpPing(inetAddress);
                  }

                  return p;
               default:
                  return tcpPing(inetAddress);
            }
         } catch (IOException var6) {
            IOException ex = var6;
            log.warn("error pinging", ex);
            return -1;
         }
      }
   }

   private static int windowsPing(InetAddress inetAddress) {
      IPHlpAPI ipHlpAPI = IPHlpAPI.INSTANCE;
      Pointer ptr = ipHlpAPI.IcmpCreateFile();

      int var8;
      try {
         byte[] address = inetAddress.getAddress();
         Memory data = new Memory((long)RUNELITE_PING.length);
         data.write(0L, RUNELITE_PING, 0, RUNELITE_PING.length);
         IcmpEchoReply icmpEchoReply = new IcmpEchoReply(new Memory((long)IcmpEchoReply.SIZE + data.size()));

         assert icmpEchoReply.size() == IcmpEchoReply.SIZE;

         int packed = address[0] & 255 | (address[1] & 255) << 8 | (address[2] & 255) << 16 | (address[3] & 255) << 24;
         int ret = ipHlpAPI.IcmpSendEcho(ptr, packed, data, (short)((int)data.size()), Pointer.NULL, icmpEchoReply, IcmpEchoReply.SIZE + (int)data.size(), 2000);
         if (ret != 1) {
            byte var12 = -1;
            return var12;
         }

         var8 = Math.toIntExact(icmpEchoReply.roundTripTime.longValue());
      } finally {
         ipHlpAPI.IcmpCloseHandle(ptr);
      }

      return var8;
   }

   private static int icmpPing(InetAddress inetAddress, boolean includeIpHeader) throws IOException {
      RLLibC libc = RLLibC.INSTANCE;
      byte[] address = inetAddress.getAddress();
      int sock = libc.socket(2, 2, 1);
      if (sock < 0) {
         throw new IOException("failed to open ICMP socket");
      } else {
         try {
            Timeval tv = new Timeval();
            tv.tv_sec = 2L;
            tv.write();
            if (libc.setsockopt(sock, RLLibC.SOL_SOCKET, RLLibC.SO_RCVTIMEO, tv.getPointer(), tv.size()) < 0) {
               throw new IOException("failed to set SO_RCVTIMEO");
            } else if (libc.setsockopt(sock, RLLibC.SOL_SOCKET, RLLibC.SO_SNDTIMEO, tv.getPointer(), tv.size()) < 0) {
               throw new IOException("failed to set SO_SNDTIMEO");
            } else {
               short var10000 = Ping.seq;
               Ping.seq = (short)(var10000 + 1);
               short seqno = var10000;
               byte[] request = new byte[]{8, 0, 0, 0, 0, 0, (byte)(seqno >> 8 & 255), (byte)(seqno & 255)};
               request = Bytes.concat(new byte[][]{request, RUNELITE_PING});
               short checksum = checksum(request);
               request[2] = (byte)(checksum >> 8 & 255);
               request[3] = (byte)(checksum & 255);
               byte[] addr = new byte[]{2, 0, 0, 0, address[0], address[1], address[2], address[3], 0, 0, 0, 0, 0, 0, 0, 0};
               int size = 8 + RUNELITE_PING.length + (includeIpHeader ? 60 : 0);
               Memory response = new Memory((long)size);
               long start = System.nanoTime();
               byte var23;
               if (libc.sendto(sock, request, request.length, 0, addr, addr.length) != request.length) {
                  var23 = -1;
                  return var23;
               } else {
                  while(true) {
                     if ((System.nanoTime() - start) / 1000000L > 2000L) {
                        log.debug("timeout elapsed checking for echo reply");
                        break;
                     }

                     int rlen = libc.recvfrom(sock, response, size, 0, (Pointer)null, (Pointer)null);
                     long end = System.nanoTime();
                     if (rlen <= 0) {
                        log.debug("recvfrom() error: len {} errno {}", rlen, Native.getLastError());
                        break;
                     }

                     int icmpHeaderOffset = 0;
                     if (includeIpHeader) {
                        int ihl = response.getByte(0L) & 15;
                        icmpHeaderOffset = ihl << 2;
                     }

                     if (icmpHeaderOffset + 7 >= rlen) {
                        log.warn("packet too short (received {} bytes but icmp header offset is {})", rlen, icmpHeaderOffset);
                     } else if (response.getByte((long)icmpHeaderOffset) != 0) {
                        log.debug("non-echo reply");
                     } else {
                        short seq = (short)((response.getByte((long)(icmpHeaderOffset + 6)) & 255) << 8 | response.getByte((long)(icmpHeaderOffset + 7)) & 255);
                        if (seqno == seq) {
                           int var19 = (int)((end - start) / 1000000L);
                           return var19;
                        }

                        log.debug("sequence number mismatch ({} != {})", seqno, seq);
                     }
                  }

                  var23 = -1;
                  return var23;
               }
            }
         } finally {
            libc.close(sock);
         }
      }
   }

   private static short checksum(byte[] data) {
      int a = 0;

      for(int i = 0; i < data.length - 1; i += 2) {
         a += (data[i] & 255) << 8 | data[i + 1] & 255;
      }

      if ((data.length & 1) != 0) {
         a += (data[data.length - 1] & 255) << 8;
      }

      a = (a >> 16 & '\uffff') + (a & '\uffff');
      return (short)(~a & '\uffff');
   }

   private static int tcpPing(InetAddress inetAddress) throws IOException {
      Socket socket = new Socket();

      int var6;
      try {
         socket.setSoTimeout(2000);
         long start = System.nanoTime();
         socket.connect(new InetSocketAddress(inetAddress, 43594));
         long end = System.nanoTime();
         var6 = (int)((end - start) / 1000000L);
      } catch (Throwable var8) {
         try {
            socket.close();
         } catch (Throwable var7) {
            var8.addSuppressed(var7);
         }

         throw var8;
      }

      socket.close();
      return var6;
   }

   @Nullable
   public static TCPInfo getTCPInfo(FileDescriptor fd) {
      switch (OSType.getOSType()) {
         case Windows:
            return getTCPInfoWindows(fd);
         case MacOS:
         case Linux:
            return getTCPInfoNix(fd);
         default:
            return null;
      }
   }

   private static TCP_INFO_v0 getTCPInfoWindows(FileDescriptor fd) {
      int handle;
      try {
         Field f = FileDescriptor.class.getDeclaredField("fd");
         f.setAccessible(true);
         handle = f.getInt(fd);
      } catch (IllegalAccessException | NoSuchFieldException var9) {
         ReflectiveOperationException ex = var9;
         log.debug((String)null, ex);
         return null;
      }

      IntByReference tcpInfoVersion = new IntByReference(0);
      TCP_INFO_v0 info = new TCP_INFO_v0();
      IntByReference bytesReturned = new IntByReference();
      Ws2_32 winsock = Ws2_32.INSTANCE;

      int rc;
      try {
         rc = winsock.WSAIoctl(new WinNT.HANDLE(Pointer.createConstant(handle)), -671088601, tcpInfoVersion.getPointer(), 4, info.getPointer(), info.size(), bytesReturned, Pointer.NULL, Pointer.NULL);
      } catch (UnsatisfiedLinkError var8) {
         UnsatisfiedLinkError ex = var8;
         log.debug("WSAIoctl()", ex);
         return null;
      }

      if (rc != 0) {
         log.debug("WSAIoctl(SIO_TCP_INFO) error");
         return null;
      } else {
         info.read();
         return info;
      }
   }

   private static TCPInfo getTCPInfoNix(FileDescriptor fdObj) {
      int fd;
      try {
         Field f = FileDescriptor.class.getDeclaredField("fd");
         f.setAccessible(true);
         fd = f.getInt(fdObj);
      } catch (IllegalAccessException | NoSuchFieldException var5) {
         ReflectiveOperationException ex = var5;
         log.debug((String)null, ex);
         return null;
      }

      Structure out = OSType.getOSType() == OSType.MacOS ? new MacOSTCPConnectionInfo() : new LinuxTCPInfo();
      IntByReference size = new IntByReference(((Structure)out).size());
      int err = RLLibC.INSTANCE.getsockopt(fd, 6, RLLibC.TCP_INFO, ((Structure)out).getPointer(), size.getPointer());
      ((Structure)out).read();
      if (err != 0) {
         log.debug("getsockopt(TCP_INFO): {}", Native.getLastError());
         return null;
      } else {
         return (TCPInfo)out;
      }
   }

   static {
      RUNELITE_PING = "RuneLitePing".getBytes(Charsets.UTF_8);
   }
}
