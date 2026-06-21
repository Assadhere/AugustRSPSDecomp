package net.runelite.client.plugins.augustcustom.ghostrendertuner;

import com.google.inject.Provides;
import custom.GhostRender;
import javax.inject.Inject;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@PluginDescriptor(
   name = "Ghost Render Tuner",
   description = "Developer: live-tune multi-world ghost transparency and spectral tint.",
   tags = {"ghost", "multiworld", "developer", "tint", "transparency"},
   enabledByDefault = false,
   developerPlugin = true
)
public class GhostRenderTunerPlugin extends Plugin {
   private static final Logger log = LoggerFactory.getLogger(GhostRenderTunerPlugin.class);
   @Inject
   private GhostRenderTunerConfig config;

   @Provides
   GhostRenderTunerConfig provideConfig(ConfigManager configManager) {
      return (GhostRenderTunerConfig)configManager.getConfig(GhostRenderTunerConfig.class);
   }

   protected void startUp() {
      this.apply();
   }

   protected void shutDown() {
      GhostRender.resetToDefaults();
   }

   @Subscribe
   public void onConfigChanged(ConfigChanged event) {
      if ("ghostrendertuner".equals(event.getGroup())) {
         this.apply();
      }

   }

   private void apply() {
      if (!this.config.override()) {
         GhostRender.resetToDefaults();
      } else {
         GhostRender.setAlpha(this.config.alpha());
         GhostRender.setTint(this.config.tintHue(), this.config.tintSaturation(), this.config.tintLuminance(), this.config.tintAmount());
         log.info("Ghost render tuned: alpha={} tint(hue={}, sat={}, lum={}, amount={})", new Object[]{this.config.alpha(), this.config.tintHue(), this.config.tintSaturation(), this.config.tintLuminance(), this.config.tintAmount()});
      }
   }
}
