package net.runelite.client.plugins.gpu;

class GLBuffer {
   String name;
   int glBufferId = -1;
   int size = -1;

   GLBuffer(String name) {
      this.name = name;
   }
}
