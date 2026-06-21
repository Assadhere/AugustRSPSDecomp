package net.runelite.client.plugins.prayer;

import com.google.common.base.MoreObjects;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.inject.Inject;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.EnumComposition;
import net.runelite.api.GameState;
import net.runelite.api.ItemComposition;
import net.runelite.api.MenuAction;
import net.runelite.api.events.BeforeRender;
import net.runelite.api.events.MenuOptionClicked;
import net.runelite.api.events.ScriptPostFired;
import net.runelite.api.events.WidgetDrag;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetUtil;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.chat.ChatMessageManager;
import net.runelite.client.chat.QueuedMessage;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ProfileChanged;
import net.runelite.client.menus.MenuManager;
import net.runelite.client.menus.WidgetMenuOption;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class PrayerReorder {
   private static final Logger log = LoggerFactory.getLogger(PrayerReorder.class);
   private static final int PRAYER_X_OFFSET = 37;
   private static final int PRAYER_Y_OFFSET = 37;
   private static final int QUICK_PRAYER_SPRITE_X_OFFSET = 2;
   private static final int QUICK_PRAYER_SPRITE_Y_OFFSET = 2;
   private static final int PRAYER_COLUMN_COUNT = 5;
   private static final int CUSTOM_PRAYERBOOK_GROUP = 3038;
   private static final int CUSTOM_QUICKPRAYER_GROUP = 3039;
   private static final int CUSTOM_QUICKPRAYER_BUTTONS = 4;
   private static final int CUSTOM_QUICKPRAYER_ICON_OFFSET = 64;
   private static final int CUSTOM_QUICKPRAYER_BORDER_OFFSET = 128;
   private static final int CUSTOM_PRAYER_OFFSET = 36;
   private static final int CUSTOM_QUICK_PRAYER_OFFSET = 37;
   private static final int PRAYER_DB_TABLE = 65420;
   private static final int PRAYER_DB_COL_LEVEL = 3;
   private static final int PRAYER_DB_COL_COMPONENT_ID = 9;
   private static final int PRAYER_DB_COL_QUICK_SLOT = 10;
   private static final int PRAYER_DB_COL_SORT_PRIORITY = 22;
   private static final String CUSTOM_ORDER_KEY = "prayer_order_custom";
   private static final String LOCK = "Disable prayer reordering";
   private static final String UNLOCK = "Enable prayer reordering";
   private static final String RESET = "Reset prayer order";
   private static final WidgetMenuOption FIXED_PRAYER_TAB_LOCK = new WidgetMenuOption("Disable prayer reordering", "", new int[]{35913797});
   private static final WidgetMenuOption FIXED_PRAYER_TAB_UNLOCK = new WidgetMenuOption("Enable prayer reordering", "", new int[]{35913797});
   private static final WidgetMenuOption FIXED_PRAYER_TAB_RESET = new WidgetMenuOption("Reset prayer order", "", new int[]{35913797});
   private static final WidgetMenuOption RESIZABLE_PRAYER_TAB_LOCK = new WidgetMenuOption("Disable prayer reordering", "", new int[]{10551360});
   private static final WidgetMenuOption RESIZABLE_PRAYER_TAB_UNLOCK = new WidgetMenuOption("Enable prayer reordering", "", new int[]{10551360});
   private static final WidgetMenuOption RESIZABLE_PRAYER_TAB_RESET = new WidgetMenuOption("Reset prayer order", "", new int[]{10551360});
   private static final WidgetMenuOption RESIZABLE_BOTTOM_LINE_PRAYER_TAB_LOCK = new WidgetMenuOption("Disable prayer reordering", "", new int[]{10747961});
   private static final WidgetMenuOption RESIZABLE_BOTTOM_LINE_PRAYER_TAB_UNLOCK = new WidgetMenuOption("Enable prayer reordering", "", new int[]{10747961});
   private static final WidgetMenuOption RESIZABLE_BOTTOM_LINE_PRAYER_TAB_RESET = new WidgetMenuOption("Reset prayer order", "", new int[]{10747961});
   private final Client client;
   private final ClientThread clientThread;
   private final ConfigManager configManager;
   private final ChatMessageManager chatMessageManager;
   private final MenuManager menuManager;
   private boolean reordering;
   private List<CustomPrayer> customPrayerModel;

   void startUp() {
      this.refreshPrayerTabOption();
      this.configManager.unsetConfiguration("reorderprayers", "unlockPrayerReordering");
      this.configManager.unsetConfiguration("reorderprayers", "prayerOrder");
      this.configManager.unsetConfiguration("runelite", "ReorderPrayersPlugin".toLowerCase());
      if (this.client.getGameState() == GameState.LOGGED_IN) {
         this.clientThread.invokeLater(this::redrawPrayers);
      }

   }

   void shutDown() {
      this.reordering = false;
      this.clearPrayerTabMenus();
      this.clientThread.invokeLater(() -> {
         this.rebuildPrayers(false);
      });
      this.clientThread.invokeLater(() -> {
         this.rebuildCustomPrayers(false);
      });
      this.clientThread.invokeLater(this::redrawPrayers);
   }

   void reset() {
      Iterator var1 = this.configManager.getConfigurationKeys("prayer.prayer_order_book").iterator();

      String key;
      String[] str;
      while(var1.hasNext()) {
         key = (String)var1.next();
         str = key.split("\\.", 2);
         if (str.length == 2) {
            this.configManager.unsetConfiguration(str[0], str[1]);
         }
      }

      var1 = this.configManager.getConfigurationKeys("prayer.prayer_hidden_book").iterator();

      while(var1.hasNext()) {
         key = (String)var1.next();
         str = key.split("\\.", 2);
         if (str.length == 2) {
            this.configManager.unsetConfiguration(str[0], str[1]);
         }
      }

      this.configManager.unsetConfiguration("prayer", "prayer_order_custom");
      this.clientThread.invokeLater(this::redrawPrayers);
   }

   private int[] getPrayerOrder(int prayerbook) {
      String s = this.configManager.getConfiguration("prayer", "prayer_order_book_" + prayerbook);
      return s == null ? null : Arrays.stream(s.split(",")).mapToInt(Integer::parseInt).toArray();
   }

   private void setPrayerOrder(int prayerbook, int[] prayers) {
      String s = (String)Arrays.stream(prayers).mapToObj(Integer::toString).collect(Collectors.joining(","));
      this.configManager.setConfiguration("prayer", "prayer_order_book_" + prayerbook, s);
   }

   private boolean isHidden(int prayerbook, int prayer) {
      Boolean b = (Boolean)this.configManager.getConfiguration("prayer", "prayer_hidden_book_" + prayerbook + "_" + prayer, (Type)Boolean.TYPE);
      return b == Boolean.TRUE;
   }

   private void setHidden(int prayerbook, int prayer, boolean hidden) {
      if (hidden) {
         this.configManager.setConfiguration("prayer", "prayer_hidden_book_" + prayerbook + "_" + prayer, (Object)true);
      } else {
         this.configManager.unsetConfiguration("prayer", "prayer_hidden_book_" + prayerbook + "_" + prayer);
      }

   }

   @Subscribe
   public void onWidgetDrag(WidgetDrag event) {
      if (this.client.getMouseCurrentButton() == 0) {
         Widget draggedWidget = this.client.getDraggedWidget();
         Widget draggedOnWidget = this.client.getDraggedOnWidget();
         if (draggedWidget != null && draggedOnWidget != null) {
            int draggedGroupId = WidgetUtil.componentToInterface(draggedWidget.getId());
            int draggedOnGroupId = WidgetUtil.componentToInterface(draggedOnWidget.getId());
            if (draggedGroupId == 3038 && draggedOnGroupId == 3038) {
               this.onCustomPrayerDrag(draggedWidget, draggedOnWidget);
               return;
            }

            if (draggedGroupId == 541 && draggedOnGroupId == 541) {
               int prayerbook = this.client.getVarbitValue(14826);
               int fromId = this.findPrayerIdFromComponent(prayerbook, draggedWidget);
               int toId = this.findPrayerIdFromComponent(prayerbook, draggedOnWidget);
               if (fromId != -1 && toId != -1) {
                  this.client.setDraggedOnWidget((Widget)null);
                  int[] prayerOrder = this.getPrayerOrder(prayerbook);
                  if (prayerOrder == null) {
                     prayerOrder = this.defaultPrayerOrder(this.getPrayerBookEnum(prayerbook));
                  }

                  int fromIdx = ArrayUtils.indexOf(prayerOrder, fromId);
                  int toIdx = ArrayUtils.indexOf(prayerOrder, toId);
                  log.debug("Inserting prayer {} at {} (idx {}->{})", new Object[]{fromId, toId, fromIdx, toIdx});
                  prayerOrder = ArrayUtils.remove(prayerOrder, fromIdx);
                  prayerOrder = ArrayUtils.add(prayerOrder, toIdx, fromId);
                  this.setPrayerOrder(prayerbook, prayerOrder);
                  this.rebuildPrayers(true);
                  return;
               }

               return;
            }

            return;
         }
      }

   }

   @Subscribe
   public void onScriptPostFired(ScriptPostFired scriptPostFired) {
      int scriptId = scriptPostFired.getScriptId();
      if (this.reordering && scriptId == 463 || scriptId == 547 || scriptId == 466) {
         this.rebuildPrayers(this.reordering);
      }

   }

   @Subscribe
   public void onBeforeRender(BeforeRender event) {
      if (this.isInterfaceOpen(3038) || this.isInterfaceOpen(3039)) {
         this.rebuildCustomPrayers(this.reordering);
      }

   }

   private EnumComposition getPrayerBookEnum(int prayerbook) {
      if (prayerbook == 1) {
         return this.client.getEnum(4959);
      } else {
         boolean deadeye = this.client.getVarbitValue(16097) != 0;
         boolean vigour = this.client.getVarbitValue(16098) != 0;
         if (deadeye && vigour) {
            return this.client.getEnum(3343);
         } else if (deadeye) {
            return this.client.getEnum(3341);
         } else {
            return vigour ? this.client.getEnum(3342) : this.client.getEnum(4956);
         }
      }
   }

   private int[] defaultPrayerOrder(EnumComposition prayerEnum) {
      return Arrays.stream(prayerEnum.getKeys()).boxed().sorted(Comparator.comparing((id) -> {
         int prayerObjId = prayerEnum.getIntValue(id);
         ItemComposition prayerObj = this.client.getItemDefinition(prayerObjId);
         return prayerObj.getIntValue(1753);
      })).mapToInt((i) -> {
         return i;
      }).toArray();
   }

   private int findPrayerIdFromComponent(int prayerbook, Widget component) {
      EnumComposition prayers = this.getPrayerBookEnum(prayerbook);
      int[] keys = prayers.getKeys();
      int[] vals = prayers.getIntVals();

      for(int i = 0; i < keys.length; ++i) {
         ItemComposition prayer = this.client.getItemDefinition(vals[i]);
         if (prayer.getIntValue(1751) == component.getId()) {
            return keys[i];
         }
      }

      return -1;
   }

   private void clearPrayerTabMenus() {
      this.menuManager.removeManagedCustomMenu(FIXED_PRAYER_TAB_LOCK);
      this.menuManager.removeManagedCustomMenu(RESIZABLE_PRAYER_TAB_LOCK);
      this.menuManager.removeManagedCustomMenu(RESIZABLE_BOTTOM_LINE_PRAYER_TAB_LOCK);
      this.menuManager.removeManagedCustomMenu(FIXED_PRAYER_TAB_UNLOCK);
      this.menuManager.removeManagedCustomMenu(RESIZABLE_PRAYER_TAB_UNLOCK);
      this.menuManager.removeManagedCustomMenu(RESIZABLE_BOTTOM_LINE_PRAYER_TAB_UNLOCK);
      this.menuManager.removeManagedCustomMenu(FIXED_PRAYER_TAB_RESET);
      this.menuManager.removeManagedCustomMenu(RESIZABLE_PRAYER_TAB_RESET);
      this.menuManager.removeManagedCustomMenu(RESIZABLE_BOTTOM_LINE_PRAYER_TAB_RESET);
   }

   private void reordering(boolean state) {
      this.reordering = state;
      String message = this.reordering ? "Prayer book reordering is now enabled." : "Prayer book reordering is now disabled.";
      this.chatMessageManager.queue(QueuedMessage.builder().type(ChatMessageType.CONSOLE).runeLiteFormattedMessage(message).build());
      this.refreshPrayerTabOption();
      this.redrawPrayers();
   }

   private void refreshPrayerTabOption() {
      this.clearPrayerTabMenus();
      if (this.reordering) {
         this.menuManager.addManagedCustomMenu(FIXED_PRAYER_TAB_LOCK, (e) -> {
            this.reordering(false);
         });
         this.menuManager.addManagedCustomMenu(RESIZABLE_PRAYER_TAB_LOCK, (e) -> {
            this.reordering(false);
         });
         this.menuManager.addManagedCustomMenu(RESIZABLE_BOTTOM_LINE_PRAYER_TAB_LOCK, (e) -> {
            this.reordering(false);
         });
      } else {
         this.menuManager.addManagedCustomMenu(FIXED_PRAYER_TAB_UNLOCK, (e) -> {
            this.reordering(true);
         });
         this.menuManager.addManagedCustomMenu(RESIZABLE_PRAYER_TAB_UNLOCK, (e) -> {
            this.reordering(true);
         });
         this.menuManager.addManagedCustomMenu(RESIZABLE_BOTTOM_LINE_PRAYER_TAB_UNLOCK, (e) -> {
            this.reordering(true);
         });
      }

      this.menuManager.addManagedCustomMenu(FIXED_PRAYER_TAB_RESET, (e) -> {
         this.resetOrder();
      });
      this.menuManager.addManagedCustomMenu(RESIZABLE_PRAYER_TAB_RESET, (e) -> {
         this.resetOrder();
      });
      this.menuManager.addManagedCustomMenu(RESIZABLE_BOTTOM_LINE_PRAYER_TAB_RESET, (e) -> {
         this.resetOrder();
      });
   }

   private void resetOrder() {
      this.reset();
      this.chatMessageManager.queue(QueuedMessage.builder().type(ChatMessageType.CONSOLE).runeLiteFormattedMessage("Prayer order has been reset to default.").build());
   }

   private void redrawPrayers() {
      Widget w = this.client.getWidget(541, 0);
      if (w != null) {
         this.client.runScript(w.getOnVarTransmitListener());
      }

   }

   private void rebuildPrayers(boolean unlocked) {
      int prayerbook = this.client.getVarbitValue(14826);
      EnumComposition prayerBookEnum = this.getPrayerBookEnum(prayerbook);
      int[] prayerIds = (int[])MoreObjects.firstNonNull(this.getPrayerOrder(prayerbook), this.defaultPrayerOrder(prayerBookEnum));
      int index;
      int prayerObjId;
      int x;
      int y;
      int widgetX;
      if (this.isInterfaceOpen(541)) {
         int index = 0;
         int[] var6 = prayerIds;
         int var7 = prayerIds.length;

         for(index = 0; index < var7; ++index) {
            int prayerId = var6[index];
            prayerObjId = prayerBookEnum.getIntValue(prayerId);
            ItemComposition prayerObj = this.client.getItemDefinition(prayerObjId);
            Widget prayerWidget = this.client.getWidget(prayerObj.getIntValue(1751));

            assert prayerWidget != null;

            x = prayerWidget.getClickMask();
            if (unlocked) {
               x |= 1179648;
            } else {
               x &= -1179649;
            }

            prayerWidget.setClickMask(x);
            boolean hidden = this.isHidden(prayerbook, prayerId);
            if (hidden && !unlocked) {
               prayerWidget.setHidden(true);
               ++index;
            } else {
               if (unlocked) {
                  prayerWidget.setHidden(false);
                  if (hidden) {
                     prayerWidget.setAction(3, "Unhide");
                     prayerWidget.getChild(1).setOpacity(200);
                  } else {
                     prayerWidget.setAction(3, "Hide");
                  }
               } else {
                  prayerWidget.setAction(3, (String)null);
               }

               int x = index % 5;
               y = index / 5;
               widgetX = x * 37;
               int widgetY = y * 37;
               prayerWidget.setPos(widgetX, widgetY);
               prayerWidget.revalidate();
               ++index;
            }
         }

         this.createWarning(unlocked);
      }

      if (this.isInterfaceOpen(77)) {
         Widget prayersContainer = this.client.getWidget(5046276);
         if (prayersContainer == null) {
            return;
         }

         Widget[] prayerWidgets = prayersContainer.getDynamicChildren();
         if (prayerWidgets == null || prayerWidgets.length != prayerBookEnum.size() * 3) {
            return;
         }

         int[] sortedPrayers = this.defaultPrayerOrder(prayerBookEnum);
         index = 0;
         int[] var23 = prayerIds;
         prayerObjId = prayerIds.length;

         for(int var24 = 0; var24 < prayerObjId; ++var24) {
            int prayerId = var23[var24];
            x = index % 5;
            int y = index / 5;
            Widget prayerWidget = prayerWidgets[prayerId];
            prayerWidget.setPos(x * 37, y * 37);
            prayerWidget.revalidate();
            y = ArrayUtils.indexOf(sortedPrayers, prayerId);
            widgetX = prayerBookEnum.size() + 2 * y;
            Widget prayerSpriteWidget = prayerWidgets[widgetX];
            prayerSpriteWidget.setPos(2 + x * 37, 2 + y * 37);
            prayerSpriteWidget.revalidate();
            Widget prayerToggleWidget = prayerWidgets[widgetX + 1];
            prayerToggleWidget.setPos(x * 37, y * 37);
            prayerToggleWidget.revalidate();
            ++index;
         }
      }

   }

   private void createWarning(boolean unlocked) {
      Widget w = this.client.getWidget(35454976);
      w.deleteAllChildren();
      if (unlocked) {
         Widget c = w.createChild(3);
         c.setHeightMode(1);
         c.setWidthMode(1);
         c.setTextColor(16711680);
         c.setFilled(true);
         c.setOpacity(220);
         c.revalidate();
      }

   }

   private void onCustomPrayerDrag(Widget draggedWidget, Widget draggedOnWidget) {
      List<CustomPrayer> model = this.getCustomPrayerModel();
      if (!model.isEmpty()) {
         int fromId = WidgetUtil.componentToId(draggedWidget.getId());
         int toId = WidgetUtil.componentToId(draggedOnWidget.getId());
         int[] order = this.customPrayerOrder(model);
         int fromIdx = ArrayUtils.indexOf(order, fromId);
         int toIdx = ArrayUtils.indexOf(order, toId);
         if (fromIdx != -1 && toIdx != -1) {
            this.client.setDraggedOnWidget((Widget)null);
            log.debug("Swapping custom prayer {}<->{} (idx {}<->{})", new Object[]{fromId, toId, fromIdx, toIdx});
            int tmp = order[toIdx];
            order[toIdx] = order[fromIdx];
            order[fromIdx] = tmp;
            this.setCustomPrayerOrder(order);
            this.rebuildCustomPrayers(true);
         }
      }
   }

   private void rebuildCustomPrayers(boolean unlocked) {
      boolean bookOpen = this.isInterfaceOpen(3038);
      boolean quickOpen = this.isInterfaceOpen(3039);
      if (bookOpen || quickOpen) {
         List<CustomPrayer> model = this.getCustomPrayerModel();
         if (!model.isEmpty()) {
            int[] order = this.customPrayerOrder(model);
            int var9;
            int componentId;
            int quickSlot;
            if (bookOpen) {
               int index = 0;
               int[] var7 = order;
               int var8 = order.length;

               for(var9 = 0; var9 < var8; ++var9) {
                  componentId = var7[var9];
                  Widget prayerWidget = this.client.getWidget(3038, componentId);
                  if (prayerWidget != null && !prayerWidget.isHidden()) {
                     quickSlot = prayerWidget.getClickMask();
                     if (unlocked) {
                        quickSlot |= 1179648;
                     } else {
                        quickSlot &= -1179649;
                     }

                     prayerWidget.setClickMask(quickSlot);
                     prayerWidget.setPos(index % 5 * 36, index / 5 * 36);
                     prayerWidget.revalidate();
                     ++index;
                  }
               }

               this.createCustomWarning(unlocked);
            }

            if (quickOpen) {
               Widget container = this.client.getWidget(3039, 4);
               if (container != null) {
                  int index = 0;
                  int[] var18 = order;
                  var9 = order.length;

                  for(componentId = 0; componentId < var9; ++componentId) {
                     int componentId = var18[componentId];
                     quickSlot = customQuickSlot(model, componentId);
                     if (quickSlot >= 0) {
                        Widget hit = container.getChild(quickSlot);
                        if (hit != null && !hit.isHidden()) {
                           int x = index % 5 * 37;
                           int y = index / 5 * 37;
                           hit.setPos(x, y);
                           hit.revalidate();
                           moveCustomQuickChild(container, quickSlot + 64, x + 2, y + 2);
                           moveCustomQuickChild(container, quickSlot + 128, x - 1, y - 1);
                           ++index;
                        }
                     }
                  }
               }
            }

         }
      }
   }

   private static void moveCustomQuickChild(Widget container, int childId, int x, int y) {
      Widget child = container.getChild(childId);
      if (child != null) {
         child.setPos(x, y);
         child.revalidate();
      }

   }

   private void createCustomWarning(boolean unlocked) {
      Widget w = this.client.getWidget(3038, 0);
      if (w != null) {
         w.deleteAllChildren();
         if (unlocked) {
            Widget c = w.createChild(3);
            c.setHeightMode(1);
            c.setWidthMode(1);
            c.setTextColor(16711680);
            c.setFilled(true);
            c.setOpacity(220);
            c.revalidate();
         }

      }
   }

   private int[] customPrayerOrder(List<CustomPrayer> model) {
      int[] defaultOrder = model.stream().mapToInt((p) -> {
         return p.componentId;
      }).distinct().toArray();
      String stored = this.configManager.getConfiguration("prayer", "prayer_order_custom");
      if (stored != null && !stored.isEmpty()) {
         Set<Integer> valid = (Set)Arrays.stream(defaultOrder).boxed().collect(Collectors.toSet());
         Set<Integer> seen = new HashSet();
         List<Integer> result = new ArrayList(defaultOrder.length);
         String[] var7 = stored.split(",");
         int var8 = var7.length;

         int var9;
         for(var9 = 0; var9 < var8; ++var9) {
            String part = var7[var9];

            int id;
            try {
               id = Integer.parseInt(part.trim());
            } catch (NumberFormatException var13) {
               continue;
            }

            if (valid.contains(id) && seen.add(id)) {
               result.add(id);
            }
         }

         int[] var14 = defaultOrder;
         var8 = defaultOrder.length;

         for(var9 = 0; var9 < var8; ++var9) {
            int id = var14[var9];
            if (seen.add(id)) {
               result.add(id);
            }
         }

         return result.stream().mapToInt(Integer::intValue).toArray();
      } else {
         return defaultOrder;
      }
   }

   private void setCustomPrayerOrder(int[] order) {
      String s = (String)Arrays.stream(order).mapToObj(Integer::toString).collect(Collectors.joining(","));
      this.configManager.setConfiguration("prayer", "prayer_order_custom", s);
   }

   private static int customQuickSlot(List<CustomPrayer> model, int componentId) {
      Iterator var2 = model.iterator();

      CustomPrayer p;
      do {
         if (!var2.hasNext()) {
            return -1;
         }

         p = (CustomPrayer)var2.next();
      } while(p.componentId != componentId);

      return p.quickSlot;
   }

   private List<CustomPrayer> getCustomPrayerModel() {
      if (this.customPrayerModel != null) {
         return this.customPrayerModel;
      } else {
         List<Integer> rows = this.client.getDBTableRows(65420);
         if (rows != null && !rows.isEmpty()) {
            List<CustomPrayer> model = new ArrayList(rows.size());
            Iterator var3 = rows.iterator();

            while(var3.hasNext()) {
               int rowId = (Integer)var3.next();
               int componentId = this.customPrayerDbInt(rowId, 9);
               int quickSlot = this.customPrayerDbInt(rowId, 10);
               int level = this.customPrayerDbInt(rowId, 3);
               int sortPriority = Math.max(0, this.customPrayerDbInt(rowId, 22));
               if (componentId >= 0 && quickSlot >= 0) {
                  model.add(new CustomPrayer(componentId, quickSlot, level, sortPriority));
               }
            }

            model.sort(Comparator.comparingInt((p) -> {
               return p.sortPriority;
            }).thenComparingInt((p) -> {
               return p.level;
            }).thenComparingInt((p) -> {
               return p.componentId;
            }));
            this.customPrayerModel = model;
            return model;
         } else {
            return Collections.emptyList();
         }
      }
   }

   private int customPrayerDbInt(int rowId, int column) {
      try {
         Object[] field = this.client.getDBTableField(rowId, column, 0);
         if (field != null && field.length > 0 && field[0] != null) {
            return (Integer)field[0];
         }
      } catch (NullPointerException var4) {
      }

      return -1;
   }

   private boolean isInterfaceOpen(int interfaceId) {
      return this.client.getWidget(interfaceId, 0) != null;
   }

   @Subscribe
   public void onMenuOptionClicked(MenuOptionClicked menuOptionClicked) {
      if (this.reordering && menuOptionClicked.getMenuAction() == MenuAction.CC_OP && menuOptionClicked.getId() == 4 && ("Hide".equals(menuOptionClicked.getMenuOption()) || "Unhide".equals(menuOptionClicked.getMenuOption()))) {
         Widget widget = menuOptionClicked.getWidget();
         int prayerbook = this.client.getVarbitValue(14826);
         int prayerId = this.findPrayerIdFromComponent(prayerbook, widget);
         if (prayerId != -1) {
            widget = widget.getChild(1);
            boolean hidden = this.isHidden(prayerbook, prayerId);
            if (hidden) {
               widget.setOpacity(0);
               this.setHidden(prayerbook, prayerId, false);
            } else {
               widget.setOpacity(200);
               this.setHidden(prayerbook, prayerId, true);
            }
         }
      }

   }

   @Subscribe
   public void onProfileChanged(ProfileChanged e) {
      this.clientThread.invokeLater(this::redrawPrayers);
   }

   @Inject
   public PrayerReorder(Client client, ClientThread clientThread, ConfigManager configManager, ChatMessageManager chatMessageManager, MenuManager menuManager) {
      this.client = client;
      this.clientThread = clientThread;
      this.configManager = configManager;
      this.chatMessageManager = chatMessageManager;
      this.menuManager = menuManager;
   }

   private static final class CustomPrayer {
      private final int componentId;
      private final int quickSlot;
      private final int level;
      private final int sortPriority;

      CustomPrayer(int componentId, int quickSlot, int level, int sortPriority) {
         this.componentId = componentId;
         this.quickSlot = quickSlot;
         this.level = level;
         this.sortPriority = sortPriority;
      }
   }
}
