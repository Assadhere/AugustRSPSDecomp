package net.runelite.client.plugins.particles.binding;

import java.util.ArrayList;
import java.util.List;

public class GameObjectModelScanResult {
   public final List<MarkerFace> emitterFaces = new ArrayList();
   public final List<MarkerFace> effectorFaces = new ArrayList();
   public final boolean hasMarkers;

   public GameObjectModelScanResult(List<MarkerFace> emitters, List<MarkerFace> effectors) {
      this.emitterFaces.addAll(emitters);
      this.effectorFaces.addAll(effectors);
      this.hasMarkers = !emitters.isEmpty() || !effectors.isEmpty();
   }

   public static class MarkerFace {
      public final int faceIndex;
      public final int markerId;
      public final boolean inheritDirection;

      public MarkerFace(int faceIndex, int markerId, boolean inheritDirection) {
         this.faceIndex = faceIndex;
         this.markerId = markerId;
         this.inheritDirection = inheritDirection;
      }
   }
}
