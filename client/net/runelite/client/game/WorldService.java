package net.runelite.client.game;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Singleton;
import net.runelite.http.api.worlds.WorldResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
public class WorldService {
   private static final Logger log = LoggerFactory.getLogger(WorldService.class);

   @Inject
   private WorldService() {
      log.debug("World list service disabled");
   }

   public void refresh() {
      log.debug("Ignoring world list refresh because world list service is disabled");
   }

   @Nullable
   public WorldResult getWorlds() {
      return null;
   }
}
