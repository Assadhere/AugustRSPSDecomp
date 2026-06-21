package net.runelite.client.plugins.augustcustom.cobalt;

import custom.CobaltClickScript;
import custom.CobaltMenuScript;
import custom.CobaltPlayerRow;
import java.util.List;
import net.runelite.api.Client;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.game.SpriteManager;
import net.runelite.client.ui.ClientToolbar;
import net.runelite.client.ui.overlay.OverlayManager;

public interface CobaltBridge {
   void initialize(Deps var1);

   void cleanup();

   void onListReset(int var1);

   void onListChunk(List<CobaltPlayerRow> var1);

   void onSession(boolean var1, String var2, int var3, int var4);

   void onCamera(int var1, int var2, int var3);

   void onClientTick();

   void onScroll(int var1, int var2, int var3);

   void onResolution(int var1, int var2);

   void onClick(CobaltClickScript var1);

   void onMenu(CobaltMenuScript var1);

   void onTab(int var1);

   void onLoggedIn();

   boolean shouldDrawLocalPlayer();

   public static class Deps {
      public final Client client;
      public final ClientThread clientThread;
      public final ClientToolbar clientToolbar;
      public final OverlayManager overlayManager;
      public final SpriteManager spriteManager;

      public Deps(Client client, ClientThread clientThread, ClientToolbar clientToolbar, OverlayManager overlayManager, SpriteManager spriteManager) {
         this.client = client;
         this.clientThread = clientThread;
         this.clientToolbar = clientToolbar;
         this.overlayManager = overlayManager;
         this.spriteManager = spriteManager;
      }
   }
}
