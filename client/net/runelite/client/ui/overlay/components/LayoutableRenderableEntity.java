package net.runelite.client.ui.overlay.components;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import net.runelite.client.ui.overlay.RenderableEntity;

public interface LayoutableRenderableEntity extends RenderableEntity {
   Rectangle getBounds();

   void setPreferredLocation(Point var1);

   void setPreferredSize(Dimension var1);
}
