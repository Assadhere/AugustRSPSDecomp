package net.runelite.client.ui.overlay;

import com.google.common.base.MoreObjects;
import com.google.common.primitives.Ints;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.swing.SwingUtilities;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.events.BeforeRender;
import net.runelite.api.events.ClientTick;
import net.runelite.api.events.FocusChanged;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetItem;
import net.runelite.client.chat.ChatMessageManager;
import net.runelite.client.chat.QueuedMessage;
import net.runelite.client.config.RuneLiteConfig;
import net.runelite.client.eventbus.EventBus;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.OverlayMenuClicked;
import net.runelite.client.externalplugins.ExternalPluginMdc;
import net.runelite.client.input.KeyManager;
import net.runelite.client.input.MouseAdapter;
import net.runelite.client.input.MouseManager;
import net.runelite.client.ui.ClientUI;
import net.runelite.client.ui.JagexColors;
import net.runelite.client.util.ColorUtil;
import net.runelite.client.util.HotkeyListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

@Singleton
public class OverlayRenderer extends MouseAdapter {
   private static final Logger log = LoggerFactory.getLogger(OverlayRenderer.class);
   private static final Marker DEDUPLICATE = MarkerFactory.getMarker("DEDUPLICATE");
   private static final int BORDER = 5;
   private static final int BORDER_TOP = 20;
   private static final int PADDING = 2;
   private static final int OVERLAY_RESIZE_TOLERANCE = 5;
   private static final Dimension SNAP_CORNER_SIZE = new Dimension(80, 80);
   private static final Color SNAP_CORNER_COLOR = new Color(0, 255, 255, 50);
   private static final Color SNAP_CORNER_ACTIVE_COLOR = new Color(0, 255, 0, 100);
   private static final Color MOVING_OVERLAY_COLOR = new Color(255, 255, 0, 100);
   private static final Color MOVING_OVERLAY_ACTIVE_COLOR = new Color(255, 255, 0, 200);
   private static final Color MOVING_OVERLAY_TARGET_COLOR;
   private static final Color MOVING_OVERLAY_RESIZING_COLOR;
   private final Client client;
   private final OverlayManager overlayManager;
   private final RuneLiteConfig runeLiteConfig;
   private final ClientUI clientUI;
   private final EventBus eventBus;
   private final ChatMessageManager chatMessageManager;
   private Font font;
   private Font tooltipFont;
   private Font interfaceFont;
   private final Point overlayOffset = new Point();
   private final Point mousePosition = new Point();
   private Overlay currentManagedOverlay;
   private Overlay dragTargetOverlay;
   private Rectangle currentManagedBounds;
   private boolean inOverlayManagingMode;
   private boolean inOverlayResizingMode;
   private boolean inOverlayDraggingMode;
   private boolean startedMovingOverlay;
   private Overlay curHoveredOverlay;
   private Overlay lastHoveredOverlay;
   private Rectangle viewportBounds;
   private Rectangle chatboxBounds;
   private boolean chatboxHidden;
   private boolean isResizeable;
   private OverlayBounds emptySnapCorners;
   private OverlayBounds snapCorners;
   private boolean dragWarn;

   @Inject
   private OverlayRenderer(Client client, OverlayManager overlayManager, RuneLiteConfig runeLiteConfig, MouseManager mouseManager, KeyManager keyManager, ClientUI clientUI, EventBus eventBus, ChatMessageManager chatMessageManager) {
      this.client = client;
      this.overlayManager = overlayManager;
      this.runeLiteConfig = runeLiteConfig;
      this.clientUI = clientUI;
      this.eventBus = eventBus;
      this.chatMessageManager = chatMessageManager;
      Objects.requireNonNull(runeLiteConfig);
      HotkeyListener hotkeyListener = new HotkeyListener(runeLiteConfig::dragHotkey) {
         public void hotkeyPressed() {
            OverlayRenderer.this.inOverlayManagingMode = true;
         }

         public void hotkeyReleased() {
            if (OverlayRenderer.this.inOverlayManagingMode) {
               OverlayRenderer.this.inOverlayManagingMode = false;
               OverlayRenderer.this.resetOverlayManagementMode();
            }

         }
      };
      keyManager.registerKeyListener(hotkeyListener);
      mouseManager.registerMouseListener(this);
      eventBus.register(this);
   }

   @Subscribe
   public void onFocusChanged(FocusChanged event) {
      if (!event.isFocused()) {
         if (this.inOverlayManagingMode) {
            this.inOverlayManagingMode = false;
            this.resetOverlayManagementMode();
         }

         this.curHoveredOverlay = null;
      }

   }

   @Subscribe
   protected void onClientTick(ClientTick t) {
      this.lastHoveredOverlay = this.curHoveredOverlay;
      Overlay overlay = this.curHoveredOverlay;
      if (overlay != null && !this.client.isMenuOpen()) {
         boolean shift = this.client.isKeyPressed(81);
         if (shift) {
            List<OverlayMenuEntry> menuEntries = overlay.getMenuEntries();
            if (!menuEntries.isEmpty()) {
               for(int i = menuEntries.size() - 1; i >= 0; --i) {
                  OverlayMenuEntry overlayMenuEntry = (OverlayMenuEntry)menuEntries.get(i);
                  this.client.createMenuEntry(-1).setOption(overlayMenuEntry.getOption()).setTarget(ColorUtil.wrapWithColorTag(overlayMenuEntry.getTarget(), JagexColors.MENU_TARGET)).setType(overlayMenuEntry.getMenuAction()).onClick((Consumer)MoreObjects.firstNonNull(overlayMenuEntry.callback, (e) -> {
                     this.eventBus.post(new OverlayMenuClicked(overlayMenuEntry, overlay));
                  }));
               }

            }
         }
      }
   }

   @Subscribe
   public void onBeforeRender(BeforeRender event) {
      this.curHoveredOverlay = null;
      if (this.client.getGameState() == GameState.LOGGED_IN) {
         if (this.shouldInvalidateBounds()) {
            this.emptySnapCorners = this.buildSnapCorners();
         }

         this.snapCorners = new OverlayBounds(this.emptySnapCorners);
      }

   }

   public void renderOverlayLayer(Graphics2D graphics, OverlayLayer layer) {
      Collection<Overlay> overlays = this.overlayManager.getLayer(layer);
      this.renderOverlays(graphics, overlays, layer);
   }

   public void renderAfterInterface(Graphics2D graphics, int interfaceId, Collection<WidgetItem> widgetItems) {
      Collection<Overlay> overlays = this.overlayManager.getForInterface(interfaceId);
      this.overlayManager.setWidgetItems(widgetItems);
      this.renderOverlays(graphics, overlays, OverlayLayer.ABOVE_WIDGETS);
      this.overlayManager.setWidgetItems(Collections.emptyList());
   }

   public void renderAfterLayer(Graphics2D graphics, Widget layer, Collection<WidgetItem> widgetItems) {
      Collection<Overlay> overlays = this.overlayManager.getForLayer(layer.getId());
      this.overlayManager.setWidgetItems(widgetItems);
      this.renderOverlays(graphics, overlays, OverlayLayer.ABOVE_WIDGETS);
      this.overlayManager.setWidgetItems(Collections.emptyList());
   }

   private void renderOverlays(Graphics2D graphics, Collection<Overlay> overlays, OverlayLayer layer) {
      if (overlays != null && !overlays.isEmpty() && this.client.getGameState() == GameState.LOGGED_IN) {
         OverlayUtil.setGraphicProperties(graphics);
         if (this.inOverlayDraggingMode && layer == OverlayLayer.UNDER_WIDGETS && this.currentManagedOverlay != null && this.currentManagedOverlay.isSnappable()) {
            OverlayBounds translatedSnapCorners = this.snapCorners.translated(-SNAP_CORNER_SIZE.width, -SNAP_CORNER_SIZE.height);
            Color previous = graphics.getColor();
            Iterator var6 = translatedSnapCorners.getBounds().iterator();

            while(var6.hasNext()) {
               Rectangle corner = (Rectangle)var6.next();
               graphics.setColor(corner.contains(this.mousePosition) ? SNAP_CORNER_ACTIVE_COLOR : SNAP_CORNER_COLOR);
               graphics.fill(corner);
            }

            graphics.setColor(previous);
         }

         AffineTransform transform = graphics.getTransform();
         Stroke stroke = graphics.getStroke();
         Composite composite = graphics.getComposite();
         Paint paint = graphics.getPaint();
         RenderingHints renderingHints = graphics.getRenderingHints();
         Color background = graphics.getBackground();
         this.font = this.runeLiteConfig.dynamicOverlayFont().getFont();
         this.tooltipFont = this.runeLiteConfig.tooltipFont().getFont();
         this.interfaceFont = this.runeLiteConfig.interfaceFont().getFont();
         Rectangle clip = this.clipBounds(layer);
         graphics.setClip(clip);
         Iterator var11 = overlays.iterator();

         while(true) {
            Overlay overlay;
            Rectangle bounds;
            do {
               if (!var11.hasNext()) {
                  return;
               }

               overlay = (Overlay)var11.next();
               OverlayPosition overlayPosition = this.getCorrectedOverlayPosition(overlay);
               bounds = overlay.getBounds();
               Dimension dimension = bounds.getSize();
               Point preferredLocation = overlay.getPreferredLocation();
               Rectangle snapCorner = null;
               Point location;
               if (overlayPosition != OverlayPosition.DYNAMIC && overlayPosition != OverlayPosition.TOOLTIP && overlayPosition != OverlayPosition.DETACHED && preferredLocation == null) {
                  snapCorner = this.snapCorners.forPosition(overlayPosition);
                  Point translation = OverlayUtil.transformPosition(overlayPosition, dimension);
                  int destX = snapCorner.x + translation.x;
                  int destY = snapCorner.y + translation.y;
                  location = this.clampOverlayLocation(destX, destY, dimension.width, dimension.height, overlay);
               } else {
                  location = preferredLocation != null ? preferredLocation : bounds.getLocation();
                  location = this.clampOverlayLocation(location.x, location.y, dimension.width, dimension.height, overlay);
               }

               if (overlay.getPreferredSize() != null) {
                  bounds.setSize(overlay.getPreferredSize());
               }

               this.safeRender(overlay, graphics, location);
               if (snapCorner != null && bounds.width + bounds.height > 0) {
                  OverlayUtil.shiftSnapCorner(overlayPosition, snapCorner, bounds, 2);
               }

               graphics.setTransform(transform);
               graphics.setStroke(stroke);
               graphics.setComposite(composite);
               graphics.setPaint(paint);
               graphics.setRenderingHints(renderingHints);
               graphics.setBackground(background);
               if (!graphics.getClip().equals(clip)) {
                  graphics.setClip(clip);
               }
            } while(bounds.isEmpty());

            if (this.inOverlayManagingMode && overlay.isMovable()) {
               Color boundsColor;
               if (this.inOverlayResizingMode && this.currentManagedOverlay == overlay) {
                  boundsColor = MOVING_OVERLAY_RESIZING_COLOR;
               } else if (this.inOverlayDraggingMode && this.currentManagedOverlay == overlay) {
                  boundsColor = MOVING_OVERLAY_ACTIVE_COLOR;
               } else if (this.inOverlayDraggingMode && overlay.isDragTargetable() && this.currentManagedOverlay.isDragTargetable() && this.currentManagedOverlay.getBounds().intersects(bounds)) {
                  boundsColor = MOVING_OVERLAY_TARGET_COLOR;

                  assert this.currentManagedOverlay != overlay;

                  this.dragTargetOverlay = overlay;
               } else {
                  boundsColor = MOVING_OVERLAY_COLOR;
               }

               graphics.setColor(boundsColor);
               graphics.draw(bounds);
               graphics.setPaint(paint);
            }

            if (!this.client.isMenuOpen() && !this.client.isWidgetSelected() && bounds.contains(this.mousePosition)) {
               this.curHoveredOverlay = overlay;
               overlay.onMouseOver();
            }
         }
      }
   }

   public MouseEvent mousePressed(MouseEvent mouseEvent) {
      Point mousePoint = mouseEvent.getPoint();
      this.mousePosition.setLocation(mousePoint);
      if (!this.inOverlayManagingMode) {
         return mouseEvent;
      } else {
         this.currentManagedOverlay = this.lastHoveredOverlay;
         if (this.currentManagedOverlay != null && this.currentManagedOverlay.isMovable()) {
            if (SwingUtilities.isRightMouseButton(mouseEvent)) {
               if (this.currentManagedOverlay.isResettable()) {
                  this.overlayManager.resetOverlay(this.currentManagedOverlay);
               }
            } else {
               if (!SwingUtilities.isLeftMouseButton(mouseEvent)) {
                  return mouseEvent;
               }

               Point offset = new Point(mousePoint.x, mousePoint.y);
               offset.translate(-this.currentManagedOverlay.getBounds().x, -this.currentManagedOverlay.getBounds().y);
               this.overlayOffset.setLocation(offset);
               this.inOverlayResizingMode = this.currentManagedOverlay != null && this.currentManagedOverlay.isResizable() && this.clientUI.getCurrentCursor() != this.clientUI.getDefaultCursor();
               this.inOverlayDraggingMode = !this.inOverlayResizingMode;
               this.startedMovingOverlay = true;
               this.currentManagedBounds = new Rectangle(this.currentManagedOverlay.getBounds());
            }

            mouseEvent.consume();
            return mouseEvent;
         } else {
            return mouseEvent;
         }
      }
   }

   public MouseEvent mouseMoved(MouseEvent mouseEvent) {
      Point mousePoint = mouseEvent.getPoint();
      this.mousePosition.setLocation(mousePoint);
      if (!this.inOverlayManagingMode) {
         return mouseEvent;
      } else {
         if (!this.inOverlayResizingMode && !this.inOverlayDraggingMode) {
            this.currentManagedOverlay = this.lastHoveredOverlay;
         }

         if (this.currentManagedOverlay != null && this.currentManagedOverlay.isResizable()) {
            Rectangle toleranceRect = new Rectangle(this.currentManagedOverlay.getBounds());
            toleranceRect.grow(-5, -5);
            int outcode = toleranceRect.outcode(mouseEvent.getPoint());
            switch (outcode) {
               case 1:
                  this.clientUI.setCursor(Cursor.getPredefinedCursor(10));
                  break;
               case 2:
                  this.clientUI.setCursor(Cursor.getPredefinedCursor(8));
                  break;
               case 3:
                  this.clientUI.setCursor(Cursor.getPredefinedCursor(6));
                  break;
               case 4:
                  this.clientUI.setCursor(Cursor.getPredefinedCursor(11));
                  break;
               case 5:
               case 7:
               case 10:
               case 11:
               default:
                  this.clientUI.setCursor(this.clientUI.getDefaultCursor());
                  break;
               case 6:
                  this.clientUI.setCursor(Cursor.getPredefinedCursor(7));
                  break;
               case 8:
                  this.clientUI.setCursor(Cursor.getPredefinedCursor(9));
                  break;
               case 9:
                  this.clientUI.setCursor(Cursor.getPredefinedCursor(4));
                  break;
               case 12:
                  this.clientUI.setCursor(Cursor.getPredefinedCursor(5));
            }

            return mouseEvent;
         } else {
            this.clientUI.setCursor(this.clientUI.getDefaultCursor());
            return mouseEvent;
         }
      }
   }

   public MouseEvent mouseDragged(MouseEvent mouseEvent) {
      Point p = mouseEvent.getPoint();
      this.mousePosition.setLocation(p);
      if (!this.inOverlayManagingMode) {
         return mouseEvent;
      } else if (this.currentManagedOverlay == null) {
         return mouseEvent;
      } else {
         if (this.dragTargetOverlay != null && !this.currentManagedOverlay.getBounds().intersects(this.dragTargetOverlay.getBounds())) {
            this.dragTargetOverlay = null;
         }

         Rectangle canvasRect = new Rectangle(this.client.getRealDimensions());
         if (!canvasRect.contains(p)) {
            return mouseEvent;
         } else {
            if (this.inOverlayResizingMode) {
               int left = p.x - this.currentManagedBounds.x;
               int top = p.y - this.currentManagedBounds.y;
               int originalX = this.currentManagedBounds.x;
               int originalY = this.currentManagedBounds.y;
               int x = originalX;
               int y = originalY;
               int width = this.currentManagedBounds.width;
               int height = this.currentManagedBounds.height;
               switch (this.clientUI.getCurrentCursor().getType()) {
                  case 4:
                     x += left;
                     width -= left;
                     height = top;
                     break;
                  case 5:
                     width = left;
                     height = top;
                     break;
                  case 6:
                     x += left;
                     y += top;
                     width -= left;
                     height -= top;
                     break;
                  case 7:
                     y += top;
                     width = left;
                     height -= top;
                     break;
                  case 8:
                     y += top;
                     height -= top;
                     break;
                  case 9:
                     height = top;
                     break;
                  case 10:
                     x += left;
                     width -= left;
                     break;
                  case 11:
                     width = left;
               }

               int minOverlaySize = this.currentManagedOverlay.getMinimumSize();
               int widthOverflow = Math.max(0, minOverlaySize - width);
               int heightOverflow = Math.max(0, minOverlaySize - height);
               int dx = x - originalX;
               int dy = y - originalY;
               if (widthOverflow > 0) {
                  width = minOverlaySize;
                  if (dx > 0) {
                     x -= widthOverflow;
                  }
               }

               if (heightOverflow > 0) {
                  height = minOverlaySize;
                  if (dy > 0) {
                     y -= heightOverflow;
                  }
               }

               this.currentManagedBounds.setRect((double)x, (double)y, (double)width, (double)height);
               this.currentManagedOverlay.setPreferredSize(new Dimension(this.currentManagedBounds.width, this.currentManagedBounds.height));
               if (this.currentManagedOverlay.getPreferredLocation() != null) {
                  this.currentManagedOverlay.setPreferredLocation(this.currentManagedBounds.getLocation());
               }
            } else {
               if (!this.inOverlayDraggingMode) {
                  return mouseEvent;
               }

               Point overlayPosition = new Point(p);
               overlayPosition.translate(-this.overlayOffset.x, -this.overlayOffset.y);
               Rectangle overlayBounds = this.currentManagedOverlay.getBounds();
               overlayPosition = this.clampOverlayLocation(overlayPosition.x, overlayPosition.y, overlayBounds.width, overlayBounds.height, this.currentManagedOverlay);
               this.currentManagedOverlay.setPreferredPosition((OverlayPosition)null);
               this.currentManagedOverlay.setPreferredLocation(overlayPosition);
            }

            if (this.startedMovingOverlay) {
               this.overlayManager.rebuildOverlayLayers();
               this.startedMovingOverlay = false;
            }

            return mouseEvent;
         }
      }
   }

   public MouseEvent mouseReleased(MouseEvent mouseEvent) {
      Point mousePoint = mouseEvent.getPoint();
      this.mousePosition.setLocation(mousePoint);
      if (this.inOverlayManagingMode && this.currentManagedOverlay != null && (this.inOverlayDraggingMode || this.inOverlayResizingMode)) {
         if (this.dragTargetOverlay != null && this.dragTargetOverlay.onDrag(this.currentManagedOverlay)) {
            mouseEvent.consume();
            this.resetOverlayManagementMode();
            return mouseEvent;
         } else {
            if (this.currentManagedOverlay.isSnappable() && this.inOverlayDraggingMode) {
               OverlayBounds snapCorners = this.emptySnapCorners.translated(-SNAP_CORNER_SIZE.width, -SNAP_CORNER_SIZE.height);
               Iterator var4 = snapCorners.getBounds().iterator();

               while(var4.hasNext()) {
                  Rectangle snapCorner = (Rectangle)var4.next();
                  if (snapCorner.contains(mousePoint)) {
                     OverlayPosition position = snapCorners.fromBounds(snapCorner);
                     if (position == this.getCorrectedOverlayPosition(this.currentManagedOverlay)) {
                        position = null;
                     }

                     this.currentManagedOverlay.setPreferredPosition(position);
                     this.currentManagedOverlay.setPreferredLocation((Point)null);
                     this.currentManagedOverlay.revalidate();
                     break;
                  }
               }
            }

            if (this.inOverlayDraggingMode && this.currentManagedOverlay instanceof WidgetOverlay && !this.dragWarn) {
               this.dragWarn = true;
               ChatMessageManager var10000 = this.chatMessageManager;
               QueuedMessage.QueuedMessageBuilder var10001 = QueuedMessage.builder().type(ChatMessageType.CONSOLE);
               String var10002 = String.valueOf(this.runeLiteConfig.dragHotkey());
               var10000.queue(var10001.runeLiteFormattedMessage("You've repositioned one of the in-game interfaces. Hold " + var10002 + " and drag to reposition the interface again, or " + String.valueOf(this.runeLiteConfig.dragHotkey()) + " and right-click to reset.").build());
            }

            this.overlayManager.saveOverlay(this.currentManagedOverlay);
            this.resetOverlayManagementMode();
            mouseEvent.consume();
            return mouseEvent;
         }
      } else {
         return mouseEvent;
      }
   }

   private Rectangle clipBounds(OverlayLayer layer) {
      return this.isResizeable || layer != OverlayLayer.ABOVE_SCENE && layer != OverlayLayer.UNDER_WIDGETS ? new Rectangle(0, 0, this.client.getCanvasWidth(), this.client.getCanvasHeight()) : new Rectangle(this.client.getViewportXOffset(), this.client.getViewportYOffset(), this.client.getViewportWidth(), this.client.getViewportHeight());
   }

   private void safeRender(Overlay overlay, Graphics2D graphics, Point point) {
      OverlayPosition position = overlay.getPosition();
      if (position != OverlayPosition.DYNAMIC && position != OverlayPosition.DETACHED) {
         if (position == OverlayPosition.TOOLTIP) {
            graphics.setFont(this.tooltipFont);
         } else {
            graphics.setFont(this.interfaceFont);
         }
      } else {
         graphics.setFont(this.font);
      }

      graphics.translate(point.x, point.y);
      overlay.getBounds().setLocation(point);
      ExternalPluginMdc.ScopedMdc pluginMdc = ExternalPluginMdc.open((Object)overlay);

      Dimension overlayDimension;
      label66: {
         try {
            overlayDimension = overlay.render(graphics);
            break label66;
         } catch (Exception var11) {
            Exception ex = var11;
            log.warn(DEDUPLICATE, "Error during overlay rendering", ex);
         } finally {
            pluginMdc.close();
         }

         return;
      }

      if (overlayDimension != null) {
         overlay.getBounds().setSize(overlayDimension);
      } else {
         overlay.getBounds().setSize(0, 0);
      }

   }

   private OverlayPosition getCorrectedOverlayPosition(Overlay overlay) {
      OverlayPosition overlayPosition = overlay.getPosition();
      if (overlay.getPreferredPosition() != null) {
         overlayPosition = overlay.getPreferredPosition();
      }

      if (!this.isResizeable) {
         switch (overlayPosition) {
            case CANVAS_TOP_RIGHT:
               overlayPosition = OverlayPosition.TOP_RIGHT;
               break;
            case ABOVE_CHATBOX_RIGHT:
               overlayPosition = OverlayPosition.BOTTOM_RIGHT;
         }
      }

      return overlayPosition;
   }

   private void resetOverlayManagementMode() {
      this.inOverlayResizingMode = false;
      this.inOverlayDraggingMode = false;
      this.currentManagedOverlay = null;
      this.dragTargetOverlay = null;
      this.currentManagedBounds = null;
      this.clientUI.setCursor(this.clientUI.getDefaultCursor());
   }

   private boolean shouldInvalidateBounds() {
      Widget chatbox = this.client.getWidget(10616866);
      boolean resizeableChanged = this.isResizeable != this.client.isResized();
      boolean changed = false;
      if (resizeableChanged) {
         this.isResizeable = this.client.isResized();
         changed = true;
      }

      boolean chatboxBoundsChanged = chatbox == null || !chatbox.getBounds().equals(this.chatboxBounds);
      if (chatboxBoundsChanged) {
         this.chatboxBounds = chatbox != null ? chatbox.getBounds() : new Rectangle();
         changed = true;
      }

      boolean chatboxHiddenChanged = this.chatboxHidden != (chatbox == null || chatbox.isHidden());
      if (chatboxHiddenChanged) {
         this.chatboxHidden = chatbox == null || chatbox.isHidden();
         changed = true;
      }

      Widget viewportWidget = this.getViewportLayer();
      Rectangle viewport = viewportWidget != null ? viewportWidget.getBounds() : new Rectangle();
      boolean viewportChanged = !viewport.equals(this.viewportBounds);
      if (viewportChanged) {
         this.viewportBounds = viewport;
         changed = true;
      }

      return changed;
   }

   private Widget getViewportLayer() {
      if (this.client.isResized()) {
         return this.client.getVarbitValue(4607) == 1 ? this.client.getWidget(10747919) : this.client.getWidget(10551311);
      } else {
         return this.client.getWidget(35913761);
      }
   }

   private OverlayBounds buildSnapCorners() {
      Point topLeftPoint = new Point(this.viewportBounds.x + 5, this.viewportBounds.y + 20);
      Point topCenterPoint = new Point(this.viewportBounds.x + this.viewportBounds.width / 2, this.viewportBounds.y + 5);
      Point topRightPoint = new Point(this.viewportBounds.x + this.viewportBounds.width - 5, topCenterPoint.y);
      Point bottomLeftPoint = new Point(topLeftPoint.x, this.viewportBounds.y + this.viewportBounds.height - 5);
      Point bottomRightPoint = new Point(topRightPoint.x, bottomLeftPoint.y);
      if (this.isResizeable && this.chatboxHidden) {
         bottomLeftPoint.y += this.chatboxBounds.height;
      }

      Point rightChatboxPoint = this.isResizeable ? new Point(this.viewportBounds.x + this.chatboxBounds.width - 5, bottomLeftPoint.y) : bottomRightPoint;
      Point canvasTopRightPoint = this.isResizeable ? new Point((int)this.client.getRealDimensions().getWidth(), 0) : topRightPoint;
      return new OverlayBounds(new Rectangle(topLeftPoint, SNAP_CORNER_SIZE), new Rectangle(topCenterPoint, SNAP_CORNER_SIZE), new Rectangle(topRightPoint, SNAP_CORNER_SIZE), new Rectangle(bottomLeftPoint, SNAP_CORNER_SIZE), new Rectangle(bottomRightPoint, SNAP_CORNER_SIZE), new Rectangle(rightChatboxPoint, SNAP_CORNER_SIZE), new Rectangle(canvasTopRightPoint, SNAP_CORNER_SIZE));
   }

   private Point clampOverlayLocation(int overlayX, int overlayY, int overlayWidth, int overlayHeight, Overlay overlay) {
      Rectangle parentBounds = overlay.getParentBounds();
      int px;
      int py;
      int pw;
      int ph;
      if (parentBounds != null && !parentBounds.isEmpty()) {
         px = parentBounds.x;
         py = parentBounds.y;
         pw = parentBounds.width;
         ph = parentBounds.height;
      } else {
         Dimension dim = this.client.getRealDimensions();
         py = 0;
         px = 0;
         pw = dim.width;
         ph = dim.height;
      }

      return new Point(Ints.constrainToRange(overlayX, px, Math.max(px, px + pw - overlayWidth)), Ints.constrainToRange(overlayY, py, Math.max(py, py + ph - overlayHeight)));
   }

   static {
      MOVING_OVERLAY_TARGET_COLOR = Color.RED;
      MOVING_OVERLAY_RESIZING_COLOR = new Color(255, 0, 255, 200);
   }
}
