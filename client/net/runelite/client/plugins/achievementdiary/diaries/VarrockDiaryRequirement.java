package net.runelite.client.plugins.achievementdiary.diaries;

import net.runelite.api.Quest;
import net.runelite.api.Skill;
import net.runelite.client.plugins.achievementdiary.CombatLevelRequirement;
import net.runelite.client.plugins.achievementdiary.GenericDiaryRequirement;
import net.runelite.client.plugins.achievementdiary.QuestPointRequirement;
import net.runelite.client.plugins.achievementdiary.QuestRequirement;
import net.runelite.client.plugins.achievementdiary.Requirement;
import net.runelite.client.plugins.achievementdiary.SkillRequirement;

public class VarrockDiaryRequirement extends GenericDiaryRequirement {
   public VarrockDiaryRequirement() {
      this.add("Have Aubury teleport you to the Essence mine.", new Requirement[]{new QuestRequirement(Quest.RUNE_MYSTERIES)});
      this.add("Mine some Iron in the south east mining patch near Varrock.", new Requirement[]{new SkillRequirement(Skill.MINING, 15)});
      this.add("Jump over the fence south of Varrock.", new Requirement[]{new SkillRequirement(Skill.AGILITY, 13)});
      this.add("Spin a bowl on the pottery wheel and fire it in the oven in Barb Village.", new Requirement[]{new SkillRequirement(Skill.CRAFTING, 8)});
      this.add("Craft some Earth runes from Essence.", new Requirement[]{new SkillRequirement(Skill.RUNECRAFT, 9)});
      this.add("Catch some trout in the River Lum at Barbarian Village.", new Requirement[]{new SkillRequirement(Skill.FISHING, 20)});
      this.add("Steal from the Tea stall in Varrock.", new Requirement[]{new SkillRequirement(Skill.THIEVING, 5)});
      this.add("Enter the Champions' Guild.", new Requirement[]{new QuestPointRequirement(32)});
      this.add("Select a colour for your kitten.", new Requirement[]{new QuestRequirement(Quest.GARDEN_OF_TRANQUILLITY, true), new QuestRequirement(Quest.GERTRUDES_CAT)});
      this.add("Use the spirit tree north of Varrock.", new Requirement[]{new QuestRequirement(Quest.TREE_GNOME_VILLAGE)});
      this.add("Enter the Tolna dungeon after completing A Soul's Bane.", new Requirement[]{new QuestRequirement(Quest.A_SOULS_BANE)});
      this.add("Teleport to the digsite using a Digsite pendant.", new Requirement[]{new QuestRequirement(Quest.THE_DIG_SITE)});
      this.add("Cast the teleport to Varrock spell.", new Requirement[]{new SkillRequirement(Skill.MAGIC, 25)});
      this.add("Get a Slayer task from Vannaka.", new Requirement[]{new CombatLevelRequirement(40)});
      this.add("Pick a White tree fruit.", new Requirement[]{new SkillRequirement(Skill.FARMING, 25), new QuestRequirement(Quest.GARDEN_OF_TRANQUILLITY)});
      this.add("Use the balloon to travel from Varrock.", new Requirement[]{new SkillRequirement(Skill.FIREMAKING, 40), new QuestRequirement(Quest.ENLIGHTENED_JOURNEY)});
      this.add("Complete a lap of the Varrock Agility course.", new Requirement[]{new SkillRequirement(Skill.AGILITY, 30)});
      this.add("Trade furs with the Fancy Dress Seller for a spottier cape and equip it.", new Requirement[]{new SkillRequirement(Skill.HUNTER, 66)});
      this.add("Make a Waka Canoe near Edgeville.", new Requirement[]{new SkillRequirement(Skill.WOODCUTTING, 57)});
      this.add("Teleport to Paddewwa.", new Requirement[]{new SkillRequirement(Skill.MAGIC, 54), new QuestRequirement(Quest.DESERT_TREASURE_I)});
      this.add("Chop some yew logs in Varrock and burn them at the top of the Varrock church.", new Requirement[]{new SkillRequirement(Skill.WOODCUTTING, 60), new SkillRequirement(Skill.FIREMAKING, 60)});
      this.add("Have the Varrock estate agent decorate your house with Fancy Stone.", new Requirement[]{new SkillRequirement(Skill.CONSTRUCTION, 50)});
      this.add("Collect at least 2 yew roots from the Tree patch in Varrock Palace.", new Requirement[]{new SkillRequirement(Skill.WOODCUTTING, 60), new SkillRequirement(Skill.FARMING, 68)});
      this.add("Pray at the altar in Varrock palace with Smite active.", new Requirement[]{new SkillRequirement(Skill.PRAYER, 52)});
      this.add("Squeeze through the obstacle pipe in Edgeville dungeon.", new Requirement[]{new SkillRequirement(Skill.AGILITY, 51)});
      this.add("Create a super combat potion in Varrock west bank.", new Requirement[]{new SkillRequirement(Skill.HERBLORE, 90), new QuestRequirement(Quest.DRUIDIC_RITUAL)});
      this.add("Use Lunar magic to make 20 mahogany planks at the Lumberyard.", new Requirement[]{new SkillRequirement(Skill.MAGIC, 86), new QuestRequirement(Quest.DREAM_MENTOR)});
      this.add("Bake a summer pie in the Cooking Guild.", new Requirement[]{new SkillRequirement(Skill.COOKING, 95)});
      this.add("Smith and fletch ten rune darts within Varrock.", new Requirement[]{new SkillRequirement(Skill.SMITHING, 89), new SkillRequirement(Skill.FLETCHING, 81), new QuestRequirement(Quest.THE_TOURIST_TRAP)});
      this.add("Craft 100 or more earth runes simultaneously from Essence without the use of Extracts.", new Requirement[]{new SkillRequirement(Skill.RUNECRAFT, 78)});
   }
}
