package net.runelite.client.hiscore;

class HiscoreResponse {
   Skill[] skills;
   Activity[] activities;

   static class Activity {
      String name;
      int rank;
      long score;
   }

   static class Skill {
      String name;
      int rank;
      int level;
      long xp;
   }
}
