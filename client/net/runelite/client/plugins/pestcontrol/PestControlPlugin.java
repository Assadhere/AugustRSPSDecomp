package net.runelite.client.plugins.pestcontrol;

import com.google.common.collect.ImmutableSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.inject.Inject;
import net.runelite.api.ChatMessageType;
import net.runelite.api.GameState;
import net.runelite.api.NPC;
import net.runelite.api.events.ChatMessage;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.NpcDespawned;
import net.runelite.api.events.NpcSpawned;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;

@PluginDescriptor(
   name = "Pest Control",
   description = "Show helpful information for the Pest Control minigame",
   tags = {"minigame", "overlay"}
)
public class PestControlPlugin extends Plugin {
   private static final Set<Integer> SPINNER_IDS = ImmutableSet.of(1709, 1710, 1711, 1712, 1713);
   private final Pattern SHIELD_DROP = Pattern.compile("The ([a-z]+), [^ ]+ portal shield has dropped!", 2);
   private final List<NPC> spinners = new ArrayList();
   @Inject
   private OverlayManager overlayManager;
   @Inject
   private PestControlOverlay overlay;

   protected void startUp() throws Exception {
      this.overlayManager.add(this.overlay);
   }

   protected void shutDown() throws Exception {
      this.overlayManager.remove(this.overlay);
      this.spinners.clear();
   }

   @Subscribe
   public void onGameStateChanged(GameStateChanged event) {
      GameState gameState = event.getGameState();
      if (gameState == GameState.CONNECTION_LOST || gameState == GameState.LOGIN_SCREEN || gameState == GameState.HOPPING) {
         this.spinners.clear();
      }

   }

   @Subscribe
   public void onChatMessage(ChatMessage chatMessage) {
      if (this.overlay.getGame() != null && chatMessage.getType() == ChatMessageType.GAMEMESSAGE) {
         Matcher matcher = this.SHIELD_DROP.matcher(chatMessage.getMessage());
         if (matcher.lookingAt()) {
            this.overlay.getGame().fall(matcher.group(1));
         }
      }

   }

   @Subscribe
   public void onNpcSpawned(NpcSpawned event) {
      NPC npc = event.getNpc();
      if (SPINNER_IDS.contains(npc.getId())) {
         this.spinners.add(npc);
      }

   }

   @Subscribe
   public void onNpcDespawned(NpcDespawned event) {
      this.spinners.remove(event.getNpc());
   }

   List<NPC> getSpinners() {
      return this.spinners;
   }
}
