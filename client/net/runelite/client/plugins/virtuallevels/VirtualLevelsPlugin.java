package net.runelite.client.plugins.virtuallevels;

import com.google.inject.Provides;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.Experience;
import net.runelite.api.Skill;
import net.runelite.api.events.ScriptCallbackEvent;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.events.PluginChanged;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;

@PluginDescriptor(
   name = "Virtual Levels",
   description = "Shows virtual levels (beyond 99) and virtual skill total on the skills tab.",
   tags = {"skill", "total", "max"},
   enabledByDefault = false
)
public class VirtualLevelsPlugin extends Plugin {
   private static final String TOTAL_LEVEL_TEXT_PREFIX = "Total level: ";
   @Inject
   private VirtualLevelsConfig config;
   @Inject
   private Client client;
   @Inject
   private ClientThread clientThread;

   @Provides
   VirtualLevelsConfig provideConfig(ConfigManager configManager) {
      return (VirtualLevelsConfig)configManager.getConfig(VirtualLevelsConfig.class);
   }

   protected void shutDown() {
      this.clientThread.invoke(this::simulateSkillChange);
   }

   @Subscribe
   public void onPluginChanged(PluginChanged pluginChanged) {
      if (pluginChanged.getPlugin() == this) {
         this.clientThread.invoke(this::simulateSkillChange);
      }

   }

   @Subscribe
   public void onConfigChanged(ConfigChanged configChanged) {
      if (configChanged.getGroup().equals("virtuallevels")) {
         this.clientThread.invoke(this::simulateSkillChange);
      }
   }

   @Subscribe
   public void onScriptCallbackEvent(ScriptCallbackEvent e) {
      String eventName = e.getEventName();
      int[] intStack = this.client.getIntStack();
      int intStackSize = this.client.getIntStackSize();
      Object[] objectStack = this.client.getObjectStack();
      int objectStackSize = this.client.getObjectStackSize();
      switch (eventName) {
         case "skillTabBaseLevel":
            int skillId = intStack[intStackSize - 2];
            Skill skill = Skill.values()[skillId];
            int exp = this.client.getSkillExperience(skill);
            intStack[intStackSize - 1] = Experience.getLevelForXp(exp);
            break;
         case "skillTabMaxLevel":
            intStack[intStackSize - 1] = 126;
            break;
         case "skillTabTotalLevel":
            if (this.config.virtualTotalLevel()) {
               int level = 0;
               Skill[] var13 = Skill.values();
               int var14 = var13.length;

               for(int var15 = 0; var15 < var14; ++var15) {
                  Skill s = var13[var15];
                  level += Experience.getLevelForXp(this.client.getSkillExperience(s));
               }

               objectStack[objectStackSize - 1] = "Total level: " + level;
            }
      }

   }

   private void simulateSkillChange() {
      Skill[] var1 = Skill.values();
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         Skill skill = var1[var3];
         this.client.queueChangedSkill(skill);
      }

   }
}
