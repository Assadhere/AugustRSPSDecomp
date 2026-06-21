package net.runelite.client.plugins.timetracking.farming;

import javax.annotation.Nullable;
import net.runelite.client.plugins.timetracking.Tab;

public enum PatchImplementation {
   MUSHROOM(Tab.SPECIAL, "", false) {
      PatchState forVarbitValue(int value) {
         if (value >= 0 && value <= 3) {
            return new PatchState(Produce.WEEDS, CropState.GROWING, 3 - value);
         } else if (value >= 4 && value <= 9) {
            return new PatchState(Produce.MUSHROOM, CropState.GROWING, value - 4);
         } else if (value >= 10 && value <= 15) {
            return new PatchState(Produce.MUSHROOM, CropState.HARVESTABLE, value - 10);
         } else if (value >= 16 && value <= 20) {
            return new PatchState(Produce.MUSHROOM, CropState.DISEASED, value - 15);
         } else if (value >= 21 && value <= 25) {
            return new PatchState(Produce.MUSHROOM, CropState.DEAD, value - 20);
         } else {
            return value >= 26 && value <= 255 ? new PatchState(Produce.WEEDS, CropState.GROWING, 3) : null;
         }
      }
   },
   HESPORI(Tab.SPECIAL, "", true) {
      PatchState forVarbitValue(int value) {
         if (value >= 0 && value <= 3) {
            return new PatchState(Produce.WEEDS, CropState.GROWING, 3 - value);
         } else if (value >= 4 && value <= 6) {
            return new PatchState(Produce.HESPORI, CropState.GROWING, value - 4);
         } else if (value >= 7 && value <= 8) {
            return new PatchState(Produce.HESPORI, CropState.HARVESTABLE, value - 7);
         } else {
            return value == 9 ? new PatchState(Produce.WEEDS, CropState.GROWING, 3) : null;
         }
      }
   },
   ALLOTMENT(Tab.ALLOTMENT, "", false) {
      PatchState forVarbitValue(int value) {
         if (value >= 0 && value <= 3) {
            return new PatchState(Produce.WEEDS, CropState.GROWING, 3 - value);
         } else if (value >= 4 && value <= 5) {
            return new PatchState(Produce.WEEDS, CropState.GROWING, 3);
         } else if (value >= 6 && value <= 9) {
            return new PatchState(Produce.POTATO, CropState.GROWING, value - 6);
         } else if (value >= 10 && value <= 12) {
            return new PatchState(Produce.POTATO, CropState.HARVESTABLE, value - 10);
         } else if (value >= 13 && value <= 16) {
            return new PatchState(Produce.ONION, CropState.GROWING, value - 13);
         } else if (value >= 17 && value <= 19) {
            return new PatchState(Produce.ONION, CropState.HARVESTABLE, value - 17);
         } else if (value >= 20 && value <= 23) {
            return new PatchState(Produce.CABBAGE, CropState.GROWING, value - 20);
         } else if (value >= 24 && value <= 26) {
            return new PatchState(Produce.CABBAGE, CropState.HARVESTABLE, value - 24);
         } else if (value >= 27 && value <= 30) {
            return new PatchState(Produce.TOMATO, CropState.GROWING, value - 27);
         } else if (value >= 31 && value <= 33) {
            return new PatchState(Produce.TOMATO, CropState.HARVESTABLE, value - 31);
         } else if (value >= 34 && value <= 39) {
            return new PatchState(Produce.SWEETCORN, CropState.GROWING, value - 34);
         } else if (value >= 40 && value <= 42) {
            return new PatchState(Produce.SWEETCORN, CropState.HARVESTABLE, value - 40);
         } else if (value >= 43 && value <= 48) {
            return new PatchState(Produce.STRAWBERRY, CropState.GROWING, value - 43);
         } else if (value >= 49 && value <= 51) {
            return new PatchState(Produce.STRAWBERRY, CropState.HARVESTABLE, value - 49);
         } else if (value >= 52 && value <= 59) {
            return new PatchState(Produce.WATERMELON, CropState.GROWING, value - 52);
         } else if (value >= 60 && value <= 62) {
            return new PatchState(Produce.WATERMELON, CropState.HARVESTABLE, value - 60);
         } else if (value >= 63 && value <= 69) {
            return new PatchState(Produce.SNAPE_GRASS, CropState.GROWING, value - 63);
         } else if (value >= 70 && value <= 73) {
            return new PatchState(Produce.POTATO, CropState.GROWING, value - 70);
         } else if (value >= 74 && value <= 76) {
            return new PatchState(Produce.WEEDS, CropState.GROWING, 3);
         } else if (value >= 77 && value <= 80) {
            return new PatchState(Produce.ONION, CropState.GROWING, value - 77);
         } else if (value >= 81 && value <= 83) {
            return new PatchState(Produce.WEEDS, CropState.GROWING, 3);
         } else if (value >= 84 && value <= 87) {
            return new PatchState(Produce.CABBAGE, CropState.GROWING, value - 84);
         } else if (value >= 88 && value <= 90) {
            return new PatchState(Produce.WEEDS, CropState.GROWING, 3);
         } else if (value >= 91 && value <= 94) {
            return new PatchState(Produce.TOMATO, CropState.GROWING, value - 91);
         } else if (value >= 95 && value <= 97) {
            return new PatchState(Produce.WEEDS, CropState.GROWING, 3);
         } else if (value >= 98 && value <= 103) {
            return new PatchState(Produce.SWEETCORN, CropState.GROWING, value - 98);
         } else if (value >= 104 && value <= 106) {
            return new PatchState(Produce.WEEDS, CropState.GROWING, 3);
         } else if (value >= 107 && value <= 112) {
            return new PatchState(Produce.STRAWBERRY, CropState.GROWING, value - 107);
         } else if (value >= 113 && value <= 115) {
            return new PatchState(Produce.WEEDS, CropState.GROWING, 3);
         } else if (value >= 116 && value <= 123) {
            return new PatchState(Produce.WATERMELON, CropState.GROWING, value - 116);
         } else if (value >= 124 && value <= 127) {
            return new PatchState(Produce.WEEDS, CropState.GROWING, 3);
         } else if (value >= 128 && value <= 134) {
            return new PatchState(Produce.SNAPE_GRASS, CropState.GROWING, value - 128);
         } else if (value >= 135 && value <= 137) {
            return new PatchState(Produce.POTATO, CropState.DISEASED, value - 134);
         } else if (value >= 138 && value <= 140) {
            return new PatchState(Produce.SNAPE_GRASS, CropState.HARVESTABLE, value - 138);
         } else if (value == 141) {
            return new PatchState(Produce.WEEDS, CropState.GROWING, 3);
         } else if (value >= 142 && value <= 144) {
            return new PatchState(Produce.ONION, CropState.DISEASED, value - 141);
         } else if (value >= 145 && value <= 148) {
            return new PatchState(Produce.WEEDS, CropState.GROWING, 3);
         } else if (value >= 149 && value <= 151) {
            return new PatchState(Produce.CABBAGE, CropState.DISEASED, value - 148);
         } else if (value >= 152 && value <= 155) {
            return new PatchState(Produce.WEEDS, CropState.GROWING, 3);
         } else if (value >= 156 && value <= 158) {
            return new PatchState(Produce.TOMATO, CropState.DISEASED, value - 155);
         } else if (value >= 159 && value <= 162) {
            return new PatchState(Produce.WEEDS, CropState.GROWING, 3);
         } else if (value >= 163 && value <= 167) {
            return new PatchState(Produce.SWEETCORN, CropState.DISEASED, value - 162);
         } else if (value >= 168 && value <= 171) {
            return new PatchState(Produce.WEEDS, CropState.GROWING, 3);
         } else if (value >= 172 && value <= 176) {
            return new PatchState(Produce.STRAWBERRY, CropState.DISEASED, value - 171);
         } else if (value >= 177 && value <= 180) {
            return new PatchState(Produce.WEEDS, CropState.GROWING, 3);
         } else if (value >= 181 && value <= 187) {
            return new PatchState(Produce.WATERMELON, CropState.DISEASED, value - 180);
         } else if (value >= 188 && value <= 192) {
            return new PatchState(Produce.WEEDS, CropState.GROWING, 3);
         } else if (value >= 193 && value <= 195) {
            return new PatchState(Produce.SNAPE_GRASS, CropState.DEAD, value - 192);
         } else if (value >= 196 && value <= 198) {
            return new PatchState(Produce.SNAPE_GRASS, CropState.DISEASED, value - 195);
         } else if (value >= 199 && value <= 201) {
            return new PatchState(Produce.POTATO, CropState.DEAD, value - 198);
         } else if (value >= 202 && value <= 204) {
            return new PatchState(Produce.SNAPE_GRASS, CropState.DISEASED, 3 + value - 201);
         } else if (value == 205) {
            return new PatchState(Produce.WEEDS, CropState.GROWING, 3);
         } else if (value >= 206 && value <= 208) {
            return new PatchState(Produce.ONION, CropState.DEAD, value - 205);
         } else if (value >= 209 && value <= 211) {
            return new PatchState(Produce.SNAPE_GRASS, CropState.DEAD, 3 + value - 208);
         } else if (value == 212) {
            return new PatchState(Produce.WEEDS, CropState.GROWING, 3);
         } else if (value >= 213 && value <= 215) {
            return new PatchState(Produce.CABBAGE, CropState.DEAD, value - 212);
         } else if (value >= 216 && value <= 219) {
            return new PatchState(Produce.WEEDS, CropState.GROWING, 3);
         } else if (value >= 220 && value <= 222) {
            return new PatchState(Produce.TOMATO, CropState.DEAD, value - 219);
         } else if (value >= 223 && value <= 226) {
            return new PatchState(Produce.WEEDS, CropState.GROWING, 3);
         } else if (value >= 227 && value <= 231) {
            return new PatchState(Produce.SWEETCORN, CropState.DEAD, value - 226);
         } else if (value >= 232 && value <= 235) {
            return new PatchState(Produce.WEEDS, CropState.GROWING, 3);
         } else if (value >= 236 && value <= 240) {
            return new PatchState(Produce.STRAWBERRY, CropState.DEAD, value - 235);
         } else if (value >= 241 && value <= 244) {
            return new PatchState(Produce.WEEDS, CropState.GROWING, 3);
         } else if (value >= 245 && value <= 251) {
            return new PatchState(Produce.WATERMELON, CropState.DEAD, value - 244);
         } else {
            return value >= 252 && value <= 255 ? new PatchState(Produce.WEEDS, CropState.GROWING, 3) : null;
         }
      }
   },
   HERB(Tab.HERB, "", false) {
      PatchState forVarbitValue(int value) {
         if (value >= 0 && value <= 3) {
            return new PatchState(Produce.WEEDS, CropState.GROWING, 3 - value);
         } else if (value >= 4 && value <= 7) {
            return new PatchState(Produce.GUAM, CropState.GROWING, value - 4);
         } else if (value >= 8 && value <= 10) {
            return new PatchState(Produce.GUAM, CropState.HARVESTABLE, 10 - value);
         } else if (value >= 11 && value <= 14) {
            return new PatchState(Produce.MARRENTILL, CropState.GROWING, value - 11);
         } else if (value >= 15 && value <= 17) {
            return new PatchState(Produce.MARRENTILL, CropState.HARVESTABLE, 17 - value);
         } else if (value >= 18 && value <= 21) {
            return new PatchState(Produce.TARROMIN, CropState.GROWING, value - 18);
         } else if (value >= 22 && value <= 24) {
            return new PatchState(Produce.TARROMIN, CropState.HARVESTABLE, 24 - value);
         } else if (value >= 25 && value <= 28) {
            return new PatchState(Produce.HARRALANDER, CropState.GROWING, value - 25);
         } else if (value >= 29 && value <= 31) {
            return new PatchState(Produce.HARRALANDER, CropState.HARVESTABLE, 31 - value);
         } else if (value >= 32 && value <= 35) {
            return new PatchState(Produce.RANARR, CropState.GROWING, value - 32);
         } else if (value >= 36 && value <= 38) {
            return new PatchState(Produce.RANARR, CropState.HARVESTABLE, 38 - value);
         } else if (value >= 39 && value <= 42) {
            return new PatchState(Produce.TOADFLAX, CropState.GROWING, value - 39);
         } else if (value >= 43 && value <= 45) {
            return new PatchState(Produce.TOADFLAX, CropState.HARVESTABLE, 45 - value);
         } else if (value >= 46 && value <= 49) {
            return new PatchState(Produce.IRIT, CropState.GROWING, value - 46);
         } else if (value >= 50 && value <= 52) {
            return new PatchState(Produce.IRIT, CropState.HARVESTABLE, 52 - value);
         } else if (value >= 53 && value <= 56) {
            return new PatchState(Produce.AVANTOE, CropState.GROWING, value - 53);
         } else if (value >= 57 && value <= 59) {
            return new PatchState(Produce.AVANTOE, CropState.HARVESTABLE, 59 - value);
         } else if (value >= 60 && value <= 63) {
            return new PatchState(Produce.HUASCA, CropState.GROWING, value - 60);
         } else if (value >= 64 && value <= 66) {
            return new PatchState(Produce.HUASCA, CropState.HARVESTABLE, 66 - value);
         } else if (value == 67) {
            return new PatchState(Produce.WEEDS, CropState.GROWING, 3);
         } else if (value >= 68 && value <= 71) {
            return new PatchState(Produce.KWUARM, CropState.GROWING, value - 68);
         } else if (value >= 72 && value <= 74) {
            return new PatchState(Produce.KWUARM, CropState.HARVESTABLE, 74 - value);
         } else if (value >= 75 && value <= 78) {
            return new PatchState(Produce.SNAPDRAGON, CropState.GROWING, value - 75);
         } else if (value >= 79 && value <= 81) {
            return new PatchState(Produce.SNAPDRAGON, CropState.HARVESTABLE, 81 - value);
         } else if (value >= 82 && value <= 85) {
            return new PatchState(Produce.CADANTINE, CropState.GROWING, value - 82);
         } else if (value >= 86 && value <= 88) {
            return new PatchState(Produce.CADANTINE, CropState.HARVESTABLE, 88 - value);
         } else if (value >= 89 && value <= 92) {
            return new PatchState(Produce.LANTADYME, CropState.GROWING, value - 89);
         } else if (value >= 93 && value <= 95) {
            return new PatchState(Produce.LANTADYME, CropState.HARVESTABLE, 95 - value);
         } else if (value >= 96 && value <= 99) {
            return new PatchState(Produce.DWARF_WEED, CropState.GROWING, value - 96);
         } else if (value >= 100 && value <= 102) {
            return new PatchState(Produce.DWARF_WEED, CropState.HARVESTABLE, 102 - value);
         } else if (value >= 103 && value <= 106) {
            return new PatchState(Produce.TORSTOL, CropState.GROWING, value - 103);
         } else if (value >= 107 && value <= 109) {
            return new PatchState(Produce.TORSTOL, CropState.HARVESTABLE, 109 - value);
         } else if (value >= 128 && value <= 130) {
            return new PatchState(Produce.GUAM, CropState.DISEASED, value - 127);
         } else if (value >= 131 && value <= 133) {
            return new PatchState(Produce.MARRENTILL, CropState.DISEASED, value - 130);
         } else if (value >= 134 && value <= 136) {
            return new PatchState(Produce.TARROMIN, CropState.DISEASED, value - 133);
         } else if (value >= 137 && value <= 139) {
            return new PatchState(Produce.HARRALANDER, CropState.DISEASED, value - 136);
         } else if (value >= 140 && value <= 142) {
            return new PatchState(Produce.RANARR, CropState.DISEASED, value - 139);
         } else if (value >= 143 && value <= 145) {
            return new PatchState(Produce.TOADFLAX, CropState.DISEASED, value - 142);
         } else if (value >= 146 && value <= 148) {
            return new PatchState(Produce.IRIT, CropState.DISEASED, value - 145);
         } else if (value >= 149 && value <= 151) {
            return new PatchState(Produce.AVANTOE, CropState.DISEASED, value - 148);
         } else if (value >= 152 && value <= 154) {
            return new PatchState(Produce.KWUARM, CropState.DISEASED, value - 151);
         } else if (value >= 155 && value <= 157) {
            return new PatchState(Produce.SNAPDRAGON, CropState.DISEASED, value - 154);
         } else if (value >= 158 && value <= 160) {
            return new PatchState(Produce.CADANTINE, CropState.DISEASED, value - 157);
         } else if (value >= 161 && value <= 163) {
            return new PatchState(Produce.LANTADYME, CropState.DISEASED, value - 160);
         } else if (value >= 164 && value <= 166) {
            return new PatchState(Produce.DWARF_WEED, CropState.DISEASED, value - 163);
         } else if (value >= 167 && value <= 169) {
            return new PatchState(Produce.TORSTOL, CropState.DISEASED, value - 166);
         } else if (value >= 170 && value <= 172) {
            return new PatchState(Produce.ANYHERB, CropState.DEAD, value - 169);
         } else if (value >= 173 && value <= 175) {
            return new PatchState(Produce.HUASCA, CropState.DISEASED, value - 172);
         } else if (value >= 176 && value <= 191) {
            return new PatchState(Produce.WEEDS, CropState.GROWING, 3);
         } else if (value >= 192 && value <= 195) {
            return new PatchState(Produce.GOUTWEED, CropState.GROWING, value - 192);
         } else if (value >= 196 && value <= 197) {
            return new PatchState(Produce.GOUTWEED, CropState.HARVESTABLE, 197 - value);
         } else if (value >= 198 && value <= 200) {
            return new PatchState(Produce.GOUTWEED, CropState.DISEASED, value - 197);
         } else if (value >= 201 && value <= 203) {
            return new PatchState(Produce.GOUTWEED, CropState.DEAD, value - 200);
         } else if (value >= 204 && value <= 219) {
            return new PatchState(Produce.WEEDS, CropState.GROWING, 3);
         } else {
            return value >= 221 && value <= 255 ? new PatchState(Produce.WEEDS, CropState.GROWING, 3) : null;
         }
      }
   },
   FLOWER(Tab.FLOWER, "", false) {
      PatchState forVarbitValue(int value) {
         if (value >= 0 && value <= 3) {
            return new PatchState(Produce.WEEDS, CropState.GROWING, 3 - value);
         } else if (value >= 4 && value <= 7) {
            return new PatchState(Produce.WEEDS, CropState.GROWING, 3);
         } else if (value >= 8 && value <= 11) {
            return new PatchState(Produce.MARIGOLD, CropState.GROWING, value - 8);
         } else if (value == 12) {
            return new PatchState(Produce.MARIGOLD, CropState.HARVESTABLE, 0);
         } else if (value >= 13 && value <= 16) {
            return new PatchState(Produce.ROSEMARY, CropState.GROWING, value - 13);
         } else if (value == 17) {
            return new PatchState(Produce.ROSEMARY, CropState.HARVESTABLE, 0);
         } else if (value >= 18 && value <= 21) {
            return new PatchState(Produce.NASTURTIUM, CropState.GROWING, value - 18);
         } else if (value == 22) {
            return new PatchState(Produce.NASTURTIUM, CropState.HARVESTABLE, 0);
         } else if (value >= 23 && value <= 26) {
            return new PatchState(Produce.WOAD, CropState.GROWING, value - 23);
         } else if (value == 27) {
            return new PatchState(Produce.WOAD, CropState.HARVESTABLE, 0);
         } else if (value >= 28 && value <= 31) {
            return new PatchState(Produce.LIMPWURT, CropState.GROWING, value - 28);
         } else if (value == 32) {
            return new PatchState(Produce.LIMPWURT, CropState.HARVESTABLE, 0);
         } else if (value >= 33 && value <= 35) {
            return new PatchState(Produce.SCARECROW, CropState.GROWING, 35 - value);
         } else if (value == 36) {
            return new PatchState(Produce.SCARECROW, CropState.GROWING, 0);
         } else if (value >= 37 && value <= 40) {
            return new PatchState(Produce.WHITE_LILY, CropState.GROWING, value - 37);
         } else if (value == 41) {
            return new PatchState(Produce.WHITE_LILY, CropState.HARVESTABLE, 0);
         } else if (value >= 42 && value <= 71) {
            return new PatchState(Produce.WEEDS, CropState.GROWING, 3);
         } else if (value >= 72 && value <= 75) {
            return new PatchState(Produce.MARIGOLD, CropState.GROWING, value - 72);
         } else if (value == 76) {
            return new PatchState(Produce.WEEDS, CropState.GROWING, 3);
         } else if (value >= 77 && value <= 80) {
            return new PatchState(Produce.ROSEMARY, CropState.GROWING, value - 77);
         } else if (value == 81) {
            return new PatchState(Produce.WEEDS, CropState.GROWING, 3);
         } else if (value >= 82 && value <= 85) {
            return new PatchState(Produce.NASTURTIUM, CropState.GROWING, value - 82);
         } else if (value == 86) {
            return new PatchState(Produce.WEEDS, CropState.GROWING, 3);
         } else if (value >= 87 && value <= 90) {
            return new PatchState(Produce.WOAD, CropState.GROWING, value - 87);
         } else if (value == 91) {
            return new PatchState(Produce.WEEDS, CropState.GROWING, 3);
         } else if (value >= 92 && value <= 95) {
            return new PatchState(Produce.LIMPWURT, CropState.GROWING, value - 92);
         } else if (value >= 96 && value <= 100) {
            return new PatchState(Produce.WEEDS, CropState.GROWING, 3);
         } else if (value >= 101 && value <= 104) {
            return new PatchState(Produce.WHITE_LILY, CropState.GROWING, value - 101);
         } else if (value >= 105 && value <= 136) {
            return new PatchState(Produce.WEEDS, CropState.GROWING, 3);
         } else if (value >= 137 && value <= 139) {
            return new PatchState(Produce.MARIGOLD, CropState.DISEASED, value - 136);
         } else if (value >= 140 && value <= 141) {
            return new PatchState(Produce.WEEDS, CropState.GROWING, 3);
         } else if (value >= 142 && value <= 144) {
            return new PatchState(Produce.ROSEMARY, CropState.DISEASED, value - 141);
         } else if (value >= 145 && value <= 146) {
            return new PatchState(Produce.WEEDS, CropState.GROWING, 3);
         } else if (value >= 147 && value <= 149) {
            return new PatchState(Produce.NASTURTIUM, CropState.DISEASED, value - 146);
         } else if (value >= 150 && value <= 151) {
            return new PatchState(Produce.WEEDS, CropState.GROWING, 3);
         } else if (value >= 152 && value <= 154) {
            return new PatchState(Produce.WOAD, CropState.DISEASED, value - 151);
         } else if (value >= 155 && value <= 156) {
            return new PatchState(Produce.WEEDS, CropState.GROWING, 3);
         } else if (value >= 157 && value <= 159) {
            return new PatchState(Produce.LIMPWURT, CropState.DISEASED, value - 156);
         } else if (value >= 160 && value <= 165) {
            return new PatchState(Produce.WEEDS, CropState.GROWING, 3);
         } else if (value >= 166 && value <= 168) {
            return new PatchState(Produce.WHITE_LILY, CropState.DISEASED, value - 165);
         } else if (value >= 169 && value <= 200) {
            return new PatchState(Produce.WEEDS, CropState.GROWING, 3);
         } else if (value >= 201 && value <= 204) {
            return new PatchState(Produce.MARIGOLD, CropState.DEAD, value - 200);
         } else if (value == 205) {
            return new PatchState(Produce.WEEDS, CropState.GROWING, 3);
         } else if (value >= 206 && value <= 209) {
            return new PatchState(Produce.ROSEMARY, CropState.DEAD, value - 205);
         } else if (value == 210) {
            return new PatchState(Produce.WEEDS, CropState.GROWING, 3);
         } else if (value >= 211 && value <= 214) {
            return new PatchState(Produce.NASTURTIUM, CropState.DEAD, value - 210);
         } else if (value == 215) {
            return new PatchState(Produce.WEEDS, CropState.GROWING, 3);
         } else if (value >= 216 && value <= 219) {
            return new PatchState(Produce.WOAD, CropState.DEAD, value - 215);
         } else if (value == 220) {
            return new PatchState(Produce.WEEDS, CropState.GROWING, 3);
         } else if (value >= 221 && value <= 224) {
            return new PatchState(Produce.LIMPWURT, CropState.DEAD, value - 220);
         } else if (value >= 225 && value <= 229) {
            return new PatchState(Produce.WEEDS, CropState.GROWING, 3);
         } else if (value >= 230 && value <= 233) {
            return new PatchState(Produce.WHITE_LILY, CropState.DEAD, value - 229);
         } else {
            return value >= 234 && value <= 255 ? new PatchState(Produce.WEEDS, CropState.GROWING, 3) : null;
         }
      }
   },
   BUSH(Tab.BUSH, "", true) {
      PatchState forVarbitValue(int value) {
         if (value >= 0 && value <= 3) {
            return new PatchState(Produce.WEEDS, CropState.GROWING, 3 - value);
         } else if (value == 4) {
            return new PatchState(Produce.WEEDS, CropState.GROWING, 3);
         } else if (value >= 5 && value <= 9) {
            return new PatchState(Produce.REDBERRIES, CropState.GROWING, value - 5);
         } else if (value >= 10 && value <= 14) {
            return new PatchState(Produce.REDBERRIES, CropState.HARVESTABLE, value - 10);
         } else if (value >= 15 && value <= 20) {
            return new PatchState(Produce.CADAVABERRIES, CropState.GROWING, value - 15);
         } else if (value >= 21 && value <= 25) {
            return new PatchState(Produce.CADAVABERRIES, CropState.HARVESTABLE, value - 21);
         } else if (value >= 26 && value <= 32) {
            return new PatchState(Produce.DWELLBERRIES, CropState.GROWING, value - 26);
         } else if (value >= 33 && value <= 37) {
            return new PatchState(Produce.DWELLBERRIES, CropState.HARVESTABLE, value - 33);
         } else if (value >= 38 && value <= 45) {
            return new PatchState(Produce.JANGERBERRIES, CropState.GROWING, value - 38);
         } else if (value >= 46 && value <= 50) {
            return new PatchState(Produce.JANGERBERRIES, CropState.HARVESTABLE, value - 46);
         } else if (value >= 51 && value <= 58) {
            return new PatchState(Produce.WHITEBERRIES, CropState.GROWING, value - 51);
         } else if (value >= 59 && value <= 63) {
            return new PatchState(Produce.WHITEBERRIES, CropState.HARVESTABLE, value - 59);
         } else if (value >= 64 && value <= 69) {
            return new PatchState(Produce.WEEDS, CropState.GROWING, 3);
         } else if (value >= 70 && value <= 74) {
            return new PatchState(Produce.REDBERRIES, CropState.DISEASED, value - 69);
         } else if (value >= 75 && value <= 79) {
            return new PatchState(Produce.WEEDS, CropState.GROWING, 3);
         } else if (value >= 80 && value <= 85) {
            return new PatchState(Produce.CADAVABERRIES, CropState.DISEASED, value - 79);
         } else if (value >= 86 && value <= 90) {
            return new PatchState(Produce.WEEDS, CropState.GROWING, 3);
         } else if (value >= 91 && value <= 97) {
            return new PatchState(Produce.DWELLBERRIES, CropState.DISEASED, value - 90);
         } else if (value >= 98 && value <= 102) {
            return new PatchState(Produce.WEEDS, CropState.GROWING, 3);
         } else if (value >= 103 && value <= 110) {
            return new PatchState(Produce.JANGERBERRIES, CropState.DISEASED, value - 102);
         } else if (value >= 111 && value <= 115) {
            return new PatchState(Produce.WEEDS, CropState.GROWING, 3);
         } else if (value >= 116 && value <= 123) {
            return new PatchState(Produce.WHITEBERRIES, CropState.DISEASED, value - 115);
         } else if (value >= 124 && value <= 133) {
            return new PatchState(Produce.WEEDS, CropState.GROWING, 3);
         } else if (value >= 134 && value <= 138) {
            return new PatchState(Produce.REDBERRIES, CropState.DEAD, value - 133);
         } else if (value >= 139 && value <= 143) {
            return new PatchState(Produce.WEEDS, CropState.GROWING, 3);
         } else if (value >= 144 && value <= 149) {
            return new PatchState(Produce.CADAVABERRIES, CropState.DEAD, value - 143);
         } else if (value >= 150 && value <= 154) {
            return new PatchState(Produce.WEEDS, CropState.GROWING, 3);
         } else if (value >= 155 && value <= 161) {
            return new PatchState(Produce.DWELLBERRIES, CropState.DEAD, value - 154);
         } else if (value >= 162 && value <= 166) {
            return new PatchState(Produce.WEEDS, CropState.GROWING, 3);
         } else if (value >= 167 && value <= 174) {
            return new PatchState(Produce.JANGERBERRIES, CropState.DEAD, value - 166);
         } else if (value >= 175 && value <= 179) {
            return new PatchState(Produce.WEEDS, CropState.GROWING, 3);
         } else if (value >= 180 && value <= 187) {
            return new PatchState(Produce.WHITEBERRIES, CropState.DEAD, value - 179);
         } else if (value >= 188 && value <= 196) {
            return new PatchState(Produce.WEEDS, CropState.GROWING, 3);
         } else if (value >= 197 && value <= 204) {
            return new PatchState(Produce.POISON_IVY, CropState.GROWING, value - 197);
         } else if (value >= 205 && value <= 209) {
            return new PatchState(Produce.POISON_IVY, CropState.HARVESTABLE, value - 205);
         } else if (value >= 210 && value <= 216) {
            return new PatchState(Produce.POISON_IVY, CropState.DISEASED, value - 209);
         } else if (value >= 217 && value <= 224) {
            return new PatchState(Produce.POISON_IVY, CropState.DEAD, value - 216);
         } else if (value == 225) {
            return new PatchState(Produce.POISON_IVY, CropState.DISEASED, 8);
         } else if (value >= 226 && value <= 249) {
            return new PatchState(Produce.WEEDS, CropState.GROWING, 3);
         } else if (value == 250) {
            return new PatchState(Produce.REDBERRIES, CropState.GROWING, Produce.REDBERRIES.getStages() - 1);
         } else if (value == 251) {
            return new PatchState(Produce.CADAVABERRIES, CropState.GROWING, Produce.CADAVABERRIES.getStages() - 1);
         } else if (value == 252) {
            return new PatchState(Produce.DWELLBERRIES, CropState.GROWING, Produce.DWELLBERRIES.getStages() - 1);
         } else if (value == 253) {
            return new PatchState(Produce.JANGERBERRIES, CropState.GROWING, Produce.JANGERBERRIES.getStages() - 1);
         } else if (value == 254) {
            return new PatchState(Produce.WHITEBERRIES, CropState.GROWING, Produce.WHITEBERRIES.getStages() - 1);
         } else {
            return value == 255 ? new PatchState(Produce.POISON_IVY, CropState.GROWING, Produce.POISON_IVY.getStages() - 1) : null;
         }
      }
   },
   FRUIT_TREE(Tab.FRUIT_TREE, "", true) {
      PatchState forVarbitValue(int value) {
         if (value >= 0 && value <= 3) {
            return new PatchState(Produce.WEEDS, CropState.GROWING, 3 - value);
         } else if (value >= 4 && value <= 7) {
            return new PatchState(Produce.WEEDS, CropState.GROWING, 3);
         } else if (value >= 8 && value <= 13) {
            return new PatchState(Produce.APPLE, CropState.GROWING, value - 8);
         } else if (value >= 14 && value <= 20) {
            return new PatchState(Produce.APPLE, CropState.HARVESTABLE, value - 14);
         } else if (value >= 21 && value <= 26) {
            return new PatchState(Produce.APPLE, CropState.DISEASED, value - 20);
         } else if (value >= 27 && value <= 32) {
            return new PatchState(Produce.APPLE, CropState.DEAD, value - 26);
         } else if (value == 33) {
            return new PatchState(Produce.APPLE, CropState.HARVESTABLE, 0);
         } else if (value == 34) {
            return new PatchState(Produce.APPLE, CropState.GROWING, Produce.APPLE.getStages() - 1);
         } else if (value >= 35 && value <= 40) {
            return new PatchState(Produce.BANANA, CropState.GROWING, value - 35);
         } else if (value >= 41 && value <= 47) {
            return new PatchState(Produce.BANANA, CropState.HARVESTABLE, value - 41);
         } else if (value >= 48 && value <= 53) {
            return new PatchState(Produce.BANANA, CropState.DISEASED, value - 47);
         } else if (value >= 54 && value <= 59) {
            return new PatchState(Produce.BANANA, CropState.DEAD, value - 53);
         } else if (value == 60) {
            return new PatchState(Produce.BANANA, CropState.HARVESTABLE, 0);
         } else if (value == 61) {
            return new PatchState(Produce.BANANA, CropState.GROWING, Produce.BANANA.getStages() - 1);
         } else if (value >= 62 && value <= 71) {
            return new PatchState(Produce.WEEDS, CropState.GROWING, 3);
         } else if (value >= 72 && value <= 77) {
            return new PatchState(Produce.ORANGE, CropState.GROWING, value - 72);
         } else if (value >= 78 && value <= 84) {
            return new PatchState(Produce.ORANGE, CropState.HARVESTABLE, value - 78);
         } else if (value >= 85 && value <= 89) {
            return new PatchState(Produce.ORANGE, CropState.DISEASED, value - 84);
         } else if (value == 90) {
            return new PatchState(Produce.ORANGE, CropState.DISEASED, 6);
         } else if (value >= 91 && value <= 96) {
            return new PatchState(Produce.ORANGE, CropState.DEAD, value - 90);
         } else if (value == 97) {
            return new PatchState(Produce.ORANGE, CropState.HARVESTABLE, 0);
         } else if (value == 98) {
            return new PatchState(Produce.ORANGE, CropState.GROWING, Produce.ORANGE.getStages() - 1);
         } else if (value >= 99 && value <= 104) {
            return new PatchState(Produce.CURRY, CropState.GROWING, value - 99);
         } else if (value >= 105 && value <= 111) {
            return new PatchState(Produce.CURRY, CropState.HARVESTABLE, value - 105);
         } else if (value >= 112 && value <= 117) {
            return new PatchState(Produce.CURRY, CropState.DISEASED, value - 111);
         } else if (value >= 118 && value <= 123) {
            return new PatchState(Produce.CURRY, CropState.DEAD, value - 117);
         } else if (value == 124) {
            return new PatchState(Produce.CURRY, CropState.HARVESTABLE, 0);
         } else if (value == 125) {
            return new PatchState(Produce.CURRY, CropState.GROWING, Produce.CURRY.getStages() - 1);
         } else if (value >= 126 && value <= 135) {
            return new PatchState(Produce.WEEDS, CropState.GROWING, 3);
         } else if (value >= 136 && value <= 141) {
            return new PatchState(Produce.PINEAPPLE, CropState.GROWING, value - 136);
         } else if (value >= 142 && value <= 148) {
            return new PatchState(Produce.PINEAPPLE, CropState.HARVESTABLE, value - 142);
         } else if (value >= 149 && value <= 154) {
            return new PatchState(Produce.PINEAPPLE, CropState.DISEASED, value - 148);
         } else if (value >= 155 && value <= 160) {
            return new PatchState(Produce.PINEAPPLE, CropState.DEAD, value - 154);
         } else if (value == 161) {
            return new PatchState(Produce.PINEAPPLE, CropState.HARVESTABLE, 0);
         } else if (value == 162) {
            return new PatchState(Produce.PINEAPPLE, CropState.GROWING, Produce.PINEAPPLE.getStages() - 1);
         } else if (value >= 163 && value <= 168) {
            return new PatchState(Produce.PAPAYA, CropState.GROWING, value - 163);
         } else if (value >= 169 && value <= 175) {
            return new PatchState(Produce.PAPAYA, CropState.HARVESTABLE, value - 169);
         } else if (value >= 176 && value <= 181) {
            return new PatchState(Produce.PAPAYA, CropState.DISEASED, value - 175);
         } else if (value >= 182 && value <= 187) {
            return new PatchState(Produce.PAPAYA, CropState.DEAD, value - 181);
         } else if (value == 188) {
            return new PatchState(Produce.PAPAYA, CropState.HARVESTABLE, 0);
         } else if (value == 189) {
            return new PatchState(Produce.PAPAYA, CropState.GROWING, Produce.PAPAYA.getStages() - 1);
         } else if (value >= 190 && value <= 199) {
            return new PatchState(Produce.WEEDS, CropState.GROWING, 3);
         } else if (value >= 200 && value <= 205) {
            return new PatchState(Produce.PALM, CropState.GROWING, value - 200);
         } else if (value >= 206 && value <= 212) {
            return new PatchState(Produce.PALM, CropState.HARVESTABLE, value - 206);
         } else if (value >= 213 && value <= 218) {
            return new PatchState(Produce.PALM, CropState.DISEASED, value - 212);
         } else if (value >= 219 && value <= 224) {
            return new PatchState(Produce.PALM, CropState.DEAD, value - 218);
         } else if (value == 225) {
            return new PatchState(Produce.PALM, CropState.HARVESTABLE, 0);
         } else if (value == 226) {
            return new PatchState(Produce.PALM, CropState.GROWING, Produce.PALM.getStages() - 1);
         } else if (value >= 227 && value <= 232) {
            return new PatchState(Produce.DRAGONFRUIT, CropState.GROWING, value - 227);
         } else if (value >= 233 && value <= 239) {
            return new PatchState(Produce.DRAGONFRUIT, CropState.HARVESTABLE, value - 233);
         } else if (value >= 240 && value <= 245) {
            return new PatchState(Produce.DRAGONFRUIT, CropState.DISEASED, value - 239);
         } else if (value >= 246 && value <= 251) {
            return new PatchState(Produce.DRAGONFRUIT, CropState.DEAD, value - 245);
         } else if (value == 252) {
            return new PatchState(Produce.DRAGONFRUIT, CropState.HARVESTABLE, 0);
         } else if (value == 253) {
            return new PatchState(Produce.DRAGONFRUIT, CropState.GROWING, Produce.DRAGONFRUIT.getStages() - 1);
         } else {
            return value >= 254 && value <= 255 ? new PatchState(Produce.WEEDS, CropState.GROWING, 3) : null;
         }
      }
   },
   HOPS(Tab.HOPS, "", false) {
      PatchState forVarbitValue(int value) {
         if (value >= 0 && value <= 3) {
            return new PatchState(Produce.WEEDS, CropState.GROWING, 3 - value);
         } else if (value >= 4 && value <= 7) {
            return new PatchState(Produce.HAMMERSTONE, CropState.GROWING, value - 4);
         } else if (value >= 8 && value <= 10) {
            return new PatchState(Produce.HAMMERSTONE, CropState.HARVESTABLE, 10 - value);
         } else if (value >= 11 && value <= 13) {
            return new PatchState(Produce.HAMMERSTONE, CropState.DISEASED, value - 10);
         } else if (value >= 14 && value <= 18) {
            return new PatchState(Produce.ASGARNIAN, CropState.GROWING, value - 14);
         } else if (value >= 19 && value <= 21) {
            return new PatchState(Produce.ASGARNIAN, CropState.HARVESTABLE, 21 - value);
         } else if (value >= 22 && value <= 25) {
            return new PatchState(Produce.ASGARNIAN, CropState.DISEASED, value - 21);
         } else if (value >= 26 && value <= 31) {
            return new PatchState(Produce.YANILLIAN, CropState.GROWING, value - 26);
         } else if (value >= 32 && value <= 34) {
            return new PatchState(Produce.YANILLIAN, CropState.HARVESTABLE, 34 - value);
         } else if (value >= 35 && value <= 39) {
            return new PatchState(Produce.YANILLIAN, CropState.DISEASED, value - 34);
         } else if (value >= 40 && value <= 46) {
            return new PatchState(Produce.KRANDORIAN, CropState.GROWING, value - 40);
         } else if (value >= 47 && value <= 49) {
            return new PatchState(Produce.KRANDORIAN, CropState.HARVESTABLE, 49 - value);
         } else if (value >= 50 && value <= 55) {
            return new PatchState(Produce.KRANDORIAN, CropState.DISEASED, value - 49);
         } else if (value >= 56 && value <= 63) {
            return new PatchState(Produce.WILDBLOOD, CropState.GROWING, value - 56);
         } else if (value >= 64 && value <= 66) {
            return new PatchState(Produce.WILDBLOOD, CropState.HARVESTABLE, 66 - value);
         } else if (value >= 67 && value <= 73) {
            return new PatchState(Produce.WILDBLOOD, CropState.DISEASED, value - 66);
         } else if (value >= 74 && value <= 77) {
            return new PatchState(Produce.BARLEY, CropState.GROWING, value - 74);
         } else if (value >= 78 && value <= 80) {
            return new PatchState(Produce.BARLEY, CropState.HARVESTABLE, 80 - value);
         } else if (value >= 81 && value <= 83) {
            return new PatchState(Produce.BARLEY, CropState.DISEASED, value - 80);
         } else if (value >= 84 && value <= 88) {
            return new PatchState(Produce.JUTE, CropState.GROWING, value - 84);
         } else if (value >= 89 && value <= 91) {
            return new PatchState(Produce.JUTE, CropState.HARVESTABLE, 91 - value);
         } else if (value >= 92 && value <= 95) {
            return new PatchState(Produce.JUTE, CropState.DISEASED, value - 91);
         } else if (value >= 96 && value <= 98) {
            return new PatchState(Produce.FLAX, CropState.GROWING, value - 96);
         } else if (value >= 99 && value <= 101) {
            return new PatchState(Produce.FLAX, CropState.HARVESTABLE, 101 - value);
         } else if (value >= 102 && value <= 103) {
            return new PatchState(Produce.FLAX, CropState.DISEASED, value - 101);
         } else if (value >= 104 && value <= 107) {
            return new PatchState(Produce.HEMP, CropState.GROWING, value - 104);
         } else if (value >= 108 && value <= 110) {
            return new PatchState(Produce.HEMP, CropState.HARVESTABLE, 110 - value);
         } else if (value >= 111 && value <= 113) {
            return new PatchState(Produce.HEMP, CropState.DISEASED, value - 110);
         } else if (value >= 114 && value <= 118) {
            return new PatchState(Produce.COTTON, CropState.GROWING, value - 114);
         } else if (value >= 119 && value <= 121) {
            return new PatchState(Produce.COTTON, CropState.HARVESTABLE, 121 - value);
         } else if (value >= 122 && value <= 125) {
            return new PatchState(Produce.COTTON, CropState.DISEASED, value - 121);
         } else if (value >= 126 && value <= 131) {
            return new PatchState(Produce.WEEDS, CropState.GROWING, 3);
         } else if (value >= 132 && value <= 135) {
            return new PatchState(Produce.HAMMERSTONE, CropState.GROWING, value - 132);
         } else if (value >= 136 && value <= 138) {
            return new PatchState(Produce.WEEDS, CropState.GROWING, 3);
         } else if (value >= 139 && value <= 141) {
            return new PatchState(Produce.HAMMERSTONE, CropState.DEAD, value - 138);
         } else if (value >= 142 && value <= 146) {
            return new PatchState(Produce.ASGARNIAN, CropState.GROWING, value - 142);
         } else if (value >= 147 && value <= 149) {
            return new PatchState(Produce.WEEDS, CropState.GROWING, 3);
         } else if (value >= 150 && value <= 153) {
            return new PatchState(Produce.ASGARNIAN, CropState.DEAD, value - 149);
         } else if (value >= 154 && value <= 159) {
            return new PatchState(Produce.YANILLIAN, CropState.GROWING, value - 154);
         } else if (value >= 160 && value <= 162) {
            return new PatchState(Produce.WEEDS, CropState.GROWING, 3);
         } else if (value >= 163 && value <= 167) {
            return new PatchState(Produce.YANILLIAN, CropState.DEAD, value - 162);
         } else if (value >= 168 && value <= 174) {
            return new PatchState(Produce.KRANDORIAN, CropState.GROWING, value - 168);
         } else if (value >= 175 && value <= 177) {
            return new PatchState(Produce.WEEDS, CropState.GROWING, 3);
         } else if (value >= 178 && value <= 183) {
            return new PatchState(Produce.KRANDORIAN, CropState.DEAD, value - 177);
         } else if (value >= 184 && value <= 191) {
            return new PatchState(Produce.WILDBLOOD, CropState.GROWING, value - 184);
         } else if (value >= 192 && value <= 194) {
            return new PatchState(Produce.WEEDS, CropState.GROWING, 3);
         } else if (value >= 195 && value <= 201) {
            return new PatchState(Produce.WILDBLOOD, CropState.DEAD, value - 194);
         } else if (value >= 202 && value <= 205) {
            return new PatchState(Produce.BARLEY, CropState.GROWING, value - 202);
         } else if (value >= 206 && value <= 208) {
            return new PatchState(Produce.WEEDS, CropState.GROWING, 3);
         } else if (value >= 209 && value <= 211) {
            return new PatchState(Produce.BARLEY, CropState.DEAD, value - 208);
         } else if (value >= 212 && value <= 216) {
            return new PatchState(Produce.JUTE, CropState.GROWING, value - 212);
         } else if (value >= 217 && value <= 219) {
            return new PatchState(Produce.WEEDS, CropState.GROWING, 3);
         } else if (value >= 220 && value <= 223) {
            return new PatchState(Produce.JUTE, CropState.DEAD, value - 219);
         } else if (value >= 224 && value <= 226) {
            return new PatchState(Produce.FLAX, CropState.GROWING, value - 224);
         } else if (value >= 227 && value <= 229) {
            return new PatchState(Produce.WEEDS, CropState.GROWING, 3);
         } else if (value >= 230 && value <= 231) {
            return new PatchState(Produce.FLAX, CropState.DEAD, value - 229);
         } else if (value >= 232 && value <= 235) {
            return new PatchState(Produce.HEMP, CropState.GROWING, value - 232);
         } else if (value >= 236 && value <= 238) {
            return new PatchState(Produce.WEEDS, CropState.GROWING, 3);
         } else if (value >= 239 && value <= 240) {
            return new PatchState(Produce.HEMP, CropState.DEAD, value - 238);
         } else if (value == 241) {
            return new PatchState(Produce.HEMP, CropState.DEAD, 3);
         } else if (value >= 242 && value <= 246) {
            return new PatchState(Produce.COTTON, CropState.GROWING, value - 242);
         } else if (value >= 247 && value <= 249) {
            return new PatchState(Produce.WEEDS, CropState.GROWING, 3);
         } else if (value >= 250 && value <= 253) {
            return new PatchState(Produce.COTTON, CropState.DEAD, value - 249);
         } else {
            return value >= 254 && value <= 255 ? new PatchState(Produce.WEEDS, CropState.GROWING, 3) : null;
         }
      }
   },
   TREE(Tab.TREE, "", true) {
      PatchState forVarbitValue(int value) {
         if (value >= 0 && value <= 3) {
            return new PatchState(Produce.WEEDS, CropState.GROWING, 3 - value);
         } else if (value >= 4 && value <= 7) {
            return new PatchState(Produce.WEEDS, CropState.GROWING, 3);
         } else if (value >= 8 && value <= 11) {
            return new PatchState(Produce.OAK, CropState.GROWING, value - 8);
         } else if (value == 12) {
            return new PatchState(Produce.OAK, CropState.GROWING, Produce.OAK.getStages() - 1);
         } else if (value == 13) {
            return new PatchState(Produce.OAK, CropState.HARVESTABLE, 0);
         } else if (value == 14) {
            return new PatchState(Produce.OAK, CropState.HARVESTABLE, 0);
         } else if (value >= 15 && value <= 20) {
            return new PatchState(Produce.WILLOW, CropState.GROWING, value - 15);
         } else if (value == 21) {
            return new PatchState(Produce.WILLOW, CropState.GROWING, Produce.WILLOW.getStages() - 1);
         } else if (value == 22) {
            return new PatchState(Produce.WILLOW, CropState.HARVESTABLE, 0);
         } else if (value == 23) {
            return new PatchState(Produce.WILLOW, CropState.HARVESTABLE, 0);
         } else if (value >= 24 && value <= 31) {
            return new PatchState(Produce.MAPLE, CropState.GROWING, value - 24);
         } else if (value == 32) {
            return new PatchState(Produce.MAPLE, CropState.GROWING, Produce.MAPLE.getStages() - 1);
         } else if (value == 33) {
            return new PatchState(Produce.MAPLE, CropState.HARVESTABLE, 0);
         } else if (value == 34) {
            return new PatchState(Produce.MAPLE, CropState.HARVESTABLE, 0);
         } else if (value >= 35 && value <= 44) {
            return new PatchState(Produce.YEW, CropState.GROWING, value - 35);
         } else if (value == 45) {
            return new PatchState(Produce.YEW, CropState.GROWING, Produce.YEW.getStages() - 1);
         } else if (value == 46) {
            return new PatchState(Produce.YEW, CropState.HARVESTABLE, 0);
         } else if (value == 47) {
            return new PatchState(Produce.YEW, CropState.HARVESTABLE, 0);
         } else if (value >= 48 && value <= 59) {
            return new PatchState(Produce.MAGIC, CropState.GROWING, value - 48);
         } else if (value == 60) {
            return new PatchState(Produce.MAGIC, CropState.GROWING, Produce.MAGIC.getStages() - 1);
         } else if (value == 61) {
            return new PatchState(Produce.MAGIC, CropState.HARVESTABLE, 0);
         } else if (value == 62) {
            return new PatchState(Produce.MAGIC, CropState.HARVESTABLE, 0);
         } else if (value >= 63 && value <= 72) {
            return new PatchState(Produce.WEEDS, CropState.GROWING, 3);
         } else if (value >= 73 && value <= 75) {
            return new PatchState(Produce.OAK, CropState.DISEASED, value - 72);
         } else if (value == 77) {
            return new PatchState(Produce.OAK, CropState.DISEASED, 4);
         } else if (value >= 78 && value <= 79) {
            return new PatchState(Produce.WEEDS, CropState.GROWING, 3);
         } else if (value >= 80 && value <= 84) {
            return new PatchState(Produce.WILLOW, CropState.DISEASED, value - 79);
         } else if (value == 86) {
            return new PatchState(Produce.WILLOW, CropState.DISEASED, 6);
         } else if (value >= 87 && value <= 88) {
            return new PatchState(Produce.WEEDS, CropState.GROWING, 3);
         } else if (value >= 89 && value <= 95) {
            return new PatchState(Produce.MAPLE, CropState.DISEASED, value - 88);
         } else if (value == 97) {
            return new PatchState(Produce.MAPLE, CropState.DISEASED, 8);
         } else if (value >= 98 && value <= 99) {
            return new PatchState(Produce.WEEDS, CropState.GROWING, 3);
         } else if (value >= 100 && value <= 108) {
            return new PatchState(Produce.YEW, CropState.DISEASED, value - 99);
         } else if (value == 110) {
            return new PatchState(Produce.YEW, CropState.DISEASED, 10);
         } else if (value >= 111 && value <= 112) {
            return new PatchState(Produce.WEEDS, CropState.GROWING, 3);
         } else if (value >= 113 && value <= 123) {
            return new PatchState(Produce.MAGIC, CropState.DISEASED, value - 112);
         } else if (value == 125) {
            return new PatchState(Produce.MAGIC, CropState.DISEASED, 12);
         } else if (value >= 126 && value <= 136) {
            return new PatchState(Produce.WEEDS, CropState.GROWING, 3);
         } else if (value >= 137 && value <= 139) {
            return new PatchState(Produce.OAK, CropState.DEAD, value - 136);
         } else if (value == 141) {
            return new PatchState(Produce.OAK, CropState.DEAD, 4);
         } else if (value >= 142 && value <= 143) {
            return new PatchState(Produce.WEEDS, CropState.GROWING, 3);
         } else if (value >= 144 && value <= 148) {
            return new PatchState(Produce.WILLOW, CropState.DEAD, value - 143);
         } else if (value == 150) {
            return new PatchState(Produce.WILLOW, CropState.DEAD, 6);
         } else if (value >= 151 && value <= 152) {
            return new PatchState(Produce.WEEDS, CropState.GROWING, 3);
         } else if (value >= 153 && value <= 159) {
            return new PatchState(Produce.MAPLE, CropState.DEAD, value - 152);
         } else if (value == 161) {
            return new PatchState(Produce.MAPLE, CropState.DEAD, 8);
         } else if (value >= 162 && value <= 163) {
            return new PatchState(Produce.WEEDS, CropState.GROWING, 3);
         } else if (value >= 164 && value <= 172) {
            return new PatchState(Produce.YEW, CropState.DEAD, value - 163);
         } else if (value == 174) {
            return new PatchState(Produce.YEW, CropState.DEAD, 10);
         } else if (value >= 175 && value <= 176) {
            return new PatchState(Produce.WEEDS, CropState.GROWING, 3);
         } else if (value >= 177 && value <= 187) {
            return new PatchState(Produce.MAGIC, CropState.DEAD, value - 176);
         } else if (value == 189) {
            return new PatchState(Produce.MAGIC, CropState.DEAD, 12);
         } else if (value >= 190 && value <= 191) {
            return new PatchState(Produce.WEEDS, CropState.GROWING, 3);
         } else if (value >= 192 && value <= 197) {
            return new PatchState(Produce.WILLOW, CropState.HARVESTABLE, 0);
         } else {
            return value >= 198 && value <= 255 ? new PatchState(Produce.WEEDS, CropState.GROWING, 3) : null;
         }
      }
   },
   HARDWOOD_TREE(Tab.TREE, "Hardwood Trees", true) {
      PatchState forVarbitValue(int value) {
         if (value >= 0 && value <= 3) {
            return new PatchState(Produce.WEEDS, CropState.GROWING, 3 - value);
         } else if (value >= 4 && value <= 7) {
            return new PatchState(Produce.WEEDS, CropState.GROWING, 3);
         } else if (value >= 8 && value <= 14) {
            return new PatchState(Produce.TEAK, CropState.GROWING, value - 8);
         } else if (value == 15) {
            return new PatchState(Produce.TEAK, CropState.GROWING, Produce.TEAK.getStages() - 1);
         } else if (value == 16) {
            return new PatchState(Produce.TEAK, CropState.HARVESTABLE, 0);
         } else if (value == 17) {
            return new PatchState(Produce.TEAK, CropState.HARVESTABLE, 0);
         } else if (value >= 18 && value <= 23) {
            return new PatchState(Produce.TEAK, CropState.DISEASED, value - 17);
         } else if (value >= 24 && value <= 29) {
            return new PatchState(Produce.TEAK, CropState.DEAD, value - 23);
         } else if (value >= 30 && value <= 37) {
            return new PatchState(Produce.MAHOGANY, CropState.GROWING, value - 30);
         } else if (value == 38) {
            return new PatchState(Produce.MAHOGANY, CropState.GROWING, Produce.MAHOGANY.getStages() - 1);
         } else if (value == 39) {
            return new PatchState(Produce.MAHOGANY, CropState.HARVESTABLE, 0);
         } else if (value == 40) {
            return new PatchState(Produce.MAHOGANY, CropState.HARVESTABLE, 0);
         } else if (value >= 41 && value <= 47) {
            return new PatchState(Produce.MAHOGANY, CropState.DISEASED, value - 40);
         } else if (value >= 48 && value <= 54) {
            return new PatchState(Produce.MAHOGANY, CropState.DEAD, value - 47);
         } else if (value >= 55 && value <= 62) {
            return new PatchState(Produce.CAMPHOR, CropState.GROWING, value - 55);
         } else if (value == 63) {
            return new PatchState(Produce.CAMPHOR, CropState.GROWING, Produce.CAMPHOR.getStages() - 1);
         } else if (value == 64) {
            return new PatchState(Produce.CAMPHOR, CropState.HARVESTABLE, 0);
         } else if (value == 65) {
            return new PatchState(Produce.CAMPHOR, CropState.HARVESTABLE, 0);
         } else if (value >= 66 && value <= 72) {
            return new PatchState(Produce.CAMPHOR, CropState.DISEASED, value - 65);
         } else if (value >= 73 && value <= 79) {
            return new PatchState(Produce.CAMPHOR, CropState.DEAD, value - 72);
         } else if (value >= 80 && value <= 87) {
            return new PatchState(Produce.IRONWOOD, CropState.GROWING, value - 80);
         } else if (value == 88) {
            return new PatchState(Produce.IRONWOOD, CropState.GROWING, Produce.IRONWOOD.getStages() - 1);
         } else if (value == 89) {
            return new PatchState(Produce.IRONWOOD, CropState.HARVESTABLE, 0);
         } else if (value == 90) {
            return new PatchState(Produce.IRONWOOD, CropState.HARVESTABLE, 0);
         } else if (value >= 91 && value <= 97) {
            return new PatchState(Produce.IRONWOOD, CropState.DISEASED, value - 90);
         } else if (value >= 98 && value <= 104) {
            return new PatchState(Produce.IRONWOOD, CropState.DEAD, value - 97);
         } else if (value >= 105 && value <= 113) {
            return new PatchState(Produce.ROSEWOOD, CropState.GROWING, value - 105);
         } else if (value == 114) {
            return new PatchState(Produce.ROSEWOOD, CropState.GROWING, Produce.ROSEWOOD.getStages() - 1);
         } else if (value == 115) {
            return new PatchState(Produce.ROSEWOOD, CropState.HARVESTABLE, 0);
         } else if (value == 116) {
            return new PatchState(Produce.ROSEWOOD, CropState.HARVESTABLE, 0);
         } else if (value >= 117 && value <= 124) {
            return new PatchState(Produce.ROSEWOOD, CropState.DISEASED, value - 116);
         } else if (value >= 125 && value <= 132) {
            return new PatchState(Produce.ROSEWOOD, CropState.DEAD, value - 124);
         } else {
            return value >= 133 && value <= 255 ? new PatchState(Produce.WEEDS, CropState.GROWING, 3) : null;
         }
      }
   },
   REDWOOD(Tab.TREE, "Redwood Trees", true) {
      PatchState forVarbitValue(int value) {
         if (value >= 0 && value <= 3) {
            return new PatchState(Produce.WEEDS, CropState.GROWING, 3 - value);
         } else if (value >= 4 && value <= 7) {
            return new PatchState(Produce.WEEDS, CropState.GROWING, 3);
         } else if (value >= 8 && value <= 17) {
            return new PatchState(Produce.REDWOOD, CropState.GROWING, value - 8);
         } else if (value == 18) {
            return new PatchState(Produce.REDWOOD, CropState.HARVESTABLE, 0);
         } else if (value >= 19 && value <= 27) {
            return new PatchState(Produce.REDWOOD, CropState.DISEASED, value - 18);
         } else if (value >= 28 && value <= 36) {
            return new PatchState(Produce.REDWOOD, CropState.DEAD, value - 27);
         } else if (value == 37) {
            return new PatchState(Produce.REDWOOD, CropState.GROWING, Produce.REDWOOD.getStages() - 1);
         } else {
            return value >= 41 && value <= 55 ? new PatchState(Produce.REDWOOD, CropState.HARVESTABLE, 0) : null;
         }
      }
   },
   SPIRIT_TREE(Tab.TREE, "Spirit Trees", true) {
      PatchState forVarbitValue(int value) {
         if (value >= 0 && value <= 3) {
            return new PatchState(Produce.WEEDS, CropState.GROWING, 3 - value);
         } else if (value >= 4 && value <= 7) {
            return new PatchState(Produce.WEEDS, CropState.GROWING, 3);
         } else if (value >= 8 && value <= 19) {
            return new PatchState(Produce.SPIRIT_TREE, CropState.GROWING, value - 8);
         } else if (value == 20) {
            return new PatchState(Produce.SPIRIT_TREE, CropState.GROWING, 12);
         } else if (value >= 21 && value <= 31) {
            return new PatchState(Produce.SPIRIT_TREE, CropState.DISEASED, value - 20);
         } else if (value >= 32 && value <= 43) {
            return new PatchState(Produce.SPIRIT_TREE, CropState.DEAD, value - 31);
         } else if (value == 44) {
            return new PatchState(Produce.SPIRIT_TREE, CropState.GROWING, Produce.SPIRIT_TREE.getStages() - 1);
         } else {
            return value >= 45 && value <= 63 ? new PatchState(Produce.WEEDS, CropState.GROWING, 3) : null;
         }
      }
   },
   ANIMA(Tab.SPECIAL, "", false) {
      PatchState forVarbitValue(int value) {
         if (value >= 0 && value <= 3) {
            return new PatchState(Produce.WEEDS, CropState.GROWING, 3 - value);
         } else if (value >= 4 && value <= 7) {
            return new PatchState(Produce.WEEDS, CropState.GROWING, 3);
         } else if (value >= 8 && value <= 16) {
            return new PatchState(Produce.ATTAS, CropState.GROWING, value - 8);
         } else if (value >= 17 && value <= 25) {
            return new PatchState(Produce.IASOR, CropState.GROWING, value - 17);
         } else if (value >= 26 && value <= 34) {
            return new PatchState(Produce.KRONOS, CropState.GROWING, value - 26);
         } else {
            return value >= 35 && value <= 255 ? new PatchState(Produce.WEEDS, CropState.GROWING, 3) : null;
         }
      }
   },
   BELLADONNA(Tab.SPECIAL, "Belladonna", false) {
      PatchState forVarbitValue(int value) {
         if (value >= 0 && value <= 3) {
            return new PatchState(Produce.WEEDS, CropState.GROWING, 3 - value);
         } else if (value >= 4 && value <= 7) {
            return new PatchState(Produce.BELLADONNA, CropState.GROWING, value - 4);
         } else if (value == 8) {
            return new PatchState(Produce.BELLADONNA, CropState.HARVESTABLE, 0);
         } else if (value >= 9 && value <= 11) {
            return new PatchState(Produce.BELLADONNA, CropState.DISEASED, value - 8);
         } else if (value >= 12 && value <= 14) {
            return new PatchState(Produce.BELLADONNA, CropState.DEAD, value - 11);
         } else {
            return value >= 15 && value <= 255 ? new PatchState(Produce.WEEDS, CropState.GROWING, 3) : null;
         }
      }
   },
   CACTUS(Tab.SPECIAL, "Cactus", true) {
      PatchState forVarbitValue(int value) {
         if (value >= 0 && value <= 3) {
            return new PatchState(Produce.WEEDS, CropState.GROWING, 3 - value);
         } else if (value >= 4 && value <= 7) {
            return new PatchState(Produce.WEEDS, CropState.GROWING, 3);
         } else if (value >= 8 && value <= 14) {
            return new PatchState(Produce.CACTUS, CropState.GROWING, value - 8);
         } else if (value >= 15 && value <= 18) {
            return new PatchState(Produce.CACTUS, CropState.HARVESTABLE, value - 15);
         } else if (value >= 19 && value <= 24) {
            return new PatchState(Produce.CACTUS, CropState.DISEASED, value - 18);
         } else if (value >= 25 && value <= 30) {
            return new PatchState(Produce.CACTUS, CropState.DEAD, value - 24);
         } else if (value == 31) {
            return new PatchState(Produce.CACTUS, CropState.GROWING, Produce.CACTUS.getStages() - 1);
         } else if (value >= 32 && value <= 38) {
            return new PatchState(Produce.POTATO_CACTUS, CropState.GROWING, value - 32);
         } else if (value >= 39 && value <= 45) {
            return new PatchState(Produce.POTATO_CACTUS, CropState.HARVESTABLE, value - 39);
         } else if (value >= 46 && value <= 51) {
            return new PatchState(Produce.POTATO_CACTUS, CropState.DISEASED, value - 45);
         } else if (value >= 52 && value <= 57) {
            return new PatchState(Produce.POTATO_CACTUS, CropState.DEAD, value - 51);
         } else if (value == 58) {
            return new PatchState(Produce.POTATO_CACTUS, CropState.GROWING, Produce.POTATO_CACTUS.getStages() - 1);
         } else {
            return value >= 59 && value <= 255 ? new PatchState(Produce.WEEDS, CropState.GROWING, 3) : null;
         }
      }
   },
   CORAL(Tab.SPECIAL, "Coral", false) {
      PatchState forVarbitValue(int value) {
         if (value >= 0 && value <= 3) {
            return new PatchState(Produce.WEEDS, CropState.GROWING, 3);
         } else if (value >= 4 && value <= 7) {
            return new PatchState(Produce.ELKHORN_CORAL, CropState.GROWING, value - 4);
         } else if (value == 8) {
            return new PatchState(Produce.ELKHORN_CORAL, CropState.GROWING, 4);
         } else if (value >= 9 && value <= 11) {
            return new PatchState(Produce.ELKHORN_CORAL, CropState.DISEASED, value - 8);
         } else if (value >= 12 && value <= 14) {
            return new PatchState(Produce.ELKHORN_CORAL, CropState.DEAD, value - 11);
         } else if (value >= 15 && value <= 18) {
            return new PatchState(Produce.PILLAR_CORAL, CropState.GROWING, value - 15);
         } else if (value == 19) {
            return new PatchState(Produce.PILLAR_CORAL, CropState.GROWING, 4);
         } else if (value >= 20 && value <= 22) {
            return new PatchState(Produce.PILLAR_CORAL, CropState.DISEASED, value - 19);
         } else if (value >= 23 && value <= 25) {
            return new PatchState(Produce.PILLAR_CORAL, CropState.DEAD, value - 22);
         } else if (value >= 26 && value <= 29) {
            return new PatchState(Produce.UMBRAL_CORAL, CropState.GROWING, value - 26);
         } else if (value == 30) {
            return new PatchState(Produce.UMBRAL_CORAL, CropState.GROWING, 4);
         } else if (value >= 31 && value <= 33) {
            return new PatchState(Produce.UMBRAL_CORAL, CropState.DISEASED, value - 30);
         } else if (value >= 34 && value <= 36) {
            return new PatchState(Produce.UMBRAL_CORAL, CropState.DEAD, value - 33);
         } else {
            return value >= 37 && value <= 255 ? new PatchState(Produce.WEEDS, CropState.GROWING, 0) : null;
         }
      }
   },
   SEAWEED(Tab.SPECIAL, "Seaweed", false) {
      PatchState forVarbitValue(int value) {
         if (value >= 0 && value <= 3) {
            return new PatchState(Produce.WEEDS, CropState.GROWING, 3 - value);
         } else if (value >= 4 && value <= 7) {
            return new PatchState(Produce.SEAWEED, CropState.GROWING, value - 4);
         } else if (value >= 8 && value <= 10) {
            return new PatchState(Produce.SEAWEED, CropState.HARVESTABLE, value - 8);
         } else if (value >= 11 && value <= 13) {
            return new PatchState(Produce.SEAWEED, CropState.DISEASED, value - 10);
         } else if (value >= 14 && value <= 16) {
            return new PatchState(Produce.SEAWEED, CropState.DEAD, value - 13);
         } else {
            return value >= 17 && value <= 255 ? new PatchState(Produce.WEEDS, CropState.GROWING, 3) : null;
         }
      }
   },
   CALQUAT(Tab.FRUIT_TREE, "Calquat", true) {
      PatchState forVarbitValue(int value) {
         if (value >= 0 && value <= 3) {
            return new PatchState(Produce.WEEDS, CropState.GROWING, 3 - value);
         } else if (value >= 4 && value <= 11) {
            return new PatchState(Produce.CALQUAT, CropState.GROWING, value - 4);
         } else if (value >= 12 && value <= 18) {
            return new PatchState(Produce.CALQUAT, CropState.HARVESTABLE, value - 12);
         } else if (value >= 19 && value <= 25) {
            return new PatchState(Produce.CALQUAT, CropState.DISEASED, value - 18);
         } else if (value >= 26 && value <= 33) {
            return new PatchState(Produce.CALQUAT, CropState.DEAD, value - 25);
         } else if (value == 34) {
            return new PatchState(Produce.CALQUAT, CropState.GROWING, Produce.CALQUAT.getStages() - 1);
         } else {
            return value >= 35 && value <= 255 ? new PatchState(Produce.WEEDS, CropState.GROWING, 3) : null;
         }
      }
   },
   CELASTRUS(Tab.FRUIT_TREE, "Celastrus", true) {
      PatchState forVarbitValue(int value) {
         if (value >= 0 && value <= 3) {
            return new PatchState(Produce.WEEDS, CropState.GROWING, 3 - value);
         } else if (value >= 4 && value <= 7) {
            return new PatchState(Produce.WEEDS, CropState.GROWING, 3);
         } else if (value >= 8 && value <= 12) {
            return new PatchState(Produce.CELASTRUS, CropState.GROWING, value - 8);
         } else if (value == 13) {
            return new PatchState(Produce.CELASTRUS, CropState.GROWING, Produce.CELASTRUS.getStages() - 1);
         } else if (value >= 14 && value <= 16) {
            return new PatchState(Produce.CELASTRUS, CropState.HARVESTABLE, value - 14);
         } else if (value == 17) {
            return new PatchState(Produce.CELASTRUS, CropState.HARVESTABLE, 0);
         } else if (value >= 18 && value <= 22) {
            return new PatchState(Produce.CELASTRUS, CropState.DISEASED, value - 17);
         } else if (value >= 23 && value <= 27) {
            return new PatchState(Produce.CELASTRUS, CropState.DEAD, value - 22);
         } else if (value == 28) {
            return new PatchState(Produce.CELASTRUS, CropState.HARVESTABLE, 0);
         } else {
            return value >= 29 && value <= 255 ? new PatchState(Produce.WEEDS, CropState.GROWING, 3) : null;
         }
      }
   },
   GRAPES(Tab.GRAPE, "", true) {
      PatchState forVarbitValue(int value) {
         if (value >= 0 && value <= 1) {
            return new PatchState(Produce.WEEDS, CropState.GROWING, 3);
         } else if (value >= 2 && value <= 9) {
            return new PatchState(Produce.GRAPE, CropState.GROWING, value - 2);
         } else if (value == 10) {
            return new PatchState(Produce.GRAPE, CropState.GROWING, 7);
         } else {
            return value >= 11 && value <= 15 ? new PatchState(Produce.GRAPE, CropState.HARVESTABLE, value - 11) : null;
         }
      }
   },
   CRYSTAL_TREE(Tab.FRUIT_TREE, "Crystal Tree", true) {
      PatchState forVarbitValue(int value) {
         if (value >= 0 && value <= 3) {
            return new PatchState(Produce.WEEDS, CropState.GROWING, 3 - value);
         } else if (value >= 8 && value <= 13) {
            return new PatchState(Produce.CRYSTAL_TREE, CropState.GROWING, value - 8);
         } else if (value == 14) {
            return new PatchState(Produce.CRYSTAL_TREE, CropState.GROWING, Produce.CRYSTAL_TREE.getStages() - 1);
         } else {
            return value == 15 ? new PatchState(Produce.CRYSTAL_TREE, CropState.HARVESTABLE, 0) : null;
         }
      }
   },
   COMPOST(Tab.SPECIAL, "Compost Bin", true) {
      PatchState forVarbitValue(int value) {
         if (value == 0) {
            return new PatchState(Produce.EMPTY_COMPOST_BIN, CropState.EMPTY, 0);
         } else if (value >= 1 && value <= 15) {
            return new PatchState(Produce.COMPOST, CropState.FILLING, value - 1);
         } else if (value >= 16 && value <= 30) {
            return new PatchState(Produce.COMPOST, CropState.HARVESTABLE, value - 16);
         } else if (value != 31 && value != 32) {
            if (value >= 33 && value <= 47) {
               return new PatchState(Produce.SUPERCOMPOST, CropState.FILLING, value - 33);
            } else if (value >= 48 && value <= 62) {
               return new PatchState(Produce.SUPERCOMPOST, CropState.HARVESTABLE, value - 48);
            } else if (value == 94) {
               return new PatchState(Produce.COMPOST, CropState.GROWING, Produce.COMPOST.getStages() - 1);
            } else if (value != 95 && value != 96) {
               if (value == 126) {
                  return new PatchState(Produce.SUPERCOMPOST, CropState.GROWING, Produce.SUPERCOMPOST.getStages() - 1);
               } else if (value >= 129 && value <= 143) {
                  return new PatchState(Produce.ROTTEN_TOMATO, CropState.FILLING, value - 129);
               } else if (value >= 144 && value <= 158) {
                  return new PatchState(Produce.ROTTEN_TOMATO, CropState.HARVESTABLE, value - 144);
               } else if (value >= 159 && value <= 160) {
                  return new PatchState(Produce.ROTTEN_TOMATO, CropState.GROWING, value - 159);
               } else {
                  return value >= 176 && value <= 190 ? new PatchState(Produce.ULTRACOMPOST, CropState.HARVESTABLE, value - 176) : null;
               }
            } else {
               return new PatchState(Produce.SUPERCOMPOST, CropState.GROWING, value - 95);
            }
         } else {
            return new PatchState(Produce.COMPOST, CropState.GROWING, value - 31);
         }
      }
   },
   BIG_COMPOST(Tab.SPECIAL, "Big Compost Bin", true) {
      PatchState forVarbitValue(int value) {
         if (value == 0) {
            return new PatchState(Produce.EMPTY_BIG_COMPOST_BIN, CropState.EMPTY, 0);
         } else if (value >= 1 && value <= 15) {
            return new PatchState(Produce.BIG_COMPOST, CropState.FILLING, value - 1);
         } else if (value >= 16 && value <= 30) {
            return new PatchState(Produce.BIG_COMPOST, CropState.HARVESTABLE, value - 16);
         } else if (value >= 33 && value <= 47) {
            return new PatchState(Produce.BIG_SUPERCOMPOST, CropState.FILLING, value - 33);
         } else if (value >= 48 && value <= 62) {
            return new PatchState(Produce.BIG_SUPERCOMPOST, CropState.HARVESTABLE, value - 48);
         } else if (value >= 63 && value <= 77) {
            return new PatchState(Produce.BIG_COMPOST, CropState.FILLING, 15 + value - 63);
         } else if (value >= 78 && value <= 92) {
            return new PatchState(Produce.BIG_COMPOST, CropState.HARVESTABLE, 15 + value - 78);
         } else if (value == 93) {
            return new PatchState(Produce.BIG_COMPOST, CropState.GROWING, Produce.BIG_COMPOST.getStages() - 1);
         } else if (value >= 97 && value <= 99) {
            return new PatchState(Produce.BIG_SUPERCOMPOST, CropState.GROWING, value - 97);
         } else if (value >= 100 && value <= 114) {
            return new PatchState(Produce.BIG_SUPERCOMPOST, CropState.HARVESTABLE, 15 + value - 100);
         } else if (value >= 127 && value <= 128) {
            return new PatchState(Produce.BIG_COMPOST, CropState.GROWING, value - 127);
         } else if (value >= 129 && value <= 143) {
            return new PatchState(Produce.BIG_ROTTEN_TOMATO, CropState.FILLING, value - 129);
         } else if (value >= 144 && value <= 158) {
            return new PatchState(Produce.BIG_ROTTEN_TOMATO, CropState.HARVESTABLE, value - 144);
         } else if (value >= 159 && value <= 160) {
            return new PatchState(Produce.BIG_ROTTEN_TOMATO, CropState.GROWING, value - 159);
         } else if (value >= 161 && value <= 175) {
            return new PatchState(Produce.BIG_SUPERCOMPOST, CropState.FILLING, 15 + value - 161);
         } else if (value >= 176 && value <= 205) {
            return new PatchState(Produce.BIG_ULTRACOMPOST, CropState.HARVESTABLE, value - 176);
         } else if (value >= 207 && value <= 221) {
            return new PatchState(Produce.BIG_ROTTEN_TOMATO, CropState.HARVESTABLE, 15 + value - 207);
         } else if (value == 222) {
            return new PatchState(Produce.BIG_ROTTEN_TOMATO, CropState.GROWING, Produce.BIG_ROTTEN_TOMATO.getStages() - 1);
         } else {
            return value >= 223 && value <= 237 ? new PatchState(Produce.BIG_ROTTEN_TOMATO, CropState.FILLING, 15 + value - 223) : null;
         }
      }
   };

   private final Tab tab;
   private final String name;
   private final boolean healthCheckRequired;

   @Nullable
   abstract PatchState forVarbitValue(int var1);

   private PatchImplementation(Tab tab, String name, boolean healthCheckRequired) {
      this.tab = tab;
      this.name = name;
      this.healthCheckRequired = healthCheckRequired;
   }

   public Tab getTab() {
      return this.tab;
   }

   public String getName() {
      return this.name;
   }

   public boolean isHealthCheckRequired() {
      return this.healthCheckRequired;
   }
}
