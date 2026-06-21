package net.runelite.client.ui.laf;

import com.formdev.flatlaf.ui.FlatToggleButtonUI;
import com.formdev.flatlaf.ui.FlatUIUtils;
import com.formdev.flatlaf.ui.FlatStylingSupport.Styleable;
import java.awt.AlphaComposite;
import java.awt.Composite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;

public class RuneLiteToggleButtonUI extends FlatToggleButtonUI {
   @Styleable
   protected float rolloverIconAlpha = 1.0F;

   public static ComponentUI createUI(JComponent c) {
      return (ComponentUI)(FlatUIUtils.canUseSharedUI(c) ? FlatUIUtils.createSharedUI(RuneLiteToggleButtonUI.class, () -> {
         return new RuneLiteToggleButtonUI(true);
      }) : new RuneLiteToggleButtonUI(false));
   }

   protected RuneLiteToggleButtonUI(boolean shared) {
      super(shared);
   }

   protected void paintIcon(Graphics g, JComponent c, Rectangle iconRect) {
      if (this.rolloverIconAlpha != 1.0F && RuneLiteButtonUI.useRolloverEffect(c)) {
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
}
