package net.runelite.client.plugins.achievementdiary;

import com.google.common.collect.ImmutableList;
import java.util.List;

class DiaryRequirement {
   private final String task;
   private final List<Requirement> requirements;

   DiaryRequirement(String task, Requirement[] requirements) {
      this.task = task;
      this.requirements = ImmutableList.copyOf(requirements);
   }

   public String getTask() {
      return this.task;
   }

   public List<Requirement> getRequirements() {
      return this.requirements;
   }
}
