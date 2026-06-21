package net.runelite.client.plugins.inventorytags;

import com.google.gson.Gson;
import com.google.inject.Provides;
import java.awt.Color;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.inject.Inject;
import javax.swing.SwingUtilities;
import net.runelite.api.Client;
import net.runelite.api.Item;
import net.runelite.api.ItemContainer;
import net.runelite.api.Menu;
import net.runelite.api.MenuAction;
import net.runelite.api.MenuEntry;
import net.runelite.api.events.MenuOpened;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetUtil;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.components.colorpicker.ColorPickerManager;
import net.runelite.client.ui.components.colorpicker.RuneliteColorPicker;
import net.runelite.client.ui.overlay.OverlayManager;
import net.runelite.client.util.ColorUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@PluginDescriptor(
   name = "Inventory Tags",
   description = "Add the ability to tag items in your inventory",
   tags = {"highlight", "items", "overlay", "tagging"},
   enabledByDefault = false
)
public class InventoryTagsPlugin extends Plugin {
   private static final Logger log = LoggerFactory.getLogger(InventoryTagsPlugin.class);
   private static final String ITEM_KEY_PREFIX = "item_";
   private static final String TAG_KEY_PREFIX = "tag_";
   @Inject
   private Client client;
   @Inject
   private ConfigManager configManager;
   @Inject
   private InventoryTagsOverlay overlay;
   @Inject
   private OverlayManager overlayManager;
   @Inject
   private Gson gson;
   @Inject
   private ColorPickerManager colorPickerManager;

   @Provides
   InventoryTagsConfig provideConfig(ConfigManager configManager) {
      return (InventoryTagsConfig)configManager.getConfig(InventoryTagsConfig.class);
   }

   protected void startUp() {
      this.overlayManager.add(this.overlay);
      this.convertConfig();
   }

   protected void shutDown() {
      this.overlayManager.remove(this.overlay);
   }

   Tag getTag(int itemId) {
      String tag = this.configManager.getConfiguration("inventorytags", "tag_" + itemId);
      return tag != null && !tag.isEmpty() ? (Tag)this.gson.fromJson(tag, Tag.class) : null;
   }

   void setTag(int itemId, Tag tag) {
      String json = this.gson.toJson(tag);
      this.configManager.setConfiguration("inventorytags", "tag_" + itemId, json);
   }

   void unsetTag(int itemId) {
      this.configManager.unsetConfiguration("inventorytags", "tag_" + itemId);
   }

   private void convertConfig() {
      String migrated = this.configManager.getConfiguration("inventorytags", "migrated");
      if ("1".equals(migrated)) {
         int removed = 0;
         List<String> keys = this.configManager.getConfigurationKeys("inventorytags.item_");
         Iterator var4 = keys.iterator();

         while(var4.hasNext()) {
            String key = (String)var4.next();
            String[] str = key.split("\\.", 2);
            if (str.length == 2) {
               this.configManager.unsetConfiguration(str[0], str[1]);
               ++removed;
            }
         }

         log.debug("Removed {} old tags", removed);
         this.configManager.setConfiguration("inventorytags", "migrated", "2");
      }
   }

   @Subscribe
   public void onConfigChanged(ConfigChanged configChanged) {
      if (configChanged.getGroup().equals("inventorytags")) {
         this.overlay.invalidateCache();
      }

   }

   @Subscribe
   public void onMenuOpened(MenuOpened event) {
      if (this.client.isKeyPressed(81)) {
         MenuEntry[] entries = event.getMenuEntries();

         label47:
         for(int idx = entries.length - 1; idx >= 0; --idx) {
            MenuEntry entry = entries[idx];
            Widget w = entry.getWidget();
            if (w != null && WidgetUtil.componentToInterface(w.getId()) == 149 && "Examine".equals(entry.getOption()) && entry.getIdentifier() == 10) {
               int itemId = w.getItemId();
               Tag tag = this.getTag(itemId);
               MenuEntry parent = this.client.createMenuEntry(idx).setOption("Inventory tag").setTarget(entry.getTarget()).setType(MenuAction.RUNELITE);
               Menu submenu = parent.createSubMenu();
               Set<Color> invEquipmentColors = new HashSet();
               invEquipmentColors.addAll(this.getColorsFromItemContainer(93));
               invEquipmentColors.addAll(this.getColorsFromItemContainer(94));
               Iterator var11 = invEquipmentColors.iterator();

               while(true) {
                  Color color;
                  do {
                     if (!var11.hasNext()) {
                        submenu.createMenuEntry(0).setOption("Pick").setType(MenuAction.RUNELITE).onClick((e) -> {
                           Color color = tag == null ? Color.WHITE : tag.color;
                           SwingUtilities.invokeLater(() -> {
                              RuneliteColorPicker colorPicker = this.colorPickerManager.create(this.client, color, "Inventory Tag", true);
                              colorPicker.setOnClose((c) -> {
                                 Tag t = new Tag();
                                 t.color = c;
                                 this.setTag(itemId, t);
                              });
                              colorPicker.setVisible(true);
                           });
                        });
                        if (tag != null) {
                           submenu.createMenuEntry(0).setOption("Reset").setType(MenuAction.RUNELITE).onClick((e) -> {
                              this.unsetTag(itemId);
                           });
                        }
                        continue label47;
                     }

                     color = (Color)var11.next();
                  } while(tag != null && tag.color.equals(color));

                  submenu.createMenuEntry(0).setOption(ColorUtil.prependColorTag("Color", color)).setType(MenuAction.RUNELITE).onClick((e) -> {
                     Tag t = new Tag();
                     t.color = color;
                     this.setTag(itemId, t);
                  });
               }
            }
         }

      }
   }

   private List<Color> getColorsFromItemContainer(int inventoryID) {
      List<Color> colors = new ArrayList();
      ItemContainer container = this.client.getItemContainer(inventoryID);
      if (container != null) {
         Item[] var4 = container.getItems();
         int var5 = var4.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            Item item = var4[var6];
            Tag tag = this.getTag(item.getId());
            if (tag != null && tag.color != null && !colors.contains(tag.color)) {
               colors.add(tag.color);
            }
         }
      }

      return colors;
   }
}
