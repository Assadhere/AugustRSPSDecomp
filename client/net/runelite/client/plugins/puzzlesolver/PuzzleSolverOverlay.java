package net.runelite.client.plugins.puzzlesolver;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.Item;
import net.runelite.api.ItemContainer;
import net.runelite.api.Point;
import net.runelite.api.widgets.Widget;
import net.runelite.client.game.SpriteManager;
import net.runelite.client.plugins.puzzlesolver.solver.PuzzleSolver;
import net.runelite.client.plugins.puzzlesolver.solver.PuzzleState;
import net.runelite.client.plugins.puzzlesolver.solver.heuristics.ManhattanDistance;
import net.runelite.client.plugins.puzzlesolver.solver.pathfinding.IDAStar;
import net.runelite.client.plugins.puzzlesolver.solver.pathfinding.IDAStarMM;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayUtil;
import net.runelite.client.ui.overlay.components.BackgroundComponent;
import net.runelite.client.ui.overlay.components.TextComponent;
import net.runelite.client.util.ColorUtil;
import net.runelite.client.util.ImageUtil;

public class PuzzleSolverOverlay extends Overlay {
   private static final int INFO_BOX_WIDTH = 100;
   private static final int INFO_BOX_OFFSET_Y = 50;
   private static final int INFO_BOX_TOP_BORDER = 2;
   private static final int INFO_BOX_BOTTOM_BORDER = 2;
   private static final int PUZZLE_TILE_SIZE = 50;
   private static final int DOT_MARKER_MAX_SIZE = 24;
   private static final int DOT_MARKER_MIN_SIZE = 4;
   private final Client client;
   private final PuzzleSolverConfig config;
   private final ScheduledExecutorService executorService;
   private final SpriteManager spriteManager;
   private PuzzleSolver solver;
   private Future<?> solverFuture;
   private int[] cachedItems;
   private BufferedImage upArrow;
   private BufferedImage leftArrow;
   private BufferedImage rightArrow;

   @Inject
   public PuzzleSolverOverlay(Client client, PuzzleSolverConfig config, ScheduledExecutorService executorService, SpriteManager spriteManager) {
      this.setPosition(OverlayPosition.DYNAMIC);
      this.setPriority(0.75F);
      this.setLayer(OverlayLayer.ABOVE_WIDGETS);
      this.client = client;
      this.config = config;
      this.executorService = executorService;
      this.spriteManager = spriteManager;
   }

   public Dimension render(Graphics2D graphics) {
      if ((this.config.displaySolution() || this.config.displayRemainingMoves()) && this.client.getGameState() == GameState.LOGGED_IN) {
         boolean useNormalSolver = true;
         ItemContainer container = this.client.getItemContainer(140);
         if (container == null) {
            useNormalSolver = false;
            container = this.client.getItemContainer(221);
            if (container == null) {
               return null;
            }
         }

         Widget puzzleBox = this.client.getWidget(20054020);
         if (puzzleBox == null) {
            return null;
         } else {
            Point puzzleBoxLocation = puzzleBox.getCanvasLocation();
            String infoString = "Solving..";
            int[] itemIds = this.getItemIds(container, useNormalSolver);
            boolean shouldCache = false;
            int stepsLeft;
            int blankX;
            int lastBlankX;
            int i;
            if (this.solver != null) {
               if (this.solver.hasFailed()) {
                  infoString = "The puzzle could not be solved";
               } else if (this.solver.hasSolution()) {
                  boolean foundPosition = false;

                  int movesToShow;
                  PuzzleState currentState;
                  for(stepsLeft = 0; stepsLeft < 6; ++stepsLeft) {
                     movesToShow = this.solver.getPosition() + stepsLeft;
                     if (movesToShow == this.solver.getStepCount()) {
                        break;
                     }

                     currentState = this.solver.getStep(movesToShow);
                     if (currentState != null && currentState.hasPieces(itemIds)) {
                        foundPosition = true;
                        this.solver.setPosition(movesToShow);
                        if (stepsLeft > 0) {
                           shouldCache = true;
                        }
                        break;
                     }
                  }

                  if (!foundPosition) {
                     for(stepsLeft = 1; stepsLeft < 6; ++stepsLeft) {
                        movesToShow = this.solver.getPosition() - stepsLeft;
                        if (movesToShow < 0) {
                           break;
                        }

                        currentState = this.solver.getStep(movesToShow);
                        if (currentState != null && currentState.hasPieces(itemIds)) {
                           foundPosition = true;
                           shouldCache = true;
                           this.solver.setPosition(movesToShow);
                           break;
                        }
                     }
                  }

                  if (foundPosition) {
                     stepsLeft = this.solver.getStepCount() - this.solver.getPosition() - 1;
                     if (stepsLeft == 0) {
                        infoString = "Solved!";
                     } else if (this.config.displayRemainingMoves()) {
                        infoString = "Moves left: " + stepsLeft;
                     } else {
                        infoString = null;
                     }

                     if (this.config.displaySolution()) {
                        int lastBlankY;
                        int blankX;
                        int yDelta;
                        int x;
                        if (this.config.drawDots()) {
                           movesToShow = this.config.movesToShow();

                           for(lastBlankX = 1; lastBlankX <= movesToShow; ++lastBlankX) {
                              lastBlankY = this.solver.getPosition() + lastBlankX;
                              if (lastBlankY >= this.solver.getStepCount()) {
                                 break;
                              }

                              PuzzleState futureMove = this.solver.getStep(lastBlankY);
                              if (futureMove == null) {
                                 break;
                              }

                              blankX = futureMove.getEmptyPiece() % 5;
                              int blankY = futureMove.getEmptyPiece() / 5;
                              blankX = (lastBlankX - 1) * 20;
                              double denominator = (double)(movesToShow - 1);
                              yDelta = (int)Math.round(24.0 - (double)blankX / denominator);
                              int x = puzzleBoxLocation.getX() + blankX * 50 + 25 - yDelta / 2 - 1;
                              x = puzzleBoxLocation.getY() + blankY * 50 + 25 - yDelta / 2 - 1;
                              Color color = ColorUtil.colorLerp(this.config.dotColor(), this.config.dotEndColor(), (double)(lastBlankX - 1) / (double)(movesToShow - 1));
                              graphics.setColor(color);
                              graphics.fillOval(x, x, yDelta, yDelta);
                              graphics.setColor(Color.BLACK);
                              graphics.drawOval(x - 1, x - 1, yDelta + 1, yDelta + 1);
                           }
                        } else {
                           PuzzleState currentMove = this.solver.getStep(this.solver.getPosition());
                           lastBlankX = currentMove.getEmptyPiece() % 5;
                           lastBlankY = currentMove.getEmptyPiece() / 5;

                           for(i = 1; i < 4; ++i) {
                              blankX = this.solver.getPosition() + i;
                              if (blankX >= this.solver.getStepCount()) {
                                 break;
                              }

                              PuzzleState futureMove = this.solver.getStep(blankX);
                              if (futureMove == null) {
                                 break;
                              }

                              blankX = futureMove.getEmptyPiece() % 5;
                              int blankY = futureMove.getEmptyPiece() / 5;
                              int xDelta = blankX - lastBlankX;
                              yDelta = blankY - lastBlankY;
                              BufferedImage arrow;
                              if (xDelta > 0) {
                                 arrow = this.getRightArrow();
                              } else if (xDelta < 0) {
                                 arrow = this.getLeftArrow();
                              } else if (yDelta > 0) {
                                 arrow = this.getDownArrow();
                              } else {
                                 arrow = this.getUpArrow();
                              }

                              if (arrow != null) {
                                 x = puzzleBoxLocation.getX() + blankX * 50 + 25 - arrow.getWidth() / 2 - 1;
                                 int y = puzzleBoxLocation.getY() + blankY * 50 + 25 - arrow.getHeight() / 2 - 1;
                                 OverlayUtil.renderImageLocation(graphics, new Point(x, y), arrow);
                                 lastBlankX = blankX;
                                 lastBlankY = blankY;
                              }
                           }
                        }
                     }
                  }
               }
            }

            if (infoString != null) {
               int x = puzzleBoxLocation.getX() + puzzleBox.getWidth() / 2 - 50;
               stepsLeft = puzzleBoxLocation.getY() - 50;
               FontMetrics fm = graphics.getFontMetrics();
               lastBlankX = 2 + fm.getHeight() + 2;
               BackgroundComponent backgroundComponent = new BackgroundComponent();
               backgroundComponent.setRectangle(new Rectangle(x, stepsLeft, 100, lastBlankX));
               backgroundComponent.render(graphics);
               i = (100 - fm.stringWidth(infoString)) / 2;
               blankX = fm.getHeight();
               TextComponent textComponent = new TextComponent();
               textComponent.setPosition(new java.awt.Point(x + i, stepsLeft + blankX));
               textComponent.setText(infoString);
               textComponent.render(graphics);
            }

            if (this.solver == null || this.cachedItems == null || !shouldCache && this.solver.hasExceededWaitDuration() && !Arrays.equals(this.cachedItems, itemIds)) {
               this.solve(itemIds, useNormalSolver);
               shouldCache = true;
            }

            if (shouldCache) {
               this.cacheItems(itemIds);
            }

            return null;
         }
      } else {
         return null;
      }
   }

   private int[] getItemIds(ItemContainer container, boolean useNormalSolver) {
      int[] itemIds = new int[25];
      Item[] items = container.getItems();

      for(int i = 0; i < items.length; ++i) {
         itemIds[i] = items[i].getId();
      }

      if (itemIds.length > items.length) {
         itemIds[items.length] = -1;
      }

      return this.convertToSolverFormat(itemIds, useNormalSolver);
   }

   private int[] convertToSolverFormat(int[] items, boolean useNormalSolver) {
      int lowestId = Integer.MAX_VALUE;
      int[] convertedItems = new int[items.length];
      int[] var5 = items;
      int value = items.length;

      for(int var7 = 0; var7 < value; ++var7) {
         int id = var5[var7];
         if (id != -1 && lowestId > id) {
            lowestId = id;
         }
      }

      for(int i = 0; i < items.length; ++i) {
         if (items[i] != -1) {
            value = items[i] - lowestId;
            if (!useNormalSolver) {
               value /= 2;
            }

            convertedItems[i] = value;
         } else {
            convertedItems[i] = -1;
         }
      }

      return convertedItems;
   }

   private void cacheItems(int[] items) {
      this.cachedItems = new int[items.length];
      System.arraycopy(items, 0, this.cachedItems, 0, this.cachedItems.length);
   }

   private void solve(int[] items, boolean useNormalSolver) {
      if (this.solverFuture != null) {
         this.solverFuture.cancel(true);
      }

      PuzzleState puzzleState = new PuzzleState(items);
      if (useNormalSolver) {
         this.solver = new PuzzleSolver(new IDAStar(new ManhattanDistance()), puzzleState);
      } else {
         this.solver = new PuzzleSolver(new IDAStarMM(new ManhattanDistance()), puzzleState);
      }

      this.solverFuture = this.executorService.submit(this.solver);
   }

   private BufferedImage getDownArrow() {
      return this.spriteManager.getSprite(422, 1);
   }

   private BufferedImage getUpArrow() {
      if (this.upArrow == null) {
         this.upArrow = ImageUtil.rotateImage(this.getDownArrow(), Math.PI);
      }

      return this.upArrow;
   }

   private BufferedImage getLeftArrow() {
      if (this.leftArrow == null) {
         this.leftArrow = ImageUtil.rotateImage(this.getDownArrow(), 1.5707963267948966);
      }

      return this.leftArrow;
   }

   private BufferedImage getRightArrow() {
      if (this.rightArrow == null) {
         this.rightArrow = ImageUtil.rotateImage(this.getDownArrow(), 4.71238898038469);
      }

      return this.rightArrow;
   }
}
