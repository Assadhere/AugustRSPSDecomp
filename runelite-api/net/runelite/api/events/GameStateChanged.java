package net.runelite.api.events;

import net.runelite.api.GameState;

public class GameStateChanged {
   private GameState gameState;

   public GameState getGameState() {
      return this.gameState;
   }

   public void setGameState(GameState gameState) {
      this.gameState = gameState;
   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof GameStateChanged)) {
         return false;
      } else {
         GameStateChanged other = (GameStateChanged)o;
         if (!other.canEqual(this)) {
            return false;
         } else {
            Object this$gameState = this.getGameState();
            Object other$gameState = other.getGameState();
            if (this$gameState == null) {
               if (other$gameState != null) {
                  return false;
               }
            } else if (!this$gameState.equals(other$gameState)) {
               return false;
            }

            return true;
         }
      }
   }

   protected boolean canEqual(Object other) {
      return other instanceof GameStateChanged;
   }

   public int hashCode() {
      int PRIME = true;
      int result = 1;
      Object $gameState = this.getGameState();
      result = result * 59 + ($gameState == null ? 43 : $gameState.hashCode());
      return result;
   }

   public String toString() {
      return "GameStateChanged(gameState=" + String.valueOf(this.getGameState()) + ")";
   }
}
