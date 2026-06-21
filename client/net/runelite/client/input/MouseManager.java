package net.runelite.client.input;

import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import javax.inject.Inject;
import javax.inject.Singleton;
import net.runelite.client.config.RuneLiteConfig;

@Singleton
public class MouseManager {
   private static final int MOUSE_BUTTON_4 = 4;
   private final List<MouseListener> mouseListeners = new CopyOnWriteArrayList();
   private final List<MouseWheelListener> mouseWheelListeners = new CopyOnWriteArrayList();
   private final RuneLiteConfig runeLiteConfig;

   @Inject
   private MouseManager(RuneLiteConfig runeLiteConfig) {
      this.runeLiteConfig = runeLiteConfig;
   }

   public void registerMouseListener(MouseListener mouseListener) {
      if (!this.mouseListeners.contains(mouseListener)) {
         this.mouseListeners.add(mouseListener);
      }

   }

   public void registerMouseListener(int position, MouseListener mouseListener) {
      this.mouseListeners.add(position, mouseListener);
   }

   public void unregisterMouseListener(MouseListener mouseListener) {
      this.mouseListeners.remove(mouseListener);
   }

   public void registerMouseWheelListener(MouseWheelListener mouseWheelListener) {
      if (!this.mouseWheelListeners.contains(mouseWheelListener)) {
         this.mouseWheelListeners.add(mouseWheelListener);
      }

   }

   public void registerMouseWheelListener(int position, MouseWheelListener mouseWheelListener) {
      this.mouseWheelListeners.add(position, mouseWheelListener);
   }

   public void unregisterMouseWheelListener(MouseWheelListener mouseWheelListener) {
      this.mouseWheelListeners.remove(mouseWheelListener);
   }

   public MouseEvent processMousePressed(MouseEvent mouseEvent) {
      if (mouseEvent.isConsumed()) {
         return mouseEvent;
      } else {
         this.checkExtraMouseButtons(mouseEvent);
         Iterator var2 = this.mouseListeners.iterator();

         while(var2.hasNext()) {
            MouseListener mouseListener = (MouseListener)var2.next();
            mouseEvent = mouseListener.mousePressed(mouseEvent);
            if (mouseEvent.isConsumed()) {
               break;
            }
         }

         return mouseEvent;
      }
   }

   public MouseEvent processMouseReleased(MouseEvent mouseEvent) {
      if (mouseEvent.isConsumed()) {
         return mouseEvent;
      } else {
         this.checkExtraMouseButtons(mouseEvent);
         Iterator var2 = this.mouseListeners.iterator();

         while(var2.hasNext()) {
            MouseListener mouseListener = (MouseListener)var2.next();
            mouseEvent = mouseListener.mouseReleased(mouseEvent);
            if (mouseEvent.isConsumed()) {
               break;
            }
         }

         return mouseEvent;
      }
   }

   public MouseEvent processMouseClicked(MouseEvent mouseEvent) {
      if (mouseEvent.isConsumed()) {
         return mouseEvent;
      } else {
         this.checkExtraMouseButtons(mouseEvent);
         Iterator var2 = this.mouseListeners.iterator();

         while(var2.hasNext()) {
            MouseListener mouseListener = (MouseListener)var2.next();
            mouseEvent = mouseListener.mouseClicked(mouseEvent);
            if (mouseEvent.isConsumed()) {
               break;
            }
         }

         return mouseEvent;
      }
   }

   private void checkExtraMouseButtons(MouseEvent mouseEvent) {
      int button = mouseEvent.getButton();
      if (button >= 4 && this.runeLiteConfig.blockExtraMouseButtons()) {
         mouseEvent.consume();
      }

   }

   public MouseEvent processMouseEntered(MouseEvent mouseEvent) {
      if (mouseEvent.isConsumed()) {
         return mouseEvent;
      } else {
         Iterator var2 = this.mouseListeners.iterator();

         while(var2.hasNext()) {
            MouseListener mouseListener = (MouseListener)var2.next();
            mouseEvent = mouseListener.mouseEntered(mouseEvent);
            if (mouseEvent.isConsumed()) {
               break;
            }
         }

         return mouseEvent;
      }
   }

   public MouseEvent processMouseExited(MouseEvent mouseEvent) {
      if (mouseEvent.isConsumed()) {
         return mouseEvent;
      } else {
         Iterator var2 = this.mouseListeners.iterator();

         while(var2.hasNext()) {
            MouseListener mouseListener = (MouseListener)var2.next();
            mouseEvent = mouseListener.mouseExited(mouseEvent);
            if (mouseEvent.isConsumed()) {
               break;
            }
         }

         return mouseEvent;
      }
   }

   public MouseEvent processMouseDragged(MouseEvent mouseEvent) {
      if (mouseEvent.isConsumed()) {
         return mouseEvent;
      } else {
         Iterator var2 = this.mouseListeners.iterator();

         while(var2.hasNext()) {
            MouseListener mouseListener = (MouseListener)var2.next();
            mouseEvent = mouseListener.mouseDragged(mouseEvent);
            if (mouseEvent.isConsumed()) {
               break;
            }
         }

         return mouseEvent;
      }
   }

   public MouseEvent processMouseMoved(MouseEvent mouseEvent) {
      if (mouseEvent.isConsumed()) {
         return mouseEvent;
      } else {
         Iterator var2 = this.mouseListeners.iterator();

         while(var2.hasNext()) {
            MouseListener mouseListener = (MouseListener)var2.next();
            mouseEvent = mouseListener.mouseMoved(mouseEvent);
            if (mouseEvent.isConsumed()) {
               break;
            }
         }

         return mouseEvent;
      }
   }

   public MouseWheelEvent processMouseWheelMoved(MouseWheelEvent mouseWheelEvent) {
      if (mouseWheelEvent.isConsumed()) {
         return mouseWheelEvent;
      } else {
         Iterator var2 = this.mouseWheelListeners.iterator();

         while(var2.hasNext()) {
            MouseWheelListener mouseWheelListener = (MouseWheelListener)var2.next();
            mouseWheelEvent = mouseWheelListener.mouseWheelMoved(mouseWheelEvent);
            if (mouseWheelEvent.isConsumed()) {
               break;
            }
         }

         return mouseWheelEvent;
      }
   }
}
