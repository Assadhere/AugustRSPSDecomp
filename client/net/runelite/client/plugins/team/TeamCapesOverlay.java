package net.runelite.client.plugins.team;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.util.Iterator;
import java.util.Map;
import javax.inject.Inject;
import net.runelite.api.MenuAction;
import net.runelite.client.game.ItemManager;
import net.runelite.client.ui.overlay.OverlayPanel;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.components.ComponentOrientation;
import net.runelite.client.ui.overlay.components.ImageComponent;

class TeamCapesOverlay extends OverlayPanel {
   private final TeamPlugin plugin;
   private final TeamConfig config;
   private final ItemManager manager;

   @Inject
   private TeamCapesOverlay(TeamPlugin plugin, TeamConfig config, ItemManager manager) {
      super(plugin);
      this.setPosition(OverlayPosition.TOP_LEFT);
      this.setPriority(0.0F);
      this.plugin = plugin;
      this.config = config;
      this.manager = manager;
      this.panelComponent.setWrap(true);
      this.panelComponent.setOrientation(ComponentOrientation.HORIZONTAL);
      this.addMenuEntry(MenuAction.RUNELITE_OVERLAY_CONFIG, "Configure", "Teamcapes overlay");
   }

   public Dimension render(Graphics2D graphics) {
      Map<Integer, Integer> teams = this.plugin.getTeams();
      if (!teams.isEmpty() && this.config.teamCapesOverlay()) {
         Iterator var3 = teams.entrySet().iterator();

         while(var3.hasNext()) {
            Map.Entry<Integer, Integer> team = (Map.Entry)var3.next();
            if ((Integer)team.getValue() >= this.config.getMinimumCapeCount()) {
               int teamcapeNumber = (Integer)team.getKey() - 1;
               int itemID;
               if (teamcapeNumber < 50) {
                  itemID = 2 * teamcapeNumber + 4315;
               } else {
                  itemID = 3 * (teamcapeNumber - 50) + 20211;
               }

               this.panelComponent.getChildren().add(new ImageComponent(this.manager.getImage(itemID, (Integer)team.getValue(), true)));
            }
         }

         return super.render(graphics);
      } else {
         return null;
      }
   }
}
