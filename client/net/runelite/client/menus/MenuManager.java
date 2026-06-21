package net.runelite.client.menus;

import com.google.common.base.Preconditions;
import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.function.Consumer;
import javax.inject.Inject;
import javax.inject.Singleton;
import net.runelite.api.Client;
import net.runelite.api.MenuAction;
import net.runelite.api.MenuEntry;
import net.runelite.api.events.MenuEntryAdded;
import net.runelite.api.events.PlayerMenuOptionsChanged;
import net.runelite.client.eventbus.EventBus;
import net.runelite.client.eventbus.Subscribe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
public class MenuManager {
   private static final Logger log = LoggerFactory.getLogger(MenuManager.class);
   private static final int IDX_LOWER = 4;
   private static final int IDX_UPPER = 8;
   private final Client client;
   private final Map<Integer, String> playerMenuIndexMap = new HashMap();
   private final Multimap<Integer, WidgetMenuOption> managedMenuOptions = LinkedHashMultimap.create();

   @Inject
   private MenuManager(Client client, EventBus eventBus) {
      this.client = client;
      eventBus.register(this);
   }

   public void addManagedCustomMenu(WidgetMenuOption customMenuOption, Consumer<MenuEntry> callback) {
      int[] var3 = customMenuOption.getWidgetIds();
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         int id = var3[var5];
         this.managedMenuOptions.put(id, customMenuOption);
      }

      customMenuOption.callback = callback;
   }

   public void removeManagedCustomMenu(WidgetMenuOption customMenuOption) {
      int[] var2 = customMenuOption.getWidgetIds();
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         int id = var2[var4];
         this.managedMenuOptions.remove(id, customMenuOption);
      }

   }

   private static boolean menuContainsCustomMenu(MenuEntry[] menuEntries, WidgetMenuOption customMenuOption) {
      MenuEntry[] var2 = menuEntries;
      int var3 = menuEntries.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         MenuEntry menuEntry = var2[var4];
         String option = menuEntry.getOption();
         String target = menuEntry.getTarget();
         if (option.equals(customMenuOption.getMenuOption()) && target.equals(customMenuOption.getMenuTarget())) {
            return true;
         }
      }

      return false;
   }

   @Subscribe
   public void onMenuEntryAdded(MenuEntryAdded event) {
      if (!this.client.isWidgetSelected() && event.getType() == MenuAction.CC_OP.getId()) {
         int widgetId = event.getActionParam1();
         Collection<WidgetMenuOption> options = this.managedMenuOptions.get(widgetId);
         if (!options.isEmpty()) {
            MenuEntry[] menuEntries = this.client.getMenuEntries();
            int insertIdx = -1;
            Iterator var6 = options.iterator();

            while(var6.hasNext()) {
               WidgetMenuOption currentMenu = (WidgetMenuOption)var6.next();
               if (menuContainsCustomMenu(menuEntries, currentMenu)) {
                  return;
               }

               this.client.createMenuEntry(insertIdx--).setOption(currentMenu.getMenuOption()).setTarget(currentMenu.getMenuTarget()).setType(MenuAction.RUNELITE).setParam1(widgetId).onClick(currentMenu.callback);
            }

         }
      }
   }

   public void addPlayerMenuItem(String menuText) {
      Preconditions.checkNotNull(menuText);
      int playerMenuIndex = this.findEmptyPlayerMenuIndex();
      if (playerMenuIndex != 8) {
         this.addPlayerMenuItem(playerMenuIndex, menuText);
      }
   }

   public void removePlayerMenuItem(String menuText) {
      Preconditions.checkNotNull(menuText);
      Iterator var2 = this.playerMenuIndexMap.entrySet().iterator();

      while(var2.hasNext()) {
         Map.Entry<Integer, String> entry = (Map.Entry)var2.next();
         if (((String)entry.getValue()).equalsIgnoreCase(menuText)) {
            this.removePlayerMenuItem((Integer)entry.getKey());
            break;
         }
      }

   }

   @Subscribe
   public void onPlayerMenuOptionsChanged(PlayerMenuOptionsChanged event) {
      int idx = event.getIndex();
      String menuText = (String)this.playerMenuIndexMap.get(idx);
      if (menuText != null) {
         int newIdx = this.findEmptyPlayerMenuIndex();
         if (newIdx == 8) {
            log.debug("Client has updated player menu index {} where option {} was, and there are no more free slots available", idx, menuText);
         } else {
            log.debug("Client has updated player menu index {} where option {} was, moving to index {}", new Object[]{idx, menuText, newIdx});
            this.playerMenuIndexMap.remove(idx);
            this.addPlayerMenuItem(newIdx, menuText);
         }
      }
   }

   private void addPlayerMenuItem(int playerOptionIndex, String menuText) {
      this.client.getPlayerOptions()[playerOptionIndex] = menuText;
      this.client.getPlayerOptionsPriorities()[playerOptionIndex] = true;
      this.client.getPlayerMenuTypes()[playerOptionIndex] = MenuAction.RUNELITE_PLAYER.getId();
      this.playerMenuIndexMap.put(playerOptionIndex, menuText);
   }

   private void removePlayerMenuItem(int playerOptionIndex) {
      this.client.getPlayerOptions()[playerOptionIndex] = null;
      this.playerMenuIndexMap.remove(playerOptionIndex);
   }

   private int findEmptyPlayerMenuIndex() {
      int index = 4;

      for(String[] playerOptions = this.client.getPlayerOptions(); index < 8 && playerOptions[index] != null; ++index) {
      }

      return index;
   }
}
