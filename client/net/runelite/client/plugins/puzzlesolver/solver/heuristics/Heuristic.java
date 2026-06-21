package net.runelite.client.plugins.puzzlesolver.solver.heuristics;

import net.runelite.client.plugins.puzzlesolver.solver.PuzzleState;

public interface Heuristic {
   int computeValue(PuzzleState var1);
}
