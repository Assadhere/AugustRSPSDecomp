package net.runelite.client.plugins.implings;

import java.awt.Color;
import net.runelite.client.config.Alpha;
import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.ConfigSection;

@ConfigGroup("implings")
public interface ImplingsConfig extends Config {
   String GROUP = "implings";
   @ConfigSection(
      name = "Impling type settings",
      description = "Configuration for each type of impling.",
      position = 99
   )
   String implingSection = "implings";

   @ConfigItem(
      position = 1,
      keyName = "showbaby",
      name = "Baby implings",
      description = "Configures whether or not baby impling tags are displayed.",
      section = "implings"
   )
   default ImplingMode showBaby() {
      return ImplingsConfig.ImplingMode.NONE;
   }

   @Alpha
   @ConfigItem(
      position = 2,
      keyName = "babyColor",
      name = "Baby impling color",
      description = "Text color for baby implings.",
      section = "implings"
   )
   default Color getBabyColor() {
      return new Color(177, 143, 179);
   }

   @ConfigItem(
      position = 3,
      keyName = "showyoung",
      name = "Young implings",
      description = "Configures whether or not young impling tags are displayed.",
      section = "implings"
   )
   default ImplingMode showYoung() {
      return ImplingsConfig.ImplingMode.NONE;
   }

   @Alpha
   @ConfigItem(
      position = 4,
      keyName = "youngColor",
      name = "Young impling color",
      description = "Text color for young implings.",
      section = "implings"
   )
   default Color getYoungColor() {
      return new Color(175, 164, 136);
   }

   @ConfigItem(
      position = 5,
      keyName = "showgourmet",
      name = "Gourmet implings",
      description = "Configures whether or not gourmet impling tags are displayed.",
      section = "implings"
   )
   default ImplingMode showGourmet() {
      return ImplingsConfig.ImplingMode.NONE;
   }

   @Alpha
   @ConfigItem(
      position = 6,
      keyName = "gourmetColor",
      name = "Gourmet impling color",
      description = "Text color for gourmet implings.",
      section = "implings"
   )
   default Color getGourmetColor() {
      return new Color(169, 131, 98);
   }

   @ConfigItem(
      position = 7,
      keyName = "showearth",
      name = "Earth implings",
      description = "Configures whether or not earth impling tags are displayed.",
      section = "implings"
   )
   default ImplingMode showEarth() {
      return ImplingsConfig.ImplingMode.NONE;
   }

   @Alpha
   @ConfigItem(
      position = 8,
      keyName = "earthColor",
      name = "Earth impling color",
      description = "Text color for earth implings.",
      section = "implings"
   )
   default Color getEarthColor() {
      return new Color(62, 86, 64);
   }

   @ConfigItem(
      position = 9,
      keyName = "showessence",
      name = "Essence implings",
      description = "Configures whether or not essence impling tags are displayed.",
      section = "implings"
   )
   default ImplingMode showEssence() {
      return ImplingsConfig.ImplingMode.NONE;
   }

   @Alpha
   @ConfigItem(
      position = 10,
      keyName = "essenceColor",
      name = "Essence impling color",
      description = "Text color for essence implings.",
      section = "implings"
   )
   default Color getEssenceColor() {
      return new Color(32, 89, 90);
   }

   @ConfigItem(
      position = 11,
      keyName = "showeclectic",
      name = "Eclectic implings",
      description = "Configures whether or not eclectic impling tags are displayed.",
      section = "implings"
   )
   default ImplingMode showEclectic() {
      return ImplingsConfig.ImplingMode.NONE;
   }

   @Alpha
   @ConfigItem(
      position = 12,
      keyName = "eclecticColor",
      name = "Eclectic impling color",
      description = "Text color for eclectic implings.",
      section = "implings"
   )
   default Color getEclecticColor() {
      return new Color(145, 155, 69);
   }

   @ConfigItem(
      position = 13,
      keyName = "shownature",
      name = "Nature implings",
      description = "Configures whether or not nature impling tags are displayed.",
      section = "implings"
   )
   default ImplingMode showNature() {
      return ImplingsConfig.ImplingMode.NONE;
   }

   @Alpha
   @ConfigItem(
      position = 14,
      keyName = "natureColor",
      name = "Nature impling color",
      description = "Text color for nature implings.",
      section = "implings"
   )
   default Color getNatureColor() {
      return new Color(92, 138, 95);
   }

   @ConfigItem(
      position = 15,
      keyName = "showmagpie",
      name = "Magpie implings",
      description = "Configures whether or not magpie impling tags are displayed.",
      section = "implings"
   )
   default ImplingMode showMagpie() {
      return ImplingsConfig.ImplingMode.NONE;
   }

   @Alpha
   @ConfigItem(
      position = 16,
      keyName = "magpieColor",
      name = "Magpie impling color",
      description = "Text color for magpie implings.",
      section = "implings"
   )
   default Color getMagpieColor() {
      return new Color(142, 142, 19);
   }

   @ConfigItem(
      position = 17,
      keyName = "showninja",
      name = "Ninja implings",
      description = "Configures whether or not ninja impling tags are displayed.",
      section = "implings"
   )
   default ImplingMode showNinja() {
      return ImplingsConfig.ImplingMode.NONE;
   }

   @Alpha
   @ConfigItem(
      position = 18,
      keyName = "ninjaColor",
      name = "Ninja impling color",
      description = "Text color for ninja implings.",
      section = "implings"
   )
   default Color getNinjaColor() {
      return new Color(71, 70, 75);
   }

   @ConfigItem(
      position = 19,
      keyName = "showCrystal",
      name = "Crystal implings",
      description = "Configures whether or not crystal implings are displayed.",
      section = "implings"
   )
   default ImplingMode showCrystal() {
      return ImplingsConfig.ImplingMode.NONE;
   }

   @Alpha
   @ConfigItem(
      position = 20,
      keyName = "crystalColor",
      name = "Crystal impling color",
      description = "Text color for crystal implings.",
      section = "implings"
   )
   default Color getCrystalColor() {
      return new Color(93, 188, 210);
   }

   @ConfigItem(
      position = 21,
      keyName = "showdragon",
      name = "Dragon implings",
      description = "Configures whether or not dragon impling tags are displayed.",
      section = "implings"
   )
   default ImplingMode showDragon() {
      return ImplingsConfig.ImplingMode.HIGHLIGHT;
   }

   @Alpha
   @ConfigItem(
      position = 22,
      keyName = "dragonColor",
      name = "Dragon impling color",
      description = "Text color for dragon implings.",
      section = "implings"
   )
   default Color getDragonColor() {
      return new Color(210, 85, 75);
   }

   @ConfigItem(
      position = 23,
      keyName = "showlucky",
      name = "Lucky implings",
      description = "Configures whether or not lucky impling tags are displayed.",
      section = "implings"
   )
   default ImplingMode showLucky() {
      return ImplingsConfig.ImplingMode.HIGHLIGHT;
   }

   @Alpha
   @ConfigItem(
      position = 24,
      keyName = "luckyColor",
      name = "Lucky impling color",
      description = "Text color for lucky implings.",
      section = "implings"
   )
   default Color getLuckyColor() {
      return new Color(102, 7, 101);
   }

   @ConfigItem(
      position = 25,
      keyName = "showspawn",
      name = "Show spawn locations",
      description = "Configures whether or not spawn locations are displayed in Puro-Puro."
   )
   default boolean showSpawn() {
      return false;
   }

   @Alpha
   @ConfigItem(
      position = 26,
      keyName = "spawnColor",
      name = "Impling spawn color",
      description = "Text color for impling spawns in Puro-Puro."
   )
   default Color getSpawnColor() {
      return Color.WHITE;
   }

   @ConfigItem(
      position = 27,
      keyName = "showname",
      name = "Show name on minimap",
      description = "Configures whether or not impling names are displayed on minimap."
   )
   default boolean showName() {
      return false;
   }

   public static enum ImplingMode {
      NONE,
      HIGHLIGHT,
      NOTIFY;
   }
}
