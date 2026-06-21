package net.runelite.client.plugins.boosts;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.util.Iterator;
import java.util.Set;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.Skill;
import net.runelite.client.ui.overlay.OverlayPanel;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.components.LineComponent;
import net.runelite.client.util.ColorUtil;

class BoostsOverlay extends OverlayPanel {
   private final Client client;
   private final BoostsConfig config;
   private final BoostsPlugin plugin;

   @Inject
   private BoostsOverlay(Client client, BoostsConfig config, BoostsPlugin plugin) {
      super(plugin);
      this.plugin = plugin;
      this.client = client;
      this.config = config;
      this.setPosition(OverlayPosition.TOP_LEFT);
      this.setPriority(0.5F);
   }

   public Dimension render(Graphics2D graphics) {
      Set<Skill> boostedSkills = this.plugin.getSkillsToDisplay();
      if (!this.config.displayPanel()) {
         return null;
      } else {
         int nextChange = this.plugin.getChangeDownTicks();
         if (nextChange != -1) {
            this.panelComponent.getChildren().add(LineComponent.builder().left("Next + restore in").right(String.valueOf(this.plugin.getChangeTime(nextChange))).build());
         }

         nextChange = this.plugin.getChangeUpTicks();
         if (nextChange != -1) {
            this.panelComponent.getChildren().add(LineComponent.builder().left("Next - restore in").right(String.valueOf(this.plugin.getChangeTime(nextChange))).build());
         }

         Skill skill;
         Color strColor;
         String str;
         for(Iterator var4 = boostedSkills.iterator(); var4.hasNext(); this.panelComponent.getChildren().add(LineComponent.builder().left(skill.getName()).right(str).rightColor(strColor).build())) {
            skill = (Skill)var4.next();
            int boosted = this.client.getBoostedSkillLevel(skill);
            int base = this.client.getRealSkillLevel(skill);
            int boost = boosted - base;
            strColor = this.getTextColor(boost);
            if (this.config.useRelativeBoost()) {
               str = String.valueOf(boost);
               if (boost > 0) {
                  str = "+" + str;
               }
            } else {
               String var10000 = ColorUtil.prependColorTag(Integer.toString(boosted), strColor);
               str = var10000 + ColorUtil.prependColorTag("/" + base, Color.WHITE);
            }
         }

         return super.render(graphics);
      }
   }

   private Color getTextColor(int boost) {
      if (boost < 0) {
         return new Color(238, 51, 51);
      } else {
         return boost <= this.config.boostThreshold() ? Color.YELLOW : Color.GREEN;
      }
   }
}
