package net.runelite.client.plugins.achievementdiary;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.FontTypeFace;
import net.runelite.api.events.WidgetLoaded;
import net.runelite.api.widgets.Widget;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.achievementdiary.diaries.ArdougneDiaryRequirement;
import net.runelite.client.plugins.achievementdiary.diaries.DesertDiaryRequirement;
import net.runelite.client.plugins.achievementdiary.diaries.FaladorDiaryRequirement;
import net.runelite.client.plugins.achievementdiary.diaries.FremennikDiaryRequirement;
import net.runelite.client.plugins.achievementdiary.diaries.KandarinDiaryRequirement;
import net.runelite.client.plugins.achievementdiary.diaries.KaramjaDiaryRequirement;
import net.runelite.client.plugins.achievementdiary.diaries.KourendDiaryRequirement;
import net.runelite.client.plugins.achievementdiary.diaries.LumbridgeDiaryRequirement;
import net.runelite.client.plugins.achievementdiary.diaries.MorytaniaDiaryRequirement;
import net.runelite.client.plugins.achievementdiary.diaries.VarrockDiaryRequirement;
import net.runelite.client.plugins.achievementdiary.diaries.WesternDiaryRequirement;
import net.runelite.client.plugins.achievementdiary.diaries.WildernessDiaryRequirement;
import net.runelite.client.util.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@PluginDescriptor(
   name = "Diary Requirements",
   description = "Display level requirements in Achievement Diary interface",
   tags = {"achievements", "tasks"}
)
public class DiaryRequirementsPlugin extends Plugin {
   private static final Logger log = LoggerFactory.getLogger(DiaryRequirementsPlugin.class);
   private static final String AND_JOINER = ", ";
   private static final Pattern AND_JOINER_PATTERN = Pattern.compile("(?<=, )");
   @Inject
   private Client client;
   @Inject
   private ClientThread clientThread;

   @Subscribe
   public void onWidgetLoaded(WidgetLoaded event) {
      if (event.getGroupId() == 741) {
         String widgetTitle = Text.removeTags(this.client.getWidget(48562178).getText()).replace(' ', '_').toUpperCase();
         if (widgetTitle.startsWith("ACHIEVEMENT_DIARY")) {
            this.showDiaryRequirements();
         }
      }

   }

   private void showDiaryRequirements() {
      Widget widget = this.client.getWidget(48562179);
      Widget[] children = widget.getStaticChildren();
      Widget titleWidget = children[0];
      if (titleWidget != null) {
         FontTypeFace font = titleWidget.getFont();
         int maxWidth = titleWidget.getWidth();
         List<String> originalAchievements = this.getOriginalAchievements(children);
         List<String> newRequirements = new ArrayList(originalAchievements);
         GenericDiaryRequirement requirements = this.getRequirementsForTitle(titleWidget.getText());
         if (requirements == null) {
            log.debug("Unknown achievement diary {}", titleWidget.getText());
         } else {
            Map<String, String> skillRequirements = this.buildRequirements(requirements.getRequirements());
            if (skillRequirements != null) {
               int offset = 0;
               String taskBuffer = "";

               int i;
               String task;
               for(i = 0; i < originalAchievements.size(); ++i) {
                  String rowText = Text.removeTags((String)originalAchievements.get(i));
                  if (skillRequirements.get(taskBuffer + " " + rowText) != null) {
                     taskBuffer = taskBuffer + " " + rowText;
                  } else {
                     taskBuffer = rowText;
                  }

                  if (skillRequirements.get(taskBuffer) != null) {
                     String levelRequirement = (String)skillRequirements.get(taskBuffer);
                     task = (String)originalAchievements.get(i);
                     int taskWidth = font.getTextWidth(task);
                     int ourWidth = font.getTextWidth(levelRequirement);
                     String strike = task.startsWith("<str>") ? "<str>" : "";
                     if (ourWidth + taskWidth < maxWidth) {
                        newRequirements.set(i + offset, task + levelRequirement);
                     } else if (ourWidth < maxWidth) {
                        ++offset;
                        newRequirements.add(i + offset, strike + levelRequirement);
                     } else {
                        StringBuilder b = new StringBuilder();
                        b.append(task);
                        int runningWidth = font.getTextWidth(b.toString());
                        String[] var21 = AND_JOINER_PATTERN.split(levelRequirement);
                        int var22 = var21.length;

                        for(int var23 = 0; var23 < var22; ++var23) {
                           String word = var21[var23];
                           int wordWidth = font.getTextWidth(word);
                           if (runningWidth != 0 && wordWidth + runningWidth >= maxWidth) {
                              newRequirements.add(i + offset++, b.toString());
                              b.delete(0, b.length());
                              runningWidth = wordWidth;
                              b.append(strike);
                              b.append(word);
                           } else {
                              runningWidth += wordWidth;
                              b.append(word);
                           }
                        }

                        newRequirements.set(i + offset, b.toString());
                     }
                  }
               }

               i = 0;

               for(int i = 0; i < newRequirements.size() && i < children.length; ++i) {
                  Widget achievementWidget = children[i];
                  task = (String)newRequirements.get(i);
                  achievementWidget.setText(task);
                  if (task != null && !task.isEmpty()) {
                     i = i;
                  }
               }

               this.clientThread.invokeLater(() -> {
                  this.client.runScript(new Object[]{6845, 1, i});
               });
            }
         }
      }
   }

   private List<String> getOriginalAchievements(Widget[] children) {
      List<String> preloadedRequirements = new ArrayList(children.length);
      Widget[] var3 = children;
      int var4 = children.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         Widget requirementWidget = var3[var5];
         preloadedRequirements.add(requirementWidget.getText());
      }

      return preloadedRequirements;
   }

   private GenericDiaryRequirement getRequirementsForTitle(String title) {
      Object diaryRequirementContainer;
      switch (Text.removeTags(title.replace(" ", "_").toUpperCase())) {
         case "ARDOUGNE_AREA_TASKS":
            diaryRequirementContainer = new ArdougneDiaryRequirement();
            break;
         case "DESERT_TASKS":
            diaryRequirementContainer = new DesertDiaryRequirement();
            break;
         case "FALADOR_AREA_TASKS":
            diaryRequirementContainer = new FaladorDiaryRequirement();
            break;
         case "FREMENNIK_TASKS":
            diaryRequirementContainer = new FremennikDiaryRequirement();
            break;
         case "KANDARIN_TASKS":
            diaryRequirementContainer = new KandarinDiaryRequirement();
            break;
         case "KARAMJA_AREA_TASKS":
            diaryRequirementContainer = new KaramjaDiaryRequirement();
            break;
         case "KOUREND_&_KEBOS_TASKS":
            diaryRequirementContainer = new KourendDiaryRequirement();
            break;
         case "LUMBRIDGE_&_DRAYNOR_TASKS":
            diaryRequirementContainer = new LumbridgeDiaryRequirement();
            break;
         case "MORYTANIA_TASKS":
            diaryRequirementContainer = new MorytaniaDiaryRequirement();
            break;
         case "VARROCK_TASKS":
            diaryRequirementContainer = new VarrockDiaryRequirement();
            break;
         case "WESTERN_AREA_TASKS":
            diaryRequirementContainer = new WesternDiaryRequirement();
            break;
         case "WILDERNESS_AREA_TASKS":
            diaryRequirementContainer = new WildernessDiaryRequirement();
            break;
         default:
            return null;
      }

      return (GenericDiaryRequirement)diaryRequirementContainer;
   }

   private Map<String, String> buildRequirements(Collection<DiaryRequirement> requirements) {
      Map<String, String> reqs = new HashMap();
      Iterator var3 = requirements.iterator();

      while(var3.hasNext()) {
         DiaryRequirement req = (DiaryRequirement)var3.next();
         StringBuilder b = new StringBuilder();
         b.append("<col=ffffff>(");

         assert !req.getRequirements().isEmpty();

         Iterator var6 = req.getRequirements().iterator();

         while(var6.hasNext()) {
            Requirement ireq = (Requirement)var6.next();
            boolean satifisfied = ireq.satisfiesRequirement(this.client);
            b.append(satifisfied ? "<col=000080><str>" : "<col=800000>");
            b.append(ireq.toString());
            b.append(satifisfied ? "</str>" : "<col=000080>");
            b.append(", ");
         }

         b.delete(b.length() - ", ".length(), b.length());
         b.append("<col=ffffff>)");
         reqs.put(req.getTask(), b.toString());
      }

      return reqs;
   }
}
