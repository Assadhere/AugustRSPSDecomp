package net.runelite.client.plugins.achievementdiary;

import net.runelite.api.Client;
import net.runelite.api.Quest;
import net.runelite.api.QuestState;

public class QuestRequirement implements Requirement {
   private final Quest quest;
   private final boolean started;

   public QuestRequirement(Quest quest) {
      this(quest, false);
   }

   public String toString() {
      return this.started ? "Started " + this.quest.getName() : this.quest.getName();
   }

   public boolean satisfiesRequirement(Client client) {
      QuestState questState = this.quest.getState(client);
      if (this.started) {
         return questState != QuestState.NOT_STARTED;
      } else {
         return questState == QuestState.FINISHED;
      }
   }

   public Quest getQuest() {
      return this.quest;
   }

   public boolean isStarted() {
      return this.started;
   }

   public QuestRequirement(Quest quest, boolean started) {
      this.quest = quest;
      this.started = started;
   }
}
