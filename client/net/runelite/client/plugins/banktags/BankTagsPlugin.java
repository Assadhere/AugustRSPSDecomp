package net.runelite.client.plugins.banktags;

import com.google.common.collect.Lists;
import com.google.common.primitives.Shorts;
import com.google.inject.Binder;
import com.google.inject.Provides;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.inject.Inject;
import javax.inject.Named;
import net.runelite.api.Client;
import net.runelite.api.ItemComposition;
import net.runelite.api.MenuAction;
import net.runelite.api.MenuEntry;
import net.runelite.api.events.GrandExchangeSearched;
import net.runelite.api.events.MenuEntryAdded;
import net.runelite.api.events.ScriptCallbackEvent;
import net.runelite.api.widgets.Widget;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.EventBus;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.game.ItemManager;
import net.runelite.client.game.ItemVariationMapping;
import net.runelite.client.game.SpriteManager;
import net.runelite.client.game.chatbox.ChatboxPanelManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.bank.BankSearch;
import net.runelite.client.plugins.banktags.tabs.Layout;
import net.runelite.client.plugins.banktags.tabs.LayoutManager;
import net.runelite.client.plugins.banktags.tabs.TabInterface;
import net.runelite.client.plugins.banktags.tabs.TabSprites;
import net.runelite.client.util.Text;

@PluginDescriptor(
   name = "Bank Tags",
   description = "Enable tagging of bank items and searching of bank tags",
   tags = {"searching", "tagging"}
)
public class BankTagsPlugin extends Plugin implements BankTagsService {
   public static final String CONFIG_GROUP = "banktags";
   public static final String TAG_ICON_PREFIX = "icon_";
   public static final String TAG_TABS_CONFIG = "tagtabs";
   public static final String TAG_LAYOUT_PREFIX = "layout_";
   static final String ITEM_KEY_PREFIX = "item_";
   static final String TAG_HIDDEN_PREFIX = "hidden_";
   public static final String TAG_SEARCH = "tag:";
   private static final String EDIT_TAGS_MENU_OPTION = "Edit-tags";
   public static final String VAR_TAG_SUFFIX = "*";
   private static final int MAX_RESULT_COUNT = 250;
   private static final String SEARCH_BANK_INPUT_TEXT = "Show items whose names or tags contain the following text:<br>(To show only tagged items, start your search with 'tag:')";
   private static final String SEARCH_BANK_INPUT_TEXT_FOUND = "Show items whose names or tags contain the following text: (%d found)<br>(To show only tagged items, start your search with 'tag:')";
   public static final int BANK_ITEM_WIDTH = 36;
   public static final int BANK_ITEM_HEIGHT = 32;
   public static final int BANK_ITEM_X_PADDING = 12;
   public static final int BANK_ITEM_Y_PADDING = 4;
   public static final int BANK_ITEMS_PER_ROW = 8;
   public static final int BANK_ITEM_START_X = 51;
   public static final int BANK_ITEM_START_Y = 0;
   @Inject
   private ItemManager itemManager;
   @Inject
   private Client client;
   @Inject
   private ClientThread clientThread;
   @Inject
   private ChatboxPanelManager chatboxPanelManager;
   @Inject
   private TagManager tagManager;
   @Inject
   private TabInterface tabInterface;
   @Inject
   private LayoutManager layoutManager;
   @Inject
   private SpriteManager spriteManager;
   @Inject
   private ConfigManager configManager;
   @Inject
   private EventBus eventBus;
   @Inject
   private BankSearch bankSearch;
   @Inject
   private BankTagsConfig config;
   @Inject
   @Named("developerMode")
   boolean developerMode;
   private String activeTag;
   private BankTag activeBankTag;
   private Layout activeLayout;
   private int options;

   public void configure(Binder binder) {
      binder.bind(BankTagsService.class).toInstance(this);
   }

   @Provides
   BankTagsConfig getConfig(ConfigManager configManager) {
      return (BankTagsConfig)configManager.getConfig(BankTagsConfig.class);
   }

   public void resetConfiguration() {
      List<String> extraKeys = Lists.newArrayList(new String[]{"banktags.item_", "banktags.icon_", "banktags.tagtabs", "banktags.layout_"});
      Iterator var2 = extraKeys.iterator();

      while(var2.hasNext()) {
         String prefix = (String)var2.next();
         List<String> keys = this.configManager.getConfigurationKeys(prefix);
         Iterator var5 = keys.iterator();

         while(var5.hasNext()) {
            String key = (String)var5.next();
            String[] str = key.split("\\.", 2);
            if (str.length == 2) {
               this.configManager.unsetConfiguration(str[0], str[1]);
            }
         }
      }

      this.clientThread.invokeLater(this::reinitBank);
   }

   public void startUp() {
      this.cleanConfig();
      this.spriteManager.addSpriteOverrides(TabSprites.values());
      this.eventBus.register(this.tabInterface);
      this.layoutManager.register();
      this.clientThread.invokeLater(this::reinitBank);
   }

   public void shutDown() {
      this.eventBus.unregister((Object)this.tabInterface);
      this.layoutManager.unregister();
      this.clientThread.invokeLater(() -> {
         this.tabInterface.deinit();
         this.reinitBank();
      });
      this.spriteManager.removeSpriteOverrides(TabSprites.values());
   }

   private void reinitBank() {
      Widget w = this.client.getWidget(786433);
      if (w != null) {
         this.client.createScriptEventBuilder(w.getOnLoadListener()).setSource(w).build().run();
      }

   }

   /** @deprecated */
   @Deprecated
   private void cleanConfig() {
      this.removeInvalidTags("tagtabs");
      List<String> tags = this.configManager.getConfigurationKeys("banktags.item_");
      tags.forEach((s) -> {
         String[] split = s.split("\\.", 2);
         this.removeInvalidTags(split[1]);
      });
      List<String> icons = this.configManager.getConfigurationKeys("banktags.icon_");
      icons.forEach((s) -> {
         String[] split = s.split("\\.", 2);
         String replaced = split[1].replaceAll("[<>/]", "");
         if (!split[1].equals(replaced)) {
            String value = this.configManager.getConfiguration("banktags", split[1]);
            this.configManager.unsetConfiguration("banktags", split[1]);
            if (replaced.length() > "icon_".length()) {
               this.configManager.setConfiguration("banktags", replaced, value);
            }
         }

      });
   }

   /** @deprecated */
   @Deprecated
   private void removeInvalidTags(String key) {
      String value = this.configManager.getConfiguration("banktags", key);
      if (value != null) {
         String replaced = value.replaceAll("[<>:/]", "");
         if (!value.equals(replaced)) {
            replaced = Text.toCSV(Text.fromCSV(replaced));
            if (replaced.isEmpty()) {
               this.configManager.unsetConfiguration("banktags", key);
            } else {
               this.configManager.setConfiguration("banktags", key, replaced);
            }
         }

      }
   }

   @Subscribe
   public void onGrandExchangeSearched(GrandExchangeSearched event) {
      String input = this.client.getVarcStrValue(359);
      if (input.startsWith("tag:")) {
         event.consume();
         String tag = input.substring("tag:".length()).trim();
         Set<Integer> ids = (Set)this.tagManager.getItemsForTag(tag).stream().mapToInt(Math::abs).mapToObj(ItemVariationMapping::getVariations).flatMap(Collection::stream).distinct().filter((i) -> {
            return this.itemManager.getItemComposition(i).isTradeable();
         }).limit(250L).collect(Collectors.toCollection(TreeSet::new));
         this.client.setGeSearchResultIndex(0);
         this.client.setGeSearchResultCount(ids.size());
         this.client.setGeSearchResultIds(Shorts.toArray(ids));
      }
   }

   @Subscribe
   public void onScriptCallbackEvent(ScriptCallbackEvent event) {
      String eventName = event.getEventName();
      int[] intStack = this.client.getIntStack();
      Object[] objectStack = this.client.getObjectStack();
      int intStackSize = this.client.getIntStackSize();
      int objectStackSize = this.client.getObjectStackSize();
      int itemId;
      switch (eventName) {
         case "setSearchBankInputText":
            objectStack[objectStackSize - 1] = "Show items whose names or tags contain the following text:<br>(To show only tagged items, start your search with 'tag:')";
            break;
         case "setSearchBankInputTextFound":
            itemId = intStack[intStackSize - 1];
            objectStack[objectStackSize - 1] = String.format("Show items whose names or tags contain the following text: (%d found)<br>(To show only tagged items, start your search with 'tag:')", itemId);
            break;
         case "bankSearchFilter":
            itemId = intStack[intStackSize - 1];
            String searchfilter = (String)objectStack[objectStackSize - 1];
            BankTag tag = this.activeBankTag;
            boolean tagSearch = true;
            boolean bankOpen = this.client.getItemContainer(95) != null;
            if (tag == null || !bankOpen) {
               if (searchfilter.isEmpty()) {
                  return;
               }

               tagSearch = searchfilter.startsWith("tag:");
               if (tagSearch) {
                  searchfilter = searchfilter.substring("tag:".length()).trim();
               }

               tag = this.buildSearchFilterBankTag(searchfilter);
            }

            if (itemId == -1 && this.activeLayout != null) {
               return;
            }

            if (itemId > -1 && tag.contains(itemId)) {
               intStack[intStackSize - 2] = 1;
            } else if (tagSearch) {
               intStack[intStackSize - 2] = 0;
            }
            break;
         case "getSearchingTagTab":
            intStack[intStackSize - 1] = this.activeBankTag != null ? 1 : 0;
            break;
         case "bankBuildTab":
            if (this.activeBankTag != null && (this.tabInterface.isTagTabActive() || this.config.removeSeparators() || this.activeLayout != null)) {
               int[] stack = this.client.getIntStack();
               int sz = this.client.getIntStackSize();
               stack[sz - 1] = 1;
            }
      }

   }

   @Subscribe
   public void onMenuEntryAdded(MenuEntryAdded event) {
      if (event.getActionParam1() == 786444 && (event.getOption().equals("Examine") || event.getOption().equals("Withdraw-All-but-1") && !this.client.getItemContainer(95).contains(event.getItemId()))) {
         Widget container = this.client.getWidget(786444);
         Widget item = container.getChild(event.getActionParam0());
         int itemId = item.getItemId();
         Collection<String> tags = this.tagManager.getTags(itemId, false);
         tags.addAll(this.tagManager.getTags(itemId, true));
         int tagCount = (int)tags.stream().filter((tag) -> {
            return this.developerMode || !this.tagManager.isHidden(tag);
         }).count();
         String text = "Edit-tags";
         if (tagCount > 0) {
            text = text + " (" + tagCount + ")";
         }

         int index = event.getOption().equals("Examine") ? -1 : -2;
         this.client.createMenuEntry(index).setParam0(event.getActionParam0()).setParam1(event.getActionParam1()).setTarget(event.getTarget()).setOption(text).setType(MenuAction.RUNELITE).setIdentifier(event.getIdentifier()).setItemId(event.getItemId()).onClick(this::editTags);
      }

   }

   private void editTags(MenuEntry entry) {
      int itemId = entry.getItemId();
      ItemComposition itemComposition = this.itemManager.getItemComposition(itemId);
      String name = itemComposition.getName();
      List<String> tags = (List)this.tagManager.getTags(itemId, false).stream().filter((tag) -> {
         return this.developerMode || !this.tagManager.isHidden(tag);
      }).collect(Collectors.toList());
      Stream var10000 = this.tagManager.getTags(itemId, true).stream().filter((tag) -> {
         return this.developerMode || !this.tagManager.isHidden(tag);
      }).map((tag) -> {
         return tag + "*";
      });
      Objects.requireNonNull(tags);
      var10000.forEach(tags::add);
      String initialValue = Text.toCSV(tags);
      this.chatboxPanelManager.openTextInput(name + " tags:<br>(append * for variation tag)").addCharValidator(TabInterface.FILTERED_CHARS).value(initialValue).onDone((newValue) -> {
         this.clientThread.invoke(() -> {
            Collection<String> newTags = new ArrayList(Text.fromCSV(newValue.toLowerCase()));
            Collection<String> newVarTags = (Collection)(new ArrayList(newTags)).stream().filter((s) -> {
               return s.endsWith("*");
            }).map((s) -> {
               newTags.remove(s);
               return s.substring(0, s.length() - "*".length());
            }).collect(Collectors.toList());
            this.tagManager.setTagString(itemId, Text.toCSV(newTags), false);
            this.tagManager.setTagString(itemId, Text.toCSV(newVarTags), true);
            this.tabInterface.reloadActiveTab();
         });
      }).build();
   }

   @Subscribe
   public void onConfigChanged(ConfigChanged configChanged) {
      if (configChanged.getGroup().equals("banktags") && configChanged.getKey().equals("useTabs")) {
         this.clientThread.invokeLater(this::reinitBank);
      }

   }

   public void openTag(String tag, Layout layout) {
      this.openTag(tag, layout, 1);
   }

   public void openTag(String tag, Layout layout, int options) {
      if (tag == null) {
         this.activeTag = null;
         this.activeBankTag = null;
         this.activeLayout = null;
         this.options = 0;
      } else {
         this.activeTag = tag;
         this.activeBankTag = this.buildSearchFilterBankTag(tag);
         this.activeLayout = layout;
         this.options = options;
         this.tabInterface.openTag(tag, layout, options, true);
      }
   }

   private BankTag buildSearchFilterBankTag(String tag) {
      BankTag custom = this.tagManager.findTag(tag);
      return (itemId) -> {
         return this.tagManager.findTag(itemId, tag) || custom != null && custom.contains(itemId);
      };
   }

   public void openBankTag(String name) {
      this.openBankTag(name, 1);
   }

   public void openBankTag(String name, int options) {
      Layout layout = (options & 4) != 0 ? null : this.layoutManager.loadLayout(name);
      this.openTag(name, layout, options);
   }

   public void closeBankTag() {
      this.tabInterface.closeTag(false);
      this.activeTag = null;
      this.activeBankTag = null;
      this.activeLayout = null;
      this.options = 0;
      this.bankSearch.layoutBank();
   }

   public String getActiveTag() {
      return this.activeTag;
   }

   public BankTag getActiveBankTag() {
      return this.activeBankTag;
   }

   public Layout getActiveLayout() {
      return this.activeLayout;
   }

   public int getOptions() {
      return this.options;
   }
}
