package net.runelite.client.plugins.puzzlesolver;

import java.awt.Color;
import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.Range;

@ConfigGroup("puzzlesolver")
public interface PuzzleSolverConfig extends Config {
   @ConfigItem(
      keyName = "displaySolution",
      name = "Display solution",
      description = "Display a solution to the puzzle.",
      position = 0
   )
   default boolean displaySolution() {
      return true;
   }

   @ConfigItem(
      keyName = "displayRemainingMoves",
      name = "Display remaining moves",
      description = "Add a text line above puzzle boxes displaying the amount of remaining moves.",
      position = 1
   )
   default boolean displayRemainingMoves() {
      return true;
   }

   @ConfigItem(
      keyName = "drawDots",
      name = "Draw dots instead of arrows",
      description = "Draw dots increasing in size instead of arrows for the solution.",
      position = 2
   )
   default boolean drawDots() {
      return true;
   }

   @ConfigItem(
      keyName = "movesToShow",
      name = "Number of dots",
      description = "The number of moves as dots to show for the puzzle.",
      position = 3
   )
   @Range(
      min = 4,
      max = 8
   )
   default int movesToShow() {
      return 4;
   }

   @ConfigItem(
      keyName = "dotColor",
      name = "Dot start color",
      description = "Dot color for the first solution dot.",
      position = 4
   )
   default Color dotColor() {
      return Color.YELLOW;
   }

   @ConfigItem(
      keyName = "dotEndColor",
      name = "Dot end color",
      description = "Dot color of the last solution dot that dots blend towards.",
      position = 5
   )
   default Color dotEndColor() {
      return Color.RED;
   }
}
