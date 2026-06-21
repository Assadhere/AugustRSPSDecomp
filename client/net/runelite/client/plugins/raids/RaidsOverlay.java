package net.runelite.client.plugins.raids;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.util.Iterator;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.FriendsChatManager;
import net.runelite.api.MenuAction;
import net.runelite.client.plugins.raids.solver.Room;
import net.runelite.client.ui.overlay.OverlayPanel;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.components.LineComponent;
import net.runelite.client.ui.overlay.components.TitleComponent;

class RaidsOverlay extends OverlayPanel {
   private static final int OLM_PLANE = 0;
   private static final String SCREENSHOT_ACTION = "Screenshot";
   private final Client client;
   private final RaidsPlugin plugin;
   private final RaidsConfig config;
   private boolean scoutOverlayShown = false;

   @Inject
   private RaidsOverlay(Client client, RaidsPlugin plugin, RaidsConfig config) {
      super(plugin);
      this.setPosition(OverlayPosition.TOP_LEFT);
      this.setPriority(0.0F);
      this.client = client;
      this.plugin = plugin;
      this.config = config;
      this.addMenuEntry(MenuAction.RUNELITE_OVERLAY_CONFIG, "Configure", "Raids overlay");
      this.addMenuEntry(MenuAction.RUNELITE_OVERLAY, "Screenshot", "Raids overlay", (e) -> {
         plugin.screenshotScoutOverlay();
      });
   }

   public Dimension render(Graphics2D graphics) {
      this.scoutOverlayShown = this.shouldShowOverlay();
      if (!this.scoutOverlayShown) {
         return null;
      } else {
         Color color = Color.WHITE;
         String layout = this.plugin.getRaid().getLayout().toCodeString();
         if (this.config.enableLayoutWhitelist() && !this.plugin.getLayoutWhitelist().contains(layout.toLowerCase())) {
            color = Color.RED;
         }

         this.panelComponent.getChildren().add(TitleComponent.builder().text(layout).color(color).build());
         if (this.config.fcDisplay()) {
            color = Color.RED;
            FriendsChatManager friendsChatManager = this.client.getFriendsChatManager();
            FontMetrics metrics = graphics.getFontMetrics();
            String worldString = "W" + this.client.getWorld();
            String owner = "Join a FC";
            if (friendsChatManager != null) {
               owner = friendsChatManager.getOwner();
               color = Color.ORANGE;
            }

            this.panelComponent.setPreferredSize(new Dimension(Math.max(129, metrics.stringWidth(worldString) + metrics.stringWidth(owner) + 14), 0));
            this.panelComponent.getChildren().add(LineComponent.builder().left(worldString).right(owner).leftColor(Color.ORANGE).rightColor(color).build());
         }

         Iterator var9 = this.plugin.getRaid().getLayout().getRooms().iterator();

         while(true) {
            while(true) {
               RaidRoom room;
               do {
                  if (!var9.hasNext()) {
                     return super.render(graphics);
                  }

                  Room layoutRoom = (Room)var9.next();
                  int position = layoutRoom.getPosition();
                  room = this.plugin.getRaid().getRoom(position);
               } while(room == null);

               color = Color.WHITE;
               String name;
               switch (room.getType()) {
                  case COMBAT:
                     if (this.plugin.getRoomWhitelist().contains(room.getName().toLowerCase())) {
                        color = Color.GREEN;
                     } else if (this.plugin.getRoomBlacklist().contains(room.getName().toLowerCase()) || this.config.enableRotationWhitelist() && !this.plugin.getRotationMatches()) {
                        color = Color.RED;
                     }

                     name = room == RaidRoom.UNKNOWN_COMBAT ? "Unknown" : room.getName();
                     this.panelComponent.getChildren().add(LineComponent.builder().left(room.getType().getName()).right(name).rightColor(color).build());
                     break;
                  case PUZZLE:
                     if (this.plugin.getRoomWhitelist().contains(room.getName().toLowerCase())) {
                        color = Color.GREEN;
                     } else if (this.plugin.getRoomBlacklist().contains(room.getName().toLowerCase())) {
                        color = Color.RED;
                     }

                     name = room == RaidRoom.UNKNOWN_PUZZLE ? "Unknown" : room.getName();
                     this.panelComponent.getChildren().add(LineComponent.builder().left(room.getType().getName()).right(name).rightColor(color).build());
               }
            }
         }
      }
   }

   private boolean shouldShowOverlay() {
      if (this.plugin.getRaid() != null && this.plugin.getRaid().getLayout() != null && this.config.scoutOverlay()) {
         if (this.plugin.isInRaidChambers()) {
            if (this.client.getVarbitValue(5425) > 0) {
               return this.client.getPlane() == 0 ? false : this.config.scoutOverlayInRaid();
            } else {
               return true;
            }
         } else {
            return this.plugin.getRaidPartyID() != -1 && this.config.scoutOverlayAtBank();
         }
      } else {
         return false;
      }
   }

   public boolean isScoutOverlayShown() {
      return this.scoutOverlayShown;
   }
}
