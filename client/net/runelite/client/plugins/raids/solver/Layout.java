package net.runelite.client.plugins.raids.solver;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Layout {
   private final List<Room> rooms = new ArrayList();

   public void add(Room room) {
      this.rooms.add(room);
   }

   public Room getRoomAt(int position) {
      Iterator var2 = this.rooms.iterator();

      Room room;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         room = (Room)var2.next();
      } while(room.getPosition() != position);

      return room;
   }

   public String toCode() {
      StringBuilder builder = new StringBuilder();
      Iterator var2 = this.rooms.iterator();

      while(var2.hasNext()) {
         Room room = (Room)var2.next();
         builder.append(room.getSymbol());
      }

      return builder.toString();
   }

   public String toCodeString() {
      return this.toCode().replace("#", "").replace("¤", "");
   }

   public List<Room> getRooms() {
      return this.rooms;
   }
}
