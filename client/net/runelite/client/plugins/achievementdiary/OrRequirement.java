package net.runelite.client.plugins.achievementdiary;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableList;
import java.util.Iterator;
import java.util.List;
import net.runelite.api.Client;

public class OrRequirement implements Requirement {
   private final List<Requirement> requirements;

   public OrRequirement(Requirement... reqs) {
      this.requirements = ImmutableList.copyOf(reqs);
   }

   public String toString() {
      return Joiner.on(" or ").join(this.requirements);
   }

   public boolean satisfiesRequirement(Client client) {
      Iterator var2 = this.getRequirements().iterator();

      Requirement r;
      do {
         if (!var2.hasNext()) {
            return false;
         }

         r = (Requirement)var2.next();
      } while(!r.satisfiesRequirement(client));

      return true;
   }

   public List<Requirement> getRequirements() {
      return this.requirements;
   }
}
