package net.runelite.client.game.npcoverlay;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import javax.inject.Inject;
import javax.inject.Singleton;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.NPC;
import net.runelite.api.WorldView;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.NpcChanged;
import net.runelite.api.events.NpcDespawned;
import net.runelite.api.events.NpcSpawned;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.eventbus.EventBus;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.ui.overlay.OverlayManager;
import net.runelite.client.ui.overlay.outline.ModelOutlineRenderer;

@Singleton
public class NpcOverlayService {
   private final Client client;
   private final ClientThread clientThread;
   private final List<Function<NPC, HighlightedNpc>> highlightFunctions = new ArrayList();
   private final Map<NPC, HighlightedNpc> highlightedNpcs = new HashMap();

   @Inject
   private NpcOverlayService(Client client, ClientThread clientThread, OverlayManager overlayManager, ModelOutlineRenderer modelOutlineRenderer, EventBus eventBus) {
      this.client = client;
      this.clientThread = clientThread;
      overlayManager.add(new NpcOverlay(client, modelOutlineRenderer, this.highlightedNpcs));
      overlayManager.add(new NpcMinimapOverlay(this.highlightedNpcs));
      eventBus.register(this);
   }

   @Subscribe
   private void onGameStateChanged(GameStateChanged event) {
      if (event.getGameState() == GameState.LOGIN_SCREEN || event.getGameState() == GameState.HOPPING) {
         this.highlightedNpcs.clear();
      }

   }

   @Subscribe(
      priority = -1.0F
   )
   private void onNpcSpawned(NpcSpawned npcSpawned) {
      NPC npc = npcSpawned.getNpc();
      Iterator var3 = this.highlightFunctions.iterator();

      HighlightedNpc highlightedNpc;
      do {
         if (!var3.hasNext()) {
            return;
         }

         Function<NPC, HighlightedNpc> f = (Function)var3.next();
         highlightedNpc = (HighlightedNpc)f.apply(npc);
      } while(highlightedNpc == null);

      this.highlightedNpcs.put(npc, highlightedNpc);
   }

   @Subscribe(
      priority = -1.0F
   )
   private void onNpcDespawned(NpcDespawned npcDespawned) {
      NPC npc = npcDespawned.getNpc();
      this.highlightedNpcs.remove(npc);
   }

   @Subscribe(
      priority = -1.0F
   )
   private void onNpcChanged(NpcChanged event) {
      NPC npc = event.getNpc();
      this.highlightedNpcs.remove(npc);
      Iterator var3 = this.highlightFunctions.iterator();

      HighlightedNpc highlightedNpc;
      do {
         if (!var3.hasNext()) {
            return;
         }

         Function<NPC, HighlightedNpc> f = (Function)var3.next();
         highlightedNpc = (HighlightedNpc)f.apply(npc);
      } while(highlightedNpc == null);

      this.highlightedNpcs.put(npc, highlightedNpc);
   }

   public void rebuild() {
      this.clientThread.invoke(() -> {
         this.highlightedNpcs.clear();
         this.rebuild(this.client.getTopLevelWorldView());
      });
   }

   private void rebuild(WorldView wv) {
      if (wv != null) {
         Iterator var2 = wv.npcs().iterator();

         while(true) {
            while(var2.hasNext()) {
               NPC npc = (NPC)var2.next();
               Iterator var4 = this.highlightFunctions.iterator();

               while(var4.hasNext()) {
                  Function<NPC, HighlightedNpc> f = (Function)var4.next();
                  HighlightedNpc highlightedNpc = (HighlightedNpc)f.apply(npc);
                  if (highlightedNpc != null) {
                     this.highlightedNpcs.put(npc, highlightedNpc);
                     break;
                  }
               }
            }

            var2 = wv.worldViews().iterator();

            while(var2.hasNext()) {
               WorldView sub = (WorldView)var2.next();
               this.rebuild(sub);
            }

            return;
         }
      }
   }

   public void registerHighlighter(Function<NPC, HighlightedNpc> p) {
      this.highlightFunctions.add(p);
      this.rebuild();
   }

   public void unregisterHighlighter(Function<NPC, HighlightedNpc> p) {
      this.highlightFunctions.remove(p);
      this.rebuild();
   }
}
