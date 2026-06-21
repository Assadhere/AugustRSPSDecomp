package net.runelite.client.plugins.poh;

import com.google.common.collect.ImmutableMap;
import java.awt.image.BufferedImage;
import java.util.Map;
import net.runelite.client.util.ImageUtil;

public enum PohIcons {
   EXITPORTAL("exitportal", new int[]{4525}),
   VARROCK("varrock", new int[]{13615, 13622, 13629, 56037}),
   FALADOR("falador", new int[]{13617, 13624, 13631, 56041}),
   LUMBRIDGE("lumbridge", new int[]{13616, 13623, 13630, 56040}),
   ARDOUGNE("ardougne", new int[]{13619, 13626, 13633, 56045}),
   YANILLE("yanille", new int[]{13620, 13627, 13634, 56046}),
   CAMELOT("camelot", new int[]{13618, 13625, 13632, 56042}),
   LUNARISLE("lunarisle", new int[]{29339, 29347, 29355, 56050}),
   WATERBIRTH("waterbirth", new int[]{29342, 29350, 29358, 56053}),
   FISHINGGUILD("fishingguild", new int[]{29343, 29351, 29359, 56054}),
   SENNTISTEN("senntisten", new int[]{29340, 29348, 29356, 56051}),
   KHARYLL("kharyll", new int[]{29338, 29346, 29354, 56049}),
   ANNAKARL("annakarl", new int[]{29341, 29349, 29357, 56052}),
   KOUREND("kourend", new int[]{29345, 29353, 29361, 56056}),
   MARIM("marim", new int[]{29344, 29352, 29360, 56055}),
   TROLLSTRONGHOLD("trollstronghold", new int[]{33179, 33180, 33181, 56058}),
   GHORROCK("ghorrock", new int[]{33433, 33436, 33439, 56060}),
   CARRALLANGER("carrallanger", new int[]{33434, 33437, 33440, 56061}),
   CATHERBY("catherby", new int[]{33432, 33435, 33438, 56059}),
   WEISS("weiss", new int[]{37581, 37593, 37605, 56062}),
   APEATOLLDUNGEON("apeatolldungeon", new int[]{37592, 37604, 37616, 56073}),
   BARROWS("barrows", new int[]{37591, 37603, 37615, 56072}),
   BATTLEFRONT("battlefront", new int[]{37584, 37596, 37608, 56065}),
   CEMETERY("cemetery", new int[]{37590, 37602, 37614, 56071}),
   DRAYNORMANOR("draynormanor", new int[]{37583, 37595, 37607, 56064}),
   FENKENSTRAINSCASTLE("fenkenstrainscastle", new int[]{37587, 37599, 37611, 56068}),
   HARMONYISLAND("harmonyisland", new int[]{37589, 37601, 37613, 56070}),
   ARCEUUSLIBRARY("arceuuslibrary", new int[]{41416, 41417, 41418, 56063}),
   MINDALTAR("mindaltar", new int[]{37585, 37597, 37609, 56066}),
   SALVEGRAVEYARD("salvegraveyard", new int[]{37586, 37598, 37610, 56067}),
   WESTARDOUGNE("westardougne", new int[]{37588, 37600, 37612, 56069}),
   CIVITASILLAFORTIS("civitasillafortis", new int[]{50713, 50714, 50715, 56057}),
   ALTAR("altar", new int[]{13179, 13180, 13181, 13182, 13183, 13184, 13185, 13186, 13187, 13188, 13189, 13190, 13191, 13192, 13193, 13194, 13196, 13197, 13198, 13199}),
   POOLS("pool", new int[]{29237, 29238, 29239, 29240, 29241, 40844, 40845, 40846, 40847, 40848}),
   GLORY("glory", new int[]{13523}),
   REPAIR("repair", new int[]{6802}),
   SPELLBOOKALTAR("spellbook", new int[]{29147, 29148, 27979, 29150}),
   JEWELLERYBOX("jewellery", new int[]{29154, 29155, 29156}),
   MAGICTRAVEL("transportation", new int[]{29227, 44936, 27097, 29228, 29229, 31554, 40778, 40779}),
   PORTALNEXUS("portalnexus", new int[]{33408, 33409, 33410, 56075}),
   XERICSTALISMAN("xericstalisman", new int[]{33411, 33412, 33413, 33414, 33415, 33419}),
   DIGSITEPENDANT("digsitependant", new int[]{33416, 33417, 33418, 33420}),
   MYTHICALCAPE("mythicalcape", new int[]{31986, 31983});

   private static final Map<Integer, PohIcons> minimapIcons;
   private final String imageResource;
   private final int[] Ids;
   private BufferedImage image;

   private PohIcons(String imageResource, int... ids) {
      this.imageResource = imageResource;
      this.Ids = ids;
   }

   public static PohIcons getIcon(int id) {
      return (PohIcons)minimapIcons.get(id);
   }

   public BufferedImage getImage() {
      if (this.image != null) {
         return this.image;
      } else {
         this.image = ImageUtil.loadImageResource(this.getClass(), this.getImageResource() + ".png");
         return this.image;
      }
   }

   public String getImageResource() {
      return this.imageResource;
   }

   public int[] getIds() {
      return this.Ids;
   }

   static {
      ImmutableMap.Builder<Integer, PohIcons> builder = new ImmutableMap.Builder();
      PohIcons[] var1 = values();
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         PohIcons icon = var1[var3];
         int[] var5 = icon.getIds();
         int var6 = var5.length;

         for(int var7 = 0; var7 < var6; ++var7) {
            Integer spotId = var5[var7];
            builder.put(spotId, icon);
         }
      }

      minimapIcons = builder.build();
   }
}
