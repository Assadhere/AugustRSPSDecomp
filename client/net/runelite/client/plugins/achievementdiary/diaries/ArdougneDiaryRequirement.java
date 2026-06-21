package net.runelite.client.plugins.achievementdiary.diaries;

import net.runelite.api.Quest;
import net.runelite.api.Skill;
import net.runelite.client.plugins.achievementdiary.GenericDiaryRequirement;
import net.runelite.client.plugins.achievementdiary.QuestRequirement;
import net.runelite.client.plugins.achievementdiary.Requirement;
import net.runelite.client.plugins.achievementdiary.SkillRequirement;

public class ArdougneDiaryRequirement extends GenericDiaryRequirement {
   public ArdougneDiaryRequirement() {
      this.add("Have Wizard Cromperty teleport you to the Rune Essence mine.", new Requirement[]{new QuestRequirement(Quest.RUNE_MYSTERIES)});
      this.add("Steal a cake from the Ardougne market stalls.", new Requirement[]{new SkillRequirement(Skill.THIEVING, 5)});
      this.add("Enter the Combat Training Camp north of W. Ardougne.", new Requirement[]{new QuestRequirement(Quest.BIOHAZARD)});
      this.add("Enter the Unicorn pen in Ardougne zoo using Fairy rings.", new Requirement[]{new QuestRequirement(Quest.FAIRYTALE_II__CURE_A_QUEEN, true)});
      this.add("Grapple over Yanille's south wall.", new Requirement[]{new SkillRequirement(Skill.AGILITY, 39), new SkillRequirement(Skill.STRENGTH, 38), new SkillRequirement(Skill.RANGED, 21)});
      this.add("Harvest some strawberries from the Ardougne farming patch.", new Requirement[]{new SkillRequirement(Skill.FARMING, 31)});
      this.add("Cast the Ardougne Teleport spell.", new Requirement[]{new SkillRequirement(Skill.MAGIC, 51), new QuestRequirement(Quest.PLAGUE_CITY)});
      this.add("Travel to Castlewars by Hot Air Balloon.", new Requirement[]{new SkillRequirement(Skill.FIREMAKING, 50), new QuestRequirement(Quest.ENLIGHTENED_JOURNEY)});
      this.add("Claim buckets of sand from Bert in Yanille.", new Requirement[]{new SkillRequirement(Skill.CRAFTING, 49), new QuestRequirement(Quest.THE_HAND_IN_THE_SAND)});
      this.add("Catch any fish on the Fishing Platform.", new Requirement[]{new QuestRequirement(Quest.SEA_SLUG, true)});
      this.add("Pickpocket the master farmer north of Ardougne.", new Requirement[]{new SkillRequirement(Skill.THIEVING, 38)});
      this.add("Collect some Nightshade from the Skavid Caves.", new Requirement[]{new QuestRequirement(Quest.WATCHTOWER, true)});
      this.add("Kill a swordchick in the Tower of Life.", new Requirement[]{new QuestRequirement(Quest.TOWER_OF_LIFE)});
      this.add("Equip Iban's upgraded staff or upgrade an Iban staff.", new Requirement[]{new SkillRequirement(Skill.MAGIC, 50), new SkillRequirement(Skill.ATTACK, 50), new QuestRequirement(Quest.UNDERGROUND_PASS)});
      this.add("Visit the Island East of the Necromancer's tower.", new Requirement[]{new QuestRequirement(Quest.FAIRYTALE_II__CURE_A_QUEEN, true)});
      this.add("Recharge some Jewellery at the Totem in the Legends Guild.", new Requirement[]{new QuestRequirement(Quest.LEGENDS_QUEST)});
      this.add("Recharge some Jewellery at Totem in the Legends Guild.", new Requirement[]{new QuestRequirement(Quest.LEGENDS_QUEST)});
      this.add("Enter the Magic Guild.", new Requirement[]{new SkillRequirement(Skill.MAGIC, 66)});
      this.add("Steal from a chest in Ardougne Castle.", new Requirement[]{new SkillRequirement(Skill.THIEVING, 72)});
      this.add("Have a zookeeper put you in Ardougne Zoo's monkey cage.", new Requirement[]{new QuestRequirement(Quest.MONKEY_MADNESS_I, true)});
      this.add("Teleport to the Watchtower.", new Requirement[]{new SkillRequirement(Skill.MAGIC, 58), new QuestRequirement(Quest.WATCHTOWER)});
      this.add("Catch a Red Salamander.", new Requirement[]{new SkillRequirement(Skill.HUNTER, 59)});
      this.add("Check the health of a Palm tree near tree gnome village.", new Requirement[]{new SkillRequirement(Skill.FARMING, 68)});
      this.add("Pick some Poison Ivy berries from the patch south of Ardougne.", new Requirement[]{new SkillRequirement(Skill.FARMING, 70)});
      this.add("Smith a Mithril platebody near Ardougne.", new Requirement[]{new SkillRequirement(Skill.SMITHING, 68)});
      this.add("Enter your POH from Yanille.", new Requirement[]{new SkillRequirement(Skill.CONSTRUCTION, 50)});
      this.add("Smith a Dragon sq shield in West Ardougne.", new Requirement[]{new SkillRequirement(Skill.SMITHING, 60), new QuestRequirement(Quest.LEGENDS_QUEST)});
      this.add("Craft some Death runes from Essence.", new Requirement[]{new SkillRequirement(Skill.RUNECRAFT, 65), new QuestRequirement(Quest.MOURNINGS_END_PART_II)});
      this.add("Catch a Manta ray in the Fishing Trawler and cook it in Port Khazard.", new Requirement[]{new SkillRequirement(Skill.FISHING, 81), new SkillRequirement(Skill.COOKING, 91)});
      this.add("Picklock the door to the basement of Yanille Agility Dungeon.", new Requirement[]{new SkillRequirement(Skill.THIEVING, 82)});
      this.add("Pickpocket a Hero.", new Requirement[]{new SkillRequirement(Skill.THIEVING, 80)});
      this.add("Make a rune crossbow yourself from scratch within Witchaven or Yanille.", new Requirement[]{new SkillRequirement(Skill.CRAFTING, 10), new SkillRequirement(Skill.SMITHING, 91), new SkillRequirement(Skill.FLETCHING, 69)});
      this.add("Imbue a Salve amulet at Nightmare Zone, or equip a Salve amulet that was imbued there.", new Requirement[]{new QuestRequirement(Quest.HAUNTED_MINE)});
      this.add("Pick some Torstol from the patch north of Ardougne.", new Requirement[]{new SkillRequirement(Skill.FARMING, 85)});
      this.add("Complete a lap of Ardougne's rooftop agility course.", new Requirement[]{new SkillRequirement(Skill.AGILITY, 90)});
      this.add("Cast Ice Barrage on another player within Castle Wars.", new Requirement[]{new SkillRequirement(Skill.MAGIC, 94), new QuestRequirement(Quest.DESERT_TREASURE_I)});
   }
}
