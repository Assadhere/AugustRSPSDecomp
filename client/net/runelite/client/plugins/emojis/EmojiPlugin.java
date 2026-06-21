package net.runelite.client.plugins.emojis;

import com.google.common.hash.Hashing;
import com.google.common.io.FileWriteMode;
import com.google.common.io.Files;
import com.google.common.util.concurrent.Runnables;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.annotations.SerializedName;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import javax.annotation.Nullable;
import javax.imageio.ImageIO;
import javax.inject.Inject;
import javax.inject.Named;
import net.runelite.api.Client;
import net.runelite.api.MessageNode;
import net.runelite.api.Player;
import net.runelite.api.events.ChatMessage;
import net.runelite.api.events.OverheadTextChanged;
import net.runelite.client.RuneLite;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.game.ChatIconManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.util.Text;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.jetbrains.annotations.VisibleForTesting;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@PluginDescriptor(
   name = "Emojis",
   description = "Replaces common emoticons such as :) with their corresponding emoji in the chat",
   enabledByDefault = false
)
public class EmojiPlugin extends Plugin {
   private static final Logger log = LoggerFactory.getLogger(EmojiPlugin.class);
   private static final File EMOJI_DIR;
   @Inject
   private Client client;
   @Inject
   private ClientThread clientThread;
   @Inject
   private ChatIconManager chatIconManager;
   @Inject
   private OkHttpClient okHttpClient;
   @Inject
   private Gson gson;
   @Inject
   private ScheduledExecutorService scheduledExecutorService;
   @Inject
   @Named("runelite.static.base")
   private HttpUrl staticBase;
   @VisibleForTesting
   Index index;
   private final Map<String, Integer> imageCache = new HashMap();

   protected void startUp() {
      this.scheduledExecutorService.execute(this::initEmojiCache);
   }

   @Subscribe
   public void onChatMessage(ChatMessage chatMessage) {
      switch (chatMessage.getType()) {
         case PUBLICCHAT:
         case MODCHAT:
         case FRIENDSCHAT:
         case CLAN_CHAT:
         case CLAN_GUEST_CHAT:
         case CLAN_GIM_CHAT:
         case PRIVATECHAT:
         case PRIVATECHATOUT:
         case MODPRIVATECHAT:
            MessageNode messageNode = chatMessage.getMessageNode();
            String message = messageNode.getValue();
            String updatedMessage = this.updateMessage(message);
            if (updatedMessage == null) {
               return;
            }

            messageNode.setValue(updatedMessage);
            return;
         default:
      }
   }

   @Subscribe
   public void onOverheadTextChanged(OverheadTextChanged event) {
      if (event.getActor() instanceof Player) {
         String message = event.getOverheadText();
         String updatedMessage = this.updateMessage(message);
         if (updatedMessage != null) {
            event.getActor().setOverheadText(updatedMessage);
         }
      }
   }

   @Nullable
   String updateMessage(String message) {
      if (this.index == null) {
         return null;
      } else {
         String editedMessage = message;
         int idxStart = -1;
         int idxStartWs = -1;

         for(int i = 0; i < message.length(); ++i) {
            char c = message.charAt(i);
            String id;
            int var10002;
            if (Character.isWhitespace(c) || c == 160 || i + 1 == message.length()) {
               int idxEndWs = i + 1 == message.length() ? message.length() : i;
               id = Text.removeFormattingTags(message.substring(idxStartWs + 1, idxEndWs));
               idxStartWs = i;
               Emoji emoji = Emoji.getEmoji(id);
               if (emoji != null) {
                  String id = Integer.toHexString(emoji.codepoint);
                  int emojiId = this.getEmojiChatIconIndex(emoji.name(), id);
                  var10002 = this.chatIconManager.chatIconIndex(emojiId);
                  editedMessage = editedMessage.replace(id, "<img=" + var10002 + ">");
               }
            }

            if (c == ':') {
               if (idxStart == -1) {
                  idxStart = i;
               } else {
                  String emojiName = message.substring(idxStart + 1, i);
                  idxStart = -1;
                  id = (String)this.index.names.get(emojiName);
                  if (id != null) {
                     int emojiId = this.getEmojiChatIconIndex(emojiName, id);
                     String var10001 = ":" + emojiName + ":";
                     var10002 = this.chatIconManager.chatIconIndex(emojiId);
                     editedMessage = editedMessage.replace(var10001, "<img=" + var10002 + ">");
                  }
               }
            }
         }

         return message == editedMessage ? null : editedMessage;
      }
   }

   private int getEmojiChatIconIndex(String name, String codepoint) {
      Integer emojiId = (Integer)this.imageCache.get(codepoint);
      if (emojiId != null) {
         return emojiId;
      } else {
         int iconId = this.chatIconManager.reserveChatIcon();
         this.imageCache.put(codepoint, iconId);
         this.scheduledExecutorService.submit(() -> {
            try {
               BufferedImage image = this.loadEmojiFromDisk(name, codepoint);
               this.chatIconManager.updateChatIcon(iconId, image);
            } catch (IOException var5) {
               IOException ex = var5;
               log.error("Unable to load emoji {}", name, ex);
            }

         });
         return iconId;
      }
   }

   private void initEmojiCache() {
      EMOJI_DIR.mkdirs();
      File indexFile = new File(EMOJI_DIR, "index.json");
      this.download("emoji/index.json", indexFile, () -> {
         IOException ex;
         try {
            InputStreamReader in = new InputStreamReader(Files.asByteSource(indexFile).openStream());

            try {
               this.index = (Index)this.gson.fromJson(in, Index.class);
            } catch (Throwable var7) {
               try {
                  in.close();
               } catch (Throwable var6) {
                  var7.addSuppressed(var6);
               }

               throw var7;
            }

            in.close();
         } catch (JsonParseException | IOException var8) {
            ex = var8;
            log.error("Unable to load emoji index", ex);
         }

         try {
            File assetFile = new File(EMOJI_DIR, "assets.zip");
            String hash = Files.asByteSource(assetFile).hash(Hashing.sha256()).toString();
            if (this.index != null && hash.equals(this.index.assetsHash)) {
               log.debug("Emoji assets are up to date");
               return;
            }
         } catch (IOException var5) {
            ex = var5;
            log.debug((String)null, ex);
         }

         log.info("Downloading emoji assets");
         this.download("emoji/assets.zip", new File(EMOJI_DIR, "assets.zip"), Runnables.doNothing());
      });
   }

   private void download(final String srnPath, final File to, final Runnable cb) {
      final HttpUrl url = this.staticBase.newBuilder().addPathSegments(srnPath).build();
      Request request = (new Request.Builder()).url(url).build();
      this.okHttpClient.newCall(request).enqueue(new Callback() {
         public void onResponse(Call call, Response response) throws IOException {
            try {
               Response var20 = response;

               try {
                  InputStream in = response.body().byteStream();

                  try {
                     Files.asByteSink(to, new FileWriteMode[0]).writeFrom(in);
                  } catch (Throwable var16) {
                     if (in != null) {
                        try {
                           in.close();
                        } catch (Throwable var15) {
                           var16.addSuppressed(var15);
                        }
                     }

                     throw var16;
                  }

                  if (in != null) {
                     in.close();
                  }
               } catch (Throwable var17) {
                  if (response != null) {
                     try {
                        var20.close();
                     } catch (Throwable var14) {
                        var17.addSuppressed(var14);
                     }
                  }

                  throw var17;
               }

               if (response != null) {
                  response.close();
               }
            } catch (IOException var18) {
               IOException ex = var18;
               EmojiPlugin.log.error("unable to download {} to {}", new Object[]{srnPath, to, ex});
               throw ex;
            } finally {
               cb.run();
            }

         }

         public void onFailure(Call call, IOException e) {
            EmojiPlugin.log.error("Unable to download {}", url, e);
            cb.run();
         }
      });
   }

   private BufferedImage loadEmojiFromDisk(String name, String id) throws IOException {
      ZipFile zipFile = new ZipFile(new File(EMOJI_DIR, "assets.zip"));

      BufferedImage var14;
      try {
         ZipEntry entry = zipFile.getEntry(id + ".png");
         if (entry == null) {
            throw new IOException("file " + id + ".png doesn't exist");
         }

         InputStream in = zipFile.getInputStream(entry);

         try {
            Class var7 = ImageIO.class;
            BufferedImage image;
            synchronized(ImageIO.class) {
               image = ImageIO.read(in);
            }

            log.debug("Loaded emoji {}: {}", name, id);
            var14 = image;
         } catch (Throwable var12) {
            if (in != null) {
               try {
                  in.close();
               } catch (Throwable var10) {
                  var12.addSuppressed(var10);
               }
            }

            throw var12;
         }

         if (in != null) {
            in.close();
         }
      } catch (Throwable var13) {
         try {
            zipFile.close();
         } catch (Throwable var9) {
            var13.addSuppressed(var9);
         }

         throw var13;
      }

      zipFile.close();
      return var14;
   }

   static {
      EMOJI_DIR = new File(RuneLite.CACHE_DIR, "emojis");
   }

   static class Index {
      Map<String, String> names = Collections.emptyMap();
      @SerializedName("assets_hash")
      String assetsHash;

      public Index() {
      }

      public Map<String, String> getNames() {
         return this.names;
      }

      public String getAssetsHash() {
         return this.assetsHash;
      }

      public void setNames(Map<String, String> names) {
         this.names = names;
      }

      public void setAssetsHash(String assetsHash) {
         this.assetsHash = assetsHash;
      }

      public boolean equals(Object o) {
         if (o == this) {
            return true;
         } else if (!(o instanceof Index)) {
            return false;
         } else {
            Index other = (Index)o;
            if (!other.canEqual(this)) {
               return false;
            } else {
               Object this$names = this.getNames();
               Object other$names = other.getNames();
               if (this$names == null) {
                  if (other$names != null) {
                     return false;
                  }
               } else if (!this$names.equals(other$names)) {
                  return false;
               }

               Object this$assetsHash = this.getAssetsHash();
               Object other$assetsHash = other.getAssetsHash();
               if (this$assetsHash == null) {
                  if (other$assetsHash != null) {
                     return false;
                  }
               } else if (!this$assetsHash.equals(other$assetsHash)) {
                  return false;
               }

               return true;
            }
         }
      }

      protected boolean canEqual(Object other) {
         return other instanceof Index;
      }

      public int hashCode() {
         int PRIME = true;
         int result = 1;
         Object $names = this.getNames();
         result = result * 59 + ($names == null ? 43 : $names.hashCode());
         Object $assetsHash = this.getAssetsHash();
         result = result * 59 + ($assetsHash == null ? 43 : $assetsHash.hashCode());
         return result;
      }

      public String toString() {
         String var10000 = String.valueOf(this.getNames());
         return "EmojiPlugin.Index(names=" + var10000 + ", assetsHash=" + this.getAssetsHash() + ")";
      }
   }
}
