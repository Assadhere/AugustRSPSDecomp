package net.runelite.client.plugins.xptracker;

import java.util.function.Function;
import net.runelite.client.util.QuantityFormatter;

public enum XpPanelLabel {
   TIME_TO_LEVEL("TTL", XpSnapshotSingle::getTimeTillGoalShort),
   XP_GAINED("XP Gained", (snap) -> {
      return format(snap.getXpGainedInSession());
   }),
   XP_HOUR("XP/hr", (snap) -> {
      return format(snap.getXpPerHour());
   }),
   XP_LEFT("XP Left", (snap) -> {
      return format(snap.getXpRemainingToGoal());
   }),
   ACTIONS_LEFT("Actions", (snap) -> {
      return format(snap.getActionsRemainingToGoal());
   }),
   ACTIONS_HOUR("Actions/hr", (snap) -> {
      return format(snap.getActionsPerHour());
   }),
   ACTIONS_DONE("Actions Done", (snap) -> {
      return format(snap.getActionsInSession());
   });

   private final String key;
   private final Function<XpSnapshotSingle, String> valueFunc;

   private static String format(int val) {
      return val == Integer.MAX_VALUE ? "N/A" : QuantityFormatter.quantityToRSDecimalStack(val, true);
   }

   public String getKey() {
      return this.key;
   }

   public Function<XpSnapshotSingle, String> getValueFunc() {
      return this.valueFunc;
   }

   private XpPanelLabel(String key, Function valueFunc) {
      this.key = key;
      this.valueFunc = valueFunc;
   }
}
