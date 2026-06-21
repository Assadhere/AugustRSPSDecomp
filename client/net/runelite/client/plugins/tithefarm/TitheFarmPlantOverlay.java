package net.runelite.client.plugins.tithefarm;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.Perspective;
import net.runelite.api.Point;
import net.runelite.api.coords.LocalPoint;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.components.ProgressPieComponent;
import net.runelite.client.util.ColorUtil;

public class TitheFarmPlantOverlay extends Overlay {
   private final Client client;
   private final TitheFarmPlugin plugin;
   private final TitheFarmPluginConfig config;
   private final Map<TitheFarmPlantState, Color> borders = new HashMap();
   private final Map<TitheFarmPlantState, Color> fills = new HashMap();

   @Inject
   TitheFarmPlantOverlay(Client client, TitheFarmPlugin plugin, TitheFarmPluginConfig config) {
      this.setPosition(OverlayPosition.DYNAMIC);
      this.setLayer(OverlayLayer.ABOVE_SCENE);
      this.plugin = plugin;
      this.config = config;
      this.client = client;
   }

   public void updateConfig() {
      this.borders.clear();
      this.fills.clear();
      Color colorUnwateredBorder = this.config.getColorUnwatered();
      Color colorUnwatered = ColorUtil.colorWithAlpha(colorUnwateredBorder, (int)((double)colorUnwateredBorder.getAlpha() / 2.5));
      this.borders.put(TitheFarmPlantState.UNWATERED, colorUnwateredBorder);
      this.fills.put(TitheFarmPlantState.UNWATERED, colorUnwatered);
      Color colorWateredBorder = this.config.getColorWatered();
      Color colorWatered = ColorUtil.colorWithAlpha(colorWateredBorder, (int)((double)colorWateredBorder.getAlpha() / 2.5));
      this.borders.put(TitheFarmPlantState.WATERED, colorWateredBorder);
      this.fills.put(TitheFarmPlantState.WATERED, colorWatered);
   }

   public Dimension render(Graphics2D graphics) {
      Iterator var2 = this.plugin.getPlants().iterator();

      while(var2.hasNext()) {
         TitheFarmPlant plant = (TitheFarmPlant)var2.next();
         if (plant.getState() != TitheFarmPlantState.DEAD && plant.getState() != TitheFarmPlantState.GROWN) {
            LocalPoint localLocation = LocalPoint.fromWorld(this.client, plant.getWorldLocation());
            if (localLocation != null) {
               Point canvasLocation = Perspective.localToCanvas(this.client, localLocation, this.client.getPlane());
               if (canvasLocation != null) {
                  ProgressPieComponent progressPieComponent = new ProgressPieComponent();
                  progressPieComponent.setPosition(canvasLocation);
                  progressPieComponent.setProgress(1.0 - plant.getPlantTimeRelative());
                  progressPieComponent.setBorderColor((Color)this.borders.get(plant.getState()));
                  progressPieComponent.setFill((Color)this.fills.get(plant.getState()));
                  progressPieComponent.render(graphics);
               }
            }
         }
      }

      return null;
   }
}
