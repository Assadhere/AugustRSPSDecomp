package net.runelite.client.plugins.devtools;

import com.google.common.collect.Lists;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import javax.inject.Inject;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.border.CompoundBorder;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import net.runelite.api.Client;
import net.runelite.api.events.ScriptPostFired;
import net.runelite.api.events.ScriptPreFired;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetUtil;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.EventBus;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.DynamicGridLayout;
import net.runelite.client.ui.FontManager;
import net.runelite.client.util.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ScriptInspector extends DevToolsFrame {
   private static final Logger log = LoggerFactory.getLogger(ScriptInspector.class);
   private static final String DEFAULT_BLACKLIST = "3174,1004";
   private static final int MAX_LOG_ENTRIES = 10000;
   private final Client client;
   private final EventBus eventBus;
   private final ConfigManager configManager;
   private final JPanel tracker = new JPanel();
   private ScriptTreeNode currentNode;
   private int lastTick;
   private Set<Integer> blacklist;
   private Set<Integer> highlights;
   private final JList<Integer> jList;
   private final DefaultListModel<Integer> listModel;
   private ListState state;

   @Inject
   ScriptInspector(Client client, EventBus eventBus, ConfigManager configManager) {
      this.state = ScriptInspector.ListState.BLACKLIST;
      this.eventBus = eventBus;
      this.client = client;
      this.configManager = configManager;
      this.setTitle("RuneLite Script Inspector");
      this.setLayout(new BorderLayout());
      this.tracker.setLayout(new DynamicGridLayout(0, 1, 0, 3));
      JPanel leftSide = new JPanel();
      leftSide.setLayout(new BorderLayout());
      JPanel trackerWrapper = new JPanel();
      trackerWrapper.setLayout(new BorderLayout());
      trackerWrapper.add(this.tracker, "North");
      JScrollPane trackerScroller = new JScrollPane(trackerWrapper);
      trackerScroller.setPreferredSize(new Dimension(400, 400));
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
      JPanel bottomLeftRow = new JPanel();
      JButton clearBtn = new JButton("Clear");
      clearBtn.addActionListener((e) -> {
         this.tracker.removeAll();
         this.tracker.revalidate();
      });
      bottomLeftRow.add(clearBtn);
      leftSide.add(bottomLeftRow, "South");
      this.add(leftSide, "Center");
      String blacklistConfig = configManager.getConfiguration("devtools", "blacklist");
      if (blacklistConfig == null) {
         blacklistConfig = "3174,1004";
      }

      try {
         this.blacklist = new HashSet(Lists.transform(Text.fromCSV(blacklistConfig), Integer::parseInt));
      } catch (NumberFormatException var24) {
         this.blacklist = new HashSet(Lists.transform(Text.fromCSV("3174,1004"), Integer::parseInt));
      }

      String highlightsConfig = configManager.getConfiguration("devtools", "highlights");
      if (highlightsConfig == null) {
         highlightsConfig = "";
      }

      try {
         this.highlights = new HashSet(Lists.transform(Text.fromCSV(highlightsConfig), Integer::parseInt));
      } catch (NumberFormatException var23) {
         this.highlights = new HashSet();
      }

      JPanel rightSide = new JPanel();
      rightSide.setLayout(new BorderLayout());
      this.listModel = new DefaultListModel();
      this.changeState(ScriptInspector.ListState.BLACKLIST);
      this.jList = new JList(this.listModel);
      this.jList.setSelectionMode(0);
      JScrollPane listScrollPane = new JScrollPane(this.jList);
      JButton blacklistButton = new JButton("Blacklist");
      blacklistButton.addActionListener((e) -> {
         this.changeState(ScriptInspector.ListState.BLACKLIST);
      });
      JButton highlightsButton = new JButton("Highlights");
      highlightsButton.addActionListener((e) -> {
         this.changeState(ScriptInspector.ListState.HIGHLIGHT);
      });
      JPanel topLeftRow = new JPanel();
      topLeftRow.setLayout(new FlowLayout());
      topLeftRow.add(blacklistButton);
      topLeftRow.add(highlightsButton);
      rightSide.add(topLeftRow, "North");
      rightSide.add(listScrollPane, "Center");
      JSpinner jSpinner = new JSpinner();
      Component mySpinnerEditor = jSpinner.getEditor();
      JFormattedTextField textField = ((JSpinner.DefaultEditor)mySpinnerEditor).getTextField();
      textField.setColumns(5);
      JButton addButton = new JButton("Add");
      addButton.addActionListener((e) -> {
         this.addToSet(jSpinner);
      });
      JButton removeButton = new JButton("Remove");
      removeButton.addActionListener((e) -> {
         this.removeSelectedFromSet();
      });
      JPanel bottomButtonRow = new JPanel();
      bottomButtonRow.setLayout(new FlowLayout());
      bottomButtonRow.add(addButton);
      bottomButtonRow.add(jSpinner);
      bottomButtonRow.add(removeButton);
      rightSide.add(bottomButtonRow, "South");
      this.add(rightSide, "East");
      this.pack();
   }

   @Subscribe
   public void onScriptPreFired(ScriptPreFired event) {
      ScriptTreeNode newNode = new ScriptTreeNode(event.getScriptId());
      if (event.getScriptEvent() != null) {
         newNode.setSource(event.getScriptEvent().getSource());
         newNode.setArgs(event.getScriptEvent().getArguments());
      }

      if (this.currentNode == null) {
         this.currentNode = newNode;
      } else {
         int count = 0;
         Enumeration children = this.currentNode.children();
         if (children != null) {
            while(children.hasMoreElements()) {
               ScriptTreeNode child = (ScriptTreeNode)children.nextElement();
               if (child.getScriptId() == event.getScriptId()) {
                  ++count;
               }
            }

            newNode.setDuplicateNumber(count + 1);
         }

         this.currentNode.add(newNode);
         this.currentNode = newNode;
      }

   }

   @Subscribe
   public void onScriptPostFired(ScriptPostFired event) {
      if (this.currentNode != null && this.currentNode.getScriptId() == event.getScriptId()) {
         if (this.currentNode.getParent() != null) {
            this.currentNode = (ScriptTreeNode)this.currentNode.getParent();
         } else {
            this.addScriptLog(this.currentNode);
            this.currentNode = null;
         }

      } else {
         log.warn("a script was post-fired that was never pre-fired. Script id: " + event.getScriptId());
      }
   }

   public void open() {
      this.eventBus.register(this);
      super.open();
   }

   public void close() {
      this.configManager.setConfiguration("devtools", "highlights", Text.toCSV(Lists.transform(new ArrayList(this.highlights), String::valueOf)));
      this.configManager.setConfiguration("devtools", "blacklist", Text.toCSV(Lists.transform(new ArrayList(this.blacklist), String::valueOf)));
      this.currentNode = null;
      this.eventBus.unregister((Object)this);
      super.close();
   }

   private void addScriptLog(ScriptTreeNode treeNode) {
      if (!this.blacklist.contains(treeNode.getScriptId())) {
         int tick = this.client.getTickCount();
         SwingUtilities.invokeLater(() -> {
            if (tick != this.lastTick) {
               this.lastTick = tick;
               JLabel header = new JLabel("Tick " + tick);
               header.setFont(FontManager.getRunescapeSmallFont());
               header.setBorder(new CompoundBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, ColorScheme.LIGHT_GRAY_COLOR), BorderFactory.createEmptyBorder(3, 6, 0, 0)));
               this.tracker.add(header);
            }

            DefaultTreeModel treeModel = new DefaultTreeModel(treeNode);
            JTree tree = new JTree(treeModel);
            tree.setRootVisible(true);
            tree.setShowsRootHandles(true);
            tree.collapsePath(new TreePath(treeNode));
            ScriptTreeNode highlightNode = this.findHighlightPathNode(treeNode);
            if (highlightNode != null) {
               tree.setExpandsSelectedPaths(true);
               tree.setSelectionPath(new TreePath(treeModel.getPathToRoot(highlightNode)));
            }

            this.tracker.add(tree);

            while(this.tracker.getComponentCount() > 10000) {
               this.tracker.remove(0);
            }

            this.tracker.revalidate();
         });
      }
   }

   private void changeState(ListState state) {
      this.state = state;
      this.refreshList();
   }

   private void addToSet(JSpinner spinner) {
      int script = (Integer)spinner.getValue();
      Set<Integer> set = this.getSet();
      set.add(script);
      this.refreshList();
      spinner.setValue(0);
   }

   private void removeSelectedFromSet() {
      int index = this.jList.getSelectedIndex();
      if (index != -1) {
         int script = (Integer)this.listModel.get(index);
         this.getSet().remove(script);
         this.refreshList();
      }
   }

   private void refreshList() {
      this.listModel.clear();
      Set<Integer> set = this.getSet();
      Iterator var2 = set.iterator();

      while(var2.hasNext()) {
         Integer i = (Integer)var2.next();
         this.listModel.addElement(i);
      }

   }

   private Set<Integer> getSet() {
      Set set;
      if (this.state == ScriptInspector.ListState.BLACKLIST) {
         set = this.blacklist;
      } else {
         set = this.highlights;
      }

      return set;
   }

   private ScriptTreeNode findHighlightPathNode(ScriptTreeNode node) {
      if (this.highlights.contains(node.getScriptId())) {
         return node;
      } else {
         Enumeration children = node.children();
         if (children != null) {
            while(children.hasMoreElements()) {
               ScriptTreeNode child = (ScriptTreeNode)children.nextElement();
               ScriptTreeNode find = this.findHighlightPathNode(child);
               if (find != null) {
                  return find;
               }
            }
         }

         return null;
      }
   }

   private static class ScriptTreeNode extends DefaultMutableTreeNode {
      private final int scriptId;
      private Widget source;
      private int duplicateNumber = 1;
      private Object[] args;

      public String toString() {
         String output = Integer.toString(this.scriptId);
         if (this.duplicateNumber != 1) {
            output = output + " (" + this.duplicateNumber + ")";
         }

         if (this.source != null) {
            int id = this.source.getId();
            output = output + "  -  " + WidgetUtil.componentToInterface(id) + "." + WidgetUtil.componentToId(id);
            if (this.source.getIndex() != -1) {
               output = output + "[" + this.source.getIndex() + "]";
            }

            String name = WidgetInspector.getWidgetName(id);
            if (name != null) {
               output = output + " " + name;
            }
         }

         if (this.args != null) {
            output = output + " args: " + Arrays.toString(this.args);
         }

         return output;
      }

      public ScriptTreeNode(int scriptId) {
         this.scriptId = scriptId;
      }

      public int getScriptId() {
         return this.scriptId;
      }

      public Widget getSource() {
         return this.source;
      }

      public int getDuplicateNumber() {
         return this.duplicateNumber;
      }

      public Object[] getArgs() {
         return this.args;
      }

      public void setSource(Widget source) {
         this.source = source;
      }

      public void setDuplicateNumber(int duplicateNumber) {
         this.duplicateNumber = duplicateNumber;
      }

      public void setArgs(Object[] args) {
         this.args = args;
      }

      public boolean equals(Object o) {
         if (o == this) {
            return true;
         } else if (!(o instanceof ScriptTreeNode)) {
            return false;
         } else {
            ScriptTreeNode other = (ScriptTreeNode)o;
            if (!other.canEqual(this)) {
               return false;
            } else if (this.getScriptId() != other.getScriptId()) {
               return false;
            } else if (this.getDuplicateNumber() != other.getDuplicateNumber()) {
               return false;
            } else {
               Object this$source = this.getSource();
               Object other$source = other.getSource();
               if (this$source == null) {
                  if (other$source == null) {
                     return Arrays.deepEquals(this.getArgs(), other.getArgs());
                  }
               } else if (this$source.equals(other$source)) {
                  return Arrays.deepEquals(this.getArgs(), other.getArgs());
               }

               return false;
            }
         }
      }

      protected boolean canEqual(Object other) {
         return other instanceof ScriptTreeNode;
      }

      public int hashCode() {
         int PRIME = true;
         int result = 1;
         result = result * 59 + this.getScriptId();
         result = result * 59 + this.getDuplicateNumber();
         Object $source = this.getSource();
         result = result * 59 + ($source == null ? 43 : $source.hashCode());
         result = result * 59 + Arrays.deepHashCode(this.getArgs());
         return result;
      }
   }

   private static enum ListState {
      BLACKLIST,
      HIGHLIGHT;
   }
}
