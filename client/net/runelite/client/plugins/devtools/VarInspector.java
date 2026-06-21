package net.runelite.client.plugins.devtools;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.inject.Inject;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.border.CompoundBorder;
import net.runelite.api.Client;
import net.runelite.api.IndexDataBase;
import net.runelite.api.VarbitComposition;
import net.runelite.api.events.VarClientIntChanged;
import net.runelite.api.events.VarClientStrChanged;
import net.runelite.api.events.VarbitChanged;
import net.runelite.api.gameval.VarClientID;
import net.runelite.api.gameval.VarPlayerID;
import net.runelite.api.gameval.VarbitID;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.eventbus.EventBus;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.DynamicGridLayout;
import net.runelite.client.ui.FontManager;

class VarInspector extends DevToolsFrame {
   private static final int MAX_LOG_ENTRIES = 10000;
   private static final int VARBITS_ARCHIVE_ID = 14;
   private static final Map<Integer, String> VARBIT_NAMES = DevToolsPlugin.loadFieldNames(VarbitID.class);
   private static final Map<Integer, String> VARC_NAMES = DevToolsPlugin.loadFieldNames(VarClientID.class);
   private static final Map<Integer, String> VARP_NAMES = DevToolsPlugin.loadFieldNames(VarPlayerID.class);
   private final Client client;
   private final ClientThread clientThread;
   private final EventBus eventBus;
   private final JPanel tracker = new JPanel();
   private int lastTick = 0;
   private int[] oldVarps = null;
   private int[] oldVarps2 = null;
   private Multimap<Integer, Integer> varbits;
   private Map<Integer, Object> varcs = null;

   @Inject
   VarInspector(Client client, ClientThread clientThread, EventBus eventBus) {
      this.client = client;
      this.clientThread = clientThread;
      this.eventBus = eventBus;
      this.setTitle("RuneLite Var Inspector");
      this.setLayout(new BorderLayout());
      this.tracker.setLayout(new DynamicGridLayout(0, 1, 0, 3));
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
      this.add(trackerScroller, "Center");
      JPanel trackerOpts = new JPanel();
      trackerOpts.setLayout(new FlowLayout());
      VarType[] var8 = VarInspector.VarType.values();
      int var9 = var8.length;

      for(int var10 = 0; var10 < var9; ++var10) {
         VarType cb = var8[var10];
         trackerOpts.add(cb.getCheckBox());
      }

      JButton clearBtn = new JButton("Clear");
      clearBtn.addActionListener((e) -> {
         this.tracker.removeAll();
         this.tracker.revalidate();
      });
      trackerOpts.add(clearBtn);
      this.add(trackerOpts, "South");
      this.pack();
   }

   private void addVarLog(VarType type, String name, int old, int neew) {
      this.addVarLog(type, name, Integer.toString(old), Integer.toString(neew));
   }

   private void addVarLog(VarType type, String name, String old, String neew) {
      if (type.getCheckBox().isSelected()) {
         int tick = this.client.getTickCount();
         SwingUtilities.invokeLater(() -> {
            if (tick != this.lastTick) {
               this.lastTick = tick;
               JLabel header = new JLabel("Tick " + tick);
               header.setFont(FontManager.getRunescapeSmallFont());
               header.setBorder(new CompoundBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, ColorScheme.LIGHT_GRAY_COLOR), BorderFactory.createEmptyBorder(3, 6, 0, 0)));
               this.tracker.add(header);
            }

            this.tracker.add(new JLabel(String.format("%s %s changed: %s -> %s", type.getName(), name, old, neew)));

            while(this.tracker.getComponentCount() > 10000) {
               this.tracker.remove(0);
            }

            this.tracker.revalidate();
         });
      }
   }

   @Subscribe
   public void onVarbitChanged(VarbitChanged varbitChanged) {
      int index = varbitChanged.getIndex();
      int[] varps = this.client.getVarps();
      Iterator var4 = this.varbits.get(index).iterator();

      int neew;
      while(var4.hasNext()) {
         neew = (Integer)var4.next();
         int old = this.client.getVarbitValue(this.oldVarps, neew);
         int neew = this.client.getVarbitValue(varps, neew);
         if (old != neew) {
            this.client.setVarbitValue(this.oldVarps2, neew, neew);
            String name = (String)VARBIT_NAMES.getOrDefault(neew, Integer.toString(neew));
            this.addVarLog(VarInspector.VarType.VARBIT, name, old, neew);
         }
      }

      int old = this.oldVarps2[index];
      neew = varps[index];
      if (old != neew) {
         String name = (String)VARP_NAMES.get(index);
         if (name != null) {
            name = name + "(" + index + ")";
         } else {
            name = Integer.toString(index);
         }

         this.addVarLog(VarInspector.VarType.VARP, name, old, neew);
      }

      System.arraycopy(this.client.getVarps(), 0, this.oldVarps, 0, this.oldVarps.length);
      System.arraycopy(this.client.getVarps(), 0, this.oldVarps2, 0, this.oldVarps2.length);
   }

   @Subscribe
   public void onVarClientIntChanged(VarClientIntChanged e) {
      int idx = e.getIndex();
      int neew = (Integer)this.client.getVarcMap().getOrDefault(idx, 0);
      int old = (Integer)this.varcs.getOrDefault(idx, 0);
      this.varcs.put(idx, neew);
      if (old != neew) {
         String name = (String)VARC_NAMES.getOrDefault(idx, Integer.toString(idx));
         this.addVarLog(VarInspector.VarType.VARCINT, name, old, neew);
      }

   }

   @Subscribe
   public void onVarClientStrChanged(VarClientStrChanged e) {
      int idx = e.getIndex();
      String neew = (String)this.client.getVarcMap().getOrDefault(idx, "");
      String old = (String)this.varcs.getOrDefault(idx, "");
      this.varcs.put(idx, neew);
      if (!Objects.equals(old, neew)) {
         String name = (String)VARC_NAMES.getOrDefault(idx, Integer.toString(idx));
         if (old != null) {
            old = "\"" + old + "\"";
         } else {
            old = "null";
         }

         if (neew != null) {
            neew = "\"" + neew + "\"";
         } else {
            neew = "null";
         }

         this.addVarLog(VarInspector.VarType.VARCSTR, name, old, neew);
      }

   }

   public void open() {
      if (this.oldVarps == null) {
         this.oldVarps = new int[this.client.getVarps().length];
         this.oldVarps2 = new int[this.client.getVarps().length];
      }

      System.arraycopy(this.client.getVarps(), 0, this.oldVarps, 0, this.oldVarps.length);
      System.arraycopy(this.client.getVarps(), 0, this.oldVarps2, 0, this.oldVarps2.length);
      this.varcs = new HashMap(this.client.getVarcMap());
      this.varbits = HashMultimap.create();
      this.clientThread.invoke(() -> {
         IndexDataBase indexVarbits = this.client.getIndexConfig();
         int[] varbitIds = indexVarbits.getFileIds(14);
         int[] var3 = varbitIds;
         int var4 = varbitIds.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            int id = var3[var5];
            VarbitComposition varbit = this.client.getVarbit(id);
            if (varbit != null) {
               this.varbits.put(varbit.getIndex(), id);
            }
         }

      });
      this.eventBus.register(this);
      super.open();
   }

   public void close() {
      super.close();
      this.tracker.removeAll();
      this.eventBus.unregister((Object)this);
      this.varcs = null;
      this.varbits = null;
   }

   private static enum VarType {
      VARBIT("Varbit"),
      VARP("VarPlayer"),
      VARCINT("VarClientInt"),
      VARCSTR("VarClientStr");

      private final String name;
      private final JCheckBox checkBox;

      private VarType(String name) {
         this.name = name;
         this.checkBox = new JCheckBox(name, true);
      }

      public String getName() {
         return this.name;
      }

      public JCheckBox getCheckBox() {
         return this.checkBox;
      }
   }
}
