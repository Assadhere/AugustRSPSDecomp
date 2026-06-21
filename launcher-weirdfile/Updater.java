package net.runelite.launcher;

import com.google.common.escape.Escaper;
import com.google.common.escape.Escapers;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.CopyOption;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.Instant;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import net.runelite.launcher.beans.Bootstrap;
import net.runelite.launcher.beans.Update;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

class Updater {
   private static final Logger log = LoggerFactory.getLogger(Updater.class);
   private static final String RUNELITE_APP = "/Applications/AugustRSPS.app";

   static void update(Bootstrap bootstrap, LauncherSettings launcherSettings, String[] args) {
      if (OS.getOs() == OS.OSType.Windows) {
         updateWindows(bootstrap, launcherSettings, args);
      } else if (OS.getOs() == OS.OSType.MacOS) {
         updateMacos(bootstrap, launcherSettings, args);
      }

   }

   private static void updateMacos(Bootstrap bootstrap, LauncherSettings launcherSettings, String[] args) {
      ProcessHandle current = ProcessHandle.current();
      Optional<String> command = current.info().command();
      if (command.isEmpty()) {
         log.debug("Running process has no command");
      } else {
         Path path = Paths.get((String)command.get());
         path = path.normalize().resolveSibling(Path.of("..", "MacOS", path.getFileName().toString())).normalize();
         if (path.getFileName().toString().equals("AugustRSPS") && path.startsWith("/Applications/AugustRSPS.app")) {
            log.debug("Running from installer");
            Update newestUpdate = findAvailableUpdate(bootstrap);
            if (newestUpdate != null) {
               boolean noupdate = launcherSettings.isNoupdates();
               if (noupdate) {
                  log.info("Skipping update {} due to noupdate being set", newestUpdate.getVersion());
               } else if (System.getenv("RUNELITE_UPGRADE") != null) {
                  log.info("Skipping update {} due to launching from an upgrade", newestUpdate.getVersion());
               } else {
                  LauncherSettings settings = LauncherSettings.loadSettings();
                  int hours = 1 << Math.min(9, settings.lastUpdateAttemptNum);
                  if (newestUpdate.getHash().equals(settings.lastUpdateHash) && Instant.ofEpochMilli(settings.lastUpdateAttemptTime).isAfter(Instant.now().minus((long)hours, ChronoUnit.HOURS))) {
                     log.info("Previous upgrade attempt to {} was at {} (backoff: {} hours), skipping", new Object[]{newestUpdate.getVersion(), LocalTime.from(Instant.ofEpochMilli(settings.lastUpdateAttemptTime).atZone(ZoneId.systemDefault())), hours});
                  } else if (newestUpdate.getRollout() > 0.0 && Math.random() > newestUpdate.getRollout()) {
                     log.info("Skipping update {} due to rollout", newestUpdate.getVersion());
                  } else {
                     settings.lastUpdateAttemptTime = System.currentTimeMillis();
                     settings.lastUpdateHash = newestUpdate.getHash();
                     ++settings.lastUpdateAttemptNum;
                     LauncherSettings.saveSettings(settings);

                     try {
                        log.info("Downloading launcher {} from {}", newestUpdate.getVersion(), newestUpdate.getUrl());
                        Path file = Files.createTempFile("rlupdate", "dmg");
                        OutputStream fout = Files.newOutputStream(file);

                        label121: {
                           try {
                              String name = newestUpdate.getName();
                              int size = newestUpdate.getSize();

                              try {
                                 Launcher.download(newestUpdate.getUrl(), newestUpdate.getHash(), (completed) -> {
                                    SplashScreen.stage(0.07, 1.0, (String)null, name, completed, size, true);
                                 }, fout);
                                 break label121;
                              } catch (VerificationException var20) {
                                 VerificationException e = var20;
                                 log.error("unable to verify update", e);
                                 file.toFile().delete();
                              }
                           } catch (Throwable var21) {
                              if (fout != null) {
                                 try {
                                    fout.close();
                                 } catch (Throwable var18) {
                                    var21.addSuppressed(var18);
                                 }
                              }

                              throw var21;
                           }

                           if (fout != null) {
                              fout.close();
                           }

                           return;
                        }

                        if (fout != null) {
                           fout.close();
                        }

                        log.debug("Mounting dmg {}", file);
                        ProcessBuilder pb = new ProcessBuilder(new String[]{"hdiutil", "attach", "-nobrowse", "-plist", file.toAbsolutePath().toString()});
                        Process process = pb.start();
                        if (!process.waitFor(5L, TimeUnit.SECONDS)) {
                           process.destroy();
                           log.error("timeout waiting for hdiutil to attach dmg");
                           return;
                        }

                        if (process.exitValue() != 0) {
                           log.error("error running hdiutil attach");
                           return;
                        }

                        InputStream in = process.getInputStream();

                        String mountPoint;
                        try {
                           mountPoint = parseHdiutilPlist(in);
                        } catch (Throwable var19) {
                           if (in != null) {
                              try {
                                 in.close();
                              } catch (Throwable var17) {
                                 var19.addSuppressed(var17);
                              }
                           }

                           throw var19;
                        }

                        if (in != null) {
                           in.close();
                        }

                        log.debug("Removing old install from {}", "/Applications/AugustRSPS.app");
                        delete(Path.of("/Applications/AugustRSPS.app"));
                        log.debug("Copying new install from {}", mountPoint);
                        copy(Path.of(mountPoint, "AugustRSPS.app"), Path.of("/Applications/AugustRSPS.app"));
                        log.debug("Unmounting dmg");
                        pb = new ProcessBuilder(new String[]{"hdiutil", "detach", mountPoint});
                        pb.start();
                        log.debug("Done! Launching...");
                        List<String> launchCmd = new ArrayList(args.length + 1);
                        launchCmd.add(path.toAbsolutePath().toString());
                        launchCmd.addAll(Arrays.asList(args));
                        pb = new ProcessBuilder(launchCmd);
                        pb.environment().put("RUNELITE_UPGRADE", "1");
                        pb.start();
                        System.exit(0);
                     } catch (Exception var22) {
                        Exception e = var22;
                        log.error("error performing upgrade", e);
                     }

                  }
               }
            }
         } else {
            log.debug("Skipping update check due to not running from installer, command is {}", command.get());
         }
      }
   }

   static String parseHdiutilPlist(InputStream in) throws Exception {
      DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
      DocumentBuilder db = dbf.newDocumentBuilder();
      Document doc = db.parse(in);
      doc.getDocumentElement().normalize();
      Element plist = (Element)doc.getElementsByTagName("plist").item(0);
      Element dict = (Element)plist.getElementsByTagName("dict").item(0);
      Element arr = (Element)dict.getElementsByTagName("array").item(0);
      NodeList dicts = arr.getElementsByTagName("dict");

      for(int i = 0; i < dicts.getLength(); ++i) {
         NodeList dict2 = (NodeList)dicts.item(i);
         String lastKey = null;

         for(int j = 0; j < dict2.getLength(); ++j) {
            Node node = dict2.item(j);
            if (node.getNodeType() == 1) {
               if (node.getNodeName().equals("key")) {
                  lastKey = node.getTextContent();
               } else if (lastKey != null) {
                  if (lastKey.equals("mount-point")) {
                     return node.getTextContent();
                  }

                  lastKey = null;
               }
            }
         }
      }

      return null;
   }

   private static void updateWindows(Bootstrap bootstrap, LauncherSettings launcherSettings, String[] args) {
      ProcessHandle current = ProcessHandle.current();
      if (current.info().command().isEmpty()) {
         log.debug("Running process has no command");
      } else {
         String installLocation;
         try {
            installLocation = Launcher.regQueryString("Software\\Microsoft\\Windows\\CurrentVersion\\Uninstall\\AugustRSPS Launcher_is1", "InstallLocation");
         } catch (RuntimeException | UnsatisfiedLinkError var21) {
            Throwable ex = var21;
            log.debug("Skipping update check, error querying install location", ex);
            return;
         }

         Path path = Paths.get((String)current.info().command().get());
         if (path.startsWith(installLocation) && path.getFileName().toString().equals("AugustRSPS.exe")) {
            log.debug("Running from installer");
            Update newestUpdate = findAvailableUpdate(bootstrap);
            if (newestUpdate != null) {
               boolean noupdate = launcherSettings.isNoupdates();
               if (noupdate) {
                  log.info("Skipping update {} due to noupdate being set", newestUpdate.getVersion());
               } else if (System.getenv("RUNELITE_UPGRADE") != null) {
                  log.info("Skipping update {} due to launching from an upgrade", newestUpdate.getVersion());
               } else {
                  LauncherSettings settings = LauncherSettings.loadSettings();
                  int hours = 1 << Math.min(9, settings.lastUpdateAttemptNum);
                  if (newestUpdate.getHash().equals(settings.lastUpdateHash) && Instant.ofEpochMilli(settings.lastUpdateAttemptTime).isAfter(Instant.now().minus((long)hours, ChronoUnit.HOURS))) {
                     log.info("Previous upgrade attempt to {} was at {} (backoff: {} hours), skipping", new Object[]{newestUpdate.getVersion(), LocalTime.from(Instant.ofEpochMilli(settings.lastUpdateAttemptTime).atZone(ZoneId.systemDefault())), hours});
                  } else {
                     List<ProcessHandle> allProcesses = (List)ProcessHandle.allProcesses().collect(Collectors.toList());
                     Iterator var11 = allProcesses.iterator();

                     while(var11.hasNext()) {
                        ProcessHandle ph = (ProcessHandle)var11.next();
                        if (ph.pid() != current.pid() && ph.info().command().equals(current.info().command())) {
                           log.info("Skipping update {} due to {} process {}", new Object[]{newestUpdate.getVersion(), "AugustRSPS.exe", ph});
                           return;
                        }
                     }

                     if (newestUpdate.getRollout() > 0.0 && installRollout() > newestUpdate.getRollout()) {
                        log.info("Skipping update {} due to rollout", newestUpdate.getVersion());
                     } else {
                        settings.lastUpdateAttemptTime = System.currentTimeMillis();
                        settings.lastUpdateHash = newestUpdate.getHash();
                        ++settings.lastUpdateAttemptNum;
                        LauncherSettings.saveSettings(settings);

                        try {
                           log.info("Downloading launcher {} from {}", newestUpdate.getVersion(), newestUpdate.getUrl());
                           Path file = Files.createTempFile("rlupdate", "exe");
                           OutputStream fout = Files.newOutputStream(file);

                           label131: {
                              try {
                                 String name = newestUpdate.getName();
                                 int size = newestUpdate.getSize();

                                 try {
                                    Launcher.download(newestUpdate.getUrl(), newestUpdate.getHash(), (completed) -> {
                                       SplashScreen.stage(0.07, 1.0, (String)null, name, completed, size, true);
                                    }, fout);
                                    break label131;
                                 } catch (VerificationException var22) {
                                    VerificationException e = var22;
                                    log.error("unable to verify update", e);
                                    file.toFile().delete();
                                 }
                              } catch (Throwable var23) {
                                 if (fout != null) {
                                    try {
                                       fout.close();
                                    } catch (Throwable var20) {
                                       var23.addSuppressed(var20);
                                    }
                                 }

                                 throw var23;
                              }

                              if (fout != null) {
                                 fout.close();
                              }

                              return;
                           }

                           if (fout != null) {
                              fout.close();
                           }

                           log.info("Launching installer version {}", newestUpdate.getVersion());
                           ProcessBuilder pb = new ProcessBuilder(new String[]{file.toFile().getAbsolutePath(), "/SILENT"});
                           Map<String, String> env = pb.environment();
                           StringBuilder argStr = new StringBuilder();
                           Escaper escaper = Escapers.builder().addEscape('"', "\\\"").build();
                           String[] var16 = args;
                           int var17 = args.length;

                           for(int var18 = 0; var18 < var17; ++var18) {
                              String arg = var16[var18];
                              if (argStr.length() > 0) {
                                 argStr.append(' ');
                              }

                              if (!arg.contains(" ") && !arg.contains("\"")) {
                                 argStr.append(arg);
                              } else {
                                 argStr.append('"').append(escaper.escape(arg)).append('"');
                              }
                           }

                           env.put("RUNELITE_UPGRADE", "1");
                           env.put("RUNELITE_UPGRADE_PARAMS", argStr.toString());
                           pb.start();
                           System.exit(0);
                        } catch (IOException var24) {
                           log.error("io error performing upgrade", var24);
                        }

                     }
                  }
               }
            }
         } else {
            log.debug("Skipping update check due to not running from installer, command is {}", current.info().command().get());
         }
      }
   }

   private static Update findAvailableUpdate(Bootstrap bootstrap) {
      Update[] updates = bootstrap.getUpdates();
      if (updates == null) {
         return null;
      } else {
         String os = System.getProperty("os.name");
         String arch = System.getProperty("os.arch");
         String ver = System.getProperty("os.version");
         String launcherVersion = LauncherProperties.getVersion();
         if (os != null && arch != null && launcherVersion != null) {
            Update newestUpdate = null;
            Update[] var7 = updates;
            int var8 = updates.length;

            for(int var9 = 0; var9 < var8; ++var9) {
               Update update = var7[var9];
               OS.OSType updateOs = OS.parseOs(update.getOs());
               if (updateOs == OS.OSType.Other) {
                  if (!update.getOs().equals(os)) {
                     continue;
                  }
               } else if (updateOs != OS.getOs()) {
                  continue;
               }

               if ((update.getOsName() == null || update.getOsName().equals(os)) && (update.getOsVersion() == null || update.getOsVersion().equals(ver)) && (update.getArch() == null || arch.equals(update.getArch())) && Launcher.compareVersion(update.getVersion(), launcherVersion) > 0 && (update.getMinimumVersion() == null || Launcher.compareVersion(launcherVersion, update.getMinimumVersion()) >= 0) && (newestUpdate == null || Launcher.compareVersion(update.getVersion(), newestUpdate.getVersion()) > 0)) {
                  log.info("Update {} is available", update.getVersion());
                  newestUpdate = update;
               }
            }

            return newestUpdate;
         } else {
            return null;
         }
      }
   }

   private static double installRollout() {
      try {
         BufferedReader reader = new BufferedReader(new FileReader("install_id.txt"));

         label30: {
            double var3;
            try {
               String line = reader.readLine();
               if (line == null) {
                  break label30;
               }

               line = line.trim();
               int i = Integer.parseInt(line);
               log.debug("Loaded install id {}", i);
               var3 = (double)i / 2.147483647E9;
            } catch (Throwable var6) {
               try {
                  reader.close();
               } catch (Throwable var5) {
                  var6.addSuppressed(var5);
               }

               throw var6;
            }

            reader.close();
            return var3;
         }

         reader.close();
      } catch (NumberFormatException | IOException var7) {
         Exception ex = var7;
         log.warn("unable to get install rollout", ex);
      }

      return Math.random();
   }

   private static void delete(Path directory) throws IOException {
      Files.walkFileTree(directory, new SimpleFileVisitor<Path>() {
         public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
            Files.delete(file);
            return FileVisitResult.CONTINUE;
         }

         public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
            Files.delete(dir);
            return FileVisitResult.CONTINUE;
         }
      });
   }

   private static void copy(final Path source, final Path target, final CopyOption... options) throws IOException {
      Files.walkFileTree(source, new SimpleFileVisitor<Path>() {
         public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
            Files.createDirectories(target.resolve(source.relativize(dir).toString()));
            return FileVisitResult.CONTINUE;
         }

         public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
            Files.copy(file, target.resolve(source.relativize(file).toString()), options);
            return FileVisitResult.CONTINUE;
         }
      });
   }
}
