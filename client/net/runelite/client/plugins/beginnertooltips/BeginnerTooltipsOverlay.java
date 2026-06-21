package net.runelite.client.plugins.beginnertooltips;

import com.google.common.collect.ImmutableSet;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.util.Set;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.MenuAction;
import net.runelite.api.MenuEntry;
import net.runelite.api.NPC;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.tooltip.Tooltip;
import net.runelite.client.ui.overlay.tooltip.TooltipManager;

class BeginnerTooltipsOverlay extends Overlay {
   private static final int MAX_WIDTH = 225;
   private static final String HEADER = "<col=ff981f>'Beginner Tooltips' plugin:</col><br>";
   private static final Set<MenuAction> OBJECT_MENU_ACTIONS;
   private static final Set<MenuAction> NPC_MENU_ACTIONS;
   private final Client client;
   private final TooltipManager tooltipManager;

   @Inject
   BeginnerTooltipsOverlay(Client client, TooltipManager tooltipManager) {
      this.client = client;
      this.tooltipManager = tooltipManager;
      this.setPosition(OverlayPosition.DYNAMIC);
      this.setLayer(OverlayLayer.ABOVE_WIDGETS);
   }

   public Dimension render(Graphics2D graphics) {
      if (this.client.isMenuOpen()) {
         return null;
      } else {
         MenuEntry[] menuEntries = this.client.getMenuEntries();
         int last = menuEntries.length - 1;
         if (last < 0) {
            return null;
         } else {
            MenuEntry menuEntry = menuEntries[last];
            MenuAction type = menuEntry.getType();
            String text = null;
            if (OBJECT_MENU_ACTIONS.contains(type)) {
               text = (String)BeginnerTooltipsData.OBJECTS.get(menuEntry.getIdentifier());
            } else if (NPC_MENU_ACTIONS.contains(type)) {
               NPC npc = menuEntry.getNpc();
               if (npc != null && npc.getName() != null) {
                  text = (String)BeginnerTooltipsData.NPCS.get(npc.getName());
               }
            }

            if (text == null) {
               return null;
            } else {
               this.tooltipManager.add(new Tooltip("<col=ff981f>'Beginner Tooltips' plugin:</col><br>" + wrap(graphics.getFontMetrics(), text)));
               return null;
            }
         }
      }
   }

   private static String wrap(FontMetrics metrics, String text) {
      StringBuilder out = new StringBuilder();
      String[] segments = text.split("<br>", -1);

      for(int s = 0; s < segments.length; ++s) {
         if (s > 0) {
            out.append("<br>");
         }

         StringBuilder line = new StringBuilder();
         String[] var6 = segments[s].split(" ");
         int var7 = var6.length;

         for(int var8 = 0; var8 < var7; ++var8) {
            String word = var6[var8];
            if (!word.isEmpty()) {
               if (line.length() > 0) {
                  String var10001 = String.valueOf(line);
                  if (metrics.stringWidth(var10001 + " " + word) > 225) {
                     out.append(line).append("<br>");
                     line.setLength(0);
                  }
               }

               if (line.length() > 0) {
                  line.append(' ');
               }

               line.append(word);
            }
         }

         out.append(line);
      }

      return out.toString();
   }

   static {
      OBJECT_MENU_ACTIONS = ImmutableSet.of(MenuAction.GAME_OBJECT_FIRST_OPTION, MenuAction.GAME_OBJECT_SECOND_OPTION, MenuAction.GAME_OBJECT_THIRD_OPTION, MenuAction.GAME_OBJECT_FOURTH_OPTION, MenuAction.GAME_OBJECT_FIFTH_OPTION, MenuAction.EXAMINE_OBJECT, new MenuAction[]{MenuAction.WIDGET_TARGET_ON_GAME_OBJECT});
      NPC_MENU_ACTIONS = ImmutableSet.of(MenuAction.NPC_FIRST_OPTION, MenuAction.NPC_SECOND_OPTION, MenuAction.NPC_THIRD_OPTION, MenuAction.NPC_FOURTH_OPTION, MenuAction.NPC_FIFTH_OPTION, MenuAction.EXAMINE_NPC, new MenuAction[]{MenuAction.WIDGET_TARGET_ON_NPC});
   }
}
