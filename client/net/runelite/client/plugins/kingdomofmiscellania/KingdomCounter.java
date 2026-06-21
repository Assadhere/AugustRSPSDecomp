package net.runelite.client.plugins.kingdomofmiscellania;

import java.awt.image.BufferedImage;
import net.runelite.client.ui.overlay.infobox.Counter;
import net.runelite.client.util.QuantityFormatter;

public class KingdomCounter extends Counter {
   private final KingdomPlugin plugin;

   KingdomCounter(BufferedImage image, KingdomPlugin plugin) {
      super(image, plugin, plugin.getApproval());
      this.plugin = plugin;
   }

   public String getText() {
      return KingdomPlugin.getApprovalPercent(this.plugin.getApproval()) + "%";
   }

   public String getTooltip() {
      int var10000 = this.plugin.getApproval();
      return "Approval: " + var10000 + "/127</br>Coffer: " + QuantityFormatter.quantityToStackSize((long)this.plugin.getCoffer());
   }
}
