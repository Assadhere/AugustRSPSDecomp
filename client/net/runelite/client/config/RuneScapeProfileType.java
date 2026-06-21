package net.runelite.client.config;

import java.util.function.Predicate;
import net.runelite.api.Client;
import net.runelite.api.WorldType;

public enum RuneScapeProfileType {
   STANDARD((client) -> {
      return true;
   }),
   BETA((client) -> {
      return client.getWorldType().contains(WorldType.NOSAVE_MODE) || client.getWorldType().contains(WorldType.BETA_WORLD);
   }),
   QUEST_SPEEDRUNNING((client) -> {
      return client.getWorldType().contains(WorldType.QUEST_SPEEDRUNNING);
   }),
   DEADMAN((client) -> {
      return client.getWorldType().contains(WorldType.DEADMAN);
   }),
   PVP_ARENA((client) -> {
      return client.getWorldType().contains(WorldType.PVP_ARENA);
   }),
   TRAILBLAZER_LEAGUE,
   DEADMAN_REBORN,
   SHATTERED_RELICS_LEAGUE,
   TRAILBLAZER_RELOADED_LEAGUE,
   RAGING_ECHOES_LEAGUE,
   GRID_MASTER,
   DEMONIC_PACTS_LEAGUE((client) -> {
      return client.getWorldType().contains(WorldType.SEASONAL);
   });

   private final Predicate<Client> test;

   private RuneScapeProfileType() {
      this((client) -> {
         return false;
      });
   }

   public static RuneScapeProfileType getCurrent(Client client) {
      RuneScapeProfileType[] types = values();

      for(int i = types.length - 1; i >= 0; --i) {
         RuneScapeProfileType type = types[i];
         if (types[i].test.test(client)) {
            return type;
         }
      }

      return STANDARD;
   }

   public Predicate<Client> getTest() {
      return this.test;
   }

   private RuneScapeProfileType(Predicate test) {
      this.test = test;
   }
}
