package net.runelite.client.ui.components.colorpicker;

import com.google.common.collect.EvictingQueue;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Iterator;
import java.util.function.Consumer;
import javax.swing.JPanel;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.util.ColorUtil;
import net.runelite.client.util.Text;

final class RecentColors {
   private static final String CONFIG_KEY = "recentColors";
   private static final int MAX = 16;
   private static final int BOX_SIZE = 16;
   private final EvictingQueue<String> recentColors = EvictingQueue.create(16);
   private final ConfigManager configManager;

   RecentColors(ConfigManager configManager) {
      this.configManager = configManager;
   }

   private void load() {
      String str = this.configManager.getConfiguration("colorpicker", "recentColors");
      if (str != null) {
         this.recentColors.addAll(Text.fromCSV(str));
      }

   }

   void add(String color) {
      if (ColorUtil.fromString(color) != null) {
         this.recentColors.remove(color);
         this.recentColors.add(color);
         this.configManager.setConfiguration("colorpicker", "recentColors", Text.toCSV(this.recentColors));
      }
   }

   JPanel build(Consumer<Color> consumer, boolean alphaHidden) {
      this.load();
      JPanel container = new JPanel(new GridBagLayout());
      GridBagConstraints cx = new GridBagConstraints();
      cx.insets = new Insets(0, 1, 4, 2);
      cx.gridy = 0;
      cx.gridx = 0;
      cx.anchor = 17;

      for(Iterator var5 = this.recentColors.iterator(); var5.hasNext(); ++cx.gridx) {
         String s = (String)var5.next();
         if (cx.gridx == 8) {
            ++cx.gridy;
            cx.gridx = 0;
         }

         if (container.getComponentCount() == this.recentColors.size() - 1) {
            cx.weightx = 1.0;
            cx.gridwidth = 8 - cx.gridx;
         }

         container.add(createBox(ColorUtil.fromString(s), consumer, alphaHidden), cx);
      }

      return container;
   }

   private static JPanel createBox(final Color color, final Consumer<Color> consumer, boolean alphaHidden) {
      JPanel box = new JPanel();
      String hex = alphaHidden ? ColorUtil.colorToHexCode(color) : ColorUtil.colorToAlphaHexCode(color);
      box.setBackground(color);
      box.setOpaque(true);
      box.setPreferredSize(new Dimension(16, 16));
      box.setToolTipText("#" + hex.toUpperCase());
      box.addMouseListener(new MouseAdapter() {
         public void mouseClicked(MouseEvent e) {
            consumer.accept(color);
         }
      });
      return box;
   }
}
