package net.runelite.client.plugins.worldhopper.ping;

import com.sun.jna.Structure;
import java.util.List;

public class LinuxTCPInfo extends Structure implements TCPInfo {
   public byte state;
   public byte ca_state;
   public byte retransmits;
   public byte probes;
   public byte backoff;
   public byte options;
   public byte wscale;
   public byte flags;
   public int rto;
   public int ato;
   public int snd_mss;
   public int rcv_mss;
   public int unacked;
   public int sacked;
   public int lost;
   public int retrans;
   public int fackets;
   public int last_data_sent;
   public int last_ack_sent;
   public int last_data_recv;
   public int last_ack_recv;
   public int pmtu;
   public int rcv_ssthresh;
   public int rtt;
   public int rttvar;
   public int snd_ssthresh;
   public int snd_cwnd;
   public int advmss;
   public int reordering;
   public int rcv_rtt;
   public int rcv_space;
   public int total_retrans;
   public long pacing_rate;
   public long max_pacing_rate;
   public long bytes_acked;
   public long bytes_received;
   public int segs_out;
   public int segs_in;
   public int notsent_bytes;
   public int min_rtt;
   public int data_segs_in;
   public int data_segs_out;
   public long delivery_rate;
   public long busy_time;
   public long rwnd_limited;
   public long sndbuf_limited;
   public int delivered;
   public int delivered_ce;
   public long bytes_sent;
   public long bytes_retrans;
   public int dsack_dups;
   public int reord_seen;
   public int rcv_ooopack;
   public int snd_wnd;

   protected List<String> getFieldOrder() {
      return List.of("state", "ca_state", "retransmits", "probes", "backoff", "options", "wscale", "flags", "rto", "ato", "snd_mss", "rcv_mss", "unacked", "sacked", "lost", "retrans", "fackets", "last_data_sent", "last_ack_sent", "last_data_recv", "last_ack_recv", "pmtu", "rcv_ssthresh", "rtt", "rttvar", "snd_ssthresh", "snd_cwnd", "advmss", "reordering", "rcv_rtt", "rcv_space", "total_retrans", "pacing_rate", "max_pacing_rate", "bytes_acked", "bytes_received", "segs_out", "segs_in", "notsent_bytes", "min_rtt", "data_segs_in", "data_segs_out", "delivery_rate", "busy_time", "rwnd_limited", "sndbuf_limited", "delivered", "delivered_ce", "bytes_sent", "bytes_retrans", "dsack_dups", "reord_seen", "rcv_ooopack", "snd_wnd");
   }

   public long getRTT() {
      return (long)this.rtt & 4294967295L;
   }

   public long getRetransmitted() {
      return (long)this.total_retrans & 4294967295L;
   }

   public long getTransmitted() {
      return (long)this.segs_out & 4294967295L;
   }
}
