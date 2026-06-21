package net.runelite.client.config;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.MoreObjects;
import com.google.common.base.Strings;
import com.google.common.collect.ComparisonChain;
import com.google.gson.Gson;
import com.google.inject.Module;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import lombok.NonNull;
import net.runelite.api.Client;
import net.runelite.api.Player;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.AccountHashChanged;
import net.runelite.api.events.PlayerChanged;
import net.runelite.api.events.WorldChanged;
import net.runelite.client.RuneLite;
import net.runelite.client.account.AccountSession;
import net.runelite.client.account.SessionManager;
import net.runelite.client.eventbus.EventBus;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ClientShutdown;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.events.ConfigSync;
import net.runelite.client.events.ProfileChanged;
import net.runelite.client.events.RuneScapeProfileChanged;
import net.runelite.client.events.SessionClose;
import net.runelite.client.events.SessionOpen;
import net.runelite.client.util.ColorUtil;
import net.runelite.client.util.RunnableExceptionLogger;
import net.runelite.http.api.config.ConfigPatch;
import net.runelite.http.api.config.ConfigPatchResult;
import net.runelite.http.api.config.Configuration;
import net.runelite.http.api.config.Profile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
public class ConfigManager {
   private static final Logger log = LoggerFactory.getLogger(ConfigManager.class);
   public static final String RSPROFILE_GROUP = "rsprofile";
   public static final String RSPROFILE_DISPLAY_NAME = "displayName";
   public static final String RSPROFILE_TYPE = "type";
   private static final String RSPROFILE_ACCOUNT_HASH = "accountHash";
   private static final long RSPROFILE_ID = -1L;
   private static final String RSPROFILE_NAME = "$rsprofile";
   private static final int KEY_SPLITTER_GROUP = 0;
   private static final int KEY_SPLITTER_PROFILE = 1;
   private static final int KEY_SPLITTER_KEY = 2;
   @Nullable
   private final String configProfileName;
   private final ScheduledExecutorService executor;
   private final EventBus eventBus;
   private final Client client;
   private final Gson gson;
   @Nonnull
   private final ConfigClient configClient;
   private final ProfileManager profileManager;
   private final SessionManager sessionManager;
   private final ConfigInvocationHandler handler = new ConfigInvocationHandler(this);
   private ConfigProfile profile;
   private ConfigProfile rsProfile;
   private ConfigData configProfile;
   private ConfigData rsProfileConfigProfile;
   @Nullable
   private String rsProfileKey;
   private final Map<Type, Serializer<?>> serializers = Collections.synchronizedMap(new WeakHashMap());

   @Inject
   private ConfigManager(@Nullable @Named("profile") String profile, ScheduledExecutorService scheduledExecutorService, EventBus eventBus, Client client, Gson gson, @Nonnull ConfigClient configClient, ProfileManager profileManager, SessionManager sessionManager) {
      this.configProfileName = profile;
      this.executor = scheduledExecutorService;
      this.eventBus = eventBus;
      this.client = client;
      this.gson = gson;
      this.configClient = configClient;
      this.profileManager = profileManager;
      this.sessionManager = sessionManager;
      scheduledExecutorService.scheduleWithFixedDelay(RunnableExceptionLogger.wrap(this::sendConfig), (long)(30 + (int)(300.0 * Math.random())), 300L, TimeUnit.SECONDS);
   }

   public void switchProfile(ConfigProfile newProfile) {
      if (newProfile.getId() == this.profile.getId()) {
         log.warn("switching to already-active profile!");
      } else {
         this.sendConfig();
         log.info("Switching profile to: {} ({})", newProfile.getName(), newProfile.getId());
         if (this.sessionManager.getAccountSession() != null && newProfile.isSync()) {
            try {
               label85: {
                  ProfileManager.Lock lock = this.profileManager.lock();

                  label76: {
                     try {
                        ConfigProfile profile = lock.findProfile(newProfile.getId());
                        if (profile == null) {
                           log.warn("lost profile while switching!");
                           break label76;
                        }

                        List<Profile> profiles = this.configClient.profiles();
                        if (profiles != null) {
                           this.syncRemote(lock, profile, profiles);
                        }
                     } catch (Throwable var16) {
                        if (lock != null) {
                           try {
                              lock.close();
                           } catch (Throwable var15) {
                              var16.addSuppressed(var15);
                           }
                        }

                        throw var16;
                     }

                     if (lock != null) {
                        lock.close();
                     }
                     break label85;
                  }

                  if (lock != null) {
                     lock.close();
                  }

                  return;
               }
            } catch (IOException var17) {
               IOException ex = var17;
               log.error("error fetching remote profile", ex);
            }
         }

         ConfigData newData = new ConfigData(ProfileManager.profileConfigFile(newProfile));
         Set<String> allKeys = new HashSet(newData.keySet());
         ConfigData oldData;
         synchronized(this) {
            this.handler.invalidate();
            oldData = this.configProfile;
            this.profile = newProfile;
            this.configProfile = newData;
         }

         allKeys.addAll(oldData.keySet());
         Iterator var5 = allKeys.iterator();

         while(var5.hasNext()) {
            String wholeKey = (String)var5.next();
            String[] split = splitKey(wholeKey);
            if (split != null) {
               String groupName = split[0];
               String profile = split[1];
               String key = split[2];
               String oldValue = oldData.getProperty(wholeKey);
               String newValue = newData.getProperty(wholeKey);
               if (!Objects.equals(oldValue, newValue)) {
                  log.debug("Loading configuration value {}: {}", wholeKey, newValue);
                  ConfigChanged configChanged = new ConfigChanged();
                  configChanged.setGroup(groupName);
                  configChanged.setProfile(profile);
                  configChanged.setKey(key);
                  configChanged.setOldValue(oldValue);
                  configChanged.setNewValue(newValue);
                  this.eventBus.post(configChanged);
               }
            }
         }

         this.eventBus.post(new ProfileChanged());
      }
   }

   public String getRSProfileKey() {
      return this.rsProfileKey;
   }

   @Subscribe
   public void onSessionOpen(SessionOpen sessionOpen) {
      AccountSession session = this.sessionManager.getAccountSession();
      this.configClient.setUuid(session.getUuid());

      try {
         List<Profile> profiles = this.configClient.profiles();
         if (profiles != null) {
            this.mergeRemoteProfiles(profiles);
         }
      } catch (IOException var4) {
         IOException e = var4;
         log.error("error syncing remote profiles", e);
      }

      ConfigPatch patch = buildConfigPatch(this.rsProfile.getName(), this.rsProfileConfigProfile.get());
      this.configClient.patch(patch, this.rsProfile.getId());
      log.debug("patched remote {}", "$rsprofile");
   }

   @Subscribe
   public void onSessionClose(SessionClose sessionClose) {
      this.configClient.setUuid((UUID)null);
      ProfileManager.Lock lock = this.profileManager.lock();

      try {
         this.profile = updateProfile(lock, this.profile);
         this.rsProfile = updateProfile(lock, this.rsProfile);
         lock.getProfiles().removeIf((p) -> {
            return p != this.profile && !p.isInternal() && p.isSync();
         });
         if (this.profile.isSync()) {
            log.info("Active remote profile '{}' lost due to session close, converting to a local profile.", this.profile.getName());
            this.profile.setSync(false);
            this.profile.setRev(-1L);
         }

         lock.dirty();
      } catch (Throwable var6) {
         if (lock != null) {
            try {
               lock.close();
            } catch (Throwable var5) {
               var6.addSuppressed(var5);
            }
         }

         throw var6;
      }

      if (lock != null) {
         lock.close();
      }

   }

   public void toggleSync(ConfigProfile profile, boolean sync) {
      log.debug("Setting sync for {}: {}", profile.getName(), sync);
      this.sendConfig();
      ProfileManager.Lock lock = this.profileManager.lock();

      label46: {
         try {
            profile = lock.findProfile(profile.getId());
            if (profile != null && profile.isSync() != sync) {
               profile.setSync(sync);
               lock.dirty();
               if (sync) {
                  File from = ProfileManager.profileConfigFile(profile);
                  ConfigData data = new ConfigData(from);
                  ConfigPatch patch = buildConfigPatch(profile.getName(), data.get());
                  this.configClient.patch(patch, profile.getId());
               } else {
                  this.configClient.delete(profile.getId());
               }
               break label46;
            }
         } catch (Throwable var8) {
            if (lock != null) {
               try {
                  lock.close();
               } catch (Throwable var7) {
                  var8.addSuppressed(var7);
               }
            }

            throw var8;
         }

         if (lock != null) {
            lock.close();
         }

         return;
      }

      if (lock != null) {
         lock.close();
      }

   }

   public void renameProfile(ConfigProfile profile, String name) {
      if (profile.isSync() && this.sessionManager.getAccountSession() != null) {
         this.configClient.rename(profile.getId(), name);
      }

   }

   private void migrate() {
      ProfileManager.Lock lock = this.profileManager.lock();

      try {
         List<ConfigProfile> profiles = lock.getProfiles();
         File configFile = new File(RuneLite.RUNELITE_DIR, "settings.properties");
         if (profiles.isEmpty() && configFile.exists()) {
            String targetProfileName = "default";
            log.info("Performing migration of config from {} to profile '{}'", configFile.getName(), targetProfileName);
            ConfigProfile targetProfile = lock.createProfile(targetProfileName);
            profiles.forEach((p) -> {
               p.setActive(false);
            });
            targetProfile.setActive(true);
            if (this.rsProfile == null) {
               this.rsProfile = lock.findProfile("$rsprofile");
               if (this.rsProfile == null) {
                  this.rsProfile = lock.createProfile("$rsprofile", -1L);
               }

               this.rsProfile.setSync(true);
            }

            if (this.rsProfileConfigProfile == null) {
               this.rsProfileConfigProfile = new ConfigData(ProfileManager.profileConfigFile(this.rsProfile));
            }

            this.importAndMigrate(lock, configFile, targetProfile);
         }
      } catch (Throwable var7) {
         if (lock != null) {
            try {
               lock.close();
            } catch (Throwable var6) {
               var7.addSuppressed(var6);
            }
         }

         throw var7;
      }

      if (lock != null) {
         lock.close();
      }

   }

   public void importAndMigrate(ProfileManager.Lock lock, File from, ConfigProfile targetProfile) {
      ConfigData migratingData = new ConfigData(from);
      ConfigData configData = new ConfigData(ProfileManager.profileConfigFile(targetProfile));
      log.debug("Importing profile from {}", from);
      Set<String> rsProfileKeys = new HashSet();
      List<Map.Entry<String, String>> rsProfileEntries = new ArrayList();
      int keys = 0;
      Iterator var9 = migratingData.get().entrySet().iterator();

      String oldKey;
      while(var9.hasNext()) {
         Map.Entry<String, String> entry = (Map.Entry)var9.next();
         String[] split = splitKey((String)entry.getKey());
         if (split != null) {
            oldKey = split[1];
            if (oldKey != null) {
               rsProfileKeys.add(oldKey);
               rsProfileEntries.add(entry);
            } else {
               configData.setProperty((String)entry.getKey(), (String)entry.getValue());
               ++keys;
            }
         }
      }

      if (rsProfileKeys.size() > 0) {
         Map<String, String> oldToNewRSProfile = new HashMap();
         List<RuneScapeProfile> existingProfiles = this.getRSProfiles();
         Iterator var22 = rsProfileKeys.iterator();

         String profile;
         while(var22.hasNext()) {
            oldKey = (String)var22.next();

            try {
               String strHash = migratingData.getProperty(getWholeKey("rsprofile", oldKey, "accountHash"));
               profile = migratingData.getProperty(getWholeKey("rsprofile", oldKey, "type"));
               if (!Strings.isNullOrEmpty(strHash) && !Strings.isNullOrEmpty(profile)) {
                  long accHash = Long.parseLong(strHash);
                  RuneScapeProfileType type = RuneScapeProfileType.valueOf(profile);
                  RuneScapeProfile newProfile = this.findRSProfile(existingProfiles, accHash, type, (String)null, true);
                  if (newProfile != null) {
                     existingProfiles.add(newProfile);
                     oldToNewRSProfile.put(oldKey, newProfile.getKey());
                     log.info("importing rsprofile \"{}\" as \"{}\"", oldKey, newProfile.getKey());
                     continue;
                  }
               }

               log.info("not importing rsprofile key \"{}\" (hash={} type={})", new Object[]{oldKey, strHash, profile});
            } catch (IllegalArgumentException var19) {
               IllegalArgumentException e = var19;
               log.info("failed to unmarshal imported rsprofile data for key \"{}\"", oldKey, e);
            }
         }

         var22 = rsProfileEntries.iterator();

         while(var22.hasNext()) {
            Map.Entry<String, String> entry = (Map.Entry)var22.next();
            String[] split = splitKey((String)entry.getKey());

            assert split != null;

            profile = split[1];
            profile = (String)oldToNewRSProfile.get(profile);
            if (profile != null && this.getConfiguration(split[0], profile, split[2]) == null) {
               this.setConfiguration(split[0], profile, split[2], (String)entry.getValue());
            }
         }
      }

      configData.patch(configData.swapChanges());
      this.rsProfile = updateProfile(lock, this.rsProfile);
      this.saveConfiguration(lock, this.rsProfile, this.rsProfileConfigProfile);
      log.info("Finished importing {} keys", keys);
   }

   private static void removeDuplicateProfiles(ProfileManager.Lock lock) {
      HashMap<Long, ConfigProfile> seen = new HashMap();
      Iterator<ConfigProfile> it = lock.getProfiles().iterator();

      while(it.hasNext()) {
         ConfigProfile profile = (ConfigProfile)it.next();
         if (seen.containsKey(profile.getId())) {
            ConfigProfile existing = (ConfigProfile)seen.get(profile.getId());
            log.warn("Duplicate profiles detected: {} and {}. Removing the latter.", existing, profile);
            it.remove();
            lock.dirty();
         } else {
            seen.put(profile.getId(), profile);
         }
      }

   }

   private static void fixRsProfileName(ProfileManager.Lock lock) {
      ConfigProfile rsProfile = lock.findProfile(-1L);
      if (rsProfile != null && !rsProfile.getName().equals("$rsprofile")) {
         log.warn("renaming {} to {}", rsProfile, "$rsprofile");
         rsProfile.setName("$rsprofile");
         lock.dirty();
      }

   }

   public void load() {
      AccountSession session = this.sessionManager.getAccountSession();
      List<Profile> remoteProfiles = Collections.emptyList();
      if (session != null) {
         this.configClient.setUuid(session.getUuid());

         try {
            List<Profile> profiles = this.configClient.profiles();
            if (profiles != null) {
               remoteProfiles = profiles;
               this.mergeRemoteProfiles(remoteProfiles);
            }
         } catch (IOException var9) {
            IOException ex = var9;
            log.error("error loading remote profiles", ex);
         }
      }

      this.migrate();
      ProfileManager.Lock lock = this.profileManager.lock();

      try {
         removeDuplicateProfiles(lock);
         fixRsProfileName(lock);
         ConfigProfile profile = null;
         ConfigProfile rsProfile = null;
         Iterator var6 = lock.getProfiles().iterator();

         while(var6.hasNext()) {
            ConfigProfile p = (ConfigProfile)var6.next();
            if (p.isInternal()) {
               log.debug("Profile '{}' (sync: {}, active: {}, id: {}, internal)", new Object[]{p.getName(), p.isSync(), p.getId(), p.isActive()});
               if (p.getName().equals("$rsprofile")) {
                  rsProfile = p;
               }
            } else {
               log.info("Profile '{}' (sync: {}, active: {}, id: {})", new Object[]{p.getName(), p.isSync(), p.isActive(), p.getId()});
            }
         }

         if (rsProfile == null) {
            rsProfile = lock.createProfile("$rsprofile", -1L);
         }

         rsProfile.setSync(true);
         this.syncRemote(lock, rsProfile, remoteProfiles);
         this.rsProfile = rsProfile;
         this.rsProfileConfigProfile = new ConfigData(ProfileManager.profileConfigFile(rsProfile));
         String launcherDisplayName = this.client.getLauncherDisplayName();
         if (this.configProfileName != null) {
            profile = lock.findProfile((px) -> {
               return !px.isInternal() && this.configProfileName.equals(px.getName());
            });
         } else {
            if (launcherDisplayName != null) {
               profile = lock.findProfile((px) -> {
                  if (px.isInternal()) {
                     return false;
                  } else {
                     List<String> defaultRsProfilesForProfile = px.getDefaultForRsProfiles();
                     if (defaultRsProfilesForProfile == null) {
                        return false;
                     } else {
                        Iterator var4 = defaultRsProfilesForProfile.iterator();

                        while(var4.hasNext()) {
                           String defaultRsProfile = (String)var4.next();
                           RuneScapeProfileType rsProfileType = (RuneScapeProfileType)this.getConfiguration((String)"rsprofile", defaultRsProfile, "type", (Type)RuneScapeProfileType.class);
                           if (rsProfileType == RuneScapeProfileType.STANDARD) {
                              String profileDisplayName = this.getConfiguration("rsprofile", defaultRsProfile, "displayName");
                              if (launcherDisplayName.equals(profileDisplayName)) {
                                 return true;
                              }
                           }
                        }

                        return false;
                     }
                  }
               });
            }

            if (profile == null) {
               profile = lock.findProfile((px) -> {
                  return !px.isInternal() && px.isActive();
               });
            }

            if (profile == null) {
               profile = lock.findProfile((px) -> {
                  return !px.isInternal();
               });
            }
         }

         if (profile != null) {
            log.info("Using profile: {} ({})", profile.getName(), profile.getId());
         } else {
            profile = lock.createProfile(this.configProfileName != null ? this.configProfileName : "default");
            if (this.configProfileName == null) {
               lock.getProfiles().forEach((px) -> {
                  px.setActive(false);
               });
               profile.setActive(true);
            }

            log.info("Creating profile: {} ({})", profile.getName(), profile.getId());
         }

         this.syncRemote(lock, profile, remoteProfiles);
         this.profile = profile;
         this.configProfile = new ConfigData(ProfileManager.profileConfigFile(profile));
      } catch (Throwable var10) {
         if (lock != null) {
            try {
               lock.close();
            } catch (Throwable var8) {
               var10.addSuppressed(var8);
            }
         }

         throw var10;
      }

      if (lock != null) {
         lock.close();
      }

      this.eventBus.post(new ProfileChanged());
   }

   private void mergeRemoteProfiles(List<Profile> remoteProfiles) {
      ProfileManager.Lock lock = this.profileManager.lock();

      try {
         boolean migrating = lock.getProfiles().isEmpty();
         Iterator var4 = remoteProfiles.iterator();

         label60:
         while(true) {
            Iterator var6;
            label58:
            while(var4.hasNext()) {
               Profile remoteProfile = (Profile)var4.next();
               var6 = lock.getProfiles().iterator();

               while(var6.hasNext()) {
                  ConfigProfile profile = (ConfigProfile)var6.next();
                  if (profile.getId() == remoteProfile.getId()) {
                     log.debug("Found local profile {} for remote {}", profile, remoteProfile);
                     profile.setName((String)MoreObjects.firstNonNull(remoteProfile.getName(), ""));
                     profile.setSync(true);
                     lock.dirty();
                     continue label58;
                  }
               }

               log.debug("Creating local profile for remote {}", remoteProfile);
               ConfigProfile profile = lock.createProfile((String)MoreObjects.firstNonNull(remoteProfile.getName(), ""), remoteProfile.getId());
               profile.setSync(true);
               if (migrating && remoteProfile.getId() == 0L) {
                  log.info("Using remote profile {} as the active profile", profile.getName());
                  profile.setActive(true);
               }
            }

            var4 = lock.getProfiles().iterator();

            while(true) {
               label72:
               while(true) {
                  if (!var4.hasNext()) {
                     break label60;
                  }

                  ConfigProfile localProfile = (ConfigProfile)var4.next();
                  var6 = remoteProfiles.iterator();

                  while(var6.hasNext()) {
                     Profile remoteProfile = (Profile)var6.next();
                     if (localProfile.getId() == remoteProfile.getId()) {
                        continue label72;
                     }
                  }

                  log.debug("Found local profile {}", localProfile);
                  if (localProfile.isSync() && !localProfile.isInternal()) {
                     log.warn("Lost remote profile for '{}'", localProfile.getName());
                     localProfile.setSync(false);
                     localProfile.setRev(-1L);
                     lock.dirty();
                  }
               }
            }
         }
      } catch (Throwable var9) {
         if (lock != null) {
            try {
               lock.close();
            } catch (Throwable var8) {
               var9.addSuppressed(var8);
            }
         }

         throw var9;
      }

      if (lock != null) {
         lock.close();
      }

   }

   private void syncRemote(ProfileManager.Lock lock, ConfigProfile profile, List<Profile> remoteProfiles) {
      if (profile.isSync()) {
         long id = profile.getId();
         Profile remoteProfile = (Profile)remoteProfiles.stream().filter((p) -> {
            return p.getId() == id;
         }).findFirst().orElse((Object)null);
         if (remoteProfile == null) {
            if (!profile.isInternal()) {
               log.warn("synced profile {} has no remote!", profile);
            }

         } else {
            if (profile.getRev() == remoteProfile.getRev()) {
               log.info("Profile '{}' is up to date", profile.getName());
            } else {
               log.info("Loading remote configuration for profile '{}'", profile.getName());

               try {
                  Configuration remoteConfiguration = this.configClient.get(profile.getId());
                  if (remoteConfiguration == null || remoteConfiguration.getConfig() == null || remoteConfiguration.getConfig().isEmpty()) {
                     log.debug("no remote configuration for {}", profile);
                     return;
                  }

                  File configFile = ProfileManager.profileConfigFile(profile);
                  configFile.delete();
                  ConfigData configData = new ConfigData(configFile);
                  configData.putAll(remoteConfiguration.getConfig());
                  configData.patch(configData.swapChanges());
                  log.debug("synced remote profile {} rev {} to disk", profile, remoteConfiguration.getRev());
                  profile.setRev(remoteConfiguration.getRev());
                  lock.dirty();
               } catch (IOException var10) {
                  IOException ex = var10;
                  log.error("unable to load remote configuration for {}", profile, ex);
               }
            }

         }
      }
   }

   public <T extends Config> T getConfig(Class<T> clazz) {
      if (!Modifier.isPublic(clazz.getModifiers())) {
         throw new RuntimeException("Non-public configuration classes can't have default methods invoked");
      } else {
         T t = (Config)Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz}, this.handler);
         return t;
      }
   }

   public List<String> getConfigurationKeys(String prefix) {
      return (List)this.configProfile.keySet().stream().filter((k) -> {
         return k.startsWith(prefix);
      }).collect(Collectors.toList());
   }

   public List<String> getRSProfileConfigurationKeys(String group, String profile, String keyPrefix) {
      if (profile == null) {
         return Collections.emptyList();
      } else {
         assert profile.startsWith("rsprofile");

         String prefix = group + "." + profile + "." + keyPrefix;
         return (List)this.rsProfileConfigProfile.keySet().stream().filter((k) -> {
            return k.startsWith(prefix);
         }).map((k) -> {
            return splitKey(k)[2];
         }).collect(Collectors.toList());
      }
   }

   public static String getWholeKey(String groupName, String profile, String key) {
      return profile == null ? groupName + "." + key : groupName + "." + profile + "." + key;
   }

   private String getConfiguration(ConfigData configData, String groupName, String rsProfile, String key) {
      return configData.getProperty(getWholeKey(groupName, rsProfile, key));
   }

   public String getConfiguration(String groupName, String key) {
      return this.getConfiguration((ConfigData)this.configProfile, groupName, (String)null, (String)key);
   }

   public String getRSProfileConfiguration(String groupName, String key) {
      String rsProfileKey = this.rsProfileKey;
      return rsProfileKey == null ? null : this.getConfiguration(this.rsProfileConfigProfile, groupName, rsProfileKey, key);
   }

   public String getConfiguration(String groupName, String profile, String key) {
      return profile != null ? this.getConfiguration(this.rsProfileConfigProfile, groupName, profile, key) : this.getConfiguration((ConfigData)this.configProfile, groupName, (String)null, (String)key);
   }

   public <T> T getConfiguration(String groupName, String key, Type clazz) {
      return this.getConfiguration((String)groupName, (String)null, key, (Type)clazz);
   }

   public <T> T getRSProfileConfiguration(String groupName, String key, Type clazz) {
      String rsProfileKey = this.rsProfileKey;
      return rsProfileKey == null ? null : this.getConfiguration(groupName, rsProfileKey, key, clazz);
   }

   public <T> T getConfiguration(String groupName, String profile, String key, Type type) {
      String value = this.getConfiguration(groupName, profile, key);
      if (!Strings.isNullOrEmpty(value)) {
         try {
            return this.stringToObject(value, type);
         } catch (Exception var7) {
            Exception e = var7;
            log.warn("Unable to unmarshal {} ", getWholeKey(groupName, profile, key), e);
         }
      }

      return null;
   }

   private void setConfiguration(ConfigData configData, String groupName, String profile, String key, @NonNull String value) {
      if (value == null) {
         throw new NullPointerException("value is marked non-null but is null");
      } else if (!Strings.isNullOrEmpty(groupName) && !Strings.isNullOrEmpty(key) && key.indexOf(58) == -1 && !key.startsWith("$")) {
         assert !key.startsWith("rsprofile.");

         String wholeKey = getWholeKey(groupName, profile, key);
         String oldValue = configData.setProperty(wholeKey, value);
         if (!Objects.equals(oldValue, value)) {
            log.debug("Setting configuration value for {} to {}", wholeKey, value);
            this.handler.invalidate();
            ConfigChanged configChanged = new ConfigChanged();
            configChanged.setGroup(groupName);
            configChanged.setProfile(profile);
            configChanged.setKey(key);
            configChanged.setOldValue(oldValue);
            configChanged.setNewValue(value);
            this.eventBus.post(configChanged);
         }
      } else {
         throw new IllegalArgumentException();
      }
   }

   public void setConfiguration(String groupName, String profile, String key, @NonNull String value) {
      if (value == null) {
         throw new NullPointerException("value is marked non-null but is null");
      } else {
         if (profile != null) {
            this.setConfiguration(this.rsProfileConfigProfile, groupName, profile, key, value);
         } else {
            this.setConfiguration(this.configProfile, groupName, (String)null, key, value);
         }

      }
   }

   public void setConfiguration(String groupName, String key, String value) {
      this.setConfiguration(this.configProfile, groupName, (String)null, key, value);
   }

   public <T> void setConfiguration(String groupName, String profile, String key, T value) {
      this.setConfiguration(groupName, profile, key, this.objectToString(value));
   }

   public <T> void setConfiguration(String groupName, String key, T value) {
      this.setConfiguration(groupName, (String)null, key, (Object)value);
   }

   public <T> void setRSProfileConfiguration(String groupName, String key, T value) {
      String rsProfileKey = this.rsProfileKey;
      if (rsProfileKey == null) {
         if (this.client == null) {
            log.warn("trying to use profile without injected client");
            return;
         }

         String displayName = null;
         Player p = this.client.getLocalPlayer();
         if (p == null) {
            log.warn("trying to create profile without display name");
         } else {
            displayName = p.getName();
         }

         RuneScapeProfile prof = this.findRSProfile(this.getRSProfiles(), this.client.getAccountHash(), RuneScapeProfileType.getCurrent(this.client), displayName, true);
         if (prof == null) {
            log.warn("trying to create a profile while not logged in");
            return;
         }

         rsProfileKey = prof.getKey();
         String previousProfile = this.rsProfileKey;
         this.rsProfileKey = rsProfileKey;
         log.debug("RS profile changed to {}", rsProfileKey);
         this.eventBus.post(new RuneScapeProfileChanged(previousProfile, rsProfileKey));
      }

      this.setConfiguration(groupName, rsProfileKey, key, value);
   }

   private void unsetConfiguration(ConfigData configData, String groupName, String profile, String key) {
      assert !key.startsWith("rsprofile.");

      String wholeKey = getWholeKey(groupName, profile, key);
      String oldValue = configData.unset(wholeKey);
      if (oldValue != null) {
         log.debug("Unsetting configuration value for {}", wholeKey);
         this.handler.invalidate();
         ConfigChanged configChanged = new ConfigChanged();
         configChanged.setGroup(groupName);
         configChanged.setProfile(profile);
         configChanged.setKey(key);
         configChanged.setOldValue(oldValue);
         this.eventBus.post(configChanged);
      }
   }

   public void unsetConfiguration(String groupName, String profile, String key) {
      if (profile != null) {
         this.unsetConfiguration(this.rsProfileConfigProfile, groupName, profile, key);
      } else {
         this.unsetConfiguration(this.configProfile, groupName, (String)null, key);
      }

   }

   public void unsetConfiguration(String groupName, String key) {
      this.unsetConfiguration(this.configProfile, groupName, (String)null, key);
   }

   public void unsetRSProfileConfiguration(String groupName, String key) {
      String rsProfileKey = this.rsProfileKey;
      if (rsProfileKey != null) {
         this.unsetConfiguration(this.rsProfileConfigProfile, groupName, rsProfileKey, key);
      }
   }

   public ConfigDescriptor getConfigDescriptor(Config configurationProxy) {
      Class<?> inter = configurationProxy.getClass().getInterfaces()[0];
      ConfigGroup group = (ConfigGroup)inter.getAnnotation(ConfigGroup.class);
      if (group == null) {
         throw new IllegalArgumentException("Not a config group");
      } else {
         List<ConfigSectionDescriptor> sections = (List)Arrays.stream(inter.getDeclaredFields()).filter((m) -> {
            return m.isAnnotationPresent(ConfigSection.class) && m.getType() == String.class;
         }).map((m) -> {
            try {
               return new ConfigSectionDescriptor(String.valueOf(m.get(inter)), (ConfigSection)m.getDeclaredAnnotation(ConfigSection.class));
            } catch (IllegalAccessException var3) {
               log.warn("Unable to load section {}::{}", inter.getSimpleName(), m.getName());
               return null;
            }
         }).filter(Objects::nonNull).sorted((a, b) -> {
            return ComparisonChain.start().compare(a.getSection().position(), b.getSection().position()).compare(a.getSection().name(), b.getSection().name()).result();
         }).collect(Collectors.toList());
         List<ConfigItemDescriptor> items = (List)Arrays.stream(inter.getMethods()).filter((m) -> {
            return m.getParameterCount() == 0 && m.isAnnotationPresent(ConfigItem.class);
         }).map((m) -> {
            return new ConfigItemDescriptor((ConfigItem)m.getDeclaredAnnotation(ConfigItem.class), m.getGenericReturnType(), (Range)m.getDeclaredAnnotation(Range.class), (Alpha)m.getDeclaredAnnotation(Alpha.class), (Units)m.getDeclaredAnnotation(Units.class));
         }).sorted((a, b) -> {
            return ComparisonChain.start().compare(a.getItem().position(), b.getItem().position()).compare(a.getItem().name(), b.getItem().name()).result();
         }).collect(Collectors.toList());
         return new ConfigDescriptor(group, sections, items);
      }
   }

   public <T extends Config> void setDefaultConfiguration(T proxy, boolean override) {
      Class<?> clazz = proxy.getClass().getInterfaces()[0];
      ConfigGroup group = (ConfigGroup)clazz.getAnnotation(ConfigGroup.class);
      if (group != null) {
         Method[] var5 = clazz.getDeclaredMethods();
         int var6 = var5.length;

         for(int var7 = 0; var7 < var6; ++var7) {
            Method method = var5[var7];
            ConfigItem item = (ConfigItem)method.getAnnotation(ConfigItem.class);
            if (item != null && method.getParameterCount() == 0) {
               if (!method.isDefault()) {
                  if (override) {
                     String current = this.getConfiguration(group.value(), item.keyName());
                     if (current != null) {
                        this.unsetConfiguration(group.value(), item.keyName());
                     }
                  }
               } else {
                  Object defaultValue;
                  if (!override) {
                     defaultValue = this.getConfiguration(group.value(), item.keyName(), method.getGenericReturnType());
                     if (defaultValue != null) {
                        continue;
                     }
                  }

                  try {
                     defaultValue = ConfigInvocationHandler.callDefaultMethod(proxy, method, (Object[])null);
                  } catch (Throwable var13) {
                     Throwable ex = var13;
                     log.warn((String)null, ex);
                     continue;
                  }

                  String current = this.getConfiguration(group.value(), item.keyName());
                  String valueString = this.objectToString(defaultValue);
                  if (!Objects.equals(current, valueString) && (!Strings.isNullOrEmpty(current) || !Strings.isNullOrEmpty(valueString))) {
                     log.debug("Setting default configuration value for {}.{} to {}", new Object[]{group.value(), item.keyName(), defaultValue});
                     if (Strings.isNullOrEmpty(valueString)) {
                        this.unsetConfiguration(group.value(), item.keyName());
                     } else {
                        this.setConfiguration(group.value(), item.keyName(), valueString);
                     }
                  }
               }
            }
         }

      }
   }

   Object stringToObject(String str, Type type) {
      if (type != Boolean.TYPE && type != Boolean.class) {
         if (type != Integer.TYPE && type != Integer.class) {
            if (type != Long.TYPE && type != Long.class) {
               if (type != Double.TYPE && type != Double.class) {
                  if (type == Color.class) {
                     return ColorUtil.fromString(str);
                  } else {
                     String[] splitStr;
                     int x;
                     int y;
                     if (type == Dimension.class) {
                        splitStr = str.split("x");
                        x = Integer.parseInt(splitStr[0]);
                        y = Integer.parseInt(splitStr[1]);
                        return new Dimension(x, y);
                     } else if (type == Point.class) {
                        splitStr = str.split(":");
                        x = Integer.parseInt(splitStr[0]);
                        y = Integer.parseInt(splitStr[1]);
                        return new Point(x, y);
                     } else {
                        int plane;
                        if (type == Rectangle.class) {
                           splitStr = str.split(":");
                           x = Integer.parseInt(splitStr[0]);
                           y = Integer.parseInt(splitStr[1]);
                           plane = Integer.parseInt(splitStr[2]);
                           int height = Integer.parseInt(splitStr[3]);
                           return new Rectangle(x, y, plane, height);
                        } else if (type instanceof Class && ((Class)type).isEnum()) {
                           return Enum.valueOf((Class)type, str);
                        } else if (type == Instant.class) {
                           return Instant.parse(str);
                        } else if (type != Keybind.class && type != ModifierlessKeybind.class) {
                           if (type == WorldPoint.class) {
                              splitStr = str.split(":");
                              x = Integer.parseInt(splitStr[0]);
                              y = Integer.parseInt(splitStr[1]);
                              plane = Integer.parseInt(splitStr[2]);
                              return new WorldPoint(x, y, plane);
                           } else if (type == Duration.class) {
                              return Duration.ofMillis(Long.parseLong(str));
                           } else if (type == byte[].class) {
                              return Base64.getUrlDecoder().decode(str);
                           } else {
                              if (type instanceof ParameterizedType) {
                                 ParameterizedType parameterizedType = (ParameterizedType)type;
                                 if (parameterizedType.getRawType() == Set.class) {
                                    return this.gson.fromJson(str, parameterizedType);
                                 }
                              }

                              if (type instanceof Class) {
                                 Class<?> clazz = (Class)type;
                                 ConfigSerializer configSerializer = (ConfigSerializer)clazz.getAnnotation(ConfigSerializer.class);
                                 if (configSerializer != null) {
                                    Class<? extends Serializer<?>> serializerClass = configSerializer.value();
                                    Serializer<?> serializer = (Serializer)this.serializers.get(type);
                                    if (serializer == null) {
                                       serializer = (Serializer)RuneLite.getInjector().createChildInjector(new Module[0]).getInstance(serializerClass);
                                       this.serializers.put(type, serializer);
                                    }

                                    return serializer.deserialize(str);
                                 }
                              }

                              return str;
                           }
                        } else {
                           splitStr = str.split(":");
                           x = Integer.parseInt(splitStr[0]);
                           y = Integer.parseInt(splitStr[1]);
                           return type == ModifierlessKeybind.class ? new ModifierlessKeybind(x, y) : new Keybind(x, y);
                        }
                     }
                  }
               } else {
                  return Double.parseDouble(str);
               }
            } else {
               return Long.parseLong(str);
            }
         } else {
            return Integer.parseInt(str);
         }
      } else {
         return Boolean.parseBoolean(str);
      }
   }

   @Nullable
   String objectToString(Object object) {
      if (object instanceof Color) {
         return String.valueOf(((Color)object).getRGB());
      } else if (object instanceof Enum) {
         return ((Enum)object).name();
      } else if (object instanceof Dimension) {
         Dimension d = (Dimension)object;
         return d.width + "x" + d.height;
      } else if (object instanceof Point) {
         Point p = (Point)object;
         return p.x + ":" + p.y;
      } else if (object instanceof Rectangle) {
         Rectangle r = (Rectangle)object;
         return r.x + ":" + r.y + ":" + r.width + ":" + r.height;
      } else if (object instanceof Instant) {
         return ((Instant)object).toString();
      } else {
         int var10000;
         if (object instanceof Keybind) {
            Keybind k = (Keybind)object;
            var10000 = k.getKeyCode();
            return "" + var10000 + ":" + k.getModifiers();
         } else if (object instanceof WorldPoint) {
            WorldPoint wp = (WorldPoint)object;
            var10000 = wp.getX();
            return "" + var10000 + ":" + wp.getY() + ":" + wp.getPlane();
         } else if (object instanceof Duration) {
            return Long.toString(((Duration)object).toMillis());
         } else if (object instanceof byte[]) {
            return Base64.getUrlEncoder().encodeToString((byte[])object);
         } else if (object instanceof Set) {
            return this.gson.toJson(object, Set.class);
         } else {
            if (object != null) {
               ConfigSerializer configSerializer = (ConfigSerializer)object.getClass().getAnnotation(ConfigSerializer.class);
               if (configSerializer != null) {
                  Class<? extends Serializer<?>> serializerClass = configSerializer.value();
                  Serializer serializer = (Serializer)this.serializers.get(serializerClass);
                  if (serializer == null) {
                     serializer = (Serializer)RuneLite.getInjector().createChildInjector(new Module[0]).getInstance(serializerClass);
                     this.serializers.put(serializerClass, serializer);
                  }

                  return serializer.serialize(object);
               }
            }

            return object == null ? null : object.toString();
         }
      }
   }

   @Subscribe(
      priority = -100.0F
   )
   private void onClientShutdown(ClientShutdown e) {
      this.sendConfig();
   }

   public void sendConfig() {
      this.eventBus.post(new ConfigSync());
      ProfileManager.Lock lock = this.profileManager.lock();

      try {
         this.profile = updateProfile(lock, this.profile);
         this.rsProfile = updateProfile(lock, this.rsProfile);
         this.saveConfiguration(lock, this.profile, this.configProfile);
         this.saveConfiguration(lock, this.rsProfile, this.rsProfileConfigProfile);
      } catch (Throwable var5) {
         if (lock != null) {
            try {
               lock.close();
            } catch (Throwable var4) {
               var5.addSuppressed(var4);
            }
         }

         throw var5;
      }

      if (lock != null) {
         lock.close();
      }

   }

   private static ConfigProfile updateProfile(ProfileManager.Lock lock, ConfigProfile profile) {
      ConfigProfile p = lock.findProfile(profile.getId());
      if (p == null) {
         log.warn("Lost active profile {}!", profile.getName());
         p = lock.createProfile(profile.getName(), profile.getId());
         p.setActive(profile.isActive());
      } else if (profile.getRev() != p.getRev()) {
         log.debug("Profile {} changed on disk", p.getName());
      }

      return p;
   }

   private void saveConfiguration(ProfileManager.Lock lock, ConfigProfile profile, ConfigData data) {
      Map<String, String> patch = data.swapChanges();
      if (!patch.isEmpty()) {
         log.debug("Saving profile {} (patch size: {})", profile.getName(), patch.size());
         if (profile.isSync() && this.sessionManager.getAccountSession() != null) {
            try {
               ConfigPatchResult patchResult = (ConfigPatchResult)this.configClient.patch(buildConfigPatch(profile.isInternal() ? profile.getName() : null, patch), profile.getId()).get();
               if (patchResult == null) {
                  profile.setRev(-1L);
               } else {
                  long oldRev = patchResult.getRev() - 1L;
                  long newRev = patchResult.getRev();
                  if (oldRev == profile.getRev()) {
                     profile.setRev(newRev);
                     log.debug("incremental patch applied {} -> {}", oldRev, newRev);
                  } else {
                     log.debug("rev mismatch {} != {}, invalidating", oldRev, newRev);
                     profile.setRev(-1L);
                  }
               }

               lock.dirty();
            } catch (InterruptedException | ExecutionException var10) {
               Exception e = var10;
               profile.setRev(-1L);
               lock.dirty();
               log.error("error applying incremental patch", e);
            }
         }

         data.patch(patch);
      }
   }

   private static ConfigPatch buildConfigPatch(@Nullable String profileName, Map<String, String> patchChanges) {
      ConfigPatch patch = new ConfigPatch();
      patch.setProfileName(profileName);
      Iterator var3 = patchChanges.entrySet().iterator();

      while(var3.hasNext()) {
         Map.Entry<String, String> entry = (Map.Entry)var3.next();
         String key = (String)entry.getKey();
         String value = (String)entry.getValue();
         if (value == null) {
            patch.getUnset().add(key);
         } else {
            patch.getEdit().put(key, value);
         }
      }

      return patch;
   }

   public List<RuneScapeProfile> getRSProfiles() {
      String prefix = "rsprofile.rsprofile.";
      Set<String> profileKeys = new HashSet();
      Iterator var3 = this.rsProfileConfigProfile.keySet().iterator();

      while(var3.hasNext()) {
         String key = (String)var3.next();
         if (key.startsWith(prefix)) {
            String[] split = splitKey(key);
            if (split != null) {
               profileKeys.add(split[1]);
            }
         }
      }

      return (List)profileKeys.stream().map((keyx) -> {
         Long accid = (Long)this.getConfiguration((String)"rsprofile", keyx, "accountHash", (Type)Long.TYPE);
         RuneScapeProfile prof = new RuneScapeProfile(this.getConfiguration("rsprofile", keyx, "displayName"), (RuneScapeProfileType)this.getConfiguration((String)"rsprofile", keyx, "type", (Type)RuneScapeProfileType.class), accid == null ? -1L : accid, keyx);
         return prof;
      }).sorted(Comparator.comparing(RuneScapeProfile::getKey)).collect(Collectors.toCollection(ArrayList::new));
   }

   private synchronized RuneScapeProfile findRSProfile(List<RuneScapeProfile> profiles, long accountHash, RuneScapeProfileType type, String displayName, boolean create) {
      if (accountHash == -1L) {
         return null;
      } else {
         List<RuneScapeProfile> matches = (List)profiles.stream().filter((p) -> {
            return p.getType() == type && accountHash == p.getAccountHash();
         }).collect(Collectors.toList());
         if (matches.size() > 1) {
            log.warn("multiple matching profiles, choosing {}, ignoring {}", matches.get(0), matches.subList(1, matches.size()));
         }

         if (matches.size() >= 1) {
            return (RuneScapeProfile)matches.get(0);
         } else if (!create) {
            return null;
         } else {
            Set<String> keys = (Set)profiles.stream().map(RuneScapeProfile::getKey).collect(Collectors.toSet());
            byte[] key = new byte[]{(byte)((int)accountHash), (byte)((int)(accountHash >> 8)), (byte)((int)(accountHash >> 16)), (byte)((int)(accountHash >> 24)), (byte)((int)(accountHash >> 32)), (byte)((int)(accountHash >> 40))};
            key[0] = (byte)(key[0] + type.ordinal());

            for(int i = 0; i < 255; ++key[1]) {
               String keyStr = "rsprofile." + Base64.getUrlEncoder().encodeToString(key);
               if (!keys.contains(keyStr)) {
                  log.info("creating new profile {} for account hash {} ({})", new Object[]{keyStr, accountHash, type});
                  this.setConfiguration("rsprofile", keyStr, "accountHash", (Object)accountHash);
                  this.setConfiguration("rsprofile", keyStr, "type", (Object)type);
                  if (displayName != null) {
                     this.setConfiguration("rsprofile", keyStr, "displayName", displayName);
                  }

                  return new RuneScapeProfile(displayName, type, accountHash, keyStr);
               }

               ++i;
            }

            throw new RuntimeException("too many rs profiles");
         }
      }
   }

   private void updateRSProfile() {
      if (this.client != null) {
         List<RuneScapeProfile> profiles = this.getRSProfiles();
         RuneScapeProfile prof = this.findRSProfile(profiles, this.client.getAccountHash(), RuneScapeProfileType.getCurrent(this.client), (String)null, false);
         String key = prof == null ? null : prof.getKey();
         if (!Objects.equals(key, this.rsProfileKey)) {
            String previousProfile = this.rsProfileKey;
            this.rsProfileKey = key;
            log.debug("RS profile changed to {}", key);
            this.eventBus.post(new RuneScapeProfileChanged(previousProfile, key));
         }
      }
   }

   @Subscribe
   private void onAccountHashChanged(AccountHashChanged ev) {
      this.updateRSProfile();
   }

   @Subscribe
   private void onWorldChanged(WorldChanged ev) {
      this.updateRSProfile();
   }

   @Subscribe
   private void onPlayerChanged(PlayerChanged ev) {
      if (ev.getPlayer() == this.client.getLocalPlayer()) {
         String name = ev.getPlayer().getName();
         this.setRSProfileConfiguration("rsprofile", "displayName", name);
      }

   }

   @Subscribe
   private void onRuneScapeProfileChanged(RuneScapeProfileChanged ev) {
      ProfileManager.Lock lock = this.profileManager.lock();

      try {
         Iterator var3 = lock.getProfiles().iterator();

         while(var3.hasNext()) {
            ConfigProfile profile = (ConfigProfile)var3.next();
            List<String> get = profile.getDefaultForRsProfiles();
            if (get != null && get.contains(this.rsProfileKey)) {
               lock.getProfiles().forEach((p) -> {
                  p.setActive(false);
               });
               profile.setActive(true);
               lock.dirty();
               log.debug("Switching to default profile {} for rsprofile {}", profile.getName(), this.rsProfileKey);
               this.executor.submit(() -> {
                  this.switchProfile(profile);
               });
               break;
            }
         }
      } catch (Throwable var7) {
         if (lock != null) {
            try {
               lock.close();
            } catch (Throwable var6) {
               var7.addSuppressed(var6);
            }
         }

         throw var7;
      }

      if (lock != null) {
         lock.close();
      }

   }

   @Nullable
   @VisibleForTesting
   static String[] splitKey(String key) {
      int i = key.indexOf(46);
      if (i == -1) {
         return null;
      } else {
         String group = key.substring(0, i);
         String profile = null;
         key = key.substring(i + 1);
         if (key.startsWith("rsprofile.")) {
            i = key.indexOf(46, "rsprofile".length() + 2);
            profile = key.substring(0, i);
            key = key.substring(i + 1);
         }

         return new String[]{group, profile, key};
      }
   }

   public ConfigProfile getProfile() {
      return this.profile;
   }
}
