package net.runelite.client.plugins.cluescrolls;

import com.google.common.base.MoreObjects;
import com.google.common.base.Strings;
import com.google.inject.Binder;
import com.google.inject.Provides;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Area;
import java.awt.image.BufferedImage;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Stream;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Named;
import net.runelite.api.Client;
import net.runelite.api.EnumComposition;
import net.runelite.api.GameObject;
import net.runelite.api.GameState;
import net.runelite.api.Item;
import net.runelite.api.ItemComposition;
import net.runelite.api.ItemContainer;
import net.runelite.api.MenuAction;
import net.runelite.api.MenuEntry;
import net.runelite.api.NPC;
import net.runelite.api.ObjectComposition;
import net.runelite.api.Point;
import net.runelite.api.Scene;
import net.runelite.api.Tile;
import net.runelite.api.TileObject;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.ChatMessage;
import net.runelite.api.events.CommandExecuted;
import net.runelite.api.events.DecorativeObjectDespawned;
import net.runelite.api.events.DecorativeObjectSpawned;
import net.runelite.api.events.GameObjectDespawned;
import net.runelite.api.events.GameObjectSpawned;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.GroundObjectDespawned;
import net.runelite.api.events.GroundObjectSpawned;
import net.runelite.api.events.ItemContainerChanged;
import net.runelite.api.events.MenuOptionClicked;
import net.runelite.api.events.NpcDespawned;
import net.runelite.api.events.NpcSpawned;
import net.runelite.api.events.WallObjectDespawned;
import net.runelite.api.events.WallObjectSpawned;
import net.runelite.api.events.WidgetLoaded;
import net.runelite.api.widgets.Widget;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.game.ItemManager;
import net.runelite.client.game.chatbox.ChatboxPanelManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDependency;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.banktags.BankTagsPlugin;
import net.runelite.client.plugins.banktags.TagManager;
import net.runelite.client.plugins.cluescrolls.clues.AnagramClue;
import net.runelite.client.plugins.cluescrolls.clues.BeginnerMapClue;
import net.runelite.client.plugins.cluescrolls.clues.CipherClue;
import net.runelite.client.plugins.cluescrolls.clues.ClueScroll;
import net.runelite.client.plugins.cluescrolls.clues.CoordinateClue;
import net.runelite.client.plugins.cluescrolls.clues.CrypticClue;
import net.runelite.client.plugins.cluescrolls.clues.EmoteClue;
import net.runelite.client.plugins.cluescrolls.clues.FairyRingClue;
import net.runelite.client.plugins.cluescrolls.clues.FaloTheBardClue;
import net.runelite.client.plugins.cluescrolls.clues.HotColdClue;
import net.runelite.client.plugins.cluescrolls.clues.LocationClueScroll;
import net.runelite.client.plugins.cluescrolls.clues.LocationsClueScroll;
import net.runelite.client.plugins.cluescrolls.clues.MapClue;
import net.runelite.client.plugins.cluescrolls.clues.MusicClue;
import net.runelite.client.plugins.cluescrolls.clues.NamedObjectClueScroll;
import net.runelite.client.plugins.cluescrolls.clues.NpcClueScroll;
import net.runelite.client.plugins.cluescrolls.clues.ObjectClueScroll;
import net.runelite.client.plugins.cluescrolls.clues.SkillChallengeClue;
import net.runelite.client.plugins.cluescrolls.clues.ThreeStepCrypticClue;
import net.runelite.client.plugins.cluescrolls.clues.item.ItemRequirement;
import net.runelite.client.ui.overlay.OverlayManager;
import net.runelite.client.ui.overlay.OverlayUtil;
import net.runelite.client.ui.overlay.components.TextComponent;
import net.runelite.client.ui.overlay.worldmap.WorldMapPointManager;
import net.runelite.client.util.ImageUtil;
import net.runelite.client.util.Text;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@PluginDescriptor(
   name = "Clue Scroll",
   description = "Show answers to clue scroll riddles, anagrams, ciphers, and cryptic clues",
   tags = {"arrow", "hints", "world", "map", "coordinates", "emotes"}
)
@PluginDependency(BankTagsPlugin.class)
public class ClueScrollPlugin extends Plugin {
   private static final Logger log = LoggerFactory.getLogger(ClueScrollPlugin.class);
   private static final Color HIGHLIGHT_BORDER_COLOR;
   private static final Color HIGHLIGHT_HOVER_BORDER_COLOR;
   private static final Color HIGHLIGHT_FILL_COLOR;
   private static final String CLUE_TAG_NAME = "clue";
   private static final String TREASURE_CHEST_TAG_NAME = "treasure chest";
   private static final String MAGIC_WARDROBE_TAG_NAME = "magic wardrobe";
   private static final String ARMOUR_CASE_TAG_NAME = "armour case";
   private static final String CAPE_RACK_TAG_NAME = "cape rack";
   private static final String TOY_BOX_TAG_NAME = "toy box";
   private static final int[] RUNEPOUCH_AMOUNT_VARBITS;
   private static final int[] RUNEPOUCH_RUNE_VARBITS;
   private static final String CLUE_NOTE_KEY_PREFIX = "note_";
   private ClueScroll clue;
   private final List<NPC> npcsToMark = new ArrayList();
   private final List<TileObject> objectsToMark = new ArrayList();
   private final Set<TileObject> namedObjectsToMark = new HashSet();
   private Item[] equippedItems;
   private Item[] inventoryItems;
   @Inject
   private Client client;
   @Inject
   private ClientThread clientThread;
   @Inject
   private ConfigManager configManager;
   @Inject
   private ChatboxPanelManager chatboxPanelManager;
   @Inject
   private ItemManager itemManager;
   @Inject
   private OverlayManager overlayManager;
   @Inject
   private ClueScrollOverlay clueScrollOverlay;
   @Inject
   private ClueScrollEmoteOverlay clueScrollEmoteOverlay;
   @Inject
   private ClueScrollMusicOverlay clueScrollMusicOverlay;
   @Inject
   private ClueScrollWorldOverlay clueScrollWorldOverlay;
   @Inject
   private ClueScrollConfig config;
   @Inject
   private WorldMapPointManager worldMapPointManager;
   @Inject
   private TagManager tagManager;
   @Inject
   @Named("developerMode")
   boolean developerMode;
   private BufferedImage emoteImage;
   private BufferedImage mapArrow;
   private Integer clueItemId;
   private boolean worldMapPointsSet = false;
   private int currentPlane = -1;
   private boolean namedObjectCheckThisTick;
   private final TextComponent textComponent = new TextComponent();
   private EmoteClue activeSTASHClue;
   private EmoteClue clickedSTASHClue;

   @Provides
   ClueScrollConfig getConfig(ConfigManager configManager) {
      return (ClueScrollConfig)configManager.getConfig(ClueScrollConfig.class);
   }

   public void configure(Binder binder) {
      binder.bind(ClueScrollService.class).to(ClueScrollServiceImpl.class);
   }

   protected void startUp() throws Exception {
      this.overlayManager.add(this.clueScrollOverlay);
      this.overlayManager.add(this.clueScrollEmoteOverlay);
      this.overlayManager.add(this.clueScrollWorldOverlay);
      this.overlayManager.add(this.clueScrollMusicOverlay);
      this.tagManager.registerTag("clue", this::testClueTag);
      this.tagManager.registerTag("treasure chest", this::testTreasureChestTag);
      this.tagManager.registerTag("magic wardrobe", this::testMagicWardrobe);
      this.tagManager.registerTag("armour case", this::testArmourCase);
      this.tagManager.registerTag("cape rack", this::testCapeRack);
      this.tagManager.registerTag("toy box", this::testToyBox);
   }

   protected void shutDown() throws Exception {
      this.tagManager.unregisterTag("clue");
      this.tagManager.unregisterTag("treasure chest");
      this.tagManager.unregisterTag("magic wardrobe");
      this.tagManager.unregisterTag("armour case");
      this.tagManager.unregisterTag("cape rack");
      this.tagManager.unregisterTag("toy box");
      this.overlayManager.remove(this.clueScrollOverlay);
      this.overlayManager.remove(this.clueScrollEmoteOverlay);
      this.overlayManager.remove(this.clueScrollWorldOverlay);
      this.overlayManager.remove(this.clueScrollMusicOverlay);
      this.npcsToMark.clear();
      this.namedObjectsToMark.clear();
      this.inventoryItems = null;
      this.equippedItems = null;
      this.currentPlane = -1;
      this.namedObjectCheckThisTick = false;
      this.resetClue(true);
   }

   @Subscribe
   public void onChatMessage(ChatMessage event) {
      switch (event.getType()) {
         case GAMEMESSAGE:
         case SPAM:
            String message = event.getMessage();
            if (this.clue instanceof HotColdClue && ((HotColdClue)this.clue).update(message, this)) {
               this.worldMapPointsSet = false;
            }

            if (this.clue instanceof SkillChallengeClue) {
               String text = Text.removeTags(message);
               if (text.equals("Skill challenge completed.") || text.equals("You have completed your master level challenge!") || text.startsWith("You have completed Charlie's task,") || text.equals("You have completed this challenge scroll.")) {
                  ((SkillChallengeClue)this.clue).setChallengeCompleted(true);
               }
            }

            if (message.endsWith(" the STASH unit.")) {
               if (this.clue instanceof EmoteClue && this.clickedSTASHClue != null && message.equals("You withdraw your items from the STASH unit.")) {
                  this.activeSTASHClue = this.clickedSTASHClue;
               } else if (message.equals("You deposit your items into the STASH unit.")) {
                  this.activeSTASHClue = null;
               }

               this.clickedSTASHClue = null;
            }
            break;
         case DIALOG:
            String[] senderAndMessage = event.getMessage().split("\\|", 2);
            ClueScroll npcChatClue = findNpcChatClueScroll(senderAndMessage[0], senderAndMessage[1]);
            if (npcChatClue != null) {
               this.updateClue(npcChatClue);
            }
      }

   }

   @Subscribe
   public void onMenuOptionClicked(MenuOptionClicked event) {
      if (event.getMenuOption() != null) {
         boolean isXMarksTheSpotOrb = event.getItemId() == 23069;
         if (!isXMarksTheSpotOrb && !event.getMenuOption().equals("Read")) {
            if (event.getMenuOption().equals("Search") && this.clue instanceof EmoteClue) {
               EmoteClue emoteClue = (EmoteClue)this.clue;
               if (emoteClue.getStashUnit() != null && emoteClue.getStashUnit().getObjectId() == event.getId()) {
                  this.clickedSTASHClue = emoteClue;
               }
            }
         } else {
            ItemComposition itemComposition = this.itemManager.getItemComposition(event.getItemId());
            if (isXMarksTheSpotOrb || itemComposition.getName().startsWith("Clue scroll") || itemComposition.getName().startsWith("Challenge scroll") || itemComposition.getName().startsWith("Treasure scroll")) {
               this.clueItemId = itemComposition.getId();
               this.updateClue(findClueScroll(this.clueItemId));
            }
         }

      }
   }

   @Subscribe
   public void onItemContainerChanged(ItemContainerChanged event) {
      ItemContainer itemContainer = event.getItemContainer();
      if (event.getContainerId() == 94) {
         this.equippedItems = itemContainer.getItems();
      } else if (event.getContainerId() == 93) {
         Item[] previousInventory = this.inventoryItems;
         this.inventoryItems = itemContainer.getItems();
         int newClueId;
         Item item;
         if (itemContainer.contains(12791) || itemContainer.contains(24416) || itemContainer.contains(27281) || itemContainer.contains(27509)) {
            List<Item> runePouchContents = this.getRunepouchContents();
            if (!runePouchContents.isEmpty()) {
               for(newClueId = 0; newClueId < this.inventoryItems.length; ++newClueId) {
                  Item invItem = this.inventoryItems[newClueId];
                  Iterator var7 = runePouchContents.iterator();

                  while(var7.hasNext()) {
                     item = (Item)var7.next();
                     if (invItem.getId() == item.getId()) {
                        this.inventoryItems[newClueId] = new Item(invItem.getId(), item.getQuantity() + invItem.getQuantity());
                        runePouchContents.remove(item);
                        break;
                     }
                  }
               }

               this.inventoryItems = (Item[])ArrayUtils.addAll(this.inventoryItems, (Item[])runePouchContents.toArray(new Item[0]));
            }
         }

         if (this.clue != null && this.clueItemId != null && !itemContainer.contains(this.clueItemId)) {
            this.resetClue(true);
         }

         if (this.clue instanceof ThreeStepCrypticClue && ((ThreeStepCrypticClue)this.clue).update(event.getContainerId(), itemContainer)) {
            this.worldMapPointsSet = false;
            this.npcsToMark.clear();
            if (this.config.displayHintArrows()) {
               this.client.clearHintArrow();
            }

            this.checkClueNPCs(this.clue, this.client.getTopLevelWorldView().npcs());
         }

         Set<Item> newInventoryClues = new HashSet();
         Collections.addAll(newInventoryClues, this.inventoryItems);
         if (previousInventory != null) {
            Item[] var10 = previousInventory;
            int var11 = previousInventory.length;

            for(int var13 = 0; var13 < var11; ++var13) {
               item = var10[var13];
               newInventoryClues.remove(item);
            }
         }

         newInventoryClues.removeIf((itemx) -> {
            ItemComposition itemComposition = this.client.getItemDefinition(itemx.getId());
            return itemComposition == null || itemComposition.getIntValue(623) == -1;
         });
         if (!newInventoryClues.isEmpty()) {
            newClueId = ((Item)newInventoryClues.iterator().next()).getId();
            ClueScrollConfig.IdentificationMode identificationMode = this.config.identify();
            if (identificationMode == ClueScrollConfig.IdentificationMode.ON_PICKUP || identificationMode == ClueScrollConfig.IdentificationMode.IF_INACTIVE && this.clue == null && this.clueItemId == null) {
               this.setActiveClue(newClueId);
            }
         }

      }
   }

   private void setActiveClue(int itemId) {
      ClueScroll clueScroll = findClueScroll(itemId);
      if (clueScroll != null) {
         this.clueItemId = itemId;
         this.updateClue(clueScroll);
      } else if (itemId != 23182 && itemId != 19835) {
         log.info("Unknown clue scroll id: {}", itemId);
      }

   }

   private List<Item> getRunepouchContents() {
      EnumComposition runepouchEnum = this.client.getEnum(982);
      List<Item> items = new ArrayList(RUNEPOUCH_AMOUNT_VARBITS.length);

      for(int i = 0; i < RUNEPOUCH_AMOUNT_VARBITS.length; ++i) {
         int amount = this.client.getVarbitValue(RUNEPOUCH_AMOUNT_VARBITS[i]);
         if (amount > 0) {
            int runeId = this.client.getVarbitValue(RUNEPOUCH_RUNE_VARBITS[i]);
            if (runeId != 0) {
               int itemId = runepouchEnum.getIntValue(runeId);
               Item item = new Item(itemId, amount);
               items.add(item);
            }
         }
      }

      return items;
   }

   @Subscribe
   public void onNpcSpawned(NpcSpawned event) {
      NPC npc = event.getNpc();
      this.checkClueNPCs(this.clue, Collections.singletonList(npc));
   }

   @Subscribe
   public void onNpcDespawned(NpcDespawned event) {
      boolean removed = this.npcsToMark.remove(event.getNpc());
      if (removed) {
         if (this.npcsToMark.isEmpty()) {
            this.client.clearHintArrow();
         } else {
            this.client.setHintArrow((NPC)this.npcsToMark.get(0));
         }
      }

   }

   @Subscribe
   public void onDecorativeObjectDespawned(DecorativeObjectDespawned event) {
      this.tileObjectDespawnedHandler(event.getDecorativeObject());
   }

   @Subscribe
   public void onDecorativeObjectSpawned(DecorativeObjectSpawned event) {
      this.tileObjectSpawnedHandler(event.getDecorativeObject());
   }

   @Subscribe
   public void onGameObjectDespawned(GameObjectDespawned event) {
      this.tileObjectDespawnedHandler(event.getGameObject());
   }

   @Subscribe
   public void onGameObjectSpawned(GameObjectSpawned event) {
      this.tileObjectSpawnedHandler(event.getGameObject());
   }

   @Subscribe
   public void onGroundObjectDespawned(GroundObjectDespawned event) {
      this.tileObjectDespawnedHandler(event.getGroundObject());
   }

   @Subscribe
   public void onGroundObjectSpawned(GroundObjectSpawned event) {
      this.tileObjectSpawnedHandler(event.getGroundObject());
   }

   @Subscribe
   public void onWallObjectDespawned(WallObjectDespawned event) {
      this.tileObjectDespawnedHandler(event.getWallObject());
   }

   @Subscribe
   public void onWallObjectSpawned(WallObjectSpawned event) {
      this.tileObjectSpawnedHandler(event.getWallObject());
   }

   private void tileObjectDespawnedHandler(TileObject despawned) {
      this.namedObjectsToMark.remove(despawned);
   }

   private void tileObjectSpawnedHandler(TileObject spawned) {
      this.checkClueNamedObject(this.clue, spawned);
   }

   @Subscribe
   public void onConfigChanged(ConfigChanged event) {
      if (event.getGroup().equals("cluescroll") && !this.config.displayHintArrows()) {
         this.client.clearHintArrow();
      }

   }

   @Subscribe
   public void onGameStateChanged(GameStateChanged event) {
      GameState state = event.getGameState();
      if (state != GameState.LOGGED_IN) {
         this.namedObjectsToMark.clear();
      }

      if (state == GameState.LOGIN_SCREEN) {
         this.resetClue(true);
      } else if (state == GameState.HOPPING) {
         this.namedObjectCheckThisTick = true;
      }

   }

   @Subscribe
   public void onGameTick(GameTick event) {
      this.objectsToMark.clear();
      WorldPoint[] locations;
      WorldPoint[] var4;
      int var5;
      int var6;
      WorldPoint location;
      if (this.clue instanceof LocationsClueScroll) {
         locations = ((LocationsClueScroll)this.clue).getLocations(this);
         if (locations.length > 0) {
            this.addMapPoints(locations);
         }

         if (this.clue instanceof ObjectClueScroll) {
            int[] objectIds = ((ObjectClueScroll)this.clue).getObjectIds();
            if (objectIds.length > 0) {
               var4 = locations;
               var5 = locations.length;

               for(var6 = 0; var6 < var5; ++var6) {
                  location = var4[var6];
                  if (location != null) {
                     this.highlightObjectsForLocation(location, objectIds);
                  }
               }
            }
         }
      }

      if (this.clue instanceof LocationClueScroll) {
         locations = ((LocationClueScroll)this.clue).getLocations(this);
         boolean npcHintArrowMarked = this.client.getHintArrowNpc() != null && this.npcsToMark.contains(this.client.getHintArrowNpc());
         if (!npcHintArrowMarked) {
            this.client.clearHintArrow();
         }

         var4 = locations;
         var5 = locations.length;

         for(var6 = 0; var6 < var5; ++var6) {
            location = var4[var6];
            if (location.isInScene(this.client) && this.config.displayHintArrows() && !npcHintArrowMarked) {
               this.client.setHintArrow(location);
            }

            this.addMapPoints(location);
            if (this.clue instanceof ObjectClueScroll) {
               int[] objectIds = ((ObjectClueScroll)this.clue).getObjectIds();
               if (objectIds.length > 0) {
                  this.highlightObjectsForLocation(location, objectIds);
               }
            }
         }
      }

      if (this.currentPlane != this.client.getPlane()) {
         this.namedObjectsToMark.clear();
         this.currentPlane = this.client.getPlane();
         this.namedObjectCheckThisTick = true;
      } else if (this.namedObjectCheckThisTick) {
         this.namedObjectCheckThisTick = false;
         this.checkClueNamedObjects(this.clue);
      }

      Widget chatDialogClueItem = this.client.getWidget(12648449);
      if (chatDialogClueItem != null && (chatDialogClueItem.getItemId() == 23182 || chatDialogClueItem.getItemId() == 19835)) {
         this.resetClue(false);
      }

   }

   @Subscribe
   public void onWidgetLoaded(WidgetLoaded event) {
      if (event.getGroupId() >= 346 && event.getGroupId() <= 356) {
         this.updateClue(BeginnerMapClue.forWidgetID(event.getGroupId()));
      } else if (event.getGroupId() == 203) {
         this.clientThread.invokeLater(() -> {
            Widget clueScrollText = this.client.getWidget(13303810);
            if (clueScrollText != null) {
               ClueScroll clueScroll = this.findClueScroll(clueScrollText.getText());
               if (clueScroll != null) {
                  this.updateClue(clueScroll);
               } else {
                  log.info("Unknown clue scroll (id {}) for text '{}'", this.clueItemId, clueScrollText.getText());
                  this.resetClue(true);
               }
            }

         });
      }

   }

   @Subscribe
   public void onCommandExecuted(CommandExecuted commandExecuted) {
      if (this.developerMode && commandExecuted.getCommand().equalsIgnoreCase("clue")) {
         String text = String.join(" ", commandExecuted.getArguments());
         if (text.isEmpty()) {
            this.resetClue(true);
         } else {
            ClueScroll clueScroll = this.findClueScroll(text);
            log.debug("Found clue scroll for '{}': {}", text, clueScroll);
            this.updateClue(clueScroll);
         }
      }

   }

   public BufferedImage getClueScrollImage() {
      return this.itemManager.getImage(19835);
   }

   public BufferedImage getEmoteImage() {
      if (this.emoteImage != null) {
         return this.emoteImage;
      } else {
         this.emoteImage = ImageUtil.loadImageResource(this.getClass(), "emote.png");
         return this.emoteImage;
      }
   }

   public BufferedImage getSpadeImage() {
      return this.itemManager.getImage(952);
   }

   BufferedImage getMapArrow() {
      if (this.mapArrow != null) {
         return this.mapArrow;
      } else {
         this.mapArrow = ImageUtil.loadImageResource(this.getClass(), "/util/clue_arrow.png");
         return this.mapArrow;
      }
   }

   void resetClue(boolean withItemId) {
      if (this.clue instanceof LocationsClueScroll) {
         ((LocationsClueScroll)this.clue).reset();
      }

      if (withItemId) {
         this.clueItemId = null;
      }

      this.clue = null;
      WorldMapPointManager var10000 = this.worldMapPointManager;
      Objects.requireNonNull(ClueScrollWorldMapPoint.class);
      var10000.removeIf(ClueScrollWorldMapPoint.class::isInstance);
      this.worldMapPointsSet = false;
      this.npcsToMark.clear();
      this.namedObjectsToMark.clear();
      if (this.config.displayHintArrows()) {
         this.client.clearHintArrow();
      }

      this.updateOverlayMenuEntries();
   }

   private ClueScroll findClueScroll(String rawText) {
      String text = Text.sanitizeMultilineText(rawText).toLowerCase();
      if (text.startsWith("i'd like to hear some music.")) {
         return MusicClue.forText(rawText);
      } else if (text.contains("degrees") && text.contains("minutes")) {
         return this.coordinatesToWorldPoint(text);
      } else {
         AnagramClue anagramClue = AnagramClue.forText(this, text);
         if (anagramClue != null) {
            return anagramClue;
         } else {
            CipherClue cipherClue = CipherClue.forText(text);
            if (cipherClue != null) {
               return cipherClue;
            } else {
               CrypticClue crypticClue = CrypticClue.forText(text);
               if (crypticClue != null) {
                  return crypticClue;
               } else {
                  EmoteClue emoteClue = EmoteClue.forText(text);
                  if (emoteClue != null) {
                     return emoteClue;
                  } else {
                     FairyRingClue fairyRingClue = FairyRingClue.forText(text);
                     if (fairyRingClue != null) {
                        return fairyRingClue;
                     } else {
                        FaloTheBardClue faloTheBardClue = FaloTheBardClue.forText(text);
                        if (faloTheBardClue != null) {
                           return faloTheBardClue;
                        } else {
                           HotColdClue hotColdClue = HotColdClue.forText(text);
                           if (hotColdClue != null) {
                              return hotColdClue;
                           } else {
                              SkillChallengeClue skillChallengeClue = SkillChallengeClue.forText(text, rawText);
                              if (skillChallengeClue != null) {
                                 return skillChallengeClue;
                              } else {
                                 ThreeStepCrypticClue threeStepCrypticClue = ThreeStepCrypticClue.forText(text, rawText);
                                 return threeStepCrypticClue != null ? threeStepCrypticClue : null;
                              }
                           }
                        }
                     }
                  }
               }
            }
         }
      }
   }

   private static ClueScroll findClueScroll(int itemId) {
      if (itemId != 23182 && itemId != 19835) {
         MapClue mapClue = MapClue.forItemId(itemId);
         if (mapClue != null) {
            return mapClue;
         } else {
            MusicClue musicClue = MusicClue.forItemId(itemId);
            if (musicClue != null) {
               return musicClue;
            } else {
               CoordinateClue coordinateClue = CoordinateClue.forItemId(itemId);
               if (coordinateClue != null) {
                  return coordinateClue;
               } else {
                  AnagramClue anagramClue = AnagramClue.forItemId(itemId);
                  if (anagramClue != null) {
                     return anagramClue;
                  } else {
                     CipherClue cipherClue = CipherClue.forItemId(itemId);
                     if (cipherClue != null) {
                        return cipherClue;
                     } else {
                        CrypticClue crypticClue = CrypticClue.forItemId(itemId);
                        if (crypticClue != null) {
                           return crypticClue;
                        } else {
                           EmoteClue emoteClue = EmoteClue.forItemId(itemId);
                           if (emoteClue != null) {
                              return emoteClue;
                           } else {
                              FairyRingClue fairyRingClue = FairyRingClue.forItemId(itemId);
                              return fairyRingClue != null ? fairyRingClue : null;
                           }
                        }
                     }
                  }
               }
            }
         }
      } else {
         return null;
      }
   }

   private static ClueScroll findNpcChatClueScroll(String sender, String rawText) {
      String text = Text.sanitizeMultilineText(rawText).toLowerCase();
      SkillChallengeClue skillChallengeClue = SkillChallengeClue.forChatboxText(sender, text);
      if (skillChallengeClue != null) {
         return skillChallengeClue;
      } else {
         FaloTheBardClue faloTheBardClue = FaloTheBardClue.forChatboxText(sender, text);
         return faloTheBardClue != null ? faloTheBardClue : null;
      }
   }

   private CoordinateClue coordinatesToWorldPoint(String text) {
      String[] splitText = text.split(" ");
      if (splitText.length != 10) {
         log.warn("Splitting \"" + text + "\" did not result in an array of 10 cells");
         return null;
      } else if (splitText[1].startsWith("degree") && splitText[3].startsWith("minute")) {
         int degY = Integer.parseInt(splitText[0]);
         int minY = Integer.parseInt(splitText[2]);
         if (splitText[4].equals("south")) {
            degY *= -1;
            minY *= -1;
         }

         int degX = Integer.parseInt(splitText[5]);
         int minX = Integer.parseInt(splitText[7]);
         if (splitText[9].equals("west")) {
            degX *= -1;
            minX *= -1;
         }

         WorldPoint coordinate = this.coordinatesToWorldPoint(degX, minX, degY, minY);
         return CoordinateClue.forLocation(coordinate);
      } else {
         log.warn("\"" + text + "\" is not a well formed coordinate string");
         return null;
      }
   }

   private WorldPoint coordinatesToWorldPoint(int degX, int minX, int degY, int minY) {
      int x2 = 2440;
      int y2 = 3161;
      x2 = (int)((long)x2 + (long)(degX * 32) + Math.round((double)minX / 1.875));
      y2 = (int)((long)y2 + (long)(degY * 32) + Math.round((double)minY / 1.875));
      return new WorldPoint(x2, y2, 0);
   }

   private void addMapPoints(WorldPoint... points) {
      if (!this.worldMapPointsSet) {
         this.worldMapPointsSet = true;
         WorldMapPointManager var10000 = this.worldMapPointManager;
         Objects.requireNonNull(ClueScrollWorldMapPoint.class);
         var10000.removeIf(ClueScrollWorldMapPoint.class::isInstance);
         WorldPoint[] var2 = points;
         int var3 = points.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            WorldPoint point = var2[var4];
            this.worldMapPointManager.add(new ClueScrollWorldMapPoint(point, this));
         }

      }
   }

   private void highlightObjectsForLocation(WorldPoint location, int... objectIds) {
      LocalPoint localLocation = LocalPoint.fromWorld(this.client, location);
      if (localLocation != null) {
         Scene scene = this.client.getScene();
         Tile[][][] tiles = scene.getTiles();
         Tile tile = tiles[this.client.getPlane()][localLocation.getSceneX()][localLocation.getSceneY()];
         Stream.concat(Stream.of(tile.getGameObjects()), Stream.of(tile.getDecorativeObject())).filter(Objects::nonNull).forEach((object) -> {
            int[] var3 = objectIds;
            int var4 = objectIds.length;

            for(int var5 = 0; var5 < var4; ++var5) {
               int id = var3[var5];
               if (object.getId() == id) {
                  this.objectsToMark.add(object);
               } else {
                  ObjectComposition comp = this.client.getObjectDefinition(object.getId());
                  ObjectComposition impostor = comp.getImpostorIds() != null ? comp.getImpostor() : comp;
                  if (impostor != null && impostor.getId() == id) {
                     this.objectsToMark.add(object);
                  }
               }
            }

         });
      }
   }

   private void checkClueNPCs(ClueScroll clue, Iterable<? extends NPC> npcs) {
      if (clue instanceof NpcClueScroll) {
         NpcClueScroll npcClueScroll = (NpcClueScroll)clue;
         String[] clueNpcs = npcClueScroll.getNpcs(this);
         Collection<Integer> clueNpcRegions = npcClueScroll.getNpcRegions();
         if (clueNpcs != null && clueNpcs.length != 0) {
            Iterator var6 = npcs.iterator();

            while(true) {
               NPC npc;
               do {
                  do {
                     do {
                        if (!var6.hasNext()) {
                           if (!this.npcsToMark.isEmpty() && this.config.displayHintArrows()) {
                              this.client.setHintArrow((NPC)this.npcsToMark.get(0));
                           }

                           return;
                        }

                        npc = (NPC)var6.next();
                     } while(npc == null);
                  } while(npc.getName() == null);
               } while(!clueNpcRegions.isEmpty() && !clueNpcRegions.contains(npc.getWorldLocation().getRegionID()));

               String[] var8 = clueNpcs;
               int var9 = clueNpcs.length;

               for(int var10 = 0; var10 < var9; ++var10) {
                  String npcName = var8[var10];
                  if (Objects.equals(npc.getName(), npcName)) {
                     this.npcsToMark.add(npc);
                  }
               }
            }
         }
      }
   }

   private void checkClueNamedObjects(@Nullable ClueScroll clue) {
      if (clue instanceof NamedObjectClueScroll) {
         Tile[][] var2 = this.client.getScene().getTiles()[this.client.getPlane()];
         int var3 = var2.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            Tile[] tiles = var2[var4];
            Tile[] var6 = tiles;
            int var7 = tiles.length;

            for(int var8 = 0; var8 < var7; ++var8) {
               Tile tile = var6[var8];
               if (tile != null) {
                  GameObject[] var10 = tile.getGameObjects();
                  int var11 = var10.length;

                  for(int var12 = 0; var12 < var11; ++var12) {
                     GameObject object = var10[var12];
                     if (object != null) {
                        this.checkClueNamedObject(clue, object);
                     }
                  }
               }
            }
         }

      }
   }

   private void checkClueNamedObject(@Nullable ClueScroll clue, @Nonnull TileObject object) {
      if (clue instanceof NamedObjectClueScroll) {
         NamedObjectClueScroll namedObjectClue = (NamedObjectClueScroll)clue;
         String[] objectNames = namedObjectClue.getObjectNames();
         int[] regionIds = namedObjectClue.getObjectRegions();
         if (objectNames != null && objectNames.length != 0 && (regionIds == null || ArrayUtils.contains(regionIds, object.getWorldLocation().getRegionID()))) {
            ObjectComposition comp = this.client.getObjectDefinition(object.getId());
            ObjectComposition impostor = comp.getImpostorIds() != null ? comp.getImpostor() : comp;
            String[] var8 = objectNames;
            int var9 = objectNames.length;

            for(int var10 = 0; var10 < var9; ++var10) {
               String name = var8[var10];
               if (comp.getName().equals(name) || impostor.getName().equals(name)) {
                  this.namedObjectsToMark.add(object);
               }
            }

         }
      }
   }

   private void updateClue(ClueScroll clue) {
      if (clue != null && clue != this.clue) {
         this.resetClue(false);
         this.checkClueNPCs(clue, this.client.getTopLevelWorldView().npcs());
         this.checkClueNamedObjects(clue);
         this.clue = clue;
         this.updateOverlayMenuEntries();
      }
   }

   void highlightWidget(Graphics2D graphics, Widget toHighlight, Widget container, Rectangle padding, String text) {
      padding = (Rectangle)MoreObjects.firstNonNull(padding, new Rectangle());
      Point canvasLocation = toHighlight.getCanvasLocation();
      if (canvasLocation != null) {
         Point windowLocation = container.getCanvasLocation();
         if (windowLocation.getY() <= canvasLocation.getY() + toHighlight.getHeight() && windowLocation.getY() + container.getHeight() >= canvasLocation.getY()) {
            Area widgetArea = new Area(new Rectangle(canvasLocation.getX() - padding.x, Math.max(canvasLocation.getY(), windowLocation.getY()) - padding.y, toHighlight.getWidth() + padding.x + padding.width, Math.min(Math.min(windowLocation.getY() + container.getHeight() - canvasLocation.getY(), toHighlight.getHeight()), Math.min(canvasLocation.getY() + toHighlight.getHeight() - windowLocation.getY(), toHighlight.getHeight())) + padding.y + padding.height));
            OverlayUtil.renderHoverableArea(graphics, widgetArea, this.client.getMouseCanvasPosition(), HIGHLIGHT_FILL_COLOR, HIGHLIGHT_BORDER_COLOR, HIGHLIGHT_HOVER_BORDER_COLOR);
            if (text != null) {
               FontMetrics fontMetrics = graphics.getFontMetrics();
               this.textComponent.setPosition(new java.awt.Point(canvasLocation.getX() + toHighlight.getWidth() / 2 - fontMetrics.stringWidth(text) / 2, canvasLocation.getY() + fontMetrics.getHeight()));
               this.textComponent.setText(text);
               this.textComponent.render(graphics);
            }
         }
      }
   }

   void scrollToWidget(int list, int scrollbar, Widget... toHighlight) {
      Widget parent = this.client.getWidget(list);
      int averageCentralY = 0;
      int nonnullCount = 0;
      Widget[] var7 = toHighlight;
      int var8 = toHighlight.length;

      for(int var9 = 0; var9 < var8; ++var9) {
         Widget widget = var7[var9];
         if (widget != null) {
            averageCentralY += widget.getRelativeY() + widget.getHeight() / 2;
            ++nonnullCount;
         }
      }

      if (nonnullCount != 0) {
         averageCentralY /= nonnullCount;
         int newScroll = Math.max(0, Math.min(parent.getScrollHeight(), averageCentralY - parent.getHeight() / 2));
         this.client.runScript(new Object[]{72, scrollbar, list, newScroll});
      }
   }

   private boolean testClueTag(int itemId) {
      ClueScroll c = this.clue;
      if (c == null) {
         return false;
      } else {
         ItemRequirement[] var4;
         int var5;
         int var6;
         ItemRequirement ir;
         if (c instanceof EmoteClue) {
            EmoteClue emote = (EmoteClue)c;
            var4 = emote.getItemRequirements();
            var5 = var4.length;

            for(var6 = 0; var6 < var5; ++var6) {
               ir = var4[var6];
               if (ir.fulfilledBy(itemId)) {
                  return true;
               }
            }
         } else {
            if (c instanceof CoordinateClue || c instanceof HotColdClue || c instanceof FairyRingClue) {
               return itemId == 952;
            }

            if (c instanceof MapClue) {
               MapClue mapClue = (MapClue)c;
               return mapClue.getObjectId() == -1 && itemId == 952;
            }

            if (c instanceof SkillChallengeClue) {
               SkillChallengeClue challengeClue = (SkillChallengeClue)c;
               var4 = challengeClue.getItemRequirements();
               var5 = var4.length;

               for(var6 = 0; var6 < var5; ++var6) {
                  ir = var4[var6];
                  if (ir.fulfilledBy(itemId)) {
                     return true;
                  }
               }
            }
         }

         return false;
      }
   }

   private boolean testTreasureChestTag(int itemId) {
      return this.testPohCostume(itemId, 3293, 3294, 3295, 3296, 3297, 3298);
   }

   private boolean testMagicWardrobe(int itemId) {
      return this.testPohCostume(itemId, 3289);
   }

   private boolean testArmourCase(int itemId) {
      return this.testPohCostume(itemId, 3290);
   }

   private boolean testCapeRack(int itemId) {
      return this.testPohCostume(itemId, 3292);
   }

   private boolean testToyBox(int itemId) {
      return this.testPohCostume(itemId, 3299);
   }

   private boolean testPohCostume(int itemId, int... enums) {
      EnumComposition members = this.client.getEnum(3077);
      EnumComposition alt = this.client.getEnum(3303);
      EnumComposition alts = this.client.getEnum(3304);
      int[] var6 = enums;
      int var7 = enums.length;

      for(int var8 = 0; var8 < var7; ++var8) {
         int tierEnumId = var6[var8];
         EnumComposition tierEnum = this.client.getEnum(tierEnumId);
         int[] var11 = tierEnum.getIntVals();
         int var12 = var11.length;

         for(int var13 = 0; var13 < var12; ++var13) {
            int baseItem = var11[var13];
            if (baseItem == itemId) {
               return true;
            }

            int membersEnumId = members.getIntValue(baseItem);
            if (membersEnumId != -1) {
               EnumComposition memberEnum = this.client.getEnum(membersEnumId);
               int[] var17 = memberEnum.getIntVals();
               int var18 = var17.length;

               for(int var19 = 0; var19 < var18; ++var19) {
                  int memberItem = var17[var19];
                  if (memberItem == itemId) {
                     return true;
                  }

                  if (this.checkAlternates(alt, alts, itemId, memberItem)) {
                     return true;
                  }
               }
            } else if (this.checkAlternates(alt, alts, itemId, baseItem)) {
               return true;
            }
         }
      }

      return false;
   }

   private boolean checkAlternates(EnumComposition alt, EnumComposition alts, int targetItemId, int checkItemId) {
      if (alt.getIntValue(checkItemId) == targetItemId) {
         return true;
      } else {
         int altsEnumId = alts.getIntValue(checkItemId);
         if (altsEnumId != -1) {
            EnumComposition e = this.client.getEnum(altsEnumId);
            int[] var7 = e.getIntVals();
            int var8 = var7.length;

            for(int var9 = 0; var9 < var8; ++var9) {
               int item = var7[var9];
               if (item == targetItemId) {
                  return true;
               }
            }
         }

         return false;
      }
   }

   private void updateOverlayMenuEntries() {
      this.clueScrollOverlay.removeMenuEntry(MenuAction.RUNELITE_OVERLAY, "Set note", "Clue Scroll overlay");
      this.clueScrollOverlay.removeMenuEntry(MenuAction.RUNELITE_OVERLAY, "Set note 1", "Clue Scroll overlay");
      this.clueScrollOverlay.removeMenuEntry(MenuAction.RUNELITE_OVERLAY, "Set note 2", "Clue Scroll overlay");
      this.clueScrollOverlay.removeMenuEntry(MenuAction.RUNELITE_OVERLAY, "Set note 3", "Clue Scroll overlay");
      if (this.clue != null) {
         int[] keys = this.clue.getConfigKeys();
         if (keys == null) {
            return;
         }

         if (keys.length == 1) {
            this.clueScrollOverlay.addMenuEntry(MenuAction.RUNELITE_OVERLAY, "Set note", "Clue Scroll overlay", this.setNoteConsumer(keys[0]));
         } else {
            for(int i = 0; i < Math.min(keys.length, 3); ++i) {
               this.clueScrollOverlay.addMenuEntry(MenuAction.RUNELITE_OVERLAY, "Set note " + (i + 1), "Clue Scroll overlay", this.setNoteConsumer(keys[i]));
            }
         }
      }

   }

   private Consumer<MenuEntry> setNoteConsumer(int key) {
      return (e) -> {
         this.chatboxPanelManager.openTextInput("Enter note").value((String)MoreObjects.firstNonNull(this.getClueNote(key), "")).onDone((s) -> {
            if (Strings.isNullOrEmpty(s)) {
               this.unsetClueNote(key);
            } else {
               this.setClueNote(key, s);
            }

         }).build();
      };
   }

   void setClueNote(int key, String note) {
      this.configManager.setConfiguration("cluescroll", "note_" + key, note);
   }

   void unsetClueNote(int key) {
      this.configManager.unsetConfiguration("cluescroll", "note_" + key);
   }

   public String getClueNote(int key) {
      return (String)this.configManager.getConfiguration("cluescroll", "note_" + key, (Type)String.class);
   }

   public ClueScroll getClue() {
      return this.clue;
   }

   public List<NPC> getNpcsToMark() {
      return this.npcsToMark;
   }

   public List<TileObject> getObjectsToMark() {
      return this.objectsToMark;
   }

   public Set<TileObject> getNamedObjectsToMark() {
      return this.namedObjectsToMark;
   }

   public Item[] getEquippedItems() {
      return this.equippedItems;
   }

   public Item[] getInventoryItems() {
      return this.inventoryItems;
   }

   public Client getClient() {
      return this.client;
   }

   public EmoteClue getActiveSTASHClue() {
      return this.activeSTASHClue;
   }

   static {
      HIGHLIGHT_BORDER_COLOR = Color.ORANGE;
      HIGHLIGHT_HOVER_BORDER_COLOR = HIGHLIGHT_BORDER_COLOR.darker();
      HIGHLIGHT_FILL_COLOR = new Color(0, 255, 0, 20);
      RUNEPOUCH_AMOUNT_VARBITS = new int[]{1624, 1625, 1626, 14286};
      RUNEPOUCH_RUNE_VARBITS = new int[]{29, 1622, 1623, 14285};
   }
}
