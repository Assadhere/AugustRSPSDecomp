package net.runelite.client.ui.overlay.infobox;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.ComparisonChain;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import javax.inject.Inject;
import javax.inject.Singleton;
import net.runelite.api.Client;
import net.runelite.api.MenuAction;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.config.RuneLiteConfig;
import net.runelite.client.eventbus.EventBus;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.events.InfoBoxMenuClicked;
import net.runelite.client.events.ProfileChanged;
import net.runelite.client.ui.overlay.OverlayManager;
import net.runelite.client.ui.overlay.OverlayMenuEntry;
import net.runelite.client.ui.overlay.components.ComponentOrientation;
import net.runelite.client.ui.overlay.tooltip.TooltipManager;
import net.runelite.client.util.AsyncBufferedImage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
public class InfoBoxManager {
   private static final Logger log = LoggerFactory.getLogger(InfoBoxManager.class);
   private static final String INFOBOXLAYER_KEY = "infoboxlayer";
   private static final String INFOBOXOVERLAY_KEY = "infoboxoverlay";
   private static final String INFOBOXOVERLAY_ORIENTATION_PREFIX = "orient_";
   private static final String DEFAULT_LAYER = "InfoBoxOverlay";
   private static final String DETACH = "Detach";
   private static final String FLIP = "Flip";
   private static final String DELETE = "Delete";
   private static final OverlayMenuEntry DETACH_ME;
   private static final OverlayMenuEntry FLIP_ME;
   private static final OverlayMenuEntry DELETE_ME;
   private final Map<String, InfoBoxOverlay> layers = new ConcurrentHashMap();
   private final RuneLiteConfig runeLiteConfig;
   private final TooltipManager tooltipManager;
   private final Client client;
   private final EventBus eventBus;
   private final OverlayManager overlayManager;
   private final ConfigManager configManager;

   @Inject
   private InfoBoxManager(RuneLiteConfig runeLiteConfig, TooltipManager tooltipManager, Client client, EventBus eventBus, OverlayManager overlayManager, ConfigManager configManager) {
      this.runeLiteConfig = runeLiteConfig;
      this.tooltipManager = tooltipManager;
      this.client = client;
      this.eventBus = eventBus;
      this.overlayManager = overlayManager;
      this.configManager = configManager;
      eventBus.register(this);
   }

   @Subscribe
   public void onConfigChanged(ConfigChanged event) {
      if (event.getGroup().equals("runelite") && event.getKey().equals("infoBoxSize")) {
         this.layers.values().forEach((l) -> {
            l.getInfoBoxes().forEach(this::updateInfoBoxImage);
         });
      }

   }

   @Subscribe
   public synchronized void onProfileChanged(ProfileChanged profileChanged) {
      List<InfoBox> infoBoxes = this.getInfoBoxes();
      infoBoxes.forEach(this::removeInfoBox);
      (new ArrayList(this.layers.values())).forEach(this::removeOverlay);
      infoBoxes.forEach(this::addInfoBox);
   }

   @Subscribe
   public void onInfoBoxMenuClicked(InfoBoxMenuClicked event) {
      if ("Detach".equals(event.getEntry().getOption())) {
         this.splitInfobox(event.getInfoBox().getName() + "_" + System.currentTimeMillis(), event.getInfoBox());
      } else {
         InfoBoxOverlay source;
         if ("Flip".equals(event.getEntry().getOption())) {
            source = (InfoBoxOverlay)this.layers.get(event.getInfoBox().layer);
            ComponentOrientation newOrientation = source.flip();
            this.setOrientation(source.getName(), newOrientation);
         } else if ("Delete".equals(event.getEntry().getOption())) {
            source = (InfoBoxOverlay)this.layers.get(event.getInfoBox().layer);
            InfoBoxOverlay dest = (InfoBoxOverlay)this.layers.computeIfAbsent("InfoBoxOverlay", this::makeOverlay);
            if (source != dest) {
               this.mergeInfoBoxes(source, dest);
            }
         }
      }

   }

   public void addInfoBox(InfoBox infoBox) {
      Preconditions.checkNotNull(infoBox);
      log.debug("Adding InfoBox {}", infoBox);
      this.updateInfoBoxImage(infoBox);
      String layerName = this.getLayer(infoBox);
      infoBox.layer = layerName;
      InfoBoxOverlay overlay = (InfoBoxOverlay)this.layers.computeIfAbsent(layerName, this::makeOverlay);
      List<OverlayMenuEntry> menuEntries = infoBox.getMenuEntries();
      menuEntries.add(DETACH_ME);
      menuEntries.add(FLIP_ME);
      if (!layerName.equals("InfoBoxOverlay")) {
         menuEntries.add(DELETE_ME);
      }

      synchronized(this) {
         int idx = findInsertionIndex(overlay.getInfoBoxes(), infoBox, (b1, b2) -> {
            return ComparisonChain.start().compare(b1.getPriority(), b2.getPriority()).compare(b1.getPlugin().getName(), b2.getPlugin().getName()).result();
         });
         overlay.getInfoBoxes().add(idx, infoBox);
      }

      BufferedImage image = infoBox.getImage();
      if (image instanceof AsyncBufferedImage) {
         AsyncBufferedImage abi = (AsyncBufferedImage)image;
         abi.onLoaded(() -> {
            this.updateInfoBoxImage(infoBox);
         });
      }

   }

   public synchronized void removeInfoBox(InfoBox infoBox) {
      if (infoBox != null && infoBox.layer != null) {
         if (((InfoBoxOverlay)this.layers.get(infoBox.layer)).getInfoBoxes().remove(infoBox)) {
            log.debug("Removed InfoBox {}", infoBox);
         }

         infoBox.layer = null;
         infoBox.getMenuEntries().remove(DETACH_ME);
         infoBox.getMenuEntries().remove(FLIP_ME);
         infoBox.getMenuEntries().remove(DELETE_ME);
      }
   }

   public synchronized void removeIf(Predicate<InfoBox> filter) {
      Iterator var2 = this.layers.values().iterator();

      while(var2.hasNext()) {
         InfoBoxOverlay overlay = (InfoBoxOverlay)var2.next();
         if (overlay.getInfoBoxes().removeIf(filter)) {
            log.debug("Removed InfoBoxes for filter {} from {}", filter, overlay);
         }
      }

   }

   public List<InfoBox> getInfoBoxes() {
      return (List)this.layers.values().stream().map(InfoBoxOverlay::getInfoBoxes).flatMap(Collection::stream).collect(Collectors.toList());
   }

   public synchronized void cull() {
      this.layers.values().forEach((l) -> {
         l.getInfoBoxes().removeIf(InfoBox::cull);
      });
   }

   public void updateInfoBoxImage(InfoBox infoBox) {
      if (infoBox.getImage() != null) {
         BufferedImage image = infoBox.getImage();
         BufferedImage resultImage = image;
         double width = (double)image.getWidth((ImageObserver)null);
         double height = (double)image.getHeight((ImageObserver)null);
         double size = (double)Math.max(2, this.runeLiteConfig.infoBoxSize());
         if (size < width || size < height) {
            double scalex = size / width;
            double scaley = size / height;
            if (scalex == 1.0 && scaley == 1.0) {
               return;
            }

            double scale = Math.min(scalex, scaley);
            int newWidth = (int)(width * scale);
            int newHeight = (int)(height * scale);
            BufferedImage scaledImage = new BufferedImage(newWidth, newHeight, 2);
            Graphics g = scaledImage.createGraphics();
            ((Graphics)g).drawImage(image, 0, 0, newWidth, newHeight, (ImageObserver)null);
            ((Graphics)g).dispose();
            resultImage = scaledImage;
         }

         infoBox.setScaledImage(resultImage);
      }
   }

   private InfoBoxOverlay makeOverlay(String name) {
      ComponentOrientation orientation = this.getOrientation(name);
      if (orientation == null) {
         if (name.equals("InfoBoxOverlay")) {
            orientation = this.runeLiteConfig.infoBoxVertical() ? ComponentOrientation.VERTICAL : ComponentOrientation.HORIZONTAL;
            this.setOrientation(name, orientation);
         } else {
            orientation = ComponentOrientation.HORIZONTAL;
         }
      }

      InfoBoxOverlay infoBoxOverlay = new InfoBoxOverlay(this, this.tooltipManager, this.client, this.runeLiteConfig, this.eventBus, name, orientation);
      this.overlayManager.add(infoBoxOverlay);
      this.eventBus.register(infoBoxOverlay);
      return infoBoxOverlay;
   }

   private void removeOverlay(InfoBoxOverlay overlay) {
      this.eventBus.unregister((Object)overlay);
      this.overlayManager.remove(overlay);
      this.layers.remove(overlay.getName());
   }

   private synchronized void splitInfobox(String newLayer, InfoBox infoBox) {
      String oldLayer = infoBox.layer;
      InfoBoxOverlay oldOverlay = (InfoBoxOverlay)this.layers.get(infoBox.layer);
      Collection<InfoBox> filtered = (Collection)oldOverlay.getInfoBoxes().stream().filter((ix) -> {
         return ix.getName().equals(infoBox.getName());
      }).collect(Collectors.toList());
      oldOverlay.getInfoBoxes().removeAll(filtered);
      if (oldOverlay.getInfoBoxes().isEmpty()) {
         log.debug("Deleted layer: {}", oldOverlay.getName());
         this.unsetOrientation(oldOverlay.getName());
         this.removeOverlay(oldOverlay);
      }

      InfoBoxOverlay newOverlay = (InfoBoxOverlay)this.layers.computeIfAbsent(newLayer, this::makeOverlay);
      newOverlay.getInfoBoxes().addAll(filtered);
      Iterator var7 = filtered.iterator();

      while(var7.hasNext()) {
         InfoBox i = (InfoBox)var7.next();
         this.setLayer(i, newLayer);
         i.layer = newLayer;
         if (!i.getMenuEntries().contains(DELETE_ME)) {
            i.getMenuEntries().add(DELETE_ME);
         }
      }

      log.debug("Moving infobox named {} (layer {}) to layer {}: {} boxes", new Object[]{infoBox.getName(), oldLayer, newLayer, filtered.size()});
   }

   public synchronized void mergeInfoBoxes(InfoBoxOverlay source, InfoBoxOverlay dest) {
      Collection<InfoBox> infoBoxesToMove = source.getInfoBoxes();
      boolean isDefault = dest.getName().equals("InfoBoxOverlay");
      log.debug("Merging InfoBoxes from {} into {} ({} boxes)", new Object[]{source.getName(), dest.getName(), infoBoxesToMove.size()});
      Iterator var5 = infoBoxesToMove.iterator();

      while(var5.hasNext()) {
         InfoBox infoBox = (InfoBox)var5.next();
         this.setLayer(infoBox, dest.getName());
         infoBox.layer = dest.getName();
         if (isDefault) {
            infoBox.getMenuEntries().remove(DELETE_ME);
         }
      }

      dest.getInfoBoxes().addAll(infoBoxesToMove);
      source.getInfoBoxes().clear();
      this.unsetOrientation(source.getName());
      this.removeOverlay(source);
      log.debug("Deleted layer: {}", source.getName());
   }

   private String getLayer(InfoBox infoBox) {
      String name = this.configManager.getConfiguration("infoboxlayer", infoBox.getName());
      return Strings.isNullOrEmpty(name) ? "InfoBoxOverlay" : name;
   }

   private void setLayer(InfoBox infoBox, String layer) {
      if (layer.equals("InfoBoxOverlay")) {
         this.configManager.unsetConfiguration("infoboxlayer", infoBox.getName());
      } else {
         this.configManager.setConfiguration("infoboxlayer", infoBox.getName(), layer);
      }

   }

   ComponentOrientation getOrientation(String name) {
      return (ComponentOrientation)this.configManager.getConfiguration("infoboxoverlay", "orient_" + name, (Type)ComponentOrientation.class);
   }

   void setOrientation(String name, ComponentOrientation orientation) {
      this.configManager.setConfiguration("infoboxoverlay", "orient_" + name, (Object)orientation);
   }

   void unsetOrientation(String name) {
      this.configManager.unsetConfiguration("infoboxoverlay", "orient_" + name);
   }

   private static <T> int findInsertionIndex(List<? extends T> list, T key, Comparator<? super T> c) {
      int idx = Collections.binarySearch(list, key, c);
      if (idx < 0) {
         return -idx - 1;
      } else {
         for(int i = idx + 1; i < list.size(); ++i) {
            T cur = list.get(i);
            int cmp = c.compare(cur, key);
            if (cmp > 0) {
               return i;
            }
         }

         return list.size();
      }
   }

   static {
      DETACH_ME = new OverlayMenuEntry(MenuAction.RUNELITE_INFOBOX, "Detach", "InfoBox");
      FLIP_ME = new OverlayMenuEntry(MenuAction.RUNELITE_INFOBOX, "Flip", "InfoBox Group");
      DELETE_ME = new OverlayMenuEntry(MenuAction.RUNELITE_INFOBOX, "Delete", "InfoBox Group");
   }
}
