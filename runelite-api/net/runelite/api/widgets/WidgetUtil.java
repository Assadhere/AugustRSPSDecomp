package net.runelite.api.widgets;

public class WidgetUtil {
   public static int componentToInterface(int c) {
      return c >>> 16;
   }

   public static int componentToId(int c) {
      return c & '\uffff';
   }

   public static int packComponentId(int interfaceId, int childId) {
      return interfaceId << 16 | childId;
   }
}
