package net.runelite.client.plugins.friendnotes;

import java.awt.Dimension;
import java.awt.Graphics2D;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.tooltip.Tooltip;
import net.runelite.client.ui.overlay.tooltip.TooltipManager;

class FriendNoteOverlay extends Overlay {
   private final Client client;
   private final FriendNotesPlugin plugin;
   private final TooltipManager tooltipManager;

   @Inject
   private FriendNoteOverlay(Client client, FriendNotesPlugin plugin, TooltipManager tooltipManager) {
      this.client = client;
      this.plugin = plugin;
      this.tooltipManager = tooltipManager;
   }

   public Dimension render(Graphics2D graphics) {
      if (this.client.isMenuOpen()) {
         return null;
      } else {
         HoveredFriend hovered = this.plugin.getHoveredFriend();
         if (hovered != null) {
            String content = hovered.getNote();
            this.tooltipManager.add(new Tooltip(content));
         }

         return null;
      }
   }
}
