package net.runelite.client.ui;

import com.formdev.flatlaf.ui.FlatNativeWindowsLibrary;
import java.awt.Dimension;
import java.awt.IllegalComponentStateException;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import javax.swing.JFrame;
import net.runelite.client.util.OSType;
import net.runelite.client.util.WinUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ContainableFrame extends JFrame {
   private static final Logger log = LoggerFactory.getLogger(ContainableFrame.class);
   private static final int SCREEN_EDGE_CLOSE_DISTANCE = 40;
   private Mode containedInScreen;
   private boolean rightSideSuction;
   private boolean scaleMinSize = false;
   private boolean overrideUndecorated;

   private void applyChange(int wX, int wY, int wWidth, int wHeight, int wOldx, int wOldY, int wOldWidth, boolean contain) {
      boolean isSnapped = WinUtil.isWindowArranged(this);
      if ((contain || isSnapped) && !this.isFullScreen()) {
         Rectangle cDpyBounds = this.getGraphicsConfiguration().getBounds();
         Insets insets = this.getInsets();
         Rectangle cRect = new Rectangle(wX + insets.left, wY + insets.top, wWidth - (insets.left + insets.right), wHeight - (insets.top + insets.bottom));
         if (this.rightSideSuction || isSnapped) {
            this.rightSideSuction = (double)(wOldx + wOldWidth - insets.right + 40) >= cDpyBounds.getMaxX();
         }

         if (this.rightSideSuction && wWidth < wOldWidth) {
            cRect.x += wOldWidth - wWidth;
         }

         if (wWidth > wOldWidth && cRect.getMaxX() > cDpyBounds.getMaxX() && (double)(wOldx + insets.left + (wOldWidth - (insets.left + insets.right)) + 40) > cDpyBounds.getMaxX() && (double)(wOldx + insets.left + (wOldWidth - (insets.left + insets.right))) <= cDpyBounds.getMaxX()) {
            cRect.x -= wWidth - wOldWidth;
         }

         cRect.x = (int)((double)cRect.x - Math.max(0.0, cRect.getMaxX() - cDpyBounds.getMaxX()));
         cRect.y = (int)((double)cRect.y - Math.max(0.0, cRect.getMaxY() - cDpyBounds.getMaxY()));
         if (cRect.x != wOldx + insets.left) {
            cRect.x = Math.max(cRect.x, cDpyBounds.x);
         }

         if (cRect.y != wOldY + insets.top) {
            cRect.y = Math.max(cRect.y, cDpyBounds.y);
         }

         if (wWidth > wOldWidth && cRect.x < wOldx + insets.left) {
            this.rightSideSuction = true;
         }

         wX = cRect.x - insets.left;
         wY = cRect.y - insets.top;
         wWidth = cRect.width + insets.left + insets.right;
         wHeight = cRect.height + insets.top + insets.bottom;
      }

      boolean xyDifferent = this.getX() != wX || this.getY() != wY;
      boolean whDifferent = this.getWidth() != wWidth || this.getHeight() != wHeight;
      if (xyDifferent && whDifferent) {
         super.reshape(wX, wY, wWidth, wHeight);
      } else if (xyDifferent) {
         super.move(wX, wY);
      } else if (whDifferent) {
         super.resize(wWidth, wHeight);
      }

   }

   public void containedSetSize(Dimension size, Rectangle oldBounds) {
      this.applyChange(this.getX(), this.getY(), size.width, size.height, oldBounds.x, oldBounds.y, oldBounds.width, this.containedInScreen != ContainableFrame.Mode.NEVER);
   }

   public void revalidateMinimumSize() {
      Dimension minSize = this.getLayout().minimumLayoutSize(this);
      this.setMinimumSize(minSize);
   }

   public void setMinimumSize(Dimension minSize) {
      if (OSType.getOSType() == OSType.Windows) {
         synchronized(this.getTreeLock()) {
            try {
               this.scaleMinSize = true;
               super.setMinimumSize(minSize);
            } finally {
               this.scaleMinSize = false;
            }
         }
      } else {
         super.setMinimumSize(minSize);
      }

   }

   public Dimension getMinimumSize() {
      Dimension minSize = super.getMinimumSize();
      if (OSType.getOSType() == OSType.Windows && minSize != null) {
         synchronized(this.getTreeLock()) {
            if (this.scaleMinSize) {
               AffineTransform transform = this.getGraphicsConfiguration().getDefaultTransform();
               int scaledX = (int)Math.round((double)minSize.width * transform.getScaleX());
               int scaledY = (int)Math.round((double)minSize.height * transform.getScaleY());
               minSize = new Dimension(scaledX, scaledY);
            }
         }
      }

      return minSize;
   }

   private boolean isFullScreen() {
      return (this.getExtendedState() & 6) == 6;
   }

   void updateContainsInScreen() {
      if (FlatNativeWindowsLibrary.isLoaded()) {
         FlatNativeWindowsLibrary.setContainInScreen(this, this.containedInScreen == ContainableFrame.Mode.ALWAYS);
      }

   }

   public void setOpacity(float opacity) {
      this.overrideUndecorated = true;

      try {
         super.setOpacity(opacity);
      } catch (UnsupportedOperationException | IllegalArgumentException | IllegalComponentStateException var6) {
         RuntimeException ex = var6;
         log.warn("unable to set opacity {}", opacity, ex);
      } finally {
         this.overrideUndecorated = false;
      }

   }

   public boolean isUndecorated() {
      return this.overrideUndecorated || super.isUndecorated();
   }

   public void setContainedInScreen(Mode containedInScreen) {
      this.containedInScreen = containedInScreen;
   }

   public static enum Mode {
      ALWAYS,
      RESIZING,
      NEVER;
   }
}
