package net.runelite.client.plugins.gpu;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.runelite.api.Projection;
import net.runelite.api.Scene;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class VAOList {
   private static final Logger log = LoggerFactory.getLogger(VAOList.class);
   private static final int VAO_SIZE = 4194304;
   private int curIdx;
   final List<VAO> vaos = new ArrayList();

   VAO get(int size) {
      assert size <= 4194304;

      VAO vao;
      while(this.curIdx < this.vaos.size()) {
         vao = (VAO)this.vaos.get(this.curIdx);
         if (!vao.vbo.mapped) {
            vao.vbo.map();
         }

         int rem = vao.vbo.vb.remaining() * 4;
         if (size <= rem) {
            return vao;
         }

         ++this.curIdx;
      }

      vao = new VAO(4194304);
      vao.init();
      vao.vbo.map();
      this.vaos.add(vao);
      log.debug("Allocated VAO {} request {}", vao.vao, size);
      return vao;
   }

   int unmap() {
      int sz = 0;

      for(int i = 0; i < this.vaos.size(); ++i) {
         VAO vao = (VAO)this.vaos.get(i);
         if (vao.vbo.mapped) {
            ++sz;
            vao.vbo.unmap();
         }
      }

      this.curIdx = 0;
      return sz;
   }

   void free() {
      Iterator var1 = this.vaos.iterator();

      while(var1.hasNext()) {
         VAO vao = (VAO)var1.next();
         vao.destroy();
      }

      this.vaos.clear();
      this.curIdx = 0;
   }

   void addRange(Projection projection, Scene scene) {
      for(int i = 0; i <= this.curIdx && i < this.vaos.size(); ++i) {
         VAO vao = (VAO)this.vaos.get(i);
         if (vao.vbo.mapped) {
            vao.addRange(projection, scene);
         }
      }

   }

   void debug() {
      log.debug("{} vaos allocated", this.vaos.size());
      Iterator var1 = this.vaos.iterator();

      while(true) {
         VAO vao;
         do {
            if (!var1.hasNext()) {
               return;
            }

            vao = (VAO)var1.next();
            log.debug("vao {} mapped: {} num ranges: {} length: {}", new Object[]{vao, vao.vbo.mapped, vao.off, vao.vbo.mapped ? vao.vbo.vb.position() : -1});
         } while(vao.off <= 1);

         for(int i = 0; i < vao.off; ++i) {
            log.debug("  {} {} {}", new Object[]{vao.lengths[i], vao.projs[i], vao.scenes[i]});
         }
      }
   }
}
