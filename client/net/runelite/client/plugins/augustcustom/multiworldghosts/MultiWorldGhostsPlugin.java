package net.runelite.client.plugins.augustcustom.multiworldghosts;

import custom.GhostNpcsScript;
import custom.GhostRender;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import net.runelite.api.events.GameStateChanged;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@PluginDescriptor(
   name = "Multi-World Ghosts",
   description = "Renders cross-world player ghosts as translucent spectres.",
   tags = {"ghost", "multiworld", "august"},
   enabledByDefault = true,
   hidden = true
)
public class MultiWorldGhostsPlugin extends Plugin {
   private static final Logger log = LoggerFactory.getLogger(MultiWorldGhostsPlugin.class);
   private final Set<Integer> ghostNpcIds = new HashSet();

   protected void startUp() {
      this.pushGhosts();
   }

   protected void shutDown() {
      GhostRender.setNpcIds(Collections.emptySet());
   }

   @Subscribe
   public void onGhostNpcsScript(GhostNpcsScript packet) {
      this.ghostNpcIds.clear();
      int[] var2 = packet.getIds();
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         int id = var2[var4];
         this.ghostNpcIds.add(id);
      }

      this.pushGhosts();
   }

   @Subscribe
   public void onGameStateChanged(GameStateChanged event) {
      switch (event.getGameState()) {
         case LOGIN_SCREEN:
         case HOPPING:
         case CONNECTION_LOST:
            this.ghostNpcIds.clear();
            this.pushGhosts();
         default:
      }
   }

   private void pushGhosts() {
      GhostRender.setNpcIds(this.ghostNpcIds);
   }
}
