package net.runelite.client.ui.overlay;

import com.google.common.base.MoreObjects;
import com.google.common.collect.ArrayListMultimap;
import java.awt.Dimension;
import java.awt.Point;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;
import javax.inject.Inject;
import javax.inject.Singleton;
import net.runelite.api.widgets.WidgetItem;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.config.RuneLiteConfig;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.events.ProfileChanged;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
public class OverlayManager {
   private static final Logger log = LoggerFactory.getLogger(OverlayManager.class);
   public static final String OPTION_CONFIGURE = "Configure";
   private static final String OVERLAY_CONFIG_PREFERRED_LOCATION = "_preferredLocation";
   private static final String OVERLAY_CONFIG_PREFERRED_POSITION = "_preferredPosition";
   private static final String OVERLAY_CONFIG_PREFERRED_SIZE = "_preferredSize";
   private static final String RUNELITE_CONFIG_GROUP_NAME = ((ConfigGroup)RuneLiteConfig.class.getAnnotation(ConfigGroup.class)).value();
   static final Comparator<Overlay> OVERLAY_COMPARATOR = (a, b) -> {
      OverlayPosition aPos = (OverlayPosition)MoreObjects.firstNonNull(a.getPreferredPosition(), a.getPosition());
      OverlayPosition bPos = (OverlayPosition)MoreObjects.firstNonNull(b.getPreferredPosition(), b.getPosition());
      if (aPos != bPos) {
         return aPos.compareTo(bPos);
      } else {
         return aPos != OverlayPosition.DYNAMIC && aPos != OverlayPosition.DETACHED ? Float.compare(b.getPriority(), a.getPriority()) : Float.compare(a.getPriority(), b.getPriority());
      }
   };
   private final List<Overlay> overlays = new ArrayList();
   private Collection<WidgetItem> widgetItems = Collections.emptyList();
   private ArrayListMultimap<Object, Overlay> overlayMap = ArrayListMultimap.create();
   private final ConfigManager configManager;
   private final RuneLiteConfig runeLiteConfig;

   @Inject
   private OverlayManager(ConfigManager configManager, RuneLiteConfig runeLiteConfig) {
      this.configManager = configManager;
      this.runeLiteConfig = runeLiteConfig;
   }

   @Subscribe
   public void onConfigChanged(ConfigChanged event) {
      if ("runelite".equals(event.getGroup()) && "overlayBackgroundColor".equals(event.getKey())) {
         this.overlays.forEach(this::updateOverlayConfig);
      }
   }

   @Subscribe
   public void onProfileChanged(ProfileChanged event) {
      synchronized(this) {
         this.overlays.forEach((o) -> {
            this.loadOverlay(o);
            o.revalidate();
         });
      }

      this.rebuildOverlayLayers();
   }

   Collection<Overlay> getLayer(OverlayLayer layer) {
      return Collections.unmodifiableCollection(this.overlayMap.get(layer));
   }

   Collection<Overlay> getForInterface(int interfaceId) {
      return Collections.unmodifiableCollection(this.overlayMap.get(interfaceId << 16 | '\uffff'));
   }

   Collection<Overlay> getForLayer(int layerId) {
      return Collections.unmodifiableCollection(this.overlayMap.get(layerId));
   }

   public synchronized boolean add(Overlay overlay) {
      if (this.overlays.contains(overlay)) {
         return false;
      } else {
         this.overlays.add(overlay);
         this.loadOverlay(overlay);
         this.updateOverlayConfig(overlay);
         if (overlay instanceof WidgetItemOverlay) {
            ((WidgetItemOverlay)overlay).setOverlayManager(this);
         }

         this.rebuildOverlayLayers();
         return true;
      }
   }

   public synchronized boolean remove(Overlay overlay) {
      boolean remove = this.overlays.remove(overlay);
      if (remove) {
         this.rebuildOverlayLayers();
      }

      return remove;
   }

   public synchronized boolean removeIf(Predicate<Overlay> filter) {
      boolean removeIf = this.overlays.removeIf(filter);
      if (removeIf) {
         this.rebuildOverlayLayers();
      }

      return removeIf;
   }

   public synchronized boolean anyMatch(Predicate<Overlay> filter) {
      return this.overlays.stream().anyMatch(filter);
   }

   public synchronized void clear() {
      this.overlays.clear();
      this.rebuildOverlayLayers();
   }

   public synchronized void saveOverlay(Overlay overlay) {
      this.saveOverlayPosition(overlay);
      this.saveOverlaySize(overlay);
      this.saveOverlayLocation(overlay);
      this.rebuildOverlayLayers();
   }

   public synchronized void resetOverlay(Overlay overlay) {
      overlay.setPreferredPosition((OverlayPosition)null);
      overlay.setPreferredSize((Dimension)null);
      overlay.setPreferredLocation((Point)null);
      this.saveOverlay(overlay);
      overlay.revalidate();
   }

   synchronized void rebuildOverlayLayers() {
      ArrayListMultimap<Object, Overlay> overlayMap = ArrayListMultimap.create();
      Iterator var2 = this.overlays.iterator();

      while(var2.hasNext()) {
         Overlay overlay = (Overlay)var2.next();
         OverlayLayer layer = overlay.getLayer();
         if (overlay.getPreferredLocation() != null && overlay.getPreferredPosition() == null && layer == OverlayLayer.UNDER_WIDGETS && !(overlay instanceof WidgetOverlay)) {
            layer = OverlayLayer.ABOVE_WIDGETS;
         }

         switch (layer) {
            case ABOVE_SCENE:
            case UNDER_WIDGETS:
            case ALWAYS_ON_TOP:
               overlayMap.put(layer, overlay);
               break;
            case ABOVE_WIDGETS:
               overlayMap.put(35979263, overlay);
               overlayMap.put(10616831, overlay);
               overlayMap.put(10813439, overlay);
         }

         Iterator var5 = overlay.getDrawHooks().iterator();

         while(var5.hasNext()) {
            int drawHook = (Integer)var5.next();
            overlayMap.put(drawHook, overlay);
         }
      }

      var2 = overlayMap.keys().iterator();

      while(var2.hasNext()) {
         Object key = var2.next();
         overlayMap.get(key).sort(OVERLAY_COMPARATOR);
      }

      this.overlayMap = overlayMap;
   }

   private void loadOverlay(Overlay overlay) {
      Point location = this.loadOverlayLocation(overlay);
      Dimension size = this.loadOverlaySize(overlay);
      OverlayPosition position = this.loadOverlayPosition(overlay);
      if (overlay.isMovable()) {
         overlay.setPreferredLocation(location);
      } else if (location != null) {
         log.info("Resetting preferred location of non-movable overlay {} (class {})", overlay.getName(), overlay.getClass().getName());
         overlay.setPreferredLocation((Point)null);
         this.saveOverlayLocation(overlay);
      }

      overlay.setPreferredSize(size);
      if (overlay.isSnappable()) {
         overlay.setPreferredPosition(position);
      } else if (position != null) {
         log.info("Resetting preferred position of non-snappable overlay {} (class {})", overlay.getName(), overlay.getClass().getName());
         overlay.setPreferredPosition((OverlayPosition)null);
         this.saveOverlayPosition(overlay);
      }

   }

   private void updateOverlayConfig(Overlay overlay) {
      if (overlay instanceof OverlayPanel) {
         ((OverlayPanel)overlay).setPreferredColor(this.runeLiteConfig.overlayBackgroundColor());
      }

   }

   private void saveOverlayLocation(Overlay overlay) {
      String key = overlay.getName() + "_preferredLocation";
      if (overlay.getPreferredLocation() != null) {
         this.configManager.setConfiguration(RUNELITE_CONFIG_GROUP_NAME, key, (Object)overlay.getPreferredLocation());
      } else {
         this.configManager.unsetConfiguration(RUNELITE_CONFIG_GROUP_NAME, key);
      }

   }

   private void saveOverlaySize(Overlay overlay) {
      String key = overlay.getName() + "_preferredSize";
      if (overlay.getPreferredSize() != null) {
         this.configManager.setConfiguration(RUNELITE_CONFIG_GROUP_NAME, key, (Object)overlay.getPreferredSize());
      } else {
         this.configManager.unsetConfiguration(RUNELITE_CONFIG_GROUP_NAME, key);
      }

   }

   private void saveOverlayPosition(Overlay overlay) {
      String key = overlay.getName() + "_preferredPosition";
      if (overlay.getPreferredPosition() != null) {
         this.configManager.setConfiguration(RUNELITE_CONFIG_GROUP_NAME, key, (Object)overlay.getPreferredPosition());
      } else {
         this.configManager.unsetConfiguration(RUNELITE_CONFIG_GROUP_NAME, key);
      }

   }

   private Point loadOverlayLocation(Overlay overlay) {
      String key = overlay.getName() + "_preferredLocation";
      return (Point)this.configManager.getConfiguration(RUNELITE_CONFIG_GROUP_NAME, key, (Type)Point.class);
   }

   private Dimension loadOverlaySize(Overlay overlay) {
      String key = overlay.getName() + "_preferredSize";
      return (Dimension)this.configManager.getConfiguration(RUNELITE_CONFIG_GROUP_NAME, key, (Type)Dimension.class);
   }

   private OverlayPosition loadOverlayPosition(Overlay overlay) {
      String locationKey = overlay.getName() + "_preferredPosition";
      return (OverlayPosition)this.configManager.getConfiguration(RUNELITE_CONFIG_GROUP_NAME, locationKey, (Type)OverlayPosition.class);
   }

   List<Overlay> getOverlays() {
      return this.overlays;
   }

   public Collection<WidgetItem> getWidgetItems() {
      return this.widgetItems;
   }

   public void setWidgetItems(Collection<WidgetItem> widgetItems) {
      this.widgetItems = widgetItems;
   }
}
