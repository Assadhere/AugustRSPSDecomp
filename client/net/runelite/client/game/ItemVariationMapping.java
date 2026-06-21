package net.runelite.client.game;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;

public class ItemVariationMapping {
   private static final Map<Integer, Integer> MAPPINGS;
   private static final Multimap<Integer, Integer> INVERTED_MAPPINGS;

   public static int map(int itemId) {
      return (Integer)MAPPINGS.getOrDefault(itemId, itemId);
   }

   public static Collection<Integer> getVariations(int itemId) {
      return (Collection)INVERTED_MAPPINGS.asMap().getOrDefault(itemId, Collections.singletonList(itemId));
   }

   static {
      Gson gson = new Gson();
      TypeToken<Map<String, Collection<Integer>>> typeToken = new TypeToken<Map<String, Collection<Integer>>>() {
      };

      Map itemVariations;
      try {
         InputStream geLimitData = ItemVariationMapping.class.getResourceAsStream("/item_variations.json");

         try {
            itemVariations = (Map)gson.fromJson(new InputStreamReader(geLimitData, StandardCharsets.UTF_8), typeToken.getType());
         } catch (Throwable var11) {
            if (geLimitData != null) {
               try {
                  geLimitData.close();
               } catch (Throwable var10) {
                  var11.addSuppressed(var10);
               }
            }

            throw var11;
         }

         if (geLimitData != null) {
            geLimitData.close();
         }
      } catch (IOException var12) {
         IOException e = var12;
         throw new RuntimeException(e);
      }

      ImmutableMap.Builder<Integer, Integer> builder = new ImmutableMap.Builder();
      ImmutableMultimap.Builder<Integer, Integer> invertedBuilder = new ImmutableMultimap.Builder();
      Iterator var5 = itemVariations.values().iterator();

      while(var5.hasNext()) {
         Collection<Integer> value = (Collection)var5.next();
         Iterator<Integer> iterator = value.iterator();
         int base = (Integer)iterator.next();

         while(iterator.hasNext()) {
            int id = (Integer)iterator.next();
            builder.put(id, base);
            invertedBuilder.put(base, id);
         }

         invertedBuilder.put(base, base);
      }

      INVERTED_MAPPINGS = invertedBuilder.build();
      MAPPINGS = builder.build();
   }
}
