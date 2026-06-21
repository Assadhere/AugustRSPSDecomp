package net.runelite.client.plugins.runecraft;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.function.ToIntFunction;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.Point;
import net.runelite.api.Skill;
import net.runelite.api.widgets.WidgetItem;
import net.runelite.client.ui.overlay.WidgetItemOverlay;

class EssencePouchOverlay extends WidgetItemOverlay {
   private final Client client;
   private final RunecraftConfig config;

   @Inject
   EssencePouchOverlay(Client client, RunecraftConfig config) {
      this.client = client;
      this.config = config;
      this.showOnInventory();
   }

   private int durabilityToEssence(int remainingDurability) {
      return (int)Math.ceil(0.4 * Math.pow((double)remainingDurability, 1.07));
   }

   public void renderItemOverlay(Graphics2D g, int itemId, WidgetItem widgetItem) {
      boolean degraded = false;
      EssPouch pouch;
      switch (itemId) {
         case 5509:
            pouch = EssencePouchOverlay.EssPouch.SMALL;
            break;
         case 5511:
            degraded = true;
         case 5510:
            pouch = EssencePouchOverlay.EssPouch.MEDIUM;
            break;
         case 5513:
            degraded = true;
         case 5512:
            pouch = EssencePouchOverlay.EssPouch.LARGE;
            break;
         case 5515:
            degraded = true;
         case 5514:
            pouch = EssencePouchOverlay.EssPouch.GIANT;
            break;
         case 26786:
            degraded = true;
         case 26784:
            pouch = EssencePouchOverlay.EssPouch.COLOSSAL;
            break;
         default:
            return;
      }

      Point pt = widgetItem.getCanvasLocation();
      int breakpoint;
      int amount;
      if (this.config.showPouch()) {
         breakpoint = pouch.maxAmount(this.client);
         amount = pouch.getAmount(this.client);
         if (amount != 0 && amount != breakpoint) {
            g.setColor(Color.YELLOW);
         } else {
            g.setColor(Color.CYAN);
         }

         g.drawString("" + amount, pt.getX(), pt.getY() + 15);
      }

      if (pouch.getDegradation != null && this.config.pouchDegrade()) {
         breakpoint = pouch.nextDegradationBreakpoint(this.client);
         amount = breakpoint - pouch.getDegradation(this.client);
         int remEss = pouch == EssencePouchOverlay.EssPouch.COLOSSAL ? amount : this.durabilityToEssence(amount);
         int limit = pouch.maxAmount(this.client);
         int remFills = (remEss + limit - 1) / limit;
         if (degraded) {
            g.setColor(Color.RED);
         } else if (remFills <= 1) {
            g.setColor(Color.YELLOW);
         } else {
            g.setColor(Color.CYAN);
         }

         g.drawString("" + remFills, pt.getX(), pt.getY() + 30);
      }

   }

   static enum EssPouch {
      SMALL(603, (ToIntFunction)null, new int[0], 3),
      MEDIUM(604, (client) -> {
         return client.getVarpValue(488);
      }, new int[]{800, 0, 400, 3}, 6),
      LARGE(605, (client) -> {
         return client.getVarpValue(489);
      }, new int[]{1000, 0, 800, 3, 600, 5, 400, 7}, 9),
      GIANT(606, (client) -> {
         return client.getVarpValue(490);
      }, new int[]{1200, 0, 1000, 3, 800, 5, 600, 6, 400, 7, 300, 8, 200, 9}, 12),
      COLOSSAL(13682, (client) -> {
         return client.getVarbitValue(13683);
      }, new int[]{1020, 0, 1015, 5, 995, 10, 950, 15, 870, 20, 745, 25, 565, 30, 320, 35}, 40) {
         int scaleLimit(Client client, int limit) {
            int rc = client.getRealSkillLevel(Skill.RUNECRAFT);
            byte scaledMax;
            if (rc >= 85) {
               scaledMax = 40;
            } else if (rc >= 75) {
               scaledMax = 27;
            } else if (rc >= 50) {
               scaledMax = 16;
            } else {
               scaledMax = 8;
            }

            return Math.max(1, limit * scaledMax / 40);
         }
      };

      private final int amountVarb;
      private final ToIntFunction<Client> getDegradation;
      private final int[] degradationLevels;
      private final int maxFill;

      int scaleLimit(Client client, int limit) {
         return limit;
      }

      int maxAmount(Client client) {
         int deg = this.getDegradation(client);
         int limit = this.maxFill;

         for(int i = 0; i < this.degradationLevels.length; i += 2) {
            if (deg >= this.degradationLevels[i]) {
               limit = this.degradationLevels[i + 1];
               break;
            }
         }

         if (limit > 0) {
            limit = this.scaleLimit(client, limit);
         }

         return limit;
      }

      int nextDegradationBreakpoint(Client client) {
         int deg = this.getDegradation.applyAsInt(client);

         for(int i = this.degradationLevels.length - 2; i >= 0; i -= 2) {
            if (deg < this.degradationLevels[i]) {
               return this.degradationLevels[i];
            }
         }

         return deg;
      }

      int getDegradation(Client client) {
         return this.getDegradation == null ? 0 : this.getDegradation.applyAsInt(client);
      }

      int getAmount(Client client) {
         return client.getVarbitValue(this.amountVarb);
      }

      private EssPouch(int amountVarb, ToIntFunction getDegradation, int[] degradationLevels, int maxFill) {
         this.amountVarb = amountVarb;
         this.getDegradation = getDegradation;
         this.degradationLevels = degradationLevels;
         this.maxFill = maxFill;
      }
   }
}
