package net.runelite.client.plugins.achievementdiary;

import java.util.HashSet;
import java.util.Set;

public abstract class GenericDiaryRequirement {
   private final Set<DiaryRequirement> requirements = new HashSet();

   protected void add(String task, Requirement... requirements) {
      DiaryRequirement diaryRequirement = new DiaryRequirement(task, requirements);
      this.requirements.add(diaryRequirement);
   }

   public Set<DiaryRequirement> getRequirements() {
      return this.requirements;
   }
}
