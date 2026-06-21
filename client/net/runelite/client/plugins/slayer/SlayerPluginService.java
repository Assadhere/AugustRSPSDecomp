package net.runelite.client.plugins.slayer;

import java.util.List;
import javax.annotation.Nullable;
import net.runelite.api.NPC;

public interface SlayerPluginService {
   List<NPC> getTargets();

   @Nullable
   String getTask();

   @Nullable
   String getTaskLocation();

   int getInitialAmount();

   int getRemainingAmount();
}
