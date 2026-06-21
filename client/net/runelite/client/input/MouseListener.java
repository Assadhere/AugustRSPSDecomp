package net.runelite.client.input;

import java.awt.event.MouseEvent;

public interface MouseListener {
   MouseEvent mouseClicked(MouseEvent var1);

   MouseEvent mousePressed(MouseEvent var1);

   MouseEvent mouseReleased(MouseEvent var1);

   MouseEvent mouseEntered(MouseEvent var1);

   MouseEvent mouseExited(MouseEvent var1);

   MouseEvent mouseDragged(MouseEvent var1);

   MouseEvent mouseMoved(MouseEvent var1);
}
