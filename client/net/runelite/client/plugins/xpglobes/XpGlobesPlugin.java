package net.runelite.client.plugins.xpglobes;

import com.google.inject.Provides;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.inject.Inject;
import net.runelite.api.Experience;
import net.runelite.api.Skill;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.StatChanged;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDependency;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.xptracker.XpTrackerPlugin;
import net.runelite.client.task.Schedule;
import net.runelite.client.ui.overlay.OverlayManager;

@PluginDescriptor(
   name = "XP Globes",
   description = "Show XP globes for the respective skill when gaining XP",
   tags = {"experience", "levels", "overlay"},
   enabledByDefault = false
)
@PluginDependency(XpTrackerPlugin.class)
public class XpGlobesPlugin extends Plugin {
   private static final int MAXIMUM_SHOWN_GLOBES = 5;
   private XpGlobe[] globeCache = new XpGlobe[Skill.values().length];
   private final List<XpGlobe> xpGlobes = new ArrayList();
   @Inject
   private XpGlobesConfig config;
   @Inject
   private OverlayManager overlayManager;
   @Inject
   private XpGlobesOverlay overlay;

   @Provides
   XpGlobesConfig getConfig(ConfigManager configManager) {
      return (XpGlobesConfig)configManager.getConfig(XpGlobesConfig.class);
   }

   protected void startUp() throws Exception {
      this.overlayManager.add(this.overlay);
   }

   protected void shutDown() throws Exception {
      this.overlayManager.remove(this.overlay);
   }

   @Subscribe
   public void onStatChanged(StatChanged statChanged) {
      Skill skill = statChanged.getSkill();
      int currentXp = statChanged.getXp();
      int currentLevel = statChanged.getLevel();
      int skillIdx = skill.ordinal();
      XpGlobe cachedGlobe = this.globeCache[skillIdx];
      if (cachedGlobe == null || cachedGlobe.getCurrentXp() < currentXp) {
         if (currentLevel >= 99) {
            if (this.config.hideMaxed()) {
               return;
            }

            if (this.config.showVirtualLevel()) {
               currentLevel = Experience.getLevelForXp(currentXp);
            }
         }

         if (cachedGlobe != null) {
            cachedGlobe.setSkill(skill);
            cachedGlobe.setCurrentXp(currentXp);
            cachedGlobe.setCurrentLevel(currentLevel);
            cachedGlobe.setTime(Instant.now());
            this.addXpGlobe(cachedGlobe);
         } else {
            this.globeCache[skillIdx] = new XpGlobe(skill, currentXp, currentLevel, Instant.now());
         }

      }
   }

   private void addXpGlobe(XpGlobe xpGlobe) {
      int idx = Collections.binarySearch(this.xpGlobes, xpGlobe, Comparator.comparing(XpGlobe::getSkill));
      if (idx < 0) {
         this.xpGlobes.add(-idx - 1, xpGlobe);
         if (this.xpGlobes.size() > 5) {
            Optional var10000 = this.xpGlobes.stream().min(Comparator.comparing(XpGlobe::getTime));
            List var10001 = this.xpGlobes;
            Objects.requireNonNull(var10001);
            var10000.ifPresent(var10001::remove);
         }
      }

   }

   @Schedule(
      period = 1L,
      unit = ChronoUnit.SECONDS
   )
   public void removeExpiredXpGlobes() {
      if (!this.xpGlobes.isEmpty()) {
         Instant expireTime = Instant.now().minusSeconds((long)this.config.xpOrbDuration());
         this.xpGlobes.removeIf((globe) -> {
            return globe.getTime().isBefore(expireTime);
         });
      }

   }

   private void resetGlobeState() {
      this.xpGlobes.clear();
      this.globeCache = new XpGlobe[Skill.values().length];
   }

   @Subscribe
   public void onGameStateChanged(GameStateChanged event) {
      switch (event.getGameState()) {
         case HOPPING:
         case LOGGING_IN:
            this.resetGlobeState();
         default:
      }
   }

   public List<XpGlobe> getXpGlobes() {
      return this.xpGlobes;
   }
}
