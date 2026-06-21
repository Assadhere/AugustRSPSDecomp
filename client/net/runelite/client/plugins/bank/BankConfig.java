package net.runelite.client.plugins.bank;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.Keybind;

@ConfigGroup("bank")
public interface BankConfig extends Config {
   @ConfigItem(
      keyName = "showGE",
      name = "Show Grand Exchange price",
      description = "Show Grand Exchange price total (GE).",
      position = 1
   )
   default boolean showGE() {
      return true;
   }

   @ConfigItem(
      keyName = "showHA",
      name = "Show high alchemy price",
      description = "Show high alchemy price total (HA).",
      position = 2
   )
   default boolean showHA() {
      return false;
   }

   @ConfigItem(
      keyName = "showExact",
      name = "Show exact bank value",
      description = "Show exact bank value.",
      position = 3
   )
   default boolean showExact() {
      return false;
   }

   @ConfigItem(
      keyName = "rightClickBankInventory",
      name = "Disable left-click bank inventory",
      description = "Configures whether the bank inventory button will bank your inventory on left-click.",
      position = 4
   )
   default boolean rightClickBankInventory() {
      return false;
   }

   @ConfigItem(
      keyName = "rightClickBankEquip",
      name = "Disable left-click bank equipment",
      description = "Configures whether the bank equipment button will bank your equipment on left-click.",
      position = 5
   )
   default boolean rightClickBankEquip() {
      return false;
   }

   @ConfigItem(
      keyName = "rightClickBankLoot",
      name = "Disable left-click bank containers",
      description = "Configures whether the bank containers button will bank your looting bag contents on left-click.",
      position = 6
   )
   default boolean rightClickBankLoot() {
      return false;
   }

   @ConfigItem(
      keyName = "rightClickPlaceholders",
      name = "Disable left-click placeholders button",
      description = "Configures whether the placeholders button will be toggled on left-click.",
      position = 7
   )
   default boolean rightClickPlaceholders() {
      return false;
   }

   @ConfigItem(
      keyName = "seedVaultValue",
      name = "Show seed vault value",
      description = "Adds the total value of all seeds inside the seed vault to the title.",
      position = 8
   )
   default boolean seedVaultValue() {
      return true;
   }

   @ConfigItem(
      keyName = "bankPinKeyboard",
      name = "Keyboard bankpin",
      description = "Allows using the keyboard keys for bank pin input.",
      position = 9
   )
   default boolean bankPinKeyboard() {
      return false;
   }

   @ConfigItem(
      keyName = "searchKeybind",
      name = "Search shortcut",
      description = "Keyboard shortcut for initiating a bank or seed vault search.",
      position = 10
   )
   default Keybind searchKeybind() {
      return new Keybind(70, 128);
   }

   @ConfigItem(
      keyName = "blockJagexAccountAd",
      name = "Block Jagex Account popup",
      description = "Blocks the weekly reminder to migrate to a Jagex account.",
      position = 11
   )
   default boolean blockJagexAccountAd() {
      return false;
   }
}
