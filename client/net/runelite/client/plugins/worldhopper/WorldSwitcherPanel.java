package net.runelite.client.plugins.worldhopper;

import com.google.common.collect.Ordering;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import javax.annotation.Nullable;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import net.runelite.api.EnumComposition;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.DynamicGridLayout;
import net.runelite.client.ui.PluginPanel;
import net.runelite.http.api.worlds.World;
import net.runelite.http.api.worlds.WorldType;

class WorldSwitcherPanel extends PluginPanel {
   private static final Color ODD_ROW = new Color(44, 44, 44);
   private static final int WORLD_COLUMN_WIDTH = 60;
   private static final int PLAYERS_COLUMN_WIDTH = 40;
   private static final int PING_COLUMN_WIDTH = 47;
   private final JPanel listContainer = new JPanel();
   private boolean active;
   private WorldTableHeader worldHeader;
   private WorldTableHeader playersHeader;
   private WorldTableHeader activityHeader;
   private WorldTableHeader pingHeader;
   private WorldOrder orderIndex;
   private boolean ascendingOrder;
   private final ArrayList<WorldTableRow> rows;
   private final WorldHopperPlugin plugin;
   private SubscriptionFilterMode subscriptionFilterMode;
   private Set<RegionFilterMode> regionFilterMode;
   private Set<WorldTypeFilter> worldTypeFilters;

   WorldSwitcherPanel(WorldHopperPlugin plugin) {
      this.orderIndex = WorldSwitcherPanel.WorldOrder.WORLD;
      this.ascendingOrder = true;
      this.rows = new ArrayList();
      this.plugin = plugin;
      this.setBorder((Border)null);
      this.setLayout(new DynamicGridLayout(0, 1));
      JPanel headerContainer = this.buildHeader();
      this.listContainer.setLayout(new GridLayout(0, 1));
      this.add(headerContainer);
      this.add(this.listContainer);
   }

   public void onActivate() {
      this.active = true;
      this.updateList();
   }

   public void onDeactivate() {
      this.active = false;
   }

   void switchCurrentHighlight(int newWorld, int lastWorld) {
      Iterator var3 = this.rows.iterator();

      while(var3.hasNext()) {
         WorldTableRow row = (WorldTableRow)var3.next();
         if (row.getWorld().getId() == newWorld) {
            row.recolour(true);
         } else if (row.getWorld().getId() == lastWorld) {
            row.recolour(false);
         }
      }

   }

   void updateListData(Map<Integer, Integer> worldData) {
      Iterator var2 = this.rows.iterator();

      while(var2.hasNext()) {
         WorldTableRow worldTableRow = (WorldTableRow)var2.next();
         World world = worldTableRow.getWorld();
         Integer playerCount = (Integer)worldData.get(world.getId());
         if (playerCount != null) {
            worldTableRow.updatePlayerCount(playerCount);
         }
      }

      if (this.orderIndex == WorldSwitcherPanel.WorldOrder.PLAYERS) {
         this.updateList();
      }

   }

   void updatePing(int world, int ping) {
      Iterator var3 = this.rows.iterator();

      while(var3.hasNext()) {
         WorldTableRow worldTableRow = (WorldTableRow)var3.next();
         if (worldTableRow.getWorld().getId() == world) {
            worldTableRow.setPing(ping);
            if (this.orderIndex == WorldSwitcherPanel.WorldOrder.PING) {
               this.updateList();
            }
            break;
         }
      }

   }

   void hidePing() {
      Iterator var1 = this.rows.iterator();

      while(var1.hasNext()) {
         WorldTableRow worldTableRow = (WorldTableRow)var1.next();
         worldTableRow.hidePing();
      }

   }

   void showPing() {
      Iterator var1 = this.rows.iterator();

      while(var1.hasNext()) {
         WorldTableRow worldTableRow = (WorldTableRow)var1.next();
         worldTableRow.showPing();
      }

   }

   void updateList() {
      this.rows.sort((r1, r2) -> {
         switch (this.orderIndex) {
            case PING:
               return this.getCompareValue(r1, r2, (row) -> {
                  int ping = row.getPing();
                  return ping > 0 ? ping : null;
               });
            case WORLD:
               return this.getCompareValue(r1, r2, (row) -> {
                  return row.getWorld().getId();
               });
            case PLAYERS:
               return this.getCompareValue(r1, r2, WorldTableRow::getPlayerCount);
            case ACTIVITY:
               return this.getCompareValue(r1, r2, (row) -> {
                  String activity = row.getWorld().getActivity();
                  return !activity.equals("-") ? activity : null;
               });
            default:
               return 0;
         }
      });
      this.rows.sort((r1, r2) -> {
         boolean b1 = this.plugin.isFavorite(r1.getWorld());
         boolean b2 = this.plugin.isFavorite(r2.getWorld());
         return Boolean.compare(b2, b1);
      });
      this.listContainer.removeAll();

      for(int i = 0; i < this.rows.size(); ++i) {
         WorldTableRow row = (WorldTableRow)this.rows.get(i);
         row.setBackground(i % 2 == 0 ? ODD_ROW : ColorScheme.DARK_GRAY_COLOR);
         this.listContainer.add(row);
      }

      this.listContainer.revalidate();
      this.listContainer.repaint();
   }

   private int getCompareValue(WorldTableRow row1, WorldTableRow row2, Function<WorldTableRow, Comparable> compareByFn) {
      Ordering<Comparable> ordering = Ordering.natural();
      if (!this.ascendingOrder) {
         ordering = ordering.reverse();
      }

      ordering = ordering.nullsLast();
      return ordering.compare((Comparable)compareByFn.apply(row1), (Comparable)compareByFn.apply(row2));
   }

   void updateFavoriteMenu(int world, boolean favorite) {
      Iterator var3 = this.rows.iterator();

      while(var3.hasNext()) {
         WorldTableRow row = (WorldTableRow)var3.next();
         if (row.getWorld().getId() == world) {
            row.setFavoriteMenu(favorite);
         }
      }

   }

   void populate(List<World> worlds, @Nullable EnumComposition worldLocations) {
      this.rows.clear();

      for(int i = 0; i < worlds.size(); ++i) {
         World world = (World)worlds.get(i);
         switch (this.subscriptionFilterMode) {
            case FREE:
               if (world.getTypes().contains(WorldType.MEMBERS)) {
                  continue;
               }
               break;
            case MEMBERS:
               if (!world.getTypes().contains(WorldType.MEMBERS)) {
                  continue;
               }
         }

         if (this.regionFilterMode.isEmpty() || this.regionFilterMode.contains(RegionFilterMode.of(world.getRegion()))) {
            if (!this.worldTypeFilters.isEmpty()) {
               boolean matches = false;

               WorldTypeFilter worldTypeFilter;
               for(Iterator var6 = this.worldTypeFilters.iterator(); var6.hasNext(); matches |= worldTypeFilter.matches(world.getTypes())) {
                  worldTypeFilter = (WorldTypeFilter)var6.next();
               }

               if (!matches) {
                  continue;
               }
            }

            this.rows.add(this.buildRow(world, i % 2 == 0, world.getId() == this.plugin.getCurrentWorld() && this.plugin.getLastWorld() != 0, this.plugin.isFavorite(world), worldLocations != null ? worldLocations.getIntValue(world.getId()) : -1));
         }
      }

      this.updateList();
   }

   private void orderBy(WorldOrder order) {
      this.pingHeader.highlight(false, this.ascendingOrder);
      this.worldHeader.highlight(false, this.ascendingOrder);
      this.playersHeader.highlight(false, this.ascendingOrder);
      this.activityHeader.highlight(false, this.ascendingOrder);
      switch (order) {
         case PING:
            this.pingHeader.highlight(true, this.ascendingOrder);
            break;
         case WORLD:
            this.worldHeader.highlight(true, this.ascendingOrder);
            break;
         case PLAYERS:
            this.playersHeader.highlight(true, this.ascendingOrder);
            break;
         case ACTIVITY:
            this.activityHeader.highlight(true, this.ascendingOrder);
      }

      this.orderIndex = order;
      this.updateList();
   }

   private JPanel buildHeader() {
      JPanel header = new JPanel(new BorderLayout());
      JPanel leftSide = new JPanel(new BorderLayout());
      JPanel rightSide = new JPanel(new BorderLayout());
      boolean var10004 = this.orderIndex == WorldSwitcherPanel.WorldOrder.PING;
      boolean var10005 = this.ascendingOrder;
      WorldHopperPlugin var10006 = this.plugin;
      Objects.requireNonNull(var10006);
      this.pingHeader = new WorldTableHeader("Ping", var10004, var10005, var10006::refresh);
      this.pingHeader.setPreferredSize(new Dimension(47, 0));
      this.pingHeader.addMouseListener(new MouseAdapter() {
         public void mousePressed(MouseEvent mouseEvent) {
            if (!SwingUtilities.isRightMouseButton(mouseEvent)) {
               WorldSwitcherPanel.this.ascendingOrder = WorldSwitcherPanel.this.orderIndex != WorldSwitcherPanel.WorldOrder.PING || !WorldSwitcherPanel.this.ascendingOrder;
               WorldSwitcherPanel.this.orderBy(WorldSwitcherPanel.WorldOrder.PING);
            }
         }
      });
      var10004 = this.orderIndex == WorldSwitcherPanel.WorldOrder.WORLD;
      var10005 = this.ascendingOrder;
      var10006 = this.plugin;
      Objects.requireNonNull(var10006);
      this.worldHeader = new WorldTableHeader("World", var10004, var10005, var10006::refresh);
      this.worldHeader.setPreferredSize(new Dimension(60, 0));
      this.worldHeader.addMouseListener(new MouseAdapter() {
         public void mousePressed(MouseEvent mouseEvent) {
            if (!SwingUtilities.isRightMouseButton(mouseEvent)) {
               WorldSwitcherPanel.this.ascendingOrder = WorldSwitcherPanel.this.orderIndex != WorldSwitcherPanel.WorldOrder.WORLD || !WorldSwitcherPanel.this.ascendingOrder;
               WorldSwitcherPanel.this.orderBy(WorldSwitcherPanel.WorldOrder.WORLD);
            }
         }
      });
      var10004 = this.orderIndex == WorldSwitcherPanel.WorldOrder.PLAYERS;
      var10005 = this.ascendingOrder;
      var10006 = this.plugin;
      Objects.requireNonNull(var10006);
      this.playersHeader = new WorldTableHeader("#", var10004, var10005, var10006::refresh);
      this.playersHeader.setPreferredSize(new Dimension(40, 0));
      this.playersHeader.addMouseListener(new MouseAdapter() {
         public void mousePressed(MouseEvent mouseEvent) {
            if (!SwingUtilities.isRightMouseButton(mouseEvent)) {
               WorldSwitcherPanel.this.ascendingOrder = WorldSwitcherPanel.this.orderIndex != WorldSwitcherPanel.WorldOrder.PLAYERS || !WorldSwitcherPanel.this.ascendingOrder;
               WorldSwitcherPanel.this.orderBy(WorldSwitcherPanel.WorldOrder.PLAYERS);
            }
         }
      });
      var10004 = this.orderIndex == WorldSwitcherPanel.WorldOrder.ACTIVITY;
      var10005 = this.ascendingOrder;
      var10006 = this.plugin;
      Objects.requireNonNull(var10006);
      this.activityHeader = new WorldTableHeader("Activity", var10004, var10005, var10006::refresh);
      this.activityHeader.addMouseListener(new MouseAdapter() {
         public void mousePressed(MouseEvent mouseEvent) {
            if (!SwingUtilities.isRightMouseButton(mouseEvent)) {
               WorldSwitcherPanel.this.ascendingOrder = WorldSwitcherPanel.this.orderIndex != WorldSwitcherPanel.WorldOrder.ACTIVITY || !WorldSwitcherPanel.this.ascendingOrder;
               WorldSwitcherPanel.this.orderBy(WorldSwitcherPanel.WorldOrder.ACTIVITY);
            }
         }
      });
      leftSide.add(this.worldHeader, "West");
      leftSide.add(this.playersHeader, "Center");
      rightSide.add(this.activityHeader, "Center");
      rightSide.add(this.pingHeader, "East");
      header.add(leftSide, "West");
      header.add(rightSide, "Center");
      return header;
   }

   private WorldTableRow buildRow(World world, boolean stripe, boolean current, boolean favorite, int worldLocation) {
      Integer var10005 = this.plugin.getStoredPing(world);
      WorldHopperPlugin var10006 = this.plugin;
      Objects.requireNonNull(var10006);
      WorldTableRow row = new WorldTableRow(world, current, favorite, var10005, var10006::hopTo, (w, add) -> {
         if (add) {
            this.plugin.addToFavorites(w);
         } else {
            this.plugin.removeFromFavorites(w);
         }

         this.updateList();
      }, worldLocation);
      row.setBackground(stripe ? ODD_ROW : ColorScheme.DARK_GRAY_COLOR);
      return row;
   }

   boolean isActive() {
      return this.active;
   }

   void setSubscriptionFilterMode(SubscriptionFilterMode subscriptionFilterMode) {
      this.subscriptionFilterMode = subscriptionFilterMode;
   }

   void setRegionFilterMode(Set<RegionFilterMode> regionFilterMode) {
      this.regionFilterMode = regionFilterMode;
   }

   void setWorldTypeFilters(Set<WorldTypeFilter> worldTypeFilters) {
      this.worldTypeFilters = worldTypeFilters;
   }

   private static enum WorldOrder {
      WORLD,
      PLAYERS,
      ACTIVITY,
      PING;
   }
}
