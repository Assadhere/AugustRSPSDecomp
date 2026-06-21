package net.runelite.client.plugins.cluescrolls.clues;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Arrays;
import java.util.Collection;
import java.util.EnumMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import net.runelite.api.NPC;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldPoint;
import net.runelite.client.plugins.cluescrolls.ClueScrollOverlay;
import net.runelite.client.plugins.cluescrolls.ClueScrollPlugin;
import net.runelite.client.plugins.cluescrolls.clues.hotcold.HotColdArea;
import net.runelite.client.plugins.cluescrolls.clues.hotcold.HotColdLocation;
import net.runelite.client.plugins.cluescrolls.clues.hotcold.HotColdSolver;
import net.runelite.client.plugins.cluescrolls.clues.hotcold.HotColdTemperature;
import net.runelite.client.plugins.cluescrolls.clues.hotcold.HotColdTemperatureChange;
import net.runelite.client.ui.overlay.OverlayUtil;
import net.runelite.client.ui.overlay.components.LineComponent;
import net.runelite.client.ui.overlay.components.PanelComponent;
import net.runelite.client.ui.overlay.components.TitleComponent;

public class HotColdClue extends ClueScroll implements LocationClueScroll, LocationsClueScroll, NpcClueScroll {
   private static final HotColdClue BEGINNER_CLUE = new HotColdClue("Buried beneath the ground, who knows where it's found. Lucky for you, A man called Reldo may have a clue.", "Reldo", "Speak to Reldo to receive a strange device.", new WorldPoint(3211, 3494, 0), true);
   private static final HotColdClue MASTER_CLUE = new HotColdClue("Buried beneath the ground, who knows where it's found. Lucky for you, A man called Jorral may have a clue.", "Jorral", "Speak to Jorral to receive a strange device.", new WorldPoint(2436, 3347, 0), false);
   private final String text;
   private final String npc;
   private final String solution;
   private final WorldPoint npcLocation;
   private final boolean isBeginner;
   private HotColdSolver hotColdSolver;
   private WorldPoint location;

   public static HotColdClue forText(String text) {
      if (BEGINNER_CLUE.text.equalsIgnoreCase(text)) {
         BEGINNER_CLUE.reset();
         return BEGINNER_CLUE;
      } else if (MASTER_CLUE.text.equalsIgnoreCase(text)) {
         MASTER_CLUE.reset();
         return MASTER_CLUE;
      } else {
         return null;
      }
   }

   private HotColdClue(String text, String npc, String solution, WorldPoint npcLocation, boolean isBeginner) {
      this.text = text;
      this.npc = npc;
      this.solution = solution;
      this.npcLocation = npcLocation;
      this.isBeginner = isBeginner;
      this.setRequiresSpade(true);
   }

   public WorldPoint getLocation(ClueScrollPlugin plugin) {
      return this.location;
   }

   public WorldPoint[] getLocations(ClueScrollPlugin plugin) {
      return this.hotColdSolver.getLastWorldPoint() == null ? new WorldPoint[]{this.npcLocation} : (WorldPoint[])this.hotColdSolver.getPossibleLocations().stream().map(HotColdLocation::getWorldPoint).toArray((x$0) -> {
         return new WorldPoint[x$0];
      });
   }

   public void makeOverlayHint(PanelComponent panelComponent, ClueScrollPlugin plugin) {
      panelComponent.getChildren().add(TitleComponent.builder().text("Hot/Cold Clue").build());
      if (this.hotColdSolver.getLastWorldPoint() == null && this.location == null) {
         if (this.getNpc() != null) {
            panelComponent.getChildren().add(LineComponent.builder().left("NPC:").build());
            panelComponent.getChildren().add(LineComponent.builder().left(this.getNpc()).leftColor(ClueScrollOverlay.TITLED_CONTENT_COLOR).build());
         }

         panelComponent.getChildren().add(LineComponent.builder().left("Solution:").build());
         panelComponent.getChildren().add(LineComponent.builder().left(this.getSolution()).leftColor(ClueScrollOverlay.TITLED_CONTENT_COLOR).build());
      } else {
         panelComponent.getChildren().add(LineComponent.builder().left("Possible locations:").build());
         Map<HotColdArea, Integer> locationCounts = new EnumMap(HotColdArea.class);
         Collection<HotColdLocation> digLocations = this.hotColdSolver.getPossibleLocations();
         Iterator var5 = digLocations.iterator();

         while(var5.hasNext()) {
            HotColdLocation hotColdLocation = (HotColdLocation)var5.next();
            HotColdArea hotColdArea = hotColdLocation.getHotColdArea();
            if (locationCounts.containsKey(hotColdArea)) {
               locationCounts.put(hotColdArea, (Integer)locationCounts.get(hotColdArea) + 1);
            } else {
               locationCounts.put(hotColdArea, 1);
            }
         }

         HotColdArea area;
         if (digLocations.size() > 10) {
            var5 = locationCounts.keySet().iterator();

            while(var5.hasNext()) {
               area = (HotColdArea)var5.next();
               panelComponent.getChildren().add(LineComponent.builder().left(area.getName()).right(Integer.toString((Integer)locationCounts.get(area))).build());
            }
         } else {
            var5 = locationCounts.keySet().iterator();

            while(var5.hasNext()) {
               area = (HotColdArea)var5.next();
               panelComponent.getChildren().add(LineComponent.builder().left(area.getName() + ":").build());
               Iterator var10 = digLocations.iterator();

               while(var10.hasNext()) {
                  HotColdLocation hotColdLocation = (HotColdLocation)var10.next();
                  if (hotColdLocation.getHotColdArea() == area) {
                     panelComponent.getChildren().add(LineComponent.builder().left("- " + hotColdLocation.getArea()).leftColor(Color.LIGHT_GRAY).build());
                     if (digLocations.size() <= 5 && hotColdLocation.getEnemy() != null) {
                        panelComponent.getChildren().add(LineComponent.builder().left(hotColdLocation.getEnemy().getText()).leftColor(Color.YELLOW).build());
                     }
                  }
               }
            }
         }
      }

   }

   public void makeWorldOverlayHint(Graphics2D graphics, ClueScrollPlugin plugin) {
      if (this.location != null) {
         LocalPoint localLocation = LocalPoint.fromWorld(plugin.getClient(), this.getLocation());
         if (localLocation != null) {
            OverlayUtil.renderTileOverlay(plugin.getClient(), graphics, localLocation, plugin.getSpadeImage(), Color.ORANGE);
         }

      } else {
         if (this.hotColdSolver.getLastWorldPoint() == null && plugin.getNpcsToMark() != null) {
            Iterator var3 = plugin.getNpcsToMark().iterator();

            while(var3.hasNext()) {
               NPC npcToMark = (NPC)var3.next();
               OverlayUtil.renderActorOverlayImage(graphics, npcToMark, plugin.getClueScrollImage(), Color.ORANGE, 30);
            }
         }

         Collection<HotColdLocation> digLocations = this.hotColdSolver.getPossibleLocations();
         if (digLocations.size() < 10) {
            Iterator var10 = digLocations.iterator();

            while(var10.hasNext()) {
               HotColdLocation hotColdLocation = (HotColdLocation)var10.next();
               WorldPoint wp = hotColdLocation.getWorldPoint();
               LocalPoint localLocation = LocalPoint.fromWorld(plugin.getClient(), wp.getX(), wp.getY());
               if (localLocation == null) {
                  return;
               }

               OverlayUtil.renderTileOverlay(plugin.getClient(), graphics, localLocation, plugin.getSpadeImage(), Color.ORANGE);
            }
         }

      }
   }

   public boolean update(String message, ClueScrollPlugin plugin) {
      Set<HotColdTemperature> temperatureSet = this.isBeginner() ? HotColdTemperature.BEGINNER_HOT_COLD_TEMPERATURES : HotColdTemperature.MASTER_HOT_COLD_TEMPERATURES;
      HotColdTemperature temperature = HotColdTemperature.getFromTemperatureSet(temperatureSet, message);
      if (temperature == null) {
         return false;
      } else {
         WorldPoint localWorld = WorldPoint.getMirrorPoint(plugin.getClient().getLocalPlayer().getWorldLocation(), true);
         if ((!this.isBeginner() || temperature != HotColdTemperature.BEGINNER_VISIBLY_SHAKING) && (this.isBeginner() || temperature != HotColdTemperature.MASTER_VISIBLY_SHAKING)) {
            this.location = null;
         } else {
            this.markFinalSpot(localWorld);
         }

         HotColdTemperatureChange temperatureChange = HotColdTemperatureChange.of(message);
         this.hotColdSolver.signal(localWorld, temperature, temperatureChange);
         return true;
      }
   }

   public void reset() {
      this.location = null;
      this.initializeSolver();
   }

   private void initializeSolver() {
      Set<HotColdLocation> locations = (Set)Arrays.stream(HotColdLocation.values()).filter((l) -> {
         return l.isBeginnerClue() == this.isBeginner();
      }).collect(Collectors.toSet());
      this.hotColdSolver = new HotColdSolver(locations);
   }

   private void markFinalSpot(WorldPoint wp) {
      this.location = wp;
   }

   public String[] getNpcs(ClueScrollPlugin plugin) {
      return new String[]{this.npc};
   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof HotColdClue)) {
         return false;
      } else {
         HotColdClue other = (HotColdClue)o;
         if (!other.canEqual(this)) {
            return false;
         } else if (this.isBeginner() != other.isBeginner()) {
            return false;
         } else {
            label61: {
               Object this$text = this.getText();
               Object other$text = other.getText();
               if (this$text == null) {
                  if (other$text == null) {
                     break label61;
                  }
               } else if (this$text.equals(other$text)) {
                  break label61;
               }

               return false;
            }

            label54: {
               Object this$npc = this.getNpc();
               Object other$npc = other.getNpc();
               if (this$npc == null) {
                  if (other$npc == null) {
                     break label54;
                  }
               } else if (this$npc.equals(other$npc)) {
                  break label54;
               }

               return false;
            }

            Object this$solution = this.getSolution();
            Object other$solution = other.getSolution();
            if (this$solution == null) {
               if (other$solution != null) {
                  return false;
               }
            } else if (!this$solution.equals(other$solution)) {
               return false;
            }

            Object this$npcLocation = this.getNpcLocation();
            Object other$npcLocation = other.getNpcLocation();
            if (this$npcLocation == null) {
               if (other$npcLocation != null) {
                  return false;
               }
            } else if (!this$npcLocation.equals(other$npcLocation)) {
               return false;
            }

            return true;
         }
      }
   }

   protected boolean canEqual(Object other) {
      return other instanceof HotColdClue;
   }

   public int hashCode() {
      int PRIME = true;
      int result = 1;
      result = result * 59 + (this.isBeginner() ? 79 : 97);
      Object $text = this.getText();
      result = result * 59 + ($text == null ? 43 : $text.hashCode());
      Object $npc = this.getNpc();
      result = result * 59 + ($npc == null ? 43 : $npc.hashCode());
      Object $solution = this.getSolution();
      result = result * 59 + ($solution == null ? 43 : $solution.hashCode());
      Object $npcLocation = this.getNpcLocation();
      result = result * 59 + ($npcLocation == null ? 43 : $npcLocation.hashCode());
      return result;
   }

   public String getText() {
      return this.text;
   }

   public String getNpc() {
      return this.npc;
   }

   public String getSolution() {
      return this.solution;
   }

   public boolean isBeginner() {
      return this.isBeginner;
   }

   public HotColdSolver getHotColdSolver() {
      return this.hotColdSolver;
   }

   public WorldPoint getLocation() {
      return this.location;
   }

   private WorldPoint getNpcLocation() {
      return this.npcLocation;
   }
}
