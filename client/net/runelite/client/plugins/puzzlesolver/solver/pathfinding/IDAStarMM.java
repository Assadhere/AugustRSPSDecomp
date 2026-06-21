package net.runelite.client.plugins.puzzlesolver.solver.pathfinding;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import net.runelite.api.Point;
import net.runelite.client.plugins.puzzlesolver.solver.PuzzleState;
import net.runelite.client.plugins.puzzlesolver.solver.PuzzleSwapPattern;
import net.runelite.client.plugins.puzzlesolver.solver.heuristics.Heuristic;

public class IDAStarMM extends IDAStar {
   private PuzzleState currentState;
   private final List<PuzzleState> stateList = new ArrayList();
   private final List<List<Integer>> validRowNumbers = new ArrayList();
   private final List<List<Integer>> validColumnNumbers = new ArrayList();

   public IDAStarMM(Heuristic heuristic) {
      super(heuristic);
      this.validRowNumbers.add(Arrays.asList(0, 1, 2, 3, 4));
      this.validRowNumbers.add(Arrays.asList(6, 7, 8, 9));
      this.validColumnNumbers.add(Arrays.asList(5, 10, 15, 20));
   }

   public List<PuzzleState> computePath(PuzzleState root) {
      this.currentState = root;
      this.stateList.add(root);
      List<PuzzleState> path = new ArrayList();
      this.solveRow(0);
      this.solveColumn();
      this.solveRow(1);
      this.stateList.remove(this.stateList.size() - 1);
      path.addAll(super.computePath(this.currentState));
      path.addAll(0, this.stateList);
      return path;
   }

   private void solveRow(int row) {
      for(int i = row; i < 5; ++i) {
         int valTarget = row * 5 + i;
         int valCurrent = this.currentState.getPiece(i, row);
         if (valCurrent != valTarget) {
            this.moveTowardsVal(valTarget, i, row, true);
         }
      }

   }

   private void solveColumn() {
      int column = 0;

      for(int i = column + 1; i < 5; ++i) {
         int valTarget = column + i * 5;
         int valCurrent = this.currentState.getPiece(column, i);
         if (valCurrent != valTarget) {
            this.moveTowardsVal(valTarget, column, i, false);
         }
      }

   }

   private void moveTowardsVal(int valTarget, int x, int y, boolean rowMode) {
      boolean reached = false;

      while(true) {
         while(this.currentState.getPiece(x, y) != valTarget) {
            Point locVal = this.findPiece(valTarget);
            Point locBlank = this.findPiece(-1);
            if (reached) {
               if (rowMode) {
                  this.alignTargetX(valTarget, x, y);
                  this.swapUpRow(valTarget, x, y);
               } else {
                  this.alignTargetY(valTarget, x, y);
                  this.swapLeftColumn(valTarget, x, y);
               }
            } else {
               int distX = locVal.getX() - locBlank.getX();
               int distY = locVal.getY() - locBlank.getY();
               int distAbsX = Math.abs(distX);
               int distAbsY = Math.abs(distY);
               Point locSwap;
               if (distX == 0) {
                  if (distAbsY == 1) {
                     reached = true;
                  } else if (distY >= 2) {
                     locSwap = new Point(locBlank.getX(), locBlank.getY() + 1);
                     this.swap(locBlank, locSwap);
                  } else if (distY <= -2) {
                     locSwap = new Point(locBlank.getX(), locBlank.getY() - 1);
                     this.swap(locBlank, locSwap);
                  }
               } else if (distY == 0) {
                  if (distAbsX == 1) {
                     reached = true;
                  } else if (distX >= 2) {
                     locSwap = new Point(locBlank.getX() + 1, locBlank.getY());
                     this.swap(locBlank, locSwap);
                  } else if (distX <= -2) {
                     locSwap = new Point(locBlank.getX() - 1, locBlank.getY());
                     this.swap(locBlank, locSwap);
                  }
               } else if (rowMode) {
                  if (locBlank.getY() - 1 == y && ((List)this.validRowNumbers.get(y)).contains(this.currentState.getPiece(locBlank.getX(), locBlank.getY() - 1)) && this.currentState.getPiece(locBlank.getX(), locBlank.getY() - 1) < valTarget && distY <= -1) {
                     locSwap = new Point(locBlank.getX() + 1, locBlank.getY());
                     this.swap(locBlank, locSwap);
                  } else if (distY >= 1) {
                     locSwap = new Point(locBlank.getX(), locBlank.getY() + 1);
                     this.swap(locBlank, locSwap);
                  } else if (distY <= -1) {
                     locSwap = new Point(locBlank.getX(), locBlank.getY() - 1);
                     this.swap(locBlank, locSwap);
                  }
               } else if (locBlank.getX() - 1 == x && ((List)this.validColumnNumbers.get(x)).contains(this.currentState.getPiece(locBlank.getX() - 1, locBlank.getY())) && this.currentState.getPiece(locBlank.getX() - 1, locBlank.getY()) < valTarget && distX <= -1) {
                  locSwap = new Point(locBlank.getX(), locBlank.getY() + 1);
                  this.swap(locBlank, locSwap);
               } else if (distX >= 1) {
                  locSwap = new Point(locBlank.getX() + 1, locBlank.getY());
                  this.swap(locBlank, locSwap);
               } else if (distX <= -1) {
                  locSwap = new Point(locBlank.getX() - 1, locBlank.getY());
                  this.swap(locBlank, locSwap);
               }
            }
         }

         return;
      }
   }

   private void alignTargetX(int valTarget, int x, int y) {
      Point locVal = this.findPiece(valTarget);
      if (locVal.getX() != x) {
         int direction = Integer.signum(x - locVal.getX());

         while(locVal.getX() != x) {
            locVal = this.findPiece(valTarget);
            Point locBlank = this.findPiece(-1);
            if (x - locVal.getX() == 0) {
               break;
            }

            int diff;
            if (locVal.getX() == locBlank.getX()) {
               diff = locBlank.getY() - locVal.getY();
               if (diff == 1) {
                  Point loc1 = new Point(locBlank.getX() + direction, locBlank.getY());
                  Point loc2 = new Point(loc1.getX(), loc1.getY() - 1);
                  this.swap(locBlank, loc1);
                  this.swap(loc1, loc2);
                  this.swap(loc2, locVal);
               } else if (diff == -1) {
                  this.swap(locBlank, locVal);
               }
            } else if (locVal.getY() == locBlank.getY()) {
               diff = locBlank.getX() - locVal.getX();
               if (diff == 1) {
                  if (direction == 1) {
                     this.swap(locVal, locBlank);
                  } else if (direction == -1) {
                     if (locVal.getY() == 4) {
                        this.performSwapPattern(locBlank, locVal, PuzzleSwapPattern.ROTATE_LEFT_UP);
                     } else {
                        this.performSwapPattern(locBlank, locVal, PuzzleSwapPattern.ROTATE_LEFT_DOWN);
                     }
                  }
               } else if (diff == -1) {
                  if (direction == -1) {
                     this.swap(locVal, locBlank);
                  } else if (direction == 1) {
                     if (locVal.getY() == 4) {
                        this.performSwapPattern(locBlank, locVal, PuzzleSwapPattern.ROTATE_RIGHT_UP);
                     } else {
                        this.performSwapPattern(locBlank, locVal, PuzzleSwapPattern.ROTATE_RIGHT_DOWN);
                     }
                  }
               }
            }
         }

      }
   }

   private void swapUpRow(int valTarget, int x, int y) {
      Point locVal = this.findPiece(valTarget);
      Point locBlank = this.findPiece(-1);
      if (locVal.getX() != x || locVal.getY() != y) {
         if (locBlank.getX() == x && locBlank.getY() == y && locVal.getY() - 1 == y) {
            this.swap(locBlank, locVal);
         } else {
            while(true) {
               locVal = this.findPiece(valTarget);
               locBlank = this.findPiece(-1);
               if (locVal.getX() == x && locVal.getY() == y) {
                  return;
               }

               int diff;
               if (locVal.getX() == locBlank.getX()) {
                  diff = locBlank.getY() - locVal.getY();
                  if (diff == 1) {
                     if (x == 4) {
                        this.performSwapPattern(locBlank, locVal, PuzzleSwapPattern.LAST_PIECE_ROW);
                        return;
                     }

                     this.performSwapPattern(locBlank, locVal, PuzzleSwapPattern.ROTATE_UP_RIGHT);
                  } else if (diff == -1) {
                     this.swap(locBlank, locVal);
                  }
               } else if (locVal.getY() == locBlank.getY()) {
                  diff = locBlank.getX() - locVal.getX();
                  if (diff == 1) {
                     this.performSwapPattern(locBlank, locVal, PuzzleSwapPattern.SHUFFLE_UP_RIGHT);
                  } else if (diff == -1) {
                     if (locVal.getY() - 1 == y) {
                        Point loc1 = new Point(locBlank.getX(), locBlank.getY() + 1);
                        Point loc2 = new Point(loc1.getX() + 1, loc1.getY());
                        this.swap(locBlank, loc1);
                        this.swap(loc1, loc2);
                     } else {
                        this.performSwapPattern(locBlank, locVal, PuzzleSwapPattern.SHUFFLE_UP_LEFT);
                     }
                  }
               }
            }
         }
      }
   }

   private void alignTargetY(int valTarget, int x, int y) {
      Point locVal = this.findPiece(valTarget);
      if (locVal.getY() != y) {
         int direction = Integer.signum(y - locVal.getY());

         while(locVal.getY() != y) {
            locVal = this.findPiece(valTarget);
            Point locBlank = this.findPiece(-1);
            if (y - locVal.getY() == 0) {
               break;
            }

            int diff;
            if (locVal.getY() == locBlank.getY()) {
               diff = locBlank.getX() - locVal.getX();
               if (diff == 1) {
                  Point loc1 = new Point(locBlank.getX(), locBlank.getY() + direction);
                  Point loc2 = new Point(loc1.getX() - 1, loc1.getY());
                  this.swap(locBlank, loc1);
                  this.swap(loc1, loc2);
                  this.swap(loc2, locVal);
               } else if (diff == -1) {
                  this.swap(locBlank, locVal);
               }
            } else if (locVal.getX() == locBlank.getX()) {
               diff = locBlank.getY() - locVal.getY();
               if (diff == 1) {
                  if (direction == 1) {
                     this.swap(locVal, locBlank);
                  } else if (direction == -1) {
                     if (locVal.getX() == 4) {
                        this.performSwapPattern(locBlank, locVal, PuzzleSwapPattern.ROTATE_UP_LEFT);
                     } else {
                        this.performSwapPattern(locBlank, locVal, PuzzleSwapPattern.ROTATE_UP_RIGHT);
                     }
                  }
               } else if (diff == -1) {
                  if (direction == -1) {
                     this.swap(locVal, locBlank);
                  } else if (direction == 1) {
                     if (locVal.getX() == 4) {
                        this.performSwapPattern(locBlank, locVal, PuzzleSwapPattern.ROTATE_DOWN_LEFT);
                     } else {
                        this.performSwapPattern(locBlank, locVal, PuzzleSwapPattern.ROTATE_DOWN_RIGHT);
                     }
                  }
               }
            }
         }

      }
   }

   private void swapLeftColumn(int valTarget, int x, int y) {
      Point locVal = this.findPiece(valTarget);
      Point locBlank = this.findPiece(-1);
      if (locVal.getX() != x || locVal.getY() != y) {
         if (locBlank.getX() == x && locBlank.getY() == y && locVal.getX() - 1 == x) {
            this.swap(locBlank, locVal);
         } else {
            while(true) {
               locVal = this.findPiece(valTarget);
               locBlank = this.findPiece(-1);
               if (locVal.getX() == x && locVal.getY() == y) {
                  return;
               }

               int diff;
               if (locVal.getX() == locBlank.getX()) {
                  diff = locBlank.getY() - locVal.getY();
                  if (diff == 1) {
                     this.performSwapPattern(locBlank, locVal, PuzzleSwapPattern.SHUFFLE_UP_BELOW);
                  } else if (diff == -1) {
                     if (locVal.getX() - 1 == x) {
                        Point loc1 = new Point(locBlank.getX() + 1, locBlank.getY());
                        Point loc2 = new Point(loc1.getX(), loc1.getY() + 1);
                        this.swap(locBlank, loc1);
                        this.swap(loc1, loc2);
                     } else {
                        this.performSwapPattern(locBlank, locVal, PuzzleSwapPattern.SHUFFLE_UP_ABOVE);
                     }
                  }
               } else if (locVal.getY() == locBlank.getY()) {
                  diff = locBlank.getX() - locVal.getX();
                  if (diff == 1) {
                     if (y == 4) {
                        this.performSwapPattern(locBlank, locVal, PuzzleSwapPattern.LAST_PIECE_COLUMN);
                        return;
                     }

                     this.performSwapPattern(locBlank, locVal, PuzzleSwapPattern.ROTATE_LEFT_DOWN);
                  } else if (diff == -1) {
                     this.swap(locBlank, locVal);
                  }
               }
            }
         }
      }
   }

   private void swap(Point p1, Point p2) {
      PuzzleState newState = this.currentState.swap(p1.getX(), p1.getY(), p2.getX(), p2.getY());
      this.currentState = newState;
      this.stateList.add(newState);
   }

   private Point findPiece(int val) {
      for(int x = 0; x < 5; ++x) {
         for(int y = 0; y < 5; ++y) {
            if (this.currentState.getPiece(x, y) == val) {
               return new Point(x, y);
            }
         }
      }

      throw new IllegalStateException("Piece wasn't found!");
   }

   private void performSwapPattern(Point locBlank, Point locVal, PuzzleSwapPattern pattern) {
      int[] offsets;
      switch (pattern) {
         case ROTATE_LEFT_UP:
         case ROTATE_RIGHT_UP:
         case ROTATE_RIGHT_DOWN:
         case ROTATE_LEFT_DOWN:
            offsets = PuzzleSwapPattern.ROTATE_LEFT_UP.getPoints();
            break;
         case ROTATE_UP_LEFT:
         case ROTATE_UP_RIGHT:
         case ROTATE_DOWN_LEFT:
         case ROTATE_DOWN_RIGHT:
            offsets = PuzzleSwapPattern.ROTATE_UP_LEFT.getPoints();
            break;
         default:
            offsets = pattern.getPoints();
      }

      if (offsets != null && offsets.length % 2 != 1) {
         int modX = pattern.getModX();
         int modY = pattern.getModY();
         ArrayList<Point> points = new ArrayList();

         for(int i = 0; i < offsets.length; i += 2) {
            int x = locVal.getX() + modX * offsets[i];
            int y = locVal.getY() + modY * offsets[i + 1];
            points.add(new Point(x, y));
         }

         points.add(locVal);
         Point start;
         Point p;
         if (pattern != PuzzleSwapPattern.LAST_PIECE_ROW && pattern != PuzzleSwapPattern.LAST_PIECE_COLUMN) {
            start = locBlank;

            for(Iterator var14 = points.iterator(); var14.hasNext(); start = p) {
               p = (Point)var14.next();
               this.swap(start, p);
            }
         } else {
            start = (Point)points.get(0);
            Point loc2 = (Point)points.get(1);
            p = (Point)points.get(2);
            Point loc4 = (Point)points.get(3);
            this.swap(locBlank, locVal);
            this.swap(locVal, p);
            this.swap(p, start);
            this.swap(start, loc2);
            this.swap(loc2, locVal);
            this.swap(locVal, p);
            this.swap(p, start);
            this.swap(start, loc2);
            this.swap(loc2, locVal);
            this.swap(locVal, locBlank);
            this.swap(locBlank, loc4);
            this.swap(loc4, p);
            this.swap(p, start);
            this.swap(start, loc2);
            this.swap(loc2, locVal);
         }

      } else {
         throw new IllegalStateException("Unexpected points given in pattern!");
      }
   }
}
