package net.runelite.client.plugins.worldhopper.ping;

public interface TCPInfo {
   long getRTT();

   long getTransmitted();

   long getRetransmitted();
}
