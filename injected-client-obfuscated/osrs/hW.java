package osrs;

import net.runelite.api.CameraFocusableEntity;

public interface hW extends CameraFocusableEntity {
   int l();

   int m();

   default int D() {
      return this.m();
   }

   int k();

   float w();

   float v();
}
