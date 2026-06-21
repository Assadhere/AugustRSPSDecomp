package net.runelite.client.plugins.timetracking.farming;

import com.google.common.collect.ImmutableSet;
import java.util.Iterator;
import java.util.Set;
import javax.inject.Inject;
import javax.inject.Singleton;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.MenuAction;
import net.runelite.api.events.ChatMessage;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.MenuOptionClicked;
import net.runelite.api.events.ScriptPreFired;
import net.runelite.api.widgets.Widget;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
public class PaymentTracker {
   private static final Logger log = LoggerFactory.getLogger(PaymentTracker.class);
   private static final Set<String> PAYMENT_TEXT = ImmutableSet.of("That'll do nicely, sir. Leave it with me - I'll make sure<br>that patch grows for you.", "That'll do nicely, madam. Leave it with me - I'll make<br>sure that patch grows for you.", "That'll do nicely. Leave it with me - I'll make sure that<br>patch grows for you.", "That'll do nicely, iknami. Leave it with me - I'll make<br>sure that patch grows for you.", "Alright, leave it with me. I'll look after that nursery for<br>you.");
   private static final String FALADOR_DIARY_TEXT = "The gardener protects your tree for you, free of charge, as a token of gratitude for completing the Falador elite diary.";
   private final Client client;
   private final ConfigManager configManager;
   private final FarmingWorld farmingWorld;
   private int lastSelectedOption;

   @Subscribe
   public void onGameTick(GameTick gameTick) {
      Widget text = this.client.getWidget(15138822);
      if (text != null && PAYMENT_TEXT.contains(text.getText())) {
         Widget name = this.client.getWidget(15138820);
         Widget head = this.client.getWidget(15138818);
         if (name != null && head != null && head.getModelType() == 2) {
            int npcId = head.getModelId();
            FarmingPatch patch = this.findPatchForNpc(npcId);
            if (patch != null) {
               if (!this.getProtectedState(patch)) {
                  log.debug("Detected patch payment for {} ({}) patch {}", new Object[]{name.getText(), npcId, patch});
                  this.setProtectedState(patch, true);
               }
            }
         }
      }
   }

   @Subscribe
   public void onMenuOptionClicked(MenuOptionClicked opt) {
      MenuAction action = opt.getMenuAction();
      if (action == MenuAction.WIDGET_CONTINUE) {
         Widget w = opt.getWidget();
         if (w != null && w.getId() == 14352385 && w.getIndex() > -1 && isPatchOption(w.getText())) {
            this.lastSelectedOption = w.getIndex() - 1;
            log.debug("Selected option via click: {}", this.lastSelectedOption);
         }
      } else if ((action == MenuAction.NPC_THIRD_OPTION || action == MenuAction.NPC_FOURTH_OPTION) && opt.getMenuOption().startsWith("Pay")) {
         this.lastSelectedOption = action == MenuAction.NPC_THIRD_OPTION ? 0 : 1;
         log.debug("Selected option via npc op: {}", this.lastSelectedOption);
      }

   }

   @Subscribe
   public void onChatMessage(ChatMessage event) {
      if (event.getType() == ChatMessageType.GAMEMESSAGE && event.getMessage().equals("The gardener protects your tree for you, free of charge, as a token of gratitude for completing the Falador elite diary.")) {
         FarmingPatch p = null;
         Iterator var3 = this.farmingWorld.getRegionsForLocation(this.client.getLocalPlayer().getWorldLocation()).iterator();

         while(var3.hasNext()) {
            FarmingRegion region = (FarmingRegion)var3.next();
            FarmingPatch[] var5 = region.getPatches();
            int var6 = var5.length;

            for(int var7 = 0; var7 < var6; ++var7) {
               FarmingPatch patch = var5[var7];
               if (region.getName().equals("Falador") && patch.getImplementation() == PatchImplementation.TREE) {
                  p = patch;
               }
            }
         }

         if (p != null && !this.getProtectedState(p)) {
            log.debug("Detected patch protection for {}", p);
            this.setProtectedState(p, true);
         }
      }
   }

   @Subscribe
   public void onScriptPreFired(ScriptPreFired scriptPreFired) {
      if (scriptPreFired.getScriptId() == 2153) {
         int[] intStack = this.client.getIntStack();
         int componentId = intStack[0];
         int subId = intStack[1];
         Widget w = this.client.getWidget(componentId).getChild(subId);
         if (componentId == 14352385 && subId > -1 && isPatchOption(w.getText())) {
            this.lastSelectedOption = subId - 1;
            log.debug("Selected option via keypress: {}", this.lastSelectedOption);
         }
      }

   }

   private static boolean isPatchOption(String name) {
      if (name == null) {
         return false;
      } else {
         return name.contains("Patch") || name.contains("allotment");
      }
   }

   private static String configKey(FarmingPatch fp) {
      return fp.configKey() + ".protected";
   }

   void setProtectedState(FarmingPatch fp, boolean state) {
      if (!state) {
         this.configManager.unsetRSProfileConfiguration("timetracking", configKey(fp));
      } else {
         this.configManager.setRSProfileConfiguration("timetracking", configKey(fp), state);
      }

   }

   boolean getProtectedState(FarmingPatch fp) {
      return Boolean.TRUE.equals(this.configManager.getRSProfileConfiguration("timetracking", configKey(fp), Boolean.class));
   }

   private FarmingPatch findPatchForNpc(int npcId) {
      FarmingPatch p = null;
      Iterator var3 = this.farmingWorld.getRegionsForLocation(this.client.getLocalPlayer().getWorldLocation()).iterator();

      while(var3.hasNext()) {
         FarmingRegion region = (FarmingRegion)var3.next();
         FarmingPatch[] var5 = region.getPatches();
         int var6 = var5.length;

         for(int var7 = 0; var7 < var6; ++var7) {
            FarmingPatch patch = var5[var7];
            if (patch.getFarmer() == npcId && (patch.getPatchNumber() == -1 || patch.getPatchNumber() == this.lastSelectedOption)) {
               p = patch;
            }
         }
      }

      return p;
   }

   @Inject
   private PaymentTracker(Client client, ConfigManager configManager, FarmingWorld farmingWorld) {
      this.client = client;
      this.configManager = configManager;
      this.farmingWorld = farmingWorld;
   }
}
