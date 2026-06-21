package net.runelite.client.plugins.xptracker;

import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import net.runelite.api.Skill;
import net.runelite.client.config.ConfigSerializer;

@ConfigSerializer(XpSaveSerializer.class)
class XpSave {
   Map<Skill, XpSaveSingle> skills = new LinkedHashMap();
   Set<Skill> compactViewSkills = EnumSet.noneOf(Skill.class);
   XpSaveSingle overall;
}
