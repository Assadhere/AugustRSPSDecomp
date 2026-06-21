package net.runelite.client.plugins.particles.render;

import java.awt.Dimension;
import java.awt.Graphics2D;
import javax.inject.Inject;
import net.runelite.client.plugins.PluginManager;
import net.runelite.client.plugins.particles.ParticlePlugin;
import net.runelite.client.plugins.particles.config.ParticlePluginConfig;
import net.runelite.client.plugins.particles.core.ParticleSystem;
import net.runelite.client.ui.overlay.OverlayPanel;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.components.LineComponent;

public class ParticleDebugOverlay extends OverlayPanel {
   private final ParticlePlugin plugin;
   private final ParticlePluginConfig config;

   @Inject
   public ParticleDebugOverlay(ParticlePlugin plugin, ParticlePluginConfig config, PluginManager pluginManager) {
      super(plugin);
      this.plugin = plugin;
      this.config = config;
      this.setPosition(OverlayPosition.TOP_LEFT);
      this.panelComponent.setPreferredSize(new Dimension(179, 0));
   }

   public Dimension render(Graphics2D graphics) {
      if (this.config.enabled() && this.config.showDebug()) {
         ParticleSystem system = this.plugin.getParticleSystem();
         if (system == null) {
            return null;
         } else {
            int particleCount = system.getTotalParticleCount();
            int emitterCount = system.getEmitterCount();
            Particle3DRenderer renderer = this.plugin.getParticle3DRenderer();
            int renderTotal = renderer != null ? renderer.getLastTotalCount() : 0;
            int depthCulled = (renderer != null ? renderer.getLastDepthCulledCount() : 0) + system.getLastWorkerDepthCulledCount();
            int frustumCulled = system.getLastWorkerFrustumCulledCount();
            int total = renderTotal + depthCulled + frustumCulled;
            this.panelComponent.getChildren().add(LineComponent.builder().left("Particles:").right(Integer.toString(particleCount)).build());
            this.panelComponent.getChildren().add(LineComponent.builder().left("Emitters:").right(Integer.toString(emitterCount)).build());
            this.panelComponent.getChildren().add(LineComponent.builder().left("Culled (depth):").right(formatCull(depthCulled, total)).build());
            this.panelComponent.getChildren().add(LineComponent.builder().left("Culled (frustum):").right(formatCull(frustumCulled, total)).build());
            return super.render(graphics);
         }
      } else {
         return null;
      }
   }

   private static String formatCull(int count, int total) {
      if (total <= 0) {
         return "0";
      } else {
         int pct = count * 100 / total;
         return "" + count + " (" + pct + "%)";
      }
   }
}
