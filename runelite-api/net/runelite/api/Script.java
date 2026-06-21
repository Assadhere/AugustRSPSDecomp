package net.runelite.api;

public interface Script extends Node {
   int[] getIntOperands();

   int[] getInstructions();
}
