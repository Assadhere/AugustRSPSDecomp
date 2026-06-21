package net.runelite.client.plugins.specialcounter;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import net.runelite.client.ui.overlay.infobox.Counter;

class SpecialCounter extends Counter {
   private final SpecialWeapon weapon;
   private final SpecialCounterConfig config;
   private final Map<String, Integer> partySpecs = new HashMap();

   SpecialCounter(BufferedImage image, SpecialCounterPlugin plugin, SpecialCounterConfig config, int hitValue, SpecialWeapon weapon) {
      super(image, plugin, hitValue);
      this.weapon = weapon;
      this.config = config;
   }

   void addHits(int hit) {
      int count = this.getCount();
      this.setCount(count + hit);
   }

   public String getTooltip() {
      int hitValue = this.getCount();
      if (this.partySpecs.isEmpty()) {
         return this.buildTooltip(hitValue);
      } else {
         StringBuilder stringBuilder = new StringBuilder();
         stringBuilder.append(this.buildTooltip(hitValue));
         Iterator var3 = this.partySpecs.entrySet().iterator();

         while(var3.hasNext()) {
            Map.Entry<String, Integer> entry = (Map.Entry)var3.next();
            stringBuilder.append("</br>").append(entry.getKey() == null ? "You" : (String)entry.getKey()).append(": ").append(this.buildTooltip((Integer)entry.getValue()));
         }

         return stringBuilder.toString();
      }
   }

   private String buildTooltip(int hitValue) {
      String var10000;
      if (!this.weapon.isDamage()) {
         if (hitValue == 1) {
            var10000 = this.weapon.getName();
            return var10000 + " special has hit " + hitValue + " time.";
         } else {
            var10000 = this.weapon.getName();
            return var10000 + " special has hit " + hitValue + " times.";
         }
      } else {
         var10000 = this.weapon.getName();
         return var10000 + " special has hit " + hitValue + " total.";
      }
   }

   public Color getTextColor() {
      int threshold = (Integer)this.weapon.getThreshold().apply(this.config);
      if (threshold > 0) {
         int count = this.getCount();
         return count >= threshold ? Color.GREEN : Color.RED;
      } else {
         return super.getTextColor();
      }
   }

   Map<String, Integer> getPartySpecs() {
      return this.partySpecs;
   }
}
