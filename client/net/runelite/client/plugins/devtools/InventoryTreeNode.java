package net.runelite.client.plugins.devtools;

import javax.annotation.Nullable;
import javax.swing.tree.DefaultMutableTreeNode;

class InventoryTreeNode extends DefaultMutableTreeNode {
   final int id;
   @Nullable
   final String name;

   InventoryTreeNode(int id, @Nullable String name) {
      this.id = id;
      this.name = name;
   }

   public String toString() {
      int var10000 = this.id;
      return "" + var10000 + (this.name == null ? "" : " - " + this.name);
   }

   public int getId() {
      return this.id;
   }

   @Nullable
   public String getName() {
      return this.name;
   }
}
