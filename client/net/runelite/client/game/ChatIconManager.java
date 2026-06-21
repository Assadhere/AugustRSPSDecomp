package net.runelite.client.game;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;
import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Singleton;
import net.runelite.api.Client;
import net.runelite.api.EnumComposition;
import net.runelite.api.FriendsChatRank;
import net.runelite.api.GameState;
import net.runelite.api.IndexedSprite;
import net.runelite.api.clan.ClanTitle;
import net.runelite.api.events.GameStateChanged;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.eventbus.EventBus;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.util.AsyncBufferedImage;
import net.runelite.client.util.ImageUtil;

@Singleton
public class ChatIconManager {
   private static final Dimension IMAGE_DIMENSION = new Dimension(11, 11);
   private static final Color IMAGE_OUTLINE_COLOR = new Color(33, 33, 33);
   private final Client client;
   private final SpriteManager spriteManager;
   private final ClientThread clientThread;
   private BufferedImage[] friendsChatRankImages;
   private BufferedImage[] clanRankImages;
   private int friendsChatOffset = -1;
   private int clanOffset = -1;
   private final List<ChatIcon> icons = new ArrayList();

   @Inject
   private ChatIconManager(Client client, SpriteManager spriteManager, ClientThread clientThread, EventBus eventBus) {
      this.client = client;
      this.spriteManager = spriteManager;
      this.clientThread = clientThread;
      eventBus.register(this);
      clientThread.invokeLater(() -> {
         if (client.getGameState().getState() >= GameState.LOGIN_SCREEN.getState()) {
            if (this.friendsChatOffset == -1) {
               this.loadRankIcons();
            }

            this.refreshIcons();
         }

      });
   }

   @Subscribe
   private void onGameStateChanged(GameStateChanged gameStateChanged) {
      GameState state = gameStateChanged.getGameState();
      if (state == GameState.STARTING) {
         this.friendsChatOffset = this.clanOffset = -1;
         ChatIcon icon;
         synchronized(this) {
            for(Iterator var4 = this.icons.iterator(); var4.hasNext(); icon.idx = -1) {
               icon = (ChatIcon)var4.next();
            }
         }
      } else if (state == GameState.LOGIN_SCREEN) {
         if (this.friendsChatOffset == -1) {
            this.loadRankIcons();
         }

         this.refreshIcons();
      }

   }

   public synchronized int registerChatIcon(BufferedImage image) {
      if (image != null && !(image instanceof AsyncBufferedImage)) {
         IndexedSprite i = ImageUtil.getImageIndexedSprite(image, this.client);
         this.icons.add(new ChatIcon(-1, i));
         this.clientThread.invokeLater(this::refreshIcons);
         return this.icons.size() - 1;
      } else {
         throw new IllegalArgumentException("invalid image");
      }
   }

   public synchronized int reserveChatIcon() {
      assert this.client.isClientThread();

      BufferedImage dummy = new BufferedImage(13, 13, 1);
      IndexedSprite sprite = ImageUtil.getImageIndexedSprite(dummy, this.client);
      IndexedSprite[] modicons = this.client.getModIcons();
      modicons = (IndexedSprite[])Arrays.copyOf(modicons, modicons.length + 1);
      modicons[modicons.length - 1] = sprite;
      this.client.setModIcons(modicons);
      this.icons.add(new ChatIcon(modicons.length - 1, sprite));
      return this.icons.size() - 1;
   }

   public synchronized void updateChatIcon(int iconId, BufferedImage image) {
      ChatIcon ci = (ChatIcon)this.icons.get(iconId);
      IndexedSprite sprite = ImageUtil.getImageIndexedSprite(image, this.client);
      int idx = ci.idx;
      if (idx == -1) {
         ci.sprite = sprite;
      } else {
         IndexedSprite[] modicons = this.client.getModIcons();
         modicons[idx] = sprite;
      }
   }

   public int chatIconIndex(int iconId) {
      return iconId >= 0 && iconId < this.icons.size() ? ((ChatIcon)this.icons.get(iconId)).idx : -1;
   }

   @Nullable
   public BufferedImage getRankImage(FriendsChatRank friendsChatRank) {
      return friendsChatRank == FriendsChatRank.UNRANKED ? null : this.friendsChatRankImages[friendsChatRank.ordinal() - 1];
   }

   @Nullable
   public BufferedImage getRankImage(ClanTitle clanTitle) {
      int rank = clanTitle.getId();
      int idx = clanRankToIdx(rank);
      return this.clanRankImages[idx];
   }

   public int getIconNumber(FriendsChatRank friendsChatRank) {
      return this.friendsChatOffset == -1 ? -1 : this.friendsChatOffset + friendsChatRank.ordinal() - 1;
   }

   public int getIconNumber(ClanTitle clanTitle) {
      int rank = clanTitle.getId();
      return this.clanOffset == -1 ? -1 : this.clanOffset + clanRankToIdx(rank);
   }

   private synchronized void refreshIcons() {
      IndexedSprite[] chatIcons = this.client.getModIcons();
      if (chatIcons != null) {
         int offset = chatIcons.length;
         int newIcons = 0;
         Iterator var4 = this.icons.iterator();

         while(var4.hasNext()) {
            ChatIcon icon = (ChatIcon)var4.next();

            assert icon.idx < offset;

            if (icon.idx == -1) {
               ++newIcons;
            }
         }

         if (newIcons != 0) {
            IndexedSprite[] newChatIcons = (IndexedSprite[])Arrays.copyOf(chatIcons, chatIcons.length + newIcons);
            newIcons = 0;
            Iterator var8 = this.icons.iterator();

            while(var8.hasNext()) {
               ChatIcon icon = (ChatIcon)var8.next();
               if (icon.idx == -1) {
                  icon.idx = offset + newIcons++;
                  newChatIcons[icon.idx] = icon.sprite;
               }
            }

            this.client.setModIcons(newChatIcons);
         }
      }
   }

   private void loadRankIcons() {
      EnumComposition friendsChatIcons = this.client.getEnum(1543);
      EnumComposition clanIcons = this.client.getEnum(3798);
      IndexedSprite[] modIcons = this.client.getModIcons();
      this.friendsChatOffset = modIcons.length;
      this.clanOffset = this.friendsChatOffset + friendsChatIcons.size();
      IndexedSprite blank = ImageUtil.getImageIndexedSprite(new BufferedImage(modIcons[0].getWidth(), modIcons[0].getHeight(), 2), this.client);
      modIcons = (IndexedSprite[])Arrays.copyOf(modIcons, this.friendsChatOffset + friendsChatIcons.size() + clanIcons.size());
      Arrays.fill(modIcons, this.friendsChatOffset, modIcons.length, blank);
      this.client.setModIcons(modIcons);
      this.friendsChatRankImages = new BufferedImage[friendsChatIcons.size()];
      this.clanRankImages = new BufferedImage[clanIcons.size()];

      int i;
      for(i = 0; i < friendsChatIcons.size(); ++i) {
         this.spriteManager.getSpriteAsync(friendsChatIcons.getIntValue(friendsChatIcons.getKeys()[i]), 0, (Consumer)((sprite) -> {
            IndexedSprite[] modIcons = this.client.getModIcons();
            this.friendsChatRankImages[i] = friendsChatImageFromSprite(sprite);
            modIcons[this.friendsChatOffset + i] = ImageUtil.getImageIndexedSprite(this.friendsChatRankImages[i], this.client);
         }));
      }

      for(i = 0; i < clanIcons.size(); ++i) {
         int key = clanIcons.getKeys()[i];
         int idx = clanRankToIdx(key);

         assert idx >= 0 && idx < clanIcons.size();

         this.spriteManager.getSpriteAsync(clanIcons.getIntValue(key), 0, (Consumer)((sprite) -> {
            IndexedSprite[] modIcons = this.client.getModIcons();
            BufferedImage img = ImageUtil.resizeCanvas(sprite, IMAGE_DIMENSION.width, IMAGE_DIMENSION.height);
            this.clanRankImages[idx] = img;
            modIcons[this.clanOffset + idx] = ImageUtil.getImageIndexedSprite(img, this.client);
         }));
      }

   }

   private static BufferedImage friendsChatImageFromSprite(BufferedImage sprite) {
      BufferedImage canvas = ImageUtil.resizeCanvas(sprite, IMAGE_DIMENSION.width, IMAGE_DIMENSION.height);
      return ImageUtil.outlineImage(canvas, IMAGE_OUTLINE_COLOR);
   }

   private static int clanRankToIdx(int key) {
      return key < 0 ? ~key : key + 5;
   }

   private static class ChatIcon {
      int idx;
      IndexedSprite sprite;

      public ChatIcon(int idx, IndexedSprite sprite) {
         this.idx = idx;
         this.sprite = sprite;
      }
   }
}
