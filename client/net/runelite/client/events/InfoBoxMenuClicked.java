package net.runelite.client.events;

import net.runelite.client.ui.overlay.OverlayMenuEntry;
import net.runelite.client.ui.overlay.infobox.InfoBox;

public final class InfoBoxMenuClicked {
   private final OverlayMenuEntry entry;
   private final InfoBox infoBox;

   public InfoBoxMenuClicked(OverlayMenuEntry entry, InfoBox infoBox) {
      this.entry = entry;
      this.infoBox = infoBox;
   }

   public OverlayMenuEntry getEntry() {
      return this.entry;
   }

   public InfoBox getInfoBox() {
      return this.infoBox;
   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof InfoBoxMenuClicked)) {
         return false;
      } else {
         InfoBoxMenuClicked other = (InfoBoxMenuClicked)o;
         Object this$entry = this.getEntry();
         Object other$entry = other.getEntry();
         if (this$entry == null) {
            if (other$entry != null) {
               return false;
            }
         } else if (!this$entry.equals(other$entry)) {
            return false;
         }

         Object this$infoBox = this.getInfoBox();
         Object other$infoBox = other.getInfoBox();
         if (this$infoBox == null) {
            if (other$infoBox != null) {
               return false;
            }
         } else if (!this$infoBox.equals(other$infoBox)) {
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
      Object $infoBox = this.getInfoBox();
      result = result * 59 + ($infoBox == null ? 43 : $infoBox.hashCode());
      return result;
   }

   public String toString() {
      String var10000 = String.valueOf(this.getEntry());
      return "InfoBoxMenuClicked(entry=" + var10000 + ", infoBox=" + String.valueOf(this.getInfoBox()) + ")";
   }
}
