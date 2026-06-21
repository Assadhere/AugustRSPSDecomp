package net.runelite.client.plugins.cluescrolls.clues;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import net.runelite.api.ItemContainer;
import net.runelite.api.coords.WorldPoint;
import net.runelite.client.plugins.cluescrolls.ClueScrollOverlay;
import net.runelite.client.plugins.cluescrolls.ClueScrollPlugin;
import net.runelite.client.ui.overlay.components.LineComponent;
import net.runelite.client.ui.overlay.components.PanelComponent;
import net.runelite.client.ui.overlay.components.TitleComponent;
import net.runelite.client.util.Text;

public class ThreeStepCrypticClue extends ClueScroll implements ObjectClueScroll, NpcClueScroll, LocationsClueScroll {
   private final List<Map.Entry<CrypticClue, Boolean>> clueSteps;
   private final String text;

   public static ThreeStepCrypticClue forText(String plainText, String text) {
      String[] split = text.split("<br>\\s*<br>");
      List<Map.Entry<CrypticClue, Boolean>> steps = new ArrayList(split.length);
      String[] var4 = split;
      int var5 = split.length;

      for(int var6 = 0; var6 < var5; ++var6) {
         String part = var4[var6];
         boolean isDone = part.contains("<str>");
         String rawText = Text.sanitizeMultilineText(part);
         Iterator var10 = CrypticClue.CLUES.iterator();

         while(var10.hasNext()) {
            CrypticClue clue = (CrypticClue)var10.next();
            if (rawText.equalsIgnoreCase(clue.getText())) {
               steps.add(new AbstractMap.SimpleEntry(clue, isDone));
               break;
            }
         }
      }

      if (!steps.isEmpty() && steps.size() >= 3) {
         return new ThreeStepCrypticClue(steps, plainText);
      } else {
         return null;
      }
   }

   public void makeOverlayHint(PanelComponent panelComponent, ClueScrollPlugin plugin) {
      panelComponent.setPreferredSize(new Dimension(200, 0));

      for(int i = 0; i < this.clueSteps.size(); ++i) {
         Map.Entry<CrypticClue, Boolean> e = (Map.Entry)this.clueSteps.get(i);
         if (!(Boolean)e.getValue()) {
            CrypticClue c = (CrypticClue)e.getKey();
            panelComponent.getChildren().add(TitleComponent.builder().text("Cryptic Clue #" + (i + 1)).build());
            panelComponent.getChildren().add(LineComponent.builder().left("Solution:").build());
            panelComponent.getChildren().add(LineComponent.builder().left(c.getSolution(plugin)).leftColor(ClueScrollOverlay.TITLED_CONTENT_COLOR).build());
            c.renderOverlayNote(panelComponent, plugin);
         }
      }

   }

   public void makeWorldOverlayHint(Graphics2D graphics, ClueScrollPlugin plugin) {
      Iterator var3 = this.clueSteps.iterator();

      while(var3.hasNext()) {
         Map.Entry<CrypticClue, Boolean> e = (Map.Entry)var3.next();
         if (!(Boolean)e.getValue()) {
            ((CrypticClue)e.getKey()).makeWorldOverlayHint(graphics, plugin);
         }
      }

   }

   public boolean update(int containerId, ItemContainer itemContainer) {
      if (containerId != 93) {
         return false;
      } else {
         return this.checkForPart(itemContainer, 19837, 0) || this.checkForPart(itemContainer, 19838, 1) || this.checkForPart(itemContainer, 19839, 2);
      }
   }

   private boolean checkForPart(ItemContainer itemContainer, int clueScrollPart, int index) {
      if (itemContainer.contains(clueScrollPart)) {
         Map.Entry<CrypticClue, Boolean> entry = (Map.Entry)this.clueSteps.get(index);
         if (!(Boolean)entry.getValue()) {
            entry.setValue(true);
            return true;
         }
      }

      return false;
   }

   public void reset() {
      Iterator var1 = this.clueSteps.iterator();

      while(var1.hasNext()) {
         Map.Entry<CrypticClue, Boolean> clueStep = (Map.Entry)var1.next();
         clueStep.setValue(false);
      }

   }

   public WorldPoint getLocation(ClueScrollPlugin plugin) {
      return null;
   }

   public WorldPoint[] getLocations(ClueScrollPlugin plugin) {
      return (WorldPoint[])this.clueSteps.stream().filter((s) -> {
         return !(Boolean)s.getValue();
      }).map((s) -> {
         return ((CrypticClue)s.getKey()).getLocation(plugin);
      }).filter(Objects::nonNull).toArray((x$0) -> {
         return new WorldPoint[x$0];
      });
   }

   public String[] getNpcs(ClueScrollPlugin plugin) {
      return (String[])this.clueSteps.stream().filter((s) -> {
         return !(Boolean)s.getValue();
      }).map((s) -> {
         return ((CrypticClue)s.getKey()).getNpc(plugin);
      }).toArray((x$0) -> {
         return new String[x$0];
      });
   }

   public int[] getObjectIds() {
      return this.clueSteps.stream().filter((s) -> {
         return !(Boolean)s.getValue();
      }).mapToInt((s) -> {
         return ((CrypticClue)s.getKey()).getObjectId();
      }).toArray();
   }

   public int[] getConfigKeys() {
      return this.clueSteps.stream().map(Map.Entry::getKey).flatMapToInt((c) -> {
         return Arrays.stream(c.getConfigKeys());
      }).toArray();
   }

   public List<Map.Entry<CrypticClue, Boolean>> getClueSteps() {
      return this.clueSteps;
   }

   public String getText() {
      return this.text;
   }

   public ThreeStepCrypticClue(List<Map.Entry<CrypticClue, Boolean>> clueSteps, String text) {
      this.clueSteps = clueSteps;
      this.text = text;
   }
}
