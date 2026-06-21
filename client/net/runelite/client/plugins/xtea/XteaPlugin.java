package net.runelite.client.plugins.xtea;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.WorldView;
import net.runelite.api.events.WorldViewLoaded;
import net.runelite.client.RuneLite;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.http.api.xtea.XteaKey;
import net.runelite.http.api.xtea.XteaRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@PluginDescriptor(
   name = "Xtea",
   hidden = true
)
public class XteaPlugin extends Plugin {
   private static final Logger log = LoggerFactory.getLogger(XteaPlugin.class);
   private static final File XTEA_CACHE;
   @Inject
   private Client client;
   @Inject
   private XteaClient xteaClient;
   @Inject
   private ScheduledExecutorService executorService;
   @Inject
   private Gson gson;
   private Map<Integer, int[]> xteas;

   protected void startUp() {
      this.executorService.execute(() -> {
         this.xteas = this.load();
      });
   }

   private Map<Integer, int[]> load() {
      try {
         FileInputStream in = new FileInputStream(XTEA_CACHE);

         Map var4;
         try {
            FileChannel channel = in.getChannel();

            try {
               InputStreamReader reader = new InputStreamReader(in, StandardCharsets.UTF_8);

               try {
                  channel.lock(0L, Long.MAX_VALUE, true);
                  var4 = (Map)this.gson.fromJson(reader, (new TypeToken<Map<Integer, int[]>>() {
                  }).getType());
               } catch (Throwable var9) {
                  try {
                     reader.close();
                  } catch (Throwable var8) {
                     var9.addSuppressed(var8);
                  }

                  throw var9;
               }

               reader.close();
            } catch (Throwable var10) {
               if (channel != null) {
                  try {
                     channel.close();
                  } catch (Throwable var7) {
                     var10.addSuppressed(var7);
                  }
               }

               throw var10;
            }

            if (channel != null) {
               channel.close();
            }
         } catch (Throwable var11) {
            try {
               in.close();
            } catch (Throwable var6) {
               var11.addSuppressed(var6);
            }

            throw var11;
         }

         in.close();
         return var4;
      } catch (FileNotFoundException var12) {
         return new HashMap();
      } catch (JsonSyntaxException | IOException var13) {
         Exception e = var13;
         log.debug("error loading xteas", e);
         return new HashMap();
      }
   }

   private void save() {
      try {
         FileOutputStream out = new FileOutputStream(XTEA_CACHE);

         try {
            FileChannel channel = out.getChannel();

            try {
               OutputStreamWriter writer = new OutputStreamWriter(out, StandardCharsets.UTF_8);

               try {
                  channel.lock();
                  this.gson.toJson(this.xteas, (new TypeToken<Map<Integer, int[]>>() {
                  }).getType(), writer);
               } catch (Throwable var9) {
                  try {
                     writer.close();
                  } catch (Throwable var8) {
                     var9.addSuppressed(var8);
                  }

                  throw var9;
               }

               writer.close();
            } catch (Throwable var10) {
               if (channel != null) {
                  try {
                     channel.close();
                  } catch (Throwable var7) {
                     var10.addSuppressed(var7);
                  }
               }

               throw var10;
            }

            if (channel != null) {
               channel.close();
            }
         } catch (Throwable var11) {
            try {
               out.close();
            } catch (Throwable var6) {
               var11.addSuppressed(var6);
            }

            throw var11;
         }

         out.close();
      } catch (IOException var12) {
         IOException e = var12;
         log.debug("error saving xteas", e);
      }

   }

   @Subscribe
   public void onWorldViewLoaded(WorldViewLoaded event) {
      WorldView wv = event.getWorldView();
      int revision = this.client.getRevision();
      int[] regions = wv.getMapRegions();
      int[][] xteaKeys = wv.getXteaKeys();
      if (xteaKeys != null) {
         XteaRequest xteaRequest = new XteaRequest();
         xteaRequest.setRevision(revision);

         for(int idx = 0; idx < regions.length; ++idx) {
            int region = regions[idx];
            int[] keys = xteaKeys[idx];
            int[] seenKeys = (int[])this.xteas.get(region);
            if (!Arrays.equals(seenKeys, keys)) {
               this.xteas.put(region, keys);
               log.debug("World {} region {} keys {}, {}, {}, {}", new Object[]{wv.getId(), region, keys[0], keys[1], keys[2], keys[3]});
               XteaKey xteaKey = new XteaKey();
               xteaKey.setRegion(region);
               xteaKey.setKeys(keys);
               xteaRequest.addKey(xteaKey);
            }
         }

         if (!xteaRequest.getKeys().isEmpty()) {
            this.xteaClient.submit(xteaRequest);
            this.executorService.execute(this::save);
         }
      }
   }

   static {
      XTEA_CACHE = new File(RuneLite.CACHE_DIR, "xtea.json");
   }
}
