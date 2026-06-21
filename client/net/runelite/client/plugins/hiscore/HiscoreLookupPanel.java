package net.runelite.client.plugins.hiscore;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import custom.UpdateHiscoreScript;
import custom.UpdateHiscoreTopListScript;
import custom.UpdateHiscoreTopListScript.TopListType;
import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import javax.inject.Inject;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;
import net.runelite.api.Experience;
import net.runelite.client.game.SpriteManager;
import net.runelite.client.hiscore.CountCategory;
import net.runelite.client.hiscore.CountId;
import net.runelite.client.hiscore.HiscoreResult;
import net.runelite.client.hiscore.HiscoreSkill;
import net.runelite.client.hiscore.HiscoreSkillType;
import net.runelite.client.hiscore.Skill;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.FontManager;
import net.runelite.client.ui.components.IconTextField;
import net.runelite.client.util.ImageUtil;
import net.runelite.client.util.QuantityFormatter;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringEscapeUtils;

class HiscoreLookupPanel extends JPanel {
   private static final int MAX_SEARCH_LENGTH = 64;
   private static final int SEARCH_TIMEOUT_MILLIS = 30000;
   private static final List<HiscoreSkill> SKILLS;
   private static final HiscoreSkill[] SCHEMA_SKILL_BY_ID;
   private static final Map<HiscoreSkill, String> TOPLIST_SKILL_IDS;
   private final HiscoreConfig config;
   private final SpriteManager spriteManager;
   private final IconTextField searchBar;
   private final JLabel nameLabel;
   private Consumer<String> searchHandler = (ignored) -> {
   };
   private BiConsumer<UpdateHiscoreTopListScript.TopListType, String> topListSelectionHandler = (ignoredType, ignoredTarget) -> {
   };
   private boolean searchPending;
   private Timer searchTimeoutTimer;
   private final Map<HiscoreSkill, JLabel> skillLabels = new HashMap();
   private final Map<CountId, JLabel> countLabels = new EnumMap(CountId.class);
   private Map<String, Integer> countGraphicMapping = Collections.emptyMap();
   private JPanel otherCountsPanel;
   private JPanel otherCountsHeader;
   private JPanel perksHeader;
   private JPanel perksPanel;
   private final List<JLabel> perkLabels = new ArrayList();
   private JLabel ranksFreshnessLabel;

   @Inject
   HiscoreLookupPanel(HiscoreConfig config, SpriteManager spriteManager) {
      this.config = config;
      this.spriteManager = spriteManager;
      this.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));
      this.setBackground(ColorScheme.DARK_GRAY_COLOR);
      this.setFocusable(true);
      this.setLayout(new GridBagLayout());
      GridBagConstraints c = new GridBagConstraints();
      c.fill = 2;
      c.gridx = 0;
      c.gridy = 0;
      c.weightx = 1.0;
      c.weighty = 0.0;
      c.insets = new Insets(0, 0, 10, 0);
      this.searchBar = new IconTextField();
      this.searchBar.setIcon(IconTextField.Icon.SEARCH);
      this.searchBar.setPreferredSize(new Dimension(205, 30));
      this.searchBar.setBackground(ColorScheme.DARKER_GRAY_COLOR);
      this.searchBar.setHoverBackgroundColor(ColorScheme.DARK_GRAY_HOVER_COLOR);
      this.searchBar.setMinimumSize(new Dimension(0, 30));
      this.searchBar.addActionListener((e) -> {
         this.requestLookup();
      });
      this.searchBar.addClearListener(() -> {
         this.searchBar.setIcon(IconTextField.Icon.SEARCH);
      });
      this.add(this.searchBar, c);
      ++c.gridy;
      this.nameLabel = new JLabel("No player selected");
      this.nameLabel.setFont(FontManager.getRunescapeSmallFont());
      this.nameLabel.setForeground(ColorScheme.LIGHT_GRAY_COLOR);
      this.nameLabel.setHorizontalAlignment(0);
      this.nameLabel.setPreferredSize(new Dimension(205, 20));
      this.add(this.nameLabel, c);
      ++c.gridy;
      JPanel statsPanel = new JPanel(new GridLayout(8, 3));
      statsPanel.setBackground(ColorScheme.DARKER_GRAY_COLOR);
      statsPanel.setBorder(new EmptyBorder(5, 0, 5, 0));
      Iterator var5 = SKILLS.iterator();

      while(var5.hasNext()) {
         HiscoreSkill skill = (HiscoreSkill)var5.next();
         statsPanel.add(this.makeHiscorePanel(skill));
      }

      this.add(statsPanel, c);
      ++c.gridy;
      JPanel totalPanel = new JPanel(new GridLayout(1, 2));
      totalPanel.setBackground(ColorScheme.DARKER_GRAY_COLOR);
      totalPanel.add(this.makeHiscorePanel((HiscoreSkill)null));
      totalPanel.add(this.makeHiscorePanel(HiscoreSkill.OVERALL));
      this.add(totalPanel, c);
      ++c.gridy;
      Map<CountCategory, List<CountId>> idsByCategory = new EnumMap(CountCategory.class);
      CountId[] var7 = CountId.values();
      int var8 = var7.length;

      int var9;
      for(var9 = 0; var9 < var8; ++var9) {
         CountId countId = var7[var9];
         ((List)idsByCategory.computeIfAbsent(countId.getCategory(), (ignored) -> {
            return new ArrayList();
         })).add(countId);
      }

      CountCategory[] var18 = CountCategory.values();
      var8 = var18.length;

      for(var9 = 0; var9 < var8; ++var9) {
         CountCategory category = var18[var9];
         if (category != CountCategory.OTHER) {
            List<CountId> categoryIds = (List)idsByCategory.get(category);
            if (categoryIds != null && !categoryIds.isEmpty()) {
               this.add(makeSectionHeader(category.getDisplayName()), c);
               ++c.gridy;
               JPanel countPanel = new JPanel(new GridLayout(0, 3));
               countPanel.setBackground(ColorScheme.DARKER_GRAY_COLOR);
               Iterator var13 = categoryIds.iterator();

               while(var13.hasNext()) {
                  CountId countId = (CountId)var13.next();
                  countPanel.add(this.makeCountEntryPanel(countId));
               }

               int remainder = categoryIds.size() % 3;
               if (remainder != 0) {
                  for(int i = 0; i < 3 - remainder; ++i) {
                     JPanel filler = new JPanel();
                     filler.setBackground(ColorScheme.DARKER_GRAY_COLOR);
                     countPanel.add(filler);
                  }
               }

               this.add(countPanel, c);
               ++c.gridy;
            }
         }
      }

      this.otherCountsHeader = makeSectionHeader(CountCategory.OTHER.getDisplayName());
      this.otherCountsHeader.setVisible(false);
      this.add(this.otherCountsHeader, c);
      ++c.gridy;
      this.otherCountsPanel = new JPanel(new GridLayout(0, 3));
      this.otherCountsPanel.setBackground(ColorScheme.DARKER_GRAY_COLOR);
      this.otherCountsPanel.setVisible(false);
      this.add(this.otherCountsPanel, c);
      ++c.gridy;
      this.perksHeader = makeSectionHeader("League Perks");
      this.perksHeader.setVisible(false);
      this.add(this.perksHeader, c);
      ++c.gridy;
      this.perksPanel = new JPanel(new GridLayout(0, 1));
      this.perksPanel.setBackground(ColorScheme.DARKER_GRAY_COLOR);
      this.perksPanel.setBorder(new EmptyBorder(2, 0, 5, 0));
      this.perksPanel.setVisible(false);

      for(int i = 0; i < 6; ++i) {
         JLabel perkLabel = new JLabel(" ");
         perkLabel.setFont(FontManager.getRunescapeSmallFont());
         perkLabel.setForeground(ColorScheme.LIGHT_GRAY_COLOR);
         perkLabel.setBorder(new EmptyBorder(2, 4, 2, 4));
         this.perkLabels.add(perkLabel);
         this.perksPanel.add(perkLabel);
      }

      this.add(this.perksPanel, c);
      ++c.gridy;
      this.ranksFreshnessLabel = new JLabel(" ");
      this.ranksFreshnessLabel.setFont(FontManager.getRunescapeSmallFont());
      this.ranksFreshnessLabel.setForeground(ColorScheme.LIGHT_GRAY_COLOR);
      this.ranksFreshnessLabel.setHorizontalAlignment(0);
      this.ranksFreshnessLabel.setBorder(new EmptyBorder(4, 0, 4, 0));
      this.ranksFreshnessLabel.setVisible(false);
      this.add(this.ranksFreshnessLabel, c);
   }

   void setSearchHandler(Consumer<String> searchHandler) {
      this.searchHandler = searchHandler == null ? (ignored) -> {
      } : searchHandler;
   }

   void setTopListSelectionHandler(BiConsumer<UpdateHiscoreTopListScript.TopListType, String> topListSelectionHandler) {
      this.topListSelectionHandler = topListSelectionHandler == null ? (ignoredType, ignoredTarget) -> {
      } : topListSelectionHandler;
   }

   void shutdown() {
      if (this.searchTimeoutTimer != null) {
         this.searchTimeoutTimer.stop();
         this.searchTimeoutTimer = null;
      }

      this.searchHandler = (ignored) -> {
      };
      this.topListSelectionHandler = (ignoredType, ignoredTarget) -> {
      };
   }

   void setCountGraphicMapping(Map<String, Integer> mapping) {
      assert SwingUtilities.isEventDispatchThread();

      this.countGraphicMapping = mapping;
      Iterator var2 = this.countLabels.entrySet().iterator();

      while(var2.hasNext()) {
         Map.Entry<CountId, JLabel> entry = (Map.Entry)var2.next();
         Integer graphic = (Integer)mapping.get(((CountId)entry.getKey()).getId());
         if (graphic != null && graphic >= 0) {
            this.loadCountSprite((JLabel)entry.getValue(), graphic);
         }
      }

   }

   public void displayHiscoreData(UpdateHiscoreScript packet) {
      assert SwingUtilities.isEventDispatchThread();

      this.finishLookupRequest();
      this.searchBar.setText(packet.getDisplayName());
      this.nameLabel.setText(packet.getDisplayName());
      Iterator var2 = this.skillLabels.entrySet().iterator();

      while(var2.hasNext()) {
         Map.Entry<HiscoreSkill, JLabel> entry = (Map.Entry)var2.next();
         HiscoreSkill skill = (HiscoreSkill)entry.getKey();
         JLabel label = (JLabel)entry.getValue();
         HiscoreSkillType skillType = skill == null ? HiscoreSkillType.SKILL : skill.getType();
         label.setText(pad("--", skillType));
         label.setToolTipText(skill == null ? "Combat" : skill.getName());
      }

      ImmutableMap.Builder<HiscoreSkill, Skill> skillBuilder = ImmutableMap.builder();

      for(int skillId = 0; skillId < SCHEMA_SKILL_BY_ID.length; ++skillId) {
         UpdateHiscoreScript.SkillData data = (UpdateHiscoreScript.SkillData)packet.getSkills().get(skillId);
         if (data != null) {
            skillBuilder.put(SCHEMA_SKILL_BY_ID[skillId], new Skill(data.getRank(), data.getRankGlobal(), data.getLevel(), data.getXp(), data.getPrestige(), data.getPrestigeXp()));
         }
      }

      skillBuilder.put(HiscoreSkill.OVERALL, new Skill(packet.getTotalRank(), packet.getTotalRankGlobal(), packet.getTotalLevel(), packet.getTotalXp(), packet.getTotalPrestige(), packet.getTotalPrestigeXp()));
      Map<String, Long> countsMap = new LinkedHashMap();
      Map<String, Integer> countRanksMap = new LinkedHashMap();
      Map<String, Integer> countRanksGlobalMap = new LinkedHashMap();
      Iterator var15 = packet.getCounts().iterator();

      while(var15.hasNext()) {
         UpdateHiscoreScript.CountEntry count = (UpdateHiscoreScript.CountEntry)var15.next();
         countsMap.put(count.getId(), count.getCount());
         countRanksMap.put(count.getId(), count.getRank());
         countRanksGlobalMap.put(count.getId(), count.getRankGlobal());
      }

      UpdateHiscoreScript.Perks perks = packet.getPerks();
      Object perksList;
      if (perks == null) {
         perksList = Collections.emptyList();
      } else {
         perksList = ImmutableList.of(perks.getPerk1() == null ? "" : perks.getPerk1(), perks.getPerk2() == null ? "" : perks.getPerk2(), perks.getPerk3() == null ? "" : perks.getPerk3(), perks.getPerk4() == null ? "" : perks.getPerk4(), perks.getPerk5() == null ? "" : perks.getPerk5(), perks.getPerk6() == null ? "" : perks.getPerk6());
      }

      HiscoreResult result = new HiscoreResult(packet.getUsername(), packet.getDisplayName(), packet.getIronmanMode(), packet.getGameMode(), packet.getPrivilege(), packet.getDonatorRank(), skillBuilder.build(), Collections.unmodifiableMap(countsMap), Collections.unmodifiableMap(countRanksMap), Collections.unmodifiableMap(countRanksGlobalMap), (List)perksList, packet.getRanksUpdatedAtMillis());
      this.applyHiscoreResult(result);
   }

   private JPanel makeCountEntryPanel(CountId countId) {
      JLabel label = new JLabel();
      label.setToolTipText(countId.getDisplayName());
      label.setFont(FontManager.getRunescapeSmallFont());
      label.setForeground(ColorScheme.LIGHT_GRAY_COLOR);
      label.setText(StringUtils.leftPad("--", 4));
      label.setIconTextGap(4);
      JPanel panel = new JPanel();
      panel.setBackground(ColorScheme.DARKER_GRAY_COLOR);
      panel.setBorder(new EmptyBorder(2, 0, 2, 0));
      this.countLabels.put(countId, label);
      panel.add(label);
      this.installTopListSelection(panel, label, TopListType.COUNT, countId.getId());
      return panel;
   }

   private JPanel makeHiscorePanel(HiscoreSkill skill) {
      HiscoreSkillType skillType = skill == null ? HiscoreSkillType.SKILL : skill.getType();
      JLabel label = new JLabel();
      label.setToolTipText(skill == null ? "Combat" : skill.getName());
      label.setFont(FontManager.getRunescapeSmallFont());
      label.setForeground(ColorScheme.LIGHT_GRAY_COLOR);
      label.setText(pad("--", skillType));
      this.spriteManager.getSpriteAsync(skill == null ? 168 : skill.getSpriteId(), 0, (Consumer)((sprite) -> {
         SwingUtilities.invokeLater(() -> {
            BufferedImage scaledSprite = ImageUtil.resizeImage(ImageUtil.resizeCanvas(sprite, 25, 25), 20, 20);
            label.setIcon(new ImageIcon(scaledSprite));
         });
      }));
      boolean totalLabel = skill == HiscoreSkill.OVERALL || skill == null;
      label.setIconTextGap(totalLabel ? 10 : 4);
      JPanel skillPanel = new JPanel();
      skillPanel.setBackground(ColorScheme.DARKER_GRAY_COLOR);
      skillPanel.setBorder(new EmptyBorder(2, 0, 2, 0));
      this.skillLabels.put(skill, label);
      skillPanel.add(label);
      String topListSkillId = getTopListSkillId(skill);
      if (topListSkillId != null) {
         this.installTopListSelection(skillPanel, label, TopListType.SKILL, topListSkillId);
      }

      return skillPanel;
   }

   private void requestLookup() {
      assert SwingUtilities.isEventDispatchThread();

      if (!this.searchPending) {
         String username = StringUtils.trimToEmpty(this.searchBar.getText());
         if (username.isEmpty()) {
            this.searchBar.setIcon(IconTextField.Icon.SEARCH);
         } else if (username.length() > 64) {
            this.searchBar.setIcon(IconTextField.Icon.ERROR);
         } else {
            this.searchPending = true;
            this.searchBar.setEditable(false);
            this.searchBar.setIcon(IconTextField.Icon.LOADING);
            this.requestFocusInWindow();
            this.searchTimeoutTimer = new Timer(30000, (e) -> {
               this.searchPending = false;
               this.searchBar.setEditable(true);
               this.searchBar.setIcon(IconTextField.Icon.ERROR);
            });
            this.searchTimeoutTimer.setRepeats(false);
            this.searchTimeoutTimer.start();
            this.searchHandler.accept(username);
         }
      }
   }

   private void finishLookupRequest() {
      assert SwingUtilities.isEventDispatchThread();

      this.searchPending = false;
      if (this.searchTimeoutTimer != null) {
         this.searchTimeoutTimer.stop();
         this.searchTimeoutTimer = null;
      }

      this.searchBar.setEditable(true);
      this.searchBar.setIcon(IconTextField.Icon.SEARCH);
   }

   private void applyHiscoreResult(HiscoreResult result) {
      assert SwingUtilities.isEventDispatchThread();

      this.repaint();
      Iterator var2 = this.skillLabels.entrySet().iterator();

      while(true) {
         while(var2.hasNext()) {
            Map.Entry<HiscoreSkill, JLabel> entry = (Map.Entry)var2.next();
            HiscoreSkill skill = (HiscoreSkill)entry.getKey();
            JLabel label = (JLabel)entry.getValue();
            if (skill == null) {
               if (result.getPlayer() != null) {
                  int combatLevel = Experience.getCombatLevel(result.getSkill(HiscoreSkill.ATTACK).getLevel(), result.getSkill(HiscoreSkill.STRENGTH).getLevel(), result.getSkill(HiscoreSkill.DEFENCE).getLevel(), result.getSkill(HiscoreSkill.HITPOINTS).getLevel(), result.getSkill(HiscoreSkill.MAGIC).getLevel(), result.getSkill(HiscoreSkill.RANGED).getLevel(), result.getSkill(HiscoreSkill.PRAYER).getLevel());
                  label.setText(Integer.toString(combatLevel));
               }

               label.setToolTipText(this.detailsHtml(result, skill));
            } else {
               Skill requestedSkill;
               if ((requestedSkill = result.getSkill(skill)) != null) {
                  long exp = requestedSkill.getExperience();
                  boolean isSkill = skill.getType() == HiscoreSkillType.SKILL;
                  int level = -1;
                  if (this.config.virtualLevels() && isSkill && exp > -1L) {
                     level = Experience.getLevelForXp((int)exp);
                  } else if (!isSkill || exp != -1L) {
                     level = requestedSkill.getLevel();
                  }

                  if (level > 0) {
                     label.setText(pad(formatLevel(level), skill.getType()));
                  }

                  label.setToolTipText(this.detailsHtml(result, skill));
               }
            }
         }

         this.applyCountsAndPerks(result);
         return;
      }
   }

   private void applyCountsAndPerks(HiscoreResult result) {
      Map<String, Long> counts = result.getCounts();
      Iterator var3 = this.countLabels.entrySet().iterator();

      while(var3.hasNext()) {
         Map.Entry<CountId, JLabel> labelEntry = (Map.Entry)var3.next();
         CountId countId = (CountId)labelEntry.getKey();
         JLabel label = (JLabel)labelEntry.getValue();
         Long count = (Long)counts.get(countId.getId());
         String text = count != null ? StringUtils.leftPad(formatLevel((int)Math.min(count, 2147483647L)), 4) : StringUtils.leftPad("--", 4);
         label.setText(text);
         label.setToolTipText(countTooltipHtml(countId.getDisplayName(), count, result.getCountRank(countId.getId()), result.getCountRankGlobal(countId.getId())));
      }

      this.otherCountsPanel.removeAll();
      List<Map.Entry<String, Long>> unknownCounts = new ArrayList();
      Iterator var11 = counts.entrySet().iterator();

      while(var11.hasNext()) {
         Map.Entry<String, Long> entry = (Map.Entry)var11.next();
         if (CountId.fromId((String)entry.getKey()) == null) {
            unknownCounts.add(entry);
         }
      }

      boolean hasOther = !unknownCounts.isEmpty();
      if (hasOther) {
         Iterator var14 = unknownCounts.iterator();

         while(var14.hasNext()) {
            Map.Entry<String, Long> entry = (Map.Entry)var14.next();
            JLabel label = new JLabel();
            label.setFont(FontManager.getRunescapeSmallFont());
            label.setForeground(ColorScheme.LIGHT_GRAY_COLOR);
            label.setToolTipText(countTooltipHtml(prettifyCountId((String)entry.getKey()), (Long)entry.getValue(), result.getCountRank((String)entry.getKey()), result.getCountRankGlobal((String)entry.getKey())));
            label.setText(StringUtils.leftPad(formatLevel((int)Math.min((Long)entry.getValue(), 2147483647L)), 4));
            label.setIconTextGap(4);
            Integer graphic = (Integer)this.countGraphicMapping.get(entry.getKey());
            if (graphic != null && graphic >= 0) {
               this.loadCountSprite(label, graphic);
            }

            JPanel panel = new JPanel();
            panel.setBackground(ColorScheme.DARKER_GRAY_COLOR);
            panel.setBorder(new EmptyBorder(2, 0, 2, 0));
            panel.add(label);
            this.installTopListSelection(panel, label, TopListType.COUNT, (String)entry.getKey());
            this.otherCountsPanel.add(panel);
         }

         int otherRemainder = unknownCounts.size() % 3;
         if (otherRemainder != 0) {
            for(int i = 0; i < 3 - otherRemainder; ++i) {
               JPanel filler = new JPanel();
               filler.setBackground(ColorScheme.DARKER_GRAY_COLOR);
               this.otherCountsPanel.add(filler);
            }
         }
      }

      this.otherCountsHeader.setVisible(hasOther);
      this.otherCountsPanel.setVisible(hasOther);
      this.otherCountsPanel.revalidate();
      this.otherCountsPanel.repaint();
      List<String> perks = result.getPerks();
      boolean hasPerks = !perks.isEmpty();

      for(int i = 0; i < this.perkLabels.size(); ++i) {
         JLabel perkLabel = (JLabel)this.perkLabels.get(i);
         if (i < perks.size() && !((String)perks.get(i)).isEmpty()) {
            Object var10001 = perks.get(i);
            perkLabel.setText("* " + prettifyCountId((String)var10001));
            perkLabel.setVisible(true);
         } else {
            perkLabel.setText(" ");
            perkLabel.setVisible(false);
         }
      }

      this.perksHeader.setVisible(hasPerks);
      this.perksPanel.setVisible(hasPerks);
      this.perksPanel.revalidate();
      this.perksPanel.repaint();
      this.updateRanksFreshnessLabel(result.getRanksUpdatedAtMillis());
   }

   private void updateRanksFreshnessLabel(long updatedAtMillis) {
      if (updatedAtMillis <= 0L) {
         this.ranksFreshnessLabel.setText("Ranks not yet computed");
         this.ranksFreshnessLabel.setVisible(true);
      } else {
         long ageMillis = System.currentTimeMillis() - updatedAtMillis;
         this.ranksFreshnessLabel.setText("Ranks updated " + formatAge(ageMillis) + " ago");
         this.ranksFreshnessLabel.setVisible(true);
      }
   }

   @VisibleForTesting
   static String formatAge(long ageMillis) {
      if (ageMillis < 0L) {
         ageMillis = 0L;
      }

      long seconds = ageMillis / 1000L;
      if (seconds < 60L) {
         return "" + seconds + "s";
      } else {
         long minutes = seconds / 60L;
         if (minutes < 60L) {
            return "" + minutes + "m";
         } else {
            long hours = minutes / 60L;
            if (hours < 24L) {
               return "" + hours + "h";
            } else {
               long days = hours / 24L;
               return "" + days + "d";
            }
         }
      }
   }

   private void installTopListSelection(final JPanel panel, JLabel label, final UpdateHiscoreTopListScript.TopListType type, final String targetId) {
      MouseAdapter listener = new MouseAdapter() {
         public void mouseEntered(MouseEvent e) {
            panel.setBackground(ColorScheme.DARK_GRAY_HOVER_COLOR);
         }

         public void mouseExited(MouseEvent e) {
            panel.setBackground(ColorScheme.DARKER_GRAY_COLOR);
         }

         public void mouseClicked(MouseEvent e) {
            HiscoreLookupPanel.this.topListSelectionHandler.accept(type, targetId);
         }
      };
      panel.setCursor(Cursor.getPredefinedCursor(12));
      label.setCursor(Cursor.getPredefinedCursor(12));
      panel.addMouseListener(listener);
      label.addMouseListener(listener);
   }

   private void loadCountSprite(JLabel label, int graphic) {
      if (graphic >= 0) {
         this.spriteManager.getSpriteAsync(graphic, 0, (Consumer)((sprite) -> {
            SwingUtilities.invokeLater(() -> {
               BufferedImage scaledSprite = ImageUtil.resizeImage(ImageUtil.resizeCanvas(sprite, 25, 25), 20, 20);
               label.setIcon(new ImageIcon(scaledSprite));
            });
         }));
      }

   }

   private static JPanel makeSectionHeader(String title) {
      JPanel panel = new JPanel(new BorderLayout());
      panel.setBackground(ColorScheme.DARK_GRAY_COLOR);
      panel.setBorder(new EmptyBorder(8, 0, 2, 0));
      JLabel label = new JLabel(title);
      label.setFont(FontManager.getRunescapeBoldFont());
      label.setForeground(ColorScheme.BRAND_ORANGE);
      panel.add(label, "West");
      return panel;
   }

   static String prettifyCountId(String rawId) {
      if (rawId != null && !rawId.isEmpty()) {
         String[] words = rawId.replace('-', '_').split("_");
         StringBuilder sb = new StringBuilder(rawId.length());
         String[] var3 = words;
         int var4 = words.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            String word = var3[var5];
            if (!word.isEmpty()) {
               if (sb.length() > 0) {
                  sb.append(' ');
               }

               sb.append(Character.toUpperCase(word.charAt(0)));
               if (word.length() > 1) {
                  sb.append(word.substring(1));
               }
            }
         }

         return sb.toString();
      } else {
         return rawId;
      }
   }

   private static String getTopListSkillId(HiscoreSkill skill) {
      return skill == null ? null : (String)TOPLIST_SKILL_IDS.get(skill);
   }

   private String detailsHtml(HiscoreResult result, HiscoreSkill skill) {
      String openingTags = "<html><body style = 'padding: 5px;color:#989898'>";
      String closingTags = "</html><body>";
      String content = "";
      int progress;
      Skill requestedSkill;
      int currentXp;
      int currentLevel;
      if (skill == null) {
         double combatLevel = Experience.getCombatLevelPrecise(result.getSkill(HiscoreSkill.ATTACK).getLevel(), result.getSkill(HiscoreSkill.STRENGTH).getLevel(), result.getSkill(HiscoreSkill.DEFENCE).getLevel(), result.getSkill(HiscoreSkill.HITPOINTS).getLevel(), result.getSkill(HiscoreSkill.MAGIC).getLevel(), result.getSkill(HiscoreSkill.RANGED).getLevel(), result.getSkill(HiscoreSkill.PRAYER).getLevel());
         double combatExperience = (double)(result.getSkill(HiscoreSkill.ATTACK).getExperience() + result.getSkill(HiscoreSkill.STRENGTH).getExperience() + result.getSkill(HiscoreSkill.DEFENCE).getExperience() + result.getSkill(HiscoreSkill.HITPOINTS).getExperience() + result.getSkill(HiscoreSkill.MAGIC).getExperience() + result.getSkill(HiscoreSkill.RANGED).getExperience() + result.getSkill(HiscoreSkill.PRAYER).getExperience());
         content = content + "<p><span style = 'color:white'>Combat</span></p>";
         content = content + "<p><span style = 'color:white'>Exact Combat Level:</span> " + QuantityFormatter.formatNumber(combatLevel) + "</p>";
         content = content + "<p><span style = 'color:white'>Experience:</span> " + QuantityFormatter.formatNumber(combatExperience) + "</p>";
      } else {
         switch (skill) {
            case OVERALL:
               requestedSkill = result.getSkill(skill);
               String rank = requestedSkill.getRank() == -1 ? "Unranked" : QuantityFormatter.formatNumber((long)requestedSkill.getRank());
               String rankGlobal = requestedSkill.getRankGlobal() == -1 ? "Unranked" : QuantityFormatter.formatNumber((long)requestedSkill.getRankGlobal());
               String exp = requestedSkill.getExperience() == -1L ? "Unranked" : QuantityFormatter.formatNumber(requestedSkill.getExperience());
               content = content + "<p><span style = 'color:white'>" + skill.getName() + "</span></p>";
               content = content + "<p><span style = 'color:white'>Global Rank:</span> " + rankGlobal + "</p>";
               content = content + "<p><span style = 'color:white'>Mode Rank:</span> " + rank + "</p>";
               content = content + "<p><span style = 'color:white'>Experience:</span> " + exp + "</p>";
               if (requestedSkill.getPrestige() > 0) {
                  content = content + "<p><span style = 'color:white'>Prestige:</span> " + requestedSkill.getPrestige() + "</p>";
               }

               if (requestedSkill.getPrestigeXp() > 0L) {
                  content = content + "<p><span style = 'color:white'>Prestige XP:</span> " + QuantityFormatter.formatNumber(requestedSkill.getPrestigeXp()) + "</p>";
               }
               break;
            default:
               requestedSkill = result.getSkill(skill);
               currentXp = requestedSkill == null ? -1 : requestedSkill.getRank();
               currentLevel = requestedSkill == null ? -1 : requestedSkill.getRankGlobal();
               long experience = requestedSkill == null ? -1L : requestedSkill.getExperience();
               String rankStr = currentXp == -1 ? "Unranked" : QuantityFormatter.formatNumber((long)currentXp);
               String rankGlobalStr = currentLevel == -1 ? "Unranked" : QuantityFormatter.formatNumber((long)currentLevel);
               String exp = experience == -1L ? "Unranked" : QuantityFormatter.formatNumber(experience);
               String remainingXp;
               if (experience == -1L) {
                  remainingXp = "Unranked";
               } else {
                  progress = Experience.getLevelForXp((int)experience);
                  remainingXp = progress + 1 <= 126 ? QuantityFormatter.formatNumber((long)Experience.getXpForLevel(progress + 1) - experience) : "0";
               }

               content = content + "<p><span style = 'color:white'>Skill:</span> " + skill.getName() + "</p>";
               content = content + "<p><span style = 'color:white'>Global Rank:</span> " + rankGlobalStr + "</p>";
               content = content + "<p><span style = 'color:white'>Mode Rank:</span> " + rankStr + "</p>";
               content = content + "<p><span style = 'color:white'>Experience:</span> " + exp + "</p>";
               content = content + "<p><span style = 'color:white'>Remaining XP:</span> " + remainingXp + "</p>";
               if (requestedSkill != null && requestedSkill.getPrestige() > 0) {
                  content = content + "<p><span style = 'color:white'>Prestige:</span> " + requestedSkill.getPrestige() + "</p>";
                  if (requestedSkill.getPrestigeXp() > 0L) {
                     content = content + "<p><span style = 'color:white'>Prestige XP:</span> " + QuantityFormatter.formatNumber(requestedSkill.getPrestigeXp()) + "</p>";
                  }
               }
         }
      }

      if (skill != null && skill.getType() == HiscoreSkillType.SKILL) {
         requestedSkill = result.getSkill(skill);
         if (requestedSkill != null && requestedSkill.getExperience() >= 0L) {
            currentXp = (int)requestedSkill.getExperience();
            currentLevel = Experience.getLevelForXp(currentXp);
            int xpForCurrentLevel = Experience.getXpForLevel(currentLevel);
            int xpForNextLevel = currentLevel + 1 <= 126 ? Experience.getXpForLevel(currentLevel + 1) : -1;
            double xpGained = (double)(currentXp - xpForCurrentLevel);
            double xpGoal = xpForNextLevel != -1 ? (double)(xpForNextLevel - xpForCurrentLevel) : 100.0;
            progress = (int)(xpGained / xpGoal * 100.0);
            content = content + "<div style = 'margin-top:3px'><div style = 'background: #070707; border: 1px solid #070707; height: 6px; width: 100%;'><div style = 'height: 6px; width: " + progress + "%; background: #dc8a00;'></div></div></div>";
         }
      }

      return openingTags + content + closingTags;
   }

   private static String countTooltipHtml(String displayName, Long count, int rank, int rankGlobal) {
      String openingTags = "<html><body style = 'padding: 5px;color:#989898'>";
      String closingTags = "</body></html>";
      String safeDisplayName = StringEscapeUtils.escapeHtml4(displayName == null ? "" : displayName);
      String rankStr = rank == -1 ? "Unranked" : QuantityFormatter.formatNumber((long)rank);
      String rankGlobalStr = rankGlobal == -1 ? "Unranked" : QuantityFormatter.formatNumber((long)rankGlobal);
      String countStr = count == null ? "0" : QuantityFormatter.formatNumber(count);
      return openingTags + "<p><span style = 'color:white'>" + safeDisplayName + "</span></p><p><span style = 'color:white'>Global Rank:</span> " + rankGlobalStr + "</p><p><span style = 'color:white'>Mode Rank:</span> " + rankStr + "</p><p><span style = 'color:white'>Count:</span> " + countStr + "</p>" + closingTags;
   }

   @VisibleForTesting
   static String formatLevel(int level) {
      return level < 10000 ? Integer.toString(level) : level / 1000 + "k";
   }

   private static String pad(String str, HiscoreSkillType type) {
      int pad = type == HiscoreSkillType.BOSS ? 4 : 2;
      return StringUtils.leftPad(str, pad);
   }

   static {
      SKILLS = ImmutableList.of(HiscoreSkill.ATTACK, HiscoreSkill.HITPOINTS, HiscoreSkill.MINING, HiscoreSkill.STRENGTH, HiscoreSkill.AGILITY, HiscoreSkill.SMITHING, HiscoreSkill.DEFENCE, HiscoreSkill.HERBLORE, HiscoreSkill.FISHING, HiscoreSkill.RANGED, HiscoreSkill.THIEVING, HiscoreSkill.COOKING, new HiscoreSkill[]{HiscoreSkill.PRAYER, HiscoreSkill.CRAFTING, HiscoreSkill.FIREMAKING, HiscoreSkill.MAGIC, HiscoreSkill.FLETCHING, HiscoreSkill.WOODCUTTING, HiscoreSkill.RUNECRAFT, HiscoreSkill.SLAYER, HiscoreSkill.FARMING, HiscoreSkill.CONSTRUCTION, HiscoreSkill.HUNTER, HiscoreSkill.SAILING});
      SCHEMA_SKILL_BY_ID = new HiscoreSkill[]{HiscoreSkill.ATTACK, HiscoreSkill.DEFENCE, HiscoreSkill.STRENGTH, HiscoreSkill.HITPOINTS, HiscoreSkill.RANGED, HiscoreSkill.PRAYER, HiscoreSkill.MAGIC, HiscoreSkill.COOKING, HiscoreSkill.WOODCUTTING, HiscoreSkill.FLETCHING, HiscoreSkill.FISHING, HiscoreSkill.FIREMAKING, HiscoreSkill.CRAFTING, HiscoreSkill.SMITHING, HiscoreSkill.MINING, HiscoreSkill.HERBLORE, HiscoreSkill.AGILITY, HiscoreSkill.THIEVING, HiscoreSkill.SLAYER, HiscoreSkill.FARMING, HiscoreSkill.RUNECRAFT, HiscoreSkill.HUNTER, HiscoreSkill.CONSTRUCTION};
      TOPLIST_SKILL_IDS = ImmutableMap.builder().put(HiscoreSkill.OVERALL, "total").put(HiscoreSkill.ATTACK, "attack").put(HiscoreSkill.DEFENCE, "defence").put(HiscoreSkill.STRENGTH, "strength").put(HiscoreSkill.HITPOINTS, "hitpoints").put(HiscoreSkill.RANGED, "ranged").put(HiscoreSkill.PRAYER, "prayer").put(HiscoreSkill.MAGIC, "magic").put(HiscoreSkill.COOKING, "cooking").put(HiscoreSkill.WOODCUTTING, "woodcutting").put(HiscoreSkill.FLETCHING, "fletching").put(HiscoreSkill.FISHING, "fishing").put(HiscoreSkill.FIREMAKING, "firemaking").put(HiscoreSkill.CRAFTING, "crafting").put(HiscoreSkill.SMITHING, "smithing").put(HiscoreSkill.MINING, "mining").put(HiscoreSkill.HERBLORE, "herblore").put(HiscoreSkill.AGILITY, "agility").put(HiscoreSkill.THIEVING, "thieving").put(HiscoreSkill.SLAYER, "slayer").put(HiscoreSkill.FARMING, "farming").put(HiscoreSkill.RUNECRAFT, "runecrafting").put(HiscoreSkill.HUNTER, "hunter").put(HiscoreSkill.CONSTRUCTION, "construction").build();
   }
}
