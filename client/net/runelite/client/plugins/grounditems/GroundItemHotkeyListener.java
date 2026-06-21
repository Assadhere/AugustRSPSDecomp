package net.runelite.client.plugins.grounditems;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.Objects;
import javax.inject.Inject;
import net.runelite.client.util.HotkeyListener;

class GroundItemHotkeyListener extends HotkeyListener {
   private final GroundItemsPlugin plugin;
   private final GroundItemsConfig config;
   private Instant lastPress;

   @Inject
   private GroundItemHotkeyListener(GroundItemsPlugin plugin, GroundItemsConfig config) {
      Objects.requireNonNull(config);
      super(config::hotkey);
      this.plugin = plugin;
      this.config = config;
   }

   public void hotkeyPressed() {
      if (this.plugin.isHideAll()) {
         this.plugin.setHideAll(false);
         this.plugin.setHotKeyPressed(true);
         this.lastPress = null;
      } else if (this.lastPress != null && !this.plugin.isHotKeyPressed() && this.config.doubleTapDelay() > 0 && Duration.between(this.lastPress, Instant.now()).compareTo(Duration.ofMillis((long)this.config.doubleTapDelay())) < 0) {
         this.plugin.setHideAll(true);
         this.lastPress = null;
      } else {
         this.plugin.setHotKeyPressed(true);
         this.lastPress = Instant.now();
      }

   }

   public void hotkeyReleased() {
      this.plugin.setHotKeyPressed(false);
      this.plugin.setTextBoxBounds((Map.Entry)null);
      this.plugin.setHiddenBoxBounds((Map.Entry)null);
      this.plugin.setHighlightBoxBounds((Map.Entry)null);
   }
}
