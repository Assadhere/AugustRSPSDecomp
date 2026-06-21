package net.runelite.client.plugins.grandexchange;

import java.util.function.ToDoubleFunction;
import javax.inject.Singleton;
import net.runelite.api.ItemComposition;
import org.apache.commons.text.similarity.JaroWinklerDistance;
import org.apache.commons.text.similarity.LongestCommonSubsequence;
import org.apache.commons.text.similarity.SimilarityScore;

@Singleton
public class FuzzySearchScorer {
   private final SimilarityScore<Double> baseAlgorithm = new JaroWinklerDistance();

   public Double score(String query, String itemName) {
      query = query.toLowerCase().replace('-', ' ');
      itemName = itemName.toLowerCase().replace('-', ' ');
      String[] queryWords = query.split(" ");
      String[] itemWords = itemName.split(" ");
      double lcsScore = 0.0;
      String[] var7 = queryWords;
      int var8 = queryWords.length;

      for(int var9 = 0; var9 < var8; ++var9) {
         String queryWord = var7[var9];
         String[] var11 = itemWords;
         int var12 = itemWords.length;

         for(int var13 = 0; var13 < var12; ++var13) {
            String itemWord = var11[var13];
            int lcsLen = (new LongestCommonSubsequence()).longestCommonSubsequence(queryWord, itemWord).length();
            lcsScore = Math.max(lcsScore, (double)lcsLen / (double)queryWord.length());
         }
      }

      double proximityScore = Math.log10(10.0 * (Double)this.baseAlgorithm.apply(query, itemName)) - 0.5;
      return lcsScore + proximityScore - 1.0;
   }

   public ToDoubleFunction<ItemComposition> comparator(String query) {
      return (item) -> {
         return this.score(query.toLowerCase().replace('-', ' '), item.getName().toLowerCase().replace('-', ' '));
      };
   }
}
