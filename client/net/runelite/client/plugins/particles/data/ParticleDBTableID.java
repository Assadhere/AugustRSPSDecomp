package net.runelite.client.plugins.particles.data;

public final class ParticleDBTableID {
   public static final class ParticleWhitelist {
      public static final int ID = 65424;
      public static final int COL_LOCS = 0;
      public static final int COL_OBJS = 1;
      public static final int COL_NPCS = 2;
      public static final int COL_SPOTANIMS = 3;
      public static final int COL_IDENTKITS = 4;
   }

   public static final class ParticleEffector {
      public static final int ID = 65425;
      public static final int COL_ID = 0;
      public static final int COL_NAME = 1;
      public static final int COL_SPREAD_ANGLE = 2;
      public static final int COL_FORCE_DIRECTION = 3;
      public static final int COL_FALLOFF = 4;
      public static final int COL_SCOPE = 5;
      public static final int COL_DIRECT_POSITION_MODE = 6;
      public static final int COL_RADIAL_FORCE_MODE = 7;
      public static final int COL_INVERT_DIRECTION = 8;
   }

   public static final class ParticleEmitter {
      public static final int ID = 65426;
      public static final int COL_ID = 0;
      public static final int COL_NAME = 1;
      public static final int COL_SPREAD_YAW = 2;
      public static final int COL_SPREAD_PITCH = 3;
      public static final int COL_SPEED = 4;
      public static final int COL_TARGET_SPEED = 5;
      public static final int COL_SPEED_TRANSITION_PERCENT = 6;
      public static final int COL_DISTANCE_FALLOFF = 7;
      public static final int COL_SCALE = 8;
      public static final int COL_TARGET_SCALE = 9;
      public static final int COL_SCALE_TRANSITION_PERCENT = 10;
      public static final int COL_ROTATION = 11;
      public static final int COL_TARGET_ROTATION = 12;
      public static final int COL_ROTATION_TRANSITION_PERCENT = 13;
      public static final int COL_INITIAL_SPAWN_COUNT = 14;
      public static final int COL_SPAWN_COUNT = 15;
      public static final int COL_LIFETIME = 16;
      public static final int COL_EMIT_ONLY_BEFORE_TIME = 17;
      public static final int COL_LOOP_EMISSION = 18;
      public static final int COL_EMISSION_TIME_THRESHOLD = 19;
      public static final int COL_EMISSION_CYCLE_DURATION = 20;
      public static final int COL_SPAWN_COLOUR = 21;
      public static final int COL_TARGET_COLOUR = 22;
      public static final int COL_COLOUR_TRANSITION_PERCENT = 23;
      public static final int COL_ALPHA_TRANSITION_PERCENT = 24;
      public static final int COL_UNIFORM_COLOUR_VARIATION = 25;
      public static final int COL_LOCAL_EFFECTORS = 26;
      public static final int COL_EMBEDDED_EFFECTORS = 27;
      public static final int COL_GLOBAL_EFFECTORS = 28;
      public static final int COL_LEVEL_BOUND = 29;
      public static final int COL_LOC_COLLISION = 30;
      public static final int COL_TERRAIN_COLLISION = 31;
      public static final int COL_TEXTURE = 32;
      public static final int COL_LOCAL_SPACE = 33;
   }
}
