package net.runelite.client.plugins.devtools;

import javax.swing.tree.DefaultMutableTreeNode;

class InventoryLogNode extends DefaultMutableTreeNode {
   private final InventoryLog log;

   InventoryLogNode(InventoryLog log) {
      this.log = log;
   }

   public String toString() {
      return "Tick: " + this.log.getTick();
   }

   public InventoryLog getLog() {
      return this.log;
   }
}
