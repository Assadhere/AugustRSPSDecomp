package net.runelite.client.plugins.cluescrolls.clues;

import com.google.common.collect.ImmutableList;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.Nonnull;
import net.runelite.api.Item;
import net.runelite.api.NPC;
import net.runelite.api.coords.WorldPoint;
import net.runelite.client.plugins.cluescrolls.ClueScrollOverlay;
import net.runelite.client.plugins.cluescrolls.ClueScrollPlugin;
import net.runelite.client.plugins.cluescrolls.clues.item.AnyRequirementCollection;
import net.runelite.client.plugins.cluescrolls.clues.item.ItemRequirement;
import net.runelite.client.plugins.cluescrolls.clues.item.RangeItemRequirement;
import net.runelite.client.plugins.cluescrolls.clues.item.SingleItemRequirement;
import net.runelite.client.ui.FontManager;
import net.runelite.client.ui.overlay.OverlayUtil;
import net.runelite.client.ui.overlay.components.LineComponent;
import net.runelite.client.ui.overlay.components.PanelComponent;
import net.runelite.client.ui.overlay.components.TitleComponent;

public class FaloTheBardClue extends ClueScroll implements NpcClueScroll {
   private static final Pattern CLUE_TEXT_PREFIX = Pattern.compile("^(?:Okay, )?here goes\\.\\.\\. ", 2);
   static final List<FaloTheBardClue> CLUES;
   private static final WorldPoint LOCATION;
   private static final String FALO_THE_BARD = "Falo the Bard";
   private final String text;
   @Nonnull
   private final ItemRequirement[] itemRequirements;

   private static SingleItemRequirement item(int itemId) {
      return new SingleItemRequirement(itemId);
   }

   private static AnyRequirementCollection any(String name, ItemRequirement... requirements) {
      return new AnyRequirementCollection(name, requirements);
   }

   private static RangeItemRequirement range(int startItemId, int endItemId) {
      return range((String)null, startItemId, endItemId);
   }

   private static RangeItemRequirement range(String name, int startItemId, int endItemId) {
      return new RangeItemRequirement(name, startItemId, endItemId);
   }

   private FaloTheBardClue(String text, @Nonnull ItemRequirement... itemRequirements) {
      this.text = text;
      this.itemRequirements = itemRequirements;
   }

   public void makeOverlayHint(PanelComponent panelComponent, ClueScrollPlugin plugin) {
      panelComponent.getChildren().add(TitleComponent.builder().text("Falo the Bard Clue").build());
      panelComponent.getChildren().add(LineComponent.builder().left("NPC:").build());
      panelComponent.getChildren().add(LineComponent.builder().left("Falo the Bard").leftColor(ClueScrollOverlay.TITLED_CONTENT_COLOR).build());
      panelComponent.getChildren().add(LineComponent.builder().left("Item:").build());
      Item[] inventory = plugin.getInventoryItems();
      if (inventory == null) {
         inventory = new Item[0];
      }

      ItemRequirement[] var4 = this.itemRequirements;
      int var5 = var4.length;

      for(int var6 = 0; var6 < var5; ++var6) {
         ItemRequirement requirement = var4[var6];
         boolean inventoryFulfilled = requirement.fulfilledBy(inventory);
         panelComponent.getChildren().add(LineComponent.builder().left(requirement.getCollectiveName(plugin.getClient())).leftColor(ClueScrollOverlay.TITLED_CONTENT_COLOR).right(inventoryFulfilled ? "✓" : "✗").rightFont(FontManager.getDefaultFont()).rightColor(inventoryFulfilled ? Color.GREEN : Color.RED).build());
      }

      this.renderOverlayNote(panelComponent, plugin);
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

   public String[] getNpcs(ClueScrollPlugin plugin) {
      return new String[]{"Falo the Bard"};
   }

   public int[] getConfigKeys() {
      return new int[]{this.text.hashCode()};
   }

   public static FaloTheBardClue forText(String text) {
      Iterator var1 = CLUES.iterator();

      FaloTheBardClue clue;
      do {
         if (!var1.hasNext()) {
            return null;
         }

         clue = (FaloTheBardClue)var1.next();
      } while(!clue.text.equalsIgnoreCase(text));

      return clue;
   }

   public static FaloTheBardClue forChatboxText(String sender, String dialogText) {
      if (!"Falo the Bard".equals(sender)) {
         return null;
      } else {
         Matcher matcher = CLUE_TEXT_PREFIX.matcher(dialogText);
         return !matcher.find() ? null : forText(dialogText.substring(matcher.end()));
      }
   }

   public String getText() {
      return this.text;
   }

   @Nonnull
   public ItemRequirement[] getItemRequirements() {
      return this.itemRequirements;
   }

   static {
      CLUES = ImmutableList.of(new FaloTheBardClue("A blood red weapon, a strong curved sword, found on the island of primate lords.", new ItemRequirement[]{any("Dragon scimitar", item(4587), item(20000))}), new FaloTheBardClue("A book that preaches of some great figure, lending strength, might and vigour.", new ItemRequirement[]{any("Any completed god book", item(3840), item(3844), item(3842), item(12610), item(12608), item(12612), item(26496), item(26488), item(26498), item(26492), item(26494), item(26490))}), new FaloTheBardClue("A bow of elven craft was made, it shimmers bright, but will soon fade.", new ItemRequirement[]{any("Crystal Bow", EmoteClue.ACTIVE_CRYSTAL_BOW_OR_BOW_OF_FAERDHINEN)}), new FaloTheBardClue("A fiery axe of great inferno, when you use it, you'll wonder where the logs go.", new ItemRequirement[]{any("Infernal axe", item(13241), item(13242), item(25066), item(25371), item(30347), item(30348))}), new FaloTheBardClue("A mark used to increase one's grace, found atop a seer's place.", new ItemRequirement[]{item(11849)}), new FaloTheBardClue("A molten beast with fiery breath, you acquire these with its death.", new ItemRequirement[]{item(11943)}), new FaloTheBardClue("A shiny helmet of flight, to obtain this with melee, struggle you might.", new ItemRequirement[]{item(11826)}), new FaloTheBardClue("A sword held in the other hand, red its colour, Cyclops strength you must withstand.", new ItemRequirement[]{any("Dragon or Avernic Defender", item(12954), item(19722), item(24143), item(27008), item(22322), item(24186), item(27550), item(27551), item(27552), item(27553))}), new FaloTheBardClue("A token used to kill mythical beasts, in hopes of a blade or just for an xp feast.", new ItemRequirement[]{item(8851)}), new FaloTheBardClue("Green is my favourite, mature ale I do love, this takes your herblore above.", new ItemRequirement[]{item(5743)}), new FaloTheBardClue("It can hold down a boat or crush a goat, this object, you see, is quite heavy.", new ItemRequirement[]{any("Barrelchest anchor", item(10887), item(27855))}), new FaloTheBardClue("It comes from the ground, underneath the snowy plain. Trolls aplenty, with what looks like a mane.", new ItemRequirement[]{item(22603)}), new FaloTheBardClue[]{new FaloTheBardClue("No attack to wield, only strength is required, made of obsidian, but with no room for a shield.", new ItemRequirement[]{any("Tzhaar-ket-om", item(6528), item(23235))}), new FaloTheBardClue("Penance healers runners and more, obtaining this body often gives much deplore.", new ItemRequirement[]{any("Fighter Torso", item(10551), item(24175))}), new FaloTheBardClue("Strangely found in a chest, many believe these gloves are the best.", new ItemRequirement[]{item(7462)}), new FaloTheBardClue("These gloves of white won't help you fight, but aid in cooking, they just might.", new ItemRequirement[]{item(775)}), new FaloTheBardClue("They come from some time ago, from a land unto the east. Fossilised they have become, this small and gentle beast.", new ItemRequirement[]{item(21555)}), new FaloTheBardClue("To slay a dragon you must first do, before this chest piece can be put on you.", new ItemRequirement[]{item(1127)}), new FaloTheBardClue("Vampyres are agile opponents, damaged best with a weapon of many components.", new ItemRequirement[]{any("Rod of Ivandis or Ivandis/Blisterwood flail", range(7639, 7648), item(22398), item(24699))}), new FaloTheBardClue("You won't bring me to heel, unless you have a bright red keel.", new ItemRequirement[]{item(32017)})});
      LOCATION = new WorldPoint(2689, 3550, 0);
   }
}
