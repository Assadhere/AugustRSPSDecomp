package net.runelite.client.plugins.bosstimer;

import java.time.Duration;
import java.util.Iterator;
import javax.inject.Inject;
import net.runelite.api.ChatMessageType;
import net.runelite.api.NPC;
import net.runelite.api.events.ChatMessage;
import net.runelite.api.events.NpcChanged;
import net.runelite.api.events.NpcDespawned;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.game.ItemManager;
import net.runelite.client.game.NpcUtil;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.infobox.InfoBox;
import net.runelite.client.ui.overlay.infobox.InfoBoxManager;
import net.runelite.client.util.RSTimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@PluginDescriptor(
   name = "Boss Timers",
   description = "Show boss spawn timer overlays",
   tags = {"combat", "pve", "overlay", "spawn"}
)
public class BossTimersPlugin extends Plugin {
   private static final Logger log = LoggerFactory.getLogger(BossTimersPlugin.class);
   private static final String BRUTUS_BELL = "You ring the bell... and hear something approaching.";
   @Inject
   private InfoBoxManager infoBoxManager;
   @Inject
   private ItemManager itemManager;
   @Inject
   private NpcUtil npcUtil;

   protected void shutDown() throws Exception {
      this.infoBoxManager.removeIf((t) -> {
         return t instanceof RespawnTimer;
      });
   }

   @Subscribe
   public void onNpcDespawned(NpcDespawned npcDespawned) {
      NPC npc = npcDespawned.getNpc();
      Boss boss = Boss.find(npc.getId());
      if (boss != null && (boss.isIgnoreDead() || this.npcUtil.isDying(npc))) {
         this.createTimer(npc, boss);
      }

   }

   @Subscribe
   public void onNpcChanged(NpcChanged npcChanged) {
      NPC npc = npcChanged.getNpc();
      Boss boss = Boss.find(npc.getId());
      if (boss == Boss.HUEYCOATL) {
         this.createTimer(npc, boss);
      }

   }

   private void createTimer(NPC npc, Boss boss) {
      this.clearTimer(boss);
      log.debug("Creating spawn timer for {} ({})", npc.getName(), boss.getSpawnTime());
      RespawnTimer timer = new RespawnTimer(boss, this.itemManager.getImage(boss.getItemSpriteId()), this);
      timer.setTooltip(npc.getName());
      this.infoBoxManager.addInfoBox(timer);
   }

   private void clearTimer(Boss boss) {
      this.infoBoxManager.removeIf((t) -> {
         return t instanceof RespawnTimer && ((RespawnTimer)t).getBoss() == boss;
      });
   }

   @Subscribe
   private void onChatMessage(ChatMessage ev) {
      if (ev.getType() == ChatMessageType.MESBOX && "You ring the bell... and hear something approaching.".equals(ev.getMessage())) {
         Iterator var2 = this.infoBoxManager.getInfoBoxes().iterator();

         while(var2.hasNext()) {
            InfoBox ib = (InfoBox)var2.next();
            if (ib instanceof RespawnTimer && ((RespawnTimer)ib).getBoss() == Boss.BRUTUS) {
               ((RespawnTimer)ib).updateDuration(Duration.of(12L, RSTimeUnit.GAME_TICKS));
            }
         }
      }

   }
}
