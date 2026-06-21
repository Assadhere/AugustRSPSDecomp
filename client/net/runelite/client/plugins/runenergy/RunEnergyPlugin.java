package net.runelite.client.plugins.runenergy;

import com.google.common.collect.ImmutableSet;
import com.google.inject.Provides;
import java.util.Arrays;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.inject.Inject;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.EquipmentInventorySlot;
import net.runelite.api.Item;
import net.runelite.api.ItemContainer;
import net.runelite.api.Skill;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.ChatMessage;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.ScriptCallbackEvent;
import net.runelite.api.events.ScriptPostFired;
import net.runelite.api.widgets.Widget;
import net.runelite.client.chat.ChatColorType;
import net.runelite.client.chat.ChatMessageBuilder;
import net.runelite.client.chat.ChatMessageManager;
import net.runelite.client.chat.QueuedMessage;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.game.ItemVariationMapping;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@PluginDescriptor(
   name = "Run Energy",
   description = "Show various information related to run energy",
   tags = {"overlay", "stamina"}
)
public class RunEnergyPlugin extends Plugin {
   private static final Logger log = LoggerFactory.getLogger(RunEnergyPlugin.class);
   private static final int GRACEFUL_FULL_SET_BOOST_BONUS = 10;
   private static final int RING_OF_ENDURANCE_PASSIVE_EFFECT = 500;
   @Inject
   private ChatMessageManager chatMessageManager;
   @Inject
   private Client client;
   @Inject
   private OverlayManager overlayManager;
   @Inject
   private RunEnergyOverlay energyOverlay;
   @Inject
   private RunEnergyConfig energyConfig;
   @Inject
   private ConfigManager configManager;
   private int lastCheckTick;
   private boolean roeWarningSent;
   private boolean localPlayerRunningToDestination;
   private WorldPoint prevLocalPlayerLocation;

   @Provides
   RunEnergyConfig getConfig(ConfigManager configManager) {
      return (RunEnergyConfig)configManager.getConfig(RunEnergyConfig.class);
   }

   protected void startUp() throws Exception {
      this.overlayManager.add(this.energyOverlay);
   }

   protected void shutDown() throws Exception {
      this.overlayManager.remove(this.energyOverlay);
      this.localPlayerRunningToDestination = false;
      this.prevLocalPlayerLocation = null;
      this.lastCheckTick = -1;
      this.roeWarningSent = false;
      this.resetRunOrbText();
   }

   Integer getRingOfEnduranceCharges() {
      return (Integer)this.configManager.getRSProfileConfiguration("runenergy", "ringOfEnduranceCharges", Integer.class);
   }

   void setRingOfEnduranceCharges(int charges) {
      this.configManager.setRSProfileConfiguration("runenergy", "ringOfEnduranceCharges", charges);
   }

   boolean isRingOfEnduranceEquipped() {
      ItemContainer equipment = this.client.getItemContainer(94);
      return equipment != null && equipment.count(24736) == 1;
   }

   @Subscribe
   public void onGameTick(GameTick event) {
      this.localPlayerRunningToDestination = this.prevLocalPlayerLocation != null && this.client.getLocalDestinationLocation() != null && this.prevLocalPlayerLocation.distanceTo(this.client.getLocalPlayer().getWorldLocation()) > 1;
      this.prevLocalPlayerLocation = this.client.getLocalPlayer().getWorldLocation();
   }

   @Subscribe
   public void onScriptPostFired(ScriptPostFired scriptPostFired) {
      if (scriptPostFired.getScriptId() == 447 && this.energyConfig.replaceOrbText()) {
         this.setRunOrbText(this.getEstimatedRunTimeRemaining(true));
      }

   }

   @Subscribe
   public void onConfigChanged(ConfigChanged event) {
      if (event.getGroup().equals("runenergy") && !this.energyConfig.replaceOrbText()) {
         this.resetRunOrbText();
      }

   }

   @Subscribe
   public void onChatMessage(ChatMessage event) {
      if (event.getType() == ChatMessageType.GAMEMESSAGE || event.getType() == ChatMessageType.SPAM) {
         String message = event.getMessage();
         if (message.equals("Your Ring of endurance doubles the duration of your stamina potion's effect.")) {
            Integer charges = this.getRingOfEnduranceCharges();
            if (charges == null) {
               log.debug("Ring of endurance charge with no known charges");
               return;
            }

            charges = charges - 1;
            this.setRingOfEnduranceCharges(charges);
            if (!this.roeWarningSent && charges < 500 && this.energyConfig.ringOfEnduranceChargeMessage()) {
               String chatMessage = (new ChatMessageBuilder()).append(ChatColorType.HIGHLIGHT).append("Your Ring of endurance now has less than 500 charges. Add more charges to regain its passive stamina effect.").build();
               this.chatMessageManager.queue(QueuedMessage.builder().type(ChatMessageType.CONSOLE).runeLiteFormattedMessage(chatMessage).build());
               this.roeWarningSent = true;
            }
         } else if (message.startsWith("Your Ring of endurance is charged with") || message.startsWith("You load your Ring of endurance with")) {
            Matcher matcher = Pattern.compile("([0-9,]+)").matcher(message);

            int charges;
            for(charges = -1; matcher.find(); charges = Integer.parseInt(matcher.group(1).replace(",", ""))) {
            }

            this.setRingOfEnduranceCharges(charges);
            if (charges >= 500) {
               this.roeWarningSent = false;
            }
         }

      }
   }

   @Subscribe
   public void onScriptCallbackEvent(ScriptCallbackEvent event) {
      if ("destroyOnOpKey".equals(event.getEventName())) {
         int yesOption = this.client.getIntStack()[this.client.getIntStackSize() - 1];
         if (yesOption == 1) {
            this.checkDestroyWidget();
         }

      }
   }

   private void setRunOrbText(String text) {
      Widget runOrbText = this.client.getWidget(10485789);
      if (runOrbText != null) {
         runOrbText.setText(text);
      }

      runOrbText = this.client.getWidget(58654747);
      if (runOrbText != null) {
         runOrbText.setText(text);
      }

   }

   private void resetRunOrbText() {
      this.setRunOrbText(Integer.toString(this.client.getEnergy() / 100));
   }

   String getEstimatedRunTimeRemaining(boolean inSeconds) {
      int effectiveWeight = Math.min(Math.max(this.client.getWeight(), 0), 64);
      int energyUnitsLost = (int)((double)(60 + 67 * effectiveWeight / 64) * (1.0 - (double)this.client.getBoostedSkillLevel(Skill.AGILITY) / 300.0));
      if (this.client.getVarbitValue(25) != 0) {
         energyUnitsLost = (int)((double)energyUnitsLost * 0.3);
      } else if (this.isRingOfEnduranceEquipped()) {
         Integer charges = this.getRingOfEnduranceCharges();
         if (charges == null) {
            return "?";
         }

         if (charges >= 500) {
            energyUnitsLost = (int)((double)energyUnitsLost * 0.85);
         }
      }

      double ticksLeft = Math.ceil((double)this.client.getEnergy() / (double)energyUnitsLost);
      double secondsLeft = ticksLeft * 600.0 / 1000.0;
      if (inSeconds) {
         return (int)Math.floor(secondsLeft) + "s";
      } else {
         int minutes = (int)Math.floor(secondsLeft / 60.0);
         int seconds = (int)Math.floor(secondsLeft - (double)minutes * 60.0);
         return "" + minutes + ":" + StringUtils.leftPad(Integer.toString(seconds), 2, "0");
      }
   }

   private int getGracefulRecoveryBoost() {
      ItemContainer equipment = this.client.getItemContainer(94);
      if (equipment == null) {
         return 0;
      } else {
         Item[] items = equipment.getItems();
         int boost = 0;
         GracefulEquipmentSlot[] var4 = RunEnergyPlugin.GracefulEquipmentSlot.values();
         int var5 = var4.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            GracefulEquipmentSlot slot = var4[var6];
            if (items.length > slot.getIndex()) {
               Item wornItem = items[slot.getIndex()];
               if (wornItem != null && slot.getItems().contains(wornItem.getId())) {
                  boost += slot.getBoost();
               }
            }
         }

         if (boost == RunEnergyPlugin.GracefulEquipmentSlot.TOTAL_BOOSTS) {
            boost += 10;
         }

         return boost;
      }
   }

   int getEstimatedRecoverTimeRemaining() {
      if (this.localPlayerRunningToDestination) {
         return -1;
      } else {
         double recoverRate = 25.0 + (double)this.client.getBoostedSkillLevel(Skill.AGILITY) / 6.0;
         recoverRate *= 1.0 + (double)this.getGracefulRecoveryBoost() / 100.0;
         double secondsLeft = (double)(10000 - this.client.getEnergy()) / recoverRate;
         return (int)secondsLeft;
      }
   }

   private void checkDestroyWidget() {
      int currentTick = this.client.getTickCount();
      if (this.lastCheckTick != currentTick) {
         this.lastCheckTick = currentTick;
         Widget widgetDestroyItemName = this.client.getWidget(38273030);
         if (widgetDestroyItemName != null) {
            if (widgetDestroyItemName.getText().equals("Ring of endurance")) {
               this.setRingOfEnduranceCharges(0);
            }

         }
      }
   }

   private static enum GracefulEquipmentSlot {
      HEAD(EquipmentInventorySlot.HEAD.getSlotIdx(), 3, new int[]{11850}),
      BODY(EquipmentInventorySlot.BODY.getSlotIdx(), 4, new int[]{11854}),
      LEGS(EquipmentInventorySlot.LEGS.getSlotIdx(), 4, new int[]{11856}),
      GLOVES(EquipmentInventorySlot.GLOVES.getSlotIdx(), 3, new int[]{11858}),
      BOOTS(EquipmentInventorySlot.BOOTS.getSlotIdx(), 3, new int[]{11860}),
      CAPE(EquipmentInventorySlot.CAPE.getSlotIdx(), 3, new int[]{11852, 9771, 13342});

      private final int index;
      private final int boost;
      private final Set<Integer> items;
      private static final int TOTAL_BOOSTS = Arrays.stream(values()).mapToInt(GracefulEquipmentSlot::getBoost).sum();

      private GracefulEquipmentSlot(int index, int boost, int... baseItems) {
         this.index = index;
         this.boost = boost;
         ImmutableSet.Builder<Integer> itemsBuilder = ImmutableSet.builder();
         int[] var7 = baseItems;
         int var8 = baseItems.length;

         for(int var9 = 0; var9 < var8; ++var9) {
            int item = var7[var9];
            itemsBuilder.addAll(ItemVariationMapping.getVariations(item));
         }

         this.items = itemsBuilder.build();
      }

      public int getIndex() {
         return this.index;
      }

      public int getBoost() {
         return this.boost;
      }

      public Set<Integer> getItems() {
         return this.items;
      }
   }
}
