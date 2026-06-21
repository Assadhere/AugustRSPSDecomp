package net.runelite.client.plugins.crowdsourcing.woodcutting;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.inject.Inject;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.MenuAction;
import net.runelite.api.Skill;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.ChatMessage;
import net.runelite.api.events.GameObjectDespawned;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.MenuOptionClicked;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.crowdsourcing.CrowdsourcingManager;
import net.runelite.client.plugins.crowdsourcing.skilling.SkillingEndReason;
import net.runelite.client.plugins.crowdsourcing.skilling.SkillingState;

public class CrowdsourcingWoodcutting {
   private static final String CHOPPING_MESSAGE = "You swing your axe at the tree.";
   private static final String INVENTORY_FULL_MESSAGE = "Your inventory is too full to hold any more logs.";
   private static final String NEST_MESSAGE = "A bird's nest falls out of the tree";
   private static final Set<Integer> TREE_OBJECTS = (new ImmutableSet.Builder()).add(8462).add(10820).add(8467).add(9734).add(10822).add(8513).add(1276).add(1277).add(1278).add(1279).add(1280).add(1282).add(1283).add(1284).add(1285).add(1286).add(1289).add(1290).add(1291).add(1318).add(1319).add(1330).add(1331).add(1332).add(1365).add(1383).add(1384).add(2091).add(2092).add(2409).add(3879).add(3881).add(3882).add(3883).add(5902).add(5903).add(5904).add(9730).add(9731).add(9732).add(9733).add(10041).add(14308).add(14309).add(16264).add(16265).add(27060).add(30852).add(30854).add(27499).add(10819).add(10829).add(10831).add(10833).add(8488).add(15970).add(15951).add(15954).add(15948).add(10832).add(4674).add(8444).add(36688).add(15062).add(15062).add(10834).add(8409).add(10821).add(10830).add(2023).add(29668).add(29670).add(29311).add(3037).add(30602).build();
   private static final Map<Integer, Integer> AXE_ANIMS = (new ImmutableMap.Builder()).put(879, 1351).put(877, 1349).put(875, 1353).put(873, 1361).put(871, 1355).put(869, 1357).put(867, 1359).put(2846, 6739).put(24, 25378).put(11940, 30352).put(2117, 13241).put(12026, 25066).put(11939, 30347).put(7264, 20011).put(8324, 23673).put(8778, 25066).build();
   private static final Set<String> SUCCESS_MESSAGES = (new ImmutableSet.Builder()).add("You get some logs.").add("You get some oak logs.").add("You get some willow logs.").add("You get some teak logs.").add("You get some teak logs and give them to Carpenter Kjallak.").add("You get some maple logs.").add("You get some maple logs and give them to Lumberjack Leif.").add("You get some mahogany logs.").add("You get some mahogany logs and give them to Carpenter Kjallak.").add("You get some yew logs.").add("You get some magic logs.").add("You get some redwood logs.").add("You get some scrapey tree logs.").add("You get some bark.").add("You get a bruma root.").add("You get an arctic pine log").add("You get some juniper logs.").add("You get some mushrooms.").build();
   @Inject
   private CrowdsourcingManager manager;
   @Inject
   private Client client;
   private SkillingState state;
   private int lastExperimentEnd;
   private WorldPoint treeLocation;
   private int treeId;
   private int startTick;
   private int axe;
   private List<Integer> chopTicks;
   private List<Integer> nestTicks;

   public CrowdsourcingWoodcutting() {
      this.state = SkillingState.RECOVERING;
      this.lastExperimentEnd = 0;
      this.chopTicks = new ArrayList();
      this.nestTicks = new ArrayList();
   }

   private void endExperiment(SkillingEndReason reason) {
      if (this.state == SkillingState.CUTTING) {
         int endTick = this.client.getTickCount();
         int woodcuttingLevel = this.client.getBoostedSkillLevel(Skill.WOODCUTTING);
         WoodcuttingData data = new WoodcuttingData(woodcuttingLevel, this.startTick, endTick, this.chopTicks, this.nestTicks, this.axe, this.treeId, this.treeLocation, reason);
         this.manager.storeEvent(data);
         this.chopTicks = new ArrayList();
         this.nestTicks = new ArrayList();
      }

      this.state = SkillingState.RECOVERING;
      this.lastExperimentEnd = this.client.getTickCount();
   }

   @Subscribe
   public void onChatMessage(ChatMessage event) {
      String message = event.getMessage();
      ChatMessageType type = event.getType();
      if (this.state == SkillingState.CLICKED && type == ChatMessageType.SPAM && message.equals("You swing your axe at the tree.")) {
         this.startTick = this.client.getTickCount();
         this.state = SkillingState.CUTTING;
      } else if (this.state == SkillingState.CUTTING && type == ChatMessageType.SPAM && SUCCESS_MESSAGES.contains(message)) {
         this.chopTicks.add(this.client.getTickCount());
      } else if (this.state == SkillingState.CUTTING && type == ChatMessageType.GAMEMESSAGE && message.equals("Your inventory is too full to hold any more logs.")) {
         this.endExperiment(SkillingEndReason.INVENTORY_FULL);
      } else if (this.state == SkillingState.CUTTING && type == ChatMessageType.GAMEMESSAGE && message.contains("A bird's nest falls out of the tree")) {
         this.nestTicks.add(this.client.getTickCount());
      }

   }

   @Subscribe
   public void onGameTick(GameTick tick) {
      int animId = this.client.getLocalPlayer().getAnimation();
      if (this.state == SkillingState.CUTTING) {
         if (AXE_ANIMS.containsKey(animId)) {
            this.axe = (Integer)AXE_ANIMS.get(animId);
         } else {
            this.endExperiment(SkillingEndReason.INTERRUPTED);
         }
      } else if (animId != -1) {
         this.endExperiment(SkillingEndReason.INTERRUPTED);
      } else if (this.state == SkillingState.RECOVERING && this.client.getTickCount() - this.lastExperimentEnd >= 4) {
         this.state = SkillingState.READY;
      } else if (this.state == SkillingState.CLICKED && this.client.getTickCount() - this.lastExperimentEnd >= 20) {
         this.state = SkillingState.READY;
      }

   }

   @Subscribe
   public void onMenuOptionClicked(MenuOptionClicked menuOptionClicked) {
      MenuAction menuAction = menuOptionClicked.getMenuAction();
      int id = menuOptionClicked.getId();
      if (this.state == SkillingState.READY && menuAction == MenuAction.GAME_OBJECT_FIRST_OPTION && TREE_OBJECTS.contains(id)) {
         this.state = SkillingState.CLICKED;
         this.lastExperimentEnd = this.client.getTickCount();
         this.treeId = id;
         this.treeLocation = WorldPoint.fromScene(this.client, menuOptionClicked.getParam0(), menuOptionClicked.getParam1(), this.client.getPlane());
      } else {
         this.endExperiment(SkillingEndReason.INTERRUPTED);
      }

   }

   @Subscribe
   public void onGameObjectDespawned(GameObjectDespawned event) {
      if (this.state == SkillingState.CUTTING) {
         if (this.treeId == event.getGameObject().getId() && this.treeLocation.equals(event.getTile().getWorldLocation())) {
            this.endExperiment(SkillingEndReason.DEPLETED);
         }

      }
   }
}
