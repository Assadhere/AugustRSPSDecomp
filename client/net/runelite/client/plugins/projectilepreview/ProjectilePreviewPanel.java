package net.runelite.client.plugins.projectilepreview;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.PluginPanel;

class ProjectilePreviewPanel extends PluginPanel {
   private static final int PANEL_W = 205;
   private final JSpinner spotanimSpinner = new JSpinner(new SpinnerNumberModel(245, 0, 65535, 1));
   private final JSlider srcHeightSlider = new JSlider(0, 400, 60);
   private final JSpinner srcHeightSpinner = new JSpinner(new SpinnerNumberModel(60, 0, 400, 1));
   private final JSlider tgtHeightSlider = new JSlider(0, 400, 0);
   private final JSpinner tgtHeightSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 400, 1));
   private final JSlider startDelaySlider = new JSlider(0, 100, 11);
   private final JSpinner startDelaySpinner = new JSpinner(new SpinnerNumberModel(11, 0, 100, 1));
   private final JSlider durationSlider = new JSlider(1, 300, 60);
   private final JSpinner durationSpinner = new JSpinner(new SpinnerNumberModel(60, 1, 300, 1));
   private final JSlider slopeSlider = new JSlider(-50, 100, 16);
   private final JSpinner slopeSpinner = new JSpinner(new SpinnerNumberModel(16, -50, 100, 1));
   private final JSlider startPosSlider = new JSlider(0, 128, 64);
   private final JSpinner startPosSpinner = new JSpinner(new SpinnerNumberModel(64, 0, 128, 1));
   private final JSlider loopSlider = new JSlider(0, 100, 0);
   private final JSpinner loopSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 100, 1));
   private final JLabel targetLabel = new JLabel("No target");
   private final JComboBox<Object> presetBox = new JComboBox();
   private final JButton fireBtn = new JButton("Fire once");
   private final JButton clearTgtBtn = new JButton("Clear target");
   private boolean syncing;

   ProjectilePreviewPanel() {
      this.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
      this.setLayout(new BoxLayout(this, 1));
      this.setBackground(ColorScheme.DARK_GRAY_COLOR);
      this.add(this.buildHeader());
      this.add(this.buildTargetRow());
      this.add(this.buildPresetRow());
      this.add(this.buildSpotanimRow());
      this.add(this.buildSliderRow("Source height offset", this.srcHeightSlider, this.srcHeightSpinner));
      this.add(this.buildSliderRow("Target height offset", this.tgtHeightSlider, this.tgtHeightSpinner));
      this.add(this.buildSliderRow("Start delay (cycles)", this.startDelaySlider, this.startDelaySpinner));
      this.add(this.buildSliderRow("Duration (cycles)", this.durationSlider, this.durationSpinner));
      this.add(this.buildSliderRow("Slope (arc)", this.slopeSlider, this.slopeSpinner));
      this.add(this.buildSliderRow("Start pos offset", this.startPosSlider, this.startPosSpinner));
      this.add(this.buildSliderRow("Auto loop (ticks, 0=off)", this.loopSlider, this.loopSpinner));
      this.add(this.buildButtonRow());
      this.add(this.buildHelp());
   }

   private JPanel buildHeader() {
      JPanel p = new JPanel(new BorderLayout());
      p.setBackground(ColorScheme.DARK_GRAY_COLOR);
      p.setAlignmentX(0.0F);
      p.setMaximumSize(new Dimension(205, 22));
      JLabel l = new JLabel("Projectile Preview");
      l.setForeground(ColorScheme.BRAND_ORANGE);
      p.add(l, "West");
      return p;
   }

   private JPanel buildTargetRow() {
      JPanel p = new JPanel(new BorderLayout());
      p.setBackground(ColorScheme.DARK_GRAY_COLOR);
      p.setAlignmentX(0.0F);
      p.setBorder(BorderFactory.createEmptyBorder(4, 0, 4, 0));
      p.setMaximumSize(new Dimension(205, 22));
      this.targetLabel.setForeground(ColorScheme.LIGHT_GRAY_COLOR);
      p.add(new JLabel("Target: "), "West");
      p.add(this.targetLabel, "Center");
      return p;
   }

   private JPanel buildPresetRow() {
      JPanel p = new JPanel(new BorderLayout(4, 0));
      p.setBackground(ColorScheme.DARK_GRAY_COLOR);
      p.setAlignmentX(0.0F);
      p.setBorder(BorderFactory.createEmptyBorder(2, 0, 2, 0));
      p.setMaximumSize(new Dimension(205, 26));
      JLabel l = new JLabel("Preset");
      l.setForeground(ColorScheme.LIGHT_GRAY_COLOR);
      this.presetBox.addItem("Select preset…");
      Preset[] var3 = Preset.values();
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         Preset preset = var3[var5];
         this.presetBox.addItem(preset);
      }

      this.presetBox.setBackground(ColorScheme.DARKER_GRAY_COLOR);
      this.presetBox.setForeground(Color.WHITE);
      this.presetBox.setRenderer(new DefaultListCellRenderer() {
         public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            if (value instanceof Preset) {
               this.setText(((Preset)value).name());
            }

            return this;
         }
      });
      this.presetBox.addActionListener((e) -> {
         Object sel = this.presetBox.getSelectedItem();
         if (sel instanceof Preset) {
            this.applyPreset((Preset)sel);
         }

      });
      p.add(l, "West");
      p.add(this.presetBox, "Center");
      return p;
   }

   private void applyPreset(Preset preset) {
      this.syncing = true;
      setBoth(this.srcHeightSlider, this.srcHeightSpinner, preset.startHeight);
      setBoth(this.tgtHeightSlider, this.tgtHeightSpinner, preset.endHeight);
      setBoth(this.startDelaySlider, this.startDelaySpinner, preset.delay);
      setBoth(this.slopeSlider, this.slopeSpinner, preset.angle);
      setBoth(this.startPosSlider, this.startPosSpinner, preset.progress);
      this.syncing = false;
   }

   private static void setBoth(JSlider slider, JSpinner spinner, int raw) {
      int v = Math.max(slider.getMinimum(), Math.min(slider.getMaximum(), raw));
      slider.setValue(v);
      spinner.setValue(v);
   }

   private JPanel buildSpotanimRow() {
      JPanel p = new JPanel(new BorderLayout());
      p.setBackground(ColorScheme.DARK_GRAY_COLOR);
      p.setAlignmentX(0.0F);
      p.setBorder(BorderFactory.createEmptyBorder(2, 0, 2, 0));
      p.setMaximumSize(new Dimension(205, 26));
      JLabel l = new JLabel("Spotanim ID");
      l.setForeground(ColorScheme.LIGHT_GRAY_COLOR);
      this.spotanimSpinner.setPreferredSize(new Dimension(80, 22));
      this.spotanimSpinner.setBackground(ColorScheme.DARKER_GRAY_COLOR);
      JSpinner.NumberEditor ed = new JSpinner.NumberEditor(this.spotanimSpinner, "#");
      ed.getTextField().setBackground(ColorScheme.DARKER_GRAY_COLOR);
      ed.getTextField().setForeground(Color.WHITE);
      ed.getTextField().setCaretColor(Color.WHITE);
      this.spotanimSpinner.setEditor(ed);
      p.add(l, "West");
      p.add(this.spotanimSpinner, "East");
      return p;
   }

   private JPanel buildSliderRow(String name, JSlider slider, JSpinner spinner) {
      JPanel row = new JPanel();
      row.setLayout(new BoxLayout(row, 1));
      row.setBackground(ColorScheme.DARK_GRAY_COLOR);
      row.setAlignmentX(0.0F);
      row.setBorder(BorderFactory.createEmptyBorder(2, 0, 2, 0));
      JPanel header = new JPanel(new BorderLayout());
      header.setBackground(ColorScheme.DARK_GRAY_COLOR);
      header.setAlignmentX(0.0F);
      header.setMaximumSize(new Dimension(205, 22));
      JLabel lbl = new JLabel(name);
      lbl.setForeground(ColorScheme.LIGHT_GRAY_COLOR);
      spinner.setPreferredSize(new Dimension(70, 20));
      spinner.setBackground(ColorScheme.DARKER_GRAY_COLOR);
      JSpinner.NumberEditor ed = new JSpinner.NumberEditor(spinner, "#");
      ed.getTextField().setBackground(ColorScheme.DARKER_GRAY_COLOR);
      ed.getTextField().setForeground(Color.WHITE);
      ed.getTextField().setCaretColor(Color.WHITE);
      spinner.setEditor(ed);
      header.add(lbl, "West");
      header.add(spinner, "East");
      slider.setBackground(ColorScheme.DARK_GRAY_COLOR);
      slider.setForeground(ColorScheme.BRAND_ORANGE);
      slider.setAlignmentX(0.0F);
      slider.setMaximumSize(new Dimension(205, 18));
      slider.addChangeListener((e) -> {
         if (!this.syncing) {
            this.syncing = true;
            spinner.setValue(slider.getValue());
            this.syncing = false;
         }
      });
      spinner.addChangeListener((e) -> {
         if (!this.syncing) {
            this.syncing = true;
            int v = ((Number)spinner.getValue()).intValue();
            v = Math.max(slider.getMinimum(), Math.min(slider.getMaximum(), v));
            slider.setValue(v);
            this.syncing = false;
         }
      });
      row.add(header);
      row.add(slider);
      return row;
   }

   private JPanel buildButtonRow() {
      JPanel p = new JPanel(new GridLayout(1, 2, 4, 4));
      p.setBackground(ColorScheme.DARK_GRAY_COLOR);
      p.setAlignmentX(0.0F);
      p.setBorder(BorderFactory.createEmptyBorder(8, 0, 4, 0));
      p.setMaximumSize(new Dimension(205, 28));
      p.add(this.fireBtn);
      p.add(this.clearTgtBtn);
      return p;
   }

   private JPanel buildHelp() {
      JPanel p = new JPanel();
      p.setLayout(new BoxLayout(p, 1));
      p.setBackground(ColorScheme.DARK_GRAY_COLOR);
      p.setAlignmentX(0.0F);
      p.setBorder(BorderFactory.createEmptyBorder(8, 0, 0, 0));
      JLabel l1 = new JLabel("Right-click an NPC or player and");
      JLabel l2 = new JLabel("pick \"Set projectile target\".");
      JLabel l3 = new JLabel("Source = local player.");
      JLabel[] var5 = new JLabel[]{l1, l2, l3};
      int var6 = var5.length;

      for(int var7 = 0; var7 < var6; ++var7) {
         JLabel l = var5[var7];
         l.setForeground(ColorScheme.LIGHT_GRAY_COLOR);
         l.setAlignmentX(0.0F);
         p.add(l);
      }

      return p;
   }

   void onFire(Runnable r) {
      this.fireBtn.addActionListener((e) -> {
         r.run();
      });
   }

   void onClearTarget(Runnable r) {
      this.clearTgtBtn.addActionListener((e) -> {
         r.run();
      });
   }

   void setTargetText(String s) {
      SwingUtilities.invokeLater(() -> {
         this.targetLabel.setText(s == null ? "No target" : s);
      });
   }

   ProjectilePreviewPlugin.Snapshot snapshot() {
      return new ProjectilePreviewPlugin.Snapshot(((Number)this.spotanimSpinner.getValue()).intValue(), this.srcHeightSlider.getValue(), this.tgtHeightSlider.getValue(), this.startDelaySlider.getValue(), this.durationSlider.getValue(), this.slopeSlider.getValue(), this.startPosSlider.getValue(), this.loopSlider.getValue());
   }
}
