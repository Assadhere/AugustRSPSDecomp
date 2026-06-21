package net.runelite.client.plugins.boosts;

import java.awt.Color;
import java.awt.image.BufferedImage;
import net.runelite.api.Client;
import net.runelite.api.Skill;
import net.runelite.client.ui.overlay.infobox.InfoBox;
import net.runelite.client.ui.overlay.infobox.InfoBoxPriority;

class BoostIndicator extends InfoBox {
   private final BoostsPlugin plugin;
   private final BoostsConfig config;
   private final Client client;
   private final Skill skill;

   BoostIndicator(Skill skill, BufferedImage image, BoostsPlugin plugin, Client client, BoostsConfig config) {
      super(image, plugin);
      this.plugin = plugin;
      this.config = config;
      this.client = client;
      this.skill = skill;
      this.setTooltip(skill.getName() + " boost");
      this.setPriority(InfoBoxPriority.HIGH);
   }

   public String getText() {
      if (!this.config.useRelativeBoost()) {
         return String.valueOf(this.client.getBoostedSkillLevel(this.skill));
      } else {
         int boost = this.client.getBoostedSkillLevel(this.skill) - this.client.getRealSkillLevel(this.skill);
         String text = String.valueOf(boost);
         if (boost > 0) {
            text = "+" + text;
         }

         return text;
      }
   }

   public Color getTextColor() {
      int boosted = this.client.getBoostedSkillLevel(this.skill);
      int base = this.client.getRealSkillLevel(this.skill);
      if (boosted < base) {
         return new Color(238, 51, 51);
      } else {
         return boosted - base <= this.config.boostThreshold() ? Color.YELLOW : Color.GREEN;
      }
   }

   public boolean render() {
      return this.plugin.getSkillsToDisplay().contains(this.getSkill()) && this.config.displayInfoboxes();
   }

   public String getName() {
      return "Boost " + this.skill.getName();
   }

   public Skill getSkill() {
      return this.skill;
   }
}
