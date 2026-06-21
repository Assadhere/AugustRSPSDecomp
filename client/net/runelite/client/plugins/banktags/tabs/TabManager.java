package net.runelite.client.plugins.banktags.tabs;

import com.google.common.base.MoreObjects;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.inject.Inject;
import javax.inject.Singleton;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.util.Text;
import org.apache.commons.lang3.math.NumberUtils;

@Singleton
public class TabManager {
   private final List<TagTab> tabs = new ArrayList();
   private final ConfigManager configManager;

   @Inject
   private TabManager(ConfigManager configManager) {
      this.configManager = configManager;
   }

   public void add(TagTab tagTab) {
      if (!this.contains(tagTab.getTag())) {
         this.tabs.add(tagTab);
      }

   }

   void clear() {
      this.tabs.clear();
   }

   public TagTab find(String tag) {
      Optional<TagTab> first = this.tabs.stream().filter((t) -> {
         return t.getTag().equals(Text.standardize(tag));
      }).findAny();
      return (TagTab)first.orElse((Object)null);
   }

   List<String> loadAllTabNames() {
      return Text.fromCSV((String)MoreObjects.firstNonNull(this.configManager.getConfiguration("banktags", "tagtabs"), ""));
   }

   TagTab load(String tag) {
      TagTab tagTab = this.find(tag);
      if (tagTab == null) {
         tag = Text.standardize(tag);
         String item = this.configManager.getConfiguration("banktags", "icon_" + tag);
         int itemid = NumberUtils.toInt(item, 952);
         tagTab = new TagTab(itemid, tag);
      }

      return tagTab;
   }

   private void save(TagTab tab) {
      this.setIcon(tab.getTag(), tab.getIconItemId());
   }

   void swap(String tagToMove, String tagDestination) {
      tagToMove = Text.standardize(tagToMove);
      tagDestination = Text.standardize(tagDestination);
      if (this.contains(tagToMove) && this.contains(tagDestination)) {
         Collections.swap(this.tabs, this.indexOf(tagToMove), this.indexOf(tagDestination));
      }

   }

   void insert(String tagToMove, String tagDestination) {
      tagToMove = Text.standardize(tagToMove);
      tagDestination = Text.standardize(tagDestination);
      if (this.contains(tagToMove) && this.contains(tagDestination)) {
         this.tabs.add(this.indexOf(tagDestination), (TagTab)this.tabs.remove(this.indexOf(tagToMove)));
      }

   }

   public void remove(String tag) {
      TagTab tagTab = this.find(tag);
      if (tagTab != null) {
         this.tabs.remove(tagTab);
         this.removeIcon(tag);
      }

   }

   public void save() {
      String tags = Text.toCSV((Collection)this.tabs.stream().map(TagTab::getTag).collect(Collectors.toList()));
      this.configManager.setConfiguration("banktags", "tagtabs", tags);
      Iterator var2 = this.tabs.iterator();

      while(var2.hasNext()) {
         TagTab tab = (TagTab)var2.next();
         this.save(tab);
      }

   }

   private void removeIcon(String tag) {
      this.configManager.unsetConfiguration("banktags", "icon_" + Text.standardize(tag));
   }

   private void setIcon(String tag, int itemId) {
      this.configManager.setConfiguration("banktags", "icon_" + Text.standardize(tag), (Object)itemId);
   }

   int size() {
      return this.tabs.size();
   }

   private boolean contains(String tag) {
      return this.tabs.stream().anyMatch((t) -> {
         return t.getTag().equals(tag);
      });
   }

   private int indexOf(TagTab tagTab) {
      return this.tabs.indexOf(tagTab);
   }

   private int indexOf(String tag) {
      return this.indexOf(this.find(tag));
   }

   public List<TagTab> getTabs() {
      return this.tabs;
   }
}
