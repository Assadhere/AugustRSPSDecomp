package net.runelite.client;

import com.google.common.hash.HashCode;
import com.google.common.hash.Hasher;
import com.google.common.hash.Hashing;
import com.google.common.hash.HashingOutputStream;
import com.google.inject.Inject;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.NetworkInterface;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import javax.inject.Named;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.ui.SplashScreen;
import net.runelite.client.util.OSType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class Updater {
   private static final Logger log = LoggerFactory.getLogger(Updater.class);
   private static final String LAUNCHER_EXECUTABLE_NAME_WIN = "RuneLite.exe";
   private static final String WIN64_URL = "https://github.com/runelite/launcher/releases/download/2.6.7/RuneLiteSetup.exe";
   private static final String WIN64_CHECKSUM = "6e388243311622782deaed24555fdcc89672c6d22b843245d8514fdaeee4586c";
   private static final int WIN64_SIZE = 29440032;
   private static final String WIN32_URL = "https://github.com/runelite/launcher/releases/download/2.6.7/RuneLiteSetup32.exe";
   private static final String WIN32_CHECKSUM = "3e79a0aa4d09ff8782e8ca7a30b4948abb935eacdce8b71d6263fb92e1d25e79";
   private static final int WIN32_SIZE = 25299936;
   private final OkHttpClient okHttpClient;
   private final RuntimeConfig runtimeConfig;
   private final ConfigManager configManager;
   private final boolean noupdate;

   @Inject
   Updater(OkHttpClient okHttpClient, @Nullable RuntimeConfig runtimeConfig, ConfigManager configManager, @Named("noupdate") boolean noupdate) {
      this.okHttpClient = okHttpClient;
      this.runtimeConfig = runtimeConfig;
      this.configManager = configManager;
      this.noupdate = noupdate;
   }

   void update() {
      try {
         this.tryUpdate();
      } catch (Exception var2) {
         Exception ex = var2;
         log.error("error updating", ex);
      }

   }

   void tryUpdate() {
      String osName = System.getProperty("os.name");
      if (!"Windows 10".equals(osName) && !"Windows 11".equals(osName)) {
         log.debug("Unsupported OS: {}", osName);
      } else {
         String arch = System.getProperty("os.arch");
         String downloadUrl;
         String checksum;
         int size;
         if ("amd64".equals(arch)) {
            downloadUrl = "https://github.com/runelite/launcher/releases/download/2.6.7/RuneLiteSetup.exe";
            checksum = "6e388243311622782deaed24555fdcc89672c6d22b843245d8514fdaeee4586c";
            size = 29440032;
         } else {
            if (!"x86".equals(arch)) {
               log.debug("Unsupported arch {}", arch);
               return;
            }

            downloadUrl = "https://github.com/runelite/launcher/releases/download/2.6.7/RuneLiteSetup32.exe";
            checksum = "3e79a0aa4d09ff8782e8ca7a30b4948abb935eacdce8b71d6263fb92e1d25e79";
            size = 25299936;
         }

         ProcessHandle current = ProcessHandle.current();
         if (current.info().command().isEmpty()) {
            log.debug("Running process has no command");
         } else {
            Path path = Paths.get((String)current.info().command().get());
            if (!path.getFileName().toString().equals("RuneLite.exe")) {
               log.debug("Skipping update check due to not running from installer, command is {}", current.info().command().get());
            } else {
               String launcherVersion = RuneLiteProperties.getLauncherVersion();
               if (launcherVersion != null && !launcherVersion.isEmpty()) {
                  log.debug("Running from installer");
                  if (this.runtimeConfig != null && this.runtimeConfig.getUpdateLauncherWinVers() != null && Arrays.asList(this.runtimeConfig.getUpdateLauncherWinVers()).contains(launcherVersion)) {
                     if (this.noupdate) {
                        log.info("Skipping update due to noupdate being set");
                     } else if (System.getenv("RUNELITE_UPGRADE") != null) {
                        log.info("Skipping update due to launching from an upgrade");
                     } else {
                        Integer updateAttemptNum = (Integer)this.configManager.getConfiguration("runelite", "updateNum", (Type)Integer.class);
                        if (updateAttemptNum == null) {
                           updateAttemptNum = 0;
                        }

                        Long updateAttemptTime = (Long)this.configManager.getConfiguration("runelite", "updateAttemptTime", (Type)Long.class);
                        if (updateAttemptTime == null) {
                           updateAttemptTime = 0L;
                        }

                        String lastUpdateHash = this.configManager.getConfiguration("runelite", "lastUpdateHash");
                        int hours = 1 << Math.min(9, updateAttemptNum);
                        if (checksum.equals(lastUpdateHash) && Instant.ofEpochMilli(updateAttemptTime).isAfter(Instant.now().minus((long)hours, ChronoUnit.HOURS))) {
                           log.info("Previous upgrade attempt was at {} (backoff: {} hours), skipping", LocalTime.from(Instant.ofEpochMilli(updateAttemptTime).atZone(ZoneId.systemDefault())), hours);
                        } else {
                           List<ProcessHandle> allProcesses = (List)ProcessHandle.allProcesses().collect(Collectors.toList());
                           Iterator var14 = allProcesses.iterator();

                           while(var14.hasNext()) {
                              ProcessHandle ph = (ProcessHandle)var14.next();
                              if (ph.pid() != current.pid() && ph.info().command().equals(current.info().command())) {
                                 log.info("Skipping update due to {} process {}", "RuneLite.exe", ph);
                                 return;
                              }
                           }

                           if (this.runtimeConfig.getUpdateRollout() > 0.0 && installRollout() > this.runtimeConfig.getUpdateRollout()) {
                              log.debug("Skipping update due to rollout");
                           } else {
                              log.info("Performing launcher update");
                              this.configManager.setConfiguration("runelite", "updateAttemptNum", (Object)(updateAttemptNum + 1));
                              this.configManager.setConfiguration("runelite", "updateAttemptTime", (Object)System.currentTimeMillis());
                              this.configManager.setConfiguration("runelite", "lastUpdateHash", checksum);
                              this.configManager.sendConfig();

                              try {
                                 log.info("Downloading launcher update");
                                 Request request = (new Request.Builder()).url(downloadUrl).build();
                                 Path tempExe = Files.createTempFile("rlupdate", "exe");
                                 Response response = this.okHttpClient.newCall(request).execute();

                                 HashCode hash;
                                 label170: {
                                    try {
                                       label171: {
                                          HashingOutputStream out = new HashingOutputStream(Hashing.sha256(), Files.newOutputStream(tempExe));

                                          label136: {
                                             try {
                                                if (response.isSuccessful()) {
                                                   InputStream in = response.body().byteStream();
                                                   byte[] buffer = new byte[1048576];
                                                   int downloaded = 0;

                                                   while(true) {
                                                      int i = in.read(buffer);
                                                      if (i == -1) {
                                                         hash = out.hash();
                                                         break label136;
                                                      }

                                                      downloaded += i;
                                                      SplashScreen.stage(0.6, 1.0, (String)null, "RuneLite Setup", downloaded, size, true);
                                                      out.write(buffer, 0, i);
                                                   }
                                                }

                                                log.info("Bad response downloading {}", downloadUrl);
                                             } catch (Throwable var25) {
                                                try {
                                                   out.close();
                                                } catch (Throwable var24) {
                                                   var25.addSuppressed(var24);
                                                }

                                                throw var25;
                                             }

                                             out.close();
                                             break label171;
                                          }

                                          out.close();
                                          break label170;
                                       }
                                    } catch (Throwable var26) {
                                       if (response != null) {
                                          try {
                                             response.close();
                                          } catch (Throwable var23) {
                                             var26.addSuppressed(var23);
                                          }
                                       }

                                       throw var26;
                                    }

                                    if (response != null) {
                                       response.close();
                                    }

                                    return;
                                 }

                                 if (response != null) {
                                    response.close();
                                 }

                                 if (!hash.toString().equals(checksum)) {
                                    log.info("Hash mismatch for update. Expected {} got {}.", checksum, hash);
                                    return;
                                 }

                                 log.info("Launching installer");
                                 ProcessBuilder pb = new ProcessBuilder(new String[]{tempExe.toFile().getAbsolutePath(), "/SILENT"});
                                 Map<String, String> env = pb.environment();
                                 env.put("RUNELITE_UPGRADE", "1");
                                 pb.start();
                                 System.exit(0);
                              } catch (IOException var27) {
                                 IOException e = var27;
                                 log.error("io error performing upgrade", e);
                              }

                           }
                        }
                     }
                  } else {
                     log.debug("No update available");
                  }
               } else {
                  log.debug("Skipping update check due to not running from installer, no launcher version");
               }
            }
         }
      }
   }

   private static double installRollout() {
      try {
         Hasher hasher = Hashing.sha256().newHasher();
         Runtime runtime = Runtime.getRuntime();
         hasher.putByte((byte)OSType.getOSType().ordinal());
         hasher.putByte((byte)runtime.availableProcessors());
         hasher.putUnencodedChars(System.getProperty("os.arch", ""));
         hasher.putUnencodedChars(System.getProperty("os.version", ""));
         hasher.putUnencodedChars(System.getProperty("user.name", ""));
         Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();

         while(networkInterfaces.hasMoreElements()) {
            NetworkInterface networkInterface = (NetworkInterface)networkInterfaces.nextElement();
            byte[] hardwareAddress = networkInterface.getHardwareAddress();
            if (hardwareAddress != null) {
               hasher.putBytes(hardwareAddress);
            }
         }

         HashCode hash = hasher.hash();
         return (double)(hash.asInt() & Integer.MAX_VALUE) / 2.147483647E9;
      } catch (Exception var5) {
         Exception ex = var5;
         log.error("unable to generate machine id", ex);
         return Math.random();
      }
   }
}
