package net.runelite.client.plugins.poh;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("poh")
public interface PohConfig extends Config {
   @ConfigItem(
      keyName = "showPortals",
      name = "Show portals",
      description = "Configures whether to display teleport portals."
   )
   default boolean showPortals() {
      return true;
   }

   @ConfigItem(
      keyName = "showAltar",
      name = "Show altar",
      description = "Configures whether or not the altar is displayed."
   )
   default boolean showAltar() {
      return true;
   }

   @ConfigItem(
      keyName = "showGlory",
      name = "Show glory mount",
      description = "Configures whether or not the mounted glory is displayed."
   )
   default boolean showGlory() {
      return true;
   }

   @ConfigItem(
      keyName = "showPools",
      name = "Show pools",
      description = "Configures whether or not the pools are displayed."
   )
   default boolean showPools() {
      return true;
   }

   @ConfigItem(
      keyName = "showRepairStand",
      name = "Show repair stand",
      description = "Configures whether or not the repair stand is displayed."
   )
   default boolean showRepairStand() {
      return true;
   }

   @ConfigItem(
      keyName = "showExitPortal",
      name = "Show exit portal",
      description = "Configures whether or not the exit portal is displayed."
   )
   default boolean showExitPortal() {
      return true;
   }

   @ConfigItem(
      keyName = "showBurner",
      name = "Show incense burner timers",
      description = "Configures whether or not unlit/lit burners are displayed."
   )
   default boolean showBurner() {
      return true;
   }

   @ConfigItem(
      keyName = "showSpellbook",
      name = "Show spellbook altar",
      description = "Configures whether or not the spellbook altar is displayed."
   )
   default boolean showSpellbook() {
      return true;
   }

   @ConfigItem(
      keyName = "showJewelleryBox",
      name = "Show jewellery box",
      description = "Configures whether or not the jewellery box is displayed."
   )
   default boolean showJewelleryBox() {
      return true;
   }

   @ConfigItem(
      keyName = "showMagicTravel",
      name = "Show fairy/ spirit tree/ obelisk",
      description = "Configures whether or not the fairy ring, spirit tree or obelisk is displayed."
   )
   default boolean showMagicTravel() {
      return true;
   }

   @ConfigItem(
      keyName = "showPortalNexus",
      name = "Show portal nexus",
      description = "Configures whether or not the portal nexus is displayed."
   )
   default boolean showPortalNexus() {
      return true;
   }

   @ConfigItem(
      keyName = "showDigsitePendant",
      name = "Show digsite pendant",
      description = "Configures whether or not the digsite pendant is displayed."
   )
   default boolean showDigsitePendant() {
      return true;
   }

   @ConfigItem(
      keyName = "showXericsTalisman",
      name = "Show Xeric's talisman",
      description = "Configures whether or not the Xeric's talisman is displayed."
   )
   default boolean showXericsTalisman() {
      return true;
   }

   @ConfigItem(
      keyName = "showMythicalCape",
      name = "Show mythical cape",
      description = "Configures whether or not the mythical cape is displayed."
   )
   default boolean showMythicalCape() {
      return true;
   }
}
