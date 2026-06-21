package net.runelite.client.ui.overlay.tooltip;

import java.util.ArrayList;
import java.util.List;
import javax.inject.Singleton;

@Singleton
public class TooltipManager {
   private final List<Tooltip> tooltips = new ArrayList();

   public void add(Tooltip tooltip) {
      this.tooltips.add(tooltip);
   }

   public void addFront(Tooltip tooltip) {
      this.tooltips.add(0, tooltip);
   }

   public void clear() {
      this.tooltips.clear();
   }

   public List<Tooltip> getTooltips() {
      return this.tooltips;
   }
}
