package net.runelite.client.plugins.gpu;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

class GpuFloatBuffer {
   private final FloatBuffer buffer;

   GpuFloatBuffer(int size) {
      this.buffer = allocateDirect(size);
   }

   GpuFloatBuffer put(float f) {
      this.buffer.put(f);
      return this;
   }

   void flip() {
      this.buffer.flip();
   }

   void clear() {
      this.buffer.clear();
   }

   FloatBuffer getBuffer() {
      return this.buffer;
   }

   static FloatBuffer allocateDirect(int size) {
      return ByteBuffer.allocateDirect(size * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
   }
}
