package net.runelite.client.plugins.grandexchange;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.hash.Hasher;
import com.google.common.hash.Hashing;
import com.google.common.primitives.Shorts;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.google.inject.Provides;
import custom.UpdateGrandExchangeHistoryScript;
import custom.UpdateGrandExchangeItemDetailScript;
import custom.UpdateGrandExchangeMarketErrorScript;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.lang.reflect.Type;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.Enumeration;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ScheduledExecutorService;
import java.util.function.ToDoubleFunction;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import javax.inject.Inject;
import javax.swing.SwingUtilities;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.GrandExchangeOffer;
import net.runelite.api.GrandExchangeOfferState;
import net.runelite.api.ItemComposition;
import net.runelite.api.MenuAction;
import net.runelite.api.MenuEntry;
import net.runelite.api.events.ChatMessage;
import net.runelite.api.events.FocusChanged;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.GrandExchangeOfferChanged;
import net.runelite.api.events.GrandExchangeSearched;
import net.runelite.api.events.MenuEntryAdded;
import net.runelite.api.events.ScriptCallbackEvent;
import net.runelite.api.events.ScriptPostFired;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetUtil;
import net.runelite.client.Notifier;
import net.runelite.client.account.AccountSession;
import net.runelite.client.account.SessionManager;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.config.RuneLiteConfig;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.events.SessionClose;
import net.runelite.client.events.SessionOpen;
import net.runelite.client.game.ItemManager;
import net.runelite.client.game.ItemStats;
import net.runelite.client.input.KeyManager;
import net.runelite.client.input.MouseManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.ClientToolbar;
import net.runelite.client.ui.NavigationButton;
import net.runelite.client.util.ColorUtil;
import net.runelite.client.util.ImageUtil;
import net.runelite.client.util.LinkBrowser;
import net.runelite.client.util.OSType;
import net.runelite.client.util.QuantityFormatter;
import net.runelite.client.util.Text;
import net.runelite.http.api.ge.GrandExchangeTrade;
import net.runelite.http.api.worlds.WorldType;
import org.apache.commons.lang3.time.DurationFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@PluginDescriptor(
   name = "Grand Exchange",
   description = "Provide additional and/or easier access to Grand Exchange information",
   tags = {"external", "integration", "notifications", "panel", "prices", "trade"}
)
public class GrandExchangePlugin extends Plugin {
   private static final Logger log = LoggerFactory.getLogger(GrandExchangePlugin.class);
   @VisibleForTesting
   static final int GE_SLOTS = 8;
   private static final int GE_LOGIN_BURST_WINDOW = 2;
   private static final int GE_MAX_EXAMINE_LEN = 100;
   private static final int GE_MARKET_MAX_LIMIT = 250;
   private static final int AUGUST_RUNELITE_UI_INTERFACE = 3009;
   private static final int GE_MARKET_COMPONENT = 12;
   private static final int GE_HISTORY_TRIGGER = 1;
   private static final int GE_ITEM_DETAIL_TRIGGER = 2;
   private static final String BUY_LIMIT_GE_TEXT = "Buy limit: ";
   private static final String BUY_LIMIT_KEY = "buylimit";
   private static final Duration BUY_LIMIT_RESET = Duration.ofHours(4L);
   static final String SEARCH_GRAND_EXCHANGE = "Search Grand Exchange";
   private static final int MAX_RESULT_COUNT = 250;
   private static final Color FUZZY_HIGHLIGHT_COLOR = new Color(8388608);
   private static final int MAX_TRADE_HISTORY = 1024;
   private static final int MAX_TRADE_DAYS = 365;
   private NavigationButton button;
   private GrandExchangePanel panel;
   private boolean hotKeyPressed;
   @Inject
   private GrandExchangeInputListener inputListener;
   @Inject
   private ItemManager itemManager;
   @Inject
   private MouseManager mouseManager;
   @Inject
   private KeyManager keyManager;
   @Inject
   private Client client;
   @Inject
   private ClientToolbar clientToolbar;
   @Inject
   private GrandExchangeConfig config;
   @Inject
   private Notifier notifier;
   @Inject
   private SessionManager sessionManager;
   @Inject
   private ConfigManager configManager;
   @Inject
   private Gson gson;
   @Inject
   private RuneLiteConfig runeLiteConfig;
   @Inject
   private ClientThread clientThread;
   @Inject
   private ScheduledExecutorService scheduledExecutorService;
   @Inject
   private FuzzySearchScorer fuzzySearchScorer;
   @Inject
   private GrandExchangeClient grandExchangeClient;
   private int lastLoginTick;
   private boolean wasFuzzySearch;
   private String machineUuid;
   private long lastAccount;
   private int tradeSeq;
   private int geMarketRequestId;

   private SavedOffer getOffer(int slot) {
      String offer = this.configManager.getRSProfileConfiguration("geoffer", Integer.toString(slot));
      return offer == null ? null : (SavedOffer)this.gson.fromJson(offer, SavedOffer.class);
   }

   private void setOffer(int slot, SavedOffer offer) {
      this.configManager.setRSProfileConfiguration("geoffer", Integer.toString(slot), this.gson.toJson(offer));
   }

   private void deleteOffer(int slot) {
      this.configManager.unsetRSProfileConfiguration("geoffer", Integer.toString(slot));
   }

   private synchronized void saveTrade(Trade trade) {
      List<Trade> trades = new ArrayList();
      String history = this.configManager.getRSProfileConfiguration("grandexchange", "tradeHistory");
      Type type = (new TypeToken<List<Trade>>() {
      }).getType();
      if (history != null) {
         try {
            List<Trade> t = (List)this.gson.fromJson(history, type);
            if (t != null) {
               trades = t;
            }
         } catch (JsonSyntaxException var6) {
            JsonSyntaxException ex = var6;
            log.warn("error updating saved trades", ex);
         }
      }

      Instant ago = Instant.now().minus(365L, ChronoUnit.DAYS);

      while(!((List)trades).isEmpty() && (((List)trades).size() >= 1024 || ((Trade)((List)trades).get(0)).time.isBefore(ago))) {
         ((List)trades).remove(0);
      }

      ((List)trades).add(trade);
      this.configManager.setRSProfileConfiguration("grandexchange", "tradeHistory", this.gson.toJson(trades, type));
   }

   @Provides
   GrandExchangeConfig provideConfig(ConfigManager configManager) {
      return (GrandExchangeConfig)configManager.getConfig(GrandExchangeConfig.class);
   }

   protected void startUp() {
      this.panel = (GrandExchangePanel)this.injector.getInstance(GrandExchangePanel.class);
      BufferedImage icon = ImageUtil.loadImageResource(this.getClass(), "ge_icon.png");
      this.button = NavigationButton.builder().tooltip("Grand Exchange").icon(icon).priority(3).panel(this.panel).build();
      this.clientToolbar.addNavigation(this.button);
      if (this.config.quickLookup()) {
         this.mouseManager.registerMouseListener(this.inputListener);
         this.keyManager.registerKeyListener(this.inputListener);
      }

      AccountSession accountSession = this.sessionManager.getAccountSession();
      if (accountSession != null) {
         this.grandExchangeClient.setUuid(accountSession.getUuid());
      } else {
         this.grandExchangeClient.setUuid((UUID)null);
      }

      this.lastLoginTick = -1;
      if (this.client.getGameState() == GameState.LOGGED_IN) {
         GrandExchangeOffer[] offers = this.client.getGrandExchangeOffers();

         for(int i = 0; i < offers.length; ++i) {
            int slot = i;
            this.clientThread.invokeLater(() -> {
               this.updatePanel(slot, offers[slot]);
            });
            this.updateConfig(i, offers[i]);
         }
      }

   }

   protected void shutDown() {
      this.clientToolbar.removeNavigation(this.button);
      this.mouseManager.unregisterMouseListener(this.inputListener);
      this.keyManager.unregisterKeyListener(this.inputListener);
      this.machineUuid = null;
      this.lastAccount = -1L;
      this.tradeSeq = 0;
   }

   void search(String itemName) {
      SwingUtilities.invokeLater(() -> {
         this.panel.showItem();
         this.clientToolbar.openPanel(this.button);
         this.panel.getItemPanel().searchByName(itemName);
      });
   }

   void openGrandExchangeItem(int itemId, String itemName) {
      SwingUtilities.invokeLater(() -> {
         this.panel.showItem();
         this.clientToolbar.openPanel(this.button);
         this.panel.getItemPanel().selectItem(itemId, itemName);
      });
   }

   int requestGrandExchangeHistory(int rangeHours, int limit, int page) {
      int requestId = this.nextGeMarketRequestId();
      int safeLimit = this.clampGeMarketLimit(limit);
      int safePage = Math.max(0, page);
      this.clientThread.invoke(() -> {
         if (this.client.getGameState() != GameState.LOGGED_IN) {
            this.displayGrandExchangeMarketError(new UpdateGrandExchangeMarketErrorScript(requestId, 1, "NOT_LOGGED_IN", "Log in to load Grand Exchange history."));
         } else {
            this.client.sendIfScriptTrigger(3009, 12, 1, new Object[]{requestId, rangeHours, safeLimit, safePage});
         }
      });
      return requestId;
   }

   int requestGrandExchangeItemDetail(int itemId, int rangeHours, int limit, int page) {
      int requestId = this.nextGeMarketRequestId();
      int safeItemId = itemId;
      int safeLimit = this.clampGeMarketLimit(limit);
      int safePage = Math.max(0, page);
      this.clientThread.invoke(() -> {
         if (safeItemId <= 0) {
            this.displayGrandExchangeMarketError(new UpdateGrandExchangeMarketErrorScript(requestId, 2, "INVALID_REQUEST", "Select an item before loading market detail."));
         } else if (this.client.getGameState() != GameState.LOGGED_IN) {
            this.displayGrandExchangeMarketError(new UpdateGrandExchangeMarketErrorScript(requestId, 2, "NOT_LOGGED_IN", "Log in to load item market detail."));
         } else {
            this.client.sendIfScriptTrigger(3009, 12, 2, new Object[]{requestId, safeItemId, rangeHours, safeLimit, safePage});
         }
      });
      return requestId;
   }

   private int clampGeMarketLimit(int limit) {
      return Math.max(1, Math.min(limit, 250));
   }

   private int nextGeMarketRequestId() {
      if (this.geMarketRequestId == Integer.MAX_VALUE) {
         this.geMarketRequestId = 0;
      }

      return ++this.geMarketRequestId;
   }

   @Subscribe
   public void onSessionOpen(SessionOpen sessionOpen) {
      AccountSession accountSession = this.sessionManager.getAccountSession();
      this.grandExchangeClient.setUuid(accountSession.getUuid());
   }

   @Subscribe
   public void onSessionClose(SessionClose sessionClose) {
      this.grandExchangeClient.setUuid((UUID)null);
   }

   @Subscribe
   public void onUpdateGrandExchangeHistoryScript(UpdateGrandExchangeHistoryScript packet) {
      SwingUtilities.invokeLater(() -> {
         if (this.panel != null) {
            this.panel.getHistoryPanel().displayHistory(packet);
         }

      });
   }

   @Subscribe
   public void onUpdateGrandExchangeItemDetailScript(UpdateGrandExchangeItemDetailScript packet) {
      SwingUtilities.invokeLater(() -> {
         if (this.panel != null) {
            this.panel.getItemPanel().displayItemDetail(packet);
         }

      });
   }

   @Subscribe
   public void onUpdateGrandExchangeMarketErrorScript(UpdateGrandExchangeMarketErrorScript packet) {
      this.displayGrandExchangeMarketError(packet);
   }

   private void displayGrandExchangeMarketError(UpdateGrandExchangeMarketErrorScript packet) {
      if (packet.getRequestId() == -1) {
         log.debug("Grand Exchange market error without active request: {} {}", packet.getCode(), packet.getMessage());
      }

      SwingUtilities.invokeLater(() -> {
         if (this.panel != null) {
            this.panel.getHistoryPanel().displayMarketError(packet);
            this.panel.getItemPanel().displayMarketError(packet);
         }

      });
   }

   @Subscribe
   public void onConfigChanged(ConfigChanged event) {
      if (event.getGroup().equals("grandexchange") && event.getKey().equals("quickLookup")) {
         if (this.config.quickLookup()) {
            this.mouseManager.registerMouseListener(this.inputListener);
            this.keyManager.registerKeyListener(this.inputListener);
         } else {
            this.mouseManager.unregisterMouseListener(this.inputListener);
            this.keyManager.unregisterKeyListener(this.inputListener);
         }
      }

   }

   @Subscribe
   public void onGrandExchangeOfferChanged(GrandExchangeOfferChanged offerEvent) {
      int slot = offerEvent.getSlot();
      GrandExchangeOffer offer = offerEvent.getOffer();
      if (offer.getState() != GrandExchangeOfferState.EMPTY || this.client.getGameState() == GameState.LOGGED_IN) {
         log.debug("GE offer updated: state: {}, slot: {}, item: {}, qty: {}, lastLoginTick: {}", new Object[]{offer.getState(), slot, offer.getItemId(), offer.getQuantitySold(), this.lastLoginTick});
         this.updatePanel(slot, offer);
         this.updateLimitTimer(offer);
         this.submitTrade(slot, offer);
         this.updateConfig(slot, offer);
      }
   }

   @VisibleForTesting
   void submitTrade(int slot, GrandExchangeOffer offer) {
      if (this.client.getEnvironment() == 0) {
         GrandExchangeOfferState state = offer.getState();
         if (state == GrandExchangeOfferState.CANCELLED_BUY || state == GrandExchangeOfferState.CANCELLED_SELL || state == GrandExchangeOfferState.BUYING || state == GrandExchangeOfferState.SELLING) {
            SavedOffer savedOffer = this.getOffer(slot);
            boolean login = this.client.getTickCount() <= this.lastLoginTick + 2;
            GrandExchangeTrade grandExchangeTrade;
            if (savedOffer == null && (state == GrandExchangeOfferState.BUYING || state == GrandExchangeOfferState.SELLING) && offer.getQuantitySold() == 0) {
               grandExchangeTrade = new GrandExchangeTrade();
               grandExchangeTrade.setBuy(state == GrandExchangeOfferState.BUYING);
               grandExchangeTrade.setItemId(offer.getItemId());
               grandExchangeTrade.setTotal(offer.getTotalQuantity());
               grandExchangeTrade.setOffer(offer.getPrice());
               grandExchangeTrade.setSlot(slot);
               grandExchangeTrade.setWorldType(this.getGeWorldType());
               grandExchangeTrade.setLogin(login);
               grandExchangeTrade.setSeq(this.tradeSeq++);
               grandExchangeTrade.setResetTime(this.getLimitResetTime(offer.getItemId()));
               log.debug("Submitting new trade: {}", grandExchangeTrade);
               this.grandExchangeClient.submit(grandExchangeTrade);
            } else if (savedOffer != null && savedOffer.getItemId() == offer.getItemId() && savedOffer.getPrice() == offer.getPrice() && savedOffer.getTotalQuantity() == offer.getTotalQuantity()) {
               if (savedOffer.getState() != offer.getState() || savedOffer.getQuantitySold() != offer.getQuantitySold()) {
                  if (state != GrandExchangeOfferState.CANCELLED_BUY && state != GrandExchangeOfferState.CANCELLED_SELL) {
                     int qty = offer.getQuantitySold() - savedOffer.getQuantitySold();
                     int dspent = offer.getSpent() - savedOffer.getSpent();
                     if (qty > 0 && dspent > 0) {
                        GrandExchangeTrade grandExchangeTrade = new GrandExchangeTrade();
                        grandExchangeTrade.setBuy(state == GrandExchangeOfferState.BUYING);
                        grandExchangeTrade.setItemId(offer.getItemId());
                        grandExchangeTrade.setQty(offer.getQuantitySold());
                        grandExchangeTrade.setDqty(qty);
                        grandExchangeTrade.setTotal(offer.getTotalQuantity());
                        grandExchangeTrade.setDspent(dspent);
                        grandExchangeTrade.setSpent(offer.getSpent());
                        grandExchangeTrade.setOffer(offer.getPrice());
                        grandExchangeTrade.setSlot(slot);
                        grandExchangeTrade.setWorldType(this.getGeWorldType());
                        grandExchangeTrade.setLogin(login);
                        grandExchangeTrade.setSeq(this.tradeSeq++);
                        grandExchangeTrade.setResetTime(this.getLimitResetTime(offer.getItemId()));
                        log.debug("Submitting trade: {}", grandExchangeTrade);
                        this.grandExchangeClient.submit(grandExchangeTrade);
                        this.saveTrade(grandExchangeTrade);
                     }
                  } else {
                     grandExchangeTrade = new GrandExchangeTrade();
                     grandExchangeTrade.setBuy(state == GrandExchangeOfferState.CANCELLED_BUY);
                     grandExchangeTrade.setCancel(true);
                     grandExchangeTrade.setItemId(offer.getItemId());
                     grandExchangeTrade.setQty(offer.getQuantitySold());
                     grandExchangeTrade.setTotal(offer.getTotalQuantity());
                     grandExchangeTrade.setSpent(offer.getSpent());
                     grandExchangeTrade.setOffer(offer.getPrice());
                     grandExchangeTrade.setSlot(slot);
                     grandExchangeTrade.setWorldType(this.getGeWorldType());
                     grandExchangeTrade.setLogin(login);
                     grandExchangeTrade.setSeq(this.tradeSeq++);
                     grandExchangeTrade.setResetTime(this.getLimitResetTime(offer.getItemId()));
                     log.debug("Submitting cancelled: {}", grandExchangeTrade);
                     this.grandExchangeClient.submit(grandExchangeTrade);
                     this.saveTrade(grandExchangeTrade);
                  }
               }
            }
         }
      }
   }

   private void saveTrade(GrandExchangeTrade trade) {
      if (trade.getQty() > 0 && (trade.isCancel() || trade.getQty() == trade.getTotal())) {
         Trade t = new Trade();
         t.setBuy(trade.isBuy());
         t.setItemId(trade.getItemId());
         t.setQuantity(trade.getQty());
         t.setPrice(trade.getSpent() / trade.getQty());
         t.setTime(Instant.now());
         log.debug("Saving trade: {}", t);
         this.scheduledExecutorService.execute(() -> {
            this.saveTrade(t);
         });
      }

   }

   private WorldType getGeWorldType() {
      EnumSet<net.runelite.api.WorldType> worldTypes = this.client.getWorldType();
      if (worldTypes.contains(net.runelite.api.WorldType.SEASONAL)) {
         return WorldType.SEASONAL;
      } else if (worldTypes.contains(net.runelite.api.WorldType.TOURNAMENT_WORLD)) {
         return WorldType.TOURNAMENT;
      } else if (worldTypes.contains(net.runelite.api.WorldType.DEADMAN)) {
         return WorldType.DEADMAN;
      } else if (worldTypes.contains(net.runelite.api.WorldType.FRESH_START_WORLD)) {
         return WorldType.FRESH_START_WORLD;
      } else {
         return worldTypes.contains(net.runelite.api.WorldType.BETA_WORLD) ? WorldType.BETA_WORLD : null;
      }
   }

   private void updatePanel(int slot, GrandExchangeOffer offer) {
      ItemComposition offerItem = this.itemManager.getItemComposition(offer.getItemId());
      boolean shouldStack = offerItem.isStackable() || offer.getTotalQuantity() > 1;
      BufferedImage itemImage = this.itemManager.getImage(offer.getItemId(), offer.getTotalQuantity(), shouldStack);
      SwingUtilities.invokeLater(() -> {
         this.panel.getOffersPanel().updateOffer(offerItem, itemImage, offer, slot);
      });
   }

   private void updateConfig(int slot, GrandExchangeOffer offer) {
      if (offer.getState() == GrandExchangeOfferState.EMPTY) {
         this.deleteOffer(slot);
      } else {
         SavedOffer savedOffer = new SavedOffer();
         savedOffer.setItemId(offer.getItemId());
         savedOffer.setQuantitySold(offer.getQuantitySold());
         savedOffer.setTotalQuantity(offer.getTotalQuantity());
         savedOffer.setPrice(offer.getPrice());
         savedOffer.setSpent(offer.getSpent());
         savedOffer.setState(offer.getState());
         this.setOffer(slot, savedOffer);
      }

   }

   @Subscribe
   public void onChatMessage(ChatMessage event) {
      if (event.getType() == ChatMessageType.GAMEMESSAGE) {
         String message = Text.removeTags(event.getMessage());
         if (message.startsWith("Grand Exchange: Finished")) {
            this.notifier.notify(this.config.notifyOnOfferComplete(), message);
         } else if (message.startsWith("Grand Exchange:")) {
            this.notifier.notify(this.config.enableNotifications(), message);
         }

      }
   }

   @Subscribe
   public void onGameStateChanged(GameStateChanged gameStateChanged) {
      switch (gameStateChanged.getGameState()) {
         case LOGIN_SCREEN:
            this.panel.getOffersPanel().resetOffers();
            break;
         case LOGGING_IN:
         case HOPPING:
         case CONNECTION_LOST:
            this.lastLoginTick = this.client.getTickCount();
            break;
         case LOGGED_IN:
            this.grandExchangeClient.setMachineId(this.getMachineUuid());
      }

   }

   @Subscribe
   public void onMenuEntryAdded(MenuEntryAdded event) {
      if (this.client.getGameState() == GameState.LOGGED_IN && this.hotKeyPressed) {
         MenuEntry[] entries = this.client.getMenuEntries();
         MenuEntry menuEntry = entries[entries.length - 1];
         int widgetId = menuEntry.getParam1();
         int groupId = WidgetUtil.componentToInterface(widgetId);
         switch (groupId) {
            case 12:
               if (widgetId != 786444) {
                  break;
               }
            case 15:
            case 149:
            case 301:
            case 467:
               menuEntry.setOption("Search Grand Exchange");
               menuEntry.setType(MenuAction.RUNELITE);
         }

      }
   }

   @Subscribe
   public void onFocusChanged(FocusChanged focusChanged) {
      if (!focusChanged.isFocused()) {
         this.setHotKeyPressed(false);
      }

   }

   @Subscribe
   public void onScriptPostFired(ScriptPostFired event) {
      if (event.getScriptId() == 752 && this.config.highlightSearchMatch()) {
         this.highlightSearchMatches();
      }

   }

   private void highlightSearchMatches() {
      if (this.wasFuzzySearch) {
         String input = this.client.getVarcStrValue(359).toLowerCase();
         String underlineTag = "<u=" + ColorUtil.colorToHexCode(FUZZY_HIGHLIGHT_COLOR) + ">";
         Widget results = this.client.getWidget(10616884);
         Widget[] children = results.getDynamicChildren();
         int resultCount = children.length / 3;

         for(int i = 0; i < resultCount; ++i) {
            Widget itemNameWidget = children[i * 3 + 1];
            String itemName = itemNameWidget.getText();
            String itemNameLower = itemName.toLowerCase();
            int sharedPrefixLen = 0;

            for(int maxLen = Math.min(input.length(), itemName.length()); sharedPrefixLen < maxLen && input.charAt(sharedPrefixLen) == itemNameLower.charAt(sharedPrefixLen); ++sharedPrefixLen) {
            }

            if (sharedPrefixLen > 0) {
               StringBuilder newItemName = new StringBuilder(itemName);
               newItemName.insert(sharedPrefixLen, "</u>");
               newItemName.insert(0, underlineTag);
               itemNameWidget.setText(newItemName.toString());
            }
         }

      }
   }

   @Subscribe(
      priority = -100.0F
   )
   public void onGrandExchangeSearched(GrandExchangeSearched event) {
      this.wasFuzzySearch = false;
      GrandExchangeSearchMode searchMode = this.config.geSearchMode();
      String input = this.client.getVarcStrValue(359);
      if (searchMode != GrandExchangeSearchMode.DEFAULT && !input.isEmpty() && !event.isConsumed()) {
         event.consume();
         this.client.setGeSearchResultIndex(0);
         int resultCount = 0;
         IntStream var10000;
         ItemManager var10001;
         if (searchMode == GrandExchangeSearchMode.FUZZY_FALLBACK) {
            var10000 = IntStream.range(0, this.client.getItemCount());
            var10001 = this.itemManager;
            Objects.requireNonNull(var10001);
            List<Integer> ids = (List)var10000.mapToObj(var10001::getItemComposition).filter((item) -> {
               return item.isTradeable() && item.getNote() == -1 && item.getName().toLowerCase().contains(input);
            }).limit(251L).sorted(Comparator.comparing(ItemComposition::getName)).map(ItemComposition::getId).collect(Collectors.toList());
            if (ids.size() > 250) {
               this.client.setGeSearchResultCount(-1);
               this.client.setGeSearchResultIds((short[])null);
            } else {
               resultCount = ids.size();
               this.client.setGeSearchResultCount(resultCount);
               this.client.setGeSearchResultIds(Shorts.toArray(ids));
            }
         }

         if (resultCount == 0) {
            ToDoubleFunction<ItemComposition> comparator = this.fuzzySearchScorer.comparator(input);
            var10000 = IntStream.range(0, this.client.getItemCount());
            var10001 = this.itemManager;
            Objects.requireNonNull(var10001);
            List<Integer> ids = (List)var10000.mapToObj(var10001::getItemComposition).filter((item) -> {
               return item.isTradeable() && item.getNote() == -1;
            }).filter((item) -> {
               return comparator.applyAsDouble(item) > 0.0;
            }).sorted(Comparator.comparingDouble(comparator).reversed().thenComparing(ItemComposition::getName)).limit(250L).map(ItemComposition::getId).collect(Collectors.toList());
            this.client.setGeSearchResultCount(ids.size());
            this.client.setGeSearchResultIds(Shorts.toArray(ids));
            this.wasFuzzySearch = true;
         }

      }
   }

   @Subscribe
   public void onScriptCallbackEvent(ScriptCallbackEvent event) {
      switch (event.getEventName()) {
         case "setGETitle":
            this.setGeTitle();
            break;
         case "geBuyExamineText":
         case "geSellExamineText":
            boolean buy = "geBuyExamineText".equals(event.getEventName());
            Object[] stack = this.client.getObjectStack();
            int sz = this.client.getObjectStackSize();
            String fee = (String)stack[sz - 2];
            String examine = (String)stack[sz - 3];
            String text = this.setExamineText(examine, fee, buy);
            if (text != null) {
               stack[sz - 1] = text;
            }
      }

   }

   private void setGeTitle() {
      if (this.config.showTotal()) {
         long total = 0L;
         GrandExchangeOffer[] offers = this.client.getGrandExchangeOffers();
         GrandExchangeOffer[] var4 = offers;
         int var5 = offers.length;

         int objectStackSize;
         for(objectStackSize = 0; objectStackSize < var5; ++objectStackSize) {
            GrandExchangeOffer offer = var4[objectStackSize];
            if (offer != null) {
               total += (long)(offer.getPrice() * offer.getTotalQuantity());
            }
         }

         if (total != 0L) {
            StringBuilder titleBuilder = new StringBuilder(" (");
            if (this.config.showExact()) {
               titleBuilder.append(QuantityFormatter.formatNumber(total));
            } else {
               titleBuilder.append(QuantityFormatter.quantityToStackSize(total));
            }

            titleBuilder.append(')');
            Object[] objectStack = this.client.getObjectStack();
            objectStackSize = this.client.getObjectStackSize();
            int var10001 = objectStackSize - 1;
            String var10002 = String.valueOf(objectStack[objectStackSize - 1]);
            objectStack[var10001] = var10002 + titleBuilder.toString();
         }
      }
   }

   private void setLimitResetTime(int itemId) {
      Instant lastDateTime = (Instant)this.configManager.getRSProfileConfiguration("grandexchange", "buylimit." + itemId, Instant.class);
      if (lastDateTime == null || lastDateTime.isBefore(Instant.now())) {
         this.configManager.setRSProfileConfiguration("grandexchange", "buylimit." + itemId, Instant.now().plus(BUY_LIMIT_RESET));
      }

   }

   private Instant getLimitResetTime(int itemId) {
      Instant lastDateTime = (Instant)this.configManager.getRSProfileConfiguration("grandexchange", "buylimit." + itemId, Instant.class);
      if (lastDateTime == null) {
         return null;
      } else if (lastDateTime.isBefore(Instant.now())) {
         this.configManager.unsetRSProfileConfiguration("grandexchange", "buylimit." + itemId);
         return null;
      } else {
         return lastDateTime;
      }
   }

   private void updateLimitTimer(GrandExchangeOffer offer) {
      if (offer.getState() == GrandExchangeOfferState.BOUGHT || offer.getQuantitySold() > 0 && offer.getState() == GrandExchangeOfferState.BUYING) {
         this.setLimitResetTime(offer.getItemId());
      }

   }

   private String setExamineText(String examine, String fee, boolean buy) {
      int itemId = this.client.getVarpValue(1151);
      StringBuilder sb = new StringBuilder();
      if (buy && this.config.enableGELimits()) {
         ItemStats itemStats = this.itemManager.getItemStats(itemId);
         if (itemStats != null && itemStats.getGeLimit() > 0) {
            sb.append("Buy limit: ").append(QuantityFormatter.formatNumber((long)itemStats.getGeLimit()));
         }
      }

      if (buy && this.config.enableGELimitReset()) {
         Instant resetTime = this.getLimitResetTime(itemId);
         if (resetTime != null) {
            Duration remaining = Duration.between(Instant.now(), resetTime);
            sb.append(" (").append(DurationFormatUtils.formatDuration(remaining.toMillis(), "H:mm")).append(')');
         }
      }

      if (this.config.showActivelyTradedPrice() && !this.client.getWorldType().contains(net.runelite.api.WorldType.DEADMAN)) {
         int price = this.itemManager.getItemPriceWithSource(itemId, true);
         if (price > 0) {
            if (sb.length() > 0) {
               sb.append(" / ");
            }

            sb.append("Actively traded price: ").append(QuantityFormatter.formatNumber((long)price));
         }
      }

      if (sb.length() == 0) {
         return null;
      } else {
         if (!fee.isEmpty()) {
            sb.append("<br>").append(fee);
         }

         String var10000 = !buy ? shortenExamine(examine) : examine;
         return var10000 + "<br>" + String.valueOf(sb);
      }
   }

   private static String shortenExamine(String examine) {
      int from = 0;

      while(true) {
         int idx = examine.indexOf(32, from);
         if (idx == -1) {
            return examine;
         }

         if (idx > 100 && from > 0) {
            String var10000 = examine.substring(0, from - 1);
            return var10000 + "...";
         }

         from = idx + 1;
      }
   }

   void openGeLink(String name, int itemId) {
      String url = this.runeLiteConfig.useWikiItemPrices() ? "https://prices.runescape.wiki/" + (this.client.getWorldType().contains(net.runelite.api.WorldType.FRESH_START_WORLD) ? "fsw" : "osrs") + "/item/" + itemId : "https://services.runescape.com/m=itemdb_oldschool/" + name.replace(" ", "+") + "/viewitem?obj=" + itemId;
      LinkBrowser.browse(url);
   }

   private String getMachineUuid() {
      long accountHash = this.client.getAccountHash();
      if (this.lastAccount == accountHash) {
         return this.machineUuid;
      } else {
         this.lastAccount = accountHash;

         try {
            Hasher hasher = Hashing.sha256().newHasher();
            Runtime runtime = Runtime.getRuntime();
            hasher.putByte((byte)OSType.getOSType().ordinal());
            hasher.putByte((byte)runtime.availableProcessors());
            hasher.putUnencodedChars(System.getProperty("os.arch", ""));
            hasher.putUnencodedChars(System.getProperty("os.version", ""));
            hasher.putUnencodedChars(System.getProperty("user.name", ""));
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();

            while(networkInterfaces.hasMoreElements()) {
               NetworkInterface networkInterface = (NetworkInterface)networkInterfaces.nextElement();
               byte[] hardwareAddress = networkInterface.getHardwareAddress();
               if (hardwareAddress != null) {
                  hasher.putBytes(hardwareAddress);
               }
            }

            hasher.putLong(accountHash);
            this.machineUuid = hasher.hash().toString();
            this.tradeSeq = 0;
            return this.machineUuid;
         } catch (SocketException var8) {
            SocketException ex = var8;
            log.debug("unable to generate machine id", ex);
            this.machineUuid = null;
            this.tradeSeq = 0;
            return null;
         }
      }
   }

   void setPanel(GrandExchangePanel panel) {
      this.panel = panel;
   }

   boolean isHotKeyPressed() {
      return this.hotKeyPressed;
   }

   void setHotKeyPressed(boolean hotKeyPressed) {
      this.hotKeyPressed = hotKeyPressed;
   }
}
