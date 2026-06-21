package net.runelite.client.plugins.puzzlesolver.solver.heuristics;

import net.runelite.client.plugins.puzzlesolver.solver.PuzzleState;

public class ManhattanDistance implements Heuristic {
   public int computeValue(PuzzleState state) {
      int value = 0;
      PuzzleState parent = state.getParent();
      int x;
      int y;
      int piece;
      int goalX;
      int goalY;
      if (parent == null) {
         for(x = 0; x < 5; ++x) {
            for(y = 0; y < 5; ++y) {
               piece = state.getPiece(x, y);
               if (piece != -1) {
                  goalX = piece % 5;
                  goalY = piece / 5;
                  value += Math.abs(x - goalX) + Math.abs(y - goalY);
               }
            }
         }
      } else {
         value = parent.getHeuristicValue(this);
         x = parent.getEmptyPiece() % 5;
         y = parent.getEmptyPiece() / 5;
         piece = state.getEmptyPiece() % 5;
         goalX = state.getEmptyPiece() / 5;
         goalY = state.getPiece(x, y);
         int targetY;
         if (piece > x) {
            targetY = goalY % 5;
            if (targetY > x) {
               ++value;
            } else {
               --value;
            }
         } else if (piece < x) {
            targetY = goalY % 5;
            if (targetY < x) {
               ++value;
            } else {
               --value;
            }
         } else if (goalX > y) {
            targetY = goalY / 5;
            if (targetY > y) {
               ++value;
            } else {
               --value;
            }
         } else {
            targetY = goalY / 5;
            if (targetY < y) {
               ++value;
            } else {
               --value;
            }
         }
      }

      return value;
   }
}
