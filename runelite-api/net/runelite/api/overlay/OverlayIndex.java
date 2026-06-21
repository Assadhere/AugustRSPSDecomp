package net.runelite.api.overlay;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OverlayIndex {
   private static final Logger log = LoggerFactory.getLogger(OverlayIndex.class);
   private static final Set<Integer> overlays = new HashSet();

   public static boolean hasOverlay(int indexId, int archiveId) {
      return overlays.contains(indexId << 16 | archiveId);
   }

   static {
      try {
         InputStream indexStream = OverlayIndex.class.getResourceAsStream("/runelite/index");

         try {
            DataInputStream in = new DataInputStream(indexStream);

            int id;
            try {
               while((id = in.readInt()) != -1) {
                  overlays.add(id);
               }
            } catch (Throwable var6) {
               try {
                  in.close();
               } catch (Throwable var5) {
                  var6.addSuppressed(var5);
               }

               throw var6;
            }

            in.close();
         } catch (Throwable var7) {
            if (indexStream != null) {
               try {
                  indexStream.close();
               } catch (Throwable var4) {
                  var7.addSuppressed(var4);
               }
            }

            throw var7;
         }

         if (indexStream != null) {
            indexStream.close();
         }
      } catch (IOException var8) {
         IOException ex = var8;
         log.warn("unable to load overlay index", ex);
      }

   }
}
