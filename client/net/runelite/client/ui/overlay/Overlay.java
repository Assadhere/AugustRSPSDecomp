package net.runelite.client.ui.overlay;

import com.google.common.base.Preconditions;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import javax.annotation.Nullable;
import net.runelite.api.MenuAction;
import net.runelite.api.MenuEntry;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.ui.overlay.components.LayoutableRenderableEntity;

public abstract class Overlay implements LayoutableRenderableEntity {
   public static final float PRIORITY_LOW = 0.0F;
   public static final float PRIORITY_DEFAULT = 0.25F;
   public static final float PRIORITY_MED = 0.5F;
   public static final float PRIORITY_HIGH = 0.75F;
   public static final float PRIORITY_HIGHEST = 1.0F;
   @Nullable
   private final Plugin plugin;
   private Point preferredLocation;
   private Dimension preferredSize;
   private OverlayPosition preferredPosition;
   private Rectangle bounds = new Rectangle();
   private OverlayPosition position;
   private float priority;
   private OverlayLayer layer;
   private final List<Integer> drawHooks;
   private final List<OverlayMenuEntry> menuEntries;
   private boolean resizable;
   private int minimumSize;
   private boolean resettable;
   private boolean dragTargetable;
   private boolean movable;
   private boolean snappable;

   protected Overlay() {
      this.position = OverlayPosition.TOP_LEFT;
      this.priority = 0.25F;
      this.layer = OverlayLayer.UNDER_WIDGETS;
      this.drawHooks = new ArrayList();
      this.menuEntries = new ArrayList();
      this.minimumSize = 32;
      this.resettable = true;
      this.movable = true;
      this.snappable = true;
      this.plugin = null;
   }

   protected Overlay(@Nullable Plugin plugin) {
      this.position = OverlayPosition.TOP_LEFT;
      this.priority = 0.25F;
      this.layer = OverlayLayer.UNDER_WIDGETS;
      this.drawHooks = new ArrayList();
      this.menuEntries = new ArrayList();
      this.minimumSize = 32;
      this.resettable = true;
      this.movable = true;
      this.snappable = true;
      this.plugin = plugin;
   }

   public void setPriority(float priority) {
      this.priority = priority;
   }

   public void setPriority(OverlayPriority overlayPriority) {
      switch (overlayPriority) {
         case LOW:
            this.priority = 0.0F;
            break;
         case NONE:
            this.priority = 0.25F;
            break;
         case MED:
            this.priority = 0.5F;
            break;
         case HIGH:
            this.priority = 0.75F;
            break;
         case HIGHEST:
            this.priority = 1.0F;
      }

   }

   public String getName() {
      return this.getClass().getSimpleName();
   }

   protected void drawAfterInterface(int interfaceId) {
      this.drawHooks.add(interfaceId << 16 | '\uffff');
   }

   protected void drawAfterLayer(int groupId, int childId) {
      Preconditions.checkArgument(groupId >= 0 && groupId <= 65535, "groupId outside of valid range");
      Preconditions.checkArgument(childId >= 0 && childId <= 65535, "childId outside of valid range");
      this.drawHooks.add(groupId << 16 | childId);
   }

   /** @deprecated */
   @Deprecated
   protected void drawAfterLayer(WidgetInfo layer) {
      this.drawHooks.add(layer.getId());
   }

   protected void drawAfterLayer(int component) {
      this.drawHooks.add(component);
   }

   public void onMouseOver() {
   }

   public boolean onDrag(Overlay other) {
      return false;
   }

   @Nullable
   public Rectangle getParentBounds() {
      return null;
   }

   public void revalidate() {
   }

   public void setPosition(OverlayPosition position) {
      this.position = position;
      switch (position) {
         case TOOLTIP:
         case DYNAMIC:
            this.movable = false;
            this.snappable = false;
            break;
         case DETACHED:
            this.movable = true;
            this.snappable = false;
            break;
         default:
            this.movable = true;
            this.snappable = true;
      }

   }

   public OverlayMenuEntry addMenuEntry(MenuAction action, String option, String target) {
      return this.addMenuEntry(action, option, target, (Consumer)null);
   }

   public OverlayMenuEntry addMenuEntry(MenuAction action, String option, String target, Consumer<MenuEntry> callback) {
      OverlayMenuEntry menuEntry = new OverlayMenuEntry(action, option, target);
      menuEntry.callback = callback;
      this.menuEntries.add(menuEntry);
      return menuEntry;
   }

   public void removeMenuEntry(MenuAction action, String option, String target) {
      this.menuEntries.remove(new OverlayMenuEntry(action, option, target));
   }

   @Nullable
   public Plugin getPlugin() {
      return this.plugin;
   }

   public Point getPreferredLocation() {
      return this.preferredLocation;
   }

   public Dimension getPreferredSize() {
      return this.preferredSize;
   }

   public OverlayPosition getPreferredPosition() {
      return this.preferredPosition;
   }

   public Rectangle getBounds() {
      return this.bounds;
   }

   public OverlayPosition getPosition() {
      return this.position;
   }

   public float getPriority() {
      return this.priority;
   }

   public OverlayLayer getLayer() {
      return this.layer;
   }

   public List<Integer> getDrawHooks() {
      return this.drawHooks;
   }

   public List<OverlayMenuEntry> getMenuEntries() {
      return this.menuEntries;
   }

   public boolean isResizable() {
      return this.resizable;
   }

   public int getMinimumSize() {
      return this.minimumSize;
   }

   public boolean isResettable() {
      return this.resettable;
   }

   public boolean isDragTargetable() {
      return this.dragTargetable;
   }

   public boolean isMovable() {
      return this.movable;
   }

   public boolean isSnappable() {
      return this.snappable;
   }

   public void setPreferredLocation(Point preferredLocation) {
      this.preferredLocation = preferredLocation;
   }

   public void setPreferredSize(Dimension preferredSize) {
      this.preferredSize = preferredSize;
   }

   public void setPreferredPosition(OverlayPosition preferredPosition) {
      this.preferredPosition = preferredPosition;
   }

   public void setBounds(Rectangle bounds) {
      this.bounds = bounds;
   }

   public void setLayer(OverlayLayer layer) {
      this.layer = layer;
   }

   public void setResizable(boolean resizable) {
      this.resizable = resizable;
   }

   public void setMinimumSize(int minimumSize) {
      this.minimumSize = minimumSize;
   }

   public void setResettable(boolean resettable) {
      this.resettable = resettable;
   }

   protected void setDragTargetable(boolean dragTargetable) {
      this.dragTargetable = dragTargetable;
   }

   protected void setMovable(boolean movable) {
      this.movable = movable;
   }

   protected void setSnappable(boolean snappable) {
      this.snappable = snappable;
   }
}
