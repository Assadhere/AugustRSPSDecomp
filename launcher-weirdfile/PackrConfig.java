package net.runelite.launcher;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Map;
import net.runelite.launcher.beans.Bootstrap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class PackrConfig {
   private static final Logger log = LoggerFactory.getLogger(PackrConfig.class);

   static void updateLauncherArgs(Bootstrap bootstrap) {
      OS.OSType os = OS.getOs();
      if (os == OS.OSType.Windows || os == OS.OSType.MacOS) {
         File configFile = (new File("config.json")).getAbsoluteFile();
         if (configFile.exists() && configFile.canWrite()) {
            Gson gson = (new GsonBuilder()).setPrettyPrinting().create();

            Map config;
            try {
               FileInputStream fin = new FileInputStream(configFile);

               try {
                  config = (Map)gson.fromJson(new InputStreamReader(fin), Map.class);
               } catch (Throwable var18) {
                  try {
                     fin.close();
                  } catch (Throwable var12) {
                     var18.addSuppressed(var12);
                  }

                  throw var18;
               }

               fin.close();
            } catch (JsonIOException | JsonSyntaxException | IOException var19) {
               Exception e = var19;
               log.warn("error deserializing packr vm args!", e);
               return;
            }

            if (config == null) {
               log.warn("packr config is null!");
            } else {
               String[] argsArr = getArgs(bootstrap);
               if (argsArr != null && argsArr.length != 0) {
                  config.put("vmArgs", argsArr);
                  config.put("env", getEnv(bootstrap));

                  try {
                     File tmpFile = File.createTempFile("runelite", (String)null);
                     FileOutputStream fout = new FileOutputStream(tmpFile);

                     try {
                        FileChannel channel = fout.getChannel();

                        try {
                           OutputStreamWriter writer = new OutputStreamWriter(fout, StandardCharsets.UTF_8);

                           try {
                              channel.lock();
                              gson.toJson(config, writer);
                              writer.flush();
                              channel.force(true);
                           } catch (Throwable var17) {
                              try {
                                 writer.close();
                              } catch (Throwable var15) {
                                 var17.addSuppressed(var15);
                              }

                              throw var17;
                           }

                           writer.close();
                        } catch (Throwable var20) {
                           if (channel != null) {
                              try {
                                 channel.close();
                              } catch (Throwable var14) {
                                 var20.addSuppressed(var14);
                              }
                           }

                           throw var20;
                        }

                        if (channel != null) {
                           channel.close();
                        }
                     } catch (Throwable var21) {
                        try {
                           fout.close();
                        } catch (Throwable var13) {
                           var21.addSuppressed(var13);
                        }

                        throw var21;
                     }

                     fout.close();

                     try {
                        Files.move(tmpFile.toPath(), configFile.toPath(), StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
                     } catch (AtomicMoveNotSupportedException var16) {
                        AtomicMoveNotSupportedException ex = var16;
                        log.debug("atomic move not supported", ex);
                        Files.move(tmpFile.toPath(), configFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                     }
                  } catch (IOException var22) {
                     IOException e = var22;
                     log.warn("error updating packr vm args!", e);
                  }

               } else {
                  log.warn("Launcher args are empty");
               }
            }
         }
      }
   }

   private static String[] getArgs(Bootstrap bootstrap) {
      return Launcher.isJava17() ? getArgsJvm17(bootstrap) : getArgsJvm11(bootstrap);
   }

   private static String[] getArgsJvm17(Bootstrap bootstrap) {
      String[] args;
      switch (OS.getOs()) {
         case Windows:
            args = bootstrap.getLauncherJvm17WindowsArguments();
            return args != null ? args : bootstrap.getLauncherJvm17Arguments();
         case MacOS:
            args = bootstrap.getLauncherJvm17MacArguments();
            return args != null ? args : bootstrap.getLauncherJvm17Arguments();
         default:
            return bootstrap.getLauncherJvm17Arguments();
      }
   }

   private static String[] getArgsJvm11(Bootstrap bootstrap) {
      String[] args;
      switch (OS.getOs()) {
         case Windows:
            args = bootstrap.getLauncherJvm11WindowsArguments();
            return args != null ? args : bootstrap.getLauncherJvm11Arguments();
         case MacOS:
            args = bootstrap.getLauncherJvm11MacArguments();
            return args != null ? args : bootstrap.getLauncherJvm11Arguments();
         default:
            return bootstrap.getLauncherJvm11Arguments();
      }
   }

   private static Map<String, String> getEnv(Bootstrap bootstrap) {
      switch (OS.getOs()) {
         case Windows:
            return bootstrap.getLauncherWindowsEnv();
         case MacOS:
            return bootstrap.getLauncherMacEnv();
         case Linux:
            return bootstrap.getLauncherLinuxEnv();
         default:
            return null;
      }
   }
}
