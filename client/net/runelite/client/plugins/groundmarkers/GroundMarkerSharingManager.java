package net.runelite.client.plugins.groundmarkers;

import com.google.common.base.Strings;
import com.google.common.util.concurrent.Runnables;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import java.awt.Toolkit;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.inject.Inject;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.MenuEntry;
import net.runelite.client.chat.ChatMessageManager;
import net.runelite.client.chat.QueuedMessage;
import net.runelite.client.game.chatbox.ChatboxPanelManager;
import net.runelite.client.menus.MenuManager;
import net.runelite.client.menus.WidgetMenuOption;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class GroundMarkerSharingManager {
   private static final Logger log = LoggerFactory.getLogger(GroundMarkerSharingManager.class);
   private static final WidgetMenuOption EXPORT_MARKERS_OPTION = new WidgetMenuOption("Export", "Ground Markers", new int[]{10485815, 58654773});
   private static final WidgetMenuOption IMPORT_MARKERS_OPTION = new WidgetMenuOption("Import", "Ground Markers", new int[]{10485815, 58654773});
   private static final WidgetMenuOption CLEAR_MARKERS_OPTION = new WidgetMenuOption("Clear", "Ground Markers", new int[]{10485815, 58654773});
   private final GroundMarkerPlugin plugin;
   private final Client client;
   private final MenuManager menuManager;
   private final ChatMessageManager chatMessageManager;
   private final ChatboxPanelManager chatboxPanelManager;
   private final Gson gson;

   @Inject
   private GroundMarkerSharingManager(GroundMarkerPlugin plugin, Client client, MenuManager menuManager, ChatMessageManager chatMessageManager, ChatboxPanelManager chatboxPanelManager, Gson gson) {
      this.plugin = plugin;
      this.client = client;
      this.menuManager = menuManager;
      this.chatMessageManager = chatMessageManager;
      this.chatboxPanelManager = chatboxPanelManager;
      this.gson = gson;
   }

   void addImportExportMenuOptions() {
      this.menuManager.addManagedCustomMenu(EXPORT_MARKERS_OPTION, this::exportGroundMarkers);
      this.menuManager.addManagedCustomMenu(IMPORT_MARKERS_OPTION, this::promptForImport);
   }

   void addClearMenuOption() {
      this.menuManager.addManagedCustomMenu(CLEAR_MARKERS_OPTION, this::promptForClear);
   }

   void removeMenuOptions() {
      this.menuManager.removeManagedCustomMenu(EXPORT_MARKERS_OPTION);
      this.menuManager.removeManagedCustomMenu(IMPORT_MARKERS_OPTION);
      this.menuManager.removeManagedCustomMenu(CLEAR_MARKERS_OPTION);
   }

   private void exportGroundMarkers(MenuEntry menuEntry) {
      int[] regions = this.client.getMapRegions();
      if (regions != null) {
         List<GroundMarkerPoint> activePoints = (List)Arrays.stream(regions).mapToObj((regionId) -> {
            return this.plugin.getPoints(regionId).stream();
         }).flatMap(Function.identity()).collect(Collectors.toList());
         if (activePoints.isEmpty()) {
            this.sendChatMessage("You have no ground markers to export.");
         } else {
            String exportDump = this.gson.toJson(activePoints);
            log.debug("Exported ground markers: {}", exportDump);
            Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(exportDump), (ClipboardOwner)null);
            this.sendChatMessage(activePoints.size() + " ground markers were copied to your clipboard.");
         }
      }
   }

   private void promptForImport(MenuEntry menuEntry) {
      String clipboardText;
      try {
         clipboardText = Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor).toString();
      } catch (UnsupportedFlavorException | IOException var6) {
         Exception ex = var6;
         this.sendChatMessage("Unable to read system clipboard.");
         log.warn("error reading clipboard", ex);
         return;
      }

      log.debug("Clipboard contents: {}", clipboardText);
      if (Strings.isNullOrEmpty(clipboardText)) {
         this.sendChatMessage("You do not have any ground markers copied in your clipboard.");
      } else {
         List importPoints;
         try {
            importPoints = (List)this.gson.fromJson(clipboardText, (new TypeToken<List<GroundMarkerPoint>>() {
            }).getType());
         } catch (JsonSyntaxException var5) {
            JsonSyntaxException e = var5;
            log.debug("Malformed JSON for clipboard import", e);
            this.sendChatMessage("You do not have any ground markers copied in your clipboard.");
            return;
         }

         if (importPoints.isEmpty()) {
            this.sendChatMessage("You do not have any ground markers copied in your clipboard.");
         } else {
            this.chatboxPanelManager.openTextMenuInput("Are you sure you want to import " + importPoints.size() + " ground markers?").option("Yes", () -> {
               this.importGroundMarkers(importPoints);
            }).option("No", Runnables.doNothing()).build();
         }
      }
   }

   private void importGroundMarkers(Collection<GroundMarkerPoint> importPoints) {
      Map<Integer, List<GroundMarkerPoint>> regionGroupedPoints = (Map)importPoints.stream().collect(Collectors.groupingBy(GroundMarkerPoint::getRegionId));
      regionGroupedPoints.forEach((regionId, groupedPoints) -> {
         log.debug("Importing {} points to region {}", groupedPoints.size(), regionId);
         Collection<GroundMarkerPoint> regionPoints = this.plugin.getPoints(regionId);
         List<GroundMarkerPoint> mergedList = new ArrayList(regionPoints.size() + groupedPoints.size());
         mergedList.addAll(regionPoints);
         Iterator var5 = groupedPoints.iterator();

         while(var5.hasNext()) {
            GroundMarkerPoint point = (GroundMarkerPoint)var5.next();
            if (!mergedList.contains(point)) {
               mergedList.add(point);
            }
         }

         this.plugin.savePoints(regionId, mergedList);
      });
      log.debug("Reloading points after import");
      this.plugin.loadPoints();
      this.sendChatMessage(importPoints.size() + " ground markers were imported from the clipboard.");
   }

   private void promptForClear(MenuEntry entry) {
      int[] regions = this.client.getMapRegions();
      if (regions != null) {
         long numActivePoints = Arrays.stream(regions).mapToLong((regionId) -> {
            return (long)this.plugin.getPoints(regionId).size();
         }).sum();
         if (numActivePoints == 0L) {
            this.sendChatMessage("You have no ground markers to clear.");
         } else {
            this.chatboxPanelManager.openTextMenuInput("Are you sure you want to clear the<br>" + numActivePoints + " currently loaded ground markers?").option("Yes", () -> {
               int[] var4 = regions;
               int var5 = regions.length;

               for(int var6 = 0; var6 < var5; ++var6) {
                  int regionId = var4[var6];
                  this.plugin.savePoints(regionId, (Collection)null);
               }

               this.plugin.loadPoints();
               this.sendChatMessage("" + numActivePoints + " ground marker" + (numActivePoints == 1L ? " was cleared." : "s were cleared."));
            }).option("No", Runnables.doNothing()).build();
         }
      }
   }

   private void sendChatMessage(String message) {
      this.chatMessageManager.queue(QueuedMessage.builder().type(ChatMessageType.CONSOLE).runeLiteFormattedMessage(message).build());
   }
}
