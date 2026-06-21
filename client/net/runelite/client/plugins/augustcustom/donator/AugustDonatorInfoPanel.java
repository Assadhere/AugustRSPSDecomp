package net.runelite.client.plugins.augustcustom.donator;

import java.awt.BorderLayout;
import java.awt.Color;
import java.text.NumberFormat;
import java.util.Iterator;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;
import net.runelite.api.Client;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.FontManager;
import net.runelite.client.ui.PluginPanel;

class AugustDonatorInfoPanel extends PluginPanel {
   private final JPanel overallPanel = new JPanel();

   AugustDonatorInfoPanel(AugustDonatorInfoPlugin augustDonatorInfoPlugin, Client client) {
      this.setBorder(new EmptyBorder(6, 6, 6, 6));
      this.setBackground(ColorScheme.DARK_GRAY_COLOR);
      this.setLayout(new BorderLayout());
      JPanel layoutPanel = new JPanel();
      BoxLayout boxLayout = new BoxLayout(layoutPanel, 1);
      layoutPanel.setLayout(boxLayout);
      this.add(layoutPanel, "North");
      this.overallPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
      this.overallPanel.setBackground(ColorScheme.DARKER_GRAY_COLOR);
      this.overallPanel.setLayout(new BoxLayout(this.overallPanel, 1));
      this.overallPanel.setVisible(true);
      JTextArea donatorInfoHeader = new JTextArea("Donator Info");
      donatorInfoHeader.setLineWrap(true);
      donatorInfoHeader.setWrapStyleWord(true);
      donatorInfoHeader.setEditable(false);
      donatorInfoHeader.setOpaque(false);
      donatorInfoHeader.setBorder(BorderFactory.createEmptyBorder());
      this.overallPanel.add(donatorInfoHeader);
      this.overallPanel.add(Box.createVerticalStrut(10));
      layoutPanel.add(this.overallPanel);
   }

   public void setInfo(String donatorRank, int donatorPoints, List<String> donatorPerks) {
      this.overallPanel.removeAll();
      JLabel rankHeader = new JLabel("Donator Rank");
      rankHeader.setForeground(new Color(16750623));
      rankHeader.setFont(rankHeader.getFont().deriveFont(1));
      this.overallPanel.add(rankHeader);
      JLabel rankLabel = new JLabel(donatorRank);
      rankLabel.setAlignmentX(0.0F);
      this.overallPanel.add(rankLabel);
      this.overallPanel.add(Box.createVerticalStrut(10));
      JLabel pointsHeader = new JLabel("Total Donated");
      pointsHeader.setForeground(new Color(16750623));
      pointsHeader.setFont(pointsHeader.getFont().deriveFont(1));
      this.overallPanel.add(pointsHeader);
      JLabel pointsLabel = new JLabel(NumberFormat.getInstance().format((long)donatorPoints));
      pointsLabel.setAlignmentX(0.0F);
      this.overallPanel.add(pointsLabel);
      this.overallPanel.add(Box.createVerticalStrut(10));
      JLabel perksHeader = new JLabel("Perks");
      perksHeader.setForeground(new Color(16750623));
      perksHeader.setFont(perksHeader.getFont().deriveFont(1));
      this.overallPanel.add(perksHeader);
      Iterator var9 = donatorPerks.iterator();

      while(var9.hasNext()) {
         String perk = (String)var9.next();
         JTextArea perkText = new JTextArea("• " + perk);
         perkText.setAlignmentX(0.0F);
         perkText.setLineWrap(true);
         perkText.setOpaque(true);
         perkText.setEditable(false);
         perkText.setBackground(ColorScheme.DARKER_GRAY_COLOR);
         perkText.setFont(FontManager.getRunescapeSmallFont());
         this.overallPanel.add(perkText);
         this.overallPanel.add(Box.createVerticalStrut(5));
      }

      this.overallPanel.revalidate();
      this.overallPanel.repaint();
   }
}
