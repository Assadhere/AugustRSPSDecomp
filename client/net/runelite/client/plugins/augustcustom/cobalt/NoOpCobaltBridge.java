package net.runelite.client.plugins.augustcustom.cobalt;

import custom.CobaltClickScript;
import custom.CobaltMenuScript;
import custom.CobaltPlayerRow;
import java.util.List;

class NoOpCobaltBridge implements CobaltBridge {
   public void initialize(CobaltBridge.Deps deps) {
   }

   public void cleanup() {
   }

   public void onListReset(int total) {
   }

   public void onListChunk(List<CobaltPlayerRow> rows) {
   }

   public void onSession(boolean enter, String targetName, int targetWidth, int targetHeight) {
   }

   public void onCamera(int yaw, int pitch, int zoom) {
   }

   public void onClientTick() {
   }

   public void onScroll(int widgetId, int scrollX, int scrollY) {
   }

   public void onResolution(int width, int height) {
   }

   public void onClick(CobaltClickScript click) {
   }

   public void onMenu(CobaltMenuScript menu) {
   }

   public void onTab(int tab) {
   }

   public void onLoggedIn() {
   }

   public boolean shouldDrawLocalPlayer() {
      return true;
   }
}
