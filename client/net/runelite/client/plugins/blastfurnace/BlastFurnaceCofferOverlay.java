package net.runelite.client.plugins.blastfurnace;

import java.awt.Dimension;
import java.awt.Graphics2D;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.MenuAction;
import net.runelite.api.widgets.Widget;
import net.runelite.client.ui.overlay.OverlayPanel;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.components.LineComponent;
import net.runelite.client.util.QuantityFormatter;
import org.apache.commons.lang3.time.DurationFormatUtils;

class BlastFurnaceCofferOverlay extends OverlayPanel {
   private static final float COST_PER_HOUR = 72000.0F;
   private final Client client;
   private final BlastFurnacePlugin plugin;
   private final BlastFurnaceConfig config;

   @Inject
   private BlastFurnaceCofferOverlay(Client client, BlastFurnacePlugin plugin, BlastFurnaceConfig config) {
      super(plugin);
      this.setPosition(OverlayPosition.TOP_LEFT);
      this.client = client;
      this.plugin = plugin;
      this.config = config;
      this.addMenuEntry(MenuAction.RUNELITE_OVERLAY_CONFIG, "Configure", "Coffer overlay");
   }

   public Dimension render(Graphics2D graphics) {
      if (this.plugin.getConveyorBelt() == null) {
         return null;
      } else {
         Widget sack = this.client.getWidget(31064066);
         if (sack != null) {
            int coffer = this.client.getVarbitValue(5357);
            sack.setHidden(true);
            this.panelComponent.getChildren().add(LineComponent.builder().left("Coffer:").right(QuantityFormatter.quantityToStackSize((long)coffer) + " gp").build());
            if (this.config.showCofferTime()) {
               long millis = (long)((float)coffer / 72000.0F * 60.0F * 60.0F * 1000.0F);
               this.panelComponent.getChildren().add(LineComponent.builder().left("Time:").right(DurationFormatUtils.formatDuration(millis, "H'h' m'm' s's'", true)).build());
            }
         }

         return super.render(graphics);
      }
   }
}
