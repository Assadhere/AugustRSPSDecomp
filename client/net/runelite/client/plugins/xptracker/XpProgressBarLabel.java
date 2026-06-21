package net.runelite.client.plugins.xptracker;

import java.text.DecimalFormat;
import java.util.function.Function;

public enum XpProgressBarLabel {
   PERCENTAGE((snap) -> {
      DecimalFormat var10000 = XpInfoBox.TWO_DECIMAL_FORMAT;
      return var10000.format(snap.getSkillProgressToGoal()) + "%";
   }),
   TIME_TO_LEVEL(XpSnapshotSingle::getTimeTillGoal),
   HOURS_TO_LEVEL(XpSnapshotSingle::getTimeTillGoalHours);

   private final Function<XpSnapshotSingle, String> valueFunc;

   public Function<XpSnapshotSingle, String> getValueFunc() {
      return this.valueFunc;
   }

   private XpProgressBarLabel(Function valueFunc) {
      this.valueFunc = valueFunc;
   }
}
