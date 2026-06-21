package net.runelite.client.game;

import java.awt.image.BufferedImage;
import javax.inject.Singleton;
import net.runelite.api.Skill;
import net.runelite.client.util.ImageUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
public class SkillIconManager {
   private static final Logger log = LoggerFactory.getLogger(SkillIconManager.class);
   private final BufferedImage[] imgCache = new BufferedImage[Skill.values().length * 2];

   public BufferedImage getSkillImage(Skill skill, boolean small) {
      int skillIdx = skill.ordinal() + (small ? Skill.values().length : 0);
      if (this.imgCache[skillIdx] != null) {
         return this.imgCache[skillIdx];
      } else {
         String skillIconPath = (small ? "/skill_icons_small/" : "/skill_icons/") + skill.getName().toLowerCase() + ".png";
         log.debug("Loading skill icon from {}", skillIconPath);
         BufferedImage skillImage = ImageUtil.loadImageResource(this.getClass(), skillIconPath);
         this.imgCache[skillIdx] = skillImage;
         return skillImage;
      }
   }

   public BufferedImage getSkillImage(Skill skill) {
      return this.getSkillImage(skill, false);
   }
}
