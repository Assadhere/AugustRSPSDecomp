package net.runelite.client.plugins.pyramidplunder;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.util.Iterator;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.GameObject;
import net.runelite.api.ObjectComposition;
import net.runelite.api.Point;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.widgets.Widget;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayUtil;
import net.runelite.client.util.ColorUtil;

class PyramidPlunderOverlay extends Overlay {
   private static final int MAX_DISTANCE = 2350;
   private final Client client;
   private final PyramidPlunderPlugin plugin;
   private final PyramidPlunderConfig config;

   @Inject
   private PyramidPlunderOverlay(Client client, PyramidPlunderPlugin plugin, PyramidPlunderConfig config) {
      super(plugin);
      this.setPosition(OverlayPosition.DYNAMIC);
      this.setLayer(OverlayLayer.ABOVE_SCENE);
      this.client = client;
      this.plugin = plugin;
      this.config = config;
   }

   public Dimension render(Graphics2D graphics) {
      Widget ppWidget = this.client.getWidget(28049410);
      if (ppWidget == null) {
         return null;
      } else {
         ppWidget.setHidden(this.config.hideTimer());
         LocalPoint playerLocation = this.client.getLocalPlayer().getLocalLocation();
         int currentFloor = this.client.getVarbitValue(2377);
         Iterator var5 = this.plugin.getObjectsToHighlight().iterator();

         while(true) {
            GameObject object;
            ObjectComposition imposter;
            do {
               do {
                  do {
                     do {
                        do {
                           if (!var5.hasNext()) {
                              Point mousePosition = this.client.getMouseCanvasPosition();
                              this.plugin.getTilesToHighlight().forEach((objectx, tile) -> {
                                 if ((this.config.highlightDoors() || !PyramidPlunderPlugin.TOMB_DOOR_WALL_IDS.contains(objectx.getId())) && (this.config.highlightSpeartraps() || 21280 != objectx.getId()) && tile.getPlane() == this.client.getPlane() && objectx.getLocalLocation().distanceTo(playerLocation) < 2350) {
                                    Color highlightColor;
                                    if (21280 == objectx.getId()) {
                                       if (this.client.getVarbitValue(2365) != 1) {
                                          return;
                                       }

                                       highlightColor = this.config.highlightSpeartrapsColor();
                                    } else {
                                       ObjectComposition imposter = this.client.getObjectDefinition(objectx.getId()).getImpostor();
                                       if (imposter.getId() != 20948) {
                                          return;
                                       }

                                       highlightColor = this.config.highlightDoorsColor();
                                    }

                                    Shape objectClickbox = objectx.getClickbox();
                                    if (objectClickbox != null) {
                                       if (objectClickbox.contains((double)mousePosition.getX(), (double)mousePosition.getY())) {
                                          graphics.setColor(highlightColor.darker());
                                       } else {
                                          graphics.setColor(highlightColor);
                                       }

                                       graphics.draw(objectClickbox);
                                       graphics.setColor(ColorUtil.colorWithAlpha(highlightColor, highlightColor.getAlpha() / 5));
                                       graphics.fill(objectClickbox);
                                    }

                                 }
                              });
                              return null;
                           }

                           object = (GameObject)var5.next();
                        } while(this.config.highlightUrnsFloor() > currentFloor && PyramidPlunderPlugin.URN_IDS.contains(object.getId()));
                     } while(this.config.highlightChestFloor() > currentFloor && 26616 == object.getId());
                  } while(this.config.highlightSarcophagusFloor() > currentFloor && 26626 == object.getId());
               } while(object.getLocalLocation().distanceTo(playerLocation) >= 2350);

               imposter = this.client.getObjectDefinition(object.getId()).getImpostor();
            } while(!PyramidPlunderPlugin.URN_CLOSED_IDS.contains(imposter.getId()) && 20946 != imposter.getId() && 21255 != imposter.getId());

            Shape shape = object.getConvexHull();
            if (shape != null) {
               OverlayUtil.renderPolygon(graphics, shape, this.config.highlightContainersColor());
            }
         }
      }
   }
}
