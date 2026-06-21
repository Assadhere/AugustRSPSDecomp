package net.runelite.client.plugins.timetracking.hunter;

import java.awt.Color;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.swing.JLabel;
import javax.swing.JToggleButton;
import javax.swing.border.Border;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.game.ItemManager;
import net.runelite.client.plugins.timetracking.TabContentPanel;
import net.runelite.client.plugins.timetracking.TimeTrackingConfig;
import net.runelite.client.plugins.timetracking.TimeablePanel;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.DynamicGridLayout;

public class BirdHouseTabPanel extends TabContentPanel {
   private static final Color COMPLETED_COLOR;
   private final ConfigManager configManager;
   private final ItemManager itemManager;
   private final BirdHouseTracker birdHouseTracker;
   private final TimeTrackingConfig config;
   private final List<TimeablePanel<BirdHouseSpace>> spacePanels;

   BirdHouseTabPanel(ConfigManager configManager, ItemManager itemManager, BirdHouseTracker birdHouseTracker, TimeTrackingConfig config) {
      this.configManager = configManager;
      this.itemManager = itemManager;
      this.birdHouseTracker = birdHouseTracker;
      this.config = config;
      this.spacePanels = new ArrayList();
      this.setLayout(new DynamicGridLayout(0, 1, 0, 0));
      this.setBackground(ColorScheme.DARK_GRAY_COLOR);
      boolean first = true;
      BirdHouseSpace[] var6 = BirdHouseSpace.values();
      int var7 = var6.length;

      for(int var8 = 0; var8 < var7; ++var8) {
         BirdHouseSpace space = var6[var8];
         TimeablePanel<BirdHouseSpace> panel = new TimeablePanel(space, space.getName(), BirdHouseTracker.BIRD_HOUSE_DURATION);
         this.spacePanels.add(panel);
         this.add(panel);
         if (first) {
            first = false;
            panel.setBorder((Border)null);
         }

         JToggleButton toggleNotify = panel.getNotifyButton();
         toggleNotify.addActionListener((e) -> {
            if (configManager.getRSProfileKey() != null) {
               configManager.setRSProfileConfiguration("timetracking", "birdHouseNotification", toggleNotify.isSelected());
            }

            this.spacePanels.forEach((p) -> {
               p.getNotifyButton().setSelected(toggleNotify.isSelected());
            });
         });
      }

   }

   public int getUpdateInterval() {
      return 50;
   }

   public void update() {
      long unixNow = Instant.now().getEpochSecond();
      boolean notifications = Boolean.TRUE.equals(this.configManager.getRSProfileConfiguration("timetracking", "birdHouseNotification", Boolean.TYPE));
      Iterator var4 = this.spacePanels.iterator();

      while(var4.hasNext()) {
         TimeablePanel<BirdHouseSpace> panel = (TimeablePanel)var4.next();
         BirdHouseSpace space = (BirdHouseSpace)panel.getTimeable();
         BirdHouseData data = (BirdHouseData)this.birdHouseTracker.getBirdHouseData().get(space);
         int value = -1;
         long startTime = 0L;
         if (data != null) {
            value = data.getVarp();
            startTime = data.getTimestamp();
         }

         BirdHouse birdHouse = BirdHouse.fromVarpValue(value);
         BirdHouseState state = BirdHouseState.fromVarpValue(value);
         if (birdHouse == null) {
            this.itemManager.getImage(314).addTo(panel.getIcon());
            panel.getProgress().setVisible(false);
         } else {
            this.itemManager.getImage(birdHouse.getItemID()).addTo(panel.getIcon());
            panel.getIcon().setToolTipText(birdHouse.getName());
            panel.getProgress().setVisible(true);
         }

         panel.getNotifyButton().setSelected(notifications);
         panel.getProgress().setForeground(state.getColor().darker());
         switch (state) {
            case EMPTY:
               panel.getIcon().setToolTipText("Empty");
               panel.getEstimate().setText("Empty");
               break;
            case BUILT:
               panel.getProgress().setValue(0);
               panel.getEstimate().setText("Built");
               break;
            case SEEDED:
               long remainingTime = startTime + (long)BirdHouseTracker.BIRD_HOUSE_DURATION - unixNow;
               if (remainingTime <= 0L) {
                  panel.getProgress().setValue(BirdHouseTracker.BIRD_HOUSE_DURATION);
                  panel.getProgress().setForeground(COMPLETED_COLOR);
                  panel.getEstimate().setText("Done");
               } else {
                  panel.getProgress().setValue((int)((long)BirdHouseTracker.BIRD_HOUSE_DURATION - remainingTime));
                  JLabel var10000 = panel.getEstimate();
                  String var10001 = getFormattedEstimate(remainingTime, this.config.timeFormatMode());
                  var10000.setText("Done " + var10001);
               }
               break;
            default:
               panel.getIcon().setToolTipText("Unknown state");
               panel.getEstimate().setText("Unknown");
         }
      }

   }

   static {
      COMPLETED_COLOR = ColorScheme.PROGRESS_COMPLETE_COLOR.darker();
   }
}
