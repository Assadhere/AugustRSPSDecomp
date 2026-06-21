package net.runelite.client.plugins.bank;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Strings;
import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import com.google.inject.Provides;
import java.awt.event.KeyEvent;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.Nullable;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.EnumComposition;
import net.runelite.api.Item;
import net.runelite.api.ItemComposition;
import net.runelite.api.ItemContainer;
import net.runelite.api.MenuEntry;
import net.runelite.api.WidgetNode;
import net.runelite.api.events.ItemContainerChanged;
import net.runelite.api.events.MenuEntryAdded;
import net.runelite.api.events.MenuShouldLeftClick;
import net.runelite.api.events.ScriptCallbackEvent;
import net.runelite.api.events.ScriptPostFired;
import net.runelite.api.events.ScriptPreFired;
import net.runelite.api.events.WidgetLoaded;
import net.runelite.api.widgets.Widget;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.config.Keybind;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.game.ItemManager;
import net.runelite.client.input.KeyListener;
import net.runelite.client.input.KeyManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.util.QuantityFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@PluginDescriptor(
   name = "Bank",
   description = "Modifications to the banking interface",
   tags = {"grand", "exchange", "high", "alchemy", "prices", "deposit", "pin"}
)
public class BankPlugin extends Plugin {
   private static final Logger log = LoggerFactory.getLogger(BankPlugin.class);
   private static final String DEPOSIT_WORN = "Deposit worn items";
   private static final String DEPOSIT_INVENTORY = "Deposit inventory";
   private static final String EMPTY_CONTAINERS = "Empty containers";
   private static final String TOGGLE_PLACEHOLDERS = "Always set placeholders";
   private static final String SEED_VAULT_TITLE = "Seed Vault";
   private static final int POTION_STORE_TAB = 15;
   private static final String NUMBER_REGEX = "[0-9]+(\\.[0-9]+)?[kmb]?";
   private static final Pattern VALUE_SEARCH_PATTERN = Pattern.compile("^(?<mode>qty|ge|ha|alch)? *(?<individual>i|iv|individual|per)? *(((?<op>[<>=]|>=|<=) *(?<num>[0-9]+(\\.[0-9]+)?[kmb]?))|((?<num1>[0-9]+(\\.[0-9]+)?[kmb]?) *- *(?<num2>[0-9]+(\\.[0-9]+)?[kmb]?)))$", 2);
   @Inject
   private Client client;
   @Inject
   private ClientThread clientThread;
   @Inject
   private ItemManager itemManager;
   @Inject
   private BankConfig config;
   @Inject
   private BankSearch bankSearch;
   @Inject
   private KeyManager keyManager;
   private boolean forceRightClickFlag;
   private Multiset<Integer> itemQuantities;
   private String searchString;
   private ContainerPrices prices;
   private final KeyListener searchHotkeyListener = new KeyListener() {
      public void keyTyped(KeyEvent e) {
      }

      public void keyPressed(KeyEvent e) {
         Keybind keybind = BankPlugin.this.config.searchKeybind();
         if (keybind.matches(e)) {
            Widget bankContainer = BankPlugin.this.client.getWidget(786444);
            if (bankContainer != null && !bankContainer.isSelfHidden()) {
               BankPlugin.log.debug("Search hotkey pressed");
               BankPlugin.this.bankSearch.initSearch();
               e.consume();
            }

            Widget groupStorageSearchButton = BankPlugin.this.client.getWidget(47448080);
            if (groupStorageSearchButton != null) {
               BankPlugin.log.debug("Search hotkey pressed");
               BankPlugin.this.clientThread.invoke(() -> {
                  Widget searchButton = BankPlugin.this.client.getWidget(47448080);
                  if (searchButton != null && !searchButton.isHidden()) {
                     Object[] searchToggleArgs = searchButton.getOnOpListener();
                     if (searchToggleArgs != null) {
                        BankPlugin.this.client.createScriptEventBuilder(searchToggleArgs).setOp(1).build().run();
                     }
                  }
               });
               e.consume();
            }

            Widget seedVaultSearchButton = BankPlugin.this.client.getWidget(41353240);
            if (seedVaultSearchButton != null) {
               BankPlugin.log.debug("Search hotkey pressed");
               BankPlugin.this.clientThread.invoke(() -> {
                  Widget searchButton = BankPlugin.this.client.getWidget(41353240);
                  if (searchButton != null && !searchButton.isHidden()) {
                     BankPlugin.this.client.runScript(searchButton.getOnOpListener());
                  }
               });
               e.consume();
            }
         }

      }

      public void keyReleased(KeyEvent e) {
      }
   };

   @Provides
   BankConfig getConfig(ConfigManager configManager) {
      return (BankConfig)configManager.getConfig(BankConfig.class);
   }

   protected void startUp() {
      this.keyManager.registerKeyListener(this.searchHotkeyListener);
   }

   protected void shutDown() {
      this.keyManager.unregisterKeyListener(this.searchHotkeyListener);
      this.clientThread.invokeLater(() -> {
         this.bankSearch.reset(false);
      });
      this.forceRightClickFlag = false;
      this.itemQuantities = null;
      this.searchString = null;
   }

   @Subscribe
   public void onMenuShouldLeftClick(MenuShouldLeftClick event) {
      if (this.forceRightClickFlag) {
         this.forceRightClickFlag = false;
         MenuEntry[] menuEntries = this.client.getMenuEntries();
         MenuEntry[] var3 = menuEntries;
         int var4 = menuEntries.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            MenuEntry entry = var3[var5];
            if (entry.getOption().equals("Deposit worn items") && this.config.rightClickBankEquip() || entry.getOption().equals("Deposit inventory") && this.config.rightClickBankInventory() || entry.getOption().equals("Empty containers") && this.config.rightClickBankLoot() || entry.getTarget().contains("Always set placeholders") && this.config.rightClickPlaceholders()) {
               event.setForceRightClick(true);
               return;
            }
         }

      }
   }

   @Subscribe
   public void onMenuEntryAdded(MenuEntryAdded event) {
      if (event.getOption().equals("Deposit worn items") && this.config.rightClickBankEquip() || event.getOption().equals("Deposit inventory") && this.config.rightClickBankInventory() || event.getOption().equals("Empty containers") && this.config.rightClickBankLoot() || event.getTarget().contains("Always set placeholders") && this.config.rightClickPlaceholders()) {
         this.forceRightClickFlag = true;
      }

   }

   @Subscribe
   public void onScriptCallbackEvent(ScriptCallbackEvent event) {
      int[] intStack = this.client.getIntStack();
      Object[] objectStack = this.client.getObjectStack();
      int intStackSize = this.client.getIntStackSize();
      int objectStackSize = this.client.getObjectStackSize();
      switch (event.getEventName()) {
         case "bankSearchFilter":
            int itemId = intStack[intStackSize - 1];
            String search = (String)objectStack[objectStackSize - 1];
            if (this.valueSearch(itemId, search)) {
               intStack[intStackSize - 2] = 1;
            }
            break;
         case "bankpinButtonSetup":
            if (!this.config.bankPinKeyboard()) {
               return;
            }

            int compId = intStack[intStackSize - 2];
            int buttonId = intStack[intStackSize - 1];
            Widget button = this.client.getWidget(compId);
            Widget buttonRect = button.getChild(0);
            Object[] onOpListener = buttonRect.getOnOpListener();
            buttonRect.setOnKeyListener(new Object[]{(e) -> {
               int typedChar = e.getTypedKeyChar() - 48;
               if (typedChar == buttonId) {
                  log.debug("Bank pin keypress");
                  this.client.runScript(onOpListener);
                  this.client.setVarcIntValue(187, this.client.getGameCycle() + 1);
               }
            }});
      }

   }

   @Subscribe
   public void onWidgetLoaded(WidgetLoaded event) {
      if (event.getGroupId() == 631 && this.config.seedVaultValue()) {
         this.clientThread.invokeLater(this::updateSeedVaultTotal);
      }

   }

   @Subscribe(
      priority = 1.0F
   )
   public void onScriptPreFired(ScriptPreFired event) {
      if (event.getScriptId() == 505) {
         if (this.client.getVarbitValue(4150) != 15) {
            this.prices = this.getWidgetContainerPrices(786444, 95);
         }
      } else if (event.getScriptId() == 4212) {
         Object text = event.getScriptEvent().getArguments()[1];
         if (((String)text).startsWith("Want more bank space?") && this.config.blockJagexAccountAd()) {
            WidgetNode wn = (WidgetNode)this.client.getComponentTable().get(786534L);
            this.clientThread.invokeAtTickEnd(() -> {
               this.client.closeInterface(wn, true);
            });
         }
      }

   }

   @Subscribe
   public void onScriptPostFired(ScriptPostFired event) {
      int scriptId = event.getScriptId();
      Widget bankTitle;
      if (scriptId != 6080 && scriptId != 6555) {
         String var10001;
         if (scriptId == 505) {
            if (this.prices != null) {
               bankTitle = this.client.getWidget(786435);
               var10001 = bankTitle.getText();
               bankTitle.setText(var10001 + this.createValueText(this.prices.getGePrice(), this.prices.getHighAlchPrice()));
            }
         } else if (scriptId == 283) {
            String inputText = this.client.getVarcStrValue(359);
            if (this.searchString != inputText && this.client.getGameCycle() % 40 != 0) {
               ClientThread var10000 = this.clientThread;
               BankSearch var7 = this.bankSearch;
               Objects.requireNonNull(var7);
               var10000.invokeLater(var7::layoutBank);
               this.searchString = inputText;
            }
         } else if (scriptId == 5269) {
            ContainerPrices price = this.getWidgetContainerPrices(47448074, 659);
            if (price == null) {
               return;
            }

            Widget bankTitle = this.client.getWidget(47448066).getChild(1);
            var10001 = bankTitle.getText();
            bankTitle.setText(var10001 + this.createValueText(price.getGePrice(), price.getHighAlchPrice()));
         }
      } else {
         this.prices = this.getPotionStoragePrice();
         bankTitle = this.client.getWidget(786435);
         bankTitle.setText("Potion store " + this.createValueText(this.prices.getGePrice(), this.prices.getHighAlchPrice()));
      }

   }

   @Subscribe
   public void onItemContainerChanged(ItemContainerChanged event) {
      int containerId = event.getContainerId();
      if (containerId == 95) {
         this.itemQuantities = null;
      } else if (containerId == 626 && this.config.seedVaultValue()) {
         this.updateSeedVaultTotal();
      }

   }

   private String createValueText(long gePrice, long haPrice) {
      StringBuilder stringBuilder = new StringBuilder();
      if (this.config.showGE() && gePrice != 0L) {
         stringBuilder.append(" (");
         if (this.config.showHA()) {
            stringBuilder.append("GE: ");
         }

         if (this.config.showExact()) {
            stringBuilder.append(QuantityFormatter.formatNumber(gePrice));
         } else {
            stringBuilder.append(QuantityFormatter.quantityToStackSize(gePrice));
         }

         stringBuilder.append(')');
      }

      if (this.config.showHA() && haPrice != 0L) {
         stringBuilder.append(" (");
         if (this.config.showGE()) {
            stringBuilder.append("HA: ");
         }

         if (this.config.showExact()) {
            stringBuilder.append(QuantityFormatter.formatNumber(haPrice));
         } else {
            stringBuilder.append(QuantityFormatter.quantityToStackSize(haPrice));
         }

         stringBuilder.append(')');
      }

      return stringBuilder.toString();
   }

   private void updateSeedVaultTotal() {
      Widget titleContainer = this.client.getWidget(41353218);
      if (titleContainer != null) {
         Widget title = titleContainer.getChild(1);
         if (title != null) {
            ContainerPrices prices = this.calculate(this.getSeedVaultItems());
            if (prices != null) {
               String titleText = this.createValueText(prices.getGePrice(), prices.getHighAlchPrice());
               title.setText("Seed Vault" + titleText);
            }
         }
      }
   }

   private Item[] getSeedVaultItems() {
      ItemContainer itemContainer = this.client.getItemContainer(626);
      return itemContainer == null ? null : itemContainer.getItems();
   }

   @VisibleForTesting
   boolean valueSearch(int itemId, String str) {
      Matcher matcher = VALUE_SEARCH_PATTERN.matcher(str);
      if (!matcher.matches()) {
         return false;
      } else {
         if (this.itemQuantities == null) {
            this.itemQuantities = this.getBankItemSet();
         }

         ItemComposition itemComposition = this.itemManager.getItemComposition(itemId);
         int qty = matcher.group("individual") != null ? 1 : this.itemQuantities.count(itemId);
         long gePrice = (long)this.itemManager.getItemPrice(itemId) * (long)qty;
         long haPrice = (long)itemComposition.getHaPrice() * (long)qty;
         boolean isPlaceholder = itemComposition.getPlaceholderTemplateId() != -1;
         long value = Math.max(gePrice, haPrice);
         String mode = matcher.group("mode");
         if (mode != null) {
            switch (mode.toLowerCase()) {
               case "qty":
                  value = isPlaceholder ? 0L : (long)qty;
                  break;
               case "ge":
                  value = gePrice;
                  break;
               case "ha":
               case "alch":
                  value = haPrice;
            }
         }

         op = matcher.group("op");
         if (op != null) {
            long compare;
            try {
               compare = QuantityFormatter.parseQuantity(matcher.group("num"));
            } catch (ParseException var23) {
               return false;
            }

            switch (op) {
               case ">":
                  return value > compare;
               case "<":
                  return value < compare;
               case "=":
                  return value == compare;
               case ">=":
                  return value >= compare;
               case "<=":
                  return value <= compare;
            }
         }

         String num1 = matcher.group("num1");
         String num2 = matcher.group("num2");
         if (num1 != null && num2 != null) {
            long compare1;
            long compare2;
            try {
               compare1 = QuantityFormatter.parseQuantity(num1);
               compare2 = QuantityFormatter.parseQuantity(num2);
            } catch (ParseException var22) {
               return false;
            }

            return compare1 <= value && compare2 >= value;
         } else {
            return false;
         }
      }
   }

   private Multiset<Integer> getBankItemSet() {
      ItemContainer itemContainer = this.client.getItemContainer(95);
      if (itemContainer == null) {
         return HashMultiset.create();
      } else {
         Multiset<Integer> set = HashMultiset.create();
         Item[] var3 = itemContainer.getItems();
         int var4 = var3.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            Item item = var3[var5];
            if (item.getId() != 20594) {
               set.add(item.getId(), item.getQuantity());
            }
         }

         return set;
      }
   }

   @Nullable
   ContainerPrices calculate(@Nullable Item[] items) {
      if (items == null) {
         return null;
      } else {
         long ge = 0L;
         long alch = 0L;
         Item[] var6 = items;
         int var7 = items.length;

         for(int var8 = 0; var8 < var7; ++var8) {
            Item item = var6[var8];
            int qty = item.getQuantity();
            int id = item.getId();
            if (id > 0 && qty != 0) {
               alch += (long)this.getHaPrice(id) * (long)qty;
               ge += (long)this.itemManager.getItemPrice(id) * (long)qty;
            }
         }

         return new ContainerPrices(ge, alch);
      }
   }

   private int getHaPrice(int itemId) {
      switch (itemId) {
         case 995:
            return 1;
         case 13204:
            return 1000;
         default:
            return this.itemManager.getItemComposition(itemId).getHaPrice();
      }
   }

   private ContainerPrices getWidgetContainerPrices(int componentId, int inventoryID) {
      Widget widget = this.client.getWidget(componentId);
      ItemContainer itemContainer = this.client.getItemContainer(inventoryID);
      Widget[] children = widget.getChildren();
      ContainerPrices prices = null;
      if (itemContainer != null && children != null) {
         long geTotal = 0L;
         long haTotal = 0L;
         log.debug("Computing bank price of {} items", itemContainer.size());

         for(int i = 0; i < itemContainer.size(); ++i) {
            Widget child = children[i];
            if (child != null && !child.isSelfHidden() && child.getItemId() > -1) {
               int alchPrice = this.getHaPrice(child.getItemId());
               geTotal += (long)this.itemManager.getItemPrice(child.getItemId()) * (long)child.getItemQuantity();
               haTotal += (long)alchPrice * (long)child.getItemQuantity();
            }
         }

         prices = new ContainerPrices(geTotal, haTotal);
      }

      return prices;
   }

   private ContainerPrices getPotionStoragePrice() {
      HashMap<Integer, EnumComposition> potionMap = new HashMap();
      EnumComposition potionStorePotions = this.client.getEnum(4826);
      int[] var3 = potionStorePotions.getIntVals();
      int var4 = var3.length;

      int var5;
      int potionEnumId;
      EnumComposition potionEnum;
      int itemId;
      int i;
      for(var5 = 0; var5 < var4; ++var5) {
         potionEnumId = var3[var5];
         potionEnum = this.client.getEnum(potionEnumId);

         for(itemId = 1; itemId <= 4; ++itemId) {
            i = potionEnum.getIntValue(itemId);
            if (i > -1) {
               potionMap.put(i, potionEnum);
            }
         }
      }

      potionStorePotions = this.client.getEnum(4829);
      var3 = potionStorePotions.getIntVals();
      var4 = var3.length;

      for(var5 = 0; var5 < var4; ++var5) {
         potionEnumId = var3[var5];
         potionEnum = this.client.getEnum(potionEnumId);
         itemId = potionEnum.getIntValue(1);
         potionMap.put(itemId, potionEnum);
      }

      Widget w = this.client.getWidget(786483);
      Widget[] children = w.getDynamicChildren();
      long geTotal = 0L;
      long haTotal = 0L;

      for(i = 0; i + 4 < children.length; i += 5) {
         Widget wItem = children[i + 1];
         Widget wDoses = children[i + 3];
         if (wItem.getItemId() != -1 && !Strings.isNullOrEmpty(wDoses.getText())) {
            int itemId = wItem.getItemId();
            int doses = Integer.parseInt(wDoses.getText().split(": ")[1].replace(",", ""));
            EnumComposition potionEnum = (EnumComposition)potionMap.get(itemId);
            if (potionEnum != null) {
               int withdrawDoses;
               for(withdrawDoses = 1; withdrawDoses < 4 && potionEnum.getIntValue(withdrawDoses) != itemId; ++withdrawDoses) {
               }

               int qty = doses / withdrawDoses;
               log.debug("Potion store has {} of {} (doses={}, withdrawDoses={})", new Object[]{qty, itemId, doses, withdrawDoses});
               geTotal += (long)this.itemManager.getItemPrice(itemId) * (long)qty;
               haTotal += (long)this.getHaPrice(itemId) * (long)qty;
            }
         }
      }

      return new ContainerPrices(geTotal, haTotal);
   }
}
