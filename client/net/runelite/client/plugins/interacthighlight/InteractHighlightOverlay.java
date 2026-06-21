package net.runelite.client.plugins.interacthighlight;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import javax.inject.Inject;
import net.runelite.api.Actor;
import net.runelite.api.Client;
import net.runelite.api.ItemLayer;
import net.runelite.api.MenuAction;
import net.runelite.api.MenuEntry;
import net.runelite.api.NPC;
import net.runelite.api.Player;
import net.runelite.api.Point;
import net.runelite.api.TileItem;
import net.runelite.api.TileObject;
import net.runelite.api.widgets.WidgetUtil;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.outline.ModelOutlineRenderer;
import net.runelite.client.util.ColorUtil;

class InteractHighlightOverlay extends Overlay {
   private static final Color INTERACT_CLICK_COLOR = new Color(-1862270977);
   private final Client client;
   private final InteractHighlightPlugin plugin;
   private final InteractHighlightConfig config;
   private final ModelOutlineRenderer modelOutlineRenderer;

   @Inject
   private InteractHighlightOverlay(Client client, InteractHighlightPlugin plugin, InteractHighlightConfig config, ModelOutlineRenderer modelOutlineRenderer) {
      this.client = client;
      this.plugin = plugin;
      this.config = config;
      this.modelOutlineRenderer = modelOutlineRenderer;
      this.setPosition(OverlayPosition.DYNAMIC);
      this.setLayer(OverlayLayer.ABOVE_SCENE);
      this.setPriority(0.75F);
   }

   public Dimension render(Graphics2D graphics) {
      this.renderMouseover();
      this.renderTarget();
      return null;
   }

   private void renderMouseover() {
      MenuEntry[] menuEntries = this.client.getMenuEntries();
      if (menuEntries.length != 0) {
         MenuEntry entry = this.client.isMenuOpen() ? this.hoveredMenuEntry(menuEntries) : menuEntries[menuEntries.length - 1];
         MenuAction menuAction = entry.getType();
         int y;
         int id;
         int worldId;
         int x;
         switch (menuAction) {
            case WIDGET_TARGET_ON_GAME_OBJECT:
            case GAME_OBJECT_FIRST_OPTION:
            case GAME_OBJECT_SECOND_OPTION:
            case GAME_OBJECT_THIRD_OPTION:
            case GAME_OBJECT_FOURTH_OPTION:
            case GAME_OBJECT_FIFTH_OPTION:
            case EXAMINE_OBJECT:
               worldId = entry.getWorldViewId();
               x = entry.getParam0();
               y = entry.getParam1();
               id = entry.getIdentifier();
               TileObject tileObject = this.plugin.findTileObject(worldId, x, y, id);
               if (tileObject != null && this.config.objectShowHover() && (tileObject != this.plugin.getInteractedObject() || !this.config.objectShowInteract())) {
                  this.modelOutlineRenderer.drawOutline(tileObject, this.config.borderWidth(), this.config.objectHoverHighlightColor(), this.config.outlineFeather());
               }
               break;
            case WIDGET_TARGET_ON_GROUND_ITEM:
            case GROUND_ITEM_FIRST_OPTION:
            case GROUND_ITEM_SECOND_OPTION:
            case GROUND_ITEM_THIRD_OPTION:
            case GROUND_ITEM_FOURTH_OPTION:
            case GROUND_ITEM_FIFTH_OPTION:
            case EXAMINE_ITEM_GROUND:
               worldId = entry.getWorldViewId();
               x = entry.getParam0();
               y = entry.getParam1();
               id = entry.getIdentifier();
               ItemLayer layer = this.plugin.findItemLayer(worldId, x, y);
               TileItem item = this.plugin.findItem(layer, id);
               if (item != null && this.config.itemShowHover() && (item != this.plugin.getInteractedObject() || !this.config.itemShowInteract())) {
                  this.modelOutlineRenderer.drawOutline(layer, item, this.config.borderWidth(), this.config.itemHoverHighlightColor(), this.config.outlineFeather());
               }
               break;
            case WIDGET_TARGET_ON_NPC:
            case NPC_FIRST_OPTION:
            case NPC_SECOND_OPTION:
            case NPC_THIRD_OPTION:
            case NPC_FOURTH_OPTION:
            case NPC_FIFTH_OPTION:
            case EXAMINE_NPC:
               NPC npc = entry.getNpc();
               if (npc != null && this.config.npcShowHover() && (npc != this.plugin.getInteractedTarget() || !this.config.npcShowInteract())) {
                  Color highlightColor = menuAction != MenuAction.NPC_SECOND_OPTION && (menuAction != MenuAction.WIDGET_TARGET_ON_NPC || WidgetUtil.componentToInterface(this.client.getSelectedWidget().getId()) != 218) ? this.config.npcHoverHighlightColor() : this.config.npcAttackHoverHighlightColor();
                  this.modelOutlineRenderer.drawOutline((Actor)npc, this.config.borderWidth(), highlightColor, this.config.outlineFeather());
               }
               break;
            case WIDGET_TARGET_ON_PLAYER:
            case PLAYER_FIRST_OPTION:
            case PLAYER_SECOND_OPTION:
            case PLAYER_THIRD_OPTION:
            case PLAYER_FOURTH_OPTION:
            case PLAYER_FIFTH_OPTION:
            case PLAYER_SIXTH_OPTION:
            case PLAYER_SEVENTH_OPTION:
            case PLAYER_EIGHTH_OPTION:
               Player player = entry.getPlayer();
               if (player != null && this.config.playerShowHover() && (player != this.plugin.getInteractedTarget() || !this.config.npcShowInteract())) {
                  this.modelOutlineRenderer.drawOutline((Actor)player, this.config.borderWidth(), this.config.playerHoverHighlightColor(), this.config.outlineFeather());
               }
         }

      }
   }

   private void renderTarget() {
      TileObject interactedObject = this.plugin.getInteractedObject();
      TileItem item = this.plugin.getInteractedItem();
      Color clickColor;
      if (item != null && interactedObject != null && this.config.itemShowInteract()) {
         clickColor = this.getClickColor(this.config.itemHoverHighlightColor(), this.config.itemInteractHighlightColor(), (long)(this.client.getGameCycle() - this.plugin.getGameCycle()));
         this.modelOutlineRenderer.drawOutline((ItemLayer)interactedObject, item, this.config.borderWidth(), clickColor, this.config.outlineFeather());
      } else if (interactedObject != null && item == null && this.config.objectShowInteract()) {
         clickColor = this.getClickColor(this.config.objectHoverHighlightColor(), this.config.objectInteractHighlightColor(), (long)(this.client.getGameCycle() - this.plugin.getGameCycle()));
         this.modelOutlineRenderer.drawOutline(interactedObject, this.config.borderWidth(), clickColor, this.config.outlineFeather());
      }

      Actor target = this.plugin.getInteractedTarget();
      Color clickColor;
      if (target instanceof NPC && this.config.npcShowInteract()) {
         clickColor = this.plugin.isAttacked() ? this.config.npcAttackHoverHighlightColor() : this.config.npcHoverHighlightColor();
         Color endColor = this.plugin.isAttacked() ? this.config.npcAttackHighlightColor() : this.config.npcInteractHighlightColor();
         Color clickColor = this.getClickColor(clickColor, endColor, (long)(this.client.getGameCycle() - this.plugin.getGameCycle()));
         this.modelOutlineRenderer.drawOutline(target, this.config.borderWidth(), clickColor, this.config.outlineFeather());
      } else if (target instanceof Player && this.config.playerShowInteract()) {
         clickColor = this.getClickColor(this.config.playerHoverHighlightColor(), this.config.playerInteractHighlightColor(), (long)(this.client.getGameCycle() - this.plugin.getGameCycle()));
         this.modelOutlineRenderer.drawOutline(target, this.config.borderWidth(), clickColor, this.config.outlineFeather());
      }

   }

   private Color getClickColor(Color start, Color end, long time) {
      if (time < 5L) {
         return ColorUtil.colorLerp(start, INTERACT_CLICK_COLOR, (double)((float)time / 5.0F));
      } else {
         return time < 10L ? ColorUtil.colorLerp(INTERACT_CLICK_COLOR, end, (double)((float)(time - 5L) / 5.0F)) : end;
      }
   }

   private MenuEntry hoveredMenuEntry(MenuEntry[] menuEntries) {
      int menuX = this.client.getMenuX();
      int menuY = this.client.getMenuY();
      int menuWidth = this.client.getMenuWidth();
      Point mousePosition = this.client.getMouseCanvasPosition();
      int dy = mousePosition.getY() - menuY;
      dy -= 19;
      if (dy < 0) {
         return menuEntries[menuEntries.length - 1];
      } else {
         int idx = dy / 15;
         idx = menuEntries.length - 1 - idx;
         return mousePosition.getX() > menuX && mousePosition.getX() < menuX + menuWidth && idx >= 0 && idx < menuEntries.length ? menuEntries[idx] : menuEntries[menuEntries.length - 1];
      }
   }
}
