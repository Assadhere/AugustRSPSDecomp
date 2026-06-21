package net.runelite.client.events;

import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayMenuEntry;

public final class OverlayMenuClicked {
   private final OverlayMenuEntry entry;
   private final Overlay overlay;

   public OverlayMenuClicked(OverlayMenuEntry entry, Overlay overlay) {
      this.entry = entry;
      this.overlay = overlay;
   }

   public OverlayMenuEntry getEntry() {
      return this.entry;
   }

   public Overlay getOverlay() {
      return this.overlay;
   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof OverlayMenuClicked)) {
         return false;
      } else {
         OverlayMenuClicked other = (OverlayMenuClicked)o;
         Object this$entry = this.getEntry();
         Object other$entry = other.getEntry();
         if (this$entry == null) {
            if (other$entry != null) {
               return false;
            }
         } else if (!this$entry.equals(other$entry)) {
            return false;
         }

         Object this$overlay = this.getOverlay();
         Object other$overlay = other.getOverlay();
         if (this$overlay == null) {
            if (other$overlay != null) {
               return false;
            }
         } else if (!this$overlay.equals(other$overlay)) {
            return false;
         }

         return true;
      }
   }

   public int hashCode() {
      int PRIME = true;
      int result = 1;
      Object $entry = this.getEntry();
      result = result * 59 + ($entry == null ? 43 : $entry.hashCode());
      Object $overlay = this.getOverlay();
      result = result * 59 + ($overlay == null ? 43 : $overlay.hashCode());
      return result;
   }

   public String toString() {
      String var10000 = String.valueOf(this.getEntry());
      return "OverlayMenuClicked(entry=" + var10000 + ", overlay=" + String.valueOf(this.getOverlay()) + ")";
   }
}
