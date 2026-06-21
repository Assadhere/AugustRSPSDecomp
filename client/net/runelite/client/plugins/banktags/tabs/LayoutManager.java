package net.runelite.client.plugins.banktags.tabs;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.NonNull;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.EnumComposition;
import net.runelite.api.EquipmentInventorySlot;
import net.runelite.api.Item;
import net.runelite.api.ItemComposition;
import net.runelite.api.ItemContainer;
import net.runelite.api.MenuAction;
import net.runelite.api.MenuEntry;
import net.runelite.api.ScriptEvent;
import net.runelite.api.events.MenuEntryAdded;
import net.runelite.api.events.MenuOptionClicked;
import net.runelite.api.events.ScriptPreFired;
import net.runelite.api.widgets.Widget;
import net.runelite.client.chat.ChatMessageManager;
import net.runelite.client.chat.QueuedMessage;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.EventBus;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.game.ItemManager;
import net.runelite.client.game.ItemVariationMapping;
import net.runelite.client.game.chatbox.ChatboxPanelManager;
import net.runelite.client.game.chatbox.ChatboxTextMenuInput;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.bank.BankSearch;
import net.runelite.client.plugins.banktags.BankTag;
import net.runelite.client.plugins.banktags.BankTagsPlugin;
import net.runelite.client.util.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
public class LayoutManager {
   private static final Logger log = LoggerFactory.getLogger(LayoutManager.class);
   private final Client client;
   private final ItemManager itemManager;
   private final BankTagsPlugin plugin;
   private final ChatboxPanelManager chatboxPanelManager;
   private final BankSearch bankSearch;
   private final ChatMessageManager chatMessageManager;
   private final PotionStorage potionStorage;
   private final EventBus eventBus;
   private final ConfigManager configManager;
   private final List<PluginAutoLayout> autoLayouts = new ArrayList();

   @Inject
   LayoutManager(Client client, ItemManager itemManager, BankTagsPlugin plugin, ChatboxPanelManager chatboxPanelManager, BankSearch bankSearch, ChatMessageManager chatMessageManager, PotionStorage potionStorage, EventBus eventBus, ConfigManager configManager) {
      this.client = client;
      this.itemManager = itemManager;
      this.plugin = plugin;
      this.chatboxPanelManager = chatboxPanelManager;
      this.bankSearch = bankSearch;
      this.chatMessageManager = chatMessageManager;
      this.potionStorage = potionStorage;
      this.eventBus = eventBus;
      this.configManager = configManager;
      this.registerAutoLayout(plugin, "Default", new DefaultLayout());
   }

   public void register() {
      this.eventBus.register(this);
      this.eventBus.register(this.potionStorage);
   }

   public void unregister() {
      this.eventBus.unregister((Object)this);
      this.eventBus.unregister((Object)this.potionStorage);
   }

   @Nullable
   public Layout loadLayout(String tag) {
      String layoutStr = this.configManager.getConfiguration("banktags", "layout_" + Text.standardize(tag));
      if (layoutStr == null) {
         return null;
      } else {
         List<String> layoutList = Text.fromCSV(layoutStr);
         int[] layout = new int[layoutList.size()];

         for(int i = 0; i < layoutList.size(); ++i) {
            layout[i] = Integer.parseInt((String)layoutList.get(i));
         }

         return new Layout(tag, layout);
      }
   }

   public void saveLayout(Layout layout) {
      String tag = layout.getTag();
      int[] l = layout.getLayout();
      StringBuilder sb = new StringBuilder(l.length * 5);

      for(int i = 0; i < l.length; ++i) {
         if (i > 0) {
            sb.append(',');
         }

         sb.append(l[i]);
      }

      this.configManager.setConfiguration("banktags", "layout_" + Text.standardize(tag), sb.toString());
   }

   public void removeLayout(String tag) {
      this.configManager.unsetConfiguration("banktags", "layout_" + Text.standardize(tag));
   }

   private void layout(Layout l) {
      ItemContainer bank = this.client.getItemContainer(95);
      Widget itemContainer = this.client.getWidget(786444);
      Set<Integer> bankItems = new LinkedHashSet();

      for(int i = 0; i < bank.size(); ++i) {
         Widget c = itemContainer.getChild(i);
         if (!c.isSelfHidden() && c.getItemId() > -1 && c.getItemId() != 6512) {
            bankItems.add(c.getItemId());
            if (log.isDebugEnabled()) {
               ItemComposition def = this.itemManager.getItemComposition(c.getItemId());
               log.debug("Bank contains {}{}", def.getName(), def.getPlaceholderId() > -1 && def.getPlaceholderTemplateId() > -1 ? " (placeholder)" : "");
            }

            c.setHidden(true);
         }
      }

      int[] layout = l.getLayout();
      ItemMatcher[] var10000 = new ItemMatcher[]{this::matchExact, this::matchPlaceholder, this::matchesVariant, null};
      PotionStorage var10003 = this.potionStorage;
      Objects.requireNonNull(var10003);
      var10000[3] = var10003::matches;
      ItemMatcher[] matchers = var10000;
      Map<Integer, Integer> layoutToBank = new HashMap();
      ItemMatcher[] var8 = matchers;
      int itemId = matchers.length;

      int layoutItemId;
      for(int var10 = 0; var10 < itemId; ++var10) {
         ItemMatcher matcher = var8[var10];
         int[] var12 = layout;
         layoutItemId = layout.length;

         for(int var14 = 0; var14 < layoutItemId; ++var14) {
            int itemId = var12[var14];
            if (itemId != -1 && !layoutToBank.containsKey(itemId)) {
               int matchedId = matcher.match(bankItems, itemId);
               if (matchedId != -1) {
                  layoutToBank.put(itemId, matchedId);
                  bankItems.remove(matchedId);
                  ItemComposition matchedItemDef = this.client.getItemDefinition(matchedId);
                  boolean removedPlaceholder = bankItems.remove(matchedItemDef.getPlaceholderId());
                  if (log.isDebugEnabled()) {
                     ItemComposition from = this.itemManager.getItemComposition(itemId);
                     ItemComposition to = matchedItemDef;
                     log.debug("Matched {}{} -> {}{} removed placeholder: {}", new Object[]{from.getName(), from.getPlaceholderId() > -1 && from.getPlaceholderTemplateId() > -1 ? " (placeholder)" : "", to.getName(), to.getPlaceholderId() > -1 && to.getPlaceholderTemplateId() > -1 ? " (placeholder)" : "", removedPlaceholder});
                  }
               }
            }
         }
      }

      int lastEmptySlot;
      for(lastEmptySlot = 0; lastEmptySlot < layout.length; ++lastEmptySlot) {
         itemId = layout[lastEmptySlot];
         if (itemId != -1) {
            Integer bankItemId = (Integer)layoutToBank.get(itemId);
            if (bankItemId == null) {
               if (log.isDebugEnabled()) {
                  ItemComposition def = this.itemManager.getItemComposition(itemId);
                  log.debug("Layout contains {}{} with no matching item", def.getName(), def.getPlaceholderTemplateId() > -1 && def.getPlaceholderId() > -1 ? " (placeholder)" : "");
               }

               bankItemId = itemId;
            }

            Widget c = itemContainer.getChild(lastEmptySlot);
            this.drawItem(l, c, bank, bankItemId, lastEmptySlot);
         }
      }

      lastEmptySlot = -1;
      boolean modified = false;

      for(Iterator var28 = bankItems.iterator(); var28.hasNext(); modified = true) {
         int itemId = (Integer)var28.next();

         do {
            ++lastEmptySlot;
         } while(lastEmptySlot < layout.length && layout[lastEmptySlot] > -1);

         Widget c = itemContainer.getChild(lastEmptySlot);
         if (c == null || c.getOriginalHeight() != 32) {
            break;
         }

         this.drawItem(l, c, bank, itemId, lastEmptySlot);
         if (log.isDebugEnabled()) {
            ItemComposition def = this.itemManager.getItemComposition(itemId);
            log.debug("Bank contains {}{} but is not in the layout", def.getName(), def.getPlaceholderTemplateId() > -1 && def.getPlaceholderId() > -1 ? " (placeholder)" : "");
         }

         layoutItemId = this.itemManager.canonicalize(itemId);
         l.addItem(layoutItemId);
      }

      while(true) {
         do {
            ++lastEmptySlot;
         } while(lastEmptySlot < layout.length && layout[lastEmptySlot] > -1);

         Widget c = itemContainer.getChild(lastEmptySlot);
         if (c == null || c.getOriginalHeight() != 32) {
            if (modified) {
               this.saveLayout(l);
            }

            return;
         }

         this.drawItem(l, c, bank, -1, lastEmptySlot);
      }
   }

   private void drawItem(Layout l, Widget c, ItemContainer bank, int item, int idx) {
      int posY;
      if (item > -1 && item != 20594) {
         ItemComposition def = this.client.getItemDefinition(item);
         posY = bank.count(item);
         int qty = posY > 0 ? posY : this.potionStorage.count(item);
         boolean isPotStorage = posY <= 0 && qty > 0;
         c.setItemId(item);
         c.setItemQuantity(qty);
         c.setItemQuantityMode(2);
         c.setName("<col=ff9040>" + def.getName() + "</col>");
         c.clearActions();
         if (def.getPlaceholderTemplateId() >= 0 && def.getPlaceholderId() >= 0) {
            c.setItemQuantity(qty);
            c.setOpacity(120);
            c.setAction(7, "Release");
            c.setAction(9, "Examine");
         } else if (qty == 0) {
            c.setOpacity(120);
            c.setItemQuantity(Integer.MAX_VALUE);
            c.setItemQuantityMode(0);
            if ((this.plugin.getOptions() & 1) != 0) {
               c.setAction(6, "Duplicate-item");
               c.setAction(7, "Remove-layout");
            }
         } else {
            int quantityType = this.client.getVarbitValue(6590);
            int requestQty = this.client.getVarbitValue(3960);
            String suffix;
            switch (quantityType) {
               case 1:
                  suffix = "5";
                  break;
               case 2:
                  suffix = "10";
                  break;
               case 3:
                  suffix = Integer.toString(Math.max(1, requestQty));
                  break;
               case 4:
                  suffix = "All";
                  break;
               default:
                  suffix = "1";
            }

            c.setAction(0, "Withdraw-" + suffix);
            if (quantityType != 0) {
               c.setAction(1, "Withdraw-1");
            }

            c.setAction(2, "Withdraw-5");
            c.setAction(3, "Withdraw-10");
            if (requestQty > 0) {
               c.setAction(4, "Withdraw-" + requestQty);
            }

            c.setAction(5, "Withdraw-X");
            c.setAction(6, "Withdraw-All");
            c.setAction(7, "Withdraw-All-but-1");
            if (!isPotStorage && this.client.getVarbitValue(16125) == 1 && def.getIntValue(2257) != -1) {
               c.setAction(8, "Configure-Charges");
            }

            if (!isPotStorage && this.client.getVarbitValue(3755) == 0) {
               c.setAction(9, "Placeholder");
            }

            if (!isPotStorage) {
               c.setAction(10, "Examine");
            }

            c.setOpacity(0);
         }

         c.setOnDragListener(new Object[]{284, -2147483645, -2147483643, -2147483647, -2147483646, 786445, 0});
         c.setOnDragCompleteListener(new Object[]{(ev) -> {
            this.dragCompleteHandler(l, ev);
         }});
      } else {
         c.setOriginalWidth(48);
         c.setOriginalHeight(36);
         c.clearActions();
         c.setItemId(-1);
         c.setItemQuantity(0);
         c.setOnDragListener((Object[])null);
         c.setOnDragCompleteListener((Object[])null);
      }

      int posX = idx % 8 * 48 + 51;
      posY = idx / 8 * 36;
      c.setHidden(false);
      c.setOriginalX(posX);
      c.setOriginalY(posY);
      c.revalidate();
   }

   private void dragCompleteHandler(Layout l, ScriptEvent ev) {
      this.client.setDraggedOnWidget((Widget)null);
      Widget source = ev.getSource();
      Widget target = ev.getTarget();
      if (target != null) {
         if (source.getId() == 786444 && target.getId() == 786444) {
            int sidx = source.getIndex();
            int tidx = target.getIndex();
            boolean swap = this.client.getVarbitValue(3959) == 0;
            if (sidx >= l.size() || tidx >= l.size()) {
               l.resize(Math.max(sidx, tidx) + 1);
            }

            if (swap) {
               log.debug("Swap {} <-> {}", sidx, tidx);
               l.swap(sidx, tidx);
            } else {
               log.debug("Insert {} -> {}", sidx, tidx);
               l.insert(sidx, tidx);
            }

            this.saveLayout(l);
            this.bankSearch.layoutBank();
         }
      }
   }

   private int matchExact(Set<Integer> bank, int itemId) {
      return bank.contains(itemId) ? itemId : -1;
   }

   private int matchPlaceholder(Set<Integer> bank, int itemId) {
      ItemComposition config = this.itemManager.getItemComposition(itemId);
      int placeholderId = config.getPlaceholderId();
      return placeholderId != -1 && bank.contains(placeholderId) ? placeholderId : -1;
   }

   private int matchesVariant(Set<Integer> bank, int itemId) {
      int baseId = ItemVariationMapping.map(itemId);
      Collection<Integer> variations = ItemVariationMapping.getVariations(baseId);
      if (variations.size() > 1) {
         Iterator var5 = variations.iterator();

         while(var5.hasNext()) {
            int variationId = (Integer)var5.next();
            if (bank.contains(variationId)) {
               return variationId;
            }

            ItemComposition config = this.itemManager.getItemComposition(variationId);
            int placeholderId = config.getPlaceholderId();
            if (placeholderId != -1 && bank.contains(placeholderId)) {
               return placeholderId;
            }
         }
      }

      return -1;
   }

   public synchronized void registerAutoLayout(@Nonnull Plugin plugin, @NonNull String name, @NonNull AutoLayout al) {
      if (name == null) {
         throw new NullPointerException("name is marked non-null but is null");
      } else if (al == null) {
         throw new NullPointerException("al is marked non-null but is null");
      } else {
         Iterator var4 = this.autoLayouts.iterator();

         PluginAutoLayout pluginAutoLayout;
         do {
            if (!var4.hasNext()) {
               this.autoLayouts.add(new PluginAutoLayout(plugin, name, al));
               return;
            }

            pluginAutoLayout = (PluginAutoLayout)var4.next();
         } while(!pluginAutoLayout.getName().equals(name));

         throw new IllegalArgumentException("Auto layout " + name + " is already registered");
      }
   }

   public synchronized void unregisterAutoLayout(String name) {
      Iterator var2 = this.autoLayouts.iterator();

      PluginAutoLayout pluginAutoLayout;
      do {
         if (!var2.hasNext()) {
            return;
         }

         pluginAutoLayout = (PluginAutoLayout)var2.next();
      } while(!pluginAutoLayout.getName().equals(name));

      this.autoLayouts.remove(pluginAutoLayout);
   }

   @Subscribe
   public void onScriptPreFired(ScriptPreFired event) {
      if (event.getScriptId() == 505) {
         this.resetWidgets();
         this.potionStorage.cachePotions = true;
         BankTag activeTag = this.plugin.getActiveBankTag();
         if (activeTag != null) {
            Layout layout = this.plugin.getActiveLayout();
            if (layout != null) {
               this.layout(layout);
               this.scrollLayout(layout);
            }
         }
      }

   }

   private void resetWidgets() {
      Widget w = this.client.getWidget(786444);
      Widget[] var2 = w.getChildren();
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         Widget c = var2[var4];
         if (c.getOriginalHeight() < 32) {
            break;
         }

         if (c.getOriginalWidth() != 36 || c.getOriginalHeight() != 32) {
            c.setOriginalWidth(36);
            c.setOriginalHeight(32);
            c.revalidate();
         }
      }

   }

   void onMenuEntryAdded(MenuEntryAdded event, TabInterface tabInterface) {
      if (event.getActionParam1() == 786441 && event.getOption().equals("Disable layout")) {
         int idx = -1;
         Iterator var4 = this.autoLayouts.iterator();

         while(var4.hasNext()) {
            PluginAutoLayout autoLayout = (PluginAutoLayout)var4.next();
            --idx;
            this.client.createMenuEntry(idx).setOption("Auto layout: " + autoLayout.getName()).setTarget(event.getTarget()).setType(MenuAction.RUNELITE_HIGH_PRIORITY).onClick((e) -> {
               String tag = Text.standardize(e.getTarget());
               if (!tag.equals(tabInterface.getActiveTag())) {
                  this.chatMessageManager.queue(QueuedMessage.builder().type(ChatMessageType.CONSOLE).runeLiteFormattedMessage("The tag tab must be open first before performing an auto layout.").build());
               } else {
                  Layout old = this.plugin.getActiveLayout();
                  Layout new_ = autoLayout.autoLayout.generateLayout(old);
                  this.plugin.openTag(tag, new_);
                  ChatboxTextMenuInput var10000 = this.chatboxPanelManager.openTextMenuInput("Tab laid out using the '" + autoLayout.getName() + "' layout.").option("1. Keep", () -> {
                     this.saveLayout(new_);
                  }).option("2. Undo", () -> {
                     this.plugin.openTag(tag, old);
                  });
                  BankSearch var10001 = this.bankSearch;
                  Objects.requireNonNull(var10001);
                  var10000.onClose(var10001::layoutBank).build();
               }
            });
         }
      }

   }

   void onMenuOptionClicked(MenuOptionClicked event) {
      if (event.getParam1() == 786444 && this.plugin.getActiveLayout() != null) {
         MenuEntry menu = event.getMenuEntry();
         Widget w = menu.getWidget();
         if (w != null && w.getItemId() > -1) {
            ItemContainer bank = this.client.getItemContainer(95);
            int idx = bank.find(w.getItemId());
            if (idx > -1 && menu.getParam0() != idx) {
               menu.setParam0(idx);
               return;
            }

            idx = this.potionStorage.getIdx(w.getItemId());
            if (idx > -1) {
               this.potionStorage.prepareWidgets();
               menu.setParam1(786483);
               menu.setParam0(idx);
            }
         }
      }

   }

   private void scrollLayout(Layout l) {
      int pos;
      for(pos = l.size() - 1; pos >= 0 && l.getItemAtPos(pos) == -1; --pos) {
      }

      int rows = (pos + 8 - 1) / 8;
      int scrollY = rows * 36;
      Widget w = this.client.getWidget(786444);
      if (scrollY < w.getScrollY()) {
         int bankHeight = w.getHeight() / 36;
         rows -= bankHeight;
         if (rows < 0) {
            rows = 0;
         }

         scrollY = rows * 36;
         log.debug("Adjusting tab scroll to {} from {}", scrollY, w.getScrollY());
         w.setScrollY(scrollY);
         this.client.setVarcIntValue(51, scrollY);
      }

   }

   private class DefaultLayout implements AutoLayout {
      public Layout generateLayout(Layout previous) {
         Layout l = new Layout(previous);
         List<Integer> removed = new ArrayList();
         ItemContainer e = LayoutManager.this.client.getItemContainer(94);
         int pos;
         int idx;
         int itemIdx;
         int oldx;
         Item item;
         if (e != null) {
            int[] format = new int[]{-1, EquipmentInventorySlot.HEAD.getSlotIdx(), -1, EquipmentInventorySlot.CAPE.getSlotIdx(), EquipmentInventorySlot.AMULET.getSlotIdx(), EquipmentInventorySlot.AMMO.getSlotIdx(), EquipmentInventorySlot.WEAPON.getSlotIdx(), EquipmentInventorySlot.BODY.getSlotIdx(), EquipmentInventorySlot.SHIELD.getSlotIdx(), -1, EquipmentInventorySlot.LEGS.getSlotIdx(), -1, EquipmentInventorySlot.GLOVES.getSlotIdx(), EquipmentInventorySlot.BOOTS.getSlotIdx(), EquipmentInventorySlot.RING.getSlotIdx()};
            pos = 0;

            for(idx = 0; pos < format.length; ++pos) {
               if (pos > 0 && pos % 3 == 0) {
                  idx += 8;
               }

               itemIdx = idx + pos % 3;
               oldx = l.getItemAtPos(itemIdx);
               if (oldx != -1) {
                  if (LayoutManager.log.isDebugEnabled()) {
                     LayoutManager.log.debug("Moving {}", LayoutManager.this.itemManager.getItemComposition(oldx).getName());
                  }

                  removed.add(oldx);
               }

               item = e.getItem(format[pos]);
               if (item != null) {
                  l.setItemAtPos(LayoutManager.this.itemManager.canonicalize(item.getId()), itemIdx);
               } else {
                  l.setItemAtPos(-1, itemIdx);
               }
            }
         }

         ItemContainer i = LayoutManager.this.client.getItemContainer(93);
         if (i != null) {
            pos = 0;

            for(idx = 4; pos < i.size(); ++pos) {
               if (pos > 0 && pos % 4 == 0) {
                  idx += 8;
               }

               itemIdx = idx + pos % 4;
               oldx = l.getItemAtPos(itemIdx);
               if (oldx != -1) {
                  removed.add(oldx);
               }

               item = i.getItem(pos);
               if (item != null) {
                  l.setItemAtPos(LayoutManager.this.itemManager.canonicalize(item.getId()), itemIdx);
               } else {
                  l.setItemAtPos(-1, itemIdx);
               }
            }
         }

         if (i != null && this.hasRunePouch(i)) {
            int[] RUNEPOUCH_RUNES = new int[]{29, 1622, 1623, 14285};
            EnumComposition runepouchEnum = LayoutManager.this.client.getEnum(982);
            itemIdx = 40;

            for(oldx = 0; oldx < RUNEPOUCH_RUNES.length; ++itemIdx) {
               int runeId = LayoutManager.this.client.getVarbitValue(RUNEPOUCH_RUNES[oldx]);
               if (runeId > 0) {
                  int itemId = runepouchEnum.getIntValue(runeId);
                  int old = l.getItemAtPos(itemIdx);
                  if (old != -1) {
                     removed.add(old);
                  }

                  l.setItemAtPos(itemId, itemIdx);
               }

               ++oldx;
            }
         }

         for(pos = 0; pos < 5; ++pos) {
            idx = pos * 8 + 3;
            itemIdx = l.getItemAtPos(idx);
            if (itemIdx != -1) {
               removed.add(itemIdx);
               l.setItemAtPos(-1, idx);
            }
         }

         pos = 56;
         Iterator var16 = removed.iterator();

         while(var16.hasNext()) {
            itemIdx = (Integer)var16.next();
            if (l.count(itemIdx) == 0) {
               if (LayoutManager.log.isDebugEnabled()) {
                  LayoutManager.log.debug("Adding {} at {}", LayoutManager.this.itemManager.getItemComposition(itemIdx).getName(), pos);
               }

               l.addItemAfter(itemIdx, pos++);
            }
         }

         return l;
      }

      private boolean hasRunePouch(ItemContainer inv) {
         Collection<Integer> runePouchVariations = ItemVariationMapping.getVariations(12791);
         Collection<Integer> divineRunePouchVariations = ItemVariationMapping.getVariations(27281);
         Stream var10000 = runePouchVariations.stream();
         Objects.requireNonNull(inv);
         boolean var4;
         if (!var10000.anyMatch(inv::contains)) {
            var10000 = divineRunePouchVariations.stream();
            Objects.requireNonNull(inv);
            if (!var10000.anyMatch(inv::contains)) {
               var4 = false;
               return var4;
            }
         }

         var4 = true;
         return var4;
      }
   }

   private static final class PluginAutoLayout {
      private final Plugin plugin;
      private final String name;
      private final AutoLayout autoLayout;

      public PluginAutoLayout(Plugin plugin, String name, AutoLayout autoLayout) {
         this.plugin = plugin;
         this.name = name;
         this.autoLayout = autoLayout;
      }

      public Plugin getPlugin() {
         return this.plugin;
      }

      public String getName() {
         return this.name;
      }

      public AutoLayout getAutoLayout() {
         return this.autoLayout;
      }

      public boolean equals(Object o) {
         if (o == this) {
            return true;
         } else if (!(o instanceof PluginAutoLayout)) {
            return false;
         } else {
            PluginAutoLayout other;
            label44: {
               other = (PluginAutoLayout)o;
               Object this$plugin = this.getPlugin();
               Object other$plugin = other.getPlugin();
               if (this$plugin == null) {
                  if (other$plugin == null) {
                     break label44;
                  }
               } else if (this$plugin.equals(other$plugin)) {
                  break label44;
               }

               return false;
            }

            Object this$name = this.getName();
            Object other$name = other.getName();
            if (this$name == null) {
               if (other$name != null) {
                  return false;
               }
            } else if (!this$name.equals(other$name)) {
               return false;
            }

            Object this$autoLayout = this.getAutoLayout();
            Object other$autoLayout = other.getAutoLayout();
            if (this$autoLayout == null) {
               if (other$autoLayout != null) {
                  return false;
               }
            } else if (!this$autoLayout.equals(other$autoLayout)) {
               return false;
            }

            return true;
         }
      }

      public int hashCode() {
         int PRIME = true;
         int result = 1;
         Object $plugin = this.getPlugin();
         result = result * 59 + ($plugin == null ? 43 : $plugin.hashCode());
         Object $name = this.getName();
         result = result * 59 + ($name == null ? 43 : $name.hashCode());
         Object $autoLayout = this.getAutoLayout();
         result = result * 59 + ($autoLayout == null ? 43 : $autoLayout.hashCode());
         return result;
      }

      public String toString() {
         String var10000 = String.valueOf(this.getPlugin());
         return "LayoutManager.PluginAutoLayout(plugin=" + var10000 + ", name=" + this.getName() + ", autoLayout=" + String.valueOf(this.getAutoLayout()) + ")";
      }
   }

   @FunctionalInterface
   interface ItemMatcher {
      int match(Set<Integer> var1, int var2);
   }
}
