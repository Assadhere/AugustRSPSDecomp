package net.runelite.client.plugins.timetracking;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.border.EmptyBorder;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.FontManager;
import net.runelite.client.ui.components.ThinProgressBar;
import net.runelite.client.ui.components.shadowlabel.JShadowedLabel;
import net.runelite.client.util.ImageUtil;
import net.runelite.client.util.SwingUtil;

public class TimeablePanel<T> extends JPanel {
   private static final ImageIcon NOTIFY_ICON = new ImageIcon(ImageUtil.loadImageResource(TimeTrackingPlugin.class, "notify_icon.png"));
   private static final ImageIcon NOTIFY_SELECTED_ICON = new ImageIcon(ImageUtil.loadImageResource(TimeTrackingPlugin.class, "notify_selected_icon.png"));
   private static final Rectangle OVERLAY_ICON_BOUNDS;
   private final T timeable;
   private final JLabel icon = new JLabel();
   private final JLabel overlayIcon = new JLabel();
   private final JLabel farmingContractIcon = new JLabel();
   private final JToggleButton notifyButton = new JToggleButton();
   private final JLabel estimate = new JLabel();
   private final ThinProgressBar progress = new ThinProgressBar();
   private final JLabel text;

   public TimeablePanel(T timeable, String title, int maximumProgressValue) {
      this.timeable = timeable;
      this.setLayout(new BorderLayout());
      this.setBorder(new EmptyBorder(7, 0, 0, 0));
      JPanel topContainer = new JPanel();
      topContainer.setBorder(new EmptyBorder(7, 7, 6, 0));
      topContainer.setLayout(new BorderLayout());
      topContainer.setBackground(ColorScheme.DARKER_GRAY_COLOR);
      this.icon.setMinimumSize(new Dimension(36, 32));
      this.overlayIcon.setMinimumSize(OVERLAY_ICON_BOUNDS.getSize());
      this.farmingContractIcon.setMinimumSize(new Dimension(36, 32));
      JPanel infoPanel = new JPanel();
      infoPanel.setBackground(ColorScheme.DARKER_GRAY_COLOR);
      infoPanel.setLayout(new GridLayout(2, 1));
      infoPanel.setBorder(new EmptyBorder(4, 4, 4, 0));
      this.text = new JShadowedLabel(title);
      this.text.setFont(FontManager.getRunescapeSmallFont());
      this.text.setForeground(Color.WHITE);
      this.estimate.setFont(FontManager.getRunescapeSmallFont());
      this.estimate.setForeground(Color.GRAY);
      infoPanel.add(this.text);
      infoPanel.add(this.estimate);
      this.notifyButton.setPreferredSize(new Dimension(30, 16));
      this.notifyButton.setBorder(new EmptyBorder(0, 0, 0, 10));
      this.notifyButton.setIcon(NOTIFY_ICON);
      this.notifyButton.setSelectedIcon(NOTIFY_SELECTED_ICON);
      SwingUtil.removeButtonDecorations(this.notifyButton);
      SwingUtil.addModalTooltip(this.notifyButton, "Disable notifications", "Enable notifications");
      JPanel notifyPanel = new JPanel();
      notifyPanel.setLayout(new BorderLayout());
      notifyPanel.setBackground(ColorScheme.DARKER_GRAY_COLOR);
      notifyPanel.add(this.notifyButton, "Center");
      JPanel iconPanel = new JPanel();
      iconPanel.setLayout(new BorderLayout());
      iconPanel.setBackground(ColorScheme.DARKER_GRAY_COLOR);
      iconPanel.add(notifyPanel, "East");
      iconPanel.add(this.farmingContractIcon, "West");
      JLayeredPane layeredIconPane = new JLayeredPane();
      layeredIconPane.setPreferredSize(new Dimension(36, 32));
      layeredIconPane.add(this.icon, 0);
      layeredIconPane.add(this.overlayIcon, 1);
      this.icon.setBounds(0, 0, 36, 32);
      this.overlayIcon.setBounds(OVERLAY_ICON_BOUNDS);
      topContainer.add(iconPanel, "East");
      topContainer.add(layeredIconPane, "West");
      topContainer.add(infoPanel, "Center");
      this.progress.setValue(0);
      this.progress.setMaximumValue(maximumProgressValue);
      this.add(topContainer, "North");
      this.add(this.progress, "South");
   }

   public void setOverlayIconImage(BufferedImage overlayImg) {
      if (overlayImg == null) {
         this.overlayIcon.setIcon((Icon)null);
      } else {
         if (OVERLAY_ICON_BOUNDS.width != overlayImg.getWidth() || OVERLAY_ICON_BOUNDS.height != overlayImg.getHeight()) {
            overlayImg = ImageUtil.resizeImage(overlayImg, OVERLAY_ICON_BOUNDS.width, OVERLAY_ICON_BOUNDS.height);
         }

         this.overlayIcon.setIcon(new ImageIcon(overlayImg));
      }
   }

   public T getTimeable() {
      return this.timeable;
   }

   public JLabel getIcon() {
      return this.icon;
   }

   public JLabel getOverlayIcon() {
      return this.overlayIcon;
   }

   public JLabel getFarmingContractIcon() {
      return this.farmingContractIcon;
   }

   public JToggleButton getNotifyButton() {
      return this.notifyButton;
   }

   public JLabel getEstimate() {
      return this.estimate;
   }

   public ThinProgressBar getProgress() {
      return this.progress;
   }

   public JLabel getText() {
      return this.text;
   }

   static {
      int width = 24;
      int height = 21;
      int x = 36 - width;
      int y = 32 - height;
      OVERLAY_ICON_BOUNDS = new Rectangle(x, y, width, height);
   }
}
