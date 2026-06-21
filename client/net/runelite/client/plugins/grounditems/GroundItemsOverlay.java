package net.runelite.client.plugins.grounditems;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.time.Instant;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.Perspective;
import net.runelite.api.Player;
import net.runelite.api.Point;
import net.runelite.api.WorldView;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldPoint;
import net.runelite.client.plugins.grounditems.config.DespawnTimerMode;
import net.runelite.client.plugins.grounditems.config.ItemHighlightMode;
import net.runelite.client.plugins.grounditems.config.OwnershipFilterMode;
import net.runelite.client.plugins.grounditems.config.PriceDisplayMode;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayUtil;
import net.runelite.client.ui.overlay.components.BackgroundComponent;
import net.runelite.client.ui.overlay.components.ProgressPieComponent;
import net.runelite.client.ui.overlay.components.TextComponent;
import net.runelite.client.util.QuantityFormatter;

public class GroundItemsOverlay extends Overlay {
   private static final int MAX_DISTANCE = 2500;
   private static final int OFFSET_Z = 20;
   private static final int STRING_GAP = 15;
   private static final int RECTANGLE_SIZE = 8;
   private static final Color PUBLIC_TIMER_COLOR;
   private static final Color PRIVATE_TIMER_COLOR;
   private static final int TIMER_OVERLAY_DIAMETER = 10;
   private final Client client;
   private final GroundItemsPlugin plugin;
   private final GroundItemsConfig config;
   private final StringBuilder itemStringBuilder = new StringBuilder();
   private final BackgroundComponent backgroundComponent = new BackgroundComponent();
   private final TextComponent textComponent = new TextComponent();
   private final ProgressPieComponent progressPieComponent = new ProgressPieComponent();
   private final Map<WorldPoint, Integer> offsetMap = new HashMap();

   @Inject
   private GroundItemsOverlay(Client client, GroundItemsPlugin plugin, GroundItemsConfig config) {
      this.setPosition(OverlayPosition.DYNAMIC);
      this.setLayer(OverlayLayer.ABOVE_SCENE);
      this.client = client;
      this.plugin = plugin;
      this.config = config;
   }

   public Dimension render(Graphics2D graphics) {
      boolean dontShowOverlay = (this.config.itemHighlightMode() == ItemHighlightMode.MENU || this.config.itemHighlightMode() == ItemHighlightMode.NONE || this.plugin.isHideAll()) && !this.plugin.isHotKeyPressed();
      if (dontShowOverlay && !this.config.highlightTiles()) {
         return null;
      } else {
         FontMetrics fm = graphics.getFontMetrics();
         Player player = this.client.getLocalPlayer();
         if (player == null) {
            return null;
         } else {
            this.offsetMap.clear();
            Point mousePos = this.client.getMouseCanvasPosition();
            Collection<GroundItem> groundItemList = this.plugin.getCollectedGroundItems().values();
            GroundItem topGroundItem = null;
            if (this.plugin.isHotKeyPressed()) {
               groundItemList = new ArrayList((Collection)groundItemList);
               java.awt.Point awtMousePos = new java.awt.Point(mousePos.getX(), mousePos.getY());
               GroundItem groundItem = null;
               Iterator var10 = ((Collection)groundItemList).iterator();

               label227:
               while(true) {
                  while(true) {
                     GroundItem item;
                     do {
                        if (!var10.hasNext()) {
                           if (groundItem != null) {
                              ((Collection)groundItemList).remove(groundItem);
                              ((Collection)groundItemList).add(groundItem);
                              topGroundItem = groundItem;
                           }
                           break label227;
                        }

                        item = (GroundItem)var10.next();
                        item.setOffset((Integer)this.offsetMap.compute(item.getItemLayer().getWorldLocation(), (k, v) -> {
                           return v != null ? v + 1 : 0;
                        }));
                     } while(groundItem != null);

                     if (this.plugin.getTextBoxBounds() != null && item.equals(this.plugin.getTextBoxBounds().getValue()) && ((Rectangle)this.plugin.getTextBoxBounds().getKey()).contains(awtMousePos)) {
                        groundItem = item;
                     } else if (this.plugin.getHiddenBoxBounds() != null && item.equals(this.plugin.getHiddenBoxBounds().getValue()) && ((Rectangle)this.plugin.getHiddenBoxBounds().getKey()).contains(awtMousePos)) {
                        groundItem = item;
                     } else if (this.plugin.getHighlightBoxBounds() != null && item.equals(this.plugin.getHighlightBoxBounds().getValue()) && ((Rectangle)this.plugin.getHighlightBoxBounds().getKey()).contains(awtMousePos)) {
                        groundItem = item;
                     }
                  }
               }
            }

            this.plugin.setTextBoxBounds((Map.Entry)null);
            this.plugin.setHiddenBoxBounds((Map.Entry)null);
            this.plugin.setHighlightBoxBounds((Map.Entry)null);
            LocalPoint localLocation = player.getLocalLocation();
            DespawnTimerMode groundItemTimers = this.config.groundItemTimers();
            boolean outline = this.config.textOutline();
            OwnershipFilterMode ownershipFilterMode = this.config.ownershipFilterMode();
            int accountType = this.client.getVarbitValue(1777);
            Iterator var13 = ((Collection)groundItemList).iterator();

            while(true) {
               GroundItem item;
               Color color;
               String itemString;
               Point textPoint;
               do {
                  LocalPoint groundPoint;
                  do {
                     do {
                        do {
                           do {
                              do {
                                 if (!var13.hasNext()) {
                                    return null;
                                 }

                                 item = (GroundItem)var13.next();
                                 WorldView wv = item.getItemLayer().getWorldView();
                                 groundPoint = LocalPoint.fromWorld(wv, item.getItemLayer().getWorldLocation());
                              } while(groundPoint == null);
                           } while(groundPoint.getWorldView() == 0 && localLocation.distanceTo(groundPoint) > 2500);
                        } while(!this.plugin.shouldDisplayItem(ownershipFilterMode, item.getOwnership(), accountType));

                        this.plugin.updateItemColor(item);
                     } while(!item.highlighted && !this.plugin.isHotKeyPressed() && (item.hidden || this.config.showHighlightedOnly()));

                     color = item.color;
                     if (this.config.highlightTiles()) {
                        Polygon poly = Perspective.getCanvasTilePoly(this.client, groundPoint, item.getItemLayer().getHeight());
                        if (poly != null) {
                           OverlayUtil.renderPolygon(graphics, poly, color);
                        }
                     }
                  } while(dontShowOverlay);

                  this.itemStringBuilder.append(item.getName());
                  if (item.getQuantity() > 1) {
                     this.itemStringBuilder.append(" (").append(QuantityFormatter.quantityToStackSize((long)item.getQuantity())).append(')');
                  }

                  if (item.getId() != 995) {
                     PriceDisplayMode displayMode = this.config.priceDisplayMode();
                     if (displayMode == PriceDisplayMode.BOTH) {
                        if (item.getGePrice() > 0) {
                           this.itemStringBuilder.append(" (GE: ").append(QuantityFormatter.quantityToStackSize((long)item.getGePrice())).append(" gp)");
                        }

                        if (item.getHaPrice() > 0) {
                           this.itemStringBuilder.append(" (HA: ").append(QuantityFormatter.quantityToStackSize((long)item.getHaPrice())).append(" gp)");
                        }
                     } else if (displayMode != PriceDisplayMode.OFF) {
                        int price = displayMode == PriceDisplayMode.GE ? item.getGePrice() : item.getHaPrice();
                        if (price > 0) {
                           this.itemStringBuilder.append(" (").append(QuantityFormatter.quantityToStackSize((long)price)).append(" gp)");
                        }
                     }
                  }

                  itemString = this.itemStringBuilder.toString();
                  this.itemStringBuilder.setLength(0);
                  textPoint = Perspective.getCanvasTextLocation(this.client, graphics, groundPoint, itemString, item.getItemLayer().getHeight() + 20);
               } while(textPoint == null);

               int offset = this.plugin.isHotKeyPressed() ? item.getOffset() : (Integer)this.offsetMap.compute(item.getItemLayer().getWorldLocation(), (k, v) -> {
                  return v != null ? v + 1 : 0;
               });
               int textX = textPoint.getX();
               int textY = textPoint.getY() - 15 * offset;
               if (this.plugin.isHotKeyPressed()) {
                  int stringWidth = fm.stringWidth(itemString);
                  int stringHeight = fm.getHeight();
                  int x = textX - 2;
                  int y = textY - stringHeight - 2;
                  int width = stringWidth + 4;
                  int height = stringHeight + 4;
                  Rectangle itemBounds = new Rectangle(x, y, width, height);
                  x += width + 2;
                  y = textY - (8 + stringHeight) / 2;
                  int height = 8;
                  int width = 8;
                  Rectangle itemHiddenBox = new Rectangle(x, y, width, height);
                  x += width + 2;
                  Rectangle itemHighlightBox = new Rectangle(x, y, width, height);
                  boolean mouseInBox = itemBounds.contains(mousePos.getX(), mousePos.getY());
                  boolean mouseInHiddenBox = itemHiddenBox.contains(mousePos.getX(), mousePos.getY());
                  boolean mouseInHighlightBox = itemHighlightBox.contains(mousePos.getX(), mousePos.getY());
                  if (mouseInBox) {
                     this.plugin.setTextBoxBounds(new AbstractMap.SimpleEntry(itemBounds, item));
                  } else if (mouseInHiddenBox) {
                     this.plugin.setHiddenBoxBounds(new AbstractMap.SimpleEntry(itemHiddenBox, item));
                  } else if (mouseInHighlightBox) {
                     this.plugin.setHighlightBoxBounds(new AbstractMap.SimpleEntry(itemHighlightBox, item));
                  }

                  boolean topItem = topGroundItem == item;
                  if (topItem && (mouseInBox || mouseInHiddenBox || mouseInHighlightBox)) {
                     this.backgroundComponent.setRectangle(itemBounds);
                     this.backgroundComponent.render(graphics);
                  }

                  this.drawRectangle(graphics, itemHiddenBox, topItem && mouseInHiddenBox ? Color.RED : color, item.hidden, true);
                  this.drawRectangle(graphics, itemHighlightBox, topItem && mouseInHighlightBox ? Color.GREEN : color, item.highlighted, false);
               }

               if (groundItemTimers != DespawnTimerMode.PIE && !this.plugin.isHotKeyPressed()) {
                  if (groundItemTimers == DespawnTimerMode.SECONDS || groundItemTimers == DespawnTimerMode.TICKS) {
                     Instant despawnTime = this.calculateDespawnTime(item);
                     Color timerColor = this.getItemTimerColor(item);
                     if (despawnTime != null && timerColor != null) {
                        long despawnTimeMillis = despawnTime.toEpochMilli() - Instant.now().toEpochMilli();
                        String timerText;
                        if (groundItemTimers == DespawnTimerMode.SECONDS) {
                           timerText = String.format(" - %.1f", (float)despawnTimeMillis / 1000.0F);
                        } else {
                           timerText = String.format(" - %d", despawnTimeMillis / 600L);
                        }

                        this.textComponent.setText(timerText);
                        this.textComponent.setColor(timerColor);
                        this.textComponent.setOutline(outline);
                        this.textComponent.setPosition(new java.awt.Point(textX + fm.stringWidth(itemString), textY));
                        this.textComponent.render(graphics);
                     }
                  }
               } else {
                  this.drawTimerPieOverlay(graphics, textX, textY, item);
               }

               this.textComponent.setText(itemString);
               this.textComponent.setColor(color);
               this.textComponent.setOutline(outline);
               this.textComponent.setPosition(new java.awt.Point(textX, textY));
               this.textComponent.render(graphics);
            }
         }
      }
   }

   private Instant calculateDespawnTime(GroundItem groundItem) {
      Instant spawnTime = groundItem.getSpawnTime();
      if (spawnTime == null) {
         return null;
      } else {
         Instant despawnTime = spawnTime.plus(groundItem.getDespawnTime());
         return Instant.now().isAfter(despawnTime) ? null : despawnTime;
      }
   }

   private Color getItemTimerColor(GroundItem groundItem) {
      Instant spawnTime = groundItem.getSpawnTime();
      if (spawnTime == null) {
         return null;
      } else {
         Instant now = Instant.now();
         Instant despawnTime = spawnTime.plus(groundItem.getDespawnTime());
         if (groundItem.isPrivate()) {
            return despawnTime.isAfter(now) ? PRIVATE_TIMER_COLOR : null;
         } else {
            Instant visibleTime = spawnTime.plus(groundItem.getVisibleTime());
            if (visibleTime.isAfter(now)) {
               return PRIVATE_TIMER_COLOR;
            } else {
               return despawnTime.isAfter(now) ? PUBLIC_TIMER_COLOR : null;
            }
         }
      }
   }

   private void drawTimerPieOverlay(Graphics2D graphics, int textX, int textY, GroundItem groundItem) {
      Instant now = Instant.now();
      Instant spawnTime = groundItem.getSpawnTime();
      Instant despawnTime = this.calculateDespawnTime(groundItem);
      Color fillColor = this.getItemTimerColor(groundItem);
      if (spawnTime != null && despawnTime != null && fillColor != null) {
         float percent = (float)(now.toEpochMilli() - spawnTime.toEpochMilli()) / (float)(despawnTime.toEpochMilli() - spawnTime.toEpochMilli());
         this.progressPieComponent.setDiameter(10);
         int x = textX - 10;
         int y = textY - 5;
         this.progressPieComponent.setPosition(new Point(x, y));
         this.progressPieComponent.setFill(fillColor);
         this.progressPieComponent.setBorderColor(fillColor);
         this.progressPieComponent.setProgress((double)(1.0F - percent));
         this.progressPieComponent.render(graphics);
      }
   }

   private void drawRectangle(Graphics2D graphics, Rectangle rect, Color color, boolean inList, boolean hiddenBox) {
      graphics.setColor(Color.BLACK);
      graphics.drawRect(rect.x + 1, rect.y + 1, rect.width, rect.height);
      graphics.setColor(color);
      graphics.draw(rect);
      if (inList) {
         graphics.fill(rect);
      }

      graphics.setColor(Color.WHITE);
      graphics.drawLine(rect.x + 2, rect.y + rect.height / 2, rect.x + rect.width - 2, rect.y + rect.height / 2);
      if (!hiddenBox) {
         graphics.drawLine(rect.x + rect.width / 2, rect.y + 2, rect.x + rect.width / 2, rect.y + rect.height - 2);
      }

   }

   static {
      PUBLIC_TIMER_COLOR = Color.YELLOW;
      PRIVATE_TIMER_COLOR = Color.GREEN;
   }
}
