package net.runelite.client.plugins.worldhopper.ping;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RetransmitCalculator {
   private static final Logger log = LoggerFactory.getLogger(RetransmitCalculator.class);
   private static final int SAMPLES = 16;
   private int index;
   private final long[] bytesOut = new long[16];
   private final long[] bytesRetrans = new long[16];
   private int loss;

   public void record(TCPInfo info) {
      int nextIndex = this.index++ & 15;
      long out = info.getTransmitted();
      long rt = info.getRetransmitted();
      log.trace("rtt: {}us out: {} retrans: {}", new Object[]{info.getRTT(), out, rt});
      this.bytesOut[nextIndex] = out;
      this.bytesRetrans[nextIndex] = rt;
      this.loss = this.computeRetransmitPercent();
   }

   private int computeRetransmitPercent() {
      int startIndex = this.index - 1 & 15;
      int endIndex = this.index & 15;
      long deltaOut = this.bytesOut[startIndex] - this.bytesOut[endIndex];
      long deltaRt = this.bytesRetrans[startIndex] - this.bytesRetrans[endIndex];
      return deltaOut == 0L ? 0 : (int)(deltaRt * 100L / deltaOut);
   }

   public int getRetransmitPercent() {
      return this.loss;
   }
}
