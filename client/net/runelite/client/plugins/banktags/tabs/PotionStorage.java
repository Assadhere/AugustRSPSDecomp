package net.runelite.client.plugins.banktags.tabs;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.IntStream;
import javax.inject.Inject;
import javax.inject.Singleton;
import net.runelite.api.Client;
import net.runelite.api.EnumComposition;
import net.runelite.api.events.ClientTick;
import net.runelite.api.events.VarbitChanged;
import net.runelite.api.events.WidgetClosed;
import net.runelite.api.widgets.Widget;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.game.ItemManager;
import net.runelite.client.plugins.bank.BankSearch;
import net.runelite.client.plugins.banktags.BankTag;
import net.runelite.client.plugins.banktags.BankTagsPlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
class PotionStorage {
   private static final Logger log = LoggerFactory.getLogger(PotionStorage.class);
   static final int BANKTAB_POTIONSTORE = 15;
   static final int COMPONENTS_PER_POTION = 5;
   private final Client client;
   private final BankTagsPlugin plugin;
   private final ItemManager itemManager;
   private final BankSearch bankSearch;
   private Potion[] potions;
   boolean cachePotions;
   private boolean layout;
   private Set<Integer> potionStoreVars;

   @Subscribe
   public void onClientTick(ClientTick event) {
      if (this.cachePotions) {
         log.debug("Rebuilding potions");
         this.cachePotions = false;
         this.rebuildPotions();
         Widget w = this.client.getWidget(786483);
         if (w != null && this.potionStoreVars == null) {
            int[] trigger = w.getVarTransmitTrigger();
            this.potionStoreVars = new HashSet();
            IntStream var10000 = Arrays.stream(trigger);
            Set var10001 = this.potionStoreVars;
            Objects.requireNonNull(var10001);
            var10000.forEach(var10001::add);
         }

         if (this.layout) {
            this.layout = false;
            BankTag activeTag = this.plugin.getActiveBankTag();
            if (activeTag != null) {
               this.bankSearch.layoutBank();
            }
         }
      }

   }

   @Subscribe
   public void onVarbitChanged(VarbitChanged varbitChanged) {
      if (this.potionStoreVars != null && this.potionStoreVars.contains(varbitChanged.getVarpId())) {
         this.cachePotions = true;
         this.layout = true;
      }

   }

   @Subscribe
   public void onWidgetClosed(WidgetClosed event) {
      if (event.getGroupId() == 12 && event.isUnload()) {
         log.debug("Invalidating potions");
         this.potions = null;
      }

   }

   private void rebuildPotions() {
      EnumComposition potionStorePotions = this.client.getEnum(4826);
      EnumComposition potionStoreUnfinishedPotions = this.client.getEnum(4829);
      this.potions = new Potion[potionStorePotions.size() + potionStoreUnfinishedPotions.size()];
      int potionsIdx = 0;
      EnumComposition[] var4 = new EnumComposition[]{potionStorePotions, potionStoreUnfinishedPotions};
      int var5 = var4.length;

      for(int var6 = 0; var6 < var5; ++var6) {
         EnumComposition e = var4[var6];
         int[] var8 = e.getIntVals();
         int var9 = var8.length;

         for(int var10 = 0; var10 < var9; ++var10) {
            int potionEnumId = var8[var10];
            EnumComposition potionEnum = this.client.getEnum(potionEnumId);
            this.client.runScript(new Object[]{3750, potionEnumId});
            int doses = this.client.getIntStack()[0];
            this.client.runScript(new Object[]{4818, potionEnumId});
            int withdrawDoses = this.client.getIntStack()[0];
            if (doses > 0 && withdrawDoses > 0) {
               Potion p = new Potion();
               p.potionEnum = potionEnum;
               p.itemId = potionEnum.getIntValue(withdrawDoses);
               p.doses = doses;
               p.withdrawDoses = withdrawDoses;
               this.potions[potionsIdx] = p;
               if (log.isDebugEnabled()) {
                  log.debug("Potion store has {} doses of {}", p.doses, this.itemManager.getItemComposition(p.itemId).getName());
               }
            }

            ++potionsIdx;
         }
      }

   }

   int matches(Set<Integer> bank, int itemId) {
      if (itemId == 229) {
         return this.hasVialsInPotionStorage() ? 229 : -1;
      } else if (this.potions == null) {
         return -1;
      } else {
         Potion[] var3 = this.potions;
         int var4 = var3.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            Potion potion = var3[var5];
            if (potion != null) {
               EnumComposition potionEnum = potion.potionEnum;
               int potionItemId1 = potionEnum.getIntValue(1);
               int potionItemId2 = potionEnum.getIntValue(2);
               int potionItemId3 = potionEnum.getIntValue(3);
               int potionItemId4 = potionEnum.getIntValue(4);
               if (potionItemId1 == itemId || potionItemId2 == itemId || potionItemId3 == itemId || potionItemId4 == itemId) {
                  int potionStoreItem = potionEnum.getIntValue(potion.withdrawDoses);
                  if (log.isDebugEnabled()) {
                     log.debug("Item {} matches a potion from potion store {}", itemId, this.itemManager.getItemComposition(potionStoreItem).getName());
                  }

                  return potionStoreItem;
               }
            }
         }

         return -1;
      }
   }

   int count(int itemId) {
      if (itemId == 229) {
         return this.getVialsInPotionStorage();
      } else if (this.potions == null) {
         return 0;
      } else {
         Potion[] var2 = this.potions;
         int var3 = var2.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            Potion potion = var2[var4];
            if (potion != null && potion.itemId == itemId) {
               return potion.doses / potion.withdrawDoses;
            }
         }

         return 0;
      }
   }

   int getIdx(int itemId) {
      if (this.potions == null) {
         return -1;
      } else if (itemId == 229) {
         return this.hasVialsInPotionStorage() ? this.potions.length * 5 + 4 : -1;
      } else {
         int potionIdx = 0;
         Potion[] var3 = this.potions;
         int var4 = var3.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            Potion potion = var3[var5];
            ++potionIdx;
            if (potion != null && potion.itemId == itemId) {
               return (potionIdx - 1) * 5;
            }
         }

         return -1;
      }
   }

   boolean hasVialsInPotionStorage() {
      return this.getVialsInPotionStorage() > 0;
   }

   int getVialsInPotionStorage() {
      return this.client.getVarpValue(4286);
   }

   void prepareWidgets() {
      Widget potStoreContent = this.client.getWidget(786483);
      if (potStoreContent.getChildren() == null) {
         int childIdx = 0;

         for(int i = 0; i < this.potions.length; ++i) {
            for(int j = 0; j < 5; ++j) {
               potStoreContent.createChild(childIdx++, 5);
            }
         }

         potStoreContent.createChild(childIdx++, 5);
         potStoreContent.createChild(childIdx++, 3);
         potStoreContent.createChild(childIdx++, 4);
         potStoreContent.createChild(childIdx++, 3);
         potStoreContent.createChild(childIdx++, 4);
      }

   }

   @Inject
   public PotionStorage(Client client, BankTagsPlugin plugin, ItemManager itemManager, BankSearch bankSearch) {
      this.client = client;
      this.plugin = plugin;
      this.itemManager = itemManager;
      this.bankSearch = bankSearch;
   }
}
