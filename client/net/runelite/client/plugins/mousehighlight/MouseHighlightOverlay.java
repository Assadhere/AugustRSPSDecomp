package net.runelite.client.plugins.mousehighlight;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableSet;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.util.Set;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.MenuAction;
import net.runelite.api.MenuEntry;
import net.runelite.api.widgets.WidgetUtil;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.tooltip.Tooltip;
import net.runelite.client.ui.overlay.tooltip.TooltipManager;

class MouseHighlightOverlay extends Overlay {
   private static final Set<MenuAction> WIDGET_MENU_ACTIONS;
   private final TooltipManager tooltipManager;
   private final Client client;
   private final MouseHighlightConfig config;

   @Inject
   MouseHighlightOverlay(Client client, TooltipManager tooltipManager, MouseHighlightConfig config) {
      this.setPosition(OverlayPosition.DYNAMIC);
      this.setLayer(OverlayLayer.ABOVE_WIDGETS);
      this.drawAfterInterface(165);
      this.client = client;
      this.tooltipManager = tooltipManager;
      this.config = config;
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
            String target = menuEntry.getTarget();
            String option = menuEntry.getOption();
            MenuAction type = menuEntry.getType();
            if (type != MenuAction.RUNELITE_OVERLAY && type != MenuAction.CC_OP_LOW_PRIORITY) {
               if (Strings.isNullOrEmpty(option)) {
                  return null;
               } else {
                  switch (option) {
                     case "Walk here":
                     case "Cancel":
                     case "Continue":
                     case "Set heading":
                        return null;
                     case "Move":
                        if (target.contains("Sliding piece")) {
                           return null;
                        }
                     default:
                        int tooltipTimeout;
                        if (WIDGET_MENU_ACTIONS.contains(type)) {
                           tooltipTimeout = menuEntry.getParam1();
                           groupId = WidgetUtil.componentToInterface(tooltipTimeout);
                           if (!this.config.uiTooltip()) {
                              return null;
                           }

                           if (!this.config.chatboxTooltip() && groupId == 162) {
                              return null;
                           }

                           if (this.config.disableSpellbooktooltip() && groupId == 218) {
                              return null;
                           }
                        }

                        tooltipTimeout = this.client.getVarcIntValue(1);
                        if (tooltipTimeout > this.client.getGameCycle()) {
                           return null;
                        } else {
                           groupId = this.client.getVarcIntValue(2);
                           if (groupId == 1) {
                              return null;
                           } else {
                              this.tooltipManager.addFront(new Tooltip(option + (Strings.isNullOrEmpty(target) ? "" : " " + target)));
                              return null;
                           }
                        }
                  }
               }
            } else {
               return null;
            }
         }
      }
   }

   static {
      WIDGET_MENU_ACTIONS = ImmutableSet.of(MenuAction.WIDGET_TYPE_1, MenuAction.WIDGET_TARGET, MenuAction.WIDGET_CLOSE, MenuAction.WIDGET_TYPE_4, MenuAction.WIDGET_TYPE_5, MenuAction.WIDGET_CONTINUE, new MenuAction[]{MenuAction.ITEM_USE_ON_ITEM, MenuAction.WIDGET_USE_ON_ITEM, MenuAction.ITEM_FIRST_OPTION, MenuAction.ITEM_SECOND_OPTION, MenuAction.ITEM_THIRD_OPTION, MenuAction.ITEM_FOURTH_OPTION, MenuAction.ITEM_FIFTH_OPTION, MenuAction.ITEM_USE, MenuAction.WIDGET_FIRST_OPTION, MenuAction.WIDGET_SECOND_OPTION, MenuAction.WIDGET_THIRD_OPTION, MenuAction.WIDGET_FOURTH_OPTION, MenuAction.WIDGET_FIFTH_OPTION, MenuAction.EXAMINE_ITEM, MenuAction.WIDGET_TARGET_ON_WIDGET, MenuAction.CC_OP_LOW_PRIORITY, MenuAction.CC_OP});
   }
}
