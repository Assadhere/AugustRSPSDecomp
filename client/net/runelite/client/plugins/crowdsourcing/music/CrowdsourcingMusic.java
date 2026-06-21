package net.runelite.client.plugins.crowdsourcing.music;

import javax.inject.Inject;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.ChatMessage;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.crowdsourcing.CrowdsourcingManager;

public class CrowdsourcingMusic {
   private static final String MUSIC_UNLOCK_MESSAGE = "You have unlocked a new music track:";
   @Inject
   private Client client;
   @Inject
   private ClientThread clientThread;
   @Inject
   private CrowdsourcingManager manager;

   @Subscribe
   public void onChatMessage(ChatMessage event) {
      if (event.getType() == ChatMessageType.GAMEMESSAGE) {
         String message = event.getMessage();
         if (message.contains("You have unlocked a new music track:")) {
            this.clientThread.invokeLater(() -> {
               LocalPoint local = LocalPoint.fromWorld(this.client, this.client.getLocalPlayer().getWorldLocation());
               WorldPoint location = WorldPoint.fromLocalInstance(this.client, local);
               boolean isInInstance = this.client.isInInstancedRegion();
               MusicUnlockData data = new MusicUnlockData(location, isInInstance, message);
               this.manager.storeEvent(data);
            });
         }
      }

   }
}
