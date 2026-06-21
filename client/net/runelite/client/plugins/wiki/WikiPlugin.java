package net.runelite.client.plugins.wiki;

import com.google.inject.Provides;
import java.util.Arrays;
import java.util.stream.Stream;
import javax.inject.Inject;
import javax.inject.Provider;
import net.runelite.api.Client;
import net.runelite.api.Menu;
import net.runelite.api.MenuAction;
import net.runelite.api.MenuEntry;
import net.runelite.api.NPC;
import net.runelite.api.NPCComposition;
import net.runelite.api.ObjectComposition;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.MenuEntryAdded;
import net.runelite.api.events.MenuOptionClicked;
import net.runelite.api.events.ScriptPostFired;
import net.runelite.api.events.WidgetLoaded;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetUtil;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.game.ItemManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.util.LinkBrowser;
import net.runelite.client.util.Text;
import okhttp3.HttpUrl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@PluginDescriptor(
   name = "Wiki",
   description = "Adds a Wiki button that takes you to the OSRS Wiki"
)
public class WikiPlugin extends Plugin {
   private static final Logger log = LoggerFactory.getLogger(WikiPlugin.class);
   static final HttpUrl WIKI_BASE = HttpUrl.get("https://oldschool.runescape.wiki");
   static final HttpUrl WIKI_API;
   static final String UTM_SOURCE_KEY = "utm_source";
   static final String UTM_SOURCE_VALUE = "runelite";
   private static final String MENUOP_WIKI = "Wiki";
   @Inject
   private WikiConfig config;
   @Inject
   private ClientThread clientThread;
   @Inject
   private Client client;
   @Inject
   private ItemManager itemManager;
   @Inject
   private Provider<WikiSearchChatboxTextInput> wikiSearchChatboxTextInputProvider;
   @Inject
   private WikiDpsManager wikiDpsManager;
   private Widget icon;
   private boolean wikiSelected = false;
   static final String CONFIG_GROUP_KEY = "wiki";

   @Provides
   WikiConfig getConfig(ConfigManager configManager) {
      return (WikiConfig)configManager.getConfig(WikiConfig.class);
   }

   public void startUp() {
      this.clientThread.invokeLater(this::addWidgets);
      this.wikiDpsManager.startUp();
   }

   public void shutDown() {
      this.clientThread.invokeLater(() -> {
         this.removeWidgets();
      });
      this.wikiDpsManager.shutDown();
   }

   private boolean removeWidgets(int wiki, int wikiIcon) {
      Widget wikiBannerParent = this.client.getWidget(wiki);
      if (wikiBannerParent == null) {
         return true;
      } else {
         Widget[] children = wikiBannerParent.getChildren();
         if (children != null && children.length >= 1) {
            children[0] = null;
            Widget vanilla = this.client.getWidget(wikiIcon);
            if (vanilla != null && this.client.getVarbitValue(10113) == 0) {
               vanilla.setHidden(false);
            }

            return false;
         } else {
            return true;
         }
      }
   }

   private void removeWidgets() {
      if (!(this.removeWidgets(10485810, 10485812) & this.removeWidgets(58654775, 58654776))) {
         this.onDeselect();
         this.client.setWidgetSelected(false);
      }
   }

   @Subscribe
   private void onWidgetLoaded(WidgetLoaded l) {
      if (l.getGroupId() == 160 || l.getGroupId() == 895) {
         this.addWidgets();
      }

   }

   private void addWidgets() {
      Widget wikiBannerParent = this.client.getWidget(10485810);
      if (wikiBannerParent == null || wikiBannerParent.isHidden()) {
         wikiBannerParent = this.client.getWidget(58654775);
      }

      if (wikiBannerParent != null && !wikiBannerParent.isHidden()) {
         if (this.client.getVarbitValue(10113) == 1) {
            wikiBannerParent.setOriginalX(this.client.isResized() ? 0 : 8);
            wikiBannerParent.setOriginalY(135);
            wikiBannerParent.setXPositionMode(2);
            wikiBannerParent.setYPositionMode(0);
            wikiBannerParent.revalidate();
         }

         Widget vanilla = this.client.getWidget(10485812);
         if (vanilla != null) {
            vanilla.setHidden(true);
         }

         vanilla = this.client.getWidget(58654776);
         if (vanilla != null) {
            vanilla.setHidden(true);
         }

         if (this.config.showWikiMinimapButton()) {
            this.icon = wikiBannerParent.createChild(0, 5);
            this.icon.setSpriteId(2420);
            this.icon.setOriginalX(0);
            this.icon.setOriginalY(0);
            this.icon.setXPositionMode(1);
            this.icon.setYPositionMode(1);
            this.icon.setOriginalWidth(40);
            this.icon.setOriginalHeight(14);
            this.icon.setTargetVerb("Lookup");
            this.icon.setName("Wiki");
            this.icon.setClickMask(79872);
            this.icon.setNoClickThrough(true);
            this.icon.setOnTargetEnterListener(new Object[]{(ev) -> {
               this.wikiSelected = true;
               this.icon.setSpriteId(2421);
               this.client.setAllWidgetsAreOpTargetable(true);
            }});
            int searchIndex = this.config.leftClickSearch() ? 4 : 5;
            this.icon.setAction(searchIndex, "Search");
            this.icon.setAction(6, "DPS");
            this.icon.setOnOpListener(new Object[]{(ev) -> {
               int op = ev.getOp() - 1;
               if (op == searchIndex) {
                  this.openSearchInput();
               } else if (op == 6) {
                  this.wikiDpsManager.launch();
               }

            }});
            this.icon.setOnTargetLeaveListener(new Object[]{(ev) -> {
               this.onDeselect();
            }});
            this.icon.revalidate();
         }
      }
   }

   @Subscribe
   public void onScriptPostFired(ScriptPostFired scriptPostFired) {
      if (scriptPostFired.getScriptId() == 3306) {
         Widget w = this.client.getWidget(10485812);
         if (w != null) {
            w.setHidden(true);
         }

         w = this.client.getWidget(58654776);
         if (w != null) {
            w.setHidden(true);
         }
      }

   }

   @Subscribe
   public void onConfigChanged(ConfigChanged event) {
      if (event.getGroup().equals("wiki")) {
         this.clientThread.invokeLater(() -> {
            this.removeWidgets();
            this.addWidgets();
         });
      }

   }

   private void onDeselect() {
      this.client.setAllWidgetsAreOpTargetable(false);
      this.wikiSelected = false;
      if (this.icon != null) {
         this.icon.setSpriteId(2420);
      }

   }

   @Subscribe
   private void onMenuOptionClicked(MenuOptionClicked ev) {
      label65: {
         if (this.wikiSelected) {
            this.onDeselect();
            this.client.setWidgetSelected(false);
            ev.consume();
            String type;
            int id;
            String name;
            WorldPoint location;
            switch (ev.getMenuAction()) {
               case RUNELITE:
                  return;
               case CANCEL:
                  return;
               case WIDGET_TARGET_ON_GROUND_ITEM:
                  type = "item";
                  id = this.itemManager.canonicalize(ev.getId());
                  name = this.itemManager.getItemComposition(id).getMembersName();
                  location = null;
                  break;
               case WIDGET_TARGET_ON_NPC:
                  type = "npc";
                  NPC npc = ev.getMenuEntry().getNpc();
                  if (npc == null) {
                     return;
                  }

                  NPCComposition nc = npc.getTransformedComposition();
                  if (nc == null) {
                     return;
                  }

                  id = nc.getId();
                  name = nc.getName();
                  location = npc.getWorldLocation();
                  break;
               case WIDGET_TARGET_ON_GAME_OBJECT:
                  type = "object";
                  ObjectComposition lc = this.client.getObjectDefinition(ev.getId());
                  if (lc.getImpostorIds() != null) {
                     lc = lc.getImpostor();
                  }

                  id = lc.getId();
                  name = lc.getName();
                  location = WorldPoint.fromScene(this.client, ev.getParam0(), ev.getParam1(), this.client.getPlane());
                  break;
               case WIDGET_TARGET_ON_WIDGET:
                  Widget w = this.getWidget(ev.getParam1(), ev.getParam0());
                  if (w.getType() == 5 && w.getItemId() != -1) {
                     type = "item";
                     id = this.itemManager.canonicalize(w.getItemId());
                     name = this.itemManager.getItemComposition(id).getMembersName();
                     location = null;
                     break;
                  }
               default:
                  break label65;
            }

            name = Text.removeTags(name);
            HttpUrl.Builder urlBuilder = WIKI_BASE.newBuilder();
            urlBuilder.addPathSegments("w/Special:Lookup").addQueryParameter("type", type).addQueryParameter("id", "" + id).addQueryParameter("name", name).addQueryParameter("utm_source", "runelite");
            if (location != null) {
               urlBuilder.addQueryParameter("x", "" + location.getX()).addQueryParameter("y", "" + location.getY()).addQueryParameter("plane", "" + location.getPlane());
            }

            HttpUrl url = urlBuilder.build();
            LinkBrowser.browse(url.toString());
            return;
         }

         return;
      }

      log.info("Unknown menu option: {} {} {}", new Object[]{ev, ev.getMenuAction(), ev.getMenuAction() == MenuAction.CANCEL});
   }

   private void openSearchInput() {
      ((WikiSearchChatboxTextInput)this.wikiSearchChatboxTextInputProvider.get()).build();
   }

   private Widget getWidget(int wid, int index) {
      Widget w = this.client.getWidget(wid);
      if (index != -1) {
         w = w.getChild(index);
      }

      return w;
   }

   @Subscribe
   public void onMenuEntryAdded(MenuEntryAdded event) {
      int widgetIndex = event.getActionParam0();
      int widgetID = event.getActionParam1();
      if (this.wikiSelected && event.getType() == MenuAction.WIDGET_TARGET_ON_WIDGET.getId()) {
         Menu menu = this.client.getMenu();
         MenuEntry[] menuEntries = menu.getMenuEntries();
         Widget w = this.getWidget(widgetID, widgetIndex);
         if (w.getType() == 5 && w.getItemId() != -1 && w.getItemId() != 6512) {
            for(int ourEntry = menuEntries.length - 1; ourEntry >= 0; --ourEntry) {
               MenuEntry entry = menuEntries[ourEntry];
               if (entry.getType() == MenuAction.WIDGET_TARGET_ON_WIDGET) {
                  int id = this.itemManager.canonicalize(w.getItemId());
                  String name = this.itemManager.getItemComposition(id).getMembersName();
                  if ("null".equals(name)) {
                     menu.removeMenuEntry(entry);
                  } else {
                     entry.setTarget("<col=ff9040>" + name);
                  }
                  break;
               }
            }
         } else {
            MenuEntry[] oldEntries = menuEntries;
            menuEntries = (MenuEntry[])Arrays.copyOf(menuEntries, menuEntries.length - 1);

            for(int ourEntry = oldEntries.length - 1; ourEntry >= 2 && oldEntries[oldEntries.length - 1].getType() != MenuAction.WIDGET_TARGET_ON_WIDGET; --ourEntry) {
               menuEntries[ourEntry - 1] = oldEntries[ourEntry];
            }

            this.client.setMenuEntries(menuEntries);
         }
      }

      Widget w;
      if (event.getType() == MenuAction.CC_OP.getId() && WidgetUtil.componentToInterface(widgetID) == 320) {
         w = this.getWidget(widgetID, widgetIndex);
         if (w.getActions() == null || w.getParentId() != 20971520) {
            return;
         }

         String action = (String)Stream.of(w.getActions()).filter((s) -> {
            return s != null && !s.isEmpty();
         }).findFirst().orElse((Object)null);
         if (action == null) {
            return;
         }

         this.client.createMenuEntry(-1).setTarget(action.replace("View ", "").replace(" guide", "")).setOption("Wiki").setType(MenuAction.RUNELITE).onClick((ev) -> {
            LinkBrowser.browse(WIKI_BASE.newBuilder().addPathSegment("w").addPathSegment(Text.removeTags(ev.getTarget())).addQueryParameter("utm_source", "runelite").build().toString());
         });
      }

      if (event.getType() == MenuAction.CC_OP.getId() && WidgetUtil.componentToInterface(widgetID) == 715) {
         w = this.getWidget(widgetID, widgetIndex);
         if (w.getActions() == null || w.getParentId() != 46858249) {
            return;
         }

         this.client.getMenu().createMenuEntry(-1).setTarget(w.getName()).setOption("Wiki").setType(MenuAction.RUNELITE).onClick((ev) -> {
            LinkBrowser.browse(WIKI_BASE.newBuilder().addPathSegment("w").addPathSegment(Text.removeTags(ev.getTarget())).addQueryParameter("utm_source", "runelite").build().toString());
         });
      }

   }

   static {
      WIKI_API = WIKI_BASE.newBuilder().addPathSegments("api.php").build();
   }
}
