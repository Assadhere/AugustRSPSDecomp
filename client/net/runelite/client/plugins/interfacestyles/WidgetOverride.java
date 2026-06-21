package net.runelite.client.plugins.interfacestyles;

enum WidgetOverride {
   FIXED_CORNER_TOP_LEFT_2005(Skin.AROUND_2005, "1026", new int[]{35913792}),
   FIXED_CORNER_TOP_RIGHT_2005(Skin.AROUND_2005, "1027", new int[]{35913798}),
   FIXED_CORNER_BOTTOM_LEFT_2005(Skin.AROUND_2005, "1028", new int[]{35913776}),
   FIXED_CORNER_BOTTOM_RIGHT_2005(Skin.AROUND_2005, "1029", new int[]{35913782}),
   FIXED_TOP_LEFT_2005(Skin.AROUND_2005, "1030_top_left", new int[]{35913793, 35913794}),
   FIXED_TOP_RIGHT_2005(Skin.AROUND_2005, "1030_top_right", new int[]{35913796, 35913797}),
   FIXED_TOP_MIDDLE_2005(Skin.AROUND_2005, "1030_top_middle", new int[]{35913795}),
   FIXED_BOTTOM_LEFT_2005(Skin.AROUND_2005, "1030_bottom_left", new int[]{35913778, 35913777}),
   FIXED_BOTTOM_RIGHT_2005(Skin.AROUND_2005, "1030_bottom_middle", new int[]{35913779}),
   FIXED_BOTTOM_MIDDLE_2005(Skin.AROUND_2005, "1030_bottom_right", new int[]{35913780, 35913781});

   private final Skin skin;
   private final String name;
   private final int[] widgetInfo;

   private WidgetOverride(Skin skin, String name, int... widgetInfo) {
      this.skin = skin;
      this.name = name;
      this.widgetInfo = widgetInfo;
   }

   public Skin getSkin() {
      return this.skin;
   }

   public String getName() {
      return this.name;
   }

   public int[] getWidgetInfo() {
      return this.widgetInfo;
   }
}
