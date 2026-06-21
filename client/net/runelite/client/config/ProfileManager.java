package net.runelite.client.config;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;
import javax.inject.Inject;
import javax.inject.Singleton;
import net.runelite.client.RuneLite;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
public class ProfileManager {
   private static final Logger log = LoggerFactory.getLogger(ProfileManager.class);
   private static final File PROFILES_DIR;
   private static final File PROFILES;
   private final Gson gson;

   public Lock lock() {
      return new Lock();
   }

   public static File profileConfigFile(ConfigProfile profile) {
      File var10002 = PROFILES_DIR;
      String var10003 = profile.getName();
      return new File(var10002, var10003 + "-" + profile.getId() + ".properties");
   }

   @Inject
   public ProfileManager(Gson gson) {
      this.gson = gson;
   }

   static {
      PROFILES_DIR = new File(RuneLite.RUNELITE_DIR, "profiles2");
      PROFILES = new File(PROFILES_DIR, "profiles.json");
      PROFILES_DIR.mkdirs();
   }

   public class Lock implements AutoCloseable {
      private final File lockFile;
      private final FileOutputStream lockOut;
      private final FileChannel lockChannel;
      private final List<ConfigProfile> profiles;
      private boolean modified = false;

      public Lock() {
         try {
            this.lockFile = new File(ProfileManager.PROFILES_DIR, "profiles.lck");
            this.lockOut = new FileOutputStream(this.lockFile);
            this.lockChannel = this.lockOut.getChannel();
            this.lockChannel.lock();
            this.profiles = new ArrayList(this.load());
         } catch (Throwable var3) {
            Throwable $ex = var3;
            throw $ex;
         }
      }

      private List<ConfigProfile> load() {
         try {
            FileInputStream in = new FileInputStream(ProfileManager.PROFILES);

            List var2;
            try {
               var2 = ((Profiles)ProfileManager.this.gson.fromJson(new InputStreamReader(in, StandardCharsets.UTF_8), Profiles.class)).getProfiles();
            } catch (Throwable var5) {
               try {
                  in.close();
               } catch (Throwable var4) {
                  var5.addSuppressed(var4);
               }

               throw var5;
            }

            in.close();
            return var2;
         } catch (FileNotFoundException var6) {
            return Collections.emptyList();
         } catch (JsonSyntaxException | IOException var7) {
            Exception e = var7;
            ProfileManager.log.error("unable to read profiles", e);
            return Collections.emptyList();
         }
      }

      public void close() {
         try {
            if (this.modified) {
               ProfileManager.log.debug("saving {} profiles", this.profiles.size());
               File tempFile = File.createTempFile("runelite_profiles", (String)null, ProfileManager.PROFILES_DIR);
               FileOutputStream out = new FileOutputStream(tempFile);

               try {
                  FileChannel channel = out.getChannel();

                  try {
                     OutputStreamWriter writer = new OutputStreamWriter(out, StandardCharsets.UTF_8);

                     try {
                        Profiles profilesData = new Profiles();
                        profilesData.setProfiles(this.profiles);
                        ProfileManager.this.gson.toJson(profilesData, writer);
                        writer.flush();
                        channel.force(true);
                     } catch (Throwable var11) {
                        try {
                           writer.close();
                        } catch (Throwable var9) {
                           var11.addSuppressed(var9);
                        }

                        throw var11;
                     }

                     writer.close();
                  } catch (Throwable var12) {
                     if (channel != null) {
                        try {
                           channel.close();
                        } catch (Throwable var8) {
                           var12.addSuppressed(var8);
                        }
                     }

                     throw var12;
                  }

                  if (channel != null) {
                     channel.close();
                  }
               } catch (Throwable var13) {
                  try {
                     out.close();
                  } catch (Throwable var7) {
                     var13.addSuppressed(var7);
                  }

                  throw var13;
               }

               out.close();

               try {
                  Files.move(tempFile.toPath(), ProfileManager.PROFILES.toPath(), StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
               } catch (AtomicMoveNotSupportedException var10) {
                  AtomicMoveNotSupportedException ex = var10;
                  ProfileManager.log.debug("atomic move not supported", ex);
                  Files.move(tempFile.toPath(), ProfileManager.PROFILES.toPath(), StandardCopyOption.REPLACE_EXISTING);
               }
            }

            this.lockOut.close();
            this.lockFile.delete();
         } catch (Throwable var14) {
            Throwable $ex = var14;
            throw $ex;
         }
      }

      public List<ConfigProfile> getProfiles() {
         return this.profiles;
      }

      public ConfigProfile createProfile(String name, long id) {
         if (this.findProfile(id) != null) {
            throw new IllegalArgumentException("profile " + id + " already exists");
         } else {
            ConfigProfile profile = new ConfigProfile(id);
            profile.setName(name);
            profile.setSync(false);
            profile.setRev(-1L);
            profile.setDefaultForRsProfiles(new ArrayList());
            this.profiles.add(profile);
            this.modified = true;
            ProfileManager.log.debug("Created profile {}", profile);
            return profile;
         }
      }

      public ConfigProfile createProfile(String name) {
         return this.createProfile(name, System.nanoTime());
      }

      public ConfigProfile findProfile(String name) {
         return this.findProfile((profile) -> {
            return profile.getName().equals(name);
         });
      }

      public ConfigProfile findProfile(long id) {
         return this.findProfile((profile) -> {
            return profile.getId() == id;
         });
      }

      public ConfigProfile findProfile(Predicate<ConfigProfile> condition) {
         Iterator var2 = this.profiles.iterator();

         ConfigProfile configProfile;
         do {
            if (!var2.hasNext()) {
               return null;
            }

            configProfile = (ConfigProfile)var2.next();
         } while(!condition.test(configProfile));

         return configProfile;
      }

      public void removeProfile(long id) {
         this.modified |= this.profiles.removeIf((p) -> {
            return p.getId() == id;
         });
      }

      public void renameProfile(ConfigProfile profile, String name) {
         File oldFile = ProfileManager.profileConfigFile(profile);
         profile.setName(name);
         this.modified = true;
         File newFile = ProfileManager.profileConfigFile(profile);
         if (!oldFile.exists()) {
            ProfileManager.log.info("Old profile file {} does not exist", oldFile.getName());
         } else {
            try {
               Files.move(oldFile.toPath(), newFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
               ProfileManager.log.info("Renamed profile file {} to {}", oldFile.getName(), newFile.getName());
            } catch (IOException var6) {
               IOException e = var6;
               ProfileManager.log.error("error renaming profile", e);
            }

         }
      }

      public void dirty() {
         this.modified = true;
      }
   }
}
