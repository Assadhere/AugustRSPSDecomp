package net.runelite.client.plugins.gpu;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import org.lwjgl.opengl.GL33C;

class VBO {
   final int size;
   private int usage;
   int bufId;
   private ByteBuffer buffer;
   IntBuffer vb;
   int len;
   boolean mapped;

   VBO(int size) {
      this.size = size;
   }

   void init(int usage) {
      this.usage = usage;
      this.bufId = GL33C.glGenBuffers();
      GL33C.glBindBuffer(34962, this.bufId);
      GL33C.glBufferData(34962, (long)this.size, usage);
      GL33C.glBindBuffer(34962, 0);
   }

   void destroy() {
      if (this.mapped) {
         GL33C.glBindBuffer(34962, this.bufId);
         GL33C.glUnmapBuffer(34962);
         GL33C.glBindBuffer(34962, 0);
         this.mapped = false;
      }

      GL33C.glDeleteBuffers(this.bufId);
      this.bufId = 0;
   }

   void map() {
      assert !this.mapped;

      GL33C.glBindBuffer(34962, this.bufId);
      this.buffer = GL33C.glMapBufferRange(34962, 0L, (long)this.size, 2 | (this.usage == 35044 ? 0 : 24), this.buffer);
      if (this.buffer == null) {
         int err = GL33C.glGetError();
         int var10002 = this.bufId;
         throw new RuntimeException("unable to map GL buffer (bufId: " + var10002 + " size: " + this.size + " renderer: " + GL33C.glGetString(7937) + " version: " + GL33C.glGetString(7938) + " err: " + err + ")");
      } else {
         this.vb = this.buffer.asIntBuffer();
         GL33C.glBindBuffer(34962, 0);
         this.mapped = true;
      }
   }

   void unmap() {
      assert this.mapped;

      this.len = this.vb.position();
      this.vb = null;
      GL33C.glBindBuffer(34962, this.bufId);
      if (this.usage != 35044) {
         GL33C.glFlushMappedBufferRange(34962, 0L, (long)this.len * 4L);
      }

      GL33C.glUnmapBuffer(34962);
      GL33C.glBindBuffer(34962, 0);
      this.mapped = false;
   }
}
