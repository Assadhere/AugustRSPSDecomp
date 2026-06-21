package net.runelite.client.plugins.freezeframe;

import net.runelite.client.events.ConfigChanged;
import net.runelite.client.events.PluginChanged;

public class NoOpFreezeFrameBridge implements FreezeFrameBridge {
   public void initialize(FreezeFrameBridge.Deps deps) {
   }

   public void cleanup() {
   }

   public void onBeforeRender() {
   }

   public void onClientTick() {
   }

   public void onConfigChanged(ConfigChanged event) {
   }

   public void onPluginChanged(PluginChanged event) {
   }

   public void toggleFreeze() {
   }

   public void capture() {
   }
}
