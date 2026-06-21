package net.runelite.client.plugins.xptracker;

import com.google.gson.annotations.SerializedName;

class XpSaveSingle {
   @SerializedName("s")
   long startXp;
   @SerializedName("br")
   int xpGainedBeforeReset;
   @SerializedName("ar")
   int xpGainedSinceReset;
   @SerializedName("t")
   long time;
}
