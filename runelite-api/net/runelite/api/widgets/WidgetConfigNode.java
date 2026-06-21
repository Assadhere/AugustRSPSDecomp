package net.runelite.api.widgets;

import net.runelite.api.Node;

public interface WidgetConfigNode extends Node {
   int getClickMask();

   int getOpMask();
}
