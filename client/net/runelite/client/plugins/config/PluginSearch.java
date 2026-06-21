package net.runelite.client.plugins.config;

import com.google.common.base.Splitter;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import net.runelite.client.util.Text;
import org.apache.commons.lang3.StringUtils;

class PluginSearch {
   private static final Splitter SPLITTER = Splitter.on(" ").trimResults().omitEmptyStrings();

   public static <T extends SearchablePlugin> List<T> search(Collection<T> searchablePlugins, String query) {
      return (List)searchablePlugins.stream().filter((plugin) -> {
         return Text.matchesSearchTerms(SPLITTER.split(query.toLowerCase()), plugin.getKeywords());
      }).sorted(comparator(query)).collect(Collectors.toList());
   }

   private static Comparator<SearchablePlugin> comparator(String query) {
      if (StringUtils.isBlank(query)) {
         return Comparator.comparing(SearchablePlugin::isPinned, Comparator.reverseOrder()).thenComparing(SearchablePlugin::getSearchableName);
      } else {
         Iterable<String> queryPieces = SPLITTER.split(query.toLowerCase());
         return Comparator.comparing(SearchablePlugin::isPinned).thenComparing((sp) -> {
            return query.equalsIgnoreCase(sp.getSearchableName());
         }).thenComparing((sp) -> {
            return stream(queryPieces).anyMatch((queryPiece) -> {
               return stream(SPLITTER.split(sp.getSearchableName().toLowerCase())).anyMatch((namePiece) -> {
                  return namePiece.startsWith(queryPiece);
               });
            });
         }).thenComparing((sp) -> {
            return stream(queryPieces).allMatch((queryPiece) -> {
               return stream(SPLITTER.split(sp.getSearchableName().toLowerCase())).anyMatch((namePiece) -> {
                  return namePiece.contains(queryPiece);
               });
            });
         }).thenComparingInt(SearchablePlugin::installs).reversed().thenComparing(SearchablePlugin::getSearchableName);
      }
   }

   private static Stream<String> stream(Iterable<String> iterable) {
      return StreamSupport.stream(iterable.spliterator(), false);
   }
}
