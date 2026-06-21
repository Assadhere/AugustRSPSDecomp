package net.runelite.client.plugins.raids;

import java.util.Arrays;
import java.util.List;

class RotationSolver {
   private static final List[] ROTATIONS;

   static boolean solve(RaidRoom[] rooms) {
      if (rooms == null) {
         return false;
      } else {
         List<RaidRoom> match = null;
         Integer start = null;
         Integer index = null;
         int known = 0;

         int i;
         for(i = 0; i < rooms.length; ++i) {
            if (rooms[i] != null && rooms[i].getType() == RoomType.COMBAT && rooms[i] != RaidRoom.UNKNOWN_COMBAT) {
               if (start == null) {
                  start = i;
               }

               ++known;
            }
         }

         if (known < 2) {
            return false;
         } else if (known == rooms.length) {
            return true;
         } else {
            List[] var11 = ROTATIONS;
            int var6 = var11.length;

            label100:
            for(int var7 = 0; var7 < var6; ++var7) {
               List rotation = var11[var7];

               for(int i = 0; i < rotation.size(); ++i) {
                  if (rooms[start] == rotation.get(i)) {
                     for(int j = start + 1; j < rooms.length; ++j) {
                        if (rooms[j].getType() == RoomType.COMBAT && rooms[j] != RaidRoom.UNKNOWN_COMBAT && rooms[j] != rotation.get(Math.floorMod(i + j - start, rotation.size()))) {
                           continue label100;
                        }
                     }

                     if (match != null && match != rotation) {
                        return false;
                     }

                     index = i - start;
                     match = rotation;
                  }
               }
            }

            if (match == null) {
               return false;
            } else {
               for(i = 0; i < rooms.length; ++i) {
                  if (rooms[i] != null && (rooms[i].getType() != RoomType.COMBAT || rooms[i] == RaidRoom.UNKNOWN_COMBAT)) {
                     rooms[i] = (RaidRoom)match.get(Math.floorMod(index + i, match.size()));
                  }
               }

               return true;
            }
         }
      }
   }

   static {
      ROTATIONS = new List[]{Arrays.asList(RaidRoom.TEKTON, RaidRoom.VASA, RaidRoom.GUARDIANS, RaidRoom.MYSTICS, RaidRoom.SHAMANS, RaidRoom.MUTTADILES, RaidRoom.VANGUARDS, RaidRoom.VESPULA), Arrays.asList(RaidRoom.TEKTON, RaidRoom.MUTTADILES, RaidRoom.GUARDIANS, RaidRoom.VESPULA, RaidRoom.SHAMANS, RaidRoom.VASA, RaidRoom.VANGUARDS, RaidRoom.MYSTICS), Arrays.asList(RaidRoom.VESPULA, RaidRoom.VANGUARDS, RaidRoom.MUTTADILES, RaidRoom.SHAMANS, RaidRoom.MYSTICS, RaidRoom.GUARDIANS, RaidRoom.VASA, RaidRoom.TEKTON), Arrays.asList(RaidRoom.MYSTICS, RaidRoom.VANGUARDS, RaidRoom.VASA, RaidRoom.SHAMANS, RaidRoom.VESPULA, RaidRoom.GUARDIANS, RaidRoom.MUTTADILES, RaidRoom.TEKTON)};
   }
}
