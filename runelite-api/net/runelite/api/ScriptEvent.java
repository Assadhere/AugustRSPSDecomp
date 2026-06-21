package net.runelite.api;

import javax.annotation.Nullable;
import net.runelite.api.widgets.Widget;

public interface ScriptEvent {
   int MOUSE_X = -2147483647;
   int MOUSE_Y = -2147483646;
   int MENU_OP = -2147483644;
   int WIDGET_ID = -2147483645;
   int WIDGET_INDEX = -2147483643;
   int WIDGET_TARGET_ID = -2147483642;
   int WIDGET_TARGET_INDEX = -2147483641;
   int KEY_CODE = -2147483640;
   int KEY_CHAR = -2147483639;
   String NAME = "event_opbase";

   Widget getSource();

   @Nullable
   Widget getTarget();

   Object[] getArguments();

   int getOp();

   String getOpbase();

   int getMouseX();

   int getMouseY();

   int getTypedKeyCode();

   int getTypedKeyChar();

   void run();
}
