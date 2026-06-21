package net.runelite.client.plugins.devtools;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.lang.reflect.Field;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import java.util.stream.Stream;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import net.runelite.api.Client;
import net.runelite.api.MenuAction;
import net.runelite.api.MenuEntry;
import net.runelite.api.events.MenuEntryAdded;
import net.runelite.api.events.MenuOptionClicked;
import net.runelite.api.gameval.InterfaceID;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetUtil;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.eventbus.EventBus;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayManager;
import net.runelite.client.util.ColorUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
class WidgetInspector extends DevToolsFrame {
   private static final Logger log = LoggerFactory.getLogger(WidgetInspector.class);
   private static final Map<Integer, String> widgetNames = new HashMap();
   static final Color SELECTED_WIDGET_COLOR;
   private static final float SELECTED_WIDGET_HUE;
   private final Client client;
   private final ClientThread clientThread;
   private final DevToolsConfig config;
   private final Provider<WidgetInspectorOverlay> overlay;
   private final OverlayManager overlayManager;
   private final JTree widgetTree;
   private final WidgetInfoTableModel infoTableModel;
   private final JCheckBox alwaysOnTop;
   private final JCheckBox hideHidden;
   private DefaultMutableTreeNode root;
   private Widget selectedWidget;
   private Widget picker = null;
   private boolean pickerSelected = false;

   @Inject
   private WidgetInspector(Client client, ClientThread clientThread, WidgetInfoTableModel infoTableModel, DevToolsConfig config, EventBus eventBus, Provider<WidgetInspectorOverlay> overlay, OverlayManager overlayManager) {
      this.client = client;
      this.clientThread = clientThread;
      this.infoTableModel = infoTableModel;
      this.config = config;
      this.overlay = overlay;
      this.overlayManager = overlayManager;
      eventBus.register(this);
      this.setTitle("RuneLite Widget Inspector");
      this.setLayout(new BorderLayout());
      this.widgetTree = new JTree(new DefaultMutableTreeNode());
      this.widgetTree.setRootVisible(false);
      this.widgetTree.setShowsRootHandles(true);
      this.widgetTree.getSelectionModel().addTreeSelectionListener((e) -> {
         Object selected = this.widgetTree.getLastSelectedPathComponent();
         if (selected instanceof WidgetTreeNode) {
            WidgetTreeNode node = (WidgetTreeNode)selected;
            Widget widget = node.getWidget();
            this.setSelectedWidget(widget, false);
         }

      });
      JScrollPane treeScrollPane = new JScrollPane(this.widgetTree);
      treeScrollPane.setPreferredSize(new Dimension(400, 800));
      JTable widgetInfo = new JTable(infoTableModel);
      JScrollPane infoScrollPane = new JScrollPane(widgetInfo);
      infoScrollPane.setPreferredSize(new Dimension(600, 800));
      JPanel bottomPanel = new JPanel();
      this.add(bottomPanel, "South");
      JButton refreshWidgetsBtn = new JButton("Refresh");
      refreshWidgetsBtn.addActionListener((e) -> {
         this.refreshWidgets();
      });
      bottomPanel.add(refreshWidgetsBtn);
      this.alwaysOnTop = new JCheckBox("Always on top");
      this.alwaysOnTop.addItemListener((ev) -> {
         config.inspectorAlwaysOnTop(this.alwaysOnTop.isSelected());
      });
      this.onConfigChanged((ConfigChanged)null);
      bottomPanel.add(this.alwaysOnTop);
      this.hideHidden = new JCheckBox("Hide hidden");
      this.hideHidden.setSelected(true);
      this.hideHidden.addItemListener((ev) -> {
         this.refreshWidgets();
      });
      bottomPanel.add(this.hideHidden);
      JButton revalidateWidget = new JButton("Revalidate");
      revalidateWidget.addActionListener((ev) -> {
         clientThread.invokeLater(() -> {
            if (this.selectedWidget != null) {
               this.selectedWidget.revalidate();
            }
         });
      });
      bottomPanel.add(revalidateWidget);
      JSplitPane split = new JSplitPane(1, treeScrollPane, infoScrollPane);
      this.add(split, "Center");
      this.pack();
   }

   @Subscribe
   private void onConfigChanged(ConfigChanged ev) {
      boolean onTop = this.config.inspectorAlwaysOnTop();
      this.setAlwaysOnTop(onTop);
      this.alwaysOnTop.setSelected(onTop);
   }

   private void refreshWidgets() {
      this.clientThread.invokeLater(() -> {
         Widget[] rootWidgets = this.client.getWidgetRoots();
         this.root = new DefaultMutableTreeNode();
         Widget wasSelectedWidget = this.selectedWidget;
         this.selectedWidget = null;
         Widget[] var3 = rootWidgets;
         int var4 = rootWidgets.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            Widget widget = var3[var5];
            DefaultMutableTreeNode childNode = this.addWidget("R", widget);
            if (childNode != null) {
               this.root.add(childNode);
            }
         }

         SwingUtilities.invokeLater(() -> {
            this.widgetTree.setModel(new DefaultTreeModel(this.root));
            this.setSelectedWidget(wasSelectedWidget, true);
         });
      });
   }

   private DefaultMutableTreeNode addWidget(String type, Widget widget) {
      if (widget != null && (!this.hideHidden.isSelected() || !widget.isHidden())) {
         DefaultMutableTreeNode node = new WidgetTreeNode(type, widget);
         Widget[] childComponents = widget.getDynamicChildren();
         Widget[] var5;
         int var6;
         int var7;
         Widget component;
         DefaultMutableTreeNode childNode;
         if (childComponents != null) {
            var5 = childComponents;
            var6 = childComponents.length;

            for(var7 = 0; var7 < var6; ++var7) {
               component = var5[var7];
               childNode = this.addWidget("D", component);
               if (childNode != null) {
                  ((DefaultMutableTreeNode)node).add(childNode);
               }
            }
         }

         childComponents = widget.getStaticChildren();
         if (childComponents != null) {
            var5 = childComponents;
            var6 = childComponents.length;

            for(var7 = 0; var7 < var6; ++var7) {
               component = var5[var7];
               childNode = this.addWidget("S", component);
               if (childNode != null) {
                  ((DefaultMutableTreeNode)node).add(childNode);
               }
            }
         }

         childComponents = widget.getNestedChildren();
         if (childComponents != null) {
            var5 = childComponents;
            var6 = childComponents.length;

            for(var7 = 0; var7 < var6; ++var7) {
               component = var5[var7];
               childNode = this.addWidget("N", component);
               if (childNode != null) {
                  ((DefaultMutableTreeNode)node).add(childNode);
               }
            }
         }

         return node;
      } else {
         return null;
      }
   }

   private void setSelectedWidget(Widget widget, boolean updateTree) {
      this.infoTableModel.setWidget(widget);
      if (this.selectedWidget != widget) {
         this.selectedWidget = widget;
         if (this.root != null && updateTree) {
            this.clientThread.invoke(() -> {
               Stack<Widget> treePath = new Stack();

               for(Widget w = widget; w != null; w = w.getParent()) {
                  treePath.push(w);
               }

               DefaultMutableTreeNode node = this.root;

               while(true) {
                  while(!treePath.empty()) {
                     Widget wx = (Widget)treePath.pop();
                     Enumeration<?> it = ((DefaultMutableTreeNode)node).children();

                     while(it.hasMoreElements()) {
                        WidgetTreeNode inner = (WidgetTreeNode)it.nextElement();
                        if (inner.getWidget().getId() == wx.getId() && inner.getWidget().getIndex() == wx.getIndex()) {
                           node = inner;
                           break;
                        }
                     }
                  }

                  DefaultMutableTreeNode fnode = node;
                  SwingUtilities.invokeLater(() -> {
                     this.widgetTree.getSelectionModel().clearSelection();
                     this.widgetTree.getSelectionModel().addSelectionPath(new TreePath(fnode.getPath()));
                  });
                  return;
               }
            });
         }
      }
   }

   static String getWidgetName(int componentId) {
      if (widgetNames.isEmpty()) {
         try {
            Class[] var12 = InterfaceID.class.getDeclaredClasses();
            int var2 = var12.length;

            for(int var3 = 0; var3 < var2; ++var3) {
               Class<?> innerClass = var12[var3];
               Field[] var5 = innerClass.getDeclaredFields();
               int var6 = var5.length;

               for(int var7 = 0; var7 < var6; ++var7) {
                  Field field = var5[var7];
                  int id = field.getInt((Object)null);
                  String var10000 = innerClass.getSimpleName();
                  String name = var10000 + "." + field.getName();
                  widgetNames.put(id, name);
               }
            }
         } catch (IllegalAccessException var11) {
            IllegalAccessException ex = var11;
            log.error("error setting up widget names", ex);
         }
      }

      return (String)widgetNames.get(componentId);
   }

   public void open() {
      super.open();
      this.overlayManager.add((Overlay)this.overlay.get());
      this.clientThread.invokeLater(this::addPickerWidget);
   }

   public void close() {
      this.overlayManager.remove((Overlay)this.overlay.get());
      this.clientThread.invokeLater(this::removePickerWidget);
      this.setSelectedWidget((Widget)null, false);
      super.close();
   }

   private void removePickerWidget() {
      if (this.picker != null) {
         Widget parent = this.picker.getParent();
         if (parent != null) {
            Widget[] children = parent.getChildren();
            if (children != null && children.length > this.picker.getIndex() && children[this.picker.getIndex()] == this.picker) {
               children[this.picker.getIndex()] = null;
            }
         }
      }
   }

   private void addPickerWidget() {
      this.removePickerWidget();
      int x = 10;
      int y = 2;
      Widget parent = this.client.getWidget(10485760);
      if (parent == null || parent.isHidden()) {
         parent = this.client.getWidget(58654720);
         x = 32;
         y = 0;
      }

      if (parent == null || parent.isHidden()) {
         Widget[] roots = this.client.getWidgetRoots();
         parent = (Widget)Stream.of(roots).filter((w) -> {
            return w.getType() == 0 && w.getContentType() == 0 && !w.isSelfHidden();
         }).sorted(Comparator.comparingInt((w) -> {
            return w.getRelativeX() + w.getRelativeY();
         }).reversed().thenComparingInt(Widget::getId).reversed()).findFirst().get();
         x = 4;
         y = 4;
      }

      this.picker = parent.createChild(-1, 5);
      log.info("Picker is {}.{} [{}]", new Object[]{WidgetUtil.componentToInterface(this.picker.getId()), WidgetUtil.componentToId(this.picker.getId()), this.picker.getIndex()});
      this.picker.setSpriteId(1653);
      this.picker.setOriginalWidth(15);
      this.picker.setOriginalHeight(17);
      this.picker.setOriginalX(x);
      this.picker.setOriginalY(y);
      this.picker.revalidate();
      this.picker.setTargetVerb("Select");
      this.picker.setName("Pick");
      this.picker.setClickMask(65536);
      this.picker.setNoClickThrough(true);
      this.picker.setOnTargetEnterListener(new Object[]{(ev) -> {
         this.pickerSelected = true;
         this.picker.setOpacity(30);
         this.client.setAllWidgetsAreOpTargetable(true);
      }});
      this.picker.setOnTargetLeaveListener(new Object[]{(ev) -> {
         this.onPickerDeselect();
      }});
   }

   private void onPickerDeselect() {
      this.client.setAllWidgetsAreOpTargetable(false);
      this.picker.setOpacity(0);
      this.pickerSelected = false;
   }

   @Subscribe
   private void onMenuOptionClicked(MenuOptionClicked ev) {
      if (this.pickerSelected) {
         this.onPickerDeselect();
         this.client.setWidgetSelected(false);
         ev.consume();
         Widget target = this.getWidgetForMenuOption(ev.getMenuAction(), ev.getParam0(), ev.getParam1());
         if (target != null) {
            this.setSelectedWidget(target, true);
         }
      }
   }

   @Subscribe
   private void onMenuEntryAdded(MenuEntryAdded event) {
      if (this.pickerSelected) {
         MenuEntry[] menuEntries = this.client.getMenuEntries();

         for(int i = 0; i < menuEntries.length; ++i) {
            MenuEntry entry = menuEntries[i];
            if (entry.getType() == MenuAction.WIDGET_TARGET_ON_WIDGET) {
               int var10000 = WidgetUtil.componentToInterface(entry.getParam1());
               String name = "" + var10000 + "." + WidgetUtil.componentToId(entry.getParam1());
               if (entry.getParam0() != -1) {
                  name = name + " [" + entry.getParam0() + "]";
               }

               Color color = this.colorForWidget(i, menuEntries.length);
               entry.setTarget(ColorUtil.wrapWithColorTag(name, color));
            }
         }

      }
   }

   Color colorForWidget(int index, int length) {
      float h = SELECTED_WIDGET_HUE + 0.1F + 0.8F / (float)length * (float)index;
      return Color.getHSBColor(h, 1.0F, 1.0F);
   }

   Widget getWidgetForMenuOption(MenuAction type, int param0, int param1) {
      if (type == MenuAction.WIDGET_TARGET_ON_WIDGET) {
         Widget w = this.client.getWidget(param1);
         if (param0 != -1) {
            w = w.getChild(param0);
         }

         return w;
      } else {
         return null;
      }
   }

   public static String getWidgetIdentifier(Widget widget) {
      int id = widget.getId();
      int var10000 = WidgetUtil.componentToInterface(id);
      String str = "" + var10000 + "." + WidgetUtil.componentToId(id);
      if (widget.getIndex() != -1) {
         str = str + "[" + widget.getIndex() + "]";
      }

      String name = getWidgetName(id);
      if (name != null) {
         str = str + " " + name;
      }

      return str;
   }

   public Widget getSelectedWidget() {
      return this.selectedWidget;
   }

   public boolean isPickerSelected() {
      return this.pickerSelected;
   }

   static {
      SELECTED_WIDGET_COLOR = Color.CYAN;
      float[] hsb = new float[3];
      Color.RGBtoHSB(SELECTED_WIDGET_COLOR.getRed(), SELECTED_WIDGET_COLOR.getGreen(), SELECTED_WIDGET_COLOR.getBlue(), hsb);
      SELECTED_WIDGET_HUE = hsb[0];
   }
}
