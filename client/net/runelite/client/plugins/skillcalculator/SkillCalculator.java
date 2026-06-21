package net.runelite.client.plugins.skillcalculator;

import com.google.common.annotations.VisibleForTesting;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import javax.inject.Inject;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import net.runelite.api.Client;
import net.runelite.api.Experience;
import net.runelite.api.Skill;
import net.runelite.api.WorldType;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.game.ItemManager;
import net.runelite.client.game.SpriteManager;
import net.runelite.client.plugins.skillcalculator.skills.SkillAction;
import net.runelite.client.plugins.skillcalculator.skills.SkillBonus;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.DynamicGridLayout;
import net.runelite.client.ui.FontManager;
import net.runelite.client.ui.components.IconTextField;

class SkillCalculator extends JPanel {
   static final int MAX_XP_MULTIPLIER = 32;
   private static final JLabel EMPTY_PANEL = new JLabel("No F2P actions to show.");
   private final UICalculatorInputArea uiInput;
   private final Client client;
   private final ClientThread clientThread;
   private final SpriteManager spriteManager;
   private final ItemManager itemManager;
   private final List<UIActionSlot> uiActionSlots = new ArrayList();
   private final UICombinedActionSlot combinedActionSlot;
   private final ArrayList<UIActionSlot> combinedActionSlots = new ArrayList();
   private final Map<SkillBonus, JCheckBox> bonusCheckBoxes = new HashMap();
   private final IconTextField searchBar = new IconTextField();
   private CalculatorType currentCalculator;
   private int currentLevel = 1;
   private int currentXP;
   private int targetLevel;
   private int targetXP;
   private int xpMultiplier;
   private final Set<SkillBonus> currentBonuses;

   @Inject
   SkillCalculator(Client client, ClientThread clientThread, UICalculatorInputArea uiInput, SpriteManager spriteManager, ItemManager itemManager) {
      this.currentXP = Experience.getXpForLevel(this.currentLevel);
      this.targetLevel = this.currentLevel + 1;
      this.targetXP = Experience.getXpForLevel(this.targetLevel);
      this.xpMultiplier = 1;
      this.currentBonuses = new HashSet();
      this.client = client;
      this.clientThread = clientThread;
      this.uiInput = uiInput;
      this.spriteManager = spriteManager;
      this.itemManager = itemManager;
      this.combinedActionSlot = new UICombinedActionSlot(spriteManager);
      this.searchBar.setIcon(IconTextField.Icon.SEARCH);
      this.searchBar.setPreferredSize(new Dimension(205, 30));
      this.searchBar.setBackground(ColorScheme.DARKER_GRAY_COLOR);
      this.searchBar.setHoverBackgroundColor(ColorScheme.DARK_GRAY_HOVER_COLOR);
      this.searchBar.addClearListener(this::onSearch);
      this.searchBar.addKeyListener(new KeyAdapter() {
         public void keyTyped(KeyEvent e) {
            SkillCalculator.this.onSearch();
         }
      });
      this.setLayout(new DynamicGridLayout(0, 1, 0, 5));
      uiInput.getUiFieldCurrentLevel().addActionListener((e) -> {
         this.onFieldCurrentLevelUpdated();
         uiInput.getUiFieldTargetLevel().requestFocusInWindow();
      });
      uiInput.getUiFieldCurrentXP().addActionListener((e) -> {
         this.onFieldCurrentXPUpdated();
         uiInput.getUiFieldTargetXP().requestFocusInWindow();
      });
      uiInput.getUiFieldTargetLevel().addActionListener((e) -> {
         this.onFieldTargetLevelUpdated();
      });
      uiInput.getUiFieldTargetXP().addActionListener((e) -> {
         this.onFieldTargetXPUpdated();
      });
      uiInput.getUiFieldXPMultiplier().addChangeListener((e) -> {
         this.onFieldXPMultiplierUpdated();
      });
      uiInput.getUiFieldCurrentLevel().addFocusListener(buildFocusAdapter((e) -> {
         this.onFieldCurrentLevelUpdated();
      }));
      uiInput.getUiFieldCurrentXP().addFocusListener(buildFocusAdapter((e) -> {
         this.onFieldCurrentXPUpdated();
      }));
      uiInput.getUiFieldTargetLevel().addFocusListener(buildFocusAdapter((e) -> {
         this.onFieldTargetLevelUpdated();
      }));
      uiInput.getUiFieldTargetXP().addFocusListener(buildFocusAdapter((e) -> {
         this.onFieldTargetXPUpdated();
      }));
      uiInput.getUiFieldXPMultiplier().addFocusListener(buildFocusAdapter((e) -> {
         this.onFieldXPMultiplierUpdated();
      }));
   }

   void openCalculator(CalculatorType calculatorType, boolean forceReload) {
      this.currentXP = this.client.getSkillExperience(calculatorType.getSkill());
      this.currentLevel = Experience.getLevelForXp(this.currentXP);
      if (forceReload || this.currentCalculator != calculatorType) {
         this.currentCalculator = calculatorType;
         this.currentBonuses.clear();
         int endGoalVarp = endGoalVarpForSkill(calculatorType.getSkill());
         int endGoal = this.client.getVarpValue(endGoalVarp);
         if (endGoal != -1) {
            this.targetLevel = Experience.getLevelForXp(endGoal);
            this.targetXP = endGoal;
         } else {
            this.targetLevel = enforceSkillBounds(this.currentLevel + 1);
            this.targetXP = Experience.getXpForLevel(this.targetLevel);
         }

         this.removeAll();
         this.searchBar.setText((String)null);
         this.clearCombinedSlots();
         if (this.client.getWorldType().contains(WorldType.MEMBERS)) {
            this.renderBonusOptions();
         }

         this.add(this.combinedActionSlot);
         this.add(this.searchBar);
         this.renderActionSlots();
      }

      this.updateInputFields();
   }

   private void updateCombinedAction() {
      int size = this.combinedActionSlots.size();
      if (size > 1) {
         this.combinedActionSlot.setTitle("" + size + " actions selected");
      } else {
         if (size != 1) {
            this.combinedActionSlot.setTitle("No action selected");
            this.combinedActionSlot.setText("Shift-click to select multiple");
            return;
         }

         this.combinedActionSlot.setTitle("1 action selected");
      }

      int actionCount = 0;
      int neededXP = this.targetXP - this.currentXP;
      int xp = 0;

      UIActionSlot slot;
      for(Iterator var5 = this.combinedActionSlots.iterator(); var5.hasNext(); xp += slot.getValue()) {
         slot = (UIActionSlot)var5.next();
      }

      if (neededXP > 0) {
         assert xp != 0;

         neededXP *= 10;
         actionCount = (neededXP - 1) / xp + 1;
      }

      this.combinedActionSlot.setText(formatXPActionString(xp, actionCount, "exp - "));
   }

   private void clearCombinedSlots() {
      Iterator var1 = this.combinedActionSlots.iterator();

      while(var1.hasNext()) {
         UIActionSlot slot = (UIActionSlot)var1.next();
         slot.setSelected(false);
      }

      this.combinedActionSlots.clear();
   }

   private void renderBonusOptions() {
      SkillBonus[] skillBonuses = this.currentCalculator.getSkillBonuses();
      if (skillBonuses != null) {
         SkillBonus[] var2 = skillBonuses;
         int var3 = skillBonuses.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            SkillBonus bonus = var2[var4];
            JPanel checkboxPanel = this.buildCheckboxPanel(bonus);
            this.add(checkboxPanel);
         }

         if (skillBonuses.length > 0) {
            this.add(Box.createRigidArea(new Dimension(0, 4)));
         }

      }
   }

   private JPanel buildCheckboxPanel(SkillBonus bonus) {
      JPanel uiOption = new JPanel(new BorderLayout());
      JLabel uiLabel = new JLabel(generateDisplayNameForBonus(bonus));
      JCheckBox uiCheckbox = new JCheckBox();
      uiLabel.setForeground(Color.WHITE);
      uiLabel.setFont(FontManager.getRunescapeSmallFont());
      uiOption.setBorder(BorderFactory.createEmptyBorder(1, 7, 1, 0));
      uiCheckbox.addActionListener((event) -> {
         this.adjustCheckboxes(uiCheckbox, bonus);
      });
      uiOption.add(uiLabel, "West");
      uiOption.add(uiCheckbox, "East");
      this.bonusCheckBoxes.put(bonus, uiCheckbox);
      return uiOption;
   }

   private static String generateDisplayNameForBonus(SkillBonus bonus) {
      String var10000 = bonus.getName();
      return var10000 + " (" + formatBonusPercentage(bonus.getValue()) + "%)";
   }

   @VisibleForTesting
   static String formatBonusPercentage(float bonus) {
      int bonusValue = Math.round(10000.0F * bonus);
      float bonusPercent = (float)bonusValue / 100.0F;
      int bonusPercentInt = (int)bonusPercent;
      return bonusPercent == (float)bonusPercentInt ? String.valueOf(bonusPercentInt) : String.valueOf(bonusPercent);
   }

   private void adjustCheckboxes(JCheckBox target, SkillBonus bonus) {
      Iterator var3 = this.bonusCheckBoxes.entrySet().iterator();

      while(var3.hasNext()) {
         Map.Entry<SkillBonus, JCheckBox> entry = (Map.Entry)var3.next();
         if (entry.getValue() != target && !((SkillBonus)entry.getKey()).getCanBeStackedWith().contains(bonus)) {
            this.currentBonuses.remove(entry.getKey());
            ((JCheckBox)entry.getValue()).setSelected(false);
         }
      }

      if (target.isSelected()) {
         this.currentBonuses.add(bonus);
      } else {
         this.currentBonuses.remove(bonus);
      }

      this.calculate();
   }

   private void renderActionSlots() {
      this.uiActionSlots.clear();
      SkillAction[] var1 = this.currentCalculator.getSkillActions();
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         SkillAction action = var1[var3];
         JLabel uiIcon = new JLabel();
         if (action.getIcon() != -1) {
            this.itemManager.getImage(action.getIcon()).addTo(uiIcon);
         } else if (action.getSprite() != -1) {
            this.spriteManager.addSpriteTo((JLabel)uiIcon, action.getSprite(), 0);
         }

         final UIActionSlot slot = new UIActionSlot(action, this.clientThread, this.itemManager, uiIcon);
         this.uiActionSlots.add(slot);
         slot.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
               if (!e.isShiftDown()) {
                  SkillCalculator.this.clearCombinedSlots();
               }

               if (slot.isSelected()) {
                  SkillCalculator.this.combinedActionSlots.remove(slot);
               } else {
                  SkillCalculator.this.combinedActionSlots.add(slot);
               }

               slot.setSelected(!slot.isSelected());
               SkillCalculator.this.updateCombinedAction();
            }
         });
      }

      if (this.client.getWorldType().contains(WorldType.MEMBERS)) {
         this.uiActionSlots.forEach(this::add);
         this.revalidate();
         this.repaint();
      } else {
         this.clientThread.invokeLater(() -> {
            List<UIActionSlot> membersActions = (List)this.uiActionSlots.stream().filter((slot) -> {
               return !slot.getAction().isMembers(this.itemManager);
            }).collect(Collectors.toList());
            SwingUtilities.invokeLater(() -> {
               if (membersActions.isEmpty()) {
                  this.add(EMPTY_PANEL);
               } else {
                  membersActions.forEach(this::add);
               }

               this.revalidate();
               this.repaint();
            });
         });
      }

   }

   private void calculate() {
      Iterator var1 = this.uiActionSlots.iterator();

      while(var1.hasNext()) {
         UIActionSlot slot = (UIActionSlot)var1.next();
         int actionCount = 0;
         int neededXP = this.targetXP - this.currentXP;
         SkillAction action = slot.getAction();
         float bonus = 1.0F;
         Iterator var7 = this.currentBonuses.iterator();

         while(var7.hasNext()) {
            SkillBonus skillBonus = (SkillBonus)var7.next();
            if (action.isBonusApplicable(skillBonus)) {
               bonus *= skillBonus.getValue();
            }
         }

         int xp = (int)Math.floor((double)(action.getXp() * 10.0F * bonus * (float)this.xpMultiplier));
         if (neededXP > 0) {
            neededXP *= 10;
            actionCount = (neededXP - 1) / xp + 1;
         }

         int var10001 = action.getLevel();
         slot.setText("Lvl. " + var10001 + " (" + formatXPActionString(xp, actionCount, "exp) - "));
         slot.setAvailable(this.currentLevel >= action.getLevel());
         slot.setOverlapping(action.getLevel() < this.targetLevel);
         slot.setValue(xp);
      }

      this.updateCombinedAction();
   }

   private static String formatXPActionString(int xp, int actionCount, String expExpression) {
      int integer = xp / 10;
      int frac = xp % 10;
      String var10000 = String.valueOf(frac != 0 ? "" + integer + "." + frac : integer);
      return var10000 + expExpression + NumberFormat.getIntegerInstance().format((long)actionCount) + (actionCount == 1 ? " action" : " actions");
   }

   private void updateInputFields() {
      if (this.targetXP < this.currentXP) {
         this.targetLevel = enforceSkillBounds(this.currentLevel + 1);
         this.targetXP = Experience.getXpForLevel(this.targetLevel);
      }

      String cXP = String.format("%,d", this.currentXP);
      String tXP = String.format("%,d", this.targetXP);
      String nXP = String.format("%,d", this.targetXP - this.currentXP);
      this.uiInput.setCurrentLevelInput(this.currentLevel);
      this.uiInput.setCurrentXPInput(cXP);
      this.uiInput.setTargetLevelInput(this.targetLevel);
      this.uiInput.setTargetXPInput(tXP);
      this.uiInput.setNeededXP(nXP + " XP required to reach target XP");
      this.uiInput.setXPMultiplier(this.xpMultiplier);
      this.calculate();
   }

   private void onFieldCurrentLevelUpdated() {
      this.currentLevel = enforceSkillBounds(this.uiInput.getCurrentLevelInput());
      this.currentXP = Experience.getXpForLevel(this.currentLevel);
      this.updateInputFields();
   }

   private void onFieldXPMultiplierUpdated() {
      this.xpMultiplier = enforceMultiplierBounds(this.uiInput.getXPMultiplierInput());
      this.updateInputFields();
   }

   private void onFieldCurrentXPUpdated() {
      this.currentXP = enforceXPBounds(this.uiInput.getCurrentXPInput());
      this.currentLevel = Experience.getLevelForXp(this.currentXP);
      this.updateInputFields();
   }

   private void onFieldTargetLevelUpdated() {
      this.targetLevel = enforceSkillBounds(this.uiInput.getTargetLevelInput());
      this.targetXP = Experience.getXpForLevel(this.targetLevel);
      this.updateInputFields();
   }

   private void onFieldTargetXPUpdated() {
      this.targetXP = enforceXPBounds(this.uiInput.getTargetXPInput());
      this.targetLevel = Experience.getLevelForXp(this.targetXP);
      this.updateInputFields();
   }

   private static int enforceSkillBounds(int input) {
      return Math.min(126, Math.max(1, input));
   }

   private static int enforceXPBounds(int input) {
      return Math.min(200000000, Math.max(0, input));
   }

   private static int enforceMultiplierBounds(int input) {
      return Math.min(32, Math.max(1, input));
   }

   private void onSearch() {
      this.uiActionSlots.forEach((slot) -> {
         if (slotContainsText(slot, this.searchBar.getText())) {
            super.add(slot);
         } else {
            super.remove(slot);
         }

         this.revalidate();
      });
   }

   private static boolean slotContainsText(UIActionSlot slot, String text) {
      return slot.getActionName().toLowerCase().contains(text.toLowerCase());
   }

   private static FocusAdapter buildFocusAdapter(final Consumer<FocusEvent> focusLostConsumer) {
      return new FocusAdapter() {
         public void focusLost(FocusEvent e) {
            focusLostConsumer.accept(e);
         }
      };
   }

   private static int endGoalVarpForSkill(Skill skill) {
      switch (skill) {
         case ATTACK:
            return 1253;
         case MINING:
            return 1265;
         case WOODCUTTING:
            return 1270;
         case DEFENCE:
            return 1257;
         case MAGIC:
            return 1256;
         case RANGED:
            return 1255;
         case HITPOINTS:
            return 1258;
         case AGILITY:
            return 1260;
         case STRENGTH:
            return 1254;
         case PRAYER:
            return 1259;
         case SLAYER:
            return 1272;
         case FISHING:
            return 1267;
         case RUNECRAFT:
            return 1264;
         case HERBLORE:
            return 1261;
         case FIREMAKING:
            return 1269;
         case CONSTRUCTION:
            return 1274;
         case HUNTER:
            return 1275;
         case COOKING:
            return 1268;
         case FARMING:
            return 1273;
         case CRAFTING:
            return 1263;
         case SMITHING:
            return 1266;
         case THIEVING:
            return 1262;
         case FLETCHING:
            return 1271;
         default:
            throw new IllegalArgumentException();
      }
   }

   static {
      EMPTY_PANEL.setHorizontalAlignment(0);
      EMPTY_PANEL.setBorder(new EmptyBorder(50, 0, 0, 0));
   }
}
