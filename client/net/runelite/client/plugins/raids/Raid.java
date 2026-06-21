package net.runelite.client.plugins.raids;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.runelite.api.coords.WorldPoint;
import net.runelite.client.plugins.raids.solver.Layout;
import net.runelite.client.plugins.raids.solver.Room;

public class Raid {
   private final RaidRoom[] rooms = new RaidRoom[16];
   private Layout layout;
   private final WorldPoint gridBase;
   private final int lobbyIndex;

   public Raid(WorldPoint gridBase, int lobbyIndex) {
      this.gridBase = gridBase;
      this.lobbyIndex = lobbyIndex;
   }

   void updateLayout(Layout layout) {
      if (layout != null) {
         this.layout = layout;

         for(int i = 0; i < this.rooms.length; ++i) {
            if (layout.getRoomAt(i) != null) {
               RaidRoom room = this.rooms[i];
               if (room == null) {
                  RoomType type = RoomType.fromCode(layout.getRoomAt(i).getSymbol());
                  room = type.getUnsolvedRoom();
                  this.setRoom(room, i);
               }
            }
         }

      }
   }

   public RaidRoom getRoom(int position) {
      return this.rooms[position];
   }

   public void setRoom(RaidRoom room, int position) {
      if (position < this.rooms.length) {
         this.rooms[position] = room;
      }

   }

   RaidRoom[] getCombatRooms() {
      List<RaidRoom> combatRooms = new ArrayList();
      Iterator var2 = this.layout.getRooms().iterator();

      while(var2.hasNext()) {
         Room room = (Room)var2.next();
         if (room != null && this.rooms[room.getPosition()].getType() == RoomType.COMBAT) {
            combatRooms.add(this.rooms[room.getPosition()]);
         }
      }

      return (RaidRoom[])combatRooms.toArray(new RaidRoom[0]);
   }

   void setCombatRooms(RaidRoom[] combatRooms) {
      int index = 0;
      Iterator var3 = this.layout.getRooms().iterator();

      while(var3.hasNext()) {
         Room room = (Room)var3.next();
         if (room != null && this.rooms[room.getPosition()].getType() == RoomType.COMBAT) {
            this.rooms[room.getPosition()] = combatRooms[index];
            ++index;
         }
      }

   }

   public String toCode() {
      StringBuilder builder = new StringBuilder();
      RaidRoom[] var2 = this.rooms;
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         RaidRoom room = var2[var4];
         if (room != null) {
            builder.append(room.getType().getCode());
         } else {
            builder.append(' ');
         }
      }

      return builder.toString();
   }

   List<RaidRoom> getOrderedRooms() {
      List<RaidRoom> orderedRooms = new ArrayList();
      Iterator var2 = this.getLayout().getRooms().iterator();

      while(var2.hasNext()) {
         Room r = (Room)var2.next();
         int position = r.getPosition();
         RaidRoom room = this.getRoom(position);
         if (room != null) {
            orderedRooms.add(room);
         }
      }

      return orderedRooms;
   }

   String toRoomString() {
      StringBuilder sb = new StringBuilder();
      Iterator var2 = this.getOrderedRooms().iterator();

      while(var2.hasNext()) {
         RaidRoom room = (RaidRoom)var2.next();
         switch (room.getType()) {
            case PUZZLE:
            case COMBAT:
               sb.append(room.getName()).append(", ");
         }
      }

      String roomsString = sb.toString();
      return roomsString.substring(0, roomsString.length() - 2);
   }

   public RaidRoom[] getRooms() {
      return this.rooms;
   }

   public Layout getLayout() {
      return this.layout;
   }

   public WorldPoint getGridBase() {
      return this.gridBase;
   }

   public int getLobbyIndex() {
      return this.lobbyIndex;
   }
}
