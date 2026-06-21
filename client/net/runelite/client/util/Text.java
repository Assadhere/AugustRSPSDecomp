package net.runelite.client.util;

import com.google.common.base.CharMatcher;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.text.WordUtils;
import org.apache.commons.text.similarity.JaroWinklerDistance;

public class Text {
   private static final JaroWinklerDistance DISTANCE = new JaroWinklerDistance();
   private static final Pattern TAG_REGEXP = Pattern.compile("<[^>]*>");
   private static final Splitter COMMA_SPLITTER = Splitter.on(",").omitEmptyStrings().trimResults();
   private static final Joiner COMMA_JOINER = Joiner.on(",").skipNulls();
   public static final CharMatcher JAGEX_PRINTABLE_CHAR_MATCHER = new JagexPrintableCharMatcher();

   public static List<String> fromCSV(String input) {
      return COMMA_SPLITTER.splitToList(input);
   }

   public static String toCSV(Collection<String> input) {
      return COMMA_JOINER.join(input);
   }

   public static String removeTags(String str) {
      return TAG_REGEXP.matcher(str).replaceAll("");
   }

   public static String removeFormattingTags(String str) {
      StringBuilder sb = new StringBuilder();
      Matcher matcher = TAG_REGEXP.matcher(str);

      while(matcher.find()) {
         matcher.appendReplacement(sb, "");
         switch (matcher.group(0)) {
            case "<lt>":
            case "<gt>":
               sb.append(match);
         }
      }

      matcher.appendTail(sb);
      return sb.toString();
   }

   public static String standardize(String str) {
      return removeTags(str).replace(' ', ' ').trim().toLowerCase();
   }

   public static String toJagexName(String str) {
      return CharMatcher.ascii().retainFrom(str.replaceAll("[ _-]", " ")).trim();
   }

   public static String sanitizeMultilineText(String str) {
      return removeTags(str.replace("-<br>", "-").replace("<br>", " ").replaceAll("[ ]+", " "));
   }

   public static String escapeJagex(String str) {
      StringBuilder out = new StringBuilder(str.length());

      for(int i = 0; i < str.length(); ++i) {
         char c = str.charAt(i);
         if (c == '<') {
            out.append("<lt>");
         } else if (c == '>') {
            out.append("<gt>");
         } else if (c == '\n') {
            out.append("<br>");
         } else if (c != '\r') {
            out.append(c);
         }
      }

      return out.toString();
   }

   public static String sanitize(String name) {
      return name.replace(' ', ' ');
   }

   public static String titleCase(Enum o) {
      String toString = o.toString();
      return o.name().equals(toString) ? WordUtils.capitalize(toString.toLowerCase(), new char[]{'_'}).replace("_", " ") : toString;
   }

   public static boolean matchesSearchTerms(Iterable<String> searchTerms, Collection<String> keywords) {
      Iterator var2 = searchTerms.iterator();

      String term;
      do {
         if (!var2.hasNext()) {
            return true;
         }

         term = (String)var2.next();
      } while(!keywords.stream().noneMatch((t) -> {
         return t.contains(term) || DISTANCE.apply(t, term) > 0.9;
      }));

      return false;
   }
}
