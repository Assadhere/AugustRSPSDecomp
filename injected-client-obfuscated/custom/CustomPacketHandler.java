package custom;

import custom.model.SkillPrestigeInfo;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import osrs.Client;

public class CustomPacketHandler {
   private static final int HIGHLIGHT_NPCS = 30000;
   private static final int UPDATE_SLAYER_INFO = 30001;
   private static final int ADD_TO_LOOT_TRACKER_SCRIPT = 30002;
   private static final int UPDATE_NPC_INFO_SCRIPT = 30003;
   private static final int UPDATE_EVENTS_INFO_SCRIPT = 30004;
   private static final int UPDATE_ITEM_STATS_SCRIPT = 30005;
   private static final int UPDATE_SERVER_AND_PLAYER_INFO_SCRIPT = 30006;
   private static final int UPDATE_TIMERS = 30007;
   private static final int UPDATE_BONUS_XP = 30008;
   private static final int UPDATE_HIGH_SCORES = 30009;
   private static final int UPDATE_COUNT_GRAPHICS = 30010;
   private static final int UPDATE_GE_HISTORY = 30011;
   private static final int UPDATE_GE_ITEM_DETAIL = 30012;
   private static final int UPDATE_HISCORES_TOPLIST = 30013;
   private static final int UPDATE_GE_MARKET_ERROR = 30014;
   private static final int SOFT_HIDE_NPCS = 30015;
   private static final int COBALT_LIST_RESET = 30016;
   private static final int COBALT_LIST_CHUNK = 30017;
   private static final int COBALT_SESSION = 30018;
   private static final int COBALT_CAMERA = 30019;
   private static final int COBALT_CLICK = 30020;
   private static final int COBALT_REPORTING = 30021;
   private static final int COBALT_SCROLL = 30022;
   private static final int COBALT_RESOLUTION = 30023;
   private static final int COBALT_MENU = 30024;
   private static final int COBALT_TAB = 30025;
   private static final int UPDATE_DISCORD_PRESENCE = 30026;
   private static final int UPDATE_GHOST_NPCS = 30027;
   private static final int DISCORD_PRESENCE_MIN_VERSION = 1;
   private static final int DISCORD_PRESENCE_MAX_VERSION = 1;
   private static final int DISCORD_PRESENCE_FIELD_COUNT = 7;
   private static final int MAX_COBALT_ENTRIES = 4096;
   private static final int MAX_HISCORES_COUNTS_SIZE = 1024;

   public static boolean handleCustomClientScript(Object[] var0) {
      try {
         int var1 = (Integer)var0[0];
         Client var2 = Client.s;
         if (var1 == 30000) {
            var2.getCallbacks().post(new HighlightNpcsScript(splitClientscriptArgumentIntoInt(var0[1])));
            return true;
         } else {
            String[] var3;
            int var4;
            int var5;
            int var7;
            String var9;
            int var36;
            String var45;
            if (var1 == 30001) {
               var3 = splitClientscriptArgument(var0[1]);
               var4 = Integer.parseInt(var3[0]);
               var5 = Integer.parseInt(var3[1]);
               var36 = Integer.parseInt(var3[2]);
               var7 = Integer.parseInt(var3[3]);
               var45 = var3[4];
               var9 = var3[5];
               var2.getCallbacks().post(new UpdateSlayerInfoScript(var4, var5, var36, var7, var45, var9));
               return true;
            } else {
               int var44;
               int var47;
               if (var1 == 30002) {
                  var3 = splitClientscriptArgument(var0[1]);
                  var4 = Integer.parseInt(var3[0]);
                  var5 = Integer.parseInt(var3[1]);
                  int[] var52 = new int[var5];
                  int[] var71 = new int[var5];
                  var44 = 2;

                  for(var47 = 0; var47 < var5; ++var47) {
                     var52[var47] = Integer.parseInt(var3[var44++]);
                     var71[var47] = Integer.parseInt(var3[var44++]);
                  }

                  var9 = var3[var44];
                  var2.getCallbacks().post(new AddToLootTrackerScript(var9, var4, var52, var71));
                  return true;
               } else if (var1 == 30003) {
                  HashMap var46 = new HashMap();
                  String[] var83 = splitClientscriptArgument(var0[1]);
                  var5 = Integer.parseInt(var83[0]);
                  var36 = 1;

                  for(var7 = 0; var7 < var5; ++var7) {
                     var44 = Integer.parseInt(var83[var36++]);
                     var47 = Integer.parseInt(var83[var36++]);
                     var46.put(var44, new UpdateNpcInfoScript.NpcInfoMessage(var47));
                  }

                  var2.getCallbacks().post(new UpdateNpcInfoScript(var46));
                  return true;
               } else {
                  int var10;
                  int var14;
                  int var15;
                  int var20;
                  int var21;
                  int var66;
                  int var75;
                  if (var1 == 30004) {
                     var3 = splitClientscriptArgument(var0[1]);
                     var4 = Integer.parseInt(var3[0]);
                     var5 = Integer.parseInt(var3[1]);
                     UpdateEventsInfoScript.EventPanelUpdateMessage[] var50 = new UpdateEventsInfoScript.EventPanelUpdateMessage[var5];
                     var7 = 2;

                     for(var44 = 0; var44 < var5; ++var44) {
                        boolean var53 = Integer.parseInt(var3[var7++]) == 1;
                        if (var53) {
                           var10 = Integer.parseInt(var3[var7++]);
                           String var59 = var3[var7++];
                           String var62 = var3[var7++];
                           String var64 = var3[var7++];
                           var14 = Integer.parseInt(var3[var7++]);
                           var15 = Integer.parseInt(var3[var7++]);
                           var66 = Integer.parseInt(var3[var7++]);
                           boolean var68 = Integer.parseInt(var3[var7++]) == 1;
                           UpdateEventsInfoScript.EventPanelButton var69 = null;
                           UpdateEventsInfoScript.EventProgressBar var70 = null;
                           if (var68) {
                              var20 = Integer.parseInt(var3[var7++]);
                              String var74 = var3[var7++];
                              var69 = new UpdateEventsInfoScript.EventPanelButton(var20, var74);
                           }

                           boolean var72 = Integer.parseInt(var3[var7++]) == 1;
                           if (var72) {
                              var21 = Integer.parseInt(var3[var7++]);
                              var75 = Integer.parseInt(var3[var7++]);
                              var70 = new UpdateEventsInfoScript.EventProgressBar(var21, var75);
                           }

                           var50[var44] = new UpdateEventsInfoScript.EventPanelUpdateMessage(var10, var4, true, var59, new UpdateEventsInfoScript.EventPanel(var59, var66, var62, var64, var14, var15, var69, var70));
                        } else {
                           String var58 = var3[var7++];
                           var50[var44] = new UpdateEventsInfoScript.EventPanelUpdateMessage(-1, var4, false, var58, (UpdateEventsInfoScript.EventPanel)null);
                        }
                     }

                     var2.getCallbacks().post(new UpdateEventsInfoScript(var50));
                     return true;
                  } else {
                     int var11;
                     int var56;
                     if (var1 == 30005) {
                        var3 = splitClientscriptArgument(var0[1]);
                        var4 = Integer.parseInt(var3[0]);
                        var5 = 1;
                        HashMap var49 = new HashMap();

                        for(var7 = 0; var7 < var4; ++var7) {
                           var44 = Integer.parseInt(var3[var5++]);
                           var47 = Integer.parseInt(var3[var5++]);
                           boolean var55 = Integer.parseInt(var3[var5++]) == 1;
                           var11 = Integer.parseInt(var3[var5++]);
                           var56 = Integer.parseInt(var3[var5++]);
                           int[] var63 = new int[14];

                           for(var14 = 0; var14 < 14; ++var14) {
                              var63[var14] = Integer.parseInt(var3[var5++]);
                           }

                           var49.put(var44, new UpdateItemStatInfoScript.ItemStatInfo(var47, var55, var11, var56, var63));
                        }

                        var2.getCallbacks().post(new UpdateItemStatInfoScript(var49));
                        return true;
                     } else if (var1 == 30006) {
                        var3 = splitClientscriptArgument(var0[1]);
                        String var81 = var3[0];
                        var5 = Integer.parseInt(var3[1]);
                        ArrayList var48 = new ArrayList();
                        var7 = 2;

                        for(var44 = 0; var44 < var5; ++var44) {
                           var47 = Integer.parseInt(var3[var7++]);
                           var10 = Integer.parseInt(var3[var7++]);
                           var11 = Integer.parseInt(var3[var7++]);
                           var56 = Integer.parseInt(var3[var7++]);
                           var48.add(new SkillPrestigeInfo(var47, var10, var11, var56));
                        }

                        var45 = var3[var7++];
                        var47 = Integer.parseInt(var3[var7++]);
                        var10 = Integer.parseInt(var3[var7++]);
                        ArrayList var57 = new ArrayList();

                        for(var56 = 0; var56 < var10; ++var56) {
                           var57.add(var3[var7++]);
                        }

                        var2.getCallbacks().post(new UpdateServerAndPlayerInfoScript(var81, var48, var45, var47, var57));
                        return true;
                     } else {
                        String var43;
                        if (var1 == 30007) {
                           var3 = splitClientscriptArgument(var0[1]);
                           var4 = Integer.parseInt(var3[0]);
                           boolean var40 = Integer.parseInt(var3[1]) == 0;
                           var36 = Integer.parseInt(var3[2]);
                           var43 = var3[3];
                           var45 = var3[4];
                           var2.getCallbacks().post(new UpdateTimerScript(var4, var40, var36, var43, var45));
                           return true;
                        } else if (var1 == 30008) {
                           var3 = splitClientscriptArgument(var0[1]);
                           var4 = Integer.parseInt(var3[0]);
                           HashMap var38 = new HashMap();
                           var36 = 1;

                           for(var7 = 0; var7 < var4; ++var7) {
                              var44 = Integer.parseInt(var3[var36++]);
                              var47 = Integer.parseInt(var3[var36++]);
                              long var51 = Long.parseLong(var3[var36++]);
                              var38.put(var44, new UpdateBonusXpScript.BonusXpState(var44, var47, var51));
                           }

                           var2.getCallbacks().post(new UpdateBonusXpScript(var38));
                           return true;
                        } else {
                           String var6;
                           int var19;
                           int var24;
                           int var25;
                           long var26;
                           String var34;
                           byte var42;
                           if (var1 == 30009) {
                              var3 = splitClientscriptArgument(var0[1]);
                              var42 = 0;
                              var4 = var42 + 1;
                              var34 = var3[var42];
                              var6 = var3[var4++];
                              var7 = Integer.parseInt(var3[var4++]);
                              var44 = Integer.parseInt(var3[var4++]);
                              var47 = Integer.parseInt(var3[var4++]);
                              var10 = Integer.parseInt(var3[var4++]);
                              long var54 = Long.parseLong(var3[var4++]);
                              long var61 = Long.parseLong(var3[var4++]);
                              var15 = Integer.parseInt(var3[var4++]);
                              var66 = Integer.parseInt(var3[var4++]);
                              long var67 = Long.parseLong(var3[var4++]);
                              var19 = Integer.parseInt(var3[var4++]);
                              var20 = Integer.parseInt(var3[var4++]);
                              LinkedHashMap var73 = new LinkedHashMap();

                              int var29;
                              for(var75 = 0; var75 < 23; ++var75) {
                                 long var23 = Long.parseLong(var3[var4++]);
                                 var25 = Integer.parseInt(var3[var4++]);
                                 int var79 = Integer.parseInt(var3[var4++]);
                                 long var27 = Long.parseLong(var3[var4++]);
                                 var29 = Integer.parseInt(var3[var4++]);
                                 int var30 = Integer.parseInt(var3[var4++]);
                                 var73.put(var75, new UpdateHiscoreScript.SkillData(var23, var25, var79, var27, var29, var30));
                              }

                              var75 = Integer.parseInt(var3[var4++]);
                              if (var75 >= 0 && var75 <= 1024) {
                                 ArrayList var76 = new ArrayList(var75);

                                 for(var24 = 0; var24 < var75; ++var24) {
                                    String var77 = var3[var4++];
                                    var26 = Long.parseLong(var3[var4++]);
                                    int var28 = Integer.parseInt(var3[var4++]);
                                    var29 = Integer.parseInt(var3[var4++]);
                                    var76.add(new UpdateHiscoreScript.CountEntry(var77, var26, var28, var29));
                                 }

                                 var24 = Integer.parseInt(var3[var4++]);
                                 UpdateHiscoreScript.Perks var78 = null;
                                 if (var24 == 1) {
                                    String var80 = var3[var4++];
                                    String var82 = var3[var4++];
                                    String var84 = var3[var4++];
                                    String var85 = var3[var4++];
                                    String var86 = var3[var4++];
                                    String var31 = var3[var4++];
                                    var78 = new UpdateHiscoreScript.Perks(var80, var82, var84, var85, var86, var31);
                                 }

                                 if (var4 != var3.length) {
                                    System.err.println("UPDATE_HIGH_SCORES: parser consumed " + var4 + " of " + var3.length + " fields — wire format may have drifted from this client's spec");
                                 }

                                 var2.getCallbacks().post(new UpdateHiscoreScript(var34, var6, var7, var44, var47, var10, var61, var15, var66, var67, var19, var20, var54, var73, var76, var78));
                                 return true;
                              } else {
                                 System.err.println("UPDATE_HIGH_SCORES: malformed countsSize=" + var75);
                                 return false;
                              }
                           } else if (var1 == 30010) {
                              var3 = splitClientscriptArgument(var0[1]);
                              var42 = 0;
                              var4 = var42 + 1;
                              var5 = Integer.parseInt(var3[var42]);
                              LinkedHashMap var41 = new LinkedHashMap(var5);

                              for(var7 = 0; var7 < var5; ++var7) {
                                 var45 = var3[var4++];
                                 var47 = Integer.parseInt(var3[var4++]);
                                 var41.put(var45, var47);
                              }

                              var2.getCallbacks().post(new UpdateCountGraphicsScript(var41));
                              return true;
                           } else if (var1 == 30011) {
                              handleGrandExchangeHistory(var2, var0.length > 1 ? var0[1] : "");
                              return true;
                           } else if (var1 == 30012) {
                              handleGrandExchangeItemDetail(var2, var0.length > 1 ? var0[1] : "");
                              return true;
                           } else if (var1 == 30013) {
                              var3 = splitClientscriptArgument(var0[1]);
                              var42 = 0;
                              var4 = var42 + 1;
                              var5 = Integer.parseInt(var3[var42]);
                              UpdateHiscoreTopListScript.TopListType var39 = UpdateHiscoreTopListScript.TopListType.fromWireId(Integer.parseInt(var3[var4++]));
                              var43 = var3[var4++];
                              var45 = var3[var4++];
                              var47 = Integer.parseInt(var3[var4++]);
                              var10 = Integer.parseInt(var3[var4++]);
                              var11 = Integer.parseInt(var3[var4++]);
                              var56 = Integer.parseInt(var3[var4++]);
                              ArrayList var60 = new ArrayList(var56);

                              for(var14 = 0; var14 < var56; ++var14) {
                                 String var65 = var3[var4++];
                                 String var16 = var3[var4++];
                                 int var17 = Integer.parseInt(var3[var4++]);
                                 int var18 = Integer.parseInt(var3[var4++]);
                                 var19 = Integer.parseInt(var3[var4++]);
                                 var20 = Integer.parseInt(var3[var4++]);
                                 var21 = Integer.parseInt(var3[var4++]);
                                 long var22 = Long.parseLong(var3[var4++]);
                                 var24 = Integer.parseInt(var3[var4++]);
                                 var25 = Integer.parseInt(var3[var4++]);
                                 var26 = Long.parseLong(var3[var4++]);
                                 var60.add(new UpdateHiscoreTopListScript.Entry(var65, var16, var17, var18, var19, var20, var21, var22, var24, var25, var26));
                              }

                              if (var4 != var3.length) {
                                 System.err.println("UPDATE_HISCORES_TOPLIST: parser consumed " + var4 + " of " + var3.length + " fields - wire format may have drifted from this client's spec");
                              }

                              var2.getCallbacks().post(new UpdateHiscoreTopListScript(var5, var39, var43, var45, var47, var10, var11, var60));
                              return true;
                           } else if (var1 == 30014) {
                              handleGrandExchangeMarketError(var2, var0.length > 1 ? var0[1] : "");
                              return true;
                           } else if (var1 == 30015) {
                              var2.getCallbacks().post(new SoftHideNpcsScript(splitClientscriptArgumentIntoInt(var0[1])));
                              return true;
                           } else if (var1 == 30027) {
                              int[] var35 = splitClientscriptArgumentIntoInt(var0[1]);
                              GhostRender.setNpcIds(var35);
                              var2.getCallbacks().post(new GhostNpcsScript(var35));
                              return true;
                           } else if (var1 == 30016) {
                              var3 = splitClientscriptArgument(var0[1]);
                              var4 = var3.length > 0 && !var3[0].isEmpty() ? Integer.parseInt(var3[0]) : 0;
                              var2.getCallbacks().post(new CobaltListResetScript(var4));
                              return true;
                           } else if (var1 == 30017) {
                              var2.getCallbacks().post(new CobaltListChunkScript(parseCobaltChunk(var0[1])));
                              return true;
                           } else {
                              boolean var37;
                              if (var1 == 30018) {
                                 var3 = splitClientscriptArgument(var0[1]);
                                 var37 = var3.length > 0 && "1".equals(var3[0]);
                                 var34 = var3.length > 1 ? var3[1] : "";
                                 var36 = var3.length > 2 ? Integer.parseInt(var3[2]) : 0;
                                 var7 = var3.length > 3 ? Integer.parseInt(var3[3]) : 0;
                                 var2.getCallbacks().post(new CobaltSessionScript(var37, var34, var36, var7));
                                 return true;
                              } else if (var1 == 30019) {
                                 var3 = splitClientscriptArgument(var0[1]);
                                 var2.getCallbacks().post(new CobaltCameraScript(Integer.parseInt(var3[0]), Integer.parseInt(var3[1]), Integer.parseInt(var3[2])));
                                 return true;
                              } else if (var1 == 30020) {
                                 var3 = splitClientscriptArgument(var0[1]);
                                 var2.getCallbacks().post(new CobaltClickScript(Integer.parseInt(var3[0]), Integer.parseInt(var3[1]), Integer.parseInt(var3[2]), Integer.parseInt(var3[3]), Integer.parseInt(var3[4]), var3.length > 5 ? Integer.parseInt(var3[5]) : 0));
                                 return true;
                              } else if (var1 == 30021) {
                                 var3 = splitClientscriptArgument(var0[1]);
                                 var2.getCallbacks().post(new CobaltReportingScript(var3.length > 0 && "1".equals(var3[0])));
                                 return true;
                              } else if (var1 == 30022) {
                                 var3 = splitClientscriptArgument(var0[1]);
                                 var2.getCallbacks().post(new CobaltScrollScript(Integer.parseInt(var3[0]), Integer.parseInt(var3[1]), Integer.parseInt(var3[2])));
                                 return true;
                              } else if (var1 == 30023) {
                                 var3 = splitClientscriptArgument(var0[1]);
                                 var2.getCallbacks().post(new CobaltResolutionScript(Integer.parseInt(var3[0]), Integer.parseInt(var3[1])));
                                 return true;
                              } else if (var1 == 30024) {
                                 var3 = splitClientscriptArgument(var0[1]);
                                 var37 = var3.length > 0 && "1".equals(var3[0]);
                                 if (var37 && var3.length >= 8) {
                                    var5 = Integer.parseInt(var3[1]);
                                    var36 = Integer.parseInt(var3[2]);
                                    var7 = Integer.parseInt(var3[3]);
                                    var44 = Integer.parseInt(var3[4]);
                                    var47 = Integer.parseInt(var3[5]);
                                    var10 = Integer.parseInt(var3[6]);
                                    var11 = Integer.parseInt(var3[7]);
                                    if (var11 >= 0 && var11 <= 256) {
                                       String[] var12 = new String[var11];
                                       String[] var13 = new String[var11];
                                       var14 = 8;

                                       for(var15 = 0; var15 < var11; ++var15) {
                                          var12[var15] = var14 < var3.length ? var3[var14++] : "";
                                          var13[var15] = var14 < var3.length ? var3[var14++] : "";
                                       }

                                       var2.getCallbacks().post(new CobaltMenuScript(true, var5, var36, var7, var44, var47, var10, var12, var13));
                                       return true;
                                    } else {
                                       return true;
                                    }
                                 } else {
                                    var2.getCallbacks().post(new CobaltMenuScript(false, 0, 0, 0, 0, 0, 0, new String[0], new String[0]));
                                    return true;
                                 }
                              } else if (var1 == 30025) {
                                 var3 = splitClientscriptArgument(var0[1]);
                                 var2.getCallbacks().post(new CobaltTabScript(Integer.parseInt(var3[0])));
                                 return true;
                              } else if (var1 == 30026) {
                                 var3 = splitClientscriptArgument(var0[1]);
                                 if (var3.length < 7) {
                                    System.err.println("UPDATE_DISCORD_PRESENCE: malformed payload with " + var3.length + " fields");
                                    return true;
                                 } else {
                                    try {
                                       var4 = 0;
                                       var5 = Integer.parseInt(var3[var4++]);
                                       if (var5 < 1 || var5 > 1) {
                                          return true;
                                       }

                                       var6 = var3[var4++];
                                       var7 = Integer.parseInt(var3[var4++]);
                                       boolean var8 = Integer.parseInt(var3[var4++]) == 1;
                                       var9 = var3[var4++];
                                       var10 = Integer.parseInt(var3[var4++]);
                                       var11 = Integer.parseInt(var3[var4++]);
                                       var2.getCallbacks().post(new UpdateDiscordPresenceScript(var6, var7, var8, var9, var10, var11));
                                    } catch (NumberFormatException var32) {
                                       System.err.println("UPDATE_DISCORD_PRESENCE: malformed numeric field — " + var32.getMessage());
                                    }

                                    return true;
                                 }
                              } else {
                                 return false;
                              }
                           }
                        }
                     }
                  }
               }
            }
         }
      } catch (Throwable var33) {
         var33.printStackTrace();
         return true;
      }
   }

   private static void handleGrandExchangeHistory(Client var0, Object var1) {
      int var2 = -1;

      try {
         String[] var3 = splitClientscriptArgument(var1);
         if (var3.length < 6) {
            postGrandExchangeMarketError(var0, var2, 1, "INVALID_REQUEST", "Malformed Grand Exchange history response.");
            return;
         }

         int var4 = 0;
         var2 = Integer.parseInt(var3[var4++]);
         int var5 = Integer.parseInt(var3[var4++]);
         int var6 = Integer.parseInt(var3[var4++]);
         int var7 = Integer.parseInt(var3[var4++]);
         boolean var8 = Integer.parseInt(var3[var4++]) == 1;
         int var9 = Integer.parseInt(var3[var4++]);
         int var10 = 6 + var9 * 4;
         if (var9 < 0 || var3.length != var10) {
            postGrandExchangeMarketError(var0, var2, 1, "INVALID_REQUEST", "Malformed Grand Exchange history response.");
            return;
         }

         ArrayList var11 = new ArrayList(var9);

         for(int var12 = 0; var12 < var9; ++var12) {
            int var13 = Integer.parseInt(var3[var4++]);
            int var14 = Integer.parseInt(var3[var4++]);
            int var15 = Integer.parseInt(var3[var4++]);
            long var16 = Long.parseLong(var3[var4++]);
            var11.add(new UpdateGrandExchangeHistoryScript.Transaction(var13, var14, var15, var16));
         }

         var0.getCallbacks().post(new UpdateGrandExchangeHistoryScript(var2, var5, var6, var7, var8, var11));
      } catch (RuntimeException var18) {
         postGrandExchangeMarketError(var0, var2, 1, "INVALID_REQUEST", "Malformed Grand Exchange history response.");
      }

   }

   private static void handleGrandExchangeItemDetail(Client var0, Object var1) {
      int var2 = -1;

      try {
         String[] var3 = splitClientscriptArgument(var1);
         if (var3.length < 13) {
            postGrandExchangeMarketError(var0, var2, 2, "INVALID_REQUEST", "Malformed Grand Exchange item detail response.");
            return;
         }

         int var4 = 0;
         var2 = Integer.parseInt(var3[var4++]);
         int var5 = Integer.parseInt(var3[var4++]);
         int var6 = Integer.parseInt(var3[var4++]);
         long var7 = Long.parseLong(var3[var4++]);
         long var9 = Long.parseLong(var3[var4++]);
         long var11 = Long.parseLong(var3[var4++]);
         long var13 = Long.parseLong(var3[var4++]);
         long var15 = Long.parseLong(var3[var4++]);
         long var17 = Long.parseLong(var3[var4++]);
         long var19 = Long.parseLong(var3[var4++]);
         long var21 = Long.parseLong(var3[var4++]);
         long var23 = Long.parseLong(var3[var4++]);
         int var25 = Integer.parseInt(var3[var4++]);
         int var26 = 13 + var25 * 6;
         int var27 = 13 + var25 * 7;
         boolean var28 = var3.length == var27;
         if (var25 < 0 || var3.length != var26 && !var28) {
            postGrandExchangeMarketError(var0, var2, 2, "INVALID_REQUEST", "Malformed Grand Exchange item detail response.");
            return;
         }

         ArrayList var29 = new ArrayList(var25);

         for(int var30 = 0; var30 < var25; ++var30) {
            if (var28) {
               ++var4;
            }

            String var31 = var3[var4++];
            int var32 = Integer.parseInt(var3[var4++]);
            int var33 = Integer.parseInt(var3[var4++]);
            String var34 = var3[var4++];
            long var35 = Long.parseLong(var3[var4++]);
            long var37 = Long.parseLong(var3[var4++]);
            var29.add(new UpdateGrandExchangeItemDetailScript.Order(var31, var32, var33, var34, var35, var37));
         }

         var0.getCallbacks().post(new UpdateGrandExchangeItemDetailScript(var2, var5, var6, var7, var9, var11, var13, var15, var17, var19, var21, var23, var29));
      } catch (RuntimeException var39) {
         postGrandExchangeMarketError(var0, var2, 2, "INVALID_REQUEST", "Malformed Grand Exchange item detail response.");
      }

   }

   private static void handleGrandExchangeMarketError(Client var0, Object var1) {
      try {
         String[] var2 = splitClientscriptArgument(var1);
         if (var2.length != 4) {
            postGrandExchangeMarketError(var0, -1, 0, "INVALID_REQUEST", "Malformed Grand Exchange error response.");
            return;
         }

         int var3 = 0;
         int var4 = Integer.parseInt(var2[var3++]);
         int var5 = Integer.parseInt(var2[var3++]);
         String var6 = var2[var3++];
         String var7 = var2[var3++];
         var0.getCallbacks().post(new UpdateGrandExchangeMarketErrorScript(var4, var5, var6, var7));
      } catch (RuntimeException var8) {
         postGrandExchangeMarketError(var0, -1, 0, "INVALID_REQUEST", "Malformed Grand Exchange error response.");
      }

   }

   private static void postGrandExchangeMarketError(Client var0, int var1, int var2, String var3, String var4) {
      var0.getCallbacks().post(new UpdateGrandExchangeMarketErrorScript(var1, var2, var3, var4));
   }

   private static List<CobaltPlayerRow> parseCobaltChunk(Object var0) {
      String[] var1 = splitClientscriptArgument(var0);
      ArrayList var2 = new ArrayList();
      if (var1.length == 0) {
         return var2;
      } else {
         int var3 = 0;
         int var4 = Integer.parseInt(var1[var3++]);
         if (var4 >= 0 && var4 <= 4096) {
            for(int var5 = 0; var5 < var4; ++var5) {
               String var6 = var1[var3++];
               int var7 = Integer.parseInt(var1[var3++]);
               int var8 = Integer.parseInt(var1[var3++]);
               int var9 = Integer.parseInt(var1[var3++]);
               int var10 = Integer.parseInt(var1[var3++]);
               String var11 = var1[var3++];
               int var12 = Integer.parseInt(var1[var3++]);
               int var13 = Integer.parseInt(var1[var3++]);
               ArrayList var14 = new ArrayList(Math.max(0, var13));

               for(int var15 = 0; var15 < var13; ++var15) {
                  var14.add(var1[var3++]);
               }

               var2.add(new CobaltPlayerRow(var6, var7, var8, var9, var10, var11, var12, var14));
            }

            return var2;
         } else {
            throw new IllegalArgumentException("Cobalt chunk count out of range: " + var4);
         }
      }
   }

   private static int[] splitClientscriptArgumentIntoInt(Object var0) {
      String var1 = var0.toString();
      if (var1.isEmpty()) {
         return new int[0];
      } else {
         String[] var2 = var1.split("\\|");
         int[] var3 = new int[var2.length];

         for(int var4 = 0; var4 < var2.length; ++var4) {
            var3[var4] = Integer.parseInt(var2[var4]);
         }

         return var3;
      }
   }

   private static String[] splitClientscriptArgument(Object var0) {
      String var1 = var0.toString();
      return var1.isEmpty() ? new String[0] : var1.split("\\|", -1);
   }
}
