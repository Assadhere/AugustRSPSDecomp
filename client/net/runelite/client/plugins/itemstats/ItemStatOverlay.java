package net.runelite.client.plugins.itemstats;

import com.google.common.annotations.VisibleForTesting;
import com.google.inject.Inject;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.time.Duration;
import net.runelite.api.Client;
import net.runelite.api.EquipmentInventorySlot;
import net.runelite.api.Item;
import net.runelite.api.ItemContainer;
import net.runelite.api.MenuEntry;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetUtil;
import net.runelite.client.game.ItemEquipmentStats;
import net.runelite.client.game.ItemManager;
import net.runelite.client.game.ItemStats;
import net.runelite.client.plugins.itemstats.potions.PotionDuration;
import net.runelite.client.ui.JagexColors;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.tooltip.Tooltip;
import net.runelite.client.ui.overlay.tooltip.TooltipManager;
import net.runelite.client.util.ColorUtil;
import net.runelite.client.util.QuantityFormatter;
import org.apache.commons.lang3.time.DurationFormatUtils;

public class ItemStatOverlay extends Overlay {
   @VisibleForTesting
   static final ItemStats UNARMED = new ItemStats(true, 0.0, 0, ItemEquipmentStats.builder().aspeed(4).build());
   @Inject
   private Client client;
   @Inject
   private ItemManager itemManager;
   @Inject
   private TooltipManager tooltipManager;
   @Inject
   private ItemStatChanges statChanges;
   @Inject
   private ItemStatConfig config;

   public Dimension render(Graphics2D graphics) {
      if (!this.client.isMenuOpen() && (this.config.relative() || this.config.absolute() || this.config.theoretical())) {
         MenuEntry[] menu = this.client.getMenuEntries();
         int menuSize = menu.length;
         if (menuSize <= 0) {
            return null;
         } else {
            MenuEntry entry = menu[menuSize - 1];
            Widget widget = entry.getWidget();
            if (widget == null) {
               return null;
            } else {
               int group = WidgetUtil.componentToInterface(widget.getId());
               int itemId = -1;
               if (group != 387 && (group != 12 || widget.getParentId() != 983044)) {
                  if (widget.getId() == 9764864 || group == 85 || widget.getId() == 786444 && this.config.showStatsInBank() || group == 15 && this.config.showStatsInBank() || widget.getId() == 47448074 && this.config.showStatsInBank() || group == 725 && this.config.showStatsInBank()) {
                     itemId = widget.getItemId();
                  }
               } else {
                  Widget widgetItem = widget.getChild(1);
                  if (widgetItem != null) {
                     itemId = widgetItem.getItemId();
                  }
               }

               if (itemId == -1) {
                  return null;
               } else {
                  if (this.config.consumableStats()) {
                     Effect change = this.statChanges.get(itemId);
                     int var13;
                     if (change != null) {
                        StringBuilder b = new StringBuilder();
                        StatsChanges statsChanges = change.calculate(this.client);
                        StatChange[] var11 = statsChanges.getStatChanges();
                        int var12 = var11.length;

                        for(var13 = 0; var13 < var12; ++var13) {
                           StatChange c = var11[var13];
                           b.append(this.buildStatChangeString(c));
                        }

                        String tooltip = b.toString();
                        if (!tooltip.isEmpty()) {
                           this.tooltipManager.add(new Tooltip(tooltip));
                        }
                     }

                     PotionDuration p = PotionDuration.get(itemId);
                     if (p != null) {
                        PotionDuration.PotionDurationRange[] durationRanges = p.getDurationRanges();
                        StringBuilder sb = new StringBuilder();
                        if (durationRanges.length == 1) {
                           Duration duration = durationRanges[0].getLowestDuration();
                           sb.append("Duration: ").append(DurationFormatUtils.formatDuration(duration.toMillis(), "m:ss"));
                        } else {
                           PotionDuration.PotionDurationRange[] var25 = durationRanges;
                           var13 = durationRanges.length;

                           for(int var27 = 0; var27 < var13; ++var27) {
                              PotionDuration.PotionDurationRange durationRange = var25[var27];
                              if (sb.length() > 0) {
                                 sb.append("</br>");
                              }

                              sb.append(durationRange.getPotionName()).append(": ");
                              Duration lowestDuration = durationRange.getLowestDuration();
                              sb.append(DurationFormatUtils.formatDuration(lowestDuration.toMillis(), "m:ss"));
                              Duration highestDuration = durationRange.getHighestDuration();
                              if (lowestDuration != highestDuration) {
                                 sb.append('~');
                                 sb.append(DurationFormatUtils.formatDuration(highestDuration.toMillis(), "m:ss"));
                              }
                           }
                        }

                        this.tooltipManager.add(new Tooltip(sb.toString()));
                     }
                  }

                  if (this.config.equipmentStats()) {
                     ItemStats stats = this.itemManager.getItemStats(itemId);
                     if (stats != null) {
                        String tooltip = this.buildStatBonusString(stats);
                        if (!tooltip.isEmpty()) {
                           this.tooltipManager.add(new Tooltip(tooltip));
                        }
                     }
                  }

                  return null;
               }
            }
         }
      } else {
         return null;
      }
   }

   private String getChangeString(double value, boolean inverse, boolean showPercent) {
      Color plus = Positivity.getColor(this.config, Positivity.BETTER_UNCAPPED);
      Color minus = Positivity.getColor(this.config, Positivity.WORSE);
      if (value == 0.0) {
         return "";
      } else {
         Color color;
         if (inverse) {
            color = value > 0.0 ? minus : plus;
         } else {
            color = value > 0.0 ? plus : minus;
         }

         String prefix = value > 0.0 ? "+" : "";
         String suffix = showPercent ? "%" : "";
         String valueString = QuantityFormatter.formatNumber(value);
         return ColorUtil.wrapWithColorTag(prefix + valueString + suffix, color);
      }
   }

   private String buildStatRow(String label, double value, double diffValue, boolean inverse, boolean showPercent) {
      return this.buildStatRow(label, value, diffValue, inverse, showPercent, true);
   }

   private String buildStatRow(String label, double value, double diffValue, boolean inverse, boolean showPercent, boolean showBase) {
      StringBuilder b = new StringBuilder();
      if (value != 0.0 || diffValue != 0.0) {
         String changeStr = this.getChangeString(diffValue, inverse, showPercent);
         if (this.config.alwaysShowBaseStats() && showBase) {
            String valueStr = QuantityFormatter.formatNumber(value);
            b.append(label).append(": ").append(valueStr).append(!changeStr.isEmpty() ? " (" + changeStr + ") " : "").append("</br>");
         } else if (!changeStr.isEmpty()) {
            b.append(label).append(": ").append(changeStr).append("</br>");
         }
      }

      return b.toString();
   }

   private ItemStats getItemStatsFromContainer(ItemContainer container, int slotID) {
      Item item = container.getItem(slotID);
      return item != null ? this.itemManager.getItemStats(item.getId()) : null;
   }

   @VisibleForTesting
   String buildStatBonusString(ItemStats s) {
      ItemStats other = null;
      ItemStats offHand = null;
      ItemEquipmentStats currentEquipment = s.getEquipment();
      ItemContainer c = this.client.getItemContainer(94);
      ItemEquipmentStats e;
      if (s.isEquipable() && currentEquipment != null && c != null) {
         int slot = currentEquipment.getSlot();
         other = this.getItemStatsFromContainer(c, slot);
         if (other == null && slot == EquipmentInventorySlot.SHIELD.getSlotIdx()) {
            other = this.getItemStatsFromContainer(c, EquipmentInventorySlot.WEAPON.getSlotIdx());
            if (other != null) {
               e = other.getEquipment();
               if (e != null) {
                  other = e.isTwoHanded() ? subtract(other, UNARMED) : null;
               }
            }
         }

         if (slot == EquipmentInventorySlot.WEAPON.getSlotIdx()) {
            if (other == null) {
               other = UNARMED;
            }

            if (currentEquipment.isTwoHanded()) {
               offHand = this.getItemStatsFromContainer(c, EquipmentInventorySlot.SHIELD.getSlotIdx());
            }
         }
      }

      ItemStats subtracted = subtract(subtract(s, other), offHand);
      e = subtracted.getEquipment();
      StringBuilder b = new StringBuilder();
      if (this.config.showWeight()) {
         double sw = this.config.alwaysShowBaseStats() ? subtracted.getWeight() : s.getWeight();
         b.append(this.buildStatRow("Weight", s.getWeight(), sw, true, false, s.isEquipable()));
      }

      if (subtracted.isEquipable() && e != null) {
         b.append(this.buildStatRow("Prayer", (double)currentEquipment.getPrayer(), (double)e.getPrayer(), false, false));
         b.append(this.buildStatRow("Speed", (double)currentEquipment.getAspeed(), (double)e.getAspeed(), true, false));
         b.append(this.buildStatRow("Melee Str", (double)currentEquipment.getStr(), (double)e.getStr(), false, false));
         b.append(this.buildStatRow("Range Str", (double)currentEquipment.getRstr(), (double)e.getRstr(), false, false));
         b.append(this.buildStatRow("Magic Dmg", (double)currentEquipment.getMdmg(), (double)e.getMdmg(), false, true));
         StringBuilder abb = new StringBuilder();
         abb.append(this.buildStatRow("Stab", (double)currentEquipment.getAstab(), (double)e.getAstab(), false, false));
         abb.append(this.buildStatRow("Slash", (double)currentEquipment.getAslash(), (double)e.getAslash(), false, false));
         abb.append(this.buildStatRow("Crush", (double)currentEquipment.getAcrush(), (double)e.getAcrush(), false, false));
         abb.append(this.buildStatRow("Magic", (double)currentEquipment.getAmagic(), (double)e.getAmagic(), false, false));
         abb.append(this.buildStatRow("Range", (double)currentEquipment.getArange(), (double)e.getArange(), false, false));
         if (abb.length() > 0) {
            b.append(ColorUtil.wrapWithColorTag("Attack Bonus</br>", JagexColors.MENU_TARGET)).append(abb);
         }

         StringBuilder dbb = new StringBuilder();
         dbb.append(this.buildStatRow("Stab", (double)currentEquipment.getDstab(), (double)e.getDstab(), false, false));
         dbb.append(this.buildStatRow("Slash", (double)currentEquipment.getDslash(), (double)e.getDslash(), false, false));
         dbb.append(this.buildStatRow("Crush", (double)currentEquipment.getDcrush(), (double)e.getDcrush(), false, false));
         dbb.append(this.buildStatRow("Magic", (double)currentEquipment.getDmagic(), (double)e.getDmagic(), false, false));
         dbb.append(this.buildStatRow("Range", (double)currentEquipment.getDrange(), (double)e.getDrange(), false, false));
         if (dbb.length() > 0) {
            b.append(ColorUtil.wrapWithColorTag("Defence Bonus</br>", JagexColors.MENU_TARGET)).append(dbb);
         }
      }

      return b.toString();
   }

   private static ItemStats subtract(ItemStats one, ItemStats two) {
      if (two == null) {
         return one;
      } else {
         double newWeight = one.getWeight() - two.getWeight();
         ItemEquipmentStats newEquipment;
         if (two.getEquipment() != null) {
            ItemEquipmentStats equipment = one.getEquipment() != null ? one.getEquipment() : ItemEquipmentStats.builder().build();
            newEquipment = ItemEquipmentStats.builder().slot(equipment.getSlot()).astab(equipment.getAstab() - two.getEquipment().getAstab()).aslash(equipment.getAslash() - two.getEquipment().getAslash()).acrush(equipment.getAcrush() - two.getEquipment().getAcrush()).amagic(equipment.getAmagic() - two.getEquipment().getAmagic()).arange(equipment.getArange() - two.getEquipment().getArange()).dstab(equipment.getDstab() - two.getEquipment().getDstab()).dslash(equipment.getDslash() - two.getEquipment().getDslash()).dcrush(equipment.getDcrush() - two.getEquipment().getDcrush()).dmagic(equipment.getDmagic() - two.getEquipment().getDmagic()).drange(equipment.getDrange() - two.getEquipment().getDrange()).str(equipment.getStr() - two.getEquipment().getStr()).rstr(equipment.getRstr() - two.getEquipment().getRstr()).mdmg(equipment.getMdmg() - two.getEquipment().getMdmg()).prayer(equipment.getPrayer() - two.getEquipment().getPrayer()).aspeed(equipment.getAspeed() - two.getEquipment().getAspeed()).build();
         } else {
            newEquipment = one.getEquipment();
         }

         return new ItemStats(one.isEquipable(), newWeight, 0, newEquipment);
      }
   }

   private String buildStatChangeString(StatChange c) {
      StringBuilder b = new StringBuilder();
      b.append(ColorUtil.colorTag(Positivity.getColor(this.config, c.getPositivity())));
      if (this.config.relative()) {
         b.append(c.getFormattedRelative());
      }

      if (this.config.theoretical()) {
         if (this.config.relative()) {
            b.append('/');
         }

         b.append(c.getFormattedTheoretical());
      }

      if (this.config.absolute() && (this.config.relative() || this.config.theoretical())) {
         b.append(" (");
      }

      if (this.config.absolute()) {
         b.append(c.getAbsolute());
      }

      if (this.config.absolute() && (this.config.relative() || this.config.theoretical())) {
         b.append(')');
      }

      b.append(' ').append(c.getStat().getName());
      b.append("</br>");
      return b.toString();
   }
}
