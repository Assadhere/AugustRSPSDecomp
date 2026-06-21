package net.runelite.client.callback;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import javax.inject.Singleton;
import net.runelite.api.Renderable;
import net.runelite.api.Scene;
import net.runelite.api.Tile;
import net.runelite.api.TileObject;
import net.runelite.client.externalplugins.ExternalPluginMdc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
public class RenderCallbackManager {
   private static final Logger log = LoggerFactory.getLogger(RenderCallbackManager.class);
   private final List<RenderCallback> callbacks = new CopyOnWriteArrayList();

   public void register(RenderCallback cb) {
      this.callbacks.add(cb);
   }

   public void unregister(RenderCallback cb) {
      this.callbacks.remove(cb);
   }

   public boolean addEntity(Renderable renderable, boolean ui) {
      if (this.callbacks.isEmpty()) {
         return true;
      } else {
         Iterator var3 = this.callbacks.iterator();

         while(true) {
            if (var3.hasNext()) {
               RenderCallback cb = (RenderCallback)var3.next();
               ExternalPluginMdc.ScopedMdc pluginMdc = ExternalPluginMdc.open((Object)cb);

               boolean var12;
               try {
                  if (cb.addEntity(renderable, ui)) {
                     continue;
                  }

                  var12 = false;
               } catch (Exception var10) {
                  Exception ex = var10;
                  log.error("exception in render callback", ex);
                  continue;
               } finally {
                  pluginMdc.close();
               }

               return var12;
            }

            return true;
         }
      }
   }

   public void onDraw(Renderable renderable) {
      if (!this.callbacks.isEmpty()) {
         Iterator var2 = this.callbacks.iterator();

         while(var2.hasNext()) {
            RenderCallback cb = (RenderCallback)var2.next();
            ExternalPluginMdc.ScopedMdc pluginMdc = ExternalPluginMdc.open((Object)cb);

            try {
               cb.onDraw(renderable);
            } catch (Exception var9) {
               Exception ex = var9;
               log.error("exception in render callback", ex);
            } finally {
               pluginMdc.close();
            }
         }

      }
   }

   public boolean drawTile(Scene scene, Tile tile) {
      if (this.callbacks.isEmpty()) {
         return true;
      } else {
         Iterator var3 = this.callbacks.iterator();

         while(true) {
            if (var3.hasNext()) {
               RenderCallback cb = (RenderCallback)var3.next();
               ExternalPluginMdc.ScopedMdc pluginMdc = ExternalPluginMdc.open((Object)cb);

               boolean var12;
               try {
                  if (cb.drawTile(scene, tile)) {
                     continue;
                  }

                  var12 = false;
               } catch (Exception var10) {
                  Exception ex = var10;
                  log.error("exception in render callback", ex);
                  continue;
               } finally {
                  pluginMdc.close();
               }

               return var12;
            }

            return true;
         }
      }
   }

   public boolean drawObject(Scene scene, TileObject object) {
      if (this.callbacks.isEmpty()) {
         return true;
      } else {
         Iterator var3 = this.callbacks.iterator();

         while(true) {
            if (var3.hasNext()) {
               RenderCallback cb = (RenderCallback)var3.next();
               ExternalPluginMdc.ScopedMdc pluginMdc = ExternalPluginMdc.open((Object)cb);

               boolean var12;
               try {
                  if (cb.drawObject(scene, object)) {
                     continue;
                  }

                  var12 = false;
               } catch (Exception var10) {
                  Exception ex = var10;
                  log.error("exception in render callback", ex);
                  continue;
               } finally {
                  pluginMdc.close();
               }

               return var12;
            }

            return true;
         }
      }
   }
}
