package net.runelite.client.plugins.combatlevel;

import com.google.inject.Provides;
import java.awt.Color;
import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.Experience;
import net.runelite.api.Skill;
import net.runelite.api.WorldType;
import net.runelite.api.events.ScriptPostFired;
import net.runelite.api.events.ScriptPreFired;
import net.runelite.api.events.StatChanged;
import net.runelite.api.widgets.Widget;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;
import net.runelite.client.util.ColorUtil;
import net.runelite.client.util.Text;

@PluginDescriptor(
   name = "Combat Level",
   description = "Show a more accurate combat level in Combat Options panel and other combat level functions",
   tags = {"wilderness", "attack", "range"}
)
public class CombatLevelPlugin extends Plugin {
   private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#.###");
   private static final String CONFIG_GROUP = "combatlevel";
   private static final String ATTACK_RANGE_CONFIG_KEY = "wildernessAttackLevelRange";
   private static final Pattern WILDERNESS_LEVEL_PATTERN = Pattern.compile("^Level: (\\d+)$");
   private static final int MIN_COMBAT_LEVEL = 3;
   private static final String COMBAT_LEVEL_SECTION_TEXT = "Combat Level:";
   private static final Color CHARACTER_SUMMARY_GREEN = Color.decode("#0dc10d");
   @Inject
   private Client client;
   @Inject
   private CombatLevelConfig config;
   @Inject
   private CombatLevelOverlay overlay;
   @Inject
   private OverlayManager overlayManager;
   private String combatLevelStr;

   @Provides
   CombatLevelConfig provideConfig(ConfigManager configManager) {
      return (CombatLevelConfig)configManager.getConfig(CombatLevelConfig.class);
   }

   protected void startUp() throws Exception {
      double combatLevel = Experience.getCombatLevelPrecise(this.client.getRealSkillLevel(Skill.ATTACK), this.client.getRealSkillLevel(Skill.STRENGTH), this.client.getRealSkillLevel(Skill.DEFENCE), this.client.getRealSkillLevel(Skill.HITPOINTS), this.client.getRealSkillLevel(Skill.MAGIC), this.client.getRealSkillLevel(Skill.RANGED), this.client.getRealSkillLevel(Skill.PRAYER));
      this.combatLevelStr = DECIMAL_FORMAT.format(combatLevel);
      this.overlayManager.add(this.overlay);
      if (this.config.wildernessAttackLevelRange()) {
         this.appendAttackLevelRangeText();
      }

   }

   protected void shutDown() throws Exception {
      this.overlayManager.remove(this.overlay);
      Widget combatLevelWidget = this.client.getWidget(38862852);
      if (combatLevelWidget != null) {
         String widgetText = combatLevelWidget.getText();
         if (widgetText.contains(".")) {
            combatLevelWidget.setText(widgetText.substring(0, widgetText.indexOf(".")));
         }
      }

      this.shutDownAttackLevelRange();
   }

   @Subscribe
   private void onStatChanged(StatChanged statChanged) {
      Skill skill = statChanged.getSkill();
      if (skill == Skill.ATTACK || skill == Skill.DEFENCE || skill == Skill.STRENGTH || skill == Skill.HITPOINTS || skill == Skill.MAGIC || skill == Skill.PRAYER || skill == Skill.RANGED) {
         double combatLevel = Experience.getCombatLevelPrecise(this.client.getRealSkillLevel(Skill.ATTACK), this.client.getRealSkillLevel(Skill.STRENGTH), this.client.getRealSkillLevel(Skill.DEFENCE), this.client.getRealSkillLevel(Skill.HITPOINTS), this.client.getRealSkillLevel(Skill.MAGIC), this.client.getRealSkillLevel(Skill.RANGED), this.client.getRealSkillLevel(Skill.PRAYER));
         this.combatLevelStr = DECIMAL_FORMAT.format(combatLevel);
      }

   }

   @Subscribe
   public void onConfigChanged(ConfigChanged event) {
      if ("combatlevel".equals(event.getGroup()) && "wildernessAttackLevelRange".equals(event.getKey())) {
         if (this.config.wildernessAttackLevelRange()) {
            this.appendAttackLevelRangeText();
         } else {
            this.shutDownAttackLevelRange();
         }

      }
   }

   @Subscribe
   public void onScriptPreFired(ScriptPreFired scriptPreFired) {
      int scriptId = scriptPreFired.getScriptId();
      if ((scriptId == 3948 || scriptId == 3950) && this.config.showPreciseCombatLevel()) {
         Object[] objectStack = this.client.getObjectStack();
         int objectStackSize = this.client.getObjectStackSize();
         String levelText;
         if (scriptId == 3948) {
            levelText = Text.removeTags((String)objectStack[objectStackSize - 1]);
            if (this.client.getLocalPlayer().getCombatLevel() != Integer.parseInt(levelText)) {
               return;
            }
         } else {
            levelText = Text.removeTags((String)objectStack[objectStackSize - 3]);
            if (!"Combat Level:".equals(levelText)) {
               return;
            }
         }

         double combatLevelPrecise = Experience.getCombatLevelPrecise(this.client.getRealSkillLevel(Skill.ATTACK), this.client.getRealSkillLevel(Skill.STRENGTH), this.client.getRealSkillLevel(Skill.DEFENCE), this.client.getRealSkillLevel(Skill.HITPOINTS), this.client.getRealSkillLevel(Skill.MAGIC), this.client.getRealSkillLevel(Skill.RANGED), this.client.getRealSkillLevel(Skill.PRAYER));
         objectStack[objectStackSize - 1] = ColorUtil.wrapWithColorTag(DECIMAL_FORMAT.format(combatLevelPrecise), CHARACTER_SUMMARY_GREEN);
      }
   }

   @Subscribe
   public void onScriptPostFired(ScriptPostFired scriptPostFired) {
      if (scriptPostFired.getScriptId() == 388 && this.config.wildernessAttackLevelRange()) {
         this.appendAttackLevelRangeText();
      }

      if (scriptPostFired.getScriptId() == 7593 && this.config.showPreciseCombatLevel()) {
         Widget combatLevelWidget = this.client.getWidget(38862852);
         combatLevelWidget.setText("Combat Lvl: " + this.combatLevelStr);
      }

   }

   private void appendAttackLevelRangeText() {
      Widget wildernessLevelWidget = this.client.getWidget(5898290);
      if (wildernessLevelWidget != null) {
         String wildernessLevelText = wildernessLevelWidget.getText();
         Matcher m = WILDERNESS_LEVEL_PATTERN.matcher(wildernessLevelText);
         if (m.matches() && !WorldType.isPvpWorld(this.client.getWorldType())) {
            int wildernessLevel = Integer.parseInt(m.group(1));
            int combatLevel = this.client.getLocalPlayer().getCombatLevel();
            wildernessLevelWidget.setText(wildernessLevelText + "<br>" + combatAttackRange(combatLevel, wildernessLevel));
         }
      }
   }

   private void shutDownAttackLevelRange() {
      if (!WorldType.isPvpWorld(this.client.getWorldType())) {
         Widget wildernessLevelWidget = this.client.getWidget(5898290);
         if (wildernessLevelWidget != null) {
            String wildernessLevelText = wildernessLevelWidget.getText();
            if (wildernessLevelText.contains("<br>")) {
               wildernessLevelWidget.setText(wildernessLevelText.substring(0, wildernessLevelText.indexOf("<br>")));
            }
         }

      }
   }

   private static String combatAttackRange(int combatLevel, int wildernessLevel) {
      int var10000 = Math.max(3, combatLevel - wildernessLevel);
      return "" + var10000 + "-" + Math.min(126, combatLevel + wildernessLevel);
   }
}
