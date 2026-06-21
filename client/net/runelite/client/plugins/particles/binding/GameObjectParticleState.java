package net.runelite.client.plugins.particles.binding;

import java.util.ArrayList;
import java.util.List;
import net.runelite.client.plugins.particles.core.ParticleEffector;
import net.runelite.client.plugins.particles.core.ParticleEmitter;

public class GameObjectParticleState {
   public final List<ParticleEmitter> emitters = new ArrayList();
   public final List<ParticleEffector> effectors = new ArrayList();
   public GameObjectModelScanResult scanResult;

   public void clear() {
      this.emitters.clear();
      this.effectors.clear();
      this.scanResult = null;
   }

   public boolean isEmpty() {
      return this.emitters.isEmpty() && this.effectors.isEmpty();
   }
}
