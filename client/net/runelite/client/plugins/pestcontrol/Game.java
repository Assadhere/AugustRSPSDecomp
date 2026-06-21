package net.runelite.client.plugins.pestcontrol;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class Game {
   private static final Logger log = LoggerFactory.getLogger(Game.class);
   private Rotation[] possibleRotations = Rotation.values();
   private int shieldsDropped;
   private final PortalContext purple;
   private final PortalContext blue;
   private final PortalContext yellow;
   private final PortalContext red;

   Game() {
      this.purple = new PortalContext(Portal.PURPLE);
      this.blue = new PortalContext(Portal.BLUE);
      this.yellow = new PortalContext(Portal.YELLOW);
      this.red = new PortalContext(Portal.RED);
   }

   void fall(String color) {
      switch (color.toLowerCase()) {
         case "purple":
            this.fall(this.purple);
            break;
         case "red":
            this.fall(this.red);
            break;
         case "yellow":
            this.fall(this.yellow);
            break;
         case "blue":
            this.fall(this.blue);
      }

   }

   private void fall(PortalContext portal) {
      if (portal.isShielded()) {
         log.debug("Shield dropped for {}", portal.getPortal());
         portal.setShielded(false);
         int shieldDrop = this.shieldsDropped++;
         List<Rotation> rotations = new ArrayList();
         Rotation[] var4 = this.possibleRotations;
         int var5 = var4.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            Rotation rotation = var4[var6];
            if (rotation.getPortal(shieldDrop) == portal.getPortal()) {
               rotations.add(rotation);
            }
         }

         this.possibleRotations = (Rotation[])rotations.toArray(new Rotation[rotations.size()]);
      }
   }

   void die(PortalContext portal) {
      if (!portal.isDead()) {
         log.debug("Portal {} died", portal.getPortal());
         portal.setDead(true);
      }
   }

   Collection<Portal> getNextPortals() {
      List<Portal> portals = new ArrayList();
      Rotation[] var2 = this.possibleRotations;
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         Rotation rotation = var2[var4];
         Portal portal = rotation.getPortal(this.shieldsDropped);
         if (portal != null && !portals.contains(portal)) {
            portals.add(portal);
         }
      }

      return portals;
   }

   public PortalContext getPurple() {
      return this.purple;
   }

   public PortalContext getBlue() {
      return this.blue;
   }

   public PortalContext getYellow() {
      return this.yellow;
   }

   public PortalContext getRed() {
      return this.red;
   }
}
