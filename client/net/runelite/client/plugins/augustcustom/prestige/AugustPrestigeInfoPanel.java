package net.runelite.client.plugins.augustcustom.prestige;

import custom.model.PrestigeState;
import custom.model.SkillPrestigeInfo;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;
import net.runelite.api.Client;
import net.runelite.api.Skill;
import net.runelite.client.game.SkillIconManager;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.FontManager;
import net.runelite.client.ui.PluginPanel;

class AugustPrestigeInfoPanel extends PluginPanel {
   private final Client client;
   private final SkillIconManager iconManager;
   private final JPanel overallPanel;
   private final JPanel noSkillsView;
   private List<SkillPrestigeInfo> prestigeInfo;
   private final Map<Skill, SkillComponent> skillComponentMap;

   AugustPrestigeInfoPanel(Client client, SkillIconManager iconManager) {
      this.client = client;
      this.iconManager = iconManager;
      this.skillComponentMap = new HashMap();
      JTextArea prestigeInfoHeader = new JTextArea("Prestige Info");
      prestigeInfoHeader.setLineWrap(true);
      prestigeInfoHeader.setWrapStyleWord(true);
      prestigeInfoHeader.setEditable(false);
      prestigeInfoHeader.setOpaque(false);
      prestigeInfoHeader.setBorder(BorderFactory.createEmptyBorder());
      this.overallPanel = new JPanel();
      this.overallPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
      this.overallPanel.setBackground(ColorScheme.DARKER_GRAY_COLOR);
      this.overallPanel.setLayout(new BoxLayout(this.overallPanel, 1));
      this.overallPanel.setVisible(true);
      JTextArea noPrestigeInfo = new JTextArea("You have not prestiged any skills");
      noPrestigeInfo.setLineWrap(true);
      noPrestigeInfo.setWrapStyleWord(true);
      noPrestigeInfo.setEditable(false);
      noPrestigeInfo.setOpaque(false);
      noPrestigeInfo.setBorder(BorderFactory.createEmptyBorder());
      this.noSkillsView = new JPanel();
      this.noSkillsView.setLayout(new BorderLayout());
      this.noSkillsView.setBorder(new EmptyBorder(5, 5, 5, 5));
      this.noSkillsView.setBackground(ColorScheme.DARKER_GRAY_COLOR);
      this.noSkillsView.add(noPrestigeInfo);
      this.overallPanel.add(this.noSkillsView);
      Skill[] var5 = Skill.values();
      int var6 = var5.length;

      for(int var7 = 0; var7 < var6; ++var7) {
         Skill skill = var5[var7];
         SkillComponent skillComponent = new SkillComponent(skill);
         skillComponent.setVisible(false);
         this.overallPanel.add(skillComponent);
         this.skillComponentMap.put(skill, skillComponent);
      }

      JPanel layoutPanel = new JPanel();
      layoutPanel.setLayout(new BoxLayout(layoutPanel, 1));
      layoutPanel.add(this.overallPanel);
      this.setBorder(new EmptyBorder(6, 6, 6, 6));
      this.setBackground(ColorScheme.DARK_GRAY_COLOR);
      this.setLayout(new BorderLayout());
      this.add(layoutPanel, "North");
   }

   public void setPrestigeInfo(List<SkillPrestigeInfo> prestigeInfo) {
      this.prestigeInfo = prestigeInfo;
      this.refresh();
   }

   public void refresh() {
      if (this.prestigeInfo != null && !this.prestigeInfo.isEmpty()) {
         List<SkillComponent> visible = new ArrayList();
         Iterator var10 = this.prestigeInfo.iterator();

         while(var10.hasNext()) {
            SkillPrestigeInfo info = (SkillPrestigeInfo)var10.next();
            Skill skill = Skill.values()[info.getSkillId()];
            int rank = info.getRank();
            int nextRankAt = info.getNextRankAt();
            PrestigeState state = info.getState();
            SkillComponent component = (SkillComponent)this.skillComponentMap.get(skill);
            component.update(rank, nextRankAt, state);
            visible.add(component);
         }

         if (!visible.isEmpty()) {
            this.noSkillsView.setVisible(false);
            var10 = visible.iterator();

            while(var10.hasNext()) {
               SkillComponent c = (SkillComponent)var10.next();
               c.setVisible(true);
            }
         }

         this.overallPanel.updateUI();
      } else {
         Iterator var1 = this.skillComponentMap.values().iterator();

         while(var1.hasNext()) {
            SkillComponent c = (SkillComponent)var1.next();
            c.setVisible(false);
         }

         this.noSkillsView.setVisible(true);
         this.overallPanel.updateUI();
      }
   }

   private class SkillComponent extends JPanel {
      private final JLabel skillIcon;
      private final JLabel skillName;
      private final JLabel prestigeRank;
      private final JLabel nextPrestigeLevelLabel;
      private final JPanel textPanel;

      SkillComponent(Skill skill) {
         this.skillIcon = new JLabel(new ImageIcon(AugustPrestigeInfoPanel.this.iconManager.getSkillImage(skill)));
         this.skillIcon.setPreferredSize(new Dimension(35, 35));
         this.skillName = new JLabel(skill.getName());
         this.skillName.setForeground(ColorScheme.BRAND_ORANGE);
         this.skillName.setAlignmentX(0.0F);
         this.skillName.setFont(FontManager.getRunescapeBoldFont());
         this.prestigeRank = new JLabel();
         this.prestigeRank.setFont(FontManager.getRunescapeSmallFont());
         this.prestigeRank.setAlignmentX(0.0F);
         this.nextPrestigeLevelLabel = new JLabel();
         this.nextPrestigeLevelLabel.setFont(FontManager.getRunescapeSmallFont());
         this.nextPrestigeLevelLabel.setAlignmentX(0.0F);
         this.textPanel = new JPanel();
         this.textPanel.setLayout(new BoxLayout(this.textPanel, 1));
         this.textPanel.setBackground(ColorScheme.DARKER_GRAY_COLOR);
         this.textPanel.add(this.skillName);
         this.textPanel.add(Box.createVerticalStrut(2));
         this.textPanel.add(this.prestigeRank);
         this.textPanel.add(Box.createVerticalStrut(2));
         this.textPanel.add(this.nextPrestigeLevelLabel);
         this.setLayout(new BorderLayout());
         this.setBorder(new EmptyBorder(5, 5, 5, 5));
         this.setBackground(ColorScheme.DARKER_GRAY_COLOR);
         this.add(this.skillIcon, "West");
         this.add(this.textPanel, "Center");
      }

      public void update(int rank, int nextRankAt, PrestigeState state) {
         this.prestigeRank.setText("Prestige Rank: " + rank);
         if (state == PrestigeState.Maxed) {
            this.nextPrestigeLevelLabel.setText("Max Prestige");
            this.nextPrestigeLevelLabel.setForeground(ColorScheme.PROGRESS_COMPLETE_COLOR);
         } else if (state == PrestigeState.CanRankUp) {
            this.nextPrestigeLevelLabel.setText("Ready To Prestige");
            this.nextPrestigeLevelLabel.setForeground(ColorScheme.PROGRESS_INPROGRESS_COLOR);
         } else {
            this.nextPrestigeLevelLabel.setForeground(ColorScheme.TEXT_COLOR);
            this.nextPrestigeLevelLabel.setText(String.format("Next Prestige: Level %d", nextRankAt));
         }

      }
   }
}
