package net.runelite.client.plugins.banktags;

import com.google.common.base.Strings;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.inject.Inject;
import javax.inject.Singleton;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.game.ItemManager;
import net.runelite.client.game.ItemVariationMapping;
import net.runelite.client.util.Text;

@Singleton
public class TagManager {
   private final ConfigManager configManager;
   private final ItemManager itemManager;
   private final Map<String, BankTag> customTags = new HashMap();

   @Inject
   private TagManager(ItemManager itemManager, ConfigManager configManager) {
      this.itemManager = itemManager;
      this.configManager = configManager;
   }

   String getTagString(int itemId, boolean variation) {
      itemId = this.getItemId(itemId, variation);
      String config = this.configManager.getConfiguration("banktags", "item_" + itemId);
      return config == null ? "" : config;
   }

   Collection<String> getTags(int itemId, boolean variation) {
      return new LinkedHashSet(Text.fromCSV(this.getTagString(itemId, variation).toLowerCase()));
   }

   void setTagString(int itemId, String tags, boolean variation) {
      itemId = this.getItemId(itemId, variation);
      if (Strings.isNullOrEmpty(tags)) {
         this.configManager.unsetConfiguration("banktags", "item_" + itemId);
      } else {
         this.configManager.setConfiguration("banktags", "item_" + itemId, tags);
      }

   }

   public void addTags(int itemId, Collection<String> t, boolean variation) {
      Collection<String> tags = this.getTags(itemId, variation);
      if (tags.addAll(t)) {
         this.setTags(itemId, tags, variation);
      }

   }

   public void addTag(int itemId, String tag, boolean variation) {
      Collection<String> tags = this.getTags(itemId, variation);
      if (tags.add(Text.standardize(tag))) {
         this.setTags(itemId, tags, variation);
      }

   }

   private void setTags(int itemId, Collection<String> tags, boolean variation) {
      this.setTagString(itemId, Text.toCSV(tags), variation);
   }

   boolean findTag(int itemId, String search) {
      Collection<String> tags = this.getTags(itemId, false);
      tags.addAll(this.getTags(itemId, true));
      return tags.stream().anyMatch((tag) -> {
         return tag.startsWith(Text.standardize(search));
      });
   }

   public List<Integer> getItemsForTag(String tag) {
      String prefix = "banktags.item_";
      return (List)this.configManager.getConfigurationKeys("banktags.item_").stream().map((item) -> {
         return Integer.parseInt(item.replace("banktags.item_", ""));
      }).filter((item) -> {
         return this.getTags(item, false).contains(tag) || this.getTags(item, true).contains(tag);
      }).collect(Collectors.toList());
   }

   public void removeTag(String tag) {
      String prefix = "banktags.item_";
      this.configManager.getConfigurationKeys("banktags.item_").forEach((item) -> {
         int id = Integer.parseInt(item.replace("banktags.item_", ""));
         this.removeTag(id, tag);
      });
      this.setHidden(tag, false);
   }

   public void removeTag(int itemId, String tag) {
      Collection<String> tags = this.getTags(itemId, false);
      if (tags.remove(Text.standardize(tag))) {
         this.setTags(itemId, tags, false);
      }

      tags = this.getTags(itemId, true);
      if (tags.remove(Text.standardize(tag))) {
         this.setTags(itemId, tags, true);
      }

   }

   public void renameTag(String oldTag, String newTag) {
      List<Integer> items = this.getItemsForTag(Text.standardize(oldTag));
      items.forEach((id) -> {
         Collection<String> tags = this.getTags(id, id < 0);
         tags.remove(Text.standardize(oldTag));
         tags.add(Text.standardize(newTag));
         this.setTags(id, tags, id < 0);
      });
   }

   public boolean isHidden(String tag) {
      return Boolean.TRUE.equals(this.configManager.getConfiguration("banktags", "hidden_" + Text.standardize(tag), (Type)Boolean.class));
   }

   public void setHidden(String tag, boolean hidden) {
      if (hidden) {
         this.configManager.setConfiguration("banktags", "hidden_" + Text.standardize(tag), (Object)true);
      } else {
         this.configManager.unsetConfiguration("banktags", "hidden_" + Text.standardize(tag));
      }

   }

   private int getItemId(int itemId, boolean variation) {
      itemId = Math.abs(itemId);
      itemId = this.itemManager.canonicalize(itemId);
      if (variation) {
         itemId = ItemVariationMapping.map(itemId) * -1;
      }

      return itemId;
   }

   public void registerTag(String name, BankTag tag) {
      this.customTags.put(name, tag);
   }

   public void unregisterTag(String name) {
      this.customTags.remove(name);
   }

   BankTag findTag(String name) {
      return (BankTag)this.customTags.get(name);
   }
}
