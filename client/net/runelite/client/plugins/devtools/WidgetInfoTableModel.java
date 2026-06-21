package net.runelite.client.plugins.devtools;

import com.google.common.collect.ImmutableMap;
import com.google.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import javax.swing.SwingUtilities;
import javax.swing.table.AbstractTableModel;
import net.runelite.api.Client;
import net.runelite.api.WidgetNode;
import net.runelite.api.widgets.Widget;
import net.runelite.client.callback.ClientThread;

public class WidgetInfoTableModel extends AbstractTableModel {
   @Inject
   private ClientThread clientThread;
   @Inject
   private Client client;
   private static final int COL_FIELD = 0;
   private static final int COL_VALUE = 1;
   private final List<WidgetField<?>> fields = this.populateWidgetFields();
   private Widget widget = null;
   private Map<WidgetField<?>, Object> values = null;

   public void setWidget(Widget w) {
      this.clientThread.invoke(() -> {
         Map<WidgetField<?>, Object> newValues = w == null ? null : (Map)this.fields.stream().collect(ImmutableMap.toImmutableMap(Function.identity(), (i) -> {
            return i.getValue(w);
         }));
         SwingUtilities.invokeLater(() -> {
            this.widget = w;
            this.values = newValues;
            this.fireTableStructureChanged();
         });
      });
   }

   public String getColumnName(int col) {
      switch (col) {
         case 0:
            return "Field";
         case 1:
            return "Value";
         default:
            return null;
      }
   }

   public int getColumnCount() {
      return 2;
   }

   public int getRowCount() {
      return this.values == null ? 0 : this.values.size();
   }

   public Object getValueAt(int rowIndex, int columnIndex) {
      WidgetField<?> field = (WidgetField)this.fields.get(rowIndex);
      switch (columnIndex) {
         case 0:
            return field.getName();
         case 1:
            return this.values.get(field);
         default:
            return null;
      }
   }

   public boolean isCellEditable(int rowIndex, int columnIndex) {
      if (columnIndex == 1) {
         WidgetField<?> field = (WidgetField)this.fields.get(rowIndex);
         return field.isSettable();
      } else {
         return false;
      }
   }

   public void setValueAt(Object value, int rowIndex, int columnIndex) {
      WidgetField<?> field = (WidgetField)this.fields.get(rowIndex);
      this.clientThread.invoke(() -> {
         field.setValue(this.widget, value);
         this.setWidget(this.widget);
      });
   }

   private List<WidgetField<?>> populateWidgetFields() {
      List<WidgetField<?>> out = new ArrayList();
      out.add(new WidgetField("Id", Widget::getId));
      out.add(new WidgetField("Type", Widget::getType, Widget::setType, Integer.class));
      out.add(new WidgetField("ContentType", Widget::getContentType, Widget::setContentType, Integer.class));
      out.add(new WidgetField("ParentId", Widget::getParentId));
      out.add(new WidgetField("SelfHidden", Widget::isSelfHidden, Widget::setHidden, Boolean.class));
      out.add(new WidgetField("Hidden", Widget::isHidden));
      out.add(new WidgetField("Text", Widget::getText, Widget::setText, String.class));
      out.add(new WidgetField("TextColor", (w) -> {
         return Integer.toString(w.getTextColor(), 16);
      }, (w, str) -> {
         w.setTextColor(Integer.parseInt(str, 16));
      }, String.class));
      out.add(new WidgetField("Opacity", Widget::getOpacity, Widget::setOpacity, Integer.class));
      out.add(new WidgetField("FontId", Widget::getFontId, Widget::setFontId, Integer.class));
      out.add(new WidgetField("TextShadowed", Widget::getTextShadowed, Widget::setTextShadowed, Boolean.class));
      out.add(new WidgetField("Name", (w) -> {
         return w.getName().trim();
      }, Widget::setName, String.class));
      out.add(new WidgetField("ItemId", Widget::getItemId, Widget::setItemId, Integer.class));
      out.add(new WidgetField("ItemQuantity", Widget::getItemQuantity, Widget::setItemQuantity, Integer.class));
      out.add(new WidgetField("ItemQuantityMode", Widget::getItemQuantityMode, Widget::setItemQuantityMode, Integer.class));
      out.add(new WidgetField("ModelId", Widget::getModelId, Widget::setModelId, Integer.class));
      out.add(new WidgetField("ModelType", Widget::getModelType, Widget::setModelType, Integer.class));
      out.add(new WidgetField("AnimationId", Widget::getAnimationId, Widget::setAnimationId, Integer.class));
      out.add(new WidgetField("RotationX", Widget::getRotationX, Widget::setRotationX, Integer.class));
      out.add(new WidgetField("RotationY", Widget::getRotationY, Widget::setRotationY, Integer.class));
      out.add(new WidgetField("RotationZ", Widget::getRotationZ, Widget::setRotationZ, Integer.class));
      out.add(new WidgetField("ModelZoom", Widget::getModelZoom, Widget::setModelZoom, Integer.class));
      out.add(new WidgetField("SpriteId", Widget::getSpriteId, Widget::setSpriteId, Integer.class));
      out.add(new WidgetField("SpriteTiling", Widget::getSpriteTiling, Widget::setSpriteTiling, Boolean.class));
      out.add(new WidgetField("BorderType", Widget::getBorderType, Widget::setBorderType, Integer.class));
      out.add(new WidgetField("IsIf3", Widget::isIf3));
      out.add(new WidgetField("HasListener", Widget::hasListener, Widget::setHasListener, Boolean.class));
      out.add(new WidgetField("Filled", Widget::isFilled, Widget::setFilled, Boolean.class));
      out.add(new WidgetField("OriginalX", Widget::getOriginalX, Widget::setOriginalX, Integer.class));
      out.add(new WidgetField("OriginalY", Widget::getOriginalY, Widget::setOriginalY, Integer.class));
      out.add(new WidgetField("OriginalWidth", Widget::getOriginalWidth, Widget::setOriginalWidth, Integer.class));
      out.add(new WidgetField("OriginalHeight", Widget::getOriginalHeight, Widget::setOriginalHeight, Integer.class));
      out.add(new WidgetField("XPositionMode", Widget::getXPositionMode, Widget::setXPositionMode, Integer.class));
      out.add(new WidgetField("YPositionMode", Widget::getYPositionMode, Widget::setYPositionMode, Integer.class));
      out.add(new WidgetField("WidthMode", Widget::getWidthMode, Widget::setWidthMode, Integer.class));
      out.add(new WidgetField("HeightMode", Widget::getHeightMode, Widget::setHeightMode, Integer.class));
      out.add(new WidgetField("LineHeight", Widget::getLineHeight, Widget::setLineHeight, Integer.class));
      out.add(new WidgetField("XTextAlignment", Widget::getXTextAlignment, Widget::setXTextAlignment, Integer.class));
      out.add(new WidgetField("YTextAlignment", Widget::getYTextAlignment, Widget::setYTextAlignment, Integer.class));
      out.add(new WidgetField("RelativeX", Widget::getRelativeX, Widget::setRelativeX, Integer.class));
      out.add(new WidgetField("RelativeY", Widget::getRelativeY, Widget::setRelativeY, Integer.class));
      out.add(new WidgetField("Width", Widget::getWidth, Widget::setWidth, Integer.class));
      out.add(new WidgetField("Height", Widget::getHeight, Widget::setHeight, Integer.class));
      out.add(new WidgetField("CanvasLocation", Widget::getCanvasLocation));
      out.add(new WidgetField("Bounds", Widget::getBounds));
      out.add(new WidgetField("ScrollX", Widget::getScrollX, Widget::setScrollX, Integer.class));
      out.add(new WidgetField("ScrollY", Widget::getScrollY, Widget::setScrollY, Integer.class));
      out.add(new WidgetField("ScrollWidth", Widget::getScrollWidth, Widget::setScrollWidth, Integer.class));
      out.add(new WidgetField("ScrollHeight", Widget::getScrollHeight, Widget::setScrollHeight, Integer.class));
      out.add(new WidgetField("DragDeadZone", Widget::getDragDeadZone, Widget::setDragDeadZone, Integer.class));
      out.add(new WidgetField("DragDeadTime", Widget::getDragDeadTime, Widget::setDragDeadTime, Integer.class));
      out.add(new WidgetField("NoClickThrough", Widget::getNoClickThrough, Widget::setNoClickThrough, Boolean.class));
      out.add(new WidgetField("NoScrollThrough", Widget::getNoScrollThrough, Widget::setNoScrollThrough, Boolean.class));
      out.add(new WidgetField("TargetVerb", Widget::getTargetVerb, Widget::setTargetVerb, String.class));
      out.add(new WidgetField("DragParent", Widget::getDragParent));
      out.add(new WidgetField("ModalMode", (w) -> {
         WidgetNode attachment = (WidgetNode)this.client.getComponentTable().get((long)w.getParentId());
         return attachment != null ? attachment.getModalMode() : null;
      }));
      out.add(new WidgetField("OnOpListener", Widget::getOnOpListener));
      out.add(new WidgetField("OnKeyListener", Widget::getOnKeyListener));
      out.add(new WidgetField("OnLoadListener", Widget::getOnLoadListener));
      out.add(new WidgetField("OnInvTransmitListener", Widget::getOnInvTransmitListener));
      out.add(new WidgetField("OnVarTransmitListener", Widget::getOnVarTransmitListener));
      return out;
   }
}
