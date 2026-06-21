package net.runelite.client.ui.laf;

import com.formdev.flatlaf.ui.FlatCheckBoxUI;
import com.formdev.flatlaf.ui.FlatUIUtils;
import com.formdev.flatlaf.ui.FlatStylingSupport.Styleable;
import java.awt.AlphaComposite;
import java.awt.Composite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import javax.swing.AbstractButton;
import javax.swing.JComponent;
import javax.swing.LookAndFeel;
import javax.swing.plaf.ComponentUI;

public class RuneLiteCheckBoxUI extends FlatCheckBoxUI {
   @Styleable
   protected float rolloverIconAlpha = 1.0F;

   public static ComponentUI createUI(JComponent c) {
      return (ComponentUI)(FlatUIUtils.canUseSharedUI(c) ? FlatUIUtils.createSharedUI(RuneLiteCheckBoxUI.class, () -> {
         return new RuneLiteCheckBoxUI(true);
      }) : new RuneLiteCheckBoxUI(false));
   }

   protected RuneLiteCheckBoxUI(boolean shared) {
      super(shared);
   }

   public void installDefaults(AbstractButton b) {
      super.installDefaults(b);
      LookAndFeel.installProperty(b, "contentAreaFilled", false);
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
