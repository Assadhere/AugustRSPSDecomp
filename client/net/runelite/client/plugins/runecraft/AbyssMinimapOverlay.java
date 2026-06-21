package net.runelite.client.plugins.runecraft;

import com.google.inject.Inject;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import net.runelite.api.Client;
import net.runelite.api.DecorativeObject;
import net.runelite.api.Perspective;
import net.runelite.api.Player;
import net.runelite.api.Point;
import net.runelite.client.game.ItemManager;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;

class AbyssMinimapOverlay extends Overlay {
   private static final Dimension IMAGE_SIZE = new Dimension(15, 14);
   private final Map<AbyssRifts, BufferedImage> abyssIcons = new HashMap();
   private final Client client;
   private final RunecraftPlugin plugin;
   private final RunecraftConfig config;
   private final ItemManager itemManager;

   @Inject
   AbyssMinimapOverlay(Client client, RunecraftPlugin plugin, RunecraftConfig config, ItemManager itemManager) {
      this.setPosition(OverlayPosition.DYNAMIC);
      this.setLayer(OverlayLayer.ABOVE_WIDGETS);
      this.client = client;
      this.plugin = plugin;
      this.config = config;
      this.itemManager = itemManager;
   }

   public Dimension render(Graphics2D graphics) {
      Player player = this.client.getLocalPlayer();
      if (player == null) {
         return null;
      } else {
         int region = player.getWorldLocation().getRegionID();
         if (region == 12107 && this.config.showRifts()) {
            Iterator var4 = this.plugin.getAbyssObjects().iterator();

            while(var4.hasNext()) {
               DecorativeObject object = (DecorativeObject)var4.next();
               AbyssRifts rift = AbyssRifts.getRift(object.getId());
               if (rift != null && rift.getConfigEnabled().test(this.config)) {
                  BufferedImage image = this.getImage(rift);
                  Point miniMapImage = Perspective.getMiniMapImageLocation(this.client, object.getLocalLocation(), image);
                  if (miniMapImage != null) {
                     graphics.drawImage(image, miniMapImage.getX(), miniMapImage.getY(), (ImageObserver)null);
                  }
               }
            }

            return null;
         } else {
            return null;
         }
      }
   }

   private BufferedImage getImage(AbyssRifts rift) {
      BufferedImage image = (BufferedImage)this.abyssIcons.get(rift);
      if (image != null) {
         return image;
      } else {
         BufferedImage image = this.itemManager.getImage(rift.getItemId());
         BufferedImage resizedImage = new BufferedImage(IMAGE_SIZE.width, IMAGE_SIZE.height, 2);
         Graphics2D g = resizedImage.createGraphics();
         g.drawImage(image, 0, 0, IMAGE_SIZE.width, IMAGE_SIZE.height, (ImageObserver)null);
         g.dispose();
         this.abyssIcons.put(rift, resizedImage);
         return resizedImage;
      }
   }
}
