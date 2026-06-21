package net.runelite.client.ui.laf;

import com.formdev.flatlaf.ui.FlatButtonUI;
import com.formdev.flatlaf.ui.FlatUIUtils;
import com.formdev.flatlaf.ui.FlatStylingSupport.Styleable;
import java.awt.AlphaComposite;
import java.awt.Composite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import javax.swing.AbstractButton;
import javax.swing.ButtonModel;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;

public class RuneLiteButtonUI extends FlatButtonUI {
   @Styleable
   protected float rolloverIconAlpha = 1.0F;

   public static ComponentUI createUI(JComponent c) {
      return (ComponentUI)(FlatUIUtils.canUseSharedUI(c) ? FlatUIUtils.createSharedUI(RuneLiteButtonUI.class, () -> {
         return new RuneLiteButtonUI(true);
      }) : new RuneLiteButtonUI(false));
   }

   protected RuneLiteButtonUI(boolean shared) {
      super(shared);
   }

   protected void paintIcon(Graphics g, JComponent c, Rectangle iconRect) {
      if (this.rolloverIconAlpha != 1.0F && useRolloverEffect(c)) {
         Graphics2D g2d = (Graphics2D)g;
         Composite composite = g2d.getComposite();

         try {
            g2d.setComposite(AlphaComposite.getInstance(3, this.rolloverIconAlpha));
            super.paintIcon(g, c, iconRect);
         } finally {
            g2d.setComposite(composite);
         }

      } else {
         super.paintIcon(g, c, iconRect);
      }
   }

   static boolean useRolloverEffect(JComponent c) {
      AbstractButton btn = (AbstractButton)c;
      ButtonModel model = btn.getModel();
      if (btn.isRolloverEnabled() && model.isRollover()) {
         Icon icon = model.isSelected() ? btn.getRolloverSelectedIcon() : null;
         if (icon == null) {
            icon = btn.getRolloverIcon();
         }

         return icon == null;
      } else {
         return false;
      }
   }
}
