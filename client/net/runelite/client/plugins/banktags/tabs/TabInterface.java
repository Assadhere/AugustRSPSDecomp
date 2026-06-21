package net.runelite.client.plugins.banktags.tabs;

import com.google.common.base.Strings;
import com.google.common.util.concurrent.Runnables;
import java.awt.Color;
import java.awt.Toolkit;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.IntPredicate;
import java.util.stream.Collectors;
import javax.inject.Inject;
import javax.inject.Singleton;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.Item;
import net.runelite.api.ItemContainer;
import net.runelite.api.MenuAction;
import net.runelite.api.MenuEntry;
import net.runelite.api.ScriptEvent;
import net.runelite.api.events.MenuEntryAdded;
import net.runelite.api.events.MenuOptionClicked;
import net.runelite.api.events.ScriptPostFired;
import net.runelite.api.events.ScriptPreFired;
import net.runelite.api.events.WidgetClosed;
import net.runelite.api.events.WidgetDrag;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetUtil;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.chat.ChatMessageManager;
import net.runelite.client.chat.QueuedMessage;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.game.ItemManager;
import net.runelite.client.game.chatbox.ChatboxItemSearch;
import net.runelite.client.game.chatbox.ChatboxPanelManager;
import net.runelite.client.plugins.bank.BankSearch;
import net.runelite.client.plugins.banktags.BankTagsConfig;
import net.runelite.client.plugins.banktags.BankTagsPlugin;
import net.runelite.client.plugins.banktags.TagManager;
import net.runelite.client.ui.JagexColors;
import net.runelite.client.util.ColorUtil;
import net.runelite.client.util.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
public class TabInterface {
   private static final Logger log = LoggerFactory.getLogger(TabInterface.class);
   public static final IntPredicate FILTERED_CHARS = (c) -> {
      return "</>:".indexOf(c) == -1;
   };
   private static final Color HILIGHT_COLOR;
   private static final String SCROLL_UP = "Scroll up";
   private static final String SCROLL_DOWN = "Scroll down";
   private static final String NEW_TAB = "New tag tab";
   private static final String REMOVE_TAB = "Delete tag tab";
   private static final String EXPORT_TAB = "Export tag tab";
   private static final String IMPORT_TAB = "Import tag tab";
   private static final String VIEW_TAB = "View tag tab";
   private static final String RENAME_TAB = "Rename tag tab";
   private static final String CHANGE_ICON = "Change icon";
   private static final String REMOVE_TAG = "Remove-tag";
   private static final String TAG_GEAR = "Tag-equipment";
   private static final String TAG_INVENTORY = "Tag-inventory";
   private static final String TAGTABS = "tagtabs";
   private static final String OPEN_TAB_MENU = "View tag tabs";
   static final String ENABLE_LAYOUT = "Enable layout";
   static final String DISABLE_LAYOUT = "Disable layout";
   static final String REMOVE_LAYOUT = "Remove-layout";
   static final String DUPLICATE_ITEM = "Duplicate-item";
   private static final int TAB_HEIGHT = 40;
   private static final int TAB_WIDTH = 39;
   private static final int BUTTON_HEIGHT = 20;
   private static final int MARGIN = 1;
   private static final int SCROLL_TICK = 500;
   private static final int INCINERATOR_WIDTH = 48;
   private static final int INCINERATOR_HEIGHT = 39;
   private static final int BANK_BOTTOM_OFFSET = 39;
   private static final int BANK_ITEM_WIDTH = 36;
   private static final int BANK_ITEM_HEIGHT = 32;
   private static final int BANK_ITEM_X_PADDING = 12;
   private static final int BANK_ITEM_Y_PADDING = 4;
   private static final int BANK_ITEMS_PER_ROW = 8;
   private static final int BANK_ITEM_START_X = 51;
   private static final int BANK_ITEM_START_Y = 0;
   private static final int TAB_OP_OPEN_TAG = 1;
   private static final int TAB_OP_CHANGE_ICON = 2;
   private static final int TAB_OP_LAYOUT = 3;
   private static final int TAB_OP_EXPORT_TAB = 4;
   private static final int TAB_OP_RENAME_TAB = 5;
   private static final int TAB_OP_DELETE_TAB = 6;
   private static final int NEWTAB_OP_NEW_TAB = 1;
   private static final int NEWTAB_OP_IMPORT_TAB = 2;
   private static final int NEWTAB_OP_OPEN_TAB_MENU = 3;
   private static final int TAGTAB_CHILD_OFFSET = 4;
   private final Client client;
   private final ClientThread clientThread;
   private final BankTagsPlugin plugin;
   private final ItemManager itemManager;
   private final TagManager tagManager;
   private final TabManager tabManager;
   private final LayoutManager layoutManager;
   private final ChatboxPanelManager chatboxPanelManager;
   private final BankTagsConfig config;
   private final BankSearch bankSearch;
   private final ChatboxItemSearch searchProvider;
   private final ChatMessageManager chatMessageManager;
   private boolean enabled;
   private String activeTag;
   private Layout activeLayout;
   private int activeOptions;
   private boolean tagTabActive;
   private int tagTabFirstChildIdx = -1;
   private int tabScrollOffset;
   private Instant startScroll = Instant.now();
   private int tabCount;
   private Widget parent;
   private Widget scrollComponent;
   private Widget upButton;
   private Widget downButton;
   private Widget newTab;

   @Inject
   private TabInterface(Client client, ClientThread clientThread, BankTagsPlugin plugin, ItemManager itemManager, TagManager tagManager, TabManager tabManager, LayoutManager layoutManager, ChatboxPanelManager chatboxPanelManager, BankTagsConfig config, BankSearch bankSearch, ChatboxItemSearch searchProvider, ChatMessageManager chatMessageManager) {
      this.client = client;
      this.clientThread = clientThread;
      this.plugin = plugin;
      this.itemManager = itemManager;
      this.tagManager = tagManager;
      this.tabManager = tabManager;
      this.layoutManager = layoutManager;
      this.chatboxPanelManager = chatboxPanelManager;
      this.config = config;
      this.bankSearch = bankSearch;
      this.searchProvider = searchProvider;
      this.chatMessageManager = chatMessageManager;
   }

   @Subscribe
   public void onScriptPreFired(ScriptPreFired event) {
      if (event.getScriptId() == 274) {
         boolean tabs = this.config.tabs();
         if (this.enabled != tabs) {
            this.enabled = tabs;
            if (tabs) {
               this.init();
            } else {
               this.deinit();
            }
         }
      } else if (event.getScriptId() == 840 && this.enabled) {
         int[] intStack = this.client.getIntStack();
         int intStackSize = this.client.getIntStackSize();
         Widget w = this.client.getWidget(intStack[intStackSize - 5]);
         int width = intStack[intStackSize - 4];
         int height = intStack[intStackSize - 3];
         if (w.getWidth() != width || w.getHeight() != height) {
            log.debug("Bank resize!");
            this.clientThread.invokeLater(() -> {
               this.repositionButtons();
               this.layoutTabs();
            });
         }
      } else if (event.getScriptId() == 281) {
         if (this.activeTag != null || this.tagTabActive) {
            this.closeTag(false);
         }
      } else if (event.getScriptId() == 505 && this.enabled) {
         if (this.tagTabActive) {
            this.hideBank();
         }

         this.repositionButtons();
         this.rebuildTabs();
         int tagTabHeight = this.rebuildTagTabTab();
         Widget bankTitle;
         if (this.tagTabActive) {
            bankTitle = this.client.getWidget(786435);
            bankTitle.setText("Tag tab tab");
         } else if (this.activeTag != null) {
            bankTitle = this.client.getWidget(786435);
            bankTitle.setText("Tag tab <col=ff0000>" + this.activeTag + "</col>");
         }

         if (this.tagTabActive) {
            int[] intStack = this.client.getIntStack();
            int intStackSize = this.client.getIntStackSize();
            intStack[intStackSize - 9] = tagTabHeight;
         }
      }

   }

   @Subscribe
   private void onScriptPostFired(ScriptPostFired ev) {
      if (ev.getScriptId() == 9221) {
         this.repositionButtons();
         this.layoutTabs();
      }

   }

   @Subscribe
   public void onWidgetClosed(WidgetClosed event) {
      if (event.getGroupId() == 12 && event.isUnload()) {
         this.enabled = false;
         this.upButton = this.downButton = this.newTab = this.scrollComponent = this.parent = null;
         this.activeTag = null;
         this.activeLayout = null;
         this.activeOptions = 0;
         this.plugin.openTag((String)null, (Layout)null);
         this.tagTabActive = false;
         this.tagTabFirstChildIdx = -1;
      }

   }

   private void init() {
      assert this.parent == null;

      this.parent = this.client.getWidget(786441);
      this.scrollComponent = this.parent.createChild(-1, 4);
      this.scrollComponent.setHasListener(true);
      this.scrollComponent.setNoScrollThrough(true);
      this.scrollComponent.setOnScrollWheelListener(new Object[]{(event) -> {
         this.scrollTab(event.getMouseY());
      }});
      this.upButton = this.createGraphic(this.parent, "", TabSprites.UP_ARROW.getSpriteId(), -1, 39, 20, 1, 0);
      this.upButton.setAction(1, "Scroll up");
      int clickmask = this.upButton.getClickMask();
      clickmask |= 1048576;
      this.upButton.setClickMask(clickmask);
      this.upButton.setHasListener(true);
      this.upButton.setOnOpListener(new Object[]{(event) -> {
         this.scrollTab(-1);
      }});
      this.downButton = this.createGraphic(this.parent, "", TabSprites.DOWN_ARROW.getSpriteId(), -1, 39, 20, 1, 0);
      this.downButton.setAction(1, "Scroll down");
      clickmask = this.downButton.getClickMask();
      clickmask |= 1048576;
      this.downButton.setClickMask(clickmask);
      this.downButton.setHasListener(true);
      this.downButton.setOnOpListener(new Object[]{(event) -> {
         this.scrollTab(1);
      }});
      this.newTab = this.createGraphic(this.parent, "", TabSprites.NEW_TAB.getSpriteId(), -1, 39, 39, 1, 0);
      this.newTab.setHasListener(true);
      this.newTab.setAction(1, "New tag tab");
      this.newTab.setAction(2, "Import tag tab");
      this.newTab.setAction(3, "View tag tabs");
      this.newTab.setOnOpListener(new Object[]{this::handleNewTab});
      this.tabManager.clear();
      this.tabManager.loadAllTabNames().forEach(this::loadTab);
      this.tabScrollOffset = this.config.position();
      this.scrollTab(0);
      if (this.config.rememberTab() && !Strings.isNullOrEmpty(this.config.tab())) {
         this.client.setVarbit(4150, 0);
         String tab = this.config.tab();
         Layout layout = this.layoutManager.loadLayout(tab);
         this.plugin.openTag(tab, layout);
      }

   }

   public void deinit() {
      this.enabled = false;
      this.activeTag = null;
      this.activeLayout = null;
      this.activeOptions = 0;
      this.plugin.openTag((String)null, (Layout)null);
      this.upButton = this.downButton = this.newTab = this.scrollComponent = null;
      if (this.parent != null) {
         this.parent.deleteAllChildren();
         this.parent = null;
      }

      this.tabManager.clear();
   }

   private void handleDeposit(MenuOptionClicked event, boolean inventory) {
      ItemContainer container = this.client.getItemContainer(inventory ? 93 : 94);
      if (container != null) {
         List<Integer> items = (List)Arrays.stream(container.getItems()).filter(Objects::nonNull).map(Item::getId).filter((id) -> {
            return id != -1;
         }).collect(Collectors.toList());
         if (Strings.isNullOrEmpty(event.getMenuTarget())) {
            this.chatboxPanelManager.openTextInput((inventory ? "Inventory" : "Equipment") + " tags:").addCharValidator(FILTERED_CHARS).onDone((newTags) -> {
               this.clientThread.invoke(() -> {
                  List<String> tags = Text.fromCSV(newTags.toLowerCase());
                  Iterator var4 = items.iterator();

                  while(var4.hasNext()) {
                     Integer item = (Integer)var4.next();
                     this.tagManager.addTags(item, tags, false);
                  }

                  this.reloadActiveTab();
               });
            }).build();
         } else {
            if (this.activeTag != null && Text.removeTags(event.getMenuTarget()).equals(this.activeTag)) {
               Iterator var5 = items.iterator();

               while(var5.hasNext()) {
                  Integer item = (Integer)var5.next();
                  this.tagManager.addTag(item, this.activeTag, false);
               }

               this.reloadActiveTab();
            }

         }
      }
   }

   private void handleNewTab(ScriptEvent event) {
      switch (event.getOp() - 1) {
         case 1:
            this.chatboxPanelManager.openTextInput("Tag name").addCharValidator(FILTERED_CHARS).onDone((tagName) -> {
               this.clientThread.invoke(() -> {
                  if (!Strings.isNullOrEmpty(tagName)) {
                     this.loadTab(tagName);
                     this.tabManager.save();
                     this.repositionButtons();
                     this.rebuildTabs();
                     this.rebuildTagTabTab();
                  }

               });
            }).build();
            break;
         case 2:
            try {
               String dataString = Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor).toString().trim();
               Iterator<String> dataIter = Text.fromCSV(dataString).iterator();
               TagTab tab = dataString.startsWith("banktaglayoutsplugin") ? this.importBtlTag(dataIter) : this.importTag(dataIter);
               if (tab == null) {
                  this.sendChatMessage("Failed to import tag tab from clipboard, invalid format.");
                  return;
               }

               this.tabManager.add(tab);
               this.tabManager.save();
               this.repositionButtons();
               this.rebuildTabs();
               this.rebuildTagTabTab();
               if (tab.getTag().equals(this.activeTag)) {
                  this.bankSearch.reset(true);
               }

               this.sendChatMessage("Tag tab '" + tab.getTag() + "' has been imported from your clipboard!");
            } catch (NoSuchElementException | IOException | NumberFormatException | UnsupportedFlavorException var5) {
               Exception ex = var5;
               log.debug("failed to import tab", ex);
               this.sendChatMessage("Failed to import tag tab from clipboard, invalid format.");
            }
            break;
         case 3:
            this.client.setVarbit(4150, 0);
            this.plugin.openTag("tagtabs", (Layout)null, 0);
      }

   }

   private TagTab importTag(Iterator<String> dataIter) {
      String name = (String)dataIter.next();
      if ("banktags".equals(name)) {
         dataIter.next();
         name = (String)dataIter.next();
      }

      StringBuilder sb = new StringBuilder();
      char[] var4 = name.toCharArray();
      int var5 = var4.length;

      int idx;
      int itemId;
      for(idx = 0; idx < var5; ++idx) {
         itemId = var4[idx];
         if (FILTERED_CHARS.test(itemId)) {
            sb.append((char)itemId);
         }
      }

      if (sb.length() == 0) {
         return null;
      } else {
         name = sb.toString();
         TagTab tab = this.tabManager.load(name);
         tab.setIconItemId(Integer.parseInt((String)dataIter.next()));

         while(dataIter.hasNext()) {
            String token = (String)dataIter.next();
            if ("layout".equals(token)) {
               break;
            }

            idx = Integer.parseInt(token);
            this.tagManager.addTag(idx, name, idx < 0);
         }

         if (dataIter.hasNext()) {
            Layout l = new Layout(name);

            while(dataIter.hasNext()) {
               idx = Integer.parseInt((String)dataIter.next());
               itemId = Integer.parseInt((String)dataIter.next());
               l.setItemAtPos(itemId, idx);
               this.tagManager.addTag(itemId, name, false);
            }

            this.layoutManager.saveLayout(l);
         }

         return tab;
      }
   }

   private TagTab importBtlTag(Iterator<String> dataIter) {
      String header = (String)dataIter.next();
      String name = header.substring("banktaglayoutsplugin:".length());
      StringBuilder sb = new StringBuilder();
      char[] var5 = name.toCharArray();
      int var6 = var5.length;

      int itemId;
      for(int var7 = 0; var7 < var6; ++var7) {
         itemId = var5[var7];
         if (FILTERED_CHARS.test(itemId)) {
            sb.append((char)itemId);
         }
      }

      if (sb.length() == 0) {
         return null;
      } else {
         name = sb.toString();
         TagTab tab = this.tabManager.load(name);
         Layout l = new Layout(name);

         String token;
         while(dataIter.hasNext()) {
            token = (String)dataIter.next();
            if (token.startsWith("banktag:")) {
               break;
            }

            String[] s = token.split(":");
            int itemId = Integer.parseInt(s[0]);
            int idx = Integer.parseInt(s[1]);
            l.setItemAtPos(itemId, idx);
         }

         tab.setIconItemId(Integer.parseInt((String)dataIter.next()));

         while(dataIter.hasNext()) {
            token = (String)dataIter.next();
            itemId = Integer.parseInt(token);
            this.tagManager.addTag(itemId, name, itemId < 0);
         }

         this.layoutManager.saveLayout(l);
         return tab;
      }
   }

   private void opTagTab(ScriptEvent event) {
      String tag;
      switch (event.getOp() - 1) {
         case 1:
            if (this.client.getVarbitValue(4150) == 15) {
               log.debug("Closing potion store");
               this.client.menuAction(-1, 786551, MenuAction.CC_OP, 1, -1, "Potion store", "");
            }

            this.client.setVarbit(4150, 0);
            Widget clicked = event.getSource();
            String tag = Text.removeTags(clicked.getName());
            if (tag.equals(this.activeTag)) {
               this.closeTag(true);
            } else {
               Layout layout = this.layoutManager.loadLayout(tag);
               this.plugin.openTag(tag, layout);
            }

            this.client.playSoundEffect(2266);
            break;
         case 2:
            tag = Text.removeTags(event.getOpbase());
            this.searchProvider.tooltipText("Change icon (" + tag + ")").onItemSelected((itemId) -> {
               TagTab tab = this.tabManager.find(tag);
               if (tab != null) {
                  tab.setIconItemId(itemId);
                  this.tabManager.save();
                  this.clientThread.invokeLater(() -> {
                     this.rebuildTabs();
                     this.rebuildTagTabTab();
                  });
               }

            }).build();
            break;
         case 3:
            tag = Text.removeTags(event.getSource().getName());
            Layout layout = this.layoutManager.loadLayout(tag);
            if (layout == null) {
               layout = new Layout(tag);
               this.layoutManager.saveLayout(layout);
               this.sendChatMessage("Tag tab '" + tag + "' is now in layout mode. You may reorder the items without changing their order in the bank.");
            } else {
               this.layoutManager.removeLayout(tag);
               layout = null;
               this.sendChatMessage("Tag tab '" + tag + "' is no longer in layout mode");
            }

            if (tag.equals(this.activeTag)) {
               this.plugin.openTag(tag, layout);
            }

            this.bankSearch.layoutBank();
            break;
         case 4:
            List<String> data = new ArrayList();
            String tag = Text.removeTags(event.getOpbase());
            TagTab tagTab = this.tabManager.find(tag);
            Layout layout = this.layoutManager.loadLayout(tag);
            data.add("banktags");
            data.add("1");
            data.add(tagTab.getTag());
            data.add(String.valueOf(tagTab.getIconItemId()));
            Iterator var7 = this.tagManager.getItemsForTag(tagTab.getTag()).iterator();

            while(true) {
               Integer item;
               do {
                  if (!var7.hasNext()) {
                     if (layout != null) {
                        data.add("layout");
                        int[] l = layout.getLayout();

                        for(int idx = 0; idx < l.length; ++idx) {
                           if (l[idx] != -1) {
                              data.add(String.valueOf(idx));
                              data.add(String.valueOf(l[idx]));
                           }
                        }
                     }

                     StringSelection stringSelection = new StringSelection(Text.toCSV(data));
                     Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringSelection, (ClipboardOwner)null);
                     this.sendChatMessage("Tag tab '" + tagTab.getTag() + "' has been copied to your clipboard!");
                     return;
                  }

                  item = (Integer)var7.next();
               } while(layout != null && layout.count(item) != 0);

               data.add(String.valueOf(item));
            }
         case 5:
            String renameTarget = Text.standardize(event.getOpbase());
            this.renameTab(renameTarget);
            break;
         case 6:
            tag = Text.standardize(event.getOpbase());
            this.chatboxPanelManager.openTextMenuInput("Delete " + tag).option("1. Tab and tag from all items", () -> {
               this.clientThread.invoke(() -> {
                  this.tagManager.removeTag(tag);
                  this.deleteTab(tag);
               });
            }).option("2. Only tab", () -> {
               this.clientThread.invoke(() -> {
                  this.deleteTab(tag);
               });
            }).option("3. Cancel", Runnables.doNothing()).build();
      }

   }

   @Subscribe
   private void onMenuEntryAdded(MenuEntryAdded event) {
      if ((this.activeOptions & 1) != 0 && event.getActionParam1() == 786444 && (event.getOption().equals("Examine") || event.getOption().equals("Withdraw-All-but-1") && !this.client.getItemContainer(95).contains(event.getItemId()))) {
         int index = event.getOption().equals("Examine") ? -1 : -2;
         if (this.activeLayout != null) {
            this.client.createMenuEntry(index).setParam0(event.getActionParam0()).setParam1(event.getActionParam1()).setTarget(event.getTarget()).setOption("Duplicate-item").setType(MenuAction.RUNELITE).setIdentifier(event.getIdentifier()).setItemId(event.getItemId()).onClick(this::opDuplicateItem);
         }

         if (this.activeLayout != null && this.activeLayout.count(this.itemManager.canonicalize(event.getItemId())) > 1) {
            this.client.createMenuEntry(index).setParam0(event.getActionParam0()).setParam1(event.getActionParam1()).setTarget(event.getTarget()).setOption("Remove-layout").setType(MenuAction.RUNELITE).setIdentifier(event.getIdentifier()).onClick(this::opRemoveLayout);
         } else {
            boolean hidden = this.tagManager.isHidden(this.activeTag);
            this.client.createMenuEntry(-1).setParam0(event.getActionParam0()).setParam1(event.getActionParam1()).setTarget(event.getTarget()).setOption(!hidden && (this.activeOptions & 2) == 0 ? "Remove-tag (" + this.activeTag + ")" : "Remove-tag").setType(MenuAction.RUNELITE).setIdentifier(event.getIdentifier()).setItemId(event.getItemId()).onClick((e) -> {
               int itemId = e.getItemId();
               if (this.activeLayout != null) {
                  this.activeLayout.removeItem(itemId);
                  this.layoutManager.saveLayout(this.activeLayout);
               }

               this.tagManager.removeTag(itemId, this.activeTag);
               this.bankSearch.layoutBank();
            });
         }
      } else if (this.activeTag != null && event.getActionParam1() == 786444 && event.getOption().equals("Duplicate-item")) {
         event.getMenuEntry().setType(MenuAction.RUNELITE_LOW_PRIORITY);
         event.getMenuEntry().onClick(this::opDuplicateItem);
      }

      if (this.activeTag != null && event.getActionParam1() == 786444 && event.getOption().equals("Remove-layout")) {
         event.getMenuEntry().setType(MenuAction.RUNELITE_LOW_PRIORITY);
         event.getMenuEntry().onClick(this::opRemoveLayout);
      } else if (event.getActionParam1() == 786473 && event.getOption().equals("Deposit inventory")) {
         this.createMenuEntry(event, "Tag-inventory", event.getTarget());
         if (this.activeTag != null && !this.tagManager.isHidden(this.activeTag) && (this.activeOptions & 3) == 1) {
            this.createMenuEntry(event, "Tag-inventory", ColorUtil.wrapWithColorTag(this.activeTag, HILIGHT_COLOR));
         }
      } else if (event.getActionParam1() == 786475 && event.getOption().equals("Deposit worn items")) {
         this.createMenuEntry(event, "Tag-equipment", event.getTarget());
         if (this.activeTag != null && !this.tagManager.isHidden(this.activeTag) && (this.activeOptions & 3) == 1) {
            this.createMenuEntry(event, "Tag-equipment", ColorUtil.wrapWithColorTag(this.activeTag, HILIGHT_COLOR));
         }
      }

      this.layoutManager.onMenuEntryAdded(event, this);
   }

   private void opDuplicateItem(MenuEntry e) {
      int id = this.itemManager.canonicalize(e.getItemId());
      log.debug("Duplicate item {} at {}", this.itemManager.getItemComposition(id).getName(), e.getParam0());
      this.activeLayout.addItemAfter(id, e.getParam0());
      this.layoutManager.saveLayout(this.activeLayout);
      this.bankSearch.layoutBank();
   }

   private void opRemoveLayout(MenuEntry e) {
      this.activeLayout.removeItemAtPos(e.getParam0());
      this.layoutManager.saveLayout(this.activeLayout);
      this.bankSearch.layoutBank();
   }

   @Subscribe(
      priority = -1.0F
   )
   public void onMenuOptionClicked(MenuOptionClicked event) {
      if (this.chatboxPanelManager.getCurrentInput() != null && event.getWidget() != null && !event.getMenuOption().equals("Scroll up") && !event.getMenuOption().equals("Scroll down")) {
         int interfaceId = WidgetUtil.componentToInterface(event.getWidget().getId());
         if (interfaceId == 12 || interfaceId == 15) {
            this.chatboxPanelManager.close();
         }
      }

      MenuEntry menuEntry = event.getMenuEntry();
      if (event.getMenuOption().startsWith("View tab") || event.getMenuOption().equals("View all items") || menuEntry.getType() == MenuAction.CC_OP && menuEntry.getParam1() == 786551) {
         this.closeTag(false);
      } else if (event.getMenuAction() == MenuAction.RUNELITE && (event.getParam1() == 786473 && event.getMenuOption().equals("Tag-inventory") || event.getParam1() == 786475 && event.getMenuOption().equals("Tag-equipment"))) {
         this.handleDeposit(event, event.getParam1() == 786473);
      }

      this.layoutManager.onMenuOptionClicked(event);
   }

   @Subscribe
   public void onWidgetDrag(WidgetDrag event) {
      if (this.enabled) {
         Widget draggedOn = this.client.getDraggedOnWidget();
         Widget draggedWidget = this.client.getDraggedWidget();
         if (draggedWidget.getId() == 786444 && this.activeTag != null && (this.activeLayout == null && this.config.preventTagTabDrags() || (this.activeOptions & 1) == 0)) {
            this.client.setDraggedOnWidget((Widget)null);
         }

         boolean shiftDown = this.client.isKeyPressed(81);
         if (draggedOn != null) {
            if (this.client.getMouseCurrentButton() == 0) {
               if (!this.tagTabActive && draggedWidget.getId() == 786444 && draggedWidget.getItemId() != -1 && draggedOn.getParent() == this.parent && draggedOn.getIndex() >= 4) {
                  log.debug("Dragged {} to tab {}", draggedWidget.getItemId(), Text.removeTags(draggedOn.getName()));
                  this.tagManager.addTag(draggedWidget.getItemId(), draggedOn.getName(), shiftDown);
                  this.reloadActiveTab();
               } else if ((!this.tagTabActive || draggedWidget.getId() != 786444 || draggedOn.getId() != 786444) && (draggedWidget.getParent() != this.parent || draggedOn.getParent() != this.parent || draggedWidget.getIndex() < 4 || draggedOn.getIndex() < 4)) {
                  this.rebuildTabs();
               } else {
                  log.debug("Reorder tag tab {} <-> {}", draggedWidget, draggedOn);
                  this.moveTagTab(draggedWidget, draggedOn);
               }
            } else if (draggedWidget.getItemId() != -1) {
               MenuEntry[] entries = this.client.getMenuEntries();
               if (entries.length > 0) {
                  MenuEntry entry = entries[entries.length - 1];
                  if (draggedWidget.getItemId() > 0 && entry.getOption().equals("View tag tab") && draggedOn.getId() != draggedWidget.getId()) {
                     String var10001 = Text.removeTags(entry.getTarget());
                     entry.setOption("tag:" + var10001 + (shiftDown ? "*" : ""));
                     entry.setTarget(draggedWidget.getName());
                  }

                  if (entry.getOption().equals("Scroll up")) {
                     this.scrollTick(-1);
                  } else if (entry.getOption().equals("Scroll down")) {
                     this.scrollTick(1);
                  }
               }
            }

         }
      }
   }

   private void moveTagTab(Widget source, Widget dest) {
      if (!Strings.isNullOrEmpty(dest.getName())) {
         if (this.client.getVarbitValue(3959) == 0) {
            this.tabManager.swap(source.getName(), dest.getName());
         } else {
            this.tabManager.insert(source.getName(), dest.getName());
         }

         this.tabManager.save();
         this.rebuildTabs();
         this.rebuildTagTabTab();
      }
   }

   private void addTabActions(TagTab tab, Widget w) {
      w.setAction(1, "View tag tab");
      w.setAction(2, "Change icon");
      if (!"tagtabs".equals(tab.getTag())) {
         Layout layout = this.layoutManager.loadLayout(tab.getTag());
         w.setAction(3, layout != null ? "Disable layout" : "Enable layout");
      }

      w.setAction(4, "Export tag tab");
      w.setAction(5, "Rename tag tab");
      w.setAction(6, "Delete tag tab");
      w.setHasListener(true);
      w.setOnOpListener(new Object[]{this::opTagTab});
   }

   private void addTabOptions(Widget w) {
      int clickmask = w.getClickMask();
      clickmask |= 131072;
      clickmask |= 1048576;
      w.setClickMask(clickmask);
      w.setDragDeadTime(5);
      w.setDragDeadZone(5);
      w.setItemQuantity(10000);
      w.setItemQuantityMode(0);
   }

   private void loadTab(String tag) {
      TagTab tagTab = this.tabManager.load(tag);
      this.tabManager.add(tagTab);
   }

   private void deleteTab(String tag) {
      if (tag.equals(this.activeTag)) {
         this.closeTag(true);
      }

      this.tabManager.remove(tag);
      this.tabManager.save();
      this.layoutManager.removeLayout(tag);
      this.repositionButtons();
      this.rebuildTabs();
      this.rebuildTagTabTab();
      this.scrollTab(0);
   }

   private void renameTab(String oldTag) {
      this.chatboxPanelManager.openTextInput("Enter new tag name for tag \"" + oldTag + "\":").addCharValidator(FILTERED_CHARS).onDone((newTag) -> {
         this.clientThread.invoke(() -> {
            if (!Strings.isNullOrEmpty(newTag) && !newTag.equalsIgnoreCase(oldTag)) {
               if (this.tabManager.find(newTag) == null) {
                  TagTab tagTab = this.tabManager.find(oldTag);
                  Layout layout = this.layoutManager.loadLayout(oldTag);
                  this.tabManager.remove(oldTag);
                  this.layoutManager.removeLayout(oldTag);
                  tagTab.setTag(newTag);
                  this.tabManager.add(tagTab);
                  this.tabManager.save();
                  if (this.activeTag.equals(oldTag)) {
                     this.activeTag = newTag;
                  }

                  if (layout != null) {
                     Layout newLayout = new Layout(newTag, layout.getLayout());
                     this.layoutManager.saveLayout(newLayout);
                  }

                  this.tagManager.renameTag(oldTag, newTag);
                  this.rebuildTabs();
                  this.rebuildTagTabTab();
                  this.reloadActiveTab();
               } else {
                  this.chatboxPanelManager.openTextMenuInput("The specified bank tag already exists.").option("1. Merge into existing tag \"" + newTag + "\".", () -> {
                     this.clientThread.invoke(() -> {
                        this.tagManager.renameTag(oldTag, newTag);
                        this.deleteTab(oldTag);
                        if (oldTag.equals(this.activeTag)) {
                           this.plugin.openBankTag(newTag);
                        } else {
                           this.reloadActiveTab();
                        }

                     });
                  }).option("2. Choose a different name.", () -> {
                     this.clientThread.invoke(() -> {
                        this.renameTab(oldTag);
                     });
                  }).build();
               }
            }

         });
      }).build();
   }

   private void scrollTick(int direction) {
      if (this.startScroll.until(Instant.now(), ChronoUnit.MILLIS) >= 500L) {
         this.startScroll = Instant.now();
         this.scrollTab(direction);
      }

   }

   private void scrollTab(int d) {
      this.tabScrollOffset += d;
      int maxScroll = this.tabManager.size() - this.tabCount;
      if (this.tabScrollOffset > maxScroll) {
         this.tabScrollOffset = maxScroll;
      }

      if (this.tabScrollOffset < 0) {
         this.tabScrollOffset = 0;
      }

      this.config.position(this.tabScrollOffset);
      this.layoutTabs();
   }

   public void openTag(String tag, Layout layout, int options, boolean relayout) {
      this.activeTag = tag;
      this.activeLayout = layout;
      this.activeOptions = options;
      this.tagTabActive = "tagtabs".equals(tag);
      this.config.tab(tag);
      if (relayout) {
         this.bankSearch.reset(true);
      }

   }

   public void closeTag(boolean relayout) {
      this.activeTag = null;
      this.activeLayout = null;
      this.activeOptions = 0;
      this.tagTabActive = false;
      this.plugin.openTag((String)null, (Layout)null);
      this.config.tab("");
      if (relayout) {
         this.bankSearch.reset(true);
      }

   }

   public void reloadActiveTab() {
      if (this.activeTag != null) {
         this.plugin.openBankTag(this.activeTag);
      }

   }

   private void repositionButtons() {
      int height = this.parent.getHeight() - 39;
      Widget widget = this.client.getWidget(786480);
      if (widget != null && !widget.isHidden()) {
         height = Math.min(height, widget.getRelativeY());
      }

      widget = this.client.getWidget(786478);
      if (widget != null && !widget.isHidden()) {
         height = Math.min(height, widget.getRelativeY());
      }

      Widget popupTab = this.client.getWidget(786479);
      if (popupTab != null && !popupTab.isHidden()) {
         widget = this.client.getWidget(786550);
         if (widget != null && !widget.isHidden()) {
            height = Math.min(height, popupTab.getRelativeY() + widget.getRelativeY());
         }

         widget = this.client.getWidget(786551);
         if (widget != null && !widget.isHidden()) {
            height = Math.min(height, popupTab.getRelativeY() + widget.getRelativeY());
         }
      }

      this.scrollComponent.setOriginalY(61);
      this.scrollComponent.setOriginalWidth(41);
      int tabLayerHeight = height - this.scrollComponent.getOriginalY() - 20;
      this.tabCount = tabLayerHeight / 41;
      this.scrollComponent.setOriginalHeight(this.tabCount * 41);
      this.scrollComponent.revalidate();
      this.upButton.setOriginalY(41);
      this.upButton.revalidate();
      this.downButton.setOriginalY(61 + this.tabCount * 41 + 1);
      this.downButton.revalidate();
   }

   private void rebuildTabs() {
      this.parent.setChildren((Widget[])Arrays.copyOf(this.parent.getChildren(), 4));
      List<TagTab> tabs = this.tabManager.getTabs();
      Iterator var2 = tabs.iterator();

      while(var2.hasNext()) {
         TagTab tab = (TagTab)var2.next();
         Widget background = this.createGraphic(this.parent, ColorUtil.wrapWithColorTag(tab.getTag(), HILIGHT_COLOR), (tab.getTag().equals(this.activeTag) ? TabSprites.TAB_BACKGROUND_ACTIVE : TabSprites.TAB_BACKGROUND).getSpriteId(), -1, 39, 40, 1, -1);
         this.addTabActions(tab, background);
         Widget icon = this.createGraphic(this.parent, ColorUtil.wrapWithColorTag(tab.getTag(), HILIGHT_COLOR), -1, tab.getIconItemId(), 36, 32, 4, -1);
         this.addTabOptions(icon);
      }

      this.layoutTabs();
   }

   private void layoutTabs() {
      Widget[] children = this.parent.getChildren();
      Widget draggedWidget = this.client.getDraggedWidget();

      int y;
      for(y = 4; y < children.length; ++y) {
         Widget child = children[y];
         if (draggedWidget != child) {
            child.setHidden(true);
         }
      }

      y = this.scrollComponent.getOriginalY();
      ++y;

      for(int i = this.tabScrollOffset; i < this.tabScrollOffset + this.tabCount && i * 2 + 1 < children.length - 4 && children[4 + i * 2] != null; ++i) {
         Widget background = children[4 + i * 2];
         background.setOriginalY(y);
         background.setHidden(false);
         background.revalidate();
         Widget icon = children[4 + i * 2 + 1];
         icon.setOriginalY(y + 4);
         icon.setHidden(false);
         icon.revalidate();
         y += 41;
      }

   }

   private int rebuildTagTabTab() {
      int itemX = 51;
      int itemY = 0;
      int rowIndex = 0;
      Widget parent = this.client.getWidget(786444);
      if (this.tagTabFirstChildIdx == -1) {
         this.tagTabFirstChildIdx = parent.getChildren().length;
      }

      int idx = this.tagTabFirstChildIdx;

      Widget w;
      while((w = parent.getChild(idx++)) != null) {
         w.setHidden(true);
      }

      if (!this.tagTabActive) {
         return 0;
      } else {
         idx = this.tagTabFirstChildIdx;
         Iterator var7 = this.tabManager.getTabs().iterator();

         while(var7.hasNext()) {
            TagTab tagTab = (TagTab)var7.next();
            Widget menu = parent.getChild(idx++);
            if (menu == null) {
               menu = parent.createChild(-1, 5);
               menu.setOriginalWidth(36);
               menu.setOriginalHeight(32);
            }

            menu.setHidden(false);
            menu.setOriginalX(itemX);
            menu.setOriginalY(itemY);
            menu.setName(ColorUtil.wrapWithColorTag(tagTab.getTag(), HILIGHT_COLOR));
            menu.setItemId(tagTab.getIconItemId());
            menu.setItemQuantity(-1);
            menu.setBorderType(1);
            this.addTabActions(tagTab, menu);
            this.addTabOptions(menu);
            menu.revalidate();
            ++rowIndex;
            if (rowIndex == 8) {
               itemX = 51;
               itemY += 36;
               rowIndex = 0;
            } else {
               itemX += 48;
            }
         }

         return itemY + 32;
      }
   }

   private void hideBank() {
      Widget parent = this.client.getWidget(786444);
      Widget[] var2 = parent.getChildren();
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         Widget w = var2[var4];
         w.setHidden(true);
      }

   }

   private Widget createGraphic(Widget container, String name, int spriteId, int itemId, int width, int height, int x, int y) {
      Widget widget = container.createChild(-1, 5);
      widget.setOriginalWidth(width);
      widget.setOriginalHeight(height);
      widget.setOriginalX(x);
      widget.setOriginalY(y);
      widget.setSpriteId(spriteId);
      if (itemId > -1) {
         widget.setItemId(itemId);
         widget.setItemQuantity(-1);
         widget.setBorderType(1);
      }

      widget.setName(name);
      widget.revalidate();
      return widget;
   }

   private void createMenuEntry(MenuEntryAdded event, String option, String target) {
      this.client.createMenuEntry(-1).setParam0(event.getActionParam0()).setParam1(event.getActionParam1()).setTarget(target).setOption(option).setType(MenuAction.RUNELITE).setIdentifier(event.getIdentifier());
   }

   private void sendChatMessage(String message) {
      this.chatMessageManager.queue(QueuedMessage.builder().type(ChatMessageType.CONSOLE).runeLiteFormattedMessage(message).build());
   }

   public String getActiveTag() {
      return this.activeTag;
   }

   public boolean isTagTabActive() {
      return this.tagTabActive;
   }

   static {
      HILIGHT_COLOR = JagexColors.MENU_TARGET;
   }
}
