package net.runelite.client.plugins.attackstyles;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.google.inject.Provides;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.Set;
import javax.annotation.Nullable;
import javax.inject.Inject;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.Skill;
import net.runelite.api.StructComposition;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.ScriptPostFired;
import net.runelite.api.events.VarbitChanged;
import net.runelite.api.widgets.Widget;
import net.runelite.client.Notifier;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.chat.ChatColorType;
import net.runelite.client.chat.ChatMessageBuilder;
import net.runelite.client.chat.ChatMessageManager;
import net.runelite.client.chat.QueuedMessage;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;

@PluginDescriptor(
   name = "Attack Styles",
   description = "Show your current attack style as an overlay",
   tags = {"combat", "defence", "magic", "overlay", "ranged", "strength", "warn", "pure"},
   enabledByDefault = false
)
public class AttackStylesPlugin extends Plugin {
   private int equippedWeaponTypeVarbit = -1;
   private AttackStyle attackStyle;
   private AttackStyle prevAttackStyle;
   private final Set<Skill> warnedSkills = EnumSet.noneOf(Skill.class);
   private boolean warnedSkillSelected;
   private final Table<Integer, Integer, Boolean> widgetsToHide = HashBasedTable.create();
   @Inject
   private Client client;
   @Inject
   private ClientThread clientThread;
   @Inject
   private AttackStylesConfig config;
   @Inject
   private OverlayManager overlayManager;
   @Inject
   private AttackStylesOverlay overlay;
   @Inject
   private ChatMessageManager chatManager;
   @Inject
   private Notifier notifier;

   @Provides
   AttackStylesConfig provideConfig(ConfigManager configManager) {
      return (AttackStylesConfig)configManager.getConfig(AttackStylesConfig.class);
   }

   protected void startUp() throws Exception {
      this.overlayManager.add(this.overlay);
      this.clientThread.invoke(() -> {
         this.resetWarnings();
         if (this.client.getGameState() == GameState.LOGGED_IN) {
            int attackStyleVarbit = this.client.getVarpValue(43);
            this.equippedWeaponTypeVarbit = this.client.getVarbitValue(357);
            int castingModeVarbit = this.client.getVarbitValue(2668);
            this.updateAttackStyle(this.equippedWeaponTypeVarbit, attackStyleVarbit, castingModeVarbit);
            this.updateWarning();
            this.processWidgets();
         }

      });
   }

   protected void shutDown() {
      this.overlayManager.remove(this.overlay);
      this.clientThread.invokeLater(() -> {
         this.updateWidgetsToHide(false);
         this.processWidgets();
         hideWidget(this.client.getWidget(38862880), false);
      });
      this.warnedSkills.clear();
   }

   @Nullable
   AttackStyle getAttackStyle() {
      return this.attackStyle;
   }

   boolean isWarnedSkillSelected() {
      return this.warnedSkillSelected;
   }

   @Subscribe
   public void onScriptPostFired(ScriptPostFired scriptPostFired) {
      if (scriptPostFired.getScriptId() == 7593) {
         this.processWidgets();
      }

   }

   private void processWidgets() {
      Iterator var1 = this.widgetsToHide.row(this.equippedWeaponTypeVarbit).keySet().iterator();

      while(var1.hasNext()) {
         int componentId = (Integer)var1.next();
         hideWidget(this.client.getWidget(componentId), (Boolean)this.widgetsToHide.get(this.equippedWeaponTypeVarbit, componentId));
      }

      hideWidget(this.client.getWidget(38862880), this.config.hideAutoRetaliate());
   }

   @Subscribe
   public void onVarbitChanged(VarbitChanged event) {
      if (event.getVarpId() == 43 || event.getVarbitId() == 357 || event.getVarbitId() == 2668) {
         int currentAttackStyleVarbit = this.client.getVarpValue(43);
         int currentEquippedWeaponTypeVarbit = this.client.getVarbitValue(357);
         int currentCastingModeVarbit = this.client.getVarbitValue(2668);
         boolean weaponSwitch = currentEquippedWeaponTypeVarbit != this.equippedWeaponTypeVarbit;
         this.equippedWeaponTypeVarbit = currentEquippedWeaponTypeVarbit;
         this.updateAttackStyle(this.equippedWeaponTypeVarbit, currentAttackStyleVarbit, currentCastingModeVarbit);
         this.updateWarning();
         if (weaponSwitch) {
            this.processWidgets();
         }
      }

   }

   @Subscribe
   public void onConfigChanged(ConfigChanged event) {
      if (event.getGroup().equals("attackIndicator")) {
         boolean enabled = Boolean.TRUE.toString().equals(event.getNewValue());
         this.clientThread.invokeLater(() -> {
            switch (event.getKey()) {
               case "warnForDefensive":
                  this.updateWarnedSkills(enabled, Skill.DEFENCE);
                  break;
               case "warnForAttack":
                  this.updateWarnedSkills(enabled, Skill.ATTACK);
                  break;
               case "warnForStrength":
                  this.updateWarnedSkills(enabled, Skill.STRENGTH);
                  break;
               case "warnForRanged":
                  this.updateWarnedSkills(enabled, Skill.RANGED);
                  break;
               case "warnForMagic":
                  this.updateWarnedSkills(enabled, Skill.MAGIC);
                  break;
               case "removeWarnedStyles":
                  this.updateWidgetsToHide(enabled);
            }

            this.updateWarning();
            this.processWidgets();
         });
      }

   }

   @Subscribe
   public void onGameTick(GameTick gameTick) {
      if (this.attackStyle != this.prevAttackStyle && this.warnedSkillSelected) {
         if (this.config.showChatWarnings()) {
            String message = (new ChatMessageBuilder()).append(ChatColorType.HIGHLIGHT).append("Your attack style has been changed to " + this.attackStyle.getName()).build();
            this.chatManager.queue(QueuedMessage.builder().type(ChatMessageType.CONSOLE).runeLiteFormattedMessage(message).build());
         }

         this.notifier.notify(this.config.warningNotification(), "Attack style changed to " + this.attackStyle.getName() + "!");
      }

      this.prevAttackStyle = this.attackStyle;
   }

   private void resetWarnings() {
      this.updateWarnedSkills(this.config.warnForAttack(), Skill.ATTACK);
      this.updateWarnedSkills(this.config.warnForStrength(), Skill.STRENGTH);
      this.updateWarnedSkills(this.config.warnForDefence(), Skill.DEFENCE);
      this.updateWarnedSkills(this.config.warnForRanged(), Skill.RANGED);
      this.updateWarnedSkills(this.config.warnForMagic(), Skill.MAGIC);
   }

   private void updateAttackStyle(int equippedWeaponType, int attackStyleIndex, int castingMode) {
      AttackStyle[] attackStyles = this.getWeaponTypeStyles(equippedWeaponType);
      if (attackStyleIndex < attackStyles.length) {
         if (attackStyleIndex == 4) {
            attackStyleIndex += castingMode;
         }

         this.attackStyle = attackStyles[attackStyleIndex];
         if (this.attackStyle == null) {
            this.attackStyle = AttackStyle.OTHER;
         }
      }

   }

   private AttackStyle[] getWeaponTypeStyles(int weaponType) {
      int weaponStyleEnum = this.client.getEnum(3908).getIntValue(weaponType);
      if (weaponStyleEnum == -1) {
         if (weaponType == 22) {
            return new AttackStyle[]{AttackStyle.ACCURATE, AttackStyle.AGGRESSIVE, null, AttackStyle.DEFENSIVE, AttackStyle.CASTING, AttackStyle.DEFENSIVE_CASTING};
         } else {
            return weaponType == 30 ? new AttackStyle[]{AttackStyle.ACCURATE, AttackStyle.AGGRESSIVE, AttackStyle.AGGRESSIVE, AttackStyle.DEFENSIVE} : new AttackStyle[0];
         }
      } else {
         int[] weaponStyleStructs = this.client.getEnum(weaponStyleEnum).getIntVals();
         AttackStyle[] styles = new AttackStyle[weaponStyleStructs.length];
         int i = 0;
         int[] var6 = weaponStyleStructs;
         int var7 = weaponStyleStructs.length;

         for(int var8 = 0; var8 < var7; ++var8) {
            int style = var6[var8];
            StructComposition attackStyleStruct = this.client.getStructComposition(style);
            String attackStyleName = attackStyleStruct.getStringValue(1407);
            AttackStyle attackStyle = AttackStyle.valueOf(attackStyleName.toUpperCase());
            if (attackStyle == AttackStyle.OTHER) {
               ++i;
            } else {
               if (i == 5 && attackStyle == AttackStyle.DEFENSIVE) {
                  attackStyle = AttackStyle.DEFENSIVE_CASTING;
               }

               styles[i++] = attackStyle;
            }
         }

         return styles;
      }
   }

   private void updateWarnedSkills(boolean enabled, Skill skill) {
      if (enabled) {
         this.warnedSkills.add(skill);
      } else {
         this.warnedSkills.remove(skill);
      }

   }

   private void updateWarning() {
      this.warnedSkillSelected = false;
      if (this.attackStyle != null) {
         Skill[] var1 = this.attackStyle.getSkills();
         int var2 = var1.length;

         for(int var3 = 0; var3 < var2; ++var3) {
            Skill skill = var1[var3];
            if (this.warnedSkills.contains(skill)) {
               this.warnedSkillSelected = true;
               break;
            }
         }
      }

      this.updateWidgetsToHide(this.config.removeWarnedStyles());
   }

   private void updateWidgetsToHide(boolean enabled) {
      AttackStyle[] attackStyles = this.getWeaponTypeStyles(this.equippedWeaponTypeVarbit);

      for(int i = 0; i < attackStyles.length; ++i) {
         AttackStyle attackStyle = attackStyles[i];
         if (attackStyle != null) {
            boolean warnedSkill = false;
            Skill[] var6 = attackStyle.getSkills();
            int var7 = var6.length;

            for(int var8 = 0; var8 < var7; ++var8) {
               Skill skill = var6[var8];
               if (this.warnedSkills.contains(skill)) {
                  warnedSkill = true;
                  break;
               }
            }

            switch (i) {
               case 0:
                  this.widgetsToHide.put(this.equippedWeaponTypeVarbit, 38862854, enabled && warnedSkill);
                  break;
               case 1:
                  this.widgetsToHide.put(this.equippedWeaponTypeVarbit, 38862858, enabled && warnedSkill);
                  break;
               case 2:
                  this.widgetsToHide.put(this.equippedWeaponTypeVarbit, 38862862, enabled && warnedSkill);
                  break;
               case 3:
                  this.widgetsToHide.put(this.equippedWeaponTypeVarbit, 38862866, enabled && warnedSkill);
                  break;
               case 4:
                  this.widgetsToHide.put(this.equippedWeaponTypeVarbit, 38862870, enabled && warnedSkill);
                  break;
               case 5:
                  this.widgetsToHide.put(this.equippedWeaponTypeVarbit, 38862871, enabled && warnedSkill);
                  this.widgetsToHide.put(this.equippedWeaponTypeVarbit, 38862873, enabled && warnedSkill);
                  this.widgetsToHide.put(this.equippedWeaponTypeVarbit, 38862874, enabled && warnedSkill);
                  this.widgetsToHide.put(this.equippedWeaponTypeVarbit, 38862875, enabled && warnedSkill);
            }
         }
      }

   }

   private static void hideWidget(Widget widget, boolean hidden) {
      if (widget != null) {
         widget.setHidden(hidden);
      }

   }

   @VisibleForTesting
   Set<Skill> getWarnedSkills() {
      return this.warnedSkills;
   }

   @VisibleForTesting
   Table<Integer, Integer, Boolean> getHiddenWidgets() {
      return this.widgetsToHide;
   }
}
