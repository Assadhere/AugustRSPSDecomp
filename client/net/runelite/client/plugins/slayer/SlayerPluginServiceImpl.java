package net.runelite.client.plugins.slayer;

import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;
import net.runelite.api.NPC;

@Singleton
class SlayerPluginServiceImpl implements SlayerPluginService {
   private final SlayerPlugin plugin;

   @Inject
   private SlayerPluginServiceImpl(SlayerPlugin plugin) {
      this.plugin = plugin;
   }

   public List<NPC> getTargets() {
      return this.plugin.getTargets();
   }

   public String getTask() {
      return this.plugin.getTaskName();
   }

   public String getTaskLocation() {
      return this.plugin.getTaskLocation();
   }

   public int getInitialAmount() {
      return this.plugin.getInitialAmount();
   }

   public int getRemainingAmount() {
      return this.plugin.getAmount();
   }
}
