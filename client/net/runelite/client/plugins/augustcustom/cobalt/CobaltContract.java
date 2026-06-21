package net.runelite.client.plugins.augustcustom.cobalt;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public final class CobaltContract {
   public static final int IF_FIRST = 3009;
   public static final int IF_SUB = 20;
   public static final int TRIGGER_REQUEST_LIST = 1;
   public static final int TRIGGER_COBALT = 2;
   public static final int TRIGGER_STOP = 3;
   public static final int TRIGGER_REPORT_CAMERA = 4;
   public static final int TRIGGER_REPORT_SCROLL = 5;
   public static final int TRIGGER_REPORT_FLOATER_SIZE = 6;
   public static final int[] FLOATER_WINDOWS = new int[]{198705153};
   public static final int TRIGGER_REPORT_TAB = 7;
   public static final int TRIGGER_REPORT_MENU = 8;
   public static final int TOPLEVEL_PANEL_VARC = 171;
   public static final int TOPLEVEL_SIDEBUTTON_SWITCH = 915;
   public static final int MAX_MENU_ENTRIES = 20;
   public static final int CHAT_ICON_ARCHIVE = 423;

   private CobaltContract() {
   }

   public static enum IronmanFilter {
      IRONMAN("Ironman", 15, new int[]{1}),
      HARDCORE("Hardcore Ironman", 17, new int[]{2}),
      ULTIMATE("Ultimate Ironman", 16, new int[]{3}),
      GROUP("Group Ironman", 41, new int[]{4}),
      HARDCORE_GROUP("Hardcore GIM", 42, new int[]{5}),
      PERMA_DEATH("Perma-Death", 30, new int[]{6}),
      SWAPPER_GROUP("Swapper GIM", 43, new int[]{100});

      private final String label;
      private final int spriteFile;
      private final Set<Integer> ids;

      private IronmanFilter(String label, int spriteFile, int... ids) {
         this.label = label;
         this.spriteFile = spriteFile;
         this.ids = (Set)Arrays.stream(ids).boxed().collect(Collectors.toSet());
      }

      public String getLabel() {
         return this.label;
      }

      public int getSpriteFile() {
         return this.spriteFile;
      }

      public boolean matches(int ironmanModeId) {
         return this.ids.contains(ironmanModeId);
      }
   }

   public static enum GameModeFilter {
      PVMER("PvMer", 5, new int[]{1}),
      SKILLER("Skiller", 3, new int[]{2}),
      MASOCHIST("Masochist", 6, new int[]{3}),
      SLAYER("Slayer Locked", 53, new int[]{4}),
      LEAGUE("League", 7, new int[]{5, 6, 7, 8}),
      RANDOMISED_LEAGUE("Randomised League", 56, new int[]{9});

      private final String label;
      private final int spriteFile;
      private final Set<Integer> ids;

      private GameModeFilter(String label, int spriteFile, int... ids) {
         this.label = label;
         this.spriteFile = spriteFile;
         this.ids = (Set)Arrays.stream(ids).boxed().collect(Collectors.toSet());
      }

      public String getLabel() {
         return this.label;
      }

      public int getSpriteFile() {
         return this.spriteFile;
      }

      public boolean matches(int gameModeId) {
         return this.ids.contains(gameModeId);
      }
   }
}
