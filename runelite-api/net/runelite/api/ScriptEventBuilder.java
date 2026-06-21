package net.runelite.api;

import javax.annotation.Nullable;
import net.runelite.api.widgets.Widget;

public interface ScriptEventBuilder {
   Object[] getArguments();

   ScriptEventBuilder setArguments(Object[] var1);

   Widget getSource();

   ScriptEventBuilder setSource(Widget var1);

   @Nullable
   Widget getTarget();

   ScriptEventBuilder setTarget(Widget var1);

   int getOp();

   ScriptEventBuilder setOp(int var1);

   ScriptEvent build();
}
