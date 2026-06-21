package net.runelite.client.plugins.timetracking.farming;

import com.google.inject.Inject;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.swing.JTextArea;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.plugins.timetracking.TabContentPanel;
import net.runelite.client.plugins.timetracking.TimeTrackingConfig;
import net.runelite.client.plugins.timetracking.TimeablePanel;
import net.runelite.client.ui.ColorScheme;

public class FarmingNextTickPanel extends TabContentPanel {
   private final FarmingTracker farmingTracker;
   private final TimeTrackingConfig config;
   private final ConfigManager configManager;
   private final List<TimeablePanel<Void>> patchPanels;
   private final JTextArea infoTextArea;

   @Inject
   public FarmingNextTickPanel(FarmingTracker farmingTracker, TimeTrackingConfig config, ConfigManager configManager) {
      this.farmingTracker = farmingTracker;
      this.config = config;
      this.configManager = configManager;
      this.patchPanels = new ArrayList();
      this.setLayout(new GridBagLayout());
      this.setBackground(ColorScheme.DARK_GRAY_COLOR);
      GridBagConstraints c = new GridBagConstraints();
      c.fill = 2;
      c.weightx = 1.0;
      c.gridx = 0;
      c.gridy = 0;
      int[] times = new int[]{5, 10, 20, 40, 80, 160, 320, 640};
      int[] var6 = times;
      int var7 = times.length;

      for(int var8 = 0; var8 < var7; ++var8) {
         int time = var6[var8];
         TimeablePanel<Void> panel = new TimeablePanel((Object)null, "" + time + " minute tick", time);
         this.patchPanels.add(panel);
         this.add(panel, c);
         ++c.gridy;
      }

      this.infoTextArea = new JTextArea();
      this.add(this.infoTextArea, c);
      ++c.gridy;
   }

   public int getUpdateInterval() {
      return 50;
   }

   public void update() {
      long unixNow = Instant.now().getEpochSecond();
      Iterator var3 = this.patchPanels.iterator();

      while(var3.hasNext()) {
         TimeablePanel<Void> panel = (TimeablePanel)var3.next();
         int tickLength = panel.getProgress().getMaximumValue();
         long nextTick = this.farmingTracker.getTickTime(tickLength, 1);
         panel.getEstimate().setText(getFormattedEstimate(nextTick - unixNow, this.config.timeFormatMode()));
      }

      String offsetPrecisionMins = this.configManager.getRSProfileConfiguration("timetracking", "farmTickOffsetPrecision");
      String offsetTimeMins = this.configManager.getRSProfileConfiguration("timetracking", "farmTickOffset");
      this.infoTextArea.setText("Offset precision:" + offsetPrecisionMins + "\nFarming tick offset: -" + offsetTimeMins);
   }
}
