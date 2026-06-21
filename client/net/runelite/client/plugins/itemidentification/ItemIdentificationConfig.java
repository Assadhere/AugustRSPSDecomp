package net.runelite.client.plugins.itemidentification;

import java.awt.Color;
import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.ConfigSection;

@ConfigGroup("itemidentification")
public interface ItemIdentificationConfig extends Config {
   @ConfigSection(
      name = "Categories",
      description = "The categories of items to identify.",
      position = 99
   )
   String identificationSection = "identification";

   @ConfigItem(
      keyName = "identificationType",
      name = "Identification type",
      position = -4,
      description = "How much to show of the item name."
   )
   default ItemIdentificationMode identificationType() {
      return ItemIdentificationMode.SHORT;
   }

   @ConfigItem(
      keyName = "textColor",
      name = "Color",
      position = -3,
      description = "The color of the identification text."
   )
   default Color textColor() {
      return Color.WHITE;
   }

   @ConfigItem(
      keyName = "showHerbSeeds",
      name = "Seeds (herb)",
      description = "Show identification on herb seeds.",
      section = "identification"
   )
   default boolean showHerbSeeds() {
      return true;
   }

   @ConfigItem(
      keyName = "showAllotmentSeeds",
      name = "Seeds (allotment)",
      description = "Show identification on allotment seeds.",
      section = "identification"
   )
   default boolean showAllotmentSeeds() {
      return false;
   }

   @ConfigItem(
      keyName = "showFlowerSeeds",
      name = "Seeds (flower)",
      description = "Show identification on flower seeds.",
      section = "identification"
   )
   default boolean showFlowerSeeds() {
      return false;
   }

   @ConfigItem(
      keyName = "showFruitTreeSeeds",
      name = "Seeds (fruit tree)",
      description = "Show identification on fruit tree seeds.",
      section = "identification"
   )
   default boolean showFruitTreeSeeds() {
      return false;
   }

   @ConfigItem(
      keyName = "showTreeSeeds",
      name = "Seeds (tree)",
      description = "Show identification on tree seeds.",
      section = "identification"
   )
   default boolean showTreeSeeds() {
      return false;
   }

   @ConfigItem(
      keyName = "showSpecialSeeds",
      name = "Seeds (special)",
      description = "Show identification on special seeds.",
      section = "identification"
   )
   default boolean showSpecialSeeds() {
      return false;
   }

   @ConfigItem(
      keyName = "showBerrySeeds",
      name = "Seeds (berry)",
      description = "Show identification on berry seeds.",
      section = "identification"
   )
   default boolean showBerrySeeds() {
      return false;
   }

   @ConfigItem(
      keyName = "showHopSeeds",
      name = "Seeds (hops)",
      description = "Show identification on hops seeds.",
      section = "identification"
   )
   default boolean showHopsSeeds() {
      return false;
   }

   @ConfigItem(
      keyName = "showCoralFrags",
      name = "Coral Frags",
      description = "Show identification on coral frags.",
      section = "identification"
   )
   default boolean showCoralFrags() {
      return false;
   }

   @ConfigItem(
      keyName = "showSacks",
      name = "Sacks",
      description = "Show identification on sacks.",
      section = "identification"
   )
   default boolean showSacks() {
      return false;
   }

   @ConfigItem(
      keyName = "showHerbs",
      name = "Herbs",
      description = "Show identification on herbs.",
      section = "identification"
   )
   default boolean showHerbs() {
      return false;
   }

   @ConfigItem(
      keyName = "showLogs",
      name = "Logs",
      description = "Show identification on logs.",
      section = "identification"
   )
   default boolean showLogs() {
      return false;
   }

   @ConfigItem(
      keyName = "showPyreLogs",
      name = "Logs (pyre)",
      description = "Show identification on pyre logs.",
      section = "identification"
   )
   default boolean showPyreLogs() {
      return false;
   }

   @ConfigItem(
      keyName = "showPlanks",
      name = "Planks",
      description = "Show identification on planks.",
      section = "identification"
   )
   default boolean showPlanks() {
      return false;
   }

   @ConfigItem(
      keyName = "showRepairKits",
      name = "Repair Kits",
      description = "Show identification on repair kits.",
      section = "identification"
   )
   default boolean showRepairKits() {
      return false;
   }

   @ConfigItem(
      keyName = "showYarn",
      name = "Yarn",
      description = "Show identification on yarns.",
      section = "identification"
   )
   default boolean showYarn() {
      return false;
   }

   @ConfigItem(
      keyName = "showCloth",
      name = "Cloth",
      description = "Show identification on bolts of cloth.",
      section = "identification"
   )
   default boolean showCloth() {
      return false;
   }

   @ConfigItem(
      keyName = "showSaplings",
      name = "Saplings",
      description = "Show identification on saplings and seedlings.",
      section = "identification"
   )
   default boolean showSaplings() {
      return true;
   }

   @ConfigItem(
      keyName = "showComposts",
      name = "Composts",
      description = "Show identification on composts.",
      section = "identification"
   )
   default boolean showComposts() {
      return false;
   }

   @ConfigItem(
      keyName = "showOres",
      name = "Ores",
      description = "Show identification on ores.",
      section = "identification"
   )
   default boolean showOres() {
      return false;
   }

   @ConfigItem(
      keyName = "showBars",
      name = "Bars",
      description = "Show identification on bars.",
      section = "identification"
   )
   default boolean showBars() {
      return false;
   }

   @ConfigItem(
      keyName = "showGems",
      name = "Gems",
      description = "Show identification on gems.",
      section = "identification"
   )
   default boolean showGems() {
      return false;
   }

   @ConfigItem(
      keyName = "showPotions",
      name = "Potions",
      description = "Show identification on potions.",
      section = "identification"
   )
   default boolean showPotions() {
      return false;
   }

   @ConfigItem(
      keyName = "showButterflyMothJars",
      name = "Butterfly & Moth jars",
      description = "Show identification on Butterfly and Moth jars",
      section = "identification"
   )
   default boolean showButterflyMothJars() {
      return false;
   }

   @ConfigItem(
      keyName = "showImplingJars",
      name = "Impling jars",
      description = "Show identification on impling jars.",
      section = "identification"
   )
   default boolean showImplingJars() {
      return false;
   }

   @ConfigItem(
      keyName = "showTablets",
      name = "Tablets",
      description = "Show identification on tablets.",
      section = "identification"
   )
   default boolean showTablets() {
      return false;
   }

   @ConfigItem(
      keyName = "showTeleportScrolls",
      name = "Teleport scrolls",
      description = "Show identification on teleport scrolls.",
      section = "identification"
   )
   default boolean showTeleportScrolls() {
      return false;
   }

   @ConfigItem(
      keyName = "showJewellery",
      name = "Jewellery (unenchanted)",
      description = "Show identification on unenchanted jewellery.",
      section = "identification"
   )
   default boolean showJewellery() {
      return false;
   }

   @ConfigItem(
      keyName = "showEnchantedJewellery",
      name = "Jewellery (enchanted)",
      description = "Show identification on enchanted jewellery.",
      section = "identification"
   )
   default boolean showEnchantedJewellery() {
      return false;
   }

   @ConfigItem(
      keyName = "showWines",
      name = "Wines",
      description = "Show identification on jugs of wine.",
      section = "identification"
   )
   default boolean showWines() {
      return false;
   }
}
