package net.runelite.client.plugins.gpu;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;

class GpuIntBuffer {
   private final IntBuffer buffer;

   GpuIntBuffer(IntBuffer ib) {
      this.buffer = ib;
   }

   void put22224(int x, int y, int z, int w) {
      this.buffer.put((y & '\uffff') << 16 | x & '\uffff');
      this.buffer.put(z & '\uffff');
      this.buffer.put(w);
   }

   void put2222(int x, int y, int z, int w) {
      this.buffer.put((y & '\uffff') << 16 | x & '\uffff');
      this.buffer.put((w & '\uffff') << 16 | z & '\uffff');
   }

   void flip() {
      this.buffer.flip();
   }

   void clear() {
      this.buffer.clear();
   }

   IntBuffer getBuffer() {
      return this.buffer;
   }

   static IntBuffer allocateDirect(int size) {
      return ByteBuffer.allocateDirect(size * 4).order(ByteOrder.nativeOrder()).asIntBuffer();
   }
}
