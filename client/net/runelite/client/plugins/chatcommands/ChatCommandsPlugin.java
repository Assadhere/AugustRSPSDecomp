package net.runelite.client.plugins.chatcommands;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.MoreObjects;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.google.inject.Provides;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ScheduledExecutorService;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import javax.inject.Inject;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.EnumComposition;
import net.runelite.api.Experience;
import net.runelite.api.GameState;
import net.runelite.api.IconID;
import net.runelite.api.IndexedSprite;
import net.runelite.api.ItemComposition;
import net.runelite.api.MessageNode;
import net.runelite.api.Player;
import net.runelite.api.WorldType;
import net.runelite.api.events.ChatMessage;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.ScriptPostFired;
import net.runelite.api.events.VarbitChanged;
import net.runelite.api.events.WidgetLoaded;
import net.runelite.api.widgets.Widget;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.chat.ChatClient;
import net.runelite.client.chat.ChatColorType;
import net.runelite.client.chat.ChatCommandManager;
import net.runelite.client.chat.ChatMessageBuilder;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.config.RuneLiteConfig;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ChatInput;
import net.runelite.client.game.ItemManager;
import net.runelite.client.hiscore.HiscoreClient;
import net.runelite.client.hiscore.HiscoreEndpoint;
import net.runelite.client.hiscore.HiscoreResult;
import net.runelite.client.hiscore.HiscoreSkill;
import net.runelite.client.hiscore.Skill;
import net.runelite.client.input.KeyManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.util.AsyncBufferedImage;
import net.runelite.client.util.ImageUtil;
import net.runelite.client.util.QuantityFormatter;
import net.runelite.client.util.Text;
import net.runelite.http.api.chat.Duels;
import net.runelite.http.api.item.ItemPrice;
import org.apache.commons.text.WordUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@PluginDescriptor(
   name = "Chat Commands",
   description = "Enable chat commands",
   tags = {"grand", "exchange", "level", "prices"}
)
public class ChatCommandsPlugin extends Plugin {
   private static final Logger log = LoggerFactory.getLogger(ChatCommandsPlugin.class);
   private static final Pattern KILLCOUNT_PATTERN = Pattern.compile("Your (?<pre>completion count for |subdued |completed )?(?:<col=[0-9a-f]{6}>)?(?<boss>.+?)(?:</col>)? (?<post>(?:(?:kill|harvest|lap|completion|success) )?(?:count )?)is: ?<col=[0-9a-f]{6}>(?<kc>[0-9,]+)</col>");
   private static final String TEAM_SIZES = "(?<teamsize>\\d+(?:\\+|-\\d+)? players?|Solo)";
   private static final Pattern RAIDS_PB_PATTERN = Pattern.compile("<col=ef20ff>Congratulations - your raid is complete!</col><br>Team size: <col=ff0000>(?<teamsize>\\d+(?:\\+|-\\d+)? players?|Solo)</col> Duration:</col> <col=ff0000>(?<pb>[0-9:]+(?:\\.[0-9]+)?)</col> \\(new personal best\\)</col>");
   private static final Pattern RAIDS_DURATION_PATTERN = Pattern.compile("<col=ef20ff>Congratulations - your raid is complete!</col><br>Team size: <col=ff0000>(?<teamsize>\\d+(?:\\+|-\\d+)? players?|Solo)</col> Duration:</col> <col=ff0000>[0-9:.]+</col> Personal best: </col><col=ff0000>(?<pb>[0-9:]+(?:\\.[0-9]+)?)</col>");
   private static final Pattern KILL_DURATION_PATTERN = Pattern.compile("(?i)(?:(?:Fight |Lap |Challenge |Corrupted challenge )?duration:|Subdued in|(?<!total )completion time:) <col=[0-9a-f]{6}>[0-9:.]+</col>\\. Personal best: (?:<col=ff0000>)?(?<pb>[0-9:]+(?:\\.[0-9]+)?)");
   private static final Pattern NEW_PB_PATTERN = Pattern.compile("(?i)(?:(?:Fight |Lap |Challenge |Corrupted challenge )?duration:|Subdued in|(?<!total )completion time:) <col=[0-9a-f]{6}>(?<pb>[0-9:]+(?:\\.[0-9]+)?)</col> \\(new personal best\\)");
   private static final Pattern DUEL_ARENA_WINS_PATTERN = Pattern.compile("You (were defeated|won)! You have(?: now)? won ([\\d,]+|one) duels?");
   private static final Pattern DUEL_ARENA_LOSSES_PATTERN = Pattern.compile("You have(?: now)? lost ([\\d,]+|one) duels?");
   private static final Pattern ADVENTURE_LOG_TITLE_PATTERN = Pattern.compile("The Exploits of (.+)");
   private static final Pattern ADVENTURE_LOG_PB_PATTERN = Pattern.compile("Fastest (?:kill|run|Room time)(?: - \\(Team size: \\(?(?<teamsize>\\d+(?:\\+|-\\d+)? players?|Solo)\\)\\)?)?: (?<time>[0-9:]+(?:\\.[0-9]+)?)");
   private static final Pattern HS_PB_PATTERN = Pattern.compile("Floor (?<floor>\\d) time: <col=ff0000>(?<floortime>[0-9:]+(?:\\.[0-9]+)?)</col>(?: \\(new personal best\\)|. Personal best: (?<floorpb>[0-9:]+(?:\\.[0-9]+)?))(?:<br>Overall time: <col=ff0000>(?<otime>[0-9:]+(?:\\.[0-9]+)?)</col>(?: \\(new personal best\\)|. Personal best: (?<opb>[0-9:]+(?:\\.[0-9]+)?)))?");
   private static final Pattern HS_KC_FLOOR_PATTERN = Pattern.compile("You have completed Floor (\\d) of the Hallowed Sepulchre! Total completions: <col=ff0000>([0-9,]+)</col>\\.");
   private static final Pattern HS_KC_GHC_PATTERN = Pattern.compile("You have opened the Grand Hallowed Coffin <col=ff0000>([0-9,]+)</col> times?!");
   private static final Pattern COLLECTION_LOG_ITEM_PATTERN = Pattern.compile("New item added to your collection log: (.*)");
   private static final Pattern GUARDIANS_OF_THE_RIFT_PATTERN = Pattern.compile("Amount of Rifts you have closed: <col=ff0000>([0-9,]+)</col>\\.", 2);
   private static final Pattern HUNTER_RUMOUR_KC_PATTERN = Pattern.compile("You have completed <col=[0-9a-f]{6}>([0-9,]+)</col> rumours? for the Hunter Guild\\.");
   private static final Pattern BIRD_EGG_OFFERING_PATTERN = Pattern.compile("You have made <col=ff0000>(?<kc>[\\d,]+|one)</col> offerings?\\.");
   private static final Pattern CHEST_OPENING_PATTERN = Pattern.compile("You have (?<never>never )?opened (the )?(?<chest>crystal chest|Larran's big chest|Larran's small chest|Brimstone chest)( (?<kc>[\\d,]+ times|once))?\\.");
   private static final String TOTAL_LEVEL_COMMAND_STRING = "!total";
   private static final String PRICE_COMMAND_STRING = "!price";
   private static final String LEVEL_COMMAND_STRING = "!lvl";
   private static final String BOUNTY_HUNTER_HUNTER_COMMAND = "!bh";
   private static final String BOUNTY_HUNTER_ROGUE_COMMAND = "!bhrogue";
   private static final String CLUES_COMMAND_STRING = "!clues";
   private static final String LAST_MAN_STANDING_COMMAND = "!lms";
   private static final String KILLCOUNT_COMMAND_STRING = "!kc";
   private static final String CMB_COMMAND_STRING = "!cmb";
   private static final String QP_COMMAND_STRING = "!qp";
   private static final String PB_COMMAND = "!pb";
   private static final String GC_COMMAND_STRING = "!gc";
   private static final String DUEL_ARENA_COMMAND = "!duels";
   private static final String LEAGUE_POINTS_COMMAND = "!lp";
   private static final String SOUL_WARS_ZEAL_COMMAND = "!sw";
   private static final String PET_LIST_COMMAND = "!pets";
   private static final String CA_COMMAND = "!ca";
   private static final String CLOG_COMMAND = "!clog";
   @VisibleForTesting
   static final int ADV_LOG_EXPLOITS_TEXT_INDEX = 1;
   static final int COL_LOG_ENTRY_HEADER_TITLE_INDEX = 0;
   private static final Map<String, String> KILLCOUNT_RENAMES = ImmutableMap.of("Barrows chest", "Barrows Chests");
   private boolean bossLogLoaded;
   private boolean advLogLoaded;
   private boolean scrollInterfaceLoaded;
   private String pohOwner;
   private HiscoreEndpoint hiscoreEndpoint;
   private String lastBossKill;
   private int lastBossTime = -1;
   private double lastPb = -1.0;
   private String lastTeamSize;
   private int petsIconIdx = -1;
   private int[] pets;
   @Inject
   private Client client;
   @Inject
   private ClientThread clientThread;
   @Inject
   private ChatCommandsConfig config;
   @Inject
   private ConfigManager configManager;
   @Inject
   private ItemManager itemManager;
   @Inject
   private ChatCommandManager chatCommandManager;
   @Inject
   private ScheduledExecutorService executor;
   @Inject
   private KeyManager keyManager;
   @Inject
   private ChatKeyboardListener chatKeyboardListener;
   @Inject
   private HiscoreClient hiscoreClient;
   @Inject
   private ChatClient chatClient;
   @Inject
   private RuneLiteConfig runeLiteConfig;
   @Inject
   private Gson gson;

   public void startUp() {
      this.keyManager.registerKeyListener(this.chatKeyboardListener);
      this.chatCommandManager.registerCommandAsync("!total", this::playerSkillLookup);
      this.chatCommandManager.registerCommandAsync("!cmb", this::combatLevelLookup);
      this.chatCommandManager.registerCommand("!price", this::itemPriceLookup);
      this.chatCommandManager.registerCommandAsync("!lvl", this::playerSkillLookup);
      this.chatCommandManager.registerCommandAsync("!bh", this::bountyHunterHunterLookup);
      this.chatCommandManager.registerCommandAsync("!bhrogue", this::bountyHunterRogueLookup);
      this.chatCommandManager.registerCommandAsync("!clues", this::clueLookup);
      this.chatCommandManager.registerCommandAsync("!lms", this::lastManStandingLookup);
      this.chatCommandManager.registerCommandAsync("!lp", this::leaguePointsLookup);
      this.chatCommandManager.registerCommandAsync("!kc", this::killCountLookup, this::killCountSubmit);
      this.chatCommandManager.registerCommandAsync("!qp", this::questPointsLookup, this::questPointsSubmit);
      this.chatCommandManager.registerCommandAsync("!pb", this::personalBestLookup, this::personalBestSubmit);
      this.chatCommandManager.registerCommandAsync("!gc", this::gambleCountLookup, this::gambleCountSubmit);
      this.chatCommandManager.registerCommandAsync("!duels", this::duelArenaLookup, this::duelArenaSubmit);
      this.chatCommandManager.registerCommandAsync("!sw", this::soulWarsZealLookup);
      this.chatCommandManager.registerCommandAsync("!pets", this::petListLookup, this::petListSubmit);
      this.chatCommandManager.registerCommandAsync("!ca", this::caLookup, this::caSubmit);
      this.chatCommandManager.registerCommandAsync("!clog", this::clogLookup, this::clogSubmit);
      this.clientThread.invoke(() -> {
         if (this.client.getGameState().getState() >= GameState.LOGIN_SCREEN.getState() && this.petsIconIdx == -1) {
            this.loadPets();
         }

      });
   }

   public void shutDown() {
      this.pets = null;
      this.lastBossKill = null;
      this.lastBossTime = -1;
      this.keyManager.unregisterKeyListener(this.chatKeyboardListener);
      this.chatCommandManager.unregisterCommand("!total");
      this.chatCommandManager.unregisterCommand("!cmb");
      this.chatCommandManager.unregisterCommand("!price");
      this.chatCommandManager.unregisterCommand("!lvl");
      this.chatCommandManager.unregisterCommand("!bh");
      this.chatCommandManager.unregisterCommand("!bhrogue");
      this.chatCommandManager.unregisterCommand("!clues");
      this.chatCommandManager.unregisterCommand("!lms");
      this.chatCommandManager.unregisterCommand("!lp");
      this.chatCommandManager.unregisterCommand("!kc");
      this.chatCommandManager.unregisterCommand("!qp");
      this.chatCommandManager.unregisterCommand("!pb");
      this.chatCommandManager.unregisterCommand("!gc");
      this.chatCommandManager.unregisterCommand("!duels");
      this.chatCommandManager.unregisterCommand("!sw");
      this.chatCommandManager.unregisterCommand("!pets");
      this.chatCommandManager.unregisterCommand("!ca");
      this.chatCommandManager.unregisterCommand("!clog");
   }

   @Provides
   ChatCommandsConfig provideConfig(ConfigManager configManager) {
      return (ChatCommandsConfig)configManager.getConfig(ChatCommandsConfig.class);
   }

   private void setKc(String boss, int killcount) {
      this.configManager.setRSProfileConfiguration("killcount", boss.toLowerCase(), killcount);
   }

   private void unsetKc(String boss) {
      this.configManager.unsetRSProfileConfiguration("killcount", boss.toLowerCase());
   }

   private int getKc(String boss) {
      Integer killCount = (Integer)this.configManager.getRSProfileConfiguration("killcount", boss.toLowerCase(), Integer.TYPE);
      return killCount == null ? 0 : killCount;
   }

   private void setPb(String boss, double seconds) {
      this.configManager.setRSProfileConfiguration("personalbest", boss.toLowerCase(), seconds);
   }

   private void unsetPb(String boss) {
      this.configManager.unsetRSProfileConfiguration("personalbest", boss.toLowerCase());
   }

   private double getPb(String boss) {
      Double personalBest = (Double)this.configManager.getRSProfileConfiguration("personalbest", boss.toLowerCase(), Double.TYPE);
      return personalBest == null ? 0.0 : personalBest;
   }

   private void loadPets() {
      assert this.petsIconIdx == -1;

      EnumComposition petsEnum = this.client.getEnum(2158);
      this.pets = new int[petsEnum.size()];

      for(int i = 0; i < petsEnum.size(); ++i) {
         this.pets[i] = petsEnum.getIntValue(i);
      }

      IndexedSprite[] modIcons = this.client.getModIcons();

      assert modIcons != null;

      IndexedSprite[] newModIcons = (IndexedSprite[])Arrays.copyOf(modIcons, modIcons.length + this.pets.length);
      this.petsIconIdx = modIcons.length;
      this.client.setModIcons(newModIcons);

      for(int i = 0; i < this.pets.length; ++i) {
         int petId = this.pets[i];
         AsyncBufferedImage abi = this.itemManager.getImage(petId);
         int idx = this.petsIconIdx + i;
         abi.onLoaded(() -> {
            BufferedImage image = ImageUtil.resizeImage(abi, 18, 16);
            IndexedSprite sprite = ImageUtil.getImageIndexedSprite(image, this.client);
            this.client.getModIcons()[idx] = sprite;
         });
      }

   }

   private void setPetList(List<Integer> petList) {
      if (petList != null) {
         this.configManager.setRSProfileConfiguration("chatcommands", "pets2", this.gson.toJson(petList));
         this.configManager.unsetRSProfileConfiguration("chatcommands", "pets");
      }
   }

   private List<Pet> getPetListOld() {
      String petListJson = (String)this.configManager.getRSProfileConfiguration("chatcommands", "pets", String.class);

      List petList;
      try {
         petList = (List)this.gson.fromJson(petListJson, (new TypeToken<List<Pet>>() {
         }).getType());
      } catch (JsonSyntaxException var4) {
         return Collections.emptyList();
      }

      return petList != null ? petList : Collections.emptyList();
   }

   private List<Integer> getPetList() {
      List<Pet> old = this.getPetListOld();
      if (!old.isEmpty()) {
         List<Integer> l = (List)old.stream().map(Pet::getIconID).collect(Collectors.toList());
         this.setPetList(l);
         return l;
      } else {
         String petListJson = (String)this.configManager.getRSProfileConfiguration("chatcommands", "pets2", String.class);

         List petList;
         try {
            petList = (List)this.gson.fromJson(petListJson, (new TypeToken<List<Integer>>() {
            }).getType());
         } catch (JsonSyntaxException var5) {
            return Collections.emptyList();
         }

         return petList != null ? petList : Collections.emptyList();
      }
   }

   @Subscribe
   public void onChatMessage(ChatMessage chatMessage) {
      if (chatMessage.getType() == ChatMessageType.TRADE || chatMessage.getType() == ChatMessageType.GAMEMESSAGE || chatMessage.getType() == ChatMessageType.SPAM || chatMessage.getType() == ChatMessageType.FRIENDSCHATNOTIFICATION) {
         String message = chatMessage.getMessage();
         Matcher matcher = KILLCOUNT_PATTERN.matcher(message);
         int kc;
         String floorpb;
         String kcString;
         String otime;
         String opb;
         if (matcher.find()) {
            kcString = matcher.group("boss");
            kc = Integer.parseInt(matcher.group("kc").replace(",", ""));
            floorpb = matcher.group("pre");
            otime = matcher.group("post");
            if (Strings.isNullOrEmpty(floorpb) && Strings.isNullOrEmpty(otime)) {
               this.unsetKc(kcString);
            } else {
               opb = ((String)KILLCOUNT_RENAMES.getOrDefault(kcString, kcString)).replace(":", "");
               if (kcString != opb) {
                  this.unsetKc(kcString);
                  this.unsetPb(kcString);
                  this.unsetKc(kcString.replace(":", "."));
                  this.unsetPb(kcString.replace(":", "."));
                  this.unsetKc("Theatre of Blood Story Mode");
                  this.unsetPb("Theatre of Blood Story Mode");
               }

               this.setKc(opb, kc);
               if (this.lastPb > -1.0) {
                  log.debug("Got out-of-order personal best for {}: {}", opb, this.lastPb);
                  int toaTeamSize;
                  if (opb.contains("Theatre of Blood")) {
                     toaTeamSize = this.tobTeamSize();
                     this.lastTeamSize = toaTeamSize == 1 ? "Solo" : "" + toaTeamSize + " players";
                  } else if (opb.contains("Tombs of Amascut")) {
                     toaTeamSize = this.toaTeamSize();
                     this.lastTeamSize = toaTeamSize == 1 ? "Solo" : "" + toaTeamSize + " players";
                  }

                  double pb = this.getPb(opb);
                  if (this.lastTeamSize == null || pb == 0.0 || this.lastPb < pb) {
                     log.debug("Setting overall pb (old: {})", pb);
                     this.setPb(opb, this.lastPb);
                  }

                  if (this.lastTeamSize != null) {
                     log.debug("Setting team size pb: {}", this.lastTeamSize);
                     this.setPb(opb + " " + this.lastTeamSize, this.lastPb);
                  }

                  this.lastPb = -1.0;
                  this.lastTeamSize = null;
               } else {
                  this.lastBossKill = opb;
                  this.lastBossTime = this.client.getTickCount();
               }

            }
         } else {
            matcher = DUEL_ARENA_WINS_PATTERN.matcher(message);
            int kc;
            if (matcher.find()) {
               kc = this.getKc("Duel Arena Wins");
               kc = matcher.group(2).equals("one") ? 1 : Integer.parseInt(matcher.group(2).replace(",", ""));
               floorpb = matcher.group(1);
               int winningStreak = this.getKc("Duel Arena Win Streak");
               int losingStreak = this.getKc("Duel Arena Lose Streak");
               if (floorpb.equals("won") && kc > kc) {
                  losingStreak = 0;
                  ++winningStreak;
               } else if (floorpb.equals("were defeated")) {
                  ++losingStreak;
                  winningStreak = 0;
               } else {
                  log.warn("unrecognized duel streak chat message: {}", message);
               }

               this.setKc("Duel Arena Wins", kc);
               this.setKc("Duel Arena Win Streak", winningStreak);
               this.setKc("Duel Arena Lose Streak", losingStreak);
            }

            matcher = DUEL_ARENA_LOSSES_PATTERN.matcher(message);
            if (matcher.find()) {
               kc = matcher.group(1).equals("one") ? 1 : Integer.parseInt(matcher.group(1).replace(",", ""));
               this.setKc("Duel Arena Losses", kc);
            }

            matcher = KILL_DURATION_PATTERN.matcher(message);
            if (matcher.find()) {
               this.matchPb(matcher);
            }

            matcher = NEW_PB_PATTERN.matcher(message);
            if (matcher.find()) {
               this.matchPb(matcher);
            }

            matcher = RAIDS_PB_PATTERN.matcher(message);
            if (matcher.find()) {
               this.matchPb(matcher);
            }

            matcher = RAIDS_DURATION_PATTERN.matcher(message);
            if (matcher.find()) {
               this.matchPb(matcher);
            }

            matcher = HS_PB_PATTERN.matcher(message);
            String chest;
            if (matcher.find()) {
               kc = Integer.parseInt(matcher.group("floor"));
               chest = matcher.group("floortime");
               floorpb = matcher.group("floorpb");
               otime = matcher.group("otime");
               opb = matcher.group("opb");
               String pb = (String)MoreObjects.firstNonNull(floorpb, chest);
               this.setPb("Hallowed Sepulchre Floor " + kc, timeStringToSeconds(pb));
               if (otime != null) {
                  pb = (String)MoreObjects.firstNonNull(opb, otime);
                  this.setPb("Hallowed Sepulchre", timeStringToSeconds(pb));
               }
            }

            matcher = HS_KC_FLOOR_PATTERN.matcher(message);
            if (matcher.find()) {
               kc = Integer.parseInt(matcher.group(1));
               kc = Integer.parseInt(matcher.group(2).replace(",", ""));
               this.setKc("Hallowed Sepulchre Floor " + kc, kc);
            }

            matcher = HS_KC_GHC_PATTERN.matcher(message);
            if (matcher.find()) {
               kc = Integer.parseInt(matcher.group(1).replace(",", ""));
               this.setKc("Hallowed Sepulchre", kc);
            }

            matcher = HUNTER_RUMOUR_KC_PATTERN.matcher(message);
            if (matcher.find()) {
               kc = Integer.parseInt(matcher.group(1).replace(",", ""));
               this.setKc("Hunter Rumours", kc);
            }

            if (this.lastBossKill != null && this.lastBossTime != this.client.getTickCount()) {
               this.lastBossKill = null;
               this.lastBossTime = -1;
            }

            matcher = COLLECTION_LOG_ITEM_PATTERN.matcher(message);
            if (matcher.find()) {
               kcString = matcher.group(1);
               kc = this.findPet(kcString);
               if (kc != -1) {
                  List<Integer> petList = new ArrayList(this.getPetList());
                  if (!petList.contains(kc)) {
                     log.debug("New pet added: {}/{}", kcString, kc);
                     petList.add(kc);
                     this.setPetList(petList);
                  }
               }
            }

            matcher = GUARDIANS_OF_THE_RIFT_PATTERN.matcher(message);
            if (matcher.find()) {
               kc = Integer.parseInt(matcher.group(1).replace(",", ""));
               this.setKc("Guardians of the Rift", kc);
            }

            matcher = BIRD_EGG_OFFERING_PATTERN.matcher(message);
            if (matcher.find()) {
               kcString = matcher.group("kc");
               kc = kcString.equals("one") ? 1 : Integer.parseInt(kcString.replace(",", ""));
               this.setKc("Bird's egg offerings", kc);
            }

            matcher = CHEST_OPENING_PATTERN.matcher(message);
            if (matcher.find()) {
               if (matcher.group("never") != null) {
                  kc = 0;
               } else {
                  chest = matcher.group("kc");
                  kc = chest.equals("once") ? 1 : Integer.parseInt(chest.split(" ")[0].replace(",", ""));
               }

               chest = matcher.group("chest");
               this.setKc(chest, kc);
            }

         }
      }
   }

   @VisibleForTesting
   static double timeStringToSeconds(String timeString) {
      String[] s = timeString.split(":");
      if (s.length == 2) {
         return (double)(Integer.parseInt(s[0]) * 60) + Double.parseDouble(s[1]);
      } else {
         return s.length == 3 ? (double)(Integer.parseInt(s[0]) * 60 * 60 + Integer.parseInt(s[1]) * 60) + Double.parseDouble(s[2]) : Double.parseDouble(timeString);
      }
   }

   @VisibleForTesting
   static String secondsToTimeString(double seconds) {
      int hours = (int)(Math.floor(seconds) / 3600.0);
      int minutes = (int)(Math.floor(seconds / 60.0) % 60.0);
      seconds %= 60.0;
      String timeString = hours > 0 ? String.format("%d:%02d:", hours, minutes) : String.format("%d:", minutes);
      return timeString + (Math.floor(seconds) == seconds ? String.format("%02d", (int)seconds) : String.format("%05.2f", seconds));
   }

   private void matchPb(Matcher matcher) {
      double seconds = timeStringToSeconds(matcher.group("pb"));
      if (this.lastBossKill != null) {
         log.debug("Got personal best for {}: {}", this.lastBossKill, seconds);
         this.setPb(this.lastBossKill, seconds);
         this.lastPb = -1.0;
         this.lastTeamSize = null;
      } else {
         this.lastPb = seconds;

         try {
            this.lastTeamSize = matcher.group("teamsize");
         } catch (IllegalArgumentException var5) {
            this.lastTeamSize = null;
         }
      }

   }

   @Subscribe
   public void onGameTick(GameTick event) {
      if (this.client.getLocalPlayer() != null) {
         Widget parent;
         if (this.advLogLoaded) {
            this.advLogLoaded = false;
            parent = this.client.getWidget(12255232);
            if (parent != null) {
               Matcher advLogExploitsText = ADVENTURE_LOG_TITLE_PATTERN.matcher(parent.getChild(1).getText());
               if (advLogExploitsText.find()) {
                  this.pohOwner = advLogExploitsText.group(1);
               }
            }
         }

         if (this.bossLogLoaded && (this.pohOwner == null || this.pohOwner.equals(this.client.getLocalPlayer().getName()))) {
            this.bossLogLoaded = false;
            parent = this.client.getWidget(35979267);
            Widget bossMonster = this.client.getWidget(35979277);
            Widget bossKills = this.client.getWidget(35979278);
            if (parent == null || bossMonster == null || bossKills == null || !"Boss Kill Log".equals(parent.getText())) {
               return;
            }

            Widget[] bossChildren = bossMonster.getChildren();
            Widget[] killsChildren = bossKills.getChildren();

            for(int i = 0; i < bossChildren.length; ++i) {
               Widget boss = bossChildren[i];
               Widget kill = killsChildren[i];
               String bossName = boss.getText().replace(":", "");
               int kc = Integer.parseInt(kill.getText().replace(",", ""));
               if (kc != this.getKc(longBossName(bossName))) {
                  this.setKc(longBossName(bossName), kc);
               }
            }
         }

         if (this.scrollInterfaceLoaded) {
            this.scrollInterfaceLoaded = false;
            if (this.client.getLocalPlayer().getName().equals(this.pohOwner)) {
               parent = this.client.getWidget(48562179);
               Widget[] children = parent.getStaticChildren();
               String[] text = (String[])Arrays.stream(children).map(Widget::getText).map(Text::removeTags).toArray((x$0) -> {
                  return new String[x$0];
               });

               for(int i = 0; i < text.length; ++i) {
                  String boss = longBossName(text[i]);
                  ++i;

                  for(; i < text.length; ++i) {
                     String line = text[i];
                     if (line.isEmpty()) {
                        break;
                     }

                     Matcher matcher = ADVENTURE_LOG_PB_PATTERN.matcher(line);
                     if (matcher.find()) {
                        double s = timeStringToSeconds(matcher.group("time"));
                        String teamSize = matcher.group("teamsize");
                        if (teamSize != null) {
                           if (teamSize.equals("1 player")) {
                              teamSize = "Solo";
                           } else if (teamSize.endsWith("player")) {
                              teamSize = teamSize + "s";
                           }

                           log.debug("Found team-size adventure log PB for {} {}: {}", new Object[]{boss, teamSize, s});
                           this.setPb(boss + " " + teamSize, s);
                        } else {
                           log.debug("Found adventure log PB for {}: {}", boss, s);
                           this.setPb(boss, s);
                        }
                     }
                  }
               }
            }
         }

      }
   }

   @Subscribe
   public void onScriptPostFired(ScriptPostFired scriptPostFired) {
      if (scriptPostFired.getScriptId() == 2731) {
         if (this.pohOwner == null || this.pohOwner.equals(this.client.getLocalPlayer().getName())) {
            Widget collectionLogEntryHeader = this.client.getWidget(40697876);
            if (collectionLogEntryHeader != null && collectionLogEntryHeader.getChildren() != null) {
               Widget entryTitle = collectionLogEntryHeader.getChild(0);
               if (entryTitle.getText().equals("All Pets")) {
                  Widget collectionLogEntryItems = this.client.getWidget(40697893);
                  if (collectionLogEntryItems != null && collectionLogEntryItems.getChildren() != null) {
                     List<Integer> petList = new ArrayList();
                     Widget[] var6 = collectionLogEntryItems.getChildren();
                     int var7 = var6.length;

                     for(int var8 = 0; var8 < var7; ++var8) {
                        Widget child = var6[var8];
                        if (child.getOpacity() == 0) {
                           petList.add(child.getItemId());
                        }
                     }

                     this.setPetList(petList);
                     log.debug("Loaded {} pets", petList.size());
                  }
               }
            }
         }

      }
   }

   @Subscribe
   public void onWidgetLoaded(WidgetLoaded widget) {
      switch (widget.getGroupId()) {
         case 187:
            this.advLogLoaded = true;
            break;
         case 549:
            this.bossLogLoaded = true;
            break;
         case 741:
            this.scrollInterfaceLoaded = true;
      }

   }

   @Subscribe
   public void onGameStateChanged(GameStateChanged event) {
      switch (event.getGameState()) {
         case LOADING:
         case HOPPING:
            this.pohOwner = null;
            break;
         case STARTING:
            this.petsIconIdx = -1;
            this.pets = null;
            break;
         case LOGIN_SCREEN:
            if (this.petsIconIdx == -1) {
               this.loadPets();
            }
      }

   }

   @Subscribe
   public void onVarbitChanged(VarbitChanged event) {
      this.hiscoreEndpoint = this.getLocalHiscoreEndpointType();
      if (event.getVarpId() == 4824 && event.getValue() > 0) {
         this.setKc("Doom of Mokhaiotl", event.getValue());
      }

   }

   private boolean killCountSubmit(ChatInput chatInput, String value) {
      int idx = value.indexOf(32);
      String boss = longBossName(value.substring(idx + 1));
      int kc = this.getKc(boss);
      if (kc <= 0) {
         return false;
      } else {
         String playerName = this.client.getLocalPlayer().getName();
         this.executor.execute(() -> {
            try {
               this.chatClient.submitKc(playerName, boss, kc);
            } catch (Exception var9) {
               Exception ex = var9;
               log.warn("unable to submit killcount", ex);
            } finally {
               chatInput.resume();
            }

         });
         return true;
      }
   }

   @VisibleForTesting
   void killCountLookup(ChatMessage chatMessage, String message) {
      if (this.config.killcount()) {
         if (message.length() > "!kc".length()) {
            ChatMessageType type = chatMessage.getType();
            String search = message.substring("!kc".length() + 1);
            String player;
            if (type.equals(ChatMessageType.PRIVATECHATOUT)) {
               player = this.client.getLocalPlayer().getName();
            } else {
               player = Text.sanitize(chatMessage.getName());
            }

            search = longBossName(search);

            int kc;
            try {
               kc = this.chatClient.getKc(player, search);
            } catch (IOException var9) {
               IOException ex = var9;
               log.debug("unable to lookup killcount", ex);
               return;
            }

            String response = (new ChatMessageBuilder()).append(ChatColorType.HIGHLIGHT).append(search).append(ChatColorType.NORMAL).append(" kill count: ").append(ChatColorType.HIGHLIGHT).append(String.format("%,d", kc)).build();
            log.debug("Setting response {}", response);
            MessageNode messageNode = chatMessage.getMessageNode();
            messageNode.setRuneLiteFormatMessage(response);
            this.client.refreshChat();
         }
      }
   }

   private boolean duelArenaSubmit(ChatInput chatInput, String value) {
      int wins = this.getKc("Duel Arena Wins");
      int losses = this.getKc("Duel Arena Losses");
      int winningStreak = this.getKc("Duel Arena Win Streak");
      int losingStreak = this.getKc("Duel Arena Lose Streak");
      if (wins <= 0 && losses <= 0 && winningStreak <= 0 && losingStreak <= 0) {
         return false;
      } else {
         String playerName = this.client.getLocalPlayer().getName();
         this.executor.execute(() -> {
            try {
               this.chatClient.submitDuels(playerName, wins, losses, winningStreak, losingStreak);
            } catch (Exception var11) {
               Exception ex = var11;
               log.warn("unable to submit duels", ex);
            } finally {
               chatInput.resume();
            }

         });
         return true;
      }
   }

   private void duelArenaLookup(ChatMessage chatMessage, String message) {
      if (this.config.duels()) {
         ChatMessageType type = chatMessage.getType();
         String player;
         if (type == ChatMessageType.PRIVATECHATOUT) {
            player = this.client.getLocalPlayer().getName();
         } else {
            player = Text.sanitize(chatMessage.getName());
         }

         Duels duels;
         try {
            duels = this.chatClient.getDuels(player);
         } catch (IOException var12) {
            IOException ex = var12;
            log.debug("unable to lookup duels", ex);
            return;
         }

         int wins = duels.getWins();
         int losses = duels.getLosses();
         int winningStreak = duels.getWinningStreak();
         int losingStreak = duels.getLosingStreak();
         String response = (new ChatMessageBuilder()).append(ChatColorType.NORMAL).append("Duel Arena wins: ").append(ChatColorType.HIGHLIGHT).append(String.format("%,d", wins)).append(ChatColorType.NORMAL).append("   losses: ").append(ChatColorType.HIGHLIGHT).append(String.format("%,d", losses)).append(ChatColorType.NORMAL).append("   streak: ").append(ChatColorType.HIGHLIGHT).append(String.format("%,d", winningStreak != 0 ? winningStreak : -losingStreak)).build();
         log.debug("Setting response {}", response);
         MessageNode messageNode = chatMessage.getMessageNode();
         messageNode.setRuneLiteFormatMessage(response);
         this.client.refreshChat();
      }
   }

   private void questPointsLookup(ChatMessage chatMessage, String message) {
      if (this.config.qp()) {
         ChatMessageType type = chatMessage.getType();
         String player;
         if (type.equals(ChatMessageType.PRIVATECHATOUT)) {
            player = this.client.getLocalPlayer().getName();
         } else {
            player = Text.sanitize(chatMessage.getName());
         }

         int qp;
         try {
            qp = this.chatClient.getQp(player);
         } catch (IOException var8) {
            IOException ex = var8;
            log.debug("unable to lookup quest points", ex);
            return;
         }

         String response = (new ChatMessageBuilder()).append(ChatColorType.NORMAL).append("Quest points: ").append(ChatColorType.HIGHLIGHT).append(Integer.toString(qp)).build();
         log.debug("Setting response {}", response);
         MessageNode messageNode = chatMessage.getMessageNode();
         messageNode.setRuneLiteFormatMessage(response);
         this.client.refreshChat();
      }
   }

   private boolean questPointsSubmit(ChatInput chatInput, String value) {
      int qp = this.client.getVarpValue(101);
      String playerName = this.client.getLocalPlayer().getName();
      this.executor.execute(() -> {
         try {
            this.chatClient.submitQp(playerName, qp);
         } catch (Exception var8) {
            Exception ex = var8;
            log.warn("unable to submit quest points", ex);
         } finally {
            chatInput.resume();
         }

      });
      return true;
   }

   private void personalBestLookup(ChatMessage chatMessage, String message) {
      if (this.config.pb()) {
         if (message.length() > "!pb".length()) {
            ChatMessageType type = chatMessage.getType();
            String search = message.substring("!pb".length() + 1);
            String player;
            if (type.equals(ChatMessageType.PRIVATECHATOUT)) {
               player = this.client.getLocalPlayer().getName();
            } else {
               player = Text.sanitize(chatMessage.getName());
            }

            search = longBossName(search);

            double pb;
            try {
               pb = this.chatClient.getPb(player, search);
            } catch (IOException var10) {
               IOException ex = var10;
               log.debug("unable to lookup personal best", ex);
               return;
            }

            String response = (new ChatMessageBuilder()).append(ChatColorType.HIGHLIGHT).append(search).append(ChatColorType.NORMAL).append(" personal best: ").append(ChatColorType.HIGHLIGHT).append(secondsToTimeString(pb)).build();
            log.debug("Setting response {}", response);
            MessageNode messageNode = chatMessage.getMessageNode();
            messageNode.setRuneLiteFormatMessage(response);
            this.client.refreshChat();
         }
      }
   }

   private boolean personalBestSubmit(ChatInput chatInput, String value) {
      int idx = value.indexOf(32);
      String boss = longBossName(value.substring(idx + 1));
      double pb = this.getPb(boss);
      if (pb <= 0.0) {
         return false;
      } else {
         String playerName = this.client.getLocalPlayer().getName();
         this.executor.execute(() -> {
            try {
               this.chatClient.submitPb(playerName, boss, pb);
            } catch (Exception var10) {
               Exception ex = var10;
               log.warn("unable to submit personal best", ex);
            } finally {
               chatInput.resume();
            }

         });
         return true;
      }
   }

   private void gambleCountLookup(ChatMessage chatMessage, String message) {
      if (this.config.gc()) {
         ChatMessageType type = chatMessage.getType();
         String player;
         if (type == ChatMessageType.PRIVATECHATOUT) {
            player = this.client.getLocalPlayer().getName();
         } else {
            player = Text.sanitize(chatMessage.getName());
         }

         int gc;
         try {
            gc = this.chatClient.getGc(player);
         } catch (IOException var8) {
            IOException ex = var8;
            log.debug("unable to lookup gamble count", ex);
            return;
         }

         String response = (new ChatMessageBuilder()).append(ChatColorType.NORMAL).append("Barbarian Assault High-level gambles: ").append(ChatColorType.HIGHLIGHT).append(String.format("%,d", gc)).build();
         log.debug("Setting response {}", response);
         MessageNode messageNode = chatMessage.getMessageNode();
         messageNode.setRuneLiteFormatMessage(response);
         this.client.refreshChat();
      }
   }

   private boolean gambleCountSubmit(ChatInput chatInput, String value) {
      int gc = this.client.getVarbitValue(4768);
      String playerName = this.client.getLocalPlayer().getName();
      this.executor.execute(() -> {
         try {
            this.chatClient.submitGc(playerName, gc);
         } catch (Exception var8) {
            Exception ex = var8;
            log.warn("unable to submit gamble count", ex);
         } finally {
            chatInput.resume();
         }

      });
      return true;
   }

   private void petListLookup(ChatMessage chatMessage, String message) {
      if (this.config.pets()) {
         ChatMessageType type = chatMessage.getType();
         String player;
         if (type.equals(ChatMessageType.PRIVATECHATOUT)) {
            player = this.client.getLocalPlayer().getName();
         } else {
            player = Text.sanitize(chatMessage.getName());
         }

         Set playerPetList;
         String response;
         MessageNode messageNode;
         try {
            playerPetList = this.chatClient.getPetList(player);
         } catch (IOException var9) {
            IOException ex = var9;
            log.debug("unable to lookup pet list", ex);
            if (player.equals(this.client.getLocalPlayer().getName())) {
               response = "Open the 'All Pets' tab in the Collection Log to update your pet list";
               log.debug("Setting response {}", response);
               messageNode = chatMessage.getMessageNode();
               messageNode.setValue(response);
               this.client.refreshChat();
            }

            return;
         }

         ChatMessageBuilder responseBuilder = (new ChatMessageBuilder()).append(ChatColorType.NORMAL).append("Pets: ").append("(" + playerPetList.size() + ")");

         for(int petIdx = 0; petIdx < this.pets.length; ++petIdx) {
            int petId = this.pets[petIdx];
            if (playerPetList.contains(petId)) {
               responseBuilder.append(" ").img(this.petsIconIdx + petIdx);
            }
         }

         response = responseBuilder.build();
         log.debug("Setting response {}", response);
         messageNode = chatMessage.getMessageNode();
         messageNode.setRuneLiteFormatMessage(response);
         this.client.refreshChat();
      }
   }

   private boolean petListSubmit(ChatInput chatInput, String value) {
      String playerName = this.client.getLocalPlayer().getName();
      this.executor.execute(() -> {
         try {
            List<Integer> petList = this.getPetList();
            if (!petList.isEmpty()) {
               this.chatClient.submitPetList(playerName, petList);
            }
         } catch (Exception var7) {
            Exception ex = var7;
            log.warn("unable to submit pet list", ex);
         } finally {
            chatInput.resume();
         }

      });
      return true;
   }

   private void caLookup(ChatMessage chatMessage, String message) {
      if (this.config.ca()) {
         ChatMessageType type = chatMessage.getType();
         String player;
         if (type.equals(ChatMessageType.PRIVATECHATOUT)) {
            player = this.client.getLocalPlayer().getName();
         } else {
            player = Text.sanitize(chatMessage.getName());
         }

         int num;
         try {
            num = this.chatClient.getKc(player, "Combat Achievements");
         } catch (IOException var8) {
            log.debug("unable to lookup combat achievements");
            return;
         }

         String response = (new ChatMessageBuilder()).append(ChatColorType.NORMAL).append("Combat Achievements: ").append(ChatColorType.HIGHLIGHT).append(Integer.toString(num)).build();
         log.debug("Setting response {}", response);
         MessageNode messageNode = chatMessage.getMessageNode();
         messageNode.setRuneLiteFormatMessage(response);
         this.client.refreshChat();
      }
   }

   private boolean caSubmit(ChatInput chatInput, String value) {
      int tasks = this.client.getVarbitValue(12885) + this.client.getVarbitValue(12886) + this.client.getVarbitValue(12887) + this.client.getVarbitValue(12888) + this.client.getVarbitValue(12889) + this.client.getVarbitValue(12890);
      String playerName = this.client.getLocalPlayer().getName();
      this.executor.execute(() -> {
         try {
            this.chatClient.submitKc(playerName, "Combat Achievements", tasks);
         } catch (Exception var8) {
            Exception ex = var8;
            log.warn("unable to submit combat achievements", ex);
         } finally {
            chatInput.resume();
         }

      });
      return true;
   }

   private void clogLookup(ChatMessage chatMessage, String message) {
      if (this.config.clog()) {
         ChatMessageType type = chatMessage.getType();
         String player;
         if (type.equals(ChatMessageType.PRIVATECHATOUT)) {
            player = this.client.getLocalPlayer().getName();
         } else {
            player = Text.sanitize(chatMessage.getName());
         }

         int num;
         try {
            num = this.chatClient.getKc(player, "Collections Logged");
         } catch (IOException var8) {
            log.debug("unable to lookup clog");
            return;
         }

         String response = (new ChatMessageBuilder()).append(ChatColorType.NORMAL).append("Collections Logged: ").append(ChatColorType.HIGHLIGHT).append(Integer.toString(num)).build();
         log.debug("Setting response {}", response);
         MessageNode messageNode = chatMessage.getMessageNode();
         messageNode.setRuneLiteFormatMessage(response);
         this.client.refreshChat();
      }
   }

   private boolean clogSubmit(ChatInput chatInput, String value) {
      int clog = this.client.getVarpValue(2943);
      String playerName = this.client.getLocalPlayer().getName();
      this.executor.execute(() -> {
         try {
            this.chatClient.submitKc(playerName, "Collections Logged", clog);
         } catch (Exception var8) {
            Exception ex = var8;
            log.warn("unable to submit clog", ex);
         } finally {
            chatInput.resume();
         }

      });
      return true;
   }

   private void itemPriceLookup(ChatMessage chatMessage, String message) {
      if (this.config.price()) {
         if (message.length() > "!price".length()) {
            MessageNode messageNode = chatMessage.getMessageNode();
            String search = message.substring("!price".length() + 1);
            List<ItemPrice> results = this.itemManager.search(search);
            if (!results.isEmpty()) {
               ItemPrice item = this.retrieveFromList(results, search);
               int itemId = item.getId();
               int itemPrice = this.runeLiteConfig.useWikiItemPrices() ? this.itemManager.getWikiPrice(item) : item.getPrice();
               ChatMessageBuilder builder = (new ChatMessageBuilder()).append(ChatColorType.NORMAL).append("Price of ").append(ChatColorType.HIGHLIGHT).append(item.getName()).append(ChatColorType.NORMAL).append(": GE average ").append(ChatColorType.HIGHLIGHT).append(QuantityFormatter.formatNumber((long)itemPrice));
               ItemComposition itemComposition = this.itemManager.getItemComposition(itemId);
               int alchPrice = itemComposition.getHaPrice();
               builder.append(ChatColorType.NORMAL).append(" HA value ").append(ChatColorType.HIGHLIGHT).append(QuantityFormatter.formatNumber((long)alchPrice));
               String response = builder.build();
               log.debug("Setting response {}", response);
               messageNode.setRuneLiteFormatMessage(response);
               this.client.refreshChat();
            }

         }
      }
   }

   @VisibleForTesting
   void playerSkillLookup(ChatMessage chatMessage, String message) {
      if (this.config.lvl()) {
         String search;
         if (message.equalsIgnoreCase("!total")) {
            search = "total";
         } else {
            if (message.length() <= "!lvl".length()) {
               return;
            }

            search = message.substring("!lvl".length() + 1);
         }

         HiscoreSkill skill = findHiscoreSkill(search);
         if (skill != null) {
            HiscoreLookup lookup = this.getCorrectLookupFor(chatMessage);

            try {
               HiscoreResult result = this.hiscoreClient.lookup(lookup.getName(), lookup.getEndpoint());
               if (result == null) {
                  log.warn("unable to look up skill {} for {}: not found", skill, search);
                  return;
               }

               Skill hiscoreSkill = result.getSkill(skill);
               ChatMessageBuilder chatMessageBuilder = (new ChatMessageBuilder()).append(ChatColorType.NORMAL).append("Level ").append(ChatColorType.HIGHLIGHT).append(skill.getName()).append(": ").append(hiscoreSkill.getLevel() > -1 ? String.valueOf(hiscoreSkill.getLevel()) : "unranked").append(ChatColorType.NORMAL);
               if (hiscoreSkill.getExperience() != -1L) {
                  chatMessageBuilder.append(" Experience: ").append(ChatColorType.HIGHLIGHT).append(String.format("%,d", hiscoreSkill.getExperience())).append(ChatColorType.NORMAL);
               }

               if (hiscoreSkill.getRank() != -1) {
                  chatMessageBuilder.append(" Rank: ").append(ChatColorType.HIGHLIGHT).append(String.format("%,d", hiscoreSkill.getRank()));
               }

               String response = chatMessageBuilder.build();
               log.debug("Setting response {}", response);
               MessageNode messageNode = chatMessage.getMessageNode();
               messageNode.setRuneLiteFormatMessage(response);
               this.client.refreshChat();
            } catch (IOException var11) {
               IOException ex = var11;
               log.warn("unable to look up skill {} for {}", new Object[]{skill, search, ex});
            }

         }
      }
   }

   private void combatLevelLookup(ChatMessage chatMessage, String message) {
      if (this.config.lvl()) {
         HiscoreLookup lookup = this.getCorrectLookupFor(chatMessage);

         try {
            HiscoreResult playerStats = this.hiscoreClient.lookup(lookup.getName(), lookup.getEndpoint());
            if (playerStats == null) {
               log.warn("Error fetching hiscore data: not found");
               return;
            }

            int attack = playerStats.getSkill(HiscoreSkill.ATTACK).getLevel();
            int strength = playerStats.getSkill(HiscoreSkill.STRENGTH).getLevel();
            int defence = playerStats.getSkill(HiscoreSkill.DEFENCE).getLevel();
            int hitpoints = playerStats.getSkill(HiscoreSkill.HITPOINTS).getLevel();
            int ranged = playerStats.getSkill(HiscoreSkill.RANGED).getLevel();
            int prayer = playerStats.getSkill(HiscoreSkill.PRAYER).getLevel();
            int magic = playerStats.getSkill(HiscoreSkill.MAGIC).getLevel();
            int combatLevel = Experience.getCombatLevel(attack, strength, defence, hitpoints, magic, ranged, prayer);
            String response = (new ChatMessageBuilder()).append(ChatColorType.NORMAL).append("Combat Level: ").append(ChatColorType.HIGHLIGHT).append(String.valueOf(combatLevel)).append(ChatColorType.NORMAL).append(" A: ").append(ChatColorType.HIGHLIGHT).append(String.valueOf(attack)).append(ChatColorType.NORMAL).append(" S: ").append(ChatColorType.HIGHLIGHT).append(String.valueOf(strength)).append(ChatColorType.NORMAL).append(" D: ").append(ChatColorType.HIGHLIGHT).append(String.valueOf(defence)).append(ChatColorType.NORMAL).append(" H: ").append(ChatColorType.HIGHLIGHT).append(String.valueOf(hitpoints)).append(ChatColorType.NORMAL).append(" R: ").append(ChatColorType.HIGHLIGHT).append(String.valueOf(ranged)).append(ChatColorType.NORMAL).append(" P: ").append(ChatColorType.HIGHLIGHT).append(String.valueOf(prayer)).append(ChatColorType.NORMAL).append(" M: ").append(ChatColorType.HIGHLIGHT).append(String.valueOf(magic)).build();
            log.debug("Setting response {}", response);
            MessageNode messageNode = chatMessage.getMessageNode();
            messageNode.setRuneLiteFormatMessage(response);
            this.client.refreshChat();
         } catch (IOException var15) {
            IOException ex = var15;
            log.warn("Error fetching hiscore data", ex);
         }

      }
   }

   private void leaguePointsLookup(ChatMessage chatMessage, String message) {
      if (this.config.lp()) {
         this.minigameLookup(chatMessage, HiscoreSkill.LEAGUE_POINTS);
      }
   }

   private void bountyHunterHunterLookup(ChatMessage chatMessage, String message) {
      if (this.config.bh()) {
         this.minigameLookup(chatMessage, HiscoreSkill.BOUNTY_HUNTER_HUNTER);
      }
   }

   private void bountyHunterRogueLookup(ChatMessage chatMessage, String message) {
      if (this.config.bhRogue()) {
         this.minigameLookup(chatMessage, HiscoreSkill.BOUNTY_HUNTER_ROGUE);
      }
   }

   private void lastManStandingLookup(ChatMessage chatMessage, String message) {
      if (this.config.lms()) {
         this.minigameLookup(chatMessage, HiscoreSkill.LAST_MAN_STANDING);
      }
   }

   private void soulWarsZealLookup(ChatMessage chatMessage, String message) {
      if (this.config.sw()) {
         this.minigameLookup(chatMessage, HiscoreSkill.SOUL_WARS_ZEAL);
      }
   }

   private void minigameLookup(ChatMessage chatMessage, HiscoreSkill minigame) {
      try {
         HiscoreLookup lookup = this.getCorrectLookupFor(chatMessage);
         HiscoreEndpoint endPoint = minigame == HiscoreSkill.LEAGUE_POINTS ? HiscoreEndpoint.SEASONAL : lookup.getEndpoint();
         HiscoreResult result = this.hiscoreClient.lookup(lookup.getName(), endPoint);
         if (result == null) {
            log.warn("error looking up {} score: not found", minigame.getName().toLowerCase());
            return;
         }

         switch (minigame) {
            case BOUNTY_HUNTER_HUNTER:
            case BOUNTY_HUNTER_ROGUE:
            case LAST_MAN_STANDING:
            case LEAGUE_POINTS:
            case SOUL_WARS_ZEAL:
               Skill hiscoreSkill = result.getSkill(minigame);
               int score = hiscoreSkill.getLevel();
               if (score == -1) {
                  return;
               }

               ChatMessageBuilder chatMessageBuilder = (new ChatMessageBuilder()).append(ChatColorType.NORMAL).append(minigame.getName()).append(" Score: ").append(ChatColorType.HIGHLIGHT).append(String.format("%,d", score));
               int rank = hiscoreSkill.getRank();
               if (rank != -1) {
                  chatMessageBuilder.append(ChatColorType.NORMAL).append(" Rank: ").append(ChatColorType.HIGHLIGHT).append(String.format("%,d", rank));
               }

               String response = chatMessageBuilder.build();
               log.debug("Setting response {}", response);
               MessageNode messageNode = chatMessage.getMessageNode();
               messageNode.setRuneLiteFormatMessage(response);
               this.client.refreshChat();
               break;
            default:
               log.warn("error looking up {} score: not implemented", minigame.getName().toLowerCase());
               return;
         }
      } catch (IOException var12) {
         IOException ex = var12;
         log.warn("error looking up {}", minigame.getName().toLowerCase(), ex);
      }

   }

   private void clueLookup(ChatMessage chatMessage, String message) {
      if (this.config.clue()) {
         String search;
         if (message.equalsIgnoreCase("!clues")) {
            search = "total";
         } else {
            search = message.substring("!clues".length() + 1);
         }

         try {
            HiscoreLookup lookup = this.getCorrectLookupFor(chatMessage);
            HiscoreResult result = this.hiscoreClient.lookup(lookup.getName(), lookup.getEndpoint());
            if (result == null) {
               log.warn("error looking up clues: not found");
               return;
            }

            Skill hiscoreSkill;
            switch (search.toLowerCase()) {
               case "beginner":
                  hiscoreSkill = result.getSkill(HiscoreSkill.CLUE_SCROLL_BEGINNER);
                  break;
               case "easy":
                  hiscoreSkill = result.getSkill(HiscoreSkill.CLUE_SCROLL_EASY);
                  break;
               case "medium":
                  hiscoreSkill = result.getSkill(HiscoreSkill.CLUE_SCROLL_MEDIUM);
                  break;
               case "hard":
                  hiscoreSkill = result.getSkill(HiscoreSkill.CLUE_SCROLL_HARD);
                  break;
               case "elite":
                  hiscoreSkill = result.getSkill(HiscoreSkill.CLUE_SCROLL_ELITE);
                  break;
               case "master":
                  hiscoreSkill = result.getSkill(HiscoreSkill.CLUE_SCROLL_MASTER);
                  break;
               case "total":
                  hiscoreSkill = result.getSkill(HiscoreSkill.CLUE_SCROLL_ALL);
                  break;
               default:
                  return;
            }

            int quantity = hiscoreSkill.getLevel();
            rank = hiscoreSkill.getRank();
            if (quantity == -1) {
               return;
            }

            ChatMessageBuilder chatMessageBuilder = (new ChatMessageBuilder()).append(ChatColorType.NORMAL).append("Clue scroll (" + level + ")").append(": ").append(ChatColorType.HIGHLIGHT).append(String.format("%,d", quantity));
            if (rank != -1) {
               chatMessageBuilder.append(ChatColorType.NORMAL).append(" Rank: ").append(ChatColorType.HIGHLIGHT).append(String.format("%,d", rank));
            }

            String response = chatMessageBuilder.build();
            log.debug("Setting response {}", response);
            MessageNode messageNode = chatMessage.getMessageNode();
            messageNode.setRuneLiteFormatMessage(response);
            this.client.refreshChat();
         } catch (IOException var13) {
            IOException ex = var13;
            log.warn("error looking up clues", ex);
         }

      }
   }

   private HiscoreLookup getCorrectLookupFor(ChatMessage chatMessage) {
      Player localPlayer = this.client.getLocalPlayer();
      String player = Text.sanitize(chatMessage.getName());
      if (!chatMessage.getType().equals(ChatMessageType.PRIVATECHATOUT) && !player.equals(localPlayer.getName())) {
         HiscoreEndpoint endpoint;
         if (chatMessage.getType() == ChatMessageType.PUBLICCHAT || chatMessage.getType() == ChatMessageType.MODCHAT) {
            endpoint = HiscoreEndpoint.fromWorldTypes(this.client.getWorldType());
            if (endpoint != HiscoreEndpoint.NORMAL) {
               return new HiscoreLookup(player, endpoint);
            }
         }

         endpoint = getHiscoreEndpointByName(chatMessage.getName());
         return new HiscoreLookup(player, endpoint);
      } else {
         return new HiscoreLookup(localPlayer.getName(), this.hiscoreEndpoint);
      }
   }

   private ItemPrice retrieveFromList(List<ItemPrice> items, String originalInput) {
      ItemPrice shortest = null;
      Iterator var4 = items.iterator();

      while(true) {
         ItemPrice item;
         do {
            if (!var4.hasNext()) {
               return shortest;
            }

            item = (ItemPrice)var4.next();
            if (item.getName().toLowerCase().equals(originalInput.toLowerCase())) {
               return item;
            }
         } while(shortest != null && item.getName().length() >= shortest.getName().length());

         shortest = item;
      }
   }

   private HiscoreEndpoint getLocalHiscoreEndpointType() {
      EnumSet<WorldType> worldType = this.client.getWorldType();
      HiscoreEndpoint endpoint = HiscoreEndpoint.fromWorldTypes(worldType);
      return endpoint != HiscoreEndpoint.NORMAL ? endpoint : toEndPoint(this.client.getVarbitValue(1777));
   }

   private static HiscoreEndpoint getHiscoreEndpointByName(String name) {
      if (name.contains(IconID.IRONMAN.toString())) {
         return HiscoreEndpoint.IRONMAN;
      } else if (name.contains(IconID.ULTIMATE_IRONMAN.toString())) {
         return HiscoreEndpoint.ULTIMATE_IRONMAN;
      } else if (name.contains(IconID.HARDCORE_IRONMAN.toString())) {
         return HiscoreEndpoint.HARDCORE_IRONMAN;
      } else {
         return name.contains(IconID.LEAGUE.toString()) ? HiscoreEndpoint.SEASONAL : HiscoreEndpoint.NORMAL;
      }
   }

   private static HiscoreEndpoint toEndPoint(int accountType) {
      switch (accountType) {
         case 1:
            return HiscoreEndpoint.IRONMAN;
         case 2:
            return HiscoreEndpoint.ULTIMATE_IRONMAN;
         case 3:
            return HiscoreEndpoint.HARDCORE_IRONMAN;
         default:
            return HiscoreEndpoint.NORMAL;
      }
   }

   private static String longBossName(String boss) {
      String lowerBoss = boss.toLowerCase();
      if (lowerBoss.endsWith(" (echo)")) {
         String actualBoss = lowerBoss.substring(0, lowerBoss.length() - " (echo)".length());
         return longBossName(actualBoss) + " (Echo)";
      } else {
         switch (lowerBoss) {
            case "corp":
               return "Corporeal Beast";
            case "jad":
            case "tzhaar fight cave":
               return "TzTok-Jad";
            case "kq":
               return "Kalphite Queen";
            case "chaos ele":
               return "Chaos Elemental";
            case "dusk":
            case "dawn":
            case "gargs":
            case "ggs":
            case "gg":
               return "Grotesque Guardians";
            case "crazy arch":
               return "Crazy Archaeologist";
            case "deranged arch":
               return "Deranged Archaeologist";
            case "mole":
               return "Giant Mole";
            case "vetion":
               return "Vet'ion";
            case "calvarion":
            case "calv":
               return "Calvar'ion";
            case "vene":
               return "Venenatis";
            case "kbd":
               return "King Black Dragon";
            case "vork":
               return "Vorkath";
            case "sire":
               return "Abyssal Sire";
            case "smoke devil":
            case "thermy":
               return "Thermonuclear Smoke Devil";
            case "cerb":
               return "Cerberus";
            case "zuk":
            case "inferno":
               return "TzKal-Zuk";
            case "hydra":
               return "Alchemical Hydra";
            case "sara":
            case "saradomin":
            case "zilyana":
            case "zily":
               return "Commander Zilyana";
            case "zammy":
            case "zamorak":
            case "kril":
            case "kril tsutsaroth":
               return "K'ril Tsutsaroth";
            case "arma":
            case "kree":
            case "kreearra":
            case "armadyl":
               return "Kree'arra";
            case "bando":
            case "bandos":
            case "graardor":
               return "General Graardor";
            case "supreme":
               return "Dagannoth Supreme";
            case "rex":
               return "Dagannoth Rex";
            case "prime":
               return "Dagannoth Prime";
            case "wt":
               return "Wintertodt";
            case "barrows":
               return "Barrows Chests";
            case "herbi":
               return "Herbiboar";
            case "cox":
            case "xeric":
            case "chambers":
            case "olm":
            case "raids":
               return "Chambers of Xeric";
            case "cox 1":
            case "cox solo":
               return "Chambers of Xeric Solo";
            case "cox 2":
            case "cox duo":
               return "Chambers of Xeric 2 players";
            case "cox 3":
               return "Chambers of Xeric 3 players";
            case "cox 4":
               return "Chambers of Xeric 4 players";
            case "cox 5":
               return "Chambers of Xeric 5 players";
            case "cox 6":
               return "Chambers of Xeric 6 players";
            case "cox 7":
               return "Chambers of Xeric 7 players";
            case "cox 8":
               return "Chambers of Xeric 8 players";
            case "cox 9":
               return "Chambers of Xeric 9 players";
            case "cox 10":
               return "Chambers of Xeric 10 players";
            case "cox 11-15":
            case "cox 11":
            case "cox 12":
            case "cox 13":
            case "cox 14":
            case "cox 15":
               return "Chambers of Xeric 11-15 players";
            case "cox 16-23":
            case "cox 16":
            case "cox 17":
            case "cox 18":
            case "cox 19":
            case "cox 20":
            case "cox 21":
            case "cox 22":
            case "cox 23":
               return "Chambers of Xeric 16-23 players";
            case "cox 24":
            case "cox 24+":
               return "Chambers of Xeric 24+ players";
            case "chambers of xeric: challenge mode":
            case "cox cm":
            case "xeric cm":
            case "chambers cm":
            case "olm cm":
            case "raids cm":
            case "chambers of xeric - challenge mode":
               return "Chambers of Xeric Challenge Mode";
            case "cox cm 1":
            case "cox cm solo":
               return "Chambers of Xeric Challenge Mode Solo";
            case "cox cm 2":
            case "cox cm duo":
               return "Chambers of Xeric Challenge Mode 2 players";
            case "cox cm 3":
               return "Chambers of Xeric Challenge Mode 3 players";
            case "cox cm 4":
               return "Chambers of Xeric Challenge Mode 4 players";
            case "cox cm 5":
               return "Chambers of Xeric Challenge Mode 5 players";
            case "cox cm 6":
               return "Chambers of Xeric Challenge Mode 6 players";
            case "cox cm 7":
               return "Chambers of Xeric Challenge Mode 7 players";
            case "cox cm 8":
               return "Chambers of Xeric Challenge Mode 8 players";
            case "cox cm 9":
               return "Chambers of Xeric Challenge Mode 9 players";
            case "cox cm 10":
               return "Chambers of Xeric Challenge Mode 10 players";
            case "cox cm 11-15":
            case "cox cm 11":
            case "cox cm 12":
            case "cox cm 13":
            case "cox cm 14":
            case "cox cm 15":
               return "Chambers of Xeric Challenge Mode 11-15 players";
            case "cox cm 16-23":
            case "cox cm 16":
            case "cox cm 17":
            case "cox cm 18":
            case "cox cm 19":
            case "cox cm 20":
            case "cox cm 21":
            case "cox cm 22":
            case "cox cm 23":
               return "Chambers of Xeric Challenge Mode 16-23 players";
            case "cox cm 24":
            case "cox cm 24+":
               return "Chambers of Xeric Challenge Mode 24+ players";
            case "tob":
            case "theatre":
            case "verzik":
            case "verzik vitur":
            case "raids 2":
               return "Theatre of Blood";
            case "tob 1":
            case "tob solo":
               return "Theatre of Blood Solo";
            case "tob 2":
            case "tob duo":
               return "Theatre of Blood 2 players";
            case "tob 3":
               return "Theatre of Blood 3 players";
            case "tob 4":
               return "Theatre of Blood 4 players";
            case "tob 5":
               return "Theatre of Blood 5 players";
            case "theatre of blood: story mode":
            case "tob sm":
            case "tob story mode":
            case "tob story":
            case "theatre of blood: entry mode":
            case "tob em":
            case "tob entry mode":
            case "tob entry":
               return "Theatre of Blood Entry Mode";
            case "theatre of blood: hard mode":
            case "tob cm":
            case "tob hm":
            case "tob hard mode":
            case "tob hard":
            case "hmt":
               return "Theatre of Blood Hard Mode";
            case "hmt 1":
            case "hmt solo":
               return "Theatre of Blood Hard Mode Solo";
            case "hmt 2":
            case "hmt duo":
               return "Theatre of Blood Hard Mode 2 players";
            case "hmt 3":
               return "Theatre of Blood Hard Mode 3 players";
            case "hmt 4":
               return "Theatre of Blood Hard Mode 4 players";
            case "hmt 5":
               return "Theatre of Blood Hard Mode 5 players";
            case "toa":
            case "tombs":
            case "amascut":
            case "warden":
            case "wardens":
            case "raids 3":
               return "Tombs of Amascut";
            case "toa 1":
            case "toa solo":
               return "Tombs of Amascut Solo";
            case "toa 2":
            case "toa duo":
               return "Tombs of Amascut 2 players";
            case "toa 3":
               return "Tombs of Amascut 3 players";
            case "toa 4":
               return "Tombs of Amascut 4 players";
            case "toa 5":
               return "Tombs of Amascut 5 players";
            case "toa 6":
               return "Tombs of Amascut 6 players";
            case "toa 7":
               return "Tombs of Amascut 7 players";
            case "toa 8":
               return "Tombs of Amascut 8 players";
            case "toa entry":
            case "tombs of amascut - entry":
            case "toa entry mode":
               return "Tombs of Amascut Entry Mode";
            case "toa entry 1":
            case "toa entry solo":
               return "Tombs of Amascut Entry Mode Solo";
            case "toa entry 2":
            case "toa entry duo":
               return "Tombs of Amascut Entry Mode 2 players";
            case "toa entry 3":
               return "Tombs of Amascut Entry Mode 3 players";
            case "toa entry 4":
               return "Tombs of Amascut Entry Mode 4 players";
            case "toa entry 5":
               return "Tombs of Amascut Entry Mode 5 players";
            case "toa entry 6":
               return "Tombs of Amascut Entry Mode 6 players";
            case "toa entry 7":
               return "Tombs of Amascut Entry Mode 7 players";
            case "toa entry 8":
               return "Tombs of Amascut Entry Mode 8 players";
            case "tombs of amascut: expert mode":
            case "toa expert":
            case "tombs of amascut - expert":
            case "toa expert mode":
               return "Tombs of Amascut Expert Mode";
            case "toa expert 1":
            case "toa expert solo":
               return "Tombs of Amascut Expert Mode Solo";
            case "toa expert 2":
            case "toa expert duo":
               return "Tombs of Amascut Expert Mode 2 players";
            case "toa expert 3":
               return "Tombs of Amascut Expert Mode 3 players";
            case "toa expert 4":
               return "Tombs of Amascut Expert Mode 4 players";
            case "toa expert 5":
               return "Tombs of Amascut Expert Mode 5 players";
            case "toa expert 6":
               return "Tombs of Amascut Expert Mode 6 players";
            case "toa expert 7":
               return "Tombs of Amascut Expert Mode 7 players";
            case "toa expert 8":
               return "Tombs of Amascut Expert Mode 8 players";
            case "gaunt":
            case "gauntlet":
            case "the gauntlet":
               return "Gauntlet";
            case "cgaunt":
            case "cgauntlet":
            case "the corrupted gauntlet":
            case "cg":
               return "Corrupted Gauntlet";
            case "nm":
            case "tnm":
            case "nmare":
            case "the nightmare":
               return "Nightmare";
            case "pnm":
            case "phosani":
            case "phosanis":
            case "phosani nm":
            case "phosani nightmare":
            case "phosanis nightmare":
               return "Phosani's Nightmare";
            case "hs":
            case "sepulchre":
            case "ghc":
               return "Hallowed Sepulchre";
            case "hs1":
            case "hs 1":
               return "Hallowed Sepulchre Floor 1";
            case "hs2":
            case "hs 2":
               return "Hallowed Sepulchre Floor 2";
            case "hs3":
            case "hs 3":
               return "Hallowed Sepulchre Floor 3";
            case "hs4":
            case "hs 4":
               return "Hallowed Sepulchre Floor 4";
            case "hs5":
            case "hs 5":
               return "Hallowed Sepulchre Floor 5";
            case "wbac":
            case "cwbac":
            case "wyrmb":
            case "wyrmbasic":
            case "wyrm basic":
            case "colossal basic":
            case "colossal wyrm basic":
               return "Colossal Wyrm Agility Course (Basic)";
            case "waac":
            case "cwaac":
            case "wyrma":
            case "wyrmadvanced":
            case "wyrm advanced":
            case "colossal advanced":
            case "colossal wyrm advanced":
               return "Colossal Wyrm Agility Course (Advanced)";
            case "prif":
            case "prifddinas":
               return "Prifddinas Agility Course";
            case "shayb":
            case "sbac":
            case "shayzienbasic":
            case "shayzien basic":
               return "Shayzien Basic Agility Course";
            case "shaya":
            case "saac":
            case "shayadv":
            case "shayadvanced":
            case "shayzien advanced":
               return "Shayzien Advanced Agility Course";
            case "aa":
            case "ape atoll":
               return "Ape Atoll Agility";
            case "draynor":
            case "draynor agility":
               return "Draynor Village Rooftop";
            case "al kharid":
            case "al kharid agility":
            case "al-kharid":
            case "al-kharid agility":
            case "alkharid":
            case "alkharid agility":
               return "Al Kharid Rooftop";
            case "varrock":
            case "varrock agility":
               return "Varrock Rooftop";
            case "canifis":
            case "canifis agility":
               return "Canifis Rooftop";
            case "fally":
            case "fally agility":
            case "falador":
            case "falador agility":
               return "Falador Rooftop";
            case "seers":
            case "seers agility":
            case "seers village":
            case "seers village agility":
            case "seers'":
            case "seers' agility":
            case "seers' village":
            case "seers' village agility":
            case "seer's":
            case "seer's agility":
            case "seer's village":
            case "seer's village agility":
               return "Seers' Village Rooftop";
            case "pollnivneach":
            case "pollnivneach agility":
               return "Pollnivneach Rooftop";
            case "rellekka":
            case "rellekka agility":
               return "Rellekka Rooftop";
            case "ardy":
            case "ardy agility":
            case "ardy rooftop":
            case "ardougne":
            case "ardougne agility":
               return "Ardougne Rooftop";
            case "ap":
            case "pyramid":
               return "Agility Pyramid";
            case "barb":
            case "barb outpost":
               return "Barbarian Outpost";
            case "brimhaven":
            case "brimhaven agility":
               return "Agility Arena";
            case "dorg":
            case "dorgesh kaan":
            case "dorgesh-kaan":
               return "Dorgesh-Kaan Agility";
            case "gnome stronghold":
               return "Gnome Stronghold Agility";
            case "penguin":
               return "Penguin Agility";
            case "werewolf":
               return "Werewolf Agility";
            case "skullball":
               return "Werewolf Skullball";
            case "wildy":
            case "wildy agility":
               return "Wilderness Agility";
            case "jad 1":
               return "TzHaar-Ket-Rak's First Challenge";
            case "jad 2":
               return "TzHaar-Ket-Rak's Second Challenge";
            case "jad 3":
               return "TzHaar-Ket-Rak's Third Challenge";
            case "jad 4":
               return "TzHaar-Ket-Rak's Fourth Challenge";
            case "jad 5":
               return "TzHaar-Ket-Rak's Fifth Challenge";
            case "jad 6":
               return "TzHaar-Ket-Rak's Sixth Challenge";
            case "gotr":
            case "runetodt":
            case "rifts closed":
               return "Guardians of the Rift";
            case "fishingtodt":
            case "fishtodt":
               return "Tempoross";
            case "phantom":
            case "muspah":
            case "pm":
               return "Phantom Muspah";
            case "the leviathan":
            case "levi":
               return "Leviathan";
            case "duke":
               return "Duke Sucellus";
            case "the whisperer":
            case "whisp":
            case "wisp":
               return "Whisperer";
            case "vard":
               return "Vardorvis";
            case "leviathan awakened":
            case "the leviathan awakened":
            case "levi awakened":
               return "Leviathan (awakened)";
            case "duke sucellus awakened":
            case "duke awakened":
               return "Duke Sucellus (awakened)";
            case "whisperer awakened":
            case "the whisperer awakened":
            case "whisp awakened":
            case "wisp awakened":
               return "Whisperer (awakened)";
            case "vardorvis awakened":
            case "vard awakened":
               return "Vardorvis (awakened)";
            case "lunar chests":
            case "perilous moons":
            case "perilous moon":
            case "moons of peril":
               return "Lunar Chest";
            case "hunterrumour":
            case "hunter contract":
            case "hunter contracts":
            case "hunter tasks":
            case "hunter task":
            case "rumours":
            case "rumour":
               return "Hunter Rumours";
            case "sol":
            case "colo":
            case "colosseum":
            case "fortis colosseum":
               return "Sol Heredit";
            case "glory":
            case "colo glory":
               return "Colosseum Glory";
            case "bird egg":
            case "bird eggs":
            case "bird's egg":
            case "bird's eggs":
               return "Bird's egg offerings";
            case "amox":
               return "Amoxliatl";
            case "the hueycoatl":
            case "huey":
               return "Hueycoatl";
            case "crystal chest":
               return "crystal chest";
            case "larran small chest":
            case "larran's small chest":
               return "Larran's small chest";
            case "larran chest":
            case "larran's chest":
            case "larran big chest":
            case "larran's big chest":
               return "Larran's big chest";
            case "brimstone chest":
               return "Brimstone chest";
            case "dom":
            case "doom":
               return "Doom of Mokhaiotl";
            default:
               return WordUtils.capitalize(boss);
         }
      }
   }

   private static String longSkillName(String skill) {
      switch (skill.toUpperCase()) {
         case "ATK":
         case "ATT":
            return net.runelite.api.Skill.ATTACK.getName();
         case "DEF":
            return net.runelite.api.Skill.DEFENCE.getName();
         case "STR":
            return net.runelite.api.Skill.STRENGTH.getName();
         case "HEALTH":
         case "HIT":
         case "HITPOINT":
         case "HP":
            return net.runelite.api.Skill.HITPOINTS.getName();
         case "RANGE":
         case "RANGING":
         case "RNG":
            return net.runelite.api.Skill.RANGED.getName();
         case "PRAY":
            return net.runelite.api.Skill.PRAYER.getName();
         case "MAG":
         case "MAGE":
            return net.runelite.api.Skill.MAGIC.getName();
         case "COOK":
            return net.runelite.api.Skill.COOKING.getName();
         case "WC":
         case "WOOD":
         case "WOODCUT":
            return net.runelite.api.Skill.WOODCUTTING.getName();
         case "FLETCH":
            return net.runelite.api.Skill.FLETCHING.getName();
         case "FISH":
            return net.runelite.api.Skill.FISHING.getName();
         case "FM":
         case "FIRE":
            return net.runelite.api.Skill.FIREMAKING.getName();
         case "CRAFT":
            return net.runelite.api.Skill.CRAFTING.getName();
         case "SMITH":
            return net.runelite.api.Skill.SMITHING.getName();
         case "MINE":
            return net.runelite.api.Skill.MINING.getName();
         case "HL":
         case "HERB":
            return net.runelite.api.Skill.HERBLORE.getName();
         case "AGI":
         case "AGIL":
            return net.runelite.api.Skill.AGILITY.getName();
         case "THIEF":
            return net.runelite.api.Skill.THIEVING.getName();
         case "SLAY":
            return net.runelite.api.Skill.SLAYER.getName();
         case "FARM":
            return net.runelite.api.Skill.FARMING.getName();
         case "RC":
         case "RUNE":
         case "RUNECRAFTING":
            return net.runelite.api.Skill.RUNECRAFT.getName();
         case "HUNT":
            return net.runelite.api.Skill.HUNTER.getName();
         case "CON":
         case "CONSTRUCT":
            return net.runelite.api.Skill.CONSTRUCTION.getName();
         case "SAIL":
            return net.runelite.api.Skill.SAILING.getName();
         case "ALL":
         case "TOTAL":
            return "Overall";
         default:
            return skill;
      }
   }

   private static HiscoreSkill findHiscoreSkill(String search) {
      String s = longSkillName(search);
      if (s == search) {
         s = longBossName(search);
      }

      HiscoreSkill[] var2 = HiscoreSkill.values();
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         HiscoreSkill skill = var2[var4];
         if (longBossName(skill.getName()).equalsIgnoreCase(s)) {
            return skill;
         }
      }

      return null;
   }

   private int tobTeamSize() {
      return Math.min(this.client.getVarbitValue(6442), 1) + Math.min(this.client.getVarbitValue(6443), 1) + Math.min(this.client.getVarbitValue(6444), 1) + Math.min(this.client.getVarbitValue(6445), 1) + Math.min(this.client.getVarbitValue(6446), 1);
   }

   private int toaTeamSize() {
      return Math.min(this.client.getVarbitValue(14346), 1) + Math.min(this.client.getVarbitValue(14347), 1) + Math.min(this.client.getVarbitValue(14348), 1) + Math.min(this.client.getVarbitValue(14349), 1) + Math.min(this.client.getVarbitValue(14350), 1) + Math.min(this.client.getVarbitValue(14351), 1) + Math.min(this.client.getVarbitValue(14352), 1) + Math.min(this.client.getVarbitValue(14353), 1);
   }

   private int findPet(String name) {
      int[] var2 = this.pets;
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         int petId = var2[var4];
         ItemComposition item = this.itemManager.getItemComposition(petId);
         if (item.getName().equals(name)) {
            return item.getId();
         }
      }

      return -1;
   }

   private static final class HiscoreLookup {
      private final String name;
      private final HiscoreEndpoint endpoint;

      public HiscoreLookup(String name, HiscoreEndpoint endpoint) {
         this.name = name;
         this.endpoint = endpoint;
      }

      public String getName() {
         return this.name;
      }

      public HiscoreEndpoint getEndpoint() {
         return this.endpoint;
      }

      public boolean equals(Object o) {
         if (o == this) {
            return true;
         } else if (!(o instanceof HiscoreLookup)) {
            return false;
         } else {
            HiscoreLookup other = (HiscoreLookup)o;
            Object this$name = this.getName();
            Object other$name = other.getName();
            if (this$name == null) {
               if (other$name != null) {
                  return false;
               }
            } else if (!this$name.equals(other$name)) {
               return false;
            }

            Object this$endpoint = this.getEndpoint();
            Object other$endpoint = other.getEndpoint();
            if (this$endpoint == null) {
               if (other$endpoint != null) {
                  return false;
               }
            } else if (!this$endpoint.equals(other$endpoint)) {
               return false;
            }

            return true;
         }
      }

      public int hashCode() {
         int PRIME = true;
         int result = 1;
         Object $name = this.getName();
         result = result * 59 + ($name == null ? 43 : $name.hashCode());
         Object $endpoint = this.getEndpoint();
         result = result * 59 + ($endpoint == null ? 43 : $endpoint.hashCode());
         return result;
      }

      public String toString() {
         String var10000 = this.getName();
         return "ChatCommandsPlugin.HiscoreLookup(name=" + var10000 + ", endpoint=" + String.valueOf(this.getEndpoint()) + ")";
      }
   }
}
