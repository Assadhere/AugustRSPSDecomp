package net.runelite.api.hooks;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.List;
import net.runelite.api.MainBufferProvider;
import net.runelite.api.Renderable;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetItem;

public interface Callbacks {
   void post(Object var1);

   void postDeferred(Object var1);

   void tick();

   void tickEnd();

   void frame();

   void serverTick();

   void drawScene();

   void drawAboveOverheads();

   void draw(MainBufferProvider var1, Graphics var2, int var3, int var4);

   void drawInterface(int var1, List<WidgetItem> var2);

   void drawLayer(Widget var1, List<WidgetItem> var2);

   MouseEvent mousePressed(MouseEvent var1);

   MouseEvent mouseReleased(MouseEvent var1);

   MouseEvent mouseClicked(MouseEvent var1);

   MouseEvent mouseEntered(MouseEvent var1);

   MouseEvent mouseExited(MouseEvent var1);

   MouseEvent mouseDragged(MouseEvent var1);

   MouseEvent mouseMoved(MouseEvent var1);

   MouseWheelEvent mouseWheelMoved(MouseWheelEvent var1);

   void keyPressed(KeyEvent var1);

   void keyReleased(KeyEvent var1);

   void keyTyped(KeyEvent var1);

   boolean draw(Renderable var1, boolean var2);

   void onDraw(Renderable var1);

   void error(String var1, Throwable var2);

   void openUrl(String var1);

   boolean isRuneLiteClientOutdated();
}
