package net.runelite.client.plugins.screenmarkers.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.Iterator;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import net.runelite.client.plugins.screenmarkers.ScreenMarkerOverlay;
import net.runelite.client.plugins.screenmarkers.ScreenMarkerPlugin;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.PluginPanel;
import net.runelite.client.ui.components.PluginErrorPanel;
import net.runelite.client.util.ImageUtil;

public class ScreenMarkerPluginPanel extends PluginPanel {
   private static final ImageIcon ADD_ICON;
   private static final ImageIcon ADD_HOVER_ICON;
   private static final Color DEFAULT_BORDER_COLOR;
   private static final Color DEFAULT_FILL_COLOR;
   private static final int DEFAULT_BORDER_THICKNESS = 3;
   public static final Color SELECTED_COLOR;
   public static final Color SELECTED_FILL_COLOR;
   public static final int SELECTED_BORDER_THICKNESS = 3;
   private final JLabel addMarker;
   private final JLabel title;
   private final PluginErrorPanel noMarkersPanel;
   private final JPanel markerView;
   private final ScreenMarkerPlugin plugin;
   private final ScreenMarkerCreationPanel creationPanel;

   public ScreenMarkerPluginPanel(ScreenMarkerPlugin screenMarkerPlugin) {
      this.addMarker = new JLabel(ADD_ICON);
      this.title = new JLabel();
      this.noMarkersPanel = new PluginErrorPanel();
      this.markerView = new JPanel(new GridBagLayout());
      this.plugin = screenMarkerPlugin;
      this.setLayout(new BorderLayout());
      this.setBorder(new EmptyBorder(10, 10, 10, 10));
      JPanel northPanel = new JPanel(new BorderLayout());
      northPanel.setBorder(new EmptyBorder(1, 0, 10, 0));
      this.title.setText("Screen Markers");
      this.title.setForeground(Color.WHITE);
      northPanel.add(this.title, "West");
      northPanel.add(this.addMarker, "East");
      JPanel centerPanel = new JPanel(new BorderLayout());
      centerPanel.setBackground(ColorScheme.DARK_GRAY_COLOR);
      this.markerView.setBackground(ColorScheme.DARK_GRAY_COLOR);
      GridBagConstraints constraints = new GridBagConstraints();
      constraints.fill = 2;
      constraints.weightx = 1.0;
      constraints.gridx = 0;
      constraints.gridy = 0;
      this.noMarkersPanel.setContent("Screen Markers", "Highlight a region on your screen.");
      this.noMarkersPanel.setVisible(false);
      this.markerView.add(this.noMarkersPanel, constraints);
      ++constraints.gridy;
      this.creationPanel = new ScreenMarkerCreationPanel(this.plugin);
      this.creationPanel.setVisible(false);
      this.markerView.add(this.creationPanel, constraints);
      ++constraints.gridy;
      this.addMarker.setToolTipText("Add new screen marker");
      this.addMarker.addMouseListener(new MouseAdapter() {
         public void mousePressed(MouseEvent mouseEvent) {
            ScreenMarkerPluginPanel.this.setCreation(true);
         }

         public void mouseEntered(MouseEvent mouseEvent) {
            ScreenMarkerPluginPanel.this.addMarker.setIcon(ScreenMarkerPluginPanel.ADD_HOVER_ICON);
         }

         public void mouseExited(MouseEvent mouseEvent) {
            ScreenMarkerPluginPanel.this.addMarker.setIcon(ScreenMarkerPluginPanel.ADD_ICON);
         }
      });
      centerPanel.add(this.markerView, "Center");
      this.add(northPanel, "North");
      this.add(centerPanel, "Center");
   }

   public void rebuild() {
      GridBagConstraints constraints = new GridBagConstraints();
      constraints.fill = 2;
      constraints.weightx = 1.0;
      constraints.gridx = 0;
      constraints.gridy = 0;
      this.markerView.removeAll();

      for(Iterator var2 = this.plugin.getScreenMarkers().iterator(); var2.hasNext(); ++constraints.gridy) {
         ScreenMarkerOverlay marker = (ScreenMarkerOverlay)var2.next();
         this.markerView.add(new ScreenMarkerPanel(this.plugin, marker), constraints);
         ++constraints.gridy;
         this.markerView.add(Box.createRigidArea(new Dimension(0, 10)), constraints);
      }

      boolean empty = constraints.gridy == 0;
      this.noMarkersPanel.setVisible(empty);
      this.title.setVisible(!empty);
      this.markerView.add(this.noMarkersPanel, constraints);
      ++constraints.gridy;
      this.markerView.add(this.creationPanel, constraints);
      ++constraints.gridy;
      this.repaint();
      this.revalidate();
   }

   public void setCreation(boolean on) {
      if (on) {
         this.noMarkersPanel.setVisible(false);
         this.title.setVisible(true);
      } else {
         boolean empty = this.plugin.getScreenMarkers().isEmpty();
         this.noMarkersPanel.setVisible(empty);
         this.title.setVisible(!empty);
      }

      this.creationPanel.setVisible(on);
      this.addMarker.setVisible(!on);
      if (on) {
         this.creationPanel.lockConfirm();
         this.plugin.setMouseListenerEnabled(true);
         this.plugin.setCreatingScreenMarker(true);
      }

   }

   public ScreenMarkerCreationPanel getCreationPanel() {
      return this.creationPanel;
   }

   static {
      DEFAULT_BORDER_COLOR = Color.GREEN;
      DEFAULT_FILL_COLOR = new Color(0, 255, 0, 0);
      SELECTED_COLOR = DEFAULT_BORDER_COLOR;
      SELECTED_FILL_COLOR = DEFAULT_FILL_COLOR;
      BufferedImage addIcon = ImageUtil.loadImageResource(ScreenMarkerPlugin.class, "add_icon.png");
      ADD_ICON = new ImageIcon(addIcon);
      ADD_HOVER_ICON = new ImageIcon(ImageUtil.alphaOffset(addIcon, 0.53F));
   }
}
