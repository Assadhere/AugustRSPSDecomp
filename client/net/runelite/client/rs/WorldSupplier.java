package net.runelite.client.rs;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.Random;
import java.util.function.Supplier;
import net.runelite.http.api.worlds.World;

class WorldSupplier implements Supplier<World> {
   private final Random random = new Random(System.nanoTime());
   private final Queue<World> worlds = new ArrayDeque();

   public World get() {
      if (!this.worlds.isEmpty()) {
         return (World)this.worlds.poll();
      } else {
         while(this.worlds.size() < 16) {
            int id = this.random.nextInt(50) + 1;
            World world = World.builder().id(300 + id).address("oldschool" + id + ".runescape.COM").build();
            this.worlds.add(world);
         }

         return (World)this.worlds.poll();
      }
   }
}
