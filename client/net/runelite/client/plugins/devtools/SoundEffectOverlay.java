package net.runelite.client.plugins.devtools;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.Player;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.events.AreaSoundEffectPlayed;
import net.runelite.api.events.SoundEffectPlayed;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.ui.overlay.OverlayPanel;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.components.LineComponent;

class SoundEffectOverlay extends OverlayPanel {
   private static final int MAX_LINES = 16;
   private static final Color COLOR_SOUND_EFFECT;
   private static final Color COLOR_AREA_SOUND_EFFECT;
   private static final Color COLOR_SILENT_SOUND_EFFECT;
   private final Client client;
   private final DevToolsPlugin plugin;

   @Inject
   SoundEffectOverlay(Client client, DevToolsPlugin plugin) {
      this.client = client;
      this.plugin = plugin;
      this.panelComponent.getChildren().add(LineComponent.builder().left("Sound Effects").leftColor(Color.CYAN).build());
      this.setClearChildren(false);
      this.setPosition(OverlayPosition.TOP_LEFT);
   }

   public Dimension render(Graphics2D graphics) {
      return !this.plugin.getSoundEffects().isActive() ? null : super.render(graphics);
   }

   @Subscribe
   public void onSoundEffectPlayed(SoundEffectPlayed event) {
      if (this.plugin.getSoundEffects().isActive()) {
         int var10000 = event.getSoundId();
         String text = "Id: " + var10000 + " - D: " + event.getDelay();
         this.panelComponent.getChildren().add(LineComponent.builder().left(text).leftColor(COLOR_SOUND_EFFECT).build());
         this.checkMaxLines();
      }
   }

   @Subscribe
   public void onAreaSoundEffectPlayed(AreaSoundEffectPlayed event) {
      if (this.plugin.getSoundEffects().isActive()) {
         Color textColor = COLOR_AREA_SOUND_EFFECT;
         Player localPlayer = this.client.getLocalPlayer();
         if (localPlayer != null) {
            LocalPoint lp = localPlayer.getLocalLocation();
            if (lp != null) {
               int sceneX = lp.getSceneX();
               int sceneY = lp.getSceneY();
               int distance = Math.abs(sceneX - event.getSceneX()) + Math.abs(sceneY - event.getSceneY());
               if (distance > event.getRange()) {
                  textColor = COLOR_SILENT_SOUND_EFFECT;
               }
            }
         }

         int var10000 = event.getSoundId();
         String text = "Id: " + var10000 + " - S: " + (event.getSource() != null ? event.getSource().getName() : "<none>") + " - L: " + event.getSceneX() + "," + event.getSceneY() + " - R: " + event.getRange() + " - D: " + event.getDelay();
         this.panelComponent.getChildren().add(LineComponent.builder().left(text).leftColor(textColor).build());
         this.checkMaxLines();
      }
   }

   private void checkMaxLines() {
      while(this.panelComponent.getChildren().size() > 16) {
         this.panelComponent.getChildren().remove(1);
      }

   }

   static {
      COLOR_SOUND_EFFECT = Color.WHITE;
      COLOR_AREA_SOUND_EFFECT = Color.YELLOW;
      COLOR_SILENT_SOUND_EFFECT = Color.GRAY;
   }
}
