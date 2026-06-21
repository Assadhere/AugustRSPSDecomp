package net.runelite.client.plugins.randomevents;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.ConfigSection;
import net.runelite.client.config.Notification;

@ConfigGroup("randomevents")
public interface RandomEventConfig extends Config {
   @ConfigSection(
      name = "Notification settings",
      description = "Choose which random events will trigger notifications when spawned.",
      position = 99
   )
   String notificationSection = "section";

   @ConfigItem(
      keyName = "removeMenuOptions",
      name = "Remove others' menu options",
      description = "Remove menu options from random events for other players.",
      position = -3
   )
   default boolean removeMenuOptions() {
      return true;
   }

   @ConfigItem(
      keyName = "notifyAll",
      name = "Notify for all events",
      description = "",
      position = -2,
      section = "section"
   )
   default Notification notifyAllEvents() {
      return Notification.OFF;
   }

   @ConfigItem(
      keyName = "notifyArnav",
      name = "Notify on Capt' Arnav's chest",
      description = "",
      section = "section"
   )
   default Notification notifyArnav() {
      return Notification.OFF;
   }

   @ConfigItem(
      keyName = "notifyBeekeeper",
      name = "Notify on Beekeeper",
      description = "",
      section = "section"
   )
   default Notification notifyBeekeeper() {
      return Notification.OFF;
   }

   @ConfigItem(
      keyName = "notifyBob",
      name = "Notify on Evil Bob",
      description = "",
      section = "section"
   )
   default Notification notifyBob() {
      return Notification.OFF;
   }

   @ConfigItem(
      keyName = "notifyCerters",
      name = "Notify on certers",
      description = "",
      section = "section"
   )
   default Notification notifyCerters() {
      return Notification.OFF;
   }

   @ConfigItem(
      keyName = "notifyDemon",
      name = "Notify on Drill Demon",
      description = "",
      section = "section"
   )
   default Notification notifyDemon() {
      return Notification.OFF;
   }

   @ConfigItem(
      keyName = "notifyDunce",
      name = "Notify on surprise exam",
      description = "",
      section = "section"
   )
   default Notification notifyDunce() {
      return Notification.OFF;
   }

   @ConfigItem(
      keyName = "notifyDwarf",
      name = "Notify on Drunken Dwarf",
      description = "",
      section = "section"
   )
   default Notification notifyDwarf() {
      return Notification.OFF;
   }

   @ConfigItem(
      keyName = "notifyForester",
      name = "Notify on Freaky Forester",
      description = "",
      section = "section"
   )
   default Notification notifyForester() {
      return Notification.OFF;
   }

   @ConfigItem(
      keyName = "notifyFlippa",
      name = "Notify on pinball",
      description = "",
      section = "section"
   )
   default Notification notifyFlippa() {
      return Notification.OFF;
   }

   @ConfigItem(
      keyName = "notifyFrog",
      name = "Notify on kiss the frog",
      description = "",
      section = "section"
   )
   default Notification notifyFrog() {
      return Notification.OFF;
   }

   @ConfigItem(
      keyName = "notifyGenie",
      name = "Notify on Genie",
      description = "",
      section = "section"
   )
   default Notification notifyGenie() {
      return Notification.OFF;
   }

   @ConfigItem(
      keyName = "notifyGravedigger",
      name = "Notify on Gravedigger",
      description = "",
      section = "section"
   )
   default Notification notifyGravedigger() {
      return Notification.OFF;
   }

   @ConfigItem(
      keyName = "notifyJekyll",
      name = "Notify on Jekyll & Hyde",
      description = "",
      section = "section"
   )
   default Notification notifyJekyll() {
      return Notification.OFF;
   }

   @ConfigItem(
      keyName = "notifyMaze",
      name = "Notify on maze",
      description = "",
      section = "section"
   )
   default Notification notifyMaze() {
      return Notification.OFF;
   }

   @ConfigItem(
      keyName = "notifyMime",
      name = "Notify on mime",
      description = "",
      section = "section"
   )
   default Notification notifyMime() {
      return Notification.OFF;
   }

   @ConfigItem(
      keyName = "notifyMoM",
      name = "Notify on Mysterious Old Man",
      description = "",
      section = "section"
   )
   default Notification notifyMoM() {
      return Notification.OFF;
   }

   @ConfigItem(
      keyName = "notifyPillory",
      name = "Notify on pillory",
      description = "",
      section = "section"
   )
   default Notification notifyPillory() {
      return Notification.OFF;
   }

   @ConfigItem(
      keyName = "notifyPrison",
      name = "Notify on Prison Pete",
      description = "",
      section = "section"
   )
   default Notification notifyPrison() {
      return Notification.OFF;
   }

   @ConfigItem(
      keyName = "notifyQuiz",
      name = "Notify on Quiz Master",
      description = "",
      section = "section"
   )
   default Notification notifyQuiz() {
      return Notification.OFF;
   }

   @ConfigItem(
      keyName = "notifySandwich",
      name = "Notify on Sandwich Lady",
      description = "",
      section = "section"
   )
   default Notification notifySandwich() {
      return Notification.OFF;
   }

   @ConfigItem(
      keyName = "notifyTurpentine",
      name = "Notify on Rick Turpentine",
      description = "",
      section = "section"
   )
   default Notification notifyTurpentine() {
      return Notification.OFF;
   }

   @ConfigItem(
      keyName = "notifyTwin",
      name = "Notify on evil twin",
      description = "",
      section = "section"
   )
   default Notification notifyTwin() {
      return Notification.OFF;
   }

   @ConfigItem(
      keyName = "notifyCountCheck",
      name = "Notify on Count Check",
      description = "",
      section = "section"
   )
   default Notification notifyCountCheck() {
      return Notification.OFF;
   }
}
