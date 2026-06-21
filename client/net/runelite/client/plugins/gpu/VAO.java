package net.runelite.client.plugins.gpu;

import java.util.Arrays;
import net.runelite.api.Projection;
import net.runelite.api.Scene;
import org.lwjgl.opengl.GL33C;

class VAO {
   static final int VERT_SIZE = 24;
   final VBO vbo;
   int vao;
   int[] lengths = new int[4];
   Projection[] projs = new Projection[4];
   Scene[] scenes = new Scene[4];
   int off = 0;

   VAO(int size) {
      this.vbo = new VBO(size);
   }

   void init() {
      this.vao = GL33C.glGenVertexArrays();
      GL33C.glBindVertexArray(this.vao);
      this.vbo.init(35048);
      GL33C.glBindBuffer(34962, this.vbo.bufId);
      GL33C.glEnableVertexAttribArray(0);
      GL33C.glVertexAttribPointer(0, 3, 5126, false, 24, 0L);
      GL33C.glEnableVertexAttribArray(1);
      GL33C.glVertexAttribIPointer(1, 1, 5124, 24, 12L);
      GL33C.glEnableVertexAttribArray(2);
      GL33C.glVertexAttribIPointer(2, 4, 5122, 24, 16L);
      GL33C.glBindBuffer(34962, 0);
      GL33C.glBindVertexArray(0);
   }

   void destroy() {
      this.vbo.destroy();
      GL33C.glDeleteVertexArrays(this.vao);
      this.vao = 0;
   }

   void addRange(Projection projection, Scene scene) {
      assert this.vbo.mapped;

      if (this.off <= 0 || this.lengths[this.off - 1] != this.vbo.vb.position()) {
         if (this.lengths.length == this.off) {
            int l = this.lengths.length << 1;
            this.lengths = Arrays.copyOf(this.lengths, l);
            this.projs = (Projection[])Arrays.copyOf(this.projs, l);
            this.scenes = (Scene[])Arrays.copyOf(this.scenes, l);
         }

         this.lengths[this.off] = this.vbo.vb.position();
         this.projs[this.off] = projection;
         this.scenes[this.off] = scene;
         ++this.off;
      }
   }

   void draw() {
      assert !this.vbo.mapped;

      int start = 0;

      for(int i = 0; i < this.off; ++i) {
         int end = this.lengths[i];
         Projection p = this.projs[i];
         Scene scene = this.scenes[i];
         int count = end - start;
         GpuPlugin.updateEntityProjection(p);
         GL33C.glUniform4i(GpuPlugin.uniEntityTint, scene.getOverrideHue(), scene.getOverrideSaturation(), scene.getOverrideLuminance(), scene.getOverrideAmount());
         GL33C.glBindVertexArray(this.vao);
         GL33C.glDrawArrays(4, start / 6, count / 6);
         start = end;
      }

   }

   void reset() {
      Arrays.fill(this.projs, 0, this.off, (Object)null);
      Arrays.fill(this.scenes, 0, this.off, (Object)null);
      this.off = 0;
   }
}
