package net.runelite.client.plugins.augustcustom.events;

import custom.UpdateBonusXpScript;
import custom.UpdateEventsInfoScript;
import custom.UpdateItemStatInfoScript;
import custom.UpdateNpcInfoScript;
import custom.UpdateServerAndPlayerInfoScript;
import java.awt.event.ActionListener;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import javax.inject.Inject;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.MenuAction;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.MenuEntryAdded;
import net.runelite.api.widgets.WidgetUtil;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.game.ItemEquipmentStats;
import net.runelite.client.game.ItemManager;
import net.runelite.client.game.ItemStats;
import net.runelite.client.game.NPCManager;
import net.runelite.client.game.NpcInfo;
import net.runelite.client.game.SkillIconManager;
import net.runelite.client.game.SpriteManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.xptracker.XpClient;
import net.runelite.client.ui.ClientToolbar;
import net.runelite.client.ui.NavigationButton;
import net.runelite.client.ui.overlay.OverlayManager;
import net.runelite.client.util.ImageUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@PluginDescriptor(
   name = "August Events",
   description = "Track Events in August",
   tags = {"events", "tracking", "timer"}
)
public class AugustEventsPlugin extends Plugin {
   private static final Logger log = LoggerFactory.getLogger(AugustEventsPlugin.class);
   @Inject
   private ClientToolbar clientToolbar;
   @Inject
   private Client client;
   @Inject
   private ClientThread clientThread;
   @Inject
   private SkillIconManager skillIconManager;
   @Inject
   private NPCManager npcManager;
   @Inject
   private ItemManager itemManager;
   @Inject
   private OverlayManager overlayManager;
   @Inject
   private XpClient xpClient;
   @Inject
   private SpriteManager spriteManager;
   private NavigationButton navButton;
   private AugustEventsPanel panel;

   protected void startUp() throws Exception {
      Consumer<Integer> bonusXpClickHandler = (worldId) -> {
         log.info("Bonus XP cell clicked for world: {}", worldId);
         this.clientThread.invoke(() -> {
            this.client.menuAction(-1, 197197831, MenuAction.CC_OP, 1, worldId, "", "");
            this.client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "Initiating hop to world " + worldId + "...", "");
         });
      };
      Consumer<Integer> votePageClickHandler = (ignored) -> {
         this.clientThread.invoke(() -> {
            int widgetComponentId = 1;
            this.client.menuAction(-1, 197197834, MenuAction.CC_OP, widgetComponentId, -1, "", "");
            this.client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "Opening Vote interface...", "");
         });
      };
      Consumer<Integer> donatePageClickHandler = (ignored) -> {
         this.clientThread.invoke(() -> {
            int widgetComponentId = 1;
            this.client.menuAction(-1, 197197833, MenuAction.CC_OP, widgetComponentId, -1, "", "");
            this.client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "Opening Donation interface...", "");
         });
      };
      this.panel = new AugustEventsPanel(this, this.client, bonusXpClickHandler, votePageClickHandler, donatePageClickHandler);
      this.navButton = NavigationButton.builder().tooltip("August Events").icon(ImageUtil.loadImageResource(this.getClass(), "/august_client_icon.png")).priority(1).panel(this.panel).build();
      this.clientToolbar.addNavigation(this.navButton);
   }

   protected void shutDown() throws Exception {
      this.clientToolbar.removeNavigation(this.navButton);
   }

   @Subscribe
   public void onUpdateServerAndPlayerInfoScript(UpdateServerAndPlayerInfoScript packet) {
      this.panel.setServerInfoText(packet.getServerInfoText());
   }

   @Subscribe
   public void onUpdateBonusXpScript(UpdateBonusXpScript packet) {
      List<BonusXpInfo> bonusXpInfoList = new ArrayList();
      Map<Integer, UpdateBonusXpScript.BonusXpState> mapping = packet.getMapping();
      if (mapping != null) {
         Iterator var4 = mapping.values().iterator();

         while(var4.hasNext()) {
            UpdateBonusXpScript.BonusXpState state = (UpdateBonusXpScript.BonusXpState)var4.next();
            bonusXpInfoList.add(new BonusXpInfo(state.getSourceWorldId(), this.spriteManager.getSprite(state.getSpriteId(), 0), state.getEndTimeMillis()));
         }
      }

      this.clientThread.invokeLater(() -> {
         if (this.panel != null) {
            this.panel.updateBonusXpDisplay(bonusXpInfoList);
         }

      });
   }

   @Subscribe
   public void onUpdateNpcInfoScript(UpdateNpcInfoScript packet) {
      Iterator var2 = packet.getNpcInfo().entrySet().iterator();

      while(var2.hasNext()) {
         Map.Entry<Integer, UpdateNpcInfoScript.NpcInfoMessage> entry = (Map.Entry)var2.next();
         NpcInfo info = new NpcInfo();
         info.setHitpoints(((UpdateNpcInfoScript.NpcInfoMessage)entry.getValue()).getHitpoints());
         this.npcManager.put((Integer)entry.getKey(), info);
      }

   }

   @Subscribe
   public void onUpdateItemStatInfoScript(UpdateItemStatInfoScript packet) {
      try {
         Map<Integer, ItemStats> itemStatsMapping = new HashMap();
         Constructor<ItemEquipmentStats> constructor = this.loadAccessibleConstructor();
         Iterator var4 = packet.getItemStatInfo().entrySet().iterator();

         while(var4.hasNext()) {
            Map.Entry<Integer, UpdateItemStatInfoScript.ItemStatInfo> entry = (Map.Entry)var4.next();
            UpdateItemStatInfoScript.ItemStatInfo info = (UpdateItemStatInfoScript.ItemStatInfo)entry.getValue();
            int[] bonuses = info.getBonuses();
            ItemEquipmentStats stats = this.createItemEquipmentStats(constructor, info.getEquipSlot(), info.isTwoHanded(), bonuses, info.getAttackSpeed());
            ItemStats itemStats = new ItemStats(info.getEquipSlot() != -1, info.getWeight(), -1, stats);
            itemStatsMapping.put((Integer)entry.getKey(), itemStats);
         }

         this.itemManager.addCustomItemStats(itemStatsMapping);
      } catch (Throwable var10) {
         Throwable $ex = var10;
         throw $ex;
      }
   }

   private Constructor<ItemEquipmentStats> loadAccessibleConstructor() throws ClassNotFoundException, NoSuchMethodException {
      Class<?> clazz = Class.forName("net.runelite.client.game.ItemEquipmentStats");
      Constructor<?> constructor = clazz.getDeclaredConstructor(Integer.TYPE, Boolean.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE, Float.TYPE, Integer.TYPE, Integer.TYPE);
      constructor.setAccessible(true);
      return constructor;
   }

   private ItemEquipmentStats createItemEquipmentStats(Constructor<ItemEquipmentStats> constructor, int equipSlot, boolean twoHanded, int[] bonuses, int attackSpeed) throws InvocationTargetException, InstantiationException, IllegalAccessException {
      return (ItemEquipmentStats)constructor.newInstance(equipSlot, twoHanded, bonuses[0], bonuses[1], bonuses[2], bonuses[3], bonuses[4], bonuses[5], bonuses[6], bonuses[7], bonuses[8], bonuses[9], bonuses[10], bonuses[11], bonuses[12], bonuses[13], attackSpeed);
   }

   @Subscribe
   public void onUpdateEventsInfoScript(UpdateEventsInfoScript packet) {
      if (this.panel != null) {
         if (packet.getMessages().length == 0) {
         }

         this.clientThread.invokeLater(() -> {
            if (this.panel != null) {
               Map<Integer, List<UpdateEventsInfoScript.EventPanelUpdateMessage>> incomingMessagesByWorld = new HashMap();
               UpdateEventsInfoScript.EventPanelUpdateMessage[] var3 = packet.getMessages();
               int var4 = var3.length;

               for(int var5 = 0; var5 < var4; ++var5) {
                  UpdateEventsInfoScript.EventPanelUpdateMessage messagex = var3[var5];
                  if (messagex.getWorldId() > 0) {
                     ((List)incomingMessagesByWorld.computeIfAbsent(messagex.getWorldId(), (k) -> {
                        return new ArrayList();
                     })).add(messagex);
                  }
               }

               if (!incomingMessagesByWorld.isEmpty()) {
                  boolean needsRebuild = false;
                  Iterator var19 = incomingMessagesByWorld.entrySet().iterator();

                  while(var19.hasNext()) {
                     Map.Entry<Integer, List<UpdateEventsInfoScript.EventPanelUpdateMessage>> entry = (Map.Entry)var19.next();
                     int worldId = (Integer)entry.getKey();
                     List<UpdateEventsInfoScript.EventPanelUpdateMessage> messagesForWorld = (List)entry.getValue();
                     Set<String> incomingActiveEventIds = new HashSet();
                     Iterator var9 = messagesForWorld.iterator();

                     while(var9.hasNext()) {
                        UpdateEventsInfoScript.EventPanelUpdateMessage msg = (UpdateEventsInfoScript.EventPanelUpdateMessage)var9.next();
                        if (msg.isAdd()) {
                           incomingActiveEventIds.add(msg.getId());
                        }
                     }

                     Set<String> knownEventIds = this.panel.getEventKeysForWorld(worldId);
                     Set<String> staleEventIds = new HashSet(knownEventIds);
                     staleEventIds.removeAll(incomingActiveEventIds);
                     Iterator var11;
                     if (!staleEventIds.isEmpty()) {
                        var11 = staleEventIds.iterator();

                        while(var11.hasNext()) {
                           String staleEventId = (String)var11.next();
                           this.panel.removeEventPanel(worldId, staleEventId);
                        }

                        needsRebuild = true;
                     }

                     var11 = messagesForWorld.iterator();

                     while(var11.hasNext()) {
                        UpdateEventsInfoScript.EventPanelUpdateMessage message = (UpdateEventsInfoScript.EventPanelUpdateMessage)var11.next();
                        if (message.isAdd()) {
                           boolean isNew = !this.panel.hasEventPanel(worldId, message.getId());
                           if (isNew) {
                              this.client.addChatMessage(ChatMessageType.ENGINE, "", "<col=ff0000>A new event has appeared in the August plugin!", "[EVENTS]");
                           }

                           UpdateEventsInfoScript.EventPanel panelData = message.getPanel();
                           if (panelData != null) {
                              ActionListener clickListener = null;
                              UpdateEventsInfoScript.EventPanelButton btnData = panelData.getButton();
                              if (btnData != null) {
                                 clickListener = (e) -> {
                                    this.clientThread.invoke(() -> {
                                       this.client.menuAction(-1, btnData.getInterfaceHashToSend(), MenuAction.CC_OP, 1, worldId, "", "");
                                    });
                                 };
                              }

                              EventPanel eventPanel = new EventPanel(panelData, this.spriteManager, clickListener);
                              this.panel.addOrUpdateEventPanel(worldId, message.getRenderOrder(), message.getId(), eventPanel);
                              needsRebuild = true;
                           }
                        } else {
                           this.panel.removeEventPanel(worldId, message.getId());
                           needsRebuild = true;
                        }
                     }
                  }

                  if (needsRebuild) {
                     this.panel.rebuildEventUI();
                  }

               }
            }
         });
      }
   }

   @Subscribe
   public void onGameStateChanged(GameStateChanged event) {
      GameState state = event.getGameState();
      if (this.panel != null) {
         if (state != GameState.LOGGED_IN && (state == GameState.LOGIN_SCREEN || state == GameState.HOPPING)) {
            this.clientThread.invokeLater(() -> {
               if (this.panel != null) {
                  this.panel.clearAllEvents();
                  this.panel.rebuildEventUI();
               }

            });
         }

      }
   }

   @Subscribe
   public void onMenuEntryAdded(MenuEntryAdded event) {
      int widgetID = event.getActionParam1();
      if (WidgetUtil.componentToInterface(widgetID) == 320 && event.getOption().startsWith("View")) {
         String skillText = event.getOption().split(" ")[1];
         int widgetComponentId = widgetID & '\uffff';
         this.client.getMenu().createMenuEntry(-1).setTarget(skillText).setOption("Configure XP Locking").setType(MenuAction.RUNELITE).onClick((e) -> {
            this.clientThread.invoke(() -> {
               this.client.menuAction(-1, 197197832, MenuAction.CC_OP, 1, widgetComponentId, "", "");
            });
         });
      }
   }
}
