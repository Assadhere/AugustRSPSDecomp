package net.runelite.client.plugins.defaultworld;

import com.google.inject.Provides;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.events.WorldChanged;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@PluginDescriptor(
   name = "Default World",
   description = "Enable a default world to be selected when launching the client",
   tags = {"home"},
   enabledByDefault = false,
   hidden = true
)
public class DefaultWorldPlugin extends Plugin {
   private static final Logger log = LoggerFactory.getLogger(DefaultWorldPlugin.class);
   @Inject
   private Client client;
   @Inject
   private DefaultWorldConfig config;

   protected void startUp() {
      log.debug("Default world plugin disabled because world list lookup is disabled");
   }

   @Provides
   DefaultWorldConfig getConfig(ConfigManager configManager) {
      return (DefaultWorldConfig)configManager.getConfig(DefaultWorldConfig.class);
   }

   @Subscribe
   public void onWorldChanged(WorldChanged worldChanged) {
      int world = this.client.getWorld();
      this.config.lastWorld(world);
      log.debug("Saving last world {}", world);
   }
}
