package net.runelite.client.plugins.timetracking.farming;

import com.google.common.base.Strings;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.image.BufferedImage;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JToggleButton;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.game.ItemManager;
import net.runelite.client.plugins.timetracking.TabContentPanel;
import net.runelite.client.plugins.timetracking.TimeTrackingConfig;
import net.runelite.client.plugins.timetracking.TimeablePanel;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.FontManager;
import net.runelite.client.util.AsyncBufferedImage;

public class FarmingTabPanel extends TabContentPanel {
   private final FarmingTracker farmingTracker;
   private final CompostTracker compostTracker;
   private final PaymentTracker paymentTracker;
   private final ItemManager itemManager;
   private final ConfigManager configManager;
   private final TimeTrackingConfig config;
   private final List<TimeablePanel<FarmingPatch>> patchPanels;
   private final FarmingContractManager farmingContractManager;

   FarmingTabPanel(FarmingTracker farmingTracker, CompostTracker compostTracker, PaymentTracker paymentTracker, ItemManager itemManager, ConfigManager configManager, TimeTrackingConfig config, Set<FarmingPatch> patches, FarmingContractManager farmingContractManager) {
      this.farmingTracker = farmingTracker;
      this.compostTracker = compostTracker;
      this.paymentTracker = paymentTracker;
      this.itemManager = itemManager;
      this.configManager = configManager;
      this.config = config;
      this.patchPanels = new ArrayList();
      this.farmingContractManager = farmingContractManager;
      this.setLayout(new GridBagLayout());
      this.setBackground(ColorScheme.DARK_GRAY_COLOR);
      GridBagConstraints c = new GridBagConstraints();
      c.fill = 2;
      c.weightx = 1.0;
      c.gridx = 0;
      c.gridy = 0;
      PatchImplementation lastImpl = null;
      boolean first = true;
      Iterator var12 = patches.iterator();

      while(var12.hasNext()) {
         FarmingPatch patch = (FarmingPatch)var12.next();
         String var10000 = patch.getRegion().getName();
         String title = var10000 + (Strings.isNullOrEmpty(patch.getName()) ? "" : " (" + patch.getName() + ")");
         TimeablePanel<FarmingPatch> p = new TimeablePanel(patch, title, 1);
         if (patch.getImplementation() != lastImpl && !Strings.isNullOrEmpty(patch.getImplementation().getName())) {
            JLabel groupLabel = new JLabel(patch.getImplementation().getName());
            if (first) {
               first = false;
               groupLabel.setBorder(new EmptyBorder(4, 0, 0, 0));
            } else {
               groupLabel.setBorder(new EmptyBorder(15, 0, 0, 0));
            }

            groupLabel.setFont(FontManager.getRunescapeSmallFont());
            this.add(groupLabel, c);
            ++c.gridy;
            lastImpl = patch.getImplementation();
         }

         JToggleButton toggleNotify = p.getNotifyButton();
         String configKey = patch.notifyConfigKey();
         toggleNotify.addActionListener((e) -> {
            if (configManager.getRSProfileKey() != null) {
               configManager.setRSProfileConfiguration("timetracking", configKey, toggleNotify.isSelected());
            }

         });
         this.patchPanels.add(p);
         this.add(p, c);
         ++c.gridy;
         if (first) {
            first = false;
            p.setBorder((Border)null);
         }
      }

   }

   public int getUpdateInterval() {
      return 50;
   }

   public void update() {
      long unixNow = Instant.now().getEpochSecond();
      Iterator var3 = this.patchPanels.iterator();

      while(var3.hasNext()) {
         TimeablePanel<FarmingPatch> panel = (TimeablePanel)var3.next();
         FarmingPatch patch = (FarmingPatch)panel.getTimeable();
         PatchPrediction prediction = this.farmingTracker.predictPatch(patch);
         boolean protected_ = this.paymentTracker.getProtectedState(patch);
         CompostState compostState = this.compostTracker.getCompostState(patch);
         AsyncBufferedImage img = this.getPatchImage(compostState, protected_);
         String tooltip = this.getPatchTooltip(compostState, protected_);
         if (img != null) {
            img.onLoaded(() -> {
               panel.setOverlayIconImage(img);
            });
         } else {
            panel.setOverlayIconImage((BufferedImage)null);
         }

         if (prediction == null) {
            this.itemManager.getImage(Produce.WEEDS.getItemID()).addTo(panel.getIcon());
            panel.getIcon().setToolTipText("Unknown state" + tooltip);
            panel.getProgress().setMaximumValue(0);
            panel.getProgress().setValue(0);
            panel.getProgress().setVisible(false);
            panel.getEstimate().setText("Unknown");
            panel.getProgress().setBackground((Color)null);
         } else {
            if (prediction.getProduce().getItemID() < 0) {
               panel.getIcon().setIcon((Icon)null);
               panel.getIcon().setToolTipText("Unknown state" + tooltip);
            } else {
               this.itemManager.getImage(prediction.getProduce().getItemID()).addTo(panel.getIcon());
               JLabel var10000 = panel.getIcon();
               String var10001 = prediction.getProduce().getName();
               var10000.setToolTipText(var10001 + tooltip);
            }

            switch (prediction.getCropState()) {
               case HARVESTABLE:
                  panel.getEstimate().setText("Done");
                  break;
               case GROWING:
                  if (prediction.getDoneEstimate() < unixNow) {
                     panel.getEstimate().setText("Done");
                  } else {
                     panel.getEstimate().setText("Done " + getFormattedEstimate(prediction.getDoneEstimate() - unixNow, this.config.timeFormatMode()));
                  }
                  break;
               case DISEASED:
                  panel.getEstimate().setText("Diseased");
                  break;
               case DEAD:
                  panel.getEstimate().setText("Dead");
                  break;
               case EMPTY:
                  panel.getEstimate().setText("Empty");
                  break;
               case FILLING:
                  panel.getEstimate().setText("Filling");
            }

            if (prediction.getProduce() == Produce.WEEDS && prediction.getStage() >= prediction.getStages() - 1) {
               panel.getProgress().setVisible(false);
            } else {
               panel.getProgress().setVisible(true);
               panel.getProgress().setForeground(prediction.getCropState().getColor().darker());
               panel.getProgress().setMaximumValue(prediction.getStages() - 1);
               panel.getProgress().setValue(prediction.getStage());
            }
         }

         JLabel farmingContractIcon = panel.getFarmingContractIcon();
         if (this.farmingContractManager.shouldHighlightFarmingTabPanel(patch)) {
            this.itemManager.getImage(22993).addTo(farmingContractIcon);
            farmingContractIcon.setToolTipText(this.farmingContractManager.getContract().getName());
         } else {
            farmingContractIcon.setIcon((Icon)null);
            farmingContractIcon.setToolTipText("");
         }

         String configKey = patch.notifyConfigKey();
         JToggleButton toggleNotify = panel.getNotifyButton();
         boolean notifyEnabled = Boolean.TRUE.equals(this.configManager.getRSProfileConfiguration("timetracking", configKey, Boolean.class));
         toggleNotify.setSelected(notifyEnabled);
      }

   }

   private AsyncBufferedImage getPatchImage(CompostState compostState, boolean protected_) {
      return protected_ ? this.itemManager.getImage(5386) : (compostState != null ? this.itemManager.getImage(compostState.getItemId()) : null);
   }

   private String getPatchTooltip(CompostState compostState, boolean protected_) {
      StringBuilder stringBuilder = new StringBuilder();
      if (protected_) {
         stringBuilder.append(" protected");
         if (compostState != null) {
            stringBuilder.append(" and ").append(compostState.name().toLowerCase()).append("ed");
         }
      } else if (compostState != null) {
         stringBuilder.append(" with ").append(compostState.name().toLowerCase());
      }

      return stringBuilder.toString();
   }
}
