package net.runelite.client.plugins.worldhopper.ping;

import com.sun.jna.Structure;
import java.util.List;

public class MacOSTCPConnectionInfo extends Structure implements TCPInfo {
   public byte state;
   public byte snd_wscale;
   public byte rcv_wscale;
   public byte pad1;
   public int options;
   public int flags;
   public int rto;
   public int maxseg;
   public int snd_ssthresh;
   public int snd_cwnd;
   public int snd_wnd;
   public int snd_sbbytes;
   public int rcv_wnd;
   public int rttcur;
   public int srtt;
   public int rttvar;
   public int tfo;
   public long txpackets;
   public long txbytes;
   public long txretransmitbytes;
   public long rxpackets;
   public long rxbytes;
   public long rxoutoforderbytes;
   public long txretransmitpackets;

   protected List<String> getFieldOrder() {
      return List.of("state", "snd_wscale", "rcv_wscale", "pad1", "options", "flags", "rto", "maxseg", "snd_ssthresh", "snd_cwnd", "snd_wnd", "snd_sbbytes", "rcv_wnd", "rttcur", "srtt", "rttvar", "tfo", "txpackets", "txbytes", "txretransmitbytes", "rxpackets", "rxbytes", "rxoutoforderbytes", "txretransmitpackets");
   }

   public long getRTT() {
      return (long)this.srtt * 1000L;
   }

   public long getRetransmitted() {
      return this.txretransmitpackets;
   }

   public long getTransmitted() {
      return this.txpackets;
   }
}
