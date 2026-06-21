package net.runelite.client.plugins.worldhopper.ping;

import com.sun.jna.Structure;
import com.sun.jna.platform.win32.WinDef;
import java.util.List;

public class TCP_INFO_v0 extends Structure implements TCPInfo {
   public WinDef.ULONG State;
   public WinDef.ULONG Mss;
   public WinDef.ULONGLONG ConnectionTimeMs;
   public WinDef.BOOL TimestampsEnabled;
   public WinDef.ULONG RttUs;
   public WinDef.ULONG MinRttUs;
   public WinDef.ULONG BytesInFlight;
   public WinDef.ULONG Cwnd;
   public WinDef.ULONG SndWnd;
   public WinDef.ULONG RcvWnd;
   public WinDef.ULONG RcvBuf;
   public WinDef.ULONGLONG BytesOut;
   public WinDef.ULONGLONG BytesIn;
   public WinDef.ULONG BytesReordered;
   public WinDef.ULONG BytesRetrans;
   public WinDef.ULONG FastRetrans;
   public WinDef.ULONG DupAcksIn;
   public WinDef.ULONG TimeoutEpisodes;
   public WinDef.UCHAR SynRetrans;

   protected List<String> getFieldOrder() {
      return List.of("State", "Mss", "ConnectionTimeMs", "TimestampsEnabled", "RttUs", "MinRttUs", "BytesInFlight", "Cwnd", "SndWnd", "RcvWnd", "RcvBuf", "BytesOut", "BytesIn", "BytesReordered", "BytesRetrans", "FastRetrans", "DupAcksIn", "TimeoutEpisodes", "SynRetrans");
   }

   public long getRTT() {
      return this.RttUs.longValue();
   }

   public long getRetransmitted() {
      return this.BytesRetrans.longValue();
   }

   public long getTransmitted() {
      return this.BytesOut.longValue();
   }
}
