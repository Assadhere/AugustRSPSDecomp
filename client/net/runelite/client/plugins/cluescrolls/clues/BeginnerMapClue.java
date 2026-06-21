package net.runelite.client.plugins.cluescrolls.clues;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.UnmodifiableIterator;
import net.runelite.api.coords.WorldPoint;

public class BeginnerMapClue extends MapClue implements LocationClueScroll {
   private static final ImmutableList<BeginnerMapClue> CLUES = ImmutableList.of(new BeginnerMapClue(346, new WorldPoint(3166, 3361, 0), "West of the Champions' Guild"), new BeginnerMapClue(347, new WorldPoint(3290, 3374, 0), "Outside Varrock East Mine"), new BeginnerMapClue(348, new WorldPoint(3093, 3226, 0), "South of Draynor Village Bank"), new BeginnerMapClue(351, new WorldPoint(3043, 3398, 0), "At the standing stones north of Falador"), new BeginnerMapClue(356, new WorldPoint(3110, 3152, 0), "On the south side of the Wizards' Tower (DIS)"));
   private final int widgetGroupID;

   private BeginnerMapClue(int widgetGroupID, WorldPoint location, String description) {
      super(-1, location, description);
      this.widgetGroupID = widgetGroupID;
      this.setRequiresSpade(true);
   }

   public static BeginnerMapClue forWidgetID(int widgetGroupID) {
      UnmodifiableIterator var1 = CLUES.iterator();

      BeginnerMapClue clue;
      do {
         if (!var1.hasNext()) {
            return null;
         }

         clue = (BeginnerMapClue)var1.next();
      } while(clue.widgetGroupID != widgetGroupID);

      return clue;
   }

   public int getWidgetGroupID() {
      return this.widgetGroupID;
   }
}
