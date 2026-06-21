package net.runelite.client.plugins.devtools;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import net.runelite.api.Client;
import net.runelite.api.Item;
import net.runelite.api.ItemComposition;
import net.runelite.api.events.ItemContainerChanged;
import net.runelite.api.gameval.InventoryID;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.eventbus.EventBus;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.game.ItemManager;
import net.runelite.client.ui.ClientUI;
import net.runelite.client.ui.ColorScheme;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
class InventoryInspector extends DevToolsFrame {
   private static final Logger log = LoggerFactory.getLogger(InventoryInspector.class);
   private static final int MAX_LOG_ENTRIES = 25;
   private final Client client;
   private final EventBus eventBus;
   private final ItemManager itemManager;
   private final Map<Integer, InventoryTreeNode> nodeMap = new HashMap();
   private final Map<Integer, InventoryLog> logMap = new HashMap();
   private final DefaultMutableTreeNode trackerRootNode = new DefaultMutableTreeNode();
   private final JTree tree;
   private final InventoryDeltaPanel deltaPanel;
   private static final Map<Integer, String> INV_NAMES = DevToolsPlugin.loadFieldNames(InventoryID.class);

   @Inject
   InventoryInspector(Client client, EventBus eventBus, ItemManager itemManager, ClientThread clientThread) {
      this.tree = new JTree(this.trackerRootNode);
      this.client = client;
      this.eventBus = eventBus;
      this.itemManager = itemManager;
      this.deltaPanel = new InventoryDeltaPanel(itemManager);
      this.setLayout(new BorderLayout());
      this.setTitle("RuneLite Inventory Inspector");
      this.setIconImages(Arrays.asList(ClientUI.ICON_128, ClientUI.ICON_16));
      this.tree.setBorder(new EmptyBorder(2, 2, 2, 2));
      this.tree.setRootVisible(false);
      this.tree.setShowsRootHandles(true);
      this.tree.addTreeSelectionListener((e) -> {
         if (e.getNewLeadSelectionPath() != null) {
            Object node = e.getNewLeadSelectionPath().getLastPathComponent();
            if (node instanceof InventoryLogNode) {
               clientThread.invoke(() -> {
                  this.displayItemSnapshot((InventoryLogNode)node);
               });
            }

         }
      });
      this.tree.setModel(new DefaultTreeModel(this.trackerRootNode));
      JPanel leftSide = new JPanel();
      leftSide.setLayout(new BorderLayout());
      JScrollPane trackerScroller = new JScrollPane(this.tree);
      trackerScroller.setPreferredSize(new Dimension(200, 400));
      final JScrollBar vertical = trackerScroller.getVerticalScrollBar();
      vertical.addAdjustmentListener(new AdjustmentListener() {
         int lastMaximum = this.actualMax();

         private int actualMax() {
            return vertical.getMaximum() - vertical.getModel().getExtent();
         }

         public void adjustmentValueChanged(AdjustmentEvent e) {
            if (vertical.getValue() >= this.lastMaximum) {
               vertical.setValue(this.actualMax());
            }

            this.lastMaximum = this.actualMax();
         }
      });
      leftSide.add(trackerScroller, "Center");
      JButton refreshBtn = new JButton("Refresh");
      refreshBtn.setFocusable(false);
      refreshBtn.addActionListener((e) -> {
         this.refreshTracker();
      });
      JButton clearBtn = new JButton("Clear");
      clearBtn.setFocusable(false);
      clearBtn.addActionListener((e) -> {
         this.clearTracker();
      });
      JPanel bottomRow = new JPanel();
      bottomRow.add(refreshBtn);
      bottomRow.add(clearBtn);
      leftSide.add(bottomRow, "South");
      JScrollPane gridScroller = new JScrollPane(this.deltaPanel);
      gridScroller.getViewport().setBackground(ColorScheme.DARK_GRAY_COLOR);
      gridScroller.setPreferredSize(new Dimension(200, 400));
      JSplitPane split = new JSplitPane(1, leftSide, gridScroller);
      this.add(split, "Center");
      this.pack();
   }

   public void open() {
      this.eventBus.register(this);
      super.open();
   }

   public void close() {
      this.eventBus.unregister((Object)this);
      this.clearTracker();
      super.close();
   }

   @Subscribe
   public void onItemContainerChanged(ItemContainerChanged event) {
      int id = event.getContainerId();
      InventoryLog log = new InventoryLog(id, (String)INV_NAMES.getOrDefault(id, "" + id), event.getItemContainer().getItems(), this.client.getTickCount());
      this.logMap.put(id, log);
   }

   private void addLog(InventoryLog invLog) {
      InventoryTreeNode node = (InventoryTreeNode)this.nodeMap.computeIfAbsent(invLog.getContainerId(), (k) -> {
         return new InventoryTreeNode(invLog.getContainerId(), invLog.getContainerName());
      });
      node.add(new InventoryLogNode(invLog));

      while(node.getChildCount() > 25) {
         node.remove(0);
      }

   }

   private void clearTracker() {
      this.logMap.clear();
      this.nodeMap.clear();
      this.deltaPanel.clear();
      this.trackerRootNode.removeAllChildren();
      this.tree.setModel(new DefaultTreeModel(this.trackerRootNode));
   }

   private void refreshTracker() {
      this.deltaPanel.clear();
      if (this.logMap.size() > 0) {
         this.logMap.values().forEach(this::addLog);
         this.logMap.clear();
      }

      SwingUtilities.invokeLater(() -> {
         this.trackerRootNode.removeAllChildren();
         Collection var10000 = this.nodeMap.values();
         DefaultMutableTreeNode var10001 = this.trackerRootNode;
         Objects.requireNonNull(var10001);
         var10000.forEach(var10001::add);
         this.tree.setModel(new DefaultTreeModel(this.trackerRootNode));
      });
   }

   private void displayItemSnapshot(InventoryLogNode logNode) {
      InventoryTreeNode treeNode = (InventoryTreeNode)this.nodeMap.get(logNode.getLog().getContainerId());
      if (treeNode == null) {
         log.warn("Clicked on a JTree node that doesn't map anywhere: {}", logNode);
      } else {
         Item[] curItems = logNode.getLog().getItems();
         InventoryItem[] curInventory = this.convertToInventoryItems(curItems);
         InventoryItem[][] deltas = null;
         if (treeNode.getIndex(logNode) > 0) {
            TreeNode prevNode = treeNode.getChildBefore(logNode);
            if (prevNode instanceof InventoryLogNode) {
               InventoryLogNode prevLogNode = (InventoryLogNode)prevNode;
               deltas = this.compareItemSnapshots(prevLogNode.getLog().getItems(), curItems);
            }
         }

         InventoryItem[] added = deltas == null ? null : deltas[0];
         InventoryItem[] removed = deltas == null ? null : deltas[1];
         SwingUtilities.invokeLater(() -> {
            this.deltaPanel.displayItems(curInventory, added, removed);
         });
      }
   }

   private InventoryItem[] convertToInventoryItems(Item[] items) {
      InventoryItem[] out = new InventoryItem[items.length];

      for(int i = 0; i < items.length; ++i) {
         Item item = items[i];
         ItemComposition c = this.itemManager.getItemComposition(item.getId());
         out[i] = new InventoryItem(i, item, c.getMembersName(), c.isStackable());
      }

      return out;
   }

   private InventoryItem[][] compareItemSnapshots(Item[] previous, Item[] current) {
      Map<Integer, Integer> qtyMap = new HashMap();
      int maxSlots = Math.max(previous.length, current.length);

      for(int i = 0; i < maxSlots; ++i) {
         Item prev = previous.length > i ? previous[i] : null;
         Item cur = current.length > i ? current[i] : null;
         if (prev != null) {
            qtyMap.merge(prev.getId(), -1 * prev.getQuantity(), Integer::sum);
         }

         if (cur != null) {
            qtyMap.merge(cur.getId(), cur.getQuantity(), Integer::sum);
         }
      }

      Map<Boolean, List<InventoryItem>> result = (Map)qtyMap.entrySet().stream().filter((e) -> {
         return (Integer)e.getValue() != 0;
      }).flatMap((e) -> {
         int id = (Integer)e.getKey();
         int qty = (Integer)e.getValue();
         ItemComposition c = this.itemManager.getItemComposition((Integer)e.getKey());
         InventoryItem[] items = new InventoryItem[]{new InventoryItem(-1, new Item(id, qty), c.getMembersName(), c.isStackable())};
         if (!c.isStackable() && (qty > 1 || qty < -1)) {
            items = new InventoryItem[Math.abs(qty)];

            for(int i = 0; i < Math.abs(qty); ++i) {
               Item item = new Item(id, Integer.signum(qty));
               items[i] = new InventoryItem(-1, item, c.getMembersName(), c.isStackable());
            }
         }

         return Arrays.stream(items);
      }).collect(Collectors.partitioningBy((item) -> {
         return item.getItem().getQuantity() > 0;
      }));
      InventoryItem[] added = (InventoryItem[])((List)result.get(true)).toArray(new InventoryItem[0]);
      InventoryItem[] removed = (InventoryItem[])((List)result.get(false)).stream().peek((ix) -> {
         ix.setItem(new Item(ix.getItem().getId(), -ix.getItem().getQuantity()));
      }).toArray((x$0) -> {
         return new InventoryItem[x$0];
      });
      return new InventoryItem[][]{added, removed};
   }
}
