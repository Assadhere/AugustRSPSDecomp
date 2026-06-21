package net.runelite.client.plugins.particles.data;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import net.runelite.api.Client;
import net.runelite.client.plugins.particles.config.ParticleEffectorConfig;
import net.runelite.client.plugins.particles.config.ParticleEmitterConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ParticleDBLoader {
   private static final Logger log = LoggerFactory.getLogger(ParticleDBLoader.class);

   public static List<ParticleEmitterConfig> loadEmitterConfigs(Client client) {
      List<ParticleEmitterConfig> configs = new ArrayList();
      List<Integer> rows = client.getDBTableRows(65426);
      if (rows != null && !rows.isEmpty()) {
         log.info("Loading {} particle emitter configs from cache", rows.size());
         Iterator var3 = rows.iterator();

         while(var3.hasNext()) {
            int rowId = (Integer)var3.next();

            try {
               configs.add(parseEmitterRow(client, rowId));
            } catch (Exception var6) {
               Exception e = var6;
               log.warn("Failed to parse emitter row {}", rowId, e);
            }
         }

         log.info("Loaded {} particle emitter configs", configs.size());
         return configs;
      } else {
         log.warn("No particle emitter rows found in cache");
         return configs;
      }
   }

   public static List<ParticleEffectorConfig> loadEffectorConfigs(Client client) {
      List<ParticleEffectorConfig> configs = new ArrayList();
      List<Integer> rows = client.getDBTableRows(65425);
      if (rows != null && !rows.isEmpty()) {
         log.info("Loading {} particle effector configs from cache", rows.size());
         Iterator var3 = rows.iterator();

         while(var3.hasNext()) {
            int rowId = (Integer)var3.next();

            try {
               configs.add(parseEffectorRow(client, rowId));
            } catch (Exception var6) {
               Exception e = var6;
               log.warn("Failed to parse effector row {}", rowId, e);
            }
         }

         log.info("Loaded {} particle effector configs", configs.size());
         return configs;
      } else {
         log.warn("No particle effector rows found in cache");
         return configs;
      }
   }

   private static ParticleEffectorConfig parseEffectorRow(Client client, int rowId) {
      int id = getInt(client, rowId, 0, 0);
      ParticleEffectorConfig config = new ParticleEffectorConfig(id);
      Object[] nameField = client.getDBTableField(rowId, 1, 0);
      if (nameField != null && nameField.length > 0 && nameField[0] != null) {
         config.setName((String)nameField[0]);
      }

      config.setSpreadAngle(getInt(client, rowId, 2, 0));
      config.setForceDirectionX(getInt(client, rowId, 3, 0));
      config.setForceDirectionY(getInt(client, rowId, 3, 1));
      config.setForceDirectionZ(getInt(client, rowId, 3, 2));
      config.setFalloffType(getInt(client, rowId, 4, 0));
      config.setFalloffRate(getInt(client, rowId, 4, 1));
      config.setScope(getInt(client, rowId, 5, 0));
      config.setDirectPositionMode(getBool(client, rowId, 6));
      config.setRadialForceMode(getBool(client, rowId, 7));
      config.setInvertDirection(getBool(client, rowId, 8));
      config.postDecode();
      return config;
   }

   public static WhitelistData loadWhitelist(Client client) {
      List<Integer> rows = client.getDBTableRows(65424);
      if (rows != null && !rows.isEmpty()) {
         Set<Integer> locs = new HashSet();
         Set<Integer> objs = new HashSet();
         Set<Integer> npcs = new HashSet();
         Set<Integer> spotanims = new HashSet();
         Set<Integer> kits = new HashSet();
         Iterator var7 = rows.iterator();

         while(var7.hasNext()) {
            int rowId = (Integer)var7.next();
            addAllToSet(locs, getIntArray(client, rowId, 0));
            addAllToSet(objs, getIntArray(client, rowId, 1));
            addAllToSet(npcs, getIntArray(client, rowId, 2));
            addAllToSet(spotanims, getIntArray(client, rowId, 3));
            addAllToSet(kits, getIntArray(client, rowId, 4));
         }

         log.info("Loaded particle whitelist from {} rows: {} locs, {} objs, {} npcs, {} spotanims, {} kits", new Object[]{rows.size(), locs.size(), objs.size(), npcs.size(), spotanims.size(), kits.size()});
         return new WhitelistData(locs, objs, npcs, spotanims, kits);
      } else {
         log.warn("No particle whitelist rows found in cache");
         return null;
      }
   }

   private static void addAllToSet(Set<Integer> set, int[] array) {
      if (array != null) {
         int[] var2 = array;
         int var3 = array.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            int value = var2[var4];
            set.add(value);
         }

      }
   }

   private static ParticleEmitterConfig parseEmitterRow(Client client, int rowId) {
      int id = getInt(client, rowId, 0, 0);
      ParticleEmitterConfig config = new ParticleEmitterConfig(id);
      Object[] nameField = client.getDBTableField(rowId, 1, 0);
      if (nameField != null && nameField.length > 0 && nameField[0] != null) {
         config.setName((String)nameField[0]);
      }

      config.setSpreadYawMin(getInt(client, rowId, 2, 0));
      config.setSpreadYawMax(getInt(client, rowId, 2, 1));
      config.setSpreadPitchMin(getInt(client, rowId, 3, 0));
      config.setSpreadPitchMax(getInt(client, rowId, 3, 1));
      config.setMinSpeed(getInt(client, rowId, 4, 0));
      config.setMaxSpeed(getInt(client, rowId, 4, 1));
      config.setTargetSpeed(getInt(client, rowId, 5, 0, -1));
      config.setSpeedTransitionPercent(getInt(client, rowId, 6, 0, 100));
      config.setDistanceFalloffType(getInt(client, rowId, 7, 0));
      config.setDistanceFalloffStrength(getInt(client, rowId, 7, 1));
      config.setMinScale(getInt(client, rowId, 8, 0));
      config.setMaxScale(getInt(client, rowId, 8, 1));
      config.setTargetScale(getInt(client, rowId, 9, 0, -1));
      config.setScaleTransitionPercent(getInt(client, rowId, 10, 0, 100));
      config.setMinRotation(getInt(client, rowId, 11, 0));
      config.setMaxRotation(getInt(client, rowId, 11, 1));
      config.setTargetRotation(getInt(client, rowId, 12, 0));
      config.setRotationTransitionPercent(getInt(client, rowId, 13, 0, 100));
      config.setInitialSpawnCount(getInt(client, rowId, 14, 0));
      config.setMinSpawnCount(getInt(client, rowId, 15, 0));
      config.setMaxSpawnCount(getInt(client, rowId, 15, 1));
      config.setMinLifetime(getInt(client, rowId, 16, 0));
      config.setMaxLifetime(getInt(client, rowId, 16, 1));
      config.setEmitOnlyBeforeTime(getBool(client, rowId, 17));
      config.setLoopEmission(getBool(client, rowId, 18));
      config.setEmissionTimeThreshold(getInt(client, rowId, 19, 0));
      config.setEmissionCycleDuration(getInt(client, rowId, 20, 0, -1));
      config.setMinColourArgb(getInt(client, rowId, 21, 0));
      config.setMaxColourArgb(getInt(client, rowId, 21, 1));
      config.setTargetColourArgb(getInt(client, rowId, 22, 0));
      config.setColourTransitionPercent(getInt(client, rowId, 23, 0, 100));
      config.setAlphaTransitionPercent(getInt(client, rowId, 24, 0, 100));
      config.setUniformColourVariation(getBool(client, rowId, 25, true));
      config.setLocalEffectorFilter(getIntArray(client, rowId, 26));
      config.setEmbeddedEffectors(getIntArray(client, rowId, 27));
      config.setGlobalEffectors(getIntArray(client, rowId, 28));
      int[] levelBounds = getIntPairArray(client, rowId, 29);
      if (levelBounds != null && levelBounds.length >= 2) {
         config.setLowerBoundLevel(levelBounds[0]);
         config.setUpperBoundLevel(levelBounds[1]);
      }

      config.setCollidesWithObjects(getBool(client, rowId, 30));
      config.setClipToTerrain(getBool(client, rowId, 31, true));
      config.setLocalSpace(getBool(client, rowId, 33));
      int textureId = getInt(client, rowId, 32, 0, -1);
      if (textureId >= 0) {
         config.setTexture(String.valueOf(textureId));
      }

      config.normalize();
      config.postDecode();
      return config;
   }

   private static int getInt(Client client, int rowId, int column, int tupleIndex) {
      return getInt(client, rowId, column, tupleIndex, 0);
   }

   private static int getInt(Client client, int rowId, int column, int tupleIndex, int defaultValue) {
      try {
         Object[] field = client.getDBTableField(rowId, column, tupleIndex);
         if (field != null && field.length > 0 && field[0] != null) {
            return (Integer)field[0];
         }
      } catch (NullPointerException var6) {
      }

      return defaultValue;
   }

   private static boolean getBool(Client client, int rowId, int column) {
      return getBool(client, rowId, column, false);
   }

   private static boolean getBool(Client client, int rowId, int column, boolean defaultValue) {
      try {
         Object[] field = client.getDBTableField(rowId, column, 0);
         if (field != null && field.length > 0 && field[0] != null) {
            return (Integer)field[0] == 1;
         }
      } catch (NullPointerException var5) {
      }

      return defaultValue;
   }

   private static int[] getIntArray(Client client, int rowId, int column) {
      Object[] field;
      try {
         field = client.getDBTableField(rowId, column, 0);
      } catch (NullPointerException var6) {
         return null;
      }

      if (field != null && field.length != 0) {
         int[] result = new int[field.length];

         for(int i = 0; i < field.length; ++i) {
            if (field[i] != null) {
               result[i] = (Integer)field[i];
            }
         }

         return result;
      } else {
         return null;
      }
   }

   private static int[] getIntPairArray(Client client, int rowId, int column) {
      Object[] first;
      Object[] second;
      try {
         first = client.getDBTableField(rowId, column, 0);
         second = client.getDBTableField(rowId, column, 1);
      } catch (NullPointerException var8) {
         return null;
      }

      if (first != null && first.length != 0) {
         int count = first.length;
         int[] result = new int[count * 2];

         for(int i = 0; i < count; ++i) {
            result[i * 2] = first[i] != null ? (Integer)first[i] : 0;
            result[i * 2 + 1] = second != null && i < second.length && second[i] != null ? (Integer)second[i] : 0;
         }

         return result;
      } else {
         return null;
      }
   }

   public static class WhitelistData {
      public final Set<Integer> locs;
      public final Set<Integer> objs;
      public final Set<Integer> npcs;
      public final Set<Integer> spotanims;
      public final Set<Integer> kits;

      public WhitelistData(Set<Integer> locs, Set<Integer> objs, Set<Integer> npcs, Set<Integer> spotanims, Set<Integer> kits) {
         this.locs = locs;
         this.objs = objs;
         this.npcs = npcs;
         this.spotanims = spotanims;
         this.kits = kits;
      }
   }
}
