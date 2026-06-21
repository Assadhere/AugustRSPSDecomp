package net.runelite.client.plugins.xpdrop;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.google.inject.Provides;
import java.awt.Color;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.EnumComposition;
import net.runelite.api.Prayer;
import net.runelite.api.Skill;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.ScriptPreFired;
import net.runelite.api.events.StatChanged;
import net.runelite.api.widgets.Widget;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;

@PluginDescriptor(
   name = "XP Drop",
   description = "Enable customization of the way XP drops are displayed",
   tags = {"experience", "levels", "tick", "prayer", "xpdrop"}
)
public class XpDropPlugin extends Plugin {
   private static final Multimap<Prayer, PrayerType> PRAYER_TYPE;
   @Inject
   private Client client;
   @Inject
   private XpDropConfig config;
   private int tickCounter = 0;
   private int previousExpGained;
   private boolean hasDropped = false;
   private int xpdropColor;
   private Skill lastSkill = null;
   private final Map<Skill, Integer> previousSkillExpTable = new EnumMap(Skill.class);

   @Provides
   XpDropConfig provideConfig(ConfigManager configManager) {
      return (XpDropConfig)configManager.getConfig(XpDropConfig.class);
   }

   @Subscribe
   public void onScriptPreFired(ScriptPreFired scriptPreFired) {
      if (scriptPreFired.getScriptId() == 996) {
         int[] intStack = this.client.getIntStack();
         int intStackSize = this.client.getIntStackSize();
         int widgetId = intStack[intStackSize - 4];
         this.processXpDrop(widgetId);
      }

   }

   private void processXpDrop(int widgetId) {
      Widget xpdrop = this.client.getWidget(widgetId);
      Widget[] children = xpdrop.getChildren();
      Widget text = children[0];
      Collection<PrayerType> prayers = this.getActivePrayerType();
      if (prayers.isEmpty()) {
         this.hideSkillIcons(xpdrop);
         this.resetTextColor(text);
      } else {
         List<PrayerType> xpDropTypes = (List)Arrays.stream(children).skip(1L).filter(Objects::nonNull).map(Widget::getSpriteId).map((id) -> {
            if (id != 197 && id != 198 && id != 199) {
               if (id == 200) {
                  return XpDropPlugin.PrayerType.RANGE;
               } else {
                  return id == 202 ? XpDropPlugin.PrayerType.MAGIC : null;
               }
            } else {
               return XpDropPlugin.PrayerType.MELEE;
            }
         }).filter(Objects::nonNull).distinct().collect(Collectors.toList());
         if (xpDropTypes.contains(XpDropPlugin.PrayerType.RANGE)) {
            if (prayers.contains(XpDropPlugin.PrayerType.RANGE)) {
               this.xpdropColor = this.config.getRangePrayerColor().getRGB();
            }
         } else if (xpDropTypes.contains(XpDropPlugin.PrayerType.MAGIC)) {
            if (prayers.contains(XpDropPlugin.PrayerType.MAGIC)) {
               this.xpdropColor = this.config.getMagePrayerColor().getRGB();
            }
         } else if (xpDropTypes.contains(XpDropPlugin.PrayerType.MELEE) && prayers.contains(XpDropPlugin.PrayerType.MELEE)) {
            this.xpdropColor = this.config.getMeleePrayerColor().getRGB();
         }

         if (this.xpdropColor != 0) {
            text.setTextColor(this.xpdropColor);
         } else {
            this.resetTextColor(text);
         }

         this.hideSkillIcons(xpdrop);
      }
   }

   private void resetTextColor(Widget widget) {
      Color standardColor = this.config.standardColor();
      if (standardColor != null) {
         int color = standardColor.getRGB();
         widget.setTextColor(color);
      } else {
         EnumComposition colorEnum = this.client.getEnum(1169);
         int defaultColorId = this.client.getVarbitValue(4695);
         int color = colorEnum.getIntValue(defaultColorId);
         widget.setTextColor(color);
      }

   }

   private void hideSkillIcons(Widget xpdrop) {
      if (this.config.hideSkillIcons()) {
         Widget[] children = xpdrop.getChildren();
         Arrays.fill(children, 1, children.length, (Object)null);
      }

   }

   private Collection<PrayerType> getActivePrayerType() {
      Iterator var1 = PRAYER_TYPE.keySet().iterator();

      Prayer prayer;
      do {
         if (!var1.hasNext()) {
            return Collections.emptyList();
         }

         prayer = (Prayer)var1.next();
      } while(this.client.getServerVarbitValue(prayer.getVarbit()) != 1);

      return PRAYER_TYPE.get(prayer);
   }

   @Subscribe
   public void onGameTick(GameTick tick) {
      this.xpdropColor = 0;
      int fakeTickDelay = this.config.fakeXpDropDelay();
      if (fakeTickDelay != 0 && this.lastSkill != null) {
         if (this.hasDropped) {
            this.hasDropped = false;
            this.tickCounter = 0;
         } else if (++this.tickCounter % fakeTickDelay == 0) {
            this.client.runScript(new Object[]{2091, this.lastSkill.ordinal(), this.previousExpGained});
         }
      }
   }

   @Subscribe
   public void onStatChanged(StatChanged statChanged) {
      Skill skill = statChanged.getSkill();
      int xp = statChanged.getXp();
      this.lastSkill = skill;
      Integer previous = (Integer)this.previousSkillExpTable.put(skill, xp);
      if (previous != null) {
         this.previousExpGained = xp - previous;
         this.hasDropped = true;
      }

   }

   static {
      PRAYER_TYPE = (new ImmutableMultimap.Builder()).put(Prayer.BURST_OF_STRENGTH, XpDropPlugin.PrayerType.MELEE).put(Prayer.CLARITY_OF_THOUGHT, XpDropPlugin.PrayerType.MELEE).put(Prayer.SHARP_EYE, XpDropPlugin.PrayerType.RANGE).put(Prayer.MYSTIC_WILL, XpDropPlugin.PrayerType.MAGIC).put(Prayer.SUPERHUMAN_STRENGTH, XpDropPlugin.PrayerType.MELEE).put(Prayer.IMPROVED_REFLEXES, XpDropPlugin.PrayerType.MELEE).put(Prayer.HAWK_EYE, XpDropPlugin.PrayerType.RANGE).put(Prayer.MYSTIC_LORE, XpDropPlugin.PrayerType.MAGIC).put(Prayer.ULTIMATE_STRENGTH, XpDropPlugin.PrayerType.MELEE).put(Prayer.INCREDIBLE_REFLEXES, XpDropPlugin.PrayerType.MELEE).put(Prayer.EAGLE_EYE, XpDropPlugin.PrayerType.RANGE).put(Prayer.MYSTIC_MIGHT, XpDropPlugin.PrayerType.MAGIC).put(Prayer.CHIVALRY, XpDropPlugin.PrayerType.MELEE).put(Prayer.PIETY, XpDropPlugin.PrayerType.MELEE).put(Prayer.RIGOUR, XpDropPlugin.PrayerType.RANGE).put(Prayer.AUGURY, XpDropPlugin.PrayerType.MAGIC).put(Prayer.RP_ANCIENT_STRENGTH, XpDropPlugin.PrayerType.MELEE).put(Prayer.RP_ANCIENT_SIGHT, XpDropPlugin.PrayerType.RANGE).put(Prayer.RP_ANCIENT_WILL, XpDropPlugin.PrayerType.MAGIC).putAll(Prayer.RP_TRINITAS, new PrayerType[]{XpDropPlugin.PrayerType.MELEE, XpDropPlugin.PrayerType.RANGE, XpDropPlugin.PrayerType.MAGIC}).put(Prayer.RP_DECIMATE, XpDropPlugin.PrayerType.MELEE).put(Prayer.RP_ANNIHILATE, XpDropPlugin.PrayerType.RANGE).put(Prayer.RP_VAPORISE, XpDropPlugin.PrayerType.MAGIC).putAll(Prayer.RP_INTENSIFY, new PrayerType[]{XpDropPlugin.PrayerType.MELEE, XpDropPlugin.PrayerType.RANGE, XpDropPlugin.PrayerType.MAGIC}).build();
   }

   static enum PrayerType {
      MELEE,
      RANGE,
      MAGIC;
   }
}
