package net.runelite.client.plugins.cluescrolls.clues;

import com.google.common.collect.ImmutableList;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.runelite.api.NPC;
import net.runelite.api.coords.WorldPoint;
import net.runelite.client.plugins.cluescrolls.ClueScrollOverlay;
import net.runelite.client.plugins.cluescrolls.ClueScrollPlugin;
import net.runelite.client.ui.overlay.OverlayUtil;
import net.runelite.client.ui.overlay.components.LineComponent;
import net.runelite.client.ui.overlay.components.PanelComponent;
import net.runelite.client.ui.overlay.components.TitleComponent;

public class MusicClue extends ClueScroll implements NpcClueScroll, LocationClueScroll {
   private static final WorldPoint LOCATION = new WorldPoint(2990, 3384, 0);
   private static final String CECILIA = "Cecilia";
   private static final Pattern SONG_PATTERN = Pattern.compile("<col=ffffff>([A-Za-z !&',.]+)</col>");
   static final List<MusicClue> CLUES = ImmutableList.of(new MusicClue(23155, "Vision"), new MusicClue(23156, "The Forlorn Homestead"), new MusicClue(23157, "Tiptoe"), new MusicClue(23158, "Rugged Terrain"), new MusicClue(23159, "On the Shore"), new MusicClue(23160, "Alone"), new MusicClue(23138, "Karamja Jam"), new MusicClue(23139, "Faerie"), new MusicClue(23140, "Forgotten"), new MusicClue(23141, "Catch Me If You Can"), new MusicClue(23142, "Cave of Beasts"), new MusicClue(23143, "Devils May Care"), new MusicClue[]{new MusicClue(23174, "Scorpia Dances"), new MusicClue(23175, "Complication"), new MusicClue(23176, "Subterranea"), new MusicClue(23177, "Little Cave of Horrors"), new MusicClue(23178, "Roc and Roll"), new MusicClue(23179, "La Mort"), new MusicClue(23180, "Fossilised"), new MusicClue(23181, "Hells Bells"), new MusicClue(25792, "Regal Pomp"), new MusicClue(28918, "The Moons of Ruin"), new MusicClue(24773, "Lament for the Hallowed"), new MusicClue(26943, "The Pharaoh")});
   private final int itemId;
   private final String song;

   public void makeOverlayHint(PanelComponent panelComponent, ClueScrollPlugin plugin) {
      panelComponent.getChildren().add(TitleComponent.builder().text("Music Clue").build());
      panelComponent.getChildren().add(LineComponent.builder().left("NPC:").build());
      panelComponent.getChildren().add(LineComponent.builder().left("Cecilia").leftColor(ClueScrollOverlay.TITLED_CONTENT_COLOR).build());
      panelComponent.getChildren().add(LineComponent.builder().left("Location:").build());
      panelComponent.getChildren().add(LineComponent.builder().left("Falador Park").leftColor(ClueScrollOverlay.TITLED_CONTENT_COLOR).build());
      panelComponent.getChildren().add(LineComponent.builder().left("Song:").build());
      panelComponent.getChildren().add(LineComponent.builder().left(this.song).leftColor(ClueScrollOverlay.TITLED_CONTENT_COLOR).build());
   }

   public void makeWorldOverlayHint(Graphics2D graphics, ClueScrollPlugin plugin) {
      if (LOCATION.isInScene(plugin.getClient())) {
         Iterator var3 = plugin.getNpcsToMark().iterator();

         while(var3.hasNext()) {
            NPC npc = (NPC)var3.next();
            OverlayUtil.renderActorOverlayImage(graphics, npc, plugin.getClueScrollImage(), Color.ORANGE, 30);
         }

      }
   }

   public static MusicClue forItemId(int itemId) {
      Iterator var1 = CLUES.iterator();

      MusicClue clue;
      do {
         if (!var1.hasNext()) {
            return null;
         }

         clue = (MusicClue)var1.next();
      } while(clue.itemId != itemId);

      return clue;
   }

   public static MusicClue forSong(String song) {
      Iterator var1 = CLUES.iterator();

      MusicClue clue;
      do {
         if (!var1.hasNext()) {
            return null;
         }

         clue = (MusicClue)var1.next();
      } while(!clue.song.equals(song));

      return clue;
   }

   public String[] getNpcs(ClueScrollPlugin plugin) {
      return new String[]{"Cecilia"};
   }

   public static MusicClue forText(String text) {
      Matcher m = SONG_PATTERN.matcher(text);
      if (m.find()) {
         String song = m.group(1);
         MusicClue clue = forSong(song);
         return clue != null ? clue : new MusicClue(-1, song);
      } else {
         return null;
      }
   }

   public WorldPoint getLocation(ClueScrollPlugin plugin) {
      return LOCATION;
   }

   private MusicClue(int itemId, String song) {
      this.itemId = itemId;
      this.song = song;
   }

   public int getItemId() {
      return this.itemId;
   }

   public String getSong() {
      return this.song;
   }
}
