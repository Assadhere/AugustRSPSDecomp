package net.runelite.client.plugins.grounditems;

import com.google.common.base.MoreObjects;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Table;
import com.google.inject.Provides;
import java.awt.Color;
import java.awt.Rectangle;
import java.lang.reflect.Type;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ScheduledExecutorService;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.inject.Inject;
import javax.swing.SwingUtilities;
import net.runelite.api.Client;
import net.runelite.api.ItemComposition;
import net.runelite.api.ItemLayer;
import net.runelite.api.Menu;
import net.runelite.api.MenuAction;
import net.runelite.api.MenuEntry;
import net.runelite.api.Tile;
import net.runelite.api.TileItem;
import net.runelite.api.WorldView;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.ClientTick;
import net.runelite.api.events.FocusChanged;
import net.runelite.api.events.ItemDespawned;
import net.runelite.api.events.ItemQuantityChanged;
import net.runelite.api.events.ItemSpawned;
import net.runelite.api.events.MenuEntryAdded;
import net.runelite.api.events.WorldViewUnloaded;
import net.runelite.client.Notifier;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.events.ProfileChanged;
import net.runelite.client.game.ItemManager;
import net.runelite.client.input.KeyManager;
import net.runelite.client.input.MouseManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.grounditems.config.HighlightTier;
import net.runelite.client.plugins.grounditems.config.ItemHighlightMode;
import net.runelite.client.plugins.grounditems.config.MenuHighlightMode;
import net.runelite.client.plugins.grounditems.config.OwnershipFilterMode;
import net.runelite.client.ui.components.colorpicker.ColorPickerManager;
import net.runelite.client.ui.components.colorpicker.RuneliteColorPicker;
import net.runelite.client.ui.overlay.OverlayManager;
import net.runelite.client.util.ColorUtil;
import net.runelite.client.util.QuantityFormatter;
import net.runelite.client.util.RSTimeUnit;
import net.runelite.client.util.Text;

@PluginDescriptor(
   name = "Ground Items",
   description = "Highlight ground items and/or show price information",
   tags = {"grand", "exchange", "high", "alchemy", "prices", "highlight", "overlay", "lootbeam"}
)
public class GroundItemsPlugin extends Plugin {
   private static final String HIGHLIGHT_COLOR_PREFIX = "highlight_";
   private static final int COINS = 995;
   private Map.Entry<Rectangle, GroundItem> textBoxBounds;
   private Map.Entry<Rectangle, GroundItem> hiddenBoxBounds;
   private Map.Entry<Rectangle, GroundItem> highlightBoxBounds;
   private boolean hotKeyPressed;
   private boolean hideAll;
   private ItemList highlightedItems = new ItemList(Collections.emptyList());
   private ItemList hiddenItems = new ItemList(Collections.emptyList());
   @Inject
   private GroundItemHotkeyListener hotkeyListener;
   @Inject
   private GroundItemMouseAdapter mouseAdapter;
   @Inject
   private MouseManager mouseManager;
   @Inject
   private KeyManager keyManager;
   @Inject
   private Client client;
   @Inject
   private ClientThread clientThread;
   @Inject
   private ItemManager itemManager;
   @Inject
   private OverlayManager overlayManager;
   @Inject
   private GroundItemsConfig config;
   @Inject
   private GroundItemsOverlay overlay;
   @Inject
   private Notifier notifier;
   @Inject
   private ScheduledExecutorService executor;
   @Inject
   private ConfigManager configManager;
   @Inject
   private ColorPickerManager colorPickerManager;
   private final Table<WorldPoint, Integer, GroundItem> collectedGroundItems = HashBasedTable.create();
   private List<PriceHighlight> priceChecks = ImmutableList.of();
   private final Map<WorldPoint, Lootbeam> lootbeams = new HashMap();
   private static final int NONE = 0;
   private static final int HIGHLIGHTED = 1;
   private static final int HIDDEN = 2;

   @Provides
   GroundItemsConfig provideConfig(ConfigManager configManager) {
      return (GroundItemsConfig)configManager.getConfig(GroundItemsConfig.class);
   }

   protected void startUp() {
      this.overlayManager.add(this.overlay);
      this.mouseManager.registerMouseListener(this.mouseAdapter);
      this.keyManager.registerKeyListener(this.hotkeyListener);
      this.migrate();
      this.executor.execute(this::reset);
   }

   protected void shutDown() {
      this.overlayManager.remove(this.overlay);
      this.mouseManager.unregisterMouseListener(this.mouseAdapter);
      this.keyManager.unregisterKeyListener(this.hotkeyListener);
      this.collectedGroundItems.clear();
      this.clientThread.invokeLater(this::removeAllLootbeams);
   }

   @Subscribe
   public void onConfigChanged(ConfigChanged event) {
      if (event.getGroup().equals("grounditems")) {
         this.executor.execute(this::reset);
      }

   }

   @Subscribe
   public void onProfileChanged(ProfileChanged profileChanged) {
      this.migrate();
   }

   private void migrate() {
      Boolean onlyShowOwnItems = (Boolean)this.configManager.getConfiguration("grounditems", "onlyShowLoot", (Type)Boolean.class);
      if (onlyShowOwnItems != null) {
         if (onlyShowOwnItems) {
            this.configManager.setConfiguration("grounditems", "ownershipFilterMode", (Object)OwnershipFilterMode.DROPS);
         }

         this.configManager.unsetConfiguration("grounditems", "onlyShowLoot");
      }

   }

   @Subscribe
   public void onWorldViewUnloaded(WorldViewUnloaded event) {
      WorldView wv = event.getWorldView();
      this.collectedGroundItems.values().removeIf((g) -> {
         return g.getItemLayer().getWorldView() == wv;
      });
      this.lootbeams.values().removeIf((l) -> {
         return l.getWorldView() == wv.getId();
      });
   }

   @Subscribe
   public void onItemSpawned(ItemSpawned itemSpawned) {
      TileItem item = itemSpawned.getItem();
      Tile tile = itemSpawned.getTile();
      ItemLayer layer = tile.getItemLayer();
      GroundItem groundItem = this.buildGroundItem(layer, item);
      GroundItem existing = (GroundItem)this.collectedGroundItems.get(tile.getWorldLocation(), item.getId());
      if (existing != null) {
         existing.setQuantity(existing.getQuantity() + groundItem.getQuantity());
         existing.reset();
      } else {
         this.collectedGroundItems.put(tile.getWorldLocation(), item.getId(), groundItem);
      }

      if (this.shouldDisplayItem(this.config.ownershipFilterMode(), groundItem.getOwnership(), this.client.getVarbitValue(1777))) {
         this.notifyHighlightedItem(groundItem);
      }

      this.handleLootbeam(tile.getWorldLocation());
   }

   void updateItemColor(GroundItem item) {
      if (item.color == null) {
         Color highlighted = this.getHighlighted(item);
         boolean hidden = this.isHidden(item);
         item.highlighted = highlighted != null;
         item.hidden = hidden;
         item.color = this.getItemColor(highlighted, hidden);
      }
   }

   @Subscribe
   public void onItemDespawned(ItemDespawned itemDespawned) {
      TileItem item = itemDespawned.getItem();
      Tile tile = itemDespawned.getTile();
      GroundItem groundItem = (GroundItem)this.collectedGroundItems.get(tile.getWorldLocation(), item.getId());
      if (groundItem != null) {
         if (groundItem.getQuantity() <= item.getQuantity()) {
            this.collectedGroundItems.remove(tile.getWorldLocation(), item.getId());
         } else {
            groundItem.setQuantity(groundItem.getQuantity() - item.getQuantity());
            groundItem.setSpawnTime((Instant)null);
            groundItem.reset();
         }

         this.handleLootbeam(tile.getWorldLocation());
      }
   }

   @Subscribe
   public void onItemQuantityChanged(ItemQuantityChanged itemQuantityChanged) {
      TileItem item = itemQuantityChanged.getItem();
      Tile tile = itemQuantityChanged.getTile();
      int oldQuantity = itemQuantityChanged.getOldQuantity();
      int newQuantity = itemQuantityChanged.getNewQuantity();
      int diff = newQuantity - oldQuantity;
      GroundItem groundItem = (GroundItem)this.collectedGroundItems.get(tile.getWorldLocation(), item.getId());
      if (groundItem != null) {
         groundItem.setQuantity(groundItem.getQuantity() + diff);
         groundItem.reset();
      }

      this.handleLootbeam(tile.getWorldLocation());
   }

   @Subscribe
   public void onClientTick(ClientTick event) {
      if (this.config.collapseEntries()) {
         MenuEntry[] menuEntries = this.client.getMenuEntries();
         List<MenuEntryWithCount> newEntries = new ArrayList(menuEntries.length);

         label43:
         for(int i = menuEntries.length - 1; i >= 0; --i) {
            MenuEntry menuEntry = menuEntries[i];
            MenuAction menuType = menuEntry.getType();
            if (menuType == MenuAction.GROUND_ITEM_FIRST_OPTION || menuType == MenuAction.GROUND_ITEM_SECOND_OPTION || menuType == MenuAction.GROUND_ITEM_THIRD_OPTION || menuType == MenuAction.GROUND_ITEM_FOURTH_OPTION || menuType == MenuAction.GROUND_ITEM_FIFTH_OPTION || menuType == MenuAction.EXAMINE_ITEM_GROUND) {
               Iterator var7 = newEntries.iterator();

               while(var7.hasNext()) {
                  MenuEntryWithCount entryWCount = (MenuEntryWithCount)var7.next();
                  if (entryWCount.getEntry().equals(menuEntry)) {
                     entryWCount.increment();
                     continue label43;
                  }
               }
            }

            newEntries.add(new MenuEntryWithCount(menuEntry));
         }

         Collections.reverse(newEntries);
         this.client.setMenuEntries((MenuEntry[])newEntries.stream().map((e) -> {
            MenuEntry entry = e.getEntry();
            int count = e.getCount();
            if (count > 1) {
               String var10001 = entry.getTarget();
               entry.setTarget(var10001 + " x " + count);
            }

            return entry;
         }).toArray((x$0) -> {
            return new MenuEntry[x$0];
         }));
      }
   }

   private GroundItem buildGroundItem(ItemLayer layer, TileItem item) {
      int itemId = item.getId();
      ItemComposition itemComposition = this.itemManager.getItemComposition(itemId);
      int realItemId = itemComposition.getNote() != -1 ? itemComposition.getLinkedNoteId() : itemId;
      int alchPrice = itemComposition.getHaPrice();
      int despawnTime = item.getDespawnTime() - this.client.getTickCount();
      int visibleTime = item.getVisibleTime() - this.client.getTickCount();
      GroundItem groundItem = GroundItem.builder().id(itemId).itemId(realItemId).quantity(item.getQuantity()).itemLayer(layer).name(itemComposition.getName()).haPrice(alchPrice).tradeable(itemComposition.isTradeable()).ownership(item.getOwnership()).isPrivate(item.isPrivate()).spawnTime(Instant.now()).stackable(itemComposition.isStackable()).despawnTime(Duration.of((long)despawnTime, RSTimeUnit.GAME_TICKS)).visibleTime(Duration.of((long)visibleTime, RSTimeUnit.GAME_TICKS)).build();
      if (realItemId == 995) {
         groundItem.setHaPrice(1);
         groundItem.setGePrice(1);
      } else {
         groundItem.setGePrice(this.itemManager.getItemPrice(realItemId));
      }

      return groundItem;
   }

   private void reset() {
      this.highlightedItems = new ItemList(Text.fromCSV(this.config.getHighlightItems()));
      this.hiddenItems = new ItemList(Text.fromCSV(this.config.getHiddenItems()));
      ImmutableList.Builder<PriceHighlight> priceCheckBuilder = ImmutableList.builder();
      if (this.config.insaneValuePrice() > 0) {
         priceCheckBuilder.add(new PriceHighlight(this.config.insaneValuePrice(), this.config.insaneValueColor()));
      }

      if (this.config.highValuePrice() > 0) {
         priceCheckBuilder.add(new PriceHighlight(this.config.highValuePrice(), this.config.highValueColor()));
      }

      if (this.config.mediumValuePrice() > 0) {
         priceCheckBuilder.add(new PriceHighlight(this.config.mediumValuePrice(), this.config.mediumValueColor()));
      }

      if (this.config.lowValuePrice() > 0) {
         priceCheckBuilder.add(new PriceHighlight(this.config.lowValuePrice(), this.config.lowValueColor()));
      }

      this.priceChecks = priceCheckBuilder.build();
      this.clientThread.invokeLater(() -> {
         this.collectedGroundItems.values().forEach(GroundItem::reset);
      });
      this.clientThread.invokeLater(this::handleLootbeams);
   }

   @Subscribe
   public void onMenuEntryAdded(MenuEntryAdded event) {
      MenuAction type = MenuAction.of(event.getType());
      boolean hotKeyPressed = this.client.isKeyPressed(81);
      int itemId;
      if (type != MenuAction.GROUND_ITEM_FIRST_OPTION && type != MenuAction.GROUND_ITEM_SECOND_OPTION && type != MenuAction.GROUND_ITEM_THIRD_OPTION && type != MenuAction.GROUND_ITEM_FOURTH_OPTION && type != MenuAction.GROUND_ITEM_FIFTH_OPTION && type != MenuAction.WIDGET_TARGET_ON_GROUND_ITEM) {
         if (hotKeyPressed && type == MenuAction.EXAMINE_ITEM_GROUND) {
            MenuEntry parent = this.client.createMenuEntry(-1).setOption("Color").setTarget(event.getTarget()).setType(MenuAction.RUNELITE);
            Menu submenu = parent.createSubMenu();
            itemId = event.getIdentifier();
            Color color = this.getItemColor(itemId);
            if (color != null) {
               submenu.createMenuEntry(-1).setOption("Reset").setType(MenuAction.RUNELITE).onClick((e) -> {
                  this.unsetItemColor(itemId);
               });
            }

            submenu.createMenuEntry(-1).setOption("Pick").setType(MenuAction.RUNELITE).onClick((e) -> {
               SwingUtilities.invokeLater(() -> {
                  RuneliteColorPicker colorPicker = this.colorPickerManager.create(this.client, color != null ? color : Color.decode("#FFFFFF"), "Item color", true);
                  colorPicker.setOnClose((c) -> {
                     this.setItemColor(itemId, c);
                  });
                  colorPicker.setVisible(true);
               });
            });
            List<Color> colors = (List)Stream.concat(this.collectedGroundItems.values().stream().map(GroundItem::getColor).filter(Objects::nonNull), Stream.of(Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW, Color.MAGENTA)).distinct().limit(5L).collect(Collectors.toList());
            colors.stream().filter((c) -> {
               return !c.equals(color);
            }).forEach((c) -> {
               submenu.createMenuEntry(-1).setOption(ColorUtil.prependColorTag("Color", c)).setType(MenuAction.RUNELITE).onClick((e) -> {
                  this.setItemColor(itemId, c);
               });
            });
         }
      } else {
         int itemId = event.getIdentifier();
         int sceneX = event.getActionParam0();
         itemId = event.getActionParam1();
         MenuEntry lastEntry = event.getMenuEntry();
         WorldView wv = this.client.getWorldView(lastEntry.getWorldViewId());
         WorldPoint worldPoint = WorldPoint.fromScene(wv, sceneX, itemId, wv.getPlane());
         GroundItem groundItem = (GroundItem)this.collectedGroundItems.get(worldPoint, itemId);
         this.updateItemColor(groundItem);
         int quantity = groundItem.getQuantity();
         boolean canBeRecolored = groundItem.highlighted || groundItem.hidden && this.config.recolorMenuHiddenItems();
         String var10001;
         if ((this.config.itemHighlightMode() == ItemHighlightMode.MENU || this.config.itemHighlightMode() == ItemHighlightMode.BOTH) && canBeRecolored && !groundItem.color.equals(this.config.defaultColor())) {
            MenuHighlightMode mode = this.config.menuHighlightMode();
            if (mode == MenuHighlightMode.BOTH || mode == MenuHighlightMode.OPTION) {
               lastEntry.setOption(ColorUtil.prependColorTag(lastEntry.getOption(), groundItem.color));
            }

            if (mode == MenuHighlightMode.BOTH || mode == MenuHighlightMode.NAME) {
               String target = lastEntry.getTarget();
               int i = target.lastIndexOf(62);
               var10001 = target.substring(0, i - 11);
               lastEntry.setTarget(var10001 + ColorUtil.colorTag(groundItem.color) + target.substring(i + 1));
            }
         }

         if (this.config.showMenuItemQuantities() && groundItem.isStackable() && quantity > 1) {
            var10001 = lastEntry.getTarget();
            lastEntry.setTarget(var10001 + " (" + quantity + ")");
         }

         if (groundItem.hidden && !groundItem.highlighted && this.config.deprioritizeHiddenItems()) {
            lastEntry.setDeprioritized(true);
         }
      }

   }

   void updateList(String item, boolean hiddenList) {
      List<String> hiddenItemSet = new ArrayList(Text.fromCSV(this.config.getHiddenItems()));
      List<String> highlightedItemSet = new ArrayList(Text.fromCSV(this.config.getHighlightItems()));
      if (hiddenList) {
         Objects.requireNonNull(item);
         highlightedItemSet.removeIf(item::equalsIgnoreCase);
      } else {
         Objects.requireNonNull(item);
         hiddenItemSet.removeIf(item::equalsIgnoreCase);
      }

      List<String> items = hiddenList ? hiddenItemSet : highlightedItemSet;
      Objects.requireNonNull(item);
      if (!items.removeIf(item::equalsIgnoreCase)) {
         items.add(item);
      }

      this.config.setHiddenItems(Text.toCSV(hiddenItemSet));
      this.config.setHighlightedItem(Text.toCSV(highlightedItemSet));
   }

   private Color getHighlighted(GroundItem groundItem) {
      Color itemColor = this.getItemColor(groundItem.getItemId());
      int hiddenOrHighlighted = this.isHiddenOrHighlighted(groundItem);
      if (hiddenOrHighlighted == 1) {
         return itemColor != null ? itemColor : this.config.highlightedColor();
      } else if (hiddenOrHighlighted == 2) {
         return null;
      } else if (itemColor != null) {
         return itemColor;
      } else {
         int price = this.getValueByMode(groundItem.getGePrice(), groundItem.getHaPrice());
         Iterator var5 = this.priceChecks.iterator();

         PriceHighlight highlight;
         do {
            if (!var5.hasNext()) {
               return null;
            }

            highlight = (PriceHighlight)var5.next();
         } while(price <= highlight.getPrice());

         return highlight.getColor();
      }
   }

   private boolean isHidden(GroundItem groundItem) {
      int hiddenOrHighlighted = this.isHiddenOrHighlighted(groundItem);
      boolean canBeHidden = groundItem.getGePrice() > 0 || groundItem.isTradeable() || !this.config.dontHideUntradeables();
      boolean underGe = groundItem.getGePrice() < this.config.getHideUnderValue();
      boolean underHa = groundItem.getHaPrice() < this.config.getHideUnderValue();
      return hiddenOrHighlighted == 2 || hiddenOrHighlighted != 1 && canBeHidden && underGe && underHa;
   }

   private int isHiddenOrHighlighted(GroundItem item) {
      int hl = this.highlightedItems.matches(item);
      if (hl == 2) {
         return 1;
      } else {
         int hi = this.hiddenItems.matches(item);
         if (hi == 2) {
            return 2;
         } else if (hl == 1) {
            return 1;
         } else {
            return hi == 1 ? 2 : 0;
         }
      }
   }

   private Color getItemColor(Color highlighted, boolean hidden) {
      if (highlighted != null) {
         return highlighted;
      } else {
         return hidden ? this.config.hiddenColor() : this.config.defaultColor();
      }
   }

   @Subscribe
   public void onFocusChanged(FocusChanged focusChanged) {
      if (!focusChanged.isFocused()) {
         this.setHotKeyPressed(false);
      }

   }

   private void notifyHighlightedItem(GroundItem item) {
      int hiddenOrHighlighted = this.isHiddenOrHighlighted(item);
      boolean shouldNotifyHighlighted = this.config.notifyHighlightedDrops() && hiddenOrHighlighted == 1;
      boolean shouldNotifyTier = this.config.notifyTier() != HighlightTier.OFF && this.getValueByMode(item.getGePrice(), item.getHaPrice()) > this.config.notifyTier().getValueFromTier(this.config) && hiddenOrHighlighted != 2;
      String dropType;
      if (shouldNotifyHighlighted) {
         dropType = "highlighted";
      } else {
         if (!shouldNotifyTier) {
            return;
         }

         dropType = "valuable";
      }

      StringBuilder notificationStringBuilder = (new StringBuilder()).append("You received a ").append(dropType).append(" drop: ").append(item.getName());
      if (item.getQuantity() > 1) {
         notificationStringBuilder.append(" (").append(QuantityFormatter.quantityToStackSize((long)item.getQuantity())).append(')');
      }

      this.notifier.notify(notificationStringBuilder.toString());
   }

   private int getValueByMode(int gePrice, int haPrice) {
      switch (this.config.valueCalculationMode()) {
         case GE:
            return gePrice;
         case HA:
            return haPrice;
         default:
            return Math.max(gePrice, haPrice);
      }
   }

   private void handleLootbeam(WorldPoint worldPoint) {
      if (!this.config.showLootbeamForHighlighted() && this.config.showLootbeamTier() == HighlightTier.OFF) {
         this.removeLootbeam(worldPoint);
      } else {
         int highestPrice = -1;
         GroundItem highestItem = null;
         Collection<GroundItem> groundItems = this.collectedGroundItems.row(worldPoint).values();
         OwnershipFilterMode ownershipFilterMode = this.config.ownershipFilterMode();
         int accountType = this.client.getVarbitValue(1777);
         Iterator var7 = groundItems.iterator();

         while(var7.hasNext()) {
            GroundItem groundItem = (GroundItem)var7.next();
            if (this.shouldDisplayItem(ownershipFilterMode, groundItem.getOwnership(), accountType)) {
               int hiddenOrHighlight = this.isHiddenOrHighlighted(groundItem);
               if (this.config.showLootbeamForHighlighted() && hiddenOrHighlight == 1) {
                  this.addLootbeam(worldPoint, (Color)MoreObjects.firstNonNull(this.getItemColor(groundItem.getItemId()), this.config.highlightedColor()));
                  return;
               }

               if (hiddenOrHighlight != 2) {
                  int itemPrice = this.getValueByMode(groundItem.getGePrice(), groundItem.getHaPrice());
                  if (itemPrice > highestPrice) {
                     highestPrice = itemPrice;
                     highestItem = groundItem;
                  }
               }
            }
         }

         if (highestItem != null && this.config.showLootbeamTier() != HighlightTier.OFF) {
            var7 = this.priceChecks.iterator();

            while(var7.hasNext()) {
               PriceHighlight highlight = (PriceHighlight)var7.next();
               if (highestPrice > highlight.getPrice() && highestPrice > this.config.showLootbeamTier().getValueFromTier(this.config)) {
                  this.addLootbeam(worldPoint, (Color)MoreObjects.firstNonNull(this.getItemColor(highestItem.getItemId()), highlight.color));
                  return;
               }
            }
         }

         this.removeLootbeam(worldPoint);
      }
   }

   private void handleLootbeams() {
      Iterator var1 = this.collectedGroundItems.rowKeySet().iterator();

      while(var1.hasNext()) {
         WorldPoint worldPoint = (WorldPoint)var1.next();
         this.handleLootbeam(worldPoint);
      }

   }

   private void removeAllLootbeams() {
      Iterator var1 = this.lootbeams.values().iterator();

      while(var1.hasNext()) {
         Lootbeam lootbeam = (Lootbeam)var1.next();
         lootbeam.remove();
      }

      this.lootbeams.clear();
   }

   private void addLootbeam(WorldPoint worldPoint, Color color) {
      Lootbeam lootbeam = (Lootbeam)this.lootbeams.get(worldPoint);
      if (lootbeam == null) {
         lootbeam = new Lootbeam(this.client, this.clientThread, worldPoint, color, this.config.lootbeamStyle());
         this.lootbeams.put(worldPoint, lootbeam);
      } else {
         lootbeam.setColor(color);
         lootbeam.setStyle(this.config.lootbeamStyle());
      }

   }

   private void removeLootbeam(WorldPoint worldPoint) {
      Lootbeam lootbeam = (Lootbeam)this.lootbeams.remove(worldPoint);
      if (lootbeam != null) {
         lootbeam.remove();
      }

   }

   Color getItemColor(int itemId) {
      return (Color)this.configManager.getConfiguration("grounditems", "highlight_" + itemId, (Type)Color.class);
   }

   void setItemColor(int itemId, Color color) {
      this.configManager.setConfiguration("grounditems", "highlight_" + itemId, (Object)color);
   }

   void unsetItemColor(int itemId) {
      this.configManager.unsetConfiguration("grounditems", "highlight_" + itemId);
   }

   boolean shouldDisplayItem(OwnershipFilterMode filterMode, int ownership, int accountType) {
      switch (filterMode) {
         case DROPS:
            return ownership == 1 || ownership == 3;
         case TAKEABLE:
            return ownership != 2 || accountType == 0;
         default:
            return true;
      }
   }

   Map.Entry<Rectangle, GroundItem> getTextBoxBounds() {
      return this.textBoxBounds;
   }

   void setTextBoxBounds(Map.Entry<Rectangle, GroundItem> textBoxBounds) {
      this.textBoxBounds = textBoxBounds;
   }

   Map.Entry<Rectangle, GroundItem> getHiddenBoxBounds() {
      return this.hiddenBoxBounds;
   }

   void setHiddenBoxBounds(Map.Entry<Rectangle, GroundItem> hiddenBoxBounds) {
      this.hiddenBoxBounds = hiddenBoxBounds;
   }

   Map.Entry<Rectangle, GroundItem> getHighlightBoxBounds() {
      return this.highlightBoxBounds;
   }

   void setHighlightBoxBounds(Map.Entry<Rectangle, GroundItem> highlightBoxBounds) {
      this.highlightBoxBounds = highlightBoxBounds;
   }

   boolean isHotKeyPressed() {
      return this.hotKeyPressed;
   }

   void setHotKeyPressed(boolean hotKeyPressed) {
      this.hotKeyPressed = hotKeyPressed;
   }

   boolean isHideAll() {
      return this.hideAll;
   }

   void setHideAll(boolean hideAll) {
      this.hideAll = hideAll;
   }

   public Table<WorldPoint, Integer, GroundItem> getCollectedGroundItems() {
      return this.collectedGroundItems;
   }

   static final class PriceHighlight {
      private final int price;
      private final Color color;

      public PriceHighlight(int price, Color color) {
         this.price = price;
         this.color = color;
      }

      public int getPrice() {
         return this.price;
      }

      public Color getColor() {
         return this.color;
      }

      public boolean equals(Object o) {
         if (o == this) {
            return true;
         } else if (!(o instanceof PriceHighlight)) {
            return false;
         } else {
            PriceHighlight other = (PriceHighlight)o;
            if (this.getPrice() != other.getPrice()) {
               return false;
            } else {
               Object this$color = this.getColor();
               Object other$color = other.getColor();
               if (this$color == null) {
                  if (other$color != null) {
                     return false;
                  }
               } else if (!this$color.equals(other$color)) {
                  return false;
               }

               return true;
            }
         }
      }

      public int hashCode() {
         int PRIME = true;
         int result = 1;
         result = result * 59 + this.getPrice();
         Object $color = this.getColor();
         result = result * 59 + ($color == null ? 43 : $color.hashCode());
         return result;
      }

      public String toString() {
         int var10000 = this.getPrice();
         return "GroundItemsPlugin.PriceHighlight(price=" + var10000 + ", color=" + String.valueOf(this.getColor()) + ")";
      }
   }
}
