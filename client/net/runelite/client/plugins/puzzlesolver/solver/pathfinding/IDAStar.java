package net.runelite.client.plugins.puzzlesolver.solver.pathfinding;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.runelite.client.plugins.puzzlesolver.solver.PuzzleState;
import net.runelite.client.plugins.puzzlesolver.solver.heuristics.Heuristic;

public class IDAStar extends Pathfinder {
   public IDAStar(Heuristic heuristic) {
      super(heuristic);
   }

   public List<PuzzleState> computePath(PuzzleState root) {
      PuzzleState goalNode = this.path(root);
      List<PuzzleState> path = new ArrayList();

      for(PuzzleState parent = goalNode; parent != null; parent = parent.getParent()) {
         path.add(0, parent);
      }

      return path;
   }

   private PuzzleState path(PuzzleState root) {
      int bound = root.getHeuristicValue(this.getHeuristic());

      while(true) {
         PuzzleState t = this.search(root, 0, bound);
         if (t != null) {
            return t;
         }

         ++bound;
      }
   }

   private PuzzleState search(PuzzleState node, int g, int bound) {
      int h = node.getHeuristicValue(this.getHeuristic());
      int f = g + h;
      if (f > bound) {
         return null;
      } else if (h == 0) {
         return node;
      } else {
         Iterator var6 = node.computeMoves().iterator();

         PuzzleState t;
         do {
            if (!var6.hasNext()) {
               return null;
            }

            PuzzleState successor = (PuzzleState)var6.next();
            t = this.search(successor, g + 1, bound);
         } while(t == null);

         return t;
      }
   }
}
