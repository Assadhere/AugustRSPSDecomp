package net.runelite.client.plugins.devtools;

import javax.swing.tree.DefaultMutableTreeNode;
import net.runelite.api.widgets.Widget;

class WidgetTreeNode extends DefaultMutableTreeNode {
   private final String type;

   public WidgetTreeNode(String type, Widget widget) {
      super(widget);
      this.type = type;
   }

   public Widget getWidget() {
      return (Widget)this.getUserObject();
   }

   public String toString() {
      String var10000 = this.type;
      return var10000 + " " + WidgetInspector.getWidgetIdentifier(this.getWidget());
   }
}
